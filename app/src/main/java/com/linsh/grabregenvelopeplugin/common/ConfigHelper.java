package com.linsh.grabregenvelopeplugin.common;

import android.graphics.Point;

import com.linsh.grabregenvelopeplugin.BuildConfig;
import com.linsh.grabregenvelopeplugin.model.OpenLuckyMoneyTimes;
import com.linsh.lshutils.utils.PropertiesFileUtils;
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
    public static final String KEY_FIRST_IN = "first_in";
    public static final String KEY_EXIT_LUCKY_MONEY_DETAIL_TIME = "exit_lucky_money_detail_time";
    public static final String KEY_OPEN_LUCKY_MONEY_TIME = "open_lucky_money_time";
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


    public static boolean isFirstIn() {
        return SharedPreferenceUtils.getBoolean(KEY_FIRST_IN, true);
    }

    public static void setFirstIn(boolean firstIn) {
        SharedPreferenceUtils.putBoolean(KEY_FIRST_IN, firstIn);
    }

    public static void saveExitLuckyMoneyDetailUiTime(int delay) {
        SharedPreferenceUtils.putInt(KEY_EXIT_LUCKY_MONEY_DETAIL_TIME, delay);
    }

    public static int getExitLuckyMoneyDetailUiTime(int def) {
        return SharedPreferenceUtils.getInt(KEY_EXIT_LUCKY_MONEY_DETAIL_TIME, def);
    }

    public static void saveOpenLuckyMoneyTime(int delay) {
        SharedPreferenceUtils.putInt(KEY_OPEN_LUCKY_MONEY_TIME, delay);
    }

    public static int getOpenLuckyMoneyTime(int def) {
        return SharedPreferenceUtils.getInt(KEY_OPEN_LUCKY_MONEY_TIME, def);
    }

    public static int getMinOpenLuckyMoneyTime() {
        if (Config.sOpenLuckyMoneyCounts < 3 || BuildConfig.IS_USER) {
            return 0;
        } else if (Config.sOpenLuckyMoneyCounts < 10) {
            return 500;
        } else {
            return 2000;
        }
    }

    public static int getOpenLuckyMoneyTime() {
        if (Config.sOpenLuckyMoneyCounts < 3 || BuildConfig.IS_USER) {
            return Config.sTimeDelayPerformClickOpen;
        } else if (Config.sOpenLuckyMoneyCounts < 10) {
            if (Config.sTimeDelayPerformClickOpen < 500)
                Config.sTimeDelayPerformClickOpen = 500;
            return Config.sTimeDelayPerformClickOpen;
        } else {
            if (Config.sTimeDelayPerformClickOpen < 2000)
                Config.sTimeDelayPerformClickOpen = 2000;
            return Config.sTimeDelayPerformClickOpen;
        }
    }

    public static void addOpenLuckyMoneyCount() {
        Config.sOpenLuckyMoneyCounts++;
        OpenLuckyMoneyTimes count = new OpenLuckyMoneyTimes();
        count.times = Config.sOpenLuckyMoneyCounts;
        count.timestamp = System.currentTimeMillis();
        PropertiesFileUtils.putObject(count);
    }

    public static void saveIdChatBack(String idChatBack) {
        SharedPreferenceUtils.putString("idChatBack", idChatBack);
    }

    public static String getIdChatBack() {
        return SharedPreferenceUtils.getString("idChatBack");
    }
}
