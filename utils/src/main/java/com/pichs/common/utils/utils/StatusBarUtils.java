package com.pichs.common.utils.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

/**
 * 修改系统状态栏颜色的工具类
 */
public class StatusBarUtils {

    /**
     * 改变状态栏的颜色
     * 注意如果使用第三方框架的activity不生效，请继承 AppCompatActivity ,继承此类有效
     *
     * @param colorResId
     */
    public static void changeStatusBarColor(Activity context, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintResource(colorResId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.setStatusBarColor(context, colorResId);
        }
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    //获取导航栏高度
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 清除 （白色状态栏图标黑色）
     * 状态栏会变成 设置的colorPrimary颜色
     * 图标会变成白色
     *
     * @param activity Activity
     */
    public static void clearStatusLightMode(Activity activity) {
        clearStatusBarLightMode(activity, 1);
        clearStatusBarLightMode(activity, 2);
        clearStatusBarLightMode(activity, 3);
    }

    /**
     * 给窗体设置 白色状态栏，图标变成黑色
     *
     * @param activity Activity
     */
    public static void setStatusLightMode(Activity activity) {
        statusBarLightMode(activity, 1);
        statusBarLightMode(activity, 2);
        statusBarLightMode(activity, 3);
    }


    /**
     * 设置Activity 的 状态栏的颜色
     *
     * @param activity   Activity
     * @param colorResId colorResId
     */
    public static void setStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给Dialog 设置 对话框
     *
     * @param dialog     Dialog
     * @param colorResId Color colorResId
     */
    public static void setStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(dialog.getContext(), colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置 Activity 状态栏的颜色
     *
     * @param activity   Activity
     * @param colorResId colorResId
     */
    public static void setNavigationBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //底部导航栏
                window.setNavigationBarColor(ContextCompat.getColor(activity, colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置 Dialog 状态栏的颜色
     *
     * @param dialog     Dialog
     * @param colorResId colorResId
     */
    public static void setNavigationBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //底部导航栏
                window.setNavigationBarColor(ContextCompat.getColor(dialog.getContext(), colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {     //5.0系统
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //4.4系统
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void translucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {     //5.0系统
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //4.4系统
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void statusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    private static void clearStatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    public static void setHideVirtualKey(Window window) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            //保持布局状态
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    //布局位于状态栏下方
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN |

                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            } else {
                uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            window.getDecorView().setSystemUiVisibility(uiOptions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }


    /*刘海屏全屏显示FLAG*/
    public static final int FLAG_NOTCH_SUPPORT = 0x00010000;

    /**
     * 设置应用窗口在华为刘海屏手机使用刘海区
     *
     * @param window 应用页面window对象
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setNotchFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("clearHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
