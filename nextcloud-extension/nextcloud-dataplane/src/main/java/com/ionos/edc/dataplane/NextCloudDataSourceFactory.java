package com.ionos.edc.dataplane;

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

public class NextCloudDataSourceFactory  implements DataSourceFactory {
    private final Validator<DataAddress> validation = new NextCloudDataAddressValidator();
    private NextCloudApi nextCloudApi;
    private Vault vault;
    private final TypeManager typeManager;

    public NextCloudDataSourceFactory(NextCloudApi nextCloudApi, TypeManager typeManager) {
        this.nextCloudApi = nextCloudApi;
        this.typeManager = typeManager;
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

        if (secret != null) {
            var token = typeManager.readValue(secret, NextCloudToken.class);

            return NextCloudDataSource.Builder.newInstance().client(nextCloudApi)
                    .filePath(source.getStringProperty(NextcloudSchema.FILE_PATH))
                    .fileName(source.getStringProperty(NextcloudSchema.FILE_NAME))
                    .url(token.getUrlToken())
                    .build();
        } else {
            return null;
        }

    }

    }

