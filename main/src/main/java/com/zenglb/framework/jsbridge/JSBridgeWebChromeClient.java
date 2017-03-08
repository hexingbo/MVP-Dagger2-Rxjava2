package com.zenglb.framework.jsbridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 *
 *
 */
public class JSBridgeWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        String callBackData = JSBridge.callJava(view, message);
        result.confirm(callBackData);
        return true;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);

    }
}
