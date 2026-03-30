package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.ChatHistoryAdapter;
import com.ojassoft.astrosage.varta.model.ChatHistoryBean;
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

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ChatHistoryFragment extends Fragment implements  RetrofitResponses {
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    TextView linkTextView,txtViewNoDataFound;
    FrameLayout frChatHistory;
    CustomProgressDialog pdFrag;
    Activity currentActivity;
    private final int DATA_CHAT = 2;
    ArrayList<ChatHistoryBean> arrayList = new ArrayList<>();
    ChatHistoryAdapter chatHistoryAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_history_frag_layout, container, false);
        linearLayout = view.findViewById(R.id.no_item_available);
        linkTextView = view.findViewById(R.id.link_tv);
        txtViewNoDataFound = view.findViewById(R.id.txtViewNoDataFound);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        frChatHistory = view.findViewById(R.id.frChatHistory);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        txtViewNoDataFound.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium));
        linkTextView.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular));
        /*ArrayList<ChatHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getChatHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {
            ChatHistoryAdapter callHistoryAdapter = new ChatHistoryAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(callHistoryAdapter);
        }else
        {
            showErrorMsg(false);
        }*/

        currentActivity = getActivity();

        ArrayList<ChatHistoryBean> arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getChatHistoryBeanList();
        if (arrayList != null && arrayList.size() > 0) {
            displayDataChat();
        }/*else
        {
            getCallData();
        }*/

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.switchToConsultTab(FILTER_TYPE_CHAT, getActivity());
                CUtils.switchToConsultTab(FILTER_TYPE_CALL, getActivity());//redirect to AI list
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

    @Override
    public void onResume() {
        super.onResume();
        if(arrayList != null) {
            arrayList.clear();
        }
        getCallData();
    }

    public void showErrorMsg(boolean isDataAvaiable) {
        try {
            if (isDataAvaiable) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            //
        }
    }

    private void getCallData() {
        Log.e("TestChat ", " CHF chat getCallData()" );
        //errorLogsConsultation = errorLogsConsultation + " CHF chat getCallData() \n";
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            //showProgressBar();
            String lastId = "0";
            if (!arrayList.isEmpty())
                lastId = arrayList.get(arrayList.size() - 1).getConsultationId();

           // CUtils.getConsultationHistory(ChatHistoryFragment.this, CONSULT_HISTORY_CHAT, lastId, DATA_CHAT);
            CUtils.getConsultationHistoryViaRetrofit(ChatHistoryFragment.this, CONSULT_HISTORY_CHAT, lastId, "ChatHistoryFragment");
        }
    }

    public void displayDataChat(){
        arrayList = ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.getChatHistoryBeanList();
        //Log.e("SAN ", " CHF arrayList.size() => " + arrayList.size() );
        if (arrayList != null && arrayList.size() > 0) {
            chatHistoryAdapter = new ChatHistoryAdapter(getActivity(), arrayList);
            recyclerView.setAdapter(chatHistoryAdapter);
        }else
        {
            showErrorMsg(false);
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

//    @Override
//    public void onResponse(String response, int method) {
//        //Log.e("SAN ", " CHF chat response " + response );
//        //errorLogsConsultation = errorLogsConsultation + " CHF chat response " + response +"\n";
//        //hideProgressBar();
//        if ( method == DATA_CHAT ) {
//            //parseConsulList(response);
//            //Log.e("SAN ", " CHF response " + response );
//            parseConsulList(response);
//        }
//
//    }
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

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            hideProgressBar();
            showErrorMsg(false);
            CUtils.showSnackbar(frChatHistory,getContext().getString(R.string.something_wrong_error)+" ( "+t+" )",getContext());

        }catch (Exception e){
            //
        }
    }
//    @Override
//    public void onError(VolleyError error) {
//        //Log.e("SAN ", "CHF chat onError " + error.toString());
//        //errorLogsConsultation = errorLogsConsultation + "CHF chat onError " + error.toString() +"\n";
//        hideProgressBar();
//    }

    private void parseConsulList(String response){
        //Log.e("TestChat", "response="+response);
        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<ChatHistoryBean> chatHistoryBeanList = new ArrayList();
                ChatHistoryBean chatHistoryBean;
                JSONArray chats = null;

                JSONObject jsonObject = new JSONObject(response);

                String walletBalance = jsonObject.getString("walletbalance");
                ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setWalletbalance(walletBalance);
                ((ConsultantHistoryActivity) getActivity()).updateWalletBalance();

                if(jsonObject.has("chats")) {
                    chats = jsonObject.getJSONArray("chats");
                    if(chats != null && chats.length()>0) {
                        for (int i = 0; i < chats.length(); i++) {

                            chatHistoryBean = new ChatHistoryBean();
                            String userPhoneNo = chats.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = chats.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = chats.getJSONObject(i).getString("astrologerName");
                            String consultationTime = chats.getJSONObject(i).getString("consultationTime");
                            String callDuration = chats.getJSONObject(i).getString("callDuration");
                            String callAmount = chats.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = chats.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = chats.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = chats.getJSONObject(i).getString("astroWalletId");
                            String urlText = chats.getJSONObject(i).getString("urlText");
                            String consultationMode = chats.getJSONObject(i).getString("consultationMode");
                            String callChatId = chats.getJSONObject(i).getString("callChatId");
                            String refundStatus = chats.getJSONObject(i).getString("refundStatus");
                            String consultationId = chats.getJSONObject(i).getString("consultationId");

                            String aiAstroId = chats.getJSONObject(i).optString("aiai");
                            String astroExpertise = chats.getJSONObject(i).optString("astroExpertise");
                            String astroImageFileLarge = chats.getJSONObject(i).optString("astroImageFileLarge");
                            boolean isFreeForChat = chats.getJSONObject(i).optBoolean("iofch");
                            String durationUnitType = chats.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = chats.getJSONObject(i).optString("calldurationmin");



                            chatHistoryBean.setUserPhoneNo(userPhoneNo);
                            chatHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            chatHistoryBean.setAstrologerName(astrologerName);
                            chatHistoryBean.setConsultationTime(consultationTime);
                            chatHistoryBean.setCallDuration(callDuration);
                            chatHistoryBean.setCallAmount(callAmount);
                            chatHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            chatHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            chatHistoryBean.setAstroWalletId(astroWalletId);
                            chatHistoryBean.setUrlText(urlText);
                            chatHistoryBean.setConsultationMode(consultationMode);
                            chatHistoryBean.setCallChatId(callChatId);
                            chatHistoryBean.setRefundStatus(refundStatus);
                            chatHistoryBean.setConsultationId(consultationId);
                            chatHistoryBean.setAiAstroId(aiAstroId);
                            chatHistoryBean.setAstroExpertise(astroExpertise);
                            chatHistoryBean.setAstroImageFileLarge(astroImageFileLarge);
                            chatHistoryBean.setFreeForChat(isFreeForChat);
                            chatHistoryBean.setDurationUnitType(durationUnitType);
                            chatHistoryBean.setCallDurationMin(callDurationMin);


                            chatHistoryBeanList.add(chatHistoryBean);
                        }
                    }

                    arrayList.addAll(chatHistoryBeanList);
                    ((ConsultantHistoryActivity) getActivity()).consultantHistoryBean.setChatHistoryBeanList(arrayList);
                    if (chatHistoryAdapter != null) {
                        chatHistoryAdapter.historyRecordsUpdate(arrayList);
                    } else {
                        displayDataChat();
                    }
                }
            }

            if (arrayList != null && arrayList.size() == 0) {
                showErrorMsg(false);
            }

            hideProgressBar();

        } catch (Exception e) {
            //Log.e("SAN ", "CHF chat response parse exp " + e.toString());
            //errorLogsConsultation = errorLogsConsultation + "CHF chat response parse exp " + e.toString() +"\n";
            String status="";
            try {
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
                if ( status.equals("100") ) {

                    LocalBroadcastManager.getInstance(currentActivity).registerReceiver(mReceiverBackgroundLoginServiceChat
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


    private final BroadcastReceiver mReceiverBackgroundLoginServiceChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errorLogsConsultation = errorLogsConsultation + "CHA gift mReceiverBackgroundLoginService \n";
            //Log.e("SAN ", " Broadcast received chat ");
            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getCallData();
            } else {
                showErrorMsg(false);
            }

            if (mReceiverBackgroundLoginServiceChat != null) {
                LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceChat);
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverBackgroundLoginServiceChat != null) {
            LocalBroadcastManager.getInstance(currentActivity).unregisterReceiver(mReceiverBackgroundLoginServiceChat);
        }
    }



}
