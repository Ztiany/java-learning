package me.ztiany.asm.core.method.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.NOP;

/**
 * 删除方法中的 NOP 指令（因为它们不做任何事情，所以删除它们没有任何问题）
 */
public class RemoveNopAdapter extends MethodVisitor {

    public static void main(String[] args) throws IOException {
        ClassReader classReader = new ClassReader("me/ztiany/asm/core/method/parser/DemoClass");
        ClassWriter cv = new ClassWriter(0);
        RemoveNopClassAdapter removeNopClassAdapter = new RemoveNopClassAdapter(cv);
        classReader.accept(removeNopClassAdapter, 0);
    }

    public RemoveNopAdapter(MethodVisitor mv) {
        super(ASM4, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode != NOP) {
            mv.visitInsn(opcode);
        }
    }

    public static class RemoveNopClassAdapter extends ClassVisitor {

        public RemoveNopClassAdapter(ClassVisitor cv) {
            super(ASM4, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            //先得到 ClassWriter 对改方法的 MethodVisitor，再进行包装
            MethodVisitor mv;
            mv = cv.visitMethod(access, name, desc, signature, exceptions);
            if (mv != null) {
                mv = new RemoveNopAdapter(mv);
            }
            return mv;
        }
    }

}