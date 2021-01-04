package com.pichs.common.utils.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 点击计时器，倒计时结束时回调点击事件，计算点击次数
 */
public class TimerManager {
    private Handler mHandler;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private long mDelay;
    private long mPeriod;
    private int mTimes = 1;
    public static final int TIMER_PROGRESS = 0;
    public static final int TIMER_FINISHED = 1;
    public static final int TIMER_CANCELED = 2;
    private int mTimerId = 0;

    private TimerManager(Handler handler, long delay, long period) {
        this.mDelay = delay;
        this.mPeriod = period;
        this.mHandler = handler;
    }

    /**
     * 参数说明：
     * 计数的总时长 = period * times 总共计（times）次数，每次之间的间隔时间为（period）(ms)毫秒
     * 计数开始的延迟时间为（delay）(ms)毫秒
     *
     * @param handler handler回调
     * @param delay   延迟毫秒 ms
     * @param period  计数频率 {eg:（1000ms）=> 1秒 记一次数,  （500ms）=>0.5s记一次数}
     * @param times   计数的次数，总共会倒计时times次 次数（times）必须 >0 且为整数
     */
    public TimerManager(Handler handler, long delay, long period, int times) {
        this(handler, delay, period);
        if (times < 1) {
            times = 1;
        }
        this.mTimes = times;
    }

    /**
     * 开始倒计时
     */
    public void startTimer() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = TIMER_PROGRESS;
                    msg.arg1 = (int) (++mTimerId);
                    if (mHandler != null) {
                        mHandler.sendMessage(msg);
                    }
                    // Have times-limit, and already reached
                    if (mTimes != -1 && mTimerId == mTimes) {
                        timerFinished();
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, mDelay, mPeriod);
        }
    }

    /**
     * 取消倒计时并回调取消事件
     */
    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        mTimerId = 0;
        if (mHandler != null) {
            mHandler.sendEmptyMessage(TIMER_CANCELED);
        }
    }

    /**
     * 倒计时结束，并回调
     */
    private void timerFinished() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        mTimerId = 0;
        if (mHandler != null) {
            mHandler.sendEmptyMessage(TIMER_FINISHED);
        }
    }
}