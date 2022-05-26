package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.QuitGroupRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;

public class QuitGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();
        System.out.print("【退出群聊】输入要退出群的 groupId：");
        String groupId = interruptReadLine(bufferedReader);
        quitGroupRequestPacket.setGroupId(groupId);

        channel.writeAndFlush(quitGroupRequestPacket);
    }

}
