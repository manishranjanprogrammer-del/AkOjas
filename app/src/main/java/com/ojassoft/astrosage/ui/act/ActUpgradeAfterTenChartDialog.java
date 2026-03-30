package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_QUESTIONS_RECEIVED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_10;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Dialog Activity for handling the upgrade flow after user has used ten charts.
 * Manages UI, plan selection, payment flow (Razorpay), and server communication for AI Plus plans.
 */
public class ActUpgradeAfterTenChartDialog extends BaseInputActivity implements
        IPurchasePlan, IPersonalDetails, BillingEventHandler, VolleyResponse, PaymentResultWithDataListener {
    public static final String SKU_PLATINUM_PLAN_MONTH = "platinum_plan_month";
    public static final String SKU_PLATINUM_PLAN_YEAR = "platinum_plan_year";
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_YEAR = 20011;
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_MONTH = 20012;
    public static int BASIC_PLAN = 0;
    public static int PLATINUM_PLAN = 5;
    public static int PLATINUM_PLAN_MONTHLY = 6;
    public static int GET_SERVICE_DETAILS = 3001;
    private final int PHONE_PAY_GATEWAY_CALLBACK = 10001;
    private final int FREE_TRIAL_PROCEED_CALLBACK = 12345;

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    public String platinumPlanPriceYear = "", platinumPlanPriceMonth = "";
    TextView tvthankstext, proceedBtn, btnBuyNow;
    int resultId = 2;//2=Not more than 10 charts and 6 Not more than 500 charts
    String screenId = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;
    String msg = "";
    CheckBox btnProceedDonotShowAgain;
    int requestCode;
    int PRICE = 2, price_amount_micros = 5, price_currency_code = 6, isFreetimeperiod = 7;
    ArrayList<String> arayPlatinumPlanYear = new ArrayList<String>(5);
    ArrayList<String> arayPlatinumPlanMonth = new ArrayList<String>(5);
    ProductDetails skuDetailsPlatinumPlanYear;
    ProductDetails skuDetailsPlatinumPlanMonth;
    String purchaseData = "", signature = "";
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
    TextView headingTV;
    CardView clMainView;
    CustomProgressDialog pd;
    RequestQueue queue;
    String COUNTRY_CODE_IND = "91";
    String currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
    String orderId = "";
    String amountToPay = "0";
    String serviceId, PriceInDollor, PriceInRS, subscriptionId;
    ServicelistModal servicelistModal;
    boolean getOrderIdReq, isLoginPerformed,isRetryPayment;
    String domesticPayMode, internationalPayMode, countryCode;
    int TAB_PLUS = 0, TAB_DHRUV = 1;
    int selectedTab = TAB_PLUS;
    TextView kundliAIQuestionLimitTV, dhruvQuesLimitTV;
    UPIAppModel selectedUPIAppModel;
    private ImageView ivClosePage;
    private boolean isOpenForAIChat;
    private String fcmLabel = "";
    private String paymentFailedFrom = "";
    private LinearLayout plusTabButton, premiumTabButton;
    private LinearLayout plusLayoutView, premiumLayoutView;
    private TextView plusTabText, premiumTabText, dontShowTV;
    private TextView tvSelectedAppName, skip_proceed_button;
    private ImageView ivSelectedAppIcon;
    private Spinner spinnerUpiApps;
    private boolean userSelect = false;
    private List<UPIAppModel> masterList;
    private View divider;
    private ConstraintLayout phonePePaymentLayout;
    private boolean isPhonePeSubscriptionEnabled;
    private Button btnStartSubsPayment;
    private String paymentModeStr = "";
    private boolean getPhonePeOrderIdReq;
    ImageView fullScreenLoader;

    boolean preFetchForFreeTrial;
    ConstraintLayout mainLayout;
    private String merchantOrderId = "", IsFreeTrialAvailable = "";
    public ActUpgradeAfterTenChartDialog() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_plan_after_ten_chart_new_layout);
        setBillingEventHandler(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActUpgradeAfterTenChartDialog.this.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(ActUpgradeAfterTenChartDialog.this, LANGUAGE_CODE, CGlobalVariables.regular);

        fullScreenLoader = findViewById(R.id.full_screen_loader);
        mainLayout = findViewById(R.id.mainLayout);
        clMainView = findViewById(R.id.clMainView);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        purchaseSource = getIntent().getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN);


        String cachedDetails = com.ojassoft.astrosage.varta.utils.CUtils.getPlusPlanServiceDetail();
        if(!TextUtils.isEmpty(cachedDetails)){
            decideForOpeningActivity(cachedDetails);
        }else{
            // Start as fullscreen
            getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );
            clMainView.setCardBackgroundColor(getColor(R.color.transparent));
            fullScreenLoader.setVisibility(View.VISIBLE);
            Glide.with(fullScreenLoader)
                    .load(getResources().getDrawable(R.drawable.new_ai_loader))
                    .into(fullScreenLoader);
            preFetchForFreeTrial = true;
            loadFromServer(false);
        }



        isPhonePeSubscriptionEnabled = CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, true);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            isPhonePeSubscriptionEnabled = false;
        }
        inti();
        initListner();
        setupTabs();

        internationalPayMode = CUtils.getStringData(ActUpgradeAfterTenChartDialog.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
        domesticPayMode = CUtils.getStringData(ActUpgradeAfterTenChartDialog.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
        countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
        setUpSelectedPlanDetails();
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
        try{
            JSONObject obj = new JSONObject(cachedDetails);
            boolean isFreeTrialAvailable = obj.getBoolean("IsFreeTrialAvailable");
            if(isFreeTrialAvailable){
                Intent intent =new Intent(ActUpgradeAfterTenChartDialog.this, PurchaseFreeTrialActivity.class);
//                Log.e("source_check", "decideForOpeningActivity: source -  "+purchaseSource );
                intent.putExtra("source", purchaseSource);
                intent.putExtra("show_proceed_button", true);
                startActivityForResult(intent,FREE_TRIAL_PROCEED_CALLBACK);
            }else{
                animateToDialogSize();
                mainLayout.setVisibility(View.VISIBLE);
                fullScreenLoader.setVisibility(View.GONE);
            }
        }catch (Exception e){
            //
        }


    }
    private void animateToDialogSize() {
        screenLoadedOnce = true; // to stop animation once loaded
        final Window window = getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        clMainView.setCardBackgroundColor(getColor(R.color.backgroundColorView));
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        int targetWidth = (int) (size.x * 0.90);
        int targetHeight = (int) (size.y * 0.90);

        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.9f);
        animator.setDuration(150);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float factor = (float) animation.getAnimatedValue();
            params.width = (int) (size.x * factor);
            params.height = (int) (size.y * factor);
            window.setAttributes(params);
        });

        animator.start();
    }

    /**
     * Initializes UI components and sets up the dialog layout.
     */
    private void inti() {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        isOpenForAIChat = getIntent().getBooleanExtra(CGlobalVariables.OPEN_FOR_AI_CHAT, false);
        ivClosePage = findViewById(R.id.ivClosePage);
        btnBuyNow = findViewById(R.id.buy_now_btn);
        btnStartSubsPayment = findViewById(R.id.btnStartSubsPayment);
        btnBuyNow.setTypeface(mediumTypeface);
        btnStartSubsPayment.setTypeface(mediumTypeface);
        proceedBtn = findViewById(R.id.cancel_btn);
        skip_proceed_button = findViewById(R.id.skip_proceed_button);
        headingTV = findViewById(R.id.tv_heading);
        com.ojassoft.astrosage.varta.utils.FontUtils.changeFont(this, headingTV, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);

        plusTabButton = findViewById(R.id.plus_tab_button);
        premiumTabButton = findViewById(R.id.premium_tab_button);
        plusLayoutView = findViewById(R.id.plus_layout_view);
        premiumLayoutView = findViewById(R.id.premium_layout_view);
        plusTabText = findViewById(R.id.plus_tab_text);
        premiumTabText = findViewById(R.id.premium_tab_text);
        kundliAIQuestionLimitTV = findViewById(R.id.kundli_ai_questLimit_tv);
//        dontShowTV = findViewById(R.id.dont_show_tv);
//
//        dontShowTV.setOnClickListener(v ->
//                btnProceedDonotShowAgain.setChecked(!btnProceedDonotShowAgain.isChecked())
//        );

        dhruvQuesLimitTV = findViewById(R.id.dhruv_plan_question_tv);

        //headingTV.setText(CUtils.getKundliAIPlusSpannableText(this,getString(R.string.upgrade_to_kundli_ai_plus)));

        proceedBtn.setTypeface(mediumTypeface);
        btnProceedDonotShowAgain = findViewById(R.id.checkDobotShowAgain);

        // btnProceedDonotShowAgain.setChecked(CUtils.getDoNotShowAccountUpgradePopupValueInPreference(this));

        tvthankstext = findViewById(R.id.headingTV);
        resultId = getIntent().getIntExtra("resultId", 2);
        msg = getIntent().getStringExtra("msg");

        if (resultId == 6) {
            tvthankstext.setText(getResources().getText(R.string.upgrade_plan_text_for_500));
            screenId = CGlobalVariables.GOLD_PLAN_VALUE_YEAR;
        } else if (resultId == -132) {
            tvthankstext.setText(getResources().getText(R.string.share_chart_error));
            proceedBtn.setText(getResources().getString(R.string.cancel));
        } else if (resultId == -133) {
            tvthankstext.setText(getResources().getText(R.string.share_chart_error));
            proceedBtn.setText(getResources().getString(R.string.cancel));
        } else {
//
        }


        if (msg != null && !msg.isEmpty()) {
            tvthankstext.setText(getResources().getText(R.string.save_notes_error));
            proceedBtn.setText(getResources().getString(R.string.cancel));
        }
        tvthankstext.setTypeface(regularTypeface);
        if (LANGUAGE_CODE == 2) {
            tvthankstext.setTextSize(15);
            btnBuyNow.setTextSize(14);
            proceedBtn.setTextSize(14);
            btnBuyNow.setPadding(5, 7, 5, 7);
            proceedBtn.setPadding(5, 7, 5, 7);

        }
        divider = findViewById(R.id.divider);
        phonePePaymentLayout = findViewById(R.id.phonePePaymentLayout);
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
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the dimensions
//        Window window = getWindow();
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        Point size = new Point();
//        getWindowManager().getDefaultDisplay().getSize(size);
//
//        int width = (int) (size.x * 0.90);
//        int height = (int) (size.y * 0.90);
//
//        layoutParams.width = width;
//        layoutParams.height = height;
//        window.setAttributes(layoutParams);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.transparent)));

        if (isLoginPerformed) {
            isLoginPerformed = false;
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActUpgradeAfterTenChartDialog.this);
            if (isLogin) {
                btnBuyNow.performClick();
            }
        }

    }

    /**
     * Initializes listeners for UI components.
     */
    private void initListner() {

        ivClosePage.setOnClickListener(v -> {
//            CUtils.setDoNotShowAccountUpgradePopupValueInPreference(ActUpgradeAfterTenChartDialog.this, btnProceedDonotShowAgain.isChecked());
            finish();
        });
        btnStartSubsPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBuyNow.performClick();
            }
        });
        btnBuyNow.setOnClickListener(view -> {
            if(servicelistModal==null){
                return;
            }
            if(servicelistModal.isFreeTrialAvailable()) {
                FreeTrialFragment freeTrialFragment = FreeTrialFragment.newInstance(
                        servicelistModal.getFreeTrialPeriod(), // Assuming this variable holds the trial period string (e.g., "7 Days")
                        servicelistModal.getFreeTrialAmount(),
                        servicelistModal.getPriceInRS()
                );
                freeTrialFragment.setPurchasePlanListener(this);
                freeTrialFragment.show(getSupportFragmentManager(), "FreeTrialFragment");
                return;
            }
            if(selectedTab==TAB_PLUS) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW_FOR_PLUS_PLAN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }else if(selectedTab==TAB_DHRUV) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW_FOR_DHRUV_PLAN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }
            // selectPlan(PLATINUM_PLAN_MONTHLY);

            if (CUtils.isUserLogedIn(this)) {//check astrosage login
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

//                String internationalPayMode = CUtils.getStringData(ActUpgradeAfterTenChartDialog.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
//                String domesticPayMode = CUtils.getStringData(ActUpgradeAfterTenChartDialog.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
//                String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
//                if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                    // Check if domestic payment mode is Razorpay or Google
//                    if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                        fcmLabel = "indian";
//                        getOrderIdNew();
//                    } else {
//                        selectPlan(PLATINUM_PLAN_MONTHLY);
//                    }
//                } else {
//                    // Check if international payment mode is Razorpay or Google
//                    if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                        fcmLabel = "international";
//                        getOrderIdNew();
//                    } else {
//                        selectPlan(PLATINUM_PLAN_MONTHLY);
//                    }
//                }
            } else {
                isLoginPerformed = true;
                Intent intent1 = new Intent(ActUpgradeAfterTenChartDialog.this, FlashLoginActivity.class);
                startActivity(intent1);
            }

        });
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_PROCEED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                setPurchaseResult();
            }
        });
        skip_proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedBtn.performClick();
            }
        });
    }



    private void setupTabs() {
        plusTabButton.setOnClickListener(v -> {
            plusTabButton.setBackgroundResource(R.drawable.tab_selected_background);
            premiumTabButton.setBackgroundResource(R.drawable.tab_unselected_background);
            plusLayoutView.setVisibility(View.VISIBLE);
            premiumLayoutView.setVisibility(View.GONE);
            plusTabText.setSelected(true);
            premiumTabText.setSelected(false);
            selectedTab = TAB_PLUS;
            setUpSelectedPlanDetails();
        });

        premiumTabButton.setOnClickListener(v -> {
            premiumTabButton.setBackgroundResource(R.drawable.tab_selected_background);
            plusTabButton.setBackgroundResource(R.drawable.tab_unselected_background);
            premiumLayoutView.setVisibility(View.VISIBLE);
            plusLayoutView.setVisibility(View.GONE);
            premiumTabText.setSelected(true);
            plusTabText.setSelected(false);
            selectedTab = TAB_DHRUV;
            setUpSelectedPlanDetails();
        });

        // Set Plus tab as default
        plusTabButton.performClick();
    }

    /**
     * Fetches product details from shared preferences or Google Play Store.
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

    /**
     * Fetches product details from Google Play Store.
     */
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

    /**
     * Displays a message based on the response code.
     *
     * @param response Response code from Google Play Store.
     */
    private void showMsg(final int response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response >= 0 && response < 9) {
                        Toast.makeText(ActUpgradeAfterTenChartDialog.this, errorResponse[response], Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ActUpgradeAfterTenChartDialog.this, getResources().getString(R.string.internet_is_not_working), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });

    }

    /**
     * Initializes product plan details.
     *
     * @param skuDetails Product details from Google Play Store.
     */
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

    /**
     * Displays product details.
     */
    private void showProductsDetail_new() {
        String rupeesInTamil = "ரூ.";
        String planPrice = "";
        if(servicelistModal.isFreeTrialAvailable()){
            setInCaseFreeTrailAvailable();
            return;
        }

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

    private void setPriceOnBtnAccordingly(){
        if(selectedTab == TAB_PLUS){
            if(CUtils.is3MonthSubsEnabled(this)){
                btnBuyNow.setText(getResources().getString(R.string.subscribe_for_3_text, platinumPlanPriceMonth));
            }else{
                btnBuyNow.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
            }
        }else{
            btnBuyNow.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
        }

    }

    private void setInCaseFreeTrailAvailable() {
        btnBuyNow.setText(getResources().getString(R.string.try_now_for_free));
        divider.setVisibility(View.VISIBLE);
        btnBuyNow.setVisibility(View.VISIBLE);
        proceedBtn.setVisibility(View.VISIBLE);
        phonePePaymentLayout.setVisibility(View.GONE);
    }
    private void setInCasePhonePeGatewayAvailable() {
        divider.setVisibility(View.GONE);
        btnBuyNow.setVisibility(View.GONE);
        proceedBtn.setVisibility(View.GONE);
        phonePePaymentLayout.setVisibility(View.VISIBLE);
        btnStartSubsPayment.setText(getResources().getString(R.string.button_text_with_price, platinumPlanPriceMonth));
        setPriceOnBtnAccordingly();
    }
    private void setInCasePhonePeGatewayNotAvailable() {
        divider.setVisibility(View.VISIBLE);
        btnBuyNow.setVisibility(View.VISIBLE);
        proceedBtn.setVisibility(View.VISIBLE);
        phonePePaymentLayout.setVisibility(View.GONE);
        setPriceOnBtnAccordingly();
    }


    /**
     * Calculates the price.
     *
     * @param strvalue Price string.
     * @return Calculated price string.
     */
    public String PriceCalculator(String strvalue) {
        return strvalue;
    }

    @Override
    public void onYesClick(int planIndex) {
        selectPlan(planIndex);
    }

    /**
     * Selects a plan.
     *
     * @param planIndex Index of the selected plan.
     */
    private void selectPlan(int planIndex) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_GOOGLE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        dumpDataString = "ActNotificationLanding selectPlan() fullJsonDataObj=";
        if (planIndex == PLATINUM_PLAN)
            gotBuyPlatinumPlan();
        if (planIndex == PLATINUM_PLAN_MONTHLY)
            gotBuyPlatinumMonthlyPlan();
    }

    /**
     * Handles the purchase of a platinum plan.
     */
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
                    Toast.makeText(ActUpgradeAfterTenChartDialog.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
        CUtils.postDumpDataToServer(ActUpgradeAfterTenChartDialog.this, dumpDataString);
    }

    /**
     * Handles the purchase of a platinum monthly plan.
     */
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
                    Toast.makeText(ActUpgradeAfterTenChartDialog.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            //Log.e("BillingClient", e.toString());
        }
        CUtils.postDumpDataToServer(ActUpgradeAfterTenChartDialog.this, dumpDataString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("verifyPurchase", "requestCode=" + requestCode + " resultCode=" + resultCode);
        /*
         * Toast.makeText( DialogPlanUpgradeNew.this,"i m here",
         * Toast.LENGTH_LONG).show();
         */
        if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_YEAR || requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH) {
            // do nothing now
        }  else if (requestCode == PHONE_PAY_GATEWAY_CALLBACK) {
            try {
                if (data != null) {
                    Log.d("UPI", "UPI App result" + data.getData());
                    //call order status api
                    getOrderStatusApi();
                }
            } catch (Exception e) {
                Log.d("UPI", "UPI App result" + e.toString());
            }
        } else if (requestCode == FREE_TRIAL_PROCEED_CALLBACK) {
            proceedBtn.performClick();
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

            if (requestCode == SUB_RC_REQUEST_PLATINUM_PLAN_MONTH) {
                SavePlaninPreference(PLATINUM_PLAN_MONTHLY);
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
            //Do not show update purchase plan in ActAppModule
            CUtils.saveBooleanData(ActUpgradeAfterTenChartDialog.this, CGlobalVariables.doNotShowMessageAgainForUpdatePlan, true);
//            CUtils.setDoNotShowAccountUpgradePopupValueInPreference(getApplicationContext(), false);
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                dumpDataString = dumpDataString + " signature=" + purchase.getSignature() + " purchaseData=" + purchase.getOriginalJson();
                if (purchase != null) {
                    setPurchaseResult();
                    gotoThanksPage(purchase, plan, price, priceCurrencycode, formattedPrice, freeTrialPeriodTxt);
                }
            }

        } else {
            paymentFailedFrom = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE;
            showRazorpayFailureDialog();
            dumpDataString = dumpDataString + " ResponseFAIL";
            Log.e("BillingClient", "onPurchasesUpdated() FAIL");
        }
        CUtils.postDumpDataToServer(ActUpgradeAfterTenChartDialog.this, dumpDataString);
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
            selectPlan(PLATINUM_PLAN_MONTHLY);
        } else if (paymentFailedFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE)) {
            getOrderIdNew();

        }
        paymentFailedFrom = "";
    }

    /**
     * Gets the question count for a specific plan.
     *
     * @param plan Plan name.
     * @return Question count.
     */
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

    /**
     * Navigates to the thank-you page after a successful purchase.
     *
     * @param purchase           Purchase object.
     * @param plan               Plan name.
     * @param price              Price.
     * @param priceCurrencycode  Currency code.
     * @param formattedPrice     Formatted price.
     * @param freetrialperiodtxt Free trial period text.
     */
    private void gotoThanksPage(Purchase purchase, String plan, String price, String priceCurrencycode, String formattedPrice, String freetrialperiodtxt) {
        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();
        Log.e("BillingClient", "gotoThanksPage()");
        if (CUtils.isConnectedWithInternet(ActUpgradeAfterTenChartDialog.this)) {
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

    /**
     * Verifies the purchase from the server.
     *
     * @param price             Price.
     * @param priceCurrencycode Currency code.
     * @param formattedPrice    Formatted price.
     * @param freetrialperiod   Free trial period.
     */
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
            pvsIntent.putExtra("source", purchaseSource);
            startService(pvsIntent);
        } catch (Exception e) {
            Log.e("BillingClient", "verifyPurchaseFromService() Exception=" + e);
        }
    }

    /**
     * Saves the plan in preferences.
     *
     * @param newPlanIndex Index of the new plan.
     */
    private void SavePlaninPreference(int newPlanIndex) {
        SharedPreferences sharedPreferences = ActUpgradeAfterTenChartDialog.this
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newPlanIndex == PLATINUM_PLAN) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR);
        } else if (newPlanIndex == PLATINUM_PLAN_MONTHLY) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan,
                    CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH);
        }
        sharedPrefEditor.commit();
    }

    @Override
    public void selectedPlan(int planIndex, UPIAppModel upiAppModel) {
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_AI_FREE_TRIAL__AFTER_TEN_CHART_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            if (CUtils.isUserLogedIn(this)) {//check astrosage login
                getOrderIdNew();
            } else {
                isLoginPerformed = true;
                Intent intent1 = new Intent(ActUpgradeAfterTenChartDialog.this, FlashLoginActivity.class);
                startActivity(intent1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sets the purchase result.
     */
    private void setPurchaseResult() {
//        CUtils.setDoNotShowAccountUpgradePopupValueInPreference(this, btnProceedDonotShowAgain.isChecked());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("screenId", screenId);
        setResult(RESULT_OK, resultIntent);
        finish();
//        CUtils.setDoNotShowAccountUpgradePopupValueInPreference(
//                getActivity(), btnProceedDonotShowAgain.isChecked());
//        if (getActivity() instanceof HomeInputScreen) {
//            ((HomeInputScreen) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
//        } else if (getActivity() instanceof HomeMatchMakingInputScreen) {
//            ((HomeMatchMakingInputScreen) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
//        } else if (getActivity() instanceof OutputMatchingMasterActivity) {
//            ((OutputMatchingMasterActivity) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
//        } else {
//            ((OutputMasterActivity) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
//        }

    }

    /**
     * Fetches available AI Kundli Plus plan services from the server.
     * Handles no-internet scenario and shows progress dialog.
     */
    private void setUpSelectedPlanDetails() {
        String cachedDetails = selectedTab == TAB_PLUS ?
                com.ojassoft.astrosage.varta.utils.CUtils.getPlusPlanServiceDetail() :
                com.ojassoft.astrosage.varta.utils.CUtils.getPremiumPlanServiceDetail();

        if (!TextUtils.isEmpty(cachedDetails)) {
            parseServiceDetails(cachedDetails);
            return;
        }
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActUpgradeAfterTenChartDialog.this)) {
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(clMainView, getResources().getString(R.string.no_internet), ActUpgradeAfterTenChartDialog.this);
        } else {
          loadFromServer(true);

        }
    }

    private void loadFromServer(boolean showProgress) {
        if (showProgress)
            showProgressBar();
        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.SERVICE_LIST_KUNDLI_AI_PLANS;
       // Log.e("errorCheck", "loadFromServer: "+CUtils.buildUrlWithParams(url,CUtils.planDetailsRequestParams(ActUpgradeAfterTenChartDialog.this)));
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                ActUpgradeAfterTenChartDialog.this, false, CUtils.planDetailsRequestParams(ActUpgradeAfterTenChartDialog.this), GET_SERVICE_DETAILS).getMyStringRequest();
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void parseServiceDetails(String JObject) {
        try {
            JSONObject obj = new JSONObject(JObject);
            if (servicelistModal == null)
                servicelistModal = new ServicelistModal();

            servicelistModal.setTitle(obj.getString("Title"));
            servicelistModal.setServiceId(obj.getString("ServiceId"));
            servicelistModal.setPriceInDollor(obj.getString("PriceInDollor"));
            servicelistModal.setPriceInRS(obj.getString("PriceInRS"));

            servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
            servicelistModal.setFreeTrialPeriod(obj.getInt("FreeTrialPeriod"));
            servicelistModal.setFreeTrialNotifyPeriod(obj.getInt("FreeTrialNotifyPeriod"));
            servicelistModal.setQuestionLimit(obj.getString("QuestionLimit"));
            servicelistModal.setPhonePeFreeTrialAmount(obj.getString("PhonePeFreeTrialAmount"));
            servicelistModal.setFreeTrialAmount(obj.getInt("FreeTrialAmount"));
            amountToPay = PriceInRS;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;

            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
                amountToPay = PriceInDollor;
                currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
            }
            showProductsDetail_new();
            showProductsDetailPriceRazorpay();
        } catch (Exception e) {

        }


    }

    /**
     * Shows the product detail price using Razorpay pricing.
     * This method retrieves and formats the plan price based on the user's language preference.
     * It sets the text on the "Buy Now" button according to the user's country and payment mode,
     * using the calculated subscription price in the appropriate currency.
     */
    private void showProductsDetailPriceRazorpay() {
        String rupeesInTamil = "ரூ.";
        String planPrice = "";

        if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
            planPrice = servicelistModal.getPriceInRS();
            if (LANGUAGE_CODE == 2) {
                platinumPlanPriceMonth = rupeesInTamil + planPrice;
            } else {
                platinumPlanPriceMonth = "\u20B9" + planPrice;
            }
        }else{
            planPrice = servicelistModal.getPriceInDollor();
            platinumPlanPriceMonth = "$" + planPrice;
        }

        if(servicelistModal.isFreeTrialAvailable()){
            setInCaseFreeTrailAvailable();
        }else {
            if(isPhonePeSubscriptionEnabled){
                setInCasePhonePeGatewayAvailable();
            }else {
                setInCasePhonePeGatewayNotAvailable();
            }

        }
        if (selectedTab == TAB_DHRUV)
            dhruvQuesLimitTV.setText(getString(R.string.ask_up_to_100_question_a_day_with_kundli_ai, servicelistModal.getQuestionLimit()));
        else if (selectedTab == TAB_PLUS)
            kundliAIQuestionLimitTV.setText(getString(R.string.ask_up_to_100_question_a_day_with_kundli_ai, servicelistModal.getQuestionLimit()));
//        if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//            if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                btnBuyNow.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
//            }
//        } else {
//            if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                btnBuyNow.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
//            }
//        }
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

        currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;

        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
        if (TextUtils.isEmpty(countryCode) || countryCode.equals(CGlobalVariables.COUNTRY_CODE_IND)) {
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
        } else {
            amountToPay = PriceInDollor;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
        }
        if (!CUtils.isConnectedWithInternet(ActUpgradeAfterTenChartDialog.this)) {
            CUtils.showSnackbar(clMainView, getResources().getString(R.string.no_internet), ActUpgradeAfterTenChartDialog.this);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            //  Log.e("source_check", "getOrderIdNew: "+purchaseSource );
            Call<ResponseBody> call = api.getOrderId(CUtils.getParamsForGenerateOrderId(ActUpgradeAfterTenChartDialog.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, purchaseSource,isRetryPayment));
            isRetryPayment = false;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject obj = new JSONObject(myResponse);
                        // Log.e("TestPayment=> ", obj.toString());
                        if (obj.has("status")) {
                            if (obj.optString("status").equalsIgnoreCase("100")) {
                                getOrderIdReq = true;
                                LocalBroadcastManager.getInstance(ActUpgradeAfterTenChartDialog.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                startBackgroundLoginService();
                            }
                        } else  {
                            String result = obj.getString("Result");
                            if(result.equalsIgnoreCase("1")) {
                                orderId = obj.getString("OrderId");
                                PriceInDollor = obj.getString("PriceToPay");
                                PriceInRS = obj.getString("PriceRsToPay");
                                servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
                                //JSONObject buyResponse = obj.getJSONObject("BuyResponse");
                                subscriptionId = obj.getString("SubscriptionId");
                                startPayment();
                            }else {
                                String errorMsg = obj.getString("Message");
                                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(clMainView, errorMsg+" ("+result+")", ActUpgradeAfterTenChartDialog.this);
                            }


                        }
                    } catch (Exception e) {
                        // Log.e("TestPayment=> ", e.toString());
                        CUtils.showSnackbar(clMainView, e.toString(), ActUpgradeAfterTenChartDialog.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Log.e("TestPayment=> f", t.toString());
                    hideProgressBar();
                    CUtils.showSnackbar(clMainView, t.toString(), ActUpgradeAfterTenChartDialog.this);
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
                    LocalBroadcastManager.getInstance(ActUpgradeAfterTenChartDialog.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            } catch (Exception e) {
                //
            }

        }
    };
//    /**
//     * Requests an order ID from the backend before starting the subscription process.
//     * Logs analytics event for Razorpay plan upgrade attempt.
//     */
//    private void getOrderId() {
//        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActUpgradeAfterTenChartDialog.this)) {
//            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(clMainView, getResources().getString(R.string.no_internet), ActUpgradeAfterTenChartDialog.this);
//        } else {
//            showProgressBar();
//            String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GENRATE_ORDER_ASKQUE;
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    ActUpgradeAfterTenChartDialog.this, false, CUtils.getParamsForGenerateOrderId(ActUpgradeAfterTenChartDialog.this, serviceId, PriceInDollor, PriceInRS), GET_ORDER_ID).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
//        }

//    /**
//     * Requests a subscription ID from the backend for Razorpay subscription flow.
//     * Handles no-internet scenario and shows progress dialog.
//     */
//    private void setGetSubscriptionId() {
//        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActUpgradeAfterTenChartDialog.this)) {
//            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(clMainView, getResources().getString(R.string.no_internet), ActUpgradeAfterTenChartDialog.this);
//        } else {
//            showProgressBar();
//            String url = CGlobalVariables.RAZORPAY_SUBSCRIPTION_ID_URL;
//            //String url = "https://bt.astrosage.com/RazorpayCreateSubscriptionServlet";
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    ActUpgradeAfterTenChartDialog.this, false, CUtils.getParamsForSubscriptionId(ActUpgradeAfterTenChartDialog.this), GET_SUBSCRIPTION_ID).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
//        }
//    }

    /**
     * Handles network responses for different request methods (order id, service details, subscription id).
     * Parses the response and triggers the next step in the subscription flow.
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
                hideProgressBar();
               // Log.e("errorCheck", "onResponse: in exception "+e.getMessage() );
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
       // Log.e("errorCheck", "onResponse: in error "+error.getMessage() );
        hideProgressBar();
        //
    }

    private static boolean  screenLoadedOnce = false;
    /**
     * Hides the progress bar.
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            if(!screenLoadedOnce){
                animateToDialogSize();
                mainLayout.setVisibility(View.VISIBLE);
                fullScreenLoader.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

    }

    /**
     * Shows a custom progress dialog if not already showing.
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(ActUpgradeAfterTenChartDialog.this);
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
        }

    }

    /**
     * Initiates the Razorpay payment flow using the subscription ID.
     * Sets up payment options and opens the Razorpay checkout UI.
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

            JSONObject prefill = new JSONObject();
            prefill.put("contact", phoneNo);
            if(!TextUtils.isEmpty(email)){
                prefill.put("email", email);
            }
            options.put("prefill", prefill);
            if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActUpgradeAfterTenChartDialog.this).equals(COUNTRY_CODE_IND)){
                options.put("checkout_config_id", "config_RqFj9XRuaSWUgl");
            }

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
            notes.put("orderFromDomain", "AK_KUNDLI_AI_PLAN_PURCHASE");
            notes.put("isfreetrialavailable", servicelistModal.isFreeTrialAvailable());
            notes.put("freetrialperiod", servicelistModal.getFreeTrialPeriod());
            notes.put("freetrialnotifyperiod", servicelistModal.getFreeTrialNotifyPeriod());
            options.put("notes", notes);
            //Log.d("testPyaement", "payment started"+options);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error starting payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback for successful Razorpay payment.
     * Logs analytics, updates user preferences, and navigates to the thank-you screen.
     */
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            // Extract subscription details from payment data
             //String paymentId = paymentData.getPaymentId();
            String signature = paymentData.getSignature();
            //String orderId = paymentData.getOrderId();
            onSubscriptionPurchaseSuccess("",signature,"",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY);
            // Toast.makeText(this, "Subscription activated successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("TestRazorPayPayment", "Error processing payment success", e);
            //Toast.makeText(this, "Error processing payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback for Razorpay payment failure.
     * Logs analytics and can show error messages if needed.
     */
    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY_FAILURE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        paymentFailedFrom = com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE;
        showRazorpayFailureDialog();
        //Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
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
            CUtils.showSnackbar(clMainView, getResources().getString(R.string.no_internet), this);
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
                Log.d("testApi","respose==>>"+response.body().toString());
                handleOrderIdResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(clMainView, t.toString(), ActUpgradeAfterTenChartDialog.this);
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
            Log.d("testApi","respose==>>"+root);

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
            CUtils.showSnackbar(clMainView, e.toString(), this);
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

    private void onSubscriptionPurchaseSuccess(String paymentId, String signature, String orderIdNew,String isSuccesFrom) {
        try {
            String plan="";
            String ecommerceLabel = "";
            if(selectedTab==TAB_PLUS) {
                CUtils.storeUserPurchasedPlanInPreference(this, KUNDLI_AI_PRIMIUM_PLAN_ID_11);
                SavePlaninPreference(KUNDLI_AI_PRIMIUM_PLAN_ID_11);
                plan = CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH;
                if (servicelistModal.isFreeTrialAvailable()) {
                    if(CUtils.is3MonthSubsEnabled(this)){
                        ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_FREE_TRIAL_3_months_PAYMENT_SUCCESS_RAZORPAY;
                    }else{
                        ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_FREE_TRIAL_PAYMENT_SUCCESS_RAZORPAY;
                    }                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    ecommerceLabel = com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_AI_PAYMENT_SUCCESS_RAZORPAY;
                }
            } else if (selectedTab == TAB_DHRUV) {
                String showAIPassPlan = CUtils.getStringData(ActUpgradeAfterTenChartDialog.this, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_AI_PASS_PLAN_KEY,"0");
                if(showAIPassPlan.equals("1")) {
                    com.ojassoft.astrosage.varta.utils.CUtils.setIsKundliAiProPlan(ActUpgradeAfterTenChartDialog.this,true);
                }
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.storeUserPurchasedPlanInPreference(this, PLATINUM_PLAN_ID_10);
                SavePlaninPreference(PLATINUM_PLAN_MONTHLY);
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
                    com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, ecommerceLabel, "ActUpgradeAfterTenChartDialog");
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
        mapNew.put("freetrialamount",  servicelistModal.getPhonePeFreeTrialAmount());
        mapNew.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(ActUpgradeAfterTenChartDialog.this));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(mapNew);
    }
}
