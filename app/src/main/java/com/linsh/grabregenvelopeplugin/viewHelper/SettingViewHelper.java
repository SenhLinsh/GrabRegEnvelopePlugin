package com.linsh.grabregenvelopeplugin.viewHelper;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.linsh.grabregenvelopeplugin.R;
import com.linsh.grabregenvelopeplugin.service.NotificationService;
import com.linsh.lshutils.helper.wm.WindowManagerHelper;
import com.linsh.lshutils.helper.wm.WindowManagerViewHelper;
import com.linsh.utilseverywhere.BackgroundUtils;
import com.linsh.utilseverywhere.ServiceUtils;
import com.linsh.views.preference.TogglePreference;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/07
 *    desc   :
 * </pre>
 */
public class SettingViewHelper implements View.OnClickListener {

    private final WindowManagerViewHelper mViewHelper;
    private final TogglePreference mTpFocusOnCurGroup;
    private final TogglePreference mTpKeepLight;
    private final TogglePreference mTpToggleNotify;
    private final TogglePreference mTpCheckOpenLuckyMoney;

    public SettingViewHelper(Context context) {
        mViewHelper = new WindowManagerViewHelper(context, R.layout.layout_floating_setting)
                .setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        final View settingView = mViewHelper.getView();

        View tvDismiss = settingView.findViewById(R.id.tv_dismiss);
        View tvExit = settingView.findViewById(R.id.tv_exit);
        mTpFocusOnCurGroup = settingView.findViewById(R.id.tp_setting_focus_on_current_group);
        mTpKeepLight = settingView.findViewById(R.id.tp_setting_keep_light);
        mTpToggleNotify = settingView.findViewById(R.id.tp_toggle_notification_service);
        mTpCheckOpenLuckyMoney = settingView.findViewById(R.id.tp_check_open_lucky_money);

        BackgroundUtils.addPressedEffect(tvDismiss);
        BackgroundUtils.addPressedEffect(tvExit);

        mTpToggleNotify.detail().setToggle(ServiceUtils.isRunning(NotificationService.class));

        tvDismiss.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        mTpFocusOnCurGroup.setOnClickListener(this);
        mTpKeepLight.setOnClickListener(this);
        mTpToggleNotify.setOnClickListener(this);
        mTpCheckOpenLuckyMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mViewHelperListener == null) return;
        switch (v.getId()) {
            case R.id.tv_dismiss:
                mViewHelperListener.dismiss();
                break;
            case R.id.tv_exit:
                mViewHelperListener.exit();
                break;
            case R.id.tp_setting_focus_on_current_group:
                mTpFocusOnCurGroup.detail().toggle();
                mViewHelperListener.focusOnCurGroup(mTpFocusOnCurGroup.detail().isToggleOn());
                break;
            case R.id.tp_setting_keep_light:
                mTpKeepLight.detail().toggle();
                mViewHelperListener.keepLight(mTpKeepLight.detail().isToggleOn());
                break;
            case R.id.tp_toggle_notification_service:
                mTpToggleNotify.detail().toggle();
                mViewHelperListener.toggleNotifyService(mTpToggleNotify.detail().isToggleOn());
                break;
            case R.id.tp_check_open_lucky_money:
                mTpCheckOpenLuckyMoney.detail().toggle();
                mViewHelperListener.checkOpenLuckyMoney(mTpCheckOpenLuckyMoney, mTpCheckOpenLuckyMoney.detail().isToggleOn());
                break;
        }
    }

    public void addView(WindowManagerHelper helper) {
        helper.addView(mViewHelper);
    }

    private ViewHelperListener mViewHelperListener;

    public void setViewHelperListener(ViewHelperListener listener) {
        mViewHelperListener = listener;
    }

    public void toggleKeepLight(boolean on) {
        mTpKeepLight.detail().setToggle(on);
    }

    public void toggleCheckOpenLuckyMoney(boolean on) {
        mTpCheckOpenLuckyMoney.detail().setToggle(on);
    }

    public void toggleIgnoreNotification(boolean ignore) {
        mTpFocusOnCurGroup.detail().setToggle(ignore);
    }

    public interface ViewHelperListener {

        void dismiss();

        void exit();

        void focusOnCurGroup(boolean toggleOn);

        void keepLight(boolean toggleOn);

        void toggleNotifyService(boolean toggleOn);

        void checkOpenLuckyMoney(View view, boolean toggleOn);
    }

}
