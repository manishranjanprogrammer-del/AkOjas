package com.ojassoft.astrosage.misc;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import java.util.Map;

public class VolleyServiceHandler implements com.android.volley.Response.Listener<String>, Response.ErrorListener {

    private VolleyResponse volleyResponse;
    //private String tag;
    private MyStringRequest stringRequest;


    public VolleyServiceHandler(int methodType, String url, VolleyResponse volleyResponse, boolean isCache, Map<String, String> params, int method) {
        this.volleyResponse = volleyResponse;
        stringRequest = new MyStringRequest(methodType, url, this, this, params, method);
        int socketTimeout = 60000;//30 seconds - change to what you want
        com.android.volley.RetryPolicy policy = new com.android.volley.DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(isCache);
    }

    public VolleyServiceHandler(int methodType, String url, VolleyResponse volleyResponse, boolean isCache, Map<String, String> params, Map<String, String> headerParams, int method) {
        this.volleyResponse = volleyResponse;
        stringRequest = new MyStringRequest(methodType, url, this, this, params, headerParams, method);
        int socketTimeout = 60000;//30 seconds - change to what you want
        com.android.volley.RetryPolicy policy = new com.android.volley.DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(isCache);
    }


    @Override
    public void onErrorResponse(com.android.volley.VolleyError error) {
        volleyResponse.onError(error);
    }

    public MyStringRequest getMyStringRequest() {
        return stringRequest;
    }

    @Override
    public void onResponse(String s) {
        volleyResponse.onResponse(s, stringRequest.getMethodNumber());
    }
}

