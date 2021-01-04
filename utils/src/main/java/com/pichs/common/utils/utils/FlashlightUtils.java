package com.pichs.common.utils.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * @author zoneyet
 */
public class FlashlightUtils {

    // 手电筒 控制类
    @SuppressLint("StaticFieldLeak")
    private static FlashlightUtils instance = new FlashlightUtils();

    private FlashlightUtils() {
    }

    public static FlashlightUtils getInstance() {
        return instance;
    }


    private Camera camera = null;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOpen = false;
    private Context mContext;
    private OnFlashlightChanged mOnFlashlightChanged;

    /**
     * 监听只对 api 23 也就是6.0及以上手机有效
     *
     * @param onFlashlightChanged {@link OnFlashlightChanged}
     * @return FlashlightUtils
     */
    public FlashlightUtils registerFlashlightCallback(@NonNull OnFlashlightChanged onFlashlightChanged) {
        this.mOnFlashlightChanged = onFlashlightChanged;
        return this;
    }

    public FlashlightUtils init(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                this.mContext = context;
                this.cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (cameraManager != null) {
                    cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                        @Override
                        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                            isFlashOpen = enabled;
                            if (mOnFlashlightChanged != null) {
                                mOnFlashlightChanged.onChanged(isFlashOpen);
                            }
                        }
                    }, new Handler(Looper.getMainLooper()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public FlashlightUtils toggleFlashlight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (cameraManager != null && !TextUtils.isEmpty(cameraId)) {
                    cameraManager.setTorchMode(cameraId, !isFlashOpen);
                    isFlashOpen = !isFlashOpen;
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            openFlashL(isFlashOpen);
        }
        return this;
    }

    private void openFlashL(boolean open) {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters parameters = camera.getParameters();
            if (open) {
                parameters.remove("flash-mode");
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                isFlashOpen = false;
                camera.release();
                camera = null;
                if (mOnFlashlightChanged != null) {
                    mOnFlashlightChanged.onChanged(false);
                }
            } else {
                parameters.remove("flash-mode");
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                isFlashOpen = true;
                if (mOnFlashlightChanged != null) {
                    mOnFlashlightChanged.onChanged(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }

    public FlashlightUtils closeFlashlight() {
        isFlashOpen = true;
        return toggleFlashlight();
    }

    public FlashlightUtils openFlashlight() {
        isFlashOpen = false;
        return toggleFlashlight();
    }

    public void release() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext != null) {
                closeFlashlight();
                mOnFlashlightChanged = null;
                mContext = null;
                cameraManager = null;
                cameraId = null;
            }
        } else {
            if (camera != null) {
                openFlashL(false);
            }
        }
    }

    public interface OnFlashlightChanged {
        void onChanged(boolean isOpen);
    }
}
