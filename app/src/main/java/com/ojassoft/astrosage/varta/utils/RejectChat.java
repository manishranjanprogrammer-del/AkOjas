package com.ojassoft.astrosage.varta.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class RejectChat extends Activity {
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //CUtils.stopChatDisconnectSound();
            //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
            //stopService(new Intent(this, CountDownTimerService.class));
        } catch (Exception e){
            //Log.d("testingLog", String.valueOf(e));
        }
        //stopChat();
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
    }
    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Intent intent = new Intent(context, RejectChat.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(context, 0, intent, 0);
        }
    }

}
