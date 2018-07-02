package com.zlb.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zlb.httplib.HttpUiTips;
import com.zlb.httplib.R;

/**
 * [FBI WARMING] 不要为了方便，只有某几个Activity 才会用的（定位，Wi-Fi 数据收集啊，写在Base里面，那还抽象什么）
 * 基类就只做基类的事情,不要把业务层面的代码写到这里来
 *
 *
 * 1.toolbar 的处理封装
 * 2.增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
 *
 * @author zenglb 20170301
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Toolbar mToolbar;
    public Context mContext;

    public LoadService mBaseLoadService; //Http Error，empty,Loading,timeout状态管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;

        View rootView=customContentView(View.inflate(this, R.layout.activity_base, null));
        setContentView(rootView);

        initViews();
        loadHttp();  //在这里进行Http 的请求
    }


    /**
     * 定制Custom View，Content 区域先留空，后面再动态的添加，同时
     * 增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
     *
     */
    private View customContentView(View rootView) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        View content = View.inflate(this, getLayoutId(), null);
        if (content != null) {
            FrameLayout flContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            flContent.addView(content, params);
//            ButterKnife.bind(this, rootView);   //ButterKnife 绑定,只要在这一处地方写好就可以了

            //增加Error，empty,Loading,timeout,等通用的场景处理
            mBaseLoadService = LoadSir.getDefault().register(content, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    onHttpReload(v);
                }
            });
        }
        return rootView;
    }


    /**
     * 点击按钮的监听
     * @param view
     */
    public void onClick(View view) {

    }

    /**
     * 如果没有重写，说明那个页面不需要Http 请求，直接是成功
     *
     */
    protected  void loadHttp(){
        mBaseLoadService.showSuccess();
    }

    /**
     * Http 请求的重新加载
     */
    protected  void onHttpReload(View v){
    }

    protected abstract int getLayoutId(); //获取相应的布局啊

    protected abstract void initViews();

    /*
     * Activity的跳转，太简单了
     *
	 */
    public final void startActivity(Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(this, cla);
        startActivity(intent);
    }


    /**
     * Activity -> webview Activity的跳转-带参数
     *
     * @param link
     */
    public final  void goWebView(String link) {
//        Intent intent = new Intent(this, WebActivity.class);
//        intent.putExtra(BaseWebViewActivity.URL, link);
//        startActivity(intent);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * Get toolbar
     *
     * @return support.v7.widget.Toolbar.
     */
    public final Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        getToolbar().setTitle(title);
        setSupportActionBar(getToolbar());
    }


    /**
     * 设置ToolBar 是否是可见的，默认是可见的
     *
     * @param visible
     */
    public void setToolBarVisible(int visible) {
        getToolbar().setVisibility(visible);
        setSupportActionBar(getToolbar());
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack() {
        getToolbar().setNavigationIcon(R.drawable.ic_back_copy);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //返回事件
            }
        });
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
    }

    /**
     * 防止内存泄漏需要：
     *
     * 1.移除消息队列中MessageQueue 中的所有的消息
     * 2.监听器注册的取消
     * 3.停止异步任务
     * 4.静态的变量置 null
     * 5.
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUiTips.dismissDialog(mContext);  // 非常的重要呢！！
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
