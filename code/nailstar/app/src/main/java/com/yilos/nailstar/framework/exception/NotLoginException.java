package com.yilos.nailstar.framework.exception;

/**
 * Created by yangdan on 15/11/18.
 */
public class NotLoginException extends CommonException {
    public NotLoginException(String detailMessage) {
        super(detailMessage);
    }

    public NotLoginException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
