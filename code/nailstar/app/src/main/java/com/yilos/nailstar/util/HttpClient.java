package com.yilos.nailstar.util;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangdan on 15/10/16.
 */
public class HttpClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private static final String serviceBaseUrl = "http://api3.naildaka.com";

    static {
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
    }

    /**
     * 获取OkHttpClient实例
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 获取Json数据
     *
     * @param url url地址
     * @return
     */
    public static String getJson(String url) throws IOException {
        Request request = new Request.Builder()
                .url(serviceBaseUrl + url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return response.body().string();
    }

    /**
     * 以json格式提交post请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(serviceBaseUrl + url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    /**
     * 以键值对格式提交post请求
     *
     * @param url
     * @param formBody 例: RequestBody formBody = new FormEncodingBuilder().add("search", "Jurassic Park").build();
     * @return
     * @throws IOException
     */
    public static String post(String url, RequestBody formBody) throws IOException {
        Request request = new Request.Builder()
                .url(serviceBaseUrl + url)
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body().string();
    }

    public static void download(String url, String filePath) throws IOException {
        Request request = new Request.Builder().url(url)
//                .addHeader("X-CSRFToken", csrftoken)
                .addHeader("Content-Type", "application/json").build();
        Response response = okHttpClient.newCall(request).execute();

        InputStream in = response.body().byteStream();
        FileOutputStream out = new FileOutputStream(filePath);
        byte b[] = new byte[1024];
        int j = 0;
        while ((j = in.read(b)) != -1) {
            out.write(b, 0, j);
        }
    }
}
