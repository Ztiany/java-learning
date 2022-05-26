package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.GroupMessageRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;

public class SendToGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        System.out.print("【发送消息给某个某个群组】请输入 groupId：");
        String toGroupId = interruptReadLine(bufferedReader);
        System.out.print("【请输入要发送的群消息：");
        String message = interruptReadLine(bufferedReader);

        channel.writeAndFlush(new GroupMessageRequestPacket(toGroupId, message));

    }
}
