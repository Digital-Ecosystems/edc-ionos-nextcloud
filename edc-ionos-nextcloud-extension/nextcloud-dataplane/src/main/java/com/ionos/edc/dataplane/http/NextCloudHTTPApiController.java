package com.ionos.edc.dataplane.http;

import com.ionos.edc.http.HttpParts;
import com.ionos.edc.nextcloudapi.NextCloudApi;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.edc.connector.dataplane.spi.pipeline.PipelineService;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.types.TypeManager;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.types.domain.transfer.DataFlowRequest;
import java.util.concurrent.ExecutorService;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path(NextCloudHTTPApiController.PATH)
public class NextCloudHTTPApiController implements NextCloudHTTPApi{
    public static final String PATH = "/nextcloudtransfer";
    private NextCloudApi nextCloudApi;
    private final ExecutorService executorService;
    private Monitor monitor;

    private final PipelineService pipelineService;
    private final Vault vault;
    private TypeManager typeManager;

    public NextCloudHTTPApiController(NextCloudApi nextCloudApi, ExecutorService executorService, Monitor monitor, PipelineService pipelineService, TypeManager typeManager, Vault vault) {
        this.nextCloudApi = nextCloudApi;
        this.executorService = executorService;
        this.monitor = monitor;
        this.pipelineService = pipelineService;
        this.typeManager = typeManager;
        this.vault = vault;
    }

    @POST
    @Override
    public void startTransferProcess(@RequestBody HttpParts httpParts) {
        var secret = httpParts.getUrl();
        vault.storeSecret(httpParts.getDataRequest().getDataDestination().getKeyName(), typeManager.writeValueAsString(secret));

        var dataflow=  DataFlowRequest.Builder.newInstance().processId(httpParts.getDataRequest().getProcessId())
                .sourceDataAddress(httpParts.getDataAddress())
                .destinationDataAddress(httpParts.getDataRequest().getDataDestination())
                .build();


        pipelineService.transfer(dataflow);
    }
}
