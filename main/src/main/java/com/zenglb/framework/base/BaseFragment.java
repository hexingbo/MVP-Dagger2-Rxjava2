package com.zenglb.framework.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zenglb.framework.MyApplication;
import com.zlb.httplib.core.HttpUiTips;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Base Fragment , Dagger fragment ok
 *
 * Created by zenglb on 2017/1/5.
 */
@Deprecated
public abstract class BaseFragment extends RxFragment implements HasSupportFragmentInjector {
    //保证Fragment即使在onDetach后，仍持有Activity的引用（有引起内存泄露的风险，但是相比空指针闪退，这种做法“安全”些）
    protected Activity mActivity;          //防止getActivity()== null

    /**
     * 一定要super，放在最后面的一行代码来Super!
     *
     */
    @CallSuper
    protected void initViews(View rootView) {
    }


    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public void onAttach(Context context) {
        //使用的Fragment 是V4 包中的，不然就是AndroidInjection.inject(this)
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUiTips.dismissDialog(getActivity());
    }


}
