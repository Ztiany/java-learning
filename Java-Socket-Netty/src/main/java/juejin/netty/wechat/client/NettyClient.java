package juejin.netty.wechat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import juejin.netty.wechat.client.console.ConsoleCommandManager;
import juejin.netty.wechat.client.console.LoginConsoleCommand;
import juejin.netty.wechat.client.handler.*;
import juejin.netty.wechat.common.codec.PacketDecoder;
import juejin.netty.wechat.common.codec.PacketEncoder;
import juejin.netty.wechat.common.handler.HeartbeatTimerHandler;
import juejin.netty.wechat.common.handler.IMIdleStateHandler;
import juejin.netty.wechat.common.protocol.PacketCodec;
import juejin.netty.wechat.utils.SessionUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static juejin.netty.wechat.Constant.*;

public class NettyClient {

    private static Thread consoleThread;

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        // 创建事件循环
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建启动器
        Bootstrap bootstrap = new Bootstrap();
        // 执行配置
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(@NotNull SocketChannel ch) {
                        ch.pipeline()
                                //in-bound
                                .addLast(new CheckHandler())//空闲检测
                                .addLast(new IMIdleStateHandler())//空闲检测
                                .addLast(PacketCodec.newProtocolDecoder())//拆包
                                .addLast(new PacketDecoder())//解码
                                .addLast(new LoginResponseHandler())//登录响应处理
                                .addLast(new MessageResponseHandler())//消息响应处理
                                .addLast(new CreateGroupResponseHandler())//建群响应处理
                                .addLast(new JoinGroupResponseHandler())//加入群响应处理
                                .addLast(new ListGroupMembersResponseHandler())//列出群成员响应处理
                                .addLast(new GroupMessageResponseHandler())//群消息响应处理
                                .addLast(new LogoutResponseHandler())//登出响应处理
                                //out-bound
                                .addLast(new PacketEncoder())//编码
                                //发送心跳包
                                .addLast(new HeartbeatTimerHandler());
                    }
                });

        /*
        attr() 方法可以给客户端 Channel，也就是 NioSocketChannel 绑定自定义属性，然后我们可以通过 channel.attr() 取出这个属性，
        下面码我们指定我们客户端 Channel 的一个 clientName 属性，属性值为 nettyClient，其实说白了就是给 NioSocketChannel 维护一个 map 而已，
        后续在这个 NioSocketChannel 通过参数传来传去的时候，就可以通过他来取出设置的属性，非常方便。
         */
        bootstrap.attr(AttributeKey.newInstance("clientName"), "nettyClient");
        // option() 方法可以给连接设置一些 TCP 底层相关的属性
        bootstrap
                // ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // ChannelOption.SO_KEEPALIVE 表示是否开启 TCP 底层心跳机制，true 为开启
                .option(ChannelOption.SO_KEEPALIVE, true)
                // ChannelOption.TCP_NODELAY 表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为 true 关闭，
                // 如果需要减少发送次数减少网络交互，就设置为 false 开启
                .option(ChannelOption.TCP_NODELAY, true);

        // 4.建立连接
        doConnect(bootstrap, HOST, PORT, 5);
    }

    /**
     * 通常情况下，连接建立失败不会立即重新连接，而是会通过一个指数退避的方式，比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接
     */
    private static void doConnect(Bootstrap bootstrap, String host, int port, int retry) {
        // 4.建立连接
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
                Channel channel = ((ChannelFuture) future).channel();
                // 连接成功之后，启动控制台线程
                startConsoleThread(bootstrap, channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                /*
                    定时任务是调用 bootstrap.config().group().schedule(), 其中 bootstrap.config() 这个方法返回的是 BootstrapConfig，其是对 Bootstrap 配置参数的抽象，
                    然后 bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，调 workerGroup 的 schedule 方法即可实现定时任务逻辑。
                 */
                bootstrap.config().group().schedule(() -> doConnect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Bootstrap bootstrap, Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        consoleThread = new Thread(() -> readConsoleAndSend(bootstrap, channel, bufferedReader, consoleCommandManager, loginConsoleCommand));
        consoleThread.start();
    }

    private static void readConsoleAndSend(Bootstrap bootstrap, Channel channel, BufferedReader bufferedReader, ConsoleCommandManager consoleCommandManager, LoginConsoleCommand loginConsoleCommand) {
        while (!clientExited) {
            if (!SessionUtil.hasLogin(channel)) {
                try {
                    loginConsoleCommand.exec(bufferedReader, channel);
                } catch (InterruptedException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    consoleCommandManager.exec(bufferedReader, channel);
                } catch (InterruptedException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stop(bootstrap.config().group(), channel);
    }

    private static void stop(EventLoopGroup eventLoopGroup, Channel channel) {
        ChannelFuture channelFuture = channel.close().awaitUninterruptibly();
        //you have to close eventLoopGroup as well
        eventLoopGroup.shutdownGracefully();
        channelFuture.addListener(future -> System.out.println("client exited"));
    }

    private volatile static boolean clientExited = false;

    public static void exit() {
        if (clientExited/*not safe but enough.*/) {
            return;
        }

        System.out.println("client exit");
        clientExited = true;
        if (consoleThread != null) {
            consoleThread.interrupt();
        }
    }

}