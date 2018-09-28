package com.baizhi.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioSocketServer {
    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        //1.创建serversocket
        ServerSocket serverSocket = new ServerSocket();
        //2.绑定端口
        serverSocket.bind(new InetSocketAddress(9000));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (true){
            //3.等待请求到来，转发新的socket
            System.out.println("等待客户端========");
            final Socket socket = serverSocket.accept();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //4.获取意图
                    try {
                        InputStream inputStream = socket.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line=null;
                        while ((line=bufferedReader.readLine())!=null){
                            stringBuilder.append(line);
                        }
                        System.out.println("服务端收到："+stringBuilder);
                        //5.响应
                        OutputStream outputStream = socket.getOutputStream();
                        PrintWriter printWriter = new PrintWriter(outputStream);
                        printWriter.println(new Date().toLocaleString());
                        printWriter.flush();
                        //6.告知写结束
                        socket.shutdownOutput();
                        //7.关闭资源
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

    }



    public static void test() throws IOException {
        //1.创建serversocket
        ServerSocket serverSocket = new ServerSocket();
        //2.绑定端口
        serverSocket.bind(new InetSocketAddress(9000));
        //3.等待请求到来，转发新的socket
        System.out.println("等待客户端========");
        Socket socket = serverSocket.accept();
        //4.获取意图
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line=null;
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        System.out.println("服务端收到："+stringBuilder);
        //5.响应

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(new Date().toLocaleString());
        printWriter.flush();
        //6.告知写结束
        socket.shutdownOutput();
        //7.关闭资源
        socket.close();
    }
}
