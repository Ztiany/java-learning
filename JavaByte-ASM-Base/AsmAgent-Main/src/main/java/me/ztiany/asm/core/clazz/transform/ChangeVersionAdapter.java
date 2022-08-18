package me.ztiany.asm.core.clazz.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

/**
 * 修改类的版本
 */
public class ChangeVersionAdapter extends ClassVisitor {

    public static void main(String[] args) throws IOException {
        ClassReader classReader = new ClassReader("me/ztiany/asm/core/method/DemoClass");
        ClassWriter classWriter = new ClassWriter(classReader,0);
        ChangeVersionAdapter changeVersionAdapter = new ChangeVersionAdapter(classWriter);
        classReader.accept(changeVersionAdapter, 0);
        byte[] newClass = classWriter.toByteArray();
    }

    ChangeVersionAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(Opcodes.V1_5, access, name, signature, superName, interfaces);
    }

}