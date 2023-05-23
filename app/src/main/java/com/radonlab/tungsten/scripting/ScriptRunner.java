package com.radonlab.tungsten.scripting;

import com.whl.quickjs.android.QuickJSLoader;
import com.whl.quickjs.wrapper.QuickJSContext;

public class ScriptRunner {
    public static void init() {
        QuickJSLoader.init();
    }

    public static void run(Script script) {
        QuickJSContext context = QuickJSContext.create();
        context.evaluate(script.getContent(), script.getFilename());
        context.destroy();
    }
}