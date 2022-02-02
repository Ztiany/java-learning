package me.ztiany.asm.core.method;


import me.ztiany.asm.core.BaseClassAdapter;
import org.objectweb.asm.ClassVisitor;
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

    public void m() throws Exception {
        long time = System.currentTimeMillis();
        Thread.sleep(100);
        time = System.currentTimeMillis() - time;
        System.out.println("m(): " + time);
    }

}
*/

/**
 * 为方法添加耗时统计并打印。
 */
public class AddTimerClassAdapterV2 extends BaseClassAdapter {

    private String mOwner;
    private boolean mIsInterface;

    public AddTimerClassAdapterV2(ClassVisitor classVisitor, List<String> adapterParams) {
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
            visitor = new AddTimerMethodAdapter(api, name, visitor);
        }
        return visitor;
    }

    class AddTimerMethodAdapter extends MethodVisitor {

        private final String mMethodName;

        public AddTimerMethodAdapter(int api, String name, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
            mMethodName = name;
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            //插入方法开头的代码，初始化时间
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LSTORE, 1);
        }

        @Override
        public void visitInsn(int opcode) {
            //1. IRETURN 到 RETURN 以及之间所有类型的 RETURN 指令
            //2. 异常抛出指令
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                mv.visitVarInsn(LLOAD, 1);
                mv.visitInsn(LSUB);
                mv.visitVarInsn(LSTORE, 1);
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                mv.visitLdcInsn(mOwner + "." + mMethodName + "(): ");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitVarInsn(LLOAD, 1);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            mv.visitMaxs(maxStack + 4, maxLocals);
        }
    }

}
