package com.radonlab.tungsten.scripting;

import android.content.Context;

import com.whl.quickjs.android.QuickJSLoader;
import com.whl.quickjs.wrapper.QuickJSContext;
import com.whl.quickjs.wrapper.QuickJSException;

public class ScriptRunner {

    static {
        QuickJSLoader.init();
    }

    private final Context context;

    public ScriptRunner(Context context) {
        this.context = context;
    }

    public void execute(Script script) throws QuickJSException {
        Environment environment = new Environment(context);
        QuickJSContext context = QuickJSContext.create();
        byte[] code = context.compile(script.getContent(), script.getFilename());
        environment.setup(context.getGlobalObject(), context);
        context.execute(code);
        context.destroy();
    }
}