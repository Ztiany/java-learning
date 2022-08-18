package me.ztiany.asm.core.method;


import me.ztiany.asm.core.BaseClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/*
public class AddTimberDemoClass {

     public void m() throws Exception {
        Thread.sleep(100);
     }

}


转换为：


public class AddTimberDemoClass {

    public static long timer;

    public void m() throws Exception {
        timer -= System.currentTimeMillis();
        Thread.sleep(100);
        timer += System.currentTimeMillis();
    }

}
*/

/**
 * 为方法添加耗时统计。
 */
public class AddTimerClassAdapterV1 extends BaseClassAdapter {

    private String mOwner;
    private boolean mIsInterface;

    public AddTimerClassAdapterV1(ClassVisitor classVisitor, List<String> adapterParams) {
        super(classVisitor, adapterParams);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mOwner = name;
        mIsInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        /*
            1. 不是接口
            2. 父类返回了 MethodVisitor
            3. 不是构造方法
         */
        if (!mIsInterface && visitor != null && !name.equals("<init>")) {
            visitor = new AddTimerMethodAdapter(api, visitor);
        }
        return visitor;
    }

    @Override
    public void visitEnd() {
        if (!mIsInterface) {
            FieldVisitor fv = super.visitField(ACC_PUBLIC + ACC_STATIC, "timer", "J", null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }

    class AddTimerMethodAdapter extends MethodVisitor {

        public AddTimerMethodAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, mOwner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
            mv.visitInsn(LSUB);
            mv.visitFieldInsn(PUTSTATIC, mOwner, "timer", "J");
        }

        @Override
        public void visitInsn(int opcode) {
            //1. IRETURN 到 RETURN 以及之间所有类型的 RETURN 指令
            //2. 异常抛出指令
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitFieldInsn(GETSTATIC, mOwner, "timer", "J");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
                mv.visitInsn(LADD);
                mv.visitFieldInsn(PUTSTATIC, mOwner, "timer", "J");
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            mv.visitMaxs(maxStack + 4, maxLocals);
        }
    }

}
