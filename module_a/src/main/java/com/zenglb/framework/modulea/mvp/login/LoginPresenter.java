package com.zenglb.framework.modulea.mvp.login;

import android.app.Activity;

import com.zlb.http.ApiService;
import com.zlb.http.param.LoginParams;
import com.zlb.http.result.LoginResult;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.rxUtils.SwitchSchedulers;

import javax.inject.Inject;

/**
 * Login Presenter
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
public class LoginPresenter implements LoginContract.LoginPresenter {

    ApiService apiService;
    LoginContract.LoginView mLoginView;  // 需要抽象出来

    /**
     * 构造方法被  @Inject  注解标注了
     *
     * @Inject
     * LoginPresenter loginPresenter;
     * 在  Activity 中很方便的依赖注入
     *
     * @param apiService
     */
    @Inject
    public LoginPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Presenter  这里最好不要进行Http请求，
     * 测试的时候保持Presenter 的单纯行，方便测试，单元测试
     *
     *
     * ！！！！！！！！！ 反例 ！！！！！！！！！！
     *
     * @param loginParams
     */
    @Override
    public void login(LoginParams loginParams) {
        apiService.goLoginByRxjavaObserver(loginParams)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<LoginResult>((Activity) mLoginView) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if(null!=mLoginView){
                            mLoginView.loginSuccess(loginResult);
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
