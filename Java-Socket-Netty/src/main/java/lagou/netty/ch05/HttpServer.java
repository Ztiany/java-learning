package lagou.netty.ch05;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lagou.netty.ch02.HttpServerHandler;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

public class HttpServer {

    public static void main(String[] args) {
        try {
            new HttpServer().start(8088);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start(int port) throws Exception {
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
                                    //InBoundHandler
                                    .addLast(new SampleErrorInBoundHandler("SampleInBoundHandlerA", false))
                                    .addLast(new SampleErrorInBoundHandler("SampleInBoundHandlerB", false))
                                    .addLast(new SampleErrorInBoundHandler("SampleInBoundHandlerC", true))
                                    //OutBoundHandler
                                    .addLast(new SampleOutBoundHandler("SampleOutBoundHandlerA"))
                                    .addLast(new SampleOutBoundHandler("SampleOutBoundHandlerB"))
                                    .addLast(new SampleOutBoundHandler("SampleOutBoundHandlerC"))
                                    //异常处理器
                                    .addLast(new ExceptionHandler());
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
