package com.pichs.common.utils.utils;

import android.content.Context;
import android.graphics.Color;

public class ColorUtils {

    public static int getColorPrimary(Context context) {
        ResourceLoader.getInstance(context)
                .getColor("colorPrimary");
        return Color.GRAY;
    }

    public static int getColorPrimaryDark(Context context) {
        ResourceLoader.getInstance(context)
                .getColor("colorPrimaryDark");
        return Color.GRAY;
    }

    public static int getColorAccent(Context context) {
        ResourceLoader.getInstance(context)
                .getColor("colorAccent");
        return Color.RED;
    }
}
