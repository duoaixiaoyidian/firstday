package com.baizhi.nio2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class DispatcherProcessor implements Runnable {
    private SelectionKey key;

    public DispatcherProcessor(SelectionKey selectionKey) {
        this.key = selectionKey;
    }

    @Override
    public void run() {
        try {
            System.out.println("请求转发...");
            ServerSocketChannel channel =(ServerSocketChannel) key.channel();
            SocketChannel socketChannel = channel.accept();
            NIOBootstrapServer.push2DispatcherQueue(socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
