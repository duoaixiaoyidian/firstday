package com.baizhi.rpc;

import com.baizhi.service.impl.DemoService;

import java.util.HashMap;
import java.util.Map;

public class TestSever {
    public static void main(String[] args) throws InterruptedException {
        Map<String,Object> objectMap=new HashMap<String,Object>();
        objectMap.put("com.baizhi.service.IDemoService",new DemoService());

        ServiceProvider serviceProvider = new ServiceProvider(9999);
        serviceProvider.setBeanFactory(objectMap);

        serviceProvider.start();

    }
}
