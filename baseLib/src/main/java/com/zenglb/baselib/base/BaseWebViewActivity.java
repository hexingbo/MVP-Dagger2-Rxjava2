package com.zenglb.baselib.base;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.liaoinstan.springview.widget.SpringView;
import com.zenglb.baselib.jsbridge.BridgeImpl;
import com.zenglb.baselib.jsbridge.JSBridge;
import com.zenglb.commonlib.R;

/**
 * 包含JSBridge 的WebView,不要写没有通用性的业务代码在这里
 */
public abstract class BaseWebViewActivity extends BaseActivity {
    public static final String WEB_ACTION = "my.intent.action.GOTOWEB";
    public static final String WEB_CATEGORY = "my.intent.category.WEB";
    public static final String SCANQR_ACTION = "my.intent.action.GOSCANQR";
    public static final String SCANQR_CATEGORY = "my.intent.category.SCANQR";

    public final static int ZXING_REQUEST_CODE   = 101;    //扫码
    public final static int GET_IMG_REQUEST_CODE = 102;    //读取照片

    public static final String URL   = "url";//网页url
    public static final String TITLE = "title";//标题内容

    private WebView mWebView;
    private ProgressBar topLoadingBar;
    private SpringView springView;
    protected String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.web_base_title);
        JSBridge.register(JSBridge.exposeClassName, BridgeImpl.class);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_jsbridge;
    }

    @Override
    protected void initViews() {
        topLoadingBar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        //Android 4.4 开始，默认的浏览器已经是 chrome 了，所以 webview 也是 chrome 了，这就给了 webview 远程调试的能力。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }
        springView = (SpringView) findViewById(R.id.springview);
        springView.setType(SpringView.Type.OVERLAP);
        // TODO: 2017/3/17  还有冲突,滑动不行啊
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mWebView.loadUrl(url);
            }

            @Override
            public void onLoadmore() {
            }
        });
        WebSettings settings = mWebView.getSettings();




//        //支持获取手势焦点，输入用户名、密码或其他
//        mWebView.requestFocusFromTouch();

//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH); //提高渲染的优先级

        //设置自适应屏幕，两者合用
//        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//        settings.settingssetTextZoom(2);//设置文本的缩放倍数，默认为 100

//        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//        setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        supportMultipleWindows(); //多窗口
//        setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        setAllowFileAccess(true); //设置可以访问文件
//        setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        setLoadsImagesAutomatically(true); //支持自动加载图片
//        setDefaultTextEncodingName("utf-8");//设置编码格式
//        settings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
//        settings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8


        settings.setJavaScriptEnabled(true);
        //手动设置UA,让运营商劫持DNS的浏览器广告不生效 http://my.oschina.net/zxcholmes/blog/596192
        settings.setUserAgentString("suijishu" + "#" + settings.getUserAgentString() + "0123456789101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899100101102103104105106107108109110111112113114115116117118119120121122123124125126127128129130131132133134135136137138139140141142143144145146147148149150");
        setWebViewClient();
        setWebChromeClient();

        System.gc();
    }

    /**
     * @param url
     */
    public void setURL(String url) {
        mWebView.loadUrl(url);
    }

    private void setWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                String message = "SSL证书错误";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "不是可信任的证书颁发机构。";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "证书已过期。";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "证书的主机名不匹配。";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "证书还没有生效。";
                        break;
                }
                message += " 你想要继续吗？";
                builder.setTitle("SSL证书错误");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

    }

    private void setWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                String callBackData = JSBridge.callJavaNative(view, message);

                result.confirm(callBackData);
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getToolbar().setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    topLoadingBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == topLoadingBar.getVisibility()) {
                        topLoadingBar.setVisibility(View.VISIBLE);
                    }
                    topLoadingBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        getToolbar().setSubtitle(mWebView.getUrl());
//        getToolbar().setNavigationIcon(R.drawable.ic_launcher);
    }


    /**
     * 允许webview 内部返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 结束后把Webview 所在的进程killed ,所有的手机都OK吗？没有测试哦
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);        //这样会不会好一点！
    }



}
