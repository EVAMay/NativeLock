package com.myapplicatiopn.nativelock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import utils.PosterHandler;

public class MainActivity extends AppCompatActivity {
    private DevicePolicyManager devicePolicyManager;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private ComponentName adminReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        powerManager= (PowerManager) getSystemService(POWER_SERVICE);
        devicePolicyManager= (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,  adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"开启后就可以使用锁屏功能");//显示位置见图二
        startActivityForResult(intent,0);

        adminReceiver=new ComponentName(getBaseContext(),ScreenOffReceiver.class);
        boolean admin=devicePolicyManager.isAdminActive(adminReceiver);//是否有管理权限
        if (admin) {
           devicePolicyManager.lockNow();
        } else {
            Toast.makeText(MainActivity.this,"没有设备管理权限",
                    Toast.LENGTH_LONG).show();
        }
        screenOff();

    }
    private void screenOff(){

        boolean admin = devicePolicyManager.isAdminActive(adminReceiver);
        if (admin) {

            devicePolicyManager.lockNow();
            finish();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后可使用锁屏功能");//显示位置见图二
            startActivityForResult(intent, 0);

            Toast.makeText(this,"没有设备管理权限",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isOpen();
    }

    /**
     * 检测是否开启设备管理权限
     */
    private void isOpen() {
        if (devicePolicyManager.isAdminActive(adminReceiver)) {
            Toast.makeText(getBaseContext(), "设备已被激活，即将锁屏", Toast.LENGTH_LONG).show();
            PosterHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    screenOff();
                }
            },1000);

        } else {
            Toast.makeText(getBaseContext(), "设备未被激活", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
