package debug;

import com.kingja.loadsir.core.LoadSir;
import com.squareup.leakcanary.LeakCanary;
import com.zenglb.framework.modulea.dagger.DaggerAModuleComponent;
import com.zlb.commontips.CustomCallback;
import com.zlb.commontips.EmptyCallback;
import com.zlb.commontips.ErrorCallback;
import com.zlb.commontips.LoadingCallback;
import com.zlb.commontips.TimeoutCallback;
import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;


/**
 * Module 调试模式的时候使用，集成模式不会使用
 *
 * Created by anylife.zlb@gmail.com on 2017/3/15.
 */
public class AModuleApplication extends BaseApplication {
    public static final String TAG = AModuleApplication.class.getSimpleName();
    public static final String MAIN_PROCESS_NAME = "com.zenglb.framework.modulea";
    public static final String WEB_PROCESS_NAME = "com.zenglb.framework.modulea:webprocess";


    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }


    @Override
    protected void injectApp() {
        DaggerAModuleComponent.builder()
                .baseGlobalModule(new BaseGlobalModule(this))
                .build()
                .inject(this);
    }


    /**
     * 需要重新规划
     *
     */
    private void initApplication() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //部分 初始化服务最好能新开一个IntentService 去处理,bugly 在两个进程都有初始化
        String processName = getProcessName();
        switch (processName) {

            case MAIN_PROCESS_NAME:
                //UI status Builder
                LoadSir.beginBuilder()
                        .addCallback(new ErrorCallback())      //添加各种状态页
                        .addCallback(new EmptyCallback())
                        .addCallback(new LoadingCallback())
                        .addCallback(new TimeoutCallback())
                        .addCallback(new CustomCallback())
                        .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                        .commit();

                break;

            case WEB_PROCESS_NAME:  //WebView 在单独的进程中

                break;

        }
    }


}
