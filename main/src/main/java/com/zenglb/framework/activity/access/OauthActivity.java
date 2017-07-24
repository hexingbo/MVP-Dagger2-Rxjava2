package com.zenglb.framework.activity.access;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.rxUtils.RxObservableUtils;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;
import com.zenglb.framework.rxhttp.BaseObserver;

/**
 * 1.修复Http请求时候Dialog 导致的内存泄漏
 * 2.练习共享元素动画
 * 3.修改LaunchMode 配置
 * <p>
 * <p>
 * <p>
 * 需要区分是从Launcher 跳转过来的还是 其他地方unOauth 导致跳转过来的
 * <p>
 * <p>
 * 1.登录的对话框在弹出键盘的时候希望能够向上移动
 * 2.内存占用实在是太多太多了，太多太多了！
 *
 * @author zenglb
 */
public class OauthActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;
    private boolean isFromLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromLaunch = getIntent().getBooleanExtra("isFromLaunch", false);
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btGo = (Button) findViewById(R.id.bt_go);
        cv = (CardView) findViewById(R.id.cv);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(this);
        btGo.setOnClickListener(this);
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
            Toast.makeText(this.getApplicationContext(), "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpCall.setToken("");

        //1.需要改进，能否改进为链式写法
        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("password");

        loginParams.setUsername(userName);
        loginParams.setPassword(password);

        HttpCall.getApiService().goLoginByRxjavaObserver(loginParams)
                .compose(RxObservableUtils.applySchedulers())
                .compose(bindToLifecycle()) //两个compose 能否合并起来，或者重写一个操作符
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
        HttpCall.setToken(SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class));

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
//
//                    //Slide
//                    Slide slideTracition = new Slide();
//                    slideTracition.setSlideEdge(Gravity.LEFT);
//                    slideTracition.setDuration(500);
//
//                    getWindow().setExitTransition(slideTracition);
//                    getWindow().setEnterTransition(slideTracition);

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                loginByRxJava2();
                break;
        }
    }

    /**
     * 登录页面不允许返回，就是这样的流氓
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("aaa", "onResume" + this.toString());
    }
}
