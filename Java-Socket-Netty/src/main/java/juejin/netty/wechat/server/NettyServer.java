package juejin.netty.wechat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import juejin.netty.wechat.codec.PacketDecoder;
import juejin.netty.wechat.codec.PacketEncoder;
import juejin.netty.wechat.protocol.PacketCodec;
import juejin.netty.wechat.server.handler.AuthHandler;
import juejin.netty.wechat.server.handler.LifecycleTestHandler;
import juejin.netty.wechat.server.handler.LoginRequestHandler;
import juejin.netty.wechat.server.handler.MessageRequestHandler;
import org.jetbrains.annotations.NotNull;

import static juejin.netty.wechat.Constant.PORT;

public class NettyServer {

    public static void main(String[] args) {
        new NettyServer().start();
    }

    private void start() {
        // 创建主 Reactor 事件循环组，也叫 BossGroup，用于执行 accept。【本质是其就是一个线程池，主 Reactor 默认只有一个线程】
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 创建从 Reactor 事件循环组，也叫 WorkerGroup，用于处理每一条连接的数据读写【本质是其就是一个线程池，从 Reactor 默认线程数量= CPU 核数】
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //开始配置
        serverBootstrap
                // 给引导类配置两大线程组，这个引导类的线程模型也就定型了。
                .group(bossGroup, workerGroup)
                // 通过.channel(NioServerSocketChannel.class)来指定 IO 模型
                .channel(NioServerSocketChannel.class)
                // 给服务端channel设置一些属性，最常见的就是 so_backlog
                .option(ChannelOption.SO_BACKLOG, 1024)
                // childOption() 可以给每条连接设置一些TCP底层相关的属性
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                // attr() 方法可以给服务端的 channel，也就是 NioServerSocketChannel 指定一些自定义属性，然后我们可以通过 channel.attr() 取出这个属性
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                // childAttr() 可以给每一条连接指定自定义属性，然后后续我们可以通过 channel.attr() 取出该属性。
                .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
                // handler() 用于指定在服务端启动过程中的一些逻辑，通常情况下，我们用不着这个方法。
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(@NotNull NioServerSocketChannel ch) {
                        //nothing need to do.
                    }
                })
                // childHandler() 用于指定处理新连接数据的读写处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(@NotNull NioSocketChannel ch) {
                        // ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式
                        ch.pipeline()
                                //in-bound
                                .addLast(new LifecycleTestHandler())//打印生命周期【测试用】
                                .addLast(PacketCodec.newProtocolDecoder())//拆包
                                .addLast(new PacketDecoder())//解码
                                .addLast(new LoginRequestHandler())//处理登录请求
                                .addLast(new AuthHandler())//鉴权
                                .addLast(new MessageRequestHandler())//处理一般的消息
                                //out-bound
                                .addLast(new PacketEncoder());//编码
                    }
                });

        //启动服务器
        doBind(serverBootstrap, PORT);
    }

    private void doBind(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                doBind(serverBootstrap, port);
            }
        });
    }

}