package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PERSONALIZED_AI_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.FullProfileFragment;
import com.ojassoft.astrosage.ui.fragments.ShareMessageFragment;
import com.ojassoft.astrosage.varta.adapters.AINotificationChatMessageAdapter;
import com.ojassoft.astrosage.varta.aichat.GetNotificationAnswer;
import com.ojassoft.astrosage.varta.aichat.models.GreetingsModel;
import com.ojassoft.astrosage.varta.dialog.ConnectPaidChatDialog;
import com.ojassoft.astrosage.varta.dialog.ConnectPaidChatOldDialog;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.AIVoiceCallingService;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ContactNumberRestrictMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.LeftStatusMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.WelcomeStatusMessage;
import com.ojassoft.astrosage.varta.ui.fragments.RechargePopUpAfterFreeChat;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.TypeWriterNotification;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Must Read
 * This activity created on the behalf of MainChatFragment to manage all chat related task
 * */

public class AINotificationChatActivity extends BaseActivity implements VolleyResponse, View.OnClickListener, GlobalRetrofitResponse, TypeWriterNotification.OnTypingComplete, ConnectPaidChatDialog.IConnectPaidChat {
    private static final String TAG = "AINotificationChatActivity";
    CustomProgressDialog pd;
    Toolbar toolbarChat;
    TextView typingTextview;
    ImageView ivbackChat, shareChat, copyChat, callBtn;
    CircularNetworkImageView astrologerProfilePic;
    TextView titleTextChat, timerTextview;
    TextView endChatButton;
    Activity activity;
    public static String CHANNEL_ID = "";
    Context context;
    RequestQueue queue;
    RelativeLayout sendButton, containerLayout, buttonKundli;
    public RecyclerView messagesListView;
    LinearLayoutManager mLayoutManager;
    EditText messageTextEdit;
    ImageView imgViewDowmArrow, ivFRPAFC;


    public AppCompatButton btnChatAgain;
    ConstraintLayout llConnectAgain;
    public static boolean openKundli = false;
    FeedbackDialog feedbackDialog;
    public AINotificationChatMessageAdapter messageAdapter;
    LinearLayout relSendMessage;
    private Random numRandom;
    public String astrologerId, astrologerName, astrologerProfileUrl, userChatTime, aiAstrologerId, astrologerLageProfile;
    private Animation animShow, animHide;
    private TextView tvChatRechargeButton;

    private String userNameTempStore = "";

    public static String followStatus = "0";

    public boolean isTypingGreetingMessage = false;
    public boolean isAvailableForCall;
    public String isNewSession = "1", isLocallyAnswered = "0",  previousQues= "", previousAns = "";
    public boolean stopTypeWriter = false;
    public View llChatButton;
    public LinearLayout llTypeWriterParent;
    public boolean scrollWhileTypeWriter = false;
    public int currentTypeWriterPosition;
    public TypeWriterNotification currentTypeWriter;
    public View currentView;
    public int mIndexTypeWriter;
    public static String langCode = "";
    public int AI_LANGUAGE_CODE;

    public static int isRoman;
    String revertQCount = "", question = "", title = "";
    boolean isAIAstrologerOnline = false;
    public AstrologerDetailBean astrologerDetailBean;
    GetNotificationAnswer GetNotificationAnswer;
    public int useIntroOffer = 0;
    public int offerFromNotification = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat_window);

        AI_LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        //Log.e("SAN ", " onCreate() call ");
        activity = this;
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_WINDOW_SHOWN, PERSONALIZED_AI_NOTIFICATION, "");

        //CUtils.isAutoChatConnected = false;
        inti();
        CUtils.updateChatBackgroundBasedOnTheme(this);
        if (savedInstanceState == null) {
            if (getIntent() != null) {
                handleIntent(getIntent());
            }
        } else {
            finish();
        }
        //  followStatus();
        //getFollowingAstrologerDataFromServer();

        //actionOnKeyBoardVisibility();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_WINDOW_CLOSED, PERSONALIZED_AI_NOTIFICATION, "");
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Update the intent
        handleIntent(intent); // handles new notification click
    }

    private void inti() {
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
        callBtn = findViewById(R.id.ai_call_btn);
        buttonKundli = findViewById(R.id.buttonKundli);
        messagesListView = findViewById(R.id.listViewMessages);
        messageTextEdit = findViewById(R.id.editTextMessage);
        relSendMessage = findViewById(R.id.relSendMessage);
        btnChatAgain = findViewById(R.id.btnChatAgain);
        llConnectAgain = findViewById(R.id.llConnectAgain);
        llConnectAgain.setVisibility(View.GONE);
        tvChatRechargeButton = findViewById(R.id.tvChatRechargeButton);
        ivSend = findViewById(R.id.ivSend);
        ivStop = findViewById(R.id.ivStop);
        messageAdapter = new AINotificationChatMessageAdapter(activity);
        mLayoutManager = new LinearLayoutManager(this);
        messagesListView.setLayoutManager(mLayoutManager);
        messagesListView.setAdapter(messageAdapter);
        messagesListView.setNestedScrollingEnabled(false);

        imgViewDowmArrow = findViewById(R.id.imgViewDowmArrow);

        ivFRPAFC = findViewById(R.id.ivFRPAFC);
        timerTextview.setVisibility(View.GONE);
        buttonKundli.setVisibility(View.GONE);

        if(CUtils.getBooleanData(this, CGlobalVariables.IS_AI_CALL_SHOW_IN_AI_CHAT, false)){
            callBtn.setOnClickListener(this);
            callBtn.setVisibility(View.VISIBLE);
        }else{
            callBtn.setVisibility(View.GONE);
        }
        /*imgViewDowmArrow.setOnClickListener(view1 -> {
            messagesListView.scrollToPosition(ScrollView.FOCUS_DOWN);
        });*/
        //hideShowScrollDown();

        /*messagesListView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (isAtBottom(recycleNestedScrollView)) {
                //isScroll = true;
                scrollWhileTypeWriter = true;
                imgViewDowmArrow.setVisibility(View.GONE);
            } else {
                imgViewDowmArrow.setVisibility(View.VISIBLE);
            }
        });*/

        //Glide.with(ivWaitMsg).load(R.drawable.typing).into(ivWaitMsg);
        setUpListeners();
        numRandom = new Random();

        //CGlobalVariables.CHAT_END_STATUS = "";

        ivbackChat.setOnClickListener(this);
        endChatButton.setOnClickListener(this);
        shareChat.setVisibility(View.GONE);
        shareChat.setOnClickListener(this);
        copyChat.setOnClickListener(this);
        copyChat.setVisibility(View.GONE);
        tvChatRechargeButton.setOnClickListener(this);

        initAnimation();

    }

    private void handleIntent(Intent intent) {
        try {
            if (intent != null) {
                if (intent.hasExtra(CGlobalVariables.KEY_REVERT_QUESTION_COUNT)) {
                    revertQCount = intent.getStringExtra(CGlobalVariables.KEY_REVERT_QUESTION_COUNT);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ID)) {
                    aiAstrologerId = intent.getStringExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ID);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_QUESTION)) {
                    question = intent.getStringExtra(CGlobalVariables.KEY_AI_QUESTION);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_NOTIFICATION_TITLE)) {
                    title = intent.getStringExtra(CGlobalVariables.KEY_AI_NOTIFICATION_TITLE);
                }
                if (intent.hasExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE)) {
                    isAIAstrologerOnline = intent.getBooleanExtra(CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE, false);
                }
                AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs + "\n AINotificationChatActivity handleIntent aiAstrologerId: " + aiAstrologerId;

                intent.replaceExtras(new Bundle());

                if (!TextUtils.isEmpty(aiAstrologerId)) {// && !TextUtils.isEmpty(question)

                    String aiAstrologerList = CUtils.getStringData(this, CGlobalVariables.KEY_AI_ASTROLOGER_LIST, "");
                    JSONObject jsonObject = new JSONObject(aiAstrologerList);
                    JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                    astrologerDetailBean = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        astrologerDetailBean = CUtils.parseAstrologerObject(object);
                        if (!TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId()) && astrologerDetailBean.getAiAstrologerId().equals(aiAstrologerId)) {
                            break;
                        }
                    }

                    if (astrologerDetailBean != null) {
                        AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs + "\n AINotificationChatActivity handleIntent astrologerDetailBean: " + astrologerDetailBean.getAiAstrologerId();

                        astrologerProfileUrl = astrologerDetailBean.getImageFileLarge();
                        String image = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFileLarge();
                        Glide.with(getApplicationContext()).load(image).circleCrop().into(astrologerProfilePic);
                        titleTextChat.setText(astrologerDetailBean.getName());
                    }

                    try {
                        CUtils.playChatConnectSound(activity, 2);
                    } catch (Exception e) {
                        Log.d("aiListService", "handleIntent ex 1: " + e);
                    }

                    if (!TextUtils.isEmpty(revertQCount)) {
                        sendProfileMsg();
                        showHideTypingIndicator(View.VISIBLE);
                        getGreetingMessage();
                    } else {
                        disableSendButton();
                        CUtils.hideMyKeyboard(this);
                        //showHideTypingIndicator(View.VISIBLE);
                        String answerId = String.valueOf(numRandom.nextInt(999) + 1);
                        speakOut(question, "", answerId);
                    }


                } else {
                    finish();
                }

            }
        } catch (Exception e) {
            Log.d("aiListService", "handleIntent ex 2: " + e);
        }
    }

    private boolean isAtBottom(NestedScrollView scrollView) {
        View contentView = scrollView.getChildAt(0);
        return contentView.getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpListeners() {
        messagesListView.setOnTouchListener((v, event) -> {
            scrollWhileTypeWriter = false;
            return false;
        });

        sendButton.setOnClickListener(v -> {
            try {
                // If the stop button is visible, stop the typewriter effect
                if (ivStop.getVisibility() == View.VISIBLE) {
                    stopTypeWriter = true;
                } else {
                    AstrologerDetailBean activeAstrologerDetailBean = getActiveAstrologerDetailBean();
                    if (activeAstrologerDetailBean == null) {
                        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Log the send button click event for analytics
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_SEND_BUTTON_CLICKED, PERSONALIZED_AI_NOTIFICATION, "");
                    // Get the AI astrologer ID from the active astrologer detail bean
                    String aiai = activeAstrologerDetailBean.getAiAstrologerId();
                    // If the chat service is running and the AI astrologer ID is valid, show the already in chat dialog
                    if (checkServiceRunning(OnGoingChatService.class) && (TextUtils.isEmpty(aiai) || !aiai.equals("0"))) {
                        openConnectChatDialog(null, getString(R.string.already_in_chat));
                         } else {
                        // Get the text input from the user
                        String text = getTextInput();
                        // Get the current offer type
                        String offerType = CUtils.getCallChatOfferType(context);

                        // If the input text is not empty, proceed
                        if (!TextUtils.isEmpty(text)) {
                            // If the user has an AI Pro plan, connect chat directly
                            if (CUtils.isKundliAiProPlan(AINotificationChatActivity.this)) {
                                connectPaidChat(text);
                            }
                            // If the offer type is free, set flags and connect chat directly
                            else if (offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                                activeAstrologerDetailBean.setFreeForChat(true);
                                activeAstrologerDetailBean.setUseIntroOffer(true);
                                connectPaidChat(text);
                            }
                            // If the offer type is reduced price, set flags and fetch astrologer status/price
                            else if (offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
                                activeAstrologerDetailBean.setFreeForChat(true);
                                activeAstrologerDetailBean.setUseIntroOffer(true);
                                getAstrologerStatusPrice(activeAstrologerDetailBean.getAstrologerId());
                            }
                            // For other offer types, fetch astrologer status/price
                            else {
                                getAstrologerStatusPrice(activeAstrologerDetailBean.getAstrologerId());
                            }
                        }
                    }

                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to process AI notification send action", e);
            }
        });
    }

    // Fetches the astrologer's current chat/call price and status from the server, then parses the response and opens the connect chat dialog
    private void getAstrologerStatusPrice(String astroid) {
        // Show the progress bar to indicate loading
        showProgressBar();
        // Create an instance of the API interface using Retrofit
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        // Prepare the API call with required parameters
        Call<ResponseBody> call = api.getAstrologerStatusPrice(getAstroStatusParams(astroid));
        //  Log.e("TestNewUser", call.request().url().toString());
        // Enqueue the API call asynchronously
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Hide the progress bar after receiving response
                hideProgressBar();
                try {
                    // Get the response body as a string
                    String myResponse = response.body().string();
                    // Parse the astrologer status and price from the response
                    parseAstrologerStatus(myResponse);

                } catch (Exception e) {
                    // If parsing fails, open the connect chat dialog
                    openConnectChatDialog(astrologerDetailBean , getTextInput());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Hide the progress bar if the API call fails
                hideProgressBar();
                // Open the connect chat dialog on failure
                openConnectChatDialog(astrologerDetailBean , getTextInput());
            }
        });
    }

    // Parses the astrologer status and price from the server response, updates the local astrologerDetailBean, and opens the connect chat dialog
    private void parseAstrologerStatus(String response) {
        try {
            //Log.e("TestAI", "response=" + response);
            // Parse the response string into a JSON object
            JSONObject jsonObject = new JSONObject(response);

            // If the JSON contains "actualServicePriceInt", update astrologerDetailBean
            if (jsonObject.has("actualServicePriceInt")) {
                astrologerDetailBean.setActualServicePriceInt(jsonObject.getString("actualServicePriceInt"));
            }
            // If the JSON contains "price", update astrologerDetailBean
            if (jsonObject.has("price")) {
                astrologerDetailBean.setServicePrice(jsonObject.getString("price"));
            }


        } catch (Exception e) {
            // Log any exception that occurs during parsing
            Log.e("ASTROLOGER_DE", "exp2=" + e);
        }
        // Open the connect chat dialog after parsing
        openConnectChatDialog(astrologerDetailBean,getTextInput());
    }

    // Opens the dialog to connect to a paid chat with the astrologer (deciding from tagmanger for old or new dialog)
    private void openConnectChatDialog(AstrologerDetailBean astrologerDetailBean, String msg) {
        try {
            if(CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_NEW_CONNECT_CHAT_DIALOG, false)){
                // Create a new ConnectPaidChatDialog instance with context, astrologer details, and input text
                ConnectPaidChatDialog connectPaidChatDialog = new ConnectPaidChatDialog(AINotificationChatActivity.this, astrologerDetailBean, msg);
                // Show the dialog using the fragment manager
                connectPaidChatDialog.show(getSupportFragmentManager(), "ConnectPaidChatDialog");
            }else{
                ConnectPaidChatOldDialog connectPaidChatOldDialog = new ConnectPaidChatOldDialog(AINotificationChatActivity.this, astrologerDetailBean, msg);
                // Show the dialog using the fragment manager
                connectPaidChatOldDialog.show(getSupportFragmentManager(), "ConnectPaidChatOldDialog");
            }
        } catch (Exception e) {
            // Handle exception if dialog cannot be shown
        }

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
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(activity));
        String offerType = CUtils.getCallChatOfferType(this);
        if (offerType == null) offerType = "";
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        //Log.e("TestNewUser", "offer= " + offerType);

        if (!TextUtils.isEmpty(offerType)) {
            useIntroOffer = 1;
            offerFromNotification = 1;
        } else {
            useIntroOffer = 0;
            offerFromNotification = 0;
        }
        headers.put(CGlobalVariables.USE_INTRO_OFFER, String.valueOf(useIntroOffer));
        headers.put(CGlobalVariables.OFFER_FROM_NOTIFICATION, offerFromNotification + "");
        //Log.e("TestNewUser", "headers= " + headers);
        return headers;
    }

    private void sendMessage(long chatId) {
        String messageText = getTextInput();
        addMessageToAdapter(messageText, CGlobalVariables.USER, chatId, false);
        showHideTypingIndicator(View.VISIBLE);
        sendPromptToAI(messageText);
        //sendMsgToFirebase(messageText, chatId, CGlobalVariables.USER, CGlobalVariables.ASTROLOGER);
        clearTextInput();
    }

    public void sendMsgToFirebase(String message, int chatId, String from, String to, String msgType) {
        /*String channelId = CHANNEL_ID;

        Map chatMap = new HashMap();
        chatMap.put("From", from);
        chatMap.put("To", to);
        chatMap.put("Text", message);
        chatMap.put("MsgTime", ServerValue.TIMESTAMP);
        chatMap.put("isSeen", false);
        chatMap.put("chatId", chatId);
        *//**
         * speakOutGreet send only this Message Type Greet
         *//*
        if (msgType.equals(CGlobalVariables.MSG_TYPE_GREET)) {
            chatMap.put(CGlobalVariables.MSG_TYPE, CGlobalVariables.GREET);
        }
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.MESSAGES_FBD_KEY).push().setValue(chatMap);
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.LAST_MSG_TIME_FBD_KEY).setValue(ServerValue.TIMESTAMP);*/
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
        if (visibility == View.VISIBLE) {
            messagesListView.post(() -> {
                messageAdapter.addTyping();
            });
        } else if (visibility == View.GONE) {
            messageAdapter.removeTyping();
        }
        scrollMyListViewToBottom();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_chat:
                onBackPressed();
                break;
            case R.id.iv_share_chat:
                //Log.e("SAN ", " CMW action share "  );
                toOpenShareDialog(messageAdapter.getMessageList());
                break;
            case R.id.iv_copy_chat:
                //Log.e("SAN ", " CMW copy chat "  );
                toOpenCopyDialog(messageAdapter.getMessageList(), messageAdapter.getCopyMessageList());
                break;
            case R.id.ai_call_btn:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CALL_BUTTON_CLICKED, PERSONALIZED_AI_NOTIFICATION, "");
                AstrologerDetailBean activeAstrologerDetailBean = getActiveAstrologerDetailBean();
                // Get the AI astrologer ID from the active astrologer detail bean
                if(activeAstrologerDetailBean != null) {
                    String aiai = activeAstrologerDetailBean.getAiAstrologerId();
                    // If the chat service is running and the AI astrologer ID is valid, show the already in chat dialog
                    if (checkServiceRunning(AIVoiceCallingService.class) && (TextUtils.isEmpty(aiai) || !aiai.equals("0"))) {
                        openConnectChatDialog(null, getString(R.string.already_in_call));
                    } else {
                        activeAstrologerDetailBean.setCallSource(CGlobalVariables.AI_NOTIFICATION_TOP_CALL_BTN);
                        connectPaidCall();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
            if (queue != null) {
                queue.cancelAll("chatRequestQueue");
            }
            CUtils.cancelNotification(AINotificationChatActivity.this);
            CUtils.cancelChatNotification(AINotificationChatActivity.this);
        } catch (Exception e) {
            //
        }

        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
        }
        super.onDestroy();
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

    public void showAstrologorFullProfile() {
        if (astrologerLageProfile != null && astrologerLageProfile.length() > 0) {
            String astroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerLageProfile;
            FullProfileFragment.getInstant(astrologerName, astroImage).show(getSupportFragmentManager(), FullProfileFragment.TAG);
        }
    }

    public Map<String, String> getChatCompleteParams(String channelID, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(AINotificationChatActivity.this));
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
        return CUtils.setRequiredParams(headers);
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

    public void openWalletScreen() {
        Intent intent = new Intent(AINotificationChatActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "AIChatWindowActivity");
        startActivity(intent);
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
                        textToShare = textToShare + "[" + cmi.getDateCreated() + "] " + author + ": " + cmi.getMessageBody(activity) + "\n";

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
            com.ojassoft.astrosage.utils.CUtils.copyTextToClipBoard(textToCopy.replace("<br>", "\n"), this);
            clearCopyList();
        } else {
            Toast.makeText(activity, "Please select chat message", Toast.LENGTH_LONG).show();
        }


    }

    public void clearCopyList() {
        messageAdapter.getCopyMessageList().clear();
        updateCopyView(false);
        messageAdapter.notifyDataSetChanged();
        messageAdapter.isLongPressClicked = false;
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
            rechargePopUpAfterFreeChat = new RechargePopUpAfterFreeChat(CGlobalVariables.AINOTIFICATIONCHATACTIVITY);
            rechargePopUpAfterFreeChat.show(getSupportFragmentManager(),
                    "RechargePopUpAfterFreeChat");
        } catch (Exception e) {
            e.printStackTrace();
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

    public void sendPromptToAI(String messageText) {
        if (CUtils.isConnectedWithInternet(this)) {

            try {
                if (GetNotificationAnswer == null) {
                    GetNotificationAnswer = new GetNotificationAnswer(AINotificationChatActivity.this, revertQCount);
                }
                GetNotificationAnswer.getQueryAnswer(AINotificationChatActivity.this, messageText);
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
            showHideTypingIndicator(View.GONE);
            enableSendButton();
            if (showError) {
                messageAdapter.markLastQuestionError();
            }
        });
    }

    public void showQuestionExpiredDialog() {
        openConnectChatDialog(null, "");
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

    public void speakOut(String result, String link, String answerId) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_ASTRO_MESSAGE_SHOWN, PERSONALIZED_AI_NOTIFICATION, "");
        showHideTypingIndicator(View.GONE);
        if (TextUtils.isEmpty(answerId)) {
            answerId = String.valueOf(numRandom.nextInt(999) + 1);
        }
        addMessageToAdapter(result, CGlobalVariables.ASTROLOGER, Long.parseLong(answerId), true);
    }

    public void speakOutGreet(String result, String link, String answerId) {
        showHideTypingIndicator(View.GONE);
        if (TextUtils.isEmpty(answerId)) {
            answerId = String.valueOf(numRandom.nextInt(999) + 1);
        }
        addMessageToAdapter(result, CGlobalVariables.ASTROLOGER, Long.parseLong(answerId), true);
        int chatId = 0;
        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_GREETING_SHOWN, PERSONALIZED_AI_NOTIFICATION, "");
        try {
            chatId = Integer.parseInt(answerId);
        } catch (Exception e) {
            //
        }

    }

    public void addMessageToAdapter(String messageText, String messageFrom, long chatId, boolean animate) {
        // Log.e("MyTag", "addMessage() text = "+messageText);
        Message message = new Message();
        message.setAuthor(messageFrom);
        message.setDateCreated(getMessageDate());
        message.setMessageBody(messageText);
        message.setSeen(animate);
        message.setChatId(chatId);
        messagesListView.post(() -> {
            messageAdapter.addMessage(message);
        });
        scrollMyListViewToBottom();
    }

    private void getGreetingMessage() {
        disableSendButton();
        try {
            Call<GreetingsModel> call = RetrofitClient.getAIInstance().create(ApiList.class).getGreetingMessage(CUtils.getGreetingMessageParams(this, aiAstrologerId, AI_LANGUAGE_CODE, false));
            call.enqueue(new Callback<GreetingsModel>() {
                @Override
                public void onResponse(@NonNull Call<GreetingsModel> call, @NonNull Response<GreetingsModel> response) {
                    if (response.isSuccessful()) {
                        GreetingsModel greetingsModel = response.body();
                        Log.d(TAG, "onResponse: " + new Gson().toJson(greetingsModel, GreetingsModel.class));
                        if (greetingsModel != null) {
                            isTypingGreetingMessage = true;
                            int chatId = numRandom.nextInt(999) + 1;
                            speakOutGreet(greetingsModel.getMessage() + "\n", "", String.valueOf(chatId));
//                            addMessageToAdapter(greetingsModel.getMessage(), CGlobalVariables.ASTROLOGER, chatId, true);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GreetingsModel> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t);
                    showHideTypingIndicator(View.GONE);
                    enableSendButton();
                }
            });


        } catch (Exception e) {
            showHideTypingIndicator(View.GONE);
            enableSendButton();
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
            if (GetNotificationAnswer != null) {
                GetNotificationAnswer.parseJsonAtOnce(finalJSONData, true, null);
            }
            finalJSONData = "";
//            messageAdapter.refreshSingleItem(currentTypeWriterPosition);
            messageAdapter.refreshList();
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
                if (isAIAstrologerOnline && !TextUtils.isEmpty(question)) {
                    messageTextEdit.setText(question);
                    identifyLanguageWithStringInput();
                }
            }
            try {
                RecyclerView.ViewHolder holder = messagesListView.findViewHolderForAdapterPosition(messageAdapter.getItemCount() - 1);
                if (holder != null) {
                    holder.itemView.findViewById(R.id.llButtonsParent).setVisibility(View.VISIBLE);
                }
                currentTypeWriter = null;
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
                    final float y = messagesListView.getBottom();
                    if (messagesListView != null) {
                        mLayoutManager.scrollToPositionWithOffset((int) y, Integer.MIN_VALUE);
                        //hideShowScrollDown();
                    }
                });
            }
        } catch (Exception e) {
            //
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

    public static String getMessageDate() {
        return new SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(new java.util.Date());
    }

    public void sendProfileMsg() {
        try {
            UserProfileData userProfileData = CUtils.getProfileForChatFromPreference(this);
            if (userProfileData == null) {
                return;
            }
            String messageText = sendProfileDetailMsg(userProfileData);
            // messageTextEdit.setText(messageText);
            // identifyLanguageWithStringInput(false);
            if (!TextUtils.isEmpty(messageText)) {
                int chatId = new Random().nextInt(999) + 1;
                addMessageToAdapter(messageText, CGlobalVariables.USER, chatId, false);
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

    private void identifyLanguageWithStringInput() {
        String text = getTextInput().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }

        disableSendButton();
        CUtils.hideMyKeyboard(this);

        int chatId = numRandom.nextInt(999) + 1;
        try {
            LanguageIdentificationOptions identifierOptions = new LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.8f).build();
            LanguageIdentifier languageIdentifier = LanguageIdentification.getClient(identifierOptions);
            languageIdentifier.identifyPossibleLanguages(text).addOnSuccessListener(identifiedLanguages -> {
            /*try {
                for (IdentifiedLanguage item : identifiedLanguages) {
                    Log.d("languageCodeIssue", "confidence->" + item.getConfidence() + "::code->" + item.getLanguageTag());
                }
            } catch (Exception e) {
                //
            }*/
            });
            langCode = "en";
            languageIdentifier.identifyLanguage(text).addOnSuccessListener(languageCode -> {

                langCode = checkValidLanguage(languageCode);

                if (checkRomanLang(text)) {
                    isRoman = 1;
                } else {
                    isRoman = 0;
                }

                sendMessage(chatId);

            }).addOnFailureListener(e -> {
                setMessageLang(text);
                sendMessage(chatId);
            });
        } catch (Exception exception) {
            setMessageLang(text);
            sendMessage(chatId);
        }
    }

    private String checkValidLanguage(String detectedLanguageCode) {
        String[] validLanguages = {"en", "hi", "ta", "bn", "mr", "ml", "te", "gu", "kn","or","as","es","ja","zh","pt","de","it"};
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
        } catch (Exception e) {
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

    public static void setLikeUnlike(Context context, String answerId, int isLiked) {
        try {
            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).setLikeUnlike(
                    CUtils.getMyAndroidId(context),
                    CUtils.getAppPackageName(context),
                    String.valueOf(CUtils.getAppVersion(context)),
                    CUtils.getApplicationSignatureHashCode(context),
                    "getuserlikedresponse",
                    answerId,
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


        } catch (Exception e) {
            //
        }

    }

    public void shareMessage(String question, String answer) {
        ShareMessageFragment.getInstance(question, answer).show(getSupportFragmentManager(), ShareMessageFragment.TAG);
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
                String currentOfferType = CUtils.getCallChatOfferType(activity);
                if (CUtils.isFreeChat && currentOfferType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {

                    CUtils.callLocalNotificationForNewUser(AINotificationChatActivity.this);
                    showRechargeAfterFreeChat();

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

    public void setResponse(String updatedString, boolean isTypeWriterEffect, String answerId) {
        runOnUiThread(() -> {
            showHideTypingIndicator(View.GONE);

            if (isTypeWriterEffect) {
                try {
                    addMessageToAdapter(updatedString, CGlobalVariables.ASTROLOGER, Long.parseLong(answerId), true);
                } catch (Exception e) {
                    addMessageToAdapter(updatedString, CGlobalVariables.ASTROLOGER, -1, true);
                }


            } else {
                // Log.e("MyTag", "contains \\n = " + updatedString.contains("\\n\\n"));
                if (currentTypeWriter != null) {
                    if (updatedString.contains("\\n\\n")) {
                        messageChunks = split(updatedString);
                        // Log.e("MyTag", "chunk Messages = " + messageChunks);
                    } else {
                        currentTypeWriter.updateAnimatedText(updatedString);
                    }
                }
            }
        });
    }

    @Override
    public void onFinishTyping(int index) {
        int nextPosition = index + 1;
        // Log.e("MyTag", "Current position = " + index);
        if (messageChunks == null || nextPosition >= messageChunks.size() || stopTypeWriter) {
            toggleSendStopButtonVisibility(true);
            return;
        }
        TypeWriterNotification instance = typeWriterInstance(nextPosition);
        llTypeWriterParent.addView(instance);
        stopTypeWriter = false;
        currentTypeWriterPosition = nextPosition;
        currentTypeWriter = instance;
        //Log.e("MyTag","position = "+ nextPosition +" Message = "+ messageChunks.get(nextPosition));

        instance.animateText(messageChunks.get(nextPosition), AINotificationChatActivity.this, this);
    }

    private TypeWriterNotification typeWriterInstance(int index) {
        TypeWriterNotification newTypeWriter = new TypeWriterNotification(this, index);
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
        messageAdapter.updateMessageInAdapter(updateSting);
    }

    private ArrayList<String> split(String text) {
        String[] split = text.split("\\\\n\\\\n");
        ArrayList<String> list = new ArrayList<>();
        for (String s : split) {
            if (!s.trim().isEmpty()) {
                list.add(s + "\n");
            }
        }
        return list;
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

    @Override
    public void connectPaidChat(String message) {
        //CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_CONNECT_PAID_CHAT_YES_CLICKED, PERSONALIZED_AI_NOTIFICATION, "");
        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_CONNECT_PAID_CHAT_BUTTON_CLICKED, FIREBASE_EVENT_ITEM_CLICK, "");
        if (AstrosageKundliApplication.chatMessagesHistory == null) {
            AstrosageKundliApplication.chatMessagesHistory = new ArrayList<>();
        }
        for (ChatMessage chatMessage : messageAdapter.getMessageList()) {
            chatMessage.setSeen(false);
            AstrosageKundliApplication.chatMessagesHistory.add(chatMessage);
        }
        CUtils.isFromAINotification = true;
        CUtils.AINotificationChatWindowNextQuestion = getTextInput();
        CUtils.AINotificationChatWindowTitle = title;
        astrologerDetailBean.setCallSource(CGlobalVariables.AI_NOTIFICATION_CHAT_ACTIVITY_SOURCE);
        ChatUtils.getInstance(this).initAIChat(astrologerDetailBean);
    }

    @Override
    public void closeChatWindow() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_CHAT_CONNECT_PAID_CHAT_NO_CLICKED, PERSONALIZED_AI_NOTIFICATION, "");
        //finish();
    }

    public void connectPaidCall() {
        try {
            AstrologerDetailBean activeAstrologerDetailBean = getActiveAstrologerDetailBean();
            if (activeAstrologerDetailBean == null) {
                Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                return;
            }
            String  currentOfferType = CUtils.getCallChatOfferType(this);
            if (!TextUtils.isEmpty(currentOfferType)) {
                activeAstrologerDetailBean.setUseIntroOffer(true);
                activeAstrologerDetailBean.setUseIntroOffer(true);
            }

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
                activeAstrologerDetailBean.setCallSource("NotificationChatWindow");
                ChatUtils.getInstance(this).initVoiceCall(activeAstrologerDetailBean);
            }else{
                CUtils.showPreMicPermissionDialog(this,()->{
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                });
            }

        } catch (Exception e) {
            // Handle any exceptions silently
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> { // The callback lambda
                if (isGranted) {
                    AstrologerDetailBean activeAstrologerDetailBean = getActiveAstrologerDetailBean();
                    if (activeAstrologerDetailBean != null) {
                        ChatUtils.getInstance(this).initVoiceCall(activeAstrologerDetailBean);
                    } else {
                        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CUtils.showPermissionDeniedDialog(this);
                }
            });

    /**
     * Returns the astrologer currently driving this AI notification chat flow.
     */
    private AstrologerDetailBean getActiveAstrologerDetailBean() {
        if (astrologerDetailBean != null) {
            return astrologerDetailBean;
        }
        return AstrosageKundliApplication.selectedAstrologerDetailBean;
    }

}
