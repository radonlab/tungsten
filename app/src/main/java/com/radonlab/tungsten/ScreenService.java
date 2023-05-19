package com.radonlab.tungsten;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;

public class ScreenService extends Service {
    private static final int LAYOUT_TYPE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

    private WindowManager windowManager;

    private Button floatButton;

    public ScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatButton = new Button(this);
        floatButton.setText("Click Me!");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.CENTER;
        windowManager.addView(floatButton, layoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(floatButton);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}