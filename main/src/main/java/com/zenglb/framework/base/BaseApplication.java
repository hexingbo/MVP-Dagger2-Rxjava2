package com.zenglb.framework.base;

import android.app.Activity;
import android.app.Application;
import com.squareup.leakcanary.RefWatcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;

/**
 * BaseApplication，初始化必然初始化的一些配置
 * 1.内存泄漏的检测配置
 * 2.SharedPreferencesDao 的初始化
 */
public class BaseApplication extends Application  {
    public static final String TAG = BaseApplication.class.getSimpleName();
    public RefWatcher refWatcher;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;  //1111111111111

    @Override
    public void onCreate() {
        super.onCreate();

    }



    /**
     * 获取进程名字
     *
     * @return
     */
    public String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
