package com.zenglb.framework.activity.quick_input_things;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.zenglb.baselib.base.BaseActivity;
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
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * 处理短时间内的多次重复事件，其实使用Rxbinding 会更加的不错呢！
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
        setTitle("防止快速输入");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_quick_input_things;
    }

    @Override
    protected void initViews() {
        mEtSearch = (EditText) findViewById(R.id.edit_query);
        mTvSearch = (TextView) findViewById(R.id.edit_keywords);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                startSearch(s.toString());
            }
        });


        mPublishSubject = PublishSubject.create();

        mPublishSubject
                .debounce(200, TimeUnit.MILLISECONDS)
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


    private void startSearch(String query) {
        mPublishSubject.onNext(query);
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