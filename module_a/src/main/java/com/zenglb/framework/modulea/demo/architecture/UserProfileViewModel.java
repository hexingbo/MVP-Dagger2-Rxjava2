package com.zenglb.framework.modulea.demo.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zlb.http.result.JokesResult;

import java.util.List;

import javax.inject.Inject;

/**
 * Google  的最新框架组建ViewModel
 *
 * UI 被销毁后这里的数据将被清除，然后自动的更新啊
 * ViewModel提供特定UI组件的数据，例如Activity和Fragment，并处理与数据处理业务部分的通信，例如调用其他组件来加载数据或转发用户修改。
 * ViewModel不了解View，并且不受UI的重建（如重由于旋转而导致的Activity的重建）的影响。
 *
 *
 * Created by zlb on 2017/11/20.
 */
public class UserProfileViewModel extends ViewModel {
    private LiveData<List<JokesResult>> user;

    @Inject
    public UserRepository userRepo;

    public void init(String mParam1, int page) {

        if (this.user != null) {
            // ViewModel is created per Fragment so
            // we know the userId won't change
            return;
        }

//        DaggerUserComponent.create().inject(this); //7 使用FruitComponent的实现类注入 //组件化删除
        user = userRepo.getUser(mParam1, page);
    }

    public LiveData<List<JokesResult>> getUser() {
        return this.user;
    }

}
