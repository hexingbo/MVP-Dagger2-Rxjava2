package com.zenglb.framework.mvp_more;

import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.framework.MyApplication;
import com.zenglb.framework.database.dbmaster.DaoSession;
import com.zenglb.framework.mvp_base.BasePresenter;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 *
 * Created by zenglb on 2017/7/5.
 */
public class TaskPresenter extends BasePresenter<TasksRepository,MVPActivity> implements TaskContract.TaskPresenter {

    /**
     * 获取缓存的最新的20条数据
     */
    @Override
    public void getCacheTasks() {
        getIModel().getCacheTasks()
                .compose(SwitchSchedulers.applySingleSchedulers())
                .subscribe(new Consumer<List<JokesResult>>() {
                    @Override
                    public void accept(@NonNull List<JokesResult> jokesResults) throws Exception {
                        // TODO: 2017/9/14 这里每次都要判断null，很是烦人啊 ！ 
                        getIView().showCacheTasks(jokesResults);
                    }
                });
    }


    /**
     * View 层去请求刷新列表的数据
     * 先搞一些假的数据过去
     */
    @Override
    public void getRemoteTasks(String type, int page) {

        getIModel().getRemoteTasks(type, page)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<List<JokesResult>>(getIView().mContext) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {

                        // View 生命周期的状态不可预知啊！
                        getIView().showTasks(jokesResults);

                        // TODO: 2017/9/14 操作数据库放到非UI 线程里面去做吧
                        //DB 保存最新的Page == 1 的20条数据
                        if (page == 1) {
                            DaoSession daoSession = MyApplication.getInstance().getDaoSession();
                            daoSession.getJokesResultDao().deleteAll();
                            daoSession.getJokesResultDao().insertOrReplaceInTx(jokesResults);
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        getIView().getTaskFailed(message);
                    }
                });
    }

}
