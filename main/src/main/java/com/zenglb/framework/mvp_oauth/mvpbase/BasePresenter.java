package com.zenglb.framework.mvp_oauth.mvpbase;

import java.lang.ref.WeakReference;

/**
 * 抽象出P 的共性，我们可以看见 View 和 Module 之间的关联是通过在P 中完成的。
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BasePresenter<V extends IView> implements IPresenter {

    private WeakReference<V> mViewRef; // View接口类型的弱引用

    /**
     * 建立关联
     *
     * @param iview
     */
    @Override
    public void attachView(IView iview) {
        mViewRef = new WeakReference(iview);
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
     * 获取View层（Activity／Fragment 等）
     *
     * @return
     */
    @Override
    public V getIView() {
        return mViewRef.get();
    }


}
