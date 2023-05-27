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
        QuickJSContext qjs = QuickJSContext.create();
        Environment environment = new Environment(context);
        environment.setup(qjs);
        byte[] code = qjs.compile(script.getContent(), script.getFilename());
        qjs.execute(code);
        qjs.destroy();
    }
}