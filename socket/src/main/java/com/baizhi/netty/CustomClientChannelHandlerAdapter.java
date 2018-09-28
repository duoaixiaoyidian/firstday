package com.baizhi.netty;

import io.netty.channel.*;

public class CustomClientChannelHandlerAdapter extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
//连接到服务器发送数据

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       /* ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("我是客户端----".getBytes());
        ctx.writeAndFlush(buffer);*/
        for(int i=0;i<2;i++){
            ChannelFuture future =ctx.writeAndFlush(new User());
            //捕获序列化异常
            future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            //异常时自动关闭连接
            future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
    }
//接收消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端收到："+msg);

    }
}
