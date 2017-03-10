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
//        setURL("http://mp.weixin.qq.com/s/Hhzt0KVYsUyRpz7RYBCqCg");
//        setURL("file:///android_asset/index.html");
//        setURL("http://www.JD.com");
//        setURL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489126386650&di=ea54f5da84bb06ff5f3afc97428b16e6&imgtype=0&src=http%3A%2F%2Fimg05.tooopen.com%2Fimages%2F20150318%2Ftooopen_sy_82837241116.jpg");
        setURL(getIntent().getStringExtra(BaseWebViewActivity.URL));

    }

}
