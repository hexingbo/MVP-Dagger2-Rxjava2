package com.zenglb.framework.http.core;

import com.zenglb.framework.entity.Messages;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.AreuSleepResult;
import com.zenglb.framework.http.result.LoginResult;
import com.zenglb.framework.http.result.Modules;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 所有的具体的和业务相关的Http请求
 *
 * Created by zenglb on 2017/3/17.
 */
public interface ApiService {
    /**
     * login/oauth2
     */
    @Headers("NoNeedAuthFlag: NoNeedAuthFlag")
    @POST("api/lebang/oauth/access_token")
    Call<HttpResponse<LoginResult>> goLogin(@Body LoginParams loginParams);

    /**
     * this request after login/oauth before logout
     * but no need oauth,so do not add auth header
     *
     * @param loginParams
     */
    @POST("api/lebang/oauth/access_token")
    @Headers("NoNeedAuthFlag: NoNeedAuthFlag")
    Call<HttpResponse<LoginResult>> refreshToken(@Body LoginParams loginParams);

    /**
     * get Message List();
     */
    @GET("api/lebang/messages")
    Call<HttpResponse<List<Messages>>> getMessages(@Query("max_id") long maxId, @Query("limit") int limit);

    /**
     * test get something
     */
    @GET("api/lebang/staffs/me/modules")
    Call<HttpResponse<Modules>> getModules();


    @GET("api/lebang/night_school/{type}")
    Call<HttpResponse<List<AreuSleepResult>>> getAreuSleep(@Path("type") String type, @Query("page") int page);

}

