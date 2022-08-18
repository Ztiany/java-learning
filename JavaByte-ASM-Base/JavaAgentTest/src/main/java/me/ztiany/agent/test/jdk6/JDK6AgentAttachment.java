package me.ztiany.agent.test.jdk6;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;

public class JDK6AgentAttachment {

    /*
    java6: run with the following argument:
        1. edit configurations
        2. modify options
        3. add  options: -cp D:\code\ztiany\Java-base-code\ASM-Base\JavaAgentLib\build\libs\JavaAgent.jar:.
    */
    public static void main(String[] args) throws Exception {
        VirtualMachine vm = VirtualMachine.attach("20328");
        try {
            File file = new File("JavaAgentLib/build/libs/JavaAgent.jar");
            System.out.println(file.getAbsolutePath());
            vm.loadAgent(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            vm.detach();
        }
    }

}
