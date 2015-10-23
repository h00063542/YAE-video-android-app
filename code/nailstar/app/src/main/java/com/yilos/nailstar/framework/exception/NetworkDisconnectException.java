package com.yilos.nailstar.framework.exception;

/**
 * Created by yangdan on 15/10/16.
 */
public class NetworkDisconnectException extends Exception {
    public NetworkDisconnectException(String detailMessage){
        super(detailMessage);
    }

    public NetworkDisconnectException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }
}
