package com.ojassoft.astrosage.varta.utils;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.libojassoft.android.utils.LibCUtils;
import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.models.NotificationModel;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;

/**
 * Helper for showing app-local notifications (non-FCM) and optionally persisting them in local DB.
 *
 * This class centralizes notification channel usage, PendingIntent wiring, and common presentation
 * so callers don't need to duplicate notification-building code.
 */
public class CreateCustomLocalNotification{

    Context mContext;

    /** Stable notification id used for the "AI response ready" notification so it can be updated/cancelled programmatically. */
    public static final int AI_RESPONSE_READY_NOTIFICATION_ID = 91001;

    public CreateCustomLocalNotification(Context context){
        this.mContext = context;
    }

    /**
     * Shows an "AI response ready" notification with a short collapsed preview and a longer expanded body.
     * Tapping the notification opens {@link DashBoardActivity} by default.
     *
     * Uses the platform notification template instead of custom RemoteViews because some Samsung builds
     * collapse custom local notifications down to only the app name, hiding the response preview.
     */
    public void showAiResponseReadyNotification(String title, String collapsedText, String expandedText) {
        try {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon);
            // Match the ongoing chat notification behavior: bring user back into the app without clearing the task.
            Intent intent = new Intent(mContext, DashBoardActivity.class);

            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(mContext, AI_RESPONSE_READY_NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(mContext, AI_RESPONSE_READY_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            String safeTitle = (title == null || title.trim().isEmpty()) ? mContext.getString(R.string.app_name) : title;
            String safeCollapsed = (collapsedText == null || collapsedText.trim().isEmpty()) ? "Your response is ready." : collapsedText.trim();
            String safeExpanded = (expandedText == null || expandedText.trim().isEmpty()) ? safeCollapsed : expandedText.trim();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(safeTitle)
                    .setContentText(safeCollapsed)
                    .setLargeIcon(icon)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    // Standard BigTextStyle keeps collapsed previews readable across OEM skins like Samsung One UI.
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(safeExpanded)
                            .setBigContentTitle(safeTitle))
                    .setOnlyAlertOnce(true);

            NotificationManager notificationManager = createNotificationChannel();
            notificationManager.notify(AI_RESPONSE_READY_NOTIFICATION_ID, builder.build());
        } catch (Exception e) {
            //
        }
    }

    /**
     * Cancels the "AI response ready" notification (if present) without requiring user interaction.
     */
    public void cancelAiResponseReadyNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(AI_RESPONSE_READY_NOTIFICATION_ID);
            }
        } catch (Exception e) {
            //
        }
    }

    public void showLocalNotification(String title, String description, boolean isShowInNotificationCenter) {
        try {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon);
            Intent intent = new Intent(mContext, ActAppModule.class);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.NOTIFICATION_TYPE, com.ojassoft.astrosage.utils.CGlobalVariables.INSTALL_FREE_NOTIFICATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls+"/talk-to-astrologers"));
            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(icon)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(description));

            NotificationManager notificationManager = createNotificationChannel();
            notificationManager.notify(LibCUtils.getRandomNumber(), builder.build());
            if (isShowInNotificationCenter) {
                saveNotificationInLocalDb(title, description, com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls+"/talk-to-astrologers","FIRSTCHATFREE");
            }

        }catch (Exception e){
            //
        }
    }

    private NotificationManager createNotificationChannel() {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        return mNotificationManager;
    }

    private void saveNotificationInLocalDb(String tit, String cont, String link, String imgurl) {

        if (mContext == null) return;
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(cont);
        notificationModel.setTitle(tit);
        notificationModel.setLink(link);
        notificationModel.setNtId("");
        notificationModel.setExtra("");
        notificationModel.setImgUrl(imgurl);
        notificationModel.setBlogId("");
        notificationModel.setNotificationType(NOTIFICATION_PUSH);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        NotificationDBManager dbManager = new NotificationDBManager(mContext);
        dbManager.addNotification(notificationModel);
    }

}
