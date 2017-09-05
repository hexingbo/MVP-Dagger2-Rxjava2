package com.zenglb.framework.mvp_oauth;

import com.zenglb.framework.mvp_oauth.mvpbase.BasePresenter;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;


/**
 * 最不喜欢的就是 OauthModel.DataListener 这个回调接口，不想使用EventBus ！Rxjava2
 *
 * Google 的最基本的todo MVP 中也是类似这样处理的
 *
 *
 */
public class OauthPresenter extends BasePresenter<Oauth_MVP_Activity>
                            implements OauthContract.OauthPresenter {

    private final OauthModel oauthMode=new OauthModel();  //


    @Override
    public void login(LoginParams loginParams) {

           oauthMode.login(loginParams, new OauthModel
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
