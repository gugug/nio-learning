package com.study.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * 内存映射文件 I/O
 * 内存映射文件 I/O 是通过使文件中的数据神奇般地出现为内存数组的内容来完成的。
 * 这其初听起来似乎不过就是将整个文件读到内存中，但是事实上并不是这样。一般来说，只有文件中实际读取或者写入的部分才会送入（或者 映射 ）到内存中。
 * <p>
 * 修改数据与将数据保存到磁盘是没有分开的。
 */
public class UseMappedFile {
    static private final int start = 0;
    static private final int size = 1024;

    static public void main(String args[]) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("usemappedfile.txt", "rw");
        FileChannel fc = raf.getChannel();

        /*
          将一个 FileChannel (它的全部或者部分)映射到内存中。为此我们将使用 FileChannel.map() 方法。下面代码行将文件的前 1024 个字节映射到内存中
         */
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,
                start, size);

        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);

        raf.close();
    }
}
