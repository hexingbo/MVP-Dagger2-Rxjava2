package com.zenglb.framework.activity.alarmManger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.zenglb.framework.http.HttpCall;
import com.zlb.httplib.core.rxUtils.SwitchSchedulers;
import com.zenglb.framework.http.result.JokesResult;
import com.zlb.httplib.core.BaseObserver;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * 定时任务的提醒
 *
 * Created by zlb on 2017/11/10.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AlarmMangerActivity.ALARM_ACTION)) {
            //定时任务
            Toasty.info(context,"收到定时任务后台广播", Toast.LENGTH_SHORT).show();

            Log.e("收到定时任务后台广播","hahahahahahha");
            getJokes();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent bcIntent = new Intent(AlarmMangerActivity.ALARM_ACTION);
            bcIntent.putExtra("id", 1234);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 3456, bcIntent, PendingIntent
                    .FLAG_CANCEL_CURRENT);

            // pendingIntent 为发送广播。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // >23
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+10000, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // >19
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000, pendingIntent);
            }


        }
    }



    /**
     * Rxjava2 + retrofit 基本
     */
    private void getJokes() {
        HttpCall.getApiService().getJokes("expired", 1)
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<List<JokesResult>>(null, false) {
                    @Override
                    public void onSuccess(List<JokesResult> areuSleepResults) {
                        Log.e("3232", areuSleepResults.toString());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
    }


}