package netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @author : web
 * @date : 2021/7/6
 */
public class NettyByteBuf {
    public static void main(String[] args) {
        //create ByteBuf
        //1. contain arr is a byte[10]
        //2. not need to flip to convert
        //底层维护了readerIndex 和writerIndex
        //0->readerIndex->writerIndex->capacity
//        ByteBuf buffer = Unpooled.buffer(10);
//        for (int i = 0; i < 10; i++) {
//            buffer.writeByte(i);
//        }
//        for (int i = 0; i < 10; i++) {
//            System.out.println(buffer.getByte(i));
//        }

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello World!", CharsetUtil.UTF_8);
        if(byteBuf.hasArray()){
            System.out.println(byteBuf);
            System.out.println(new String(byteBuf.array()));
        }
    }
}
