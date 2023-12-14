package com.ionos.edc.provision;

import java.util.Objects;
import org.eclipse.edc.connector.transfer.spi.types.ResourceDefinition;
public class NextCloudResourceDefinition extends ResourceDefinition {
    private String filePath;
    private String fileName;

    public NextCloudResourceDefinition() {

    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public Builder toBuilder() {
        return initializeBuilder(new Builder()).filePath(filePath).fileName(fileName);
    }
    public static class Builder extends ResourceDefinition.Builder<NextCloudResourceDefinition, Builder> {

        private Builder() {
            super(new NextCloudResourceDefinition());
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder filePath(String filePath) {
            resourceDefinition.filePath = filePath;
            return this;
        }
        public Builder fileName(String fileName) {
            resourceDefinition.fileName = fileName;
            return this;
        }


        @Override
        protected void verify() {
            super.verify();
            Objects.requireNonNull(resourceDefinition.getFilePath(), "file path is required");
            Objects.requireNonNull(resourceDefinition.getFileName(), "file Name is required");

        }
    }
}
