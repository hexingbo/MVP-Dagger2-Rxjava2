package com.zenglb.framework.mvp_base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zenglb.baselib.base.BaseActivity;

/**
 * 并不要求所有的功能都用MVP, 如果不需要使用MVP 就直接extends BaseActivity
 *
 * implements IView 是为了抽象出统一的View 的操作，比如ShowDialog.ShowError.
 *
 * <P extends BasePresenter,M extends BaseModel>  范型上限
 *
 * 按理来说M，V 是在P 中产生关联的，在V 中不会和Model 产生任何的关联，
 * 这里是通过{@link BaseMVPActivity}s {@link BaseMVPActivity#initCommonData} method来在P 中关联M和V
 *
 * P和M 通过下面的代码实例化
 * mPresenter = CreateObjUtil.getT(this, 0);
 * mModel = CreateObjUtil.getT(this,1);
 *
 * 在P 中就只要通过{@link BasePresenter} 的 {@link BasePresenter#getIModel()}
 * 和{@link BasePresenter#getIView()} 来获取view 和 Model
 *
 * 当然问题getIView 要每次判断是否为null, 好烦啊，能不能对象为null 不执行方法啊？
 *
 * @param <P>
 * @param <M>
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BaseMVPActivity<P extends BasePresenter, M extends BaseModel> extends BaseActivity implements IView {
    protected P mPresenter;
    protected M mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = CreateObjUtil.getT(this, 0);
        mModel = CreateObjUtil.getT(this, 1);

        initCommonData();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void showError() {

    }

    /**
     * V 和 M 在P 中关联起来了，V 要获取M 的数据什么的都是通过P 来获取的
     */
    private void initCommonData() {
        if (mPresenter != null) {
            mPresenter.attachModelAndView(mModel, this);
        }
    }

    /**
     * 取消Presenter 和 View 的关联
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

}
