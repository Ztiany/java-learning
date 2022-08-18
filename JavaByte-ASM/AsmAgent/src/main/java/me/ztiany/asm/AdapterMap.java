package me.ztiany.asm;

import me.ztiany.asm.core.clazz.RemoveMethodAdapter;
import me.ztiany.asm.core.method.AddTimerClassAdapterV1;
import me.ztiany.asm.core.method.AddTimerClassAdapterV2;
import org.objectweb.asm.ClassVisitor;

import java.util.HashMap;
import java.util.Map;

public class AdapterMap {

    public static final String REMOVE_METHOD = "REMOVE_METHOD";
    public static final String ADD_TIMER = "ADD_TIMER";

    static final Map<String, Class<? extends ClassVisitor>> ADAPTERS_MAP = new HashMap<>();

    static {
        ADAPTERS_MAP.put(REMOVE_METHOD, RemoveMethodAdapter.class);
        ADAPTERS_MAP.put(ADD_TIMER, AddTimerClassAdapterV2.class);
    }

}
