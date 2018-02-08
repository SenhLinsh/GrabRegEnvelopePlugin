package com.linsh.grabregenvelopeplugin.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;

import com.linsh.grabregenvelopeplugin.R;
import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService;
import com.linsh.grabregenvelopeplugin.service.NotificationService;
import com.linsh.grabregenvelopeplugin.viewHelper.FloatingViewHelper;
import com.linsh.grabregenvelopeplugin.viewHelper.SettingViewHelper;
import com.linsh.lshutils.helper.wm.WindowManagerFloatHelper;
import com.linsh.lshutils.helper.wm.WindowManagerHelper;
import com.linsh.lshutils.helper.wm.WindowManagerViewHelper;
import com.linsh.lshutils.tools.PowerHelper;
import com.linsh.utilseverywhere.ContextUtils;
import com.linsh.utilseverywhere.ServiceUtils;
import com.linsh.utilseverywhere.ViewUtils;

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
    private PowerHelper mPowerHelper;

    public GREWindowManagerHelper() {
        mWindowManagerHelper = new WindowManagerHelper();
    }

    public WindowManagerHelper getWindowManagerHelper() {
        return mWindowManagerHelper;
    }

    public void showFloatingView(final Context context) {
        if (mWindowManagerHelper.getViewCount() == 0) {
            FloatingViewHelper floatingViewHelper = new FloatingViewHelper(context);
            floatingViewHelper.addViewToWindowManagerHelper(mWindowManagerHelper);
            floatingViewHelper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mWindowManagerHelper.containViewType(ConstraintLayout.class)) {
                        SettingViewHelper settingViewHelper = new SettingViewHelper(v.getContext());
                        settingViewHelper.addView(mWindowManagerHelper);
                        settingViewHelper.toggleIgnoreNotification(Config.isNotificationIgnored);
                        settingViewHelper.toggleKeepLight(mPowerHelper != null);
                        settingViewHelper.toggleCheckOpenLuckyMoney(!ConfigHelper.isRequireOpenLuckyMoneyLocation());
                        settingViewHelper.setViewHelperListener(new SettingViewHelper.ViewHelperListener() {
                            @Override
                            public void dismiss() {
                                removeSettingView();
                            }

                            @Override
                            public void exit() {
                                exitWindowManager();
                            }

                            @Override
                            public void focusOnCurGroup(boolean toggleOn) {
                                Config.isNotificationIgnored = toggleOn;
                            }

                            @Override
                            public void keepLight(boolean toggleOn) {
                                keepScreen(toggleOn);
                            }

                            @Override
                            public void toggleNotifyService(boolean toggleOn) {
                                if (toggleOn) {
                                    String string = Settings.Secure.getString(ContextUtils.getContentResolver(),
                                            "enabled_notification_listeners");
                                    if (!string.contains(NotificationService.class.getName()) || !ServiceUtils.isRunning(NotificationService.class)) {
                                        ContextUtils.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                                    }
                                    removeSettingView();
                                } else {
                                    ServiceUtils.stopService(NotificationService.class);
                                }
                            }

                            @Override
                            public void checkOpenLuckyMoney(View view, boolean toggleOn) {
                                if (toggleOn) {
                                    removeSettingView();
                                    showLocationView(view.getContext(), ConfigHelper.getOpenLuckyMoneyLocation(), ConfigHelper.KEY_OPEN_LUCKY_MONEY_LOCATION);
                                } else {
                                    ConfigHelper.saveLocation(ConfigHelper.KEY_OPEN_LUCKY_MONEY_LOCATION, null);
                                    removeLocationView();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void removeSettingView() {
        mWindowManagerHelper.removeView(1);
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
                    removeLocationView();
                }
            });
            mLocationView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeLocationView();
                    return true;
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
            mLocationView = null;
        }
    }

    public void exitWindowManager() {
        mWindowManagerHelper.removeAllViews();
        ServiceUtils.stopService(GREAccessibilityService.class);
        ServiceUtils.stopService(NotificationService.class);
        keepScreen(false);
        Config.isExit = true;
    }

    private void keepScreen(boolean on) {
        if (on) {
            mPowerHelper = new PowerHelper();
            mPowerHelper.keepScreenOn();
        } else {
            if (mPowerHelper != null) {
                mPowerHelper.turnScreenOff();
                mPowerHelper = null;
            }
        }
    }
}
