package com.zenglb.framework.dragger;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**
 * Module 就是用来生成各种实例，可以类比为一个工厂
 *
 * Created by zlb on 2017/8/25.
 */
@Module
public class AAAModule {

    @Provides
    public Gson provideGson(){
        return new Gson();
    }

}
