package com.baizhi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class UserMessageToMessageDecoder extends MessageToMessageDecoder<ByteBuf>{
    /**
     *
     * @param channelHandlerContext
     * @param byteBuf 解码一帧数据
     * @param list 解码后的帧数据
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object o = ois.readObject();
        ois.close();
        list.add(o);

    }
}
