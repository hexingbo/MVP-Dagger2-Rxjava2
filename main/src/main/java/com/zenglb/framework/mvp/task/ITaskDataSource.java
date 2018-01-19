package com.zenglb.framework.mvp.task;

import com.zlb.httplib.core.HttpResponse;
import com.zenglb.framework.http.result.JokesResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * data 层接口定义，一看就知道有哪些数据的操作了！
 * <p>
 * Created by zenglb on 2017/7/5.
 */
public interface ITaskDataSource {

    /**
     * 获取缓存最新的20条数据
     *
     * @return
     */
    Single<List<JokesResult>> getCacheTasks();

    /**
     * 获取服务器的数据
     *
     * @return
     */
    Observable<HttpResponse<List<JokesResult>>> getRemoteTasks(String type, int page);

}
