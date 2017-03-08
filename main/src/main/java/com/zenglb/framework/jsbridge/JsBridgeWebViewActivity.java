package com.zenglb.framework.jsbridge;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zenglb.commonlib.base.BaseActivity;
import com.zenglb.framework.R;

/**
 * 包含JSBridge 的WebView
 *
 */
public class JsBridgeWebViewActivity extends BaseActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSBridge.register(JSBridge.exposeClassName, BridgeImpl.class);

    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_jsbridge;
    }

    @Override
    protected void initViews() {
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new JSBridgeWebChromeClient());
        mWebView.loadUrl("file:///android_asset/index.html");

//        mWebView.loadUrl("http://www.csst.com");
    }


    @Override
    protected void onStart() {
        super.onStart();
        getToolbar().setTitle(mWebView.getTitle());
//        getToolbar().setSubtitle(mWebView.getUrl());
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


}
