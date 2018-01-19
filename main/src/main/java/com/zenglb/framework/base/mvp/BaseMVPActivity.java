package com.zenglb.framework.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.mvp.BasePresenter;

import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * 需要依赖注入extends this ，其实可以不要这么多的Base 吧，难以维护 ...
 *
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BaseMVPActivity extends BaseActivity implements  HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    BasePresenter basePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //一处声明，处处依赖注入，before calling super.onCreate();:
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Bind view to the presenter which will signal for the presenter to load the task.
        basePresenter.takeView(this);  //NEED base
    }

    @Override
    public void onPause() {
        basePresenter.dropView();  //Need BASE
        super.onPause();
    }

}
