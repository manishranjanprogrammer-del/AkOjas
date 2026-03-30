package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.NameHoroscopeMatchingModel;
import com.ojassoft.astrosage.model.NameSwarCombModel;
import com.ojassoft.astrosage.model.NameSwarModel;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class NameMatchingBaseActivity extends BaseInputActivity {

    protected Activity currentActivity;
    protected Context context;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private RequestQueue queue;
    private CustomProgressDialog pd;

    protected abstract void initContext();

    public NameSwarCombModel nameSwarCombModel;
    public NameSwarModel nameBoySwarModel;
    public NameSwarModel nameGirlSwarModel;

    public NameMatchingBaseActivity(int titleRes) {
        super(titleRes);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(currentActivity).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        initContext();
    }


    /**
     * get name matching detail
     */
    public void getMatchingDetails(final String boyNameStr, final String girlNameStr) {

        showProgressBar();
        final String url = CGlobalVariables.nameMatchingApiUrl;

        Log.e("LoadMore URL ", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        Log.e("LoadMore URL ", response);
                        setMatchingResult(response);
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                hideProgressBar();
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(context));
                headers.put("languageCode", LANGUAGE_CODE + "");
                headers.put("boyName", boyNameStr);
                headers.put("girlName", girlNameStr);

                if (nameSwarCombModel != null) {
                    headers.put("boyNakshtra", nameSwarCombModel.getBoyNakshtra());
                    headers.put("boyCharan", nameSwarCombModel.getBoyCharan());
                    headers.put("girlNakshtra", nameSwarCombModel.getGirlNakshtra());
                    headers.put("girlCharan", nameSwarCombModel.getGirlCharan());
                } else if (nameBoySwarModel != null && nameGirlSwarModel != null) {
                    headers.put("boyNakshtra", nameBoySwarModel.getNakshtranumber());
                    headers.put("boyCharan", nameBoySwarModel.getCharan());
                    headers.put("girlNakshtra", nameGirlSwarModel.getNakshtranumber());
                    headers.put("girlCharan", nameGirlSwarModel.getCharan());
                }

                //Log.e("headers = ", headers.toString());
                return headers;
            }

        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        //stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void showProgressBar() {
        pd = new CustomProgressDialog(this, regularTypeface);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected NameHoroscopeMatchingModel parseData(String response) {
        Gson gson = new Gson();
        NameHoroscopeMatchingModel matchingModel = gson.fromJson(response, NameHoroscopeMatchingModel.class);
        return matchingModel;
    }

    protected void setMatchingResult(String response) {

    }

}
