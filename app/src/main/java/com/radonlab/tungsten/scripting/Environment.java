package com.radonlab.tungsten.scripting;

import android.content.Context;
import android.widget.Toast;

import com.whl.quickjs.wrapper.JSObject;
import com.whl.quickjs.wrapper.QuickJSContext;

public class Environment {

    private final Context context;

    public Environment(Context context) {
        this.context = context;
    }

    public void setup(QuickJSContext qjs) {
        JSObject global = qjs.getGlobalObject();
        JSObject app = qjs.createNewJSObject();
        app.setProperty("toast", this::toast);
        app.setProperty("click", this::click);
        global.setProperty("app", app);
    }

    private Object toast(Object... args) {
        String msg = (String) args[0];
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        return null;
    }

    private Object click(Object... args) {
        Float x = (Float) args[0];
        Float y = (Float) args[1];
        return null;
    }
}