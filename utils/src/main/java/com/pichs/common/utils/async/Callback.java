package com.pichs.common.utils.async;

/**
 * Created by he.b.wang on 16/8/10.
 */

public interface Callback<T> {
    void onResult(T result);
    void onError(Exception e);
}
