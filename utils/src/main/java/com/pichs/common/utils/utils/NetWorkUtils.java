package com.pichs.common.utils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 判断手机是否联网，获取手机连接类型
 */

public class NetWorkUtils {
    // 手机网络类型
    public static final int WIFI = 0x01;
    public static final int CMWAP = 0x02;
    public static final int CMNET = 0x03;

    /**
     * 只关注是否联网
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return netType;
            }
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                String extraInfo = networkInfo.getExtraInfo();
                if (!TextUtils.isEmpty(extraInfo)) {
                    if (extraInfo.toLowerCase().equals("cmnet")) {
                        netType = CMNET;
                    } else {
                        netType = CMWAP;
                    }
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = WIFI;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netType;
    }

    public static boolean isWifiConnect(Context context) {
        return getNetworkType(context) == 1;
    }

    public static boolean isNetConnect(Context context) {
        return getNetworkType(context) != 0;
    }

}
