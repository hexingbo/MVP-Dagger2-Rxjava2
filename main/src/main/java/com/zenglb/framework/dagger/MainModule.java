package com.zenglb.framework.dagger;

import android.app.Application;
import android.content.Context;

import com.kingja.loadsir.core.LoadSir;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zenglb.framework.http.ApiService;
import com.zenglb.framework.persistence.SPDao;
import com.zenglb.framework.persistence.dbmaster.DaoMaster;
import com.zenglb.framework.persistence.dbmaster.DaoSession;
import com.zenglb.framework.persistence.dbupdate.MySQLiteOpenHelper;
import com.zenglb.framework.http.HttpRetrofit;
import com.zenglb.framework.status_callback.EmptyCallback;
import com.zenglb.framework.status_callback.ErrorCallback;
import com.zenglb.framework.status_callback.LoadingCallback;
import com.zenglb.framework.status_callback.TimeoutCallback;
import com.zlb.httplib.core.SPKey;
import org.greenrobot.greendao.database.Database;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * 在这里提供全局的并且是唯一的东西，SharedPrefence,DB,HTTP,etc
 * <p>
 * <p>
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Module
public class MainModule {

    public static final boolean ENCRYPTED = false;
    Application mContext;

    /**
     * Module  带有构造方法并且参数被使用的情况下所产生的Component 是没有Create方法的
     *
     * @param mContext
     */
    public MainModule(Application mContext) {
        this.mContext = mContext;
    }


    /***
     * @return
     */
    @Provides
    @Singleton
    Context providemContext() {
        return mContext;
    }


    /**
     * SharedPreferences 保存KEY VALUE 配置信息
     *
     * @return
     */
    @Provides
    @Singleton   //在这加了Singleton 的注解就是单例的了，打出内存地址查看一下
    SPDao provideSPDao() {
        return new SPDao(mContext);
    }


    /***
     * @return
     */
    @Provides
    @Singleton
    RefWatcher provideRefWatcher(Application mContext) {
        return LeakCanary.install(mContext);  //只管主进程的,其他的进程自保吧
    }



    /**
     * 网络访问
     *
     * @return
     */
    @Provides
    @Singleton
    ApiService provideApiService(SPDao spDao,Context mContext){
        return HttpRetrofit.getRetrofit(spDao,mContext).create(ApiService.class);
    }



    /**
     * 增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
     *
     * @return
     */
    @Provides
    @Singleton
    LoadSir provideCommonStatusService(){
        return  new LoadSir.Builder()
//                .addCallback(new LoadingCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new ErrorCallback())
                .addCallback(new TimeoutCallback())
                .build();
    }


    /**
     * 数据库访问的DaoSession,登录的时候切换账号后怎么更换呢？
     * <p>
     * 这个DaoSession 和SP 还不一样
     *
     * @param spDao return
     */
    @Provides
//    @Singleton //这个怎么能够动态的替换呢DB 链接的a
    DaoSession provideDaoSession(SPDao spDao) {
        String account = spDao.getData(SPKey.KEY_LAST_ACCOUNT, "default_error_db", String.class);
        String DBName = ENCRYPTED ? account + "encrypted" : account;
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(mContext, DBName, null);
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

}