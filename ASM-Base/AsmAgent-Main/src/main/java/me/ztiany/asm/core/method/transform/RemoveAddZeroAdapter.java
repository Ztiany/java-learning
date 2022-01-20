package me.ztiany.asm.core.method.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 移除方法中，加 0 的指令
 */
public class RemoveAddZeroAdapter {

    public static void main(String[] args) throws IOException {
        printOriginalDemoClass();
    }

    private static void printOriginalDemoClass() throws IOException {
        ClassWriter cw = new ClassWriter(0);
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);
        new ClassReader(DemoClass.class.getName()).accept(cv, 0);
    }

    private static class DemoClass {

        public void addZero(int param) {
            int result = param + 0;
            System.out.println(result);
        }

        public void multipleZero(int param) {
            int result = param * 0;
            System.out.println(result);
        }

    }

}
