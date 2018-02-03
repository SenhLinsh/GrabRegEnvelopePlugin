package com.linsh.grabregenvelopeplugin;

import android.graphics.Point;

import com.linsh.utilseverywhere.ScreenUtils;
import com.linsh.utilseverywhere.SharedPreferenceUtils;
import com.linsh.utilseverywhere.UnitConverseUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/02
 *    desc   :
 * </pre>
 */
public class ConfigHelper {

    public static final String KEY_OPEN_LUCKY_MONEY_LOCATION = "open_lucky_money_location";
    public static final String KEY_EXIT_LUCKY_MONEY_DETAIL_LOCATION = "exit_lucky_money_detail_location";

    private static String sAimGroupChat;
    private static Point sOpenLuckyMoneyLocation;
    private static Point sExitLuckyMoneyDetailLocation;
    private static boolean requireOpenLuckyMoneyLocation;
    private static boolean requireExitLuckyMoneyDetailLocation;

    public static String getAimGroupChat() {
        return sAimGroupChat;
    }

    public static void setAimGroupChat(String aimGroupChat) {
        sAimGroupChat = aimGroupChat;
    }

    public static Point getOpenLuckyMoneyLocation() {
        if (sOpenLuckyMoneyLocation == null) {
            int location = SharedPreferenceUtils.getInt(KEY_OPEN_LUCKY_MONEY_LOCATION);
            if (location > 0) {
                sOpenLuckyMoneyLocation = new Point(location / 10000, location % 10000);
            } else {
                sOpenLuckyMoneyLocation = new Point(ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenHeight() * 5 / 8);
                requireOpenLuckyMoneyLocation = true;
            }
        }
        return sOpenLuckyMoneyLocation;
    }

    public static boolean isRequireOpenLuckyMoneyLocation() {
        return requireOpenLuckyMoneyLocation;
    }

    public static Point getExitLuckyMoneyDetailLocation() {
        if (sExitLuckyMoneyDetailLocation == null) {
            int location = SharedPreferenceUtils.getInt(KEY_EXIT_LUCKY_MONEY_DETAIL_LOCATION);
            if (location > 0) {
                sExitLuckyMoneyDetailLocation = new Point(location / 10000, location % 10000);
            } else {
                sExitLuckyMoneyDetailLocation = new Point(UnitConverseUtils.dp2px(24), UnitConverseUtils.dp2px(44));
                requireExitLuckyMoneyDetailLocation = true;
            }
        }
        return sExitLuckyMoneyDetailLocation;
    }

    public static boolean isRequireExitLuckyMoneyDetailLocation() {
        return requireExitLuckyMoneyDetailLocation;
    }

    public static void saveLocation(String key, Point point) {
        switch (key) {
            case KEY_OPEN_LUCKY_MONEY_LOCATION:
                sOpenLuckyMoneyLocation = point;
                break;
            case KEY_EXIT_LUCKY_MONEY_DETAIL_LOCATION:
                sExitLuckyMoneyDetailLocation = point;
                break;
        }
        SharedPreferenceUtils.putInt(key, point.x * 10000 + point.y);
    }
}
