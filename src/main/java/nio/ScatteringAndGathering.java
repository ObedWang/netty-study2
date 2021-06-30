package nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数据写入到buffer时，可以采用bufer数组，依次写入
 * Gathering：可以从buffer数组，批量读取数据
 *
 * @author : wangebie
 * @date : 2021/6/30 19:29
 */
public class ScatteringAndGathering {
    public static void main(String[] args) throws Exception {
        //使用serverSocketChannel 和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端链接
        SocketChannel socketChannel = serverSocketChannel.accept();
        //从客户端接收8个字节
        int messageLength = 8;
        //循环的读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                byteRead += socketChannel.read(byteBuffers);
                System.out.println("byteRead = " + byteRead);
                //使用流打印，看看当前buffer的position 和limit
            }
            Arrays.stream(byteBuffers).map(x -> "position = " + x.position() + " ; limit = " + x.limit() + " ;").forEach(System.out::println);
            //将所有的buffer进行flip
            Arrays.stream(byteBuffers).forEach(Buffer::flip);
            //读数据
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                byteWrite += socketChannel.write(byteBuffers);
            }
            //将所有的buffer clean
            Arrays.stream(byteBuffers).forEach(Buffer::clear);
            System.out.println("byteRead = " + byteRead + " byteWrite = " + byteWrite + " messageLength = " + messageLength);

        }
    }
}
