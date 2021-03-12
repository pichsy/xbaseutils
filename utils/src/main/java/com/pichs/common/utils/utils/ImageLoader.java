package com.pichs.common.utils.utils;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * 图片加载
 */
public class ImageLoader {

    @IntDef({
            DiskCacheStrategy.NONE,
            DiskCacheStrategy.ALL,
            DiskCacheStrategy.RESULT,
            DiskCacheStrategy.SOURCE,
            DiskCacheStrategy.AUTO
    })
    public @interface DiskCacheStrategy {
        int NONE = -1;
        int ALL = 0;// 全部缓存
        int RESULT = 1;// 剪裁后的图片缓存
        int SOURCE = 2;// 原图缓存
        int AUTO = 3;// 自动
    }

    @DrawableRes
    private int placeHolder;
    @DrawableRes
    private int errorHolder;
    @DiskCacheStrategy
    private int diskCacheStrategy = DiskCacheStrategy.ALL;

    /**
     * @hide
     */
    private static final class _INSTANCE {
        private static final ImageLoader mHelper = new ImageLoader();
    }

    public static ImageLoader with() {
        return _INSTANCE.mHelper;
    }

    public ImageLoader setGlobalPlaceHolder(@DrawableRes int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public ImageLoader setGlobalErrorHolder(@DrawableRes int errorHolder) {
        this.errorHolder = errorHolder;
        return this;
    }

    public ImageLoader setGlobalDiskCacheStrategy(@DiskCacheStrategy int diskCacheStrategy) {
        this.diskCacheStrategy = diskCacheStrategy;
        return this;
    }

    public <T> Builder<T> load(T url) {
        return new Builder<>(url);
    }

    public static class Builder<T> {

        @DrawableRes
        private int placeHolder;
        @DrawableRes
        private int errorHolder;
        @DiskCacheStrategy
        private int diskCacheStrategy;
        private int cropType = 0;
        private int overrideWidth;
        private int overrideHeight;
        private int timeout = 0;
        private boolean dontAnimate = true;
        private boolean useAnimationPool = false;
        private T url;

        Builder(T url) {
            this.url = url;
        }

        public Builder<T> placeholder(@DrawableRes int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public Builder<T> error(@DrawableRes int errorHolder) {
            this.errorHolder = errorHolder;
            return this;
        }

        public Builder<T> diskCacheStrategy(@DrawableRes int diskCacheStrategy) {
            this.diskCacheStrategy = diskCacheStrategy;
            return this;
        }

        public Builder<T> override(int width, int height) {
            this.overrideWidth = width;
            this.overrideHeight = height;
            return this;
        }

        public Builder<T> centerCrop() {
            cropType = 1;
            return this;
        }

        public Builder<T> circleCrop() {
            cropType = 2;
            return this;
        }

        public Builder<T> fitCenter() {
            cropType = 3;
            return this;
        }

        public Builder<T> centerInside() {
            cropType = 4;
            return this;
        }

        public Builder<T> useAnimate() {
            this.dontAnimate = false;
            return this;
        }

        public Builder<T> useAnimationPool() {
            this.useAnimationPool = true;
            return this;
        }

        public Builder<T> timeout(@IntRange(from = 0) int timeout) {
            this.timeout = timeout;
            return this;
        }

        @SuppressLint("CheckResult")
        public void into(ImageView iv) {
            RequestOptions requestOptions = new RequestOptions();
            if (placeHolder != 0) {
                requestOptions.placeholder(placeHolder);
            }
            if (errorHolder != 0) {
                requestOptions.error(errorHolder);
            }
            if (overrideHeight != 0 && overrideWidth != 0) {
                requestOptions.override(overrideWidth, overrideHeight);
            }
            if (cropType == 1) {
                requestOptions.centerCrop();
            } else if (cropType == 2) {
                requestOptions.circleCrop();
            } else if (cropType == 3) {
                requestOptions.fitCenter();
            } else if (cropType == 4) {
                requestOptions.centerInside();
            }

            if (diskCacheStrategy == DiskCacheStrategy.NONE) {
                requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE);
            } else if (diskCacheStrategy == DiskCacheStrategy.RESULT) {
                requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE);
            } else if (diskCacheStrategy == DiskCacheStrategy.AUTO) {
                requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.AUTOMATIC);
            } else if (diskCacheStrategy == DiskCacheStrategy.SOURCE) {
                requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.DATA);
            } else {
                requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL);
            }

            if (timeout > 0) {
                requestOptions.timeout(timeout);
            }

            requestOptions.dontTransform();

            if (dontAnimate) {
                requestOptions.dontAnimate();
            }

            if (useAnimationPool) {
                requestOptions.useAnimationPool(true);
            }
            Glide.with(iv).load(url).apply(requestOptions).into(iv);
        }
    }
}
