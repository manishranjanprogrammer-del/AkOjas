package com.ojassoft.astrosage.ui.act;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.libojassoft.android.utils.LibCGlobalVariables.UPDATE_NOTIFICATION_COUNT;
import static com.libojassoft.android.utils.LibCGlobalVariables.activity;
import static com.ojassoft.astrosage.ui.act.OutputMasterActivity.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.ui.act.OutputMasterActivity.LAGANA_CHART_QUESTION;
import static com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity.BACK_FROM_PLAN_PURCHASE_AD_SCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.APP_THEME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CLICKED_CATEGORY_ENUM_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_OPENED_FROM_K_AI_CHAT_BTN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAST_LANG_CODE_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OPEN_KUNDALI_VIRTUAL_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OPEN_KUNDALI_VIRTUAL_URLS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SCREEN_ID_DHRUV;
import static com.ojassoft.astrosage.utils.CGlobalVariables.USERID;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity.BACK_FROM_PROFILECHATDIALOG;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AUTO_OPEN_LOGIN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FREE_OFFER_TAKEN_NOT_TRANSACTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_KUNDLI_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_GOOGLE_FACEBOOK_VISIBLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_IS_CHAT_FOR_HOME_CATEGORY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_MODULE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_SELECTION_SCRREN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_FOLLOW;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_TRANSACTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_OFFER_TAKEN_NOT_TRANSACTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.isFromConsultTab;
import static com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsInstanceId;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources;
import static com.ojassoft.astrosage.varta.utils.CUtils.getSelectedChannelID;
import static com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected;
import static com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail;
import static com.ojassoft.astrosage.varta.utils.CUtils.removeAINotificationPeriodicTask;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.models.NotificationModel;
import com.libojassoft.android.utils.AESDecryption;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.BeanUserMapping;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.interfaces.NavigateToAstroshopFrag;
import com.ojassoft.astrosage.jinterface.IChooseLanguageFragment;
import com.ojassoft.astrosage.jinterface.NotificationCenterCallback;
import com.ojassoft.astrosage.misc.ActivityDetailWithExtraData;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.GetCityInBackground;
import com.ojassoft.astrosage.misc.GetTagManagerDataService;
import com.ojassoft.astrosage.misc.PersonalizedCategoryENUM;
import com.ojassoft.astrosage.misc.SaveAstroLogerInfoService;
import com.ojassoft.astrosage.misc.ServiceToGetPurchasePlanDetails;
import com.ojassoft.astrosage.misc.SubsAndUnsubsTopics;
import com.ojassoft.astrosage.model.AstrologerInfo;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.model.SuggestedQuestionHelperClass;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.act.indnotes.NotesActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.Astroshop_Frag;
import com.ojassoft.astrosage.ui.fragments.ChooseLanguageFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.Horoscope_Frag;
import com.ojassoft.astrosage.ui.fragments.KundliModules_Frag;
import com.ojassoft.astrosage.ui.fragments.Panchang_Frag;
import com.ojassoft.astrosage.ui.fragments.RenewPlanDialog;
import com.ojassoft.astrosage.ui.fragments.Reports_frag;
import com.ojassoft.astrosage.ui.fragments.Video_Frag;
import com.ojassoft.astrosage.ui.fragments.vratfragment.Frag_Year;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.GetDefaultKundliDataService;
import com.ojassoft.astrosage.utils.MaterialSearchView;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.utils.VolleySingletonForDefaultHttp;
import com.ojassoft.astrosage.utils.indnotes.DateTimeUtils;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.dialog.AstroBusyAlertDialog;
import com.ojassoft.astrosage.varta.dialog.BottomSheetDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentFailDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentSucessfulDialog;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.model.NextOfferBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.receiver.AstroLiveDataManager;
import com.ojassoft.astrosage.varta.receiver.OngoingAICallData;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.service.AIVoiceCallingService;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.service.PreFetchAstroDataservice;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
import com.ojassoft.astrosage.varta.service.PreFetchLiveAstroDataservice;
import com.ojassoft.astrosage.varta.service.PrefetchHistoryDataService;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.ui.fragments.HomeFragment1;
import com.ojassoft.astrosage.varta.ui.fragments.OfferRechargeDialog;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

//import android.widget.GridView;
/*import com.actionbarsherlock.view.Menu;
 import com.actionbarsherlock.view.MenuItem;*/

/**
 * Main application module activity that serves as the central hub for the Astrosage Kundli application.
 * This activity manages the core functionality including navigation, user authentication, and feature access.
 * <p>
 * Key Features:
 * - Handles main navigation and tab management
 * - Manages user authentication and session
 * - Controls access to premium features
 * - Handles deep linking and notifications
 * - Manages in-app purchases and subscriptions
 * - Provides access to various astrological services
 * <p>
 * Fragments Used:
 * <p>
 * - HomeNavigationDrawerFragment: Main navigation drawer fragment that provides access to:
 * - User profile and authentication
 * - Menu items and navigation options
 * - Settings and preferences
 * - Premium features and subscriptions
 * <p>
 * <p>
 * - KundliModules_Frag (Home):
 * Main home screen fragment
 * Displays kundli-related modules and features
 * Handles user's primary astrological calculations and predictions
 * Manages the home screen layout and navigation
 * - Frag_Year (2025):
 * Yearly calendar and events fragment
 * Shows yearly vrat (fasting) dates and important dates
 * Displays annual astrological events and festivals
 * * Only shown for Hindi and English language users
 * - Astroshop_Frag(Astro shop):
 * Astrological shop interface
 * Displays products and services available for purchase
 * Handles product categories and details
 * - HomeFragment1 (Consult/varta Tab):
 * Consultation interface
 * Manages chat and consultation features
 * Handles user-astrologer interactions
 * Only shown for non-Hindi/English languages
 * - Reports_frag(Report):
 * Astrological reports section
 * Generates and displays various astrological reports
 * Manages report generation and viewing
 * - Video_Frag(Video):
 * Video content section
 * Displays astrological video content
 * Manages video playback and categories
 * - Panchang_Frag(Panchang):
 * Panchang (Hindu calendar) information
 * Manages panchang calculations and display
 * - Horoscope_Frag(Horoscope):
 * Horoscope section
 * Displays daily, weekly, and monthly horoscopes
 * Manages zodiac sign-based predictions
 * <p>
 * <p>
 * The activity implements several interfaces:
 * - IChooseLanguageFragment: For language selection functionality
 * - NotificationCenterCallback: For handling notifications
 * - NavigateToAstroshopFrag: For navigation to astro shop
 * - VolleyResponse: For network request handling
 * - GlobalRetrofitResponse: For API response handling
 */

public class ActAppModule extends BaseInputActivity implements IChooseLanguageFragment, NotificationCenterCallback, NavigateToAstroshopFrag, VolleyResponse, GlobalRetrofitResponse {

    public static final String SKU_ASKQUESTION_PLAN = "ask_a_question";
    static final String BigHorscopeId = "Bighorscope_Main_Module";
    //public Typeface typeface;
    static final String GemstoneId = "Gemstone_Main_Module";
    static final String YantrasId = "Yantra_Main_Module";
    static final String RudrakshaId = "Rudraksha_Main_Module";
    static final String MalaId = "Mala_Main_Module";
    static final String NavagrahId = "Navagrah_Yantra_Main_Module";
    static final String JadiId = "Jadi_Main_Module";
    static final String BrihatHorscopeId = "Brihathorscope_Main_Module";
    static final String fengshuiId = "Fengshui_Main_Module";
    static final String miscId = "Misc_Main_Module";
    static final int BRIHATHOROSCOPE = 0;
    static final int GEMSTONE = 1;
    static final int YANTRAS = 2;
    static final int RUDRAKSHA = 3;
    static final int MALA = 4;
    static final int NAVAGRAH = 5;
    static final int JADI = 6;
    static final int SERVICE = 7;
    static final int ASTROLOGER = 8;
    static final int FENGSHUI = 9;
    static final int MISC = 10;
    private static final String TITLE = "ASTROSAGE KUNDLI";
    private static final int PERMISSION_REQ_CODE = 1;
    public static int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public static boolean isExitBannerAdShow = true;
    public static String brihatHorscopeDeepLinkUrl = "https://buy.astrosage.com/virtual/astrosage-brihat-horoscope-url";
    private static final String gemStoneDeeplink = "https://buy.astrosage.com/gemstone";
    private static final String yantraDeeplink = "https://buy.astrosage.com/yantra";
    private static final String rudrakshDeeplink = "https://buy.astrosage.com/rudraksha";
    private static final String malaDeeplink = "https://buy.astrosage.com/mala";
    private static final String navgrahDeeplink = "https://buy.astrosage.com/navagrah-yantra";
    private static final String jadiDeeplink = "https://buy.astrosage.com/jadi-tree-roots";
    private static final String fengshuiDeeplink = "https://buy.astrosage.com/fengshui";
    private static final String miscDeeplink = "https://buy.astrosage.com/miscellaneous";
    private static final String liveAstrologerDeeplink = "https://varta.astrosage.com/live-astrologers";
    public static final int runTime = 10;//30000
    public boolean isSearchBarOpen;
    public HomeNavigationDrawerFragment drawerFragment;
    public ArrayList<CustomAddModel> sliderList;
    protected Fragment mFrag;
    int moduleType;
    boolean isExitApp = false;
    public ViewPager mViewPager;
    TextView tvTitle;
    //TextView walletPriceTxt;
    //LinearLayout walletLayout;
    //RelativeLayout walletBoxLayout;
    String[] titles = new String[8];
    ImageView ivToggleImage, imgMoreItem;
    String articleIdLastPathSegment = null;
    String articleId = "";
    String articleBaseUrl = "";
    String TAG = "ActAppModule";
    String sessionedPartnerId = "Astrosage";
    boolean isOtherpartnerId = false;
    CustomProgressDialog pd = null;
    ArrayList<String> arrayList;
    Video_Frag videoFrag;
    HomeFragment1 vartaTabFragment;
    //HomeFragment1 vartaTabFragment;
    AlertDialog alertDialog = null;
    String amount;
    RequestQueue mainQueue;
    boolean isShowLoginScreen;
    private static final int END_CHAT_VALUE = 115;

    private ArrayList<AstrologerDetailBean> searchedAstroList;

    String[][] keys = {{"Kundli", "", "కుండలి", "ஜாதகம்", "कुंडली(पत्रिका)", "ജാതകം", "कुंडली", "কুষ্ঠি", "કુંડળી", "ಕುಂಡಲಿ"},
            {"Matching", "Kundli Milan", "మేళన", "எட்டு பொருத்தம்", "मेलन", "ജാതക പൊരുത്തം", "कुंडली मिलान", "কুষ্ঠি মিলান", "મેળાપ", "ಹೊಂದಾಣಿಕೆ"},
            {"Horoscope", "Rashifal", "రాశి ఫలములు", "ராசி பலன்கள்", "राशी भविष्य", "രാശിഫലം", "राशिफल", "রাশিফল", "રાશી ભવિષ્ય", "ರಾಶಿ ಭವಿಷ್ಯ"},
            {"Predictions", "Faladesh", "జాతక ఫలితములు", "முன்கூறல்", "भविष्यवाण्या", "ഫലപ്രവചനം", "फलादेश", "গণনা", "ફળકથન", "ಭವಿಷ್ಯವಾಣಿ"},
            {"Daily Panchang", "Dainik Panchang", "పంచాంగము", "பஞ்சாங்கம்", "पंचांग", "പഞ്ചാംഗം", "दैनिक पंचांग", "পঞ্জিকা", "પંચાંગ", "ಪಂಚಾಂಗ"},
            {"Ask A Question", "Prashna Puche", "ఒక ప్రశ్న అడగండి", "ஒரு கேள்வி", "एक प्रश्न विचारा", "ഒരു ചോദ്യം ചോദിക്കൂ", "प्रश्न पूछें", "প্রশ্ন জিজ্ঞাসা করুন", "એક પ્રશ્ન પૂછો", "ಪ್ರಶ್ನೆಯನ್ನು ಕೇಳಿ"},
            {"KP System", "", "కే.పి సిస్టమ్", "கே.பி. முறை", "केपी पद्धती", "കെപി സമ്പ്രദായം", "केपी सिस्टम", "কেপি সিস্টেম", "કેપી પદ્ધતિ", "ಕೆ.ಪಿ ವ್ಯವಸ್ಥೆ"},
            {"Lal Kitab", "", "లాల్ కితాబ్", "லால் கிதாப்", "लाल किताब", "ലാൽ കിതാബ്", "लाल किताब", "লাল কিতাব", "લાલ કિતાબ", "ಕೆಂಪು ಪುಸ್ತಕ"},
            {"Varshphal", "Varshfal", "వర్షఫలము", "வருடாந்திர பலன்கள்", "वर्ष फळ", "വർഷഫലം", "वर्षफल", "বর্ষফল", "વર્ષફળ", "ವರ್ಷಫಲ"},
            {"Astro Shop", "", "ఆస్ట్రో షాప్", "ஜோதிட அங்காடி", "ऍस्ट्रो  शॉप", "ആസ്ട്രോ ഷോപ്", "एस्ट्रो शाॅप", "এস্ট্রশপ", "એસ્ટ્રો શોપ", "ಆಸ್ಟ್ರೋ ಶಾಪ್"},
            {"AstroSage TV", "", "ఆస్ట్రోసేజ్ TV", "ஆஸ்ட்ரோசேஜ் டிவி", "ऍस्ट्रोसेज टीव्ही", "ആസ്ട്രോ ടിവി", "एस्ट्रोसेज टीवी", "এস্ট্রস্যাজ টিভি", "એસ્ટ્રોસેજ ટીવી", "ಆಸ್ಟ್ರೋಸೇಜ್ ಟಿವಿ"},
            {"AstroSage Magazine", "AstroSage Patrika", "ఆస్ట్రో సేజ్ మాస పత్రిక", "அஸ்ட்ரோஸேஜ் பத்திரிகை", "ऍस्ट्रोसेज मासिक", "ആസ്ട്രോ സേജ്  മാസിക", "एस्ट्रोसेज पत्रिका", "এসট্রস্যাজ পত্রিকা", "એસ્ટ્રોસેજ પત્રિકા", "ಆಸ್ಟ್ರೋಸೇಜ್  ಲೇಖನಗಳು"},
            {"Learn Astrology", "Jyotish Sikhe", "జ్యోతిషము నేర్చుకోండి", "ஜோதிட பாடம்", "ज्योतिष विद्या शिका", "ജ്യോതിഷം പഠിക്കൂ", "ज्योतिष सीखें", "জ্যোতিষ শিখুন", "જ્યોતિષવિદ્યા સીખો", "ಜ್ಯೋತಿಷ್ಯ ಕಲಿಕೆ"},
            {"Porutham", "", "పొంతనము", "பத்து பொருத்தம்", "पोरुथम", "പൊരുത്തം", "पोरुथम", "দক্ষিণ ভারতীয় কুষ্ঠি মিলান", "પોરુથમ", "ಪೌರೋಹಿತ್ಯ"},
            {"Free Matrimony", "Muft matrimony", "ఉచిత మాట్రిమోని", "இலவச திருமணம்", "निःशुल्क विवाह विचार", "സൗജന്യ മാട്രിമോണി", "मुफ्त मेट्रीमनी", "বিনামূল্যে ম্যাট্রিমনি", "મફત લગ્ન", "ಉಚಿತ ವೈವಾಹಿಕ"},

            {"Horoscope 2026", "Rashifal 2026", "", "", "", "", "राशिफल 2026", "রাশিফল 2026", "", ""},
            {"Jupiter Transit 2026", "Guru Gochar 2026", "", "", "", "", "गुरु गोचर 2026", "", "", ""},
            {"Saturn Transit 2026", "Shani Gochar 2026", "", "", "", "", "शनि गोचर 2026", "", "", ""},
            {"Love Horoscope 2026", "Prem Rashifal 2026", "", "", "", "", "प्रेम राशिफल 2026", "", "", ""},
            {"Career Horoscope 2026", "Career Rashifal 2026", "", "", "", "", "करियर राशिफल 2026", "", "", ""},
            {"Chinese Horoscope 2026", "Chini Rashifal 2026", "", "", "", "", "चीनी राशिफल 2026", "", "", ""},
            {"Education Horoscope 2026", "Shakshik Rashifal 2026", "", "", "", "", "शैक्षिक राशिफल 2026", "", "", ""},
            {"Finance Horoscope 2026", "Arthik Rashifal 2026", "", "", "", "", "आर्थिक राशिफल 2026", "", "", ""},
            {"Lalkitab Horoscope 2026", "Lal Kitab Rashifal 2026", "", "", "", "", "लाल किताब राशिफल 2026", "", "", ""},
            {"Numerology 2026", "Ank Jyotish 2026", "", "", "", "", "अंक ज्योतिष 2026", "", "", ""},
            {"Vivah Muhurat 2026", "", "", "", "", "", "विवाह मुहूर्त 2026", "", "", ""},
            {"Mundan Muhurat 2026", "", "", "", "", "", "मुंडन मुहूर्त 2026", "", "", ""},
            {"Griha Muhurat 2026", "Griha Pravesh Muhurat 2026", "", "", "", "", "गृह प्रवेश मुहूर्त 2026", "", "", ""},
            {"Namkaran Muhurat 2026", "", "", "", "", "", "नामकरण मुहूर्त 2026", "", "", ""},
            {"Annaprashan Muhurat 2026", "", "", "", "", "", "अन्नप्राशन मुहूर्त 2026", "", "", ""},
            {"Karnavedha Muhurat 2026", "", "", "", "", "", "कर्णवेध मुहूर्त 2026", "", "", ""},
            {"Vidyarambh Muhurat 2026", "", "", "", "", "", "विद्यारम्भ मुहूर्त 2026", "", "", ""},
            {"Ketu Transit 2026", "Ketu Gochar 2026", "", "", "", "", "केतु गोचर 2026", "", "", ""},
            {"Rahu Transit 2026", "Rahu Gochar 2026", "", "", "", "", "राहु गोचर 2026", "", "", ""},
            {"Lunar Eclipse 2026", "Chandra Grahan 2026", "", "", "", "", "चंद्र ग्रहण 2026", "", "", ""},
            {"Solar Eclipse 2026", "Surya Grahan 2026", "", "", "", "", "सूर्य ग्रहण 2026", "", "", ""},
            {"Planets in Retrograde 2026", "2026 mein Vakri Grah", "", "", "", "", "2026 में वक्री ग्रह", "", "", ""},
            {"Mercury Retrograde 2026", "2026 mein Vakri Budh", "", "", "", "", "2026 में वक्री बुध", "", "", ""},

            {"Life Report", "Jeevan Report", "జీవిత నివేదిక", "வாழ்க்கை பொது அறிக்கை", "जीवन अहवाल", "ജീവിത വിവരണം", "जीवन रिपोर्ट", "জীবন রিপোর্ট", "આજીવન અહેવાળ", "ಲೈಫ್ ವರದಿ"},
            {"Monthly Report", "Masik Report", "మాస నివేదిక", "மாதாந்திர அறிக்கை", "मासिक अहवाल", "മാസ വിവരണം", "मासिक रिपोर्ट", "মাসিক রিপোর্ট", "માસિક અહેવાળ", "ಮಾಸಿಕ ವರದಿ"},
            {"Daily Report", "Dainik Report", "నిత్య నివేదిక", "தினசரி  அறிக்கை", "दैनिक अहवाल", "ദിവസവിവണം", "दैनिक रिपोर्ट", "দৈনিক রিপোর্ট", "દૈનિક અહેવાળ", "ದೈನಂದಿನ ವರದಿ"},
            {"Sade Sati Report", "", "ఏలినాటి శని నివేదిక", "ஏழரை சனி அறிக்கை", "साडेसाती अहवाल", "ഏഴരശ്ശനി  വിവരണം", "साढ़े साती रिपोर्ट", "সাড়েসাতি  জীবন প্রতিবেদন", "સાડે સાતી અહેવાળ", "ಸಾಡೆ ಸಾತ್ ವರದಿ"},
            {"Ascendant Prediction", "Lagna Bhavishyavani", "లగ్న జాతక ఫలము", "லக்ன பலன்கள்", "लग्न भविष्यवाणी", "ലഗ്ന ഫല പ്രവചനം", "लग्न भविष्यवाणी", "লগ্ন ভবিষ্যতবাণী", "લગ્ન આગાહીઓ", "ಲಗ್ನದ ಭವಿಷ್ಯವಾಣಿ"},
            {"Annual Prediction", "Varshik Report", "వార్షిక జాతక ఫలములు", "வார்ஷிக பலன்கள்", "वार्षिक भविष्यवाणी", "വാർഷിക ഫല പ്രവചനം", "वार्षिक रिपोर्ट", "বার্ষিক ভবিষ্যতবাণী", "વાર્ષિક ફલાદેશ", "ವರ್ಷ ಭವಿಷ್ಯ"},
            {"Mangal Dosh", "", "కుజ దోషం", "செவ்வாய் தோஷம்", "मंगळ दोष", "ചൊവ്വാദോഷം", "मंगल दोष", "মঙ্গল দশা", "મંગળ દોષ", "ಮಂಗಳ ದೋಷ"},
            {"Kaal Sarp Dosh", "", "కాలసర్ప దోషం/యోగం", "கால சர்ப தோஷம்", "काळ सर्प दोष", "കാളസർപ്പ ദോഷം", "कालसर्प दोष", "কালসর্প দোষ", "કાલસર્પ  દોષ", "ಕಾಳ ಸರ್ಪ ದೋಷ"},
            {"Moon Sign", "Chandra Rashi", "జన్మరాశి", "உங்கள் சந்திர ராசி", "चंद्र राशी", "ചന്ദ്ര രാശി", "चंद्रराशि", "চন্দ্ররাশি", "ચંદ્ર રાશિ", "ಚಂದ್ರ ರಾಶಿ"},
            {"Lal Kitab Debt", "Lal Kitab rina", "లాల్ కితాబ్ ఋణాలు", "லால்கிதாப் பாவ கடன்கள்", "लाल किताब ऋण", "ലാൽകിതാബ്‌ഋണം", "लाल किताब ऋण", "লাল কিতাবের ঋণ", "લાલ કિતાબ ઋણ", "ಲಾಲ್ ಕಿತಾಬ್ ಸಾಲ"},
            {"Lal Kitab Teva", "", "లాల్ కితాబ్ తేవ", "லால்கிதாப் டேவா", "लाल किताब तेवा", "ലാൽകിതാബ്‌ തേവ", "लाल किताब टेवा", "লাল কিতাবের তেভা ", "લાલકિતાબ  ટેવા", "ಕೆಂಪು ಪುಸ್ತಕದ (ಲಾಲ್ ಕಿತಾಬ್) ಮಾಹಿತಿ"},
            {"Baby Names", "naamkaran sujhav", "పిల్లల పేర్లు", "பெயர்கள் பரிந்துரைகள்", "मुलांची नावे", "കുട്ടികളുടെ പേരുകൾ", "नामकरण सुझाव", "নামকরণ প্রস্তাবন", "નામકરણ સુઝાવ", "ಶಿಶುವಿನ ನಾಮವಳಿ"},
            {"Lal Kitab Remedies", "Lal Kitab Upay", "లాల్ కితాబ్   పరిహారం", "லால் கிதாப் பரிகார முறைகள்", "लाल किताब उपाय", "ലാൽകിതാബ്‌ പ്രതിവിധി", "लाल किताब उपाय", "লাল কিতাবের প্রতিকার", "લાલ કિતાબ ઉપચાર", "ಲಾಲ್ ಕಿತಾಬ್ ಪರಿಹಾರ"},
            {"Planet Consideration", "Grah Vichar", "గ్రహ పరిగణన", "கிரகத்தின் பரிசீலனை", "ग्रह विचार", "ഗ്രഹചിന്ത", "ग्रह विचार", "গ্রহ বিচার", "ગ્રહ વિચાર", "ಗ್ರಹದ ಪರಿಗಣನೆ"},
            {"Gemstones Report", "Ratna Report", "రత్న నివేదిక", "இரத்தின கற்கள் பற்றிய அறிக்கை", "रत्न अहवाल", "രത്ന കല്ല്  സംവാദം", "रत्न रिपोर्ट", "রত্ন পাথর প্রতিবেদন", "રત્નોના વિચાર", "ರತ್ನದ ಹರಳುಗಳ ಮಾಹಿತಿ"},
            {"Transit Today", "Aaj Ka Gochar", "నేటి సంక్రమణం", "இன்றைய கோச்சாரம்", "आजचे गोचर", "സംക്രമണം", "आज का गोचर", "আজকের গোচরফল", "આજનું ગોચર", "ಈ ದಿನ ಸ್ಥಾನಪಲ್ಲಟ"},
            {"Mahadasha Phala", "Mahadasha ka fal", "మహాదశ ఫలం", "மஹா தசா பலன்கள்", "", "മഹാദാശ ഫലം", "महादशा का फल", "মহাদশার ফল", "મહાદશા ફળ", "ಮಹಾದಶ ಫಲ"},
            {"Nakshatra Report", "", "నక్షత్ర నివేదిక", "நட்சத்திரம் அறிக்கைகள்", "नक्षत्र अहवाल", "നക്ഷത്ര വൃത്താന്തം", "नक्षत्र रिपोर्ट", "নক্ষত্র প্রতিবেদন", "નક્ષત્ર અહેવાલ", "ನಕ್ಷತ್ರ ಫಲ"},

            {"Monthly Calendar", "Masik Panchang", "నెలవారీ క్యాలెండర్", "மாத காலண்டர்", "मासिक कॅलेंडर", "പ്രതിമാസകാല കലണ്ടർ", "मासिक पंचांग", "মাসিক ক্যালেন্ডার", "માસિક કેલેન્ડર", "ಮಾಸಿಕ ಕ್ಯಾಲೆಂಡರ್"},
            {"Hindu Calendar", "", "హిందూ క్యాలెండర్", "இந்து காலண்டர்", "हिंदू कॅलेंडर", "ഹിന്ദു കലണ്ടർ", "हिन्दू कैलेंडर", "হিন্দু ক্যালেন্ডার", "હિન્દુ કેલેન્ડર", "ಹಿಂದೂ ಕ್ಯಾಲೆಂಡರ್"},
            {"Yearly Vrat", "Varshik Vrat", "వార్షిక ఉపవాసం", "ஆண்டு உண்ணாவிரதம்", "वार्षिक उपवास", "വാർഷിക ഉപവാസം", "वार्षिक व्रत", "বার্ষিক উপবাস", "વાર્ષિક ઉપવાસ", "ವಾರ್ಷಿಕ ಉಪವಾಸ"},
            {"Festival 2026", "Tyohar", "ఉత్సవముల", "விழா", "उत्सव", "ഫെസ്റ്റിവൽ", "त्यौहार", "উৎসব", "ત્યોહાર", "ಹಬ್ಬಗಳು"},
            {"Hora", "", "హోర", "ஹோரை", "होरा", "ഹോര", "", "হোরা", "હોરા", "ಹೊರಾ"},
            {"Chogadia", "Choghadiya", "చొగాడియ", "சோகடியா", "चौ घडिया/ चार घटी", "ചോഗഡിയ മുഹൂർത്തം", "चोघडिया", "চৌঘ ড়িয়া", "ચોઘડિયા", "ಚಗೊದಿಯ"},
            {"Do Ghati", "Do Ghati Muhurat", "దో ఘటి", "இரண்டு நாழிகை", "दोन घटी", "ദോ ഗഡി", "दोघटी मुहूर्त", "দো ঘটি", "બે ઘટી", "ಎರಡು ಘಟಿ"},
            {"Rahu Kaal", "", "రాహు కాలము", "ராகுகாலம்", "राहूकाळ", "രാഹുകാലം", "राहु काल", "রাহুকাল", "રાહુ કાળ", "ರಾಹು ಕಾಲ"},
            {"Other Calendars", "Anya Calendar", "వార్షిక క్యాలెండరు", "வருடாந்தர நாட்காட்டி", "इतर कॅलेंडर", "വാർഷിക കലണ്ടർ", "अन्य कैलेंडर", "বার্ষিক ক্যালেন্ডার", "અન્ય કેલેન્ડર", "ವರ್ಷದ ಪಂಚಾಂಗ"},

            {"Daily horoscope", "Dainik Rashifal", "దిన", "தினசரி", "दैनिक राशी भविष्य", "ദിവസ രാശിഫലം", "दैनिक राशिफल", "দৈনিক রাশিফল", "દૈનિક રાશી ભવિષ્ય", "ದಿನನಿತ್ಯ"},
            {"Weekly horoscope", "Saptahik Rashifal", "వార", "வாராந்திர", "साप्ताहिक राशी भविष्य", "ആഴ്ചതോറും", "साप्ताहिक राशिफल", "সাপ্তাহিক রাশিফল", "અઠવાડિક રાશી ભવિષ્ય", "ವಾರ"},
            {"Weekly Love", "Saptahik Prem", "వారం  ప్రేమ", "வாராந்திர காதல்", "साप्ताहिक प्रेम", "ആഴ്ച പ്രേമകാര്യം", "साप्ताहिक प्रेम", "সাপ্তাহিক প্রেম", "અઠવાડિક પ્રેમ", "ವಾರದ ಪ್ರೀತಿ , ಪ್ರೇಮ"},
            {"Monthly horoscope", "Masik Rashifal", "మాస", "மாதாந்திர", "मासिक राशी भविष्य", "മാസ രാശിഫലം", "मासिक राशिफल", "মাসিক রাশিফল", "માસિક રાશી ભવિષ્ય", "ಮಾಸಿಕ"},
            {"Yearly horoscope", "Varshik Rashifal", "వార్షికం", "வருடாந்திர", "वार्षिक राशी भविष्य", "വാർഷികം", "वार्षिक राशिफल", "বার্ষিক রাশিফল", "વાર્ષિક રાશી ભવિષ્ય", "ವರ್ಷ"},

            {"Brihat Horoscope", "বৃহৎ কুষ্ঠি", "બૃહત્ત કુંડળી", "बृहत् कुंडली", "ಬೃಹತ್ ಜಾತಕ", "ബൃഹത് ജാതകം", "बृहत कुंडली", "பரிஹாட் ஜோதிடம்", "బ్రిహట్ జాతకము", ""},
            {"Gemstones", "Ratna", "జాతి రత్నములు", "ராசி கற்கள்", "रत्न", "രത്‌നക്കല്ലുകൾ", "रत्न", "রত্ন পাথর", "રત્ન", "ರತ್ನಗಳು"},
            {"Yantras", "Yantra", "యంత్రములు", "யந்த்ரங்கள்", "यंत्र", "യന്ത്രങ്ങൾ", "यन्त्र", "যন্ত্র", "યંત્ર", "ಯಂತ್ರ"},
            {"Rudraksh", "", "రుద్రాక్షలు", "ருத்ராக்ஷம்", "रुद्राक्ष", "രുദ്രാക്ഷം", "रुद्राक्ष", "রুদ্রাক্ষ", "રૂદ્રાક્ષ", "ರುದ್ರಾಕ್ಷಿ"},
            {"Mala", "", "మాల", "மாலை", "माळ", "മാല", "माला", "মালা", "માલા", "ಮಾಲಾ"},
            {"Navagrah", "Navgrah", "గ్రహ యంత్రం", "நவக்ரஹா", "नवग्रह", "നവഗ്രഹ", "नवग्रह", "নবগ্রহ", "નવગ્રહ", "ನವಗ್ರಹ"},
            {"Jadi", "", "వన మూలికలు", "மூலிகை", "औषधि मुळे", "ഔഷധം", "जड़ी", "জড়ি", "જડી", "ಜಡಿ"},
            {"Services", "Paramarsh", "సేవలు", "சேவைகள்", "सेवा", "സേവനങ്ങൾ", "परामर्श", "সার্ভিস", "સેવાઓ", "ಸೇವೆಗಳು"},
            {"Astrologer", "Pandit Ji", "జ్యోతిష్కుడు", "ஜோதிடர்", "ज्योतिषी", "ജ്യോത്സ്യൻ", "पंडित जी", "জ্যোতিষী", "જ્યોતિષી", "ಜ್ಯೋತಿಷಿ"},
            {"Panchak", "পঞ্চক", "પંચક", "पंचक", "ಪಂಚಕ", "പഞ്ചാംഗം", "पंचक", "பஞ்சக்", "పంచాక్", ""},
            {"Bhadra", "ভদ্রা", "ભદ્રા", "भद्रा", "ಭದ್ರ", "ഭദ്ര", "भद्रा", "பத்ரா", "భద్ర", ""},
            {"Muhurat", "", "முஹுர்த்தம்", "ముహూర్తము షేర్ చేయండి", "मुहूर्त", "മുഹൂർത്തം", "મુહૂર્ત", "মুহূর্ত", "ಮುಹೂರ್ತ", ""},
            {"Numerology", "अंक शास्त्र", "సంఖ్యా శాస్త్రము", "अंकशास्त्र", "സംഖ്യാശാസ്ത്ര", "અંક શાસ્ત્ર", "নিউমেরলজি", "", "", ""},
            /*{"Bhrigoo.ai", "भृगु", "ভৃগু", "ભૃગુ", "ಭ್ರಿಗೂ", "ഭൃഗു", "भ्रिगू", "பிரிகோவ்", "భ్రిగూ", ""},*/
            {"Lagna Table", "লগ্ন তালিকা", "લગ્ન ટેબલ", "लग्न तक्ता", "ಲಘ್ನ ಕೋಷ್ಟಕ", "ലഗ്ന പട്ടിക", "लग्न तालिका", "லக்கின அட்டவளை", "", ""},
            {"Daily Notes", "தினசரி குறிப்புகள்", "দৈনিক নোট", "दैनिक नोट्स", "દૈનિક નોંધ", "दैनिक टीप", "రోజువారీ గమనికలు", "ദൈനംദിന കുറിപ്പുകൾ", "", ""},
            {"Feng Shui", "ফেং সুই", "ફેંગ શૂઈ", "फेंग शुई", "ಫೆಂಗ್ ಶೂಯಿ", "ഫെങ് ഷൂയി", "फेंग शुई", "பெங் ஷுய்", "ఫెంగ్ షుయి", ""},
            {"Misc", "অন্য", "વિવિધ", "अन्य", "ಅದೃಷ್ಟ", "പലവക", "किरकोळ", "மற்றவை", "ఇతరములు", ""}};
    ArrayList<String> suggetions;
    /**
     * @created by : Amit Rautela
     * @date : 23-2-2016
     * @Description : This method is used to set View Pager- page change
     */
    ViewPagerAdapter adapter;
    MenuItem itemshare;
    /**
     * @param intent
     * @author : Amit Rautela
     * This method is used for deep linking
     */

    String mTitle;
    String mDescription;
    Uri mUrl = null;
    ArrayList<String> CategoryFullName;
    ArrayList<String> yearlyTabKeys;
    ArrayList yearlyTabvalues;
    ActivityDetailWithExtraData activityDetailWithExtraData;
    CoordinatorLayout cordinatorLay;
    private final boolean isRateDialogDisplayed = false;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private ArrayList<AdData> adList;
    private List<CustomAddModel> customaddmodel;
    //Typeface typeface;
    private final String IsShowBanner = "False";
    private final String User = "USER";
    private final String Logout = "Logout";
    private final String Login = "Login";
    private final String UX = "UX";
    private final String PlayService = "PLAY_SERVICE";
    private RequestQueue queue;
    private MaterialSearchView searchView;
    private TextView notificationcountTV;
    private ProgressDialog installLiveProgressDialog;
    private LinearLayout wallet_layout;
    public static TextView wallet_price_txt;
    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private LinearLayout navView;
    //private FloatingActionButton appActFab;
    public static ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;
    private static final int GET_FIREBASE_AUTH_TOKEN = 443;
    private static final int NEXT_OFFER_API_RESPONSE = 4;
    //private String downloadingState = "Initial";
    private android.app.AlertDialog alert;
    AppUpdateManager appUpdateManager;
    InstallStateUpdatedListener updateListener;
    Task<AppUpdateInfo> appUpdateInfoTask;
    int UPDATE_REQUEST_CODE = 319;
    int UPDATE_DOWNLOAD_STATUS = 0;
    private String nextRechargeOfferType = "";
    public static boolean isKundliAIPlusPopUpShownOnce = false;
    /*SplitInstallStateUpdatedListener listener =
            new SplitInstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(SplitInstallSessionState state) {

                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            //downloadingState = downloadingState+" DOWNLOADING";
                            int totalBytes = (int) state.totalBytesToDownload();
                            int progress = (int) state.bytesDownloaded();
                            //downloadingState = downloadingState+" progress="+progress;
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            updateInstallLiveProgressBar(com.ojassoft.astrosage.varta.utils.CUtils.convertBytesToMB(totalBytes), com.ojassoft.astrosage.varta.utils.CUtils.convertBytesToMB(progress));
                            break;
                        case SplitInstallSessionStatus.INSTALLING:
                            //downloadingState = downloadingState+" INSTALLING";
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            break;
                        case SplitInstallSessionStatus.DOWNLOADED:
                            //downloadingState = downloadingState+" DOWNLOADED";
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            break;

                        case SplitInstallSessionStatus.INSTALLED:
                            //downloadingState = downloadingState+" INSTALLED";
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            hideInstallLiveProgressBar();
                            openLiveStramingScreen();
                            break;

                        case SplitInstallSessionStatus.CANCELED:
                            //downloadingState = downloadingState+" CANCELED";
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            hideInstallLiveProgressBar();
                            callSnakbar("Live content installation cancelled");
                            break;

                        case SplitInstallSessionStatus.PENDING:
                            //downloadingState = downloadingState+" PENDING";
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            break;

                        case SplitInstallSessionStatus.FAILED:
                            //downloadingState = downloadingState+" FAILED";
                            //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                            hideInstallLiveProgressBar();
                            callSnakbar("Live content installation failed. Error code: " + state.errorCode());
                            break;
                    }
                }
            };*/

    /*
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actapp_nav_home:
                            mViewPager.setCurrentItem(0,true);
                            return false;
                        case R.id.actapp_nav_call:
                            switchToConsultTab(FILTER_TYPE_CALL);
                            return false;
                        case R.id.actapp_nav_live:
                            fabActions();
                            return false;
                        case R.id.actapp_nav_chat:
                            switchToConsultTab(FILTER_TYPE_CHAT);
                            return false;
                        case R.id.actapp_nav_account:
                            isUserLogin(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PROFILE_SCRREN);
                            return false;
                    }
                    return false;
                }
            };
            */

    private void fabActions() {
        try {
            //boolean liveAstroEnabledForAstrosageHomeScreen = CUtils.getBooleanData(ActAppModule.this, CGlobalVariables.liveAstrologerEnabledForAstrosageHomeScreen, false);
            boolean liveAstroEnabledForAstrosageHomeScreen = true;
            if (!liveAstroEnabledForAstrosageHomeScreen) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_AK_BOTTOM_BAR_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.createSession(this, CGlobalVariables.AK_HOME_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(ActAppModule.this);
            } else {
                if (liveAstrologerModelArrayList != null && !liveAstrologerModelArrayList.isEmpty()) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_AK_BOTTOM_BAR_LIVE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(this, CGlobalVariables.AK_HOME_BOTTOM_BAR_LIVE_PARTNER_ID);
                    checkPermissions(liveAstrologerModelArrayList.get(0));
                } else {
                    startActivity(new Intent(ActAppModule.this, AllLiveAstrologerActivity.class));
                }
            }
        } catch (Exception e) {
            //
        }

    }

    /**
     * checkLoginStatusReceiver
     * Purpose: Handles user login/logout status changes
     * Functionality:
     * Receives logout status from intent
     * Triggers user sign-out functionality
     * Handles cloud logout process
     * Registration: Registered in onStart() with CGlobalVariables.CHECK_LOGIN_INTENT
     */
    private final BroadcastReceiver checkLoginStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null) {
                    boolean isLogout = intent.getBooleanExtra(CGlobalVariables.IS_LOGOUT, false);
                    if (isLogout) {
                        drawerFragment.usersignoutFuntionality();
                        logoutFromAstroSageCloud(false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * aiRandomChatAstroDeatils:
     * Purpose: Handles AI chat astrologer details
     * Functionality:
     * Receives AI chat-related broadcasts
     * Logs chat details for testing purposes
     * Registration: Registered in onStart() with CGlobalVariables.AI_ASTRO_RANDOM_CHAT_DETAILS
     */
    private final BroadcastReceiver aiRandomChatAstroDeatils = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TestChat", "aiRandomChatAstroDeatils onReceive");
//            try {
//                if(AstrosageKundliApplication.showAIFreePopup) {
//                    AstrosageKundliApplication.showAIFreePopup = false;
//                    com.ojassoft.astrosage.varta.utils.CUtils.showConsultPremiumAIFreeChatDialog(ActAppModule.this);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    };


    /**
     * checkFreeDhruvPlanAvailReceiver:
     * Purpose: Monitors availability of free Dhruv plan
     * Functionality:
     * Receives broadcasts about Dhruv plan availability
     * Can trigger plan subscription dialogs
     * Registration: Registered in onStart() with CGlobalVariables.CHECK_FREE_DHRUV_PLAN_AVAIL_INTENT
     */
    private final BroadcastReceiver checkFreeDhruvPlanAvailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //openPlanSubscriptionDialog();
            //openDialogOnHomeScreen();
        }
    };

    /**
     * mMessageReceiver:
     * Purpose: Handles notification count updates
     * Functionality:
     * Updates notification count in the UI
     * Receives notification-related broadcasts
     * Registration: Registered in onStart() with UPDATE_NOTIFICATION_COUNT
     */
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //update notification count
            updateNotificationCount();
        }
    };

    private boolean isNotification;
    private boolean activityVisible;

    public SuggestedQuestionHelperClass suggestedQuestionHelperClass;

    public ActAppModule() {
        super(R.string.astrosage_name);
    }

    @AddTrace(name = "onActAppModuleCreateTraceV1", enabled = true /* optional */)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.pull_in_from_top, R.anim.hold);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();// ADDED BY BIJENDRA ON 24-12-14
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.white));
//        }
        setContentView(R.layout.lay_home_new);
        searchedAstroList = new ArrayList<>();
        initView();
        initData();
        handleAdsLink();
        getFirebaseAccessToken();
        initUpdateManager();
        loadSuggestedQuestion();
        com.ojassoft.astrosage.varta.utils.CUtils.subscribeRegisteredFreeOrPaidTopic(this, 0);
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getUserIntroOfferType(this);

        if (isAutoConsultationConnected) {
            Log.d("darkModeIssue", "onCreate: 1");
        } else if (com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(this) && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
            Log.d("darkModeIssue", "onCreate: 2");
        } else {
            Log.d("darkModeIssue", "onCreate: 3");
            if (!CUtils.getBooleanData(ActAppModule.this, CGlobalVariables.IS_DARK_MODE_SELECTED, false)) {
                showDarkModePopUp();
            }
        }

//        String extras = "{\"astroid\":\"123456\",\"name\":\"AstroGPT\",\"imageUrl\":\"custom value\",\"question\":\"How will be my day today?\",\"r\":\"custom value\",\"aiastroonline\":true}";
//        String link = "https://varta.astrosage.com/chat-with-ai-astrologers";
//        CustomAINotification customAINotification = new CustomAINotification(this, extras, link);
//        customAINotification.loadNotification();

        //com.ojassoft.astrosage.varta.utils.CUtils.sendNotificationWithLink(ActAppModule.this, "10% discount For Toady"," Hurry up we are giving 10% discount on all product, Please grave it, this offer is valid for today","https://astrosage.shop/collections/rudraksha");


    }


    /**
     * Checks and manages the free call/chat offer status for users.
     * This method handles the following scenarios:
     * 1. Checks if a user has taken a free chat/call offer
     * 2. Manages the offer expiration based on user's wallet status
     * 3. Tracks user engagement after taking the free offer
     * <p>
     * Key Functionality:
     * - Verifies if user has taken a free offer by checking firstFreeChatDt
     * - If offer was taken:
     * - Checks user's wallet balance
     * - If wallet is empty (0.0 or null):
     * - Calculates days since offer was taken
     * - If 14+ days passed without transaction:
     * - Logs analytics event for non-transaction
     * - Clears the offer taken date
     * - If wallet has balance:
     * - Clears the offer taken date
     * - If no offer was taken:
     * - Checks for reduced price offer status
     * <p>
     * Analytics Events:
     * - FREE_OFFER_TAKEN_NOT_TRANSACTED: Triggered when user doesn't transact within 14 days
     */
    private void checkFreeCallChatOfferType() {
        // if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
        String firstFreeChatDt = com.ojassoft.astrosage.varta.utils.CUtils.getFirstFreeOfferTakenDate(this);
        if (!firstFreeChatDt.isEmpty()) {
            String wallet = com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(this);
            if ((wallet == null) || wallet.equals("0.0") || wallet.equals("0") || wallet.trim().length() == 0) {
                long days = dayBetween(firstFreeChatDt, DateTimeUtils.currentDate());
                // user not transacted/recharge 15 day(two week) after taken free chat/call offer.
                if (days >= 14) {
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(FREE_OFFER_TAKEN_NOT_TRANSACTED, "two_week", "");
                    com.ojassoft.astrosage.varta.utils.CUtils.saveFirstFreeOfferTakenDate(this, "");
                }
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.saveFirstFreeOfferTakenDate(this, "");
            }
        } else {
            checkReducePriceOfferTaken();
        }
        // }
    }

    private void checkReducePriceOfferTaken() {
        // if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
        String reducePriceDt = com.ojassoft.astrosage.varta.utils.CUtils.getReducePriceOfferTakenDate(this);
        if (!reducePriceDt.isEmpty()) {
            String wallet = com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(this);
            if ((wallet == null) || wallet.equals("0.0") || wallet.equals("0") || wallet.trim().length() == 0) {
                long days = dayBetween(reducePriceDt, DateTimeUtils.currentDate());
                // user not transacted/recharge 30 day(1 month) after taken user chat/call offer (10 Rs. min).
                if (days >= 30) {
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(USER_OFFER_TAKEN_NOT_TRANSACTED, "thirty_days", "");
                    com.ojassoft.astrosage.varta.utils.CUtils.saveReducePriceOfferTakenDate(this, "");
                }
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.saveReducePriceOfferTakenDate(this, "");
            }
        }
        // }
    }

    /**
     * Retrieves the Firebase access token for the current user.
     * <p>
     * This method checks if the Firebase token needs to be refreshed. If so, it fetches a new token from the server.
     * If the token does not need to be refreshed, it attempts to get the current user's ID token.
     * If the user is not logged in or if fetching the token fails, it fetches a new token from the server.
     * <p>
     * Key Functionality:
     * - Checks if Firebase token refresh is required using shared preferences.
     * - If refresh is needed, calls fetchFirebaseAuthTokenFromServer().
     * - If no refresh is needed, retrieves the current Firebase user.
     * - If user is null, calls fetchFirebaseAuthTokenFromServer().
     * - If user exists, attempts to get ID token with force refresh.
     * - On success, retrieves and stores the ID token.
     * - On failure, calls fetchFirebaseAuthTokenFromServer().
     */
    public void getFirebaseAccessToken() {

        boolean isFirebaseTokenRefresh = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, CGlobalVariables.IS_FIREBASE_TOKEN_REFRESH, true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // refresh firebase token forcefully after install or upgrade
        if (firebaseUser == null || isFirebaseTokenRefresh) {
            fetchFirebaseAuthTokenFromServer();
            com.ojassoft.astrosage.varta.utils.CUtils.saveBooleanData(this, CGlobalVariables.IS_FIREBASE_TOKEN_REFRESH, false);
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

        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            if (TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginPassword(this))) {
                return;
            }
            com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAuthToken(this, GET_FIREBASE_AUTH_TOKEN);
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
                                Log.e("TestFirebaseToken", mAuth.getCurrentUser().getUid());
                            }
                        }
                    });
        } catch (Exception e) {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() excep " + e.toString() );
        }
    }

    private void notTransacted() {
        try {
            long installed = getPackageManager().getPackageInfo(getPackageName(), 0).firstInstallTime;
            Date appInstallDt = new Date(installed);
            String installDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(appInstallDt);
            long days = dayBetween(installDate, DateTimeUtils.currentDate());
            // user not transacted/recharge 26 day after installation app.
            if (days >= 26) {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(NOT_TRANSACTED, "last_twenty_six_days", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long dayBetween(String freeCallChatDate, String currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(freeCallChatDate);
            Date2 = sdf.parse(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
    }

    private void initView() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        cordinatorLay = findViewById(R.id.cordinatorLay);
        tvTitle = findViewById(R.id.tvTitle);
        navView = findViewById(R.id.nav_view);
        wallet_layout = findViewById(R.id.wallet_layout);
        wallet_price_txt = findViewById(R.id.wallet_price_txt);
        //appActFab = findViewById(R.id.fabHome);
        //tvTitle.setTypeface(mediumTypeface);
        ivToggleImage = findViewById(R.id.ivToggleImage);
        ivToggleImage.setVisibility(View.VISIBLE);
        FontUtils.changeFont(this, tvTitle, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, findViewById(R.id.drawerLayout), tool_barAppModule, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mViewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        try {
            searchView = findViewById(R.id.search_view);
            searchView.setVoiceSearch(false);
            searchView.showVoice(false);
            searchView.setEllipsize(true);
            searchView.setHintTextColor(getColor(R.color.colorPrimary_day_night));
        } catch (Exception e) {
            //
        }

        //navView.setOnClickListener(navbarItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
    }

    private void initData() {
        queue = VolleySingleton.getInstance(ActAppModule.this).getRequestQueue();
        String virtualKundaliUrl = CUtils.getStringData(this, CGlobalVariables.KEY_KUNDALI_VIRTUAL_URL, "");
        if (!TextUtils.isEmpty(virtualKundaliUrl)) {
            AstrosageKundliApplication.isOpenVartaPopup = true; //don't show popup while opening kundli
        }
        //manager = SplitInstallManagerFactory.create(this);
        initSearchView();
        setupViewPager();
        initWalletClickListner();
        onIntent(getIntent());
        initChatInfoLayout();
        // Notify user to update plan For Update Plan
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // notifyUserTorenewthePlan("21-jul-2016");
                checkNeedToNotifyTheUserToUpdatePlan();
                getFirebaseAnalyticsInstanceId(ActAppModule.this);
                //CUtils.syncChartWithCloud(ActAppModule.this, true);
                /*
                 * uncomment below code to detect
                 * first time app install
                 * */

                /*boolean isUserFirstTimeInstallTheApp = CUtils.getBooleanData(ActAppModule.this, CGlobalVariables.GET_FREE_QUESTION_AT_USER_FIRST_INSTALL_KEY, true);
                if (CUtils.isConnectedWithInternet(ActAppModule.this) && isUserFirstTimeInstallTheApp) {
                    if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {
                        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserIntroOfferType(ActAppModule.this) != null && com.ojassoft.astrosage.varta.utils.CUtils.getUserIntroOfferType(ActAppModule.this).equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                            getFreeQuestionIfUserFirstTimeInstallTheApp();
                        }
                    } else {
                        getFreeQuestionIfUserFirstTimeInstallTheApp();
                    }

                }*/
            }
        }, 6000);//6 secons delay

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendAnalyticsVartaUserIsLogin();
                sendAnalyticsForUserLoginOrNot();
                startSubsAndUnsubsTopicsService();
                checkFreeCallChatOfferType();
                // Add for get InAppbillling products detail
                fetchProductFromGoogleServer();
                if (CUtils.isConnectedWithInternet(ActAppModule.this)) {
                    stratPlanPurchaseService();
                    //to check if user is aipass subscriber
                    com.ojassoft.astrosage.varta.utils.CUtils.startToGetAiPassSubService(ActAppModule.this);

                }
                openKundaliByVirtualUrl();

                if (CUtils.isConnectedWithInternet(ActAppModule.this)) {
                    getImageAdData();
                } else {
                    CUtils.saveStringData(ActAppModule.this, "CUSTOMADDS", "");
                }
                astrosageAndVartaLoginAction();

                BeanUserMapping beanUserMapping = CUtils.getUserMappingData(ActAppModule.this);
                if (CUtils.isConnectedWithInternet(ActAppModule.this)) {
                    if (beanUserMapping != null && beanUserMapping.getStatus() == 1) {
                        try {
                            Intent sendUserInfoIntent = new Intent(ActAppModule.this, SaveAstroLogerInfoService.class);
                            startService(sendUserInfoIntent);
                        } catch (Exception e) {
                            //
                        }
                    }
                }
                if (beanUserMapping == null) {
                    try {
                        Intent intent = new Intent(ActAppModule.this, GetCityInBackground.class);
                        startService(intent);
                    } catch (Exception e) {
                        //
                    }
                }

                BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils.getCustomObject(ActAppModule.this);
                setAnalyticsByAgeOfDefaultKundli(beanHoroPersonalInfo);
                String jsonString = CUtils.getStringData(ActAppModule.this, "defaultKundliData", "");
                //if default kundli is selected and dasha data is not available
                if (beanHoroPersonalInfo != null && jsonString.equals("")) {
                    //this service get dasha data for default kundli
                    try {
                        Intent intent = new Intent(ActAppModule.this, GetDefaultKundliDataService.class);
                        intent.putExtra("beanHoroPersonalInfo", beanHoroPersonalInfo);
                        startService(intent);
                    } catch (Exception e) {
                        //
                    }
                }
                arrayList = CUtils.getArrayListStringForHoroscope(ActAppModule.this, CGlobalVariables.SAVEHOROSCOPECOUNT);
                if (arrayList != null && arrayList.size() > 0) {
                    CUtils.sendStaticHoroscopeCountToAnalytics(ActAppModule.this, arrayList);
                }
                startPrefetchDataService();
            }
        }, 1000);//1 secons delay
        /*if(!CUtils.getBooleanData(this,CGlobalVariables.IS_DARK_MODE_SELECTED,false)){
            showDarkModePopUp();
        }*/
    }

    private void startPrefetchDataService() {
        try {
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this);
            String dataIsAvaibale = com.ojassoft.astrosage.varta.utils.CUtils.getWalletRechargeData();
            if (isLogin && TextUtils.isEmpty(dataIsAvaibale)&& !AstrosageKundliApplication.isPrefetchServiceRunning) {
                Intent intentService = new Intent(ActAppModule.this, PreFetchDataservice.class);
                startService(intentService);
            }

        } catch (Exception e) {
        }
    }
    private void loadAstroShopData() {
        String entry = CUtils.getStringData(ActAppModule.this, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
        Log.e("dataCheck", "data check : entry " + entry);
        if (TextUtils.isEmpty(entry)) {//need to load astro shop data here to let products open from home
            Log.e("dataCheck", "data loading : entry is null called service");
            CUtils.startAstroShopeDataDownloadService(ActAppModule.this);
        }
    }

    private void initChatInfoLayout() {
        //Log.e("SAN ", "initChatInfoLayout() ");
        chatInitiateInfoLayout = findViewById(R.id.chat_initiate_info_layout);
        ongoingChatInfoLayout = findViewById(R.id.join_ongoing_chat_layout);
        ongoingAICallLayout = findViewById(R.id.call_initiate_info_layout);
        chatInitiateInfoLayout.setVisibility(View.GONE);
        cross_chat_initiate_view = chatInitiateInfoLayout.findViewById(R.id.cross_chat_initiate_view);
        cross_chat_initiate_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("SAN ", "initChatInfoLayout() X click ");
                cross_chat_initiate_view.setEnabled(false);
                openChatRequestCancel();
            }
        });
    }

    private void openChatRequestCancel() {

        //Log.e("SAN ", "openChatRequestCancel() X click ");

        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_button_click", AstrosageKundliApplication.currentEventType, "");
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.end_chat_confirm_dialog, null);
        builder.setView(dialogView);

        TextView end_chat_confirm_text = dialogView.findViewById(R.id.end_chat_confirm_text);
        FontUtils.changeFont(ActAppModule.this, end_chat_confirm_text, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
        TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
        alert = builder.create();
        Objects.requireNonNull(alert.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        end_chat_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_btn_yes_click", AstrosageKundliApplication.currentEventType, "");
                alert.dismiss();
                //Log.e("SAN ", "openChatRequestCancel() X click yes ");
                clickCrossChatRequest();
            }
        });

        end_chat_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("SAN ", "openChatRequestCancel() X click no ");
                cross_chat_initiate_view.setEnabled(true);
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_btn_no_click", com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                alert.dismiss();
            }
        });
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_dialog_show", AstrosageKundliApplication.currentEventType, "");
        alert.setCanceledOnTouchOutside(false);
        alert.show();

    }

    private void clickCrossChatRequest() {

        if (com.ojassoft.astrosage.varta.utils.CUtils.getSelectedAstrologerID(getApplicationContext()) != null && com.ojassoft.astrosage.varta.utils.CUtils.getSelectedAstrologerID(getApplicationContext()).length() > 0 &&
                getSelectedChannelID(getApplicationContext()) != null && getSelectedChannelID(getApplicationContext()).length() > 0) {
            AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
            com.ojassoft.astrosage.varta.utils.CUtils.changeFirebaseKeyStatus(getSelectedChannelID(getApplicationContext()), com.ojassoft.astrosage.varta.utils.CGlobalVariables.CANCELLED, true, com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_CANCELLED);
            chatCompleted(com.ojassoft.astrosage.varta.utils.CGlobalVariables.END_CHAT_URL, getSelectedChannelID(getApplicationContext()), com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_CANCELLED, com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_BUSY, com.ojassoft.astrosage.varta.utils.CUtils.getSelectedAstrologerID(getApplicationContext()));
        }
        if (com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
            if (com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean != null && AstrosageKundliApplication.selectedAstrologerDetailBean != null)
                com.ojassoft.astrosage.varta.utils.CUtils.changeFirebaseKeyStatus(com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean.getCallsid(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.CANCELLED, true, com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_CANCELLED);
            chatCompleted(com.ojassoft.astrosage.varta.utils.CGlobalVariables.END_CALL_URL, com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean.getCallsid(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_CANCELLED, com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_BUSY, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
        }

    }

    private void initWalletClickListner() {
        wallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTROSAGE_HOME_WALLET_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.createSession(ActAppModule.this, CGlobalVariables.ASTROSAGE_HOME_WALLET_PARTNER_ID);
                boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this);
                if (!isLogin) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTROSAGE_HOME_WALLET_LOGIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    Intent intent = new Intent(ActAppModule.this, LoginSignUpActivity.class);
                    intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RECHARGE_SCRREN);
                    startActivity(intent);
                } else {
                    openWalletScreen("act_app_module");
                }
            }
        });
    }

    private void initSearchView() {
        suggetions = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Background thread
                    for (int i = 0; i < keys.length; i++) {
                        for (int j = 0; j < keys[i].length; j++) {
                            if (!TextUtils.isEmpty(keys[i][j])) {
                                suggetions.add(keys[i][j]);
                            }
                        }
                    }

                    //UI process
                    handler.post(() -> {
                        // UI thread
                        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                if (query == null) return false;
                                boolean isResultFound = false;
                                if (suggetions != null) {
                                    for (int i = 0; i < suggetions.size(); i++) {
                                        String str = suggetions.get(i);
                                        if (str == null) continue;
                                        if (str.toLowerCase().contains(query.toLowerCase()) || query.toLowerCase().contains(str.toLowerCase())) {
                                            CUtils.fcmAnalyticsEvents(CGlobalVariables.SEARCH_ITEM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                            isResultFound = true;
                                            break;
                                        }
                                    }
                                }

                                MyCustomToast mct = new MyCustomToast(ActAppModule.this, getLayoutInflater(), ActAppModule.this, regularTypeface);
                                if (isResultFound) {
                                    mct.show(getString(R.string.text_select));
                                } else {
                                    mct.show(getResources().getString(R.string.search_not_available));
                                }
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                //Do some magic
                                //ArrayList<AstroShopItemDetails> fetchedData = getitems(newText);
                                if (newText.length() > 3) {
                                    if (!searchedAstroList.isEmpty()) {
                                        getSearchResultFromApi(newText);
                                    }
                                } else if (newText.length() == 3) {
                                    getSearchResultFromApi(newText);
                                }
                                return false;
                            }
                        });

                        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                            @Override
                            public void onSearchViewShown() {
                                isSearchBarOpen = true;
                                //Do some magic
                                searchView.setTintVisibility(true);
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.SEARCH_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                searchView.setSuggestions(ActAppModule.this, suggetions);
                            }

                            @Override
                            public void onSearchViewClosed() {
                                //Do some magic
                                isSearchBarOpen = false;
                                searchView.setTintVisibility(false);

                            }
                        });
                        searchView.closeSearch();
                    });
                } catch (Exception e) {
                    //
                }
            }
        });
    }


    /**
     * This method check varta login and open login screen weekly if not loggedin in varta section.
     * If logged-in in varta section but not logged-in in astrosage then hit login api forcefully to login in astrosage.
     */
    private void astrosageAndVartaLoginAction() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {//check varta login
                if (!CUtils.isUserLogedIn(this)) { //if not logged-in in astrosage
                    astrosageLoginWithVartaId();
                }
            } else { // not logged-in in varta
                if (!AstrosageKundliApplication.isUserFirstTimeInstallTheApp) {
                    String dialogDate = CUtils.getOpenAutoLoginDateData(ActAppModule.this);
                    //Log.e("PlanSubscription", "dialogDate="+dialogDate);
                    if (!TextUtils.isEmpty(dialogDate)) {
                        //Log.e("PlanSubscription", "checkDatesSame before");
                        if (CUtils.checkDatesSame(dialogDate)) {
                            //Log.e("PlanSubscription", "checkDatesSame after");
                            openLoginScreen();
                        }
                    } else {
                        openLoginScreen();
                    }
                }
            }
        } catch (Exception e) {
            //
        }
    }


    private void astrosageLoginWithVartaId() {

        String mobileNumber = com.ojassoft.astrosage.varta.utils.CUtils.getUserID(ActAppModule.this);
        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActAppModule.this);
        if (TextUtils.isEmpty(mobileNumber) || TextUtils.isEmpty(countryCode)) {
            return;
        }
        if (CUtils.isConnectedWithInternet(ActAppModule.this)) {
            String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.AstrosageRegisterWithVarta;
            //Log.d("LoginFlow", " varta: url" + url);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    ActAppModule.this, false, getParamsAstrosageLogin(countryCode, mobileNumber), 3).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);
        }
    }

    public Map<String, String> getParamsAstrosageLogin(String countryCode, String mobileNumber) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, mobileNumber);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, countryCode);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.REG_SOURCE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_SOURCE);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);

        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_LOGIN);

        String _pwd = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginPassword(this);
        int kundliCount = com.ojassoft.astrosage.utils.CUtils.getKundliCount(this);
        String strFirstLoginAfterPlanPurchase = "";
        if (com.ojassoft.astrosage.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false)) {
            strFirstLoginAfterPlanPurchase = com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_LOGIN_AFTER_PLAN_PURCHASED;
        }
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROSAGE_USERID, "");
        headers.put("firstloginafterplanpurchase", strFirstLoginAfterPlanPurchase);
        headers.put("isverified", "1");
        headers.put("nocharts", kundliCount + "");
        headers.put("pw", _pwd);
        //Log.d("LoginFlow", " varta: " + headers);
        return headers;
    }

    private void openLoginScreen() {
        isShowLoginScreen = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AUTO_OPEN_LOGIN_SCREEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    Intent intent = new Intent(ActAppModule.this, LoginSignUpActivity.class);
                    intent.putExtra(IS_FROM_SCREEN, LANGUAGE_SELECTION_SCRREN);
                    intent.putExtra(AUTO_OPEN_LOGIN, true);
                    intent.putExtra(IS_GOOGLE_FACEBOOK_VISIBLE, true);
                    startActivity(intent);

                    String datee = CUtils.getDialogDate(CGlobalVariables.ONE_DAY);
                    CUtils.saveOpenAutoLoginDateData(ActAppModule.this, datee);
                    //   new UnlockBrihatHelperClass(ActAppModule.this).handleFirstTimeInstall();
                } catch (Exception e) {
                    //
                }
            }
        }, 3000);//3 secons delay
    }

    private void setAnalyticsByAgeOfDefaultKundli(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        try {
            //Log.d("TestLog", "beanHoroPersonalInfo="+beanHoroPersonalInfo);
            if (beanHoroPersonalInfo == null) return;
            BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            int year = beanDateTime.getYear();
            int month = beanDateTime.getMonth();
            int day = beanDateTime.getDay();
            com.ojassoft.astrosage.varta.utils.CUtils.setFcmAnalyticsByAge(year, month, day);
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
                FontUtils.changeFont(ActAppModule.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                v.setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    /*private void setBottomNavigationText() {
        try {
            // get menu from navigationView
            Menu menu = navView.getMenu();
            // find MenuItem you want to change
            MenuItem navHome = menu.getItem(0);
            MenuItem navCall = menu.getItem(1);
            MenuItem navLive = menu.getItem(2);
            MenuItem navChat = menu.getItem(3);
            MenuItem navMyaccount = menu.getItem(4);

            // set new title to the MenuItem
            navHome.setTitle(getResources().getString(R.string.title_home));
            navCall.setTitle(getResources().getString(R.string.call));
            navChat.setTitle(getResources().getString(R.string.chat_now));
            navLive.setTitle(getResources().getString(R.string.live));
            // set new title to the MenuItem
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {
                navMyaccount.setTitle(getResources().getString(R.string.history));
                wallet_layout.setBackground(getResources().getDrawable(R.drawable.bg_border_black));
                wallet_price_txt.setVisibility(View.VISIBLE);
                wallet_price_txt.setText(getResources().getString(R.string.astroshop_rupees_sign) + com.ojassoft.astrosage.varta.utils.CUtils.convertAmtIntoIndianFormat(com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(ActAppModule.this)));
            } else {
                navMyaccount.setTitle(getResources().getString(R.string.sign_up));
                wallet_layout.setBackground(null);
                wallet_price_txt.setVisibility(View.GONE);
            }
        }catch (Exception e){
            //
        }
    }*/

    private void setBottomNavigationText() {
        try {

            // find MenuItem you want to change
            ImageView navHomeImg = navView.findViewById(R.id.imgViewHome);
            TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
            ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
            TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
            TextView navLiveTxt = navView.findViewById(R.id.txtViewLive);
            ImageView navChatImg = navView.findViewById(R.id.imgViewChat);
            TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
            ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
            TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);


            // set new title to the MenuItem
            navHomeTxt.setText(getResources().getString(R.string.title_home));

            boolean isAIChatDisplayed = CUtils.isAIChatDisplayed(this);
            if (isAIChatDisplayed) {
                navChatTxt.setText(getResources().getString(R.string.text_ask));
                navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
                Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
            } else {
                navChatTxt.setText(getResources().getString(R.string.chat_now));
                navCallTxt.setText(getResources().getString(R.string.call));
                navCallImg.setImageResource(R.drawable.nav_call_icons);
            }

            navLiveTxt.setText(getResources().getString(R.string.live));
            // set new title to the MenuItem
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {
                navHisTxt.setText(getResources().getString(R.string.history));
                navHisImg.setImageResource(R.drawable.nav_more_icons);
                wallet_layout.setBackground(getResources().getDrawable(R.drawable.bg_border_black));
                wallet_price_txt.setVisibility(View.VISIBLE);
                wallet_price_txt.setText(getResources().getString(R.string.astroshop_rupees_sign) + com.ojassoft.astrosage.varta.utils.CUtils.convertAmtIntoIndianFormat(com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(ActAppModule.this)));
            } else {
                navHisTxt.setText(getResources().getString(R.string.sign_up));
                navHisImg.setImageResource(R.drawable.nav_profile_icons);
                wallet_layout.setBackground(null);
                wallet_price_txt.setVisibility(View.GONE);
            }
            navHomeImg.setImageResource(R.drawable.ic_home_new_filled);
            //setting Click listener
            navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
            navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
            navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
            navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
            navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
        } catch (Exception e) {
            //
        }
    }


    private void startTagManagerService() {
        try {
            startService(new Intent(ActAppModule.this, GetTagManagerDataService.class));
        } catch (Exception e) {
            //
        }
    }

    private void stratPlanPurchaseService() {
        try {
            Intent intent = new Intent(ActAppModule.this, ServiceToGetPurchasePlanDetails.class);
            startService(intent);
        } catch (Exception e) {
            //
        }
    }


    private void startSubsAndUnsubsTopicsService() {
        try {
            Intent pvsIntent = new Intent(ActAppModule.this, SubsAndUnsubsTopics.class);
            startService(pvsIntent);
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int count = CUtils.getIntData(this, KEY_NOTIFICATION_COUNT, 0);
        if (count > 0)
            CUtils.saveIntData(this, KEY_NOTIFICATION_COUNT, --count);

        overridePendingTransition(R.anim.pull_in_from_top, R.anim.hold);

        CUtils.createSession(ActAppModule.this, "Astrosage");
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        //tvTitle.setTypeface(mediumTypeface);
        CUtils.saveStringData(ActAppModule.this, "CUSTOMADDS", "");

        if (CUtils.isConnectedWithInternet(ActAppModule.this)) {
            getImageAdData();
        }
        setupViewPager();

        //For Deep linking
        onIntent(intent);
        //handleVartaWalletRecharge(intent);
    }

    private void setupViewPager() {

        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            titles[0] = getResources().getString(R.string.home_tag);//.toUpperCase();
            titles[1] = getResources().getString(R.string.home_astro_shop);//.toUpperCase();
            titles[2] = getResources().getString(R.string.consult);//.toUpperCase();
            titles[3] = getResources().getString(R.string.report_tag);//.toUpperCase();
            titles[4] = getResources().getString(R.string.t_video);//.toUpperCase();
            titles[5] = getResources().getString(R.string.panchang_tag);//.toUpperCase();
            titles[6] = getResources().getString(R.string.horoscopeText);//.toUpperCase();
            titles[7] = getResources().getString(R.string.t_2019);
        } else {
            titles[0] = getResources().getString(R.string.home_tag);
            titles[1] = getResources().getString(R.string.home_astro_shop);
            titles[2] = getResources().getString(R.string.consult);
            titles[3] = getResources().getString(R.string.report_tag);
            titles[4] = getResources().getString(R.string.t_video);
            titles[5] = getResources().getString(R.string.panchang_tag);
            titles[6] = getResources().getString(R.string.horoscopeText);
        }

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActAppModule.this);

        videoFrag = new Video_Frag();
        vartaTabFragment = new HomeFragment1();
        //vartaTabFragment = new HomeFragment1();

        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            adapter.addFragment(new KundliModules_Frag(), titles[0]);
            adapter.addFragment(new Frag_Year(), titles[7]);
            adapter.addFragment(new Astroshop_Frag(), titles[1]);
            adapter.addFragment(vartaTabFragment, titles[2]);
            adapter.addFragment(new Reports_frag(), titles[3]);
            adapter.addFragment(videoFrag, titles[4]);
            adapter.addFragment(new Panchang_Frag(), titles[5]);
            adapter.addFragment(new Horoscope_Frag(), titles[6]);
        } else {
            adapter.addFragment(new KundliModules_Frag(), titles[0]);
            adapter.addFragment(new Astroshop_Frag(), titles[1]);
            adapter.addFragment(vartaTabFragment, titles[2]);
            adapter.addFragment(new Reports_frag(), titles[3]);
            adapter.addFragment(videoFrag, titles[4]);
            adapter.addFragment(new Panchang_Frag(), titles[5]);
            adapter.addFragment(new Horoscope_Frag(), titles[6]);

        }


        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setPageTitles(position);
                CUtils.hideMyKeyboard(ActAppModule.this);
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    String result = CUtils.getStringData(ActAppModule.this, "CUSTOMADDS", "");
                    if (result != null && !result.isEmpty()) {

                        //CUtils.saveStringData(ActAppModule.this, "CUSTOMADDS", result);
                        ArrayList<AdData> sliderList = parseData(result);
                        if (sliderList != null && sliderList.size() > 0) {
                            setSliderdata(sliderList, position);

                        }
                    } /*else {
                        CUtils.saveStringData(ActAppModule.this, "CUSTOMADDS", "");

                    }*/
                }

                if (tabLayout != null && adapter != null) {
                    adapter.setAlpha(position, tabLayout);
                }



               /*invalidateOptionsMenu();
               if (position == 1) {
                    walletBoxLayout.setVisibility(View.VISIBLE);

                } else {

                    walletBoxLayout.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setsTabLayout();

    }

    private void setsTabLayout() {
        try {
            tabLayout.setupWithViewPager(mViewPager);

            // Iterate over all tabs and set the custom view
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }

            adapter.setAlpha(mViewPager.getCurrentItem(), tabLayout);
        } catch (Exception e) {
            //
        }
    }

    /**
     * @created by : Amit Rautela
     * @date : 24-2-2016
     * @Description : This method is used to set the page titles
     */
    private void setPageTitles(int pos) {

        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            switch (pos) {
                case 0:
                    tvTitle.setText(getResources().getString(R.string.astrosage_name));
                    //tvTitle.setText(titles[0]);
                    break;
                case 1:
                    tvTitle.setText(titles[7]);
                    break;
                case 2:
                    tvTitle.setText(titles[1]);
                    break;
                case 3:
                    tvTitle.setText(titles[2]);
                    break;
                case 4:
                    tvTitle.setText(titles[3]);
                    break;
                case 5:
                    tvTitle.setText(titles[4]);
                    break;
                case 6:
                    tvTitle.setText(titles[5]);
                    break;
                case 7:
                    tvTitle.setText(titles[6]);
                    break;
                default:
                    break;
            }
        } else {
            switch (pos) {
                case 0:
                    tvTitle.setText(getResources().getString(R.string.astrosage_name));
                    //tvTitle.setText(titles[0]);
                    break;
                case 1:
                    tvTitle.setText(titles[1]);
                    break;
                case 2:
                    tvTitle.setText(titles[2]);
                    break;
                case 3:
                    tvTitle.setText(titles[3]);
                    break;
                case 4:
                    tvTitle.setText(titles[4]);
                    break;
                case 5:
                    tvTitle.setText(titles[5]);
                    break;
                case 6:
                    tvTitle.setText(titles[6]);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @created by : Amit Rautela
     * @date : 29-dec-2015
     * @Description : This method is used to notify the user to update the plan
     */
    private void checkNeedToNotifyTheUserToUpdatePlan() {

        int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(this);
        // check, Is user have silver or gold plan
        if (purchasePlanId != CGlobalVariables.BASIC_PLAN_ID) {
            String planExpiryDate = CUtils.getStringData(this,
                    CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, "0");
            if (planExpiryDate != null && !planExpiryDate.equals("0")) {

                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",
                        Locale.US);
                Date currentDate = new Date();
                // String currentDate = dateFormat.format(date);
                try {
                    Date expiryDate = dateFormat.parse(planExpiryDate);
                    Date currentDateWithoutTime = dateFormat.parse(dateFormat
                            .format(currentDate));

                    int diffInDays = CUtils.returnRemainingDaysFromTwoDates(
                            currentDateWithoutTime, expiryDate);
                    if (diffInDays < 0) {

                        DateFormat dateFormat2 = new SimpleDateFormat(
                                "EEE, MMM d, yyyy", Locale.US);
                        final String dateToShow = dateFormat2.format(expiryDate);

                        if (!CUtils
                                .getBooleanData(
                                        ActAppModule.this,
                                        CGlobalVariables.doNotShowMessageAgainForUpdatePlan,
                                        false)) {
                            notifyUserTorenewthePlan(dateToShow);
                        }
                    } else {
                        CUtils.saveBooleanData(
                                ActAppModule.this,
                                CGlobalVariables.doNotShowMessageAgainForUpdatePlan,
                                false);
                    }

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * @param expiryDate
     * @created by : Amit Rautela
     * @date 29 dec 2015
     * @description Notify the user to update the plan
     */
    private void notifyUserTorenewthePlan(final String expiryDate) {

        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("Renew_Plan_Dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            RenewPlanDialog renewPlanDialog = RenewPlanDialog.getInstance(expiryDate);
            renewPlanDialog.show(fm, "Renew_Plan_Dialog");
            ft.commit();
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem item = menu.findItem(R.id.action_search);
            MenuItem itemMore = menu.findItem(R.id.action_more);
            MenuItem itemshare = menu.findItem(R.id.action_share);
            //itemshare.setVisible(mViewPager.getCurrentItem() != 1);

            itemMore.setVisible(false);
            searchView.setVisibility(View.VISIBLE);

            searchView.setMenuItem(item);

            //ADDED BY ABHISEK FOR NOTIFICATION HANDLING
            MenuItem itemNotification = menu.findItem(R.id.action_notification);
            itemNotification.setVisible(true);
            if (itemNotification.getActionView() != null) {
                notificationcountTV = itemNotification.getActionView().findViewById(R.id.notificationcount);
                updateNotificationCount();
                new MyMenuItemStuffListener(itemNotification.getActionView()) {
                    @Override
                    public void onClick(View v) {
                        CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_OPEN_NOTIFICATION, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_OPEN_NOTIFICATION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        ActNotificationCenter.setNotificationCenterCallback(ActAppModule.this);
                        Intent intent = new Intent(ActAppModule.this, ActNotificationCenter.class);
                        startActivity(intent);
                    }
                };
            }

        } else {

        }

        return true;
    }

    public void updateNotificationCount() {
        final int count = CUtils.getIntData(ActAppModule.this, KEY_NOTIFICATION_COUNT, 0);

        if (notificationcountTV == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    notificationcountTV.setVisibility(View.INVISIBLE);
                else {
                    notificationcountTV.setVisibility(View.VISIBLE);
                    notificationcountTV.setText(Integer.toString(count));
                }
            }
        });
    }

    @Override
    public void redirectToLink(Intent intent) {
        sessionedPartnerId = CGlobalVariables.NOTIFICATION_PARTNER_ID;
        actionOnIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {//CUtils.shareToFriendMail(this);
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this);
            if (!isLogin) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTROSAGE_HOME_WALLET_LOGIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(ActAppModule.this, LoginSignUpActivity.class);
                intent.putExtra(IS_FROM_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RECHARGE_SCRREN);
                startActivity(intent);
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTROSAGE_HOME_WALLET_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openWalletScreen("act_app_module");
            }
            return true;
        } else if (itemId == R.id.action_notification) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                //showSecondaryMenu();
                return true;
            // break;
            case KeyEvent.KEYCODE_BACK:
                try {
                    if (drawerFragment.isDrawerOpen) {
                        drawerFragment.closeDrawer();
                    } else {
                        showExitScreen();
                    }
                } catch (Exception ex) {
                    showExitScreen();
                }


                return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitScreen() {
        if (mViewPager.getCurrentItem() != 0) {
            gotoHomeScreen(0);
        } else {
            //If user has a plan then no need to show Exit Screen
            int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(ActAppModule.this);
            if (CUtils.isUserLogedIn(ActAppModule.this)
                    && purchasePlanId != CGlobalVariables.BASIC_PLAN_ID) {
                isExitApp = true;
                ActAppModule.this.finish();
            } else {
                redirectToExitScreen();
            }
        }

    }

    private void redirectToExitScreen() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            boolean isUserComment = CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_AD_EXIT_SCREEN, false);
            boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(this);
            if (isUserComment && enabledAIFreeChatPopup) {

                String firstFreeChatType = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE, "");
                String secondFreeChatType = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");
                String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(this);

                //Log.e("TestChat", "enabledAIFreeChatPopup=" + enabledAIFreeChatPopup);
                //Log.e("TestChat", "isChatFree=" + isFreeChat);
                // Log.e("TestChat", "offerType=" + offerType);
                //  Log.e("TestChat", "isSecondFreeChat=" + com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(this));

                Intent intent = new Intent(this, FreeChatNowActivity.class);
                if (com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(this) || offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    startActivity(intent);
                } else {
                    ActAppModule.this.finish();
                }

            } else {
                startActivityForResult(new Intent(this, AppExitActivity.class), SUB_ACTIVITY_EXIT_PLAN);
            }
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(UPDATE_NOTIFICATION_COUNT));
        LocalBroadcastManager.getInstance(this).registerReceiver(checkFreeDhruvPlanAvailReceiver, new IntentFilter(CGlobalVariables.CHECK_FREE_DHRUV_PLAN_AVAIL_INTENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(checkLoginStatusReceiver, new IntentFilter(CGlobalVariables.CHECK_LOGIN_INTENT));
        LocalBroadcastManager.getInstance(this).registerReceiver((aiRandomChatAstroDeatils),
                new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_ASTRO_RANDOM_CHAT_DETAILS)
        );
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
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
        if (mMessageReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (checkFreeDhruvPlanAvailReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(checkFreeDhruvPlanAvailReceiver);
        }
        if (checkLoginStatusReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(checkLoginStatusReceiver);
        }
        if (aiRandomChatAstroDeatils != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(aiRandomChatAstroDeatils);
        }
        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiverBackgroundLoginService);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            activityVisible = true;
            actionOnResume();
            showOrHideCallChatInitiate();
            showOrHideOngoingChat();
            showHideAICallOngoing();
            appUpdateManager.getAppUpdateInfo()
                    .addOnSuccessListener(appUpdateInfo -> {
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            popupSnackbarForCompleteUpdate();
                        }
                    });
//            if(com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(AIVoiceCallingService.class)
//                    && !AIVoiceCallingActivity.isActivityInPIPMode){
//                startActivity(new Intent(this,AIVoiceCallingActivity.class));
//            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    private void actionOnResume() {
        isActAppModuleResumed = true;
        setBottomNavigationText();
        /*if (manager != null) {
            manager.registerListener(listener);
        }*/


        loadAstroShopData();//load astroshop data to let products open from home
        try {
            CUtils.createSession(ActAppModule.this, "Astrosage");
            if (searchView != null) {
                searchView.setHint(getString(R.string.search_hint));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateNotificationCount();
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            CUtils.hideMyKeyboard(ActAppModule.this);
        } catch (Exception e) {

        }
        try {
            drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
        }

        if (!isFromConsultTab) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            setsTabLayout();
        } else {
            isFromConsultTab = false;
        }


        removeAINotificationPeriodicTask(this); //remove this line and uncomment below code to enable personalized notifications

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    startPrefatchAstroDataService();
//                    startPrefatchLiveAstroDataService();
//                    startPrefetchHistoryDataService();
//                }catch (Exception e){
//                    //
//                }
//            }
//        }, 500);//half second delay

        if (com.ojassoft.astrosage.varta.utils.CUtils.isFreeConsultationStarted && !com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(OnGoingChatService.class)) {
            com.ojassoft.astrosage.varta.utils.CUtils.isFreeConsultationStarted = false;
            if (!CUtils.getBooleanData(this, CGlobalVariables.IS_DARK_MODE_SELECTED, false)) {
                showDarkModePopUp();
            }
        }

    }

    private void loadSuggestedQuestion() {
        String lastLangCodeKey = CGlobalVariables.AI_HOME_SCREEN_MODULE + "_" + LAST_LANG_CODE_KEY;
        String lastLangCode = CUtils.getStringData(activity, lastLangCodeKey, "0");

        Log.e("suggested", "getSuggestQuestionForModule langcode:" + CUtils.getLanguageCode(activity));
        if (suggestedQuestionHelperClass == null || (!lastLangCode.equalsIgnoreCase(String.valueOf(CUtils.getLanguageCode(activity))))) {
            suggestedQuestionHelperClass = new SuggestedQuestionHelperClass(this, CGlobalVariables.AI_HOME_SCREEN_MODULE);
            suggestedQuestionHelperClass.getSuggestQuestionForModule(this::sendSuggestedQuestionReadyBroadcast);
        }
    }


    private void sendSuggestedQuestionReadyBroadcast() {
        Intent intent = new Intent(SuggestedQuestionBroadcast.ACTION_QUESTION_READY);

        LocalBroadcastManager
                .getInstance(this)
                .sendBroadcast(intent);
    }
    //refresh live and online astrologer lists

    /*
     * if plan id == Basic plan, Silver Monthly or Gold Monthly then the dialog will open after 3 minutes
     * Added by Monika
     * */
    private void openPlanSubscriptionDialog() {


    }

    /**
     * Shows a promotional dialog on the home screen after a delay.
     * <p>
     * This method schedules a dialog to appear after a set runtime. The specific dialog shown
     * depends on the user's current subscription plan and the available offers. The pop-up is
     * suppressed if a login screen is currently visible.
     *
     * @param offerType The type of offer to potentially display, which determines whether to show
     *                  a free introductory offer or fetch a "next recharge" offer.
     */
    public void openDialogOnHomeScreen(String offerType) {
        // Suppress the dialog if the login screen is currently active to avoid interrupting the user.
        if (isShowLoginScreen) {
            return;
        }

        new Handler().postDelayed(() -> {
            try {
                // Fetch the user's currently active subscription plan ID.
                int currentPlanId = CUtils.getUserPurchasedPlanFromPreference(ActAppModule.this);

                // Define a list of premium plans that should prevent this dialog from showing.
                boolean isUserOnPremiumPlan =
                        currentPlanId == CGlobalVariables.PLATINUM_PLAN_ID ||
                                currentPlanId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                                currentPlanId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                                currentPlanId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;

                // Only proceed if the user is NOT on a premium plan.
                if (!isUserOnPremiumPlan) {
                    // Check if the main activity is visible and if another Varta pop-up isn't already open.
                    if (activityVisible && !AstrosageKundliApplication.isOpenVartaPopup) {

                        // Case 1: The offer is the free introductory consultation.
                        if (com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE.equals(offerType)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.showConsultPremiumDialogNew(ActAppModule.this);
                        }
                        // Case 2: The offer is for the next recharge.
                        else {
                            nextRechargeOfferType = offerType;
                            getNextRechargeFromApi();
                        }
                    }
                    // Case 3: No specific offer is available or conditions aren't met, show the default Kundli AI+ pop-up.
                    else {
                        if (!com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE.equals(offerType)) {//if free chat offer not available then show kundli ai popup
                            openKundliAIPlusPopup();
                        }
                    }
                }
            } catch (Exception e) {
                // Log the exception to understand potential failures in the logic.
                e.printStackTrace();
            }
        }, runTime);
    }


    public void showCustomDialog() {

        Button buyNowText;
        ImageView crossBtnLayout;
        Typeface typeFace, typeFaceRegular, typeFaceBold;

        try {
            typeFaceBold = CUtils.getRobotoFont(this, LANGUAGE_CODE, CGlobalVariables.medium);

            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(this).inflate(R.layout.omf_plan_dialog_layout, viewGroup, false);

            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);

            //finally creating the alert dialog and displaying it
            alertDialog = builder.create();

            alertDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            buyNowText = dialogView.findViewById(R.id.buy_now_text);
            crossBtnLayout = dialogView.findViewById(R.id.cross_btn_layout);
            TextView omfHeading = dialogView.findViewById(R.id.omf_heading);
            TextView dmpaText = dialogView.findViewById(R.id.dmpa_text);
            TextView h1 = dialogView.findViewById(R.id.h1);
            TextView h2 = dialogView.findViewById(R.id.h2);
            TextView h3 = dialogView.findViewById(R.id.h3);
            TextView h4 = dialogView.findViewById(R.id.h4);
            TextView h5 = dialogView.findViewById(R.id.h5);
            TextView t1 = dialogView.findViewById(R.id.t1);
            TextView t2 = dialogView.findViewById(R.id.t2);
            TextView t3 = dialogView.findViewById(R.id.t3);
            TextView t4 = dialogView.findViewById(R.id.t4);
            TextView t5 = dialogView.findViewById(R.id.t5);


            setFinishOnTouchOutside(false);
            /*buyNowText.setTypeface(typeFaceBold);

            h1.setTypeface(typeFaceBold);
            h2.setTypeface(typeFaceBold);
            h3.setTypeface(typeFaceBold);
            h4.setTypeface(typeFaceBold);
            h5.setTypeface(typeFaceBold);
            omfHeading.setTypeface(typeFaceBold);*/

            FontUtils.changeFont(ActAppModule.this, omfHeading, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(ActAppModule.this, h1, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(ActAppModule.this, h2, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(ActAppModule.this, h3, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(ActAppModule.this, h4, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(ActAppModule.this, h5, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(ActAppModule.this, buyNowText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);


            /*typeFace = CUtils.getRobotoFont(
                    getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);
            dmpaText.setTypeface(typeFace);

            typeFaceRegular = CUtils.getRobotoFont(
                    getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
            t1.setTypeface(typeFaceRegular);
            t2.setTypeface(typeFaceRegular);
            t3.setTypeface(typeFaceRegular);
            t4.setTypeface(typeFaceRegular);
            t5.setTypeface(typeFaceRegular);*/

            FontUtils.changeFont(ActAppModule.this, t1, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(ActAppModule.this, t2, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(ActAppModule.this, t3, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(ActAppModule.this, t4, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(ActAppModule.this, t5, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(ActAppModule.this, dmpaText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);


            String datee1 = CUtils.getDialogDate(CGlobalVariables.ONE_DAY);
            CUtils.saveSilverPlanDialogData(ActAppModule.this, true, datee1);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.DHRUV_OMF_DIALOG_SHOW, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");


            buyNowText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                    CUtils.saveConsultPremimumDateData(ActAppModule.this, true, datee);

                    CUtils.fcmAnalyticsEvents(CGlobalVariables.DHRUV_OMF_DIALOG_BUYNOW_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.gotoProductPlanListUpdated(ActAppModule.this,
                            LANGUAGE_CODE, BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR, "purchase_from_dhruv_plan_dialog", true);
                    alertDialog.dismiss();
                }
            });

            crossBtnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.DHRUV_OMF_DIALOG_CROSS_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    String datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                    CUtils.saveConsultPremimumDateData(ActAppModule.this, true, datee);
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConsultPremiumDialog() {
        Button callBtn;
        TextView cancelTV, consultPremiumAstrologerTV, firstCallRsTV;
        Typeface typeFace;
        try {
            typeFace = CUtils.getRobotoFont(this, LANGUAGE_CODE, CGlobalVariables.medium);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.consult_premium_astrologer, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            consultPremiumAstrologerTV = dialogView.findViewById(R.id.consult_premium_astrologers_tv);
            firstCallRsTV = dialogView.findViewById(R.id.first_call_in_1rs_tv);
            cancelTV = dialogView.findViewById(R.id.cancel_tv);
            callBtn = dialogView.findViewById(R.id.call_btn);

            setFinishOnTouchOutside(false);

            consultPremiumAstrologerTV.setTypeface(typeFace);
            firstCallRsTV.setTypeface(typeFace);
            cancelTV.setTypeface(typeFace);
            callBtn.setTypeface(typeFace);

            String datee1 = CUtils.getDialogDate(CGlobalVariables.ONE_DAY);
            CUtils.saveSilverPlanDialogData(ActAppModule.this, true, datee1);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULT_PREMIMUM_DIALOG_OPEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CROSS_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    String datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                    CUtils.saveConsultPremimumDateData(ActAppModule.this, true, datee);

                    alertDialog.dismiss();
                }
            });
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    switchToConsultTab(0);
                    CUtils.createSession(ActAppModule.this, CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_PARTNER_ID);
                    String datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                    CUtils.saveConsultPremimumDateData(ActAppModule.this, true, datee);
                    alertDialog.dismiss();
                }
            });

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("premium_astrologers_dialog_cancelled", com.ojassoft.astrosage.varta.utils.CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*private void showConsultPremiumDialogNew() {

        Button btnChatNow;
        ImageView cancelIv;
        TextView  consultPremiumAstrologerTV, firstCallRsTV;
        try {
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.consult_preminum_astrologer_new, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            consultPremiumAstrologerTV = dialogView.findViewById(R.id.consult_premium_astrologers_tv);
            firstCallRsTV = dialogView.findViewById(R.id.first_call_in_1rs_tv);
            cancelIv = dialogView.findViewById(R.id.cancel_tv);
            btnChatNow = dialogView.findViewById(R.id.btnChatNow);
            setFinishOnTouchOutside(false);
            FontUtils.changeFont(ActAppModule.this,consultPremiumAstrologerTV,CGlobalVariables.FONTS_ROBOTO_BOLD);
            FontUtils.changeFont(ActAppModule.this,firstCallRsTV,CGlobalVariables.FONTS_ROBOTO_BLACK);
            FontUtils.changeFont(ActAppModule.this,btnChatNow,CGlobalVariables.FONTS_ROBOTO_BOLD);

            String datee1 = CUtils.getDialogDate(CGlobalVariables.ONE_DAY);
            CUtils.saveSilverPlanDialogData(ActAppModule.this, true, datee1);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULT_PREMIMUM_DIALOG_OPEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

            final int callChatType;
            String showFreeCallChatPopupType = CUtils.getStringData(ActAppModule.this, CGlobalVariables.showFreeCallChatPopupType, CGlobalVariables.TYPE_CALL);
            String callChatText = getResources().getString(R.string.first_chat_call_free);
            //chat or call by tagmanager or international login
            if(showFreeCallChatPopupType.equals(CGlobalVariables.TYPE_CHAT) || !com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(CGlobalVariables.COUNTRY_CODE_IND)){
                callChatText = callChatText.replace("#", getResources().getString(R.string.chat_now));
                btnChatNow.setText(getResources().getString(R.string.txt_chat_now));
                callChatType = FILTER_TYPE_CHAT;
            }else {
                callChatText = callChatText.replace("#", getResources().getString(R.string.call));
                btnChatNow.setText(getResources().getString(R.string.call_now));
                callChatType = FILTER_TYPE_CALL;
            }
            firstCallRsTV.setText(callChatText);

            cancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CROSS_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    String datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                    CUtils.saveConsultPremimumDateData(ActAppModule.this, true, datee);
                    alertDialog.dismiss();
                }
            });
            btnChatNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CUtils.createSession(ActAppModule.this, CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_PARTNER_ID);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    switchToConsultTab(callChatType);
                    String datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                    CUtils.saveConsultPremimumDateData(ActAppModule.this, true, datee);
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
            AstrosageKundliApplication.isOpenVartaPopup = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DialogOnHomeScreen", "exp1"+e);
        }
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            articleIdLastPathSegment = null;
            mUrl = null;
        } catch (Exception ex) {
            //Log.i(TAG, ex.getMessage());
        }
    }

    private String getMarriageUrl() {
        String blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_EN;

        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH)
            blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_EN;
        else if (LANGUAGE_CODE == CGlobalVariables.HINDI)
            blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_HI;

        return blogUrl;
    }

    private void applyClickAnimation(View viewToAnimate) {
        Animation anim;
        int delay = 100;
        anim = AnimationUtils.loadAnimation(this, R.anim.button_click_anim);
        anim.setDuration(delay);
        viewToAnimate.startAnimation(anim);
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        drawerFragment.updateLoginDetials(false, "", "", getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        if (isShowToast) {

            // create a handler to post messages to the main thread
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyCustomToast mct = new MyCustomToast(ActAppModule.this,
                            ActAppModule.this.getLayoutInflater(), ActAppModule.this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.sign_out_success));
                }
            });
        }
        if (CUtils.isConnectedWithInternet(ActAppModule.this)) {
            //new GetData().execute();
            getImageAdData();
        }

        adapter.notifyDataSetChanged();
        setsTabLayout();
        setBottomNavigationText();
    }

    private void openLanguageSelectDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("HOME_INPUT_LANGUAGE");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChooseLanguageFragmentDailog clfd = new ChooseLanguageFragmentDailog();
        clfd.show(fm, "HOME_INPUT_LANGUAGE");
        ft.commit();
    }

    @Override
    public void onSelectedLanguage(int languageIndex) {
        if (LANGUAGE_CODE != languageIndex) {
            LANGUAGE_CODE = languageIndex;
            applyChangedLanguageInApplication();
        }
    }


    private void applyChangedLanguageInApplication() {
        startActivity(new Intent(getApplicationContext(), ActAppModule.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        //this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            case SUB_ACTIVITY_EXIT_PLAN:
                if (resultCode == RESULT_OK) {
                    isExitApp = true;
                    ActAppModule.this.finish();
                }
                break;

            case BOOK_MARK_LIST_ACTIVITY_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bCTP = data.getExtras();
                    int gid = bCTP.getInt("GORUP_ID");
                    int cid = bCTP.getInt("CHILD_ID");

                    callCalculateKundli(0, true, gid, cid);

                }
                break;
            case SUB_ACTIVITY_BHRIGOO:
                if (data != null) {
                    int index = data.getIntExtra("index", 0);
                    mViewPager.setCurrentItem(index);
                }

                break;
            case BACK_FROM_PROFILE_CHAT_DIALOG:
                if (data != null) {
                    boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                    UserProfileData userProfileDataBean = (UserProfileData) data.getExtras().get("USER_DETAIL");
                    String fromWhere = data.getStringExtra("fromWhere");
                    String returnedUrlText = data.getStringExtra("urlText");
                    String consultationType = data.getStringExtra("consultationType");
                    if (TextUtils.isEmpty(consultationType) && TextUtils.equals(returnedUrlText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                        consultationType = returnedUrlText;
                    }
                    if (TextUtils.isEmpty(consultationType) && TextUtils.equals(fromWhere, com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                        consultationType = fromWhere;
                    }
                    if (TextUtils.isEmpty(consultationType)) {
                        consultationType = ChatUtils.getInstance(this).consultationType;
                    }
                    boolean isDummyChat = data.getBooleanExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW, false);
                    /*if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_CHAT)) {
                        ChatUtils.getInstance(ActAppModule.this).startChat(userProfileDataBean);
                    } else */
                    AstrosageKundliApplication.backgroundLoginCountForChat = 0;
                    if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT)) {
                        if (isDummyChat) {
                            CUtils.openAINotificationChatWindow(this, CUtils.AINotificationQuestion, CUtils.AIRevertQCount, CUtils.AINotificationAstroId, CUtils.AINotificationTitle, CUtils.isAIAstrologerOnline);
                        } else {
                            ChatUtils.getInstance(ActAppModule.this).startAIChat(userProfileDataBean);
                        }
                    } else if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_CHAT_RANDOM)) {
                        ChatUtils.getInstance(ActAppModule.this).startChatRandom(userProfileDataBean, AstrosageKundliApplication.apiCallingSource);
                    } else if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                        ChatUtils.getInstance(ActAppModule.this).startAIChatRandom(userProfileDataBean, AstrosageKundliApplication.apiCallingSource);
                    } else if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_VIDEO_CALL)) {
                        ChatUtils.getInstance(ActAppModule.this).startVideoCall(userProfileDataBean);
                    } else if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_VOICE_CALL)) {
                        ChatUtils.getInstance(ActAppModule.this).startAudioCall(userProfileDataBean);
                    } else if (!isProceed && data.getExtras().containsKey("openKundliList")) {
                        if (isDummyChat) {
                            com.ojassoft.astrosage.varta.utils.CUtils.openSavedKundliList(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW, fromWhere, 2001);
                        } else {
                            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                                com.ojassoft.astrosage.varta.utils.CUtils.openSavedKundliList(ActAppModule.this, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), fromWhere, 2001);
                            } else if (TextUtils.equals(consultationType, com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                                com.ojassoft.astrosage.varta.utils.CUtils.openSavedKundliList(ActAppModule.this, consultationType, fromWhere, 2001);
                            }
                        }
                    } else if (!isProceed && data.getExtras().containsKey("openProfileForChat")) {
                        boolean prefillData = true;
                        if (data.getExtras().containsKey("prefillData")) {
                            prefillData = data.getBooleanExtra("prefillData", true);
                        }
                        Bundle bundle = data.getExtras();
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                            com.ojassoft.astrosage.varta.utils.CUtils.openProfileForChat(ActAppModule.this, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), fromWhere, bundle, prefillData, 2001);
                        } else if (TextUtils.equals(consultationType, com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.openProfileForChat(ActAppModule.this, consultationType, fromWhere, bundle, prefillData, 2001);
                        }
                    } else if (isProceed && fromWhere.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_KUNDLI_CHAT) && userProfileDataBean != null && !userProfileDataBean.getName().isEmpty()) {
                        PersonalizedCategoryENUM personalizedCategoryENUM = null;
                        try {
                            if (data.getExtras().containsKey(CLICKED_CATEGORY_ENUM_KEY)) {
                                personalizedCategoryENUM = PersonalizedCategoryENUM.valueOf(data.getStringExtra(CLICKED_CATEGORY_ENUM_KEY));
                            }
                            boolean isFromChatBox = data.getBooleanExtra(IS_OPENED_FROM_K_AI_CHAT_BTN,false);
                            openKundliAIScreen(personalizedCategoryENUM, userProfileDataBean, isFromChatBox);
                        } catch (Exception e) {
                            //
                        }

                    } else {
                        AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
                    }

                }
                break;
            case BACK_FROM_PLAN_PURCHASE_AD_SCREEN:
                Intent rashiPredictionIntent = new Intent(this, DetailedHoroscope.class);
                rashiPredictionIntent.putExtra("rashiType", CUtils.getMoonSignIndex(this));
                rashiPredictionIntent.putExtra("prediction_type", 1);//com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_HOROSCOPE);
                rashiPredictionIntent.putExtra("screenType", 0);
                startActivity(rashiPredictionIntent);
                break;
            default:
                break;
        }
    }

    /**
     * Prepares and launches the Kundli AI chat screen (MiniChatWindow).
     * <p>
     * This method orchestrates the necessary steps to open a chat window for a specific user profile
     * and a selected astrological category. It generates a unique conversation ID, prepares the
     * required data models, and passes all necessary information to the MiniChatWindow activity
     * via an Intent.
     *
     * @param personalizedCategoryENUM The specific astrological category (e.g., LOVE, CAREER) the user
     *                                 is interested in. This determines the title and suggested questions.
     *                                 Cannot be null.
     * @param userProfileData          The user profile data (name, DOB, etc.) for which the Kundli
     *                                 is being analyzed. This is used to generate a stable
     *                                 conversation ID. Cannot be null.
     * @param isFromChatBox            A boolean flag indicating if the screen was opened from a context
     *                                 where a random question should be pre-filled (like the home
     *                                 screen chat button).
     */
    public void openKundliAIScreen(PersonalizedCategoryENUM personalizedCategoryENUM, UserProfileData userProfileData, boolean isFromChatBox) {
        try {
            // 1. Generate a stable conversation ID based on user's birth details and module name.
            // This ensures that returning to the chat for the same person re-opens the same history.
            KundliHistoryDao chatDao = KundliHistoryDao.getInstance(this);
            String conversationId = chatDao.getConversationId(userProfileData.getName().split(" ")[0]
                    + userProfileData.getDay()
                    + userProfileData.getMonth()
                    + userProfileData.getYear()
                    + userProfileData.getHour()
                    + userProfileData.getMinute(), getString(R.string.text_kundli_ai));

            // 2. Create the underlying data model required for Kundli AI calculations.
            KundliModules_Frag.createModelForKundliAI(userProfileData);

            // 3. Prepare the Intent to launch the MiniChatWindow.
            Intent intent = new Intent(ActAppModule.this, MiniChatWindow.class);

            // 4. Get screen details (ID and localized name) from the category enum.
            int screenId = personalizedCategoryENUM.getScreenID();
            String screenName;
            Resources resources;
            if (LANGUAGE_CODE == 1) { // English
                resources = getLocaleResources(this, "en");
            } else {
                resources = getLocaleResources(this, getLanguageKey(LANGUAGE_CODE));
            }
            screenName = personalizedCategoryENUM.getTitle(getApplicationContext(), resources);

            // 5. Add all necessary data as extras to the Intent.
            if (isFromChatBox) {
                // If coming from the home chat button, pre-fill a random question.
                intent.putExtra(LAGANA_CHART_QUESTION, KundliModules_Frag.randomQuestion);
            }
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID, conversationId);

            if (suggestedQuestionHelperClass != null) {
                // Pass a list of relevant suggested questions for the specific screen.
                intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestionHelperClass.getSuggestedQuestionsForScreenId(screenId));
            }

            intent.putExtra(KEY_MODULE_ID, CGlobalVariables.AI_HOME_SCREEN_MODULE);
            intent.putExtra(CURRENT_SCREEN_ID_KEY, screenId);
            intent.putExtra(KEY_SCREEN_NAME, screenName);
            intent.putExtra(KEY_IS_CHAT_FOR_HOME_CATEGORY, true);

            // Flag to indicate this is not a chat being reopened from history.
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FROM_HISTORY, false);

            // Add a source string for analytics tracking.
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN_HOME + "_" + personalizedCategoryENUM.name());

            // 6. Launch the chat screen.
            startActivity(intent);

        } catch (Exception e) {
            // Log any unexpected errors that occur during the process to prevent crashes.
            Log.e("TAG", "openKundliAIScreen: Exception" + e.getMessage());
        }
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        //Log.d("LoginFlow", " varta: loginName" + loginName);
        drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }

    protected void onDestroy() {
        // TODO Auto-generated method stub
        try {
            if (isExitApp) {
                // Process.killProcess(Process.myPid());
            }

        } catch (Exception e) {

        }
        if (updateListener != null) {
            appUpdateManager.unregisterListener(updateListener);
        }
        super.onDestroy();
    }

    List<String> getDrawerListItem() {

        try {
            String[] menuItems1 = getResources().getStringArray(R.array.app_home_menu_item_list);
            String[] menuItems2 = getResources().getStringArray(R.array.module_list);
            return CUtils.getDrawerListItem(ActAppModule.this, menuItems1, menuItems2, app_home_menu_item_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }

    }

    List<Drawable> getDrawerListItemIcon() {

        try {
            TypedArray itemsIcon1 = getResources().obtainTypedArray(R.array.app_home_menu_item_list_icon);
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.module_icons);

            return CUtils.getDrawerListItemIcon(ActAppModule.this, itemsIcon1, itemsIcon2, app_home_menu_item_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }

    }

    List<Integer> getDrawerListItemIndex() {
        try {
            return CUtils.getDrawerListItemIndex(ActAppModule.this, app_home_menu_item_list_index, module_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }
    }

    @Override
    public void gotoHomeScreen(int id) {
        try {
            mViewPager.setCurrentItem(id);
        } catch (Exception ex) {

        }
    }

    protected void onIntent(Intent intent) {
        actionOnIntent(intent);
    }

    public void actionOnIntent(Intent intent) {
        try {

            String action = intent.getAction();
            Uri data = intent.getData();
            //Log.e("actionOnIntent", "action = " + action);

            //  Map<String, String> keyValuePairs=CUtils.splitQuery(data);
            if (Intent.ACTION_VIEW.equals(action) && data != null) {
                //Log.e("actionOnIntent", "Uri = " + data.toString());
                //redirect to astro shop module
                articleIdLastPathSegment = data.getLastPathSegment();
                articleId = data.toString();

                mUrl = data;

                String p_ID = data.getQueryParameter("prtnr_id");
                String offerType = data.getQueryParameter("offertype");
                //    //Log.e("PartnerId",p_ID);
                if (p_ID != null && !p_ID.isEmpty()) {
                    if (!sessionedPartnerId.equalsIgnoreCase(CGlobalVariables.NOTIFICATION_PARTNER_ID)) {
                        sessionedPartnerId = p_ID;
                    }
                    CUtils.createSession(ActAppModule.this, sessionedPartnerId);
                }
                if (articleId.contains(CGlobalVariables.AI_KUNDLI_PLUS)) {
                    com.ojassoft.astrosage.varta.utils.CUtils.openPurchaseDhruvPlanActivity(ActAppModule.this, false, true, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_NOTIFICATION_CLICK,false,false);
                } else if (articleId.contains(CGlobalVariables.CREATE_KUNDLI_LINK)) {
                    CUtils.openCreateKundliActivity(this);
                } else if (articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls)
                        || articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_url)) {

                    boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this);
                    if (articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_UPCOMING)) {
                        //   Log.d("actionOnIntentTest","openAstrologerDetail");
                        Intent i = new Intent(this, AllLiveAstrologerActivity.class);
                        i.putExtra("upcoming", "upcoming");
                        startActivity(i);
                    } else if (articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_LIVE)) {
                        openLiveScreen(articleIdLastPathSegment);
                        //Log.d("actionOnIntentTest","openAstrologerDetail");
                    } else if (articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_ASTROLOGER)) {
                        openAstrologerDetail(this, articleIdLastPathSegment, offerType, true, false, articleId);
                        //Log.d("actionOnIntentTest","openAstrologerDetail");
                    } else if (articleId.contains(LINK_HAS_FOLLOW)) {
                        openAstrologerDetail(this, articleIdLastPathSegment, true, true, articleId);
                        //com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail(this, articleIdLastPathSegment, true);
                        //Log.d("actionOnIntentTest","openAstrologerDetail");
                    } else if (articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.rechargeNow)) {
                        isUserLogin(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RECHARGE_SCRREN);
                    } else if (articleIdLastPathSegment.equals(CGlobalVariables.live_astrologers)) {
                        //Live Astrologer
                        CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_LIVE_ASTROLGERS, null);
                        Intent tvIntent = new Intent(this, AllLiveAstrologerActivity.class);
                        startActivity(tvIntent);
                    } else if (articleIdLastPathSegment.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.consultationHistory) || articleIdLastPathSegment.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.rechargeNow)) {
                        if (isLogin) {
                            openWalletScreen("act_app_module");
                        } else {
                            if (articleId.contains(CGlobalVariables.talk_to_astrologers)) {
                                //switchToConsultTab(FILTER_TYPE_CALL);
                                com.ojassoft.astrosage.varta.utils.CUtils.openCallList(ActAppModule.this);
                            } else if (articleId.contains(CGlobalVariables.chat_with_astrologers)) {
                                //switchToConsultTab(FILTER_TYPE_CHAT);
                                switchToConsultTab(FILTER_TYPE_CALL); //redirect to ai list
                            } else if (articleId.contains(CGlobalVariables.open_ai_astrologers)) {
                                CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_AI_CHAT, null);
                                com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(ActAppModule.this);
                            }
                        }
                    } else if (articleId.contains(CGlobalVariables.chat_with_ai_astrologers)) {
                        CUtils.AINotificationAstroId = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID);
                        CUtils.AINotificationQuestion = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION);

                        AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs + "\n ActAppModule CUtils.AINotificationAstroId=" + CUtils.AINotificationAstroId;

                        CUtils.AINotificationTitle = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE);
                        CUtils.AIRevertQCount = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT);
                        CUtils.isAIAstrologerOnline = intent.getBooleanExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE, false);
                    } else if (articleId.contains(CGlobalVariables.chat_with_kundli_ai)) {
                        try {
                            String category = intent.getStringExtra(CGlobalVariables.KEY_FOR_CATEGORY_ID);
                            // Log.e("testNotification", "actionOnIntent: category "+category);
                            int screenId = category != null ? Integer.parseInt(category) : CGlobalVariables.LOVE_CATEGORY_SCREEN_ID; // default -> 175 love category
                            //getting Category enum with corresponding screen id
                            startKundliAIChatByCategory(screenId);
                        } catch (Exception e) {
                            Log.e("testNotification", "actionOnIntent:exception  " + e.getMessage());
                        }

                    } else {
                        if (articleId.contains(CGlobalVariables.talk_to_astrologers)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY, null);
                            //switchToConsultTab(FILTER_TYPE_CALL);
                            com.ojassoft.astrosage.varta.utils.CUtils.openCallList(ActAppModule.this);
                        } else if (articleId.contains(CGlobalVariables.chat_with_astrologers)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_CHAT, null);
                            //switchToConsultTab(FILTER_TYPE_CHAT);
                            switchToConsultTab(FILTER_TYPE_CALL); //redirect to ai list
                        } else if (articleId.contains(CGlobalVariables.open_ai_astrologers)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_AI_CHAT, null);
                            com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(ActAppModule.this);
                        }
                    }

                } else if (articleId.contains(CGlobalVariables.buy_astrosage_url) ||
                        articleId.contains(CGlobalVariables.buy_astrosage_urls)) {

                    if (articleIdLastPathSegment == null) {
                        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                            gotoHomeScreen(2); //astro Shop module
                        } else {
                            gotoHomeScreen(1); //astro Shop module
                        }
                        //gotoHomeScreen(1); //astro Shop module
                    } else {

                        if (articleIdLastPathSegment.equals(CGlobalVariables.gold_plan_ads)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_GOLD_PLAN, null);
                            CUtils.gotoProductPlanListUpdated(ActAppModule.this, LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.GOLD_PLAN_VALUE_YEAR, "deeplink");
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.silver_plan_ads)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_SILVER_PLAN, null);
                            CUtils.gotoProductPlanListUpdated(ActAppModule.this, LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SILVER_PLAN_VALUE_YEAR, "deeplink");
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.platinum_plan_ads)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_PLATINUM_PLAN, null);
                            CUtils.gotoProductPlanListUpdated(ActAppModule.this, LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR, "deeplink");
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.ask_A_Question_Android)) {
                            //Ask a question
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION, null);
                            CUtils.sendToActAskQuestion(ActAppModule.this, articleIdLastPathSegment);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.ask_A_Question_For_Marriage_Android)) {
                            //Ask a question Marriage
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_MARRIAGE, null);
                            CUtils.sendToActAskQuestionForMarriageResults(ActAppModule.this, articleIdLastPathSegment, false);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.ask_A_Question_For_Career_Android)) {
                            //Ask a question Career
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_CAREER, null);
                            CUtils.sendToActAskQuestion(ActAppModule.this, articleIdLastPathSegment);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.ask_A_Question_For_Health_Android)) {
                            //Ask a question Health
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_HEALTH, null);
                            CUtils.sendToActAskQuestion(ActAppModule.this, articleIdLastPathSegment);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.ask_A_Question_For_House_Android)) {
                            //Ask a question House
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_HOUSE, null);
                            CUtils.sendToActAskQuestion(ActAppModule.this, articleIdLastPathSegment);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.ask_A_Question_For_Telephonic_Consultation_Android)) {
                            //Ask a question Telephonic Consultation
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_TELEPHONIC_CONSULTATION, null);
                            CUtils.sendToActAskQuestion(ActAppModule.this, articleIdLastPathSegment);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.chat_with_astrologer_paid)) {
                            //Chat with Astrologer
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_CHAT_WITH_ASTROLOGER, null);
                            CUtils.sendToAstroChat(ActAppModule.this);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_tv)) {
                            //Chat with Astrologer
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASTROSAGE_TV, null);
                            Intent tvIntent = new Intent(this, ActAstrosageTV.class);
                            startActivity(tvIntent);
                            // CUtils.sendToAstroChat(ActAppModule.this);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_learn_astrology)) {
                            //Chat with Astrologer
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_LEARN_ASTROLOGY, null);
                            Intent tvIntent = new Intent(this, ActLearnAstrology.class);
                            startActivity(tvIntent);
                            // CUtils.sendToAstroChat(ActAppModule.this);
                        }
                        //mahtab deep linking
                        /*else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_big_horscope)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BIG_HORSCOPE_ACTIVITY, null);
                            Intent tvIntent F= new Intent(this, BigHorscopeActivity.class);
                            this.startActivity(tvIntent);

                        }*/
                        else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_brihat_horscope)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BRIHAT_HORSCOPE_ACTIVITY, null);
                            boolean showNewBrihatScreen = CUtils.getBooleanData(activity, CGlobalVariables.SHOW_NEW_BRIHAT_KUNDLI_PAGE, false);
                            Intent tvIntent;
                            tvIntent = showNewBrihatScreen ? new Intent(activity, BrihatKundliActivity.class) : new Intent(activity, BrihatActivity.class);
                            CUtils.BRIHAT_KUNDALI_PURCHASE_SOURCE = "deeplink";
                            this.startActivity(tvIntent);

                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_cogni_astro)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_ACTIVITY, null);
                            Intent tvIntent = new Intent(this, CogniAstroActivity.class);
                            this.startActivity(tvIntent);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_dhruv_virtual_url)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_DHRUV_ACTIVITY, null);

                            if (CUtils.isDhruvPlan(ActAppModule.this)) {
                                Intent dhruvIntent = new Intent(ActAppModule.this, ActUserPlanDetails.class);
                                this.startActivity(dhruvIntent);
                            } else {
                                CUtils.gotoProductPlanListUpdated(ActAppModule.this, ActAppModule.LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, SCREEN_ID_DHRUV, "upgrade_plan");
                            }
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_dhruv_pop_virtual_url)) {
                            //showCustomDialog();
                            Intent i = new Intent(ActAppModule.this, SilverPlanSubscriptionFragmentDailog.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else if (articleIdLastPathSegment.equals(CGlobalVariables.astrosage_join_varta_virtual_url)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_JOIN_VARTA_ACTIVITY, null);
                            CUtils.gotoJoinVartaPanel(ActAppModule.this, LANGUAGE_CODE);
                        }
                        // Services deep linking

                        else if (data.toString().contains(CGlobalVariables.buy_astrosage_url + "/" + CGlobalVariables.ASTROSHOP_SERVICES_DEEP_LINKING) ||
                                data.toString().contains(CGlobalVariables.buy_astrosage_urls + "/" + CGlobalVariables.ASTROSHOP_SERVICES_DEEP_LINKING)) {

                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_SERVICES, null);
                            if (articleIdLastPathSegment.equals(CGlobalVariables.ASTROSHOP_SERVICES_DEEP_LINKING)) {
                                CUtils.sendToAstoServices(ActAppModule.this);
                                CUtils.BRIHAT_KUNDALI_PURCHASE_SOURCE = "deeplink";
                            } else {
                                String entry = CUtils.getStringData(this, CGlobalVariables.Astroshop_Service_Data + LANGUAGE_CODE, "");
                                if (entry != null && !entry.isEmpty()) {
                                    try {
                                        String saveData = entry;//new String(entry.data, "UTF-8");
                                        List<ServicelistModal> datasaved;
                                        Gson gson = new Gson();
                                        JsonElement element = gson.fromJson(saveData, JsonElement.class);
                                        //com.google.analytics.tracking.android.//Log.e("Element" + element.toString());
                                        datasaved = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
                                        }.getType());
                                        CUtils.goToServiceDescription(ActAppModule.this, datasaved, articleId, "ActApp_DeepLink", false);

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                } else {
                                    CUtils.callActAstroServices(ActAppModule.this, articleId, "ActApp_DeepLink");
                                }
                            }


                        }

                        //Astrologer deep linking

                        else if (data.toString().contains(CGlobalVariables.buy_astrosage_url + "/" + CGlobalVariables.ASTROSHOP_ASTROLOGER_DEEP_LINKING) ||
                                data.toString().contains(CGlobalVariables.buy_astrosage_urls + "/" + CGlobalVariables.ASTROSHOP_ASTROLOGER_DEEP_LINKING)) {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASTROLOGERS, null);
                            if (articleIdLastPathSegment.equals(CGlobalVariables.ASTROSHOP_ASTROLOGER_DEEP_LINKING)) {
                                CUtils.sendToAstoAstrologer(ActAppModule.this);
                            } else {
                                Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
                                Cache.Entry entry = cache.get(CGlobalVariables.astrologerLive);
                                if (entry != null) {
                                    try {
                                        String saveData = new String(entry.data, StandardCharsets.UTF_8);
                                        List<AstrologerInfo> datasaved;
                                        Gson gson = new Gson();
                                        JsonElement element = gson.fromJson(saveData, JsonElement.class);
                                        //com.google.analytics.tracking.android.//Log.e("Element" + element.toString());
                                        datasaved = gson.fromJson(saveData, new TypeToken<ArrayList<AstrologerInfo>>() {
                                        }.getType());
                                        CUtils.goToAstrologerServiceDescription(ActAppModule.this, datasaved, articleId, false);

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                } else {
                                    CUtils.callActAstrologer(ActAppModule.this, articleId);
                                }
                            }

                        }

                        //end mahtab


                        else {
                            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_PRODUCTS, null);
                            String entry = CUtils.getStringData(this, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
                            if (entry != null && !entry.isEmpty()) {
                                try {
                                    String saveData = entry;//new String(entry.data, "UTF-8");
                                    String[] array = parseAstroShopData(saveData, articleIdLastPathSegment);
                                    if (array != null) {
                                        //initializeGoogleIndexing(array);
                                        int pos = Integer.parseInt(array[2]);
                                        CUtils.callActAstroShop(pos, ActAppModule.this, "");
                                    } else {
                                        if (CategoryFullName != null) {
                                            boolean result = false;
                                            for (String titleName : CategoryFullName) {
                                                result = getItemsInDetail(titleName, saveData);
                                                if (result) {
                                                    break;
                                                }
                                            }

                                            if (!result) {
                                                //
                                                CUtils.saveStringData(this, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
                                                CUtils.callActAstroShop(0, ActAppModule.this, articleId);
                                            }

                                        } else {
                                            //
                                            CUtils.saveStringData(this, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
                                            CUtils.callActAstroShop(0, ActAppModule.this, articleId);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                CUtils.callActAstroShop(0, ActAppModule.this, articleId);
                            }
                        }

                    }
                } else if (articleId.contains(CGlobalVariables.astrosage_services_url) ||
                        articleId.contains(CGlobalVariables.astrosage_services_urls)) {
                    CUtils.getUrlLink(articleId, ActAppModule.this, LANGUAGE_CODE, 0);
                } else if (articleId.contains(CGlobalVariables.astrosage_offers_url) ||
                        articleId.contains(CGlobalVariables.astrosage_offers_urls)) {
                    CUtils.sendToShowOffers(ActAppModule.this, articleId);
                } else if (articleId.contains(CGlobalVariables.astrosage_varta_join_url)) {
                    Intent vartaJoinIntent = new Intent(ActAppModule.this, VartaReqJoinActivity.class);
                    startActivity(vartaJoinIntent);
                } else if (articleId.contains(CGlobalVariables.openShareChartDeepLink) ||
                        articleId.contains(CGlobalVariables.openShareChartDeepLinks)) {
                    Typeface typeface = CUtils.getRobotoFont(
                            ActAppModule.this, LANGUAGE_CODE, CGlobalVariables.regular);
                    CUtils.sendToOpenChart(ActAppModule.this, articleIdLastPathSegment, typeface, queue);
                }
                //youtube Notification deeplink
                else if (articleId.contains(CGlobalVariables.astrosage_youtube_url) ||
                        articleId.contains(CGlobalVariables.astrosage_youtube_urls)) {

                    if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                        gotoHomeScreen(5);//youtube video
                    } else {
                        gotoHomeScreen(4);//youtube video
                    }
                    //gotoHomeScreen(4);//youtube video
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_YOUTUBE_VIDEO, null);
                    Intent intentforYoutube = new Intent(ActAppModule.this, ActYouTubePlayer.class);
                    Bundle bundleYoutube = new Bundle();
                    if (articleId.contains("v=")) {
                        bundleYoutube.putString("playlist", articleId.split("v=")[1]);
                        bundleYoutube.putInt("position", 0);
                        bundleYoutube.putBoolean("fromSearch", true);
                        bundleYoutube.putBoolean("fromNotification", true);
                        intentforYoutube.putExtras(bundleYoutube);
                        startActivityForResult(intentforYoutube, 5100);
                    }
                }//share app Notification deeplink
                else if (articleId.contains(CGlobalVariables.astrosage_shareapp_url) ||
                        articleId.contains(CGlobalVariables.astrosage_shareapp_urls)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_SHAREAPP, null);
                    gotoHomeScreen(0);
                    CUtils.shareToFriendMail(this);

                } else if (articleId.contains(CGlobalVariables.astrosage_topics_httpurl) ||
                        articleId.contains(CGlobalVariables.astrosage_topics_httpsurl) ||
                        articleId.contains(CGlobalVariables.astrology_topics_httpurl) ||
                        articleId.contains(CGlobalVariables.astrology_topics_httpsurl) ||
                        articleId.contains(CGlobalVariables.jyotish_topics_httpurl) ||
                        articleId.contains(CGlobalVariables.jyotish_topics_httpsurl)) {
                    Intent intentForArtical = new Intent(ActAppModule.this, ActShowOjasSoftArticlesWithTabs.class);
                    intentForArtical.putExtra("BLOG_LINK_TO_SHOW", articleId);// CHANGED
                    startActivity(intentForArtical);

                } else if (articleId.contains(OPEN_KUNDALI_VIRTUAL_URLS) || articleId.contains(OPEN_KUNDALI_VIRTUAL_URL)) {
                    openKundaliByNotification(data);
                } else if (articleIdLastPathSegment.equals(CGlobalVariables.talk_to_astrologers)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY, null);
                    //switchToConsultTab(FILTER_TYPE_CALL);
                    com.ojassoft.astrosage.varta.utils.CUtils.openCallList(ActAppModule.this);
                } else if (articleIdLastPathSegment.equals(CGlobalVariables.chat_with_astrologers)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_CHAT, null);
                    //switchToConsultTab(FILTER_TYPE_CHAT);
                    switchToConsultTab(FILTER_TYPE_CALL); //redirect to ai list
                } else if (articleId.contains(CGlobalVariables.open_ai_astrologers)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_AI_CHAT, null);
                    com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(ActAppModule.this);
                } else if (articleId.contains(CGlobalVariables.learn_astrology_url)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ACT_LEARN_ACTIVITY, null);
                    Intent intentActLearnAstrology = new Intent(ActAppModule.this, ActLearnAstrology.class);
                    startActivity(intentActLearnAstrology);

                } else if (articleId.contains(CGlobalVariables.numrology_url)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_NUMOROLOGY_ACTIVITY, null);
                    Intent intentNumerology = new Intent(ActAppModule.this, NumerologyCalculatorInputActivity.class);
                    startActivity(intentNumerology);

                } else if (articleId.contains(CGlobalVariables.match_making_url)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_HOME_MATCH_MAKING_ACTIVITY, null);
                    Intent intentMatchMaking = new Intent(ActAppModule.this, HomeMatchMakingInputScreen.class);
                    startActivity(intentMatchMaking);

                } else if (articleId.contains(CGlobalVariables.horoscope_url)) {
                    CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_HOROSCOPE_HOME_ACTIVITY, null);
                    Intent intentHoroscope = new Intent(ActAppModule.this, HoroscopeHomeActivity.class);
                    startActivity(intentHoroscope);

                } else if (articleId.contains(CGlobalVariables.astrosage_shop_url)) {// astrosage.shop url
                    String utmCampaign = "cat_"+articleIdLastPathSegment;
                    CUtils.openAstroSageShopWebView(ActAppModule.this, articleId,CGlobalVariables.AKNOTIFICTIONCLICK, utmCampaign);
                } else {
                    //added by Ankit on 29-8-2019
                    if (URLUtil.isValidUrl(articleId)) {
                        Uri uri = Uri.parse(articleId);
                        String isBrowser = uri.getQueryParameter(CGlobalVariables.KEY_IS_BROWSER);
                        if (!TextUtils.isEmpty(isBrowser) && isBrowser.equalsIgnoreCase("true")) {
                            CUtils.openWebBrowserOnlyChrome(ActAppModule.this, uri);
                        } else {
                            CUtils.sendToShowOffers(ActAppModule.this, uri.toString());
                        }
                    }
                }
            } else {
                int pagerPostion = intent.getIntExtra("position", 0);
                if (pagerPostion == 2) {
                    int consultationType = intent.getIntExtra("consultationType", 0);
                    switchToConsultTab(consultationType);
                } else {
                    gotoHomeScreen(pagerPostion);
                }
                //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                gotoHomeScreen(2); //astro Shop module
            } else {
                gotoHomeScreen(1); //astro Shop module
            }
            //gotoHomeScreen(1); //astro Shop module
        }
    }

    private boolean checkForListInnerData(List<AstroShopItemDetails> alldta, String articleIdLastPathSegment, int postion) {

        boolean resultFound = false;

        for (int i = 0; i < alldta.size(); i++) {
            if (alldta.get(i).getP_url_text().trim().equalsIgnoreCase(articleIdLastPathSegment)) {
                AstroShopItemDetails astroShopItemDetails = alldta.get(i);
                String[] array = new String[]{astroShopItemDetails.getPName(), astroShopItemDetails.getPSmallDesc()};
                // initializeGoogleIndexing(array);
                resultFound = true;
                CUtils.goToFoundedItemScreen(postion, astroShopItemDetails, ActAppModule.this, alldta);
            }
        }

        return resultFound;
        /*if(!resultFound){
            CUtils.callActAstroShop(0,ActAppModule.this);
        }*/

    }

    private boolean getItemsInDetail(String title, String mainData) {
        boolean resultFound = false;
        try {
            List<AstroShopItemDetails> alldta = new ArrayList<>();
            int pos = 0;
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject innerObject;
            JSONArray arrayOfItems;
            for (int i = 1; i < jsonArray.length(); i++) {
                pos = i - 1;
                innerObject = jsonArray.getJSONObject(i);
                Iterator<String> iter = innerObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (key.equals(title)) {
                        arrayOfItems = innerObject.getJSONArray(title);
                        if (arrayOfItems != null) {
                            alldta.addAll(new Gson().fromJson(arrayOfItems.toString(), new TypeToken<ArrayList<AstroShopItemDetails>>() {
                            }.getType()));

                        }
                    }
                }
            }

            if (alldta.size() > 0) {
                resultFound = checkForListInnerData(alldta, articleIdLastPathSegment, pos);
            } /*else {
                CUtils.callActAstroShop(0, ActAppModule.this, "");
            }*/
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return resultFound;
    }

    private String[] parseAstroShopData(String data, String lastSegment) {
        String[] array = null;
        CategoryFullName = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(data);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            JSONArray jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");

            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {

                String catUrl = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryUrl").trim().toLowerCase();
                CategoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));

                if (catUrl.equalsIgnoreCase(lastSegment)) {
                    array = new String[3];
                    array[0] = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName");
                    array[1] = jsonArrayProductCategoryName.getJSONObject(i).getString("CategorySmallDescription");
                    array[2] = i + "";
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return array;
    }

    @Override
    public void navigateToAstroshopFrag() {
        //mViewPager.setCurrentItem(1);
        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            mViewPager.setCurrentItem(2);
        } else {
            mViewPager.setCurrentItem(1);
        }
    }

    /**
     * Navigates the home pager to the tab that currently hosts {@link Video_Frag}.
     * This avoids hardcoded indexes when the yearly tab is hidden for some languages.
     */
    public void navigateToVideoTab() {
        if (mViewPager == null) {
            return;
        }

        if (mViewPager.getAdapter() instanceof ViewPagerAdapter) {
            ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) mViewPager.getAdapter();
            for (int index = 0; index < viewPagerAdapter.getCount(); index++) {
                Fragment fragment = viewPagerAdapter.getFragment(index);
                if (fragment instanceof Video_Frag) {
                    mViewPager.setCurrentItem(index);
                    return;
                }
            }
        }

        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            mViewPager.setCurrentItem(5);
        } else {
            mViewPager.setCurrentItem(4);
        }
    }

    private void fetchProductFromGoogleServer() {

        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(SKU_ASKQUESTION_PLAN)
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build())).build();

        AstrosageKundliApplication.billingClient.queryProductDetailsAsync(
                queryProductDetailsParams, (billingResult, productDetailsList) -> {
                    int response = billingResult.getResponseCode();
                    Log.e("BillingClient", "onProductDetailsResponse() response=" + response);
                    if (response == BillingClient.BillingResponseCode.OK) {
                        for (ProductDetails productDetails : productDetailsList) {
                            intiProductPlan(productDetails);
                        }
                    }
                }
        );

    }

    private void intiProductPlan(ProductDetails productDetails) {
        try {
            Log.e("BillingClient", "skuDetails getSku " + productDetails.getProductId());
            if (productDetails.getProductId().equalsIgnoreCase(SKU_ASKQUESTION_PLAN)) {
                String questionprice = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
                CUtils.saveStringData(ActAppModule.this, CGlobalVariables.ASTROASKAQUESTIONPRICE, questionprice);
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }

    /**
     * @author amit Rautela
     * @desc This method is used to get the data from cointainer
     */

    public void getDataFromGTMCointainer(NetworkImageView imgBanner) {
        try {
            //Getting Variables for MainModuleCustomAds
            boolean key_MainModuleCustomAdsVisibility = CUtils.getBooleanData(ActAppModule.this, CGlobalVariables.key_MainModuleCustomAdsVisibility, false);
            if (key_MainModuleCustomAdsVisibility) {
                String key = CUtils.getLanguageKey(LANGUAGE_CODE);
                String mainModuleCustomAdsImageData = CUtils.getStringData(ActAppModule.this, CGlobalVariables.key_MainModuleCustomAdsImageUrl, "");
                String mainModuleCustomAdsImageUrl = CUtils.getLanguageBasedUrl(mainModuleCustomAdsImageData, key);
                if (!mainModuleCustomAdsImageUrl.equals("")) {
                    String mainModuleCustomAdsImageClickListenerUrl = CUtils.getStringData(ActAppModule.this, CGlobalVariables.key_MainModuleCustomAdsImageClickListenerUrl, "");
                    imgBanner.setVisibility(View.VISIBLE);
                    imgBanner.setImageUrl(mainModuleCustomAdsImageUrl, VolleySingletonForDefaultHttp.getInstance(ActAppModule.this).getImageLoader());
                    setImgBannerClickListener(imgBanner, mainModuleCustomAdsImageClickListenerUrl);
                } else {
                    //due to some error
                    imgBanner.setOnClickListener(null);
                    imgBanner.setVisibility(View.GONE);
                }
            } else {
                imgBanner.setOnClickListener(null);
                imgBanner.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            //Log.i("ActAppModule", "getDataFromCointainer - " + ex.getMessage());
        }
    }

    /**
     * @param url
     * @desc This method is used to set the listener of Image Ad Banner
     */
    private void setImgBannerClickListener(NetworkImageView imgBanner, final String url) {

        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("called", "Intent");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                intent.setData(uri);

                if (url.contains(CGlobalVariables.buy_astrosage_url) ||
                        url.contains(CGlobalVariables.buy_astrosage_urls)) {
                    onIntent(intent);
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    public void getImageAdData() {
        RequestQueue queue = VolleySingleton.getInstance(ActAppModule.this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.CUSTOM_ADDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("sessionrespo-", response);
                        int position = mViewPager.getCurrentItem();
                        if (response != null && !response.isEmpty()) {
                            //Log.d("CUSTOMADDS",response);
                            CUtils.saveStringData(ActAppModule.this, "CUSTOMADDS", response);
                            ArrayList<AdData> sliderList = parseData(response);
                            if (sliderList != null && sliderList.size() > 0) {
                                setSliderdata(sliderList, position);
                            }
                        } else {
                            CUtils.saveStringData(ActAppModule.this, "CUSTOMADDS", "");
                            Fragment fragment = ((ViewPagerAdapter) mViewPager.getAdapter()).getFragment(position);
                            if (position == 0) {

                                ((KundliModules_Frag) (fragment)).setTopAdd(null);
                                ((KundliModules_Frag) (fragment)).setBottomAd(null);

                            }

                        }
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ParseError: " + error.getMessage());
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("adscreenname", "1");
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActAppModule.this));
                headers.put("languagecode", String.valueOf(LANGUAGE_CODE));
                headers.put("versioncode", String.valueOf(BuildConfig.VERSION_CODE));
                headers.put(CGlobalVariables.PACKAGE_NAME, BuildConfig.APPLICATION_ID);
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActAppModule.this)));
                headers.put(DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(ActAppModule.this));
                headers.put("countrycode", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActAppModule.this));
                headers.put(PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(ActAppModule.this));
                headers.put(USERID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(ActAppModule.this));

                int isfreecallavailable = 0;
                String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(ActAppModule.this);
                //Log.d("TestOfferType", "offerType2="+offerType);
                if (offerType == null) {
                    offerType = "";
                }
                try {
                    if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {
                        isfreecallavailable = 2;
                    } else {
                        if (!TextUtils.isEmpty(offerType) && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                            isfreecallavailable = 1;
                        }
                    }
                } catch (Exception e) {
                    //
                }
                headers.put("isfreecallavailable", String.valueOf(isfreecallavailable));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OFFER_TYPE, offerType);
                headers.put(APP_THEME, CUtils.getAppThemeMode(ActAppModule.this));

                //Log.d("TestOfferType", "headers="+headers);
                return headers;
            }

        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public String getVisibility() {

        return IsShowBanner;
    }

    /*Parse Recieved Gson Data*/
    public ArrayList<AdData> parseData(String response) {
        try {

            adList = new Gson().fromJson(response, new TypeToken<ArrayList<AdData>>() {
            }.getType());

        } catch (Exception e) {

            //Log.i("Exception generate", e.getMessage());
        }
        return adList;

    }

    /*@author Swatantra
     * Set data on each kundli fragment based on the visibility
     * */
    private void setSliderdata(ArrayList<AdData> adList, int position) {
        AdData topKundliAd = null, botttomKundliAd = null, topReportAd = null, bottomReportAd = null;
        AdData topPanchangAd = null, botttomPanchangAd = null, topHoroAd = null, bottomHoroAd = null;
        AdData topAstroShopAd = null, botttomAstroShopAd = null;
        AdData topYearAd = null, botttomYearAd = null;
        AdData topVideoAd = null, botttomVideoAd = null;
        topKundliAd = CUtils.getSlotData(adList, "0");
        botttomKundliAd = CUtils.getSlotData(adList, "1");

        topVideoAd = CUtils.getSlotData(adList, "29");
        botttomVideoAd = CUtils.getSlotData(adList, "30");

        topYearAd = CUtils.getSlotData(adList, "26");
        botttomYearAd = CUtils.getSlotData(adList, "27");

        topReportAd = CUtils.getSlotData(adList, "2");
        bottomReportAd = CUtils.getSlotData(adList, "3");

        topPanchangAd = CUtils.getSlotData(adList, "4");
        botttomPanchangAd = CUtils.getSlotData(adList, "5");

        topHoroAd = CUtils.getSlotData(adList, "6");
        bottomHoroAd = CUtils.getSlotData(adList, "7");

        topAstroShopAd = CUtils.getSlotData(adList, "8");
        botttomAstroShopAd = CUtils.getSlotData(adList, "9");

        Fragment fragment = ((ViewPagerAdapter) mViewPager.getAdapter()).getFragment(position);

        if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            if (position == 0) {
                ((KundliModules_Frag) (fragment)).setBottomAd(botttomKundliAd);
                ((KundliModules_Frag) (fragment)).setTopAdd(topKundliAd);
            } else if (position == 2) {
                ((Astroshop_Frag) (fragment)).setTopAdd(topAstroShopAd);
                ((Astroshop_Frag) (fragment)).setBottomAd((botttomAstroShopAd));
            } else if (position == 4) {
                ((Reports_frag) (fragment)).setTopAdd(topReportAd);
                ((Reports_frag) (fragment)).setBottomAd(bottomReportAd);
            } else if (position == 5) {
                ((Video_Frag) (fragment)).setBottomAd(botttomVideoAd);
                ((Video_Frag) (fragment)).setTopAdd(topVideoAd);
            } else if (position == 6) {
                ((Panchang_Frag) (fragment)).setTopAdd(topPanchangAd);
                ((Panchang_Frag) (fragment)).setBottomAd(botttomPanchangAd);
            } else if (position == 7) {
                ((Horoscope_Frag) (fragment)).setTopAdd(topHoroAd);
                ((Horoscope_Frag) (fragment)).setBottomAd(bottomHoroAd);
            } else if (position == 1) {
                ((Frag_Year) (fragment)).setBottomAd(botttomYearAd);
                ((Frag_Year) (fragment)).setTopAdd(topYearAd);
            }
        } else {
            if (position == 0) {
                ((KundliModules_Frag) (fragment)).setBottomAd(botttomKundliAd);
                ((KundliModules_Frag) (fragment)).setTopAdd(topKundliAd);
            } else if (position == 1) {
                ((Astroshop_Frag) (fragment)).setTopAdd(topAstroShopAd);
                ((Astroshop_Frag) (fragment)).setBottomAd((botttomAstroShopAd));
            } else if (position == 3) {
                ((Reports_frag) (fragment)).setTopAdd(topReportAd);
                ((Reports_frag) (fragment)).setBottomAd(bottomReportAd);
            } else if (position == 4) {
                ((Video_Frag) (fragment)).setBottomAd(botttomVideoAd);
                ((Video_Frag) (fragment)).setTopAdd(topVideoAd);
            } else if (position == 5) {
                ((Panchang_Frag) (fragment)).setTopAdd(topPanchangAd);
                ((Panchang_Frag) (fragment)).setBottomAd(botttomPanchangAd);
            } else if (position == 6) {
                ((Horoscope_Frag) (fragment)).setTopAdd(topHoroAd);
                ((Horoscope_Frag) (fragment)).setBottomAd(bottomHoroAd);
            }
        }
    }

    /*
    private List<NameValuePair> getNameValuePairs_CustomAdds(String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("adscreenname", "1"));
        nameValuePairs.add(new BasicNameValuePair("key", CUtils.getApplicationSignatureHashCode(this)));
        nameValuePairs.add(new BasicNameValuePair("languagecode", String.valueOf(LANGUAGE_CODE)));
        nameValuePairs.add(new BasicNameValuePair("versioncode", String.valueOf(BuildConfig.VERSION_CODE)));

        return nameValuePairs;

    }*/

    private void sendAnalyticsForUserLoginOrNot() {
        try {
            String userName = CUtils.getUserName(this);
            String labell = "";
            if (!userName.equals("")) {
                CUtils.googleAnalyticSendWitPlayServie(this, User, Login, null);
                labell = "ak_logged_in_user";
            } else {
                CUtils.googleAnalyticSendWitPlayServie(this, User, Logout, null);
                labell = "ak_not_logged_in_user";
            }

            CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.LOGIN_STATUS, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAnalyticsVartaUserIsLogin() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {
                CUtils.fcmAnalyticsEvents("varta_logged_in_user", CGlobalVariables.LOGIN_STATUS, "");
            } else {
                CUtils.fcmAnalyticsEvents("varta_not_logged_in_user", CGlobalVariables.LOGIN_STATUS, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
//                    openLiveStramingScreen();
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

    /**
     * This method is used to get free question, if user first tim install the App.
     */
    private void getFreeQuestionIfUserFirstTimeInstallTheApp() {
        try {
            //  Log.d("testApiRes","Url is "+CGlobalVariables.GET_FREE_CHAT_CALL_INSTALL_URL);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GET_FREE_CHAT_CALL_INSTALL_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // No work
                            //Log.d("eligibleTestApiRes",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = "";
                                if (jsonObject.has("status"))
                                    status = jsonObject.getString("status");
                                if (status.equals("1")) {
                                    String title = "", description = "";
                                    if (jsonObject.has("title")) {
                                        title = jsonObject.getString("title");
                                    }
                                    if (jsonObject.has("description")) {
                                        description = jsonObject.getString("description");
                                    }

                                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                                        //callLocalNotification(title, description);
                                        CreateCustomLocalNotification notification = new CreateCustomLocalNotification(ActAppModule.this);
                                        notification.showLocalNotification(title, description, !description.contains("49"));
                                    }
                                    //callLocalNotification();
                                    CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRST_TIME_INSTALL_NOTIFICATION, CGlobalVariables.NOTIFICATION_RECEIVED, "");
                                }

                                CUtils.saveBooleanData(ActAppModule.this, CGlobalVariables.GET_FREE_QUESTION_AT_USER_FIRST_INSTALL_KEY, false);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }

            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }


                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getParams() {

                    //useremailid, key
                    String emailId = UserEmailFetcher.getEmail(ActAppModule.this);
                    //String emailId="chandantestliveapi@gmail.com";
                    String androidId = CUtils.getMyAndroidId(ActAppModule.this);

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("key", CUtils.getApplicationSignatureHashCode(ActAppModule.this));
                    headers.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
                    headers.put("androidid", androidId);
                    headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
                    headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(ActAppModule.this));
                    headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(ActAppModule.this));
                    headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
                    headers.put("language", "" + LANGUAGE_CODE);
                    String astrosageUserID = CUtils.getUserName(ActAppModule.this);
                    if (astrosageUserID == null) {
                        astrosageUserID = "";
                    }
                    boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this);
                    try {
                        if (isLogin) {
                            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(ActAppModule.this));
                            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActAppModule.this));
                        } else {
                            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, "");
                            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
                        }
                    } catch (Exception e) {
                    }
                    headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(astrosageUserID));
                    //Log.e("", headers.toString());
                    //Log.d("testApiRes","Headers is "+headers.toString());

                    return headers;
                }

            };

            // Add the request to the RequestQueue.
            //Log.e("tag", "API HIT HERE");
            int socketTimeout = 60000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        } catch (Exception ex) {
            //
        }
    }

    private void callLocalNotification() {
        try {
            Bitmap icon = BitmapFactory.decodeResource(ActAppModule.this.getResources(), com.libojassoft.android.R.mipmap.icon);
            Intent intent = new Intent(ActAppModule.this, ActAppModule.class);
            intent.putExtra(CGlobalVariables.NOTIFICATION_TYPE, CGlobalVariables.INSTALL_FREE_NOTIFICATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls + "/talk-to-astrologers"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ActAppModule.this, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                    .setContentTitle(getString(R.string.local_noti_free_chat_title))
                    .setContentText(getString(R.string.local_noti_free_chat_desc))
                    .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                    .setLargeIcon(icon)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(getString(R.string.local_noti_free_chat_desc)));

            NotificationManager notificationManager = createNotificationChannel();
            notificationManager.notify(LibCUtils.getRandomNumber(), builder.build());
            saveNotificationInLocalDb(getString(R.string.local_noti_free_chat_title), getString(R.string.local_noti_free_chat_desc), com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls + "/talk-to-astrologers", "");

        } catch (Exception e) {
            //
        }
    }


    // To open Activity on respected screen on search
    public void openActivity(String key) {
        try {
        CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_INAPP_SEARCH, null);

        searchView.clearFocus();
        Log.i("key>>>", key);
        if (Arrays.asList(this.keys[70]).contains(key)) {
            callAstroShopModuleActivities(BRIHATHOROSCOPE);
        } else if (Arrays.asList(this.keys[71]).contains(key)) {
            callAstroShopModuleActivities(GEMSTONE);
        } else if (Arrays.asList(this.keys[72]).contains(key)) {
            callAstroShopModuleActivities(YANTRAS);
        } else if (Arrays.asList(this.keys[73]).contains(key)) {
            callAstroShopModuleActivities(RUDRAKSHA);
        } else if (Arrays.asList(this.keys[74]).contains(key)) {
            callAstroShopModuleActivities(MALA);
        } else if (Arrays.asList(this.keys[75]).contains(key)) {
            callAstroShopModuleActivities(NAVAGRAH);
        } else if (Arrays.asList(this.keys[76]).contains(key)) {
            callAstroShopModuleActivities(JADI);
        } else if (Arrays.asList(this.keys[77]).contains(key)) {
            callAstroShopModuleActivities(SERVICE);
        } else if (Arrays.asList(this.keys[78]).contains(key)) {
            callAstroShopModuleActivities(ASTROLOGER);
        } else if (Arrays.asList(this.keys[85]).contains(key)) {
            callAstroShopModuleActivities(FENGSHUI);
        } else if (Arrays.asList(this.keys[86]).contains(key)) {
            callAstroShopModuleActivities(MISC);
        } else if (Arrays.asList(this.keys[9]).contains(key)) {
            navigateToAstroshopFrag();
        } else if (Arrays.asList(this.keys[10]).contains(key)) {
            Intent intent = new Intent(this, ActAstrosageTV.class);
            startActivity(intent);
        } else if (Arrays.asList(this.keys[11]).contains(key)) {
            Intent intent = new Intent(this,
                    ActShowOjasSoftArticlesWithTabs.class);
            intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                    CGlobalVariables.MODULE_ASTROSAGE_ARTICLES);
            startActivity(intent);
        } else if (Arrays.asList(this.keys[12]).contains(key)) {
            Intent intent = new Intent(this, ActLearnAstrology.class);
            startActivity(intent);
        } else if (key.contains("###")) {

            String[] separated = key.split("###");
            if(searchedAstroList != null && searchedAstroList.size() > 0) {
                for (int i = 0; i < searchedAstroList.size(); i++) {
                    AstrologerDetailBean astrologerDetail = searchedAstroList.get(i);
                    if(astrologerDetail == null)continue;
                    String urlText = astrologerDetail.getUrlText();
                    if(urlText == null) urlText = "";
                    if (separated[1].equalsIgnoreCase(urlText)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("phoneNumber", astrologerDetail.getPhoneNumber());
                        bundle.putString("urlText", urlText);
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_HOME_ASTRO_DETAIL_CLICK,
                                com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        Intent intent = new Intent(ActAppModule.this, AstrologerDescriptionActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //  finish();
                    }
                }
            }
        } else {
            HashMap<String, ActivityDetailWithExtraData> hashMap = getActivityDetail();
            ActivityDetailWithExtraData activityDetailWithExtraData = hashMap.get(key);
            ArrayList<String> keys = activityDetailWithExtraData.getKeys();
            ArrayList values = activityDetailWithExtraData.getValues();
            Intent intent = new Intent(this, activityDetailWithExtraData.getActivity());
            if (keys != null && keys.size() > 0) {
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i) instanceof Integer) {
                        intent.putExtra(keys.get(i), (int) values.get(i));
                    } else if (values.get(i) instanceof String) {
                        intent.putExtra(keys.get(i), (String) values.get(i));
                    } else if (values.get(i) instanceof Calendar) {
                        intent.putExtra(keys.get(i), (Calendar) values.get(i));
                    }
                }
            }
            startActivity(intent);
        }
        searchView.closeSearch();
        }catch (Exception e){

        }
    }

    //Map search key with respected screen
    public HashMap<String, ActivityDetailWithExtraData> getActivityDetail() {
        ArrayList<String> keys;
        ArrayList values;
        ActivityDetailWithExtraData activityDetailWithExtraData;
        HashMap<String, ActivityDetailWithExtraData> hashMap = new HashMap();
        for (int i = 0; i < 9; i++) {

            addKundliTabData(hashMap, this.keys[0][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC, null, HomeInputScreen.class);
            addKundliTabData(hashMap, this.keys[1][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_MATCHING, null, HomeMatchMakingInputScreen.class);
            addHoroscopeTabData(hashMap, this.keys[2][i].trim(), 0);
            addKundliTabData(hashMap, this.keys[3][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_PREDICTION, null, HomeInputScreen.class);
            addPanchangTabData(hashMap, this.keys[4][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_PANCHANG, InputPanchangActivity.class);
            addKundliTabData(hashMap, this.keys[5][i].trim(), CGlobalVariables.ask_A_Question_Data, -1, CGlobalVariables.ask_A_Question_Android, ActAskQuestion.class);
            addKundliTabData(hashMap, this.keys[6][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_KP, null, HomeInputScreen.class);
            addKundliTabData(hashMap, this.keys[7][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_LALKITAB, null, HomeInputScreen.class);
            addKundliTabData(hashMap, this.keys[8][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_VARSHAPHAL, null, HomeInputScreen.class);
            addKundliTabData(hashMap, this.keys[9][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, 0, null, ActAstroShopCategories.class);
            addKundliTabData(hashMap, this.keys[10][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, 0, null, ActAstrosageTV.class);
            addKundliTabData(hashMap, this.keys[11][i].trim(), CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ASTROSAGE_ARTICLES, null, ActShowOjasSoftArticlesWithTabs.class);
            addKundliTabData(hashMap, this.keys[12][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, 0, null, ActLearnAstrology.class);
            addKundliTabData(hashMap, this.keys[13][i].trim(), CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_PORUTHAM, null, ActPorutham.class);
            addKundliTabData(hashMap, this.keys[14][i].trim(), CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE, null, ActAstroSageMarriage.class);

            //2019 year data
            addYearLyTabData(hashMap, getResources().getString(R.string.horoscope_url), this.keys[15][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.jupiter_transit_url), this.keys[16][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.saturn_transit_url), this.keys[17][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.loveHoroscope_url), this.keys[18][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.careerHoroscope_url), this.keys[19][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.chineseHoroscope_url), this.keys[20][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.educationHoroscope_url), this.keys[21][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.financeHoroscope_url), this.keys[22][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.lalkitabHoroscope_url), this.keys[23][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.numerology_url), this.keys[24][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.viah_muhurat_url), this.keys[25][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.mundan_muhurat_url), this.keys[26][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.griha_muhurat_url), this.keys[27][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.namkaran_muhurat_url), this.keys[28][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.annaprashan_muhurat_url), this.keys[29][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.karnavedha_muhurat_url), this.keys[30][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.vidyarambh_muhurat_url), this.keys[31][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.ketu_transit_url), this.keys[32][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.rahu_transit_url), this.keys[33][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.lunar_eclipse_url), this.keys[34][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.solar_eclipse_url), this.keys[35][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.planets_in_retrograde_url), this.keys[36][i].trim());
            addYearLyTabData(hashMap, getResources().getString(R.string.mercury_retrograde_url), this.keys[37][i].trim());

            //Add reporttab data

            addReportTabData(hashMap, this.keys[38][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_LIFE_PREDICTIONS);
            addReportTabData(hashMap, this.keys[39][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_MONTHLY_PREDICTIONS);
            addReportTabData(hashMap, this.keys[40][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_DAILY_PREDICTIONS);
            addReportTabData(hashMap, this.keys[41][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_SADE_SATI);
            addReportTabData(hashMap, this.keys[42][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_ASCENDANT_PREDICTION);
            addReportTabData(hashMap, this.keys[43][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_VARSHPHAL);
            addReportTabData(hashMap, this.keys[44][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_MANGAL_DOSH);
            addReportTabData(hashMap, this.keys[45][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA);
            addReportTabData(hashMap, this.keys[46][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_MOONWESTERN);
            addReportTabData(hashMap, this.keys[47][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_LALKITAB_DEBT);
            addReportTabData(hashMap, this.keys[48][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_LALKITAB_TEVA_TYPE);
            addReportTabData(hashMap, this.keys[49][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_BABYNAME);
            addReportTabData(hashMap, this.keys[50][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_LAL_KITAB_REMEDIES);
            addReportTabData(hashMap, this.keys[51][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_PLANET_CONSIDERATION);
            addReportTabData(hashMap, this.keys[52][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_GEMSTONE_REPORT);
            addReportTabData(hashMap, this.keys[53][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_TRANSIT_TODAY);
            addReportTabData(hashMap, this.keys[54][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_MAHADASHA_PHALA);
            addReportTabData(hashMap, this.keys[55][i].trim(), CGlobalVariables.SUB_MODULE_PREDICTION_NAKSHATRA_REPORT);

            //Add panchang tab data

            //addPanchangTabData(hashMap, "Daily Panchang", CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_PANCHANG, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[56][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_INDIAN_CALENDER, ActMontlyCalendar.class);
            addPanchangTabData(hashMap, this.keys[57][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_HINDU_CALENDER, ActHinduCalender.class);
            addPanchangTabData(hashMap, this.keys[58][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_YEARLY_VART, ActYearlyVrat.class);
            addPanchangTabData(hashMap, this.keys[59][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_INDIAN_CALENDER, ActIndianCalender.class);
            addPanchangTabData(hashMap, this.keys[60][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_HORA, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[61][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[62][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_DO_GHATI_MUHURT, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[63][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_RAHUKAAL, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[64][i].trim(), CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_CALENDAR, ActCalendar.class);

            //Add horoscope tab data
            addHoroscopeTabData(hashMap, this.keys[65][i].trim(), 0);
            addHoroscopeTabData(hashMap, this.keys[66][i].trim(), 2);
            addHoroscopeTabData(hashMap, this.keys[67][i].trim(), 3);
            addHoroscopeTabData(hashMap, this.keys[68][i].trim(), 4);
            addHoroscopeTabData(hashMap, this.keys[69][i].trim(), 5);

            addPanchangTabData(hashMap, this.keys[79][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_PANCHAK, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[80][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BHADRA, InputPanchangActivity.class);
            addPanchangTabData(hashMap, this.keys[81][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_MUHURAT, InputPanchangActivity.class);
            addKundliTabData(hashMap, this.keys[82][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_ASTROSAGE_NUMROLOGY, null, NumerologyCalculatorInputActivity.class);
            addKundliTabData(hashMap, this.keys[83][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_LAGNA, null, InputPanchangActivity.class);
            //addPanchangTabData(hashMap, this.keys[83][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_LAGNA, InputPanchangActivity.class);
            addKundliTabData(hashMap, this.keys[84][i].trim(), CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_DAILY_NOTES, null, NotesActivity.class);
        }
        return hashMap;
    }

    //To add kundli module tab item in search
    private void addKundliTabData(HashMap<String, ActivityDetailWithExtraData> hashMap, String hashMapKey, String key, int value1, String value2, Class className) {
        yearlyTabKeys = new ArrayList<String>();
        yearlyTabvalues = new ArrayList();
        yearlyTabKeys.add(key);
        if (value2 != null) {
            yearlyTabvalues.add(value2);
        } else {
            yearlyTabvalues.add(value1);
        }

        activityDetailWithExtraData = new ActivityDetailWithExtraData(className, yearlyTabKeys, yearlyTabvalues);
        hashMap.put(hashMapKey, activityDetailWithExtraData);
    }

    //To add yearly tab item in search
    private void addYearLyTabData(HashMap<String, ActivityDetailWithExtraData> hashMap, String url, String title) {
        yearlyTabKeys = new ArrayList<String>();
        yearlyTabvalues = new ArrayList();
        yearlyTabKeys.add("url");
        yearlyTabKeys.add("title");

        yearlyTabvalues.add(url);
        yearlyTabvalues.add(title);
        activityDetailWithExtraData = new ActivityDetailWithExtraData(ActYearly.class, yearlyTabKeys, yearlyTabvalues);
        hashMap.put(title, activityDetailWithExtraData);
    }

    //To add report tab item in search
    private void addReportTabData(HashMap<String, ActivityDetailWithExtraData> hashMap, String key, int subModule) {
        yearlyTabKeys = new ArrayList<String>();
        yearlyTabvalues = new ArrayList();
        yearlyTabKeys.add(CGlobalVariables.MODULE_TYPE_KEY);
        yearlyTabKeys.add(CGlobalVariables.SUB_MODULE_TYPE_KEY);

        yearlyTabvalues.add(CGlobalVariables.MODULE_PREDICTION);
        yearlyTabvalues.add(subModule);
        activityDetailWithExtraData = new ActivityDetailWithExtraData(HomeInputScreen.class, yearlyTabKeys, yearlyTabvalues);
        hashMap.put(key, activityDetailWithExtraData);
    }

    //To add panchang tab item in search
    private void addPanchangTabData(HashMap<String, ActivityDetailWithExtraData> hashMap, String key, String moduleTypeKey, int moduleTypeVal, Class className) {
        yearlyTabKeys = new ArrayList<String>();
        yearlyTabvalues = new ArrayList();
        yearlyTabKeys.add(moduleTypeKey);
        yearlyTabKeys.add("date");
        yearlyTabKeys.add("place");

        yearlyTabvalues.add(moduleTypeVal);
        yearlyTabvalues.add(Calendar.getInstance());
        yearlyTabvalues.add("");

        activityDetailWithExtraData = new ActivityDetailWithExtraData(className, yearlyTabKeys, yearlyTabvalues);
        hashMap.put(key, activityDetailWithExtraData);
    }

    //To add horoscope tab item in search
    private void addHoroscopeTabData(HashMap<String, ActivityDetailWithExtraData> hashMap, String key, int moduleTypeVal) {
        yearlyTabKeys = new ArrayList<String>();
        yearlyTabvalues = new ArrayList();
        yearlyTabKeys.add(CGlobalVariables.MODULE_TYPE_KEY);
        yearlyTabKeys.add(CGlobalVariables.SUB_MODULE_TYPE_KEY);

        yearlyTabvalues.add(CGlobalVariables.MODULE_MATCHING);
        yearlyTabvalues.add(moduleTypeVal);

        activityDetailWithExtraData = new ActivityDetailWithExtraData(HoroscopeHomeActivity.class, yearlyTabKeys, yearlyTabvalues);
        hashMap.put(key, activityDetailWithExtraData);
    }

    //To open Astroshop module
    public void callAstroShopModuleActivities(int position) {

        int moduleType = 0;
        if (position == BRIHATHOROSCOPE) {

            CUtils.getUrlLink(brihatHorscopeDeepLinkUrl, ActAppModule.this, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, BrihatHorscopeId, null);
        } else if (position == GEMSTONE) {

            CUtils.getUrlLink(gemStoneDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);
            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, GemstoneId, null);

        } else if (position == YANTRAS) {
            CUtils.getUrlLink(yantraDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, YantrasId, null);

        } else if (position == RUDRAKSHA) {
            CUtils.getUrlLink(rudrakshDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);


            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, RudrakshaId, null);
        } else if (position == MALA) {
            CUtils.getUrlLink(malaDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);
            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, MalaId, null);

        } else if (position == NAVAGRAH) {
            CUtils.getUrlLink(navgrahDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);
            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, NavagrahId, null);

        } else if (position == JADI) {
            CUtils.getUrlLink(jadiDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, JadiId, null);
        } else if (position == SERVICE) {
            moduleType = CGlobalVariables.MODULE_ASTROSHOP_SERVICE;

            Intent i = new Intent(ActAppModule.this, ActAstroShopServices.class);
            i.putExtra(CGlobalVariables.SOUCRE_ACTIVITY, "Home");
            startActivity(i);
            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, null);
        } else if (position == ASTROLOGER) {
            moduleType = CGlobalVariables.MODULE_ASTROSHOP_ASTROLOGER;

            Intent intent = new Intent(ActAppModule.this, ActAstrologer.class);
            startActivity(intent);

            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_ASTROLOGER, null);
        } else if (position == FENGSHUI) {
            CUtils.getUrlLink(fengshuiDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, fengshuiId, null);
        } else if (position == MISC) {
            CUtils.getUrlLink(miscDeeplink, ActAppModule.this, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(ActAppModule.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, miscId, null);
        }
    }

    private void getDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //Log.e("FirebaseDynamicLinks", "" + deepLink);
                        if (deepLink == null) {
                            onIntent(getIntent());
                        } else {
                            Intent resultIntent = new Intent();
                            resultIntent.setAction(Intent.ACTION_VIEW);
                            resultIntent.setData(deepLink);
                            onIntent(resultIntent);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                        onIntent(getIntent());
                    }
                });
    }

    private void openKundaliByVirtualUrl() {
        try {
            String virtualKundaliUrl = CUtils.getStringData(this, CGlobalVariables.KEY_KUNDALI_VIRTUAL_URL, "");
            //Toast.makeText(this, virtualKundaliUrl, Toast.LENGTH_LONG).show();
            if (!TextUtils.isEmpty(virtualKundaliUrl)) {
                openKundaliByNotification(Uri.parse(virtualKundaliUrl));
                CUtils.saveStringData(this, CGlobalVariables.KEY_KUNDALI_VIRTUAL_URL, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method open the user kundali by notification virtual url
     *
     * @param data
     */
    private void openKundaliByNotification(Uri data) {
        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = new BeanHoroPersonalInfo();
            beanHoroPersonalInfo.setName(data.getQueryParameter("name"));
            beanHoroPersonalInfo.setGender(data.getQueryParameter("gender"));
            int KpHorarayNumver = 0;
            beanHoroPersonalInfo.setHoraryNumber(KpHorarayNumver);
            beanHoroPersonalInfo.setAyanIndex(0);
            String dst = data.getQueryParameter("dst");
            if (dst == null || dst.equalsIgnoreCase("null")) {
                dst = "0";
            }
            beanHoroPersonalInfo.setDST(Integer.parseInt(dst));
            BeanPlace place = CUtils.getUserBirthPlace(data);
            beanHoroPersonalInfo.setPlace(place);
            beanHoroPersonalInfo.setDateTime(CUtils.getUserBirthDateTime(data));
            if (place == null) {
                Intent intent = new Intent(ActAppModule.this, HomeInputScreen.class);
                intent.putExtra(CGlobalVariables.BIRTH_DETAILS_KEY, beanHoroPersonalInfo);
                startActivity(intent);
            } else {
                CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, false, ActAppModule.this, regularTypeface, CGlobalVariables.MODULE_BASIC, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
                kundli.calculate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("actionOnIntent", "Exception = " + e.toString());
        }
    }

//    private void getWalletPriceData() {
//
//        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActAppModule.this)) {
//            //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), DashBoardActivity.this);
//        } else {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GET_WALLET_PRICE_URL,
//                    ActAppModule.this, false, getParamsWallet(), 1).getMyStringRequest();
//            queue.add(stringRequest);
//        }
//    }

    public Map<String, String> getParamsWallet() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActAppModule.this));
        //Log.e("LoadMore params ", headers.toString());
        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.d("LoginFlow", " varta: response=" + response);
        hideProgressBar();
        if (method == 1) {
            try {
                parseJsonDataAndUpdateUi(response);
            } catch (Exception e) {
                clearList();
            }
        } else if (method == NEXT_OFFER_API_RESPONSE) {
            // handleNextRechargeApiResponse(response);

        } else if (method == GET_FIREBASE_AUTH_TOKEN) {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                if (jsonObject.has("status")) {
//                    String status = jsonObject.getString("status");
//                    if (status.equals("1")) {
//                        String authToken = jsonObject.getString("token");
//                        firebaseSignIn(authToken);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else if (method == 2) { //wallet recharge
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("msg");
                //Toast.makeText(BaseActivity.this, response, Toast.LENGTH_LONG).show();
                if (status.equals("1") || status.equals("2")) {
                    try {
                        //com.ojassoft.astrosage.varta.utils.CUtils.clearWalletApiCache(mainQueue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PaymentSucessfulDialog dialog = new PaymentSucessfulDialog(amount);
                    dialog.show(getSupportFragmentManager(), "PaymentSucessfulDialog");
                } else {
                    PaymentFailDialog dialog = new PaymentFailDialog();
                    dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
                }

            } catch (Exception e) {
                //Log.e("Exception>>", e.getMessage());
            }
        } else if (method == 3) { //astrosage login with varta id
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                    handleAstroSageLogin(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (method == END_CHAT_VALUE) {
//            hideProgressBar();
//            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
//            //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
//            try {
//                if (response != null && response.length() > 0) {
//                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.chatTimerTime = 0;
//                    AstrosageKundliApplication.chatTimerRemainingTime = 0;
//                    AstrosageKundliApplication.selectedAstrologerDetailBean = null;
//                    AstrosageKundliApplication.chatJsonObject = "";
//                    AstrosageKundliApplication.channelIdTempStore = "";
//                    com.ojassoft.astrosage.varta.utils.CUtils.saveAstrologerIDAndChannelID(ActAppModule.this, "", "");
//                    if (AstrosageKundliApplication.currentConsultType != null) {
//                        if (AstrosageKundliApplication.currentConsultType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TEXT)) {
//                            stopService(new Intent(ActAppModule.this, AstroAcceptRejectService.class));
//                        } else if (AstrosageKundliApplication.currentConsultType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AUDIO_CALL_TEXT)) {
//                            stopService(new Intent(ActAppModule.this, AgoraCallInitiateService.class));
//                        } else if (AstrosageKundliApplication.currentConsultType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIDEO_CALL_TEXT)) {
//                            stopService(new Intent(ActAppModule.this, AgoraCallInitiateService.class));
//                        }
//                    }
//                    chatInitiateInfoLayout.setVisibility(View.GONE);
//                }
//            } catch (Exception e) {
//            }

        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.d("LoginFlow", " varta: error=" + error);
        hideProgressBar();
    }

    private void handleAstroSageLogin(JSONObject jsonObject) {
        try {
            //Log.d("LoginFlow", " handleAstroSageLogin()1");
            JSONArray jsonArray = jsonObject.getJSONArray("as");
            JSONObject respObj = jsonArray.getJSONObject(0);

            HashMap<String, String> jsonObjHash = com.ojassoft.astrosage.utils.CUtils.parseLoginSignupJson(respObj);
            if (jsonObjHash.size() > 0) {
                String _userId = "", _pwd = "";
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                    _userId = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
                }
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD)) {
                    _pwd = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD);
                }
                com.ojassoft.astrosage.varta.utils.CUtils.saveInformation(ActAppModule.this, _userId, _pwd, jsonObjHash);

                setUserLoginDetails(_userId, _pwd);
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()2");
        } catch (Exception e) {
            //
        }
    }

    public void isUserLogin(String whichScreen) {
        boolean isLogin = false;
        isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this);
        if (!isLogin) {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_NAV_SIGNUP,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(ActAppModule.this, LoginSignUpActivity.class);
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else if (whichScreen.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RECHARGE_SCRREN)) {
            openWalletScreen("act_app_module");
        } else {

            // SAN bottom History changes

            /*
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_NAV_RECHARGE,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(ActAppModule.this, MyAccountActivity.class);
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "ActAppModule");
            startActivity(intent);
            */

            /*com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_MY_ACCOUNT,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");*/

            Intent cunsultationIntent = new Intent(ActAppModule.this, ConsultantHistoryActivity.class);
            cunsultationIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "ActAppModule");
            startActivity(cunsultationIntent);


        }
    }

    public void callSnakbar(String msg) {
        CUtils.showSnackbar(cordinatorLay, msg, ActAppModule.this);
    }

    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(ActAppModule.this, robotRegularTypeface);
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToConsultTab(int filterType) {
        /*if (LANGUAGE_CODE == CGlobalVariables.HINDI || LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            mViewPager.setCurrentItem(3);
        }else {
            mViewPager.setCurrentItem(2);
        }*/
        if (filterType == FILTER_TYPE_CALL) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_AK_BOTTOM_BAR_CALL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            CUtils.createSession(this, CGlobalVariables.AK_HOME_BOTTOM_BAR_CALL_PARTNER_ID);
        } else if (filterType == FILTER_TYPE_CHAT) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_AK_BOTTOM_BAR_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            CUtils.createSession(this, CGlobalVariables.AK_HOME_BOTTOM_BAR_CHAT_PARTNER_ID);
        }
        com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(filterType, this);
        /*com.ojassoft.astrosage.varta.utils.CUtils.astroListFilterType = filterType;
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.putExtra(KEY_FILTER_TYPE, filterType);
        startActivity(intent);*/
    }

    private void startPrefatchAstroDataService() {
        try {
            Intent intentService = new Intent(this, PreFetchAstroDataservice.class);
            startService(intentService);
        } catch (Exception e) {
            //
        }
    }

    private void startPrefatchLiveAstroDataService() {
        try {
            boolean liveAstroEnabledForAstrosageHomeScreen = CUtils.getBooleanData(ActAppModule.this, CGlobalVariables.liveAstrologerEnabledForAstrosageHomeScreen, false);
            //Log.d("testTagManager",liveAstroEnabledForAstrosageHomeScreen+"");
            if (liveAstroEnabledForAstrosageHomeScreen) { //show data according to tagmanager
                Intent intentService = new Intent(this, PreFetchLiveAstroDataservice.class);
                intentService.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_SOURCE, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(ActAppModule.this));
                startService(intentService);
            }

        } catch (Exception e) {
            //
        }
    }

    private void startPrefetchHistoryDataService() {
        try {
            boolean isLogin;
            isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this);
            if (isLogin) {
                Intent intentService = new Intent(this, PrefetchHistoryDataService.class);
                intentService.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_SOURCE, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(ActAppModule.this));
                startService(intentService);
            } else {
                Log.e("PrefetchHistoryData", "isLogin: " + isLogin);
            }
        } catch (Exception e) {
            Log.e("PrefetchHistoryData", "ex: " + e);
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
//    private void toastNeedPermissions() {
//        callSnakbar(getString(R.string.need_necessary_permissions));
//    }

    private void launchVartaLiveStreaming() {
        /*if (manager == null) return;
        if (manager.getInstalledModules().contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MODULE_VARTA_LIVE)) {
            Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_LIVE_ACTIVITY);
            intent.setPackage(BuildConfig.APPLICATION_ID);
            startActivity(intent);
        } else {
            installVartaLiveStreaming();
        }*/

        com.ojassoft.astrosage.varta.utils.CUtils.openLiveActivity(this);
    }

 /*   public void installVartaLiveStreaming() {
        //downloadingState = downloadingState+ " installVartaLiveStreaming()";
        //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
        if (manager == null) return;
        SplitInstallRequest request = SplitInstallRequest
                .newBuilder()
                .addModule(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MODULE_VARTA_LIVE)
                .build();

        manager.startInstall(request)
                .addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer sessionId) {
                        //downloadingState = downloadingState+" showInstallLiveProgressBar()";
                        //CUtils.saveStringData(ActAppModule.this,"downloadingState",downloadingState);
                        showInstallLiveProgressBar();
                    }
                })
                .addOnFailureListener(new com.google.android.play.core.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        callSnakbar("Live content installation failed. Error code: " + exception);
                    }
                });
    }*/

    public void showInstallLiveProgressBar() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (installLiveProgressDialog == null) {
                        installLiveProgressDialog = new ProgressDialog(ActAppModule.this);
                    }

                    installLiveProgressDialog.setMessage(getResources().getString(R.string.downloading_live_content));
                    installLiveProgressDialog.setIndeterminate(true);
                    installLiveProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    installLiveProgressDialog.setCancelable(false);
                    installLiveProgressDialog.show();
                }
            });
        } catch (Exception e) {
            //
        }
    }

    public void hideInstallLiveProgressBar() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (installLiveProgressDialog != null && installLiveProgressDialog.isShowing()) {
                        installLiveProgressDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            //
        }
    }

    public void updateInstallLiveProgressBar(int totalBytes, int progress) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (totalBytes <= 0 || progress <= 0) {
                        return;
                    }
                    if (installLiveProgressDialog != null) {
                        installLiveProgressDialog.setIndeterminate(false);
                        installLiveProgressDialog.setMax(totalBytes);
                        installLiveProgressDialog.setProgress(progress);
                    }
                }
            });
        } catch (Exception e) {
            //
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

    public void openWalletScreen(String openFrom) {
        if (openFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)) {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_app_dashboard_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "HDREC");
        } else if (openFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)) {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_app_dashboard_low_balance_subscribe_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "HSREC");

        } else {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_app_dashboard_low_balance_page_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "HPREC");
        }
        Intent intent = new Intent(ActAppModule.this, WalletActivity.class);
        intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "ActAppModule");
        startActivity(intent);


        //com.ojassoft.astrosage.varta.utils.CUtils.openFirstTimeProfileActivity(this, "aicall", BACK_FROM_PROFILE_CHAT_DIALOG);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response, int requestCode) {
        if (response.body() != null) {
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
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    abstract class MyMenuItemStuffListener implements View.OnClickListener {
        private String hint;
        private final View view;

        MyMenuItemStuffListener(View view) {
            this.view = view;
            view.setOnClickListener(this);
        }

        @Override
        public abstract void onClick(View v);
    }

    private void openLiveScreen(String urlText) {
        try {

            Fragment fragment = ((ViewPagerAdapter) mViewPager.getAdapter()).getFragment(0);
            KundliModules_Frag kundliModulesFrag = ((KundliModules_Frag) (fragment));
            if (kundliModulesFrag != null) {
                kundliModulesFrag.getLiveAstrologerModelAndJoinLiveSession(urlText);
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fragment fragment = ((ViewPagerAdapter) mViewPager.getAdapter()).getFragment(0);
                    KundliModules_Frag kundliModulesFrag = ((KundliModules_Frag) (fragment));
                    if (kundliModulesFrag != null) {
                        kundliModulesFrag.getLiveAstrologerModelAndJoinLiveSession(urlText);
                    }
                }
            }, 200);
        } catch (Exception e) {

        }
    }

    /**
     * This method handles the ads link and install referral link
     */
    private void handleAdsLink() {
        try {
            // if facebook-adlink contains varta then open varta chat directly and reset this prefrence
            String fbAdLink = CUtils.getStringData(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FB_AD_LINK, "");
            Log.e("TestFBLink", "handleAdsLink: fbAdLink " + fbAdLink);
            if (!TextUtils.isEmpty(fbAdLink)) {
                if (fbAdLink.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_LIVE_ASTRO)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FB_DEEPLINK_OPEN_VARTA_LIVE, CGlobalVariables.FIREBASE_EVENT_DEEPLINK_CLICK, "");
                    CUtils.saveStringData(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FB_AD_LINK, "");
                    Intent intent = new Intent(ActAppModule.this, AllLiveAstrologerActivity.class);
                    intent.putExtra("fromLiveIcon", true);
                    startActivity(intent);
                    return;
                } else if (fbAdLink.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_VARTA)) {
                    if (fbAdLink.contains(CGlobalVariables.chat_with_kundli_ai)) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FB_DEEPLINK_OPEN_KUNDLI_AI_CHAT, CGlobalVariables.FIREBASE_EVENT_DEEPLINK_CLICK, "");
                        openKundliAIChatByDeeplink(fbAdLink);
                    } else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FB_DEEPLINK_OPEN_VARTA_CHAT, CGlobalVariables.FIREBASE_EVENT_DEEPLINK_CLICK, "");
                        connectAutoRandomChats(CGlobalVariables.FACEBOOK_FREE_CHAT_TYPE);
                    }
                    CUtils.saveStringData(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FB_AD_LINK, "");
                    return;
                }
            }

            // if install-refferal contains AS_POPUP_BTM then open varta chat directly and reset this prefrence
            String referrerUrl = CUtils.getStringData(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_INSTALL_REFERRER, "");
            Log.e("TestFBLink", "handleAdsLink: referrerUrl1 " + referrerUrl);
            if (referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AS_POPUP_BTM) ||
                    referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_REFERRER_TEXT) ||
                    referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FB_REFERRER_TEXT) ||
                    referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INSTA_REFERRER_TEXT) ||
                    referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ORGANIC_REFERRER_TEXT)) {

                if (referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AS_POPUP_BTM)) {

                    CUtils.fcmAnalyticsEvents(CGlobalVariables.INSTALL_REF_ASPOPUPBTM_OPEN_VARTA_CHAT, CGlobalVariables.FIREBASE_EVENT_INSTALL_REFERRER, "");
                    //switchToConsultTab(FILTER_TYPE_CHAT);
                    switchToConsultTab(FILTER_TYPE_CALL); //redirect to ai list
                } else if (referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_REFERRER_TEXT)) {

                    CUtils.fcmAnalyticsEvents(CGlobalVariables.INSTALL_REF_GOOGLE_OPEN_VARTA_CHAT, CGlobalVariables.FIREBASE_EVENT_INSTALL_REFERRER, "");
                    connectAutoRandomChats(CGlobalVariables.GOOGLE_FREE_CHAT_TYPE);

                } else if (referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FB_REFERRER_TEXT) || referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INSTA_REFERRER_TEXT)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.INSTALL_REF_FB_OPEN_VARTA_CHAT, CGlobalVariables.FIREBASE_EVENT_INSTALL_REFERRER, "");
                    referrerUrl = AESDecryption.decryptFBReferrer(referrerUrl);
                    Log.e("TestFBLink", "handleAdsLink: referrerUrl2 " + referrerUrl);
                    if (referrerUrl.contains(CGlobalVariables.CAMP_INSTALL)) {
                        connectAutoRandomChats(CGlobalVariables.FACEBOOK_INSTALL_FREE_CHAT_TYPE);
                    } else if (referrerUrl.contains(CGlobalVariables.CAMP_KUNDLI_AI)) { // if referrer url contains camp_kundli_ai then open kundli ai chat
                        openKundliAIChatByRefferalUrl(referrerUrl);
                    } else {
                        connectAutoRandomChats(CGlobalVariables.FACEBOOK_FREE_CHAT_TYPE);
                    }
                } else if (referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ORGANIC_REFERRER_TEXT)) {
                    String organicInstallFreeChatType = CUtils.getStringData(ActAppModule.this, CGlobalVariables.ORGANIC_INSTALL_FREE_CHAT_TYPE, "");
                    if (!TextUtils.isEmpty(organicInstallFreeChatType)) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.INSTALL_REF_GOOGLE_ORGANIC_OPEN_VARTA_CHAT, CGlobalVariables.FIREBASE_EVENT_INSTALL_REFERRER, "");
                        connectAutoRandomChats(CGlobalVariables.ORGANIC_INSTALL_FREE_CHAT_TYPE);
                    }
                }

                CUtils.saveStringData(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_INSTALL_REFERRER, "NA");
                return;
            }
        } catch (Exception e) {
            Log.e("TestFBLink", "handleAdsLink() Exception= " + e);
        }
        try {
            //fcm analytics for free chat notification get when app is installed
            String notificationType = getIntent().getStringExtra(CGlobalVariables.NOTIFICATION_TYPE);
            if (notificationType != null) {
                if (notificationType.equals(CGlobalVariables.INSTALL_FREE_NOTIFICATION)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.INSTALL_FREE_NOTIFICATION, CGlobalVariables.FIREBASE_NOTIFICATION_CLICKED_EVENT, "");
                }
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * This method connects the auto random chats based on the type passed.
     * It checks if the user is logged in and if the offer type is free, then it connects to the random chat.
     *
     * @param type The type of free chat to connect to (e.g., GOOGLE_FREE_CHAT_TYPE, FACEBOOK_FREE_CHAT_TYPE, etc.)
     */
    private void connectAutoRandomChats(String type) {
        try {
            // if user logged-in and offertype is free then start random chat automatically

            String offerType = com.ojassoft.astrosage.varta.utils.CUtils.userOfferAfterLogin;
            if (TextUtils.isEmpty(offerType)) {
                offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(this);
            }

            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)
                    && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {

                String newInstallDirectSecondFreeChat = CUtils.getStringData(ActAppModule.this, CGlobalVariables.NEW_INSTALL_DIRECT_SECOND_FREE_CHAT, "");
                if (com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(ActAppModule.this)) {
                    //in case of enabled from server
                    com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected = newInstallDirectSecondFreeChat.equals(ENABLED);
                } else {
                    com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected = true;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleFreeChat(newInstallDirectSecondFreeChat, type);

                    }
                }, 1000);
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * This method handles the free chat connection based on whether it's a second free chat or the first free chat.
     * If it's a second free chat and direct connection is enabled, it connects to the second free chat.
     * Otherwise, it shows a popup for free chat selection.
     * If it's the first free chat, it initiates the appropriate type of free chat (AI chat, AI call, or human chat)
     * based on the provided type parameter(type wiiil be split will hyphen(-) and first value will be used).
     *
     * @param newInstallDirectSecondFreeChat A string indicating if direct connection for second free chat is enabled.
     * @param type                           The type of free chat to connect to (e.g., GOOGLE_FREE_CHAT_TYPE, FACEBOOK_FREE_CHAT_TYPE, etc.)
     */
    private void handleFreeChat(String newInstallDirectSecondFreeChat, String type) {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.isSecondFreeChat(ActAppModule.this)) {
                if (newInstallDirectSecondFreeChat.equals(ENABLED)) {
                    //connect second free chat
                    String secondFreeChatType = CUtils.getStringData(ActAppModule.this, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");
                    com.ojassoft.astrosage.varta.utils.CUtils.isDirectSecondFreeChat(secondFreeChatType, ActAppModule.this);
                } else {
                    //show free chat popup
                    com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected = false;
                }
            } else {

                // connect first free chat
                if (type.equals(CGlobalVariables.GOOGLE_FREE_CHAT_TYPE)) {
                    String googleFreeChatType = CUtils.getStringData(ActAppModule.this, CGlobalVariables.GOOGLE_FREE_CHAT_TYPE, "");
                    String defaultFreeConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringBeforeHyphen(googleFreeChatType);
                    if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_INSTALL_AI_FREE_CHAT, googleFreeChatType);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CALL)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiCall(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_INSTALL_AI_FREE_CALL, googleFreeChatType);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_HUMAN_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_INSTALL_FREE_CHAT, googleFreeChatType);
                    }
                } else if (type.equals(CGlobalVariables.FACEBOOK_FREE_CHAT_TYPE)) {
                    String facebookFreeChatType = CUtils.getStringData(ActAppModule.this, CGlobalVariables.FACEBOOK_FREE_CHAT_TYPE, "");
                    String defaultFreeConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringBeforeHyphen(facebookFreeChatType);
                    if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_OPEN_AI_FREE_CHAT, facebookFreeChatType);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CALL)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiCall(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_OPEN_AI_FREE_CALL, facebookFreeChatType);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_HUMAN_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_OPEN_FREE_CHAT, facebookFreeChatType);
                    }
                } else if (type.equals(CGlobalVariables.ORGANIC_INSTALL_FREE_CHAT_TYPE)) {
                    String organicInstallFreeChatType = CUtils.getStringData(ActAppModule.this, CGlobalVariables.ORGANIC_INSTALL_FREE_CHAT_TYPE, "");
                    String defaultFreeConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringBeforeHyphen(organicInstallFreeChatType);
                    if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ORGANIC_INSTALL_AI_FREE_CHAT, organicInstallFreeChatType);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CALL)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiCall(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ORGANIC_INSTALL_AI_FREE_CALL, organicInstallFreeChatType);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_HUMAN_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ORGANIC_INSTALL_FREE_CHAT, organicInstallFreeChatType);
                    }
                } else if (type.equals(CGlobalVariables.FACEBOOK_INSTALL_FREE_CHAT_TYPE)) {//install campion
                    String typeFacebookInstallFreeChat = CUtils.getStringData(ActAppModule.this, CGlobalVariables.FACEBOOK_INSTALL_FREE_CHAT_TYPE, "");
                    String defaultFreeConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringBeforeHyphen(typeFacebookInstallFreeChat);
                    if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_INSTALL_AI_FREE_CHAT, typeFacebookInstallFreeChat);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_AI_CALL)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiCall(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_INSTALL_AI_FREE_CLL, typeFacebookInstallFreeChat);
                    } else if (defaultFreeConsultType.equals(CGlobalVariables.TYPE_HUMAN_CHAT)) {
                        com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_INSTALL_FREE_CHAT, typeFacebookInstallFreeChat);
                    } else {
                        // if desabled then use previous
                        String facebookFreeChatType = CUtils.getStringData(ActAppModule.this, CGlobalVariables.FACEBOOK_FREE_CHAT_TYPE, "");
                        String defaultFBFreeConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringBeforeHyphen(facebookFreeChatType);
                        if (defaultFBFreeConsultType.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_OPEN_AI_FREE_CHAT, facebookFreeChatType);
                        } else if (defaultFBFreeConsultType.equals(CGlobalVariables.TYPE_AI_CALL)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomAiCall(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_OPEN_AI_FREE_CALL, facebookFreeChatType);
                        } else if (defaultFBFreeConsultType.equals(CGlobalVariables.TYPE_HUMAN_CHAT)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.initiateRandomChat(ActAppModule.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FACEBOOK_OPEN_FREE_CHAT, facebookFreeChatType);
                        }
                    }
                }
            }

        } catch (Exception e) {
            //
        }
    }

    private void getSearchResultFromApi(String text) {
        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActAppModule.this)) {
            //showProgressBar();
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GET_SEARCHED_ASTRO_URL,
                    ActAppModule.this, false, getParams(text), 1).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> getParams(String text) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_API, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(ActAppModule.this));
        headers.put("q", text);
        //Log.d("SearchAstro", "Params=>" + headers);
        return headers;
    }

    private void parseJsonDataAndUpdateUi(String response) {
        boolean isClearList = true;
        //Log.d("SearchAstro", "Params=>" + response);
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.getString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS);
                if (status.equals("1")) {
                    JSONArray jsonArray = jsonObj.getJSONArray("astrologers");
                    if (jsonArray.length() > 0) {
                        clearList();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
                            astrologerDetailBean.setAstroWalletId(object.getString("wi"));
                            astrologerDetailBean.setUrlText(object.getString("urlText"));
                            astrologerDetailBean.setName(object.getString("n"));
                            astrologerDetailBean.setImageFile(object.getString("if"));

                            searchedAstroList.add(astrologerDetailBean);
                            String astro = object.getString("n") + "###" + object.getString("urlText");
                            if (!suggetions.contains(astro)) {
                                suggetions.add(astro);
                            }
                        }
                    }
                    //           searchAstrologerAdapter.getFilter().filter(searchView.toString());
                    searchView.setSuggestions(ActAppModule.this, suggetions);
                    isClearList = false;
                } else if (status.equals("2")) {
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_astrologer_found), Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong_error) + " (" + status + ")", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong_error), Toast.LENGTH_LONG).show();
            }
        }

        if (isClearList) {
            //clearList();
        }
    }

    private void clearList() {
        try {
            if (searchedAstroList != null) {
                searchedAstroList.clear();
            }
            //  searchAstrologerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    private NotificationManager createNotificationChannel() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        return mNotificationManager;
    }

    private void saveNotificationInLocalDb(String tit, String cont, String link, String imgurl) {

        Context context = ActAppModule.this;
        if (context == null) return;
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(cont);
        notificationModel.setTitle(tit);
        notificationModel.setLink(link);
        notificationModel.setNtId("");
        notificationModel.setExtra("");
        notificationModel.setImgUrl("FIRSTCHATFREE");
        notificationModel.setBlogId("");
        notificationModel.setNotificationType(NOTIFICATION_PUSH);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        NotificationDBManager dbManager = new NotificationDBManager(context);
        dbManager.addNotification(notificationModel);
    }


    @Override
    public void clickHome() {
        mViewPager.setCurrentItem(0, true);
    }

    @Override
    public void clickCall() {
        com.ojassoft.astrosage.varta.utils.CUtils.expertiseFilterType = -1;
        switchToConsultTab(FILTER_TYPE_CALL);
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    @Override
    public void clickChat() {
        com.ojassoft.astrosage.varta.utils.CUtils.expertiseFilterType = -1;
        switchToConsultTab(FILTER_TYPE_CHAT);
    }

    public void showOrHideCallChatInitiate() {
        flag = false;
        cross_chat_initiate_view.setEnabled(true);

        if (com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(AstroAcceptRejectService.class) || com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
            AstroLiveDataManager.getAstroAcceptLiveData().observe(this, callChatObserver);
        } else {
            if (callChatObserver != null) {
                AstroLiveDataManager.getAstroAcceptLiveData().removeObserver(callChatObserver);
            }
            chatInitiateInfoLayout.setVisibility(View.GONE);
        }

    }

    public void showOrHideOngoingChat() {
        if (com.ojassoft.astrosage.varta.utils.CUtils.checkServiceRunning(OnGoingChatService.class)) {
            OngoingCallChatManager.getOngoingChatLiveData().observe(this, ongoingChatObserver);
        } else {
            if (ongoingChatObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
            }
            ongoingChatInfoLayout.setVisibility(View.GONE);
        }

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
        String astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
        String astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
        String remenTimeDuration = intent.getStringExtra("time_remaining");
        String callSId = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_CALLS_ID);
        String callDuration = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_CALL_DURATION);
        String astrologerId = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_ID);
        boolean micStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.MIC_MUTE_STATUS;
        //Log.e("testAiCallIssue","micStatus==>>"+micStatus);
        FontUtils.changeFont(this, callAstroName, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        callAstroName.setText(astrologerName.replace("+"," "));
        String remtime = getResources().getString(R.string.time_remaining)+remenTimeDuration;
        callRemenTime.setText(remtime);
        callMicStatus.setActivated(micStatus);
        String callingAstroImage = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
        Glide.with(AstrosageKundliApplication.getAppContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(callAstroProfileImage);

        ongoingAICallLayout.setOnClickListener(view -> {
            Intent activityIntent = new Intent(ActAppModule.this,AIVoiceCallingActivity.class);
            activityIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_RETURN_TO_CALL,true);
            activityIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            activityIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            activityIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_ID, astrologerId);
            activityIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_CALLS_ID, callSId);
            activityIntent.putExtra("time_remaining", remenTimeDuration);
            activityIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_CALL_DURATION, callDuration);
            startActivity(activityIntent);
        });
    }
    ImageView callAstroProfileImage;
    TextView callRemenTime;
    TextView callAstroName;
    ImageView callMicStatus;
    ImageView callEnd;

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
                if(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MIC_MUTE_STATUS){
                    callMicStatus.setActivated(false);
                }else {
                    callMicStatus.setActivated(true);
                }
                Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MIC_ON_OFF_ACTIVITY_ACTION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            });

        }else{
            OngoingAICallData.getOngoingChatLiveData().removeObserver(aicallObserver);
            ongoingAICallLayout.setVisibility(View.GONE);
        }
    }


    private boolean flag;
    View chatInitiateInfoLayout;
    View ongoingChatInfoLayout;
    View ongoingAICallLayout;
    ImageView cross_chat_initiate_view;
    private final Observer<String> callChatObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            try {
                if (s.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTRO_NO_ANSWER) && flag) {
                    flag = false;
                    chatInitiateInfoLayout.setVisibility(View.GONE);
                    String callSource = "";
                    AstrologerDetailBean astroBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
                    if (astroBean != null) {
                        callSource = astroBean.getCallSource();
                        if (callSource == null) callSource = "";
                    }
                    String isFreeHumanRandomChat = s.split("@")[1];
                    if (isFreeHumanRandomChat.equals("true")) {
                        com.ojassoft.astrosage.varta.utils.CUtils.startRandomAIChatAfterAstroNoAnswer(ActAppModule.this);
                    } else if (callSource.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HUMAN_CONTINUE_CHAT_DIALOG)) {
                        //connect chat to AI astro
                        if (AstrosageKundliApplication.lastChatAIAstrologerDetailBean != null) {
                            AstrosageKundliApplication.lastChatAIAstrologerDetailBean.setCallSource(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HUMAN_CONTINUE_CHAT_FALLBACK_TO_SAME_AI);
                            ChatUtils.getInstance(ActAppModule.this).initAIChat(AstrosageKundliApplication.lastChatAIAstrologerDetailBean);
                        }
                    } else {
                        AstroBusyAlertDialog astroBusyAlertDialog = AstroBusyAlertDialog.newInstance(AstrosageKundliApplication.astrologerDetailBeanForBusyDialog, s);
                        astroBusyAlertDialog.show(ActAppModule.this.getSupportFragmentManager(), "astroBusyAlertDialog");
                        // CUtils.showSnackbar(cordinatorLay, getResources().getString(R.string.astrologer_not_answer), ActAppModule.this);
                    }
                } else if (s.equals("-1") && flag) {
                    flag = false;
                    chatInitiateInfoLayout.setVisibility(View.GONE);
                } else {
                    String internationalCharges = "0.0";
                    if (s.contains("###")) {
                        String[] str = s.split("###");
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
                            com.ojassoft.astrosage.varta.utils.CUtils.setChatInitiateLayout(ActAppModule.this, AstrosageKundliApplication.selectedAstrologerDetailBean, leftTime, internationalCharges, remTime);
                            chatInitiateInfoLayout.setVisibility(View.VISIBLE);
                        } else {
                            chatInitiateInfoLayout.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (Exception e) {
                //
            }

        }
    };

    public void chatCompleted(String URL, String channelID, String remarks, String status, String astroId) {
        com.ojassoft.astrosage.varta.utils.CUtils.sendLogDataRequest(astroId, channelID, "DashboardActivity chatCompleted() onChildAdded() remarks=" + remarks);
        com.ojassoft.astrosage.varta.utils.CUtils.cancelNotification(ActAppModule.this);
        com.ojassoft.astrosage.varta.utils.CGlobalVariables.chatTimerTime = 0;
        // CUtils.saveAstrologerIDAndChannelID(DashBoardActivity.this, "", "");
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActAppModule.this)) {
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), ActAppModule.this);
        } else {
            showProgressBar();
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, URL,
//                    ActAppModule.this, false, getChatCompleteParams(channelID, remarks,status,astroId), END_CHAT_VALUE).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            if (URL.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.END_CHAT_URL)) {
                callEndChatApi(channelID, remarks, status, astroId);
            } else {
                callEndCallApi(channelID, remarks, status, astroId);
            }


        }
    }

    private void callEndChatApi(String channelID, String remarks, String status, String astroId) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks, status, astroId), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                if (response.body() != null) {
                    //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
                    try {
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

    private void endCallChatActions() {
        try {
            com.ojassoft.astrosage.varta.utils.CGlobalVariables.chatTimerTime = 0;
            AstrosageKundliApplication.chatTimerRemainingTime = 0;
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            AstrosageKundliApplication.chatJsonObject = "";
            AstrosageKundliApplication.channelIdTempStore = "";
            com.ojassoft.astrosage.varta.utils.CUtils.saveAstrologerIDAndChannelID(ActAppModule.this, "", "");
            if (AstrosageKundliApplication.currentConsultType != null) {
                if (AstrosageKundliApplication.currentConsultType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TEXT)) {
                    stopService(new Intent(ActAppModule.this, AstroAcceptRejectService.class));
                } else if (AstrosageKundliApplication.currentConsultType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AUDIO_CALL_TEXT)) {
                    stopService(new Intent(ActAppModule.this, AgoraCallInitiateService.class));
                } else if (AstrosageKundliApplication.currentConsultType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIDEO_CALL_TEXT)) {
                    stopService(new Intent(ActAppModule.this, AgoraCallInitiateService.class));
                }
            }
            chatInitiateInfoLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            //
        }
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
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                endCallChatActions();
            }
        });
    }


    public Map<String, String> getChatCompleteParams(String channelID, String remarks, String status, String astroId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_END_STATUS = status;
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS, status);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_DURATION, /*"15"*/"00");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.REMARKS, remarks);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(headers);
    }

    private final Observer<Intent> ongoingChatObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            String remTime = intent.getStringExtra("rem_time");
            String CHANNEL_ID = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_USER_CHANNEL);
            String chatJsonObject = intent.getStringExtra("connect_chat_bean");
            String astrologerName = intent.getStringExtra("astrologer_name");
            String astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
            String astrologerId = intent.getStringExtra("astrologer_id");
            String userChatTime = intent.getStringExtra("userChatTime");
            String chatinitiatetype = intent.getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHATINITIATETYPE);
            if (!remTime.equals("00:00:00")) {
                if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                    com.ojassoft.astrosage.varta.utils.CUtils.joinOngoingChatLayoutView(ActAppModule.this, remTime, CHANNEL_ID, chatJsonObject, astrologerName, astrologerProfileUrl, astrologerId, userChatTime, chatinitiatetype);
                    ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                }
            } else {
                ongoingChatInfoLayout.setVisibility(View.GONE);
            }
        }
    };

    private void getNextRechargeFromApi() {
        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActAppModule.this) &&
                com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActAppModule.this)) {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, NEXT_RECHARGE,
//                    ActAppModule.this, false, getNextRechargeParams(), NEXT_OFFER_API_RESPONSE).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);


            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.nextRecharge(getNextRechargeParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String myResponse = response.body().string();
                        if (nextRechargeOfferType.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.REDUCED_PRICE_OFFER)) {
                            AstrosageKundliApplication.status100BgLoginCount = 0;
                            handleNextRechargeApiResponse(myResponse);
                        } else {
                            checkPopUnder(myResponse);
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

    public void showBottomSheet(NextOfferBean nextOfferBean) {
        try {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_OPEN_NEXT_OFFER_RECHARGE_DIALOG_FROM_HOME,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            BottomSheetDialog bottomSheet = new BottomSheetDialog();
            Bundle bndl = new Bundle();
            bndl.putString("message", nextOfferBean.getPromotionalmsg());
            bndl.putString("extra", nextOfferBean.getStatic());
            bndl.putString("amount", nextOfferBean.getOfferamountstring());
            bndl.putString("serviceId", nextOfferBean.getServiceid());
            bottomSheet.setArguments(bndl);
            bottomSheet.show(getSupportFragmentManager(),
                    "ModalBottomSheet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getNextRechargeParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(ActAppModule.this)); //"8860085780"
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(ActAppModule.this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(headers);
    }

    private void handleNextRechargeApiResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("status")) {
                String status = jsonObject.getString("status");

                if (status.equals("1")) {
                    Gson gson = new Gson();
                    NextOfferBean nextOfferBean = gson.fromJson(jsonObject.toString(), NextOfferBean.class);
                    String rateRsStr = nextOfferBean.getRaters();
                    String offerAmountStr = nextOfferBean.getOfferamout();

                    int offerAmount = Integer.parseInt(offerAmountStr);
                    int rateRs = (int) Double.parseDouble(rateRsStr);
                    if (rateRs == offerAmount) {
                        // show 100% cashback dialog
                        OfferRechargeDialog mDialog = new OfferRechargeDialog(rateRs, offerAmount);
                        mDialog.show(getSupportFragmentManager(), "OfferRechargeDialog");
                        AstrosageKundliApplication.isOpenVartaPopup = true;
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OPEN_100_PERCENT_CASHBACK_DIALOG, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    } else {
                        checkPopUnder(response);
                    }
                } else if (status.equals("100")) {
                    if (mReceiverBackgroundLoginService != null && AstrosageKundliApplication.status100BgLoginCount < 2) {
                        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                        startBackgroundLoginService();
                        AstrosageKundliApplication.status100BgLoginCount += 1;

                    }
                } else { // if no popup will show then kundli AI plus plan popup will appear
                    openKundliAIPlusPopup();
                }
            } else { // if no popup will show then kundli AI plus plan popup will appear
                openKundliAIPlusPopup();
            }
        } catch (Exception e) {
            Log.d("newpopunder", "handleNextRechargeApiResponse e: " + e);
        }
    }

    private void checkPopUnder(String myResponse) {
        try {
            if (TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getPrefNextOffer(ActAppModule.this)) || !com.ojassoft.astrosage.varta.utils.CUtils.isSameDay(com.ojassoft.astrosage.varta.utils.CUtils.getPrefNextOffer(ActAppModule.this), com.ojassoft.astrosage.varta.utils.CUtils.getCurrentDateInString())) {
                JSONObject jsonObject = new JSONObject(myResponse);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        NextOfferBean nextOfferBean = gson.fromJson(jsonObject.toString(), NextOfferBean.class);
                        showBottomSheet(nextOfferBean);
                    }else{
                        openKundliAIPlusPopup(); // in any case no popup will appear then kundli AI plus popup will appear
                    }
                } else{
                    openKundliAIPlusPopup();
                }
            } else {
                openKundliAIPlusPopup();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Shows the Kundli AI Plus purchase screen as a pop-up, but only under specific conditions.
     * This method ensures the pop-up is shown only once per session and is suppressed if the app
     * was opened from a specific type of push notification.
     */
    private void openKundliAIPlusPopup() {
        //check if kundli AI popup/ intersticial enabled from tag manager
        if (com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, CGlobalVariables.IS_HOME_SCREEN_KUNDLI_AI_POPUP_SHOWN, false)) {
            // Condition 1: Check if the app was launched from a "Chat with Astrologer" push notification.
            // We do not want to show a purchase pop-up if the user's intent was to start a chat.
            boolean isLaunchedFromChatNotification = !TextUtils.isEmpty(getIntent().getStringExtra(KEY_AI_ASTROLOGER_ID));

            // Condition 2: Check if this pop-up has already been shown during the current app session.
            // This prevents the user from being repeatedly shown the same promotion.
            boolean isPopupAlreadyShown = isKundliAIPlusPopUpShownOnce;
            boolean isTwoMinBreakCompletedForPopUp = CUtils.canShowInterstitial(this);


            if (!PurchaseDhruvPlanActivity.iSAlreadyShownKundliAIPlusPopUp) {
                // The pop-up will only be displayed if it has NOT been shown before AND the app was NOT
                // launched from a chat notification.
                if (!isPopupAlreadyShown && !isLaunchedFromChatNotification && isTwoMinBreakCompletedForPopUp) {
                    //save current time to check two minute break for next auto popUps for kundli AI plus plan upgrade
                    com.ojassoft.astrosage.utils.CUtils.saveLongData(this, com.ojassoft.astrosage.utils.CGlobalVariables.INTERSTITIAL_SESSION_TIME, System.currentTimeMillis());
                    // Log a Firebase Analytics event to track how many times this pop-up is shown.
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(
                            com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_PLUS_PURCHASE_DIALOG_FROM_HOME,
                            com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,
                            ""
                    );

                    // Navigate to the purchase screen for the Kundli AI Plus plan.
                    // The source is marked as 'SOURCE_FROM_HOME_SCREEN_POPUP' for tracking purposes.
                    com.ojassoft.astrosage.varta.utils.CUtils.openPurchaseDhruvPlanActivity(
                            ActAppModule.this,
                            false,
                            false,
                            CGlobalVariables.SOURCE_FROM_HOME_SCREEN_POPUP,
                            false,
                            false
                    );

                    // Set the flag to true to prevent this pop-up from showing again in this session.
                    isKundliAIPlusPopUpShownOnce = true;
                }
            }
        }
    }



    /**
     * mReceiverBackgroundLoginService:-
     * Purpose: Handles background login service responses
     * Functionality:
     * Receives login status updates
     * Triggers next recharge API call on successful login
     * Shows error messages on failed login
     * Unregisters itself after processing
     * Registration: Dynamically registered when needed with CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN
     */
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                getNextRechargeFromApi();
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(navView, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(ActAppModule.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    private void startBackgroundLoginService() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {/**/}
    }


    private void initUpdateManager() {
        try {
            appUpdateManager = AppUpdateManagerFactory.create(this);
            //appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    if (!CUtils.getBooleanData(this, CGlobalVariables.UPDATE_DIALOG_SHOWN, false)) {
                        initUpdateListeners();
                        appUpdateManager.registerListener(updateListener);
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfoTask.getResult(),
                                    AppUpdateType.FLEXIBLE,
                                    this,
                                    UPDATE_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            Log.d("mUpdateLog", "startUpdateFlowForResult: " + e);
                        }
                    }

                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                    UPDATE_DOWNLOAD_STATUS = 1;
                }
            });
            appUpdateInfoTask.addOnFailureListener(e -> {
                //Log.d("mUpdateLog", "appUpdateInfoTask: " + e));
            });
        } catch (Exception e) {

        }
    }

    boolean showingSnakbar;

    private void initUpdateListeners() {
        updateListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADING) {
                if (!showingSnakbar) {
                    showSnakbar(getString(R.string.downloading), Snackbar.LENGTH_INDEFINITE);
                    showingSnakbar = true;
                }
            } else if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
                UPDATE_DOWNLOAD_STATUS = 1;
                CUtils.saveBooleanData(this, CGlobalVariables.UPDATE_DIALOG_SHOWN, true);
            } else if (installState.installStatus() == InstallStatus.FAILED) {
                UPDATE_DOWNLOAD_STATUS = 0;
                initUpdateManager();
            } else if (installState.installStatus() == InstallStatus.INSTALLED) {
                CUtils.saveBooleanData(this, CGlobalVariables.UPDATE_DIALOG_SHOWN, false);
            }
        };
    }

    public void showSnakbar(String string, int time) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), string, time);
        snackbar.setActionTextColor(getColor(R.color.colorPrimary_day_night));
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(getColor(R.color.white));
        snackbar.show();
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.download_completed),
                        Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(getColor(R.color.white));
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(getColor(R.color.colorPrimary_day_night));
        snackbar.show();
    }

    public void showDarkModePopUp() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dark_mode_prompt);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(lp);
        }
        RadioButton darkModeOn = dialog.findViewById(R.id.rb_dark_mode_on);
        FontUtils.changeFont(this, darkModeOn, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        RadioButton darkModeOff = dialog.findViewById(R.id.rb_dark_mode_off);
        FontUtils.changeFont(this, darkModeOff, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        Button okButton = dialog.findViewById(R.id.button_ok);
        FontUtils.changeFont(this, okButton, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        TextView tvNote = dialog.findViewById(R.id.tv_note);
        FontUtils.changeFont(this, tvNote, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        TextView tvHeader = dialog.findViewById(R.id.tv_heading);
        FontUtils.changeFont(this, tvHeader, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkModeOn.setChecked(true);
                darkModeOn.setText(R.string.continue_with_dark_theme);
                darkModeOff.setText(R.string.revert_to_light_theme);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                darkModeOff.setChecked(true);
                darkModeOff.setText(R.string.continue_with_light_theme);
                darkModeOn.setText(R.string.change_to_dark_theme);
                break;

        }
        okButton.setOnClickListener(v -> {
            if (darkModeOn.isChecked()) {
                ((AstrosageKundliApplication) getApplication()).changeAppTheme(CGlobalVariables.DARK_THEME);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.DARK_MODE_THEME, CGlobalVariables.APP_THEME_CHANGE_EVENT, "");
            } else {
                ((AstrosageKundliApplication) getApplication()).changeAppTheme(CGlobalVariables.LIGHT_THEME);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.LIGHT_THEME, CGlobalVariables.APP_THEME_CHANGE_EVENT, "");
            }
            CUtils.saveBooleanData(ActAppModule.this, CGlobalVariables.IS_DARK_MODE_SELECTED, true);
            dialog.dismiss();
        });
        dialog.show();
    }

    /**
     * This method is used to open the Kundli AI chat category from the deeplink URL.
     * It extracts the category ID from the URL and starts the Kundli AI chat by that category.
     *
     * @param urlText The deeplink URL text containing the category information.
     */

    private void openKundliAIChatByDeeplink(String urlText) {
        if (urlText.contains(CGlobalVariables.chat_with_kundli_ai)) {
            try {
                Uri linkData = Uri.parse(urlText);
                String category = null;
                if (linkData != null) {
                    category = linkData.getQueryParameter(CGlobalVariables.KEY_CATEGORY_ID);
                }
                Log.e("TestFBLink", "openKundliAIChatByDeeplink: category " + category);
                int screenId = category != null ? Integer.parseInt(category) : CGlobalVariables.LOVE_CATEGORY_SCREEN_ID; // default -> 175 love category
                startKundliAIChatByCategory(screenId);
            } catch (Exception e) {
                Log.e("TestFBLink", "actionOnIntent:exception  " + e.getMessage());
            }

        }
    }

    /**
     * This method is used to open the Kundli AI chat by referral URL.
     * It extracts the screen ID from the referral URL and starts the Kundli AI chat by that category.
     *
     * @param referrerUrl The referral URL containing the category information.
     */
    private void openKundliAIChatByRefferalUrl(String referrerUrl) {
        try {
            int screenId = CUtils.getKundliAIChatCategoryByReferrerUrl(referrerUrl);
            Log.e("TestFBLink", "openKundliAIChatByRefferalUrl: screenId " + screenId);
            startKundliAIChatByCategory(screenId);
        } catch (Exception e) {
            Log.e("TestFBLink", "actionOnIntent:exception  " + e.getMessage());
        }
    }

    /**
     * This method is used to start Kundli AI chat by category or screenid.
     */
    private void startKundliAIChatByCategory(int screenId) {
        try {
            //getting Category enum with corresponding screen id
            PersonalizedCategoryENUM personalizedCategoryENUM = PersonalizedCategoryENUM.getByScreenId(screenId);
            UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(this);
            if (userProfileData != null && CUtils.isCompleteUserData(userProfileData)) {
                openKundliAIScreen(personalizedCategoryENUM, userProfileData, false);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(CLICKED_CATEGORY_ENUM_KEY, personalizedCategoryENUM.name());
                com.ojassoft.astrosage.varta.utils.CUtils.openProfileForChat(activity, null, HOME_KUNDLI_CHAT, bundle, true, BACK_FROM_PROFILECHATDIALOG);
            }
        } catch (Exception e) {
            Log.e("testNotification", "startKundliAIChatByFacebookDeeplink:exception  " + e.getMessage());
        }
    }
    public final class SuggestedQuestionBroadcast {

        public static final String ACTION_QUESTION_READY =
                "ACTION_QUESTION_READY";

        private SuggestedQuestionBroadcast() {}
    }
}
