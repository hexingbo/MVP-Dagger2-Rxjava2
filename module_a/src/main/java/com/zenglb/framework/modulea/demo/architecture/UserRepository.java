package com.zenglb.framework.modulea.demo.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zlb.http.ApiService;
import com.zlb.http.result.JokesResult;
import com.zlb.httplib.rxUtils.SwitchSchedulers;
import com.zlb.httplib.BaseObserver;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 抽象数据来源，让你根本不知道从哪里来的，低耦合
 *
 * Created by zlb on 2017/11/20.
 */
@Singleton
public class UserRepository {

    @Inject
    AModuleApiService apiService;

    public LiveData<List<JokesResult>> getUser(String mParam1, int page) {
        // This is not an optimal implementation, we'll fix it below
        final MutableLiveData<List<JokesResult>> data = new MutableLiveData<>();

        apiService.getAreuSleepByObserver(mParam1, page)
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
