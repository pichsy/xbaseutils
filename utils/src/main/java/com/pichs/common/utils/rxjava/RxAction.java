package com.pichs.common.utils.rxjava;

/**
 * @Description:
 * @Author: 吴波
 * @CreateDate: 2020/12/31 13:12
 * @UpdateUser: 吴波
 * @UpdateDate: 2020/12/31 13:12
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface RxAction<T, R> {
    R run(T t);
}
