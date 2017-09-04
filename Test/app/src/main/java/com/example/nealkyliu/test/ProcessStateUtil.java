package com.example.nealkyliu.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by nealkyliu on 2017/8/28.
 */

public class ProcessStateUtil {
    private static final String TEST_TAG = "TEST_TAG";

    public static String getForegroundProcess() {
        File[] files = new File("/proc").listFiles();
        String processName = "";
        Log.d(TEST_TAG, "Mypid == " + Process.myPid());

        for (File file : files) {
            if (file.isFile()) {
                continue;
            }
            int pid;
            try {
                pid = Integer.parseInt(file.getName());
            } catch (NumberFormatException e) {
                continue;
            }
            try {
                //读取进程名称
                String oomAdj = do_exec(String.format("cat /proc/%d/oom_adj", pid));
                Log.d(TEST_TAG, "PID == " + pid + "; oomAdj == " + oomAdj);
                if (oomAdj.trim().equals("0")) {
                    String cmdline = do_exec(String.format("cat /proc/%d/cmdline", pid));
                    //前台进程
                    Log.i(TEST_TAG, "The Foreground Name: " + cmdline);
                    return cmdline;
                } else {
                    continue;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    private static String do_exec(String cmd) {
        String result = "";
        try {
            java.lang.Process exec = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // 有多个桌面程序存在，且未指定默认项时；
            return null;
        } else {
            Log.w(TEST_TAG, "Launch packageName == " + res.activityInfo.packageName);
            return res.activityInfo.packageName;
        }
    }
}
