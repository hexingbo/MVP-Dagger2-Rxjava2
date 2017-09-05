package com.zenglb.framework.dragger;

import dagger.Component;

/**
 *
 * Created by zlb on 2017/8/25.
 */
@Component(modules = AAAModule.class)
public interface AAAComponent {
    void inject(DraggerActivity draggerActivity);
}
