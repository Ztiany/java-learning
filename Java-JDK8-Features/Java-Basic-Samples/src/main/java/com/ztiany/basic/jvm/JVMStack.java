package com.ztiany.basic.jvm;

/**
 * 栈帧之间数据的共享。
 */
public class JVMStack {

    public int work(int x) throws Exception{
        int z =(x+5)*10;//局部变量表有
        Thread.sleep(Integer.MAX_VALUE);
        return  z;
    }

    public static void main(String[] args)throws Exception {
        JVMStack jvmStack = new JVMStack();
        jvmStack.work(10);//10  放入main 栈帧操作数栈
    }

}
