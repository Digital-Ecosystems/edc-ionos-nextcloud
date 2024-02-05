package com.ionos.edc.nextcloudapi;

import java.io.ByteArrayInputStream;
import org.eclipse.edc.runtime.metamodel.annotation.ExtensionPoint;

@ExtensionPoint
public interface NextCloudApi {
    void nextCloudApi(String url, String username, String password);

    String generateUrlDownload(String filePath,String fileName);

    byte[] downloadFile( String url);

    void uploadFile(String filePath, String fileName, ByteArrayInputStream part);

    void fileShare(String filePath, String fileName, String shareWith, String shareType, String permissionType, String expireDate);
}
