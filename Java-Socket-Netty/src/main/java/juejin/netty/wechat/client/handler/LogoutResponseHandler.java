package juejin.netty.wechat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import juejin.netty.wechat.client.NettyClient;
import juejin.netty.wechat.common.protocol.response.LogoutResponsePacket;
import juejin.netty.wechat.utils.SessionUtil;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        SessionUtil.unbindSession(ctx.channel());
        NettyClient.exit();
    }

}
