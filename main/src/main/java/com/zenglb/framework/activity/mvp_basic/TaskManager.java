package com.zenglb.framework.activity.mvp_basic;

import android.util.Log;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.List;

/**
 * * 从数据层获取的数据，在这里进行拼装和组合
 * Created by zenglb on 2017/7/5.
 */

public class TaskManager {
    TaskDataSource dataSource;

    public TaskManager(TaskDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getShowContent() {
        //Todo what you want do on the original data
        return dataSource.getStringFromRemote() + dataSource.getStringFromCache();
    }

    /**
     * 这个请求到的数据怎么返回到MainView 呢？
     */
    public void getHttpData(String mParam1, int page) {
        HttpCall.getApiService().getAreuSleepByObserver(mParam1, page)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<List<JokesResult>>(null) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {
//                        disposeHttpResult(jokesResults);
                        Log.e("getHttpData","数据我是拿到了!怎么返回去呢？忧伤，Size="+jokesResults.size());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }

                });
    }


}
