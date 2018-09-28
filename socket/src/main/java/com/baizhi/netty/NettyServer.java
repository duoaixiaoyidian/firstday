package com.baizhi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1.创建服务启动引导
        ServerBootstrap sbt=new ServerBootstrap();
        //2.创建线程池组 boss、worker
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        //3.设置线程池组
        sbt.group(boss,worker);
        //4.设置服务实现类
        sbt.channel(NioServerSocketChannel.class);
        //5.初始化通讯管道
        sbt.childHandler(new CustomServerChannelInitializer());
        //6.绑定端口，并且启动服务
        System.out.println("我在9999监听...");
        ChannelFuture future = sbt.bind("192.168.0.80",9999).sync();
        //7.关闭通道资源
        future.channel().closeFuture().sync();
        //8.释放资源
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
