package com.baizhi.rpc;

import java.io.Serializable;

public class Response implements Serializable{
    private Object returnValue;
    private RuntimeException runtimeException;

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public RuntimeException getRuntimeException() {
        return runtimeException;
    }

    public void setRuntimeException(RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }

    public Response() {
    }

    public Response(Object returnValue, RuntimeException runtimeException) {
        this.returnValue = returnValue;
        this.runtimeException = runtimeException;
    }
}
