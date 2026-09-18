package com.cinarli.yemekhane.kiosk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "KioskBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        String action = intent.getAction();
        Log.i(TAG, "Boot olayi yakalandi: " + action);

        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action) ||
            "com.htc.intent.action.QUICKBOOT_POWERON".equals(action)) {

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (powerManager != null) {
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE,
                    "YemekNETKiosk:BootWakeLock"
                );
                wakeLock.acquire(10000);
            }

            Intent kioskIntent = new Intent(context, MainActivity.class);
            kioskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            kioskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            kioskIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            
            try {
                context.startActivity(kioskIntent);
                Log.i(TAG, "Kiosk MainActivity basariyla baslatildi.");
            } catch (Exception e) {
                Log.e(TAG, "MainActivity baslatilamadi: " + e.getMessage(), e);
            }
        }
    }
}