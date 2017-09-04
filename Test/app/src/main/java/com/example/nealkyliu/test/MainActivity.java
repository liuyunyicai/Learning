package com.example.nealkyliu.test;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nealkyliu.test.utils.LogUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextView;
    private TextView mTextView2;
    private boolean isFullScreen = false;

    private LinearLayout mRelatedNews;
    private ImageView mImgView;

    private float mScale = 0.5f;
    int screen_width;
    int screen_height;
    private View view;
    private Window window;

    private RecyclerView mRecyclerView;
    private ViewAdapter  mViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screen_width = UIUtils.getScreenWidth(this);
        screen_height = UIUtils.getScreenHeight(this);

        mRecyclerView = $(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> datas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            datas.add("1");
        }

        mViewAdapter = new ViewAdapter(this, datas);
        mRecyclerView.setAdapter(mViewAdapter);

//        test();
        test1();
    }

    private void test1() {
//        startService(new Intent(this, MyTestService2.class));
    }

    long count = 0;
    Observable<Long> observable;
    Subscription mSubscription;
    private void test() {
//        FullScreenUtil.isFullScreen(this, new View(this));
//        startService(new Intent(this, MyTestService2.class));
//        isProcessInfoHidden();

        Observable<Integer> observable1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> observable2 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                try {
//                    mRecyclerView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }, 3000);
                    String str = null;
                    str.length();
                    subscriber.onNext(2);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

        Observable<Integer> observable3 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());



        Observable<Integer> observable = Observable.concat(observable1, observable2, observable3);
//        Observable<Integer> observable = Observable.just(null);

        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                LogUtil.i("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("onError == " + e);
            }

            @Override
            public void onNext(Integer integer) {
                LogUtil.i("on Next == " + integer + "; Thread == " + Thread.currentThread());
            }
        });





    }

    @Override
    protected void onDestroy() {
        if (null != observable) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    static abstract class A implements Comparable<A>{

        @Override
        public int compareTo(@NonNull A o) {
            return 0;
        }
    }

    static class B extends A {}
    static class C extends A {}
    static class D extends A {}


    public static boolean isProcessInfoHidden() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/"));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] columns = line.split("\\s+");
                if (columns.length == 6 && columns[1].equals("/proc")) {
                    return columns[3].contains("hidepid=1") || columns[3].contains("hidepid=2");
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "Error reading /proc/mounts. Checking if UID 'readproc' exists.");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return android.os.Process.getUidForName("readproc") == 3009;
    }

    private void readSys() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("//s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "/t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte

            localBufferedReader.close();
        } catch (IOException e) {

        }

        Log.i("MY_TEST", "size == " + Formatter.formatFileSize(getBaseContext(), initial_memory));
        return;// Byte转换为KB或者MB，内存大小
    }


    private <T> T $(int resId) {
        return (T) findViewById(resId);
    }



    private void changeImageView() {
        mScale = 0.5 == mScale ? 0.8f : 0.5f;
        Log.i(TAG, "current scale == " + mScale);
        int cur_height = (int)(screen_height * mScale);
        int cur_width = screen_width;
        mImgView.setMaxWidth(cur_width);
        mImgView.setMaxHeight(cur_height);

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, (int)(cur_height * 0.5));
        mImgView.setImageMatrix(matrix);
        mImgView.setLayoutParams(new RelativeLayout.LayoutParams(cur_width, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void setFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isFullScreen = true;
    }

    private void setNotFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isFullScreen = false;
    }

    private void setLandScapeAndFullScreen() {

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isFullScreen = true;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        startService(new Intent(this, FullScreenListenerService.class));
//        logLocation();
    }




}
