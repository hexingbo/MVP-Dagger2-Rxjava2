package com.zenglb.framework.mvp_more;

import com.zenglb.framework.MyApplication;
import com.zenglb.framework.database.dbmaster.JokesResultDao;
import com.zenglb.framework.mvp_base.BaseModel;
import com.zenglb.framework.retrofit.core.HttpResponse;
import com.zenglb.framework.retrofit.result.JokesResult;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by zlb on 2017/9/13.
 */

public class TasksRepository extends BaseModel implements ITaskDataSource {
    /**
     * 获取缓存的数据,测试1，这样子还是在主线程读取的数据库啊！
     *
     * @return
     */
    public Maybe<List<JokesResult>> getCacheTasks22() {
        String threadName = Thread.currentThread().getName(); //  这里线程的切换并没有成功

        JokesResultDao jokesResultDao = MyApplication.getInstance().getDaoSession().getJokesResultDao();
        List<JokesResult> jokesResultList = jokesResultDao.loadAll();
        return jokesResultList.isEmpty() ? Maybe.empty() : Maybe.just(jokesResultList);
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

            JokesResultDao jokesResultDao = MyApplication.getInstance().getDaoSession().getJokesResultDao();
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

        return ApiService.getAreuSleepByObserver(type, page);
    }

}
