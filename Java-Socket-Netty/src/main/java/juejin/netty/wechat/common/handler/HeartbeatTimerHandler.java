package juejin.netty.wechat.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juejin.netty.wechat.common.protocol.request.HeartbeatRequestPacket;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/*
    服务端在一段时间内没有收到客户端的数据，这个现象产生的原因可以分为以下两种：

        （1）连接假死。
        （2）非假死状态下确实没有发送数据。

    第一个中情况由 Netty 提供的 IdleStateHandler 可以检测出来， 我们只需要排除掉第二种可能性，那么连接自然就是假死的。要排查第二种情况，我们可以在客户端定期发送数据到服务端，
    通常这个数据包称为心跳数据包。

    HeartBeatTimerHandler 继承了 ChannelInboundHandlerAdapter，但是其并没有对深入做任何操作，只是在建立连接之后，定期发送心跳消息。
*/
public class HeartbeatTimerHandler extends ChannelInboundHandlerAdapter {

    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        scheduleSendHeartBeat(ctx);
        super.channelActive(ctx);
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                //调用 ctx.writeAndFlush 而不是 ctx.channel() 的前提是 HeartbeatTimerHandler 在 Client 中的位置在 PacketEncoder 的后面。
                ctx.writeAndFlush(new HeartbeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

}