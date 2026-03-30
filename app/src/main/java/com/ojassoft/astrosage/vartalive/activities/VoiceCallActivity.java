package com.ojassoft.astrosage.vartalive.activities;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISVIDEOMUTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
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
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
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

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.service.VoiceCallOngoingNotificationService;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.ClientRoleOptions;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoiceCallActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener, VolleyResponse {
    private static final String EXTRA_SHOW_POST_CALL_UI = "extra_show_post_call_ui";
    private static final long FEEDBACK_STATUS_REQUEST_DELAY_MS = 2000L;
    Context context;
    CircularNetworkImageView civInternetCallDP;
    ConstraintLayout clInternetCallTVContainer, clInternetCallParent;
    TextView tvInternetCallAstroName,tvInternetCallTimer,remoteUserOffline;
    private TextView tvPipInternetCallAstroName;
    private TextView tvPipInternetCallTimer;
    ImageView ivInternetCallSpeaker,ivInternetCallMic,ivInternetCallEnd;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int PERMISSION_REQ_ID = 22;
    CustomProgressDialog pd;
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
    private static final int AGORA_CALL_ACCEPTED = 104;
    private static final String[] REQUESTED_PERMISSIONS = {
                    Manifest.permission.RECORD_AUDIO };
    ValueEventListener endCallValueEventListener,audioEventListener;
    private ValueEventListener endChatStateValueEventListener;
    DatabaseReference readRef,audioDBRef;
    private DatabaseReference endChatStateRef;
    private String audioStatus = CGlobalVariables.STATUS_ON_AUDIO;
    private ConstraintLayout clMuteInfo;
    private View topPaddingView;
    private View bottomPaddingView;
    private View controlLayoutView;
    private FirebaseDatabase mFirebaseInstance;
    Handler handler;
    Runnable runnable;
    private static boolean isRemoteUserJoined;
    View guideView;
    private ImageView ivCallAgain;
    private ImageView ivChatAgain;
    private ImageView ivCancelPostCall;
    private TextView tvCallAgain;
    private TextView tvChatAgain;
    private TextView tvCancelPostCall;
    private boolean isCallDisconnected;
    private boolean isCallAgainInProgress;
    private boolean isChatAgainInProgress;
    private boolean isActivityDataInitialized;
    private boolean isCurrentCallResourcesReleased;
    private boolean hasConnectedCallSession;
    private boolean isFeedbackRequestedForCallEnd;
    private boolean isFeedbackCheckPending;
    private boolean isFeedbackCheckScheduled;
    private boolean isSpeakerEnabled = true;
    private AudioManager audioManager;
    private AudioDeviceCallback audioDeviceCallback;
    private FeedbackDialog feedbackDialog;
    private String lastEndRemark = "";
    private final Handler callAgainHandler = new Handler();
    private Runnable callAgainFinishRunnable;
    private Runnable chatAgainFinishRunnable;
    private final Handler feedbackHandler = new Handler();
    private Runnable feedbackCheckRunnable;

    private Handler connectivityTimer;
    private Runnable connectivityRunnable;
    private ValueEventListener valueEventListenerNetConnection;
    private boolean isConnectivityDisconnected;
    private BottomSheetDialog NetworkBottomSheetDialog;
    private boolean showPoorNetworkView = true;
    private View statusBarSpacer;
    private View profileContainer;
    private View pipInfoContainer;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    errorLogs = errorLogs + "mRtcEventHandler() isRemoteUserJoined voice_onUserJoined called uid ==>>"+uid+"\n";

                    isRemoteUserJoined = true;
                    hasConnectedCallSession = true;
                    timeSetOnTimer(agoraCallDuration);
                    startOngoingCallNotificationService(false);
                }
            });
        }
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    applySpeakerAsDefaultAudioRoute();
                }
            });
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
        registerBackPressHandler();
        handler = new Handler();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setupAudioDeviceCallback();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        getDataFromIntent(getIntent());
        initViews();
    }

    /**
     * Routes system back presses through the in-call minimize flow for both button and gesture navigation.
     */
    private void registerBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackNavigation();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (handleNotificationActionIntent(intent)) {
            return;
        }
        if (consumePostCallUiIntent(intent)) {
            return;
        }
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
        isCallAgainInProgress = false;
        isFeedbackRequestedForCallEnd = false;
        isFeedbackCheckPending = false;
    }

    /**
     * Consumes the internal intent that reopens this activity to show the finished-call UI after PiP ends.
     */
    private boolean consumePostCallUiIntent(Intent intent) {
        if (intent == null || !intent.getBooleanExtra(EXTRA_SHOW_POST_CALL_UI, false)) {
            return false;
        }
        intent.removeExtra(EXTRA_SHOW_POST_CALL_UI);
        updatePostCallState(lastEndRemark);
        runPendingFeedbackCheckIfReady();
        return true;
    }
    private void initViews() {
        remoteUserOffline = findViewById(R.id.remote_user_offline);
        clInternetCallParent = findViewById(R.id.clInternetCallParent);
        statusBarSpacer = findViewById(R.id.statusBarSpacer);
        civInternetCallDP = findViewById(R.id.civInternetCallDP);
        clInternetCallTVContainer = findViewById(R.id.clInternetCallTVContainer);
        tvInternetCallAstroName = findViewById(R.id.tvInternetCallAstroName);
        tvInternetCallTimer = findViewById(R.id.tvInternetCallTimer);
        tvPipInternetCallAstroName = findViewById(R.id.tvPipInternetCallAstroName);
        tvPipInternetCallTimer = findViewById(R.id.tvPipInternetCallTimer);
        ivInternetCallSpeaker = findViewById(R.id.ivInternetCallSpeaker);
        ivInternetCallMic = findViewById(R.id.ivInternetCallMic);
        ivInternetCallEnd = findViewById(R.id.ivInternetCallEnd);
        guideView  = findViewById(R.id.guideView);
        profileContainer = findViewById(R.id.profileContainer);
        pipInfoContainer = findViewById(R.id.pipInfoContainer);
        ivCallAgain = findViewById(R.id.call_again);
        ivChatAgain = findViewById(R.id.iv_chat_icon);
        ivCancelPostCall = findViewById(R.id.iv_cancel_call);
        tvCallAgain = findViewById(R.id.tv_call_again);
        tvChatAgain = findViewById(R.id.tv_chat);
        tvCancelPostCall = findViewById(R.id.tv_cancel);
        clMuteInfo = findViewById(R.id.cl_mute_info);
        topPaddingView = findViewById(R.id.top_padding);
        bottomPaddingView = findViewById(R.id.paddingView);
        controlLayoutView = findViewById(R.id.consLayoutBottom);
        applySystemWindowInsets();
        FontUtils.changeFont(context,tvInternetCallAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context,tvInternetCallTimer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context,tvPipInternetCallAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context,tvPipInternetCallTimer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvCallAgain, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, tvChatAgain, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, tvCancelPostCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        ivInternetCallSpeaker.setOnClickListener(this);
        ivInternetCallMic.setOnClickListener(this);
        ivInternetCallEnd.setOnClickListener(this);
        ivCallAgain.setOnClickListener(this);
        ivChatAgain.setOnClickListener(this);
        ivCancelPostCall.setOnClickListener(this);

        checkPermission();
    }
    private void applySystemWindowInsets() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        final int initialLeftPadding = clInternetCallParent.getPaddingLeft();
        final int initialTopPadding = clInternetCallParent.getPaddingTop();
        final int initialRightPadding = clInternetCallParent.getPaddingRight();
        final int initialBottomPadding = clInternetCallParent.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(clInternetCallParent, (view, windowInsets) -> {
            Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Keep the top content below the status bar while preserving bottom space for nav controls.
            ConstraintLayout.LayoutParams spacerLayoutParams = (ConstraintLayout.LayoutParams) statusBarSpacer.getLayoutParams();
            spacerLayoutParams.height = systemBarsInsets.top;
            statusBarSpacer.setLayoutParams(spacerLayoutParams);
            view.setPadding(
                    initialLeftPadding + systemBarsInsets.left,
                    initialTopPadding,
                    initialRightPadding + systemBarsInsets.right,
                    initialBottomPadding + systemBarsInsets.bottom
            );
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(clInternetCallParent);
    }

    /**
     * Updates the bottom margin of the call info card so the PiP header matches the AI voice-call layout.
     */
    private void updateGuideViewBottomMargin(int bottomMarginDp) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideView.getLayoutParams();
        layoutParams.bottomMargin = (int) (bottomMarginDp * getResources().getDisplayMetrics().density);
        guideView.setLayoutParams(layoutParams);
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
        if (isActivityDataInitialized) {
            return;
        }
        initData();
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = true;
        isCallDisconnected = false;
        isCurrentCallResourcesReleased = false;
        hasConnectedCallSession = false;
        showPostCallActions(false);

        setupVoiceSDKEngine();
        if (agoraEngine == null) {
            Toast.makeText(this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        isActivityDataInitialized = true;
        initEndChatListener();
        applySpeakerAsDefaultAudioRoute();

        applyMicButtonState(MIC_STATUS);
        if (MIC_STATUS) {
            agoraEngine.muteLocalAudioStream(false);
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_ON_AUDIO);
        } else {
            agoraEngine.muteLocalAudioStream(true);
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_AUDIO);
        }
        if(!IS_FROM_JOIN_CALL){
            remoteUserOffline.setVisibility(View.VISIBLE);
            agoraCallAccepted();
        }else {
            isCallConnected = true;
            hasConnectedCallSession = true;
            joinChannel();
        }
        try {
            if (CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
                stopService(new Intent(this, AgoraCallInitiateService.class));
            }
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(intentNotificationId);
        } catch (Exception e) {

        }
        getAudioStatus();

        cancelAcceptedNotification();
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
        handleNotificationActionIntent(getIntent());
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
        tvPipInternetCallAstroName.setText(astrologerName);
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
            agoraEngine = null;
        }
    }

    /**
     * Keeps voice calls on speaker by default until the user explicitly switches it off.
     */
    private void applySpeakerAsDefaultAudioRoute() {
        isSpeakerEnabled = !hasPreferredExternalAudioDevice();
        syncSpeakerAudioRoute();
    }

    /**
     * Reapplies the selected route while preserving connected earbuds and Bluetooth devices across lifecycle changes.
     */
    private void syncSpeakerAudioRoute() {
        if (agoraEngine != null) {
            agoraEngine.setEnableSpeakerphone(isSpeakerEnabled);
        }
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            if (isSpeakerEnabled) {
                routeAudioToSpeaker();
            } else {
                routeAudioAwayFromSpeaker();
            }
        }
        applySpeakerButtonState(isSpeakerEnabled);
    }

    /**
     * Registers a device callback so headset changes can immediately restore the intended call route.
     */
    private void setupAudioDeviceCallback() {
        if (audioManager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        audioDeviceCallback = new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                handleAudioDeviceRouteChange();
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                handleAudioDeviceRouteChange();
            }
        };
        audioManager.registerAudioDeviceCallback(audioDeviceCallback, null);
    }

    /**
     * Unregisters the audio device callback to avoid leaking the activity after the call screen closes.
     */
    private void teardownAudioDeviceCallback() {
        if (audioManager == null || audioDeviceCallback == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        audioManager.unregisterAudioDeviceCallback(audioDeviceCallback);
        audioDeviceCallback = null;
    }

    /**
     * Re-syncs the in-call route after Android reports a headset or Bluetooth device change.
     */
    private void handleAudioDeviceRouteChange() {
        if (isFinishing() || isDestroyed() || !isActivityDataInitialized || isCurrentCallResourcesReleased) {
            return;
        }
        runOnUiThread(this::syncSpeakerAudioRoute);
    }

    /**
     * Forces the current call back to speaker and clears any previously pinned communication device.
     */
    private void routeAudioToSpeaker() {
        clearPinnedCommunicationDevice();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
        }
        audioManager.setSpeakerphoneOn(true);
    }

    /**
     * Keeps audio on the best non-speaker output so PiP transitions do not briefly blast through the phone speaker.
     */
    private void routeAudioAwayFromSpeaker() {
        audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AudioDeviceInfo preferredDevice = findPreferredCommunicationDevice(audioManager.getAvailableCommunicationDevices());
            if (preferredDevice != null) {
                audioManager.setCommunicationDevice(preferredDevice);
                return;
            }
            audioManager.clearCommunicationDevice();
            return;
        }
        if (hasBluetoothCommunicationDevice()) {
            audioManager.startBluetoothSco();
            audioManager.setBluetoothScoOn(true);
        } else {
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
        }
    }

    /**
     * Clears any communication device pinned for the active call when speaker becomes the preferred route again.
     */
    private void clearPinnedCommunicationDevice() {
        if (audioManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            audioManager.clearCommunicationDevice();
        }
    }

    /**
     * Returns whether a wired or Bluetooth call device is currently available and should outrank the phone speaker.
     */
    private boolean hasPreferredExternalAudioDevice() {
        if (audioManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return findPreferredCommunicationDevice(audioManager.getAvailableCommunicationDevices()) != null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        return findPreferredCommunicationDevice(Arrays.asList(audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS))) != null;
    }

    /**
     * Chooses the best communication device for voice calls, prioritizing Bluetooth headsets over wired outputs.
     */
    private AudioDeviceInfo findPreferredCommunicationDevice(List<AudioDeviceInfo> devices) {
        AudioDeviceInfo bluetoothDevice = findCommunicationDeviceByType(devices,
                AudioDeviceInfo.TYPE_BLE_HEADSET,
                AudioDeviceInfo.TYPE_BLUETOOTH_SCO);
        if (bluetoothDevice != null) {
            return bluetoothDevice;
        }
        return findCommunicationDeviceByType(devices,
                AudioDeviceInfo.TYPE_WIRED_HEADSET,
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
                AudioDeviceInfo.TYPE_USB_HEADSET,
                AudioDeviceInfo.TYPE_USB_DEVICE,
                AudioDeviceInfo.TYPE_USB_ACCESSORY);
    }

    /**
     * Finds the first matching device type from the provided list so route selection stays deterministic.
     */
    private AudioDeviceInfo findCommunicationDeviceByType(List<AudioDeviceInfo> devices, int... targetTypes) {
        for (int targetType : targetTypes) {
            for (AudioDeviceInfo device : devices) {
                if (device != null && device.getType() == targetType) {
                    return device;
                }
            }
        }
        return null;
    }

    /**
     * Detects whether a Bluetooth communication route is available on pre-Android 12 devices that still rely on SCO.
     */
    private boolean hasBluetoothCommunicationDevice() {
        if (audioManager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        for (AudioDeviceInfo device : devices) {
            if (device != null && (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
                    || device.getType() == AudioDeviceInfo.TYPE_BLE_HEADSET)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the mic button visual state to match whether the local mic is currently enabled.
     */
    private void applyMicButtonState(boolean isMicEnabled) {
        ivInternetCallMic.setActivated(isMicEnabled);
        ivInternetCallMic.setColorFilter(ContextCompat.getColor(this, R.color.no_change_white));
    }

    /**
     * Updates the speaker button visual state to match the active audio route styling used in AI voice calls.
     */
    private void applySpeakerButtonState(boolean isSpeakerEnabled) {
        ivInternetCallSpeaker.setActivated(isSpeakerEnabled);
        ivInternetCallSpeaker.setColorFilter(ContextCompat.getColor(this,
                isSpeakerEnabled ? R.color.voice_call_speaker_icon_active : R.color.no_change_white));
    }

    private void joinChannel() {
        try {
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
        }catch (Exception e){
            //
        }



    }

    private void initEndChatListener() {
        endCallValueEventListener =     new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    try {
                        END_CALL_DATA = getFirebaseBooleanValue(dataSnapshot);
                        errorLogs = errorLogs + "initEndCallListener()"+"END_CALL_DATA==>>"+END_CALL_DATA+"\n";
                        if (END_CALL_DATA){
                            handleRemoteEndCall(CGlobalVariables.ASTROLOGER);
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

        endChatStateValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (getFirebaseBooleanValue(dataSnapshot)) {
                        handleRemoteEndCall(CGlobalVariables.ASTROLOGER);
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        };
        endChatStateRef = getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.END_CHAT_FBD_KEY);
        endChatStateRef.addValueEventListener(endChatStateValueEventListener);

    }

    /**
     * Normalizes Firebase values so remote-end flags work for both boolean and string payloads.
     */
    private boolean getFirebaseBooleanValue(@NonNull DataSnapshot dataSnapshot) {
        Object value = dataSnapshot.getValue();
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean(((String) value).trim());
        }
        return false;
    }

    /**
     * Applies the astrologer-ended cleanup once, even if multiple Firebase end flags arrive together.
     */
    private void handleRemoteEndCall(String remark) {
        if (isCallDisconnected || isCurrentCallResourcesReleased) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        CGlobalVariables.callTimerTime = 0;
        queueFeedbackCheckAfterConfirmedEnd();
        processEndCall(remark);
    }

    /**
     * Tears down the finished call and updates the activity into its post-call state.
     */
    private void processEndCall(String remark) {
        try {
            cancelOnDisconnectListner();
            CUtils.updateChatCallOfferType(VoiceCallActivity.this, true, CGlobalVariables.CALL_CLICK);
            isCallConnected = false;
            isCallDisconnected = true;
            lastEndRemark = remark;
            stopTimerAndListener();
            updatePostCallState(remark);
            runPendingFeedbackCheckIfReady();
            reopenPostCallScreenFromPipIfNeeded();
        } catch (Exception e) {
            //
        }
    }

    /**
     * Updates the screen after the current call has ended so retry actions stay visible.
     */
    private void updatePostCallState(String remark) {
        remoteUserOffline.setVisibility(View.GONE);
        clMuteInfo.setVisibility(View.GONE);
        showPostCallActions(true);
        updatePostCallStatusText(remark);
    }

    /**
     * Shows or hides the post-call actions that mirror the AI voice-call exit state.
     */
    private void showPostCallActions(boolean shouldShow) {
        int postCallVisibility = shouldShow && !isInPictureInPictureMode ? View.VISIBLE : View.GONE;
        int inCallVisibility = shouldShow ? View.GONE : View.VISIBLE;
        int postCallTextVisibility = postCallVisibility;
        boolean shouldShowChatAction = shouldShow && !isInPictureInPictureMode && isChatAvailableForRetry();

        ivInternetCallEnd.setVisibility(inCallVisibility);
        ivInternetCallSpeaker.setVisibility(inCallVisibility);
        ivInternetCallMic.setVisibility(inCallVisibility);

        ivCallAgain.setVisibility(postCallVisibility);
        ivChatAgain.setVisibility(shouldShowChatAction ? View.VISIBLE : View.GONE);
        ivCancelPostCall.setVisibility(postCallVisibility);
        tvCallAgain.setVisibility(postCallTextVisibility);
        tvChatAgain.setVisibility(shouldShowChatAction ? View.VISIBLE : View.GONE);
        tvCancelPostCall.setVisibility(postCallVisibility);
    }

    /**
     * Reopens the activity in fullscreen after a remote call end so the finished-call state is not trapped in PiP.
     */
    private void reopenPostCallScreenFromPipIfNeeded() {
        if (!isInPictureInPictureMode || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        try {
            Intent intent = new Intent(this, VoiceCallActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("ongoing_notification", true);
            intent.putExtra(EXTRA_SHOW_POST_CALL_UI, true);
            startActivity(intent);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Maps the latest end reason to the status text shown above the post-call actions.
     */
    private void updatePostCallStatusText(String remark) {
        String statusText;
        if (TextUtils.isEmpty(remark)) {
            statusText = getResources().getString(R.string.call_completed);
            tvInternetCallTimer.setText(statusText);
            tvPipInternetCallTimer.setText(statusText);
            return;
        }

        if (remark.contains(CGlobalVariables.ASTROLOGER) || remark.contains(CGlobalVariables.ASTROLOGER_NOT_JOINED)) {
            statusText = getResources().getString(R.string.call_ended_by_astrologer);
        } else if (remark.contains(CGlobalVariables.TIME_OVER)) {
            statusText = getResources().getString(R.string.call_completed);
        } else if (remark.contains(CGlobalVariables.USER_ENDED)) {
            statusText = getResources().getString(R.string.call_completed);
        } else {
            statusText = getResources().getString(R.string.call_failed_user);
        }
        tvInternetCallTimer.setText(statusText);
        tvPipInternetCallTimer.setText(statusText);
    }

    /**
     * Handles notification action intents after the voice-call screen is restored from the ongoing notification.
     */
    private boolean handleNotificationActionIntent(Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return false;
        }
        String action = intent.getAction();
        if (CGlobalVariables.HANG_UP_ACTION.equals(action)) {
            intent.setAction(null);
            voiceCallCompleted(isCallConnected ? CGlobalVariables.USER_ENDED : CGlobalVariables.ASTROLOGER_NOT_JOINED, false);
            return true;
        }
        if (CGlobalVariables.SPEAKER_ACTION.equals(action)) {
            intent.setAction(null);
            onSpeakerClicked(ivInternetCallSpeaker);
            return true;
        }
        if (CGlobalVariables.MIC_ACTION.equals(action)) {
            intent.setAction(null);
            toggleMicButton(ivInternetCallMic);
            return true;
        }
        return false;
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
                    String timerText = getResources().getString(R.string.time_remaining)+" : "+text;
                    tvInternetCallTimer.setText(timerText);
                    tvPipInternetCallTimer.setText(timerText);
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
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CHAT_ACCEPTED_URL,
//                    this, false, getAgoraCallAcceptedParams(agoraCallSId), AGORA_CALL_ACCEPTED).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);


            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.userChatAcceptV2( getAgoraCallAcceptedParams(channelName));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body()!=null){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.USER_VOICE_CALL_ACCEPT_API_RESPONSE, AstrosageKundliApplication.currentEventType, "");
                        //hideProgressBar();
                        joinChannel();
                        isCallConnected = true;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
    private Map<String, String> getAgoraCallAcceptedParams(String channelID) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.CHAT_DURATION, changeMinToSec());
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.d("testVideoCallActivity", "getAgoraCallAcceptedParams   =    " + headers);

        return CUtils.setRequiredParams(headers);
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
        lastEndRemark = remark;
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, remark);
        Log.d("testVideoCallActivity","Remark ==>>"+ remark +"   And Url is ==>>"+CGlobalVariables.END_CALL_URL);
        //CUtils.cancelNotification(context);
        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(clInternetCallParent, getResources().getString(R.string.no_internet), context);
            processEndCall(remark);
        } else {
            if (isShowProgress) {
                showProgressBar();
            }
            CGlobalVariables.callTimerTime = 0;
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endInternetcall(getCallCompleteParams(remark));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //Log.d("testCallComptedNoti","END_AGORACALL_VALUE"+response);
                    hideProgressBar();
                    // Log.d("testVideoCallActivity","Response is :: == "+response);
                    String status = "";
                    try{
                        String myRespose = response.body().string();
                        JSONObject jsonObject = new JSONObject(myRespose);
                        status = jsonObject.getString("status");
                    } catch (Exception e){
                        status = "";
                    }

                    if(!status.equals("1")){ // end-chat-api fail
                        setEndChatOverValue(agoraCallSId);
                    } else {
                        queueFeedbackCheckAfterConfirmedEnd();
                    }
                    processEndCall(remark);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    if(isRequestForEndCall){
                        setEndChatOverValue(agoraCallSId);
                    }
                    processEndCall(remark);
                }
            });
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
            case R.id.call_again:
                startCallAgainFlow();
                break;
            case R.id.iv_chat_icon:
                openChatAfterCall();
                break;
            case R.id.iv_cancel_call:
                closePostCallScreen();
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
                Objects.requireNonNull(alert.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        isSpeakerEnabled = !view.isActivated();
        syncSpeakerAudioRoute();
    }

    /**
     * Re-initiates the same human voice call and closes this finished screen after the new wait service starts.
     */
    private void startCallAgainFlow() {
        if (isCallAgainInProgress) {
            return;
        }
        AstrologerDetailBean astrologerDetailBean = getActiveAstrologerForRetry();
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(this);
        if (astrologerDetailBean == null || userProfileData == null) {
            Toast.makeText(this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            return;
        }

        isCallAgainInProgress = true;
        ivCallAgain.setEnabled(false);
        AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;
        AstrosageKundliApplication.chatAgainAstrologerDetailBean = astrologerDetailBean;
        ChatUtils chatUtils = ChatUtils.getInstance(this);
        chatUtils.consultationType = CGlobalVariables.TYPE_VOICE_CALL;
        AstrosageKundliApplication.currentConsultType = "audio_call";
        chatUtils.startAudioCall(userProfileData);
        waitForCallAgainServiceStart();
    }

    /**
     * Opens chat with the same astrologer from the post-call screen.
     */
    private void openChatAfterCall() {
        if (isChatAgainInProgress) {
            return;
        }
        if (!isChatAvailableForRetry()) {
            return;
        }
        AstrologerDetailBean astrologerDetailBean = getActiveAstrologerForRetry();
        if (astrologerDetailBean == null) {
            Toast.makeText(this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            return;
        }
        isChatAgainInProgress = true;
        ivChatAgain.setEnabled(false);
        ChatUtils.getInstance(this).initChat(astrologerDetailBean);
        waitForChatAgainServiceStart();
    }

    /**
     * Closes the finished call screen and falls back to a stable screen when this activity is task root.
     */
    private void closePostCallScreen() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, ActAppModule.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        finish();
    }

    /**
     * Returns the astrologer that should be reused by Call Again and Chat actions.
     */
    private AstrologerDetailBean getActiveAstrologerForRetry() {
        if (AstrosageKundliApplication.chatAgainAstrologerDetailBean != null) {
            return AstrosageKundliApplication.chatAgainAstrologerDetailBean;
        }
        return AstrosageKundliApplication.selectedAstrologerDetailBean;
    }

    /**
     * Returns whether the current astrologer can still accept a human chat retry from the post-call screen.
     */
    private boolean isChatAvailableForRetry() {
        AstrologerDetailBean astrologerDetailBean = getActiveAstrologerForRetry();
        return astrologerDetailBean != null && astrologerDetailBean.isAvailableForChatBool();
    }

    /**
     * Marks feedback as pending only after one of the two confirmed end-call sources fires:
     * a successful `/end-internet-call` response or the astrologer-ended Firebase callback.
     */
    private void queueFeedbackCheckAfterConfirmedEnd() {
        if (isFeedbackRequestedForCallEnd || isFeedbackCheckPending || !hasConnectedCallSession || TextUtils.isEmpty(astrologerId)) {
            return;
        }
        isFeedbackCheckPending = true;
        Log.d("VoiceCallActivity23423423", "Feedback check queued after confirmed call end.");
    }

    /**
     * Executes the queued feedback-status request exactly once after the end-call UI is stable and not stuck in PiP.
     */
    private void runPendingFeedbackCheckIfReady() {
        if (!isFeedbackCheckPending || isInPictureInPictureMode || isFeedbackRequestedForCallEnd || isFeedbackCheckScheduled) {
            return;
        }
        scheduleFeedbackStatusRequest();
    }

    /**
     * Waits briefly before requesting feedback eligibility so the backend can finish persisting the end-call state.
     */
    private void scheduleFeedbackStatusRequest() {
        isFeedbackCheckScheduled = true;
        Log.d("VoiceCallActivity23423423", "Scheduling feedback status request after " + FEEDBACK_STATUS_REQUEST_DELAY_MS + " ms.");
        feedbackCheckRunnable = new Runnable() {
            @Override
            public void run() {
                isFeedbackCheckScheduled = false;
                feedbackCheckRunnable = null;
                if (!isFeedbackCheckPending || isFeedbackRequestedForCallEnd || isInPictureInPictureMode) {
                    Log.d("VoiceCallActivity23423423", "Skipping feedback status request. pending=" + isFeedbackCheckPending + ", requested=" + isFeedbackRequestedForCallEnd + ", pip=" + isInPictureInPictureMode);
                    return;
                }
                isFeedbackCheckPending = false;
                isFeedbackRequestedForCallEnd = true;
                Log.d("VoiceCallActivity23423423", "Requesting feedback status now.");
                getAstrologerFeedbackStatus();
            }
        };
        feedbackHandler.postDelayed(feedbackCheckRunnable, FEEDBACK_STATUS_REQUEST_DELAY_MS);
    }

    /**
     * Fetches feedback eligibility only after a confirmed end-call event has queued the request.
     */
    private void getAstrologerFeedbackStatus() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Map<String, String> headers = getAstroFeedbackStatusParams(astrologerId);
        Log.d("VoiceCallActivity23423423", "Headers: " + headers);
        Call<ResponseBody> call = api.getAstrologerFeedbackStatus(headers);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null) {
                        return;
                    }
                    String responseBody = response.body().string();
                    Log.d("VoiceCallActivity23423423", "Response: " + responseBody);
                    parseAstrologerFeedbackStatus(responseBody);
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //
            }
        });
    }

    /**
     * Builds the request payload used by the backend to decide whether post-call feedback is enabled.
     */
    private Map<String, String> getAstroFeedbackStatusParams(String astroId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        headers.put("languagecode", String.valueOf(LANGUAGE_CODE));
        headers.put("name", CUtils.getUserFullName(context));
        return headers;
    }

    /**
     * Opens the feedback dialog when the backend indicates feedback is enabled for this ended call.
     */
    private void parseAstrologerFeedbackStatus(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String feedbackEnabledValue = jsonObject.optString("enablefeedbacks");
            boolean isFeedbackEnabled = jsonObject.optBoolean("enablefeedbacks")
                    || "1".equals(feedbackEnabledValue)
                    || "true".equalsIgnoreCase(feedbackEnabledValue);
            if (isFeedbackEnabled) {
                showRatingDialogToUser();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Shows the same astrologer feedback dialog used by the AI voice-call flow.
     */
    private void showRatingDialogToUser() {
        try {
            AstrologerDetailBean astrologerDetailBean = getActiveAstrologerForRetry();
            if (astrologerDetailBean == null) {
                astrologerDetailBean = new AstrologerDetailBean();
                astrologerDetailBean.setAstrologerId(astrologerId);
            }
            if (feedbackDialog != null && feedbackDialog.isVisible()) {
                return;
            }
            feedbackDialog = new FeedbackDialog(CGlobalVariables.CON_TYPE_CALL, agoraCallSId, getSupportFragmentManager(), astrologerDetailBean);
            feedbackDialog.show(getSupportFragmentManager(), "FeedbackDialog");
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN, CGlobalVariables.SHOW_DIALOG_EVENT, "");
        } catch (Exception e) {
            //
        }
    }

    /**
     * Waits until the new initiate service is running before removing the finished call screen.
     */
    private void waitForCallAgainServiceStart() {
        if (callAgainFinishRunnable != null) {
            callAgainHandler.removeCallbacks(callAgainFinishRunnable);
        }
        final long startTime = System.currentTimeMillis();
        callAgainFinishRunnable = new Runnable() {
            @Override
            public void run() {
                if (CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
                    finishAfterCallAgainStarted();
                    return;
                }
                if ((System.currentTimeMillis() - startTime) >= 8000L) {
                    isCallAgainInProgress = false;
                    ivCallAgain.setEnabled(true);
                    return;
                }
                callAgainHandler.postDelayed(this, 250L);
            }
        };
        callAgainHandler.postDelayed(callAgainFinishRunnable, 250L);
    }

    /**
     * Waits until the chat-initiation service owns the next human-chat flow before closing this screen.
     */
    private void waitForChatAgainServiceStart() {
        if (chatAgainFinishRunnable != null) {
            callAgainHandler.removeCallbacks(chatAgainFinishRunnable);
        }
        final long startTime = System.currentTimeMillis();
        chatAgainFinishRunnable = new Runnable() {
            @Override
            public void run() {
                if (CUtils.checkServiceRunning(AstroAcceptRejectService.class)) {
                    finishAfterChatAgainStarted();
                    return;
                }
                if ((System.currentTimeMillis() - startTime) >= 8000L) {
                    isChatAgainInProgress = false;
                    ivChatAgain.setEnabled(true);
                    return;
                }
                callAgainHandler.postDelayed(this, 250L);
            }
        };
        callAgainHandler.postDelayed(chatAgainFinishRunnable, 250L);
    }

    /**
     * Closes the current post-call screen after the new call-initiation flow has been handed over to the service layer.
     */
    private void finishAfterCallAgainStarted() {
        isCallAgainInProgress = false;
        if (callAgainFinishRunnable != null) {
            callAgainHandler.removeCallbacks(callAgainFinishRunnable);
            callAgainFinishRunnable = null;
        }
        finish();
    }

    /**
     * Closes the current post-call screen after the chat-initiation flow has been handed over to the service layer.
     */
    private void finishAfterChatAgainStarted() {
        isChatAgainInProgress = false;
        if (chatAgainFinishRunnable != null) {
            callAgainHandler.removeCallbacks(chatAgainFinishRunnable);
            chatAgainFinishRunnable = null;
        }
        finish();
    }


    private void toggleMicButton(View view){
        if (view.isActivated()) {
            agoraEngine.muteLocalAudioStream(true);
            applyMicButtonState(false);
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_AUDIO);
            MIC_STATUS = false;
        } else {
            agoraEngine.muteLocalAudioStream(false);
            applyMicButtonState(true);
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
        syncSpeakerAudioRoute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    protected void onDestroy() {
        if (feedbackCheckRunnable != null) {
            feedbackHandler.removeCallbacks(feedbackCheckRunnable);
            feedbackCheckRunnable = null;
        }
        if (callAgainFinishRunnable != null) {
            callAgainHandler.removeCallbacks(callAgainFinishRunnable);
            callAgainFinishRunnable = null;
        }
        if (chatAgainFinishRunnable != null) {
            callAgainHandler.removeCallbacks(chatAgainFinishRunnable);
            chatAgainFinishRunnable = null;
        }
        isActivityDataInitialized = false;
        teardownAudioDeviceCallback();
        stopTimerAndListener();
        super.onDestroy();

    }
    private void stopTimerAndListener() {
        if (isCurrentCallResourcesReleased) {
            return;
        }
        isCurrentCallResourcesReleased = true;
        unRegisterConnectivityStatusReceiver();
        stopVoiceCallNotificationService();
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
        if (endChatStateRef != null && endChatStateValueEventListener != null) {
            endChatStateRef.removeEventListener(endChatStateValueEventListener);
        }
        if (audioDBRef != null && audioEventListener != null) {
            audioDBRef.removeEventListener(audioEventListener);
        }
        if(handler!=null && runnable!=null){
            handler.removeCallbacks(runnable);
        }
        if (audioManager != null) {
            clearPinnedCommunicationDevice();
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(false);
            audioManager.setMode(AudioManager.MODE_NORMAL);
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
        handleBackNavigation();
    }

    /**
     * Decides whether back should close post-call UI, minimize an active call, or exit the screen.
     */
    private void handleBackNavigation() {
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        CGlobalVariables.callTimerTime = 0;
        if (isCallDisconnected) {
            closePostCallScreen();
        } else if (isActivityDataInitialized) {
            moveActiveCallToBackground();
        } else {
            stopVoiceCallNotificationService();
            stopTimerAndListener();
            finish();
        }
    }

    /**
     * Moves an ongoing voice call into PiP or the foreground notification fallback, depending on OS support.
     */
    private void moveActiveCallToBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPIPMode();
        } else {
            if (!CUtils.checkServiceRunning(VoiceCallOngoingNotificationService.class)) {
                startOngoingCallNotificationService(true);
            } else {
                stopVoiceCallNotificationService();
                startOngoingCallNotificationService(true);
            }
        }
    }
    /**
     * Starts the ongoing-call notification service and optionally closes the full-screen activity.
     */
    private void startOngoingCallNotificationService(boolean shouldFinishActivity) {
        try {
            Intent serviceIntent = new Intent(context, VoiceCallOngoingNotificationService.class);
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
            bundle.putString(CGlobalVariables.CALL_TIME_REMAINING, getNotificationRemainingDuration());
            bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
            bundle.putString(CGlobalVariables.AGORA_CALL_ACTIVITY_CLASS, VoiceCallActivity.class.getName());
            bundle.putBoolean(CGlobalVariables.CALL_NOTIFICATION_ONLY_MODE, false);
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, serviceIntent);
            } else {
                startService(serviceIntent);
            }
            if (shouldFinishActivity) {
                AstrosageKundliApplication.isBackFromCall = true;
                finish();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Converts the current in-call timer text into the `MM:SS` value consumed by the notification service.
     */
    private String getNotificationRemainingDuration() {
        if (!TextUtils.isEmpty(timeRemaining)) {
            String[] parts = timeRemaining.split(":");
            try {
                if (parts.length == 3) {
                    int hours = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    int seconds = Integer.parseInt(parts[2]);
                    int totalMinutes = (hours * 60) + minutes;
                    return String.format(java.util.Locale.US, "%02d:%02d", totalMinutes, seconds);
                }
                if (parts.length == 2) {
                    return String.format(java.util.Locale.US, "%02d:%02d", Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
            } catch (Exception e) {
                //
            }
        }
        return agoraCallDuration;
    }

    /**
     * Stops the ongoing-call notification service when the call flow is torn down.
     */
    private void stopVoiceCallNotificationService() {
        if (CUtils.checkServiceRunning(VoiceCallOngoingNotificationService.class)) {
            stopService(new Intent(context, VoiceCallOngoingNotificationService.class));
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

    private void cancelAcceptedNotification() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CUtils.cancelNotification(VoiceCallActivity.this, AstrosageKundliApplication.currentDisplayedNotificationTag, AstrosageKundliApplication.currentDisplayedNotificationId);
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
            if (isCallConnected) {
                voiceCallCompleted(CGlobalVariables.USER_ENDED, false);
            }
            return;
        }
        this.isInPictureInPictureMode = isInPictureInPictureMode;
        if(isInPictureInPictureMode){
            ivInternetCallEnd.setVisibility(View.GONE);
            ivInternetCallSpeaker.setVisibility(View.GONE);
            ivInternetCallMic.setVisibility(View.GONE);
            ivCallAgain.setVisibility(View.GONE);
            ivChatAgain.setVisibility(View.GONE);
            ivCancelPostCall.setVisibility(View.GONE);
            tvCallAgain.setVisibility(View.GONE);
            tvChatAgain.setVisibility(View.GONE);
            tvCancelPostCall.setVisibility(View.GONE);
            civInternetCallDP.setVisibility(View.GONE);
            clMuteInfo.setVisibility(View.GONE);
            profileContainer.setVisibility(View.GONE);
            pipInfoContainer.setVisibility(View.VISIBLE);
            guideView.setVisibility(View.VISIBLE);
            controlLayoutView.setVisibility(View.GONE);
            bottomPaddingView.setVisibility(View.GONE);
            topPaddingView.setVisibility(View.GONE);
            updateGuideViewBottomMargin(10);
        }else {
            if (isCallDisconnected) {
                showPostCallActions(true);
                clMuteInfo.setVisibility(View.GONE);
                runPendingFeedbackCheckIfReady();
            } else {
                ivInternetCallEnd.setVisibility(View.VISIBLE);
                ivInternetCallSpeaker.setVisibility(View.VISIBLE);
                ivInternetCallMic.setVisibility(View.VISIBLE);
                setVideoAudioMuteView();
            }
            civInternetCallDP.setVisibility(View.VISIBLE);
            profileContainer.setVisibility(View.VISIBLE);
            pipInfoContainer.setVisibility(View.GONE);
            guideView.setVisibility(View.VISIBLE);
            controlLayoutView.setVisibility(View.VISIBLE);
            bottomPaddingView.setVisibility(View.VISIBLE);
            topPaddingView.setVisibility(View.VISIBLE);
            tvInternetCallTimer.setGravity(Gravity.CENTER);
            tvInternetCallAstroName.setGravity(Gravity.CENTER);
            updateGuideViewBottomMargin(40);


        }
    }
    @Override
    public boolean onPictureInPictureRequested() {
        return super.onPictureInPictureRequested();

    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        if (isCallDisconnected || !isActivityDataInitialized) {
            return;
        }
        moveActiveCallToBackground();
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
