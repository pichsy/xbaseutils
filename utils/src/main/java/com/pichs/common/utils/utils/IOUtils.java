package com.pichs.common.utils.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.internal.schedulers.RxThreadFactory;

public class IOUtils {

    private static final Handler mHandler;
    // 线程池
    private static ExecutorService mExecutorService;
    // 可用核心数
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final long KEEP_ALIVE_TIME = 10L;
    public static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // 队列
    public static BlockingQueue<Runnable> mTaskQueue = new LinkedBlockingQueue<>();

    static {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                (NUMBER_OF_CORES * 2),
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mTaskQueue
        );
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public static void runOnUiThread(long delay, final Runnable runnable) {
        if (mHandler != null) {
            mHandler.postDelayed(runnable, delay);
        }
    }

    public static void runOnIOThread(final Runnable runnable) {
        if (mExecutorService.isShutdown()) {
            mExecutorService = new ThreadPoolExecutor(
                    NUMBER_OF_CORES,
                    (NUMBER_OF_CORES * 2),
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    mTaskQueue
            );
        }
        try {
            // 优先使用线程池
            mExecutorService.execute(runnable);
        } catch (Exception e) {
            new Thread(runnable).start();
        }
    }

    public static void releaseExecutor() {
        mExecutorService.shutdown();
    }
}
