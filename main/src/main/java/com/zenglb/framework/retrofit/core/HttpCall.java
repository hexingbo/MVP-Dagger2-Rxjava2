package com.zenglb.framework.retrofit.core;

import android.text.TextUtils;
import android.util.Log;

import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Http 请求配置和流程处理，部分Return 配置可以更加的简洁，为了试验，不简洁了
 *
 *
 * 1.问题
 * .retryOnConnectionFailure(false)以及设置了header("Connection", "Keep-Alive")
 * 是不是会导致http 请求 FAILED: java.io.IOException: unexpected end of stream on Connection
 *
 *
 * Created by Anylife.zlb@gmail.com on 2017/3/16.
 */
public class HttpCall {
    private static final String TAG = HttpCall.class.getSimpleName();
    private static final String baseUrl = "http://test.4009515151.com/";  // WARMING-just for test !
//    private static final String baseUrl = "http://xxx.4009515151.com/";

    private static String TOKEN;

    /**
     * 下面apiService对象其实是一个动态代理对象，并不是一个真正的ApiService接口的implements产生的对象，
     * 当apiService对象调用getXxxx方法时会被动态代理拦截，然后调用Proxy.newProxyInstance方法中的InvocationHandler对象，
     * 它的invoke方法会传入3个参数：{@link Retrofit}
     */
    private static ApiService apiService;

    public static void setToken(String token) {
        TOKEN = token;
    }

    /**
     * 每次都要invoke 这个方法不是很繁琐吗？
     *
     * @return
     */
    public static ApiService getApiService() {
        if (apiService == null) {
            //处理没有认证  http 401 Not Authorised
            Authenticator mAuthenticator2 = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    if (responseCount(response) >= 2) {
                        // If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
                        return null;
                    }
                    refreshToken();
                    return response.request().newBuilder()
                            .header("Authorization", TOKEN)
                            .build();
                }
            };

            /**
             * 如果你的 token 是空的，就是还没有请求到 token，比如对于登陆请求，是没有 token 的，
             * 只有等到登陆之后才有 token，这时候就不进行附着上 token。另外，如果你的请求中已经带有验证 header 了，
             * 比如你手动设置了一个另外的 token，那么也不需要再附着这一个 token.
             */
            Interceptor mRequestInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    if (TextUtils.isEmpty(TOKEN)) {
                        TOKEN = SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
                    }

                    /***
                     * TOKEN == null，Login/Register noNeed Token
                     * noNeedAuth(originalRequest)    refreshToken api request is after log in before log out,but  refreshToken api no need auth
                     */
                    if (TextUtils.isEmpty(TOKEN) || alreadyHasAuthorizationHeader(originalRequest) || noNeedAuth(originalRequest)) {
                        Response originalResponse = chain.proceed(originalRequest);
                        return originalResponse.newBuilder()
                                //get http request progress,et download app
//                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }

                    Request authorisedRequest = originalRequest.newBuilder()
                            .header("Authorization", TOKEN)
                            .header("Connection", "Keep-Alive") //新添加，time-out默认是多少呢？
                            .build();

                    Response originalResponse = chain.proceed(authorisedRequest);
                    return originalResponse.newBuilder()
//                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();


                }
            };

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(11, TimeUnit.SECONDS)
//                    .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                    .addNetworkInterceptor(mRequestInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .authenticator(mAuthenticator2)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();


            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }


    /**
     * If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
     * count times ++
     *
     * @param response
     * @return
     */
    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    /**
     * check if already has auth header
     *
     * @param originalRequest
     */
    private static boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        if (originalRequest.headers().toString().contains("Authorization")) {
            Log.w(TAG, "already add Auth header");
            return true;
        }
        return false;
    }

    /**
     * some request after login/oauth before logout
     * but they no need oauth,so do not add auth header
     *
     * @param originalRequest
     */
    private static boolean noNeedAuth(Request originalRequest) {
        if (originalRequest.headers().toString().contains("NoNeedAuthFlag")) {
            Log.d("WW", "no need auth !");
            return true;
        }
        return false;
    }




    /**
     * uese refresh token to Refresh an Access Token
     * 不是必须这样
     */
    private static void refreshToken() {
        if (TextUtils.isEmpty(SharedPreferencesDao.getInstance().getData(SPKey.KEY_REFRESH_TOKEN, "", String.class))) {
            return;
        }

        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("refresh_token");
        loginParams.setRefresh_token(SharedPreferencesDao.getInstance().getData(SPKey.KEY_REFRESH_TOKEN, "", String.class));

        Call<HttpResponse<LoginResult>> refreshTokenCall = HttpCall.getApiService().refreshToken(loginParams);
        try {
            retrofit2.Response<HttpResponse<LoginResult>> response = refreshTokenCall.execute();
            if (response.isSuccessful()) {
                int responseCode = response.body().getCode();
                if (responseCode == 0) {
                    HttpResponse<LoginResult> httpResponse = response.body();
                    SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + httpResponse.getResult().getAccessToken());
                    SharedPreferencesDao.getInstance().saveData(SPKey.KEY_REFRESH_TOKEN, httpResponse.getResult().getRefreshToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
