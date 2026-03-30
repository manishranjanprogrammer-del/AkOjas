package com.ojassoft.astrosage.ui.JobServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class BroadcastToResetJobSchedular extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            try {
                //if (CUtils.hasStoragePermission(context)) {
               // CUtils.startNotificationServiceForEachHour(context);// close on 1-19-2023
                //}
                //CUtils.scheduleHoroscopeNotificationsEachHour(context);// close on 1-19-2023
                CUtils.BirthdayNotifications(context, CGlobalVariables.BIRTHDAY_INTENT_ACTION, 50);
            }catch (Exception ex){
                //Log.e("Exception","BroadcastToResetJobSchedular");
            }
        }
    }
}
