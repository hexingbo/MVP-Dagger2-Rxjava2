package com.zenglb.framework.mvp.handylife;

import com.zenglb.framework.http.ApiService;
import com.zlb.httplib.core.BaseObserver;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里就不做什么先加载本地的ORM DB 然后再加载 HTTP 了
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class HandyLifeRepository implements IHandyLifeDataSource {

    // Get the ApiService from dagger。 用dagger 来注入 ApiService
    @Inject
    ApiService apiService;

    @Inject
    public HandyLifeRepository() {

    }

    public HandyLifeRepository(ApiService apiService){
        this.apiService=apiService;
    }


    /**
     * getHandyLifeData from http server
     *
     * @param type                      数据类型，{city guide,shop,eat}
     * @param page                      page index
     * @param loadHandyLifeDataCallback the callBack
     */
    @Override
    public void getHandyLifeData(String type, int page, LoadHandyLifeDataCallback loadHandyLifeDataCallback) {
        apiService.getHandyLifeData(type, page)
//                .compose(SwitchSchedulers.applySchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<HandyLifeResultBean>>(null) {
                    @Override
                    public void onSuccess(List<HandyLifeResultBean> lifeResultBeans) {
                        if (null != loadHandyLifeDataCallback) {
                            loadHandyLifeDataCallback.onHandyLifeDataSuccess(lifeResultBeans);
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        if (null != loadHandyLifeDataCallback) {
                            loadHandyLifeDataCallback.onHandyLifeDataFailed(code, message);
                        }
                    }
                });
    }

}
