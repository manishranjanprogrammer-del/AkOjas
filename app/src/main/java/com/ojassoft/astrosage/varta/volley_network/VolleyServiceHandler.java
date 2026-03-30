package com.ojassoft.astrosage.varta.volley_network;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.Map;

public class VolleyServiceHandler implements Response.Listener<String>, Response.ErrorListener {

    private VolleyResponse volleyResponse;
    //private String tag;
    private MyStringRequest stringRequest;


    public VolleyServiceHandler(int methodType, String url, VolleyResponse volleyResponse, boolean isCache, Map<String, String> params, int method) {
        this.volleyResponse = volleyResponse;
        try {
            params.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(AstrosageKundliApplication.getAppContext()));
            params.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
            params.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(AstrosageKundliApplication.getAppContext()));
        }catch (Exception e){
            //
        }
        stringRequest = new MyStringRequest(methodType, url, this, this, params, method);
        int socketTimeout = 30000;//30 seconds - change to what you want
        com.android.volley.RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(isCache);
    }

    public VolleyServiceHandler(int methodType, String url, VolleyResponse volleyResponse, boolean isCache, Map<String, String> params, Map<String, String> headerParams, int method) {
        this.volleyResponse = volleyResponse;
        stringRequest = new MyStringRequest(methodType, url, this, this, params, headerParams, method);
        int socketTimeout = 30000;//30 seconds - change to what you want
        com.android.volley.RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(isCache);
    }


    @Override
    public void onErrorResponse(com.android.volley.VolleyError error) {
        try {
            volleyResponse.onError(error);
        } catch (Exception e){
            //
        }
    }

    public MyStringRequest getMyStringRequest() {
        return stringRequest;
    }

    @Override
    public void onResponse(String s) {
/*        try {
            s = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) { }*/
        volleyResponse.onResponse(s, stringRequest.getMethodNumber());
    }

}