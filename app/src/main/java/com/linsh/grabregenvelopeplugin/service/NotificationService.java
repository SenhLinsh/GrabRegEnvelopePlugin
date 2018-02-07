package com.linsh.grabregenvelopeplugin.service;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.page.UINotification;
import com.linsh.utilseverywhere.LogUtils;

public class NotificationService extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LshLog", "NotificationService onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LshLog", "NotificationService onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("LshLog", "NotificationService onBind: ");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (Config.isExit) return;
        Log.i("LshLog", "NotificationService onNotificationPosted: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ("com.tencent.mm".equals(sbn.getPackageName())) {
                UINotification.onNotificationPosted(sbn);
                LogUtils.i("title", sbn.getNotification().extras.get("android.title"));
                LogUtils.i("text", sbn.getNotification().extras.get("android.text"));

            }
//            Log.i("log", "open" + "-----" + sbn.getPackageName()); // 包名
//            Log.i("log", "open" + "------" + sbn.getNotification().tickerText); // 即时消息
//            Log.i("log", "open" + "-----" + sbn.getNotification().extras.get("android.title")); // 标题
//            Log.i("log", "open" + "-----" + sbn.getNotification().extras.get("android.text")); // 文本
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.i("log", "remove" + "-----" + sbn.getPackageName());
    }
}