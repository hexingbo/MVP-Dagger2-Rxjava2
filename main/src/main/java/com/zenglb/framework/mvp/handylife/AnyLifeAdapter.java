package com.zenglb.framework.mvp.handylife;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zenglb.framework.R;

import java.util.List;

/**
 * HandyLifeAdapter
 *
 * Created by zlb on 2018/3/23.
 */
public class AnyLifeAdapter extends BaseMultiItemQuickAdapter<AnyLifeResultBean, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param context
     * @param data
     */
    public AnyLifeAdapter(Context context, List<AnyLifeResultBean> data) {
        super(data);
        addItemType(AnyLifeResultBean.DEFAULT, R.layout.handylife_list_item);
        addItemType(AnyLifeResultBean.IMG_ONLY, R.layout.handylife_list_item_only_image);
    }


    /**
     * data --> UI
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(BaseViewHolder helper, AnyLifeResultBean item) {
        switch (helper.getItemViewType()) {
            case AnyLifeResultBean.DEFAULT:
                helper.setText(R.id.topic, item.getTilte());
                helper.setText(R.id.description, item.getDescription());
                // TODO: 2018/3/24  加载网络图片,应该再封装一层的，防止以后换图片加载库，
                Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.image));
                break;
            case AnyLifeResultBean.IMG_ONLY:
                // TODO: 2018/3/24  加载网络图片,应该再封装一层的，防止以后换图片加载库，
                Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.image));
                break;
        }
    }

}
