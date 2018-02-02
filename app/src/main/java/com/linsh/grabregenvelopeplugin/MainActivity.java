package com.linsh.grabregenvelopeplugin;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.linsh.lshutils.viewHelper.WindowManagerFloatHelper;
import com.linsh.lshutils.viewHelper.WindowManagerHelper;
import com.linsh.lshutils.viewHelper.WindowManagerViewHelper;
import com.linsh.utilseverywhere.BackgroundUtils;
import com.linsh.utilseverywhere.PermissionUtils;
import com.linsh.utilseverywhere.ScreenUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View view) {
        boolean check = PermissionUtils.checkPermission(Manifest.permission.WAKE_LOCK);
        if (!check) {
            PermissionUtils.requestPermission(this, Manifest.permission.DISABLE_KEYGUARD, null);
        } else {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
        showFloatingView();
    }

    private void showFloatingView() {
        WindowManagerHelper windowManagerHelper = GREApplication.getWindowManagerHelper();
        if (windowManagerHelper.getViewCount() == 0) {
            WindowManagerViewHelper floatingViewHelper = new WindowManagerViewHelper(this, R.layout.layout_floating_btn)
                    .setLocation(ScreenUtils.getScreenWidth() / 2, ScreenUtils.getScreenHeight() / 2)
                    .addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            View floatingView = floatingViewHelper.getView();
            WindowManagerFloatHelper floatingHelper = new WindowManagerFloatHelper(floatingView, true, 0.3f);
            windowManagerHelper.addView(floatingViewHelper);
            windowManagerHelper.addFloat(floatingHelper);
            floatingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WindowManagerHelper windowManagerHelper = GREApplication.getWindowManagerHelper();
                    if (windowManagerHelper.getViewCount() == 1) {
                        WindowManagerViewHelper settingViewHelper = new WindowManagerViewHelper(MainActivity.this, R.layout.layout_floating_setting)
                                .setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                        View settingView = settingViewHelper.getView();
                        windowManagerHelper.addView(settingViewHelper);
                        View tvDismiss = settingView.findViewById(R.id.tv_dismiss);
                        View tvExit = settingView.findViewById(R.id.tv_exit);
                        BackgroundUtils.addPressedEffect(tvDismiss);
                        BackgroundUtils.addPressedEffect(tvExit);
                        tvDismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GREApplication.getWindowManagerHelper().removeView(1);
                            }
                        });
                        tvExit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GREApplication.getWindowManagerHelper().removeAllViews();
                            }
                        });
                    }
                }
            });
        }
    }
}
