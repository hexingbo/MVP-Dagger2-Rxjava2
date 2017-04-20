package com.zenglb.framework.activity.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.baselib.utils.AESHelper;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.access.LoginActivity;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.config.SPKey;

import java.io.UnsupportedEncodingException;


/**
 * 启动页面的背景图放在不同的目录还会导致内存的占用大小不一样啊
 */
public class LaunchActivity extends BaseActivity {

    private String TAG = "Rxjava2";

    private Handler UiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String accessToken = SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);

                    if (TextUtils.isEmpty(accessToken)) {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, LoginActivity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    } else {
//                          goWebView("https://www.baidu.com");
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, MainActivityBottomNavi.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    }

                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int setLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiHandler.sendEmptyMessageDelayed(0, 2000); //
        testRxjava();

        String originalText = "宫亭是SB";
        String key = "VankeService1997";


        Toast.makeText(this, "自助加密再解密:" + AESHelper.decrypt(AESHelper.encrypt("爱你哦，F!",key),key), Toast.LENGTH_SHORT).show();

        //1.模拟加密,base 64
        String baseStrEncode = Base64.encodeToString(AESHelper.encrypt(originalText, key).getBytes(), Base64.URL_SAFE);

        //2.解密，解不出？ 重启试试
        String result = null;
        try {
            result = AESHelper.decrypt(new String(Base64.decode(baseStrEncode, Base64.URL_SAFE), "UTF-8"), key);
        } catch (Exception e) {
            Log.e("E", e.toString());
        }


        Toast.makeText(this, "解码结果:" + result, Toast.LENGTH_SHORT).show();
        Log.e("result", result);

    }



    /**
     * 测试Rxjava2 的基本使用
     */
    private void testRxjava() {


//        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
//                Log.d(TAG, "emit 1");
//                emitter.onNext(1);
//            }
//        });
//
//        Consumer<Integer> consumer = new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
//                Log.d(TAG, "onNext: " + integer);
//            }
//        };
//
//        observable.subscribeOn(Schedulers.newThread())     //订阅新的线程中的事件源
//                  .observeOn(AndroidSchedulers.mainThread()) //在主线程中观察，切换到主线程处理问题
//                  .subscribe(consumer);

    }


    /**
     * 获取手机号码，一般获取不到
     * <p>
     * 用到的权限：
     * name="android.permission.READ_PHONE_STATE"
     * <p>
     * 要想获取更多电话、数据、移动网络相关信息请查阅TelephonyManager资料
     */
    public String getLineNum(Context ctx) {
        String strResult = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            strResult = telephonyManager.getLine1Number();
        }
        return strResult;
    }

    /**
     *
     *
     */
    private void getSomeInfo() {
        int cid = -1, lac = -1;
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();
            if (null != location) {
                lac = location.getLac();
                cid = location.getCid();
            }
        } catch (Exception e) {
        }

        try {
            CdmaCellLocation location2 = (CdmaCellLocation) mTelephonyManager.getCellLocation();
            if (null != location2) {
                cid = location2.getBaseStationId();
                lac = location2.getNetworkId();
            }
        } catch (Exception e) {
        }
        Toast.makeText(this, "cid:+" + cid + "  lac:" + lac, Toast.LENGTH_LONG).show();
    }

}
