package com.baizhi.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOBootstrapServer1 {
        public static void main(String[] args) throws IOException {
        start02();
    }
    public static void start02() throws IOException {
        //1.创建serversocket
        ServerSocketChannel open = ServerSocketChannel.open();
        //2.绑定监听端口
        open.bind(new InetSocketAddress(9000));
        //3.设置通道非阻塞
        open.configureBlocking(false);
        //4.创建selector通道选择器
        Selector selector = Selector.open();
        //5.注册通道事件
        open.register(selector,SelectionKey.OP_ACCEPT);
        while (true){
            System.out.println("选择keys中----------");
            int i = selector.select();
            if (i>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    //处理事件key
                    if (key.isAcceptable()){
                        System.out.println("accept---------");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //设置非阻塞
                        socketChannel.configureBlocking(false);
                        SelectionKey register = socketChannel.register(selector, SelectionKey.OP_READ);

                    }else if (key.isReadable()){
                        System.out.println("read-------");
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteArrayOutputStream baos = null;
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        if (key.attachment()!=null){
                            baos=(ByteArrayOutputStream)key.attachment();

                        }else {
                            baos=new ByteArrayOutputStream();
                        }
                        int read = sc.read(buffer);
                        if (read==-1){
                            //注册写
                            sc.register(selector,SelectionKey.OP_WRITE,baos);

                        }else {
                            buffer.flip();
                            baos.write(buffer.array(),0,read);
                            key.attach(baos);
                        }
                    }else if (key.isWritable()){
                        System.out.println("write-------");
                        ByteArrayOutputStream baos = (ByteArrayOutputStream) key.attachment();
                        System.out.println(new String(baos.toByteArray()));
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer wrap = ByteBuffer.wrap(new Date().toLocaleString().getBytes());
                        socketChannel.write(wrap);
                        socketChannel.shutdownOutput();
                        socketChannel.close();
                    }
                    //移除事件
                    iterator.remove();
                }
            }
        }
    }
    public static void start01() throws IOException {
        //1.创建ServerSocket
        ServerSocketChannel ss= ServerSocketChannel.open();
        //2.绑定监听端口
        ss.bind(new InetSocketAddress(9000));
        //3.设置通道非阻塞
        ss.configureBlocking(false);
        //4.创建selector通道选择器
        Selector selector = Selector.open();
        //5.注册通道事件
        ss.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            System.out.println("选择keys中=======");
            //可以处理的keys数目，如果没有，该方法阻塞
            int i = selector.select();
            if (i>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    //处理事件key
                    if (key.isAcceptable()){
                        System.out.println("accept======");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel channel = serverSocketChannel.accept();
                        //设置非阻塞
                        channel.configureBlocking(false);
                        channel.register(selector,SelectionKey.OP_READ);

                    }//注册读
                    else if (key.isReadable()){
                        System.out.println("read====");
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        while (true){
                            buffer.clear();
                            int num = socketChannel.read(buffer);
                            if (num==-1)break;
                            buffer.flip();
                            byteArrayOutputStream.write(buffer.array(),0,num);
                        }
                        //注册写
                        socketChannel.register(selector,SelectionKey.OP_WRITE);
                    }else if (key.isWritable()){
                        System.out.println("write========");
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer wrap = ByteBuffer.wrap(new Date().toLocaleString().getBytes());
                        sc.write(wrap);
                        sc.shutdownOutput();
                        sc.close();
                    }
                    //移除事件
                    iterator.remove();
                }

            }

        }


    }

}
