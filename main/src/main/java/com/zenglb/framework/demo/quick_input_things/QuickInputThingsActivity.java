package com.zenglb.framework.demo.quick_input_things;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * 处理短时间内的多次重复事件，可以使用
 * 1.Rxjava  debounce
 * 2.Rxbing  这个侵入比较大
 * <p>
 * 有没有更多的优化？
 * 比如能够取消前面的查询过程
 * <p>
 * 遇到这一类的事情怎么处理，比如定位的快速回调，传感器三重定位的的快速回调，都会在短时间产生大量的数据
 * <p>
 * http://www.jianshu.com/p/055002aaf1ca
 * <p>
 * <p>
 * <p>
 * <p>
 * Created by zlb on 2017/11/8.
 */
public class QuickInputThingsActivity extends BaseActivity {
    private EditText mEtSearch;
    private TextView mTvSearch;
    private PublishSubject<String> mPublishSubject;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("防止快速输入");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_quick_input_things;
    }

    @Override
    protected void initViews() {
        mEtSearch = (EditText) findViewById(R.id.edit_query);
        mTvSearch = (TextView) findViewById(R.id.edit_keywords);

        //RxBind click事件
        RxView.clicks(findViewById(R.id.edit_keywords))
                .throttleFirst(1, TimeUnit.SECONDS) //防止手快的点击，只是第一次有效
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(getApplicationContext(), "this is onClick", Toast.LENGTH_SHORT).show();
                    }
                });


        //2.Rxbind 防止大数据量的处理
        RxTextView.textChanges(findViewById(R.id.edit_query))
                .debounce(600, TimeUnit.MILLISECONDS)  //防止手抖快速输入，600 毫秒无输入后再处理
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {

                        return new StringBuilder(charSequence).toString();
                    }
                })
                .subscribe(charSequence -> {
                    Log.e("hahah", "准备去搜索，关键字： " + charSequence);
                });


//        //3.Rxjava 防止快速输入的处理
//        mEtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                mPublishSubject.onNext(s.toString());
//            }
//        });


        mPublishSubject = PublishSubject.create();

        mPublishSubject
                .debounce(200, TimeUnit.MILLISECONDS)  //去除抖动啊
                .switchMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String query) throws Exception {
                        return getSearchObservable(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mTvSearch.setText(s);
                    }
                });


        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mCompositeDisposable);
    }


    private Observable<String> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                Log.d("SearchActivity", "开始请求，关键词为：" + query);
                try {
                    Thread.sleep(100 + (long) (Math.random() * 500));
                } catch (InterruptedException e) {
                    if (!observableEmitter.isDisposed()) {
                        observableEmitter.onError(e);
                    }
                }
                Log.d("SearchActivity", "结束请求，关键词为：" + query);
                observableEmitter.onNext("完成搜索，关键词为：" + query);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}