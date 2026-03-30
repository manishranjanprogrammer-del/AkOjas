package com.ojassoft.astrosage.varta.service;

import static easypay.appinvoke.manager.PaytmAssist.getContext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

import java.util.Objects;

public class HeadsUpNotificationService extends Service {
    private String CHANNEL_ID = "VoipChannel";
    private String CHANNEL_NAME = "Voip Channel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle data = null;
//        if (intent != null && intent.getExtras() != null) {
//            data = intent.getBundleExtra(CGlobalVariables.CHAT_USER_CHANNEL);
//        }
        try {
            /*Intent receiveCallAction = new Intent(getContext(), HeadsUpNotificationActionReceiver.class);
            receiveCallAction.putExtra(ConstantApp.CALL_RESPONSE_ACTION_KEY, ConstantApp.CALL_RECEIVE_ACTION);
            receiveCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data);
            receiveCallAction.setAction("RECEIVE_CALL");

            Intent cancelCallAction = new Intent(getContext(), HeadsUpNotificationActionReceiver.class);
            cancelCallAction.putExtra(ConstantApp.CALL_RESPONSE_ACTION_KEY, ConstantApp.CALL_CANCEL_ACTION);
            cancelCallAction.putExtra(ConstantApp.FCM_DATA_KEY, data);
            cancelCallAction.setAction("CANCEL_CALL");

            PendingIntent receiveCallPendingIntent = PendingIntent.getBroadcast(getContext(), 1200, receiveCallAction, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent cancelCallPendingIntent = PendingIntent.getBroadcast(getContext(), 1201, cancelCallAction, PendingIntent.FLAG_UPDATE_CURRENT);
*/
            String title = getResources().getString(R.string.chat_request_accepted);
            String msg = getResources().getString(R.string.astrologer_has_accepted_chat_req);
            int notificationId = 4242;
            Intent resultIntent = new Intent(this, DashBoardActivity.class);
            resultIntent.putExtra("chat_action_type", "accepted");
            resultIntent.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, "sdhfcshdhfksdfh");

            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            Intent resultIntentCancel = new Intent(this, DashBoardActivity.class);
            // resultIntentCancel.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
            resultIntent.putExtra("chat_action_type", "rejected");
            resultIntentCancel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent cancelIntent;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                cancelIntent = PendingIntent.getActivity(this, 0, resultIntentCancel, PendingIntent.FLAG_IMMUTABLE);
            } else {
                cancelIntent = PendingIntent.getActivity(this, 0, resultIntentCancel, 0);
            }

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
            createChannel();
            NotificationCompat.Builder notificationBuilder = null;
          //  if (data != null) {
                notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentText(msg)
                        .setContentTitle(title)
                        .setSmallIcon(R.mipmap.icon)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .addAction(R.drawable.ic_today_black_icon, "Receive Call", pendingIntent)
                        .addAction(R.drawable.ic_action_action_search, "Cancel call", cancelIntent)
                        .setAutoCancel(true)
                        .setSound(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.accept_tone))
                        .setFullScreenIntent(pendingIntent, true);
          //  }

            Notification incomingCallNotification = null;
            if (notificationBuilder != null) {
                incomingCallNotification = notificationBuilder.build();
            }
            startForeground(120, incomingCallNotification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    /*
    Create noticiation channel if OS version is greater than or eqaul to Oreo
    */
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Call Notifications");
            channel.setSound(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.accept_tone),
                    new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setLegacyStreamType(AudioManager.STREAM_RING)
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION).build());
            Objects.requireNonNull(getContext().getSystemService(NotificationManager.class)).createNotificationChannel(channel);
        }
    }
}
