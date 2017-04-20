package com.zenglb.framework.http.core;

import com.zenglb.framework.entity.Messages;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.AreuSleepResult;
import com.zenglb.framework.http.result.LoginResult;
import com.zenglb.framework.http.result.ModulesResult;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 所有的具体的和业务相关的Http请求，现在就是基本可以使用Rxjava 或者不使用
 *
 * 要能够自由的在Service 中使用！
 *
 *
 * Created by zenglb on 2017/3/17.
 */
public interface ApiService {
    // TODO: 2017/4/18 所有的请求都是在io 中执行，切换回main,那么要怎么省掉这部分相同的代码？
    // TODO: 2017/4/18 Rxjava 出来了内存泄漏，感觉会死的很惨，不敢上线实际的使用啊！



    /**
     * Login ,尝试使用Flowable 来处理，
     */
    @Headers("NoNeedAuthFlag: NoNeedAuthFlag")
    @POST("api/lebang/oauth/access_token")
    Observable<HttpResponse<LoginResult>> goLoginByRxjavaObserver(@Body LoginParams loginRequest);


    /**
     * Login ,尝试使用Flowable 来处理，
     */
    @Headers("NoNeedAuthFlag: NoNeedAuthFlag")
    @POST("api/lebang/oauth/access_token")
    Flowable<HttpResponse<LoginResult>> goLoginByRxjavaFlowable(@Body LoginParams loginRequest);

    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     */
    @Headers("NoNeedAuthFlag: NoNeedAuthFlag")
    @POST("api/lebang/oauth/access_token1")
    Call<HttpResponse<LoginResult>> goLoginByRetrofit(@Body LoginParams loginParams);





    @GET("api/lebang/night_school/{type}")
    Flowable<HttpResponse<List<AreuSleepResult>>> getAreuSleep(@Path("type") String type, @Query("page") int page);

    /**
     * get Message List();
     */
    @GET("api/lebang/messages")
    Call<HttpResponse<List<Messages>>> getMessages(@Query("max_id") long maxId, @Query("limit") int limit);

    /**
     * test get something
     */
    @GET("api/lebang/staffs/me/modules")
    Call<HttpResponse<ModulesResult>> getModules();

    /**
     * this request after login/oauth before logout
     * but no need oauth,so do not add auth header
     *
     * @param loginParams
     */
    @POST("api/lebang/oauth/access_token")
    @Headers("NoNeedAuthFlag: NoNeedAuthFlag")
    Call<HttpResponse<LoginResult>> refreshToken(@Body LoginParams loginParams);


}

