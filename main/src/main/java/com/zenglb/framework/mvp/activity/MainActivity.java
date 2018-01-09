package com.zenglb.framework.mvp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zenglb.framework.R;
import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.mvp.Student;
import com.zenglb.framework.mvp.contract.MainContract;
import com.zenglb.framework.mvp.presenter.MainPresenter;
import com.zenglb.framework.mvp_base.BaseMVPActivity;
import com.zenglb.framework.mvp_more.MVPActivity;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by QingMei on 2017/7/28.
 * desc:
 */

public class MainActivity extends BaseMVPActivity implements MainContract.View {

    @Inject
    String className;

    @Inject
    SharedPreferences sp;

    @Inject
    MainPresenter presenter;

    @Inject
    Student s1;

    @Inject
    Student s2;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        tvContent.setText(className + "\n" +
                s2.toString() + "\n" +
                s1.toString());
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

    }

    public void gotoSecond(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void requestHttp(View view) {
        presenter.requestHttp();
    }

    public void onGetMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
