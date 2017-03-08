package com.zenglb.framework.activity.WebActivity;

import android.os.Bundle;
import android.view.View;
import com.zenglb.commonlib.base.BaseWebViewActivity;

/**
 * 业务逻辑相关的写在这里处理
 *
 */
public class WebActivity extends BaseWebViewActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setURL("http://mp.weixin.qq.com/s/Hhzt0KVYsUyRpz7RYBCqCg");
//        setURL("file:///android_asset/index.html");

    }

}
