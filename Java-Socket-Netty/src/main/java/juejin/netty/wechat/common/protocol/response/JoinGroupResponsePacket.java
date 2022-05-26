package juejin.netty.wechat.common.protocol.response;

import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class JoinGroupResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }

}
