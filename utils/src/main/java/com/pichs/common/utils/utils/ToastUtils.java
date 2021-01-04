package com.pichs.common.utils.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


public class ToastUtils {
    private static Handler mHandler;

    static {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static void toastLong(final Context context, final String s) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (s != null) {
                    Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private static void toastShort(final Context context, final String s) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (s != null) {
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void toast(Context context, String s) {
        toastShort(context, s);
    }


}
