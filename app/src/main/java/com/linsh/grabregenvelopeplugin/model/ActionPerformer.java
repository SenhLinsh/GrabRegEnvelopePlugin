package com.linsh.grabregenvelopeplugin.model;

import android.annotation.TargetApi;
import android.os.Build;

import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService5;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class ActionPerformer implements Runnable {

    private GREAccessibilityService5 mService;
    private int mAction;
    private String mActivityName;

    public ActionPerformer(GREAccessibilityService5 service, int action, String activityName) {
        mService = service;
        mAction = action;
        mActivityName = activityName;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void run() {
        if (mActivityName.equals(GREAccessibilityService5.sCurActivityName)) {
            mService.performGlobalAction(mAction);
        }
    }
}
