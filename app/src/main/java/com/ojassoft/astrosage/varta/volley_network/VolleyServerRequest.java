package com.ojassoft.astrosage.varta.volley_network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Ankit Varshney
 * CreatedOn: 17/7/2019
 * Volley Server Request
 * Parameters Requires
 * #context
 * #callback
 * #url
 * #paramsHashMap
 */
public class VolleyServerRequest {

    private Context context;
    private OnVolleyResultListener mCallBack;

    public VolleyServerRequest(Context context, OnVolleyResultListener callback, String url, HashMap<String, String> paramsHashMap) {
        this.context = context;
        mCallBack = callback;
        setPostRequest(url, paramsHashMap);
    }

    private void setPostRequest(String url, final HashMap<String, String> paramsHashMap) {

        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        mCallBack.onVolleySuccess(response, VolleySingleton.getInstance(context).getRequestQueue().getCache());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", error.toString());

                        if (error instanceof TimeoutError) {
                            //VolleyLog.d("TimeoutError: " + error.getMessage());
                            //If timeout occur it will again hit the request for get the product list
                            //      loadAstroShopData();
                        } else if (error instanceof NoConnectionError) {
                            //VolleyLog.d("NoConnectionError: " + error.getMessage());
                        } else if (error instanceof AuthFailureError) {
                            //VolleyLog.d("AuthFailureError: " + error.getMessage());
                        } else if (error instanceof ServerError) {
                            //VolleyLog.d("ServerError: " + error.getMessage());
                        } else if (error instanceof NetworkError) {
                            //VolleyLog.d("NetworkError: " + error.getMessage());
                        } else if (error instanceof ParseError) {
                            //VolleyLog.d("ParseError: " + error.getMessage());
                        }
                        mCallBack.onVolleyError(error);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                return paramsHashMap;
            }
        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        postRequest.setShouldCache(true);
        queue.add(postRequest);
    }


}