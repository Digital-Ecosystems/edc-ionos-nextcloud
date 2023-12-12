package com.ionos.edc.nextcloudapi;

import java.io.ByteArrayInputStream;
import org.eclipse.edc.runtime.metamodel.annotation.ExtensionPoint;
@ExtensionPoint
public interface NextCloudApi {
    void nextCloudApi(String url, String username, String password);

    String generateUrlDownload(String objectName);

    byte[] downloadFile(String fileId);

    void uploadFile( String objectName, ByteArrayInputStream part);


}
