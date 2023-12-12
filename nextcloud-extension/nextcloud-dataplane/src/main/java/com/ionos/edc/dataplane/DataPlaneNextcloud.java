package com.ionos.edc.dataplane;

import com.ionos.edc.nextcloudapi.NextCloudApi;
import org.eclipse.edc.connector.dataplane.spi.pipeline.DataTransferExecutorServiceContainer;
import org.eclipse.edc.connector.dataplane.spi.pipeline.PipelineService;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.types.TypeManager;

@Extension(value = DataPlaneNextcloud.NAME)
public class DataPlaneNextcloud  implements ServiceExtension {

    public static final String NAME = "Data Plane NextCloud";
    @Inject
    private PipelineService pipelineService;

    @Inject
    private DataTransferExecutorServiceContainer executorContainer;

    @Inject
    private Vault vault;

    @Inject
    private TypeManager typeManager;

    private NextCloudApi nextCloudApi;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {

        var monitor = context.getMonitor();

        var sourceFactory = new  NextCloudDataSourceFactory(nextCloudApi, typeManager);
        pipelineService.registerFactory(sourceFactory);

        var sinkFactory = new NextCloudDataSinkFactory(executorContainer.getExecutorService(), monitor, vault,
                typeManager, nextCloudApi);


        pipelineService.registerFactory(sinkFactory);
        context.getMonitor().info(NAME+ " Extension initialized!");
    }
}