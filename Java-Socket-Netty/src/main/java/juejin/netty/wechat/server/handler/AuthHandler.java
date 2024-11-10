package juejin.netty.wechat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juejin.netty.wechat.utils.SessionUtil;
import org.jetbrains.annotations.NotNull;

/*
    在登录成功之后，我们通过给 channel 打上属性标记的方式，标记这个 channel 已成功登录【参考 】，那么，接下来，我们是不是需要在后续的每一种指令的处理前，
    都要来判断一下用户是否登录？

    判断一个用户是否登录很简单，只需要调用一下 LoginUtil.hasLogin(channel) 即可，但是，Netty 的 pipeline 机制帮我们省去了重复添加同一段逻辑的烦恼，
    我们只需要在后续所有的指令处理 handler 之前插入一个用户认证 handle 即可。

    下面的  AuthHandler 就实现了用户认证功能。
        （1）AuthHandler 继承自 ChannelInboundHandlerAdapter，覆盖了 channelRead() 方法，表明它可以处理所有类型的数据。
        （2）在 channelRead() 方法里面，在决定是否把读到的数据传递到后续指令处理器之前，首先会判断是否登录成功，如果未登录，直接强制关闭连接【当然生产环境的逻辑会更加复杂】。
  */
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        System.out.println("AuthHandler 收到消息：" + msg);
        if (!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            /*
                如果客户端已经登录成功了，那么在每次处理客户端数据之前，我们都要经历这么一段逻辑，比如，平均每次用户登录之后发送 100 次消息，
                其实剩余的 99 次身份校验逻辑都是没有必要的，因为只要连接未断开，客户端只要成功登录过，后续就不需要再进行客户端的身份校验。

                对于 Netty 的设计来说，handler 其实可以看做是一段功能相对聚合的逻辑，然后通过 pipeline 把这些一个个小的逻辑聚合起来，串起一个功能完整的逻辑链。
                既然可以把逻辑串起来，也可以做到动态删除一个或多个逻辑。

                下面代码，判断如果已经经过权限认证，那么就直接调用 pipeline 的 remove() 方法删除自身，这里的 this 指的其实就是 AuthHandler 这个对象，
                删除之后，这条客户端连接的逻辑链中就不再有这段逻辑了。
             */
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        //这里主要用于打印一些信息，不影响实际功能
        if (SessionUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }

}