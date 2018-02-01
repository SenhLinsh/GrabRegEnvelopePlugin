package com.linsh.grabregenvelopeplugin;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class GREAccessibilityService extends AccessibilityService {

    private List<AccessibilityNodeInfo> parents;
    private GREStatus mStatus;
    private int tryOpenAgainTimes;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LshLog", "onCreate: 服务启动");
    }

    /**
     * 当启动服务的时候就会被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        parents = new ArrayList<>();
    }

    /**
     * 监听窗口变化的回调
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i("LshLog", "onAccessibilityEvent: PackageName=" + event.getPackageName() + "   className=" + event.getClassName());
        int eventType = event.getEventType();
        switch (eventType) {
            //当通知栏发生改变时
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
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
                                    mStatus = GREStatus.NEW_ONE;
                                    Log.i("LshLog", "进入微信");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            //当窗口的状态发生改变时
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.i("LshLog", "onAccessibilityEvent: TYPE_WINDOW_STATE_CHANGED");
                onStateChanged(event);
                break;
        }
    }

    private void onStateChanged(AccessibilityEvent event) {
        String className = event.getClassName().toString();
        Log.i("LshLog", "onStateChanged: className=" + event.getClassName().toString());

        if (className.equals("com.tencent.mm.ui.LauncherUI")) {
            if (mStatus == GREStatus.OPENED) return;

            //点击最后一个红包
            Log.i("LshLog", "打开红包");
            getLastPacket();
        } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f")) {
            if (mStatus != GREStatus.OPENING) return;

            //开红包
            Log.i("LshLog", "拆红包");
            inputClick("com.tencent.mm:id/bjj");
        } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
            //退出红包
//            Log.i("LshLog", "退出红包");
//            inputClick("com.tencent.mm:id/gd");
        }
    }

    /**
     * 通过ID获取控件，并进行模拟点击
     */
    private void inputClick(String clickId) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(clickId);
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                mStatus = GREStatus.OPENED;
                Log.i("LshLog", "inputClick: 模拟点击拆红包");
                tryOpenAgainTimes = 0;
            }
            if (list.size() == 0) {
                tryOpenAgain();
            }
        } else {
            tryOpenAgain();
        }
    }

    private void tryOpenAgain() {
        if (mStatus != GREStatus.OPENING) return;
        Log.i("LshLog", "run: tryOpenAgain");
    }

    /**
     * 获取List中最后一个红包，并进行模拟点击
     */
    private void getLastPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        recycle(rootNode);
        if (parents.size() > 0) {
            parents.get(parents.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            mStatus = GREStatus.OPENING;
        }
    }

    /**
     * 回归函数遍历每一个节点，并将含有"领取红包"存进List中
     */
    public void recycle(AccessibilityNodeInfo info) {
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
                    recycle(info.getChild(i));
                }
            }
        }
    }

    /**
     * 中断服务的回调
     */
    @Override
    public void onInterrupt() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private enum GREStatus {
        NEW_ONE, OPENING, OPENED
    }
}
