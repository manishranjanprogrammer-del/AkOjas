package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.RechargeHistoryAdapter;
import com.ojassoft.astrosage.varta.model.RechargeHistoryBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.activity.MyAccountActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.vartalive.widgets.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_RECHARG;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RechargeHistoryFragment extends Fragment implements RetrofitResponses {
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    TextView linkTextView, tvAvbBal, tvWalBal, btnRecharge, tvMyAccount,txtViewNoDataFound;

    CustomProgressDialog pdFrag;
    Activity currentActivity;
    private final int DATA_RECHARGE = 2;
    ArrayList<RechargeHistoryBean> arrayList = new ArrayList<>();
    RechargeHistoryAdapter rechargeHistoryAdapter;
    private int status100Count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recharge_history_frag_layout, container, false);

        currentActivity = getActivity();

        linearLayout = view.findViewById(R.id.no_item_available);
        linkTextView = view.findViewById(R.id.link_tv);
        txtViewNoDataFound = view.findViewById(R.id.txtViewNoDataFound);
        recyclerView = view.findViewById(R.id.recyclerview);
        frRechargeHistory = view.findViewById(R.id.frRechargeHistory);
        tvAvbBal  = view.findViewById(R.id.tv_avb_bal);
        tvWalBal = view.findViewById(R.id.tv_wal_bal);
        btnRecharge = view.findViewById(R.id.btn_recharge);
        tvMyAccount = view.findViewById(R.id.tv_my_account);

        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        txtViewNoDataFound.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium));
        linkTextView.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular));
        FontUtils.changeFont(getActivity(), tvAvbBal, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), tvWalBal, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), btnRecharge, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvMyAccount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));

        ArrayList<RechargeHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getRechargeHistoryList();
        if (arrayList != null && arrayList.size() > 0) {
            displayDataRecharge();
        } else {
            getCallData();
        }
        displayBalance();
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                Intent intent = new Intent(getActivity(), WalletActivity.class);
                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "RechargeHistory");
                startActivity(intent);
            }
        });

        tvMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                    //intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "RechargeHistory");
                    getActivity().startActivity(intent);

                } catch (Exception e){
                }

            }
        });

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), WalletActivity.class);
                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "RechargeHistory");
                getActivity().startActivity(intent);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("SAN ", " dy => " + dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    getCallData();
                }
            }
        });

        return view;
    }

    private void getCallData() {
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            String lastId = "0";
            if (!arrayList.isEmpty())
                lastId = arrayList.get(arrayList.size() - 1).getRechargeId();
            CUtils.getConsultationHistoryViaRetrofit(RechargeHistoryFragment.this, CONSULT_HISTORY_RECHARG, lastId, "RechargeHistoryFragment");
        }
    }

    public void displayDataRecharge() {
        ArrayList<RechargeHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getRechargeHistoryList();
       //Log.d("rechargeHistory",new Gson().toJson(arrayList));
        if (arrayList != null && arrayList.size() > 0) {
            rechargeHistoryAdapter = new RechargeHistoryAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(rechargeHistoryAdapter);
        } else {
            showErrorMsg(false);
        }
    }

    public void displayBalance(){
        if ( ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getWalletbalance() != null){
            tvWalBal.setText(getResources().getString(R.string.rs_sign) + ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getWalletbalance());
        }
    }

    public void showErrorMsg(boolean isDataAvaiable) {
        if(getActivity() == null || linearLayout == null) return;
        if (isDataAvaiable) {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }



    private void hideProgressBar() {
        try {
            if (pdFrag != null && pdFrag.isShowing())
                pdFrag.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() != null) {
            try {
                String myResponse = response.body().string();
                Log.d("TestHistoryResponse","TestHistory==>>"+myResponse);
                parseConsulList(myResponse);
            }catch (Exception e){
                //
            }

        }
    }
    LinearLayout frRechargeHistory;
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            hideProgressBar();
            showErrorMsg(false);
            CUtils.showSnackbar(frRechargeHistory,getContext().getString(R.string.something_wrong_error)+" ( "+t+" )",getContext());

        }catch (Exception e){
            //
        }
    }

    private void parseConsulList(String response) {
        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<RechargeHistoryBean> rechargeHistoryList = new ArrayList();
                RechargeHistoryBean rechargeHistoryBean;
                JSONArray recharges = null;

                JSONObject jsonObject = new JSONObject(response);

                String walletBalance = jsonObject.getString("walletbalance");
                if (!TextUtils.isEmpty(walletBalance)) {
                    tvWalBal.setText(String.format("%s%s", getResources().getString(R.string.rs_sign), walletBalance));
                }

                if (jsonObject.has("recharges")) {
                    recharges = jsonObject.getJSONArray("recharges");
                    if (recharges != null && recharges.length() > 0) {
                            CUtils.saveBooleanData(getContext(),CGlobalVariables.HAS_WALLET_RECHARGE_HISTORY,true);
                        for (int i = 0; i < recharges.length(); i++) {
                            rechargeHistoryBean = new RechargeHistoryBean();
                            String rechargeType = recharges.getJSONObject(i).getString("rechargeType");
                            String rechargeAmount = recharges.getJSONObject(i).getString("rechargeAmount");
                            String rechargeDateTime = recharges.getJSONObject(i).getString("rechargeDateTime");
                            String displayMessage = recharges.getJSONObject(i).getString("displayMsg");
                            String orderId = recharges.getJSONObject(i).getString("orderId");
                            String message = recharges.getJSONObject(i).optString("message");
                            String referralMsg = recharges.getJSONObject(i).optString("referralMsg");
                            String rechargeId = recharges.getJSONObject(i).getString("rechargeId");

                            //Log.e("SAN ", "CHA response parse consultationId " + rechargeId );

                            rechargeHistoryBean.setRechargeType(rechargeType);
                            rechargeHistoryBean.setRechargeAmount(rechargeAmount);
                            rechargeHistoryBean.setRechargeDateTime(rechargeDateTime);
                            rechargeHistoryBean.setDisplayMsg(displayMessage);
                            rechargeHistoryBean.setOrderId(orderId);
                            rechargeHistoryBean.setMessage(message);
                            rechargeHistoryBean.setReferralMsg(referralMsg);
                            rechargeHistoryBean.setRechargeId(rechargeId);
                            rechargeHistoryList.add(rechargeHistoryBean);
                        }
                    }


                    arrayList.addAll(rechargeHistoryList);
                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setWalletbalance(walletBalance);
                    ((ConsultantHistoryActivity) getActivity()).updateWalletBalance();
                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setRechargeHistoryList(arrayList);

                    if (rechargeHistoryAdapter != null) {
                        rechargeHistoryAdapter.historyRecordsUpdate(arrayList);
                    } else {
                        displayDataRecharge();
                    }
                } else {
                    if (arrayList != null && arrayList.size() == 0) {
                        showErrorMsg(false);
                    }
                }

            }

            if (arrayList != null && arrayList.size() == 0) {
                showErrorMsg(false);
            }
            hideProgressBar();
        } catch (Exception e) {
            String status = "";
            try {
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
                if (status.equals("100") && status100Count < 2) {

                    LocalBroadcastManager.getInstance(currentActivity).registerReceiver(mReceiverBackgroundLoginServiceRecharge
                            , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                    startBackgroundLoginService();
                }
                status100Count +=1;
            } catch (Exception exception) {
             //
            }

            if (!status.equals("100")) {
                hideProgressBar();
                showErrorMsg(false);
            }
        }
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(currentActivity)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_consult_history",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(currentActivity, Loginservice.class);
                currentActivity.startService(intent);
            }
        } catch (Exception e) {}
    }


    private final BroadcastReceiver mReceiverBackgroundLoginServiceRecharge = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errorLogsConsultation = errorLogsConsultation + "CHA gift mReceiverBackgroundLoginService \n";
            //Log.e("SAN ", " Broadcast received Recharge ");
            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getCallData();
            } else {
                showErrorMsg(false);
            }

            if (mReceiverBackgroundLoginServiceRecharge != null) {
                LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceRecharge);
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverBackgroundLoginServiceRecharge != null) {
            LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceRecharge);
        }
    }



}