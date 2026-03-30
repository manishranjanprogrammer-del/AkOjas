package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.WalletRechargeSuggestionAdapter;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.OnItemClickListener;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A BottomSheetDialogFragment that suggests recharge amounts to the user
 * when their balance is insufficient for a service.
 */
public class RechargeSuggestionBottomSheet extends BottomSheetDialogFragment {

    public static final String TITLE = "RechargeSuggestionBottomSheet";
    private static RechargeSuggestionBottomSheet mInstance;
    int selectedPosition = 4;
    ArrayList<WalletAmountBean.Services> servicesArrayList;
    ArrayList<WalletAmountBean.Services> masterservicesArrayList;
    WalletRechargeSuggestionAdapter walletRecyclerViewAdapter;
    int mSelectedPosition;
    double intMinBalanceNeededText = 0.0;
    double rechargeNeededAmount = 0.0;
    String minBalanceNeededText = "0", userbalance = "0", astroName = "",callingActivity="",bottomSheetDialogUserFor="";
    private int maxRechargeServicesCount = 7, selectedRechargeServicesPosition = 1,minInternationalRecharge=100;
    private Context context;
    private Activity activity;
    private WalletAmountBean walletAmountBean;
    private WalletAmountBean masterWalletAmountBean;
    private View mView;
    private RecyclerView recyclerView;
    private TextView tvBFQRHeading, txtMinBalanceNeeded;
    private ImageView ivBFQRClose;
    private CustomProgressDialog pd;
    private Button btnProceedRecharge;
    private boolean showSuggestedAmount;
    private ProgressBar pbLoading;

    public RechargeSuggestionBottomSheet() {
        // Required empty public constructor
    }

    /**
     * Returns a singleton instance of RechargeSuggestionBottomSheet.
     *
     * @return The singleton instance.
     */
    public static RechargeSuggestionBottomSheet getInstance() {
        if (mInstance == null) mInstance = new RechargeSuggestionBottomSheet();
        return mInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_recharge_suggestion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initViews(mView);
        initListeners();
        if (getArguments() != null) {
            checkCachedData(); // This triggers parseResponse() or API call
        }
    }

    /**
     * Initializes the views in the layout.
     *
     * @param view The root view of the fragment.
     */
    private void initViews(View view) {
        tvBFQRHeading = view.findViewById(R.id.tvBFQRHeading);
        ivBFQRClose = view.findViewById(R.id.ivBFQRClose);
        txtMinBalanceNeeded = view.findViewById(R.id.txtMinBalanceNeeded);
        recyclerView = view.findViewById(R.id.rvBFQRAmounts);
        btnProceedRecharge = view.findViewById(R.id.btnProceedRecharge);
        pbLoading  = view.findViewById(R.id.pbLoading);
        if (getArguments() != null) {
            minBalanceNeededText = getArguments().getString("minBalanceNeededText");
            astroName = getArguments().getString("astroName");
            userbalance = getArguments().getString("userbalance");
            callingActivity = getArguments().getString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY);
            bottomSheetDialogUserFor = getArguments().getString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR);
            if (TextUtils.isEmpty(userbalance)) {
                userbalance = "0";
            }
            if (TextUtils.isEmpty(minBalanceNeededText)) {
                minBalanceNeededText = "0";
            }
            intMinBalanceNeededText = Double.parseDouble(minBalanceNeededText);
            rechargeNeededAmount = intMinBalanceNeededText - Double.parseDouble(userbalance);

        }
        if (astroName.contains("+")) {
            astroName = astroName.replace("+", " ");
        }
        String minBalText = context.getString(R.string.five_minutes)+" (₹" + minBalanceNeededText+")";
        FontUtils.changeFont(context, txtMinBalanceNeeded, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);


        String template = context.getString(R.string.minimum_balance_required_to_talk_with_new);

        // replace placeholders with real values
        String finalText = template.replace("#####", astroName)
                .replace("@@@@", minBalText);

        SpannableStringBuilder ssb = new SpannableStringBuilder(finalText);

        // Bold Astro Name
        int nameStart = finalText.indexOf(minBalText);
        if (nameStart != -1) {
            int nameEnd = nameStart + minBalText.length();
            ssb.setSpan(new ForegroundColorSpan(Color.RED),
                    nameStart, nameEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Bold amountText
        String amountText = "₹" + minBalanceNeededText;
        int amountStart = finalText.lastIndexOf(minBalText);
        if (amountStart != -1) {
            int amountEnd = amountStart + minBalText.length();
//            ssb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary_day_night)
//                    ),
//                    amountStart, amountEnd,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new StyleSpan(Typeface.BOLD),
                    amountStart, amountEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        txtMinBalanceNeeded.setText(ssb);

        FontUtils.changeFont(context, tvBFQRHeading, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        String amount = " ₹" + userbalance;
        String fullText = getString(R.string.insufficient_wallet_balance_, amount);

        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf(amount);
        int end = start + amount.length();

        spannable.setSpan(new ForegroundColorSpan(Color.RED),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvBFQRHeading.setText(spannable);
    }

    /**
     * Initializes the click listeners for the views.
     */
    private void initListeners() {
        ivBFQRClose.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_INSUFFICIENT_BALANCE_DIALOG_CANCEL_BUTTON,
                    FIREBASE_EVENT_ITEM_CLICK, "");
            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
            try {
                if (context instanceof ChatWindowActivity) {
                    ((ChatWindowActivity) context).cancelRechargeAfterChat();
                } else if (context instanceof AIChatWindowActivity) {
                    ((AIChatWindowActivity) context).cancelRechargeAfterChat();
                } else if (context instanceof AIVoiceCallingActivity) {
                    ((AIVoiceCallingActivity) context).cancelRechargeAfterChat();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismiss();
        });

        btnProceedRecharge.setOnClickListener(v -> {
            if (walletRecyclerViewAdapter != null) {
                WalletAmountBean.Services selectedItem = walletRecyclerViewAdapter.getSelectedItem();
                if (selectedItem == null) {
                    Toast.makeText(context, "Please select recharge amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedItem.getServiceid().equals(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT)) {
                    mSelectedPosition = -1;
                    paymentInfoActivity(selectedItem);
                } else {
                    for (int i = 0; i < masterservicesArrayList.size(); i++) {
                        WalletAmountBean.Services item = masterservicesArrayList.get(i);
                        if (item.getServiceid().equals(selectedItem.getServiceid())) {
                            mSelectedPosition = i;
                            paymentInfoActivity(selectedItem);
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * Checks for cached recharge data. If available, it parses the cached data.
     * Otherwise, it makes an API call to get the recharge amounts.
     */
    private void checkCachedData() {
        try {
            walletAmountBean = new WalletAmountBean();
            masterWalletAmountBean = new WalletAmountBean();
            servicesArrayList = new ArrayList<>();
            int intRechargeAmount = 0;
            if (TextUtils.isEmpty(CUtils.getUserIntroOfferType(context))) { //user have no offer
                intRechargeAmount = (int) Double.parseDouble(minBalanceNeededText) - (int) Double.parseDouble(userbalance);
                String minRechargeAmt = com.ojassoft.astrosage.utils.CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.KEY_MIN_RECHARGE_AMOUNT, "00");

                //if suggestion recharge amount is less than minimum recharge amt from server, set it to minimum recharge amt from server
                int minimumRechargeAmount = Integer.parseInt(minRechargeAmt);
                if(intRechargeAmount < minimumRechargeAmount){
                    intRechargeAmount = minimumRechargeAmount;
                }
                showSuggestedAmount = true;
            }

            if (showSuggestedAmount && rechargeNeededAmount <= intRechargeAmount) {
                String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context);
                if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                        servicesArrayList.add(get1stService(intRechargeAmount));
                } else {
                    if (intRechargeAmount >= minInternationalRecharge) {
                            servicesArrayList.add(get1stService(intRechargeAmount));

                    }
                }

            }
            if (!TextUtils.isEmpty(CUtils.getWalletRechargeData())&& CUtils.getMyWalletAmountBean()!=null &&CUtils.getMyMasterWalletAmountBean()!=null ) {
                masterWalletAmountBean = CUtils.getMyMasterWalletAmountBean();
                masterservicesArrayList = CUtils.getMyMasterWalletAmountBean().getServiceList();
                setAdapter(CUtils.getMyWalletAmountBean());
                //parseResponse(CUtils.getWalletRechargeData());
            } else {
                pbLoading.setVisibility(View.VISIBLE);
                getRechargeAmounts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a suggested recharge service based on the required amount.
     *
     * @param intRechargeAmount The suggested recharge amount.
     * @return A {@link WalletAmountBean.Services} object representing the suggested recharge.
     */
    private WalletAmountBean.Services get1stService(int intRechargeAmount) {
        WalletAmountBean.Services services = walletAmountBean.new Services();
        services.setServiceid(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT);
        services.setServicename("Recharge " + intRechargeAmount);
        services.setSmalliconfile("");
        services.setCategoryid("");
        services.setRate("");
        services.setRaters(intRechargeAmount + ".0");
        services.setActualrate("");
        services.setActualraters(intRechargeAmount + ".0");
        services.setPaymentamount("0");
        services.setOffermessage("");
        services.setOfferAmount("0");
        return services;
    }

    /**
     * Creates a "More Recharges" service item.
     *
     * @return A {@link WalletAmountBean.Services} object for "More Recharges".
     */
    private WalletAmountBean.Services getMoreRechargeServices() {
        WalletAmountBean.Services services = walletAmountBean.new Services();
        services.setServiceid(CGlobalVariables.SERVICE_ID_MORE_RECHARGES);
        services.setServicename(context.getResources().getString(R.string.more_recharge));
        services.setSmalliconfile("");
        services.setCategoryid("");
        services.setRate("");
        services.setRaters("00.0");
        services.setActualrate("");
        services.setActualraters("00.0");
        services.setPaymentamount("00.0");
        services.setOffermessage("");
        services.setOfferAmount("0");
        return services;
    }


    /**
     * Navigates to the payment information activity.
     *
     * @param selectedItem The selected recharge service.
     */
    public void paymentInfoActivity(WalletAmountBean.Services selectedItem) {
        if (bottomSheetDialogUserFor.equals(com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT)) {
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE, FIREBASE_EVENT_ITEM_CLICK, "");
            if (mSelectedPosition == -1) {
                if(context instanceof AIChatWindowActivity){
                    ((AIChatWindowActivity) context).gotoPaymentInfoActivityVariableAmount(mSelectedPosition, masterWalletAmountBean,selectedItem.getRaters());
                }else if(context instanceof ChatWindowActivity){
                    ((ChatWindowActivity) context).gotoPaymentInfoActivityVaribaleInfo(mSelectedPosition, masterWalletAmountBean,selectedItem.getRaters());
                }
            }else {
                if(context instanceof AIChatWindowActivity) {
                    ((AIChatWindowActivity) context).gotoPaymentInfoActivity(mSelectedPosition, masterWalletAmountBean);
                }else if(context instanceof ChatWindowActivity){
                    ((ChatWindowActivity) context).gotoPaymentInfoActivity(mSelectedPosition, masterWalletAmountBean);
                }
            }
            return;
        }
        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.PID_INSUFFICIANT_BALANCE_RECHARGE_CLICK);
        CUtils.addFcmAnalyticsEvents(context);
        Bundle bundle = new Bundle();
        if (mSelectedPosition == -1) {
            bundle.putString(CGlobalVariables.RECHARGE_AMOUNT, selectedItem.getRaters());
            bundle.putString("frompopunder", "1");
        }
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, masterWalletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);

        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, getCallingActivity());

        bundle.putString("screen_open_from", "PopUnderRecharge");
        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
            bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
        }
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        Intent intent = new Intent(context, PaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    /**
     * Returns the name of the calling activity.
     *
     * @return The simple name of the calling activity class.
     */
    public String getCallingActivity() {
        if (context != null) {
            return context.getClass().getSimpleName();
        }
        return "BaseActivity";
    }

    /**
     * Fetches the recharge amounts from the API.
     */
    private void getRechargeAmounts() {
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            CUtils.showSnackbar(mView, getResources().getString(R.string.no_internet), getActivity());
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(getActivity());
            pd.show();
            pd.setCancelable(false);
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi = apiList.getRechargeServices(CUtils.getRechargeParams(getActivity(), CUtils.getUserID(getActivity())));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            parseResponse(myResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
     * Hides the progress bar.
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the JSON response containing the recharge amounts.
     *
     * @param response The JSON response string.
     */
    private void parseResponse(String response) {
        if (response != null && !response.isEmpty()) {
            try {
                boolean isFirstTime= true;
                JSONObject jsonObject = new JSONObject(response);
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");

                JSONArray jsonArray = jsonObject.getJSONArray("services");
                masterservicesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerJsonObject = jsonArray.getJSONObject(i);
                    WalletAmountBean.Services services = walletAmountBean.new Services();
                    services.setServiceid(innerJsonObject.getString("serviceid"));
                    services.setServicename(innerJsonObject.getString("servicename"));
                    services.setSmalliconfile(innerJsonObject.getString("smalliconfile"));
                    services.setCategoryid(innerJsonObject.getString("categoryid"));
                    services.setRate(innerJsonObject.getString("rate"));
                    services.setRaters(innerJsonObject.getString("raters"));
                    services.setActualrate(innerJsonObject.getString("actualrate"));
                    services.setActualraters(innerJsonObject.getString("actualraters"));
                    services.setPaymentamount(innerJsonObject.getString("paymentamount"));
                    services.setOffermessage(innerJsonObject.getString("offermessage"));
                    services.setOfferAmount(innerJsonObject.getString("offeramout"));

                    if (servicesArrayList.size() < maxRechargeServicesCount) {

                        double nextRechargeAmount = Double.parseDouble(services.getRaters());
                        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context);
                        if (rechargeNeededAmount <= nextRechargeAmount) {
                            if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {

                              // if both variable and services has same value
                               if(isFirstTime){
                                   if(servicesArrayList.size()==1){
                                       if(servicesArrayList.get(0).getServiceid().equals(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT)){
                                           double intRaters = Double.parseDouble(services.getRaters());
                                           double intRechargeAmount = Double.parseDouble(servicesArrayList.get(0).getRaters());
                                           if (intRechargeAmount == intRaters) {
                                               servicesArrayList.remove(0);
                                           }
                                       }

                                   }
                                   isFirstTime = false;
                               }
                                if (servicesArrayList.size() == selectedRechargeServicesPosition) {
                                    services.setSelected(true);
                                }
                                servicesArrayList.add(services);
                            } else {
                                if (nextRechargeAmount >= minInternationalRecharge) {
                                    // if both variable and services has same value
                                    if(isFirstTime){
                                        if(servicesArrayList.size()==1){
                                            if(servicesArrayList.get(0).getServiceid().equals(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT)){
                                                double intRaters = Double.parseDouble(services.getRaters());
                                                double intRechargeAmount = Double.parseDouble(servicesArrayList.get(0).getRaters());
                                                if (intRechargeAmount == intRaters) {
                                                    servicesArrayList.remove(0);
                                                }
                                            }

                                        }
                                        isFirstTime = false;
                                    }
                                    if (servicesArrayList.size() == selectedRechargeServicesPosition) {
                                        services.setSelected(true);
                                    }
                                    servicesArrayList.add(services);
                                }
                            }

                        }
                    }
                    masterservicesArrayList.add(services);
                }
                //added for more recharge services
                servicesArrayList.add(getMoreRechargeServices());
                walletAmountBean.setGstrate(gstRate);
                masterWalletAmountBean.setGstrate(gstRate);
                walletAmountBean.setDollorConverstionRate(dollarconversionrate);
                masterWalletAmountBean.setDollorConverstionRate(dollarconversionrate);
                walletAmountBean.setServiceList(servicesArrayList);
                masterWalletAmountBean.setServiceList(masterservicesArrayList);
                setAdapter(walletAmountBean);
                CUtils.saveWalletRechargeData(response);
            } catch (Exception e) {
                Log.d("testException", "e=>>" + e);
                e.printStackTrace();
            }
        } else {
            CUtils.showSnackbar(mView.findViewById(android.R.id.content), getResources().getString(R.string.server_error), context);
        }
    }

    /**
     * Sets the adapter for the RecyclerView.
     *
     * @param walletAmountBean The wallet amount data.
     */
    private void setAdapter(WalletAmountBean walletAmountBean) {
    walletRecyclerViewAdapter = new WalletRechargeSuggestionAdapter(context, walletAmountBean, new OnItemClickListener() {
            @Override
            public void onRechargeServicesClick() {
                btnProceedRecharge.performClick();
            }
        });
        RecyclerView.LayoutManager manager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(walletRecyclerViewAdapter);

        int pos = -1;
        ArrayList<WalletAmountBean.Services> list = walletAmountBean.getServiceList();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getActualraters().equalsIgnoreCase("1.0")) {
                    pos = i;
                    break;
                }
            }
            if (pos != -1) {
                selectedPosition = pos;
            }
        }
        pbLoading.setVisibility(View.GONE);

    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.d("quickRecharge", "showMethod=" + e);
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("quick_recharge_bottom_sheet_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }
}
