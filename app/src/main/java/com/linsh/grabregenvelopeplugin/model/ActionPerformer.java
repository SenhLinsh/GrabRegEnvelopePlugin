package com.linsh.grabregenvelopeplugin.model;

import android.annotation.TargetApi;
import android.os.Build;

import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class ActionPerformer implements Runnable {

    private GREAccessibilityService mService;
    private int mAction;
    private String mActivityName;

    public ActionPerformer(GREAccessibilityService service, int action, String activityName) {
        mService = service;
        mAction = action;
        mActivityName = activityName;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void run() {
        if (mActivityName.equals(GREAccessibilityService.sCurActivityName)) {
            mService.performGlobalAction(mAction);
        }
    }
}
