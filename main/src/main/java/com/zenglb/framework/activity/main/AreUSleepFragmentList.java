package com.zenglb.framework.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.leakcanary.RefWatcher;
import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.base.BaseFragment;
import com.zenglb.framework.R;
import com.zenglb.framework.base.MyApplication;
import com.zenglb.framework.http.core.HttpCall;
import com.zenglb.framework.http.result.AreuSleepResult;
import com.zenglb.framework.rxhttp.BaseSubscriber;
import com.zenglb.framework.rxhttp.RxSubscriberUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 懒加载太乱了，使用Rxjava 改造一下
 *
 *
 * @author zenglb 2016.10.24
 */
public class AreUSleepFragmentList extends BaseFragment {
    private String TAG = AreUSleepFragmentList.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private int page;
    private String mParam1;
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private List<AreuSleepResult> data = new ArrayList<>();

    public AreUSleepFragmentList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AreUSleepFragment.
     */
    public static AreUSleepFragmentList newInstance(String param1) {
        AreUSleepFragmentList fragment = new AreUSleepFragmentList();
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
     * 当视图可见的时候就会被调用，当然在onCreateView 也会调用一次，
     *
     * 太乱了，使用rxjava 改造一下懒加载  ！逻辑都是错误的！
     *
     *
     */
    @Override
    protected void lazyLoadData(boolean isForceLoad) {
        if (isViewsInit && visibleTime < 1) {
            Log.e(TAG, "视图已经初始化完毕了，虽然不去加载网络数据，但是可以加载一下本地持久化的缓存数据啊！");
        }
        if (!isViewsInit || visibleTime < 1) {  //假如views 没有初始化或者Fragment不可见，那就不要尝试加载数据
            return;
        } else {
            if (isForceLoad) {
                Log.e(TAG, "前面的支付页面支付9.9，那么这里显示的剩余金额必然变动了，敏感数据，要实时刷新");
            }
            if (visibleTime == 1) { //这里也不是每次可见的时候都能刷新，只有第一次可见的时候或者数据加载从来没有成功 才调用刷新
                springView.callFresh();
                Toast.makeText(mActivity, "第一次可见", Toast.LENGTH_SHORT).show();
            }
        }
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

        springView = (SpringView) rootView.findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getHttpData(mParam1, page);
            }

            @Override
            public void onLoadmore() {
                getHttpData(mParam1, page);
            }
        });

        mEmptyTipsTxt = (TextView) rootView.findViewById(R.id.tips_txt);
        mEmptyTipsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                springView.callFresh();
            }
        });

        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));

        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.goWebView("http://www.baidu.com", "");
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        super.initViews(rootView);  //
    }

    /**
     * 请求答题列表
     */
    private void getHttpData(String mParam1, int page) {
        HttpCall.getApiService().getAreuSleep(mParam1, page)
                .compose(RxSubscriberUtils.rxNetThreadHelper())
                .compose(bindToLifecycle())
                .subscribe(new BaseSubscriber<List<AreuSleepResult>>(getActivity(),false){
                    @Override
                    public void onSuccess(List<AreuSleepResult> areuSleepResults) {
                        disposeHttpResult(areuSleepResults);
                    }
                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
    }


    /**
     * 处理http返回来的结果
     *
     * @return
     */
    private void disposeHttpResult(List<AreuSleepResult> areuSleepBeanLista) {
        springView.onFinishFreshAndLoad();
        if (areuSleepBeanLista != null) {
            if (page <= 1) data.clear();

            if (areuSleepBeanLista != null && areuSleepBeanLista.size() != 0) {
                data.addAll(areuSleepBeanLista);
                page++;
                areUSleepListAdapter.notifyDataSetChanged();
            } else {
				Toast.makeText(getActivity(), "暂无数据，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        }

        if (data == null || data.size() == 0) {
            mEmptyTipsTxt.setVisibility(View.VISIBLE);
        } else {
            mEmptyTipsTxt.setVisibility(View.GONE);
        }
    }


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
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
