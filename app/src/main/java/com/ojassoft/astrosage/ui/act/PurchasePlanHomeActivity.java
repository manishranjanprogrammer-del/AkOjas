package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_QUESTIONS_RECEIVED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_10;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SCREEN_ID_DHRUV;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.ImmutableList;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.jinterface.IPersonalDetails;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.misc.PurchaseVerificationService;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.fragments.BasicPlanFrag;
import com.ojassoft.astrosage.ui.fragments.FreeTrialFragment;
import com.ojassoft.astrosage.ui.fragments.GoldPlanFrag;
import com.ojassoft.astrosage.ui.fragments.PlatinumPlanFrag;
import com.ojassoft.astrosage.ui.fragments.SilverPlanFrag;
import com.ojassoft.astrosage.ui.fragments.vratfragment.KundliAiPlusPlanFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.dialog.SubscriptionPaymentFailDialog;
import com.ojassoft.astrosage.varta.model.UPIAppModel;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * PurchasePlanHomeActivity is the main activity for handling subscription plans and purchases.
 * It manages different types of plans (Basic, Silver, Gold, Platinum) and their variations
 * (monthly/yearly) with support for both Google Play Billing and Razorpay payment systems.
 * 
 * Key features:
 * - Plan selection and display
 * - Payment processing (Google Play & Razorpay)
 * - Subscription management
 * - Plan verification
 * - Analytics tracking
 * - Multi-language support
 * 
 * @see BaseInputActivity
 * @see IPurchasePlan
 * @see IPersonalDetails
 * @see BillingEventHandler
 * @see PaymentResultWithDataListener
 * @see VolleyResponse
 */
public class PurchasePlanHomeActivity extends BaseInputActivity implements
        IPurchasePlan, IPersonalDetails, BillingEventHandler, PaymentResultWithDataListener, VolleyResponse {

    public static final String SKU_SILVER_PLAN_YEAR = "silver_plan_year";
    public static final String SKU_GOLD_PLAN_MONTH = "gold_plan_month";
    public static final String SKU_GOLD_PLAN_YEAR = "gold_plan_year";
    public static final String SKU_SILVER_PLAN_MONTH = "silver_plan_month";
    public static final String SKU_PLATINUM_PLAN_MONTH = "platinum_plan_month";
    public static final String SKU_PLATINUM_PLAN_YEAR = "platinum_plan_year";
    public static final String SKU_PLATINUM_PLAN_MONTH_OMF = "platinum_plan_month_omf";
    public static final String SKU_PLATINUM_PLAN_YEAR_OMF = "platinum_plan_year_omf";
    // ADDED BY BIJENDRA ON 20-08-15
    static final int SUB_RC_REQUEST_SILVER_PLAN_YEAR = 20007;
    static final int SUB_RC_REQUEST_GOLD_PLAN_YEAR = 20008;
    static final int SUB_RC_REQUEST_SILVER_PLAN_MONTH = 20009;
    static final int SUB_RC_REQUEST_GOLD_PLAN_MONTH = 20010;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_YEAR = 20011;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_MONTH = 20012;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_MONTH_OMF = 20013;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_YEAR_OMF = 20014;
    public static int BASIC_PLAN = 0;
    public static int SIVLER_PLAN = 1;
    public static int GOLD_PLAN = 2;
    public static int GOLD_PLAN_MONTHLY = 3;
    public static int SIVLER_PLAN_MONTHLY = 4;
    public static int PLATINUM_PLAN = 5;
    public static int PLATINUM_PLAN_MONTHLY = 6;

    public static int PLATINUM_PLAN_MONTHLY_FREE_TRIAL = 100;
    public static int PLATINUM_PLAN_MONTHLY_OMF = 7;
    public static int PLATINUM_PLAN_YEARLY_OMF = 8;

    public static int GET_SERVICE_DETAILS = 3101;

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    public String silverPlanPriceYear = "", goldPlanPriceYear = "", silverPlanPriceMonth = "", goldPlanPriceMonth = "", platinumPlanPriceYear = "", platinumPlanPriceMonth = "", platinumPlanPriceYearOmf = "", platinumPlanPriceMonthOmf = "";
    public boolean enableMonthlySubscriptionValue = false;
    public ArrayList<String> arayPlatinumPlanMonth = new ArrayList<String>(5);
    LinearLayout toolbar;
    ViewPager pager;
    LinearLayout linearLayout1;
    TabLayout tabs;
    LinearLayout ganeshaBgLL;
    /* CharSequence Titles[]={"BASIC ","SILVER"," GOLD "}; */
    int Numboftabs = 3;
    SilverPlanFrag silverplan;
    GoldPlanFrag goldplan;
    BasicPlanFrag basicplan;
    PlatinumPlanFrag platinumplan;
    KundliAiPlusPlanFrag kundliAiPlusPlanFrag;
    ModulePagerAdapter modulePagerAdapter;
    int requestCode;
    int PRODUCTID = 0, TITLE = 1, PRICE = 2, DESCRIPTION = 3, TYPE = 4, price_amount_micros = 5, price_currency_code = 6, isFreetimeperiod = 7;
    ArrayList<String> araySilverPlanYear = new ArrayList<String>(5);
    ArrayList<String> arayGoldPlanYear = new ArrayList<String>(5);
    ArrayList<String> arayGoldPlanMonth = new ArrayList<String>(5);
    ArrayList<String> araySilverPlanMonth = new ArrayList<String>(5);
    ArrayList<String> arayPlatinumPlanYear = new ArrayList<String>(5);
    ArrayList<String> arayPlatinumPlanMonthOmf = new ArrayList<String>(5);
    ArrayList<String> arayPlatinumPlanYearOmf = new ArrayList<String>(5);
    ProductDetails skuDetailsSilverPlanYear;
    ProductDetails skuDetailsGoldPlanYear;
    ProductDetails skuDetailsGoldPlanMonth;
    ProductDetails skuDetailsSilverPlanMonth;
    ProductDetails skuDetailsPlatinumPlanYear;
    ProductDetails skuDetailsPlatinumPlanMonth;
    ProductDetails skuDetailsPlatinumPlanMonthOmf;
    ProductDetails skuDetailsPlatinumPlanYearOmf;
    ImageView textViewHeading, platinumImageview;
    TextView txtHeading1;
    String purchaseData = "", signature = "";
    String screenId = "";
    ValueAnimator mColorAnimation1;
    boolean isFreeDhruvPlanShow;
    LinearLayout mainlayout;
    String[] errorResponse = {"Success",
            "Billing response result user canceled",
            "Network connection is down",
            "Billing API version is not supported for the type requested",
            "Requested product is not available for purchase",
            "Invalid arguments provided to the API",
            "Fatal error during the API action",
            "Failure to purchase since item is already owned",
            "Failure to consume since item is not owned"};
    String purchaseSource;
    String dumpDataString = "";
    CustomProgressDialog pd;
    RequestQueue queue;
    String COUNTRY_CODE_IND = "91";
    String currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
    ServicelistModal servicelistModal;
    String orderId = "",merchantOrderId="",IsFreeTrialAvailable ="";
    private String paymentModeStr = "";
    String amountToPay = "0";
    private final int PHONE_PAY_GATEWAY_CALLBACK= 10001;
//    boolean isFreeTrailAvailable = false;
    String serviceId, PriceInDollor, PriceInRS, subscriptionId;
    // Declaring Your View and Variables
    /*Integer[] iconResourceArray = {R.drawable.circle_for_plan_purchase, R.drawable.circle_for_plan_purchase,
            R.drawable.circle_for_plan_purchase};
    Integer[] iconCheckResourceArray = {R.drawable.right_circle_for_plan_purchase,
            R.drawable.right_circle_for_plan_purchase, R.drawable.right_circle_for_plan_purchase};
*/
    private String[] pageTitles;
    private TextView Continuewithbasicplantxt, tvCurrentPlan, textViewSubHeading;
    String domesticPayMode, internationalPayMode, countryCode;
    private String paymentFailedFrom = "";
    public boolean isPremiumUserAfterLogin = false;
    private UPIAppModel upiAppModel;
    public PurchasePlanHomeActivity() {
        super(R.id.app_name);
    }

    /**
     * Initializes the activity and sets up the UI components.
     * Configures ViewPager, TabLayout, and initializes plan fragments.
     * 
     * @param savedInstanceState Bundle containing the activity's previously saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*CGlobalVariables.isShowSilverPlanTab = false;
        CGlobalVariables.isShowGoldPlanTab = true;
        enableMonthlySubscriptionValue =true;*/
        enableMonthlySubscriptionValue = CUtils.getBooleanData(this, CGlobalVariables.enableMonthlySubscription, false);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.activity_purchase_plan);
        setBillingEventHandler(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) PurchasePlanHomeActivity.this
                .getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(
                PurchasePlanHomeActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);

        toolbar = findViewById(R.id.toolbar);
        linearLayout1 = findViewById(R.id.linearLayout1);
        mainlayout = findViewById(R.id.mainlayout);
        platinumImageview = findViewById(R.id.platinum_imageview);
        txtHeading1 = findViewById(R.id.txtHeading1);
        FontUtils.changeFont(this, txtHeading1, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
            pageTitles = getResources().getStringArray(
                    R.array.purchase_plan_tab_titles_list);
        } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
            pageTitles = getResources().getStringArray(
                    R.array.purchase_plan_tab_titles_list2);
        } else if (CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
            pageTitles = getResources().getStringArray(
                    R.array.purchase_plan_tab_titles_list3);
        } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
            pageTitles = getResources().getStringArray(
                    R.array.purchase_plan_tab_titles_list1);
        }


        textViewHeading = findViewById(R.id.Astrofeatureheading);
        //textViewHeading.setTypeface(mediumTypeface);
        textViewSubHeading = findViewById(R.id.Astrofeaturesubheading);
        textViewSubHeading.setTypeface(regularTypeface);

        try {
            screenId = getIntent().getStringExtra("ScreenId");
            purchaseSource = getIntent().getStringExtra("purchaseSource");
            if (getIntent().hasExtra("isFreeDhruvPlanShow")) {
                isFreeDhruvPlanShow = getIntent().getBooleanExtra("isFreeDhruvPlanShow", false);
                //enableMonthlySubscriptionValue = isFreeDhruvPlanShow;
            }/*else{
                enableMonthlySubscriptionValue = false;
            }*/
        } catch (Exception ex) {
            Log.e("Exception :", "onCreate: " + ex.getMessage());
        }
        if (screenId == null) {
            screenId = "";
        }
        if (purchaseSource == null) {
            purchaseSource = "";
        }

        Log.e("ScreenID ", screenId);
        pager = findViewById(R.id.pager);
        //pager.setOffscreenPageLimit(2);

        // Assiging the Sliding Tab Layout View
        int planId = CUtils.getUserPurchasedPlanFromPreference(PurchasePlanHomeActivity.this);


        tabs = findViewById(R.id.tabs);
        if (pageTitles != null && pageTitles.length > 0) {
            tabs.addTab(tabs.newTab().setText(pageTitles[0]));
            if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {

                tabs.addTab(tabs.newTab().setText(pageTitles[1]));
                tabs.addTab(tabs.newTab().setText(pageTitles[2]));
                tabs.addTab(tabs.newTab().setText(pageTitles[3]));
            } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                tabs.addTab(tabs.newTab().setText(pageTitles[1]));
            } else if (CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                tabs.addTab(tabs.newTab().setText(pageTitles[1]));
                tabs.addTab(tabs.newTab().setText(pageTitles[2]));
            } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                tabs.addTab(tabs.newTab().setText(pageTitles[1]));
                tabs.addTab(tabs.newTab().setText(pageTitles[2]));
            }

            tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        if (planId == CGlobalVariables.SILVER_PLAN_ID || planId == CGlobalVariables.SILVER_PLAN_ID_4 || planId == CGlobalVariables.SILVER_PLAN_ID_5) {
            //  linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
            textViewHeading.setVisibility(View.VISIBLE);
            platinumImageview.setVisibility(View.GONE);
            txtHeading1.setVisibility(View.GONE);
        } else if (planId == CGlobalVariables.GOLD_PLAN_ID || planId == CGlobalVariables.GOLD_PLAN_ID_6 || planId == CGlobalVariables.GOLD_PLAN_ID_7) {
            //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_gold_plan));
            textViewHeading.setVisibility(View.VISIBLE);
            platinumImageview.setVisibility(View.GONE);
            txtHeading1.setVisibility(View.GONE);
        } else if (planId == CGlobalVariables.PLATINUM_PLAN_ID || planId == CGlobalVariables.PLATINUM_PLAN_ID_9 || planId == PLATINUM_PLAN_ID_10  || planId == KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.platinum_color));
            textViewHeading.setVisibility(View.GONE);
            platinumImageview.setVisibility(View.VISIBLE);
            txtHeading1.setVisibility(View.VISIBLE);
        }
        final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.HONEYCOMB) {
            mColorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.subs_violet), getResources().getColor(R.color.subs_sky_blue), getResources().getColor(R.color.subs_gold_plan));
            // mColorAnimation1.setDuration((3 - 1) * 10000000000L);
            mColorAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    //       linearLayout1.setBackgroundColor((Integer) animator.getAnimatedValue());
                    // mainLayout.setBackgroundColor((Integer) animator.getAnimatedValue());
                }
            });

        } else {
            // do something for phones running an SDK before lollipop
        }

        final Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.lay_purchase_plan_tab_title, null);
        ImageView img = view.findViewById(R.id.tabimage);
        View leftLine = view.findViewById(R.id.left_line_connector);
        View rightLine = view.findViewById(R.id.right_line_connector);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {


            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (currentapiVersion > Build.VERSION_CODES.HONEYCOMB) {
                    //mColorAnimation1.setCurrentPlayTime((long) ((positionOffset + position) * 10000000000L));
                }
                if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                    if (position == 0) {        //for basic plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            //    window.setStatusBarColor(getResources().getColor(R.color.subs_violet));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //  linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        //  mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_basic);
                        textViewSubHeading.setText(getResources().getString(R.string.complete_experiance_basic));

                    } else if (position == 1) {         //for silver plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            // window.setStatusBarColor(getResources().getColor(R.color.subs_sky_blue));
                            window.setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        //    linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                        //   mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_silver);
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_silver_text));

                    } else if (position == 2) {         //for gold plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            // window.setStatusBarColor(getResources().getColor(R.color.subs_gold_plan));
                            window.setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_gold_plan));
                        //  mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_gold_plan));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_gold);
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_gold_text));
                    } else if (position == 3) {         //for gold plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            //  window.setStatusBarColor(getResources().getColor(R.color.platinum_color));
                            window.setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.platinum_color));
                        //  mainLayout.setBackgroundColor(getResources().getColor(R.color.platinum_color));
                        textViewHeading.setVisibility(View.GONE);
                        platinumImageview.setVisibility(View.VISIBLE);
                        txtHeading1.setVisibility(View.VISIBLE);
                        platinumImageview.setImageResource(R.drawable.kundli_ai_plus_logo);
                        //textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text));
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text_2));
                    }
                } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                    if (position == 0) {
                        //for basic plan
                        selectedPlanIndex = BASIC_PLAN_INDEX;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            //  window.setStatusBarColor(getResources().getColor(R.color.subs_violet));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        //  mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_basic);
                        textViewSubHeading.setText(getResources().getString(R.string.complete_experiance_basic));

                    } else if (position == 1) {         //for platinum plan
                        selectedPlanIndex = KUNDLI_AI_PLUS_PLAN_INDEX;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            window.setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        textViewHeading.setVisibility(View.GONE);
                        platinumImageview.setVisibility(View.VISIBLE);
                        txtHeading1.setVisibility(View.VISIBLE);
                        platinumImageview.setImageResource(R.drawable.kundli_ai_plus_logo);

                        // Set original size for platinum plan
                        int width = (int) getResources().getDimension(R.dimen.platinum_image_width);
                        int height = (int) getResources().getDimension(R.dimen.platinum_image_height);
                        ViewGroup.LayoutParams params = platinumImageview.getLayoutParams();
                        params.width = width;
                        params.height = height;
                        platinumImageview.setLayoutParams(params);
                        platinumImageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        platinumImageview.setAdjustViewBounds(true);

                        //textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text));
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text_2));
                    } else if (position == 2) { //for dhruv plan
                        selectedPlanIndex = DHRUV_PLAN_INDEX;
                        textViewHeading.setVisibility(View.GONE);
                        platinumImageview.setVisibility(View.VISIBLE);
                        txtHeading1.setVisibility(View.GONE);
                        platinumImageview.setImageResource(R.drawable.dhruv_plan_logo_new);

                        // Get screen width
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int screenWidth = displayMetrics.widthPixels;

                        // Calculate height to maintain aspect ratio
                        float originalWidth = getResources().getDimension(R.dimen.platinum_image_width);
                        float originalHeight = getResources().getDimension(R.dimen.platinum_image_height);
                        float aspectRatio = originalHeight / originalWidth;
                        
                        // Set width to 70% of screen width and adjust height proportionally
                        int width = (int) (screenWidth * 0.8f);
                        int height = (int) (width * aspectRatio);

                        ViewGroup.LayoutParams params = platinumImageview.getLayoutParams();
                        params.width = width;
                        params.height = height;
                        platinumImageview.setLayoutParams(params);
                        platinumImageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        platinumImageview.setAdjustViewBounds(true);

                        //textViewSubHeading.setText(getResources().getString(R.string.dhruv_plan_description));
                        textViewSubHeading.setText(getResources().getString(R.string.dhruv_plan_description_2));
                    }
                    getAIKundliPlusPlanServices(true); // To update service details
                } else if (CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                    if (position == 0) {        //for basic plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            //  window.setStatusBarColor(getResources().getColor(R.color.subs_violet));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //  linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        //  mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_basic);
                        textViewSubHeading.setText(getResources().getString(R.string.complete_experiance_basic));

                    } else if (position == 1) {         //for silver plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
//                            window.setStatusBarColor(getResources().getColor(R.color.subs_sky_blue));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                        //   mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_silver);
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_silver_text));

                    } else if (position == 2) {         //for gold plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
//                            window.setStatusBarColor(getResources().getColor(R.color.platinum_color));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.platinum_color));
                        //   mainLayout.setBackgroundColor(getResources().getColor(R.color.platinum_color));
                        textViewHeading.setVisibility(View.GONE);
                        platinumImageview.setVisibility(View.VISIBLE);
                        txtHeading1.setVisibility(View.VISIBLE);
                        platinumImageview.setImageResource(R.drawable.kundli_ai_plus_logo);
                        //textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text));
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text_2));
                    }
                } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                    if (position == 0) {        //for basic plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                            //       window.setStatusBarColor(getResources().getColor(R.color.subs_violet));
                            window.setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        //   linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        //   mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_basic);
                        textViewSubHeading.setText(getResources().getString(R.string.complete_experiance_basic));

                    } else if (position == 1) {         //for gold plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
//                            window.setStatusBarColor(getResources().getColor(R.color.subs_gold_plan));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //  linearLayout1.setBackgroundColor(getResources().getColor(R.color.subs_gold_plan));
                        //    mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_gold_plan));
                        textViewHeading.setVisibility(View.VISIBLE);
                        platinumImageview.setVisibility(View.GONE);
                        txtHeading1.setVisibility(View.GONE);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_gold);
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_gold_text));
                    } else if (position == 2) {         //for gold plan
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
//                            window.setStatusBarColor(getResources().getColor(R.color.platinum_color));
                            window.setStatusBarColor(getResources().getColor(R.color.white));

                        }
                        //    linearLayout1.setBackgroundColor(getResources().getColor(R.color.platinum_color));
                        //   mainLayout.setBackgroundColor(getResources().getColor(R.color.platinum_color));
                        textViewHeading.setVisibility(View.GONE);
                        platinumImageview.setVisibility(View.VISIBLE);
                        txtHeading1.setVisibility(View.VISIBLE);
                        platinumImageview.setImageResource(R.drawable.kundli_ai_plus_logo);
                        //textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text));
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_platinum_text_2));
                    }
                }


                // tabs.setBackgroundColor(getResources().getColor(R.color.white));
            }

            public void onPageSelected(int position) {

                if (currentapiVersion > Build.VERSION_CODES.HONEYCOMB) {

                } else {

                    /*if (position == 0) {
                       // tabs.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                       // toolbar.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_violet));
                        //ganeshaBgLL.setImageResource(R.drawable.basic_circle_bg);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_basic);
                        textViewSubHeading.setText(getResources().getString(R.string.complete_experiance_basic));

                    } else if (position == 1) {
                       // tabs.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                       // toolbar.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                        mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_sky_blue));
                        //ganeshaBgLL.setImageResource(R.drawable.silver_circle_bg);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_silver);
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_silver_text));

                    } else if (position == 2) {
                      ///  tabs.setBackgroundColor(getResources().getColor(R.color.subs_orange));
                      //  toolbar.setBackgroundColor(getResources().getColor(R.color.subs_orange));
                        mainLayout.setBackgroundColor(getResources().getColor(R.color.subs_orange));
                        //ganeshaBgLL.setImageResource(R.drawable.gold_circle_bg);
                        textViewHeading.setImageResource(R.drawable.words_astrosage_gold);
                        textViewSubHeading.setText(getResources().getString(R.string.upgrade_gold_text));

                    }*/
                }
           /*     if (position == 0) {
                    // finally change the color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                        window.setStatusBarColor(getResources().getColor(R.color.subs_violet));
                                                    window.setStatusBarColor(getResources().getColor(R.color.white));

                    }
                } else if (position == 1) {
                    // finally change the color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                        window.setStatusBarColor(getResources().getColor(R.color.subs_sky_blue));
                    }
                } else if (position == 2) {
                    // finally change the color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && window != null) {
                        window.setStatusBarColor(getResources().getColor(R.color.subs_orange));
                    }
                }*/

            }
        });

        // Added on 11-sep-2015 to set the size of Tabs equal and makes it
        // unscrollable

        float px = resizingLayoutOfTabsImages();
        try {

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img
                    .getLayoutParams();
            layoutParams.width = (int) px;
            layoutParams.height = (int) px;
            img.setLayoutParams(layoutParams);

        } catch (Exception ex) {
            //Log.i("error11", ex.getMessage()+"");
        }
        getAIKundliPlusPlanServices(false);
        getProductListPref();
        //if source of purchase is AstroShop, then set the current item to Dhruv Plan
        if(purchaseSource.equals("product_list")
                || purchaseSource.equals("act_astrologer_description")
                || purchaseSource.equals("product_description")
                || purchaseSource.equals("astro_shop_service_adapter")
                || purchaseSource.equals("astrosage-cloud-paid-plan-platinum")
                || purchaseSource.equals("astrosage-cloud-paid-plan-platinum-one-month")) {

            pager.setCurrentItem(DHRUV_PLAN_INDEX);
        }
        //if 10 means dhruv plan , if 11 means Kundli AI+plan in both case redirecting to dhruv plan
        if(CUtils.getUserPurchasedPlanFromPreference(this)>9){
            pager.setCurrentItem(DHRUV_PLAN_INDEX);
        }
//        String internationalPayMode = CUtils.getStringData(PurchasePlanHomeActivity.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
//        String domesticPayMode = CUtils.getStringData(PurchasePlanHomeActivity.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
//        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
//        if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//            if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                getAIKundliPlusPlanServices();
//            } else {
//                getProductListPref();
//            }
//        } else {
//            if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                getAIKundliPlusPlanServices();
//            } else {
//                getProductListPref();
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }


    public String[] getPageTitles() {
        ArrayList<String> pageTileNeeded = new ArrayList<>();
        for (int i = 0; i < pageTitles.length; i++) {
            pageTileNeeded.add(pageTitles[i]);
        }
        return pageTileNeeded.toArray(new String[0]);
    }

    private float resizingLayoutOfTabsImages() {

        Display display = getWindowManager().getDefaultDisplay();

        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        // float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (dpWidth - 35) / 7, r.getDisplayMetrics());

        // Toast.makeText(this, ""+px, Toast.LENGTH_SHORT).show();

        return px;

    }

    /**
     * Initializes the fragment adapter for the ViewPager.
     * Sets up the plan fragments based on button enable state.
     * 
     * @param isButtonEnable Whether the buy button should be enabled
     */
    private void initFragmentAdapter(boolean isButtonEnable) {
        //String PlanPreferencename;
        modulePagerAdapter = new ModulePagerAdapter(getSupportFragmentManager(), isButtonEnable);
        pager.setAdapter(modulePagerAdapter);
        if (screenId.equals("")) {
            int planId = CUtils.getUserPurchasedPlanFromPreference(this);
            /*if (planId == 6 || planId == 7 || planId == 3) {
                pager.setCurrentItem(GOLD_PLAN);
            } else {
                pager.setCurrentItem(SIVLER_PLAN);
            }*/
            if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                if (planId == CGlobalVariables.PLATINUM_PLAN_ID || planId == CGlobalVariables.PLATINUM_PLAN_ID_9 || planId == PLATINUM_PLAN_ID_10) {
                    pager.setCurrentItem(3);//3 platinum plan frag
                } else if (planId == CGlobalVariables.GOLD_PLAN_ID || planId == CGlobalVariables.GOLD_PLAN_ID_6 || planId == CGlobalVariables.GOLD_PLAN_ID_7) {
                    pager.setCurrentItem(2);//2 gold plan frag
                } else {
                    pager.setCurrentItem(1);//1 silver plan frag
                }
            }
            if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                pager.setCurrentItem(1);//1 platinum plan frag
            } else if (CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                if (planId == CGlobalVariables.PLATINUM_PLAN_ID || planId == CGlobalVariables.PLATINUM_PLAN_ID_9 || planId == PLATINUM_PLAN_ID_10) {
                    pager.setCurrentItem(2);//2 platinum plan frag
                } else {
                    pager.setCurrentItem(1);//1 silver plan frag
                }
            } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                if (planId == CGlobalVariables.PLATINUM_PLAN_ID || planId == CGlobalVariables.PLATINUM_PLAN_ID_9 || planId == PLATINUM_PLAN_ID_10) {
                    pager.setCurrentItem(2);//2 platinum plan frag
                } else {
                    pager.setCurrentItem(1);//1 gold plan frag
                }
            }

        } else if (screenId.equalsIgnoreCase(SCREEN_ID_DHRUV)) {
            if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                pager.setCurrentItem(3);//2 platinum plan frag
            } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                pager.setCurrentItem(1);//1 platinum plan frag
            } else if (CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                pager.setCurrentItem(2);//2 platinum plan frag
            } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                pager.setCurrentItem(2);//2 platinum plan frag
            }
        } else {

            if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                if (screenId.equals(CGlobalVariables.GOLD_PLAN_VALUE_YEAR)) {
                    pager.setCurrentItem(2);//2 gold plan frag
                } else if (screenId.equals(CGlobalVariables.SILVER_PLAN_VALUE_YEAR)) {
                    pager.setCurrentItem(1);//1 silver plan frag
                } else {
                    pager.setCurrentItem(3);//3 platinum plan frag
                }
            } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {

                pager.setCurrentItem(1);//2 platinum plan frag

            } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                if (screenId.equals(CGlobalVariables.SILVER_PLAN_VALUE_YEAR)) {
                    pager.setCurrentItem(1);//1 silver plan frag
                } else {
                    pager.setCurrentItem(2);//2 platinum plan frag
                }

            } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                if (screenId.equals(CGlobalVariables.GOLD_PLAN_VALUE_YEAR)) {
                    pager.setCurrentItem(1);//1 gold plan frag
                } else {
                    pager.setCurrentItem(2);//2 platinum plan frag
                }

            }
        }
        // tabs.setViewPager(pager, screenId);
        tabs.setupWithViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*@authour
     * Add This method to parce and get data back
     * from sharedPrefrences*/
    public void getProductListPref() {

        ArrayList<ProductDetails> skuDetailsList = CUtils.getCloudPlanList();
        //Log.e("BillingClient", "onSkuDetailsResponse() OK size"+skuDetailsList);
        //Toast.makeText(PurchasePlanHomeActivity.this, "skuDetailsList="+skuDetailsList, Toast.LENGTH_SHORT).show();
        if (skuDetailsList != null && skuDetailsList.size() > 0) {
            Log.e("BillingClient", "onProductDetailsResponse() 1");
            for (ProductDetails skuDetails : skuDetailsList) {
                intiProductPlan(skuDetails);
            }
            showProductsDetail_new();
        } else {
            Log.e("BillingClient", "onProductDetailsResponse() 2");
            showProductsDetail_new();
            fetchProductFromGoogleServer();
        }

    }

    /**
     * Fetches product details from Google Play Billing.
     * Retrieves pricing and subscription information for all plans.
     */
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
                                    showProductsDetail_new();
                                }
                            });
                        } else {
                            showMsg(response);
                        }
                    }
            );
        } catch (Exception e) {
            Log.e("BillingClient", "fetchProductFromGoogleServer() exp=" + e);
            showProductsDetail_new();
        }
    }

    private void showMsg(final int response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response >= 0 && response < 9) {
                        Toast.makeText(PurchasePlanHomeActivity.this, errorResponse[response], Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PurchasePlanHomeActivity.this, getResources().getString(R.string.internet_is_not_working), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });

    }

    /**
     * Initializes product plan details with the provided SKU details.
     * Updates UI and pricing information based on the plan type.
     * 
     * @param skuDetails Product details from Google Play Billing
     */
    private void intiProductPlan(ProductDetails skuDetails) {
        try {
            //Log.e("CloudPlanTest", "skuDetails="+skuDetails.getProductId()+" "+skuDetails.getTitle());
            ProductDetails.PricingPhase priceDetails = skuDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0);
            if (skuDetails.getProductId().equalsIgnoreCase(SKU_GOLD_PLAN_YEAR)) {
                arayGoldPlanYear.add(skuDetails.getProductId());// 0
                arayGoldPlanYear.add(skuDetails.getTitle());// 1
                arayGoldPlanYear.add(priceDetails.getFormattedPrice()); // 2
                //Log.e("mytag", "intiProductPlan: getFormattedPrice ="+ priceDetails.getFormattedPrice());
                arayGoldPlanYear.add(skuDetails.getDescription());// 3
                arayGoldPlanYear.add(skuDetails.getProductType());// 4
                arayGoldPlanYear.add(priceDetails.getPriceAmountMicros() + "");//5
                // Log.e("mytag", "intiProductPlan: getPriceAmountMicros ="+ priceDetails.getPriceAmountMicros());

                arayGoldPlanYear.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    arayGoldPlanYear.add(priceDetails.getBillingPeriod());
                }
                skuDetailsGoldPlanYear = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_GOLD_PLAN_MONTH)) {
                arayGoldPlanMonth.add(skuDetails.getProductId());// 0
                arayGoldPlanMonth.add(skuDetails.getTitle());// 1
                arayGoldPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                arayGoldPlanMonth.add(skuDetails.getDescription());// 3
                arayGoldPlanMonth.add(skuDetails.getProductType());// 4
                arayGoldPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                arayGoldPlanMonth.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    arayGoldPlanMonth.add(priceDetails.getBillingPeriod());
                }
                skuDetailsGoldPlanMonth = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_SILVER_PLAN_YEAR)) {

                araySilverPlanYear.add(skuDetails.getProductId());// 0
                araySilverPlanYear.add(skuDetails.getTitle());// 1
                araySilverPlanYear.add(priceDetails.getFormattedPrice()); // 2
                araySilverPlanYear.add(skuDetails.getDescription());// 3
                araySilverPlanYear.add(skuDetails.getProductType());// 4
                araySilverPlanYear.add(priceDetails.getPriceAmountMicros() + "");//5
                araySilverPlanYear.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    araySilverPlanYear.add(priceDetails.getBillingPeriod());
                }

                skuDetailsSilverPlanYear = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_SILVER_PLAN_MONTH)) {

                araySilverPlanMonth.add(skuDetails.getProductId());// 0
                araySilverPlanMonth.add(skuDetails.getTitle());// 1
                araySilverPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                araySilverPlanMonth.add(skuDetails.getDescription());// 3
                araySilverPlanMonth.add(skuDetails.getProductType());// 4
                araySilverPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                araySilverPlanMonth.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    araySilverPlanMonth.add(priceDetails.getBillingPeriod());
                }

                skuDetailsSilverPlanMonth = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_PLATINUM_PLAN_MONTH)) {
                arayPlatinumPlanMonth.add(skuDetails.getProductId());// 0
                arayPlatinumPlanMonth.add(skuDetails.getTitle());// 1
                arayPlatinumPlanMonth.add(priceDetails.getFormattedPrice()); // 2
                arayPlatinumPlanMonth.add(skuDetails.getDescription());// 3
                arayPlatinumPlanMonth.add(skuDetails.getProductType());// 4
                arayPlatinumPlanMonth.add(priceDetails.getPriceAmountMicros() + "");//5
                arayPlatinumPlanMonth.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    arayPlatinumPlanMonth.add(priceDetails.getBillingPeriod());
                }
                skuDetailsPlatinumPlanMonth = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_PLATINUM_PLAN_YEAR)) {
                arayPlatinumPlanYear.add(skuDetails.getProductId());// 0
                arayPlatinumPlanYear.add(skuDetails.getTitle());// 1
                arayPlatinumPlanYear.add(priceDetails.getFormattedPrice()); // 2
                arayPlatinumPlanYear.add(skuDetails.getDescription());// 3
                arayPlatinumPlanYear.add(skuDetails.getProductType());// 4
                arayPlatinumPlanYear.add(priceDetails.getPriceAmountMicros() + "");//5
                arayPlatinumPlanYear.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    arayPlatinumPlanYear.add(priceDetails.getBillingPeriod());
                }

                skuDetailsPlatinumPlanYear = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_PLATINUM_PLAN_MONTH_OMF)) {
                arayPlatinumPlanMonthOmf.add(skuDetails.getProductId());// 0
                arayPlatinumPlanMonthOmf.add(skuDetails.getTitle());// 1
                arayPlatinumPlanMonthOmf.add(priceDetails.getFormattedPrice()); // 2
                arayPlatinumPlanMonthOmf.add(skuDetails.getDescription());// 3
                arayPlatinumPlanMonthOmf.add(skuDetails.getProductType());// 4
                arayPlatinumPlanMonthOmf.add(priceDetails.getPriceAmountMicros() + "");//5
                arayPlatinumPlanMonthOmf.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    arayPlatinumPlanMonthOmf.add(priceDetails.getBillingPeriod());
                }

                skuDetailsPlatinumPlanMonthOmf = skuDetails;
            }

            if (skuDetails.getProductId().equalsIgnoreCase(SKU_PLATINUM_PLAN_YEAR_OMF)) {
                arayPlatinumPlanYearOmf.add(skuDetails.getProductId());// 0
                arayPlatinumPlanYearOmf.add(skuDetails.getTitle());// 1
                arayPlatinumPlanYearOmf.add(priceDetails.getFormattedPrice()); // 2
                arayPlatinumPlanYearOmf.add(skuDetails.getDescription());// 3
                arayPlatinumPlanYearOmf.add(skuDetails.getProductType());// 4
                arayPlatinumPlanYearOmf.add(priceDetails.getPriceAmountMicros() + "");//5
                arayPlatinumPlanYearOmf.add(priceDetails.getPriceCurrencyCode());

                if (priceDetails.getPriceAmountMicros() == 0) {
                    arayPlatinumPlanYearOmf.add(priceDetails.getBillingPeriod());
                }

                skuDetailsPlatinumPlanYearOmf = skuDetails;
            }

        } catch (Exception e) {
            Log.e("BillingClient", "intiProductPlan() exp=" + e);
        }

    }

    private void showProductsDetail_new() {
        boolean hasPrice = false;
        String rupeesInTamil = "ரூ.";
        String month = "/" + getResources().getString(R.string.month);
        String year = "/" + getResources().getString(R.string.year);
        String planPrice = "";

        //INIT SILVER PLAN YEARLY
        if (!araySilverPlanYear.isEmpty()) {
            planPrice = araySilverPlanYear.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    silverPlanPriceYear = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + year;
                } else {
                    silverPlanPriceYear = PriceCalculator(planPrice) + year;
                }

            } else if (LANGUAGE_CODE == 2) {

                silverPlanPriceYear = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + year;
            } else {
                silverPlanPriceYear = PriceCalculator(planPrice) + year;
            }
            hasPrice = true;
        }
        //INIT SILVER PLAN MONTH
        if (!araySilverPlanMonth.isEmpty()) {

            planPrice = araySilverPlanMonth.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    silverPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + month;
                } else {
                    silverPlanPriceMonth = PriceCalculator(planPrice) + month;
                }

            } else if (LANGUAGE_CODE == 2) {

                silverPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + month;
            } else {
                silverPlanPriceMonth = PriceCalculator(planPrice) + month;
            }
            hasPrice = true;
        }

        /*// INIT Gold PLAN Year
        if (arayGoldPlanYear.size() > 0) {
            goldPlanPriceYear = getResources().getString(R.string.yearly_txt);
            hasPrice = true;
        }

        // INIT Gold PLAN Month
        if (arayGoldPlanMonth.size() > 0) {
            goldPlanPriceMonth = getResources().getString(R.string.monthly_txt);
            hasPrice = true;
        }*/
// INIT Gold PLAN Year
        if (!arayGoldPlanYear.isEmpty()) {

            planPrice = arayGoldPlanYear.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    goldPlanPriceYear = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + year;
                } else {
                    goldPlanPriceYear = PriceCalculator(planPrice) + year;
                }

            } else if (LANGUAGE_CODE == 2) {

                goldPlanPriceYear = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + year;
            } else {
                goldPlanPriceYear = PriceCalculator(planPrice) + year;
            }
            hasPrice = true;
        }

        // INIT Gold PLAN Month
        if (!arayGoldPlanMonth.isEmpty()) {

            planPrice = arayGoldPlanMonth.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    goldPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + month;
                    //  Log.e("mytag", "943 line "+goldPlanPriceMonth);
                } else {
                    goldPlanPriceMonth = PriceCalculator(planPrice) + month;
                    //  Log.e("mytag", "946 line "+goldPlanPriceMonth);
                }

            } else if (LANGUAGE_CODE == 2) {

                goldPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + month;
                //  Log.e("mytag", "952 line "+goldPlanPriceMonth);
            } else {
                goldPlanPriceMonth = PriceCalculator(planPrice) + month;
                //  Log.e("mytag", "955 line "+goldPlanPriceMonth);
            }
            hasPrice = true;
        }
        // if (hasPrice)
        //initFragmentAdapter(hasPrice);

        // INIT Dhruv PLAN Year
        if (!arayPlatinumPlanYear.isEmpty()) {

            planPrice = arayPlatinumPlanYear.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    platinumPlanPriceYear = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + year;
                } else {
                    platinumPlanPriceYear = PriceCalculator(planPrice) + year;
                }

            } else if (LANGUAGE_CODE == 2) {

                platinumPlanPriceYear = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + year;
            } else {
                platinumPlanPriceYear = PriceCalculator(planPrice) + year;
            }
            hasPrice = true;
        }

        // INIT Dhruv PLAN Month
        if (!arayPlatinumPlanMonth.isEmpty()) {

            planPrice = arayPlatinumPlanMonth.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    platinumPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + month;
                } else {
                    platinumPlanPriceMonth = PriceCalculator(planPrice) + month;
                }

            } else if (LANGUAGE_CODE == 2) {

                platinumPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + month;
            } else {
                platinumPlanPriceMonth = PriceCalculator(planPrice) + month;
            }
            hasPrice = true;
        }

        // INIT Dhruv PLAN Year
        if (!arayPlatinumPlanYearOmf.isEmpty()) {

            planPrice = arayPlatinumPlanYearOmf.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    platinumPlanPriceYearOmf = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + year;
                } else {
                    platinumPlanPriceYearOmf = PriceCalculator(planPrice) + year;
                }

            } else if (LANGUAGE_CODE == 2) {

                platinumPlanPriceYearOmf = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + year;
            } else {
                platinumPlanPriceYearOmf = PriceCalculator(planPrice) + year;
            }
            hasPrice = true;
        }

        // INIT Dhruv PLAN Month
        if (!arayPlatinumPlanMonthOmf.isEmpty()) {

            planPrice = arayPlatinumPlanMonthOmf.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    platinumPlanPriceMonthOmf = PriceCalculator(planPrice.replace("Rs.", "\u20B9")) + month;
                } else {
                    platinumPlanPriceMonthOmf = PriceCalculator(planPrice) + month;
                }

            } else if (LANGUAGE_CODE == 2) {

                platinumPlanPriceMonthOmf = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil)) + month;
            } else {
                platinumPlanPriceMonthOmf = PriceCalculator(planPrice) + month;
            }
            hasPrice = true;
        }
        if (servicelistModal != null && servicelistModal.getPriceInRS() != null) {
            hasPrice = true;
        }
       /* // INIT Platinum PLAN Year
        if (arayPlatinumPlanYear.size() > 0) {
            platinumPlanPriceYear = getResources().getString(R.string.yearly_txt);
            hasPrice = true;
        }

        // INIT Platinum PLAN Month
        if (arayPlatinumPlanMonth.size() > 0) {
            platinumPlanPriceMonth = getResources().getString(R.string.monthly_txt);
            hasPrice = true;
        }*/


        //if (hasPrice)
        //Log.e("BillingClient", "initFragmentAdapter() before"+hasPrice);
        initFragmentAdapter(hasPrice);
        //Log.e("BillingClient", "initFragmentAdapter() after"+hasPrice);
    }

    public String PriceCalculator(String strvalue) {
        //comment by Tejinder Singh
        //  as per discuss we need to show Price as it as come from server
    /*    int lastIndex = strvalue.lastIndexOf(".");

        String[] planArray = {strvalue.substring(0, lastIndex),
                strvalue.substring(lastIndex)};
        return planArray[0];*/
        return strvalue;
    }

    @Override
    public void onYesClick(int planIndex) {
        selectPlan(planIndex);
    }

    @Override
    public void selectedPlan(int planIndex, UPIAppModel upiAppModel) {
        // it may be null if no UPI app is installed
        if(upiAppModel!=null){
            this.upiAppModel = upiAppModel;
        }

        if (planIndex == BASIC_PLAN) {
            Intent intent = new Intent();
            intent.putExtra("purchaseSilverPlan", true);
            setResult(RESULT_OK, intent);
            this.finish();
            return;
        }

        boolean isEmailIdVisible = true;
        boolean isPhnNumbVisisble = true;

        String astroShopEmail = CUtils.getAstroshopUserEmail(PurchasePlanHomeActivity.this);
        String askAQuesEmail = CUtils.getStringData(PurchasePlanHomeActivity.this, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, "");
        String askAQuesMobNo = CUtils.getStringData(PurchasePlanHomeActivity.this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, "");
        String emailId = UserEmailFetcher.getEmail(getApplicationContext());
        if (askAQuesEmail == null) {
            askAQuesEmail = "";
        }
        if (emailId == null) {
            emailId = "";
        }
        if (!askAQuesEmail.equals("") || !astroShopEmail.isEmpty() || !emailId.equals("")) {
            isEmailIdVisible = false;
        }

        if (!askAQuesMobNo.equals("")) {
            isPhnNumbVisisble = false;
        }
//        if (isFreeDhruvPlanShow) {
//            selectPlan(planIndex);
//        } else {
//            //If both email id and phone number is available
//            if (!isEmailIdVisible && !isPhnNumbVisisble) {
//                selectPlan(planIndex);
//            } else {
//                CUtils.openPersonalDetailsDialog(this, planIndex, isEmailIdVisible, isPhnNumbVisisble);
//            }
//        }
        selectPlan(planIndex);
    }

    /**
     * Handles the purchase flow for different plan types.
     * Initiates the appropriate purchase process based on plan selection.
     * 
     * @param planIndex The index of the selected plan
     */
    private void selectPlan(int planIndex) {
        dumpDataString = "ActNotificationLanding selectPlan() fullJsonDataObj=";
        if (planIndex == BASIC_PLAN) {
            Intent intent = new Intent();
            intent.putExtra("purchaseSilverPlan", true);
            setResult(RESULT_OK, intent);
            this.finish();
        }
        //if free trial available then on click of "try now for rs 0" btn this block is called
        if(planIndex == PLATINUM_PLAN_MONTHLY_FREE_TRIAL){
            FreeTrialFragment freeTrialFragment = FreeTrialFragment.newInstance(
                    servicelistModal.getFreeTrialPeriod(), // Assuming this variable holds the trial period string (e.g., "7 Days")
                    servicelistModal.getFreeTrialAmount(),
                    servicelistModal.getPriceInRS()
            );

            freeTrialFragment.setPurchasePlanListener(this);

           freeTrialFragment.show(getSupportFragmentManager(),"FreeTrialFragment");
            return;
        }

        if (planIndex == SIVLER_PLAN)
            gotBuySilverPlan();
        if (planIndex == GOLD_PLAN)
            gotBuyGoldPlan();
        if (planIndex == SIVLER_PLAN_MONTHLY)
            gotBuySilverMonthlyPlan();
        if (planIndex == GOLD_PLAN_MONTHLY)
            gotBuyGoldMonthlyPlan();
        if (planIndex == PLATINUM_PLAN)
            gotBuyPlatinumPlan();
        if (planIndex == PLATINUM_PLAN_MONTHLY)
            gotBuyPlatinumMonthlyPlan();
        if (planIndex == PLATINUM_PLAN_MONTHLY_OMF)
            gotBuyPlatinumMonthlyPlanOmf();
        if (planIndex == PLATINUM_PLAN_YEARLY_OMF)
            gotBuyPlatinumPlanYearOmf();

    }

    public void gotBuySilverPlan() {
        try {

            if (skuDetailsSilverPlanYear != null) {
                requestCode = SUB_RC_REQUEST_SILVER_PLAN_YEAR;
                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsSilverPlanYear)
                                        .setOfferToken(skuDetailsSilverPlanYear.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
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
                                        .setOfferToken(skuDetailsSilverPlanMonth.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    public void gotBuyGoldPlan() {
        try {
            if (skuDetailsGoldPlanYear != null) {
                requestCode = SUB_RC_REQUEST_GOLD_PLAN_YEAR;

                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsGoldPlanYear)
                                        .setOfferToken(skuDetailsGoldPlanYear.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            // Log.e("RESPONSE_ERROR",e.getMessage());
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
                                        .setOfferToken(skuDetailsGoldPlanMonth.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            // Log.e("RESPONSE_ERROR",e.getMessage());
        }
    }

    public void gotBuyPlatinumPlan() {
        dumpDataString = dumpDataString + " gotBuyPlatinumPlan()";
        try {
            if (skuDetailsPlatinumPlanYear != null) {
                requestCode = SUB_RC_REQUEST_PLATINUM_PLAN_YEAR;

                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsPlatinumPlanYear)
                                        .setOfferToken(skuDetailsPlatinumPlanYear.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                dumpDataString = dumpDataString + " gotBuyPlatinumPlan() prepayment responseCode=" + responseCode;
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
        CUtils.postDumpDataToServer(PurchasePlanHomeActivity.this, dumpDataString);
    }

    public void gotBuyPlatinumMonthlyPlan() {
        fcmLabel = "indian_international";
        if (servicelistModal.isFreeTrialAvailable()) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_AI_FREE_TRIAL_UPGRADE_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        } else {
            if (selectedPlanIndex == KUNDLI_AI_PLUS_PLAN_INDEX) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_FROM_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            } else if (selectedPlanIndex == DHRUV_PLAN_INDEX) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_FROM_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }
        }
        if (upiAppModel != null) {
            if (upiAppModel.getPayMethodName().equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
                getOrderIdNew();
            } else {
                paymentModeStr = upiAppModel.getPayMethodName();
                getApiCallForPhonePayIntentUrl(upiAppModel.getPayMethodName());
            }
        } else {
            getOrderIdNew();
        }

//        Intent intent = new Intent(PurchasePlanHomeActivity.this, SubscriptionPaymentActivity.class);
//        intent.putExtra("service_model", servicelistModal);
//        intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_PARAMS, purchaseSource);
//        startActivity(intent);
//        String internationalPayMode = CUtils.getStringData(PurchasePlanHomeActivity.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
//        String domesticPayMode = CUtils.getStringData(PurchasePlanHomeActivity.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
//        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
//        if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//            // Check if domestic payment mode is Razorpay or Google
//            if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                fcmLabel = "indian";
//                getOrderIdNew();
//            } else {
//                googlePlanBuy();
//            }
//        } else {
//            // Check if international payment mode is Razorpay or Google
//            if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                fcmLabel = "international";
//                getOrderIdNew();
//            } else
//                googlePlanBuy();
//        }
    }


    public void googlePlanBuy() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_GOOGLE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        dumpDataString = dumpDataString + " gotBuyPlatinumMonthlyPlan()";
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
                //Log.d("BillingClient", "gotBuyPlatinumMonthlyPlan() responseCode="+responseCode);
                dumpDataString = dumpDataString + " gotBuyPlatinumMonthlyPlan() prepayment responseCode=" + responseCode;
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            //Log.e("BillingClient", e.toString());
        }
        CUtils.postDumpDataToServer(PurchasePlanHomeActivity.this, dumpDataString);

    }

    public void gotBuyPlatinumMonthlyPlanOmf() {
        try {
            if (skuDetailsPlatinumPlanMonthOmf != null) {
                requestCode = SUB_RC_REQUEST_PLATINUM_PLAN_MONTH_OMF;

                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsPlatinumPlanMonthOmf)
                                        .setOfferToken(skuDetailsPlatinumPlanMonthOmf.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    public void gotBuyPlatinumPlanYearOmf() {
        try {
            if (skuDetailsPlatinumPlanYearOmf != null) {
                requestCode = SUB_RC_REQUEST_PLATINUM_PLAN_YEAR_OMF;

                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(skuDetailsPlatinumPlanYearOmf)
                                        .setOfferToken(skuDetailsPlatinumPlanYearOmf.getSubscriptionOfferDetails().get(0).getOfferToken())
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchasePlanHomeActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    private String calculatePayloadKey(String planTyle) {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(planTyle);
        sb.append(calendar.getTime().getTime());

        return sb.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("verifyPurchase", "requestCode=" + requestCode + " resultCode=" + resultCode);
        /*
         * Toast.makeText( DialogPlanUpgradeNew.this,"i m here",
         * Toast.LENGTH_LONG).show();
         */
        if (requestCode == SUB_RC_REQUEST_GOLD_PLAN_YEAR
                || requestCode == SUB_RC_REQUEST_SILVER_PLAN_YEAR
                || requestCode == SUB_RC_REQUEST_SILVER_PLAN_MONTH
                || requestCode == SUB_RC_REQUEST_GOLD_PLAN_MONTH
                || requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR
                || requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH
                || requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH_OMF
                || requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR_OMF
        ) {
            // do nothing now
        }
       else if (requestCode == PHONE_PAY_GATEWAY_CALLBACK) {
            try {
                if (data != null) {
                    Log.d("UPI", "UPI App result" + data.getData());
                    //call order status api
                    getOrderStatusApi();
                }
            } catch (Exception e) {
                Log.d("UPI", "UPI App result" + e.toString());
            }
        } else {
            this.finish();

        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        String astroSageUserId = CUtils.getUserName(this);
        dumpDataString = dumpDataString + " onPurchasesUpdated() responseCode=" + billingResult.getResponseCode() + " astroSageUserId=" + astroSageUserId;

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            dumpDataString = dumpDataString + " ResponseOK";
            String plan = "";
            String freeTrialPeriodTxt = "";
            String price = "0";
            String priceCurrencycode = "INR";
            String formattedPrice = "";
            if (requestCode == SUB_RC_REQUEST_GOLD_PLAN_YEAR) {
                SavePlaninPreference(GOLD_PLAN);
                plan = CGlobalVariables.GOLD_PLAN_VALUE_YEAR;

                if (arayGoldPlanYear != null && arayGoldPlanYear.size() > 0) {
                    price = arayGoldPlanYear.get(price_amount_micros);// 2
                    priceCurrencycode = arayGoldPlanYear.get(price_currency_code);
                    if (arayGoldPlanYear.size() > 7) {
                        freeTrialPeriodTxt = arayGoldPlanYear.get(isFreetimeperiod);
                    }
                }
                double dPrice = 0.0;
                try {
                    if (price != null && price.trim().length() > 0) {
                        dPrice = Double.valueOf(price);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_GOLD_PLAN_YEARLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_SILVER_PLAN_YEAR) {
                SavePlaninPreference(SIVLER_PLAN);
                plan = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;

                if (araySilverPlanYear != null && araySilverPlanYear.size() > 0) {
                    price = araySilverPlanYear.get(price_amount_micros);// 2
                    priceCurrencycode = araySilverPlanYear.get(price_currency_code);
                    if (araySilverPlanYear.size() > 7) {
                        freeTrialPeriodTxt = araySilverPlanYear.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_SILVER_PLAN_YEARLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_GOLD_PLAN_MONTH) {
                SavePlaninPreference(GOLD_PLAN_MONTHLY);
                plan = CGlobalVariables.GOLD_PLAN_VALUE_MONTH;

                if (arayGoldPlanMonth != null && arayGoldPlanMonth.size() > 0) {
                    price = arayGoldPlanMonth.get(price_amount_micros);// 2
                    priceCurrencycode = arayGoldPlanMonth.get(price_currency_code);
                    if (arayGoldPlanMonth.size() > 7) {
                        freeTrialPeriodTxt = arayGoldPlanMonth.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_GOLD_PLAN_MONTHLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_SILVER_PLAN_MONTH) {
                SavePlaninPreference(SIVLER_PLAN_MONTHLY);
                plan = CGlobalVariables.SILVER_PLAN_VALUE_MONTH;

                if (araySilverPlanMonth != null && araySilverPlanMonth.size() > 0) {
                    price = araySilverPlanMonth.get(price_amount_micros);// 2
                    priceCurrencycode = araySilverPlanMonth.get(price_currency_code);
                    if (araySilverPlanMonth.size() > 7) {
                        freeTrialPeriodTxt = araySilverPlanMonth.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_SILVER_PLAN_MONTHLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH) {
                SavePlaninPreference(PLATINUM_PLAN_MONTHLY);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;

                if (arayPlatinumPlanMonth != null && arayPlatinumPlanMonth.size() > 0) {
                    price = arayPlatinumPlanMonth.get(price_amount_micros);// 2
                    priceCurrencycode = arayPlatinumPlanMonth.get(price_currency_code);
                    formattedPrice = arayPlatinumPlanMonth.get(PRICE);
                    if (arayPlatinumPlanMonth.size() > 7) {
                        freeTrialPeriodTxt = arayPlatinumPlanMonth.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR) {
                SavePlaninPreference(PLATINUM_PLAN);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR;

                if (arayPlatinumPlanYear != null && arayPlatinumPlanYear.size() > 0) {
                    price = arayPlatinumPlanYear.get(price_amount_micros);// 2
                    priceCurrencycode = arayPlatinumPlanYear.get(price_currency_code);
                    formattedPrice = arayPlatinumPlanYear.get(PRICE);
                    if (arayPlatinumPlanYear.size() > 7) {
                        freeTrialPeriodTxt = arayPlatinumPlanYear.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_YEARLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH_OMF) {
                Log.e("verifyPurchase", "SUB_RC_REQUEST_PLATINUM_PLAN_MONTH_OMF enter");
                SavePlaninPreference(PLATINUM_PLAN_MONTHLY_OMF);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH_OMF;

                if (arayPlatinumPlanMonthOmf != null && arayPlatinumPlanMonthOmf.size() > 0) {
                    price = arayPlatinumPlanMonthOmf.get(price_amount_micros);// 2
                    priceCurrencycode = arayPlatinumPlanMonthOmf.get(price_currency_code);
                    if (arayPlatinumPlanMonthOmf.size() > 7) {
                        freeTrialPeriodTxt = arayPlatinumPlanMonthOmf.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_OMF_SUCCESS, null, dPrice, purchaseSource);
                Log.e("verifyPurchase", "SUB_RC_REQUEST_PLATINUM_PLAN_MONTH_OMF enter");
            }
            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR_OMF) {
                SavePlaninPreference(PLATINUM_PLAN_YEARLY_OMF);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR_OMF;

                if (arayPlatinumPlanYearOmf != null && arayPlatinumPlanYearOmf.size() > 0) {
                    price = arayPlatinumPlanYearOmf.get(price_amount_micros);// 2
                    priceCurrencycode = arayPlatinumPlanYearOmf.get(price_currency_code);
                    if (arayPlatinumPlanYearOmf.size() > 7) {
                        freeTrialPeriodTxt = arayPlatinumPlanYearOmf.get(isFreetimeperiod);
                    }
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
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_YEARLY_OMF_SUCCESS, null, dPrice, purchaseSource);
            }

            //Do not show update purchase plan in ActAppModule
            CUtils.saveBooleanData(PurchasePlanHomeActivity.this, CGlobalVariables.doNotShowMessageAgainForUpdatePlan, true);
            CUtils.setDoNotShowAccountUpgradePopupValueInPreference(getApplicationContext(), false);
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                dumpDataString = dumpDataString + " signature=" + purchase.getSignature() + " purchaseData=" + purchase.getOriginalJson();
                if (purchase != null) {
                    gotoThanksPage(purchase, plan, price, priceCurrencycode, formattedPrice, freeTrialPeriodTxt);
                }
            }

        } else {
            paymentFailedFrom = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE;
            showRazorpayFailureDialog();
            dumpDataString = dumpDataString + " ResponseFAIL";
            Log.e("BillingClient", "onPurchasesUpdated() FAIL");

//           Intent tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreenNew.class);
//            tppsIntent.putExtra(FREE_QUESTIONS_RECEIVED_KEY,"100");
//        tppsIntent.putExtra("SIGNATURE", signature);
//        tppsIntent.putExtra("PURCHASE_DATA", purchaseData);
//        tppsIntent.putExtra("PLAN", CGlobalVariables.GOLD_PLAN_VALUE_MONTH);
//        tppsIntent.putExtra("price", "100");
//        tppsIntent.putExtra("priceCurrencycode", "300");
//        tppsIntent.putExtra("freetrialperiod", "1");
//        startActivity(tppsIntent);
//        finish();
        }
        CUtils.postDumpDataToServer(PurchasePlanHomeActivity.this, dumpDataString);
    }


    private String getQuestionCountForPlan(String plan) {
        try {
            switch (plan) {
                case CGlobalVariables.SILVER_PLAN_VALUE_MONTH:
                case CGlobalVariables.SILVER_PLAN_VALUE_YEAR:
                    return CUtils.getStringData(this, CGlobalVariables.SILVER_PLAN_QUES_COUNT_KEY, "100");
                case CGlobalVariables.GOLD_PLAN_VALUE_MONTH:
                case CGlobalVariables.GOLD_PLAN_VALUE_YEAR:
                    return CUtils.getStringData(this, CGlobalVariables.GOLD_PLAN_QUES_COUNT_KEY, "200");
                case CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH:
                case CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR:
                case CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH_OMF:
                case CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR_OMF:
                    return CUtils.getStringData(this, CGlobalVariables.DHRUV_PLAN_QUES_COUNT_KEY, "1000");

            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private void gotoThanksPage(Purchase purchase, String plan, String price, String priceCurrencycode, String formattedPrice, String freetrialperiodtxt) {
        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();
        Log.e("BillingClient", "gotoThanksPage()");
        if (CUtils.isConnectedWithInternet(PurchasePlanHomeActivity.this)) {
            verifyPurchaseFromService(price, priceCurrencycode, formattedPrice, freetrialperiodtxt);
        }

        getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).edit()
                .putString("VALUE", purchaseData).commit();// ADDED BY BIJENDRA

        Intent tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreen.class);
        tppsIntent.putExtra("SIGNATURE", signature);
        tppsIntent.putExtra("PURCHASE_DATA", purchaseData);
        tppsIntent.putExtra("PLAN", plan);
        tppsIntent.putExtra("price", price);
        tppsIntent.putExtra("priceCurrencycode", priceCurrencycode);
        tppsIntent.putExtra("freetrialperiod", freetrialperiodtxt);
        startActivity(tppsIntent);
        this.finish();
    }


    private void verifyPurchaseFromService(String price, String priceCurrencycode, String formattedPrice, String freetrialperiod) {
        try {
            String phoneNo = com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this);// varta logged-in phone number
            CUtils.saveStringData(this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, phoneNo);
            CUtils.saveStringData(this, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, CUtils.getUserName(this));

            Log.e("BillingClient", "verifyPurchaseFromService() enter");

            String astroSageUserId = CUtils.getUserName(this);
            Intent pvsIntent = new Intent(getApplicationContext(),
                    PurchaseVerificationService.class);
            pvsIntent.putExtra("SIGNATURE", signature);
            pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
            pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);
            pvsIntent.putExtra("price", price);
            pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);
            pvsIntent.putExtra("formattedPrice", formattedPrice);
            pvsIntent.putExtra("freetrialperiod", freetrialperiod);
            pvsIntent.putExtra("openForAIChat", false);
            pvsIntent.putExtra("source", purchaseSource);
            startService(pvsIntent);
        } catch (Exception e) {
            Log.e("BillingClient", "verifyPurchaseFromService() Exception=" + e);
        }
    }

    private void SavePlaninPreference(int newPlanIndex) {
        SharedPreferences sharedPreferences = PurchasePlanHomeActivity.this
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newPlanIndex == SIVLER_PLAN) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.SILVER_PLAN_VALUE_YEAR);
        } else if (newPlanIndex == GOLD_PLAN) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.GOLD_PLAN_VALUE_YEAR);
        } else if (newPlanIndex == GOLD_PLAN_MONTHLY) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.GOLD_PLAN_VALUE_MONTH);
        } else if (newPlanIndex == SIVLER_PLAN_MONTHLY) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.SILVER_PLAN_VALUE_MONTH);
        } else if (newPlanIndex == PLATINUM_PLAN) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR);
        } else if (newPlanIndex == PLATINUM_PLAN_MONTHLY) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH);
        } else if (newPlanIndex == PLATINUM_PLAN_MONTHLY_OMF) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH_OMF);
        } else if (newPlanIndex == PLATINUM_PLAN_YEARLY_OMF) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR_OMF);
        }else if (newPlanIndex == KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH);
        }
        sharedPrefEditor.commit();
    }

    public HashMap<String, Integer> getIcons() {
        HashMap<String, Integer> icons = new HashMap<String, Integer>();

        icons.put("plan_second_icon", R.drawable.plan_second_icon);
        icons.put("plan_free_ads_icon", R.drawable.plan_free_ads_icon);
        icons.put("plan_horoscope_icon", R.drawable.plan_horoscope_icon);
        icons.put("plan_kundli_icon", R.drawable.plan_kundli_icon);
        icons.put("plan_mobile_icon", R.drawable.plan_mobile_icon);
        icons.put("plan_notes_icon", R.drawable.plan_notes_icon);
        icons.put("plan_work_sheet_icon", R.drawable.plan_work_sheet_icon);
        icons.put("plan_all_features_icon", R.drawable.plan_all_features_icon);
        icons.put("plan_cloud_icon", R.drawable.plan_cloud_icon);
        icons.put("plan_discount_icon", R.drawable.plan_discount_icon);
        icons.put("plan_shipping_icon", R.drawable.plan_shipping_icon);
        icons.put("plan_subscription_icon", R.drawable.plan_subscription_icon);
        icons.put("plan_download_icon", R.drawable.plan_download_icon);
        icons.put("plan_name_address_icon", R.drawable.plan_name_address_icon);
        icons.put("free_chat_icon", R.drawable.icon_free_white);
        icons.put("ai_astrologer", R.drawable.ai_astrologer);

        icons.put("default", R.drawable.plan_kundli_icon);
        return icons;
    }

    public void takeActionAfterPhoneVerified() {
        /*if (enableMonthlySubscriptionValue) {
            openPlanSelectDialog();
        } else {
            // 1 for yearly plan subscription
            getPlatinumPlan(1);
        }*/
    }

    public static final int BASIC_PLAN_INDEX = 0;
    public static final int KUNDLI_AI_PLUS_PLAN_INDEX = 1;
    public static final int DHRUV_PLAN_INDEX = 2;
    int selectedPlanIndex = KUNDLI_AI_PLUS_PLAN_INDEX;

    private void getAIKundliPlusPlanServices(boolean fromScroll) { // if from ViewPagerScroll only need To update service details
        String serviceObj = "";
        if (selectedPlanIndex == KUNDLI_AI_PLUS_PLAN_INDEX) {
            serviceObj = com.ojassoft.astrosage.varta.utils.CUtils.getPlusPlanServiceDetail();
        } else if (selectedPlanIndex == DHRUV_PLAN_INDEX) {
            serviceObj = com.ojassoft.astrosage.varta.utils.CUtils.getPremiumPlanServiceDetail();
        }else{return;}
        if (!TextUtils.isEmpty(serviceObj)) {
            parseServiceDetails(serviceObj, fromScroll);
        } else {
            if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(PurchasePlanHomeActivity.this)) {
                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), PurchasePlanHomeActivity.this);
            } else {
                showProgressBar();
                String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.SERVICE_LIST_KUNDLI_AI_PLANS;
                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                        PurchasePlanHomeActivity.this, false, CUtils.planDetailsRequestParams(PurchasePlanHomeActivity.this), GET_SERVICE_DETAILS).getMyStringRequest();
                stringRequest.setShouldCache(false);
                queue.add(stringRequest);
            }
        }
    }

    private void updateServiceDetails(String JObject) {
        try {
            JSONObject obj = new JSONObject(JObject);
            servicelistModal = new ServicelistModal();

            servicelistModal.setTitle(obj.getString("Title"));
            servicelistModal.setServiceId(obj.getString("ServiceId"));
            servicelistModal.setPriceInDollor(obj.getString("PriceInDollor"));
            servicelistModal.setPriceInRS(obj.getString("PriceInRS"));
            servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
            servicelistModal.setFreeTrialPeriod(obj.getInt("FreeTrialPeriod"));
            servicelistModal.setFreeTrialNotifyPeriod(obj.getInt("FreeTrialNotifyPeriod"));
            servicelistModal.setQuestionLimit(obj.getString("QuestionLimit"));
            servicelistModal.setFreeTrialAmount(obj.getInt("FreeTrialAmount"));
            servicelistModal.setPhonePeFreeTrialAmount(obj.getString("PhonePeFreeTrialAmount"));
            amountToPay = PriceInRS;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;

            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
                amountToPay = PriceInDollor;
                currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
            }
        } catch (Exception e) {
            //
        }
    }

    private void parseServiceDetails(String JObject, boolean fromScroll) {
        try {
            updateServiceDetails(JObject);
            if (!fromScroll)
                showProductsDetail_new();
        } catch (Exception e) {
            //
        }
    }

    boolean getOrderIdReq,isRetryPayment;
    boolean getPhonePeOrderIdReq;
    private String fcmLabel = "";

    /**
     * Fetches a new order ID from the server.
     * This method initiates a request to the backend to generate an order ID,
     * typically used for payment processing (e.g., with Razorpay).
     * It handles currency selection based on the user's country and checks for internet connectivity.
     * If the user is not logged in, it may trigger a background login process.
     */
    public void getOrderIdNew() {
        getOrderIdReq = false; // Reset request flag
        // Track FCM event for plan upgrade attempt via Razorpay
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY + fcmLabel, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        // Determine currency based on user's country code
        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
        if (TextUtils.isEmpty(countryCode) || countryCode.equals(CGlobalVariables.COUNTRY_CODE_IND)) {
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
        } else {
            amountToPay = PriceInDollor;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
        }

        // Check for internet connection
        if (!CUtils.isConnectedWithInternet(PurchasePlanHomeActivity.this)) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), PurchasePlanHomeActivity.this);
        } else {
            showProgressBar(); // Display progress indicator
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);

            // Log.e("source_check", "getOrderIdNew: PurchasePlanHomeACtivity - "+purchaseSource );
            // Prepare and make the API call to get the order ID
            Call<ResponseBody> call = api.getOrderId(CUtils.getParamsForGenerateOrderId(PurchasePlanHomeActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, purchaseSource,isRetryPayment));
            isRetryPayment = false;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar(); // Hide progress indicator
                    try {
                        String myResponse = response.body().string();
                        JSONObject obj = new JSONObject(myResponse);

                        //Log.e("TestPayment=> ", obj.toString());
                        if (obj.has("status")) {
                            // Handle cases where a status code indicates a need for re-login
                            if (obj.optString("status").equalsIgnoreCase("100")) {
                                getOrderIdReq = true; // Set flag to retry getOrderIdNew after login
                                // Register a broadcast receiver to listen for login completion
                                LocalBroadcastManager.getInstance(PurchasePlanHomeActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                startBackgroundLoginService(); // Initiate background login
                            }
                        } else {
                            String result = obj.getString("Result");
                            if(result.equalsIgnoreCase("1")) {
                                // Successfully retrieved order details
                                orderId = obj.getString("OrderId");
                                PriceInDollor = obj.getString("PriceToPay");
                                PriceInRS = obj.getString("PriceRsToPay");
                                subscriptionId = obj.getString("SubscriptionId");
                                servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
                                startPayment(); // Proceed to payment
                            }else {
                                String errorMsg = obj.getString("Message");
                                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(mainlayout, errorMsg+" ("+result+")", PurchasePlanHomeActivity.this);

                            }
                        }
                    } catch (Exception e) {
                        // Log.e("TestPayment=> ", e.toString());
                        CUtils.showSnackbar(mainlayout, "Error parsing order response: " + e.getMessage(), PurchasePlanHomeActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Log.e("TestPayment=> f", t.toString());
                    hideProgressBar(); // Hide progress indicator
                    CUtils.showSnackbar(mainlayout, "Failed to get order ID: " + t.getMessage(), PurchasePlanHomeActivity.this);
                }
            });
        }
    }
    /**
     * Generates a new order ID for Razorpay payment processing.
     */
    public void getApiCallForPhonePayIntentUrl(String paymentMethodName) {
        getPhonePeOrderIdReq = false;
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_INTENT_FLOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        // Determine currency based on user's country code
        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
        if (TextUtils.isEmpty(countryCode) || countryCode.equals(CGlobalVariables.COUNTRY_CODE_IND)) {
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
        } else {
            amountToPay = PriceInDollor;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
        }

        if (!CUtils.isConnectedWithInternet(this)) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), this);
            return;
        }
        showProgressBar();
        Map<String, String> mapNew = CUtils.getParamsForGenerateOrderId(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, purchaseSource,isRetryPayment);
        isRetryPayment = false;
        switch (paymentMethodName) {
            case com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PHONE:
                //com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIA_PHONEPE_UPI, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PHONEPE);
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAY_MODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONEPE);

                break;
            case com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_GPAY:
                //com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIA_GPAY_UPI, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_GOOGLE_PAY);
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAY_MODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GPAY);
                break;
            case com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PAYTM:
                //com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIA_PAYTM_UPI, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PAYTM);
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAY_MODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAYTM);
                break;
            case com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_BHIM:
                //com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIA_PAYTM_UPI, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_BHIM);
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAY_MODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.BHIM);
                break;
            case com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_AMAZON:
                //com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIA_PAYTM_UPI, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PAYTM);
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAY_MODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.AMAZON);
                break;
            case com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_CRED:
                //com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.VIA_PAYTM_UPI, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_CRED);
                mapNew.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAY_MODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.CRED);
                break;
        }
        mapNew.put("ordertype", "1");
        mapNew.put("reguserid", com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(this));
        mapNew.put("orderfromdomain", CGlobalVariables.AI_PASS_PURCHASE);
        mapNew.put("asuserid",  com.ojassoft.astrosage.utils.CUtils.getUserName(this));
        mapNew.put("freetrialamount",  servicelistModal.getPhonePeFreeTrialAmount());

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getPhonePeGatewayOrderId(mapNew);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                //Log.d("testApi","respose==>>"+response.body().toString());
                handleOrderIdResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(mainlayout, t.toString(), PurchasePlanHomeActivity.this);
            }
        });
    }
    /**
     * Handles the order ID response from the server.
     */
    private void handleOrderIdResponse(retrofit2.Response<ResponseBody> response) {
        try {
            String myResponse = response.body().string();
            JSONObject root = new JSONObject(myResponse);
            if (root.has("status") && root.optString("status").equalsIgnoreCase("100")) {
                handleBackgroundLoginRequired();
            } else {
                try {
                    // Merchant side order id
                    JSONObject merchantResp = root.getJSONObject("merchantResponse");
                    orderId = merchantResp.getString("OrderId");   // 2025120524540591
                    merchantOrderId = merchantResp.getString("merchantOrderId");   // 2025120524540591
                    IsFreeTrialAvailable = merchantResp.getString("IsFreeTrialAvailable");   // 2025120524540591

                    // PhonePe side details
                    JSONObject phonepeGateWayResp = root.getJSONObject("phonepeResponse");
                    String intentUrl = phonepeGateWayResp.getString("intentUrl");        // upi://mandate?...
                    String phonePeorderId = phonepeGateWayResp.getString("orderId");     // OMO2512...

                    // Just to check
                    Log.d("UPI", "orderId = " + orderId);
                    Log.d("UPI", "phonePeorderId  = " + phonePeorderId);
                    Log.d("UPI", "intentUrl       = " + intentUrl);
                    //open upi payment app(Phone Pe, G Pay, Paytm) via intent url

                    openPaymentApp(intentUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            CUtils.showSnackbar(mainlayout, e.toString(), this);
        }
    }
    /**
     * Handles background login requirement.
     */
    private void handleBackgroundLoginRequired() {
        getPhonePeOrderIdReq = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService,
                new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        startBackgroundLoginService();
    }

    //open upi payment app(Phone Pe, G Pay, Paytm) via intent url
    private void openPaymentApp(String intentUrl) {
        Uri uri = Uri.parse(
                intentUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (paymentModeStr.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
            intent.setPackage(UPIAppChecker.PACKAGE_PAYTM); // Force open in Paytm app
        } else if (paymentModeStr.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_GPAY)) {
            intent.setPackage(UPIAppChecker.PACKAGE_GOOGLE_PAY); // Force open in Google Pay app
        } else if (paymentModeStr.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PHONE)) {
            intent.setPackage(UPIAppChecker.PACKAGE_PHONEPE); // Force open in PhonePe app
        }else if (paymentModeStr.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_BHIM)) {
            intent.setPackage(UPIAppChecker.PACKAGE_BHIM);
        }else if (paymentModeStr.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_AMAZON)) {
            intent.setPackage(UPIAppChecker.PACKAGE_AMAZON);
        }else if (paymentModeStr.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_CRED)) {
            intent.setPackage(UPIAppChecker.PACKAGE_CRED);
        }
        try {
            startActivityForResult(intent, PHONE_PAY_GATEWAY_CALLBACK);
        } catch (Exception e) {
            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is used to get the order status of the UPI transaction.
     * It makes a network call to the server and retrieves the order status.
     * If the order status is "SUCCESS", then it shows a success dialog.
     * If the order status is "FAILURE", then it shows a failure dialog.
     * If the order status is "PENDING", then it retries the network call after a delay of 10 seconds.
     */
    private void getOrderStatusApi() {
     showProgressBar();
        ApiList api = RetrofitClient.getInstance3().create(ApiList.class);
        Call<ResponseBody> call = api.getUpiOrdersatusApi(getParamsForOrderStatus());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    String myResponse = response.body().string();
                    Log.d("UPI", "myResponse"+myResponse);
                    JSONObject obj = new JSONObject(myResponse);
                    if (obj.getString("state").equals("COMPLETED")) {

                        String phonepeOrderId = obj.getString("phonepeOrderId");
                        String phonepeid = obj.getString("phonepeid");
                        String od = obj.getString("OrderId");
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_INTENT_FLOW_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        onSubscriptionPurchaseSuccess(phonepeOrderId,phonepeid,od,"phonepe_gateway");
                    } else  {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_INTENT_FLOW_FAILURE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        // Show a dialog to inform the user about the payment failure
                        //  if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1) {
                        paymentFailedFrom = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE;
                        // }
                        showRazorpayFailureDialog();
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                hideProgressBar();
            }
        });

    }
    // generate params for upi payments order status
    private Map<String, String> getParamsForOrderStatus() {
        Map<String, String> mapNew = new HashMap<>();
        mapNew.put("orderId", merchantOrderId);//only in case of subcription
        mapNew.put("isfreetrialavailable",IsFreeTrialAvailable );
        //mapNew.put("merchantOrderId", merchantOrderId);
        mapNew.put("currency", com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA);
        mapNew.put("freetrialamount", servicelistModal.getPhonePeFreeTrialAmount());
        mapNew.put("PriceRs", String.valueOf(servicelistModal.getPriceInRS()));
        mapNew.put("Price", String.valueOf(servicelistModal.getPriceInDollor()));
        mapNew.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(PurchasePlanHomeActivity.this));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(mapNew);
    }
    /**
     * Starts a background service to handle user login.
     * This is typically called when an action requires the user to be logged in,
     * and the login status needs to be refreshed or established.
     */
    private void startBackgroundLoginService() {
        try {
            // Check if user is already logged in before starting the service
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
            // Catch any exception during service start, though specific logging might be better
            android.util.Log.e("LoginService", "Error starting Loginservice: " + e.getMessage());
        }
    }

    /**
     * BroadcastReceiver to listen for results from the background login service.
     * If login is successful and an order ID request was pending (`getOrderIdReq` is true),
     * it retries fetching the order ID.
     */
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String status = intent.getStringExtra("status");
                // Check if login was successful
                if (status != null && status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                    // If an order ID request was pending, retry it
                    if (getOrderIdReq ) {
                        getOrderIdNew();
                    }
                    if(getPhonePeOrderIdReq){
                        if(upiAppModel!=null){
                            getApiCallForPhonePayIntentUrl(upiAppModel.getPayMethodName());
                        }
                    }
                }
                // Unregister the receiver after handling the broadcast
                if (mReceiverBackgroundLoginService != null) {
                    LocalBroadcastManager.getInstance(PurchasePlanHomeActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            } catch (Exception e) {
                // Catch any exception during broadcast handling
                android.util.Log.e("LoginReceiver", "Error in mReceiverBackgroundLoginService: " + e.getMessage());
            }
        }
    };

    /**
     * Initiates the payment process using Razorpay.
     * Sets up Razorpay checkout with order details, prefill data (contact, email),
     * and payment options.
     */
    private void startPayment() {
        Activity activity = this;

        // Prepare user contact information
        String phoneNo = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this) + com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this);
        String astrosageId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);

        // Determine email address, defaulting if username is not an email
        String email="";
        if (astrosageId.contains("@")) {
            email = astrosageId;
        }

        final Checkout checkout = new Checkout();
        checkout.setFullScreenDisable(true); // Optional: Configure full-screen mode

        // Set Razorpay Key ID from resources
        checkout.setKeyID(getResources().getString(R.string.razorpay_key));
        try {
            JSONObject options = new JSONObject();
            // Basic payment details
            options.put("name", "AstroSage AI");
            options.put("description", "Monthly Subscription"); // Consider making this dynamic based on plan
            options.put("theme.color", "#ff6f00");
            options.put("currency", currency); // Currency set in getOrderIdNew
            // options.put("amount", "50000"); // Amount should be dynamic if plans vary; Razorpay expects amount in paisa/subunits
            options.put("subscription_id", subscriptionId); // Subscription ID obtained from getOrderIdNew
            if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(PurchasePlanHomeActivity.this).equals(COUNTRY_CODE_IND)){
                options.put("checkout_config_id", "config_RqFj9XRuaSWUgl");
            }
            // Prefill user contact information
            JSONObject prefill = new JSONObject();
            prefill.put("contact", phoneNo);
            if(!TextUtils.isEmpty(email)){
                prefill.put("email", email);
            }
            options.put("prefill", prefill);

            // Configure UPI payment flow (Intent flow)
            JSONObject upi = new JSONObject();
            upi.put("flow", "intent");
            options.put("upi", upi);

            // Enable various payment methods
            JSONObject method = new JSONObject();
            method.put("upi", true);
            method.put("card", true);
            method.put("netbanking", true);
            method.put("wallet", true);
            options.put("method", method);

            // Add notes for tracking and webhook purposes
            JSONObject notes = new JSONObject();
            notes.put("orderId", orderId);
            notes.put("asuserid", astrosageId);
            notes.put("name", astrosageId); // Consider if a display name is more appropriate
            notes.put("orderType", "1"); // "1" might signify subscription or a specific plan type
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("source", "ak_sub"); // Source identifier
            notes.put("subscriptionId", subscriptionId);
            notes.put("osname", "ANDROID");
            notes.put("isfreetrialavailable",servicelistModal.isFreeTrialAvailable());
            notes.put("freetrialperiod", servicelistModal.getFreeTrialPeriod());
            notes.put("freetrialnotifyperiod", servicelistModal.getFreeTrialNotifyPeriod());
            notes.put("orderFromDomain", "AK_KUNDLI_AI_PLAN_PURCHASE"); // Specific domain for this purchase flow
            options.put("notes", notes);

             //Log.d("testPyaement", "payment started menu"+options);
            // Open Razorpay checkout
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error starting payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResponse(String response, int method) {
        //Log.d("testRazorpay", "response  :-  " + response);

        hideProgressBar();
        if (method == GET_SERVICE_DETAILS) {
            try {
                JSONArray array = new JSONArray(response);
                JSONObject plusService = array.getJSONObject(0);//Kundli AI+ plan service
                JSONObject premiumService = array.getJSONObject(1); // Dhruv plan service
                com.ojassoft.astrosage.varta.utils.CUtils.setPlusPlanServiceDetail(plusService.toString());
                com.ojassoft.astrosage.varta.utils.CUtils.setPremiumPlanServiceDetail(premiumService.toString());
              getAIKundliPlusPlanServices(false);
            } catch (Exception e) {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            // Extract subscription details from payment data
           // String paymentId = paymentData.getPaymentId();
            String signature = paymentData.getSignature();
            //String orderId = paymentData.getOrderId();
            onSubscriptionPurchaseSuccess("",signature,"",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY);

            //Toast.makeText(this, "Subscription activated successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Toast.makeText(this, "Error processing payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //called in both cases phonepe and razorpay success after payment
    private void onSubscriptionPurchaseSuccess(String paymentId, String signature, String orderIdNew,String isSucessFrom) {
        try {
            // Log payment details
            Log.d("TestRazorPayPayment", "Payment successful: " + paymentId);

            // Save subscription details to your backend and local storage
            // Update UI to show subscription status
            String plan="";
            String ecommerceLabel="" ;

            if(selectedPlanIndex==KUNDLI_AI_PLUS_PLAN_INDEX) {
                CUtils.storeUserPurchasedPlanInPreference(this, KUNDLI_AI_PRIMIUM_PLAN_ID_11);
                SavePlaninPreference(KUNDLI_AI_PRIMIUM_PLAN_ID_11);
                plan = CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH;
                if (servicelistModal.isFreeTrialAvailable()) {
                    if (CUtils.is3MonthSubsEnabled(this)) {
                        ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_FREE_TRIAL_3_months_PAYMENT_SUCCESS_RAZORPAY;
                    } else {
                        ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_FREE_TRIAL_PAYMENT_SUCCESS_RAZORPAY;
                    }
                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_PAYMENT_SUCCESS_RAZORPAY;
                }
            } else if (selectedPlanIndex == DHRUV_PLAN_INDEX) {

                String showAIPassPlan = CUtils.getStringData(PurchasePlanHomeActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_AI_PASS_PLAN_KEY,"0");
                if(showAIPassPlan.equals("1")) {
                    com.ojassoft.astrosage.varta.utils.CUtils.setIsKundliAiProPlan(PurchasePlanHomeActivity.this,true);
                }
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.storeUserPurchasedPlanInPreference(this, PLATINUM_PLAN_ID_10);
                SavePlaninPreference(PLATINUM_PLAN_MONTHLY);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;
                ecommerceLabel =  com.ojassoft.astrosage.varta.utils.CGlobalVariables.DHRUV_PAYMENT_SUCCESS_RAZORPAY;
            }


            double actualrates;

            // Parse the 'amountToPay' string into the 'actualraters' double. and if amount>0 setEcommerce Event
            try {
                if (amountToPay != null && !amountToPay.isEmpty()) {
                    actualrates = Double.parseDouble(amountToPay);
                } else {
                    actualrates = 0;
                }
            } catch (NumberFormatException e) {
                actualrates = 0; // Fallback if parsing fails
                e.printStackTrace();
            }
            if (actualrates > 0) {
                if(servicelistModal.isFreeTrialAvailable()) {
                    com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, ecommerceLabel, "PurchasePlanHomeActivity");
                } else {
                    CUtils.setEcommercePurchaseEvent(this, orderId, ecommerceLabel, actualrates);
                }
            }

            Intent tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreen.class);
            tppsIntent.putExtra(FREE_QUESTIONS_RECEIVED_KEY, "");
            tppsIntent.putExtra("SIGNATURE", signature);
            tppsIntent.putExtra("PURCHASE_DATA", "");
            tppsIntent.putExtra("PLAN", plan);
            tppsIntent.putExtra("price", "");
            tppsIntent.putExtra("priceCurrencycode", currency);
            tppsIntent.putExtra("freetrialperiod", "");
            tppsIntent.putExtra("isSuccesFrom", isSucessFrom);
            startActivity(tppsIntent);
            this.finish();

        }catch (Exception e){
            //
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY_FAILURE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        // Show a dialog to inform the user about the payment failure
      //  if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1) {
            paymentFailedFrom = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE;
       // }
        showRazorpayFailureDialog();
        //Toast.makeText(this, "Subscription onPaymentError !"+ i + s + paymentData.getExternalWallet(), Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a dialog to inform the user about the payment failure.
     * The dialog is different based on the payment mode. If the payment was attempted through Razorpay,
     * the dialog suggests trying Google Payment. If the payment was attempted through Google Payment, the dialog
     * suggests trying Razorpay.
     */
    private void showRazorpayFailureDialog() {
        CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        SubscriptionPaymentFailDialog dialog = new SubscriptionPaymentFailDialog(paymentFailedFrom);
        dialog.setOnDialogActionListener(this::doSomethingAfterDialog);
        dialog.show(fragmentManager, "payment_fail_dialog");
    }

    /*
    This method is called after the payment failure dialog is dismissed.
    It takes the payment mode as a parameter and takes the user to the corresponding payment flow.
    If the payment was attempted through Razorpay, it takes the user to the Google Payment flow.
    If the payment was attempted through Google Payment, it takes the user to the Razorpay flow.
     */
    private void doSomethingAfterDialog() {
        isRetryPayment = true;
        if (paymentFailedFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
            googlePlanBuy();
        } else if (paymentFailedFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE)) {
            getOrderIdNew();

        }
        paymentFailedFrom = "";
    }

    @Override
    public void onError(VolleyError error) {
        //
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
        }

    }

    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(PurchasePlanHomeActivity.this);
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
        }

    }

    public class ModulePagerAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<Fragment> mFragments;

        public ModulePagerAdapter(FragmentManager fm, boolean isButtonEnable) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mFragments = new ArrayList<Fragment>();


            basicplan = new BasicPlanFrag();
            silverplan = new SilverPlanFrag(isButtonEnable);
            goldplan = new GoldPlanFrag(isButtonEnable);
            platinumplan = new PlatinumPlanFrag(isButtonEnable, isFreeDhruvPlanShow);
            kundliAiPlusPlanFrag = new KundliAiPlusPlanFrag(isButtonEnable);
            mFragments.add(basicplan);
            if (CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                mFragments.add(silverplan);
                mFragments.add(goldplan);
                mFragments.add(kundliAiPlusPlanFrag);
                mFragments.add(platinumplan);
            } else if (!CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                mFragments.add(kundliAiPlusPlanFrag);
                mFragments.add(platinumplan);
            } else if (CGlobalVariables.isShowSilverPlanTab && !CGlobalVariables.isShowGoldPlanTab) {
                mFragments.add(silverplan);
                mFragments.add(kundliAiPlusPlanFrag);
                mFragments.add(platinumplan);
            } else if (!CGlobalVariables.isShowSilverPlanTab && CGlobalVariables.isShowGoldPlanTab) {
                mFragments.add(goldplan);
                mFragments.add(kundliAiPlusPlanFrag);
                mFragments.add(platinumplan);
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
            return getPageTitles()[position];
        }

        private void updatePagerAdapter(boolean isButtonEnable) {

        }

    }
}