package juejin.netty.wechat.server.handler;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.command.Command;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class ComposeHandler extends SimpleChannelInboundHandler<Packet> {

    public static final ComposeHandler INSTANCE = new ComposeHandler();

    private final Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap = new HashMap<>();

    private ComposeHandler() {
        handlerMap.put(Command.MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(Command.LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        SimpleChannelInboundHandler<? extends Packet> simpleChannelInboundHandler = handlerMap.get(msg.getCommand());
        if (simpleChannelInboundHandler != null) {
            simpleChannelInboundHandler.channelRead(ctx, msg);
        } else {
            System.out.println(msg.getCommand() + " is not support.");
        }
    }

}
