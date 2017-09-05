package com.example.nealkyliu.test.utils;

import android.util.Log;

/**
 * Created by nealkyliu on 2017/8/29.
 */

public class LogUtil {
    private static final String TAG = "MY_TEST";

    public static void d(String er) {
        Log.d(TAG, er);
    }

    public static void v(String er) {
        Log.v(TAG, er);
    }

    public static void i(String er) {
        Log.i(TAG, er);
    }

    public static void w(String er) {
        Log.w(TAG, er);
    }

    public static void e(String er) {
        Log.e(TAG, er);
    }

}
