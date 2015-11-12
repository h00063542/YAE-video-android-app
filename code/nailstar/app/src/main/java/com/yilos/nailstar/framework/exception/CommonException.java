package com.yilos.nailstar.framework.exception;

/**
 * Created by yangdan on 15/11/11.
 */
public class CommonException extends Exception {
    public CommonException(String detailMessage){
        super(detailMessage);
    }

    public CommonException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }
}
