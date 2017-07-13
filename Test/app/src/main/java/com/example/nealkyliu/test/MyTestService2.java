package com.example.nealkyliu.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MyTestService2 extends Service {
    private static final String TAG = MyTestService2.class.getSimpleName();
    private MyMsgHandler mHandler;
    private static final int UPDATE_MSG = 1;

    public MyTestService2() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new MyMsgHandler(this);


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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * 用以捕获后台线程发送来的更新指令
     **/
    private static class MyMsgHandler extends Handler {
        /* 建立弱引用 */
        private WeakReference<Context> mOuter;

        /* 构造函数 */
        public MyMsgHandler(Context context) {
            mOuter = new WeakReference<>(context);
        }

        /** 处理函数*/
        public void handleMessage(Message msg) {
            // 防止内存泄露
            MyTestService2 outer = (MyTestService2) mOuter.get();
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
//                if (FullScreenUtil.isFullScreen(this)) {
//                    Log.w(TAG, "current is full screen");
//                } else {
//                    Log.i(TAG, "current is not full screen");
//                }

                break;
            default:
                break;
        }
    }
}
