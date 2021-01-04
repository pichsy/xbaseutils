package com.pichs.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

@SuppressLint("ApplySharedPref")
public abstract class BaseSPHelper {

    // 防止内存泄漏，弱引用
    protected WeakReference<Context> mContextWeakReference;
    protected SharedPreferences mSP;
    private final String _spName;

    protected BaseSPHelper(Context context) {
        this.mContextWeakReference = new WeakReference<>(context.getApplicationContext());
        this._spName = getSpName();
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
    }

    protected abstract String getSpName();

    public String getString(String key) {
        if (mContextWeakReference.get() == null) {
            return null;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        if (mContextWeakReference.get() == null) {
            return defaultValue;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getString(key, defaultValue);
    }


    public void setString(String key, String value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setStringAsync(String key, String value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        if (mContextWeakReference.get() == null) {
            return false;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (mContextWeakReference.get() == null) {
            return defaultValue;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        if (mContextWeakReference.get() == null) {
            return -1;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        if (mContextWeakReference.get() == null) {
            return defaultValue;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getInt(key, defaultValue);
    }

    public void setInt(String key, int value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        if (mContextWeakReference.get() == null) {
            return -1L;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getLong(key, -1L);
    }

    public long getLong(String key, long defaultValue) {
        if (mContextWeakReference.get() == null) {
            return defaultValue;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getLong(key, defaultValue);
    }

    public void setLong(String key, long value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public float getFloat(String key) {
        if (mContextWeakReference.get() == null) {
            return -1f;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getFloat(key, -1f);
    }

    public float getFloat(String key, float defaultValue) {
        if (mContextWeakReference.get() == null) {
            return defaultValue;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getFloat(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public Set<String> getStringSet(String key) {
        if (mContextWeakReference.get() == null) {
            return null;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        if (mContextWeakReference.get() == null) {
            return defaultValue;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getStringSet(key, defaultValue);
    }

    public void setStringSet(String key, Set<String> value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public void setStringSetAsync(String key, Set<String> value) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public Map<String, ?> getAll() {
        if (mContextWeakReference.get() == null) {
            return null;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        return mSP.getAll();
    }

    public void remove(String key) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        if (mContextWeakReference.get() == null) {
            return;
        }
        if (mSP == null) {
            mSP = mContextWeakReference.get().getSharedPreferences(_spName, 0);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.clear();
        editor.commit();
    }

}
