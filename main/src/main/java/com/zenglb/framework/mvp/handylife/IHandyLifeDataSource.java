package com.zenglb.framework.mvp.handylife;

import java.util.List;

/**
 * Main entry point for accessing data.
 *
 * For simplicity, All The data is load form remote,we do not use orm db to persistent data
 *
 * Created by zlb on 2018/3/23.
 */
public interface IHandyLifeDataSource {

    void getHandyLifeData(String type, int page,LoadHandyLifeDataCallback loadHandyLifeDataCallback) ;


    /**
     *  the callback of getHandyLifeData
     */
    interface LoadHandyLifeDataCallback {
        void onHandyLifeDataSuccess(List<HandyLifeResultBean> handyLifeResultBeans);
        void onHandyLifeDataFailed(int code,String message);
    }

}
