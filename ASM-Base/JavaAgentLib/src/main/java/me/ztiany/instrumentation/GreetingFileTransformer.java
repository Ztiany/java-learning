package me.ztiany.instrumentation;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class GreetingFileTransformer implements ClassFileTransformer {

    private final String agentArgs;

    public GreetingFileTransformer(String agentArgs) {
        System.out.println("GreetingFileTransformer.GreetingFileTransformer");
        this.agentArgs = agentArgs;
    }

    /*
        step1（JDK5）：注册
           该方法将由 JVM 调用，通过 Instrumentation 对象，我们可以注册一个类转换器，然后每当一个类被加载，都会调用 transform 方法。
    */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.printf("premain-I've been called with options: \"%s\"\n", agentArgs);
        inst.addTransformer(new GreetingFileTransformer(agentArgs));
    }

    /*
    step1（JDK6）：注册
       该方法将由 JVM 调用，通过 Instrumentation 对象，我们可以注册一个类转换器，然后每当一个类被加载，都会调用 transform 方法。
*/
    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.printf("agentmain-I've been called with options: \"%s\"\n", agentArgs);
        inst.addTransformer(new GreetingFileTransformer(agentArgs), true);

        Class classes[] = inst.getAllLoadedClasses();
        for (int i = 0; i < classes.length; i++) {
            System.out.println("try Reloading: " + classes[i].getName());
            if (classes[i].getName().contains("JDK6AgentTest")) {
                System.out.println("Reloading: " + classes[i].getName());
                inst.retransformClasses(classes[i]);
                break;
            }
        }
    }

    /*
        step2：转换字节码。
            操作字节码可以使用 ASM、Apache BCEL、Javassist、cglib、Byte Buddy 等库。
            下面示例代码，使用 BCEL 库实现将 me.ztiany.agent.test.jdk5.JDK5AgentTest.getGreeting() 方法输出的 hello world，替换为输出 premain 方法的传入的参数 agentArgs。
    */
    @Override
    public byte[] transform(ClassLoader loader,//要转换的类加载器；如果是引导加载器，则为 null
                            String className,//类的全路径名称，例如，"java/util/List"。
                            Class<?> classBeingRedefined,//如果是被重定义或重转换触发，则为重定义或重转换的类；如果是类加载，则为 null
                            ProtectionDomain protectionDomain,//类的保护域
                            byte[] classfileBuffer//类的字节码
    ) {
        System.out.println("load->" + className);

        if (className.equals("me/ztiany/agent/test/jdk5/JDK5AgentTest")) {
            try {
                JavaClass clazz = Repository.lookupClass(className);
                ClassGen cg = new ClassGen(clazz);
                ConstantPoolGen cp = cg.getConstantPool();
                for (Method method : clazz.getMethods()) {
                    if (method.getName().equals("getGreeting")) {
                        MethodGen mg = new MethodGen(method, cg.getClassName(), cp);
                        InstructionList il = new InstructionList();
                        il.append(new PUSH(cp, this.agentArgs));
                        il.append(InstructionFactory.createReturn(Type.STRING));
                        mg.setInstructionList(il);
                        mg.setMaxStack();
                        mg.setMaxLocals();
                        cg.replaceMethod(method, mg.getMethod());
                    }
                }
                return cg.getJavaClass().getBytes();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        if (className.equals("me/ztiany/agent/test/jdk6/JDK6AgentTest")) {
            try {
                JavaClass clazz = Repository.lookupClass(className);
                ClassGen cg = new ClassGen(clazz);
                ConstantPoolGen cp = cg.getConstantPool();
                for (Method method : clazz.getMethods()) {
                    if (method.getName().equals("foo")) {
                        MethodGen mg = new MethodGen(method, cg.getClassName(), cp);
                        InstructionList il = new InstructionList();
                        il.append(new PUSH(cp, 50));
                        il.append(InstructionFactory.createReturn(Type.INT));
                        mg.setInstructionList(il);
                        mg.setMaxStack();
                        mg.setMaxLocals();
                        cg.replaceMethod(method, mg.getMethod());
                    }
                }
                return cg.getJavaClass().getBytes();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classfileBuffer;
    }

}