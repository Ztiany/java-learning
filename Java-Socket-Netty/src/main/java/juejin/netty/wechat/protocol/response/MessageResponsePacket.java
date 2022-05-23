package juejin.netty.wechat.protocol.response;

import juejin.netty.wechat.protocol.Packet;
import juejin.netty.wechat.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }

}
