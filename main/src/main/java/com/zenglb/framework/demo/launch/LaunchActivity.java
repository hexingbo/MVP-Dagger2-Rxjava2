package com.zenglb.framework.demo.launch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.zenglb.framework.R;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
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
                    //没有登陆过就去指导页面（Guide Page）
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
        UiHandler.sendEmptyMessageDelayed(FINISH_LAUNCHER, 2500);  //测试内存泄漏,只为测试.

        setToolBarVisible(View.GONE);  //这里是不需要Base 中的Toolbar,不要的情况毕竟是少数

        //bg_splash 是很长的图   bg_splash2 是短的图！  测试适配，测试适配
        scaleImage(LaunchActivity.this, findViewById(R.id.launch_img), R.drawable.bg);

//        Toast.makeText(this,NDKinterface.getAESDecrypt(NDKinterface.getAESEncrypt("如果不是乱码就是成功了")),
//                Toast.LENGTH_LONG).show();     //测试加密解密是否有问题
    }


    /**
     * 裁剪Bitmap 适配屏幕,屏幕的上下边或者左右边可能会被裁剪；所以不要放内容在外边的1/6 圈内
     *
     * @param activity
     * @param view
     * @param drawableResId
     */
    public void scaleImage(final Activity activity, final ImageView view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap originBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId, options);

        int originWidth1 = options.outWidth;
        int originHeight1 = options.outHeight;       //和下面的不一样？https://blog.csdn.net/qq_31285265/article/details/48780667

        int originWidth = originBitmap.getWidth();   //不一样 ？？？？options.outWidth
        int originHeight = originBitmap.getHeight();

        //图片比例变换成图片的长（宽）等于屏幕的长（宽），而另外一边不小于屏幕的宽（长）
        if ((float) outSize.y / outSize.x > (float) originHeight / originWidth) {
            //图片的左右两边要截掉
            int cuteWidth = (int) (originWidth - (float) outSize.x * originHeight / outSize.y); //
            view.setImageBitmap(Bitmap.createBitmap(originBitmap, cuteWidth / 2, 0, originWidth - cuteWidth, originHeight));
        } else if ((float) outSize.y / outSize.x < (float) originHeight / originWidth) {
            //图片的上下两边要截掉
            int cuteHeight = (int) (originHeight - (float) outSize.y * originWidth / outSize.x); //
            view.setImageBitmap(Bitmap.createBitmap(originBitmap, 0, cuteHeight / 2, originWidth, originHeight - cuteHeight));
        } else {
            view.setImageResource(drawableResId);
        }

    }


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
