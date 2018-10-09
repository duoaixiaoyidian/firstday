package com.baizhi.rpc;

public class MethodInvokeMeta {
    private Class<?> targetInterface;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public MethodInvokeMeta() {
    }

    public MethodInvokeMeta(Class<?> targetInterface, String method, Class<?>[] parameterTypes, Object[] args) {
        this.targetInterface = targetInterface;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }
}
