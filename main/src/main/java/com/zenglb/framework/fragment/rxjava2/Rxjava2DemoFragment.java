package com.zenglb.framework.fragment.rxjava2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liaoinstan.springview.widget.SpringView;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.main.AreUSleepListAdapter;
import com.zenglb.framework.http.result.AreuSleepResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 喂,你睡着了吗（答题列表）
 *
 * @author zenglb 2016.10.24
 */
public class Rxjava2DemoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private int page;
    private String mParam1;
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private List<AreuSleepResult> data = new ArrayList<>();

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

        mEmptyTipsTxt = (TextView) rootView.findViewById(R.id.tips_txt);
        mEmptyTipsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                springView.callFresh();
            }
        });

        getHttpData(mParam1, page);

        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;

                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    /**
     * 请求答题列表
     */
    private void getHttpData(String mParam1, int page) {
        data.add(new AreuSleepResult("map 的变换操作", "map hddddddddddd", "111"));
        data.add(new AreuSleepResult("zip 的变换操作", "传说中的谷歌四件套，对于用了一段时间的谷歌四件套的开发者们来说，基础应该都已经掌握的差不多了，但是四件套确实很博大精深，要想完全掌握", "222"));

        areUSleepListAdapter.notifyDataSetChanged();
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

}
