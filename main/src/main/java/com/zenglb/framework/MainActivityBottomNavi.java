package com.zenglb.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.zenglb.commonlib.base.BaseActivity;

/**
 * 本来挺好的，但是4/5 个bottom navi 的时候 不能定制样式啊
 * Sadly, there isn't any way to force enable or disable this behaviour which may not work with every design.
 * It also doesn't allow populating the Bottom Navigation View with more than five items - as per the design spec
 * (it throws an IllegalArgumentException if you try to).
 */
public class MainActivityBottomNavi extends BaseActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_set:
                    mTextMessage.setText(R.string.action_settings);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main_bottom_navi;
    }

    public void initViews() {
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setAccessibilityLiveRegion(BottomNavigationView.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);
    }

}
