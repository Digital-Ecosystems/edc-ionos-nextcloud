package com.ionos.edc.provision;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ionos.edc.http.HttpParts;
import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.schema.NextcloudSchema;
import com.ionos.edc.token.NextCloudToken;
import dev.failsafe.RetryPolicy;

import org.eclipse.edc.connector.contract.spi.offer.store.ContractDefinitionStore;
import org.eclipse.edc.connector.transfer.spi.provision.Provisioner;
import org.eclipse.edc.connector.transfer.spi.types.*;
import org.eclipse.edc.policy.model.AtomicConstraint;
import org.eclipse.edc.policy.model.Constraint;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.response.ResponseStatus;
import org.eclipse.edc.spi.response.StatusResult;
import org.eclipse.edc.spi.http.EdcHttpClient;


import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.concurrent.CompletableFuture;



public class NextCloudProvisioner  implements Provisioner<NextCloudResourceDefinition, NextCloudProvisionedResource> {
    private static final MediaType JSON = MediaType.get("application/json");
    private static final String TAG = "http://www.w3.org/ns/odrl/2/assignee";
    private RetryPolicy<Object> retryPolicy;
    private Monitor monitor;

    private NextCloudApi nextCloudApi;

    private final EdcHttpClient httpClient;

    private final ObjectMapper mapper;

    private String authKey;
    private String endpoint = "/nextcloudtransfer";

    public NextCloudProvisioner(RetryPolicy<Object> retryPolicy, Monitor monitor, NextCloudApi nextCloudApi, EdcHttpClient httpClient, ObjectMapper mapper, String authKey) {
        this.retryPolicy = retryPolicy;
        this.monitor = monitor;
        this.nextCloudApi = nextCloudApi;
        this.httpClient = httpClient;
        this.mapper = mapper;
        this.authKey = authKey;

    }

    @Override
    public boolean canProvision(ResourceDefinition resourceDefinition) {
        return resourceDefinition instanceof NextCloudResourceDefinition;
    }

    @Override
    public boolean canDeprovision(ProvisionedResource resourceDefinition) {
        return resourceDefinition instanceof NextCloudProvisionedResource;
    }

    @Override
    public CompletableFuture<StatusResult<ProvisionResponse>> provision(NextCloudResourceDefinition resourceDefinition, Policy policy) {
        String fileNameDest = resourceDefinition.getDataRequest().getDataDestination().getStringProperty(NextcloudSchema.FILE_NAME);
        String filePathDest = resourceDefinition.getDataRequest().getDataDestination().getStringProperty(NextcloudSchema.FILE_PATH);
        String filePath = resourceDefinition.getDataAddress().getStringProperty(NextcloudSchema.FILE_PATH);
        String fileName = resourceDefinition.getDataAddress().getStringProperty(NextcloudSchema.FILE_NAME);

        String resourceName = resourceDefinition.getDataAddress().getKeyName();

        AtomicConstraint ct = (AtomicConstraint) policy.getProhibitions().get(0).getConstraints().get(0);
        if (policy.getProhibitions().size() == 0) {
            throw new EdcException("No Prohibition available");
        }
        if (Boolean.parseBoolean(ct.getRightExpression().toString().replace("'", ""))) {

            var resourceBuilder = NextCloudProvisionedResource.Builder.newInstance()
                    .id(resourceDefinition.getId())
                    .resourceName(resourceName)
                    .filePath(filePathDest)
                    .fileName(fileNameDest)
                    .urlKey(filePathDest + fileNameDest + resourceDefinition.getId())
                    .resourceDefinitionId(resourceDefinition.getId())
                    .transferProcessId(resourceDefinition.getTransferProcessId())
                    .hasToken(true);


            var resource = resourceBuilder.build();
            try {
                var urlKey = "";
                if (resourceDefinition.getDataAddress().getType().equals(NextcloudSchema.TYPE)) {
                    urlKey = nextCloudApi.generateUrlDownload(filePath, fileName);
                }
                var expiryTime = OffsetDateTime.now().plusHours(1);
                var urlToken = new NextCloudToken(urlKey, true, expiryTime.toInstant().toEpochMilli());

                Request request;
                try {
                    if (resourceDefinition.getDataRequest().getDataDestination().getStringProperty(NextcloudSchema.HTTP_RECEIVER) == null) {
                        throw new EdcException("No receiverHttpEndpoint available");
                    }

                    request = createRequest(resourceDefinition, urlToken, policy);
                } catch (JsonProcessingException e) {
                    monitor.severe("Error serializing: ", e);
                    return CompletableFuture.completedFuture(StatusResult.failure(ResponseStatus.FATAL_ERROR, "Fatal error serializing request: " + e.getMessage()));
                }

                try (var response = httpClient.execute(request)) {

                    if (response.isSuccessful()) {
                        return CompletableFuture.completedFuture(StatusResult.success(ProvisionResponse.Builder.newInstance().inProcess(true).build()));
                    } else if (response.code() >= 500 && response.code() <= 504) {
                        // retry
                        return CompletableFuture.completedFuture(StatusResult.failure(ResponseStatus.ERROR_RETRY, "HttpProviderProvisioner: received error code: " + response.code()));
                    } else {
                        // fatal error
                        return CompletableFuture.completedFuture(StatusResult.failure(ResponseStatus.FATAL_ERROR, "HttpProviderProvisioner: received fatal error code: " + response.code()));
                    }
                }

            } catch (Exception e) {
                monitor.severe("Error provisioning", e);
                return CompletableFuture.completedFuture(StatusResult.failure(ResponseStatus.FATAL_ERROR, e.getMessage()));
            }
        } else {
            var resourceBuilder = NextCloudProvisionedResource.Builder.newInstance()
                    .id(resourceDefinition.getId())
                    .resourceName(resourceName)
                    .filePath(filePath)
                    .fileName(fileName)
                    .urlKey(filePath + fileName + resourceDefinition.getId())
                    .resourceDefinitionId(resourceDefinition.getId())
                    .transferProcessId(resourceDefinition.getTransferProcessId())

                    .hasToken(true);

            var resource = resourceBuilder.build();
            var expiryTime = OffsetDateTime.now().plusHours(1);
            var urlToken = new NextCloudToken("", false, expiryTime.toInstant().toEpochMilli());

            var response = ProvisionResponse.Builder.newInstance().resource(resource).inProcess(true).secretToken(urlToken).build();
            try {



                if (policy.getObligations().size() == 0 ) {
                    throw new EdcException("Expiration time not available");
                }
                if (policy.getProhibitions().size() == 0) {
                    throw new EdcException("Prohibition not available");
                }
                if(policy.getPermissions().size() == 0){
                    throw new EdcException("Permission not available");
                }



                nextCloudApi.fileShare(filePath,
                        fileName,
                        getAtomicConstraint(policy.getPermissions().get(0).getConstraints(),"shareWith"),
                        getAtomicConstraint(policy.getPermissions().get(0).getConstraints(),"shareType"),
                        getAtomicConstraint(policy.getObligations().get(0).getConstraints(),"permissionType"),
                        nowDate(Integer.parseInt(getAtomicConstraint(policy.getObligations().get(0).getConstraints(),"expirationTime"))));
            } catch (Exception e) {
                monitor.severe("Error sharing file, cause: ", e);
                return CompletableFuture.completedFuture(StatusResult.failure(ResponseStatus.FATAL_ERROR, e.getMessage()));
            }
            return CompletableFuture.completedFuture(StatusResult.success(response));

        }
    }

    @Override
    public CompletableFuture<StatusResult<DeprovisionedResource>> deprovision(NextCloudProvisionedResource provisionedResource, Policy policy) {
        return CompletableFuture.completedFuture(StatusResult.success(DeprovisionedResource.Builder.newInstance().provisionedResourceId(provisionedResource.getId()).build()));
    }


    private Request createRequest(NextCloudResourceDefinition resourceDefinition, NextCloudToken url, Policy policy) throws JsonProcessingException {
        var provisionerRequest = HttpParts.Builder.newInstance()
                .dataAddress(resourceDefinition.getDataAddress())
                .dataRequest(resourceDefinition.getDataRequest())
                .assetId(resourceDefinition.getDataRequest().getAssetId())
                .transferProcessId(resourceDefinition.getTransferProcessId())
                .resourceDefinitionId(resourceDefinition.getId())
                .policy(policy)
                .url(url)
                .build();
        var requestBody = RequestBody.create(mapper.writeValueAsString(provisionerRequest), JSON);
        return new Request.Builder()
                .addHeader("X-API-Key", "%s".formatted(authKey))
                .url(resourceDefinition
                        .getDataRequest()
                        .getDataDestination()
                        .getStringProperty(NextcloudSchema.HTTP_RECEIVER) + endpoint)
                .post(requestBody).build();
    }


    private String nowDate(int plusDays) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursLater = now.plusDays(plusDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = twoHoursLater.format(formatter);
        return formattedDate;
    }

    private String getAtomicConstraint(List<Constraint> ct, String tag) {
        for(int i=0; i<ct.size(); i++){
            AtomicConstraint ctToValidate = (AtomicConstraint)  ct.get(i);
            if(ctToValidate.getLeftExpression().toString().replace("'", "").equals(tag)){
                return ctToValidate.getRightExpression().toString().replace("'", "");
            }
        }
        return "";
        }

}
