package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.beans.AstroShopMaindata;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

////import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on २७/४/१६.
 *
 * Background service that fetches the Astroshop category payload, persists it
 * to shared preferences, and broadcasts completion to UI listeners.
 */
public class AstroShopDataDownloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     * private RequestQueue queue;
     */
    private RequestQueue queue;
    private Context ctx;
    public ArrayList<AstroShopMaindata> data = new ArrayList();
    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";

    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.COPA_MSG";
    private LocalBroadcastManager broadcast;

    public AstroShopDataDownloadService() {
        super(AstroShopDataDownloadService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Starts the fetch for the currently selected language, with English fallback.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        ctx = this;
        queue = VolleySingleton.getInstance(ctx).getRequestQueue();
        broadcast = LocalBroadcastManager.getInstance(ctx);
        final int languageCode = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        loadAstroShopData(languageCode, languageCode, false);
    }


    /**
     * Loads Astroshop data for the requested language and optionally falls back to English.
     *
     * @param languageCode     The language code to request from the server.
     * @param saveLanguageCode The language code to save the data under for UI consumption.
     * @param isFallback       Whether this request is a fallback attempt (prevents infinite retry).
     */
    private void loadAstroShopData(final int languageCode, final int saveLanguageCode, final boolean isFallback) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopItemsLive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //        //Log.e("Simple+" + response.toString());
                        if (response != null && !response.isEmpty()) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject obj = jsonArray.getJSONObject(0);
                                Gson gson = new Gson();
                                JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                                //Log.e("tag","Element" + element.toString());
                                String result = "";
                                if (obj.has("result")) {
                                    result = obj.getString("result");

                                }
                                if (result.isEmpty() && !result.equalsIgnoreCase("0") && !result.equalsIgnoreCase("2")) {
                                    /*byte ptext[] = response.getBytes();
                                    String value = new String(ptext, "UTF-8");
                                    response=value;*/

                                    String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                    //Log.e("wiConvert+", str);

                               /* byte ptext[] = response.getBytes();
                                String value = new String(ptext, "UTF-8");
                                response=value;*/
                                    response = str;
                                    CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Data + String.valueOf(saveLanguageCode), response);
                                    parseGsonData();
                                    //Log.e("tag","SERVICE        success");
                                } else {

                                    if (!isFallback && languageCode != CGlobalVariables.ENGLISH) {
                                        loadAstroShopData(CGlobalVariables.ENGLISH, saveLanguageCode, true);
                                        return;
                                    }
                                    CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Data + String.valueOf(saveLanguageCode), "");
                                    sendResult("0");
                                    //Log.e("tag","SERVICE        uncess");
                                }
                            } catch (Exception e) {
                                if (!isFallback && languageCode != CGlobalVariables.ENGLISH) {
                                    loadAstroShopData(CGlobalVariables.ENGLISH, saveLanguageCode, true);
                                    return;
                                }
                                CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Data + String.valueOf(saveLanguageCode), "");
                                sendResult("0");
                                e.printStackTrace();

                            }

                        } else {
                            if (!isFallback && languageCode != CGlobalVariables.ENGLISH) {
                                loadAstroShopData(CGlobalVariables.ENGLISH, saveLanguageCode, true);
                                return;
                            }
                            CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Data + String.valueOf(saveLanguageCode), "");
                            sendResult("0");
                        }

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("tag","SERVICE         Error Through" + error.getMessage());

                if (!isFallback && languageCode != CGlobalVariables.ENGLISH) {
                    loadAstroShopData(CGlobalVariables.ENGLISH, saveLanguageCode, true);
                    return;
                }
                CUtils.saveStringData(ctx, CGlobalVariables.Astroshop_Data + String.valueOf(saveLanguageCode), "");
                sendResult("0");

                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
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
                headers.put("langcode", "" + languageCode);
                //Log.e("Data",headers.toString());

                headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ctx)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ctx)));

                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                //headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);

                //   headers.put("Key","9865");
                return headers;
            }

        };
        //Log.e("tag","APISERVICE HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);


    }

    /**
     * Broadcasts a success signal once the payload is saved.
     */
    private void parseGsonData() {
       /* Gson gson = new Gson();
        JsonElement element = gson.fromJson(saveData.toString(), JsonElement.class);
        //Log.e("tag","Element" + element.toString());
        data = gson.fromJson(saveData, new TypeToken<ArrayList<AstroShopMaindata>>() {
        }.getType());*/


        sendResult("1");


    }

    /**
     * Sends a local broadcast result consumed by Astroshop UI.
     */
    public void sendResult(String id) {


        Intent intent = new Intent(COPA_RESULT);
        intent.putExtra(COPA_MESSAGE, id);
        broadcast.sendBroadcast(intent);
    }

}
