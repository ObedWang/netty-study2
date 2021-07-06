package netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @author : web
 * @date : 2021/7/7
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 定义一个channel组，管理所有channel
     * GlobalEventExecutor.INSTANCE  全局事件执行器
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 表示连接建立，将当前channel加入到channelGroup
     *
     * @param ctx ctx
     * @throws Exception Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //notify all channel
        channelGroup.writeAndFlush("[client]" + channel.remoteAddress() + " add chatting\n");
        channelGroup.add(channel);
    }

    /**
     * 表示channel in active 。提示 xx上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " is online now!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " is offline now!");
    }

    /**
     * 断开连接
     *
     * @param ctx ctx
     * @throws Exception except
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //notify all channel
        channelGroup.writeAndFlush("[client]" + channel.remoteAddress() + " leave chatting\n");
        System.out.println("current size of channelGroup : " + channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[client]" + channel.remoteAddress() + msg + "\n");
            } else {
                ch.writeAndFlush("[self]" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
