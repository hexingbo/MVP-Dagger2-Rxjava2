package com.zenglb.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * BaseDiActivity：不要MVP，但是要依赖注入用这个（同时在这里加两行代码）
 * 同时在{@link com.zenglb.framework.di.AllActivityModule} 中按照模版添加两行代码
 *
 *
 * @author zenglb 2018 01 14
 */
public abstract class BaseDIActivity extends BaseActivity implements HasFragmentInjector, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @Inject DispatchingAndroidInjector<android.app.Fragment> frameworkFragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //一处声明，处处依赖注入
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    @Override
    public AndroidInjector<android.app.Fragment> fragmentInjector() {
        return frameworkFragmentInjector;
    }
    private static final String TAG = BaseDIActivity.class.getSimpleName();


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AndroidInjection.inject(this);
//    }


}
