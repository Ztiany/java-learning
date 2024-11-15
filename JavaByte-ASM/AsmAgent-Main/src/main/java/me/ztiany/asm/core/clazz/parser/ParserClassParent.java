package me.ztiany.asm.core.clazz.parser;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取两个类的共同超类。
 */
public class ParserClassParent {

    /**
     * 获取两个类的超类。
     */
    public static void main(String... args) {
        ExtendClassWriter extendClassWriter = new ExtendClassWriter(ParserClassParent.class.getClassLoader(), ExtendClassWriter.COMPUTE_FRAMES);
        String commonSuperClass = extendClassWriter.getCommonSuperClass(Type.getInternalName(SonA.class), Type.getInternalName(SonB.class));
        System.out.println("commonSuperClass = " + commonSuperClass);
    }

    private static class Parent {

    }

    private static class SonA extends Parent {

    }

    private static class SonB extends Parent {

    }

    private static class ExtendClassWriter extends ClassWriter {

        public static final String TAG = "ExtendClassWriter";
        private static final String OBJECT = "java/lang/Object";
        private final ClassLoader urlClassLoader;

        public ExtendClassWriter(ClassLoader urlClassLoader, int flags) {
            super(flags);
            this.urlClassLoader = urlClassLoader;
        }

        @Override
        protected String getCommonSuperClass(final String type1, final String type2) {
            System.out.println("type1 = " + type1 + ", type2 = " + type2);

            if (type1 == null || type1.equals(OBJECT) || type2 == null || type2.equals(OBJECT)) {
                return OBJECT;
            }

            if (type1.equals(type2)) {
                return type1;
            }

            ClassReader type1ClassReader = getClassReader(type1);
            ClassReader type2ClassReader = getClassReader(type2);
            System.out.println("type1ClassReader = " + type1ClassReader);
            System.out.println("type2ClassReader = " + type2ClassReader);

            if (type1ClassReader == null || type2ClassReader == null) {
                return OBJECT;
            }

            if (isInterface(type1ClassReader)) {
                String interfaceName = type1;
                if (isImplements(interfaceName, type2ClassReader)) {
                    return interfaceName;
                }
                if (isInterface(type2ClassReader)) {
                    interfaceName = type2;
                    if (isImplements(interfaceName, type1ClassReader)) {
                        return interfaceName;
                    }
                }
                return OBJECT;
            }

            if (isInterface(type2ClassReader)) {
                String interfaceName = type2;
                if (isImplements(interfaceName, type1ClassReader)) {
                    return interfaceName;
                }
                return OBJECT;
            }

            final Set<String> superClassNames = new HashSet<>();
            superClassNames.add(type1);
            superClassNames.add(type2);

            String type1SuperClassName = type1ClassReader.getSuperName();
            if (!superClassNames.add(type1SuperClassName)) {//加不进去说明已存在？
                return type1SuperClassName;
            }

            String type2SuperClassName = type2ClassReader.getSuperName();
            if (!superClassNames.add(type2SuperClassName)) {//加不进去说明已存在？
                return type2SuperClassName;
            }

            while (type1SuperClassName != null || type2SuperClassName != null) {
                if (type1SuperClassName != null) {
                    type1SuperClassName = getSuperClassName(type1SuperClassName);
                    if (type1SuperClassName != null) {
                        if (!superClassNames.add(type1SuperClassName)) {
                            return type1SuperClassName;
                        }
                    }
                }
                if (type2SuperClassName != null) {
                    type2SuperClassName = getSuperClassName(type2SuperClassName);
                    if (type2SuperClassName != null) {
                        if (!superClassNames.add(type2SuperClassName)) {
                            return type2SuperClassName;
                        }
                    }
                }
            }

            return OBJECT;
        }

        private boolean isImplements(final String interfaceName, final ClassReader classReader) {
            ClassReader classInfo = classReader;
            while (classInfo != null) {
                final String[] interfaceNames = classInfo.getInterfaces();
                for (String name : interfaceNames) {
                    if (name != null && name.equals(interfaceName)) {
                        return true;
                    }
                }
                for (String name : interfaceNames) {
                    if (name != null) {
                        final ClassReader interfaceInfo = getClassReader(name);
                        if (interfaceInfo != null) {
                            if (isImplements(interfaceName, interfaceInfo)) {
                                return true;
                            }
                        }
                    }
                }
                final String superClassName = classInfo.getSuperName();
                if (superClassName == null || superClassName.equals(OBJECT)) {
                    break;
                }
                classInfo = getClassReader(superClassName);
            }
            return false;
        }

        private boolean isInterface(final ClassReader classReader) {
            return (classReader.getAccess() & Opcodes.ACC_INTERFACE) != 0;
        }

        private String getSuperClassName(final String className) {
            final ClassReader classReader = getClassReader(className);
            if (classReader == null) {
                return null;
            }
            return classReader.getSuperName();
        }

        private ClassReader getClassReader(final String className) {
            InputStream inputStream = urlClassLoader.getResourceAsStream(className + ".class");
            try {
                if (inputStream != null) {
                    return new ClassReader(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            return null;
        }

    }

}