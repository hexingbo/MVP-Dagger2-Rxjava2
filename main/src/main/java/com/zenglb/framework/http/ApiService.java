package com.zenglb.framework.http;

import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.CustomWeatherResult;
import com.zenglb.framework.http.result.JokesResult;
import com.zenglb.framework.http.result.LoginResult;
import com.zenglb.framework.http.result.StaffMsg;
import com.zenglb.framework.mvp.handylife.AnyLifeResultBean;
import com.zlb.httplib.core.HttpResponse;

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
import retrofit2.http.Url;

/**
 *
 *
 * ApiService 对象最终会被在retrofit.create(ApiService.class)后被动态代理
 * 比如 HttpCall.getApiService().getWeather(url,"深圳")
 * <p>
 * Retrofit关心的就是method和它的参数args，接下去Retrofit就会用Java反射获取到getWeather方法的注解信息，
 * 配合args参数，创建一个ServiceMethod对象，ServiceMethod就像是一个中央处理器，传入Retrofit对象和
 * Method对象，调用各个接口和解析器，最终生成一个Request，包含api 的域名、path、http请求方法、请求头、
 * 是有body、是否是multipart等等。最后返回一个Call对象，Retrofit2中Call接口的默认实现是OkHttpCall，
 * 它默认使用OkHttp3作为底层http请求client。
 *
 *
 * Created by zenglb on 2017/3/17.
 */
public interface ApiService {

    @GET("https://zhihu.0x01.site/articles/test1")
    Observable<HttpResponse<List<AnyLifeResultBean>>> getHandyLifeData(@Query("type") String type, @Query("page") int page);

    /**
     * 第三方动态 url 访问
     * 测试在同一个系统下访问外部URL
     */
    @GET
    Call<CustomWeatherResult> getWeather(@Url String url, @Query("city") String city);


    @GET("api/lebang/night_school/{type}")
    Observable<HttpResponse<List<JokesResult>>> getJokes(@Path("type") String type, @Query("page") int page);


    /**
     * Login ,尝试使用Flowable 来处理，
     */
    @Headers("NeedOauthFlag: NeedOauthFlag")
    @POST("api/lebang/oauth/access_token")
    Observable<HttpResponse<LoginResult>> goLoginByRxjavaObserver(@Body LoginParams loginRequest);

    /**
     * 获取信息
     */
    @GET("api/lebang/staffs/me/detail")
    Observable<HttpResponse<StaffMsg>> getStaffMsg();


    @GET("api/lebang/night_school/{type}")
    Observable<HttpResponse<List<JokesResult>>> getAreuSleepByObserver(@Path("type") String type, @Query("page") int page);

    /**
     * this request after login/oauth before logout
     * but no need oauth,so do not add auth header
     *
     * @param loginParams
     */
    @POST("api/lebang/oauth/access_token")
    @Headers("NeedOauthFlag: noNeed")
    Call<HttpResponse<LoginResult>> refreshToken(@Body LoginParams loginParams);

    /**
     * Login ,尝试使用Flowable 来处理，
     */
    @Deprecated
    @Headers("NeedOauthFlag: noNeed")
    @POST("api/lebang/oauth/access_token")
    Flowable<HttpResponse<LoginResult>> goLoginByRxjavaFlowable(@Body LoginParams loginRequest);

    @GET()
    Call<String> getUserProfile(@Url String url);

}

