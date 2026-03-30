package com.ojassoft.astrosage.varta.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

//import com.google.analytics.tracking.android.Log;


/**
 * Created by ojas on ६/७/१६.
 */
public class CallStatusCheckService extends IntentService implements VolleyResponse {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     * private RequestQueue queue;
     */
    private RequestQueue queue;
    private Context ctx;
    static final public String BROAD_ACTION = "com.ojassoft.astrosage.misc.ASTROLOGER_SERVICE_LIST";
    static final public String BROAD_RESULT = "data";
    private LocalBroadcastManager broadcast;

    public CallStatusCheckService() {
        super(CallStatusCheckService.class.getName());
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

        String callsID = intent.getStringExtra(CGlobalVariables.CALLS_ID);
        //Log.e("LoadMore callid ", callsID);
        String mobNo = CUtils.getUserID(ctx);
        checkCallStatus(callsID);
    }

    private void checkCallStatus(String callID) {

            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CHECK_CALL_STATUS_URL,
                    CallStatusCheckService.this, false, getParams(callID), 1).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);

    }

    public Map<String, String> getParams(String callsID) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ctx));
        headers.put(CGlobalVariables.CALLS_ID, callsID);
        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
        return headers;
    }

    public void sendResult(String data) {
        Intent intent = new Intent(BROAD_ACTION);
        if (data != null)
            intent.putExtra(BROAD_RESULT, data);
        broadcast.sendBroadcast(intent);
    }

    @Override
    public void onResponse(String response, int method) {

        //Log.e("LoadMore response ", response);
        if(response != null && response.length()>0) {
            try {
                sendResult(response);
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    //com.ojassoft.astrosage.varta.utils.CUtils.updateChatCallOfferType(this, false);
                    String offerType = CUtils.getCallChatOfferType(this);
                    if (!TextUtils.isEmpty(offerType)) {
                        CUtils.saveAstroList("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            sendResult("");
        }
    }

    @Override
    public void onError(VolleyError error) {

    }
}
