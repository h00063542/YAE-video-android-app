package com.yilos.nailstar.download;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import com.yilos.nailstar.util.LoggerFactory;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by yilos on 15/11/11.
 */
public class DownLoadTask {

    private static Logger logger = LoggerFactory.getLogger(DownLoadTask.class);

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

    public String getUrl() {
        return url;
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

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        saveFile(response, path, fileName);

    }

    public void cancel() {

        client.cancel(url);

    }

    private void saveFile(Response response, String path, String fileName) throws IOException {
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

            long totalBytesRead = 0L;
            long totalBytes = response.body().contentLength();

            File file = new File(dir, fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                totalBytesRead += len != -1 ? len : 0;
                progressListener.update(totalBytesRead, totalBytes, false);
            }
            fos.flush();
            progressListener.update(totalBytesRead, totalBytes, true);

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("saveFile close InputStream failed", e);
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                logger.error("saveFile close FileOutputStream failed", e);
            }

        }
    }

}
