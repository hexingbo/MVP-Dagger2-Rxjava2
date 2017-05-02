package com.zenglb.baselib.jsbridge;

/**
 * 其实大多数的失败/CANCEL 都是没有必要的...
 *
 * Created by zenglb on 2017/3/14.
 */
public class JSCallBackStatusCode {

    public final static int NATIVE_FUNCTION_NULL   = -1;      //JS 调用了并不存在的本地方法
    public final static int NATIVE_BRIDGENAME_NULL = -2;      //JS NATIVECLANAME IS WRONG


    public final static int JS_CALL_BACK_SUCCESS = 0;      //所有的成功

    public final static int SCAN_QR_CODE_CANCEL  = 100;    //扫码取消
    public final static int TAKE_IMG_CODE_CANCEL = 101;    //获取照片取消


}
