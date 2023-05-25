package com.radonlab.tungsten.scripting;

import android.content.Context;
import android.widget.Toast;

import com.whl.quickjs.wrapper.JSCallFunction;
import com.whl.quickjs.wrapper.JSObject;
import com.whl.quickjs.wrapper.QuickJSContext;

public class Environment {

    private final Context context;

    public Environment(Context context) {
        this.context = context;
    }

    public void setup(JSObject global, QuickJSContext qjs) {
        JSObject app = qjs.createNewJSObject();
        app.setProperty("toast", (JSCallFunction) (Object... args) -> {
            String msg = (String) args[0];
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            return null;
        });
        global.setProperty("app", app);
    }
}