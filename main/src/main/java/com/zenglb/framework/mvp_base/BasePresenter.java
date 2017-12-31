package com.zenglb.framework.mvp_base;

import android.support.annotation.NonNull;
import android.util.Log;
import java.lang.ref.WeakReference;


/**
 * 抽象出P 的共性，我们可以看见 View 和 Module 之间的关联是通过在P 中完成的。
 * <p>
 * getIView 就能得到View
 * getIModel 就能得到Model
 * <p>
 * <p>
 * public abstract class BasePresenter<M, V>
 * <p>
 * Created by zlb on 2017/8/20.
 */

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    /**
     * 使用弱引用是为了mViewRef 在需要被回收的时候被回收，不要Activity被关闭了他被强引用了得不到回收内存泄漏
     *
     */
    private WeakReference<V> mViewRef;            // View接口类型的弱引用
    private WeakReference<M> mModelRef;           // 其实根本没有必要


    /**
     * @param model
     * @param view
     */
    @Override
    public void attachModelAndView(IModel model, IView view) {
        mViewRef = new WeakReference(view);
        mModelRef = new WeakReference(model);
    }

    /**
     * 解除关联
     */
    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 获取View层（Activity／Fragment 等），有可能会已经销毁了
     *
     * 在MVP模式 中我们使用网络异步请求数据成功后需要更新UI 中的显示，但是你不知道对应的Activity／Fragment 等处于什么状态，
     * 此时当我们的异步调用面对无法预知的用户操作和系统处理的时候，什么问题都可能发生。

     * 总而言之，由于我们对于UI实时的状态做不到了如指掌，以至于对数据和逻辑的处理就无法尽善尽美。这是类似隐患得不到很好的解决根本原因。
     * 这个时候我们就会参考Google 的最新的项目架构组件https://github.com/googlesamples/android-architecture-components
     *
     * @return
     */
    @NonNull
    @Override
    public V getIView() {
        Log.e("mViewRef.get","----------网络请求异步返回页面是否关闭了？----------"+isAttachView());

        if(mViewRef!=null){
            return mViewRef.get();
        }else{
            return null;
        }
    }


    /**
     * 每次都要这样子判断是不是会很累啊
     *
     *
     * <p>
     * if（isAttachView）{
     * getIView.doSomething;
     * }
     * <p>
     * 能否在getIView 中mViewRef.get() 判断mViewRef = null && mViewRef.get() = null 后
     * 自动的阻止doSomething  的执行，不要每次重复的去判断 ！
     * <p>
     * <p>
     * 问题总是会存在的！试试  Android Architecture Components 吧
     * <p>
     * https://developer.android.com/topic/libraries/architecture/index.html
     *
     * @return
     */
    public boolean isAttachView() {
        return mViewRef != null && mViewRef.get() != null;
    }

//    /**
//     * 把结果返回到页面等等
//     */
//    public void invokeMethod(){
//
//    }


    @Override
    public M getIModel() {
        return mModelRef.get();
    }

}
