package com.zenglb.framework.activity.web;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zenglb.commonlib.base.BaseWebViewActivity;
import com.zenglb.commonlib.jsbridge.BridgeImpl;
import com.zenglb.commonlib.jsbridge.Callback;
import com.zenglb.commonlib.jsbridge.JSCallBackCode;

import org.json.JSONObject;

/**
 * 业务逻辑相关的写在这里处理,拍照因为部分4.4 无法响应<type-file>的问题，全部使用native,同时启动一个前台Service
 *
 */
public class WebActivity extends BaseWebViewActivity implements View.OnClickListener {
    // TODO: 2017/3/17 添加一个刷新！
    private static final int REQUEST_PICK_IMAGE = 1001;   //相册选取
    private static final int REQUEST_CAPTURE = 1002;      //拍照
    private static final int REQUEST_PICTURE_CUT = 1003;  //剪裁图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ZXING_REQUEST_CODE:
                    String code = data.getStringExtra("qrcode");
                    Callback callback = BridgeImpl.getCallback(BaseWebViewActivity.ZXING_REQUEST_CODE);
                    if (null != callback) {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("qrcode", code);
                            callback.apply(BridgeImpl.getJSONObject(JSCallBackCode.JS_CALL_BACK_SUCCESS, "扫码成功", object));  //这里回调js 没有任何的意义呀！
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case ZXING_REQUEST_CODE:
                    Callback callback = BridgeImpl.getCallback(BaseWebViewActivity.ZXING_REQUEST_CODE);
                    if (null != callback) {
                        try {  //使用gson 改造一下
                            JSONObject object = new JSONObject();
                            object.put("qrcode", "");
                            callback.apply(BridgeImpl.getJSONObject(JSCallBackCode.SCAN_QR_CODE_CANCEL, "扫码失败", object));  //这里回调js 没有任何的意义呀！
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }


}
