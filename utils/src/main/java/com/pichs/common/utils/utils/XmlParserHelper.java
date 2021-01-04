package com.pichs.common.utils.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

/**
 * 动态加载编译后的xml文件，
 * 文件放在assets目录下面，
 * 只能放在assets目录下，可以应用在
 * 只能打jar包却需要资源的情况
 */
public class XmlParserHelper {

    private final static String assetsFile = "assets/";
    private Context mContext;

    public XmlParserHelper(Context context) {
        this.mContext = context;
    }

    public View getAssetsLayout(String filename) {
        AssetManager am = mContext.getResources().getAssets();
        try {
            XmlResourceParser parser = am.openXmlResourceParser(assetsFile + filename);
            return LayoutInflater.from(mContext).inflate(parser, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public <T extends View> T getViewByTag(View viewGroup, String tag) {
        return viewGroup.findViewWithTag(tag);
    }

}
