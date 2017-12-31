package com.zenglb.framework.mvp_oauth;

import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.LoginResult;

/**
 * 合约，一个功能的基本只要看这个类就知道功能纲要了
 *
 * 框架的重要性就是只看接口就能知道功能，根本不去具体的实现
 *
 * 实现应该外包给#@！%¥%……
 *
 * Created by anylife.zlb@gmail.com
 * on 2017/8/20.
 */
public class OauthContract {

    /**
     * 对UI 的操作的接口有哪些，一看就只明白了
     *
     */
    public interface OauthView {
        void loginSuccess(LoginResult loginBean); // 登录成功，展示数据
        void loginFail(String failMsg);
    }


    /**
     *View 层对Presenter 的请求
     *
     */
    public interface OauthPresenter {
        void login(LoginParams loginParams);    // Model层面拿回数据后通过回调通知Presenter 再通知View
        void login2(LoginParams loginParams);   // Model 返回Obserable<T> 给 Preseter 订阅
    }


}
