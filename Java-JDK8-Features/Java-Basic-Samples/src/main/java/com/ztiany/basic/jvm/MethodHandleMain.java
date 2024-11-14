package com.ztiany.basic.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleMain {

    public void print(String s) {
        System.out.println("hello, " + s);
    }


    public static void main(String[] args) throws Throwable {
        MethodHandleMain foo = new MethodHandleMain();
        MethodType methodType = MethodType.methodType(void.class, String.class);
        MethodHandle methodHandle = MethodHandles.lookup().findVirtual(MethodHandleMain.class, "print", methodType);
        methodHandle.invokeExact(foo, "world");
    }

}