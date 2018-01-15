package com.zenglb.framework.di;

import com.zenglb.framework.MyApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 全局的单例的东西提到这里来，比如SharedPrefence,Daosession 等等 ！
 *
 */
@Singleton
@Component(modules = {
        AppModule.class,               //全局的Module,要确保提供的对象是全局唯一的
        AllActivityModule.class,       //减少模版代码
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
})

// DaggerMainComponent.create().inject(this);   //在MyApplication 中使用过了
public interface MainComponent {

    void inject(MyApplication application);

}
