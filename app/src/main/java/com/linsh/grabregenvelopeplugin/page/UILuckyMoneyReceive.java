package com.linsh.grabregenvelopeplugin.page;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.grabregenvelopeplugin.common.Constants;
import com.linsh.grabregenvelopeplugin.model.ClickPerformer;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService;
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
public class UILuckyMoneyReceive {

    public static void openLuckyMoney(GREAccessibilityService service, AccessibilityHelper helper) {
        checkOpenLuckyMonkeyBtn(helper); // 检查『打开』红包按钮位置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ToastUtils.showLong("我要打开红包了哦 ✧(≖ ◡ ≖✿) 兴不兴奋!");
            ClickPerformer clickPerformer = new ClickPerformer(service, ConfigHelper.getOpenLuckyMoneyLocation(), Constants.UI_LUCKY_MONEY_OPEN);
            clickPerformer.setErrorMsg("(;´༎ຶД༎ຶ`) 这个红包我拆不开啊!")
                    .setInterval(Config.sTimeIntervalPerformClick)
                    .setKey(ConfigHelper.KEY_OPEN_LUCKY_MONEY_LOCATION);
            service.performClick(clickPerformer, ConfigHelper.getOpenLuckyMoneyTime());
        } else {
            ToastUtils.showLong("主人你快点击吧, 我帮不了你! \n(必须 7.0 以上系统才可以帮你点击)");
        }
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
