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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgoraCallOngoingService extends Service implements VolleyResponse {
    private Context context;
    RequestQueue queue;
     long callTimerTime;
    String consultationType;
    String agoraCallSId,agoraToken,agoraTokenId,astrologerName,astrologerProfileUrl,agoraCallDuration,astrologerId,timeRemaining;
    private boolean MIC_STATUS,VIDEO_STATUS;
    VolleyResponse volleyResponse;
    public static final String NOTIFICATION_CHANNEL_ID = "ForegroundServiceOngoingCall";
    private long longTotalVerificationTime = 60000;
    private final long longOneSecond = 1000;
    private static final int END_AGORACALL_VALUE = 106;
    private CountDownTimer countDownTimer;
    ValueEventListener endCallValueEventListener;
    DatabaseReference readRef;
    private FirebaseDatabase mFirebaseInstance;
    private boolean END_CALL_DATA;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;
        this.volleyResponse = this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        try {
            getDataFromIntent(intent); 
            createNotificationChannel();
            createNotification();
            //getDataFromChannel(CHAT_NOTIFICATION_CHANNEL_ID);
            Log.d("agoraCallOngoing","agoraCallDuration == >"+agoraCallDuration);
            timeSetOnTimer(agoraCallDuration);
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
                notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(getResources().getString(R.string.ongoing_call)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(ContextCompat.getColor(this, R.color.colorPurple)).setLargeIcon(icon).setAutoCancel(false).setContentIntent(pendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setContentTitle(getResources().getString(R.string.ongoing_call)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(getResources().getColor(R.color.colorPurple)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(pendingIntent).build();
            }

            if (notification != null) {
                startForeground(301, notification);
            }
        } catch (Exception e) {

        }
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
        Log.d("agoraCallOngoing","longTotalVerificationTime == >"+longTotalVerificationTime);

        initializingCountDownTimer(longTotalVerificationTime);
    }
    public void initializingCountDownTimer(long longTotalVerificationTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
            countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("agoraCallOngoing","initializingCountDownTimer == > called");
                    String text = String.format(Locale.US, "%02d: %02d: %02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    //sendBroadcastOnGoingChat(text);
                    timeRemaining = text;
                    updateStatus(text);
                    callTimerTime = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    Log.d("agoraCallOngoing","onFinish == >"+longTotalVerificationTime);

                    callTimerTime = 0;
                    try {
                        updateStatus("00:00:00");
                        CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, CGlobalVariables.TIME_OVER);
                        voiceCallCompleted(CGlobalVariables.TIME_OVER);

                        // unRegisterConnectivityStatusReceiver();
                        stopService(new Intent(context, AgoraCallOngoingService.class));

                        //endChatData();
                    } catch (Exception e) {
                        Log.d("test_ongoing_chat", "Exception ==>>" + e);

                        e.printStackTrace();
                    }
                }
            }.start();

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
                            updateStatus("00:00:00");
                            // CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");
                            CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, CGlobalVariables.TIME_OVER);
                            voiceCallCompleted(CGlobalVariables.ASTROLOGER);
                            stopService(new Intent(context, AgoraCallOngoingService.class));
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
        readRef = getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY);
        readRef.addValueEventListener(endCallValueEventListener);

    }
    public  DatabaseReference getmFirebaseDatabase(String channelIDD) {
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
            VIDEO_STATUS = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, true);
            consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
            agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
            agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
            agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
            astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
            astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
            agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
            astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onResponse(String response, int method) {

    }

    @Override
    public void onError(VolleyError error) {

    }
    public void voiceCallCompleted(String remark) {

        //CUtils.cancelNotification(context);
        if (!CUtils.isConnectedWithInternet(context)) {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            //showProgressBar();
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CALL_URL, this, false, getCallCompleteParams(remark), END_AGORACALL_VALUE).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endInternetcall( getCallCompleteParams(remark));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        headers.put(CGlobalVariables.CHANNEL_ID, agoraCallSId);
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
            if (agoraCallDuration.length() > 0) {
                timeArray = agoraCallDuration.split(":");
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
    private void updateStatus(String time) {
        try {
            Log.d("agoraCallOngoing","updateStatus == > called 1");

            Intent intent = new Intent();
            intent.putExtra("rem_time", time);
            Bundle bundle = new Bundle();
            bundle.putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, MIC_STATUS);
            bundle.putBoolean(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, VIDEO_STATUS);
            bundle.putString(CGlobalVariables.AGORA_CALL_TYPE,consultationType);
            bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
            bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
            bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
            bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, changeToMinSec());
            bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
            intent.putExtras(bundle);
            OngoingCallChatManager.updateViews(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}
