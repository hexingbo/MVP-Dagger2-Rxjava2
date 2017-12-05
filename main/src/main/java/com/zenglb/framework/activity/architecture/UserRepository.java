package com.zenglb.framework.activity.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.retrofit.result.StaffMsg;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.List;

import javax.inject.Singleton;

/**
 * 抽象数据来源，让你根本不知道从哪里来的，低耦合
 *
 * Created by zlb on 2017/11/20.
 */
@Singleton
public class UserRepository {

    public LiveData<List<JokesResult>> getUser(String mParam1,int page) {
        // This is not an optimal implementation, we'll fix it below
        final MutableLiveData<List<JokesResult>> data = new MutableLiveData<>();

        HttpCall.getApiService().getAreuSleepByObserver(mParam1, page)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<List<JokesResult>>(null) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {
                        data.setValue(jokesResults);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });


        return data;  //live data,会自动的更新UI
    }

}
