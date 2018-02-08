package com.linsh.grabregenvelopeplugin.page;

import android.view.accessibility.AccessibilityNodeInfo;

import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService;
import com.linsh.utilseverywhere.ToastUtils;
import com.linsh.utilseverywhere.tools.AccessibilityHelper;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class UIChat {

    public static void findPackets(GREAccessibilityService service, AccessibilityHelper helper) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode != null) {
            AccessibilityNodeInfo info = recycle(rootNode);
            if (info != null) {
                GREAccessibilityService.isOpening = true;
                ToastUtils.show("找到一个红包 φ(゜▽゜*)♪ 正在召唤出来");
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 回归函数遍历每一个节点，并将含有"领取红包"存进List中
     */
    public static AccessibilityNodeInfo recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("领取红包".equals(info.getText().toString())) {
                    if (info.isClickable()) {
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    AccessibilityNodeInfo parent = info.getParent();
                    while (parent != null) {
                        if (parent.isClickable()) {
                            return parent;
                        }
                        parent = parent.getParent();
                    }
                }
            }
        } else {
            for (int i = info.getChildCount() - 1; i >= 0; i--) {
                if (!GREAccessibilityService.isOpening) {
                    AccessibilityNodeInfo child = info.getChild(i);
                    if (child != null) {
                        AccessibilityNodeInfo recycle = recycle(child);
                        if (recycle != null)
                            return recycle;
                    }
                }
            }
        }
        return null;
    }

    public static void checkBackId(AccessibilityHelper helper) {
        if (Config.sIsIdChatBackNeeded) {
            List<AccessibilityNodeInfo> list = helper.findNodeInfosByContentDescriptions("返回");
            if (list.size() == 1) {
                Config.sIsIdChatBackNeeded = false;
                Config.sIdChatBack = list.get(0).getViewIdResourceName();
                ConfigHelper.saveIdChatBack(Config.sIdChatBack);
            }
        }
    }
}
