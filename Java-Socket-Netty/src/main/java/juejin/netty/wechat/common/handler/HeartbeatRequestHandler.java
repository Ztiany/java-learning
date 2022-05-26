package juejin.netty.wechat.common.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import juejin.netty.wechat.common.protocol.request.HeartbeatRequestPacket;
import juejin.netty.wechat.common.protocol.response.HeartbeatResponsePacket;

@ChannelHandler.Sharable
public class HeartbeatRequestHandler extends SimpleChannelInboundHandler<HeartbeatRequestPacket> {

    public static final HeartbeatRequestHandler INSTANCE = new HeartbeatRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatRequestPacket requestPacket) {
        ctx.writeAndFlush(new HeartbeatResponsePacket());
    }

}
