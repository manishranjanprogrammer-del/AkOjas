package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTRO_NO_ANSWER;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.receiver.AgoraCallStatusReceiver;
import com.ojassoft.astrosage.varta.receiver.AstroLiveDataManager;
import com.ojassoft.astrosage.varta.ui.activity.AgoraCallAcceptRejectActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.activities.VideoCallActivity;
import com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgoraCallInitiateService extends Service implements VolleyResponse, GlobalRetrofitResponse {
    public static final String CHANNEL_ID = "ForegroundServiceAgoraCallAcceptReject";
    public static final String CHANNEL_ID_BACKGROUND = "BackgroundServiceAgoraCallAcceptReject";
    public static final String CHANNEL_ID_LOCKED = "LockedServiceAgoraCallAcceptReject";
    private DatabaseReference astroDbRef;
    private ValueEventListener valueEventListener;
    private Context context;
    private String ASTROLOGER_ACCEPTED_DATA = "";
    private String CAll_CHANNEL_ID = "";
    private CountDownTimer countDownTimerWaitAcceptCall;
    private boolean isWaitingStateResolved;
    private boolean isAgoraCallFailedReceiverRegistered;
    private final BroadcastReceiver agoraCallFailedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && CGlobalVariables.SEND_BROADCAST_AGORA_CALL_FAILED.equals(intent.getAction())) {
                handleCallFailedBroadcast();
            }
        }
    };

    String astrologerName, astrologerId, astrologerProfileUrl, userCallTime, callCharge;
    MediaPlayer player;
    RequestQueue queue;
    private static final int GET_FIREBASE_AUTH_TOKEN = 231,END_AGORACALL_VALUE = 324;
    String agoraCallInitiateObj;
    String token, tokenId, consultationType;

    // AstrologerDetailBean astrologerDetailBean;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;
       // Log.d("testAgoraCall", "onStartCommand called");

        queue = VolleySingleton.getInstance(context).getRequestQueue();
        if (!hasValidStartData(intent)) {
            stopSelf(startId);
            return START_NOT_STICKY;
        }
        try {
            Bundle bundle = intent.getExtras();

            Gson gson = new Gson();
            assert bundle != null;
            agoraCallInitiateObj = bundle.getString(CGlobalVariables.AGORA_CALL_INITIATE_OBJECT);
            consultationType = bundle.getString(CGlobalVariables.AGORA_CALL_TYPE);
            CUtils.connectAgoraCallBean = gson.fromJson(agoraCallInitiateObj, ConnectAgoraCallBean.class);
            if (CUtils.connectAgoraCallBean != null) {
               // Log.d("testAgoraCall", "connectAgoraCallBean called");

                CAll_CHANNEL_ID = CUtils.connectAgoraCallBean.getCallsid();
                token = CUtils.connectAgoraCallBean.getTokenid();
                tokenId = CUtils.connectAgoraCallBean.getAgoratokenid();
                userCallTime = CUtils.connectAgoraCallBean.getTalktime();
                callCharge = CUtils.connectAgoraCallBean.getCallcharge();
                //Log.e("testVideoCall", "callCharge="+callCharge);
                try {
                    // Format to max 2 decimal places
                    double charges = Double.parseDouble(callCharge);
                    java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
                    callCharge = df.format(charges);
                }catch (Exception e){
                 //
                }

            }



            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                //Log.d("testAgoraCall", "selectedAstrologerDetailBean called");


                astrologerName = AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
                astrologerProfileUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();
                astrologerId = AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId();
            }
        } catch (Exception e) {
            //Log.d("testAgoraCall", "intent.getExtras() Exception" + e);
        }

        resetWaitingState();
        createNotificationChannel();
        createNotification();
        registerAgoraCallFailedReceiverIfNeeded();
        getDataFromChannel(CAll_CHANNEL_ID);
        initializingCountDownTimer(181 * 1000, CAll_CHANNEL_ID);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                performOperations(CAll_CHANNEL_ID, valueEventListener, CGlobalVariables.ACCEPTED);
//            }
//        },10000);
        return START_REDELIVER_INTENT;
    }

    /**
     * Validates the launch payload required to resume the call-initiation wait state after process recreation.
     */
    private boolean hasValidStartData(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return false;
        }
        String initiateObject = intent.getExtras().getString(CGlobalVariables.AGORA_CALL_INITIATE_OBJECT);
        return !TextUtils.isEmpty(initiateObject);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createNotification() {
        try {
            String msg = context.getResources().getString(R.string.waiting_for_astrologer_to_accept_agora_call_request);
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
                notification = new Notification.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.new_call_request)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(ContextCompat.getColor(this, R.color.colorPurple)).setLargeIcon(icon).setAutoCancel(false).setContentIntent(pendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.new_call_request)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(getResources().getColor(R.color.colorPurple)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(pendingIntent).build();
            }
           // Log.d("testAgoraCall","called 12");
            if (notification != null) {
                startForeground(5, notification);
            }
        } catch (Exception e) {

        }
    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Agora Call Invite", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        } catch (Exception e) {

        }
    }

    public void getDataFromChannel(String channelIDD) {
        /**
         *
         */
        AstrosageKundliApplication.getAppContext().getEndChatData(channelIDD);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(context, "onDataChange called ", Toast.LENGTH_SHORT).show();
                Log.d("agora_call_test", "Called onDataChange Listener");
                if (dataSnapshot != null) {
                    ASTROLOGER_ACCEPTED_DATA = getAstrologerAcceptedState(dataSnapshot);
                    errorLogs = errorLogs + "api ASTROLOGER_ACCEPTED_DATA->"+ASTROLOGER_ACCEPTED_DATA+"\n";
                    if (ASTROLOGER_ACCEPTED_DATA.equalsIgnoreCase("true")) {
                        Log.d("agora_call_test", "Called Accept call from Firebase Listener");
                        performOperations(channelIDD, this, CGlobalVariables.ACCEPTED);
                    } else if (ASTROLOGER_ACCEPTED_DATA.equalsIgnoreCase("false")) {
                        try {
                            CUtils.cancelOnDisconnentEvent(channelIDD);
                            Log.d("agora_call_test", "Called Reject call from Firebase Listener");
                            performOperations(channelIDD, this, CGlobalVariables.REJECTED);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /*
                 * in case of permission denied from firebase
                 * reauthenticate and initiate chat again
                 * */
                if (String.valueOf(error).contains(CGlobalVariables.PERMISSION_DENIED)) {
                    fetchFirebaseAuthTokenFromServer();
                }


            }
        };
        astroDbRef = AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.ASTROLOGER_ACCEPTED_FBD_KEY);
        astroDbRef.addValueEventListener(valueEventListener);

    }

    private void fetchFirebaseAuthTokenFromServer() {

        if (CUtils.isConnectedWithInternet(context)) {
            if (TextUtils.isEmpty(CUtils.getUserLoginPassword(context))) {
                return;
            }
            CUtils.getFirebaseAuthToken(this, GET_FIREBASE_AUTH_TOKEN);
        }
    }

    private void performOperations(String channelIDD, ValueEventListener valueEventListener, String actionType) {
        if (isWaitingStateResolved) {
            return;
        }
        isWaitingStateResolved = true;
        stopWaitingCountdown();
        if (actionType.equals(CGlobalVariables.ACCEPTED)) {

            //CUtils.fcmAnalyticsEvents("astrologer_accept_chat_request_firebase", AstrosageKundliApplication.currentEventType, "");
            if (AstrosageKundliApplication.isActivityVisible()) {
               // Log.d("agora_call_test", "App is in forground ");
               // Log.d("agora_call_test", "  ");
                openAcceptedCallScreen();
                AstroLiveDataManager.updateViews("-1");
                stopWaitingService();
            } else {
                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                if (myKM.isKeyguardLocked()) {
                    //it is lockedw
                 //   Log.d("agora_call_test", "phone is locked ");

                    createLockedScreenNotificationChannel();
                    createLockedScreenNotification();
                } else {
                    //Log.d("agora_call_test", "app is in background or kill ");

                    //it is not locked
                    createNotificationChannelNew();
                    sendCustomPushNotification();
                    // Handler to delay the notification removal
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Cancel the notification after the delay
                            stopForeground(true);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (notificationManager != null) {
                                notificationManager.cancel(4242);  // Cancel notification by ID
                            }

                            // Stop the ringtone if it's playing
                            if (CUtils.player != null) {
                                CUtils.player.stop();
                                CUtils.player.release();
                                CUtils.player = null;
                            }
                        }
                    }, 60000);
                }
            }
        } else {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_REJECTED;
            sendBroadcastMesage(CGlobalVariables.REJECTED);
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            String msg =ASTRO_NO_ANSWER+"@false";
            AstroLiveDataManager.updateViews(msg);
            //sendBroadcastViewTimer(0);
            //CUtils.fcmAnalyticsEvents("astrologer_reject_chat_request", AstrosageKundliApplication.currentEventType, "");
           // Log.d("agora_call_test", "stopService  AstroAcceptRejectService  ");
            stopWaitingService();
        }
        AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.ASTROLOGER_ACCEPTED_FBD_KEY).removeEventListener(valueEventListener);

    }

    /**
     * Clears any listener/timer state left from a previous start before a new wait session begins.
     */
    private void resetWaitingState() {
        isWaitingStateResolved = false;
        stopWaitingCountdown();
        if (astroDbRef != null && valueEventListener != null) {
            astroDbRef.removeEventListener(valueEventListener);
        }
    }

    /**
     * Normalizes the astrologer-accepted Firebase value so string and boolean payloads are both handled.
     */
    private String getAstrologerAcceptedState(@NonNull DataSnapshot dataSnapshot) {
        Object acceptedValue = dataSnapshot.getValue();
        if (acceptedValue == null) {
            return "";
        }
        return String.valueOf(acceptedValue).trim();
    }

    /**
     * Cancels the wait-accept countdown once the service reaches any terminal state.
     */
    private void stopWaitingCountdown() {
        if (countDownTimerWaitAcceptCall != null) {
            countDownTimerWaitAcceptCall.cancel();
            countDownTimerWaitAcceptCall = null;
        }
    }

    /**
     * Launches the accepted call screen directly from the service so the flow does not depend on a foreground broadcast.
     */
    private void openAcceptedCallScreen() {
        try {
            Intent intent;
            if (CGlobalVariables.TYPE_VIDEO_CALL.equals(consultationType)) {
                intent = new Intent(this, VideoCallActivity.class);
            } else {
                intent = new Intent(this, VoiceCallActivity.class);
            }
            intent.setPackage(BuildConfig.APPLICATION_ID);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(CGlobalVariables.AGORA_CALLS_ID, CAll_CHANNEL_ID);
            intent.putExtra(CGlobalVariables.AGORA_TOKEN, token);
            intent.putExtra(CGlobalVariables.AGORA_TOKEN_ID, tokenId);
            intent.putExtra(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            intent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            intent.putExtra(CGlobalVariables.AGORA_CALL_DURATION, userCallTime);
            intent.putExtra(CGlobalVariables.ASTROLOGER_ID, astrologerId);
            startActivity(intent);
        } catch (Exception e) {
            sendBroadcastMesage(CGlobalVariables.ACCEPTED);
        }
    }

    /**
     * Stops the waiting service after the accept/reject flow has been handed off to UI or cleanup logic.
     */
    private void stopWaitingService() {
        stopForeground(true);
        stopSelf();
    }

    private void sendBroadcastMesage(String status) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_AGORA_CALL_ASTROLOGER_ACCEPT_REJECT);
           // Log.d("fdjdslf","consultationType = >>"+consultationType);
            intent.putExtra(CGlobalVariables.AGORA_CALL_TYPE, consultationType);
            intent.putExtra(CGlobalVariables.STATUS, status);
            intent.putExtra(CGlobalVariables.AGORA_CALLS_ID, CAll_CHANNEL_ID);
            intent.putExtra(CGlobalVariables.AGORA_TOKEN, token);
            intent.putExtra(CGlobalVariables.AGORA_TOKEN_ID, tokenId);
            intent.putExtra(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            intent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            intent.putExtra(CGlobalVariables.ASTROLOGER_ID, astrologerId);
            intent.putExtra(CGlobalVariables.AGORA_CALL_DURATION, userCallTime);
            //intent.putExtra("chatInitiateAstrologerDetailBean",astrologerDetailBean);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void sendBroadcastViewTimer(long time) {
//        try {
//            Context context = AstrosageKundliApplication.getAppContext();
//            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_CHAT_WAITING_VIEW);
//            intent.putExtra("rem_time", time);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void initializingCountDownTimer(long longTotalVerificationTime, String channelID) {
        // Log.d("testChatNew", "initializingCountDownTimer  for three min  ");
        countDownTimerWaitAcceptCall = null;
        countDownTimerWaitAcceptCall = new CountDownTimer(longTotalVerificationTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //sendBroadcastViewTimer(millisUntilFinished / 1000);
                AstroLiveDataManager.updateViews((millisUntilFinished / 1000)+"###"+callCharge);

            }

            @Override
            public void onFinish() {
                try {
                    handleWaitingTimerFinished(channelID);
                } catch (Exception e) {
                    //
                }
            }
        }.start();
    }

    /**
     * Registers the local receiver used to react to "Call Failed" push events during wait state.
     */
    private void registerAgoraCallFailedReceiverIfNeeded() {
        if (!isAgoraCallFailedReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(agoraCallFailedReceiver,
                    new IntentFilter(CGlobalVariables.SEND_BROADCAST_AGORA_CALL_FAILED));
            isAgoraCallFailedReceiverRegistered = true;
        }
    }

    /**
     * Forces the wait-accept countdown into its finish cleanup when FCM reports call failure.
     */
    private void handleCallFailedBroadcast() {
        stopWaitingCountdown();
        handleWaitingTimerFinished(CAll_CHANNEL_ID);
    }

    /**
     * Applies the timeout/failure cleanup so the waiting cancel-call view is removed on user side.
     */
    private void handleWaitingTimerFinished(String channelID) {
        if (isWaitingStateResolved) {
            return;
        }
        isWaitingStateResolved = true;
        String msg = ASTRO_NO_ANSWER + "@false";
        AstroLiveDataManager.updateViews(msg);
        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
        CUtils.changeFirebaseKeyStatus(channelID, CGlobalVariables.CANCELLED, true, CGlobalVariables.REMARKS_ASTROLOGER_NO_ANSWER);
        agoraCallComplted(channelID, CGlobalVariables.ASTROLOGER_NO_ANSWER, CGlobalVariables.REMARKS_ASTROLOGER_NO_ANSWER);
        stopWaitingService();
    }

    public void agoraCallComplted(String channelID, String chatStatus, String remarks) {
//        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CALL_URL, this, false, getCallCompleteParams(channelID,chatStatus,remarks), END_AGORACALL_VALUE).getMyStringRequest();
//        stringRequest.setShouldCache(true);
//        queue.add(stringRequest);
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endInternetcall( getCallCompleteParams(channelID,chatStatus,remarks));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    CGlobalVariables.chatTimerTime = 0;
                    CUtils.saveAstrologerIDAndChannelID(context, "", "");
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public Map<String, String> getCallCompleteParams(String channelID, String chatStatus, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, chatStatus);
        headers.put(CGlobalVariables.CHAT_DURATION, "00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(CGlobalVariables.LANGUAGE_CODE));
       // Log.d("check_end_chat_api", "Called from Service Astro Accept and rej");

        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onDestroy() {
        stopWaitingCountdown();
        if (isAgoraCallFailedReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(agoraCallFailedReceiver);
            isAgoraCallFailedReceiverRegistered = false;
        }
        if (astroDbRef != null && valueEventListener != null) {
            astroDbRef.removeEventListener(valueEventListener);
        }
        CUtils.stopDefaultRingtone(player);
        super.onDestroy();
    }

    private void createNotificationChannelNew() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID_BACKGROUND, "Foreground Service Agora Call Invite Background", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void sendCustomPushNotification() {
       // Log.d("testAgoraCall","sendCustomPushNotification");
        String title = getResources().getString(R.string.call_request_accepted);
        String msg = getResources().getString(R.string.astrologer_has_accepted_call_req);
        int notificationId = 4242;
        Intent acceptIntent = null;
        if(consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)){

            acceptIntent = new Intent(this, VideoCallActivity.class);
        }else{
            acceptIntent = new Intent(this, VoiceCallActivity.class);
        }
        acceptIntent.setPackage(BuildConfig.APPLICATION_ID);
        acceptIntent.putExtra(CGlobalVariables.NOTIFICATION_ID, notificationId);
        acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Intent acceptIntent = new Intent(context, ChatWindowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.AGORA_CALL_TYPE, consultationType);
        bundle.putString(CGlobalVariables.AGORA_CALLS_ID, CAll_CHANNEL_ID);
        bundle.putString(CGlobalVariables.AGORA_TOKEN, token);
        bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, tokenId);
        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, userCallTime);
        bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        //launchI.setPackage(BuildConfig.APPLICATION_ID);
        acceptIntent.putExtras(bundle);
        startActivity(acceptIntent);

        PendingIntent acceptPI;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            acceptPI = PendingIntent.getActivity(this, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            acceptPI = PendingIntent.getActivity(this, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent cancelIntent = new Intent(this, AgoraCallStatusReceiver.class);
        cancelIntent.putExtra(CGlobalVariables.ACTION_TYPE, CGlobalVariables.AGORA_CALL_REJECTED_BY_USER);
        cancelIntent.putExtra(CGlobalVariables.NOTIFICATION_ID, notificationId);
        cancelIntent.putExtra(CGlobalVariables.AGORA_CALL_TYPE, consultationType);
        cancelIntent.putExtra(CGlobalVariables.AGORA_CALLS_ID, CAll_CHANNEL_ID);
        cancelIntent.putExtra(CGlobalVariables.AGORA_TOKEN, token);
        cancelIntent.putExtra(CGlobalVariables.AGORA_TOKEN_ID, tokenId);
        cancelIntent.putExtra(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        cancelIntent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        cancelIntent.putExtra(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        cancelIntent.putExtra(CGlobalVariables.AGORA_CALL_DURATION, userCallTime);
        PendingIntent cancelPI;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            cancelPI = PendingIntent.getBroadcast(this, 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            cancelPI = PendingIntent.getBroadcast(this, 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_BACKGROUND).setContentText(msg).setContentTitle(title).setSmallIcon(R.mipmap.icon).setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_CALL).setFullScreenIntent(acceptPI, true).addAction(R.drawable.ic_chat_reject, getString(R.string.reject_text), cancelPI).addAction(R.drawable.ic_chat_accept, getString(R.string.accept_text), acceptPI).setAutoCancel(true);

         CUtils.playDefaultRingtone(this);

        Notification notification = notificationBuilder.build();
        startForeground(notificationId, notification);
    }

    private void createLockedScreenNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID_LOCKED, "Locked Screen Chat Request", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void createLockedScreenNotification() {
        String title = getResources().getString(R.string.call_request_accepted);
        String msg = getResources().getString(R.string.astrologer_has_accepted_call_req);
        int notificationId = 5645;

//        if (consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)) {
//            resultIntent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_VIDEO_CALL_ACTIVITY);
//        } else {
//            resultIntent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_INTERNET_CALL_ACTIVITY);
//        }
        Intent launchI = new Intent(this, AgoraCallAcceptRejectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.AGORA_CALL_TYPE, consultationType);
        bundle.putString(CGlobalVariables.AGORA_CALLS_ID, CAll_CHANNEL_ID);
        bundle.putString(CGlobalVariables.AGORA_TOKEN, token);
        bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, tokenId);
        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, userCallTime);
        bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        //launchI.setPackage(BuildConfig.APPLICATION_ID);
        launchI.putExtras(bundle);
        launchI.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launchI);
        PendingIntent launchPI;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            launchPI = PendingIntent.getActivity(this, 0, launchI, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            launchPI = PendingIntent.getActivity(this, 0, launchI, 0);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_LOCKED).setContentText(msg).setContentTitle(title).setSmallIcon(R.mipmap.icon).setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_CALL).setContentIntent(launchPI).setFullScreenIntent(launchPI, true).setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        startForeground(notificationId, notification);
    }

    @Override
    public void onResponse(String response, int method) {
        if (method == GET_FIREBASE_AUTH_TOKEN) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        String authToken = jsonObject.getString("token");
                        firebaseSignIn(authToken);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(method == END_AGORACALL_VALUE){
            try {
                CGlobalVariables.chatTimerTime = 0;
                CUtils.saveAstrologerIDAndChannelID(context, "", "");
            }catch (Exception e){
               //
            }

        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    private void firebaseSignIn(String authToken) {
        try {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() inside "  );
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            mAuth.signInWithCustomToken(authToken)
//                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        }
//                    });
            mAuth.signInWithCustomToken(authToken).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    /*
                     * in case of permission denied from firebase
                     * reauthenticate and initiate chat again
                     * */
                    getDataFromChannel(CAll_CHANNEL_ID);
                }
            });
        } catch (Exception e) {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() excep " + e.toString() );
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response, int requestCode) {
        if (requestCode == GET_FIREBASE_AUTH_TOKEN) {
            try {
                String myResponse = response.body().string();
                JSONObject jsonObject = new JSONObject(myResponse);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        String authToken = jsonObject.getString("token");
                        firebaseSignIn(authToken);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {//
    }
}
