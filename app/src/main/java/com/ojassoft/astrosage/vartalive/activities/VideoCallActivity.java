package com.ojassoft.astrosage.vartalive.activities;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHANNELS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISASTROVIDEOMUTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISVIDEOMUTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AgoraCallOngoingService;
import com.ojassoft.astrosage.varta.ui.activity.FollowingAstrologerActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.rtc.EngineConfig;
import com.ojassoft.astrosage.vartalive.stats.StatsManager;
import com.ojassoft.astrosage.vartalive.utils.PrefManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.ClientRoleOptions;
import io.agora.rtc2.video.LowLightEnhanceOptions;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCallActivity extends AppCompatActivity implements VolleyResponse, View.OnClickListener, View.OnTouchListener {
   // private final String appId = "388e87b2ab364a1fa6a7837df4a9c988";
    private int intentNotificationId;
    private ConnectAgoraCallBean connectAgoraCallBean;
    private final int CHANNEL_TOKEN_API = 1;
    private FrameLayout flVideoCallCanvasRemote;
    private FrameLayout flVideoCallCanvasSelf;
    private String channelName;
    private String token;
    private AstrologerDetailBean astrologerDetailBean;
    ImageButton ivVideoCallSwitch, ivVideoCallMic, ivVideoCallVideo, ivVideoCallEnd;
    TextView tvVideoCallDuration, tvVideoCallAstroName, muteInfo,tvVideoCallDurationText,remoteUserOffline;
    LinearLayout llVideoCallRinging,llVideoCallDuration;
    private ConstraintLayout clVideoCallParent;
    private long longTotalVerificationTime = 60000;
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private static final int AGORA_CALL_ACCEPTED = 102;
    private Context context;
    CustomProgressDialog pd;
    RequestQueue queue;
    private RtcEngine agoraEngine;
    private boolean END_CALL_DATA, isCallConnected, IS_FROM_JOIN_CALL;
    View localSurfaceView;
    SurfaceView remoteSurfaceView;

    CountDownTimer countDownTimer;
    ViewGroup _root;
    float dX, dY, newX, newY;
    String  agoraCallSId, astrologerId, agoraToken, agoraTokenId, astrologerName, astrologerProfileUrl, agoraCallDuration;
    CircularNetworkImageView profileIcon;
    private boolean MIC_STATUS, VIDEO_STATUS,isRequestForEndCall;
    private static final int END_AGORACALL_VALUE = 106;
    ValueEventListener endCallValueEventListener, audioEventListener, videoEventListener;
    DatabaseReference readRef, audioDBRef, videoDBRef;
    private String audioStatus = CGlobalVariables.STATUS_ON_AUDIO;
    private String videoStatus = CGlobalVariables.STATUS_ON_VIDEO;
    private ConstraintLayout clMuteInfo;
    private ImageView muteImageIcon;
    private FirebaseDatabase mFirebaseInstance;
    private String CALL_STATUS = CGlobalVariables.VIDEO_CALL_RUNNING;
    private static boolean isRemoteUserJoined;
    Handler handler;
    Runnable runnable;
    private Handler connectivityTimer;
    private Runnable connectivityRunnable;
    private ValueEventListener valueEventListenerNetConnection;
    private boolean isConnectivityDisconnected;
    private final EngineConfig mGlobalConfig = new EngineConfig();
    private final StatsManager mStatsManager = new StatsManager();
    private BottomSheetDialog NetworkBottomSheetDialog;
    private boolean showPoorNetworkView = true;
    private View statusBarSpacer;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            errorLogs = errorLogs + "mRtcEventHandler() isRemoteUserJoined video_onUserJoined called uid ==>>"+uid+"\n";
            onUserJoinedActions(uid);
        }
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
           // showMessage(getResources().getString(R.string.you_joined_video_call));
        }
        @Override
        public void onUserOffline(int uid, int reason) {
            //showMessage(getResources().getString(R.string.astro_offline_from_call));
            runOnUiThread(() -> flVideoCallCanvasRemote.setVisibility(View.GONE));
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

    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(getApplicationContext());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                com.ojassoft.astrosage.vartalive.utils.Constants.PREF_RESOLUTION_IDX, com.ojassoft.astrosage.vartalive.utils.Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(com.ojassoft.astrosage.vartalive.utils.Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(com.ojassoft.astrosage.vartalive.utils.Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(com.ojassoft.astrosage.vartalive.utils.Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(com.ojassoft.astrosage.vartalive.utils.Constants.PREF_MIRROR_ENCODE, 0));
    }

    private void onUserJoinedActions(int uid) {
       // showMessage(getResources().getString(R.string.astro_joined));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRemoteUserJoined = true;
                errorLogs = errorLogs + "setupRemoteVideo()\n";
                timeSetOnTimer(agoraCallDuration);
                remoteUserOffline.setVisibility(View.GONE);
                registerConnectivityStatusReceiver();
                setOnDisconnectListner();
                setupRemoteVideo(uid);

            }
        });
    }

    private void timeSetOnTimer(String talktime) {
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
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        setLanguageCode();
        context = this;
        handler = new Handler();
        queue = VolleySingleton.getInstance(VideoCallActivity.this).getRequestQueue();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getDataFromIntent(getIntent());
        initViews();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            //Log.d("ChatWindowTest", "onNewIntent");
            boolean onNotificationClick = intent.getBooleanExtra("ongoing_notification", false);
            if(!onNotificationClick){
                getDataFromIntent(intent);
                initViews();
            }

        }

    }
    private void getDataFromIntent( Intent intent) {
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
             profileIcon = findViewById(R.id.profile_icon);
            intentNotificationId = getIntent().getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
            if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
                astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
                //profileIcon.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(VideoCallActivity.this).getImageLoader());
                Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(profileIcon);
            }
        }
    }

    private void initViews() {
        _root = findViewById(R.id.root);
        setLocalVideoViewContainer(getResources().getDimension(R.dimen.canvas_local_width),getResources().getDimension(R.dimen.canvas_local_height));
        flVideoCallCanvasRemote = findViewById(R.id.flVideoCallCanvasRemote);
        remoteUserOffline = findViewById(R.id.remote_user_offline);
        ivVideoCallSwitch = findViewById(R.id.ivVideoCallSwitch);
        ivVideoCallMic = findViewById(R.id.ivVideoCallMic);
        ivVideoCallVideo = findViewById(R.id.ivVideoCallVideo);
        tvVideoCallAstroName = findViewById(R.id.tvVideoCallAstroName);
        tvVideoCallDurationText = findViewById(R.id.tvVideoCallDurationText);
        tvVideoCallAstroName.setText(astrologerName);
        tvVideoCallDuration = findViewById(R.id.tvVideoCallDuration);
        llVideoCallDuration = findViewById(R.id.llVideoCallDuration);
        ivVideoCallEnd = findViewById(R.id.ivVideoCallEnd);
        clVideoCallParent = findViewById(R.id.clVideoCallParent);
        statusBarSpacer = findViewById(R.id.statusBarSpacer);
        clMuteInfo = findViewById(R.id.cl_mute_info);
        muteImageIcon = findViewById(R.id.mute_image_icon);
        muteInfo = findViewById(R.id.text_mute_info);
        applySystemWindowInsets();
        checkPermission();
    }
    private void applySystemWindowInsets() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        final int initialLeftPadding = clVideoCallParent.getPaddingLeft();
        final int initialTopPadding = clVideoCallParent.getPaddingTop();
        final int initialRightPadding = clVideoCallParent.getPaddingRight();
        final int initialBottomPadding = clVideoCallParent.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(clVideoCallParent, (view, windowInsets) -> {
            Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Keep the top card below the status bar while preserving bottom space for nav controls.
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
        ViewCompat.requestApplyInsets(clVideoCallParent);
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
                CUtils.showSnackbar(_root, getResources().getString(R.string.need_necessary_permissions),context);
            }
        }
    }
    private void setUpActivityData() {
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = true;

        /*
        Initiate the listeners
         */
        initListeners();
        // get the token and channel from the saved [CUtils.connectAgoraCallBean]
        initData();
        // set the RtcEngine
        setupVideoSDKEngine();
        //set the video profile
       // setupVideoProfile();

        /* Api for again user confirmation
        and after api response initiate the agora and join the particular channel which the astrologer has joined.
        method Called  joinChannel();
         */
        if (!IS_FROM_JOIN_CALL) {
            remoteUserOffline.setVisibility(View.VISIBLE);
            agoraCallAccepted();
        } else {
            isCallConnected = true;
            joinChannel();
        }

        getAudioStatus();
        getVideoStatus();
        getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.END_CHAT_FBD_KEY).setValue(false);
        initEndCallListener();
        sendCustomPushNotification(astrologerName, getResources().getString(R.string.title_ongoing_video_call));
        if (MIC_STATUS) {
            agoraEngine.muteLocalAudioStream(false);
            ivVideoCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gray_circle));
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_ON_AUDIO);
        } else {
            agoraEngine.muteLocalAudioStream(true);
            ivVideoCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.red_circle));
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_AUDIO);
        }
        if (VIDEO_STATUS) {
            agoraEngine.muteLocalVideoStream(false);
            agoraEngine.enableLocalVideo(true);
            ivVideoCallVideo.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gray_circle));
            updateVideoStatusInFirebaseDB(CGlobalVariables.STATUS_ON_VIDEO);
        } else {
            agoraEngine.muteLocalVideoStream(true);
            agoraEngine.enableLocalVideo(false);
            ivVideoCallVideo.setBackground(ContextCompat.getDrawable(context,R.drawable.red_circle));
            updateVideoStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_VIDEO);
        }

        ivVideoCallMic.setActivated(MIC_STATUS);
        ivVideoCallVideo.setActivated(VIDEO_STATUS);
        try {
            stopService(new Intent(this, AgoraCallInitiateService.class));
            if (CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
                stopService(new Intent(this, AgoraCallOngoingService.class));
            }
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(intentNotificationId);
        } catch (Exception e) {

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
        runnable = new Runnable() {
            @Override
            public void run() {
                //Log.d("testRunnable","isRemoteUserJoined ==>>>>"+isRemoteUserJoined);
                if(!isRemoteUserJoined){
                  //  Log.d("testRunnable","videoCallCompleted ==>>>>");
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.END_VIDEO_CALL_REMOTE_USER_NOT_JOIN, AstrosageKundliApplication.currentEventType, "");
                    videoCallCompleted( CGlobalVariables.ASTROLOGER_NOT_JOINED, false);
                }
            }
        };
        handler.postDelayed(runnable,60000);
    }
    private void setLocalVideoViewContainer(float width,float height) {
        flVideoCallCanvasSelf = new FrameLayout(this);
        int borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        GradientDrawable localVideoContainerBackground = new GradientDrawable();
        localVideoContainerBackground.setColor(Color.TRANSPARENT);
        localVideoContainerBackground.setCornerRadius(cornerRadius);
        flVideoCallCanvasSelf.setBackground(localVideoContainerBackground);

        GradientDrawable localVideoContainerBorder = new GradientDrawable();
        localVideoContainerBorder.setColor(Color.TRANSPARENT);
        localVideoContainerBorder.setCornerRadius(cornerRadius);
        localVideoContainerBorder.setStroke(borderWidth, ContextCompat.getColor(context, R.color.white));
        flVideoCallCanvasSelf.setForeground(localVideoContainerBorder);
        flVideoCallCanvasSelf.setClipChildren(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flVideoCallCanvasSelf.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
                }
            });
            flVideoCallCanvasSelf.setClipToOutline(true);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) width, (int) height);
        if(isInPictureInPictureMode){
            layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.canvas_local_height_end_margin_pip);
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.canvas_local_height_bottom_margin_pip);
        }else {
            layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.canvas_local_height_end_margin);
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.canvas_local_height_bottom_margin);
        }

        layoutParams.gravity = Gravity.END | Gravity.BOTTOM;
        flVideoCallCanvasSelf.setLayoutParams(layoutParams);
        _root.addView(flVideoCallCanvasSelf);
        flVideoCallCanvasSelf.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        switch (event.getAction()) {
            //this is your code
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                newX = event.getRawX() + dX;
                newY = event.getRawY() + dY;

                if ((newX <= 0 || newX >= screenWidth - view.getWidth()) || (newY <= 0 || newY >= screenHeight - view.getHeight())) {
                    return true;
                }
                view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();
                break;
            default:
                return false;
        }
        return true;
    }

    private void initListeners() {
        ivVideoCallSwitch.setOnClickListener(this);
        ivVideoCallMic.setOnClickListener(this);
        ivVideoCallVideo.setOnClickListener(this);
        ivVideoCallEnd.setOnClickListener(this);
    }

    private void initData() {
        connectAgoraCallBean = CUtils.connectAgoraCallBean;
        if (connectAgoraCallBean != null) {
            channelName = connectAgoraCallBean.getCallsid();
            token = connectAgoraCallBean.getAgoratokenid();
        } else {
            channelName = agoraCallSId;
            token = agoraTokenId;
        }
      //  Log.d("VideoCallOurActvity", "channelName==>" + channelName);
       // Log.d("VideoCallOurActvity", "token==>" + token);
        //channelName = "MANISHRANJAN098765";
        // token = "007eJxTYDjo99OmpNf+MMvrhY+jbQNWTNhlpXizTv5klviNKQI8s/MVGIwtLFItzJOMEpOMzUwSDdMSzRLNLYzNU9JMEi2TLS0szukVpzQEMjKs2jyLkZEBAkF8IQZfRz/PYI8gRz8vRz8DSwtzM1MGBgAdsyKc";

    }

    private void setupVideoSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = getResources().getString(R.string.livestreaming_app_id);
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(this, getResources().getString(R.string.livestreaming_app_id), mRtcEventHandler);
            initConfig();
           // agoraEngine = RtcEngine.create(this, "388e87b2ab364a1fa6a7837df4a9c988", mRtcEventHandler);
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine.enableVideo();

        } catch (Exception e) {
            showMessage(e.toString());
        }
    }
    protected EngineConfig config() {
        return mGlobalConfig;
    }
    public void joinChannel() {

        if (checkSelfPermission()) {
            if (TextUtils.isEmpty(channelName) || TextUtils.isEmpty(token)) {
                return;
            }
            setupLocalVideo();
            agoraEngine.startPreview();
            int uid = Integer.parseInt(CUtils.getUserIdForBlock(this));
            String newToken = "";
            try {
                newToken = URLDecoder.decode(token, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            //LowLightEnhanceOptions lowLightEnhanceOptions = new LowLightEnhanceOptions(LowLightEnhanceOptions.LOW_LIGHT_ENHANCE_LEVEL_HIGH_QUALITY,LowLightEnhanceOptions.LOW_LIGHT_ENHANCE_LEVEL_FAST);
            //agoraEngine.setLowlightEnhanceOptions(true,lowLightEnhanceOptions);
           // Log.d("VideoCallOurActvity", " newToken==>" + newToken);
            //agoraEngine.joinChannel("007eJxTYOBhWp4i4FnjfNPOyK38zeM/GxZM+ur++HbA1h1n1z9jZ5RUYDC2sEi1ME8ySkwyNjNJNExLNEs0tzA2T0kzSbRMtrSwmN42O6UhkJEhQ3g7MyMDBIL4qgyeYc6Whr6WlsYmpiZmFmZGIYZmFubmFhYm5obGRqYl+dmpeZ4pDAwA/vkn2w==", "IVC91M9934546862T1687788471325tokenId", "", 0);
            configVideo();
            agoraEngine.joinChannel(newToken, channelName, "", uid);
            agoraEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);

//            ClientRoleOptions clientRoleOptions = new ClientRoleOptions();
//            clientRoleOptions.audienceLatencyLevel = 1;
//            agoraEngine.setClientRole(2, clientRoleOptions);


        } else {
            Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
    }
    private void configVideo() {
        VideoEncoderConfiguration configuration = new VideoEncoderConfiguration(
                com.ojassoft.astrosage.vartalive.utils.Constants.VIDEO_DIMENSIONS[config().getVideoDimenIndex()],
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        );
        //configuration.mirrorMode = com.ojassoft.astrosage.vartalive.utils.Constants.VIDEO_MIRROR_MODES[config().getMirrorEncodeIndex()];
        agoraEngine.setVideoEncoderConfiguration(configuration);
    }
    private void setupVideoProfile() {
        agoraEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_1280x720, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30, 0, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void  setupLocalVideo() {
        localSurfaceView = new TextureView(getBaseContext());
        localSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
//        localSurfaceView.setClipBounds(rect);
        flVideoCallCanvasSelf.addView(localSurfaceView);
        agoraEngine.enableLocalVideo(true);
        agoraEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }


    private void setupRemoteVideo(int uid) {
        //remoteSurfaceView = RtcEngine.CreateRendererView(getBaseContext());

        remoteSurfaceView = prepareRtcVideo(uid, false);

        //remoteSurfaceView.setZOrderMediaOverlay(true);
        flVideoCallCanvasRemote.addView(remoteSurfaceView);

        //agoraEngine.setupRemoteVideo(new VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
       // remoteSurfaceView.setTag(uid);
        flVideoCallCanvasRemote.setVisibility(View.VISIBLE);

    }
    protected SurfaceView prepareRtcVideo(int uid, boolean local) {
        // Render local/remote video on a SurfaceView

        SurfaceView surface = new SurfaceView(getBaseContext());
        if (local) {
            agoraEngine.setupLocalVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            0
                            //com.ojassoft.astrosage.vartalive.utils.Constants.VIDEO_MIRROR_MODES[config().getMirrorLocalIndex()]
                    )
            );
        } else {
            agoraEngine.setupRemoteVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            uid//, com.ojassoft.astrosage.vartalive.utils.Constants.VIDEO_MIRROR_MODES[config().getMirrorRemoteIndex()]
                    )
            );
        }
        return surface;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivVideoCallSwitch:
                agoraEngine.switchCamera();
                break;
            case R.id.ivVideoCallMic:
                onMuteAudioClicked(ivVideoCallMic);
                break;
            case R.id.ivVideoCallVideo:
                onMuteVideoClicked(ivVideoCallVideo);
                break;
            case R.id.ivVideoCallEnd:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.end_chat_confirm_dialog, null);
                builder.setView(dialogView);

                TextView end_call_confirm_text = dialogView.findViewById(R.id.end_chat_confirm_text);
                FontUtils.changeFont(VideoCallActivity.this, end_call_confirm_text, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                end_call_confirm_text.setText(getResources().getString(R.string.end_call_confirm));
                TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
                TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
                final AlertDialog alert = builder.create();
                Objects.requireNonNull(alert.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                end_chat_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.END_VIDEO_BTN_YES_CLICK, AstrosageKundliApplication.currentEventType, "");
                        alert.dismiss();
                        videoCallCompleted(CGlobalVariables.USER_ENDED, true);
                    }
                });

                end_chat_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // CUtils.fcmAnalyticsEvents("end_chat_btn_no_click", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        alert.dismiss();
                    }
                });
                //CUtils.fcmAnalyticsEvents("end_chat_dialog_show", AstrosageKundliApplication.currentEventType, "");
                alert.show();
                break;
        }
    }

    public void onMuteVideoClicked(View view) {
        if (view.isActivated()) {
            agoraEngine.muteLocalVideoStream(true);
            view.setActivated(false);
            agoraEngine.enableLocalVideo(false);
            ivVideoCallVideo.setBackground(ContextCompat.getDrawable(context,R.drawable.red_circle));
            updateVideoStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_VIDEO);
            VIDEO_STATUS = false;
        } else {
            agoraEngine.muteLocalVideoStream(false);
            view.setActivated(true);
            agoraEngine.enableLocalVideo(true);
            ivVideoCallVideo.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gray_circle));
            updateVideoStatusInFirebaseDB(CGlobalVariables.STATUS_ON_VIDEO);
            VIDEO_STATUS = true;

        }
    }

    public void onMuteAudioClicked(View view) {
        if (view.isActivated()) {
            agoraEngine.muteLocalAudioStream(true);
            view.setActivated(false);
            ivVideoCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.red_circle));
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_OFF_AUDIO);
            MIC_STATUS = false;
        } else {
            agoraEngine.muteLocalAudioStream(false);
            view.setActivated(true);
            ivVideoCallMic.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_gray_circle));
            updateAudioStatusInFirebaseDB(CGlobalVariables.STATUS_ON_AUDIO);
            MIC_STATUS = true;

        }
    }

    private void startTimer(long longTotalVerificationTime) {
        if (countDownTimer != null) {
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(longTotalVerificationTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(java.util.Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvVideoCallDuration.setText(text);
                CGlobalVariables.callTimerTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                isCallConnected = false;
                CUtils.fcmAnalyticsEvents(CGlobalVariables.END_VIDEO_CALL_TIME_OVER, AstrosageKundliApplication.currentEventType, "");
                videoCallCompleted(CGlobalVariables.TIME_OVER, false);

            }
        }.start();
    }


    private void videoCallCompleted(String remark, boolean isShowProgress) {
        CALL_STATUS = CGlobalVariables.VIDEO_CALL_COMPLETED;
        CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, remark);
        isCallConnected = false;
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        CGlobalVariables.callTimerTime = 0;
       // Log.d("testVideoCallActivity", "Remark ==>>" + remark + "   And Url is ==>>" + CGlobalVariables.END_CALL_URL);
        //CUtils.cancelNotification(context);
        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(_root, getResources().getString(R.string.no_internet), context);
        } else {
            if (isShowProgress) {
                showProgressBar();
            }
            Log.d("testCallComptedNoti","videoCallCompleted api called ");
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CALL_URL, this, false, getCallCompleteParams(remark), END_AGORACALL_VALUE).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endInternetcall(getCallCompleteParams(remark));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //Log.d("testCallComptedNoti","END_AGORACALL_VALUE"+response);
                    String status = "";
                    try{
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        status = jsonObject.getString("status");

                    } catch (Exception e){
                        status = "";
                    }

                    if(!status.equals("1")){ // end-chat-api fail
                        setEndChatOverValue(agoraCallSId);
                    }
                    processEndCall();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    if(isRequestForEndCall){
                        setEndChatOverValue(agoraCallSId);
                    }
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
        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/timeChangeInSecond(tvVideoCallDuration.getText().toString()));
        headers.put(CGlobalVariables.CHANNEL_ID, agoraCallSId);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
       // Log.d("testVideoCallActivity", "get Call completed params ==>>" + headers);

        return headers;
    }

    private String getCurrentDuration(long currentDuration) {
        return getResources().getString(R.string.duration)
                .concat(getResources().getString(R.string.colon))
                .concat(DateUtils.formatElapsedTime(currentDuration));
    }


    private void rearrangeViews() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(clVideoCallParent);
        constraintSet.clear(tvVideoCallAstroName.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(tvVideoCallAstroName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, CUtils.convertDpToPx(context, 25));
        constraintSet.connect(tvVideoCallAstroName.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, CUtils.convertDpToPx(context, 25));

        constraintSet.clear(llVideoCallDuration.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(llVideoCallDuration.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, CUtils.convertDpToPx(context, 25));

        constraintSet.applyTo(clVideoCallParent);
        tvVideoCallDuration.setTextSize(14f);
    }

    private void animateViews(boolean show) {
        if (show) {
            tvVideoCallAstroName.animate().alpha(1f).setDuration(300);
            llVideoCallDuration.animate().alpha(1f).setDuration(300);
        } else {
            tvVideoCallAstroName.animate().alpha(0f).setDuration(300);
            llVideoCallDuration.animate().alpha(0f).setDuration(300);
        }
    }

    private void setLanguageCode() {
        try {
            SharedPreferences sharedPreferencesForLang = getSharedPreferences(com.ojassoft.astrosage.utils.CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
            LANGUAGE_CODE = sharedPreferencesForLang.getInt(com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_AppLanguage, com.ojassoft.astrosage.utils.CGlobalVariables.ENGLISH);
        } catch (Exception e) {
            //
        }
    }

    void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void agoraCallAccepted() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.USER_VIDEO_CALL_ACCEPT_API_CALL, AstrosageKundliApplication.currentEventType, "");
        //Log.d("testNewDialog", "channelID   =    " + channelID);
        if (!CUtils.isConnectedWithInternet(context)) {
            //CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), DashBoardActivity.this);
        } else {
//            if (pd == null) pd = new CustomProgressDialog(context);
//            pd.show();
//            pd.setCancelable(false);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CHAT_ACCEPTED_URL, this, false, getAgoraCallAcceptedParams(channelName), AGORA_CALL_ACCEPTED).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.userChatAcceptV2( getAgoraCallAcceptedParams(channelName));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.body()!=null){
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.USER_VIDEO_CALL_ACCEPT_API_RESPONSE, AstrosageKundliApplication.currentEventType, "");
                            hideProgressBar();
                            isCallConnected = true;
                            // to join the agora channel
                            joinChannel();
                        }
                    }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    if(isRequestForEndCall){
                        setEndChatOverValue(agoraCallSId);
                    }
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
        //Log.d("testNewDialog", "getChatAcceptedParams   =    " + headers.toString());

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

    @Override
    public void onResponse(String response, int method) {
        //Log.d("testVideoCallActivity", "onResponse=" + response + "method ==>>  "+ method);
        if (method == 0) {
            //Log.d("testChatNew", " response  =>>>" + response);
            //parseAstrologerStatus(response);
        } else if (method == AGORA_CALL_ACCEPTED) {
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.USER_VIDEO_CALL_ACCEPT_API_RESPONSE, AstrosageKundliApplication.currentEventType, "");
//            hideProgressBar();
//            isCallConnected = true;
//            // to join the agora channel
//            joinChannel();
        }
        if (method == END_AGORACALL_VALUE) {
            //Log.d("testCallComptedNoti","END_AGORACALL_VALUE"+response);
//            String status = "";
//            try{
//                JSONObject jsonObject = new JSONObject(response);
//                status = jsonObject.getString("status");
//
//            } catch (Exception e){
//                status = "";
//            }
//
//            if(!status.equals("1")){ // end-chat-api fail
//                setEndChatOverValue(agoraCallSId);
//            }
//            processEndCall();
        }
    }

    private void processEndCall() {
        try {
            cancelOnDisconnectListner();
            CUtils.updateChatCallOfferType(VideoCallActivity.this, true, CGlobalVariables.CALL_CLICK);
            isCallConnected = false;
            onBackPressed();
        }catch (Exception e){
            //
        }

    }

    @Override
    public void onError(VolleyError error) {
        //Log.d("TestLog", "error="+error);
        hideProgressBar();
        if(isRequestForEndCall){
            setEndChatOverValue(agoraCallSId);
        }
    }
    private void setEndChatOverValue(String channelID) {
        if (!TextUtils.isEmpty(channelID)) {
            getmFirebaseDatabase(channelID).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY).setValue(true);
        }
    }
    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        if (agoraEngine != null) {
            agoraEngine.stopPreview();
            agoraEngine.leaveChannel();

            // Destroy the engine in a sub-thread to avoid congestion
            new Thread(() -> {
                RtcEngine.destroy();
                agoraEngine = null;
            }).start();
        }

        removeTimerAndListener();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isCallConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPIPMode();
            }else{
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
            removeTimerAndListener();
            finish();
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
            enterPictureInPictureMode(pipBuilder.build());
        }catch (Exception e){

        }
    }
    private void startService() {
        try {
            Intent serviceIntent = new Intent(context, AgoraCallOngoingService.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, MIC_STATUS);
            //Log.d("asdhjsafs",VIDEO_STATUS+" VIDEO_STATUS is ");
            bundle.putBoolean(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, VIDEO_STATUS);
            bundle.putString(CGlobalVariables.AGORA_CALL_TYPE, CGlobalVariables.TYPE_VIDEO_CALL);
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

    private void sendCustomPushNotification(String title, String msg) {
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);
//        int notificationId = (int) System.currentTimeMillis();
//        Intent resultIntent = new Intent(this, VideoCallActivity.class);
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


    private void removeTimerAndListener() {
        CGlobalVariables.callTimerTime = 0;
        AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(CGlobalVariables.ONGOING_CALL_NOTIFICATION, CGlobalVariables.ONGOING_CALL_NOTIFICATION_ID);
        if (agoraEngine != null) {
            agoraEngine.stopPreview();
            agoraEngine.leaveChannel();
        }
        if (remoteSurfaceView != null) remoteSurfaceView.setVisibility(View.GONE);
        // Stop local video rendering.
        if (localSurfaceView != null) localSurfaceView.setVisibility(View.GONE);
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
        if (videoDBRef != null && videoEventListener != null) {
            videoDBRef.removeEventListener(videoEventListener);
        }
        if(handler!=null && runnable!=null){
            handler.removeCallbacks(runnable);
        }
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

    /*-------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------Firebase method--------------------------------------------*/

    /**
     *
     */
    private void initEndCallListener() {
        endCallValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    try {
                        END_CALL_DATA = (boolean) dataSnapshot.getValue();
                        errorLogs = errorLogs + "initEndCallListener()"+"END_CALL_DATA==>>"+END_CALL_DATA+"\n";
                        if (END_CALL_DATA) {
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            isCallConnected = false;
                            processEndCall();
                                Toast.makeText(context, getResources().getString(R.string.call_ended_by_astrologer), Toast.LENGTH_SHORT).show();
                            // CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");

                        }

                    } catch (Exception e) {
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

    protected void updateAudioStatusInFirebaseDB(String status) {
        try {
            DatabaseReference databaseReference = getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.ISAUDIOMUTED);
            databaseReference.setValue(status);
        } catch (Exception e) {

        }
    }

    protected void updateVideoStatusInFirebaseDB(String status) {
        try {
            DatabaseReference databaseReference = getmFirebaseDatabase(agoraCallSId).child(CGlobalVariables.ISVIDEOMUTED);
            databaseReference.setValue(status);
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
    private void getVideoStatus() {
        try {
            if (videoDBRef != null && videoEventListener != null) {
                videoDBRef.removeEventListener(videoEventListener);
            }

            videoDBRef = getmFirebaseDatabase(agoraCallSId).child(ISASTROVIDEOMUTED);
            videoEventListener = (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        videoStatus = Objects.requireNonNull(snapshot.getValue()).toString();
                        setVideoAudioMuteView();
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            videoDBRef.addValueEventListener(videoEventListener);
        } catch (Exception e) {
        }

    }

    /**
     *
     */
    private void setVideoAudioMuteView() {
        try {
            if (audioStatus.equals(CGlobalVariables.STATUS_ON_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_ON_VIDEO))) {
                clMuteInfo.setVisibility(View.GONE);
                MIC_STATUS = true;
                VIDEO_STATUS = true;
            } else if (audioStatus.equals(CGlobalVariables.STATUS_OFF_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_ON_VIDEO))) {
                clMuteInfo.setVisibility(View.VISIBLE);
                muteImageIcon.setImageResource(R.drawable.ic_live_mute);
                muteInfo.setText(getResources().getString(R.string.muteInfoMic));
                MIC_STATUS = false;
                VIDEO_STATUS = true;
            } else if (audioStatus.equals(CGlobalVariables.STATUS_ON_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_OFF_VIDEO))) {
                clMuteInfo.setVisibility(View.VISIBLE);
                muteImageIcon.setImageResource(R.drawable.ic_live_mute_video);
                muteInfo.setText(getResources().getString(R.string.muteInfoVideo));
                MIC_STATUS = true;
                VIDEO_STATUS = false;
            } else if (audioStatus.equals(CGlobalVariables.STATUS_OFF_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_OFF_VIDEO))) {
                clMuteInfo.setVisibility(View.VISIBLE);
                muteImageIcon.setImageResource(R.drawable.ic_live_mute);
                muteInfo.setText(getResources().getString(R.string.muteInfoMicVideo));
                MIC_STATUS = false;
                VIDEO_STATUS = false;
            }

        } catch (Exception e) {
        }

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

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (getLifecycle().getCurrentState() == Lifecycle.State.CREATED) {
            //Log.d("TestPIP", "close PIP");
            //when user click on Close button of PIP this will trigger
            CUtils.fcmAnalyticsEvents(CGlobalVariables.EXIT_VIDEO_CALL_FROM_PIP, AstrosageKundliApplication.currentEventType, "");
            videoCallCompleted(CGlobalVariables.USER_ENDED, false);
            return;
        }
        this.isInPictureInPictureMode = isInPictureInPictureMode;
        if(isInPictureInPictureMode){
            tvVideoCallDuration.setTextSize(10);
            tvVideoCallAstroName.setTextSize(12);
            tvVideoCallDurationText.setVisibility(View.GONE);
            ivVideoCallSwitch.setVisibility(View.GONE);
            ivVideoCallMic.setVisibility(View.GONE);
            ivVideoCallVideo.setVisibility(View.GONE);
            ivVideoCallEnd.setVisibility(View.GONE);
            profileIcon.setVisibility(View.GONE);
            _root.removeView(flVideoCallCanvasSelf);
            setLocalVideoViewContainer(getResources().getDimension(R.dimen.canvas_local_width_pip),getResources().getDimension(R.dimen.canvas_local_height_pip));
            setupLocalVideo();
        }else {
            tvVideoCallDuration.setTextSize(16);
            tvVideoCallAstroName.setTextSize(18);
            tvVideoCallDurationText.setVisibility(View.VISIBLE);
            ivVideoCallSwitch.setVisibility(View.VISIBLE);
            ivVideoCallMic.setVisibility(View.VISIBLE);
            ivVideoCallVideo.setVisibility(View.VISIBLE);
            ivVideoCallEnd.setVisibility(View.VISIBLE);
            profileIcon.setVisibility(View.VISIBLE);
            _root.removeView(flVideoCallCanvasSelf);
            setLocalVideoViewContainer(getResources().getDimension(R.dimen.canvas_local_width),getResources().getDimension(R.dimen.canvas_local_height));
            setupLocalVideo();

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
