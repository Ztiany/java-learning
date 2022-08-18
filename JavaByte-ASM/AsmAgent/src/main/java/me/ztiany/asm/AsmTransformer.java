package me.ztiany.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class AsmTransformer {

    private final AgentParams mAgentParams;

    public AsmTransformer(AgentParams agentParams) {
        mAgentParams = agentParams;
        System.out.println("agentParams = " + agentParams);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (contains(className)) {

            System.out.println("transformer: " + className);

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = ClassVisitorFactory.createVisitor(mAgentParams.getAdapterName(), mAgentParams.getAdapterArguments(), classWriter);
            if (classVisitor == null) {
                System.out.println("returning Adapter: null");
                return null;
            } else {
                System.out.println("returning Adapter: " + classVisitor);
            }

            ClassReader classReader = new ClassReader(classfileBuffer);
            classReader.accept(classVisitor, 0);

            /*最终我们返回的是classWriter的字节码*/
            return classWriter.toByteArray();
        }
        return null;
    }

    private boolean contains(String className) {
        boolean contains = false;
        for (String applyingPackage : mAgentParams.getApplyingPackage()) {
            if (className.startsWith(applyingPackage)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

}
