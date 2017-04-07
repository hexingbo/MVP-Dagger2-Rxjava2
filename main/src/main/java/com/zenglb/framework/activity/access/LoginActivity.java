package com.zenglb.framework.activity.access;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.core.HttpCallBack;
import com.zenglb.framework.http.core.HttpResponse;
import com.zenglb.framework.http.result.LoginResult;

import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;

/**
 * 1.登录的对话框在弹出键盘的时候希望能够向上移动
 * 2.内存占用实在是太多太多了，太多太多了！
 * 3.在这里把需要申请的权限都申请一遍？ 这样不是都很流氓
 * @author zenglb
 */
//@RuntimePermissions
public class LoginActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        etUsername.setText("18826562075");
        etPassword.setText("zxcv1234");
    }


    /**
     * Login @
     */
    private void login() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpCall.cleanToken();

        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("password");
        loginParams.setUsername(userName);
        loginParams.setPassword(password);

        //2.Generic Programming Techniques is the basis of Android develop
        Call<HttpResponse<LoginResult>> loginCall = HttpCall.getApiService().goLogin(loginParams);
        loginCall.enqueue(new HttpCallBack<HttpResponse<LoginResult>>(this) {
            @Override
            public void onSuccess(HttpResponse<LoginResult> loginResultHttpResponse) {
                SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + loginResultHttpResponse.getResult().getAccessToken());
                SharedPreferencesDao.getInstance().saveData(SPKey.KEY_REFRESH_TOKEN, loginResultHttpResponse.getResult().getRefreshToken());
                SharedPreferencesDao.getInstance().saveData(SPKey.KEY_LAST_ACCOUNT, etUsername.getText().toString().trim());

                Intent i2 = new Intent(LoginActivity.this, MainActivityBottomNavi.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Android 5.0 以下不能使用啊
                    Explode explode = new Explode();
                    explode.setDuration(300);
                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                    startActivity(i2, oc2.toBundle());
                }else{
                    startActivity(i2);
                }
                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int code, String messageStr) {
                super.onFailure(code, messageStr);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                login();
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


}
