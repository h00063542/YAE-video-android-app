package com.yilos.nailstar.framework.exception;

/**
 * Created by yangdan on 15/11/18.
 */
public class NoWatchException extends CommonException {
    public NoWatchException(String detailMessage) {
        super(detailMessage);
    }

    public NoWatchException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
