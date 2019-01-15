# study jav-nio
[TOC]
## ref  
[IBM: NIO 入门](https://www.ibm.com/developerworks/cn/education/java/j-nio/j-nio.html)
[Java NIO Tutorial](http://tutorials.jenkov.com/java-nio/index.html)

## 三大元素
* Channels
* Buffers
* Selectors
## Channels
* FileChannel：从文件读写数据
* DatagramChannel：UDP协议从网络上读写数据
* SocketChannel：TCP协议从网络上读写数据
* ServerSocketChannel：允许您监听传入的TCP连接，就像Web服务器一样，每传入一个连接，创建一个SocketChannel 
## Buffers
数据从通道读入缓冲区，并从缓冲区写入通道。
### 三元素
* position：在从通道读取时,position 变量跟踪已经写了多少数据，它指定了下一个字节将放到数组的哪一个元素中。在写入通道时，您是从缓冲区中获取数据。 position 值跟踪从缓冲区中获取了多少数据，它指定下一个字节来自数组的哪一个元素。
* limit：limit 变量表明还有多少数据需要取出(在从缓冲区写入通道时)，或者还有多少空间可以放入数据(在从通道读入缓冲区时)。
* capacity：缓冲区的 capacity 表明可以储存在缓冲区中的最大数据容量。
### Buffer Types
* ByteBuffer
* MappedByteBuffer
* CharBuffer
* DoubleBuffer
* FloatBuffer
* IntBuffer
* LongBuffer
* ShortBuffer
### flip
我们要将数据写到输出通道中。在这之前，必须调用 flip() 方法。使得buffer从read模式转换到write模式
这个方法做两件非常重要的事：
1. 将 limit 设置为当前 position
2. 将 position 设置为 0。
### buffer.clear&compact
clear()方法清除整个缓冲区。 compact()方法仅清除已读取的数据。任何未读数据都会移动到缓冲区的开头，数据将在未读数据之后写入缓冲区。
### 比较
* equals()只比较Buffer中的剩余元素,equals只是比较Buffer的一部分，不是每一个在它里面的元素都比较.
当满足下列条件时，表示两个Buffer相等：
1. 有相同的类型（byte、char、int等）。
2. Buffer中剩余的个数相等。
3. Buffer中所有剩余等都相同。
* compareTo()方法比较两个Buffer的剩余元素，
1. 第一个不相等的元素小于另一个Buffer中对应的元素 。
2. 所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。
满足以上条件，将会认为Buffer小于另一个Buffer

## 分散/聚集NIO
* 分散读(Scattering Read)：数据从一个通道写入多个缓冲区
* 聚集写(Gathering Writes)：数据从多个缓冲区写入一个通道  

在分散读取中，按照缓冲区在数组中出现的顺序从通道写入数据，通道依次填充每个缓冲区。填满一个缓冲区后，它就开始填充下一个。在某种意义上，缓冲区数组就像一个大缓冲区。

## 选择器（selector）
您可以使用选择器使用单个线程处理多个通道。
```java
//创建新的选择器
Selector selector = Selector.open();
//将 SocketChannel 设置为 非阻塞的, 否则异步 I/O 就不能工作
channel.configureBlocking(false);
//将新打开的 SocketChannel 注册到 Selector上
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
```
通道必须处于非阻塞模式才能与选择器一起使用。这意味着您无法将FileChannel与Selector一起使用，因为FileChannel无法切换到非阻塞模式。套接字通道可以正常工作。
### 注册监听事件类别
* SelectionKey.OP_CONNECT
* SelectionKey.OP_ACCEPT
* SelectionKey.OP_READ
* SelectionKey.OP_WRITE

## FileChannel
FileChannel无法设置为非阻塞模式。它始终以阻塞模式运行。


## Pipe
Java NIO Pipe是两个线程之间的单向数据连接。管道具有源通道和接收器通道。您将数据写入接收器通道。然后可以从源通道读取该数据。

## NIO vs. IO
IO   | NIO
---- | ---
面向流 |  面向缓冲
阻塞IO |  非阻塞IO
无     |  选择器
### 面向流&面向缓冲
* IO面向流：意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。此外，它不能前后移动流中的数据。
* 面向缓冲：您可以根据需要在缓冲区中前后移动。这使您在处理过程中更具灵活性。需要检查缓冲区是否包含完整所需的数据，需要确保在读入缓冲区时，不覆盖尚未处理的缓冲区中的数据。
### 阻塞&非阻塞IO
* IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。
* 非阻塞：线程可以请求从通道读取数据，并且只获取当前可用的数据，或者当前没有数据的时候。不获取任何内容。线程可以继续处理其他事情，而不是在数据可供读取之前保持阻塞状态。
### 选择器selector
* NIO的选择器允许一个单独的线程来监视多个通道的输入，你可以使用一个选择器注册多个通道，然后使用一个单独的线程来“选择”通道，这种选择器机制使单个线程可以轻松管理多个通道。
