package com.baizhi.nio2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadProcessor  implements Runnable {
    private SelectionKey key;

    public ReadProcessor(SelectionKey selectionKey) {
        this.key = selectionKey;
    }
    @Override
    public void run() {
        try {
            System.out.println("处理读...");

            SocketChannel sc =(SocketChannel) key.channel();
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            ByteArrayOutputStream baos=null;

            if(key.attachment()!=null){
                baos=(ByteArrayOutputStream)key.attachment();

            }else{
                baos=new ByteArrayOutputStream();
            }
            int num = sc.read(buffer);
            if(num==-1) {
                //注册写
                NIOBootstrapServer.push2WriteQueue(key);
            }else {
                buffer.flip();
                baos.write(buffer.array(), 0, num);
                key.attach(baos);
                //注册读
                NIOBootstrapServer.push2ReadQueue(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
