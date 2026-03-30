package com.ojassoft.astrosage.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.libojassoft.android.misc.CStartMagazineNotificationService;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class BroadcastAstroSageArticleNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (com.libojassoft.android.utils.LibCGlobalVariables.NEW_ASTROSAGE_KUNDLI_ARTICLE_INTENT.equals(intent.getAction())
                || "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            new CStartMagazineNotificationService(
                    context,
                    com.libojassoft.android.utils.LibCGlobalVariables.CONST_ASTROSAGE_KUNDLI_SERVICE_START_DELAY,
                    CGlobalVariables.MY_AD_ID.trim(), CUtils.getLanguageCodeFromPreference(context))
                    .startMyService();
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            //CUtils.startNotificationServiceForEachHour(context);
        }
    }

}
