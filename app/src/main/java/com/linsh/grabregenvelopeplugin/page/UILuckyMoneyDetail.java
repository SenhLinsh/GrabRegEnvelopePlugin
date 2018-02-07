package com.linsh.grabregenvelopeplugin.page;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.grabregenvelopeplugin.common.Constants;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService5;
import com.linsh.grabregenvelopeplugin.model.ClickPerformer;
import com.linsh.utilseverywhere.ToastUtils;
import com.linsh.utilseverywhere.tools.AccessibilityHelper;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class UILuckyMoneyDetail {

    public static void exitLuckyMoneyDetail(GREAccessibilityService5 service, AccessibilityHelper helper) {
        checkLuckyMonkeyDetailBackBtn(helper);
        int time = Constants.TIME_EXIT_LUCKY_MONEY_UI;
        ToastUtils.showLong(String.format("这里没什么好留恋的,我准备%s秒后回去继续抢红包哦 (๑•̀ㅂ•́)و✧", time / 1000f));

        ClickPerformer clickPerformer = new ClickPerformer(service, ConfigHelper.getExitLuckyMoneyDetailLocation(), Constants.UI_LUCKY_MONEY_DETAIL);
        clickPerformer.setInterval(Constants.TIME_INTERVAL_PERFORM_CLICK)
                .setKey(ConfigHelper.KEY_EXIT_LUCKY_MONEY_DETAIL_LOCATION);
        service.performClick(clickPerformer, time);
    }

    private static void checkLuckyMonkeyDetailBackBtn(AccessibilityHelper helper) {
        if (ConfigHelper.isRequireExitLuckyMoneyDetailLocation()) {
            AccessibilityNodeInfo info = helper.findFirstNodeInfoByViewId("com.tencent.mm:id/hp");
            if (info != null) {
                Rect rect = new Rect();
                info.getBoundsInScreen(rect);
                ConfigHelper.saveLocation(ConfigHelper.KEY_EXIT_LUCKY_MONEY_DETAIL_LOCATION,
                        new Point((rect.right - rect.left) / 2, (rect.bottom - rect.top) / 2));
            }
        }
    }
}
