package com.example.nealkyliu.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;
/*
* 判断当前手机屏幕是够全屏
**/
public class FullScreenListenerService extends Service {
    private static final String TAG = FullScreenListenerService.class.getSimpleName();
    private View mView;
    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;

    private MyMsgHandler mHandler;
    private static final int UPDATE_MSG = 1;

    public FullScreenListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new MyMsgHandler(this);

        initView();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (null != mHandler) {
                        mHandler.sendEmptyMessage(UPDATE_MSG);
                    }
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void initView() {
        mView = new TextView(this);
        mView.setBackgroundColor(Color.parseColor("#ff0dd1"));

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWmParams = new WindowManager.LayoutParams();
        mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
        mWmParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWmParams.width = 1;
        mWmParams.height = 1;

        try {
            mWindowManager.addView(mView, mWmParams);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
    * Log出View当前在Screen的位置
    **/
    private void logLocation(View mView) {
        int location[] = new int[2];
        if (null != mView) {
            mView.getLocationOnScreen(location);
            Log.d(TAG, "LocationOnScreen = (" + location[0] + ", " + location[1] + ")");

            mView.getLocationInWindow(location);
            Log.d(TAG, "getLocationInWindow = (" + location[0] + ", " + location[1] + ")");
        }
    }

    boolean isOrientationChanged = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        isOrientationChanged = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
    * 判断是否全屏
    * @param view
    **/
    private boolean isFullscreen(View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        return location[0] == 0 && location[1] == 0;
    }

    /*
     * 用以捕获后台线程发送来的更新指令
     **/
    private static class MyMsgHandler extends Handler{
        /* 建立弱引用 */
        private WeakReference<FullScreenListenerService> mOuter;

        /* 构造函数 */
        public MyMsgHandler(FullScreenListenerService context) {
            mOuter = new WeakReference<>(context);
        }

        /** 处理函数*/
        public void handleMessage(Message msg) {
            // 防止内存泄露
            FullScreenListenerService outer = mOuter.get();
            if (outer != null) {
                outer.handleMessage(msg);
            }
        }
    }

    /*
     * 消息处理函数
     **/
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE_MSG:
                if (isOrientationChanged) {
                    mWindowManager.updateViewLayout(mView, mWmParams);
                    isOrientationChanged = false;
                }
                logLocation(mView);
                if (isFullscreen(mView)) {
                    Log.w(TAG, "current is full screen");
                } else {
                    Log.i(TAG, "current is not full screen");
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ((null != mWindowManager) && (null != mView)) {
            mWindowManager.removeView(mView);
        }
    }


}