package com.zenglb.framework.mvp_base;

/**
 * 抽象出Presenter 接口
 *
 *
 * @param <M>
 * @param <V>
 */
@Deprecated
public interface IPresenter<M extends IModel,V extends IView> {

    void attachModelAndView(M model,V view);

    /**
     * 防止内存的泄漏, 清除Presenter与Activity之间的绑定
     */
    void detachView();

    /**
     * @return 获取View
     */
    V getIView();

    M getIModel();

}
