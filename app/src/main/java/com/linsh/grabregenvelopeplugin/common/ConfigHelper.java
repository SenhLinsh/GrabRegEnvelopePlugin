package com.linsh.grabregenvelopeplugin.common;

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

    public static final String KEY_OPEN_LUCKY_MONEY_LOCATION = "open_lucky_money_location";
    private static Point sOpenLuckyMoneyLocation;
    private static boolean requireOpenLuckyMoneyLocation;

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

    public static void saveLocation(String key, Point point) {
        switch (key) {
            case KEY_OPEN_LUCKY_MONEY_LOCATION:
                sOpenLuckyMoneyLocation = point;
                requireOpenLuckyMoneyLocation = point == null;
                break;
        }
        SharedPreferenceUtils.putInt(key, point != null ? (point.x * 10000 + point.y) : 0);
    }


}
