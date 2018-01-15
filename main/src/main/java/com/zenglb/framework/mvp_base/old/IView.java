package com.zenglb.framework.mvp_base.old;

/**
 * 界面中会有很多类似的操作方法，比如显示 隐藏Dialog，提示错误等
 *
 */
@Deprecated
public interface IView {

    //显示等待框
    void showLoading();

    //隐藏Dialog
    void hideLoading();

    //显示错误提示
    void showError(String errorMsg);

}
