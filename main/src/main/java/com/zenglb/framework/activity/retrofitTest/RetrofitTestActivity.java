package com.zenglb.framework.activity.retrofitTest;

import android.os.Bundle;
import android.widget.TextView;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.R;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.List;

public class RetrofitTestActivity extends BaseActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpData("expired", 1);
        getHttpData("expired", 2);
        getHttpData("expired", 3);
    }


    /**
     * 请
     */
    private void getHttpData(String mParam1, int page) {
        HttpCall.getApiService().getAreuSleepByObserver(mParam1, page)
                .compose(SwitchSchedulers.applySchedulers())
                .compose(bindToLifecycle()) //两个compose 能否合并起来，或者重写一个操作符
                .subscribe(new BaseObserver<List<JokesResult>>(mContext) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {
                        textView.setText(jokesResults.toString());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_retrofit_test;
    }

    @Override
    protected void initViews() {
        textView = (TextView) findViewById(R.id.test);
//        this.finish();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        getHttpData("expired", 1);
        getHttpData("expired", 2);
        getHttpData("expired", 3);
    }

}
