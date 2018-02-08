package com.linsh.grabregenvelopeplugin.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linsh.grabregenvelopeplugin.R;
import com.linsh.grabregenvelopeplugin.common.Config;
import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.grabregenvelopeplugin.service.GREAccessibilityService;
import com.linsh.lshutils.utils.Permission;
import com.linsh.utilseverywhere.IntentUtils;
import com.linsh.utilseverywhere.PermissionUtils;
import com.linsh.utilseverywhere.ServiceUtils;
import com.linsh.utilseverywhere.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private TextView tvDelayOpenLuckyMoney;
    private SeekBar sbDelayOpenLuckyMoney;
    private TextView tvDelayExitLuckyMoneyDetail;
    private SeekBar sbDelayExitLuckyMoneyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDelayOpenLuckyMoney = (TextView) findViewById(R.id.tv_delay_open_lucky_money);
        sbDelayOpenLuckyMoney = (SeekBar) findViewById(R.id.sb_delay_open_lucky_money);
        tvDelayExitLuckyMoneyDetail = (TextView) findViewById(R.id.tv_delay_exit_lucky_money_detail);
        sbDelayExitLuckyMoneyDetail = (SeekBar) findViewById(R.id.sb_delay_exit_lucky_money_detail);

        sbDelayOpenLuckyMoney.setMax(100);
        sbDelayOpenLuckyMoney.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = ConfigHelper.getMinOpenLuckyMoneyTime() / 100;
                if (progress < min) {
                    progress = min;
                    sbDelayOpenLuckyMoney.setProgress(progress);
                }
                int delay = progress * 100;
                tvDelayOpenLuckyMoney.setText("打开红包-延迟时间: " + delay + "ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int delay = sbDelayOpenLuckyMoney.getProgress() * 100;
                Config.sTimeDelayPerformClickOpen = delay;
                ConfigHelper.saveOpenLuckyMoneyTime(delay);
            }
        });
        sbDelayExitLuckyMoneyDetail.setMax(100);
        sbDelayExitLuckyMoneyDetail.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int delay = progress * 100;
                tvDelayExitLuckyMoneyDetail.setText("退出红包详情-延迟时间: " + delay + "ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int delay = sbDelayExitLuckyMoneyDetail.getProgress() * 100;
                Config.sTimeExitLuckyMoneyDetailUi = delay;
                ConfigHelper.saveExitLuckyMoneyDetailUiTime(delay);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        int openLuckyMoneyTime = ConfigHelper.getOpenLuckyMoneyTime();
        tvDelayOpenLuckyMoney.setText("打开红包-延迟时间: " + openLuckyMoneyTime + "ms");
        sbDelayOpenLuckyMoney.setProgress(openLuckyMoneyTime / 100);
        int exitLuckyMoneyDetailTime = Config.sTimeExitLuckyMoneyDetailUi;
        tvDelayExitLuckyMoneyDetail.setText("退出红包详情-延迟时间: " + exitLuckyMoneyDetailTime + "ms");
        sbDelayExitLuckyMoneyDetail.setProgress(exitLuckyMoneyDetailTime / 100);
    }

    public void onclick(View view) {
        if (PermissionUtils.Storage.checkPermission()) {
            checkAlertWindowPermission();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PermissionUtils.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE}, null);
            } else {
                ToastUtils.show("读写权限都不给, 主人酱紫不好吧 (っ*´Д`)っ");
            }
        }
    }

    private void checkAlertWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Permission.SystemAlertWindow.checkOrRequest(this, "我被囚禁啦 (っ*´Д`)っ 快开启『悬浮窗权限』~")) {
                checkPermission();
            }
        } else {
            checkPermission();
            findViewById(R.id.tv_hint).setVisibility(View.VISIBLE);
        }
    }

    private void checkPermission() {
        boolean check = PermissionUtils.checkPermission(Manifest.permission.WAKE_LOCK);
        if (!check) {
            PermissionUtils.requestPermission(this, Manifest.permission.DISABLE_KEYGUARD, null);
        } else {
            runServiceIfNotRunning();
        }
    }

    private void runServiceIfNotRunning() {
        boolean running = ServiceUtils.isRunning(GREAccessibilityService.class);
        if (!running) {
            IntentUtils.gotoAccessibilitySetting();
            ToastUtils.showLong("((*・∀・）ゞ→→ 需要打开自动模式");
        } else {
            ServiceUtils.startService(GREAccessibilityService.class);
        }
    }

    public void getPermission(View view) {
        IntentUtils.gotoPermissionSetting();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            switch (permissions[i]) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkAlertWindowPermission();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
