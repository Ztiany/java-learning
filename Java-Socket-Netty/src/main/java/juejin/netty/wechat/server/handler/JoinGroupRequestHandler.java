package juejin.netty.wechat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import juejin.netty.wechat.common.protocol.request.JoinGroupRequestPacket;
import juejin.netty.wechat.common.protocol.response.JoinGroupResponsePacket;
import juejin.netty.wechat.utils.SessionUtil;

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private JoinGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket requestPacket) {
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();

        // 1. 获取群对应的 channelGroup，然后将当前用户的 channel 添加进去
        String groupId = requestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup == null) {
            responsePacket.setReason("该群不存在");
            responsePacket.setSuccess(false);
        } else {
            channelGroup.add(ctx.channel());
            responsePacket.setSuccess(true);
            responsePacket.setGroupId(groupId);
        }

        // 2. 构造加群响应发送给客户端
        ctx.writeAndFlush(responsePacket);
    }

}