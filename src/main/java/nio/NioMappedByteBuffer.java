package nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1. MappedByteBuffer可以让文件直接在内存（堆外内存）中修改，操作系统不需要拷贝到cpu中
 *
 * @author : wangebie
 * @date : 2021/6/26 12:20
 */
public class NioMappedByteBuffer {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * param1:使用读写模式
         * param2: 可以直接修改的起始位置
         * param3：映射到内存的大小，即将文件的多少个字节映射到内存
         * 可以直接修改的范围就是0~5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte)'H');
        mappedByteBuffer.put(3,(byte)'9');
        randomAccessFile.close();
    }
}
