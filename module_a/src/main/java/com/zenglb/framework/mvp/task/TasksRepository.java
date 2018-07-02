package com.zenglb.framework.mvp.task;

import com.zlb.http.ApiService;
import com.zlb.http.result.JokesResult;
import com.zlb.httplib.HttpResponse;
import com.zlb.persistence.dbmaster.DaoSession;
import com.zlb.persistence.dbmaster.JokesResultDao;

import java.util.List;

import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Just A Demo
 *
 * Created by zlb on 2017/9/13.
 */
@Deprecated
public class TasksRepository  implements ITaskDataSource {

    @Inject
    DaoSession daoSession;

    @Inject
    ApiService apiService;

    @Inject
    public TasksRepository() {

    }

    /**
     * 获取缓存的数据,测试2，OK
     *
     * @return
     */
    @Override
    public Single<List<JokesResult>> getCacheTasks() {
        return Single.create(emitter -> {
            String threadName = Thread.currentThread().getName();  // 这里一定不是主线程，也就是在这里打断点并不会影响UI

            JokesResultDao jokesResultDao = daoSession.getJokesResultDao();
            List<JokesResult> jokesResultList = jokesResultDao.loadAll();
            emitter.onSuccess(jokesResultList);
        });
    }


    /**
     * 获取服务器的数据，那这里又是怎么切换线程的呢？如果是主线程Http请求肯定会报错的！
     *
     * @param type 数据的类型
     * @param page 当前页码
     * @return
     */
    @Override
    public Observable<HttpResponse<List<JokesResult>>> getRemoteTasks(String type, int page) {
        String threadName = Thread.currentThread().getName();

        return apiService.getAreuSleepByObserver(type, page);
    }

}
