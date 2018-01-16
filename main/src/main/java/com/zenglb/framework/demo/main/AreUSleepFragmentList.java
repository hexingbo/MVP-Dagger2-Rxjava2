package com.zenglb.framework.demo.main;

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
import com.zenglb.framework.base.BaseFragment;
import com.zenglb.framework.di.scope.ActivityScope;
import com.zenglb.framework.http.ApiService;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;
import com.zenglb.baselib.utils.TransitionHelper;
import com.zenglb.framework.R;
import com.zenglb.framework.demo.animal.SharedElementActivity;
import com.zenglb.framework.MyApplication;
import com.zenglb.framework.http.result.JokesResult;
import com.zlb.httplib.core.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 懒加载
 * 数据状态恢复
 *
 * @author zenglb 2016.10.24
 */
@ActivityScope  //?????????
public class AreUSleepFragmentList extends BaseFragment {
    private String TAG = AreUSleepFragmentList.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private int page;
    private String mParam1="expired";
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private ArrayList<JokesResult> data = new ArrayList<>();

    @Inject
    ApiService apiService;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("dataArray", data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("dataArray")) {
            Log.e(TAG, "onActivityCreated:      " + savedInstanceState.getParcelableArrayList("dataArray").toString());
        } else {
            Log.e(TAG, "onActivityCreated ");
        }
    }

    @Inject
    public AreUSleepFragmentList() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @return A new instance of fragment AreUSleepFragment.
//     */
//    @Deprecated
//    public static AreUSleepFragmentList newInstance(String param1) {
//        AreUSleepFragmentList fragment = new AreUSleepFragmentList();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        } else {
            setArguments(new Bundle());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_are_usleep, container, false);
        viewsInit(rootView);
        new Handler().postDelayed(() -> springView.callFresh(), 500);

        return rootView;
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
//        getActivity().startActivityForResult();
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
                Log.e(TAG, "onRefresh data");
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
        apiService.getAreuSleepByObserver(mParam1, page)
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
    }
}
