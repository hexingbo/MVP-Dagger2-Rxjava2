package com.zenglb.framework.mvp.presenter;



import com.zenglb.framework.mvp.activity.MainActivity;
import com.zenglb.framework.mvp.contract.MainContract;
import com.zenglb.framework.mvp.model.MainModel;

import javax.inject.Inject;

/**
 * Created by QingMei on 2017/8/16.
 * desc:
 */

public class MainPresenter implements MainContract.Presenter {

    private final MainActivity view;
    private final MainModel model;

    @Inject
    public MainPresenter(MainActivity view, MainModel model) {
        this.view = view;
        this.model = model;
    }

    public void requestHttp() {
        view.onGetMessage(model.returnMessage());
    }
}
