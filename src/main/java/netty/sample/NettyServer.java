package netty.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author : web
 * @date : 2021/7/3
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = null, workerGroup = null;
        try {
            //创建BossGroup和WorkerGroup
            //boss group只是处理连接请求，真正和客户端业务处理交个worker group
            //都是无限循环
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            //创建服务器端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    //设置两个线程组
                    .group(bossGroup, workerGroup)
                    //使用NioSocketChannel 作为服务器的通信实现
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //创建一个通道测试对象（匿名对象）
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 给Pipeline 设置处理器
                         * @param ch ch
                         * @throws Exception Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("server is ready");
            //绑定一个端口并且同步，生成了一个channel Future对象
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
        }


    }
}
