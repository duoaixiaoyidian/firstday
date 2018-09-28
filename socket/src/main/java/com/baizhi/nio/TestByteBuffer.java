package com.baizhi.nio;

import java.nio.ByteBuffer;

public class TestByteBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        test("lfsjdjl垃圾房间数量多客服经理",buffer);
        buffer.put((byte)'a');
        test("lfsjdjl垃圾房间数量多客服经理",buffer);
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        test("写一个字节读",buffer);
        buffer.clear();
        buffer.put((byte)'b');
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        test("又写了一个字节再读",buffer);



    }
    public static void test(String msg, ByteBuffer byteBuffer){
        System.out.println(msg+" "+byteBuffer.position()+" "+byteBuffer.limit());
    }
}
