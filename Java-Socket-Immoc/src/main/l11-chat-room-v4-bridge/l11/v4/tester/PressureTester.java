package l11.v4.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import l11.v4.client.ServerInfo;
import l11.v4.client.TCPClient;
import l11.v4.client.UDPSearcher;
import l11.v4.clink.core.IoContext;
import l11.v4.clink.impl.IoSelectorProvider;
import l11.v4.clink.impl.SchedulerImpl;
import l11.v4.foo.Foo;

/**
 * 压力测试
 */
public class PressureTester {

    private static boolean done;

    public static void main(String[] args) throws IOException {
        File cachePath = Foo.getCacheDir("l6/tester");
        IoContext.setup()
                .ioProvider(new IoSelectorProvider())
                .scheduler(new SchedulerImpl(1))
                .start();

        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if (info == null) {
            return;
        }

        // 当前连接数量
        int size = 0;
        final List<TCPClient> tcpClients = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            try {
                TCPClient tcpClient = TCPClient.linkWith(info, cachePath);
                if (tcpClient == null) {
                    throw new NullPointerException();
                }

                tcpClients.add(tcpClient);

                System.out.println("连接成功：" + (++size));

            } catch (NullPointerException e) {
                System.out.println("连接异常");
                break;
            }
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        bufferedReader.readLine();

        Runnable runnable = () -> {
            while (!done) {
                for (TCPClient tcpClient : tcpClients) {
                    tcpClient.send("Hello~~");
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.setName("Test sender-----");
        thread.start();

        bufferedReader.readLine();

        // 等待线程完成
        done = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 客户端结束操作
        for (TCPClient tcpClient : tcpClients) {
            tcpClient.exit();
        }

        IoContext.close();
    }

}