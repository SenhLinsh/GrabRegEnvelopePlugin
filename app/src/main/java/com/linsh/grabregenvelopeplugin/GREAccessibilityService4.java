package com.linsh.grabregenvelopeplugin;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.LogUtils;
import com.linsh.utilseverywhere.tools.AccessibilityHelper;

import java.util.ArrayList;
import java.util.List;

public class GREAccessibilityService4 extends AccessibilityService {

    private AccessibilityHelper mHelper;
    private String mCurActivityName;

    @Override
    public void onCreate() {
        super.onCreate();
        mHelper = new AccessibilityHelper(this);
    }

    /**
     * 当启动服务的时候就会被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.i("onServiceConnected: 服务启动");
        AccessibilityServiceInfo info = getServiceInfo();
        if (info == null) info = new AccessibilityServiceInfo();
        info.flags |= AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS | AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        setServiceInfo(info);
    }

    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String typeToString = AccessibilityEvent.eventTypeToString(eventType);
        LogUtils.i("onAccessibilityEvent:", "eventType = " + typeToString, "PackageName = " + event.getPackageName(), "className = " + event.getClassName());
        switch (eventType) {
            //当通知栏发生改变时
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                onNotificationStateChanged(event);
                break;
            //当窗口的状态发生改变时
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                onWindowStateChanged(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;
        }
    }

    private void onNotificationStateChanged(AccessibilityEvent event) {
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
                            LogUtils.i("进入微信");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void onWindowStateChanged(final AccessibilityEvent event) {
        String className = event.getClassName().toString();
        switch (className) {
            // 主界面 & 聊天界面
            case Constants.UI_MAIN:
                mCurActivityName = className;
                // 寻找没有打开过的红包, 并查看红包
                findPacketsAndOpen();
                break;
            // 红包界面
            case Constants.UI_LUCKY_MONEY_OPEN:
                mCurActivityName = className;
                // 打开红包
                openLuckyMoney();
                break;
            // 红包详情界面
            case Constants.UI_LUCKY_MONEY_DETAIL:
                mCurActivityName = className;
                // 退出红包
                // 暂时不进行自动退出红包详情界面, 自己操作, 毕竟不是 root 放养操作
                // inputClick("com.tencent.mm:id/gd");
                break;
            default:
                mCurActivityName = null;
                break;
        }
    }

    private void openLuckyMoney() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Constants.UI_LUCKY_MONEY_OPEN.equals(mCurActivityName)) {
            Path path = new Path();
            Point location = ConfigHelper.getOpenLuckyMoneyLocation();
            path.moveTo(location.x, location.y);
            GestureDescription gesture = new GestureDescription.Builder()
                    .addStroke(new GestureDescription.StrokeDescription(path, 100, 50))
                    .build();
            dispatchGesture(gesture, null, null);
            List<String> allTexts = mHelper.findAllTexts();
            LogUtils.i("allTexts size=" + allTexts.size(), allTexts);
            HandlerUtils.postRunnable(new Runnable() {
                @Override
                public void run() {
                    openLuckyMoney();
                }
            }, 500);
        }
    }

    /**
     * 打开第一个红包
     */
    private void findPacketsAndOpen() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> parents = new ArrayList<>();
        recycle(parents, rootNode);
        if (parents.size() > 0) {
            parents.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 回归函数遍历每一个节点，并将含有"领取红包"存进List中
     */
    public List<AccessibilityNodeInfo> recycle(List<AccessibilityNodeInfo> parents, AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("领取红包".equals(info.getText().toString())) {
                    if (info.isClickable()) {
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    AccessibilityNodeInfo parent = info.getParent();
                    while (parent != null) {
                        if (parent.isClickable()) {
                            parents.add(parent);
                            break;
                        }
                        parent = parent.getParent();
                    }
                }
            }
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(parents, info.getChild(i));
                }
            }
        }
        return parents;
    }

    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {
    }
}
