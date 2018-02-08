package com.linsh.grabregenvelopeplugin.common;

import com.linsh.grabregenvelopeplugin.model.OpenLuckyMoneyTimes;
import com.linsh.lshutils.utils.PropertiesFileUtils;
import com.linsh.utilseverywhere.LogUtils;
import com.linsh.utilseverywhere.module.SimpleDate;

import java.io.File;
import java.util.Date;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/07
 *    desc   :
 * </pre>
 */
public class Config {

    public static String sAimGroupChat;

    public static boolean isNotificationIgnored;
    public static boolean isExit = true;
    public static int sOpenLuckyMoneyCounts;

    public static int sTimeExitLuckyMoneyDetailUi = 3000;
    public static int sTimeDelayPerformClick = 1000;
    public static int sTimeDelayPerformClickOpen = 1000;
    public static int sTimeIntervalPerformClick = 1000;

    public static String sIdChatBack = "com.tencent.mm:id/h_";
    public static boolean sIsIdChatBackNeeded = true;

    static {
        sTimeExitLuckyMoneyDetailUi = ConfigHelper.getExitLuckyMoneyDetailUiTime(3000);

        OpenLuckyMoneyTimes openLuckyMoneyTimes = PropertiesFileUtils.getObject(OpenLuckyMoneyTimes.class);
        if (openLuckyMoneyTimes != null) {
            File propertyFile = PropertiesFileUtils.getPropertyFile(OpenLuckyMoneyTimes.class);
            long timestamp = openLuckyMoneyTimes.timestamp;
            if (Math.abs(propertyFile.lastModified() - timestamp) < 500) {
                SimpleDate simpleDate = new SimpleDate(timestamp);
                if (simpleDate.isSameDay(new SimpleDate(new Date()))) {
                    sOpenLuckyMoneyCounts = openLuckyMoneyTimes.times;
                }
            } else {
                sOpenLuckyMoneyCounts = 3;
                LogUtils.e("Config initializer: ", "OpenLuckyMoneyTimes 配置文件的 timestamp 和 lastModified 不同!");
            }
        }
        sTimeDelayPerformClickOpen = ConfigHelper.getOpenLuckyMoneyTime(1000);
        String idChatBack = ConfigHelper.getIdChatBack();
        if (idChatBack != null) {
            sIdChatBack = idChatBack;
            sIsIdChatBackNeeded = false;
        }
    }
}
