package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;
import juejin.netty.wechat.utils.SessionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsoleCommandManager implements ConsoleCommand {

    private final Map<String, ConsoleCommand> consoleCommandMap = new HashMap<>();

    public ConsoleCommandManager() {
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("sendToGroup", new SendToGroupConsoleCommand());
    }

    @Override
    public void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException {
        // 获取第一个指令
        String command = interruptReadLine(bufferedReader);
        if (!SessionUtil.hasLogin(channel)) {
            System.out.println("请先等待登录完毕...");
            return;
        }

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if (consoleCommand != null) {
            consoleCommand.exec(bufferedReader, channel);
        } else {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
        }
    }

}
