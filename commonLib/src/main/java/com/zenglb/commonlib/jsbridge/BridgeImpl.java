package com.zenglb.commonlib.jsbridge;

import android.content.Intent;
import android.webkit.WebView;
import android.widget.Toast;
import com.zenglb.commonlib.base.BaseWebViewActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个类中的方法都是JS 通过Bridge 来调用的，使用前需要注入
 * 里面的所有的方法（除了方法名字）都必须满足一定的规则！
 */
public class BridgeImpl implements IBridge {
    private static Map<Integer,Callback> callbackCache=new HashMap();
    public static String fliterTAG="MY.intent.action.XXXXXXXXXX";

    /**
     * 从缓存的CallBack cache 中移除某个callback!
     */
    public static Callback getCallback(Integer key) {
        Callback callback=callbackCache.get(key);
        callbackCache.remove(key);
        return callback;
    }

    /**
     * 调用新的Activity处理需要的数据结果，需要在Activity接收数据
     * @param webView
     * @param tag
     */
    private static void callNewActForResult(WebView webView,int tag){
        Intent intent=new Intent();
        intent.setAction(fliterTAG);
        intent.putExtra("code",tag);
        webView.getContext().sendBroadcast(intent);
    }

    /**
     *
     *
     * @param webView
     * @param param
     * @param callback
     */
    public static void getImage(WebView webView, JSONObject param, final Callback callback) {
        callbackCache.put(BaseWebViewActivity.GET_IMG_REQUEST_CODE,callback);
        callNewActForResult(webView,BaseWebViewActivity.GET_IMG_REQUEST_CODE);
    }


    /**
     * 扫码回传给JS，这个处理和其他的有些不一样，需要新启动一个Activity 来获取数据
     *
     * @param webView
     * @param param
     * @param callback
     */
    public static void scanQRCode(WebView webView, JSONObject param, final Callback callback) {
        callbackCache.put(BaseWebViewActivity.ZXING_REQUEST_CODE,callback);
        callNewActForResult(webView,BaseWebViewActivity.ZXING_REQUEST_CODE);
    }


    public static void showToast(WebView webView, JSONObject param, final Callback callback) {
        String message = param.optString("msg");
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
        if (null != callback) {
            try {
                JSONObject object = new JSONObject();
                object.put("key", "value11111111");
                callback.apply(returnJSONObject(0, "ok", object));  //这里回调js 没有任何的意义呀！
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testThread(WebView webView, JSONObject param, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    JSONObject object = new JSONObject();
                    object.put("key", "value");
                    callback.apply(returnJSONObject(0, "ok", object));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     *
     * @param code
     * @param msg
     * @param result
     * @return
     */
    public static JSONObject returnJSONObject(int code, String msg, JSONObject result) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("msg", msg);
            object.putOpt("result", result);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
