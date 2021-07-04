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
---
### zero copy
nio还可以零拷贝
nio的优点其实是在netty中也有体现的
所以netty为什么这么快 
1. IO多路复用
2. 零拷贝
--- 
### IO模型
#### 特点
1. 采用阻塞IO模式获取输入的数据
2. 每个链接都需要独立的线程完成数据的输入
#### 问题
1. 并发数很大的时候，会创建大量的线程，占用系统资源
2. 会被read阻塞
### reactor模式
reactor 反应器模式，分发者模式（dispatcher），通知者模式（notifier）
1. 基于IO复用模型
2. 基于线程池复用线程资源
#### 说明
1. 基于事件驱动
2. 服务器端程序处理传入的多个请求，并将它们同步分派到相应的线程处理
3. Reactor模式使用IO复用监听事件。收到事件后，分发给某个线程。
#### 主从Reactor多线程
1. Reactor主线程MainReactor对象通过select监听连接事件，受到事件后，通过Acceptor处理连接事件
2. 当Acceptor处理连接事件后，MainReactor将连接分配给SubReactor
3. SubReactor将两节加入到连接队列进行监听，并创建handler进行各种事件处理
4. 当有新事件发生时，SubReactor就会调用对应的handler处理
5. handler通过read读取数据，分发给后面的worker线程处理
6. worker线程池分配独立的worker线程进行业务处理，并返回结果
7. handler受到响应的结果后，再通过send将结果返回给client
8. Reactor主线程可以对应多个Reactor子线程，即MainReactor可以关联多个
---
## netty
1. BossGroup线程维护Selector，只关心Accept
2. 当接收到Accept事件，获取到相关的SocketChannel，封装成NIOSocketChannel，并注册到Worker线程（事件循环）并注册
3. 当Worker线程坚挺到selector中通道发生事件后，就进行处理（由handler），注意handler事先加入到通道了
---
### 深入理解
1. Netty抽象出两组线程池 BossGroup-专门负责接收客户端的连接，WorkerGroup 专门负责网络的读写
2. BossGroup和WorkerGroup 类型都是NioEventLoopGroup
3. NioEventLoopGroup 相当于一个事件循环组，这个组中含有多个事件循环，每一个事件循环是NioEventLoop
4. NioEventLoop 表示一个不断循环的执行处理任务的线程，每个NioEventLoop都有一个selector，用于监听绑定其上的Socket的网络通讯
5. NioEventLoopGroup 可以有多个线程，既可以含有多个NioEventLoop
6. 每个Boss NioEventLoop循环执行步骤有三步
    1. 轮询accept事件
    2. 处理accept事件，与client建立连接，生成NIOSocketChannel，并将其注册到某个Worker NiOEventLoop上的selector
    3. 处理任务队列的任务，即runAllTasks
7. 每个Worker NioEventLoop循环执行的步骤
    1. 轮询read，write事件
    2. 处理i/o事件，即read，write事件，在对应的NioSocketChannel处理
    3. 处理任务队列的任务 即runAllTasks
8. 每个Worker NioEventLoop处理业务时，会使用pipeline（管道），pipeline中包含了channel，即通过pipeline可以获取到对应的通道，管道中维护了很多的处理区
---
### Future 说明
1. 表示异步的执行结果，可以通过它提供的方法来检测执行是否完成
2. ChannelFuture是一个接口，我们可以添加监听器，当监听的时间发生时，会被通知到
3. 底层实现原理其实是利用Promise类，Promise类的实现逻辑是，当添加listener的时候，将listener+future变成一个runnable,
这个runnable执行listener.operationComplete(future)方法，并将这个runnable放入eventLoop的execute里面，eventLoop会把他放入到taskQueue中
这样当future执行完了之后，便再回去执行这个runnable。当然这只是主要逻辑，在调用setSuccess或者addListener这些方法的时候，马上也会调用isDone方法
判断是否成功，如果成功直接去调用回调函数。