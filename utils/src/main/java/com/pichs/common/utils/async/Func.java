package com.pichs.common.utils.async;

/**
 * Created by he.b.wang on 16/8/10.
 */

public interface Func<T, R> {
    R call(T t);
}
