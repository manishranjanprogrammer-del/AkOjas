package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_QUESTIONS_RECEIVED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_10;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PURCHASE_SOURCE_FROM_DHRUV_DIALOG_AFTER_LIMIT_EXCEED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN_MONTHLY;
import static com.ojassoft.astrosage.varta.utils.CUtils.SavePlaninPreference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.adapters.UPIAppAdapter;
import com.ojassoft.astrosage.varta.dialog.SubscriptionPaymentFailDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.model.UPIAppModel;
import com.ojassoft.astrosage.varta.service.Loginservice;
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
 * Activity that handles the purchase flow when a user exceeds their Kundli AI plus plan Question limit.
 * This activity displays Dhruv plan and processes in-app purchases.
 * It integrates with the billing client for subscription management and handles payment callbacks.
 *
 * <p>Key features:
 * <ul>
 *   <li>Displays Dhruv plan with pricing</li>
 *   <li>Handles in-app purchase flow for subscriptions</li>
 *   <li>Manages payment callbacks and purchase verification</li>
 *   <li>Integrates with payment gateways for secure transactions</li>
 * </ul>
 *
 * @author Gaurav
 * @see BaseInputActivity
 * @see IPurchasePlan
 * @see IPersonalDetails
 * @see BillingEventHandler
 * @see PaymentResultWithDataListener
 * @see VolleyResponse
 */
public class LimitExceedDhruvPurchaseActivity extends BaseInputActivity implements
        IPurchasePlan, IPersonalDetails, BillingEventHandler, PaymentResultWithDataListener, VolleyResponse {


    /**
     * Product IDs for in-app purchases
     */
    /**
     * Monthly subscription plan product ID
     */
    public static final String SKU_PLATINUM_PLAN_MONTH = "platinum_plan_month";

    /**
     * Yearly subscription plan product ID
     */
    public static final String SKU_PLATINUM_PLAN_YEAR = "platinum_plan_year";

    /**
     * Request code for yearly subscription purchase
     */
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_YEAR = 20011;

    /**
     * Request code for monthly subscription purchase
     */
    static final int SUB_RC_REQUEST_PLATINUM_PLAN_MONTH = 20012;
    /**
     * Constant representing the basic plan type
     */
    public static final int BASIC_PLAN = 0;

    /**
     * Request code for fetching service details
     */
    public static final int GET_SERVICE_DETAILS = 3001;
    ServicelistModal servicelistModal;
    /**
     * Request code for generating order ID
     */
    public static final int GET_ORDER_ID = 4001;
    /**
     * Request code for getting subscription ID
     */
    public static final int GET_SUBSCRIPTION_ID = 5001;
    /**
     * Current language code for localization
     */
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    /**
     * Typeface used for text views
     */
    public Typeface typeface;

    /**
     * Price strings for yearly and monthly plans
     */
    public String platinumPlanPriceYear = "", platinumPlanPriceMonth = "";

    /**
     * Request code for various operations
     */
    int requestCode;

    // Constants for array indices
    /**
     * Index for price in plan details array
     */
    final int PRICE = 2;
    /**
     * Index for price amount in micros in plan details array
     */
    final int price_amount_micros = 5;
    /**
     * Index for currency code in plan details array
     */
    final int price_currency_code = 6;
    /**
     * Index for free trial period flag in plan details array
     */
    final int isFreetimeperiod = 7;

    /**
     * List to store yearly plan details
     */
    ArrayList<String> arayPlatinumPlanYear = new ArrayList<String>(5);
    /**
     * List to store monthly plan details
     */
    ArrayList<String> arayPlatinumPlanMonth = new ArrayList<String>(5);

    /**
     * Product details for yearly subscription
     */
    ProductDetails skuDetailsPlatinumPlanYear;
    /**
     * Product details for monthly subscription
     */
    ProductDetails skuDetailsPlatinumPlanMonth;
    String purchaseData = "", signature = "";
    String screenId = "";
    String[] errorResponse = {"Success", "Billing response result user canceled", "Network connection is down", "Billing API version is not supported for the type requested", "Requested product is not available for purchase", "Invalid arguments provided to the API", "Fatal error during the API action", "Failure to purchase since item is already owned", "Failure to consume since item is not owned"};
    String purchaseSource;
    String dumpDataString = "";
    String source = PURCHASE_SOURCE_FROM_DHRUV_DIALOG_AFTER_LIMIT_EXCEED;
    RelativeLayout planDhruvRootView;
    CustomProgressDialog pd;
    RequestQueue queue;
    String COUNTRY_CODE_IND = "91";
    String currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
    String orderId = "";
    String amountToPay = "0";
    String PriceInDollor, PriceInRS, subscriptionId;
    // UI components
    private ImageView ivClosePage;
    private RelativeLayout subscribeForBtnDiable, subscribeForBtn;
    private TextView tvBtnText, tv_btn_txt_diable;
    public boolean isOpenForAIChat, isLoginPerformed;

    private String fcmLabel = "";
    private TextView txtAlreadyPurchasePlan;
    private String paymentFailedFrom = "";
    private static final int TAB_PLUS = 0;
    private static final int TAB_DHRUV = 1;
    int selectedTab = TAB_PLUS;
    TextView questionLimitTv;
    private TextView tvSelectedAppName;
    private ImageView ivSelectedAppIcon;
    private Spinner spinnerUpiApps;
    private boolean userSelect = false;
    UPIAppModel selectedUPIAppModel;
    private List<UPIAppModel> masterList;
    private String paymentModeStr = "";
    private final int PHONE_PAY_GATEWAY_CALLBACK= 10001;
    boolean getPhonePeOrderIdReq,isRetryPayment;
    String merchantOrderId="",IsFreeTrialAvailable ="";
    private boolean isPhonePeSubscriptionEnabled;
    private ConstraintLayout phonePePaymentLayout;
    private Button btnStartSubsPayment;
    public LimitExceedDhruvPurchaseActivity() {
        super(R.id.app_name);
    }

    /**
     * Encodes an email address by shifting each character's ASCII value.
     * This is used for basic obfuscation of email addresses.
     *
     * @param emailId The email address to encode
     * @return The encoded email address
     */
    public static String replaceEmailChar(String emailId) {

        try {
            int xCharacterAhead = 1;
            emailId = encodeWithAsciiValue(emailId, xCharacterAhead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailId;
    }

    /**
     * Encodes a string by shifting each character's ASCII value by a specified amount.
     *
     * @param stringToEncode  The string to encode
     * @param XAheadCharacter The number of positions to shift each character
     * @return The encoded string
     */
    static String encodeWithAsciiValue(String stringToEncode, int XAheadCharacter) {
        StringBuilder newString = new StringBuilder();
        for (int iterator = 0; iterator < stringToEncode.length(); ++iterator) {
            newString.append((char) (stringToEncode.charAt(iterator) + XAheadCharacter));
        }
        return newString.toString();
    }

    String domesticPayMode, internationalPayMode, countryCode;

    /**
     * Initializes the activity, sets up the UI components, and prepares the purchase flow.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testSubFail", "Called onCreate");
        setContentView(R.layout.dhruv_upgrade_pop_up_design);
        TextView aiModelTV = findViewById(R.id.font_auto_dhruv_upgrade_pop_up_design_2);
        TextView premiumKundliTV = findViewById(R.id.font_auto_dhruv_upgrade_pop_up_design_3);
        TextView brandingTV = findViewById(R.id.font_auto_dhruv_upgrade_pop_up_design_4);
        TextView discountTV = findViewById(R.id.font_auto_dhruv_upgrade_pop_up_design_5);
        FontUtils.changeFont(this, aiModelTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, premiumKundliTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, brandingTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, discountTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        setBillingEventHandler(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) LimitExceedDhruvPurchaseActivity.this.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(LimitExceedDhruvPurchaseActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        isPhonePeSubscriptionEnabled = CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, true);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            isPhonePeSubscriptionEnabled = false;
        }
        inti();
        initListner();
        internationalPayMode = CUtils.getStringData(LimitExceedDhruvPurchaseActivity.this, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
        domesticPayMode = CUtils.getStringData(LimitExceedDhruvPurchaseActivity.this, CGlobalVariables.DOMESTIC_PAY_MODE, "");
        countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(LimitExceedDhruvPurchaseActivity.this);
    }

    /**
     * Initializes UI components and sets up the initial view state.
     * This method should be called during activity creation to set up all
     * the necessary UI elements and their initial states.
     */
    private void inti() {
        ivClosePage = findViewById(R.id.ivClosePage);
        subscribeForBtn = findViewById(R.id.subscribeForBtn);
        subscribeForBtnDiable = findViewById(R.id.subscribeForBtnDiable);
        btnStartSubsPayment = findViewById(R.id.btnStartSubsPayment);
        phonePePaymentLayout = findViewById(R.id.phonePePaymentLayout);
        planDhruvRootView = findViewById(R.id.planDhruvRootView);
        txtAlreadyPurchasePlan = findViewById(R.id.txt_already_purchase_plan);
        questionLimitTv = findViewById(R.id.unlimited_chat_text);

        tvBtnText = findViewById(R.id.tv_btn_txt);
        tv_btn_txt_diable = findViewById(R.id.tv_btn_txt_diable);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        if (!CUtils.isConnectedWithInternet(this)) {
            CUtils.showSnackbar(planDhruvRootView, getString(R.string.no_internet), this);
            return;
        }


        // Make network request for fresh data
        showProgressBar();
        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.SERVICE_LIST_KUNDLI_AI_PLANS;
        StringRequest request = new VolleyServiceHandler(Request.Method.POST, url, this, false, CUtils.planDetailsRequestParams(this), GET_SERVICE_DETAILS).getMyStringRequest();

        request.setShouldCache(false);
        queue.add(request);
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
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Called when the activity will start interacting with the user.
     * This is where we check if we need to resume any pending purchases
     * or update the UI based on the current subscription status.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Log.d("testSubFail", "onResume called PurchaseDhruvPlanActivity");
        if (isLoginPerformed) {
            isLoginPerformed = false;
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(LimitExceedDhruvPurchaseActivity.this);
            if (isLogin) {
                subscribeForBtn.performClick();
            }
        }

    }

    /**
     * Sets up click listeners for UI components.
     * This method initializes all the click listeners for buttons and other interactive
     * elements in the activity.
     */
    private void initListner() {

        ivClosePage.setOnClickListener(view -> {
            finish();
        });
        btnStartSubsPayment.setOnClickListener(view -> {
            subscribeForBtn.performClick();
        });
        // When the Subscribe button is tapped, verify login and launch the appropriate purchase flow:
        // 1. If user is logged in, read domestic/international pay mode setting and start Razorpay order or Google subscription.
        // 2. If not logged in, redirect to the login screen.
        subscribeForBtn.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.DHRUV_UPGRADE_SUBSCRIBE_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            if (CUtils.isUserLogedIn(LimitExceedDhruvPurchaseActivity.this)) {
                //check astrosage login
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
            } else {
                isLoginPerformed = true;
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_DHRUV_NOT_LOGGED_IN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent1 = new Intent(LimitExceedDhruvPurchaseActivity.this, FlashLoginActivity.class);
                startActivity(intent1);
            }


        });
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
            CUtils.showSnackbar(planDhruvRootView, getResources().getString(R.string.no_internet), this);
            return;
        }
        showProgressBar();
        Map<String, String> mapNew = CUtils.getParamsForGenerateOrderId(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, "LimitExceedDhruvPurchaseActivity",isRetryPayment );
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
                CUtils.showSnackbar(planDhruvRootView, t.toString(), LimitExceedDhruvPurchaseActivity.this);
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
        mapNew.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(LimitExceedDhruvPurchaseActivity.this));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(mapNew);
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
        if (planIndex == PLATINUM_PLAN) gotBuyPlatinumPlan();
        if (planIndex == PLATINUM_PLAN_MONTHLY) gotBuyPlatinumMonthlyPlan();

    }

    public void gotBuyPlatinumPlan() {
        dumpDataString = dumpDataString + " gotBuyPlatinumPlan()";
        try {
            if (skuDetailsPlatinumPlanYear != null) {
                requestCode = SUB_RC_REQUEST_PLATINUM_PLAN_YEAR;

                ImmutableList productDetailsParamsList = ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(skuDetailsPlatinumPlanYear).setOfferToken(skuDetailsPlatinumPlanYear.getSubscriptionOfferDetails().get(0).getOfferToken()).build());

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

                dumpDataString = dumpDataString + " gotBuyPlatinumPlan() prepayment responseCode=" + responseCode;
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(LimitExceedDhruvPurchaseActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Log.e("RESPONSE_ERROR", e.getMessage());
        }
        CUtils.postDumpDataToServer(LimitExceedDhruvPurchaseActivity.this, dumpDataString);
    }

    public void gotBuyPlatinumMonthlyPlan() {
        dumpDataString = dumpDataString + " gotBuyPlatinumMonthlyPlan()";
        try {
            if (skuDetailsPlatinumPlanMonth != null) {
                requestCode = SUB_RC_REQUEST_PLATINUM_PLAN_MONTH;

                ImmutableList productDetailsParamsList = ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(skuDetailsPlatinumPlanMonth).setOfferToken(skuDetailsPlatinumPlanMonth.getSubscriptionOfferDetails().get(0).getOfferToken()).build());

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                Log.d("BillingClient", "gotBuyPlatinumMonthlyPlan() responseCode=" + responseCode);
                dumpDataString = dumpDataString + " gotBuyPlatinumMonthlyPlan() prepayment responseCode=" + responseCode;
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(LimitExceedDhruvPurchaseActivity.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            //Log.e("BillingClient", e.toString());
        }
        CUtils.postDumpDataToServer(LimitExceedDhruvPurchaseActivity.this, dumpDataString);
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
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this, CGlobalVariables.GOOGLE_ANALYTIC_CLOUD, CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_SUCCESS, null, dPrice, purchaseSource);
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
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this, CGlobalVariables.GOOGLE_ANALYTIC_CLOUD, CGlobalVariables.GOOGLE_ANALYTIC_PLATINUM_PLAN_YEARLY_SUCCESS, null, dPrice, purchaseSource);
            }
            //Do not show update purchase plan in ActAppModule
            CUtils.saveBooleanData(LimitExceedDhruvPurchaseActivity.this, CGlobalVariables.doNotShowMessageAgainForUpdatePlan, true);
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
        CUtils.postDumpDataToServer(LimitExceedDhruvPurchaseActivity.this, dumpDataString);
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
        if (CUtils.isConnectedWithInternet(LimitExceedDhruvPurchaseActivity.this)) {
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
            Intent pvsIntent = new Intent(getApplicationContext(), PurchaseVerificationService.class);
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
            if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(LimitExceedDhruvPurchaseActivity.this).equals(COUNTRY_CODE_IND)){
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
//            notes.put("isfreetrialavailable",servicelistModal.isFreeTrialAvailable());
//            notes.put("freetrialperiod", servicelistModal.getFreeTrialPeriod());
//            notes.put("freetrialnotifyperiod", servicelistModal.getFreeTrialNotifyPeriod());
//            notes.put("isfreetrailenable", isFreeTrailAvailable);
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
                parseServiceDetails(premiumService.toString());
            } catch (Exception e) {
                Log.e("testRazorpay", "onResponse: exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        //
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
            if (servicelistModal == null) servicelistModal = new ServicelistModal();

            servicelistModal.setTitle(obj.getString("Title"));
            servicelistModal.setServiceId(obj.getString("ServiceId"));
            servicelistModal.setPriceInDollor(obj.getString("PriceInDollor"));
            servicelistModal.setPriceInRS(obj.getString("PriceInRS"));
            servicelistModal.setQuestionLimit(obj.getString("QuestionLimit"));
            servicelistModal.setPhonePeFreeTrialAmount(obj.getString("PhonePeFreeTrialAmount"));
            amountToPay = PriceInRS;
            currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;

            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
                amountToPay = PriceInDollor;
                currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
            }
            showProductsDetailPriceRazorpay();

        } catch (Exception e) {
            Log.e("serviceDetailCheck", "parseServiceDetails: exception- " + e);
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) pd.dismiss();
        } catch (Exception e) {
        }
    }

    /**
     * Shows a custom progress dialog if not already showing.
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(LimitExceedDhruvPurchaseActivity.this);
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
    /**
     * Callback method that is invoked when a payment is successfully processed.
     *
     * @param s           The payment ID
     * @param paymentData The payment data containing transaction details
     */
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        //Log.d("testRazorpay", "paymentData"+paymentData.toString());
        //Log.d("testRazorpay", "paymentData"+s.toString());
        try {
            //String paymentId = paymentData.getPaymentId();
            String signature = paymentData.getSignature();
            //String orderId = paymentData.getOrderId();
            onSubscriptionPurchaseSuccess("",signature,"",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY);
            //Toast.makeText(this, "Subscription activated successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Toast.makeText(this, "Error processing payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onSubscriptionPurchaseSuccess(String s, String signature, String s1, String razorpay) {
        String plan = "";
        if (selectedTab == TAB_PLUS) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            CUtils.storeUserPurchasedPlanInPreference(this, KUNDLI_AI_PRIMIUM_PLAN_ID_11);
            SavePlaninPreference(this, KUNDLI_AI_PRIMIUM_PLAN_ID_11);
            plan = CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH;
        } else if (selectedTab == TAB_DHRUV) {
            String showAIPassPlan = CUtils.getStringData(LimitExceedDhruvPurchaseActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_AI_PASS_PLAN_KEY, "0");
            if (showAIPassPlan.equals("1")) {
                com.ojassoft.astrosage.varta.utils.CUtils.setIsKundliAiProPlan(LimitExceedDhruvPurchaseActivity.this, true);
            }
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            CUtils.storeUserPurchasedPlanInPreference(this, PLATINUM_PLAN_ID_10);
            SavePlaninPreference(this, PLATINUM_PLAN_MONTHLY);
            plan = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;
        }


        Intent tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreenNew.class);
        tppsIntent.putExtra(FREE_QUESTIONS_RECEIVED_KEY, "");
        tppsIntent.putExtra("SIGNATURE", signature);
        tppsIntent.putExtra("PURCHASE_DATA", "");
        tppsIntent.putExtra("PLAN", plan);
        tppsIntent.putExtra("price", "");
        tppsIntent.putExtra("priceCurrencycode", currency);
        tppsIntent.putExtra("freetrialperiod", "");
        tppsIntent.putExtra("isSuccesFrom", com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY);
        startActivity(tppsIntent);
        this.finish();
    }

    /**
     * Callback for Razorpay payment failure.
     * Logs analytics and can show error messages if needed.
     */
    /**
     * Callback method that is invoked when a payment fails.
     *
     * @param errorCode        The error code indicating the reason for the failure
     * @param errorDescription A human-readable description of the error
     * @param paymentData      The payment data associated with the failed transaction
     */
    @Override
    public void onPaymentError(int errorCode, String errorDescription, PaymentData paymentData) {
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

    boolean getOrderIdReq;

    /**
     * Formats and displays the subscription price based on user's country and language.
     * <p>
     * Flow:
     * 1. Checks user's country code
     * 2. For Indian users:
     * - Uses rupee symbol (₹) for English
     * - Uses Tamil rupee symbol (ரூ.) for Tamil language
     * 3. For international users:
     * - Uses dollar symbol ($)
     * 4. Updates both active and disabled subscribe button texts
     * <p>
     * The method handles currency formatting for:
     * - Domestic (Indian) users with language-specific rupee symbols
     * - International users with dollar symbol
     */
    private void showProductsDetailPriceRazorpay() {
        String rupeesInTamil = "ரூ.";
        String planPrice = "";
//        if(servicelistModal.isFreeTrialAvailable()){
//            tvBtnText.setText(getResources().getString(R.string.try_now_for_free));
//            return;
//        }
        if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
            planPrice = servicelistModal.getPriceInRS();
            if (LANGUAGE_CODE == 2) {
                platinumPlanPriceMonth = rupeesInTamil + planPrice;
            } else {
                platinumPlanPriceMonth = "\u20B9" + planPrice;
            }
        } else {
            planPrice = servicelistModal.getPriceInDollor();
            platinumPlanPriceMonth = "$" + planPrice;
        }
        if(isPhonePeSubscriptionEnabled){
            phonePePaymentLayout.setVisibility(View.VISIBLE);
            subscribeForBtn.setVisibility(View.GONE);
            btnStartSubsPayment.setText(getResources().getString(R.string.button_text_with_price, platinumPlanPriceMonth));
        }
        tvBtnText.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
        tv_btn_txt_diable.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
        questionLimitTv.setText(getString(R.string.num_ques_a_day_txt,servicelistModal.getQuestionLimit()));

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

        if (!CUtils.isConnectedWithInternet(LimitExceedDhruvPurchaseActivity.this)) {
            CUtils.showSnackbar(planDhruvRootView, getResources().getString(R.string.no_internet), LimitExceedDhruvPurchaseActivity.this);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
//            Log.e("source_check", "getOrderIdNew: PurchaseDhruv "+source );
            Call<ResponseBody> call = api.getOrderId(CUtils.getParamsForGenerateOrderId(LimitExceedDhruvPurchaseActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, source,isRetryPayment));
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
                                LocalBroadcastManager.getInstance(LimitExceedDhruvPurchaseActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                startBackgroundLoginService();
                            }
                        } else {
                            String result = obj.getString("Result");
                            if(result.equalsIgnoreCase("1")) {
                                orderId = obj.getString("OrderId");
                                PriceInDollor = obj.getString("PriceToPay");
                                PriceInRS = obj.getString("PriceRsToPay");
                                // JSONObject buyResponse = obj.getJSONObject("BuyResponse");
                                subscriptionId = obj.getString("SubscriptionId");
                                startPayment();
                            }else {
                                String errorMsg = obj.getString("Message");
                                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(planDhruvRootView, errorMsg+" ("+result+")", LimitExceedDhruvPurchaseActivity.this);
                            }


                        }
                    } catch (Exception e) {
                        Log.e("TestPayment=> error", e.toString());
                        CUtils.showSnackbar(planDhruvRootView, e.toString(), LimitExceedDhruvPurchaseActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Log.e("TestPayment=> f", t.toString());
                    hideProgressBar();
                    CUtils.showSnackbar(planDhruvRootView, t.toString(), LimitExceedDhruvPurchaseActivity.this);
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
                }
                if (mReceiverBackgroundLoginService != null) {
                    LocalBroadcastManager.getInstance(LimitExceedDhruvPurchaseActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            } catch (Exception e) {
                //
            }

        }
    };
}
