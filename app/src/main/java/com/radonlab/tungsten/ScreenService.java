package com.radonlab.tungsten;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.radonlab.tungsten.scripting.Script;
import com.radonlab.tungsten.scripting.ScriptRunner;

public class ScreenService extends Service {
    private static final int LAYOUT_TYPE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

    private ScriptRunner scriptRunner;

    private View touchBall;

    private WindowManager windowManager;

    public ScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        scriptRunner = new ScriptRunner();
        LayoutInflater inflater = LayoutInflater.from(this);
        touchBall = inflater.inflate(R.layout.touch_ball, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.END | Gravity.BOTTOM;
        layoutParams.alpha = 0.5f;
        layoutParams.y = 300;
        windowManager.addView(touchBall, layoutParams);
        initEventListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(touchBall);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void initEventListener() {
        touchBall.findViewById(R.id.touch_btn).setOnClickListener(this::onTriggered);
    }

    private void onTriggered(View view) {
        Log.d("ScreenService", "triggered");
        Script script = new Script("foo.js", "gunn()");
        scriptRunner.execute(script);
    }
}