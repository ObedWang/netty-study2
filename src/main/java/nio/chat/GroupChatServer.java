package nio.chat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author : wangebie
 * @date : 2021/7/1 20:33
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    private void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();
                        //监听accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + " 上线");
                        } else if (key.isReadable()) {
                            //处理读
                            read(key);

                        }
                    }
                } else {
                    System.out.println("waiting for connect");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取消息
     */
    private void read(SelectionKey selectionKey) {
        SocketChannel channel = null;
        channel = (SocketChannel) selectionKey.channel();
        //创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int count = channel.read(buffer);
            if (count > 0) {
                //把缓冲区的数据转换成字符串
                String msg = new String(buffer.array(), 0, buffer.limit());
                System.out.println("from 客户端：" + msg);
                //向其他客户端发送消息，专门写一个方法处理
                sendMsgToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端（通道）
     *
     * @param msg  消息
     * @param self 消息源
     */
    private void sendMsgToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中");
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
