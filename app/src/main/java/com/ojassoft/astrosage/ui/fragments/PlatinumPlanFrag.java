package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CloudPlanData;
import com.ojassoft.astrosage.customadapters.PlanRecyclerViewAdapter;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.adapters.UPIAppAdapter;
import com.ojassoft.astrosage.varta.model.UPIAppModel;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment that displays the Platinum (Dhruv) plan features and handles user interactions
 * for purchasing or viewing details about the plan.
 * 
 * This fragment is responsible for:
 * - Displaying plan features in a RecyclerView
 * - Handling plan selection and purchase flow
 * - Managing subscription states
 * - Supporting multiple languages
 * - Handling both free and paid plan scenarios
 * 
 * @see Fragment
 * @see IPurchasePlan
 * @see CloudPlanData
 * @see PlanRecyclerViewAdapter
 */
public class PlatinumPlanFrag extends Fragment {


    private static final org.apache.commons.logging.Log log = LogFactory.getLog(PlatinumPlanFrag.class);
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface, mediumTypeface;

    private TextView buyTV;
    private IPurchasePlan iPurchasePlan;
    Activity activity;

    PlanRecyclerViewAdapter planRecyclerViewAdapter;
    private List<CloudPlanData> planDataList;
    private boolean isButtonEnable;
    private boolean isFreeDhruvPlanShow;
    private LinearLayout buyDisable;
    private ConstraintLayout phonePePaymentLayout;
    private boolean isLoginPerformed;
    RecyclerView recyclerView;
    Context context;

    ArrayList<CloudPlanData> cloudPlanDataArrayList;
    private Spinner spinnerUpiApps;
    private boolean userSelect = false;
    UPIAppModel selectedUPIAppModel;
    private TextView tvTrialByPaying,tvSelectedAppName;
    private ImageView ivCloseBtn,ivSelectedAppIcon;
    private List<UPIAppModel> masterList;
    private Button btnStartSubsPayment;

    public PlatinumPlanFrag() {

    }

    /**
     * Creates a new instance of PlatinumPlanFrag with specified button state and plan visibility.
     * 
     * @param isButtonEnable Whether the buy button should be enabled
     * @param isFreeDhruvPlanShow Whether to show the free Dhruv plan
     */
    @SuppressLint("ValidFragment")
    public PlatinumPlanFrag(boolean isButtonEnable, boolean isFreeDhruvPlanShow) {
        this.isButtonEnable  = isButtonEnable;
        this.isFreeDhruvPlanShow = isFreeDhruvPlanShow;
    }

    /**
     * Initializes the fragment with language settings and typefaces.
     * Sets up the plan data list for the RecyclerView.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        mediumTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium);
        planDataList = new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
        iPurchasePlan = (IPurchasePlan) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
        iPurchasePlan = null;
    }
    private boolean isPhonePeSubscriptionEnabled;
    /**
     * Inflates the layout, initializes UI components, sets up click listeners and adapters.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.lay_premium_plan_frag, container, false);
        recyclerView = view.findViewById(R.id.prem_plan_rv);
        buyTV = view.findViewById(R.id.buy_tv);
        phonePePaymentLayout = view.findViewById(R.id.phonePePaymentLayout);
        btnStartSubsPayment = view.findViewById(R.id.btnStartSubsPayment);
        buyDisable = view.findViewById(R.id.buy_disable);
        isPhonePeSubscriptionEnabled = CUtils.getBooleanData(getActivity(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, true);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(getActivity()).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            isPhonePeSubscriptionEnabled = false;
        }
        if(isPhonePeSubscriptionEnabled){
            buyTV.setVisibility(View.GONE);
            phonePePaymentLayout.setVisibility(View.VISIBLE);
        }else {
            buyTV.setVisibility(View.VISIBLE);
            phonePePaymentLayout.setVisibility(View.GONE);
        }

//        String internationalPayMode = CUtils.getStringData(activity, CGlobalVariables.INTERNATIONAL_PAY_MODE, "");
//        String domesticPayMode = CUtils.getStringData(activity, CGlobalVariables.DOMESTIC_PAY_MODE, "");
//        if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//            if (domesticPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                showProductsDetailPriceRazorpay();
//            } else {
//                showProductsDetailPriceGoogle();
//            }
//        } else {
//            if (internationalPayMode.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
//                showProductsDetailPriceRazorpay();
//            } else {
//                showProductsDetailPriceGoogle();
//            }
//        }
        showProductsDetailPriceRazorpay();
        setPremiumDataInList();
        planRecyclerViewAdapter = new PlanRecyclerViewAdapter(this, getActivity(), cloudPlanDataArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(planRecyclerViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        setUpBuyBtnListener();
        String ScreenId = "PlatinumPlanFrag";
        //To show expiry date
        String expiryDate = CUtils.isExpiryDateAvailableToShow(requireContext(), ScreenId);
        LinearLayout expiryDateContainer = (LinearLayout) view.findViewById(R.id.expiry_date_container);
        if (expiryDate != null) {
            TextView tvPlanExpiryDate = (TextView) view.findViewById(R.id.tvPlanExpiryDate);
            tvPlanExpiryDate.setText(expiryDate);
            tvPlanExpiryDate.setTypeface(regularTypeface);
            expiryDateContainer.setVisibility(View.VISIBLE);
        } else {
            expiryDateContainer.setVisibility(View.GONE);
        }
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
        DisableOrEnableBuyBtn();
        return view;
    }
    private void onUpiAppSelected(UPIAppModel app) {
        // Called when user selects an app. Do whatever you need (save selection, start payment intent, etc.)
        //Toast.makeText(getActivity(), "Selected: " + app.getName() + " (" + app.getPackageName() + ")", Toast.LENGTH_SHORT).show();
        tvSelectedAppName.setText(app.getName());
        ivSelectedAppIcon.setImageResource(app.getIconResId());

    }

    /**
     * Sets up the buy button click listener with appropriate analytics tracking.
     * Handles different scenarios:
     * - Free Dhruv plan selection
     * - Regular plan purchase
     * - User login state
     */
    private void setUpBuyBtnListener() {
        buyTV.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_DHRUV_FROM_TAB, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            if (isFreeDhruvPlanShow) {
                openFreeDhruvPersonalDetailsDialog(true, true);
            } else {
                /*if (((PurchasePlanHomeActivity) getActivity()).enableMonthlySubscriptionValue) {
                    openPlanSelectDialog();
                } else {
                    // 1 for yearly plan subscription
                    getPlatinumPlan(1);
                }*/
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
                buyTV.performClick();
            }
        });
    }

    /**
     * Populates the RecyclerView with premium plan features.
     * Creates CloudPlanData objects for each feature including:
     * - AI model features
     * - Premium kundli features
     * - Personalization options
     * - Chat features
     * - Discount benefits
     * - Ad-free experience
     * - Cloud storage features
     */
    private void setPremiumDataInList() {
        cloudPlanDataArrayList = new ArrayList<>();
        HashMap<String, Integer> icons = ((PurchasePlanHomeActivity) getActivity()).getIcons();

        CloudPlanData cloudPlanData;
        cloudPlanData = new CloudPlanData();
        cloudPlanData.setHeading(getString(R.string.our_most_advanced_AI_model));
        cloudPlanData.setDesc(getString(R.string.be_the_first_to_use_our_most_advanced_AI_models));

        cloudPlanData.setImageID(icons.get("ai_astrologer"));
        cloudPlanDataArrayList.add(cloudPlanData);

        CloudPlanData cloudPlanData1;
        cloudPlanData1 = new CloudPlanData();
        cloudPlanData1.setHeading(getString(R.string.hun_premium_kundli_worth_rs50000));
        cloudPlanData1.setDesc(getString(R.string.hund_premium_kundli_every_month_desc));
        cloudPlanData1.setImageID(icons.get("plan_download_icon"));
        cloudPlanDataArrayList.add(cloudPlanData1);


        CloudPlanData cloudPlanData2;
        cloudPlanData2 = new CloudPlanData();
        cloudPlanData2.setHeading(getString(R.string.your_name_adress_on_every_kundli));
        cloudPlanData2.setDesc(getString(R.string.your_name_adress_on_every_kundli_desc));
        cloudPlanData2.setImageID(icons.get("plan_name_address_icon"));
        cloudPlanDataArrayList.add(cloudPlanData2);


//        CloudPlanData cloudPlanData3;
//        cloudPlanData3 = new CloudPlanData();
//        cloudPlanData3.setHeading(getString(R.string.unlock_all_premium_reports));
//        cloudPlanData3.setDesc(getString(R.string.unlock_all_premium_reports_desc));
//        cloudPlanData3.setImageID(icons.get("plan_kundli_icon"));
//        cloudPlanDataArrayList.add(cloudPlanData3);


//        CloudPlanData cloudPlanData4;
//        cloudPlanData4 = new CloudPlanData();
//        cloudPlanData4.setHeading(getString(R.string.num_ques_a_day_txt,questionLimit));
//        cloudPlanData4.setDesc(getString(R.string.dhruv_plan_question_limit_description));//getString(com.ojassoft.astrosage.varta.utils.CUtils.getAIPassStringFromConfig(getContext(),true)
//        cloudPlanData4.setImageID(icons.get("plan_mobile_icon"));
//        cloudPlanDataArrayList.add(cloudPlanData4);

        CloudPlanData cloudPlanData4;
        cloudPlanData4 = new CloudPlanData();
        cloudPlanData4.setHeading(getString(R.string.more_kundli_ai_ques_day_than_plus,"5x"));
        cloudPlanData4.setDesc(getString(R.string.dhruv_plan_question_limit_description));//getString(com.ojassoft.astrosage.varta.utils.CUtils.getAIPassStringFromConfig(getContext(),true)
        cloudPlanData4.setImageID(icons.get("plan_mobile_icon"));
        cloudPlanDataArrayList.add(cloudPlanData4);

	        CloudPlanData cloudPlanData8;
	        String showAIPassPlan = CUtils.getStringData(requireContext(), CGlobalVariables.SHOW_AI_PASS_PLAN_KEY, "0");
	        if ("1".equals(showAIPassPlan)) {
	            cloudPlanData8 = new CloudPlanData();
	            // cloudPlanData8.setHeading(getString(R.string._100_ques_day_to_ai_astrologers,questionLimit));
	            cloudPlanData8.setHeading(getString(R.string.ask_up_to_100_question_a_day_with_ai_astrologer, "100"));
	            cloudPlanData8.setDesc(getString(R.string.get_quick_precise_and_highly_personalized_answer_with_ai_astrologers)); //getString(com.ojassoft.astrosage.varta.utils.CUtils.getAIPassStringFromConfig(getContext(),true)
	            cloudPlanData8.setImageID(icons.get("plan_mobile_icon"));
	            cloudPlanDataArrayList.add(cloudPlanData8);
	        }

        CloudPlanData cloudPlanData5;
        cloudPlanData5 = new CloudPlanData();
        cloudPlanData5.setHeading(getString(R.string.discount_on_everything));
        cloudPlanData5.setDesc(getString(R.string.discount_on_everything_desc));
        cloudPlanData5.setImageID(icons.get("plan_discount_icon"));
        cloudPlanDataArrayList.add(cloudPlanData5);


        CloudPlanData cloudPlanData6;
        cloudPlanData6 = new CloudPlanData();
        cloudPlanData6.setHeading(getString(R.string.no_interruptions));
        cloudPlanData6.setDesc(getString(R.string.no_interruptions_desc));

        cloudPlanData6.setImageID(icons.get("plan_free_ads_icon"));
        cloudPlanDataArrayList.add(cloudPlanData6);


        CloudPlanData cloudPlanData7;
        cloudPlanData7 = new CloudPlanData();
        cloudPlanData7.setHeading(getString(R.string.save_unlimited_charts_kundali_on_cloud));
        cloudPlanData7.setDesc(getString(R.string.save_unlimited_charts_kundali_on_cloud_desc));
        cloudPlanData7.setImageID(icons.get("plan_cloud_icon"));
        cloudPlanDataArrayList.add(cloudPlanData7);


    }

    /**
     * Enables or disables the buy button based on the user's current plan status.
     * If user already has a Dhruv plan, the button is disabled.
     */
    private void DisableOrEnableBuyBtn() {

        if (CUtils.isDhruvPlan(requireContext())) {
            buyTV.setEnabled(false);
            buyDisable.setVisibility(View.VISIBLE);
            phonePePaymentLayout.setVisibility(View.GONE);
        }
    }

    String questionLimit;
    /**
     * Formats and displays the subscription price for Razorpay payment integration.
     * 
     * Flow:
     * 1. Retrieves cached premium plan service details
     * 2. Parses JSON data to get pricing information
     * 3. Formats price based on user's country:
     *    - For Indian users:
     *      * Uses Tamil rupee symbol (ரூ.) for Tamil language
     *      * Uses standard rupee symbol (₹) for other languages
     *    - For international users:
     *      * Uses dollar symbol ($)
     * 4. Updates the buy button text with formatted price
     * 
     * Note: Uses cached service details to avoid network calls.
     * Handles exceptions silently to prevent UI disruption.
     */
    private void showProductsDetailPriceRazorpay() {
        try {
            if (!TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getPremiumPlanServiceDetail())) {
                // Toast.makeText(this, "Called from this", Toast.LENGTH_SHORT).show();
                String planObj = com.ojassoft.astrosage.varta.utils.CUtils.getPremiumPlanServiceDetail();
                //TODO hit service to get ServiceDetail from server
              //  Log.e("pdf_check", "showProductsDetailPriceRazorpay: "+planObj );
                JSONObject obj = new JSONObject(planObj);
                String rupeesInTamil = "ரூ.";
                String planPrice = "";
                String platinumPlanPriceMonth;
                if (com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity).equals(COUNTRY_CODE_IND)) {
                    planPrice = obj.getString("PriceInRS");

                if (LANGUAGE_CODE == 2) {
                    platinumPlanPriceMonth = rupeesInTamil + planPrice;
                } else {
                    platinumPlanPriceMonth = "\u20B9" + planPrice;
                }
                }else{
                    planPrice = obj.getString("OriginalPriceInDollor");
                    platinumPlanPriceMonth = "$" + planPrice;
                }
                questionLimit = obj.getString("QuestionLimit");

                buyTV.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
                btnStartSubsPayment.setText(getResources().getString(R.string.button_text_with_price, platinumPlanPriceMonth));
            }

        } catch (Exception e) {

        }


    }

    /**
     * Displays product details and pricing for Google Play payment integration.
     * Handles different currency formats based on language settings.
     */
    private void showProductsDetailPriceGoogle() {

        try {
            String rupeesInTamil = "ரூ.";
            String planPrice = "";
            String platinumPlanPriceMonth;
            if (!((PurchasePlanHomeActivity) getActivity()).arayPlatinumPlanMonth.isEmpty()) {

                planPrice = ((PurchasePlanHomeActivity) getActivity()).arayPlatinumPlanMonth.get(2).replace(".00", "");
                if (LANGUAGE_CODE == 1) {
                    platinumPlanPriceMonth = (planPrice.replace("Rs.", "\u20B9"));

                } else if (LANGUAGE_CODE == 2) {

                    platinumPlanPriceMonth = (planPrice.replace("Rs.", rupeesInTamil));
                } else {
                    platinumPlanPriceMonth = (planPrice);
                }
                buyTV.setText(getResources().getString(R.string.subscribe_for_text, platinumPlanPriceMonth));
            }
        } catch (Exception e) {
            //
        }


    }

    /**
     * Opens the plan selection dialog for choosing between monthly and yearly plans.
     * Manages fragment transactions and dialog display.
     */
    public void openPlanSelectDialog() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("HOME_PLANACTIVATOR");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        ChoosePlanFragmentDailogPlatinum choosePlanFragmentDailog = new ChoosePlanFragmentDailogPlatinum();
        choosePlanFragmentDailog.show(fragmentManager, "HOME_PLANACTIVATOR");
        fragmentTransaction.commit();
    }

    /**
     * Opens the personal details dialog for free Dhruv plan users.
     * 
     * @param isEmailIdVisible Whether to show email field
     * @param isPhnNumbVisisble Whether to show phone number field
     */
    public void openFreeDhruvPersonalDetailsDialog(boolean isEmailIdVisible, boolean isPhnNumbVisisble) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("FRAG_PERSONAL_DETAILS");
        if (prev != null) {
            ft.remove(prev);
        }

        Bundle bundle = new Bundle();
        //bundle.putInt("planIndex", planIndex);
        bundle.putBoolean("isEmailIdVisible", isEmailIdVisible);
        bundle.putBoolean("isPhnNumbVisisble", isPhnNumbVisisble);

        //FragPersonalDetails clfd = new FragPersonalDetails();
        FragPersonalDetailsOMF clfd = new FragPersonalDetailsOMF();
        clfd.setArguments(bundle);
        clfd.show(fm, "FRAG_PERSONAL_DETAILS");
        ft.commit();
    }
    int MONTHLY_PLAN_TYPE = 0 , YEARLY_PLAN_TYPE =1;

    /**
     * Handles the platinum plan selection based on plan type.
     * 
     * @param planType The type of plan (MONTHLY_PLAN_TYPE or YEARLY_PLAN_TYPE)
     */
    public void getPlatinumPlan(int planType) {
        if (planType == MONTHLY_PLAN_TYPE) {
            CUtils.SavePlanForPopupPreference(getActivity(), true);
            if (isFreeDhruvPlanShow) {
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN_MONTHLY_OMF,selectedUPIAppModel);
            } else {
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN_MONTHLY,selectedUPIAppModel);
            }
        } else if (planType == YEARLY_PLAN_TYPE) {
            CUtils.SavePlanForPopupPreference(getActivity(), true);
            if (isFreeDhruvPlanShow) {
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN_YEARLY_OMF,null);
            } else {
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN,null);
            }
        }
    }


    /**
     * Handles the resume state of the fragment.
     * Checks login status and triggers appropriate actions if login was performed.
     */
    @Override
    public void onResume() {
        super.onResume();
        //if user login at another Fragment then this will diable btns if user has plans and need to execute only once after login
        if(getActivity() instanceof PurchasePlanHomeActivity && ((PurchasePlanHomeActivity) getActivity()).isPremiumUserAfterLogin){
            buyTV.setEnabled(false);
            buyDisable.setVisibility(View.VISIBLE);
            ((PurchasePlanHomeActivity) getActivity()).isPremiumUserAfterLogin = false;
        }
        if (isLoginPerformed) {
            isLoginPerformed = false;
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(getActivity());
            if (isLogin) {
                if(CUtils.isDhruvPlan(getContext())) {
                    ((PurchasePlanHomeActivity) getActivity()).isPremiumUserAfterLogin = true;
                    buyTV.setEnabled(false);
                    buyDisable.setVisibility(View.VISIBLE);
                }else{
                    buyTV.performClick();
                }

            }
        }
    }

    /**
     * Opens the plan selection interface based on subscription availability.
     * Either shows plan selection dialog or directly selects yearly plan.
     */
    public void openSelectPlan() {

        if (((PurchasePlanHomeActivity) getActivity()).enableMonthlySubscriptionValue) {
            openPlanSelectDialog();
        } else {
            // 1 for yearly plan subscription
            getPlatinumPlan(1);
        }
    }

}
