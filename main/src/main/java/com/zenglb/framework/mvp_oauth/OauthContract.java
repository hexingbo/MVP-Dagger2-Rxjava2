package com.zenglb.framework.mvp_oauth;

import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;

/**
 *
 * Created by zlb on 2017/8/20.
 */
public class OauthContract {

    public interface OauthView {
        void loginSuccess(LoginResult loginBean); // 登录成功，展示数据
        void loginFail(String failMsg);
    }

    public interface OauthPresenter {
        void login(LoginParams loginParams);      // 业务逻辑
    }


}
