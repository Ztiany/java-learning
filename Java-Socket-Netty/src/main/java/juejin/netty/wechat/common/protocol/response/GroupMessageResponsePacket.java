package juejin.netty.wechat.common.protocol.response;

import juejin.netty.wechat.common.protocol.Packet;
import juejin.netty.wechat.common.protocol.command.Command;
import juejin.netty.wechat.common.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群消息响应，用于将某个用户发送的群消息发送给其他群成员
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;
    private Session fromUser;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }

}
