package com.zenglb.framework.demo.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.zenglb.framework.R;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
import com.zenglb.framework.demo.demo.Rxjava_ZIP_Activity;
import com.zenglb.framework.mvp.login.LoginActivity;
import com.zenglb.framework.mvp.task.TaskMVPActivity;
import com.zenglb.framework.persistence.SPDao;
import com.zlb.httplib.core.SPKey;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import javax.inject.Inject;

/**
 * 启动页，并使所有的UI 的模型都需要MVP，复杂的才用
 *
 *  Created by anylife.zlb@gmail.com on 2017/1/11.
 */
public class LaunchActivity extends BaseMVPActivity {
    private static final int FINISH_LAUNCHER = 0;
    private Handler UiHandler = new MyHandler();

    @Inject
    SPDao spDao;

    /**
     * 接受消息，处理消息 ，此Handler会与当前主线程一块运行
     *
     */
    class MyHandler extends Handler {

        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
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
//                        i1.setClass(LaunchActivity.this, TaskMVPActivity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    } else {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, MainActivityBottomNavi.class);
//                        i1.setClass(LaunchActivity.this, TaskMVPActivity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiHandler.sendEmptyMessageDelayed(0, 2000);  //好假啊

        spDao.toString();  //Android Profile

//        String idCard = "42900417865218093X";
//        System.out.println(idCard.replaceAll("(\\d{6})\\d{8}(\\w{4})","$1********$2"));

//        Toast.makeText(this,NDKinterface.getAESDecrypt(NDKinterface.getAESEncrypt("如果不是乱码就是成功了")),
//                Toast.LENGTH_LONG).show();     //测试加密解密是否有问题
    }


    @Override
    protected int setLayoutId() {
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


}
