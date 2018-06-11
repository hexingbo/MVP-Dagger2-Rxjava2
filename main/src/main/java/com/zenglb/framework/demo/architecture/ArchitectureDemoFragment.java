package com.zenglb.framework.demo.architecture;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.zenglb.framework.R;
import com.zenglb.framework.demo.demo.AreUSleepListAdapter;
import com.zenglb.framework.http.result.JokesResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author zenglb 2016.10.24
 */
public class ArchitectureDemoFragment extends BaseFragment {
    public static final String TAG = ArchitectureDemoFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private int page;
    private String mParam1;
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private ArrayList<JokesResult> data = new ArrayList<>();

    private UserProfileViewModel viewModel;

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

        //ViewModel 对象的生成很有趣味，很有趣
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);

        viewModel.init(mParam1,1);
        viewModel.getUser().observe(this, new Observer<List<JokesResult>>() {
            @Override
            public void onChanged(@Nullable List<JokesResult> jokesResults) {
                //怎样缓存这些List 数据呢？
                Log.e(TAG, "onActivityCreated ");
                disposeHttpResult(jokesResults);
            }
        });

    }


    public static ArchitectureDemoFragment newInstance(String param1) {
        ArchitectureDemoFragment fragment = new ArchitectureDemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        if (getArguments() != null) {
            //如果Android操作系统回收了Fragment，则会保留此信息，以便下次重新启动应用时，该ID可用。
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
        return rootView;
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
                page = 1;
                // TODO: 2017/11/29 怎样来刷新数据呢？ 
//                getHttpData(mParam1, page);
            }

            @Override
            public void onLoadmore() {
//                getHttpData(mParam1, page);
            }
        });

        mEmptyTipsTxt = (TextView) rootView.findViewById(R.id.tips_txt);
        mEmptyTipsTxt.setOnClickListener(view -> springView.callFresh());

        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));

        super.initViews(rootView);  //
    }


//    @Override
//    protected void lazyLoadData(boolean isForceLoad) {
//
//    }

    public ArchitectureDemoFragment() {
        // Required empty public constructor
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
    }
}
