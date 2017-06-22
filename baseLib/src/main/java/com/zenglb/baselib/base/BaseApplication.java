package com.zenglb.baselib.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.squareup.leakcanary.RefWatcher;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * BaseApplication，初始化必然初始化的一些配置
 * 1.内存泄漏的检测配置
 * 2.SharedPreferencesDao 的初始化
 */
public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    public RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName();  //注意区分进程初始化不同的东西

        if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) { //main Process
            SharedPreferencesDao.initSharePrefenceDao(this);
        } else {
            //
        }

    }


    /**
     * 获取RefWatcher
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
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
