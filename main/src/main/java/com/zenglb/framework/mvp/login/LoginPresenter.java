package com.zenglb.framework.mvp.login;

import android.app.Activity;

import com.zenglb.framework.http.ApiService;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.LoginResult;
import com.zlb.httplib.core.BaseObserver;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;

import javax.inject.Inject;

/**
 * Login Presenter
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
public class LoginPresenter implements LoginContract.LoginPresenter {

    ApiService apiService;
    LoginContract.LoginView mLoginView;  // 需要抽象出来

    @Inject
    public LoginPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void login(LoginParams loginParams) {
        apiService.goLoginByRxjavaObserver(loginParams)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<LoginResult>((Activity) mLoginView) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if(null!=mLoginView){
                            mLoginView.loginSuccess(loginResult); //页面关闭了怎么办？
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        if(null!=mLoginView){
                            mLoginView.loginFail(message);
                        }
                    }
                });
    }


    /**
     * 这下面的两行能不能 Base化解
     *
     * @param view the view associated with this presenter
     */
    @Override
    public void takeView(LoginContract.LoginView view) {
        mLoginView = view;
    }


    /**
     * 防止异步请求回来后View 已经不在了
     *
     */
    @Override
    public void dropView() {
        mLoginView=null;  //f
    }


}
