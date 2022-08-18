package me.ztiany.asm;

import me.ztiany.asm.core.clazz.RemoveMethodAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class ClassVisitorFactory {

    public static ClassVisitor createVisitor(String adapterName, List<String> adapterArguments, ClassWriter classWriter) {
        System.out.println("adapterName = " + adapterName + ", adapterArguments = " + adapterArguments + ", classWriter = " + classWriter);

        Class<? extends ClassVisitor> aClass = AdapterMap.ADAPTERS_MAP.get(adapterName);
        if (aClass == null) {
            return null;
        }
        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor.toString() + " " + constructor.getParameterCount());
            if (constructor.getParameterCount() == 2) {
                try {
                    return (ClassVisitor) constructor.newInstance(classWriter, adapterArguments);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
