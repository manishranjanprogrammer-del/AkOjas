package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_GIFT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.getGiftModelArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.GiftHistoryAdapter;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.vartalive.widgets.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class GiftHistoryFragment extends Fragment implements VolleyResponse, RetrofitResponses {

    private Activity activity;
    private RecyclerView recyclerView;
    LinearLayout linearLayout;
    TextView linkTextView,txtViewNoDataFound;

    CustomProgressDialog pdFrag;
    Activity currentActivity;
    private final int DATA_GIFT = 2;
    ArrayList<CallHistoryBean> arrayList = new ArrayList<>();
    GiftHistoryAdapter giftHistoryAdapter;

    FrameLayout frGiftFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_history, container, false);
        init(view);
        return view;
    }

    /**
     * @param view
     */
    private void init(View view) {
        activity = getActivity();
        linearLayout = view.findViewById(R.id.no_item_available);
        linkTextView = view.findViewById(R.id.link_tv);
        txtViewNoDataFound = view.findViewById(R.id.txtViewNoDataFound);
        recyclerView = view.findViewById(R.id.recyclerview);
        frGiftFragment = view.findViewById(R.id.frGiftFragment);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        txtViewNoDataFound.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium));
        /*ArrayList<CallHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getGiftHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {

              ArrayList<GiftModel> giftModelArrayList = getGiftModelArrayList();
            //ArrayList<GiftModel> giftModelArrayList = new ArrayList<>();
            GiftHistoryAdapter adapter = new GiftHistoryAdapter(activity, arrayList, giftModelArrayList);
            recyclerView.setAdapter(adapter);
        } else {
            showErrorMsg(false);
        }*/

        currentActivity = getActivity();

        ArrayList<CallHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getGiftHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {
            displayDataGift();
        }else
        {
            getGiftData();
        }


        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("SAN ", " dy => " + dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    getGiftData();
                }
            }
        });

    }

    private void getGiftData() {
        //Log.e("SAN ", " CHF gift getGiftData()" );
        //errorLogsConsultation = errorLogsConsultation + "CHF gift getGiftData() \n";
        if (CUtils.isConnectedWithInternet(currentActivity)) {
           // showProgressBar();
            String lastId = "0";
            if (!arrayList.isEmpty())
                lastId = arrayList.get(arrayList.size() - 1).getConsultationId();

            //CUtils.getConsultationHistory(GiftHistoryFragment.this, CONSULT_HISTORY_GIFT, lastId, DATA_GIFT);
            CUtils.getConsultationHistoryViaRetrofit(GiftHistoryFragment.this, CONSULT_HISTORY_GIFT, lastId, "GiftHistoryFragment");
        }
    }

    public void displayDataGift(){
        arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getGiftHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {

            ArrayList<GiftModel> giftModelArrayList = getGiftModelArrayList();
            //ArrayList<GiftModel> giftModelArrayList = new ArrayList<>();
            giftHistoryAdapter = new GiftHistoryAdapter(getContext(), arrayList, giftModelArrayList);
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
            recyclerView.setAdapter(giftHistoryAdapter);
        } else {
            showErrorMsg(false);
        }
    }

    public void showErrorMsg(boolean isDataAvaiable) {
        if (linearLayout != null && recyclerView != null) {
            if (isDataAvaiable) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void showProgressBar() {

        if (pdFrag == null) {
            pdFrag = new CustomProgressDialog(getActivity());
        }

        pdFrag.setCanceledOnTouchOutside(false);
        pdFrag.setCancelable(false);
        pdFrag.show();
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
    public void onResponse(String response, int method) {
        //Log.e("SAN ", " CHF gift response " + response );
        //errorLogsConsultation = errorLogsConsultation + " CHF gift response " + response +"\n";
        //hideProgressBar();
        if (method == DATA_GIFT ) {
            //Log.e("SAN ", " CHF response " + response );
            parseConsulList(response);
        }

    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("SAN ", "CHF gift onError " + error.toString());
        //errorLogsConsultation = errorLogsConsultation + "CHF gift onError " + error +"\n";
        hideProgressBar();
    }
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() != null) {
            try {
                String myResponse = response.body().string();
               // Log.d("TestHistoryResponse","TestHistory==>>"+myResponse);
                parseConsulList(myResponse);
            }catch (Exception e){

            }

        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            hideProgressBar();
            showErrorMsg(false);
            CUtils.showSnackbar(frGiftFragment,getContext().getString(R.string.something_wrong_error)+" ( "+t+" )",getContext());

        }catch (Exception e){
            //
        }
    }
    private void parseConsulList(String response){

        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<CallHistoryBean> giftHistoryBeanList  = new ArrayList();
                CallHistoryBean giftHistoryBean;
                JSONArray consultations = null;

                JSONObject jsonObject = new JSONObject(response);

                String walletBalance = jsonObject.getString("walletbalance");
                ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setWalletbalance(walletBalance);
                ((ConsultantHistoryActivity) getActivity()).updateWalletBalance();

                if(jsonObject.has("gifts")) {
                    consultations = jsonObject.getJSONArray("gifts");
                    if(consultations != null && consultations.length()>0) {
                        for (int i = 0; i < consultations.length(); i++) {
                            giftHistoryBean = new CallHistoryBean();
                            String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                            String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                            String callDuration = consultations.getJSONObject(i).getString("callDuration");
                            String callAmount = consultations.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                            String urlText = consultations.getJSONObject(i).getString("urlText");
                            String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                            String callChatId = consultations.getJSONObject(i).getString("callChatId");
                            String serviceIdId = consultations.getJSONObject(i).getString("serviceIdId");
                            String refundStatus = consultations.getJSONObject(i).getString("refundStatus");
                            String consultationId = consultations.getJSONObject(i).getString("consultationId");
                            String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");



                            //Log.e("SAN ", "CHA response parse consultationId " + consultationId );

                            giftHistoryBean.setUserPhoneNo(userPhoneNo);
                            giftHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            giftHistoryBean.setAstrologerName(astrologerName);
                            giftHistoryBean.setConsultationTime(consultationTime);
                            giftHistoryBean.setCallDuration(callDuration);
                            giftHistoryBean.setCallAmount(callAmount);
                            giftHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            giftHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            giftHistoryBean.setAstroWalletId(astroWalletId);
                            giftHistoryBean.setUrlText(urlText);
                            giftHistoryBean.setConsultationMode(consultationMode);
                            giftHistoryBean.setCallChatId(callChatId);
                            giftHistoryBean.setServiceIdId(serviceIdId);
                            giftHistoryBean.setRefundStatus(refundStatus);
                            giftHistoryBean.setConsultationId(consultationId);
                            giftHistoryBean.setDurationUnitType(durationUnitType);
                            giftHistoryBean.setCallDurationMin(callDurationMin);


                            giftHistoryBeanList.add(giftHistoryBean);
                        }
                    }

                    arrayList.addAll(giftHistoryBeanList);
                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setGiftHistoryBeanList(arrayList);

                    if (giftHistoryAdapter != null) {
                        giftHistoryAdapter.historyRecordsUpdate(arrayList);
                    } else {
                        displayDataGift();
                    }
                }
            }

            if (arrayList != null && arrayList.size() == 0) {
                showErrorMsg(false);
            }

            hideProgressBar();

        } catch (Exception e) {
            //Log.e("SAN ", "CHF gift response parse exp " + e.toString());
            //errorLogsConsultation = errorLogsConsultation + "CHF gift response parse exp " + e.toString() +"\n";
            String status="";
            try {
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
                if ( status.equals("100") ) {

                    LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverBackgroundLoginServiceGift
                            , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                    startBackgroundLoginService();
                }
            } catch (Exception exception){
                //Log.e("SAN CHA response ", " pasrse exp " + exception.toString() );
                //Log.e("SAN ", "CHA gift exception 100 exp 2 " + e.toString() );
                //errorLogsConsultation = errorLogsConsultation + "CHA gift exception 100 exp 2 " + e.toString()+"\n";
            }

            if ( !status.equals("100") ) {
                hideProgressBar();
                showErrorMsg(false);
            }
        }
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(activity)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_consult_history",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(activity, Loginservice.class);
                activity.startService(intent);
            }
        } catch (Exception e) {}
    }


    private final BroadcastReceiver mReceiverBackgroundLoginServiceGift = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errorLogsConsultation = errorLogsConsultation + "CHA gift mReceiverBackgroundLoginService \n";
            //Log.e("SAN ", " Broadcast received gift ");
            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getGiftData();
            } else {
                showErrorMsg(false);
            }

            if (mReceiverBackgroundLoginServiceGift != null) {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginServiceGift);
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverBackgroundLoginServiceGift != null) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginServiceGift);
        }
    }

}