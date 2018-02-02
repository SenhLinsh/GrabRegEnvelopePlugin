package com.linsh.grabregenvelopeplugin;

import android.app.Application;

import com.linsh.lshutils.viewHelper.WindowManagerHelper;
import com.linsh.utilseverywhere.Utils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/02
 *    desc   :
 * </pre>
 */
public class GREApplication extends Application {

    private static WindowManagerHelper mWindowManagerHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        mWindowManagerHelper = new WindowManagerHelper();
    }

    public static WindowManagerHelper getWindowManagerHelper() {
        return mWindowManagerHelper;
    }
}
