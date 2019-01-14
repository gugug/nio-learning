package com.study.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ReadAndShow {
    static public void main(String args[]) throws Exception {

        //        第一步是获取通道。我们从 FileInputStream 获取通道
        FileInputStream fin = new FileInputStream("readandshow.txt");
        FileChannel fc = fin.getChannel();

        //        下一步是创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //        需要将数据从通道读到缓冲区中
        fc.read(buffer);

        buffer.flip();

        int i = 0;
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.println("Character " + i + ": " + ((char) b));
            i++;
        }

        fin.close();
    }
}
