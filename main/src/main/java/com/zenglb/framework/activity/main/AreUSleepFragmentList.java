package com.zenglb.framework.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
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
import com.zenglb.baselib.base.BaseFragment;
import com.zenglb.baselib.rxUtils.SwitchSchedulers;
import com.zenglb.baselib.utils.TransitionHelper;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.animal.SharedElementActivity;
import com.zenglb.framework.base.MyApplication;
import com.zenglb.framework.retrofit.core.HttpCall;
import com.zenglb.framework.retrofit.result.JokesResult;
import com.zenglb.framework.rxhttp.BaseObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 懒加载
 * 数据状态恢复
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
    private ArrayList<JokesResult> data = new ArrayList<>();


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("dataArray",data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null&&savedInstanceState.containsKey("dataArray")){
            Log.e(TAG,"onActivityCreated:      "+savedInstanceState.getParcelableArrayList("dataArray").toString());
        }else{
            Log.e(TAG,"onActivityCreated ");
        }

    }


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
        Log.e(TAG,"onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }else {
            setArguments(new Bundle());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_are_usleep, container, false);
        viewsInit(rootView);
        return rootView;
    }


    /**
     * 当视图可见的时候就会被调用，当然在onCreateView 也会调用一次，
     * <p>
     * 太乱了，使用rxjava 改造一下懒加载  ！逻辑都是错误的！
     */
    @Override
    protected void lazyLoadData(boolean isForceLoad) {
        Log.e(TAG,"lazyLoadData         "+"visibleTime: "+visibleTime+"     isViewsInit: "+isViewsInit);

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
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        springView.callFresh();
//                    }
//                }, 500);
                new Handler().postDelayed(() -> springView.callFresh(), 500);

            }
        }
    }


    /**
     * 带动画的跳转,
     *
     * @param target
     * @param viewHolder
     * @param jokesResult
     */
    private void transitionToActivity(Class target, AreUSleepListAdapter.ViewHolder viewHolder, JokesResult jokesResult) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), true,
                new Pair<>(viewHolder.getTopic(), getActivity().getString(R.string.shared_name)),
                new Pair<>(viewHolder.getTime(), getActivity().getString(R.string.shared_time)));

        startActivity(target, pairs, jokesResult);
    }


    /**
     * 带动画
     *
     * @param target
     * @param pairs
     * @param jokesResult
     */
    private void startActivity(Class target, Pair<View, String>[] pairs, JokesResult jokesResult) {
        Intent i = new Intent(getActivity(), target);
        i.putExtra("jokesResult", jokesResult);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
        getActivity().startActivity(i, transitionActivityOptions.toBundle());
    }


    /**
     * init views,
     *
     * @param
     */
    private void viewsInit(View rootView) {
        areUSleepListAdapter = new AreUSleepListAdapter(getActivity(), data);
        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AreUSleepListAdapter.ViewHolder view, int position) {
                transitionToActivity(SharedElementActivity.class, view, data.get(position));
            }

            @Override
            public void onItemLongClick(AreUSleepListAdapter.ViewHolder view, int position) {

            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(areUSleepListAdapter);

        springView = (SpringView) rootView.findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG,"onRefresh data");
                page = 1;
                getHttpData(mParam1, page);
            }

            @Override
            public void onLoadmore() {
                getHttpData(mParam1, page);
            }
        });

        mEmptyTipsTxt = (TextView) rootView.findViewById(R.id.tips_txt);
        mEmptyTipsTxt.setOnClickListener(view -> springView.callFresh());

        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));

        super.initViews(rootView);  //
    }


    /**
     * 请求答题列表
     */
    private void getHttpData(String mParam1, int page) {
        HttpCall.getApiService().getAreuSleepByObserver(mParam1, page)
                .compose(SwitchSchedulers.applySchedulers())
                .compose(bindToLifecycle()) //两个compose 能否合并起来，或者重写一个操作符
                .subscribe(new BaseObserver<List<JokesResult>>(getActivity(), false) {
                    @Override
                    public void onSuccess(List<JokesResult> jokesResults) {
                        disposeHttpResult(jokesResults);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        disposeHttpResult(null);
                    }
                });
    }


    /**
     * 处理http返回来的结果
     *
     * @return
     */
    private void disposeHttpResult(List<JokesResult> jokesResultList) {
        springView.onFinishFreshAndLoad();
        if (jokesResultList != null) {
            if (page <= 1) data.clear();

            if (jokesResultList != null && jokesResultList.size() != 0) {
                data.addAll(jokesResultList);
                page++;
                areUSleepListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "暂无数据，请稍后再试！", Toast.LENGTH_SHORT).show();
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");

        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
