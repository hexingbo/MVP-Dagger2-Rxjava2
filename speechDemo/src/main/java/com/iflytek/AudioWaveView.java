package com.iflytek;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;


import com.iflytek.voicedemo.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 声音波形的view
 * Created by shuyu on 2016/11/15.
 */

public class AudioWaveView extends View {

    final protected Object mLock = new Object();

    private Context mContext;

    private Bitmap mBitmap, mBackgroundBitmap;

    private Paint mPaint;

    private Paint mViewPaint;

    private Canvas mCanvas = new Canvas();

    private Canvas mBackCanVans = new Canvas();

    private final ArrayList<Short> mRecDataList = new ArrayList<>();

    private drawThread mInnerThread;


    private int mWidthSpecSize;
    private int mHeightSpecSize;
    private int mScale = 1;
    private int mBaseLine;

    private int mOffset = -11;//波形之间线与线的间隔

    private boolean mAlphaByVolume; //是否更具声音大小显示清晰度

    private boolean mIsDraw = true;

    private boolean mDrawBase = true;

    private boolean mDrawReverse = false;//绘制反方向


    private boolean mPause = false;//是否站暂停

    private int mWaveCount = 2;

    private int mWaveColor = Color.WHITE;

    private int mColorPoint = 1;

    private int mPreFFtCurrentFrequency;

    private int mColorChangeFlag;

    private int mColor1 = Color.argb(0xfa, 0x6f, 0xff, 0x81);

    private int mColor2 = Color.argb(0xfa, 0xff, 0xff, 0xff);

    private int mColor3 = Color.argb(0xfa, 0x42, 0xff, 0xff);

    private int mDrawStartOffset = 0;

    private final List<Integer> mVolumeList = new ArrayList<>();

    /**
     * invalidate
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AudioWaveView.this.invalidate();
        }
    };

    public AudioWaveView(Context context) {
        super(context);
        init(context, null);
    }

    public AudioWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AudioWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsDraw = false;
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        if (mBackgroundBitmap != null && !mBackgroundBitmap.isRecycled()) {
            mBackgroundBitmap.recycle();
        }
    }

    public void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (isInEditMode())
            return;

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.waveView);
            mOffset = ta.getInt(R.styleable.waveView_waveOffset, dip2px(context, -11));
            mWaveColor = ta.getColor(R.styleable.waveView_waveColor, Color.WHITE);
            mWaveCount = ta.getInt(R.styleable.waveView_waveCount, 2);
            ta.recycle();
        }

        if (mOffset == dip2px(context, -11)) {
            mOffset = dip2px(context, 1);
        }

        if (mWaveCount < 1) {
            mWaveCount = 1;
        } else if (mWaveCount > 2) {
            mWaveCount = 2;
        }

        mPaint = new Paint();
        mViewPaint = new Paint();
        mPaint.setColor(mWaveColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        createBackGroundBitmap();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && mBackgroundBitmap == null) {
            createBackGroundBitmap();
        }
    }


    private void createBackGroundBitmap() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getWidth() > 0 && getHeight() > 0) {
                    mWidthSpecSize = getWidth();
                    mHeightSpecSize = getHeight();
                    mBaseLine = mHeightSpecSize / 2;
                    mBackgroundBitmap = Bitmap.createBitmap(mWidthSpecSize, mHeightSpecSize, Bitmap.Config.ARGB_8888);
                    mBitmap = Bitmap.createBitmap(mWidthSpecSize, mHeightSpecSize, Bitmap.Config.ARGB_8888);
                    mBackCanVans.setBitmap(mBackgroundBitmap);
                    mCanvas.setBitmap(mBitmap);
                    ViewTreeObserver vto = getViewTreeObserver();
                    vto.removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    int offset = 22;

    /**
     * 修改为根据音量的大小来绘制波纹
     * 但是要考虑音量是非线性的，最好改为非线性的
     */
    private class drawThread extends Thread {
        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            while (mIsDraw) {

                List<Integer> myVolumeList = new ArrayList<>();
                synchronized (mVolumeList) {
                    if (mVolumeList.size() != 0) {
                        try {
                            myVolumeList = (List<Integer>) deepCopy(mVolumeList);// 保存  接收数据
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }

                if (mBackgroundBitmap == null) {
                    continue;
                }

                if (!mPause) {

                    if (mBackCanVans != null) {
                        mBackCanVans.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        int startPosition = (mDrawReverse) ? mWidthSpecSize - mDrawStartOffset : mDrawStartOffset;

                        mBackCanVans.drawLine(startPosition, mBaseLine, mWidthSpecSize, mBaseLine, mPaint);

                        offset = 2;  //绘制的偏移量
                        //======== 从下面开始使用真实的音量来画东西=======

                        int size = myVolumeList.size();

                        int maxVolume = 1;  //已经出现的最大的声音
                        for (int i = 0; i < size; i++) {
                            int x = myVolumeList.get(i); //取出声音大小的值

                            if (x > maxVolume) {
                                maxVolume = x;
                            }
                        }

                        int maxAbsY = (mHeightSpecSize - mBaseLine);

                        for (int i = 0; i < size; i++) {

                            offset = offset + 5;
                            int j = offset;

                            int x = myVolumeList.get(i); //取出声音值

                            x = (maxAbsY * 4 * x) / (5 * maxVolume); //按照比例从新的赋值

                            if(x==0) x=1;

                            int max = mBaseLine + x;
                            int min = mBaseLine - x;

                            mBackCanVans.drawLine(j, mBaseLine, j, max, mPaint);
                            mBackCanVans.drawLine(j, min, j, mBaseLine, mPaint);

//                            Log.e("AAA", "min" + min + "     j: " + j + "    i:" + i);
                        }

                        //======== 从下面开始使用真实的音量来画东西=======

                        synchronized (mLock) {
                            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            mCanvas.drawBitmap(mBackgroundBitmap, 0, 0, mPaint);
                        }

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }
                //休眠暂停资源
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * deepClone to avoid ConcurrentModificationException
     *
     * @param src list
     * @return dest
     */
    public List deepCopy(List src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List dest = (List) in.readObject();
        return dest;
    }


    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (mIsDraw && mBitmap != null) {
            synchronized (mLock) {
                c.drawBitmap(mBitmap, 0, 0, mViewPaint);
            }
        }
    }


    /**
     * 更具当前块数据来判断缩放音频显示的比例
     *
     * @param list 音频数据
     */
    private void resolveToWaveData(ArrayList<Short> list) {
        short allMax = 0;
        for (int i = 0; i < list.size(); i++) {
            Short sh = list.get(i);
            if (sh != null && sh > allMax) {
                allMax = sh;
            }
        }
        int curScale = allMax / mBaseLine;
        if (curScale > mScale) {
            mScale = ((curScale == 0) ? 1 : curScale);
        }
    }

    /**
     * 开始绘制
     */
    public void startView() {
        if (mInnerThread != null && mInnerThread.isAlive()) {
            mIsDraw = false;
            while (mInnerThread.isAlive()) ;
            mBackCanVans.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        mIsDraw = true;
        mInnerThread = new drawThread();
        mInnerThread.start();
    }

    /**
     * 停止绘制
     */
    public void stopView(boolean cleanView) {
        mIsDraw = false;
        if (mInnerThread != null) {
            while (mInnerThread.isAlive()) ;
        }
        if (cleanView) {
            mRecDataList.clear();
            mBackCanVans.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
    }

    /**
     * 停止绘制
     */
    public void stopView() {
        stopView(true);
    }



    public boolean isPause() {
        return mPause;
    }

    public void setPause(boolean pause) {
        synchronized (mRecDataList) {
            this.mPause = pause;
        }
    }

    /**
     * 三种颜色,不设置用默认的
     */
    public void setChangeColor(int color1, int color2, int color3) {
        this.mColor1 = color1;
        this.mColor2 = color2;
        this.mColor3 = color3;
    }


    public boolean isAlphaByVolume() {
        return mAlphaByVolume;
    }

    /**
     * 是否更具声音大小显示清晰度
     */
    public void setAlphaByVolume(boolean alphaByVolume) {
        this.mAlphaByVolume = alphaByVolume;
    }



    /**
     * 将这个list传到Record线程里，对其不断的填充
     * <p>
     * Map存有两个key，一个对应AudioWaveView的MAX这个key,一个对应AudioWaveView的MIN这个key
     *
     * @return 返回的是一个map的list
     */
    public ArrayList<Short> getRecList() {
        return mRecDataList;
    }


    public List<Integer> getmVolumeList() {
        return mVolumeList;
    }

    /**
     * 设置线与线之间的偏移
     *
     * @param offset 偏移值 pix
     */
    public void setOffset(int offset) {
        this.mOffset = offset;
    }


    public int getWaveColor() {
        return mWaveColor;
    }

    /**
     * 设置波形颜色
     *
     * @param waveColor 音频颜色
     */
    public void setWaveColor(int waveColor) {
        this.mWaveColor = waveColor;
        if (mPaint != null) {
            mPaint.setColor(mWaveColor);
        }
    }

    /**
     * 设置自定义的paint
     */
    public void setLinePaint(Paint paint) {
        if (paint != null) {
            mPaint = paint;
        }
    }


    /**
     * 设置波形颜色
     *
     * @param waveCount 波形数量 1或者2
     */
    public void setWaveCount(int waveCount) {
        mWaveCount = waveCount;
        if (mWaveCount < 1) {
            mWaveCount = 1;
        } else if (mWaveCount > 2) {
            mWaveCount = 2;
        }
    }

    /**
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    /**
     * 是否画出基线
     *
     * @param drawBase
     */
    public void setDrawBase(boolean drawBase) {
        mDrawBase = drawBase;
    }


    /**
     * 绘制相反方向
     */
    public void setDrawReverse(boolean drawReverse) {
        this.mDrawReverse = drawReverse;
    }


    /**
     * 绘制开始偏移量
     */
    public void setDrawStartOffset(int drawStartOffset) {
        this.mDrawStartOffset = drawStartOffset;
    }


}
