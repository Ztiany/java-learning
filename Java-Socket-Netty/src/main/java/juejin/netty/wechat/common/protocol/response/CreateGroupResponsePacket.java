package juejin.netty.wechat.common.protocol.response;

import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateGroupResponsePacket extends Packet {

    private boolean success;
    private String reason;
    private String groupId;
    private List<String> memberNameList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }

}
