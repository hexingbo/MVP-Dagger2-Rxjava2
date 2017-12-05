package com.zenglb.framework.mvp_oauth;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.mvp_base.BasePresenter;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;
import com.zenglb.framework.rxhttp.BaseObserver;

/**
 * 最不喜欢的就是 OauthModel.DataListener 这个回调接口，不想使用EventBus ！Rxjava2
 * <p>
 * Google 的最基本的todo MVP 中也是类似这样处理的
 */
public class OauthPresenter extends BasePresenter<OauthModel, Oauth_MVP_Activity>
        implements OauthContract.OauthPresenter {

    /**
     * Model 层只是提供Observable ,subscribe 放在Presenter 层！
     * 这里的场景是比较简单的，只需要从网络缓存的，更复杂的场景是：
     * <p>
     * Local --》 Remote   --》Cache To Local  --》 DisPlay
     *
     * @param loginParams
     */
    @Override
    public void login2(LoginParams loginParams) {
        getIModel().getLoginObservable(loginParams)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<LoginResult>(getIView().mContext) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        /**
                         * getIView() 可能出现是null 的情况，
                         */
                        if(isAttachView()){
                            getIView().loginSuccess(loginResult);
                        }



//                        invokeMethod();  //把loginSuccess 放到里面去执行，不用判断isAttachView();
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
    @Deprecated
    public void login(LoginParams loginParams) {
        getIModel().loginWithCallBack(loginParams, new OauthModel
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
