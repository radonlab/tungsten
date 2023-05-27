package com.radonlab.tungsten;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.radonlab.tungsten.scripting.Script;
import com.radonlab.tungsten.scripting.ScriptRunner;
import com.radonlab.tungsten.ui.DndState;
import com.whl.quickjs.wrapper.QuickJSException;

public class ScreenService extends Service {
    private static final int LAYOUT_TYPE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

    private ScriptRunner scriptRunner;

    private DndState dndState;

    private View touchBall;

    private WindowManager windowManager;

    private WindowManager.LayoutParams layoutParams;

    public ScreenService() {
    }

    @SuppressLint({"RtlHardcoded", "InflateParams"})
    @Override
    public void onCreate() {
        super.onCreate();
        scriptRunner = new ScriptRunner(getApplicationContext());
        dndState = new DndState();
        LayoutInflater inflater = LayoutInflater.from(this);
        touchBall = inflater.inflate(R.layout.touch_ball, null, false);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.alpha = 0.5f;
        setInitPosition();
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

    private void setInitPosition() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int width = getResources().getDimensionPixelSize(R.dimen.touch_ball_width);
        int height = getResources().getDimensionPixelSize(R.dimen.touch_ball_height);
        layoutParams.x = metrics.widthPixels - width;
        layoutParams.y = (int) (metrics.heightPixels * 0.8f) - height;
    }

    private void initEventListener() {
        View btn = touchBall.findViewById(R.id.touch_btn);
        btn.setOnClickListener(this::onTrigger);
        btn.setOnTouchListener(this::onTouch);
    }

    private void onTrigger(View view) {
        try {
            Log.d("ScreenService", "triggered");
            Script script = new Script("foo.js", "app.toast('hello');");
            scriptRunner.execute(script);
        } catch (QuickJSException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean onTouch(View view, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dndState.startDrag(e.getRawX(), e.getRawY(), layoutParams.x, layoutParams.y);
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x = (int) dndState.getCurrentX(e.getRawX());
                layoutParams.y = (int) dndState.getCurrentY(e.getRawY());
                windowManager.updateViewLayout(touchBall, layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                dndState.endDrag();
                break;
        }
        return false;
    }
}