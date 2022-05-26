package juejin.netty.wechat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import juejin.netty.wechat.common.protocol.request.LoginRequestPacket;
import juejin.netty.wechat.common.protocol.response.LoginResponsePacket;
import juejin.netty.wechat.common.session.Session;
import juejin.netty.wechat.utils.IDUtil;
import juejin.netty.wechat.utils.SessionUtil;

@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    private LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        System.out.println("收到客户端[" + msg.getUsername() + "]的登录请求……");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(msg.getVersion());
        loginResponsePacket.setUserName(msg.getUsername());

        if (SessionUtil.verifyLoginRequest(msg)) {
            loginResponsePacket.setSuccess(true);
            loginResponsePacket.setUserId(IDUtil.createRandomId());

            System.out.println("[" + msg.getUsername() + "]登录成功");
            SessionUtil.bindSession(new Session(loginResponsePacket.getUserId(), msg.getUsername()), ctx.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(msg.getUsername() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

}
