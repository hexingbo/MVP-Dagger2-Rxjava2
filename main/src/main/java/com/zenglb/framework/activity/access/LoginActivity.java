package com.zenglb.framework.activity.access;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.http.core.HttpCallBack;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.http.param.LoginParams;
import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.result.LoginResult;
import com.zenglb.framework.rxhttp.BaseObserver;
import com.zenglb.framework.rxhttp.RxObservableUtils;


/**
 * 1.登录的对话框在弹出键盘的时候希望能够向上移动
 * 2.内存占用实在是太多太多了，太多太多了！
 *
 * @author zenglb
 */
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

//        cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("ddd","ddddddddd");
//            }
//        });

        cv.setOnClickListener(v -> Log.e("ddd", "ddddddddd"));
    }

    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     */
    private void loginByRxJava2() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请完整输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpCall.cleanToken();

        //1.需要改进，能否改进为链式写法
        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("password");
        loginParams.setUsername(userName);
        loginParams.setPassword(password);

        /**
         * 使用compose 来处理统一的线程切换问题
         *
         *
         在subscribe()之后， Observable会持有 Subscriber的引用，这个引用如果不能及时被释放，将有内存泄露的风险。
         所以最好保持一个原则：要在不再使用的时候尽快在合适的地方（例如 onPause()、onStop()等方法中）
         调用 unsubscribe()来解除引用关系，以避免内存泄露的发生。

         */
//        HttpCall.getApiService().goLoginByRxjavaFlowable(loginParams)
//                .compose(RxSubscriberUtils.rxNetThreadHelper())
//                .compose(bindToLifecycle())
//                .subscribe(new BaseSubscriber<LoginResult>(this, true) {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        loginSuccess(loginResult);
//                    }
//                });


                HttpCall.getApiService().goLoginByRxjavaObserver(loginParams)
                .compose(RxObservableUtils.applySchedulers())
                .compose(bindToLifecycle())
                .subscribe(new BaseObserver<LoginResult>(this, true) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginSuccess(loginResult);
                    }
                });


    }


    /**
     * Login ,普通的登录和使用Rxjava 的方式都可以
     */
    private void loginByRetrofit() {
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
        HttpCall.getApiService().goLoginByRetrofit(loginParams)
                .enqueue(new HttpCallBack<LoginResult>(this) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginSuccess(loginResult);
                    }

                    @Override
                    public void onFailure(int code, String messageStr) {
                        super.onFailure(code, messageStr);
                    }
                });
    }

    private void loginSuccess(LoginResult loginResult) {
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, "Bearer " + loginResult.getAccessToken());
        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_REFRESH_TOKEN, loginResult.getRefreshToken());
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
        } else {
            startActivity(i2);
        }
        LoginActivity.this.finish();

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
//                loginByRetrofit();
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

}
