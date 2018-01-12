package com.zenglb.framework.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.main.AreUSleepFragmentList;
import com.zenglb.framework.fragment.mainfragment.MeProfileFragment;
import com.zenglb.framework.fragment.others.DemoFragment;
import com.zenglb.framework.fragment.rxjava2.Rxjava2DemoFragment;
import com.zenglb.framework.persistence.SPDao;
import com.zenglb.framework.persistence.dbmaster.DaoSession;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

/**
 *
 *
 * 本来挺好的，但是4/5 个bottom navi 的时候 不能定制样式啊，反射XX
 * Sadly, there isn't any way to force enable or disable this behaviour which may not work with every design.
 * It also doesn't allow populating the Bottom Navigation View with more than five items - as per the design spec
 * (it throws an IllegalArgumentException if you try to).
 */
public class MainActivityBottomNavi extends BaseActivity {
    private ViewPager viewPager;
    private MenuItem menuItem;

    @Inject
    DaoSession daoSession;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    setTitle("主页");
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    setTitle("Webview");

                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    setTitle("消息");

                    return true;
                case R.id.navigation_set:
                    viewPager.setCurrentItem(3);
                    setTitle("设置");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main");
        daoSession.toString();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main_bottom_navi;
    }

    public void initViews() {
        getToolbar().setNavigationIcon(null);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setAccessibilityLiveRegion(BottomNavigationView.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("sss",position+" =");
            }

            @Override
            public void onPageSelected(int position) {
                //setTitle(position),
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3); //123456789--97534567
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DemoFragment.newInstance("demo"));
        adapter.addFragment(AreUSleepFragmentList.newInstance("expired"));
        adapter.addFragment(Rxjava2DemoFragment.newInstance("done"));
        adapter.addFragment(MeProfileFragment.newInstance("MeProfile", "333"));

        viewPager.setAdapter(adapter);
    }

    protected boolean isShowBacking() {
        return false;
    }


    /**
     * 快速按2次退出
     *
     */
    private long exitTime = 0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime < 2000) {
                finish();
            } else {
                exitTime = System.currentTimeMillis();
                Toasty.info(this.getApplicationContext(), "再按一次退出！", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event); // 按下其他按钮，调用父类进行默认处理
        }
    }


}
