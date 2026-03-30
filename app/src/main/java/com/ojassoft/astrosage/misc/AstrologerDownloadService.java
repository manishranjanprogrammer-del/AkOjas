package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
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
//import com.google.analytics.tracking.android.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.model.AstrologerInfo;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas on ६/७/१६.
 */
public class AstrologerDownloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     * private RequestQueue queue;
     */
    private RequestQueue queue;
    private Context ctx;
    public ArrayList<AstrologerInfo> data = new ArrayList();
    static final public String BROAD_ACTION = "com.ojassoft.astrosage.misc.ASTROLOGER_LIST";
    static final public String BROAD_RESULT = "data";
    private LocalBroadcastManager broadcast;

    public AstrologerDownloadService() {
        super(AstroShopDataDownloadService.class.getName());
    }

    @Override
    public void onCreate() {
        ///  queue = VolleySingleton.getInstance(this).getRequestQueue();

        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        ctx = this;
        broadcast = LocalBroadcastManager.getInstance(ctx);
        downloadAstrologerDetails();
    }


    private void downloadAstrologerDetails() {

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astrologerLive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //        //Log.e("Simple+" + response.toString());
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("inService" + element.toString());
                            parseGsonData(response);

                        }
                        else
                        {
                            Cache cache = VolleySingleton.getInstance(ctx).getRequestQueue().getCache();
                            cache.remove(CGlobalVariables.astrologerLive);
                        }
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   mTextView.setText("That didn't work!");

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


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ctx));
                //   headers.put("Key","9865");
                return headers;
            }

        };


        ;
// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void parseGsonData(String saveData) {
        Gson gson = new Gson();
        data = gson.fromJson(saveData, new TypeToken<ArrayList<AstrologerInfo>>() {
        }.getType());
        sendResult(data);
    }

    public void sendResult(ArrayList<AstrologerInfo> data) {
        Intent intent = new Intent(BROAD_ACTION);
        if (data != null)
            intent.putExtra(BROAD_RESULT, data);
        broadcast.sendBroadcast(intent);
    }

}
