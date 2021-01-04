package com.pichs.common.utils.utils;

import android.content.Context;

import com.pichs.common.utils.BaseSPHelper;


public class SPHelper extends BaseSPHelper {

    private final static String spName = "xp_base_sp_helper_info";
    private static SPHelper INSTANCE;

    protected SPHelper(Context context) {
        super(context);
    }

    public static SPHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SPHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SPHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    protected String getSpName() {
        return spName;
    }
}
