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

    public static final String ID_MAIN_WECHAT_TOP;
    public static final String ID_MAIN_WECHAT_BOTTOM;
    public static final String ID_MAIN_NOTIFICATION_NUM = "com.tencent.mm:id/iu";
    public static final String ID_CHAT_BACK = "com.tencent.mm:id/h_";
    public static final String ID_OPEN_LUCKY_MONEY_CLOSE = "com.tencent.mm:id/c07";

    public static final int TIME_EXIT_LUCKY_MONEY_UI;
    public static final int TIME_DELAY_PERFORM_CLICK = 1000;
    public static final int TIME_DELAY_PERFORM_CLICK_OPEN = 0;
    public static final int TIME_INTERVAL_PERFORM_CLICK = 1000;

    public static final int TYPE_MAIN = 1;
    public static final int TYPE_CHAT = 2;
    public static final int TYPE_MAIN_UI = 3;
    public static final int TYPE_LUCKY_MONEY_OPEN = 4;
    public static final int TYPE_LUCKY_MONEY_DETAIL = 5;

    static {
        UI_MAIN = "com.tencent.mm.ui.LauncherUI";
        UI_LUCKY_MONEY_OPEN = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        UI_LUCKY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";

        ID_MAIN_WECHAT_TOP = "android:id/text1";
        ID_MAIN_WECHAT_BOTTOM = "com.tencent.mm:id/c8t";

        TIME_EXIT_LUCKY_MONEY_UI = 3000;
    }
}
