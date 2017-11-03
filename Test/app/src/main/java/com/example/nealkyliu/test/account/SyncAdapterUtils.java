package com.example.nealkyliu.test.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.nealkyliu.test.utils.LogUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by nealkyliu on 2017/9/19.
 */

public class SyncAdapterUtils {
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.ss.android.application.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.ss.android.application";
    // The account name
    public static final String ACCOUNT = "dummyaccount1";

    private static final String PREF_SETUP_COMPLETE = "pref_setup_complete";


    public static void triggerRefresh(Account account) {
        if (null == account) {
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.requestSync(account, AUTHORITY, bundle);

            LogUtil.d("SyncAdapterUtils triggerRefresh");
        } catch (Throwable e) {
            LogUtil.e("triggerRefresh Error " + e);
        }
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static void createSyncAccount(Context context) {
        if (null == context) {
            return;
        }

        try {
            boolean isNewAccount = false;
            Account account = new Account(ACCOUNT, ACCOUNT_TYPE);

            boolean setupComplete = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);
            AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
            if (accountManager.addAccountExplicitly(account, null, null)) {
                ContentResolver.setIsSyncable(account, AUTHORITY, 1);
                ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
                ContentResolver.addPeriodicSync(account, AUTHORITY, new Bundle(), TimeUnit.SECONDS.toSeconds(1));
                isNewAccount = true;
            } else {
                LogUtil.w("createSyncAccount Error");
            }

            // Schedule an initial sync if we detect problems with either our account or our local
            // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
            // the account list, so wee need to check both.)
            if (isNewAccount || !setupComplete) {
                triggerRefresh(account);
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_SETUP_COMPLETE, true).commit();
            }
        } catch (Exception e) {
            LogUtil.e("createSyncAccount Error" + e);
        }

    }
}
