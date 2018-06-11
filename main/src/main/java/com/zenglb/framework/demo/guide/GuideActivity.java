package com.zenglb.framework.demo.guide;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zenglb.framework.R;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
import com.zenglb.framework.demo.custom_view.PagerIndicator;
import com.zenglb.framework.navigation.MainActivityBottomNavi;
import com.zenglb.framework.persistence.SPDao;
import com.zlb.httplib.core.SPKey;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Guide ViewPager,添加4 个Layout View
 *
 * 我们的适配方案就是这样的： 底图背景   主图（1：1）文字描述（最大16：9）  三个区域分割处理
 *
 * 自定义指示器 PagerIndicator，圆角矩形指示器；UI 效果大于一切
 *
 *
 * Created by anylife.zlb@gmail.com on 2017/1/11.
 */
public class GuideActivity extends BaseMVPActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    List<View> viewList=new ArrayList<>();

    @Inject
    SPDao spDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBarTitle("It is test");
        setToolBarVisible(View.GONE);  //这里是不需要Toolbar 的
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews() {

        viewPager=findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);//设置监听器

        LayoutInflater inflater = LayoutInflater.from(this);

        //四个页面
        View view1 = inflater.inflate(R.layout.layout_guide_1, null);
        View view2 = inflater.inflate(R.layout.layout_guide_1, null);
        View view3 = inflater.inflate(R.layout.layout_guide_1, null);
        View view4 = inflater.inflate(R.layout.layout_guide_4, null);

        //将View加入到集合
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);

        //实例化适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewList);
        viewPager.setAdapter(myPagerAdapter);

        PagerIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }


    /**
     * 隐藏虚拟按键，并且全屏，所有的版本都是全屏
     *
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void goApp(View view){
        spDao.saveData(SPKey.KEY_HAVE_SHOW_GUIDE,true);

        Intent i1 = new Intent(GuideActivity.this, MainActivityBottomNavi.class);
        startActivity(i1);
        GuideActivity.this.finish();
    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }



    /**
     * ViewPager的适配器,引导页面只需要Layout 切换就可以，没有逻辑的
     *
     */
    public class MyPagerAdapter extends PagerAdapter{

        private List<View> viewList;//数据源

        public MyPagerAdapter(List<View> viewList){
            this.viewList = viewList;
        }

        //数据源的数目
        public int getCount() {
            return viewList.size();
        }

        //view是否由对象产生，官方写arg0==arg1即可
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }


        //销毁一个页卡(即ViewPager的一个item)
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        //对应页卡添加上数据
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));//千万别忘记添加到container
            return viewList.get(position);
        }

    }


}
