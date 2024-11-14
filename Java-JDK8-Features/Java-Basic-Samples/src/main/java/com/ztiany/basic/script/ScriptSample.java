package com.ztiany.basic.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class ScriptSample {

    public static void main(String... args) throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        List<ScriptEngineFactory> engineFactories = scriptEngineManager.getEngineFactories();
        System.out.println(engineFactories);

        ScriptEngine groovy = scriptEngineManager.getEngineByName("groovy");
        System.out.println(groovy);

        ScriptEngine javaScript = scriptEngineManager.getEngineByName("JavaScript");
        System.out.println(javaScript);
    }

}
