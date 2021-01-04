package com.pichs.common.utils.rxjava;

/**
 * @Description:
 * @Author: 吴波
 * @CreateDate: 2020/12/31 13:14
 * @UpdateUser: 吴波
 * @UpdateDate: 2020/12/31 13:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface RxResult<R> {
    void onResult(R r);
}

