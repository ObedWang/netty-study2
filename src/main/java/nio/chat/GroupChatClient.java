package nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author : wangebie
 * @date : 2021/7/1 21:39
 */
public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到username
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param info info
     */
    public void sendInfo(String info) {
        System.out.println("send message : "+ info);
        info = userName + " 说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取消息
     */
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        //转字符串
                        String msg = new String(buffer.array(), 0, buffer.limit());
                        System.out.println(msg.trim());
                    }
                }
            } else {
                System.out.println("没有可用通道");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    groupChatClient.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.next();
            groupChatClient.sendInfo(msg);
        }
    }
}
