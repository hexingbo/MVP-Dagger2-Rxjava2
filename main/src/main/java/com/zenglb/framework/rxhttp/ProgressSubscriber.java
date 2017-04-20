package com.zenglb.framework.rxhttp;

import android.app.ProgressDialog;

/**
 * 默认是不需要有进度条的，如果有的话就是ProgressSubscriber
 *
 * Created by zenglb on 2017/4/20.
 */
@Deprecated
public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> {
    private ProgressDialog dialog;

    protected ProgressSubscriber(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if (dialog != null) dialog.dismiss();
    }
}