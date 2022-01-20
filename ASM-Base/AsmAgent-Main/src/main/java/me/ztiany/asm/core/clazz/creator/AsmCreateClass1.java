package me.ztiany.asm.core.clazz.creator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

import static org.objectweb.asm.Opcodes.*;


/**
 * 类的创建：构建一个新的类。
 */
public class AsmCreateClass1 {

    public static void main(String... args) throws ClassNotFoundException {

        final byte[] aClass = createClass();

        Class exampleClass = new ClassLoader() {
            @SuppressWarnings("unchecked")
            protected Class findClass(String name) {
                return defineClass(name, aClass, 0, aClass.length);
            }
        }.loadClass("pkg.Comparable");

        System.out.println();
        System.out.println(exampleClass);
    }

    private static byte[] createClass() {

        /*
        package pkg;
        public interface Comparable  {
            int LESS = -1;
            int EQUAL = 0;
            int GREATER = 1;
            int compareTo(Object o);
        }
         */

        ClassWriter cw = new ClassWriter(0);
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);

        //类的声明
        cv.visit(
                //V1_5 表示 java 字节码的版本
                V1_5,
                //ACC_XXX 常量是与 Java 修饰符对应的标志。这里规定这个类是一个接口，而且它是 public 和 abstract 的
                ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                //以内部形式规定类的名称
                "pkg/Comparable",
                //泛型
                null,
                //以内部形式规定类的超类名称
                "java/lang/Object",
                //一个数组，其中是被扩展的接口，这些接口由其内部名指定。
                null);

        //添加三个字段
        //创建字段，每一个部分都是一个子过程，所以也对应一个 visitEnd 方法
        cv.visitField(
                //第一个参数是一组标志，对应于 Java 修饰符。
                ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                //第二个参数是字段的名字，与它在源代码中的显示相同。
                "LESS",
                //第三个参数是字段的类型，采用类型描述符形式。
                "I",
                //第四个参数对应于泛型。
                null,
                //最后一个参数是字段的常量值，这个参数必须仅用于真正的常量字段，也就是 final static 字段。对于其他字段，它必须为 null。
                -1).visitEnd();
        cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, 0).visitEnd();
        cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, 1).visitEnd();

        /*添加一个方法*/

        //创建方法
        cv.visitMethod(
                //第一个参数是一组对应于 Java 修饰符的标志。
                ACC_PUBLIC + ACC_ABSTRACT,
                //第二个参数是方法名，与其在源代码中的显示一样。
                "compareTo",
                //第三个参数是方法的描述符。
                "(Ljava/lang/Object;)I",
                //第四个参数对应于泛型。
                null,
                //最后一个参数是一个数组，其中包括可由该方法抛出的异常，这些异常由其内部名指明。
                null
        ).visitEnd();

        //对 visitEnd 的最后一个调用是为了通知 cw：这个类已经结束
        cv.visitEnd();

        //对 toByteArray 的调用用于以字节数组的形式提取它。
        return cw.toByteArray();
    }

}