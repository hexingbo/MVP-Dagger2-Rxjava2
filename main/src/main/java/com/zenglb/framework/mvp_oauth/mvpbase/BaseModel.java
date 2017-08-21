package com.zenglb.framework.mvp_oauth.mvpbase;

import com.zenglb.framework.retrofit.core.ApiService;
import com.zenglb.framework.retrofit.core.HttpCall;

/**
 * 一般的Model 层会有通过APi 去请求网络
 */
public abstract class BaseModel implements IModel {

    protected static ApiService ApiService;
    // 初始化HttpService
    static {
        ApiService = HttpCall.getApiService();
    }

}
