package com.zenglb.framework.status_callback;


import com.kingja.loadsir.callback.Callback;
import com.zenglb.framework.R;


/**
 * Description:TODO
 * Create Time:2017/9/4 10:20
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }
}
