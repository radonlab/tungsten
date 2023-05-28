package com.radonlab.tungsten.scripting;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;

import com.whl.quickjs.android.QuickJSLoader;
import com.whl.quickjs.wrapper.QuickJSContext;
import com.whl.quickjs.wrapper.QuickJSException;

public class ScriptRunner extends Environment {

    static {
        QuickJSLoader.init();
    }

    public ScriptRunner(Context context, AccessibilityService service) {
        super(context, service);
    }

    public void execute(Script script) throws QuickJSException {
        QuickJSContext qjs = QuickJSContext.create();
        setup(qjs);
        byte[] code = qjs.compile(script.getContent(), script.getFilename());
        qjs.execute(code);
        qjs.destroy();
    }
}