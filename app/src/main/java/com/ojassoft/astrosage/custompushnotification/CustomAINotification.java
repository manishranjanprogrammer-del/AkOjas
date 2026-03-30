package com.ojassoft.astrosage.custompushnotification;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.libojassoft.android.utils.LibCGlobalVariables.UPDATE_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_NEW_STYLE_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_PROFILE_URL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION_NEW;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_NOTIFICATION_SHOWN;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.models.NotificationModel;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.AINotificationTransparentActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * CustomAINotification handles the creation and display of AI-related notifications in the application.
 * It supports both personalized notifications and AI astrologer notifications with custom styling.
 */
public class CustomAINotification {

    private final Context context;
    private final AtomicBoolean notificationDisplayed = new AtomicBoolean(false);
    private String extras, name, profilePicture, description, aiQuestion, astroId, revertQCount = "", link, title;
    private boolean isShowTitle = false, isAIAstrologerOnline = false;

  //  private static final String TAG = "ImageCheck";
    /**
     * Constructor for creating AI notifications with a specific question and title
     * @param context Application context
     * @param description The description to be displayed in the notification
     * @param title The title of the notification
     * @param isShowTitle Whether to show the title in the notification
     */
    public CustomAINotification(Context context, String description, String title, boolean isShowTitle) {
        this.context = AstrosageKundliApplication.getAppContext();
        this.description = description;
        this.title = title;
        this.isShowTitle = isShowTitle;
    }

    /**
     * Constructor for creating AI notifications with extras data and a link
     * @param context Application context
     * @param extras JSON string containing notification data
     * @param link URL to be opened when notification is clicked
     */
    public CustomAINotification(Context context, String extras, String link) {
        this.context = AstrosageKundliApplication.getAppContext();
        this.extras = extras;
        this.link = link;
    }

    /**
     * Loads and displays the notification with astrologer details and profile picture.
     * This method handles both AI notifications and personalized notifications.
     */
    public void loadNotification() {
        try { //for ai notification
            // Get last used astrologer details
            ArrayList<String> lastUsedAstrologer = com.ojassoft.astrosage.varta.utils.CUtils.getLastUsedAIAstrologerDetails(context);
//            Log.e(TAG, "loadNotification: "+lastUsedAstrologer.get(2) );
            if (!TextUtils.isEmpty(extras)) {
                // Parse notification data from extras
                JSONObject obj = new JSONObject(extras);
                this.description = obj.optString(KEY_AI_QUESTION);
                this.aiQuestion = obj.optString(KEY_AI_QUESTION_NEW);
                this.revertQCount = obj.optString(KEY_REVERT_QUESTION_COUNT);
                this.astroId = obj.optString(KEY_AI_ASTROLOGER_ID);
                this.name = obj.optString(KEY_AI_ASTROLOGER_NAME);
                this.profilePicture = obj.optString(KEY_AI_PROFILE_URL);
                this.isAIAstrologerOnline = obj.optBoolean(KEY_AI_ASTROLOGER_ONLINE);

                // Use last used astrologer details if current details are invalid
                if (TextUtils.isEmpty(astroId) || astroId.equals("0") || astroId.contains("123456")) {
                    this.profilePicture = lastUsedAstrologer.get(2);
                    this.name = lastUsedAstrologer.get(1);
                    this.astroId = lastUsedAstrologer.get(0);
                }

            } else { //for personalized notification
                this.astroId = lastUsedAstrologer.get(0);
                this.name = lastUsedAstrologer.get(1);
                this.profilePicture = lastUsedAstrologer.get(2);
                this.link = "https://varta.astrosage.com/chat-with-ai-astrologers";
            }

            // Create JSON object with notification data
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(KEY_AI_QUESTION, description);
                jsonObject.addProperty(KEY_AI_QUESTION_NEW, aiQuestion);
                jsonObject.addProperty(KEY_AI_ASTROLOGER_ID, astroId);
                jsonObject.addProperty(KEY_AI_PROFILE_URL, profilePicture);
                jsonObject.addProperty(KEY_AI_ASTROLOGER_NAME, name);
                jsonObject.addProperty(KEY_REVERT_QUESTION_COUNT, revertQCount);
                jsonObject.addProperty(KEY_AI_NOTIFICATION_TITLE, title);
                jsonObject.addProperty(KEY_AI_ASTROLOGER_ONLINE, isAIAstrologerOnline);
                this.extras = String.valueOf(jsonObject);
            } catch (Exception e) {
                // Handle JSON creation exceptions
            }

            //Log.e(TAG, "loadNotification:imgURL- "+profilePicture );

            profilePicture =    com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN +profilePicture;
            // Glide automatically checks its memory and disk cache before loading from the network.
            Glide.with(context)
                    .asBitmap() // Request a Bitmap object
                    .load(profilePicture)
                    // Use a CustomTarget to receive the Bitmap asynchronously
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            // Image loaded (from cache or network), pass the Bitmap to showNotification
                            showNotificationOnce(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Ignore clear callbacks so the same notification is not rendered twice.
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            // Image load failed, show notification without the profile picture
                            super.onLoadFailed(errorDrawable);
                            showNotificationOnce(null);
                        }
                    });
            // === END GLIDE IMPLEMENTATION ===

        } catch (Exception e) {
            //
        }
    }

    /**
     * Ensures Glide callback races render the notification only once for a single payload.
     * @param bitmap Profile picture bitmap to be displayed in the notification
     */
    private void showNotificationOnce(@Nullable Bitmap bitmap) {
        if (!notificationDisplayed.compareAndSet(false, true)) {
            return;
        }
        showNotification(bitmap);
    }

    /**
     * Displays the notification with the provided profile picture
     * @param bitmap Profile picture bitmap to be displayed in the notification
     */
    private void showNotification(Bitmap bitmap) {
        try {
            // Save notification to local database
            saveNotificationInLocalDb();
            AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n CustomAINotification aiastroid : "+astroId;

            // Set up notification components
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);
            int notificationId = LibCUtils.getRandomNumber();

            // Create intent for notification click action
            Intent resultIntent = new Intent(context, AINotificationTransparentActivity.class);
            resultIntent.putExtra(KEY_AI_QUESTION, aiQuestion);
            resultIntent.putExtra(KEY_AI_ASTROLOGER_ONLINE, isAIAstrologerOnline);
            resultIntent.putExtra(KEY_REVERT_QUESTION_COUNT, revertQCount);
            resultIntent.putExtra(KEY_AI_ASTROLOGER_ID, astroId);
            resultIntent.putExtra("link", link);
            if (isShowTitle) {
                resultIntent.putExtra(KEY_AI_NOTIFICATION_TITLE, title);
            }

            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            resultIntent.setAction(Long.toString(System.currentTimeMillis()));

            PendingIntent pending = PendingIntent.getActivity(context, notificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            String CHANNEL_ID = "AI Notification";

            // Set default profile picture if bitmap is null
            if (bitmap == null) {
                Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.ai_astro_default);
                if (drawable != null) {
                    bitmap = ((BitmapDrawable) drawable).getBitmap();
                }
            }

            // Create person object for messaging style
            Person person;
            if (bitmap != null) {
                person = new Person.Builder()
                        .setName(name)
                        .setIcon(IconCompat.createWithBitmap(bitmap))
                        .build();
            } else {
                person = new Person.Builder()
                        .setName(name)
                        .build();
            }

            // Truncate question if too long
            String shortQuestion;
            if (description.length() >= 100) {
                shortQuestion = description.substring(0, 99) + "...";
            } else {
                shortQuestion = description;
            }

            if (!isShowTitle) {
                title = "";
            }
            //for showing notification heading properly while expanded, use title
            String customTitle = name;
            boolean showNewStyleNotification = CUtils.getBooleanData(context,SHOW_NEW_STYLE_NOTIFICATION,false);
            if(showNewStyleNotification){
                customTitle = title;
            }

            // Create messaging style for notification
            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(person)
                    .setConversationTitle(customTitle)
                    //.setConversationTitle(title)
                    .addMessage(shortQuestion, System.currentTimeMillis() - 10 * 60 * 1000, person);

            // Build notification
            NotificationCompat.Builder notificationBuilder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("")
                        .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setStyle(messagingStyle);
            }

            // Get notification manager
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Create notification channel for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }

            // Display notification if enabled
            if (CUtils.isDisplayNotification(context)) {
                mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), notificationId, notificationBuilder.build());
            }
            

        } catch (Exception e) {
            // Handle notification display exceptions
        }
    }

    /**
     * Saves the notification data to the local database and updates notification count
     */
    private void saveNotificationInLocalDb() {
        if (context == null) return;
        
        // Create notification model
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(description);
        notificationModel.setTitle(name);
        notificationModel.setLink(link);
        notificationModel.setNtId("");
        notificationModel.setExtra(extras);
        notificationModel.setImgUrl("");
        notificationModel.setBlogId("");
        notificationModel.setNotificationType(NOTIFICATION_PUSH);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        // Save to database
        NotificationDBManager dbManager = new NotificationDBManager(context);
        dbManager.addNotification(notificationModel);
        
        // Update notification count
        int count = com.ojassoft.astrosage.utils.CUtils.getIntData(context, KEY_NOTIFICATION_COUNT, 0);
        com.ojassoft.astrosage.utils.CUtils.saveIntData(context, KEY_NOTIFICATION_COUNT, ++count);
        Intent intent = new Intent(UPDATE_NOTIFICATION_COUNT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
