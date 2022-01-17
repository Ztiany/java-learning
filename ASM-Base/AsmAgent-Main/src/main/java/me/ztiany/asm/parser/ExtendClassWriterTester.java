package me.ztiany.asm.parser;


import org.objectweb.asm.Type;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2020/6/2 15:37
 */
public class ExtendClassWriterTester {

    /**
     * 获取两个类的超类。
     */
    public static void main(String... args) {
        ExtendClassWriter extendClassWriter = new ExtendClassWriter(ExtendClassWriterTester.class.getClassLoader(), ExtendClassWriter.COMPUTE_FRAMES);
        String commonSuperClass = extendClassWriter.getCommonSuperClass(Type.getInternalName(SonA.class), Type.getInternalName(SonB.class));
        System.out.println("commonSuperClass = " + commonSuperClass);
    }

    private static class Parent {

    }

    private static class SonA extends Parent {

    }

    private static class SonB extends Parent {

    }

}
