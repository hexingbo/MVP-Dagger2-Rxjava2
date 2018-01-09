package com.zenglb.framework.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.zenglb.framework.di.scope.ActivityScope;
import com.zenglb.framework.mvp.Student;
import com.zenglb.framework.mvp.activity.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by QingMei on 2017/8/16.
 * desc:
 */
@Module
public class MainActivityModule {

    @Provides
    static String provideName() {
        return MainActivity.class.getName();
    }

    @Provides
    static SharedPreferences provideSp(MainActivity activity) {
        return activity.getSharedPreferences("def", Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    static Student provideStudent() {
        return new Student();
    }

}
