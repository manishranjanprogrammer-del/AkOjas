package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.PopUnderWalletRecyclerViewAdapter;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.MiniPaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickRechargeBottomSheet extends BottomSheetDialogFragment {

    public static final String TITLE = "QuickRechargeBottomSheet";
    private static QuickRechargeBottomSheet mInstance;
    int selectedPosition = 4;
    private Context context;
    private String mResponse, urlText, channelId;
    private WalletAmountBean walletAmountBean;
    private View mView;
    private RecyclerView recyclerView;
    private TextView tvBFQRHeading, txtMinBalanceNeeded;
    private ImageView ivBFQRClose;
    private String bottomServiceListUsedFor = "",callingActivity="";
    private CustomProgressDialog pd;

    public QuickRechargeBottomSheet() {

    }

    public static QuickRechargeBottomSheet getInstance() {
        if (mInstance == null) mInstance = new QuickRechargeBottomSheet();
        return mInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout first
        View view = inflater.inflate(R.layout.bottomsheet_fragment_quick_recharge, container, false);
        return view; // Important — return the inflated view
    }

    /**
     * Check and load cache data
     */
    private void checkCachedData() {
        if (!TextUtils.isEmpty(CUtils.getWalletRechargeData())) {
            parseResponse(CUtils.getWalletRechargeData());
            //Log.d("testServices","Called is Cached");
        } else {
            //Log.d("testServices","Called is not Cached");
            getRechargeAmounts(CUtils.getUserID(getActivity()));
        }
        /*Calendar calendar = Calendar.getInstance();
        String currentDate = String.valueOf(calendar.get(Calendar.DATE));
        String savedDate = CUtils.getStringData(WalletActivity.this, CGlobalVariables.RECHARGE_SERVICES_DATE, "");
        if (!currentDate.equals(savedDate)) {
            getRechargeAmounts(CUtils.getUserID(this));
        } else {
            if (!TextUtils.isEmpty(CUtils.getWalletRechargeData())) {
                parseResponse(CUtils.getWalletRechargeData());
            } else {
                getRechargeAmounts(CUtils.getUserID(this));
            }

        }*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initViews(mView);
        initListeners();
        if (getArguments() != null) {
            bottomServiceListUsedFor = getArguments().getString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR);
            checkCachedData(); // This triggers parseResponse() or API call
        }
        //parseResponse(mResponse);
    }

    private void initListeners() {
        ivBFQRClose.setOnClickListener(v -> dismiss());
    }

    private void initViews(View view) {
        tvBFQRHeading = view.findViewById(R.id.tvBFQRHeading);
        ivBFQRClose = view.findViewById(R.id.ivBFQRClose);
        txtMinBalanceNeeded = view.findViewById(R.id.txtMinBalanceNeeded);
        recyclerView = view.findViewById(R.id.rvBFQRAmounts);
        boolean isInsufficientDialogueHide = CUtils.getBooleanData(context, CGlobalVariables.ISINSUFFICIENTDIALOGUEHIDE, false);
        try {
            if (bottomServiceListUsedFor.equals(com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT) && isInsufficientDialogueHide) {
                String minBalanceNeededText = "";
                if (getArguments() != null) {
                    minBalanceNeededText = getArguments().getString("minBalanceNeededText");
                }
                txtMinBalanceNeeded.setText(minBalanceNeededText);
                txtMinBalanceNeeded.setVisibility(View.VISIBLE);
            } else {
                txtMinBalanceNeeded.setVisibility(View.GONE);
            }
        }catch (Exception e){
            //
        }


        FontUtils.changeFont(context, tvBFQRHeading, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, txtMinBalanceNeeded, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
    }

    //api call to get recharge amounts
    private void getRechargeAmounts(String mobileNo) {
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
                            hideProgressBar();
                            String myResponse = response.body().string();
                            mResponse = myResponse;
                            //Log.d("testServices","Called is not Cached===>>"+myResponse);

                            parseResponse(myResponse);
                        } catch (Exception e) {
                            //
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
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseResponse(String response) {
        if (response != null && response.length() > 0) {
            walletAmountBean = new WalletAmountBean();
            try {
                WalletAmountBean.Services services;
                JSONObject jsonObject = new JSONObject(response);
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");

                JSONArray jsonArray = jsonObject.getJSONArray("services");
                JSONObject innerJsonObject;
                ArrayList<WalletAmountBean.Services> servicesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    innerJsonObject = jsonArray.getJSONObject(i);
                    services = walletAmountBean.new Services();
                    String serviceid = innerJsonObject.getString("serviceid");
                    String servicename = innerJsonObject.getString("servicename");
                    String smalliconfile = innerJsonObject.getString("smalliconfile");
                    String categoryid = innerJsonObject.getString("categoryid");
                    String rate = innerJsonObject.getString("rate");
                    String raters = innerJsonObject.getString("raters");
                    String actualrate = innerJsonObject.getString("actualrate");
                    String actualraters = innerJsonObject.getString("actualraters");
                    String paymentamount = innerJsonObject.getString("paymentamount");
                    String offermessage = innerJsonObject.getString("offermessage");
                    String offerAmount = innerJsonObject.getString("offeramout");

                    services.setServiceid(serviceid);
                    services.setServicename(servicename);
                    services.setSmalliconfile(smalliconfile);
                    services.setCategoryid(categoryid);
                    services.setRate(rate);
                    services.setRaters(raters);
                    services.setActualrate(actualrate);
                    services.setActualraters(actualraters);
                    services.setPaymentamount(paymentamount);
                    services.setOffermessage(offermessage);
                    services.setOfferAmount(offerAmount);
                    double paymentAmount = Double.parseDouble(raters);
                    if(paymentAmount <= 20000){
                        servicesArrayList.add(services);
                    }

                }
                walletAmountBean.setGstrate(gstRate);
                walletAmountBean.setDollorConverstionRate(dollarconversionrate);
                walletAmountBean.setServiceList(servicesArrayList);
                setAdapter(walletAmountBean);
            } catch (Exception e) {
                Log.d("testException","e=>>"+e);
                e.printStackTrace();
            }
        } else {
            CUtils.showSnackbar(mView.findViewById(android.R.id.content), getResources().getString(R.string.server_error), context);
        }
    }

    private void setAdapter(WalletAmountBean walletAmountBean) {
        PopUnderWalletRecyclerViewAdapter walletRecyclerViewAdapter = new PopUnderWalletRecyclerViewAdapter(context, walletAmountBean);
        RecyclerView.LayoutManager manager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(walletRecyclerViewAdapter);

        int pos = -1;
        ArrayList<WalletAmountBean.Services> list = walletAmountBean.getServiceList();
        if (list != null && list.size() > 0) {
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
    }

    public void setSelectedPosition(int mSelectedPosition) {
        if (CUtils.isConnectedWithInternet(context)) {
            if (walletAmountBean.getServiceList() == null || walletAmountBean.getServiceList().isEmpty()) {
                CUtils.showSnackbar(mView.findViewById(android.R.id.content), getResources().getString(R.string.server_error), context);
                return;
            }
            if (context instanceof ChatWindowActivity) {
                switch (bottomServiceListUsedFor) {
                    case com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT:
                        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE, FIREBASE_EVENT_ITEM_CLICK, "");
                        ((ChatWindowActivity) context).gotoPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                        break;
                    case com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_AFTER_FREE_CHAT:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CASHBACK_RECHARGE_SERVICE_SELECTION, FIREBASE_EVENT_ITEM_CLICK, "");
                        AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
                        ((ChatWindowActivity) context).gotoMiniPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                        break;
                    case com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CONTINUE_CHAT_RECHARGE_SERVICE_SELECTION, FIREBASE_EVENT_ITEM_CLICK, "");
                        ((ChatWindowActivity) context).gotoMiniPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                        break;
                }
                // ((ChatWindowActivity) context).gotoPaymentInfoActivity(mSelectedPosition, walletAmountBean);
            } else if (context instanceof AIChatWindowActivity) {
                switch (bottomServiceListUsedFor) {
                    case com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT:
                        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE, FIREBASE_EVENT_ITEM_CLICK, "");
                        ((AIChatWindowActivity) context).gotoPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                        break;
                    case com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_AFTER_FREE_CHAT:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CASHBACK_RECHARGE_SERVICE_SELECTION, FIREBASE_EVENT_ITEM_CLICK, "");
                        AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
                        ((AIChatWindowActivity) context).gotoMiniPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                        break;
                    case com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CONTINUE_CHAT_RECHARGE_SERVICE_SELECTION, FIREBASE_EVENT_ITEM_CLICK, "");
                        ((AIChatWindowActivity) context).gotoMiniPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                        break;
                }
            } else if (context instanceof AIVoiceCallingActivity) {
                if (bottomServiceListUsedFor.equals(com.ojassoft.astrosage.utils.CGlobalVariables.EXTEND_CHAT)) {
                    CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CHAT_WINDOW_CHAT_EXTEND_RECHARGE, FIREBASE_EVENT_ITEM_CLICK, "");
                    ((AIVoiceCallingActivity) context).gotoPaymentInfoActivity(mSelectedPosition, walletAmountBean);
                } else {
                    handleActionForAiVoiceCalling(mSelectedPosition);
                }

            }else if (context instanceof DashBoardActivity) {
                CUtils.fcmAnalyticsEvents("astro_list_chat_recharge_service_selection", FIREBASE_EVENT_ITEM_CLICK, "");
                ((DashBoardActivity) context).gotoMiniPaymentInfoActivity(mSelectedPosition, walletAmountBean);
            }else if (context instanceof AstrologerDescriptionActivity) {
                CUtils.fcmAnalyticsEvents("astro_list_chat_recharge_service_selection", FIREBASE_EVENT_ITEM_CLICK, "");
                ((AstrologerDescriptionActivity) context).gotoMiniPaymentInfoActivity(mSelectedPosition, walletAmountBean);
            }
        } else {
            CUtils.showSnackbar(mView.findViewById(android.R.id.content), getResources().getString(R.string.no_internet), context);
        }
    }

    private void handleActionForAiVoiceCalling(int mSelectedPosition) {
        if (bottomServiceListUsedFor.equals(com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_AFTER_FREE_CHAT)) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_CHAT_WINDOW_CASHBACK_RECHARGE_SERVICE_SELECTION, FIREBASE_EVENT_ITEM_CLICK, "");
            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
        }else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_AI_CALL_WINDOW_CONTINUE_AI_CALL_RECHARGE_SERVICE_SELECTION, FIREBASE_EVENT_ITEM_CLICK, "");
        }
        callingActivity = getArguments().getString(com.ojassoft.astrosage.utils.CGlobalVariables.CALLING_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, callingActivity);
        Intent intent = new Intent(context, MiniPaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
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
