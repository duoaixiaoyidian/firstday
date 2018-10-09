package com.baizhi.service.impl;

import com.baizhi.service.IDemoService;

public class DemoService implements IDemoService {
    @Override
    public Integer sum(Integer x, Integer y) {
        System.out.println("x+y:"+x+y);
        return x+y;
    }
}
