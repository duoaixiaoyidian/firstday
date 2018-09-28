package com.baizhi.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class BioSocketClient {
    public static void main(String[] args) throws IOException {
        test("经费落实国家数量设计费两地分居十分努力");
    }
    public static void test(String str) throws IOException {
        //1.创建socket
        Socket socket = new Socket();
        //2.连接
        socket.connect(new InetSocketAddress("127.0.0.1",9000));
        //3.发出请求
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(str);
        printWriter.flush();
        //4.告知写结束
        socket.shutdownOutput();
        //5.获取响应
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line=null;
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        System.out.println("客户端收到："+stringBuilder);

        //6.关闭资源
        socket.close();
    }
}
