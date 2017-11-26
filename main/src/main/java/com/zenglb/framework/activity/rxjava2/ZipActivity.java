package com.zenglb.framework.activity.rxjava2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private String imageUri5="http://a.hiphotos.baidu.com/image/pic/item/503d269759ee3d6d453aab8b48166d224e4adef5.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        imageView=(ImageView) findViewById(R.id.imageview);
//        basicZip();
        zipBitmap();
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



    private void zipBitmap(){

        Observable<Bitmap> observable1 = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap b1=null;
                Bitmap bmp2 = ImageLoader.getInstance().loadImageSync(imageUri2);

                // Load image, decode it to Bitmap and return Bitmap to callback
                ImageLoader.getInstance().loadImage(imageUri1, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        Log.e("dd","1111111");
                        emitter.onNext(loadedImage);
                        emitter.onComplete();
                    }
                });

            }
        }).subscribeOn(Schedulers.io());

        Observable<Bitmap> observable2 = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap b1=null;
                Bitmap bmp2 = ImageLoader.getInstance().loadImageSync(imageUri2);

                // Load image, decode it to Bitmap and return Bitmap to callback
                ImageLoader.getInstance().loadImage(imageUri2, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        // Do whatever you want with Bitmap
                        Log.e("dd","2222");
                        emitter.onNext(loadedImage);
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


}
