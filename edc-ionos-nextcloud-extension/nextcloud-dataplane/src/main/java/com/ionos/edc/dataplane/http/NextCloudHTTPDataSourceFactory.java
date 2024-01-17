package com.ionos.edc.dataplane.http;

import com.ionos.edc.dataplane.validation.NextCloudDataAddressValidator;
import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.schema.NextcloudSchema;
import com.ionos.edc.token.NextCloudToken;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSourceFactory;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.spi.types.domain.DataAddress;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowRequest;
import org.eclipse.edc.validator.spi.Validator;
import org.jetbrains.annotations.NotNull;

public class NextCloudHTTPDataSourceFactory implements DataSourceFactory {
    private final Validator<DataAddress> validation = new NextCloudDataAddressValidator();
    private NextCloudApi nextCloudApi;
    private final Vault vault;
    private final TypeManager typeManager;

    public NextCloudHTTPDataSourceFactory(NextCloudApi nextCloudApi, TypeManager typeManager, Vault vault ) {
        this.nextCloudApi = nextCloudApi;
        this.typeManager = typeManager;
        this.vault = vault;
    }

    @Override
    public boolean canHandle(DataFlowRequest request) {
        return NextcloudSchema.TYPE.equals(request.getSourceDataAddress().getType());
    }

    @Override
    public @NotNull Result<Void> validateRequest(DataFlowRequest request) {
        var source = request.getSourceDataAddress();
        return validation.validate(source).toResult();
    }

    @Override
    public DataSource createSource(DataFlowRequest request) {


        var source = request.getSourceDataAddress();
        var destination = request.getDestinationDataAddress();

        var secret = vault.resolveSecret(destination.getKeyName());

        if (secret != null && secret.contains("dataspaceconnector:nexcloudtoken")) {
            var token = typeManager.readValue(secret, NextCloudToken.class);

            return NextCloudHTTPDataSource.Builder.newInstance().client(nextCloudApi)
                        .filePath(source.getStringProperty(NextcloudSchema.FILE_PATH))
                        .fileName(source.getStringProperty(NextcloudSchema.FILE_NAME))
                        .downloadable(token.getDownloadable())
                        .url(token.getUrlToken())
                        .build();


        }else if(secret != null){
            var urlKey = nextCloudApi.generateUrlDownload(source.getStringProperty(NextcloudSchema.FILE_PATH), source.getStringProperty(NextcloudSchema.FILE_NAME));


            return NextCloudHTTPDataSource.Builder.newInstance().client(nextCloudApi)
                    .filePath(source.getStringProperty(NextcloudSchema.FILE_PATH))
                    .fileName(source.getStringProperty(NextcloudSchema.FILE_NAME))
                    .downloadable(true)
                    .url(urlKey)
                    .build();
        }else{
            return NextCloudHTTPDataSource.Builder.newInstance().client(nextCloudApi)
                    .filePath(source.getStringProperty(NextcloudSchema.FILE_PATH))
                    .fileName(source.getStringProperty(NextcloudSchema.FILE_NAME))
                    .downloadable(false)
                    .build();
        }

    }

    }

