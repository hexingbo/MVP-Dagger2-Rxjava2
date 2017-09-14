package com.zenglb.framework.config;


import android.os.Environment;

/**
 * Created by zenglb on 2017/3/15.
 */
public class FileCacheConfig {
    public static final String CACHE_HOME = Environment.getExternalStorageDirectory() + "/lebang/";
    public static final String CACHE_IMAGE = CACHE_HOME + "images/";
    public static final String CACHE_DATA = CACHE_HOME + "data/";
    public static final String CACHE_AUDIO = CACHE_HOME + "audio/";
    public static final String CACHE_APK = CACHE_HOME + "apk/";


}