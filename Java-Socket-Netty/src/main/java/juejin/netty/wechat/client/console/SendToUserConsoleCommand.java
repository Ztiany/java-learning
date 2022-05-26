package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.MessageRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;

public class SendToUserConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        System.out.print("【发送消息给某个用户】请输入对方的用户 id：");
        String toUserId = interruptReadLine(bufferedReader);
        System.out.print("请输入需要发送的消息：");
        String message = interruptReadLine(bufferedReader);
        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
    }

}
