package com.ojassoft.vartalive.activities;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISVIDEOMUTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AgoraCallOngoingService;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.RtcEngineConfig;
import io.agora.rtc.models.ChannelMediaOptions;
import io.agora.rtc.models.ClientRoleOptions;

public class VoiceCallActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener, VolleyResponse {
    Context context;
    CircularNetworkImageView civInternetCallDP;
    ConstraintLayout clInternetCallTVContainer, clInternetCallParent;
    TextView tvInternetCallAstroName,tvInternetCallTimer,remoteUserOffline;
    ImageView ivInternetCallSpeaker,ivInternetCallMic,ivInternetCallEnd;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int PERMISSION_REQ_ID = 22;
    CustomProgressDialog pd;
    RequestQueue queue;
    private long longTotalVerificationTime = 60000;
   // private final String appId = "388e87b2ab364a1fa6a7837df4a9c988";
    private String channelName,token ;
    private int uid = 0;
    private boolean isJoined = false,END_CALL_DATA;
    private String CALL_STATUS = "running";
    private int intentNotificationId;
    private ConnectAgoraCallBean connectAgoraCallBean;
    private RtcEngine agoraEngine;
    CountDownTimer countDownTimer;
    String agoraCallSId,agoraToken,agoraTokenId,astrologerName,astrologerProfileUrl,agoraCallDuration,astrologerId,timeRemaining;
    private boolean MIC_STATUS, VIDEO_STATUS,isCallConnected,IS_FROM_JOIN_CALL,isRequestForEndCall;
    private static final int END_AGORACALL_VALUE = 106,AGORA_CALL_ACCEPTED = 104;
    private static final String[] REQUESTED_PERMISSIONS = {
                    Manifest.permission.RECORD_AUDIO };
    ValueEventListener endCallValueEventListener,audioEventListener;
    DatabaseReference readRef,audioDBRef;
    private String audioStatus = CGlobalVariables.STATUS_ON_AUDIO;
    private ConstraintLayout clMuteInfo;
    private FirebaseDatabase mFirebaseInstance;
    Handler handler;
    Runnable runnable;
    private static boolean isRemoteUserJoined;
    View guideView;

    private Handler connectivityTimer;
    private Runnable connectivityRunnable;
    private ValueEventListener valueEventListenerNetConnection;
    private boolean isConnectivityDisconnected;
    private BottomSheetDialog NetworkBottomSheetDialog;
    private boolean showPoorNetworkView = true;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isRemoteUserJoined = true;
                    timeSetOnTimer(agoraCallDuration);
                }
            });
        }
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            //showMessage(getResources().getString(R.string.you_joined_voice_call));
        }
        @Override
        public void onUserOffline(int uid, int reason) {
            //showMessage(getResources().getString(R.string.astro_offline_from_call));
        }
        @Override
        public void onLeaveChannel(RtcStats 	stats) {
            //showMessage("You have leaved the voice call." );
        }

        @Override
        public void onLastmileProbeResult(LastmileProbeResult result) {
            agoraEngine.stopLastmileProbeTest();
            // The result object contains the detailed test results that help you
            // manage call quality, for example, the downlink jitter.
            // showMessage("Downlink jitter: " + result.downlinkReport.jitter);
        }
        @Override
        public void onLastmileQuality(int quality) {
            runOnUiThread(() -> updateNetworkStatus(quality));
        }
        @Override
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
            // Use downlink network quality to update the network status
            runOnUiThread(() -> updateNetworkStatus(rxQuality));
        }
    };
    private void updateNetworkStatus(int quality){
        if (quality > 0 && quality < 3){
            hidePoorNetworkView();
            //showPoorNetworkView();
            //networkStatus.setBackgroundColor(Color.GREEN);
        }
        else if (quality <= 4) {
            hidePoorNetworkView();
            //networkStatus.setBackgroundColor(Color.YELLOW);
        }
        else if (quality <= 6) {
            showPoorNetworkView();
            //networkStatus.setBackgroundColor(Color.RED);
        }
        else {
            showPoorNetworkView();
            //networkStatus.setBackgroundColor(Color.WHITE);
        }
    }
    private void hidePoorNetworkView() {
        if (NetworkBottomSheetDialog != null) {
            NetworkBottomSheetDialog.dismiss();
        }
    }
    private void showPoorNetworkView() {
        if (NetworkBottomSheetDialog != null) {
            if (!NetworkBottomSheetDialog.isShowing() && showPoorNetworkView) {
                showBottomSheetDialog();
            }
        } else {
            if(showPoorNetworkView){
                showBottomSheetDialog();
            }
        }
    }
    private void showBottomSheetDialog() {
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.OPEN_POOR_NETWORK_CONNECTION_VIEW, CGlobalVariables.SHOW_DIALOG_EVENT, "");
            if (NetworkBottomSheetDialog != null) {
                NetworkBottomSheetDialog.dismiss();
                NetworkBottomSheetDialog = null;
            }
            NetworkBottomSheetDialog = new BottomSheetDialog(this);
            NetworkBottomSheetDialog.setContentView(R.layout.network_status_layout);
            Objects.requireNonNull(NetworkBottomSheetDialog.getWindow()).findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
            ImageView close_network_view = NetworkBottomSheetDialog.findViewById(R.id.close_network_view);
            close_network_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NetworkBottomSheetDialog.dismiss();
                }
            });
            showPoorNetworkView = false;
            NetworkBottomSheetDialog.show();
            NetworkBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.CLOSE_POOR_NETWORK_CONNECTION_VIEW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showPoorNetworkView = true;
                        }
                    },10000);
                }
            });
        } catch (Exception e) {
            //
        }

    }
    private boolean isInPictureInPictureMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_call);
        context = this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        handler = new Handler();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        getDataFromIntent(getIntent());
        initViews();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean onNotificationClick = intent.getBooleanExtra("ongoing_notification", false);
        if(!onNotificationClick){
            getDataFromIntent(intent);
            initViews();
        }

    }
    private void getDataFromIntent(Intent intent) {
        AstrosageKundliApplication.isBackFromCall = false;
        if (intent != null) {
            IS_FROM_JOIN_CALL = intent.getBooleanExtra(CGlobalVariables.IS_FROM_RETURN_TO_CALL, false);
            MIC_STATUS = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_MIC_STATUS, true);
            VIDEO_STATUS = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, true);
            agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
            agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
            agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
            astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
            astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
            agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
            astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
            intentNotificationId = getIntent().getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
        }
    }
    private void initViews() {
        remoteUserOffline = findViewById(R.id.remote_user_offline);
        clInternetCallParent = findViewById(R.id.clInternetCallParent);
        civInternetCallDP = findViewById(R.id.civInternetCallDP);
        clInternetCallTVContainer = findViewById(R.id.clInternetCallTVContainer);
        tvInternetCallAstroName = findViewById(R.id.tvInternetCallAstroName);
        tvInternetCallTimer = findViewById(R.id.tvInternetCallTimer);
        ivInternetCallSpeaker = findViewById(R.id.ivInternetCallSpeaker);
        ivInternetCallMic = findViewById(R.id.ivInternetCallMic);
        ivInternetCallEnd = findViewById(R.id.ivInternetCallEnd);
        guideView  = findViewById(R.id.guideView);
        clMuteInfo = findViewById(R.id.cl_mute_info);
        FontUtils.changeFont(context,tvInternetCallAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context,tvInternetCallTimer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        ivInternetCallSpeaker.setOnClickListener(this);
        ivInternetCallMic.setOnClickListener(this);
        ivInternetCallEnd.setOnClickListener(this);

        checkPermission();
    }
    private void checkPermission() {
        boolean granted = true;
        for (String per : REQUESTED_PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            setUpActivityData();
        } else {
            requestPermissions();
        }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
    }
    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                setUpActivityData();

            } else {
                CUtils.showSnackbar(clInternetCallParent, getResources().getString(R.string.need_necessary_permissions),context);
            }
        }
    }



    private void setUpActivityData() {
        initData();
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = true;


        setupVoiceSDKEngine();
        initEndChatListener();
        agoraEngine.setEnableSpeakerphone(false);

        ivInternetCallSpeaker.setActivated(false);

        ivInternetCallMic.setActivated(MIC_STATUS);
        if (MIC_STATUS) {
            ivInternetCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gray_circle));
            agoraEngine.muteLocalAudioStream(false);
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_ON_AUDIO);
        } else {
            ivInternetCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.red_circle));
            agoraEngine.muteLocalAudioStream(true);
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_AUDIO);
        }
        if(!IS_FROM_JOIN_CALL){
            remoteUserOffline.setVisibility(View.VISIBLE);
            agoraCallAccepted();
        }else {
            isCallConnected = true;
            joinChannel();
        }
        try {
            if (CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
                stopService(new Intent(this, AgoraCallInitiateService.class));
            }
            if (CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
                stopService(new Intent(this, AgoraCallOngoingService.class));
            }
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(intentNotificationId);
        } catch (Exception e) {

        }
        getAudioStatus();

        sendCustomPushNotification(astrologerName, getResources().getString(R.string.title_ongoing_voice_call));
        runnable = new Runnable() {
            @Override
            public void run() {
                if(!isRemoteUserJoined){
                   // Log.d("testRunnable","videoCallCompleted ==>>>>");
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.END_VOICE_CALL_REMOTE_USER_NOT_JOIN, AstrosageKundliApplication.currentEventType, "");
                    voiceCallCompleted( CGlobalVariables.ASTROLOGER_NOT_JOINED, false);
                }
            }
        };
        handler.postDelayed(runnable,60000);

        agoraEngine.disableVideo();
        agoraEngine.enableLocalVideo(false);
        agoraEngine.muteLocalVideoStream(true);
        agoraEngine.muteAllRemoteVideoStreams(true);
    }

    private void initData() {
        connectAgoraCallBean = CUtils.connectAgoraCallBean;
        if (connectAgoraCallBean != null) {
            channelName = connectAgoraCallBean.getCallsid();
            token = connectAgoraCallBean.getAgoratokenid();
        }else {
            channelName = agoraCallSId;
            token = agoraTokenId;
        }
        tvInternetCallAstroName.setText(astrologerName);
        if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
            String newAstrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
            civInternetCallDP.setImageUrl(newAstrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());

        }
        //Log.d("VoiceCallOurActvity", "channelName==>" + channelName);
        //Log.d("VoiceCallOurActvity", "token==>" + token);
    }
    private void setupVoiceSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = getResources().getString(R.string.livestreaming_app_id);
            config.mEventHandler = mRtcEventHandler;
            //agoraEngine = RtcEngine.create(this,"388e87b2ab364a1fa6a7837df4a9c988",mRtcEventHandler);
            agoraEngine = RtcEngine.create(this,getResources().getString(R.string.livestreaming_app_id),mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }
    }
    private void joinChannel() {
        ChannelMediaOptions options = new ChannelMediaOptions();
        options.autoSubscribeAudio = true;
        int uid = Integer.parseInt(CUtils.getUserIdForBlock(this));
        String newToken = "";
        try {
            newToken = URLDecoder.decode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        agoraEngine.joinChannel(newToken, agoraCallSId, "", uid);
        agoraEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);


    }

    private void initEndChatListener() {
        endCallValueEventListener =     new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    try {
                        END_CALL_DATA = (boolean) dataSnapshot.getValue();
                        errorLogs = errorLogs + "initEndCallListener()"+"END_CALL_DATA==>>"+END_CALL_DATA+"\n";
                        if (END_CALL_DATA){
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
                            CGlobalVariables.callTimerTime = 0;
                            processEndCall();
                            // CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");
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

    private void processEndCall() {
        try {
            cancelOnDisconnectListner();
            CUtils.updateChatCallOfferType(VoiceCallActivity.this, true, CGlobalVariables.CALL_CLICK);
            isCallConnected = false;
            stopTimerAndListener();
            finish();
        }catch (Exception e){
            //
        }

    }

    private boolean checkSelfPermission()
    {
        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) !=  PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        return true;
    }
    void showMessage(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
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
            }
        }
        startTimer(longTotalVerificationTime);
        remoteUserOffline.setVisibility(View.GONE);
        registerConnectivityStatusReceiver();
        setOnDisconnectListner();
    }
    private void startTimer(long longTotalVerificationTime){
        try {
            if (countDownTimer != null) {
                countDownTimer = null;
            }
            countDownTimer = new CountDownTimer(longTotalVerificationTime, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {

                    String text = String.format(java.util.Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    tvInternetCallTimer.setText(getResources().getString(R.string.time_remaining)+" : "+text);
                    timeRemaining = text;
                    CGlobalVariables.callTimerTime = millisUntilFinished;

                    //Log.d("testVideoCallActivity", "timer is calling" + remTime);
                }

                @Override
                public void onFinish() {
                    isCallConnected = false;
                    CGlobalVariables.callTimerTime = 0;
                   // CUtils.cancelOnDisconnentEvent(agoraCallSId);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.END_VOICE_CALL_TIME_OVER, AstrosageKundliApplication.currentEventType, "");
                    voiceCallCompleted(CGlobalVariables.TIME_OVER, false);
                }
            }.start();
        }catch (Exception e){
            Log.d("kdfhksdf","Error is +"+ e);
        }

    }
    private String getCurrentDuration(long currentDuration) {
        return "Duration: ".concat(DateUtils.formatElapsedTime(currentDuration));
    }

    public void agoraCallAccepted() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.USER_VOICE_CALL_ACCEPT_API_CALL, AstrosageKundliApplication.currentEventType, "");
        if (!CUtils.isConnectedWithInternet(context)) {
            Toast.makeText(context, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
//            if (pd == null)
//                pd = new CustomProgressDialog(context);
//            pd.show();
//            pd.setCancelable(false);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CHAT_ACCEPTED_URL,
                    this, false, getAgoraCallAcceptedParams(agoraCallSId), AGORA_CALL_ACCEPTED).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);
        }
    }
    private Map<String, String> getAgoraCallAcceptedParams(String channelID) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.CHAT_DURATION, changeMinToSec());
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        Log.d("testVideoCallActivity", "getAgoraCallAcceptedParams   =    " + headers);

        return headers;
    }
    public String changeMinToSec() {
        String seconds = "00";
        if (agoraCallDuration != null && agoraCallDuration.length() > 0) {
            String[] time = agoraCallDuration.split(" ");
            if (time != null && time.length > 0) {
                String[] minSec = time[0].split(":");
                if (minSec != null && minSec.length > 1) {
                    int convertSec = Integer.parseInt(minSec[0]) * 60;
                    int totalSec = convertSec + Integer.parseInt(minSec[1]);
                    seconds = "" + totalSec;
                }
            }
        }
        return seconds;
    }

    public void voiceCallCompleted(String remark, boolean isShowProgress) {
        isCallConnected = false;
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, remark);
        Log.d("testVideoCallActivity","Remark ==>>"+ remark +"   And Url is ==>>"+CGlobalVariables.END_CALL_URL);
        //CUtils.cancelNotification(context);
        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(clInternetCallParent, getResources().getString(R.string.no_internet), context);
        } else {
            if (isShowProgress) {
                showProgressBar();
            }
            CGlobalVariables.callTimerTime = 0;
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CALL_URL, this, false, getCallCompleteParams(remark), END_AGORACALL_VALUE).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
            isRequestForEndCall = true;
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
        Log.d("testVideoCallActivity","get Call completed params ==>>"+headers);

        return headers;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivInternetCallSpeaker:
                onSpeakerClicked(ivInternetCallSpeaker);
                break;
            case R.id.ivInternetCallMic:
                toggleMicButton(ivInternetCallMic);
                break;
            case R.id.ivInternetCallEnd:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.end_chat_confirm_dialog, null);
                builder.setView(dialogView);

                TextView end_call_confirm_text = dialogView.findViewById(R.id.end_chat_confirm_text);
                FontUtils.changeFont(VoiceCallActivity.this, end_call_confirm_text, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                end_call_confirm_text.setText(getResources().getString(R.string.end_call_confirm));
                TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
                TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
                final AlertDialog alert = builder.create();

                end_chat_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.END_VOICE_BTN_YES_CLICK, AstrosageKundliApplication.currentEventType, "");
                        alert.dismiss();
                        CALL_STATUS = "completed";
                        //CUtils.cancelOnDisconnentEvent(agoraCallSId);
                        voiceCallCompleted(CGlobalVariables.USER_ENDED, true);
                    }
                });

                end_chat_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                alert.show();
                break;
        }
    }
    public void onSpeakerClicked(View view) {
        if (view.isActivated()) {
            agoraEngine.setEnableSpeakerphone(false);
            view.setActivated(false);
        } else {
            agoraEngine.setEnableSpeakerphone(true);
            view.setActivated(true);
        }
    }


    private void toggleMicButton(View view){
        if (view.isActivated()) {
            agoraEngine.muteLocalAudioStream(true);
            view.setActivated(false);
            ivInternetCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.red_circle));
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_AUDIO);
            MIC_STATUS = false;
        } else {
            agoraEngine.muteLocalAudioStream(false);
            view.setActivated(true);
            ivInternetCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gray_circle));
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_ON_AUDIO);
            MIC_STATUS = true;
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        if (distance < 5.0){
            findViewById(R.id.viewInternetCallBlackScreen).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.viewInternetCallBlackScreen).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onResponse(String response, int method) {
       // Log.d("testVideoCall","response ==== >> "+response + "method ==>> "+method);
        if (method == 0) {

        } else if (method == AGORA_CALL_ACCEPTED) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.USER_VOICE_CALL_ACCEPT_API_RESPONSE, AstrosageKundliApplication.currentEventType, "");
            //hideProgressBar();
            joinChannel();
            isCallConnected = true;
        }else if (method == END_AGORACALL_VALUE) {
            hideProgressBar();
           // Log.d("testVideoCallActivity","Response is :: == "+response);
            String status = "";
            try{
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
            } catch (Exception e){
                status = "";
            }

            if(!status.equals("1")){ // end-chat-api fail
                setEndChatOverValue(agoraCallSId);
            }
            processEndCall();

        }
    }
    private void setEndChatOverValue(String channelID) {
        if (!TextUtils.isEmpty(channelID)) {
            getmFirebaseDatabase(channelID).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY).setValue(true);
        }
    }
    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        if(isRequestForEndCall){
            setEndChatOverValue(agoraCallSId);
        }
        //Log.d("TestLog", "error="+error);
    }
    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    protected void onDestroy() {
        stopTimerAndListener();
        super.onDestroy();

    }
    private void stopTimerAndListener() {
        unRegisterConnectivityStatusReceiver();
        if(agoraEngine!=null){
            agoraEngine.stopPreview();
            agoraEngine.leaveChannel();
        }
        // Destroy the engine in a sub-thread to avoid congestion
        new Thread(() -> {
            RtcEngine.destroy();
            agoraEngine = null;
        }).start();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (readRef != null && endCallValueEventListener != null) {
            readRef.removeEventListener(endCallValueEventListener);
        }
        if (audioDBRef != null && audioEventListener != null) {
            audioDBRef.removeEventListener(audioEventListener);
        }
        if(handler!=null && runnable!=null){
            handler.removeCallbacks(runnable);
        }
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(CGlobalVariables.ONGOING_CALL_NOTIFICATION, CGlobalVariables.ONGOING_CALL_NOTIFICATION_ID);

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
    public  DatabaseReference getmFirebaseDatabase(String channelIDD) {
        //channelIDD = "FCH9911764722P137A1620053834631";
        if (channelIDD == null || channelIDD.isEmpty()) {
            channelIDD = "";
        }
        if (mFirebaseInstance == null) {
            mFirebaseInstance = FirebaseDatabase.getInstance();
        }
        return mFirebaseInstance.getReference(CGlobalVariables.CHANNELS).child(channelIDD);
    }

    @Override
    public void onBackPressed() {
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        CGlobalVariables.callTimerTime = 0;
        if (isCallConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPIPMode();
            }else {
                if (!CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
                    startService();
                } else {
                    stopService(new Intent(context, AgoraCallOngoingService.class));
                    startService();
                }
            }
        } else {
            if (CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
                stopService(new Intent(context, AgoraCallOngoingService.class));
            }
            stopTimerAndListener();
            finish();
        }

    }
    private void startService() {
        try {
            Intent serviceIntent = new Intent(context, AgoraCallOngoingService.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, MIC_STATUS);
            bundle.putBoolean(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, VIDEO_STATUS);
            bundle.putString(CGlobalVariables.AGORA_CALL_TYPE,CGlobalVariables.TYPE_VOICE_CALL);
            bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
            bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
            bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
            bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, agoraCallDuration);
            bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            AstrosageKundliApplication.isBackFromCall = true;
            finish();
        } catch (Exception e) {
            //
        }
    }

    protected void updateAudioStatusInFirebaseDB(String status) {
        try {
            DatabaseReference channelIdDbRef = getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.ISAUDIOMUTED);
            channelIdDbRef.setValue(status);
        } catch (Exception e) {
        }
    }
    private void getAudioStatus() {
        try {
            if (audioDBRef != null && audioEventListener != null) {
                audioDBRef.removeEventListener(audioEventListener);
            }
            audioDBRef = getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.ISASTROAUDIOMUTED);
            audioEventListener = (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        audioStatus = Objects.requireNonNull(snapshot.getValue()).toString();
                        setVideoAudioMuteView();
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            audioDBRef.addValueEventListener(audioEventListener);
        } catch (Exception e) {
        }
    }
    /**
     *
     */
    private void setVideoAudioMuteView() {
        try {
            // States=> VideoOff AudioOff AudioOn VideoOn
            if (audioStatus.equals(CGlobalVariables.STATUS_ON_AUDIO)) {
                clMuteInfo.setVisibility(View.GONE);
                MIC_STATUS = true;
            } else if (audioStatus.equals(CGlobalVariables.STATUS_OFF_AUDIO) ) {
                clMuteInfo.setVisibility(View.VISIBLE);
                MIC_STATUS = false;
            }

        } catch (Exception e) {
        }

    }
    private void sendCustomPushNotification(String title, String msg) {


//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);
//        int notificationId = (int) System.currentTimeMillis();
//        Intent resultIntent = new Intent(this, VoiceCallActivity.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        resultIntent.putExtra("ongoing_notification", true);
//        resultIntent.setAction(Intent.ACTION_VIEW);
//
//
//        PendingIntent pending;
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);
//        } else {
//            pending = PendingIntent.getActivity(this, notificationId, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setContentIntent(pending).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
//
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "agora_call_notification", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            mNotificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        // using the same tag and Id causes the new notification to replace an existing one
//        mNotificationManager.notify(CGlobalVariables.ONGOING_CALL_NOTIFICATION, CGlobalVariables.ONGOING_CALL_NOTIFICATION_ID, notificationBuilder.build());
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CUtils.cancelNotification(context, AstrosageKundliApplication.currentDisplayedNotificationTag, AstrosageKundliApplication.currentDisplayedNotificationId);
                }
            }, 5000);
        } catch (Exception e) {
            //
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enterPIPMode(){
        try{
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            Rational ratio = new Rational(width, height);
            PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();

            pipBuilder.setAspectRatio(ratio).build();
            findViewById(R.id.viewInternetCallBlackScreen).setVisibility(View.GONE);
            enterPictureInPictureMode(pipBuilder.build());
        }catch (Exception e){

        }
    }
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (getLifecycle().getCurrentState() == Lifecycle.State.CREATED) {
            //Log.d("TestPIP", "close PIP");
            //when user click on Close button of PIP this will trigger
            CUtils.fcmAnalyticsEvents(CGlobalVariables.EXIT_VOICE_CALL_FROM_PIP, AstrosageKundliApplication.currentEventType, "");
            voiceCallCompleted(CGlobalVariables.USER_ENDED, false);
            return;
        }
        //this.isInPictureInPictureMode = isInPictureInPictureMode;
        if(isInPictureInPictureMode){
            ivInternetCallEnd.setVisibility(View.GONE);
            ivInternetCallSpeaker.setVisibility(View.GONE);
            ivInternetCallMic.setVisibility(View.GONE);
            civInternetCallDP.setVisibility(View.GONE);
            guideView.setVisibility(View.GONE);
            tvInternetCallAstroName.setTextSize(16);
            tvInternetCallTimer.setTextSize(12);
            tvInternetCallTimer.setGravity(Gravity.CENTER);
            tvInternetCallAstroName.setGravity(Gravity.CENTER);
        }else {
            ivInternetCallEnd.setVisibility(View.VISIBLE);
            ivInternetCallSpeaker.setVisibility(View.VISIBLE);
            ivInternetCallMic.setVisibility(View.VISIBLE);
            civInternetCallDP.setVisibility(View.VISIBLE);
            guideView.setVisibility(View.VISIBLE);
            tvInternetCallAstroName.setTextSize(22);
            tvInternetCallTimer.setTextSize(18);
            tvInternetCallTimer.setGravity(Gravity.START);
            tvInternetCallAstroName.setGravity(Gravity.START);


        }
    }
    @Override
    public boolean onPictureInPictureRequested() {
        return super.onPictureInPictureRequested();

    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPIPMode();
        }else {
            if (!CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
                startService();
            } else {
                stopService(new Intent(context, AgoraCallOngoingService.class));
                startService();
            }
        }
    }
    private void setOnDisconnectListner() {
        try {
            String channelId = agoraCallSId;
            //Log.d("OnDisconnectListner", "setOnDisconnectListner channelId="+channelId);
            getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().setValue(ServerValue.TIMESTAMP);
        } catch (Exception e) {
            //
        }
    }

    private void cancelOnDisconnectListner() {
        try {
            String channelId = agoraCallSId;
            getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().cancel();
        } catch (Exception e) {
            //
        }
    }

    private void setConnectivityRegainInFirebase() {
        try {
            String channelId = agoraCallSId;
            Log.d("OnDisconnectListner", "setConnectivityRegainInFirebase channelId="+channelId);
            getmFirebaseDatabase(channelId).child("UserConnRegainTime").setValue(ServerValue.TIMESTAMP);
        } catch (Exception e) {
            //
        }
    }

    private void registerConnectivityStatusReceiver() {
        unRegisterConnectivityStatusReceiver();
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        valueEventListenerNetConnection = connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // Log.d("CHAT", "connected");
                    if (isConnectivityDisconnected) {
                        setOnDisconnectListner();
                        setConnectivityRegainInFirebase();
                        stopConnectivityTimer();
                        isConnectivityDisconnected = false;
                    }
                } else {
                    // Log.d("CHAT", "not connected");
                    if (!isConnectivityDisconnected) {
                        startConnectivityTimer();
                        isConnectivityDisconnected = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.w("TAG", "Listener was cancelled");
            }
        });
    }

    private void unRegisterConnectivityStatusReceiver() {
        if (valueEventListenerNetConnection != null) {
            FirebaseDatabase.getInstance().getReference(".info/connected").removeEventListener(valueEventListenerNetConnection);
        }
    }

    private void startConnectivityTimer() {
        try {
            if (connectivityTimer != null && connectivityRunnable != null) {
                return;
            }
            connectivityTimer = new Handler();
            connectivityRunnable = new Runnable() {
                public void run() {
                    isConnectivityDisconnected = false;
                    connectivityTimer = null;
                    connectivityRunnable = null;
                    CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.CHAT_INTERNET_DISCONNECTION;
                }
            };
            connectivityTimer.postDelayed(connectivityRunnable, 60000);
        } catch (Exception e) {
            //
        }
    }

    private void stopConnectivityTimer() {
        try {
            if (connectivityTimer != null && connectivityRunnable != null) {
                connectivityTimer.removeCallbacks(connectivityRunnable);
                connectivityTimer = null;
                connectivityRunnable = null;
            }
        } catch (Exception e) {
            //
        }
    }

    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(context);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

}
