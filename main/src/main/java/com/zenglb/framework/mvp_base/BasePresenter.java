package com.zenglb.framework.mvp_base;

import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

/**
 * 抽象出P 的共性，我们可以看见 View 和 Module 之间的关联是通过在P 中完成的。
 * <p>
 * public abstract class BasePresenter<M, V>
 * <p>
 * Created by zlb on 2017/8/20.
 */

//public abstract class BasePresenter<M, V> {
public abstract class BasePresenter<M extends IModel , V extends IView> implements IPresenter {
    private WeakReference<V> mViewRef;  // View接口类型的弱引用
    private WeakReference<M> mModelRef; // 其实根本没有必要

    /**
     * 建立关联
     *
     * @param iview
     */
    @Override
    @Deprecated
    public void attachView(IView iview) {
        mViewRef = new WeakReference(iview);
    }

    @Override
    public void attachModelAndView(IModel model, IView view) {
        mViewRef = new WeakReference(view);
        mModelRef = new WeakReference(model);
    }


    /**
     * 解除关联
     */
    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 每次都要这样子判断是不是会很累啊
     *
     * @return
     */
    public boolean isAttachView() {
        return mViewRef != null && mViewRef.get() != null;
    }


    /**
     * 获取View层（Activity／Fragment 等），有可能会已经销毁了，所以使用虚引用
     *
     * @return
     */
    @NonNull
    @Override
    public V getIView() {
        return mViewRef.get();
    }



    @Override
    public M getIModel() {
        return mModelRef.get();
    }


}
