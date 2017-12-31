package com.zenglb.framework.http;

import com.zlb.httplib.core.HttpRetrofit;

/**
 * Api service
 *
 * Created by zlb on 2017/12/26.
 */
public class HttpCall {

    public static ApiService apiService;

    /**
     * 获取Service
     * @return
     */
    public static ApiService getApiService() {
        if(apiService==null){
            apiService=HttpRetrofit.getRetrofit().create(ApiService.class);
        }

        return apiService;
    }

}
