package com.linsh.grabregenvelopeplugin;

import android.graphics.Point;

import com.linsh.utilseverywhere.ScreenUtils;
import com.linsh.utilseverywhere.SharedPreferenceUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/02
 *    desc   :
 * </pre>
 */
public class ConfigHelper {

    private static final String KEY_OPEN_LUCKY_MONEY_LOCATION = "open_lucky_money_location";

    private static String sAimGroupChat;
    private static Point sOpenLuckyMoneyLocation;

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
            }
        }
        return sOpenLuckyMoneyLocation;
    }

    public static void saveOpenLuckyMoneyLocation(Point point) {
        sOpenLuckyMoneyLocation = point;
        SharedPreferenceUtils.putInt(KEY_OPEN_LUCKY_MONEY_LOCATION, point.x * 10000 + point.y);
    }
}
