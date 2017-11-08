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
import android.widget.Toast;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.quick_input_things.QuickInputThingsActivity;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.mvp_oauth.Oauth_MVP_Activity;
import com.zenglb.framework.navigation.MainActivityBottomNavi;

/**
 * 启动页面的背景图放在不同的目录还会导致内存的占用大小不一样啊
 */
public class LaunchActivity extends BaseActivity {
    private static final int FINISH_LAUNCHER=0;

    private  Handler UiHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case FINISH_LAUNCHER:
                    String accessToken = SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
                    if (TextUtils.isEmpty(accessToken)) {
                        Intent i1 = new Intent();
                        i1.putExtra("isFromLaunch", true);
                        i1.setClass(LaunchActivity.this, Oauth_MVP_Activity.class);
                        startActivity(i1);
                        LaunchActivity.this.finish();
                    } else {
                        Intent i1 = new Intent();
                        i1.setClass(LaunchActivity.this, MainActivityBottomNavi.class);
                        // i1.setClass(LaunchActivity.this, QuickInputThingsActivity.class);
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
        UiHandler.sendEmptyMessageDelayed(0, 2000);

//        Toast.makeText(this,NDKinterface.getAESDecrypt(NDKinterface.getAESEncrypt("如果不是乱码就是成功了")),
//                Toast.LENGTH_LONG).show();     //测试加密解密是否有问题

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UiHandler.removeCallbacksAndMessages(null);
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
