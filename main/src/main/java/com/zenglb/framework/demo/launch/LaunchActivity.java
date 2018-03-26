package com.zenglb.framework.demo.launch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.zenglb.framework.R;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
import com.zenglb.framework.mvp.handylife.HandyLifeActivity;
import com.zenglb.framework.mvp.login.LoginActivity;
import com.zenglb.framework.persistence.SPDao;
import com.zlb.httplib.core.SPKey;
import com.zenglb.framework.navigation.MainActivityBottomNavi;

import javax.inject.Inject;

/**
 * 启动页，并使所有的UI 的模型都需要MVP，复杂的才用
 * <p>
 * Created by anylife.zlb@gmail.com on 2017/1/11.
 */
public class LaunchActivity extends BaseMVPActivity {
    private static final int FINISH_LAUNCHER = 0;
    private Handler UiHandler = new MyHandler();

    @Inject
    SPDao spDao;

    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行，，只为测试只为测试只为测试只为测试
     */
    class MyHandler extends Handler {
        public MyHandler() {

        }

        // 子类必须重写此方法，接受数据
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_LAUNCHER:
                    String accessToken = spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);

                    if (TextUtils.isEmpty(accessToken)) {
                        Intent i1 = new Intent();
                        i1.putExtra("isFromLaunch", true);
                        i1.setClass(LaunchActivity.this, LoginActivity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    } else {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, MainActivityBottomNavi.class);
//                        i1.setClass(LaunchActivity.this, HandyLifeActivity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }

//    /**
//     * 这种页面没有Http 的Load ，也不需要Toolbar
//     */
//    @Override
//    protected void initHttp() {
//        mBaseLoadService.showSuccess();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 2500);  //测试内存泄漏,只为测试

        setToolBarTitle("It is test");
        setToolBarVisible(View.GONE);  //这里是不需要Toolbar 的

//        Toast.makeText(this,NDKinterface.getAESDecrypt(NDKinterface.getAESEncrypt("如果不是乱码就是成功了")),
//                Toast.LENGTH_LONG).show();     //测试加密解密是否有问题

//        List<String> list = new ArrayList<String>();
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        Test test = new Test();
//        List<String> listTemp = test.m2(list);
    }

    /**
     * 换成注解吧，写的烦死了
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


}
