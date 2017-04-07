package com.zenglb.framework.activity.zxing;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.framework.R;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 二维码扫描
 * <p>
 * anylife.zlb@gmail.com
 */
@RuntimePermissions
public class ZxingActivity extends BaseActivity {
    private String TAG = ZxingActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            lastText = result.getText();
            beepManager.playBeepSoundAndVibrate();

            Intent data = new Intent();
            data.putExtra("qrcode", result.getText());             //把数据返回给发起的Activity
            setResult(RESULT_OK, data);
            ZxingActivity.this.finish();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("二维码");

        ZxingActivityPermissionsDispatcher.showCameraWithCheck(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initViews() {
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        barcodeView.setStatusText("音量加减按键可以控制手电筒开关");
        beepManager = new BeepManager(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ZxingActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //=============== 下面都是权限管理的===================
    /**
     * 权限被同意了，同意后每次check 都会被调用
     */
    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        Log.e(TAG, "showCamera");
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        Log.e(TAG, "showRationaleForCamera");
        new AlertDialog.Builder(this)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("不给", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("扫码需要摄像头权限，应用将要申请摄像头权限")
                .show();
    }

    /**
     * 权限申请被拒绝了，简单一点关闭页面然后弹出Toast
     */
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        ZxingActivity.this.finish();
        Toast.makeText(ZxingActivity.this,"你拒绝了授权使用此功能",Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Log.e(TAG, "showNeverAskForCamera");
    }


}