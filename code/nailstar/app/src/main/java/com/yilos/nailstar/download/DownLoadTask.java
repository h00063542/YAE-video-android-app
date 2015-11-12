package com.yilos.nailstar.download;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by yilos on 15/11/11.
 */
public class DownLoadTask {

    private OkHttpClient client;
    private String url;
    private String path;
    private String fileName;
    private ProgressListener progressListener;

    public DownLoadTask(String url, String path, String fileName) {
        this.url = url;
        this.path = path;
        this.fileName = fileName;
    }

    public DownLoadTask(OkHttpClient client, String url, String path, String fileName) {
        this.client = client;
        this.url = url;
        this.path = path;
        this.fileName = fileName;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void run() throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .tag(url)
                .build();

        if (progressListener == null) {
            progressListener = new ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {

                }
            };
        }

        if (client == null) {
            client = new OkHttpClient();
        }

        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        saveFile(response, path, fileName);

    }

    public void cancel() {

        client.cancel(url);

    }

    private String saveFile(Response response, String path, String fileName) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();

            return file.getAbsolutePath();

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }

        }
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() throws IOException {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

}
