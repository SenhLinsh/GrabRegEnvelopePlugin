package com.linsh.grabregenvelopeplugin.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import com.linsh.grabregenvelopeplugin.R;
import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService1;
import com.linsh.grabregenvelopeplugin.service.NotificationService;
import com.linsh.lshutils.helper.wm.WindowManagerFloatHelper;
import com.linsh.lshutils.helper.wm.WindowManagerHelper;
import com.linsh.lshutils.helper.wm.WindowManagerViewHelper;
import com.linsh.utilseverywhere.BackgroundUtils;
import com.linsh.utilseverywhere.ScreenUtils;
import com.linsh.utilseverywhere.ServiceUtils;
import com.linsh.utilseverywhere.ViewUtils;
import com.linsh.views.preference.TogglePreference;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/03
 *    desc   :
 * </pre>
 */
public class GREWindowManagerHelper {

    private WindowManagerHelper mWindowManagerHelper;
    private View mLocationView;

    public GREWindowManagerHelper() {
        mWindowManagerHelper = new WindowManagerHelper();
    }

    public WindowManagerHelper getWindowManagerHelper() {
        return mWindowManagerHelper;
    }

    public void showFloatingView(final Context context) {
        if (mWindowManagerHelper.getViewCount() == 0) {
            WindowManagerViewHelper floatingViewHelper = new WindowManagerViewHelper(context, R.layout.layout_floating_btn)
                    .setLocation(ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenHeight() / 2)
                    .addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            final View floatingView = floatingViewHelper.getView();
            WindowManagerFloatHelper floatingHelper = new WindowManagerFloatHelper(floatingView, true, 0.3f);
            mWindowManagerHelper.addView(floatingViewHelper);
            mWindowManagerHelper.addFloat(floatingHelper);
            floatingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWindowManagerHelper.getViewCount() == 1) {
                        WindowManagerViewHelper settingViewHelper = new WindowManagerViewHelper(v.getContext(), R.layout.layout_floating_setting)
                                .setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                        final View settingView = settingViewHelper.getView();
                        mWindowManagerHelper.addView(settingViewHelper);
                        View tvDismiss = settingView.findViewById(R.id.tv_dismiss);
                        View tvExit = settingView.findViewById(R.id.tv_exit);
                        final TogglePreference tpToggleNotify = settingView.findViewById(R.id.tp_toggle_notification_service);
                        BackgroundUtils.addPressedEffect(tvDismiss);
                        BackgroundUtils.addPressedEffect(tvExit);
                        tvDismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mWindowManagerHelper.removeView(1);
                            }
                        });
                        tvExit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mWindowManagerHelper.removeAllViews();
                                ServiceUtils.stopService(GREAccessibilityService1.class);
                            }
                        });
                        tpToggleNotify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tpToggleNotify.detail().toggel();
                                boolean toggleOn = tpToggleNotify.detail().isToggleOn();
                                if (toggleOn) {
                                    String string = Settings.Secure.getString(v.getContext().getContentResolver(),
                                            "enabled_notification_listeners");
                                    if (!string.contains(NotificationService.class.getName())) {
                                        v.getContext().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                                    }
                                } else {
                                    ServiceUtils.stopService(NotificationService.class);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void showLocationView(Context context, Point location, final String key) {
        if (mLocationView == null) {
            WindowManagerViewHelper floatingViewHelper = new WindowManagerViewHelper(context, R.layout.layout_floating_location)
                    .setLocation(location.x, location.y)
                    .addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mLocationView = floatingViewHelper.getView();
            WindowManagerFloatHelper floatingHelper = new WindowManagerFloatHelper(mLocationView);
            mWindowManagerHelper.addView(floatingViewHelper);
            mWindowManagerHelper.addFloat(floatingHelper);
            mLocationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] locations = ViewUtils.getLocationsOnScreen(mLocationView);
                    ConfigHelper.saveLocation(key, new Point(locations[0], locations[1]));
                }
            });
        } else {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mLocationView.getLayoutParams();
            layoutParams.x = location.x;
            layoutParams.y = location.y;
            mWindowManagerHelper.updateViewLayout(mLocationView);
        }
    }

    public void removeLocationView() {
        if (mLocationView != null) {
            mWindowManagerHelper.removeView(mLocationView);
        }
    }
}
