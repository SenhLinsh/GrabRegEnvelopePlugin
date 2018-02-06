package com.linsh.grabregenvelopeplugin.common;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/02
 *    desc   :
 * </pre>
 */
public class Constants {

    public static final String UI_MAIN;
    public static final String UI_LUCKY_MONEY_OPEN;
    public static final String UI_LUCKY_MONEY_DETAIL;
    public static final int TIME_EXIT_LUCKY_MONEY_UI;

    static {
        UI_MAIN = "com.tencent.mm.ui.LauncherUI";
        UI_LUCKY_MONEY_OPEN = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        UI_LUCKY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
        TIME_EXIT_LUCKY_MONEY_UI = 3000;
    }
}
