package com.yilos.nailstar.framework.entity;

/**
 * Created by yangdan on 15/11/11.
 */
public class CommonResult<T> {
    private boolean error;

    private String errorMsg;

    private T result;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
