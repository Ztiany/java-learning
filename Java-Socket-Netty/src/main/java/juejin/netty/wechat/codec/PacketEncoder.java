package juejin.netty.wechat.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import juejin.netty.wechat.protocol.PacketCodec;
import juejin.netty.wechat.protocol.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        PacketCodec.get().encode(out, msg);
    }

}
