package com.zenglb.framework.mvp_oauth;

import android.support.annotation.NonNull;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.mvp_base.BaseModel;
import com.zenglb.framework.retrofit.core.HttpResponse;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;
import com.zenglb.framework.rxhttp.BaseObserver;

import io.reactivex.Observable;

/**
 * DataListener ......
 */
public class OauthModel extends BaseModel {


    /**
     * 使用回调的方式把数据返回，
     *
     * @param loginParams
     * @return
     */
    public Observable<HttpResponse<LoginResult>> getLoginObservable(@NonNull LoginParams loginParams) {

        Thread.currentThread().getName();

        return ApiService.goLoginByRxjavaObserver(loginParams);
//                .compose(SwitchSchedulers.toMainThread())
//                .subscribe(new BaseObserver<LoginResult>(null) {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        listener.successInfo(loginResult);
//                    }
//
//                    @Override
//                    public void onFailure(int code, String message) {
//                        super.onFailure(code, message);
//                        listener.failInfo(message);
//                    }
//                });
    }


    /**
     * 使用回调的方式把数据返回，
     *
     * @param loginParams
     * @param listener
     * @return
     */
    @Deprecated
    public void loginWithCallBack(@NonNull LoginParams loginParams, @NonNull final DataListener listener) {
        if (listener == null) {
            throw new RuntimeException("listener =null 无法通知更新UI");
        }

        ApiService.goLoginByRxjavaObserver(loginParams)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<LoginResult>(null) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        listener.successInfo(loginResult);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        listener.failInfo(message);
                    }
                });
    }


    /**
     * 通过接口通知数据的更新
     *
     * @param <T>
     */
    @Deprecated
    public interface DataListener<T> {
        void successInfo(T result);

        void failInfo(String result);
    }
}
