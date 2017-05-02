package com.zenglb.framework.activity.ndk;

import android.os.Bundle;
import android.widget.TextView;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.framework.R;

/**
 * 测试NDK的一些东西
 */
public class NDKActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("NDK C++");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_ndk;
    }

    @Override
    protected void initViews() {
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.jni);
        tv.setText(stringFromJNITest());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNITest();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String getHtmlContent(String str);


}
