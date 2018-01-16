package com.zenglb.framework.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zenglb.framework.MyApplication;
import com.zlb.httplib.core.HttpUiTips;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Base Fragment,实现懒加载，一般的主页面的要求实现懒加载
 * <p>
 * Created by zenglb on 2017/1/5.
 */
public abstract class BaseFragment extends RxFragment implements HasSupportFragmentInjector {
    private String TAG = BaseFragment.class.getSimpleName();
    //保证Fragment即使在onDetach后，仍持有Activity的引用（有引起内存泄露的风险，但是相比空指针闪退，这种做法“安全”些）
    protected Activity mActivity;          //防止getActivity()== null


    /**
     * 会先于onCreate 和onCreateView 执行
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) { //如果是可见
//            visibleTime++;
//            onVisible();
//        } else {
//            onInvisible();
//        }
    }

    /**
     * 一定要super，放在最后面的一行代码来Super!
     *
     */
    @CallSuper
    protected void initViews(View rootView) {
//        isViewsInit = true;
    }

//    /**
//     * 选择性的实现懒加载方案，不是所有的Fragment 都需要懒加载的
//     */
//    protected abstract void lazyLoadData(boolean isForceLoad);

//    /**
//     * Fragment 可见的时候调用尝试调用加载数据，
//     */
//    protected void onVisible() {
//        lazyLoadData(false);
//    }
//
//    /**
//     * Fragment 不可见的时候调用，选择性的使用，可以基本不用
//     */
//    protected void onInvisible() {
//
//    }

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public void onAttach(Context context) {
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
        HttpUiTips.dismissDialog(getActivity());  // 非常的重要呢！！
    }



}
