package com.baizhi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //1.创建服务启动引导
        Bootstrap bt=new Bootstrap();
        //2.创建线程池组worker
        EventLoopGroup worker=new NioEventLoopGroup();
        //3.设置线程池组
        bt.group(worker);
        //4.设置服务实现类
        bt.channel(NioSocketChannel.class);
        //5.初始化通讯管道
        bt.handler(new CustomClientChannelInitializer());
        //6.连接端口，并且启动服务
        ChannelFuture future = bt.connect("127.0.0.1",9999).sync();
        //7.关闭通道资源
        future.channel().closeFuture().sync();
        //8.释放资源
        worker.shutdownGracefully();
    }
}
