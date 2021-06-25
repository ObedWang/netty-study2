package nio;


import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : web
 * @date : 2021/6/25
 */
public class NioFileChannelRead {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("D:\\hello world.txt");
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将通道的数据放入放到缓冲区中
        fileChannel.read(byteBuffer);
        //byte[] to string
        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));
        fileInputStream.close();
    }
}
