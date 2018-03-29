package com.zenglb.framework;

import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * 只是开发 Debug 需要的
 *
 * Created by zenglb on 2017/4/24.
 */
public class SdkManager {
    public static void initDebugOrRelease(Context context) {
        /**
         * Debug need init ,在这里全部注释
         */
        Stetho.initializeWithDefaults(context);


        /**
         * Release need init ,在这里全部打开
         */


    }
}