package com.zenglb.framework.di;

import com.zenglb.framework.di.component.BaseActivityComponent;
import com.zenglb.framework.di.module.MainActivityModule;
import com.zenglb.framework.di.module.SecondActivityModule;
import com.zenglb.framework.di.scope.ActivityScope;
import com.zenglb.framework.mvp.activity.MainActivity;
import com.zenglb.framework.mvp.activity.SecondActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 全部放在这里来统一的管理 ！
 *
 *
 */
@Module(subcomponents = {
        BaseActivityComponent.class
})
public abstract class AllActivitysModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivitytInjector();

    @ContributesAndroidInjector(modules = SecondActivityModule.class)
    abstract SecondActivity contributeSecondActivityInjector();

}
