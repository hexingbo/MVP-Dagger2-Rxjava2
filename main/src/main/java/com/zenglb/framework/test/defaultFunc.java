package com.zenglb.framework.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

/**
 * 接口默认方法
 * 在java8中，接口可以添加默认的方法以及静态方法，其类型也是public abstract的类型
 *
 * Created by zenglb on 2017/5/3.
 */
public interface defaultFunc<T> {
    List<T> reqeustData(String name);

    @RequiresApi(api = Build.VERSION_CODES.N)
    default void defaultMethod() {
        Log.i("DataSource", "i am defaultMethod");
    }

    static void staticMethod(){
        Log.i("DataSource", "i am staticMethod");
    }

}