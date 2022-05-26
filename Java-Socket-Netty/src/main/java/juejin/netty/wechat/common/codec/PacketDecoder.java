package juejin.netty.wechat.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import juejin.netty.wechat.common.protocol.PacketCodec;

import java.util.List;

/*
 通常情况下，无论我们是在客户端还是服务端，当我们收到数据之后，首先要做的事情就是把二进制数据转换到我们的一个 Java 对象，
 Netty 定义了 ByteToMessageDecoder 抽象类来专门做这个事情，传递进来的时候就已经是 ByteBuf 类型，所以我们不再需要强转，
 第三个参数是 List 类型，我们通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递，
 这样，我们就实现了解码的逻辑 handler。 传递进来的时候就已经是 ByteBuf 类型，所以我们不再需要强转，第三个参数是 List 类型，
 我们通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递，这样，我们就实现了解码的逻辑 handler。

 另外这里使用 ByteToMessageDecoder，Netty 会自动进行内存的释放，我们不用操心太多的内存管理方面的逻辑。
*/
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(PacketCodec.get().decode(in));
    }

}
