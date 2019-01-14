package com.study.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class CopyFile {
    static public void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java CopyFile infile outfile");
            System.exit(1);
        }

        String infile = args[0];
        String outfile = args[1];

        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {

            // clear() 方法重设缓冲区，使它可以接受读入的数据。
            buffer.clear();

            // 检查拷贝何时完成。当没有更多的数据时，拷贝就算完成，并且可以在 read() 方法返回 -1
            int r = fcin.read(buffer);

            if (r == -1) {
                break;
            }

            //flip() 方法让缓冲区可以将新读入的数据写入另一个通道
            buffer.flip();

            fcout.write(buffer);
        }
    }
}
