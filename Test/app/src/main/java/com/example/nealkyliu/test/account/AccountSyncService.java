package com.example.nealkyliu.test.account;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.nealkyliu.test.utils.LogUtil;

/**
 * Created by nealkyliu on 2017/9/19.
 */

public class AccountSyncService extends Service{
    private static final Object sSyncAdapterLock = new Object();
    private static volatile AccountSyncAdapter sAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();

        if (null == sAdapter) {
            synchronized (sSyncAdapterLock) {
                if (null == sAdapter) {
                    sAdapter = new AccountSyncAdapter(getApplicationContext(), true);
                }
            }
        }

        LogUtil.i("AccountSyncService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sAdapter.getSyncAdapterBinder();
    }

    static class AccountSyncAdapter extends AbstractThreadedSyncAdapter {

        public AccountSyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            LogUtil.w("AccountSyncAdapter onPerformSync");
            try {
                getContext().getContentResolver().notifyChange(TestAccountProvider.CONTENT_URI, null, false);
            } catch (Exception e) {

            }
        }
    }
}
