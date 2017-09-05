package com.zenglb.framework.dragger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.zenglb.framework.R;

import javax.inject.Inject;

import dagger.Component;

/**
 * Dragger 测试
 *
 */
public class DraggerActivity extends AppCompatActivity {
    @Inject
    AAA aaa;

    @Inject
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragger);

        DaggerAAAComponent.builder().build().inject(this);

        aaa.setA1("hahanah");
        String jsonaaa=gson.toJson(aaa);
        aaa.doSomething(this,jsonaaa);

    }
}
