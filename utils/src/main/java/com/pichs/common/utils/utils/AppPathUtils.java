package com.pichs.common.utils.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class AppPathUtils {

    private static final String BASE_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "AppXData" + File.separator;

    public static File getDCIMPath(Context context) {
        File file = new File(BASE_DIR + "DCIM");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getPicturePath(Context context) {
        File file = new File(BASE_DIR + "Picture");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getVideoPath(Context context) {
        File file = new File(BASE_DIR + "Video");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getFilePath(Context context) {
        File file = new File(BASE_DIR + "File");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;

    }

    public static File getDownloadPath(Context context) {
        File file = new File(BASE_DIR + "Download");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getAudioPath(Context context) {
        File file = new File(BASE_DIR + "Audio");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getMusicPath(Context context) {
        File file = new File(BASE_DIR + "Music");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCachePath(Context context) {
        File file = new File(BASE_DIR + "Cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

}
