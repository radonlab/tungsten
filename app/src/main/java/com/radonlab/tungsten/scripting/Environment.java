package com.radonlab.tungsten.scripting;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.widget.Toast;

import com.whl.quickjs.wrapper.JSObject;
import com.whl.quickjs.wrapper.QuickJSContext;

public class Environment {

    private final Context context;

    private final AccessibilityService service;

    public Environment(Context context, AccessibilityService service) {
        this.context = context;
        this.service = service;
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
        Long duration = (Long) args[2];
        Path path = new Path();
        path.lineTo(x, y);
        GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(path, 0, duration);
        GestureDescription gesture = new GestureDescription.Builder()
                .addStroke(stroke)
                .build();
        service.dispatchGesture(gesture, null, null);
        return null;
    }
}