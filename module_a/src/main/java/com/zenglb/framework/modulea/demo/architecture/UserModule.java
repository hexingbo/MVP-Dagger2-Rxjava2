package com.zenglb.framework.modulea.demo.architecture;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger2 的使用
 *
 * Created by zlb on 2017/11/20.
 */
@Module  //1。注明这个类是Module 类
public class UserModule {
    @Provides  //2 注明该方法是用来提供依赖对象的特殊方法
    public UserRepository provideFruit(){
        return new UserRepository();
    }
}
