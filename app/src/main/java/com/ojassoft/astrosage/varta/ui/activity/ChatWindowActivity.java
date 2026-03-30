package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity.BACK_FROM_PLAN_PURCHASE_AD_SCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CHAT_COMPLETED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DURING_CHAT_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_WINDOW_CHAT_EXTEND_RECHARGE_PARTNER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_PAYMENT_SUCCESS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_FAILED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE_FAILED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_SUCCESS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_SPEED;
import static com.ojassoft.astrosage.varta.utils.CUtils.backPressFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.chatWindowOpenType;
import static com.ojassoft.astrosage.varta.utils.CUtils.editTextTouchFlag;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;
import static com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents;
import static com.ojassoft.astrosage.varta.utils.CUtils.followAstrologerModelArrayList;
import static com.ojassoft.astrosage.varta.utils.CUtils.hideMyKeyboard;
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
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
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
import android.widget.FrameLayout;
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
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.varta.dao.ChatHistoryDAO;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteMinimizeDialog;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ExtendInfo;
import com.ojassoft.astrosage.varta.model.MessageBean;
import com.ojassoft.astrosage.varta.model.NextOfferBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.CallStatusCheckService;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.service.RunningChatService;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ContactNumberRestrictMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.CustomStatusMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.LeftStatusMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.LowBalanceMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.MessageAdapter;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.StatusMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.WelcomeStatusMessage;
import com.ojassoft.astrosage.varta.ui.fragments.RechargePopUpAfterFreeChat;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.RoundImage;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
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

public class ChatWindowActivity extends BaseActivity implements VolleyResponse, View.OnClickListener , GlobalRetrofitResponse {
    private static final String TAG = "ChatRepeat";
    private static final int CHAT_HISTORY_PAGE_SIZE = 10;
    private static final int CALL_AGAIN_METHOD = 45;
    CustomProgressDialog pd;
    private static final int END_CHAT_VALUE = 5;
    private boolean isConnectivityDisconnected;
    Toolbar toolbarChat;
    TextView typingTextview;
    ImageView ivbackChat, shareChat, copyChat;
    CircularNetworkImageView astrologerProfilePic;
    TextView titleTextChat, timerTextview;
    TextView endChatButton;
    Activity activity;
    public static String CHANNEL_ID = "", channelIdForNps = "";
    Context context;
    RequestQueue queue;
    RelativeLayout sendButton, containerLayout, buttonKundli;
    RecyclerView messagesListView;
    LinearLayoutManager mLayoutManager;
    //NestedScrollView recycleNestedScrollView;
    EditText messageTextEdit;
    TextView tvWaitMsg;
    public AppCompatButton btnChatAgain, btnCallAgain;
    ConstraintLayout llConnectAgain;
    public static boolean openKundli = false;
    private int countDots = 0;
    private final String[] dotsArray = {".  ", ".. ", "..."};
    private final int mInterval = 500; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private Runnable mStatusChecker;
    FeedbackDialog feedbackDialog;
    //RatingAndDakshinaDialog ratingAndDakshinaDialog;
    MessageAdapter messageAdapter;
    // Channel currentChannel;
    //Messages messagesObject;
    MessageBean messagesdata;
    LinearLayout relSendMessage;
    private Random numRandom;
    private String astrologerId, astrologerName, astrologerProfileUrl, userChatTime;
    boolean /*isFeedbackEnabled,*/ isChatAgainClicked;
    private DatabaseReference endChatRef, messageReadRef, messageTypingRef;
    private ChildEventListener childEventListener;
    private ValueEventListener childEventListenerTyping;
    private ValueEventListener valueEventListenerNetConnection;
    private ValueEventListener endChatValueEventListener;
    private Handler connectivityTimer;
    private Runnable connectivityRunnable;
    private Animation animShow, animHide;
    public boolean END_CHAT_DATA = false;
    public boolean IS_ASTROLOGER_DISCONNECT = false;
    public  String offerTypeDuringInitChat, channelIdDuringInitChat;
    private long longTotalVerificationTime = 60000;
    private CountDownTimer countDownTimer, endChatDisabledTimer;
    boolean isIsChatStartedFromAstrologer = false;
    boolean isFreeConsultationChat = false;
    private final long longOneSecond = 1000;
    private String chatJsonObject;
    int intentNotificationId;
    boolean onJoinChatClick;
    private CountDownTimer countDownTimerChatStatus;
    private TextView tvChatRechargeButton;
    private String minBalText="";

    private String chatDurarion;
    private String userNameTempStore = "";
    boolean isQuickRechargeClicked = false;
    private ConstraintLayout clExtendChatReminder;
    private TextView tvChatRechargeTitle,tvExtendChatMsg;
    private final int NEXT_OFFER_API_RESPONSE = 22;
    private final int EXTEND_RECHARGE_INFO_API_RESPONSE = 23;
    private final ExecutorService chatHistoryExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean hasMoreStoredChats = true;

    private long remainingChatTime;
    private static final int WALLET_RECHARGE_RESPONSE = 99;
    private final int FOLLOW_ASTROLOGER_REQ = 6;
    public static String followStatus = "0";

    public static boolean isChatCompleted = false;
    private final int ASTROLOGER_STATUS_API = 0;
    private String urlText = "";
    private ImageView imgViewDowmArrow;
    private boolean isFreeConsultation;
    private FreeMinuteDialog freeMinutedialog;
    private FreeMinuteMinimizeDialog freeMinuteMinimizeDialog;
    public boolean isAvailableForCall;
    ExtendInfo extendInfo;
    int extendableChatDuration;
    private TextView tvFreeChatBlinker;
    LinearLayout  btn_options_view;
    LinearLayout btnKundli,btnMatching,btnHoroscope,btnPanchang;
    private static final String EXTEND_REMINDER_VISIBLE_KEY_PREFIX = "extend_chat_reminder_visible_";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.d("testChatWindow","onCreate");
        //Log.e("SAN ", " onCreate() call ");
        activity = this;
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        CUtils.isAutoConsultationConnected = false;
        inti();
        //Log.d("TestChatIssue","onCreate getIntent() ==>"+getIntent());
        // Log.d("TestChatIssue","AstrosageKundliApplication.currentChatStatus ==>"+AstrosageKundliApplication.currentChatStatus);
        if (savedInstanceState == null) {
            Log.d("testChatWindow","savedInstanceState is null");
            if (getIntent() != null) {
                //Log.d("ChatWindowTest", "onCreate");
                handleIntent(getIntent(), "onCreate");
                if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
                    stopService(new Intent(activity, OnGoingChatService.class));
                }

            }
        } else {
            finish();
        }
        messageTextEdit.setText(CUtils.getChatDraftMessage(activity, astrologerId));
        //followStatus();
        getIsUserFollowingAstrologer();
        CUtils.updateChatBackgroundBasedOnTheme(this);
        actionOnKeyBoardVisibility();
       // getExtendRechargeInfo();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("testChatWindow","onNewIntent");
        //Log.e("SAN ", " onNewIntent() call ");
        if (intent != null) {
            boolean isRecharged = false;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
            }
            if (isRecharged) {
                try {
                    if(QuickRechargeBottomSheet.getInstance()!=null){
                        QuickRechargeBottomSheet.getInstance().dismiss();
                    }
                }catch (Exception e){
                }
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
                String razorpayid = bundle.getString(CGlobalVariables.RAZORPAY_ID);
                if (orderStatus.equals("0")) {
                    udatePaymentStatusOnserveronFailed(containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode);
                } else {
                    udatePaymentStatusOnserver(this,containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode,razorpayid);
                }
            } else if (intent.hasExtra(CGlobalVariables.KEY_AI_QUESTION)){
                String question = "", revertQCount = "", aiAstrologerId = "", title = "";
                boolean isAiAstrologerOnline = false;
                if (intent.hasExtra(CGlobalVariables.KEY_REVERT_QUESTION_COUNT)) {
                    revertQCount = intent.getStringExtra(CGlobalVariables.KEY_REVERT_QUESTION_COUNT);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ID)) {
                    aiAstrologerId = intent.getStringExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ID);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_QUESTION)) {
                    question = intent.getStringExtra(CGlobalVariables.KEY_AI_QUESTION);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE)) {
                    isAiAstrologerOnline = intent.getBooleanExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE, false);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_NOTIFICATION_TITLE)) {
                    title = intent.getStringExtra(CGlobalVariables.KEY_AI_NOTIFICATION_TITLE);
                }
                checkAIAstrologerInLocalList(true, question, revertQCount, aiAstrologerId, title, isAiAstrologerOnline);
            } else {
                //Log.d("ChatWindowTest", "onNewIntent");
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
        sendButton = (RelativeLayout) findViewById(R.id.buttonSend);
        buttonKundli = (RelativeLayout) findViewById(R.id.buttonKundli);
        messagesListView =  findViewById(R.id.listViewMessages);
        imgViewDowmArrow = (ImageView) findViewById(R.id.imgViewDowmArrow);
        //recycleNestedScrollView = findViewById(R.id.recycleNestedScrollView);

        messageTextEdit = (EditText) findViewById(R.id.editTextMessage);
        relSendMessage = (LinearLayout) findViewById(R.id.relSendMessage);
        btnChatAgain = (AppCompatButton) findViewById(R.id.btnChatAgain);
        btnCallAgain = findViewById(R.id.btnCallAgain);
        llConnectAgain = findViewById(R.id.llConnectAgain);
        llConnectAgain.setVisibility(View.GONE);
        tvWaitMsg = findViewById(R.id.tvWaitMsg);
        tvChatRechargeButton = findViewById(R.id.tvChatRechargeButton);
        clExtendChatReminder = findViewById(R.id.clExtendChatReminder);
        tvChatRechargeTitle = findViewById(R.id.tvChatRechargeTitle);
        tvExtendChatMsg = findViewById(R.id.tvExtendChatMsg);
        tvFreeChatBlinker = findViewById(R.id.tvFreeChatBlinker);

        FontUtils.changeFont(this,tvChatRechargeTitle,CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this,tvChatRechargeButton,CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        messageAdapter = new MessageAdapter(activity);
        //addHistory();
        mLayoutManager = new LinearLayoutManager(this);
        messagesListView.setLayoutManager(mLayoutManager);
        messagesListView.setNestedScrollingEnabled(false);
        messagesListView.setAdapter(messageAdapter);
        mHandler = new Handler();
        setUpListeners();
        //setMessageInputEnabled(false);
        numRandom = new Random();

        CGlobalVariables.CHAT_END_STATUS = "";

        ivbackChat.setOnClickListener(this);
        endChatButton.setOnClickListener(this);
        shareChat.setOnClickListener(this);
        copyChat.setOnClickListener(this);
        copyChat.setVisibility(View.GONE);
        tvChatRechargeButton.setOnClickListener(this);

        btnKundli.setOnClickListener(this);
        btnMatching.setOnClickListener(this);
        btnHoroscope.setOnClickListener(this);
        btnPanchang.setOnClickListener(this);
        initAnimation();
        // SAN add dummy data
        //addDummyData();

        imgViewDowmArrow.setOnClickListener(view1 -> {
            scrollMyListViewToBottom();
        });

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

    }
    private boolean isAtBottom() {
        int lastVisibleItemPos = mLayoutManager.findLastVisibleItemPosition();
        return messageAdapter.getItemCount()-1 <= lastVisibleItemPos;
    }
    public void scrollMyListViewToBottom() {
        try {
            if (messagesListView != null) {
                messagesListView.post(() -> {
                    final int y = messageAdapter.getItemCount()-1;
                    mLayoutManager.scrollToPositionWithOffset(y,Integer.MIN_VALUE);
                });
            }
        } catch (Exception e) {
            //
        }
    }

    public void updateCopyView(boolean stage) {
        if ( stage ) {
            copyChat.setVisibility(View.VISIBLE);
        } else {
            copyChat.setVisibility(View.GONE);
        }
    }

    public void addDummyData(){

            for ( int i = 0; i <= 10; i++ ) {
                int chatId = numRandom.nextInt(999) + 1;
                Message message = new Message();
                message.setDateCreated(System.currentTimeMillis() / 1000 + "");
                if (i%2==0){
                    message.setMessageBody( i + " Hi " );
                    message.setAuthor(CGlobalVariables.USER);
                } else {
                    message.setMessageBody( i + " Hello " );
                    message.setAuthor(CGlobalVariables.ASTROLOGER);
                }

                message.setSeen(false);
                message.setChatId(chatId);
                messageAdapter.addMessage(message);
            }

    }


    @Override
    protected void onResume() {
        super.onResume();
        chatWindowOpenType = "human";
    }

    private void handleIntent(Intent intent, String from) {
        AstrosageKundliApplication.isBackFromChat = false;
        Log.d("testChatWindow","handleIntent");
        if (intent != null) {
            boolean onNotificationClick = intent.getBooleanExtra("ongoing_notification", false);
            intent.putExtra("ongoing_notification", false);
            if (onNotificationClick && AstrosageKundliApplication.currentChatStatus.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STARTED)) {
                onNotificationClick = false;
            }
            if (!onNotificationClick) {
                onJoinChatClick = intent.getBooleanExtra("isFromJoinButton", false);
                CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
               // Log.d("TestCannelId","CHANNEL_ID"+CHANNEL_ID);
                chatJsonObject = intent.getStringExtra("connect_chat_bean");
                astrologerName = intent.getStringExtra("astrologer_name");
                astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
                astrologerId = intent.getStringExtra("astrologer_id");
                userChatTime = intent.getStringExtra("userChatTime");
                try {
                    if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        isAvailableForCall = Boolean.parseBoolean(AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall());
                    }
                } catch (Exception e){
                    //
                }
                if(AstrosageKundliApplication.selectedAstrologerDetailBean!=null &&
                        AstrosageKundliApplication.selectedAstrologerDetailBean.getCurrentFreeConsultation()!=null&&
                        AstrosageKundliApplication.selectedAstrologerDetailBean.getCurrentFreeConsultation().equals(CGlobalVariables.CURRENT_FREE_CONSULTATION_TYPE_USER_CHAT)){
                    isFreeConsultationChat = true;
                }

                String offerType = CUtils.getCallChatOfferType(activity);
                if(CUtils.isFreeChat && CUtils.isSecondFreeChat(activity) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    String directSecondFreeChat = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.DIRECT_SECOND_FREE_CHAT, "");
                    if (directSecondFreeChat.equals(ENABLED)) {
                        btn_options_view.setVisibility(View.VISIBLE);
                    }
                }
                //isFreeConsultationChat = true;
                intent.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, "");
                intent.putExtra("isFromJoinButton", false);
                intent.putExtra("connect_chat_bean", "");
                intent.putExtra("astrologer_name", "");
                intent.putExtra("astrologer_profile_url", "");
                intent.putExtra("astrologer_id", "");
                intent.putExtra("userChatTime", "");

                intentNotificationId = getIntent().getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
                channelIdForNps = CHANNEL_ID;
                queue = VolleySingleton.getInstance(ChatWindowActivity.this).getRequestQueue();
                if (onJoinChatClick) {
                    joinConnectedChat();
                } else {
                    CUtils.setEndChatButtonVisibilityTimer(context,CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER);
                    CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "ChatWindowActivity open from "+from);
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACCEPTED_FBD_KEY).setValue("true");
                    try {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initConnectChat();
                            }
                        }, 200);

                    }catch (Exception e){
                        initConnectChat();
                    }


                }

                titleTextChat.setText(astrologerName);
                if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
                    String astroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
                    Glide.with(getApplicationContext()).load(astroImage).circleCrop().into(astrologerProfilePic);
                }
                changeVisibilitySendMsgLayout(true);
                if (!from.equals("onNewIntent")) {
                    addHistory();
                    if (onJoinChatClick) {
                        sendWelcomeMsg();
                        sendContactShareWarningMsg();
                    }
                    if(!isChatAgainClicked && !onJoinChatClick)
                        loadOldChats(astrologerId);
                }

            }

        }
        /**
         *
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CUtils.checkServiceRunning(RunningChatService.class)) {
                    stopService(new Intent(activity, RunningChatService.class));
                }
                startRunningService();
            }
        }, 2000);

    }
    /*

     */
    private void initlizeChatStatusTimer() {
        if (!isFreeConsultationChat) {
            return;
        }
        if (countDownTimerChatStatus != null) {
            countDownTimerChatStatus.cancel();
            countDownTimerChatStatus = null;
        }
        countDownTimerChatStatus = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                try {
                    finishChat(CGlobalVariables.AUTO_ENDED);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CUtils.initiateRandomAiChat(activity,"AutoAIChatChatWindow30sec",com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                        }
                    },1000);
                } catch (Exception e) {
                    //
                }
            }
        }.start();
    }
    private void checkForAstrologerChatStatus(String from,String message) {
        if (!isIsChatStartedFromAstrologer && isFreeConsultationChat) {
            if(from.equals(CGlobalVariables.ASTROLOGER)){
                if(! message.contains(CGlobalVariables.HI_WHA_D_YO_WANT_TO_ASK)){
                    Log.d("Test30Sec","Called checkForAstrologerChatStatus countDownTimerChatStatus.cancel();");
                    isIsChatStartedFromAstrologer = true;
                    if (countDownTimerChatStatus != null) {
                        countDownTimerChatStatus.cancel();
                        countDownTimerChatStatus = null;
                    }
                }
            }
        }
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
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER);

            bundle.putString("userChatTime", changeToMinSec());
            // bundle.putSerializable("chatInitiateAstrologerDetailBean",AstrosageKundliApplication.chatInitiateAstrologerDetailBean);
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent);
            } else {
                activity.startService(serviceIntent);
            }
            AstrosageKundliApplication.isBackFromChat = true;
            // when user click on back button from chat window we have to request api with this below action
            ChatUtils.getInstance(this).cancelRechargeAfterChat(channelIdForNps,"CHAT_BACK_BTN");

            finish();
        } catch (Exception e) {
            //
        }
    }
    private void startRunningService() {
        try {
            Intent serviceIntent = new Intent(activity, RunningChatService.class);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHANNEL_ID);
            bundle.putString("connect_chat_bean", chatJsonObject);
            bundle.putString("astrologer_name", astrologerName);
            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
            bundle.putString("astrologer_id", astrologerId);
            bundle.putString(CGlobalVariables.CHATINITIATETYPE, CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER);
            bundle.putString("userChatTime", changeToMinSec());
            // bundle.putSerializable("chatInitiateAstrologerDetailBean",AstrosageKundliApplication.chatInitiateAstrologerDetailBean);
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

    private void initConnectChat() {
        try {
            CUtils.playChatConnectSound(activity, 2);
        } catch (Exception e) {
            //
        }
        channelIdDuringInitChat = CHANNEL_ID;
        offerTypeDuringInitChat = CUtils.getCallChatOfferType(activity);
        IS_ASTROLOGER_DISCONNECT = false;
        initEndChatListener(CHANNEL_ID);
        timerStart();
        AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
        try {
            stopService(new Intent(this, AstroAcceptRejectService.class));
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
//            sendWelcomeMsg();
//            sendFirstMsgToFirebase();
//            sendProfileMsg();
            //sendCustomPushNotification(astrologerName, getResources().getString(R.string.ongoing_call), "");
            registerReceiverBackgroundLogin();
            messageReadFromFirebase();
        }
        AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
        try {
            stopService(new Intent(this, AstroAcceptRejectService.class));
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(intentNotificationId);
        } catch (Exception e) {

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpListeners() {

        btnChatAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChatAgainClicked = true;
                AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("chat_btn_click_chat_window", AstrosageKundliApplication.currentEventType, "");
                messageAdapter.removeFlowAstrologer();
                setMessageInputEnabled(true);
                llConnectAgain.setVisibility(View.GONE);
                showHideCallAgainButton(false);
                if (CUtils.isChatNotInitiated()) {
//                    FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
//                    InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, "10", "100", "chat_global");
//                    dialog.show(ft, "Dialog");
//                    AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = AstrosageKundliApplication.chatAgainAstrologerDetailBean;
                    ChatUtils.getInstance(ChatWindowActivity.this).initChat(AstrosageKundliApplication.chatAgainAstrologerDetailBean);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), context);
                }
                if (AstrosageKundliApplication.ASTROLOGER_ID != null && !AstrosageKundliApplication.ASTROLOGER_ID.equals(astrologerId)) {
                    AstrosageKundliApplication.chatMessagesHistory = null;
                    AstrosageKundliApplication.statusMessageSetHistory = null;
                    AstrosageKundliApplication.isChatAgainButtonClick = false;
                }
                AstrosageKundliApplication.chatMessagesHistory = messageAdapter.getMessageList();
                AstrosageKundliApplication.statusMessageSetHistory = messageAdapter.getStatusList();
                AstrosageKundliApplication.ASTROLOGER_ID = astrologerId;
                AstrosageKundliApplication.isChatAgainButtonClick = true;
            }
        });

        btnCallAgain.setOnClickListener(view -> {
            try {
                if(AstrosageKundliApplication.chatAgainAstrologerDetailBean == null) return;
                llConnectAgain.setVisibility(View.GONE);
                AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("call_btn_click_chat_window", AstrosageKundliApplication.currentEventType, "");
                int isFreeForCall = AstrosageKundliApplication.chatAgainAstrologerDetailBean.isFreeForCall() ? 1 : 2;
                int isFreeForChat = AstrosageKundliApplication.chatAgainAstrologerDetailBean.isFreeForChat() ? 1 : 2;
                if (isFreeForCall == 1) { //true
                    isFreeConsultation = true;
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setFreeForCall(true);
                } else if (isFreeForChat == 2) { //false
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setFreeForCall(false);

                    isFreeConsultation = false;
                } else {
                    isFreeConsultation = AstrosageKundliApplication.chatAgainAstrologerDetailBean.getUseIntroOffer();
                    AstrosageKundliApplication.chatAgainAstrologerDetailBean.setFreeForCall(AstrosageKundliApplication.chatAgainAstrologerDetailBean.getUseIntroOffer());
                }

                boolean isAgoraCallEnabled = com.ojassoft.astrosage.utils.CUtils.getBooleanData(ChatWindowActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.ISAGORACALLENABLED, false);
                if (isAgoraCallEnabled) {
                    checkCallMedium();
                } else {
                    CUtils.openProfileOrKundliAct(this, urlText, "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                }
            } catch (Exception e){
                //
            }
        });

        buttonKundli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMyKeyboard(ChatWindowActivity.this);
                openProfileDialog();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideMyKeyboard(ChatWindowActivity.this);
                if (!sendButtonFlag) {
                    sendButtonFlag = true;
                    userActionsEvents.append(CGlobalVariables.SEND_BUTTON_EVENT).append(",");
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                }
                sendMessage();
            }
        });
        messageTextEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                messageTextEdit.post(() -> messageTextEdit.requestFocus());
            }
        });
        messageTextEdit.setOnTouchListener((view, motionEvent) -> {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                InputMethodManager manager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(messageTextEdit, 0);
                messageTextEdit.requestFocus();
                if (!editTextTouchFlag) {
                    editTextTouchFlag = true;
                    userActionsEvents.append(CGlobalVariables.EDITTEXT_TOUCH_EVENT).append(",");
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                }
            }
            return false;
        });
//        messageTextEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                return false;
//            }
//        });

        messageTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (!typingFlag) {
                    typingFlag = true;
                    userActionsEvents.append(CGlobalVariables.TYPING_EVENT).append(",");
                    AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
                }
                if (cs != null) {
                    if (cs.length() == 1) {
                        sendTypingIndicator(true);
                    } else if (cs.length() == 0) {
                        sendTypingIndicator(false);
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        mStatusChecker = () -> {
            try {
                tvWaitMsg.setText(dotsArray[countDots]);
            } finally {
                if (countDots == 2) {
                    countDots = 0;
                } else {
                    countDots++;
                }
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        };
        setOnScrollListener();

    }

    private void checkCallMedium() {
        //if (manager == null) return;
        //if (manager.getInstalledModules().contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MODULE_VARTA_LIVE)) {
        //init agora call
        if (CUtils.isConnectedMobile(context)) {
            if (CUtils.isNetworkSpeedSlow(context)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.NETWORK_CALL_CONNECT_REASON_SPEED_SLOW,
                        AstrosageKundliApplication.currentEventType, "");
                CUtils.openProfileOrKundliAct(this, urlText, "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
            } else {
                errorLogs = errorLogs + "profile dialog open 1\n";
               connectAICall();
            }
        } else {
            errorLogs = errorLogs + "profile dialog open 2\n";
            connectAICall();
        }

    }

    private void connectAICall(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            ChatUtils.getInstance(ChatWindowActivity.this).initVoiceCall(AstrosageKundliApplication.chatAgainAstrologerDetailBean);
        }else{
            CUtils.showPreMicPermissionDialog(this,()->{
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            });
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> { // The callback lambda
                if (isGranted) {
                    checkCallMedium();
                } else {
                    CUtils.showPermissionDeniedDialog(this);
                }
            });

    boolean firstMessage = true;
    private void sendMessage() {

        boolean isContainMobileNumber = false;

        String messageText = getTextInput();
        if (messageText.length() == 0) {
            return;
        }
        if(firstMessage) {
            CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "ChatWindowActivity sendMessage() messageText=" + messageText);
        }
        int chatId = numRandom.nextInt(999) + 1;
        if (messageText.matches(".*\\d{10}.*")) {
            Message message = new Message();
            message.setAuthor(CGlobalVariables.USER);
            message.setDateCreated(System.currentTimeMillis() / 1000 + "");
            message.setMessageBody(messageText + "  " + getString(R.string.no_entry));
            message.setSeen(false);
            message.setChatId(chatId);
            messageAdapter.addMessage(message);
            scrollMyListViewToBottom();
            sendContactShareWarningMsg();
            clearTextInput();
            return;
        }
        if ((messageText.matches(".*[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}.*"))) {
            Message message = new Message();
            message.setAuthor(CGlobalVariables.USER);
            message.setDateCreated(System.currentTimeMillis() / 1000 + "");
            message.setMessageBody(messageText + "  " + getString(R.string.no_entry));
            message.setSeen(false);
            message.setChatId(chatId);
            messageAdapter.addMessage(message);
            scrollMyListViewToBottom();
            sendContactShareWarningMsg();
            clearTextInput();
            return;
        }

        if(firstMessage) {
            firstMessage = false;
            CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "ChatWindowActivity sendMsgToFirebase() messageText=" + messageText);
        }

        sendMsgToFirebase(messageText, chatId);
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

    public void sendProfileMsg() {
        try {
            String messageText = sendProfileDetailMsg();

            if (messageText != null && messageText.length() > 0) {
                int chatId = numRandom.nextInt(999) + 1;
                sendMsgToFirebase(messageText, chatId);
                clearTextInput();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsgToFirebase(String message, int chatId) {
        String toastMsg = "";
        //String timeStamp = String.valueOf(System.currentTimeMillis());
        String channelId = CUtils.getSelectedChannelID(activity);

        Map chatMap = new HashMap();
        chatMap.put("From", CGlobalVariables.USER);
        chatMap.put("To", CGlobalVariables.ASTROLOGER);
        chatMap.put("Text", message);
        chatMap.put("MsgTime", ServerValue.TIMESTAMP);
        chatMap.put("isSeen", false);
        chatMap.put("chatId", chatId);

        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.MESSAGES_FBD_KEY).push().setValue(chatMap);
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.LAST_MSG_TIME_FBD_KEY).setValue(ServerValue.TIMESTAMP);
    }

    public void sendFirstMsgToFirebase() {
        int chatId = numRandom.nextInt(999) + 1;
        String textMsg="";
        if(CUtils.getLanguageKey(LANGUAGE_CODE).equals("en")){
             textMsg = CGlobalVariables.HI_WHA_D_YO_WANT_TO_ASK;
        }else {
            textMsg = CGlobalVariables.HI_WHA_D_YO_WANT_TO_ASK_NEW_LINE +getString(R.string.chat_first_message);
        }
        String channelId = CUtils.getSelectedChannelID(activity);
        Map chatMap = new HashMap();
        chatMap.put("From", CGlobalVariables.ASTROLOGER);
        chatMap.put("To", CGlobalVariables.USER);
        chatMap.put("Text", textMsg);
        chatMap.put("MsgTime", ServerValue.TIMESTAMP);
        chatMap.put("isSeen", false);
        chatMap.put("chatId", chatId);
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.MESSAGES_FBD_KEY).push().setValue(chatMap);
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.LAST_MSG_TIME_FBD_KEY).setValue(ServerValue.TIMESTAMP);

    }

    private void sendTypingIndicator(boolean isTyping) {
        try {
            String channelId = CUtils.getSelectedChannelID(activity);
            Map chatMap = new HashMap();
            chatMap.put("From", CGlobalVariables.USER);
            chatMap.put("To", CGlobalVariables.ASTROLOGER);
            chatMap.put("MsgTime", ServerValue.TIMESTAMP);
            chatMap.put("isTyping", isTyping);
            //chatDBRef.child("isAstrologerTyping").updateChildren(chatMap);
            // VartaUserApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.TYPING_FBD_KEY_USER).push().setValue(chatMap);
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.TYPING_FBD_KEY_USER).updateChildren(chatMap);
            // VartaUserApplication.getmFirebaseDatabase(channelId).child("isUserTyping").push().setValue(chatMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setMessageInputEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendButton.setEnabled(enabled);
                messageTextEdit.setEnabled(enabled);
            }
        });
    }

    private String getTextInput() {
        return messageTextEdit.getText().toString().trim();
    }

    private void clearTextInput() {
        messageTextEdit.setText("");
    }

    public void onMessageAdded(Message message) {
        if (openKundli) {
            tvWaitMsg.setVisibility(View.VISIBLE);
            mStatusChecker.run();
        } else {
            tvWaitMsg.setVisibility(View.GONE);
            mHandler.removeCallbacks(mStatusChecker);
            countDots = 0;
        }
        messageAdapter.addMessage(message);
        checkForAstrologerChatStatus(message.getAuthor(),message.getMessageBody());
        persistMessageAsync(new UserMessage(message));
        scrollMyListViewToBottom();
    }

    public void updateSeenMsg(Message message) {
        messageAdapter.updateSeenMsg(message);
        if(!message.getMessageBody().equals(sendProfileDetailMsg()))
            updateSeenMessageAsync(new UserMessage(message));
    }

    public void timerStart() {
        try {
            if (!userChatTime.trim().isEmpty()) {
                if (CUtils.isFreeChat) {
                    tvFreeChatBlinker.setVisibility(View.VISIBLE);
                    Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.blinking_animation);
                    tvFreeChatBlinker.startAnimation(startAnimation);
                }
                //Log.e("SAN ", " timerStart() WelcomeMessage chat connect  " );

                timeSetOnTimer(userChatTime);
                if (!isChatAgainClicked) {
                    sendWelcomeMsg();
                }
                sendContactShareWarningMsg();
                sendProfileMsg();
                sendFirstMsgToFirebase();
                //sendCustomPushNotification(astrologerName, getResources().getString(R.string.ongoing_call), "");
                registerReceiverBackgroundLogin();
                messageReadFromFirebase();
                isChatCompleted = false;
            }
        } catch (Exception e){
            //
        }
    }

    public void sendWelcomeMsg() {
        StatusMessage statusMessage = new WelcomeStatusMessage("");
        this.messageAdapter.addStatusMessage(statusMessage);
    }

    public void sendLowBalanceMsg() {
        StatusMessage statusMessage = new LowBalanceMessage("");
        this.messageAdapter.addStatusMessage(statusMessage);
    }

    public void sendContactShareWarningMsg() {
        ContactNumberRestrictMessage statusMessage = new ContactNumberRestrictMessage("");
        this.messageAdapter.addStatusMessage(statusMessage);
    }

    public void endChatData(String chatEndedBy) {
        try {
            StatusMessage statusMessage = null;
            CUtils.cancelNotification(activity);
            statusMessage = new LeftStatusMessage(chatEndedBy, activity);
            this.messageAdapter.addStatusMessage(statusMessage);

            String follow = context.getResources().getString(R.string.follow_astrologer) + " <b>" + astrologerName + "</b> " + context.getResources().getString(R.string.to_get_information);
            statusMessage = new LeftStatusMessage(follow, activity);
            this.messageAdapter.addStatusMessage(statusMessage);

            messageTextEdit.setText("");
            timerStop();
            changeVisibilitySendMsgLayout(false);
            isChatCompleted = true;
            CGlobalVariables.chatTimerTime = 0;
            CUtils.saveAstrologerIDAndChannelID(activity, "", "");
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_COMPLETED;
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (CUtils.checkServiceRunning(RunningChatService.class)) {
                stopService(new Intent(activity, RunningChatService.class));
            }
        } catch (Exception e) {

        }
    }

    public void dialogToShowOnEndChat() {
        AstrosageKundliApplication.isEndChatCompleted = true;
        //endChatButton.setVisibility(View.GONE);
        clExtendChatReminder.setVisibility(View.GONE);
        saveExtendChatReminderVisibility(false);
        if (isChatAgainClicked) {
            isChatAgainClicked = false;
            AstrosageKundliApplication.isChatAgainButtonClick = false;
        }
        /*if(!CUtils.isFreeChat){
            getAstrologerFeedbackStatus();
        }*/
        getAstrologerFeedbackStatus();
        if (tvFreeChatBlinker.getVisibility() == View.VISIBLE) tvFreeChatBlinker.setVisibility(View.GONE);
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
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        //Log.e("TestFreeChat", " AF headers="+headers);
        return headers;
    }
    public void changeVisibilitySendMsgLayout(boolean isVisibleLayout) {
        if (isVisibleLayout) {
            llConnectAgain.setVisibility(View.GONE);
            relSendMessage.setVisibility(View.VISIBLE);
        } else {
            setMessageInputEnabled(isVisibleLayout);
            relSendMessage.setVisibility(View.INVISIBLE);
            llConnectAgain.setVisibility(View.VISIBLE);
            showHideCallAgainButton(isAvailableForCall);
            //relSendMessage.setVisibility(View.INVISIBLE);
        }

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
//                Intent intent2 = new Intent(ChatWindowActivity.this, NumerologyCalculatorInputActivity.class);
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
                FontUtils.changeFont(ChatWindowActivity.this, end_chat_confirm_text, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

                TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
                TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
                final AlertDialog alert = builder.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

                end_chat_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                toOpenShareDialog( messageAdapter.getMessageList() );
                break;
            case R.id.iv_copy_chat:
                //Log.e("SAN ", " CMW copy chat "  );
                toOpenCopyDialog( messageAdapter.getMessageList(), messageAdapter.getCopyMessageList() );
                break;
            case R.id.tvChatRechargeButton:
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE, FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.utils.CUtils.createSession(this, CHAT_WINDOW_CHAT_EXTEND_RECHARGE_PARTNER_ID);
                bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT;
                    //openQuickRechargeSheet();
                bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT;
                if(extendInfo!=null){
                    if(extendableChatDuration > 0){
                        extendChatTime();
                    }else {
                        FragmentManager ft = getSupportFragmentManager();
                        openQuickRechargeSheet(this, astrologerName, extendInfo.getMinimumrecharge(), extendInfo.getRemainingwalbal(), ft);
                    } }else {
                    getExtendRechargeInfo();
                }
                break;
        }
    }


    public void initializingCountDownTimer(long totalVerificationTime) {
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
                String text = String.format(java.util.Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);

                timerTextview.setText(text);
                //tvTimer.setText(text);
                CGlobalVariables.chatTimerTime = millisUntilFinished;
                remainingChatTime = millisUntilFinished;
                if (longTotalVerificationTime > 180000) {
                    if (text.equalsIgnoreCase("00:01:00")) {
                        CUtils.fcmAnalyticsEvents("chat_show_low_balance_msg", AstrosageKundliApplication.currentEventType, "");
                        sendLowBalanceMsg();
                    }
                    if (millisUntilFinished <= 120000) { //less than 2 minutes
                        if (clExtendChatReminder.getVisibility() == View.GONE) {
                            getExtendRechargeInfo();
                        }
                    }
                    if (clExtendChatReminder.getVisibility() == View.VISIBLE && extendableChatDuration > 0) {
                        String remaining;
                        if (minutes >= 1) {
                            remaining = String.format(java.util.Locale.US, "%02dm %02ds", minutes, seconds);
                        } else {
                            remaining = String.format(java.util.Locale.US, "%02ds", seconds);
                        }
                        tvChatRechargeTitle.setText(getResources().getString(R.string.chat_ends_in, remaining));
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

                        StatusMessage customStatusMessage = new CustomStatusMessage("", getString(R.string.chat_ended_due_to_low_balance));
                        messageAdapter.addStatusMessage(customStatusMessage);

                        CUtils.fcmAnalyticsEvents("status_time_over_chat_completed", AstrosageKundliApplication.currentEventType, "");

                        CUtils.changeFirebaseKeyStatus(CHANNEL_ID, "NA", true, CGlobalVariables.TIME_OVER);
                        finishChat(CGlobalVariables.TIME_OVER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        initEndChatDisableTimer();
    }

    public void initEndChatDisableTimer() {
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer = null;
            endChatButton.setVisibility(View.INVISIBLE);
            endChatButton.setEnabled(false);
        }
//        if (AstrosageKundliApplication.endChatTimeShowMilliSeconds > 0) {
//            endChatTimeShowMilliSeconds = AstrosageKundliApplication.endChatTimeShowMilliSeconds;
//        }

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
            chatDurarion = timeChangeInSecond(timerTextview.getText().toString());
            if (countDownTimer != null) {
                timeChangeInSecond(timerTextview.getText().toString());
                countDownTimer.cancel();
                changeVisibilitySendMsgLayout(false);
                isChatCompleted = true;
                //if (connectChatBean != null) {
                CUtils.changeFirebaseKeyStatus(CHANNEL_ID, "NA", true, CGlobalVariables.USER_ENDED);
                // chatCompleted(connectChatBean.getCallsid(), CGlobalVariables.USER_ENDED);
                chatCompleted(CHANNEL_ID, chatEndedBy);
                CUtils.fcmAnalyticsEvents("status_user_end_chat_completed", AstrosageKundliApplication.currentEventType, "");

                CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
                resetTimerView();
            }
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
    }

    public String sendProfileDetailMsg() {

        String userProfileMsg = "";
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
        boolean isProfileSendToAstrologer = userProfileData.isProfileSendToAstrologer();

        userNameTempStore = userProfileData.getName();
        if ( TextUtils.isEmpty(userNameTempStore) ) {
            userNameTempStore = "User";
        }

        if (isProfileSendToAstrologer) {
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

        }
        return userProfileMsg;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CUtils.updateChatBackgroundBasedOnTheme(this); // Call the method when theme changes
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
            /*if (ratingAndDakshinaDialog != null && ratingAndDakshinaDialog.isVisible()) {
                return;
            }*/
            feedbackDialog = new FeedbackDialog(CGlobalVariables.CON_TYPE_CHAT, channelIdForNps, getSupportFragmentManager(), AstrosageKundliApplication.selectedAstrologerDetailBean);
            feedbackDialog.show(getSupportFragmentManager(), "FeedbackDialog");
            //ratingAndDakshinaDialog = new RatingAndDakshinaDialog(context, activity, phoneNo1, astrologerId, null);
            //ratingAndDakshinaDialog.show(getSupportFragmentManager(), "FeedbackDialog");
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN, CGlobalVariables.SHOW_DIALOG_EVENT, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, int method) {
        if(method == METHOD_RECHARGE){
            handleWalletRechargeResponse(response);
        }
    }


    private void handleConnectCallResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("msg");
            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                final String callsId = jsonObject.getString("callsid");
                String talkTime = jsonObject.getString("talktime");
                final String exophoneNo = jsonObject.getString("exophone");
                String internationalCharges = jsonObject.getString("callcharge");

                if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                    CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog", AstrosageKundliApplication.currentEventType, "");
                    openFreeMinDialogBox();
                }
                String astroName = "", profileUrl = "";
                if (AstrosageKundliApplication.chatAgainAstrologerDetailBean != null) {
                    profileUrl = AstrosageKundliApplication.chatAgainAstrologerDetailBean.getImageFile();
                    astroName = AstrosageKundliApplication.chatAgainAstrologerDetailBean.getName();
                }
                CUtils.fcmAnalyticsEvents("show_call_initiate_dialog", AstrosageKundliApplication.currentEventType, "");

                CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CALL_CLICK, internationalCharges, astroName, profileUrl);
                dialog.show(getSupportFragmentManager(), "Dialog");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CUtils.fcmAnalyticsEvents("start_call_status_service", AstrosageKundliApplication.currentEventType, "");
                            Intent intent = new Intent(ChatWindowActivity.this, CallStatusCheckService.class);
                            intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                            context.startService(intent);
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
                String astrologerName = jsonObject.getString("astrologername");
                String userbalance = jsonObject.getString("userbalance");
                String minBalance = jsonObject.getString("minbalance");
                CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog", AstrosageKundliApplication.currentEventType, "");
                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CALL_CLICK);
                dialog.show(getSupportFragmentManager(), "Dialog");

            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                String fcm_label = "call_astrologer_" + message;

                CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");

                String dialogMsg = "";
                if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                    dialogMsg = getResources().getString(R.string.user_blocked_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                    dialogMsg = getResources().getString(R.string.astrologer_busy_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                    String stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                    dialogMsg = stringMsg;
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                    dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                    dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                    dialogMsg = getResources().getString(R.string.existing_call_msg);
                } else {
                    dialogMsg = getResources().getString(R.string.something_wrong_error);
                }
                callMsgDialogData(dialogMsg, "", true, CALL_CLICK);
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), context);
            }
        } catch (Exception e) {
            //
        }
    }

    private void openQuickRechargeSheet(){
        try {
            QuickRechargeBottomSheet quickRechargeBottomSheet = QuickRechargeBottomSheet.getInstance();
            Bundle bundle = new Bundle();
            bundle.putString("astrologerUrlText", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.CHATWINDOWACTIVITY);
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR,bottomServiceListUsedFor);
            bundle.putString("minBalanceNeededText",minBalText);
            quickRechargeBottomSheet.setArguments(bundle);
            quickRechargeBottomSheet.show(getSupportFragmentManager(), QuickRechargeBottomSheet.TITLE);
        } catch (Exception e){
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
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.CHATWINDOWACTIVITY);
            rechargeSuggestionBottomSheet.setArguments(bundle);
            CUtils.checkCachedData(activity,minBalanceNeededText,userbalance);
            rechargeSuggestionBottomSheet.show(fragmentManager, RechargeSuggestionBottomSheet.TITLE);
        } catch (Exception e) {

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
                    //Log.d("extendChatTime", "url : "+response.raw().request().url());
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
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    hideProgressBar();
                }
            });
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
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Log.d("TestlinkIssue","onDestroy Called");
        try {
            CUtils.saveChatDraftMessage(activity, astrologerId,getTextInput());
            if (queue != null) {
                queue.cancelAll("chatRequestQueue");
            }
            CUtils.cancelNotification(ChatWindowActivity.this);
            CUtils.cancelChatNotification(ChatWindowActivity.this);
        } catch (Exception e) {
            //
        }
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }
        if (endChatRef != null && endChatValueEventListener != null) {
            endChatRef.removeEventListener(endChatValueEventListener);
        }
        removeMessageListner();
        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
        }
        chatHistoryExecutor.shutdownNow();
        super.onDestroy();
    }

    private void parseAstrologerStatus(String response) {
        //Log.e("TestFreeChat", " AF response="+response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            boolean isFeedbackEnabled = jsonObject.optBoolean("enablefeedbacks");

            /*if (!CUtils.isFreeChat){
                getAstrologerStatusPrice(astrologerId,"");
            }*/
          //  openBottomsheetChatcontineDialog();
            if (isFeedbackEnabled) {
                //Log.e("TestFreeChat", " showRatingDialogToUser");
                showRatingDialogToUser();
            }

        } catch (Exception e) {
            // Log.e("ASTROLOGER_DE", "exp2=" + e);
        }
    }

    int astrologerMsgCount = 0;
    public void messageReadFromFirebase() {

        removeMessageListner();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    ////Log.e("DATABASE_DATA CHILD ", "" + dataSnapshot);
                    if(astrologerMsgCount <5) {
                        astrologerMsgCount++;
                        CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "ChatWindowActivity messageReadFromFirebase() onChildAdded()="+dataSnapshot);
                    }

                    try {
                        if (!dataSnapshot.getKey().equalsIgnoreCase("isSeen") && dataSnapshot.getValue() != null) {
                            onShowTypingIndicator(false);
                            String From = dataSnapshot.child("From").getValue(String.class);
                            String Text = dataSnapshot.child("Text").getValue(String.class);

                            Message message = new Message();
                            message.setAuthor(From);
                            message.setMessageBody(Text);
                            if (dataSnapshot.child("MsgTime").exists()) {
                                long MsgTime = dataSnapshot.child("MsgTime").getValue(Long.class);
                                message.setDateCreated("" + MsgTime);
                                message.setTimeStamp(MsgTime);
                            } else {
                                message.setDateCreated("0L");
                                message.setTimeStamp(System.currentTimeMillis());
                            }
                            if (dataSnapshot.child("chatId").exists()) {
                                int chatId = dataSnapshot.child("chatId").getValue(Integer.class);
                                message.setChatId(chatId);
                            }
                            if (dataSnapshot.child("isSeen").exists()) {
                                boolean isSeen = dataSnapshot.child("isSeen").getValue(Boolean.class);
                                message.setSeen(isSeen);
                            } else {
                                message.setSeen(false);
                            }

                            if (dataSnapshot.child("openKundli").exists()) {
                                openKundli = dataSnapshot.child("openKundli").getValue(Boolean.class);
                            }

                            if (!AstrosageKundliApplication.wasInBackground) {
                                CGlobalVariables.CHAT_NOTIFICATION_QUEUE = CGlobalVariables.CHAT_NOTIFICATION_QUEUE + Text + "\n";
                                displayChatNotification(astrologerName, CGlobalVariables.CHAT_NOTIFICATION_QUEUE);
                            }

                            onMessageAdded(message);

                            if (message.getAuthor().equalsIgnoreCase("Astrologer") && !message.isSeen()) {
                                HashMap<String, Object> chatMap = new HashMap<>();
                                chatMap.put("isSeen", true);
                                dataSnapshot.getRef().updateChildren(chatMap);
                            }

                        }
                    } catch (Exception e) {
                        Log.e("error received2", e.toString());
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    if (dataSnapshot.child("isSeen").exists()) {
                        boolean isSeen = dataSnapshot.child("isSeen").getValue(Boolean.class);
                        if (isSeen) {
                            String From = dataSnapshot.child("From").getValue(String.class);
                            if (From != null && From.equals("User")) {
                                String Text = dataSnapshot.child("Text").getValue(String.class);
                                Message message = new Message();
                                message.setSeen(isSeen);
                                message.setAuthor(From);
                                message.setMessageBody(Text);
                                if (dataSnapshot.child("MsgTime").exists()) {
                                    long MsgTime = dataSnapshot.child("MsgTime").getValue(Long.class);
                                    message.setDateCreated("" + MsgTime);
                                    message.setTimeStamp(MsgTime);
                                } else {
                                    message.setDateCreated("0L");
                                    message.setTimeStamp(System.currentTimeMillis());
                                }
                                if (dataSnapshot.child("chatId").exists()) {
                                    int chatId = dataSnapshot.child("chatId").getValue(Integer.class);
                                    message.setChatId(chatId);
                                }
                                updateSeenMsg(message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        };


        childEventListenerTyping = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Log.e("messages", snapshot.toString());
                try {
                    if (snapshot.getKey().equalsIgnoreCase(CGlobalVariables.TYPING_FBD_KEY_ASTROLOGER) && snapshot.getValue() != null) {
                        boolean isTyping = snapshot.child("isTyping").getValue(Boolean.class);
                        String From = snapshot.child("From").getValue(String.class);
                        if (From != null && From.equals("Astrologer")) {
                            onShowTypingIndicator(isTyping);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        messageReadRef = AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.MESSAGES_FBD_KEY);
        messageReadRef.addChildEventListener(childEventListener);
        messageTypingRef = AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.TYPING_FBD_KEY_ASTROLOGER);
        messageTypingRef.addValueEventListener(childEventListenerTyping);
        registerConnectivityStatusReceiver();
        setOnDisconnectListner();
    }

    private void removeMessageListner() {
        try {
            //String channelId = CUtils.getSelectedChannelID(this);
            if (messageReadRef != null && childEventListener != null) {
                messageReadRef.removeEventListener(childEventListener);
            }
            if (messageTypingRef != null && childEventListenerTyping != null) {
                messageTypingRef.removeEventListener(childEventListenerTyping);
            }

        } catch (Exception e) {
            //
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
            String channelId = CUtils.getSelectedChannelID(this);
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().setValue(ServerValue.TIMESTAMP);
        } catch (Exception e) {
            //
        }
    }

    private void cancelOnDisconnectListner() {
        try {
            String channelId = CUtils.getSelectedChannelID(this);
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().cancel();
        } catch (Exception e) {
            //
        }
    }
    private void displayChatNotification(String title, String msg) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);
        int notificationId = LibCUtils.getRandomNumber();
        // pending intent is redirection using the deep-link
        //If url contains Play store then use Action view else open the App
        Intent resultIntent;

        resultIntent = new Intent(this, ChatWindowActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("ongoing_notification", true);
        resultIntent.setAction(Intent.ACTION_VIEW);
        PendingIntent pending;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setContentIntent(pending).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "chatnotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // using the same tag and Id causes the new notification to replace an existing one
        mNotificationManager.notify(CGlobalVariables.CHAT_NOTIFICATION, CGlobalVariables.CHAT_NOTIFICATION_ID, notificationBuilder.build());
    }

    private void initEndChatListener(String channelIDD) {
        endChatValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    END_CHAT_DATA = (boolean) dataSnapshot.getValue();
                    if (END_CHAT_DATA && CGlobalVariables.CHAT_END_STATUS.equals(CGlobalVariables.CHAT_INTERNET_DISCONNECTION)) {
                        try {
                            proceedEndChat(channelIDD, this, CGlobalVariables.CHAT_INTERNET_DISCONNECTION);
                            endChatData(CGlobalVariables.CHAT_INTERNET_DISCONNECTION);
                            CUtils.fcmAnalyticsEvents("firebase_success_end_chat_due_to_internet", AstrosageKundliApplication.currentEventType, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }  else if (END_CHAT_DATA && CGlobalVariables.CHAT_END_STATUS.equals(CGlobalVariables.USER_BACKGROUND)) {
                        try {
                            proceedEndChat(channelIDD, this, CGlobalVariables.ASTROLOGER);
                            endChatData(CGlobalVariables.ASTROLOGER);
                            CUtils.fcmAnalyticsEvents("firebase_success_end_chat_user_background", AstrosageKundliApplication.currentEventType, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (END_CHAT_DATA && !IS_ASTROLOGER_DISCONNECT && !CGlobalVariables.CHAT_END_STATUS.equals(CGlobalVariables.COMPLETED)) {
                        try {
                            proceedEndChat(channelIDD, this, CGlobalVariables.ASTROLOGER);
                            endChatData(CGlobalVariables.ASTROLOGER);
                            CUtils.fcmAnalyticsEvents("firebase_sucess_end_chat_by_astrologer", AstrosageKundliApplication.currentEventType, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (END_CHAT_DATA && CGlobalVariables.CHAT_END_STATUS.equals(CGlobalVariables.COMPLETED)) {
                        try {
                            proceedEndChat(channelIDD, this, CGlobalVariables.USER);
                            endChatData(CGlobalVariables.USER);
                            CUtils.fcmAnalyticsEvents("firebase_success_end_chat_by_user", AstrosageKundliApplication.currentEventType, "");
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

    private void proceedEndChat(String channelIDD, ValueEventListener valueEventListener, String chatEndedBy) {
        try {
            isChatCompleted = true;
            CHANNEL_ID = "";
            CUtils.cancelOnDisconnentEvent(channelIDD);
            removeMessageListner();
            unRegisterConnectivityStatusReceiver();
            cancelOnDisconnectListner();
            AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY).removeEventListener(valueEventListener);
            endChatButton.setVisibility(View.GONE);
            if (chatEndedBy.equalsIgnoreCase(CGlobalVariables.USER)) {
                CUtils.updateChatCallOfferType(ChatWindowActivity.this, true, CHAT_CLICK);
                dialogToShowOnEndChat();
            } else {
                chatDurarion = timeChangeInSecond(timerTextview.getText().toString());
            }
        } catch (Exception e) {
            //
        }
    }

//    private void sendCustomPushNotification(String title, String msg, String link) {
//        CUtils.saveStringData(this, CGlobalVariables.TITLE_ONGOING_CHAT, title);
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);
//
//        int notificationId = LibCUtils.getRandomNumber();
//        // pending intent is redirection using the deep-link
//        //If url contains Play store then use Action view else open the App
//        Intent resultIntent = new Intent(this, ChatWindowActivity.class);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        resultIntent.putExtra("ongoing_notification", true);
//        resultIntent.setAction(Intent.ACTION_VIEW);
//
//
//        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setOngoing(true).setContentTitle(title).setContentText(msg).setSmallIcon(R.drawable.chat_logo).setColor(getResources().getColor(R.color.Orangecolor)).setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND).setContentIntent(pending).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID).setAutoCancel(false).setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
//
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            mNotificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        // using the same tag and Id causes the new notification to replace an existing one
//        mNotificationManager.notify(CGlobalVariables.ONGOING_NOTIFICATION, 1001, notificationBuilder.build());
//        try {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    CUtils.cancelNotification(activity, AstrosageKundliApplication.currentDisplayedNotificationTag, AstrosageKundliApplication.currentDisplayedNotificationId);
//                }
//            }, 5000);
//        } catch (Exception e) {
//            //
//        }
//
//    }

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
        //check astrologer message
       initlizeChatStatusTimer();
    }



    long endChatResponseTime = 0;
    public void chatCompleted(String channelID, String remarks) {
        unRegisterConnectivityStatusReceiver();
        cancelOnDisconnectListner();
        CUtils.cancelNotification(ChatWindowActivity.this);
        CGlobalVariables.chatTimerTime = 0;
        CUtils.saveAstrologerIDAndChannelID(ChatWindowActivity.this, "", "");
        if (!CUtils.isConnectedWithInternet(ChatWindowActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), ChatWindowActivity.this);
        } else {
            showProgressBar();
            endChatResponseTime = Calendar.getInstance().getTimeInMillis();
            AstrosageKundliApplication.isEndChatReqOnGoing = true;
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks), channelID, getClass().getSimpleName());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    AstrosageKundliApplication.isEndChatReqOnGoing = false;
                    hideProgressBar();
                    endChatResponseTime = Calendar.getInstance().getTimeInMillis() - endChatResponseTime;
                    String status = "";
                    try{
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        status = jsonObject.getString("status");
                    } catch (Exception e){
                        status = "";
                    }

                    if (status.equals("1")) {
                        CUtils.startUserAiChatCategoryDataService(activity, offerTypeDuringInitChat, channelIdDuringInitChat,TYPE_HUMAN_CHAT);
                    } else { // end-chat-api fail
                        setEndChatOverValue(channelIdDuringInitChat);
                    }

                    CHANNEL_ID = "";
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_COMPLETED;
                    CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                    CGlobalVariables.chatTimerTime = 0;
                    CUtils.saveAstrologerIDAndChannelID(ChatWindowActivity.this, "", "");
                    CUtils.updateChatCallOfferType(ChatWindowActivity.this, true, CHAT_CLICK);
                    if (CUtils.checkServiceRunning(RunningChatService.class)) {
                        stopService(new Intent(activity, RunningChatService.class));
                    }
                    dialogToShowOnEndChat();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        hideProgressBar();
                        AstrosageKundliApplication.isEndChatReqOnGoing = false;
                        String msg = getResources().getString(R.string.something_went_wrong) + " (" + t.getMessage() + ")";
                        CUtils.showSnackbar(findViewById(android.R.id.content), msg, ChatWindowActivity.this);
                        endChatButton.setVisibility(View.GONE);
                        clExtendChatReminder.setVisibility(View.GONE);
                        saveExtendChatReminderVisibility(false);
                        CUtils.updateChatCallOfferType(ChatWindowActivity.this, true, CHAT_CLICK);
                        setEndChatOverValue(CHANNEL_ID);
                        CHANNEL_ID = "";
                    } catch (Exception e){
                        //
                    }
                }
            });
        }
    }

    public Map<String, String> getChatCompleteParams(String channelID, String remarks) {

        HashMap<String, String> headers = new HashMap<String, String>();
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ChatWindowActivity.this));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/timeChangeInSecond(timerTextview.getText().toString()));
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("LoadMore params ", headers.toString());
        return CUtils.setRequiredParams(headers);
    }

    private void openBottomsheetChatcontineDialog(String servicePrice,String actualServicePriceInt,String doubleRating, String currentOfferType,String minbalrequired){
        shareChat.setVisibility(View.GONE);
        //  Log.d("openDailog","open");
        // Create the BottomSheetDialog with a custom theme
        //  bottomSheetDialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme);
        AstrologerDetailBean astrologerDetail_ = AstrosageKundliApplication.selectedAstrologerDetailBean;
        // Inflate the layout for the Bottom Sheet
        //  View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_continue_chat, null);
        // Get and manipulate views inside the bottom sheet
        FrameLayout overLayBottomLayout = findViewById(R.id.overLayBottomLayout);
        overLayBottomLayout.setVisibility(View.VISIBLE);
        View includeLayoutBottomSheet = findViewById(R.id.includeLayoutBottomSheet);
        RoundImage imgViewAstrologer = includeLayoutBottomSheet.findViewById(R.id.imgViewAstrologer);

        if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
            String astroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
            Glide.with(getApplicationContext()).load(astroImage).circleCrop().into(astrologerProfilePic);
            Glide.with(context.getApplicationContext()).load(astroImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgViewAstrologer);

        }
        TextView txtviewRating = includeLayoutBottomSheet.findViewById(R.id.txtviewRating);
        TextView txtViewAstrologerName = includeLayoutBottomSheet.findViewById(R.id.txtViewAstrologerName);
        TextView txtViewOffer = includeLayoutBottomSheet.findViewById(R.id.txtViewOffer);
        Button btnContinueChat = includeLayoutBottomSheet.findViewById(R.id.btnContinueChat);
        ImageView imgViewCancel = includeLayoutBottomSheet.findViewById(R.id.imgViewCancel);
        txtviewRating.setText(doubleRating);
        txtViewAstrologerName.setText(astrologerDetail_.getName());
        int servicePrice_ = Integer.parseInt(servicePrice);
        int actualPrice_ = Integer.parseInt(actualServicePriceInt);
        minBalText  = getResources().getString(R.string.minBalNeeded).replace("####",minbalrequired);
        if (!TextUtils.isEmpty(currentOfferType) && currentOfferType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE) && CUtils.isFreeChat ) {
            //txtViewOffer.setText(getString(R.string.continue_this_chat_with_expert_insights_free, userNameTempStore));
            String freeText= getString(R.string.title_free).trim();
            setFormattedFreeText(txtViewOffer,getString(R.string.continue_this_chat_with_expert_insights_free, userNameTempStore),freeText);
        } else if(actualPrice_ > servicePrice_){
            setFormattedText(txtViewOffer,"₹"+getString(R.string.astrologer_rate,servicePrice_),"₹"+getString(R.string.astrologer_rate,actualPrice_));
        }else{
            txtViewOffer.setText(getString(R.string.continue_this_chat_with_expert_insights_at_just_min, userNameTempStore,"₹"+getString(R.string.astrologer_rate,servicePrice_),""));
            //setFormattedText(txtViewOffer,"","₹"+getString(R.string.astrologer_rate,servicePrice_));
        }
        imgViewCancel.setOnClickListener(v -> {
            //   bottomSheetDialog.dismiss();
        });
        btnContinueChat.setOnClickListener(v ->{
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_CONTINUE_CHAT_HUMAN_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            isChatAgainClicked = true;
            AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
            CUtils.fcmAnalyticsEvents("chat_btn_click_chat_window", AstrosageKundliApplication.currentEventType, "");

            setMessageInputEnabled(true);
            llConnectAgain.setVisibility(View.GONE);
            showHideCallAgainButton(false);
            if (CUtils.isChatNotInitiated()) {
                ChatUtils.getInstance(ChatWindowActivity.this).initChat(AstrosageKundliApplication.chatAgainAstrologerDetailBean);
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), context);
            }
            if (AstrosageKundliApplication.ASTROLOGER_ID != null && !AstrosageKundliApplication.ASTROLOGER_ID.equals(astrologerId)) {
                AstrosageKundliApplication.chatMessagesHistory = null;
                AstrosageKundliApplication.statusMessageSetHistory = null;
                AstrosageKundliApplication.isChatAgainButtonClick = false;
            }
            AstrosageKundliApplication.chatMessagesHistory = messageAdapter.getMessageList();
            AstrosageKundliApplication.statusMessageSetHistory = messageAdapter.getStatusList();
            AstrosageKundliApplication.ASTROLOGER_ID = astrologerId;
            AstrosageKundliApplication.isChatAgainButtonClick = true;
            // bottomSheetDialog.dismiss();
            overLayBottomLayout.setVisibility(View.GONE);
            shareChat.setVisibility(View.VISIBLE);
        });
        // bottomSheetDialog.setCancelable(true);
        //  bottomSheetDialog.setCanceledOnTouchOutside(false);
        // Set the view to the dialog
        //  bottomSheetDialog.setContentView(view);

        // Show the dialog
        // bottomSheetDialog.show();

       /* // Add a dismiss listener to finish the Activity when the dialog is dismissed
        bottomSheetDialog.setOnDismissListener(dialog -> {

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
        });*/

    }

    public void setFormattedFreeText(TextView txtView,String text1, String freeText){
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


    }
    public void setFormattedText(TextView textView, String offerPrice, String actualPrice) {
        // Create the dynamic text
        // String text = "Hi "+userNameTempStore + " continue this chat with expert insights at just " + offerPrice + " " + actualPrice + ".";
        String text1 = getString(R.string.continue_this_chat_with_expert_insights_at_just_min, userNameTempStore,offerPrice,actualPrice);

        // Create a SpannableString from the text
        SpannableString spannableString = new SpannableString(text1);

        // Apply orange color to the offer price
        // int orangeColor = Color.parseColor("#FFA500"); // Orange color code
        int startIndexOffer = text1.indexOf(offerPrice);
        spannableString.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorPrimary_day_night)),
                startIndexOffer,
                startIndexOffer + offerPrice.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Apply gray color and strikethrough to the discount price
        // int grayColor = Color.GRAY;
        int startIndexDiscount = text1.indexOf(actualPrice);
        spannableString.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this,R.color.grey_light)),
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

        // Set the SpannableString to the TextView
        textView.setText(spannableString);
    }
    private void getAstrologerStatusPrice(String astroid, String currentOfferType) {


        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerStatusPrice(getAstroStatusParams(astroid));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String myResponse = response.body().string();
                    // Log.e("TestNewUser", myResponse);
                    JSONObject jsonObject = new JSONObject(myResponse);
                    String servicePrice = jsonObject.getString("servicePrice");
                    String actualServicePriceInt = jsonObject.getString("actualServicePriceInt");
                    String doubleRating = jsonObject.getString("doubleRating");
                    String minbalrequired = jsonObject.getString("minbalrequired");
                    openBottomsheetChatcontineDialog(servicePrice,actualServicePriceInt,doubleRating,currentOfferType,minbalrequired);
                    //parseAstrologerStatus(myResponse);

                } catch (Exception e) {
                       CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), ChatWindowActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                 CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), ChatWindowActivity.this);
            }
        });
    }

    public Map<String, String> getAstroStatusParams(String astroId) {

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
        }
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));

        if (AstrosageKundliApplication.selectedAstrologerDetailBean.getIntroOfferType() == null){
            headers.put(CGlobalVariables.OFFER_TYPE, "");
        }else {
            headers.put(CGlobalVariables.OFFER_TYPE, AstrosageKundliApplication.selectedAstrologerDetailBean.getIntroOfferType());

        }
        if (AstrosageKundliApplication.selectedAstrologerDetailBean.getUseIntroOffer()){
            headers.put(CGlobalVariables.USE_INTRO_OFFER, "1");
        }else {
            headers.put(CGlobalVariables.USE_INTRO_OFFER, "2");
        }
       /* String offerType = CUtils.getCallChatOfferType(this);
        if (offerType == null) offerType = "";
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.USE_INTRO_OFFER, "2");
        headers.put(CGlobalVariables.OFFER_FROM_NOTIFICATION,"0");*/
        // Log.e("TestNewUser", "headers= " + headers);
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
            userChatDuration = "" + actualSeconds;
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
                CUtils.sendLogDataRequest(astrologerId, CHANNEL_ID, "ChatWindowActivity connectivity status ="+connected);
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
            String channelId = CUtils.getSelectedChannelID(this);
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child("UserConnRegainTime").setValue(ServerValue.TIMESTAMP);
            CUtils.sendLogDataRequest(astrologerId, channelId, "DashboardActivity setConnectivityRegainInFirebase() UserConnRegainTime="+System.currentTimeMillis());
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
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
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
                    seconds = "" + totalSec;
                }
            }
        }
        return seconds;
    }

    public void showProgressBar(){
        try {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e){

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
       // Log.d("quickrecharge result","requestCode="+requestCode);
       // Log.d("quickrecharge result","data="+data);
        if (requestCode == DashBoardActivity.BACK_FROM_PROFILECHATDIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                if (isProceed) {
                    String phoneNo = data.getStringExtra("phoneNo");
                    String urlText = data.getStringExtra("urlText");
                    callToAstrologer(phoneNo, urlText, isFreeConsultation);
                } else {
                    showHideCallAgainButton(isAvailableForCall);
                }

                if (data.getExtras().containsKey("openKundliList")) {
                    CUtils.openSavedKundliList(this, urlText, "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                } else if (data.getExtras().containsKey("openProfileForChat")) {
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")) {
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChat(this, urlText, "call", bundle, prefillData, DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                }
            }
        } else if (requestCode == BACK_FROM_PROFILE_CHAT_DIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                UserProfileData userProfileDataBean = (UserProfileData) data.getExtras().get("USER_DETAIL");
                String fromWhere = data.getStringExtra("fromWhere");
                if(fromWhere == null) {
                    fromWhere = "";
                }
                if (isProceed) {
                    if (fromWhere.equalsIgnoreCase("profile_send")) {
                        boolean isSkip = data.getExtras().getBoolean("IS_SKIP");
                        //ChatUtils.getInstance(ChatWindowActivity.this).startChat(userProfileDataBean);
                        if(!isSkip) {
                            sendProfileMsg();
                        }
                    } else if (fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_CHAT)) {
                        ChatUtils.getInstance(ChatWindowActivity.this).startChat(userProfileDataBean);
                    } else if (fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_VOICE_CALL)) {
                        errorLogs = errorLogs + "goto api hit\n";
                        ChatUtils.getInstance(ChatWindowActivity.this).startAudioCall(userProfileDataBean);
                    }
                } else {
                    if (!fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_CHAT)) { // in case of call
                        showHideCallAgainButton(isAvailableForCall);
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
                } else {
                    if (!fromWhere.equalsIgnoreCase("profile_send")) { // in case of call
                        llConnectAgain.setVisibility(View.VISIBLE);
                        AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
                    }
                }
            }

        } else if(requestCode == CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE){
            isQuickRechargeClicked = false;
            if (data != null) {
                Bundle bundle = data.getExtras();
                boolean isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
                if (isRecharged) {
                    String orderID = bundle.getString(CGlobalVariables.ORDER_ID);
                    String orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS);
                    String rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT);
                    String paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE);
                    String razorpayid = bundle.getString(CGlobalVariables.RAZORPAY_ID);
                    String phonepeid = bundle.getString(CGlobalVariables.PHONEPE_ID);
                    String phonepeOrderId = bundle.getString(CGlobalVariables.PHONEPE_ORDER_ID);
                    String od = bundle.getString(CGlobalVariables.ORDER_ID_PHONEPE);
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

    public void callToAstrologer(String phoneNo, String urlText, boolean isFreeConsultation) {
        showFreeOneMinDialog();
        callSelectedAstrologer(phoneNo, urlText, isFreeConsultation);
    }

    private void showFreeOneMinDialog() {
        try {
            String offerType = CUtils.getCallChatOfferType(this);
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE) && CUtils.getCountryCode(ChatWindowActivity.this).equals("91")) {
                freeMinutedialog = new FreeMinuteDialog(CGlobalVariables.INTRO_OFFER_TYPE_FREE);
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
                CUtils.fcmAnalyticsEvents("first_min_free_dialog_free_call", AstrosageKundliApplication.currentEventType, "");

            }
            String wallet = CUtils.getWalletRs(this);
            //Log.e("LoadMore wallet ", wallet);
            if ((wallet != null) && !wallet.equals("0.0")) {
                CUtils.fcmAnalyticsEvents("first_min_free_dialog", AstrosageKundliApplication.currentEventType, "");
                freeMinutedialog = new FreeMinuteDialog("");
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
            } else {

            }
        } catch (Exception e) {
            //
        }

    }

    private void openFreeMinDialogBox() {
        try {
            //Log.e("SAN ADA", " openFreeMinDialogBox call");
            freeMinutedialog.dismiss();
            if (freeMinuteMinimizeDialog == null) {
                freeMinuteMinimizeDialog = new FreeMinuteMinimizeDialog();
            }
            freeMinuteMinimizeDialog.show(getSupportFragmentManager(), "MiniDialog");

        } catch (Exception e) {
            //Log.e("SAN ADA", " openFreeMinDialogBox exp " + e.toString());
        }
    }

    private void callSelectedAstrologer(String mobileNo, String urlTextt, boolean isFreeConsultation) {

        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), context);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(context);
            pd.show();
            pd.setCancelable(false);
            //Log.e("SAN FEEDBACK SIZE ", "" + urlTextt + " " + mobileNo + " url " + CGlobalVariables.CALL_ASTROLOGER_URL );
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CALL_ASTROLOGER_URL,
//                    ChatWindowActivity.this, false, getCallParams(mobileNo, urlTextt, isFreeConsultation), CALL_AGAIN_METHOD).getMyStringRequest();
//            queue.add(stringRequest);

            CUtils.fcmAnalyticsEvents("connect_call_api_called", AstrosageKundliApplication.currentEventType, "");

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.connectCall(getCallParams(mobileNo, urlTextt, isFreeConsultation));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String myRespose = response.body().string();
                        handleConnectCallResponse(myRespose);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(containerLayout,getResources().getString(R.string.something_wrong_error),activity);
                }
            });
        }
    }

    public Map<String, String> getCallParams(String phoneNo, String urlText, boolean isFreeConsultation) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
                headers.put(KEY_PASSWORD, CUtils.getUserLoginPassword(context));
            } else {
                headers.put(CGlobalVariables.USER_PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.URL_TEXT, urlText);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.CALL_SOURCE, TAG);
        headers.put(DEVICE_ID, CUtils.getMyAndroidId(context));
        String currentOfferType = CUtils.getUserIntroOfferType(this);
        headers.put(CGlobalVariables.ISFREE_CONSULTATION, String.valueOf(isFreeConsultation));
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(context)));
        return headers;
    }

    public void showChatGainButton(){
        llConnectAgain.setVisibility(View.VISIBLE);
        isChatCompleted = true;
    }
    public void addHistory() {
        try {
            if (AstrosageKundliApplication.ASTROLOGER_ID.equals(astrologerId) && AstrosageKundliApplication.chatMessagesHistory != null && AstrosageKundliApplication.chatMessagesHistory.size() > 0) {
                if ( AstrosageKundliApplication.isChatAgainButtonClick || onJoinChatClick ){
                    AstrosageKundliApplication.isChatAgainButtonClick = false;
                    //messageAdapter.setStatusMessage(AstrosageKundliApplication.statusMessageSetHistory);
                    messageAdapter.setMessages(AstrosageKundliApplication.chatMessagesHistory);
                }

            }

        } catch (Exception e) {
            //
        }
    }
    private  String bottomServiceListUsedFor;
    public void openWalletScreen(String openFrom) {
        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_chat_window_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "CWREC");
        }else {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_chat_window_low_balance_subscribe_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "CSREC");

        }
        com.ojassoft.astrosage.utils.CUtils.createSession(this, CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID);
        openWalletScreen();
//        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
//            if (CUtils.getCountryCode(ChatWindowActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                //intent = new Intent(AIChatWindowActivity.this, MiniPaymentInformationActivity.class);
//                bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT;
//                    openQuickRechargeSheet();
//            }else {
//                openWalletScreen();
//            }
//        }else {
//            openWalletScreen();
//        }



    }
    private void openWalletScreen() {
        Intent intent = new Intent(ChatWindowActivity.this, WalletActivity.class);
        intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.CHATWINDOWACTIVITY);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        saveExtendChatReminderVisibility(clExtendChatReminder.getVisibility() == View.VISIBLE);
        super.onBackPressed();
        closeActivityForAINotification();
        String analyticsLabel = "";
        if(isChatCompleted){
            analyticsLabel = com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_HUMAN_CHAT_BACK_PRESS+"_"+CHAT_COMPLETED_KEY;
          }else{
            analyticsLabel = com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_HUMAN_CHAT_BACK_PRESS+"_"+DURING_CHAT_KEY;
             }
        //Log.e(TAG, "onBackPressed: "+analyticsLabel );
        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(analyticsLabel, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        if (isActivityRunning(ActAppModule.class)
                || isActivityRunning(DashBoardActivity.class)) {
            // Do Noting
        } else {
            Intent intent = new Intent(ChatWindowActivity.this, DashBoardActivity.class);
            startActivity(intent);
        }
        if (CUtils.checkServiceRunning(RunningChatService.class)) {
            stopService(new Intent(activity, RunningChatService.class));
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
        }catch (Exception e){
            //
        }
        return false;
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_share, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_share) {
            Log.e("SAN ", " CMW action share "  );
            toOpenShareDialog( messageAdapter.getMessageList() );
            return true;
        } else if (item.getItemId() == R.id.action_copy) {
            Log.e("SAN ", " CMW action copy() "  );
            toCopyData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public String convertDateTime(String dateInMilliseconds) {

        String dateFormat = "dd MMM yyyy hh:mm a";
        String dateTime = "";
        try {
            if(dateInMilliseconds != null && dateInMilliseconds.length()>0){
                dateTime = DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
            }
        } catch (Exception e) {}

        return dateTime;
    }

    public void toOpenShareDialog(List<ChatMessage> messageList ) {
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
                        textToShare = textToShare + "[" + convertDateTime(cmi.getDateCreated()) + "] " + author + ": " + cmi.getMessageBody(activity) + "\n";

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
        }catch (Exception e){
            //Log.e("SAN ", " CMW toOpenShareDialog() Exp2=" + e);
        }
    }

    public void toOpenCopyDialog(List<ChatMessage> messageList, List<Integer> messageCopyList ) {

        String textToShare = "";
        String author = "";

        if ( messageCopyList.size() > 0 ) {

            Collections.sort(messageCopyList);

            for ( int i = 0;  i < messageCopyList.size(); i++ ) {
                    int position = messageCopyList.get(i);
                if ( messageList.get(position) instanceof LeftStatusMessage
                        || messageList.get(position) instanceof WelcomeStatusMessage
                        || messageList.get(i) instanceof ContactNumberRestrictMessage) {
                    continue;
                } else {

                    ChatMessage cmi = messageList.get(position);

                    if ( cmi.getAuthor().equalsIgnoreCase(CGlobalVariables.USER) )
                        author = userNameTempStore;
                    else
                        author = astrologerName;

                    textToShare = textToShare + "[" +  convertDateTime( cmi.getDateCreated() ) + "] " + author +": " + cmi.getMessageBody(activity) + "\n" ;

                    //Log.e("SAN ", " CMW loop textToShare => " + textToShare );

                }

            }

            //Log.e("SAN ", " CMW toOpenShareDialog() " + textToShare );

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, textToShare);
            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //AnalyticsUtils.setAnalytics(context, "Chat Share Clicked");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.text_share_it)));
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(activity, "Please select chat message", Toast.LENGTH_LONG).show();
        }


    }

    public void toCopyData(){
        //Log.e("SAN ", " CMW toCopyData() " );
    }

    private long timeTakenForRecharge = 0;

    public void gotoPaymentInfoActivity(int mSelectedPosition, WalletAmountBean walletAmountBean){
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
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "QuickRechargeBottomSheet");
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        bundle.putString("screen_open_from", "QuickRechargeBottomSheet");

        bundle.putString(CGlobalVariables.CHANNEL_ID, CUtils.getSelectedChannelID(activity));
        Intent intent;
        // condition for open MiniPayment info or Payment info dont remove
//        if (CUtils.getCountryCode(ChatWindowActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//            intent = new Intent(context, MiniPaymentInformationActivity.class);
//        }else {
//            intent = new Intent(context, PaymentInformationActivity.class);
//        }
        intent = new Intent(context, PaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE);
    }
    public void gotoPaymentInfoActivityVaribaleInfo(int mSelectedPosition, WalletAmountBean walletAmountBean,String recharge_amount){
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
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "QuickRechargeBottomSheet");
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        bundle.putString("screen_open_from", "QuickRechargeBottomSheet");

        bundle.putString(CGlobalVariables.CHANNEL_ID, CUtils.getSelectedChannelID(activity));
        Intent intent;
        // condition for open MiniPayment info or Payment info dont remove
//        if (CUtils.getCountryCode(ChatWindowActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//            intent = new Intent(context, MiniPaymentInformationActivity.class);
//        }else {
//            intent = new Intent(context, PaymentInformationActivity.class);
//        }
        intent = new Intent(context, PaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE);
    }
    public void gotoMiniPaymentInfoActivity(int mSelectedPosition, WalletAmountBean walletAmountBean){
        Bundle bundle = new Bundle();
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, CGlobalVariables.CHATWINDOWACTIVITY);
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        bundle.putString(CGlobalVariables.CHANNEL_ID, CUtils.getSelectedChannelID(activity));
        Intent intent = new Intent(context, MiniPaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    public void updatePaymentStatusOnServer(View containerLayout, String orderStatus, String orderID,
                                           String amount, RequestQueue queue, String paymentMode,
                                           String razorpayid, int extendedWaitTime,String phonepeid,String phonepeOrderId,String od) {
        this.amount = amount;
        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), activity);
        } else {
            showProgressBar();

            /*mainQueue = queue;
            String url = CGlobalVariables.WALLETRECHARGE;
            //Log.d("quickrecharge updateurl", url);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    this, false, getParamsForWalletRecharge(orderStatus, orderID,razorpayid, extendedWaitTime), WALLET_RECHARGE_RESPONSE).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.walletRecharge(getParamsForWalletRecharge(orderStatus, orderID,razorpayid, extendedWaitTime,phonepeid,phonepeOrderId,od));
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
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("msg");
                        if (status.equals("1")){
                            //Log.d("quickrecharge response", "1");
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_SUCCESS, FIREBASE_EVENT_PAYMENT_SUCCESS, "");
                            clExtendChatReminder.setVisibility(View.GONE);
                            saveExtendChatReminderVisibility(false);
                            long newChatDurationMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(jsonObject.getString("chatduration")));
                            //Log.d("quickrecharge response2","CGlobalVariables.chatTimerTime="+CGlobalVariables.chatTimerTime);
                            if (llConnectAgain.getVisibility() != View.VISIBLE) {
                                initializingCountDownTimer(CGlobalVariables.chatTimerTime + newChatDurationMillis);
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_FAILED, FIREBASE_EVENT_PAYMENT_FAILED, "");
                            initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                            CUtils.showSnackbar(containerLayout,msg,activity);
                        }
                    } catch (Exception e){
                        if (llConnectAgain.getVisibility() != View.VISIBLE) {
                            initializingCountDownTimer(CGlobalVariables.chatTimerTime);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(containerLayout,getResources().getString(R.string.something_wrong_error),activity);
                }
            });
        }
    }

    private Map<String, String> getParamsForWalletRecharge(String orderStatus, String orderId,
                                                               String razorpayid, int extendedWaitTime,String phonepeid,String phonepeOrderId,String od) {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(activity));
            headers.put("od", orderId);
            headers.put("isSucess", orderStatus);
            headers.put("paycurr", "INR");
            headers.put("razorpayid", razorpayid);
            headers.put("rechargefromchat", "1");
            headers.put("urltext", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            headers.put("channelid", CUtils.getSelectedChannelID(activity));
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

    public void getNextRechargeFromApi() {
        if (CUtils.isConnectedWithInternet(ChatWindowActivity.this) &&
                CUtils.getUserLoginStatus(ChatWindowActivity.this)) {
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.nextRecharge(getNextRechargeParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String myResposeNew = response.body().string();
                        if(TextUtils.isEmpty(myResposeNew)){
                            myResposeNew = response.body().string();
                        }
                        JSONObject jsonObject = new JSONObject(myResposeNew);
                        if (jsonObject.has("status")) {
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                Gson gson = new Gson();
                                NextOfferBean nextOfferBean = gson.fromJson(jsonObject.toString(), NextOfferBean.class);
                                //showRechargeAfterFreeChat(nextOfferBean);
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

    public Map<String, String> getNextRechargeParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ChatWindowActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(ChatWindowActivity.this)); //"8860085780"
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(ChatWindowActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(ChatWindowActivity.this));
        headers.put(CGlobalVariables.DEVICE_ID,CUtils.getMyAndroidId(ChatWindowActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return headers;
    }

    RechargePopUpAfterFreeChat rechargePopUpAfterFreeChat;
    public void showRechargeAfterFreeChat() {
        try {
            if(rechargePopUpAfterFreeChat != null && rechargePopUpAfterFreeChat.isVisible()){
                return;
            }
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_NEXT_OFFER_RECHARGE_DIALOG,
                    CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

            rechargePopUpAfterFreeChat = new RechargePopUpAfterFreeChat(CGlobalVariables.CHATWINDOWACTIVITY);
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

    private void getExtendRechargeInfo(){
        /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, EXTEND_RECHARGE_INFO,
                ChatWindowActivity.this, false, getExtendRechargeInfoParams(), EXTEND_RECHARGE_INFO_API_RESPONSE).getMyStringRequest();
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);*/


        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.extendRechargeInfo(getExtendRechargeInfoParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               // Log.d("getExtendRechargeInfo",call.request().url().toString());
                try {
                    String myRespose = response.body().string();
                    extendInfo = new ExtendInfo();
                    JSONObject jsonObject = new JSONObject(myRespose);
                    Log.d("getExtendRechargeInfo","response : " +myRespose);
                    extendInfo.setServiceprice( jsonObject.getString("serviceprice"));
                    extendInfo.setMinimumrecharge( jsonObject.getString("minimumrecharge"));

                    extendInfo.setCurrentserviceprice(jsonObject.getString("currentserviceprice"));
                    extendInfo.setRemainingwalbal( jsonObject.getString("remainingwalbal"));
                    extendableChatDuration = jsonObject.optInt("chatduration",0);
                    extendInfo.setChatduration(String.valueOf(extendableChatDuration));

                    String serviceprice = jsonObject.getString("serviceprice");
                    //minimumrecharge = jsonObject.getString("minimumrecharge");
                    clExtendChatReminder.setVisibility(View.VISIBLE);
                    saveExtendChatReminderVisibility(true);
                    if(extendableChatDuration > 0){
                        tvExtendChatMsg.setVisibility(View.VISIBLE);
                        tvChatRechargeButton.setText(getResources().getString(R.string.extend_now));
                    }else {
                        String lowBalance = getString(R.string.low_balance);
                        if (getString(R.string.low_balance).contains("**")) {
                            lowBalance = lowBalance.replace("**", serviceprice);
                            tvChatRechargeTitle.setText(lowBalance);
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

    public Map<String, String> getExtendRechargeInfoParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ChatWindowActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(ChatWindowActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID,CUtils.getMyAndroidId(ChatWindowActivity.this));
        headers.put("channelid", CHANNEL_ID);
        return headers;
    }

    /**
     * Builds a channel-scoped preference key for extend reminder visibility state.
     */
    private String getExtendChatReminderVisibilityKey() {
        return EXTEND_REMINDER_VISIBLE_KEY_PREFIX + CHANNEL_ID;
    }

    /**
     * Persists extend reminder visibility for the active chat channel.
     */
    private void saveExtendChatReminderVisibility(boolean isVisible) {
        if (!TextUtils.isEmpty(CHANNEL_ID)) {
            CUtils.saveBooleanData(this, getExtendChatReminderVisibilityKey(), isVisible);
        }
    }

    /**
     * Restores extend reminder after rejoin if it was visible before leaving the chat window.
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

    private String follow;
    public void onFollowClick(String followAstro) {
        follow = followAstro;

        if (CUtils.isConnectedWithInternet(this)) {

            /*String url = CGlobalVariables.FOLLOW_ASTROLOGER_URL;
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    this, false, getfollowAstrologerParams(followAstro), FOLLOW_ASTROLOGER_REQ).getMyStringRequest();
            stringRequest.setShouldCache(true);
            if (queue == null)
                queue = VolleySingleton.getInstance(this).getRequestQueue();

            queue.add(stringRequest);*/



            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.followAstrologer(getfollowAstrologerParams(followAstro));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String myRespose = response.body().string();
                        JSONObject jsonObject = new JSONObject(myRespose);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if(follow.equalsIgnoreCase("0")) {
                            followStatus = "0";
                            CUtils.unSubscribeFollowTopic(context, astrologerId);
                        } else {
                            followStatus = "1";
                            CUtils.subscribeFollowTopic(context, astrologerId);
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



    public void showHideCallAgainButton(boolean show) {
        if (show) {
            btnCallAgain.setVisibility(View.VISIBLE);
        } else {
            btnCallAgain.setVisibility(View.GONE);
        }
    }

    private void openProfileDialog(){
        try {
            CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
        } catch (Exception e){
            //
        }
    }
    private void registerReceiverBackgroundLogin() {
        try {
            LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverBackgroundLoginService
                    , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        } catch (Exception e){
            //
        }
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
                String currentOfferType = CUtils.getCallChatOfferType(activity);
                if (CUtils.isFreeChat && currentOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
                    if(isChatCompleted) {
                        CUtils.callLocalNotificationForNewUser(ChatWindowActivity.this);
                        showRechargeAfterFreeChat();
                    }
                    /*if (isChatCompleted) {
                        getAstrologerStatusPrice(astrologerId,currentOfferType);
                    }*/
                }
            } catch (Exception e) {
                //
            }
        }
    };

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
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        handleWalletRechargeError();
        hideProgressBar();
    }

    private void actionOnKeyBoardVisibility() {
        RelativeLayout contentView = findViewById(R.id.containerLayout);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
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
                    scrollMyListViewToBottom();
                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }
    private void followStatus(){
        try {
            if (followAstrologerModelArrayList != null && followAstrologerModelArrayList.size() > 0) {
                for (int i = 0; i < followAstrologerModelArrayList.size(); i++) {
                    if (followAstrologerModelArrayList.get(i).getAstrologerId().equalsIgnoreCase(astrologerId)) {
                        followStatus = "1";
                        break;
                    }

                }
            }
        }catch (Exception e){
            //
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
                        Log.d("testProfile","Called res==>>>"+responses);
                        JSONObject jsonObject = new JSONObject(responses);
                        String status = jsonObject.getString("status");

                        if(status.equals("1")){
                            String isFollowing = jsonObject.getString("isfollowing");
                            if(isFollowing.equals("true")){
                                followStatus = "1";
                                //followAstrologer.setText(getString(R.string.following));
                            }
                            if(isFollowing.equals("false")){
                                followStatus = "0";
                                //followAstrologer.setText(getString(R.string.follow));
                            }

                        }

                    }catch (Exception e){
                        Log.d("testProfile","Called Exception==>>>"+e);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // hideProgressBar();
                }
            });

        }
    }

    private void closeActivityForAINotification() {
        if (countDownTimer != null) {
            chatWindowOpenType = "";
            if (!backPressFlag) {
                backPressFlag = true;
                userActionsEvents.append(CGlobalVariables.BACK_PRESS_EVENT).append(",");
                AstrosageKundliApplication.getmFirebaseDatabase(CHANNEL_ID).child(CGlobalVariables.USER_ACTION_EVENTS).setValue(userActionsEvents.toString());
            }
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
                AstrosageKundliApplication.statusMessageSetHistory = null;
                AstrosageKundliApplication.isChatAgainButtonClick = false;
            }

        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (endChatDisabledTimer != null) {
            endChatDisabledTimer.cancel();
        }

    }

    private void checkAIAstrologerInLocalList(boolean isFromIntent, String question, String revertQCount, String aiAstrologerId, String title, boolean isAiAstrologerOnline) {
        try {
            String list = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_LIST, "");
            if (!TextUtils.isEmpty(list)) {
                JSONObject jsonObject = new JSONObject(list);
                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                AstrologerDetailBean astrologerDetailBean = null;
                boolean astrologerFound = false;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    astrologerDetailBean = com.ojassoft.astrosage.varta.utils.CUtils.parseAstrologerObject(object);
                    if (astrologerDetailBean != null && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId()) && astrologerDetailBean.getAiAstrologerId().equals(aiAstrologerId)) {
                        astrologerFound = true;
                        break;
                    }
                }

                if (astrologerFound) {
                    openChatNotificationWindow(question, revertQCount, aiAstrologerId, title, isAiAstrologerOnline);
                } else {
                    //true from actionOnIntent() false from fetchAiAstroList()
                    if (isFromIntent) {
                        fetchAiAstroList(question, revertQCount, aiAstrologerId, title, isAiAstrologerOnline);
                    } else {
                        //if astrologer is not found in server list then use the first astrologer in the list
                        JSONObject object = jsonArray.getJSONObject(0);
                        astrologerDetailBean = com.ojassoft.astrosage.varta.utils.CUtils.parseAstrologerObject(object);
                        if (astrologerDetailBean != null && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId()) && astrologerDetailBean.getAiAstrologerId().equals(aiAstrologerId)) {
                            aiAstrologerId = astrologerDetailBean.getAiAstrologerId();
                            openChatNotificationWindow(question, revertQCount, aiAstrologerId, title, isAiAstrologerOnline);
                        }
                    }
                }
            } else {
                fetchAiAstroList(question, revertQCount, aiAstrologerId, title, isAiAstrologerOnline);
            }
        } catch (Exception e) {
            //
        }
    }

    private void openChatNotificationWindow(String question, String revertQCount, String aiAstrologerId, String title, boolean isAiAstrologerOnline) {
        com.ojassoft.astrosage.utils.CUtils.openAINotificationChatWindow(this, question, revertQCount, aiAstrologerId, title, isAiAstrologerOnline);
    }

    private void fetchAiAstroList(String question, String revertQCount, String aiAstrologerID, String title, boolean isAiAstrologerOnline) {
        showProgressBar();
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAIAstrologerList(getAIAstroListParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    hideProgressBar();
                    if (response.isSuccessful()) {
                        String mResponse = response.body().string();
                        if (!TextUtils.isEmpty(mResponse)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(context, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_LIST, mResponse);
                            checkAIAstrologerInLocalList(false, question, revertQCount, aiAstrologerID, title, isAiAstrologerOnline);
                        }
                    }
                } catch (Exception e) {
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
            }
        });
    }

    public HashMap<String, String> getAIAstroListParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this);
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(this);
        try {
            if (isLogin) {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
            } else {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, "");
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
                //offerType = "";
            }
        } catch (Exception e) {
            //
        }
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FETCHALL, "1");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, "" + BuildConfig.APPLICATION_ID);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(this));
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(activity));
        //Log.e("SAN ", " HF1 params= " + headers.toString());
        return headers;
    }


    private void loadOldChats(String astrologerId){
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<ChatMessage> msgList  = CUtils.filterStatusMessage(AstrosageKundliApplication.chatMessagesHistory);
            int index = 0;
            if(msgList != null){
                index = msgList.size();
            }
            loadStoredChatsPage(astrologerId, index, false);
        },100);
    }

    boolean loading = false;
    private void setOnScrollListener(){
        messagesListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("loadOldChats", "lastVisible item position ==>> " + mLayoutManager.findFirstVisibleItemPosition());
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
                int firstVisibleViewPostion = mLayoutManager.findFirstVisibleItemPosition();
                if (dy < 0) {
                    if (!loading && hasMoreStoredChats && firstVisibleViewPostion < 2) {
                        loading = true;
                        loadMoreData();
                    }
                }
            }
        });
    }

    private void loadMoreData(){
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                loadStoredChatsPage(astrologerId, messageAdapter.getItemCount(), true),100);
    }

    /**
     * Loads one page of persisted human chat messages off the main thread.
     */
    private void loadStoredChatsPage(String astrologerId, int startIndex, boolean scrollAfterAppend) {
        submitChatHistoryTask(() -> {
            ArrayList<ChatMessage> oldMessages;
            try {
                oldMessages = ChatHistoryDAO.getInstance(getApplicationContext())
                        .getOldMessages(astrologerId, startIndex, CHAT_HISTORY_PAGE_SIZE);
            } catch (Exception ignore) {
                oldMessages = null;
            }

            final ArrayList<ChatMessage> finalOldMessages = oldMessages;
            hasMoreStoredChats = finalOldMessages != null && finalOldMessages.size() == CHAT_HISTORY_PAGE_SIZE;

            runOnUiThread(() -> {
                try {
                    if (isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())) {
                        return;
                    }
                    if (finalOldMessages != null && !finalOldMessages.isEmpty()) {
                        messageAdapter.appendMessages(finalOldMessages);
                        if (scrollAfterAppend) {
                            messagesListView.post(() -> messagesListView.scrollToPosition(finalOldMessages.size()));
                            if(AstrosageKundliApplication.allChatMessagesHistory != null){
                                AstrosageKundliApplication.allChatMessagesHistory.addAll(0,finalOldMessages);
                            }
                        }
                    }
                } finally {
                    loading = false;
                }
            });
        });
    }

    /**
     * Persists a newly received chat message without blocking the chat UI thread.
     */
    private void persistMessageAsync(UserMessage message) {
        final String channelId = CHANNEL_ID;
        final String currentAstrologerId = astrologerId;
        submitChatHistoryTask(() ->
                ChatHistoryDAO.getInstance(getApplicationContext()).addMessage(message, channelId, currentAstrologerId));
    }

    /**
     * Updates the seen state for a stored chat message on the background executor.
     */
    private void updateSeenMessageAsync(UserMessage message) {
        final String currentAstrologerId = astrologerId;
        submitChatHistoryTask(() ->
                ChatHistoryDAO.getInstance(getApplicationContext()).updateMessageStatus(message, currentAstrologerId));
    }

    /**
     * Queues chat-history work and ignores late submissions after the activity has started tearing down.
     */
    private void submitChatHistoryTask(Runnable task) {
        try {
            chatHistoryExecutor.execute(task);
        } catch (RejectedExecutionException ignore) {
            loading = false;
        }
    }
    public void showAlertMessage(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ChatWindowActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.leaving_chat));
        alertDialog.setMessage(getResources().getString(R.string.are_you_sure_want_to_leave_chat));
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * This method is used to submit data on server while user cancel the recharge dialog after free chat complete
     */
    public void cancelRechargeAfterChat() {
        ChatUtils.getInstance(this).cancelRechargeAfterChat(channelIdForNps,"INSUFFICIENT_DIALOG_CANCEL_BTN");
    }
}
