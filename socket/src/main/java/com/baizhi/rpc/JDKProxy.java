package com.baizhi.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy implements RpcProxy,InvocationHandler {
   private Class<?> interfaceClass;
   private HostAndPort hostAndPort;
   private RpcClient rpcClient;

    public JDKProxy(HostAndPort hostAndPort, RpcClient rpcClient) {
        this.hostAndPort = hostAndPort;
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvokeMeta methodInvokeMeta = new MethodInvokeMeta(interfaceClass,method.getName(),method.getParameterTypes(),args);
        Response response = rpcClient.call(hostAndPort, methodInvokeMeta);
        if(response.getRuntimeException()!=null){
            throw response.getRuntimeException();
        }
        return response.getReturnValue();
    }

    @Override
    public Object createProxy(Class interfaceClass) {
        this.interfaceClass=interfaceClass;

        return Proxy.newProxyInstance(JDKProxy.class.getClassLoader(),new Class[]{interfaceClass},this);
    }
}
