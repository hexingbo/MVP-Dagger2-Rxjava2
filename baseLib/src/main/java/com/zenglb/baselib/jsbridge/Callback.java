package com.zenglb.baselib.jsbridge;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Native 回调JS 注册的callback
 */
public class Callback {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    //格式化一下
    private static final String CALLBACK_JS_FORMAT = "javascript:JSBridge.onFinish('%s', %s);";
    private String mPort;
    private WeakReference<WebView> mWebViewRef;

    public Callback(WebView view, String port) {
        mWebViewRef = new WeakReference<>(view);
        mPort = port;
    }

    public void apply(JSONObject jsonObject) {
        final String execJs = String.format(CALLBACK_JS_FORMAT, mPort, String.valueOf(jsonObject));
        if (mWebViewRef != null && mWebViewRef.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Java要调用js的方法，是非常容易做到的，使用WebView.loadUrl(“JavaScript:function()”)即可
                    mWebViewRef.get().loadUrl(execJs);
                }
            });
        }
    }
}
