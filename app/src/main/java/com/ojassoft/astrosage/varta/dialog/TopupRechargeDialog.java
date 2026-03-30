package com.ojassoft.astrosage.varta.dialog;

import static android.view.View.GONE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OPEN_FOR_AI_CHAT;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PID_TOPUP_RECHARGE_ADD_MONEY;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.CloudTopUpPriceItem;
import com.ojassoft.astrosage.model.CloudTopupModel;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopupRechargeDialog extends DialogFragment implements View.OnClickListener, CloudTopUpPriceItem.TopUpAmountSelectedCallback {

    Activity activity;
    ArrayList<CloudTopupModel> cloudTopupRechargeList;


    TextView questCountTV, balanceTV, topUpBtn;
    ImageView closeBtn;
    LinearLayout addMoneyBtn;
    RecyclerView recyclerView;
    CustomProgressDialog pd;
    CloudTopupModel selectedCloudTopupModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.topup_dialog_layout, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        balanceTV = view.findViewById(R.id.balance_tv);
        questCountTV = view.findViewById(R.id.ques_count_tv);
        recyclerView = view.findViewById(R.id.amt_recycler_view);
        topUpBtn = view.findViewById(R.id.top_up_btn);
        closeBtn = view.findViewById(R.id.close_btn);
        addMoneyBtn = view.findViewById(R.id.add_money_btn);
        closeBtn.setOnClickListener(v -> dismiss());

        addMoneyBtn.setOnClickListener(this);
        topUpBtn.setOnClickListener(this);
        questCountTV.setVisibility(GONE);
        showTopupRechargeList();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_TOPUP_DIALOG_CANCEL_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dismiss();
                break;

            case R.id.top_up_btn:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_TOPUP_DIALOG_RECHARGE_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                if (selectedCloudTopupModel != null) {
                    sendTopupPurchaseRequest(selectedCloudTopupModel);
                } else if(!cloudTopupRechargeList.isEmpty()){
                    CUtils.showSnackbar(topUpBtn, getResources().getString(R.string.select_topup_amount), activity);
                }else{
                    CUtils.showSnackbar(topUpBtn, getResources().getString(R.string.please_add_money_to_wallet), activity);
                }
                break;
            case R.id.add_money_btn:
                createSession(activity,PID_TOPUP_RECHARGE_ADD_MONEY);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_TOPUP_DIALOG_ADD_MONEY_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openWalletScreen();
                break;
        }
    }


    private void openWalletScreen() {
        try {
            Intent intent = new Intent(activity, WalletActivity.class);
            intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "TopupRechargeDialog");
            getActivity().startActivityForResult(intent,1);
        } catch (Exception e) {
            //
        }
    }

    private void showTopupRechargeList() {
        cloudTopupRechargeList = new ArrayList<>();
        String topupRechargeData = CUtils.getStringData(activity, CGlobalVariables.TOPUP_RECHARGE_DATA, "");
        Log.e("TestTopup", "topupRechargeData =" + topupRechargeData);
        try {
            JSONObject jsonObject = new JSONObject(topupRechargeData);
            String status = jsonObject.getString("status");
            if (status.equals("1")) {
                String rechargeData = String.valueOf(jsonObject.getJSONArray("data"));
                String totalBalance = jsonObject.getString("walletbalance");
                balanceTV.setVisibility(View.VISIBLE);
                String balanceText = getString(R.string.balance) + ":" + getString(R.string.astroshop_rupees_sign) + totalBalance;
                balanceTV.setText(balanceText);
                Gson gson = new Gson();
                ArrayList<CloudTopupModel> cloudTopupModels = gson.fromJson(rechargeData, new TypeToken<ArrayList<CloudTopupModel>>() {
                }.getType());
                cloudTopupRechargeList.addAll(cloudTopupModels);
            } else {
                balanceTV.setVisibility(GONE);
            }
            if (cloudTopupRechargeList != null && !cloudTopupRechargeList.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
//                int lastItemPos = cloudTopupRechargeList.size() - 1;
//                cloudTopupRechargeList.get(lastItemPos).setSelected(true);
                CloudTopUpPriceItem cloudTopUpPriceItem = new CloudTopUpPriceItem(cloudTopupRechargeList, activity, this);
                recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
                recyclerView.setAdapter(cloudTopUpPriceItem);
//                showQuestionCount(lastItemPos);
            } else {
                recyclerView.setVisibility(GONE);
            }
        } catch (Exception e) {
            Log.e("TestTopup","Exception="+e);
        }
    }

    private void showQuestionCount(int position) {
        try {
            if (position >= 0) {
                selectedCloudTopupModel = cloudTopupRechargeList.get(position);
                String quesCount = selectedCloudTopupModel.getChatCount();
                String text = String.format(getString(R.string.you_will_get_questions), quesCount);
                SpannableString spannableString = new SpannableString(text);
                int startIndex = text.indexOf(quesCount);
                int endIndex = startIndex + quesCount.length();
                if (startIndex != -1) {
                    spannableString.setSpan(
                            getContext().getColor(R.color.colorPrimary_day_night),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
                questCountTV.setVisibility(View.VISIBLE);
                questCountTV.setText(spannableString);
            }
        } catch (Exception e) {
            //
        }
    }


    public void sendTopupPurchaseRequest(CloudTopupModel cloudTopupModel) {

        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(topUpBtn, getResources().getString(R.string.no_internet), activity);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.sendTopupPurchaseRequest(getTopupPurchaseRequestParams(cloudTopupModel));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String myResponse = response.body().string();
                        Log.e("TestTopup", "myResponse= " + myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            String totalBalance = jsonObject.optString("walletbalance");
                            dismiss();
                            showTopupSucessDialog(cloudTopupModel, totalBalance);
                        } else {
                            CUtils.showSnackbar(topUpBtn, getResources().getString(R.string.something_wrong_error), activity);
                        }
                    } catch (Exception e) {
                        CUtils.showSnackbar(topUpBtn, getResources().getString(R.string.something_wrong_error), activity);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        hideProgressBar();
                        CUtils.showSnackbar(topUpBtn, getResources().getString(R.string.something_wrong_error), activity);
                        Log.e("TestTopup", " onFailure= " + t);
                    } catch (Exception e) {
                        //
                    }
                }
            });
        }
    }

    public Map<String, String> getTopupPurchaseRequestParams(CloudTopupModel cloudTopupModel) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(activity));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put("rechargeamt", cloudTopupModel.getRechargeAmount());
        headers.put("planuserid", com.ojassoft.astrosage.utils.CUtils.getUserName(activity));
        //Log.e("TestTopup", " params= " + headers);
        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onAmountSelected(int pos) {
        showQuestionCount(pos);
    }

    private void showProgressBar() {
        try {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
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

    private void showTopupSucessDialog(CloudTopupModel cloudTopupModel, String walletBalance) {
        try {
            Log.e("TestTopup", "showTopupSucessDialog, walletBalance: " + walletBalance);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_TOPUP_SUCESS_DIALOG, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.topup_success, null);
            TextView msgTextView = dialogView.findViewById(R.id.dhruvPlan_desc_tv);


            String msgText = getString(R.string.msg_topup_sucess, cloudTopupModel.getChatCount(), walletBalance);
            SpannableString spannableString = new SpannableString(msgText);
            String highlightingText = "₹" + walletBalance;
            int start = msgText.indexOf(highlightingText);
            if (start >= 0) {
                int end = start + highlightingText.length();
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            msgTextView.setText(spannableString);
            builder.setView(dialogView);
            final AlertDialog alert = builder.create();


            Window window = alert.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(window.getAttributes());

                DisplayMetrics displayMetrics = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                lp.width = (int) (displayMetrics.widthPixels * 0.50);

                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setAttributes(lp);

                ImageView cancelBtn = dialogView.findViewById(R.id.cancelIV);
                cancelBtn.setOnClickListener(v -> alert.dismiss());
                Button purchaseBtn = dialogView.findViewById(R.id.purchase_btn);
                purchaseBtn.setOnClickListener(v -> {
                    //open Dhruv plan screen(Activity)
                    try {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_CLOSE_TOPUP_SUCESS_DIALOG, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        alert.dismiss();
                    } catch (Exception e) {
                        //
                    }
                });

                CUtils.fcmAnalyticsEvents("end_chat_dialog_show", AstrosageKundliApplication.currentEventType, "");
                alert.show();
            }
        } catch (Exception e) {
            Log.e("TestTopup", "showTopupSucessDialog, Exception: " + e);
        }
    }
}
