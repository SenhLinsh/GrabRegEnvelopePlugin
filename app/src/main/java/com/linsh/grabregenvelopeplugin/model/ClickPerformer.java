package com.linsh.grabregenvelopeplugin.model;

import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;

import com.linsh.grabregenvelopeplugin.GREAccessibilityService7;
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

    private GREAccessibilityService7 mService;
    private Point mPoint;
    private String mActivityName;
    private int mTimes;
    private String errorMsg = "(;´༎ຶД༎ຶ`) 到达不了想去的彼岸";
    private String key;
    private int interval;

    public ClickPerformer(GREAccessibilityService7 service, Point point, String activityName) {
        mService = service;
        mPoint = point;
        mActivityName = activityName;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void run() {
        if (mActivityName.equals(GREAccessibilityService7.sCurActivityName)) {
            Path path = new Path();
            path.moveTo(mPoint.x, mPoint.y);
            GestureDescription gesture = new GestureDescription.Builder()
                    .addStroke(new GestureDescription.StrokeDescription(path, 0, 50))
                    .build();
            mService.dispatchGesture(gesture, null, null);

            switch (mTimes) {
                case 5:
                    ToastUtils.showLong("╮（╯＿╰）╭ 我尝试了几次 好像点了没反应?");
                    break;
                case 10:
                    ToastUtils.showLong("(⊙﹏⊙) 难道我的人工智能算法出错了?");
                    break;
                case 15:
                    ToastUtils.showLong(errorMsg + " 帮我校准一下位置吧");
                    mService.showLocationView(mPoint, key);
                    return;
                default:
                    break;
            }
            if (interval > 0) {
                HandlerUtils.postRunnable(this, interval);
            }
        }
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
