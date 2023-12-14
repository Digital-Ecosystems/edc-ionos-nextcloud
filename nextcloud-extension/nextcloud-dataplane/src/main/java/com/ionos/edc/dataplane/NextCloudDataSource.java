package com.ionos.edc.dataplane;

import com.ionos.edc.nextcloudapi.NextCloudApi;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.error;
import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.success;
public class NextCloudDataSource implements DataSource {
    private String fileName;
    private String filePath;
    private String url;
    private NextCloudApi nextCloudApi;

    @Override
    public StreamResult<Stream<Part>> openPartStream() {
        return success(Stream.of(new nextCloudPart(nextCloudApi, fileName, filePath,url)));
    }

    @Override
    public void close() throws Exception {

    }

    private static class nextCloudPart  implements Part {
        private final NextCloudApi nextCloudApi;
        private String fileName;
        private String filePath;
        private String url;

        public nextCloudPart(NextCloudApi nextCloudApi, String fileName, String filePath, String url) {
            this.nextCloudApi = nextCloudApi;
            this.fileName = fileName;
            this.filePath = filePath;
            this.url = url;
        }
        @Override
        public String name() {
            return fileName;
        }

        @Override
        public InputStream openStream() {

            return new ByteArrayInputStream( nextCloudApi.downloadFile(url));
        }


    }
    public static class Builder {
        private final NextCloudDataSource source;
        public Builder fileName;


        private Builder() {
            source = new NextCloudDataSource();
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder fileName(String fileName) {
            source.fileName = fileName;
            return this;
        }
        public Builder filePath(String filePath) {
            source.filePath = filePath;
            return this;
        }
        public Builder url(String url) {
            source.url = url;
            return this;
        }
        public Builder client(NextCloudApi nextCloudApi) {
            source.nextCloudApi = nextCloudApi;
            return this;
        }

        public NextCloudDataSource build() {
            return source;
        }

    }


}