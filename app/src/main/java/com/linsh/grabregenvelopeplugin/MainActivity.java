package com.linsh.grabregenvelopeplugin;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.linsh.lshutils.utils.Permission;
import com.linsh.utilseverywhere.ADBUtils;
import com.linsh.utilseverywhere.PermissionUtils;
import com.linsh.utilseverywhere.ToastUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View view) {
        if (Permission.SystemAlertWindow.checkOrRequest(this, 101)) {
            if (ADBUtils.checkRoot()) {
                ADBUtils.startAccessibilityService(getPackageName(), GREAccessibilityService7.class.getName());
            } else {
                boolean check = PermissionUtils.checkPermission(Manifest.permission.WAKE_LOCK);
                if (!check) {
                    PermissionUtils.requestPermission(this, Manifest.permission.DISABLE_KEYGUARD, null);
                } else {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
            }
        } else {
            ToastUtils.show("需要悬浮窗权限才能看见我的真身哦~");
        }
    }
}
