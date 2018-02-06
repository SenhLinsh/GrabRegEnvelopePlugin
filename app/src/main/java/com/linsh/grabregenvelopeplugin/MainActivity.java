package com.linsh.grabregenvelopeplugin;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService1;
import com.linsh.lshutils.utils.Permission;
import com.linsh.utilseverywhere.ADBUtils;
import com.linsh.utilseverywhere.IntentUtils;
import com.linsh.utilseverywhere.PermissionUtils;
import com.linsh.utilseverywhere.ServiceUtils;
import com.linsh.utilseverywhere.ToastUtils;
import com.linsh.utilseverywhere.module.CommandResult;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Permission.SystemAlertWindow.checkOrRequest(this, "我被囚禁啦 (っ*´Д`)っ 快开启『悬浮窗权限』~")) {
                checkRoot();
            }
        } else {
            checkRoot();
            findViewById(R.id.tv_hint).setVisibility(View.VISIBLE);
        }
    }

    private void checkRoot() {
        if (ADBUtils.checkRoot()) {
            CommandResult result = ADBUtils.startAccessibilityService(getPackageName(), GREAccessibilityService1.class.getName());
            Log.i("LshLog", "checkRoot Result: successMsg=" + result.successMsg + "    errorMsg=" + result.errorMsg);
            runServiceIfNotRunning();
        } else {
            boolean check = PermissionUtils.checkPermission(Manifest.permission.WAKE_LOCK);
            if (!check) {
                PermissionUtils.requestPermission(this, Manifest.permission.DISABLE_KEYGUARD, null);
            } else {
                runServiceIfNotRunning();
            }
        }
    }

    private void runServiceIfNotRunning() {
        boolean running = ServiceUtils.isRunning(GREAccessibilityService1.class);
        if (!running) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            ToastUtils.showLong("((*・∀・）ゞ→→ 需要打开自动模式");
        }
    }

    public void getPermission(View view) {
        IntentUtils.gotoPermissionSetting();
    }
}
