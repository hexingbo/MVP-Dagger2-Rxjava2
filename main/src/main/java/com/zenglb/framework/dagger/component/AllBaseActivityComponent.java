package com.zenglb.framework.dagger.component;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * 在 {@link com.zenglb.framework.dagger.AllDefaultActivityModule} 中被使用
 * 不要在每个Activity 中建立一个ActivitySubComponent，麻烦而且重复的无聊代码
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Subcomponent(modules = {
        AndroidInjectionModule.class,
})

public interface AllBaseActivityComponent extends AndroidInjector<BaseMVPActivity> {

    //每一个继承BaseActivity的Activity，都共享同一个SubComponent
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BaseMVPActivity> {

    }

}
