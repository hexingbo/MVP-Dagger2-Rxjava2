package com.zenglb.framework;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zenglb.framework.base.BaseApplication;
import com.zenglb.framework.di.AppModule;
import com.zenglb.framework.di.DaggerMainComponent;
import com.zenglb.framework.http.HttpRetrofit;
import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * 依赖注入还有的问题
 *
 * 1.在AllActivityModule 都要添加那默认的两行代码好烦人，manifest 中 OK ？
 * 2.在非Activity 中注入XX 的问题
 * 3.
 *
 * Created by zenglb on 2017/3/15.
 */
public class MyApplication extends BaseApplication implements HasActivityInjector {
    public static final String TAG = MyApplication.class.getSimpleName();
    public static final String MAIN_PROCESS_NAME = "com.zenglb.framework";
    public static final String WEB_PROCESS_NAME = "com.zenglb.framework:webprocess";
    private boolean isDebug = false;  //App 是否是调试模式

    private static MyApplication myApplication;
    public RefWatcher refWatcher;

    //依赖注入的核心原则：一个类不应该知道如何实现依赖注入。
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;  //1111111111111

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName();
        Log.d(TAG, processName + "Application onCreate");

        myApplication = this;

        //Module  带有构造方法并且参数被使用的情况下所产生的Component 是没有Create方法的
//        DaggerMainComponent.create().inject(this);
        DaggerMainComponent.builder().appModule(new AppModule(this)).build().inject(this); //22222222222222

        // 很多的东西最好能放到一个IntentService 中去初始化
        // InitializeService.start(this);
        isDebugCheck();
        initApplication();
    }


    //33333333333
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    /**
     * 根据不同的进程来初始化不同的东西
     * 比如web进程就不需要初始化推送，也不需要图片加载等等
     * <p>
     * 发新版 或 测试版也有不同的初始化
     * 比如调试工具stetho 在debug 环境是要的，Release 是不需要的
     */
    private void initApplication() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        //部分 初始化服务最好能新开一个IntentService 去处理,bugly 在两个进程都有初始化
        String processName = getProcessName();

        switch (processName) {
            case MAIN_PROCESS_NAME:
                SdkManager.initDebugOrRelease(this);
                HttpRetrofit.init(this);

                refWatcher = LeakCanary.install(this);  //只管主进程的,其他的进程自保吧
                //创建默认的ImageLoader配置参数
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
                //Initialize ImageLoader with configuration.
                ImageLoader.getInstance().init(configuration);
                break;

            case WEB_PROCESS_NAME:  //WebView 在单独的进程中

                break;

            default:
                Log.e(TAG, "what a fatal error!");
                break;
        }
    }


    /**
     * 获取RefWatcher
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }



    /**
     * 检查APP 是不是调试模式
     *
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 检查是不是Debug 模式
     */
    private void isDebugCheck() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    this.getPackageName(), PackageManager.GET_META_DATA);
            isDebug = info.applicationInfo.metaData.getBoolean("APP_DEBUG");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
