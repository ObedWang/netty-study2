package netty.sample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 1.自顶一个handler 需要继承netty 规定好的某个HandlerAdapter
 * 2.这时我们自顶一个Handler，才能成为一个handler
 *
 * @author : web
 * @date : 2021/7/3
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // write + flush 数据写入缓存并刷新
        //一般讲 我们对这个发送的数据编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8));
    }

    /**
     * 读取数据实际 -> 这里我们可以读取客户端发送的消息
     *
     * @param ctx ctx 上下文对象，含有 管道，通道，地址
     * @param msg msg 就是客户端发送的数据 默认是Object
     * @throws Exception Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx : " + ctx);
        //将 msg 转成 ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是 ：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
