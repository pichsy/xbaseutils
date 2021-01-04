package com.pichs.common.utils.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.DrawableRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * assets工具
 */

public class AssetsUtil {

    /**
     * 从raw问件中读取文件
     *
     * @param context
     * @param rawResId
     * @return
     */
    public static String getFromRaw(Context context, int rawResId) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(rawResId));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Assets中读取文本
     */
    public static String getStringFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssets(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void playMusicFromAssets(Context context, String fileName) {
        try {
            AssetFileDescriptor openFd = context.getAssets().openFd(fileName);
            MediaPlayer mediaPlayer = new MediaPlayer();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();    //如果正在播放，则重置为初始状态
            }
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());//设置资源目录
            mediaPlayer.prepare();//缓冲
            mediaPlayer.start();//开始或恢复播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以获取drawable资源的uri
     *
     * @param context Context
     * @param id      DrawableRes
     * @return String
     */
    public static String getResourcesUri(Context context, @DrawableRes int id) {
        Resources resources = context.getResources();
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
    }

    /**
     * 可以获取drawable资源的uri
     *
     * @param context Context
     * @param id      DrawableRes
     * @return Uri
     */
    public static Uri getResourcesUri(@DrawableRes int id, Context context) {
        return Uri.parse(getResourcesUri(context, id));
    }

}
