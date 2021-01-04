package com.pichs.common.utils.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.util.List;

public class AppUtils {

    private static final String TAG = "AppUtils";

    /**
     * 开启界面
     */
    public static void startActivity(Context context, Class<?> clazz) {
        startActivity(context, null, clazz);
    }

    /**
     * 开启界面
     *
     * @param intent 意图
     */
    public static void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            //调用方没有设置context或app间组件跳转，context为application
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 开启界面
     *
     * @param bundle 参数
     * @param clazz  目标类名
     */
    public static void startActivity(Context context, Bundle bundle, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            //调用方没有设置context或app间组件跳转，context为application
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 开启界面
     *
     * @param bundle 参数
     * @param clazz  目标类名
     */
    public static void startActivityForResult(Context context, int requestCode, Bundle bundle, Class<?> clazz) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("context is not instanceof Activity, please use 'cc.setContext(Activity *)' where you use CC ");
        }
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 开启界面 返回result
     */
    public static void startActivityForResult(Context context, int requestCode, Class<?> clazz) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("context is not instanceof Activity, please use 'cc.setContext(Activity *)' where you use CC");
        }
        Intent intent = new Intent(context, clazz);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转到权限设置界面
     */
    public static void toAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (9 <= Build.VERSION.SDK_INT) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }


//    /**
//     *  分享本App
//     * @param activity Activity
//     */
//    public static void shareApp(Activity activity){
//        String appFilePath = OsUtils.getAppFilePath(activity);
//        if (appFilePath != null) {
//            File file = new File(appFilePath);
//            FileShare.with(activity)
//                    .setOnActivityResult(100)
//                    .setContentType(ContentType.TEXT)
//                    .addShareFileUri(FileUriUtils.getUriFromFile(activity, null, file))
//                    .setTitle("share")
//                    .setTextContent("apk")
//                    .build()
//                    .share();
//        }
//    }

    /**
     * App是否运行在前台
     * @param context
     * @return
     */
    public static boolean isAppRunningForeground(Context context) {
        boolean isForeground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isForeground = true;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isForeground = true;
            }
        }
        return isForeground;
    }



    public static String getMetaDataString(Context context, String metaDataName, String... defaultValue) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            value = (defaultValue != null && defaultValue.length > 0) ? defaultValue[0] : value;
            value = appInfo.metaData.getString(metaDataName, value);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getMetaDataInt(Context context, String metaDataName, int... defaultValue) {
        int value = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            value = (defaultValue != null && defaultValue.length > 0) ? defaultValue[0] : value;
            value = appInfo.metaData.getInt(metaDataName, value);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean getMetaDataBoolean(Context context, String metaDataName, boolean... defaultValue) {
        boolean value = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            value = (defaultValue != null && defaultValue.length > 0) ? defaultValue[0] : value;
            value = appInfo.metaData.getBoolean(metaDataName, value);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static Object getMetaData(Context context, String metaDataName) throws Exception {
        ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                context.getPackageName(), PackageManager.GET_META_DATA);
        return appInfo.metaData.get(metaDataName);
    }

    public static float getMetaDataFloat(Context context, String metaDataName, float... defaultValue) {
        float value = 0.0f;
        try {
            value = (float) getMetaData(context, metaDataName);
        } catch (Exception e) {
            value = (defaultValue != null && defaultValue.length > 0) ? defaultValue[0] : value;
        }
        return value;
    }

    @SafeVarargs
    public static <T> T getMetaData(Context context, String metaDataName, T... defaultValue) {
        T value = null;
        try {
            value = (T) getMetaData(context, metaDataName);
        } catch (Exception e) {
            value = (defaultValue != null && defaultValue.length > 0) ? defaultValue[0] : value;
        }
        return value;
    }

}
