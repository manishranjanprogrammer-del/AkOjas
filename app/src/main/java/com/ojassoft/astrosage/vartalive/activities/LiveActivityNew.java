package com.ojassoft.astrosage.vartalive.activities;

import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_LIVE_ACTIVITY_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.varta.ui.fragments.VartaHomeFragment.liveAstrologersArrayList;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTRO_AUDIO_STATUS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.END_LIVE_PIP_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISVIDEOMUTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.OPEN_LIVE_IN_PIP;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_SPEED;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.ApiRepository;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.LiveAstrologerBottomAdapter;
import com.ojassoft.astrosage.varta.adapters.LiveGiftAdapter;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteMinimizeDialog;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.dialog.RatingAndDakshinaDialog;
import com.ojassoft.astrosage.varta.dialog.ReportAbuseDialog;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ChatMessageModel;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.service.CallStatusCheckService;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.OnSwipeTouchListener;
import com.ojassoft.astrosage.vartalive.fragments.LiveIntroFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomDialog;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.vartalive.stats.LocalStatsData;
import com.ojassoft.astrosage.vartalive.stats.RemoteStatsData;
import com.ojassoft.astrosage.vartalive.stats.StatsData;
import com.ojassoft.astrosage.vartalive.widgets.VideoGridContainer;
import com.ojassoft.astrosage.vartalive.widgets.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.ClientRoleOptions;
import io.agora.rtc2.video.BeautyOptions;
import io.agora.rtc2.video.VideoEncoderConfiguration;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveActivityNew extends RtcBaseActivity implements VolleyResponse, OnSwipeTouchListener.onSwipeListener {

    private static final String TAG = "LiveActivityNew";
    private static final int JOIN_AUDIANCE_REQ = 1;
    private static final int END_LIVE_CALL_REQ = 7;
    private static final int CONNECT_LIVE_CALL_REQ = 2;
    private static final int SEND_GIFT_REQ = 3;
    private static final int SEND_LOG_DATA_REQ = 4;
    private static final int FETCH_LIVE_ASTROLOGER = 5;
    private static final long longOneSecond = 1000;
    private int currentPos = 0;
    private ApiRepository apiRepository;
    private ArrayList<LiveAstrologerModel> liveAstrologerModelList;
    private RecyclerView recyclerViewLiveStream;
    private LinearLayout mBackLayout;
    private LinearLayout mLoginLayout;
    private LinearLayout mFollowLayout,mUnFollowLayout;
    private RelativeLayout mAstrologerDescription;
    private DatabaseReference chatDBRef;
    private DatabaseReference audioDBRef;
    private DatabaseReference userBlockDBRef;
    private DatabaseReference videoDBRef;
    private DatabaseReference astroPvtCallDBRef;
    private DatabaseReference pinMessageDBRef;
    private DatabaseReference callStatusDBRef;
    private FirebaseDatabase mFirebaseInstance;
    private VideoGridContainer mVideoGridContainer;
    private RelativeLayout call_astrologer_btn;
    private LinearLayout ll_inner_call_btn;
    private ImageView mMuteAudioBtn, mEbableSpeakerBtn;
    private ImageView mMuteVideoBtn;
    private ConstraintLayout sendMessageLL;
    private TextView messageTextEdit;
    private ArrayList<ChatMessageModel> chatMessageArrayList;
    private LiveStreamMsgInfoAdapter liveStreamMsgDetailAdapter;
    private WrapContentLinearLayoutManager linearLayoutManager;
    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;
    private ChildEventListener chatValueEventListener;
    private ValueEventListener runningCallsIdEventListener, runningCallsIdEventListenerPrivateCall;
    private ValueEventListener endLiveSesionEventListner;
    private ValueEventListener audioEventListener;
    private ValueEventListener userBlockEventListener;
    private ValueEventListener videoEventListener;
    private ValueEventListener pinMessageEventListener;
    private ValueEventListener callStatusValueEventListener;
    private String audioCallStatus = CGlobalVariables.STATUS_LIVE_CALL_ON, videoCallStatus = CGlobalVariables.STATUS_LIVE_CALL_ON, privateCallStatus = CGlobalVariables.STATUS_LIVE_CALL_ON;
    private ValueEventListener astroPvtCallEventListener;
    private String userDisplayName;
    private RequestQueue queue;
    private CountDownTimer countDownTimer, countDownTimerOtherUser;
    private String callsId = "";
    private Handler connectivityTimer;
    private Runnable connectivityRunnable;
    private boolean isConnectivityDisconnected;
    private ConnectivityStatusReceiver connectivityStatusReceiver;
    private DatabaseReference callsIdEndCallDbRef;
    private DatabaseReference runningCallsIdDbRef, runningCallsIdDbRefPrivateCall;
    private DatabaseReference endLiveSesionDbRef;

    private TextView tvPriceToCall, tvTimeDown, tvPriceToCross, callingUserName, otherUserName, tvTimeDownOtherUser;
    private LinearLayout llAlr1, llAlr2, llAlrOtherUser;
    private ImageView callDisconnect;
    private CircularNetworkImageView profileImg;
    private TextView descPrice, descWalletBalanceTxt, descWalletAmount, descPriceCross;
    private FrameLayout leaveLiveSessionsLayout;
    private LinearLayout giftLayout;
    private LinearLayout mConsultationEnd;
    private LinearLayout mConsultationEndMsg, mEndConsultationFromAstro, mAstroNotLive, ll_desc_join_queue;
    private Button desc_join_video_call, desc_join_audio_call, desc_join_private_call, descRecharge;
    private Button leave_live_btn;
    private Button countinue_live_btn;
    private ImageView desc_cancel;
    private ImageView consult_msg_cancel;
    //private Button consult_msg_rate;
    private ImageView leave_change_btn;
    private ImageView consultMsgCancel;
    private ImageView previous;
    private ImageView next;
    private int myCallingState;
    private ImageView userCallAnimation,userCallAnimationConnecting, otherUserCallAnimation;

    private AstrologerDetailBean astrologerDetailBeanData = null;
    private final int ASTRO_STATUS_UPDATE = 103;
    private FeedbackDialog feedbackDialog;
    private RatingAndDakshinaDialog ratingAndDakshinaDialog;
    private ReportAbuseDialog reportAbuseDialog;
    private long currentTimeStamp;
    private UserProfileData userProfileDataBeanGlobal;
    private CustomDialog dialogMsg;
    private String msgText="",prevMsg="";
    private int agoraUid;
    private String videoStatus = CGlobalVariables.STATUS_ON_VIDEO;
    private String audioStatus = CGlobalVariables.STATUS_ON_AUDIO;
    private ConstraintLayout clMuteInfo;
    private ImageView muteImageIcon;
    private TextView muteInfo, textAstroPinned,textAstroPinnedType;
    private  LinearLayout fl_pined;
    private LinearLayout textPrivateCallInfo;
    private String callTypeForCallingUser = CGlobalVariables.CALL_TYPE_AUDIO;
    private String callTypeForOtherUser;
    private int videoCallUid;
    private CustomProgressDialog pd;
    private boolean getAstroListFromCloseBtn;
    private boolean getAstroListFromEndLiveSession;
    TextView followCount, followTxt,tvLiveViewerCount;
    static String followCountVal, followStatus = "";
    int finish_live_session;
    boolean isAstroOnPvtCall= false;
    boolean isPvtCallTimerDisplayed;

    RelativeLayout rlLayout;

    private ArrayList<GiftModel> giftModelArrayList;
    private Button btnSendGift;
    private LiveGiftAdapter liveGiftAdapter;
    private GiftModel giftModel;
    boolean isLoadChatFirstTime = false;

    // TextView txtUnavailableForCall;
    public ImageView imgliveGift;
    LinearLayout bgMsgLL;
    TextView fromTV;
    TextView messageTV;
    // TextView txtUnavailableForCall;

    long callDurationInSecsPrivateCall;
    long callCreateTimeInMillisecondsPrivateCall;
    String userNamePrivateCall;

    private int BACK_FROM_PROFILECHATDIALOG_PRIVATE_CALL = 2001;
    private FreeMinuteDialog freeMinutedialog;
    private int PRIVATE_CALL = 101;
    private FreeMinuteMinimizeDialog freeMinuteMinimizeDialog;
    private ImageView live_btn_share;
    private ImageView live_btn_abuse_report;
    private ImageView live_btn_mute_audio_2;
    private ImageView live_btn_mute_video_2;
    private ImageView live_btn_switch_camera;
    private ImageView live_btn_beautification;
    private ImageView live_btn_more;
    private ImageView live_btn_push_stream;
    private ImageView live_btn_mute_audio_1;
    private ImageView live_btn_mute_video_1;
    // tap on UI
    private LinearLayout layout_action_btn;
    private ConstraintLayout layout_astro_profile;
    private boolean isUserBlocked;
    public static String crossAstroPrice="",showAstroPrice="";
    private int followReqCount;
    private boolean isJoinSuccess;
    boolean isInPictureInPictureMode = false, isOpenInPip = false;
    OnSwipeTouchListener onSwipeTouchListener;
    private boolean isCallTimerLayoutShown = true;
    private long userSpentTime = 0;
    private String preUserSpentTime = "", preChannelName = "", preAstrologerId = "";
    private ConstraintLayout bottom_layer;
    private String liveUserId;
    private boolean isStatus100;
    private final BroadcastReceiver mReceiverLiveOpenKundli = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.d("blockuserDisabled", "onRecieve");
            blockUser(CGlobalVariables.USER_BLOCKED_NOTIFICATION);
            CUtils.fcmAnalyticsEvents("live_call_end_by_block", AstrosageKundliApplication.currentEventType, "");

        }
    };
    private static final int PERMISSION_REQ_CODE_VIDEO = 1;
    private static final int PERMISSION_REQ_CODE_AUDIO = 2;
    private final String[] PERMISSION = {
            Manifest.permission.RECORD_AUDIO
    };
    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowUtil.hideWindowStatusBar(getWindow());
        setContentView(R.layout.activity_live_room);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if(liveAstrologerModel == null){
            finish();
            return;
        }
        if (getIntent() != null && getIntent().hasExtra(OPEN_LIVE_IN_PIP)) {
            isOpenInPip = getIntent().getBooleanExtra(OPEN_LIVE_IN_PIP, false);
        }
        if (getIntent() != null && getIntent().hasExtra("userspenttime")) {
            preUserSpentTime = getIntent().getStringExtra("userspenttime");
            preChannelName = getIntent().getStringExtra("prechannelname");
            preAstrologerId = getIntent().getStringExtra("preastrologerid");
        }
        initUI();
        setUpListeners();
        initData();
        initUserIcon();
        initCallPrice();
        updateLeaveLiveStatus();
        registerReceiverLiveOpenKundli();
        LocalBroadcastManager.getInstance(LiveActivityNew.this).registerReceiver(openLiveScreenFromNotification, new IntentFilter(CGlobalVariables.SEND_BROADCAST_OPEN_LIVE_SCREEN_FROM_NOTIFICATION));
        //Log.e("ListFlatuate ", " onCreate() ");
        if (isOpenInPip){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPIPMode();
            }
        }
    }
    /**
     *
     */
    private final BroadcastReceiver openLiveScreenFromNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d("MyTestingData","openLiveScreenFromNotification Called");
            LiveAstrologerModel liveAstrologerModel1 = (LiveAstrologerModel) intent.getSerializableExtra(CGlobalVariables.ASTROLOGER_DATA);
            openLiveStramingScrean(liveAstrologerModel1,true);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("testMyprofile","Called onResume");
        unMuteAtrologerAudio();
        setOldAudioState();
        rtcEngine().adjustRecordingSignalVolume(400);
        if(permissionSettingClicked){
            permissionSettingClicked = false;
            if(callTypeForCallingUser != null && callTypeForCallingUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_AUDIO)){
                checkPermissionsAudio();
            }else if(callTypeForCallingUser != null && callTypeForCallingUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_VIDEO)){
                checkPermissionsVideo();
            }

        }

    }

    private void hideChatInputDialog(){
        try {
            if (dialogMsg != null) {
                dialogMsg.closeOnlyKeyboard();
                if (dialogMsg.isShowing()) {
                    dialogMsg.dismiss();
                }
            }
        }catch (Exception e){
            //
        }
    }

    private void updateLeaveLiveStatus() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    AstrosageKundliApplication.leaveChannelState = false;
                    //Log.e("leaveChannelState", "updateLeaveLiveStatus="+AstrosageKundliApplication.leaveChannelState);
                }
            }, 1500);
        } catch (Exception e) {
            //Log.e("LiveStreamAstro ", " onJoinChannelSuccess exp "+e.toString());
        }
    }

    private void unMuteAtrologerAudio() {
        try {
            int astroId = Integer.parseInt(liveAstrologerModel.getId());
            rtcEngine().muteRemoteAudioStream(astroId, false);
        } catch (Exception e) {
            //
        }
    }

    private void setOldAudioState() {
        try {
            if (mMuteAudioBtn.getVisibility() != View.VISIBLE) {
                return; //i.e, audio call is not running
            }
            if (mMuteAudioBtn.isActivated()) {
                //unmute
                rtcEngine().muteLocalAudioStream(false);
                mMuteAudioBtn.setActivated(true);
            } else {
                //mute
                rtcEngine().muteLocalAudioStream(true);
                mMuteAudioBtn.setActivated(false);
            }
        } catch (Exception e) {
            //
        }
    }

    private void initUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        currentActivity = LiveActivityNew.this;
        sendMessageLL = findViewById(R.id.relSendMessage);
        messageTextEdit = findViewById(R.id.editTextMessage);
        giftLayout = findViewById(R.id.layout_gift);
        imgliveGift = findViewById(R.id.imgliveGift);

        recyclerViewLiveStream = findViewById(R.id.recyclerViewLiveStream);


        tvPriceToCall = findViewById(R.id.tvPriceToCall);
        tvPriceToCross = findViewById(R.id.tvPriceToCross);
        //tvPriceToCross.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvTimeDown = findViewById(R.id.tvTimeDown);
        callingUserName = findViewById(R.id.callingUserName);
        otherUserName = findViewById(R.id.otherUserName);
        tvTimeDownOtherUser = findViewById(R.id.tvTimeDownOtherUser);
        llAlr1 = findViewById(R.id.llAlr1);
        llAlr2 = findViewById(R.id.llAlr2);
        llAlrOtherUser = findViewById(R.id.llAlrOtherUser);
        llAlrOtherUser.setTag(false);

        llAlr1.setVisibility(View.GONE);
        llAlr2.setVisibility(View.GONE);
        llAlrOtherUser.setVisibility(View.GONE);
        isCallTimerLayoutShown = true;

        call_astrologer_btn = findViewById(R.id.call_astrologer_btn);
        ll_inner_call_btn = findViewById(R.id.ll_inner_call_btn);
       // call_astrologer_btn.setActivated(isBroadcaster);

        callDisconnect = findViewById(R.id.callDisconnect);
        mVideoGridContainer = findViewById(R.id.live_video_grid_layout);
        mVideoGridContainer.setStatsManager(statsManager());

        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video_2);
        mMuteVideoBtn.setActivated(true);
        mMuteVideoBtn.setVisibility(View.GONE);

        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio_2);
        mMuteAudioBtn.setActivated(true);
        mMuteAudioBtn.setVisibility(View.GONE);

        setClientRoleAudiance();

        mEbableSpeakerBtn = findViewById(R.id.liveBtnEnableSpeaker);
        rtcEngine().setEnableSpeakerphone(true);
        mEbableSpeakerBtn.setActivated(false);

        mBackLayout = findViewById(R.id.layout_back_live);
        mLoginLayout = findViewById(R.id.layout_live_login);
        mFollowLayout = findViewById(R.id.layout_live_follow);
        mUnFollowLayout = findViewById(R.id.layout_live_unfollow);
        mConsultationEnd = findViewById(R.id.layout_live_end_consultation);
        mConsultationEndMsg = findViewById(R.id.layout_live_end_consultation_msg);
        mEndConsultationFromAstro = findViewById(R.id.layout_live_end_consultation_from_astro);
        mAstroNotLive = findViewById(R.id.layout_astro_not_live);


        mAstrologerDescription = findViewById(R.id.layout_live_astrologer_description);
        ll_desc_join_queue = findViewById(R.id.ll_desc_join_queue);
        desc_join_video_call = findViewById(R.id.desc_join_video_call);
        desc_join_audio_call = findViewById(R.id.desc_join_audio_call);
        desc_join_private_call = findViewById(R.id.desc_join_private_call);
        descRecharge = findViewById(R.id.descRecharge);

        leave_live_btn = findViewById(R.id.leave_live_btn);
        countinue_live_btn = findViewById(R.id.countinue_live_btn);
        desc_cancel = findViewById(R.id.desc_cancel);
        consult_msg_cancel = findViewById(R.id.consult_msg_cancel);
        //consult_msg_rate = findViewById(R.id.consult_msg_rate);
        leave_change_btn = findViewById(R.id.leave_change_btn);
        // Astro end session or call
        consultMsgCancel = findViewById(R.id.consult_msg_cancel_1);
        previous = (ImageView) findViewById(R.id.previous);
        next = (ImageView) findViewById(R.id.next);
        clMuteInfo = findViewById(R.id.cl_mute_info);
        clMuteInfo.setTag(false);
        muteImageIcon = findViewById(R.id.mute_image_icon);
        muteInfo = findViewById(R.id.text_mute_info);
        textAstroPinned = findViewById(R.id.text_astro_pinned);
        textAstroPinnedType = findViewById(R.id.text_astro_pinned_type);
        fl_pined = findViewById(R.id.fl_pined);
        fl_pined.setTag(false);
        textPrivateCallInfo = findViewById(R.id.text_private_call_info);
        textPrivateCallInfo.setTag(false);
        bottom_layer = findViewById(R.id.bottom_layer);

        TextView external_call = findViewById(R.id.external_call);
        FontUtils.changeFont(currentActivity, external_call, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(currentActivity, muteInfo, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        userCallAnimation = (ImageView) findViewById(R.id.userCallAnimation);
        userCallAnimationConnecting = (ImageView) findViewById(R.id.userCallAnimationConnecting);
        otherUserCallAnimation = (ImageView) findViewById(R.id.otherUserCallAnimation);


        descPrice = findViewById(R.id.desc_price);
        descPriceCross = findViewById(R.id.descPriceCross);
        //descPriceCross.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        descWalletBalanceTxt = findViewById(R.id.descWalletBalanceTxt);
        descWalletBalanceTxt.setVisibility(View.GONE);
        profileImg = findViewById(R.id.profileImg);
        descWalletAmount = findViewById(R.id.descWalletAmount);
        followCount = findViewById(R.id.followCount);
        followTxt = findViewById(R.id.followTxt);
        tvLiveViewerCount = findViewById(R.id.tv_live_viewers_count);
        bgMsgLL= findViewById(R.id.bgMsgLL);
        fromTV= findViewById(R.id.fromTV);
        messageTV= findViewById(R.id.messageTV);
        live_btn_share = findViewById(R.id.live_btn_share);
        live_btn_abuse_report = findViewById(R.id.live_btn_abuse_report);
        live_btn_switch_camera = findViewById(R.id.live_btn_switch_camera);
        live_btn_beautification = findViewById(R.id.live_btn_beautification);
        live_btn_more = findViewById(R.id.live_btn_more);
        live_btn_push_stream = findViewById(R.id.live_btn_push_stream);
        live_btn_mute_audio_1 = findViewById(R.id.live_btn_mute_audio_1);
        live_btn_mute_video_1 = findViewById(R.id.live_btn_mute_video_1);
        // tap on UI
        layout_action_btn = findViewById(R.id.layout_action_btn);
        layout_astro_profile = findViewById(R.id.layout_astro_profile);

        FontUtils.changeFont(this, fromTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        followTxt.setOnClickListener(view ->
        {
            if(followStatus == null || followStatus.equals("0"))
            {
                if (CUtils.getUserLoginStatus(currentActivity)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_FOLLOW_ASTROLOGER_BTN_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    //finish_live_session=0;
                    followReqCount = 0;
                    followAstrologerRequest("1");
                } else {
                    openLoginDialog();
                }
            }
            else {
                //finish_live_session=0;
                openUnFollowDialog();
            }
        });

        descWalletAmount.setText(getResources().getString(R.string.rs_sign) + CUtils.getWalletRs(currentActivity));
        messageTextEdit.setOnClickListener(view -> {
            try {
                showMessageDialogWithKeyboard();
            }catch (Exception e){
                //
            }
        });

        rlLayout = findViewById(R.id.rlLiveRoom);


        onSwipeTouchListener = new OnSwipeTouchListener(this, rlLayout,this);

        rlLayout.setOnClickListener(view -> {
            //Log.e("SAN ", " LA rlLayout screenTapUntap() ");
            screenTapUntap();
        });


        if(!CUtils.getBooleanData(LiveActivityNew.this,CGlobalVariables.SHOWN_LIVE_INTRO, false) && !isOpenInPip){
            showIntroFragment();
        }

    }

    public void screenTapUntap(){

        //Log.e("SAN ", " LA screenTapUntap() ");

        if (layout_action_btn.isShown()){
            hideControlerOnTap();
        } else {
            showControlerOnTap();
        }

    }

    public void hideIntroFragment(){
        disableEnableControls(true,rlLayout);
        findViewById(R.id.frameLiveIntro).setVisibility(View.GONE);
    }

    private void showIntroFragment(){
        try {
            disableEnableControls(false, rlLayout);
            findViewById(R.id.frameLiveIntro).setVisibility(View.VISIBLE);
            Fragment newFragment = new LiveIntroFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frameLiveIntro, newFragment).commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showControlerOnTap(){
        isCallTimerLayoutShown = true;
        if (layout_astro_profile != null) {
            layout_astro_profile.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.live_room_top_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_astro_profile).setVisibility(View.VISIBLE);
        }
        layout_action_btn.setVisibility(View.VISIBLE);
        sendMessageLL.setVisibility(View.VISIBLE);
        recyclerViewLiveStream.setVisibility(View.VISIBLE);
        if ((boolean) fl_pined.getTag()){
            fl_pined.setVisibility(View.VISIBLE);
        }else {
            fl_pined.setVisibility(View.INVISIBLE);
        }

        if ((boolean) textPrivateCallInfo.getTag()){
            textPrivateCallInfo.setVisibility(View.VISIBLE);
        }else {
            textPrivateCallInfo.setVisibility(View.INVISIBLE);
        }

        if ((boolean) clMuteInfo.getTag()){
            clMuteInfo.setVisibility(View.VISIBLE);
        }else {
            clMuteInfo.setVisibility(View.INVISIBLE);
        }

        if ((boolean) llAlrOtherUser.getTag()){
            llAlrOtherUser.setVisibility(View.VISIBLE);
        }else {
            llAlrOtherUser.setVisibility(View.INVISIBLE);
        }

        if(isUserBlocked) {
            showHideChatCallUI(false);
        }
    }
    private void hideControlerOnTap(){
        isCallTimerLayoutShown = false;
        if (layout_astro_profile != null) {
            layout_astro_profile.setVisibility(View.INVISIBLE);
        }
        layout_action_btn.setVisibility(View.INVISIBLE);
        recyclerViewLiveStream.setVisibility(View.INVISIBLE);
        sendMessageLL.setVisibility(View.INVISIBLE);
        fl_pined.setVisibility(View.INVISIBLE);
        textPrivateCallInfo.setVisibility(View.INVISIBLE);
        clMuteInfo.setVisibility(View.INVISIBLE);
        llAlrOtherUser.setVisibility(View.INVISIBLE);
    }
    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup)child);
            }
        }
    }

    public void followUnfollowAstrologer()
    {
        if(followStatus == null || followStatus.equals("0"))
        {
            if (CUtils.getUserLoginStatus(currentActivity)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_FOLLOW_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //finish_live_session=0;
                followAstrologerRequest("1");
            } else {
                openLoginDialog();
            }
        }
        else {
            openUnFollowDialog();
        }
    }
    private void showMessageDialogWithKeyboard() {
        try {
            dialogMsg = new CustomDialog(currentActivity, messageTextEdit, imgliveGift);
            dialogMsg.setContentView(R.layout.dialog_live_send_message);
            dialogMsg.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            dialogMsg.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialogMsg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogMsg.getWindow().setDimAmount(0);
            dialogMsg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            EditText editText = dialogMsg.findViewById(R.id.editTextMessage);
            editText.setText(msgText);
            editText.requestFocus();
            ImageView sendMessage = dialogMsg.findViewById(R.id.buttonSend);
            sendMessage.setOnClickListener(view -> {
                if (CUtils.getUserLoginStatus(currentActivity)) {

                    String messageText = editText.getText().toString().trim();

                    if ( messageText.length() > 0 ) {

                        //String unicodeMessageText = getUnicodeString(messageText);

                        giftModelArrayList = CUtils.getGiftModelArrayList();
                        GiftModel giftModel = null;
                        boolean isFound = false;
                        if(giftModelArrayList != null) {
                            for (int i = 0; i < giftModelArrayList.size(); i++) {
                                giftModel = giftModelArrayList.get(i);
                                try {
                                    if (messageText.toLowerCase().contains(giftModel.getIconcode().toLowerCase())) {
                                        isFound = true;
                                        break;
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }

                        if ( isFound ) {
                            CUtils.showSnackbar(editText, getResources().getString(R.string.text_emoji_is_not_allowed), currentActivity);
                        } else {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_SEND_MESSAGE_BTN_CLICK,
                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            sendMessage(editText);
                        }

                    }

                } else {
                    dialogMsg.onBackPressed();
                    openLoginDialog();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editText.setShowSoftInputOnFocus(true);
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    msgText = charSequence.toString();
                    if(charSequence.toString().isEmpty()){
                        sendMessage.setBackgroundResource(R.drawable.circle_gray_live);
                        sendMessage.setColorFilter(ContextCompat.getColor(LiveActivityNew.this, R.color.no_change_white));
                    }else{
                        sendMessage.setBackgroundResource(R.drawable.bg_button_orange);
                        sendMessage.setColorFilter(ContextCompat.getColor(LiveActivityNew.this, R.color.white));
                    }
                  }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

        /*dialogMsg.setOnDismissListener(dialog -> {
            if (getIntent() != null && getIntent().hasExtra(OPEN_LIVE_IN_PIP)) {
                isOpenInPip = getIntent().getBooleanExtra(OPEN_LIVE_IN_PIP, false);
            }
        });*/

            dialogMsg.show();
            messageTextEdit.setVisibility(View.GONE);
            imgliveGift.setVisibility(View.GONE);
        }catch (Exception e){
            //
        }
        //isOpenInPip = false;

    }

//    private String getUnicodeString(String str) {
//
//        StringBuilder retStr = new StringBuilder();
//        for(int i=0; i<str.length(); i++) {
//            int cp = Character.codePointAt(str, i);
//            int charCount = Character.charCount(cp);
//            if (charCount > 1) {
//                i += charCount - 1; // 2.
//                if (i >= str.length()) {
//                    throw new IllegalArgumentException("truncated unexpectedly");
//                }
//            }
//
//            if (cp < 128) {
//                retStr.appendCodePoint(cp);
//            } else {
//                retStr.append(String.format("U+%x", cp));
//            }
//        }
//        return retStr.toString();
//    }

    private void showHideChatCallUI(boolean isShow) {
        //Log.d("TestLiveBlock", "showHideChatCallUI() isUserBlocked="+isUserBlocked);
        try {
            if (isShow && !isUserBlocked) { //show controls in case of user not blocked
                if (!isInPictureInPictureMode) {
                    recyclerViewLiveStream.setVisibility(View.VISIBLE);
                    call_astrologer_btn.setVisibility(View.VISIBLE);
                    sendMessageLL.setVisibility(View.VISIBLE);
                    followTxt.setEnabled(true);
                }
            } else {
                recyclerViewLiveStream.setVisibility(View.GONE);
                call_astrologer_btn.setVisibility(View.GONE);
                sendMessageLL.setVisibility(View.INVISIBLE);
                followTxt.setEnabled(false);
                live_btn_share.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //
        }
    }

    private boolean getIsUserBlocked(){
        boolean isBlocked = false;
        try {
            String currentAstrologerId = liveAstrologerModel.getId();
            String[] blockedAstroIds = CUtils.getblockByAstrologerList();
            if (blockedAstroIds != null) {
                for (int i = 0; i < blockedAstroIds.length; i++) {
                    String astrologer = blockedAstroIds[i];
                    if (currentAstrologerId.equals(astrologer)) {
                        isBlocked = true;
                        break;
                    }
                }
            }
        }catch (Exception e){
            //
        }
        return isBlocked;
    }

    private void setUpListeners() {

        call_astrologer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_CALL_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                try {
                    boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
                    if (isLogin) {
                        getAstroCurrentCallStatus();
                    } else {
                        openLoginDialog();
                    }
                }catch (Exception e){
                    //
                }
            }
        });

        callDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_CALL_DISCONNECT_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                initEndConsultation();
                open_layout(mConsultationEnd);
            }
        });

        mLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mFollowLayout);
                }
            }
        });
        mUnFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mUnFollowLayout);
                }
            }
        });

        mConsultationEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        leave_live_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_LEAVE_LIVE_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                finishLiveSession(CGlobalVariables.FINISH_LIVE_SESSION_USER);
            }
        });

        countinue_live_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_CONTINUE_LIVE_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                close_layout(mBackLayout);
            }
        });


        desc_join_video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AstrosageKundliApplication.currentEventType = CGlobalVariables.LIVE_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("live_video_call_btn_click_", AstrosageKundliApplication.currentEventType, "");




                try {
                    if (videoStatus.equals(CGlobalVariables.STATUS_OFF_VIDEO)) {
                        CUtils.fcmAnalyticsEvents("live_video_call_btn_click_video_off", CGlobalVariables.LIVE_BTN_CLICKED, "");
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.astrologer_unavailable_video_call), currentActivity);
                        return;
                    }
                    AstrosageKundliApplication.currentEventType = CGlobalVariables.LIVE_BTN_CLICKED;
                    CUtils.fcmAnalyticsEvents("live_video_call_btn_click_", AstrosageKundliApplication.currentEventType, "");

                    callTypeForCallingUser = CGlobalVariables.CALL_TYPE_VIDEO;
                    checkPermissionsVideo();
                    //openProfileActivity();
                } catch (Exception e){
                    //
                }
            }
        });

        desc_join_audio_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AstrosageKundliApplication.currentEventType = CGlobalVariables.LIVE_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("live_audio_call_btn_click_", AstrosageKundliApplication.currentEventType, "");

//                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_CALL_NOW_BTN_CLICK,
//                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                callTypeForCallingUser = CGlobalVariables.CALL_TYPE_AUDIO;
                checkPermissionsAudio();

            }
        });

        desc_join_private_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CUtils.isChatNotInitiated()) {
                    AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                    CUtils.fcmAnalyticsEvents("call_btn_click_privatelive", AstrosageKundliApplication.currentEventType, "");

                    openProfileActivityForPrivateCall();
                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.allready_in_chat), Toast.LENGTH_LONG).show();
                }
            }
        });


        descRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_DES_RECHARGE_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(currentActivity, WalletActivity.class);
                intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "LiveStreaming");
                startActivity(intent);
            }
        });

        desc_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_DES_CANCEL_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                ll_desc_join_queue.setVisibility(View.VISIBLE);
                descRecharge.setVisibility(View.GONE);
                descWalletBalanceTxt.setVisibility(View.GONE);
                call_astrologer_btn.setClickable(true);
                close_layout(mAstrologerDescription);
            }
        });

        consult_msg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_layout(mConsultationEndMsg);
            }
        });

        /*consult_msg_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ASTRO_RATE_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                close_layout(mConsultationEndMsg);
                if (liveAstrologerModel != null) {
                    Log.e("TestLiveCall", "getAstrologerStatusPrice1");
                    getAstrologerStatusPrice(liveAstrologerModel.getId());
                }
            }
        });*/

        leave_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_LEAVE_LIVE_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                hidePopUnders();
                Log.d("LiveCountIssue", "onClick: leave_change_btn");
                if (llAlr2.getVisibility() == View.VISIBLE) {
                    initEndConsultation();
                    open_layout(mConsultationEnd);
                    return;
                }
                getAstroListFromCloseBtn = true;
                getLiveAstrologerDataFromServer();

                //onLeaveClicked();

            }
        });

        // Astro end session or call
        consultMsgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_layout(mEndConsultationFromAstro);
                onLeaveClicked();
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPrevious();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNext();
            }
        });

        imgliveGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CUtils.getUserLoginStatus(currentActivity)) {
                    openLoginScreen();
                } else {
                    giftModelArrayList = CUtils.getGiftModelArrayList();
                    if(giftModelArrayList != null && !giftModelArrayList.isEmpty()){
                        openGiftDialog();
                    }else {
                        try {
                            apiRepository = new ApiRepository(LiveActivityNew.this);
                            Map<String, String> stringMap = com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroParams(LiveActivityNew.this, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(LiveActivityNew.this));
                            apiRepository.getGiftDataFromServer(stringMap, new ApiRepository.ApiCallback() {
                                @Override
                                public void onSuccess() {
                                    giftModelArrayList = CUtils.getGiftModelArrayList();
                                    openGiftDialog();
                                }

                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                        }catch (Exception e){

                        }
                    }

                }
            }
        });

        live_btn_share.setOnClickListener(v -> {
            showProgressBar();
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_LIVE_ACTIVITY_SHARE, FIREBASE_EVENT_ITEM_CLICK, "");
            CUtils.shareLiveSession(this,liveAstrologerModel, CUtils.getViewBitmap(LiveActivityNew.this,findViewById(R.id.sllConstraintLayout)));
            hideProgressBar();
        });

        live_btn_abuse_report.setOnClickListener(v->{
            onAbuseReportClicked();
        });

        mMuteAudioBtn.setOnClickListener(v->{
            onMuteAudioClicked(mMuteAudioBtn);
        });

        mMuteVideoBtn.setOnClickListener(v->{
            onMuteVideoClicked(mMuteVideoBtn);
        });

        mEbableSpeakerBtn.setOnClickListener(v->{
            onSpeakerClicked(mEbableSpeakerBtn);
        });

        live_btn_switch_camera.setOnClickListener(v->{
            onSwitchCameraClicked(live_btn_switch_camera);
        });

        live_btn_beautification.setOnClickListener(v->{
            onBeautyClicked(live_btn_beautification);
        });

        live_btn_more.setOnClickListener(v->{
            onMoreClicked(live_btn_more);
        });

        live_btn_push_stream.setOnClickListener(v->{
            onPushStreamClicked(live_btn_push_stream);
        });

        live_btn_mute_audio_1.setOnClickListener(v->{
            onMuteAudioClicked(live_btn_mute_audio_1);
        });

        live_btn_mute_video_1.setOnClickListener(v->{
            onMuteVideoClicked(live_btn_mute_video_1);
        });

    }
    private void openGiftDialog(){
        initGiftDialog();
        makeGiftUnSelected();
        open_layout(giftLayout);
        sendMessageLL.setVisibility(View.INVISIBLE);
    }
    private void callNext(){
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ASTRO_NEXT_CLICK,
                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        try {
            if (liveAstrologerModelList == null){
                return;
            }

            if (liveAstrologerModelList.size() > (currentPos + 1)) {
                LiveAstrologerModel astrologerModel = liveAstrologerModelList.get(currentPos + 1);
                openLiveStramingScrean(astrologerModel,true);
            }
        } catch (Exception e) {
            //
        }
    }

    private void callPrevious(){
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ASTRO_PREVIOUS_CLICK,
                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        try {
            if (liveAstrologerModelList == null) return;
            if (liveAstrologerModelList.size() > 1) {
                LiveAstrologerModel astrologerModel = liveAstrologerModelList.get(currentPos - 1);
                openLiveStramingScrean(astrologerModel,false);
            }
        } catch (Exception e) {
            //
        }
    }

    private void openFollowDialog() {
        initAstrologerFollowDialog();
        if (mFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mFollowLayout);
        }
        if(followStatus == null || followStatus.equals("0"))
        {
            if(giftLayout.getVisibility()==View.GONE)
                open_layout(mFollowLayout);
        }
    }
    private void openUnFollowDialog() {
        initAstrologerUnFollowDialog();
        if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mUnFollowLayout);
        }
        if(followStatus != null && followStatus.equals("1"))
        {
            open_layout(mUnFollowLayout);
        }
    }

    private void openLoginDialog() {
        initAstrologerLoginDialog();
        if (mLoginLayout.getVisibility() == View.VISIBLE) {
            close_layout(mLoginLayout);
        }
        open_layout(mLoginLayout);
    }

    private void changeViewState(int position) {
        if (liveAstrologerModelList == null) return;

        if (liveAstrologerModelList.size() == 1) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else if (position <= 0) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        } else if (position >= liveAstrologerModelList.size() - 1) {
            previous.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);

        } else {
            previous.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);

        }

    }

    private void openLoginScreen() {
        try {
            AstrosageKundliApplication.liveAstrologerModel = liveAstrologerModel;
            Intent intent = new Intent(currentActivity, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.LIVESTREAMING_SCRREN);
            startActivity(intent);
            finishLiveSession(CGlobalVariables.FINISH_LIVE_SESSION_OPEN_LOGIN);
        } catch (Exception e) {
            //
        }
    }

    private void initUserIcon() {
        TextView sllName = findViewById(R.id.sllName);
        TextView astrologerName = findViewById(R.id.live_astrologer_name);
        FontUtils.changeFont(currentActivity,sllName,FONTS_ROBOTO_BOLD);
        astrologerName.setText(liveAstrologerModel.getName());
        sllName.setText(CUtils.removeAcharyaTarot(liveAstrologerModel.getName()));
        CircularNetworkImageView iconView = findViewById(R.id.live_name_board_icon);
        CircularNetworkImageView sllImage = findViewById(R.id.sllImage);
        String astrologerProfileUrl = "";
        if (liveAstrologerModel.getProfileImgUrl() != null && liveAstrologerModel.getProfileImgUrl().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModel.getProfileImgUrl();
            //iconView.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(currentActivity).getImageLoader());
            //sllImage.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(currentActivity).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(iconView);
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(sllImage);
//            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(profileImg);

            //Log.e("liveimageurl", " : " + astrologerProfileUrl);
        }
        iconView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", "");
            bundle.putString("urlText", liveAstrologerModel.getUrltext());
            bundle.putBoolean("isFromLiveActivity",true);
            Intent intent = new Intent(currentActivity, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void initCallPrice() {
        boolean liveIntroOffer = CUtils.isLiveintrooffer(this);
        if (!CUtils.getUserLoginStatus(this)) {
            liveIntroOffer = true;
        }

        String amountStr = currentActivity.getResources().getString(R.string.live_call_price);
        if (liveIntroOffer) {
            amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudiointroprice());
            tvPriceToCall.setText(amountStr);
            //tvPriceToCross.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
            CUtils.setStrikeOnTextView(tvPriceToCross, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
            showAstroPrice = amountStr;
            crossAstroPrice =getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice();
        } else if (liveAstrologerModel.getActualliveaudioprice().equals(liveAstrologerModel.getLiveaudioprice())) {
            amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudioprice());
            tvPriceToCall.setText(amountStr);
            crossAstroPrice = "";
            showAstroPrice = amountStr;
            tvPriceToCross.setVisibility(View.GONE);
        } else {
            amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudioprice());
            tvPriceToCall.setText(amountStr);
            //tvPriceToCross.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
            CUtils.setStrikeOnTextView(tvPriceToCross, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
            showAstroPrice = amountStr;
            crossAstroPrice = getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice();
        }

    }

    private void setCallPrice() {
        boolean liveIntroOffer = CUtils.isLiveintrooffer(this);
        if (!CUtils.getUserLoginStatus(this)) {
            liveIntroOffer = true;
        }

        String amountStr = currentActivity.getResources().getString(R.string.live_call_price);
        if (liveIntroOffer) {
            amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudiointroprice());
            //tvPriceToCall.setText(amountStr);
            descPrice.setText(amountStr);
            //descPriceCross.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
            CUtils.setStrikeOnTextView(descPriceCross, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());


        } else if (liveAstrologerModel.getActualliveaudioprice().equals(liveAstrologerModel.getLiveaudioprice())) {
            amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudioprice());
            //tvPriceToCall.setText(amountStr);
            descPrice.setText(amountStr);
            descPriceCross.setVisibility(View.GONE);
        } else {
            amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudioprice());
            //tvPriceToCall.setText(amountStr);
            descPrice.setText(amountStr);
            //descPriceCross.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
            CUtils.setStrikeOnTextView(descPriceCross, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());

        }

        if (liveAstrologerModel.getProfileImgUrl() != null && liveAstrologerModel.getProfileImgUrl().length() > 0) {
            String astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModel.getProfileImgUrl();
            //profileImg.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(currentActivity).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(profileImg);


        }

    }

    private void initData() {
        isUserBlocked = getIsUserBlocked();
        userDisplayName = CUtils.getUserDisplayName(currentActivity);
        liveAstrologerModelList = new ArrayList<>();
        parseLiveAstrologerList(CUtils.getLiveAstroList());
        currentPos = getCurrentAstrologerPossition();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        connectivityStatusReceiver = new ConnectivityStatusReceiver();
        chatMessageArrayList = new ArrayList<>();
        mVideoDimension = com.ojassoft.astrosage.vartalive.utils.Constants.VIDEO_DIMENSIONS[
                config().getVideoDimenIndex()];

        if (queue == null)
            queue = VolleySingleton.getInstance(currentActivity).getRequestQueue();

        liveStreamMsgDetailAdapter = new LiveStreamMsgInfoAdapter(currentActivity, chatMessageArrayList, new LiveStreamMsgInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                if (layout_action_btn.isShown()){
                    hideControlerOnTap();
                }else {
                    showControlerOnTap();
                }
            }
        });
        linearLayoutManager = new WrapContentLinearLayoutManager(currentActivity);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewLiveStream.setLayoutManager(linearLayoutManager);
        recyclerViewLiveStream.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLiveStream.setAdapter(liveStreamMsgDetailAdapter);

        Glide.with(getApplicationContext()).load(R.drawable.sound_gif).into(userCallAnimation);
//        Glide.with(currentActivity).load(R.drawable.sound_gif).into(userCallAnimationConnecting);
        Glide.with(getApplicationContext()).load(R.drawable.sound_gif).into(otherUserCallAnimation);


        changeViewState(currentPos);


    }

    /**
     *
     */
    private void getVideoStatus() {
        String channelId = channelName;
        try {
            if (videoDBRef != null && videoEventListener != null) {
                videoDBRef.removeEventListener(videoEventListener);
            }

            videoDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + ISVIDEOMUTED);
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
    private void getAudioStatus() {
        String channelId = channelName;
        try {
            if (audioDBRef != null && audioEventListener != null) {
                audioDBRef.removeEventListener(audioEventListener);
            }

            audioDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + CGlobalVariables.ISAUDIOMUTED);
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


    private void getBlockUser() {
        String userId = CUtils.getUserIdForBlock(currentActivity);
        if (!userId.isEmpty()) {
            String channelId = channelName;
            try {
                if (userBlockDBRef != null && userBlockEventListener != null) {
                    userBlockDBRef.removeEventListener(userBlockEventListener);
                }
                userBlockDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + userId + "/" + CGlobalVariables.ISUSERBLOCK);
                userBlockEventListener = (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                // String astrologerid = snapshot.child("astrologerid").getValue(String.class);
                                String blockUserId = snapshot.child("userid").getValue(String.class);
                                if (blockUserId.equalsIgnoreCase(userId)) {
                                    isUserBlocked = true;
                                    blockUser(CGlobalVariables.USER_BLOCKED_FIREBASE);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                userBlockDBRef.addValueEventListener(userBlockEventListener);
            } catch (Exception e) {
            }
        }
    }


    private void blockUser(String remarks){
        call_astrologer_btn.setVisibility(View.GONE);
        sendMessageLL.setVisibility(View.INVISIBLE);
        followTxt.setEnabled(false);
        imgliveGift.setVisibility(View.GONE);
        updateEndCallStatusInFirebaseDB(0, remarks);
        processEndCallForCallingUser();
        hideChatInputDialog();
        followAstrologerRequest("0");
    }



    /**
     *
     */
    private void setVideoAudioMuteView() {
        try {
            // States=> VideoOff AudioOff AudioOn VideoOn
            boolean tag = false;
            if (audioStatus.equals(CGlobalVariables.STATUS_ON_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_ON_VIDEO))) {
                tag = false;
            } else if (audioStatus.equals(CGlobalVariables.STATUS_OFF_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_ON_VIDEO))) {
                tag = true;
                muteImageIcon.setImageResource(R.drawable.ic_live_mute);
                muteInfo.setText(getResources().getString(R.string.muteInfoMic));
            } else if (audioStatus.equals(CGlobalVariables.STATUS_ON_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_OFF_VIDEO))) {
                tag = true;
                muteImageIcon.setImageResource(R.drawable.ic_live_mute_video);
                muteInfo.setText(getResources().getString(R.string.muteInfoVideo));
            } else if (audioStatus.equals(CGlobalVariables.STATUS_OFF_AUDIO) && (videoStatus.equals(CGlobalVariables.STATUS_OFF_VIDEO))) {
                tag = true;
                muteImageIcon.setImageResource(R.drawable.ic_live_mute);
                muteInfo.setText(getResources().getString(R.string.muteInfoMicVideo));
            }

            if (tag){
                if (!isInPictureInPictureMode){
                    clMuteInfo.setVisibility(View.VISIBLE);
                }
            } else {
                clMuteInfo.setVisibility(View.GONE);
            }
            clMuteInfo.setTag(tag);

        } catch (Exception e) {
        }

    }

    /**
     *
     */
    private void getPinnedMessage() {
        String channelId = channelName;
        try {
            if (pinMessageDBRef != null && pinMessageEventListener != null) {
                pinMessageDBRef.removeEventListener(pinMessageEventListener);
            }
            pinMessageDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + CGlobalVariables.ASTRO_DESCRIPTION);
            pinMessageEventListener = (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (!Objects.requireNonNull(snapshot.getValue()).toString().equals("")) {
                            fl_pined.setVisibility(View.VISIBLE);
                            fl_pined.setTag(true);
                            // textAstroPinned.setClickable(true);
                            textAstroPinned.setMovementMethod(LinkMovementMethod.getInstance());
                            String dataString = Objects.requireNonNull(snapshot.getValue()).toString();
                            String[] parts = dataString.split("@@_-");
                            if (parts.length == 2) {
                                textAstroPinnedType.setVisibility(View.VISIBLE);
                                textAstroPinnedType.setMovementMethod(new ScrollingMovementMethod());
                                String link = parts[1];
                                String linkText = parts[0];
                                String[] linkParts = linkText.split(" : ");
                                textAstroPinnedType.setText(getResources().getString(R.string.pin_symbol) + " " +linkParts[0]+" : "+linkParts[1]+" - ");
                                // String linkText = getResources().getString(R.string.pin_symbol)+ parts[0];
                                String text = "<a href='" + link + "'> " + link + " </a>";
                                textAstroPinned.setLinkTextColor(getResources().getColor(R.color.Bluecolor));
                                textAstroPinned.setText(Html.fromHtml(text));
                            } else{
                                textAstroPinnedType.setVisibility(View.GONE);
                                textAstroPinned.setText(getResources().getString(R.string.pin_symbol) + " " + parts[0]);
//                                String linkText = getResources().getString(R.string.pin_symbol)+ Objects.requireNonNull(snapshot.getValue()).toString();
//                                String linkText = Objects.requireNonNull(snapshot.getValue()).toString();
//                                String text = "<a href='" + linkText + "'> " + linkText + " </a>";
//                                textAstroPinned.setText(Html.fromHtml(text));
                            }

                        } else{
                            fl_pined.setVisibility(View.GONE);
                            fl_pined.setTag(false);
                        }
                    } catch (Exception e) {
                        fl_pined.setVisibility(View.GONE);
                        fl_pined.setTag(false);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            pinMessageDBRef.addValueEventListener(pinMessageEventListener);
        } catch (Exception e) {
        }

    }


    private void getAstroCallStatus() {
        // callStatusValueEventListener

        String channelId = channelName;
        try {
            if (callStatusDBRef != null && callStatusValueEventListener != null) {
                callStatusDBRef.removeEventListener(callStatusValueEventListener);
            }
            callStatusDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + CGlobalVariables.CALL_STATUS);
            callStatusValueEventListener = (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Map<String, String> map = (Map) dataSnapshot.getValue();
                        String audioStatus = map.get(ASTRO_AUDIO_STATUS);
                        String videoStatus = map.get(CGlobalVariables.ASTRO_VIDEO_STATUS);
                        String privateStatus = map.get(CGlobalVariables.ASTRO_PRIVATE_CALL_STATUS);

                        audioCallStatus = audioStatus;
                        videoCallStatus = videoStatus;
                        privateCallStatus = privateStatus;
                        if (audioStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_OFF) && (videoStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_OFF)) && (privateStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_OFF))) {
                            call_astrologer_btn.setEnabled(false);
                            ll_inner_call_btn.setBackground(ContextCompat.getDrawable(currentActivity, R.drawable.light_gray_rect_less_rounded));
                        } else {
                            call_astrologer_btn.setEnabled(true);
                            ll_inner_call_btn.setBackground(ContextCompat.getDrawable(currentActivity, R.drawable.bg_border_live_call));
                        }
                    } catch (Exception e) {
                        //
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            callStatusDBRef.addValueEventListener(callStatusValueEventListener);
        } catch (Exception e) {
        }
    }
    private void getAstroCurrentCallStatus() {
        String channelId = channelName;
        mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + CGlobalVariables.CALL_STATUS).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean isAllBtnDesabled = false;
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Map<String, String> map = (Map) task.getResult().getValue();
                    String audioStatus = map.get(CGlobalVariables.ASTRO_AUDIO_STATUS);
                    String videoStatus = map.get(CGlobalVariables.ASTRO_VIDEO_STATUS);
                    String privateStatus = map.get(CGlobalVariables.ASTRO_PRIVATE_CALL_STATUS);

                    audioCallStatus = audioStatus;
                    videoCallStatus = videoStatus;
                    privateCallStatus = privateStatus;
                    if (audioStatus != null && videoStatus != null && privateStatus != null) {
                        if (audioStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_OFF) && (videoStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_OFF)) && (privateStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_OFF))) {
                            isAllBtnDesabled = true;
                            call_astrologer_btn.setEnabled(false);
                            ll_inner_call_btn.setBackground(ContextCompat.getDrawable(currentActivity, R.drawable.light_gray_rect_less_rounded));
                        } else {
                            call_astrologer_btn.setEnabled(true);
                            ll_inner_call_btn.setBackground(ContextCompat.getDrawable(currentActivity, R.drawable.bg_border_live_call));
                        }
                    }
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
                if(!isAllBtnDesabled) { //dialog will not show in case of all button desabled
                    initAstrologerDescriptionDialog();
                    call_astrologer_btn.setClickable(false);
                    open_layout(mAstrologerDescription);
                    setCallPrice();
                    if(giftLayout.getVisibility()== View.VISIBLE){
                         close_layout(giftLayout);
                        sendMessageLL.setVisibility(View.VISIBLE);
                        }
                }
            }
        });
    }

    protected void onGlobalLayoutCompleted() {
        RelativeLayout topLayout = findViewById(R.id.live_room_top_layout);
        layout_astro_profile = findViewById(R.id.layout_astro_profile);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
        params.height = mStatusBarHeight + topLayout.getMeasuredHeight();
        topLayout.setLayoutParams(params);
        topLayout.setPadding(0, mStatusBarHeight, 0, 0);
    }

    private int getCurrentAstrologerPossition() {
        int currentPos = 0;
        try {
            for (int i = 0; i < liveAstrologerModelList.size(); i++) {
                LiveAstrologerModel liveAstroModel = this.liveAstrologerModelList.get(i);
                if (liveAstroModel != null && liveAstrologerModel != null) {
                    if (liveAstroModel.getId().equalsIgnoreCase(liveAstrologerModel.getId())) {
                        currentPos = i;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            //
        }
        return currentPos;
    }

    /**
     * This method is used to start the audio/vedeo call with new token for the broadcaster.
     * @param newToken
     */
    private void startAudioCallBroadcast(String newToken) {
        //refresh the token during the call with role publisher(means audio/video will be share of user)
        rtcEngine().renewToken(newToken);
        //Log.e("TestLiveCall", " startAudioCallBroadcast() newToken = " + newToken);
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true);

        if (callTypeForCallingUser != null && callTypeForCallingUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_VIDEO)) {
            mVideoGridContainer.addUserVideoSurface(0, surface, true);
            rtcEngine().enableLocalVideo(true);
        } else {
            rtcEngine().enableLocalVideo(false);
        }
    }

    /**
     * This method is used to stop the audio/video call with existing token and set the client role audiance
     */
    private void stopAudioCallBroadcast() {
        if(!TextUtils.isEmpty(token)){
            //refresh the token with old token used during live join while stop the call means audio/video sharing will be stopped
            rtcEngine().renewToken(token);
            //Log.e("TestLiveCall", " stopAudioCallBroadcast() token = " + token);
        }
        setClientRoleAudiance();

        removeRtcVideo(0, true);
        mVideoGridContainer.removeUserVideo(0, true);
    }

    private void setClientRoleAudiance() {
        ClientRoleOptions clientRoleOptions = new ClientRoleOptions();
        clientRoleOptions.audienceLatencyLevel = Constants.AUDIENCE_LATENCY_LEVEL_LOW_LATENCY;
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE, clientRoleOptions);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        agoraUid = uid;
        //Log.d("MyTestingData","onJoinChannelSuccess called channel ==="+channel);
        //Log.e("LiveStreamAstro ", " onJoinChannelSuccess ");
        joinAudianceRequest(1);
        //Log.e("LiveStreamAstro ", " onJoinChannelSuccess1 ");
        try {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                   //Log.e("SAN LiveStreamAstro ", " onJoinChannelSuccess2 ");
                    //showHideChatCallUI(true);
                    getChatMessageFromFirebaseDB();
                    getAudioStatus();
                    getVideoStatus();
                    getAstroPvtCallStatus();
                    getPinnedMessage();
                    getAstroCallStatus();
                    getBlockUser();
                }
            }, 500);
        } catch (Exception e) {
            // Log.e("LiveStreamAstro ", " onJoinChannelSuccess exp "+e.toString());
        }
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserOffline(final int uid, int reason) {
        //Log.e("LiveStreamAstro ", " onUserOffline inside ");

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.e("LiveStreamAstro ", " onUserOffline run() " + uid);
                if (uid == Integer.parseInt(liveAstrologerModel.getId())) {
                    //Log.e("LiveStreamAstro ", " onUserOffline uid == liveAstrologerModel.getId()  " + uid);
                    removeRemoteUser(uid);
                    if ( videoCallUid != 0 ) {
                        removeRemoteUser(videoCallUid);
                        videoCallUid = 0;
                    }
                } else if (uid == videoCallUid) {
                    //Log.e("LiveStreamAstro ", " uid == videoCallUid " + uid);
                    removeRemoteUser(uid);
                }
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        //Log.d("TestLiveBlock", "onFirstRemoteVideoDecoded()");
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (uid == Integer.parseInt(liveAstrologerModel.getId())) {
                        renderRemoteUser(uid);
                        //processJoinAudianceSucess();
                        unMuteAtrologerAudio();
                    } else {
                        videoCallUid = uid;
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handleVideoAndAnonymousCall();
                            }
                        }, 500);

                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void handleVideoAndAnonymousCall() {

        if (videoCallUid == 0) return;
        if (callTypeForOtherUser != null && callTypeForOtherUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_VIDEO)) {
            renderRemoteUser(videoCallUid);
        }
    }

    private void renderRemoteUser(int uid) {
        removeRemoteUser(uid);
        try {
            SurfaceView surface = prepareRtcVideo(uid, false);
            mVideoGridContainer.addUserVideoSurface(uid, surface, false);
        }catch (Exception e){
            //
        }
    }

    private void removeRemoteUser(int uid) {
        try {
            removeRtcVideo(uid, false);
            mVideoGridContainer.removeUserVideo(uid, false);
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setWidth(mVideoDimension.width);
        data.setHeight(mVideoDimension.height);
        data.setFramerate(stats.sentFrameRate);
    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        //Log.e("FetchToken", "RtcStats=" + channelName);
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setLastMileDelay(stats.lastmileDelay);
        data.setVideoSendBitrate(stats.txVideoKBitRate);
        data.setVideoRecvBitrate(stats.rxVideoKBitRate);
        data.setAudioSendBitrate(stats.txAudioKBitRate);
        data.setAudioRecvBitrate(stats.rxAudioKBitRate);
        data.setCpuApp(stats.cpuAppUsage);
        data.setCpuTotal(stats.cpuAppUsage);
        data.setSendLoss(stats.txPacketLossRate);
        data.setRecvLoss(stats.rxPacketLossRate);
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        if (!statsManager().isEnabled()) return;

        StatsData data = statsManager().getStatsData(uid);
        if (data == null) return;

        data.setSendQuality(statsManager().qualityToString(txQuality));
        data.setRecvQuality(statsManager().qualityToString(rxQuality));
    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setWidth(stats.width);
        data.setHeight(stats.height);
        data.setFramerate(stats.rendererOutputFrameRate);
        data.setVideoDelay(stats.delay);
    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setAudioNetDelay(stats.networkTransportDelay);
        data.setAudioNetJitter(stats.jitterBufferDelay);
        data.setAudioLoss(stats.audioLossRate);
        data.setAudioQuality(statsManager().qualityToString(stats.quality));
    }

    private void finishLiveSession(String remarks) {
        if (!remarks.equals(CGlobalVariables.FINISH_LIVE_SESSION_OPEN_NEW)) {
            joinAudianceRequest(2);
        }
        updateEndCallStatusInFirebaseDB(0, remarks);
        statsManager().clearAllData();
        removeRtcEventHandler(this);
        rtcEngine().leaveChannel();
        if(rtmClient() != null) {
            unSubscribeRTM();
            rtmClient().removeEventListener(rtmEventListener);
        }
        AstrosageKundliApplication.leaveChannelState = true;
        CUtils.localLiveList.clear();
        finish();
    }

    public void open_layout(View linearLayout) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", linearLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }

    public void close_layout(View linearLayout) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, linearLayout.getHeight());
        animator.setDuration(200L);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                linearLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                linearLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    public void onCallClicked() {
        initAstrologerDescriptionDialog();
        open_layout(mAstrologerDescription);
    }

    public void onLeaveClicked() {

        if (mAstrologerDescription != null) {
            if (mAstrologerDescription.getVisibility() != View.VISIBLE) {
                parseLiveAstrologerList(CUtils.getLiveAstroList());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initLeaveAndFollowDialog();
                        open_layout(leaveLiveSessionsLayout);
                    }
                }, 100);
            }
        }
    }

    public void onSwitchCameraClicked(View view) {
        rtcEngine().switchCamera();
    }

    public void onBeautyClicked(View view) {
        view.setActivated(!view.isActivated());
        rtcEngine().setBeautyEffectOptions(view.isActivated(),
                new BeautyOptions());
    }

    public void onMoreClicked(View view) {
        // Do nothing at the moment
    }

    public void onPushStreamClicked(View view) {
        // Do nothing at the moment
    }

    public void onMuteAudioClicked(View view) {
        if (view.isActivated()) {
            rtcEngine().muteLocalAudioStream(true);
            view.setActivated(false);
        } else {
            rtcEngine().muteLocalAudioStream(false);
            view.setActivated(true);
        }
    }

    public void onMuteVideoClicked(View view) {
        if (view.isActivated()) {
            rtcEngine().muteLocalVideoStream(true);
            view.setActivated(false);
        } else {
            rtcEngine().muteLocalVideoStream(false);
            view.setActivated(true);
        }
    }

    public void onSpeakerClicked(View view) {
        if (view.isActivated()) {
            //enabled speaker
            if (!rtcEngine().isSpeakerphoneEnabled()) {
                rtcEngine().setEnableSpeakerphone(true);
                mEbableSpeakerBtn.setActivated(false);
            }
        } else {
            //desabled speaker
            if (rtcEngine().isSpeakerphoneEnabled()) {
                rtcEngine().setEnableSpeakerphone(false);
                mEbableSpeakerBtn.setActivated(true);
            }
        }
    }

    /*public void onCallStartEnd(View view) {

        if (view.isActivated()) {
            stopAudioCallBroadcast();
        } else {
            startAudioCallBroadcast();
        }
        view.setActivated(!view.isActivated());
    }*/

    private void sendMessage(EditText editText) {
        String messageText = editText.getText().toString().trim();
        String regex = "(\r?\n){5,}";
        messageText = messageText.replaceAll(regex," ");
        //messageText = messageText.replaceAll("(?<!\\.)\\s*\\n\\s*(?!\\.)", " ");
        if (messageText.isEmpty()) {
            return;
        }

        if(!isJoinSuccess || isUserBlocked){
            return;
        }

        if(prevMsg.equals(messageText)){
            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.setFrom(userDisplayName);
            chatMessageModel.setText(messageText+"  "+getString(R.string.no_entry));
            chatMessageModel.setMsgTime(System.currentTimeMillis()/1000);
            chatMessageModel.setTo("");
            updateMessageList(chatMessageModel);
            editText.setText("");
            return;
        }
        //Log.d("fjg",containsWords(msgText,CGlobalVariables.abuseKeyword)+"");
        // Log.d("fjg",containsWordsAhoCorasick(msgText,CGlobalVariables.abuseKeyword)+"");
        String msgTextNew = msgText.replaceAll("\\s", "");
        if((msgTextNew.matches(".*\\d{10}.*"))){
            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.setFrom(userDisplayName);
            chatMessageModel.setText(messageText+"  "+getString(R.string.no_entry));
            chatMessageModel.setMsgTime(System.currentTimeMillis()/1000);
            chatMessageModel.setTo("");
            updateMessageList(chatMessageModel);
            editText.setText("");
            return;
        }
        if(isContain(msgText,CGlobalVariables.abuseKeyword)){
            ChatMessageModel chatMessageModel = new ChatMessageModel();
            chatMessageModel.setFrom(userDisplayName);
            chatMessageModel.setText(messageText+"  "+getString(R.string.no_entry));
            chatMessageModel.setMsgTime(System.currentTimeMillis()/1000);
            chatMessageModel.setTo("");
            updateMessageList(chatMessageModel);
            editText.setText("");
            return;
        }
        prevMsg = messageText;
        sendMsgToFirebase(messageText);
        editText.setText("");
        msgText = "";
        // clearTextInput();
    }

    public void sendMsgToFirebase(String message) {
        try {
            String channelId = channelName;
            if (TextUtils.isEmpty(channelId)) return;
            DatabaseReference chatDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId);

            Map chatMap = new HashMap();
            chatMap.put("From", userDisplayName);
            chatMap.put("Text", message);
            chatMap.put("MsgTime", ServerValue.TIMESTAMP);
            chatMap.put("UserId", CUtils.getUserIdForBlock(currentActivity));
            chatMap.put("Type", "M");
            String userId = (String) chatMap.get("UserId");
            if(userId != null && userId.equals(CUtils.getUserIdForBlock(currentActivity))){
            chatDBRef.child(CGlobalVariables.MESSAGES_FBD_KEY).push().setValue(chatMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearTextInput() {
        messageTextEdit.setText("");
    }

    private void getChatMessageFromFirebaseDB() {
        isLoadChatFirstTime = true;
        try {
            //Log.e("SAN ListFlatuate ", " getChatMessageFromFirebaseDB() ");

            String channelId = channelName;
            if (TextUtils.isEmpty(channelId)) return;
            if (chatDBRef != null && chatValueEventListener != null) {
                chatDBRef.removeEventListener(chatValueEventListener);
            }
            chatValueEventListener = (new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        if (dataSnapshot != null) {
                            //Log.e("FirebaseRealTimeDB", dataSnapshot.exists() + " " + channelName);
                            ChatMessageModel chat = dataSnapshot.getValue(ChatMessageModel.class);
                            String chatType = chat.getType();
                            if(chatType == null){
                                chatType = "";
                            }
                            if(!chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_UNFOLLOW)) {
                                updateMessageList(chat);
                            }
                            updateFollowCount(chatType);
                            if(!isLoadChatFirstTime) {
                                displayAndnimateGiftMessage(chat);
                            }
                            //Log.e("FirebaseRealTimeDB",  " size=" + chatMessageArrayList.size());
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            chatDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + CGlobalVariables.MESSAGES_FBD_KEY);
            chatDBRef.limitToLast(10).addChildEventListener(chatValueEventListener);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoadChatFirstTime = false;
                    //Log.e("ChatMessage", "onChildAdded()2 = "+isLoadChatFirstTime);
                }
            }, 4000);
        } catch (Exception ex) {
            //
        }
    }

    private void updateMessageList(ChatMessageModel chat){
        try {
            if (chatMessageArrayList.size() >= 60) {
                chatMessageArrayList.remove(chatMessageArrayList.size() - 1);
            }
        } catch (Exception e) {
            //
        }

        try {
            chatMessageArrayList.add(0, chat);
            liveStreamMsgDetailAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            //
        }
    }

    private void isEnabledFollowBtn(boolean isEnabled){
        if(isEnabled){
            followTxt.setEnabled(true);
            followTxt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_orange_min));
        }else {
            followTxt.setEnabled(false);
            followTxt.setBackground(ContextCompat.getDrawable(this, R.drawable.light_gray_rect_less_rounded));
        }
    }

    public void followAstrologerRequest(String followVal) {
        if(followVal !=  null && followVal.equals("1")){
            CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST,"follow_astrologer","LiveActivityNew");
        }
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            isEnabledFollowBtn(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.followAstrologer(getfollowAstrologerParams(followVal));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        isEnabledFollowBtn(true);
                        parseFollowAstrologerStatus(myResponse, followVal);
                    } catch (Exception e) {
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

        } else {
            //CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), currentActivity);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }
    public Map<String, String> getfollowAstrologerParams(String followVal) {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(currentActivity));
        headers.put(CGlobalVariables.ASTROLOGER_ID, liveAstrologerModel.getId());
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(currentActivity));
        headers.put(CGlobalVariables.KEY_FOLLOW, followVal);
        headers.put(CGlobalVariables.KEY_IAPI, "1");
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(currentActivity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.KEY_USERNAME, CUtils.getUserDisplayName(currentActivity));
        headers.put(CGlobalVariables.ACTIONSOURCE, CUtils.getActivityName(currentActivity));
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        return headers;
    }

    public void joinAudianceRequest(int from) {
        //Log.e("SAN LiveStreamAstro ", " joinAudianceRequest() 1");
        if (CUtils.isConnectedWithInternet(currentActivity)) {

            /*String url = CGlobalVariables.JOIN_AUDIANCE_URL;
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    LiveActivityNew.this, false, getJoinAudianceParams(from), JOIN_AUDIANCE_REQ).getMyStringRequest();
            //Log.e("LiveStreamAstro ", " joinAudianceRequest() 2.1 stringRequest =>  " + stringRequest);
            stringRequest.setShouldCache(true);
            if (queue == null)
                queue = VolleySingleton.getInstance(currentActivity).getRequestQueue();
            queue.add(stringRequest);
            */

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.liveJoinAudiance(getJoinAudianceParams(from));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        userSpentTime = System.currentTimeMillis();
                        processJoinAudianceResponce(myResponse,from);

                    } catch (Exception e) {
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

        } else {
            //CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), currentActivity);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> getJoinAudianceParams(int from) {
        HashMap<String, String> headers = new HashMap<String, String>();

        if (from == 1) {
            headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
            headers.put(CGlobalVariables.KEY_NAME, CUtils.getUserDisplayName(currentActivity));
            headers.put(CGlobalVariables.ASTROLOGER_ID, liveAstrologerModel.getId());
            boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
            try {
                if (isLogin) {
                    headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(currentActivity));
                    headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
                }
            } catch (Exception e) {
            }
            headers.put("agorauid", agoraUid + "");
            if (!preUserSpentTime.isEmpty()) {
                headers.put("prechannelname", preChannelName);
                headers.put("preastrologerid", preAstrologerId);
                headers.put("userspenttime", preUserSpentTime);
            }
        } else {
            headers.put(CGlobalVariables.CHANNEL_NAME, "");
            headers.put(CGlobalVariables.KEY_NAME, "");
            headers.put(CGlobalVariables.ASTROLOGER_ID, "");
            headers.put("prechannelname", channelName);
            headers.put("preastrologerid", liveAstrologerModel.getId());
            userSpentTime = System.currentTimeMillis() - userSpentTime;
            headers.put("userspenttime", String.valueOf(userSpentTime/1000));
        }
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(currentActivity));
        if (!CUtils.getUserLoginStatus(currentActivity)) {
            if(TextUtils.isEmpty(userDisplayName)) {
                userDisplayName = CUtils.getUserDisplayName(this);
            }
            headers.put(CGlobalVariables.KEY_USER_ID, userDisplayName);
        }
        liveUserId = headers.get(CGlobalVariables.KEY_USER_ID);
        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(currentActivity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(currentActivity));
        return headers;
    }
//name=Guest0212, astrologerid=45, channelname=LCH_45_1657863272092, userid=91, key=-1489918760
    /*public Map<String, String> getJoinAudianceParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(currentActivity));
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        headers.put("name", CUtils.getUserDisplayName(currentActivity));
        headers.put("agorauserid", String.valueOf(agoraUid));
        headers.put("userid", CUtils.getAppUserId(this));
        headers.put("logmsg", CUtils.getAudioMode(this));
        //Log.e("LiveStreamAstro ", " params join " + headers.toString());
        return headers;
    }*/


    public void connectCallRequest(UserProfileData userProfileDataBean) {

        if (CUtils.isConnectedWithInternet(currentActivity)) {
            call_astrologer_btn.setClickable(true);
            close_layout(mAstrologerDescription);
            llAlr1.setVisibility(View.VISIBLE);
            myCallingState = CGlobalVariables.CALL_STATE_INITIATED;

            /*String url = CGlobalVariables.CONNECT_LIVE_CALL_URL;
            //Log.e("ER100 ", " URL=>" + url);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    LiveActivityNew.this, false, getConnectCallParams(userProfileDataBean), CONNECT_LIVE_CALL_REQ).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.liveConnectLiveCall(getConnectCallParams(userProfileDataBean));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        llAlr1.setVisibility(View.GONE);
                        processConnectCallResponce(myResponse);
                    } catch (Exception e) {
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

            CUtils.fcmAnalyticsEvents("connect_live_call_api_called",AstrosageKundliApplication.currentEventType,"");

        } else {
            CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), currentActivity);
        }

    }

    public Map<String, String> getConnectCallParams(UserProfileData userProfileDataBean) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(currentActivity));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
            } else {
                headers.put(CGlobalVariables.USER_PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(KEY_PASSWORD, CUtils.getUserLoginPassword(currentActivity));
        headers.put(CGlobalVariables.URL_TEXT, liveAstrologerModel.getUrltext());
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID,CUtils.getMyAndroidId(currentActivity));
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(currentActivity));
        headers.put("type", callTypeForCallingUser);
        if (userProfileDataBean != null && userProfileDataBean.isProfileSendToAstrologer()) {
            headers.put("isprofilepresent", "1");
            headers.put("isshowbirthdetails", "1");
            headers.put("name", userProfileDataBean.getName());
            headers.put("gender", userProfileDataBean.getGender());
            headers.put("place", userProfileDataBean.getPlace());
            headers.put("day", userProfileDataBean.getDay());
            headers.put("month", userProfileDataBean.getMonth());
            headers.put("year", userProfileDataBean.getYear());
            headers.put("hour", userProfileDataBean.getHour());
            headers.put("minute", userProfileDataBean.getMinute());
            headers.put("second", userProfileDataBean.getSecond());
            headers.put("longdeg", userProfileDataBean.getLongdeg());
            headers.put("longmin", userProfileDataBean.getLongmin());
            headers.put("longew", userProfileDataBean.getLongew());
            headers.put("latmin", userProfileDataBean.getLatmin());
            headers.put("latdeg", userProfileDataBean.getLatdeg());
            headers.put("latns", userProfileDataBean.getLatns());
            headers.put("timezone", userProfileDataBean.getTimezone());
            headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
            headers.put("occupation", userProfileDataBean.getOccupation());
        } else {
            headers.put("isprofilepresent", "0");
            headers.put("isshowbirthdetails", "0");
        }
        //send sfc
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(currentActivity));
        //Log.e("ER100 ", " params=>" + headers.toString());
        //Log.d("MyTestingData"," params=>" + headers.toString());
        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
       // Log.d("MyTestingData ","response == "+response+"+ method == "+method);
        hideProgressBar();
//        Log.e("LiveStreamAstro", method + " response=" + response);
        if (method == JOIN_AUDIANCE_REQ) {
           // Log.d("MyTestingData ","JOIN_AUDIANCE_REQ called");

            userSpentTime = System.currentTimeMillis();
            processJoinAudianceResponce(response,-1);
        } else if (method == CONNECT_LIVE_CALL_REQ) {

            llAlr1.setVisibility(View.GONE);
            processConnectCallResponce(response);
        } else if (method == ASTRO_STATUS_UPDATE) {
            parseAstrologerStatus(response);
        } else if (method == FETCH_LIVE_ASTROLOGER) {
            try {
                CUtils.setApiLastHitTime();
               // Log.d("SANLANTest ", method + " Live Astro response=" + response);
                CUtils.saveLiveAstroList(response);
                if(getAstroListFromCloseBtn){
                    getAstroListFromCloseBtn = false;
                    onLeaveClicked();
                }
                if(getAstroListFromEndLiveSession){
                    getAstroListFromEndLiveSession = false;
                    parseLiveAstrologerList(CUtils.getLiveAstroList());
                }else {
                    onLeaveClicked();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ( method == SEND_GIFT_REQ ) {
            try {
                //Log.e("SAN ", " gift res " + response );
                if(btnSendGift != null){
                    btnSendGift.setEnabled(true);
                }
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if ( status.equals("1") ) {

                    updateWalletBalance();

                    makeGiftUnSelected();
                    //CUtils.showSnackbar(giftLayout, getResources().getString(R.string.gift_sucessful), LiveActivityNew.this);
                    close_layout(giftLayout);
                    sendMessageLL.setVisibility(View.VISIBLE);
                } else {
                    CUtils.showSnackbar(giftLayout, getResources().getString(R.string.gift_unsucessful)+" ("+status+")", LiveActivityNew.this);
                }
            } catch (Exception e){
                CUtils.showSnackbar(giftLayout, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
            }

        } else if (method == PRIVATE_CALL) {
            try {

                close_layout(mAstrologerDescription);
                call_astrologer_btn.setClickable(true);

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                try {
                    if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                        freeMinutedialog.dismiss();
                    }
                }catch (Exception e){
                    //
                }
                if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                    final String callsId = jsonObject.getString("callsid");
                    String talkTime = jsonObject.getString("talktime");
                    final String exophoneNo = jsonObject.getString("exophone");
                    final String internationalCharges = jsonObject.getString("callcharge");

                    CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog",AstrosageKundliApplication.currentEventType,"");
                    openFreeMinDialogBox();
                    String astroName = "",profileUrl ="";
                    if(!TextUtils.isEmpty(astrologerDetailBeanData.getName())){
                        astroName = astrologerDetailBeanData.getName();
                        profileUrl = astrologerDetailBeanData.getImageFile();
                    }

                    CUtils.fcmAnalyticsEvents("show_call_initiate_dialog",AstrosageKundliApplication.currentEventType,"");

                    // Free offer taken event
                    /*
                    String offerType = CUtils.getCallChatOfferType(this);
                    if (liveAstrologerModel != null && liveAstrologerModel.isPrivateIntroOffer() && !TextUtils.isEmpty(offerType)) {
                        if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                            CUtils.fcmAnalyticsEvents(FREE_OFFER_TAKEN,CALL_CLICK,"");
                            CUtils.saveFirstFreeOfferTakenDate(LiveActivityNew.this, DateTimeUtils.currentDate());
                        }else {
                            CUtils.fcmAnalyticsEvents(REDUCE_PRICE_OFFER_TAKEN,CALL_CLICK,"");
                            CUtils.saveReducePriceOfferTakenDate(LiveActivityNew.this, DateTimeUtils.currentDate());
                        }
                    }*/

                    CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CGlobalVariables.CALL_CLICK, internationalCharges,astroName,profileUrl);
                    dialog.show(getSupportFragmentManager(), "Dialog");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CUtils.fcmAnalyticsEvents("start_call_status_service",CGlobalVariables.SERVICE_CALL_EVENT,"");

                                Intent intent = new Intent(LiveActivityNew.this, CallStatusCheckService.class);
                                intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                                currentActivity.startService(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 60000);

                } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                    CUtils.fcmAnalyticsEvents("call_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                    String dialogMsg = getResources().getString(R.string.unable_to_connect_free_consultation);
                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                    CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog",AstrosageKundliApplication.currentEventType,"");

                    String astrologerName = jsonObject.getString("astrologername");
                    String userbalance = jsonObject.getString("userbalance");
                    String minBalance = jsonObject.getString("minbalance");

                    InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CGlobalVariables.CALL_CLICK);
                    dialog.show(getSupportFragmentManager(), "Dialog");

                } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                    CUtils.fcmAnalyticsEvents("call_astrologer_busy_dialog",AstrosageKundliApplication.currentEventType,"");
                    String dialogMsg = "";
                    if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                        dialogMsg = getResources().getString(R.string.user_blocked_msg);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                        dialogMsg = getResources().getString(R.string.astrologer_busy_msg);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                        dialogMsg = getResources().getString(R.string.astrologer_offline_msg);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                        dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                        dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                        dialogMsg = getResources().getString(R.string.existing_call_msg);
                    } else {
                        dialogMsg = getResources().getString(R.string.something_wrong_error);
                    }

                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CALL_CLICK);
                }
            } catch (Exception e) {}
        }else if(method == END_LIVE_CALL_REQ){
           // Log.d("liveCallEnd ", "response-->" + response);
        }
    }

    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {
        try {
            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            dialog.show(getSupportFragmentManager(), "Dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFreeMinDialogBox() {
        try {
            if (freeMinuteMinimizeDialog == null) {
                freeMinuteMinimizeDialog = new FreeMinuteMinimizeDialog();
            }
            freeMinuteMinimizeDialog.show(getSupportFragmentManager(), "MiniDialog");

        } catch (Exception e) {
            //Log.e("SAN ADA", " openFreeMinDialogBox exp " + e.toString());
        }
    }

    public void popUpOverPopUp() {
        try {

            if (freeMinuteMinimizeDialog != null && freeMinuteMinimizeDialog.isVisible()) {
                freeMinuteMinimizeDialog.dismiss();
                freeMinutedialog = new FreeMinuteDialog("");
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
            }
        } catch (Exception e) {

        }

    }



    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        isEnabledFollowBtn(true);
        if(getAstroListFromEndLiveSession){
            getAstroListFromEndLiveSession = false;
        }else {
            onLeaveClicked();
        }

        if (getAstroListFromCloseBtn) {
            getAstroListFromCloseBtn = false;
            onLeaveClicked();
        }
        //Log.e("LiveStreamAstro", " onError =" + error.toString());
        llAlr1.setVisibility(View.GONE);
        myCallingState = CGlobalVariables.CALL_STATE_DISCONNECTED;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (chatDBRef != null && chatValueEventListener != null) {
                chatDBRef.removeEventListener(chatValueEventListener);
            }

            if (runningCallsIdDbRef != null && runningCallsIdEventListener != null) {
                runningCallsIdDbRef.removeEventListener(runningCallsIdEventListener);
            }

            if (runningCallsIdDbRefPrivateCall != null && runningCallsIdEventListenerPrivateCall != null) {
                runningCallsIdDbRefPrivateCall.removeEventListener(runningCallsIdEventListenerPrivateCall);
            }

            if (endLiveSesionDbRef != null && endLiveSesionEventListner != null) {
                endLiveSesionDbRef.removeEventListener(endLiveSesionEventListner);
            }
            if (audioDBRef != null && audioEventListener != null) {
                audioDBRef.removeEventListener(audioEventListener);
            }
            if (userBlockDBRef != null && userBlockEventListener != null) {
                userBlockDBRef.removeEventListener(userBlockEventListener);
            }
            if (videoDBRef != null && videoEventListener != null) {
                videoDBRef.removeEventListener(videoEventListener);
            }
            if (pinMessageDBRef != null && pinMessageEventListener != null) {
                pinMessageDBRef.removeEventListener(pinMessageEventListener);
            }
            if (astroPvtCallDBRef != null && astroPvtCallEventListener != null) {
                astroPvtCallDBRef.removeEventListener(astroPvtCallEventListener);
            }

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            if (countDownTimerOtherUser != null) {
                countDownTimerOtherUser.cancel();
            }

            unRegisterConnectivityStatusReceiver();
            if (mReceiverLiveOpenKundli != null) {
                LocalBroadcastManager.getInstance(LiveActivityNew.this).unregisterReceiver(mReceiverLiveOpenKundli);
            }
            if (openLiveScreenFromNotification != null) {
                LocalBroadcastManager.getInstance(LiveActivityNew.this).unregisterReceiver(openLiveScreenFromNotification);
            }

            LocalBroadcastManager.getInstance(LiveActivityNew.this).unregisterReceiver(endLiveBroadcastReceiver);

        } catch (Exception e) {
            //
        }
    }

    private void processJoinAudianceResponce(String response,int from) {
        try {
           //Log.e("SAN ListFlatuate ", " processJoinAudianceResponce() response=> " + response );
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            isJoinSuccess = false;
            if (status.equals(CGlobalVariables.STATUS_SUCESS)) {
                isJoinSuccess = true;
                currentTimeStamp = jsonObject.getLong("currenttimestamp");
                followCountVal = jsonObject.getString("astrofollowcount");
                followStatus = jsonObject.getString("followbyuser");
                String rowToken = jsonObject.optString("rtmtoken");
                if(from == 1) {
                    String rtmToken = URLDecoder.decode(rowToken, "UTF-8");
                    //Log.e("LIveCount", "c=" + rtmToken);
                    loginRtmSDK(rtmToken,liveUserId);
                }

                followCount.setText(followCountVal);
                live_btn_share.setVisibility(View.VISIBLE);
                if(followStatus == null || followStatus.equals("0"))
                {
                    followTxt.setText(getResources().getString(R.string.follow));
                    CUtils.unSubscribeFollowTopic(currentActivity, liveAstrologerModel.getId());
                    try {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openFollowDialog();
                            }
                        }, CGlobalVariables.SHOW_FOLLOW_DIALOG_TIME_MS);
                    } catch (Exception e) {
                    }
                }
                else
                {
                    followTxt.setText(getResources().getString(R.string.following));
                    CUtils.subscribeFollowTopic(currentActivity, liveAstrologerModel.getId());
                }
            } else if (status.equals("0")) {
                live_btn_share.setVisibility(View.GONE);
                getAstroListFromEndLiveSession = true;
                getLiveAstrologerDataFromServer();
                initAstroNotLive();
                open_layout(mAstroNotLive);
            } else if (status.equals("2")) {
                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error) + " (" + status + ")", currentActivity);
                isUserBlocked = true;
                showHideChatCallUI(false);
            } else if (status.equals("100")) {
                boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
                if (isLogin) {
                    isStatus100 = true;
                    LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginServiceJoinAudience
                            , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                    startBackgroundLoginService();
                } else {
                    openLoginScreen();
                }
            }else {
                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error) + " (" + status + ")", currentActivity);
            }

            if (isJoinSuccess) {
                //Log.d("TestLiveBlock", "isJoinSuccess()");
                processJoinAudianceSucess();
            } else {
                if (runningCallsIdDbRef != null && runningCallsIdEventListener != null) {
                    runningCallsIdDbRef.removeEventListener(runningCallsIdEventListener);
                }
                if (runningCallsIdDbRefPrivateCall != null && runningCallsIdEventListenerPrivateCall != null) {
                    runningCallsIdDbRefPrivateCall.removeEventListener(runningCallsIdEventListenerPrivateCall);
                }
            }
        } catch (Exception e) {
            //
        }
    }

    private void processJoinAudianceSucess() {
        //Log.d("TestLiveBlock", "processJoinAudianceSucess()");
        showHideChatCallUI(true);
        listenEndLiveSessionFirebaseDB();
        listenConnectCallFirebaseDB();
        listenConnectPrivateCallFirebaseDB();
    }

    private void processConnectCallResponce(String response) {
        //Log.d("joinLiveAud",response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            String status = jsonObject.getString("status");
            myCallingState = CGlobalVariables.CALL_STATE_DISCONNECTED;
            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                myCallingState = CGlobalVariables.CALL_STATE_CONNECTED;
                callsId = jsonObject.getString("callsid");
                String newToken = URLDecoder.decode(jsonObject.getString("token"), "UTF-8");
                String callDurationInSecs = jsonObject.getString("durationinsecs");
                long longSecTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(callDurationInSecs));
                startAudioCallBroadcast(newToken);
                initCountDownTimerForCallingUser(longSecTime);
                setOnDisconnectListner();
                listenEndCallFirebaseDB();
                registerConnectivityStatusReceiver();
                hideShowNextPrev(false);
                CUtils.fcmAnalyticsEvents("live_call_initiated",AstrosageKundliApplication.currentEventType,"");

                //sendLogDataRequest("ConnectCall");
            } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                CUtils.fcmAnalyticsEvents("live_call_insufficient_bal_dialog",AstrosageKundliApplication.currentEventType,"");

                String userbalance = jsonObject.getString("userbalance");
                ll_desc_join_queue.setVisibility(View.GONE);
                descRecharge.setVisibility(View.VISIBLE);
                descWalletBalanceTxt.setVisibility(View.VISIBLE);
                descWalletAmount.setText(getResources().getString(R.string.rs_sign) + userbalance);
                open_layout(mAstrologerDescription);

            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                CUtils.fcmAnalyticsEvents("live_call_astrologer_busy_dialog",AstrosageKundliApplication.currentEventType,"");
                String message = "";
                try {
                    message = jsonObject.getString("msg");
                }catch (Exception e){
                    message = "";
                }
                String dialogMsg = "";
                if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                    dialogMsg = getResources().getString(R.string.user_blocked_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                    dialogMsg = getResources().getString(R.string.astrologer_busy_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                    dialogMsg = getResources().getString(R.string.astrologer_offline_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                    dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                    dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                    dialogMsg = getResources().getString(R.string.existing_call_msg);
                } else {
                    dialogMsg = getResources().getString(R.string.something_wrong_error);
                }

                CUtils.showSnackbar(messageTextEdit, dialogMsg, currentActivity);
            } else if (status.equals("100")) {
                CUtils.fcmAnalyticsEvents("live_call_astrologer_status_100",AstrosageKundliApplication.currentEventType,"");

                LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService
                        , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                startBackgroundLoginService();
            } else {
                CUtils.fcmAnalyticsEvents("live_call_astrologer_error",AstrosageKundliApplication.currentEventType,"");

                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error) + " (" + status + ")", currentActivity);
            }
        } catch (Exception e) {
            //
        }
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(this)) {
                //Log.e("ER100 ", " startBackgroundLoginService() ");
                CUtils.fcmAnalyticsEvents("bg_login_from_live_activity",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");
                Intent intent = new Intent(LiveActivityNew.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        Log.e("TestPIP", "onStop onPictureInPictureModeChanged="+isInPictureInPictureMode);
        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiverBackgroundLoginService);
        }
        if (isInPictureInPictureMode){
            CUtils.isPipOpenInHoroscope = false;
            finishAndRemoveTask();
        }
        super.onStop();
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                connectCallRequest(userProfileDataBeanGlobal);
            } else {
                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(LiveActivityNew.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    private void hideShowNextPrev(boolean showHide) {

        if (showHide) {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
        }
    }

    public void initCountDownTimerForCallingUser(long timeDuretion) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        llAlr2.setVisibility(View.VISIBLE);
        mMuteAudioBtn.setVisibility(View.VISIBLE);
        callDisconnect.setVisibility(View.VISIBLE);
        call_astrologer_btn.setClickable(false);
        String userNameStr = CUtils.getUserDisplayName(currentActivity);
        callingUserName.setText(userNameStr);

        if (callTypeForCallingUser != null && callTypeForCallingUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_VIDEO)) {
            mMuteVideoBtn.setVisibility(View.VISIBLE);
            mMuteVideoBtn.setActivated(true);
        }

        countDownTimer = new CountDownTimer(timeDuretion, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {

                String text = String.format(java.util.Locale.US, "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                                TimeUnit.MILLISECONDS.toDays(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                tvTimeDown.setText(text);

            }

            @Override
            public void onFinish() {
                try {
                    updateEndCallStatusInFirebaseDB(0, CGlobalVariables.TIME_OVER);
                    CUtils.fcmAnalyticsEvents("live_call_end_time_over",AstrosageKundliApplication.currentEventType,"");
                    processEndCallForCallingUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void initCountDownTimerForOtherUser(long timeDuretion, String userName) {

        if (countDownTimerOtherUser != null) {
            countDownTimerOtherUser.cancel();
        }

        otherUserName.setText(userName);
        if ( callTypeForOtherUser != null && callTypeForOtherUser.equalsIgnoreCase(CGlobalVariables.LIVE_STREAMING_KEY_PRIVATE_CALL) ) {
            textPrivateCallInfo.setVisibility(View.GONE);
            textPrivateCallInfo.setTag(false);
        }
        countDownTimerOtherUser = new CountDownTimer(timeDuretion, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {

                String text = String.format(java.util.Locale.US, "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                                TimeUnit.MILLISECONDS.toDays(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                tvTimeDownOtherUser.setText(text);

                if(TextUtils.isEmpty(text)){
                    llAlrOtherUser.setVisibility(View.GONE);
                    llAlrOtherUser.setTag(false);
                } else {
                    if (isCallTimerLayoutShown) {
                        if (!isInPictureInPictureMode){
                            llAlrOtherUser.setVisibility(View.VISIBLE);
                        }
                        llAlrOtherUser.setTag(true);
                    }
                }

            }

            @Override
            public void onFinish() {
                processEndCallForOtherUser();
            }
        }.start();
    }

    private void processEndCallForOtherUser() {

        if (countDownTimerOtherUser != null) {
            countDownTimerOtherUser.cancel();
        }
        callTypeForOtherUser = "";
        llAlrOtherUser.setVisibility(View.GONE);
        llAlrOtherUser.setTag(false);

        if ( videoCallUid != 0 ) {
            removeRemoteUser(videoCallUid);
            videoCallUid = 0;
        }
        isPvtCallTimerDisplayed = false;

    }

    private void processEndCallForCallingUser() {
        try {
            if ( videoCallUid != 0 ) {
                removeRemoteUser(videoCallUid);
                videoCallUid = 0;
            }
            //sendLogDataRequest("EndCall");
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (runningCallsIdDbRef != null) {
                runningCallsIdDbRef.setValue("NA");
            }

            if (runningCallsIdDbRefPrivateCall != null) {
                runningCallsIdDbRefPrivateCall.setValue("NA");
            }

            llAlr2.setVisibility(View.GONE);
            mMuteAudioBtn.setVisibility(View.GONE);
            mMuteVideoBtn.setVisibility(View.GONE);
            call_astrologer_btn.setClickable(true);
            changeViewState(currentPos);
            stopAudioCallBroadcast();
            cancelOnDisconnectListner();
            unRegisterConnectivityStatusReceiver();
            myCallingState = CGlobalVariables.CALL_STATE_DISCONNECTED;
            callsId = "";
            if (CUtils.isLiveintrooffer(this)) {
                CUtils.setUserOffers(this, false, CUtils.getCallChatOfferType(this));
                initCallPrice();
            }
        } catch (Exception e) {
            //
        }
    }


    private DatabaseReference getCallsIdDbRefrance() {
        try {
            if (TextUtils.isEmpty(channelName) || TextUtils.isEmpty(callsId)) return null;
            DatabaseReference callIdDbRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelName + "/" + CGlobalVariables.LIVE_STREAMING_CALLS_NODE + "/" + callsId);
            return callIdDbRef;
        } catch (Exception e) {
            return null;
        }
    }

    private void updateEndCallStatusInFirebaseDB(int statusForEndCallApi, String remarks) {
        try {
            DatabaseReference callIdDbRef = getCallsIdDbRefrance();
            if (callIdDbRef != null) {
                callIdDbRef.child(CGlobalVariables.LIVE_STREAMING_END_CALL_NODE).setValue(true);
                callIdDbRef.child("EndTime").setValue(ServerValue.TIMESTAMP);
                callIdDbRef.child(CGlobalVariables.END_CALL_SATATUS_FBD_KEY).setValue(remarks);
            }
            if(statusForEndCallApi==1){
                requestEndCallDuringLive(remarks);
            }
        } catch (Exception e) {
            //
        }
    }

    public void requestEndCallDuringLive(String remarks) {
        if (CUtils.isConnectedWithInternet(currentActivity)) {

            /*String url = CGlobalVariables.END_LIVE_CALL_URL;
            //Log.e("ER100 ", " URL=>" + url);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    LiveActivityNew.this, false, getEndCallParams(remarks), END_LIVE_CALL_REQ).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.liveEndLiveCall(getEndCallParams(remarks));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                    } catch (Exception e) {
                        CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

        } else {
            CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), currentActivity);
        }
    }

    public Map<String, String> getEndCallParams(String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(currentActivity));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
            } else {
                headers.put(CGlobalVariables.USER_PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        //headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        //headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        headers.put(CGlobalVariables.CALLS_ID,callsId);
        headers.put(CGlobalVariables.ASTROLOGER_ID,liveAstrologerModel.getId());
        headers.put(CGlobalVariables.REMARKS, remarks);

        return CUtils.setRequiredParams(headers);
    }

    private void setOnDisconnectListner() {
        try {
            DatabaseReference callIdDbRef = getCallsIdDbRefrance();
            if (callIdDbRef != null) {
                callIdDbRef.child("UserDisconnectTime").onDisconnect().setValue(ServerValue.TIMESTAMP);
            }

        } catch (Exception e) {
            //
        }
    }

    private void cancelOnDisconnectListner() {
        try {
            DatabaseReference callIdDbRef = getCallsIdDbRefrance();
            if (callIdDbRef != null) {
                callIdDbRef.child("UserDisconnectTime").onDisconnect().cancel();
            }
        } catch (Exception e) {
            //
        }
    }

    private void registerConnectivityStatusReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            currentActivity.registerReceiver(connectivityStatusReceiver, intentFilter);
        } catch (Exception e) {
            //
        }
    }

    private void unRegisterConnectivityStatusReceiver() {
        try {
            if (connectivityStatusReceiver != null) {
                currentActivity.unregisterReceiver(connectivityStatusReceiver);
            }
        } catch (Exception e) {
            //
        }
    }

    private void setConnectivityRegainInFirebase() {
        try {
            DatabaseReference callIdDbRef = getCallsIdDbRefrance();
            if (callIdDbRef != null) {
                callIdDbRef.child("UserConnRegainTime").setValue(ServerValue.TIMESTAMP);
            }

        } catch (Exception e) {
            //
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

    private void listenEndCallFirebaseDB() {
        try {
            callsIdEndCallDbRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelName + "/" + CGlobalVariables.LIVE_STREAMING_CALLS_NODE + "/" + callsId + "/" + CGlobalVariables.LIVE_STREAMING_END_CALL_NODE);
            callsIdEndCallDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        boolean userEndCall = (boolean) dataSnapshot.getValue();
                        if (userEndCall) {
                            if (callsIdEndCallDbRef != null) {
                                callsIdEndCallDbRef.removeEventListener(this);
                            }
                            CUtils.fcmAnalyticsEvents("live_call_end",AstrosageKundliApplication.currentEventType,"");

                            processEndCallForCallingUser();
                            if (mConsultationEndMsg != null && mConsultationEndMsg.getVisibility() == View.VISIBLE) {
                                // do nothing
                            } else {
                                if (dialogMsg != null)
                                    dialogMsg.dismiss();
                                clMuteInfo.setVisibility(View.GONE);
                                clMuteInfo.setTag(false);
                                textAstroPinned.setText("");

                                if (liveAstrologerModel != null) {
                                    getAstrologerStatusPrice(liveAstrologerModel.getId());
                                }
                                //initEndConsultationMsg();
                                //open_layout(mConsultationEndMsg);
                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            //
        }
    }


    private void listenEndLiveSessionFirebaseDB() {
        try {

            if (endLiveSesionDbRef != null && endLiveSesionEventListner != null) {
                endLiveSesionDbRef.removeEventListener(endLiveSesionEventListner);
            }

            endLiveSesionEventListner = (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {

                        //Log.e("LiveStreamAstro ", " EndLive Listner call ");

                        boolean astroEndLive = (boolean) dataSnapshot.getValue();

                        //Log.e("LiveStreamAstro ", " EndLive Listner astroEndLive => " + astroEndLive );

                        if (astroEndLive) {

                            if (endLiveSesionDbRef != null) {
                                endLiveSesionDbRef.removeEventListener(this);
                            }
                            if (dialogMsg != null)
                                dialogMsg.dismiss();
                            showHideChatCallUI(false);
                            initEndConsultationMsgAstro();
                            open_layout(mEndConsultationFromAstro);
                            clMuteInfo.setVisibility(View.GONE);
                            clMuteInfo.setTag(false);

                            if ( videoCallUid != 0 ) {
                                removeRemoteUser(videoCallUid);
                                videoCallUid = 0;
                            }

                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            endLiveSesionDbRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelName + "/" + CGlobalVariables.LIVE_STREAMING_END_LIVE_NODE);
            endLiveSesionDbRef.addValueEventListener(endLiveSesionEventListner);
        } catch (Exception e) {
            //
        }
    }

    private void openProfileActivity() {
        try {
            /*Intent i = new Intent(currentActivity, SavedKundliListActivity.class);
            i.putExtra("fromWhere", "");
            startActivityForResult(i, DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);*/
            CUtils.openProfileOrKundliAct(this,liveAstrologerModel.getUrltext(),"", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
        } catch (Exception e) {
        }
    }

    private void openProfileActivityForPrivateCall(){
        /*Intent i = new Intent(LiveActivityNew.this, SavedKundliListActivity.class);
        i.putExtra("phoneNo", "1");
        i.putExtra("urlText", liveAstrologerModel.getUrltext());
        i.putExtra("fromWhere", "");
        startActivityForResult(i, BACK_FROM_PROFILECHATDIALOG_PRIVATE_CALL);*/
        CUtils.openProfileOrKundliAct(this,liveAstrologerModel.getUrltext(),"", BACK_FROM_PROFILECHATDIALOG_PRIVATE_CALL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DashBoardActivity.BACK_FROM_PROFILECHATDIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                if (isProceed) {
                    userProfileDataBeanGlobal = (UserProfileData) data.getExtras().get("USER_DETAIL");
                    connectCallRequest(userProfileDataBeanGlobal);
                } else if (data.getExtras().containsKey("openKundliList")){
                    CUtils.openSavedKundliList(this,liveAstrologerModel.getUrltext(),"call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                } else if (data.getExtras().containsKey("openProfileForChat")){
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")){
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChat(this, liveAstrologerModel.getUrltext(),"call", bundle, prefillData,DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                }
            }

        } else if (requestCode == BACK_FROM_PROFILECHATDIALOG_PRIVATE_CALL) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                if (isProceed) {
                    String phoneNo = data.getStringExtra("phoneNo");
                    String urlText = data.getStringExtra("urlText");
                    callToAstrologer(phoneNo, urlText);
                } else if (data.getExtras().containsKey("openKundliList")){
                    CUtils.openSavedKundliList(this,liveAstrologerModel.getUrltext(),"call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                } else if (data.getExtras().containsKey("openProfileForChat")){
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")){
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChat(this, liveAstrologerModel.getUrltext(),"call", bundle, prefillData,DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                }
            }
        }
    }

    public void callToAstrologer(String phoneNo, String urlText) {
        showFreeOneMinDialog();
        callSelectedAstrologer(phoneNo, urlText);
    }
    private void showFreeOneMinDialog() {
        try {
            String offerType = CUtils.getCallChatOfferType(this);
            if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)&& CUtils.getCountryCode(LiveActivityNew.this).equals("91")) {
                freeMinutedialog = new FreeMinuteDialog(CGlobalVariables.INTRO_OFFER_TYPE_FREE);
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
                CUtils.fcmAnalyticsEvents("first_min_free_dialog_free_call", AstrosageKundliApplication.currentEventType,"");

            }
            String wallet = CUtils.getWalletRs(this);
            //Log.e("LoadMore wallet ", wallet);
            if ((wallet!=null) && !wallet.equals("0.0")) {
                CUtils.fcmAnalyticsEvents("first_min_free_dialog",AstrosageKundliApplication.currentEventType,"");
                freeMinutedialog = new FreeMinuteDialog("");
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
            }else {

            }
        } catch (Exception e) {
            //
        }

    }
    private void callSelectedAstrologer(String mobileNo, String urlTextt) {

        if (!CUtils.isConnectedWithInternet(currentActivity)) {
            CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.no_internet), currentActivity);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(currentActivity);
            pd.show();
            pd.setCancelable(false);

            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CALL_ASTROLOGER_URL,
                    LiveActivityNew.this, false, getCallParams(mobileNo, urlTextt), PRIVATE_CALL).getMyStringRequest();
            queue.add(stringRequest);*/

            CUtils.fcmAnalyticsEvents("connect_call_api_called",AstrosageKundliApplication.currentEventType,"");

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.connectNetworkCall(getCallParams(mobileNo, urlTextt));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();

                        try {

                            close_layout(mAstrologerDescription);
                            call_astrologer_btn.setClickable(true);

                            JSONObject jsonObject = new JSONObject(myResponse);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            try {
                                if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                                    freeMinutedialog.dismiss();
                                }
                            }catch (Exception e){
                                //
                            }
                            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                                final String callsId = jsonObject.getString("callsid");
                                String talkTime = jsonObject.getString("talktime");
                                final String exophoneNo = jsonObject.getString("exophone");
                                final String internationalCharges = jsonObject.getString("callcharge");

                                CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog",AstrosageKundliApplication.currentEventType,"");
                                openFreeMinDialogBox();
                                String astroName = "",profileUrl ="";
                                if(!TextUtils.isEmpty(astrologerDetailBeanData.getName())){
                                    astroName = astrologerDetailBeanData.getName();
                                    profileUrl = astrologerDetailBeanData.getImageFile();
                                }

                                CUtils.fcmAnalyticsEvents("show_call_initiate_dialog",AstrosageKundliApplication.currentEventType,"");

                                CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CGlobalVariables.CALL_CLICK, internationalCharges,astroName,profileUrl);
                                dialog.show(getSupportFragmentManager(), "Dialog");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            CUtils.fcmAnalyticsEvents("start_call_status_service",CGlobalVariables.SERVICE_CALL_EVENT,"");

                                            Intent intent = new Intent(LiveActivityNew.this, CallStatusCheckService.class);
                                            intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                                            currentActivity.startService(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 60000);

                            } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                                CUtils.fcmAnalyticsEvents("call_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                                String dialogMsg = getResources().getString(R.string.unable_to_connect_free_consultation);
                                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                                CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog",AstrosageKundliApplication.currentEventType,"");

                                String astrologerName = jsonObject.getString("astrologername");
                                String userbalance = jsonObject.getString("userbalance");
                                String minBalance = jsonObject.getString("minbalance");

                                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CGlobalVariables.CALL_CLICK);
                                dialog.show(getSupportFragmentManager(), "Dialog");

                            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                                CUtils.fcmAnalyticsEvents("call_astrologer_busy_dialog",AstrosageKundliApplication.currentEventType,"");
                                String dialogMsg = "";
                                if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                    dialogMsg = getResources().getString(R.string.user_blocked_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                    dialogMsg = getResources().getString(R.string.astrologer_busy_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                    dialogMsg = getResources().getString(R.string.astrologer_offline_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                    dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                    dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                    dialogMsg = getResources().getString(R.string.existing_call_msg);
                                } else {
                                    dialogMsg = getResources().getString(R.string.something_wrong_error);
                                }

                                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CALL_CLICK);
                            }
                        } catch (Exception e) {}

                    } catch (Exception e) {
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

        }
    }

    public Map<String, String> getCallParams(String phoneNo, String urlText) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(currentActivity));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
            } else {
                headers.put(CGlobalVariables.USER_PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.URL_TEXT, urlText);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(currentActivity));
        headers.put(CGlobalVariables.CALL_SOURCE,TAG);
        String currentOfferType = CUtils.getUserIntroOfferType(this);
        boolean isFreeConsultation = liveAstrologerModel.isPrivateIntroOffer();
        headers.put(CGlobalVariables.ISFREE_CONSULTATION, String.valueOf(isFreeConsultation));
        headers.put(CGlobalVariables.OFFER_TYPE,currentOfferType);
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(currentActivity)));
        //send sfc
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(currentActivity));
        return headers;
    }

    private void initLeaveAndFollowDialog() {
        leaveLiveSessionsLayout = findViewById(R.id.layout_leave_live_session_dialog);
        ImageView closeView = leaveLiveSessionsLayout.findViewById(R.id.close);
        TextView titleText = leaveLiveSessionsLayout.findViewById(R.id.title_text);
        FontUtils.changeFont(currentActivity, titleText, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        RecyclerView astrologerList = leaveLiveSessionsLayout.findViewById(R.id.astrologer_list);
        TextView astrologerNotFound = leaveLiveSessionsLayout.findViewById(R.id.astrologer_not_found);
        LiveAstrologerBottomAdapter adapter = new LiveAstrologerBottomAdapter(currentActivity, liveAstrologerModelList);
        astrologerList.setAdapter(adapter);
        if (liveAstrologerModelList != null) {
            if (liveAstrologerModelList.size() == 0)
                astrologerNotFound.setVisibility(View.VISIBLE);
        } else
            astrologerNotFound.setVisibility(View.VISIBLE);
        Button leave = leaveLiveSessionsLayout.findViewById(R.id.leave);
        FontUtils.changeFont(currentActivity, leave, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        Button followAndLeave = leaveLiveSessionsLayout.findViewById(R.id.followAndLeave);
        FontUtils.changeFont(currentActivity, followAndLeave, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        if(followStatus != null && followStatus.equals("0"))
        {
            followAndLeave.setText(getResources().getString(R.string.follow_amp_leave));
        }
        else
        {
            followAndLeave.setText(getResources().getString(R.string.text_stay));
        }
        closeView.setOnClickListener(view -> {
            close_layout(leaveLiveSessionsLayout);
        });
        leave.setOnClickListener(view -> {
            finishLiveSession(CGlobalVariables.FINISH_LIVE_SESSION_USER);
        });
        followAndLeave.setOnClickListener(view -> {
            close_layout(leaveLiveSessionsLayout);
            if(followAndLeave.getText().equals(getResources().getString(R.string.follow_amp_leave)))
            {
                if (CUtils.getUserLoginStatus(currentActivity)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_FOLLOW_ASTROLOGER_BTN_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    finish_live_session=1;
                    followReqCount = 0;
                    followAstrologerRequest("1");
                } else {
                    openLoginDialog();
                }
            }
        });
        adapter.onItemClick = new LiveAstrologerBottomAdapter.OnItemClick() {
            @Override
            public void itemClick(View view, int position) {
                try {
                    LiveAstrologerModel astrologerModel = liveAstrologerModelList.get(position);
                    openLiveStramingScrean(astrologerModel,true);
                } catch (Exception e) {
                    //
                }
            }
        };
    }




    private void initAstrologerDescriptionDialog() {
        try {
            TextView desc_astrologer_nameTv = mAstrologerDescription.findViewById(R.id.desc_astrologer_name);
            TextView descExpertiseTv = mAstrologerDescription.findViewById(R.id.desc_expertise);
            TextView desc_price_detailTv = mAstrologerDescription.findViewById(R.id.desc_price_detail);
            TextView descExperienceTv = mAstrologerDescription.findViewById(R.id.desc_experience);
            TextView descPriceCrossTv = mAstrologerDescription.findViewById(R.id.descPriceCross);
            TextView desc_wallet_balanceTv = mAstrologerDescription.findViewById(R.id.desc_wallet_balance);

            TextView tvVideoCall = mAstrologerDescription.findViewById(R.id.tv_video_call);
            TextView tvAudioCall = mAstrologerDescription.findViewById(R.id.tv_audio_call);
            TextView tvAnonymousCall = mAstrologerDescription.findViewById(R.id.tv_anonymous_call);

            TextView tvPriceToCrossVideoCall = mAstrologerDescription.findViewById(R.id.tvPriceToCrossVideoCall);
            TextView tvPriceToCallVideoCall = mAstrologerDescription.findViewById(R.id.tvPriceToCallVideoCall);
            TextView tvPriceToCrossAudioCall = mAstrologerDescription.findViewById(R.id.tvPriceToCrossAudioCall);
            TextView tvPriceToCallAudioCall = mAstrologerDescription.findViewById(R.id.tvPriceToCallAudioCall);
            TextView tvPriceToCrossAnonCall = mAstrologerDescription.findViewById(R.id.tvPriceToCrossAnonCall);
            TextView tvPriceToCallAnonCall = mAstrologerDescription.findViewById(R.id.tvPriceToCallAnonCall);

            //tvPriceToCrossVideoCall.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //tvPriceToCrossAudioCall.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            //tvPriceToCrossAnonCall.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            FontUtils.changeFont(currentActivity, desc_astrologer_nameTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, descExpertiseTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, desc_price_detailTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, descExperienceTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, descPriceCrossTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, desc_wallet_balanceTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, descWalletAmount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

            FontUtils.changeFont(currentActivity, tvVideoCall, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, tvAudioCall, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(currentActivity, tvAnonymousCall, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

            FontUtils.changeFont(currentActivity, tvPriceToCrossVideoCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, tvPriceToCallVideoCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, tvPriceToCrossAudioCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, tvPriceToCallAudioCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, tvPriceToCrossAnonCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, tvPriceToCallAnonCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

            ImageView desc_cancel = mAstrologerDescription.findViewById(R.id.desc_cancel);
            LinearLayout ll_desc_join_queue = mAstrologerDescription.findViewById(R.id.ll_desc_join_queue);
            RelativeLayout rlPrivateCall = mAstrologerDescription.findViewById(R.id.rlPrivateCall);
            RelativeLayout rlAudioCall = mAstrologerDescription.findViewById(R.id.rlAudioCall);
            RelativeLayout rlVideoCall = mAstrologerDescription.findViewById(R.id.rlVideoCall);
            rlPrivateCall.setVisibility(View.GONE);
            //FontUtils.changeFont(currentActivity, desc_join_video_call, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

            desc_astrologer_nameTv.setText(liveAstrologerModel.getName());
            descExperienceTv.setText(liveAstrologerModel.getExperience() + " " + getResources().getString(R.string.experience_text));
            descExpertiseTv.setText(CUtils.toTitleCases(liveAstrologerModel.getExpertise()));
            desc_cancel.setOnClickListener(view -> {
                call_astrologer_btn.setClickable(true);
                close_layout(mAstrologerDescription);
            });

            if (liveAstrologerModel.getIsavailableforcall() != null && liveAstrologerModel.getIsavailableforcall().equalsIgnoreCase("true")) {
                rlPrivateCall.setVisibility(View.VISIBLE);
            } else {
                rlPrivateCall.setVisibility(View.GONE);
            }

            String offerType = CUtils.getCallChatOfferType(this);


            if (Float.parseFloat(CUtils.getWalletRs(LiveActivityNew.this)) > 0) {
                ll_desc_join_queue.setVisibility(View.VISIBLE);
                descRecharge.setVisibility(View.GONE);
                descWalletBalanceTxt.setVisibility(View.GONE);
            } else {
                if ((liveAstrologerModel.isPrivateIntroOffer() && !TextUtils.isEmpty(offerType)) && liveAstrologerModel.getIsavailableforcall().equalsIgnoreCase("true")) {
                    ll_desc_join_queue.setVisibility(View.VISIBLE);
                    rlPrivateCall.setVisibility(View.VISIBLE);
                    rlAudioCall.setVisibility(View.GONE);
                    rlVideoCall.setVisibility(View.GONE);
                } else {
                    ll_desc_join_queue.setVisibility(View.GONE);
                }
                descRecharge.setVisibility(View.VISIBLE);
                descWalletBalanceTxt.setVisibility(View.VISIBLE);
            }

            boolean liveIntroOffer = CUtils.isLiveintrooffer(this);
            if (!CUtils.getUserLoginStatus(this)) {
                liveIntroOffer = true;
            }

            String amountStr = currentActivity.getResources().getString(R.string.live_call_price);
            //  For Audio user
            if (liveIntroOffer) {
                amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudiointroprice());
                tvPriceToCallAudioCall.setText(amountStr);
                //tvPriceToCrossAudioCall.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
                CUtils.setStrikeOnTextView(tvPriceToCrossAudioCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());

            } else if (liveAstrologerModel.getActualliveaudioprice() != null && liveAstrologerModel.getActualliveaudioprice().equals(liveAstrologerModel.getLiveaudioprice())) {
                amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudioprice());
                tvPriceToCallAudioCall.setText(amountStr);
                tvPriceToCrossAudioCall.setVisibility(View.GONE);
            } else {
                amountStr = amountStr.replace("#", liveAstrologerModel.getLiveaudioprice());
                tvPriceToCallAudioCall.setText(amountStr);
                //tvPriceToCrossAudioCall.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());
                CUtils.setStrikeOnTextView(tvPriceToCrossAudioCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveaudioprice());

            }

            // For Video User
            String videoPrice = currentActivity.getResources().getString(R.string.live_call_price);
            if (liveIntroOffer) {
                videoPrice = videoPrice.replace("#", liveAstrologerModel.getLivevideointroprice());
                tvPriceToCallVideoCall.setText(videoPrice);
                //tvPriceToCrossVideoCall.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActuallivevideoprice());
                CUtils.setStrikeOnTextView(tvPriceToCrossVideoCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActuallivevideoprice());

            } else if (liveAstrologerModel.getActuallivevideoprice() != null && liveAstrologerModel.getActuallivevideoprice().equals(liveAstrologerModel.getLivevideoprice())) {
                videoPrice = videoPrice.replace("#", liveAstrologerModel.getLivevideoprice());
                tvPriceToCallVideoCall.setText(videoPrice);
                tvPriceToCrossVideoCall.setVisibility(View.GONE);
            } else {
                videoPrice = videoPrice.replace("#", liveAstrologerModel.getLivevideoprice());
                tvPriceToCallVideoCall.setText(videoPrice);
                //tvPriceToCrossVideoCall.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActuallivevideoprice());
                CUtils.setStrikeOnTextView(tvPriceToCrossVideoCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActuallivevideoprice());

            }

            // For Private call
            String anonPrice = currentActivity.getResources().getString(R.string.live_call_price);

            if (liveAstrologerModel.isPrivateIntroOffer() && !TextUtils.isEmpty(offerType)) {

                //Log.d("AstroData", "offerType2="+offerType);

                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    if (CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
                        anonPrice = anonPrice.replace("#", liveAstrologerModel.getLiveanonymousprice());
                        //tvPriceToCallAnonCall.setText(anonPrice);
                        tvPriceToCrossAnonCall.setText(getResources().getString(R.string.text_free));

                        CUtils.removeStrikeOnTextView(tvPriceToCrossAnonCall);
                        CUtils.setStrikeOnTextView(tvPriceToCallAnonCall, anonPrice);
                    } else {
                        anonPrice = anonPrice.replace("#", liveAstrologerModel.getLiveanonymousprice());
                        tvPriceToCallAnonCall.setText(anonPrice);
                        CUtils.setStrikeOnTextView(tvPriceToCrossAnonCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveanonymousprice());
                    }
                    //tvPriceToCrossAnonCall.setPaintFlags(tvPriceToCrossAnonCall.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    //tvPriceToCallAnonCall.setPaintFlags(tvPriceToCallAnonCall.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    anonPrice = anonPrice.replace("#", liveAstrologerModel.getLiveanonymousintroprice());
                    tvPriceToCallAnonCall.setText(anonPrice);
                    //tvPriceToCrossAnonCall.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveanonymousprice());
                    CUtils.setStrikeOnTextView(tvPriceToCrossAnonCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveanonymousprice());

                }
            } else if (liveAstrologerModel.getActualliveanonymousprice() != null && liveAstrologerModel.getActualliveanonymousprice().equals(liveAstrologerModel.getLiveanonymousprice())) {
                anonPrice = anonPrice.replace("#", liveAstrologerModel.getLiveanonymousprice());
                tvPriceToCallAnonCall.setText(anonPrice);
                tvPriceToCrossAnonCall.setVisibility(View.GONE);
            } else {
                anonPrice = anonPrice.replace("#", liveAstrologerModel.getLiveanonymousprice());
                tvPriceToCallAnonCall.setText(anonPrice);
                //tvPriceToCrossAnonCall.setText(getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveanonymousprice());
                CUtils.setStrikeOnTextView(tvPriceToCrossAnonCall, getResources().getString(R.string.rs_sign) + liveAstrologerModel.getActualliveanonymousprice());


            }
            if (audioCallStatus == null) {
                audioCallStatus = CGlobalVariables.STATUS_LIVE_CALL_ON;
            }
            if (videoCallStatus == null) {
                videoCallStatus = CGlobalVariables.STATUS_LIVE_CALL_ON;
            }
            if (privateCallStatus == null) {
                privateCallStatus = CGlobalVariables.STATUS_LIVE_CALL_ON;
            }

            if (audioCallStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_ON)) {
                desc_join_audio_call.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_orange_min));
                desc_join_audio_call.setEnabled(true);
            } else {
                desc_join_audio_call.setBackground(ContextCompat.getDrawable(this, R.drawable.light_gray_rect_less_rounded));
                desc_join_audio_call.setEnabled(false);
            }
            if (videoCallStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_ON)) {
                desc_join_video_call.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_orange_min));
                desc_join_video_call.setEnabled(true);
            } else {
                desc_join_video_call.setBackground(ContextCompat.getDrawable(this, R.drawable.light_gray_rect_less_rounded));
                desc_join_video_call.setEnabled(false);
            }
            if (privateCallStatus.equals(CGlobalVariables.STATUS_LIVE_CALL_ON)) {
                desc_join_private_call.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_button_orange_min));
                desc_join_private_call.setEnabled(true);
            } else {
                desc_join_private_call.setBackground(ContextCompat.getDrawable(this, R.drawable.light_gray_rect_less_rounded));
                desc_join_private_call.setEnabled(false);
            }
        } catch (Exception e){
            //
        }

    }

    private void initAstrologerLoginDialog() {
        TextView live_login_textTv = mLoginLayout.findViewById(R.id.live_login_text);
        FontUtils.changeFont(currentActivity, live_login_textTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        Button live_login_cancel_btn = mLoginLayout.findViewById(R.id.live_login_cancel_btn);
        Button live_login_ok_btn = mLoginLayout.findViewById(R.id.live_login_ok_btn);
        FontUtils.changeFont(currentActivity, live_login_cancel_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(currentActivity, live_login_ok_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        live_login_cancel_btn.setOnClickListener(view -> {
            close_layout(mLoginLayout);
        });

        live_login_ok_btn.setOnClickListener(view -> {
            openLoginScreen();
        });
    }
    private void initAstrologerFollowDialog() {
        TextView live_follow_textTv = mFollowLayout.findViewById(R.id.live_login_text);
        FontUtils.changeFont(currentActivity, live_follow_textTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        live_follow_textTv.setText(getResources().getString(R.string.astro_follow_txt).replace("#", liveAstrologerModel.getName()));
        TextView live_astrologer_name_follow = mFollowLayout.findViewById(R.id.live_astrologer_name_follow);
        FontUtils.changeFont(currentActivity, live_astrologer_name_follow, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        live_astrologer_name_follow.setText(liveAstrologerModel.getName());

        CircularNetworkImageView live_astrologer_image_follow = findViewById(R.id.live_astrologer_image_follow);
        String astrologerProfileUrl = "";
        if (liveAstrologerModel.getProfileImgUrl() != null && liveAstrologerModel.getProfileImgUrl().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModel.getProfileImgUrl();
           // live_astrologer_image_follow.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(currentActivity).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(live_astrologer_image_follow);
            //Log.e("liveimageurl", " : " + astrologerProfileUrl);
        }

        Button live_follow_ok_btn = mFollowLayout.findViewById(R.id.live_follow_ok_btn);
        FontUtils.changeFont(currentActivity, live_follow_ok_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        ImageView follow_cancel = mFollowLayout.findViewById(R.id.follow_cancel);
        follow_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_layout(mFollowLayout);
            }
        });

        live_follow_ok_btn.setOnClickListener(view -> {
            close_layout(mFollowLayout);
            if (CUtils.getUserLoginStatus(currentActivity)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_FOLLOW_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //finish_live_session=0;
                followReqCount = 0;
                followAstrologerRequest("1");
            } else {
                openLoginDialog();
            }
        });

    }

    private void initAstrologerUnFollowDialog() {
        TextView live_follow_textTv = mUnFollowLayout.findViewById(R.id.live_login_text);
        FontUtils.changeFont(currentActivity, live_follow_textTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        TextView live_astrologer_name_follow = mUnFollowLayout.findViewById(R.id.live_astrologer_name_follow);
        FontUtils.changeFont(currentActivity, live_astrologer_name_follow, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        live_astrologer_name_follow.setText(liveAstrologerModel.getName());

        CircularNetworkImageView live_astrologer_image_follow = findViewById(R.id.live_astrologer_image_unfollow);
        String astrologerProfileUrl = "";
        if (liveAstrologerModel.getProfileImgUrl() != null && liveAstrologerModel.getProfileImgUrl().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModel.getProfileImgUrl();
           // live_astrologer_image_follow.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(currentActivity).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(live_astrologer_image_follow);

            //Log.e("liveimageurl", " : " + astrologerProfileUrl);
        }

        Button live_follow_ok_btn = mUnFollowLayout.findViewById(R.id.live_follow_ok_btn);
        FontUtils.changeFont(currentActivity, live_follow_ok_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        ImageView follow_cancel = mUnFollowLayout.findViewById(R.id.unfollow_cancel);
        follow_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_layout(mUnFollowLayout);
            }
        });

        live_follow_ok_btn.setOnClickListener(view -> {
            close_layout(mUnFollowLayout);
            if (CUtils.getUserLoginStatus(currentActivity)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_UNFOLLOW_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                followReqCount = 0;
                followAstrologerRequest("0");
            } else {
                openLoginDialog();
            }
        });

    }

    private void initEndConsultation() {
        TextView live_end_text = mConsultationEnd.findViewById(R.id.live_end_text);
        FontUtils.changeFont(currentActivity, live_end_text, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        Button live_consultation_cancel_btn = mConsultationEnd.findViewById(R.id.live_consultation_cancel_btn);
        Button live_consultation_confirm_btn = mConsultationEnd.findViewById(R.id.live_consultation_confirm_btn);
        FontUtils.changeFont(currentActivity, live_consultation_cancel_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(currentActivity, live_consultation_confirm_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        live_consultation_cancel_btn.setOnClickListener(view -> {
            close_layout(mConsultationEnd);
        });

        live_consultation_confirm_btn.setOnClickListener(view -> {
            updateEndCallStatusInFirebaseDB(1, CGlobalVariables.USER_ENDED);
            CUtils.fcmAnalyticsEvents("live_call_end_consultation",AstrosageKundliApplication.currentEventType,"");

            processEndCallForCallingUser();
            close_layout(mConsultationEnd);
        });
    }

    private void initEndConsultationMsg(boolean isRateAstro) {
        TextView end_textTv_1 = mConsultationEndMsg.findViewById(R.id.end_text_1);
        TextView end_textTv_2 = mConsultationEndMsg.findViewById(R.id.end_text_2);
        FontUtils.changeFont(currentActivity, end_textTv_1, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(currentActivity, end_textTv_2, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        Button consult_msg_session = mConsultationEndMsg.findViewById(R.id.consult_msg_session);
        Button consult_msg_rate = mConsultationEndMsg.findViewById(R.id.consult_msg_rate);
        FontUtils.changeFont(currentActivity, consult_msg_session, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(currentActivity, consult_msg_rate, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        if(isRateAstro){
            consult_msg_rate.setVisibility(View.VISIBLE);
        } else {
            consult_msg_rate.setVisibility(View.GONE);
        }

        consult_msg_session.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ANOTHER_SESSION_CLICK,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            close_layout(mConsultationEndMsg);
            onLeaveClicked();
        });

        consult_msg_rate.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ASTRO_RATE_CLICK,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            close_layout(mConsultationEndMsg);
            showRatingDialogToUser(astrologerDetailBeanData.getUserOwnReviewModel());
        });
    }

    private void initEndConsultationMsgAstro() {
        TextView end_textTv_1 = mEndConsultationFromAstro.findViewById(R.id.end_text_1);
        TextView end_textTv_2 = mEndConsultationFromAstro.findViewById(R.id.end_text_2);
        FontUtils.changeFont(currentActivity, end_textTv_1, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(currentActivity, end_textTv_2, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        Button consult_msg_session = mEndConsultationFromAstro.findViewById(R.id.consult_msg_session_1);
        FontUtils.changeFont(currentActivity, consult_msg_session, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        consult_msg_session.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ANOTHER_SESSION_CLICK,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            close_layout(mEndConsultationFromAstro);
            onLeaveClicked();
        });
    }

    private void initAstroNotLive() {
        TextView end_textTv_1 = mAstroNotLive.findViewById(R.id.end_text_1);
        TextView end_textTv_2 = mAstroNotLive.findViewById(R.id.end_text_2);
        FontUtils.changeFont(currentActivity, end_textTv_1, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(currentActivity, end_textTv_2, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        Button btn_not_live_ok = mAstroNotLive.findViewById(R.id.btn_not_live_ok);
        FontUtils.changeFont(currentActivity, btn_not_live_ok, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        btn_not_live_ok.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_ASTRO_NOT_LIVE_CLICK,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            close_layout(mAstroNotLive);
            if(getAstroListFromEndLiveSession){
                getAstroListFromEndLiveSession = false;
                showProgressBar();
            }else {
                onLeaveClicked();
            }
            //finishLiveSession();
        });

    }

    private void openLiveStramingScrean(LiveAstrologerModel astrologerModel, boolean isSwipeNext) {
        try {
            finishLiveSession(CGlobalVariables.FINISH_LIVE_SESSION_OPEN_NEW);
            AstrosageKundliApplication.liveAstrologerModel = astrologerModel;
            Intent intent = new Intent(currentActivity, LiveActivityNew.class);
            if (userSpentTime != 0) {
                intent.putExtra("prechannelname", channelName);
                intent.putExtra("preastrologerid", liveAstrologerModel.getId());
                userSpentTime = System.currentTimeMillis() - userSpentTime;
                Log.e("liveQualityTest", "userSpentTime at next->" + (userSpentTime/1000));
                intent.putExtra("userspenttime", String.valueOf(userSpentTime/1000));
            }
            startActivity(intent);
            if(isSwipeNext){
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.no_animation);
            }else{
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.no_animation);
            }

        } catch (Exception e) {

        }
    }

    private void listenConnectCallFirebaseDB() {
        try {
            if (TextUtils.isEmpty(channelName)) return;

            if (runningCallsIdDbRef != null && runningCallsIdEventListener != null) {
                runningCallsIdDbRef.removeEventListener(runningCallsIdEventListener);
            }

            runningCallsIdEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Log.d("SAN_NEW","ondataChnageCalled");
                    AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" ConnectCall onDataChange() ##";

                    try {
                        String runningCallsId = (String) dataSnapshot.getValue();
                        //Log.d("SAN_NEW","runningCallsId    :"+runningCallsId);
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" runningCallsId="+runningCallsId+" ##";

                        if (!TextUtils.isEmpty(runningCallsId) && !runningCallsId.equalsIgnoreCase("NA")) {
                            getCallingUserDetailsFromFirebaseDB(runningCallsId);
                        } else {
                            processEndCallForOtherUser();
                        }
                    } catch (Exception e) {
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" ConnectCall exp="+e+" ##";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" ConnectCall() onCancelled() databaseError="+databaseError+" ##";
                }
            };

            runningCallsIdDbRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelName + "/" + CGlobalVariables.LIVE_STREAMING_CALLS_NODE + "/" + CGlobalVariables.LIVE_STREAMING_RUNNING_CALL_NODE);
            runningCallsIdDbRef.addValueEventListener(runningCallsIdEventListener);
        } catch (Exception e) {
            //
        }
    }

    private void listenConnectPrivateCallFirebaseDB() {
        //Log.e("ListFlatuate ", " listenConnectPrivateCallFirebaseDB() channelName=> " + channelName );
        try {
            if (TextUtils.isEmpty(channelName)) return;

            if (runningCallsIdDbRefPrivateCall != null && runningCallsIdEventListenerPrivateCall != null) {
                runningCallsIdDbRefPrivateCall.removeEventListener(runningCallsIdEventListenerPrivateCall);
            }

            runningCallsIdEventListenerPrivateCall = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" ConnectPvtCall onDataChange() ##";
                    try {
                        String runningCallsId = (String) dataSnapshot.getValue();
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+"ConnectPvtCall runningCallsId="+runningCallsId+" ##";

                        if (!TextUtils.isEmpty(runningCallsId) && !runningCallsId.equalsIgnoreCase("NA")) {
                            getCallingUserDetailsFromFirebaseDB(runningCallsId);
                        } else {
                            processEndCallForOtherUser();
                        }
                    } catch (Exception e) {
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+"ConnectPvtCall exp="+e+" ##";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+"ConnectPvtCall onCancelled="+databaseError+" ##";
                }
            };
            runningCallsIdDbRefPrivateCall = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelName + "/" + CGlobalVariables.LIVE_STREAMING_CALLS_NODE + "/" + CGlobalVariables.LIVE_STREAMING_RUNNING_CALL_NODE_PRIVATE_CALL);
            runningCallsIdDbRefPrivateCall.addValueEventListener(runningCallsIdEventListenerPrivateCall);
        } catch (Exception e) {
            //
        }
    }

    private void getCallingUserDetailsFromFirebaseDB(String runningCallsId) {
        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails()"+runningCallsId+" ##";
        //Log.e("ListFlatuate ", " getCallingUserDetailsFromFirebaseDB() ");
        try {
            DatabaseReference callsIdDbRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelName + "/" + CGlobalVariables.LIVE_STREAMING_CALLS_NODE + "/" + runningCallsId);
            //Log.e("SAN ", " getCallling callsIdEndCallDbRef " + callsIdEndCallDbRef );
            callsIdDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        //Log.d("SAN_NEW", "processCall()1.0");
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() onDataChange()1"+" ##";
                        //Log.d("SAN_NEW ", " getCallingUserDetailsFromFirebaseDB()  onDataChange() ");
                        Map map = (Map) dataSnapshot.getValue();
                        callsIdDbRef.removeEventListener(this);
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() myCallingState="+myCallingState+" ##";
                        // Log.e("PrivateCall", "processCall()1.1");
                        //Log.d("SAN_NEW ", " getCallingUserDetailsFromFirebaseDB()  myCallingState= "+myCallingState);
                        if (myCallingState == CGlobalVariables.CALL_STATE_DISCONNECTED) { // i.e, my audio call is not ongoing with astrologer
                            //Log.d("SAN_NEW", "processCall()1.2");
                            callTypeForOtherUser = (String) map.get("CallType");
                            AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() callType="+callTypeForOtherUser+" ##";

                            long callDurationInSecs = (long) map.get("InitiatedCallDurationSecs"); //in seconds
                            long callCreateTimeInMilliseconds = (long) map.get("CreateTime"); //in milliseconds
                            String userName = (String) map.get("FirstName");
                            //Log.e("PrivateCall", "processCall()1.3 = "+callTypeForOtherUser);
                            //Log.d("SAN_NEW ", " getCallingUserDetailsFromFirebaseDB() callTypeForOtherUser=> " + callTypeForOtherUser);

                            if ( callTypeForOtherUser != null && callTypeForOtherUser.equalsIgnoreCase(CGlobalVariables.LIVE_STREAMING_KEY_PRIVATE_CALL) ) {
                                //Log.d("SAN_NEW", "processCall()1.4");
                                callDurationInSecsPrivateCall = callDurationInSecs;
                                callCreateTimeInMillisecondsPrivateCall = callCreateTimeInMilliseconds;
                                userNamePrivateCall = userName;
                                AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() isPvtCallTimerDisplayed="+isPvtCallTimerDisplayed+" ##";

                                //Log.d("SAN_NEW", "processCall()1.5="+textPrivateCallInfo.getVisibility());
                                if( !isPvtCallTimerDisplayed) {
                                    //Log.d("SAN_NEW", "textPrivateCallInfo.getVisibility()="+textPrivateCallInfo.getVisibility());
                                    AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() processCall()2 duretion=##"+callDurationInSecsPrivateCall;
                                    processCall(callDurationInSecsPrivateCall, callCreateTimeInMillisecondsPrivateCall, userNamePrivateCall);
                                    //Log.e("PrivateCall", "processCall()1");
                                    isPvtCallTimerDisplayed = true;
                                }
                            } else {
                                AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() processCall()1 ##";
                                processCall(callDurationInSecs, callCreateTimeInMilliseconds, userName);
                            }
                        }
                    } catch (Exception e) {
                        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() exp="+e+" ##";
                        //Log.d("SAN_NEW ", " getCallingUserDetailsFromFirebaseDB()  e= "+e.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.d("SAN_NEW", "databaseError"+databaseError.toString());
                    AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog+" UserDetails() onCancelled() databaseError="+databaseError+" ##";

                }
            });
        } catch (Exception e) {
            //
        }
    }

    private void processCall(long callDurationInSecs, long callCreateTimeInMilliseconds, String userName) {

        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog + " processCall() callTypeForOtherUser=" + callTypeForOtherUser + " ##";

        handleVideoAndAnonymousCall();

        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog + " processCall() 2" + " ##";

        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog + " processCall() 3" + " ##";

        //Log.e("SAN ", " userName  " + userName);
        long callDurationInMiliseconds = TimeUnit.SECONDS.toMillis(callDurationInSecs);

        //Log.e("ListFlatuate ", " getCallingUserDetailsFromFirebaseDB()  currentTimeStamp= "+currentTimeStamp);
        //Log.e("ListFlatuate ", " getCallingUserDetailsFromFirebaseDB()  callCreateTimeInMilliseconds= "+callCreateTimeInMilliseconds);

        long diffTimeInMiliseconds = 0;
        if (currentTimeStamp > callCreateTimeInMilliseconds) {
            diffTimeInMiliseconds = currentTimeStamp - callCreateTimeInMilliseconds;
            //Log.e("ListFlatuate ", " getCallingUserDetailsFromFirebaseDB()  diffTimeInMiliseconds= "+diffTimeInMiliseconds);
        }

        initCountDownTimerForOtherUser((callDurationInMiliseconds - diffTimeInMiliseconds), userName);
        AstrosageKundliApplication.runningCallsIdLog = AstrosageKundliApplication.runningCallsIdLog + " processCall() 4" + " ##";

    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.frameLiveIntro).getVisibility() == View.GONE){
            if (llAlr2.getVisibility() == View.VISIBLE) {
                initEndConsultation();
                open_layout(mConsultationEnd);
                return;
            }

            if (giftLayout.getVisibility() == View.VISIBLE) {
                close_layout(giftLayout);
                sendMessageLL.setVisibility(View.VISIBLE);
                return;
            }

            boolean isLeave = false;
            if (leaveLiveSessionsLayout != null || mAstrologerDescription != null || mLoginLayout != null || mConsultationEnd != null || mConsultationEndMsg != null || mEndConsultationFromAstro != null || mAstroNotLive != null) {
                if (mAstrologerDescription.getVisibility() == View.VISIBLE) {
                    call_astrologer_btn.setClickable(true);
                    close_layout(mAstrologerDescription);
                } else if (mLoginLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mLoginLayout);
                } else if (mConsultationEnd.getVisibility() == View.VISIBLE) {
                    close_layout(mConsultationEnd);
                } else if (mConsultationEndMsg.getVisibility() == View.VISIBLE) {
                    close_layout(mConsultationEndMsg);
                } else if (mEndConsultationFromAstro.getVisibility() == View.VISIBLE) {
                    close_layout(mEndConsultationFromAstro);
                } else if (mAstroNotLive.getVisibility() == View.VISIBLE) {
                    close_layout(mAstroNotLive);
                } else if (mFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mFollowLayout);
                } else if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mUnFollowLayout);
                } else {
                    if (leaveLiveSessionsLayout != null) {
                        if (leaveLiveSessionsLayout.getVisibility() == View.VISIBLE) {
                            close_layout(leaveLiveSessionsLayout);
                        } else {
                            isLeave = true;
                            //onLeaveClicked();
                        }
                    } else {
                        isLeave = true;
                        //onLeaveClicked();
                    }
                }
            } else {
                isLeave = true;
                //onLeaveClicked();
            }

            if (isLeave) {
                onLeaveClicked();
            }
        }
    }

    public void onAbuseReportClicked() {
        if (CUtils.getUserLoginStatus(currentActivity)) {
            try {
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN,
//                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                if (reportAbuseDialog != null && reportAbuseDialog.isVisible()) {
                    return;
                }
                reportAbuseDialog = new ReportAbuseDialog(LiveActivityNew.this, liveAstrologerModel.getId(), channelName);
                reportAbuseDialog.show(getSupportFragmentManager(), "Dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else openLoginDialog();
    }

    public class ConnectivityStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {

                if (isConnectivityDisconnected) {
                    setOnDisconnectListner();
                    setConnectivityRegainInFirebase();
                    stopConnectivityTimer();
                    isConnectivityDisconnected = false;
                }
            } else {

                if (!isConnectivityDisconnected) {
                    startConnectivityTimer();
                    isConnectivityDisconnected = true;
                }
            }
        }
    }

    private void getAstrologerStatusPrice(String astroid) {

        /*
        //Log.e("SAN ADA ", "Astro URL Status N Price " + CGlobalVariables.ASTROLOGER_STATUS_N_PRICE_URL + " Time=> " + System.currentTimeMillis());
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_STATUS_N_PRICE_URL,
                LiveActivityNew.this, false, getAstroStatusParams(astroid), ASTRO_STATUS_UPDATE).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
        //Log.d("jdsgsadj", "Called");
        */

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerStatusPrice(getAstroStatusParams(astroid));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String myResponse = response.body().string();
                    parseAstrologerStatus(myResponse);
                } catch (Exception e) {
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
            }
        });

    }

    public Map<String, String> getAstroStatusParams(String astroId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(currentActivity));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(currentActivity));

        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        return headers;
    }

    private void parseFollowAstrologerStatus(String response, String followVal) {
//        Log.e("followjoin1"," : "+response);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");

                if(status.equals("1"))
                {
                    followCountVal = jsonObject.getString("astrofollowcount");
                    followCount.setText(followCountVal);
                    if(followVal.equals("1"))
                    {
                        followStatus="1";
                        followTxt.setText(getResources().getString(R.string.following));
                        CUtils.subscribeFollowTopic(currentActivity, liveAstrologerModel.getId());
                    }
                    else
                    {
                        followStatus="0";
                        followTxt.setText(getResources().getString(R.string.follow));
                        CUtils.unSubscribeFollowTopic(currentActivity, liveAstrologerModel.getId());
                    }
                    if(finish_live_session==1)
                    {
                        finish_live_session = 0;
                        finishLiveSession(CGlobalVariables.FINISH_LIVE_SESSION_FOLLOW_AND_LEAVE);
                    }
                }
                else if (status.equals("100")) {
                    followReqCount++;
                    LocalBroadcastManager.getInstance(LiveActivityNew.this).registerReceiver(mReceiverBackgroundLoginServiceForFollow, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                    startBackgroundLoginService();
                }
                else
                {
                    CUtils.showSnackbar(messageTextEdit, message, currentActivity);
                }
            } catch (Exception e) { }
        }
    }

    private final BroadcastReceiver mReceiverBackgroundLoginServiceForFollow = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                if(followReqCount <=1) {
                    followUnfollowAstrologer();
                }
            } else {
                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }
            if (mReceiverBackgroundLoginServiceForFollow != null) {
                LocalBroadcastManager.getInstance(LiveActivityNew.this).unregisterReceiver(mReceiverBackgroundLoginServiceForFollow);
            }
        }
    };

    private final BroadcastReceiver mReceiverBackgroundLoginServiceJoinAudience = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                if(isStatus100){
                    isStatus100 = false;
                    joinAudianceRequest(1);
                }
            } else {
                CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }
            if (mReceiverBackgroundLoginServiceJoinAudience != null) {
                LocalBroadcastManager.getInstance(LiveActivityNew.this).unregisterReceiver(mReceiverBackgroundLoginServiceJoinAudience);
            }
        }
    };


    private void parseAstrologerStatus(String response) {
        boolean isRateAstro = false;
        try {
            astrologerDetailBeanData = new AstrologerDetailBean();
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("enablefeedbacks")) {
                String isEnabled = jsonObject.optString("enablefeedbacks");
                if (isEnabled.equalsIgnoreCase("true")){
                    isRateAstro = true;
                }
            }

            if (jsonObject.has("currentuserfeedback")) {
                JSONArray jsonArray = null;
                JSONObject reviewObject = null;
                UserReviewBean userOwnReviewBean = null;
                try {

                    jsonArray = jsonObject.getJSONArray("currentuserfeedback");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        userOwnReviewBean = new UserReviewBean();
                        reviewObject = jsonArray.getJSONObject(0);
                        userOwnReviewBean.setUsername(reviewObject.getString("username"));
                        userOwnReviewBean.setMaskedusername(reviewObject.getString("maskedusername"));
                        userOwnReviewBean.setComment(reviewObject.getString("comment"));
                        userOwnReviewBean.setRate(reviewObject.getString("rate"));
                        userOwnReviewBean.setDate(reviewObject.getString("date"));
                        userOwnReviewBean.setFeedbackId(reviewObject.getString("feedbackId"));

                        astrologerDetailBeanData.setUserOwnReviewModel(userOwnReviewBean);
                    } else {
                        astrologerDetailBeanData.setUserOwnReviewModel(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    astrologerDetailBeanData.setUserOwnReviewModel(null);
                }
                //showRatingDialogToUser(astrologerDetailBeanData.getUserOwnReviewModel());
            }


        } catch (Exception e) {
        }
        initEndConsultationMsg(isRateAstro);
        open_layout(mConsultationEndMsg);
    }

    public void showRatingDialogToUser(UserReviewBean userOwnReviewModel) {
        // Log.d("jdsgsadj", "dflsjdsljsdljflsdjf");
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            String phoneNo1 = CUtils.getUserID(currentActivity);
            if (feedbackDialog != null && feedbackDialog.isVisible()) {
                return;
            }
            String consultationType = CGlobalVariables.CON_TYPE_LIVE_AUDIO_CALL;
            if (callTypeForCallingUser != null && callTypeForCallingUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_VIDEO)) {
                consultationType = CGlobalVariables.CON_TYPE_LIVE_VIDEO_CALL;
            }

            /*feedbackDialog = new FeedbackDialog(phoneNo1, liveAstrologerModel.getId(),
                    userOwnReviewModel, consultationType, "");*/
            astrologerDetailBeanData.setAstrologerId(liveAstrologerModel.getId());
            feedbackDialog = new FeedbackDialog(consultationType,"", getSupportFragmentManager(), astrologerDetailBeanData);
            feedbackDialog.show(getSupportFragmentManager(), "Dialog");

            /*if (ratingAndDakshinaDialog != null && ratingAndDakshinaDialog.isVisible()) {
                return;
            }
            ratingAndDakshinaDialog = new RatingAndDakshinaDialog(this,
                    this,phoneNo1, liveAstrologerModel.getId(), userOwnReviewModel);
            ratingAndDakshinaDialog.show(getSupportFragmentManager(), "FeedbackDialog");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGiftDialog() {
        ImageView closeView = giftLayout.findViewById(R.id.close);
        RecyclerView giftList = giftLayout.findViewById(R.id.gift_list);
        try {
            for (int i = 0; i < giftModelArrayList.size(); i++) {
                GiftModel giftModel = giftModelArrayList.get(i);
                giftModel.setSelected(false);
            }
        }catch (Exception e){
            //
        }
        liveGiftAdapter = new LiveGiftAdapter(currentActivity, giftModelArrayList);
        giftList.setAdapter(liveGiftAdapter);

        Button btnRecharge = giftLayout.findViewById(R.id.btnRecharge);
         btnSendGift = giftLayout.findViewById(R.id.btnSendGift);
        btnSendGift.setBackgroundResource(R.drawable.bg_button_gray);
        TextView giftPriceTV = giftLayout.findViewById(R.id.giftPriceTV);

        FontUtils.changeFont(currentActivity, btnRecharge, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(currentActivity, btnSendGift, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(currentActivity, giftPriceTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        try {
            String amount = String.valueOf((int)Float.parseFloat(CUtils.getWalletRs(currentActivity)));
            String wallAmtStr = getResources().getString(R.string.text_current_balance).replace("#", amount);
            giftPriceTV.setText( wallAmtStr );
        }catch (Exception e){
            //
        }




        closeView.setOnClickListener(view -> {
            close_layout(giftLayout);
            sendMessageLL.setVisibility(View.VISIBLE);
        });

        btnRecharge.setOnClickListener(view -> {

            Intent intent = new Intent(currentActivity, WalletActivity.class);
            intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "LiveStreaming");
            startActivity(intent);

        });
        btnSendGift.setOnClickListener(view -> {
            try {
                btnSendGift.setEnabled(false);
                giftModel = null;
                for (int i = 0; i < giftModelArrayList.size(); i++) {
                    giftModel = giftModelArrayList.get(i);
                    if (giftModel.isSelected()) {
                        giftModel.setSelected(false);
                        break;
                    } else {
                        giftModel = null;
                    }
                }
                if (giftModel == null) {
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.select_send_gift_error), currentActivity);
                } else {
                    if ( Float.parseFloat( giftModel.getActualraters() ) > Float.parseFloat( CUtils.getWalletRs(currentActivity) ) ) {
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.insufficient_wallet_balance), currentActivity);
                    } else {
                        sendGiftRequest( giftModel.getServiceid() );
                    }
                }
            } catch (Exception e) {
                //
            }
        });

        liveGiftAdapter.onItemClick = new LiveGiftAdapter.OnItemClick() {
            @Override
            public void itemClick(View view, int position) {
                try {
                    btnSendGift.setBackgroundResource(R.drawable.bg_button_orange);
                    btnSendGift.setTextColor(ContextCompat.getColor(currentActivity,
                            R.color.white));
                    for (int i = 0; i < giftModelArrayList.size(); i++) {
                        GiftModel giftModel = giftModelArrayList.get(i);
                        giftModel.setSelected(false);
                    }

                    GiftModel giftModel = giftModelArrayList.get(position);
                    giftModel.setSelected(true);
                    liveGiftAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    //
                }
            }
        };

    }

    public void sendGiftRequest(String giftId) {

        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            /*String url = CGlobalVariables.SEND_GIFT_URL;
            //Log.e("SAN ", " gift channel name " + channelName );
            //Log.e("SAN ", " gift url " + url );
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    LiveActivityNew.this, false, sendGiftParams(giftId), SEND_GIFT_REQ).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.sendGift(sendGiftParams(giftId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        hideProgressBar();
                        String myResponse = response.body().string();
                        //Log.e("SAN ", " gift res " + response );
                        if(btnSendGift != null){
                            btnSendGift.setEnabled(true);
                        }
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if ( status.equals("1") ) {

                            updateWalletBalance();

                            makeGiftUnSelected();
                            //CUtils.showSnackbar(giftLayout, getResources().getString(R.string.gift_sucessful), LiveActivityNew.this);
                            close_layout(giftLayout);
                            sendMessageLL.setVisibility(View.VISIBLE);
                        } else {

                            CUtils.showSnackbar(giftLayout, getResources().getString(R.string.gift_unsucessful)+" ("+status+")", LiveActivityNew.this);
                        }
                    } catch (Exception e){
                        CUtils.showSnackbar(giftLayout, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(btnSendGift != null){
                        btnSendGift.setEnabled(true);
                    }
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

        } else {
            CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.no_internet), currentActivity);
        }
    }

    public Map<String, String> sendGiftParams(String giftId) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(currentActivity));
        boolean isLogin = CUtils.getUserLoginStatus(currentActivity);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(currentActivity));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(currentActivity));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(currentActivity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(currentActivity) );
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        headers.put(CGlobalVariables.ASTROLOGER_ID, liveAstrologerModel.getId());
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(currentActivity));

        headers.put(CGlobalVariables.KEY_GIFT_ID, giftId);

        //Log.e("SAN ", " gift params " + headers.toString() );

        return headers;
    }

//    public void sendLogDataRequest(String head) {
//        try {
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (CUtils.isConnectedWithInternet(currentActivity)) {
//                        String url = CGlobalVariables.JOIN_LOG_DATA_URL;
//                        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                                LiveActivityNew.this, false, getLogDataParams(head), SEND_LOG_DATA_REQ).getMyStringRequest();
//                        stringRequest.setShouldCache(true);
//                        if (queue == null)
//                            queue = VolleySingleton.getInstance(currentActivity).getRequestQueue();
//
//                        //queue.add(stringRequest);
//                    } else {
//                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
//                    }
//                }
//            }, 3 * 1000);
//        } catch (Exception e) {
//            //Log.e("LiveStreamAstro ", " onJoinChannelSuccess exp "+e.toString());
//        }
//    }

    public Map<String, String> getLogDataParams(String head) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(currentActivity));
        headers.put(CGlobalVariables.CHANNEL_NAME, channelName);
        headers.put("astrologerid", liveAstrologerModel.getId());
        headers.put("externalid", String.valueOf(agoraUid));
        headers.put("userid", CUtils.getAppUserId(this));
        headers.put("logmsg", getLogMsg(head));
        if (callsId == null) callsId = "";
        headers.put("callsid", callsId);
        //Log.e("LiveStreamAstro ", " params join " + headers.toString());
        return headers;
    }

    private String getLogMsg(String head) {
        if (AstrosageKundliApplication.liveRemoteAudioState == null) {
            AstrosageKundliApplication.liveRemoteAudioState = " ## ";
        }
        if (AstrosageKundliApplication.liveLocalAudioState == null) {
            AstrosageKundliApplication.liveLocalAudioState = " ## ";
        }
        String logMsg = head + "=> RemoteAudioState=" + AstrosageKundliApplication.liveRemoteAudioState +
                "LocalAudioState=" + AstrosageKundliApplication.liveLocalAudioState +
                "AudioMode=" + CUtils.getAudioMode(this);
        return logMsg;
    }

    @Override
    public void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed) {
        //Log.e("LiveStreamAstro ", " onRemoteAudioStateChanged() ");
        //sendLogDataRequest("onRemoteAudioState");
    }

    @Override
    public void onLocalAudioStateChanged(int state, int error) {
        //Log.e("LiveStreamAstro ", " onLocalAudioStateChanged() ");
        //sendLogDataRequest();
    }

    private void registerReceiverLiveOpenKundli() {

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverLiveOpenKundli
                , new IntentFilter(CGlobalVariables.SEND_BROADCAST_LIVE_OPEN_KUNDLI));
    }

    private void getLiveAstrologerDataFromServer() {

        if (CUtils.isConnectedWithInternet(this)) {
            if(getAstroListFromCloseBtn) {
                showProgressBar();
            }

            /*
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
                    LiveActivityNew.this, false,
                    CUtils.getLiveAstroParams(this,CUtils.getActivityName(LiveActivityNew.this)),
                    FETCH_LIVE_ASTROLOGER).getMyStringRequest();
            queue.add(stringRequest);
            */

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.liveAstroList(CUtils.getLiveAstroParams(this,CUtils.getActivityName(LiveActivityNew.this)));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        try {
                            CUtils.setApiLastHitTime();
                            // Log.d("SANLANTest ", method + " Live Astro response=" + response);
                            CUtils.saveLiveAstroList(myResponse);
                            if(getAstroListFromCloseBtn){
                                getAstroListFromCloseBtn = false;
                                onLeaveClicked();
                            }
                            if(getAstroListFromEndLiveSession){
                                getAstroListFromEndLiveSession = false;
                                parseLiveAstrologerList(CUtils.getLiveAstroList());
                            }else {
                                onLeaveClicked();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(messageTextEdit, getResources().getString(R.string.something_wrong_error), LiveActivityNew.this);
                }
            });

        } else {
            if(getAstroListFromCloseBtn) {
                onLeaveClicked();
            }
        }
    }

    private void parseLiveAstrologerList(String liveAstroData) {
        try {

            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
            if(liveAstrologersArrayList!=null) liveAstrologersArrayList.clear();
            liveAstrologerModelList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LiveAstrologerModel liveAstrologerModel = CUtils.parseLiveAstrologerObject(object);
                if(liveAstrologerModel == null) continue;
                if(liveAstrologersArrayList!=null) liveAstrologersArrayList.add(liveAstrologerModel);
                liveAstrologerModelList.add(liveAstrologerModel);
            }

            /*
            * code added by shreyans
            * for keeping a consistent list while in live session
            * */
            if (CUtils.localLiveList.isEmpty()){
                CUtils.localLiveList.addAll(liveAstrologerModelList);
            } else {
                ArrayList<LiveAstrologerModel> filterList = new ArrayList<>();
                for (LiveAstrologerModel stateLiveItem:CUtils.localLiveList) {
                    LiveAstrologerModel itemToRemove = null;
                    for (LiveAstrologerModel liveItem:liveAstrologerModelList){
                        if (stateLiveItem.getId().equals(liveItem.getId())) {
                            itemToRemove = liveItem;
                            break;
                        }
                    }
                    if (itemToRemove != null){
                        liveAstrologerModelList.remove(itemToRemove);
                    } else {
                        filterList.add(stateLiveItem);
                    }
                }

                CUtils.localLiveList.addAll(liveAstrologerModelList);
                CUtils.localLiveList.removeAll(filterList);
                liveAstrologerModelList.clear();
                liveAstrologerModelList.addAll(CUtils.localLiveList);
            }

        } catch (Exception e) {

        }
    }

    public void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(this);
                pd.setCancelable(false);
            }
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void getAstroPvtCallStatus() {
        String channelId = channelName;
        try {
            if (astroPvtCallDBRef != null && astroPvtCallEventListener != null) {
                astroPvtCallDBRef.removeEventListener(astroPvtCallEventListener);
            }

            astroPvtCallDBRef = mFirebaseInstance.getReference(CGlobalVariables.LIVE_STREAMING_NODE + "/" + channelId + "/" + CGlobalVariables.IS_ON_PVT_CALL);
            astroPvtCallEventListener = (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Log.e("PrivateCall", "processCall()2.0");
                    textPrivateCallInfo.setVisibility(View.GONE);
                    textPrivateCallInfo.setTag(false);
                    try {
                        isAstroOnPvtCall = (boolean) snapshot.getValue();
                        //Log.e("PrivateCall", "processCall()2.0 = "+isAstroOnPvtCall);
                        if(isAstroOnPvtCall){
                            //Log.e("PrivateCall", "processCall()2.1");
                            if ( callTypeForOtherUser != null && callTypeForOtherUser.equalsIgnoreCase(CGlobalVariables.LIVE_STREAMING_KEY_PRIVATE_CALL) ) {
                                //Log.e("PrivateCall", "processCall()2");
                                if(!isPvtCallTimerDisplayed) {
                                    processCall(callDurationInSecsPrivateCall, callCreateTimeInMillisecondsPrivateCall, userNamePrivateCall);
                                    isPvtCallTimerDisplayed = true;
                                }
                            } else {
                                //Log.e("PrivateCall", "processCall()2.3");
                                if (!isInPictureInPictureMode){
                                    textPrivateCallInfo.setVisibility(View.VISIBLE);
                                }
                                textPrivateCallInfo.setTag(true);
                            }
                        }
                    } catch (Exception e) {
                        //Log.e("PrivateCall", "processCall()2.4="+e.toString());
                        //Log.e("AstroPvtCall","onDataChange() ex="+e.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            astroPvtCallDBRef.addValueEventListener(astroPvtCallEventListener);
        } catch (Exception e) {
        }

    }

    private void makeGiftUnSelected() {

        try {
            giftModel = null;

            if ( giftModelArrayList == null  )
                return;

            for (int i = 0; i < giftModelArrayList.size(); i++) {
                GiftModel giftModel = giftModelArrayList.get(i);
                giftModel.setSelected(false);
            }
            liveGiftAdapter.notifyDataSetChanged();

        }catch (Exception e){}

    }

    private void updateWalletBalance(){

        try {

            if ( giftModel == null )
                return;

            float currWalletBal = Float.parseFloat( CUtils.getWalletRs(currentActivity) ) ;
            currWalletBal = currWalletBal - Float.parseFloat( giftModel.getActualraters() ) ;
            CUtils.setWalletRs( currentActivity, String.valueOf( currWalletBal ) );

        } catch (Exception e){}


    }
    private void updateFollowCount(String chatType){
        try{
            if(chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_FOLLOW)){
                long fCount = Long.parseLong(followCount.getText().toString());
                followCount.setText(String.valueOf(++fCount));
            }else if(chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_UNFOLLOW)){
                long fCount = Long.parseLong(followCount.getText().toString());
                followCount.setText(String.valueOf(--fCount));
            }
        }catch (Exception e){
            //
        }
    }

    private void displayAndnimateGiftMessage(ChatMessageModel chatMessageModel){
        try{
            String chatType = chatMessageModel.getType();
            if(chatType == null) chatType = "";
            if(chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_GIFT)) {
                //Log.e("ChatMessage", "displayAndnimateGiftMessage() chatType="+chatType);
                fromTV.setText(chatMessageModel.getFrom());
                String msgText = chatMessageModel.getText();
                String giftIcon = msgText.substring(msgText.lastIndexOf(" ")+1);
                messageTV.setText(giftIcon);
                slideToAbove();
            }
        }catch (Exception e){

        }
    }

    public void slideToAbove() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        params.bottomMargin = CUtils.convertDpToPx(currentActivity,100);
        bgMsgLL.setLayoutParams(params);
        bgMsgLL.setVisibility(View.VISIBLE);
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -4.7f);

        slide.setDuration(5000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        bgMsgLL.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                bgMsgLL.clearAnimation();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.TOP|Gravity.CENTER;
                params.topMargin = CUtils.convertDpToPx(currentActivity,100);
                bgMsgLL.setLayoutParams(params);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bgMsgLL.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });
    }

    private static boolean isContain(String inputString, String[] items){
        String lowerCase = inputString.toLowerCase();
        boolean found = false;
        for (String item : items) {
            String pattern = "\\b"+item+"\\b";
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(lowerCase);
            if(m.find()){
                found = true;
                break;
            }

        }
        return found;
    }
//    public static boolean containsWordsAhoCorasick(String inputString, String[] words) {
//        Trie trie = Trie.builder().onlyWholeWords().addKeywords(words).build();
//
//        Collection<Emit> emits = trie.parseText(inputString);
//        //emits.forEach(System.out::println);
//
//        boolean found = false;
//        for(String word : words) {
//            boolean contains = Arrays.toString(emits.toArray()).contains(word);
//            if (contains) {
//                found = true;
//                break;
//            }
//        }
//        return found;
//    }

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
            pipBuilder.setAspectRatio(ratio);
            pipBuilder.build();
            enterPictureInPictureMode(pipBuilder.build());
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        Log.e("TestPIP", "onPictureInPictureModeChanged="+isInPictureInPictureMode);
        this.isInPictureInPictureMode = isInPictureInPictureMode;
        if(isInPictureInPictureMode){
            LocalBroadcastManager.getInstance(this).registerReceiver(endLiveBroadcastReceiver, new IntentFilter(END_LIVE_PIP_KEY));
            setLayoutVisibilityForPIP(false);
        } else {
            setLayoutVisibilityForPIP(true);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(endLiveBroadcastReceiver);
            if(!CUtils.getBooleanData(LiveActivityNew.this,CGlobalVariables.SHOWN_LIVE_INTRO, false)){
                showIntroFragment();
            }
        }
    }

    BroadcastReceiver endLiveBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                isInPictureInPictureMode = false;
                finishAndRemoveTask();
                Intent intent1 = new Intent(LiveActivityNew.this, ActAppModule.class);
                startActivity(intent1);
            } catch (Exception e){
                //
            }
        }
    };

    private void setLayoutVisibilityForPIP(boolean show){
        if (show){
            findViewById(R.id.live_room_top_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_astro_profile).setVisibility(View.VISIBLE);
            findViewById(R.id.next).setVisibility(View.VISIBLE);
            findViewById(R.id.previous).setVisibility(View.VISIBLE);
            findViewById(R.id.call_astrologer_btn).setVisibility(View.VISIBLE);
            showControlerOnTap();
        } else {
            findViewById(R.id.layout_leave_live_session_dialog).setVisibility(View.GONE);
            findViewById(R.id.live_room_top_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.next).setVisibility(View.INVISIBLE);
            findViewById(R.id.previous).setVisibility(View.INVISIBLE);
            findViewById(R.id.call_astrologer_btn).setVisibility(View.INVISIBLE);
            hideControlerOnTap();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        if (isInPictureInPictureMode && (llAlr2.getVisibility() != View.VISIBLE)){
            finishAndRemoveTask();
            super.onUserLeaveHint();
        }
    }

    @Override
    public void swipeRight() {
        //Log.e("SAN ", " LA swipeRight ");
        //screenTapUntap();
        callPrevious();

    }

    @Override
    public void swipeTop() {
        //Log.e("SAN ", " LA swipeTop ");
    }

    @Override
    public void swipeBottom() {
        //Log.e("SAN ", " LA swipeBottom ");
    }

    @Override
    public void swipeLeft() {
        //Log.e("SAN ", " LA swipeLeft ");
        //screenTapUntap();
        callNext();
    }

    /*@Override
    public void downTouch() {
        Log.e("SAN ", " LA downTouch ");
       // screenTapUntap();
    }*/

    private void hidePopUnders() {
        try {
            int viewCount = bottom_layer.getChildCount();
            for (int i = 0; i < viewCount; i++) {
                View view = bottom_layer.getChildAt(i);
                view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected void updateUsersCount(String userCount){
        runOnUiThread(()->{tvLiveViewerCount.setText(userCount);});
    }



    public void checkPermissionsVideo() {
        boolean granted = true;
        for (String per : PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            openProfileActivity();
        } else {
            requestPermissions();
        }
    }
    public void checkPermissionsAudio() {
        boolean granted = true;
        for (String per : PERMISSION) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            Log.e("liveAstrologerModel", "checkPermissions2");
            openProfileActivity();
        } else {
            requestPermission();
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE_VIDEO);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQ_CODE_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE_VIDEO) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                openProfileActivity();
            } else {
                openAlertDialogForOpenSetting();
            }
        }
        if (requestCode == PERMISSION_REQ_CODE_AUDIO ) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                openProfileActivity();
            } else {
                openAlertDialogForOpenSetting();
            }
        }
    }

    public void openAlertDialogForOpenSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiveActivityNew.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_microphone_permission, null);
        builder.setView(dialogView);

        // Get references to the views
        TextView notNowButton = dialogView.findViewById(R.id.btn_not_now);
        TextView txtSubText = dialogView.findViewById(R.id.txtSubText);
        Button settingsButton = dialogView.findViewById(R.id.btn_settings);
        if(callTypeForCallingUser != null && callTypeForCallingUser.equalsIgnoreCase(CGlobalVariables.CALL_TYPE_VIDEO)){
            txtSubText.setText(getString(R.string.we_need_camera_and_microphone_permission));
        }else {
            txtSubText.setText(getString(R.string.we_need_microphone_permission));
        }

        // Create and show the dialog
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        // Set click listeners
        notNowButton.setOnClickListener(v -> {
            // Close the dialog if "Not now" is clicked
            dialog.dismiss();
        });

        settingsButton.setOnClickListener(v -> {
            // Open app settings if "Settings" is clicked
            dialog.dismiss();
            openAppSettings();

        });
    }
    boolean permissionSettingClicked = false;
    private void openAppSettings() {
        permissionSettingClicked = true;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
    public void openWalletScreen(String openFrom) {
        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_astro_live_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "LVREC");
        }else  if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_astro_live_low_balance_subscribe_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "LVREC");

        }else {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_astro_desc_low_balance_page_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "APREC");
        }
        Intent intent = new Intent(LiveActivityNew.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "LiveActivityNew");
        startActivity(intent);
    }

}
