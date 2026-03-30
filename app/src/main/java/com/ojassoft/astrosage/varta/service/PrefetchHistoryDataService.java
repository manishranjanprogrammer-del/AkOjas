package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_HISTORY_LIST;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_SHOW_NEGATIVE_ASTRO;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.KundliModules_Frag;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PrefetchHistoryDataService extends IntentService implements VolleyResponse {

    private final int FETCH_LAST_CONSULTATIONS = 1;
    private RequestQueue queue;
    private ArrayList<CallHistoryBean> callHistoryList;
    private Context context;
    private boolean flag = true;

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                fetchHistoryData();
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    public PrefetchHistoryDataService(){
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        context = AstrosageKundliApplication.getAppContext();
        callHistoryList = new ArrayList<>();
        queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();
        fetchHistoryData();
    }

    private void fetchHistoryData(){

        if (CUtils.isConnectedWithInternet(this)) {
//            String url = CGlobalVariables.CONSULTATIONHISTORY;
//           StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    this, false, getParamsNew(), FETCH_LAST_CONSULTATIONS).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getRecentHistory(getParamsNew());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            try {
                                String myResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(myResponse);
                                if (jsonObject.has("status")) {
                                    if (jsonObject.getString("status").equalsIgnoreCase("100")) {
                                        startBackgroundLoginService();
                                    }
                                } else {
                                    com.ojassoft.astrosage.varta.utils.CUtils.saveHistoryList(myResponse);
                                    Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_ASTRO_BROAD_ACTION);
                                    intent.putExtra(IS_HISTORY_LIST, true);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                }
                            } catch (Exception e) {
                                //
                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                       //
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));
            headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(this));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this));
            headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this));
            headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
            headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
            headers.put(CGlobalVariables.IGNORE_ASTRO,"true");
            headers.put(NOT_SHOW_NEGATIVE_ASTRO,"1");
        } catch (Exception e) {
            //Log.e("PrefetchHistoryData", "headers ex: "+e);
        }
        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
//        if (method == FETCH_LAST_CONSULTATIONS){
//            Log.d("fsdfdsfsdf",response+"");
//            if (response != null && response.length() > 0) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.has("status")) {
//                        if (jsonObject.getString("status").equalsIgnoreCase("100")) {
//                            startBackgroundLoginService();
//                        }
//                    } else {
//                        com.ojassoft.astrosage.varta.utils.CUtils.saveHistoryList(response);
//                        Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_ASTRO_BROAD_ACTION);
//                        intent.putExtra(IS_HISTORY_LIST, true);
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                    }
//                } catch (Exception e) {
//                    //
//                }
//            } else {
//                //Log.e("PrefetchHistoryData", "response empty");
//            }
//        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("PrefetchHistoryData", "onError: "+error);
    }

    private void startBackgroundLoginService() {
        try {
            if(flag) {
                flag = false;
                LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                if (CUtils.getUserLoginStatus(this)) {
                    CUtils.fcmAnalyticsEvents("bg_login_from_consult_history_service",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                    Intent intent = new Intent(PrefetchHistoryDataService.this, Loginservice.class);
                    startService(intent);
                }
            } else {
                flag = true;
            }
        } catch (Exception e) {}
    }

}
