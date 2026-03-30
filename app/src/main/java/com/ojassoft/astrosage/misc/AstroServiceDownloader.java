package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
//import com.google.analytics.tracking.android.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas on ६/७/१६.
 */
public class AstroServiceDownloader extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     * private RequestQueue queue;
     */
    private RequestQueue queue;
    private Context ctx;
    public ArrayList<ServicelistModal> data = new ArrayList();
    static final public String BROAD_ACTION = "com.ojassoft.astrosage.misc.ASTROLOGER_SERVICE_LIST";
    static final public String BROAD_RESULT = "data";
    private LocalBroadcastManager broadcast;

    public AstroServiceDownloader() {
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
        loadAstroShopServiceData("");
    }


    private void loadAstroShopServiceData(final String astroId) {
        final int LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopServiceList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //        //Log.e("Simple+" + response.toString());
                        if (response != null && !response.isEmpty()) {
                            try {
                                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("Result")) {
                                    return;
                                }
                                CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), response);
                                parseGsonData(response);

                            } catch (Exception e) {
                                CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), "");
                                e.printStackTrace();
                            }

                        } else {
                            CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), "");
                        }

//                        data = gson.fromJson(response, new TypeToken<ArrayList<AstroShopMaindata>>() {
//                        }.getType());
                        //    //Log.e("Size returned" + data.get(0).getGemStones().size());
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
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
                int LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                        .getLanguageCode();
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ctx));
                headers.put("profile_Id", astroId);
                headers.put("langcode", "" + LANGUAGE_CODE);

                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(AstroServiceDownloader.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ctx)));

                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                // headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);

                //Log.e("Post data servicelist ", headers.toString());
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
        data = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
        }.getType());
        sendResult(data);
    }

    public void sendResult(ArrayList<ServicelistModal> data) {
        Intent intent = new Intent(BROAD_ACTION);
        if (data != null)
            intent.putExtra(BROAD_RESULT, data);
        broadcast.sendBroadcast(intent);
    }

}
