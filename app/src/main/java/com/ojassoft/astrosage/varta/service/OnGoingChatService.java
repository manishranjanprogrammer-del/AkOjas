package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.ConnectChatBean;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/* Must Read
this service use to handle app is in background and forground during chat
Note - Need to focus on AstrosageKundliApplication.currentChatStatus variable for getting chat current status like chat completed , running, cancelled
* */

public class OnGoingChatService extends Service implements VolleyResponse {
    private static final int END_CHAT_VALUE = 105;
    public boolean IS_ASTROLOGER_DISCONNECT = false;
    public static final String CHANNEL_ID = "ForegroundServiceOngoingChat";
   // public static final String CHANNEL_ID_BACKGROUND = "BackgroundServiceChatAcceptReject";
   // public static final String CHANNEL_ID_LOCKED = "LockedServiceChatAcceptReject";
    private DatabaseReference astroDbRef;
    private ValueEventListener valueEventListener;
    private Context context;
    private String ASTROLOGER_ACCEPTED_DATA = "";
    private String CHAT_CHANNEL_ID = "";
    private CountDownTimer countDownTimerWaitAcceptChat;
    private String chatJsonObject;
    ConnectChatBean connectChatBean;
    String astrologerName="", astrologerId="", astrologerProfileUrl="", userChatTime="",chatinitiatetype="";
    MediaPlayer player;
    RequestQueue queue;
    private long longTotalVerificationTime = 60000;
    private final long longOneSecond = 1000;

    private CountDownTimer countDownTimer,endChatDisabledTimer;
    public boolean END_CHAT_DATA = false;
    ValueEventListener endChatValueEventListener;
    DatabaseReference readRef;
    VolleyResponse volleyResponse;
   // long endChatTimeShowMilliSeconds = TimeUnit.MINUTES.toMillis(1);

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
            //astrologerDetailBean = (AstrologerDetailBean)intent.getSerializableExtra("chatInitiateAstrologerDetailBean");

//            Gson g = new Gson();
//            connectChatBean = g.fromJson(bundle.getString("connect_chat_bean"), ConnectChatBean.class);
           // Log.d("testChatNewOnGoing", "intent.getExtras() " + CHAT_CHANNEL_ID + "    " + chatJsonObject + "  " + astrologerName + "  " + astrologerProfileUrl + "  " + astrologerId + "  " + userChatTime);

        } catch (Exception e) {
          //  Log.d("testChatNewOnGoing", "intent.getExtras() Exception" + e);
        }

        createNotificationChannel();
        createNotification();
        messageReadFromFirebase(CHAT_CHANNEL_ID);
        timeSetOnTimer(userChatTime);
        initEndChatListener(CHAT_CHANNEL_ID);
        Toast.makeText(this, getResources().getString(R.string.ongoing_chat), Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    private DatabaseReference  messageReadRef;
    private ChildEventListener childEventListener;

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

                        boolean isSeen = dataSnapshot.child("isSeen").getValue(Boolean.class);
                        if(!isSeen && From.equals("Astrologer")){
                            CGlobalVariables.CHAT_NOTIFICATION_QUEUE = CGlobalVariables.CHAT_NOTIFICATION_QUEUE + Text + "\n";
                            displayChatNotification(astrologerName, CGlobalVariables.CHAT_NOTIFICATION_QUEUE);
                        }

//                        if (!AstrosageKundliApplication.wasInBackground) {
//                            CGlobalVariables.CHAT_NOTIFICATION_QUEUE = CGlobalVariables.CHAT_NOTIFICATION_QUEUE + Text + "\n";
//                            displayChatNotification(astrologerName, CGlobalVariables.CHAT_NOTIFICATION_QUEUE);
//                        }

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

        resultIntent = new Intent(this, DashBoardActivity.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        resultIntent.putExtra("ongoing_notification", true);
//        resultIntent.setAction(Intent.ACTION_VIEW);
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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(String response, int method) {
        if (method == END_CHAT_VALUE) {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_COMPLETED;
            CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
            //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
            if (response != null && response.length() > 0) {
                CGlobalVariables.chatTimerTime = 0;
                CUtils.saveAstrologerIDAndChannelID(context, "", "");
            }
        }
    }

    @Override
    public void onError(VolleyError error) {

    }
    private void timeSetOnTimer(String talktime) {

        //talktime = "36:24 minutes";
        if (talktime != null && talktime.length() > 0) {
            String[] timeArray = talktime.split(":");

            if (timeArray != null && timeArray.length > 1) {
                String[] secondArr = timeArray[1].split(" ");
                long minTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(timeArray[0]));
                long secTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(secondArr[0]));
                longTotalVerificationTime = minTime + secTime;
                ////Log.e("TIMER MILLISECOND 2 ", "" + longTotalVerificationTime);
            }
        }
        initializingCountDownTimer(longTotalVerificationTime);
    }

    public void initializingCountDownTimer(long longTotalVerificationTime) {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String text = String.format(Locale.US, "%02d: %02d: %02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    //sendBroadcastOnGoingChat(text);
                    updateStatus(text);
                    CGlobalVariables.chatTimerTime = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    CGlobalVariables.chatTimerTime = 0;
                    try {
                        updateStatus("00:00:00");
                        CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");

                        CUtils.changeFirebaseKeyStatus(CHANNEL_ID, "NA", true, CGlobalVariables.TIME_OVER);
                        chatCompleted(CHANNEL_ID, CGlobalVariables.TIME_OVER);

                        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
                       // unRegisterConnectivityStatusReceiver();
                        stopService(new Intent(context, OnGoingChatService.class));

                        //endChatData();
                    } catch (Exception e) {
                        Log.d("test_ongoing_chat","Exception ==>>"+e);

                        e.printStackTrace();
                    }
                }
            }.start();
        }


            endChatDisabledTimer = new CountDownTimer(AstrosageKundliApplication.endChatTimeShowMilliSeconds, longOneSecond) {
                @Override
                public void onTick(long millisUntilFinished) {
                    AstrosageKundliApplication.endChatTimeShowMilliSeconds = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    AstrosageKundliApplication.endChatTimeShowMilliSeconds = 0;
                }
            }.start();


    }

    private void updateStatus(String time) {
        try {
            Intent intent = new Intent();
            intent.putExtra("rem_time", time);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
            bundle.putString("connect_chat_bean", chatJsonObject);
            bundle.putString("astrologer_name", astrologerName);
            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
            bundle.putString("astrologer_id", astrologerId);
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, chatinitiatetype);
            bundle.putString("userChatTime",changeToMinSec());
            intent.putExtras(bundle);
            OngoingCallChatManager.updateViews(intent);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void sendBroadcastOnGoingChat( String time) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_ONGOING_CHAT);
            intent.putExtra("rem_time", time);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
            bundle.putString("connect_chat_bean", chatJsonObject);
            bundle.putString("astrologer_name", astrologerName);
            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, chatinitiatetype);
            bundle.putString("astrologer_id", astrologerId);
            bundle.putString("userChatTime",changeToMinSec());
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  String changeToMinSec()  {
        try {
            long millis = CGlobalVariables.chatTimerTime;
            String ms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) ,
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            Log.d("TestChatRemainingTime","changeToMinSec ===>>"+ms);
            return ms;
        }catch (Exception e){
            return "00:00";
        }

    }
    public void chatCompleted( String channelID, String remarks) {

        CUtils.cancelNotification(context);
        CGlobalVariables.chatTimerTime = 0;
        CUtils.saveAstrologerIDAndChannelID(context, "", "");
        if (!CUtils.isConnectedWithInternet(context)) {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            //showProgressBar();
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CHAT_URL,this, false, getChatCompleteParams(channelID, remarks), END_CHAT_VALUE).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            callEndChatApi(channelID, remarks);
        }
    }
    private void callEndChatApi(String channelID, String remarks) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body()!=null) {
                    try {
                        AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_COMPLETED;
                        CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                        //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
                        if (response.body() != null ) {
                            CGlobalVariables.chatTimerTime = 0;
                            CUtils.saveAstrologerIDAndChannelID(context, "", "");
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public Map<String, String> getChatCompleteParams(String channelID, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
       // CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/"00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //////Log.e("LoadMore params ", headers.toString());
        return  CUtils.setRequiredParams(headers);
    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Ongoing Chat", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        } catch (Exception e) {

        }
    }
    private void createNotification() {
        try {
            String msg = context.getResources().getString(R.string.text_chat_running_with_astro).replace("##",astrologerName);
            Intent notificationIntent = new Intent(this, DashBoardActivity.class);
            PendingIntent pendingIntent;

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            }

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
            Notification notification;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notification = new Notification.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.ongoing_chat)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(ContextCompat.getColor(this, R.color.colorPurple)).setLargeIcon(icon).setAutoCancel(false).setContentIntent(pendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.ongoing_chat)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(getResources().getColor(R.color.colorPurple)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(pendingIntent).build();
            }

            if (notification != null) {
                startForeground(301, notification);
            }
        } catch (Exception e) {

        }
    }
    private void initEndChatListener(String channelIDD) {
        endChatValueEventListener =     new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    try {
                        END_CHAT_DATA = (boolean) dataSnapshot.getValue();
                        if (END_CHAT_DATA){
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            if (endChatDisabledTimer != null) {
                                endChatDisabledTimer.cancel();
                            }

                            updateStatus("00:00:00");
                            // CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");
                            CUtils.changeFirebaseKeyStatus(CHANNEL_ID, "NA", true, CGlobalVariables.TIME_OVER);
                            chatCompleted(CHANNEL_ID, CGlobalVariables.ASTROLOGER);
                            stopService(new Intent(context, OnGoingChatService.class));
                        }

                    }catch (Exception e){
                        //
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        readRef = AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY);
        readRef.addValueEventListener(endChatValueEventListener);

    }

    @Override
    public void onDestroy() {
        if(readRef!=null && endChatValueEventListener!=null ){
            readRef.removeEventListener(endChatValueEventListener);
        }
        if (messageReadRef != null && childEventListener != null) {
            messageReadRef.removeEventListener(childEventListener);
        }
        super.onDestroy();
        updateStatus("00:00:00");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }
        CUtils.cancelChatNotification(this);
    }
}
