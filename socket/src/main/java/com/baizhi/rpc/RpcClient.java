package com.baizhi.rpc;

public interface RpcClient {
    public Response call(HostAndPort hostAndPort,MethodInvokeMeta methodInvokeMeta);
    public void close();
}
