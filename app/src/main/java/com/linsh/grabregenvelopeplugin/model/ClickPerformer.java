package com.linsh.grabregenvelopeplugin.model;

import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;

import com.linsh.grabregenvelopeplugin.common.Constants;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService5;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.ToastUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class ClickPerformer implements Runnable {

    private GREAccessibilityService5 mService;
    private Point mPoint;
    private String mActivityName;
    private int mTimes;
    private String errorMsg = "(;´༎ຶД༎ຶ`) 到达不了想去的彼岸";
    private String key;
    private int interval;

    public ClickPerformer(GREAccessibilityService5 service, Point point, String activityName) {
        mService = service;
        mPoint = point;
        mActivityName = activityName;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void run() {
        if (mActivityName.equals(GREAccessibilityService5.sCurActivityName)) {
            // 模拟点击
            performClick(mPoint);
            switch (mTimes++) {
                case 4:
                    ToastUtils.showLong("╮（╯＿╰）╭ 我尝试了几次 好像点了没反应?");
                    break;
                case 6:
                    if (mActivityName.equals(Constants.UI_LUCKY_MONEY_OPEN)
                            || mActivityName.equals(Constants.UI_LUCKY_MONEY_DETAIL)) {
                        mService.performActionBack();
                    }
                    break;
                case 10:
                    ToastUtils.showLong(errorMsg);
                    return;
                default:
                    break;
            }
            if (interval > 0) {
                HandlerUtils.postRunnable(this, interval);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void performClick(Point point) {
        Path path = new Path();
        path.moveTo(point.x, point.y);
        GestureDescription gesture = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 50))
                .build();
        mService.dispatchGesture(gesture, null, null);
    }

    public ClickPerformer setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public ClickPerformer setKey(String key) {
        this.key = key;
        return this;
    }

    public ClickPerformer setInterval(int interval) {
        this.interval = interval;
        return this;
    }
}
