package me.ztiany.agent.test.jdk5;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class JDK5AgentTest {

    /*
        java5: run with the following argument:
            1. edit configurations
            2. modify options
            3. add jvm options:  -javaagent:D:\code\ztiany\Java-base-code\ASM-Base\JDK5AgentLib\build\libs\JavaAgent.jar=World--------Hello
     */
    public static void main(String... args) throws InterruptedException {
        while (true) {
            System.out.println(getGreeting());
            Thread.sleep(1000L);
        }
    }

    public static String getGreeting() {
        return "hello world";
    }

}
