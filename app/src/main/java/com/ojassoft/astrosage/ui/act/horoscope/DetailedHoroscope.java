package com.ojassoft.astrosage.ui.act.horoscope;

import static com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity.BACK_FROM_PLAN_PURCHASE_AD_SCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_OPEN_FIRST_TIME_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.horoscopeScreenIds;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.utils.CUtils.sendClosePipBroadcastMessage;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE_YEAR_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.OPEN_LIVE_IN_PIP;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.RASHI_HOROSCOPE_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;
import static com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.IHoroscopeDetailSelect;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.CheckNextYearPridictionDataService;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.BaseTtsActivity;
import com.ojassoft.astrosage.ui.customcontrols.AppRater;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.AIHoroscopeFragment;
import com.ojassoft.astrosage.ui.fragments.AskQuestionsFragment;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.KundliAIFirstTimeMsgFragment;
import com.ojassoft.astrosage.ui.fragments.PanchangInputFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.DailyHoroscopeFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.HoroscopeNotificationFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.MonthlyHoroscopeFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.WeeklyHoroscopeFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.WeeklyLoveHoroscopeFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.YearlyHoroscope;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.TypeWriter;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailedHoroscope extends BaseTtsActivity implements IHoroscopeDetailSelect, IPanchang, DailyHoroscopeFragment.UpdateMoonSign, VolleyResponse {

    private static final int FETCH_LIVE_ASTROLOGER = 1;
    String[] pageTitles;
    ViewPager viewPager;

    final int AI_HOROSCOPE_SCREEN = 0;
    final int DAILY_SCREEN = 1;
    final int PANCHANG = 2;
    final int WEEKLY_SCREEN = 3;
    final int WEEKLY_LOVE_SCREEN = 4;
    final int MONTHLY_SCREEN = 5;
    final int YEARLY_SCREEN = 6;

    /*public final int HOROSCOPE = 0;
    public final int HOME = 1;
    public final int TODAY = 2;
    public final int WEEKLY = 3;
    public final int WEEKLY_LOVE = 4;
    public final int MONTHLY = 5;
    public final int YEARLY = 6;
    public final int SETTINGS = 7;
    public final int HOROSCOPE_NOTIFICATION = 8;
    public final int MISC = 9;
    public final int FEEDBACK = 10;
    public final int RATE_APP = 11;
    public final int SHARE_APP = 12;
    public final int OUR_OTHER_APPS = 13;
    public final int CALL_US = 14;
    public final int ASTRO_SHOP = 15;
    public final int ASK_OUR_ASTROLOGER = 16;
    public final int ABOUT_US = 17;
    private static final int CLOUD_SIGN_OUT = 18;
    public static final int PRODUCT_PLAN_LIST = 19;*/

    /*
     * DailyWeeklyMonthlyHoroscope dailyHoroscope; DailyWeeklyMonthlyHoroscope
     * weeklyHoroscope; DailyWeeklyMonthlyHoroscope weeklyLoveHoroscope;
     * DailyWeeklyMonthlyHoroscope monthlyHoroscope;
     */
    AIHoroscopeFragment aiHoroscopeFragment;
    DailyHoroscopeFragment dailyHoroscope;
    WeeklyHoroscopeFragment weeklyHoroscope;
    WeeklyLoveHoroscopeFragment weeklyLoveHoroscope;
    MonthlyHoroscopeFragment monthlyHoroscope;
    YearlyHoroscope yearlyHoroscope;
    PanchangInputFragment panchangInputFragment;

    public int rashiType;
    int selectedScreen;
    int predictionType;
    boolean fromNotification = false;
    TextView titleTextView;
    ImageView homeImageView;
    ImageView moreImageView;
    ImageView toggleImageView;
    ImageView imgWhatsApp;
    HomeNavigationDrawerFragment drawerFragment;

    //ModulePagerAdapter modulePagerAdapter;
    ViewPagerAdapter adapter;
    private TextView tvToolBarTitle;

    private Toolbar toolBar_HoroscopeDetail;

    private TabLayout tabs_detailed_horoscope;
    Resources resources;
    //SlidingMenu slidingMenu;

    ImageView ivToggleImage, imgBackButton;

    //DetailedHoroscopeMenuFragment detailedHoroscopeMenuFragment;

    private CountDownTimer myCountDownTimer;
    private String staticText = null;
    private String dynamicText = null;
    private ArrayList<String> arrayList;
    public BeanPlace beanPlace;
    public Calendar calendar;
    public int screenType;
    LinearLayout navView;
    FloatingActionButton fabOutputMaster;

    String heading = "";
    String subHeading = "";
    private RequestQueue queue;
    private ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;

    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private static final int PERMISSION_REQ_CODE = 1;

    RelativeLayout ask_question_layout;
    TypeWriter tv_ask_quest;
    String conversationId;
    HashMap<String, ArrayList<String>> questionMap;

    private final String GET_SUGGESTED_QUESTION_DATE = "horoscope_suggested_questions_date";
    private final String SUGGESTED_QUESTION_KEY = "horoscope_suggested_questions";

    public int yearToPass;

    private boolean isCloudAIClick = false;

    public DetailedHoroscope() {
        super(R.string.app_name);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setData(intent);
    }

    @AddTrace(name = "onDetailedHoroscopeCreateTrace", enabled = true /* optional */)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();
        setContentView(R.layout.lay_output_screen_horoscope);
        /*typeface = CUtils.getUserSelectedLanguageFontType(
                getApplicationContext(),
                CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/

        heading = getResources().getString(R.string.pop_up_heading_horoscope);
        subHeading = getResources().getString(R.string.pop_up_sub_heading_horoscope);


        if (getIntent().getSerializableExtra("place") != null && !getIntent().getSerializableExtra("place").equals("")) {
            beanPlace = (BeanPlace) getIntent().getSerializableExtra("place");
        } else {
            beanPlace = CUtils.getBeanPalce(this);
        }
        if (beanPlace == null) {
            beanPlace = CUtils.getDefaultPlace();
        }
        tv_ask_quest = findViewById(R.id.tv_ask_que);
        setData(getIntent());

        // to send analytics to total count
        arrayList = CUtils.getArrayListStringForHoroscope(this, CGlobalVariables.SAVEHOROSCOPECOUNT);
        if (arrayList.size() > 0) {
            CUtils.sendStaticHoroscopeCountToAnalytics(this, arrayList);
        }
        // CUtils.showAdvertisement(DetailedHoroscope.this,(LinearLayout)findViewById(R.id.advLayout));
        /*CUtils.showAdvertisement(DetailedHoroscope.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
        fabOutputMaster = findViewById(R.id.fabHome);
        navView = findViewById(R.id.nav_view);

//        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        fabOutputMaster.setOnClickListener(v -> {
            fabActions();
        });

        ask_question_layout = findViewById(R.id.ask_question_layout);

        if (ask_question_layout.getVisibility() == View.VISIBLE) {
            getSuggestQuestionForHoroscopeScreens();
        }
        conversationId = KundliHistoryDao.getInstance(this).getConversationId(CGlobalVariables.CONVERS_START + CGlobalVariables.AI_HOROSCOPE_MODULE, getString(R.string.horoscope));
        //Log.e("KundliChatAi","HORO Conversation Id = "+conversationId);
        ask_question_layout.setOnClickListener(v -> {
            isCloudAIClick = true;
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                boolean isFirstTime = CUtils.getBooleanData(this, KUNDLI_AI_OPEN_FIRST_TIME_KEY, true);
                if (isFirstTime) {
                    KundliAIFirstTimeMsgFragment kundliAIFirstTimeMsgFragment = new KundliAIFirstTimeMsgFragment(() -> {
                        openKundliAIChatWindow();
                        CUtils.saveBooleanData(this, KUNDLI_AI_OPEN_FIRST_TIME_KEY, false);
                    });
                    kundliAIFirstTimeMsgFragment.show(getSupportFragmentManager(), "kundliAIWelcomeMsg");
                    return;
                }
                openKundliAIChatWindow();
            } else {
                PopUpLogin popUpLogin = new PopUpLogin
                        (com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI,
                                "ONLY_LOGIN");
                popUpLogin.show(getSupportFragmentManager(), "PopUpLogin");
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                //startActivity(new Intent(OutputMasterActivity.this, LoginSignUpActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int planId = CUtils.getUserPurchasedPlanFromPreference(DetailedHoroscope.this);
                    if (planId != CGlobalVariables.PLATINUM_PLAN_ID && planId != CGlobalVariables.PLATINUM_PLAN_ID_9 && planId != CGlobalVariables.PLATINUM_PLAN_ID_10 && planId != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {

                        PopUpLogin popUpLogin = new PopUpLogin
                                (com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE,
                                        heading,
                                        subHeading,
                                        R.drawable.astrologer_icon_1);
                        popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");
                    }
                } catch (Exception e) {
                    //
                }
            }
        }, 15000);

        if (!AIVoiceCallingActivity.isActivityInPIPMode) {
            if (checkPipPermission()) {
                isLiveAllowed();
            }
        }


        // Check if the interstitial/home popup feature is enabled via TagManager/Prefs
        boolean isPopupEnabled = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, CGlobalVariables.IS_INTERSTICIAL_ENABLED, false);
        // Check if the user is already a premium subscriber (Kundli AI+ or Dhruv Plan)
        boolean isPremiumUser = CUtils.isKundliAIPlusPlan(this) || CUtils.isDhruvPlan(this);
        // Check if the previous screen requested an Ad
        boolean needToShowAd = getIntent().getBooleanExtra("needToShowAD", false);
        // Logic: Show Ad if feature enabled + requested by intent + user NOT premium + time interval allows
        if (isPopupEnabled && needToShowAd && !isPremiumUser && CUtils.canShowInterstitial(this)) {
            // Redirect to the Plan Purchase screen (acting as an Ad)
            com.ojassoft.astrosage.varta.utils.CUtils.openPurchasePlanScreenForAd(
                    this,
                    false,
                    false,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_HOROSCOPE_AD,
                    true,
                    BACK_FROM_PLAN_PURCHASE_AD_SCREEN
            );
        }


    }

    private void openKundliAIChatWindow() {
        int screenId = horoscopeScreenIds[viewPager.getCurrentItem()];
        if (screenId != 132) {
            yearToPass = Integer.parseInt(CUtils.getCurrentYear());
        }
        ArrayList<String> suggestedQuestions = getSuggestedQuestionsForScreenId(screenId);
        Intent intent = new Intent(DetailedHoroscope.this, MiniChatWindow.class);
        intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestions);
        intent.putExtra(RASHI_HOROSCOPE_KEY, rashiType);
        intent.putExtra(HOROSCOPE_YEAR_KEY, String.valueOf(yearToPass));
        intent.putExtra(CURRENT_SCREEN_ID_KEY, screenId);
        intent.putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_HOROSCOPE_SCREEN);
        if (LANGUAGE_CODE == 1) {
            intent.putExtra(KEY_SCREEN_NAME, getRomanScreenName(viewPager.getCurrentItem()));
        } else {
            intent.putExtra(KEY_SCREEN_NAME, getScreenName(viewPager.getCurrentItem()));
        }
        intent.putExtra(KEY_CONVERSATION_ID, conversationId);
        startActivity(intent);
    }

    private void isLiveAllowed() {
        if (!CUtils.getBooleanData(this, CGlobalVariables.displayliveinhoroscope, false)) {
            return;
        }
        //SplitInstallManager manager = SplitInstallManagerFactory.create(this);
        //if (manager.getInstalledModules().contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MODULE_VARTA_LIVE)) {
        String data = com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroList();
        if (TextUtils.isEmpty(data)) {
            getLiveAstrologerList();
        } else {
            parseLiveAstrologerList(data);
        }
        //}
    }

    private boolean checkPipPermission() {
        try {
            AppOpsManager manager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int value;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                value = manager.unsafeCheckOp(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, android.os.Process.myUid(), BuildConfig.APPLICATION_ID);
            } else {
                value = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    value = manager.checkOp(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, android.os.Process.myUid(), BuildConfig.APPLICATION_ID);
                }
            }
            return value == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            return false;
        }
    }

    private void setData(Intent intent) {
        ivToggleImage = findViewById(R.id.ivToggleImage);
        ivToggleImage.setVisibility(View.GONE);

        /*imgBackButton = (ImageView)findViewById(R.id.imgBackButton);
        imgBackButton.setVisibility(View.VISIBLE);
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });*/
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        if (currentMonth >= 9) { //oct-nov-dec
            try {
                Intent startNextyearservice = new Intent(DetailedHoroscope.this, CheckNextYearPridictionDataService.class);
                startService(startNextyearservice);
            } catch (Exception e) {
                //
            }
        }

        toolBar_HoroscopeDetail = findViewById(R.id.tool_barAppModule);
        tvToolBarTitle = toolBar_HoroscopeDetail
                .findViewById(R.id.tvTitle);
        tabs_detailed_horoscope = findViewById(R.id.tabs);
        // tabs_detailed_horoscope.initTextTypeface(typeface);
        /*tabs_detailed_horoscope.setCustomTabView(R.layout.lay_input_kundli_tab_title,
                R.id.tabtext);*/
        try {
            configureActionBarTabStyle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolBar_HoroscopeDetail);
        //tabs_detailed_horoscope.setActivity(DetailedHoroscope.this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toggleImageView = findViewById(R.id.ivToggleImage);
        titleTextView = findViewById(R.id.tvTitle);
        homeImageView = findViewById(R.id.imgHome);
        moreImageView = findViewById(R.id.imgMoreItem);
        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        //showWhatsAppIcon(true);
        toggleImageView.setVisibility(View.GONE);
        homeImageView.setVisibility(View.VISIBLE);
        moreImageView.setVisibility(View.VISIBLE);
        setVisibilityOfMoreIconImage(moreImageView, getResources().getStringArray(
                R.array.detailed_horoscope_menu_titles_list), getResources().obtainTypedArray(
                R.array.detailed_horoscope_menu_titles_icon_list), detailed_horoscope_menu_titles_list_index);
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.gotoHomeScreen(DetailedHoroscope.this);
            }
        });
        resources = getResources();
        // titleTextView.setText(resources.getString(R.string.daily) + " " + resources.getString(R.string.horoscope_tag));
        titleTextView.setText(resources.getString(R.string.horoscopeText));
        titleTextView.setTypeface(mediumTypeface);
        rashiType = intent.getIntExtra("rashiType", 0);
        if (rashiType < 0 || rashiType > 11) {
            rashiType = 0;
        }
        viewPager = findViewById(R.id.pager);
        predictionType = intent.getIntExtra("prediction_type",
                DAILY_SCREEN);
        screenType = intent.getIntExtra("screenType",
                0);
        //getting boolean value both static and dynamic horscope notification to measasure notification
        fromNotification = intent.getBooleanExtra("fromNotification",
                false);
        staticText = intent.getStringExtra("staticnotification");
        dynamicText = intent.getStringExtra("dynamicnotification");


        //for static notification measurment

        if (predictionType == -1 || predictionType == 1) { // TODO change predictionType to 1 if predictionType is for 0
            predictionType = 0;
        }
        selectedScreen = predictionType;


        setViewPagerAdapter();
        //setViewPagerListeners();

        // detailedHoroscopeMenuFragment = new DetailedHoroscopeMenuFragment();
        // customize the SlidingMenu
        //slidingMenu=getSlidingMenu();
        //slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // getSupportActionBar().setDisplayShowTitleEnabled(true);

        showWhatsAppIcon(selectedScreen != YEARLY_SCREEN);

        // viewPager.setCurrentItem(selectedScreen);

        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (LANGUAGE_CODE == 1) {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi, getScreenName(viewPager.getCurrentItem())));
                } else {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli, getScreenName(viewPager.getCurrentItem())));
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        // titleTextView.setText(resources.getString(R.string.daily) + " " + resources.getString(R.string.horoscope_tag));
                        showWhatsAppIcon(true);
                        break;
                    case 1:
                        //titleTextView.setText(resources.getString(R.string.weekly) + " " + resources.getString(R.string.horoscope_tag));
                        showWhatsAppIcon(true);
                        break;
                    case 2:
                        // titleTextView.setText(resources.getString(R.string.weekly_love) + " " + resources.getString(R.string.horoscope_tag));
                        showWhatsAppIcon(true);
                        break;
                    case 3:
                        //titleTextView.setText(resources.getString(R.string.monthly) + " " + resources.getString(R.string.horoscope_tag));
                        showWhatsAppIcon(true);
                        break;
                    case 4:
                        // titleTextView.setText(resources.getString(R.string.yearly) + " " + resources.getString(R.string.horoscope_tag));
                        showWhatsAppIcon(true);
                        break;
                    case 5:
                        // titleTextView.setText(resources.getString(R.string.yearly) + " " + resources.getString(R.string.horoscope_tag));
                        showWhatsAppIcon(false);
                        break;

                }

                updatePlayButton();

                CUtils.googleAnalyticSendWitPlayServie(
                        DetailedHoroscope.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                        CGlobalVariables.pageViewDailyWeeklyMonthlyHoroscope[position],
                        CGlobalVariables.RashiType[rashiType]);

                setCurrentView(position, false);
                Fragment frag = adapter.getFragment(position);
                if (frag instanceof WeeklyLoveHoroscopeFragment) {
                    ((WeeklyLoveHoroscopeFragment) frag).updateData();
                } else if (frag instanceof WeeklyHoroscopeFragment) {
                    ((WeeklyHoroscopeFragment) frag).updateData();
                } else if (frag instanceof MonthlyHoroscopeFragment) {
                    ((MonthlyHoroscopeFragment) frag).updateData();
                } else if (frag instanceof YearlyHoroscope) {
                    ((YearlyHoroscope) frag).updateData();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // Menu
        //slidingMenu.setMode(SlidingMenu.RIGHT);

        // Secondary menu
        //slidingMenu.setSecondaryMenu(R.layout.menu_frame_two);
        // slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
/*
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame_two,
                        detailedHoroscopeMenuFragment).commit();
*/

        // customize the SlidingMenu
        // slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // getSupportActionBar().setDisplayShowTitleEnabled(true);

        // getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.astrosage_logo));

       /* getSupportActionBar().setLogo(
                getResources().getDrawable(R.drawable.astrosage_logo));*/
        // tabs_detailed_horoscope.setDistributeEvenly(false); // To make the Tabs
        // Fixed set this
        // true, This makes
        // the tabs Space
        // Evenly in
        // Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
       /* tabs_detailed_horoscope
                .setCustomTabColorizer(new SlidingTabLayoutInputKundli.TabColorizer() {
                    @Override
                    public int getIndicatorColor(int position) {
                        return getResources().getColor(R.color.tabsScrollColor);
                    }
                });*/
        tabs_detailed_horoscope.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabs_detailed_horoscope.getTabCount(); i++) {
            TabLayout.Tab tab = tabs_detailed_horoscope.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
            if(i==0){
                tab.setIcon(getResources().getDrawable(R.drawable.ic_ai_astrologer_unselected));
            }
        }

        setCurrentView(selectedScreen, false);
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = DetailedHoroscope.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


    private void showWhatsAppIcon(boolean isVisisble) {

        if (isVisisble) {
            if (CUtils.isPackageExisted(this, "com.whatsapp")) {

                imgWhatsApp.setVisibility(View.VISIBLE);
                imgWhatsApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareMessageWithWhatsApp(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_TOOLBAR);
                    }
                });
            }
        } else {
            imgWhatsApp.setVisibility(View.GONE);
            imgWhatsApp.setOnClickListener(null);
        }


    }

    @Override
    public void setVisibilityOfMoreIconImage(View view, final String[] subMenuItems, final TypedArray subMenuItemsIcon, final Integer[] menuIndex) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingForpopUpMenu(view, subMenuItems, subMenuItemsIcon, menuIndex);
            }
        });
    }

    private void callPopUpForRateApp() {
        try {
            myCountDownTimer = new CountDownTimer(RATE_DIALOG_THREAD_SLEEP_TIME,
                    1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    if (!com.ojassoft.astrosage.varta.utils.CUtils.isPopUpLoginShowing) {
                        String Headingtext = DetailedHoroscope.this
                                .getString(R.string.app_mainheading_text_hornocsope);
                        String SubHeadingtext = DetailedHoroscope.this
                                .getString(R.string.app_subheading_text_hornocsope);
                        String Subchildheading = DetailedHoroscope.this
                                .getString(R.string.app_subchild_text_hornocsope);
                        AppRater.app_launched(DetailedHoroscope.this,
                                Headingtext, SubHeadingtext, Subchildheading);
                    }
                }
            };
            myCountDownTimer.start();
        } catch (Exception e) {
            //
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();

        callPopUpForRateApp();

        //EasyTracker.getInstance().activityStart(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
        try {

            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
            }

            if (AppRater.myCountDownTimer != null) {
                AppRater.myCountDownTimer.cancel();
            }

        } catch (Exception e) {
            //Log.i("Exception", e.getMessage().toString());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_WhatsApp_Menu:
                shareMessageWithWhatsApp();
                // Toast.makeText(this, String.valueOf(viewPager.getCurrentItem()),
                // Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void shareMessageWithWhatsApp(String googleAnalyticAction) {
        String heading = "", horoscopeText = "";
        try {
            switch (viewPager.getCurrentItem()) {
                case CGlobalVariables.DAILY_TYPE:
                    heading = dailyHoroscope.getWhatsAppTitle();
                    horoscopeText = dailyHoroscope.getDescription();
                    break;
                case CGlobalVariables.PANCHANG_TYPE:
                    //heading = panchangInputFragment.getWhatsAppTitle();
                    // horoscopeText = panchangInputFragment.getDescription();
                    panchangInputFragment.shareContentData("com.whatsapp", beanPlace);
                    return;
                //break;
                case CGlobalVariables.WEEKLY_TYPE:
                    heading = weeklyHoroscope.getWhatsAppTitle();
                    horoscopeText = weeklyHoroscope.getDescription();
                    break;
                case CGlobalVariables.WEEKLY_LOVE_TYPE:
                    heading = weeklyLoveHoroscope.getWhatsAppTitle();
                    horoscopeText = weeklyLoveHoroscope.getDescription();
                    break;
                case CGlobalVariables.MONTHLY_TYPE:
                    heading = monthlyHoroscope.getWhatsAppTitle();
                    horoscopeText = monthlyHoroscope.getDescription();
                    break;
                case CGlobalVariables.YEARLY_TYPE:
                    heading = yearlyHoroscope.getWhatsAppTitle();
                    horoscopeText = yearlyHoroscope.getDescription();
                    break;
            }
            shareDataOnWhatsApp(getShareTextForWhatsApp(heading, horoscopeText,
                    viewPager.getCurrentItem()));

            // ADDED BY BIJENDRA ON 14-04-15
            // Updated by ankit on 26/3/2019
            CUtils.googleAnalyticSendWitPlayServie(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    googleAnalyticAction,
                    null);
            // END

        } catch (Exception e) {
            //Log.e("shareMessage", e.getMessage());

        }

    }

    private String getShareTextForWhatsApp(String heading,
                                           String horoscopeText, int predictionType) {

        StringBuilder stringBuilder = new StringBuilder();

        // if (((predictionType == CGlobalVariables.MONTHLY_TYPE) && (LANGUAGE_CODE == CGlobalVariables.HINDI))&& CUtils.isSupportUnicodeHindi()) {
        if (predictionType == CGlobalVariables.MONTHLY_TYPE) {
            String temp = heading + "\n" + horoscopeText + "\n";
            if (temp.length() > 2000)
                stringBuilder.append(temp.substring(0, 1500) + "...\n\n");
            else {
                stringBuilder.append(heading + "\n");
                stringBuilder.append(horoscopeText + "\n");
            }

        } else {
            stringBuilder.append(heading + "\n");
            stringBuilder.append(horoscopeText + "\n");
        }
        /*
         * stringBuilder.append("Shared By: "+CUtils.getMyApplicationName(this)+"\n"
         * );
         * stringBuilder.append("https://play.google.com/store/apps/details?id="
         * +getPackageName());
         */
        stringBuilder.append("Download " + CUtils.getMyApplicationName(this)
                + " App: \n");
        stringBuilder.append("https://go.astrosage.com/akwa");
        return stringBuilder.toString();
    }

    private void shareDataOnWhatsApp(String value) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, value);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DetailedHoroscope.this, "Application Not Found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        isCloudAIClick = false;
        super.onResume();
        LANGUAGE_CODE = CUtils.getLanguageCode(this);
        setBottomNavigationText();
        try {
            /*typeface = CUtils
                    .getUserSelectedLanguageFontType(
                            getApplicationContext(),
                            CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/
            if (CUtils.isPackageExisted(DetailedHoroscope.this,
                    "com.whatsapp") && LANGUAGE_CODE == 1) {
                /*tvToolBarTitle.setText(getResources().getStringArray(
                        R.array.horoscope_short_titles_list)[selectedScreen]);*/
                tvToolBarTitle.setText(R.string.horoscopeText);
            } else {
                tvToolBarTitle.setText(R.string.horoscopeText);
                /*tvToolBarTitle.setText(getResources().getStringArray(
                        R.array.horoscope_titles_list)[selectedScreen]);*/
            }

			/*tvToolBarTitle.setText(getResources().getStringArray(
                    R.array.horoscope_titles_list)[selectedScreen]);*/
            //	System.out.println("SELECTED SCEEENNNNNNNNNNNNNN"+selectedScreen);
            tvToolBarTitle.setTypeface(regularTypeface);
            // CUtils.applyTypeFaceOnActionBarTitle(DetailedHoroscope.this,
            // typeface,getResources().getStringArray(R.array.horoscope_titles_list)[selectedScreen]);
            /*CUtils.showAdvertisement(DetailedHoroscope.this,
                    (LinearLayout) findViewById(R.id.advLayout));*/

            if (fromNotification && staticText != null) {
                staticHorscopeNotificationMeasurment(fromNotification, predictionType, staticText);
                staticText = null;
            } else if (fromNotification && dynamicText != null) {
                dynamicHorscopeNotificationMeasurment(fromNotification, predictionType, dynamicText);
                dynamicText = null;
            }

        } catch (Exception e) {

        }

        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi, getScreenName(viewPager.getCurrentItem())));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli, getScreenName(viewPager.getCurrentItem())));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            /*CUtils.removeAdvertisement(DetailedHoroscope.this,
                    (LinearLayout) findViewById(R.id.advLayout));*/
        } catch (Exception e) {

        }
    }

    private void configureActionBarTabStyle() {

        pageTitles = getResources().getStringArray(
                R.array.horoscope_tab_titles_list);

        /*
         * ActionBar actionBar = getSupportActionBar();
         * actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
         * ActionBar.TabListener tabListener = new ActionBar.TabListener() {
         * public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
         * { // show the given tab viewPager.setCurrentItem(tab.getPosition());
         * }
         *
         * public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction
         * ft) { // hide the given tab }
         *
         * public void onTabReselected(ActionBar.Tab tab, FragmentTransaction
         * ft) { // probably ignore this event } };
         *
         * Tab tab = actionBar.newTab().setTabListener(tabListener);
         * actionBar.addTab(tab); tab =
         * actionBar.newTab().setTabListener(tabListener);
         * actionBar.addTab(tab); tab =
         * actionBar.newTab().setTabListener(tabListener);
         * actionBar.addTab(tab); tab =
         * actionBar.newTab().setTabListener(tabListener);
         * actionBar.addTab(tab); tab =
         * actionBar.newTab().setTabListener(tabListener);
         * actionBar.addTab(tab);
         *
         * for(int i = 0; i<actionBar.getTabCount(); i++){ LayoutInflater
         * inflater = LayoutInflater.from(this); View customView =
         * inflater.inflate(R.layout.tab_title, null); TextView titleTV =
         * (TextView) customView.findViewById(R.id.action_custom_title);
         * titleTV.setText(pageTitles[i]); //Here you can also add any other
         * styling you want. titleTV.setTypeface(typeface,Typeface.BOLD);
         * if(LANGUAGE_CODE == CGlobalVariables.HINDI){
         * titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); }
         * actionBar.getTabAt(i).setCustomView(customView); }
         */
    }

    private void setViewPagerAdapter() {

        try {
            //aiHoroscopeFragment = AIHoroscopeFragment.newInstance(rashiType);
            dailyHoroscope = DailyHoroscopeFragment.newInstance(rashiType);
            weeklyHoroscope = WeeklyHoroscopeFragment.newInstance(rashiType);
            weeklyLoveHoroscope = WeeklyLoveHoroscopeFragment.newInstance(rashiType);
            monthlyHoroscope = MonthlyHoroscopeFragment.newInstance(rashiType);
            yearlyHoroscope = YearlyHoroscope.newInstance(rashiType);
            panchangInputFragment = PanchangInputFragment.newInstance("Panchang");

            adapter = new ViewPagerAdapter(getSupportFragmentManager(), DetailedHoroscope.this) {
                @Override
                public Parcelable saveState() {
                    return null;
                }
            };
            //adapter.addFragment(aiHoroscopeFragment, pageTitles[0]);
            adapter.addFragment(dailyHoroscope, pageTitles[1]);
            adapter.addFragment(panchangInputFragment, pageTitles[2]);
            adapter.addFragment(weeklyHoroscope, pageTitles[3]);
            adapter.addFragment(weeklyLoveHoroscope, pageTitles[4]);
            adapter.addFragment(monthlyHoroscope, pageTitles[5]);
            adapter.addFragment(yearlyHoroscope, pageTitles[6]);
            adapter.addFragment(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE), pageTitles[7]);
            viewPager.setAdapter(adapter);
           /* modulePagerAdapter = new ModulePagerAdapter(
                    getSupportFragmentManager());
            viewPager.setAdapter(modulePagerAdapter);*/
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void setViewPagerListeners() {
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

                //Log.e("onPageScroll", String.valueOf(arg0));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {
                // set Tab selected for given position
                // getSupportActionBar().setSelectedNavigationItem(position);
                selectedScreen = position;
                // set actionbar title
                if (CUtils.isPackageExisted(DetailedHoroscope.this,
                        "com.whatsapp") && LANGUAGE_CODE == 1) {
                    /*tvToolBarTitle.setText(getResources().getStringArray(
                            R.array.horoscope_short_titles_list)[selectedScreen]);*/
                    tvToolBarTitle.setText(R.string.horoscopeText);
                } else {
                    /*tvToolBarTitle.setText(getResources().getStringArray(
                            R.array.horoscope_titles_list)[selectedScreen]);*/
                    tvToolBarTitle.setText(R.string.horoscopeText);
                }

				/*tvToolBarTitle.setText(getResources().getStringArray(
                        R.array.horoscope_titles_list)[selectedScreen]);*/
                /*setTitle(getResources().getStringArray(
                        R.array.horoscope_titles_list)[selectedScreen]);
				*/
                /*	System.out.println("SELECTED SCEEENNNNNNNNNNNNNN"+position);*/


                // send google analytics of categories
                // CUtils.googleAnalyticSend(null,
                // CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,CGlobalVariables.pageViewDailyWeeklyMonthlyHoroscope[position],CGlobalVariables.RashiType[rashiType],0L);
                CUtils.googleAnalyticSendWitPlayServie(
                        DetailedHoroscope.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                        CGlobalVariables.pageViewDailyWeeklyMonthlyHoroscope[position],
                        CGlobalVariables.RashiType[rashiType]);
                switch (position) {
                    case YEARLY_SCREEN:
                        // ADDED BY BIJENDRA FOR WHATSAPP MENU
                       /* if (!CUtils.isPackageExisted(DetailedHoroscope.this,
                                "com.whatsapp"))
                            menuItemWhatsApp.setVisible(false);
                        else {
                            if (menuItemWhatsApp != null)
                                menuItemWhatsApp.setVisible(false);
                        }*/
                        // END
                        //slidingMenu.setTouchModeAbove(
                        //SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    default:
                        // ADDED BY BIJENDRA FOR WHATSAPP MENU
                       /* if (!CUtils.isPackageExisted(DetailedHoroscope.this,
                                "com.whatsapp"))
                            menuItemWhatsApp.setVisible(false);
                        else {
                            if (menuItemWhatsApp != null)
                                menuItemWhatsApp.setVisible(true);
                        }*/
                        // END

                        //slidingMenu.setTouchModeAbove(
                        //   SlidingMenu.TOUCHMODE_MARGIN);
                        break;
                }
            }

        });
    }

    @Override
    public void updateLayout(int id) {

    }

    @Override
    public void changeMoonSign(int moonSign) {
        rashiType = moonSign;
        // weeklyHoroscope.updateRashifal(DetailedHoroscope.this,moonSign);
    }


    public class ModulePagerAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<Fragment> mFragments;

        public ModulePagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();

            /*
             * dailyHoroscope =
             * DailyWeeklyMonthlyHoroscope.newInstance(DAILY_SCREEN,rashiType);
             * weeklyHoroscope =
             * DailyWeeklyMonthlyHoroscope.newInstance(WEEKLY_SCREEN,rashiType);
             * weeklyLoveHoroscope =
             * DailyWeeklyMonthlyHoroscope.newInstance(WEEKLY_LOVE_SCREEN
             * ,rashiType); monthlyHoroscope =
             * DailyWeeklyMonthlyHoroscope.newInstance
             * (MONTHLY_SCREEN,rashiType);
             */
            dailyHoroscope = DailyHoroscopeFragment.newInstance(rashiType);
            weeklyHoroscope = WeeklyHoroscopeFragment.newInstance(rashiType);
            weeklyLoveHoroscope = WeeklyLoveHoroscopeFragment
                    .newInstance(rashiType);
            monthlyHoroscope = MonthlyHoroscopeFragment.newInstance(rashiType);
            yearlyHoroscope = YearlyHoroscope.newInstance(rashiType);
            panchangInputFragment = PanchangInputFragment.newInstance("Panchang");

            mFragments.add(dailyHoroscope);
            mFragments.add(panchangInputFragment);
            mFragments.add(weeklyHoroscope);
            mFragments.add(weeklyLoveHoroscope);
            mFragments.add(monthlyHoroscope);
            mFragments.add(yearlyHoroscope);

        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        @Override
        public CharSequence getPageTitle(int position) {

            return pageTitles[position];
        }


    }

    /*
     * private void applyChangedLanguageInApplication() { Intent intent = new
     * Intent(getApplicationContext(), HomeInputScreen.class);
     * intent.putExtra("LANGUAGE_CODE", LANGUAGE_CODE); startActivity(intent);
     * startActivity(new
     * Intent(getApplicationContext(),ActAppModule.class).addFlags
     * (Intent.FLAG_ACTIVITY_CLEAR_TOP)); this.finish(); } private void
     * openLanguageSelectDialog() { FragmentTransaction ft =
     * getSupportFragmentManager().beginTransaction(); FragmentManager fm =
     * getSupportFragmentManager(); Fragment prev =
     * fm.findFragmentByTag("HOME_INPUT_LANGUAGE"); if (prev != null) {
     * ft.remove(prev); } ft.addToBackStack(null); ChooseLanguageFragmentDailog
     * clfd=new ChooseLanguageFragmentDailog(); clfd.show(fm,
     * "HOME_INPUT_LANGUAGE"); ft.commit(); }
     *
     * @Override public void onSelectedLanguage(int languageIndex) {
     * if(LANGUAGE_CODE!=languageIndex) { LANGUAGE_CODE=languageIndex;
     * applyChangedLanguageInApplication(); } }
     */

    /*@Override
    public void switchContent(int menuItemPosition) {

        switch (menuItemPosition) {
            case HOROSCOPE:
                // its saperator only
                break;
            case HOME:
                CUtils.gotoHomeScreen(DetailedHoroscope.this);
                break;
            case TODAY:
                viewPager.setCurrentItem(DAILY_SCREEN);
                break;
            case WEEKLY:
                viewPager.setCurrentItem(WEEKLY_SCREEN);
                break;
            case WEEKLY_LOVE:
                viewPager.setCurrentItem(WEEKLY_LOVE_SCREEN);
                break;
            case MONTHLY:
                viewPager.setCurrentItem(MONTHLY_SCREEN);
                break;
            case YEARLY:
                viewPager.setCurrentItem(YEARLY_SCREEN);
                break;
            case SETTINGS:
                // its saperator only
                break;
            case HOROSCOPE_NOTIFICATION:
                openNotificationCategaryDialog();
                break;
            case MISC:
                // its saperator only
                break;
            case FEEDBACK:
                CUtils.sendFeedBackViaApi(this, regularTypeface,
                        CUtils.getUserName(DetailedHoroscope.this));
                break;
            case RATE_APP:
                CUtils.rateAppication(this);
                break;
            case SHARE_APP:
                CUtils.shareToFriendMail(this);
                break;
            case OUR_OTHER_APPS:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(CGlobalVariables.OUR_OTHER_SOFTWARE_URL));
                startActivity(browserIntent);
                break;
            case CALL_US:
                CUtils.gotoCallUsScreen(this, LANGUAGE_CODE);
                break;
            case ASTRO_SHOP:
                CUtils.gotoAstroShopScreen(this);
                break;
            case ASK_OUR_ASTROLOGER:
                CUtils.gotoAskOurAstrologerScreen(this);
                break;
            case ABOUT_US:
                CUtils.gotoAboutUsScreen(this, LANGUAGE_CODE);
                break;
            case CLOUD_SIGN_OUT:
                logoutFromAstroSageCloud();
                break;
            case PRODUCT_PLAN_LIST:
            *//*
     * CUtils.gotopProductPlanList(HomeInputScreen.this, LANGUAGE_CODE,
     * HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN);
     *//*
                CUtils.gotoProductPlanListUpdated(DetailedHoroscope.this,
                        LANGUAGE_CODE,
                        HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, "");
                break;
        }
        //slidingMenu.showContent();
    }*/

    @Override
    public void callHorscope(int position) {

        if (position == BaseInputActivity.TAG_TODAYS_HOROSCOPE) {
            //viewPager.setCurrentItem(DAILY_SCREEN);
            setCurrentView(DAILY_SCREEN, false);
        } else if (position == BaseInputActivity.TAG_HOROSCOPE_PANCHANG) {
            // viewPager.setCurrentItem(WEEKLY_SCREEN);
            setCurrentView(PANCHANG, false);
        } else if (position == BaseInputActivity.TAG_WEEKLY_HOROSCOPE) {
            // viewPager.setCurrentItem(WEEKLY_SCREEN);
            setCurrentView(WEEKLY_SCREEN, false);
        } else if (position == BaseInputActivity.TAG_WEEKLY_LOVE_HOROSCOPE) {
            //viewPager.setCurrentItem(WEEKLY_LOVE_SCREEN);
            setCurrentView(WEEKLY_LOVE_SCREEN, false);
        } else if (position == BaseInputActivity.TAG_MONTHLY_HOROSCOPE) {
            // viewPager.setCurrentItem(MONTHLY_SCREEN);
            setCurrentView(MONTHLY_SCREEN, false);
        } else if (position == BaseInputActivity.TAG_YEARLY_HOROSCOPE) {
            //viewPager.setCurrentItem(YEARLY_SCREEN);
            setCurrentView(YEARLY_SCREEN, false);
        }
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        //detailedHoroscopeMenuFragment.updateLoginDetials(false, "", "");
        MyCustomToast mct = new MyCustomToast(DetailedHoroscope.this,
                DetailedHoroscope.this.getLayoutInflater(),
                DetailedHoroscope.this, regularTypeface);
        mct.show(getResources().getString(R.string.sign_out_success));
    }

    @Override
    public void openNotificationCategaryDialog() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            Fragment prev = fm.findFragmentByTag("HOROSCOPE_CATEGARY");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            HoroscopeNotificationFragment hnfd = new HoroscopeNotificationFragment();
            hnfd.show(fm, "HOROSCOPE_CATEGARY");
            ft.commit();
        }catch (Exception e){
            //
        }


    }

    @Override
    public void horoscopeDetailDelected(int position) {
        // TODO Auto-generated method stub
        selectedScreen = position;
        if (selectedScreen == -1) {
            selectedScreen = 0;
        }
        if (CUtils.isPackageExisted(DetailedHoroscope.this,
                "com.whatsapp") && LANGUAGE_CODE == 1) {
          /*  tvToolBarTitle.setText(getResources().getStringArray(
                    R.array.horoscope_short_titles_list)[selectedScreen]);*/
            tvToolBarTitle.setText(R.string.horoscopeText);
        } else {
            /*tvToolBarTitle.setText(getResources().getStringArray(
                    R.array.horoscope_titles_list)[selectedScreen]);*/
            tvToolBarTitle.setText(R.string.horoscopeText);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AIHoroScope", "DetailedHoroscope  onActivityResult: BACK_FROM_PROFILE_CHAT_DIALOG  "+requestCode);

        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                }
            }
            break;
            case SUB_ACTIVITY_PLACE_SEARCH:
                Bundle bundle = data.getExtras();
                BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                CGlobal.getCGlobalObject().getHoroPersonalInfoObject().setPlace(place);
                beanPlace = place;
                int cureentPage = viewPager.getCurrentItem();
                Fragment currentFrag = adapter.getFragment(cureentPage);
                if (currentFrag instanceof PanchangInputFragment) {
                    ((PanchangInputFragment) currentFrag).updateAfterPlaceSelect(place);
                }
                break;
            case BACK_FROM_PROFILE_CHAT_DIALOG:
                       // Log.d("AIHoroScope", "onActivityResult: BACK_FROM_PROFILE_CHAT_DIALOG");
                        if (data != null) {
                            boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                            String fromWhere = data.getStringExtra("fromWhere");
                            if (isProceed) {
                                AstrosageKundliApplication.backgroundLoginCountForChat = 0;
                                if (!TextUtils.isEmpty(fromWhere) && fromWhere.equalsIgnoreCase("profile_send")) {
                                    errorLogs = errorLogs + "goto api hit\n";
                                    //sendProfileMsg();
                                    //Log.d("AIHoroScope", "onActivityResult: isProceed true");
                                    UserProfileData userProfileDataBean = (UserProfileData) data.getExtras().get("USER_DETAIL");
                                    aiHoroscopeFragment.handleUserProfileHoroscope(userProfileDataBean);
                                }
                            }

                            if (!isProceed && data.getExtras().containsKey("openKundliList")) {
                                com.ojassoft.astrosage.varta.utils.CUtils.openSavedKundliList(this, "", "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
                            } else if (!isProceed && data.getExtras().containsKey("openProfileForChat")) {
                                boolean prefillData = true;
                                if (data.getExtras().containsKey("prefillData")) {
                                    prefillData = data.getBooleanExtra("prefillData", true);
                                }
                                Bundle bundle1 = data.getExtras();
                                //com.ojassoft.astrosage.varta.utils.CUtils.openProfileForChat(this, "", "profile_send", bundle1, prefillData, BACK_FROM_PROFILE_CHAT_DIALOG);
                                AIHoroscopeFragment.openProfileForHoroScope(this, "", "profile_send", bundle1, prefillData, BACK_FROM_PROFILE_CHAT_DIALOG);
                            }
                        }
                break;
        }
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        //detailedHoroscopeMenuFragment.updateLoginDetials(true, loginName, loginPwd);
    }

    /*public void goToLogin() {

        if (!CUtils.isUserLogedIn(DetailedHoroscope.this)) {
            *//*
     * Intent intent = new Intent(HomeInputScreen.this,
     * ActWizardScreens.class); intent.putExtra("callerActivity",
     * CGlobalVariables.HOME_INPUT_SCREEN);
     * startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
     *//*
            Intent intent = new Intent(DetailedHoroscope.this,
                    ActLogin.class);
            intent.putExtra("callerActivity",
                    CGlobalVariables.HOME_INPUT_SCREEN);
            startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
        } else {
            // detailedHoroscopeMenuFragment.updateLoginDetials(false, "", "");
            MyCustomToast mct = new MyCustomToast(DetailedHoroscope.this,
                    DetailedHoroscope.this.getLayoutInflater(),
                    DetailedHoroscope.this, regularTypeface);
            mct.show(getResources().getString(R.string.sign_out_success));
        }
        //slidingMenu.showContent();
    }*/

    /*private String[] getDrawerListItem() {
        if (CUtils.getCustomObject(DetailedHoroscope.this) == null) {
            return getResources().getStringArray(
                    R.array.detailed_horoscope_menu_titles_list);
        } else {
            return getResources().getStringArray(
                    R.array.module_list);
        }

    }

    private TypedArray getDrawerListItemIcon() {
        if (CUtils.getCustomObject(DetailedHoroscope.this) == null) {
            return getResources().obtainTypedArray(
                    R.array.detailed_horoscope_menu_titles_icon_list);
        } else {
            return getResources().obtainTypedArray(
                    R.array.module_icons);
        }

    }*/
    private void finishActivity() {
        this.finish();
    }

    private void setCurrentView(int index, boolean smoothScroll) {
        try {
            viewPager.setCurrentItem(index, smoothScroll);
            if (tabs_detailed_horoscope != null && adapter != null) {
                adapter.setAlpha(index, tabs_detailed_horoscope);
            }
            if (LANGUAGE_CODE == 1) {
                tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi, pageTitles[index]));
            } else {
                tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli, pageTitles[index]));
            }
        } catch (Exception e){
            //
        }
    }

    // this method is used to calculate static click count horscope notification measurment

    private void staticHorscopeNotificationMeasurment(boolean fromNotification, int predictionType, String staticText) {

        if (fromNotification && predictionType == CGlobalVariables.DAILY_TYPE && staticText.equals(CGlobalVariables.static_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_STATIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_DAILY,
                    null);
        } else if (fromNotification && predictionType == CGlobalVariables.WEEKLY_TYPE && staticText.equals(CGlobalVariables.static_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_STATIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_WEEKLY,
                    null);
        } else if (fromNotification && predictionType == CGlobalVariables.WEEKLY_LOVE_TYPE && staticText.equals(CGlobalVariables.static_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_STATIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_WEEKLY_LOVE,
                    null);

        } else if (fromNotification && predictionType == CGlobalVariables.MONTHLY_TYPE && staticText.equals(CGlobalVariables.static_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_STATIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_MONTHLY,
                    null);


        } else {

        }
    }
// end static horscope notification measurment


    // this method is used to calculate click count of dynamic horscope notification measurment

    private void dynamicHorscopeNotificationMeasurment(boolean fromNotification, int predictionType, String dynamicText) {
        if (fromNotification && predictionType == CGlobalVariables.DAILY_TYPE && dynamicText.equals(CGlobalVariables.dynamic_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DYNAMIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_DAILY,
                    null);
        } else if (fromNotification && predictionType == CGlobalVariables.WEEKLY_TYPE && dynamicText.equals(CGlobalVariables.dynamic_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DYNAMIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_WEEKLY,
                    null);
        } else if (fromNotification && predictionType == CGlobalVariables.WEEKLY_LOVE_TYPE && dynamicText.equals(CGlobalVariables.dynamic_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DYNAMIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_WEEKLY_LOVE,
                    null);

        } else if (fromNotification && predictionType == CGlobalVariables.MONTHLY_TYPE && dynamicText.equals(CGlobalVariables.dynamic_horscope_measurment)) {
            CUtils.googleAnalyticsNotification(
                    this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DYNAMIC_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_LABEL_MONTHLY,
                    null);


        } else {

        }
    }

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actapp_nav_home:
                            Intent intent = new Intent(DetailedHoroscope.this, ActAppModule.class);
                            startActivity(intent);
                            return true;
                        case R.id.actapp_nav_call:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(DetailedHoroscope.this, DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, DetailedHoroscope.this);
                            return true;
                        case R.id.actapp_nav_live:
                            fabActions();
                            return true;
                        case R.id.actapp_nav_chat:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(DetailedHoroscope.this, DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, DetailedHoroscope.this);
                            return true;
                        case R.id.actapp_nav_account:
                            com.ojassoft.astrosage.varta.utils.CUtils.openAccountScreen(DetailedHoroscope.this);
                            return true;
                    }
                    return false;
                }
            };*/

    private void fabActions() {
        try {
            //boolean liveStreamingEnabledForAstrosage = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanag
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(DetailedHoroscope.this, DETAILED_HOROSCOPE_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(DetailedHoroscope.this, DETAILED_HOROSCOPE_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(DetailedHoroscope.this);
            }
        } catch (Exception e) {
            //
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
                FontUtils.changeFont(DetailedHoroscope.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                v.setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    private void setBottomNavigationText() {
        // find MenuItem you want to change
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);
        TextView navLive = navView.findViewById(R.id.txtViewLive);

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if (isAIChatDisplayed) {
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.nav_call_icons);
        }
        // set new title to the MenuItem
        CUtils.handleFabOnActivities(this, fabOutputMaster, navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(DetailedHoroscope.this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        //navView.getMenu().setGroupCheckable(0,false,true);
        //setting click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    @Override
    protected void onDestroy() {
        sendClosePipBroadcastMessage(this);
        super.onDestroy();
    }

    // end dynamic horscope notification measurment

    @Override
    public void clickCall() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(DetailedHoroscope.this, DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(DetailedHoroscope.this, DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    private void getLiveAstrologerList() {
        boolean liveStreamingEnabledForAstrosage = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, false);
        if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanager
            return;
        }
        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            //Log.e("SAN PF LiveAstro URL1=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GET_LIVE_ASTRO_URL,
//                    DetailedHoroscope.this, false, com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroParams(this, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(this)), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
//            queue.add(stringRequest);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getAstrologerLiveList(com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroParams(this, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(this)));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String myResponse = response.body().string();
                        com.ojassoft.astrosage.varta.utils.CUtils.setApiLastHitTime();
                        com.ojassoft.astrosage.varta.utils.CUtils.saveLiveAstroList(myResponse);
                        if (!TextUtils.isEmpty(myResponse)) {
                            parseLiveAstrologerList(myResponse);
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

    @Override
    public void onResponse(String response, int method) {
        //Log.e("SAN PreFetch Res=>", response);
        if (response != null && response.length() > 0) {
            if (method == FETCH_LIVE_ASTROLOGER) {
//                try {
//                    com.ojassoft.astrosage.varta.utils.CUtils.setApiLastHitTime();
//                    com.ojassoft.astrosage.varta.utils.CUtils.saveLiveAstroList(response);
//                    if (!TextUtils.isEmpty(response)) {
//                        parseLiveAstrologerList(response);
//                    }
//                } catch (Exception e) {
////
//                }
            }

        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    private void parseLiveAstrologerList(String liveAstroData) {
        //Log.d("testTagManager","response >>= "+liveAstroData);
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }
        try {
            if (liveAstrologerModelArrayList == null) {
                liveAstrologerModelArrayList = new ArrayList<>();
            } else {
                liveAstrologerModelArrayList.clear();
            }

            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LiveAstrologerModel liveAstrologerModel = com.ojassoft.astrosage.varta.utils.CUtils.parseLiveAstrologerObject(object);
                if (liveAstrologerModel == null) continue;

                liveAstrologerModelArrayList.add(liveAstrologerModel);
            }

            checkPermissions(liveAstrologerModelArrayList.get(0));

            com.ojassoft.astrosage.varta.utils.CUtils.parseGiftList(liveAstroData);
        } catch (Exception e) {
            liveAstrologerModelArrayList.clear();
        }
    }

    private void openLiveStreamingPipWindow() {
        /*Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_LIVE_ACTIVITY);
        intent.setPackage(BuildConfig.APPLICATION_ID);
        intent.putExtra(OPEN_LIVE_IN_PIP,true);
        startActivity(intent);*/
//        if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
//            openLoginScreen();
//            return;
//        }
//        try {
//            Intent intent = new Intent(this, LiveActivityNew.class);
//            intent.putExtra(OPEN_LIVE_IN_PIP, true);
//            startActivity(intent);
//            com.ojassoft.astrosage.varta.utils.CUtils.isPipOpenInHoroscope = true;
//        } catch (Exception e) {
//            //
//        }
    }

    private void openLoginScreen() {
        try {
            Intent intent = new Intent(currentActivity, LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            //
        }
    }
    @Override
    public void onBackPressed() {
        sendClosePipBroadcastMessage(this);
        super.onBackPressed();
    }

    @Override
    protected void onUserLeaveHint() {
        if (!isCloudAIClick) {
            sendClosePipBroadcastMessage(this);
            super.onUserLeaveHint();
        }
    }

    public void checkPermissions(LiveAstrologerModel liveAstrologerModel) {
        AstrosageKundliApplication.liveAstrologerModel = liveAstrologerModel;
        openLiveStreamingPipWindow();
//        boolean granted = true;
//        for (String per : PERMISSIONS) {
//            if (!permissionGranted(per)) {
//                granted = false;
//                break;
//            }
//        }
//
//        if (granted) {
//            openLiveStreamingPipWindow();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
//            if (requestCode == PERMISSION_REQ_CODE) {
//                boolean granted = true;
//                for (int result : grantResults) {
//                    granted = (result == PackageManager.PERMISSION_GRANTED);
//                    if (!granted) break;
//                }
//
//                if (granted) {
//                    openLiveStreamingPipWindow();
//                } else {
//                    toastNeedPermissions();
//                }
//            } else

                if (requestCode == PERMISSION_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openFilePicker();
                //CUtils.startNotificationServiceForEachHour(ActAppModule.this);
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(this, CGlobalVariables.PERMISSION_KEY_STRORAGE, true);
                }
            }
        }
    }

    private void toastNeedPermissions() {
        CUtils.showSnackbar(findViewById(android.R.id.content), getString(R.string.need_necessary_permissions), DetailedHoroscope.this);
    }

    @Override
    public void clickHome() {
        //Log.e("TestPIP", "clickHome1");
        if (com.ojassoft.astrosage.varta.utils.CUtils.isPipOpenInHoroscope) {
            com.ojassoft.astrosage.utils.CUtils.sendClosePipBroadcastMessage(this);
        } else {
            super.clickHome();
        }
    }

    private void getSuggestQuestionForHoroscopeScreens() {
        try {
            // one day caching
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date curentDate = new Date();
            String todayDate = formatter.format(curentDate);
            String getSuggestedQuestionDate = CUtils.getStringData(this, GET_SUGGESTED_QUESTION_DATE, "");
            int lang = LANGUAGE_CODE;
            String suggestedQuestionLangKey = SUGGESTED_QUESTION_KEY + "_" + lang;

            if (getSuggestedQuestionDate.equalsIgnoreCase(todayDate)) {
                String suggestedQuestion = CUtils.getStringData(this, suggestedQuestionLangKey, "");
                if (!TextUtils.isEmpty(suggestedQuestion)) { //check in local
                    parseSuggestedQuestion(suggestedQuestion, suggestedQuestionLangKey);
                    return;
                }
            }

            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getSuggestedQuestionModule(getQuestionListParams(lang));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseString = response.body().string();
                            //Log.e("TestQuestion", "onResponse: " + responseString);
                            parseSuggestedQuestion(responseString, suggestedQuestionLangKey);
                            CUtils.saveStringData(DetailedHoroscope.this, GET_SUGGESTED_QUESTION_DATE, todayDate);
                        }

                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        } catch (Exception e) {
            //
        }
    }

    private void parseSuggestedQuestion(String responseString, String suggestedQuestionLangKey) {
        try {
            if (questionMap == null) {
                questionMap = new HashMap<>();
            }
            JSONObject jsonObject = new JSONObject(responseString);
            CUtils.saveStringData(this, suggestedQuestionLangKey, jsonObject.toString());
            Iterator<String> keys = jsonObject.keys();

            Gson gson = new Gson();
            // Iterate over the keys
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> questionList = (gson.fromJson(jsonArray.toString(), listType));

                questionMap.put(key, questionList);
            }
        } catch (Exception e) {
            //
        }
    }

    private Map<String, String> getQuestionListParams(int lang) {
        Map<String, String> headers = new HashMap<>();

        headers.put("lang", String.valueOf(lang));
        headers.put("moduleid", String.valueOf(CGlobalVariables.AI_HOROSCOPE_MODULE));
        headers.put(APP_KEY, getApplicationSignatureHashCode(this));
        headers.put("methodname", "suggestedquesakmodules");

        return headers;
    }

    private ArrayList<String> getSuggestedQuestionsForScreenId(int screenId) {
        try {
            return new ArrayList<>(questionMap.get(String.valueOf(screenId)));
        } catch (Exception e) {
            return null;
        }
    }

    private String getScreenName(int position) {
        try {
            String screenName;
            if (position == 1) {
                screenName = pageTitles[position];
            } else if (position == 6) {
                screenName = getString(R.string.horoscope);
            } else {
                screenName = pageTitles[position] + " " + getString(R.string.horoscope);
            }
            return screenName;
        } catch (Exception e) {
            return "";
        }
    }

    private String getRomanScreenName(int position) {
        try {
            Resources resources = getLocaleResources(this, "en");
            String[] titles = resources.getStringArray(R.array.horoscope_tab_titles_list);
            return titles[position];
        } catch (Exception e) {
            return "";
        }
    }

}
