package com.baizhi.nio2;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOBootstrapServer {
    private static ExecutorService boss= Executors.newFixedThreadPool(10);
    private static ExecutorService worker= Executors.newFixedThreadPool(20);
    private static Selector selector;
    private static ServerSocketChannel ssc;


    //构建转发队列 负责注册读
    private static CopyOnWriteArrayList<SocketChannel> dispatcherQueue=new CopyOnWriteArrayList<SocketChannel>();
    private static CopyOnWriteArrayList<SelectionKey> readQueue=new CopyOnWriteArrayList<SelectionKey>();
    private static CopyOnWriteArrayList<SelectionKey> writeQueue=new CopyOnWriteArrayList<SelectionKey>();



    public static void main(String[] args) throws IOException {
        start01();
    }
    public static void start01() throws IOException {
        //1.创建ServerSocket
        ssc=ServerSocketChannel.open();
        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(9999));
        //3.设置通道非阻塞
        ssc.configureBlocking(false);
        selector = Selector.open();
        //5.注册通道事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            System.out.println("选择keys中...");
            //可以出来keys的数目，如果没有该方法block
            int n = selector.select();
            if(n>0){
                Iterator<SelectionKey> iterator =selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    final SelectionKey key = iterator.next();
//                    key.cancel();
                    //处理事件key
                    if(key.isAcceptable()){
                        //取消ACCEPT事件
                        key.cancel();
                        boss.submit(new DispatcherProcessor(key));
                    }else if(key.isReadable()){
                        //取消读事件
                        key.cancel();
                        worker.submit(new ReadProcessor(key));
                    }else if(key.isWritable()){
                        //取消写
                        key.cancel();
                        worker.submit(new FinalProcessor(key));
                    }
                    //移除事件
                    iterator.remove();
                }
            }else{
                //注册 ACCEPT 事件
                if(dispatcherQueue.size()>0){
                    System.out.println("注册ACCEPT...");
                    ssc.register(selector,SelectionKey.OP_ACCEPT);
                }
                //注册读事件
                while(dispatcherQueue.size()!=0){
                    System.out.println("注册读...");
                    SocketChannel socketChannel = dispatcherQueue.remove(0);
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                //注册读事件
                while(readQueue.size()!=0){
                    System.out.println("注册重复读...");

                    SelectionKey key = readQueue.remove(0);
                    SocketChannel channel =(SocketChannel) key.channel();
                    channel.register(selector,SelectionKey.OP_READ,key.attachment());
                }
                //注册写事件
                while(writeQueue.size()!=0){
                    System.out.println("注册写...");
                    SelectionKey key = writeQueue.remove(0);
                    SocketChannel channel =(SocketChannel) key.channel();
                    channel.register(selector,SelectionKey.OP_WRITE,key.attachment());
                }
            }
        }
    }
    public static void push2DispatcherQueue(SocketChannel socketChannel){
        dispatcherQueue.add(socketChannel);
        //打破main线程阻塞
        selector.wakeup();
    }
    public static void push2ReadQueue(SelectionKey key){
        readQueue.add(key);
        //打破main线程阻塞
        selector.wakeup();
    }
    public static void push2WriteQueue(SelectionKey key){
        writeQueue.add(key);
        //打破main线程阻塞
        selector.wakeup();
    }
}
