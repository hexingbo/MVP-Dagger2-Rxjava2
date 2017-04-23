package com.zenglb.framework.fragment.others;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.demo.DemoActivity;
import com.zenglb.framework.service.TestRxIntentService;

/**
 * 喂,你睡着了吗（答题列表）
 *
 * @author zenglb 2016.10.24
 */
public class DemoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public DemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AreUSleepFragment.
     */
    public static DemoFragment newInstance(String param1) {
        DemoFragment fragment = new DemoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_demo, container, false);
        viewsInit(rootView);

        return rootView;
    }

    /**
     * init views
     *
     * @param
     */
    private void viewsInit(View rootView) {
        rootView.findViewById(R.id.jsbridge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).goWebView("file:///android_asset/index.html");
//                ((BaseActivity) getActivity()).goWebView("file:///android_asset/yahoo.pdf");
            }
        });

        /**
         * demo Act
         */
        rootView.findViewById(R.id.animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).setIntentClass(DemoActivity.class);
            }
        });


        /**
         * jni jni
         */
        rootView.findViewById(R.id.jni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestRxIntentService.start(getActivity());
            }
        });


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
