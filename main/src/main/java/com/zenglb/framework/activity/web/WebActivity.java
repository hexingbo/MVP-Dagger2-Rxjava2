package com.zenglb.framework.activity.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.View;

import com.zenglb.baselib.base.BaseWebViewActivity;
import com.zenglb.baselib.jsbridge.BridgeImpl;
import com.zenglb.baselib.jsbridge.Callback;
import com.zenglb.baselib.jsbridge.JSCallBackStatusCode;
import com.zenglb.baselib.utils.BitMapUtil;
import com.zenglb.baselib.utils.FileCachePathConfig;
import com.zenglb.baselib.utils.FileStorage;
import com.zenglb.framework.service.ForegroundService;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * WIP :work in Process
 *
 *  4.4 的手机有毛病 ！再也不怕拍照问题了，fuck them
 *
 * 业务逻辑相关的写在这里处理,拍照因为部分4.4 无法响应<type-file>的问题，全部使用native,同时启动一个前台Service
 */
public class WebActivity extends BaseWebViewActivity implements View.OnClickListener {
    private final static int REQUEST_CAPTURE_IMG = 1001;   //相册选取
    private final static int REQUEST_PICK_IMAGE  = 1002;   //拍照问题见issue
    private final static int REQUEST_PICTURE_CUT = 1003;   //剪裁图片
    private Uri imageUri;
    private CallNewActForResultReceiver callNewActForResultReceiver = null;

    /**
     * JS call Native 方法的时候并不是都能在那个方法马上执行获取数据
     * 比如拍照和二维码需要启动一个新的Activity 才能获取到需要的数据
     * 这个时候就发广播过来处理把，也许会有更好的方法，但是我不知道！
     */
    public class CallNewActForResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra("code", -1);
            switch (code) {
                case ZXING_REQUEST_CODE:
                    Intent zxingIntent = new Intent();
                    zxingIntent.setAction(BaseWebViewActivity.SCANQR_ACTION);
                    zxingIntent.addCategory(BaseWebViewActivity.SCANQR_CATEGORY);
                    WebActivity.this.startActivityForResult(zxingIntent, BaseWebViewActivity.ZXING_REQUEST_CODE);
                    break;
                case GET_IMG_REQUEST_CODE:
                    openCamera();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra(BaseWebViewActivity.URL);
        setURL(url);

        //动态注册，在当前activity的生命周期內运行
        IntentFilter filter = new IntentFilter(BridgeImpl.fliterTAG);
        callNewActForResultReceiver = new CallNewActForResultReceiver();
        registerReceiver(callNewActForResultReceiver, filter);

        Intent startIntent = new Intent(mContext, ForegroundService.class);
        startIntent.setAction(ForegroundService.START_FOREGROUND_ACTION);
        startService(startIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE_IMG://
                    backImgToJS(BitMapUtil.getSimpleByBelowLine(this, imageUri, 800, 800));
                    break;
                case ZXING_REQUEST_CODE:
                    String code = data.getStringExtra("qrcode");
                    Callback callback = BridgeImpl.getCallback(ZXING_REQUEST_CODE);
                    if (null != callback) {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("qrcode", code);
                            callback.apply(BridgeImpl.returnJSONObject(JSCallBackStatusCode.JS_CALL_BACK_SUCCESS, "扫码成功", object));  //这里回调js 没有任何的意义呀！
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case ZXING_REQUEST_CODE:
                    Callback callback = BridgeImpl.getCallback(ZXING_REQUEST_CODE);
                    if (null != callback) {
                        try {  //使用gson 改造一下
                            JSONObject object = new JSONObject();
                            object.put("qrcode", "");
                            callback.apply(BridgeImpl.returnJSONObject(JSCallBackStatusCode.SCAN_QR_CODE_CANCEL, "扫码失败", object));  //这里回调js 没有任何的意义呀！
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 把图片等信息回传给JS
     */
    private void backImgToJS(Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //如果图片大于100k,压缩到50%质量
        if (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }

        Callback callback = BridgeImpl.getCallback(GET_IMG_REQUEST_CODE);
        if (null != callback) {
            try {
                JSONObject object = new JSONObject();
                object.put("imgData", "data:image/jpg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
                callback.apply(BridgeImpl.returnJSONObject(JSCallBackStatusCode.JS_CALL_BACK_SUCCESS, "拍照成功 ！", object));  //这里回调js 没有任何的意义呀！
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 这不是最好的拍照方案
     *
     */
    private void openCamera() {
        File file = new FileStorage(FileCachePathConfig.CACHE_IMAGE_CHILD).createTempFile("tempTake.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(WebActivity.this, "com.zenglb.framework.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);          //设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);         //将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE_IMG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != callNewActForResultReceiver) {
            unregisterReceiver(callNewActForResultReceiver);
        }
        Intent stopIntent = new Intent(mContext, ForegroundService.class);
        stopIntent.setAction(ForegroundService.STOP_FOREGROUND_ACTION);
        startService(stopIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Intent startIntent = new Intent(mContext, ForegroundService.class);
//        startIntent.setAction(ForegroundService.START_FOREGROUND_ACTION);
//        startService(startIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
