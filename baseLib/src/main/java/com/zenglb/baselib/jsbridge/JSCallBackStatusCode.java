package com.zenglb.baselib.jsbridge;

/**
 * 其实大多数的失败/CANCEL 都是没有必要的...
 *
 * Created by zenglb on 2017/3/14.
 */
public class JSCallBackStatusCode {
    public final static int JS_CALL_BACK_SUCCESS = 0;      //所有的成功

    public final static int SCAN_QR_CODE_CANCEL  = 100;    //扫码取消
    public final static int TAKE_IMG_CODE_CANCEL = 101;    //获取照片取消


}
