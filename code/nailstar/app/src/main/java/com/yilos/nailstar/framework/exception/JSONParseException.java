package com.yilos.nailstar.framework.exception;

/**
 * Created by yangdan on 15/10/20.
 */
public class JSONParseException extends Exception {

    public JSONParseException(String detailMessage){
        super(detailMessage);
    }

    public JSONParseException(String detailMessage, Throwable throwable){
        super(detailMessage, throwable);
    }
}
