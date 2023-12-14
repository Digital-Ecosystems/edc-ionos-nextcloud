package com.ionos.edc.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.eclipse.edc.connector.transfer.spi.types.SecretToken;

@JsonTypeName("dataspaceconnector:nexcloudtoken")
public class NextCloudToken implements SecretToken {
    private final String urlToken;
    private final long expiration;

    public NextCloudToken(@JsonProperty("urlKey") String urlToken,@JsonProperty("expiration") long expiration) {
        this.urlToken = urlToken;
        this.expiration = expiration;
    }

    public String getUrlToken() {
        return urlToken;
    }

    @Override
    public long getExpiration() {
        return expiration;
    }
}
