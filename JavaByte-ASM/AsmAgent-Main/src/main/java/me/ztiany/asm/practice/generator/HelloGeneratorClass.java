package me.ztiany.asm.practice.generator;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 参考：<a href="https://blog.csdn.net/catoop/article/details/50629921">通过 ASM 生成类的字节码</a>。
 */
public class HelloGeneratorClass implements Opcodes {

    /**
     * 生成保存 class 文件
     */
    public static void main(String[] args) {
        try {
            byte[] data = generatorHelloClass();
            File file = new File("ASM-Generated/Hello.class");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用构造 Hello 类 class 字节码，package 中的 Hello.java 只是代码原型，
     * 本例中只供对比用，没有实际使用到这个类。ASM 代码生成工具使用 bytecode。
     */
    private static byte[] generatorHelloClass() {

        ClassWriter cw = new ClassWriter(0);

        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "me/Hello", null, "java/lang/Object", null);

        cw.visitSource("Hello.java", null);

        {
            fv = cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "FLAG", "Ljava/lang/String;", null, "\u6211\u662f\u5e38\u91cf");
            fv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();//Label表示标签，比如 a: ，用于break，goto语法
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "display", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            Label l2 = new Label();
            mv.visitJumpInsn(GOTO, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{Opcodes.INTEGER}, 0, null);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(">>>>>>>>>>\u6211\u662f\u5e38\u91cf");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitIincInsn(1, 1);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitIntInsn(BIPUSH, 8);
            mv.visitJumpInsn(IF_ICMPLT, l3);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitInsn(RETURN);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "testList", "()Ljava/util/List;", "()Ljava/util/List<Ljava/lang/String;>;", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, "java/util/ArrayList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 1);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("Tome");
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("Jack");
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitLdcInsn("Lily");
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitLdcInsn(">>>>>>>>>>testList > list.size = ");
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ARETURN);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "testMapList", "(Z[Ljava/util/Map;)Ljava/util/List;", "(Z[Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;)Ljava/util/List<Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;>;", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, "java/util/ArrayList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 3);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ILOAD, 1);
            Label l2 = new Label();
            mv.visitJumpInsn(IFEQ, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ASTORE, 7);
            mv.visitInsn(ARRAYLENGTH);
            mv.visitVarInsn(ISTORE, 6);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 5);
            Label l4 = new Label();
            mv.visitJumpInsn(GOTO, l4);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitFrame(Opcodes.F_FULL, 8, new Object[]{"com/shanhy/demo/asm/hello/Hello", Opcodes.INTEGER, "[Ljava/util/Map;", "java/util/List", Opcodes.TOP, Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/util/Map;"}, 0, new Object[]{});
            mv.visitVarInsn(ALOAD, 7);
            mv.visitVarInsn(ILOAD, 5);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ASTORE, 4);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitIincInsn(5, 1);
            mv.visitLabel(l4);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ILOAD, 5);
            mv.visitVarInsn(ILOAD, 6);
            mv.visitJumpInsn(IF_ICMPLT, l5);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_FULL, 4, new Object[]{"com/shanhy/demo/asm/hello/Hello", Opcodes.INTEGER, "[Ljava/util/Map;", "java/util/List"}, 0, new Object[]{});
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitLdcInsn(">>>>>>>>>>testMapList > list.size = ");
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I", true);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitInsn(ARETURN);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitMaxs(4, 8);
            mv.visitEnd();
        }

        cw.visitEnd();

        return cw.toByteArray();
    }


}