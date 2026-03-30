package com.ojassoft.astrosage.varta.ui;


import static com.ojassoft.astrosage.ui.act.OutputMasterActivity.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.ui.act.OutputMasterActivity.CURRENT_VARSHPHAL_YEAR_KEY;
import static com.ojassoft.astrosage.ui.act.OutputMasterActivity.LAGANA_CHART_QUESTION;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_LAGNA_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOLD_PLAN_QUES_COUNT_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CUtils.showSnakbar;
import static com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity.checkRomanLang;
import static com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity.isHindiLanguage;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CLOUD_CHAT_LIMIT_UPGRADE_PLAN_PID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE_YEAR_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FROM_HISTORY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_IS_CHAT_FOR_HOME_CATEGORY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_KUNDLI_AI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_MODULE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SUB_MODULE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_VIDEO_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.MATCHING_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NUMERO_CLNY_TYPE_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PANCHANG_DATE_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PANCHANG_LID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.RASHI_HOROSCOPE_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.model.StaticQuestionAdapter;
import com.ojassoft.astrosage.model.SuggestQuestionAdapter;
import com.ojassoft.astrosage.model.SuggestedQuestionHelperClass;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.TelUsMoreDialog;
import com.ojassoft.astrosage.ui.act.ActAppSplash;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.LimitExceedDhruvPurchaseActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.GPSCitySearch;
import com.ojassoft.astrosage.ui.fragments.ShareKundliMessageFragment;
import com.ojassoft.astrosage.ui.fragments.ShareMessageFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.varta.adapters.KundliAIChatMsgAdapter;
import com.ojassoft.astrosage.varta.aichat.models.GetAnswerFromServerForKundali;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.dialog.TopupRechargeDialog;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ContactNumberRestrictMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.LeftStatusMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.WelcomeStatusMessage;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.TypeWritterKundali;

import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MiniChatWindow is a comprehensive chat interface for AI-powered astrological consultations.
 *
 * This activity provides a full-featured chat experience with the following key capabilities:
 *
 * AI Chat Interface: Real-time conversation with AI astrologer using natural language processing
 * Multi-language Support: Automatic language detection and support for Hindi, English, and Romanized Hindi
 * Voice Features: Text-to-speech for AI responses and speech-to-text for user input
 * Location Services: Integration with GPS and place search for accurate astrological calculations
 * Message Management: Copy, share, delete, and rate individual messages
 * Chat History: Persistent storage and retrieval of conversation history
 * Question Suggestions: Dynamic suggestion system with auto-scrolling static questions
 * Plan Management: Integration with subscription plans and upgrade prompts
 * Theme Support: Dynamic background changes based on chat categories and system theme
 *
 * Key Features:
 * - Typewriter effect for AI responses with chunked message display
 * - Real-time typing indicators and message status updates
 * - Automatic scroll management and keyboard handling
 * - Sensor-based background animations for enhanced UX
 * - Comprehensive error handling and offline support
 * - Analytics tracking for user interactions and plan upgrades
 *
 * Architecture:
 * - Extends BaseActivity for common functionality
 * - Implements multiple interfaces for sensor events, typing completion, and chat callbacks
 * - Uses RecyclerView with custom adapters for message display
 * - Integrates with Retrofit for API communications
 * - Utilizes Room database for chat history persistence
 *
 * Usage:
 * This activity is typically launched with specific intent extras including screen ID,
 * conversation ID, and user context information. It handles various astrological modules
 * like love, career, marriage, health, education, business, stock market, and legal consultations.
 *
 * @author Android Development Team
 * @see BaseActivity
 * @see ChatListener
 * @see SensorEventListener
 * @see TypeWritterKundali.OnTypingComplete
 * @see StaticQuestionAdapter.OnItemClickListener
 * @see VolleyResponse
 */
public class MiniChatWindow extends BaseActivity implements ChatListener, SensorEventListener, TypeWritterKundali.OnTypingComplete, StaticQuestionAdapter.OnItemClickListener, VolleyResponse {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(MiniChatWindow.class);
    TextView subTitleTV;
    public String source;
    ImageView profileImg,imgViewLocation;
    EditText editTextMessage;
    public RelativeLayout buttonSendLayout;
    Activity activity;
    public RecyclerView messagesListView;
    public View currentView;
    public boolean stopTypeWriter = false;
    public View llChatButton;
    public int currentTypeWriterPosition;
    public TypeWritterKundali currentTypeWriter;
    ImageView ivWaitMsg, ivShareMsg, ivCopyMsg, ivBackNav, ivDeleteChat;
    public NestedScrollView recycleNestedScrollView;
    public LinearLayout llTypeWriterParent;
    Toolbar toolbarChat;
    public int mIndexTypeWriter;
    public boolean isStaticQueSet = false;
    LinearLayoutManager mLayoutManager;
    private FrameLayout questionSuggestionFL;
    private ImageView ivHideSuggestions;
    public RecyclerView questionSuggestionListView;
    private RecyclerView rvStaticQuestions;
    SuggestQuestionAdapter suggestionListAdapter;
    StaticQuestionAdapter staticQuestionAdapter;
    private ArrayList<String> suggestQuestionList; //send this list as per screen ID from Activity in bundle
    public int screenId = 100;
    public String varshphalYear, clnyType, horoscopeYear, lid, panDate, screenName, videoId;
    public int rashiType;
    public boolean isMatching;
    private OnBackPressedCallback onBackPressedCallback;
    private boolean isShowLoginScreen;
    private ConstraintLayout btnUpdatePlan;
    TopupRechargeDialog topupRechargeDialog;

    public TextToSpeech tts;
    public int speakingPosition = -1;
    private Handler handler;
    private Runnable smoothScrollRunnable;
    private int scrollPosition = 0;
    TypeWritterKundali textViewMessage;
    TextView textViewDate, tvLoadOld;
    String greetingMessage;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100101;
    public KundliHistoryDao chatDao;
    public String conversationId = "";
    public int selectedModule = -1, selectedSubModule = -1;
    public String openedFrom = "0";  //0 = default, 1 = chart click
    private boolean isChatForHomeCategory;
    View container;
    ImageView backgroundImage;
    public static String cplace ="";
    public static  String clat ="";
    public static  String clong ="";
    public static  String ctimezone ="";
    public static  String ctimezoneid ="";
    int GET_TIME_ZONE = 0;

    /**
     * Initializes the MiniChatWindow activity and sets up the chat interface.
     *
     * This method performs the following initialization tasks:
     * - Sets up the activity layout and configuration
     * - Initializes language settings and fonts
     * - Sets up the chat database instance
     * - Configures back press handling
     * - Initializes UI components and adapters
     * - Sets up keyboard visibility handling
     * - Configures background themes based on chat category
     * - Initializes logging and analytics tracking
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state, or null if no saved state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mini_chat_window_layout);
        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_AI_CHAT_SCREEN_OPEN, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(this, LANGUAGE_CODE, CGlobalVariables.regular);
        this.setFinishOnTouchOutside(false);
        activity = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        chatDao = KundliHistoryDao.getInstance(this);

        initView();
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (messageAdapter.isLongPressClicked) {
                    messageAdapter.isLongPressClicked = false;
                    updateCopyView(false);
                    if (!messageAdapter.copyMessage.isEmpty())
                        messageAdapter.copyMessage.clear();
                    if (!messageAdapter.messagesToDeleteList.isEmpty())
                        messageAdapter.messagesToDeleteList.clear();
                    if (messageAdapter.listener != null) {
                        messageAdapter.listener.onLongClickEnabled(false);
                    }
                    messageAdapter.notifyDataSetChanged();
                } else {
                    List<ChatMessage> list = messageAdapter.getUpdatedMessageList();
                    AstrosageKundliApplication.kundliChatMessages = list;
                        for (int i = 0; i < list.size(); i++) {
                            if (AstrosageKundliApplication.kundliChatMessages.get(i).chatId() == -1) {
                                AstrosageKundliApplication.kundliChatMessages.remove(i);
                            }
                        }
                    }
                    finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        actionOnKeyBoardVisibility();
        setSuggestionListAdapter();

        if (isChatForHomeCategory) {
            updateBackgroundasPerCategory();
        } else {
            updateBackgroundBasedOnTheme();
        }
        //openPlanPurchaseScreen();
//        getTopupAmounts();
        AstrosageKundliApplication.brokenMessageLogs = "";

        CUtils.saveStringData(activity, "sharableLog", "");
        Button btnShareLogs = findViewById(R.id.btnShareLogs);
        btnShareLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "araj@ojassoft.com",
                        subject = "Kundli AI Logs",
                        body = CUtils.getStringData(MiniChatWindow.this, "sharableLog", "");

                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    //intent.putExtra(Intent.EXTRA_TEXT,AstrosageKundliApplication.notificationIssueLogs.trim());
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(intent);

                } catch (Exception e) {
                    //Log.e("SAN ", "composeNewEmail exception" + e.toString() );
                }
            }
        });
        //CUtils.sendNotificationWithLink(MiniChatWindow.this,getString(R.string.limit_exceeded_subscribe_now),getString(R.string.subscribe_now_to_kundli_ai_your_question_limit_is_exhausted), com.ojassoft.astrosage.utils.CGlobalVariables.AI_KUNDLI_PLUS_DEEP_LINK);

    }

    private void updateBackgroundasPerCategory() {
        try {
            switch (screenId) {
                case 175://LOVE
                    backgroundImage.setBackgroundResource(R.drawable.love_chat_bg);
                    container.setBackgroundResource(R.drawable.love_chat_bg);
                    break;
                case 176://career
                    backgroundImage.setBackgroundResource(R.drawable.career_chat_bg);
                    container.setBackgroundResource(R.drawable.career_chat_bg);
                    break;
                case 177://marriage
                    backgroundImage.setBackgroundResource(R.drawable.marriage_chat_bg);
                    container.setBackgroundResource(R.drawable.marriage_chat_bg);
                    break;
                case 178://health
                    backgroundImage.setBackgroundResource(R.drawable.health_chat_bg);
                    container.setBackgroundResource(R.drawable.health_chat_bg);
                    break;
                case 179://Education
                    backgroundImage.setBackgroundResource(R.drawable.education_chat_bg);
                    container.setBackgroundResource(R.drawable.education_chat_bg);
                    break;
                case 180://Business
                    backgroundImage.setBackgroundResource(R.drawable.business_chat_bg);
                    container.setBackgroundResource(R.drawable.business_chat_bg);
                    break;
                case 181://Stock market
                    backgroundImage.setBackgroundResource(R.drawable.stock_market_chat_bg);
                    container.setBackgroundResource(R.drawable.stock_market_chat_bg);
                    break;
                case 182://legal
                    backgroundImage.setBackgroundResource(R.drawable.legal_chat_bg);
                    container.setBackgroundResource(R.drawable.legal_chat_bg);
                    break;
                default:
                    updateBackgroundBasedOnTheme();

            }
        }catch (Exception e){
            Log.e("error", "updateBackgroundasPerCategory: "+e.getMessage());
        }
    }

    private boolean isUserScrolling = false;

    public void startAutoScroll() {
        if (handler != null && smoothScrollRunnable != null) {
            handler.removeCallbacks(smoothScrollRunnable);
        }
        smoothScrollRunnable = new Runnable() {
            @Override
            public void run() {
                // Get the current position
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) rvStaticQuestions.getLayoutManager();
                if (layoutManager != null) {
                    rvStaticQuestions.scrollBy(4, 0); // Slower smooth scroll horizontally
                }

                handler.postDelayed(this, 40); // Delay increased to make scrolling slower
            }
        };

        handler.postDelayed(smoothScrollRunnable, 40); // Initial delay
    }

    private void setStaticQuestionAdapter() {
        if (suggestQuestionList==null) {
            //Log.e(TAG, "setSuggestionListAdapter:suggestedQuestion = null in static " );
            rvStaticQuestions.setVisibility(View.GONE);
            return;
        }
        rvStaticQuestions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 10) {
                    startAutoScroll();
                } else if (dx < 0) {
                    stopAutoScroll();
                }
            }
        });
        rvStaticQuestions.setVisibility(View.VISIBLE);
        staticQuestionAdapter = new StaticQuestionAdapter(MiniChatWindow.this, suggestQuestionList);
        staticQuestionAdapter.setOnItemClickListener(this); // Set listener

        // GridLayoutManager with 3 rows (spanCount) and horizontal scrolling
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, RecyclerView.HORIZONTAL);
        rvStaticQuestions.setLayoutManager(layoutManager);
        rvStaticQuestions.setAdapter(staticQuestionAdapter);

        // Scroll to the middle of the dataset initially
        if (staticQuestionAdapter.getItemCount() > 0)
            rvStaticQuestions.scrollToPosition(500);

        // Start auto-scroll
        startAutoScroll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CUtils.saveChatDraftMessage(activity, KEY_KUNDLI_AI+screenId, editTextMessage.getText().toString().trim());
        if (handler != null) {
            handler.removeCallbacks(smoothScrollRunnable);
        }
    }

    public void stopAutoScroll() {
        if (handler != null) {
            handler.removeCallbacks(smoothScrollRunnable);
        }
    }

    private void setSuggestionListAdapter() {
        try {
            questionSuggestionListView = findViewById(R.id.questionSuggestionListView);

            if (getIntent() != null) {
                suggestQuestionList = getIntent().getStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY);
            }
            if (suggestQuestionList == null) {
               SuggestedQuestionHelperClass suggestedQuestionHelperClass = new SuggestedQuestionHelperClass(this, com.ojassoft.astrosage.utils.CGlobalVariables.AI_HOME_SCREEN_MODULE);
                suggestedQuestionHelperClass.getSuggestQuestionForModule(new SuggestedQuestionHelperClass.SuggestedQuestionListener() {
                    @Override
                    public void onSuggestedQuestionReady() {
                        suggestQuestionList = suggestedQuestionHelperClass.getSuggestedQuestionsForScreenId(screenId);
                        List<String> listToShow = suggestQuestionList.subList(0, Math.min(7, suggestQuestionList.size()));//need only 7 questions to show as suggestion from list
                        loadSuggestedQuestion(listToShow);
                        setStaticQuestionAdapter();
                    }
                });

            } else {
                List<String> listToShow = suggestQuestionList.subList(0, Math.min(7, suggestQuestionList.size()));//need only 7 questions to show as suggestion from list
//            Log.e("listCheck", "setStaticQuestionAdapter: "+listToShow.size()+listToShow );
//            Log.e("listCheck", "setStaticQuestionAdapter: Full List"+suggestQuestionList.size()+suggestQuestionList );
                loadSuggestedQuestion(listToShow);
                setStaticQuestionAdapter();
            }
        } catch (Exception e) {
            showSnakbar(buttonSendLayout, e.toString());
            Log.d("rvListIssue", "setSuggestionListAdapter e: " + e);
        }
    }

    private void loadSuggestedQuestion(List<String> listToShow ) {
        suggestionListAdapter = new SuggestQuestionAdapter(MiniChatWindow.this, new ArrayList<>(listToShow));
        questionSuggestionListView.setLayoutManager(new LinearLayoutManager(MiniChatWindow.this, LinearLayoutManager.VERTICAL, false));
        questionSuggestionListView.setAdapter(suggestionListAdapter);
        questionSuggestionListView.addItemDecoration(new DividerItemDecoration(MiniChatWindow.this, DividerItemDecoration.VERTICAL));
        questionSuggestionListView.scrollToPosition(listToShow.size() - 1);
        showSuggestions(false);
    }


    public void updateMessageInItem(String updateSting) {
        messageAdapter.updateMessageInAdapter(updateSting);
    }

    /**
     * Initializes and configures all UI components for the chat interface.
     *
     * This method sets up the following UI elements:
     * - Message input field with text change listeners
     * - Send and stop buttons with click handlers
     * - Message list with RecyclerView and adapter
     * - Question suggestion panels and static question lists
     * - Location selection and profile display
     * - Voice input button and speech recognition
     * - Message action buttons (share, copy, delete)
     * - Plan upgrade button and status indicators
     *
     * The method also configures:
     * - Font styling for all text elements
     * - Click listeners and touch event handlers
     * - Text watchers for input validation
     * - Scroll listeners for message navigation
     * - Keyboard visibility detection
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        subTitleTV = findViewById(R.id.typingTextview);
        rvStaticQuestions = findViewById(R.id.rvStaticQuestions);
        ivWaitMsg = findViewById(R.id.ivWaitMsg);
        recycleNestedScrollView = findViewById(R.id.recycleNestedScrollView);
        profileImg = findViewById(R.id.astrologer_profile_pic);
        imgViewLocation = findViewById(R.id.imgViewLocation);
        editTextMessage = findViewById(R.id.editTextMessage);
        FontUtils.changeFont(activity, editTextMessage, CGlobalVariables.FONTS_POPPINS_LIGHT);
        toolbarChat = findViewById(R.id.toolbar_chat);
        messagesListView = findViewById(R.id.listViewMessages);
        ivSend = findViewById(R.id.ivSend);
        ivStop = findViewById(R.id.ivStop);
        ivShareMsg = findViewById(R.id.iv_share_chat);
        ivCopyMsg = findViewById(R.id.iv_copy_chat);
        ivDeleteChat = findViewById(R.id.iv_delete_msg);
        ivBackNav = findViewById(R.id.img_back_nav);
        questionSuggestionFL = findViewById(R.id.questionSuggestionFL);
        ivHideSuggestions = findViewById(R.id.ivHideSuggestions);
        btnUpdatePlan = findViewById(R.id.btnUpdatePlan);
        textViewMessage = findViewById(R.id.textViewKundaliMsg);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        backgroundImage = findViewById(R.id.iv_back_ground);
        container = findViewById(R.id.containerLayout);
        LinearLayout editTextLayout = findViewById(R.id.relSendMessage);
        TextView tvStatusMessage = findViewById(R.id.warning_message);
        TextView tvTagLine = findViewById(R.id.tv_tag_line);
        tvLoadOld = findViewById(R.id.tv_load_old_chat);
        FontUtils.changeFont(activity, tvStatusMessage, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(activity, tvTagLine, CGlobalVariables.FONTS_POPPINS_LIGHT);

        if (com.ojassoft.astrosage.utils.CUtils.isDhruvPlan(MiniChatWindow.this) || com.ojassoft.astrosage.utils.CUtils.getUserPurchasedPlanFromPreference(this) == com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            btnUpdatePlan.setVisibility(View.GONE);
        }
        buttonSendLayout = findViewById(R.id.buttonSend);
        Intent intent = getIntent();
        boolean isFromHistory = false;
        if (intent != null) {
            screenId = intent.getIntExtra(CURRENT_SCREEN_ID_KEY, 100);
            varshphalYear = intent.getStringExtra(CURRENT_VARSHPHAL_YEAR_KEY);
            clnyType = intent.getStringExtra(NUMERO_CLNY_TYPE_KEY);
            isMatching = intent.getBooleanExtra(MATCHING_KEY, false);
            isChatForHomeCategory = intent.getBooleanExtra(KEY_IS_CHAT_FOR_HOME_CATEGORY, false);
            rashiType = intent.getIntExtra(RASHI_HOROSCOPE_KEY, -1);
            horoscopeYear = intent.getStringExtra(HOROSCOPE_YEAR_KEY);
            lid = intent.getStringExtra(PANCHANG_LID_KEY);
            panDate = intent.getStringExtra(PANCHANG_DATE_KEY);
            screenName = intent.getStringExtra(KEY_SCREEN_NAME);
            videoId = intent.getStringExtra(KEY_VIDEO_ID);
            selectedModule = intent.getIntExtra(KEY_MODULE_ID, -1);
            selectedSubModule = intent.getIntExtra(KEY_SUB_MODULE_ID, -1);
            conversationId = intent.getStringExtra(KEY_CONVERSATION_ID);
            if(!TextUtils.isEmpty(conversationId)&&conversationId.contains("#")){
                conversationId = conversationId.split("#")[0];
            }
            source = intent.getStringExtra(SOURCE_OF_SCREEN);
            Log.e("KundliChatAi", "Mini Conversation Id = " + conversationId);
            if (varshphalYear == null) {
                varshphalYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            }
            isFromHistory = intent.getBooleanExtra(KEY_FROM_HISTORY, false);
            if (isFromHistory) {
                editTextLayout.setVisibility(View.INVISIBLE);
                btnUpdatePlan.setVisibility(View.GONE);
                imgViewLocation.setVisibility(View.GONE);
                tvLoadOld.setVisibility(View.GONE);
            }
        }
        setSupportActionBar(toolbarChat);
        if (isChatForHomeCategory) {
            editTextMessage.setHint(getResources().getString(R.string.ask_about, screenName));
        } else {
            editTextMessage.setHint(getString(R.string.ask_anything_to_kundli, screenName));
        }
        editTextMessage.setText(CUtils.getChatDraftMessage(activity, KEY_KUNDLI_AI+screenId));
        messagesListView.setOnTouchListener((v, event) -> {
            scrollWhileTypeWriter = false;
            showSuggestions(false);
            return false;
        });
       // Log.d("MiniChatWindow", "Profile name : " + CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getName());

        // Attempt to retrieve the user's current place information for chat context.
// Priority order:
// 1. Use BeanPlace from getBeanPalce() if available (likely user-selected or most accurate).
// 2. Fallback to getCurrentPalce() (possibly last known or default).
// 3. If both are unavailable, try to get latitude/longitude from shared preferences and fetch timezone from server.

/**
 * Initializes global location variables for the chat window.
 *
 * The method attempts to determine the user's current place using the following order:
 * 1. If a BeanPlace object is available from CUtils.getBeanPalce(this), use its details.
 * 2. Otherwise, try to get the current place from CUtils.getCurrentPalce(this).
 * 3. If neither is available, attempt to retrieve latitude and longitude from shared preferences,
 *    and fetch timezone information from the server if coordinates are present.
 */
        if (com.ojassoft.astrosage.utils.CUtils.getBeanPalce(this) != null){
            // Use the most accurate or user-selected place
            BeanPlace currentPlace = com.ojassoft.astrosage.utils.CUtils.getBeanPalce(this);
            cplace = currentPlace.getCityName();
            clong = currentPlace.getLongitude();
            clat = currentPlace.getLatitude();
            ctimezone = currentPlace.getTimeZone();
            ctimezoneid = currentPlace.getTimeZoneString();
        }else {
            // Fallback to last known or default place
            BeanPlace currentPlace = com.ojassoft.astrosage.utils.CUtils.getCurrentPalce(this);
            if(currentPlace!=null) {
                cplace = currentPlace.getCityName();
                clong = currentPlace.getLongitude();
                clat = currentPlace.getLatitude();
                ctimezone = currentPlace.getTimeZone();
                ctimezoneid = currentPlace.getTimeZoneString();
            } else {
                // If no place info is available, try to get coordinates from preferences and fetch timezone
                String latitude = com.ojassoft.astrosage.utils.CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.KEY_LATITUDE, "");
                String longitude = com.ojassoft.astrosage.utils.CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.KEY_LONGITUDE, "");
                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                    // Fetch timezone and location details from server using coordinates
                    getTimeZoneFromServer(latitude, longitude);
                }
            }
           // Log.d("MiniChatWindow", "Current place is null, fetching from server");
        }

        // Location icon click handler - Opens place search dialog
        // When user clicks on location icon, it gets the current place from horoscope info
        // and opens the place search activity to allow selecting a new location
        imgViewLocation.setOnClickListener(view -> {
            openSearchPlace(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getPlace());

        });
        messageAdapter = new KundliAIChatMsgAdapter(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesListView.setLayoutManager(layoutManager);
        messagesListView.setAdapter(messageAdapter);
        Glide.with(ivWaitMsg).load(R.drawable.typing).into(ivWaitMsg);
        if (TextUtils.isEmpty(AstrosageKundliApplication.KUNDLI_CHAT_CONVERSATION_ID) || !AstrosageKundliApplication.KUNDLI_CHAT_CONVERSATION_ID.equalsIgnoreCase(conversationId)) {
            if (AstrosageKundliApplication.kundliChatMessages != null)
                AstrosageKundliApplication.kundliChatMessages.clear();
        }
        AstrosageKundliApplication.KUNDLI_CHAT_CONVERSATION_ID = conversationId;
        if (AstrosageKundliApplication.kundliChatMessages != null && !AstrosageKundliApplication.kundliChatMessages.isEmpty()) {
            messageAdapter.setMessages(AstrosageKundliApplication.kundliChatMessages);
            if (screenId != BASIC_LAGNA_SCREEN_ID) {
                replaceGreeting(getString(R.string.intro_message_others, screenName));
            }
        } else {
            if (screenId != BASIC_LAGNA_SCREEN_ID) {
                showGreetingMessage();
            }
        }
        if (chatDao.getChatHistoryCount() > 0 && !isFromHistory) {
            int itemCount = messageAdapter.getItemCount() / 2;
            if (chatDao.getChatHistoryCount() > itemCount) {
                tvLoadOld.setVisibility(View.VISIBLE);
            } else {
                tvLoadOld.setVisibility(View.GONE);
            }
        }
        if (isFromHistory) {
            setOnScrollListener();
        }
        ivShareMsg.setOnClickListener(view -> toOpenShareDialog());
        ivHideSuggestions.setOnClickListener(view -> showSuggestions(false));
        ivCopyMsg.setOnClickListener(view -> {
            //TODO
            copySelectedMessages();
        });

        ivDeleteChat.setOnClickListener(v -> deleteChatMessages());
        FontUtils.changeFont(activity, tvLoadOld, CGlobalVariables.FONTS_POPPINS_LIGHT);

        tvLoadOld.setOnClickListener((v) -> {
            stopTypeWriter = true;
            tvLoadOld.setVisibility(View.GONE);
            loadOldChat();
        });

        buttonSendLayout.setOnClickListener(view1 -> sendButtonClick());
        recycleNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isAtBottom(recycleNestedScrollView)) {
                    scrollWhileTypeWriter = true;
                }
            }
        });

        recycleNestedScrollView.setOnTouchListener((v, event) -> {
            scrollWhileTypeWriter = false;
            showSuggestions(false);
            return false;
        });

        ivBackNav.setOnClickListener(view1 -> onBackPressedCallback.handleOnBackPressed());
        btnUpdatePlan.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CLOUD_CHAT_UPGRADE_PLAN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            isTyping = true;
       //     Log.e("source_check", "initView: source-> "+source );
            isPlanPurchaseScreenOpen = true;
            CUtils.openPurchaseDhruvPlanActivity(MiniChatWindow.this, true, false, source+"_"+CGlobalVariables.AI_CLOUD_CHAT_UPGRADE_BTN_CLICK,false,true);

        });


        editTextMessage.setOnEditorActionListener((v, actionId, event) -> false);
        editTextMessage.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (editTextMessage.getText().toString().isEmpty() && questionSuggestionFL.getVisibility() == View.GONE)
                    showSuggestions(true);
                else if (questionSuggestionFL.getVisibility() == View.VISIBLE)
                    showSuggestions(false);
            }
            return false;
        });



        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Map<Integer, String> map = new HashMap<>();
                map.put(screenId, s.toString());
                if (!s.toString().trim().isEmpty()) {
                    isStaticQueSet = false;
                    questionSuggestionFL.setVisibility(View.GONE);
                } else {
                    if (!isStaticQueSet)
                        showSuggestions(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RelativeLayout micButton = findViewById(R.id.buttonSpeechToText);

        micButton.setOnClickListener(view -> {

            if (tts.isSpeaking()) {
                stopSpeaking();
            }
            openVoiceToTextDialog(LANGUAGE_CODE);
        });
        handler = new Handler();
    }


    /**
     * Opens the Place Search Activity to allow user to search and select a new location.
     *
     * This method launches the place search interface to enable users to:
     * - Search for cities and locations worldwide
     * - Select precise coordinates for astrological calculations
     * - Update their birth place or current location
     * - Maintain location context for accurate horoscope generation
     *
     * The method preserves the current location data while searching to ensure
     * no data loss during the location selection process.
     *
     * @param beanPlace The current place object containing location details like city, lat, long etc.
     *                  This is passed to preserve current location data while searching
     *
     * @see ActPlaceSearch
     * @see BaseInputActivity#SUB_ACTIVITY_PLACE_SEARCH
     */
    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(MiniChatWindow.this, ActPlaceSearch.class);
        intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_TYPE_KEY, 0);
        intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, BaseInputActivity.SUB_ACTIVITY_PLACE_SEARCH);
    }

    /**
     * Fetches timezone information from GeoNames API based on latitude and longitude.
     *
     * This method uses the geonames.org web service to retrieve comprehensive
     * timezone and location data for accurate astrological calculations.
     *
     * The API call returns XML response containing:
     * - Timezone ID: Standard timezone identifier (e.g., "Asia/Kolkata")
     * - DST Offset: Daylight Saving Time offset in hours
     * - Raw Offset: Base timezone offset from GMT
     * - GMT Offset: Current offset from Greenwich Mean Time
     * - Country Name: Full country name for the location
     *
     * The method uses a random username for API access and handles the response
     * through the VolleyResponse interface.
     *
     * @param lat Latitude of the location to get timezone for
     * @param lon Longitude of the location to get timezone for
     *
     * @see VolleyResponse#onResponse(String, int)
     */
    private void getTimeZoneFromServer(String lat, String lon) {
        String url = "https://secure.geonames.org/timezone?lat="
                + String.valueOf(lat) + "&lng=" + String.valueOf(lon)
                + "&username="+ com.ojassoft.astrosage.utils.CUtils.getRandomUsername();
        LibCUtils.vollyGetRequest(activity, MiniChatWindow.this, url, GET_TIME_ZONE);
    }

    // This method is called when the Volley response is received
    @Override
    public void onResponse(String response, int method) {
       // Check if activity is still active to prevent crashes
        if (activity != null) {
            // Validate response is not empty
            if (!TextUtils.isEmpty(response)) {
                showTimeZone(response);
            } else {
                Log.e("MiniChatWindow", "API response is null or empty");
                // Show error to user if needed
            }
        }
    }

    /**
     * Parses timezone XML response and extracts location details
     * Updates global location variables with the parsed values
     *
     * @param timezonevalue XML string containing timezone and location data
     *
     * XML Format expected:
     * <timezone>
     *   <dstOffset>...</dstOffset>
     *   <timezoneId>...</timezoneId>
     *   <lat>...</lat>
     *   <lng>...</lng>
     *   <countryName>...</countryName>
     * </timezone>
     */
    private void showTimeZone(String timezonevalue) {
        try {
            // Extract DST (Daylight Saving Time) offset
            ctimezone = timezonevalue.substring(
                    timezonevalue.indexOf("<dstOffset>")
                            + "<dstOffset>".length(),
                    timezonevalue.indexOf("</dstOffset>"));
            ctimezone = ctimezone.trim();

            // Extract timezone identifier (e.g. "Asia/Kolkata")
            ctimezoneid = timezonevalue.substring(
                    timezonevalue.indexOf("<timezoneId>")
                            + "<timezoneId>".length(),
                    timezonevalue.indexOf("</timezoneId>"));
            ctimezoneid = ctimezoneid.trim();

            // Extract latitude coordinate
            clat = timezonevalue.substring(
                    timezonevalue.indexOf("<lat>") + "<lat>".length(),
                    timezonevalue.indexOf("</lat>")
            ).trim();

            // Extract longitude coordinate
            clong = timezonevalue.substring(
                    timezonevalue.indexOf("<lng>") + "<lng>".length(),
                    timezonevalue.indexOf("</lng>")
            ).trim();

            // Extract country/place name
            cplace = timezonevalue.substring(
                    timezonevalue.indexOf("<countryName>") + "<countryName>".length(),
                    timezonevalue.indexOf("</countryName>")
            ).trim();

            // Create and save a BeanPlace object with the current location and timezone details.
            /**
             * Creates a BeanPlace object with the current location and timezone information,
             * then saves it as the current place in shared preferences or persistent storage.
             *
             * @see BeanPlace
             * @see com.ojassoft.astrosage.utils.CUtils#saveCurrentPalce(Context, BeanPlace)
             */
            BeanPlace place = new BeanPlace();
            place.setCityName(cplace);           // Set the city name
            place.setLatitude(clat);             // Set the latitude
            place.setLongitude(clong);           // Set the longitude
            place.setTimeZoneString(ctimezoneid);// Set the timezone ID (e.g., "Asia/Kolkata")
            place.setTimeZone(ctimezone);        // Set the timezone offset (e.g., "+5.5")
            com.ojassoft.astrosage.utils.CUtils.saveCurrentPalce(MiniChatWindow.this, place); // Save the place

        } catch (Exception e) {
            // Silently handle any parsing errors
            // Keeps existing location data unchanged if parsing fails
        }
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void copySelectedMessages() {
        String textToCopy = createTextFromSeletedMsg();
        com.ojassoft.astrosage.utils.CUtils.copyTextToClipBoard(removeHtmlTags(textToCopy), this);
        clearCopyList();
    }

    public void clearCopyList() {
        messageAdapter.getCopyMessageList().clear();
        updateCopyView(false);
        messageAdapter.notifyDataSetChanged();
        messageAdapter.isLongPressClicked = false;
    }

    public void showSuggestions(boolean isToShow) {
        questionSuggestionFL.setVisibility(isToShow ? View.VISIBLE : View.GONE);
    }

    private boolean isAtBottom(NestedScrollView scrollView) {
        View contentView = scrollView.getChildAt(0);
        return contentView.getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight();
    }

    public void showHideTypingIndicator(int visibility) {
        if (visibility == View.GONE) {
            buttonSendLayout.setEnabled(true);
        }
        ivWaitMsg.setVisibility(visibility);
        scrollMyListViewToBottom();
    }

    public void scrollMyListViewToBottom() {
        try {
            if (messagesListView != null) {
                messagesListView.post(() -> {
                    final float y = messagesListView.getBottom();
                    if (recycleNestedScrollView != null) {
                        recycleNestedScrollView.fling(0);
                        recycleNestedScrollView.smoothScrollTo(0, (int) y);
                    }
                });
            }
        } catch (Exception e) {
            //
        }
    }

    boolean loading = false;

    private void setOnScrollListener() {
        recycleNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                try {
                    int dy = scrollY - oldScrollY;
                    if (dy < 0 && scrollY < 700) {
                        int chatCount = AstrosageKundliApplication.kundliChatMessages.size();
                        if (chatCount % 2 != 0) {
                            chatCount++;
                        }
                        int itemCount = chatCount / 2;
                        if (!loading) {
                            loading = true;
                            ArrayList<ChatMessage> list = chatDao.getMessagePaged(conversationId, itemCount, 5);
                            if (list != null && !list.isEmpty()) {
                                // Sort messages by timestamp before adding
                                Collections.sort(list, (m1, m2) -> m1.getDateCreated().compareTo(m2.getDateCreated()));
                                messageAdapter.appendMessageList(list);
                                AstrosageKundliApplication.kundliChatMessages.addAll(0, list);
                                // Sort the complete list after adding new messages
                                Collections.sort(AstrosageKundliApplication.kundliChatMessages, 
                                    (m1, m2) -> m1.getDateCreated().compareTo(m2.getDateCreated()));
                            }
                            loading = false;
                        }
                    }
                } catch (Exception ignore) {
                }
            }
        });
    }

    public static String langCode = "";

    /**
     * Identifies the language of the input message and optionally sends it to the AI.
     *
     * This method uses Google ML Kit's Language Identification API to detect the language
     * of the user's message. It supports the following languages:
     * - Hindi (hi): Native Hindi script
     * - English (en): English language
     * - Romanized Hindi (hi:Latn): Hindi written in Roman script
     * - Marathi (mr): Marathi language
     *
     * The method handles edge cases such as:
     * - Undetermined language detection
     * - Fallback to default language based on app settings
     * - Roman script detection for Hindi text
     *
     * @param messageText The text message to analyze for language detection
     * @param isProceedToSendMessage If true, sends the message to AI after language identification
     *
     * @see LanguageIdentifier
     * @see LanguageIdentificationOptions
     */
    public void identifyLanguage(String messageText, boolean isProceedToSendMessage) {
        if (isProceedToSendMessage) {
            disableStopButton();
            CUtils.hideMyKeyboard(this);
            showHideTypingIndicator(View.VISIBLE);
        }
        LanguageIdentificationOptions identifierOptions = new LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.8f).build();
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient(identifierOptions);
        languageIdentifier.identifyPossibleLanguages(messageText).addOnSuccessListener(identifiedLanguages -> {
        });

        languageIdentifier.identifyLanguage(messageText).addOnSuccessListener(languageCode -> {
            try {
                textLanguage = languageCode;
                if (languageCode.equals("hi") || languageCode.equals("en") || languageCode.equals("mr") || languageCode.contains("Latn"))
                    langCode = (languageCode.equals("hi") || languageCode.equals("mr")) ? "hi" : (languageCode.contains("Latn")) ? "hi:Latn" : languageCode;
                else if (languageCode.equals("und")) {
                    if (checkRomanLang(messageText)) {
                        langCode = "hi:Latn";
                    } else if (isHindiLanguage(messageText.charAt(0))) {
                        langCode = "hi";
                    } else {
                        langCode = "en";
                    }
                } else {
                    if (LANGUAGE_CODE == CGlobalVariables.HINDI)
                        langCode = "hi";
                    else langCode = "en";
                }

                if (isProceedToSendMessage) {

                    sendMessage(messageText);
                }
            } catch (Exception e) {
                //
            }
        }).addOnFailureListener(e -> {
            try {
                if (checkRomanLang(messageText)) {
                    langCode = "hi:Latn";
                } else if (isHindiLanguage(messageText.charAt(0))) {
                    langCode = "hi";
                } else {
                    langCode = "en";
                }
                if (isProceedToSendMessage) {
                    sendMessage(messageText);
                }
            } catch (Exception e1) {
                //
            }
        });


    }

    public int chatId;

    /**
     * Handles the send button click event and processes user message input.
     *
     * This method performs the following actions:
     * - Checks if the stop button is visible (indicating ongoing AI response)
     * - Validates that the message text is not empty
     * - Triggers language identification and message sending
     * - Handles typewriter effect interruption if needed
     *
     * The method ensures proper state management during message sending
     * and prevents duplicate message submissions while AI is responding.
     */
    public void sendButtonClick() {
        if (ivStop.getVisibility() == View.VISIBLE) {
            stopTypeWriter = true;
        } else {
            String messageText = editTextMessage.getText().toString();
            if (messageText.isEmpty()) {
                return;
            }
            identifyLanguage(messageText.trim(), true);
        }
    }

    /**
     * Sends a user message to the AI and updates the chat interface.
     *
     * This method performs the following operations:
     * - Generates a unique chat ID for the message
     * - Adds the user message to the chat adapter
     * - Updates Firebase topic subscriptions for notifications
     * - Clears the input field after sending
     * - Initiates AI response generation
     *
     * The method also handles:
     * - Static question state management
     * - Firebase topic management for push notifications
     * - Input field cleanup and focus management
     *
     * @param message The text message to send to the AI
     */
    private void sendMessage(String message) {

        isStaticQueSet = true;
        Random numRandom = new Random();
        chatId = numRandom.nextInt(999) + 1;
        addMessageToAdapter(message, CGlobalVariables.USER, chatId, false);
        try {
            String oldTopic = CUtils.getStringData(activity, CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_KEY, "");
            String newTopic = CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_TOPIC + "3";
            if (TextUtils.isEmpty(oldTopic)) {
                CUtils.saveStringData(activity, CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_KEY, newTopic);
                CUtils.subscribeTopics("", newTopic, activity);
            } else if (!oldTopic.equals(newTopic)) {
                CUtils.unSubscribeFollowTopic(activity, oldTopic);
                CUtils.saveStringData(activity, CGlobalVariables.AI_ASTROLOGER_LAST_CHAT_KEY, newTopic);
                CUtils.subscribeTopics("", newTopic, activity);
            }
        } catch (Exception e) {
            //
        }

        sendPromptToAI(message);
        editTextMessage.setText("");
    }

    GetAnswerFromServerForKundali getAnswerFromServer;

    /**
     * Sends a user prompt to the AI service for response generation.
     *
     * This method handles the communication with the AI backend service:
     * - Checks internet connectivity before making the request
     * - Creates or reuses the GetAnswerFromServerForKundali instance
     * - Initiates the AI query processing
     * - Handles network errors and connectivity issues
     *
     * Error handling includes:
     * - Network connectivity validation
     * - User-friendly error messages for offline scenarios
     * - Proper error state management in the UI
     *
     * @param messageText The user's question or prompt to send to the AI
     *
     * @see GetAnswerFromServerForKundali
     */
    public void sendPromptToAI(String messageText) {
        if (CUtils.isConnectedWithInternet(activity)) {
            try {
                if (getAnswerFromServer == null) {
                    getAnswerFromServer = new GetAnswerFromServerForKundali(activity); //here have to create new method due to tightly coupled code in GetAnswerFromServer with AIChaTWindowActivity
                }
                getAnswerFromServer.getQueryAnswer(activity, messageText);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            responseErrorHandling(false);
            Toast.makeText(activity, getResources().getString(R.string.no_internet_tap_to_retry), Toast.LENGTH_SHORT).show();
        }
    }

    public void responseErrorHandling(boolean showError) {
        runOnUiThread(() -> {
            showHideTypingIndicator(View.GONE);
            if (showError) {
                messageAdapter.markLastQuestionError();
            }
        });
    }

    public void showAlertForDhruvPurchase() {
        runOnUiThread(() -> {
            showHideTypingIndicator(View.GONE);
            if (this.speakingPosition != -1) stopSpeaking();
            int planId = com.ojassoft.astrosage.utils.CUtils.getUserPurchasedPlanFromPreference(MiniChatWindow.this);
            if (planId == com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_PLAN_ID) {
                //startPaidChatWithAlert();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CLOUD_CHAT_LIMIT_UPGRADE_PLAN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.utils.CUtils.createSession(this, AI_CLOUD_CHAT_LIMIT_UPGRADE_PLAN_PID);
                isTyping = true;
                isPlanPurchaseScreenOpen = true;
                String errorMsg = getResources().getString(R.string.error_cloud_chat_limit_reached_open_plan_screen);
                addMessageToAdapter(errorMsg, CGlobalVariables.CHAT_STATUS_WARNING, -2, false);
                CUtils.sendNotificationWithLink(MiniChatWindow.this, getString(R.string.limit_exceeded_subscribe_now), getString(R.string.subscribe_now_to_kundli_ai_your_question_limit_is_exhausted), com.ojassoft.astrosage.utils.CGlobalVariables.AI_KUNDLI_PLUS_DEEP_LINK);
//                startActivity(new Intent(MiniChatWindow.this, LimitExceedDhruvPurchaseActivity.class));
                CUtils.openPurchaseDhruvPlanActivity(MiniChatWindow.this, true, true, source,false,true);
            } else if (com.ojassoft.astrosage.utils.CUtils.isKundliAIPlusPlan(this)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CLOUD_CHAT_LIMIT_CROSSED_PAID_USER, CGlobalVariables.SHOW_DIALOG_EVENT, "");
                startActivity(new Intent(MiniChatWindow.this, LimitExceedDhruvPurchaseActivity.class));
            } else {
                //getTopupAmounts();
                //show error dialog here
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CLOUD_CHAT_LIMIT_CROSSED_PAID_USER, CGlobalVariables.SHOW_DIALOG_EVENT, "");
                CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.error_cloud_chat_limit_reached), activity);
            }

        });
    }

    /**
     * Initiates text-to-speech playback for AI responses.
     *
     * This method processes AI responses for voice playback:
     * - Splits long responses into manageable chunks for better speech synthesis
     * - Adds the first chunk to the chat interface immediately
     * - Generates a unique answer ID if not provided
     * - Prepares the response for typewriter effect display
     *
     * The method ensures smooth integration between text display and voice playback,
     * providing a multi-modal user experience.
     *
     * @param result The AI response text to be spoken
     * @param answerId Unique identifier for the response, generated if null
     */
    public void speakOut(String result, String answerId) {
        showHideTypingIndicator(View.GONE);
        Random numRandom = new Random();
        showHideTypingIndicator(View.GONE);
        if (TextUtils.isEmpty(answerId)) {
            answerId = String.valueOf(numRandom.nextInt(999) + 1);
        }
        //  Log.d("KundliAIChatTesting", "speak: " + result);
        messageChunks = split(result);
        //  Log.d("KundliAIChatTesting", "chunk: " + messageChunks);
        addMessageToAdapter(messageChunks.get(0), CGlobalVariables.ASTROLOGER, Long.parseLong(answerId), true);
    }

    public KundliAIChatMsgAdapter messageAdapter;

    /**
     * Adds a new message to the chat adapter and updates the UI.
     *
     * This method handles message addition with the following features:
     * - Creates a new Message object with current timestamp
     * - Sets message properties (author, body, seen status, chat ID)
     * - Adds the message to the RecyclerView adapter
     * - Scrolls to the bottom to show the latest message
     * - Updates the global message list for persistence
     *
     * The method distinguishes between user messages and AI responses,
     * ensuring proper categorization and storage.
     *
     * @param messageText The text content of the message
     * @param messageFrom The sender of the message (USER or ASTROLOGER)
     * @param chatId Unique identifier for the message
     * @param animate Whether to animate the message addition
     */
    public void addMessageToAdapter(String messageText, String messageFrom, long chatId, boolean animate) {
        Message message = new Message();
        message.setAuthor(messageFrom);
        message.setDateCreated(CUtils.getDbNowDateAndTime());
        message.setMessageBody(messageText);
        message.setSeen(animate);
        message.setChatId(chatId);
        //Log.d("KundliAIChatTesting", "messageText: " + messageText);
        messageAdapter.addMessage(message);
        scrollMyListViewToBottom();
        if (messageFrom.equals(USER) || (messageFrom.equals(ASTROLOGER) && chatId == -1)) {
            if (AstrosageKundliApplication.kundliChatMessages == null) {
                AstrosageKundliApplication.kundliChatMessages = new ArrayList<>();
            }
            AstrosageKundliApplication.kundliChatMessages.add(new UserMessage(message));
        }
    }

    private ImageView ivStop, ivSend;
    public String finalJSONData;
    public boolean scrollWhileTypeWriter = false;

    private Handler stopHandler;
    private Runnable stopRunnable;

    public void toggleSendStopButtonVisibility(boolean visibility) {
        if (visibility) {

            if (stopHandler != null) {
                stopHandler.removeCallbacks(stopRunnable);
                stopHandler = null;
            }
//            enableSendButton();
            ivStop.setVisibility(View.GONE);
            ivSend.setVisibility(View.VISIBLE);
            if (llChatButton != null)
                llChatButton.setVisibility(View.VISIBLE);
//            if (getAnswerFromServer != null) {
//                getAnswerFromServer.parseJsonAtOnce(finalJSONData, true, null);
//            }
            finalJSONData = "";
            if (AstrosageKundliApplication.kundliChatMessages != null) {
                messageAdapter.refreshMessageList(AstrosageKundliApplication.kundliChatMessages);
            }
//            Log.e("greetingCheck", "toggleSendStopButtonVisibility: history loading" );
            messageAdapter.notifyDataSetChanged();
            if (messageChunks != null) {
                messageChunks.clear();
            }
            //messageAdapter.refreshSingleItem(currentTypeWriterPosition);
            if (scrollWhileTypeWriter) {
                scrollMyListViewToBottom();
            }
        } else {
            ivSend.setVisibility(View.GONE);
            ivStop.setVisibility(View.VISIBLE);
            //buttonSendLayout.setEnabled(true);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.anim_slide_out_down);
    }

    boolean isTyping = false;
    public boolean isPlanPurchaseScreenOpen=false;

    /**
     * Handles activity resumption and performs necessary state updates.
     *
     * This method manages the activity lifecycle when returning to the foreground:
     * - Applies slide-up animation for smooth transition
     * - Processes any pending questions from intent extras
     * - Initializes text-to-speech engine with proper configuration
     * - Handles plan purchase screen state management
     * - Updates UI elements based on current state
     *
     * The method ensures proper state restoration and handles edge cases
     * such as plan upgrades and pending user interactions.
     */
    @Override
    public void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.anim_slide_up, 0);

        Intent intent = getIntent();
        if (intent != null) {
            String question = intent.getStringExtra(LAGANA_CHART_QUESTION);
            if (!TextUtils.isEmpty(question)) {
                openedFrom = "1";
                isTyping = true;
                identifyLanguage(question, true);
                intent.removeExtra(LAGANA_CHART_QUESTION);
            }
        }

        /*if (AstrosageKundliApplication.kundliChatMessages == null || AstrosageKundliApplication.kundliChatMessages.isEmpty()) {
            if (isTyping){
                showStaticGreetingMessage();
            }else{
                showTypingGreetingMessage();
            }
        }*/
        rvStaticQuestions.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() ->
                CUtils.hideMyKeyboard(MiniChatWindow.this), 500);
//        textLanguage = CUtils.getLanguageForSpeechRecognize(CUtils.getIntData(this, CGlobalVariables.app_language_key, CGlobalVariables.ENGLISH));
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                setUtteranceProgressListener();
            } else {
                //Log.e("TTS", "Initialization Failed!");
            }
        });

        /*if(isShowLoginScreen){
            isShowLoginScreen = false;
            getTopupAmounts();
        }*/
        try {
            int purchasePlanId = com.ojassoft.astrosage.utils.CUtils.getUserPurchasedPlanFromPreference(this);
            if (isPlanPurchaseScreenOpen && (purchasePlanId != com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_PLAN_ID)) {
                isPlanPurchaseScreenOpen = false;
                btnUpdatePlan.setVisibility(View.GONE);
                messageAdapter.removeMessageByChatId(-2);
                enableStopButton();
            }
            tvLoadOld.setEnabled(true);
        }catch (Exception e){
            //
        }

    }


    /*@Override
    public void onBackPressed() {
        if (messageAdapter.isLongPressClicked) {
            updateCopyView(false);
            messageAdapter.isLongPressClicked = false;
        } else {
            AstrosageKundliApplication.kundliChatMessages = messageAdapter.getUpdatedMessageList();
            super.onBackPressed();
        }
    }*/


    public void toOpenShareDialog() {
        try {
            String textToShare = createTextFromSeletedMsg();
            CUtils.shareWithFriends(activity, textToShare);
            clearCopyList();
        } catch (Exception e) {
            //Log.e("SAN ", " CMW toOpenShareDialog() Exp2=" + e);
        }
    }

    private String createTextFromSeletedMsg() {
        try {
            String textToShare = "";
            String author = "";
            List<ChatMessage> messageList = messageAdapter.getMessageList();
            List<Integer> selectedPos = messageAdapter.getCopyMessageList();
            if (!messageList.isEmpty()) {
                for (int i = 0; i < selectedPos.size(); i++) {
                    int position = selectedPos.get(i);
                    if (messageList.get(position) instanceof LeftStatusMessage
                            || messageList.get(position) instanceof WelcomeStatusMessage
                            || messageList.get(position) instanceof ContactNumberRestrictMessage) {
                        continue;
                    } else {

                        ChatMessage cmi = messageList.get(position);
                        author = cmi.getAuthor();
                        textToShare = textToShare + "[" + cmi.getDateCreated() + "] " + author + ": " + removeHtmlTags(cmi.getMessageBody(activity)) + "\n";
                    }

                }

            }
            return textToShare;
        } catch (Exception e) {
            //
        }
        return "";
    }


    public void updateCopyView(boolean stage) {
        if (stage) {
            ivShareMsg.setVisibility(View.VISIBLE);
            ivCopyMsg.setVisibility(View.VISIBLE);
            btnUpdatePlan.setVisibility(View.GONE);
            imgViewLocation.setVisibility(View.GONE);
            ivDeleteChat.setVisibility(View.VISIBLE);
        } else {
            ivShareMsg.setVisibility(View.GONE);
            ivCopyMsg.setVisibility(View.GONE);
            imgViewLocation.setVisibility(View.VISIBLE);
            if (com.ojassoft.astrosage.utils.CUtils.isDhruvPlan(MiniChatWindow.this) || com.ojassoft.astrosage.utils.CUtils.getUserPurchasedPlanFromPreference(this) == com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                btnUpdatePlan.setVisibility(View.GONE);
            }else {
                btnUpdatePlan.setVisibility(View.VISIBLE);
            }
            ivDeleteChat.setVisibility(View.GONE);
        }
    }

    boolean isKeyboardShowing = false;

    private void actionOnKeyBoardVisibility() {
        RelativeLayout contentView = findViewById(R.id.containerLayout);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            contentView.getWindowVisibleDisplayFrame(r);
            int screenHeight = contentView.getRootView().getHeight();

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            int keypadHeight = screenHeight - r.bottom;

//                        Log.d("MyTag", "keypadHeight = " + keypadHeight);

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true;
                    final float y = messagesListView.getBottom();
                    recycleNestedScrollView.fling(0);
                    recycleNestedScrollView.smoothScrollTo(0, (int) y);

                    //  Log.d("MyTag", "isKeyboardShowing: " + isKeyboardShowing);
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false;
                    //  Log.d("MyTag", "isKeyboardShowing: " + isKeyboardShowing);
                }
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float valueX = sensorEvent.values[0];
        float valueY = sensorEvent.values[1];

        ObjectAnimator animation = ObjectAnimator.ofFloat(backgroundImage, "translationX", -valueX * 4);
        animation.setDuration(100);
        animation.start();
        ObjectAnimator animation1 = ObjectAnimator.ofFloat(backgroundImage, "translationY", -valueY * 3);
        animation1.setDuration(100);
        animation1.start();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onChatFailure() {

    }

    @Override
    public void onLongClickEnabled(boolean isEnabled) {
        updateCopyView(isEnabled);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isChatForHomeCategory) {
            updateBackgroundasPerCategory();
        } else {
            updateBackgroundBasedOnTheme();
        }// Call the method when theme changes
//        updateStatusBarColor();
    }


    // Method to update background based on the theme
    private void updateBackgroundBasedOnTheme() {

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                backgroundImage.setBackgroundResource(R.drawable.chat_bg); // Setting background to null for light theme
                container.setBackgroundResource(R.drawable.chat_bg); // Setting background to null for light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                backgroundImage.setBackgroundResource(R.drawable.chat_bg_dark); // Setting background to null for light theme
                container.setBackgroundResource(R.drawable.chat_bg_dark); // Setting background to null for light theme

                break;
        }
    }


    private void setQuestionText(String question, String categoryCode) {
        editTextMessage.setText(question);
        editTextMessage.setSelection(editTextMessage.getText().length());
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

    public void shareMessage(String question, String answer) {

        BeanHoroPersonalInfo beanHoroPersonalInfo;
        if (isMatching) {
            beanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail();
        }
        // check of clnType for Numerology screen and screen Id check for Home Screen Chat
        else if (!TextUtils.isEmpty(clnyType) || screenId == com.ojassoft.astrosage.utils.CGlobalVariables.AI_HOME_SCREEN_MODULE) {
            beanHoroPersonalInfo = com.ojassoft.astrosage.utils.CUtils.getNumeroBeanHoroPersonalInfo();
        } else {
            beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
        }
        String placeOfBirth = beanHoroPersonalInfo.getPlace() == null ? "" : beanHoroPersonalInfo.getPlace().getCityName();

        String dob = CUtils.appendZeroOnSingleDigit(beanHoroPersonalInfo.getDateTime().getDay()) + "-" + CUtils.appendZeroOnSingleDigit(beanHoroPersonalInfo.getDateTime().getMonth() + 1) + "-" + beanHoroPersonalInfo.getDateTime().getYear();
        String tob = CUtils.convertTimeToHrMtScAmPm(CUtils.appendZeroOnSingleDigit(beanHoroPersonalInfo.getDateTime().getHour()) + ":" + CUtils.appendZeroOnSingleDigit(beanHoroPersonalInfo.getDateTime().getMin()));
        String birthDetails = getResources().getString(R.string.birth_detail_top_heading) + " - " + dob + ", " + placeOfBirth + " | " + tob;

        if (selectedModule != -1 && selectedSubModule != -1) {
            ShareKundliMessageFragment.getInstance(question, answer, selectedModule, selectedSubModule, birthDetails).show(getSupportFragmentManager(), ShareMessageFragment.TAG);
        } else
            ShareMessageFragment.getInstance(question, answer).show(getSupportFragmentManager(), ShareMessageFragment.TAG);
    }

    ArrayList<String> messageChunks;

    /**
     * Processes and displays AI responses in the chat interface.
     *
     * This method handles different types of AI responses:
     * - Typewriter Effect: Displays responses with animated typing effect
     * - Chunked Messages: Splits long responses into manageable chunks
     * - Complete Responses: Finalizes response display and enables user input
     *
     * The method manages:
     * - UI thread safety for all UI updates
     * - Typewriter animation state management
     * - Message chunking for better readability
     * - Response completion state handling
     *
     * @param updatedString The AI response text to display
     * @param answerId Unique identifier for the AI response
     * @param isTypeWriterEffect Whether to apply typewriter animation effect
     * @param isCompleteResponse Whether this is the final response in a sequence
     */
    public void setResponse(String updatedString, long answerId, boolean isTypeWriterEffect, boolean isCompleteResponse) {
        //Log.e("KundliAIChatTesting", "setResponse: "+updatedString );
        runOnUiThread(() -> {
            //showHideTypingIndicator(View.GONE);
            ivWaitMsg.setVisibility(View.GONE);
            //scrollMyListViewToBottom();
            if (isCompleteResponse) enableStopButton();
            if (isTypeWriterEffect) {
                addMessageToAdapter(updatedString, CGlobalVariables.ASTROLOGER, answerId, true);
                //Log.e("TestCloudChat", "setResponse: in if isTYpeWriterEffect " + updatedString + " answerId = " + answerId);
            } else {
                // Log.e("MyTag", "contains \\n = " + updatedString.contains("\\n\\n"));
                if (currentTypeWriter != null) {
                    if (updatedString.contains("\\n\\n")) {
                        messageChunks = split(updatedString);
                        //Log.e("KundliAIChatTesting", "chunk Messages = " + messageChunks);
                    } else {
                        currentTypeWriter.updateAnimatedText(updatedString);
                    }
                }
            }
        });
    }


    private void openTopupRechargeDialog() {
        try {
            if (topupRechargeDialog != null) {
                topupRechargeDialog.dismiss();
            }
            Log.e("TestTopup", "openTopupRechargeDialog()");
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_TOPUP_DIALOG_OPEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            topupRechargeDialog = new TopupRechargeDialog();
            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
            topupRechargeDialog.show(ft, "TopupRechargeDialog");
        } catch (Exception e) {
            Log.e("TestTopup", "openTopupRechargeDialog() Exception=" + e);
            //
        }
    }

    public void getTopupAmounts() {
        boolean isLogin = CUtils.getUserLoginStatus(MiniChatWindow.this);
        //Log.e("TestTopup", "getTopupAmounts() isLogin="+isLogin);

        if (!isLogin) {
            Intent intent1 = new Intent(this, FlashLoginActivity.class);
            startActivity(intent1);
            isShowLoginScreen = true;
            return;
        }
        //Log.e("TestTopup", "getTopupAmounts() ");
        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.no_internet), activity);
        } else {
            showProgressBar();
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi = apiList.getTopupRecharges(getRechargeParams(CUtils.getUserID(activity)));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            if (!myResponse.isEmpty()) {
                                JSONObject jsonObject = new JSONObject(myResponse);
                                Log.e("TestTopup", "onResponse " + jsonObject);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
                                    CUtils.saveStringData(activity, CGlobalVariables.TOPUP_RECHARGE_DATA, jsonObject.toString());
                                    openTopupRechargeDialog();
                                } else if (status.equals("100")) {
                                    CUtils.saveStringData(activity, CGlobalVariables.TOPUP_RECHARGE_DATA, "");
                                    LocalBroadcastManager.getInstance(MiniChatWindow.this).registerReceiver(mReceiverBackgroundLoginService
                                            , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                    startBackgroundLoginService();
                                } else {
                                    CUtils.saveStringData(activity, CGlobalVariables.TOPUP_RECHARGE_DATA, "");
                                    CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.something_wrong_error) + " (" + status + ")", activity);
                                }
                            }
                        } catch (Exception e) {
//                            Log.e("failureTest", "exception: "+e.getMessage() );
                            CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.something_wrong_error), activity);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    Log.e("TestTopup", "onFailure " + t);
                    CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.something_wrong_error), activity);
                }
            });
        }
    }

    public Map<String, String> getRechargeParams(String mobileNo) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("SAN WA Wallet params=>", headers.toString());
        return CUtils.setRequiredParams(headers);
    }

    CustomProgressDialog pd;

    private void showProgressBar() {
        try {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
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
            //
        }
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(MiniChatWindow.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    int backgroundLoginCount = 0;
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            /*if (status.equals(CGlobalVariables.SUCCESS)) {
                if(backgroundLoginCount == 0) {
                    getTopupAmounts();
                    backgroundLoginCount++;
                }
            } else {
                CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }*/

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(MiniChatWindow.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }

        }
    };

    /**
     * Handles results from various activities launched for result.
     *
     * This method processes results from the following activities:
     * - Top-up amount selection (requestCode = 1): Handles recharge amount selection
     * - Speech to text input (REQUEST_CODE_SPEECH_INPUT): Processes voice recognition results
     * - Place search selection (SUB_ACTIVITY_PLACE_SEARCH): Updates location data from place search
     *
     * For speech recognition, the method:
     * - Extracts recognized text from the result
     * - Inserts the text at the current cursor position
     * - Handles text insertion with proper spacing
     *
     * For place search, the method:
     * - Updates global location variables with selected place details
     * - Saves the selected place as current location
     * - Updates timezone and coordinate information
     *
     * @param requestCode Identifier for the activity that was launched
     * @param resultCode Result status (RESULT_OK if successful)
     * @param data Intent containing the result data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("TestTopup", "getTopupAmounts();");

        // Handle top-up amount selection result
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //getTopupAmounts();
            }
        } else if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            try {
                if (resultCode == RESULT_OK && data != null) {
                    // Get speech recognition results
                    ArrayList<String> result = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    String voiceText = Objects.requireNonNull(result).get(0);

                    // Get current cursor position in edit text
                    int start = editTextMessage.getSelectionStart();
                    int end = editTextMessage.getSelectionEnd();

                    // Delete any selected text
                    editTextMessage.getText().delete(start, end);

                    // Insert recognized text with space if not at start
                    if (editTextMessage.getText().length() > 1)
                        editTextMessage.getText().insert(start, " " + voiceText);
                    else
                        editTextMessage.getText().insert(start, voiceText);
                }
            } catch (Exception ignored) {
                // Ignore any errors in speech recognition processing
            }

        }        // Handle place search selection result
        else if (requestCode == BaseInputActivity.SUB_ACTIVITY_PLACE_SEARCH) {

                try {
                    if (data != null) {
                        Bundle bundle = data.getExtras();

                        // Extract selected place details from bundle
                        BeanPlace place = com.ojassoft.astrosage.utils.CUtils.getPlaceObjectFromBundle(bundle);
                        CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                                .setPlace(place);
                        //CGlobal.getCGlobalObject().setHoroPersonalInfoObject(CGlobal.getCGlobalObject().getHoroPersonalInfoObject());
                        // Update global location variables with selected place details
                        cplace = place.getCityName();
                        clat = place.getLatitude();
                        clong = place.getLongitude();
                        ctimezone = place.getTimeZone();
                        ctimezoneid = place.getTimeZoneString();
                       // Log.d("GPSCitySearch", "TimeZoneString: " + place.getTimeZoneString());

                        if (place != null) {
                            // Save selected place as current place
                            com.ojassoft.astrosage.utils.CUtils.saveBeanPalce(MiniChatWindow.this, place);
                            //updateDataOnPlaceChange(place); //commented by Abhishek bcz it is calling in onresume
                        }

                    }
                } catch (Exception e) {
                    // Silently handle any errors in processing place selection
                }

        }
    }

    private TypeWritterKundali typeWriterForGreetingInstance(int index, int lastIndex) {
        TypeWritterKundali newTypeWriter = new TypeWritterKundali(this, index);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, toPixel(10), 0, 0);
        newTypeWriter.setLayoutParams(params);
        newTypeWriter.setPadding(toPixel(10), toPixel(4), toPixel(10), toPixel(4));
        newTypeWriter.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        newTypeWriter.setTextSize(16);
        newTypeWriter.setTextColor(ContextCompat.getColor(this, R.color.black));
        FontUtils.changeFont(activity, newTypeWriter, CGlobalVariables.FONTS_POPPINS_LIGHT);
        if (index == 0) {
            newTypeWriter.setBackgroundResource(R.drawable.bg_chat_white);
        } else if (index >= lastIndex - 1) {
            newTypeWriter.setBackgroundResource(R.drawable.bg_ai_chat_date);
        } else {
            newTypeWriter.setBackgroundResource(R.drawable.drawable_ai_greeting_normal_bg);
        }
        return newTypeWriter;
    }

    private TypeWritterKundali typeWriterInstance(int index) {
        TypeWritterKundali newTypeWriter = new TypeWritterKundali(this, index);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, toPixel(10), 0, 0);
        newTypeWriter.setLayoutParams(params);
        newTypeWriter.setPadding(toPixel(10), toPixel(4), toPixel(10), toPixel(4));
        newTypeWriter.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        newTypeWriter.setTextSize(17);
        newTypeWriter.setTextColor(ContextCompat.getColor(this, R.color.black));
        newTypeWriter.setBackgroundResource(R.drawable.bg_ai_astro_mgs);
        FontUtils.changeFont(activity, newTypeWriter, CGlobalVariables.FONTS_POPPINS_LIGHT);
        return newTypeWriter;
    }

    private int toPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * Handles completion of typewriter animation for a message chunk.
     *
     * This method manages the sequential display of message chunks with typewriter effect:
     * - Determines the next chunk to display
     * - Creates a new TypeWritterKundali instance for the next chunk
     * - Adds the new instance to the UI layout
     * - Starts the typewriter animation for the next chunk
     * - Handles completion of all chunks
     *
     * When all chunks are complete, the method:
     * - Shows the send/stop button toggle
     * - Displays static questions
     * - Enables user input
     * - Scrolls to the bottom if needed
     *
     * @param index The index of the completed message chunk
     */
    @Override
    public void onFinishTyping(int index) {

        int nextPosition = index + 1;
        //  Log.e("KundliAIChatTesting", "Current position = " + index);
        if (messageChunks == null || nextPosition >= messageChunks.size() || stopTypeWriter) {
            toggleSendStopButtonVisibility(true);
            rvStaticQuestions.setVisibility(View.VISIBLE);
            buttonSendLayout.setEnabled(true);
            return;
        }
        currentTypeWriter.setText(Html.fromHtml(CUtils.convertMarkdownToHTML(messageChunks.get(index).replace("\n", "<br>"))));
        TypeWritterKundali instance = typeWriterInstance(nextPosition);
        llTypeWriterParent.addView(instance);
        stopTypeWriter = false;
        currentTypeWriterPosition = nextPosition;
        currentTypeWriter = instance;
        // Log.e("KundliAIChatTesting","position = "+ nextPosition +" Message = "+ messageChunks.get(nextPosition));

        instance.animateText(messageChunks.get(nextPosition), MiniChatWindow.this, this);
    }


    /**
     * Converts text to speech for a specific message in the chat.
     *
     * <p>This method handles text-to-speech conversion with the following features:</p>
     * <ul>
     *   <li>Automatic language detection and locale setting</li>
     *   <li>Support for Hindi, English, and Romanized Hindi</li>
     *   <li>Text preprocessing to remove formatting tags</li>
     *   <li>Position tracking for UI updates during playback</li>
     * </ul>
     *
     * <p>The method ensures proper language selection for optimal speech synthesis
     * and maintains UI state during voice playback.</p>
     *
     * @param text The text content to convert to speech
     * @param position The position of the message in the chat list for UI updates
     */
    public void speakOutMsg(String text, int position) {
        text = replaceExtraTags(text);
        identifyLanguage(text, false);
        Log.e("speakOutMsg", "Language Code = " + langCode);
        if(text.contains("~~")){
            text = text.substring(0,text.indexOf("~~"));
        }
        if (langCode.equalsIgnoreCase("hi:Latn")) {
            tts.setLanguage(new Locale("hi"));
        } else {
            tts.setLanguage(new Locale(langCode));
        }
        this.speakingPosition = position;
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utteranceId");
    }

    public void stopSpeaking() {
        tts.stop();
        messageAdapter.notifyItemChanged(speakingPosition);
        speakingPosition = -1;
    }

    private String replaceExtraTags(String text) {
        return text.replace("- <strong>", "")
                .replace("</strong>:", "");

    }


    /**
     * Submits user rating for an AI response to the server.
     *
     * This method sends rating data to the backend API with the following information:
     * - User rating value (1-5 stars)
     * - Answer ID for the specific response
     * - User profile information (name, gender, birth details)
     * - Language preferences and settings
     * - Device and app information
     *
     * The method handles:
     * - Language code detection and conversion
     * - Roman script language identification
     * - User birth details extraction
     * - API response processing
     *
     * @param rating The user's rating value (1.0 to 5.0)
     * @param answerId The unique identifier of the AI response being rated
     */
    public void callRatingApi(float rating, String answerId) {
        scrollMyListViewToBottom();
        int chatLangCode = CUtils.getIntData(this, CUtils.getLanguageKey(LANGUAGE_CODE), CGlobalVariables.ENGLISH);
        if (MiniChatWindow.langCode.equals("hi")) {
            chatLangCode = CGlobalVariables.HINDI;
        } else if (MiniChatWindow.langCode.equals("en")) {
            chatLangCode = CGlobalVariables.ENGLISH;
        }

        int isRoman = MiniChatWindow.isRomanLang();
        if (isRoman == 1)
            chatLangCode = 1;

        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(this);
        Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getRatingResponse(
                "38", (int) rating + "", "good", com.ojassoft.astrosage.utils.CUtils.getUserName(this), CUtils.getCountryCode(this),
                CUtils.getMyAndroidId(this), ASTROSAGE_AI_PACKAGE_NAME, CUtils.getAppVersion(this) + "", beanHoroPersonalInfo.getGender(),
                beanHoroPersonalInfo.getDateTime().getDay() + "", (beanHoroPersonalInfo.getDateTime().getMonth()) + "", beanHoroPersonalInfo.getDateTime().getYear() + "",
                beanHoroPersonalInfo.getDateTime().getHour() + "", beanHoroPersonalInfo.getDateTime().getMin() + "", beanHoroPersonalInfo.getDateTime().getSecond() + "",
                chatLangCode + "", CUtils.getApplicationSignatureHashCode(this), "getaiastrorating", String.valueOf(1), answerId

        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    try {
                      /*  String resposeData = response.body().string();
                        JSONObject jsonObject = new JSONObject(resposeData);
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(MainActivity.this, msg + " ", Toast.LENGTH_SHORT).show();*/
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                        //  Toast.makeText(MainActivity.this, ignored + " ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MiniChatWindow.this, t + " ", Toast.LENGTH_SHORT).show();

            }
        });
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

    private String textLanguage;


    private void setUtteranceProgressListener() {
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(() -> {
                    messageAdapter.notifyItemChanged(speakingPosition);
                    speakingPosition = -1;
                });
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    public void showTellUsMoreDialog(float rating, String answerId) {
        Dialog dialog = new TelUsMoreDialog(this, rating, answerId);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    @Override
    protected void onStop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onStop();
    }

    /*private void showStaticGreetingMessage() {
        //String greetingMessage;
        if (screenId == BASIC_LAGNA_SCREEN_ID) {
            greetingMessage = getString(R.string.intro_message_lagna, "\\n\\n", "\\n\\n");
        } else {
            greetingMessage = getString(R.string.intro_message_others, screenName);
        }
        ArrayList<String> textMessage = split(greetingMessage);
        type_writer_layout.removeAllViews();
        int count = 0;
        do {
            TypeWritterKundali instance = typeWriterForGreetingInstance(count,textMessage.size());
            type_writer_layout.addView(instance);
            instance.setText(textMessage.get(count++));
        } while (textMessage.size() > count);

    }*/
    private void showTypingGreetingMessage() {
        buttonSendLayout.setEnabled(false);
        CUtils.hideMyKeyboard(this);
        //String greetingMessage;
        if (screenId == BASIC_LAGNA_SCREEN_ID) {
            greetingMessage = getString(R.string.intro_message_lagna);
        } else {
            greetingMessage = getString(R.string.intro_message_others, screenName);
        }

        ArrayList<String> textMessage = split(greetingMessage);
        TypeWritterKundali typeWritterKundali = typeWriterForGreetingInstance(0, textMessage.size());
        LinearLayout type_writer_layout = findViewById(R.id.type_writer_layout);
        TypeWritterKundali.OnTypingComplete onTypingComplete = new TypeWritterKundali.OnTypingComplete() {
            @Override
            public void onFinishTyping(int index) {
                int nextPosition = index + 1;
                //  Log.e("KundliAIChatTesting", "Current position = " + index);
                if (textMessage == null || nextPosition >= textMessage.size()) {
                    toggleSendStopButtonVisibility(true);
                    rvStaticQuestions.setVisibility(View.VISIBLE);
                    buttonSendLayout.setEnabled(true);
                    return;
                }
                TypeWritterKundali instance = typeWriterForGreetingInstance(nextPosition, textMessage.size());
                type_writer_layout.addView(instance);

                // Log.e("KundliAIChatTesting","position = "+ nextPosition +" Message = "+ messageChunks.get(nextPosition));

                instance.animateText(textMessage.get(nextPosition), MiniChatWindow.this, this);
            }
        };

        type_writer_layout.removeAllViews();
        typeWritterKundali.animateText(textMessage.get(0), this, onTypingComplete);
        type_writer_layout.addView(typeWritterKundali);
    }

    /**
     * Removes HTML tags and formatting from text messages for clean display.
     *
     * This utility method cleans text content by:
     * - Converting HTML line breaks to newline characters
     * - Removing HTML formatting tags (strong, em, etc.)
     * - Handling escaped newline characters
     * - Truncating text at special markers (^^)
     *
     * The method ensures that text is properly formatted for:
     * - Clipboard copying operations
     * - Text-to-speech synthesis
     * - Clean message display
     * - Share functionality
     *
     * @param textMessage The HTML-formatted text to clean
     * @return Clean text without HTML tags and formatting
     */
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

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
        showSuggestions(false);
        stopAutoScroll();
    }

    boolean isShowingGreeting = false;

    private void showGreetingMessage() {
        isShowingGreeting = true;
        buttonSendLayout.setEnabled(false);
        CUtils.hideMyKeyboard(this);
        showHideTypingIndicator(View.VISIBLE);
        greetingMessage = getString(R.string.intro_message_others, screenName);
        setResponse(greetingMessage, -1, true, false);
        /*if (screenId == BASIC_LAGNA_SCREEN_ID) {
            greetingMessage = getString(R.string.intro_message_lagna, "\\n\\n","\\n\\n");
            speakOut(greetingMessage, String.valueOf(-1));
        } else {
            greetingMessage = getString(R.string.intro_message_others,screenName);
            setResponse(greetingMessage,true, String.valueOf(-1));
        }*/
    }

    public void setQuestionFromSuggestions(String question) {
        if (ivStop.getVisibility() == View.GONE && ivWaitMsg.getVisibility() == View.GONE) {
            identifyLanguage(question, true);
        } else {
            setQuestionText(question, "");
        }
    }

    private void replaceGreeting(String newGreeting) {
        if(TextUtils.isEmpty(getIntent().getStringExtra(LAGANA_CHART_QUESTION))) {
            addMessageToAdapter(newGreeting, ASTROLOGER, -1, true);
        }else{
            addMessageToAdapter(newGreeting, ASTROLOGER, -1, false);
        }
        scrollMyListViewToBottom();
    }

    /**
     * Deletes selected messages from the chat and database.
     *
     * <p>This method performs bulk deletion of user-selected messages:</p>
     * <ul>
     *   <li>Executes deletion operations on a background thread</li>
     *   <li>Removes messages from the local database</li>
     *   <li>Updates the UI adapter and global message list</li>
     *   <li>Clears the selection state and copy list</li>
     *   <li>Updates the UI on the main thread</li>
     * </ul>
     *
     * <p>The method ensures thread safety and proper cleanup of all
     * message references across the application.</p>
     */
    public void deleteChatMessages() {
        try {
            Executors.newSingleThreadExecutor().execute(() -> {
                ArrayList<ChatMessage> messagesToDeleteList = messageAdapter.getMessagesToDeleteList();
                for (ChatMessage message : messagesToDeleteList) {
                    chatDao.deleteMessage(message);
                    messageAdapter.removeMessageFromList(message);
                    if(AstrosageKundliApplication.kundliChatMessages != null && !AstrosageKundliApplication.kundliChatMessages.isEmpty()){
                        AstrosageKundliApplication.kundliChatMessages.remove(message);
                    }
                }
                runOnUiThread(() -> {
                    messageAdapter.notifyDataSetChanged();
                    clearCopyList();
                    messageAdapter.isLongPressClicked = false;
                });
//                for (ChatMessage item : messagesToDeleteList) {
//                    try {
//                        db.deleteMessage(item.chatId());
//                    } catch (Exception e) {
//                        Log.d("deleteChatMessage", "deleteChatMessages e: " + e);
//                    }
//                }
            });
        } catch (Exception e) {
            Log.d("deleteChatMessage", "deleteChatMessages e: " + e);
        }
    }

    private void openVoiceToTextDialog(int selectedLanguage) {
        try {
            String language = CUtils.getLanguageForSpeechRecognize(selectedLanguage);
            //Log.e("TestLang", "language="+language);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
            intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{language});
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            //
        }
    }

    public void setLikeUnlike(String answerId, int isLiked, int isDislike) {
        try {
            chatDao.setLikeDislike(answerId, isLiked, isDislike);
            Call<ResponseBody> call = RetrofitClient.getKundliAIInstance().create(ApiList.class).setLikeUnlike(
                    CUtils.getMyAndroidId(this),
                    CUtils.getAppPackageName(this),
                    String.valueOf(CUtils.getAppVersion(this)),
                    CUtils.getApplicationSignatureHashCode(this),
                    "getuserlikedresponse",
                    answerId,
                    isLiked,
                    CUtils.getCountryCode(this) + CUtils.getUserID(this));

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

    // THIS IS THE CORRECT, RELIABLE CODE
    private void loadOldChat() {
        try {
            // STEP 1: Get the count of messages ONLY from the current screen's adapter.
            // This is always the correct number for the current conversation. No more global list confusion.
            int currentMessageCount = messageAdapter.getItemCount();

            // STEP 2: Use the reliable count as a direct offset for the database query.
            // This is not a guess. It says "give me the next 10 messages after the ones I already have".
            ArrayList<ChatMessage> newlyLoadedMessages = chatDao.getMessagePaged(conversationId, currentMessageCount, 10);

            // STEP 3: If the database returns nothing, we are done. Simple and clean.
            if (newlyLoadedMessages == null || newlyLoadedMessages.isEmpty()) {
                Log.d("LoadOldChat", "No more messages to load from the database.");
                tvLoadOld.setVisibility(View.GONE); // Optional: hide the "load more" button
                return;
            }

            // STEP 4: Sort ONLY the small, new list of 10 messages. This is extremely fast.
            Collections.sort(newlyLoadedMessages, (m1, m2) -> m1.getDateCreated().compareTo(m2.getDateCreated()));

            // STEP 5: Add the new messages to the TOP of the adapter's list.
            messageAdapter.prependMessages(newlyLoadedMessages);

            // STEP 6: Keep the scroll view from jumping, creating a smooth user experience.
            messagesListView.scrollToPosition(newlyLoadedMessages.size() - 1);

            // STEP 7: Re-enable the listener for the next "load more" event.
            setOnScrollListener();

        } catch (Exception e) {
            Log.e("LoadOldChat", "Failed to load old chat messages.", e);
        }
    }


    public int getActualMessageCount() {
        int count = 0;
        for (ChatMessage message : AstrosageKundliApplication.kundliChatMessages) {
            if (message.chatId() != -1)
                count++;
        }
//        Log.e("greetingCheck", "getActualMessageCount:in loadOldChat "+count );
        return count;
    }

    public void showServerIsBusy() {
        runOnUiThread(() -> {
            showHideTypingIndicator(View.GONE);
            if (this.speakingPosition != -1) stopSpeaking();
            CUtils.showSnackbar(recycleNestedScrollView, getResources().getString(R.string.error_cloud_chat_limit_reached), activity);
        });
    }

    /**
     * Enables the stop button and related UI elements for user interaction.
     *
     * This method updates the UI state to allow user input:
     * - Changes button background to orange color
     * - Enables the send button for new messages
     * - Enables the "Load Old Chat" functionality
     *
     * The method is typically called when AI responses are complete
     * and the user can send new messages.
     */
    private void enableStopButton() {
        buttonSendLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_orange));
        buttonSendLayout.setEnabled(true);
        tvLoadOld.setEnabled(true);

    }

    /**
     * Disables the stop button and related UI elements during AI processing.
     *
     * This method updates the UI state to prevent user input during AI response generation:
     * - Changes button background to gray color
     * - Disables the send button to prevent duplicate submissions
     * - Disables the "Load Old Chat" functionality
     *
     * The method is typically called when AI is processing a user's question
     * to prevent multiple submissions and provide visual feedback.
     */
    private void disableStopButton() {
        buttonSendLayout.setEnabled(false);
        buttonSendLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_gray_live));
        tvLoadOld.setEnabled(false);
    }
}

