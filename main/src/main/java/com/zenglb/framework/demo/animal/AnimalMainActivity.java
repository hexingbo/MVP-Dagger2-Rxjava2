package com.zenglb.framework.demo.animal;

import android.os.Bundle;
import android.widget.TextView;

import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.R;

/**
 * 动画演示的主页面
 * <p>
 * https://github.com/lgvalle/Material-Animations
 */
public class AnimalMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowAnimations();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_animal_main;
    }

    @Override
    protected void initViews() {
        setToolBarTitle("");
        String  title = getIntent().getExtras().getString("title");
        ((TextView) findViewById(R.id.title)).setText(title);
    }

    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        getWindow().getEnterTransition().setDuration(300);
    }


}
