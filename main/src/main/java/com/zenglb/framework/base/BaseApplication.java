package com.zenglb.framework.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * BaseApplication，初始化必然初始化的一些配置
 *
 */
public class BaseApplication extends Application  {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private boolean isDebug = false;  //App 是否是调试模式


    @Override
    public void onCreate() {
        super.onCreate();
//        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
//            ARouter.openLog();     // 打印日志
//            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
//
//        ARouter.init(this); // 尽可能早，推荐在Application中初始化

    }



    /**
     * 是否是Debug app,默认的是不测试
     */
    public String test = null;

    public boolean isDebug() {
        if (test == null) {
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        this.getPackageName(), PackageManager.GET_META_DATA);
                test = info.applicationInfo.metaData.getString("IS_DEBUG");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        isDebug="yes".equals(test);
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
