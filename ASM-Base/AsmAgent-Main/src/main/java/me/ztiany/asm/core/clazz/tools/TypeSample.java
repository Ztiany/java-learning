package me.ztiany.asm.core.clazz.tools;

import org.objectweb.asm.Type;

import java.util.Arrays;

public class TypeSample {

    public static void main(String[] args) throws NoSuchMethodException {
        //返回一个 Type 的内部名称
        System.out.println(Type.getType(String.class).getInternalName());
        //返回 Type 的描述符
        System.out.println(Type.getType(String.class).getDescriptor());

        //对对象执行操作
        System.out.println(Type.getDescriptor(String.class));
        System.out.println(Arrays.toString(Type.getArgumentTypes(String.class.getMethod("toCharArray"))));
        System.out.println(Type.getReturnType(String.class.getMethod("toCharArray")));
    }

}
