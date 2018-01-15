package com.zenglb.framework.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zenglb.framework.base.BaseDIActivity;

/**
 * 需要改写！！！！！！！ BaseDI 的部分功能放进来会不会好点？
 *
 *
 *
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BaseMVPActivityNEW extends BaseDIActivity   {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCommonData();
    }


    /**
     * V 和 M 在P 中关联起来了，V 要获取M 的数据什么的都是通过P 来获取的
     */
    private void initCommonData() {

    }

    /**
     * 取消Presenter 和 View 的关联
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
