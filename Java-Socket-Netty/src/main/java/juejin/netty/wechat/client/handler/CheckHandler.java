package juejin.netty.wechat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juejin.netty.wechat.client.NettyClient;
import org.jetbrains.annotations.NotNull;

public class CheckHandler extends ChannelInboundHandlerAdapter {


    /**
     * In Netty, channelInactive() is a method defined in the ChannelHandler interface that gets called when a Channel becomes inactive,
     * which typically means that it has been closed or disconnected.
     * <p>
     * When a Channel becomes inactive, it means that any resources associated with it, such as network connections, file descriptors,
     * or buffers, are no longer available. As a result, the channelInactive() method can be used to perform cleanup tasks, such as releasing any resources held by the ChannelHandler.
     * <p>
     * Some of the tasks that might be performed in the channelInactive() method include:
     *
     * <ol>
     * <li>Closing any open files or streams associated with the ChannelHandler</li>
     * <li>Releasing any allocated memory or buffers used by the ChannelHandler</li>
     * <li>Logging information about the Channel being closed or disconnected</li>
     * <li>Cleaning up any external resources, such as network connections or database connections</li>
     * </ol>
     * <p>
     * In summary, the channelInactive() method in Netty provides a way for developers to perform cleanup tasks when a Channel becomes inactive, which can help ensure that resources are properly released and that the application runs smoothly.
     */
    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NettyClient.exit();
    }

}