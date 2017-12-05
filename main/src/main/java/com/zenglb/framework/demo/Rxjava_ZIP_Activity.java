package com.zenglb.framework.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.framework.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 练习使用ZIP 操作符从网络异步获取4张图片然后ZIP 合成一张图片
 *
 */
public class Rxjava_ZIP_Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ZIP 异步合成图像");

        test();
    }


    /**
     * 合成图像
     *
     */
    private void test(){

        Observable<Bitmap> observable1 = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap=null;

                emitter.onNext(bitmap);
                emitter.onComplete();
            }
        });

        Observable<Bitmap> observable2 = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap=null;

                emitter.onNext(bitmap);
                emitter.onComplete();
            }
        });


    }



    @Override
    protected int setLayoutId() {
        return R.layout.activity_rxjava__zip_;
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void setToolBarTitle(CharSequence title) {
        super.setToolBarTitle(title);
    }

    @Override
    protected boolean isShowBacking() {
        return super.isShowBacking();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
