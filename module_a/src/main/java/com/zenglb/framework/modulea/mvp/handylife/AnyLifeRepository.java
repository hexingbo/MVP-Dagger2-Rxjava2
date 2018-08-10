package com.zenglb.framework.modulea.mvp.handylife;

import com.zlb.http.result.AnyLifeResult;
import com.zlb.http.ApiService;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.rxUtils.SwitchSchedulers;

import java.util.List;

import javax.inject.Inject;

/**
 * 这里就不做什么先加载本地的ORM DB 然后再加载 HTTP 了
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class AnyLifeRepository implements IAnyLifeDataSource {

    // Get the ApiService from dagger。 用dagger 来注入 ApiService
    @Inject
    ApiService apiService;

    @Inject
    public AnyLifeRepository() {

    }

    public AnyLifeRepository(ApiService apiService) {
        this.apiService = apiService;
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
                .compose(SwitchSchedulers.applySchedulers())

                //BaseObserver 参数问题优化，如果不传参数context 的话，依赖context 的功能就要改
                .subscribe(new BaseObserver<List<AnyLifeResult>>(null) {
                    @Override
                    public void onSuccess(List<AnyLifeResult> lifeResultBeans) {
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

//                        //这个API 失效了，先假设能成功吧
//                        try {
//                            List<AnyLifeResultBean> mHandyLifeResultList = new Gson().fromJson(StaticJSON.jsonStr,
//                                    new TypeToken<List<AnyLifeResultBean>>() {
//                                    }.getType());
//                            loadHandyLifeDataCallback.onHandyLifeDataSuccess(mHandyLifeResultList);
//                        } catch (Exception e) {
//                            Log.e("JSON Exception", e.toString());
//                        }


                    }
                });
    }

}
