package juejin.netty.wechat.common.protocol.response;

import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.command.Command;
import juejin.netty.wechat.common.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;
    private boolean success;
    private String reason;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }

}
