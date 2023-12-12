package com.ionos.edc.dataplane;

import com.ionos.edc.nextcloudapi.NextCloudApi;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.error;
import static org.eclipse.edc.connector.dataplane.spi.pipeline.StreamResult.success;
public class NextCloudDataSource implements DataSource {
    private String fileName;
    private NextCloudApi nextCloudApi;

    @Override
    public StreamResult<Stream<Part>> openPartStream() {
        return success(Stream.of(new nextCloudPart(nextCloudApi, fileName)));
    }

    @Override
    public void close() throws Exception {

    }

    private static class nextCloudPart  implements Part {
        private final NextCloudApi nextCloudApi;
        private String fileName;

        public nextCloudPart(NextCloudApi nextCloudApi, String fileName) {
            this.nextCloudApi = nextCloudApi;
            this.fileName = fileName;
        }
        @Override
        public String name() {
            return fileName;
        }

        @Override
        public StreamResult<Stream<Part>> openStream() {
            InputStream targetStream = new ByteArrayInputStream("");
            return success(Stream.of(new HttpPart(name, body.byteStream())));
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


        public Builder client(NextCloudApi nextCloudApi) {
            source.nextCloudApi = nextCloudApi;
            return this;
        }

        public NextCloudDataSource build() {
            return source;
        }

    }

    private static class HttpPart implements Part {
        private final String name;
        private final InputStream content;

        HttpPart(String name, InputStream content) {
            this.name = name;
            this.content = content;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public long size() {
            return SIZE_UNKNOWN;
        }

        @Override
        public InputStream openStream() {
            return content;
        }

    }
}