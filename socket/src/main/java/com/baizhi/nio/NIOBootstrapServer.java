package com.baizhi.nio;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOBootstrapServer {
    public static void main(String[] args) throws IOException {
        start01();
    }
    public static void start01() throws IOException {
        //1.创建ServerSocket
        ServerSocketChannel ss= ServerSocketChannel.open();
        //2.绑定监听端口
        ss.bind(new InetSocketAddress(9000));

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        while(true) {
            //3.等待请求到来  转发新的Socket
            System.out.println("我在9999等待...");
           final SocketChannel socket = ss.accept();
            fixedThreadPool.submit(new Runnable() {
                public void run() {
                    try {
                        //4.获取意图
                        ByteArrayOutputStream baos=new ByteArrayOutputStream();

                        ByteBuffer buffer=ByteBuffer.allocate(1024);
                        while (true){
                            buffer.clear();
                            int n = socket.read(buffer);
                            if(n==-1) break;
                            buffer.flip();
                            baos.write(buffer.array(),0,n);
                        }
                        System.out.println("服务器收到："+new String(baos.toByteArray()));


                        //5.给出响应
                        ByteBuffer buffer1=ByteBuffer.wrap(new Date().toLocaleString().getBytes());
                        socket.write(buffer1);
                        //6.告知客户端写结束
                        socket.shutdownOutput();
                        //7.释放socket资源
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
