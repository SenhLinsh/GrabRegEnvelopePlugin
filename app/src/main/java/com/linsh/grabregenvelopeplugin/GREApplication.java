package com.linsh.grabregenvelopeplugin;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
