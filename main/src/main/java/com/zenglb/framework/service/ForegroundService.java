package com.zenglb.framework.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.zenglb.framework.R;
import com.zenglb.framework.navigation.MainActivityBottomNavi;


/**
 * 前台Service，提高进程优先级，在系统内存不足的时候不会那么容易的被限制活动
 * <p>
 * 在HomeActivity 和 Web调用拍照的时候Start.(WEB 拍照的时候因为拍照需要消耗大量的内存，如果在过程中来电话等打断很可能助这儿因为内存不足进程优先级不高被限制活动了)
 * <p>
 * Created by zenglb on 2017/3/15.
 */
public class ForegroundService extends Service {
    private static final String LOG_TAG = "ForegroundService";
    public static final int FOREGROUND_SERVICE_NOTIFICATION_ID = 9372;
    public static final String MAIN_ACTION = "com.zenglb.framework.action.main";
    public static final String START_FOREGROUND_ACTION = "com.zenglb.framework.action.startforeground";
    public static final String STOP_FOREGROUND_ACTION = "com.zenglb.framework.action.stopforeground";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && START_FOREGROUND_ACTION.equals(intent.getAction())) {
            Intent notificationIntent = new Intent(this, MainActivityBottomNavi.class);
            notificationIntent.setAction(MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            RemoteViews notificationView = new RemoteViews(this.getPackageName(), R.layout.notification);

//            // And now, building and attaching the Play button.
//            Intent buttonPlayIntent = new Intent(this, NotificationPlayButtonHandler.class);
//            buttonPlayIntent.putExtra("action", "togglePause");
//
//            PendingIntent buttonPlayPendingIntent = pendingIntent.getBroadcast(this, 0, buttonPlayIntent, 0);
//            notificationView.setOnClickPendingIntent(R.id.notification_button_play, buttonPlayPendingIntent);


            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("AppFrameWork运行中...")
                    .setTicker("AppFrameWork运行中...")
                    .setContentText("AppFrameWork")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContent(notificationView)
                    .setOngoing(true).build();

            startForeground(FOREGROUND_SERVICE_NOTIFICATION_ID,
                    notification);
        } else if (intent.getAction().equals(STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    public static class NotificationCloseButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    /**
     * Called when user clicks the "play/pause" button on the on-going system Notification.
     */
    public static class NotificationPlayButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


}