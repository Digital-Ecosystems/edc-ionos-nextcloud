package com.ionos.edc.nextcloudapi;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.*;


import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class NextCloudImpl implements NextCloudApi {
    private final String OC_URL = "url";
    private final String OC_FILEID = "oc|fileid";

    OkHttpClient client = new OkHttpClient();
    String basicUrl = "";
    String username = "";
    String password = "";

    public NextCloudImpl(String basicUrl, String username, String password) {
        this.basicUrl = basicUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void nextCloudApi(String url, String username, String password) {
        this.basicUrl = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public String generateUrlDownload(String filePath, String fileName) {
        String urlPart = "/ocs/v2.php/apps/dav/api/v1/direct?fileId=";
        String fileId = retrieveId(filePath, fileName);
        String url = basicUrl + urlPart + fileId;



        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIRequest", "true")
                .addHeader("Authorization", Credentials.basic(username, password))
                .post(RequestBody.create(null, new byte[0]))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String xmlResponse = response.body().string();
            Document document = Jsoup.parse(xmlResponse);
            String urlValue = document.select(OC_URL).text();

            return urlValue;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public byte[] downloadFile( String url) {



        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            InputStream in = response.body().byteStream();

            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(String filePath, String fileName, ByteArrayInputStream part) {
      //  PUT remote.php/dav/files/user/path/to/file

        String urlPart = "/remote.php/dav/files/" + username + "/";
        String url = basicUrl + urlPart +filePath+ "/" +fileName;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, RequestBody.create( part.readAllBytes()))

                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIRequest", "true")
                .addHeader("Authorization", Credentials.basic(username, password))
                .put(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String retrieveId(String path, String fileName) {
         basicUrl = "http://localhost:8080";

        String urlPart = "/remote.php/dav/files/" + username ;
        String url = basicUrl + urlPart + path+"/"+fileName;
        String body = "<?xml version=\"1.0\"?>\n" +
                "<d:propfind  xmlns:d=\"DAV:\" xmlns:oc=\"http://owncloud.org/ns\" xmlns:nc=\"http://nextcloud.org/ns\">\n" +
                "  <d:prop>\n" +
                "  <oc:fileid />\n" +
                "  </d:prop>\n" +
                "</d:propfind>";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIRequest", "true")
                .addHeader("Authorization", Credentials.basic(username, password))
                .method("PROPFIND", RequestBody.create(null, body))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String xmlResponse = response.body().string();
            Document document = Jsoup.parse(xmlResponse);
            String fileId = document.select(OC_FILEID).text();
            return fileId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}