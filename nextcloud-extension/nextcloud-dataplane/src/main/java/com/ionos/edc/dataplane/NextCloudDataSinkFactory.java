package com.ionos.edc.dataplane;
import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.schema.NextcloudSchema;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSink;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataSinkFactory;
import org.eclipse.edc.connector.dataplane.util.validation.ValidationRule;
import org.eclipse.edc.spi.EdcException;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowRequest;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;

public class NextCloudDataSinkFactory  implements DataSinkFactory {
    private final ExecutorService executorService;
    private final Monitor monitor;
    private Vault vault;
    private TypeManager typeManager;

    private NextCloudApi nextCloudApi;

    public NextCloudDataSinkFactory(@NotNull ExecutorService executorService, Monitor monitor, Vault vault, TypeManager typeManager, NextCloudApi nextCloudApi) {
        this.executorService = executorService;
        this.monitor = monitor;
        this.vault = vault;
        this.typeManager = typeManager;
        this.nextCloudApi = nextCloudApi;
    }

    @Override
    public boolean canHandle(DataFlowRequest request) {
        return NextcloudSchema.TYPE.equals(request.getDestinationDataAddress().getType());
    }

    @Override
    public @NotNull Result<Void> validateRequest(DataFlowRequest request) {
        var destination = request.getDestinationDataAddress();


        return validation.apply(destination).map(it -> null);
    }

    @Override
    public DataSink createSink(DataFlowRequest request) {
        var validationResult = validateRequest(request);
        if (validationResult.failed()) {
            throw new EdcException(String.join(", ", validationResult.getFailureMessages()));
        }
        var destination = request.getDestinationDataAddress();;




        return NextCloudDataSink.Builder.newInstance().fileName(destination.getStringProperty(NextcloudSchema.OBJECT_NAME))
                .requestId(request.getId()).executorService(executorService)
                .monitor(monitor).nextCloudApi(nextCloudApi).build();
    }

        }



}