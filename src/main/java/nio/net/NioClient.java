package nio.net;



import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author : wangebie
 * @date : 2021/6/30 20:47
 */
public class NioClient {
    public static void main(String[] args) throws Exception{
        //得到网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的Ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("因为连接需要事件，客户端不会阻塞，可以做其他工作");
            }
        }
        String str = "hello,World";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer数据写入channel
        socketChannel.write(byteBuffer);
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        socketChannel.read(allocate);
        System.out.println(allocate.limit());
        System.out.println(new String(allocate.array(),0,allocate.limit()));
    }
}
