package com.pichs.common.utils.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class ForeBackgroundHelper implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "ForeBackgroundHelper";

    private static ForeBackgroundHelper singleton;

    public static void init(Application application) {
        init(application, null);
    }

    public static void init(Application application, Filter filter) {
        if (singleton == null) {
            singleton = new ForeBackgroundHelper(filter);
            application.registerActivityLifecycleCallbacks(singleton);
        } else {
            Log.w(TAG, TAG + " has been initialized.");
        }
    }

    private static void checkInit() {
        if (singleton == null) {
            throw new RuntimeException(TAG + " has not been initialized.");
        }
    }

    public static void registerListener(Listener listener) {
        checkInit();
        synchronized (singleton.listenerList) {
            singleton.listenerList.add(listener);
        }
    }

    public static void unregisterListener(Listener listener) {
        checkInit();
        synchronized (singleton.listenerList) {
            singleton.listenerList.remove(listener);
        }
    }

    public static boolean isApplicationInTheForeground() {
        checkInit();
        return singleton.foregroundCount > 0;
    }

    public static boolean isApplicationInTheBackground() {
        return !isApplicationInTheForeground();
    }

    private final List<Listener> listenerList = new ArrayList<>();
    private final Filter filter;

    private int foregroundCount = 0;
    private int bufferCount = 0;

    private ForeBackgroundHelper(Filter filter) {
        this.filter = filter;
    }

    private Listener[] collectListeners() {
        synchronized (listenerList) {
            if (listenerList.size() > 0) {
                Listener[] listeners = new Listener[listenerList.size()];
                listenerList.toArray(listeners);
                return listeners;
            } else {
                return null;
            }
        }
    }

    private void dispatchApplicationEnterForeground(Activity activity) {
        Listener[] listeners = collectListeners();
        if (listeners != null) {
            for (Listener listener : listeners) {
                if (listener != null) {
                    listener.onApplicationEnterForeground(activity);
                }
            }
        }
    }

    private void dispatchApplicationEnterBackground(Activity activity) {
        Listener[] listeners = collectListeners();
        if (listeners != null) {
            for (Listener listener : listeners) {
                if (listener != null) {
                    listener.onApplicationEnterBackground(activity);
                }
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (filter == null || !filter.isIgnore(activity)) {
            if (foregroundCount <= 0) {
                dispatchApplicationEnterForeground(activity);
            }
            if (bufferCount < 0) {
                bufferCount++;
            } else {
                foregroundCount++;
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (filter == null || !filter.isIgnore(activity)) {
            if (activity.isChangingConfigurations()) {
                bufferCount--;
            } else {
                foregroundCount--;
                if (foregroundCount <= 0) {
                    dispatchApplicationEnterBackground(activity);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    public interface Listener {

        void onApplicationEnterForeground(Activity activity);

        void onApplicationEnterBackground(Activity activity);

    }

    public interface Filter {

        boolean isIgnore(Activity activity);

    }

}