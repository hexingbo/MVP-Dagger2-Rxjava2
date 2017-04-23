package com.zenglb.framework.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.result.JokesResult;
import com.zenglb.framework.rxhttp.BaseObserver;
import com.zenglb.baselib.rxUtils.RxObservableUtils;

import java.util.List;

/**
 * 在Service 中做一点事情！
 *
 */
public class TestRxIntentService extends IntentService {
    public TestRxIntentService() {
        super("TestRxIntentService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TestRxIntentService.class);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            HttpCall.getApiService().getJokes("expired", 1)
                    .compose(RxObservableUtils.applySchedulers())
                    .subscribe(new BaseObserver<List<JokesResult>>(null,false){
                        @Override
                        public void onSuccess(List<JokesResult> areuSleepResults) {
                            Log.e("3232",areuSleepResults.toString());
                        }
                        @Override
                        public void onFailure(int code, String message) {
//                            super.onFailure(code, message);
                        }
                    });

        }
    }

}
