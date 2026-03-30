package com.ojassoft.astrosage.ui.JobServices;

import static com.ojassoft.astrosage.utils.CGlobalVariables.CHART_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MAKE_CHART_DEFAULT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.activity.OtpVerifyActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class MakeDefaultService extends Worker implements VolleyResponse {

    private Context context;

    RequestQueue queue;

    int MAKE_CHART_METHOD = 33;

    public MakeDefaultService(@NonNull Context appContext, @NonNull WorkerParameters params) {
        super(appContext, params);
        context = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        //Log.e("SAN ", " MKD MDS doWork() " );
        if (context == null) {
            context = AstrosageKundliApplication.getAppContext();
        }

        queue = VolleySingleton.getInstance(context).getRequestQueue();

        String chartid = getInputData().getString("chartid");
        //Log.e("SAN ", " MKD MDS doWork() chartid " + chartid );
        if ( !TextUtils.isEmpty( chartid ) ) {
            makeDefaultKundli(MAKE_CHART_DEFAULT, MAKE_CHART_METHOD, chartid);
        }

        return Result.success();
    }

    @Override
    public void onStopped() {}

    private void makeDefaultKundli(String url, int method, String chartId) {
        //Log.e("SAN ", " MKD MDS url " + url );
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                MakeDefaultService.this, false, getParams(chartId), method).getMyStringRequest();
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public Map<String, String> getParams(String chartId) {

       String userId = com.ojassoft.astrosage.utils.CUtils.getUserName(context);
       String pwd = com.ojassoft.astrosage.utils.CUtils.getUserPassword(context);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.KEY_API, com.ojassoft.astrosage.utils.CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.KEY_USER_ID, userId);
        headers.put(CGlobalVariables.KEY_USER_PASSWORD, pwd);
        headers.put(CHART_ID, chartId );
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.e("SAN ", " MKD MDS params " + headers.toString() );
        return headers;
    }


    @Override
    public void onError(VolleyError error) {
        //Log.e("SAN ", " MKD MDS volley onError " + error.toString() );
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.e("SAN ", " MKD MDS onResponse " + response );
        if (response != null && response.length() > 0) {

            if (method == MAKE_CHART_METHOD) {

            }
        }
    }



}
