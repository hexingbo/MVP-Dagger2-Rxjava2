package com.zenglb.framework.mvp_oauth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.access.RegisterActivity;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.mvp_base.BaseMVPActivity;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.LoginResult;

import es.dmoral.toasty.Toasty;


/**
 * 是不是感觉更加的复杂了，其实不是都要强制使用MVP 的,其他的模式可以extends
 * {@link com.zenglb.baselib.base.BaseActivity}
 * <p>
 * 更多请参考Google【to-do-MVP（TasksDataSource）】，抽象后发现代码很不好写啊
 * extends BaseMVPActivity<OauthPresenter,OauthModel> implements OauthContract.OauthView
 * <p>
 * 这样子写我想理解应该不会有太大的问题，关键是写起来繁琐，我们从来不干重复性的事情，尝试写一个MVP 模版生成器
 * 比如OAUTH 功能，会自动的生产：
 * 1，OauthActivity / OauthFragment  和对应的布局文件
 * 2，OauthContract
 * 3，OauthModel
 * 4，OauthPresenter
 * 5，
 *
 * @author anylife.zlb@gmail.com
 */
public class Oauth_MVP_Activity extends BaseMVPActivity<OauthPresenter, OauthModel> implements OauthContract.OauthView {
    private static final String PW = "zxcv1234";  //FBI WARMING !!!!
    private boolean isFromLaunch = false;         //从哪里跳转来登录页面的

    EditText etUsername, etPassword;
    Button oauthBtn;
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginInit();

        //1,从Launcher 页面过来 2，用户主动退出 3，超时或其他页面退出（再次登录要回到那里去）
        isFromLaunch = getIntent().getBooleanExtra("isFromLaunch", false);
        if (!isFromLaunch) {
            logoutCustomComponent();
        }

    }


    /**
     * 登录的从新初始化，把数据
     */
    private void loginInit() {
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "");
        HttpCall.setToken("");
    }


    /**
     * 集成的IM 等第三方系统需要单独的退出来,因为
     */
    private void logoutCustomComponent() {
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
        oauthBtn = (Button) findViewById(R.id.login_btn);
        fabBtn = (FloatingActionButton) findViewById(R.id.fab_btn);

        fabBtn.setOnClickListener(this);
        oauthBtn.setOnClickListener(this);

        etUsername.setText(SharedPreferencesDao.getInstance().getData(SPKey.KEY_LAST_ACCOUNT, "", String.class));
        etPassword.setText(PW);
        etUsername.setText("18826562075");
    }


    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     */
    public void mvpLogin() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toasty.error(this.getApplicationContext(), "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        //1.需要改进，能否改进为链式写法
        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("password");
        loginParams.setUsername(userName);
        loginParams.setPassword(password);

        mPresenter.login2(loginParams);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_btn:
                goRegister();
                break;
            case R.id.login_btn:
                mvpLogin();
                break;
        }
    }


    @Override
    public void loginFail(String failMsg) {
        Toasty.error(this.getApplicationContext(), "登录失败" + failMsg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 登录成功
     *
     * @param loginResult
     */
    public void loginSuccess(LoginResult loginResult) {
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + loginResult.getAccessToken());
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_REFRESH_TOKEN, loginResult.getRefreshToken());
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_LAST_ACCOUNT, etUsername.getText().toString().trim());
        HttpCall.setToken(SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class));

        if (isFromLaunch) {
            Intent i2 = new Intent(Oauth_MVP_Activity.this, MainActivityBottomNavi.class);
            startActivity(i2);
            Oauth_MVP_Activity.this.finish();
        } else {//是来自Launcher启动的就跳转到主页面，否则从哪里来就到那里去
            Oauth_MVP_Activity.this.finish();
        }

    }

    public void goRegister() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fabBtn, fabBtn.getTransitionName());
            startActivity(new Intent(this, RegisterActivity.class), options.toBundle());

        } else {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


    /**
     * 登录页面不允许返回，之前返回Home
     * <p>
     * App用户在其他设备上登录，原来的设备因为unOauth 弹出到登录页面
     * 竟然按返回键还能回去，假如有缓存敏感信息不是。。。
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


}
