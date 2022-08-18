package me.ztiany.asm.core;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class BaseClassAdapter extends org.objectweb.asm.ClassVisitor {

    protected final List<String> mAdapterParams;

    public BaseClassAdapter(ClassVisitor classVisitor, List<String> adapterParams) {
        super(Opcodes.ASM5, classVisitor);
        mAdapterParams = adapterParams;
    }

}
