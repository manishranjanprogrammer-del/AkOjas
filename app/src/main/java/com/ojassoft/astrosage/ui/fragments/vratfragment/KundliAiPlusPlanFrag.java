package com.ojassoft.astrosage.ui.fragments.vratfragment;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SERVICE_DETAILS_BROADCAST;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CloudPlanData;
import com.ojassoft.astrosage.customadapters.PlanRecyclerViewAdapter;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.misc.ServiceToGetPurchasePlanDetails;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PlanLocalizationUtil;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.adapters.UPIAppAdapter;
import com.ojassoft.astrosage.varta.model.UPIAppModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Fragment that displays the Kundli AI Plus plan features and handles user interactions
 * for purchasing or viewing details about the plan.
 * <p>
 * This fragment is responsible for:
 * - Displaying plan features and pricing
 * - Handling user interactions for plan purchase
 * - Managing plan data updates
 * - Supporting multiple languages
 * - Handling free vs premium plan views
 */
public class KundliAiPlusPlanFrag extends Fragment {
    // UI Components
    private TextView subscribeText;
    private TextView textViewAstroFeature, textViewExperiance;
    private LinearLayout buyDisable;
    private RecyclerView recyclerView;
    private PlanRecyclerViewAdapter planRecyclerViewAdapter;

    // Data and State
    private ArrayList<CloudPlanData> planDataList;
    private boolean isButtonEnable;
    private boolean isLoginPerformed;
    private int initializeTagManagerCount = 0;
    private Context context;
    private Typeface regularTypeface, mediumTypeface;
    private IPurchasePlan iPurchasePlan;
    private Button btnStartSubsPayment;
    // Broadcast Receiver
    private BroadcastReceiver planDetailsReceiver;
    private static final String ACTION_PLAN_DETAILS = "com.ojassoft.astrosage.PLAN_DETAILS_RECEIVED";
    private LocalBroadcastManager localBroadcastManager;

    // Constants
    private static final String RUPEE_SYMBOL = "\u20B9";
    private static final String TAMIL_RUPEE_SYMBOL = "ரூ.";
    private Spinner spinnerUpiApps;
    private boolean userSelect = false;
    UPIAppModel selectedUPIAppModel;
    private TextView tvTrialByPaying,tvSelectedAppName;
    private ImageView ivCloseBtn,ivSelectedAppIcon;
    private List<UPIAppModel> masterList;
    private ConstraintLayout phonePePaymentLayout;
    private boolean isPhonePeSubscriptionEnabled;
    /**
     * Constructor for KundliAiPlusPlanFrag
     *
     * @param isButtonEnable Whether the subscribe button should be enabled
     */
    public KundliAiPlusPlanFrag(boolean isButtonEnable) {
        this.isButtonEnable = isButtonEnable;
    }

    public KundliAiPlusPlanFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFonts();
        initializeData();
        setupBroadcastReceiver();
    }

    /**
     * Initializes the fonts based on the current language.
     * Sets up regular and medium typefaces for text components.
     */
    private void initializeFonts() {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        mediumTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium);
    }

    /**
     * Initializes data structures and lists.
     * Sets up plan data list and broadcast manager.
     */
    private void initializeData() {
        planDataList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext());
    }

    /**
     * Sets up the broadcast receiver for plan details updates.
     * Handles incoming plan data updates from the service.
     */
    private void setupBroadcastReceiver() {
        planDetailsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(SERVICE_DETAILS_BROADCAST)) {
                    String planDetails = intent.getStringExtra("plusPlanServiceDetails");
                    if (planDetails != null) {
                           setPlanDetails(planDetails);
                    }
                }
            }
        };
    }

    /**
     * Formats the price based on the current language.
     * 
     * @param price The price to format
     * @return Formatted price string with appropriate currency symbol
     */
    private String formatPrice(String price) {
        if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
            return TAMIL_RUPEE_SYMBOL + price;
        }
        return RUPEE_SYMBOL + price;
    }

    /**
     * Creates a CloudPlanData object from JSON data.
     * 
     * @param jsonObject The JSON object containing plan data
     * @param icons Map of icon keys to resource IDs
     * @return CloudPlanData object or null if creation fails
     */
    private CloudPlanData createPlanData(JSONObject jsonObject, HashMap<String, Integer> icons) {
        try {
            String iconKey = jsonObject.getString("icon");
            Integer iconId = icons.get(iconKey);
            if (iconId == null) {
                Log.w("KundliAiPlusPlanFrag", "Icon not found for key: " + iconKey);
                return null;
            }

            CloudPlanData cloudPlanData = new CloudPlanData();
            cloudPlanData.setHeading(jsonObject.getString("heading"));
            cloudPlanData.setDesc(jsonObject.getString("desc"));
            cloudPlanData.setImageID(iconId);
            return cloudPlanData;
        } catch (JSONException e) {
            Log.e("KundliAiPlusPlanFrag", "Error creating plan data", e);
            return null;
        }
    }

    /**
     * Creates a JSON object for the free questions plan.
     * 
     * @return JSONObject containing free questions plan data
     * @throws JSONException if there's an error creating the JSON object
     */
    private JSONObject createFreeQuestionsPlan() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("heading", context.getString(R.string.num_ques_a_day_txt,questionLimit));
        String quesCount = CUtils.getStringData(getContext(), CGlobalVariables.DHRUV_PLAN_QUES_COUNT_KEY, "1000");
        jsonObject.put("desc", context.getString(R.string.kundli_ai_plus_question_limit_description));
        jsonObject.put("icon", "free_chat_icon");
        return jsonObject;
    }

    /**
     * Parses the JSON data and updates the plan list.
     * 
     * @param jsonString The JSON string containing plan data
     */
    private void parseData(String jsonString) {
        try {
            boolean isFreeQuestionInfoAdded = false;
            HashMap<String, Integer> icons = ((PurchasePlanHomeActivity) getActivity()).getIcons();
            if (icons == null) {
                Log.e("KundliAiPlusPlanFrag", "Icons map is null");
                return;
            }

            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("plans");

            // Add free questions plan if needed
            if (jsonArray.length() < 8) {
                jsonArray.put(createFreeQuestionsPlan());
                isFreeQuestionInfoAdded = true;
            }

          //  Log.e("JSONCHECK", "parseData: size=" + jsonArray.length());
            planDataList.clear();

            // Process each plan
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject innerJsonObject = jsonArray.getJSONObject(i);
               // Log.e("JSONCHECK", "parseData: " + innerJsonObject.toString());

                CloudPlanData cloudPlanData = createPlanData(innerJsonObject, icons);
                if (cloudPlanData != null) {
                    if (isFreeQuestionInfoAdded && i == jsonArray.length() - 1) {
                        planDataList.add(0, cloudPlanData);
                    } else {
                        planDataList.add(cloudPlanData);
                    }
                }
            }

            // Update UI
            if (planRecyclerViewAdapter != null) {
                planRecyclerViewAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("KundliAiPlusPlanFrag", "Error in parseData", e);
        }
    }

    String questionLimit;
    boolean isFreeTrialAvailable;
    int freeTrialPeriod;
    String plusPlanPrice ="";
    /**
     * Updates the plan details and pricing display in the UI.
     * 
     * Flow:
     * 1. Validates input plan object
     * 2. Extracts pricing information based on user's country:
     *    - For Indian users: Uses PriceInRS with appropriate currency symbol
     *    - For international users: Uses OriginalPriceInDollor with $ symbol
     * 3. Formats price with appropriate currency symbol:
     *    - ₹ for English
     *    - ரூ. for Tamil
     *    - $ for international users
     * 4. Updates subscribe button text with formatted price
     * 
     * @param planObj JSON string containing plan details including pricing information
     * 
     * Note: This method only updates pricing, free-trial state, and question-limit driven CTA text.
     * The feature list shown in the RecyclerView is loaded separately from local localized JSON so
     * it does not depend on the remote plan payload for translation coverage.
     */
    private void setPlanDetails(String planObj) {
        try {
            if (TextUtils.isEmpty(planObj)) {
                Log.e("KundliAiPlusPlanFrag", "Empty plan object received");
                return;
            }

            JSONObject obj = new JSONObject(planObj);
            String planPrice = "";

            if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                planPrice = obj.getString("PriceInRS");
                plusPlanPrice = formatPrice(planPrice);
            }else{
                planPrice = obj.getString("OriginalPriceInDollor");
                plusPlanPrice = "$" + planPrice;
            }
            isFreeTrialAvailable = obj.getBoolean("IsFreeTrialAvailable");
            freeTrialPeriod = obj.getInt("FreeTrialPeriod");


            if (TextUtils.isEmpty(planPrice)) {
                Log.e("KundliAiPlusPlanFrag", "Empty price received");
                return;
            }
            if (subscribeText != null) {
                if(isFreeTrialAvailable){
                    subscribeText.setText(getResources().getString(R.string.try_now_for_free));
                    phonePePaymentLayout.setVisibility(View.GONE);
                    subscribeText.setVisibility(View.VISIBLE);
                }else {
                    if(isPhonePeSubscriptionEnabled){
                        phonePePaymentLayout.setVisibility(View.VISIBLE);
                        subscribeText.setVisibility(View.GONE);
                    }else {
                        phonePePaymentLayout.setVisibility(View.GONE);
                        subscribeText.setVisibility(View.VISIBLE);
                    }
                    if(CUtils.is3MonthSubsEnabled(context)){
                        subscribeText.setText(getResources().getString(R.string.subscribe_for_3_text, plusPlanPrice));
                    }else{
                        subscribeText.setText(getResources().getString(R.string.subscribe_for_text, plusPlanPrice));
                    }
                    btnStartSubsPayment.setText(getResources().getString(R.string.button_text_with_price, plusPlanPrice));
                }
            }
            questionLimit = obj.getString("QuestionLimit");
            DisableOrEnableBuyBtn();
        } catch (Exception e) {
            Log.e("KundliAiPlusPlanFrag", "Error in setPlanDetails", e);
        }
    }

    /**
     * Handles the platinum plan selection
     *
     * @param planType The type of plan selected
     */
    public void getPlatinumPlan(int planType) {
        if (planType == 0) {
            CUtils.SavePlanForPopupPreference(getActivity(), true);
            if(isFreeTrialAvailable) {
                if(CUtils.is3MonthSubsEnabled(context)){
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_AI_FREE_TRIAL_FOR_3_MONTH_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }else{
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_AI_FREE_TRIAL_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN_MONTHLY_FREE_TRIAL,null);
            }else {
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN_MONTHLY,selectedUPIAppModel);
            }
        }
    }

    private void setBuyDisable(){
        subscribeText.setEnabled(false);
        buyDisable.setVisibility(View.VISIBLE);
        phonePePaymentLayout.setVisibility(View.GONE);
        if(CUtils.is3MonthSubsEnabled(context)){
            subscribeText.setText(getResources().getString(R.string.subscribe_for_3_text, plusPlanPrice));
        }else{
            subscribeText.setText(getResources().getString(R.string.subscribe_for_text, plusPlanPrice));
        }    }

    @Override
    public void onResume() {
        super.onResume();
        // Register broadcast receiver
        IntentFilter filter = new IntentFilter(SERVICE_DETAILS_BROADCAST);
        localBroadcastManager.registerReceiver(planDetailsReceiver, filter);
        //if user login at another Fragment then this will diable btns if user has plans and need to execute only once after login
        if (getActivity() instanceof PurchasePlanHomeActivity && ((PurchasePlanHomeActivity) getActivity()).isPremiumUserAfterLogin) {
            setBuyDisable();
            ((PurchasePlanHomeActivity) getActivity()).isPremiumUserAfterLogin = false;
        }
        // Handle login state
        if (isLoginPerformed) {
            isLoginPerformed = false;
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(getActivity());
            if (isLogin) {
                if (CUtils.isKundliAIPlusPlan(getContext()) || CUtils.isDhruvPlan(getContext())) {
                    ((PurchasePlanHomeActivity) getActivity()).isPremiumUserAfterLogin = true;
                    setBuyDisable();
                } else {
                    subscribeText.performClick();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister broadcast receiver
        try {
            localBroadcastManager.unregisterReceiver(planDetailsReceiver);
        } catch (Exception e) {
            Log.e("KundliAiPlusPlanFrag", "Error unregistering receiver", e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iPurchasePlan = (IPurchasePlan) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lay_platinum_plan, container, false);
        isPhonePeSubscriptionEnabled = CUtils.getBooleanData(getActivity(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, true);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(getActivity()).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            isPhonePeSubscriptionEnabled = false;
        }
        setupView(view);
        showProductsDetailPriceRazorpay();
        setDataInList();
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {

            String buyText = "";

//            if (isFreeDhruvPlanShow) {
//                buyText = getResources().getString(R.string.join_free_month).toUpperCase();
//                subscribeText.setText(buyText);
//            }
//
//        } else {
//            String buyText = "";
//
//            if (isFreeDhruvPlanShow) {
//                buyText = getResources().getString(R.string.join_free_month);
//                subscribeText.setText(buyText);
//            }
        }

        String ScreenId = "KundliAiPlanFrag";
        //To show expiry date
        String expiryDate = CUtils.isExpiryDateAvailableToShow(context, ScreenId);
        LinearLayout expiryDateContainer = (LinearLayout) view.findViewById(R.id.expiry_date_container);
        if (expiryDate != null) {
            TextView tvPlanExpiryDate = (TextView) view.findViewById(R.id.tvPlanExpiryDate);
            tvPlanExpiryDate.setText(expiryDate);
            tvPlanExpiryDate.setTypeface(regularTypeface);
            expiryDateContainer.setVisibility(View.VISIBLE);
        } else {
            expiryDateContainer.setVisibility(View.GONE);
        }

        return view;
    }
    private void onUpiAppSelected(UPIAppModel app) {
        // Called when user selects an app. Do whatever you need (save selection, start payment intent, etc.)
        //Toast.makeText(getActivity(), "Selected: " + app.getName() + " (" + app.getPackageName() + ")", Toast.LENGTH_SHORT).show();
        tvSelectedAppName.setText(app.getName());
        ivSelectedAppIcon.setImageResource(app.getIconResId());

    }

    /**
     * Initializes and configures all UI components for the Kundli AI Plus plan view.
     * Sets up RecyclerView, text views, subscribe button, and their respective states.
     * 
     * @param view The root view of the fragment
     */
    private void setupView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.plan_rv);
        planRecyclerViewAdapter = new PlanRecyclerViewAdapter(this, getActivity(), planDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(planRecyclerViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        phonePePaymentLayout = view.findViewById(R.id.phonePePaymentLayout);
        textViewAstroFeature = (TextView) view.findViewById(R.id.Astrofeatureheading);
        textViewAstroFeature.setTypeface(mediumTypeface);
        textViewExperiance = (TextView) view.findViewById(R.id.Astrofeaturesubheading);
        textViewExperiance.setTypeface(regularTypeface);

        textViewAstroFeature.setVisibility(View.GONE);
        textViewExperiance.setVisibility(View.GONE);

        subscribeText = (TextView) view.findViewById(R.id.buy_tv);
        buyDisable = view.findViewById(R.id.buy_disable);
        btnStartSubsPayment = view.findViewById(R.id.btnStartSubsPayment);

        subscribeText.setTypeface(mediumTypeface);

        //PhonePe gateway settings
        LinearLayout llSelectedAppName = view.findViewById(R.id.llSelectedAppName);
        spinnerUpiApps = view.findViewById(R.id.spinnerApps);
        ivSelectedAppIcon = view.findViewById(R.id.ivSelectedAppIcon);
        tvSelectedAppName = view.findViewById(R.id.tvSelectedAppName);

        masterList = Arrays.asList(
                new UPIAppModel(getActivity().getResources().getString(R.string.phonepe), R.drawable.ic_phonpe_icon, UPIAppChecker.PACKAGE_PHONEPE,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PHONE),
                new UPIAppModel(getActivity().getResources().getString(R.string.gpay), R.drawable.ic_gpay_icon, UPIAppChecker.PACKAGE_GOOGLE_PAY,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_GPAY),
                new UPIAppModel(getActivity().getResources().getString(R.string.paytm), R.drawable.ic_paytm_icon, UPIAppChecker.PACKAGE_PAYTM,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_PAYTM),
                new UPIAppModel(getActivity().getResources().getString(R.string.bhim), R.drawable.ic_bhim_icon, UPIAppChecker.PACKAGE_BHIM,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_BHIM),
                //new UPIAppModel(getActivity().getResources().getString(R.string.amazon_pay), R.drawable.ic_amazon_icon, UPIAppChecker.PACKAGE_AMAZON,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_AMAZON),
                new UPIAppModel(getActivity().getResources().getString(R.string.cred), R.drawable.ic_cred_icon, UPIAppChecker.PACKAGE_CRED,com.ojassoft.astrosage.varta.utils.CGlobalVariables.UPI_PAYMENTS_CRED));

        // 2) Filter based on installed packages on device
        List<UPIAppModel> installedApps = UPIAppChecker.getInstalledAndReadyUpiApps(getActivity(),masterList);
        // 3) Create adapter and set to spinner
        installedApps.add(new UPIAppModel(getActivity().getResources().getString(R.string.razorpay), R.drawable.ic_razorpay_icon, "com.razorpay",com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY));
        UPIAppAdapter adapter = new UPIAppAdapter(
                getActivity(),
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
                //if (first) { first = false; return; } // optional
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

        if (isButtonEnable) {
            subscribeText.setEnabled(true);
            buyDisable.setVisibility(View.GONE);
        } else {
            setBuyDisable();
        }

        setUpBuyBtnListener();

    }

    /**
     * Sets up the click listener for the buy/subscribe button.
     * Handles user authentication and plan purchase flow.
     * - Checks if user is logged in
     * - Initiates login flow if needed
     * - Triggers plan purchase if user is authenticated
     */
    private void setUpBuyBtnListener() {
        subscribeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_FROM_TAB, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                if (CUtils.isUserLogedIn(getActivity())) {//check astrosage login
                    getPlatinumPlan(0);
                } else {
                    isLoginPerformed = true;
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_NOT_LOGGED_IN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    Intent intent1 = new Intent(getActivity(), FlashLoginActivity.class);
                    startActivity(intent1);
                }
            }
        });
        btnStartSubsPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeText.performClick();
            }
        });
    }


    /**
     * Initializes the feature list shown in the RecyclerView for the current language.
     * This list is intentionally sourced from app-side localized JSON because not every visible app
     * language has a matching remote platinum-plan feature payload.
     */
    private void setDataInList() {
        String data = PlanLocalizationUtil.getPlatinumPlanFeatureJson(LANGUAGE_CODE);
        parseData(data);
    }

    /**
     * Enables or disables the buy button based on user's plan status.
     * Checks if user has a premium plan or Dhruv plan.
     */
    private void DisableOrEnableBuyBtn() {
        int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(getActivity());

        if (CUtils.isDhruvPlan(requireContext()) || purchasePlanId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
            setBuyDisable();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * Displays the product details and pricing for the Kundli AI Plus plan.
     * This method handles both cached and fresh data scenarios:
     * 1. If cached plan details exist, it displays them immediately
     * 2. If no cached data exists, it triggers a service to fetch fresh data
     *
     * The fetched payload is limited to price, trial, and question-limit metadata. It does not
     * replace the localized feature rows that are loaded through {@link #setDataInList()}.
     */
    private void showProductsDetailPriceRazorpay() {
            String planObj = com.ojassoft.astrosage.varta.utils.CUtils.getPlusPlanServiceDetail();
            if (!TextUtils.isEmpty(planObj)) {
                setPlanDetails(planObj);
            } else {
                // Call service when planObj is null
                Intent serviceIntent = new Intent(getActivity(), ServiceToGetPurchasePlanDetails.class);
                getActivity().startService(serviceIntent);
            }
    }
}
