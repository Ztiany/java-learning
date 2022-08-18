package me.ztiany.asm.core.clazz;

import me.ztiany.asm.core.BaseClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.List;

/**
 * 移除任意类的 void setAge(int) 方法。
 */
public class RemoveMethodAdapter extends BaseClassAdapter {

    private static final String NAME = "setAge";
    private static final String DESC = "(I)V";

    public RemoveMethodAdapter(ClassVisitor classVisitor, List<String> adapterParams) {
        super(classVisitor, adapterParams);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("access = [" + access + "], name = [" + name + "], desc = [" + desc + "], signature = [" + signature + "], exceptions = [" + Arrays.toString(exceptions) + "]");
        //不转发到下一个链，就是移除该方法。因为类的生成最终是由 ClassWriter 处理的，我们不把这个方法的信息传递给它，自然它就不会生成这个方法。
        if (name.equals(NAME) && desc.equals(DESC)) {
            // do not delegate to next visitor -> this removes the method
            return null;
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }

}