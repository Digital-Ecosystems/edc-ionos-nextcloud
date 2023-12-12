package com.ionos.edc.dataplane;

import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.schema.NextcloudSchema;

import org.eclipse.edc.connector.dataplane.spi.client.DataPlaneClient;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSource;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSourceFactory;
import org.eclipse.edc.connector.transfer.spi.callback.ControlPlaneApiUrl;
import org.eclipse.edc.connector.transfer.spi.flow.DataFlowController;
import org.eclipse.edc.connector.transfer.spi.types.DataFlowResponse;
import org.eclipse.edc.connector.transfer.spi.types.DataRequest;
import org.eclipse.edc.connector.dataplane.util.validation.ValidationRule;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.spi.types.domain.DataAddress;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowRequest;
import org.jetbrains.annotations.NotNull;

public class NextCloudDataSourceFactory  implements DataSourceFactory {
    private NextCloudApi nextCloudApi;

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
    public  @NotNull Result<Void> validate(DataFlowRequest request) {
        var source = request.getSourceDataAddress();
        return  validation.apply(source).map(it -> true);
    }

    @Override
    public @NotNull Result<Void> validateRequest(DataFlowRequest request) {
        var source = request.getSourceDataAddress();
        return validation.apply(source).map(it -> null);
    }

    @Override
    public DataSource createSource(DataFlowRequest request) {


        var source = request.getSourceDataAddress();

        return NextCloudDataSource.Builder.newInstance().client(nextCloudApi)
                .fileName(source.getStringProperty(NextcloudSchema.OBJECT_NAME))
                .build();
    }

}
}
