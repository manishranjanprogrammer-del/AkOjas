package com.ojassoft.astrosage.ui.act;

import static android.view.View.GONE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_QUESTIONS_RECEIVED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_10;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN_MONTHLY;
import static com.ojassoft.astrosage.varta.utils.CUtils.SavePlaninPreference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsConstants;
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
import com.ojassoft.astrosage.ui.fragments.FreeTrialFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.adapters.UPIAppAdapter;
import com.ojassoft.astrosage.varta.dialog.SubscriptionPaymentFailDialog;
import com.ojassoft.astrosage.varta.model.UPIAppModel;
import com.ojassoft.astrosage.varta.service.Loginservice;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Activity for handling the purchase flow of the Dhruv Plan (AI Plus Plan).
 * Manages UI, user interactions, network requests, and payment integration (Razorpay).
 * 
 * Key Features:
 * - Tab-based UI for Plus and Premium plans
 * - Dynamic plan details loading
 * - Razorpay payment integration
 * - Purchase verification and analytics
 */
public class PurchaseDhruvPlanActivity extends BaseInputActivity implements
        IPurchasePlan, IPersonalDetails, BillingEventHandler, PaymentResultWithDataListener, VolleyResponse {


    // Constants for product IDs and request codes
    public static final String SKU_PLATINUM_PLAN_MONTH = "platinum_plan_month";
    public static final String SKU_PLATINUM_PLAN_YEAR = "platinum_plan_year";
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_YEAR = 20011;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_MONTH = 20012;
    public static int BASIC_PLAN = 0;

    public static int GET_SERVICE_DETAILS = 3001;
    ServicelistModal servicelistModal;
    public static int GET_ORDER_ID = 4001;
    public static int GET_SUBSCRIPTION_ID = 5001;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    public String platinumPlanPriceYear = "", platinumPlanPriceMonth = "";
    int requestCode;
    int PRICE = 2, price_amount_micros = 5, price_currency_code = 6, isFreetimeperiod = 7;
    ArrayList<String> arayPlatinumPlanYear = new ArrayList<String>(5);
    ArrayList<String> arayPlatinumPlanMonth = new ArrayList<String>(5);
    ProductDetails skuDetailsPlatinumPlanYear;
    ProductDetails skuDetailsPlatinumPlanMonth;
    String purchaseData = "", signature = "";
    String screenId = "";
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
    String source;
    RelativeLayout planDhruvRootView;
    CustomProgressDialog pd;
    RequestQueue queue;
    String COUNTRY_CODE_IND = "91";
    String currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
    String orderId = "";
    String amountToPay = "0";
    String PriceInDollor, PriceInRS, subscriptionId;
    // UI components
    private ImageView ivClosePage, imgHeading;
    private RelativeLayout subscribeForBtnDiable, subscribeForBtn;
    private TextView tvHeading, txtHeading1, subHeading2, subHeading3, subHeading4, subHeading5,
            tvBtnText, tv_btn_txt_diable, unlimited_chat_text_5x, unlimited_chat_text_500,
            dhruvQuesCountTV, kundliAIQuestionCount, KAPlusQuesCountTV, freeTrialAmountTV, renewalDateTV , subsBeginTV;
    ;
    private ConstraintLayout errorHeadingLL;
    public boolean isOpenForAIChat, isFreeLimitExceed, isLoginPerformed, isOpenAsAd;

    private String fcmLabel = "";
    private TextView txtAlreadyPurchasePlan;
    private String paymentFailedFrom = "";
    //    private VideoView videoView;
    private LinearLayout plusTabButton, premiumTabButton;
    private LinearLayout plusLayoutView, premiumLayoutView;
    private ConstraintLayout phonePePaymentLayout;
    private Button btnStartSubsPayment;
    private TextView plusTabText, premiumTabText;

    private static final int TAB_PLUS = 0;
    private static final int TAB_DHRUV = 1;
    int selectedTab = TAB_PLUS;

    ConstraintLayout freeTrialLayout, planContentLayout;
    TextView freeTrialTv, reminderDayTv;
    ProgressBar adProgressBar;
    FrameLayout adTimerLayout;
    TextView adTimerTV;
    boolean isAddFreeBottomShown = false;
    public static boolean iSAlreadyShownKundliAIPlusPopUp = false;
    private TextView tvSelectedAppName;
    private ImageView ivSelectedAppIcon;
    private Spinner spinnerUpiApps;
    private boolean userSelect = false;
    UPIAppModel selectedUPIAppModel;
    private List<UPIAppModel> masterList;
    private String paymentModeStr = "";
    private final int PHONE_PAY_GATEWAY_CALLBACK= 10001;
    boolean getPhonePeOrderIdReq;
    String merchantOrderId="",IsFreeTrialAvailable ="";
    private boolean isPhonePeSubscriptionEnabled;
    ImageView fullScreenLoader;
    public PurchaseDhruvPlanActivity() {
        super(R.id.app_name);
    }

    public static String replaceEmailChar(String emailId) {

        try {
            int xCharacterAhead = 1;
            emailId = encodeWithAsciiValue(emailId, xCharacterAhead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailId;
    }

    static String encodeWithAsciiValue(String stringToEncode, int XAheadCharacter) {
        StringBuilder newString = new StringBuilder();
        for (int iterator = 0; iterator < stringToEncode.length(); ++iterator) {
            newString.append((char) (stringToEncode.charAt(iterator) + XAheadCharacter));
        }
        return newString.toString();
    }

    String domesticPayMode, internationalPayMode, countryCode;
    boolean preFetchForFreeTrial;
    ConstraintLayout topMainLayout;
    LinearLayout bottomMainLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testSubFail", "Called onCreate");
        setContentView(R.layout.activity_kundli_plan_new_layout);
        setBillingEventHandler(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) PurchaseDhruvPlanActivity.this.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(PurchaseDhruvPlanActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        TextView premiumKundliMonthlyTV = findViewById(R.id.font_auto_activity_kundli_plan_new_layout_6);
        TextView cloudSaveTV = findViewById(R.id.font_auto_activity_kundli_plan_new_layout_7);
        TextView aiModelTV = findViewById(R.id.font_auto_activity_kundli_plan_new_layout_11);
        TextView premiumKundliWorthTV = findViewById(R.id.font_auto_activity_kundli_plan_new_layout_14);
        TextView brandingTV = findViewById(R.id.font_auto_activity_kundli_plan_new_layout_15);
        TextView discountTV = findViewById(R.id.font_auto_activity_kundli_plan_new_layout_16);
        FontUtils.changeFont(this, premiumKundliMonthlyTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, cloudSaveTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, aiModelTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, premiumKundliWorthTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, brandingTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, discountTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        fullScreenLoader = findViewById(R.id.full_screen_loader);
        topMainLayout = findViewById(R.id.main_layout_top);
        bottomMainLayout = findViewById(R.id.bottom_layout);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        source = getIntent().getStringExtra("source");



        String cachedDetails = com.ojassoft.astrosage.varta.utils.CUtils.getPlusPlanServiceDetail();
        if(!TextUtils.isEmpty(cachedDetails)){
                decideForOpeningActivity(cachedDetails);
        }else{
            fullScreenLoader.setVisibility(View.VISIBLE);
            Glide.with(fullScreenLoader)
                    .load(getResources().getDrawable(R.drawable.new_ai_loader))
                    .into(fullScreenLoader);
            preFetchForFreeTrial = true;
            loadDataFromServer(false);
        }


        // initializePhonePe(this);
        isPhonePeSubscriptionEnabled = CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, true);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            isPhonePeSubscriptionEnabled = false;
        }
        inti();
        initListner();
        setupTabs();
//        initVideoPlayer();

        internationalPayMode = CUtils.getStringData(PurchaseDhruvPlanActivity.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
        domesticPayMode = CUtils.getStringData(PurchaseDhruvPlanActivity.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
        countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(PurchaseDhruvPlanActivity.this);
//        setupSelectedPlanDetails();
//        getProductListPref();
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

    private void decideForOpeningActivity(String cachedDetails) {
        try {
            JSONObject obj = new JSONObject(cachedDetails);
            boolean isFreeTrialAvailable = obj.getBoolean("IsFreeTrialAvailable");
            if (isFreeTrialAvailable) {
                finish();
                Intent intent = new Intent(PurchaseDhruvPlanActivity.this, PurchaseFreeTrialActivity.class);
//            Log.e("source_check", "decideForOpeningActivity: source -  "+source );
                intent.putExtra("source", source);
                startActivity(intent);
            } else {
                topMainLayout.setVisibility(View.VISIBLE);
                bottomMainLayout.setVisibility(View.VISIBLE);
                fullScreenLoader.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //
        }
    }

//    private void initVideoPlayer() {
//        Uri uri = Uri.parse("https://www.astrosage.com/images/ads/kundli-ai-help.mp4");
//        videoView.setVideoURI(uri);
//
//        videoView.start();
//
//    }

    boolean isCountdownActive = false;

    private void inti() {
        isOpenForAIChat = getIntent().getBooleanExtra(CGlobalVariables.OPEN_FOR_AI_CHAT, false);
        isFreeLimitExceed = getIntent().getBooleanExtra(CGlobalVariables.FREE_LIMT_EXCEED, false);
        isOpenAsAd = getIntent().getBooleanExtra(CGlobalVariables.IS_OPEN_AS_AD, false);
        isAddFreeBottomShown = getIntent().getBooleanExtra(CGlobalVariables.IS_AD_FREE_BOTTOM, false);
//          Log.e("source_check", "inti: PurchaseDhruvPlanActivity -> "+source );
        ivClosePage = findViewById(R.id.ivClosePage);
        subscribeForBtn = findViewById(R.id.subscribeForBtn);
        btnStartSubsPayment = findViewById(R.id.btnStartSubsPayment);
        phonePePaymentLayout = findViewById(R.id.phonePePaymentLayout);

        subscribeForBtnDiable = findViewById(R.id.subscribeForBtnDiable);

        planDhruvRootView = findViewById(R.id.planDhruvRootView);
        txtAlreadyPurchasePlan = findViewById(R.id.txt_already_purchase_plan);
        imgHeading = findViewById(R.id.platinumImageview);
        tvHeading = findViewById(R.id.tv_heading);
        freeTrialLayout = findViewById(R.id.free_trial_layout);
        planContentLayout = findViewById(R.id.plan_content_layout);
        reminderDayTv = findViewById(R.id.reminder_day_tv);
        freeTrialTv = findViewById(R.id.free_trial_tv);
        LinearLayout linLayoutAdFreeExperience = findViewById(R.id.linLayoutAdFreeExperience);
        LinearLayout linLayoutAdFreeExperienceDhruv = findViewById(R.id.linLayoutAdFreeExperienceDhruv);
        LinearLayout linLayoutAdFreeExperiencebottom = findViewById(R.id.linLayoutAdFreeExperiencebottom);
        LinearLayout linLayoutAdFreeExperienceDhruvbottom = findViewById(R.id.linLayoutAdFreeExperienceDhruvbottom);


        adTimerLayout = findViewById(R.id.adTimerLayout);
        adProgressBar = findViewById(R.id.ADprogressBar);
        adTimerTV = findViewById(R.id.ADtvCountdown);

        if (isAddFreeBottomShown) {
            linLayoutAdFreeExperience.setVisibility(GONE);
            linLayoutAdFreeExperienceDhruv.setVisibility(GONE);
            linLayoutAdFreeExperiencebottom.setVisibility(View.VISIBLE);
            linLayoutAdFreeExperienceDhruvbottom.setVisibility(View.VISIBLE);
        } else {
            linLayoutAdFreeExperience.setVisibility(View.VISIBLE);
            linLayoutAdFreeExperienceDhruv.setVisibility(View.VISIBLE);
            linLayoutAdFreeExperiencebottom.setVisibility(GONE);
            linLayoutAdFreeExperienceDhruvbottom.setVisibility(GONE);
        }

        if(isOpenAsAd){
           // linLayoutAdFreeExperience.setVisibility(View.VISIBLE);
           // linLayoutAdFreeExperienceDhruv.setVisibility(View.VISIBLE);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_SCREEN_OPEN_FOR_AD, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            adTimerLayout.setVisibility(View.VISIBLE);
            ivClosePage.setVisibility(View.GONE);
            isCountdownActive = true;
            CountDownTimer timer = new CountDownTimer(5000, 1000) {
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000) + 1;
                    adTimerTV.setText(String.valueOf(seconds));
                    adProgressBar.setProgress((int) millisUntilFinished);
                }

                public void onFinish() {
                    adTimerTV.setText("0");
                    isCountdownActive = false;
                    adTimerLayout.setVisibility(View.GONE);
                    ivClosePage.setVisibility(View.VISIBLE);
                }
            };
            timer.start();
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    // Do nothing if the countdown is active, otherwise perform default back press
                    if (!isCountdownActive) {
                        // Re-enables the callback and calls onBackPressed()
                        setResult(Activity.RESULT_OK, new Intent());
                        finish();
                    }
                }
            });

        }


        // Set ImageView size and tint dynamically
        if (imgHeading != null) {
            // Set size
            int width = (int) getResources().getDimension(R.dimen.platinum_image_width);
            int height = (int) getResources().getDimension(R.dimen.platinum_image_height);
            ViewGroup.LayoutParams params = imgHeading.getLayoutParams();
            params.width = width;
            params.height = height;
            imgHeading.setLayoutParams(params);

            // Set tint color
            int tintColor = getResources().getColor(R.color.colorPrimary_day_night);
            imgHeading.setColorFilter(tintColor, android.graphics.PorterDuff.Mode.SRC_IN);

            // Set scale type
            imgHeading.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgHeading.setAdjustViewBounds(true);
        }

        plusTabButton = findViewById(R.id.plus_tab_button);
        premiumTabButton = findViewById(R.id.premium_tab_button);
        plusLayoutView = findViewById(R.id.plus_layout_view);
        premiumLayoutView = findViewById(R.id.premium_layout_view);
        plusTabText = findViewById(R.id.plus_tab_text);
        premiumTabText = findViewById(R.id.premium_tab_text);

//        txtHeading1 = findViewById(R.id.txtHeading1);
//        subHeading2 = findViewById(R.id.subHeading2);
//        subHeading3 = findViewById(R.id.subHeading3);
//        subHeading4 = findViewById(R.id.subHeading4);
//        subHeading5 = findViewById(R.id.subHeading5);
        tvBtnText = findViewById(R.id.tv_btn_txt);
        tv_btn_txt_diable = findViewById(R.id.tv_btn_txt_diable);
        errorHeadingLL = findViewById(R.id.errorHeadingLL);
//        videoView = findViewById(R.id.video_view);
        if (isFreeLimitExceed) {
            errorHeadingLL.setVisibility(View.VISIBLE);
//             linLayoutAdFreeExperience.setVisibility(GONE);
//             linLayoutAdFreeExperienceDhruv.setVisibility(GONE);
//             linLayoutAdFreeExperiencebottom.setVisibility(View.VISIBLE);
//             linLayoutAdFreeExperienceDhruvbottom.setVisibility(View.VISIBLE);
        } else {
            errorHeadingLL.setVisibility(View.GONE);
//             linLayoutAdFreeExperience.setVisibility(View.VISIBLE);
//             linLayoutAdFreeExperienceDhruv.setVisibility(View.VISIBLE);
//            linLayoutAdFreeExperiencebottom.setVisibility(GONE);
//            linLayoutAdFreeExperienceDhruvbottom.setVisibility(GONE);
        }
        if (LANGUAGE_CODE != 0) {
            imgHeading.setVisibility(View.GONE);
            tvHeading.setVisibility(View.VISIBLE);
        } else {
            imgHeading.setVisibility(View.VISIBLE);
            tvHeading.setVisibility(View.GONE);
        }
        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, plusTabText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_LIGHT);
        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, premiumTabText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_LIGHT);
//        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, subHeading2, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, subHeading3, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, subHeading4, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, subHeading5, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, tvBtnText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(PurchaseDhruvPlanActivity.this, tv_btn_txt_diable, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        //dhruvQuesCountTV = findViewById(R.id.unlimited_chat_text);
        unlimited_chat_text_5x = findViewById(R.id.unlimited_chat_text_5x);
        unlimited_chat_text_500 = findViewById(R.id.unlimited_chat_text_500);
        KAPlusQuesCountTV = findViewById(R.id.kundliAIQuestCount);
        kundliAIQuestionCount = findViewById(R.id.kundliAIQuestionCount);
        freeTrialAmountTV = findViewById(R.id.free_trial_amount_tv);
        renewalDateTV = findViewById(R.id.renewalDateTV);
        subsBeginTV = findViewById(R.id.auto_pay_time_tv);
        //tvHeading.setText(CUtils.getKundliAIPlusSpannableText(this,getString(R.string.platinum_plan)));
        initPhonePeGateway();
    }
    /**
     * Initializes the PhonePe payment gateway
     * This method sets up the UI components for PhonePe payment method
     */
    private void initPhonePeGateway() {
        LinearLayout llSelectedAppName = findViewById(R.id.llSelectedAppName);
        spinnerUpiApps = findViewById(R.id.spinnerApps);
        ivSelectedAppIcon = findViewById(R.id.ivSelectedAppIcon);
        tvSelectedAppName = findViewById(R.id.tvSelectedAppName);

        masterList = Arrays.asList(
                new UPIAppModel(getResources().getString(R.string.phonepe), R.drawable.ic_phonpe_icon, UPIAppChecker.PACKAGE_PHONEPE,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PHONE),
                new UPIAppModel(getResources().getString(R.string.gpay), R.drawable.ic_gpay_icon, UPIAppChecker.PACKAGE_GOOGLE_PAY,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_GPAY),
                new UPIAppModel(getResources().getString(R.string.paytm), R.drawable.ic_paytm_icon, UPIAppChecker.PACKAGE_PAYTM,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PAYTM),
                new UPIAppModel(getResources().getString(R.string.bhim), R.drawable.ic_bhim_icon, UPIAppChecker.PACKAGE_BHIM,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_BHIM),
                //new UPIAppModel(getResources().getString(R.string.amazon_pay), R.drawable.ic_amazon_icon, UPIAppChecker.PACKAGE_AMAZON,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_AMAZON),
                new UPIAppModel(getResources().getString(R.string.cred), R.drawable.ic_cred_icon, UPIAppChecker.PACKAGE_CRED,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_CRED));

        // 2) Filter based on installed packages on device
        List<UPIAppModel> installedApps = UPIAppChecker.getInstalledAndReadyUpiApps(this,masterList);
        // 3) Create adapter and set to spinner
        installedApps.add(new UPIAppModel(getResources().getString(R.string.razorpay), R.drawable.ic_razorpay_icon, "com.razorpay",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY));
        UPIAppAdapter adapter = new UPIAppAdapter(
                this,
                R.layout.row_upi_app,
                R.layout.row_upi_app,
                installedApps
        );
        spinnerUpiApps.setAdapter(adapter);
        // 4 Listen for selection
        spinnerUpiApps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true; // if you want to ignore initial automatic selection callback
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if (first) { first = false; return; } // optional
                selectedUPIAppModel = (UPIAppModel) parent.getItemAtPosition(position);
                onUpiAppSelected(selectedUPIAppModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no-op
            }
        });

        llSelectedAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Optionally set flag so performClick counts as user action
                userSelect = true;
                spinnerUpiApps.performClick();
//                Intent intent = new Intent(PurchaseAiConsultationPlan.this, SubscriptionPaymentActivity.class);
//                intent.putExtra("service_model", servicelistModal);
//                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_PARAMS, source);
//                startActivity(intent);
            }
        });
    }

    private void onUpiAppSelected(UPIAppModel app) {
        // Called when user selects an app. Do whatever you need (save selection, start payment intent, etc.)
        //Toast.makeText(getActivity(), "Selected: " + app.getName() + " (" + app.getPackageName() + ")", Toast.LENGTH_SHORT).show();
        tvSelectedAppName.setText(app.getName());
        ivSelectedAppIcon.setImageResource(app.getIconResId());

    }

    /**
     * Sets up the tab layout for Plus and Premium plans.
     * Handles tab switching, UI state updates, and plan details loading.
     * <p>
     * Flow:
     * 1. Set up click listeners for both tabs
     * 2. Handle tab selection state
     * 3. Update UI visibility
     * 4. Load appropriate plan details
     * 5. Update text colors based on selection
     */
    private void setupTabs() {
        plusTabButton.setOnClickListener(v -> {
            plusTabButton.setBackgroundResource(R.drawable.tab_selected_background);
            premiumTabButton.setBackgroundResource(R.drawable.tab_unselected_background);
            plusLayoutView.setVisibility(View.VISIBLE);
            premiumLayoutView.setVisibility(View.GONE);
            plusTabText.setSelected(true);
            premiumTabText.setSelected(false);
            selectedTab = TAB_PLUS;
            setupSelectedPlanDetails();
        });

        premiumTabButton.setOnClickListener(v -> {
            premiumTabButton.setBackgroundResource(R.drawable.tab_selected_background);
            plusTabButton.setBackgroundResource(R.drawable.tab_unselected_background);
            premiumLayoutView.setVisibility(View.VISIBLE);
            plusLayoutView.setVisibility(View.GONE);
            premiumTabText.setSelected(true);
            plusTabText.setSelected(false);
            selectedTab = TAB_DHRUV;
            setupSelectedPlanDetails();

        });

        // Set Plus tab as default
        plusTabButton.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        iSAlreadyShownKundliAIPlusPopUp = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Log.d("testSubFail", "onResume called PurchaseDhruvPlanActivity");
        iSAlreadyShownKundliAIPlusPopUp = true;
        if (isLoginPerformed) {
            isLoginPerformed = false;
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(PurchaseDhruvPlanActivity.this);
            if (isLogin) {
                int purchasedPlanId = CUtils.getUserPurchasedPlanFromPreference(this);
                if (selectedTab == TAB_PLUS) {
                    if (CUtils.isKundliAIPlusPlan(this) || CUtils.isDhruvPlan(this)) {
                        finish();
                    } else {
                        subscribeForBtn.performClick();
                    }
                } else if (selectedTab == TAB_DHRUV) {
                    if (CUtils.isDhruvPlan(this)) {
                        finish();
                    } else {
                        subscribeForBtn.performClick();
                    }
                }
            }
        }

    }

    boolean isShownFreeTrialInfo = false;

    private void initListner() {

        ivClosePage.setOnClickListener(view -> {
            iSAlreadyShownKundliAIPlusPopUp = false;

            setResult(Activity.RESULT_OK, new Intent());
            finish();
        });
        btnStartSubsPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeForBtn.performClick();
            }
        });
        // When the Subscribe button is tapped, verify login and launch the appropriate purchase flow:
        // 1. If user is logged in, read domestic/international pay mode setting and start Razorpay order or Google subscription.
        // 2. If not logged in, redirect to the login screen.
        subscribeForBtn.setOnClickListener(view -> {
            if(servicelistModal==null){
                return;
            }
            if (servicelistModal.isFreeTrialAvailable() && !isShownFreeTrialInfo) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FREE_TRAIL_INFO_SCREEN_SHOWN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                freeTrialLayout.setVisibility(View.VISIBLE);
                planContentLayout.setVisibility(View.GONE);
                freeTrialTv.setText(getFormattedFreeTrialText(this, servicelistModal.getFreeTrialPeriod()));
                tvBtnText.setText(getResources().getString(R.string.start_my_free_trial_btn_text));

                isShownFreeTrialInfo = true;
                if(isPhonePeSubscriptionEnabled){
                    subscribeForBtn.setVisibility(GONE);
                    phonePePaymentLayout.setVisibility(View.VISIBLE);
                    btnStartSubsPayment.setText(getResources().getString(R.string.start_my_trial_btn_text));
                }
                return;
            }
            if (servicelistModal.isFreeTrialAvailable() && isShownFreeTrialInfo) {
                if(CUtils.is3MonthSubsEnabled(this)){
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_AI_FREE_TRIAL_FOR_3_MONTH_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }else{
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_AI_FREE_TRIAL_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }

            } else {
                //setGetSubscriptionId();
                //selectPlan(PLATINUM_PLAN_MONTHLY);
                if (selectedTab == TAB_PLUS) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_FROM_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                } else if (selectedTab == TAB_DHRUV) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_FROM_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }

            }
            if (CUtils.isUserLogedIn(PurchaseDhruvPlanActivity.this)) {//check astrosage login
                if (isPhonePeSubscriptionEnabled && selectedUPIAppModel != null) {
                    if (selectedUPIAppModel.getPayMethodName().equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
                        getOrderIdNew();
                    } else {
                        paymentModeStr = selectedUPIAppModel.getPayMethodName();
                        getApiCallForPhonePayIntentUrl(selectedUPIAppModel.getPayMethodName());
                    }
                } else {
                    getOrderIdNew();
                }
//                getOrderIdNew();
//                    String internationalPayMode = CUtils.getStringData(PurchaseDhruvPlanActivity.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
//                    String domesticPayMode = CUtils.getStringData(PurchaseDhruvPlanActivity.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
//                    String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(PurchaseDhruvPlanActivity.this);

//                    if(selectedTab==TAB_PLUS) {
//                        if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                            // Check if domestic payment mode is Razorpay or Google
//                            if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                                fcmLabel = "indian";
//                                getOrderIdNew();
//                            } else {
//                                selectPlan(PLATINUM_PLAN_MONTHLY);
//                            }
//                        } else {
//                            // Check if international payment mode is Razorpay or Google
//                            if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                                fcmLabel = "international";
//                                getOrderIdNew();
//                            } else {
//                                selectPlan(PLATINUM_PLAN_MONTHLY);
//                            }
//                        }
//                    }else{
//                        getOrderIdNew();
//                    }
            } else {
                isLoginPerformed = true;
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_NOT_LOGGED_IN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent1 = new Intent(PurchaseDhruvPlanActivity.this, FlashLoginActivity.class);
                startActivity(intent1);
            }


        });
    }

    public static Spannable getFormattedFreeTrialText(Context context, int freeTrialPeriod) {
        String freeTrialText = context.getString(R.string.free_trial_7day, String.valueOf(freeTrialPeriod));
        try {
            // Create Spannable
            SpannableString spannable = new SpannableString(freeTrialText);
            String freeText = context.getString(R.string.text_free);
            // Find "FREE" position
            int start = freeTrialText.indexOf(freeText);
            int end = start + freeText.length();
            // Apply color span (use theme primary color)
            int primaryColor = ContextCompat.getColor(context, R.color.colorPrimary_day_night);
            spannable.setSpan(
                    new ForegroundColorSpan(primaryColor),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            return spannable;
        } catch (Exception e) {
            return new SpannableString(freeTrialText);
        }
    }

    /*
     * Add This method to parce and get data back
     * from sharedPrefrences
     */
    public void getProductListPref() {

        ArrayList<ProductDetails> skuDetailsList = CUtils.getDhruvPlanList();
        if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
            Log.e("BillingClient", "onProductDetailsResponse() 1");
            for (ProductDetails skuDetails : skuDetailsList) {
                intiProductPlan(skuDetails);
            }
            showProductsDetail_new();
        } else {
            Log.e("BillingClient", "onProductDetailsResponse() 2");
            fetchProductFromGoogleServer();
        }

    }

    private void fetchProductFromGoogleServer() {
        try {

            QueryProductDetailsParams queryProductDetailsParams =
                    QueryProductDetailsParams.newBuilder().setProductList(
                            ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_PLATINUM_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(SKU_PLATINUM_PLAN_MONTH)
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
                            CUtils.setDhruvPlanList((ArrayList<ProductDetails>) productDetailsList);
                            //  Log.d("testSubFail", "Called showProduct1232423525");
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
        }
    }

    private void showMsg(final int response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response >= 0 && response < 9) {
                        Toast.makeText(PurchaseDhruvPlanActivity.this, errorResponse[response], Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PurchaseDhruvPlanActivity.this, getResources().getString(R.string.internet_is_not_working), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });

    }

    private void intiProductPlan(ProductDetails skuDetails) {
        try {
            //Log.e("CloudPlanTest", "skuDetails="+skuDetails.getProductId()+" "+skuDetails.getTitle());
            ProductDetails.PricingPhase priceDetails = skuDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0);

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


        } catch (Exception e) {
            Log.e("BillingClient", "intiProductPlan() exp=" + e);
        }

    }

    private void showProductsDetail_new() {
//        Log.d("testSubFail", "Called showProduct");
        String rupeesInTamil = "ரூ.";
        String planPrice = "";

        // INIT Dhruv PLAN Month
        if (!arayPlatinumPlanMonth.isEmpty()) {

            planPrice = arayPlatinumPlanMonth.get(PRICE).replace(".00", "");
            if (LANGUAGE_CODE == 1) {
                platinumPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", "\u20B9"));

            } else if (LANGUAGE_CODE == 2) {

                platinumPlanPriceMonth = PriceCalculator(planPrice.replace("Rs.", rupeesInTamil));
            } else {
                platinumPlanPriceMonth = PriceCalculator(planPrice);
            }

            if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE)) {
                    setPriceOnBtnAccordingly();
                }
            } else {
                if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE)) {
                    setPriceOnBtnAccordingly();
                }
            }

        }

    }

    private void setPriceOnBtnAccordingly(){
        if (selectedTab == TAB_PLUS) {
            if (CUtils.is3MonthSubsEnabled(this)) {
                tvBtnText.setText(getResources().getString(R.string.subscribe_for_3_text, platinumPlanPriceMonth));
                tv_btn_txt_diable.setText(getResources().getString(R.string.subscribe_for_3_text, platinumPlanPriceMonth));
            } else {
                tvBtnText.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
                tv_btn_txt_diable.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
            }
        } else {
            tvBtnText.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
            tv_btn_txt_diable.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
        }
    }

    public String PriceCalculator(String strvalue) {
        return strvalue;
    }

    @Override
    public void onYesClick(int planIndex) {
        selectPlan(planIndex);
    }

    /**
     * This method is used to select the plan
     *
     * @param planIndex
     */
    private void selectPlan(int planIndex) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_GOOGLE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        dumpDataString = "ActNotificationLanding selectPlan() fullJsonDataObj=";
        if (planIndex == PLATINUM_PLAN)
            gotBuyPlatinumPlan();
        if (planIndex == PLATINUM_PLAN_MONTHLY)
            gotBuyPlatinumMonthlyPlan();

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
                    Toast.makeText(PurchaseDhruvPlanActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
        CUtils.postDumpDataToServer(PurchaseDhruvPlanActivity.this, dumpDataString);
    }

    public void gotBuyPlatinumMonthlyPlan() {
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
                Log.d("BillingClient", "gotBuyPlatinumMonthlyPlan() responseCode=" + responseCode);
                dumpDataString = dumpDataString + " gotBuyPlatinumMonthlyPlan() prepayment responseCode=" + responseCode;
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(PurchaseDhruvPlanActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            //Log.e("BillingClient", e.toString());
        }
        CUtils.postDumpDataToServer(PurchaseDhruvPlanActivity.this, dumpDataString);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("verifyPurchase", "requestCode=" + requestCode + " resultCode=" + resultCode);
        /*
         * Toast.makeText( DialogPlanUpgradeNew.this,"i m here",
         * Toast.LENGTH_LONG).show();
         */
        if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR || requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH) {
            // do nothing now
        } else if (requestCode == PHONE_PAY_GATEWAY_CALLBACK) {
            try {
                if (data != null) {
                    Log.d("UPI", "UPI App result" + data.getData());
                    //call order status api
                    getOrderStatusApi();
                }
            } catch (Exception e) {
                Log.d("UPI", "UPI App result" + e.toString());
            }
        }else {
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

            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH) {
                SavePlaninPreference(this, PLATINUM_PLAN_MONTHLY);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;

                if (arayPlatinumPlanMonth != null && !arayPlatinumPlanMonth.isEmpty()) {
                    price = arayPlatinumPlanMonth.get(price_amount_micros);// 2
                    priceCurrencycode = arayPlatinumPlanMonth.get(price_currency_code);
                    formattedPrice = arayPlatinumPlanMonth.get(PRICE);
                    if (arayPlatinumPlanMonth.size() > 7) {
                        freeTrialPeriodTxt = arayPlatinumPlanMonth.get(isFreetimeperiod);
                    }
                }

                double dPrice = 0.0;
                try {
                    if (price != null && !price.isEmpty()) {
                        dPrice = Double.valueOf(price);
                        dPrice = (dPrice / CGlobalVariables.PRICE_IN_ONE_UNIT);
                    }
                } catch (NumberFormatException e) {
                    //e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_CLOUD,
                        CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_SUCCESS, null, dPrice, purchaseSource);
            }
            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR) {
                SavePlaninPreference(this, PLATINUM_PLAN);
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
            //Do not show update purchase plan in ActAppModule
            CUtils.saveBooleanData(PurchaseDhruvPlanActivity.this, CGlobalVariables.doNotShowMessageAgainForUpdatePlan, true);
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
        }
        CUtils.postDumpDataToServer(PurchaseDhruvPlanActivity.this, dumpDataString);
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
        if (CUtils.isConnectedWithInternet(PurchaseDhruvPlanActivity.this)) {
            verifyPurchaseFromService(price, priceCurrencycode, formattedPrice, freetrialperiodtxt);
        }

        getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).edit().putString("VALUE", purchaseData).commit();// ADDED BY BIJENDRA

        Intent tppsIntent;
        if (CUtils.isUserLogedIn(this)) {
            tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreenNew.class);
        } else {
            tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreen.class);
        }
        tppsIntent.putExtra(FREE_QUESTIONS_RECEIVED_KEY, getQuestionCountForPlan(plan));
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
            pvsIntent.putExtra("openForAIChat", isOpenForAIChat);
            pvsIntent.putExtra("source", source);
            startService(pvsIntent);
        } catch (Exception e) {
            Log.e("BillingClient", "verifyPurchaseFromService() Exception=" + e);
        }
    }


    @Override
    public void selectedPlan(int planIndex, UPIAppModel upiAppModel) {

    }

    /**
     * Fetches and displays details for the currently selected plan (Plus or Dhruv).
     *
     * <p><b>Logic Flow:</b></p>
     * <ol>
     *   <li><b>Update UI based on Purchase Status:</b> First, it checks if the user has already purchased a relevant plan.
     *       If so, it disables the buy button and shows a message.</li>
     *   <li><b>Check for Cached Data:</b> It checks if valid, non-expired (cached for today) plan details are available locally.
     *       If fresh cached data exists, it's parsed and displayed, and the method exits to avoid a network call.</li>
     *   <li><b>Handle Stale Cache:</b> If cached data exists but is from a previous day, it's displayed immediately for a fast UI response,
     *       but the process continues to fetch fresh data in the background.</li>
     *   <li><b>Network Fetch:</b> If no cached data is available or the cache is stale, it proceeds to fetch fresh details
     *       from the network after ensuring there is an internet connection.</li>
     * </ol>
     */
    private void setupSelectedPlanDetails() {
        // Step 1: Update UI based on the user's current subscription status.
        int purchasedPlanId = CUtils.getUserPurchasedPlanFromPreference(this);

        // Disable the buy button if the user already has a superior or equivalent plan.
        if (CUtils.isDhruvPlan(this) || (purchasedPlanId == KUNDLI_AI_PRIMIUM_PLAN_ID_11 && selectedTab == TAB_PLUS)) {
            txtAlreadyPurchasePlan.setVisibility(View.VISIBLE);
            txtAlreadyPurchasePlan.setText(getResources().getString(R.string.already_purchased_plan, getResources().getStringArray(R.array.cloud_plans_on_menu)[purchasedPlanId - 1]));
            subscribeForBtn.setVisibility(View.GONE);
            phonePePaymentLayout.setVisibility(GONE);
            subscribeForBtnDiable.setVisibility(View.VISIBLE);
        } else {
            // Enable the buy button if the user is eligible to purchase.
            txtAlreadyPurchasePlan.setVisibility(View.GONE);
            subscribeForBtn.setVisibility(View.VISIBLE);
            subscribeForBtnDiable.setVisibility(View.GONE);

        }

      loadDataFromLocalOrserver();
    }

    private void loadDataFromLocalOrserver() {
        // Step 2: Check for valid, non-expired cached data.
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todayDate = formatter.format(new Date());
        String serviceListCachingDate = CUtils.getStringData(this, CGlobalVariables.SERVICE_LIST_CACHING_DATE_KEY, "");

        String cachedDetails = selectedTab == TAB_PLUS
                ? com.ojassoft.astrosage.varta.utils.CUtils.getPlusPlanServiceDetail()
                : com.ojassoft.astrosage.varta.utils.CUtils.getPremiumPlanServiceDetail();

        boolean hasCache = !TextUtils.isEmpty(cachedDetails);
        boolean isCacheFresh = hasCache && serviceListCachingDate.equalsIgnoreCase(todayDate);

        // If fresh cache is available, display it and stop.
        if (isCacheFresh) {
            parseServiceDetails(cachedDetails);
            return; // Exit here to prevent unnecessary network call.
        }

        // If stale cache is available, display it for a quick UI update, then proceed to fetch fresh data.
        if (hasCache) {
            parseServiceDetails(cachedDetails);
        }

        loadDataFromServer(hasCache);
    }

    private void loadDataFromServer(boolean isLoadingSilently) {
        // Step 3: Fetch fresh data from the network.
        if (!CUtils.isConnectedWithInternet(this)) {
            // Only show an error if there's no cached data to display.
            if (!isLoadingSilently) {
                Toast.makeText(this,getString(R.string.no_internet),Toast.LENGTH_LONG).show();
                finish();
            }else
                return;
        }

        // Show a progress bar only if no data (even stale) is being displayed.
        if (!isLoadingSilently) {
            showProgressBar();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todayDate = formatter.format(new Date());
        // Update the caching date to today before making the network request.
        CUtils.saveStringData(this, CGlobalVariables.SERVICE_LIST_CACHING_DATE_KEY, todayDate);

        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.SERVICE_LIST_KUNDLI_AI_PLANS;
        StringRequest request = new VolleyServiceHandler(
                Request.Method.POST,
                url,
                this,
                false,
                CUtils.planDetailsRequestParams(this),
                GET_SERVICE_DETAILS
        ).getMyStringRequest();

        request.setShouldCache(false);
        queue.add(request);
    }

//    /**
//     * Requests a subscription ID from the backend for Razorpay subscription flow.
//     * Shows a progress bar and handles no-internet scenario.
//     */
//    private void setGetSubscriptionId() {
//        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(PurchaseDhruvPlanActivity.this)) {
//            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(planDhruvRootView, getResources().getString(R.string.no_internet), PurchaseDhruvPlanActivity.this);
//        } else {
//            if (pd == null)
//                pd = new CustomProgressDialog(PurchaseDhruvPlanActivity.this);
//            pd.show();
//            pd.setCancelable(false);
//            String url = CGlobalVariables.RAZORPAY_SUBSCRIPTION_ID_URL;
//            //String url = "https://bt.astrosage.com/RazorpayCreateSubscriptionServlet";
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    PurchaseDhruvPlanActivity.this, false, CUtils.getParamsForSubscriptionId(PurchaseDhruvPlanActivity.this), GET_SUBSCRIPTION_ID).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
//        }
//    }

    /**
     * Initiates the Razorpay payment flow using the subscription ID.
     * Sets up payment options and opens the Razorpay checkout UI.
     * <p>
     * Flow:
     * 1. Get user contact details
     * 2. Initialize Razorpay checkout
     * 3. Configure payment options
     * 4. Set up payment methods
     * 5. Add metadata for tracking
     * 6. Open checkout UI
     */
    private void startPayment() {
        Activity activity = this;

        String phoneNo = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this) + com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this);
        String astrosageId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);

        String email="";
        if (astrosageId.contains("@")) {
            email = astrosageId;
        }
        final Checkout checkout = new Checkout();
        checkout.setFullScreenDisable(true);

        checkout.setKeyID(getResources().getString(R.string.razorpay_key));
        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage AI");
            options.put("description", "Monthly Subscription");
            options.put("theme.color", "#ff6f00");
            options.put("currency", currency);
            //options.put("amount", "50000"); // pass amount in currency subunits
            options.put("subscription_id", subscriptionId);
            if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(PurchaseDhruvPlanActivity.this).equals(COUNTRY_CODE_IND)){
                options.put("checkout_config_id", "config_RqFj9XRuaSWUgl");
            }
            JSONObject prefill = new JSONObject();
            prefill.put("contact", phoneNo);
            if(!TextUtils.isEmpty(email)){
                prefill.put("email", email);
            }
            options.put("prefill", prefill);

            JSONObject upi = new JSONObject();
            upi.put("flow", "intent"); // Important for UPI Intent
            options.put("upi", upi);

            JSONObject method = new JSONObject();
            method.put("upi", true);
            method.put("card", true);
            method.put("netbanking", true);
            method.put("wallet", true);
            options.put("method", method);

            JSONObject notes = new JSONObject();
            //added for Razorpay Webhook
            notes.put("orderId", orderId);
            notes.put("asuserid", astrosageId);
            notes.put("name", astrosageId);
            notes.put("orderType", "1");
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("source", "ak_sub");
            notes.put("subscriptionId", subscriptionId);
            notes.put("osname", "ANDROID");
            notes.put("isfreetrialavailable", servicelistModal.isFreeTrialAvailable());
            notes.put("freetrialperiod", servicelistModal.getFreeTrialPeriod());
            notes.put("freetrialnotifyperiod", servicelistModal.getFreeTrialNotifyPeriod());
            notes.put("orderFromDomain", "AK_KUNDLI_AI_PLAN_PURCHASE");
            options.put("notes", notes);
            checkout.open(activity, options);
            Log.d("testPyaement", "payment started" + options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error starting payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles network responses for different request methods (order id, service details, subscription id).
     * Parses the response and triggers the next step in the subscription flow.
     * <p>
     * Flow:
     * 1. Hide progress dialog
     * 2. Parse response based on method type
     * 3. Cache service details
     * 4. Update UI with parsed data
     */
    @Override
    public void onResponse(String response, int method) {
        Log.d("testRazorpay", "response  :-  " + response);
        hideProgressBar();
        if (method == GET_SERVICE_DETAILS) {
            try {
                JSONArray array = new JSONArray(response);
                JSONObject obj = array.getJSONObject(0); // 0 for Kundli AI+
                JSONObject premiumService = array.getJSONObject(1); //1 for Dhruv
                com.ojassoft.astrosage.varta.utils.CUtils.setPlusPlanServiceDetail(obj.toString());
                com.ojassoft.astrosage.varta.utils.CUtils.setPremiumPlanServiceDetail(premiumService.toString());
                if(preFetchForFreeTrial){
                    decideForOpeningActivity(obj.toString());
                }else {
                    if (selectedTab == TAB_PLUS)
                        parseServiceDetails(obj.toString());
                    else if (selectedTab == TAB_DHRUV)
                        parseServiceDetails(premiumService.toString());
                }
            } catch (Exception e) {
                Log.e("testRazorpay", "onResponse: exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        // Log.e("testRazorpay", "onResponse: on errror: "+error.getMessage());
        if(preFetchForFreeTrial) {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            finish();
        }
        hideProgressBar();
    }

    /**
     * Parses service details from JSON response.
     * Updates UI with plan information and pricing.
     * <p>
     * Flow:
     * 1. Parse JSON response into service model
     * 2. Set plan details (title, ID, prices)
     * 3. Handle currency conversion based on country
     * 4. Show appropriate payment options
     * 5. Update UI with formatted prices
     */
    private void parseServiceDetails(String JObject) {
        try {
            JSONObject obj = new JSONObject(JObject);
            if (servicelistModal == null)
                servicelistModal = new ServicelistModal();

            servicelistModal.setTitle(obj.getString("Title"));
            servicelistModal.setServiceId(obj.getString("ServiceId"));
            servicelistModal.setPriceInDollor(obj.getString("PriceInDollor"));
            servicelistModal.setPriceInRS(obj.getString("PriceInRS"));
            amountToPay = PriceInRS;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;

            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
                amountToPay = PriceInDollor;
                currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
            }
             servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
            servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
            servicelistModal.setFreeTrialPeriod(obj.getInt("FreeTrialPeriod"));
            servicelistModal.setFreeTrialNotifyPeriod(obj.getInt("FreeTrialNotifyPeriod"));
            servicelistModal.setQuestionLimit(obj.getString("QuestionLimit"));
            servicelistModal.setFreeTrialAmount(obj.getInt("FreeTrialAmount"));
            servicelistModal.setPhonePeFreeTrialAmount(obj.getString("PhonePeFreeTrialAmount"));
//            if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                    showProductsDetailPriceRazorpay();
//                }
//            } else {
//                if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                    showProductsDetailPriceRazorpay();
//                }
//            }
            showProductsDetailPriceRazorpay();

        } catch (Exception e) {
            Log.e("serviceDetailCheck", "parseServiceDetails: exception- " + e);
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
        }
    }

    /**
     * Shows a custom progress dialog if not already showing.
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(PurchaseDhruvPlanActivity.this);
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
        }

    }


    /**
     * Callback for successful Razorpay payment.
     * Logs analytics, updates user preferences, and navigates to the thank-you screen.
     */
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        //Log.d("testRazorpay", "paymentData"+paymentData.toString());
        //Log.d("testRazorpay", "paymentData"+s.toString());
        try {
           // String paymentId = paymentData.getPaymentId();
            String signature = paymentData.getSignature();
            //String orderId = paymentData.getOrderId();
           onSubscriptionPurchaseSuccess("",signature,"",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY);
            //Toast.makeText(this, "Subscription activated successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Toast.makeText(this, "Error processing payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback for Razorpay payment failure.
     * Logs analytics and can show error messages if needed.
     */
    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        // Log.d("testRazorpay", "onPaymentError" + i);
        //Log.d("testRazorpay", "onPaymentError" + s);
        //Log.d("testRazorpay", "onPaymentError" + paymentData.toString());
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY_FAILURE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        // Show a dialog to inform the user about the payment failure
        paymentFailedFrom = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE;
       showRazorpayFailureDialog();
    }

    /**
     * Shows a dialog to inform the user about the payment failure.
     * The dialog is different based on the payment mode. If the payment was attempted through Razorpay,
     * the dialog suggests trying Google Payment. If the payment was attempted through Google Payment, the dialog
     * suggests trying Razorpay.
     */
    private void showRazorpayFailureDialog() {
        try {
            CUtils.saveBooleanData(this, CGlobalVariables.IS_SUBSCRIPTION_ERROR, true);
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            SubscriptionPaymentFailDialog dialog = new SubscriptionPaymentFailDialog(paymentFailedFrom);
            dialog.setOnDialogActionListener(this::doSomethingAfterDialog);
            if (!isFinishing() && !isDestroyed()) {
                dialog.show(fragmentManager, "payment_fail_dialog");
            }
        } catch (Exception e) {
            //
        }


    }

    /**
     * This method is called after the payment failure dialog is dismissed.
     * It takes the payment mode as a parameter and takes the user to the corresponding payment flow.
     * If the payment was attempted through Razorpay, it takes the user to the Google Payment flow.
     * If the payment was attempted through Google Payment, it takes the user to the Razorpay flow.
     */
    private void doSomethingAfterDialog() {
        isRetryPayment = true;
        if (paymentFailedFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
            selectPlan(PLATINUM_PLAN_MONTHLY);
        } else if (paymentFailedFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE)) {
            getOrderIdNew();

        }
        paymentFailedFrom = "";
    }

    boolean getOrderIdReq,isRetryPayment;

    /*** Updates the UI to display subscription details and pricing for Razorpay payments.
     *
     * This method configures the purchase screen based on the user's country,
     * the selected plan, and whether a free trial is available.
     */
    private void showProductsDetailPriceRazorpay() {
        // Define currency symbols.
        String tamilRupeeSymbol = "ரூ.";
        String indianRupeeSymbol = "\u20B9";
        String dollarSymbol = "$";

        String planPrice;
        String formattedPrice;

        // Step 1: Determine the correct currency and price based on the user's country.
        boolean isIndia = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this)
                .equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND);

        if (isIndia) {
            planPrice = servicelistModal.getPriceInRS();
            // Use Tamil rupee symbol if the language is Tamil.
            formattedPrice = (LANGUAGE_CODE == 2) ? tamilRupeeSymbol + planPrice : indianRupeeSymbol + planPrice;
        } else {
            planPrice = servicelistModal.getPriceInDollor();
            formattedPrice = dollarSymbol + planPrice;
        }
        // Store the final formatted price.
        platinumPlanPriceMonth = formattedPrice;


        // Step 2: Update UI based on whether a free trial is available.
        if (servicelistModal.isFreeTrialAvailable()) {
            phonePePaymentLayout.setVisibility(GONE);
            // --- Free Trial UI ---
            tvBtnText.setText(getResources().getString(R.string.try_now_for_free));
            freeTrialAmountTV.setText(getString(R.string.pay_deducted_refunded_immediately, String.valueOf(servicelistModal.getFreeTrialAmount())));

            // Display renewal information, checking if a special 3-month plan is active.
            if (CUtils.is3MonthSubsEnabled(this)) {
                subsBeginTV.setText(getResources().getString(R.string.subscription_starts_on_date, FreeTrialFragment.getAutoPayDate(servicelistModal.getFreeTrialPeriod())));
                renewalDateTV.setText(FreeTrialFragment.getFormattedAutoPayFor3MonthString(this, servicelistModal.getPriceInRS()));
            } else {
                subsBeginTV.setText(getString(R.string.subscription_begins));
                renewalDateTV.setText(getString(R.string.subscription_begins_on_date_cancel_anytime_before_no_charges_no_penalties, FreeTrialFragment.getAutoPayDate(servicelistModal.getFreeTrialPeriod()), String.valueOf(servicelistModal.getPriceInRS())));
            }
        } else {
            if (isPhonePeSubscriptionEnabled) {
                phonePePaymentLayout.setVisibility(View.VISIBLE);
                subscribeForBtn.setVisibility(View.GONE);
            } else {
                subscribeForBtn.setVisibility(View.VISIBLE);
                phonePePaymentLayout.setVisibility(View.GONE);
            }
            btnStartSubsPayment.setText(getResources().getString(R.string.button_text_with_price, platinumPlanPriceMonth));

            setPriceOnBtnAccordingly();

        }
        // Step 3: Update the feature list text based on the selected subscription tab (Dhruv vs. Plus).
        if (selectedTab == TAB_DHRUV) {
            unlimited_chat_text_5x.setText(getString(R.string.more_kundli_ai_ques_day_than_plus, "5x"));
            unlimited_chat_text_500.setText(getString(R.string.ques_day_to_ai_astrologers, "100"));
        } else if (selectedTab == TAB_PLUS) {
            KAPlusQuesCountTV.setText(getString(R.string.ask_num_ques_a_day, servicelistModal.getQuestionLimit()));
        }

        // Update the generic question count text visible on both tabs.
        kundliAIQuestionCount.setText(getString(R.string.up_to_100_questions_day_on_kundli_ai, servicelistModal.getQuestionLimit()));
    }

    /**
     * Displays product details and updates UI with the pricing information.
     * The method formats and sets the pricing text according to the language code.
     * If the domestic or international payment mode is Razorpay, the "Buy Now" button text is updated
     * with the subscription information using the calculated plan price.
     */
    public void getOrderIdNew() {
        getOrderIdReq = false;
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY + fcmLabel, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
        if (TextUtils.isEmpty(countryCode) || countryCode.equals(CGlobalVariables.COUNTRY_CODE_IND)) {
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
        } else {
            amountToPay = PriceInDollor;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
        }

        if (!CUtils.isConnectedWithInternet(PurchaseDhruvPlanActivity.this)) {
            CUtils.showSnackbar(planDhruvRootView, getResources().getString(R.string.no_internet), PurchaseDhruvPlanActivity.this);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
//            Log.e("source_check", "getOrderIdNew: PurchaseDhruv "+source );
            Call<ResponseBody> call = api.getOrderId(CUtils.getParamsForGenerateOrderId(PurchaseDhruvPlanActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, source,isRetryPayment));
            isRetryPayment = false;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject obj = new JSONObject(myResponse);
                        Log.e("TestPayment=> ", obj.toString());
                        if (obj.has("status")) {
                            if (obj.optString("status").equalsIgnoreCase("100")) {
                                getOrderIdReq = true;
                                LocalBroadcastManager.getInstance(PurchaseDhruvPlanActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                startBackgroundLoginService();
                            }
                        } else {
                            String result = obj.getString("Result");
                            if(result.equalsIgnoreCase("1")) {
                            orderId = obj.getString("OrderId");
                            PriceInDollor = obj.getString("PriceToPay");
                            PriceInRS = obj.getString("PriceRsToPay");
                            servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
                            // JSONObject buyResponse = obj.getJSONObject("BuyResponse");
                            subscriptionId = obj.getString("SubscriptionId");
                            startPayment();
                            }else {
                                String errorMsg = obj.getString("Message");
                                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(planDhruvRootView, errorMsg+" ("+result+")", PurchaseDhruvPlanActivity.this);
                            }

                        }
                    } catch (Exception e) {
                        // Log.e("TestPayment=> ", e.toString());
                        CUtils.showSnackbar(planDhruvRootView, e.toString(), PurchaseDhruvPlanActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Log.e("TestPayment=> f", t.toString());
                    hideProgressBar();
                    CUtils.showSnackbar(planDhruvRootView, t.toString(), PurchaseDhruvPlanActivity.this);
                }
            });

        }
    }


    private void startBackgroundLoginService() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {/**/}
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String status = intent.getStringExtra("status");
                if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                    if (getOrderIdReq) {
                        getOrderIdNew();
                    }
                    if(getPhonePeOrderIdReq){
                        getApiCallForPhonePayIntentUrl(selectedUPIAppModel.getPayMethodName());
                    }
                }
                if (mReceiverBackgroundLoginService != null) {
                    LocalBroadcastManager.getInstance(PurchaseDhruvPlanActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            } catch (Exception e) {
                //
            }

        }
    };
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
            CUtils.showSnackbar(planDhruvRootView, getResources().getString(R.string.no_internet), this);
            return;
        }
        showProgressBar();
        Map<String, String> mapNew = CUtils.getParamsForGenerateOrderId(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, "PurchaseDhruvPlanActivity",isRetryPayment);
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
                //Log.d("testApi","response==>>"+response.body().toString());
                handleOrderIdResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(planDhruvRootView, t.toString(), PurchaseDhruvPlanActivity.this);
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
            CUtils.showSnackbar(planDhruvRootView, e.toString(), this);
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
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_INTENT_FLOW_APP_LUNCHED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
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

    private void onSubscriptionPurchaseSuccess(String paymentId, String signature, String orderIdRaz,String isSuccesFrom) {
        try {
            String plan = "";
            String ecommerceLabel = "";
            if (selectedTab == TAB_PLUS) {
                CUtils.storeUserPurchasedPlanInPreference(this, KUNDLI_AI_PRIMIUM_PLAN_ID_11);
                SavePlaninPreference(this, KUNDLI_AI_PRIMIUM_PLAN_ID_11);
                plan = CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH;
                if (servicelistModal.isFreeTrialAvailable()) {
                    if(CUtils.is3MonthSubsEnabled(this)){
                        ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_FREE_TRIAL_3_months_PAYMENT_SUCCESS_RAZORPAY;
                    }else{
                        ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_FREE_TRIAL_PAYMENT_SUCCESS_RAZORPAY;
                    }

                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_PAYMENT_SUCCESS_RAZORPAY;
                }
            } else if (selectedTab == TAB_DHRUV) {
                String showAIPassPlan = CUtils.getStringData(PurchaseDhruvPlanActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_AI_PASS_PLAN_KEY, "0");
                if (showAIPassPlan.equals("1")) {
                    com.ojassoft.astrosage.varta.utils.CUtils.setIsKundliAiProPlan(PurchaseDhruvPlanActivity.this, true);
                }
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.storeUserPurchasedPlanInPreference(this, PLATINUM_PLAN_ID_10);
                SavePlaninPreference(this, PLATINUM_PLAN_MONTHLY);
                plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;
                ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.DHRUV_PAYMENT_SUCCESS_RAZORPAY;
            }

            // ---------------------------------------------------------
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
                if (servicelistModal.isFreeTrialAvailable()) {
                    com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, ecommerceLabel, "PurchaseDhruvPlanActivity");
                } else {
                    CUtils.setEcommercePurchaseEvent(this, orderId, ecommerceLabel, actualrates);
                }
            }

            Intent tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreenNew.class);
            tppsIntent.putExtra(FREE_QUESTIONS_RECEIVED_KEY, "");
            tppsIntent.putExtra("SIGNATURE", signature);
            tppsIntent.putExtra("PURCHASE_DATA", "");
            tppsIntent.putExtra("PLAN", plan);
            tppsIntent.putExtra("price", "");
            tppsIntent.putExtra("priceCurrencycode", currency);
            tppsIntent.putExtra("freetrialperiod", "");
            tppsIntent.putExtra("isSuccesFrom", isSuccesFrom);
            startActivity(tppsIntent);
            this.finish();
        }catch (Exception e){
            //
        }

    }

    // generate params for upi payments order status
    private Map<String, String> getParamsForOrderStatus() {
        Map<String, String> mapNew = new HashMap<>();
        mapNew.put("orderId", merchantOrderId);//only in case of subcription
        mapNew.put("isfreetrialavailable",IsFreeTrialAvailable );
        //mapNew.put("merchantOrderId", merchantOrderId);
        mapNew.put("currency", com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA);
        mapNew.put("PriceRs", String.valueOf(servicelistModal.getPriceInRS()));
        mapNew.put("Price", String.valueOf(servicelistModal.getPriceInDollor()));
        mapNew.put("freetrialamount", servicelistModal.getPhonePeFreeTrialAmount());
        mapNew.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(PurchaseDhruvPlanActivity.this));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(mapNew);
    }
}
