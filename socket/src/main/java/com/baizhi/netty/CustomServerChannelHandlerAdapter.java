package com.baizhi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Date;

public class CustomServerChannelHandlerAdapter extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg: "+msg);
        /*ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(new Date().toLocaleString().getBytes());*/
        //发送消息
        ChannelFuture future = ctx.writeAndFlush(new Date());
        //关闭socketchannel
        //future.addListener(ChannelFutureListener.CLOSE);
        //捕获序列化异常
        future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        //异常时自动关闭连接
        future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

    }
}
