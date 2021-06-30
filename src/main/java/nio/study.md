## Selector,Channel和Buffer关系
1. 每个channel都对应一个buffer
2. Selector对应一个线程，一个线程对应多个channel（连接）
3. 多个Channel可以注册到一个Selector上
4. 程序切换到那个Channel，是由事件决定的。Event就是一个很重要的概念
5. Selector会根据不同的事件，在各个通道上切换
6. Buffer就是一个内存块，底层是由一个数组
7. 数据读取写入都是通过Buffer，这个和BIO不同，BIO是单向流，而NIO的Buffer可以读也可以写，需要flip切换
8. Channel是双向的，可以返回底层操作系统的情况
NIO异步非阻塞

**非阻塞更多是针对Server端与selector对应的线程来说的** 

---
Buffer的属性
1. capacity -> 底层数组最大容量
2. limit -> 当前可读或可写的容量
3. position -> 当前位置
4. mark -> 标记位
---
### Channel
1. 通道可以同时读写
2. 通道可以实现异步读写数据
3. 通道可以从缓冲中读数据，也可以写数据进缓冲
#### 常用Channel
1. FileChannel
常用方法：write，read，transferFrom(从目标通道拷贝数据到本通道)，transferTo(主语都是当前的FileChannel)
ServerSocketChannel，SocketChannel，DataGramChannel(UDP读写)
---
### Byte，Channel注意事项
1. ByteBuffer put什么类型，就要get什么类型，否则会抛出异常
2. 可以将一个普通的Buffer转换成只读buffer buffer.asReadOnlyBuffer
3. MappedByteBuffer 可以让文件直接在内存（堆外内存）中进行修改，修改同步到文件又NIO完成
---
### selector
selector - 能够检测多个**注册**的通道上是否有**事件**发生
selector.select() - blocking，直到有事件发生
selector.select(long timeOut) - blocking，一直到超时时间到
selector.selectNow() - non-blocking，return right now

1. 当客户端连接时，会通过ServerSocketChannel得到SocketChannel
2. 将socketChannel注册到Selector上，一个Selector上可以注册多个SocketChannel
3. 注册后返回SelectionKey，会和该Selector关联（集合）
4. Selector进行监听 select方法，返回有事件发生的通道个数
5. 进一步得到各个SelectionKey（有事件发生）
6. 在通过SelectionKey获取Channel
7. 可以通过得到的Channel完成业务处理