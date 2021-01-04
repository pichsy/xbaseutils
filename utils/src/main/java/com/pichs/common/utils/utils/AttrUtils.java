package com.pichs.common.utils.utils;

import android.util.AttributeSet;

public final class AttrUtils {

    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

    public String getAndroidAttr(AttributeSet attrs, String attrName) {
        return attrs.getAttributeValue(ANDROID_NAMESPACE, attrName);
    }


}
