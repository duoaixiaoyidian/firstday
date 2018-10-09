package com.baizhi.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class UserMessageToMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    /**
     *
     * @param ctx
     * @param msg :解码一帧数据
     * @param out ：解码帧数据
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        byte[] values=new byte[msg.readableBytes()];
        msg.readBytes(values);

        ByteArrayInputStream bais=new ByteArrayInputStream(values);
        ObjectInputStream ois=new ObjectInputStream(bais);
        Object o = ois.readObject();
        ois.close();

        out.add(o);

    }
}
