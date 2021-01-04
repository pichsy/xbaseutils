package com.pichs.common.utils.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

/**
 * @Description: 时间格式化工具类
 * @Author: WuBo
 * @CreateDate: 2020/11/10$ 14:32$
 * @UpdateUser: WuBo
 * @UpdateDate: 2020/11/10$ 14:32$
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

    // 常用的，不合适请自定义
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String hh_mm_ss = "hh:mm:ss";
    public static final String HH_mm = "hh:mm";
    public static final String hh_mm = "hh:mm";
    public static final String mm_ss = "mm:ss";

    /**
     * 时间格式化
     * 可自定义
     *
     * @param timeStamp 时间戳（毫秒 mills）System.currentMills()
     * @param format    格式 {@link #yyyy_MM_dd_HH_mm_ss}
     * @return String time format-ed
     */
    public static String getFormatDate(long timeStamp, String format) {
        return new SimpleDateFormat(format).format(timeStamp);
    }

    /**
     * 年月日  2020-05-30
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String formatYear(long timStamp) {
        return new SimpleDateFormat(yyyy_MM_dd).format(timStamp);
    }

    /**
     * 24小时制的 年月日时分秒  2020-05-30 23:39:01
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String formatYear24HourSecond(long timStamp) {
        return new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss).format(timStamp);
    }

    /**
     * 12小时制的 年月日时分秒  2020-05-30 11:39:01
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String formatYear12HourSecond(long timStamp) {
        return new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss).format(timStamp);
    }

    /**
     * 24小时制的 时分秒  23:39:01
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String format24HourSecond(long timStamp) {
        return new SimpleDateFormat(HH_mm_ss).format(timStamp);
    }

    /**
     * 12小时制的 时分秒  11:39:01
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String format12HourSecond(long timStamp) {
        return new SimpleDateFormat(hh_mm_ss).format(timStamp);
    }

    /**
     * 24小时制的 分秒  39:01
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String format_mm_ss(long timStamp) {
        return new SimpleDateFormat(mm_ss).format(timStamp);
    }

    /**
     * 12小时制的 时分  11:39
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String format_hh_mm(long timStamp) {
        return new SimpleDateFormat(hh_mm).format(timStamp);
    }

    /**
     * 24小时制的 时分  23:39
     *
     * @param timStamp long
     * @return time format-ed
     */
    public static String format_HH_mm(long timStamp) {
        return new SimpleDateFormat(HH_mm).format(timStamp);
    }


}
