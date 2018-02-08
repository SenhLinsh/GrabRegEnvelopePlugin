package com.linsh.grabregenvelopeplugin.page;

import android.accessibilityservice.AccessibilityService;

import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.common.Constants;
import com.linsh.grabregenvelopeplugin.model.ActionPerformer;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService;
import com.linsh.utilseverywhere.HandlerUtils;
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

    public static void exitLuckyMoneyDetail(GREAccessibilityService service, AccessibilityHelper helper) {
        int time = Config.sTimeExitLuckyMoneyDetailUi;
        if (time > 1000) {
            ToastUtils.showLong(String.format("这里没什么好留恋的,我准备%s秒后回去继续抢红包哦 (๑•̀ㅂ•́)و✧", time / 1000f));
        }
        ActionPerformer clickPerformer = new ActionPerformer(service, AccessibilityService.GLOBAL_ACTION_BACK, Constants.UI_LUCKY_MONEY_DETAIL);
        HandlerUtils.postRunnable(clickPerformer, time);
    }
}
