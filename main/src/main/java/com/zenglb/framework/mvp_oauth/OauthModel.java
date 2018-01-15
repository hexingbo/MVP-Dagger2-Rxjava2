package com.zenglb.framework.mvp_oauth;

import android.support.annotation.NonNull;

import com.zenglb.framework.http.HttpCall;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;
import com.zenglb.framework.mvp_base.old.BaseModel;
import com.zlb.httplib.core.HttpResponse;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.LoginResult;
import com.zlb.httplib.core.BaseObserver;

import io.reactivex.Observable;

/**
 * DataListener ......
 */
public class OauthModel extends BaseModel {


    /**
     * 直接的返回Observable
     *
     * @param loginParams
     * @return
     */
    public Observable<HttpResponse<LoginResult>> getLoginObservable(@NonNull LoginParams loginParams) {

        Thread.currentThread().getName();
        return HttpCall.getApiService().goLoginByRxjavaObserver(loginParams);

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

        HttpCall.getApiService().goLoginByRxjavaObserver(loginParams)
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
