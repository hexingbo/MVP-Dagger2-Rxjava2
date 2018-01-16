package com.zenglb.framework.mvp.mvp_more;

import com.zenglb.framework.http.result.JokesResult;
import java.util.List;

/**
 * 合约，一个功能的基本只要看这个类就知道功能纲要了
 * <p>
 * Created by anylife.zlb@gmail.com
 * on 2017/8/20.
 */
public class TaskContract {

    /**
     * 对UI 的操作的接口有哪些，一看就只明白了
     */
    public interface TaskView {
        void showTasks(List<JokesResult> jokesResults);
        void getTaskFailed(String message);
    }


    /**
     * View 层对Presenter 的请求
     */
    public interface TaskPresenter {
        void getRemoteTasks(String type, int page);
        void getCacheTasks();
    }


}
