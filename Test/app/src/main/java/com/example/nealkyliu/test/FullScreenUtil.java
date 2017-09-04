package com.example.nealkyliu.test;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by nealkyliu on 2017/8/24.
 */

public class FullScreenUtil {
    public static final int MESSAGE_STRATEGY_SAVE = 2;
    public static final int MESSAGE_STRATEGY_SHOW = 1;


    public interface IFullScreenCallback {
        void onFullScreenResult(boolean isFull);
    }

    public static final String TAG = FullScreenUtil.class.getSimpleName();
    public static final String TEST_TAG = "MY_TEST";

    public static Observable<Integer> isFullScreen(final Context mContext) {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                final Context context = mContext.getApplicationContext();
                final View view = new View(context);
                WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN; // 添加FULL_SCREEN属性
                wmParams.gravity = Gravity.TOP | Gravity.LEFT;
                wmParams.width = 0;
                wmParams.height = 0;

                final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                try {
                    windowManager.addView(view, wmParams);

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            // 获取可视区域大小
                            Rect rect = new Rect();
                            view.getWindowVisibleDisplayFrame(rect);

                            Point point = getScreenDimenson(context, windowManager);
                            boolean isFullScreenFlag = (rect.width() >= point.x) && (rect.height() >= point.y);

                            Log.d(TEST_TAG, "Screen Size =(" + point.x + ", " + point.y + ")");
                            Log.d(TEST_TAG, "Current Rect= (" + rect.width() + "," + rect.height() + ")");
                            Log.d(TEST_TAG, "IsFullScreen" + isFullScreenFlag);

                            int result = isFullScreenFlag ? MESSAGE_STRATEGY_SAVE : MESSAGE_STRATEGY_SHOW;
                            Log.i(TEST_TAG, "IsFullScreen Result == " + result);
                            subscriber.onNext(result);
                            subscriber.onCompleted();

                            try {
                                windowManager.removeView(view);
                            } catch (Exception e) {
                                Log.e(TEST_TAG, e.toString());
                            }

                        }
                    });
                } catch (Exception e) {
                    Log.e(TEST_TAG, "get Error == " +  e.toString());
                    subscriber.onNext(MESSAGE_STRATEGY_SHOW);
                    subscriber.onCompleted();
                } finally {

                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private static Point getScreenDimenson(Context context, WindowManager windowManager) {
        Point screenPoint = new Point();
        if (null == windowManager) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        windowManager.getDefaultDisplay().getSize(screenPoint);
        return screenPoint;
    }
}
