package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTRO_NO_ANSWER;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
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
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.R;


import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.ConnectChatBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.receiver.AstroLiveDataManager;
import com.ojassoft.astrosage.varta.receiver.ChatStatusReciever;
import com.ojassoft.astrosage.varta.ui.activity.ChatAcceptRejectActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AstroAcceptRejectService extends Service implements VolleyResponse, GlobalRetrofitResponse {
    public static final String CHANNEL_ID = "ForegroundServiceChatAcceptReject";
    public static final String CHANNEL_ID_BACKGROUND = "BackgroundServiceChatAcceptReject";
    public static final String CHANNEL_ID_LOCKED = "LockedServiceChatAcceptReject";
    private DatabaseReference astroDbRef;
    private ValueEventListener valueEventListener;
    private Context context;
    private String ASTROLOGER_ACCEPTED_DATA = "";
    private String CHAT_CHANNEL_ID = "";
    private CountDownTimer countDownTimerWaitAcceptChat;
    private String chatJsonObject;
    ConnectChatBean connectChatBean;
    String astrologerName, astrologerId, astrologerProfileUrl, userChatTime;
    MediaPlayer player;
    RequestQueue queue;
    private static final int GET_FIREBASE_AUTH_TOKEN = 231;
    boolean isFreeHumanRandomChat;
    // AstrologerDetailBean astrologerDetailBean;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;

        queue = VolleySingleton.getInstance(context).getRequestQueue();
        if (!hasValidStartData(intent)) {
            stopSelf(startId);
            return START_NOT_STICKY;
        }
        try {

            Bundle bundle = intent.getExtras();
            CHAT_CHANNEL_ID = bundle.getString(CGlobalVariables.CHAT_USER_CHANNEL);
            chatJsonObject = bundle.getString(CGlobalVariables.CONNECT_CHAT_BEAN);
            astrologerName = bundle.getString(CGlobalVariables.ASTROLOGER_NAME);
            astrologerProfileUrl = bundle.getString(CGlobalVariables.ASTROLOGER_PROFILE_URL);
            astrologerId = bundle.getString(CGlobalVariables.CHAT_ASTROLOGER_ID);
            userChatTime = bundle.getString(CGlobalVariables.USER_CHAT_TIME);
            isFreeHumanRandomChat = bundle.getBoolean(CGlobalVariables.IS_FREE_HUMAN_RANDOM_CHAT,false);
        } catch (Exception e) {
           // Log.d("testChatNew", "intent.getExtras() Exception" + e);
        }
        if(AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
            CUtils.sendLogDataRequest(AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), CHAT_CHANNEL_ID, "AstroAcceptRejectService, bottom yellow strip is showing & waiting for astrologer to be accept");
        }
        createNotificationChannel();
        createNotification();
        getDataFromChannel(CHAT_CHANNEL_ID);
        initializingCountDownTimer(181 * 1000, CHAT_CHANNEL_ID);
        return START_REDELIVER_INTENT;
    }

    /**
     * Validates the minimum chat-initiation state required before the foreground wait service starts.
     */
    private boolean hasValidStartData(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return false;
        }
        Bundle bundle = intent.getExtras();
        return !TextUtils.isEmpty(bundle.getString(CGlobalVariables.CHAT_USER_CHANNEL))
                && !TextUtils.isEmpty(bundle.getString(CGlobalVariables.CONNECT_CHAT_BEAN));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createNotification() {
        try {
            String msg = context.getResources().getString(R.string.waiting_for_astrologer_to_accept_chat);
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
                notification = new Notification.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.new_chat_resuest)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(ContextCompat.getColor(this, R.color.colorPurple)).setLargeIcon(icon).setAutoCancel(false).setContentIntent(pendingIntent).build();
            } else {
                notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(getResources().getString(R.string.new_chat_resuest)).setContentText(msg).setSmallIcon(R.drawable.astrosage_app_icon).setColor(getResources().getColor(R.color.colorPurple)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentIntent(pendingIntent).build();
            }

            if (notification != null) {
                startForeground(2, notification);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Chat Invite", NotificationManager.IMPORTANCE_DEFAULT);
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
                if (dataSnapshot != null) {
                    ASTROLOGER_ACCEPTED_DATA = (String) dataSnapshot.getValue();
                    if(AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        CUtils.sendLogDataRequest(AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), CHAT_CHANNEL_ID, "AstroAcceptRejectService, astrologer accepted value is " + ASTROLOGER_ACCEPTED_DATA);
                    }
                    if (ASTROLOGER_ACCEPTED_DATA.equalsIgnoreCase("true")) {
                        Log.d("chat_test_new", "Called Accept chat from Firebase Listener");

                        performOperations(channelIDD, this, CGlobalVariables.ASTROLOGER_ACCEPTED);
                    } else if (ASTROLOGER_ACCEPTED_DATA.equalsIgnoreCase("false")) {
                        try {
                            CUtils.cancelOnDisconnentEvent(channelIDD);
                            Log.d("chat_test_new", "Called Reject chat from Firebase Listener");

                            performOperations(channelIDD, this, CGlobalVariables.ASTROLOGER_REJECTED);
                            CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.ASTROLOGER_NO_ANSWER;
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
                if (String.valueOf(error).contains(CGlobalVariables.PERMISSION_DENIED)){
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
        if (actionType.equals(CGlobalVariables.ASTROLOGER_ACCEPTED)) {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_ACCEPTED;
            AstrosageKundliApplication.channelIdTempStore = "";
            CUtils.fcmAnalyticsEvents("astrologer_accept_chat_request_firebase", AstrosageKundliApplication.currentEventType, "");
            if (countDownTimerWaitAcceptChat != null) {
                countDownTimerWaitAcceptChat.cancel();
            }
            if (AstrosageKundliApplication.isActivityVisible()) {
                Log.d("chat_test_new", "App is in forground ");
                Log.d("chat_test_new", "  ");
                sendBroadcastMesage(CGlobalVariables.ASTROLOGER_ACCEPTED, chatJsonObject);
                if (countDownTimerWaitAcceptChat != null) {
                    countDownTimerWaitAcceptChat.cancel();
                }
                AstroLiveDataManager.updateViews("-1");
                //sendBroadcastViewTimer(0);
                Log.d("chat_test_new", "stopService  AstroAcceptRejectService  ");

                stopService(new Intent(context, AstroAcceptRejectService.class));
            } else {
                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                if (myKM.isKeyguardLocked()) {
                    //it is locked
                    Log.d("chat_test_new", "phone is locked ");

                    createLockedScreenNotificationChannel();
                    createLockedScreenNotification();
                } else {
                    Log.d("chat_test_new", "app is in background or kill ");

                    //it is not locked
                    createNotificationChannelNew();
                    sendCustomPushNotification();
                }
            }
        } else {
            Log.d("TestChatAI", "ASTROLOGER_REJECTED");
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_REJECTED;
            sendBroadcastMesage(CGlobalVariables.ASTROLOGER_REJECTED, chatJsonObject);
            CGlobalVariables.chatTimerTime = 0;
            AstrosageKundliApplication.chatTimerRemainingTime = 0;
            AstrosageKundliApplication.chatJsonObject = "";
            AstrosageKundliApplication.channelIdTempStore = "";
            CUtils.saveAstrologerIDAndChannelID(context, "", "");
            String msg =ASTRO_NO_ANSWER+"@"+isFreeHumanRandomChat;
            Log.d("TestChatAI", "msg="+msg);
            AstroLiveDataManager.updateViews(msg);
            //sendBroadcastViewTimer(0);
            CUtils.fcmAnalyticsEvents("astrologer_reject_chat_request", AstrosageKundliApplication.currentEventType, "");
            Log.d("testChatNew", "stopService  AstroAcceptRejectService  ");
            stopService(new Intent(context, AstroAcceptRejectService.class));
            }
        AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.ASTROLOGER_ACCEPTED_FBD_KEY).removeEventListener(valueEventListener);

    }

    private void sendBroadcastMesage(String status, String chatJsonObject) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_CHAT_ACCEPTED_OR_REJCET);
            intent.putExtra(CGlobalVariables.STATUS, status);
            intent.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
            intent.putExtra(CGlobalVariables.CONNECT_CHAT_BEAN, chatJsonObject);
            intent.putExtra(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            intent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            intent.putExtra(CGlobalVariables.CHAT_ASTROLOGER_ID, astrologerId);
            intent.putExtra(CGlobalVariables.USER_CHAT_TIME, userChatTime);
            //intent.putExtra("chatInitiateAstrologerDetailBean",astrologerDetailBean);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastViewTimer(long time) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_CHAT_WAITING_VIEW);
            intent.putExtra("rem_time", time);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializingCountDownTimer(long longTotalVerificationTime, String channelID) {
       // Log.d("testChatNew", "initializingCountDownTimer  for three min  ");
        countDownTimerWaitAcceptChat = null;
        countDownTimerWaitAcceptChat = new CountDownTimer(longTotalVerificationTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //sendBroadcastViewTimer(millisUntilFinished / 1000);
                AstroLiveDataManager.updateViews(String.valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                try {
                    //Log.d("testChatNew", "initializingCountDownTimer  onFinish  ");
                    //Log.d("testChatNew", "AstrosageKundliApplication  data temp cleared  ");
                    //sendBroadcastViewTimer(0);
                    String msg =ASTRO_NO_ANSWER+"@"+isFreeHumanRandomChat;
                    AstroLiveDataManager.updateViews(msg);
                    AstrosageKundliApplication.chatTimerRemainingTime = 0;
                    AstrosageKundliApplication.chatJsonObject = "";
                    AstrosageKundliApplication.channelIdTempStore = "";
                    CUtils.saveAstrologerIDAndChannelID(context, "", "");
                    CUtils.fcmAnalyticsEvents("time_over_waiting_timer_dialog", CGlobalVariables.DISMISS_DIALOG_EVENT, "");
                    CUtils.changeFirebaseKeyStatus(channelID, CGlobalVariables.CANCELLED, true, CGlobalVariables.REMARKS_ASTROLOGER_NO_ANSWER);
                   // Log.d("testChatNew", "stopService  AstroAcceptRejectService  ");
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_CANCELED;
                    chatCompleted(channelID, CGlobalVariables.ASTROLOGER_NO_ANSWER, CGlobalVariables.REMARKS_ASTROLOGER_NO_ANSWER);
                    stopService(new Intent(context, AstroAcceptRejectService.class));
                } catch (Exception e) {
                    //
                }
            }
        }.start();
    }

    public void chatCompleted(String channelID, String chatStatus, String remarks) {
        if (context == null) return;
        CUtils.cancelNotification(context);
        CGlobalVariables.chatTimerTime = 0;
        if (CUtils.isConnectedWithInternet(context)) {
            //userChatStatus = chatStatus;
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CHAT_URL, this
//                    , false, getChatCompleteParams(channelID, chatStatus, remarks), 111).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            callEndChatApi(channelID,remarks,chatStatus);
        }
    }
    private void callEndChatApi(String channelID, String remarks, String chatStatus) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, chatStatus, remarks), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body()!=null) {
                    try {
                        CGlobalVariables.chatTimerTime = 0;
                        CUtils.saveAstrologerIDAndChannelID(context, "", "");
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

    public Map<String, String> getChatCompleteParams(String channelID, String chatStatus, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, chatStatus);
        headers.put(CGlobalVariables.CHAT_DURATION, "00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(CGlobalVariables.LANGUAGE_CODE));

        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onDestroy() {
        if (countDownTimerWaitAcceptChat != null) {
            countDownTimerWaitAcceptChat.cancel();
        }
        if (astroDbRef != null && valueEventListener != null) {
            astroDbRef.removeEventListener(valueEventListener);
        }
        CUtils.stopDefaultRingtone(player);
        super.onDestroy();
    }

    private void createNotificationChannelNew() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID_BACKGROUND, "Foreground Service Chat Invite Background", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void sendCustomPushNotification() {
        String title = getResources().getString(R.string.chat_request_accepted);
        String msg = getResources().getString(R.string.astrologer_has_accepted_chat_req);
        int notificationId = 4242;

        Intent acceptIntent = new Intent(context, ChatWindowActivity.class);
        acceptIntent.putExtra(CGlobalVariables.NOTIFICATION_ID, notificationId);
        acceptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);

        bundle.putString(CGlobalVariables.CONNECT_CHAT_BEAN, chatJsonObject);
        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.CHAT_ASTROLOGER_ID, astrologerId);
        bundle.putString(CGlobalVariables.USER_CHAT_TIME, userChatTime);
        //bundle.putSerializable("chatInitiateAstrologerDetailBean",astrologerDetailBean);

        acceptIntent.putExtras(bundle);
        PendingIntent acceptPI;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            acceptPI = PendingIntent.getActivity(this, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            acceptPI = PendingIntent.getActivity(this, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent cancelIntent = new Intent(this, ChatStatusReciever.class);
        cancelIntent.putExtra(CGlobalVariables.CHAT_ACTION_TYPE, CGlobalVariables.CHAT_REJECTED_BY_USER);
        cancelIntent.putExtra(CGlobalVariables.NOTIFICATION_ID, notificationId);
        cancelIntent.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
        cancelIntent.putExtra(CGlobalVariables.CONNECT_CHAT_BEAN, chatJsonObject);
        cancelIntent.putExtra(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        cancelIntent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        cancelIntent.putExtra(CGlobalVariables.CHAT_ASTROLOGER_ID, astrologerId);
        cancelIntent.putExtra(CGlobalVariables.USER_CHAT_TIME, userChatTime);
        PendingIntent cancelPI;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            cancelPI = PendingIntent.getBroadcast(this, 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            cancelPI = PendingIntent.getBroadcast(this, 2, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        try {


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_BACKGROUND).setContentText(msg).setContentTitle(title).setSmallIcon(R.mipmap.icon).setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_CALL).setFullScreenIntent(acceptPI, true).addAction(R.drawable.ic_chat_reject, getString(R.string.reject_text), cancelPI).addAction(R.drawable.ic_chat_accept, getString(R.string.accept_text), acceptPI).setAutoCancel(true);

            player = CUtils.playDefaultRingtone(this);

            Notification notification = notificationBuilder.build();
            startForeground(notificationId, notification);
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void createLockedScreenNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID_LOCKED, "Locked Screen Chat Request", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void createLockedScreenNotification() {
        String title = getResources().getString(R.string.chat_request_accepted);
        String msg = getResources().getString(R.string.astrologer_has_accepted_chat_req);
        int notificationId = 4243;

        Intent launchI = new Intent(this, ChatAcceptRejectActivity.class);
        launchI.putExtra(CGlobalVariables.NOTIFICATION_ID, notificationId);
        Bundle bundle = new Bundle();
        bundle.putString("msg", getResources().getString(R.string.astrologer_accepted_chat_request));
        bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
        bundle.putString(CGlobalVariables.CONNECT_CHAT_BEAN, chatJsonObject);
        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.CHAT_ASTROLOGER_ID, astrologerId);
        bundle.putString(CGlobalVariables.USER_CHAT_TIME, userChatTime);
        //bundle.putSerializable("chatInitiateAstrologerDetailBean",astrologerDetailBean);
        launchI.putExtras(bundle);
        launchI.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent launchPI;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            launchPI = PendingIntent.getActivity(this, 0, launchI, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            launchPI = PendingIntent.getActivity(this, 0, launchI, 0);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_LOCKED).setContentText(msg).setContentTitle(title).setSmallIcon(R.mipmap.icon).setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_CALL).setContentIntent(launchPI).setFullScreenIntent(launchPI, true).setAutoCancel(true);
        try {
            Notification notification = notificationBuilder.build();
            startForeground(notificationId, notification);
        }catch (Exception e){
            e.fillInStackTrace();
        }

    }

    @Override
    public void onResponse(String response, int method) {
//        if (response != null && response.length() > 0) {
//            if (method == 111) {
//                CGlobalVariables.chatTimerTime = 0;
//                CUtils.saveAstrologerIDAndChannelID(context, "", "");
//            }
//        }
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
                    getDataFromChannel(CHAT_CHANNEL_ID);
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
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }


    private void startRandomAIChat(Context context){
        try{
            Log.e("TestChatAI", "startRandomAIChat()");
            Activity activity = (Activity) context;
            UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
            Log.e("TestChatAI", "startRandomAIChat() userProfileData="+userProfileData);
            ChatUtils.getInstance(activity).startAIChatRandom(userProfileData,CGlobalVariables.AI_FREE_CHAT_AFTER_ASTRO_NO_ANSWER);
        } catch (Exception e){
            Log.e("TestChatAI", "startRandomAIChat() e="+e);
        }
    }
}
