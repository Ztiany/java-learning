package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.ListGroupMembersRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;

public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();
        System.out.print("【获取群成员列表】请输入 groupId，：");
        String groupId = interruptReadLine(bufferedReader);

        listGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }

}
