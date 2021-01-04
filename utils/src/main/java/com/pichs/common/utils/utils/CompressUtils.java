package com.pichs.common.utils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 图片压缩
 */
public final class CompressUtils {

    public static Builder with(Context context) {
        return new Builder(context);
    }

    private static File compressImage(File imageFile, int reqWidth, int reqHeight, Bitmap.CompressFormat compressFormat, int quality, String destinationPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        File file = new File(destinationPath).getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(destinationPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(compressFormat, quality, fileOutputStream);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return new File(destinationPath);
    }

    private static Bitmap decodeSampledBitmapFromFile(File imageFile, int reqWidth, int reqHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        //check the rotation of the image and display it properly
        ExifInterface exif;
        exif = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90);
        } else if (orientation == 3) {
            matrix.postRotate(180);
        } else if (orientation == 8) {
            matrix.postRotate(270);
        }
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public final static class Builder {
        //max width and height values of the compressed image is taken as 612x816
        private int maxWidth = 1080;
        private int maxHeight = 1080;
        private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        private int quality = 80;
        private String destinationDirectoryPath;

        private Builder(Context context) {
            destinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
        }

        public Builder setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            this.compressFormat = compressFormat;
            return this;
        }

        public Builder setQuality(int quality) {
            this.quality = quality;
            return this;
        }

        public Builder setDestDir(String destinationDirectoryPath) {
            this.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        /**
         * 原名保存
         *
         * @param imageFile 文件路经
         * @return File
         * @throws IOException e
         */
        public File compressToFile(File imageFile) throws IOException {
            return compressToFile(imageFile, imageFile.getName());
        }

        /**
         * 指定名保存
         *
         * @param imageFile 文件路经
         * @return File
         * @throws IOException e
         */
        public File compressToFile(File imageFile, String compressedFileName) throws IOException {
            return compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                    destinationDirectoryPath + File.separator + compressedFileName);
        }

        /**
         * 压缩后转成bitmap
         *
         * @param imageFile 文件路经
         * @return Bitmap
         * @throws IOException e
         */
        public Bitmap compressToBitmap(File imageFile) throws IOException {
            return decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
        }

    }
}