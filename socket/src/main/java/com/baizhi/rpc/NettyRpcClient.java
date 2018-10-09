package com.baizhi.rpc;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.ArrayList;
import java.util.List;

public class NettyRpcClient implements RpcClient {
    private Bootstrap bootstrap;
    private NioEventLoopGroup worker;

    public NettyRpcClient(){
        bootstrap=new Bootstrap();
        worker=new NioEventLoopGroup();
        bootstrap.group(worker);

        bootstrap.channel(NioSocketChannel.class);
    }

    @Override
    public Response call(HostAndPort hostAndPort, final MethodInvokeMeta methodInvokeMeta)  {
        final List<Response> responseHolder=new ArrayList<Response>(1);
        try {
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
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
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ChannelFuture future = ctx.writeAndFlush(methodInvokeMeta);

                            future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                            future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            responseHolder.add((Response) msg);
                        }
                    });
                }
            });
            ChannelFuture f = bootstrap.connect(hostAndPort.getHost(), hostAndPort.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseHolder.get(0);
    }

    @Override
    public void close() {
        worker.shutdownGracefully();
    }
}
