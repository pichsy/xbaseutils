package com.pichs.common.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewUtils {

    /**
     * 已经绘制的图片转成bitmap
     *
     * @param view {@link View}
     * @return bitmap  {@link Bitmap}
     */
    public static Bitmap viewConvertToBitmap(View view) {
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 未绘制的图，转成bitmap
     *
     * @param view         目标view
     * @param widthOffset  宽度增加或减小的px数值, 负值为减小, 0不增不减
     * @param heightOffset 高度增加或减小的px数值, 负值为减小, 0不增不减
     * @return bitmap  {@link Bitmap}
     */
    public static Bitmap viewConvertToBitmap(View view, int width, int height, int widthOffset, int heightOffset) {
        view.setDrawingCacheEnabled(false);
        view.measure(View.MeasureSpec.makeMeasureSpec(width + widthOffset, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height + heightOffset, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }


    /**
     * 获取屏幕截屏的bitmap
     *
     * @param activity {@link Activity}
     * @return bitmap {@link Bitmap}
     */
    public static Bitmap getScreenShot(Activity activity) {
        if (activity == null) {
            return null;
        }
        Window window = activity.getWindow();
        if (window == null) return null;
        View view = window.getDecorView();
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 保存bitmap为图片
     *
     * @param context   {@link Context}
     * @param imageView {@link Bitmap}
     * @param fileName  文件名字
     * @return 文件保存路径
     * 默认路径:
     * @see AppPathUtils#getCachePath
     */
    public static String saveImage(Context context, ImageView imageView, String fileName) {
        if (imageView == null) {
            return null;
        }
        Bitmap bitmap = viewConvertToBitmap(imageView);
        if (null == bitmap) {
            return null;
        }
        File folder = AppPathUtils.getCachePath(context);
        return saveImage(context, bitmap, fileName, folder);
    }

    /**
     * 保存bitmap为图片
     *
     * @param context   {@link Context}
     * @param imageView {@link Bitmap}
     * @param fileName  文件名字
     * @param destDir   保存的文件夹路径
     * @return 文件保存路径
     */
    public static String saveImage(Context context, ImageView imageView, String fileName, File destDir) {
        if (imageView == null) {
            return null;
        }
        Bitmap bitmap = viewConvertToBitmap(imageView);
        if (null == bitmap) {
            return null;
        }
        if (destDir == null) {
            return null;
        }
        File file = saveImage2File(context, bitmap, fileName, destDir);
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * 保存bitmap为图片
     *
     * @param context  {@link Context}
     * @param bitmap   {@link Bitmap}
     * @param fileName 文件名字
     * @return 文件保存路径
     * 默认路径:
     * @see AppPathUtils#getCachePath
     */
    public static String saveImage(Context context, Bitmap bitmap, String fileName) {
        if (null == bitmap) {
            return null;
        }
        File folder = AppPathUtils.getCachePath(context);
        return saveImage(context, bitmap, fileName, folder);
    }

    /**
     * 保存bitmap为图片
     *
     * @param context  {@link Context}
     * @param bitmap   {@link Bitmap}
     * @param fileName 文件名字
     * @param destDir  保存的文件夹路径
     * @return 文件保存路径
     */
    public static String saveImage(Context context, Bitmap bitmap, String fileName, File destDir) {
        if (null == bitmap) {
            return null;
        }
        if (destDir == null) {
            return null;
        }
        File file = saveImage2File(context, bitmap, fileName, destDir);
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * @param context  {@link Context}
     * @param bitmap   {@link Bitmap}
     * @param fileName 文件名字
     * @return 文件保存路径
     * 默认路径:
     * @see AppPathUtils#getCachePath
     */
    public static File saveImage2File(Context context, Bitmap bitmap, String fileName) {
        if (null == bitmap) {
            return null;
        }
        File folder = AppPathUtils.getCachePath(context);
        return saveImage2File(context, bitmap, fileName, folder);
    }

    /**
     * 保存bitmap为图片
     *
     * @param context  {@link Context}
     * @param bitmap   {@link Bitmap}
     * @param fileName 文件名字
     * @param destDir  保存的文件夹路径
     * @return 文件保存路径
     */
    public static File saveImage2File(Context context, Bitmap bitmap, String fileName, File destDir) {
        if (null == bitmap) {
            return null;
        }
        if (destDir == null) {
            return null;
        }
        if (!destDir.exists()) {
            boolean mkdirs = destDir.mkdirs();
            if (mkdirs) {
            } else {
                return null;
            }
        }
        File destFile = new File(destDir, fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
            //压缩保存到本地
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return destFile;
    }

    /**
     * 保存bitmap为图片
     *
     * @param context   {@link Context}
     * @param imageView {@link ImageView}
     * @param fileName  文件名字
     * @return 文件保存路径
     * 默认路径:
     * @see AppPathUtils#getCachePath
     */
    public static File saveImage2File(Context context, ImageView imageView, String fileName) {
        if (imageView == null) {
            return null;
        }
        Bitmap bitmap = viewConvertToBitmap(imageView);
        if (null == bitmap) {
            return null;
        }
        File folder = AppPathUtils.getCachePath(context);
        return saveImage2File(context, bitmap, fileName, folder);
    }

    /**
     * 保存bitmap为图片
     *
     * @param context   {@link Context}
     * @param imageView {@link ImageView}
     * @param fileName  文件名字
     * @param destDir   保存的文件夹路径
     * @return 文件保存路径
     */
    public static File saveImage2File(Context context, ImageView imageView, String fileName, File destDir) {
        if (imageView == null) {
            return null;
        }
        Bitmap bitmap = viewConvertToBitmap(imageView);
        if (null == bitmap) {
            return null;
        }
        if (destDir == null) {
            return null;
        }
        return saveImage2File(context, bitmap, fileName, destDir);
    }


}
