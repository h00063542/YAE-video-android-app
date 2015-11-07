package com.yilos.nailstar.util;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
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
}
