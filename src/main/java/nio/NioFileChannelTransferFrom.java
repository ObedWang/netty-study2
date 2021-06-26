package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author : wangebie
 * @date : 2021/6/26 9:35
 */
public class NioFileChannelTransferFrom {
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("bird.jpg");
        FileOutputStream outputStream = new FileOutputStream("bird_copy.jpg");
        FileChannel source = inputStream.getChannel();
        FileChannel target = outputStream.getChannel();

        //使用transferFrom完成拷贝
        target.transferFrom(source, 0, source.size());
        //close
        source.close();
        target.close();
        inputStream.close();
        outputStream.close();
    }
}
