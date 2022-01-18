package me.ztiany.asm.core.clazz.transform;

import org.objectweb.asm.ClassVisitor;

/*
 * 一般转换过程的调用链都是一个 ClassReader，一个 ClassWriter 和一个 ClassVisitor 组成的，事实上可以使用更为复杂的转换链，将几个类适配器链接在一起。将几个适配器链接在一起，
 * 就可以组成几个独立的类转换，以完成复杂转换。还要注意，转换链不一定是线性的。可以编写一个 ClassVisitor，将接收到的所有方法调用同时转发给几个 ClassVisitor。
 */
import static org.objectweb.asm.Opcodes.ASM5;

public class MultiClassAdapter extends ClassVisitor {

    protected ClassVisitor[] cvs;

    public MultiClassAdapter(ClassVisitor[] cvs) {
        super(ASM5);
        this.cvs = cvs;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        for (ClassVisitor cv : cvs) {
            cv.visit(version, access, name, signature, superName, interfaces);
        }
    }

    //...

}