package com.baizhi.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileCopyDemo {
    public static void main(String[] args) throws IOException {
        FileChannel fileInputStream = new FileInputStream("F:\\software\\WeChat\\WeChat.exe").getChannel();
        FileChannel fileOutputStream = new FileOutputStream("C:\\Users\\Public\\Desktop\\WeChat.exe").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true){
            buffer.clear();
            int i = fileInputStream.read(buffer);
            if (i==-1)break;
            buffer.flip();
            fileOutputStream.write(buffer);

        }
        fileOutputStream.close();
        fileInputStream.close();

    }
}
