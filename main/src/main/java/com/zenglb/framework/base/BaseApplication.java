package com.zenglb.framework.base;

import android.app.Application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * BaseApplication，初始化必然初始化的一些配置
 *
 */
public class BaseApplication extends Application  {
    public static final String TAG = BaseApplication.class.getSimpleName();

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
