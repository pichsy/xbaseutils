package com.pichs.common.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;

public class ApkUtils {

    private static final String PACKAGE_NAME_WECHAT = "com.tencent.mm";
    private static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";


    public static boolean isQQInstalled(Context context) {
        return checkAppInstalled(context, PACKAGE_NAME_QQ);
    }

    public static boolean isWechatInstalled(Context context) {
        return checkAppInstalled(context, PACKAGE_NAME_WECHAT);
    }

    public static boolean checkAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void installApk(Activity activity, String targetFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(targetFilePath);
        //判断是否Android N 或更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

    /**
     * 打开微信扫一扫
     */
    public static void openWechatScan(Activity activity) {
        try {
            Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            if (intent != null) {
                intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "打开失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 通知系统相册刷新
     *
     * @param filePath 图片路径
     */
    public static void notifyGalleryRefresh(Context context, String filePath) {
        try {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, UriUtils.getUri(context, UriUtils.UriType.IMAGE, new File(filePath))));
        } catch (Exception e) {
            if (context == null || filePath == null) {
                return;
            }
            MediaScannerConnection.scanFile(context,
                    new String[]{filePath},
                    new String[]{"image/jpeg"},
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(final String s, Uri uri) {
                        }
                    });
        }
    }


    /**
     * 启动app
     *
     * @param pkgName 包名
     */
    public static void startAPP(Context context, String pkgName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 跳转应用市场
    public static final String MARKET_GOOGLE_PLAY = "com.android.vending";
    public static final String MARKET_HUAWEI = "com.huawei.appmarket";
    public static final String MARKET_XIAOMI = "com.xiaomi.market";
    public static final String MARKET_MEIZU = "com.meizu.mstore";
    public static final String MARKET_WANDOUJIA = "com.wandoujia.phoenix2";
    public static final String MARKET_360 = "com.qihoo.appstore";
    public static final String MARKET_YYB = "com.tencent.android.qqdownloader";
    public static final String MARKET_BAIDU = "com.baidu.appsearch";

    public static boolean toMarket(Context context, String appPkg, @Nullable String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (marketPkg != null && marketPkg.trim().length() > 0) {
                // 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
                intent.setPackage(marketPkg.trim());
            }
            context.startActivity(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static final String APP_TAOBAO = "com.taobao.taobao";

    /**
     * 打开淘宝
     */
    public static void startTaoBaoApp(Activity activity) {
        if (checkAppInstalled(activity, APP_TAOBAO)) {
            startAPP(activity, APP_TAOBAO);
        } else {
            Toast.makeText(activity, "未安装淘宝客户端", Toast.LENGTH_SHORT).show();
        }
    }

}
