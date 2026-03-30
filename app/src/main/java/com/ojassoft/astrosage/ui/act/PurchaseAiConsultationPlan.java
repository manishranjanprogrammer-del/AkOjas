package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_QUESTIONS_RECEIVED_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_ID_10;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SERVICE_DETAILS_BROADCAST;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.ServiceToGetPurchasePlanDetails;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.dialog.SubscriptionPaymentFailDialog;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for handling the purchase process of AI Astrologer consultation plans using Razorpay.
 * <p>
 * <b>Features:</b>
 * <ul>
 *   <li>Displays AI consultation plan details and pricing (INR/USD)</li>
 *   <li>Handles plan selection (Pro/Dhruv) with tabbed UI</li>
 *   <li>Fetches plan details from server and caches them</li>
 *   <li>Processes payments via Razorpay, including order ID generation and payment callbacks</li>
 *   <li>Handles payment success/failure, analytics, and navigation to thank you screen</li>
 *   <li>Supports background login if required by backend</li>
 *   <li>Manages broadcast receivers for plan details and login events</li>
 * </ul>
 * <b>Usage:</b>
 * <ol>
 *   <li>User selects a plan tab (Pro or Dhruv)</li>
 *   <li>Plan details are fetched and displayed</li>
 *   <li>User clicks subscribe, triggering order ID generation and Razorpay checkout</li>
 *   <li>Handles payment result and navigates accordingly</li>
 * </ol>
 *
 * <b>Requirements:</b>
 * <ul>
 *   <li>Razorpay SDK integration</li>
 *   <li>Retrofit for API calls</li>
 *   <li>Internet connectivity</li>
 *   <li>User authentication</li>
 * </ul>
 *
 * @author AstroSage Team
 * @version 2.0
 * @since 2024
 */
public class PurchaseAiConsultationPlan extends BaseInputActivity implements PaymentResultWithDataListener {

    /** Tab index for Pro plan */
    public static final int TAB_PRO = 0;
    /** Tab index for Dhruv plan */
    public static final int TAB_DHRUV = 1;

    // UI Components
    /** Close button for the activity */
    private ImageView ivClosePage;
    /** Root view for the activity */
    private ConstraintLayout planAiConsultationPlanRootView;
    /** Heading text view */
    private TextView tv_heading, txtHeading1, tvBtnText, proTabTextTV, dhruvTabTextTV, dhruvPlanQuestionCountTV,dhruvPlanKundliTv;
    /** Subscribe button */
    private RelativeLayout subscribeForBtn, planDhruvRootView;
    /** Tab buttons and layouts */
    private LinearLayout proTabBtn, dhruvTabBtn, proPlanLayout, dhruvPlanLayout;

    // Data and State
    /** Current language code */
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    /** Typeface for custom fonts */
    public Typeface typeface;
    /** Progress dialog for loading states */
    private CustomProgressDialog pd;
    /** Model for service/plan details */
    private ServicelistModal servicelistModal;
    /** Current currency (INR/USD) */
    private String currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
    /** Price in USD */
    private String PriceInDollor;
    /** Price in INR */
    private String PriceInRS;
    /** Subscription ID for Razorpay */
    private String subscriptionId;
    /** Whether order ID request is pending after background login */
    private boolean getOrderIdReq,isRetryPayment;
    /** Current order ID */
    private String orderId = "";
    /** Source string for analytics */
    private String source = "";
    /** Currently selected tab (Pro/Dhruv) */
    private int selectedTab = TAB_PRO;

    // Broadcast Management
    /** Local broadcast manager for event handling */
    private LocalBroadcastManager localBroadcastManager;
    /** Broadcast receiver for plan details */
    private BroadcastReceiver planDetailsReceiver;

    /**
     * Default constructor. Calls super with app name resource.
     */
    public PurchaseAiConsultationPlan() {
        super(R.id.app_name);
    }

    /**
     * Lifecycle method called when the activity is first created.
     * Initializes UI components, sets up listeners, and fetches AI plan details.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this contains the data most recently supplied.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_ai_consultation_plan);
        initializeAppSettings();
        init();
        setupBroadcastReceiver();
        setupTabs();
        setupClickListeners();
    }

    /**
     * Initializes application-level settings like language code and font.
     */
    private void initializeAppSettings() {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(this, LANGUAGE_CODE, CGlobalVariables.regular);
    }

    /**
     * Initializes UI components and applies appropriate fonts.
     * Retrieves source parameter from intent if available.
     */
    private void init() {
        extractIntentData();
        initializeViews();
        applyFonts();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Extracts data from the intent extras.
     */
    private void extractIntentData() {
        if (getIntent() != null) {
            source = getIntent().getStringExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_PARAMS);
        }
    }

    /**
     * Initializes all view references.
     */
    private void initializeViews() {
        tv_heading = findViewById(R.id.tv_heading);
        txtHeading1 = findViewById(R.id.txtHeading1);
        subscribeForBtn = findViewById(R.id.subscribeForBtn);
        ivClosePage = findViewById(R.id.ivClosePage);
        tvBtnText = findViewById(R.id.tv_btn_txt);
        proPlanLayout = findViewById(R.id.pro_plan_layout);
        dhruvPlanLayout = findViewById(R.id.premium_layout_view);
        proTabTextTV = findViewById(R.id.pro_tab_text);
        dhruvTabTextTV = findViewById(R.id.premium_tab_text);
        planDhruvRootView = findViewById(R.id.planDhruvRootView);
        proTabBtn = findViewById(R.id.pro_tab_button);
        dhruvTabBtn = findViewById(R.id.premium_tab_button);
        planAiConsultationPlanRootView = findViewById(R.id.planAiConsultationPlanRootView);

        dhruvPlanQuestionCountTV = findViewById(R.id.dhruv_plan_question_tv);
        dhruvPlanKundliTv = findViewById(R.id.dhruv_plan_kundli_100x_tv);
        TextView aiPassPlanTextPro = findViewById(R.id.ai_pass_plan_text_pro);
        String aiPassPlanCount = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(this, CGlobalVariables.AI_Pass_Daily_Chat_Count, "100");
        //dhruvPlanQuestionCountTV.setText(com.ojassoft.astrosage.varta.utils.CUtils.getAIPassStringFromConfig(this,false));
        aiPassPlanTextPro.setText(getString(R.string.ask_up_to_100_question_a_day_with_ai_astrologer,"100"));

    }

    /**
     * Applies custom fonts to text views for consistent styling.
     */
    private void applyFonts() {
        FontUtils.changeFont(this, tv_heading, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, txtHeading1, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, tvBtnText, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
    }

    /**
     * Sets up click listeners for UI components.
     */
    private void setupClickListeners() {
        ivClosePage.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PURCHASE_AI_CONSULTATION_CLOSE_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            finish();
        });

        subscribeForBtn.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PURCHASE_AI_CONSULTATION_SUBSCRIBE_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            getOrderIdNew();
        });
    }

    /**
     * Sets up click listeners for tab buttons.
     */
    private void setupTabs() {
        proTabBtn.setOnClickListener(v -> selectTab(TAB_PRO));
        dhruvTabBtn.setOnClickListener(v -> selectTab(TAB_DHRUV));
        proTabBtn.performClick(); // Set Pro tab as default
    }

    /**
     * Selects the specified tab and updates UI accordingly.
     *@author Gaurav kumar das
     * */
    private void selectTab(int tabIndex) {
        selectedTab = tabIndex;

        if (tabIndex == TAB_PRO) {
            proTabBtn.setBackgroundResource(R.drawable.tab_selected_background);
            dhruvTabBtn.setBackgroundResource(R.drawable.tab_unselected_background);
            proPlanLayout.setVisibility(View.VISIBLE);
            dhruvPlanLayout.setVisibility(View.GONE);
            proTabTextTV.setSelected(true);
            dhruvTabTextTV.setSelected(false);
        } else {
            dhruvTabBtn.setBackgroundResource(R.drawable.tab_selected_background);
            proTabBtn.setBackgroundResource(R.drawable.tab_unselected_background);
            dhruvPlanLayout.setVisibility(View.VISIBLE);
            proPlanLayout.setVisibility(View.GONE);
            dhruvTabTextTV.setSelected(true);
            proTabTextTV.setSelected(false);
        }

        setupSelectedPlanDetails();
    }

    /**
     * Fetches AI consultation plan details from the server using Retrofit.
     */
    public void getProPlanRechargeAmount() {
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressBar();
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAiPassDetails(getUserRechargeParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    String myResponse = response.body().string();
                    com.ojassoft.astrosage.varta.utils.CUtils.setAiPassPlanServiceDetail(myResponse);
                    getServiceDetails(myResponse);
                } catch (Exception e) {
                    // Handle exceptions silently
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(PurchaseAiConsultationPlan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Prepares parameters for the AI plan details API request.
     */
    private Map<String, String> getUserRechargeParams() {
        boolean firstvisitstatusValue = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITSTATUS,false);
        String  firstvisitdatetimeValue = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME,"");
        Map<String, String> map = new HashMap<>();
        map.put("countrycode", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_USER_ID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITSTATUS, firstvisitstatusValue+"");
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME, firstvisitdatetimeValue);
        return map;
    }

    /**
     * Generates a new order ID for Razorpay payment processing.
     */
    public void getOrderIdNew() {
        getOrderIdReq = false;
        setCurrencyBasedOnCountry();

        if (!CUtils.isConnectedWithInternet(this)) {
            CUtils.showSnackbar(planAiConsultationPlanRootView, getResources().getString(R.string.no_internet), this);
            return;
        }

        showProgressBar();
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getOrderId(CUtils.getParamsForGenerateOrderId(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY, currency, servicelistModal, source,isRetryPayment));
        isRetryPayment = false;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                handleOrderIdResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(planAiConsultationPlanRootView, t.toString(), PurchaseAiConsultationPlan.this);
            }
        });
    }

    /**
     * Sets currency based on user's country code.
     */
    private void setCurrencyBasedOnCountry() {
        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this);
        currency = (TextUtils.isEmpty(countryCode) || countryCode.equals(CGlobalVariables.COUNTRY_CODE_IND))
                ? com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA
                : com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
    }

    /**
     * Handles the order ID response from the server.
     */
    private void handleOrderIdResponse(retrofit2.Response<ResponseBody> response) {
        try {
            String myResponse = response.body().string();
            JSONObject obj = new JSONObject(myResponse);

            if (obj.has("status") && obj.optString("status").equalsIgnoreCase("100")) {
                handleBackgroundLoginRequired();
            } else {
                extractOrderDetails(obj);
                startPayment();
            }
        } catch (Exception e) {
            CUtils.showSnackbar(planAiConsultationPlanRootView, e.toString(), this);
        }
    }

    /**
     * Handles background login requirement.
     */
    private void handleBackgroundLoginRequired() {
        getOrderIdReq = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService,
                new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        startBackgroundLoginService();
    }

    /**
     * Extracts order details from the response.
     */
    private void extractOrderDetails(JSONObject obj) throws Exception {
        String result = obj.getString("Result");
        if(result.equalsIgnoreCase("1")) {
            orderId = obj.getString("OrderId");
            PriceInDollor = obj.getString("PriceToPay");
            PriceInRS = obj.getString("PriceRsToPay");
            subscriptionId = obj.getString("SubscriptionId");
        }else {
            String errorMsg = obj.getString("Message");
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(planDhruvRootView, errorMsg+" ("+result+")", PurchaseAiConsultationPlan.this);
        }
    }

    /**
     * Starts a background service to handle user authentication.
     */
    private void startBackgroundLoginService() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
            // Silent exception handling
        }
    }

    /**
     * Initializes and configures the Razorpay payment checkout process.
     */
    private void startPayment() {
        try {
            final Checkout checkout = new Checkout();
            checkout.setFullScreenDisable(true);
            checkout.setKeyID(getResources().getString(R.string.razorpay_key));

            JSONObject options = createPaymentOptions();
            checkout.open(this, options);
            Log.d("testPyaement", "payment started" + options);
        } catch (Exception e) {
            Toast.makeText(this, "Error starting payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates payment options for Razorpay checkout.
     */
    private JSONObject createPaymentOptions() throws Exception {
        JSONObject options = new JSONObject();
        options.put("name", "AstroSage AI");
        options.put("description", "Monthly Subscription");
        options.put("theme.color", "#ff6f00");
        options.put("currency", currency);
        options.put("subscription_id", subscriptionId);
        if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(PurchaseAiConsultationPlan.this).equals(COUNTRY_CODE_IND)){
            options.put("checkout_config_id", "config_RqFj9XRuaSWUgl");
        }

        options.put("prefill", createPrefillData());
        options.put("upi", createUpiOptions());
        options.put("method", createPaymentMethods());
        options.put("notes", createTransactionNotes());

        return options;
    }

    /**
     * Creates prefill data for payment form.
     */
    private JSONObject createPrefillData() throws Exception {
        String phoneNo = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this) + com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this);
        String astrosageId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
        String email="";
        if (astrosageId.contains("@")) {
            email = astrosageId;
        }

        JSONObject prefill = new JSONObject();
        prefill.put("contact", phoneNo);
        if(!TextUtils.isEmpty(email)){
            prefill.put("email", email);
        }
        return prefill;
    }

    /**
     * Creates UPI options for payment.
     */
    private JSONObject createUpiOptions() throws Exception {
        JSONObject upi = new JSONObject();
        upi.put("flow", "intent");
        return upi;
    }

    /**
     * Creates payment methods configuration.
     */
    private JSONObject createPaymentMethods() throws Exception {
        JSONObject method = new JSONObject();
        method.put("upi", true);
        method.put("card", true);
        method.put("netbanking", true);
        method.put("wallet", true);
        return method;
    }

    /**
     * Creates transaction notes for tracking.
     */
    private JSONObject createTransactionNotes() throws Exception {
        String astrosageId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
        String userId = com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(this);

        JSONObject notes = new JSONObject();
        notes.put("orderId", orderId);
        notes.put("asuserid", astrosageId);
        notes.put("userid", userId);
        notes.put("name", astrosageId);
        notes.put("orderType", "1");
        notes.put("appVersion", BuildConfig.VERSION_NAME);
        notes.put("appName", BuildConfig.APPLICATION_ID);
        notes.put("source", "ak_sub");
        notes.put("subscriptionId", subscriptionId);
        notes.put("osname", "ANDROID");
        notes.put("orderFromDomain", CGlobalVariables.AI_PASS_PURCHASE);
        return notes;
    }

    /**
     * Callback method called when Razorpay payment is successful.
     */
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            String signature = paymentData.getSignature();
            trackPaymentSuccess();
            navigateToThankYouScreen(signature);
        } catch (Exception e) {
            // Silent exception handling
        }
    }

    /**
     * Tracks payment success analytics based on selected tab.
     */
    private void trackPaymentSuccess() {
        double actualraters;
        // Parse the 'amountToPay' string into the 'actualraters' double. and if amount>0 setEcommerce Event
        try {
            if (PriceInRS != null && !PriceInRS.isEmpty()) {
                actualraters = Double.parseDouble(PriceInRS);
            } else {
                actualraters = 0;
            }
        } catch (NumberFormatException e) {
            actualraters = 0; // Fallback if parsing fails
            e.printStackTrace();
        }
        String ecommerceLabel="";
        if (selectedTab == TAB_PRO) {
            ecommerceLabel =  com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_PASS_PAYMENT_SUCCESS_RAZORPAY;
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_AI_PASS_RAZORPAY_SUCCESS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            com.ojassoft.astrosage.varta.utils.CUtils.setIsKundliAiProPlan(this, true);
        } else if (selectedTab == TAB_DHRUV) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS_FRIM_AI_PASS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            CUtils.storeUserPurchasedPlanInPreference(this, PLATINUM_PLAN_ID_10);
            com.ojassoft.astrosage.varta.utils.CUtils.SavePlaninPreference(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN_MONTHLY);
            ecommerceLabel =  com.ojassoft.astrosage.varta.utils.CGlobalVariables.DHRUV_PAYMENT_SUCCESS_RAZORPAY;
        }
        CUtils.setEcommercePurchaseEvent(this, orderId,ecommerceLabel, actualraters);
        com.ojassoft.astrosage.varta.utils.CUtils.saveWalletRechargeData("");
    }

    /**
     * Navigates to the thank you screen with payment details.
     */
    private void navigateToThankYouScreen(String signature) {
        Intent tppsIntent = new Intent(getApplicationContext(), ThanksProductPurchaseScreenNew.class);
        com.ojassoft.astrosage.varta.utils.CUtils.setIsKundliAiProPlan(this, true);
        tppsIntent.putExtra(FREE_QUESTIONS_RECEIVED_KEY, "");
        tppsIntent.putExtra("SIGNATURE", signature);
        tppsIntent.putExtra("PURCHASE_DATA", "");
        tppsIntent.putExtra("PLAN", "");
        tppsIntent.putExtra("price", "");
        tppsIntent.putExtra("priceCurrencycode", currency);
        tppsIntent.putExtra("freetrialperiod", "");
        tppsIntent.putExtra("isSuccesFrom", "AI_PASS_PURCHASE");
        startActivity(tppsIntent);
        finish();
    }

    /**
     * Callback method called when Razorpay payment fails.
     */
    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_AI_PASS_RAZORPAY_FAILURE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        showRazorpayFailureDialog();
    }

    /**
     * Shows the Razorpay payment failure dialog.
     */
    private void showRazorpayFailureDialog() {
        isRetryPayment = true;
        CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        SubscriptionPaymentFailDialog dialog = new SubscriptionPaymentFailDialog(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE);
        dialog.setOnDialogActionListener(this::getOrderIdNew);
        dialog.show(fragmentManager, "payment_fail_dialog");
    }

    /**
     * Shows a custom progress dialog if not already showing.
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(this);
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            // Silent exception handling
        }
    }

    /**
     * Hides the progress dialog if it's currently showing.
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) pd.dismiss();
        } catch (Exception e) {
            // Silent exception handling
        }
    }

    /**
     * Parses the service details JSON and populates the ServicelistModal object.
     */
    private void getServiceDetails(String JObject) {
        try {
            JSONObject obj = new JSONObject(JObject);
            if (servicelistModal == null) {
                servicelistModal = new ServicelistModal();
            }
            String  firstvisitdatetimeValue = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME,"");
            if(TextUtils.isEmpty(firstvisitdatetimeValue)){
                com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME,obj.optString("firstvisittimemillis"));
                com.ojassoft.astrosage.varta.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITSTATUS,true);
            }
            populateServiceModal(obj);
            setCurrencyBasedOnCountry();
            showProductsDetailPriceRazorpay();
        } catch (Exception e) {
            // Silent exception handling
        }
    }

    /**
     * Populates the service modal based on selected tab.
     */
    private void populateServiceModal(JSONObject obj) throws Exception {
        if (selectedTab == TAB_PRO) {
            servicelistModal.setTitle(obj.getString("servicename"));
            servicelistModal.setServiceId(obj.getString("serviceid"));
            servicelistModal.setPriceInDollor(obj.getString("rateindollar"));
            servicelistModal.setPriceInRS(obj.getString("rateinrs"));
        } else if (selectedTab == TAB_DHRUV) {
            servicelistModal.setTitle(obj.getString("Title"));
            servicelistModal.setServiceId(obj.getString("ServiceId"));
            servicelistModal.setPriceInDollor(obj.getString("PriceInDollor"));
            servicelistModal.setPriceInRS(obj.getString("PriceInRS"));
//            servicelistModal.setFreeTrialAvailable(obj.getBoolean("IsFreeTrialAvailable"));
//            servicelistModal.setFreeTrialPeriod(obj.getInt("FreeTrialPeriod"));
//            servicelistModal.setFreeTrialNotifyPeriod(obj.getInt("FreeTrialNotifyPeriod"));
            servicelistModal.setQuestionLimit(obj.getString("QuestionLimit"));
        }
    }

    /**
     * Updates the UI to show plan pricing with appropriate currency symbol.
     */
    private void showProductsDetailPriceRazorpay() {
        String planPrice = getPlanPrice();
        String planPriceNew = formatPlanPrice(planPrice);
        tvBtnText.setText(getResources().getString(R.string.subscribe_for_text, planPriceNew));
        if(selectedTab == TAB_DHRUV){
            //dhruvPlanQuestionCountTV.setText(getString(R.string._100_ques_day_to_ai_astrologers,servicelistModal.getQuestionLimit()));
            dhruvPlanQuestionCountTV.setText(getString(R.string.ques_day_to_ai_astrologers,"100"));
            dhruvPlanKundliTv.setText(getString(R.string.s_more_kundli_ai_ques_day_than_basic,"100x"));
        }
    }

    /**
     * Gets the plan price based on user's country.
     */
    private String getPlanPrice() {
        return com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)
                ? servicelistModal.getPriceInRS()
                : servicelistModal.getPriceInDollor();
    }

    /**
     * Formats the plan price with appropriate currency symbol.
     */
    private String formatPlanPrice(String planPrice) {
        if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
            return (LANGUAGE_CODE == 2) ? "ரூ." + planPrice : "\u20B9" + planPrice;
        } else {
            return "$" + planPrice;
        }
    }

    /**
     * Sets up selected plan details based on cached data or network request.
     *@author Gaurav Kumar Das*/
    private void setupSelectedPlanDetails() {
        String cachedDetails = getCachedPlanDetails();

        if (!TextUtils.isEmpty(cachedDetails)) {
            getServiceDetails(cachedDetails);
            return;
        }

        if (!CUtils.isConnectedWithInternet(this)) {
            CUtils.showSnackbar(planDhruvRootView, getString(R.string.no_internet), this);
            return;
        }

        showProgressBar();
        if (selectedTab == TAB_DHRUV) {
            Intent serviceIntent = new Intent(this, ServiceToGetPurchasePlanDetails.class);
            startService(serviceIntent);
        } else {
            getProPlanRechargeAmount();
        }
    }

    /**
     * Gets cached plan details based on selected tab.
     */
    private String getCachedPlanDetails() {
        return selectedTab == TAB_PRO
                ? com.ojassoft.astrosage.varta.utils.CUtils.getAiPassPlanServiceDetail()
                : com.ojassoft.astrosage.varta.utils.CUtils.getPremiumPlanServiceDetail();
    }

    /**
     * BroadcastReceiver for handling background login service responses.
     */
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String status = intent.getStringExtra("status");
                if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS) && getOrderIdReq) {
                    getOrderIdNew();
                }
                if (mReceiverBackgroundLoginService != null) {
                    LocalBroadcastManager.getInstance(PurchaseAiConsultationPlan.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            } catch (Exception e) {
                // Silent exception handling
            }
        }
    };

    /**
     * Sets up broadcast receiver for plan details.
     */
    private void setupBroadcastReceiver() {
        planDetailsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                hideProgressBar();
                if (intent.getAction() != null && intent.getAction().equals(SERVICE_DETAILS_BROADCAST)) {
                    String planDetails = intent.getStringExtra("premiumPlanServiceDetails");
                    if (planDetails != null) {
                        getServiceDetails(planDetails);
                    }
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(SERVICE_DETAILS_BROADCAST);
        localBroadcastManager.registerReceiver(planDetailsReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            localBroadcastManager.unregisterReceiver(planDetailsReceiver);
        } catch (Exception e) {
            Log.e("KundliAiPlusPlanFrag", "Error unregistering receiver", e);
        }
    }
}
