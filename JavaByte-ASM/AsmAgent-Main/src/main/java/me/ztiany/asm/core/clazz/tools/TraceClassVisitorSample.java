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
        ClassWriter cw = new ClassWriter(0);
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);
        new ClassReader(Runnable.class.getName()).accept(cv, 0);
    }

}
