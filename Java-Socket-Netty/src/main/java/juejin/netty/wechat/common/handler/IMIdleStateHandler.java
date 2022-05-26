package juejin.netty.wechat.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/*
 Netty 提供的用于检测非活跃连接的 IdleStateHandler。

    说明：连接假死的现象是，即在某一端（服务端或者客户端）看来，底层的 TCP 连接已经断开了，但是应用程序并没有捕获到，因此会认为这条连接仍然是存在的，
         从 TCP 层面来说，只有收到四次握手数据包或者一个 RST 数据包，连接的状态才表示已断开。

    连接假死会带来以下两大问题

        （1）对于服务端来说，因为每条连接都会耗费 cpu 和内存资源，大量假死的连接会逐渐耗光服务器的资源，最终导致性能逐渐下降，程序奔溃。
        （2）对于客户端来说，连接假死会造成发送数据超时，影响用户体验。

    通常，连接假死由以下几个原因造成的

        （1）应用程序出现线程堵塞，无法进行数据的读写。
        （2）客户端或者服务端网络相关的设备出现故障，比如网卡，机房故障。
        （3）公网丢包。公网环境相对内网而言，非常容易出现丢包，网络抖动等现象，如果在一段时间内用户接入的网络连续出现丢包现象，
            那么对客户端来说数据一直发送不出去，而服务端也是一直收不到客户端来的数据，连接就一直耗着。

    空闲检测：对于服务端来说，客户端的连接如果出现假死，那么服务端将无法收到客户端的数据，也就是说，如果能一直收到客户端发来的数据，那么可以说明这条连接还是活的，
            因此，服务端对于连接假死的应对策略就是空闲检测。空闲检测指的是每隔一段时间，检测这段时间内是否有数据读写，简化一下，我们的服务端只需要检测一段时间内，
            是否收到过客户端发来的数据即可，Netty 自带的 IdleStateHandler 就可以实现这个功能。


    IdleStateHandler 的构造函数有四个参数：
        第一个参数是读空闲时间，指的是在这段时间内如果没有数据读到，就表示连接假死；
        第二个参数是写空闲时间，指的是在这段时间如果没有写数据，就表示连接假死；
        第三个参数是读写空闲时间，表示在这段时间内如果没有产生数据读或者写，就表示连接假死。写空闲和读写空闲为 0，表示我们不关心者两类条件；
        最后一个参数表示时间单位。


    IMIdleStateHandler 的插入位置：要 IMIdleStateHandler 插入到最前面，因为如果插入到最后面的话，如果这条连接读到了数据，但是在 inBound 传播的过程中
                                 出错了或者数据处理完完毕就不往后传递了（我们的应用程序属于这类），那么最终 IMIdleStateHandler 就不会读到数据，最终导致误判。


    IMIdleStateHandler 包含了内部状态，因此不能被多个 Channel 共享。
 */
public class IMIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;

    public IMIdleStateHandler() {
        /* 表示的是：如果 15 秒内没有读到数据，就表示连接假死。  */
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        /* 连接假死之后会回调 channelIdle() 方法，我们这个方法里面打印消息，并手动关闭连接。 */
        ctx.channel().close();
    }

}
