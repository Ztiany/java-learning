package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.CreateGroupRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITTER = ",";

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
        System.out.print("【建立群聊】请输入需要拉入群聊的 userId 列表，userId 之间英文逗号隔开：");
        String userIds = interruptReadLine(bufferedReader);
        createGroupRequestPacket.setUserIdList(Arrays.asList(userIds.split(USER_ID_SPLITTER)));

        channel.writeAndFlush(createGroupRequestPacket);
    }

}