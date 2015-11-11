package com.yilos.nailstar.download;

/**
 * Created by yilos on 15/11/11.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
