package com.ojassoft.astrosage.varta.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.concurrent.TimeUnit;

public class RunningChatService extends Service implements VolleyResponse {
    private static String CHAT_CHANNEL_ID;
    private Context context;
   private VolleyResponse volleyResponse;
  private RequestQueue queue;
    private String chatJsonObject;
    private String astrologerName;
    private String astrologerProfileUrl;
    private String astrologerId;
    private String userChatTime;
    private String chatinitiatetype;
    public static final String CHANNEL_ID = "ForegroundServiceRunningChat";
    private DatabaseReference  messageReadRef;
    private ChildEventListener childEventListener;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;
        this.volleyResponse = this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        try {

            Bundle bundle = intent.getExtras();
            CHAT_CHANNEL_ID = bundle.getString(CGlobalVariables.CHAT_USER_CHANNEL);
            chatJsonObject = bundle.getString("connect_chat_bean");
            astrologerName = bundle.getString("astrologer_name");
            astrologerProfileUrl = bundle.getString("astrologer_profile_url");
            astrologerId = bundle.getString("astrologer_id");
            userChatTime = bundle.getString("userChatTime");
            chatinitiatetype = bundle.getString(CGlobalVariables.CHATINITIATETYPE);
            // Log.d("testChatNewRunning", "intent.getExtras() " + CHAT_CHANNEL_ID + "    " + chatJsonObject + "  " + astrologerName + "  " + astrologerProfileUrl + "  " + astrologerId + "  " + userChatTime);

        } catch (Exception e) {
              Log.d("testChatNewRunning", "intent.getExtras() Exception" + e);
        }
        createNotificationChannel();
        createNotification();
        //messageReadFromFirebase(CHAT_CHANNEL_ID);
        return START_STICKY;
    }
    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Running Chat", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        } catch (Exception e) {
            Log.d("testChatNewRunning", "Exception Exception" + e);
        }
    }
    private void createNotification() {
        try {
            String msg =  getResources().getString(R.string.ongoing_chat);
            Intent resultIntent;
            resultIntent = new Intent(this, ChatWindowActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.putExtra("ongoing_notification", true);
            resultIntent.setAction(Intent.ACTION_VIEW);
            PendingIntent pendingIntent;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
            }

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
            Notification notification;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notification = new Notification.Builder(this, CHANNEL_ID).setContentTitle(astrologerName).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(ContextCompat.getColor(this, R.color.colorPurple)).setLargeIcon(icon).setAutoCancel(false).setContentIntent(pendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.running_chat)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(getResources().getColor(R.color.colorPurple)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(pendingIntent).build();
            }

            if (notification != null) {
                startForeground(3001, notification);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try {
            if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
                stopService(new Intent(this, OnGoingChatService.class));
            }
            startService();
            //Toast.makeText(this, "Called onTaskRemoved", Toast.LENGTH_SHORT).show();
            stopService(new Intent(this, RunningChatService.class));
        }catch ( Exception e){
            //
        }

        super.onTaskRemoved(rootIntent);
    }
    private void startService() {
        try {
            Intent serviceIntent = new Intent(this, OnGoingChatService.class);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
            bundle.putString("connect_chat_bean", chatJsonObject);
            bundle.putString("astrologer_name", astrologerName);
            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
            bundle.putString("astrologer_id", astrologerId);
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER);
            //Log.d("testIssueService","connect_chat_bean   "+ chatJsonObject+"astrologer_name  "+ astrologerName+"changeToMinSec()===>>"+changeToMinSec());
            bundle.putString("userChatTime", changeToMinSec());
            // bundle.putSerializable("chatInitiateAstrologerDetailBean",AstrosageKundliApplication.chatInitiateAstrologerDetailBean);
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(serviceIntent);
            } else {
                this.startService(serviceIntent);
            }
            AstrosageKundliApplication.isBackFromChat = true;

        } catch (Exception e) {
            //
        }
    }
    public String changeToMinSec() {
        try {
            long millis = CGlobalVariables.chatTimerTime;
            String ms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            return ms;
        } catch (Exception e) {
            return "00:00";
        }

    }
    private void messageReadFromFirebase(String chatChannelId) {
        if (messageReadRef != null && childEventListener != null) {
            messageReadRef.removeEventListener(childEventListener);
        }
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                try {
                    if (!dataSnapshot.getKey().equalsIgnoreCase("isSeen") && dataSnapshot.getValue() != null) {
                        String From = dataSnapshot.child("From").getValue(String.class);
                        String Text = dataSnapshot.child("Text").getValue(String.class);

                        Message message = new Message();
                        message.setAuthor(From);
                        message.setMessageBody(Text);
                        if (dataSnapshot.child("MsgTime").exists()) {
                            long MsgTime = dataSnapshot.child("MsgTime").getValue(Long.class);
                            message.setDateCreated("" + MsgTime);
                        } else {
                            message.setDateCreated("0L");
                        }
                        if (dataSnapshot.child("chatId").exists()) {
                            int chatId = dataSnapshot.child("chatId").getValue(Integer.class);
                            message.setChatId(chatId);
                        }
                        if (dataSnapshot.child("isSeen").exists()) {
                            boolean isSeen = dataSnapshot.child("isSeen").getValue(Boolean.class);
                            message.setSeen(isSeen);
                        } else {
                            message.setSeen(false);
                        }
                        if (!AstrosageKundliApplication.wasInBackground) {
                            CGlobalVariables.CHAT_NOTIFICATION_QUEUE = CGlobalVariables.CHAT_NOTIFICATION_QUEUE + Text + "\n";
                            displayChatNotification("astrologerName", CGlobalVariables.CHAT_NOTIFICATION_QUEUE);
                        }

                    }
                } catch (Exception e) {
                    Log.e("error received2", e.toString());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        messageReadRef = AstrosageKundliApplication.getmFirebaseDatabase(chatChannelId).child(CGlobalVariables.MESSAGES_FBD_KEY);
        messageReadRef.addChildEventListener(childEventListener);
    }
    private void displayChatNotification(String title, String msg) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);
        int notificationId = LibCUtils.getRandomNumber();
        // pending intent is redirection using the deep-link
        //If url contains Play store then use Action view else open the App
        Intent resultIntent;

        resultIntent = new Intent(this, ChatWindowActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("ongoing_notification", true);
        resultIntent.setAction(Intent.ACTION_VIEW);
        PendingIntent pending;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setContentIntent(pending).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "chatnotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // using the same tag and Id causes the new notification to replace an existing one
        mNotificationManager.notify(CGlobalVariables.CHAT_NOTIFICATION, CGlobalVariables.CHAT_NOTIFICATION_ID, notificationBuilder.build());
    }
    @Override
    public void onResponse(String response, int method) {

    }

    @Override
    public void onError(VolleyError error) {

    }
}
