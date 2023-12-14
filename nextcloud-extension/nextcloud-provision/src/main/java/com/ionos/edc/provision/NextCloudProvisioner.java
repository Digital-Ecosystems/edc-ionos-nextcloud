package com.ionos.edc.provision;

import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.token.NextCloudToken;
import dev.failsafe.RetryPolicy;
import org.eclipse.edc.connector.transfer.spi.provision.Provisioner;
import org.eclipse.edc.connector.transfer.spi.types.DeprovisionedResource;
import org.eclipse.edc.connector.transfer.spi.types.ProvisionResponse;
import org.eclipse.edc.connector.transfer.spi.types.ProvisionedResource;
import org.eclipse.edc.connector.transfer.spi.types.ResourceDefinition;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.response.StatusResult;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

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

            var urlKey = nextCloudApi.generateUrlDownload(filePath,fileName);


            var resourceBuilder = NextCloudProvisionedResource.Builder.newInstance()
                    .id(resourceDefinition.getId())
                    .filePath(filePath)
                    .fileName(fileName)
                    .urlKey(filePath+fileName+resourceDefinition.getId())
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
            var urlToken = new NextCloudToken(urlKey, expiryTime.toInstant().toEpochMilli() );
            var response = ProvisionResponse.Builder.newInstance().resource(resource).secretToken(urlToken).build();

            return CompletableFuture.completedFuture(StatusResult.success(response));
    }

    @Override
    public CompletableFuture<StatusResult<DeprovisionedResource>> deprovision(NextCloudProvisionedResource provisionedResource, Policy policy) {
        return null;
    }


}