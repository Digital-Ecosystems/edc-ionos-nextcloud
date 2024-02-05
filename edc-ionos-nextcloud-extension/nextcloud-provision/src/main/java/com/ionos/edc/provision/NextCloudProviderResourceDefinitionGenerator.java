package com.ionos.edc.provision;

import com.ionos.edc.schema.NextcloudSchema;
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

        var id = randomUUID().toString();

        return  NextCloudResourceDefinition.Builder.newInstance()
                .id(id)
                .DataAddress(dataAddress)
                .DataRequest(dataRequest)
                .build();
    }

    @Override
    public boolean canGenerate(DataRequest dataRequest, DataAddress dataAddress, Policy policy) {
        Objects.requireNonNull(dataRequest, "dataRequest must always be provided");
        Objects.requireNonNull(dataAddress, "dataAddress must always be provided");
        Objects.requireNonNull(policy, "policy must always be provided");

        return NextcloudSchema.TYPE.equals(dataRequest.getDestinationType());
    }
}
