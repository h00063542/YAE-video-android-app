package com.yilos.nailstar.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangdan on 15/10/16.
 */
public class HttpClient {
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private static final String serviceBaseUrl = "http://api2.naildaka.com";

    static{
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
    }

    /**
     * 获取Json数据
     * @param url url地址
     * @return
     */
    public static String getJson(String url) throws IOException {
        Request request = new Request.Builder()
                .url(serviceBaseUrl + url)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }

        return response.body().string();
    }
}
