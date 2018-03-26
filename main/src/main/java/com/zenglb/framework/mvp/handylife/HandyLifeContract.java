package com.zenglb.framework.mvp.handylife;

import com.zenglb.framework.mvp.BasePresenter;
import com.zenglb.framework.mvp.BaseView;

import java.util.List;

/**
 * 合约，一个功能的基本只要看这个类就知道功能纲要了
 * Created by zlb on 2018/3/23.
 */
public class HandyLifeContract {

    /**
     * 对UI 的操作的接口有哪些，一看就只明白了
     *
     */
    public interface HandyLifeView extends BaseView<HandyLifePresenter> {
        void showHandyLifeData(List<HandyLifeResultBean> tabsResultBeans);
        void getHandyLifeDataFailed(int code, String message);
        boolean isActive();
    }

    /**
     * View 层对Presenter 的请求
     *
     */
    public interface HandyLifePresenter extends BasePresenter<HandyLifeView> {
        void getHandyLifeData(String type,int page);
    }

}
