package nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : web
 * @date : 2021/6/25
 */
public class NioFileChannelWrite {
    public static void main(String[] args) throws Exception {
        String str = "Hello,World";
        //创建输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\hello world.txt");
        //通过fileOutputStream 获取 对应 FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str 放入bytebuffer
        byteBuffer.put(str.getBytes());
        //对byteBuffer反转
        byteBuffer.flip();
        //将byteBuffer写入到FileChannel中
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
