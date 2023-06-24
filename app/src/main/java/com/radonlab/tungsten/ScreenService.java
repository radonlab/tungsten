package com.radonlab.tungsten;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.ScriptRepo;
import com.radonlab.tungsten.dto.ScriptDTO;
import com.radonlab.tungsten.scripting.Script;
import com.radonlab.tungsten.scripting.ScriptRunner;
import com.radonlab.tungsten.util.DndState;
import com.whl.quickjs.wrapper.QuickJSException;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ScreenService extends AccessibilityService {
    private static final int LAYOUT_TYPE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

    private static boolean isRunning = false;

    private ScriptRunner scriptRunner;

    private DndState dndState;

    private View touchBall;

    private WindowManager windowManager;

    private WindowManager.LayoutParams layoutParams;

    private Size screenSize;

    private Size touchBallSize;

    private ScriptDTO script;

    private CompositeDisposable disposables;

    public static boolean running() {
        return isRunning;
    }

    @SuppressLint({"RtlHardcoded", "InflateParams"})
    @Override
    public void onCreate() {
        super.onCreate();
        scriptRunner = new ScriptRunner(getApplicationContext(), this);
        dndState = new DndState();
        LayoutInflater inflater = LayoutInflater.from(this);
        touchBall = inflater.inflate(R.layout.touch_ball, null, false);
        windowManager = getSystemService(WindowManager.class);
        // Init dimensions
        initDimensions();
        // Init layout
        layoutParams = new WindowManager.LayoutParams(
                touchBallSize.getWidth(),
                touchBallSize.getHeight(),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.alpha = 0.5f;
        // Init position
        layoutParams.x = screenSize.getWidth() - touchBallSize.getWidth();
        layoutParams.y = (int) (screenSize.getHeight() * 0.8f) - touchBallSize.getHeight();
        windowManager.addView(touchBall, layoutParams);
        initEventListener();
        disposables = new CompositeDisposable();
        Log.d("ScreenService", "created");
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(touchBall);
        disposables.dispose();
        Log.d("ScreenService", "destroyed");
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int scriptId = intent.getIntExtra(AppConstant.SCRIPT_ID, AppConstant.UNDEFINED_SCRIPT_ID);
        Disposable disposable = ScriptRepo.getInstance(this)
                .findById(scriptId)
                .subscribe(result -> {
                    script = result.map(ScriptDTO::fromDO)
                            .orElse(null);
                    Log.d("ScreenService", "loaded script: " + script);
                }, e -> {
                    Log.e("ScreenService", "fatal", e);
                });
        disposables.add(disposable);
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    }

    @Override
    public void onInterrupt() {
    }

    private void initDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenSize = new Size(metrics.widthPixels, metrics.heightPixels);
        int touchBallWidth = getResources().getDimensionPixelSize(R.dimen.touch_ball_width);
        int touchBallHeight = getResources().getDimensionPixelSize(R.dimen.touch_ball_height);
        touchBallSize = new Size(touchBallWidth, touchBallHeight);
    }

    private void initEventListener() {
        touchBall.setOnClickListener(this::onTrigger);
        touchBall.setOnTouchListener(this::onTouch);
    }

    private void onTrigger(View view) {
        try {
            Log.d("ScreenService", "triggered");
            Script script = new Script("foo.js", "app.toast('hello');");
            scriptRunner.execute(script);
        } catch (QuickJSException e) {
            Log.w("ScreenService", e);
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