package juejin.netty.wechat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import juejin.netty.wechat.protocol.command.Command;
import juejin.netty.wechat.protocol.request.LoginRequestPacket;
import juejin.netty.wechat.protocol.request.MessageRequestPacket;
import juejin.netty.wechat.protocol.response.LoginResponsePacket;
import juejin.netty.wechat.protocol.response.MessageResponsePacket;
import juejin.netty.wechat.serialization.Algorithms;
import juejin.netty.wechat.serialization.JSONSerializer;
import juejin.netty.wechat.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class PacketCodec {

    private static final PacketCodec codec = new PacketCodec();

    public static PacketCodec get() {
        return codec;
    }

    private static final int MAGIC_NUMBER = 0x12345678;

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;

    private final Map<Byte, Serializer> serializerMap;

    private PacketCodec() {
        //指令对应的 Package 类型
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);

        //序列化器
        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getAlgorithm(), serializer);
    }

    private Serializer getDefaultSerializer() {
        return serializerMap.get(Algorithms.JSON);
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        // 获取序列化器
        Serializer defaultSerializer = getDefaultSerializer();
        byte[] data = defaultSerializer.serialize(packet);

        // 执行编码
        byteBuf.writeInt(MAGIC_NUMBER);//魔数
        byteBuf.writeByte(packet.getVersion());//协议版本
        byteBuf.writeByte(defaultSerializer.getAlgorithm());//序列化方式
        byteBuf.writeByte(packet.getCommand());//指令
        byteBuf.writeInt(data.length);//实体数据长度
        byteBuf.writeBytes(data);//实体数据
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过魔数
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 读取序列化方式，一个字节
        byte serializationAlgorithm = byteBuf.readByte();
        Serializer serializer = getSerializer(serializationAlgorithm);
        // 读取指令
        byte command = byteBuf.readByte();
        // 读取长度
        int length = byteBuf.readInt();
        // 读取数据
        byte[] data = new byte[length];
        byteBuf.readBytes(data);
        // 根据指令获取对应的 Packet 类型
        Class<? extends Packet> clazz = getRequestType(command);

        if (clazz == null) {
            throw new IllegalStateException(String.format("unsupported command %d", command));
        }
        if (serializer == null) {
            throw new IllegalStateException(String.format("unsupported serialization algorithm %d", serializationAlgorithm));
        }

        // 执行反序列化
        return serializer.deserialize(clazz, data);
    }

    private static final int MAGIC_NUM_LENGTH = 4;
    private static final int VERSION_LENGTH = 1;
    private static final int COMMAND_LENGTH = 1;
    private static final int SERIALIZATION_LENGTH = 1;
    private static final int DATA_LENGTH = 4;

    /**
     * 构建 LengthFieldBasedFrameDecoder 来解决粘包半包现象
     */
    public static LengthFieldBasedFrameDecoder newProtocolDecoder() {
        return new LengthFieldBasedFrameDecoder(
                Integer.MAX_VALUE,
                MAGIC_NUM_LENGTH + VERSION_LENGTH + COMMAND_LENGTH + SERIALIZATION_LENGTH,
                DATA_LENGTH
        ) {

            //这里的 decode() 方法中，第二个参数 in，每次传递进来的时候，均为一个数据包的开头。
            @Override
            protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                // 屏蔽非本协议的客户端【getInt 方法不改变索引】
                // 设计魔数的原因是为了尽早屏蔽非本协议的客户端，通常在第一个 handler 处理这段逻辑。
                if (in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
                    System.err.println("MAGIC_NUMBER not match.");
                    ctx.channel().close();
                    return null;
                }
                return super.decode(ctx, in);
            }
        };
    }

}
