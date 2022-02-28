package me.ztiany.agent.test.jdk6;

import java.util.concurrent.TimeUnit;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2020/12/14 15:58
 */
public class JDK6AgentTest {

    public static void main(String... args) throws InterruptedException {
        while (true) {
            System.out.println(foo());
            TimeUnit.SECONDS.sleep(3);
        }
    }

    public static int foo() {
        return 100; // 修改后 return 50;
    }

}
