package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_HOME_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_HOME_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_HOME_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_HOME_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CHAT_WINDOW_SUBSCRIBE_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.BANNER_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FILTER_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.MAINCHATFRAGMENT;
import static com.ojassoft.astrosage.varta.utils.CUtils.astroListFilterType;
import static com.ojassoft.astrosage.varta.utils.CUtils.hideMyKeyboard;
import static com.ojassoft.astrosage.varta.utils.CUtils.sendNotificationWithLink;
import static com.ojassoft.astrosage.varta.utils.CUtils.subscribeRegisteredFreeOrPaidTopic;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.custompushnotification.MyCloudRegistrationService;
import com.ojassoft.astrosage.misc.GetTagManagerDataService;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActNotificationCenter;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.AstroBusyAlertDialog;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.CongratulationDialog;
import com.ojassoft.astrosage.varta.dialog.FilterDialog;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet;
import com.ojassoft.astrosage.varta.dialog.UnlockedDialog;
import com.ojassoft.astrosage.varta.interfacefile.IBirthDetailInputFragment;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.BannerLinkModel;
import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.model.NextOfferBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.receiver.AstroLiveDataManager;
import com.ojassoft.astrosage.varta.receiver.OngoingAICallData;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.service.AIVoiceCallingService;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AgoraCallOngoingService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.ui.fragments.AIAstrologersFragment;
import com.ojassoft.astrosage.varta.ui.fragments.HomeFragment1;
import com.ojassoft.astrosage.varta.ui.fragments.ReadFragment;
import com.ojassoft.astrosage.varta.ui.fragments.RechargePopUpAfterFreeChat;
import com.ojassoft.astrosage.varta.ui.fragments.VartaHomeFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener, IBirthDetailInputFragment, VolleyResponse, GlobalRetrofitResponse {

    private static final int END_CHAT_VALUE = 5;
    private static final int NEXT_OFFER_API_RESPONSE = 22;
    private static final int GET_FIREBASE_AUTH_TOKEN = 23;
    private static final int PERMISSION_REQ_CODE = 1;
    public static int BACK_FROM_PROFILECHATDIALOG = 2000;
    public static boolean IS_SHOW_ACCEPT_DIALOG = false;
    public static boolean IS_ASTROLOGER_DISCONNECT = false;
    public static String CHANNEL_ID = "";
    public static String isGzipDesabled = "";
    private static Activity mainActivity;
    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    public Dialog acceptanceDialogg = null;
    public boolean isDontShowBottomPopUnderDialog = false;
    public ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;
    public RelativeLayout containerLayout;
    public String CHAT_TYPE;
    public TreeSet<Integer> statusMessageSetHistory;
    public boolean showDialogOnce;
    LinearLayout navView;
    UserProfileData userProfileData;
    String isFromScreen = "";
    LinearLayout walletLayout, backTitleLayout;
    TextView walletPriceTxt, tvTitle;
    RelativeLayout walletBoxLayout, vartaIconLayout;
    ImageView ivBack;
    ImageView support_btn;
    RequestQueue queue;
    TextView demoTxt;
    CustomProgressDialog pd;
    Toolbar toolbarChat, toolbar;
    TextView typingTextview;
    ImageView ivbackChat;
    ImageView filter_btn;
    CircularNetworkImageView astrologerProfilePic;
    TextView titleTextChat, timerTextview;
    TextView endChatButton;
    View viewInc;
    TextView tvTimer;
    AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
    CallInitiatedDialog callInitiatedDialog;
    Dialog acceptanceDialog;
    ImageView search_btn, notification_btn;
    View chatInitiateInfoLayout;
    View ongoingChatInfoLayout;

    private final Observer<Intent> ongoingChatObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            //Log.d("test_ongoing_chat","intent ==>>"+intent);
            String remTime = intent.getStringExtra("rem_time");
            String CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
            String chatJsonObject = intent.getStringExtra("connect_chat_bean");
            String astrologerName = intent.getStringExtra("astrologer_name");
            String astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
            String astrologerId = intent.getStringExtra("astrologer_id");
            String userChatTime = intent.getStringExtra("userChatTime");
            String chatinitiatetype = intent.getStringExtra(CGlobalVariables.CHATINITIATETYPE);
            // Log.d("test_ongoing_chat","remTime ==>>"+remTime);
//            Log.d("test_ongoing_chat","CHANNEL_ID ==>>"+CHANNEL_ID);
//            Log.d("test_ongoing_chat","chatJsonObject ==>>"+chatJsonObject);
//            Log.d("test_ongoing_chat","astrologerName ==>>"+astrologerName);
//            Log.d("test_ongoing_chat","astrologerProfileUrl ==>>"+astrologerProfileUrl);
//            Log.d("test_ongoing_chat","astrologerId ==>>"+astrologerId);

            if (!remTime.equals("00:00:00")) {
                if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                    CUtils.joinOngoingChatLayoutView(DashBoardActivity.this, remTime, CHANNEL_ID, chatJsonObject, astrologerName, astrologerProfileUrl, astrologerId, userChatTime,chatinitiatetype);
                    ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                }
            } else {
                // OngoingChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                ongoingChatInfoLayout.setVisibility(View.GONE);
            }
        }
    };
    ImageView cross_chat_initiate_view;
    /**
     * Bottom Navigation View
     */
    /*
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_HOME, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            if (viewInc.getVisibility() != View.VISIBLE) {
                                openAstrosageHomeActivity();
                            }
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_CALL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            applyCallChatFilter(FILTER_TYPE_CALL);
                            return true;
                        case R.id.navigation_share:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_LIVE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            fabActions();
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            applyCallChatFilter(FILTER_TYPE_CHAT);
                            return true;
                        case R.id.navigation_profile:
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_ACCOUNT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            isUserLogin(new ProfileFragment(), CGlobalVariables.PROFILE_SCRREN);
                            return true;
                    }
                    return false;
                }
            };
    */
    private BroadcastReceiver receiver;
    private Context context;
    private boolean isFromDeleteChannel = false;
    private NextOfferBean nextOfferBean;
    private String bannerUrl = "";
    private boolean isNotification;

    private android.app.AlertDialog alert;
    /*SplitInstallStateUpdatedListener listener =
            new SplitInstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(SplitInstallSessionState state) {

                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            int totalBytes = (int) state.totalBytesToDownload();
                            int progress = (int) state.bytesDownloaded();
                            updateInstallLiveProgressBar(CUtils.convertBytesToMB(totalBytes), CUtils.convertBytesToMB(progress));
                            break;
                        case SplitInstallSessionStatus.INSTALLING:
                            break;
                        case SplitInstallSessionStatus.DOWNLOADED:
                            break;

                        case SplitInstallSessionStatus.INSTALLED:
                            hideInstallLiveProgressBar();
                            openLiveStramingScreen();
                            break;

                        case SplitInstallSessionStatus.CANCELED:
                            hideInstallLiveProgressBar();
                            CUtils.showSnackbar(containerLayout, "Live content installation cancelled", DashBoardActivity.this);
                            break;

                        case SplitInstallSessionStatus.PENDING:
                            break;

                        case SplitInstallSessionStatus.FAILED:
                            hideInstallLiveProgressBar();
                            CUtils.showSnackbar(containerLayout, "Live content installation failed. Error code: " + state.errorCode(), DashBoardActivity.this);
                            break;
                    }
                }
            };*/
    private boolean flag;

    private final Observer<String> callChatObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            try {
                if (s.contains(CGlobalVariables.ASTRO_NO_ANSWER) && flag) {
                    flag = false;
                    chatInitiateInfoLayout.setVisibility(View.GONE);
                    String callSource = "";
                    AstrologerDetailBean astroBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
                    if(astroBean != null){
                        callSource = astroBean.getCallSource();
                        if(callSource == null) callSource = "";
                    }
                    String isFreeHumanRandomChat = s.split("@")[1];
                    if(isFreeHumanRandomChat.equals("true")) {
                        CUtils.startRandomAIChatAfterAstroNoAnswer(DashBoardActivity.this);
                    } else if(callSource.equalsIgnoreCase(CGlobalVariables.HUMAN_CONTINUE_CHAT_DIALOG)){
                        //connect chat to AI astro
                        if(AstrosageKundliApplication.lastChatAIAstrologerDetailBean!=null ) {
                            AstrosageKundliApplication.lastChatAIAstrologerDetailBean.setCallSource(CGlobalVariables.HUMAN_CONTINUE_CHAT_FALLBACK_TO_SAME_AI);
                            ChatUtils.getInstance(DashBoardActivity.this).initAIChat(AstrosageKundliApplication.lastChatAIAstrologerDetailBean);
                        }
                    }else {
                        AstroBusyAlertDialog astroBusyAlertDialog = AstroBusyAlertDialog.newInstance(AstrosageKundliApplication.astrologerDetailBeanForBusyDialog, s);
                        astroBusyAlertDialog.show(DashBoardActivity.this.getSupportFragmentManager(), "astroBusyAlertDialog");

                        //  CUtils.showSnackbar(containerLayout, getResources().getString(R.string.astrologer_not_answer), DashBoardActivity.this);
                    }
                } else if (s.equals("-1") && flag) {
                    flag = false;
                    chatInitiateInfoLayout.setVisibility(View.GONE);
                } else {
                    String internationalCharges = "0.0";
                    if (s.contains("###")) {
                        String str[] = s.split("###");
                        s = str[0];
                        internationalCharges = str[1];
                    }
                    long remTime = Long.parseLong(s);

                    String leftTime = "" + remTime;
                    if (remTime > 60) {
                        leftTime = getResources().getString(R.string.wait_time) + " " + leftTime + " " + getResources().getString(R.string.sec);
                    } else {
                        leftTime = getResources().getString(R.string.wait_time_almost);
                    }
                    if (remTime > 0) {
                        flag = true;
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                            CUtils.setChatInitiateLayout(DashBoardActivity.this, AstrosageKundliApplication.selectedAstrologerDetailBean, leftTime, internationalCharges,remTime);
                            chatInitiateInfoLayout.setVisibility(View.VISIBLE);
                        } else {
                            chatInitiateInfoLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }catch (Exception e){
                //
            }

        }
    };
    public static Activity getMainActivity() {
        return mainActivity;
    }
    private String currentFragment;
    private void applyCallChatFilter(int filterType) {
        astroListFilterType = filterType;
        if (filterType == FILTER_TYPE_CALL) {
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
            createSession(this, VARTA_HOME_BOTTOM_BAR_CALL_PARTNER_ID);
        } else if (filterType == FILTER_TYPE_CHAT) {
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
            createSession(this, VARTA_HOME_BOTTOM_BAR_CHAT_PARTNER_ID);
        }
        openWantedFragment();
    }

    public void fabActions() {
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanag
                openKundliActivity(this);
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, VARTA_HOME_BOTTOM_BAR_KUNDLI_PARTNER_ID);
            } else {
                if (liveAstrologerModelArrayList != null && !liveAstrologerModelArrayList.isEmpty()) {
                    CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                    createSession(this, VARTA_HOME_BOTTOM_BAR_LIVE_PARTNER_ID);
                    checkPermissions(liveAstrologerModelArrayList.get(0));
                } else if (ActAppModule.liveAstrologerModelArrayList != null && !ActAppModule.liveAstrologerModelArrayList.isEmpty()) {
                    CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                    createSession(this, VARTA_HOME_BOTTOM_BAR_LIVE_PARTNER_ID);
                    checkPermissions(ActAppModule.liveAstrologerModelArrayList.get(0));
                } else {
                    startActivity(new Intent(DashBoardActivity.this, AllLiveAstrologerActivity.class));
                }
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        CUtils.getRobotoFont(DashBoardActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startGcmService();

        context = this;
        mainActivity = this;
        //manager = SplitInstallManagerFactory.create(this);
        viewInc = findViewById(R.id.inc_return_chat);
        viewInc.setOnClickListener(this);
        viewInc.setVisibility(View.GONE);
        CGlobalVariables.fromLanguage = 1;
        isNotification = getIntent().getBooleanExtra("isNotification", false);
        astroListFilterType = getIntent().getIntExtra(KEY_FILTER_TYPE, 0);
        if (isNotification) {
            //startTagManagerService();
        }

        tvTimer = viewInc.findViewById(R.id.timer);
        search_btn = findViewById(R.id.search_btn);
        notification_btn = findViewById(R.id.notification_btn);
        notification_btn.setVisibility(View.VISIBLE);
        navView = findViewById(R.id.nav_view);
        containerLayout = findViewById(R.id.container_layout);
        walletLayout = findViewById(R.id.wallet_layout);
        walletPriceTxt = findViewById(R.id.wallet_price_txt);
        walletBoxLayout = findViewById(R.id.wallet_box_layout);
        vartaIconLayout = findViewById(R.id.varta_icon_layout);
        backTitleLayout = findViewById(R.id.back_title_layout);
        toolbarChat = findViewById(R.id.toolbar_chat);
        typingTextview = findViewById(R.id.typingTextview);
        typingTextview.setVisibility(View.GONE);

        toolbar = findViewById(R.id.toolbar);
        filter_btn = findViewById(R.id.filter_btn);
        ivbackChat = findViewById(R.id.iv_close_chat);
        astrologerProfilePic = findViewById(R.id.astrologer_profile_pic);
        titleTextChat = findViewById(R.id.title_text_chat);
        timerTextview = findViewById(R.id.timer_textview);
        endChatButton = findViewById(R.id.end_chat_button);
        ivbackChat.setOnClickListener(this);
        endChatButton.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        support_btn = findViewById(R.id.support_btn);
        demoTxt = findViewById(R.id.demo_txt);
        demoTxt.setVisibility(View.GONE);

        FontUtils.changeFont(DashBoardActivity.this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        //FontUtils.changeFont(DashBoardActivity.this, walletPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        walletLayout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        support_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        filter_btn.setOnClickListener(this);
        notification_btn.setOnClickListener(this);
        queue = VolleySingleton.getInstance(DashBoardActivity.this).getRequestQueue();

        if (getIntent().getExtras() != null) {
            userProfileData = (UserProfileData) getIntent().getExtras().get(CGlobalVariables.USER_DATA);
        }
        if (userProfileData == null) {
            userProfileData = CUtils.getUserSelectedProfileFromPreference(DashBoardActivity.this);
        }
        //added by abhishek for open notification url
        actionOnIntent(getIntent());
        handleRechargeData(getIntent());
        openWantedFragment();
        initReceiver();
        getFirebaseAccessToken();
        chatInitiateInfoLayout = findViewById(R.id.chat_initiate_info_layout);
        ongoingChatInfoLayout = findViewById(R.id.join_ongoing_chat_layout);
        ongoingAICallLayout = findViewById(R.id.call_initiate_info_layout);
        chatInitiateInfoLayout.setVisibility(View.GONE);
        cross_chat_initiate_view = chatInitiateInfoLayout.findViewById(R.id.cross_chat_initiate_view);
        cross_chat_initiate_view.setOnClickListener(this);

        checkConnectFreeCallChat();
        CUtils.subscribeRegisteredFreeOrPaidTopic(this, 0);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishActivity();
            }
        });
        //com.ojassoft.astrosage.utils.CUtils.syncChartWithCloud(DashBoardActivity.this, true);
    }

    private void checkConnectFreeCallChat(){
        if(AstrosageKundliApplication.connectAiChatAfterLogin){
            AstrosageKundliApplication.connectAiChatAfterLogin = false;
            CUtils.initiateRandomAiChat(this,AstrosageKundliApplication.apiCallingSource, AstrosageKundliApplication.chatCallconfigType);
        } if(AstrosageKundliApplication.connectAiCallAfterLogin){
            AstrosageKundliApplication.connectAiCallAfterLogin = false;
            CUtils.initiateRandomAiCall(this,AstrosageKundliApplication.apiCallingSource, AstrosageKundliApplication.chatCallconfigType);
        } else if(AstrosageKundliApplication.connectHumanChatAfterLogin){
            AstrosageKundliApplication.connectHumanChatAfterLogin = false;
            CUtils.initiateRandomChat(this,AstrosageKundliApplication.apiCallingSource, AstrosageKundliApplication.chatCallconfigType);
        }
    }

    public void openWantedFragment() {
        try {
            if(astroListFilterType == 0){
                astroListFilterType = FILTER_TYPE_CHAT;
            }
            boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
            if (astroListFilterType == FILTER_TYPE_CALL && isAIChatDisplayed) { //AI Astrologer
                if (!getSupportFragmentManager().isDestroyed()) {
                    openFragment(AIAstrologersFragment.newInstance(CGlobalVariables.DASHBOARD), "AIASTROFRAGMENT");
                } else {
                    AIAstrologersFragment fragment = AIAstrologersFragment.getInstance();
                    if (fragment != null && fragment.isAdded()) {
                        fragment.getData(CUtils.astroListFilterType);
                    }
                }
                changeToolbar(CGlobalVariables.AI_ASTRO_FRAGMENT);
            } else {
                if (!getSupportFragmentManager().isDestroyed()) {
                    openFragment(HomeFragment1.newInstance(CGlobalVariables.DASHBOARD), "HOMEFRAGMENT");
                } else {
                    HomeFragment1 fragment = HomeFragment1.getInstance();
                    if (fragment != null && fragment.isAdded()) {
                        fragment.getData(CUtils.astroListFilterType);
                    }
                }
                changeToolbar(CGlobalVariables.HOMEFRAGMENT);
            }
        } catch (Exception e) {
            Toast.makeText(DashBoardActivity.this, "Exception2=" + e, Toast.LENGTH_LONG).show();
        }
        setBottomNavigationText(CUtils.getUserLoginStatus(this));
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Log.e("SAN CI DA ", " onReceive inside");
                String message = intent.getStringExtra(CGlobalVariables.BROAD_MSG_RESULT);
                String title = intent.getStringExtra(CGlobalVariables.BROAD_TITLE_RESULT);
                String urlText = intent.getStringExtra(CGlobalVariables.BROAD_LINK_RESULT);
                String consultationType = intent.getStringExtra(CGlobalVariables.KEY_CONSULTATION_TYPE);
                try {
                    //Log.e("SAN CI DA ", " onReceive urlText => " + urlText);
                    if (urlText != null && urlText.length() > 0) {
                        if (urlText.contains(CGlobalVariables.ASTROLOGER_ACCEPT_CHAT_REQUEST_NOTIFICATION)) {
                            // do nothing
                        } else {
                            Intent intent1 = new Intent(DashBoardActivity.this, AstrologerDescriptionActivity.class);
                            intent1.putExtra("phoneNumber", CUtils.getUserID(DashBoardActivity.this));
                            intent1.putExtra("urlText", urlText);
                            intent1.putExtra("msg", message);
                            intent1.putExtra("title", title);
                            intent1.putExtra("fromDashboard", true);
                            intent1.putExtra(CGlobalVariables.KEY_CONSULTATION_TYPE, consultationType);
                            startActivity(intent1);
                        }
                    } else {
                        if ((message != null && message.length() > 0) || (title != null && title.length() > 0)) {
                            try {
                                if (CUtils.getStringData(DashBoardActivity.this, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "").length() > 0) {
                                    callMsgDialogData(message, title, true, CGlobalVariables.CHAT_CLICK);
                                } else {
                                    callMsgDialogData(message, title, true, CGlobalVariables.CALL_CLICK);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e("SAN CI DA ", " onReceive exp=>" + e.toString());
                }
            }
        };
        if (receiver != null) {
            LocalBroadcastManager.getInstance(DashBoardActivity.this).registerReceiver((receiver),
                    new IntentFilter(CGlobalVariables.CALL_BROAD_ACTION)
            );
        }
    }

    public void popUnderClicked() {
        String firstConsultType = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE, "");
        String defaultFreeConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringBeforeHyphen(firstConsultType);
        if (defaultFreeConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT)) {
            CUtils.initiateRandomAiChat(this, AstrosageKundliApplication.apiCallingSource, firstConsultType);
        } else if (defaultFreeConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)) {
            CUtils.initiateRandomAiCall(this, AstrosageKundliApplication.apiCallingSource, firstConsultType);
        } else if (defaultFreeConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT)) {
            CUtils.initiateRandomChat(this, AstrosageKundliApplication.apiCallingSource, firstConsultType);
        }

        //CUtils.initiateRandomChat(this,AstrosageKundliApplication.apiCallingSource,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
    }

    private void startTagManagerService() {
        try {
            startService(new Intent(DashBoardActivity.this, GetTagManagerDataService.class));
        } catch (Exception e) {
            //
        }
    }

    public void getNextRechargeFromApi() {
        if (CUtils.isConnectedWithInternet(DashBoardActivity.this) &&
                CUtils.getUserLoginStatus(DashBoardActivity.this)) {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, NEXT_RECHARGE,
//                    DashBoardActivity.this, false, getNextRechargeParams(), NEXT_OFFER_API_RESPONSE).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.nextRecharge(getNextRechargeParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.body()!=null){
                        try {
                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if (jsonObject.has("status")) {
                                String status = jsonObject.getString("status");

                                if (status.equals("1")) {
                                    Gson gson = new Gson();
                                    nextOfferBean = gson.fromJson(jsonObject.toString(), NextOfferBean.class);
                                    if (!isDontShowBottomPopUnderDialog) {
                                        showBottomSheet(nextOfferBean);
                                    }
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
            });
        }
    }

    public Map<String, String> getNextRechargeParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(DashBoardActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(DashBoardActivity.this)); //"8860085780"
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(DashBoardActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(DashBoardActivity.this));
        headers.put(CGlobalVariables.DEVICE_ID,CUtils.getMyAndroidId(DashBoardActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    private void updateData(boolean isLogin) {
        try {
            if (isLogin) {
                walletLayout.setVisibility(View.VISIBLE);
                walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(DashBoardActivity.this)));
                ////Log.e("LoadMore ELIGIBLE ", CUtils.getIsEligibleForBonusStatus(DashBoardActivity.this));
                if (CUtils.getIsEligibleForBonusStatus(DashBoardActivity.this).equals("1")) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_HUNDRED_RS_BONUS_DIALOG,
                            CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    Dialog congratulationDialog = new CongratulationDialog(DashBoardActivity.this, CUtils.getAmountOnDialog(DashBoardActivity.this), true).congratsDialog();
                    congratulationDialog.show();
                    CUtils.saveLoginDetailInPrefs(context, CUtils.getUserID(context), isLogin, CUtils.getWalletRs(DashBoardActivity.this), "0");
                } else if (CUtils.getDataForOneRsDialog(DashBoardActivity.this).equals("1")) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_ONE_RS_DIALOG,
                            CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    isDontShowBottomPopUnderDialog = true;
                    Dialog congratulationDialog = new CongratulationDialog(DashBoardActivity.this, CUtils.getAmountOnDialog(DashBoardActivity.this), false).congratsDialog();
                    congratulationDialog.show();
                    CUtils.setDataForOneRsDialog(DashBoardActivity.this, "0");
                } else if (CUtils.getDataForFreeSessionDialog(DashBoardActivity.this).equals("1")) {
                    isDontShowBottomPopUnderDialog = true;
                    UnlockedDialog congratulationUnlockDialog = new UnlockedDialog(DashBoardActivity.this);
                    congratulationUnlockDialog.showDialog().show();
                    AstrosageKundliApplication.isOpenVartaPopup = true;
                    CUtils.setDataForFreeSessionDialog(DashBoardActivity.this, "0");
                }
            } else {
                walletLayout.setVisibility(View.GONE);
            }
            setBottomNavigationText(isLogin);
        } catch (Exception e) {
            //
        }
    }

    /*public void setBottomNavigationText(boolean isLoginData) {

        // get menu from navigationView
        Menu menu = navView.getMenu();

        // find MenuItem you want to change
        MenuItem navHome = menu.findItem(R.id.navigation_home);
        MenuItem navRead = menu.findItem(R.id.navigation_recharge);
        MenuItem navNotificaton = menu.findItem(R.id.navigation_share);
        MenuItem navChat = menu.findItem(R.id.navigation_notification);
        MenuItem navMyaccount = menu.findItem(R.id.navigation_profile);

        // set new title to the MenuItem
        navHome.setTitle(getResources().getString(R.string.title_home));

        if (isLoginData) {
            navMyaccount.setTitle(getResources().getString(R.string.account));
        } else {
            navMyaccount.setTitle(getResources().getString(R.string.sign_up));
        }

        if (astroListFilterType == FILTER_TYPE_CALL) {
            navRead.setChecked(true);
        } else if (astroListFilterType == FILTER_TYPE_CHAT) {
            navChat.setChecked(true);
        } else {
            navHome.setChecked(true);
        }
    }*/

    public void setBottomNavigationText(boolean isLoginData) {


        // find MenuItem you want to change
        ImageView navHomeImg = navView.findViewById(R.id.imgViewHome);
        TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        ImageView navChatImg = navView.findViewById(R.id.imgViewChat);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);
        // set new title to the MenuItem
        navHomeTxt.setText(getResources().getString(R.string.title_home));

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if(isAIChatDisplayed){
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_selected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.ic_call_icon_new_filled);
        }

        if (isLoginData) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }

        if (astroListFilterType == FILTER_TYPE_CALL) {
            if(isAIChatDisplayed){
                Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_selected).into(navCallImg);
                navChatImg.setImageResource(R.drawable.chat_icon_new);
            } else {
                navCallImg.setImageResource(R.drawable.ic_call_icon_new_filled);
                navChatImg.setImageResource(R.drawable.chat_icon_new);
            }
        } else if (astroListFilterType == FILTER_TYPE_CHAT) {
            if(isAIChatDisplayed){
                Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
                navChatImg.setImageResource(R.drawable.chat_icon_new_filled);
            }else {
                navChatImg.setImageResource(R.drawable.chat_icon_new_filled);
                navCallImg.setImageResource(R.drawable.ic_call_icon_new);
            }
        } else {
            navHomeImg.setImageResource(R.drawable.ic_home_new_filled);
        }
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }


    public void changeToolbar(String fragment) {
        if (fragment.equals(CGlobalVariables.VARTA_HOME_FRAGMENT) || fragment.equals(CGlobalVariables.HOMEFRAGMENT)
                || fragment.equals(CGlobalVariables.AI_ASTRO_FRAGMENT)) {
            navView.setVisibility(View.VISIBLE);
            //findViewById(R.id.fabMain).setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            vartaIconLayout.setVisibility(View.VISIBLE);
            backTitleLayout.setVisibility(View.VISIBLE);
            if (fragment.equals(CGlobalVariables.VARTA_HOME_FRAGMENT)) {
                support_btn.setVisibility(View.VISIBLE);
                filter_btn.setVisibility(View.GONE);
            } else if (fragment.equals(CGlobalVariables.HOMEFRAGMENT)) {
                filter_btn.setVisibility(View.VISIBLE);
                support_btn.setVisibility(View.GONE);
            } else if (fragment.equals(CGlobalVariables.AI_ASTRO_FRAGMENT)) {
                filter_btn.setVisibility(View.GONE);
                support_btn.setVisibility(View.VISIBLE);
            }
            if (CUtils.getUserLoginStatus(DashBoardActivity.this)) {
                walletBoxLayout.setVisibility(View.VISIBLE);
                walletLayout.setVisibility(View.VISIBLE);
                walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(DashBoardActivity.this)));
            } else {
                walletBoxLayout.setVisibility(View.INVISIBLE);
            }
            toolbarChat.setVisibility(View.GONE);
            navView.setVisibility(View.VISIBLE);
        } else if (fragment.equals(CGlobalVariables.MAINCHATFRAGMENT)) {
            toolbarChat.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            navView.setVisibility(View.GONE);
        }
    }

    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(DashBoardActivity.this);
        if (!isLogin) {
            Intent intent = new Intent(DashBoardActivity.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else {
            if (whichScreen.equals(CGlobalVariables.RECHARGE_SCRREN)) {
                openWalletScreen("dashboard");
            } else {
                changeToolbar(CGlobalVariables.PROFILE_FRAGMENT);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(DashBoardActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(DashBoardActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Method to open Fragments
     *
     * @param fragment
     */
    public void openFragment(Fragment fragment, String fragTag) {
        //currentFragment = fragTag;
        CUtils.saveStringData(DashBoardActivity.this, "CHAT_CLICK", "");
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment, fragTag);
            transaction.addToBackStack(null);
            transaction.commit();
            currentFragment = fragTag;
        } catch (Exception e) {
            isFromDeleteChannel = true;
        }
    }

    private void finishActivity() {
        try {
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
            if (taskList.get(0).numActivities == 1) {
                openAstrosageHomeActivity();
            } else {
                finish();
            }
        } catch (Exception e) {
            openAstrosageHomeActivity();
        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        //
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_layout:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VARTA_HOME_WALLET_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(DashBoardActivity.this,CGlobalVariables.VARTA_HOME_WALLET_CLICK_PARTNER_ID);
                isUserLogin(new ReadFragment(), CGlobalVariables.RECHARGE_SCRREN);
                break;

            case R.id.ivBack:
                if (viewInc.getVisibility() != View.VISIBLE) {
                    //onBackPressed();
                    finishActivity();
                }
                break;


            case R.id.support_btn:
                Intent supportIntent = new Intent(DashBoardActivity.this, GenerateTicketActivity.class);
                startActivity(supportIntent);
                break;
            case R.id.search_btn:
                Intent intent = new Intent(DashBoardActivity.this, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.filter_btn:
                openFilter();
                break;

            case R.id.notification_btn:
                //composeNewEmail();
                Intent intentNotif = new Intent(DashBoardActivity.this, ActNotificationCenter.class);
                startActivity(intentNotif);
                break;
            case R.id.cross_chat_initiate_view:
                Log.e("SAN ", " DA cross_chat_initiate_view " );
                cross_chat_initiate_view.setEnabled(false);
                openChatRequestCancel();

                break;
        }
    }

    private void openChatRequestCancel() {

        Log.e("SAN ", "DA openChatRequestCancel() X click ");

        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_button_click", AstrosageKundliApplication.currentEventType, "");
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.end_chat_confirm_dialog, null);
        builder.setView(dialogView);

        TextView end_chat_confirm_text = dialogView.findViewById(R.id.end_chat_confirm_text);
        FontUtils.changeFont(DashBoardActivity.this, end_chat_confirm_text, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
        TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
        alert = builder.create();
        Objects.requireNonNull(alert.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        end_chat_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_btn_yes_click", AstrosageKundliApplication.currentEventType, "");
                alert.dismiss();
                Log.e("SAN ", "DA openChatRequestCancel() X click yes ");
                clickCrossChatRequest();
            }
        });

        end_chat_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("SAN ", "DA openChatRequestCancel() X click no ");
                cross_chat_initiate_view.setEnabled(true);
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_btn_no_click", com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                alert.dismiss();
            }
        });
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_dialog_show", AstrosageKundliApplication.currentEventType, "");
        alert.setCanceledOnTouchOutside(false);
        alert.show();

    }

    private void clickCrossChatRequest(){

        if (CUtils.getSelectedAstrologerID(getApplicationContext()) != null && CUtils.getSelectedAstrologerID(getApplicationContext()).length() > 0 &&
                CUtils.getSelectedChannelID(getApplicationContext()) != null && CUtils.getSelectedChannelID(getApplicationContext()).length() > 0) {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_CANCELED;
            CUtils.changeFirebaseKeyStatus(CUtils.getSelectedChannelID(getApplicationContext()), CGlobalVariables.CANCELLED, true, CGlobalVariables.USER_CANCELLED);
            chatCompleted(CGlobalVariables.END_CHAT_URL,CUtils.getSelectedChannelID(getApplicationContext()),CGlobalVariables.USER_CANCELLED,CGlobalVariables.USER_BUSY,CUtils.getSelectedAstrologerID(getApplicationContext()) );
        }
        if (CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
            if (CUtils.connectAgoraCallBean != null && AstrosageKundliApplication.selectedAstrologerDetailBean != null)
                CUtils.changeFirebaseKeyStatus(CUtils.connectAgoraCallBean.getCallsid(), CGlobalVariables.CANCELLED, true, CGlobalVariables.USER_CANCELLED);
            chatCompleted(CGlobalVariables.END_CALL_URL,CUtils.connectAgoraCallBean.getCallsid(), CGlobalVariables.USER_CANCELLED, CGlobalVariables.USER_BUSY,AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
        }

    }


    public void openFilter() {
        try {
            /*if(currentFragment != null && currentFragment.equalsIgnoreCase("AIASTROFRAGMENT")){
                AIFilterDialog filterDialog = AIFilterDialog.getInstance();
                filterDialog.show(getSupportFragmentManager(), AIFilterDialog.TAG);
            } else {*/
                try {
                    FilterDialog filterDialog = FilterDialog.getInstance();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag( FilterDialog.TAG);
                    if (prev != null) {
                        getSupportFragmentManager().beginTransaction().remove(prev).commitAllowingStateLoss();
                        getSupportFragmentManager().executePendingTransactions(); // Ensure it's removed before adding
                    }
                    filterDialog.show(getSupportFragmentManager(), FilterDialog.TAG);
                }catch (Exception e){
                    //
                }


            //}
        } catch (Exception e) {
            //
        }

    }

    public void callSnakbar(String msg) {
        CUtils.showSnackbar(containerLayout, msg, DashBoardActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (manager != null) {
            manager.registerListener(listener);
        }*/

        AstrosageKundliApplication.isDasboardActivityVisible = true;
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        CUtils.getRobotoFont(DashBoardActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);

        /*if (CGlobalVariables.isChatCompleteRefreshHome) {
            changeToolbar(CGlobalVariables.VARTA_HOME_FRAGMENT);
            CGlobalVariables.isChatCompleteRefreshHome = false;
            openHomeFragment(VartaHomeFragment.newInstance(CGlobalVariables.DASHBOARD), "VARTAHOMEFRAGMENT");
        }
        if (isFromDeleteChannel) {
            isFromDeleteChannel = false;
            openHomeFragment(VartaHomeFragment.newInstance(CGlobalVariables.DASHBOARD), "VARTAHOMEFRAGMENT");
        }*/
        checkLoginAndRedirectToBannerLink();
        showOrHideCallChatInitiate();
        showOrHideOngoingChat();
        showOrHideOngoingCall();
        showHideAICallOngoing();
    }
    Observer<Intent> aicallObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            try {
                if (intent != null) {
                    ongoingAICallLayout.setVisibility(View.VISIBLE);
                    setDataOngoingAICall(intent);
                } else {
                    ongoingAICallLayout.setVisibility(View.GONE);
                }
            }catch (Exception e){
                //
            }

        }
    };

    public void setDataOngoingAICall(Intent intent){
        String astrologerName = intent.getStringExtra(com.ojassoft.astrosage.utils.CGlobalVariables.ASTROLOGER_NAME);
        String astrologerProfileUrl = intent.getStringExtra(com.ojassoft.astrosage.utils.CGlobalVariables.ASTROLOGER_PROFILE_URL);
        String remenTimeDuration = intent.getStringExtra("time_remaining");
        String callSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
        String callDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
        String astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
        boolean micStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.MIC_MUTE_STATUS;

        FontUtils.changeFont(this, callAstroName, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        callAstroName.setText(astrologerName.replace("+"," "));
        String remtime = getResources().getString(R.string.time_remaining)+remenTimeDuration;
        callRemenTime.setText(remtime);
        callMicStatus.setActivated(micStatus);
        String callingAstroImage = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
        Glide.with(AstrosageKundliApplication.getAppContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(callAstroProfileImage);

        ongoingAICallLayout.setOnClickListener(view -> {
            Intent activityIntent = new Intent(DashBoardActivity.this,AIVoiceCallingActivity.class);
            activityIntent.putExtra(CGlobalVariables.IS_FROM_RETURN_TO_CALL,true);
            activityIntent.putExtra(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            activityIntent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            activityIntent.putExtra(CGlobalVariables.ASTROLOGER_ID, astrologerId);
            activityIntent.putExtra(CGlobalVariables.AGORA_CALLS_ID, callSId);
            activityIntent.putExtra("time_remaining", remenTimeDuration);
            activityIntent.putExtra(CGlobalVariables.AGORA_CALL_DURATION, callDuration);
            startActivity(activityIntent);
        });
    }

    ImageView callAstroProfileImage;
    TextView callRemenTime;
    TextView callAstroName;
    ImageView callMicStatus;
    ImageView callEnd;
    View ongoingAICallLayout;

    public void showHideAICallOngoing(){
        if(com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(AIVoiceCallingService.class)){
            callAstroProfileImage = ongoingAICallLayout.findViewById(R.id.callAstroProfileImage);
            callRemenTime = ongoingAICallLayout.findViewById(R.id.callRemTime);
            callAstroName = ongoingAICallLayout.findViewById(R.id.callAstroName);
            callMicStatus = ongoingAICallLayout.findViewById(R.id.iv_mic_btn);
            callEnd = ongoingAICallLayout.findViewById(R.id.iv_end_call_btn);
            OngoingAICallData.getOngoingChatLiveData().observe(this,aicallObserver);

            callEnd.setOnClickListener((v)->{
                Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FINISH_CALL_ACTIVITY_ACTION);
                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.REMARKS, com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_ENDED);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            });

            callMicStatus.setOnClickListener((v)->{
                callMicStatus.setActivated(!CGlobalVariables.MIC_MUTE_STATUS);
                Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MIC_ON_OFF_ACTIVITY_ACTION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            });

        }else{
            OngoingAICallData.getOngoingChatLiveData().removeObserver(aicallObserver);
            ongoingAICallLayout.setVisibility(View.GONE);
        }
    }

    public void showOrHideCallChatInitiate() {
        flag = false;
        cross_chat_initiate_view.setEnabled(true);

        if (CUtils.checkServiceRunning(AstroAcceptRejectService.class)||CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
            AstroLiveDataManager.getAstroAcceptLiveData().observe(this, callChatObserver);
        } else {
            if(callChatObserver !=null) {
                AstroLiveDataManager.getAstroAcceptLiveData().removeObserver(callChatObserver);
            }
            chatInitiateInfoLayout.setVisibility(View.GONE);
        }

    }
    public void showOrHideOngoingChat() {
        if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
            OngoingCallChatManager.getOngoingChatLiveData().observe(this, ongoingChatObserver);
        } else {
            if (ongoingChatObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
            }
            ongoingChatInfoLayout.setVisibility(View.GONE);
        }

    }
    public void showOrHideOngoingCall() {
        Log.d("agoraCallOngoing","showOrHideOngoingCall == > called Des");
        if (CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
            Log.d("agoraCallOngoing","showOrHideOngoingCall == > called showOrHideOngoingCall Des");

            OngoingCallChatManager.getOngoingChatLiveData().observe(this,ongoingCallObserver);
        } else {
            if(ongoingCallObserver !=null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingCallObserver);
            }
            ongoingChatInfoLayout.setVisibility(View.GONE);
        }

    }
    private final Observer<Intent> ongoingCallObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            try {
                String remTime = intent.getStringExtra("rem_time");
                boolean micStatus = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_MIC_STATUS, true);
                boolean videoStatus = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, true);
                String consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
                String agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
                String agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
                String agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
                String astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
                String astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
                String agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
                String astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
                if (!remTime.equals("00:00:00")) {
                    CUtils.joinOngoingCallLayoutView(DashBoardActivity.this, remTime,agoraCallSId,agoraToken,agoraTokenId,astrologerName,astrologerProfileUrl,astrologerId,agoraCallDuration,consultationType,micStatus,videoStatus);
                    ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                } else {
                    // OngoingChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                    ongoingChatInfoLayout.setVisibility(View.GONE);
                }
            }catch (Exception e){
                //
            }

        }
    };
    private void openHomeFragment(Fragment fragment, String fragTag) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment, fragTag);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openCalendar(BeanDateTime beanDateTime) {

    }

    @Override
    public void openTimePicker(BeanDateTime beanDateTime) {

    }

    @Override
    public void openSearchPlace(BeanPlace b) {

    }

    @Override
    public void birthDetailInputFragmentCreated() {
    }

    public void actionOnResume() {
        boolean isLogin = CUtils.getUserLoginStatus(DashBoardActivity.this);
        try {
            if (isLogin) {
                getWalletPriceData();
            }
        } catch (Exception e) {
        }
        updateData(isLogin);
    }

    private void getWalletPriceData() {
        if (CUtils.isConnectedWithInternet(DashBoardActivity.this)) {
            //Log.d("TestLog", "getWalletPriceData()");
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_WALLET_PRICE_URL,
//                    DashBoardActivity.this, false, getParamsNew(), 1).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getWalletBalance(getParamsNew());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null ) {//{"userphone":"7838624890", "userbalancce":"120.25"}
                        try {
                            String myResponse = response.body().string();
                            JSONObject object = new JSONObject(myResponse);
                            String walletBal = object.getString("userbalancce");
                            if (walletBal != null && walletBal.length() > 0) {
                                CUtils.setWalletRs(DashBoardActivity.this, walletBal);
                                walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(DashBoardActivity.this)));
                                walletBoxLayout.setVisibility(View.VISIBLE);
                                walletLayout.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public Map<String, String> getParamsNew() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(DashBoardActivity.this));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(DashBoardActivity.this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(DashBoardActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(DashBoardActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(DashBoardActivity.this));
        //Log.e("SAN CI DA ", " getWalletPriceData params "  + headers );

        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.d("PaymentStatus", "response="+response);
        try {
            response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            //Log.e("SAN CI DA ", " onResponse Method exep " + e.toString()   );
        }
        try {
            if(method == METHOD_RECHARGE){
                handleWalletRechargeResponse(response);
            } else if (method == END_CHAT_VALUE) {
                hideProgressBar();
                CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
                try {
                    if (response != null && response.length() > 0) {
                        CGlobalVariables.chatTimerTime = 0;
                        AstrosageKundliApplication.chatTimerRemainingTime = 0;
                        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
                        AstrosageKundliApplication.chatJsonObject = "";
                        AstrosageKundliApplication.channelIdTempStore = "";
                        CUtils.saveAstrologerIDAndChannelID(DashBoardActivity.this, "", "");
                        if (AstrosageKundliApplication.currentConsultType != null) {
                            if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                                stopService(new Intent(context, AstroAcceptRejectService.class));
                            } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.AUDIO_CALL_TEXT)) {
                                stopService(new Intent(context, AgoraCallInitiateService.class));
                            } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.VIDEO_CALL_TEXT)) {
                                stopService(new Intent(context, AgoraCallInitiateService.class));
                            }
                        }
                        chatInitiateInfoLayout.setVisibility(View.GONE);
                        HomeFragment1 fragment = HomeFragment1.getInstance();
                        if (fragment != null && fragment.isAdded()) {
                            fragment.getAstrologersStatusPrice();
                        }
                    }
                } catch (Exception e) {
                }

            } else if (method == NEXT_OFFER_API_RESPONSE) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.has("status")) {
//                        String status = jsonObject.getString("status");
//
//                        if (status.equals("1")) {
//                            Gson gson = new Gson();
//                            nextOfferBean = gson.fromJson(jsonObject.toString(), NextOfferBean.class);
//                            if (!isDontShowBottomPopUnderDialog) {
//                                showBottomSheet(nextOfferBean);
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }

            } else if (method == GET_FIREBASE_AUTH_TOKEN) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.has("status")) {
//                        String status = jsonObject.getString("status");
//                        if (status.equals("1")) {
//                            String authToken = jsonObject.getString("token");
//                            firebaseSignIn(authToken);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            } else if (method == 2) {
                super.onResponse(response, method);
            } else if(method == 1) {
//                if (response != null && response.length() > 0) {//{"userphone":"7838624890", "userbalancce":"120.25"}
//                    try {
//                        JSONObject object = new JSONObject(response);
//                        String walletBal = object.getString("userbalancce");
//                        if (walletBal != null && walletBal.length() > 0) {
//                            CUtils.setWalletRs(DashBoardActivity.this, walletBal);
//                            walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(DashBoardActivity.this)));
//                            walletBoxLayout.setVisibility(View.VISIBLE);
//                            walletLayout.setVisibility(View.VISIBLE);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.d("TestLog", "error="+error);
        handleWalletRechargeError();
        hideProgressBar();
        CUtils.showVolleyErrorRespMessage(containerLayout, this, error, "1");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        openWantedFragment();
        checkAndRedirectToLiveStramingActivity(intent);
        actionOnIntent(intent); // added by abhishek for open notification url
        handleRechargeData(intent);
        checkConnectFreeCallChat();
    }

    private void handleRechargeData(Intent intent){
        if(intent == null) return;
        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
                Log.e("testLogs","isRecharged="+isRecharged);
                if (isRecharged) {
                    try {
                        if(QuickRechargeBottomSheet.getInstance()!=null){
                            QuickRechargeBottomSheet.getInstance().dismiss();
                        }
                        if(RechargePopUpAfterFreeChat.newInstance()!=null){
                            RechargePopUpAfterFreeChat.newInstance().dismiss();
                        }
                    }catch (Exception e){

                    }
                    try {
                        if(RechargeSuggestionBottomSheet.getInstance()!=null){
                            RechargeSuggestionBottomSheet.getInstance().dismiss();
                        }
                    }catch (Exception e){
                        //
                    }
                    String orderID = bundle.getString(CGlobalVariables.ORDER_ID);
                    String orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS);
                    String rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT);
                    String paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE);
                    String razorpayid = bundle.getString("razorpayid");
                    if (orderStatus.equals("0")) {
                        udatePaymentStatusOnserveronFailed(containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode);
                    } else {
                        udatePaymentStatusOnserver(this,containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode, razorpayid);
                    }
                }
            }
        } catch (Exception e){
            //
        }
    }
//    public void openWalletScreen(String openFrom) {
//        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
//            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_dashboard_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
//            com.ojassoft.astrosage.utils.CUtils.createSession(this, "DAREC");
//        }else  if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)){
//            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_dashboard_low_balance_subscribe_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
//            com.ojassoft.astrosage.utils.CUtils.createSession(this, "DSREC");
//
//        }else {
//            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_dashboard_low_balance_page_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
//            com.ojassoft.astrosage.utils.CUtils.createSession(this, "DPREC");
//        }
//        Intent intent = new Intent(DashBoardActivity.this, WalletActivity.class);
//        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
//        startActivity(intent);
//    }
    private  String bottomServiceListUsedFor;


    public void openWalletScreen(String openFrom) {
        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_dashboard_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "DAREC");
        }else  if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_dashboard_low_balance_subscribe_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "DSREC");

        }else {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_dashboard_low_balance_page_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "DPREC");
        }
        openWalletScreen();
//        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)||openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)){
//            if (CUtils.getCountryCode(DashBoardActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                //intent = new Intent(AIChatWindowActivity.this, MiniPaymentInformationActivity.class);
//                bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT;
//                openQuickRechargeSheet();
//            }else {
//                openWalletScreen();
//            }
//        }else {
//            openWalletScreen();
//        }
    }
    private void openWalletScreen() {
        Intent intent = new Intent(DashBoardActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }
    private void openQuickRechargeSheet() {
        try {
            QuickRechargeBottomSheet quickRechargeBottomSheet = QuickRechargeBottomSheet.getInstance();
            Bundle bundle = new Bundle();
            //bundle.putString("mResponse", response);
            bundle.putString("astrologerUrlText", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR,bottomServiceListUsedFor);
            bundle.putString("minBalanceNeededText","");
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");

            quickRechargeBottomSheet.setArguments(bundle);
            quickRechargeBottomSheet.show(getSupportFragmentManager(), QuickRechargeBottomSheet.TITLE);
        } catch (Exception e) {
            //
        }
    }
    public void gotoMiniPaymentInfoActivity(int mSelectedPosition, WalletAmountBean walletAmountBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        Intent intent = new Intent(context, MiniPaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    public void RefreshHomeFragment(String fromWhichDialog) {
        openWantedFragment();
        /*if (CUtils.isCallChatInitFromAstroList) {
            openFragment(HomeFragment1.newInstance(CGlobalVariables.DASHBOARD), "HOMEFRAGMENT");
            HomeFragment1 fragment = HomeFragment1.getInstance();
            if (fragment != null && fragment.isAdded()) {
                fragment.getData(CUtils.astroListFilterType);
            }
        } else {
            openFragment(VartaHomeFragment.newInstance(CGlobalVariables.DASHBOARD), "VARTAHOMEFRAGMENT");
        }*/
    }

    // deep linking
    private void actionOnIntent(Intent intent) {
        try {
            String connectDirectChat = "";
            if (intent != null) {
                Uri linkData = intent.getData();
                if (linkData != null) {
                    isGzipDesabled = linkData.getQueryParameter("gzip_desabled");
                    connectDirectChat = linkData.getQueryParameter(CGlobalVariables.CONNECT_AUTO_CHAT);
                }
                String action = intent.getAction();
                if (action == null) action = "";
                if (Intent.ACTION_VIEW.equals(action)) {
                    String articleIdLastPathSegment = linkData.getLastPathSegment();
                    if (articleIdLastPathSegment == null) articleIdLastPathSegment = "";
                    String articleId = linkData.toString();
                    //Log.d("actionOnIntentTest",articleId+"  ");
                    if (articleId.contains(CGlobalVariables.varta_astrosage_urls) ||
                            articleId.contains(CGlobalVariables.varta_astrosage_url)) {

                        if (articleId.contains("isfollow")) {
                            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_FOLLOW_NOTIFICATION_CLICKED, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_NOTIFICATION_CLICKED_EVENT, "");
                        } else {
                            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_PROMO_NOTIFICATION_CLICKED, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_NOTIFICATION_CLICKED_EVENT, "");
                        }


                        boolean isLogin = CUtils.getUserLoginStatus(DashBoardActivity.this);
                        String p_ID = linkData.getQueryParameter("prtnr_id");
                        // Log.d("actionOnIntentTest",p_ID+"  ");
                        if (!TextUtils.isEmpty(p_ID)) {
                            if (CUtils.isConnectedWithInternet(DashBoardActivity.this)) {
                                createSession(DashBoardActivity.this, p_ID);
                                //Log.d("actionOnIntentTest",p_ID+"  ");
                            }
                        }
                        if (articleIdLastPathSegment.equals(com.ojassoft.astrosage.utils.CGlobalVariables.live_astrologers)) {
                            //Live Astrologers
                            com.ojassoft.astrosage.utils.CUtils.googleAnalyticSendWitPlayServie(this, com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_LIVE_ASTROLGERS, null);
                            Intent tvIntent = new Intent(this, AllLiveAstrologerActivity.class);
                            startActivity(tvIntent);
                        } else if (articleId.contains(CGlobalVariables.LINK_HAS_LIVE) || articleId.contains(CGlobalVariables.LINK_HAS_LIVE_ASTRO)) {
                            openLiveScreen(articleIdLastPathSegment);
                            //Log.d("actionOnIntentTest","openAstrologerDetail");
                        } else if (articleId.contains(CGlobalVariables.LINK_HAS_ASTROLOGER)) {
                            CUtils.openAstrologerDetail(this, articleIdLastPathSegment, true, false, articleId);
                            //Log.d("actionOnIntentTest","openAstrologerDetail");
                        } else if (articleId.contains(CGlobalVariables.rechargeNow)) {
                            isUserLogin(null, CGlobalVariables.RECHARGE_SCRREN);
                        } else if (isLogin) {
                            if (articleIdLastPathSegment.equals(CGlobalVariables.consultationHistory) || articleIdLastPathSegment.equals(CGlobalVariables.rechargeNow)) {
                                openWalletScreen("dashboard");
                            } else {
                                if(connectDirectChat == null){
                                    connectDirectChat = "";
                                }
                                String offerType = CUtils.getCallChatOfferType(this);
                                Log.e("TestAuto","connectDirectChat="+connectDirectChat);
                                if(articleIdLastPathSegment.contains(com.ojassoft.astrosage.utils.CGlobalVariables.chat_with_astrologers) &&
                                        offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE) &&
                                        connectDirectChat.equals("true")){
                                    connectDirectChat(this,offerType);
                                }
                                applyCallChatFilter(FILTER_TYPE_CHAT);
                            }
                        } else {
                            applyCallChatFilter(FILTER_TYPE_CHAT);
                        }

                    } else if (articleId.contains(CGlobalVariables.astrosage_horo_urls) || articleId.contains(CGlobalVariables.astrosage_horo_url)) {
                        CUtils.openWebBrowser(this, linkData);
                    } else {

                        CUtils.openWebBrowser(this, linkData);
                    }
                }else if(action.equals(Intent.ACTION_QUICK_VIEW)){
                    com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_APP_ICON_FREE_CHAT, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                }

                Bundle extras = intent.getExtras();
                String message = "", title = "", urlText = "";
                if (extras != null) {
                    if (extras.containsKey("title")) {
                        title = extras.getString("title");
                        //////Log.e("LoadMore ", "Extra:" + extras.getString("title"));
                    }
                    if (extras.containsKey("msg")) {
                        message = extras.getString("msg");
                        //////Log.e("LoadMore ", "Extra:" + extras.getString("msg"));
                    }
                    if (extras.containsKey("astrologerUrl")) {
                        urlText = extras.getString("astrologerUrl");
                        if (urlText != null && urlText.length() > 0) {
                            String[] astroUrl = urlText.split("\\/");
                            if (astroUrl != null && astroUrl.length > 0) {
                                Intent intent1 = new Intent(DashBoardActivity.this, AstrologerDescriptionActivity.class);
                                intent1.putExtra("phoneNumber", CUtils.getUserID(DashBoardActivity.this));
                                intent1.putExtra("urlText", astroUrl[astroUrl.length - 1]);
                                intent1.putExtra("msg", message);
                                intent1.putExtra("title", title);
                                intent1.putExtra("isFromNotification", isNotification);
                                intent1.putExtra("fromDashboard", true);
                                startActivity(intent1);
                            }
                        }
                    } else {
                        if (message != null && message.length() > 0 && title != null && title.length() > 0) {
                            if (CUtils.getStringData(DashBoardActivity.this, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "").length() > 0) {
                                callMsgDialogData(message, title, true, CGlobalVariables.CHAT_CLICK);
                            } else {
                                callMsgDialogData(message, title, true, CGlobalVariables.CALL_CLICK);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {
        try {
            if (title.equals(getResources().getString(R.string.chat_failed_user)) || title.equalsIgnoreCase(getResources().getString(R.string.chat_not_completed))) {
                if (callInitiatedDialog != null) {
                    callInitiatedDialog.dismiss();
                }
                if (acceptanceDialog != null) {
                    acceptanceDialog.dismiss();
                }
            }
            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            dialog.show(getSupportFragmentManager(), "Dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFirebaseAccessToken();
        //callBackgroundLogin(); -- move on application level
    }

    public void startGcmService() {
        try {
            startService(new Intent(this, MyCloudRegistrationService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (manager != null) {
            manager.unregisterListener(listener);
        }*/
        //Log.e("SAN CI DA ", " onDestroy() ");

        if(callChatObserver !=null){
            AstroLiveDataManager.getAstroAcceptLiveData().removeObserver(callChatObserver);
        }
        if(ongoingChatObserver !=null){
            OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
        }
        if (ongoingCallObserver !=null){
            OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingCallObserver);
        }
        mainActivity = null;
    }

    @Override
    protected void onStop() {
        try {
            if (alert != null && alert.isShowing()) {
                alert.dismiss();
            }
        } catch (Exception e) {
            //
        }
        super.onStop();
        if (receiver != null) {
            //Log.e("SAN CI DA ", " onDestroy() receiver != null ");
            LocalBroadcastManager.getInstance(DashBoardActivity.this).unregisterReceiver(receiver);
        }
        AstrosageKundliApplication.isDasboardActivityVisible = false;
        // LocalBroadcastManager.getInstance(DashBoardActivity.this).unregisterReceiver(waitingChatTimerView);
        //LocalBroadcastManager.getInstance(DashBoardActivity.this).unregisterReceiver(ongoingChatTimerView);

    }


    public void openChatFragment(Fragment fragment, String fragTag, String astrologerId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("astrologerId", astrologerId);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment, fragTag);
            transaction.addToBackStack(null);
            transaction.commit();

            changeToolbar(MAINCHATFRAGMENT);
        } catch (Exception e) {
            //Log.d("fragmentSetup","openChatFragment exception --> "+e);
        }
    }

    public void chatCompleted(String URL, String channelID, String remarks, String status,String astroId) {
        CUtils.sendLogDataRequest(astroId, channelID, "DashboardActivity chatCompleted() onChildAdded() remarks="+remarks);
        CUtils.cancelNotification(DashBoardActivity.this);
        CGlobalVariables.chatTimerTime = 0;
        // CUtils.saveAstrologerIDAndChannelID(DashBoardActivity.this, "", "");
        if (!CUtils.isConnectedWithInternet(DashBoardActivity.this)) {
            CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), DashBoardActivity.this);
        } else {
            showProgressBar();
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, URL,
//                    DashBoardActivity.this, false, getChatCompleteParams(channelID, remarks,status,astroId), END_CHAT_VALUE).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            if(URL.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.END_CHAT_URL)){
                callEndChatApi(channelID,remarks,status,astroId);
            }else {
                callEndCallApi(channelID,remarks,status,astroId);
            }

        }
    }
    private void callEndChatApi(String channelID, String remarks, String status, String astroId) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks, status, astroId), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body()!=null) {
                    //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
                    try {
                        hideProgressBar();
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                        endCallChatActions();

                    } catch (Exception e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                endCallChatActions();
            }
        });
    }
    private void callEndCallApi(String channelID, String remarks, String status, String astroId) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endInternetcall(getChatCompleteParams(channelID, remarks, status, astroId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    hideProgressBar();
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("internet_call_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                    endCallChatActions();

                } catch (Exception e) {
                    //
                }            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                endCallChatActions();
            }
        });
    }
    private void endCallChatActions() {
        try {
                CGlobalVariables.chatTimerTime = 0;
                AstrosageKundliApplication.chatTimerRemainingTime = 0;
                AstrosageKundliApplication.selectedAstrologerDetailBean = null;
                AstrosageKundliApplication.chatJsonObject = "";
                AstrosageKundliApplication.channelIdTempStore = "";
                CUtils.saveAstrologerIDAndChannelID(DashBoardActivity.this, "", "");
                if (AstrosageKundliApplication.currentConsultType != null) {
                    if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                        stopService(new Intent(context, AstroAcceptRejectService.class));
                    } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.AUDIO_CALL_TEXT)) {
                        stopService(new Intent(context, AgoraCallInitiateService.class));
                    } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.VIDEO_CALL_TEXT)) {
                        stopService(new Intent(context, AgoraCallInitiateService.class));
                    }
                }
                chatInitiateInfoLayout.setVisibility(View.GONE);
                HomeFragment1 fragment = HomeFragment1.getInstance();
                if (fragment != null && fragment.isAdded()) {
                    fragment.getAstrologersStatusPrice();
                }

        } catch (Exception e) {
        }

    }
    public Map<String, String> getChatCompleteParams(String channelID, String remarks,String status,String astroId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        CGlobalVariables.CHAT_END_STATUS = status;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(DashBoardActivity.this));
        headers.put(CGlobalVariables.STATUS, status);
        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/"00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID,astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2001) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                UserProfileData userProfileDataBean = (UserProfileData) data.getExtras().get("USER_DETAIL");
                String fromWhere = data.getStringExtra("fromWhere");
                String returnedUrlText = data.getStringExtra("urlText");
                String consultationType = data.getStringExtra("consultationType");
                if (TextUtils.isEmpty(consultationType) && TextUtils.equals(returnedUrlText, CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                    consultationType = returnedUrlText;
                }
                if (TextUtils.isEmpty(consultationType) && TextUtils.equals(fromWhere, CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                    consultationType = fromWhere;
                }
                if (TextUtils.isEmpty(consultationType)) {
                    consultationType = ChatUtils.getInstance(this).consultationType;
                }
                AstrosageKundliApplication.backgroundLoginCountForChat = 0;
                if (isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_CHAT)) {
                    ChatUtils.getInstance(DashBoardActivity.this).startChat(userProfileDataBean);
                }else if (isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_CHAT_RANDOM)) {
                    ChatUtils.getInstance(DashBoardActivity.this).startChatRandom(userProfileDataBean,AstrosageKundliApplication.apiCallingSource);
                }else if (isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_AI_CHAT)) {
                    ChatUtils.getInstance(DashBoardActivity.this).startAIChat(userProfileDataBean);
                } else if (isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                    ChatUtils.getInstance(DashBoardActivity.this).startAIChatRandom(userProfileDataBean,AstrosageKundliApplication.apiCallingSource);
                }else if (isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_VIDEO_CALL)) {
                    ChatUtils.getInstance(DashBoardActivity.this).startVideoCall(userProfileDataBean);
                }else if(isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_VOICE_CALL)) {
                    ChatUtils.getInstance(DashBoardActivity.this).startAudioCall(userProfileDataBean);
                } else if (!isProceed && data.getExtras().containsKey("openKundliList")){
                    if(AstrosageKundliApplication.selectedAstrologerDetailBean!=null){
                        CUtils.openSavedKundliList(DashBoardActivity.this, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(),fromWhere, 2001);
                    } else if (TextUtils.equals(consultationType, com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                        CUtils.openSavedKundliList(DashBoardActivity.this, consultationType,fromWhere, 2001);
                    }
                } else if (!isProceed && data.getExtras().containsKey("openProfileForChat")){
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")){
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    if(AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        CUtils.openProfileForChat(DashBoardActivity.this, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), fromWhere, bundle, prefillData, 2001);
                    } else if (TextUtils.equals(consultationType, com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                        CUtils.openProfileForChat(DashBoardActivity.this, consultationType, fromWhere, bundle, prefillData, 2001);
                    }
                }else {
                    AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
                }
            }

        }
    }

    private void firebaseSignIn(String authToken) {
        try {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() inside "  );
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithCustomToken(authToken)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                            }
                        }
                    });
        } catch (Exception e) {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() excep " + e.toString() );
        }
    }

    public void getFirebaseAccessToken() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            fetchFirebaseAuthTokenFromServer();
        } else {
            firebaseUser.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult result) {
                    String idToken = result.getToken();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    fetchFirebaseAuthTokenFromServer();
                }
            });
        }
    }

    private void fetchFirebaseAuthTokenFromServer() {

        if (CUtils.isConnectedWithInternet(this)) {
            if (TextUtils.isEmpty(CUtils.getUserLoginPassword(this))) {
                return;
            }
            CUtils.getFirebaseAuthToken(this, GET_FIREBASE_AUTH_TOKEN);
        }

    }

    private void showProgressBar() {
        try {
            if (pd == null)
                pd = new CustomProgressDialog(DashBoardActivity.this);
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

    public void onBannerClicked(BannerLinkModel linkModel) {
        if (linkModel == null) return;
        String bannerUrl = linkModel.getLink();
        if (linkModel.isLoginrequired()) {
            boolean isLogin = CUtils.getUserLoginStatus(DashBoardActivity.this);
            if (isLogin) {
                redirectToLink(bannerUrl);
            } else {
                this.bannerUrl = bannerUrl;
                Intent intent = new Intent(DashBoardActivity.this, LoginSignUpActivity.class);
                intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, BANNER_CLICK);
                startActivity(intent);
            }
        } else {
            redirectToLink(bannerUrl);
        }
    }

    private void redirectToLink(String bannerUrl) {
        try {
            String decodedUrl = URLDecoder.decode(bannerUrl, "UTF-8");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(decodedUrl));
            ////Log.e("redirectToLink", decodedUrl);
            actionOnIntent(intent);
        } catch (Exception e) {
            //
        }
    }

    private void checkLoginAndRedirectToBannerLink() {
        boolean isLogin = CUtils.getUserLoginStatus(DashBoardActivity.this);
        if (!TextUtils.isEmpty(bannerUrl) && isLogin) { //when user come from login after banner click
            redirectToLink(bannerUrl);
        }
        bannerUrl = "";
    }

    private void checkAndRedirectToLiveStramingActivity(Intent intent1) {
        isFromScreen = intent1.getStringExtra(CGlobalVariables.IS_FROM_SCREEN);
        if (isFromScreen == null) return;
        if (CUtils.getUserLoginStatus(this) && isFromScreen.equalsIgnoreCase(CGlobalVariables.LIVESTREAMING_SCRREN)) {
            openLiveStramingScreen();
            isFromScreen = "";
        }
    }

    private void openLiveStramingScreen() {
        try {
            if (AstrosageKundliApplication.liveAstrologerModel == null) {
                return;
            }
            launchVartaLiveStreaming();
        } catch (Exception e) {
        }
    }

    public void checkPermissions(LiveAstrologerModel liveAstrologerModel) {
        AstrosageKundliApplication.liveAstrologerModel = liveAstrologerModel;
        openLiveStramingScreen();
//        boolean granted = true;
//        for (String per : PERMISSIONS) {
//            if (!permissionGranted(per)) {
//                granted = false;
//                break;
//            }
//        }
//
//        if (granted) {
//            Log.e("liveAstrologerModel", "checkPermissions2");
//            openLiveStramingScreen();
//        } else {
//            requestPermissions();
//        }
    }

//    private boolean permissionGranted(String permission) {
//        return ContextCompat.checkSelfPermission(
//                this, permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQ_CODE) {
//            boolean granted = true;
//            for (int result : grantResults) {
//                granted = (result == PackageManager.PERMISSION_GRANTED);
//                if (!granted) break;
//            }
//
//            if (granted) {
//                openLiveStramingScreen();
//            } else {
//                toastNeedPermissions();
//            }
//        }
//    }
//
//    private void toastNeedPermissions() {
//        callSnakbar(getString(R.string.need_necessary_permissions));
//    }


    private void launchVartaLiveStreaming() {
        /*if (manager == null) return;
        if (manager.getInstalledModules().contains(CGlobalVariables.MODULE_VARTA_LIVE)) {
            Intent intent = new Intent(CGlobalVariables.INTENT_LIVE_ACTIVITY);
            intent.setPackage(BuildConfig.APPLICATION_ID);
            startActivity(intent);
        } else {
            installVartaLiveStreaming();
        }*/

        CUtils.openLiveActivity(this);
    }

    /*public void installVartaLiveStreaming() {
        if (manager == null) return;
        SplitInstallRequest request = SplitInstallRequest
                .newBuilder()
                .addModule(CGlobalVariables.MODULE_VARTA_LIVE)
                .build();

        manager.startInstall(request)
                .addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer sessionId) {
                        showInstallLiveProgressBar();
                    }
                })
                .addOnFailureListener(new com.google.android.play.core.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        CUtils.showSnackbar(containerLayout, "Live content installation failed", DashBoardActivity.this);
                    }
                });
    }*/

    private void openLiveScreen(String urlText) {
        try {
            getLiveAstrologerModelAndJoinLiveSession(urlText);
        } catch (Exception e) {

        }
    }

    private void openAstrosageHomeActivity() {
        Intent intent = new Intent(DashBoardActivity.this, ActAppModule.class);
        startActivity(intent);
        finish();
    }

    public void hideKeyBoard() {
        try {
            if (ViewCompat.getRootWindowInsets(containerLayout).isVisible(WindowInsetsCompat.Type.ime())) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    WindowInsetsController controller = containerLayout.getWindowInsetsController();
                    controller.hide(WindowInsetsCompat.Type.ime());
                } else {
                    hideMyKeyboard(mainActivity);
                }
            }
        } catch (Exception e) {
            //
        }
    }


    /*
     * Varta Dialog open after 30 sec on Dashboard
     * */
    public void openDialogOnHomeScreen() {
        if (showDialogOnce) {
            return;
        }
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        int planId = com.ojassoft.astrosage.utils.CUtils.getUserPurchasedPlanFromPreference(DashBoardActivity.this);
                        //Log.e("PlanSubscription", "planId="+planId);
                        if (planId != com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID && planId != com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_9 && planId != com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_10 && planId != com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                            if (!AstrosageKundliApplication.isOpenVartaPopup) {
                                showDialogOnce = true;
                                com.ojassoft.astrosage.varta.utils.CUtils.showConsultPremiumDialogNew(DashBoardActivity.this);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ActAppModule.runTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickHome() {
        if (viewInc.getVisibility() != View.VISIBLE) {
            openAstrosageHomeActivity();
        }
    }

    @Override
    public void clickCall(){
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_CALL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        applyCallChatFilter(FILTER_TYPE_CALL);
    }

    @Override
    public void clickChat(){
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        applyCallChatFilter(FILTER_TYPE_CHAT);
    }

    @Override
    public void clickLive() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_LIVE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        fabActions();
    }
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response, int requestCode) {
        hideProgressBar();
        if (requestCode == METHOD_RECHARGE) {
            if(response.body()!=null) {
                try {
                    String myResponse = response.body().string();
                    handleWalletRechargeResponse(myResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
        handleWalletRechargeError();
        hideProgressBar();
    }

    private void composeNewEmail() {
        String email = "araj@ojassoft.com",
                subject = "Astrologer List Logs";

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT,AstrosageKundliApplication.astroListLogs);
            startActivity(intent);

        } catch (Exception e) {
            //Log.e("SAN ", "composeNewEmail exception" + e.toString() );
        }

    }

    private String astroUrlForLiveJoin;
    private ArrayList<LiveAstrologerModel> liveAstrologersArrayList;
    LiveAstrologerModel liveAstrologerModel;

    public void getLiveAstrologerModelAndJoinLiveSession(String urlText) {
        try {
            liveAstrologerModel = null;
            astroUrlForLiveJoin = urlText;
            String liveAstroData = CUtils.getLiveAstroList();
            if (!TextUtils.isEmpty(liveAstroData)) {
                parseLiveAstrologerList(liveAstroData);
                if (liveAstrologerModel != null) {
                    openLiveStramingScreenFromNotification();
                } else {
                    getLiveAstrologerDataFromServer();
                }
            } else {
                getLiveAstrologerDataFromServer();
            }
        } catch (Exception e) {

        }
    }

    /**
     * @param urlText
     * @return LiveAstrologerModel
     */
    private LiveAstrologerModel getLiveAstrologerModelFromList(String urlText) {
        try {
            if (TextUtils.isEmpty(urlText)) {
                return null;
            }
            if (liveAstrologersArrayList != null) {
                for (int i = 0; i < liveAstrologersArrayList.size(); i++) {
                    LiveAstrologerModel liveAstrologerModel = liveAstrologersArrayList.get(i);
                    if (liveAstrologerModel == null) continue;
                    if (urlText.equals(liveAstrologerModel.getUrltext())) {
                        return liveAstrologerModel;
                    }

                }
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    private void getLiveAstrologerDataFromServer() {
        if (CUtils.isConnectedWithInternet(this)) {
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getLiveAstrologerList(CUtils.getLiveAstroParams(this, CUtils.getActivityName(DashBoardActivity.this)));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            String myResponse = response.body().string();
                            CUtils.saveLiveAstroList(myResponse);
                            parseLiveAstrologerList(myResponse);
                            if (liveAstrologerModel != null) {
                                openLiveStramingScreenFromNotification();
                            }
                        }
                    } catch (Exception e) {
                        //
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }


    private void parseLiveAstrologerList(String liveAstroData) {
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }
        if (liveAstrologersArrayList == null) {
            liveAstrologersArrayList = new ArrayList<>();
        } else {
            liveAstrologersArrayList.clear();
        }
        try {
            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LiveAstrologerModel liveAstrologerModel = CUtils.parseLiveAstrologerObject(object);
                if (liveAstrologerModel == null) continue;
                liveAstrologersArrayList.add(liveAstrologerModel);
                if (astroUrlForLiveJoin.equals(liveAstrologerModel.getUrltext())) {
                    this.liveAstrologerModel = liveAstrologerModel;
                }
            }
            liveAstrologerModelArrayList = liveAstrologersArrayList;
        } catch (Exception e) {
            //Log.e("redirectToLink", e.toString());
        }
    }


    /**
     * if liveAstrologerModel not null then join live otherwise open astrologer details page.
     */
    private void openLiveStramingScreenFromNotification() {
        try {
            if (liveAstrologerModel != null) {
                if(AstrosageKundliApplication.liveActivityVisible){
                    Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_OPEN_LIVE_SCREEN_FROM_NOTIFICATION);
                    intent.putExtra(CGlobalVariables.ASTROLOGER_DATA,liveAstrologerModel);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }else {
                    checkPermissions(liveAstrologerModel);
                }
            } else {
                CUtils.openAstrologerDetail(this, astroUrlForLiveJoin, true, false,"");
            }
        } catch (Exception e) {
            //
        }
        astroUrlForLiveJoin = "";
    }

    private void connectDirectChat(Activity activity, String offerType){
        try{
            boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(activity);
            String firstFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE,"");
            String secondFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE,"");

            if (enabledAIFreeChatPopup) {
                if(CUtils.isSecondFreeChat(activity) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                    if(secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                        CUtils.initiateRandomAiChat(activity,CGlobalVariables.NOTIFICATION_DIRECT_CHAT_AI, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                    } else {
                        CUtils.initiateRandomChat(activity,CGlobalVariables.NOTIFICATION_DIRECT_CHAT, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                    }
                } else {
                    if(firstFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                        CUtils.initiateRandomAiChat(activity,CGlobalVariables.NOTIFICATION_DIRECT_CHAT_AI, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                    } else {
                        CUtils.initiateRandomChat(activity,CGlobalVariables.NOTIFICATION_DIRECT_CHAT, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                    }
                }
            } else {
                CUtils.initiateRandomChat(activity,CGlobalVariables.NOTIFICATION_DIRECT_CHAT, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
            }
        } catch (Exception e){
            //
        }
    }



}
