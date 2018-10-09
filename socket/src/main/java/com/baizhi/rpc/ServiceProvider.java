package com.baizhi.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.Method;
import java.util.Map;

public class ServiceProvider {
    Map<String,Object> beanFactory;

    public void setBeanFactory(Map<String, Object> beanFactory) {
        this.beanFactory = beanFactory;
    }

    /*Netty*/
    private  int port;
    private ServerBootstrap sbt;
    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public ServiceProvider(int port){
        this.port=port;
        sbt=new ServerBootstrap();
        boss=new NioEventLoopGroup();
        worker=new NioEventLoopGroup();

        sbt.group(boss,worker);
        sbt.channel(NioServerSocketChannel.class);
    }

    public void start() throws InterruptedException {

        sbt.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //解帧
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                //添加对象解码器
                pipeline.addLast(new UserMessageToMessageDecoder());
                //添加长度帧编码器
                pipeline.addLast(new LengthFieldPrepender(2));
                //添加对象编码器
                pipeline.addLast(new UserMessageToMessageEncoder());
                //添加最终处理者
                pipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        MethodInvokeMeta mim= (MethodInvokeMeta) msg;
                        System.out.println("mim:"+mim);
                        //实现类
                        Object taregtBean = beanFactory.get(mim.getTargetInterface().getName());
                        //获取方法对象
                        Method m=taregtBean.getClass().getDeclaredMethod(mim.getMethod(),mim.getParameterTypes());

                        Response response=new Response();
                        try {
                            Object retrunValue = m.invoke(taregtBean, mim.getArgs());
                            response.setReturnValue(retrunValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.setRuntimeException(new RuntimeException(e.getCause()));
                        }

                        //将结果写回去
                        ChannelFuture f = ctx.writeAndFlush(response);
                        f.addListener(ChannelFutureListener.CLOSE);
                        f.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        f.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);

                    }
                });
            }
        });
        System.out.println("我在"+port+"监听服务...");
        ChannelFuture f = sbt.bind(port).sync();
        f.channel().closeFuture().sync();

    }

    public void close(){
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
