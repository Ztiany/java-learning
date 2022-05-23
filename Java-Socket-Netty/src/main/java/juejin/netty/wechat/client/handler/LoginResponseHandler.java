package juejin.netty.wechat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import juejin.netty.wechat.protocol.request.LoginRequestPacket;
import juejin.netty.wechat.protocol.response.LoginResponsePacket;
import juejin.netty.wechat.utils.LoginUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

/*
 当我们要处理的指令越来越多的时候，代码会显得越来越臃肿，各种消息类型都在一个 Handler 内处理：

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                ByteBuf byteBuf = (ByteBuf) msg;
                Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

                if (packet instanceof LoginResponsePacket) {
                    LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
                    if (loginResponsePacket.isSuccess()) {
                        System.out.println(new Date() + ": 客户端登录成功");
                        LoginUtil.markAsLogin(ctx.channel());
                    } else {
                        System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
                    }
                } else if (packet instanceof MessageResponsePacket) {
                    MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
                    System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
                }
            }

    我们可以通过给 pipeline 添加多个 handler(ChannelInboundHandlerAdapter的子类) 来解决在单个 ChannelInboundHandlerAdapter
    内过多的 if else 问题，让每个 ChannelInboundHandlerAdapter 处理一个特定的消息，此时每个 ChannelInboundHandlerAdapter 的 channelRead
    方法可以按如下方式实现：

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                ByteBuf byteBuf = (ByteBuf) msg;
                Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

                if (packet instanceof XXXPacket) {
                    // ...处理自己对应的 XXXPacket 的逻辑
                } else {
                    // 转发到其他的 Handler
                    ctx.fireChannelRead(packet);
                }
            }

    但是这种做法还不够优雅，上面的代码依然编写了一段我们其实可以不用关心的 if else 判断，然后还要手动传递无法处理的对象 (XXXPacket) 至下一个指令处理器，
    这也是一段重复度极高的代码，因此，Netty 基于这种考虑抽象出了一个 SimpleChannelInboundHandler 对象，类型判断和对象传递的活都自动帮我们实现了，
    而我们可以专注于处理我们所关心的指令即可。

    SimpleChannelInboundHandler 是一个泛型类型，创建 SimpleChannelInboundHandler 子类是传递的实际类型参数就是这个 SimpleChannelInboundHandler
    需要处理的类型。
*/
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");
        // 写数据
        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) {
        if (msg.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + msg.getReason());
        }
    }

}