package com.zenglb.framework.activity.access;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.http.HttpCall;
import com.zlb.httplib.core.HttpRetrofit;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zlb.httplib.core.SPKey;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.LoginResult;
import com.zlb.httplib.core.BaseObserver;

import es.dmoral.toasty.Toasty;

/**
 * 使用MVP 改造了 {@link com.zenglb.framework.mvp_oauth.Oauth_MVP_Activity}
 *
 * @author anylife.zlb@gmail.com
 */
@Deprecated
public class OauthActivity extends BaseActivity {

    private static final String TAG = OauthActivity.class.getSimpleName();
    private EditText etUsername;
    private EditText etPassword;
    private Button loginBtn;
    private CardView cardview;
    private FloatingActionButton fabBtn;
    private boolean isFromLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1,从Launcher 页面过来 2，用户主动退出 3，超时或其他页面退出（再次登录要回到那里去）
        isFromLaunch = getIntent().getBooleanExtra("isFromLaunch", false);
        if (!isFromLaunch) {

        }
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "");
    }


    /**
     * 集成的IM 等第三方系统需要单独的退出来,因为
     *
     */
    private void logoutCustomTime() {
//        RongyunIM.logout();
//        Clear Oautoken,在web 页面的时候怎么退出来

    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        cardview = (CardView) findViewById(R.id.cardview);
        fabBtn = (FloatingActionButton) findViewById(R.id.fab_btn);

        fabBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        etUsername.setText(SharedPreferencesDao.getInstance().getData(SPKey.KEY_LAST_ACCOUNT, "", String.class));

        etPassword.setText("zxcv1234");
        etUsername.setText("18826562075");
    }


    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     */
    private void loginByRxJava2() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toasty.error(this.getApplicationContext(), "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpRetrofit.setToken("");

        //1.需要改进，能否改进为链式写法
        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("password");

        loginParams.setUsername(userName);
        loginParams.setPassword(password);

        /**
         * 1.两个compose 能否合并起来，或者重写一个操作符
         * 2.这里HttpCall 不能在MVP 模式下很好的使用啊
         *
         */
        HttpCall.getApiService().goLoginByRxjavaObserver(loginParams)
                .compose(SwitchSchedulers.applySchedulers())
                .compose(bindToLifecycle())
                .subscribe(new BaseObserver<LoginResult>(mContext) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginSuccess(loginResult);
                    }
                });

    }

    /**
     * 登陆后的跳转，
     *
     * @param loginResult
     */
    private void loginSuccess(LoginResult loginResult) {
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + loginResult.getAccessToken());
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_REFRESH_TOKEN, loginResult.getRefreshToken());
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_LAST_ACCOUNT, etUsername.getText().toString().trim());
        HttpRetrofit.setToken(SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class));

        if (isFromLaunch) {
            Intent i2 = new Intent(OauthActivity.this, MainActivityBottomNavi.class);
            startActivity(i2);
            OauthActivity.this.finish();
        } else {//是来自Launcher启动的就跳转到主页面，否则从哪里来就到那里去
            OauthActivity.this.finish();
        }

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    //Explode
//                    Explode explode = new Explode();
//                    explode.setDuration(300);

//                    //Slide
//                    Slide slideTracition = new Slide();
//                    slideTracition.setSlideEdge(Gravity.LEFT);
//                    slideTracition.setDuration(500);
//
//                    getWindow().setExitTransition(slideTracition);
//                    getWindow().setEnterTransition(slideTracition);

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fabBtn, fabBtn.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.login_btn:
                loginByRxJava2();
                break;
        }
    }

    /**
     * 登录页面不允许返回，之前返回Home
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();

//        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  am.getRunningTasks(11);
//        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfoList) {
//            Log.e(TAG,"id: " + runningTaskInfo.id);
//            Log.e(TAG,"description: " + runningTaskInfo.description);
//            Log.e(TAG,"number of activities: " + runningTaskInfo.numActivities);
//            Log.e(TAG,"topActivity: " + runningTaskInfo.topActivity);
//            Log.e(TAG,"baseActivity: " + runningTaskInfo.baseActivity.toString()+"\n");
//        }

    }


}
