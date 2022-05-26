package juejin.netty.wechat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import juejin.netty.wechat.common.protocol.request.QuitGroupRequestPacket;
import juejin.netty.wechat.common.protocol.response.QuitGroupResponsePacket;
import juejin.netty.wechat.utils.SessionUtil;

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    private QuitGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket requestPacket) {
        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();

        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 移除
        String groupId = requestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup != null) {
            channelGroup.remove(ctx.channel());
            responsePacket.setGroupId(requestPacket.getGroupId());
            responsePacket.setSuccess(true);
        } else {
            responsePacket.setSuccess(false);
            responsePacket.setReason("该群不存在");
        }

        // 2. 构造退群响应发送给客户端
        ctx.writeAndFlush(responsePacket);
    }

}