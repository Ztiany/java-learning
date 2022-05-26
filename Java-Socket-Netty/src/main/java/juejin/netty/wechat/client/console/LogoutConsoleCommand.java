package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.LogoutRequestPacket;

import java.io.BufferedReader;

public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) {
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        channel.writeAndFlush(logoutRequestPacket);
    }

}