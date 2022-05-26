package juejin.netty.wechat.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.PacketCodec;

import java.util.List;

/*
 * Netty 内部提供了一个类，叫做 MessageToMessageCodec，使用它可以让我们的编解码操作放到一个类里面去实现：
 *
 *  说明：
 *  （1）首先，这里 PacketCodecHandler 是一个无状态的 handler，因此可以使用单例模式来实现。
 *  （2）我们看到，我们需要实现 decode() 和 encode() 方法，decode 是将二进制数据 ByteBuf 转换为 java 对象 Packet，
 *      而 encode 操作是一个相反的过程，在 encode() 方法里面，我们调用了 channel 的 内存分配器手工分配了 ByteBuf。
 *
 * 添加了 IMCodecHandler，那么 PacketDecoder 和 PacketEncoder 就可以移除了。
 *
 * 另外，从代码可以看出 IMCodecHandler 没有内部状态，因此可以被多个 Channel 共享，而需要共享 Handler 需要添加 ChannelHandler.Sharable 注解。
 */
@ChannelHandler.Sharable
public class IMCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    public static final IMCodecHandler INSTANCE = new IMCodecHandler();

    private IMCodecHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodec.get().encode(byteBuf, msg);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        out.add(PacketCodec.get().decode(msg));
    }

}