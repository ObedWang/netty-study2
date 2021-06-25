package nio;

import java.nio.IntBuffer;

/**
 *
 * @author : web
 * @date : 2021/6/25
 */
public class BasicBuffer {
    public static void main(String[] args) {
        //创建一个可以存放5个int的buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //存数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        //如何从buffer中读取数据
        //将buffer读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
