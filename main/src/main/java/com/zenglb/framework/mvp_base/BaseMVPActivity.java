package com.zenglb.framework.mvp_base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zenglb.baselib.base.BaseActivity;

/**
 *
 *
 * 并不要求所有的功能都用MVP，真的不要求,简单的登录就不强制的需要
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BaseMVPActivity<P extends BasePresenter,M extends BaseModel> extends BaseActivity implements IView{
    protected P mPresenter;    //View  中包含P，以后的Activity 只要从写loadPresenter 就好了
    protected M mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = CreateObjUtil.getT(this, 0);
        mModel = CreateObjUtil.getT(this,1);

        initCommonData();
    }



    /**
     * 让P和V 关联起来
     *
     */
    private void initCommonData() {
        if (mPresenter != null) {
            mPresenter.attachModelAndView(mModel,this);
        }
    }

    /**
     * 取消Presenter 和 View 的关联
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }


}
