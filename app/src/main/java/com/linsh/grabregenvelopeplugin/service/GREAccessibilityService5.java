package com.linsh.grabregenvelopeplugin.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.linsh.grabregenvelopeplugin.BuildConfig;
import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.common.Constants;
import com.linsh.grabregenvelopeplugin.model.ClickPerformer;
import com.linsh.grabregenvelopeplugin.page.UIChat;
import com.linsh.grabregenvelopeplugin.page.UILuckyMoneyDetail;
import com.linsh.grabregenvelopeplugin.page.UILuckyMoneyReceive;
import com.linsh.grabregenvelopeplugin.page.UINotification;
import com.linsh.grabregenvelopeplugin.ui.GREWindowManagerHelper;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.LogUtils;
import com.linsh.utilseverywhere.ToastUtils;
import com.linsh.utilseverywhere.tools.AccessibilityHelper;

public class GREAccessibilityService5 extends AccessibilityService {

    public static String sCurActivityName;
    public static int sCurPageType;

    private AccessibilityHelper mHelper;
    private GREWindowManagerHelper mWindowManagerHelper;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 当启动服务的时候就会被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        ToastUtils.showLong("你终于把我召唤粗来啦, 主人 ο(=•ω＜=)ρ⌒☆");
        mHelper = new AccessibilityHelper(this);
        mWindowManagerHelper = new GREWindowManagerHelper();
        mWindowManagerHelper.showFloatingView(this);
        if (BuildConfig.DEBUG) {
            AccessibilityServiceInfo info = getServiceInfo();
            if (info == null) info = new AccessibilityServiceInfo();
            info.flags |= AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS | AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
            setServiceInfo(info);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Config.isExit = false;
        if (mWindowManagerHelper != null) mWindowManagerHelper.showFloatingView(this);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (Config.isExit) return;
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        if (BuildConfig.DEBUG) {
            String typeToString = AccessibilityEvent.eventTypeToString(eventType);
            LogUtils.i("onAccessibilityEvent:", "eventType = " + typeToString, "className = " + className);
            LogUtils.i("allTexts:", mHelper.findAllTexts(event.getSource()));
        }
        switch (eventType) {
            // 通知栏状态改变
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                // UINotification 判断是否是微信红包消息, 如果是则模拟跳转
                UINotification.onNotificationStateChanged(event);
                break;
            // 窗口状态改变
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (className.equals(Constants.UI_MAIN)) { // 主界面 & 聊天界面
                    sCurActivityName = className;
                    sCurPageType = 0;
                    // 由于从微信主界面到微信聊天界面的切换不会发生窗口状态变化,
                    // 所以把这个逻辑放在窗口内容变化那里去
                } else if (className.equals(Constants.UI_LUCKY_MONEY_OPEN)) { // 红包打开界面
                    sCurActivityName = className;
                    sCurPageType = Constants.TYPE_LUCKY_MONEY_OPEN;
                    // 尝试打开红包 (一般情况下查找控件会失败)
                    UILuckyMoneyReceive.openLuckyMoney(this, mHelper);
                } else if (className.equals(Constants.UI_LUCKY_MONEY_DETAIL)) { // 红包详情界面
                    sCurActivityName = className;
                    sCurPageType = Constants.TYPE_LUCKY_MONEY_DETAIL;
                    // 提示并退出红包
                    UILuckyMoneyDetail.exitLuckyMoneyDetail(this, mHelper);
                } else {
                    sCurActivityName = null;
                    sCurPageType = 0;
                }
                break;
            // View 滚动
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (Constants.UI_MAIN.equals(sCurActivityName)) {
                    if (sCurPageType == Constants.TYPE_CHAT) {
                        // 寻找没有打开过的红包, 并查看红包
                        UIChat.findPackets(this, mHelper);
                    }
                }
                break;
            // 窗口内容改变
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if (Constants.UI_MAIN.equals(sCurActivityName)) {
                    if (mHelper.findNodeInfosByViewId(Constants.ID_CHAT_BACK).size() > 0) { // 聊天界面
                        if (sCurPageType != Constants.TYPE_CHAT) {
                            // 寻找没有打开过的红包, 并查看红包
                            UIChat.findPackets(this, mHelper);
                            sCurPageType = Constants.TYPE_CHAT;
                            Log.i("GREAccessibilityService", "sCurPageType: " + sCurPageType);
                        }
                    } else { // 主界面
                        if (sCurPageType != Constants.TYPE_MAIN) {
                            sCurPageType = Constants.TYPE_MAIN;
                            Log.i("GREAccessibilityService", "sCurPageType: " + sCurPageType);
                        }
                    }
                }
                break;
        }
    }

    public void performClick(ClickPerformer performer, long delay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HandlerUtils.postRunnable(performer, delay);
        }
    }

    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {
        ToastUtils.showLong("(＞﹏＜) 我中枪倒地了...");
        mHelper = null;
        mWindowManagerHelper = null;
    }
}
