package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInbound 是 ChannelInboundHandlerAdapter的子类
 * HTTPObject 表示客户端和服务器通讯的数据封装
 *
 * @author : web
 * @date : 2021/7/5
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取事件发生
     *
     * @param ctx ctx
     * @param msg msg
     * @throws Exception Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是HTTPRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg type = " + msg.getClass());
            System.out.println("client address = " + ctx.channel().remoteAddress());
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取到uri,过滤对应资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico 不作响应");
                return;
            }

            //回复信息给浏览器[http 协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, I'm Server", CharsetUtil.UTF_8);
            //构造一个http的想响应
            FullHttpMessage response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
