package juejin.netty.wechat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import juejin.netty.wechat.common.codec.IMCodecHandler;
import juejin.netty.wechat.common.handler.HeartbeatRequestHandler;
import juejin.netty.wechat.common.handler.IMIdleStateHandler;
import juejin.netty.wechat.common.protocol.PacketCodec;
import juejin.netty.wechat.server.handler.*;
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
                        /*
                        ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式。

                        每建立一个新的连接，这里的代码就被会被执行，对于服务端来说，因为可能承接成千上万个客户端的连接，因此这里可以做一些特定的优化
                            （1）压缩 handler 的链条，利用一个 Handler 来管理那些没有状态的 Handler，这个可以压缩 handler 的传播链条，从而优化响应速度。【比如这里的 ComposeHandler】
                            （2）没有状态的 Handler 可以用单例来实现，从而避免为每个连接创建一个对象。
                         */
                        ch.pipeline()
                                //打印生命周期【测试用】
                                //.addLast(new LifecycleTestHandler())
                                //空闲检测
                                .addLast(new IMIdleStateHandler())
                                //拆包
                                .addLast(PacketCodec.newProtocolDecoder())
                                //编解解码
                                .addLast(IMCodecHandler.INSTANCE)
                                //处理登录请求
                                .addLast(LoginRequestHandler.INSTANCE)
                                //处理心跳消息
                                .addLast(HeartbeatRequestHandler.INSTANCE)
                                //鉴权
                                .addLast(AuthHandler.INSTANCE)
                                //其他需要鉴权的 Handler
                                .addLast(ComposeHandler.INSTANCE);
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