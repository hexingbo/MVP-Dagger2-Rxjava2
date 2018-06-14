package com.zenglb.framework.mvp.task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.zenglb.framework.R;
import com.zenglb.framework.base.mvp.BaseMVPActivity;
import com.zenglb.framework.demo.demo.AreUSleepListAdapter;
import com.zenglb.framework.http.result.JokesResult;
import com.zenglb.framework.persistence.dbmaster.DaoSession;
import com.zenglb.framework.UIStatus.EmptyCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 *  反例，准备cut
 *
 */
@Deprecated
public class TaskMVPActivity extends BaseMVPActivity implements TaskContract.TaskView {
    /**
     * 接收view 层的网络数据请求，并分发给对应的Model层处理，同时监听Model层的处理结果，
     * 最终反馈给View 层，从而实现界面的刷新
     */
    private TextView mShowTxt;
    private String TAG = TaskMVPActivity.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private int page=1;
    private String mParam1;
    private SpringView springView;
    private RecyclerView mRecyclerView = null;
    private AreUSleepListAdapter areUSleepListAdapter;
    private List<JokesResult> data = new ArrayList<>();

    @Inject
    DaoSession daoSession;
    @Inject
    TaskPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoSession.toString();
    }


    @Override
    protected void onHttpReload(View v) {
        super.onHttpReload(v);
        mPresenter.getRemoteTasks("expired", 1); //重新加载
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Bind view to the presenter which will signal for the presenter to load the task.
        mPresenter.takeView(this);  //NEED base
    }

    @Override
    public void onPause() {
        mPresenter.dropView();  //Need BASE
        super.onPause();
    }

    /**
     * 拿到数据了，刷新ListView UI
     */
    public void showTasks(List<JokesResult> jokesResultList) {
        springView.onFinishFreshAndLoad();

        if (jokesResultList != null) {
            if (page <= 1) data.clear();

            if (jokesResultList != null && jokesResultList.size() != 0) {
                for(int i=0;i<jokesResultList.size();i++){
                    jokesResultList.get(i).setTopic(" # "+i+" #"+jokesResultList.get(i).getTopic());
                }


                data.addAll(jokesResultList);
                page++; //自动 的加1

                areUSleepListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this.getApplicationContext(), "暂无数据，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * mBaseLoadService，很方便的就能展示空啊什么的；
         *
         */
        if (data == null || data.size() == 0) {
            mBaseLoadService.showCallback(EmptyCallback.class);
        } else {
            mBaseLoadService.showSuccess();
        }
    }

    @Override
    public void getTaskFailed(int code,String message) {
        springView.onFinishFreshAndLoad();
        showTasks(null);
    }


    /**
     * 显示缓存的20条数据
     */
    public void showCacheTasks(List<JokesResult> jokesResultList) {
        if (jokesResultList != null && jokesResultList.size() != 0) {
            data.addAll(jokesResultList);
            areUSleepListAdapter.notifyDataSetChanged();
//            mEmptyTipsTxt.setVisibility(View.GONE);
        } else {
//            mEmptyTipsTxt.setVisibility(View.VISIBLE);
            Log.d(TAG, "No Cache data !");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    protected void initViews() {
        setToolBarTitle("mvp+rxjava2+dagger2.android");

        mShowTxt = (TextView) findViewById(R.id.tips_txt);

        areUSleepListAdapter = new AreUSleepListAdapter(this, data);
        areUSleepListAdapter.setOnItemClickListener(new AreUSleepListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AreUSleepListAdapter.ViewHolder view, int position) {
                data.remove(position);//删除数据源
                areUSleepListAdapter.notifyItemRemoved(position);//刷新被删除的地方
            }

            @Override
            public void onItemLongClick(AreUSleepListAdapter.ViewHolder view, int position) {
                //测试动画
//                transitionToActivity(SharedElementActivity.class, view, data.get(position));
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
//                int pageTemp=11111;   //只是为了测试方便
                mPresenter.getRemoteTasks("expired", page);
            }

            @Override
            public void onLoadmore() {
                mPresenter.getRemoteTasks("expired", page);
            }

        });

        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));

        new Handler().postDelayed(() -> springView.callFresh(), 500);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        mPresenter.getCacheTasks();
//        mPresenter.getRemoteTasks("expired", page);
    }

    /**
     * 带动画的跳转,
     *
     * @param target
     * @param viewHolder
     * @param jokesResult
     */
    private void transitionToActivity(Class target, AreUSleepListAdapter.ViewHolder viewHolder, JokesResult jokesResult) {
//        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true,
//                new Pair<>(viewHolder.getTopic(), mContext.getString(R.string.shared_name)),
//                new Pair<>(viewHolder.getTime(), mContext.getString(R.string.shared_time)));
//
//        startActivity(target, pairs, jokesResult);
    }


    /**
     * 带动画的Activity 跳转
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
