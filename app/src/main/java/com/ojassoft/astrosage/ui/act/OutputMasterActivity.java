package com.ojassoft.astrosage.ui.act;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar.OnNavigationListener;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.ImmutableList;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.CBookMarkItemCollection;
import com.ojassoft.astrosage.beans.CScreenHistoryItemCollection;
import com.ojassoft.astrosage.beans.CScreenHistoryItemCollectionStack;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIKpSystemMiscException;
import com.ojassoft.astrosage.jinterface.ICategoryBasic;
import com.ojassoft.astrosage.jinterface.ICategoryKP;
import com.ojassoft.astrosage.jinterface.ICategoryLalkitab;
import com.ojassoft.astrosage.jinterface.ICategoryMiscellaneous;
import com.ojassoft.astrosage.jinterface.ICategoryPrediction;
import com.ojassoft.astrosage.jinterface.ICategoryShodashvarga;
import com.ojassoft.astrosage.jinterface.ICategoryVarshphal;
import com.ojassoft.astrosage.jinterface.IChartStyleFragmentDailog;
import com.ojassoft.astrosage.jinterface.IChooseAyanamsaFragment;
import com.ojassoft.astrosage.jinterface.IChooseLanguageFragment;
import com.ojassoft.astrosage.jinterface.IModuleBoardListAdapter;
import com.ojassoft.astrosage.jinterface.IPersonalDetails;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadPdfReceiver;
import com.ojassoft.astrosage.misc.DownloadPdfService;
import com.ojassoft.astrosage.misc.ShareBirthChart;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.customcontrols.AppRater;
import com.ojassoft.astrosage.ui.customcontrols.CGestureDetector;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customviews.common.ViewDrawRotateKundli;
import com.ojassoft.astrosage.ui.fragments.AppLalKitabViewFragment;
import com.ojassoft.astrosage.ui.fragments.AppTajikViewFragment;
import com.ojassoft.astrosage.ui.fragments.AppViewFragment;
import com.ojassoft.astrosage.ui.fragments.AskQuestionsFragment;
import com.ojassoft.astrosage.ui.fragments.AyanamsaFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.CategoryBasic;
import com.ojassoft.astrosage.ui.fragments.CategoryDasha;
import com.ojassoft.astrosage.ui.fragments.CategoryKP;
import com.ojassoft.astrosage.ui.fragments.CategoryLalkitab;
import com.ojassoft.astrosage.ui.fragments.CategoryMisc;
import com.ojassoft.astrosage.ui.fragments.CategoryPrediction;
import com.ojassoft.astrosage.ui.fragments.CategoryShodashvarga;
import com.ojassoft.astrosage.ui.fragments.CategoryVarshphal;
import com.ojassoft.astrosage.ui.fragments.ChartStyleFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.ChooseShareOption;
import com.ojassoft.astrosage.ui.fragments.DownloadPDF;
import com.ojassoft.astrosage.ui.fragments.FragAdvertisementView;
import com.ojassoft.astrosage.ui.fragments.FragDasa;
import com.ojassoft.astrosage.ui.fragments.FragDasa.IDasaCallbacks;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.KundliAIFirstTimeMsgFragment;
import com.ojassoft.astrosage.ui.fragments.ReportsFragment;
import com.ojassoft.astrosage.ui.fragments.SaveKundliOnCloudDialog;
import com.ojassoft.astrosage.ui.fragments.TransitFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CXMLOperations;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.model.UPIAppModel;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.TypeWriter;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
import java.util.Random;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.View.GONE;
import static com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity.BACK_FROM_PLAN_PURCHASE_AD_SCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_CHALIT_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_KARAKAMSHA_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_LAGNA_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_MOON_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_NAVAMSHA_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_SWAMSA_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_TRANSIT_SCREEN_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CHART_EAST_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CHART_NORTH_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CHART_SOUTH_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_OPEN_FIRST_TIME_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_SUGGESTED_QUESTIONS_DATE_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAST_LANG_CODE_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_DATE_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_LIVE;

import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_ACTIONBAR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_SIDEMENU;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CHART_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MASTER_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MASTER_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MASTER_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_PANEL_ACTION_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_PANEL_PACKAGE;
// SAN For SCALE CHANGE
import static com.ojassoft.astrosage.utils.CGlobalVariables.fontSizeChange;
import static com.ojassoft.astrosage.utils.CGlobalVariables.isFontSizeChange;
import static com.ojassoft.astrosage.utils.CUtils.chatScreenList;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.getLanguageCode;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_AppLanguage;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.BASE_INPUT_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_MODULE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SUB_MODULE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//BaseActivity   IModuleMenuFragment
public class OutputMasterActivity extends BaseInputActivity implements OnNavigationListener,
        CGestureDetector.SimpleGestureListener, IChooseLanguageFragment,
        IChooseAyanamsaFragment, IChartStyleFragmentDailog, IDasaCallbacks,
        ICategoryPrediction, ICategoryBasic, ICategoryKP,
        ICategoryShodashvarga, ICategoryLalkitab, ICategoryVarshphal, ICategoryMiscellaneous,
        IModuleBoardListAdapter, IPurchasePlan, IPersonalDetails, VolleyResponse, BillingEventHandler, ViewDrawRotateKundli.OnKundliCellClickListener {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(OutputMasterActivity.class);

    //IModuleMenuFragment

	/*public static final int FILE = 0;
    public static final int HOME = 1;
	public static final int NEW_KUNDLI = 2;
	public static final int OPEN_KUNDLI = 3; // ADDED BY DEEPAK ON 31-10-2014
	// public static final int EDIT_KUNDLI = 4;
	public static final int EDIT_KUNDLI = 4;
	public static final int DOWNLOAD_PDF = 5;
	public static final int PRODUCT_PLAN_LIST = 6;
	// public static final int SHARE_KUNDLI = 7;
	public static final int EMAIL_SCREEN = 7;
	public static final int SETTINGS = 8;
	public static final int CHART_STYLE = 9;
	public static final int CHANGE_LANGUAGE = 10;
	public static final int CHANGE_AYANAMSA = 11;
	public static final int MISC = 12;
	public static final int HELP = 13;
	public static final int FEEDBACK = 14;
	public static final int RATE_KUNDLI_APP = 15;
	public static final int SHARE_APP = 16;
	public static final int OUR_OTHER_APPS = 17;
	public static final int CALL_US = 18;
	public static final int ASTRO_SHOP = 19;
	public static final int ASK_OUR_ASTROLOGER = 20;
	public static final int ABOUT_US = 21;
	public static final int CLOUD_SIGN_OUT = 22;// ADDED BY BIJENDRA ON 08-06-15*/

    // tejinder
    public static boolean active = false;
    boolean testBoolean = false, testBooleanOne;
    private String fileName = "";
    private Handler mHandler;
    private Bundle savedInstanceState = null;
    private Dialog dialog = null;
    // @Tejinder Singh now Chart type more than two type so we cannot use
    // boolean
    // public boolean IS_NORTH_CHART = true;
    public int chart_Style = 0;
    // End of Work
    int level = 0;
    String[] pageTitles;
    String screenName;
    public String conversationId;
    public static String notes = "";
    private CGestureDetector detector;
    boolean dasaLavelChanged = false;
    ArrayAdapter<String> topDropDownListAadapter;
    int previoueSubScreenIndex = 0, previoueModuleIndex = 0;// ADDED BY BIJENDRA
    // ON 27-05-14
    boolean isPreviousScreen = false; // ADDED BY BIJENDRA ON 05-06-14
    boolean isScreenOpenedFromBookMarkList = false;// ADDED BY BIJENDRA ON
    // 21-06-14
    boolean test2;
    boolean isVarshphalDataReadyToShow = false;
    TextView viweForStatusBar1;

    public boolean enableMonthlySubscriptionValue = false;
    int APP_LANG_CODE;


    // private InterstitialAd interstitialAd;
    //KundliOutputMenuFrag kundliOutputMenuFrag;

    public OutputMasterActivity() {
        super(R.string.app_name);
    }

    int screenId = CGlobalVariables.HOME_INPUT_SCREEN;
    ViewPager viewPager;
    final int SAVED_KUNDLI_SCREEN = 0;
    private Toolbar toolBar_OutputKundli;
    private Spinner spinnerScreenList;
    boolean is_bookMarkSelectFromAppModule;

    // private Session.StatusCallback statusCallback = new
    // SessionStatusCallback();
    ImageView toggleImageView, imgMoreItem, cloud_imageview, expandCollapseLayout,helpVideoImg;
    LinearLayout downloadKundliBasicMenuLayout, icPrint, icNotes, icShare, action_bookmark_Menu, askQuesMenuLayout, upgradeLayout;
    TextView tvTitle;
    private TabLayout tabs_input_kundli;
    private TabLayout main_tab_layout;
    HomeNavigationDrawerFragment drawerFragment;
    private CountDownTimer myCountDownTimer;
    private int SUB_SCREEN_MAIN_ID = 0;

    static boolean isActivityRunning = false;

    public String silverPlanPriceMonth = "";
    public String goldPlanPriceMonth = "";
    public String platinumPlanPriceMonth = "";
    public boolean isAdTitleHaseBeenGivenTOTabs = false;

    public static final String SKU_SILVER_PLAN_YEAR = "silver_plan_year";
    public static final String SKU_GOLD_PLAN_MONTH = "gold_plan_month";
    public static final String SKU_GOLD_PLAN_YEAR = "gold_plan_year";
    public static final String SKU_SILVER_PLAN_MONTH = "silver_plan_month";
    public static final String SKU_PLATINUM_PLAN_MONTH = "platinum_plan_month";
    public static final String SKU_PLATINUM_PLAN_YEAR = "platinum_plan_year";
    public static final String SKU_PLATINUM_PLAN_MONTH_OMF = "platinum_plan_month_omf";
    public static final String SKU_PLATINUM_PLAN_YEAR_OMF = "platinum_plan_year_omf";

    private boolean isViewPagerSwiped;
    // ArrayList<String> araySilverPlanYear = new ArrayList<String>(5);
    // ArrayList<String> arayGoldPlanYear = new ArrayList<String>(5);
    ArrayList<String> arayPlatinumPlanMonth = new ArrayList<String>(5);
    ArrayList<String> arayGoldPlanMonth = new ArrayList<String>(5);
    ArrayList<String> araySilverPlanMonth = new ArrayList<String>(5);
    private final ArrayList<String> mBroadcastList = new ArrayList<>();

    int PRICE = 2;

    String[] errorResponse = {"Success",
            "Billing response result user canceled",
            "Network connection is down",
            "Billing API version is not supported for the type requested",
            "Requested product is not available for purchase",
            "Invalid arguments provided to the API",
            "Fatal error during the API action",
            "Failure to purchase since item is already owned",
            "Failure to consume since item is not owned"};

    String purchaseData = "", signature = "", freeTrialPeriodTxt = "";


    static final int SUB_RC_REQUEST_SILVER_PLAN_YEAR = 20007;
    static final int SUB_RC_REQUEST_GOLD_PLAN_YEAR = 20008;
    static final int SUB_RC_REQUEST_SILVER_PLAN_MONTH = 20009;
    static final int SUB_RC_REQUEST_GOLD_PLAN_MONTH = 20010;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_YEAR = 20011;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_MONTH = 20012;
    int price_amount_micros = 5, price_currency_code = 6, isFreetimeperiod = 7;
    private final String notesNeedToOpenKey = "isNotesNeedToOpen";
    private final int NOTES_REQUEST_CODE = 2001;

    private CustomProgressDialog pd;
    private boolean isPdfShare;
    DownloadPDF downloadPDFFrag;
    ReportsFragment reportsFragment;
    int SAVE_KUNDLI = 1;
    int VARSHFAL_CALCULATION = 0;

    ProductDetails skuDetailsGoldPlanMonth;
    ProductDetails skuDetailsSilverPlanMonth;
    ProductDetails skuDetailsPlatinumPlanMonth;
    int requestCode;
    LinearLayout navView;
    FloatingActionButton fabOutputMaster;
    private NetworkImageView topAdImage;
    private AdData topAdData;
    private ArrayList<AdData> adList;
    private String IsShowBanner = "False";

    String heading = "";
    String subHeading = "";

    private Location locationMain = null;
    double latitude = 0.0d;
    double longitude = 0.0d;
    public static String gpsVLatDeg = "", gpsVLatMin = "", gpsVLongDeg = "", gpsVLongMin = "";
    int GET_TIME_ZONE = 2;
    public static String gpstimezoneString = "", gpstimezoneDST = "", gpstimezoneGMT = "";
    public static boolean gpsIsCurrLocFetch = false;
    RelativeLayout askQuestLayout;
    TypeWriter tvAskQuest;

    HashMap<String, ArrayList<String>> questionMap;
    int currentScreenId;

    public static final String CURRENT_SCREEN_ID_KEY = "current_screenid";
    public static final String CURRENT_VARSHPHAL_YEAR_KEY = "current_varshphal_year_key";
    public static final String LAGANA_CHART_QUESTION = "lagna_chart_question";

    public boolean isKundliChatWindowShowing = false;
    public String varshPhalSelectedYear;
    LinearLayout menuLayout;
    View extraViewForBookmark;//this view should be removed if there is no Bookmark visibility

    @AddTrace(name = "onOutputMasterActivityCreateTrace", enabled = true /* optional */)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        active = true;

        isPopupLoginShown = false;

        //Log.e("SAN ", "OMA Locale => " + CUtils.getCurrentLocale(OutputMasterActivity.this).getLanguage() );
        gpsVLatDeg = "";
        gpsVLatMin = "";
        gpsVLongDeg = "";
        gpsVLongMin = "";
        gpstimezoneString = "";
        gpstimezoneDST = "";
        gpstimezoneGMT = "";
        gpsIsCurrLocFetch = false;
        APP_LANG_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();

        enableMonthlySubscriptionValue = CUtils.getBooleanData(this, CGlobalVariables.enableMonthlySubscription, false);
        try {
            int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(OutputMasterActivity.this);
            if (purchasePlanId == CGlobalVariables.BASIC_PLAN_ID) {
                ArrayList<ProductDetails> skuDetailsList = CUtils.getCloudPlanList();
                if (skuDetailsList != null && skuDetailsList.size() > 0) {
                    for (ProductDetails skuDetails : skuDetailsList) {
                        intiProductPlan(skuDetails);
                    }
                    showProductsDetail();
                } else {
                    fetchProductFromGoogleServer();
                }
            }
        } catch (Exception ex) {
            //Log.i(ex.getStackTrace().toString());
        }
        setBillingEventHandler(this);
        SELECTED_MODULE = getIntent().getIntExtra(CGlobalVariables.MODULE_TYPE_KEY, MODULE_BASIC);
        //sendGoogleAnalytics();

        calculateKundli = getIntent().getBooleanExtra("calculateKundli", false);

        LALKITAB_INPUT_YEAR = getIntent().getIntExtra("LALKITAB_INPUT_YEAR",
                LALKITAB_INPUT_YEAR);
        VARSHPHAL_INPUT_YEAR = getIntent().getIntExtra("VARSHPHAL_INPUT_YEAR",
                VARSHPHAL_INPUT_YEAR);

        if (LALKITAB_INPUT_YEAR == -1) {
            LALKITAB_INPUT_YEAR = getPresentYearNumber();
        }
        if (VARSHPHAL_INPUT_YEAR == -1) {
            VARSHPHAL_INPUT_YEAR = getPresentYearNumber();
        }


        screenId = getIntent()
                .getIntExtra("ScreenId",
                        CGlobalVariables.HOME_INPUT_SCREEN);

        //   Log.e("mytag", "screenId " + screenId);
        if (savedInstanceState != null) {
            try {
                CGlobal cGlobal = (CGlobal) savedInstanceState
                        .getSerializable(CGlobalVariables.outPutMasterActSavedBundleKey);
                CGlobal.setCGlobalObject(cGlobal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        detector = new CGestureDetector(OutputMasterActivity.this,
                OutputMasterActivity.this);

        setContentView(R.layout.lay_output_screen);
        initIds();
        hideViewWithAnimation();
        setSupportActionBar(toolBar_OutputKundli);
        inItListeners();
        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        //AstrosageKundliApplication.currentActivity = OutputMasterActivity.this;
        if (Integer.parseInt(CUtils.getKundliAIHelpPrefNew(this)) < CGlobalVariables.HELP_LIMIT && SELECTED_MODULE == MODULE_BASIC) {
            try {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        openHoverHelpDialog(Integer.parseInt(CUtils.getHelpPref(OutputMasterActivity.this)) < CGlobalVariables.HELP_LIMIT);

                    }
                }, 1000L);
            } catch (Exception e) {
                //
            }

        }

        heading = getResources().getString(R.string.pop_up_heading_kundli);
        subHeading = getResources().getString(R.string.pop_up_sub_heading_kundli);


        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)//addded by neeraj to display correct spinner in higher virsion 29/4/16
        {
            spinnerScreenList.setPopupBackgroundResource(R.drawable.spinner_dropdown);
        }










        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().
                findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), toolBar_OutputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SELECTED_SUB_SCREEN = getIntent().getIntExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, 0);
        // ADDED BY BIJENDRA ON 19-05-15
        // UPDATED BY BIJENDRA ON 22-05-15:ADDED SHODASHVARGA ALSo
        if (SELECTED_SUB_SCREEN == 0) {

            SELECTED_SUB_SCREEN = 1;
            if (SELECTED_MODULE == MODULE_PREDICTION
                    || SELECTED_MODULE == MODULE_SHODASHVARGA)
                SELECTED_SUB_SCREEN = 0;
            else if (SELECTED_MODULE == MODULE_VARSHAPHAL) {
                SELECTED_SUB_SCREEN = 3;
                testBoolean = true;
            }
            // tejinder
            else
                testBoolean = true;
        } else {
            SUB_SCREEN_MAIN_ID = SELECTED_SUB_SCREEN;
            testBoolean = true;
        }
        // END
        previoueSubScreenIndex = SELECTED_SUB_SCREEN;// ADDED BY BIJENDRA ON
        // 27-05-14
        previoueModuleIndex = SELECTED_MODULE;// ADDED BY BIJENDRA ON 27-05-14
        int chartstyle = getIntent().getIntExtra(
                CGlobalVariables.IS_NORTH_INDIAN, -1);
        if (chartstyle == -1) {
            chartstyle = CUtils.getChartStyleFromPreference(this);
            // @ Tejinder Singh Commented it for more than two type chart now
            /*
             * if (chartstyle == CGlobalVariables.CHART_NORTH_STYLE)
             * IS_NORTH_CHART = true; else IS_NORTH_CHART = false;
             */
            chart_Style = chartstyle;
        } else {
            chart_Style = getIntent().getIntExtra(
                    CGlobalVariables.IS_NORTH_INDIAN,
                    CGlobalVariables.CHART_NORTH_STYLE);

            /*
             * if (getIntent().getIntExtra(CGlobalVariables.IS_NORTH_INDIAN,
             * CGlobalVariables.CHART_NORTH_STYLE) ==
             * CGlobalVariables.CHART_NORTH_STYLE) { IS_NORTH_CHART = true; }
             * else if (getIntent().getIntExtra(
             * CGlobalVariables.IS_NORTH_INDIAN,
             * CGlobalVariables.CHART_NORTH_STYLE) ==
             * CGlobalVariables.CHART_SOUTH_STYLE) { IS_NORTH_CHART = false; }
             */
        }
        setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);
        setListeners(viewPager);

		/*getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);

		initializeOutputModuleMenu();// ADDED BY BIJENDRA ON 05-06-14
		kundliOutputMenuFrag = new KundliOutputMenuFrag();
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_two, kundliOutputMenuFrag).commit();
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		if (SELECTED_MODULE == CGlobalVariables.MODULE_DASA) {
			sendGoogleAnalytics();
		}*/
        /*
         * if(SELECTED_MODULE!=CGlobalVariables.MODULE_DASA) {
         * setActionBarNavigation(); }
         */
        setActionBarNavigation();
        setCustomAds();
        if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN != 0) {
            if (SELECTED_SUB_SCREEN >= localAdvertismentPosition) {
                SELECTED_SUB_SCREEN = SELECTED_SUB_SCREEN + 1;
                // viewPager.setCurrentItem(SELECTED_SUB_SCREEN);
                setCurrentView(SELECTED_SUB_SCREEN, false);
            } else {
                // viewPager.setCurrentItem(SELECTED_SUB_SCREEN);
                setCurrentView(SELECTED_SUB_SCREEN, false);
            }
        } else {
            // viewPager.setCurrentItem(SELECTED_SUB_SCREEN);
            setCurrentView(SELECTED_SUB_SCREEN, false);
        }
        if ((SELECTED_MODULE == MODULE_VARSHAPHAL)) {
            testBoolean = true;
           /* new CCalculateTajikVarshphalSync(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR)
                    .execute();*/
            calculateTajikVarshphal(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR);
        }

        // CUtils.showAdvertisement(OutputMasterActivity.this,(LinearLayout)findViewById(R.id.advLayout));

        if (SELECTED_MODULE == MODULE_PREDICTION) {
            if (CUtils
                    .isUserAllowingToShowHindiTextMessage(OutputMasterActivity.this)) {
                if ((CUtils
                        .getLanguageCodeFromPreference(getApplicationContext()) == CGlobalVariables.HINDI)
                        && (!SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE)) {
                    showHindiSupportMessage();
                }
            }
        }


		/*((ImageView) toolBar_OutputKundli.findViewById(R.id.ivLeft))
                .setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						toggle();
					}
				});*/

        //If book mark is select from App Main module
        is_bookMarkSelectFromAppModule = getIntent().getBooleanExtra("is_bookMarkSelectFromAppModule", false);
        if (is_bookMarkSelectFromAppModule) {

            int group_id = getIntent().getIntExtra("group_id", 0);
            int child_id = getIntent().getIntExtra("child_id", 0);
            openSelectedBookMarkScreen(group_id, child_id);
        }
        if (getIntent().hasExtra("from")) {
            String from = getIntent().getStringExtra("from");
            if (from.equals("Notification")) {

            }
        }

        //Open notes
        boolean isNotesNeedToOpen = getIntent().getBooleanExtra(notesNeedToOpenKey, false);
        if (isNotesNeedToOpen) {
            openNotesDialog();
        }

        /*CUtils.showAdvertisement(OutputMasterActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/

        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();


        if (chatScreenList == null)
            chatScreenList = new ArrayList<>();

        mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "kundliScreenNames"));

        if (!chatScreenList.contains(mBroadcastList.get(tabs_input_kundli.getSelectedTabPosition()))) {
            sendDataToPanel(OutputMasterActivity.this, mBroadcastList.get(tabs_input_kundli.getSelectedTabPosition()));
        }

        tabs_input_kundli.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    int position = tab.getPosition();
                    if (validatePanelData(position)) {
                        sendDataToPanel(OutputMasterActivity.this, mBroadcastList.get(position));
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

         conversationId = KundliHistoryDao.getInstance(this)
                 .getConversationId(
                         String.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getLocalChartId()),
                         CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getOnlineChartId().trim(),
                         getString(R.string.txt_kundli)
                 );
         Log.e("KundliOutput","local_chat_id = "+CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getLocalChartId() + " online_chart_Id = " + CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getOnlineChartId());
        /*if (!CUtils.checkLocationPermissions(this)) {
            CUtils.requestForLocationPermissions(this, this, PERMISSION_LOCATION);
        } else {
            getLocation1();
        }*/

        getLocation1();

    }

    private void initIds() {
        viewPager = (ViewPager) findViewById(R.id.pager);

        askQuestLayout = findViewById(R.id.ask_question_layout);
        askQuestLayout.setVisibility(View.VISIBLE);
        tvAskQuest = findViewById(R.id.tv_ask_que);
        menuLayout = findViewById(R.id.menu_layout);
        menuLayout.setVisibility(GONE);
        expandCollapseLayout = findViewById(R.id.expand_collapse_icon);
        helpVideoImg = findViewById(R.id.imgAskAQuestion);
        helpVideoImg.setVisibility(View.VISIBLE);

        main_tab_layout = (TabLayout) findViewById(R.id.tabs);
        tabs_input_kundli = (TabLayout) findViewById(R.id.kundliTabLayout);
        main_tab_layout.setVisibility(GONE);
        toolBar_OutputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
        toolBar_OutputKundli.setElevation(0f);

        topAdImage = findViewById(R.id.topAdImage);

        toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
        toggleImageView.setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setVisibility(GONE);
        imgMoreItem = (ImageView) findViewById(R.id.imgMoreItem);
        imgMoreItem.setVisibility(View.VISIBLE);
        setVisibilityOfMoreIconImage(imgMoreItem, getResources().getStringArray(
                R.array.kundli_output_menu_item_list), getResources().obtainTypedArray(
                R.array.kundli_output_menu_item_list_icon), kundli_output_menu_item_list_index);
        action_bookmark_Menu = findViewById(R.id.bookmark_menu_layout);

        fabOutputMaster = findViewById(R.id.fabHome);
        navView = findViewById(R.id.nav_view);
        spinnerScreenList = (Spinner) findViewById(R.id.spinnerScreenList);
        extraViewForBookmark = findViewById(R.id.extra_view);
//LinearLayout menuLayout = findViewById(R.id.menu_layout);
        spinnerScreenList.setVisibility(View.VISIBLE);
        pageTitles = getSubMenuSpinnerItems(SELECTED_MODULE);


    }

    private void inItListeners() {
        fabOutputMaster.setOnClickListener(v -> {
            fabActions();
        });

        action_bookmark_Menu.setOnClickListener(view -> {
            int submodile = 0;
            if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN > localAdvertismentPosition) {
                submodile = SELECTED_SUB_SCREEN - 1;
            } else {
                submodile = SELECTED_SUB_SCREEN;
            }
            //goToBookMark(SELECTED_MODULE,SELECTED_SUB_SCREEN);
            goToBookMark(SELECTED_MODULE, submodile);
        });

        action_bookmark_Menu.setOnClickListener(view -> {
            int submodile = 0;
            if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN > localAdvertismentPosition) {
                submodile = SELECTED_SUB_SCREEN - 1;
            } else {
                submodile = SELECTED_SUB_SCREEN;
            }
            //goToBookMark(SELECTED_MODULE,SELECTED_SUB_SCREEN);
            goToBookMark(SELECTED_MODULE, submodile);
        });

        helpVideoImg.setOnClickListener(v -> {
            startActivity(new Intent(OutputMasterActivity.this,HelpVideoPlayerActivity.class));
        });
        askQuestLayout.setOnClickListener(view -> {

            if (!isMenuCloseflag) {
                return;
            }

            if (isKundliChatWindowShowing) {
                return; //prevent to show chat window multiple times
            }


            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_AI_CHAT_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (CUtils.isUserLogedIn(this)) {//check astrosage login
                boolean isFirstTime = CUtils.getBooleanData(this, KUNDLI_AI_OPEN_FIRST_TIME_KEY, true);
                if (isFirstTime) {
                    KundliAIFirstTimeMsgFragment kundliAIFirstTimeMsgFragment = new KundliAIFirstTimeMsgFragment(() -> {
                        openKundliAIChatWindow();
                        CUtils.saveBooleanData(this,KUNDLI_AI_OPEN_FIRST_TIME_KEY,false);
                    });
                    kundliAIFirstTimeMsgFragment.show(getSupportFragmentManager(), "kundliAIWelcomeMsg");

                    return;
                }
                openKundliAIChatWindow();
            } else {
//                PopUpLogin popUpLogin = new PopUpLogin
//                        (com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI,
//                                "ONLY_LOGIN");
//                popUpLogin.show(getSupportFragmentManager(), "PopUpLogin");
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                isKundliChatWindowShowing = true;
                Intent intent1 = new Intent(this, FlashLoginActivity.class);
                startActivity(intent1);

                //startActivity(new Intent(OutputMasterActivity.this, LoginSignUpActivity.class));
            }

        });
    }

    private void openKundliAIChatWindow() {
        ArrayList<String> suggestedQuestions = getSuggestedQuestionsForScreenId();
        String title;
        int selectedSubScreenPos;
        Resources resources = null;

        if (LANGUAGE_CODE == 1) {
            resources = getLocaleResources(this, "en");
            title = getLocalizedResources(viewPager.getCurrentItem());
        } else {
            resources = getLocaleResources(this, getLanguageKey(LANGUAGE_CODE));
            title = pageTitles[viewPager.getCurrentItem()];
        }

        if (SELECTED_SUB_SCREEN > localAdvertismentPosition && isCloudAdded)
            selectedSubScreenPos = SELECTED_SUB_SCREEN - 1;
        else
            selectedSubScreenPos = SELECTED_SUB_SCREEN;

        if (isKundliScreen()) {
            if (resources != null) {
                title = getaCustomTitles(title, resources);
            }
        } else {
            title = getaCustomTitles(title, null);
        }

        /*if (title.equals(tempTitle)) {
            if (resources != null) {
                title = resources.getString(R.string.varshfal_kundli);
            } else {
                title = getString(R.string.varshfal_kundli);
            }
        } else {
            if (isKundliScreen()) {
                if (resources != null) {
                    if (title.equals(resources.getString(R.string.transit))) {
                        title = getString(R.string.gochar) + " " + resources.getString(R.string.txt_kundli);
                    } else if (title.equals(resources.getString(R.string.chandra_chart))) {
                        title = getString(R.string.chandra_chart) + resources.getString(R.string.txt_kundli);
                    } else if (title.equals(resources.getString(R.string.graha_sthiti))) {
                        title = getString(R.string.graha_sthiti);
                    } else if (title.equals(resources.getString(R.string.graha_sthiti_sub))) {
                        title = getString(R.string.graha_sthiti_sub);
                    } else if (title.equals(resources.getString(R.string.chalit_table))) {
                        title = getString(R.string.chalit_taalika);
                    } else if (title.equals(resources.getString(R.string.birth_detail_top_heading))) {
                        title = getString(R.string.janma_vivran);
                    } else if (title.equals(resources.getString(R.string.friendship))) {
                        title = getString(R.string.maitri_chakra);
                    } else if (title.equals(resources.getString(R.string.person_details))) {
                        title = getString(R.string.vyakti_vivran);
                    } else if (title.equals(resources.getString(R.string.ghatak_and_favourable))) {
                        title = getString(R.string.ashubh_va_shubh);
                    } else {
                        title = title + " " + resources.getString(R.string.txt_kundli);
                    }
                } else {
                    title = title + " " + getString(R.string.txt_kundli);
                }
            } else {
                title = pageTitles[viewPager.getCurrentItem()];
            }
        }*/

        Intent intent = new Intent(OutputMasterActivity.this, MiniChatWindow.class);
        intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestions);
        if (SELECTED_MODULE == MODULE_VARSHAPHAL) {
            intent.putExtra(CURRENT_VARSHPHAL_YEAR_KEY, varshPhalSelectedYear);
        }
        intent.putExtra(CURRENT_SCREEN_ID_KEY, currentScreenId);
        intent.putExtra(KEY_SCREEN_NAME, title);
        intent.putExtra(KEY_MODULE_ID, SELECTED_MODULE);
        intent.putExtra(KEY_SUB_MODULE_ID, selectedSubScreenPos);
        intent.putExtra(KEY_CONVERSATION_ID,conversationId);
        intent.putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_KUNDLI_SCREEN);
        startActivity(intent);
        isKundliChatWindowShowing = true;
        isActivityRunning = false;
    }

    private String getLocalizedResources(int position) {
        Resources resources = getLocaleResources(this, "en");
        ArrayList<String> titles;

        switch (SELECTED_MODULE) {
            case CGlobalVariables.MODULE_DASA:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.dasha_sub)));
                break;
            case CGlobalVariables.MODULE_PREDICTION:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.predictions_module_list)));
                break;
            case CGlobalVariables.MODULE_KP:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.kpsystem_module_list)));
                break;
            case CGlobalVariables.MODULE_SHODASHVARGA:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.shodasvarga_module_list)));
                break;
            case CGlobalVariables.MODULE_LALKITAB:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.lalkitab_module_list)));
                break;
            case CGlobalVariables.MODULE_VARSHAPHAL:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.tajik_module_list)));
                break;
            case CGlobalVariables.MODULE_MISC:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.misc_sub)));
                break;
            default:
                titles = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.basic_module_list)));
        }

        /* increment each value after it to get exact screen if Cloud screen is not there */

        if (!TextUtils.isEmpty(goldPlanPriceMonth) || !TextUtils.isEmpty(platinumPlanPriceMonth)) {
            titles.add(3, resources.getString(R.string.featured));
        }

        return titles.get(position);
    }

    private ArrayList<String> getSuggestedQuestionsForScreenId() {
        try {
            int screenId = 0;
            int viewPagerPosition = viewPager.getCurrentItem();
            int positionForScreen;
/*** there is >2 check because we need to skip position 3(cloud screen position)
 * and increment each value after it to get exact screen if Cloud screen is not there***/
            if (!isAdTitleHaseBeenGivenTOTabs && viewPagerPosition > 2) {
                positionForScreen = viewPagerPosition + 1;
            } else {
                positionForScreen = viewPagerPosition;
            }


            switch (SELECTED_MODULE) {
                case CGlobalVariables.MODULE_BASIC:
                    screenId = CGlobalVariables.BasicIdsList[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_DASA:
                    screenId = CGlobalVariables.DashaScreenIdsArray[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_PREDICTION:
                    screenId = CGlobalVariables.AllPredictionIds[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_KP:
                    screenId = CGlobalVariables.KPSystemScreenIds[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_SHODASHVARGA:
                    screenId = CGlobalVariables.ShodashvargaScreenIds[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_LALKITAB:
                    screenId = CGlobalVariables.LalKitabScreenIds[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_VARSHAPHAL:
                    screenId = CGlobalVariables.VarshphalScreenIds[positionForScreen];
                    break;
                case CGlobalVariables.MODULE_MISC:
                    screenId = CGlobalVariables.MiscScreenIds[positionForScreen];
                    break;
            }
            //Retrieve Question List From HashMap by ScreenId
            currentScreenId = screenId;
            return new ArrayList<>(questionMap.get(String.valueOf(currentScreenId)));
        } catch (Exception e) {
            return null;
        }
    }

    Handler handler;
    Runnable runnable;

    public void openConfuseAboutKundli() {
        try {
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            //
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    if (!isKundliChatWindowShowing) {
                        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(OutputMasterActivity.this);
                        CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
                        int planId = CUtils.getUserPurchasedPlanFromPreference(OutputMasterActivity.this);
                        if (planId != CGlobalVariables.PLATINUM_PLAN_ID && planId != CGlobalVariables.PLATINUM_PLAN_ID_9 && planId != CGlobalVariables.PLATINUM_PLAN_ID_10 && planId != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                            try {
                                if (AstrosageKundliApplication.popUpLogin != null) {
                                    AstrosageKundliApplication.popUpLogin.dismiss();
                                }
                            } catch (Exception e) {
                                //
                            }
                            AstrosageKundliApplication.popUpLogin = new PopUpLogin
                                    (com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI,
                                            heading,
                                            subHeading,
                                            R.drawable.astrologer_icon_2);
                            AstrosageKundliApplication.popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");
                            isPopupLoginShown = true;
                        }

                    }
                } catch (Exception e) {
                    //
                }
            }
        };
        handler.postDelayed(runnable, 15000);

    }

    private boolean validatePanelData(int position) {
        boolean flag;

        if ((position == 0)) {
            flag = false;
        } else if (mBroadcastList.get(0).equals("Predictions")) {
            flag = false;
        } else if (chatScreenList.contains(mBroadcastList.get(position))) {
            flag = false;
        } else if (mBroadcastList.get(position).equals("Download PDF") ||
                mBroadcastList.get(position).equals("Ask a question")) {
            flag = false;
        } else flag = position != localAdvertismentPosition;

        return flag;
    }

    private void sendDataToPanel(Context context, String title) {
        try {
            chatScreenList.add(title);
            Intent intent = new Intent();
            intent.setPackage(VARTA_PANEL_PACKAGE);
            intent.setAction(VARTA_PANEL_ACTION_NAME);
            intent.putExtra("currentPosition", title);
            Log.e("mytag", "sendDataToPanel: " + title);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            //
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

    private void countRateApp() {

        myCountDownTimer = new CountDownTimer(RATE_DIALOG_THREAD_SLEEP_TIME,
                1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                if (isActivityRunning) {
                    OutputMasterActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!com.ojassoft.astrosage.varta.utils.CUtils.isPopUpLoginShowing) {
                                String Headingtext = OutputMasterActivity.this
                                        .getString(R.string.app_mainheading_text_kundali);
                                String SubHeadingtext = OutputMasterActivity.this
                                        .getString(R.string.app_subheading_text_kundali);
                                String Subchildheading = OutputMasterActivity.this
                                        .getString(R.string.app_subchild_text_kundali);
                                AppRater.app_launched(OutputMasterActivity.this, Headingtext,
                                        SubHeadingtext, Subchildheading);
                            }
                        }
                    });
                } else {
                    myCountDownTimer.cancel();
                    myCountDownTimer.start();
                }
            }
        };
        myCountDownTimer.start();

    }

    private void configureActionBarTabStyle() {
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
         * ft) { // probably ignore this event } }; Tab tab = null; for (int i =
         * 0; i < pageTitles.length; i++) { tab =
         * actionBar.newTab().setTabListener(tabListener);
         * actionBar.addTab(tab); }
         *
         * for (int i = 0; i < actionBar.getTabCount(); i++) { LayoutInflater
         * inflater = LayoutInflater.from(this); View customView =
         * inflater.inflate(R.layout.tab_title, null); TextView titleTV =
         * (TextView) customView .findViewById(R.id.action_custom_title);
         * titleTV.setText(pageTitles[i]); // Here you can also add any other
         * styling you want. titleTV.setTypeface(typeface, Typeface.BOLD); if
         * (LANGUAGE_CODE == CGlobalVariables.HINDI) {
         * titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); }
         * actionBar.getTabAt(i).setCustomView(customView); }
         */
    }

    private void setActionBarNavigation() {

        /*
         * getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST
         * );
         * getSupportActionBar().setListNavigationCallbacks(
         * topDropDownListAadapter, this);
         */
        topDropDownListAadapter = new ArrayAdapter<String>(
                OutputMasterActivity.this, R.layout.tool_bar_spinner_list_item,  //android.R.layout.simple_spinner_item,
                pageTitles) {

            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                final TextView textView = (TextView) view.findViewById(R.id.textview);
//                textView.setTextColor(Color.WHITE);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(16);
                textView.setText(pageTitles[position]);
                textView.setTypeface(regularTypeface);
               /* if (LANGUAGE_CODE == CGlobalVariables.HINDI) { textView.setText(title);
                    //to show three dots after text truncate
                    ViewTreeObserver vto = textView.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            String textViewText = textView.getText().toString();
                            Layout layout = textView.getLayout();
                            //String elpText = (String) textView.getText().subSequence(layout.getEllipsisStart(0), textView.getText().toString().length());
                            String elpText = textView.getText().subSequence(layout.getEllipsisStart(0), textView.getText().toString().length()).toString();
                            if (!textViewText.equals(elpText)) {
                                String finalString = textViewText.replace(elpText, "");
                                textView.setText(finalString + "---");
                            }
                        }
                    });
                }*/
                /*final View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v).setTypeface(regularTypeface);
                if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    //to show three dots after text truncate
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    ViewTreeObserver vto = ((TextView) v).getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            String textViewText = ((TextView) v).getText().toString();
                            Layout layout = ((TextView) v).getLayout();
                            String elpText = (String) ((TextView) v).getText().subSequence(layout.getEllipsisStart(0), ((TextView) v).getText().length());
                            if (!textViewText.equals(elpText)) {
                                String finalString = textViewText.replace(elpText, "");
                                ((TextView) v).setText(finalString + "---");
                            }
                        }


                    });
                }
                //((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setPadding(0, 0, 0, 10);*/

                return view;
            }


            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                view.setBackgroundColor(getColor(R.color.bg_card_view_color));
                TextView textView = (TextView) view.findViewById(R.id.textview);
                textView.setTextColor(getColor(R.color.black));
                textView.setTextSize(16);
                String title = pageTitles[position];
               /* if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    if (title.length() > 25) {
                        textView.setText(title.substring(0, 21) + "---");
                    } else {
                        textView.setText(title);
                    }
                } else {
                    textView.setText(title);
                }*/
                textView.setText(title);
                textView.setTypeface(regularTypeface);
             /*   View v = super.getDropDownView(position, convertView, parent);
                // Added on 9-Sep-2015
                // v.setBackgroundColor(getResources().getColor(R.color.white));
                ((TextView) v).setTextColor(getResources().getColor(R.color.black_87));
                ((TextView) v).setTypeface(regularTypeface);
                ((TextView) v).setTextSize(16);
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN)//addded by neeraj to display correct spinner in higher virsion 29/4/16
                {
                    ((TextView) v).setBackground(getResources().getDrawable(R.drawable.spinner_item_selector_jellybean));
                }

                //((TextView) v).setPadding(10,0,10,0);*/
                return view;
            }
        };
        spinnerScreenList.setAdapter(topDropDownListAadapter);
        spinnerScreenList
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(getApplicationContext(), "click",
                        // 2000).show();
                        // viewPager.setCurrentItem(position);
                        if (!isViewPagerSwiped) {
                            CUtils.googleAnalyticSendWitPlayServie(OutputMasterActivity.this,
                                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                    CGlobalVariables.GOOGLE_ANALYTIC_SPINNER_KUNDALI, GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_ACTIONBAR);
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SPINNER_KUNDALI_ACTIONBAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        }
                        isViewPagerSwiped = false;
                        setCurrentView(position, false);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }

                });
    }

    protected void showHindiSupportMessage() {
        Dialog dialog = null;
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.bg_card_view_color));
        AlertDialog.Builder builder = new AlertDialog.Builder(
                OutputMasterActivity.this);
        builder.setMessage(
                        getResources().getString(
                                R.string.this_device_does_not_support_hindi))
                .setTitle("")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                dialog = null;
                            }
                        });
        builder.setNegativeButton(
                getResources().getString(R.string.donot_show_again),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CUtils.setDoNotShowHindiLanguageSupportPopup(
                                OutputMasterActivity.this, false);
                        dialog = null;
                    }
                });
        dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(colorDrawable);
        dialog.show();
        TextView textView = (TextView) dialog
                .findViewById(android.R.id.message);
        textView.setTypeface(mediumTypeface);
        Button btnYes = (Button) dialog.findViewById(android.R.id.button1);
        Button btnNo = (Button) dialog.findViewById(android.R.id.button2);
        btnYes.setTypeface(mediumTypeface);
        btnNo.setTypeface(mediumTypeface);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean returnVal = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                //toggle();
                break;
            case KeyEvent.KEYCODE_BACK:
                try {
                    if (isMenuShowing) {
                        rotateArrow(expandCollapseLayout);
                        menuLayout.setVisibility(GONE);
                        isArrowUp = true;
                        isMenuShowing = false;
                    } else {
                        if (drawerFragment.isDrawerOpen) {
                            drawerFragment.closeDrawer();
                        } else {
                            if ((SELECTED_MODULE == MODULE_DASA)) {
                                if (dasaLavelChanged && (viewPager.getCurrentItem() == 1)) {
                                    FragDasa fd = (FragDasa) mFragments.get(1);
                                    fd.backButtonPressedForLavelUp();
                                    return false;
                                } else {
                                    returnVal = gotoPreviousScreen();
                                }
                            } else {
                                returnVal = gotoPreviousScreen();
                            }
                        }
                    }
                } catch (Exception ex) {
                    //Log.i(ex.getMessage().toString());
                    this.finish();
                }

                break;
        }
        // return super.onKeyDown(keyCode, event);
        return returnVal;
    }

    /*
     * This function is used to go previous screen
     */

    private boolean gotoPreviousScreen() {
        try {

            int size = CScreenHistoryItemCollectionStack
                    .getScreenHistoryItemCollection(this).getHistory().size();
            // tejinder
            // Toast.makeText(this, "go back"+size,Toast.LENGTH_LONG).show();
            // tejinder
            // Toast.makeText(this, "go back"+size,Toast.LENGTH_SHORT).show();
            if (size > 0) {
                int moduleId, screenId;
                moduleId = CScreenHistoryItemCollectionStack
                        .getScreenHistoryItemCollection(this).getHistory()
                        .get(size - 1).ModuleId;
                screenId = CScreenHistoryItemCollectionStack
                        .getScreenHistoryItemCollection(this).getHistory()
                        .get(size - 1).ScreenId;
                // Tejinder Added for go back some screen and than switch again
                previoueModuleIndex = moduleId;
                previoueSubScreenIndex = screenId;
                // End of work

                // //Log.e("gotoPreviousScreen",
                // "MODULE "+String.valueOf(moduleId)+"  SCREEN ID "+String.valueOf(screenId));
                // MODIFIED BY BIJENDRA 04-06-14
                SELECTED_SUB_SCREEN = screenId;
                if (moduleId == SELECTED_MODULE) {
                    isPreviousScreen = true;
                    SELECTED_MODULE = moduleId;
                }
                if (moduleId != SELECTED_MODULE) {
                    SELECTED_MODULE = moduleId;
                    // tejinder temporary this work propery on 11-jan-2016
                    // this line added for when module switch by sliding menu
                    // and come by back button previsious module sceen
                    // called two time so this line added to solve this problem
                    // isPreviousScreen = true;
                    // end work by tejinder
                    pageTitles = getSubMenuSpinnerItems(SELECTED_MODULE);
                    setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);

                    /*
                     * if(SELECTED_MODULE==CGlobalVariables.MODULE_DASA) {
                     * getSupportActionBar
                     * ().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                     * getSupportActionBar().setDisplayShowTitleEnabled(false);
                     * } else setActionBarNavigation();
                     */// DISABLED BY DEEPAK ON 15-12-2014
                    setActionBarNavigation(); // ADDED BY DEEPAK ON 15-12-2014

                    //setBehindContentView(R.layout.menu_frame);
                    /*
                     * getSupportFragmentManager() .beginTransaction()
                     * .replace(R.id.menu_frame,
                     * ModuleMenuFragment.newInstance(SELECTED_MODULE))
                     * .commit();
                     */
                    //initializeOutputModuleMenu();// ADDED BY BIJENDRA ON
                    // 05-06-14

                }
               /* try {

                    if (isAdTitleHaseBeenGivenTOTabs){
                        int selectSubModuleIfAdIsVisisble = SELECTED_SUB_SCREEN + 1;
                        viewPager.setCurrentItem(selectSubModuleIfAdIsVisisble, true);
                        *//*if(SELECTED_SUB_SCREEN >= localAdvertismentPosition) {
                            int selectSubModuleIfAdIsVisisble = SELECTED_SUB_SCREEN + 1;
                            viewPager.setCurrentItem(selectSubModuleIfAdIsVisisble, true);
                        }else if(SELECTED_SUB_SCREEN <= localAdvertismentPosition -1) {
                            int selectSubModuleIfAdIsVisisble = SELECTED_SUB_SCREEN + 1;
                            viewPager.setCurrentItem(selectSubModuleIfAdIsVisisble, true);
                        }else{
                            viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
                        }*//*
                    }else{
                        viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
                    }
                }catch (Exception ex){
                    viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
                }*/
                if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN >= localAdvertismentPosition) {
                    int selectSubModuleIfAdIsVisisble = SELECTED_SUB_SCREEN + 1;
                    viewPager.setCurrentItem(selectSubModuleIfAdIsVisisble, true);
                    setCurrentView(selectSubModuleIfAdIsVisisble, true);
                } else {
                    // viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
                    setCurrentView(SELECTED_SUB_SCREEN, true);
                }
                CScreenHistoryItemCollectionStack
                        .getScreenHistoryItemCollection(
                                OutputMasterActivity.this).removeTopItem(
                                SELECTED_MODULE, SELECTED_SUB_SCREEN);
                return false;
                // END

            } else {
                //User can also come from Main module , so no need togo home Input activity . if a user come from main module
                if (screenId == CGlobalVariables.APP_MODULE_SCREEN) {
                    this.finish();
                } else {
                    showInterstetialAdv(true);
                    // BIJENDRA ON 10-04-15
                }
            }

        } catch (Exception e) {

        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(
                CGlobalVariables.outPutMasterActSavedBundleKey,
                CGlobal.getCGlobalObject());
        super.onSaveInstanceState(outState);
        // uiHelper.onSaveInstanceState(outState);
        /*
         * Session session = Session.getActiveSession();
         * Session.saveSession(session, outState);
         */
    }

    int localAdvertismentPosition = 3;

    private String[] getSubMenuSpinnerItems(int moduleType) {
        Resources resources = getLocaleResources(this, getLanguageKey(LANGUAGE_CODE));
        //
        //localAdvertismentPosition = 3;
        String[] elements = null;
        switch (moduleType) {
            case 0:
//                localAdvertismentPosition = getRandomNumberForAdvertisePosition(3, 5);
                elements = getElementWithAdvertisement(resources.getStringArray(R.array.basic_module_list));//
                break;
            case 1:
//                localAdvertismentPosition = 3;//static value 3
                elements = getElementWithAdvertisement(resources.getStringArray(R.array.dasha_sub));
                break;
            case 2:
//                localAdvertismentPosition = getRandomNumberForAdvertisePosition(3, 4);
                elements = getElementWithAdvertisement(resources.getStringArray(
                        R.array.predictions_module_list));//
                break;
            case 3:
//                localAdvertismentPosition = getRandomNumberForAdvertisePosition(3, 5);
                elements = getElementWithAdvertisement(resources.getStringArray(
                        R.array.kpsystem_module_list));//
                break;
            case 4:
//                localAdvertismentPosition = getRandomNumberForAdvertisePosition(3, 5);
                elements = getElementWithAdvertisement(resources.getStringArray(
                        R.array.shodasvarga_module_list));//
                break;
            case 5:
//                localAdvertismentPosition = getRandomNumberForAdvertisePosition(3, 5);
                elements = getElementWithAdvertisement(resources.getStringArray(
                        R.array.lalkitab_module_list));//
                break;
            case 6:
//                localAdvertismentPosition = getRandomNumberForAdvertisePosition(2, 3);
                elements = getElementWithAdvertisement(resources.getStringArray(R.array.tajik_module_list));//
                break;
            case 7:
//                localAdvertismentPosition = 3;//static value 3
                elements = getElementWithAdvertisement(resources.getStringArray(R.array.misc_sub));
                break;
        }
        return elements;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // viewPager.setCurrentItem(itemPosition);
        setCurrentView(itemPosition, false);
        return false;
    }

    private void setViewPagerAdapter(int moduleType, boolean isVarshphalDataReadyToShow) {
        try {
            //if 50 then result will be 0 t0 49

            getSuggestQuestionForKundliModule();
            ModulePagerAdapter modulePagerAdapter = new ModulePagerAdapter(
                    getSupportFragmentManager(), moduleType, isVarshphalDataReadyToShow);
            viewPager.setAdapter(modulePagerAdapter);

            viewPager.setOffscreenPageLimit(0);

            tabs_input_kundli.setupWithViewPager(viewPager);
            // Iterate over all tabs and set the custom view
            for (int i = 0; i < tabs_input_kundli.getTabCount(); i++) {
                TabLayout.Tab tab = tabs_input_kundli.getTabAt(i);
                tab.setCustomView(modulePagerAdapter.getTabView(i));
            }

        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void setListeners(ViewPager vp) {
        vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                SELECTED_SUB_SCREEN = position;
                isViewPagerSwiped = true;
                spinnerScreenList.setSelection(position);
                //tvAskQuest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,pageTitles[position]));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isViewPagerSwiped = false;
                    }
                }, 100);
                /*switch (position) {
                case 0:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_MARGIN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}*/
                int pos = 0;

                if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN > localAdvertismentPosition) {
                    pos = position - 1;
                } else {
                    pos = position;
                }

                if (isAdTitleHaseBeenGivenTOTabs && localAdvertismentPosition == position) {
                    action_bookmark_Menu.setVisibility(GONE);
                    extraViewForBookmark.setVisibility(GONE);
                } else {
                    action_bookmark_Menu.setVisibility(View.VISIBLE);
                    extraViewForBookmark.setVisibility(View.VISIBLE);
                    setBookMarkIcon(pos);
                    OutputMasterActivity.super.setHistoryScreen(pos);
                }

                sendGoogleAnalytics();

                try {
                    // if(!isPreviousScreen)//DISABLED BY BIJENDRA ON 21-06-14
                    if ((!isPreviousScreen)
                            && (!isScreenOpenedFromBookMarkList))// ADDED BY
                        // BIJENDRA
                        // ON
                        // 21-06-14
                        // tejinder
                        if (!testBoolean) {
                            setScreenInfoInBackKeyStack(SELECTED_MODULE, previoueSubScreenIndex);
                            // setScreenInfoInBackKeyStack(pos,previoueSubScreenIndex);
                        }
                    testBoolean = false;
                    previoueSubScreenIndex = position;
                    //previoueSubScreenIndex = pos;
                    isPreviousScreen = false;

                    isScreenOpenedFromBookMarkList = false;// ADDED BY BIJENDRA
                    // ON 21-06-14
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // //Log.e("ERROr", e.getMessage());
                    e.printStackTrace();
                }// ADDED BY BIJENDRA ON 27-05-14

            }

        });
    }



/*	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		this.actionMenu = menu;
		getMenuInflater().inflate(R.menu.home_screen, menu);
		// this is because when user select any book marked sub screen or start
		// this activity bookmark icon should be initialize
		OutputMasterActivity.super.setBookMarkIcon(SELECTED_SUB_SCREEN);
		OutputMasterActivity.super.setHistoryScreen(SELECTED_SUB_SCREEN);
		return true;
	}*/

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    boolean isMenuShowing = false;

    @Override
    protected void onResume() {
        // Log.e("mytag", "onResume: ");
        super.onResume();
        LANGUAGE_CODE = getLanguageCode(this);
        setBottomNavigationText();
        isActivityRunning = true;
        isKundliChatWindowShowing = false;
        if ((!CUtils.isDhruvPlan(OutputMasterActivity.this) || CUtils.getUserPurchasedPlanFromPreference(this) != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11)&& !isPopupLoginShown) {
            //Log.e("SAN ", " openConfuseAboutKundli() ");
            openConfuseAboutKundli();
        }

        initializeBookMarks();// ADDED BY BIJENDRA ON 05-06-14
        /*typeface = CUtils.getUserSelectedLanguageFontType(
                getApplicationContext(),
				CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/
        /*CUtils.showAdvertisement(OutputMasterActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
        // registerInterstetialBroadCast();//Disabled by bijendra on
        // 10-04-15//ADDED BY BIJENDRA ON 13-02-15//
        cloud_imageview = (ImageView) findViewById(R.id.imgCloud);
        // comment_imageview = (ImageView) findViewById(R.id.imgComment);
        askQuesMenuLayout = findViewById(R.id.ask_question_menu_layout);
        icShare = findViewById(R.id.share_menu_layout);

        expandCollapseLayout.setVisibility(View.VISIBLE);
        expandCollapseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disableSingleTap = true;

                // Get the button's bounds on the screen
                expandCollapseLayout.getGlobalVisibleRect(toolbarButtonBounds);
                rotateArrow(expandCollapseLayout);
                if (!isMenuShowing) {
                    showViewWithAnimation();
                    isMenuShowing = true;
                } else {
                    hideViewWithAnimation();
                    isMenuShowing = false;
                }
                // Reset the flag after a short delay if needed
                // new Handler(Looper.getMainLooper()).postDelayed(() -> disableSingleTap = false, 300);
            }

        });

        upgradeLayout = findViewById(R.id.btnUpdatePlanMenu);
        upgradeLayout.setVisibility(View.VISIBLE);
        icNotes = findViewById(R.id.notes_menu_layout);
        icPrint = findViewById(R.id.print_menu_layout);
        downloadKundliBasicMenuLayout = findViewById(R.id.download_kundli_basic_menu_layout);
        icNotes.setVisibility(View.VISIBLE);
        if (CUtils.isUserLogedIn(this)) {
            icPrint.setVisibility(View.VISIBLE);
            downloadKundliBasicMenuLayout.setVisibility(GONE);
        } else {
            icPrint.setVisibility(GONE);
            downloadKundliBasicMenuLayout.setVisibility(View.VISIBLE);
        }
        if (CUtils.isDhruvPlan(this) || CUtils.getUserPurchasedPlanFromPreference(this) == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            upgradeLayout.setVisibility(GONE);
        } else {
            upgradeLayout.setVisibility(View.VISIBLE);
        }

//        if (CUtils.getDeviceDencity(this) <= 320) {
//            icShare.setVisibility(View.GONE);
//        } else {
////            icShare.setVisibility(View.VISIBLE);
//        }


        icNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNotesDialog();

            }
        });
        upgradeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_UPGRADE_PLAN_CLICK, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.varta.utils.CUtils.openPurchaseDhruvPlanActivity(OutputMasterActivity.this, false,false,"KundliBtnUpgradePlan",false,true);
            }
        });
        icShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shareChart();
                openShareOptionDialog();
            }
        });

        downloadKundliBasicMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.googleAnalyticSendWitPlayServie(OutputMasterActivity.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF, GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_ACTIONBAR);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_DOWNLOAD_PDF_ACTIONBAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //startActivity(new Intent(OutputMasterActivity.this, ActPrintKundliCategory.class));

                sharePDF(false);
            }
        });
        icPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.googleAnalyticSendWitPlayServie(OutputMasterActivity.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_PRINT_PDF, GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_ACTIONBAR);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_PRINT_PDF_ACTIONBAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //startActivity(new Intent(OutputMasterActivity.this, ActPrintKundliCategory.class));
                Intent intent = new Intent(OutputMasterActivity.this, ActPrintKundliCategory.class);
                intent.putExtra("ChartStyle", chart_Style);
                startActivity(intent);
////////////////////////////////
            }
        });

        BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject()
                .getHoroPersonalInfoObject();
//        Log.e("kundli_details", "onCreate beans: " + beanHoroPersonalInfo.getName());
        //      Log.e("kundli_details", "onCreate:last " + AstrosageKundliApplication.lastKundliDetails.getName());

        if (!CUtils.isUserLogedIn(OutputMasterActivity.this)) {

            if (beanHoroPersonalInfo != null && (beanHoroPersonalInfo.getOnlineChartId().equals("") || beanHoroPersonalInfo.getOnlineChartId().equals("-1"))) {
                checkForChatDeletion(beanHoroPersonalInfo);
//                cloud_imageview.setVisibility(View.VISIBLE);
                askAQuestionImgVisibility(false);
                cloud_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openSaveKundliPopup();
                    }
                });
            } else {
                cloud_imageview.setVisibility(GONE);
                askAQuestionImgVisibility(true);
            }
        } else {
            checkForChatDeletion(beanHoroPersonalInfo);
            cloud_imageview.setVisibility(GONE);
            askAQuestionImgVisibility(true);
        }
//        AstrosageKundliApplication.lastKundliDetails = beanHoroPersonalInfo;
       /* if (CUtils.isUserLogedIn(OutputMasterActivity.this) &&
                beanHoroPersonalInfo != null &&
                !beanHoroPersonalInfo.getOnlineChartId().equals("") &&
                !beanHoroPersonalInfo.getOnlineChartId().equals("-1") &&
                CUtils.getUserPurchasedPlanFromPreference(OutputMasterActivity.this) != 1) {
            comment_imageview.setVisibility(View.VISIBLE);
        } else {
            comment_imageview.setVisibility(View.GONE);
        }
        comment_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject();
                String onlineChartid = beanHoroPersonalInfo.getOnlineChartId();
                saveNotes(onlineChartid);
            }
        });*/
        super.onResume();

        updateMenus();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CUtils.hideMyKeyboard(OutputMasterActivity.this);
            }
        }, 500);
        //for update screen after plan purchase
        final SharedPreferences purchasageDataPlanSharedPreferences = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLAN, Context.MODE_PRIVATE);
        if (purchasageDataPlanSharedPreferences != null) {
            //Log.e("SHAREDVALUE ",""+purchasageDataPlanSharedPreferences.getBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE,false));
            if (purchasageDataPlanSharedPreferences.getBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE, false)) {
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*CUtils.showAdvertisement(OutputMasterActivity.this,
                                    (LinearLayout) findViewById(R.id.advLayout));*/
                            /*SharedPreferences.Editor purchasePlanEditor = purchasageDataPlanSharedPreferences.edit();
                            purchasePlanEditor.putBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE, false);
                            purchasePlanEditor.commit();*/

                            //Log.e("SHAREDVALUE After ",""+purchasageDataPlanSharedPreferences.getBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE,false));

                        }
                    }, 100);//1 minutes delay
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        animateTextInfinite();
        /*screenName = pageTitles[viewPager.getCurrentItem()];
        if (LANGUAGE_CODE == 1) {
            tvAskQuest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi, screenName));
        } else {
            tvAskQuest.animateTextInfinite(getString(R.string.ask_anything_to_kundli, screenName));
        }*/

        int purchasePlanId = com.ojassoft.astrosage.utils.CUtils.getUserPurchasedPlanFromPreference(this);
        if (purchasePlanId != com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_PLAN_ID) {
            upgradeLayout.setVisibility(GONE);
        }
    }

    private void checkForChatDeletion(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        if ((AstrosageKundliApplication.lastKundliDetails != null && beanHoroPersonalInfo.getDateTime() != null))
            if (AstrosageKundliApplication.lastKundliDetails.getDateTime().getDay() != beanHoroPersonalInfo.getDateTime().getDay()
                    || AstrosageKundliApplication.lastKundliDetails.getDateTime().getMonth() != beanHoroPersonalInfo.getDateTime().getMonth()
                    || AstrosageKundliApplication.lastKundliDetails.getDateTime().getYear() != beanHoroPersonalInfo.getDateTime().getYear()
                    || AstrosageKundliApplication.lastKundliDetails.getDateTime().getMin() != beanHoroPersonalInfo.getDateTime().getMin()
                    || AstrosageKundliApplication.lastKundliDetails.getDateTime().getHour() != beanHoroPersonalInfo.getDateTime().getHour()
                    || AstrosageKundliApplication.lastKundliDetails.getDateTime().getSecond() != beanHoroPersonalInfo.getDateTime().getSecond()
                    || !AstrosageKundliApplication.lastKundliDetails.getName().equals(beanHoroPersonalInfo.getName())) {
                AstrosageKundliApplication.kundliChatMessages = null;
                AstrosageKundliApplication.lastKundliDetails = new BeanHoroPersonalInfo();
                AstrosageKundliApplication.lastKundliDetails.getDateTime().setDay(beanHoroPersonalInfo.getDateTime().getDay());
                AstrosageKundliApplication.lastKundliDetails.getDateTime().setMonth(beanHoroPersonalInfo.getDateTime().getMonth());
                AstrosageKundliApplication.lastKundliDetails.getDateTime().setYear(beanHoroPersonalInfo.getDateTime().getYear());
                AstrosageKundliApplication.lastKundliDetails.getDateTime().setMin(beanHoroPersonalInfo.getDateTime().getMin());
                AstrosageKundliApplication.lastKundliDetails.getDateTime().setHour(beanHoroPersonalInfo.getDateTime().getHour());
                AstrosageKundliApplication.lastKundliDetails.getDateTime().setSecond(beanHoroPersonalInfo.getDateTime().getSecond());
                AstrosageKundliApplication.lastKundliDetails.setName(beanHoroPersonalInfo.getName());

            }
    }

    private boolean isArrowUp = true;

    private void rotateArrow(ImageView arrowImageView) {
        float fromDegree = isArrowUp ? 0f : 180f;  // Start angle
        float toDegree = isArrowUp ? 180f : 360f; // End angle
        isArrowUp = !isArrowUp; // Toggle state

        // Animate the rotation
        ObjectAnimator rotation = ObjectAnimator.ofFloat(arrowImageView, "rotation", fromDegree, toDegree);
        rotation.setDuration(300); // Animation duration in milliseconds
        rotation.start();
    }

    private void askAQuestionImgVisibility(boolean visible) {

        if (visible) {
            askQuesMenuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CUtils.googleAnalyticSendWitPlayServie(OutputMasterActivity.this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_FROM_MENU, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDALI_ASK_A_QUESTION_ACTIONBAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    //CUtils.sendToActAskQuestion(OutputMasterActivity.this);
                    BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                    checkForChatDeletion(beanHoroPersonalInfo);
                    int planId = 1;

                    planId = CUtils.getUserPurchasedPlanFromPreference(OutputMasterActivity.this);

                    Intent intent = new Intent(OutputMasterActivity.this, ActAskQuestion.class);
                    intent.putExtra(CGlobalVariables.DataComingFromAskAQuesAdvertisementView, true);
                    intent.putExtra(CGlobalVariables.ask_A_Question_Data, CGlobalVariables.ask_A_Question_Android);
                    intent.putExtra("BeanHoroPersonalInfo", beanHoroPersonalInfo);
                    if (planId == CGlobalVariables.SILVER_PLAN_ID ||
                            planId == CGlobalVariables.SILVER_PLAN_ID_5 ||
                            planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                            planId == CGlobalVariables.GOLD_PLAN_ID ||
                            planId == CGlobalVariables.GOLD_PLAN_ID_7 ||
                            planId == CGlobalVariables.GOLD_PLAN_ID_6 ||
                            planId == CGlobalVariables.PLATINUM_PLAN_ID ||
                            planId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                            planId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                            planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                        intent.putExtra(CGlobalVariables.IS_USER_HAS_PLAN, true);
                    } else {
                        intent.putExtra(CGlobalVariables.IS_USER_HAS_PLAN, false);
                    }
                    startActivity(intent);
                }
            });
        } else {
            askQuesMenuLayout.setVisibility(GONE);
            askQuesMenuLayout.setOnClickListener(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityRunning = false;
        /*CUtils.removeAdvertisement(OutputMasterActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    protected void onDestroy() {
        // CUtils.cancelAlarmToshowInterstetialAdv(OutputMasterActivity.this);
        active = false;
        notes = "";
        if (chatScreenList != null) chatScreenList.clear();
        super.onDestroy();
    }

    public void onDetachedFromWindow() {
        // CUtils.cancelAlarmToshowInterstetialAdv(OutputMasterActivity.this);

        super.onDetachedFromWindow();

    }

    protected void sendGoogleAnalytics() {
        //Log.e("SELECTED_MODULE ", ""+SELECTED_MODULE);
        switch (SELECTED_MODULE) {
            case MODULE_BASIC:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_BASIC);
                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_BASIC,
                                titlesName[SELECTED_SUB_SCREEN]);
                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < CGlobalVariables.GOOGLE_ANALYTIC_PAGES_BASIC.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_BASIC,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_BASIC[SELECTED_SUB_SCREEN]);
                    }
                }
                break;
            case MODULE_DASA:
                CUtils.googleAnalyticSendWitPlayServie(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                        CGlobalVariables.GOOGLE_ANALYTIC_DASHA, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DASHA, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

                break;
            case MODULE_PREDICTION:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_PREDICTION);
                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_PREDICTION,
                                titlesName[SELECTED_SUB_SCREEN]);

                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN <
                            CGlobalVariables.GOOGLE_ANALYTIC_PAGES_PREDICTION.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_PREDICTION,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_PREDICTION[SELECTED_SUB_SCREEN]);

                    }
                }
                break;
            case MODULE_KP:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_KP);

                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_KP,
                                titlesName[SELECTED_SUB_SCREEN]);
                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN <
                            CGlobalVariables.GOOGLE_ANALYTIC_PAGES_KP.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_KP,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_KP[SELECTED_SUB_SCREEN]);
                    }
                }
                break;
            case MODULE_SHODASHVARGA:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_SHODASHVARGA);
                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_SHODASHVARGA,
                                titlesName[SELECTED_SUB_SCREEN]);
                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN <
                            CGlobalVariables.GOOGLE_ANALYTIC_PAGES_SHODASHVARGA.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_SHODASHVARGA,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_SHODASHVARGA[SELECTED_SUB_SCREEN]);
                    }
                }
                break;
            case MODULE_LALKITAB:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_LALKITAB);
                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_LALKITAB,
                                titlesName[SELECTED_SUB_SCREEN]);
                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN <
                            CGlobalVariables.GOOGLE_ANALYTIC_PAGES_LALKITAB.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_LALKITAB,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_LALKITAB[SELECTED_SUB_SCREEN]);
                    }
                }
                break;
            case MODULE_VARSHAPHAL:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_VARSHPHAL);
                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_VARSHPHAL,
                                titlesName[SELECTED_SUB_SCREEN]);
                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN <
                            CGlobalVariables.GOOGLE_ANALYTIC_PAGES_VARSHPHAL.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_VARSHPHAL,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_VARSHPHAL[SELECTED_SUB_SCREEN]);
                    }
                }
                break;
            case MODULE_MISC:
                if (isAdTitleHaseBeenGivenTOTabs) {
                    String[] titlesName = getElementWithAdvertisement(CGlobalVariables.GOOGLE_ANALYTIC_PAGES_MISC);
                    if (titlesName != null && titlesName.length > 0 &&
                            SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN < titlesName.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_MISCELLANEOUS,
                                titlesName[SELECTED_SUB_SCREEN]);

                    }
                } else {
                    if (SELECTED_SUB_SCREEN >= 0 && SELECTED_SUB_SCREEN <
                            CGlobalVariables.GOOGLE_ANALYTIC_PAGES_MISC.length) {
                        CUtils.googleAnalyticSendWitPlayServie(
                                this,
                                CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                                CGlobalVariables.GOOGLE_ANALYTIC_MISCELLANEOUS,
                                CGlobalVariables.GOOGLE_ANALYTIC_PAGES_MISC[SELECTED_SUB_SCREEN]);

                    }
                }
                break;
        }

    }

    private ArrayList<Fragment> mFragments;

    @Override
    public void onYesClick(int planIndex) {
        //gotBuySilverMonthlyPlan();
        //gotBuyGoldMonthlyPlan();
        gotBuyPlatinumMonthlyPlan();
    }

    private boolean isMenuCloseflag = true;

    @Override
    public void onCellClicked(int cellNumber) {
        if (askQuestLayout.getVisibility() == GONE || isKundliChatWindowShowing) return;
        if(!CUtils.isUserLogedIn(this)){
            isPopupLoginShown = true;
            AstrosageKundliApplication.isOpenVartaPopup = true;
            isKundliChatWindowShowing = true;
            Intent intent1 = new Intent(this, FlashLoginActivity.class);
            startActivity(intent1);
            return;
        }
        ArrayList<String> suggestedQuestions = getSuggestedQuestionsForScreenId();
        Intent intent = new Intent(OutputMasterActivity.this, MiniChatWindow.class);
        intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestions);
        if (SELECTED_MODULE == MODULE_VARSHAPHAL) {
            //  Log.e("datecheck", "onCreate: "+varshPhalSelectedYear );
            intent.putExtra(CURRENT_VARSHPHAL_YEAR_KEY, varshPhalSelectedYear);
        }
        String question = null;
        try {
            getResources().getConfiguration().setLocale(Locale.getDefault());
            question = getResources().getStringArray(R.array.lagna_chart_questions)[cellNumber];
        } catch (Exception ignore) {
        }
        intent.putExtra(LAGANA_CHART_QUESTION, question);
        intent.putExtra(CURRENT_SCREEN_ID_KEY, currentScreenId);
        intent.putExtra(KEY_SCREEN_NAME, screenName);
        intent.putExtra(KEY_MODULE_ID, SELECTED_MODULE);
        intent.putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_KUNDLI_SCREEN);
        if (SELECTED_SUB_SCREEN > localAdvertismentPosition && isCloudAdded)
            intent.putExtra(KEY_SUB_MODULE_ID, SELECTED_SUB_SCREEN - 1);
        else
            intent.putExtra(KEY_SUB_MODULE_ID, SELECTED_SUB_SCREEN);

        intent.putExtra(KEY_CONVERSATION_ID,conversationId);
        if (isMenuCloseflag) {
            startActivity(intent);
            isKundliChatWindowShowing = true;
            isActivityRunning = false;
        }


    }

    boolean isCloudAdded = false;

    public class ModulePagerAdapter extends FragmentStatePagerAdapter {

        public ModulePagerAdapter(FragmentManager fm, int moduleType, boolean isVarshphalDataReadytoShow)
                throws UIKpSystemMiscException {
            super(fm);

            mFragments = new ArrayList<Fragment>();
            boolean isAdvertisementAdded = false;

            switch (moduleType) {

                case MODULE_BASIC:
                case MODULE_KP:
                case MODULE_SHODASHVARGA:
                    // MODIFIED BY BIJENDRA ON 18-5-15
                    for (int subModuleId = 0; subModuleId < pageTitles.length; subModuleId++) {
                        if (subModuleId == 0) {
                            if (moduleType == MODULE_BASIC) {
                                mFragments.add(CategoryBasic.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                                mBroadcastList.clear();
                                mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "kundliScreenNames"));
                            }
                            if (moduleType == MODULE_KP) {
                                mFragments.add(CategoryKP.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                                mBroadcastList.clear();
                                mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "kpSystemScreenNames"));
                            }
                            if (moduleType == MODULE_SHODASHVARGA) {
                                mFragments.add(CategoryShodashvarga.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                                mBroadcastList.clear();
                                mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "shodashvargaScreenNames"));
                            }
                        }
                        else if (moduleType == MODULE_BASIC && subModuleId == pageTitles.length - 2) {
                            reportsFragment = ReportsFragment.newInstance(chart_Style);
                            mFragments.add(reportsFragment);

                        }else if (moduleType == MODULE_BASIC && subModuleId == pageTitles.length - 3) {
                            downloadPDFFrag = DownloadPDF.newInstance(chart_Style);
                            mFragments.add(downloadPDFFrag);
                        }

                        else if (subModuleId == pageTitles.length - 1) {//moduleType == MODULE_BASIC &&
                            //mFragments.add(FragAskAQuesAdvertisementView.newInstance());
                            mFragments.add(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI)); //newadded

                        } else if ((subModuleId == localAdvertismentPosition) && isAdTitleHaseBeenGivenTOTabs) {
                            //Ad Advertisment with next fragment //moduleType == MODULE_BASIC &&

                            mFragments.add(FragAdvertisementView.newInstance());
                            isAdvertisementAdded = true;
                            isCloudAdded = true;

                        } else if (isAdvertisementAdded) {//moduleType == MODULE_BASIC &&
                            //-1 subModuleId because Advertisment is added
                            if (moduleType == MODULE_BASIC && subModuleId == 14) {        //for transit fragment
                                mFragments.add(TransitFragment.newInstance(CGlobal.getCGlobalObject()
                                        .getHoroPersonalInfoObject()));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(SELECTED_MODULE, subModuleId - 1, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        } else {
                            if (moduleType == MODULE_BASIC && subModuleId == 13) {
                                mFragments.add(TransitFragment.newInstance(CGlobal.getCGlobalObject()
                                        .getHoroPersonalInfoObject()));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(SELECTED_MODULE, subModuleId, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        }/* else if (isAdvertisementAdded) {//moduleType == MODULE_BASIC &&
                            //-1 subModuleId because Advertisment is added
                            mFragments.add(AppViewFragment.newInstance(
                                    SELECTED_MODULE, subModuleId - 1));
                        } else
                            mFragments.add(AppViewFragment.newInstance(
                                    SELECTED_MODULE, subModuleId));*/
                    }

                    break;
                case MODULE_PREDICTION:
                    mBroadcastList.clear();
                    mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "predictionsScreenNames"));
                    for (int subModuleId = 0; subModuleId < pageTitles.length; subModuleId++) {
                        /* ADDED BY BIJENDRA
                         * FOR PREDICTION
                         * CATEGORY ON
                         * 07-05-15
                         */
                        if (subModuleId == 2) {//|| subModuleId == 16
                            mFragments
                                    .add(AppTajikViewFragment
                                            .newInstance(
                                                    MODULE_VARSHAPHAL,
                                                    CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_PREDICTIONS,
                                                    VARSHPHAL_INPUT_YEAR, isVarshphalDataReadytoShow));

                        } else if (subModuleId == pageTitles.length - 1) {

                            // mFragments.add(FragAskAQuesAdvertisementView.newInstance());
                            mFragments.add(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI)); //newadded


                        } else {
                            /* ADDED BY BIJENDRA FOR PREDICTION CATEGORY ON 07-05-15 */
                            if (subModuleId == 0) {
                                mFragments.add(CategoryPrediction.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else if ((subModuleId == localAdvertismentPosition) && isAdTitleHaseBeenGivenTOTabs) {
                                //Ad Advertisment with next fragment //moduleType == MODULE_BASIC &&

                                mFragments.add(FragAdvertisementView.newInstance());
                                isAdvertisementAdded = true;

                            } else if (isAdvertisementAdded) {//moduleType == MODULE_BASIC &&
                                //-1 subModuleId because Advertisment is added
                                int localsubModuleId = subModuleId - 1;
                                if (localsubModuleId == 16) {
                                    mFragments
                                            .add(AppTajikViewFragment
                                                    .newInstance(
                                                            MODULE_VARSHAPHAL,
                                                            CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_PREDICTIONS,
                                                            VARSHPHAL_INPUT_YEAR, isVarshphalDataReadytoShow));
                                } else {
                                    mFragments.add(AppViewFragment.newInstance(
                                            SELECTED_MODULE, subModuleId - 1, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                                }
                            } else if (subModuleId == 16) {
                                mFragments
                                        .add(AppTajikViewFragment
                                                .newInstance(
                                                        MODULE_VARSHAPHAL,
                                                        CGlobalVariables.SUB_MODULE_TAJIK_VARSHPHAL_PREDICTIONS,
                                                        VARSHPHAL_INPUT_YEAR, isVarshphalDataReadytoShow));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        SELECTED_MODULE, subModuleId, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        }
                    }

                    break;
                case MODULE_DASA:
                    mBroadcastList.clear();
                    mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "dashaScreenNames"));
                    for (int subModuleId = 0; subModuleId < pageTitles.length; subModuleId++) {
                        //added by tejinder
                        if (subModuleId == 0) {
                            mFragments.add(CategoryDasha.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                        } else if (subModuleId == 1) {
                            FragDasa fd = CGenerateAppViews.getDasaFragment();
                            mFragments.add(fd);
                        } else if (subModuleId == 2) {
                            mFragments
                                    .add(AppViewFragment
                                            .newInstance(
                                                    MODULE_PREDICTION,
                                                    CGlobalVariables.SUB_MODULE_PREDICTION_MAHADASHA_PHALA, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                        } else if (subModuleId == 3) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(FragAdvertisementView.newInstance());
                            } else {
                                mFragments
                                        .add(AppViewFragment
                                                .newInstance(
                                                        MODULE_PREDICTION,
                                                        CGlobalVariables.SUB_MODULE_PREDICTION_CharAntardasha, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        }
                        // ADDED   BY  TEJINDER ON 01-jan-2016
                        else if (subModuleId == 4) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments
                                        .add(AppViewFragment
                                                .newInstance(
                                                        MODULE_PREDICTION,
                                                        CGlobalVariables.SUB_MODULE_PREDICTION_CharAntardasha, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments
                                        .add(AppViewFragment
                                                .newInstance(
                                                        MODULE_PREDICTION,
                                                        CGlobalVariables.SUB_MODULE_PREDICTION_YoginiDasha, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        } else if (subModuleId == 5) {
                            mFragments
                                    .add(AppViewFragment
                                            .newInstance(
                                                    MODULE_PREDICTION,
                                                    CGlobalVariables.SUB_MODULE_PREDICTION_YoginiDasha, isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                        }

                        //end

                    }

                    break;

                case MODULE_MISC:
                    mBroadcastList.clear();
                    mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "miscScreenNames"));

                    for (int subModuleId = 0; subModuleId < pageTitles.length; subModuleId++) {
                        if (subModuleId == 0) {
                            mFragments.add(CategoryMisc.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                        } /* else if ((subModuleId == localAdvertismentPosition) && isAdTitleHaseBeenGivenTOTabs) {
                            mFragments.add(FragAdvertisementView.newInstance());
                        } */ else if (subModuleId == 1) {
                            /*mFragments.add(AppViewFragment.newInstance(
                                    MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Panchadhikari,
                                    isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));*/

                            mFragments.add(AppViewFragment.newInstance(
                                    MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Yoga_Dosha_Summary,
                                    isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));

                        } else if (subModuleId == 2) {
                            mFragments.add(AppViewFragment.newInstance(
                                    MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Remedies_Recommendations,
                                    isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                        } else if (subModuleId == 3) {

                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(FragAdvertisementView.newInstance());
                                isAdvertisementAdded = true;
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Karak,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } else if (subModuleId == 4) {

                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Karak,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Avastha,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } else if (subModuleId == 5) {

                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Avastha,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Navatara,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } else if (subModuleId == 6) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Navatara,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Upgraha_Table,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        } else if (subModuleId == 7) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Upgraha_Table,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Upgraha_Chart,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }
                        } else if (subModuleId == 8) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Upgraha_Chart,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Arudha_Chart,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } /*else if (subModuleId == 9) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Arudha_Chart,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {

                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Current_Ruling_Planets,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } */ else if (subModuleId == 9) {

                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Arudha_Chart,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Shodashvarta_Table_Rashi,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } else if (subModuleId == 10) {
                            if (isAdTitleHaseBeenGivenTOTabs) {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Shodashvarta_Table_Rashi,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            } else {
                                mFragments.add(AppViewFragment.newInstance(
                                        MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Shodashvarga_Bhava,
                                        isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));
                            }

                        } else if (subModuleId == 11) {
                            mFragments.add(AppViewFragment.newInstance(
                                    MODULE_MISC, CGlobalVariables.SUB_MODULE_MISC_Shodashvarga_Bhava,
                                    isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));

                        }
                    }

                    break;

				/*//Toast.makeText(getApplicationContext(), "dasha", 2000).show();
                FragDasa fd = CGenerateAppViews.getDasaFragment();
				mFragments.add(fd);
				mFragments
						.add(AppViewFragment
								.newInstance(
										CGlobalVariables.MODULE_PREDICTION,
										CGlobalVariables.SUB_MODULE_PREDICTION_MAHADASHA_PHALA));
				mFragments.add(AppViewFragment.newInstance(
						CGlobalVariables.MODULE_PREDICTION,
						CGlobalVariables.SUB_MODULE_PREDICTION_CharAntardasha));
				mFragments.add(AppViewFragment.newInstance(
						CGlobalVariables.MODULE_PREDICTION,
						CGlobalVariables.SUB_MODULE_PREDICTION_YoginiDasha));// ADDED
																				// BY
																				// TEJINDER
																				// ON
																				// 01-jan-2016
*/

                case MODULE_LALKITAB:
                    mBroadcastList.clear();
                    mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "lalKitabScreenNames"));
                    for (int subModuleId = 0; subModuleId < pageTitles.length; subModuleId++) {
                        // MODIFIED BY BIJENDRA ON 18-5-15
                        if (subModuleId == 0)
                            mFragments.add(CategoryLalkitab.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));// END
                        else if (subModuleId == pageTitles.length - 1) {
                            //mFragments.add(FragAskAQuesAdvertisementView.newInstance());
                            mFragments.add(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI)); //newadded


                        } else if ((subModuleId == localAdvertismentPosition) && isAdTitleHaseBeenGivenTOTabs) {
                            //Ad Advertisment with next fragment //moduleType == MODULE_BASIC &&

                            mFragments.add(FragAdvertisementView.newInstance());
                            isAdvertisementAdded = true;

                        } else if (isAdvertisementAdded) {//moduleType == MODULE_BASIC &&
                            //-1 subModuleId because Advertisment is added
                            mFragments.add(AppLalKitabViewFragment.newInstance(
                                    SELECTED_MODULE, subModuleId - 1,
                                    LALKITAB_INPUT_YEAR));
                        } else
                            mFragments.add(AppLalKitabViewFragment.newInstance(
                                    SELECTED_MODULE, subModuleId,
                                    LALKITAB_INPUT_YEAR));
                    }
                    break;

                case MODULE_VARSHAPHAL:
                    mBroadcastList.clear();
                    mBroadcastList.addAll(CGlobalVariables.getBroadcastList(localAdvertismentPosition, "varshphalScreenNames"));
                    for (int subModuleId = 0; subModuleId < pageTitles.length; subModuleId++) {
                        // MODIFIED BY BIJENDRA ON 18-5-15
                        if (subModuleId == 0)
                            mFragments.add(CategoryVarshphal.newInstance(isAdTitleHaseBeenGivenTOTabs, localAdvertismentPosition));// END
                        else if (subModuleId == pageTitles.length - 1) {
                            // mFragments.add(FragAskAQuesAdvertisementView.newInstance());
                            mFragments.add(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI)); //newadded


                        } else if ((subModuleId == localAdvertismentPosition) && isAdTitleHaseBeenGivenTOTabs) {
                            //Ad Advertisment with next fragment //moduleType == MODULE_BASIC &&

                            mFragments.add(FragAdvertisementView.newInstance());
                            isAdvertisementAdded = true;

                        } else if (isAdvertisementAdded) {//moduleType == MODULE_BASIC &&
                            //-1 subModuleId because Advertisment is added
                            mFragments.add(AppTajikViewFragment.newInstance(
                                    SELECTED_MODULE, subModuleId - 1,
                                    VARSHPHAL_INPUT_YEAR, isVarshphalDataReadytoShow));
                        } else
                            mFragments.add(AppTajikViewFragment.newInstance(
                                    SELECTED_MODULE, subModuleId,
                                    VARSHPHAL_INPUT_YEAR, isVarshphalDataReadytoShow));
                    }
                    break;

            }

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

        public View getTabView(int position) {

            View view = LayoutInflater.from(OutputMasterActivity.this).inflate(R.layout.lay_input_kundli_tab_title, null);
            TextView tv = (TextView) view.findViewById(R.id.tabtext);
            // View separater = (View) view.findViewById(R.id.view);
            tv.setTypeface(regularTypeface);

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                tv.setText(pageTitles[position].toUpperCase());
            } else {
                tv.setText(pageTitles[position]);
            }

            /*if (position == 0) {
                separater.setVisibility(View.GONE);
            } else {
                separater.setVisibility(View.VISIBLE);
            }*/

            return view;


        }
    }

	/*@Override
    protected void openBookMarkedList() {
		*//*
     * @author : Amit.R
     * @date : update on 15 feb 2016,
     * @description : bookmarke is not working properly.
     *//*
     *//*CUtils.showBookMarkedScreenList(OutputMasterActivity.this, typeface,
                LANGUAGE_CODE);*//*
        openBookMarkList();

		//getSlidingMenu().showContent();
	}*/


    public void switchContent(int moduleIndex, boolean chartStyleChanged) {
        if ((SELECTED_MODULE != moduleIndex) || chartStyleChanged) {
            // //Log.e("New  ", String.valueOf(previoueModuleIndex) +" "
            // +String.valueOf(previoueSubScreenIndex));
            setScreenInfoInBackKeyStack(previoueModuleIndex,
                    previoueSubScreenIndex);// ADDED BY BIJENDRA ON 27-05-14

            Intent intent = new Intent(OutputMasterActivity.this,
                    OutputMasterActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleIndex);
            intent.putExtra(CGlobalVariables.LANGUAGE_CODE, LANGUAGE_CODE);
            // @ Tejinder Singh Edited it now more than two type chart
            if (chart_Style == 0)
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                        CGlobalVariables.CHART_NORTH_STYLE);
            else if (chart_Style == 1)
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                        CHART_SOUTH_STYLE);
            else if (chart_Style == 2)
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                        CGlobalVariables.CHART_EAST_STYLE);
            else
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                        CGlobalVariables.CHART_NORTH_STYLE);

            startActivity(intent);
            this.finish();
        } else {
            //getSlidingMenu().showContent();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ADDED BY BIJENDRA ON 17-06-14
        /*if (getSlidingMenu().isMenuShowing())
            getSlidingMenu().showContent();*/
        // END
        switch (requestCode) {
            case BOOK_MARK_LIST_ACTIVITY_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bCTP = data.getExtras();
                    int gid = bCTP.getInt("GORUP_ID");
                    int cid = bCTP.getInt("CHILD_ID");

                    // //Log.e("GORUP_ID  CHILD_ID", String.valueOf(gid)+"  "+
                    // String.valueOf(cid));
                    openSelectedBookMarkScreen(gid, cid);
                }
                break;
            case SUB_ACTIVITY_USER_LOGIN:
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                    // saveKundliOnserver();
                    callOutputMasterActivityAfterLogin(false);
                }

                break;
            case SUB_ACTIVITY_USER_LOGIN_UPLOAD_KUNDLI:
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                    saveKundliOnserver("", false);
                    //saveKundliOnserver1("", false);
                }
                break;
            case SUB_ACTIVITY_USER_LOGIN_FOR_SHARE:
                if (resultCode == RESULT_OK) {
                    shareChart();
                } else {
                    /*String a = "{\"orderId\" : \"12999763169054705758.1371079406387615\", \"packageName\" : \"com.example.app\", \"productId\" : \"goldplan\", \"purchaseTime\" : \"1345678900000\", \"purchaseState\" : \"0\", \"\" : \"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ\", \"purchaseToken\" : \"purchaseToken/bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ\"}";
                    String plan = CGlobalVariables.SILVER_PLAN_VALUE_MONTH;
                    Intent tppsIntent = new Intent(getApplicationContext(),
                            ThanksProductPurchaseScreen.class);
                    tppsIntent.putExtra("SIGNATURE", "signature");
                    tppsIntent.putExtra("PURCHASE_DATA", a);
                    tppsIntent.putExtra("DEVELOPER_PAYLOAD", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                    tppsIntent.putExtra("PLAN", plan);
                    tppsIntent.putExtra("price", "10000000");
                    tppsIntent.putExtra("priceCurrencycode", "INR");
                    startActivity(tppsIntent);

                    getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).edit()
                            .putString("VALUE", a).commit();*/
                }
                break;
            case NOTES_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                    saveKundliOnserver(getResources().getString(R.string.save_notes_error), true);
                    //saveKundliOnserver1(getResources().getString(R.string.save_notes_error), true);
                }
                break;
            case PERMISSION_STORAGE_FOR_SHARE_SCREEN:
                /*if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        screenShare();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                    }
                }*/
                screenShare();
                break;
            case PERMISSION_STORAGE_FOR_SHARE_PDF:
                /*if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        sharePDF(isPdfShare);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                    }
                }*/
                sharePDF(isPdfShare);
                break;
            case HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG: {

                //doNothing, as already plan purchased or skiped, no need to open kundli again
//                if (resultCode == RESULT_OK) {
//                    boolean purchaseSilverPlan = data.getBooleanExtra(
//                            "purchaseSilverPlan", false);
//                    int screenId = data.getIntExtra("screenId",2);
//                    setDataAfterPurchasePlan(purchaseSilverPlan, String.valueOf(screenId));
//                }
            }
            break;
            case BACK_FROM_PLAN_PURCHASE_AD_SCREEN:
                if (isEdit) {
                    goToNewEditKundli(isEdit);
                }
                break;

        }

    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            String plan = "", price = "0", priceCurrencycode = "INR";
            if (requestCode == SUB_RC_REQUEST_SILVER_PLAN_MONTH) {
                SavePlaninPreference(CGlobalVariables.SILVER_PLAN_VALUE_MONTH);
                plan = CGlobalVariables.SILVER_PLAN_VALUE_MONTH;
                price = araySilverPlanMonth.get(price_amount_micros);// 2
                priceCurrencycode = araySilverPlanMonth.get(price_currency_code);
                if (araySilverPlanMonth.size() > 7) {
                    freeTrialPeriodTxt = araySilverPlanMonth.get(isFreetimeperiod);
                }

                double dPrice = 0.0;
                try {
                    if (price != null && price.length() > 0) {
                        dPrice = Double.valueOf(price);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_SILVER_PLAN_MONTHLY_SUCCESS, null, dPrice, "");
            }

            if (requestCode == SUB_RC_REQUEST_SILVER_PLAN_YEAR) {
                SavePlaninPreference(CGlobalVariables.SILVER_PLAN_VALUE_YEAR);
                plan = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;
                price = araySilverPlanMonth.get(price_amount_micros);// 2
                priceCurrencycode = araySilverPlanMonth.get(price_currency_code);
                if (araySilverPlanMonth.size() > 7) {
                    freeTrialPeriodTxt = araySilverPlanMonth.get(isFreetimeperiod);
                }

                double dPrice = 0.0;
                try {
                    if (price != null && price.length() > 0) {
                        dPrice = Double.valueOf(price);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_SILVER_PLAN_YEARLY_SUCCESS, null, dPrice, "");
            }

            if (requestCode == SUB_RC_REQUEST_GOLD_PLAN_MONTH) {
                SavePlaninPreference(CGlobalVariables.GOLD_PLAN_VALUE_MONTH);
                plan = CGlobalVariables.GOLD_PLAN_VALUE_MONTH;
                price = arayGoldPlanMonth.get(price_amount_micros);// 2
                priceCurrencycode = arayGoldPlanMonth.get(price_currency_code);
                if (arayGoldPlanMonth.size() > 7) {
                    freeTrialPeriodTxt = arayGoldPlanMonth.get(isFreetimeperiod);
                }

                double dPrice = 0.0;
                try {
                    if (price != null && price.length() > 0) {
                        dPrice = Double.valueOf(price);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_GOLD_PLAN_MONTHLY_SUCCESS, null, dPrice, "");
            }

            if (requestCode == SUB_RC_REQUEST_GOLD_PLAN_YEAR) {
                SavePlaninPreference(CGlobalVariables.GOLD_PLAN_VALUE_YEAR);
                plan = CGlobalVariables.GOLD_PLAN_VALUE_YEAR;
                price = arayGoldPlanMonth.get(price_amount_micros);// 2
                priceCurrencycode = arayGoldPlanMonth.get(price_currency_code);
                if (arayGoldPlanMonth.size() > 7) {
                    freeTrialPeriodTxt = arayGoldPlanMonth.get(isFreetimeperiod);
                }

                double dPrice = 0.0;
                try {
                    if (price != null && price.length() > 0) {
                        dPrice = Double.valueOf(price);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_GOLD_PLAN_YEARLY_SUCCESS, null, dPrice, "");
            }

            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH) {
                SavePlaninPreference(CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;
                price = arayPlatinumPlanMonth.get(price_amount_micros);// 2
                priceCurrencycode = arayPlatinumPlanMonth.get(price_currency_code);
                if (arayPlatinumPlanMonth.size() > 7) {
                    freeTrialPeriodTxt = arayPlatinumPlanMonth.get(isFreetimeperiod);
                }

                double dPrice = 0.0;
                try {
                    if (price != null && price.length() > 0) {
                        dPrice = Double.valueOf(price);
                        dPrice = (dPrice / CGlobalVariables.PRICE_IN_ONE_UNIT);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_SUCCESS, null, dPrice, "");
            }

            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR) {
                SavePlaninPreference(CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR;
                price = arayPlatinumPlanMonth.get(price_amount_micros);// 2
                priceCurrencycode = arayPlatinumPlanMonth.get(price_currency_code);
                if (arayPlatinumPlanMonth.size() > 7) {
                    freeTrialPeriodTxt = arayPlatinumPlanMonth.get(isFreetimeperiod);
                }

                double dPrice = 0.0;
                try {
                    if (price != null && price.length() > 0) {
                        dPrice = Double.valueOf(price);
                        dPrice = (dPrice / CGlobalVariables.PRICE_IN_ONE_UNIT);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_YEARLY_SUCCESS, null, dPrice, "");
            }
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                if (purchase != null) {
                    gotoThanksPage(purchase, plan, price, priceCurrencycode, freeTrialPeriodTxt);
                }
            }
        } else {
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
            //Log.e("BillingClient", "onPurchasesUpdated() FAIL");
        }

    }


    /**
     * @author Amit Rautela
     * This method is used to call same activity after login or logout for visibility of Advertisements.
     */
    private void callOutputMasterActivityAfterLogin(boolean isNotesNeedToOpen) {

        int submodile = 0;

        if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN > localAdvertismentPosition) {
            submodile = SELECTED_SUB_SCREEN - 1;
        } else {
            submodile = SELECTED_SUB_SCREEN;
        }

        Intent intent = new Intent(OutputMasterActivity.this,
                OutputMasterActivity.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, submodile);
        intent.putExtra(CGlobalVariables.LANGUAGE_CODE, LANGUAGE_CODE);
        // @ Tejinder Singh Edited it now more than two type chart
        if (chart_Style == 0)
            intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                    CGlobalVariables.CHART_NORTH_STYLE);
        else if (chart_Style == 1)
            intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                    CHART_SOUTH_STYLE);
        else if (chart_Style == 2)
            intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                    CGlobalVariables.CHART_EAST_STYLE);
        else
            intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                    CGlobalVariables.CHART_NORTH_STYLE);
        intent.putExtra(notesNeedToOpenKey, isNotesNeedToOpen);
        startActivity(intent);
        this.finish();
    }

    private void SavePlaninPreference(String plan) {
        SharedPreferences sharedPreferences = OutputMasterActivity.this
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                        Context.MODE_PRIVATE);

        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                plan);
        sharedPrefEditor.commit();
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        drawerFragment.updateLoginDetials(false, "", "", getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        MyCustomToast mct = new MyCustomToast(OutputMasterActivity.this,
                OutputMasterActivity.this.getLayoutInflater(),
                OutputMasterActivity.this, mediumTypeface);
        mct.show(getResources().getString(R.string.sign_out_success));
        callOutputMasterActivityAfterLogin(false);
    }

    /*private class CCalculateTajikVarshphalSync extends
            AsyncTask<String, Long, Void> {
        int _inputYear = 0;
        boolean isCalulated = true;
        BeanHoroPersonalInfo _beanHoroPersonalInfo;
        String msg = "";
        CustomProgressDialog pd = null;

        public CCalculateTajikVarshphalSync(
                BeanHoroPersonalInfo beanHoroPersonalInfo, int inputYear) {
            _inputYear = inputYear;
            _beanHoroPersonalInfo = beanHoroPersonalInfo;
            CGlobal.getCGlobalObject().getBeanTajikVarshaphal()
                    .setYearNumber(String.valueOf(inputYear));

        }

        @Override
        protected void onPreExecute() {
            *//*
     * pd = ProgressDialog.show(OutputMasterActivity.this, null,
     * getResources().getString(R.string.msg_please_wait), true, false);
     * TextView tvMsg = (TextView)
     * pd.findViewById(android.R.id.message);
     * tvMsg.setTypeface(typeface); tvMsg.setTextSize(20);
     *//*
            pd = new CustomProgressDialog(OutputMasterActivity.this, regularTypeface);
            pd.show();

        }

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                isCalulated = new ControllerManager().calculateTajikVarshaphal(
                        _beanHoroPersonalInfo, String.valueOf(_inputYear));
            } catch (UICTajikVarshaphalOperationException e) {
                isCalulated = false;
                msg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {

            }
            // if (isCalulated) {

            setViewPagerAdapter(SELECTED_MODULE, (!isVarshphalDataReadyToShow));
            //viewPager.setCurrentItem(SELECTED_SUB_SCREEN);
            setCurrentView(SELECTED_SUB_SCREEN, false);
           *//* } else {

            }*//*

        }

    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        try {
            this.detector.onTouchEvent(me);
        } catch (IllegalArgumentException e) {
            //return true;
        }
        return super.dispatchTouchEvent(me);
    }

    private boolean disableSingleTap = false;
    private Rect toolbarButtonBounds = new Rect();

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Get the tap's coordinates
        int x = (int) e.getRawX();
        int y = (int) e.getRawY();

        // Ignore taps within the button's bounds
        if (toolbarButtonBounds.contains(x, y)) {
            return false; // Skip processing this tap
        }

        if (isMenuShowing) {
            rotateArrow(expandCollapseLayout);
            hideViewWithAnimation();
            isMenuShowing = false;
            isMenuCloseflag = false;
        } else {
            isMenuCloseflag = true;
        }

        //Toast.makeText(this, "Single Tap Detected!", Toast.LENGTH_SHORT).show();
        return true; // Process single tap normally
    }

    @Override
    public void onDoubleTap() {
        // CUtils.showBookMarkedScreenList(OutputMasterActivity.this, typeface,
        // LANGUAGE_CODE);
        openBookMarkList();
    }

	/*private void openLanguageSelectDialog() {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		FragmentManager fm = getSupportFragmentManager();
		Fragment prev = fm.findFragmentByTag("HOME_INPUT_LANGUAGE_OUT");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		ChooseLanguageFragmentDailog clfd = new ChooseLanguageFragmentDailog();
		clfd.show(fm, "HOME_INPUT_LANGUAGE_OUT");
		ft.commit();

	}*/

    @Override
    protected void openAyanSelectDialog() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("CHOOSE_AYAN_OUT");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AyanamsaFragmentDailog afd = new AyanamsaFragmentDailog();
        afd.show(fm, "CHOOSE_AYAN_OUT");
        ft.commit();

    }


   protected void customeDialogHelp(){
       startActivity(new Intent(OutputMasterActivity.this,HelpVideoPlayerActivity.class));

   }
    @Override
    protected void openChartStyleSelectDialog() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("CHOOSE_CHART_STYLE_OUT");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ChartStyleFragmentDailog csfd = new ChartStyleFragmentDailog();
        csfd.show(fm, "CHOOSE_CHART_STYLE_OUT");
        ft.commit();

    }

    @Override
    public void onSelectedLanguage(int languageIndex) {
        if (LANGUAGE_CODE != languageIndex) {
            LANGUAGE_CODE = languageIndex;
            // switchContent(SELECTED_MODULE);
            startActivity(new Intent(getApplicationContext(),
                    ActAppModule.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            // //Log.e("BIJENDRA","HERE");
            this.finish();
            /*
             * Intent intent=new Intent(this,
             * OutputMasterActivity.class).addFlags
             * (Intent.FLAG_ACTIVITY_CLEAR_TOP);
             * intent.putExtra(CGlobalVariables.LANGUAGE_CODE, LANGUAGE_CODE);
             * intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
             * SELECTED_MODULE);
             * intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY,
             * SELECTED_SUB_SCREEN);
             * intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
             * IS_NORTH_CHART); startActivity(intent);
             */
        }

    }

    @Override
    public void onSelectedAyanamsa(int ayanIndex) {
        // TODO Auto-generated method stub
        if (CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getAyanIndex() != ayanIndex) {
            CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .setAyanIndex(ayanIndex);
            if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN != 0) {
                if (SELECTED_SUB_SCREEN >= localAdvertismentPosition) {
                    SELECTED_SUB_SCREEN--;
                }
            }
            CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject().getHoroPersonalInfoObject(), CUtils.isConnectedWithInternet(getApplicationContext()), OutputMasterActivity.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
            kundli.calculate();
          /*  Intent intent = new Intent(getApplicationContext(),
                    ActCalculateKundli.class);
            intent.putExtra("ModuleId", SELECTED_MODULE);
            intent.putExtra("SubModuleId", SELECTED_SUB_SCREEN);
            startActivity(intent);
            this.finish();*/
        }

    }

    private void goToNewEditKundli(boolean isEdit) {
        // DISABLED BY BIJENDRA ON 17-06-14
        if (!isEdit) {
            // added clear top in case of new kundli click
            Intent intent = new Intent(getApplicationContext(),
                    HomeInputScreen.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("IS_EDIT_KUNDLI", isEdit);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);// ADDED
            // BY
            // BIJENDRA
            // ON
            // 26-05-14
            intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, SUB_SCREEN_MAIN_ID);
            startActivity(intent);
            this.finish();
        } else {
            // simply finish activity in case of edit kundli
            // this.finish();
            // ADDED BY DEEPAK ON 06-11-14
            // if user comes from Matching Activity then simply finishing will
            // not display HomeInputScreen as it is not on the backstack.
            Intent intent = new Intent(getApplicationContext(),
                    HomeInputScreen.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("IS_EDIT_KUNDLI", isEdit);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);// ADDED
            // BY
            // BIJENDRA
            // ON
            // 26-05-14
            intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, SUB_SCREEN_MAIN_ID);
            startActivity(intent);
            this.finish();
            // END ON 06-11-14
        }
        // ADDED BY BIJENDRA ON 17-06-14
        // Intent intent = new Intent(getApplicationContext(),
        // HomeInputScreen.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*
         * Intent intent = new Intent(getApplicationContext(),
         * HomeInputScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         * intent.putExtra("IS_EDIT_KUNDLI", isEdit);
         * intent.putExtra(CGlobalVariables
         * .MODULE_TYPE_KEY,SELECTED_MODULE);//ADDED BY BIJENDRA ON 26-05-14
         * startActivity(intent); this.finish();
         */
    }

    // ADDED BY DEEPAK ON 31-10-2014
    private void gotoOpenKundli() {
        Intent intent = new Intent(getApplicationContext(),
                HomeInputScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra("PAGER_INDEX", SAVED_KUNDLI_SCREEN);
        intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, SUB_SCREEN_MAIN_ID);
        startActivity(intent);
        this.finish();
    }

    // END ON 31-10-2014
    @Override
    public void onSelectedChartStyle(int chartStyleIndex) {
        boolean CHART_STYLE_CHANGED = true;
        if (chartStyleIndex == CGlobalVariables.CHART_NORTH_STYLE) {
            chart_Style = 0;
        } else if (chartStyleIndex == CHART_SOUTH_STYLE) {
            chart_Style = 1;
        } else if (chartStyleIndex == CGlobalVariables.CHART_EAST_STYLE) {
            chart_Style = 2;
        } else {
            chart_Style = 0;
        }

        switchContent(SELECTED_MODULE, CHART_STYLE_CHANGED);
    }

    @Override
    public void goToEmailScreen() {
        screenShare();
    }



    private void screenShare() {

        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH, "Share_Screen",
                "Share_Screen");
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH_SHARE_SCREEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

        toolBar_OutputKundli.setVisibility(GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                /*CUtils.removeAdvertisement(OutputMasterActivity.this,
                        (LinearLayout) findViewById(R.id.advLayout));*/
                emailScreenToUser();
                toolBar_OutputKundli.setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    private void emailScreenToUser() {

        View rootView = findViewById(android.R.id.content).getRootView();
        String savedImgUrl = CUtils.saveScreenImageInSD(this, rootView, this
                .getTitle().toString().trim(), false);
        /*CUtils.showAdvertisement(OutputMasterActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
        CUtils.sendMailOfUserScreen(this, savedImgUrl,
                CGlobalVariables.enuFileType.IMAGE);

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        countRateApp();

//        getSuggestQuestionForKundliModule();
        //EasyTracker.getInstance().activitySt
        // art(this);
        /*
        if (!CUtils.isInterstitialAdReady(OutputMasterActivity.this)) {
            ((AstrosageKundliApplication) getApplication())
                    .loadInterstitialAd();
        }*/
    }

    private void getSuggestQuestionForKundliModule() {
        try {
            // one day caching
            boolean needOnlyLagnaQuestion = false;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date curentDate = new Date();
            String todayDate = formatter.format(curentDate);
            String moduleKeyForCachedQuestion = SELECTED_MODULE + "_" + MODULE_SUGGESTED_QUESTIONS_KEY;
//            Log.e("TestQuestion", "questionKey: " + moduleKeyForCachedQuestion);
            String moduleKeyForCachedDate = SELECTED_MODULE + "_" + MODULE_SUGGESTED_QUESTIONS_DATE_KEY;
            String KUNDLI_ID = String.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getLocalChartId());
            String lagnaSuggestedQuestionDateKey = KUNDLI_ID + "_" + LAGNA_SUGGESTED_QUESTIONS_DATE_KEY;


            /**@Gaurav added check lastLanguage, to clean caching if language change detected
             * create module wise key because suggested questions came with modules(store different caching for different modules)**/
            String lastLangCodeKey = SELECTED_MODULE + "_" + LAST_LANG_CODE_KEY;

            String lastLangCode = CUtils.getStringData(this, lastLangCodeKey, "0");
            //  Log.e("TestQuestion", "getSuggestQuestionForKundliModule: lastLang " +lastLangCode);
            //  Log.e("TestQuestion", "getSuggestQuestionForKundliModule: currentLang " +APP_LANG_CODE);
            if (!lastLangCode.equalsIgnoreCase(String.valueOf(APP_LANG_CODE))) {
                CUtils.saveStringData(this, moduleKeyForCachedDate, "");
                CUtils.saveStringData(this, lagnaSuggestedQuestionDateKey, "");
            }

            Log.e("TestQuestion", "dateKey: " + moduleKeyForCachedDate);
            String moduleSuggestedQuestionDate = CUtils.getStringData(OutputMasterActivity.this, moduleKeyForCachedDate, "");
            String lagnaSuggestedQuesDate = CUtils.getStringData(OutputMasterActivity.this, lagnaSuggestedQuestionDateKey, "");

            // Log.e("TestQuestion", "getSuggestedQuestionDate: " + moduleSuggestedQuestionDate);
            if (moduleSuggestedQuestionDate.equalsIgnoreCase(todayDate)) {
                String suggestedQuestions = CUtils.getStringData(OutputMasterActivity.this, moduleKeyForCachedQuestion, "");
                if (!TextUtils.isEmpty(suggestedQuestions)) { //check in local
                    //       Log.e("TestQuestion", "suggestedQuestion: from local" + suggestedQuestions);

                    parseSuggestedQuestion(suggestedQuestions, needOnlyLagnaQuestion);
                    if (SELECTED_MODULE == 0 && !lagnaSuggestedQuesDate.equalsIgnoreCase(todayDate)) {
                        needOnlyLagnaQuestion = true;
                    } else {
                        return;
                    }
                }
            }
            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getSuggestedQuestionModule(getQuestionListParams(needOnlyLagnaQuestion));
            boolean finalNeedOnlyLagnaQuestion = needOnlyLagnaQuestion;
            //   Log.e("TestQuestion", "needLagna: " + needOnlyLagnaQuestion);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseString = response.body().string();
                            //Log.e("TestQuestion", "onResponse: " + responseString);
                            CUtils.saveStringData(OutputMasterActivity.this, lastLangCodeKey, String.valueOf(CUtils.getIntData(OutputMasterActivity.this, APP_PREFS_AppLanguage, 0)));
                            parseSuggestedQuestion(responseString, finalNeedOnlyLagnaQuestion);
                            CUtils.saveStringData(OutputMasterActivity.this, lastLangCodeKey, String.valueOf(APP_LANG_CODE));

                            if (!finalNeedOnlyLagnaQuestion) {
                                CUtils.saveStringData(OutputMasterActivity.this, moduleKeyForCachedDate, todayDate);
                                CUtils.saveStringData(OutputMasterActivity.this, moduleKeyForCachedQuestion, responseString);
                            } else {
                                CUtils.saveStringData(OutputMasterActivity.this, lagnaSuggestedQuestionDateKey, todayDate);
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
        } catch (Exception e) {
            //
        }
    }

    private void parseSuggestedQuestion(String responseString, boolean needOnlyUpdateLagnaQuestion) {
        try {
            if (questionMap == null) {
                questionMap = new HashMap<>();
            }

            JSONObject jsonObject = new JSONObject(responseString);
//            CUtils.saveStringData(OutputMasterActivity.this, MODULE_SUGGESTED_QUESTIONS_KEY, jsonObject.toString());
            Iterator<String> keys = jsonObject.keys();

            //Log.e("TestQuestion", "onResponse key set: " + keys);
            Gson gson = new Gson();
            // Iterate over the keys

            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> questionList = (gson.fromJson(jsonArray.toString(), listType));
                if (!key.isEmpty() && questionList != null) {
                    if (!needOnlyUpdateLagnaQuestion) {
                        questionMap.put(key, questionList);
                    } else {
                        if (SDK_INT >= Build.VERSION_CODES.N) {
                            questionMap.replace(key, questionList);
                        }
                    }
                }
            }

            //  Log.e("TestQuestion", "parseSuggestedQuestion: " + questionMap.toString());

        } catch (Exception e) {
            //
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {

        //EasyTracker.getInstance().activityStop(this);
        // unregisterReceiver(interstetialAdvBroadcastReceiver);//Disabled by
        // bijendra on 10-04-15
        try {

            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
            }

            if (AppRater.myCountDownTimer != null) {
                AppRater.myCountDownTimer.cancel();
            }

        } catch (Exception e) {
            //Log.i(e.getMessage().toString());
        }

        super.onStop();
    }

    @Override
    public void setLavelBoolean(boolean b) {
        this.dasaLavelChanged = b;
    }

	/*@Override
    public void switchContent(int menuItemPosition) {
		switch (menuItemPosition) {
		case FILE:
			// its separator do nothing
			break;
		case HOME:
			CUtils.gotoHomeScreen(OutputMasterActivity.this);
			break;
		case NEW_KUNDLI:
			// DISABLED BY BIJENDRA ON 13-02-15
			// ENABLED BY BIJENDRA ON 10-04-15
			*//*
     * if(CUtils.isInterstitialAdReady()){
     * CUtils.displayInterstitialAd(new AdListener() {
     *
     * @Override public void onAdClosed() { super.onAdClosed();
     * if(AstrosageKundliApplication.interstitialAd != null)
     * AstrosageKundliApplication.interstitialAd.setAdListener(null);
     * goToNewEditKundli(false); } }); }else{ goToNewEditKundli(false);
     * } goToNewEditKundli(false);
     *//*
     *//* tejinder *//*
            showInterstetialAdv(true);

			break;
		case EDIT_KUNDLI:
			// goToNewEditKundli(true);
			// //Log.e("bIJENDRA", "Edit");
			showInterstetialAdv(true);
			break;
		// ADDED BY Shelendra ON 04-06-2015

		case PRODUCT_PLAN_LIST:
			CUtils.gotopProductPlanList(OutputMasterActivity.this,
					LANGUAGE_CODE,
					HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE);
			break;
		case OPEN_KUNDLI:
			gotoOpenKundli();
			break;
		// END ON 31-10-2014
		case DOWNLOAD_PDF:
			// @TEJINDER SINGH TEMPORARY ADDED
			boolean IS_NORTH_CHART = false;
			if (chart_Style == 0 || chart_Style == 2)
				IS_NORTH_CHART = true;
			CUtils.downloadKundliPdf(OutputMasterActivity.this, IS_NORTH_CHART,
					LANGUAGE_CODE);
			break;
		*//*
     * case SHARE_KUNDLI: CUtils.googleAnalyticSendWitPlayServie(this,
     * CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH, "Share_Kundli_with_friends",
     * "Share_Kundli_with_friends"); Intent intentShareKundli = new
     * Intent(OutputMasterActivity.this,
     * ActSearchKundliToPubliclyShare.class);
     * intentShareKundli.putExtra(CGlobalVariables.LANGUAGE_CODE,
     * LANGUAGE_CODE); startActivityForResult(intentShareKundli, 5000);
     * break;
     *//*
        case EMAIL_SCREEN:
			// CUtils.googleAnalyticSend(null,
			// CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH, "Share_Screen",
			// "Share_Screen", 0L);
			CUtils.googleAnalyticSendWitPlayServie(this,
					CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH, "Share_Screen",
					"Share_Screen");
			goToEmailScreen();
			break;

		case SETTINGS:
			// its separator do nothing
			break;
		case CHART_STYLE:
			openChartStyleSelectDialog();
			break;
		case CHANGE_LANGUAGE:
			openLanguageSelectDialog();
			*//*
     * if(LANGUAGE_CODE==CGlobalVariables.ENGLISH)
     * LANGUAGE_CODE=CGlobalVariables.HINDI; else
     * LANGUAGE_CODE=CGlobalVariables.ENGLISH;
     *//*

     *//* switchContent(SELECTED_MODULE); *//*
            break;
		case CHANGE_AYANAMSA:
			openAyanSelectDialog();
			break;
		case MISC:
			// its separator do nothing
			break;
		case HELP:

			customeDialogHelp();
			break;
		case FEEDBACK:
			CUtils.sendFeedBackViaApi(this, typeface,
					CUtils.getUserName(OutputMasterActivity.this));
			break;
		case RATE_KUNDLI_APP:
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
		}
		//getSlidingMenu().showContent();
	}*/

    boolean isEdit;
    private void showInterstetialAdv(final boolean isEdit) {
        this.isEdit = isEdit;

        // Check if the interstitial/home popup feature is enabled from tagmanager
        boolean isPopupEnabled = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, CGlobalVariables.IS_INTERSTICIAL_ENABLED, false);

        // Check if the user is already a premium subscriber (Kundli AI+ or Dhruv Plan)
        boolean isPremiumUser = CUtils.isKundliAIPlusPlan(this) || CUtils.isDhruvPlan(this);

        // Logic: If popup is enabled AND user is NOT premium AND the app is allowed to show an interstitial...
        if (isPopupEnabled && !isPremiumUser && CUtils.canShowInterstitial(this)) {
            // ...Redirect to the Plan Purchase screen (acting as an Ad)
            com.ojassoft.astrosage.varta.utils.CUtils.openPurchasePlanScreenForAd(
                    this,
                    false,
                    false,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_KUNDLI_SCREEN_AD,
                    true,
                    BACK_FROM_PLAN_PURCHASE_AD_SCREEN
            );
        } else {
            // Otherwise, proceed directly to the New/Edit Kundli screen
            goToNewEditKundli(isEdit);
        }


        // DISABLED BY BIJENDRA ON 10-04-15
        /* comment by vishal
        if (CUtils.isInterstitialAdReady(OutputMasterActivity.this)) {
            goToNewEditKundli(isEdit); // changed by vishal
            if (CUtils.isCycleCompleted()) {
                CUtils.displayInterstitialAd(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        goToNewEditKundli(isEdit);
                    }
                });
            } else {
                goToNewEditKundli(isEdit);
            }
        } else {
            goToNewEditKundli(isEdit);
        }*/
        // END

        // ADDED BY BIJENDRA ON 10-04-15

        /*
         * interstitialAd = new InterstitialAd(this);
         * interstitialAd.setAdUnitId(CGlobalVariables.INTERSTITIAL_AD_ID); //
         * Create an ad request. AdRequest.Builder adRequestBuilder = new
         * AdRequest.Builder();
         *
         * // Optionally populate the ad request builder.
         * adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
         *
         * // Set an AdListener. interstitialAd.setAdListener(new AdListener() {
         *
         * @Override public void onAdLoaded() { interstitialAd .show(); }
         *
         * @Override public void onAdClosed() { // Proceed to the next level.
         * goToNewEditKundli(isEdit); }
         *
         * @Override public void onAdFailedToLoad(int errorCode) { // TODO
         * Auto-generated method stub super.onAdFailedToLoad(errorCode);
         * goToNewEditKundli(isEdit); } });
         *
         * interstitialAd.loadAd(adRequestBuilder.build()); //END
         */

    }

    /**
     * facebook methods and classes for creating session
     */
    /*
     * private void createFacebookSession(){
     * Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
     *
     * Session session = Session.getActiveSession(); if (session == null) { if
     * (savedInstanceState != null) { session = Session.restoreSession(this,
     * null, statusCallback, savedInstanceState); } if (session == null) {
     * session = new Session(this); } Session.setActiveSession(session); if
     * (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
     * session.openForRead(new Session.OpenRequest(this)
     * .setCallback(statusCallback)); } }
     *
     * }
     */

    /*
     * class for Status Callback for facebook session it will upload the image
     * after creating of facebook session
     */
    /*
     * private class SessionStatusCallback implements Session.StatusCallback {
     *
     * @SuppressWarnings("deprecation")
     *
     * @Override public void call(Session session, SessionState state, Exception
     * exception) { View rootView =
     * findViewById(android.R.id.content).getRootView(); String
     * savedImgUrl=CUtils.saveScreenImageInSD(OutputMasterActivity.this,
     * rootView, OutputMasterActivity.this.getTitle().toString().trim(),false);
     * if(session.isOpened()){ //Log.e("here", "call");
     *
     *
     * File file = new File(savedImgUrl); //Log.e("file", file.getPath()); Bitmap
     * bi = BitmapFactory.decodeFile(file.getPath());
     * Request.executeUploadPhotoRequestAsync(Session.getActiveSession(), bi,
     * new Request.Callback() {
     *
     * @Override public void onCompleted(Response response) { MyCustomToast mct
     * = new MyCustomToast(OutputMasterActivity.this,
     * OutputMasterActivity.this.getLayoutInflater(), OutputMasterActivity.this,
     * (OutputMasterActivity.this).typeface);
     * mct.show(OutputMasterActivity.this.
     * getResources().getString(R.string.facebook_upload));
     *
     * }
     *
     * }); } } }
     */

    /**
     * publishing feed to facebook create session
     */
    /*
     * private void publishFeedDialog() { createFacebookSession(); Session
     * session = Session.getActiveSession(); if (!session.isOpened() &&
     * !session.isClosed()) {
     *
     * Session.OpenRequest request = new Session.OpenRequest(this);
     *
     * request.setCallback(statusCallback );
     *
     * session.openForRead(request); //Log.e("not open", "not close"); } else {
     * Session.openActiveSession(this, true, statusCallback); //Log.e("open",
     * "open"); }
     *
     * if (session.isOpened()) { //Log.e("URL_PREFIX_FRIENDS",
     * session.getAccessToken().toString());
     *
     *
     * //Log.e("URL_PREFIX_FRIENDS", session.getAccessToken().toString());
     * onClickLogout(); }
     *
     *
     * } private void onClickLogout() { Session session =
     * Session.getActiveSession(); if (!session.isClosed()) {
     * session.closeAndClearTokenInformation(); } }
     */

    /**
     * ask to user for conforming of uploading of images on facebook
     */
    private void facebookDialog() {
        Dialog dialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                        getResources().getString(R.string.share_facebook_ask))
                .setTitle("")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                MyCustomToast mct = new MyCustomToast(
                                        OutputMasterActivity.this,
                                        OutputMasterActivity.this
                                                .getLayoutInflater(),
                                        OutputMasterActivity.this,
                                        (OutputMasterActivity.this).mediumTypeface);
                                mct.show(OutputMasterActivity.this
                                        .getResources().getString(
                                                R.string.facebook_progress));
                                // publishFeedDialog();
                                dialog = null;

                            }
                        });
        builder.setNegativeButton(getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog = null;
                    }
                });
        dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        TextView textView = (TextView) dialog
                .findViewById(android.R.id.message);
        textView.setTypeface(mediumTypeface);
        Button btnYes = (Button) dialog.findViewById(android.R.id.button1);
        Button btnNo = (Button) dialog.findViewById(android.R.id.button2);
        btnYes.setTypeface(mediumTypeface);
        btnNo.setTypeface(mediumTypeface);
    }

    /**
     * This method open a dialog for helping user
     */

    protected void openHoverHelpDialog(boolean isFirstTime) {
        try {
            Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            Animation ScaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            ScaleAnimation.setDuration(500);
            alphaAnimation.setDuration(500);
            dialog = new Dialog(this);
            LinearLayout layoutRotateHelp = null, layoutSpinnerHelp = null;
            LinearLayout layoutMove;
            TextView textRotateHelp, textMainMenu, textSpinner, textRightMenu, textMove, textBookmark, textkundliAIChatHelp, textKundliHouseHelp;
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflator.inflate(R.layout.lay_help_new, null, false);

            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.setCancelable(false);//false
            dialog.setCanceledOnTouchOutside(false);
            view.startAnimation(alphaAnimation);
            view.startAnimation(ScaleAnimation);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            TextView skipBtn = dialog.findViewById(R.id.skip_btn);
            ConstraintLayout kundliIntroLayout = dialog.findViewById(R.id.kundli_page_intro_layout);
            RelativeLayout northKundliHouseIntroLayout = dialog.findViewById(R.id.north_kundli_house_layout);
            RelativeLayout eastKundliHouseIntroLayout = dialog.findViewById(R.id.east_kundli_house_layout);
            View requiredKundliHouseView;
            SharedPreferences sharedPreferences = getSharedPreferences(
                    CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);

            switch (sharedPreferences.getInt(CGlobalVariables.APP_PREFS_ChartStyle, CHART_NORTH_STYLE)) {
                case CHART_NORTH_STYLE:
                    requiredKundliHouseView = northKundliHouseIntroLayout;
                    setUpNorthKundliHouseHelpView();
                    break;
                case CHART_EAST_STYLE:
                    requiredKundliHouseView = eastKundliHouseIntroLayout;
                    setUpEastKundliHouseHelpView();
                    break;
                case CHART_SOUTH_STYLE:
                    requiredKundliHouseView = eastKundliHouseIntroLayout;
                    setUpSouthKundliHouseHelpView();
                    break;
                default:
                    requiredKundliHouseView = northKundliHouseIntroLayout;
                    setUpNorthKundliHouseHelpView();
                    break;
            }


            ConstraintLayout chatBoxIntroLayout = dialog.findViewById(R.id.ask_question_layout);
            TextView chatBoxTV = dialog.findViewById(R.id.tv_ask_que);
            chatBoxTV.setText(getString(R.string.ask_anything_to_kundli, pageTitles[1]));


            skipBtn.setOnClickListener(v -> {
                dialog.dismiss();//need to handle preference
            });

            ImageView nextIcon = dialog.findViewById(R.id.next_image);
            TextView nextDoneTV = dialog.findViewById(R.id.next_done_tv);
            nextDoneTV.setText(getString(R.string.next));
            LinearLayout nextDoneBtn = dialog.findViewById(R.id.next_done_btn);
            nextDoneBtn.setOnClickListener(v -> {
                if (nextDoneTV.getText().equals(getString(R.string.done))) {
                    dialog.dismiss();
                }
                if (kundliIntroLayout.getVisibility() == View.VISIBLE) {
                    kundliIntroLayout.setVisibility(GONE);
                    requiredKundliHouseView.setVisibility(View.VISIBLE);
                } else if (requiredKundliHouseView.getVisibility() == View.VISIBLE) {
                    kundliIntroLayout.setVisibility(GONE);
                    requiredKundliHouseView.setVisibility(GONE);
                    chatBoxIntroLayout.setVisibility(View.VISIBLE);
                    nextDoneTV.setText(getString(R.string.done));
                    nextIcon.setVisibility(GONE);
                    skipBtn.setVisibility(GONE);
                }

            });


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);

            String number = CUtils.getHelpPref(OutputMasterActivity.this);
            CUtils.saveHelpPref(OutputMasterActivity.this,
                    String.valueOf(Integer.valueOf(number) + 1));

            String kundliAINumber = CUtils.getKundliAIHelpPrefNew(OutputMasterActivity.this);
            CUtils.saveKundliAIHelpPrefNew(OutputMasterActivity.this,
                    String.valueOf(Integer.valueOf(kundliAINumber) + 1));
        } catch (Exception e){
            //
        }

    }

//    protected void customeDialogHelp(boolean isFirstTime) {
//        try {
//            Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
//            Animation ScaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                    0.5f);
//            ScaleAnimation.setDuration(500);
//            alphaAnimation.setDuration(500);
//
//            dialog = new Dialog(this);
//            LinearLayout layoutRotateHelp = null, layoutSpinnerHelp = null;
//            LinearLayout layoutMove;
//            TextView textRotateHelp, textMainMenu, textSpinner, textRightMenu, textMove, textBookmark, textkundliAIChatHelp, textKundliHouseHelp;
//            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflator.inflate(R.layout.lay_help, null, false);
//
//            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(view);
//            dialog.setCancelable(false);//false
//            dialog.setCanceledOnTouchOutside(false);
//            view.startAnimation(alphaAnimation);
//            view.startAnimation(ScaleAnimation);
//
//            layoutRotateHelp = dialog.findViewById(R.id.layoutRotateHelp);
//            layoutSpinnerHelp = dialog.findViewById(R.id.layoutSpinnerHelp);
//            layoutMove = dialog.findViewById(R.id.layoutMove);
//            viweForStatusBar1 = dialog.findViewById(R.id.viweForStatusBar1);
//            ImageView ivNextIntroChatBoxBtn = dialog.findViewById(R.id.ivChatIntroNext);
//            ImageView ivNextIntroFirstBtn = dialog.findViewById(R.id.ivIntroFirstBtn);
//            ImageView ivFinalNextBtn = dialog.findViewById(R.id.kundliHouseNextBtn);
//            RelativeLayout firstIntroLayout = dialog.findViewById(R.id.first_intro_layout);
//            LinearLayout kundliBoxIntroLayout = dialog.findViewById(R.id.kundli_btn_intro_layout);
//            RelativeLayout KundliHouseHelpLayout = dialog.findViewById(R.id.north_kundli_house_layout);
//
////here we decide whether need to show only Kundli AI intro or full intro
//            if (!isFirstTime) {
//                firstIntroLayout.setVisibility(GONE);
//                kundliBoxIntroLayout.setVisibility(View.VISIBLE);
//            } else {
//                firstIntroLayout.setVisibility(View.VISIBLE);
//                kundliBoxIntroLayout.setVisibility(GONE);
//            }
//
//            ivNextIntroFirstBtn.setOnClickListener(v -> {
//                firstIntroLayout.setVisibility(GONE);
//                kundliBoxIntroLayout.setVisibility(View.VISIBLE);
//            });
//
//
//            ivNextIntroChatBoxBtn.setOnClickListener(v -> {
//                KundliHouseHelpLayout.setVisibility(View.VISIBLE);
//                kundliBoxIntroLayout.setVisibility(GONE);
//            });
//
//            ivFinalNextBtn.setOnClickListener(v -> dialog.dismiss());
//
//            FrameLayout KundliHouseHelpFrame = dialog.findViewById(R.id.kundli_house);
//            setUpNorthKundliHouseHelpView();
//
//            textRotateHelp = (TextView) dialog.findViewById(R.id.textViewRotateHelp);
//            textMainMenu = (TextView) dialog.findViewById(R.id.tvMainMunuHelp);
//            textSpinner = (TextView) dialog.findViewById(R.id.tvSpinnerHelp);
//            textRightMenu = (TextView) dialog.findViewById(R.id.tvRightMenuHelp);
//            textBookmark = (TextView) dialog.findViewById(R.id.textDoubleTouch);
//            textMove = (TextView) dialog.findViewById(R.id.textMove);
//            textkundliAIChatHelp = (TextView) dialog.findViewById(R.id.tv_intro_chat_box);
//            textKundliHouseHelp = (TextView) dialog.findViewById(R.id.kundliHouseHelpText);
//            TextView tv_ask_quest = dialog.findViewById(R.id.tv_ask_que);
//
//            textRotateHelp.setTypeface(mediumTypeface);
//            textMainMenu.setTypeface(mediumTypeface);
//            textSpinner.setTypeface(mediumTypeface);
//            textRightMenu.setTypeface(mediumTypeface);
//            textBookmark.setTypeface(mediumTypeface);
//            textMove.setTypeface(mediumTypeface);
//            textkundliAIChatHelp.setTypeface(mediumTypeface);
//            if (LANGUAGE_CODE == 1) {
//                textkundliAIChatHelp.setText(getString(R.string.ask_anything_to_kundli_hi, pageTitles[1]));
//                tv_ask_quest.setText(getString(R.string.ask_anything_to_kundli_hi, pageTitles[1]));
//            } else {
//                textkundliAIChatHelp.setText(getString(R.string.ask_anything_to_kundli, pageTitles[1]));
//                tv_ask_quest.setText(getString(R.string.ask_anything_to_kundli, pageTitles[1]));
//            }
//
//            textKundliHouseHelp.setTypeface(mediumTypeface);
//
//            textRotateHelp.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textMainMenu.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textSpinner.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textRightMenu.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textBookmark.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textMove.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textKundliHouseHelp.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//            textkundliAIChatHelp.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize_List);
//
//            textRotateHelp.setText(getResources().getString(R.string.rorate_help));
//
//            textMove.setText(getResources().getString(R.string.swipe_help));
//            dialog.getWindow().setBackgroundDrawable(
//                    new ColorDrawable(Color.TRANSPARENT));
//
//            if (SELECTED_MODULE == MODULE_DASA) {
//                if (SELECTED_SUB_SCREEN == 1) {
//                    textRotateHelp.setText(getResources()
//                            .getString(R.string.dasha_help));
//                    textRotateHelp.setVisibility(View.VISIBLE);
//                } else {
//                    textRotateHelp.setVisibility(GONE);
//                }
//
//                layoutSpinnerHelp.setVisibility(GONE);
//                layoutMove.setVisibility(GONE);
//
//            } else if (!CUtils.isKundliOnScreen(SELECTED_MODULE,
//                    SELECTED_SUB_SCREEN)) {
//                layoutRotateHelp.setVisibility(GONE);
//            } else if (CUtils.getChartStyleFromPreference(this) != CGlobalVariables.CHART_NORTH_STYLE) {
//                layoutRotateHelp.setVisibility(GONE);
//            }
//            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//            lp.copyFrom(dialog.getWindow().getAttributes());
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            dialog.show();
//            dialog.getWindow().setAttributes(lp);
//            addOptionHelp(view);
//
//            String number = CUtils.getHelpPref(OutputMasterActivity.this);
//            CUtils.saveHelpPref(OutputMasterActivity.this,
//                    String.valueOf(Integer.valueOf(number) + 1));
//
//            String kundliAINumber = CUtils.getKundliAIHelpPref(OutputMasterActivity.this);
//            CUtils.saveKundliAIHelpPref(OutputMasterActivity.this,
//                    String.valueOf(Integer.valueOf(kundliAINumber) + 1));
//
//
//            /*
//             * new Handler().postDelayed(new Runnable() {
//             *
//             * @Override public void run() { dialog.dismiss();
//             *
//             * } }, 2000*60);
//             */
//        } catch (Exception e) {
//            //
//        }
//    }

    private void setUpNorthKundliHouseHelpView() {
        // Set up the main diamond placeholder
        FrameLayout dynamicPlaceholder = dialog.findViewById(R.id.kundli_house);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getColor(R.color.no_change_white));
        shape.setCornerRadius(0); // No rounded corners
        dynamicPlaceholder.setBackground(shape);
        shape.setStroke(2, getColor(R.color.colorPrimary));

        // Set position (align to parent top-left with margins)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        double diagonalLength = (double) displayMetrics.widthPixels /2; // Width in pixels
        Log.e("viewCheck", "setUpEastKundliHouseHelpView: toolbar: "+ toolBar_OutputKundli.getHeight()+" tablayout: "+tabs_input_kundli.getHeight());

        int viewSize = (int)Math.sqrt((diagonalLength*diagonalLength)/2);
        int topMargin =  toolBar_OutputKundli.getHeight()+tabs_input_kundli.getHeight()+(int)diagonalLength+60;
        params.setMargins(0,topMargin , 0, 0); // Left, Top, Right, Bottom

        params.width = (viewSize) ; // Set width in pixels
        params.height = (viewSize); // Set height equal to width for a square
        dynamicPlaceholder.setLayoutParams(params);

        // Rotate the square to look like a diamond
        dynamicPlaceholder.setRotation(45);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        TextView laText = new TextView(this);
        laText.setText(getResources().getStringArray(R.array.VarChartPlanets)[12]);
        laText.setTextColor(getColor(R.color.no_change_black));
        laText.setTextSize(20);

        TextView laNumText = new TextView(this);
        laNumText.setText("25");
        laNumText.setTextColor(getColor(R.color.no_change_black));
        laNumText.setTextSize(10);

//        TextView keText = new TextView(this);
//        keText.setText("Ke*");
//        keText.setTextColor(Color.WHITE);
//        keText.setTextSize(20);

        TextView textView = new TextView(this);
        textView.setText("6");
        textView.setTextColor(getColor(R.color.colorPrimary_day_night));
        textView.setTextSize(20);


        textParams.leftMargin = 200;
        textParams.topMargin = 100;
        textParams.addRule(RelativeLayout.ALIGN_START);
        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        laText.setLayoutParams(textParams);
        laText.setRotation(-45);
        dynamicPlaceholder.addView(laText);

        textParams.leftMargin = 200;
        textParams.topMargin = 80;
        textParams.addRule(RelativeLayout.ALIGN_START);
        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        laNumText.setLayoutParams(textParams);
        laNumText.setRotation(-45);
        dynamicPlaceholder.addView(laNumText);

//        textParams.rightMargin =300;
//        textParams.bottomMargin = 0;
//        textParams.addRule(RelativeLayout.ALIGN_END);
//        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
//        keText.setLayoutParams(textParams);
//        keText.setRotation(-45);
//        dynamicPlaceholder.addView(keText);


        textParams.leftMargin = 200;
        textParams.topMargin = 200;
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        // Apply LayoutParams to TextView
        textView.setLayoutParams(textParams);
        textView.setRotation(-45);

        dynamicPlaceholder.addView(textView);


    }

    private void setUpSouthKundliHouseHelpView() {
        FrameLayout dynamicPlaceholder = dialog.findViewById(R.id.east_kundli_house);
        View arrowView = dialog.findViewById(R.id.east_arrow_right);
        View arrowTextView = dialog.findViewById(R.id.east_kundliHouseHelpText);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );


        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getColor(R.color.no_change_white));
        shape.setCornerRadius(0); // No rounded corners
        dynamicPlaceholder.setBackground(shape);
        shape.setStroke(2, getColor(R.color.colorPrimary));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int viewSize = displayMetrics.widthPixels/4; // Width in pixels
        int topMargin =  toolBar_OutputKundli.getHeight()+tabs_input_kundli.getHeight()+viewSize;

        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.setMargins(0,topMargin , 0, 0); // Left, Top, Right, Bottom
        params.width = (viewSize) ;
        params.height = (viewSize);
        dynamicPlaceholder.setLayoutParams(params);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        TextView laText = new TextView(this);
        laText.setText(getResources().getStringArray(R.array.VarChartPlanets)[12]);
        laText.setTextColor(getColor(R.color.no_change_black));
        laText.setTextSize(20);

        TextView laNumText = new TextView(this);
        laNumText.setText("25");
        laNumText.setTextColor(getColor(R.color.no_change_black));
        laNumText.setTextSize(10);


        TextView textView = new TextView(this);
        textView.setText("6");
        textView.setTextColor(getColor(R.color.colorPrimary_day_night));
        textView.setTextSize(20);


        textParams.leftMargin = 200;
        textParams.topMargin = 100;
        textParams.addRule(RelativeLayout.ALIGN_START);
        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        laText.setLayoutParams(textParams);
        dynamicPlaceholder.addView(laText);

        textParams.leftMargin = 200;
        textParams.topMargin = 80;
        textParams.addRule(RelativeLayout.ALIGN_START);
        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        laNumText.setLayoutParams(textParams);
        dynamicPlaceholder.addView(laNumText);


        textParams.leftMargin = 200;
        textParams.topMargin = 200;
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(textParams);
        dynamicPlaceholder.addView(textView);

        RelativeLayout.LayoutParams arrowViewParams = new RelativeLayout.LayoutParams(
                CUtils.convertDpToPx(this,32),
                CUtils.convertDpToPx(this,32)
        );

        RelativeLayout.LayoutParams arrowTextViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        /**this is view handling for arrow ImageView pointing east Kundli house view*/
        arrowViewParams.setMargins(0,topMargin + (viewSize/3) , CUtils.convertDpToPx(this,10), 0); // Left, Top, Right, Bottom
        arrowViewParams.addRule(RelativeLayout.LEFT_OF, dynamicPlaceholder.getId());
        arrowView.setLayoutParams(arrowViewParams);
        /**this is view handling for text  pointing east Kundli house view*/
        arrowTextViewParams.setMargins(0,topMargin + (viewSize/3)+10 , CUtils.convertDpToPx(this,5), 0); // Left, Top, Right, Bottom
        arrowTextViewParams.addRule(RelativeLayout.LEFT_OF, arrowView.getId());
        arrowTextView.setLayoutParams(arrowTextViewParams);


    }

    private void setUpEastKundliHouseHelpView() {
        // Set up the main diamond placeholder
        FrameLayout dynamicPlaceholder = dialog.findViewById(R.id.east_kundli_house);
        View arrowView = dialog.findViewById(R.id.east_arrow_right);
        View arrowTextView = dialog.findViewById(R.id.east_kundliHouseHelpText);

        /**Kundli house view adjustment by this params*/
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getColor(R.color.no_change_white));
        shape.setCornerRadius(0); // No rounded corners
        dynamicPlaceholder.setBackground(shape);
        shape.setStroke(2, getColor(R.color.colorPrimary));

        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int viewSize = displayMetrics.widthPixels / 3; // Width in pixels
        int topMargin = toolBar_OutputKundli.getHeight() + tabs_input_kundli.getHeight() + viewSize;
        params.setMargins(0, topMargin, 0, 0); // Left, Top, Right, Bottom
        params.width = (viewSize); // Set width in pixels
        params.height = (viewSize); // Set height equal to width for a square
        dynamicPlaceholder.setLayoutParams(params);


        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        TextView laText = new TextView(this);
        laText.setText(getResources().getStringArray(R.array.VarChartPlanets)[12]);
        laText.setTextColor(getColor(R.color.no_change_black));
        laText.setTextSize(20);

        TextView laNumText = new TextView(this);
        laNumText.setText("25");
        laNumText.setTextColor(getColor(R.color.no_change_black));
        laNumText.setTextSize(10);

        TextView textView = new TextView(this);
        textView.setText("6");
        textView.setTextColor(getColor(R.color.colorPrimary_day_night));
        textView.setTextSize(20);


        textParams.leftMargin = 200;
        textParams.topMargin = 100;
        textParams.addRule(RelativeLayout.ALIGN_START);
        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        laText.setLayoutParams(textParams);
        dynamicPlaceholder.addView(laText);

        textParams.leftMargin = 200;
        textParams.topMargin = 80;
        textParams.addRule(RelativeLayout.ALIGN_START);
        textParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        laNumText.setLayoutParams(textParams);
        dynamicPlaceholder.addView(laNumText);


        textParams.leftMargin = 200;
        textParams.topMargin = 200;
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        // Apply LayoutParams to TextView
        textView.setLayoutParams(textParams);
        dynamicPlaceholder.addView(textView);

        RelativeLayout.LayoutParams arrowViewParams = new RelativeLayout.LayoutParams(
                CUtils.convertDpToPx(this,32),
                CUtils.convertDpToPx(this,32)
        );

        RelativeLayout.LayoutParams arrowTextViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );



        /**this is view handling for arrow ImageView pointing east Kundli house view*/
        arrowViewParams.setMargins(0,topMargin + (viewSize/3) , CUtils.convertDpToPx(this,10), 0); // Left, Top, Right, Bottom
        arrowViewParams.addRule(RelativeLayout.LEFT_OF, dynamicPlaceholder.getId());
        arrowView.setLayoutParams(arrowViewParams);

        /**this is view handling for text  pointing east Kundli house view*/
        arrowTextViewParams.setMargins(0,topMargin + (viewSize/3)+10, CUtils.convertDpToPx(this,5), 0); // Left, Top, Right, Bottom
        arrowTextViewParams.addRule(RelativeLayout.LEFT_OF, arrowView.getId());
        arrowTextView.setLayoutParams(arrowTextViewParams);


    }

    private void addOptionHelp(View view) {

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.llHelp1);

        int additionalValue = 0;

        double density = getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            additionalValue = additionalValue + 100;
            //xxxhdpi
        } else if (density >= 3.5 && density < 4.0) {
            additionalValue = additionalValue + 100;
        } else if (density >= 3.0 && density < 3.5) {
            additionalValue = additionalValue + 15;
        } else if (density >= 2.0 && density < 3.0) {
            additionalValue = additionalValue + 5;
            //xhdpi
        } else if (density >= 1.5 && density < 2.0) {
            additionalValue = additionalValue + 5;
            //hdpi
        } else if (density >= 1.0 && density < 1.5) {
            additionalValue = additionalValue + 5;
            //mdpi
        }
        //additionalValue=(int)getResources().getDimension(R.dimen.helP_top_marging);

        // int width = (int)
        // ((HomeInputScreen)getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams
                .setMargins(
                        0,
                        additionalValue,
                        0,
                        0);

        layout.setLayoutParams(layoutParams);

    }

    /**
     * This function is used to set module and screen id in history to use on
     * back key.
     *
     * @param moduleIndex
     * @param screenIndex
     * @author Bijendra(05 - 06 - 14)
     */
    private void setScreenInfoInBackKeyStack(int moduleIndex, int screenIndex) {
        try {


            int pos = 0;
            if (isAdTitleHaseBeenGivenTOTabs && screenIndex > localAdvertismentPosition) {
                pos = screenIndex - 1;
            } else {
                pos = screenIndex;
            }
            // //Log.e("BACK KEY STACK ADDED",
            // "MODULE "+String.valueOf(moduleIndex)+"  SCREEN ID "+String.valueOf(screenIndex));
                /*CScreenHistoryItemCollectionStack.getScreenHistoryItemCollection(
                        OutputMasterActivity.this).addScreenInHistory(moduleIndex,
                        screenIndex);*/
            if (isAdTitleHaseBeenGivenTOTabs && (screenIndex == localAdvertismentPosition)) {
                //Do not save advertisment
            } else {
                CScreenHistoryItemCollectionStack.getScreenHistoryItemCollection(
                        OutputMasterActivity.this).addScreenInHistory(moduleIndex,
                        pos);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }// ADDED BY BIJENDRA ON 27-05-14

    }

    /**
     * This function is used to navigate module according to passed module index
     * ADDED BY BIJENDRA ON 05-06-14
     */
    @Override
    public void moduleNavigate(int moduleIndex) {
        // TODO Auto-generated method stub

        //Log.e("SAN Module", String.valueOf(moduleIndex));

        if (moduleIndex != SELECTED_MODULE) {

            setScreenInfoInBackKeyStack(previoueModuleIndex,
                    previoueSubScreenIndex);// ADDED BY BIJENDRA ON 05-06-14

            SELECTED_MODULE = moduleIndex;
            // ADDED BY BIJENDRA ON 19-05-15
            SELECTED_SUB_SCREEN = 1;
            // UPDATED BY BIJENDRA ON 22-05-15:ADDED SHODASHVARGA ALSO
            if (SELECTED_MODULE == MODULE_PREDICTION
                    || SELECTED_MODULE == MODULE_SHODASHVARGA)
                SELECTED_SUB_SCREEN = 0;
            else
                testBoolean = true;

            // END

            previoueSubScreenIndex = SELECTED_SUB_SCREEN;// ADDED BY BIJENDRA ON
            // 05-06-14
            previoueModuleIndex = SELECTED_MODULE;// ADDED BY BIJENDRA ON
            // 05-06-14

            pageTitles = getSubMenuSpinnerItems(SELECTED_MODULE);
            setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);

            setActionBarNavigation(); // ADDED BY DEEPAK ON 15-12-2014
            // CODE FOR VARSHAPHAL MODULE
            if ((SELECTED_MODULE == MODULE_VARSHAPHAL)) {
                testBoolean = true;
               /* new CCalculateTajikVarshphalSync(CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR)
                        .execute();*/
                calculateTajikVarshphal(CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR);
            }

            // CODE FOR PREDICTION MODULE
            if (SELECTED_MODULE == MODULE_PREDICTION) {
                // //Log.e("MODULE_PREDICTION", String.valueOf(moduleIndex));
                if (CUtils
                        .isUserAllowingToShowHindiTextMessage(OutputMasterActivity.this)) {
                    if ((CUtils
                            .getLanguageCodeFromPreference(getApplicationContext()) == CGlobalVariables.HINDI)
                            && (!SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE)) {
                        showHindiSupportMessage();
                    }
                }
            }
            //initializeOutputModuleMenu();// ADDED BY BIJENDRA ON 05-06-14
            initializeBookMarks();// ADDED BY BIJENDRA ON 05-06-14
            // viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);// ADDED BY
            setCurrentView(SELECTED_SUB_SCREEN, true);
            // BIJENDRA ON
            // 19-05-15
        }

        //getSlidingMenu().showContent();

    }

    public void moduleNavigate(int moduleIndex, int subModuleIndex) {
        // TODO Auto-generated method stub

        //Log.e("SAN Module", String.valueOf(moduleIndex));

        if (moduleIndex != SELECTED_MODULE) {

            setScreenInfoInBackKeyStack(previoueModuleIndex,
                    previoueSubScreenIndex);// ADDED BY BIJENDRA ON 05-06-14

            SELECTED_MODULE = moduleIndex;
            // ADDED BY BIJENDRA ON 19-05-15
            SELECTED_SUB_SCREEN = 1;
            // UPDATED BY BIJENDRA ON 22-05-15:ADDED SHODASHVARGA ALSO
            if (SELECTED_MODULE == MODULE_PREDICTION
                    || SELECTED_MODULE == MODULE_SHODASHVARGA)
                SELECTED_SUB_SCREEN = subModuleIndex;
            else
                testBoolean = true;

            // END

            previoueSubScreenIndex = SELECTED_SUB_SCREEN;// ADDED BY BIJENDRA ON
            // 05-06-14
            previoueModuleIndex = SELECTED_MODULE;// ADDED BY BIJENDRA ON
            // 05-06-14

            pageTitles = getSubMenuSpinnerItems(SELECTED_MODULE);
            setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);

            setActionBarNavigation(); // ADDED BY DEEPAK ON 15-12-2014
            // CODE FOR VARSHAPHAL MODULE
            if ((SELECTED_MODULE == MODULE_VARSHAPHAL)) {
                testBoolean = true;
               /* new CCalculateTajikVarshphalSync(CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR)
                        .execute();*/
                calculateTajikVarshphal(CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR);
            }

            // CODE FOR PREDICTION MODULE
            if (SELECTED_MODULE == MODULE_PREDICTION) {
                // //Log.e("MODULE_PREDICTION", String.valueOf(moduleIndex));
                if (CUtils
                        .isUserAllowingToShowHindiTextMessage(OutputMasterActivity.this)) {
                    if ((CUtils
                            .getLanguageCodeFromPreference(getApplicationContext()) == CGlobalVariables.HINDI)
                            && (!SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE)) {
                        showHindiSupportMessage();
                    }
                }
            }
            //initializeOutputModuleMenu();// ADDED BY BIJENDRA ON 05-06-14
            initializeBookMarks();// ADDED BY BIJENDRA ON 05-06-14
            // viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);// ADDED BY
            setCurrentView(SELECTED_SUB_SCREEN, true);
            // BIJENDRA ON
            // 19-05-15
        }

        //getSlidingMenu().showContent();

    }


    public void moduleNavigate(int moduleIndex, String selectedText) {
        int subModuleIndex = 0;

        // //Log.e("Module", String.valueOf(moduleIndex));
        if (moduleIndex != SELECTED_MODULE) {
            testBoolean = true;
            SELECTED_MODULE = moduleIndex;
            setScreenInfoInBackKeyStack(previoueModuleIndex,
                    previoueSubScreenIndex);// ADDED BY BIJENDRA ON 05-06-14
            pageTitles = getSubMenuSpinnerItems(SELECTED_MODULE);
            subModuleIndex = Arrays.asList(pageTitles).indexOf(selectedText);

            // ADDED BY BIJENDRA ON 19-05-15
            SELECTED_SUB_SCREEN = subModuleIndex;

            previoueSubScreenIndex = SELECTED_SUB_SCREEN;// ADDED BY BIJENDRA ON
            // 05-06-14
            previoueModuleIndex = SELECTED_MODULE;// ADDED BY BIJENDRA ON
            // 05-06-14


            setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);

            setActionBarNavigation(); // ADDED BY DEEPAK ON 15-12-2014

            // CODE FOR PREDICTION MODULE
            if (SELECTED_MODULE == MODULE_PREDICTION) {
                // //Log.e("MODULE_PREDICTION", String.valueOf(moduleIndex));
                if (CUtils
                        .isUserAllowingToShowHindiTextMessage(OutputMasterActivity.this)) {
                    if ((CUtils
                            .getLanguageCodeFromPreference(getApplicationContext()) == CGlobalVariables.HINDI)
                            && (!SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE)) {
                        showHindiSupportMessage();
                    }
                }
            }

            initializeBookMarks();
            setCurrentView(SELECTED_SUB_SCREEN, true);

        }


    }


    /**
     * This function is used to initialize module menu for output screen. ADDED
     * BY BIJENDRA ON 05-06-14
     */
	/*private void initializeOutputModuleMenu() {
		// Primary menu
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.menu_frame,
						ModuleMenuFragment.newInstance(SELECTED_MODULE))
				.commit();
	}*/

    /*
     * This function is used to show year kundli for Tajik/Lalkitab after
     * changing year. ADDED BY BIJENDRA ON 05-06-14
     */
    @Override
    void selectedInputYear(int inputyear) {
        // TODO Auto-generated method stub
        // //Log.e("inputyear", String.valueOf(inputyear));
        if (SELECTED_MODULE == MODULE_LALKITAB) {
            LALKITAB_INPUT_YEAR = inputyear;
            if (LALKITAB_INPUT_YEAR == -1)
                LALKITAB_INPUT_YEAR = super.getPresentYearNumber();

        }
        if ((SELECTED_MODULE == MODULE_VARSHAPHAL)
                || (SELECTED_MODULE == MODULE_PREDICTION)) {
            VARSHPHAL_INPUT_YEAR = inputyear;
            if (VARSHPHAL_INPUT_YEAR == -1)
                VARSHPHAL_INPUT_YEAR = super.getPresentYearNumber();

        }
        if ((SELECTED_MODULE == MODULE_PREDICTION)) {
            setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);
            // viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
            setCurrentView(SELECTED_SUB_SCREEN, true);
        }
        if ((SELECTED_MODULE == MODULE_VARSHAPHAL)) {
           /* new CCalculateTajikVarshphalSync(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR)
                    .execute();*/
            calculateTajikVarshphal(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR);
        }
        if ((SELECTED_MODULE == MODULE_LALKITAB)) {
            setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);
            // viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
            setCurrentView(SELECTED_SUB_SCREEN, true);
        }

    }

    private void initializeBookMarks() {

        int pos = 0;
        if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN > localAdvertismentPosition) {
            pos = SELECTED_SUB_SCREEN - 1;
        } else {
            pos = SELECTED_SUB_SCREEN;
        }

        if (isAdTitleHaseBeenGivenTOTabs && localAdvertismentPosition == SELECTED_SUB_SCREEN) {
            action_bookmark_Menu.setVisibility(GONE);
        } else {
            action_bookmark_Menu.setVisibility(View.VISIBLE);
            setBookMarkIcon(pos);
            OutputMasterActivity.super.setHistoryScreen(pos);
        }
    }


    private void openSelectedBookMarkScreen(int group, int child) {
        // ADDED BY BIJENDRA ON 21-06-14
        isScreenOpenedFromBookMarkList = true;
        setScreenInfoInBackKeyStack(SELECTED_MODULE, previoueSubScreenIndex);
        // END

        try {
            if (group == 0) {
                goToBookMarkHistoryScreen(
                        CBookMarkItemCollection
                                .getBookMarkItemCollection(OutputMasterActivity.this).UserBookMarkedScreen
                                .get(child).ModuleId,
                        CBookMarkItemCollection
                                .getBookMarkItemCollection(OutputMasterActivity.this).UserBookMarkedScreen
                                .get(child).ScreenId);
            } else if (group == 1) {
                {
                    // //Log.e("Bijendra", "group == 1");
                    goToBookMarkHistoryScreen(
                            CScreenHistoryItemCollection
                                    .getScreenHistoryItemCollection(
                                            OutputMasterActivity.this)
                                    .getHistory().get(child).ModuleId,
                            CScreenHistoryItemCollection
                                    .getScreenHistoryItemCollection(
                                            OutputMasterActivity.this)
                                    .getHistory().get(child).ScreenId);
                }
            }
        } catch (Exception e) {

        }
    }

    public void goToBookMarkHistoryScreen(int moduleId, int screenId) {
        // //Log.e("Bijendra-1",
        // String.valueOf(moduleId)+"  "+String.valueOf(screenId));
       /*else{
            screenId = screenId;
        }*/

        SELECTED_SUB_SCREEN = screenId;

        if (moduleId == SELECTED_MODULE) {
            SELECTED_MODULE = moduleId;
            // //Log.e("Bijendra-1-Same",
            // String.valueOf(moduleId)+"  "+String.valueOf(screenId));
        }

        if (moduleId != SELECTED_MODULE) {
            SELECTED_MODULE = moduleId;
            SELECTED_SUB_SCREEN = screenId;
            // //Log.e("Bijendra-1-Different",
            // String.valueOf(moduleId)+"  "+String.valueOf(screenId));

            pageTitles = getSubMenuSpinnerItems(SELECTED_MODULE);
            setViewPagerAdapter(SELECTED_MODULE, isVarshphalDataReadyToShow);

            /*
             * if(SELECTED_MODULE!=CGlobalVariables.MODULE_DASA)
             * setActionBarNavigation();
             *
             * if(SELECTED_MODULE==CGlobalVariables.MODULE_DASA)//HIDE ACTIONBAR
             * SPINNER ON DASA SELECTION {
             * getSupportActionBar().setNavigationMode
             * (ActionBar.NAVIGATION_MODE_STANDARD);
             * getSupportActionBar().setDisplayShowTitleEnabled(false); }
             */// DISABLED BY DEEPAK ON 25-12-2014
            setActionBarNavigation(); // ADDED BY DEEPAK ON 25-12-2014
            // CODE FOR VARSHAPHAL MODULE
            if ((SELECTED_MODULE == MODULE_VARSHAPHAL)) {
               /* new CCalculateTajikVarshphalSync(CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR)
                        .execute();*/
                calculateTajikVarshphal(CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject(), VARSHPHAL_INPUT_YEAR);
            }

            // CODE FOR PREDICTION MODULE
            if (SELECTED_MODULE == MODULE_PREDICTION) {
                // //Log.e("MODULE_PREDICTION", String.valueOf(moduleIndex));
                if (CUtils
                        .isUserAllowingToShowHindiTextMessage(OutputMasterActivity.this)) {
                    if ((CUtils
                            .getLanguageCodeFromPreference(getApplicationContext()) == CGlobalVariables.HINDI)
                            && (!SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE)) {
                        showHindiSupportMessage();
                    }
                }
            }
        }

        if (isAdTitleHaseBeenGivenTOTabs && SELECTED_SUB_SCREEN >= localAdvertismentPosition) {
            screenId = screenId + 1;
            SELECTED_SUB_SCREEN = screenId;
        }

        // viewPager.setCurrentItem(SELECTED_SUB_SCREEN, true);
        setCurrentView(SELECTED_SUB_SCREEN, true);

    }

	/*private void openBookMarkList() {
		startActivityForResult(new Intent(OutputMasterActivity.this,
				ActBookmarkAndHistory.class), BOOK_MARK_LIST_ACTIVITY_CODE);
	}*/

    @Override
    public void onSetectedItemCategoryPrediction(int index) {
        // TODO Auto-generated method stub
        // viewPager.setCurrentItem(index + 1, true);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSetectedItemCategoryMiscellaneous(int index) {
        // TODO Auto-generated method stub
        // viewPager.setCurrentItem(index + 1, true);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSetectedItemCategoryBasic(int index) {
        // TODO Auto-generated method stub
        // viewPager.setCurrentItem(index + 1, true);
        //Log.e("SAN ", " OMA  isAdTitleHaseBeenGivenTOTabs " + isAdTitleHaseBeenGivenTOTabs);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSetectedItemCategoryKP(int index) {
        // TODO Auto-generated method stub
        // viewPager.setCurrentItem(index + 1, true);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSetectedItemCategoryShodashvarga(int index) {
        // TODO Auto-generated method stub
        //viewPager.setCurrentItem(index + 1, true);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSetectedItemCategoryLalkitab(int index) {
        // TODO Auto-generated method stub
        // viewPager.setCurrentItem(index + 1, true);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSetectedItemCategoryVarshphal(int index) {
        // TODO Auto-generated method stub
        // viewPager.setCurrentItem(index + 1, true);
        setCurrentView(index + 1, true);
    }

    @Override
    public void onSelectedCategoryBoardItem(int moduleIndex) {
        // TODO Auto-generated method stub
        testBoolean = true;
        // TODO Auto-generated method stub
        // tejinder now testBoolean=true;
        moduleNavigate(moduleIndex);

    }

    public void onSelectedCategoryBoardItem(int moduleIndex, int subModule) {
        testBoolean = true;
        moduleNavigate(moduleIndex, subModule);

    }

	/*@Override
	public void goToLogin() {
		if (!CUtils.isUserLogedIn(OutputMasterActivity.this)) {

			Intent intent = new Intent(OutputMasterActivity.this,
					ActLogin.class);
			intent.putExtra("callerActivity",
					CGlobalVariables.HOME_INPUT_SCREEN);
			startActivityForResult(intent,
					HomeInputScreen.SUB_ACTIVITY_USER_LOGIN);
		} else {
			kundliOutputMenuFrag.updateLoginDetials(false, "", "");
			MyCustomToast mct = new MyCustomToast(OutputMasterActivity.this,
					OutputMasterActivity.this.getLayoutInflater(),
					OutputMasterActivity.this, typeface);
			mct.show(getResources().getString(R.string.sign_out_success));

		}
		//getSlidingMenu().showContent();

	}*/

    List<String> getDrawerListItem() {

        try {
            String[] menuItems2 = getResources().getStringArray(R.array.module_list);
            return Arrays.asList(menuItems2);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }

    }

    List<Drawable> getDrawerListItemIcon() {

        try {
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.module_icons);
            return CUtils.convertTypedArrayToArrayList(OutputMasterActivity.this, itemsIcon2, module_list_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }

    }

    List<Integer> getDrawerListItemIndex() {
        try {
            //return CUtils.getDrawerListItemIndex(OutputMasterActivity.this, app_home_menu_item_list_index, module_list_index);
            return Arrays.asList(module_list_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

	/*@Override
	protected void onRestart() {
		super.onRestart();
		try {

			drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(),getDrawerListItemIndex());
		} catch (Exception ex) {
			//Log.i(ex.getMessage().toString());
		}

	}*/

    public void updateMenus() {

        try {
            drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }

    }

    @Override
    public void newKundli() {
        showInterstetialAdv(false);
    }

    @Override
    public void setEditKundli() {
        showInterstetialAdv(true);
    }

    @Override
    public void openKundli() {
        gotoOpenKundli();
    }

    @Override
    public void downloadPDF() {
        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF, GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_SIDEMENU);
        String labell = CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;// + "_" + GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_SIDEMENU;
        //CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, ""); //now trigger from addFacebookAndFirebaseEvent
        com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,labell,GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_SIDEMENU);
        sharePDF(false);
    }

    public void showCustomDatePickerDialogAboveHoneyComb(int year) {
        try {
            final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {

                @Override
                public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    int yearDiff = 0, year2 = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .getDateTime().getYear();

                    if (checkYearInput(year)) {

                        yearDiff = year - year2;
                        onSelectedInputYear(yearDiff);
                    }
                }

            };


            final MyDatePickerDialog dg = new MyDatePickerDialog(OutputMasterActivity.this, R.style.AppCompatAlertDialogStyle, myDateSetListener, 1, 1, year, false);
            dg.setCanceledOnTouchOutside(false);
            //mTimePicker.setTitle("hello");
            // dg.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
            dg.setTitle("");
            //if device is tablet than i do not need to set DatePicker and Time Picker to be match Parent
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (!tabletSize) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dg.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dg.show();
                dg.getWindow().setAttributes(lp);
            } else {
                dg.show();
            }

            try {
                //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                if (SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    dg.getWindow().setBackgroundDrawableResource(android.R.color.white);
                }
                int divierId = dg.getContext().getResources()
                        .getIdentifier("android:id/titleDivider", null, null);
                View divider = dg.findViewById(divierId);
                divider.setVisibility(GONE);

            } catch (Exception e) {
                //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
            }

            Button butOK = (Button) dg.findViewById(android.R.id.button1);
            Button butCancel = (Button) dg.findViewById(android.R.id.button2);
            butOK.setText(R.string.set);
            butCancel.setText(R.string.cancel);

            try {
                com.ojassoft.astrosage.utils.NumberPicker date = (com.ojassoft.astrosage.utils.NumberPicker) dg.findViewById(R.id.date);
                date.setVisibility(GONE);
                com.ojassoft.astrosage.utils.NumberPicker month = (com.ojassoft.astrosage.utils.NumberPicker) dg.findViewById(R.id.month);
                month.setVisibility(GONE);
            } catch (Exception ex) {
                //Log.i(ex.getMessage().toString());
            }

            butOK.setTypeface(regularTypeface);
            butCancel.setTypeface(regularTypeface);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }

    }

    public void initMonthPicker(int year) {

        // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
        if (SDK_INT == 24 || SDK_INT == 25) {
            showCustomDatePickerDialogAboveHoneyComb(year);
            return;
        }
        /*@ Tejinder Singh
         * on 2-aug-2016
         * due to problem in below lollipop phone show month and day in calendar but we do not need it
         * so going to change it*/
        try {
            final DatePickerDialog dg = new DatePickerDialog(OutputMasterActivity.this, mDateSetListener,
                    year, 10,
                    01);

            dg.setTitle("");
            /*This Code for set DatePicker Width full*/
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dg.getWindow().getAttributes());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            dg.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            lp.width = (int) (displayMetrics.widthPixels * 0.70);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dg.getWindow().setAttributes(lp);
            dg.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.bg_card_view_color)));
            dg.show();

            //This code for API 6 date Picker Color not like holo
            CUtils.applyStyLing(dg, OutputMasterActivity.this);


            Button butOK = (Button) dg.findViewById(android.R.id.button1);
            Button butCancel = (Button) dg.findViewById(android.R.id.button2);
            butOK.setText(R.string.set);
            butCancel.setText(R.string.cancel);

            butOK.setTypeface(regularTypeface);
            butCancel.setTypeface(regularTypeface);

            CUtils.setCustomDatePickerEdittext(dg);
            if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = dg.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(GONE);
                    }
                }

                int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                if (monthSpinnerId != 0) {
                    View monthSpinner = dg.findViewById(monthSpinnerId);
                    if (monthSpinner != null) {
                        monthSpinner.setVisibility(GONE);
                    }
                }

                int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
                if (yearSpinnerId != 0) {
                    View yearSpinner = dg.findViewById(yearSpinnerId);
                    if (yearSpinner != null) {
                        yearSpinner.setVisibility(View.VISIBLE);

                    }
                }
            } else { //Older SDK versions
                dg.setTitle("");
                Field f[] = dg.getClass().getDeclaredFields();
                for (Field field : f) {
                    if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                        field.setAccessible(true);
                        Object dayPicker = null;
                        try {
                            dayPicker = field.get(dg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(GONE);
                    }

                    if (field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner")) {
                        field.setAccessible(true);
                        Object monthPicker = null;
                        try {
                            monthPicker = field.get(dg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) monthPicker).setVisibility(GONE);
                    }

                    if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) {
                        field.setAccessible(true);
                        Object yearPicker = null;
                        try {
                            yearPicker = field.get(dg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) yearPicker).setVisibility(View.VISIBLE);

                    }
                }
            }


            dg.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // DatePicker datePicker = dg.getDatePicker();

                    try {
                        DatePicker datePicker = null;
                        try {
                            Field mDatePickerField = dg.getClass().getDeclaredField("mDatePicker");
                            mDatePickerField.setAccessible(true);
                            datePicker = (DatePicker) mDatePickerField.get(dg);
                        } catch (Exception ex) {
                            //
                        }
                        // The following clear focus did the trick of saving the date while the date is put manually by the edit text.
                        datePicker.clearFocus();
                        mDateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                    } catch (Exception ex) {
                        //
                    }
                }
            });
        } catch (Exception e) {
            //android.util.//Log.e("Excption", "initMonthPicker: " + e.getMessage());
        }

    }
    public void showYearPicker(int year) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.year_picker_dialog);
        dialog.setCancelable(true);

        NumberPicker picker = dialog.findViewById(R.id.numberPickerYear);
        TextView ok = dialog.findViewById(R.id.btnOk);
        TextView cancel = dialog.findViewById(R.id.btnCancel);

        // int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        picker.setMinValue(1900);
        picker.setMaxValue(2100);
        picker.setValue(year);
        picker.setWrapSelectorWheel(false);


        ok.setOnClickListener(v -> {
            int selectedYear = picker.getValue();
            int yearDiff = 0, year2 = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear();

            if (checkYearInput(selectedYear)) {

                yearDiff = selectedYear - year2;
                onSelectedInputYear(yearDiff);
            }
            Log.d("YEAR", String.valueOf(selectedYear));
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
        /*This Code for set DatePicker Width full*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lp.width = (int) (displayMetrics.widthPixels * 0.70);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }

    private boolean checkYearInput(int year) {
        int userInputYear = -1;
        int birthYear = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getDateTime().getYear();

        boolean _isValid = true;
        // THIS FUNCTION IS UPDATED ON 3-SEP-13(BIJENDRA)
        try {
            userInputYear = year;
        } catch (Exception e) {
            _isValid = true;

        }
        if (!_isValid) {
            Toast.makeText(OutputMasterActivity.this, "Please enter valid year", Toast.LENGTH_SHORT).show();
            // _ebtYear.setError("Please enter valid year");
            _isValid = false;
        }

        if (_isValid) {
            int diff = userInputYear - birthYear;
            if (diff < 0) {
                // Toast.makeText(OutputMasterActivity.this, "Year can not less than birth year", Toast.LENGTH_SHORT).show();
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, regularTypeface);
                mct.show(getResources().getString(R.string.text_year_not_less_than_birth_year));
                // _ebtYear.setError("Year can not less than birth year");
                _isValid = false;
            }
            if (diff > 119) {
                Toast.makeText(OutputMasterActivity.this, "Please enter valid year", Toast.LENGTH_SHORT).show();
                // _ebtYear.setError("Please enter valid year");
                _isValid = false;
            }

        }
        /*
         * if(_ebtYear.getText().toString().trim().length()==0 ||
         * _ebtYear.getText().toString().trim().length()<4 || userInputYear <
         * _birthYear) { _ebtYear.setError("Please enter valid year");
         * _isValid=false; } if(_ebtYear.getText().toString().trim().length()==0
         * || _ebtYear.getText().toString().trim().length()<4 || userInputYear <
         * _birthYear) { _ebtYear.setError("Please enter valid year");
         * _isValid=false; }
         */
        return _isValid;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            int yearDiff = 0, year2 = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear();

            if (checkYearInput(year)) {

                yearDiff = year - year2;
                onSelectedInputYear(yearDiff);
            }
        }
    };

    private void openSaveKundliPopup() {
        CUtils.googleAnalyticSendWitPlayServie(
                this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.signin_screen_from_output_activity,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.signin_screen_from_output_activity, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("OpenSaveChartPopup");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        SaveKundliOnCloudDialog saveKundliOnCloudDialog = SaveKundliOnCloudDialog.getInstance();
        //SaveKundliOnCloudDialog.setTargetFragment(SearchBirthDetailsFragment.this, 0);
        saveKundliOnCloudDialog.show(fm, "OpenSaveChartPopup");
        ft.commit();
    }

    /*private void saveKundliOnserver(final String msg, final boolean isNotesNeedToOpen) {
        new AsyncTask<String, Long, Void>() {
            long[] onlineServerArray = new long[2];
            boolean isKundliSaved = true;
            ControllerManager controllerManager = new ControllerManager();
            BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject();
            CustomProgressDialog pd = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new CustomProgressDialog(OutputMasterActivity.this, regularTypeface);
                pd.show();
            }

            @Override
            protected Void doInBackground(String... arg0) {
                try {
                    onlineServerArray = controllerManager
                            .saveChartOnServer(beanHoroPersonalInfo, CUtils
                                    .getUserName(OutputMasterActivity.this), CUtils
                                    .getUserPassword(OutputMasterActivity.this));
                } catch (Exception e) {
                    isKundliSaved = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {

                    pd.dismiss();
                    if (isKundliSaved) {
                        beanHoroPersonalInfo.setOnlineChartId(String.valueOf(onlineServerArray[0]));
                        controllerManager.addEditHoroPersonalInfoOperation(OutputMasterActivity.this, beanHoroPersonalInfo);
                        CUtils.saveKundliInPreference(OutputMasterActivity.this, beanHoroPersonalInfo);
                        if ((onlineServerArray[1] == 2 || onlineServerArray[1] == 6) && !(CUtils
                                .getDoNotShowAccountUpgradePopupValueInPreference(OutputMasterActivity.this))) {
                            CUtils.gotoshowCompletedFreeChartPlanScreen(
                                    OutputMasterActivity.this, LANGUAGE_CODE,
                                    SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, (int) onlineServerArray[1], msg);


                        } else if (onlineServerArray[1] == 4) {
                            MyCustomToast mct = new MyCustomToast(OutputMasterActivity.this,
                                    OutputMasterActivity.this.getLayoutInflater(),
                                    OutputMasterActivity.this, regularTypeface);
                            mct.show(getResources().getString(R.string.chart_not_saved_on_server));
                        } else if (onlineServerArray[1] == 1)//added by neeraj for char saved msg 8/6/16
                        {
                            MyCustomToast mct = new MyCustomToast(OutputMasterActivity.this,
                                    OutputMasterActivity.this.getLayoutInflater(),
                                    OutputMasterActivity.this, regularTypeface);
                            mct.show(getResources().getString(R.string.chart_saved_on_server));

                            callOutputMasterActivityAfterLogin(isNotesNeedToOpen);
                        } else if (onlineServerArray[1] == 3) {
                            callOutputMasterActivityAfterLogin(isNotesNeedToOpen);
                        }
                    }
                } catch (Exception e) {
                }


            }
        }.execute();
    }*/

    public void setDataAfterPurchasePlan(boolean purchaseSilverPlan, String screenId) {

        if (purchaseSilverPlan) {

            //08-feb-2016 work for which screen will be open
            //String ScreenId = data.getStringExtra("ScreenId");
            CUtils.gotoProductPlanListUpdated(
                    OutputMasterActivity.this,
                    LANGUAGE_CODE,
                    HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE, screenId, "output_master_activity");

        }// END ON 22-12-2014
        else {
            /*
            CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), false, OutputMasterActivity.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
            kundli.new CCalculateOnlineDataSync(
                    CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject(),
                    CUtils.isConnectedWithInternet(getApplicationContext()))
                    .execute();*/
        }
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimim value
     * @param max Maximim value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @author Amit Rautela
     * @see Random#nextInt(int)
     */
    private static int getRandomNumberForAdvertisePosition(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private String[] getElementWithAdvertisement(String[] element) {

        //Add 1 extra title for Advertisemnet
        String[] returnElement = null;
        /*if (!silverPlanPriceMonth.equals("")) {

            returnElement = new String[element.length + 1];
            boolean isTitleAdded = false;
            int j = 1;
            for (int i = 0; i < element.length; i++) {
                if (i == localAdvertismentPosition) {//&& !silverPlanPriceMonth.equals("")
                    returnElement[i] = getResources().getString(R.string.featured);
                    returnElement[j] = element[i];
                    isTitleAdded = true;
                    isAdTitleHaseBeenGivenTOTabs = true;
                } else if (isTitleAdded) {
                    returnElement[j] = element[i];
                } else {
                    returnElement[i] = element[i];
                }
                j++;
            }
        } else {
            returnElement = element;
        }*/
        if (!goldPlanPriceMonth.equals("") || !platinumPlanPriceMonth.equals("")) {

            returnElement = new String[element.length + 1];
            boolean isTitleAdded = false;
            int j = 1;
            for (int i = 0; i < element.length; i++) {
                if (i == localAdvertismentPosition) {//&& !goldPlanPriceMonth.equals("")
                    Resources resources = getLocaleResources(this, getLanguageKey(LANGUAGE_CODE));
                    returnElement[i] = resources.getString(R.string.featured);
                    returnElement[j] = element[i];
                    isTitleAdded = true;
                    isAdTitleHaseBeenGivenTOTabs = true;
                } else if (isTitleAdded) {
                    returnElement[j] = element[i];
                } else {
                    returnElement[i] = element[i];
                }
                j++;
            }
        } else {
            returnElement = element;
        }

        return returnElement;
    }


    class AsyncProductDetailFromGoogleServer extends
            AsyncTask<String, Long, Void> {
        boolean isSuccess = true;

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                fetchProductFromGoogleServer();
            } catch (Exception e) {
                isSuccess = false;
                //android.util.//Log.e("Exception IS: ", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // //Log.e("Bijendra", "onPreExecute");

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (isSuccess) {
                //showProductsDetail();
            }

        }

    }

    private void fetchProductFromGoogleServer() {

        try {

            QueryProductDetailsParams queryProductDetailsParams =
                    QueryProductDetailsParams.newBuilder().setProductList(
                            ImmutableList.of(

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_SILVER_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_GOLD_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_SILVER_PLAN_MONTH)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_GOLD_PLAN_MONTH)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_PLATINUM_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_PLATINUM_PLAN_MONTH)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_PLATINUM_PLAN_MONTH_OMF)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_PLATINUM_PLAN_YEAR_OMF)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build()
                            )).build();

            AstrosageKundliApplication.billingClient.queryProductDetailsAsync(
                    queryProductDetailsParams, (billingResult, productDetailsList) -> {
                        int response = billingResult.getResponseCode();
                        Log.e("BillingClient", "onProductDetailsResponse() response=" + response);
                        if (response == BillingClient.BillingResponseCode.OK) {
                            for (ProductDetails productDetails : productDetailsList) {
                                intiProductPlan(productDetails);
                            }
                            CUtils.setCloudPlanList((ArrayList<ProductDetails>) productDetailsList);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        showProductsDetail();
                                    } catch (Exception e) {
                                        //
                                    }
                                }
                            });
                        } else {
                            //showMsg(response);
                        }
                    }
            );
        } catch (Exception e) {
//
        }

    }

    private void intiProductPlan(ProductDetails skuDetails) {
        try {

            ProductDetails.PricingPhase priceDetails = skuDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0);
            //Log.e("BillingClient", priceDetails.getFormattedPrice() + "intiProductPlan() SKU_GOLD_PLAN_YEAR=" + skuDetails.getProductId());
            if (enableMonthlySubscriptionValue) {
                if (skuDetails.getProductId().equalsIgnoreCase(SKU_SILVER_PLAN_MONTH)) {
                    araySilverPlanMonth.add(skuDetails.getProductId());// 0
                    araySilverPlanMonth.add(skuDetails.getTitle());// 1
                    araySilverPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                    araySilverPlanMonth.add(skuDetails.getDescription());// 3
                    araySilverPlanMonth.add(skuDetails.getProductType());// 4
                    araySilverPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                    araySilverPlanMonth.add(priceDetails.getPriceCurrencyCode());
                    skuDetailsSilverPlanMonth = skuDetails;
                } else if (skuDetails.getProductId().equalsIgnoreCase(SKU_GOLD_PLAN_MONTH)) {
                    arayGoldPlanMonth.add(skuDetails.getProductId());// 0
                    arayGoldPlanMonth.add(skuDetails.getTitle());// 1
                    arayGoldPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                    arayGoldPlanMonth.add(skuDetails.getDescription());// 3
                    arayGoldPlanMonth.add(skuDetails.getProductType());// 4
                    arayGoldPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                    arayGoldPlanMonth.add(priceDetails.getPriceCurrencyCode());
                    skuDetailsGoldPlanMonth = skuDetails;
                } else if (skuDetails.getProductId().equalsIgnoreCase(SKU_PLATINUM_PLAN_MONTH)) {
                    arayPlatinumPlanMonth.add(skuDetails.getProductId());// 0
                    arayPlatinumPlanMonth.add(skuDetails.getTitle());// 1
                    arayPlatinumPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                    arayPlatinumPlanMonth.add(skuDetails.getDescription());// 3
                    arayPlatinumPlanMonth.add(skuDetails.getProductType());// 4
                    arayPlatinumPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                    arayPlatinumPlanMonth.add(priceDetails.getPriceCurrencyCode());
                    skuDetailsPlatinumPlanMonth = skuDetails;
                }
            } else {
//arraylist is same for yearly and monthly
                if (skuDetails.getProductId().equalsIgnoreCase(SKU_SILVER_PLAN_YEAR)) {
                    araySilverPlanMonth.add(skuDetails.getProductId());// 0
                    araySilverPlanMonth.add(skuDetails.getTitle());// 1
                    araySilverPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                    araySilverPlanMonth.add(skuDetails.getDescription());// 3
                    araySilverPlanMonth.add(skuDetails.getProductType());// 4
                    araySilverPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                    araySilverPlanMonth.add(priceDetails.getPriceCurrencyCode());
                    skuDetailsSilverPlanMonth = skuDetails;
                } else if (skuDetails.getProductId().equalsIgnoreCase(SKU_GOLD_PLAN_YEAR)) {
                    arayGoldPlanMonth.add(skuDetails.getProductId());// 0
                    arayGoldPlanMonth.add(skuDetails.getTitle());// 1
                    arayGoldPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                    arayGoldPlanMonth.add(skuDetails.getDescription());// 3
                    arayGoldPlanMonth.add(skuDetails.getProductType());// 4
                    arayGoldPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                    arayGoldPlanMonth.add(priceDetails.getPriceCurrencyCode());
                    skuDetailsGoldPlanMonth = skuDetails;
                } else if (skuDetails.getProductId().equalsIgnoreCase(SKU_PLATINUM_PLAN_YEAR)) {
                    arayPlatinumPlanMonth.add(skuDetails.getProductId());// 0
                    arayPlatinumPlanMonth.add(skuDetails.getTitle());// 1
                    arayPlatinumPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                    arayPlatinumPlanMonth.add(skuDetails.getDescription());// 3
                    arayPlatinumPlanMonth.add(skuDetails.getProductType());// 4
                    arayPlatinumPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                    arayPlatinumPlanMonth.add(priceDetails.getPriceCurrencyCode());
                    skuDetailsPlatinumPlanMonth = skuDetails;
                }
            }


        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }


    private void showMsg(final int response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response >= 0 && response < 9) {
                        Toast.makeText(OutputMasterActivity.this, errorResponse[response], Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OutputMasterActivity.this, getResources().getString(R.string.something_wrong_error), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });

    }

    private void showProductsDetail() {
        String year = "/" + getResources().getString(R.string.year);
        /*String yearinHindi = "/वर्ष";
        String yearinTamil = "/வருடம்";*/
        String rupeesInTamil = "ரூ.";
        String month = "/" + getResources().getString(R.string.month);
        boolean hasPrice = false;
        String monthYearText = "";


        //INIT SILVER PLAN MONTH
        if (araySilverPlanMonth.size() > 0) {

            if (enableMonthlySubscriptionValue) {
                monthYearText = month;
            } else {
                monthYearText = year;
            }

            if (LANGUAGE_CODE == 1) {

                if (SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    silverPlanPriceMonth = PriceCalculator(araySilverPlanMonth.get(PRICE)
                            .replace("Rs.", "\u20B9")) + monthYearText;
                } else {
                    silverPlanPriceMonth = PriceCalculator(araySilverPlanMonth.get(PRICE))
                            + monthYearText;
                }

            } else if (LANGUAGE_CODE == 2) {

                silverPlanPriceMonth = PriceCalculator(araySilverPlanMonth.get(PRICE)
                        .replace("Rs.", rupeesInTamil)) + monthYearText;
            } else {

                silverPlanPriceMonth = PriceCalculator(araySilverPlanMonth.get(PRICE))
                        + monthYearText;
            }
            hasPrice = true;
        }
        //INIT GOLD PLAN MONTH
        if (arayGoldPlanMonth.size() > 0) {

            if (enableMonthlySubscriptionValue) {
                monthYearText = month;
            } else {
                monthYearText = year;
            }

            if (LANGUAGE_CODE == 1) {

                if (SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    goldPlanPriceMonth = PriceCalculator(arayGoldPlanMonth.get(PRICE)
                            .replace("Rs.", "\u20B9")) + monthYearText;
                } else {
                    goldPlanPriceMonth = PriceCalculator(arayGoldPlanMonth.get(PRICE))
                            + monthYearText;
                }

            } else if (LANGUAGE_CODE == 2) {

                goldPlanPriceMonth = PriceCalculator(arayGoldPlanMonth.get(PRICE)
                        .replace("Rs.", rupeesInTamil)) + monthYearText;
            } else {

                goldPlanPriceMonth = PriceCalculator(arayGoldPlanMonth.get(PRICE))
                        + monthYearText;
            }
            hasPrice = true;
        }
//INIT PLATINUM PLAN MONTH
        if (arayPlatinumPlanMonth.size() > 0) {

            if (enableMonthlySubscriptionValue) {
                monthYearText = month;
            } else {
                monthYearText = year;
            }

            if (LANGUAGE_CODE == 1) {

                if (SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    platinumPlanPriceMonth = PriceCalculator(arayPlatinumPlanMonth.get(PRICE)
                            .replace("Rs.", "\u20B9")) + monthYearText;
                } else {
                    platinumPlanPriceMonth = PriceCalculator(arayPlatinumPlanMonth.get(PRICE))
                            + monthYearText;
                }

            } else if (LANGUAGE_CODE == 2) {

                platinumPlanPriceMonth = PriceCalculator(arayPlatinumPlanMonth.get(PRICE)
                        .replace("Rs.", rupeesInTamil)) + monthYearText;
            } else {

                platinumPlanPriceMonth = PriceCalculator(arayPlatinumPlanMonth.get(PRICE))
                        + monthYearText;
            }
            hasPrice = true;
        }
        if (hasPrice) {
        }
        // initFragmentAdapter();
    }

    public String PriceCalculator(String strvalue) {
        return strvalue;
    }

    @Override
    public void selectedPlan(int planIndex, UPIAppModel upiAppModel) {
        boolean isEmailIdVisible = true;
        boolean isPhnNumbVisisble = true;

        String astroShopEmail = CUtils.getAstroshopUserEmail(OutputMasterActivity.this);
        String askAQuesEmail = CUtils.getStringData(OutputMasterActivity.this, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, "");
        String askAQuesMobNo = CUtils.getStringData(OutputMasterActivity.this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, "");
        String emailId = UserEmailFetcher.getEmail(getApplicationContext());
        if (!TextUtils.isEmpty(askAQuesEmail) || !TextUtils.isEmpty(astroShopEmail) || !TextUtils.isEmpty(emailId)) {
            isEmailIdVisible = false;
        }
        if (!TextUtils.isEmpty(askAQuesMobNo)) {
            isPhnNumbVisisble = false;
        }
        //If both email id and phone number is available
        if (!isEmailIdVisible && !isPhnNumbVisisble) {
            //gotBuySilverMonthlyPlan();
            //gotBuyGoldMonthlyPlan();
            gotBuyPlatinumMonthlyPlan();

        } else {
            CUtils.openPersonalDetailsDialog(this, planIndex, isEmailIdVisible, isPhnNumbVisisble);
        }
    }


    public void gotBuySilverMonthlyPlan() {
        try {
            if (skuDetailsSilverPlanMonth != null) {
                requestCode = SUB_RC_REQUEST_SILVER_PLAN_MONTH;
                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsSilverPlanMonth)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

// Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(OutputMasterActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
// TODO Auto-generated catch block
//android.util.//Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    public void gotBuyGoldMonthlyPlan() {
        try {
            if (skuDetailsGoldPlanMonth != null) {
                requestCode = SUB_RC_REQUEST_GOLD_PLAN_MONTH;
                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsGoldPlanMonth)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

// Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(OutputMasterActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
// TODO Auto-generated catch block
//android.util.//Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    public void gotBuyPlatinumMonthlyPlan() {
        try {
            if (skuDetailsPlatinumPlanMonth != null) {
                requestCode = SUB_RC_REQUEST_PLATINUM_PLAN_MONTH;
                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsPlatinumPlanMonth)
                                        .setOfferToken(skuDetailsPlatinumPlanMonth.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();
// Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(OutputMasterActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
//Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    private String calculatePayloadKey(String planTyle) {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(planTyle);
        sb.append(String.valueOf(calendar.getTime().getTime()));

        return sb.toString();
    }

    private void gotoThanksPage(Purchase purchase, String plan, String price, String priceCurrencycode, String freetrialperiodtxt) {
        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();
        //
        getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).edit()
                .putString("VALUE", purchaseData).commit();// ADDED BY BIJENDRA
        // ON 16-06-15

        //Track records
        //sendGoogleAnalytics(purchaseData,plan);

        Intent tppsIntent = new Intent(getApplicationContext(),
                ThanksProductPurchaseScreen.class);
        tppsIntent.putExtra("SIGNATURE", signature);
        tppsIntent.putExtra("PURCHASE_DATA", purchaseData);
        tppsIntent.putExtra("PLAN", plan);
        tppsIntent.putExtra("price", price);
        tppsIntent.putExtra("priceCurrencycode", priceCurrencycode);
        tppsIntent.putExtra("freetrialperiod", freetrialperiodtxt);
        startActivity(tppsIntent);
        this.finish();

    }

    private void setCurrentView(int index, boolean smoothScroll) {
        try {
            viewPager.setCurrentItem(index, smoothScroll);
            if (tabs_input_kundli != null) {
                setAlpha(index, tabs_input_kundli);
            }
            tabs_input_kundli.getTabAt(index).select();
            animateTextInfinite();
        } catch (Exception e){
            //
        }
    }

    private void setAlpha(int position, TabLayout tabLayout) {
        try {
            if (SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    TextView textView = (TextView) view.findViewById(R.id.tabtext);
                    if (position == i) {
                        textView.setAlpha(1F);
                    } else {
                        textView.setAlpha(0.5F);
                    }
                }
            }
        } catch (Exception ex) {
            //android.util.//Log.i("Exception",ex.getMessage());
        }
    }

    private void saveNotes(String onlineChartid) {
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("opensavenotesdialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        String userId = CUtils.getUserName(OutputMasterActivity.this);
        String password = CUtils.getUserPassword(OutputMasterActivity.this);
        NotesDialogFragment notesDialogFragment = NotesDialogFragment.getInstance(onlineChartid, notes, userId, password);
        notesDialogFragment.show(fm, "opensavenotesdialog");
        ft.commit();*/
        String userId = CUtils.getUserName(OutputMasterActivity.this);
        String password = CUtils.getUserPassword(OutputMasterActivity.this);
        Intent intent = new Intent(this, Notes.class);
        intent.putExtra("notes", notes);
        intent.putExtra("onlineChartId", onlineChartid);
        intent.putExtra("userId", userId);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void openNotesDialog() {
        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_SAVE_NOTES, null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SAVE_NOTES, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
        if (beanHoroPersonalInfo != null) {
            String onlineChartid = beanHoroPersonalInfo.getOnlineChartId();
            if (CUtils.isUserLogedIn(OutputMasterActivity.this)) {
                if (!onlineChartid.equals("") && !onlineChartid.equals("-1")) {
                    //  if (CUtils.getUserPurchasedPlanFromPreference(OutputMasterActivity.this) != CGlobalVariables.BASIC_PLAN_ID) {

                    saveNotes(onlineChartid);

                   /* } else {
                        boolean boolVal = (CUtils.getDoNotShowAccountUpgradePopupValueInPreference(OutputMasterActivity.this));
                        if (!boolVal) {
                            CUtils.gotoshowCompletedFreeChartPlanScreen(
                                    OutputMasterActivity.this, LANGUAGE_CODE,
                                    SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, 0, getResources().getString(R.string.save_notes_error));
                        }
                    }*/

                } else {
                    saveKundliOnserver(getResources().getString(R.string.save_notes_error), true);
                    //saveKundliOnserver1(getResources().getString(R.string.save_notes_error), true);
                }
            } else {
                //Intent intent = new Intent(OutputMasterActivity.this, ActLogin.class);
                Intent intent = new Intent(OutputMasterActivity.this, LoginSignUpActivity.class);
                intent.putExtra(IS_FROM_SCREEN, BASE_INPUT_SCREEN);
                startActivityForResult(intent, NOTES_REQUEST_CODE);

            }
        }
    }

    // to get url for share when user click on share chart icon on menu item
    public void shareChart() {
        String chartId = CGlobal.getCGlobalObject()
                .getHoroPersonalInfoObject().getOnlineChartId();
        if (CUtils.isUserLogedIn(this)) {
            new ShareBirthChart(OutputMasterActivity.this, chartId, LANGUAGE_CODE);
        } else {
            //Intent loginAct = new Intent(this, ActLogin.class);
            Intent loginAct = new Intent(OutputMasterActivity.this, LoginSignUpActivity.class);
            loginAct.putExtra(IS_FROM_SCREEN, BASE_INPUT_SCREEN);
            startActivityForResult(loginAct, SUB_ACTIVITY_USER_LOGIN_FOR_SHARE);

        }

    }

    // this method is used to calculate click count of dynamic horscope notification measurment

    private void dynamicHorscopeNotificationMeasurment(boolean fromNotification, int predictionType, String dynamicText) {
        if (fromNotification && predictionType == CGlobalVariables.SADE_SATI_TYPE) {
            CUtils.googleAnalyticsNotification(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DASHA_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_SADE_SATI,
                    null);
        } else if (fromNotification && predictionType == CGlobalVariables.ANTER_DASHA_TYPE) {
            CUtils.googleAnalyticsNotification(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DASHA_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_ANTER_DASHA,
                    null);
        } else if (fromNotification && predictionType == CGlobalVariables.MAHA_DASHA_TYPE) {
            CUtils.googleAnalyticsNotification(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_NOTIFICATION_DASHA_CLICK,
                    CGlobalVariables.GOOGLE_ANALYTIC_MAHA_DASHA,
                    null);

        }
    }

    private void openShareOptionDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("ChooseShareOption");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChooseShareOption clfd = new ChooseShareOption();
        clfd.show(fm, "ChooseShareOption");
        ft.commit();
    }

    public void sharePDF(boolean isPdfShare) {
        this.isPdfShare = isPdfShare;


        if (!CUtils.isConnectedWithInternet(this)) {
            MyCustomToast mct = new MyCustomToast(this, this
                    .getLayoutInflater(), this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
            return;
        }

        pd = new CustomProgressDialog(OutputMasterActivity.this, regularTypeface);
        pd.setCancelable(false);
        pd.show();
        /*boolean IS_NORTH_CHART = false;
        if (chart_Style == 0 || chart_Style == 2)
            IS_NORTH_CHART = true;*/

        Intent intent = new Intent(this, DownloadPdfService.class);
        intent.putExtra(KEY_PDF_SHARE, isPdfShare);
        intent.putExtra(KEY_CHART_STYLE, chart_Style);
        intent.putExtra(KEY_URL, CUtils.getPdfUrl(OutputMasterActivity.this, chart_Style, LANGUAGE_CODE) + "&time=" + (new Date().getTime()));
        intent.putExtra(KEY_RECEIVER, new DownloadPdfReceiver(OutputMasterActivity.this, new Handler()));

        startService(intent);
    }

    public void cancelProgressDialog() {
        hideProgressBar();
    }

    public void cancelDownloadProgressDialog() {
        if (downloadPDFFrag != null) {
            downloadPDFFrag.cancelProgressDialog();
        }
        if (reportsFragment != null) {
            reportsFragment.cancelProgressDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_STORAGE_FOR_SHARE_SCREEN) {
                boolean isPermissionGranted = false;
                if (grantResults.length == 1) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                } else if (grantResults.length == 2) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                }

                if (isPermissionGranted) {
                    screenShare();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PERMISSION_STORAGE_FOR_SHARE_PDF) {
                boolean isPermissionGranted = false;
                if (grantResults.length == 1) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                } else if (grantResults.length == 2) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                }
                if (isPermissionGranted) {
                    sharePDF(isPdfShare);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PERMISSION_LOCATION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        }
    }

    private void calculateTajikVarshphal(BeanHoroPersonalInfo beanHoroPersonalInfo, int VARSHPHAL_INPUT_YEAR) {
        //new CCalculateTajikVarshphalSync(beanHoroPersonalInfo, VARSHPHAL_INPUT_YEAR).execute();
        CCalculateTajikVarshphalSync(beanHoroPersonalInfo, VARSHPHAL_INPUT_YEAR);
    }

    private void CCalculateTajikVarshphalSync(BeanHoroPersonalInfo _birthDetail, int varshphalYear) {
        showProgressBar();
        CGlobal.getCGlobalObject().getBeanTajikVarshaphal().setYearNumber(String.valueOf(varshphalYear));
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = CGlobalVariables.TAJIK_VARSHAPHAL_URL;
        //Log.e("SAN LoadMore URL ", url);
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParams(_birthDetail, String.valueOf(varshphalYear)), VARSHFAL_CALCULATION).getMyStringRequest();
        queue.add(stringRequest);
    }


    public Map<String, String> getParams(BeanHoroPersonalInfo _birthDetail, String varshphalYear) {

        String _name = _birthDetail.getName().trim().replace(' ', '_');
        String _mycity = _birthDetail.getPlace().getCityName().trim();
        _mycity = _mycity.substring(0, 3);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", _name);
        params.put("sex", _birthDetail.getGender().trim());
        params.put("day", String.valueOf(_birthDetail.getDateTime().getDay()).trim());
        params.put("month", String.valueOf(_birthDetail.getDateTime().getMonth() + 1).trim());
        params.put("year", String.valueOf(_birthDetail.getDateTime().getYear()).trim());
        params.put("hrs", String.valueOf(_birthDetail.getDateTime().getHour()).trim());
        params.put("min", String.valueOf(_birthDetail.getDateTime().getMin()).trim());
        params.put("sec", String.valueOf(_birthDetail.getDateTime().getSecond()));
        params.put("dst", String.valueOf(_birthDetail.getDST()));
        params.put("place", _birthDetail.getPlace().getCityName().trim());
        params.put("longdeg", _birthDetail.getPlace().getLongDeg().trim());
        params.put("longmin", _birthDetail.getPlace().getLongMin().trim());
        params.put("longew", _birthDetail.getPlace().getLongDir().trim());
        params.put("latdeg", _birthDetail.getPlace().getLatDeg().trim());
        params.put("latmin", _birthDetail.getPlace().getLatMin().trim());
        params.put("latns", _birthDetail.getPlace().getLatDir().trim());
        params.put("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue()));
        params.put("ayanamsa", String.valueOf(_birthDetail.getAyanIndex()).trim());
        params.put("kphn", String.valueOf(_birthDetail.getHoraryNumber()));

        int calVarshphalYear = _birthDetail.getDateTime().getYear() + Integer.valueOf(varshphalYear);
        params.put("varshphalyear", String.valueOf(calVarshphalYear));
        params.put("charting", "0");
        params.put("button1", "Get+Kundali");
        params.put("languageCode", "0");


        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        //Log.e("SAN ", "OMA Response " + response);
        if (method == VARSHFAL_CALCULATION) {
            //Log.e("LoadMore URL ", response);
            initValuesForMyKundli(response);
            setViewPagerAdapter(SELECTED_MODULE, (!isVarshphalDataReadyToShow));
            setCurrentView(SELECTED_SUB_SCREEN, false);
        } else if (method == SAVE_KUNDLI) {
            doActionAfterSaveKundli(response);
        } else if (method == GET_TIME_ZONE) {
            if (!TextUtils.isEmpty(response)) {
                //Log.e("SAN ", "OMA Response " + response);
                showTimeZone(response);
            }
        }

    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    private void initValuesForMyKundli(String strData) {
        PlanetData _planetDegree = new PlanetData();
        double _dValues = 0.0;
        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Lagna>")
                + "<Lagna>".length(), strData.indexOf("</Lagna>")));
        if (_dValues >= 360.00)
            _dValues -= 360.00;
        _planetDegree.setLagna(_dValues);

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Sun>")
                + "<Sun>".length(), strData.indexOf("</Sun>")));
        _planetDegree.setSun(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Moon>")
                + "<Moon>".length(), strData.indexOf("</Moon>")));
        _planetDegree.setMoon(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Mars>")
                + "<Mars>".length(), strData.indexOf("</Mars>")));
        _planetDegree.setMarsh(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(
                strData.indexOf("<Mercury>") + "<Mercury>".length(),
                strData.indexOf("</Mercury>")));
        _planetDegree.setMercury(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(
                strData.indexOf("<Jupiter>") + "<Jupiter>".length(),
                strData.indexOf("</Jupiter>")));
        _planetDegree.setJup(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Venus>")
                + "<Venus>".length(), strData.indexOf("</Venus>")));
        _planetDegree.setVenus(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Saturn>")
                + "<Saturn>".length(), strData.indexOf("</Saturn>")));
        _planetDegree.setSat(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Rahu>")
                + "<Rahu>".length(), strData.indexOf("</Rahu>")));
        _planetDegree.setRahu(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Ketu>")
                + "<Ketu>".length(), strData.indexOf("</Ketu>")));
        _planetDegree.setKetu(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Uranus>")
                + "<Uranus>".length(), strData.indexOf("</Uranus>")));
        _planetDegree.setUranus(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(
                strData.indexOf("<Neptune>") + "<Neptune>".length(),
                strData.indexOf("</Neptune>")));
        _planetDegree.setNeptune(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Pluto>")
                + "<Pluto>".length(), strData.indexOf("</Pluto>")));
        _planetDegree.setPluto(Double.valueOf(_dValues));
        _dValues = Double.valueOf(strData.substring(strData.indexOf("<MunthaInBhav>")
                + "<MunthaInBhav>".length(), strData.indexOf("</MunthaInBhav>")));

        //muntha=(int) _dValues;
        CGlobal.getCGlobalObject().getBeanTajikVarshaphal().setMuntha(String.valueOf((int) _dValues));

        CGlobal.getCGlobalObject().getBeanTajikVarshaphal().setPlanetData(_planetDegree);
		/*CGlobal.getCGlobalObject().getTajikVarshaphal().setPlanetData(CGlobal.getCGlobalObject().getPlanetDataObject());
		CGlobal.getCGlobalObject().getTajikVarshaphal().setMuntha("5");*/

    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(OutputMasterActivity.this, regularTypeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
        }
    }

    String msg;
    boolean isNotesNeedToOpen;

    private void saveKundliOnserver(final String msg, final boolean isNotesNeedToOpen) {
        showProgressBar();
        this.isNotesNeedToOpen = isNotesNeedToOpen;
        this.msg = msg;
        BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject()
                .getHoroPersonalInfoObject();
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = CGlobalVariables.ONLINE_CHART_SAVE_URL;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParams(beanHoroPersonalInfo), SAVE_KUNDLI).getMyStringRequest();
        queue.add(stringRequest);
    }

    public Map<String, String> getParams(BeanHoroPersonalInfo _birthDetail) {

        String _name = _birthDetail.getName().trim();
       /* String _mycity = _birthDetail.getPlace().getCityName().trim();
        _mycity = _mycity.substring(0, 3);*/

        HashMap<String, String> params = new HashMap<String, String>();
        //params.put("name", _name);

        String _city = _birthDetail.getPlace().getCityName().trim();

        params.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(OutputMasterActivity.this)));
        params.put(CGlobalVariables.KEY_PASSWORD, CUtils.getUserPassword(OutputMasterActivity.this));

        params.put("Name", _name);
        params.put("Sex", _birthDetail.getGender().trim());
        params.put("Day", String.valueOf(_birthDetail.getDateTime().getDay()).trim());
        params.put("Month", String.valueOf(_birthDetail.getDateTime().getMonth() + 1).trim());
        params.put("Year", String.valueOf(_birthDetail.getDateTime().getYear()).trim());
        params.put("Hrs", String.valueOf(_birthDetail.getDateTime().getHour()).trim());
        params.put("Min", String.valueOf(_birthDetail.getDateTime().getMin()).trim());
        params.put("Sec", String.valueOf(_birthDetail.getDateTime().getSecond()).trim());
        params.put("Place", _city);
        params.put("LongDeg", _birthDetail.getPlace().getLongDeg().trim());
        params.put("LongMin", _birthDetail.getPlace().getLongMin().trim());
        params.put("LongEW", _birthDetail.getPlace().getLongDir().trim());
        params.put("LatDeg", _birthDetail.getPlace().getLatDeg().trim());
        params.put("LatMin", _birthDetail.getPlace().getLatMin().trim());
        params.put("LatNS", _birthDetail.getPlace().getLatDir().trim());
        params.put("Ayanamsa", String.valueOf(_birthDetail.getAyanIndex()).trim());
        params.put("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue()).trim());
        params.put("DST", String.valueOf(_birthDetail.getDST()).trim());
        params.put("Isapi", "1");
        if (_birthDetail.getOnlineChartId().trim().equals("-1")) {
            params.put("ChartId", "");
        } else {
            params.put("ChartId", _birthDetail.getOnlineChartId().trim());
        }
        params.put("kphn", String.valueOf(_birthDetail.getHoraryNumber()));

        return params;
    }

    private void doActionAfterSaveKundli(String response) {
        try {

            hideProgressBar();
            BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
            long[] onlineServerArray = getSaveOnlineChartId(response, null);

            beanHoroPersonalInfo.setOnlineChartId(String.valueOf(onlineServerArray[0]));
            new ControllerManager().addEditHoroPersonalInfoOperation(OutputMasterActivity.this, beanHoroPersonalInfo);
            CUtils.saveKundliInPreference(OutputMasterActivity.this, beanHoroPersonalInfo);
            if ((onlineServerArray[1] == 2 || onlineServerArray[1] == 6) ) {
                CUtils.gotoshowCompletedFreeChartPlanScreen(
                        OutputMasterActivity.this, LANGUAGE_CODE,
                        SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, (int) onlineServerArray[1], msg);


            } else if (onlineServerArray[1] == 4) {
                MyCustomToast mct = new MyCustomToast(OutputMasterActivity.this,
                        OutputMasterActivity.this.getLayoutInflater(),
                        OutputMasterActivity.this, regularTypeface);
                mct.show(getResources().getString(R.string.chart_not_saved_on_server));
            } else if (onlineServerArray[1] == 1)//added by neeraj for char saved msg 8/6/16
            {
                MyCustomToast mct = new MyCustomToast(OutputMasterActivity.this,
                        OutputMasterActivity.this.getLayoutInflater(),
                        OutputMasterActivity.this, regularTypeface);
                mct.show(getResources().getString(R.string.chart_saved_on_server));

                callOutputMasterActivityAfterLogin(isNotesNeedToOpen);
            } else if (onlineServerArray[1] == 3) {
                callOutputMasterActivityAfterLogin(isNotesNeedToOpen);
            }

        } catch (Exception e) {

        }
    }

    public long[] getSaveOnlineChartId(String chartSavedId, String updatedOnlineChartId) throws Exception {

        long _chartId = -1;
        long _msgcode = -1;
        long[] char_msg = new long[2];
        try {
            Document doc = CXMLOperations.XMLfromString(chartSavedId);
            int numResults = doc.getChildNodes().getLength();

            if ((numResults <= 0)) {
                _chartId = -1;
                char_msg[0] = _chartId;
                char_msg[1] = _msgcode;

            } else {
                if (SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                    try {
                        XPath xpathChartId = XPathFactory.newInstance()
                                .newXPath();
                        String chartIdExp = "/product/chartid"; // chart id
                        NodeList chartIdNodes = (NodeList) xpathChartId
                                .evaluate(chartIdExp, doc, XPathConstants.NODESET);
                        _chartId = Long.valueOf(chartIdNodes.item(0).getTextContent());

                        String msgcodeExp = "/product/msgcode"; // msgcode
                        NodeList msgcodeNodes = (NodeList) xpathChartId
                                .evaluate(msgcodeExp, doc, XPathConstants.NODESET);
                        _msgcode = Long.valueOf(msgcodeNodes.item(0).getTextContent());

                        char_msg[0] = _chartId;
                        char_msg[1] = _msgcode;
                    } catch (Exception e) {
                        XPath xpathMsgId = XPathFactory.newInstance().newXPath();
                        String msgCodeExp = "/product/msgcode"; // message code
                        NodeList msgcodeNodes = (NodeList) xpathMsgId.evaluate(msgCodeExp, doc, XPathConstants.NODESET);
                        _msgcode = Long.valueOf(msgcodeNodes.item(0).getTextContent());
                        if ((updatedOnlineChartId != null) && (_msgcode == 3)
                                || (_msgcode == 4) || (_msgcode == 5)) {
                            char_msg[0] = Long.valueOf(updatedOnlineChartId);
                        } else if ((_msgcode == 2) || (_msgcode == 0)) {
                            char_msg[0] = _chartId;
                        }
                        char_msg[1] = _msgcode;
                    }

                } else {
                    NodeList nodes = doc.getElementsByTagName("product");

                    try {
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element e1 = (Element) nodes.item(i);
                            _chartId = Long.valueOf(CXMLOperations.getValue(e1, "chartid"));
                        }
                        for (int j = 0; j < nodes.getLength(); j++) {
                            Element e2 = (Element) nodes.item(j);
                            _msgcode = Long.valueOf(CXMLOperations.getValue(e2, "msgcode"));
                        }
                        char_msg[0] = _chartId;
                        char_msg[1] = _msgcode;
                    } catch (Exception e) {
                        // THIS CODE IS ADDED BY BIJENDRA ON 28-NOV-2012
                        // DESCRIPTION:This code is run when the chart saving
                        // account is full on server
                        // this code is written in catch because xml values
                        // changed so it throw exception
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element eMsg = (Element) nodes.item(i);
                            _msgcode = Long.valueOf(CXMLOperations.getValue(eMsg, "msgcode"));
                        }
                        if ((updatedOnlineChartId != null) && (_msgcode == 3)
                                || (_msgcode == 4) || (_msgcode == 5)) {
                            char_msg[0] = Long.valueOf(updatedOnlineChartId);
                        } else if ((_msgcode == 2) || (_msgcode == 0)) {
                            char_msg[0] = _chartId;
                        }
                        char_msg[1] = _msgcode;
                        // END
                    }

                }
            }

        } catch (Exception e) {
            throw e;
        }
        return char_msg;
    }

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actapp_nav_home:
                            Intent intent = new Intent(OutputMasterActivity.this, ActAppModule.class);
                            startActivity(intent);
                            return true;
                        case R.id.actapp_nav_call:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(OutputMasterActivity.this, OUTPUT_MASTER_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, OutputMasterActivity.this);
                            return true;
                        case R.id.actapp_nav_live:
                            fabActions();
                            return true;
                        case R.id.actapp_nav_chat:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(OutputMasterActivity.this, OUTPUT_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, OutputMasterActivity.this);
                            return true;
                        case R.id.actapp_nav_account:
                            com.ojassoft.astrosage.varta.utils.CUtils.openAccountScreen(OutputMasterActivity.this);
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
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(OutputMasterActivity.this, OUTPUT_MASTER_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(OutputMasterActivity.this, OUTPUT_MASTER_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(OutputMasterActivity.this);
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
                FontUtils.changeFont(OutputMasterActivity.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    private void setBottomNavigationText() {

        // find MenuItem you want to change
        TextView navLive = navView.findViewById(R.id.txtViewLive);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);

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

        // set new title to the MenuItem
        CUtils.handleFabOnActivities(this, fabOutputMaster, navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(OutputMasterActivity.this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        // setting click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
        //navView.getMenu().setGroupCheckable(0,false,true);
    }

    private void setCustomAds() {
        try {
            getData();
            if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
                setTopAdd(topAdData);
            }

            topAdImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (topAdData != null && !topAdData.getImageObj().isEmpty()) {
                        CUtils.googleAnalyticSendWitPlayServie(OutputMasterActivity.this,
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_62_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_62_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        CUtils.createSession(OutputMasterActivity.this, "S62");

                        CustomAddModel modal = topAdData.getImageObj().get(0);
                        CUtils.divertToScreen(OutputMasterActivity.this, modal.getImgthumbnailurl(), LANGUAGE_CODE);
                    }
                }
            });
        } catch (Exception e) {
            //
        }
    }

    private void getData() {

        try {
            String result = CUtils.getStringData(OutputMasterActivity.this, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "62");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTopAdd(AdData topData) {
        getData();
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;

        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (topAdImage != null) {

                topAdImage.setVisibility(GONE);
            }
        } else {

            if (topAdImage != null) {
                topAdImage.setVisibility(View.VISIBLE);
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(OutputMasterActivity.this).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(OutputMasterActivity.this) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(GONE);
            }
        }
    }

    @Override
    public void clickCall() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(OutputMasterActivity.this, OUTPUT_MASTER_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    @Override
    public void clickChat() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(OutputMasterActivity.this, OUTPUT_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    // SAN For SCALE CHANGE
    public void checkFontConfigration() {
        Configuration configuration = getResources().getConfiguration();
        float fontOldSize = configuration.fontScale;
        //Log.e("SAN ", "font size configuration.fontScale " + configuration.fontScale );

        if (fontOldSize > 1.0) {
            //Log.e("SAN ", "fontScale=" + configuration.fontScale);
            //Log.e("SAN ", "font too big. scale down...");
            configuration.fontScale = 1.0f;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getResources().updateConfiguration(configuration, metrics);
            isFontSizeChange = true;
            fontSizeChange = fontOldSize;
        } else {
            //Log.e("SAN ", "font Normal");
        }
    }

    // SAN Exp Location update
    public void getLocation1() {
        try {
            getLocation2();
        } catch (Exception e) {
            //Log.e("SAN ", "OMA getLocation1 exp " + e.toString());
        }

        if (locationMain != null) {
            latitude = locationMain.getLatitude();
            longitude = locationMain.getLongitude();
            //Log.e("SAN ", " OMA getLocation1() lat, Lng " + latitude + "," + longitude );
            setInformation(latitude, longitude);
        } else {
            //Log.e("SAN ", "OMA getLocation1 locationMain != null ");
        }
    }

    public void getLocation2() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //Log.e("SAN ", "OMA getLocation2 gps_enabled " + gps_enabled );
        //Log.e("SAN ", "OMA getLocation2 network_enabled " + network_enabled );

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled)
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null && net_loc != null) {

            //Log.e("SAN ", "OMA getLocation2 1 " );

            //smaller the number more accurate result will
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                locationMain = net_loc;
            else
                locationMain = gps_loc;

        } else {

            //Log.e("SAN ", "OMA getLocation2 2 " );

            if (gps_loc != null) {
                locationMain = gps_loc;
            } else if (net_loc != null) {
                locationMain = net_loc;
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getLocation1();
    }

    private boolean setInformation(double lat, double lon) {
        int tempCalMin = 0;
        double orignal_lat = lat;
        double orignal_lon = lon;
        try {

            if (lat < 0.)
                lat *= -1;
            if (lon < 0.)
                lon *= -1;

            gpsVLatDeg = (String.valueOf((int) lat));
            tempCalMin = ((int) ((lat - (int) lat) * 60));
            if (tempCalMin < 0.)
                tempCalMin *= -1;
            gpsVLatMin = (String.valueOf(tempCalMin));

            tempCalMin = 0;

            gpsVLongDeg = (String.valueOf((int) lon));

            tempCalMin = ((int) ((lon - (int) lon) * 60));
            if (tempCalMin < 0.)
                tempCalMin *= -1;
            gpsVLongMin = (String.valueOf(tempCalMin));

            //Log.e("SAN ", "OMA setInformation() gpsVLatDeg, gpsVLatMin, gpsVLongDeg, gpsVLongMin " + gpsVLatDeg +", "+ gpsVLatMin +", " + gpsVLongDeg +", " + gpsVLongMin  );

            if (CUtils.isConnectedWithInternet(this)) {
                getTimeZoneFromServer(orignal_lat, orignal_lon);
            }
        } catch (Exception e) {
            //Log.e("SAN ", " OMA setInformation() exp " + e.toString() );
        }
        return true;
    }

    private void getTimeZoneFromServer(double lat, double lon) {
        String url = "https://secure.geonames.org/timezone?lat="
                + String.valueOf(lat) + "&lng=" + String.valueOf(lon)
                + "&username="+CUtils.getRandomUsername();
        //Log.e("SAN ", " OMA getTimeZoneFromServer() url " + url );
        LibCUtils.vollyGetRequest(OutputMasterActivity.this, this, url, GET_TIME_ZONE);
    }

    private void showTimeZone(String timezonevalue) {
        try {
            //Log.e("SAN ", " OMA showTimeZone() inside " );
            gpstimezoneString = timezonevalue.substring(
                    timezonevalue.indexOf("<timezoneId>")
                            + "<timezoneId>".length(),
                    timezonevalue.indexOf("</timezoneId>"));
            gpstimezoneString = gpstimezoneString.trim();

            gpstimezoneDST = timezonevalue.substring(
                    timezonevalue.indexOf("<dstOffset>")
                            + "<dstOffset>".length(),
                    timezonevalue.indexOf("</dstOffset>"));
            gpstimezoneDST = gpstimezoneDST.trim();

            gpstimezoneGMT = timezonevalue.substring(
                    timezonevalue.indexOf("<gmtOffset>")
                            + "<gmtOffset>".length(),
                    timezonevalue.indexOf("</gmtOffset>"));
            gpstimezoneGMT = gpstimezoneGMT.trim();

            gpsIsCurrLocFetch = true;

            //Log.e("SAN ", " OMA showTimeZone() gpstimezoneString, gpstimezoneDST, gpstimezoneGMT " + gpstimezoneString + ", " + gpstimezoneDST + ", " + gpstimezoneGMT );
        } catch (Exception e) {
            //Log.e("SAN ", " OMA showTimeZone() exp " + e.toString() );
        }
    }


    private Map<String, String> getQuestionListParams(boolean isLagnaScreen) {

        BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();

        Map<String, String> headers = new HashMap<String, String>();

        headers.put("name", beanHoroPersonalInfo.getName());
        headers.put("sex", beanHoroPersonalInfo.getGender());
        headers.put("day", String.valueOf(beanHoroPersonalInfo.getDateTime().getDay()));
        headers.put("month", String.valueOf(beanHoroPersonalInfo.getDateTime().getMonth()));
        headers.put("year", String.valueOf(beanHoroPersonalInfo.getDateTime().getYear()));
        headers.put("hrs", String.valueOf(beanHoroPersonalInfo.getDateTime().getHour()));
        headers.put("min", String.valueOf(beanHoroPersonalInfo.getDateTime().getMin()));
        headers.put("sec", String.valueOf(beanHoroPersonalInfo.getDateTime().getSecond()));
        headers.put("dst", String.valueOf(beanHoroPersonalInfo.getDST()));
        headers.put("place", String.valueOf(beanHoroPersonalInfo.getPlace().getCityName()));
        headers.put("longdeg", String.valueOf(beanHoroPersonalInfo.getPlace().getLongDeg()));
        headers.put("longmin", String.valueOf(beanHoroPersonalInfo.getPlace().getLongMin()));
        headers.put("longew", String.valueOf(beanHoroPersonalInfo.getPlace().getLongDir()));
        headers.put("latdeg", String.valueOf(beanHoroPersonalInfo.getPlace().getLatDeg()));
        headers.put("latmin", String.valueOf(beanHoroPersonalInfo.getPlace().getLatMin()));
        headers.put("latns", String.valueOf(beanHoroPersonalInfo.getPlace().getLatDir()));
        headers.put("timezone", String.valueOf(beanHoroPersonalInfo.getPlace().getTimeZoneValue()));
        headers.put("charting", "0");
        headers.put("ayanamsa", String.valueOf(beanHoroPersonalInfo.getAyanIndex()));
        headers.put("kphn", String.valueOf(beanHoroPersonalInfo.getHoraryNumber()));
        headers.put("appversion", BuildConfig.VERSION_NAME);
        headers.put("pkgname", ASTROSAGE_AI_PACKAGE_NAME);
        headers.put("lang", String.valueOf(APP_LANG_CODE));
        headers.put("userid", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this) + (com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this)));
        headers.put("androidid", com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(this));

        if (!isLagnaScreen)
            headers.put("moduleid", String.valueOf(SELECTED_MODULE));
        else
            headers.put("moduleid", String.valueOf(99));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        headers.put("methodname", "suggestedquesakmodules");


        return headers;
    }


    public void goToBookMark(int module, int sub_module) {
        try {
            boolean addedScreenInBookmarks = CUtils.bookMarkOrUnBookMarkScreen(
                    OutputMasterActivity.this, module, sub_module);
            // BaseInputActivity.this, SELECTED_MODULE, SELECTED_SUB_SCREEN);
            changeBookMarkOptionIcon(addedScreenInBookmarks);

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_BOOKMARK, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeBookMarkOptionIcon(boolean addedScreenInBookmarks) {
        if (addedScreenInBookmarks) {
            ((ImageView) findViewById(R.id.bookmark_menu_iv)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bookmark_fill));
        } else {
            ((ImageView) findViewById(R.id.bookmark_menu_iv)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bookmark_line));
        }
    }

    public void setBookMarkIcon(int subMenuItemPosition) {
        try {
            if (CUtils.isScreenBookMarked(OutputMasterActivity.this, SELECTED_MODULE,
                    subMenuItemPosition))
                ((ImageView) findViewById(R.id.bookmark_menu_iv)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bookmark_filled_orenge));
            else
                ((ImageView) findViewById(R.id.bookmark_menu_iv)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.book_mark_outline_yellow_48));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showViewWithAnimation() {
        menuLayout.setVisibility(View.VISIBLE);

        // Get pixel value for 20dp margin
        float margin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        // Create a scale animation for expansion
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0f, 1f, // Scale from 0 to 1 in X direction
                0f, 1f, // Scale from 0 to 1 in Y direction
                Animation.ABSOLUTE, menuLayout.getWidth() - margin, // Pivot at the right edge minus margin
                Animation.ABSOLUTE, margin // Pivot at the top edge plus margin
        );
        scaleAnimation.setDuration(300); // Animation duration

        // Create a translation animation to slide in from the right top corner
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, menuLayout.getWidth(), // Start from right outside the screen
                Animation.ABSOLUTE, 0f, // End at the current position
                Animation.ABSOLUTE, -margin, // Start above the screen with margin
                Animation.ABSOLUTE, 0f // End at the current position
        );
        translateAnimation.setDuration(300);

        // Start animations
        menuLayout.startAnimation(translateAnimation);
        menuLayout.startAnimation(scaleAnimation);
    }

    private void hideViewWithAnimation() {
        menuLayout.clearAnimation();
        // Get pixel value for 20dp margin
        float margin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        // Create a scale animation for collapsing
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 0f, // Scale from 1 to 0 in X direction
                1f, 0f, // Scale from 1 to 0 in Y direction
                Animation.ABSOLUTE, menuLayout.getWidth() - margin, // Pivot at the right edge minus margin
                Animation.ABSOLUTE, margin // Pivot at the top edge plus margin
        );
        scaleAnimation.setDuration(300); // Animation duration

        // Create a translation animation to slide out to the top-right corner
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, 0f, // Start at the current position
                Animation.ABSOLUTE, menuLayout.getWidth(), // End at the right outside the screen
                Animation.ABSOLUTE, 0f, // Start at the current position
                Animation.ABSOLUTE, -margin // End above the screen with margin
        );
        translateAnimation.setDuration(300);

        // Combine animations
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                menuLayout.setVisibility(View.INVISIBLE); // Ensure it's hidden at the end
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        menuLayout.startAnimation(translateAnimation);
        menuLayout.startAnimation(scaleAnimation);
    }

    private void animateTextInfinite() {
        screenName = pageTitles[viewPager.getCurrentItem()];

        String name = screenName;
        Resources resources = null;
        if (LANGUAGE_CODE == 1) {
            resources = getLocaleResources(this, "hi");
        }
        if (resources == null) {
            resources = getLocaleResources(this, getLanguageKey(LANGUAGE_CODE));
        }
        if (isKundliScreen()) {
            if (!screenName.contains(resources.getString(R.string.txt_kundli))){
                name = screenName + " " + resources.getString(R.string.txt_kundli);
                }
            if (screenName.equals(resources.getStringArray(R.array.tajik_module_list)[1])) {
                name = getString(R.string.varshfal_kundli);
            } else if (screenName.equals(resources.getStringArray(R.array.lalkitab_module_list)[3])) {
                name = screenName;
            }
        } else {
            if(SELECTED_MODULE==MODULE_BASIC){
                int pdfPosition = viewPager.getCurrentItem();
                if (pdfPosition == pageTitles.length-3){
                    screenName = getResources().getString(R.string.text_kundli_ai);
                }
            }

            name = screenName;
        }
       // Log.e("mytest", "animateTextInfinite: "+name );
        if (LANGUAGE_CODE == 1) {
            tvAskQuest.animateTextInfinite(resources.getString(R.string.ask_anything_to_kundli_hi, name));
        } else {
            tvAskQuest.animateTextInfinite(resources.getString(R.string.ask_anything_to_kundli, name));
        }
    }

    private boolean isKundliScreen() {
        int selectedSubScreenPos;
        if (SELECTED_SUB_SCREEN > localAdvertismentPosition && isCloudAdded) {
            selectedSubScreenPos = SELECTED_SUB_SCREEN - 1;
        } else {
            selectedSubScreenPos = SELECTED_SUB_SCREEN;
        }

        switch (SELECTED_MODULE) {
            case CGlobalVariables.MODULE_DASA:
            case CGlobalVariables.MODULE_MISC:
            case CGlobalVariables.MODULE_KP:
                return false;
            case CGlobalVariables.MODULE_PREDICTION:
                return selectedSubScreenPos == 6;
            case CGlobalVariables.MODULE_SHODASHVARGA:
                return selectedSubScreenPos == 1 ||
                        selectedSubScreenPos == 2 ||
                        selectedSubScreenPos == 3 ||
                        selectedSubScreenPos == 4 ||
                        selectedSubScreenPos == 5 ||
                        selectedSubScreenPos == 6 ||
                        selectedSubScreenPos == 7 ||
                        selectedSubScreenPos == 8 ||
                        selectedSubScreenPos == 9 ||
                        selectedSubScreenPos == 10 ||
                        selectedSubScreenPos == 11 ||
                        selectedSubScreenPos == 12 ||
                        selectedSubScreenPos == 13 ||
                        selectedSubScreenPos == 14 ||
                        selectedSubScreenPos == 15 ||
                        selectedSubScreenPos == 16;
            case CGlobalVariables.MODULE_LALKITAB:
                return selectedSubScreenPos == 1 ||
                        selectedSubScreenPos == 3;
            case CGlobalVariables.MODULE_VARSHAPHAL:
                return selectedSubScreenPos == 1;
            default:
                if (SELECTED_SUB_SCREEN > localAdvertismentPosition && isCloudAdded) {
                    selectedSubScreenPos = SELECTED_SUB_SCREEN - 2;
                } else {
                    selectedSubScreenPos = SELECTED_SUB_SCREEN - 1;
                }
                return selectedSubScreenPos == BASIC_LAGNA_SCREEN_ID ||
                        selectedSubScreenPos == BASIC_NAVAMSHA_SCREEN_ID ||
                        selectedSubScreenPos == BASIC_MOON_SCREEN_ID ||
                        selectedSubScreenPos == BASIC_CHALIT_SCREEN_ID ||
                        selectedSubScreenPos == BASIC_KARAKAMSHA_SCREEN_ID ||
                        selectedSubScreenPos == BASIC_SWAMSA_SCREEN_ID ||
                        selectedSubScreenPos == BASIC_TRANSIT_SCREEN_ID;
        }

    }

    private String getaCustomTitles(String title, @Nullable Resources resources) {
        if (resources != null) {
            if (LANGUAGE_CODE != 1) {
                resources = getLocaleResources(this, getLanguageKey(LANGUAGE_CODE));
            } else {
                if (title.equals(resources.getString(R.string.chandra_chart))) {
                    title = resources.getString(R.string.chandra) + " " + resources.getString(R.string.txt_kundli);
                }
            }
            if (title.trim().equals("Transit") && LANGUAGE_CODE == 1) {
                title = getString(R.string.gochar) + " " + resources.getString(R.string.txt_kundli);
            }/* else if (title.equals(resources.getString(R.string.chandra_chart))) {
                title = resources.getString(R.string.chandra) + " " + resources.getString(R.string.txt_kundli);
            }*/ else if (title.equals("Chart")) {
                title = resources.getString(R.string.varshfal_kundli);
            } else {
                title = title + " " + resources.getString(R.string.txt_kundli);
            }
        } else {
            resources = getLocaleResources(this,"en");
            if (title.equals(resources.getStringArray(R.array.basic_module_list)[5].trim())) {
                title = getString(R.string.graha_sthiti);
            } else if (title.equals(resources.getStringArray(R.array.basic_module_list)[6].trim())) {
                title = getString(R.string.graha_sthiti_sub);
            } else if (title.equals(resources.getStringArray(R.array.basic_module_list)[7].trim())) {
                title = getString(R.string.chalit_taalika);
            } else if (title.equals(resources.getString(R.string.birth_detail_top_heading).trim())) {
                title = getString(R.string.janma_vivran);
            } else if (title.equals(resources.getStringArray(R.array.basic_module_list)[17].trim())) {
                title = getString(R.string.maitri_chakra);
            } else if (title.equals(resources.getStringArray(R.array.basic_module_list)[18].trim())) {
                title = getString(R.string.vyakti_vivran);
            } else if (title.equals(resources.getStringArray(R.array.basic_module_list)[20].trim())) {
                title = getString(R.string.ashubh_va_shubh);
            } else if (title.equals(resources.getStringArray(R.array.tajik_module_list)[1].trim())) {
                title = getString(R.string.varshfal_kundli);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[1].trim())) {
                title = getString(R.string.sampurna_jeevan_bhavishyafal);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[2].trim())) {
                title = getString(R.string.maasik_bhavishyafal);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[3].trim())) {
                title = getString(R.string.maasik_bhavishyafal);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[7].trim())) {
                title = getString(R.string.lal_kitab_rin);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[8].trim())) {
                title = getString(R.string.lal_kitab_teva_prakaar);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[9].trim())) {
                title = getString(R.string.lal_kitab_upaay);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[10].trim())) {
                title = getString(R.string.lagna_bhavishyavaani);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[11].trim())) {
                title = getString(R.string.graha_vichaar);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[13].trim())) {
                title = getString(R.string.aaj_ka_gochar);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[16].trim())) {
                title = getString(R.string.varshfal);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[17].trim())) {
                title = getString(R.string.naamkaran_sujhaav);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[18].trim())) {
                title = getString(R.string.chandra_raashi);
            } else if (title.equals(resources.getStringArray(R.array.predictions_module_list)[19].trim())) {
                title = getString(R.string.chandra_raashi_paaramparik);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[4].trim())) {
                title = getString(R.string.bhaaav_sandhi);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[5].trim())) {
                title = getString(R.string.graha_nirdeshan);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[6].trim())) {
                title = getString(R.string.bhaav_nirdeshan);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[7].trim())) {
                title = getString(R.string.graha_nirdeshan_khaakaa);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[12].trim())) {
                title = getString(R.string.shaasak_graha);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[13].trim())) {
                title = getString(R.string.vartamaan_shaasak_graha);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[14].trim())) {
                title = getString(R.string.anya);
            } else if (title.equals(resources.getStringArray(R.array.kpsystem_module_list)[15].trim())) {
                title = getString(R.string.kp_sandhi);
            } else if (title.equals(resources.getStringArray(R.array.lalkitab_module_list)[2].trim())) {
                title = getString(R.string.phal_aivam_upaay);
            } else if (title.equals(resources.getStringArray(R.array.lalkitab_module_list)[4].trim())) {
                title = getString(R.string.rin);
            } else if (title.equals(resources.getStringArray(R.array.lalkitab_module_list)[5].trim())) {
                title = getString(R.string.teva_prakaar);
            } else if (title.equals(resources.getStringArray(R.array.lalkitab_module_list)[7].trim())) {
                title = getString(R.string.graha);
            }
        }
        if (title.contains("Kundli Kundli")) {
            title = title.replace("Kundli Kundli", "Kundli");
        }

        return title;
    }

}
