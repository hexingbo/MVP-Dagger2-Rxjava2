package com.zenglb.framework.activity.architecture;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zenglb.framework.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@Deprecated
public class UserProfileFragment extends Fragment {

    public static  final String TAG=UserProfileFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String UID_KEY = "userId";

    // TODO: Rename and change types of parameters
    private String userId;

    private UserProfileViewModel viewModel;

    private TextView infoTxt;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(UID_KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
            userId  = getArguments().getString(UID_KEY);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initViews(rootView);
        return rootView;
    }


    /**
     * 初始化所有的视图
     *
     * @param rootView
     */
    protected void initViews(View rootView) {

        infoTxt = (TextView) rootView.findViewById(R.id.user_profile);
        infoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"dsafdsafdsafdas");
                //这里尝试改变一下数据看看会不会自动的改变
            }
        });

    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        //ViewModel 对象的生成很有趣味
//        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
//        viewModel.init(userId);
//        viewModel.getUser().observe(this, new Observer<StaffMsg>() {
//            @Override
//            public void onChanged(@Nullable StaffMsg staffMsg) {
//                infoTxt.setText(staffMsg.toString());  //
//                // update UI
//                Log.e(TAG,staffMsg.toString());
//            }
//        });
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
