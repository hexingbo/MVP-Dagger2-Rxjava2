package com.zenglb.framework.activity.java8test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


/**
 * 接口默认方法
 * 在java8中，接口可以添加默认的方法以及静态方法，其类型也是public abstract的类型
 *
 * Created by zenglb on 2017/5/3.
 */
public interface defaultFunc {
    @RequiresApi(api = Build.VERSION_CODES.N)
    default String defaultMethod() {
        Log.i("DataSource", "i am defaultMethod");
        return "api<24 就不能调用？";
    }
}