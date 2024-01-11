package com.ionos.edc.provision;

import com.ionos.edc.schema.NextcloudSchema;
import org.eclipse.edc.connector.transfer.spi.provision.ConsumerResourceDefinitionGenerator;
import org.eclipse.edc.connector.transfer.spi.provision.ProviderResourceDefinitionGenerator;
import org.eclipse.edc.connector.transfer.spi.types.DataRequest;
import org.eclipse.edc.connector.transfer.spi.types.ResourceDefinition;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.spi.types.domain.DataAddress;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.util.UUID.randomUUID;

public class NextCloudProviderResourceDefinitionGenerator implements ProviderResourceDefinitionGenerator {
    @Override
    public @Nullable ResourceDefinition generate(DataRequest dataRequest,DataAddress dataAddress, Policy policy) {
        Objects.requireNonNull(dataRequest, "dataRequest must always be provided");
        Objects.requireNonNull(policy, "policy must always be provided");

        var destination = dataRequest.getDataDestination();
        var id = randomUUID().toString();
        var keyName = destination.getKeyName();
        var filePath = destination.getStringProperty(NextcloudSchema.FILE_PATH);
        var fileName = destination.getStringProperty(NextcloudSchema.FILE_NAME);


        return  NextCloudResourceDefinition.Builder.newInstance()
                .id(id)
                .keyName(keyName)
                .filePath(filePath)
                .fileName(fileName)
                .build();
    }

    @Override
    public boolean canGenerate(DataRequest dataRequest, DataAddress dataAddress, Policy policy) {
        Objects.requireNonNull(dataRequest, "dataRequest must always be provided");
        Objects.requireNonNull(policy, "policy must always be provided");

        return NextcloudSchema.TYPE.equals(dataRequest.getDestinationType());
    }


}
