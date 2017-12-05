package com.zenglb.framework.activity.rxjava2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zenglb.framework.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ZipActivity extends AppCompatActivity {

    private ImageView imageView;

    private String imageUri1="http://c.hiphotos.baidu.com/image/pic/item/a044ad345982b2b7e2ee380e38adcbef77099b82.jpg";
    private String imageUri2="http://a.hiphotos.baidu.com/image/pic/item/a9d3fd1f4134970a489111e59ccad1c8a6865d1d.jpg";
    private String imageUri3="http://h.hiphotos.baidu.com/image/pic/item/a044ad345982b2b76762a30e38adcbef77099b7f.jpg";
    private String imageUri4="http://g.hiphotos.baidu.com/image/pic/item/f703738da9773912edab11abf1198618377ae26f.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        imageView=(ImageView) findViewById(R.id.imageview);
//        basicZip();
        drawTextBitmap();
        zipBitmap();
    }

    /**
     *
     */
    private Bitmap drawTextBitmap(){
        Bitmap bitmap=Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(getResources().getColor(R.color.colorPrimary));//填充颜色
        Canvas canvas = new Canvas(bitmap);
        String testString = "静";
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(200);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(testString, 0, testString.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (400 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(testString,400 / 2 - bounds.width() / 2, baseline, mPaint);

        return bitmap;
    }


    /**
     *  最基础的Zip 操作
     */
    private void basicZip(){
        Bitmap bmp = ImageLoader.getInstance().loadImageSync(imageUri1);
        Bitmap bmp2 = ImageLoader.getInstance().loadImageSync(imageUri2);

        // Load image, decode it to Bitmap and return Bitmap to callback
        ImageLoader.getInstance().loadImage(imageUri1, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap
                Log.e("dd","ddd");
            }
        });

    }


    /**
     *
     *
     */
    private void zipBitmap(){
        Observable<Bitmap> observable1 = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                ImageLoader.getInstance().loadImage(imageUri1, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        emitter.onNext(scaleBitmap(loadedImage,400,400));
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io());

        Observable<Bitmap> observable2 = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                // Load image, decode it to Bitmap and return Bitmap to callback
                ImageLoader.getInstance().loadImage(imageUri2, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        emitter.onNext(scaleBitmap(loadedImage,400,400));
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io());


        Observable.zip(observable1, observable2, new BiFunction<Bitmap, Bitmap, Bitmap>() {
            @Override
            public Bitmap apply(Bitmap b1, Bitmap b2) throws Exception {
                Bitmap bitmap=Bitmap.createBitmap(800,800, Bitmap.Config.ARGB_4444);  //800*800 的空白区域

                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(b1, 0,0, null);  //第一张图,在大图的x坐标，用坐标，paint
                canvas.drawBitmap(b2, 400,400, null);

                return bitmap;
            }
        }).observeOn(AndroidSchedulers.mainThread())
           .subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap s) throws Exception {
                Log.e("dd",s.toString()+"555");
                imageView.setImageBitmap(s);
            }
        });

    }



    //======Utils===========

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }


}
