package com.ojassoft.astrosage.ui.fragments;

import static com.libojassoft.android.utils.LibCGlobalVariables.context;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_PLAN_PDF_PAGE_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CLICKED_CATEGORY_ENUM_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DHRUV_PLAN_PDF_PAGE_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_FIFTY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOLD_PLAN_PDF_PAGE_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.HUNDRED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_CALL_RECORDING_ENABLED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_OPENED_FROM_K_AI_CHAT_BTN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SCREEN_ID_DHRUV;
import static com.ojassoft.astrosage.utils.CGlobalVariables.TWO_HUNDRED;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity.BACK_FROM_PROFILECHATDIALOG;
import static com.ojassoft.astrosage.varta.ui.fragments.AIAstrologersFragment.getAIAstroParams;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.*;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_KUNDLI_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_HISTORY_LIST;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_SHOW_NEGATIVE_ASTRO;
import static com.ojassoft.astrosage.varta.utils.CUtils.getProfileForChatFromPreference;
import static com.ojassoft.astrosage.varta.utils.CUtils.getSelectedChannelID;
import static com.ojassoft.astrosage.varta.utils.CUtils.joinOnGoingChatOnCLick;
import static com.ojassoft.astrosage.varta.utils.CUtils.openProfileOrKundliAct;
import static com.ojassoft.astrosage.varta.utils.CUtils.parseAstrologerObject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.beans.LibOutPlace;
import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.CustomPagerAdapterForAdds;
import com.ojassoft.astrosage.customadapters.MainModulesAdapter;
import com.ojassoft.astrosage.interfaces.NavigateToAstroshopFrag;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CategorySortHelper;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.PersonalizedCategoryENUM;
import com.ojassoft.astrosage.model.AllPanchangData;
import com.ojassoft.astrosage.model.ArticleModel;
import com.ojassoft.astrosage.model.AstrologyLessonHomeAdapter;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.HomeArticlesAdapter;
import com.ojassoft.astrosage.model.HomeCategoryChatAdapter;
import com.ojassoft.astrosage.model.HomeProductAdapter;
import com.ojassoft.astrosage.model.HomeReportItemAdapter;
import com.ojassoft.astrosage.model.OthersHomeItemAdapter;
import com.ojassoft.astrosage.model.ProductCategory;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAstroSageMarriage;
import com.ojassoft.astrosage.ui.act.ActAstroShopServices;
import com.ojassoft.astrosage.ui.act.ActAstrosageTV;
import com.ojassoft.astrosage.ui.act.ActLearnAstrology;
import com.ojassoft.astrosage.ui.act.ActPorutham;
import com.ojassoft.astrosage.ui.act.ActShowOjasSoftArticlesWithTabs;
import com.ojassoft.astrosage.ui.act.ActUserPlanDetails;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.DhruvDashBoardActivity;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.NumerologyCalculatorInputActivity;
import com.ojassoft.astrosage.ui.act.indnotes.NotesActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.HeightWrappingViewPager;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customviews.basic.ScrollableGridView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CallChatAstrologerAdapter;
import com.ojassoft.astrosage.utils.HorizontalDottedProgress;
import com.ojassoft.astrosage.utils.VideoHomeAdapter;
import com.ojassoft.astrosage.varta.adapters.AkLiveAstrologerAdapter;
import com.ojassoft.astrosage.varta.adapters.LastConsultAdapter;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.service.PreFetchAstroDataservice;
import com.ojassoft.astrosage.varta.service.PreFetchLiveAstroDataservice;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.WebViewActivity;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.HorizontalSpaceItemDecoration;
import com.ojassoft.astrosage.vartalive.widgets.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

//import com.ojassoft.astrosage.misc.OnSwipeTouchListener;


/**
 * KundliModules_Frag is the main home screen fragment for the Astrosage Kundli application.
 * This fragment serves as the central hub for accessing various astrological modules and features.
 * 
 * Key Features:
 * 1. Module Navigation:
 *    - Kundli (Birth Chart)
 *    - Matching (Compatibility)
 *    - Horoscope
 *    - Prediction
 *    - Panchang (Hindu Calendar)
 *    - Ask Question
 *    - Cogni-Astro
 *    - Dhruv
 *    - Brihat Horoscope
 *    - And many more specialized modules
 * 
 * 2. Live Features:
 *    - Live Astrologer Consultation
 *    - Chat with Astrologers
 *    - AI-powered Astrology
 *    - Video Content
 * 
 * 3. Content Sections:
 *    - Daily Panchang Information
 *    - Astrological Articles
 *    - Learning Resources
 *    - Product Categories
 *    - Reports
 * 
 * 4. User Interface:
 *    - Grid-based module navigation
 *    - Tab-based content organization
 *    - Dynamic content loading
 *    - Multi-language support
 * 
 * @author OjasSoft
 * @version 1.0
 * @since 1.0
 */
public class KundliModules_Frag extends Fragment {

    static final int KUNDLI = 0;
    static final int MATCHING = 1;
    static final int HOROSCOPE = 2;
    static final int PREDICTION = 3;
    static final int PANCHANG = 4;
    static final int ASKQUESTION = 5;
    static final int COGNI_ASTRO = 6;
    static final int DHRUV = 7;
    static final int BRIHAT_HOROSCOPE = 8;

    //Typeface typeface;
    static final int NUMERLOGY_CALCULATOR = 9;
    static final int DAILY_NOTES = 10;
    static final int FREE_PDF = 11;
    static final int ASTRO_SHOP = 12;
    static final int ASTROTV = 13;
    static final int MAGZINE = 14;
    static final int KP_SYSTEM = 15;
    static final int LAL_KITAB = 16;
    static final int VARSHFAL = 17;
    static final int LEARNASTRO = 18;
    static final int PORUTHAM = 19;
    static final int MATROMONY = 20;
    static Activity activity;
    static NavigateToAstroshopFrag navigateToAstroshopFrag;
    // public CirclePageIndicator indicator;
    private static int NUM_PAGES = 0;
    private static int currentPage = 0;
    private final String prefSaveCities = "SAVE_CITIES_PREF";
    private final String keySaveCities = "SAVE_CITIES_KEY";
    public Integer[] moduleIconListAbove = new Integer[]{
            R.drawable.ic_kundali,
            R.drawable.ic_matching,
            R.drawable.ic_horoscope,
            R.drawable.ic_predection
    };
    public Integer[] moduleIconListBelow = new Integer[]{
            R.drawable.ic_panchang,
            R.drawable.ic_astro_ask_question,
            R.drawable.icon_cogni_astro,
            R.drawable.icon_dhruv_plan,
            R.drawable.icon_brihat_home,
            R.drawable.n_calculator,
            R.mipmap.ic_notes_orange,
            R.drawable.free_pdf,
            R.drawable.ic_astroshop,
            R.drawable.ic_astrotv,
            R.drawable.ic_magazine,
            R.drawable.ic_kp,
            R.drawable.ic_lalkitab,
            R.drawable.ic_varshfal,
            R.drawable.ic_lastro,
            R.drawable.ic_pourtham,
            R.drawable.ic_marriage
    };

    public Integer[] moduleIconListForCategoryChat = new Integer[]{
            R.drawable.ic_weekly_love,
            R.drawable.ic_career,
            R.drawable.ic_marriage_knot,
            R.drawable.ic_health,
            R.drawable.ic_book_stack,
            R.drawable.ic_business,
            R.drawable.stock_market,
            R.drawable.ic_legal
    };


    // add for showimg custom adds
    public HeightWrappingViewPager mViewPageradd;
    TabLayout tabLayout;
    MainModulesAdapter adapterAbove;
//    MainModulesAdapter adapterBelow;
    OthersHomeItemAdapter adapterBelow;
    // static final int ASKQUESTION = 15;
    ScrollableGridView gridViewAbove;
//    ScrollableGridView gridViewBelow;
    RecyclerView rvCategoryChat;
    //ScrollView scrollview;
    Typeface typeface;
    // for bottom screen
    CustomProgressDialog pd = null;
    AdData topAdData, bottomAdData;
    FrameLayout frameLay;
    TextView btnTalk, btnChat, tvShare;
    RelativeLayout btnCallRL, btnChatRL;
    AajKaPanchangCalulation todayCalculation;
    AajKaPanchangModel todayModel;
    AajKaPanchangCalulation yesterdayCalculation;
    AajKaPanchangModel yesterdayModel;
    AajKaPanchangCalulation tomorrowCalculation;
    AajKaPanchangModel tomorrowModel;
    BeanDateTimeForPanchang beanDateTime;
    BeanDateTimeForPanchang yesterdaybeanDateTime;
    BeanDateTimeForPanchang tomorrowbeanDateTime;
    BeanPlace beanPlace = new BeanPlace();
    String language = "";
    int[] moonImages = {
            R.drawable.moon_light_dark_14,
            R.drawable.moon_light_dark_13,
            R.drawable.moon_light_dark_12,
            R.drawable.moon_light_dark_11,
            R.drawable.moon_light_dark_10,
            R.drawable.moon_light_dark_9,
            R.drawable.moon_light_dark_8,
            R.drawable.moon_light_dark_7,
            R.drawable.moon_light_dark_6,
            R.drawable.moon_light_dark_5,
            R.drawable.moon_light_dark_4,
            R.drawable.moon_light_dark_3,
            R.drawable.moon_light_dark_2,
            R.drawable.moon_light_dark_1,
            R.drawable.ic_moon_light,
            R.drawable.moon_light_14,
            R.drawable.moon_light_13,
            R.drawable.moon_light_12,
            R.drawable.moon_light_11,
            R.drawable.moon_light_10,
            R.drawable.moon_light_9,
            R.drawable.moon_light_8,
            R.drawable.moon_light_7,
            R.drawable.moon_light_6,
            R.drawable.moon_light_5,
            R.drawable.moon_light_4,
            R.drawable.moon_light_3,
            R.drawable.moon_light_2,
            R.drawable.moon_light_1,
            R.drawable.ic_moon_dark
    };
    String lat = "0";
    String lng = "0";
    String timeZone = "0";
    String timeZoneString = "";
    private String[] moduleNameList;
    private String[] moduleNameListAbove;
    private String[] moduleNameListBelow;
    private String[] moduleNameListForCategoryChat;

    private CustomPagerAdapterForAdds mPagerAdapter;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Handler handler;
    private Runnable Update;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private NetworkImageView adImage;
    private String IsShowBanner1 = "False";
    private TextView mTithi, mTithiDay, mTithiStartTime, mTithiEndTime, mTithiName, mTithiDayName, mTithiDate, mTithiPaksha, mTithiFestival, chatBoxTv;
    private LinearLayout mTithiLL;
    private String cityId = "1261481";
    private RequestQueue queue;
    private ImageView mTithiImage, astroLiveIcon;
    private int selectedYear;
    private String langCode = null;
    private RecyclerView liveRecyclerView, rvHistoryKMF;
    private AkLiveAstrologerAdapter liveAstrologerAdapter;
    private CallChatAstrologerAdapter callChatAstrologerAdapter;
    private CallChatAstrologerAdapter aiAstrologerAdapter;
    private VideoHomeAdapter videoHomeAdapter;
    private AstrologyLessonHomeAdapter learnVideoAdapter;
    private HomeProductAdapter homeProductAdapter;
    private HomeReportItemAdapter homeReportItemAdapter;
    private HomeArticlesAdapter homeArticlesAdapter;

    HomeCategoryChatAdapter homeCategoryChatAdapter;
    private RelativeLayout liveAstroTitle, astroTitle, rl_history_section,videoLayout, learnAstroLayout,productLayout, reportLayout, articleLayout,aiAstrologerLayout;
    private ShimmerFrameLayout shimmerFrameLayout, shimmerLayoutLive;
    private FrameLayout seeMoreAstrologer, btnViewAllNow, see_more_history,seeMoreAIAstrologer, seeMoreVideo, seeMoreLearnVideo,seeMoreProductCategory, btnViewAllreport, btnViewAllArticles;
    private BroadcastReceiver liveAstroReceiver;
    private BroadcastReceiver aiAstrologerReceiver;
    private ImageView ivRefresh, ivRefreshAstro;
    private HorizontalDottedProgress horizontalDottedProgress, horizontalDottedProgressAstro;

    private ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList, liveAstrologerModelArrayListFinal;
    private ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList, aiAstroDetailBeanList;
    private ArrayList<YoutubeVideoBean> youtubeVideoBeans;
    private ArrayList<SliderModal> sliderModels;
    private ArrayList<ArticleModel> articleList;
    private List<ProductCategory> productCategoryList;
    private ArrayList<CallHistoryBean> callHistoryList, callHistoryListFinal;
    private ArrayList<ServicelistModal> reportCategoryList;

    private String astroUrlForLiveJoin;
    private LastConsultAdapter lastConsultAdapter;
    int prevLiveAstroSize = 0;
    RelativeLayout kundliAIChatBtnLayout;
    RelativeLayout aiAstroLayout;


    private final BroadcastReceiver questionReadyReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (ActAppModule.SuggestedQuestionBroadcast.ACTION_QUESTION_READY
                            .equals(intent.getAction())) {
                        // Questions are ready —
                        switchRandomQuestionFromScreens();
                    }
                }
            };

    private static void firebaseAnalytics(String label, String event) {
        CUtils.fcmAnalyticsEvents(CUtils.replaceSpaceDashFromLabelFcmEventName(label), event, "");
    }

    /**
     * @param position
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     */
    public static void callActivity(int position) {
        int moduleType = -1;

        switch (position) {
            case KUNDLI:
                moduleType = CGlobalVariables.MODULE_BASIC;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_BASIC, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_BASIC, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                break;
            case MATCHING:
                moduleType = CGlobalVariables.MODULE_MATCHING;

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_MATCH, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_MATCH, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case HOROSCOPE:
                moduleType = CGlobalVariables.MODULE_HOROSCOPE;

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_HORSCOPE, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HORSCOPE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case PREDICTION:
                moduleType = CGlobalVariables.MODULE_PREDICTION;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_PRDICTION,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_PRDICTION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case PANCHANG:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_DASHBOARD;

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case KP_SYSTEM:
                moduleType = CGlobalVariables.MODULE_KP;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_KP, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_KP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case LAL_KITAB:
                moduleType = CGlobalVariables.MODULE_LALKITAB;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_LALKITAB, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_LALKITAB, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case VARSHFAL:
                moduleType = CGlobalVariables.MODULE_VARSHAPHAL;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_Varshphal, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_Varshphal, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case PORUTHAM:
                moduleType = CGlobalVariables.MODULE_PORUTHAM;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_PORUTHAM, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PORUTHAM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case ASTROTV:
                moduleType = CGlobalVariables.MODULE_ASTROTV;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROTV, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROTV, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case MAGZINE:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_ARTICLES;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_ARTICLES, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_ARTICLES, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case ASTRO_SHOP:
                moduleType = CGlobalVariables.MODULE_ASTRO_SHOP;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTRO_SHOP, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTRO_SHOP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
         /*   case HORA:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_HORA;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_HORA,
                        null);
                break;*/
           /* case CHOGADIA:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_CHOGADIA,
                        null);
                break;*/
            case MATROMONY:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_MARRIAGE, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_MARRIAGE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case ASKQUESTION:
                moduleType = CGlobalVariables.MODULE_ASTROSAGE_ASKQUESTION;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_ASKAQUESTION, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_ASKAQUESTION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;

            case LEARNASTRO:
                moduleType = CGlobalVariables.MODULE_LEARN_ASTRO;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_LEARNASTROLOGY, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSAHE_LEARNASTROLOGY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case NUMERLOGY_CALCULATOR:
                moduleType = CGlobalVariables.MODULE_NUMERLOGY_CALCULATOR;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_NUMEROLOGY_CALCULATOR, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_NUMEROLOGY_CALCULATOR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case DAILY_NOTES:
                moduleType = CGlobalVariables.MODULE_DAILY_NOTES;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_DAILY_NOTES, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DAILY_NOTES, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
            case FREE_PDF:
                moduleType = CGlobalVariables.MODULE_FREE_PDF;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_FREE_PDF, null);

                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_FREE_PDF,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;

            case COGNI_ASTRO:
                moduleType = CGlobalVariables.MODULE_COGNIASTRO;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_VARTA, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_VARTA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                break;
            case DHRUV:
                moduleType = CGlobalVariables.MODULE_DHRUV;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                break;

            case BRIHAT_HOROSCOPE:
                moduleType = CGlobalVariables.MODULE_BRIHAT_HOROSCOPE;
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                        CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                break;
        }

        gotoSelectedModule(moduleType);

    }

    /**
     * get Replace String
     *
     * @return
     */
    private static String getReplaceString() {
        String replaceString = "";

        switch (CUtils.getCurrentPlanCustomReturnString(CUtils.getUserPurchasedPlanFromPreference(activity))) {
            case BASIC_PLAN_PDF_PAGE_COUNT:
                replaceString = FREE_FIFTY;
                break;
            case GOLD_PLAN_PDF_PAGE_COUNT:
                replaceString = HUNDRED;
                break;
            case DHRUV_PLAN_PDF_PAGE_COUNT:
                replaceString = TWO_HUNDRED;
                break;
        }
        return replaceString;
    }

    /**
     * @param moduleType
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     */
    private static void gotoSelectedModule(int moduleType) {

        if (moduleType == CGlobalVariables.MODULE_MATCHING) {
            Intent intent = new Intent(activity,
                    HomeMatchMakingInputScreen.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTRO_SHOP) {
            /*Intent intent = new Intent(activity,
                    ActAstroShopCategories.class);
            activity.startActivity(intent);*/
            navigateToAstroshopFrag.navigateToAstroshopFrag();
        } else if (moduleType == CGlobalVariables.MODULE_ASTROTV) {

            Intent intent = new Intent(activity, ActAstrosageTV.class);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_ARTICLES) {
            Intent intent = new Intent(activity,
                    ActShowOjasSoftArticlesWithTabs.class);
           /* Intent intent = new Intent(activity,
                    ActShowOjasSoftArticles.class);*/
            intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_HOROSCOPE) {
           /* Intent intent = new Intent(activity,
                    HoroscopeHomeActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);*/
            CUtils.callHoroscopeActivity(activity, moduleType, 0, 0);

        } else if (moduleType == CGlobalVariables.MODULE_PORUTHAM) {
            Intent intent = new Intent(activity, ActPorutham.class);
            intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_DASHBOARD) {
            // Intent intent = new Intent(ActAppModule.this, ActPanchang.class);
            Intent intent = new Intent(activity,
                    InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_HORA) {
            Intent intent = new Intent(activity,
                    InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA) {
            Intent intent = new Intent(activity,
                    InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE) {
            Intent intent = new Intent(activity, ActAstroSageMarriage.class);
            intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_ASTROSAGE_ASKQUESTION) {

            // Intent intent = new Intent(activity, ActAskQuestion.class);
            // activity.startActivity(intent);
            /*Intent intent = new Intent(activity, ActAsKqquestionDescription.class);
            activity.startActivity(intent);*/
            /*Intent intent = new Intent(activity, ActAstroPaymentOptions.class);
            activity.startActivity(intent);*/
        /*    Intent intent = new Intent(activity, ActAskQuestion.class);
            BeanHoroPersonalInfo beanHoroPersonalInfo = null;
            intent.putExtra("BeanHoroPersonalInfo1", beanHoroPersonalInfo);
            intent.putExtra("BeanHoroPersonalInfo2", beanHoroPersonalInfo);
            intent.putExtra("callingActivity", "ActAppModule");
            activity.startActivity(intent);*/
            CUtils.sendToActAskQuestion(activity, CGlobalVariables.ask_A_Question_Android);


        } else if (moduleType == CGlobalVariables.MODULE_LEARN_ASTRO) {
            Intent intent = new Intent(activity, ActLearnAstrology.class);
            activity.startActivity(intent);
       /*     Intent intent = new Intent(activity, BigHorscopeActivity.class);
            activity.startActivity(intent);*/
        } else if (moduleType == CGlobalVariables.MODULE_NUMERLOGY_CALCULATOR) {
            Intent intent = new Intent(activity, NumerologyCalculatorInputActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_DAILY_NOTES) {
            Intent intent = new Intent(activity, NotesActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_FREE_PDF) {
            Intent intent = new Intent(activity, HomeInputScreen.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
            //Intent intent = new Intent(activity, PopularCityActivity.class);
            //activity.startActivity(intent);
        } else if (moduleType == CGlobalVariables.MODULE_COGNIASTRO) {
            if (activity instanceof ActAppModule) {
                ((ActAppModule) activity).switchToConsultTab(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT);
            }
        } else if (moduleType == CGlobalVariables.MODULE_DHRUV) {
            int planId = CUtils.getUserPurchasedPlanFromPreference(activity);
             if (CUtils.isDhruvPlan(activity)) {
                activity.startActivity(new Intent(activity, DhruvDashBoardActivity.class));
            } else if (planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                 activity.startActivity(new Intent( new Intent(activity, ActUserPlanDetails.class)));
            } else{
                     CUtils.gotoProductPlanListUpdated(activity, ActAppModule.LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, SCREEN_ID_DHRUV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_HOME_KUNDLI_AI_PLUS_ICON);
             }
        } else if (moduleType == CGlobalVariables.MODULE_BRIHAT_HOROSCOPE) {
            CUtils.getUrlLink(ActAppModule.brihatHorscopeDeepLinkUrl, activity, ActAppModule.LANGUAGE_CODE, 0);
        } else {
            Intent intent = new Intent(activity, HomeInputScreen.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActAppModule)
            KundliModules_Frag.activity = activity;
        else
            KundliModules_Frag.activity = activity;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }


    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "0");
                bottomAdData = CUtils.getSlotData(adList, "1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable questionRunnable;

    /**
     * Sets up the fragment's view and initializes UI components.
     * This method:
     * 1. Inflates the layout
     * 2. Initializes view references
     * 3. Sets up click listeners
     * 4. Configures adapters
     * 
     * @param inflater LayoutInflater for inflating views
     * @param container Parent view group
     * @param savedInstanceState Saved instance state
     * @return The inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kundli_frag_file, container, false);

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);


        setLayRef(rootView);
        startQuestionRotaion();
        initClickListner();
        navigateToAstroshopFrag = (NavigateToAstroshopFrag) activity;
        /* calling varta for call chat astrologer and live and recent consultation history api calling */
        callVartaApis();
        getDataFromServerOrLocal();
        /*--------------*/
        checkConnectFreeCallChat();
        return rootView;
    }

    private void startQuestionRotaion() {
        if (handlerForTransition == null) {
            handlerForTransition = new Handler();
        }
        questionRunnable = new Runnable() {
            @Override
            public void run() {
                // This runs every 4 seconds
                updateQuestionWithAnimation();
                handlerForTransition.postDelayed(this, 4000);
            }
        };
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void callVartaApis() {
        String liveAstroData = com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroList();
        if (!TextUtils.isEmpty(liveAstroData)) {
           // Log.e(TAG, "callVartaApis: live data from cache" );
            parseLiveAstrologerList(liveAstroData);
        }
        String response = com.ojassoft.astrosage.varta.utils.CUtils.getAstroListWithLimit();
        if (!TextUtils.isEmpty(response)) {
           // Log.e(TAG, "callVartaApis: astro data from cache" );
            parseAstrologerList(response);
        }
        getLastConsultList();

        if ((!TextUtils.isEmpty(liveAstroData)) && (!TextUtils.isEmpty(response)) ) {
            if ((com.ojassoft.astrosage.varta.utils.CUtils.getCurrentTimeStamp() - com.ojassoft.astrosage.varta.utils.CUtils.getApiLastHitAkHomeTime()) > (30 * 1000)) {
                // Log.e(TAG, "callVartaApis: live and astro data from server as chaace time passed" );
                com.ojassoft.astrosage.varta.utils.CUtils.setApiLastHitAkHomeTime();
                getLiveAstrologerListFromServer();
                getCallChatAstrologerListFromServer();
                getLastConsultListFromServer();

            }
        } else {
            // Log.d("TestVartaAkHome","Called Api due to no cache");
//            Log.e(TAG, "callVartaApis: live and astro data from server" );
            getLiveAstrologerListFromServer();
            getCallChatAstrologerListFromServer();
            getLastConsultListFromServer();
        }
    }

    /**
     * Parses the AI astrologer list from JSON response and updates the UI.
     * This method handles both the initial list population and notification-based astrologer selection.
     * 
     * The method performs the following operations:
     * 1. Initializes or clears the AI astrologer list
     * 2. Parses the JSON response containing astrologer details
     * 3. Updates the adapter with the parsed data
     * 4. Handles notification-based astrologer selection if applicable
     *
     * @param aiAstrologerList JSON string containing the list of AI astrologers
     * @throws RuntimeException if JSON parsing fails
     */
    private void parseAIAstrologerList(String aiAstrologerList) {
        try {
            // Initialize or clear the AI astrologer list
            if (aiAstroDetailBeanList == null) {
                aiAstroDetailBeanList = new ArrayList<>();
            } else {
                aiAstroDetailBeanList.clear();
            }
            aiAstroLayout.setVisibility(View.VISIBLE);

            // Process the astrologer list if not empty
            if (!TextUtils.isEmpty(aiAstrologerList)) {
                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(aiAstrologerList);
                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");

                // Process up to 20 astrologers from the list
                for (int i = 0; i < Math.min(jsonArray.length(), 20) ; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    AstrologerDetailBean astrologerDetailBean = parseAstrologerObject(object);
                    aiAstroDetailBeanList.add(astrologerDetailBean);
                }

                hideAiAstrologer();
                // Update the adapter with new data
                if (aiAstrologerAdapter != null)
                     aiAstrologerAdapter.notifyDataSetChanged();

                AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n KundliModules_Frag AINotificationAstroId : "+CUtils.AINotificationAstroId;

                // Handle notification-based astrologer selection
                if (!TextUtils.isEmpty(CUtils.AINotificationAstroId)) {
                    boolean astrologerFound = false;
                    AstrologerDetailBean astrologerDetailBean = null;

                    // Search for the specific astrologer from notification
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        astrologerDetailBean = parseAstrologerObject(object);
                        String aiAstroId = astrologerDetailBean.getAiAstrologerId();
                        
                        // Check if this is the astrologer from the notification
                        if (!TextUtils.isEmpty(aiAstroId) && !aiAstroId.equals("0") && 
                            aiAstroId.equals(CUtils.AINotificationAstroId)) {
                            astrologerFound = true;
                            break;
                        }
                    }

                    // Open chat with the selected astrologer if found
                    if (astrologerFound) {
                        // if User is not login send to LoginSignupActivity
                        if(com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(requireContext())) {
                            openNotificationInChat(astrologerDetailBean);
                        }else{
                            Intent intent = new Intent(requireContext(), LoginSignUpActivity.class);
                            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN,LANGUAGE_SELECTION_SCRREN);
                            startActivity(intent);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a chat session with an AI astrologer based on notification data.
     * This method handles different scenarios for initiating chat:
     * 1. If a chat service is already running, it checks if we can join the existing chat
     * 2. If no chat service is running, it prepares to open a new chat window
     * 
     * The method performs the following operations:
     * - Checks if chat service is running
     * - Validates the AI astrologer ID
     * - Handles existing chat sessions
     * - Prepares chat parameters for new sessions
     * - Manages chat window opening
     *
     * @param astrologerDetailBean The astrologer details for the chat session
     */
    private void openNotificationInChat(AstrologerDetailBean astrologerDetailBean) {
        AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n KundliModules_Frag openNotificationInChat : "+astrologerDetailBean.getAiAstrologerId();

        // Check if chat service is already running
        if (com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(OnGoingChatService.class)) {
            // Get current AI astrologer ID
            String aiai = AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId();
            AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n KundliModules_Frag openNotificationInChat selected aiai: "+aiai;

            // Validate AI astrologer ID
            if (!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                // Check if AI astrologer is not already in chat
                if (!CUtils.isAIAstrologerOnline) {
                    // Prepare chat parameters
                    String CHANNEL_ID = getSelectedChannelID(activity);
                    String chatJsonObject = AstrosageKundliApplication.chatJsonObject;
                    String astrologerName = AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
                    String astrologerProfileUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();
                    String astrologerId = AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId();
                    String userChatTime = CUtils.changeToMinSec(chatTimerTime);

                    // Join ongoing chat with prepared parameters
                    joinOnGoingChatOnCLick(activity, CHAT_INITIATE_TYPE_ASTROLOGER_AI, CHANNEL_ID, 
                        chatJsonObject, astrologerName, astrologerId, astrologerProfileUrl, 
                        userChatTime, CUtils.AINotificationQuestion);
                } else {
                    // Show message if already in chat
                    Toast.makeText(activity, getString(R.string.already_in_chat), Toast.LENGTH_SHORT).show();
                }
            } else {
                AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n KundliModules_Frag openChatNotificationWindow()1";
                // Open chat notification window if AI astrologer ID is invalid
                openChatNotificationWindow();
            }
        } else {
            // Set selected astrologer and open chat window if no service is running
            AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;
            AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n KundliModules_Frag openChatNotificationWindow()2";
            openChatNotificationWindow();
        }
    }

    /**
     * Opens the AI chat notification window based on user profile status.
     * This method handles two scenarios:
     * 1. If user profile exists and is valid, opens the AI notification chat window
     * 2. If user profile is missing or invalid, redirects to profile/kundli activity
     * 
     * The method performs the following operations:
     * - Checks user profile existence and validity
     * - Opens appropriate chat window or redirects to profile setup
     * - Handles AI chat parameters and online status
     */
    private void openChatNotificationWindow() {
        // Check if user profile exists and has a valid name
        if (getProfileForChatFromPreference(activity) != null && 
            !TextUtils.isEmpty(getProfileForChatFromPreference(activity).getName())) {
            // Open AI notification chat window with current parameters
            CUtils.openAINotificationChatWindow(
                activity, 
                CUtils.AINotificationQuestion, 
                CUtils.AIRevertQCount, 
                CUtils.AINotificationAstroId, 
                CUtils.AINotificationTitle, 
                CUtils.isAIAstrologerOnline
            );
        } else {
            // Redirect to profile/kundli activity if profile is missing
            openProfileOrKundliAct(activity, OPEN_DUMMY_CHAT_WINDOW, TYPE_AI_CHAT, 2001);
        }
    }

    /*this is used to get list of astrologer who is available on call or chat or both call and chat */
    private void getLiveAstrologerListFromServer() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> apiCall = api.getAstrologerLiveList(com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroParams(activity, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(activity)));
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        com.ojassoft.astrosage.varta.utils.CUtils.setApiLastHitTime();
                        com.ojassoft.astrosage.varta.utils.CUtils.getApiLastHitAkHomeTime();
                        String myResponse = response.body().string();
                        if (myResponse != null && !myResponse.trim().isEmpty()) {
                            com.ojassoft.astrosage.varta.utils.CUtils.saveLiveAstroList(myResponse);
                            getLiveAstrologerList();
                        } else {
                            stopLiveAstroLoading();
                        }

                    } else {
                        stopLiveAstroLoading();
                    }
                } catch (Exception e) {
                    stopLiveAstroLoading();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                stopLiveAstroLoading();
            }
        });

    }

    /*this is used to get list of astrologer who is available on call or chat or both call and chat */
    private void getCallChatAstrologerListFromServer() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> apiCall = api.getAstrologerListMiniList(getCallChatAstroParams());
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has("astrologers") && jsonObject.getJSONArray("astrologers").length() > 0) {
                            com.ojassoft.astrosage.varta.utils.CUtils.saveAstroListWithLimit(jsonObject.toString());
                            getAstrologerList();
                        } else {
                            stopCallChatLoading();
                        }
                    } else {
                        stopCallChatLoading();
                    }
                } catch (Exception e) {
                    stopCallChatLoading();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                stopCallChatLoading();
            }
        });
    }

    /**
     * Stops the live astrologer loading indicators and exposes the refresh action.
     */
    private void stopLiveAstroLoading() {
        try {
            if (shimmerLayoutLive != null) {
                shimmerLayoutLive.hideShimmer();
                shimmerLayoutLive.setVisibility(View.GONE);
            }
            if (horizontalDottedProgress != null) {
                horizontalDottedProgress.clearAnimation();
                horizontalDottedProgress.setVisibility(View.GONE);
            }
            if (ivRefresh != null) {
                ivRefresh.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Stops the call/chat astrologer loading indicators and exposes the refresh action.
     */
    private void stopCallChatLoading() {
        try {
            if (shimmerFrameLayout != null) {
                shimmerFrameLayout.hideShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
            if (horizontalDottedProgressAstro != null) {
                horizontalDottedProgressAstro.clearAnimation();
                horizontalDottedProgressAstro.setVisibility(View.GONE);
            }
            if (ivRefreshAstro != null) {
                ivRefreshAstro.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            //
        }
    }

    private void getAIAstrologersListFromServer() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAIAstrologerList(getAIAstroParams(activity));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.e("TestAIChat", "response=" + response);
                try {
                    if (response.body() != null) {
                        if (activity == null) return;
                        String myResponse = response.body().string();
//                        Log.e("TestAIChat", "myResponse=" + myResponse);
                        CUtils.saveStringData(activity,com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_LIST,myResponse);
                        parseAIAstrologerList(myResponse);
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (activity == null || getActivity() == null) return;
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     *
     */
    private void getLastConsultListFromServer() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> apiCall = api.getRecentHistory(getParamsNew());
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        try {

                            String myResponse = response.body().string();
                            //Log.e("responseCheck", "MainScreen onResponse: "+myResponse );
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if (jsonObject.has("status")) {
                                if (jsonObject.getString("status").equalsIgnoreCase("100")) {
                                    startBackgroundLoginService();
                                }
                            } else {
                                String isCallRecordingEnabled = jsonObject.getString("isCallRecordingEnabled");
                                CUtils.saveBooleanData(activity, IS_CALL_RECORDING_ENABLED_KEY, isCallRecordingEnabled.equals("true"));
                                com.ojassoft.astrosage.varta.utils.CUtils.saveHistoryList(myResponse);
                                getLastConsultList();
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private boolean flag = true;

    private void startBackgroundLoginService() {
        try {
            if (flag) {
                flag = false;
                LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
                    Intent intent = new Intent(activity, Loginservice.class);
                    activity.startService(intent);
                }
            } else {
                flag = true;
            }
        } catch (Exception e) {
            //
        }
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                getLastConsultListFromServer();
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiverBackgroundLoginService);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (handler != null && Update != null && mPagerAdapter != null && mViewPageradd != null) {
            handler.postDelayed(Update, 3000);
        }
        if(randomQuestion!=null){
            chatBoxTv.setText(randomQuestion);
        }
        startQuestionTransition();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null && Update != null) {
            handler.removeCallbacks(Update);
        }
        stopQuestionTransition();
    }

    private void initClickListner() {
        seeMoreAstrologer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllLiveAstrologerActivity.class);
                startActivity(intent);
            }
        });
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intentService = new Intent(getContext(), PreFetchLiveAstroDataservice.class);
                    intentService.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_SOURCE, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(getContext()));
                    getContext().startService(intentService);
                    horizontalDottedProgress.startAnimation();
                    horizontalDottedProgress.setVisibility(View.VISIBLE);
                    ivRefresh.setVisibility(View.GONE);
                } catch (Exception e) {
                    //
                }
            }
        });
        ivRefreshAstro.setOnClickListener(view -> {
            try {
                Intent intentService = new Intent(getContext(), PreFetchAstroDataservice.class);
                getContext().startService(intentService);
                horizontalDottedProgressAstro.startAnimation();
                horizontalDottedProgressAstro.setVisibility(View.VISIBLE);
                ivRefreshAstro.setVisibility(View.GONE);
            } catch (Exception e) {
                //
            }
        });
        adImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_1_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_1_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S1");


                CustomAddModel modal = bottomAdData.getImageObj().get(0);

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);
            }
        });

        mTithiLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG_FROM_CARD, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG_FROM_CARD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                gotoSelectedModule(CGlobalVariables.MODULE_ASTROSAGE_PANCHANG);
            }
        });

        btnCallRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOME_TALK_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.HOME_PAGE_BOTTOM_CALL_BTN_PARTNER_ID);
                    if (activity instanceof ActAppModule) {
                        if (btnTalk.getText().toString().equalsIgnoreCase(getString(R.string.free_call))){
                            String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(activity);
                            String firstFreeCallType = CUtils.getStringData(activity,CGlobalVariables.FIRST_FREE_CALL_TYPE,"");
                            if(!TextUtils.isEmpty(offerType) && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                                if(!com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(activity)){
                                    if(firstFreeCallType.equals(CGlobalVariables.TYPE_AI_CALL)){
                                        ChatUtils.getInstance(activity).initAICallRandomAstrologer(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_FREE_CALL_BUTTON,CGlobalVariables.TYPE_AI_CALL);
                                    }else{
                                        ((ActAppModule) activity).switchToConsultTab(FILTER_TYPE_CHAT);//redirect to Human list
                                    }
                                }
                            }
                        }else {
                            ((ActAppModule) activity).switchToConsultTab(FILTER_TYPE_CALL);//redirect to AI list
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }
        });

        btnChatRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOME_CHAT_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.HOME_PAGE_BOTTOM_CHAT_BTN_PARTNER_ID);
                    if (activity instanceof ActAppModule) {
                        if (btnChat.getText().toString().equalsIgnoreCase(getString(R.string.free_chat))) {
                            freeChatFunctionality();
                        } else {
                            ((ActAppModule) activity).switchToConsultTab(FILTER_TYPE_CALL);//redirect to AI list
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }
        });
        btnViewAllNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOME_CHAT_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.HOME_PAGE_ASTRO_VIEW_ALL_PARTNER_ID);
                    if (activity instanceof ActAppModule) {
                        ((ActAppModule) activity).switchToConsultTab(FILTER_TYPE_CHAT);
                    }
                } catch (Exception e) {
                    //
                }
            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.APP_SHARE_APP_WITH_FRIEND, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.shareWithFriends(activity);

                } catch (Exception e) {
                    //
                }
            }
        });

        see_more_history.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ConsultantHistoryActivity.class));
        });

        kundliAIChatBtnLayout.setOnClickListener(v -> {
            handleKundliAiChatBtnClick();
        });

        seeMoreAIAstrologer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActAppModule) activity).switchToConsultTab(FILTER_TYPE_CALL);//redirect to AI list
            }
        });

        seeMoreVideo.setOnClickListener(v -> ((ActAppModule) activity).navigateToVideoTab());

        seeMoreLearnVideo.setOnClickListener(v -> {
            Intent tvIntent = new Intent(activity, ActLearnAstrology.class);
            startActivity(tvIntent);

        });

        seeMoreProductCategory.setOnClickListener(v -> {
            if(LANGUAGE_CODE < 2 ) {
                ((ActAppModule) activity).mViewPager.setCurrentItem(2);
            }else{
                ((ActAppModule) activity).mViewPager.setCurrentItem(1);
            }
        });

        btnViewAllreport.setOnClickListener(v -> {
            Intent i = new Intent(activity, ActAstroShopServices.class);
            i.putExtra(CGlobalVariables.SOUCRE_ACTIVITY, "Home_All_report");
            activity.startActivity(i);
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        });

        btnViewAllArticles.setOnClickListener(v->{
            String allArticleURL = "https://horoscope.astrosage.com/";
            if (LANGUAGE_CODE == 1) {
                allArticleURL = allArticleURL + "hindi/";
            }
            HomeArticlesAdapter.addParamsForDarkInURL(activity,allArticleURL);
          //  Log.e("mytag", "initClickListner: "+allArticleURL );
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("URL", allArticleURL);
            intent.putExtra("TITLE_TO_SHOW", activity.getString(R.string.all_articles));
            activity.startActivity(intent);
        });


    }

    /**
     * This listener orchestrates the user flow with the following logic:
     * 1.  Logs an analytics event for tracking the button click.
     * 2.  Checks if the user is currently logged into an AstroSage account.
     * 3.  If the user is LOGGED IN:
     *     a. It retrieves the currently selected user profile from preferences.
     *     b. If a valid profile exists (not null and has a name), it opens the Kundli AI chat
     *        screen with a randomly selected category. It includes a fallback to the 'LOVE'
     *        category if parsing the random screen fails.
     *     c. If no valid profile is found, it redirects the user to the profile creation/selection
     *        screen, passing along the context that the user's original goal was to use
     *        the Kundli AI chat.
     * 4.  If the user is NOT LOGGED IN:
     *     a. It sets flags to indicate a login pop-up is being shown.
     *     b. It redirects the user to the FlashLoginActivity to log in or sign up.
     * 5.  A try-catch block wraps the entire logic to prevent the app from crashing due to any
     *     unexpected exceptions.
     */
    private void handleKundliAiChatBtnClick() {
        // Log the button click for analytics and tracking purposes.
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_AI_HOME_CHAT_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        try {
            // First, check if the user is logged into an AstroSage account.
            if (CUtils.isUserLogedIn(activity)) {
                // User is logged in. Now, get their selected Kundli profile.
                UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(getContext());

                // Check if a valid profile with a name has been selected.
                if (userProfileData != null && !TextUtils.isEmpty(userProfileData.getName())) {
                    // A valid profile exists, proceed to open the Kundli AI screen.
                    try {
                        // Attempt to open the AI screen with a randomly selected category.
                        ((ActAppModule) activity).openKundliAIScreen(PersonalizedCategoryENUM.getByScreenId(Integer.parseInt(randomScreen)), userProfileData, true);
                    }catch (Exception e){
                        // This catch block handles parsing errors if 'randomScreen' is invalid.
                        // As a fallback, open the AI screen with the default 'LOVE' category to prevent a crash.
                        ((ActAppModule) activity).openKundliAIScreen(PersonalizedCategoryENUM.LOVE, userProfileData, true);
                    }
                } else {
                    // No valid profile is selected. Redirect user to the profile screen.
                    Bundle bundle = new Bundle();
                    // Pass the intended category and a flag to remember the user's original goal.
                    bundle.putString(CLICKED_CATEGORY_ENUM_KEY,PersonalizedCategoryENUM.getByScreenId(Integer.parseInt(randomScreen)).name());
                    bundle.putBoolean(IS_OPENED_FROM_K_AI_CHAT_BTN,true);
                    // Open the profile screen, which will then redirect back to the AI chat flow.
                    com.ojassoft.astrosage.varta.utils.CUtils.openProfileForChat(activity, null, HOME_KUNDLI_CHAT, bundle, true, CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG);
                }
            } else {
                // User is not logged in. Redirect them to the login activity.
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                Intent intent1 = new Intent(activity, FlashLoginActivity.class);
                startActivity(intent1);
            }
        } catch (Exception e) {
            Log.e("userDetails", "onClick: exception :" + e);
        }
    }

    /**
     * Creates a model for Kundli AI functionality.
     * This method:
     * 1. Initializes AI model with user data
     * 2. Sets up prediction parameters
     * 3. Prepares for AI analysis
     * 
     * @param userProfileData User profile data for AI analysis
     */
    public static void createModelForKundliAI(UserProfileData userProfileData) {
        BeanHoroPersonalInfo beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        beanHoroPersonalInfo.setName(userProfileData.getName());
        BeanDateTime beanDateTime = new BeanDateTime();

//        Log.e("TAG", "createModelForKundliAI: " + beanHoroPersonalInfo.getName());
        beanDateTime.setDay(Integer.parseInt(userProfileData.getDay()));
        beanDateTime.setMonth(Integer.parseInt(userProfileData.getMonth()) - 1);
        beanDateTime.setYear(Integer.parseInt(userProfileData.getYear()));
        beanDateTime.setHour(Integer.parseInt(userProfileData.getHour()));
        beanDateTime.setMin(Integer.parseInt(userProfileData.getMinute()));
        beanDateTime.setSecond(Integer.parseInt(userProfileData.getSecond()));
        beanHoroPersonalInfo.setDateTime(beanDateTime);

        BeanPlace beanPlace1 = new BeanPlace();
        beanPlace1.setLatitude(userProfileData.getLatdeg());
        beanPlace1.setLongitude(userProfileData.getLongdeg());
        beanPlace1.setTimeZone(userProfileData.getTimezone());
        beanPlace1.setCityName(userProfileData.getPlace());
        beanHoroPersonalInfo.setPlace(beanPlace1);

        beanHoroPersonalInfo.setMaritalStatus(userProfileData.getMaritalStatus());
        beanHoroPersonalInfo.set_occupation(userProfileData.getOccupation());
//        Log.e("Mytag", "createModelForKundliAI: "+beanPlace1 );
//        Log.e("Mytag", "createModelForKundliAI: "+beanPlace1.getLatitude() );
//        Log.e("Mytag", "createModelForKundliAI: "+beanPlace1.getLongitude() );
//        Log.e("Mytag", "createModelForKundliAI: "+beanPlace1.getCityName() );
//        Log.e("Mytag", "createModelForKundliAI: "+beanHoroPersonalInfo.getMaritalStatus() );
//        Log.e("Mytag", "createModelForKundliAI: "+beanHoroPersonalInfo.get_occupation() );

        CUtils.setNumeroBeanHoroPersonalInfo(beanHoroPersonalInfo);
    }

    /**
     * Manages free chat functionality for users.
     * This method:
     * 1. Checks user's eligibility for free chat
     * 2. Handles chat initiation
     * 3. Manages chat session state
     */
    private void freeChatFunctionality() {
        try {
            boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(activity);
            String firstFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE, "");
            String secondFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");
            String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(activity);
            if (enabledAIFreeChatPopup) {
                if (com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(activity) && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    if (secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_AI_FREE_CHAT_BUTTON,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                    } else {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_FREE_CHAT_BUTTON, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                    }
                } else {
                    if (firstFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_AI_FREE_CHAT_BUTTON, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                    } else {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_FREE_CHAT_BUTTON, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                    }
                }
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_FREE_CHAT_BUTTON, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Handles the Panchang (Hindu Calendar) data display.
     * This method:
     * 1. Fetches Panchang data for today
     * 2. Updates UI with Tithi, Nakshatra, and other details
     * 3. Handles time calculations and display
     */
    private void getPanchangData() {
        language = CUtils.getLanguageKey(CUtils.getLanguageCodeFromPreference(activity));
        selectedYear = Calendar.getInstance().get(Calendar.YEAR);
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        // set default value
        if (beanPlace != null) {
            beanPlace.setLatitude(lat);
            beanPlace.setLongitude(lng);
            beanPlace.setTimeZone(timeZone);
            beanPlace.setTimeZoneString(timeZoneString);
        }

        //get place value from preference
        getValueFromPreference();

        if (cityId.equals("")) {
            cityId = "1261481";
        }

        // download festival data without blocking the Home UI
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        downloadFestivalData(selectedYear, cityId, false);

        if (beanPlace != null) {

            if (beanPlace != null && beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Nepal")) {

                if (CUtils.getNewKundliSelectedDate() <= 1985) {
                    beanPlace.setTimeZoneName("GMT+5.5");
                    beanPlace.setTimeZone("5.5");
                    beanPlace.setTimeZoneValue(Float.parseFloat("5.5"));
                } else {
                    beanPlace.setTimeZoneName("GMT+5.75");
                    beanPlace.setTimeZone("5.75");
                    beanPlace.setTimeZoneValue(Float.parseFloat("5.75"));
                }
            }
            if (beanPlace != null && beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Suriname")) {
                if (CUtils.getNewKundliSelectedDate() <= 1984) {
                    if (CUtils.getNewKundliSelectedDate() == 1984 &&
                            CUtils.getNewKundliSelectedMonth() > 9) {
                        beanPlace.setTimeZoneName("GMT-3.0");
                        beanPlace.setTimeZone("-3.0");
                        beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                    } else {
                        beanPlace.setTimeZoneName("GMT-3.5");
                        beanPlace.setTimeZone("-3.5");
                        beanPlace.setTimeZoneValue(Float.parseFloat("-3.5"));
                    }
                } else {
                    beanPlace.setTimeZoneName("GMT-3.0");
                    beanPlace.setTimeZone("-3.0");
                    beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                }
            }
            lat = beanPlace.getLatitude();
            lng = beanPlace.getLongitude();
            timeZone = beanPlace.getTimeZone();
            timeZoneString = beanPlace.getTimeZoneString();
            cityId = String.valueOf(beanPlace.getCityId());
        }

        Calendar calendar = Calendar.getInstance();
        //today
        getTodayModelData(calendar);

        //yesterday
        getYesterdayCalander();

        //tomorrow
        getTomorrowCalander();

        setDataInTithiCard(calendar);
    }

    /**
     * get Tomorrow Calander
     */
    private void getTomorrowCalander() {
        Calendar tcalendar = Calendar.getInstance();
        tcalendar.add(Calendar.DATE, 1);
        tomorrowbeanDateTime.setCalender(tcalendar.get(Calendar.MONTH), tcalendar.get(Calendar.DAY_OF_MONTH), tcalendar.get(Calendar.YEAR));
        tomorrowbeanDateTime.setDay(tcalendar.get(Calendar.DAY_OF_MONTH));
        tomorrowbeanDateTime.setMonth(tcalendar.get(Calendar.MONTH));
        tomorrowbeanDateTime.setYear(tcalendar.get(Calendar.YEAR));
        Date tomorrowdate = tomorrowbeanDateTime.getCalender().getTime();
        tomorrowCalculation = new AajKaPanchangCalulation(tomorrowdate, cityId, language, lat, lng, timeZone, timeZoneString);
        if (tomorrowCalculation != null) {
            tomorrowModel = tomorrowCalculation.getPanchang();
        }
    }

    /**
     * get Yesterday Calander
     */
    private void getYesterdayCalander() {
        Calendar ycalendar = Calendar.getInstance();
        ycalendar.add(Calendar.DATE, -1);
        yesterdaybeanDateTime.setCalender(ycalendar.get(Calendar.MONTH), ycalendar.get(Calendar.DAY_OF_MONTH), ycalendar.get(Calendar.YEAR));
        yesterdaybeanDateTime.setDay(ycalendar.get(Calendar.DAY_OF_MONTH));
        yesterdaybeanDateTime.setMonth(ycalendar.get(Calendar.MONTH));
        yesterdaybeanDateTime.setYear(ycalendar.get(Calendar.YEAR));
        Date yesterdaydate = yesterdaybeanDateTime.getCalender().getTime();
        yesterdayCalculation = new AajKaPanchangCalulation(yesterdaydate, cityId, language, lat, lng, timeZone, timeZoneString);
        if (yesterdayCalculation != null) {
            yesterdayModel = yesterdayCalculation.getPanchang();
        }
    }

    /**
     * get Today Model Data
     *
     * @param calendar
     */
    private void getTodayModelData(Calendar calendar) {
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        Date date = beanDateTime.getCalender().getTime();
        todayCalculation = new AajKaPanchangCalulation(date, cityId, language, lat, lng, timeZone, timeZoneString);
        if (todayCalculation != null) {
            todayModel = todayCalculation.getPanchang();
        }
    }

    private void getValueFromPreference() {
        SharedPreferences prefs = getActivity().getSharedPreferences(prefSaveCities, Context.MODE_PRIVATE);
        String storedHashMapString = prefs.getString(keySaveCities, "noDataFound");

        if (!storedHashMapString.equals("noDataFound")) {

            java.lang.reflect.Type type = new TypeToken<ArrayList<LibOutPlace>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<LibOutPlace> placeArrayList = gson.fromJson(storedHashMapString, type);

            if (placeArrayList != null) {
                beanPlace.setTimeZone(placeArrayList.get(0).getTimezone());
                beanPlace.setTimeZoneString(placeArrayList.get(0).getTimeZoneString());
                beanPlace.setCityId(Integer.parseInt(placeArrayList.get(0).getId()));
                timeZone = beanPlace.getTimeZone();
                timeZoneString = beanPlace.getTimeZoneString();
                cityId = String.valueOf(beanPlace.getCityId());
                lat = placeArrayList.get(0).getLatitude();
                lng = placeArrayList.get(0).getLongitude();
                beanPlace.setLatitude(lat);
                beanPlace.setLongitude(lng);
            }
        }
    }

    /**
     * set Data In Tithi Card
     *
     * @param calendar
     */
    private void setDataInTithiCard(Calendar calendar) {
        String[] monthArray = getResources().getStringArray(R.array.month_short_name_list);
        String[] weekArray = getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);

        float currentTimeToCompare = getCurrentTimeToCompare(calendar);

        // return if any model is null
        if (todayModel == null || yesterdayModel == null || tomorrowModel == null) {
            return;
        }


        /**
         * right side data
         */
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            mTithiDayName.setText(weekArray[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
            mTithiDate.setText(calendar.get(Calendar.DATE) + " " + monthArray[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR));
        } else {
            mTithiDayName.setText(weekArray[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
            mTithiDate.setText(getDayNumberSuffix(calendar.get(Calendar.DATE)) + " " + monthArray[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR));
        }

        if (todayModel.getMonthAmanta() != null && todayModel.getPakshaName() != null) {
            mTithiPaksha.setText(todayModel.getMonthAmanta() + " - " + todayModel.getPakshaName());
        }

        /**
         * left side data
         */
        //tithi day , Image , tithi name and tithi start time , tithi end time
        //data from yesterday model
        if (todayModel.getTithiInt() != null && todayModel.getTithiInt().length > 2) {  //
            if (currentTimeToCompare < todayModel.getTithiInt()[1]) {
                setTithiDayImageName(todayModel, currentTimeToCompare, false);
                //start time
                setStartTime(todayModel, yesterdayModel, currentTimeToCompare, false);
                //end time
                setEndTime(todayModel, currentTimeToCompare, false);
            } else {
                setTithiDayImageName(todayModel, currentTimeToCompare, false);
                //start time
                setStartTime(todayModel, yesterdayModel, currentTimeToCompare, false);
                //end time
                setEndTime(todayModel, currentTimeToCompare, false);
            }
        } else {
            //data from today model
            if (todayModel.getTithiInt() != null && currentTimeToCompare < todayModel.getTithiInt()[1]) {
                setTithiDayImageName(todayModel, currentTimeToCompare, false);
                //start time
                setStartTime(todayModel, yesterdayModel, currentTimeToCompare, false);
                //end time
                setEndTime(todayModel, currentTimeToCompare, false);
            }
            //data from tomorrow model
            else {
                setTithiDayImageName(tomorrowModel, currentTimeToCompare, true);
                //start time
                setStartTime(tomorrowModel, todayModel, currentTimeToCompare, true);
                //end time
                setEndTime(tomorrowModel, currentTimeToCompare, true);
            }
        }

    }

    /**
     * @param firstModel
     * @param currentTimeToCompare
     */
    private void setEndTime(AajKaPanchangModel firstModel, float currentTimeToCompare, boolean isForTomorrowModel) {
        String endLabel = getResources().getString(R.string.end) + ": ";
        String tomorrowLabel = getResources().getString(R.string.tomorrow_label) + " ";
        String endTime = firstModel.getTithiTime();
        endTime = endTime.replace("\n", "");

        if (endTime.contains(",")) {
            String[] splitendtime = endTime.split(",");
            String[] endSplit = splitendtime[0].split(":");
            float endtimetoCompare = getTimeToCopmare(endSplit);
            if (endtimetoCompare > currentTimeToCompare) {
                //match for +24 time and replace + with ""
                if (CUtils.convertTimeToAmPm(splitendtime[0]).contains("+")) {
                    mTithiEndTime.setText(endLabel + tomorrowLabel + CUtils.convertTimeToAmPm(splitendtime[0]).replace("+", "").trim());
                } else {
                    mTithiEndTime.setText(endLabel + CUtils.convertTimeToAmPm(splitendtime[0]).trim());
                }
            } else if (endtimetoCompare < currentTimeToCompare && isForTomorrowModel) { // having two tithi on same day
                //match for +24 time and replace + with ""
                if (CUtils.convertTimeToAmPm(splitendtime[0]).contains("+")) {
                    mTithiEndTime.setText(endLabel + tomorrowLabel + CUtils.convertTimeToAmPm(splitendtime[0]).replace("+", "").trim());
                } else {
                    mTithiEndTime.setText(endLabel + tomorrowLabel + CUtils.convertTimeToAmPm(splitendtime[0]).trim());
                }
            } else {
                //match for +24 time and replace + with ""
                if (CUtils.convertTimeToAmPm(splitendtime[1]).contains("+")) {
                    mTithiEndTime.setText(endLabel + tomorrowLabel + CUtils.convertTimeToAmPm(splitendtime[1]).replace("+", "").trim());
                } else {
                    mTithiEndTime.setText(endLabel + CUtils.convertTimeToAmPm(splitendtime[1]).trim());
                }
            }
        } else {
            //match for +24 time and replace + with ""
            if (CUtils.convertTimeToAmPm(endTime).contains("+")) {
                mTithiEndTime.setText(endLabel + tomorrowLabel + CUtils.convertTimeToAmPm(endTime).replace("+", "").trim());
            } else if (isForTomorrowModel) {
                mTithiEndTime.setText(endLabel + tomorrowLabel + CUtils.convertTimeToAmPm(endTime).trim());
            } else {
                mTithiEndTime.setText(endLabel + CUtils.convertTimeToAmPm(endTime).trim());
            }
        }
    }

    /**
     * @param firstModel
     * @param secondModel
     * @param currentTimeToCompare
     */
    private void setStartTime(AajKaPanchangModel firstModel, AajKaPanchangModel secondModel, float currentTimeToCompare, boolean isForTomorrowModel) {
        String startLabel = getResources().getString(R.string.start) + ": ";
        String yesterdayLabel = getResources().getString(R.string.yesterday_label) + " ";
        String startTime;
        String startday;
        String[] splitstarttime1 = firstModel.getTithiTime().split(",")[0].split(":");
        float starttimetoCompare1 = getTimeToCopmare(splitstarttime1);

        if (firstModel.getTithiTime().contains(",") && starttimetoCompare1 < currentTimeToCompare && !isForTomorrowModel) {
            startTime = firstModel.getTithiTime().split(",")[0];
            startday = "";
        } else if (isForTomorrowModel || starttimetoCompare1 < currentTimeToCompare) {
            startTime = secondModel.getTithiTime().split(",")[0];
            startday = "";
        } else {
            startTime = secondModel.getTithiTime();
            startTime = startTime.replace("\n", "");
            startday = yesterdayLabel;
        }

        if (startTime.contains(",")) {
            String[] splitstarttime = startTime.split(",");
            String[] startSplit = splitstarttime[0].split(":");
            float starttimetoCompare = getTimeToCopmare(startSplit);
            if (starttimetoCompare > currentTimeToCompare) {
                //match for +24 time and replace + with ""
                if (CUtils.convertTimeToAmPm(splitstarttime[0]).contains("+")) {
                    mTithiStartTime.setText(startLabel + CUtils.convertTimeToAmPm(splitstarttime[0]).replace("+", "").trim());
                } else {
                    mTithiStartTime.setText(startLabel + startday + CUtils.convertTimeToAmPm(splitstarttime[0]).trim());
                }
            } else {
                //match for +24 time and replace + with ""
                if (CUtils.convertTimeToAmPm(splitstarttime[1]).contains("+")) {
                    mTithiStartTime.setText(startLabel + CUtils.convertTimeToAmPm(splitstarttime[1]).replace("+", "").trim());
                } else {
                    mTithiStartTime.setText(startLabel + startday + CUtils.convertTimeToAmPm(splitstarttime[1]).trim());
                }
            }
        } else {
            //match for +24 time and replace + with ""
            if (CUtils.convertTimeToAmPm(startTime).contains("+")) {
                mTithiStartTime.setText(startLabel + CUtils.convertTimeToAmPm(startTime).replace("+", "").trim());
            } else {
                mTithiStartTime.setText(startLabel + startday + CUtils.convertTimeToAmPm(startTime).trim());
            }
        }
    }

    /**
     * @param firstModel
     * @param currentTimeToCompare
     */
    private void setTithiDayImageName(AajKaPanchangModel firstModel, float currentTimeToCompare, boolean isForTomorrowModel) {
        //tithi day , Image , tithi name
        if (firstModel.getTithiInt().length > 2) {

            if (firstModel.getTithiInt()[1] > currentTimeToCompare || isForTomorrowModel) {
                mTithiDay.setText(String.valueOf((int) (firstModel.getTithiInt()[0])).trim());
                mTithiImage.setImageDrawable(getResources().getDrawable(moonImages[(int) (firstModel.getTithiInt()[0]) - 1]));
                mTithiName.setText(firstModel.getTithiValue().split(",")[0].trim());
            } else {
                mTithiDay.setText(String.valueOf((int) (firstModel.getTithiInt()[2])).trim());
                mTithiImage.setImageDrawable(getResources().getDrawable(moonImages[(int) (firstModel.getTithiInt()[2]) - 1]));
                mTithiName.setText(firstModel.getTithiValue().split(",")[1].trim());
            }

        } else {
            int tithiDay = (int) (firstModel.getTithiInt()[0] > 15 ? (int) firstModel.getTithiInt()[0] - 15 : firstModel.getTithiInt()[0]);
            mTithiDay.setText(String.valueOf(tithiDay).trim());
            mTithiImage.setImageDrawable(getResources().getDrawable(moonImages[(int) (firstModel.getTithiInt()[0]) - 1]));
            mTithiName.setText(firstModel.getTithiValue().trim());
        }
    }

    /**
     * @param calendar
     * @return
     */
    private float getCurrentTimeToCompare(Calendar calendar) {
        //get time in 24 hours to compare
        float timeReturn = 0;

        float hours = calendar.get(Calendar.HOUR);
        float minutes = calendar.get(Calendar.MINUTE);
        float seconds = calendar.get(Calendar.SECOND);

        //if calander return PM then add 12 hours to hours
        if (calendar.get(Calendar.AM_PM) == 1) {
            hours = hours + 12;
        }
        timeReturn = hours + (minutes / 60) + (seconds / 3600);
        return timeReturn;
    }

    /**
     * @param startSplit
     * @return
     */
    private float getTimeToCopmare(String[] startSplit) {
        float startTimeHour = Float.parseFloat(startSplit[0]);
        float startTimeMinute = Float.parseFloat(startSplit[1]);
        float startTimeSecond = Float.parseFloat(startSplit[2]);
        float timetoCompare = startTimeHour + (startTimeMinute / 60) + (startTimeSecond / 3600);

        if (timetoCompare > 24) {
            timetoCompare = timetoCompare - 24;
        }

        return timetoCompare;
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "st";
                } else {
                    return String.valueOf(day);
                }

            case 2:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "nd";
                } else {
                    return String.valueOf(day);
                }

            case 3:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "rd";
                } else {
                    return String.valueOf(day);
                }

            default:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "th";
                } else {
                    return String.valueOf(day);
                }

        }
    }

    /**
     * @created by : Amit Rautela
     * @created on : 22/2/16.
     * @desc : This method is used to look up the layout reference
     */
    private void setLayRef(View rootView) {
        liveAstroTitle = rootView.findViewById(R.id.rl_live_astro_section);
        aiAstrologerLayout = rootView.findViewById(R.id.rl_ai_astro_section);
        astroTitle = rootView.findViewById(R.id.rl_astro_section);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerLayoutCallChat);
        shimmerFrameLayout.startShimmer();
        shimmerLayoutLive = rootView.findViewById(R.id.shimmerLayoutLive);
        shimmerLayoutLive.startShimmer();
        rl_history_section = rootView.findViewById(R.id.rl_history_section);
        liveRecyclerView = rootView.findViewById(R.id.liveAstroRecyclerView);
        rvHistoryKMF = rootView.findViewById(R.id.rvHistoryKMF);

        videoLayout = rootView.findViewById(R.id.video_layout);
        learnAstroLayout = rootView.findViewById(R.id.learn_video_layout);
        productLayout = rootView.findViewById(R.id.product_category_layout);
        reportLayout = rootView.findViewById(R.id.report_layout);
        articleLayout = rootView.findViewById(R.id.articleLayout);




        RecyclerView astroRecyclerView = rootView.findViewById(R.id.AstroRecyclerView);
        RecyclerView aiAstroRecyclerView = rootView.findViewById(R.id.AIAstroRecyclerView);
        RecyclerView videoRecyclerView = rootView.findViewById(R.id.videoRecyclerView);
        RecyclerView learnVideoRecyclerView = rootView.findViewById(R.id.learn_videoRecyclerView);
        RecyclerView productRecyclerView = rootView.findViewById(R.id.product_categoryRecyclerView);
        RecyclerView reportsRecyclerView = rootView.findViewById(R.id.reportRecyclerView);
        RecyclerView articlesRecyclerView = rootView.findViewById(R.id.articleRecyclerView);
        RecyclerView othersRecyclerView = rootView.findViewById(R.id.othersRecyclerView);
        seeMoreAstrologer = rootView.findViewById(R.id.see_more_astro);
        btnViewAllNow = rootView.findViewById(R.id.btnViewAllNow);
        horizontalDottedProgress = rootView.findViewById(R.id.horizontal_dotted_progress);
        horizontalDottedProgressAstro = rootView.findViewById(R.id.horizontal_dotted_progress_astro);
        ivRefresh = rootView.findViewById(R.id.iv_refresh);
        ivRefreshAstro = rootView.findViewById(R.id.iv_refresh_astro);
        mViewPageradd = rootView.findViewById(R.id.viewpageradd);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPageradd.setOffscreenPageLimit(10);
        mViewPageradd.setVisibility(View.GONE);
        adImage = rootView.findViewById(R.id.adImage);
        aiAstroLayout = rootView.findViewById(R.id.rl_ai_astro_section);



        // indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        frameLay = rootView.findViewById(R.id.frameLay);
        btnTalk = rootView.findViewById(R.id.btnTalk);
//        callWithAstroTV = rootView.findViewById(R.id.callWithAstroTV);
//        chatWithAstroTV = rootView.findViewById(R.id.chatWithAstroTV);
        btnChat = rootView.findViewById(R.id.btnChat);
        btnCallRL = rootView.findViewById(R.id.btnCallRL);
        btnChatRL = rootView.findViewById(R.id.btnChatRL);

        tvShare = rootView.findViewById(R.id.tv_share);
        // get view home_tithi_include
        mTithi = rootView.findViewById(R.id.tithi);
        mTithiImage = rootView.findViewById(R.id.tithi_image);
        astroLiveIcon = rootView.findViewById(R.id.img_astro_live_ic);
        mTithiDay = rootView.findViewById(R.id.tithi_day);
        mTithiStartTime = rootView.findViewById(R.id.tithi_start_time);
        mTithiEndTime = rootView.findViewById(R.id.tithi_end_time);
        mTithiName = rootView.findViewById(R.id.tithi_name);
        mTithiDayName = rootView.findViewById(R.id.tithi_day_name);
        mTithiDate = rootView.findViewById(R.id.tithi_date);
        mTithiPaksha = rootView.findViewById(R.id.tithi_paksha);
        mTithiFestival = rootView.findViewById(R.id.tithi_festival);
        mTithiLL = rootView.findViewById(R.id.tithi_ll);
        see_more_history = rootView.findViewById(R.id.see_more_history);
        seeMoreAIAstrologer = rootView.findViewById(R.id.btnViewAllAIAstrologer);
        seeMoreVideo = rootView.findViewById(R.id.btnViewAllVideos);
        seeMoreLearnVideo = rootView.findViewById(R.id.btnViewAllLearnVideos);
        seeMoreProductCategory = rootView.findViewById(R.id.btnViewAllProduct_category);
        btnViewAllreport = rootView.findViewById(R.id.btnViewAllreport);
        btnViewAllArticles = rootView.findViewById(R.id.btnViewAllArticles);

        kundliAIChatBtnLayout = rootView.findViewById(R.id.kundli_ai_chat_btn_layout);
        chatBoxTv = rootView.findViewById(R.id.chat_box_tv);

        tvShare.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        chatBoxTv.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        btnTalk.setTypeface((((BaseInputActivity) activity).mediumTypeface));
//        callWithAstroTV.setTypeface((((BaseInputActivity) activity).mediumTypeface));
//        chatWithAstroTV.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        btnChat.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithiDay.setTypeface((((BaseInputActivity) activity).mediumTypeface), Typeface.BOLD);
        mTithiDayName.setTypeface((((BaseInputActivity) activity).mediumTypeface), Typeface.BOLD);
        mTithi.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithi.setAllCaps(true);
        mTithiStartTime.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithiEndTime.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithiName.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithiDate.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithiPaksha.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        mTithiFestival.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        beanDateTime = new BeanDateTimeForPanchang();
        yesterdaybeanDateTime = new BeanDateTimeForPanchang();
        tomorrowbeanDateTime = new BeanDateTimeForPanchang();

        moduleNameList = getResources().getStringArray(R.array.kundliModulesList);
        moduleNameListAbove = new String[moduleIconListAbove.length];
        moduleNameListBelow = new String[moduleNameList.length];
        int belowIndexIterator = 0;

        for (int iterator = 0; iterator < moduleNameList.length; iterator++) {
            if (iterator < moduleIconListAbove.length) {
                moduleNameListAbove[iterator] = moduleNameList[iterator];
            } else {
                if(belowIndexIterator == 3){ //in case of Kundli AI+ plan show
                    String planNameToShow = "";
                    if(CUtils.isDhruvPlan(activity)){ //only in case of dhruv plan show dhruv
                        planNameToShow = getString(R.string.dhruv);
                    }else{ //in both case basic or kundli AI+ show kundli AI+
                        planNameToShow = getString(R.string.platinum_plan);
                    }
                    moduleNameListBelow[belowIndexIterator++] = planNameToShow;
                }else {
                    moduleNameListBelow[belowIndexIterator++] = moduleNameList[iterator];
                }
            }
        }

        moduleNameListForCategoryChat = getResources().getStringArray(R.array.kundliAiCategoryNameList);

        gridViewAbove = rootView.findViewById(R.id.gridView);
//        gridViewBelow = rootView.findViewById(R.id.gridView_below);
        rvCategoryChat = rootView.findViewById(R.id.rvCategoryChat);
        handler = new Handler(Looper.getMainLooper());

        /**
         * Set live astro recyclerView by manish
         */

        //liveAstrologerModelArrayList = new ArrayList<>();
        liveAstrologerModelArrayListFinal = new ArrayList<>();
        astrologerDetailBeanArrayList = new ArrayList<>();
        aiAstroDetailBeanList = new ArrayList<>();
        //callHistoryList = new ArrayList<>();
        callHistoryListFinal = new ArrayList<>();
        youtubeVideoBeans = new ArrayList<>();
        sliderModels = new ArrayList<>();
        productCategoryList = new ArrayList<>();
        reportCategoryList = new ArrayList<>();
        articleList = new ArrayList<>();
//        initLiveAstroReceiver();
//        getLiveAstrologerList();
//       getAstrologerList();
//        getLastConsultList();

        callChatAstrologerAdapter = new CallChatAstrologerAdapter(activity, astrologerDetailBeanArrayList);
        WrapContentLinearLayoutManager onlineLayoutManager = new WrapContentLinearLayoutManager(activity);
        onlineLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        astroRecyclerView.setLayoutManager(onlineLayoutManager);
        astroRecyclerView.setAdapter(callChatAstrologerAdapter);

        aiAstrologerAdapter = new CallChatAstrologerAdapter(activity, aiAstroDetailBeanList);
        aiAstrologerAdapter.setIsAi(true);
        WrapContentLinearLayoutManager aiAstroLayoutManager = new WrapContentLinearLayoutManager(activity);
        aiAstroLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        aiAstroRecyclerView.setLayoutManager(aiAstroLayoutManager);
        aiAstroRecyclerView.setAdapter(aiAstrologerAdapter);
        //initilize astro receiver
        initAiAstrologerReceiver();
        videoHomeAdapter = new VideoHomeAdapter(activity, youtubeVideoBeans);
        WrapContentLinearLayoutManager videoLayoutManager = new WrapContentLinearLayoutManager(activity);
        videoLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videoRecyclerView.setLayoutManager(videoLayoutManager);
        videoRecyclerView.setAdapter(videoHomeAdapter);

        CUtils.makeGetRequest(new VolleyResponse() {
            @Override
            public void onResponse(String response, int method) {
//                Log.e("VideoCheck", "onResponse: "+response );
                youtubeVideoBeans = (ArrayList<YoutubeVideoBean>) LibCUtils.parseYoutubeRssFeedXML(response);
                videoHomeAdapter.setData(youtubeVideoBeans);
//                Log.e("videoBean", "onResponse: "+youtubeVideoBeans.size()+" ---\n"+youtubeVideoBeans );
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, CGlobalVariables.youtubeRssFeedUrl, 0);


        learnVideoAdapter = new AstrologyLessonHomeAdapter(sliderModels, activity);
        WrapContentLinearLayoutManager learnVideoLayoutManager = new WrapContentLinearLayoutManager(activity);
        learnVideoLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        learnVideoRecyclerView.setLayoutManager(learnVideoLayoutManager);
        learnVideoRecyclerView.setAdapter(learnVideoAdapter);

//        String entry = CUtils.getStringData(activity, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
//        if (!entry.isEmpty()) {
//            productCategoryList = parseCategories(entry);
//        }else{
//            productCategoryList = new ArrayList<>();
//        }


        homeProductAdapter = new HomeProductAdapter(activity, productCategoryList);
        WrapContentLinearLayoutManager productLayoutManager = new WrapContentLinearLayoutManager(activity);
        productLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        productRecyclerView.setLayoutManager(productLayoutManager);
        productRecyclerView.setAdapter(homeProductAdapter);


        homeReportItemAdapter = new HomeReportItemAdapter(activity, reportCategoryList, LANGUAGE_CODE);
        WrapContentLinearLayoutManager reportLayoutManager = new WrapContentLinearLayoutManager(activity);
        reportLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        reportsRecyclerView.setLayoutManager(reportLayoutManager);
        reportsRecyclerView.setAdapter(homeReportItemAdapter);

        homeArticlesAdapter = new HomeArticlesAdapter(articleList, activity);
        WrapContentLinearLayoutManager articleLayoutManager = new WrapContentLinearLayoutManager(activity);
        articleLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        articlesRecyclerView.setLayoutManager(articleLayoutManager);
        articlesRecyclerView.setAdapter(homeArticlesAdapter);

        UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(activity);
        ArrayList<PersonalizedCategoryENUM> personalizedCategoryENUMS = new ArrayList<>();
        String[] personalizedCategoryList = CategorySortHelper.givePersonalizeCategoryList(userProfileData);
        for (String enumString : personalizedCategoryList) {
            try {
                personalizedCategoryENUMS.add(PersonalizedCategoryENUM.valueOf(enumString));
            } catch (Exception e) {
                Log.e("fatalException", "setLayRef: exception = " + e.getMessage());
            }
        }
        CategorySortHelper.applySortByClickCount(personalizedCategoryENUMS);

        homeCategoryChatAdapter = new HomeCategoryChatAdapter(activity, personalizedCategoryENUMS);
        WrapContentLinearLayoutManager categoryLayoutManager = new WrapContentLinearLayoutManager(activity);
        categoryLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCategoryChat.setLayoutManager(categoryLayoutManager);
        rvCategoryChat.setAdapter(homeCategoryChatAdapter);

        setRecyclerView();

        lastConsultAdapter = new LastConsultAdapter(getContext(), callHistoryListFinal, 1);
        WrapContentLinearLayoutManager historyLayoutManager = new WrapContentLinearLayoutManager(activity);
        historyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHistoryKMF.setLayoutManager(historyLayoutManager);
        int spacingInDp = 10; // or 10, 12 as you like
        int spacingInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                spacingInDp,
                requireContext().getResources().getDisplayMetrics()
        );
        // Add spacing here 👇
        rvHistoryKMF.addItemDecoration(new HorizontalSpaceItemDecoration(spacingInPx));
        rvHistoryKMF.setAdapter(lastConsultAdapter);

        /**
         * Set Above Adapter
         */
        adapterAbove = new MainModulesAdapter(activity, moduleIconListAbove, moduleNameListAbove, ((BaseInputActivity) activity).mediumTypeface, KundliModules_Frag.this);
        gridViewAbove.setAdapter(adapterAbove);
        gridViewAbove.setExpanded(true);
        gridViewAbove.setFocusable(false);

        /**
         * Set Below Adapter
         */
//        adapterBelow = new MainModulesAdapter(activity, moduleIconListBelow, moduleNameListBelow, ((BaseInputActivity) activity).mediumTypeface, KundliModules_Frag.this);
//        gridViewBelow.setAdapter(adapterBelow);
//        gridViewBelow.setExpanded(true);
//        gridViewBelow.setFocusable(false);

        if(CUtils.isDhruvPlan(activity)){
            moduleIconListBelow[3] = R.drawable.icon_dhruv_plan;
        } else {
            moduleIconListBelow[3] = R.drawable.icon_kundli_ai_plus;
        }

        adapterBelow = new OthersHomeItemAdapter(activity, moduleIconListBelow, moduleNameListBelow);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL);
        othersRecyclerView.setLayoutManager(layoutManager);
        othersRecyclerView.setAdapter(adapterBelow);


        /**
         * Set Category Chat Adapter
         */

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            Log.e("TestAdData", "setTopAdd()1");
            setTopAdd(topAdData);
        }

        if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
            setBottomAd(bottomAdData);
        }

        Update = new Runnable() {
            public void run() {
                if (currentPage == mPagerAdapter.getCount()) {
                    currentPage = 0;
                }
                mViewPageradd.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);

            }
        };

        mViewPageradd.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // mPagerAdapter.notifyDataSetChanged();
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


    }

    private List<ProductCategory> parseCategories(JSONArray jsonArray) {
        List<ProductCategory> categories = new ArrayList<>();
        // Adding static category at position 0
//        categories.add(new ProductCategory(
//                getString(R.string.module_brihat),
//                getString(R.string.module_brihat),
//                "Brihat Description",
//                "brihat",
//                null,
//                String.valueOf(R.drawable.ic_brihat_kundli_round) // Set from drawable resource
//        ));

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                categories.add(new ProductCategory(
                        obj.getString("CategoryFullName"),
                        obj.getString("CategoryShortName"),
                        obj.getString("CategorySmallDescription"),
                        obj.getString("CategoryUrl"),
                        obj.getString("CategoryImagePath"),
                        null
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Adding static category at position 1
//        if (categories.size() >= 1) {
//            categories.add(1, new ProductCategory(
//                    getString(R.string.platinum_plan),
//                    getString(R.string.platinum_plan),
//                    "Kundli AI+ Description",
//                    "kundli ai+",
//                    null,
//                    String.valueOf(R.drawable.ic_kundli_ai_round) // Set from drawable resource
//            ));
//        }


        // Adding static category at position 2
//        if (categories.size() >= 2) {
//            categories.add(2,new ProductCategory(
//                    getString(R.string.astro_services),
//                    getString(R.string.astro_services),
//                    "Service Description",
//                    "services",
//                    null,
//                    String.valueOf(R.drawable.ic_dollers_round) // Set from drawable resource
//            ));
//        }

        // Adding static category at position 8
//        if (categories.size() >= 8) {
//            categories.add(8, new ProductCategory(
//                    getString(R.string.module_cogni_astro),
//                    getString(R.string.module_cogni_astro),
//                    "CogniAstro Description",
//                    "cogniastro",
//                    null,
//                    String.valueOf(R.drawable.ic_cogniastro_round) // Set from drawable resource
//            ));
//        }


        return categories;
    }



    private void getLiveAstrologerList() {
        try {
            boolean liveAstroEnabledForAstrosageHomeScreen = CUtils.getBooleanData(getContext(), CGlobalVariables.liveAstrologerEnabledForAstrosageHomeScreen, false);
            if (!liveAstroEnabledForAstrosageHomeScreen) {
                return;
            }
            horizontalDottedProgress.clearAnimation();
            horizontalDottedProgress.setVisibility(View.GONE);
            ivRefresh.setVisibility(View.GONE);
            String liveAstroData = com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroList();
            if (!TextUtils.isEmpty(liveAstroData)) {
                parseLiveAstrologerList(liveAstroData);
            }
        } catch (Exception e) {
            //
        }
    }

    private void getAstrologerList() {
        //Log.d("TestOfferType", "getAstrologerList()");
        horizontalDottedProgressAstro.clearAnimation();
        horizontalDottedProgressAstro.setVisibility(View.GONE);
        ivRefreshAstro.setVisibility(View.GONE);
        String response = com.ojassoft.astrosage.varta.utils.CUtils.getAstroListWithLimit();
        //Log.d("TestOfferType", "getAstrologerList()response="+response);
        if (!TextUtils.isEmpty(response)) {
            parseAstrologerList(response);
        }
    }

    private void getLastConsultList() {
        boolean isLogin;
        isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(getContext());
        if (isLogin) {
            String response = com.ojassoft.astrosage.varta.utils.CUtils.getHistoryList();
            if (!TextUtils.isEmpty(response)) {
                parseHistory(response);
            }
        }
    }

    private void parseLiveAstrologerList(String liveAstroData) {
        //Log.d("testTagManager","response >>= "+liveAstroData);
        prevLiveAstroSize = 0;
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }

        liveAstrologerModelArrayList = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Background thread
                    try {
                        //Log.e("ParseFreq", "1");
                        /*if (liveAstrologerModelArrayList == null) {
                            liveAstrologerModelArrayList = new ArrayList<>();
                        } else {*/
                        prevLiveAstroSize = liveAstrologerModelArrayListFinal.size();
                        liveAstrologerModelArrayList.clear();
                        //}

                        JSONObject jsonObject = new JSONObject(liveAstroData);
                        JSONArray jsonArray = jsonObject.getJSONArray("astrologers");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            LiveAstrologerModel liveAstrologerModel = com.ojassoft.astrosage.varta.utils.CUtils.parseLiveAstrologerObject(object);
                            if (liveAstrologerModel == null) continue;
                            liveAstrologerModelArrayList.add(liveAstrologerModel);
                        }

                        com.ojassoft.astrosage.varta.utils.CUtils.parseGiftList(liveAstroData);
                    } catch (Exception e) {
                        liveAstrologerModelArrayList.clear();
                    }
                    ActAppModule.liveAstrologerModelArrayList = liveAstrologerModelArrayList;
                    //Log.e("ParseFreq", "2");
                    //UI process
                    activity.runOnUiThread(() -> {
                        // UI thread
                        //Log.e("ParseFreq", "3");
                        try {
                            liveAstrologerModelArrayListFinal.clear();
                            liveAstrologerModelArrayListFinal.addAll(liveAstrologerModelArrayList);
                            openLiveStramingScreen();
                            if (liveAstrologerAdapter == null || prevLiveAstroSize < 4) {
                                setRecyclerView();
                            } else {
                                liveAstrologerAdapter.notifyDataSetChanged();
                            }
                            hideLiveTitle();
                            hideShimmerEffectLive();
                        } catch (Exception e) {
                            //
                        }
                        //Log.e("ParseFreq", "4");
                    });
                } catch (Exception e) {
                    Log.e("ParseFreq", "Exception=" + e);
                }
            }
        });
    }
private void initAiAstrologerReceiver() {
    aiAstrologerReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (aiAstrologerAdapter != null){
                aiAstrologerAdapter.notifyDataSetChanged();
            }
        }
    };
    LocalBroadcastManager.getInstance(activity).registerReceiver((aiAstrologerReceiver),
            new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_PASS_ASTRO_BROAD_ACTION)
    );
}
    private void initLiveAstroReceiver() {
        try {
            liveAstroReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null && intent.getBooleanExtra("isLiveAstro", false)) {
                        getLiveAstrologerList();
                        horizontalDottedProgress.clearAnimation();
                        horizontalDottedProgress.setVisibility(View.GONE);
                        ivRefresh.setVisibility(View.GONE);
                    } else if (intent != null && intent.getBooleanExtra(IS_HISTORY_LIST, false)) {
                        String history = com.ojassoft.astrosage.varta.utils.CUtils.getHistoryList();
                        if (!TextUtils.isEmpty(history)) {
                            parseHistory(history);
                        }
                    } else {
                        horizontalDottedProgressAstro.clearAnimation();
                        horizontalDottedProgressAstro.setVisibility(View.GONE);
                        ivRefreshAstro.setVisibility(View.GONE);
                        String response = com.ojassoft.astrosage.varta.utils.CUtils.getAstroListWithLimit();
                        //Log.d("TestOfferType", "getAstrologerList()response2="+response);
                        if (!TextUtils.isEmpty(response)) {
                            parseAstrologerList(response);
                        }
                        //checkConnectFreeAIChat();
                    }
                }
            };
            LocalBroadcastManager.getInstance(activity).registerReceiver((liveAstroReceiver),
                    new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_ASTRO_BROAD_ACTION)
            );
        } catch (Exception e) {
            //
        }
    }

    private void checkConnectFreeCallChat(){
        if(AstrosageKundliApplication.connectAiChatAfterLogin){
            AstrosageKundliApplication.connectAiChatAfterLogin = false;
            com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(activity,AstrosageKundliApplication.apiCallingSource, AstrosageKundliApplication.chatCallconfigType);
        } if(AstrosageKundliApplication.connectAiCallAfterLogin){
            AstrosageKundliApplication.connectAiCallAfterLogin = false;
            com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiCall(activity,AstrosageKundliApplication.apiCallingSource, AstrosageKundliApplication.chatCallconfigType);
        } else if(AstrosageKundliApplication.connectHumanChatAfterLogin){
            AstrosageKundliApplication.connectHumanChatAfterLogin = false;
            com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(activity,AstrosageKundliApplication.apiCallingSource, AstrosageKundliApplication.chatCallconfigType);
        }
    }

    private void setRecyclerView() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            com.ojassoft.astrosage.varta.utils.CGlobalVariables.width = displayMetrics.widthPixels;
            com.ojassoft.astrosage.varta.utils.CGlobalVariables.height = displayMetrics.heightPixels;
            com.ojassoft.astrosage.varta.utils.CGlobalVariables.modifyHeight = (com.ojassoft.astrosage.varta.utils.CGlobalVariables.width / 16) * 5;
            liveAstrologerAdapter = new AkLiveAstrologerAdapter(getActivity(), liveAstrologerModelArrayListFinal);
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(activity);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            liveRecyclerView.setLayoutManager(layoutManager);
            liveRecyclerView.setAdapter(liveAstrologerAdapter);
        } catch (Exception e) {
            //
        }

    }

    public void hideLiveTitle() {
        if (liveAstroTitle != null) {
            Glide.with(astroLiveIcon).load(R.drawable.ic_live).into(astroLiveIcon);
            liveAstroTitle.setVisibility(liveAstrologerModelArrayListFinal.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    public void hideAiAstrologer() {
        if (aiAstrologerLayout != null) {
            aiAstrologerLayout.setVisibility(aiAstroDetailBeanList.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }



    public void hideAstroTitle() {
        if (astroTitle != null)
            astroTitle.setVisibility(astrologerDetailBeanArrayList.size() > 0 ? View.VISIBLE : View.GONE);

    }



    private void hideShimmerEffectCallChat() {
        if (astrologerDetailBeanArrayList.size() > 0) {
            shimmerFrameLayout.hideShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    private void hideShimmerEffectLive() {
        if (liveAstrologerModelArrayListFinal.size() > 0) {
            shimmerLayoutLive.hideShimmer();
            shimmerLayoutLive.setVisibility(View.GONE);
        }
    }

    public void hideHistoryTitle() {
        if (rl_history_section != null)
            rl_history_section.setVisibility(callHistoryListFinal.size() > 0 ? View.VISIBLE : View.GONE);
    }

    public void setTopAdd(AdData topData) {
        try {
            //Log.e("TestAdData", "topData=" + topData);
            //getData();

            if (topData != null) {
                IsShowBanner = topData.getIsShowBanner();
                IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;
            }

            if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
                if (frameLay != null) {
                    frameLay.setVisibility(View.GONE);
                }
            } else if (mViewPageradd != null) {

                mViewPageradd.setVisibility(View.VISIBLE);
                mPagerAdapter = new CustomPagerAdapterForAdds(getChildFragmentManager(), (topData.getImageObj()), "ActAppModule");
                mViewPageradd.setAdapter(mPagerAdapter);

                tabLayout.setupWithViewPager(mViewPageradd, true);

                frameLay.setVisibility(View.VISIBLE);
                NUM_PAGES = topData.getImageObj().size();
                mViewPageradd.setCurrentItem(0);
            }
        } catch (Exception e) {

        }
    }

    public void setBottomAd(AdData bottomData) {
        try {
            getData();
            if (bottomData != null) {
                IsShowBanner1 = bottomData.getIsShowBanner();
                IsShowBanner1 = IsShowBanner1 == null ? "" : IsShowBanner1;

            }
            if (bottomAdData == null || bottomData.getImageObj() == null || bottomData.getImageObj().size() <= 0 || IsShowBanner1.equalsIgnoreCase("False")) {
                if (adImage != null) {
                    adImage.setVisibility(View.GONE);

                }
            } else {
                if (adImage != null) {
                    adImage.setVisibility(View.VISIBLE);
                    adImage.setImageUrl(bottomData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(activity).getImageLoader());
                }
            }
            if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
                if (adImage != null) {
                    adImage.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {

        }

        /*Code for bottom screen ad*/
        //    if (bottomAdData!=null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
    /*    if (bottomAdData ==null ||bottomData.getImageObj()==null || bottomData.getImageObj().size()<=0 || IsShowBanner1.equalsIgnoreCase("False")) {
            if (frameLay1 != null) {
                frameLay1.setVisibility(View.GONE);
            }
        } else if (mViewPageradd1 != null && mViewPageradd1.getAdapter()==null) {

            if (swipeTimer1 != null) {
                swipeTimer1.cancel();
            }
            swipeTimer1 = new Timer();

            if (bottomData.getImageObj().size() <= 1) {
                indicator1.setVisibility(View.GONE);
            } else {
               // indicator1.setVisibility(View.VISIBLE);
                swipeTimer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler1.post(Update1);
                    }
                }, 5000, 5000);
            }
            mViewPageradd1.setVisibility(View.VISIBLE);
            mPagerAdapter1 = new CustomPagerAdapterForAdds(getChildFragmentManager(), (bottomData.getImageObj()), "ActAppModule");
            mViewPageradd1.setAdapter(mPagerAdapter1);
            frameLay1.setVisibility(View.VISIBLE);
            NUM_PAGES1 = bottomData.getImageObj().size();
            mViewPageradd1.setCurrentItem(0);
            indicator1.setViewPager(mViewPageradd1);
            indicator1.setRadius(10);
            indicator1.setStrokeWidth(3);

        } else {
                if (mViewPageradd1 != null) {
                    frameLay1.setVisibility(View.VISIBLE);
                    mViewPageradd1.getAdapter().notifyDataSetChanged();


                }
            }*/

        //      }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        /*try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (handler != null && Update != null) {
//            handler.removeCallbacks(Update);
//        }
        if (mReceiverBackgroundLoginService != null && getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiverBackgroundLoginService);
        }
        if (aiAstrologerReceiver != null && getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(aiAstrologerReceiver);
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (liveAstroReceiver != null) {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(liveAstroReceiver);
            }
            if (aiAstrologerReceiver != null && getActivity() != null) {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(aiAstrologerReceiver);
            }
        } catch (Exception e) {
            //
        }
        super.onDestroy();
    }

    /**
     * @param selectedYear
     * @param cityId
     * @param isShowProgressbar
     */
    public void downloadFestivalData(int selectedYear, String cityId, boolean isShowProgressbar) {
        checkCachedData(selectedYear, cityId, isShowProgressbar);
    }

    /**
     * @param selectedYear
     * @param cityId
     * @param isShowProgressbar
     */
    private void downloadDataDetails(final int selectedYear, final String cityId, boolean isShowProgressbar) {
        downloadDataDetails(selectedYear, cityId, isShowProgressbar, LANGUAGE_CODE, CUtils.getLanguageKey(LANGUAGE_CODE), false);
    }

    /**
     * Downloads festival data for a specific language and optionally retries in English.
     */
    private void downloadDataDetails(final int selectedYear, final String cityId, boolean isShowProgressbar, int languageCode, String languageKey, boolean isFallback) {

        if (pd == null)
            pd = new CustomProgressDialog(getContext(), typeface);

        if (isShowProgressbar) {
            pd.show();
            pd.setCancelable(false);
        }

        final String url = CGlobalVariables.indianCalenderUrl + languageCode + "&cityid=" + cityId + "&year=" + selectedYear;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response, JsonElement.class);
                            parseGsonData(response, url);

                        } else {
                            retryFestivalDataWithEnglish(selectedYear, cityId, languageCode, isFallback);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                retryFestivalDataWithEnglish(selectedYear, cityId, languageCode, isFallback);
                MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                        .getLayoutInflater(), getActivity(), typeface);
                mct.show(error.getMessage());

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                pd.dismiss();
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("language", languageKey);
                headers.put("date", String.valueOf(selectedYear));
                String cityIdToSend = "";
                if (beanPlace != null) {
                    cityIdToSend = String.valueOf(cityId);
                }

                headers.put("lid", cityIdToSend);
                return headers;
            }
        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * Retries the festival data request using English when the localized response is empty or fails.
     */
    private void retryFestivalDataWithEnglish(int selectedYear, String cityId, int languageCode, boolean isFallback) {
        if (!isFallback && languageCode != CGlobalVariables.ENGLISH) {
            downloadDataDetails(selectedYear, cityId, false, CGlobalVariables.ENGLISH, CUtils.getLanguageKey(CGlobalVariables.ENGLISH), true);
        }
    }

    /**
     * @param selectedYear
     * @param cityId
     * @param isShowProgressbar
     */
    private void checkCachedData(int selectedYear, String cityId, boolean isShowProgressbar) {
        String url = CGlobalVariables.indianCalenderUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {    // cache data
            String saveData = new String(entry.data, StandardCharsets.UTF_8);
            parseGsonData(saveData, url);
        } else {    // Api hit
            // Cached response doesn't exists. Make network call here
            //Log.e("Volley Not Cached Data");
            if (!CUtils.isConnectedWithInternet(getActivity())) {
                MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                        .getLayoutInflater(), getActivity(), typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                downloadDataDetails(selectedYear, cityId, isShowProgressbar);
            }
        }
    }


    /**
     * @param saveData
     * @param url
     */
    private void parseGsonData(String saveData, String url) {
        try {
            AllPanchangData allPanchangData;
            Gson gson = new Gson();
            allPanchangData = gson.fromJson(saveData, AllPanchangData.class);

            if (allPanchangData.getIndianCalenderData().size() > 0) {
                for (int iterator = 0; iterator < allPanchangData.getIndianCalenderData().size(); iterator++) {
                    String festivalDate = allPanchangData.getIndianCalenderData().get(iterator).getFestivalDate().trim();
                    //current date then break else continue
                    if (festivalDate != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            String getCurrentDateTime = sdf.format(Calendar.getInstance().getTime());
                            Date date1 = sdf.parse(festivalDate);
                            Date date2 = sdf.parse(getCurrentDateTime);
                            if (date1.compareTo(date2) == 0) {
                                mTithiFestival.setVisibility(View.VISIBLE);
                                mTithiFestival.setText(allPanchangData.getIndianCalenderData().get(iterator).getFestivalName().trim());
                                break;
                            } else {
                                mTithiFestival.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            queue.getCache().remove(url);
        }
    }

    public void updateAdapter() {
        adapterAbove.notifyDataSetChanged();
        adapterBelow.notifyDataSetChanged();
    }


    private void parseAstrologerList(String responseData) {
        try {
            if (astrologerDetailBeanArrayList == null) {
                astrologerDetailBeanArrayList = new ArrayList<>();
            } else {
                astrologerDetailBeanArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.length() > 0) {

                if (jsonObject.has("verifiedicon")) {
                    try {
                        com.ojassoft.astrosage.varta.utils.CUtils.setVerifiedAndOfferImage(URLDecoder.decode(jsonObject.getString("verifiedicon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("verifiedicondetail"), "UTF-8"),
                                URLDecoder.decode(jsonObject.getString("offericon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("offericondetail"), "UTF-8"));
                    } catch (Exception ex) {

                    }
                }
                String offerType = jsonObject.optString("offertype"); //"FIRSTSESSIONFREE";//
                boolean isSecondFreeChat = jsonObject.optBoolean("sfc");
                com.ojassoft.astrosage.varta.utils.CUtils.setSecondFreeChat(activity, isSecondFreeChat);
                //Log.d("ConfigDataReq", "mini list isSecondFreeChat="+isSecondFreeChat);
                //if (offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                if (activity instanceof ActAppModule) {
                    if (!AstrosageKundliApplication.isOpenVartaPopup) {
                        ((ActAppModule) activity).openDialogOnHomeScreen(offerType);
                    }
                }
                //}
                if (offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {

                    String firstFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE, "");
                    String secondFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");
                    String firstFreeCallType = CUtils.getStringData(activity,CGlobalVariables.FIRST_FREE_CALL_TYPE,"");

                    if (com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(activity)) {
                        if (secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                            btnChat.setText(R.string.free_chat);
                        } else {
                            btnChat.setText(R.string.free_chat);
                            btnTalk.setText(R.string.free_call);
                        }
                    } else {
                        if(firstFreeCallType.equalsIgnoreCase(CGlobalVariables.TYPE_AI_CALL)){
                            btnChat.setText(R.string.free_chat);
                            btnTalk.setText(R.string.free_call);
                        }else if(firstFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                            btnChat.setText(R.string.free_chat);
                        } else {
                            btnChat.setText(R.string.free_chat);
                            btnTalk.setText(R.string.free_call);
                        }
                    }

                }
                //Log.d("TestOffer", "offerType3="+offerType);
                String callChatOfferType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(activity);
                //Log.d("TestOfferType", "offerType4="+callChatOfferType);
                if (TextUtils.isEmpty(callChatOfferType) && !TextUtils.isEmpty(offerType)) { //refresh s0 banner
                    if (activity instanceof ActAppModule) {
                        ((ActAppModule) activity).getImageAdData();
                    }
                }
                com.ojassoft.astrosage.varta.utils.CUtils.setUserIntroOfferType(activity, offerType);
               // Log.d("TestOfferType", "jsonofferType  ="+offerType);

                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AstrologerDetailBean astrologerDetailBean = parseAstrologerObject(object);
                        if (astrologerDetailBean == null) continue;
                        astrologerDetailBeanArrayList.add(astrologerDetailBean);
                    }
                }

                if (callChatAstrologerAdapter != null)
                    callChatAstrologerAdapter.notifyDataSetChanged();
                hideAstroTitle();
                hideShimmerEffectCallChat();
            } else {
                hideShimmerEffectCallChat();
                ((ActAppModule) activity).callSnakbar(getResources().getString(R.string.server_error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLiveAstrologerModelAndJoinLiveSession(String urlText) {
        try {
            astroUrlForLiveJoin = urlText;
            LiveAstrologerModel liveAstrologerModel = getLiveAstrologerModelFromList(urlText);
            if (liveAstrologerModel != null) {
                openLiveStramingScreen();
            }
        } catch (Exception e) {

        }
    }

    /**
     * match url-text in live astrologer list, if available then join live otherwise open astrologer details page.
     */
    private void openLiveStramingScreen() {
        if (TextUtils.isEmpty(astroUrlForLiveJoin)) {
            return;
        }
        try {
            LiveAstrologerModel liveAstrologerModel = getLiveAstrologerModelFromList(astroUrlForLiveJoin);
            if (liveAstrologerModel != null) {
                if (activity instanceof ActAppModule) {
                    ((ActAppModule) activity).checkPermissions(liveAstrologerModel);
                }
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail(activity, astroUrlForLiveJoin, true, false,"");
            }

        } catch (Exception e) {
            //
        }
        astroUrlForLiveJoin = "";
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
            if (liveAstrologerModelArrayListFinal != null) {
                for (int i = 0; i < liveAstrologerModelArrayListFinal.size(); i++) {
                    LiveAstrologerModel liveAstrologerModel = liveAstrologerModelArrayListFinal.get(i);
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

    private void parseHistory(String response) {
        Log.e("SAN CHA response", "CHActivity parseConsulList() "+response);

        /*if(callHistoryList == null){
            callHistoryList = new ArrayList<>();
        }else {
            callHistoryList.clear();
        }*/
        callHistoryList = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Background thread
                    JSONArray consultations = null;
                    CallHistoryBean callHistoryBean;
                    JSONArray chats = null;
                    JSONObject jsonObject = new JSONObject(response);

                    String bal = jsonObject.getString("walletbalance");
                    com.ojassoft.astrosage.varta.utils.CUtils.setWalletRs(getContext(), bal);

                    if (jsonObject.has("videos")) {
                        JSONArray videos = jsonObject.getJSONArray("videos");
                        if (videos != null && videos.length() > 0) {
                            for (int i = 0; i < videos.length(); i++) {
                                callHistoryBean = new CallHistoryBean();
                                String userPhoneNo = videos.getJSONObject(i).getString("userPhoneNo");
                                String astrologerPhoneNo = videos.getJSONObject(i).getString("astrologerPhoneNo");
                                String astrologerName = videos.getJSONObject(i).getString("astrologerName");
                                String consultationTime = videos.getJSONObject(i).getString("consultationTime");
                                String callDuration = videos.getJSONObject(i).getString("callDuration");
                                String callAmount = videos.getJSONObject(i).getString("callAmount");
                                String astrologerImageFile = videos.getJSONObject(i).getString("astrologerImageFile");
                                String astrologerServiceRs = videos.getJSONObject(i).getString("astrologerServiceRs");
                                String astroWalletId = videos.getJSONObject(i).getString("astroWalletId");
                                String urlText = videos.getJSONObject(i).getString("urlText");
                                String consultationMode = videos.getJSONObject(i).getString("consultationMode");
                                String callChatId = videos.getJSONObject(i).getString("callChatId");
                                String durationUnitType = videos.getJSONObject(i).optString("durationUnitType");
                                String callDurationMin = videos.getJSONObject(i).optString("calldurationmin");


                                callHistoryBean.setUserPhoneNo(userPhoneNo);
                                callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                                callHistoryBean.setAstrologerName(astrologerName);
                                callHistoryBean.setConsultationTime(consultationTime);
                                callHistoryBean.setCallDuration(callDuration);
                                callHistoryBean.setCallAmount(callAmount);
                                callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                                callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                                callHistoryBean.setAstroWalletId(astroWalletId);
                                callHistoryBean.setUrlText(urlText);
                                callHistoryBean.setConsultationMode(consultationMode);
                                callHistoryBean.setCallChatId(callChatId);
                                callHistoryBean.setType("Call");
                                callHistoryBean.setDurationUnitType(durationUnitType);
                                callHistoryBean.setCallDurationMin(callDurationMin);

                                callHistoryList.add(callHistoryBean);
                            }
                        }
                    }
                    if (jsonObject.has("consultations")) {
                        consultations = jsonObject.getJSONArray("consultations");

                        if (consultations != null && consultations.length() > 0) {
                            for (int i = 0; i < consultations.length(); i++) {
                                callHistoryBean = new CallHistoryBean();
                                String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                                String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                                String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                                String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                                String callDuration = consultations.getJSONObject(i).getString("callDuration");
                                String callAmount = consultations.getJSONObject(i).getString("callAmount");
                                String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                                String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                                String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                                String urlText = consultations.getJSONObject(i).getString("urlText");
                                String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                                String callChatId = consultations.getJSONObject(i).getString("callChatId");
                                String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                                String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");



                                callHistoryBean.setUserPhoneNo(userPhoneNo);
                                callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                                callHistoryBean.setAstrologerName(astrologerName);
                                callHistoryBean.setConsultationTime(consultationTime);
                                callHistoryBean.setCallDuration(callDuration);
                                callHistoryBean.setCallAmount(callAmount);
                                callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                                callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                                callHistoryBean.setAstroWalletId(astroWalletId);
                                callHistoryBean.setUrlText(urlText);
                                callHistoryBean.setConsultationMode(consultationMode);
                                callHistoryBean.setCallChatId(callChatId);
                                callHistoryBean.setType("Call");
                                callHistoryBean.setDurationUnitType(durationUnitType);
                                callHistoryBean.setCallDurationMin(callDurationMin);


                                callHistoryList.add(callHistoryBean);
                            }
                        }
                    }
                    if (jsonObject.has("chats")) {
                        Log.d("testhistory", "Called w8574532fdt");
                        chats = jsonObject.getJSONArray("chats");
                        if (chats != null && chats.length() > 0) {
                            for (int i = 0; i < chats.length(); i++) {

                                callHistoryBean = new CallHistoryBean();
                                String userPhoneNo = chats.getJSONObject(i).getString("userPhoneNo");
                                String astrologerPhoneNo = chats.getJSONObject(i).getString("astrologerPhoneNo");
                                String astrologerName = chats.getJSONObject(i).getString("astrologerName");
                                String consultationTime = chats.getJSONObject(i).getString("consultationTime");
                                String callDuration = chats.getJSONObject(i).getString("callDuration");
                                String callAmount = chats.getJSONObject(i).getString("callAmount");
                                String astrologerImageFile = chats.getJSONObject(i).getString("astrologerImageFile");
                                String astrologerServiceRs = chats.getJSONObject(i).getString("astrologerServiceRs");
                                String astroWalletId = chats.getJSONObject(i).getString("astroWalletId");
                                String urlText = chats.getJSONObject(i).getString("urlText");
                                String consultationMode = chats.getJSONObject(i).getString("consultationMode");
                                String callChatId = chats.getJSONObject(i).getString("callChatId");
                                String aiAstroId = chats.getJSONObject(i).optString("aiai");
                                String astroExpertise = chats.getJSONObject(i).optString("astroExpertise");
                                String astroImageFileLarge = chats.getJSONObject(i).optString("astroImageFileLarge");
                                boolean isFreeForChat = chats.getJSONObject(i).optBoolean("iofch");
                                String durationUnitType = chats.getJSONObject(i).optString("durationUnitType");
                                String callDurationMin = chats.getJSONObject(i).optString("calldurationmin");


                                callHistoryBean.setUserPhoneNo(userPhoneNo);
                                callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                                callHistoryBean.setAstrologerName(astrologerName);
                                callHistoryBean.setConsultationTime(consultationTime);
                                callHistoryBean.setCallDuration(callDuration);
                                callHistoryBean.setCallAmount(callAmount);
                                callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                                callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                                callHistoryBean.setAstroWalletId(astroWalletId);
                                callHistoryBean.setUrlText(urlText);
                                callHistoryBean.setConsultationMode(consultationMode);
                                callHistoryBean.setCallChatId(callChatId);
                                callHistoryBean.setType("Chat");
                                callHistoryBean.setAiAstroId(aiAstroId);
                                callHistoryBean.setAstroExpertise(astroExpertise);
                                callHistoryBean.setAstroImageFileLarge(astroImageFileLarge);
                                callHistoryBean.setFreeForChat(isFreeForChat);
                                callHistoryBean.setDurationUnitType(durationUnitType);
                                callHistoryBean.setCallDurationMin(callDurationMin);



                                callHistoryList.add(callHistoryBean);
                            }
                        }
                    }
                    if (jsonObject.has("livesessions")) {
                        consultations = jsonObject.getJSONArray("livesessions");
                        if (consultations != null && consultations.length() > 0) {
                            for (int i = 0; i < consultations.length(); i++) {
                                callHistoryBean = new CallHistoryBean();
                                String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                                String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                                String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                                String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                                String callDuration = consultations.getJSONObject(i).getString("callDuration");
                                String callAmount = consultations.getJSONObject(i).getString("callAmount");
                                String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                                String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                                String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                                String urlText = consultations.getJSONObject(i).getString("urlText");
                                String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                                String callChatId = consultations.getJSONObject(i).getString("callChatId");
                                String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                                String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");


                                callHistoryBean.setUserPhoneNo(userPhoneNo);
                                callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                                callHistoryBean.setAstrologerName(astrologerName);
                                callHistoryBean.setConsultationTime(consultationTime);
                                callHistoryBean.setCallDuration(callDuration);
                                callHistoryBean.setCallAmount(callAmount);
                                callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                                callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                                callHistoryBean.setAstroWalletId(astroWalletId);
                                callHistoryBean.setUrlText(urlText);
                                callHistoryBean.setConsultationMode(consultationMode);
                                callHistoryBean.setCallChatId(callChatId);
                                callHistoryBean.setType("Call");
                                callHistoryBean.setDurationUnitType(durationUnitType);
                                callHistoryBean.setCallDurationMin(callDurationMin);


                                callHistoryList.add(callHistoryBean);
                            }
                        }
                    }

                    if (callHistoryList.size() > 0) {
                        Collections.sort(callHistoryList);
                    }

                    //UI process
                    handler.post(() -> {
                        // UI thread
                        try {

                            if (ActAppModule.wallet_price_txt != null) {
                                String balance = getResources().getString(R.string.astroshop_rupees_sign) + com.ojassoft.astrosage.varta.utils.CUtils.convertAmtIntoIndianFormat(bal);
                                ActAppModule.wallet_price_txt.setText(balance);
                            }
                            if (lastConsultAdapter != null) {
                                callHistoryListFinal.clear();
                                callHistoryListFinal.addAll(callHistoryList);
                                Log.d("callHistoryListFinal", "callHistoryListFinal: " + callHistoryListFinal.size());
                                lastConsultAdapter.notifyDataSetChanged();
                            }
                            hideHistoryTitle();

                        } catch (Exception e) {
                            //
                        }
                    });
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    public HashMap<String, String> getCallChatAstroParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(activity));
        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity);
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(activity);
        try {
            if (isLogin) {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(activity));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity));
            } else {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, "");
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FETCHALL, "1");
        headers.put(CGlobalVariables.PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISMINASTROLIST, "1");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(activity));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        return headers;
    }

    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(activity));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(activity));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(activity));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(activity));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IGNORE_ASTRO, "true");
            headers.put(NOT_SHOW_NEGATIVE_ASTRO, "1");
        } catch (Exception e) {
            //Log.e("PrefetchHistoryData", "headers ex: "+e);
        }
        return headers;
    }

    Handler handlerForTransition;

    private void startQuestionTransition() {
        if (handlerForTransition == null) {
            handlerForTransition = new Handler();
        }
        handlerForTransition.post(questionRunnable); // Start handler when activity is visible
    }

    private void stopQuestionTransition() {
        if (handlerForTransition != null && questionRunnable != null) {
            handlerForTransition.removeCallbacks(questionRunnable); // Stop handler to prevent memory leaks
        }
    }

    public static String randomQuestion = "";
    public static String randomScreen;

    private void updateQuestionWithAnimation() {
        try {
            if (((ActAppModule) activity).suggestedQuestionHelperClass == null) {
                return;
            }
            switchRandomQuestionFromScreens();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;


            // Slide out to the right
            ObjectAnimator slideOut = ObjectAnimator.ofFloat(chatBoxTv, "translationX", 0, screenWidth);
            slideOut.setDuration(1000);
            slideOut.start();

            slideOut.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        String safeText = randomQuestion.replace("'","'"); // smart to plain apostrophe
                        chatBoxTv.setText(safeText);
                        chatBoxTv.setTranslationX(-screenWidth); // Move TextView off-screen to the left
                        // Slide in from left to right
                        ObjectAnimator slideIn = ObjectAnimator.ofFloat(chatBoxTv, "translationX", -screenWidth, 0);
                        slideIn.setDuration(500);
                        slideIn.start();

                    } catch (Exception e){
                        //
                    }
                }
            });
        } catch (Exception e){
            //
        }
    }

    private void switchRandomQuestionFromScreens() {
        if(activity==null ||((ActAppModule) activity).suggestedQuestionHelperClass == null)
            return;
        HashMap<String, ArrayList<String>> questionMap = ((ActAppModule) activity).suggestedQuestionHelperClass.questionMap;
        if (questionMap == null) {
            Log.e(TAG, "questionMap: empty" );
            return ;
        }

        // Get a random screenId
        Set<String> keySet = questionMap.keySet();
        if (keySet.isEmpty()) {
            Log.e(TAG, "keySet: empty" );
            return ;
        }
        List<String> keys = new ArrayList<>(keySet);
        Random random = new Random();
        randomScreen = keys.get(random.nextInt(keys.size()));

        // Get a random question from the selected screen
        List<String> questions = questionMap.get(randomScreen);
        randomQuestion = (questions != null && !questions.isEmpty()) ?
                questions.get(random.nextInt(questions.size())) :
                "No Questions Available";
    }


    public void getHomeScreenDataFromServer() {
        getHomeScreenDataFromServer(CUtils.getLanguageCode(activity), false);
    }

    /**
     * Fetches home screen data for a specific language and optionally falls back to English.
     *
     * @param languageCode The language code to use for the API request.
     * @param isFallback True when this call is a fallback attempt, to avoid infinite retries.
     */
    private void getHomeScreenDataFromServer(int languageCode, boolean isFallback) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> apiCall = api.getHomeScreenStaticList(getHomeScreenParams(languageCode));
        apiCall.timeout().timeout(10, TimeUnit.SECONDS);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() == null) {
                    retryHomeScreenWithEnglish(languageCode, isFallback);
                    return;
                }
                try {
                    String myResponse = response.body().string();
                    if (myResponse == null || myResponse.trim().isEmpty()) {
                        retryHomeScreenWithEnglish(languageCode, isFallback);
                        return;
                    }
                    com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(
                            activity,
                            com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_SCREEN_UI_RESPONSE_KEY,
                            myResponse
                    );
                    parseWholeResponse(myResponse);
                } catch (Exception e) {
                    Log.e("KundliModules_Frag", "onResponse: Exception occurred:" + e);
                    retryHomeScreenWithEnglish(languageCode, isFallback);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                retryHomeScreenWithEnglish(languageCode, isFallback);
            }
        });
    }

    /**
 * Parses the complete home screen API response and updates UI data sources.
 * <p>
 * This method processes the full JSON response from the home screen API, extracting and parsing:
 * <ul>
 *   <li>ReportServiceDetails (delegates to parseReportGsonData)</li>
 *   <li>ProductsCategoryName (updates productCategoryList and homeProductAdapter)</li>
 *   <li>LearnAstrologyVideoDetails (updates sliderModels via parseLearnVideoData)</li>
 * </ul>
 * It updates the corresponding adapters and lists in the fragment for proper UI display.
 *
 * @param myResponse The full JSON response string from the home screen API.
 */
private void parseWholeResponse(String myResponse) {
        try {
            if(myResponse==null){
                return;
            }
            parseReportGsonData(myResponse);
            JSONObject responseObj = new JSONObject(myResponse);

            // Extract ProductsCategoryName JSONArray
            JSONArray productsCategoryArray = responseObj.getJSONArray("ProductsCategoryName");
            productCategoryList = parseCategories(productsCategoryArray);
            productLayout.setVisibility(productCategoryList.isEmpty() ? View.GONE : View.VISIBLE);
            homeProductAdapter.setCategoryList(productCategoryList);

            JSONArray videoArray = responseObj.getJSONArray("YouTubeVideos");
            youtubeVideoBeans = parseVideoResponseArray(videoArray);
            videoLayout.setVisibility(youtubeVideoBeans.isEmpty() ? View.GONE : View.VISIBLE);
            videoHomeAdapter.setData(youtubeVideoBeans);

            // Extract LearnAstrologyVideoDetails JSONArray
            JSONArray learnAstrologyArray = responseObj.getJSONArray("LearnAstrologyVideoDetails");
            parseLearnVideoData(learnAstrologyArray);


            JSONArray articlesArray = responseObj.getJSONArray("Articles");
            ParseArticlesData(articlesArray);
        }catch (Exception e){
            Log.e("KundliModule_frag", "parseWholeResponse: parsing error: "+e);
        }
    }

    /**
 * Parses a JSONArray of article data and updates the articleList with the parsed ArticleModel objects.
 * Uses Gson for conversion from JSON to model list and updates the adapter for UI refresh.
 * Handles null and parsing errors gracefully.
 *
 * @param articlesArray JSONArray containing article data to parse
 */
private void ParseArticlesData(JSONArray articlesArray) {
        if (articlesArray == null) return;
        try {
            // Use Gson to convert JSONArray to List<ArticleModel>
            Gson gson = new Gson();
            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<ArticleModel>>(){}.getType();
            String jsonStr = articlesArray.toString();
            ArrayList<ArticleModel> parsedList = gson.fromJson(jsonStr, listType);
            if (articleList == null) {
                articleList = new ArrayList<>();
            } else {
                articleList.clear();
            }
            if (parsedList != null) {
                articleList.addAll(parsedList);
            }
            articleLayout.setVisibility(articleList.isEmpty() ? View.GONE : View.VISIBLE);
            if (homeArticlesAdapter != null) {
                homeArticlesAdapter.setArticleList(articleList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<YoutubeVideoBean> parseVideoResponseArray(JSONArray videoArray) {
    try {
        if (youtubeVideoBeans == null) {
            youtubeVideoBeans = new ArrayList<>();
        } else {
            youtubeVideoBeans.clear();
        }
        for (int i = 0; i < videoArray.length(); i++) {
            JSONObject videoObj = videoArray.getJSONObject(i);
            YoutubeVideoBean youtubeVideoBean = new YoutubeVideoBean();
            youtubeVideoBean.setVideoId(videoObj.optString("VideoId"));
            youtubeVideoBean.setTitle(videoObj.optString("Title"));
            youtubeVideoBean.setDescription(videoObj.optString("Description"));
            youtubeVideoBean.setThumbnail(videoObj.optString("Thumbnail"));
            youtubeVideoBean.setPublishedAt(videoObj.optString("PublishedAt"));
            youtubeVideoBeans.add(youtubeVideoBean);
        }
//        Log.e("videolCheck", "parseLearnVideoData: " + youtubeVideoBeans + "\n");
    }catch (Exception e){
        Log.e("KundliModule_frag", "parseVideoResponseArray: parsing error: "+e);
    }
        return youtubeVideoBeans;
    }

    public Map<String, String> getHomeScreenParams() {
        return getHomeScreenParams(CUtils.getLanguageCode(activity));
    }

    /**
     * Builds the home screen API parameters for a specific language code.
     *
     * @param languageCode The language code to send to the API.
     */
    public Map<String, String> getHomeScreenParams(int languageCode) {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(activity));
            headers.put("langcode", String.valueOf(languageCode));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(activity));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(activity));
        } catch (Exception e) {
            Log.e("KundliModules_frag", "headers exception : "+e);
        }
        return headers;
    }

    /**
     * Retries the home screen API using English when a localized response is empty or fails.
     *
     * @param languageCode The language code that failed.
     * @param isFallback True if this is already a fallback attempt.
     */
    private void retryHomeScreenWithEnglish(int languageCode, boolean isFallback) {
        if (!isFallback && languageCode != CGlobalVariables.ENGLISH) {
            getHomeScreenDataFromServer(CGlobalVariables.ENGLISH, true);
        }
    }


    private void parseLearnVideoData(JSONArray videoArray) {
try{
    if (sliderModels == null) {
        sliderModels = new ArrayList<>();
    } else {
        sliderModels.clear();
    }
        for (int i = 0; i < videoArray.length(); i++) {
            JSONObject videoObj = videoArray.getJSONObject(i);
            SliderModal sliderModal = new SliderModal();
            sliderModal.setVideoid(videoObj.optString("Videoid"));
            sliderModal.setTitle(videoObj.optString("Title"));
            sliderModal.setDescription(videoObj.optString("Description"));
            sliderModal.setVideo_url(videoObj.optString("VideoURL"));
            sliderModal.setUrl(videoObj.optString("ImageURL"));
            sliderModal.setThumbnailImageURL(videoObj.optString("ThumbnailImageURL"));
            sliderModal.setIsfeatured(videoObj.optString("Isfeatured"));
            sliderModal.setCategoryName(videoObj.optString("CategoryName"));
            sliderModels.add(sliderModal);
        }
//            Log.e("videolCheck", "parseLearnVideoData: "+sliderModels+"\n" );
              learnAstroLayout.setVisibility(sliderModels.isEmpty() ? View.GONE : View.VISIBLE);
            learnVideoAdapter.setDataList(sliderModels);
        } catch (Exception ex) {
            //
        }
    }

    public static String LANG_CHANGE_KEY = "lang_change_key";
    // Define cache duration as a constant for clarity and maintainability
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    private static final String TAG = "KundliModules_Frag_TAG";


    /**
     * Decides whether to fetch home and AI astrologer data from the server or use cached data.
     * Optimizes for freshness and efficiency. Handles language change and cache consistency.
     */
    private void getDataFromServerOrLocal() {
        try {
            // Retrieve last API call timestamp and current time
            String lastApiCallTimestamp = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_API_CACHE_TIME_KEY,"");
//            long currentTime = System.currentTimeMillis();

            String date = CUtils.getDialogDate(0);
//             Log.e(TAG, "getDataFromServerOrLocal: lastApiCallTimestamp =  "+lastApiCallTimestamp );
//            Log.e(TAG, "getDataFromServerOrLocal: date =  "+date );
            boolean isCacheValid = CUtils.checkDatesSame(lastApiCallTimestamp);
            // Check if cache is still valid (within 24 hours)
//            Log.e(TAG, "getDataFromServerOrLocal: isCacheValid =  "+isCacheValid );
            int lastLangCode = CUtils.getIntData(activity, LANG_CHANGE_KEY, 0);
            int currentLangCode = CUtils.getLanguageCode(activity);

            // If language has changed, force refresh and update cache keys
            if (isCacheValid && lastLangCode != currentLangCode) {
             //   Log.e(TAG, "getDataFromServerOrLocal: get from server due to lang change. " );
                com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_API_CACHE_TIME_KEY, date);
                com.ojassoft.astrosage.varta.utils.CUtils.saveIntData(activity, LANG_CHANGE_KEY, currentLangCode);
                getAIAstrologersListFromServer();
                getHomeScreenDataFromServer();
                return;
            }

            if (isCacheValid) {
                // Try to load cached data
                String homeScreenResponse = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_SCREEN_UI_RESPONSE_KEY, "");
                String aiAstrologerList = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_LIST, "");

                boolean cacheMiss = false;
                // Handle AI astrologer list
                if (aiAstrologerList.isEmpty()) {
              //      Log.e(TAG, "getDataFromServerOrLocal:  ai list is empty " );
                    getAIAstrologersListFromServer();
                    cacheMiss = true;
                } else {
                    try {
                       // Log.e(TAG, "getDataFromServerOrLocal: get from cache. AI list  " );
                        parseAIAstrologerList(aiAstrologerList);
                    } catch (Exception e) {
                       // Log.e(TAG, "Error parsing AI astrologer list from cache", e);
                        getAIAstrologersListFromServer();
                        cacheMiss = true;
                    }
                }

                // Handle home screen response
                if (homeScreenResponse.isEmpty()) {
                    //Log.e(TAG, "getDataFromServerOrLocal: home screen data list is empty " );
                    getHomeScreenDataFromServer();
                    cacheMiss = true;
                } else {
                    try {
                       // Log.e(TAG, "getDataFromServerOrLocal: get from cache. All home data list  " );
                        parseWholeResponse(homeScreenResponse);
                    } catch (Exception e) {
                      //  Log.e(TAG, "Error parsing home screen response from cache", e);
                        getHomeScreenDataFromServer();
                        cacheMiss = true;
                    }
                }

                // If either cache is missing or failed to parse, update the cache timestamp
                if (cacheMiss) {
                    com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_API_CACHE_TIME_KEY, date);
                }
            } else {
                // Cache expired or missing: fetch both from server and update cache timestamp
               // Log.e(TAG, "getDataFromServerOrLocal: get from server   " );
                com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_API_CACHE_TIME_KEY, date);
                getAIAstrologersListFromServer();
                getHomeScreenDataFromServer();
            }
        } catch (Exception ex) {
            Log.e("KundliModules_Frag", "Error in getDataFromServerOrLocal: ", ex);
            // As a fallback, force fetch from server
            getAIAstrologersListFromServer();
            getHomeScreenDataFromServer();
        }
    }

    /**
 * Parses the ReportServiceDetails JSONArray from the given API response string.
 * <p>
 * This method extracts the "ReportServiceDetails" array from the provided JSON response,
 * deserializes it into a list of ServicelistModal objects, and updates both the
 * reportCategoryList field and the homeReportItemAdapter with the parsed data.
 *
 * @param saveData The full JSON response string from the home screen API.
 */
private void parseReportGsonData(String saveData) {
    try {
        Gson gson = new Gson();
        // Parse the root JSON object from the full API response string
        JsonObject rootObj = gson.fromJson(saveData, JsonObject.class);
        
        // Extract the "ReportServiceDetails" JSONArray from the root object
        JsonArray reportServiceArray = rootObj.getAsJsonArray("ReportServiceDetails");
        
        // Deserialize the JSONArray into an ArrayList of ServicelistModal objects
        ArrayList<ServicelistModal> servicelistModals = gson.fromJson(
            reportServiceArray, new TypeToken<ArrayList<ServicelistModal>>(){}.getType()
        );
        
        // Update the reportCategoryList field so the parsed data is available for other uses
        if (reportCategoryList != null) {
            reportCategoryList.clear();
            reportCategoryList.addAll(servicelistModals);
        }

        reportLayout.setVisibility(reportCategoryList.isEmpty() ? View.GONE : View.VISIBLE);
        
        // Update the homeReportItemAdapter with the new data if it exists
        if (homeReportItemAdapter != null) {
            homeReportItemAdapter.setReportList(servicelistModals);
        }
    } catch (Exception ex) {
        Log.e("KundliModules_Frag", "Error in parsingReportGSON: ", ex);
    }
}
    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter =
                new IntentFilter(ActAppModule.SuggestedQuestionBroadcast.ACTION_QUESTION_READY);

        LocalBroadcastManager
                .getInstance(requireContext())
                .registerReceiver(questionReadyReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager
                .getInstance(requireContext())
                .unregisterReceiver(questionReadyReceiver);
    }

}
