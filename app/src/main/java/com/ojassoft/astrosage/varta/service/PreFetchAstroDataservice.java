package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.GZipRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class PreFetchAstroDataservice extends IntentService {

    private Context context;
    private int LANGUAGE_CODE = 0;

    public PreFetchAstroDataservice() {
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            context = AstrosageKundliApplication.getAppContext();
            LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
            astrologerListRequest();
           // sendGzipRequest(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_LIST_URL, getAtroParams());
        }catch (Exception e){
            //
        }
    }
        private void astrologerListRequest(){
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getAstrologerListMiniList(getAtroParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.has("astrologers") && jsonObject.getJSONArray("astrologers").length() > 0) {
                               // Log.d("getAstrologerListItems","Called 123"+jsonObject);
                                com.ojassoft.astrosage.varta.utils.CUtils.saveAstroListWithLimit(jsonObject.toString());
                                Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_ASTRO_BROAD_ACTION);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
//    private void sendGzipRequest(String url, final HashMap<String, String> paramsHashMap) {
//
//        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
//
//        GZipRequest postRequest = new GZipRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response != null && response.length() > 0) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                if (jsonObject.has("astrologers") && jsonObject.getJSONArray("astrologers").length() > 0) {
//                                    com.ojassoft.astrosage.varta.utils.CUtils.saveAstroListWithLimit(response);
//                                    Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_ASTRO_BROAD_ACTION);
//                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                                }
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                       // Log.d("TestResponse",error.toString()+"hjkhk");
//
//                    }
//                }
//        ) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            public Map<String, String> getParams() {
//                return paramsHashMap;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.putAll(super.getHeaders());
//                params.put("Accept-Encoding", "gzip,deflate");
//                return params;
//            }
//        };
//
//        int socketTimeout = 60000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        postRequest.setShouldCache(true);
//        queue.add(postRequest);
//    }

    public HashMap<String, String> getAtroParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));
        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this);
        String offerType = CUtils.getCallChatOfferType(this);
        try {
            if (isLogin) {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
            } else {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, "");
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OFFER_TYPE,offerType);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FETCHALL, "1");
        headers.put(CGlobalVariables.PACKAGE_NAME, "" + BuildConfig.APPLICATION_ID);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISMINASTROLIST, "1");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        return headers;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
