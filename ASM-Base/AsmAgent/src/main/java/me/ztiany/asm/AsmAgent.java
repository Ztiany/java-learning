package me.ztiany.asm;

import com.google.gson.Gson;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class AsmAgent implements ClassFileTransformer {

    private final AsmTransformer mAsmTransformer;

    private AsmAgent(String agentArgs) {
        AgentParams agentParams = new Gson().fromJson(agentArgs, AgentParams.class);
        mAsmTransformer = new AsmTransformer(agentParams);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.println("process: " + className);
        return mAsmTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.printf("I've been called with options: \"%s\"\n", agentArgs);
        inst.addTransformer(new AsmAgent(agentArgs));
    }

}
