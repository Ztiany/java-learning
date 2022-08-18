package me.ztiany.asm.core.method.transform;

public class AddTimberDemoClass {

    public void m() throws Exception {
        long time = System.currentTimeMillis();
        Thread.sleep(100);
        time = System.currentTimeMillis() - time;
        System.out.println("m(): " + time);
    }

}
