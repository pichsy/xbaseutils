package com.pichs.common.utils.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/*******************************************************
 *
 *                            _ooOoo_
 *                           o8888888o
 *                           88" . "88
 *                           (| -_- |)
 *                           O\  =  /O
 *                        ____/`---'\____
 *                      .'  \\|     |//  `.
 *                     /  \\|||  :  |||//  \
 *                    /  _||||| -:- |||||-  \
 *                    |   | \\\  -  /// |   |
 *                    | \_|  ''\---/''  |   |
 *                    \  .-\__  `-`  ___/-. /
 *                  ___`. .'  /--.--\  `. . __
 *               ."" '<  `.___\_<|>_/___.'  >'"".
 *              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *              \  \ `-.   \_ __\ /__ _/   .-` /  /
 *         ======`-.____`-.___\_____/___.-`____.-'======
 *                            `=---='
 *         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 * 优雅的实现方式
 * 类似RxAndroid的写法实现
 * 优点: 不影响回调的类型 View.OnClickListener
 * @date 2020/6/17
 * @author pichs
 **********************************************************/
public final class ClickHelper {

    /**
     * 控制点击事件，防止多次点击
     *
     * @param view 需要控制点击频率的View
     * @return {@link ClickHolder}
     */
    public static ClickHolder clicks(View view) {
        return new ClickHolder(view);
    }

    /**
     * 点击View事件持有类
     */
    public static class ClickHolder {

        final View mView;
        private long interval = 1000L;
        private long lastClickTime = 0;

        /**
         * 点击事件持有类
         *
         * @param view 需要点击事件的View {@link View}
         */
        public ClickHolder(View view) {
            mView = view;
        }

        /**
         * 设置时间间隔
         *
         * @param time time 单位 ms
         * @return {@link ClickHolder}
         * then call this method: {@link #call(View.OnClickListener)}
         */
        public ClickHolder setInterval(long time) {
            this.interval = time;
            return this;
        }

        /**
         * 触发点击事件 并回调
         *
         * @param onClickListener {@link View.OnClickListener}
         */
        public void call(final View.OnClickListener onClickListener) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > interval) {
                        lastClickTime = currentTime;
                        if (onClickListener != null) {
                            onClickListener.onClick(v);
                        }
                    }
                }
            });
        }
    }

    /**
     * （双击）多击事件 处理
     *
     * @param view 点击的View
     * @return {@link MultiClickHolder}
     */
    public static MultiClickHolder multiClicks(View view) {
        return new MultiClickHolder(view);
    }

    /**
     * 多次店家事件的持有类
     */
    public static class MultiClickHolder {

        private int clickTimes = 0;
        private View mView;
        private long interval = 400L;
        // suodingzhi
        private long lockedTime = 1000L;
        private boolean isRunning = false;
        private OnMultiClickListener mListener;
        private ClickTimerManager mTimerManager;
        private boolean clock = false;

        @SuppressLint("HandlerLeak")
        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ClickTimerManager.TIMER_FINISHED) {
                    clock = true;
                    // 时间到 ,点击事件回调
                    if (clickTimes == 1) {
                        if (mListener != null) mListener.onClickOnce(mView);
                    } else if (clickTimes == 2) {
                        if (mListener != null) mListener.onClickTwice(mView);
                    } else if (clickTimes > 2) {
                        if (mListener != null) mListener.onClickMore(mView, clickTimes);
                    }
                    clickTimes = 0;
                    isRunning = false;
                    sendEmptyMessageDelayed(10, lockedTime);
                } else if (msg.what == 10) {
                    clock = false;
                }
            }
        };

        /**
         * 构造方法
         *
         * @param view {@link View}
         */
        public MultiClickHolder(View view) {
            mView = view;
            mTimerManager = new ClickTimerManager(mHandler, interval, interval);
        }

        /**
         * 设置点击事件的时间总长，也就是多少毫秒结束
         *
         * @param time interval
         * @return {@link MultiClickHolder}
         */
        public MultiClickHolder setInterval(long time) {
            this.interval = time;
            return this;
        }

        /**
         * 点击事件触发后锁定时间内不触发点击事件，锁定点击事件时间
         *
         * @param lockedTime lockedTime
         * @return {@link MultiClickHolder}
         */
        public MultiClickHolder setLockedTime(long lockedTime) {
            this.lockedTime = lockedTime;
            return this;
        }

        /**
         * 设置监听事件回调
         *
         * @param onMultiClickListener {@link OnMultiClickListener}
         */
        public void call(final OnMultiClickListener onMultiClickListener) {
            this.mListener = onMultiClickListener;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clock) {
                        return;
                    }
                    // 如果单击事件，直接回调，无需计算
                    if (isRunning) {
                        clickTimes++;
                    }
                    if (!isRunning) {
                        clickTimes = 1;
                        mTimerManager.startTimer();
                        isRunning = true;
                    }
                }
            });
        }
    }


    /**
     * 多次点击事件回调
     */
    public abstract static class OnMultiClickListener {
        /**
         * 单击回调
         *
         * @param view {@link View}
         */
        public void onClickOnce(View view) {
        }

        /**
         * 双击回调
         *
         * @param view {@link View}
         */
        public void onClickTwice(View view) {
        }

        /**
         * 多次点击回调
         *
         * @param view       {@link View}
         * @param clickTimes 点击的次数
         */
        public void onClickMore(View view, int clickTimes) {
        }
    }

    /**
     * 点击计时器，倒计时结束时回调点击事件，计算点击次数
     */
    public static class ClickTimerManager {
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

        /**
         * 参数说明：
         * 计数的总时长 = period * times 总共计（times）次数，每次之间的间隔时间为（period）(ms)毫秒
         * 计数开始的延迟时间为（delay）(ms)毫秒
         *
         * @param handler handler回调
         * @param delay   延迟毫秒 ms
         * @param period  计数频率 {eg:（1000ms）=> 1秒 记一次数,  （500ms）=>0.5s记一次数}
         */
        private ClickTimerManager(Handler handler, long delay, long period) {
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
        public ClickTimerManager(Handler handler, long delay, long period, int times) {
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
}
