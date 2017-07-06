package com.zenglb.framework.activity.mvp;

/**
 * Created by zenglb on 2017/7/5.
 */

public class TaskDataSourceImpl implements TaskDataSource {
    @Override
    public String getStringFromRemote() {
        return "Hello(remote) ";
    }

    @Override
    public String getStringFromCache() {
        return " World (Cache)";
    }
}

