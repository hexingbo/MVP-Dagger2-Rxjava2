package com.zenglb.framework;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.zenglb.framework.base.BaseApplication;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;

import com.zenglb.framework.database.dbmaster.DaoMaster;
import com.zenglb.framework.database.dbmaster.DaoSession;
import com.zenglb.framework.database.dbupdate.MySQLiteOpenHelper;
import com.zenglb.framework.di.DaggerMyAppComponent;
import com.zenglb.framework.http.ApiService;
import com.zlb.httplib.core.HttpRetrofit;

import org.greenrobot.greendao.database.Database;

/**
 * Thank you, Vanke Service;I have enough time to do my favourite
 *
 * Created by zenglb on 2017/3/15.
 */
public class MyApplication extends BaseApplication {
    public static final String TAG = MyApplication.class.getSimpleName();
    private boolean isDebug = false;  //App 是否是调试模式

    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName();
        Log.d(TAG, processName + "Application onCreate");

        myApplication=this;

        DaggerMyAppComponent.create().inject(this);   //22222222


        // 很多的东西最好能放到一个IntentService 中去初始化
        // InitializeService.start(this);
        isDebugCheck();
        initApplication();
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
            case "com.zenglb.framework":
                SdkManager.initDebugOrRelease(this);
                HttpRetrofit.init(this);
                setDaoSession(SharedPreferencesDao.getInstance().getData("Account", "DefDb", String.class));
                refWatcher = LeakCanary.install(this);  //只管主进程的,其他的进程自保吧

                //创建默认的ImageLoader配置参数
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
                //Initialize ImageLoader with configuration.
                ImageLoader.getInstance().init(configuration);

                break;

            case "com.zenglb.framework:webprocess":

                break;

            default:
                Log.e(TAG, "what a fatal error!");
                break;
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    /**
     * 设置数据库操作对象
     * 1.在Application 中设置一个默认的上传登陆的Session,在登录成功后创建一个新的Session
     *
     */
    public void setDaoSession(String account) {
        if (!TextUtils.isEmpty(account)) {
            String DBName = ENCRYPTED ? account + "encrypted" : account;
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, DBName, null);
            Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
            daoSession = new DaoMaster(db).newSession();
        } else {
            Log.w(TAG, "Account is empty,init db failed");
        }
    }

    /**
     * 获取数据库操作对象
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
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
