package juejin.netty.wechat.common.protocol.response;

import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HeartbeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }

}
