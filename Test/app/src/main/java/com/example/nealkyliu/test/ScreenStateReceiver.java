package com.example.nealkyliu.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nealkyliu on 2017/9/7.
 */

public class ScreenStateReceiver extends BroadcastReceiver {
    private static final String TAG = ScreenStateReceiver.class.getSimpleName();
    private static final String TEST_TAG = "DELAY_TEST1";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Log.d(TEST_TAG, "ScreenStateReceiver ACTION_SCREEN_ON");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.d(TEST_TAG, "ScreenStateReceiver ACTION_SCREEN_OFF");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.d(TEST_TAG, "ScreenStateReceiver ACTION_USER_PRESENT");
            try {
//                onTriggeredByUserPresent(context, intent);
            } catch (Exception e) {
                Log.e(TAG, "onTriggeredByUserPresent Error " + e);
            }
        }
    }

//    private void onTriggeredByUserPresent(Context context, Intent intent) {
//        if (null == intent || null == context) {
//            return;
//        }
//        Intent newIntent = new Intent(MessageConstants.MESSAGE_ACTION);
//        newIntent.putExtra(MessageConstants.MESSAGE_ACTION_TYPE, MESSAGE_ACTION_RECEIVE_DELAY_SHOW);
//        newIntent.putExtra(MessageConstants.MESSAGE_PUSH_TYPE, "GCM");
//        newIntent.setPackage(BuildConfig.PKG_NAME);
//        context.startService(newIntent);
//    }
}
