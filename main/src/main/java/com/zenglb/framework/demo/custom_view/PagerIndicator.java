package com.zenglb.framework.demo.custom_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zenglb.framework.R;

/**
 * 指示器 PagerIndicator，圆角矩形指示器；UI 效果大于一切
 *
 */
public class PagerIndicator extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private ViewPager mViewpager;
    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;

    private int mIndicatorBackgroundColorId = Color.BLACK;
    private int mIndicatorSelectedBackgroundColorId = Color.WHITE;

    private Context mContext;

    public PagerIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        handleTypedArray(context, attrs);
        checkIndicatorConfig(context);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        mIndicatorWidth =
                typedArray.getDimensionPixelSize(R.styleable.PagerIndicator_ci_width, -1);
        mIndicatorHeight =
                typedArray.getDimensionPixelSize(R.styleable.PagerIndicator_ci_height, -1);
        mIndicatorMargin =
                typedArray.getDimensionPixelSize(R.styleable.PagerIndicator_ci_margin, -1);

        mIndicatorBackgroundColorId =
                typedArray.getResourceId(R.styleable.PagerIndicator_ci_color,
                        Color.BLACK);

        mIndicatorSelectedBackgroundColorId =
                typedArray.getResourceId(R.styleable.PagerIndicator_ci_color_selected,
                        Color.WHITE);

//        int orientation = typedArray.getInt(R.styleable.PagerIndicator_ci_orientation, -1);
//        setOrientation(orientation == VERTICAL ? VERTICAL : HORIZONTAL);

        typedArray.recycle();
    }


    private void checkIndicatorConfig(Context context) {
        mIndicatorWidth = (mIndicatorWidth < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;
    }


    public void setViewPager(ViewPager viewPager) {
        mViewpager = viewPager;
        if (mViewpager != null && mViewpager.getAdapter() != null) {
            createIndicators();
            mViewpager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewpager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewpager.getCurrentItem());
        }
    }

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mViewpager.getAdapter() == null || mViewpager.getAdapter().getCount() <= 0) {
                return;
            }
            createIndicators();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     * 变换圆角边，两个联合起来一起弄
     */
    private void createIndicators() {
        removeAllViews();
        int count = mViewpager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }
        int currentItem = mViewpager.getCurrentItem();

        ViewProperty viewProperty = new ViewProperty(count, mIndicatorWidth, mIndicatorHeight, currentItem, mIndicatorBackgroundColorId, mIndicatorSelectedBackgroundColorId);

        addView(new MyPagerIndicatorView(mContext, viewProperty), mIndicatorWidth * 4, mIndicatorHeight);
    }


    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 合成圆角的矩形View
     */
    public class MyPagerIndicatorView extends View {
        ViewProperty viewProperty;
        Paint paint;

        public MyPagerIndicatorView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyPagerIndicatorView(Context context, ViewProperty viewProperty) {
            super(context);
            this.viewProperty = viewProperty;
        }

        /**
         * 画的东西比较少，简单的处理吧
         *
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paint = new Paint();
            paint.setColor(getResources().getColor(viewProperty.backColorRes));

            // 绘制背景
            RectF rectFBack = new RectF(0, 0, viewProperty.width * viewProperty.size, viewProperty.height);
            canvas.drawRoundRect(rectFBack, viewProperty.height / 2, viewProperty.height / 2, paint);

            // 绘制前景
            paint.setColor(getResources().getColor(viewProperty.selectedColorRes));
            int left = viewProperty.current * viewProperty.width;
            RectF rectFSelected = new RectF(left, 0, viewProperty.width + left, viewProperty.height);
            canvas.drawRoundRect(rectFSelected, viewProperty.height / 2, viewProperty.height / 2, paint);
        }

    }

    /**
     * 指示器的属性
     */
    public class ViewProperty {
        int size;
        int width;
        int height;
        int current;
        int backColorRes;
        int selectedColorRes;

        public ViewProperty(int size, int width, int height, int current, int backColorRes, int selectedColorRes) {
            this.size = size;
            this.width = width;
            this.height = height;
            this.current = current;
            this.backColorRes = backColorRes;
            this.selectedColorRes = selectedColorRes;
        }
    }


}