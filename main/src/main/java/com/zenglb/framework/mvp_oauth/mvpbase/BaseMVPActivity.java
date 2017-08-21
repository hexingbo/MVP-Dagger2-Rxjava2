package com.zenglb.framework.mvp_oauth.mvpbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.zenglb.baselib.base.BaseActivity;

/**
 * 并不要求所有的功能都用MVP，真的不要求,想这种简单的登录就不需要
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BaseMVPActivity<P extends BasePresenter> extends BaseActivity implements IView{
    protected P mPresenter;    //View  中包含P，以后的Activity 只要从写loadPresenter 就好了

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = loadPresenter();  //实例化Presenter
        initCommonData();
    }

    /**
     * 如果某个功能要搞MVP 模式，那么具体的P一定要在子类中实例化
     * @return
     */
    protected abstract P loadPresenter();

    /**
     * 让P和V 关联起来
     */
    private void initCommonData() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
