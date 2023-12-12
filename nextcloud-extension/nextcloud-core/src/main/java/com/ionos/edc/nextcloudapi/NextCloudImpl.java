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

public class NextCloudImpl implements NextCloudApi{
    OkHttpClient client = new OkHttpClient();
    String basicUrl = "";
    String username = "";
    String password = "";
    @Override
    public void nextCloudApi(String url, String username, String password) {
       this.basicUrl = url;
       this.username = username;
       this.password = password;
    }

    @Override
    public String generateUrlDownload(String objectName) {
        String urlPart = "/ocs/v2.php/apps/dav/api/v1/direct?fileId=";
        String fileId = retrieveId(objectName);
        String url = basicUrl + urlPart + fileId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("OCS-APIRequest", "true")
                .addHeader("Authorization", Credentials.basic(username,password))
                .post(RequestBody.create(null, new byte[0]))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String xmlResponse = response.body().string();
            Document document = Jsoup.parse(xmlResponse);
            String urlValue = document.select("url").text();

            return urlValue;


        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public byte[] downloadFile(String path) {

        String url = basicUrl + path;

       Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            InputStream in = response.body().byteStream();
            System.out.println("PL " + in.readAllBytes());

			return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(String objectName, ByteArrayInputStream part) {

    }

    private String retrieveId(String objectName){

        return "";
    }
}