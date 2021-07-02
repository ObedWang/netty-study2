package nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author : web
 * @date : 2021/7/2
 */
public class NewIoClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String fileName = "E:\\Chrome Downloads\\onmyoji_setup-2.bin";

        //得到一个文件channel
        FileChannel channel = new FileInputStream(fileName).getChannel();

        //发送时间
        long startTime = System.currentTimeMillis();
        //在linux 下一个transferTo方法就可以完成传输
        //在windows 下一次调用transferTo只能发送8M，就系要分段传输文件，而且要注意传输的位置
        long transferCount = channel.transferTo(0, channel.size(), socketChannel);
        System.out.println("发送的总字节数 =" + transferCount + " 耗时 = " + (System.currentTimeMillis() - startTime));

    }
}
