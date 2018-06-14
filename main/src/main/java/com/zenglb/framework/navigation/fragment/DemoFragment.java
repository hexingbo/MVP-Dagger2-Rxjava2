package com.zenglb.framework.navigation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zenglb.framework.base.BaseActivity;
import com.zenglb.framework.R;
import com.zenglb.framework.demo.MemoryLeakTest;
import com.zenglb.framework.demo.animal.AnimalMainActivity;
import com.zenglb.framework.demo.quick_input_things.QuickInputThingsActivity;
import com.zenglb.framework.dagger.scope.ActivityScope;
import com.zenglb.framework.http.ApiService;

import com.zenglb.framework.mvp.handylife.AnyLifeActivity;
import com.zenglb.framework.http.result.CustomWeatherResult;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 喂,你睡着了吗（答题列表）
 *
 * https://blog.csdn.net/a_long_/article/details/54829644
 *
 * @author zenglb 2016.10.24
 */
@ActivityScope
public class DemoFragment extends Fragment {

    @Inject
    ApiService apiService;

    @Inject
    public DemoFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @return A new instance of fragment AreUSleepFragment.
//     */
//    public static DemoFragment newInstance(String param1) {
//        DemoFragment fragment = new DemoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        rootView.findViewById(R.id.quickinput).setOnClickListener(v -> {

            ((BaseActivity) getActivity()).startActivity(QuickInputThingsActivity.class);
        });

        //
        rootView.findViewById(R.id.JSBridge).setOnClickListener(v -> {
            ((BaseActivity) getActivity()).goWebView("file:///android_asset/index.html");
        });


        /**
         * 动态替换URL,参数照样的随意的使用啊。
         *
         */
        rootView.findViewById(R.id.java8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.sojson.com/open/api/weather/json.shtml";
                Call<CustomWeatherResult> news = apiService.getWeather(url, "深圳");
                //上面的实现是非常的精巧  http://www.jianshu.com/p/c1a3a881a144

                news.enqueue(new Callback<CustomWeatherResult>() {
                    @Override
                    public void onResponse(Call<CustomWeatherResult> call, Response<CustomWeatherResult> response) {
                        CustomWeatherResult customWeatherResult = response.body();
                        Toast.makeText(getActivity(), "天气：" + customWeatherResult.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<CustomWeatherResult> call, Throwable t) {
                        Toast.makeText(getActivity(), "失败：" + call.toString() + "  异常：" + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        /**
         * mvp
         */
        rootView.findViewById(R.id.mvp).setOnClickListener(v -> {
            ((BaseActivity) getActivity()).startActivity(AnyLifeActivity.class);
        });

        /**
         * 使用lamada 替代，Ctrl+Alt+L 格式化代码 Ctrl+Alt+O 优化导入的类和包 Alt+Insert 生成代码
         */
        rootView.findViewById(R.id.animation).setOnClickListener
                (view -> transitionToActivity(AnimalMainActivity.class, (TextView) rootView.findViewById(R.id.animation), "Material Animations 动画演示"));

        /**
         * jni jni
         */
        rootView.findViewById(R.id.jni).setOnClickListener(v -> {
//            ((BaseActivity) getActivity()).startActivity(MemoryLeakTest.class);
        });


        /**
         * 内存泄漏 以及 内存无法及时释放
         * https://www.jianshu.com/p/0076cb510372
         */
        rootView.findViewById(R.id.memory_leak).setOnClickListener(v -> {
            ((BaseActivity) getActivity()).startActivity(MemoryLeakTest.class);
        });



        /**
         * dragger
         */
        rootView.findViewById(R.id.dragger).setOnClickListener(v -> {
//            ((BaseActivity) getActivity()).startActivity(DraggerActivity.class);
        });

    }


    private void transitionToActivity(Class target, TextView textView, String title) {
//        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), true,
//                new Pair<>(textView, getActivity().getString(R.string.shared_name)));
//
//        startActivity(target, pairs, title);
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
