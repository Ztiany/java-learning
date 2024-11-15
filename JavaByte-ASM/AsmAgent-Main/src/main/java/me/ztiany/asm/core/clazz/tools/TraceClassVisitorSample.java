package me.ztiany.asm.core.clazz.tools;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * TraceClassVisitorSample 以可读文本的方式打印一个类的字节代码。
 */
public class TraceClassVisitorSample {

    public static void main(String[] args) throws IOException {
        parse(Runnable.class.getName());
        parse(ClassWithLambda.class.getName());
        parse(ClassWithCheckedLambda.class.getName());
        parse(ViewClick.class.getName());
        parse(ViewClickChecked.class.getName());
        parse(ViewClickWith1PChecked.class.getName());
        parse(ViewClickWith2PChecked.class.getName());
    }

    private static void parse(String className) throws IOException {
        System.err.println("----------------------------------------------------------");
        System.err.println("----------------------------------------------------------");
        ClassWriter cw = new ClassWriter(0);
        PrintWriter printWriter = new PrintWriter(System.err);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);
        new ClassReader(className).accept(cv, 0);
    }

}
