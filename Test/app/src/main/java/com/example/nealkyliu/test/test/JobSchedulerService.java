package com.example.nealkyliu.test.test;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import com.example.nealkyliu.test.utils.LogUtil;

/**
 * Created by nealkyliu on 2017/9/4.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.d("onStartJob, tag-->" + params.getJobId());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtil.i("onStopJob, tag-->" + params.getJobId());
        return false;
    }
}
