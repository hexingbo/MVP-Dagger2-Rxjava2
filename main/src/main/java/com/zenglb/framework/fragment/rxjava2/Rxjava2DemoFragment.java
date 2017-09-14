package com.zenglb.framework.fragment.rxjava2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zenglb.baselib.base.BaseFragment;
import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.baselib.sharedpreferences.SharedPreferencesDao;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.main.AreUSleepListAdapter;
import com.zenglb.framework.config.SPKey;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.core.HttpResponse;
import com.zenglb.framework.retrofit.param.LoginParams;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.retrofit.result.LoginResult;
import com.zenglb.framework.retrofit.result.StaffMsg;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 喂,你睡着了吗（答题列表）
 *
 * @author zenglb 2016.10.24
 */
public class Rxjava2DemoFragment extends BaseFragment {
    private String TAG = Rxjava2DemoFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private String mParam1;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private List<JokesResult> data = new ArrayList<>();

    public Rxjava2DemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AreUSleepFragment.
     */
    public static Rxjava2DemoFragment newInstance(String param1) {
        Rxjava2DemoFragment fragment = new Rxjava2DemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_are_usleep, container, false);
        viewsInit(rootView);
        return rootView;
    }

    /**
     * Rxjava2 + retrofit 基本
     */
    private void getJokes() {
        HttpCall.getApiService().getJokes("expired", 1)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<List<JokesResult>>(null, false) {
                    @Override
                    public void onSuccess(List<JokesResult> areuSleepResults) {
                        Log.e("3232", areuSleepResults.toString());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
    }


    /**
     * 测试FlatMap 的操作
     * 实际中的应用场景应该是注册完毕后直接登录，这里演示为登录后获取信息，为了安全演示
     * <p>
     * 当然我觉得还是分开写更加简单，清晰，容易控制
     */
    private void testFlatMap() {
        LoginParams loginParams = new LoginParams();
        loginParams.setClient_id("5e96eac06151d0ce2dd9554d7ee167ce");
        loginParams.setClient_secret("aCE34n89Y277n3829S7PcMN8qANF8Fh");
        loginParams.setGrant_type("password");
        loginParams.setUsername("18826562075");
        loginParams.setPassword("zxcv1234");

        HttpCall.getApiService().goLoginByRxjavaObserver(loginParams)
                .compose(SwitchSchedulers.applySchedulers())
                .compose(bindToLifecycle())
                .doOnNext(loginResult -> {
                    if (loginResult.isSuccess()) {
                        String token = "Bearer " + loginResult.getResult().getAccessToken();
                        HttpCall.setToken(token);
                        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_ACCESS_TOKEN, token);
                        SharedPreferencesDao.getInstance().saveData(SPKey.KEY_REFRESH_TOKEN, loginResult.getResult().getRefreshToken());
                    } else {
                        //这里应该控制不要往下执行了！
                    }
                })
                .observeOn(Schedulers.io())          //回到IO线程去发起获取信息的请求
                .flatMap(new Function<HttpResponse<LoginResult>, ObservableSource<HttpResponse<StaffMsg>>>() {
                    @Override
                    public ObservableSource<HttpResponse<StaffMsg>> apply(@NonNull HttpResponse<LoginResult> loginResultHttpResponse) throws Exception {
                        return HttpCall.getApiService().getStaffMsg();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求个人信息的结果
                .subscribe(new BaseObserver<StaffMsg>(getActivity(), true) {
                    @Override
                    public void onSuccess(StaffMsg staffMsg) {
                        Log.e(TAG, staffMsg.toString());
                    }
                });
    }


    /**
     * zip 操作,是两个无任何意义的合成
     * 可是如果没有得到想要的结果呢？
     */
    private void zip() {
        Observable<HttpResponse<List<JokesResult>>> observable1 =
                HttpCall.getApiService().getJokes("expired", 1).subscribeOn(Schedulers.io());

        Observable<HttpResponse<StaffMsg>> observable2 =
                HttpCall.getApiService().getStaffMsg().subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2,
                new BiFunction<HttpResponse<List<JokesResult>>, HttpResponse<StaffMsg>, StaffMsg>() {
                    @Override
                    public StaffMsg apply(HttpResponse<List<JokesResult>> baseInfo,
                                          HttpResponse<StaffMsg> extraInfo) throws Exception {
                        return new StaffMsg(baseInfo.getResult().get(0).getTopic(), extraInfo.getResult().getFullname());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StaffMsg>() {
                    @Override
                    public void accept(StaffMsg staffMsg) throws Exception {
                        //do something;
                        Log.e(TAG, staffMsg.toString());
                    }
                });

        //下面是Lamada 的表达式

//        Observable.zip(observable1, observable2,
//                (baseInfo, extraInfo) -> new StaffMsg(baseInfo.getResult().get(0).getTopic(), extraInfo.getResult().getFullname()))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(staffMsg -> Log.e(TAG,staffMsg.toString()));


    }

    /**
     * init views,
     *
     * @param
     */
    private void viewsInit(View rootView) {
        areUSleepListAdapter = new AreUSleepListAdapter(getActivity(), data);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(areUSleepListAdapter);
        mEmptyTipsTxt = (TextView) rootView.findViewById(R.id.tips_txt);

        initDemoData();

        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AreUSleepListAdapter.ViewHolder view, int position) {
                switch (position) {
                    case 0:
                        getJokes();
                        break;
                    case 1:
                        testFlatMap();
                        break;
                    case 2:
                        zip();
                        break;
                    case 3:

                        break;
                }
            }

            @Override
            public void onItemLongClick(AreUSleepListAdapter.ViewHolder view, int position) {

            }
        });

    }

    /**
     * 初始化Demo 的数据
     */
    private void initDemoData() {
        data.add(new JokesResult("Rxjava+retrofit 基础操作", "Rxjava2+retrofit2 基础操作，返回的是HttpRespose<T>, T is List<Jokes>", "000"));

        data.add(new JokesResult("FlatMap 的变换操作", "FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，" +
                "然后将它们发射的事件合并后放进一个单独的Observable里. ", "111"));

        data.add(new JokesResult("Zip 的变换操作", "需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取, " +
                "而只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了", "222"));

        areUSleepListAdapter.notifyDataSetChanged();
    }


    /**
     * 当视图可见的时候就会被调用，当然在onCreateView 也会调用一次，
     * <p>
     * 太乱了，使用rxjava 改造一下懒加载  ！逻辑都是错误的！
     */
    @Override
    protected void lazyLoadData(boolean isForceLoad) {
        if (!isViewsInit || visibleTime < 1) {  //假如views 没有初始化或者Fragment不可见，那就不要尝试加载数据
            return;
        } else {
            if (isForceLoad) {
                Log.e(TAG, "前面的支付页面支付9.9，那么这里显示的剩余金额必然变动了，敏感数据，要实时刷新");
            }
            if (visibleTime == 1) { //这里也不是每次可见的时候都能刷新，只有第一次可见的时候或者数据加载从来没有成功 才调用刷新
                Toast.makeText(mActivity, "第一次可见", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //============================================================================================

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
