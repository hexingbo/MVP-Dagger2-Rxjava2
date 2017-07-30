package com.zenglb.framework.activity.mvp;

import android.util.Log;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.List;

/**
 *
 *
 * Created by zenglb on 2017/7/5.
 */
public class MainPresenter {
    MainView mainView;
    TaskManager taskData;

    public MainPresenter() {
        this.taskData = new TaskManager(new TaskDataSourceImpl());
    }

    /**
     * 这个 Test 是怎么写的啊？
     *
     * @param viewListener
     * @return
     */
//    public MainPresenter test() {
//        this.taskData = new TaskManager(new TaskDataSourceTestImpl());
//        return this;
//    }

    /**
     * what is this
     *
     * @param viewListener
     * @return
     */
    public MainPresenter addTaskListener(MainView viewListener) {
        this.mainView = viewListener;
        return this;
    }


    public void getString() {
        String str = taskData.getShowContent();
        mainView.onShowString(str);
    }


    /**
     * View 层去请求刷新列表的数据
     * 先搞一些假的数据过去
     */
    public void getRefreshData(String mParam1, int page) {

        HttpCall.getApiService().getAreuSleepByObserver(mParam1, page)
                .compose(SwitchSchedulers.applySchedulers())
//                .compose(bindToLifecycle()) //两个compose 能否合并起来，或者重写一个操作符
                .subscribe(new BaseObserver<List<JokesResult>>(null) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {
                        mainView.onRefreshListView(jokesResults);
                        Log.e("getHttpData", "数据我是拿到了!怎么返回去呢？忧伤，Size=" + jokesResults.size());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });

    }

}
