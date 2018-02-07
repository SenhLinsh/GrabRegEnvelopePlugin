package com.linsh.grabregenvelopeplugin.page;

import android.view.accessibility.AccessibilityNodeInfo;

import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService5;
import com.linsh.utilseverywhere.ToastUtils;
import com.linsh.utilseverywhere.tools.AccessibilityHelper;

import java.util.ArrayList;
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

    public static void findPackets(GREAccessibilityService5 service, AccessibilityHelper helper) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode != null) {
            final List<AccessibilityNodeInfo> parents = new ArrayList<>();
            recycle(parents, rootNode);
            if (parents.size() > 0) {
                ToastUtils.show("找到一个红包 φ(゜▽゜*)♪ 正在召唤出来");
                parents.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    /**
     * 回归函数遍历每一个节点，并将含有"领取红包"存进List中
     */
    public static List<AccessibilityNodeInfo> recycle(List<AccessibilityNodeInfo> parents, AccessibilityNodeInfo info) {
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
}
