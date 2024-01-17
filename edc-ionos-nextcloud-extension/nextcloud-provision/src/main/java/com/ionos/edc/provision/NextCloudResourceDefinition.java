package com.ionos.edc.provision;

import java.util.Objects;

import com.ionos.edc.schema.NextcloudSchema;
import org.eclipse.edc.connector.transfer.spi.types.DataRequest;
import org.eclipse.edc.connector.transfer.spi.types.ResourceDefinition;
import org.eclipse.edc.spi.types.domain.DataAddress;

public class NextCloudResourceDefinition extends ResourceDefinition {
    private DataRequest dataRequest;
    private DataAddress dataAddress;


    public NextCloudResourceDefinition() {

    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }

    public DataAddress getDataAddress() {
        return dataAddress;
    }

    @Override
    public Builder toBuilder() {
        return initializeBuilder(new Builder()).DataRequest(dataRequest).DataAddress(dataAddress);
    }
    public static class Builder extends ResourceDefinition.Builder<NextCloudResourceDefinition, Builder> {

        private Builder() {
            super(new NextCloudResourceDefinition());
        }

        public static Builder newInstance() {
            return new Builder();
        }

       public Builder DataRequest(DataRequest dataRequest) {
            resourceDefinition.dataRequest = dataRequest;
            return this;
        }

        public Builder DataAddress(DataAddress dataAddress) {
            resourceDefinition.dataAddress = dataAddress;
            return this;
        }
        @Override
        protected void verify() {
            super.verify();
            Objects.requireNonNull(resourceDefinition.dataAddress.getKeyName(), "file path is required");
            Objects.requireNonNull(resourceDefinition.dataRequest.getAssetId(), "file Name is required");
            Objects.requireNonNull(resourceDefinition.dataRequest.getDataDestination(), "Destination is required");
            Objects.requireNonNull(resourceDefinition.dataRequest.getDataDestination().getStringProperty(NextcloudSchema.HTTP_RECEIVER), "Destination Http Receiver is required");
            Objects.requireNonNull(resourceDefinition.dataRequest.getDataDestination().getStringProperty(NextcloudSchema.FILE_PATH), "Destination file path is required");
            Objects.requireNonNull(resourceDefinition.dataRequest.getDataDestination().getStringProperty(NextcloudSchema.FILE_NAME), "Destination file name is required");

        }
    }
}
