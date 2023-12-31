package com.ionos.edc.provision;

import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.token.NextCloudToken;
import dev.failsafe.RetryPolicy;
import org.eclipse.edc.connector.transfer.spi.provision.Provisioner;
import org.eclipse.edc.connector.transfer.spi.types.DeprovisionedResource;
import org.eclipse.edc.connector.transfer.spi.types.ProvisionResponse;
import org.eclipse.edc.connector.transfer.spi.types.ProvisionedResource;
import org.eclipse.edc.connector.transfer.spi.types.ResourceDefinition;
import org.eclipse.edc.policy.model.AtomicConstraint;
import org.eclipse.edc.policy.model.Constraint;
import org.eclipse.edc.policy.model.Expression;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.response.StatusResult;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import static dev.failsafe.Failsafe.with;

public class NextCloudProvisioner  implements Provisioner<NextCloudResourceDefinition, NextCloudProvisionedResource> {
    private  RetryPolicy<Object> retryPolicy;
    private  Monitor monitor;

    private NextCloudApi nextCloudApi;


    public NextCloudProvisioner(RetryPolicy<Object> retryPolicy, Monitor monitor, NextCloudApi nextCloudApi) {
        this.retryPolicy = retryPolicy;
        this.monitor = monitor;
        this.nextCloudApi = nextCloudApi;
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
        String fileName = resourceDefinition.getFileName();
        String filePath = resourceDefinition.getFilePath();
        String resourceName = resourceDefinition.getKeyName();

        AtomicConstraint ct = (AtomicConstraint) policy.getProhibitions().get(0).getConstraints().get(0);
        if(policy.getProhibitions().size() ==0 ){
             throw new EdcException("No Prohibition available");
        }
        if(Boolean.parseBoolean(ct.getRightExpression().toString().replace("'",""))) {

                var resourceBuilder = NextCloudProvisionedResource.Builder.newInstance()
                        .id(resourceDefinition.getId())
                        .resourceName(resourceName)
                        .filePath(filePath)
                        .fileName(fileName)
                        .urlKey(filePath + fileName + resourceDefinition.getId())
                        .resourceDefinitionId(resourceDefinition.getId())
                        .transferProcessId(resourceDefinition.getTransferProcessId())
                        .hasToken(true);
                if (resourceDefinition.getFilePath() != null) {
                    resourceBuilder = resourceBuilder.filePath(resourceDefinition.getFilePath());
                }
                if (resourceDefinition.getFileName() != null) {
                    resourceBuilder = resourceBuilder.fileName(resourceDefinition.getFileName());
                }

                var resource = resourceBuilder.build();
                var urlKey = nextCloudApi.generateUrlDownload("", fileName);
                var expiryTime = OffsetDateTime.now().plusHours(1);
                var urlToken = new NextCloudToken(urlKey,true ,expiryTime.toInstant().toEpochMilli());
                var response = ProvisionResponse.Builder.newInstance().resource(resource).secretToken(urlToken).build();

                return CompletableFuture.completedFuture(StatusResult.success(response));
            }else {
            var resourceBuilder = NextCloudProvisionedResource.Builder.newInstance()
                    .id(resourceDefinition.getId())
                    .resourceName(resourceName)
                    .filePath(filePath)
                    .fileName(fileName)
                    .urlKey(filePath + fileName + resourceDefinition.getId())
                    .resourceDefinitionId(resourceDefinition.getId())
                    .transferProcessId(resourceDefinition.getTransferProcessId())
                    .hasToken(true);
            if (resourceDefinition.getFilePath() != null) {
                resourceBuilder = resourceBuilder.filePath(resourceDefinition.getFilePath());
            }
            if (resourceDefinition.getFileName() != null) {
                resourceBuilder = resourceBuilder.fileName(resourceDefinition.getFileName());
            }
            var resource = resourceBuilder.build();
            var expiryTime = OffsetDateTime.now().plusHours(1);
            var urlToken = new NextCloudToken("",false ,expiryTime.toInstant().toEpochMilli());

            var response = ProvisionResponse.Builder.newInstance().resource(resource).secretToken(urlToken).build();

            nextCloudApi.fileShare(filePath,fileName,"user1","0");
            return CompletableFuture.completedFuture(StatusResult.success(response));

        }
    }

    @Override
    public CompletableFuture<StatusResult<DeprovisionedResource>> deprovision(NextCloudProvisionedResource provisionedResource, Policy policy) {
      return CompletableFuture.completedFuture(StatusResult.success(DeprovisionedResource.Builder.newInstance().provisionedResourceId(provisionedResource.getId()).build()));
    }


}