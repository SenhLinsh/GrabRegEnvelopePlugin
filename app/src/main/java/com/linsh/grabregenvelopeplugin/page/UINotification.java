package com.linsh.grabregenvelopeplugin.page;

import android.app.Notification;
import android.app.PendingIntent;
import android.service.notification.StatusBarNotification;
import android.view.accessibility.AccessibilityEvent;

import com.linsh.utilseverywhere.ToastUtils;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class UINotification {

    public static void onNotificationStateChanged(AccessibilityEvent event) {
        // 通知发生变化, 查看是否有红包相关信息, 有则点开
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                if (content.contains("[微信红包]")) {
                    //模拟打开通知栏消息，即打开微信
                    if (event.getParcelableData() != null &&
                            event.getParcelableData() instanceof Notification) {
                        Notification notification = (Notification) event.getParcelableData();
                        PendingIntent pendingIntent = notification.contentIntent;
                        try {
                            pendingIntent.send();
                            ToastUtils.show("发现目标 ヾ(≧∇≦*)ゝ 火速赶往现场 ~");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void onNotificationPosted(StatusBarNotification sbn) {
        String text = sbn.getNotification().tickerText.toString();
        if (text.contains("[微信红包]")) {
            Notification notification = sbn.getNotification();
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
                ToastUtils.show("发现目标 ヾ(≧∇≦*)ゝ 火速赶往现场 ~");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
