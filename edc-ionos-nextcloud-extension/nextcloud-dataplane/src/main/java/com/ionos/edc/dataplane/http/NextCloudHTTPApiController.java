package com.ionos.edc.dataplane.http;

import com.ionos.edc.http.HttpParts;
import com.ionos.edc.nextcloudapi.NextCloudApi;
import com.ionos.edc.schema.NextcloudSchema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.edc.spi.monitor.Monitor;

import java.util.concurrent.ExecutorService;
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path(NextCloudHTTPApiController.PATH)
public class NextCloudHTTPApiController implements NextCloudHTTPApi{
    public static final String PATH = "/nextcloudtransfer";
    private NextCloudApi nextCloudApi;
    private final ExecutorService executorService;
    private Monitor monitor;
    public NextCloudHTTPApiController(NextCloudApi nextCloudApi, ExecutorService executorService, Monitor monitor) {
        this.nextCloudApi = nextCloudApi;
        this.executorService = executorService;
        this.monitor = monitor;
    }

    @POST
    @Override
    public void startTransferProcess(@RequestBody HttpParts httpParts) {
        var dataSource = NextCloudHTTPDataSource.Builder.newInstance()
                .client(nextCloudApi)
                .filePath(httpParts.getDataAddress().getStringProperty(NextcloudSchema.FILE_PATH))
                .fileName(httpParts.getDataAddress().getStringProperty(NextcloudSchema.FILE_NAME))
                .downloadable(true)
                .url(httpParts.getUrl().getUrlToken())
                .build();
        var dataSink=  NextCloudHTTPDataSink.Builder.newInstance()
                .filePath(httpParts.getDataRequest().getDataDestination().getStringProperty(NextcloudSchema.FILE_PATH))
                .fileName(httpParts.getDataRequest().getDataDestination().getStringProperty(NextcloudSchema.FILE_NAME))
                .downloadable(true)
                .requestId(httpParts.getDataRequest().getId()).executorService(executorService)
                .monitor(monitor).nextCloudApi(nextCloudApi).build();

            dataSink.transferParts(dataSource.openPartStreamPart().toList());




    }
}
