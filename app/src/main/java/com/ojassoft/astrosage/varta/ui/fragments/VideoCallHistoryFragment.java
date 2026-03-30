package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.CallHistoryAdapter;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
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

import javax.annotation.Nullable;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_LIVE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_VIDEO;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class VideoCallHistoryFragment extends Fragment implements VolleyResponse, RetrofitResponses {

    RecyclerView recyclerView;
    LinearLayout linearLayout;
    TextView linkTextView,txtViewNoDataFound;

    CustomProgressDialog pdFrag;
    Activity currentActivity;
    private final int DATA_VIDEO = 2;
    ArrayList<CallHistoryBean> arrayList = new ArrayList<>();
    private CallHistoryAdapter callHistoryAdapter;

    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_video_call_history, container, false);
        linearLayout = view.findViewById(R.id.no_item_available);
        frVideoCallHistory = view.findViewById(R.id.frVideoCallHistory);
        linkTextView = view.findViewById(R.id.link_tv);
        txtViewNoDataFound = view.findViewById(R.id.txtViewNoDataFound);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        txtViewNoDataFound.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium));
        linkTextView.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular));
        /*
        ArrayList<CallHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getVideoCallHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {
            CallHistoryAdapter callHistoryAdapter = new CallHistoryAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(callHistoryAdapter);
        } else {
            showErrorMsg(false);
        }
        */

        currentActivity = getActivity();

        ArrayList<CallHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getVideoCallHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {
            displayDataVideo();
        }else
        {
            getVideoData();
        }
        
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(getActivity());
                if (isAIChatDisplayed) {
                    CUtils.switchToConsultTab(FILTER_TYPE_CHAT, getActivity());//redirect to chat list
                } else {
                    CUtils.switchToConsultTab(FILTER_TYPE_CALL, getActivity());//redirect to AI list
                }*/

                CUtils.switchToConsultTab(FILTER_TYPE_CALL, getActivity());//redirect to AI list
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //Log.e("SAN ", " dy => " + dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    getVideoData();
                }
            }
        });

        return view;
    }

    private void getVideoData() {
        //Log.e("SAN ", " CHF VC getLiveData()" );
        //errorLogsConsultation = errorLogsConsultation + " CHF VC getLiveData()\n";
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            //showProgressBar();
            String lastId = "0";
            if (!arrayList.isEmpty())
                lastId = arrayList.get(arrayList.size() - 1).getConsultationId();

           // CUtils.getConsultationHistory(VideoCallHistoryFragment.this, CONSULT_HISTORY_VIDEO, lastId, DATA_VIDEO);
            CUtils.getConsultationHistoryViaRetrofit(VideoCallHistoryFragment.this, CONSULT_HISTORY_VIDEO, lastId, "VideoCallHistoryFragment");
        }
    }

    public void displayDataVideo(){
        arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getVideoCallHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {
            callHistoryAdapter = new CallHistoryAdapter(getContext(), arrayList, VideoCallHistoryFragment.this);
            recyclerView.setAdapter(callHistoryAdapter);
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
        //Log.e("SAN ", " CHF VC response " + response );
        //errorLogsConsultation = errorLogsConsultation + " CHF VC response " + response +"\n";
        //hideProgressBar();
        if (method == DATA_VIDEO ) {
            //Log.e("SAN ", " CHF response " + response );
            parseConsulList(response);
        }

    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("SAN ", "CHF VC onError " + error.toString());
        //errorLogsConsultation = errorLogsConsultation + "CHF VC onError " + error.toString() +"\n";
        hideProgressBar();
    }
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() != null) {
            try {
                String myResponse = response.body().string();
                //Log.d("TestHistoryResponse","TestHistory==>>"+myResponse);
                parseConsulList(myResponse);
            }catch (Exception e){

            }

        }
    }
private FrameLayout frVideoCallHistory;
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            hideProgressBar();
            showErrorMsg(false);
            CUtils.showSnackbar(frVideoCallHistory,getContext().getString(R.string.something_wrong_error)+" ( "+t+" )",getContext());

        }catch (Exception e){
            //
        }    }
    private void parseConsulList(String response){
        //hideProgressBar();
        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<CallHistoryBean> liveHistoryBeanList = new ArrayList();
                CallHistoryBean liveHistoryBean;
                JSONArray consultations = null;

                JSONObject jsonObject = new JSONObject(response);

                String walletBalance = jsonObject.getString("walletbalance");
                ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setWalletbalance(walletBalance);
                ((ConsultantHistoryActivity) getActivity()).updateWalletBalance();

                if(jsonObject.has("videos")) {
                    consultations = jsonObject.getJSONArray("videos");
                    if(consultations != null && consultations.length()>0) {
                        for (int i = 0; i < consultations.length(); i++) {
                            liveHistoryBean = new CallHistoryBean();
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
                            String refundStatus = consultations.getJSONObject(i).getString("refundStatus");
                            String consultationId = consultations.getJSONObject(i).getString("consultationId");
                            String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");



                            liveHistoryBean.setUserPhoneNo(userPhoneNo);
                            liveHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            liveHistoryBean.setAstrologerName(astrologerName);
                            liveHistoryBean.setConsultationTime(consultationTime);
                            liveHistoryBean.setCallDuration(callDuration);
                            liveHistoryBean.setCallAmount(callAmount);
                            liveHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            liveHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            liveHistoryBean.setAstroWalletId(astroWalletId);
                            liveHistoryBean.setUrlText(urlText);
                            liveHistoryBean.setConsultationMode(consultationMode);
                            liveHistoryBean.setCallChatId(callChatId);
                            liveHistoryBean.setRefundStatus(refundStatus);
                            liveHistoryBean.setConsultationId(consultationId);
                            liveHistoryBean.setDurationUnitType(durationUnitType);
                            liveHistoryBean.setCallDurationMin(callDurationMin);


                            liveHistoryBeanList.add(liveHistoryBean);

                        }
                    }

                    arrayList.addAll(liveHistoryBeanList);

                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setVideoCallHistoryBeanList(arrayList);

                    if (callHistoryAdapter != null) {
                        callHistoryAdapter.historyRecordsUpdate(arrayList);
                    } else {
                        displayDataVideo();
                    }
                }
            }

            if (arrayList != null && arrayList.size() == 0) {
                showErrorMsg(false);
            }
            hideProgressBar();
        } catch (Exception e) {
            //Log.e("SAN ", "CHF VC response parse exp " + e.toString());
            //errorLogsConsultation = errorLogsConsultation + "CHF VC response parse exp " + e.toString() +"\n";
            String status="";
            try {
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
                if ( status.equals("100") ) {

                    LocalBroadcastManager.getInstance(currentActivity).registerReceiver(mReceiverBackgroundLoginServiceVideo
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
            if (CUtils.getUserLoginStatus(currentActivity)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_consult_history",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(currentActivity, Loginservice.class);
                currentActivity.startService(intent);
            }
        } catch (Exception e) {}
    }


    private final BroadcastReceiver mReceiverBackgroundLoginServiceVideo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errorLogsConsultation = errorLogsConsultation + "CHA gift mReceiverBackgroundLoginService \n";
            //Log.e("SAN ", " Broadcast received video ");
            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getVideoData();
            } else {
                showErrorMsg(false);
            }

            if (mReceiverBackgroundLoginServiceVideo != null) {
                LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceVideo);
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverBackgroundLoginServiceVideo != null) {
            LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceVideo);
        }
    }


}