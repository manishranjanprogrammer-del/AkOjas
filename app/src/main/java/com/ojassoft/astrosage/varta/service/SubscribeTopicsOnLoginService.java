package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.ui.act.AstrosageKundliApplication.getAppContext;
import static com.ojassoft.astrosage.utils.CGlobalVariables.REPORT_ERROR_PREF;
import static com.ojassoft.astrosage.varta.utils.CUtils.followAstrologerModelArrayList;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.model.FollowAstrologerModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeTopicsOnLoginService extends IntentService implements VolleyResponse {

    private final int FETCH_FOLLOWED_ASTROLOGERS = 1;
    private RequestQueue queue;
    private boolean flag = true;

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getFollowedAstros();
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    public SubscribeTopicsOnLoginService() {
        super("app name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        queue = VolleySingleton.getInstance(getAppContext()).getRequestQueue();
        if (followAstrologerModelArrayList == null) followAstrologerModelArrayList = new ArrayList<>();
        //Toast.makeText(this, "sub->started", Toast.LENGTH_LONG).show();
        //com.ojassoft.astrosage.utils.CUtils.saveStringData(SubscribeTopicsOnLoginService.this, "subscribeAndUnSubscribe", "sub->started");
        getFollowedAstros();
    }

    private void getFollowedAstros(){
        if (CUtils.isConnectedWithInternet(this)) {
            //Log.e("SAN PF LiveAstro URL1=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_FOLLOWING_ASTRO_URL,
//                    SubscribeTopicsOnLoginService.this, false, CUtils.getFollowingAstroParams(this), FETCH_FOLLOWED_ASTROLOGERS).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.getFollowedAstrologers(com.ojassoft.astrosage.varta.utils.CUtils.getFollowingAstroParams(this));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);

                        //Log.e("TestANRFollow", "jsonObject= " + jsonObject);

                        if (jsonObject.has("status")) {
                            if (jsonObject.getString("status").equalsIgnoreCase("100")) {
                                startBackgroundLoginService();
                            }
                        } else {
                            Log.d("unsubtopic", "onResponse: 1");
                            parseFollowingAstrologerList(responses);
                            //parseUnfollowAstrologerList(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onResponse(String response, int method) {
        //Toast.makeText(SubscribeTopicsOnLoginService.this, "subResponse->"+response, Toast.LENGTH_LONG).show();
        /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(SubscribeTopicsOnLoginService.this,"subscribeAndUnSubscribe","")+"\nFETCH_FOLLOWED_ASTROLOGERS response->"+response;
        com.ojassoft.astrosage.utils.CUtils.saveStringData(this, "subscribeAndUnSubscribe",log );*/
        if (response != null && response.length() > 0) {
            if (method == FETCH_FOLLOWED_ASTROLOGERS) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("status")) {
                        if (jsonObject.getString("status").equalsIgnoreCase("100")) {
                            startBackgroundLoginService();
                        }
                    } else {
                        Log.d("unsubtopic", "onResponse: 1");
                        parseFollowingAstrologerList(response);
                        //parseUnfollowAstrologerList(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startBackgroundLoginService() {
        try {
            if(flag) {
                flag = false;
                LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                if (CUtils.getUserLoginStatus(this)) {
                    CUtils.fcmAnalyticsEvents("bg_login_from_subs_topic_service",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                    Intent intent = new Intent(SubscribeTopicsOnLoginService.this, Loginservice.class);
                    startService(intent);
                }
            } else {
                flag = true;
            }
        } catch (Exception e) {
            //
        }
    }

    private void parseFollowingAstrologerList(String liveAstroData){
        if(TextUtils.isEmpty(liveAstroData)){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(liveAstroData);
            if(jsonObject.has("astrologerslist")) {
                JSONArray jsonArray = jsonObject.getJSONArray("astrologerslist");
                //Toast.makeText(SubscribeTopicsOnLoginService.this, "parseFollowingAstrologerList->"+jsonArray.length(), Toast.LENGTH_LONG).show();
                /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(SubscribeTopicsOnLoginService.this,"subscribeAndUnSubscribe","")+"\nparseFollowingAstrologerList->"+jsonArray.length();
                com.ojassoft.astrosage.utils.CUtils.saveStringData(this, "subscribeAndUnSubscribe", log);*/
                followAstrologerModelArrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    FollowAstrologerModel followAstrologerModel = new FollowAstrologerModel();
                    followAstrologerModel.setAstrologerName(object.getString("nickName"));
                    followAstrologerModel.setAstrologerImage(object.getString("imageFile"));
                    followAstrologerModel.setFollowingStatus(object.getString("followValue"));
                    followAstrologerModel.setAstrologerId(object.getString("astrologerId"));
                    followAstrologerModel.setUserId(object.getString("userId"));
                    followAstrologerModelArrayList.add(followAstrologerModel);
                    //CUtils.subscribeFollowTopic(this, followAstrologerModel.getAstrologerId());
                    //Log.e("SANPF", "parseFollowingAstrologerList= " + followAstrologerModel.getAstrologerId());
                }
                if(!followAstrologerModelArrayList.isEmpty()) {
                    subscribeTopics();
                }
            }
        }catch (Exception e){
            //Log.e("SANPF", "parseUnfollowAstrologerList EXP1= "+e);
            //Toast.makeText(SubscribeTopicsOnLoginService.this, "parseFollowingAstrologerList ex->"+e, Toast.LENGTH_LONG).show();
            /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(SubscribeTopicsOnLoginService.this,"subscribeAndUnSubscribe","")+"\nparseFollowingAstrologerList ex->"+e;
            com.ojassoft.astrosage.utils.CUtils.saveStringData(this, "subscribeAndUnSubscribe", log);*/
        }
    }

    private void parseUnfollowAstrologerList(String liveAstroData){
        CUtils.listCount = 0;
        if(TextUtils.isEmpty(liveAstroData)){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(liveAstroData);
            if(jsonObject.has("unfolist")) {
                JSONArray jsonArray = jsonObject.getJSONArray("unfolist");
                //Toast.makeText(SubscribeTopicsOnLoginService.this, "parseUnfollowAstrologerList->"+jsonArray.length(), Toast.LENGTH_LONG).show();
                /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(SubscribeTopicsOnLoginService.this,"subscribeAndUnSubscribe","")+"\nparseUnfollowAstrologerList->"+jsonArray.length();
                com.ojassoft.astrosage.utils.CUtils.saveStringData(this, "subscribeAndUnSubscribe", log);*/
                Log.d("unfolist", liveAstroData);
                CUtils.myCount = jsonArray.length();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    CUtils.unSubscribeFollowTopic(this, object.getString("astrologerId"));
                    //Log.e("SANPF", "parseUnfollowAstrologerList= " + object.getString("astrologerId"));
                }
            }
        }catch (Exception e){
            /*Log.e("unsubtopic", "parseUnfollowAstrologerList EXP2= "+e);
            //Toast.makeText(SubscribeTopicsOnLoginService.this, "parseUnfollowAstrologerList ex->"+e, Toast.LENGTH_LONG).show();
            String log = com.ojassoft.astrosage.utils.CUtils.getStringData(SubscribeTopicsOnLoginService.this,"subscribeAndUnSubscribe","")+"\nparseUnfollowAstrologerList ex->"+e;
            com.ojassoft.astrosage.utils.CUtils.saveStringData(this, "subscribeAndUnSubscribe", log);*/
        }
    }

    @Override
    public void onError(VolleyError error) {
        //Toast.makeText(SubscribeTopicsOnLoginService.this, "subError->"+error, Toast.LENGTH_LONG).show();
        /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(SubscribeTopicsOnLoginService.this,"subscribeAndUnSubscribe","")+"\nsubError->"+error;
        com.ojassoft.astrosage.utils.CUtils.saveStringData(this, "subscribeAndUnSubscribe", log);*/
    }

    private void subscribeTopics(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < followAstrologerModelArrayList.size(); i++) {
                        FollowAstrologerModel followAstrologerModel = followAstrologerModelArrayList.get(i);
                        CUtils.addFollowEventForAstrologer(followAstrologerModel.getAstrologerId(),"SubscribeTopicsOnLoginService");
                        CUtils.subscribeFollowTopic(SubscribeTopicsOnLoginService.this, followAstrologerModel.getAstrologerId());
                        //Log.e("TestANRFollow", "subscribeTopics= " + followAstrologerModel.getAstrologerId());
                    }
                } catch (Exception e){
                    //
                }
            }
        });
    }

}
