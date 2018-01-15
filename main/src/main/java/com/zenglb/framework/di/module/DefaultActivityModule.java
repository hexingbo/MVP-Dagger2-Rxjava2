package com.zenglb.framework.di.module;

import dagger.Module;
import dagger.Provides;

/**
 * 其实大部分的Activity 都只需要全局的对象依赖注入就好了，那么就使用这个吧
 *
 */
@Module
public abstract class DefaultActivityModule {

    @Provides
    static String provideName() {
        return "NULL,It is DefaultActivityModule";
    }

}
