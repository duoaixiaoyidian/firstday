package com.baizhi.rpc;

import com.baizhi.service.IDemoService;

public class TestRPC {
    public static void main(String[] args) {
        HostAndPort hostAndPort = new HostAndPort("localhost", 9999);
        RpcClient rpcClient=new NettyRpcClient();
        JDKProxy jdkProxy=new JDKProxy(hostAndPort,rpcClient);

        IDemoService proxy = (IDemoService)jdkProxy.createProxy(IDemoService.class);

        Integer sum = proxy.sum(1, 100);
        System.out.println(sum);
    }
}
