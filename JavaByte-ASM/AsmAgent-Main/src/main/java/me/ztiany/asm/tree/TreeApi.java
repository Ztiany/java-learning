package me.ztiany.asm.tree;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import java.io.IOException;

public class TreeApi {

    public static void main(String[] args) throws IOException {
        ClassReader classReader = new ClassReader(ClassWithLambda.class.getName());
        ClassNode classNode = new ClassNode(Opcodes.ASM5);
        classReader.accept(classNode, 0);

        classNode.methods.forEach(methodNode -> {
            System.out.println(methodNode.name + methodNode.desc);
            methodNode.instructions.forEach(abstractInsnNode -> {
                System.out.println("    " + abstractInsnNode.getClass().getName());
                if (abstractInsnNode instanceof InvokeDynamicInsnNode) {
                    InvokeDynamicInsnNode invokeDynamicInsnNode = (InvokeDynamicInsnNode) abstractInsnNode;
                    System.out.println("        " + invokeDynamicInsnNode.name);
                    System.out.println("        " + invokeDynamicInsnNode.desc);
                    System.out.println("        " + invokeDynamicInsnNode.bsm);
                    for (Object bsmArg : invokeDynamicInsnNode.bsmArgs) {
                        System.out.println("               " + bsmArg.getClass());
                        if (bsmArg instanceof Handle) {
                            Handle handle = (Handle) bsmArg;
                            System.out.println("                   " + handle.getOwner());
                            System.out.println("                   " + handle.getName());
                            System.out.println("                   " + handle.getDesc());
                        } else {
                            System.out.println("                   " + bsmArg);
                        }
                    }
                }
            });
        });
    }

}
