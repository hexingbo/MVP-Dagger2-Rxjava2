package com.zenglb.framework.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;


import com.zenglb.framework.R;
import com.zenglb.framework.persistence.SPDao;
import com.zenglb.framework.mvp_base.old.BaseMVPActivity;

import javax.inject.Inject;

/**
 * Created by QingMei on 2017/7/31.
 * desc:
 */
@Deprecated
public class SecondActivity extends BaseMVPActivity {

    @Inject
    String className;

    @Inject
    SPDao spDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = (TextView) findViewById(R.id.tv_content);
        tv.setText(className+spDao.get("ALLISOVER","可以使用吗？全局的那个啥啥啥"));
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void initViews() {

    }


}
