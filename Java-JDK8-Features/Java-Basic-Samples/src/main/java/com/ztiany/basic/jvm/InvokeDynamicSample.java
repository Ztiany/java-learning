package com.ztiany.basic.jvm;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * invokedynamic 是在 Java 7 中新增的字节码调用指令，作为 Java 支持动态类型语言的改进之一，
 * 在 Java 8 开始应用，Lambda 表达式底层就依靠该指令来实现。invokedynamic 指令在常量池中
 * 并没有包含其目标方法的具体符号信息，存储的是 BootstapMethod 信息，在运行时再来通过引
 * 导方法机制动态确定方法的所属者和类型。
 */
public class InvokeDynamicSample {

    private void lambda() {
        Runnable runnable = () -> System.out.println("Hello World!");
        runnable.run();
    }

    public static void main(String[] args) {
        new InvokeDynamicSample().lambda();
        try {
            InvokeDynamicSample.class.getDeclaredMethod("lambda$lambda$0").invoke(null);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            Logger.getGlobal().log(Level.INFO, "InvokeDynamicSample", e);
        }
    }

}
