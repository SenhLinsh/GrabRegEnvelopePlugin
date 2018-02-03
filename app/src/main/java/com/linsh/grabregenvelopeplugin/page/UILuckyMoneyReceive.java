package com.linsh.grabregenvelopeplugin.page;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.linsh.grabregenvelopeplugin.ConfigHelper;
import com.linsh.grabregenvelopeplugin.Constants;
import com.linsh.grabregenvelopeplugin.GREAccessibilityService7;
import com.linsh.grabregenvelopeplugin.model.ClickPerformer;
import com.linsh.utilseverywhere.LogUtils;
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
public class UILuckyMoneyReceive {

    public static void openLuckyMoney(GREAccessibilityService7 service, AccessibilityHelper helper) {
        checkOpenLuckyMonkeyBtn(helper);
        List<String> allTexts = helper.findAllTexts();
        LogUtils.i("allTexts size=" + allTexts.size(), allTexts);
        ToastUtils.showLong("我要打开红包了哦 ✧(≖ ◡ ≖✿) 兴不兴奋!");

        ClickPerformer clickPerformer = new ClickPerformer(service, ConfigHelper.getOpenLuckyMoneyLocation(), Constants.UI_LUCKY_MONEY_OPEN);
        clickPerformer.setErrorMsg("(;´༎ຶД༎ຶ`) 这个红包我拆不开啊!")
                .setInterval(500)
                .setKey(ConfigHelper.KEY_OPEN_LUCKY_MONEY_LOCATION);
        service.performClick(clickPerformer, 500);
    }

    private static void checkOpenLuckyMonkeyBtn(AccessibilityHelper helper) {
        if (ConfigHelper.isRequireOpenLuckyMoneyLocation()) {
            AccessibilityNodeInfo info = helper.findFirstNodeInfoByViewId("com.tencent.mm:id/a3");
            if (info != null) {
                Rect rect = new Rect();
                info.getBoundsInScreen(rect);
                ConfigHelper.saveLocation(ConfigHelper.KEY_OPEN_LUCKY_MONEY_LOCATION,
                        new Point((rect.right - rect.left) / 2, (rect.bottom - rect.top) / 2));
            }
        }
    }
}
