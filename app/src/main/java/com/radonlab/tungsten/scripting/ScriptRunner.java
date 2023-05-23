package com.radonlab.tungsten.scripting;

import com.whl.quickjs.android.QuickJSLoader;
import com.whl.quickjs.wrapper.QuickJSContext;
import com.whl.quickjs.wrapper.QuickJSException;

public class ScriptRunner {

    static {
        QuickJSLoader.init();
    }

    public void execute(Script script) throws QuickJSException {
        QuickJSContext context = QuickJSContext.create();
        byte[] code = context.compile(script.getContent(), script.getFilename());
        context.execute(code);
        context.destroy();
    }
}