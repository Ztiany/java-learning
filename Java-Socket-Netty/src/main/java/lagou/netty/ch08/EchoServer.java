package lagou.netty.ch08;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

public class EchoServer {

    public static void main(String[] args) {
        try {
            new EchoServer().start(8088);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(@NotNull SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    // 固定长度解码器
                                    //.addLast(new FixedLengthFrameDecoder(10))
                                    // 特殊字符解码器
                                    .addLast(new DelimiterBasedFrameDecoder(10, true, true, Unpooled.copiedBuffer("&".getBytes())))
                                    .addLast(new EchoServerInboundHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("Http Server started， Listening on " + port);
            channelFuture.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

}
