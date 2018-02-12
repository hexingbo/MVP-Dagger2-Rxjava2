package com.zenglb.framework.dagger.module;

import dagger.Module;
import dagger.Provides;

/**
 * 其实大部分的Activity 都只需要全局的对象依赖注入就好了，那么就使用这个吧
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Module
public abstract class DefaultActivityModule {

    /**
     * 这是测试性质的提供一个String
     * @return
     */
    @Provides
    static String provideName() {
        return "NULL,It is DefaultActivityModule";
    }

}
