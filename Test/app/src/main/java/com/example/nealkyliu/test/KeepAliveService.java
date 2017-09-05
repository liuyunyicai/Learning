package com.example.nealkyliu.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.example.nealkyliu.test.utils.LogUtil;

import java.util.logging.Logger;

/**
 * Created by nealkyliu on 2017/9/4.
 */

public class KeepAliveService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.w("KeepAliveService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.w("KeepAliveService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
