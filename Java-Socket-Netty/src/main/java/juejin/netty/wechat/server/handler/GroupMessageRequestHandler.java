package juejin.netty.wechat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import juejin.netty.wechat.common.protocol.request.GroupMessageRequestPacket;
import juejin.netty.wechat.common.protocol.response.GroupMessageResponsePacket;
import juejin.netty.wechat.utils.SessionUtil;

@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket requestPacket) {
        // 1. 拿到群聊对应的 channelGroup，写到每个客户端
        String groupId = requestPacket.getToGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup == null) {
            System.out.println("群[" + groupId + "]不存在，无法发送消息到该群");
            return;
        }

        // 2.拿到 groupId 构造群聊消息的响应
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setFromGroupId(groupId);
        responsePacket.setMessage(requestPacket.getMessage());
        responsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));

        channelGroup.writeAndFlush(responsePacket);
    }

}