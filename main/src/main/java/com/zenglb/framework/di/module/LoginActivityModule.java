package com.zenglb.framework.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.zenglb.framework.di.scope.ActivityScope;
import com.zenglb.framework.mvp.login.LoginActivity;
import com.zenglb.framework.mvp.login.LoginPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 *
 *
 */
@Module
public class LoginActivityModule {

    @Provides
    static String provideName() {
        return LoginActivity.class.getName();
    }

    @Provides
    static SharedPreferences provideSp(LoginActivity activity) {
        return activity.getSharedPreferences("def", Context.MODE_PRIVATE);
    }

//    @Provides
//    @ActivityScope
//    static Student provideStudent() {
//        return new Student();
//    }


//
//    @ActivityScoped
//    @Binds
//    LoginPresenter taskPresenter(AddEditTaskPresenter presenter);


}
