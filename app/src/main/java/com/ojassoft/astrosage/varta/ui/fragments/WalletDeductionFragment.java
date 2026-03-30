package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_DEDUCTION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.varta.adapters.DeductionHistoryAdapter;
import com.ojassoft.astrosage.varta.model.DeductionHistoryBean;
import com.ojassoft.astrosage.varta.model.RechargeHistoryBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.vartalive.widgets.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletDeductionFragment extends Fragment implements RetrofitResponses {



    CustomProgressDialog pdFrag;
    RelativeLayout frDeductionHistory;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    ArrayList<DeductionHistoryBean> arrayList = new ArrayList<>();
    DeductionHistoryAdapter deductionHistoryAdapter;

    private int status100Count;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_wallet_deduction, container, false);
        pdFrag = new CustomProgressDialog(getActivity());
        frDeductionHistory = view.findViewById(R.id.frDeductionHistory);
        linearLayout = view.findViewById(R.id.no_item_available);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));

        ArrayList<DeductionHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getDeductionHistoryBeanArrayList();
        if (arrayList != null && arrayList.size() > 0) {
            displayDataRecharge();
        } else {
            getCallData();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getCallData();
        // parseList();
    }

//    private void parseList() {
//        try {
//            JSONObject jsonObject = new JSONObject(jsonString); // Your full JSON string
//            JSONArray jsonArray = jsonObject.getJSONArray("deduction");
//
//            Gson gson = new Gson();
//            Type listType = new TypeToken<List<Deduction>>(){}.getType();
//            List<Deduction> deductionList = gson.fromJson(jsonArray.toString(), listType);
//
//            // Set adapter
//            DeductionAdapter adapter = new DeductionAdapter(deductionList);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setAdapter(adapter);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void fetchWalletDeductionList() {


        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerList(getParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {

                        String myResponse = response.body().string();
                        Log.e("SAN TestList", "Response " + myResponse);

                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (getActivity() == null) return;
                try {
                    CUtils.showSnackbar(frDeductionHistory, getActivity().getResources().getString(R.string.something_wrong_error), getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public HashMap<String, String> getParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getActivity()));


        headers.put(CGlobalVariables.PACKAGE_NAME, "" + BuildConfig.APPLICATION_ID);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(getActivity()));
        //Log.e("SAN ", " HF1 params= " + headers.toString());


        return headers;
    }

    private void getCallData() {
        if (CUtils.isConnectedWithInternet(requireActivity())) {
            String lastId = "0";
            if (!arrayList.isEmpty())
                lastId = arrayList.get(arrayList.size() - 1).getPurchaseId();
            pdFrag.show();
            CUtils.getConsultationHistoryViaRetrofit(WalletDeductionFragment.this, CONSULT_HISTORY_DEDUCTION, lastId, "WalletDeductionHistoryFragment");
        }
    }
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() != null) {
            try {
                hideProgressBar();
                String myResponse = response.body().string();
                Log.d("TestDuctionResponse","TestDuctionHistory==>>"+myResponse);
                parseConsulList(myResponse);
            }catch (Exception e){
                //
            }

        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            hideProgressBar();
            showErrorMsg(false);
            CUtils.showSnackbar(frDeductionHistory,getContext().getString(R.string.something_wrong_error)+" ( "+t+" )",getContext());

        }catch (Exception e){
            //
        }
    }

    private void parseConsulList(String response) {
        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<DeductionHistoryBean> deductionHistoryBeanArrayList = new ArrayList();
                DeductionHistoryBean rechargeHistoryBean;
                JSONArray deductions = null;

                JSONObject jsonObject = new JSONObject(response);


                if (jsonObject.has("deduction")) {
                    deductions = jsonObject.getJSONArray("deduction");
                    if (deductions != null && deductions.length() > 0) {
                        for (int i = 0; i < deductions.length(); i++) {
                            rechargeHistoryBean = new DeductionHistoryBean();
                            String deductedAmount = deductions.getJSONObject(i).optString("deductedAmount");
                            String deductedTime = deductions.getJSONObject(i).optString("deductedTime");
                            String displayMessage = deductions.getJSONObject(i).optString("displayMsg");
                            String orderId = deductions.getJSONObject(i).optString("orderId");
                            String purchaseId = deductions.getJSONObject(i).optString("purchaseId");

                            //Log.e("SAN ", "CHA response parse consultationId " + rechargeId );


                            rechargeHistoryBean.setDeductedAmount(deductedAmount);
                            rechargeHistoryBean.setDeductedTime(deductedTime);
                            rechargeHistoryBean.setDisplayMsg(displayMessage);
                            rechargeHistoryBean.setDisplayMsg(displayMessage);
                            rechargeHistoryBean.setOrderId(orderId);
                            rechargeHistoryBean.setPurchaseId(purchaseId);
                            deductionHistoryBeanArrayList.add(rechargeHistoryBean);
                        }
                    }

                    arrayList.addAll(deductionHistoryBeanArrayList);
                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setDeductionHistoryBeanArrayList(arrayList);

                    if (deductionHistoryAdapter != null) {
                        deductionHistoryAdapter.historyRecordsUpdate(arrayList);
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

                    LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(mReceiverBackgroundLoginServiceRecharge
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
            if (CUtils.getUserLoginStatus(requireActivity())) {
                CUtils.fcmAnalyticsEvents("bg_login_from_consult_history",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(requireActivity(), Loginservice.class);
                requireActivity().startService(intent);
            }
        } catch (Exception e) {}
    }
    public void displayDataRecharge() {
        ArrayList<DeductionHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getDeductionHistoryBeanArrayList();
        //Log.d("rechargeHistory",new Gson().toJson(arrayList));
        if (arrayList != null && arrayList.size() > 0) {
            //Log.e("SAN ", "CHA response parse consultationId " + deductionHistoryBeanArrayList.get(0).getDeductedTime() );

            deductionHistoryAdapter = new DeductionHistoryAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(deductionHistoryAdapter);
        } else {
            showErrorMsg(false);
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
                LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(mReceiverBackgroundLoginServiceRecharge);
            }

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverBackgroundLoginServiceRecharge != null) {
            LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(mReceiverBackgroundLoginServiceRecharge);
        }
    }
}