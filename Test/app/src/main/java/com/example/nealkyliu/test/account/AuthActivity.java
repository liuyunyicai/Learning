package com.example.nealkyliu.test.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.nealkyliu.test.utils.LogUtil;

/**
 * Created by nealkyliu on 2017/9/19.
 */

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SyncAdapterUtils.createSyncAccount(this);
        LogUtil.d("AuthActivity onCreate");
    }

}
