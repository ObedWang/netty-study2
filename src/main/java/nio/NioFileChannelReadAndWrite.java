package nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : web
 * @date : 2021/6/25
 */
public class NioFileChannelReadAndWrite {
    public static void main(String[] args) throws Exception {
        File file = new File("1");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        FileChannel outputChannel = fileOutputStream.getChannel();

        while (inputChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            outputChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
