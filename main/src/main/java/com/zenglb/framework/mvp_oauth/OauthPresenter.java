package com.zenglb.framework.mvp_oauth;

import android.content.Context;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.mvp_oauth.mvpbase.BasePresenter;
import com.zenglb.framework.retrofit.core.ApiService;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;
import com.zenglb.framework.rxhttp.BaseObserver;


/**
 * 最不喜欢的就是 OauthModel.DataListener 这个回调接口，不想使用EventBus ！Rxjava2
 * <p>
 * Google 的最基本的todo MVP 中也是类似这样处理的
 */
public class OauthPresenter extends BasePresenter<Oauth_MVP_Activity>
        implements OauthContract.OauthPresenter {

    private final OauthModel oauthMode = new OauthModel();  //


    /**
     * Model 层只是提供Observable ,subscribe 放在Presenter 层！
     * 应该再实验一个有远程缓存的出来！
     *
     *
     * @param loginParams
     */
    @Override
    public void login2(LoginParams loginParams, Context mContext) {
        oauthMode.getLoginObservable(loginParams)
                .compose(SwitchSchedulers.toMainThread())
                .subscribe(new BaseObserver<LoginResult>(mContext) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getIView().loginSuccess(loginResult);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        getIView().loginFail(message);
                    }
                });
    }


    /**
     * Model 层的数据是通过回调传回的，这种方式需要加一个CallBack，感觉有点冗余
     *
     * @param loginParams
     */
    @Override
    public void login(LoginParams loginParams) {
        oauthMode.loginWithCallBack(loginParams, new OauthModel
                .DataListener<LoginResult>() {
            @Override
            public void successInfo(LoginResult result) {
                getIView().loginSuccess(result);
            }

            @Override
            public void failInfo(String result) {
                getIView().loginFail(result);
            }
        });
    }


}
