package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.receiver.OngoingAICallData;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIVoiceCallingService extends Service {

    private Context context;
    long callTimerTime;
    String callSId, token, tokenId,astrologerName,astrologerProfileUrl, callDuration,astrologerId,timeRemaining;
    private boolean MIC_STATUS;
    public static final String NOTIFICATION_CHANNEL_ID = "ForegroundServiceOngoingCall";
    private long longTotalVerificationTime = 60000;
    private final long longOneSecond = 1000;
    public static  String  REMAINING_TIME=null;
    private CountDownTimer countDownTimer;
    ValueEventListener endCallValueEventListener;
    DatabaseReference readRef;
    private FirebaseDatabase mFirebaseInstance;
    private boolean END_CALL_DATA;
    private Bitmap astrologerProfileBitmap;
    private boolean isForegroundServiceStarted = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;


        try {
            getDataFromIntent(intent);
            createNotificationChannel();
            if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
                String  newAstrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
                createNotification(newAstrologerProfileUrl);
            }else {
                notificationWithNoAstrologerImage();
            }
            //Log.d("notification","service CallDuration == >"+ callDuration);
            timeSetOnTimer(callDuration);
            initEndChatListener();
            Toast.makeText(this, getResources().getString(R.string.ongoing_call), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d("ServiceCallRun","Error in onStartCommand ==>>  "+e);
        }
        return START_STICKY;
    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Foreground Service Ongoing Call", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        } catch(Exception e){

        }
    }

    private void createNotification(String astrologerImageUrl) {
        try {

            Glide.with(this)
                    .asBitmap()
                    .load(astrologerImageUrl)
                    .into(new com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            astrologerProfileBitmap = resource;

                            Notification notification = buildNotification(resource,callDuration);
                            startForeground(301, notification);
                            isForegroundServiceStarted = true;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            notificationWithNoAstrologerImage();
                        }
                    });

        } catch (Exception e) {

        }
    }

    private void notificationWithNoAstrologerImage() {
        astrologerProfileBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ai_astro_default);
        Notification notification = buildNotification(astrologerProfileBitmap,callDuration);
        startForeground(301, notification);
        isForegroundServiceStarted = true;
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
        //Log.d("agoraCallOngoing","longTotalVerificationTime == >"+longTotalVerificationTime);

        initializingCountDownTimer(longTotalVerificationTime);
    }

    public void initializingCountDownTimer(long longTotalVerificationTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d("agoraCallOngoing","initializingCountDownTimer == > called");
                timeRemaining = String.format(Locale.US, " %02d:%02d",  TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                upadteViewLiveDate();
                REMAINING_TIME = timeRemaining;
                callTimerTime = millisUntilFinished;
                CGlobalVariables.chatTimerTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FINISH_CALL_ACTIVITY_ACTION);
                intent.putExtra(CGlobalVariables.REMARKS,CGlobalVariables.TIME_OVER);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                callTimerTime = 0;
                CGlobalVariables.chatTimerTime = 0;
                Log.d("agoraCallOngoing","onFinish == >"+longTotalVerificationTime);
            }
        }.start();

    }


    void upadteViewLiveDate(){
        Intent intent = new Intent();
        intent.putExtras(getIntentBundle());
        OngoingAICallData.getOngoingChatLiveData().setOngoingChatView(intent);
    }


    private void initEndChatListener() {
        endCallValueEventListener =     new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    try {
                        END_CALL_DATA = (boolean) dataSnapshot.getValue();
                        if (END_CALL_DATA){
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            updateNotification(astrologerProfileBitmap,"00:00:00");
                            // CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");
                            CUtils.changeFirebaseKeyStatus(callSId, "NA", true, CGlobalVariables.TIME_OVER);
                            voiceCallCompleted(CGlobalVariables.TIME_OVER);
                            stopSelf();
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
        readRef = getmFirebaseDatabase(callSId).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY);
        readRef.addValueEventListener(endCallValueEventListener);

    }

    public DatabaseReference getmFirebaseDatabase(String channelIDD) {
        if (channelIDD == null || channelIDD.isEmpty()) {
            channelIDD = "";
        }
        if (mFirebaseInstance == null) {
            mFirebaseInstance = FirebaseDatabase.getInstance();
        }
        return mFirebaseInstance.getReference(CGlobalVariables.CHANNELS).child(channelIDD);
    }

    private void getDataFromIntent(Intent intent) {
        if (intent != null) {
            MIC_STATUS = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_MIC_STATUS, true);
            //VIDEO_STATUS = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, true);
            callSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
            token = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
            tokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
            astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
            astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
            callDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
            astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);

            if(TextUtils.isEmpty(astrologerName)){
                astrologerName = astrologerName.replace("+", " ");
            }
        }
    }

    public void voiceCallCompleted(String remark) {


        if (!CUtils.isConnectedWithInternet(context)) {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endInternetcall(getCallCompleteParams(remark));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //
                    Intent intent = new Intent(CGlobalVariables.FINSIH_ACTIVITY_ACTION);
                    CGlobalVariables.chatTimerTime = 0;
                    sendBroadcast(intent);
                    stopSelf();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (!TextUtils.isEmpty(callSId)) {
                        getmFirebaseDatabase(callSId).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY)
                                .setValue(true);
                        CGlobalVariables.chatTimerTime = 0;
                        Intent intent = new Intent(CGlobalVariables.FINSIH_ACTIVITY_ACTION);
                        sendBroadcast(intent);
                        stopSelf();
                    }
                }
            });
        }
    }

    public Map<String, String> getCallCompleteParams(String remarks) {

        HashMap<String, String> headers = new HashMap<String, String>();
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/timeChangeInSecond(timeRemaining));
        headers.put(CGlobalVariables.CHANNEL_ID, callSId);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("LoadMore params ", headers.toString());
        return CUtils.setRequiredParams(headers);
    }

    private String timeChangeInSecond(String userRemainingTime) {
        //talktime = "36:24 minutes";
        String userChatDuration = "00";
        long totalTime = (long) 0.0, remainingUserTime = (long) 0.0, actualChatUserTime = (long) 0.0;
        String[] timeArray;
        try {
            if (callDuration.length() > 0) {
                timeArray = callDuration.split(":");
                if (timeArray != null && timeArray.length > 1) {
                    String[] secondArr = timeArray[1].split(" ");
                    long minTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(timeArray[0]));
                    long secTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(secondArr[0]));
                    totalTime = minTime + secTime;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            if (!userRemainingTime.equals("00:00:00")) {
                String[] remainingTimeArr = userRemainingTime.split(":");
                long hourTime = TimeUnit.HOURS.toMillis(Integer.parseInt(remainingTimeArr[0]));
                long minTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(remainingTimeArr[1]));
                long secTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(remainingTimeArr[2]));
                remainingUserTime = hourTime + minTime + secTime;
                ////Log.e("TIMER remaining", "" + remainingUserTime);
            }

            actualChatUserTime = totalTime - remainingUserTime;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(actualChatUserTime);
            int actualSeconds = (int) seconds;
            userChatDuration = "" + actualSeconds;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userChatDuration;
    }



    public  String changeToMinSec()  {
        try {
            long millis = callTimerTime;
            String ms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) ,
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            Log.d("TestChatRemainingTime","changeToMinSec ===>>"+ms);
            return ms;
        }catch (Exception e){
            return "00:00";
        }

    }

    public void updateNotification(Bitmap astrologerIcon,String newContentText) {
        Notification notification = buildNotification(astrologerIcon,newContentText);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(301, notification);
    }

    @SuppressLint("NewApi")
    private Notification buildNotification(Bitmap icon, String timeRemaining) {

        String msg = context.getResources().getString(R.string.ongoing_call);
        Intent notificationIntent = new Intent(this, AIVoiceCallingActivity.class);
        notificationIntent.putExtras(getIntentBundle());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent hangUpIntent = new Intent(this, AIVoiceCallingActivity.class);
        hangUpIntent.setAction(CGlobalVariables.HANG_UP_ACTION);
        PendingIntent hangUpPendingIntent = PendingIntent.getActivity(
                this, 0, hangUpIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent speakerNotification = new Intent(this, AIVoiceCallingActivity.class);
        speakerNotification.setAction(CGlobalVariables.SPEAKER_ACTION);
        PendingIntent speakerPendingIntent = PendingIntent.getActivity(
                this, 0, speakerNotification,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent micNotification = new Intent(this, AIVoiceCallingActivity.class);
        micNotification.setAction(CGlobalVariables.MIC_ACTION);
        PendingIntent micPendingIntent = PendingIntent.getActivity(
                this, 0, micNotification,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (icon == null) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
        }

        Notification notification = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Person caller = new Person.Builder()
                        .setName(astrologerName.replace("+", " "))
                        .setImportant(true)
                        .setIcon(Icon.createWithAdaptiveBitmap(icon))
                        .build();

                Notification.Action speakerAction = new Notification.Action.Builder(
                        Icon.createWithResource(this,R.drawable.speaker),
                        "Speaker",
                        speakerPendingIntent
                ).build();

                Notification.Action micAction = new Notification.Action.Builder(
                        Icon.createWithResource(this,R.drawable.ic_live_mute),
                        "Mic",
                        micPendingIntent
                ).build();

                // 🔑 Main countdown settings
                notification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(astrologerName.replace("+", " "))
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.calling_icon_)
                        .setCategory(Notification.CATEGORY_CALL)
                        .setOngoing(true)
                        .setUsesChronometer(true)             // show timer
                        .setChronometerCountDown(true)        // countdown
                        .setWhen(getEndTimeMillisFromRemaining(timeRemaining))               // end time for countdown
                        .setContentIntent(pendingIntent)
                        .addAction(speakerAction)
                        .addAction(micAction)
                        .setStyle(Notification.CallStyle.forOngoingCall(caller, hangUpPendingIntent))
                        .setOnlyAlertOnce(true)
                        .build();
            } else {
                RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
                customView.setTextViewText(R.id.title_text, astrologerName.replace("+", " "));
                customView.setTextViewText(R.id.description_text, msg);
                customView.setOnClickPendingIntent(R.id.button_hangup, hangUpPendingIntent);
                customView.setImageViewBitmap(R.id.astro_avatar, icon);
                customView.setImageViewBitmap(R.id.icon, BitmapFactory.decodeResource(context.getResources(), R.drawable.calling_icon_));

                // System countdown for older versions
                notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(astrologerName.replace("+", " "))
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.calling_icon_)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setUsesChronometer(true)
                        .setChronometerCountDown(true)
                        .setWhen(getEndTimeMillisFromRemaining(timeRemaining))   // countdown target
                        .setOnlyAlertOnce(true)
                        .build();
            }
        }catch (Exception e){
            //
        }
        return notification;
    }

    private Bundle getIntentBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CGlobalVariables.IS_FROM_RETURN_TO_CALL, true);
        bundle.putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, MIC_STATUS);
        bundle.putString(CGlobalVariables.AGORA_CALL_TYPE, CGlobalVariables.TYPE_VOICE_CALL);
        bundle.putString(CGlobalVariables.AGORA_CALLS_ID, callSId);
        bundle.putString(CGlobalVariables.AGORA_TOKEN, token);
        bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, tokenId);
        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, callDuration);
        bundle.putString("time_remaining", timeRemaining);
        bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        return bundle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        OngoingAICallData.getOngoingChatLiveData().setOngoingChatView(null);
        if (readRef != null && endCallValueEventListener != null) {
            readRef.removeEventListener(endCallValueEventListener);
        }
        Log.d("notification","service onDestroy");
    }

    private long getEndTimeMillisFromRemaining(String timeRemaining) {
        if (timeRemaining == null || !timeRemaining.contains(":")) {
            return System.currentTimeMillis();
        }

        try {
            String[] parts = timeRemaining.split(":");
            int minutes = Integer.parseInt(parts[0].trim());
            int seconds = Integer.parseInt(parts[1].trim());

            long durationMillis = (minutes * 60L + seconds) * 1000L;

            return System.currentTimeMillis() + durationMillis;
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }
}
