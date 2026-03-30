package com.ojassoft.astrosage.varta.dialog;


import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CLOUD_CHAT_LIMIT_UPGRADE_PLAN_PID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_CONSULTAION_PASS_SESSION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PID_INSUFFICIENT_BALANCE_DIALOG_RECHARGE_BUTTON;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchaseAiConsultationPlan;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * LowBalanceSubscribeAiAstroDialog
 * 
 * Requirements:
 * 1. This dialog is displayed when a user attempts to connect with an AI astrologer but has insufficient balance
 * 2. The user must have a balance less than the minimum required amount set by the server
 * 3. The dialog provides two options to the user:
 *    a) Recharge their account to continue with the current AI astrologer
 *    b) Subscribe to a monthly plan that allows unlimited chats with any AI astrologer
 * 4. The dialog should display relevant information like astrologer name, service price, and minimum balance required
 * 5. Firebase analytics should track user interactions with dialog options
 *
 * Uses:
 * 1. Presented when a user with low balance tries to connect to an AI astrologer chat
 * 2. Provides seamless navigation to either wallet recharge screen or subscription plan purchase
 * 3. Displays dynamic pricing information based on user's country and currency
 * 4. Helps convert free or low-balance users to paid subscribers
 * 5. Collects analytics data for user behavior analysis
 */
public class LowBalanceSubscribeAiAstroDialog extends DialogFragment {
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    String currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_INDIA;
    private TextView astrologer_name_txt, txtHead, txtSubHead, astrologer_price, minBalanceRequired, txtUnlimitedAiChat, txtViewAllAiAstrologer, txtviewMonthlyPlan;
    private LinearLayout llRecharge, llSubscribe;
    private Activity activity;
    private Context context;
    private CardView main_not_enough_balance_layout;
    private CustomProgressDialog pd;
    private String minReqBalance, astrologerName, servicePrice, servicePriceInRs, imageUrl,serviceInDoller;
    private ImageView iv_close,ri_profile_img;

    /**
     * Constructor initializes the dialog with required information about the astrologer and pricing
     * 
     * @param minReqBalance Minimum balance required for chat connection
     * @param astrologerName Name of the AI astrologer
     * @param servicePrice Price of the service
     */
    public LowBalanceSubscribeAiAstroDialog(String minReqBalance, String astrologerName, String servicePrice,String imageUrl) {
        this.minReqBalance = minReqBalance;
        this.astrologerName = astrologerName;
        this.servicePrice = servicePrice;
        this.imageUrl = imageUrl;
    }

    public LowBalanceSubscribeAiAstroDialog() {
        //
    }

    /**
     * Creates and returns the view hierarchy associated with the dialog
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.not_enough_balance_layout, container, false);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(getContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        activity = getActivity();
        context = getContext();
        if(getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().setCancelable(false);
        }


        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_DIALOG_INUFFICIENT_SUBCRI_OPEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

        inti(view);
        return view;
    }

    /**
     * Initialize all UI components and set up the dialog
     * 
     * @param view Root view of the dialog
     */
    private void inti(View view) {
        // Initialize UI components
        main_not_enough_balance_layout = view.findViewById(R.id.main_not_enough_balance_layout);
        ri_profile_img = view.findViewById(R.id.ri_profile_img);
        astrologer_name_txt = view.findViewById(R.id.astrologer_name_txt);
        astrologer_price = view.findViewById(R.id.astrologer_price);
        minBalanceRequired = view.findViewById(R.id.minBalanceRequired);
        txtUnlimitedAiChat = view.findViewById(R.id.txtUnlimitedAiChat);
        txtViewAllAiAstrologer = view.findViewById(R.id.txtViewAllAiAstrologer);
        txtviewMonthlyPlan = view.findViewById(R.id.txtviewMonthlyPlan);
        iv_close = view.findViewById(R.id.iv_close);
        llRecharge = view.findViewById(R.id.llRecharge);
        llSubscribe = view.findViewById(R.id.llSubscribe);
        //txtSubHead = view.findViewById(R.id.txtSubHead);
        txtHead = view.findViewById(R.id.txtHead);
        
        // Apply custom fonts to all text views
        FontUtils.changeFont(activity, txtHead, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, astrologer_name_txt, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
//        FontUtils.changeFont(context, txtSubHead, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, astrologer_price, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, minBalanceRequired, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, txtUnlimitedAiChat, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, txtViewAllAiAstrologer, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, txtviewMonthlyPlan, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        // Set text values for all fields
        setViewValues();
        
        // Set click listeners for buttons
        setOnClickListeners();
        
        // Load service plan details either from cache or from server
        if (!TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getAiPassPlanServiceDetail())) {
            String ojb = com.ojassoft.astrosage.varta.utils.CUtils.getAiPassPlanServiceDetail();
            getServiceDetails(ojb);
        } else {
            getUserRechargeAmount();
        }
    }

    /**
     * Set up click listeners for all interactive elements in the dialog
     */
    private void setOnClickListeners() {
        // Close button click listener
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Track close button click with Firebase analytics
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_DIALOG_CLOSE_BTN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dismiss();
            }
        });
        
        // Recharge button click listener
        llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Track recharge button click with Firebase analytics
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_DIALOG_SUBSCRIBE_RECHARGE_BTN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.PID_INSUFFICIENT_BALANCE_DIALOG_SUBSCRIBE_BUTTON);
                try {
                    activity = requireActivity();
                    if (activity != null) {
                        // Open wallet screen based on which activity is currently hosting the dialog
                        if (activity instanceof AstrologerDescriptionActivity) {
                            ((AstrologerDescriptionActivity) activity).openWalletScreen(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE);
                        } else if (activity instanceof DashBoardActivity) {
                            ((DashBoardActivity) activity).openWalletScreen(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE);
                        } else if (activity instanceof ActAppModule) {
                            ((ActAppModule) activity).openWalletScreen(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE);
                        } else if (activity instanceof ChatWindowActivity) {
                            ((ChatWindowActivity) activity).openWalletScreen(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE);
                        } else if (activity instanceof AIChatWindowActivity) {
                            ((AIChatWindowActivity) activity).openWalletScreen(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE);
                        } else {
                            ((BaseActivity) activity).openWalletScreenDashboard(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE);
                        }
                    }
                } catch (Exception e) {
                    try {
                        com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(requireActivity().findViewById(android.R.id.content), e.getMessage(), requireContext());
                    } catch (Exception e2) {
                        // Ignore secondary exceptions
                    }
                }
                dismiss();
            }
        });
        
        // Subscribe button click listener
        llSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Track subscribe button click with Firebase analytics
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_DIALOG_INUFFICIENT_SUBCRI_OPEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.utils.CUtils.createSession(activity, AI_CONSULTAION_PASS_SESSION_ID);

                Intent intent = new Intent(activity, PurchaseAiConsultationPlan.class);
                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_PARAMS, "LowBalanceSubscribeAiAstroDialog");
                activity.startActivity(intent);
                dismiss();
            }
        });
    }

    /**
     * Set text values for the dialog components based on constructor parameters
     */
    private void setViewValues() {
        try {
            imageUrl = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + imageUrl;
            //holder.roundImage.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(context.getApplicationContext()).load(imageUrl).placeholder(R.drawable.ic_profile_view).error(R.drawable.ic_profile_view).circleCrop().into(ri_profile_img);

            // Set astrologer name
            // Replace '+' with space in astrologerName if it contains '+'
            if (astrologerName.contains("+")) {
                astrologerName = astrologerName.replace("+", " ");
            }
            astrologer_name_txt.setText(astrologerName);

            // Set service price with appropriate formatting
            String quePrice = context.getResources().getString(R.string.question_price).replace("$", servicePrice);
            astrologer_price.setText("₹" + quePrice);

            // Set minimum balance required message
            String minBalanceReq = activity.getResources().getString(R.string.min_balance_required_for_5min).replace("####", minReqBalance);
            minBalanceRequired.setText(minBalanceReq);
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Configure dialog layout parameters when it's displayed
     */
    @Override
    public void onStart() {
        super.onStart();
        // Make the dialog full-width
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * Shows a custom progress dialog if not already showing.
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(context);
            }
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Hides the progress dialog if it's showing
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) pd.dismiss();
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Sets up the subscription plan price display based on user's locale
     */
    private void showProductsDetailPriceRazorpay() {
        String rupeesInTamil = "ரூ.";
        String planPrice = "", planPriceNew;
        planPrice = servicePriceInRs;
        
        // Apply appropriate currency symbol based on language
        if (LANGUAGE_CODE == 2) {
            planPriceNew = rupeesInTamil + planPrice;
        } else {
            planPriceNew = "\u20B9" + planPrice;
        }
        txtviewMonthlyPlan.setText(getResources().getString(R.string.ai_pass_price, planPriceNew));
    }

    /**
     * Fetch AI pass details from server
     */
    public void getUserRechargeAmount() {
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getAiPassDetails(getUserRechargeParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject obj = new JSONObject(myResponse);
                        String status = obj.getString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS);
                        if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS_SUCESS)) {
                            // Cache response for future use
                            com.ojassoft.astrosage.varta.utils.CUtils.setAiPassPlanServiceDetail(myResponse);
                            getServiceDetails(myResponse);
                        } else {
                            CUtils.showSnackbar(main_not_enough_balance_layout, activity.getResources().getString(R.string.something_went_wrong) + "(" + status + ")", activity);
                        }
                    } catch (Exception e) {
                        // Ignore exceptions
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    /**
     * Prepare parameters for API request
     * 
     * @return Map containing request parameters
     */
    private Map<String, String> getUserRechargeParams() {
        boolean firstvisitstatusValue = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITSTATUS,false);
        String  firstvisitdatetimeValue = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME,"");

        Map<String, String> map = new HashMap<>();
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(activity));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(activity));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_USER_ID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(activity));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITSTATUS, firstvisitstatusValue+"");
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME, firstvisitdatetimeValue);
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(map);
    }

    /**
     * Parse and process AI service details from JSON response
     * 
     * @param JObject JSON response containing service details
     */
    private void getServiceDetails(String JObject) {
        try {
            JSONObject obj = new JSONObject(JObject);
            serviceInDoller = obj.getString("rateindollar");
            servicePriceInRs = obj.getString("rateinrs");
            String  firstvisitdatetimeValue = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME,"");
            if(TextUtils.isEmpty(firstvisitdatetimeValue)){
                com.ojassoft.astrosage.varta.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITDATETIME,obj.optString("firstvisittimemillis"));
                com.ojassoft.astrosage.varta.utils.CUtils.saveBooleanData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIRSTVISITSTATUS,true);
            }
            // Set currency based on user's country
            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context).equals(COUNTRY_CODE_IND)) {
                currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
            }
            showProductsDetailPriceRazorpay();
        } catch (Exception e) {
            // Ignore exceptions
        }
    }
}
