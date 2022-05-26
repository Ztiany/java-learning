package juejin.netty.wechat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juejin.netty.wechat.client.NettyClient;
import org.jetbrains.annotations.NotNull;

public class CheckHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NettyClient.exit();
    }

}
