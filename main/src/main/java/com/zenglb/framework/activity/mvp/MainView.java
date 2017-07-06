package com.zenglb.framework.activity.mvp;

import com.zenglb.framework.retrofit.result.JokesResult;

import java.util.List;

/**
 * View 层的对外接口 ,Present 拿到数据后会调用UI 的刷新接口更新数据
 *
 * Created by zenglb on 2017/7/5.
 */
public interface MainView {
    void onShowString(String json);
    void onRefreshListView(List<JokesResult> jokesResults);  //刷新数据
}
