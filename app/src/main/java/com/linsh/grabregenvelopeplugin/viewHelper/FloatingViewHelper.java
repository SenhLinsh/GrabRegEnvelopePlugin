package com.linsh.grabregenvelopeplugin.viewHelper;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.linsh.grabregenvelopeplugin.R;
import com.linsh.lshutils.helper.wm.WindowManagerFloatHelper;
import com.linsh.lshutils.helper.wm.WindowManagerHelper;
import com.linsh.lshutils.helper.wm.WindowManagerViewHelper;
import com.linsh.utilseverywhere.ScreenUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/07
 *    desc   :
 * </pre>
 */
public class FloatingViewHelper {

    private final WindowManagerViewHelper mViewHelper;

    public FloatingViewHelper(Context context) {
        mViewHelper = new WindowManagerViewHelper(context, R.layout.layout_floating_btn)
                .setLocation(ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenHeight() / 2)
                .setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                .addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void addViewToWindowManagerHelper(WindowManagerHelper helper) {
        final View floatingView = mViewHelper.getView();
        WindowManagerFloatHelper floatingHelper = new WindowManagerFloatHelper(floatingView, true, 0.3f);
        helper.addView(mViewHelper);
        helper.addFloat(floatingHelper);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mViewHelper.getView().setOnClickListener(listener);
    }
}
