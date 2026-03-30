package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class PreFetchLiveAstroDataservice extends IntentService implements VolleyResponse {

    private final int FETCH_LIVE_ASTROLOGER = 1;
    private RequestQueue queue;
    private String REQ_TAG = "FetchLiveAstro";
    private String serviceCallingFrom = "";
    public PreFetchLiveAstroDataservice() {
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            if(intent!=null)
                serviceCallingFrom = intent.getStringExtra(CGlobalVariables.CALL_SOURCE);
        }catch (Exception e){
            //
        }
        queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();
        getLiveAstrologerList();
    }

    private void getLiveAstrologerList() {
        boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
        if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanager
            return;
        }
        if (CUtils.isConnectedWithInternet(this)) {
            //Log.e("SAN PF LiveAstro URL1=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
//                    PreFetchLiveAstroDataservice.this, false, CUtils.getLiveAstroParams(this,serviceCallingFrom), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
//            queue.add(stringRequest);
            astrologerLiveListRequest();
        }
    }

    private void astrologerLiveListRequest(){
        String offerType = CUtils.getCallChatOfferType(this);
        String phoneNo = "";
        String countryCode = "";

        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this);
        try {
            if (isLogin) {
                phoneNo = CUtils.getUserID(this);
                countryCode = CUtils.getCountryCode(this);
            }
        } catch (Exception e) {
            //
        }
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerLiveList(CUtils.getLiveAstroParams(this,serviceCallingFrom));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        try {
                            CUtils.setApiLastHitTime();
                            String myResponse = response.body().string();
                            //Log.e("SAN PF LiveAstro Res=>", "1.1");
                            CUtils.saveLiveAstroList(myResponse);

                            Context context = AstrosageKundliApplication.getAppContext();
                            Intent intent = new Intent(CGlobalVariables.LIVE_ASTRO_BROAD_ACTION);
                            intent.putExtra("isLiveAstro", true);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            //Log.e("SAN PF LiveAstro Res=>", "2");
                        } catch (Exception e) {
                            //Log.e("SAN PF LiveAstro Res=>", "err" + e.toString());
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }


    @Override
    public void onResponse(String response, int method) {
        //Log.e("SAN PreFetch Res=>", response);
        if (response != null && response.length() > 0) {
            if (method == FETCH_LIVE_ASTROLOGER) {
                //Log.e("SAN PF LiveAstro Res=>", "1");
//                try {
//                    CUtils.setApiLastHitTime();
//                    //Log.e("SAN PF LiveAstro Res=>", "1.1");
//                    CUtils.saveLiveAstroList(response);
//                    Context context = AstrosageKundliApplication.getAppContext();
//                    Intent intent = new Intent(CGlobalVariables.LIVE_ASTRO_BROAD_ACTION);
//                    intent.putExtra("isLiveAstro", true);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                    //Log.e("SAN PF LiveAstro Res=>", "2");
//                } catch (Exception e) {
//                    //Log.e("SAN PF LiveAstro Res=>", "err" + e.toString());
//                }
            }

        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("SAN PreFetch error=>", error.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
