package com.zenglb.framework.mvp.login;

import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.LoginResult;
import com.zenglb.framework.mvp_base.BasePresenter;
import com.zenglb.framework.mvp_base.BaseView;

/**
 * Created by QingMei on 2017/8/16.
 * desc:
 */

public interface LoginContract {

    /**
     * 对UI 的操作的接口有哪些，一看就只明白了
     *
     */
    interface View extends BaseView<Presenter> {
        void loginSuccess(LoginResult loginBean); // 登录成功，展示数据
        void loginFail(String failMsg);
    }


    interface Presenter extends BasePresenter<View> {
        void login(LoginParams loginParams);    // Model层面拿回数据后通过回调通知Presenter 再通知View
    }
}



