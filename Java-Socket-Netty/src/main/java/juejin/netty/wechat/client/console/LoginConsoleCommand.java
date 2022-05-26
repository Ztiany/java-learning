package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.common.protocol.request.LoginRequestPacket;

import java.io.BufferedReader;
import java.io.IOException;

public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        // 准备数据
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        System.out.print("【登录】输入用户名登录: ");
        loginRequestPacket.setUsername(interruptReadLine(bufferedReader));
        System.out.print("请输入密码: ");
        loginRequestPacket.setPassword(interruptReadLine(bufferedReader));

        // 发送登录数据包
        channel.writeAndFlush(loginRequestPacket);

        // 等待登录成功
        waitForLoginResponse();
    }

    // TODO: 2022/5/26 超时等待
    private static void waitForLoginResponse() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }

}
