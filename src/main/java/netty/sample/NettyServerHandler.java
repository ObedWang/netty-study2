package netty.sample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

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
        //耗时任务 -》 异步任务 -》 提交该channel对应的NioEventLoop的taskQueue中
        //解决方案1，用户自定义普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            //将 msg 转成 ByteBuf
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("客户端发送消息是 ：" + buf.toString(CharsetUtil.UTF_8));
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello world client", CharsetUtil.UTF_8));
        });
        //解决方案2 用户自定义定义任务 -》 该任务提交到schedule taskQueue
        EventLoop eventExecutors = ctx.channel().eventLoop();

        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            //将 msg 转成 ByteBuf
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("客户端发送消息是 ：" + buf.toString(CharsetUtil.UTF_8));
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello world client", CharsetUtil.UTF_8));
        },5, TimeUnit.SECONDS);
        System.out.println("客户端地址" + ctx.channel().remoteAddress());
        System.out.println("go on");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
