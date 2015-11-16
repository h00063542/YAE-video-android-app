package com.yilos.nailstar.download;

/**
 * Created by yilos on 15/11/11.
 */
public interface ProgressListener {
    /**
     * 监听下载进度
     *
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    void update(long bytesRead, long contentLength, boolean done);
}
