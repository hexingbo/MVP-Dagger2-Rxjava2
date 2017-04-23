package com.zenglb.framework.activity.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.jsbridge.JSBridge;
import com.zenglb.baselib.utils.AESHelper;
import com.zenglb.baselib.utils.WaterMarkTextUtil;
import com.zenglb.framework.R;
import com.zenglb.framework.entity.Messages;
import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.core.HttpCallBack;
import com.zenglb.framework.http.core.HttpResponse;
import com.zenglb.framework.http.result.ModulesResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * BaseActivity is bigger
 *
 */
public class DemoActivity extends BaseActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("MainActivity"); //也可以直接在manifest 中设置好
        getToolbar().setOnMenuItemClickListener(onMenuItemClick);


    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        Drawable drawable1 = new BitmapDrawable(WaterMarkTextUtil.drawWaterMark("    张锦鸿    ", this));
        findViewById(R.id.message_txt).setBackground(drawable1);
        mWebView = (WebView) findViewById(com.zenglb.commonlib.R.id.webview);
        setWebViewClient();
        setWebChromeClient();
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.loadUrl("https://www.baidu.com/");
    }

    private void setWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("dd",url);
                return true;
            }

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
                    mWebView.setBackgroundColor(Color.TRANSPARENT);
                    mWebView.setBackground(new BitmapDrawable(WaterMarkTextUtil.drawWaterMark("    张锦鸿    ", DemoActivity.this)));
//                    topLoadingBar.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    /**
     * Test @Query and result is json array, not json object
     * <p>
     * Call<HttpResponse< jsonArray >> ,not Call<HttpResponse< jsonObj >>
     */
    private void getMessages() {
//        Call<HttpResponse<List<Messages>>> getMsgsCall = HttpCall.getApiService().getMessages(0, 20);
//        getMsgsCall.enqueue(new HttpCallBack<List<Messages>>(null) {
//            @Override
//            public void onSuccess(List<Messages> listHttpResponse) {
//                ((TextView) findViewById(R.id.message_txt)).setText(listHttpResponse.toString());
//            }

//            @Override
//            public void onFailure(int code, String messageStr) {
//                super.onFailure(code, messageStr);
//                ((TextView) findViewById(R.id.message_txt)).setText(code + "@@checkMobileCall@@" + messageStr);
//            }
//
//        });
    }


    /**
     * test get http
     */
    private void requestModules() {
        Call<HttpResponse<ModulesResult>> getModulesCall = HttpCall.getApiService().getModules();
        getModulesCall.enqueue(new HttpCallBack<ModulesResult>(this) {
            @Override
            public void onSuccess(ModulesResult getModulesCallResponse) {
//                ((TextView) findViewById(R.id.message_txt)).setText(getModulesCallResponse.toString());
            }

            @Override
            public void onFailure(int code, String failedText) {
                super.onFailure(code, failedText);
            }
        });
    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if (!TextUtils.isEmpty(msg)) {
                Toast.makeText(DemoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }


}
