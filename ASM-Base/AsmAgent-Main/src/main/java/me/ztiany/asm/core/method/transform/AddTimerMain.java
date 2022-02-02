package me.ztiany.asm.core.method.transform;

/**
 * 给方法加上耗时统计
 */
public class AddTimerMain {

    /*
    java5: run with the following argument:
        1. edit configurations
        2. modify options
        3. add jvm options: -javaagent:D:\code\ztiany\Java-base-code\ASM-Base\AsmAgent\build\libs\AsmAgent-1.0.jar=your_arguments
        4. your_arguments is like {"applyingPackage":[\"me/ztiany\"],"adapterArguments":[],"adapterName":"ADD_TIMER"}
    */
    public static void main(String[] args) {
        methodA();
        methodB();
        methodC();
    }

    private static void methodA() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("methodA");
    }

    private static void methodB() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("methodB");
    }

    private static void methodC() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("methodC");
    }

}
