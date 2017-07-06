package com.zenglb.framework.activity.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.baselib.utils.TransitionHelper;
import com.zenglb.framework.R;
import com.zenglb.framework.activity.animal.SharedElementActivity;
import com.zenglb.framework.activity.main.AreUSleepListAdapter;
import com.zenglb.framework.retrofit.result.JokesResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的MVP，数据可以从网络或者DB 里面获取
 *
 * Greendao3 还不支持Rxjava2,哎.....
 *
 */
public class MVPActivity extends BaseActivity implements MainView {
    /**
     * 接收view 层的网络数据请求，并分发给对应的Model层处理，同时监听Model层的处理结果，
     * 最终反馈给View 层，从而实现界面的刷新
     */
    private MainPresenter presenter;
    private TextView mShowTxt;

    private static final String ARG_PARAM1 = "param1";
    private TextView mEmptyTipsTxt;
    private int page;
    private String mParam1;
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private ArrayList<JokesResult> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * MainView
         */
        presenter = new MainPresenter();
        presenter.addTaskListener(this);  //这个这个这个这个这个
        presenter.getString();
    }


    /**
     * 显示数据
     *
     * @param json
     */
    @Override
    public void onShowString(String json) {
        mShowTxt.setText(json);
    }


    /**
     * 拿到数据了，刷新ListView UI
     */
    @Override
    public void onRefreshListView(List<JokesResult> jokesResultList) {
        springView.onFinishFreshAndLoad();
        if (jokesResultList != null) {

            if (page <= 1) data.clear();

            if (jokesResultList != null && jokesResultList.size() != 0) {
                data.addAll(jokesResultList);
                page++; //自动 的加11111111
                areUSleepListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this.getApplicationContext(), "暂无数据，请稍后再试！", Toast.LENGTH_SHORT).show();
            }

        }

        if (data == null || data.size() == 0) {
            mEmptyTipsTxt.setVisibility(View.VISIBLE);
        } else {
            mEmptyTipsTxt.setVisibility(View.GONE);
        }

    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    protected void initViews() {
        setToolBarTitle("MVP  (Rxjava");

        mShowTxt = (TextView) findViewById(R.id.tips_txt);

        areUSleepListAdapter = new AreUSleepListAdapter(this, data);
        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AreUSleepListAdapter.ViewHolder view, int position) {
                transitionToActivity(SharedElementActivity.class, view, data.get(position));
            }

            @Override
            public void onItemLongClick(AreUSleepListAdapter.ViewHolder view, int position) {

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(areUSleepListAdapter);

        springView = (SpringView) findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getRefreshData("expired",page);
            }

            @Override
            public void onLoadmore() {
                presenter.getRefreshData("expired",page);
            }

        });

        mEmptyTipsTxt = (TextView) findViewById(R.id.tips_txt);
        mEmptyTipsTxt.setOnClickListener(view -> springView.callFresh());

        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
    }


    /**
     * 带动画的跳转,
     *
     * @param target
     * @param viewHolder
     * @param jokesResult
     */
    private void transitionToActivity(Class target, AreUSleepListAdapter.ViewHolder viewHolder, JokesResult jokesResult) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true,
                new Pair<>(viewHolder.getTopic(), mContext.getString(R.string.shared_name)),
                new Pair<>(viewHolder.getTime(), mContext.getString(R.string.shared_time)));

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
        Intent i = new Intent(mContext, target);
        i.putExtra("jokesResult", jokesResult);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }


}
