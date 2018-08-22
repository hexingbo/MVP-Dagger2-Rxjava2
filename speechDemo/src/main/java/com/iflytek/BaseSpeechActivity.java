package com.iflytek;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 语音识别,不使用默认的识别UI，设置细节封装在这里
 * <p>
 * <p>
 * <p>
 * <p>
 * https://doc.xfyun.cn/msc_android/index.html
 */
public abstract class BaseSpeechActivity extends AppCompatActivity {
    private static String TAG = BaseSpeechActivity.class.getSimpleName();

    // 语音听写对象
    public SpeechRecognizer mIat;

    // 用HashMap存储听写结果
    public HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    public List<Integer> mVolumeList;  //音量List

    public AudioWavePopupView audioWavePopupView;  //声音指示器

    public abstract void showSpeechResult(String result);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(BaseSpeechActivity.this, mInitListener);
    }


    /**
     * 开始语音识别
     */
    public void startAISpeech(View parent) {
        //移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(BaseSpeechActivity.this, "iat_recognize");

        mIatResults.clear();
        // 设置参数
        setParam();


        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            audioWavePopupView = new AudioWavePopupView(this);
            mVolumeList = audioWavePopupView.getmVolumeList();

            audioWavePopupView.showPopupWindow(parent, BaseSpeechActivity.this);
            audioWavePopupView.startWaveView();
        }

    }


    int ret = 0; // 函数调用返回值

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = code -> {
        if (code != ErrorCode.SUCCESS) {
            showTip("初始化失败，错误码：" + code);
        }
    };


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            audioWavePopupView.setTips("请开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            String errorTips = error.getPlainDescription(true);

            showTip(errorTips);
            audioWavePopupView.setTips(errorTips);
            audioWavePopupView.dismiss();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            audioWavePopupView.setTips("正在语音转文字");


        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);
            if (isLast) {
                audioWavePopupView.dismiss();
            }
        }

        /**
         * 只有声音的大小改变了才会有变动，那么是否应该倒计时的加入上一没有更改的声音
         *
         * @param volume
         * @param data
         */
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if (mVolumeList != null && mVolumeList.size() > 180) {
                mVolumeList.remove(0);
            }
            mVolumeList.add(volume);  //需要限制长度。
        }


        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        showSpeechResult(resultBuffer.toString());

        showTip(resultBuffer.toString());
    }


    public void showTip(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");


        String lag = "zh_cn"; //默认都是中文
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "4000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }


}
