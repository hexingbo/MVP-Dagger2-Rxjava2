package com.zenglb.framework.mvp.task;

import android.app.Activity;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;
import com.zenglb.framework.persistence.dbmaster.DaoSession;
import com.zenglb.framework.http.result.JokesResult;
import com.zlb.httplib.core.BaseObserver;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 *
 * Created by zenglb on 2017/7/5.
 */
public class TaskPresenter implements TaskContract.TaskPresenter {
    @Inject
    DaoSession daoSession;

    @Inject
    TasksRepository tasksRepository;

    TaskContract.TaskView taskView;

    @Inject
    public TaskPresenter(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    /**
     * 获取缓存的最新的20条数据
     */
    @Override
    public void getCacheTasks() {
        tasksRepository.getCacheTasks()
                .compose(SwitchSchedulers.applySingleSchedulers())
                .subscribe(new Consumer<List<JokesResult>>() {
                    @Override
                    public void accept(@NonNull List<JokesResult> jokesResults) throws Exception {
                        taskView.showCacheTasks(jokesResults);
//                        taskView.showTasks(jokesResults);

                    }
                });
    }

    /**
     * View 层去请求刷新列表的数据
     * 先搞一些假的数据过去
     */
    @Override
    public void getRemoteTasks(String type, int page) {

        tasksRepository.getRemoteTasks(type, page)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<List<JokesResult>>(((Activity)taskView)) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {
                        // View 生命周期的状态不可预知啊！
                        taskView.showTasks(jokesResults);

                        // TODO: 2017/9/14 操作数据库放到非UI 线程里面去做吧
                        //DB 保存最新的Page == 1 的20条数据
                        if (page == 1) {
                            daoSession.getJokesResultDao().deleteAll();
                            daoSession.getJokesResultDao().insertOrReplaceInTx(jokesResults);
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        taskView.getTaskFailed(code,message);
                    }
                });
    }


    /**
     * 这下面的两行能不能 Base化解
     *
     * @param view the view associated with this presenter
     */
    @Override
    public void takeView(TaskContract.TaskView view) {
        taskView=view;
    }

    @Override
    public void dropView() {
        //?????????????
    }



}
