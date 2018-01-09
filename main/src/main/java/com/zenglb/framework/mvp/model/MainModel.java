package com.zenglb.framework.mvp.model;

import android.util.Log;


import com.zenglb.framework.mvp.contract.MainContract;

import javax.inject.Inject;

/**
 * Created by QingMei on 2017/8/16.
 * desc:
 */

public class MainModel implements MainContract.Model {

    @Inject
    public MainModel() {
        Log.e("AAA","3333333");
    }

    public String returnMessage() {
        return "qingmei2";
    }

}
