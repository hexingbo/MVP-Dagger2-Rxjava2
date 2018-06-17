package com.zenglb.framework.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


import com.alibaba.android.arouter.launcher.ARouter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * BaseApplication，初始化必然初始化的一些配置
 */
public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    public boolean isDebug = false;  //App 是否是调试模式，默认不是，不要把调试信息加进去


    @Override
    public void onCreate() {
        super.onCreate();

        //ARouter 相关的配置
        if (isAppDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);       // 尽可能早，推荐在Application中初始化

    }


    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private boolean isAppDebug() {
        if (TextUtils.isEmpty(this.getPackageName())) return false;
        try {
            PackageManager pm = this.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(this.getPackageName(), 0);
            isDebug=ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            return isDebug;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否是Debug
     *
     * @return
     */
    public boolean isDebug(){
        return isDebug;
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
