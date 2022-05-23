package juejin.netty.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Client {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        new Client().start();
    }

    private void start() {
        // 创建事件循环
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建启动器
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(@NotNull SocketChannel ch) {
                        ch.pipeline().addLast(new FirstClientInboundHandler());
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
        doConnect(bootstrap, 5);
    }

    /**
     * 通常情况下，连接建立失败不会立即重新连接，而是会通过一个指数退避的方式，比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接
     */
    private void doConnect(Bootstrap bootstrap, int retry) {
        // 4.建立连接
        bootstrap.connect("127.0.0.1", 8088).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
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
                bootstrap.config().group().schedule(() -> doConnect(bootstrap, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }
}