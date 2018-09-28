package com.baizhi.nio2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class FinalProcessor implements Runnable {
    private SelectionKey key;

    public FinalProcessor(SelectionKey selectionKey) {
        this.key = selectionKey;
    }
    @Override
    public void run() {
        try {
            System.out.println("处理写...");

            ByteArrayOutputStream baos= (ByteArrayOutputStream)key.attachment();
            System.out.println(new String(baos.toByteArray()));
            SocketChannel sc =(SocketChannel) key.channel();
            ByteBuffer byteBuffer=ByteBuffer.wrap((new Date().toString()).getBytes());
            sc.write(byteBuffer);
            sc.shutdownOutput();

            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
