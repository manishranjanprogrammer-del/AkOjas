package com.ojassoft.astrosage.varta.receiver;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatStatusReciever extends BroadcastReceiver implements VolleyResponse {

    private String intentActionType, channelId;
    private int intentNotificationId;
    private Context context;
    private String msg, chatJsonObject;
    private MediaPlayer mMediaPlayer;
    private String userChatStatus = "", astrologerName, astrologerProfileUrl, astrologerId,userChatTime;
    private RequestQueue queue;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        try {
            queue = VolleySingleton.getInstance(context).getRequestQueue();
            actionOnIntent(intent);
        } catch (Exception e){
            Log.d("myLogs","receiver Exception 1-->"+e);
        }
    }

    private void actionOnIntent(Intent intent){
        if (intent != null) {
            intentActionType = intent.getStringExtra(CGlobalVariables.CHAT_ACTION_TYPE);
            intentNotificationId = intent.getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
            channelId = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
            chatJsonObject = intent.getStringExtra("connect_chat_bean");
            astrologerName = intent.getStringExtra("astrologer_name");
            astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
            astrologerId = intent.getStringExtra("astrologer_id");
            userChatTime =  intent.getStringExtra("userChatTime");

            if (intentActionType.equals(CGlobalVariables.CHAT_REJECTED_BY_USER)){
                Log.d("testNewChat", " ChatStatusReceiver Called fro reject Chat " );

                chatRejected();

            }
        }
    }

    private void chatRejected() {
        try {
            if (CUtils.isConnectedWithInternet(context)) {
                if (context == null) return;
                CUtils.changeFirebaseKeyStatus(channelId, "false", true, CGlobalVariables.USER_REJECTED);
                String chatStatus = CGlobalVariables.USER_BUSY;
                String remarks = CGlobalVariables.USER_REJECTED;
                try{
                    context.stopService(new Intent(context, AstroAcceptRejectService.class));
                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                    nMgr.cancel(intentNotificationId);
                } catch (Exception e){
                    //
                }
//                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CHAT_URL,
//                        this, false, getChatCompleteParams(channelId, chatStatus, remarks), 1).getMyStringRequest();
//                stringRequest.setShouldCache(false);
//                queue.add(stringRequest);
                callEndChatApi(channelId,remarks,chatStatus);
            }
        } catch (Exception e){
            Log.d("myLogs","receiver Exception-->"+e);
        }
    }
    private void callEndChatApi(String channelID, String remarks, String status) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks,status), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body()!=null) {
                    try {
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");

                    } catch (Exception e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public Map<String, String> getChatCompleteParams(String channelID, String chatStatus, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, chatStatus);
        headers.put(CGlobalVariables.CHAT_DURATION, "00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onResponse(String response, int method) {
//        if (response != null && response.length() > 0) {
//            if (method == 1) {
//                Log.d("myLogs","receiver response-->"+response);
//            }
//        }
    }

    @Override
    public void onError(VolleyError error) {

    }
}
