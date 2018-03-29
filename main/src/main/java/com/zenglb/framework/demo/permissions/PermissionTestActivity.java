package com.zenglb.framework.demo.permissions;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zenglb.framework.R;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;

import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 三种Permission 的测试，Easy,Rx,DisPatcher
 *
 */
//@RuntimePermissions    //PermissionsDispatcher 测试
public class PermissionTestActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    private static final  int RC_CAMERA_AND_LOCATION=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_error_layout);
//        RxPermissionTest();

        //222222 PermissionsDispatcher
//        PermissionTestActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);


        // Button click listener that will request one permission.
        findViewById(R.id.statues).setOnClickListener(v -> easyPermissionTest());




        String OS=android.os.Build.SERIAL;

        WifiManager wm = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        String wlan_mac = wm.getConnectionInfo().getMacAddress();

//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String IMEI = tm.getDeviceId().toString();

        getMac();

    }



    public  String getMac() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return macSerial;
    }



    public  String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public  String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 测试Google 的权限测试
     *
     */
    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void easyPermissionTest(){
        //33333 Google EasyPermissionDispatcher (Google 的权限检查)
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toasty.success(this,"已经有了所有的权限，你可以去做想要做的事情！").show();
            // 已经申请过权限，做想做的事
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, getString(R.string.permission_test),
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }


    // 33333333 EasyPermission
    /**
     * google EasyPermisson  拥抱Google 的大腿，稳定才是第一生产要素，有机会再转为RxPermisson
     *
     * https://segmentfault.com/a/1190000012247350
     *
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 拦截申请权限的请求，交给EasyPermission 去处理吧，传统的实在是太麻烦了
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 这个方法不用谁，太简单了
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode){
            case RC_CAMERA_AND_LOCATION:
                Toast.makeText(this, "已获取WRITE_EXTERNAL_STORAGE权限", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 当用户点击了不再提醒的时候的处理方式
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        switch (requestCode){
            case RC_CAMERA_AND_LOCATION:
                Toast.makeText(this, "拒绝了一些事情", Toast.LENGTH_SHORT).show();

                if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                    new AppSettingsDialog.Builder(this).build().show();
                }
        }
    }



    /**
     * somePermissionPermanentlyDenied
     *
     * @param requestCode
     * @param perms
     */
//    @Override
    public void onPermissionsDenied2(int requestCode, List<String> perms) {
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms){
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2,sb.length(),"");

        switch (requestCode){
            case RC_CAMERA_AND_LOCATION:
                Toast.makeText(this, "已拒绝权限" + perms.get(0), Toast.LENGTH_SHORT).show();
                break;
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问" , Toast.LENGTH_SHORT).show();
            new AppSettingsDialog
                    .Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("是")
                    .setNegativeButton("否")
                    .build()
                    .show();
        }

    }


    /**
     * 测试RX,其实这个是最最简洁的，然而不能使用，那又能怎么办呢？
     *
     *
     */
    void RxPermissionTest(){
        // Must be done during an initialization phase like onCreate
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                        Toasty.success(this,"权限申请--成功").show();
                    } else {
                        // Oups permission denied
                        Toasty.success(this,"权限申请--失败").show();
                    }
                });
    }


//    //PermissionsDispatcher 测试  ，太多的问题没有专门的人去跟踪处理了，
//    @NeedsPermission(Manifest.permission.CAMERA)
//    void showCamera() {
//        Toasty.success(this,"权限申请--有了有了！").show();
//    }
//
//
//    /**
//     *
//     *
//     * @param request
//     */
//    @OnShowRationale(Manifest.permission.CAMERA)
//    void showRationaleForCamera(final PermissionRequest request) {
//        new AlertDialog.Builder(this)
//                .setMessage("你要给我权限啊，不然的话你XXX")
//                .setPositiveButton("允许", (dialog, button) -> request.proceed())
//                .setNegativeButton("不可以", (dialog, button) -> request.cancel())
//                .show();
//    }
//
//    @OnPermissionDenied(Manifest.permission.CAMERA)
//    void showDeniedForCamera() {
//        Toasty.success(this,"权限申请--失败").show();
//    }
//
//
//    @OnNeverAskAgain(Manifest.permission.CAMERA)
//    void showNeverAskForCamera() {
//        Toasty.success(this,"权限申请--不再说话").show();
//    }







}