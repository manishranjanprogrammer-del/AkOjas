package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity.BACK_FROM_PLAN_PURCHASE_AD_SCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.AI_CHAT_EXTEND_CHAT_RECHARGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CHAT_COMPLETED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DURING_CHAT_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PERSONALIZED_AI_NOTIFICATION;
import static com.ojassoft.astrosage.utils.CUtils.getUserBirthDetailBean;
import static com.ojassoft.astrosage.utils.CUtils.msgCount;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CHAT_WINDOW_SUBSCRIBE_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONTINUE_AI_CHAT_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_PAYMENT_SUCCESS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_AI_CHAT_WINDOW_CHAT_EXTEND_RECHARGE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_FAILED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE_FAILED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_SUCCESS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_AI_CALL_SHOW_IN_AI_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_ASTROLOGER;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NEW_USER_AI_CHAT_5MIN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER;
import static com.ojassoft.astrosage.varta.utils.CUtils.AINotificationChatWindowTitle;
import static com.ojassoft.astrosage.varta.utils.CUtils.AINotificationChatWindowNextQuestion;
import static com.ojassoft.astrosage.varta.utils.CUtils.backPressFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.editTextTouchFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;
import static com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents;
import static com.ojassoft.astrosage.varta.utils.CUtils.followAstrologerModelArrayList;
import static com.ojassoft.astrosage.varta.utils.CUtils.hideMyKeyboard;
import static com.ojassoft.astrosage.varta.utils.CUtils.chatWindowOpenType;
import static com.ojassoft.astrosage.varta.utils.CUtils.keypadOpenFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.sendButtonFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.typingFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.userActionsEvents;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanHoroscopeRemedies;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.misc.OnSuccessKundliCalculation;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.fragments.FullProfileFragment;
import com.ojassoft.astrosage.ui.fragments.ShareMessageFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.varta.adapters.AIChatMessageAdapter;
import com.ojassoft.astrosage.varta.aichat.AIChatTraceLogger;
import com.ojassoft.astrosage.varta.aichat.GetAnswerFromServer;
import com.ojassoft.astrosage.varta.aichat.models.GreetingsModel;
import com.ojassoft.astrosage.varta.dao.ChatHistoryDAO;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteMinimizeDialog;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ConnectChatBean;
import com.ojassoft.astrosage.varta.model.ExtendInfo;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ContactNumberRestrictMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.LeftStatusMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.WelcomeStatusMessage;
import com.ojassoft.astrosage.varta.ui.fragments.RechargePopUpAfterFreeChat;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;
import com.ojassoft.astrosage.varta.utils.RoundImage;
import com.ojassoft.astrosage.varta.utils.TypeWriter;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Must Read
 * This activity created on the behalf of MainChatFragment to manage all chat related task
 * */

public class AIChatWindowActivity extends BaseActivity implements VolleyResponse, View.OnClickListener, GlobalRetrofitResponse, TypeWriter.OnTypingComplete, OnSuccessKundliCalculation {
    private static final String TAG = CGlobalVariables.AICHATWINDOWACTIVITY;
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(AIChatWindowActivity.class);
    CustomProgressDialog pd;
    private boolean isConnectivityDisconnected;
    Toolbar toolbarChat;
    TextView typingTextview;
    ImageView ivbackChat, shareChat, copyChat;
    CircularNetworkImageView astrologerProfilePic;
    TextView titleTextChat, timerTextview;
    TextView endChatButton;
    Activity activity;
    public static String CHANNEL_ID = "", channelIdForNps = "";
    /**
     * True only while this Activity is in the foreground (resumed).
     *
     * Used by {@link com.ojassoft.astrosage.varta.aichat.GetAnswerFromServer} to decide whether it should
     * show a "response ready" notification (only when user is not currently on the chat screen).
     */
    public static volatile boolean isAiChatWindowInForeground = false;
    Context context;
    RequestQueue queue;
    RelativeLayout sendButton, containerLayout, buttonKundli;
    public RecyclerView messagesListView;
    LinearLayoutManager mLayoutManager;
    EditText messageTextEdit;
    public ImageView /*ivWaitMsg,*/ imgViewDowmArrow,ivFRPAFC;
    private boolean isScroll = false;
    public int speakingPosition = -1;
    public TextToSpeech tts;
    public AppCompatButton btnChatAgain;
    ConstraintLayout llConnectAgain;
    public static boolean openKundli = false;
    private int countDots = 0;
    private final String[] dotsArray = {".  ", ".. ", "..."};
    private final int mInterval = 500; // 5 seconds by default, can be changed later
    FeedbackDialog feedbackDialog;
    public AIChatMessageAdapter messageAdapter;
    LinearLayout relSendMessage;
    private Random numRandom;
    public String astrologerId, astrologerName, astrologerProfileUrl, userChatTime, aiAstrologerId, astrologerLageProfile;
    public boolean isChatAgainClicked;
    private ValueEventListener valueEventListenerNetConnection;
    private Handler connectivityTimer;
    private Runnable connectivityRunnable;
    private Animation animShow, animHide;
    public boolean IS_ASTROLOGER_DISCONNECT = false;
    public boolean isAiBehaveLikeHuman = false;
    public boolean isShowKundli = false;
    public int isNotNeedSteam = 0;
    private long longTotalVerificationTime = 60000;
    private CountDownTimer countDownTimer, endChatDisabledTimer;
    private final long longOneSecond = 1000;
    private String chatJsonObject;
    private ConnectChatBean connectChatBean;
    UserReviewBean userOwnReviewBean;
    int intentNotificationId;
    boolean onJoinChatClick;
    private boolean isDialogShownOnEndChat;
    private TextView tvChatRechargeButton;

    private String userNameTempStore = "";
    boolean isQuickRechargeClicked = false;
    private ConstraintLayout clExtendChatReminder;
    private TextView tvChatRechargeTitle,tvExtendChatMsg;// followAstrologer,textViewAstrologerMessage,textViewDate;
    //private LinearLayout llStatusLayout;
    private long remainingChatTime;

    public static String followStatus = "0";

    public static boolean isChatCompleted = false;
    public boolean isTypingGreetingMessage = false;
    private String urlText = "";
    private FreeMinuteMinimizeDialog freeMinuteMinimizeDialog;
    public boolean isAvailableForCall;
    public String isNewSession = "1", isLocallyAnswered = "0", previousQues = "", previousAns = "";
    public ArrayList<BeanHoroscopeRemedies> beanHoroscopeRemedies;
    public boolean stopTypeWriter = false;
    public View llChatButton;
    public LinearLayout llTypeWriterParent;
    public boolean scrollWhileTypeWriter = true;
    public int currentTypeWriterPosition;
    public TypeWriter currentTypeWriter;
    public int mIndexTypeWriter;
    public static String langCode = "";
    private ValueEventListener endChatValueEventListener;
    private DatabaseReference endChatRef;
    private boolean isKundliCalculated = false;
    private String chatDurarion;
    private int extendableChatDuration;
    private String isEnd = "0";
    public int AI_LANGUAGE_CODE;

    private CountDownTimer noUserResponseTimer;
    private boolean isDynamicGreeting = false;

    private TextView tvFreeChatBlinker;

    public static int isRoman;
    private final int AUTO_GREETING_TIME = 15000;
    LinearLayout btn_options_view;
    LinearLayout btnKundli,btnMatching,btnHoroscope,btnPanchang;
    private String newQuestion;
    private String serviceprice="0",minimumrecharge="0";
    ExtendInfo extendInfo;
    private String iLangCode = "";
    private final String[] validLanguages = {"en", "hi", "ta", "bn", "mr", "ml", "te", "gu", "kn","or","es"};
    private static final String AI_EXTEND_REMINDER_VISIBLE_KEY_PREFIX = "ai_extend_chat_reminder_visible_";

    //private ShimmerFrameLayout shimmerLayoutChat;

    View includeLayoutBottomSheet;
    private  String bottomServiceListUsedFor;
    private String storedOfferType = "", minBalText;
    public boolean isAiProPlan,isAiProPlanLimitReached;
    public String aiPassPlanId = "";
    public String offerTypeDuringInitChat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat_window);
        AI_LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        //Log.e("SAN ", " onCreate() call ");
        activity = this;
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        CUtils.isAutoConsultationConnected = false;
        inti();
        CUtils.updateChatBackgroundBasedOnTheme(this);
        if (savedInstanceState == null) {
            if (getIntent() != null) {
                if (CUtils.isFromAINotification) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_PAID_CHAT_CONNECTED, PERSONALIZED_AI_NOTIFICATION,"");
                    isChatAgainClicked = true;
                    //  Log.e(TAG, "onCreate: AI NOTIFICATION CALLED AND OPENED AI-CHATWINDOW");
                    AstrosageKundliApplication.isChatAgainButtonClick = true;
                }
                // Join-chat can open without any cached in-memory history; seed UI from local DB to avoid blank state.
                String intentAstrologerId = getIntent().getStringExtra("astrologer_id");
                if (!TextUtils.isEmpty(intentAstrologerId) && (messageAdapter == null || messageAdapter.getItemCount() == 0)) {
                    loadOldChats(intentAstrologerId);
                    Log.d("ChatHistoryDAO", "onCreate: astrologer_id = " + intentAstrologerId);
                }
                handleIntent(getIntent(), "onCreate");
                if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
                    stopService(new Intent(activity, OnGoingChatService.class));
                }

            }
        } else {
            finish();
        }

        messageTextEdit.setText(CUtils.getChatDraftMessage(activity, astrologerId));

        //  followStatus();
        // getFollowingAstrologerDataFromServer();
        getIsUserFollowingAstrologer();
        actionOnKeyBoardVisibility();
        setOnScrollListener();
        AstrosageKundliApplication.brokenMessageLogs = "";
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Log.e("SAN ", " onNewIntent() call ");
        if (intent != null) {
            boolean isRecharged = false;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
                try {
                    if(QuickRechargeBottomSheet.getInstance()!=null){
                        QuickRechargeBottomSheet.getInstance().dismiss();
                    }
                    if(RechargePopUpAfterFreeChat.newInstance()!=null){
                        RechargePopUpAfterFreeChat.newInstance().dismiss();
                    }
                }catch (Exception e){
                    Log.e("testLogs",e+"");
                }
                try {
                    if(RechargeSuggestionBottomSheet.getInstance()!=null){
                        RechargeSuggestionBottomSheet.getInstance().dismiss();
                    }

                }catch (Exception e){
                    Log.e("testLogs",e+"");
                }

            }
            if (isRecharged) {
                try {
                    if(RechargeSuggestionBottomSheet.getInstance()!=null){
                        RechargeSuggestionBottomSheet.getInstance().dismiss();
                    }
                }catch (Exception e){
                }
                String orderID = bundle.getString(CGlobalVariables.ORDER_ID);
                String orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS);
                String rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT);
                String paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE);
                String razorpayid = bundle.getString("razorpayid");
                if (orderStatus.equals("0")) {
                    udatePaymentStatusOnserveronFailed(containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode);
                } else {
                    udatePaymentStatusOnserver(this, containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode, razorpayid);
                }
            } else if (intent.hasExtra("newQuestion")) {
                newQuestion = intent.getStringExtra("newQuestion");
                intent.putExtra("newQuestion", "");
                previousAns = newQuestion;
                previousQues = intent.getStringExtra(KEY_AI_NOTIFICATION_TITLE);
                newQuestion = "";
                isLocallyAnswered = "1";
                long chatId = numRandom.nextInt(999) + 1;
                sendMsgToFirebase(previousAns, chatId, CGlobalVariables.ASTROLOGER, CGlobalVariables.USER, "");
                addMessageToAdapter(previousAns,ASTROLOGER,chatId,false,false);
            } else {
                //Log.e("SAN ", " onNewIntent() calling handleIntent(); ");
                handleIntent(intent, "onNewIntent");
            }
        }
    }

    private void inti() {
        btn_options_view = findViewById(R.id.btn_options_view);
        btn_options_view.setVisibility(View.GONE);
        btnKundli = findViewById(R.id.btn_kundli);
        btnMatching = findViewById(R.id.btnMatching);
        btnHoroscope = findViewById(R.id.btnHoroscope);
        btnPanchang = findViewById(R.id.btnPanchang);
        //btnNumerology = findViewById(R.id.btnNumerology);


        String offerType = CUtils.getCallChatOfferType(activity);
        if(CUtils.isFreeChat && CUtils.isSecondFreeChat(activity) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
            String directSecondFreeChat = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.DIRECT_SECOND_FREE_CHAT, "");
            if (directSecondFreeChat.equals(ENABLED)) {
                btn_options_view.setVisibility(View.VISIBLE);
            }
        }
        containerLayout = findViewById(R.id.containerLayout);
        toolbarChat = findViewById(R.id.toolbar_chat);
        typingTextview = findViewById(R.id.typingTextview);
        typingTextview.setVisibility(View.GONE);
        ivbackChat = findViewById(R.id.iv_close_chat);
        astrologerProfilePic = findViewById(R.id.astrologer_profile_pic);
        titleTextChat = findViewById(R.id.title_text_chat);
        timerTextview = findViewById(R.id.timer_textview);
        endChatButton = findViewById(R.id.end_chat_button);
        shareChat = findViewById(R.id.iv_share_chat);
        copyChat = findViewById(R.id.iv_copy_chat);
        sendButton = findViewById(R.id.buttonSend);
        buttonKundli = findViewById(R.id.buttonKundli);
        messagesListView = findViewById(R.id.listViewMessages);
        messageTextEdit = findViewById(R.id.editTextMessage);
        relSendMessage = findViewById(R.id.relSendMessage);
        btnChatAgain = findViewById(R.id.btnChatAgain);
        llConnectAgain = findViewById(R.id.llConnectAgain);
        llConnectAgain.setVisibility(View.GONE);
        tvChatRechargeButton = findViewById(R.id.tvChatRechargeButton);
        clExtendChatReminder = findViewById(R.id.clExtendChatReminder);
        tvChatRechargeTitle = findViewById(R.id.tvChatRechargeTitle);
        tvExtendChatMsg = findViewById(R.id.tvExtendChatMsg);
        //followAstrologer = findViewById(R.id.followAstrologer);
        //llStatusLayout = findViewById(R.id.llStatusLayout);
        //textViewDate = findViewById(R.id.textViewDate);
        ivSend = findViewById(R.id.ivSend);
        ivStop = findViewById(R.id.ivStop);
        messageAdapter = new AIChatMessageAdapter(activity);
        mLayoutManager = new LinearLayoutManager(this);
        messagesListView.setLayoutManager(mLayoutManager);
        messagesListView.setItemAnimator(null);
        messagesListView.setAdapter(messageAdapter);
        messagesListView.setItemViewCacheSize(20);
        messagesListView.setNestedScrollingEnabled(false);

        //ivWaitMsg = findViewById(R.id.ivWaitMsg);
        imgViewDowmArrow = findViewById(R.id.imgViewDowmArrow);
        tvFreeChatBlinker = findViewById(R.id.tvFreeChatBlinker);
        ivFRPAFC = findViewById(R.id.ivFRPAFC);
//        shimmerLayoutChat = findViewById(R.id.shimmerLayoutChat);
//        shimmerLayoutChat.setVisibility(View.VISIBLE);
//        shimmerLayoutChat.startShimmer();
        includeLayoutBottomSheet = findViewById(R.id.includeLayoutBottomSheet);

        imgViewDowmArrow.setOnClickListener(view1 -> {
            scrollMyListViewToBottom();
        });
        FontUtils.changeFont(this,tvChatRechargeTitle,CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this,tvChatRechargeButton,CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        messagesListView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isAtBottom()) {
                    imgViewDowmArrow.setVisibility(View.GONE);
                } else {
                    imgViewDowmArrow.setVisibility(View.VISIBLE);

                }
            }
        });

        //Glide.with(ivWaitMsg).load(R.drawable.typing).into(ivWaitMsg);
        setUpListeners();
        numRandom = new Random();

        CGlobalVariables.CHAT_END_STATUS = "";

        ivbackChat.setOnClickListener(this);
        endChatButton.setOnClickListener(this);
        shareChat.setVisibility(View.GONE);
        shareChat.setOnClickListener(this);
        copyChat.setOnClickListener(this);
        copyChat.setVisibility(View.GONE);
        tvChatRechargeButton.setOnClickListener(this);

        btnKundli.setOnClickListener(this);
        btnMatching.setOnClickListener(this);
        btnHoroscope.setOnClickListener(this);
        btnPanchang.setOnClickListener(this);
        initAnimation();

    }


    @Override
    protected void onResume() {
        super.onResume();
        chatWindowOpenType = "AI";
        isAiChatWindowInForeground = true;
        try {
            new CreateCustomLocalNotification(this).cancelAiResponseReadyNotification();
        } catch (Exception e) {
            //
        }
        // If the background service is running, stop it while the chat screen is visible
        // to avoid double-notifications and to keep UI as the source of truth.
        try {
            if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
                stopService(new Intent(activity, OnGoingChatService.class));
            }
        } catch (Exception e) {
            //
        }
        if (CGlobalVariables.CHAT_END_IN_BACKGROUND) {
            CGlobalVariables.CHAT_END_IN_BACKGROUND = false;
            finishChat(CGlobalVariables.USER_BACKGROUND);
            clExtendChatReminder.setVisibility(View.GONE);
            saveExtendChatReminderVisibility(false);
        }
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                setUtteranceProgressListener();
            } else {
                //Log.e("TTS", "Initialization Failed!");
            }
        });

    }

    /**
     * Starts {@link OnGoingChatService} (without finishing this Activity) so chat keeps running
     * and notifications can be shown when the user backgrounds the app (e.g., home/middle button).
     */
    private void startBackgroundChatServiceIfNeeded() {
        try {
            if (isChangingConfigurations() || isFinishing()) {
                return;
            }
            // Only start while a chat is actually running.
            // Some flows may not have started the timer yet (e.g., user backgrounds immediately),
            // so fall back to the global chat-status signal as well.
            boolean isChatRunning = CGlobalVariables.CHAT_RUNNING.equals(AstrosageKundliApplication.currentChatStatus)
                    || CGlobalVariables.CHAT_STARTED.equals(AstrosageKundliApplication.currentChatStatus)
                    || countDownTimer != null;
            if (!isChatRunning || isChatCompleted || TextUtils.isEmpty(CHANNEL_ID)) {
                return;
            }
            // Avoid re-starting the service if it is already running.
            if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
                return;
            }
            Intent serviceIntent = new Intent(activity, OnGoingChatService.class);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHANNEL_ID);
            bundle.putString("connect_chat_bean", chatJsonObject);
            bundle.putString("astrologer_name", astrologerName);
            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
            bundle.putString("astrologer_id", astrologerId);
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI);
            bundle.putString("userChatTime", changeToMinSec());
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent);
            } else {
                activity.startService(serviceIntent);
            }
        } catch (Exception e) {
            //
        }
    }


    private void handleIntent(Intent intent, String from) {
        // Log.d("TestChatIssue","From ==>"+from);
        AstrosageKundliApplication.isBackFromChat = false;
        if (intent != null) {
            boolean onNotificationClick = intent.getBooleanExtra("ongoing_notification", false);
            intent.putExtra("ongoing_notification", false);
            if (onNotificationClick && AstrosageKundliApplication.currentChatStatus.equals(CGlobalVariables.CHAT_STARTED)) {
                onNotificationClick = false;
            }
            if (!onNotificationClick) {
                CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
                //Log.d(TAG, "CHANNEL_ID: "+CHANNEL_ID);
                onJoinChatClick = intent.getBooleanExtra("isFromJoinButton", false);
                chatJsonObject = intent.getStringExtra("connect_chat_bean");
                astrologerName = intent.getStringExtra("astrologer_name");
                astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
                astrologerLageProfile = intent.getStringExtra("image_file_large");
                astrologerId = intent.getStringExtra("astrologer_id");
                aiAstrologerId = intent.getStringExtra("ai_astrologer_id");
                userChatTime = intent.getStringExtra("userChatTime");
                if (intent.hasExtra("newQuestion")) {
                    newQuestion = intent.getStringExtra("newQuestion");
                }
                if(aiAstrologerId == null){
                    aiAstrologerId = "";
                }
                CUtils.saveAIAstrologerID(this, aiAstrologerId);

                intent.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, "");
                intent.putExtra("isFromJoinButton", false);
                intent.putExtra("connect_chat_bean", "");
                intent.putExtra("astrologer_name", "");
                intent.putExtra("astrologer_profile_url", "");
                intent.putExtra("image_file_large", "");
                intent.putExtra("astrologer_id", "");
                intent.putExtra("ai_astrologer_id", "");
                intent.putExtra("userChatTime", "");
                intent.putExtra("newQuestion", "");

                channelIdForNps = CHANNEL_ID;
                AstrosageKundliApplication.AI_ASTROLOGER_ID = aiAstrologerId;
                setIsAiPassFromJObj();
                Gson g = new Gson();
                connectChatBean = g.fromJson(chatJsonObject, ConnectChatBean.class);
                intentNotificationId = getIntent().getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);

                queue = VolleySingleton.getInstance(AIChatWindowActivity.this).getRequestQueue();
                if (onJoinChatClick) {
                    joinConnectedChat();
                } else {
                    if(!isChatAgainClicked && AstrosageKundliApplication.allChatMessagesHistory == null)
                        AstrosageKundliApplication.allChatMessagesHistory = new ArrayList<>();

                    CUtils.setEndChatButtonVisibilityTimer(context, CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI);
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACCEPTED_FBD_KEY).setValue("true");
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child("OpenChatWindow").setValue("true");
                    CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "AIChatWindowActivity open from "+from);
                    try {

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initConnectChat();
                            }
                        }, 500);

                    } catch (Exception e) {
                        initConnectChat();
                    }
                }

                titleTextChat.setText(astrologerName);
                if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
                    String astroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
                    Glide.with(getApplicationContext()).load(astroImage).circleCrop().into(astrologerProfilePic);
                }
                astrologerProfilePic.setOnClickListener(v -> {
                    CUtils.fcmAnalyticsEvents("astrologer_picture_click_in_ai_chat_window", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    showAstrologorFullProfile();
                });

                changeVisibilitySendMsgLayout(true);
                if (!from.equals("onNewIntent")) {
                    AstrosageKundliApplication.ASTROLOGER_ID = astrologerId;
                    addHistory();
                }
                try {
                    if(!TextUtils.isEmpty(chatJsonObject)){
                        JSONObject connectChat = new JSONObject(chatJsonObject);
                        if(connectChat.has("astrologer")){
                            JSONObject astroJason = connectChat.getJSONObject("astrologer");
                            isAiBehaveLikeHuman = astroJason.optBoolean("isaiastrobehavelikehuman");
                            //Log.d("Chat like human","isAiBehaveLikeHuman = " + isAiBehaveLikeHuman);
                            isNotNeedSteam = isAiBehaveLikeHuman ? 1:0;
                        }
                        //Log.d("kundli in Chat","isShowKundli = " + chatJsonObject);
                        isShowKundli = connectChat.optBoolean("showkundali");
                    }
                } catch (Exception e) {
                    //
                }

            }
        }
    }
    /**
     * Method to set the boolean value of isAiProPlan according to the value from the JsonObject.
     * This value is used to check if the AI astrologer is on pro plan or not.
     * If the value is true then the AI astrologer is on pro plan else not.
     */
    private void setIsAiPassFromJObj() {
        try {
            //Log.e("isAiPassFromJObj","isAiPassFromJObj = " + chatJsonObject);
            JSONObject jsonObject = new JSONObject(chatJsonObject);
            if (jsonObject.has("isaipass")) {
                isAiProPlan = jsonObject.getBoolean("isaipass");
                /*if (stringAiProPlan.equals("true")) {
                    isAiProPlan = true;
                } else {
                    isAiProPlan = false;
                }*/
            }

            if (jsonObject.has("planid")) {
                aiPassPlanId = jsonObject.getString("planid");
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during JSON parsing
        }
    }


    private boolean isAtBottom() {
        return mLayoutManager.findLastVisibleItemPosition() >= messageAdapter.getItemCount()-1;
    }

    private void startService() {
        try {
            Intent serviceIntent = new Intent(activity, OnGoingChatService.class);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHANNEL_ID);
            bundle.putString("connect_chat_bean", chatJsonObject);
            bundle.putString("astrologer_name", astrologerName);
            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
            bundle.putString("astrologer_id", astrologerId);
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI);
            bundle.putString("userChatTime", changeToMinSec());
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent);
            } else {
                activity.startService(serviceIntent);
            }
            AstrosageKundliApplication.isBackFromChat = true;
            // when user click on back button from chat window we have to request api with this below action
            ChatUtils.getInstance(this).cancelRechargeAfterChat(channelIdForNps,"AI_CHAT_BACK_BTN");

            finish();
        } catch (Exception e) {
            //
        }
    }

    private void initConnectChat() {
        includeLayoutBottomSheet.setVisibility(View.GONE);
        CUtils.saveLastUsedAIAstrologerDetails(this, aiAstrologerId,astrologerName,astrologerProfileUrl);
        msgCount = 0;
        try {
            CUtils.playChatConnectSound(activity, 2);
        } catch (Exception e) {
            //
        }
        offerTypeDuringInitChat = CUtils.getCallChatOfferType(activity);
        IS_ASTROLOGER_DISCONNECT = false;
        AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
        initEndChatListener(CHANNEL_ID);
        timerStart();
        AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child("TimerStart").setValue("true");
        try {
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(intentNotificationId);
        } catch (Exception e) {

        }
    }

    private void joinConnectedChat() {
        IS_ASTROLOGER_DISCONNECT = false;
        initEndChatListener(CHANNEL_ID);
        if (userChatTime.trim().length() > 0) {
            timeSetOnTimer(userChatTime);
            restoreExtendChatReminderIfNeeded();
            sendCustomPushNotification(astrologerName, getResources().getString(R.string.ongoing_chat), "");
            registerConnectivityStatusReceiver();
            registerReceiverBackgroundLogin();
            setOnDisconnectListner();
        }
        AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
        try {
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(intentNotificationId);
        } catch (Exception e) {

        }

        if (!TextUtils.isEmpty(newQuestion)) {
            messageTextEdit.setText(newQuestion);
            newQuestion = "";
            identifyLanguageWithStringInput(getTextInput().trim(),true);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpListeners() {
        messagesListView.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP){
                scrollWhileTypeWriter = false;
            }
            return false;
        });
        btnChatAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                includeLayoutBottomSheet.setVisibility(View.GONE);
                messageAdapter.removeFollowMessage();
                //llStatusLayout.setVisibility(View.GONE);
                enableSendButton();
                scrollMyListViewToBottom();
                isChatAgainClicked = true;
                AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("chat_btn_click_chat_window", AstrosageKundliApplication.currentEventType, "");

                setMessageInputEnabled(true);
                llConnectAgain.setVisibility(View.GONE);
                //bottomSheetDialog.dismiss();
                if (CUtils.isChatNotInitiated()) {
                    ChatUtils.getInstance(AIChatWindowActivity.this).initAIChat(AstrosageKundliApplication.chatAgainAstrologerDetailBean);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), context);
                }
                if (AstrosageKundliApplication.ASTROLOGER_ID != null && !AstrosageKundliApplication.ASTROLOGER_ID.equals(astrologerId)) {
                    AstrosageKundliApplication.chatMessagesHistory = null;
                    //AstrosageKundliApplication.statusMessageSetHistory = null;
                    AstrosageKundliApplication.isChatAgainButtonClick = false;
                }
                AstrosageKundliApplication.chatMessagesHistory = messageAdapter.getUpdatedMessageList();
                //AstrosageKundliApplication.statusMessageSetHistory = messageAdapter.getStatusList();
                AstrosageKundliApplication.ASTROLOGER_ID = astrologerId;
                AstrosageKundliApplication.AI_ASTROLOGER_ID = aiAstrologerId;
                AstrosageKundliApplication.isChatAgainButtonClick = true;
            }
        });

        buttonKundli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMyKeyboard(AIChatWindowActivity.this);
                openProfileDialog();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivStop.getVisibility() == View.VISIBLE) {
                    stopTypeWriter = true;
                } else {
                    if (!sendButtonFlag) {
                        sendButtonFlag = true;
                        userActionsEvents.append(CGlobalVariables.SEND_BUTTON_EVENT).append(",");
                        AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                    }
                    identifyLanguageWithStringInput(getTextInput().trim(),true);
                }
            }
        });

        messageTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!typingFlag) {
                    typingFlag = true;
                    userActionsEvents.append(CGlobalVariables.TYPING_EVENT).append(",");
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        messageTextEdit.setOnTouchListener((view, motionEvent) -> {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                messageTextEdit.requestFocus();
                InputMethodManager manager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(messageTextEdit, 0);
                if (!editTextTouchFlag) {
                    editTextTouchFlag = true;
                    userActionsEvents.append(CGlobalVariables.EDITTEXT_TOUCH_EVENT).append(",");
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());

                }
            }
            return false;
        });

    }

    private void sendMessage(long chatId) {
        String messageText = getTextInput();
        addMessageToAdapter(messageText, CGlobalVariables.USER, chatId, true, false);

        CUtils.hideMyKeyboard(this);
        if (isAiBehaveLikeHuman) {
            onShowTypingIndicator(true);
        } else {
            showHideTypingIndicator(View.VISIBLE);
            disableSendButton();
        }
        sendMsgToFirebase(messageText, chatId, CGlobalVariables.USER, CGlobalVariables.ASTROLOGER, "");

        try {
            String oldTopic = CUtils.getStringData(this, CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_KEY, "");
            String newTopic = CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_TOPIC + aiAstrologerId;
            if (TextUtils.isEmpty(oldTopic)) {
                CUtils.saveStringData(this, CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_KEY, newTopic);
                CUtils.subscribeTopics("", newTopic, this);
            } else if (!oldTopic.equals(newTopic)) {
                CUtils.unSubscribeFollowTopic(this, oldTopic);
                CUtils.saveStringData(this, CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_KEY, newTopic);
                CUtils.subscribeTopics("", newTopic, this);
            }
        } catch (Exception e) {
            //
        }
        sendPromptToAI(messageText);
        //sendMsgToFirebase(messageText, chatId, CGlobalVariables.USER, CGlobalVariables.ASTROLOGER);
        clearTextInput();
    }

    private boolean isContain(String inputString, String[] items) {
        String lowerCase = inputString.toLowerCase();
        boolean found = false;
        for (String item : items) {
            String pattern = "\\b" + item + "\\b";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(lowerCase);
            if (m.find()) {
                found = true;
                break;
            }

        }
        return found;
    }


    public void sendMsgToFirebase(String message, long chatId, String from, String to, String msgType) {
        String channelId = CHANNEL_ID;
        Map<String, Object> chatMap = AIChatMessageEngine.buildFirebasePayload(message, chatId, from, to, msgType);
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.MESSAGES_FBD_KEY).push().setValue(chatMap);
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.LAST_MSG_TIME_FBD_KEY).setValue(ServerValue.TIMESTAMP);

        if(msgCount < 4) {
            msgCount++;
            CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "AIChatWindowActivity sendMessage() messageText=" + message);
        }
    }


    public void setMessageInputEnabled(final boolean enabled) {
        /*runOnUiThread(() -> {
            sendButton.setEnabled(enabled);
            messageTextEdit.setEnabled(enabled);
        });*/
    }

    private String getTextInput() {
        return messageTextEdit.getText().toString();
    }

    private void clearTextInput() {
        messageTextEdit.setText("");
    }

    public void showHideTypingIndicator(int visibility) {
        if(visibility == View.VISIBLE){
            messagesListView.post(()->{
                messageAdapter.addTyping();
            });
        }else if(visibility == View.GONE) {
            messageAdapter.removeTyping();
        }

        scrollMyListViewToBottom();
    }

    /*public void updateSeenMsg(Message message) {
        messageAdapter.updateSeenMsg(message);
    }*/

    public void timerStart() {
        if (!TextUtils.isEmpty(userChatTime)) {
            timeSetOnTimer(userChatTime);
            if (CUtils.isFreeChat) {
                tvFreeChatBlinker.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.blinking_animation);
                tvFreeChatBlinker.startAnimation(startAnimation);
            } else {
                tvFreeChatBlinker.setVisibility(View.GONE);
            }
            if (messageAdapter.getItemCount() == 0){
                sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_WELCOME);
                sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_SUGGESTION);
            }
            if (!isChatAgainClicked) {
                Log.e("loadOldChats","timerStatr()");
                sendProfileMsg();
                isTypingGreetingMessage = true;
                isDynamicGreeting = false;
                getGreetingMessage(false);
            } else {
                if (!CUtils.isFromAINotification) {
                    Log.d("welcommessage", "timerStart: welcome");
                    sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_CHAT_AGAIN);
                    sendWelcomeBackMessage();
                } else {
                    isChatAgainClicked = false;
                    CUtils.isFromAINotification = false;
                    if (!TextUtils.isEmpty(AINotificationChatWindowNextQuestion)) {
                        messageTextEdit.setText(AINotificationChatWindowNextQuestion);
                        AINotificationChatWindowNextQuestion = "";
                        identifyLanguageWithStringInput(getTextInput().trim(),true);
                    }
                }
            }
            sendCustomPushNotification(astrologerName, getResources().getString(R.string.ongoing_chat), "");
            isChatCompleted = false;
            scrollMyListViewToBottom();
            registerConnectivityStatusReceiver();
            registerReceiverBackgroundLogin();
            setOnDisconnectListner();
        }
    }

    private void sendWelcomeBackMessage() {
        try {
            isChatCompleted = false;
            int chatId = numRandom.nextInt(999) + 1;
            if (isAiBehaveLikeHuman) {
                setDelay(() -> speakOutGreet(getResources().getString(R.string.welcome_back_message) + "\n", "", String.valueOf(chatId)));
            } else {
                speakOutGreet(getResources().getString(R.string.welcome_back_message) + "\n", "", String.valueOf(chatId));
            }
            isChatCompleted = true;
        } catch (Exception e) {
            Log.d("welcommessage", "sendWelcomeBackMessage e: "+e);
        }
    }

    public void sendCustomStatusMessage(String author) {
        Message message = new Message();
        message.setAuthor(author);
        message.setDateCreated(String.valueOf(System.currentTimeMillis()));
        message.setMessageBody("");
        message.setSeen(false);
        messageAdapter.addMessage(message);
    }

    public void sendLowBalanceMsg() {
        /*StatusMessage statusMessage = new LowBalanceMessage("");
        this.messageAdapter.addStatusMessage(statusMessage);*/
        sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_LOW_BALANCE);
    }

    public void dialogToShowOnEndChat() {
        AstrosageKundliApplication.isEndChatCompleted = true;
        endChatButton.setVisibility(View.GONE);
        clExtendChatReminder.setVisibility(View.GONE);
        saveExtendChatReminderVisibility(false);
        if (isChatAgainClicked) {
            isChatAgainClicked = false;
            AstrosageKundliApplication.isChatAgainButtonClick = false;
        }
        getAstrologerFeedbackStatus();
//        if (!CUtils.isFreeChat) {
//            getAstrologerFeedbackStatus();
//        }
    }

    public void changeVisibilitySendMsgLayout(boolean isVisibleLayout) {
        if (isVisibleLayout) {
            llConnectAgain.setVisibility(View.GONE);
            //bottomSheetDialog.dismiss();
            relSendMessage.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) messagesListView.getLayoutParams();
            lp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.ABOVE, relSendMessage.getId());
            messagesListView.setLayoutParams(lp);

        } else {
            relSendMessage.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) messagesListView.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


//            if(isAiProPlan && !isAiProPlanLimitReached){
//                llConnectAgain.setVisibility(View.VISIBLE);
//                lp.setMargins(0,0,0,150);
//            }

            messagesListView.setLayoutParams(lp);

            scrollMyListViewToBottom();

            //llConnectAgain.setVisibility(View.VISIBLE);
            //bottomSheetDialog.show();
            isChatCompleted = true;
        }
        setMessageInputEnabled(isVisibleLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_chat:
                onBackPressed();
                break;
            case R.id.btn_kundli:
                fcmAnalyticsEvents(CGlobalVariables.KUDALI_FROM_CHAT,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                Intent intent = new Intent(activity, HomeInputScreen.class);
                intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_TYPE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_BASIC);
                activity.startActivity(intent);
                break;
            case R.id.btnMatching:
                fcmAnalyticsEvents(CGlobalVariables.MATCHING_FROM_CHAT,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                Intent intent1 = new Intent(activity,
                        HomeMatchMakingInputScreen.class);
                intent1.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_TYPE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_MATCHING);
                activity.startActivity(intent1);
                break;
            case R.id.btnHoroscope:
                fcmAnalyticsEvents(CGlobalVariables.HOROSCOPE_FROM_CHAT,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                com.ojassoft.astrosage.utils.CUtils.callHoroscopeActivity(activity, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_HOROSCOPE, 0, 0);
                break;
            case R.id.btnPanchang:
                Intent Panchang = new Intent(activity,
                        InputPanchangActivity.class);
                Panchang.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_TYPE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ASTROSAGE_DASHBOARD  );
                Panchang.putExtra("date", Calendar.getInstance());
                Panchang.putExtra("place", "");
                activity.startActivity(Panchang);
                break;
//            case R.id.btnNumerology:
//                fcmAnalyticsEvents(CGlobalVariables.NUMEROLOGY_FROM_CHAT,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
//                Intent intent2 = new Intent(activity, NumerologyCalculatorInputActivity.class);
//                intent2.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_TYPE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ASTROSAGE_NUMROLOGY);
//                intent2.putExtra("FROM", "OMA");
//                startActivity(intent2);
//                break;
            case R.id.end_chat_button:

                CUtils.hideMyKeyboard(this);
                CUtils.fcmAnalyticsEvents("end_chat_button_click", AstrosageKundliApplication.currentEventType, "");
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.end_chat_confirm_dialog, null);
                builder.setView(dialogView);

                TextView end_chat_confirm_text = dialogView.findViewById(R.id.end_chat_confirm_text);
                FontUtils.changeFont(AIChatWindowActivity.this, end_chat_confirm_text, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

                TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
                TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
                final AlertDialog alert = builder.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));


                end_chat_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEnd = "1";
                        CUtils.fcmAnalyticsEvents("end_chat_btn_yes_click", AstrosageKundliApplication.currentEventType, "");
                        alert.dismiss();
                        finishChat(CGlobalVariables.USER_ENDED);
                    }
                });

                end_chat_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CUtils.fcmAnalyticsEvents("end_chat_btn_no_click", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        alert.dismiss();
                    }
                });
                CUtils.fcmAnalyticsEvents("end_chat_dialog_show", AstrosageKundliApplication.currentEventType, "");
                alert.show();
                break;
            case R.id.iv_share_chat:
                //Log.e("SAN ", " CMW action share "  );
                toOpenShareDialog(messageAdapter.getMessageList());
                break;
            case R.id.iv_copy_chat:
                //Log.e("SAN ", " CMW copy chat "  );
                toOpenCopyDialog(messageAdapter.getMessageList(), messageAdapter.getCopyMessageList());
                break;
            case R.id.tvChatRechargeButton:
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_AI_CHAT_WINDOW_CHAT_EXTEND_RECHARGE, FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.utils.CUtils.createSession(this, AI_CHAT_EXTEND_CHAT_RECHARGE);
                //openQuickRechargeSheet();
                bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT;
                if(extendInfo!=null){
                    if(extendableChatDuration > 0){
                        extendChatTime();
                    }else {
                        FragmentManager ft = getSupportFragmentManager();
                        openQuickRechargeSheet(AIChatWindowActivity.this, astrologerName, extendInfo.getMinimumrecharge(), extendInfo.getRemainingwalbal(), ft);
                    }
                }else {
                    getExtendRechargeInfo();
                }
                break;
        }
    }



    private String getUserRemainingBalance() {
        String wallet = CUtils.getWalletRs(this);
        double walletBalance = 0;

        try {
            walletBalance = Double.parseDouble(wallet.replace(",", ""));
        } catch (Exception e) {
            walletBalance = 0;
        }
        // Total talk time already consumed (seconds → minutes)
        double totalTalkTimeMin = Integer.parseInt(connectChatBean.getDurationinsecs()) / 60.0;
        // Amount already used in current session
        //TODO

        double totalCurrentCallAmt = totalTalkTimeMin * Double.parseDouble(extendInfo.getCurrentserviceprice());
        // Remaining wallet balance after current usage
        double remainingBalance = Math.max(0, walletBalance - totalCurrentCallAmt);
        // round to 2 decimal places
        remainingBalance = Math.round(remainingBalance * 100.0) / 100.0;

        return remainingBalance+"";
    }

//    private void getRechargeAmounts() {
//        if (!CUtils.isConnectedWithInternet(AIChatWindowActivity.this)) {
//            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), AIChatWindowActivity.this);
//        } else {
//            if (pd == null)
//                pd = new CustomProgressDialog(AIChatWindowActivity.this);
//            pd.show();
//            pd.setCancelable(false);
//
//            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
//            Call<ResponseBody> callApi = apiList.getRechargeServices(CUtils.getRechargeParams(this, CUtils.getUserID(this)));
//            callApi.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    hideProgressBar();
//                    if (response.body() != null) {
//                        try {
//                            String myResponse = response.body().string();
//                            amountResponse = myResponse;
//                            hideProgressBar();
//                            openQuickRechargeSheet(myResponse);
//                        } catch (Exception e) {
//                            //
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    hideProgressBar();
//                }
//            });
//        }
//    }

    public void initializingCountDownTimer(long totalVerificationTime) {
        boolean newUserAiChat5Min = CUtils.getBooleanData(this, NEW_USER_AI_CHAT_5MIN, false);
        String offerType = CUtils.getCallChatOfferType(activity);
//Log.d("quickrecharge","initializingCountDownTimer="+longTotalVerificationTime);
        timerTextview.setVisibility(View.VISIBLE);
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(totalVerificationTime, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                String text = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
                timerTextview.setText(text);
//tvTimer.setText(text);
                CGlobalVariables.chatTimerTime = millisUntilFinished;
                remainingChatTime = millisUntilFinished;

//showExtendChatView(longTotalVerificationTime <= 180000, text, millisUntilFinished);
/*if (longTotalVerificationTime > 180000) {
showExtendChatView(false, text, millisUntilFinished);
} else {
showExtendChatView(true, text, millisUntilFinished);
}*/
                if (!isAiProPlan) {
                    if (longTotalVerificationTime > 180000) {
                        if (text.equalsIgnoreCase("00:01:00")) {
                            fcmAnalyticsEvents("chat_show_low_balance_msg", AstrosageKundliApplication.currentEventType, "");
                            if (offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) { //if new user price then next condition will be check
                                if (!newUserAiChat5Min) { //in case of 5 min chat is false then show
                                    sendLowBalanceMsg();
                                }
                            } else { //free or regular price then show
                                sendLowBalanceMsg();
                            }
                        }
                        if (millisUntilFinished <= 120000) { //less than 2 minutes
                            if (TextUtils.isEmpty(offerType)) { //extend chat will only display in case of regular price
                                if (clExtendChatReminder.getVisibility() == View.GONE) {
                                    getExtendRechargeInfo();
                                }
                            } else if (offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {//show in case of 5 min chat is false
                                if (!newUserAiChat5Min && clExtendChatReminder.getVisibility() == View.GONE) {
                                    getExtendRechargeInfo();
                                }
                            }
                            if (clExtendChatReminder.getVisibility() == View.VISIBLE && extendableChatDuration > 0) {
                                String remaining;
                                if (minutes >= 1) {
                                    remaining = String.format(Locale.US, "%02dm %02ds", minutes, seconds);
                                } else {
                                    remaining = String.format(Locale.US, "%02ds", seconds);
                                }
                                tvChatRechargeTitle.setText(getResources().getString(R.string.chat_ends_in, remaining));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                if (isQuickRechargeClicked) {
                    isQuickRechargeClicked = false;
                } else {
                    CGlobalVariables.chatTimerTime = 0;
                    try {
                        //checked in case of pro plan or not
                        if (!isAiProPlan) {
                            if (TextUtils.isEmpty(offerType) || offerType.equalsIgnoreCase(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {//regular price or free offer
                                sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_END_DUE_TO_LOW_BALANCE);
                            } else if (offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {//new user offer
                                if (!newUserAiChat5Min) {//if false then show
                                    sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_END_DUE_TO_LOW_BALANCE);
                                }
                            }
                        }
                        fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");
                        finishChat(CGlobalVariables.TIME_OVER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        initEndChatDisableTimer();
    }

//    private void showExtendChatView(boolean isFreeChat, String text, long millisUntilFinished) {
//        if (text.equalsIgnoreCase("00:01:00")) {
//            CUtils.fcmAnalyticsEvents("chat_show_low_balance_msg", AstrosageKundliApplication.currentEventType, "");
//            sendLowBalanceMsg();
//        }
//        long conditionalMillis = (isFreeChat) ? 60000 : 120000; //60 seconds for free and 2 mins for paid chat
//        if (millisUntilFinished <= conditionalMillis) { //less than 1 minutes
//            if (clExtendChatReminder.getVisibility() == View.GONE) {
//                getExtendRechargeInfo();
//            }
//        }
//    }

    private void initNoUserResponseTimer() {
        if (noUserResponseTimer == null) {
            noUserResponseTimer = new CountDownTimer(AUTO_GREETING_TIME, longOneSecond) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    if (!typingFlag) {
                        isDynamicGreeting = true;
                        getGreetingMessage(true);
                    }
                }
            }.start();
        }
    }

    public void initEndChatDisableTimer() {
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer = null;
            endChatButton.setVisibility(View.INVISIBLE);
            endChatButton.setEnabled(false);
        }

        endChatDisabledTimer = new CountDownTimer(AstrosageKundliApplication.endChatTimeShowMilliSeconds, longOneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                AstrosageKundliApplication.endChatTimeShowMilliSeconds = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                endChatButton.setVisibility(View.VISIBLE);
                endChatButton.setEnabled(true);
            }
        }.start();

    }

    public void finishChat(String chatEndedBy) {
        try {
            storedOfferType = CUtils.getCallChatOfferType(this);
            //showHideTypingIndicator(View.GONE);
            if(isAiBehaveLikeHuman)
                onShowTypingIndicator(false);
            else
                showHideTypingIndicator(View.GONE);

            long activeStreamChatId = currentMessage != null ? currentMessage.getChatId() : activeAiAnswerId;
            persistCurrentAiMessageForAdapter();
            stopTypeWriter = true;
            int activeMessagePosition = messageAdapter.finishTyping(CGlobalVariables.ASTROLOGER, activeStreamChatId);
            if (activeMessagePosition != -1) {
                if (currentMessage != null) {
                    currentMessage.setSeen(false);
                    currentMessage.setDelayed(false);
                }
                messageAdapter.refreshSingleItem(activeMessagePosition);
            } else if (messageAdapter.getItemCount() > 0) {
                messageAdapter.stopTyping();
                messageAdapter.refreshSingleItem(messageAdapter.getItemCount() - 1);
            }
            chatDurarion = timeChangeInSecond(timerTextview.getText().toString());
            if (countDownTimer != null) {
                countDownTimer.cancel();
                changeVisibilitySendMsgLayout(false);
                isChatCompleted = true;

                CUtils.fcmAnalyticsEvents("status_user_end_chat_completed", AstrosageKundliApplication.currentEventType, "");
                resetTimerView();
                unRegisterConnectivityStatusReceiver();
                cancelOnDisconnectListner();
                CUtils.cancelNotification(AIChatWindowActivity.this);
                CGlobalVariables.chatTimerTime = 0;
                CUtils.saveAstrologerIDAndChannelID(AIChatWindowActivity.this, "", "");
                if (!chatEndedBy.equalsIgnoreCase(CGlobalVariables.USER_BACKGROUND)) {
                    endChatButton.setVisibility(View.GONE);
                    CUtils.changeFirebaseKeyStatus(CHANNEL_ID, "NA", true, chatEndedBy);
                    chatCompleted(CHANNEL_ID, chatEndedBy);
                } else {
                    CUtils.saveAstrologerIDAndChannelID(AIChatWindowActivity.this, "", "");
                    dialogToShowOnEndChat();
                }
                CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
                messageAdapter.addFollowMessage();
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (tvFreeChatBlinker.getVisibility() == View.VISIBLE) tvFreeChatBlinker.setVisibility(View.GONE);
        } catch (Exception e) {
            //
        }

    }

    /**
     * on Timer Finish
     */
    private void resetTimerView() {
        timerTextview.setVisibility(View.GONE);
        timerTextview.setText("00:00:00");
        countDownTimer = null;
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }
    }


    public void showRatingDialogToUser() {
        try {
            if (TextUtils.isEmpty(astrologerId)) {
                return;
            }
            String phoneNo1 = CUtils.getUserID(context);
            if (feedbackDialog != null && feedbackDialog.isVisible()) {
                return;
            }
            feedbackDialog = new FeedbackDialog(CGlobalVariables.CON_TYPE_CHAT, channelIdForNps, getSupportFragmentManager(), AstrosageKundliApplication.selectedAstrologerDetailBean);
            feedbackDialog.show(getSupportFragmentManager(), "FeedbackDialog");
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN, CGlobalVariables.SHOW_DIALOG_EVENT, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAstrologerFeedbackStatus() {
        /*try {
            long chatDurarionLong = Long.parseLong(chatDurarion);
            Log.e("TestFreeChat", "chatDurarionLong=" + chatDurarionLong);
            if (chatDurarionLong < CGlobalVariables.DURATION_3MIN) { // 180 sec(3 min)
                return;
            }
        } catch (Exception e){
            //
        }*/
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerFeedbackStatus(getAstroFeedbackStatusParams(astrologerId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String myResponse = response.body().string();
                    parseAstrologerStatus(myResponse);
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("TestFreeChat", " AF onFailure="+t);
            }
        });
    }

    public Map<String, String> getAstroFeedbackStatusParams(String astroId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(AI_LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        headers.put("languagecode", String.valueOf(AI_LANGUAGE_CODE));
        headers.put("name", CUtils.getUserFullName(context));
        //Log.e("TestFreeChat", " AF headers="+headers);
        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        if (method == METHOD_RECHARGE) {
            handleWalletRechargeResponse(response);
        }
    }

    private void extendChatTime(){
        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), activity);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Map<String ,String> params = new HashMap<>();
            params.put(CGlobalVariables.APP_KEY,CUtils.getApplicationSignatureHashCode(activity));
            params.put("channelid",CHANNEL_ID);
            CUtils.setRequiredParams(params);
            Call<ResponseBody> call = api.extendConsultation(params);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("extendChatTime", "url : "+response.raw().request().url());
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        Log.d("extendChatTime", "onResponse: "+myResponse);
                        if(jsonObject.optString("status","0").equals("1") ){
                            clExtendChatReminder.setVisibility(View.GONE);
                            saveExtendChatReminderVisibility(false);
                            //long newChatDurationMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(jsonObject.getString("chatduration")));
                            //Log.d("quickrecharge response2","CGlobalVariables.chatTimerTime="+CGlobalVariables.chatTimerTime);
                            long extededChatDurationMills =  TimeUnit.MINUTES.toMillis(jsonObject.getInt("chatduration"));
                            if (llConnectAgain.getVisibility() != View.VISIBLE) {
                                initializingCountDownTimer(CGlobalVariables.chatTimerTime + extededChatDurationMills);
                            }
                        }

                    }catch(Exception e){
                        hideProgressBar();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    hideProgressBar();
                }
            });
        }
    }

    private void openQuickRechargeSheet() {
        try {
            QuickRechargeBottomSheet quickRechargeBottomSheet = QuickRechargeBottomSheet.getInstance();
            Bundle bundle = new Bundle();
            //bundle.putString("mResponse", response);
            bundle.putString("astrologerUrlText", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR,bottomServiceListUsedFor);
            bundle.putString("minBalanceNeededText",minBalText);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AICHATWINDOWACTIVITY);

            quickRechargeBottomSheet.setArguments(bundle);
            quickRechargeBottomSheet.show(getSupportFragmentManager(), QuickRechargeBottomSheet.TITLE);
        } catch (Exception e) {
            //
        }
    }
public  void openQuickRechargeSheet(Activity activity,String astroName, String minBalanceNeededText, String userbalance, FragmentManager fragmentManager) {
    try {
//        isQuickRechargeClicked = true;
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        countDownTimer = null;
//        timeTakenForRecharge = Calendar.getInstance().getTimeInMillis();

        RechargeSuggestionBottomSheet rechargeSuggestionBottomSheet = RechargeSuggestionBottomSheet.getInstance();
        Bundle bundle = new Bundle();
        //bundle.putString("mResponse", response);
        // bundle.putString("astrologerUrlText", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
        bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR, bottomServiceListUsedFor);
        bundle.putString("minBalanceNeededText", minBalanceNeededText);
        bundle.putString("astroName", astroName);
        bundle.putString("userbalance", userbalance);
        bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AICHATWINDOWACTIVITY);
        rechargeSuggestionBottomSheet.setArguments(bundle);
        CUtils.checkCachedData(activity,minBalanceNeededText,userbalance);
        rechargeSuggestionBottomSheet.show(fragmentManager, RechargeSuggestionBottomSheet.TITLE);
    } catch (Exception e) {

    }
}
    @Override
    public void onError(VolleyError error) {
        handleWalletRechargeError();
        hideProgressBar();
    }

    @Override
    protected void onPause() {
        //Log.d("TestlinkIssue","onPause Called");
        isAiChatWindowInForeground = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Log.d("TestlinkIssue","onDestroy Called");
        isAiChatWindowInForeground = false;
        try {
            CUtils.saveChatDraftMessage(activity, astrologerId, getTextInput());
            if (queue != null) {
                queue.cancelAll("chatRequestQueue");
            }
            CUtils.cancelNotification(AIChatWindowActivity.this);
            CUtils.cancelChatNotification(AIChatWindowActivity.this);
        } catch (Exception e) {
            //
        }
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }
        if (endChatRef != null && endChatValueEventListener != null) {
            endChatRef.removeEventListener(endChatValueEventListener);
        }
        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
        }
        super.onDestroy();
    }

    private void parseAstrologerStatus(String response) {
        //Log.e("TestFreeChat", " AF response=" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean isFeedbackEnabled = jsonObject.optBoolean("enablefeedbacks");

            if (isFeedbackEnabled) {
                //Log.e("TestFreeChat", " showRatingDialogToUser");
                showRatingDialogToUser();
            }
            if (followStatus.equals("0")){
                //llStatusLayout.setVisibility(View.VISIBLE);
                //messageAdapter.addFollowMessage();
                scrollMyListViewToBottom();
            }else{
                //llStatusLayout.setVisibility(View.GONE);
                //messageAdapter.removeFollowMessage();
            }

            if (!CUtils.isFreeChat && storedOfferType.isEmpty()){
                getAstrologerStatusPrice(astrologerId,"");
            }


        } catch (Exception e) {
            // Log.e("ASTROLOGER_DE", "exp2=" + e);
        }
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

    public void onShowTypingIndicator(Boolean isTyping) {
        if (isTyping) {
            typingTextview.setVisibility(View.VISIBLE);
            typingTextview.startAnimation(animShow);
        } else {
            typingTextview.startAnimation(animHide);
            typingTextview.setVisibility(View.GONE);
        }
    }


    private void setOnDisconnectListner() {
        try {
            String channelId = CHANNEL_ID;
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().setValue(ServerValue.TIMESTAMP);
        } catch (Exception e) {
            //
        }
    }

    private void cancelOnDisconnectListner() {
        try {
            String channelId = CHANNEL_ID;
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().cancel();
        } catch (Exception e) {
            //
        }
    }

    public void showAstrologorFullProfile() {
        if (astrologerLageProfile != null && astrologerLageProfile.length() > 0) {
            String astroImage = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + astrologerLageProfile;
            FullProfileFragment.getInstant(astrologerName, astroImage).show(getSupportFragmentManager(), FullProfileFragment.TAG);
        }
    }

    private void sendCustomPushNotification(String title, String msg, String link) {
        CUtils.saveStringData(this, CGlobalVariables.TITLE_ONGOING_CHAT, title);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);

        int notificationId = LibCUtils.getRandomNumber();
        //If url contains Play store then use Action view else open the App
        Intent resultIntent = new Intent(this, AIChatWindowActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("ongoing_notification", true);
        resultIntent.setAction(Intent.ACTION_VIEW);

        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CGlobalVariables.NOTIFICATION_CHANNEL_ID).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setContentIntent(pending).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // using the same tag and Id causes the new notification to replace an existing one
        mNotificationManager.notify(CGlobalVariables.ONGOING_NOTIFICATION, 1001, notificationBuilder.build());
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CUtils.cancelNotification(activity, AstrosageKundliApplication.currentDisplayedNotificationTag, AstrosageKundliApplication.currentDisplayedNotificationId);
                }
            }, 5000);
        } catch (Exception e) {
            //
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
        initializingCountDownTimer(longTotalVerificationTime);
    }

    long endChatResponseTime = 0;

    public void chatCompleted(String channelID, String remarks) {
        if (!CUtils.isConnectedWithInternet(AIChatWindowActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), AIChatWindowActivity.this);
        } else {
            showProgressBar();
            endChatResponseTime = Calendar.getInstance().getTimeInMillis();

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks), channelID, getClass().getSimpleName());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    endChatResponseTime = Calendar.getInstance().getTimeInMillis() - endChatResponseTime;
                    String status = "";
                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        status = jsonObject.getString("status");

                        String msg = jsonObject.optString("endMsg");
                        // Log.d("freeChatType", String.valueOf(CUtils.isFreeChat));
                        if (!msg.isEmpty()){
                            if (CUtils.isFreeChat) {
                                long chatId = numRandom.nextInt(999) + 1;
                                addMessageToAdapter(msg,ASTROLOGER,chatId,false,false);
                            }
                            //linLayoutEntChatMessage.setVisibility(View.VISIBLE);
                            scrollMyListViewToBottom();
                        }

                    } catch (Exception e) {
                        status = "";
                    }

                    if (status.equals("1")) {
                        CUtils.startUserAiChatCategoryDataService(activity, offerTypeDuringInitChat, CHANNEL_ID,TYPE_AI_CHAT);
                    } else { // end-chat-api fail
                        setEndChatOverValue(CHANNEL_ID);
                    }
                    CHANNEL_ID = "";
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_COMPLETED;
                    CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                    CUtils.updateChatCallOfferType(AIChatWindowActivity.this, true, CHAT_CLICK);
                    CGlobalVariables.chatTimerTime = 0;
                    CUtils.saveAstrologerIDAndChannelID(AIChatWindowActivity.this, "", "");
                    dialogToShowOnEndChat();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        hideProgressBar();
                        String msg = getResources().getString(R.string.something_went_wrong) + " (" + t.getMessage() + ")";
                        CUtils.showSnackbar(findViewById(android.R.id.content), msg, AIChatWindowActivity.this);
                        endChatButton.setVisibility(View.GONE);
                        clExtendChatReminder.setVisibility(View.GONE);
                        saveExtendChatReminderVisibility(false);
                        CUtils.updateChatCallOfferType(AIChatWindowActivity.this, true, CHAT_CLICK);
                        setEndChatOverValue(CHANNEL_ID);
                        AstrosageKundliApplication.isEndChatReqOnGoing = false;
                        CHANNEL_ID = "";
                    } catch (Exception e) {
                        //
                    }
                }
            });
        }
    }

    /**
     * Displays a bottom sheet dialog prompting the user to continue the chat session.
     *
     * @param servicePrice          The discounted or current service price as a String.
     * @param actualServicePriceInt The original service price as a String (before discount).
     * @param doubleRating          The astrologer's rating as a String.
     * @param currentOfferType      The current offer type identifier as a String.
     *                              <p>
     *                              This method sets up the UI for the bottom sheet, populates astrologer details,
     *                              handles offer display, and manages user actions for continuing or dismissing the chat.
     * @param minbalrequired
     * @param isAvailableForChat
     * @param isAvailableForCall
     */
    private void openBottomSheetChatContinueDialog(String servicePrice, String actualServicePriceInt, String doubleRating, String currentOfferType, String minbalrequired, boolean isAvailableForChat, boolean isAvailableForCall){
        try {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AI_CONTINUE_CHAT_DIALOG_OPEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        shareChat.setVisibility(View.VISIBLE);
        AstrologerDetailBean astrologerDetail_ = AstrosageKundliApplication.selectedAstrologerDetailBean;
        includeLayoutBottomSheet.setVisibility(View.VISIBLE);
        RoundImage imgViewAstrologer = includeLayoutBottomSheet.findViewById(R.id.imgViewAstrologer);
        String astrologerProfileUrl = "";
        if (astrologerDetail_ != null && !TextUtils.isEmpty(astrologerDetail_.getImageFile())) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetail_.getImageFileLarge();
            Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewAstrologer);
        }

        // Log analytics event for dialog open
        TextView txtviewRating = includeLayoutBottomSheet.findViewById(R.id.txtviewRating);
        TextView txtViewAstrologerName = includeLayoutBottomSheet.findViewById(R.id.txtViewAstrologerName);
        TextView txtViewOffer = includeLayoutBottomSheet.findViewById(R.id.txtViewOffer);
        TextView txtViewOneTimeOffer = includeLayoutBottomSheet.findViewById(R.id.txtViewOneTimeOffer);
        TextView minBalNeeded = includeLayoutBottomSheet.findViewById(R.id.txtMinBalNeeded);
        Button btnContinueChat = includeLayoutBottomSheet.findViewById(R.id.btnContinueChat);
        ImageView imgViewCancel = includeLayoutBottomSheet.findViewById(R.id.imgViewCancel);
        RelativeLayout btnChatRL = includeLayoutBottomSheet.findViewById(R.id.btnChatRL);
        RelativeLayout btnCallRL = includeLayoutBottomSheet.findViewById(R.id.btnCallRL);


        boolean isAiCallShowInAIChat = CUtils.getBooleanData(this, IS_AI_CALL_SHOW_IN_AI_CHAT, false);

        if (isAiCallShowInAIChat && isAvailableForCall) {
            btnCallRL.setVisibility(View.VISIBLE);
        }else {
            btnCallRL.setVisibility(View.GONE);
        }

        txtviewRating.setText(doubleRating);
        if(astrologerDetail_ != null) {
            txtViewAstrologerName.setText(astrologerDetail_.getName());
        }
        minBalText  = getResources().getString(R.string.minBalNeeded).replace("####",minbalrequired);
        minBalNeeded.setText(minBalText);
        int servicePrice_ = Integer.parseInt(servicePrice);
        int actualPrice_ = Integer.parseInt(actualServicePriceInt);


        // Set offer text based on plan and offer type
        if (isAiProPlan) {
            minBalNeeded.setVisibility(View.GONE);
            String freeText= getString(R.string.title_free).trim();
            setFormattedFreeText(txtViewOffer,getString(R.string.continue_this_chat_with_expert_insights_free, userNameTempStore),freeText);
        }else {
            if (!TextUtils.isEmpty(currentOfferType)  ) {
                if (currentOfferType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE) && CUtils.isFreeChat){
                    //txtViewOffer.setText(getString(R.string.continue_this_chat_with_expert_insights_free, userNameTempStore));

                    //if user have free chat then show free text
                    String freeText = getString(R.string.title_free).trim();
                    setFormattedFreeText(txtViewOffer, getString(R.string.continue_this_chat_with_expert_insights_free, userNameTempStore), freeText);
                }else {
                    //if user have Reduced price offer then show reduced price
                    if (actualPrice_ > servicePrice_) {
                        setFormattedText(txtViewOffer, "₹" + getString(R.string.astrologer_rate, servicePrice_), "₹" + getString(R.string.astrologer_rate, actualPrice_));
                    } else {
                        txtViewOffer.setText(getString(R.string.continue_your_expert_consultation,  "₹" + getString(R.string.astrologer_rate, servicePrice_), ""));
                        //setFormattedText(txtViewOffer,"","₹"+getString(R.string.astrologer_rate,servicePrice_));
                    }
                    //if user is New user then show one time offer
                    txtViewOneTimeOffer.setVisibility(View.VISIBLE);
                    txtViewOneTimeOffer.setText(R.string.one_time_offer);
                }

            } else if (actualPrice_ > servicePrice_) {
                //if user is not new user then show offer price and actual price and hide one time offer
                txtViewOneTimeOffer.setVisibility(View.GONE);
                setFormattedText(txtViewOffer, "₹" + getString(R.string.astrologer_rate, servicePrice_), "₹" + getString(R.string.astrologer_rate, actualPrice_));
            } else {
                //if user is not new user then show only offer price and hide one time offer
                txtViewOneTimeOffer.setVisibility(View.GONE);
                txtViewOffer.setText(getString(R.string.continue_your_expert_consultation,  "₹" + getString(R.string.astrologer_rate, servicePrice_), ""));
                //setFormattedText(txtViewOffer,"","₹"+getString(R.string.astrologer_rate,servicePrice_));
            }
        }

        // Cancel button action
        imgViewCancel.setOnClickListener(v -> {
            // Dismiss logic if needed
         //   bottomSheetDialog.dismiss();
        });

            // chat button action
            btnContinueChat.setOnClickListener(v ->{

            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_CONTINUE_AI_CHAT_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            //llStatusLayout.setVisibility(View.GONE);
            messageAdapter.removeFollowMessage();
            scrollMyListViewToBottom();
            isChatAgainClicked = true;
            AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;

            setMessageInputEnabled(true);
              llConnectAgain.setVisibility(View.GONE);
            //bottomSheetDialog.dismiss();
            shareChat.setVisibility(View.GONE);

            // Apply offer flags if needed
//            if (currentOfferType.equalsIgnoreCase(CGlobalVariables.INTRO_OFFER_TYPE_FREE) ||
//                    currentOfferType.equalsIgnoreCase(CGlobalVariables.REDUCED_PRICE_OFFER)){
            if(AstrosageKundliApplication.chatAgainAstrologerDetailBean != null) {
                if (!TextUtils.isEmpty(currentOfferType)) {
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setUseIntroOffer(true);
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setFreeForChat(true);
                } else {
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setUseIntroOffer(false);
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setFreeForChat(false);
                }
            }
            try {
                if (CUtils.isChatNotInitiated()) {
                    ChatUtils.getInstance(AIChatWindowActivity.this).initAIChat(AstrosageKundliApplication.chatAgainAstrologerDetailBean);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), context);
                }

            }catch (Exception e){
                //
            }


            // Update chat state
            if (AstrosageKundliApplication.ASTROLOGER_ID != null && !AstrosageKundliApplication.ASTROLOGER_ID.equals(astrologerId)) {
                AstrosageKundliApplication.chatMessagesHistory = null;
                //AstrosageKundliApplication.statusMessageSetHistory = null;
                AstrosageKundliApplication.isChatAgainButtonClick = false;
            }
            AstrosageKundliApplication.chatMessagesHistory = messageAdapter.getUpdatedMessageList();
            //AstrosageKundliApplication.statusMessageSetHistory = messageAdapter.getStatusList();
            AstrosageKundliApplication.ASTROLOGER_ID = astrologerId;
            AstrosageKundliApplication.AI_ASTROLOGER_ID = aiAstrologerId;
            AstrosageKundliApplication.isChatAgainButtonClick = true;
           // bottomSheetDialog.dismiss();
            //overLayBottomLayout.setVisibility(View.GONE);
            shareChat.setVisibility(View.GONE);

        });

        btnCallRL.setOnClickListener(v ->{
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_CONTINUE_AI_CALL_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            if(AstrosageKundliApplication.selectedAstrologerDetailBean != null){
            if (!TextUtils.isEmpty(currentOfferType)) {
                AstrosageKundliApplication.selectedAstrologerDetailBean.setUseIntroOffer(true);
                AstrosageKundliApplication.selectedAstrologerDetailBean.setUseIntroOffer(true);
                AstrosageKundliApplication.selectedAstrologerDetailBean.setFreeForCall(true);
            }else{
                AstrosageKundliApplication.chatAgainAstrologerDetailBean.setUseIntroOffer(false);
                AstrosageKundliApplication.chatAgainAstrologerDetailBean.setFreeForChat(false);
                AstrosageKundliApplication.selectedAstrologerDetailBean.setFreeForCall(false);
            }
            }
            // Toast.makeText(AIChatWindowActivity.this, "Call feature coming soon", Toast.LENGTH_SHORT).show();

            if(ContextCompat.checkSelfPermission(AIChatWindowActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
                astrologerDetail_.setCallSource(CGlobalVariables.AI_CHAT_ACTIVITY_CALL_BTN);
                ChatUtils.getInstance(AIChatWindowActivity.this).initVoiceCall(astrologerDetail_);
            }else{
                CUtils.showPreMicPermissionDialog(this,()->{
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                });
            }
        });
        scrollMyListViewToBottom();
        }catch (Exception e){
            CUtils.showSnackbar(containerLayout, "e2"+e, AIChatWindowActivity.this);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> { // The callback lambda
                if (isGranted) {
                    ChatUtils.getInstance(AIChatWindowActivity.this).initVoiceCall(AstrosageKundliApplication.selectedAstrologerDetailBean);
                } else {
                    CUtils.showPermissionDeniedDialog(this);
                }
            });


    public void setFormattedFreeText(TextView txtView,String text1, String freeText){
        try {


        // String text = "Hi "+userNameTempStore + " continue this chat with expert insights at just " + offerPrice + " " + actualPrice + ".";

        // Create a SpannableString from the text
        SpannableString spannableString = new SpannableString(text1);

        // Apply orange color to the offer price
        // int orangeColor = Color.parseColor("#FFA500"); // Orange color code
        if (text1.contains(freeText)){
            int startIndexfreeText = text1.indexOf(freeText);
            spannableString.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorPrimary_day_night)),
                    startIndexfreeText,
                    startIndexfreeText + freeText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        txtView.setText(spannableString);
        }catch (Exception e){
            CUtils.showSnackbar(containerLayout, "e3"+e.toString(), AIChatWindowActivity.this);
        }
    }

    public void setFormattedText(TextView textView, String offerPrice, String actualPrice) {
        try {
        // Create the dynamic text
       // String text = "Hi "+userNameTempStore + " continue this chat with expert insights at just " + offerPrice + " " + actualPrice + ".";
        String text1 = getString(R.string.continue_your_expert_consultation,offerPrice,actualPrice);
        //String text1 = getString(R.string.continue_this_chat_with_expert_insights_at_just_min, userNameTempStore,offerPrice,actualPrice);

        // Create a SpannableString from the text
        SpannableString spannableString = new SpannableString(text1);
        try {
            // Apply orange color to the offer price
            // int orangeColor = Color.parseColor("#FFA500"); // Orange color code
            int startIndexOffer = text1.indexOf(offerPrice);
            spannableString.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary_day_night)),
                    startIndexOffer,
                    startIndexOffer + offerPrice.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            // Apply gray color and strikethrough to the discount price
            // int grayColor = Color.GRAY;
            int startIndexDiscount = text1.indexOf(actualPrice);
            spannableString.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(this, R.color.grey_light)),
                    startIndexDiscount,
                    startIndexDiscount + actualPrice.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            spannableString.setSpan(
                    new StrikethroughSpan(),  // This adds the cut line
                    startIndexDiscount,
                    startIndexDiscount + actualPrice.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }catch(Exception ignore){}

        // Set the SpannableString to the TextView
        textView.setText(spannableString);
        }catch (Exception e){
            CUtils.showSnackbar(containerLayout, "e4"+e.toString(), AIChatWindowActivity.this);
        }
    }

    public Map<String, String> getChatCompleteParams(String channelID, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(AIChatWindowActivity.this));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
        headers.put(CGlobalVariables.CHAT_DURATION, timeChangeInSecond(timerTextview.getText().toString()));
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(AI_LANGUAGE_CODE));
        headers.put("languagecode", String.valueOf(AI_LANGUAGE_CODE));
        headers.put("name", CUtils.getUserFullName(context));
        //Log.e("TestFreeChat params ", headers.toString());
        if(remarks!=null && remarks.equals(CGlobalVariables.AI_PASS_QUESTION_LIMIT_REACHED)&& getAnswerFromServer!=null){
            headers.put("limitreached", getAnswerFromServer.isQuestionLimitReached);
            headers.put("questionlimit", getAnswerFromServer.questionNoLimit);
        }
        return CUtils.setRequiredParams(headers);
    }

    private void getAstrologerStatusPrice(String astroid, String currentOfferType) {

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerStatusPrice(getAstroStatusParams(astroid, currentOfferType));
        //Log.e("TestNewUser", call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String myResponse = response.body().string();
                    //Log.e("priceIssue", myResponse);
                    JSONObject jsonObject = new JSONObject(myResponse);
                    String servicePrice = jsonObject.getString("servicePrice");
                    String actualServicePriceInt = jsonObject.getString("actualServicePriceInt");
                    String doubleRating = jsonObject.getString("doubleRating");
                    String minbalrequired = jsonObject.getString("minbalrequired");
                    boolean isAvailableForChat = jsonObject.optBoolean("isAvailableForChat", false);
                    boolean isAvailableForCall = jsonObject.optBoolean("isAvailableForCall", false);
                    if(!isAiProPlanLimitReached){
                        openBottomSheetChatContinueDialog(servicePrice,actualServicePriceInt,doubleRating,currentOfferType,minbalrequired,isAvailableForChat,isAvailableForCall);
                    }
                    //parseAstrologerStatus(myResponse);

                } catch (Exception e) {

                    CUtils.showSnackbar(containerLayout, "e1"+e.toString(), AIChatWindowActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), AIChatWindowActivity.this);
            }
        });
    }

    public Map<String, String> getAstroStatusParams(String astroId, String currentOfferType) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
            //
        }
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(activity));
//        if (currentOfferType.equalsIgnoreCase(CGlobalVariables.INTRO_OFFER_TYPE_FREE) ||
//                currentOfferType.equalsIgnoreCase(CGlobalVariables.REDUCED_PRICE_OFFER)){
        if (!TextUtils.isEmpty(currentOfferType)) {
            headers.put(CGlobalVariables.USE_INTRO_OFFER, "1");
        }else {
            headers.put(CGlobalVariables.USE_INTRO_OFFER, "2");
        }
       // headers.put(CGlobalVariables.OFFER_FROM_NOTIFICATION,"0");
       /* String offerType = CUtils.getCallChatOfferType(this);
        if (offerType == null) offerType = "";
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.USE_INTRO_OFFER, "2");
        headers.put(CGlobalVariables.OFFER_FROM_NOTIFICATION,"0");*/
        //Log.e("TestNewUser", "headers ai = " + headers);
        return headers;
    }
    private String timeChangeInSecond(String userRemainingTime) {
        //talktime = "36:24 minutes";
        String userChatDuration = "00";
        long totalTime = (long) 0.0, remainingUserTime = (long) 0.0, actualChatUserTime = (long) 0.0;
        String[] timeArray;
        try {
            if (userChatTime.length() > 0) {
                timeArray = userChatTime.split(":");
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
            userChatDuration = String.valueOf(actualSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userChatDuration;
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

    private void setConnectivityRegainInFirebase() {
        try {
            String channelId = CHANNEL_ID;
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child("UserConnRegainTime").setValue(ServerValue.TIMESTAMP);
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
                //////Log.e("ConnectivityStatus", "TimerRemove");
            }
        } catch (Exception e) {
            //
        }
    }

    public void timerStop() {
        if (countDownTimer != null) {
            timeChangeInSecond(timerTextview.getText().toString());
            countDownTimer.cancel();
        }
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }
        timerTextview.setText("00:00:00");
        countDownTimer = null;
        typingTextview.startAnimation(animHide);
        typingTextview.setVisibility(View.GONE);
    }

    public Map<String, String> getChatAcceptedParams(String channelID) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.CHAT_DURATION, changeMinToSec());
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(AI_LANGUAGE_CODE));
        //Log.d("testNewDialog", "getChatAcceptedParams   =    " + headers.toString());

        return headers;
    }

    public String changeMinToSec() {
        String seconds = "00";
        if (userChatTime != null && userChatTime.length() > 0) {
            String[] time = userChatTime.split(" ");
            if (time != null && time.length > 0) {
                String[] minSec = time[0].split(":");
                if (minSec != null && minSec.length > 1) {
                    int convertSec = Integer.parseInt(minSec[0]) * 60;
                    int totalSec = convertSec + Integer.parseInt(minSec[1]);
                    seconds = String.valueOf(totalSec);
                }
            }
        }
        return seconds;
    }

    public void showProgressBar() {
        try {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {

        }
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BACK_FROM_PROFILE_CHAT_DIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                String fromWhere = data.getStringExtra("fromWhere");
                if (isProceed) {
                    AstrosageKundliApplication.backgroundLoginCountForChat = 0;
                    if (!TextUtils.isEmpty(fromWhere) && fromWhere.equalsIgnoreCase("profile_send")) {
                        errorLogs = errorLogs + "goto api hit\n";
                        sendProfileMsg();
                    }
                }

                if (!isProceed && data.getExtras().containsKey("openKundliList")) {
                    CUtils.openSavedKundliList(this, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
                } else if (!isProceed && data.getExtras().containsKey("openProfileForChat")) {
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")) {
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChat(this, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), "profile_send", bundle, prefillData, BACK_FROM_PROFILE_CHAT_DIALOG);
                }
            }

        } else if (requestCode == CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE) {
            isQuickRechargeClicked = false;
            if (data != null) {
                Bundle bundle = data.getExtras();
                boolean isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
                if (isRecharged) {
                    String orderID = bundle.getString(CGlobalVariables.ORDER_ID);
                    String orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS);
                    String rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT);
                    String paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE);
                    String razorpayid = bundle.getString("razorpayid");
                    String phonepeid = bundle.getString(CGlobalVariables.PHONEPE_ID);
                    String phonepeOrderId = bundle.getString(CGlobalVariables.PHONEPE_ORDER_ID);
                    String od = bundle.getString(CGlobalVariables.ORDER_ID_PHONEPE);
                    // Log.d("isRecharged","orderID = "+ orderID + " orderStatus = "+ orderStatus + " recharge = "+ rechargeAmount);
                    //demoTxt.setText("orderID = "+ orderID + " orderStatus = "+ orderStatus + " recharge = "+ rechargeAmount);
                    if (orderStatus.equals("0")) {
                        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE_FAILED, FIREBASE_EVENT_ITEM_CLICK, "");
                        initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                        udatePaymentStatusOnserveronFailed(containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode);
                    } else {
                        timeTakenForRecharge = Calendar.getInstance().getTimeInMillis() - timeTakenForRecharge;
                        int extendedWaitTime = Math.toIntExact(timeTakenForRecharge / 1000);

                        updatePaymentStatusOnServer(containerLayout, orderStatus, orderID,
                                rechargeAmount, queue, paymentMode, razorpayid, extendedWaitTime,phonepeid,phonepeOrderId,od);
                    }
                }
            } else {
                initializingCountDownTimer(CGlobalVariables.chatTimerTime);
            }
        } else if(requestCode == BACK_FROM_PLAN_PURCHASE_AD_SCREEN){
            Intent rashiPredictionIntent = new Intent(this, DetailedHoroscope.class);
            rashiPredictionIntent.putExtra("rashiType", com.ojassoft.astrosage.utils.CUtils.getMoonSignIndex(this));
            rashiPredictionIntent.putExtra("prediction_type", 1);//com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_HOROSCOPE);
            rashiPredictionIntent.putExtra("screenType", 0);
            startActivity(rashiPredictionIntent);
        }
    }

    public void addHistory() {
        try {
            if (AstrosageKundliApplication.ASTROLOGER_ID.equals(astrologerId) && AstrosageKundliApplication.chatMessagesHistory != null && !AstrosageKundliApplication.chatMessagesHistory.isEmpty()) {
                if (AstrosageKundliApplication.isChatAgainButtonClick || onJoinChatClick) {
                    AstrosageKundliApplication.isChatAgainButtonClick = false;
                    if (CUtils.isFromAINotification) {
                        sendCustomStatusMessage(CGlobalVariables.CHAT_STATUS_WELCOME);
                        previousAns = AstrosageKundliApplication.chatMessagesHistory.get(0).getMessageBody(this);
                        previousQues = AINotificationChatWindowTitle;
                        isLocallyAnswered = "1";
                        for (ChatMessage message : AstrosageKundliApplication.chatMessagesHistory) {
                            String to = (message.getAuthor().equals(CGlobalVariables.ASTROLOGER)) ? CGlobalVariables.USER : CGlobalVariables.ASTROLOGER;
                            sendMsgToFirebase(message.getMessageBody(this), message.chatId(), message.getAuthor(), to, "");
                        }
                    }

                    messageAdapter.addAllMessages(AstrosageKundliApplication.allChatMessagesHistory);
                     scrollMyListViewToBottom();
                }
            }
        } catch (Exception e) {
            //
        }
    }

            public void openWalletScreen(String openFrom) {
                if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AI_CHAT_WINDOW_LOW_BALANCE_OPEN_WALLET, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    com.ojassoft.astrosage.utils.CUtils.createSession(this, AI_CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID);
                }else {
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AI_CHAT_WINDOW_LOW_BALANCE_SUBSCRIBE_OPEN_WALLET, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    com.ojassoft.astrosage.utils.CUtils.createSession(this, AI_CHAT_WINDOW_SUBSCRIBE_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID);

        }
        openWalletScreen();
//                if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
//                    if (CUtils.getCountryCode(AIChatWindowActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                        //intent = new Intent(AIChatWindowActivity.this, MiniPaymentInformationActivity.class);
//                        bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT;
//                        openQuickRechargeSheet();
//                    }else {
//                        openWalletScreen();
//                    }
//                }else {
//                    openWalletScreen();
//                }


    }

    private void openWalletScreen() {
        Intent intent = new Intent(AIChatWindowActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AICHATWINDOWACTIVITY);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        saveExtendChatReminderVisibility(clExtendChatReminder.getVisibility() == View.VISIBLE);
        super.onBackPressed();
        String analyticsLabel="";
        if(isChatCompleted){
            analyticsLabel = com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_AI_CHAT_BACK_PRESS+"_"+CHAT_COMPLETED_KEY;
        }else{
            analyticsLabel = com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_AI_CHAT_BACK_PRESS+"_"+DURING_CHAT_KEY;
        }
       // Log.e(TAG, "onBackPressed: "+analyticsLabel );
        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(analyticsLabel, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


        if (countDownTimer != null) {
            chatWindowOpenType = "";
            if (!backPressFlag) {
                backPressFlag = true;
                userActionsEvents.append(CGlobalVariables.BACK_PRESS_EVENT).append(",");
                AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "AIChatWindowActivity onBackPressed() userActionsEvents=" + userActionsEvents);
            }

            AstrosageKundliApplication.chatMessagesHistory = messageAdapter.getUpdatedMessageList();
            AstrosageKundliApplication.ASTROLOGER_ID = astrologerId;

            if (!CUtils.checkServiceRunning(OnGoingChatService.class)) {
                startService();
            } else {
                stopService(new Intent(activity, OnGoingChatService.class));
                startService();
            }
        } else {
            if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
                stopService(new Intent(activity, OnGoingChatService.class));
            }
            if (isChatCompleted) {
                AstrosageKundliApplication.chatMessagesHistory = null;
                AstrosageKundliApplication.allChatMessagesHistory = null;
                //AstrosageKundliApplication.statusMessageSetHistory = null;
                AstrosageKundliApplication.isChatAgainButtonClick = false;
            }
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }

        if (isActivityRunning(ActAppModule.class)
                || isActivityRunning(DashBoardActivity.class)) {
            // Do Noting
        } else {
            Intent intent = new Intent(AIChatWindowActivity.this, DashBoardActivity.class);
            startActivity(intent);
        }

        finish();


    }

    public String changeToMinSec() {
        try {
            long millis = CGlobalVariables.chatTimerTime;
            String ms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            return ms;
        } catch (Exception e) {
            return "00:00";
        }

    }


    protected Boolean isActivityRunning(Class activityClass) {
        try {
            ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
            for (ActivityManager.RunningTaskInfo task : tasks) {
                if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                    return true;
            }
        } catch (Exception e) {
            //
        }
        return false;
    }

    public String convertDateTime(String dateInMilliseconds) {

        String dateFormat = "dd MMM yyyy hh:mm a";
        String dateTime = "";
        try {
            if (dateInMilliseconds != null && dateInMilliseconds.length() > 0) {
                dateTime = DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
            }
        } catch (Exception e) {
        }

        return dateTime;
    }

    public void toOpenShareDialog(List<ChatMessage> messageList) {
        try {
            String textToShare = "";
            String author = "";
            //Log.e("SAN ", " CMW toOpenShareDialog() messageList.size()=" + messageList.size());
            if (messageList.size() > 0) {
                for (int i = 0; i < messageList.size(); i++) {
                    //Log.e("SAN ", " CMW toOpenShareDialog() messageList.get(i)=" + messageList.get(i));

                    if (messageList.get(i) instanceof LeftStatusMessage
                            || messageList.get(i) instanceof WelcomeStatusMessage
                            || messageList.get(i) instanceof ContactNumberRestrictMessage) {
                        continue;
                    } else {

                        ChatMessage cmi = messageList.get(i);
                        //Log.e("SAN ", " CMW toOpenShareDialog() cmi=" + cmi);

                        if (cmi.getAuthor().equalsIgnoreCase(CGlobalVariables.USER))
                            author = userNameTempStore;
                        else
                            author = astrologerName;

                        //textToShare = textToShare + "[" +  convertDateTime( cmi.getDateCreated() ) + "] " + cmi.getAuthor() +": " + cmi.getMessageBody(activity) + "\n" ;
                        textToShare = textToShare + "[" + cmi.getDateCreated() + "] " + author + ": " + removeHtmlTags(cmi.getMessageBody(activity)) + "\n";

                        //Log.e("SAN ", " CMW loop textToShare => " + textToShare);

                    }

                }
            }

            //Log.e("SAN ", " CMW toOpenShareDialog() textToShare=" + textToShare);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, textToShare);
            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //AnalyticsUtils.setAnalytics(context, "Chat Share Clicked");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.text_share_it)));
            } catch (android.content.ActivityNotFoundException ex) {
                //Log.e("SAN ", " CMW toOpenShareDialog() Exp1=" + ex);
            }
        } catch (Exception e) {
            //Log.e("SAN ", " CMW toOpenShareDialog() Exp2=" + e);
        }
    }

    public void toOpenCopyDialog(List<ChatMessage> messageList, List<Integer> messageCopyList) {
        try {
            String textToCopy = "";
            String author = "";

            if (!messageCopyList.isEmpty()) {
                if (messageCopyList.size() == 1) {
                    int position = messageCopyList.get(0);
                    textToCopy = messageList.get(position).getMessageBody(activity);
                } else {
                    Collections.sort(messageCopyList);
                    for (int i = 0; i < messageCopyList.size(); i++) {
                        int position = messageCopyList.get(i);
                        if (messageList.get(position) instanceof LeftStatusMessage
                                || messageList.get(position) instanceof WelcomeStatusMessage
                                || messageList.get(i) instanceof ContactNumberRestrictMessage) {
                            continue;
                        } else {

                            ChatMessage cmi = messageList.get(position);

                            if (cmi.getAuthor().equalsIgnoreCase(CGlobalVariables.USER))
                                author = userNameTempStore;
                            else
                                author = astrologerName;

                            textToCopy = textToCopy + "[" + cmi.getDateCreated() + "] " + author + ": " + cmi.getMessageBody(activity) + "\n";

                        }

                    }
                }
                com.ojassoft.astrosage.utils.CUtils.copyTextToClipBoard(textToCopy.replace("<br>", "\n")
                        .replace("<strong>", "")
                        .replace("<em>", "")
                        .replace("</em>", "")
                        .replace("</strong>", ""), this);
                clearCopyList();
            } else {
                Toast.makeText(activity, "Please select chat message", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            //
        }

    }

    public void clearCopyList() {
        messageAdapter.getCopyMessageList().clear();
        updateCopyView(false);
        messageAdapter.notifyDataSetChanged();
        messageAdapter.isLongPressClicked = false;
    }

    private long timeTakenForRecharge = 0;

    public void gotoPaymentInfoActivity(int mSelectedPosition, WalletAmountBean walletAmountBean) {
        isQuickRechargeClicked = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
        timeTakenForRecharge = Calendar.getInstance().getTimeInMillis();
        //Log.d("quickrecharge","gotoPaymentInfoActivity="+CGlobalVariables.chatTimerTime);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "AIQuickRechargeBottomSheet");
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        bundle.putString("screen_open_from", "QuickRechargeBottomSheet");
        bundle.putString(CGlobalVariables.CHANNEL_ID, CHANNEL_ID);
        Intent intent;
        // condition for open MiniPayment info or Payment info dont remove
        if (CUtils.getCountryCode(AIChatWindowActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
             intent = new Intent(context, MiniPaymentInformationActivity.class);
        }else {
             intent = new Intent(context, PaymentInformationActivity.class);
        }

        intent = new Intent(context, PaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE);
    }
    public void gotoPaymentInfoActivityVariableAmount(int mSelectedPosition, WalletAmountBean walletAmountBean,String recharge_amount) {
        isQuickRechargeClicked = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
        timeTakenForRecharge = Calendar.getInstance().getTimeInMillis();
        //Log.d("quickrecharge","gotoPaymentInfoActivity="+CGlobalVariables.chatTimerTime);
        Bundle bundle = new Bundle();
        if (mSelectedPosition == -1) {
            bundle.putString(CGlobalVariables.RECHARGE_AMOUNT, recharge_amount);
            bundle.putString("frompopunder", "1");
        }
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "AIQuickRechargeBottomSheet");
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        bundle.putString("screen_open_from", "QuickRechargeBottomSheet");
        bundle.putString(CGlobalVariables.CHANNEL_ID, CHANNEL_ID);
        Intent intent;
        // condition for open MiniPayment info or Payment info dont remove
        if (CUtils.getCountryCode(AIChatWindowActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
            intent = new Intent(context, MiniPaymentInformationActivity.class);
        }else {
            intent = new Intent(context, PaymentInformationActivity.class);
        }

        intent = new Intent(context, PaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE);
    }
    public void gotoMiniPaymentInfoActivity(int mSelectedPosition, WalletAmountBean walletAmountBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.AICHATWINDOWACTIVITY);
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        Intent intent = new Intent(context, MiniPaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void updatePaymentStatusOnServer(View containerLayout, String orderStatus, String orderID,
                                            String amount, RequestQueue queue, String paymentMode,
                                            String razorpayid, int extendedWaitTime, String phonepeid,
                                            String phonepeOrderId, String od) {
        this.amount = amount;
        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), activity);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.walletRecharge(getParamsForWalletRecharge(orderStatus, orderID, razorpayid, extendedWaitTime,phonepeid,phonepeOrderId,od));
            //  Log.d(TAG, "quickrecharge url: "+call.request().url());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        hideProgressBar();
                        try {
                            if(RechargeSuggestionBottomSheet.getInstance()!=null){
                                RechargeSuggestionBottomSheet.getInstance().dismiss();
                            }
                            //QuickRechargeBottomSheet.getInstance().dismiss();

                        }catch (Exception e){
                            Log.e("testLogs",e+"");
                        }

                        tvFreeChatBlinker.setVisibility(View.GONE);
                        String myResponse = response.body().string();
                        Log.d("quickrecharge", "myResponse: " + myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("msg");
                        if (status.equals("1")) {
                            //Log.d("quickrecharge response", "1");
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_SUCCESS, FIREBASE_EVENT_PAYMENT_SUCCESS, "");
                            clExtendChatReminder.setVisibility(View.GONE);
                            saveExtendChatReminderVisibility(false);
                            long newChatDurationMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(jsonObject.getString("chatduration")));
                            //Log.d("quickrecharge response2","CGlobalVariables.chatTimerTime="+CGlobalVariables.chatTimerTime);
                            if (llConnectAgain.getVisibility() != View.VISIBLE) {
                                initializingCountDownTimer(CGlobalVariables.chatTimerTime + newChatDurationMillis);
                            }
                            /*if(!bottomSheetDialog.isShowing()){
                                initializingCountDownTimer(CGlobalVariables.chatTimerTime + newChatDurationMillis);
                            }*/
                        } else {
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_FAILED, FIREBASE_EVENT_PAYMENT_FAILED, "");
                            initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                            CUtils.showSnackbar(containerLayout, msg, activity);
                        }
                    } catch (Exception e) {
                        if (llConnectAgain.getVisibility() != View.VISIBLE) {
                            initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                        }
                        /*if(!bottomSheetDialog.isShowing()){
                            initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                        }*/
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), activity);
                }
            });
        }
    }

    private Map<String, String> getParamsForWalletRecharge(String orderStatus, String orderId,
                                                           String razorpayid, int extendedWaitTime, String phonepeid,
                                                           String phonepeOrderId, String od) {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(activity));
            headers.put("od", orderId);
            headers.put("isSucess", orderStatus);
            headers.put("paycurr", "INR");
            headers.put("razorpayid", razorpayid);
            headers.put("rechargefromchat", "1");
            headers.put("urltext", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            headers.put("channelid", CHANNEL_ID);
            headers.put("extendedwaittime", String.valueOf(extendedWaitTime));
            if(!TextUtils.isEmpty(od)) {
                headers.put(CGlobalVariables.ORDER_ID_PHONEPE, od);
                headers.put(CGlobalVariables.PHONEPE_ID, phonepeid);
                headers.put(CGlobalVariables.PHONEPE_ORDER_ID, phonepeOrderId);
            }
            //headers.put("callfrom", "admin");
        } catch (Exception e) {
            //
        }
        return CUtils.setRequiredParams(headers);

    }

    RechargePopUpAfterFreeChat rechargePopUpAfterFreeChat;

    public void showRechargeAfterFreeChat() {
        try {
            if (feedbackDialog != null && feedbackDialog.isVisible()) {
                return;
            }
            if (rechargePopUpAfterFreeChat != null && rechargePopUpAfterFreeChat.isVisible()) {
                return;
            }
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_NEXT_OFFER_RECHARGE_DIALOG,
                    CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            rechargePopUpAfterFreeChat = new RechargePopUpAfterFreeChat(CGlobalVariables.AICHATWINDOWACTIVITY);
            rechargePopUpAfterFreeChat.show(getSupportFragmentManager(),
                    "RechargePopUpAfterFreeChat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setEndChatOverValue(String channelID) {
        if (!TextUtils.isEmpty(channelID)) {
            AstrosageKundliApplication.getmFirebaseDatabase(channelID).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY).setValue(true);
        }
    }

    private void getExtendRechargeInfo() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.extendRechargeInfo(getExtendRechargeInfoParams());
        //Log.d("getExtendRechargeInfo",call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String myRespose = response.body().string();
                    extendInfo = new ExtendInfo();
                    Log.d("getExtendRechargeInfo",myRespose);
//                    {
//                        "serviceprice" : "1.0",
//                            "minimumrecharge" : "5.0",
//                            "chatduration" : "0",
//                            "remainingwalbal" : "45.0",
//                            "currentserviceprice" : "1.0"
//                    }
                    JSONObject jsonObject = new JSONObject(myRespose);
                    extendInfo.setServiceprice( jsonObject.getString("serviceprice"));
                    extendInfo.setMinimumrecharge( jsonObject.getString("minimumrecharge"));
                    extendableChatDuration = jsonObject.optInt("chatduration",0);
                    try {
                        extendInfo.setCurrentserviceprice(jsonObject.getString("currentserviceprice"));
                        extendInfo.setRemainingwalbal( jsonObject.getString("remainingwalbal"));
                        extendInfo.setChatduration(jsonObject.getString("chatduration"));
                    }catch (Exception e){
                        extendInfo.setRemainingwalbal(getUserRemainingBalance());
                        extendInfo.setChatduration("0");
                    }
                    serviceprice = jsonObject.getString("serviceprice");
                    minimumrecharge = jsonObject.getString("minimumrecharge");
                    clExtendChatReminder.setVisibility(View.VISIBLE);
                    saveExtendChatReminderVisibility(true);

                    if(extendableChatDuration > 0){
                        tvExtendChatMsg.setVisibility(View.VISIBLE);
                        tvChatRechargeButton.setText(getResources().getString(R.string.extend_now));
                    }else{
                        String lowBalance = getString(R.string.low_balance);
                        if (getString(R.string.low_balance).contains("**")) {
                            lowBalance = lowBalance.replace("**", serviceprice);
                            tvChatRechargeTitle.setText(lowBalance);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public Map<String, String> getExtendRechargeInfoParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(AIChatWindowActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(AIChatWindowActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(AI_LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(AIChatWindowActivity.this));
        headers.put("channelid", CHANNEL_ID);
        return headers;
    }

    /**
     * Builds a channel-specific preference key for persistently tracking reminder visibility.
     */
    private String getExtendChatReminderVisibilityKey() {
        return AI_EXTEND_REMINDER_VISIBLE_KEY_PREFIX + CHANNEL_ID;
    }

    /**
     * Stores whether the extend reminder should be restored for the current active channel.
     */
    private void saveExtendChatReminderVisibility(boolean isVisible) {
        if (!TextUtils.isEmpty(CHANNEL_ID)) {
            CUtils.saveBooleanData(this, getExtendChatReminderVisibilityKey(), isVisible);
        }
    }

    /**
     * Restores the reminder after rejoin by re-fetching extend info when it was visible earlier.
     */
    private void restoreExtendChatReminderIfNeeded() {
        if (TextUtils.isEmpty(CHANNEL_ID)) {
            return;
        }
        boolean shouldRestore = CUtils.getBooleanData(this, getExtendChatReminderVisibilityKey(), false);
        if (shouldRestore) {
            // Consume the saved state on join to prevent stale true values in preferences.
            saveExtendChatReminderVisibility(false);
        }
        if (shouldRestore && clExtendChatReminder.getVisibility() == View.GONE) {
            getExtendRechargeInfo();
        }
    }

    private void followStatus() {
        try {
            if (followAstrologerModelArrayList != null && followAstrologerModelArrayList.size() > 0) {

                for (int i = 0; i < followAstrologerModelArrayList.size(); i++) {

                    if (followAstrologerModelArrayList.get(i).getAstrologerId().equalsIgnoreCase(astrologerId)) {
                        followStatus = "1";
                        //followAstrologer.setText(getString(R.string.following));
                        break;
                    }

                }
            }
        } catch (Exception e) {
            //
        }
    }


    private void getFollowingAstrologerDataFromServer() {
        //showProgressBar();
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), this);
        } else {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_FOLLOWING_ASTRO_URL,
//                    FollowingAstrologerActivity.this, false, CUtils.getFollowingAstroParams(this), 42).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.getFollowedAstrologers(com.ojassoft.astrosage.varta.utils.CUtils.getFollowingAstroParams(this));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        // hideProgressBar();
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);
                        JSONObject result = jsonObject.getJSONObject("result");
                        String status = result.getString("status");
                        if (status.equals("1")) {
                            parseFollowingAstrologerList(responses);
                        }
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });

        }
    }
    private void getIsUserFollowingAstrologer(){
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), this);
        } else {
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.getIsUserFollowingAstrologer(com.ojassoft.astrosage.varta.utils.CUtils.getIsUserFollowingAstrologerParams(this,astrologerId));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);
                        String status = jsonObject.getString("status");
                        if(status.equals("1")){
                            String isFollowing = jsonObject.getString("isfollowing");
                            if(isFollowing.equals("true")){
                                followStatus = "1";
                                // followAstrologer.setText(getString(R.string.following));
                            }
                            if(isFollowing.equals("false")){
                                followStatus = "0";
                                // followAstrologer.setText(getString(R.string.follow));
                            }

                        }

                    }catch (Exception e){
                        //
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // hideProgressBar();
                }
            });

        }
    }
    private void parseFollowingAstrologerList(String liveAstroData) {
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }
        try {
            if (followAstrologerModelArrayList == null)
                followAstrologerModelArrayList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologerslist");
            followAstrologerModelArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if (object.getString("astrologerId").equalsIgnoreCase(astrologerId)) {
                    followStatus = "1";
                    //followAstrologer.setText(getString(R.string.following));
                    break;
                }

            }

        } catch (Exception e) {
            followAstrologerModelArrayList.clear();
        }
    }

    private String follow;

    public void onFollowClick(String followAstro) {



        if (CUtils.isConnectedWithInternet(this)) {

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.followAstrologer(getfollowAstrologerParams(followAstro));
            //Log.d("followUnFollow","Url= "+call.request().url());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String myRespose = response.body().string();
                        JSONObject jsonObject = new JSONObject(myRespose);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        //Log.d("followUnFollow",myRespose.toString());
                        if(status.equals("1")) {
                            if (followAstro.equalsIgnoreCase("0")) {
                                followStatus = "0";
                                // followAstrologer.setText(getString(R.string.follow));
                                CUtils.unSubscribeFollowTopic(context, astrologerId);
                            } else {
                                followStatus = "1";
                                // followAstrologer.setText(getString(R.string.following));
                                CUtils.subscribeFollowTopic(context, astrologerId);
                                CUtils.addFollowEventForAstrologer(astrologerId,"AIChatWindowActivity");

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
        } else {
            //CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), AstrologerDescriptionActivity.this);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> getfollowAstrologerParams(String followAstro) {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(this));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(this));
        headers.put(CGlobalVariables.KEY_FOLLOW, followAstro);
        headers.put(CGlobalVariables.KEY_IAPI, "1");
        headers.put(CGlobalVariables.ACTIONSOURCE, CUtils.getActivityName(this));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        return headers;
    }

    private void openProfileDialog() {
        try {
            CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response, int requestCode) {
        hideProgressBar();
        if (requestCode == METHOD_RECHARGE) {
            if (response.body() != null) {
                try {
                    String myResponse = response.body().string();
                    handleWalletRechargeResponse(myResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        handleWalletRechargeError();
        hideProgressBar();
    }

    GetAnswerFromServer getAnswerFromServer;

    public void sendPromptToAI(String messageText) {
        if (CUtils.isConnectedWithInternet(this)) {
            try {
                if (getAnswerFromServer == null) {
                    getAnswerFromServer = new GetAnswerFromServer(AIChatWindowActivity.this, aiAstrologerId);
                }
                if(isAiBehaveLikeHuman)
                    getAnswerFromServer.getAnswerResponse(AIChatWindowActivity.this, messageText);
                else
                    getAnswerFromServer.getQueryAnswer(AIChatWindowActivity.this, messageText);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            responseErrorHandling(false);
            Toast.makeText(this, getResources().getString(R.string.no_internet_tap_to_retry), Toast.LENGTH_SHORT).show();
        }
    }

    public void responseErrorHandling(boolean showError) {
        runOnUiThread(() -> {
            //showHideTypingIndicator(View.GONE);
            if(isAiBehaveLikeHuman) {
                onShowTypingIndicator(false);
            } else{
                showHideTypingIndicator(View.GONE);
                enableSendButton();
            }

            if (showError) {
                messageAdapter.markLastQuestionError();
            }
        });
    }

    public void showSendAgainAlert(String question) {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle(getString(R.string.send_again_title))
                .setMessage(getString(R.string.send_agin_msg))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .setPositiveButton(R.string.try_again, (dialogInterface, i) -> {
                    messageTextEdit.setText(question);
                    dialogInterface.dismiss();
                }).create();
        dialog.show();
    }

    public void speakOut(String result, String link,  String answerId) {
        showHideTypingIndicator(View.GONE);

        if (TextUtils.isEmpty(answerId)) {
            answerId = String.valueOf(numRandom.nextInt(999) + 1);
        }

        if(isAiBehaveLikeHuman) {
            result = result.replace("\n","<br>");
            String finalAnswerId = answerId;
            String finalResult = result;
            setDelay(()->{
                addMessageToAdapter(finalResult, CGlobalVariables.ASTROLOGER, Long.parseLong(finalAnswerId), false, false);
            });
        }else {
            messageChunks = split(result);
            addMessageToAdapter(messageChunks.get(0), CGlobalVariables.ASTROLOGER, Long.parseLong(answerId), true, false);
        }
        long chatId = 0;
        try {
            chatId = Long.parseLong(answerId);
        } catch (Exception e) {
            //
        }
        sendMsgToFirebase(result, chatId, CGlobalVariables.ASTROLOGER, CGlobalVariables.USER, "");

    }

    public void speakOutGreet(String result, String link, String answerId) {
        showHideTypingIndicator(View.GONE);
        if(isChatCompleted) return;

        if (TextUtils.isEmpty(answerId)) {
            answerId = String.valueOf(numRandom.nextInt(999) + 1);
        }
        addMessageToAdapter(result, CGlobalVariables.ASTROLOGER, Long.parseLong(answerId), !isAiBehaveLikeHuman, true);
        long chatId = 0;
        try {
            chatId = Long.parseLong(answerId);
        } catch (Exception e) {
            //
        }
        sendMsgToFirebase(result, chatId, CGlobalVariables.ASTROLOGER, CGlobalVariables.USER, CGlobalVariables.MSG_TYPE_GREET);

    }

    public Message addMessageToAdapter(String messageText, String messageFrom, long chatId, boolean animate, boolean isGreetingMsg) {
        Message message = AIChatMessageEngine.createMessage(messageText, messageFrom, chatId, animate, isAiBehaveLikeHuman, getMessageDate(), System.currentTimeMillis());
        Runnable addMessageTask = () -> messageAdapter.addMessage(message);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            addMessageTask.run();
        } else {
            messagesListView.post(addMessageTask);
        }
        scrollMyListViewToBottom();
        if(AIChatMessageEngine.shouldPersistLocally(messageFrom, isGreetingMsg)) {
            saveMessageInList(message);
            ChatHistoryDAO.getInstance(activity).addMessage(new UserMessage(message),CHANNEL_ID,astrologerId);
        }
        return message;
    }

    private void saveMessageInList(Message message){
        if (AstrosageKundliApplication.allChatMessagesHistory == null) {
            AstrosageKundliApplication.allChatMessagesHistory = new ArrayList<>();
        }
        UserMessage temp =new UserMessage(message);
        temp.setSeen(false);
        AstrosageKundliApplication.allChatMessagesHistory.add(temp);
    }

    public int getChatLangCode() {
        /*int chatLangCode=CUtils.getIntData(AIChatWindowActivity.this, CGlobalVariables.app_language_key, CGlobalVariables.ENGLISH);
        if(MainActivity.langCode.equals ( "hi" ))
            chatLangCode=CGlobalVariables.HINDI;
        else if(MainActivity.langCode.equals ( "en" ))
            chatLangCode= CGlobalVariables.ENGLISH;*/
        return CGlobalVariables.ENGLISH;
    }

    private void getGreetingMessage(boolean isDynamicGreeting) {
        //showHideTypingIndicator(View.VISIBLE);
        //shimmerLayoutChat.stopShimmer();
        //shimmerLayoutChat.setVisibility(View.GONE);
        if(isAiBehaveLikeHuman) {
            onShowTypingIndicator(true);
        }else {
            showHideTypingIndicator(View.VISIBLE);
            disableSendButton();
        }

        try {
            Call<GreetingsModel> call = RetrofitClient.getAIInstance().create(ApiList.class).getGreetingMessage(CUtils.getGreetingMessageParams(this,aiAstrologerId,AI_LANGUAGE_CODE,isDynamicGreeting));
            call.enqueue(new Callback<GreetingsModel>() {
                @Override
                public void onResponse(@NonNull Call<GreetingsModel> call, @NonNull Response<GreetingsModel> response) {
                    if (response.isSuccessful()) {
                        GreetingsModel greetingsModel = response.body();
                        if (greetingsModel != null && (greetingsModel.getStatus() == 1)) {
                            int chatId = numRandom.nextInt(999) + 1;
                            if(isAiBehaveLikeHuman){
                                setDelay(()->{
                                    speakOutGreet(greetingsModel.getMessage() + "\n", "", String.valueOf(chatId));
                                });
                            }else {
                                speakOutGreet(greetingsModel.getMessage() + "\n", "", String.valueOf(chatId));
                            }
//                            addMessageToAdapter(greetingsModel.getMessage(), CGlobalVariables.ASTROLOGER, chatId, true);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GreetingsModel> call, @NotNull Throwable t) {
                    //showHideTypingIndicator(View.GONE);
                    if(isAiBehaveLikeHuman) {
                        onShowTypingIndicator(false);
                    } else {
                        showHideTypingIndicator(View.GONE);
                        enableSendButton();
                    }
                }
            });


        } catch (Exception e) {
            //showHideTypingIndicator(View.GONE);
            if(isAiBehaveLikeHuman) {
                onShowTypingIndicator(false);
            } else{
                showHideTypingIndicator(View.GONE);
                enableSendButton();
            }
        }

    }

    private ImageView ivStop, ivSend;
    public String finalJSONData;

    public void toggleSendStopButtonVisibility(boolean visibility) {
        if (visibility) {
            //disable handler if a message finished typing before 10 seconds
            if (stopHandler != null) {
                stopHandler.removeCallbacks(stopRunnable);
                stopHandler = null;
            }
            enableSendButton();
            if (llChatButton != null)
                llChatButton.setVisibility(View.VISIBLE);

            if(!isTypingGreetingMessage) {
                // Do not force adapter rebind here; typewriter UI is already rendered on the visible item.
                // Rebinding at this point can collapse the response back to the first chunk.
            }

            if (messageChunks != null) {
                messageChunks.clear();
            }
            if (scrollWhileTypeWriter) {
                scrollMyListViewToBottom();
            }
            /*if (TextUtils.isEmpty(AstrosageKundliApplication.tempNameForAiTarot) && isTypingGreetingMessage) {
                sendProfileMsg();
            }*/
            if (isTypingGreetingMessage) {
                isTypingGreetingMessage = false;
                if (!isDynamicGreeting) {
                    initNoUserResponseTimer();
                }
                if(isKundliCalculated) {
                    isKundliCalculated = false;
                    addKundliToAdapter();
                }
            }
            try {
                RecyclerView.ViewHolder holder = messagesListView.findViewHolderForAdapterPosition(messageAdapter.getItemCount() - 1);
                if (holder != null) {
                    holder.itemView.findViewById(R.id.llButtonsParent).setVisibility(View.VISIBLE);
                }
                currentTypeWriter = null;
                isTypeWriterTyping =false;
            } catch (Exception e) {
                //
            }
        } else {
            initStopHandler();
            /*ivSend.setVisibility(View.GONE);
            ivStop.setVisibility(View.VISIBLE);*/
        }
    }

    /*
     * handler to enable stop button after 10 seconds if
     * a long message is typing
     * */
    private void initStopHandler() {
        stopRunnable = () -> {
            if (sendButton != null) {
                sendButton.setEnabled(true);
                sendButton.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_orange));
            }
        };
        stopHandler = new Handler();
        stopHandler.postDelayed(stopRunnable, 10000);
    }

    private Handler stopHandler;
    private Runnable stopRunnable;

    public void scrollMyListViewToBottom() {
        try {
            if (messagesListView != null) {
                messagesListView.post(() -> {
                    final int y = messageAdapter.getItemCount()-1;
                    mLayoutManager.scrollToPositionWithOffset(y,Integer.MIN_VALUE);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void checkViewPosition(View view) {

        try {
            // Get the location and size of the toolbar
            int[] toolbarLocation = new int[2];
            toolbarChat.getLocationOnScreen(toolbarLocation);
            int toolbarTop = toolbarLocation[1];
            int toolbarHeight = toolbarChat.getHeight();
            int toolbarBottom = toolbarTop + toolbarHeight;

// Get the location of the type_writer_layout
            int[] typeWriterLocation = new int[2];
            view.getLocationOnScreen(typeWriterLocation);
            int typeWriterTop = typeWriterLocation[1];
            int typeWriterHeight = view.getHeight();
            int typeWriterBottom = typeWriterTop + typeWriterHeight;

// Check if type_writer_layout touches or overlaps the toolbar's bottom
            if (typeWriterTop <= toolbarBottom) {
                // The view is fully covering the screen
                scrollWhileTypeWriter = false;
                imgViewDowmArrow.setVisibility(View.VISIBLE);
                // Log.d("fullScreenCover", "Covered Full Screen");
            } else {
                // The type_writer_layout is not touching the toolbar
                // Perform other actions if needed
                imgViewDowmArrow.setVisibility(View.GONE); // Example action
                // Log.d("fullScreenCover", "Not Covered Full Screen");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void updateCopyView(boolean stage) {
        if (stage) {
            shareChat.setVisibility(View.VISIBLE);
            copyChat.setVisibility(View.VISIBLE);
            //deleteChat.setVisibility(View.VISIBLE);
        } else {
            shareChat.setVisibility(View.GONE);
            copyChat.setVisibility(View.GONE);
            //deleteChat.setVisibility(View.GONE);
        }
    }

    public void updateStreamedMessage(String newStream, int position) {

        //messageAdapter.updateStreamedMessageInAdapter(newStream, position);
    }

    public static String getMessageDate() {
        return new SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(new java.util.Date());
    }

    public void sendProfileMsg() {
        try {
            CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "AIChatWindowActivity sendProfileMsg() called");
            UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(this);
            if (!com.ojassoft.astrosage.utils.CUtils.isCompleteUserData(userProfileData)) {
                return;
            }  else if (!userProfileData.isProfileSendToAstrologer()) {
                userProfileData.setProfileSendToAstrologer(true);
                CUtils.saveProfileForChatInPreference(this, userProfileData);
                return;
            }
            userProfileData = com.ojassoft.astrosage.utils.CUtils.prepareUserProfile(this);
            String messageText = sendProfileDetailMsg(userProfileData);
            // messageTextEdit.setText(messageText);
            // identifyLanguageWithStringInput(false);
            if (messageText != null && messageText.length() > 0) {
                long chatId = new Random().nextInt(999) + 1;
                sendMsgToFirebase(messageText, chatId, CGlobalVariables.USER, CGlobalVariables.ASTROLOGER, "");
                // messagesListView.setAdapter(messageAdapter);
                addMessageToAdapter(messageText, CGlobalVariables.USER, chatId, true, false);
            }
            if(isShowKundli) {
                calculateKundli(getUserBirthDetailBean(userProfileData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendProfileDetailMsg(UserProfileData userProfileData) {

        String userProfileMsg = "";

        userNameTempStore = userProfileData.getName();
        if (TextUtils.isEmpty(userNameTempStore)) {
            userNameTempStore = "User";
        }

        String placeOfBirth = userProfileData.getPlace() == null ? "" : userProfileData.getPlace();
        String dob = CUtils.appendZeroOnSingleDigit(Integer.parseInt(userProfileData.getDay())) + "/" + CUtils.appendZeroOnSingleDigit(Integer.parseInt(userProfileData.getMonth())) + "/" + Integer.parseInt(userProfileData.getYear());
        String tob = CUtils.convertTimeToHrMtScAmPm(userProfileData.getHour() + ":" + userProfileData.getMinute() + ":" + userProfileData.getSecond());
        String gender = userProfileData.getGender();

        if (gender.equalsIgnoreCase("m")) {
            gender = "Male";
        } else if (gender.equalsIgnoreCase("f")) {
            gender = "Female";
        }

        String maritalStatus = userProfileData.getMaritalStatus();
        String occupation = userProfileData.getOccupation();

        userProfileMsg = CGlobalVariables.HI + "," + "\n" + CGlobalVariables.BELOW_ARE_MY_DETAILS + ":" + "\n" + CGlobalVariables.DATE_OF_BIRTH + ": " + dob + "\n" + CGlobalVariables.TIME_OF_BIRTH + ": " + tob + "\n" + CGlobalVariables.PLACE_OF_BIRTH + ": " + placeOfBirth + "\n" + CGlobalVariables.GENDER + ": " + gender + "\n" + CGlobalVariables.MARITAL_STATUS + ": " + maritalStatus + "\n" + CGlobalVariables.OCCUPATION + ": " + occupation;

        return userProfileMsg;
    }

    private void identifyLanguageWithStringInput(String text, boolean isProceedToSendMessage) {
        if (text.isEmpty()) {
            return;
        }
        long chatId = numRandom.nextInt(999) + 1;

        try {
            LanguageIdentificationOptions identifierOptions = new LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.8f).build();
            LanguageIdentifier languageIdentifier = LanguageIdentification.getClient(identifierOptions);
            languageIdentifier.identifyPossibleLanguages(text).addOnSuccessListener(identifiedLanguages -> {});
            langCode = "en";
            languageIdentifier.identifyLanguage(text).addOnSuccessListener(languageCode -> {

                Log.d("speakOutMsg", "languageCode: "+languageCode);

                //if passing app language code like 6 for bengali then not getting response

                iLangCode = languageCode;
                langCode = checkValidLanguage(languageCode);

                if (checkRomanLang(text) || languageCode.equals("hi-Latn")) {
                    isRoman = 1;
                } else {
                    isRoman = 0;
                }
                if(isProceedToSendMessage) {
                    sendMessage(chatId);
                    List<String> wordsNeedNotToUpdate = CGlobalVariables.wordsNeedNotToUpdate;
                    if (!isWordsNeedNotToUpdate(text, wordsNeedNotToUpdate)) {
                        AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.KEY_LAST_MSG_LANG_CODE).setValue(getILangCode());
                    }
                }else{
                    speakOutMessage(text,languageCode);
                }

            }).addOnFailureListener(e -> {
                if(!isProceedToSendMessage) return;
                setMessageLang(text);
                sendMessage(chatId);
            });
        } catch (Exception exception) {
            if(!isProceedToSendMessage) return;
            setMessageLang(text);
            sendMessage(chatId);
        }
    }
    public static boolean isWordsNeedNotToUpdate(String sentence, List<String> wordsNeedNotToUpdate) {
        String lowerCaseSentence = sentence.toLowerCase();
        if(sentence.split(" ").length < 4) {
            return false;
        }
        for (String word : wordsNeedNotToUpdate) {
            if (lowerCaseSentence.equals(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    private String checkValidLanguage(String detectedLanguageCode) {
        String languageCode = "";
        if (Arrays.asList(validLanguages).contains(detectedLanguageCode)) {
            languageCode = detectedLanguageCode;
        } else if (detectedLanguageCode.equals("hi-Latn")) {
            languageCode = "hi";
        } else {
            languageCode = getAppLanguage();
        }
        return languageCode;
    }

    private String getAppLanguage() {
        String appLanguageCode = CUtils.getLanguageKey(AI_LANGUAGE_CODE);
        if (appLanguageCode.equals("ka")) appLanguageCode = "kn";

        return appLanguageCode;
    }

    private void setMessageLang(String text) {
        try {
            if (checkRomanLang(text)) {
                langCode = getAppLanguage();
                isRoman = 1;
            } else if (isHindiLanguage(text.charAt(0))) {
                langCode = "hi";
                isRoman = 0;
            } else {
                langCode = getAppLanguage();
                isRoman = 0;
            }
        }catch (Exception e){
            //
        }
    }

    public static String[] arrCommonWordsHindi = {
            "aap", "aapke", "apake", "agar", "aur", "bataiye", "bataye", "bahut", "chota", "bada", "badi",
            "chahta", "chahti", "dikkat", "dino", " & _", "ghar", "gyi", "gayi", "gaya",
            "gya", "ghata", "hoga", "hogi", "hoon", "hai", "han", "haan", "hun", "hu",
            "hamara", "humara", "humare", "hamare", "hamari", "humari", " & _ ", "inka",
            "inhe", "inke", "janna", "jana", "janta", "janti", "jeevan", "jivan",
            "jivansathi", "jeevansathi", " & _", "kintu", "kinhi", "ki", "ka", "kyo",
            "kyu", "karu", "kab", "kabtak", "karna", "karni", "karta", "karti", "kyuki",
            "kuch", "krna", "kaunsa", "konsa", "kare", " & _", "liya", "liye", "lena",
            "liye", "lekin", "ladka", "ladki", "ladke", " & _", "mujhe", "meri", "mera",
            "mein", "milker", "mein", "mere", "milega", "milegi", "muje", "munafa", " & _",
            "na", "nahi", "nahe", "naukri", "nokri", "pasand", "pyar", "pati", "patni",
            "raha", "rha", "rehta", "samadhan", "sathi", "sath", "tak", "tang", " & _",
            "umar", "umer", "uska", "usse", "uchit", "vivran", "vivah", "wo", "kaise"};

    public static boolean checkRomanLang(String question) {
        boolean boolVal = false;
        String[] quesSplitWords = question.toLowerCase().split(" ");
        List<String> commonWordsList = Arrays.asList(arrCommonWordsHindi);
        for (String quesSplitWord : quesSplitWords) {
            for (int j = 0; j < commonWordsList.size(); j++) {
                if (commonWordsList.contains(quesSplitWord)) {
                    boolVal = true;
                    break;
                }
            }
            if (boolVal) {
                break;
            }
        }
        return boolVal;
    }

    public static boolean isHindiLanguage(char c) {
        return c >= '\u0900' && c <= '\u097F';
    }

    public static int isRomanLang() {
        int langCodeIsRoman = 0;
        if (langCode.equalsIgnoreCase("und")) {
            langCodeIsRoman = 0;
        } else if (langCode.equalsIgnoreCase("en")) {
            langCodeIsRoman = 0;
        } else if (langCode.equalsIgnoreCase("hi")) {
            langCodeIsRoman = 0;
        } else if (langCode.equalsIgnoreCase("hi-Latn")) {
            langCodeIsRoman = 1;
        } else {
            langCodeIsRoman = 1;
        }
        return langCodeIsRoman;
    }

    public boolean END_CHAT_DATA = false;

    private void initEndChatListener(String channelIDD) {
        endChatValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    END_CHAT_DATA = (boolean) dataSnapshot.getValue();
                    if (END_CHAT_DATA && CGlobalVariables.CHAT_END_STATUS.equals(CGlobalVariables.CHAT_INTERNET_DISCONNECTION)) {
                        try {
                            finishChat(CGlobalVariables.CHAT_INTERNET_DISCONNECTION);
                            CUtils.fcmAnalyticsEvents("firebase_success_end_chat_due_to_internet", AstrosageKundliApplication.currentEventType, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (END_CHAT_DATA && CGlobalVariables.CHAT_END_STATUS.equals(CGlobalVariables.USER_BACKGROUND)) {
                        try {
                            finishChat(CGlobalVariables.USER_BACKGROUND);
                            CUtils.fcmAnalyticsEvents("firebase_success_end_chat_user_background", AstrosageKundliApplication.currentEventType, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        endChatRef = AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY);
        endChatRef.addValueEventListener(endChatValueEventListener);
    }

    public static void setLikeUnlike(Context context, long answerId, int isLiked,int isUnLike) {
        try {
            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).setLikeUnlike(
                    CUtils.getMyAndroidId(context),
                    CUtils.getAppPackageName(context),
                    String.valueOf(CUtils.getAppVersion(context)),
                    CUtils.getApplicationSignatureHashCode(context),
                    "getuserlikedresponse",
                    ""+answerId,
                    isLiked,
                    CUtils.getCountryCode(context) + CUtils.getUserID(context));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.has("message")) {
                                Log.d("MyTag", "likeUnlikeResponse->" + jsonObject.optString("message"));
                            }

                        } catch (Exception e) {
                            //
                        }
                    }

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                    Log.e("MyTag", "likeUnlikeResponse->" + t);

                }
            });
            ChatHistoryDAO.getInstance(context).setLikeDislike(answerId,isLiked,isUnLike);

        } catch (Exception e) {
            //
        }

    }


    public void shareMessage(String question, String answer) {
        ShareMessageFragment.getInstance(question, answer).show(getSupportFragmentManager(), ShareMessageFragment.TAG);
    }

    private void registerReceiverBackgroundLogin() {
        try {
            LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverBackgroundLoginService
                    , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        } catch (Exception e) {
            //
        }
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
                String currentOfferType = CUtils.getCallChatOfferType(activity);
                if (isChatCompleted) {
                    if (CUtils.isFreeChat && currentOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) { //in case of user took complete free chat and now got REDUCEDPRICE
                        CUtils.callLocalNotificationForNewUser(AIChatWindowActivity.this);
                        showRechargeAfterFreeChat();
                        checkConfigrationForContinueChat(astrologerId, currentOfferType);
                    } else if (CUtils.isFreeChat && currentOfferType.equalsIgnoreCase(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) { //in case of user took incomplete free chat and still has FIRSTSESSIONFREE
                        getAstrologerStatusPrice(astrologerId, currentOfferType);
                    } else if (!CUtils.isFreeChat && currentOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) { //in case of user took incomplete REDUCEDPRICE chat and still has REDUCEDPRICE
                        checkConfigrationForContinueChat(astrologerId, currentOfferType);
                    } else { //in case of user took incomplete REDUCEDPRICE chat and now has no offer
                        getAstrologerStatusPrice(astrologerId, currentOfferType);
                    }
                }
            } catch (Exception e) {
                //
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CUtils.updateChatBackgroundBasedOnTheme(this); // Call the method when theme changes
    }


    ArrayList<String> messageChunks;
    boolean isTypeWriterTyping = false;
    Message currentMessage;
    private long activeAiAnswerId = -1L;
    private int pendingChunkRetryCount = 0;
    private long lastAiChunkUpdateTimeMs = 0L;
    private int lastHandledTypingIndex = -1;
    private int typeWriterSafetyToken = 0;

    /**
     * Appends one compact UI-side AI trace line for later chunk mismatch analysis.
     *
     * @param stage short stage identifier for the current UI event
     * @param details stage-specific state details
     */
    private void appendAiTrace(String stage, String details) {
        AIChatTraceLogger.append(this, stage, details);
    }

    /**
     * Refreshes the adapter row that belongs to the currently active streamed AI answer.
     *
     * This avoids accidentally updating trailing status rows when the active answer bubble is not
     * the last adapter item anymore.
     *
     * @param updatedText latest full text for the active AI answer
     * @param chatId active AI answer chat id
     */
    private void refreshActiveAiMessage(String updatedText, long chatId) {
        if (messageAdapter == null || messageAdapter.getItemCount() == 0) {
            return;
        }
        if (chatId <= 0L) {
            messageAdapter.updateMessageInAdapter(updatedText);
            return;
        }
        int updatedIndex = messageAdapter.updateMessageInAdapter(chatId, updatedText);
        if (updatedIndex >= 0) {
            messageAdapter.refreshSingleItem(updatedIndex);
        } else {
            messageAdapter.updateMessageInAdapter(updatedText);
        }
    }

    /**
     * Completes the active chunk if typing progress stalls before the expected text is fully rendered.
     *
     * @param typeWriter active typewriter view for the chunk
     * @param chunkPosition position index of the active chunk
     * @param chunkText full expected chunk text
     */
    public void scheduleTypewriterSafetyCompletion(TypeWriter typeWriter, int chunkPosition, String chunkText) {
        if (typeWriter == null || TextUtils.isEmpty(chunkText)) {
            return;
        }
        final int safetyToken = ++typeWriterSafetyToken;
        final int expectedLength = removeHtmlTags(chunkText).trim().length();
        long delayMs = Math.max(4000L, Math.min(20000L, expectedLength * 70L));
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (safetyToken != typeWriterSafetyToken) {
                return;
            }
            if (currentTypeWriter != typeWriter || currentTypeWriterPosition != chunkPosition || stopTypeWriter) {
                return;
            }
            CharSequence visibleText = typeWriter.getText();
            int visibleLength = visibleText == null ? 0 : visibleText.toString().trim().length();
            if (expectedLength > 0 && visibleLength < expectedLength) {
                Log.d("AIChunkDebug", "Typewriter safety completion fired. position=" + chunkPosition
                        + ", visible=" + visibleLength + ", expected=" + expectedLength);
                appendAiTrace("UI_SAFETY_COMPLETE", "position=" + chunkPosition
                        + ", visibleLen=" + visibleLength
                        + ", expectedLen=" + expectedLength
                        + ", activeAnswerId=" + activeAiAnswerId
                        + ", preview=" + AIChatTraceLogger.preview(chunkText));
                typeWriter.setText(chunkText, this);
            }
        }, delayMs);
    }

    public void setResponse(String updatedString, boolean isTypeWriterEffect, long answerId) {
        runOnUiThread(() -> {
            if(isChatCompleted) return;
            appendAiTrace("UI_SET_RESPONSE", "typeWriter=" + isTypeWriterEffect
                    + ", answerId=" + answerId
                    + ", activeAnswerId=" + activeAiAnswerId
                    + ", currentChatId=" + (currentMessage != null ? currentMessage.getChatId() : -1)
                    + ", currentTypePos=" + currentTypeWriterPosition
                    + ", bodyLen=" + (updatedString == null ? 0 : updatedString.length())
                    + ", preview=" + AIChatTraceLogger.preview(updatedString));

            if (isTypeWriterEffect) {
                if(isAiBehaveLikeHuman)
                    onShowTypingIndicator(false);
                else
                    showHideTypingIndicator(View.GONE);

                if (currentMessage == null || activeAiAnswerId != answerId) {
                    AIChatStreamCoordinator.StartPlan startPlan = AIChatStreamCoordinator.planTypewriterStart(
                            updatedString,
                            answerId,
                            currentMessage != null,
                            activeAiAnswerId,
                            numRandom
                    );
                    if (startPlan.shouldAddMessage()) {
                        currentMessage = addMessageToAdapter(startPlan.messageBody(), CGlobalVariables.ASTROLOGER, startPlan.activeAnswerId(), true, false);
                        activeAiAnswerId = startPlan.activeAnswerId();
                    } else if (startPlan.shouldRefreshExistingMessage() && currentMessage != null) {
                        currentMessage.setMessageBody(startPlan.messageBody());
                        refreshActiveAiMessage(startPlan.messageBody(), startPlan.activeAnswerId());
                    }
                    appendAiTrace("UI_START_PLAN", "add=" + startPlan.shouldAddMessage()
                            + ", refresh=" + startPlan.shouldRefreshExistingMessage()
                            + ", safeAnswerId=" + startPlan.activeAnswerId()
                            + ", currentChatId=" + (currentMessage != null ? currentMessage.getChatId() : -1)
                            + ", bodyLen=" + (startPlan.messageBody() == null ? 0 : startPlan.messageBody().length()));
                    pendingChunkRetryCount = startPlan.pendingChunkRetryCount();
                    lastHandledTypingIndex = startPlan.lastHandledTypingIndex();
                } else {
                    // Same answerId stream update: reuse current item and avoid duplicate bubble creation.
                    currentMessage.setMessageBody(updatedString);
                    refreshActiveAiMessage(updatedString, activeAiAnswerId);
                    appendAiTrace("UI_START_REUSE", "answerId=" + answerId
                            + ", activeAnswerId=" + activeAiAnswerId
                            + ", currentChatId=" + currentMessage.getChatId()
                            + ", bodyLen=" + (updatedString == null ? 0 : updatedString.length()));
                }
                pendingChunkRetryCount = 0;
                lastAiChunkUpdateTimeMs = System.currentTimeMillis();
                lastHandledTypingIndex = -1;
                isTypeWriterTyping =true;

            } else {
                lastAiChunkUpdateTimeMs = System.currentTimeMillis();
                AIChatStreamCoordinator.StreamUpdatePlan updatePlan = AIChatStreamCoordinator.planStreamUpdate(
                        updatedString,
                        answerId,
                        currentMessage != null,
                        activeAiAnswerId,
                        currentTypeWriter != null ? currentTypeWriterPosition : 0,
                        numRandom
                );
                if (updatePlan.shouldAddMessage()) {
                    currentMessage = addMessageToAdapter(updatePlan.messageBody(), CGlobalVariables.ASTROLOGER, updatePlan.messageChatId(), true, false);
                    activeAiAnswerId = updatePlan.activeAnswerId();
                    pendingChunkRetryCount = 0;
                }
                appendAiTrace("UI_STREAM_PLAN", "add=" + updatePlan.shouldAddMessage()
                        + ", answerId=" + answerId
                        + ", activeAnswerId=" + updatePlan.activeAnswerId()
                        + ", messageChatId=" + updatePlan.messageChatId()
                        + ", chunkCount=" + (updatePlan.messageChunks() == null ? 0 : updatePlan.messageChunks().size())
                        + ", animatedLen=" + (updatePlan.animatedText() == null ? 0 : updatePlan.animatedText().length())
                        + ", activeTypePos=" + currentTypeWriterPosition);
                if (currentTypeWriter != null) {
                    if (updatePlan.messageChunks() != null) {
                        messageChunks = updatePlan.messageChunks();
                        Log.d("AIChunkDebug", "Chunk update detected. chunks=" + messageChunks.size()
                                + ", len=" + updatedString.length());
                    }
                    currentTypeWriter.updateAnimatedText(updatePlan.animatedText());
                }
                if(currentMessage != null) {
                    currentMessage.setMessageBody(updatePlan.messageBody());
                }

            }
        });
    }

    /**
     * Ensures streamed AI updates always have a backing chat item, even when the first callback arrives
     * without the typewriter-start event. This prevents valid server responses from being dropped on UI.
     */
    private void ensureAiMessageInitializedForStream(String updatedString, long answerId) {
        if (TextUtils.isEmpty(updatedString)) {
            return;
        }
        AIChatStreamCoordinator.StreamUpdatePlan updatePlan = AIChatStreamCoordinator.planStreamUpdate(
                updatedString,
                answerId,
                currentMessage != null,
                activeAiAnswerId,
                0,
                numRandom
        );
        if (!updatePlan.shouldAddMessage()) {
            return;
        }
        currentMessage = addMessageToAdapter(updatePlan.messageBody(), CGlobalVariables.ASTROLOGER, updatePlan.messageChatId(), true, false);
        activeAiAnswerId = updatePlan.activeAnswerId();
        pendingChunkRetryCount = 0;
        appendAiTrace("UI_ENSURE_STREAM_MESSAGE", "answerId=" + answerId
                + ", activeAnswerId=" + activeAiAnswerId
                + ", messageChatId=" + updatePlan.messageChatId()
                + ", bodyLen=" + (updatePlan.messageBody() == null ? 0 : updatePlan.messageBody().length()));
    }

    @Override
    public void onFinishTyping(int index) {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                index,
                messageChunks,
                stopTypeWriter,
                currentMessage != null ? currentMessage.getMessageBody() : null,
                lastHandledTypingIndex,
                pendingChunkRetryCount,
                System.currentTimeMillis(),
                lastAiChunkUpdateTimeMs,
                llTypeWriterParent != null ? llTypeWriterParent.getChildCount() : 0
        );
        messageChunks = decision.messageChunks();
        pendingChunkRetryCount = decision.pendingChunkRetryCount();
        lastHandledTypingIndex = decision.lastHandledTypingIndex();
        appendAiTrace("UI_FINISH_DECISION", "index=" + index
                + ", action=" + decision.action()
                + ", nextPosition=" + decision.nextPosition()
                + ", retryCount=" + pendingChunkRetryCount
                + ", chunkCount=" + (messageChunks == null ? 0 : messageChunks.size())
                + ", childCount=" + (llTypeWriterParent == null ? 0 : llTypeWriterParent.getChildCount())
                + ", activeAnswerId=" + activeAiAnswerId);

        if (decision.action() == AIChatTypewriterFlow.Action.IGNORE_DUPLICATE) {
            Log.d("AIChunkDebug", "Duplicate onFinishTyping ignored. index=" + index + ", lastHandled=" + lastHandledTypingIndex);
            return;
        }
        if (decision.action() == AIChatTypewriterFlow.Action.WAIT_RETRY) {
            int nextPosition = index + 1;
            Log.d("AIChunkDebug", "Retry waiting for next chunk. nextPosition=" + nextPosition + ", retry=" + pendingChunkRetryCount);
            new Handler(Looper.getMainLooper()).postDelayed(() -> onFinishTyping(index), decision.retryDelayMs());
            return;
        }
        if (decision.action() == AIChatTypewriterFlow.Action.COMPLETE) {
            int nextPosition = index + 1;
            Log.d("AIChunkDebug", "Typing end. nextPosition=" + nextPosition + ", chunkCount=" + (messageChunks == null ? 0 : messageChunks.size()) + ", stop=" + stopTypeWriter);
            typeWriterSafetyToken++;
            persistCurrentAiMessageForAdapter();
            if (currentMessage != null) {
                currentMessage.setSeen(false);
                currentMessage.setDelayed(false);
                int finishedMessagePosition = messageAdapter.finishTyping(CGlobalVariables.ASTROLOGER, currentMessage.getChatId());
                if (finishedMessagePosition != -1) {
                    messageAdapter.refreshSingleItem(finishedMessagePosition);
                }
            }
            currentMessage = null;
            activeAiAnswerId = -1L;
            toggleSendStopButtonVisibility(true);
            return;
        }
        if (decision.action() == AIChatTypewriterFlow.Action.ALREADY_RENDERED) {
            Log.d("AIChunkDebug", "Chunk already added. nextPosition=" + decision.nextPosition() + ", childCount=" + (llTypeWriterParent == null ? 0 : llTypeWriterParent.getChildCount()));
            return;
        }

        Log.d("AIChunkDebug", "Starting next chunk. nextPosition=" + decision.nextPosition() + ", totalChunks=" + (messageChunks == null ? 0 : messageChunks.size()));
        TypeWriter instance = typeWriterInstance(decision.nextPosition());
        llTypeWriterParent.addView(instance);
        stopTypeWriter = false;
        currentTypeWriterPosition = decision.nextPosition();
        currentTypeWriter = instance;
        instance.animateText(decision.nextChunkText(), AIChatWindowActivity.this, this);
        scheduleTypewriterSafetyCompletion(instance, decision.nextPosition(), decision.nextChunkText());
        checkViewPosition(llChatButton);

    }

    private TypeWriter typeWriterInstance(int index) {
        TypeWriter newTypeWriter = new TypeWriter(this, index);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, toPixel(10), 0, 0);
        newTypeWriter.setLayoutParams(params);
        newTypeWriter.setPadding(toPixel(10), toPixel(4), toPixel(10), toPixel(4));
        newTypeWriter.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        newTypeWriter.setTextSize(18);
        newTypeWriter.setTextColor(ContextCompat.getColor(this, R.color.black));
        newTypeWriter.setBackgroundResource(R.drawable.bg_chat_white);

        return newTypeWriter;
    }

    private int toPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void updateMessageInItem(String updateSting) {
        if (currentMessage != null) {
            currentMessage.setMessageBody(updateSting);
            refreshActiveAiMessage(updateSting, currentMessage.getChatId());
            appendAiTrace("UI_UPDATE_ITEM", "currentChatId=" + currentMessage.getChatId()
                    + ", bodyLen=" + (updateSting == null ? 0 : updateSting.length())
                    + ", preview=" + AIChatTraceLogger.preview(updateSting));
            return;
        }
        messageAdapter.updateMessageInAdapter(updateSting);
    }

    private void actionOnKeyBoardVisibility() {
        RelativeLayout contentView = findViewById(R.id.containerLayout);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> {

                    Rect r = new Rect();
                    contentView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = contentView.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    int keypadHeight = screenHeight - r.bottom;

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        // keyboard is opened
                        if (!keypadOpenFlag) {
                            keypadOpenFlag = true;
                            userActionsEvents.append(CGlobalVariables.KEYPAD_OPEN_EVENT).append(",");
                            AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                        }
                        if(scrollWhileTypeWriter)
                            scrollMyListViewToBottom();
                    }

                });
    }

    private ArrayList<String> split(String text) {
        return AIChatMessageEngine.splitMessageChunks(text);
    }

    private void enableSendButton() {
        sendButton.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_orange));
        sendButton.setEnabled(true);
        ivStop.setVisibility(View.GONE);
        ivSend.setVisibility(View.VISIBLE);
    }

    private void disableSendButton() {
        sendButton.setEnabled(false);
        sendButton.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_gray_live));
        ivSend.setVisibility(View.GONE);
        ivStop.setVisibility(View.VISIBLE);
    }

    private void setDelay(Runnable runnable){
        new Handler(Looper.getMainLooper()).postDelayed(runnable,3000);
    }
    public void humanTypingToggle(boolean isTyping){
        onShowTypingIndicator(isTyping);
        if(!isTyping) {
            if (isTypingGreetingMessage) {
                isTypingGreetingMessage = false;
                if (!isDynamicGreeting) {
                    initNoUserResponseTimer();
                }
            }
        }
    }

    private String getILangCode() {
        if (Arrays.asList(validLanguages).contains(iLangCode)) {
            if (isRoman == 1) {
                return "hi-Latn";
            } else {
                return iLangCode;
            }
        } else {
            if (isRoman == 1) {
                return "hi-Latn";
            } else {
                return getAppLanguage();
            }
        }
    }

    /**
     * This method check the configuration from shared preference. If continue chat type is HUMAN and user offer is new-user(REDUCEDPRICE) then hit the api to
     * get random human astrologer details and display the continue dialog according to api data. If continue chat type is AI or EMPTY
     * then get the price of current astrologer according to user offer and display the dialog of same astrologer.
     *
     * @param astroid
     * @param currentOfferType
     */
    private void checkConfigrationForContinueChat(String astroid, String currentOfferType) {
        String typeContinueAIChat = CUtils.getStringData(this, CONTINUE_AI_CHAT_TYPE, "");
        Log.e("TestRandomAstro", "typeContinueAIChat=" + typeContinueAIChat);
        if (TextUtils.isEmpty(typeContinueAIChat) || typeContinueAIChat.equals(CGlobalVariables.CONTINUE_AI_CHAT_TYPE_AI)) {
            getAstrologerStatusPrice(astroid, currentOfferType);
        } else if (typeContinueAIChat.equals(CGlobalVariables.CONTINUE_AI_CHAT_TYPE_HUMAN) && currentOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
            getHumanRandomAstrologer(typeContinueAIChat);
        }
    }

    private void getHumanRandomAstrologer(String typeContinueAIChat) {
        if (!CUtils.isConnectedWithInternet(AIChatWindowActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), AIChatWindowActivity.this);
        } else {
            showProgressBar();

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi = apiList.getAIRandomAstrologer(getAIRandomAstroParams(typeContinueAIChat));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            parseRandomHumanAstroResponse(myResponse);
                        } catch (Exception e) {
                            String msg = getResources().getString(R.string.something_went_wrong) + " (" + e + ")";
                            CUtils.showSnackbar(findViewById(android.R.id.content), msg, AIChatWindowActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    String msg = getResources().getString(R.string.something_went_wrong) + " (" + t.getMessage() + ")";
                    CUtils.showSnackbar(findViewById(android.R.id.content), msg, AIChatWindowActivity.this);
                    Log.e("TestRandomAstro", "Throwable=" + t);
                }
            });
        }
    }

    public Map<String, String> getAIRandomAstroParams(String typeContinueAIChat) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(AIChatWindowActivity.this));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(AIChatWindowActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put("type", typeContinueAIChat);
        //Log.e("TestRandomAstro", "headers=" + headers);
        return CUtils.setRequiredParams(headers);
    }

    private void parseRandomHumanAstroResponse(String response) {
        Log.e("TestRandomAstro", "response=" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString(CGlobalVariables.STATUS);
            if (status.equals(CGlobalVariables.STATUS_SUCESS)) {
                AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
                astrologerDetailBean.setAstrologerId(jsonObject.optString("wi"));
                astrologerDetailBean.setUrlText(jsonObject.optString("urlt"));
                astrologerDetailBean.setName(jsonObject.optString("n"));
                astrologerDetailBean.setExpertise(jsonObject.optString("et"));
                astrologerDetailBean.setImageFileLarge(jsonObject.optString("ifl"));
                astrologerDetailBean.setImageFile(jsonObject.optString("if"));
                astrologerDetailBean.setDoubleRating(jsonObject.optString("dr"));
                astrologerDetailBean.setServicePrice(jsonObject.optString("sp"));
                astrologerDetailBean.setActualServicePriceInt(jsonObject.optString("asp"));
                astrologerDetailBean.setUseIntroOffer(true);
                //astrologerDetailBean.setFreeForChat(true);
                astrologerDetailBean.setCallSource(CGlobalVariables.HUMAN_CONTINUE_CHAT_DIALOG);
                openBottomSheetHumanChatContinueDialog(astrologerDetailBean);
            } else {
                CUtils.showSnackbar(findViewById(android.R.id.content), getResources().getString(R.string.something_went_wrong), AIChatWindowActivity.this);
            }
        } catch (Exception e) {

        }
    }


    private void openBottomSheetHumanChatContinueDialog(AstrologerDetailBean astrologerDetailBean) {
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HUMAN_CONTINUE_CHAT_DIALOG_OPEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            shareChat.setVisibility(View.GONE);
            //AstrologerDetailBean astrologerDetail_ = AstrosageKundliApplication.selectedAstrologerDetailBean;
            /*FrameLayout overLayBottomLayout = findViewById(R.id.overLayBottomLayout);
            overLayBottomLayout.setVisibility(View.VISIBLE);*/
            View includeLayoutBottomSheet = findViewById(R.id.includeLayoutBottomSheet);
            includeLayoutBottomSheet.setVisibility(View.VISIBLE);
            RoundImage imgViewAstrologer = includeLayoutBottomSheet.findViewById(R.id.imgViewAstrologer);
            String astrologerProfileUrl = "";
            String imgFileLarge = astrologerDetailBean.getImageFileLarge();
            if (!TextUtils.isEmpty(imgFileLarge)) {
                astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + imgFileLarge;
                Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewAstrologer);
            }
            TextView txtviewRating = includeLayoutBottomSheet.findViewById(R.id.txtviewRating);
            TextView txtViewAstrologerName = includeLayoutBottomSheet.findViewById(R.id.txtViewAstrologerName);
            TextView txtViewOffer = includeLayoutBottomSheet.findViewById(R.id.txtViewOffer);
            Button btnContinueChat = includeLayoutBottomSheet.findViewById(R.id.btnContinueChat);
            ImageView imgViewCancel = includeLayoutBottomSheet.findViewById(R.id.imgViewCancel);
            txtviewRating.setText(astrologerDetailBean.getDoubleRating());
            txtViewAstrologerName.setText(astrologerDetailBean.getName());
            int servicePrice_ = Integer.parseInt(astrologerDetailBean.getServicePrice());
            int actualPrice_ = Integer.parseInt(astrologerDetailBean.getActualServicePriceInt());

            if (actualPrice_ > servicePrice_) {
                setFormattedText(txtViewOffer, "₹" + getString(R.string.astrologer_rate, servicePrice_), "₹" + getString(R.string.astrologer_rate, actualPrice_));
            } else {
                txtViewOffer.setText(getString(R.string.continue_your_expert_consultation,  "₹" + getString(R.string.astrologer_rate, servicePrice_), ""));
            }


            imgViewCancel.setOnClickListener(v -> {
                //   bottomSheetDialog.dismiss();
            });
//            btnContinueChat.setOnClickListener(v -> {
//                //llStatusLayout.setVisibility(View.GONE);
//                messageAdapter.removeFollowMessage();
//                scrollMyListViewToBottom();
//                AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
//                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HUMAN_CONTINUE_CHAT_DIALOG_CONTINUE_BTN_CLICKED, AstrosageKundliApplication.currentEventType, "");
//
//                setMessageInputEnabled(true);
//                llConnectAgain.setVisibility(View.GONE);
//
//                String currentOfferType = CUtils.getCallChatOfferType(activity);
////                if (currentOfferType.equalsIgnoreCase(CGlobalVariables.INTRO_OFFER_TYPE_FREE) ||
////                        currentOfferType.equalsIgnoreCase(CGlobalVariables.REDUCED_PRICE_OFFER)){
//                if(astrologerDetailBean != null) {
//                    if (!TextUtils.isEmpty(currentOfferType)) {
//                        astrologerDetailBean.setUseIntroOffer(true);
//                        astrologerDetailBean.setFreeForChat(true);
//                    }
//                }
//
//                if (CUtils.isChatNotInitiated()) {
//                    AstrosageKundliApplication.lastChatAIAstrologerDetailBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
//                    ChatUtils.getInstance(AIChatWindowActivity.this).initChat(astrologerDetailBean);
//                } else {
//                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), context);
//                }
//
//                //overLayBottomLayout.setVisibility(View.GONE);
//                includeLayoutBottomSheet.setVisibility(View.VISIBLE);
//                shareChat.setVisibility(View.VISIBLE);
//
//            });
        } catch (Exception e) {
            //
        }
    }


    /**
     * This method calculate kundli data on background thread and call "OnSuccessKundliCalculationCallback.displayKundli()"
     * when kundli calculated successfully
     * @param beanHoroPersonalInfo
     */
   private void calculateKundli(BeanHoroPersonalInfo beanHoroPersonalInfo){
       ExecutorService executor = Executors.newSingleThreadExecutor();
       executor.execute(()-> {
           try {
               CGlobal.getCGlobalObject().setHoroPersonalInfoObject(beanHoroPersonalInfo);

               ControllerManager _controllerManager = new ControllerManager(/*OnSuccessKundliCalculationCallback*/AIChatWindowActivity.this, activity);
                //Kundli calculation stated
               _controllerManager.calculateKundliData(beanHoroPersonalInfo,
                       true, com.ojassoft.astrosage.utils.CUtils.isConnectedWithInternet(activity));
           } catch (UICOnlineChartOperationException | NoInternetException e) {
               //e.printStackTrace();
           }
       });
   }

    /**
     * When kundli is calculated, used to add kundli data to MessageAdapter
     */
    private void addKundliToAdapter() {
        try {
            ControllerManager objControllManager = new ControllerManager();
            int[] laganKundliPlanetRashi = objControllManager.getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
            double[] planetsDegreeData = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());

            if(planetsDegreeData != null && laganKundliPlanetRashi != null) {
                Message message = new Message();
                message.setAuthor(KUNDLI_ASTROLOGER);
                message.setDateCreated(getMessageDate());
                message.setMessageBody(getString(R.string.here_is_your_birth_chart));
                message.setSeen(false);
                message.setChatId(numRandom.nextInt(999) + 1);
                message.setPlanetsInRashiLagna(Arrays.copyOf(laganKundliPlanetRashi,laganKundliPlanetRashi.length));
                message.setPlanetDegreeArray(Arrays.copyOf(planetsDegreeData,planetsDegreeData.length));
                messageAdapter.addMessage(message);
                scrollMyListViewToBottom();
                saveMessageInList(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * OnSuccessKundliCalculationCallback implementation
     */
    @Override
    public void displayKundli() {
        isKundliCalculated = true;
    }

    public void speakOutMessage(String text,int speakingPosition){
        this.speakingPosition = speakingPosition;
        identifyLanguageWithStringInput(text,false);
        Log.e("speakOutMsg","Text  = " + text);
    }

    private void speakOutMessage(String text,String langCode){
        Log.e("speakOutMsg","Language Code = " + langCode);
        if(langCode.equalsIgnoreCase("hi:Latn")){
            tts.setLanguage(new Locale("hi"));
        }else{
            tts.setLanguage(new Locale(langCode));
        }
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utteranceId");
    }

    public void stopSpeaking(){
        tts.stop();
        messageAdapter.notifyItemChanged(speakingPosition);
        speakingPosition = -1;
    }

    private void setUtteranceProgressListener() {
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(() -> {
                    messageAdapter.notifyItemChanged(speakingPosition);
                    speakingPosition=-1;
                });
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    /**
     * Persists the latest AI response text into the adapter-backed message model before UI rebind/end-chat flows.
     * This avoids losing the currently typed response when RecyclerView items are refreshed.
     */
    private void persistCurrentAiMessageForAdapter() {
        try {
            if (currentMessage == null || messageAdapter == null || messageAdapter.getItemCount() == 0) {
                return;
            }
            String finalText = currentMessage.getMessageBody();
            if (messageChunks != null && !messageChunks.isEmpty()) {
                String merged = AIChatMessageEngine.mergeChunksForPersistence(messageChunks, isAiBehaveLikeHuman);
                if (!TextUtils.isEmpty(merged)) {
                    finalText = merged;
                }
            }
            if (!TextUtils.isEmpty(finalText)) {
                currentMessage.setMessageBody(finalText);
                refreshActiveAiMessage(finalText, currentMessage.getChatId());
                appendAiTrace("UI_PERSIST_FINAL", "chatId=" + currentMessage.getChatId()
                        + ", activeAnswerId=" + activeAiAnswerId
                        + ", finalLen=" + finalText.length()
                        + ", chunkCount=" + (messageChunks == null ? 0 : messageChunks.size())
                        + ", preview=" + AIChatTraceLogger.preview(finalText));
            }
        } catch (Exception e) {
            // ignore model persist failure to keep chat-end flow uninterrupted
            appendAiTrace("UI_PERSIST_ERROR", String.valueOf(e));
        }
    }

    @Override
    protected void onStop() {
        // Home/middle button flow reaches onStop (not onBackPressed): start background service so
        // ongoing-chat UX (ongoing notification + dashboard join stripe) stays consistent.
        startBackgroundChatServiceIfNeeded();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onStop();
    }

    private static String removeHtmlTags(String textMessage) {
        String text = textMessage.replace("<br><br>", "\n\n").replace("<br>", "\n\n")
                .replace("<strong>", "")
                .replace("<em>", "")
                .replace("</em>", "")
                .replace("</strong>", "")
                .replace("\\n", "\n");
        //.replace("\\n", "");
        if (text.contains("^^")) {
            int index = text.indexOf("^^");
            if (index == -1) {
                index = text.length();
            }
            text = text.substring(0, index - 1);
        }
        return text;
    }

    private void loadOldChats(String astrologerId){
        //Log.e("loadOldChats", "astrologer Id ==>> " + astrologerId);
        ArrayList<ChatMessage> oldMessages = ChatHistoryDAO.getInstance(this).getOldMessages(astrologerId,true,0,10);
        if(oldMessages != null && !oldMessages.isEmpty()){
            messageAdapter.appendMessageList(oldMessages);
            if(AstrosageKundliApplication.allChatMessagesHistory == null) {
                AstrosageKundliApplication.allChatMessagesHistory = new ArrayList<>();
            }

            //Log.e("ChatHistoryDAO", "loadOldChats = " + oldMessages);
            AstrosageKundliApplication.allChatMessagesHistory.addAll(0,oldMessages);
        }
        //Log.e("loadOldChats", "list ==>> " + oldMessages);
    }

    boolean loading = false;
    private void setOnScrollListener(){
        messagesListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("loadOldChats", "lastVisible item position ==>> " + mLayoutManager.findFirstVisibleItemPosition());

                int firstVisibleViewPostion = mLayoutManager.findFirstVisibleItemPosition();
                int chatCountInDb = ChatHistoryDAO.getInstance(context).getChatHistoryCount(astrologerId);
                if (dy < 0) {
                    if (!loading && !isTypeWriterTyping) {
                        if(chatCountInDb > messageAdapter.getSize() && firstVisibleViewPostion < 1){
                            loading = true;
                            loadMoreData();
                        }
                    }

                }


            }
        });
    }

    private void loadMoreData(){
        new Handler().postDelayed(()->{
            try {
                //Log.e("loadOldChats", "messageAdapter size = " + messageAdapter.getItemCount());
                //Log.e("loadOldChats", "DB Message size = " + ChatHistoryDAO.getInstance(this).getChatHistoryCount(astrologerId));
                ArrayList<ChatMessage> oldMessage = ChatHistoryDAO.getInstance(AIChatWindowActivity.this).getOldMessages(astrologerId,true, messageAdapter.getSize(), 10);                //Log.e("loadOldChats", "messageAdapter size = " + messageAdapter.getItemCount());

                if (oldMessage != null && !oldMessage.isEmpty()) {
                    //Log.e("ChatHistoryDAO", "oldMessages = " + oldMessage);
                    int LastViewedPosition = mLayoutManager.findFirstVisibleItemPosition();
                    messageAdapter.appendMessageList(oldMessage);
                   messagesListView.post(() -> messagesListView.scrollToPosition(oldMessage.size()));
                    if(AstrosageKundliApplication.allChatMessagesHistory != null){
                        AstrosageKundliApplication.allChatMessagesHistory.addAll(0,oldMessage);
                    }
                    //Log.e("loadOldChats", "oldMessage = " + oldMessage.size() );
                }
            }catch (Exception ignore){}

            loading = false;
        },500);
    }

    /**
     * This method is used to submit data on server while user cancel the recharge dialog after free chat complete
     */
    public void cancelRechargeAfterChat() {
        ChatUtils.getInstance(this).cancelRechargeAfterChat(channelIdForNps,"INSUFFICIENT_DIALOG_CANCEL_BTN");
    }
}
