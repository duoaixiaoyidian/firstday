package com.baizhi.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class UserMessageToMessageEncoder extends MessageToMessageEncoder<Object> {
    /**
     *
     * @param ctx
     * @param msg  :需要编码的对象 一帧数据
     * @param out  ：
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        ByteBuf buf= Unpooled.buffer();

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(msg);
        oos.flush();
        oos.close();

        buf.writeBytes(baos.toByteArray());



        out.add(buf);


    }
}
