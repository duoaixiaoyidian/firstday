package com.baizhi.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class CustomClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //解帧
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        //添加对象解码器
        pipeline.addLast(new UserMessageToMessageDecoder());
        //添加长度帧编码器
        pipeline.addLast(new LengthFieldPrepender(2));
        //添加对象编码器
        pipeline.addLast(new UserMessageToMessageEncoder());
        //末端添加最终处理者
        pipeline.addLast(new CustomClientChannelHandlerAdapter());
    }
}
