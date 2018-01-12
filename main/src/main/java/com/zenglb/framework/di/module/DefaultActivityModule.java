package com.zenglb.framework.di.module;

import dagger.Module;
import dagger.Provides;

/**
 *
 *
 *
 */
@Module
public abstract class DefaultActivityModule {

    @Provides
    static String provideName() {
        return "NULL,It is DefaultActivityModule";
    }

}
