package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreFetchDataservice extends IntentService implements VolleyResponse {

    private RequestQueue queue;
    private final int FETCH_WALLET_RECHARGES = 1;
    private final int FETCH_BANNER = 2;
    private String url = "";

    public PreFetchDataservice() {
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();

        getBannerImgData();
        getRechargeAmounts(CUtils.getUserID(this));
    }

    private void getBannerImgData() {

        if (CUtils.isConnectedWithInternet(this)) {
            //Log.e("SAN PreFet Banner URL=>", CGlobalVariables.GET_BANNER_URL);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_BANNER_URL,
                    PreFetchDataservice.this, false, getBannerParams(), FETCH_BANNER).getMyStringRequest();
            queue.add(stringRequest);
        }
    }

    public Map<String, String> getBannerParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));

        String wallet = CUtils.getWalletRs(this);
        String codeValue = CGlobalVariables.BANNER_ONE_RUPEES;
        //Log.e("LoadMore wallet ", wallet);
        if (!(wallet.equals("0.0") || wallet == null)) {
            codeValue = CGlobalVariables.BANNER_WITHOUT_RUPEES;
        }
        headers.put(CGlobalVariables.WALLET_CODE, codeValue);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("LoadMore params ", headers.toString());
        return headers;
    }

    private void getRechargeAmounts(String mobNo) {
        if (CUtils.isConnectedWithInternet(PreFetchDataservice.this)) {
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi  = apiList.getRechargeServices(CUtils.getRechargeParams(PreFetchDataservice.this,mobNo));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body()!=null){
                        try {
                            AstrosageKundliApplication.isPrefetchServiceRunning = false;

                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if (jsonObject.has("services") && jsonObject.getJSONArray("services").length() > 0) {
                                CUtils.saveWalletRechargeData(myResponse);
                                Calendar calendar=Calendar.getInstance();
                                CUtils.saveStringData(PreFetchDataservice.this,CGlobalVariables.RECHARGE_SERVICES_DATE, String.valueOf(calendar.get(Calendar.DATE)));
                            }
                        }catch (Exception e){
                            //
                        }
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
        //Log.e("SAN PreFetch Res=>", response);
        if (response != null && response.length() > 0) {
            if (method == FETCH_BANNER) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("links") && jsonObject.getJSONArray("links").length() > 0) {
                        CUtils.saveBannerData(response);
                    }
                } catch (Exception e) {
                }
            } else if (method == FETCH_WALLET_RECHARGES) {
                //Log.e("SAN PreFet Wallet Res=>", response );
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.has("services") && jsonObject.getJSONArray("services").length() > 0) {
//                        CUtils.saveWalletRechargeData(response);
//                    }
//                } catch (Exception e) {
//                }
                // do nothing
            }

        }
    }

    @Override
    public void onError(VolleyError error) {

    }

}
