package com.linsh.grabregenvelopeplugin;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.linsh.grabregenvelopeplugin.model.ClickPerformer;
import com.linsh.grabregenvelopeplugin.page.UIChat;
import com.linsh.grabregenvelopeplugin.page.UILuckyMoneyDetail;
import com.linsh.grabregenvelopeplugin.page.UILuckyMoneyReceive;
import com.linsh.grabregenvelopeplugin.page.UINotification;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.LogUtils;
import com.linsh.utilseverywhere.ToastUtils;
import com.linsh.utilseverywhere.tools.AccessibilityHelper;

public class GREAccessibilityService7 extends AccessibilityService {

    public static String sCurActivityName;

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

    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String typeToString = AccessibilityEvent.eventTypeToString(eventType);
        String className = event.getClassName().toString();
        String packageName = event.getPackageName().toString();
        LogUtils.i("onAccessibilityEvent:", "eventType = " + typeToString, "PackageName = " + packageName, "className = " + className);
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
                    // 寻找没有打开过的红包, 并查看红包
                    UIChat.findPackets(this, mHelper);
                } else if (className.equals(Constants.UI_LUCKY_MONEY_OPEN)) { // 红包打开界面
                    sCurActivityName = className;
                    // 尝试打开红包 (一般情况下查找控件会失败)
                    UILuckyMoneyReceive.openLuckyMoney(this, mHelper);
                } else if (className.equals(Constants.UI_LUCKY_MONEY_DETAIL)) { // 红包详情界面
                    sCurActivityName = className;
                    // 提示并退出红包
                    UILuckyMoneyDetail.exitLuckyMoneyDetail(this, mHelper);
                } else {
                    sCurActivityName = null;
                }
                break;
            // View 滚动
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                if (className.equals(Constants.UI_MAIN)) {
                    Log.i("LshLog", "onAccessibilityEvent: ");
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                break;
        }
    }

    public void performClick(ClickPerformer performer, long delay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HandlerUtils.postRunnable(performer, delay);
        }
    }

    public void showLocationView(Point point, String key) {
        mWindowManagerHelper.showLocationView(this, point, key);
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
