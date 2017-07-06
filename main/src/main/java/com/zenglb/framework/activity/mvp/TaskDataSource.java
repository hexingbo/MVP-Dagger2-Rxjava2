package com.zenglb.framework.activity.mvp;

/**
 * data 层接口定义
 *
 * Created by zenglb on 2017/7/5.
 */

public interface TaskDataSource {
    String getStringFromRemote();
    String getStringFromCache();
}
