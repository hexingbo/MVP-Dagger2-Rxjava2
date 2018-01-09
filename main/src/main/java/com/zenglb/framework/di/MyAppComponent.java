package com.zenglb.framework.di;



import com.zenglb.framework.MyApplication;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by QingMei on 2017/7/28.
 * desc:
 */
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AllActivitysModule.class
})

// DaggerMyAppComponent.create().inject(this);   //在MyApplication 中使用过了
public interface MyAppComponent {
    void inject(MyApplication application);
}
