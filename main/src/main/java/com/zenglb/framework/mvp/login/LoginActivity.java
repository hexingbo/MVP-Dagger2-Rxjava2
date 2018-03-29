package com.zenglb.framework.mvp.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zenglb.framework.R;
import com.zenglb.framework.demo.access.RegisterActivity;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
import com.zenglb.framework.http.HttpRetrofit;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.result.LoginResult;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.persistence.SPDao;
import com.zenglb.framework.persistence.dbmaster.DaoSession;
import com.zlb.httplib.core.HttpUiTips;
import com.zlb.httplib.core.SPKey;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import es.dmoral.toasty.Toasty;

/**
 * 简单的登录页面，简单的MVP 和Dagger demo
 *
 * 【ButterKnife znif的使用】 https://www.jianshu.com/p/94eb3cc1852a
 *
 * <
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
public class LoginActivity extends BaseMVPActivity implements LoginContract.LoginView {
    private boolean isFromLaunch = false; //从哪里跳转来登录页面的

    @Inject
    SPDao spDao;

    @Inject
    DaoSession daoSession;

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.login_btn)
    Button oauthBtn;

    @BindView(R.id.fab_btn)
    FloatingActionButton fabBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginInit();
        //1,从Launcher 页面过来 2，用户主动退出 3，超时或其他页面退出（再次登录要回到那里去）
        isFromLaunch = getIntent().getBooleanExtra("isFromLaunch", false);
        if (!isFromLaunch) {
            logoutCustomComponent();
        }

        setToolBarVisible(View.GONE);  //这里是不需要Toolbar 的
    }

    /**
     * 登录的从新初始化，把数据
     */
    private void loginInit() {
        spDao.saveData(SPKey.KEY_ACCESS_TOKEN, "");
        HttpRetrofit.setToken("");
    }

    /**
     * 集成的IM 等第三方系统需要单独的退出来,因为账号体系不能联动的
     *
     */
    private void logoutCustomComponent() {
//        RongyunIM.logout();
//        Clear Oautoken,在web 页面的时候怎么退出来
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Bind view to the presenter which will signal for the presenter to load the task.
        loginPresenter.takeView(this);
//        HttpUiTips.showDialog(mContext, null);
    }

    @Override
    public void onPause() {
        loginPresenter.dropView();
        super.onPause();
    }

    @Override
    protected void initViews() {
        etUsername.setText(spDao.getData(SPKey.KEY_LAST_ACCOUNT, "", String.class));

        etPassword.setText("hl123456");
        etUsername.setText("13619169002");

    }


    @OnLongClick (R.id.login_btn)
    public boolean mvpLogin2() {
        HttpUiTips.showDialog(this,"测试中");
        return true;
    }

    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     */
    @OnClick(R.id.login_btn)
    public void mvpLogin() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toasty.error(this.getApplicationContext(), "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        //1.需要改进，能否改进为链式写法
        LoginParams loginParams = new LoginParams();
        loginParams.setUsername(userName);
        loginParams.setPassword(password);

        loginPresenter.login(loginParams);
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
        spDao.saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + loginResult.getAccessToken());
        spDao.saveData(SPKey.KEY_REFRESH_TOKEN, loginResult.getRefreshToken());
        spDao.saveData(SPKey.KEY_LAST_ACCOUNT, etUsername.getText().toString().trim());
        HttpRetrofit.setToken(spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class));

        if (isFromLaunch) {
            Intent i2 = new Intent(LoginActivity.this, MainActivityBottomNavi.class);
            startActivity(i2);
            LoginActivity.this.finish();
        } else {//是来自Launcher启动的就跳转到主页面，否则从哪里来就到那里去
            LoginActivity.this.finish();
        }

    }

    /**
     * 跳转到注册®️
     */
    @OnClick(R.id.fab_btn)
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
