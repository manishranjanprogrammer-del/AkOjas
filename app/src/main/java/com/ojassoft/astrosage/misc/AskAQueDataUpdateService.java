package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas on ६/७/१६.
 */
public class AskAQueDataUpdateService extends IntentService implements VolleyResponse {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     * private RequestQueue queue;
     */
    // private RequestQueue queue;
    private Context ctx;
    public ArrayList<ServicelistModal> data = new ArrayList();
    static final public String BROAD_ACTION = "com.ojassoft.astrosage.misc.ASK_A_QUE_DATAUPDATE";
    static final public String BROAD_RESULT = "data";
    private LocalBroadcastManager broadcast;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    InputStream is = null;
    String askAQuestionDataPage;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * .
     */


    public AskAQueDataUpdateService() {
        super(AstroShopDataDownloadService.class.getName());
    }

    @Override
    public void onCreate() {
        ///  queue = VolleySingleton.getInstance(this).getRequestQueue();

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        askAQuestionDataPage = intent.getStringExtra(CGlobalVariables.ask_A_Question_Data);
        ctx = this;
        LANGUAGE_CODE = ((AstrosageKundliApplication) ctx.getApplicationContext())
                .getLanguageCode();
        broadcast = LocalBroadcastManager.getInstance(ctx);
        String url = CGlobalVariables.astroShopServiceAskaQuestion;
        CUtils.vollyPostRequest(AskAQueDataUpdateService.this, url,
                getParams(String.valueOf(LANGUAGE_CODE), CUtils.getApplicationSignatureHashCode(this), CUtils.getAskAQuestionServiceTypeCodeFromPage(askAQuestionDataPage)), 0);
        // getData(CUtils.getAskAQuestionServiceTypeCodeFromPage(askAQuestionDataPage));

    }

    /*private List<NameValuePair> getNameValuePairs_Askquestion(
            String languagecode, String key, String serviceType) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("servicetype", serviceType));
        nameValuePairs.add(new BasicNameValuePair("languageCode", languagecode));
        nameValuePairs.add(new BasicNameValuePair("Key", key));

        nameValuePairs.add(new BasicNameValuePair("asuserid", CUtils.getUserName(ctx)));
        nameValuePairs.add(new BasicNameValuePair("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ctx))));
             nameValuePairs.add(new BasicNameValuePair(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ctx))));
             nameValuePairs.add(new BasicNameValuePair("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ctx))));

        //nameValuePairs.add(new BasicNameValuePair("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST));
        // nameValuePairs.add(new BasicNameValuePair("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST));
        return nameValuePairs;

    }*/

    Map<String, String> getParams(String languagecode, String key, String serviceType) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("servicetype", serviceType);
        params.put("languageCode", languagecode);
        params.put("Key", key);

        params.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ctx)));
        params.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ctx)));

        return params;
    }


    public void sendResult(String data) {
        Intent intent = new Intent(BROAD_ACTION);
        if (data != null)
            intent.putExtra(BROAD_RESULT, data);
        broadcast.sendBroadcast(intent);
    }


    /*private void getData(String serviceType) {
        try {
            HttpClient hc = CUtils.getNewHttpClient();
            HttpPost postMethod = new HttpPost(CGlobalVariables.astroShopServiceAskaQuestion);

            postMethod.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Askquestion(
                    String.valueOf(LANGUAGE_CODE), CUtils.getApplicationSignatureHashCode(this), serviceType), HTTP.UTF_8));
            HttpResponse httpResponse = null;

            httpResponse = hc.execute(postMethod);

            HttpEntity httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            System.out.println("Ask service=" + result);
            if (result != null && !result.isEmpty()) {
                // if(askAQuestionDataPage == null || askAQuestionDataPage.equals("")){
                // askAQuestionDataPage = CGlobalVariables.ask_A_Question_Android;
                // }
                CUtils.saveStringData(ctx, askAQuestionDataPage + String.valueOf(LANGUAGE_CODE), result);

                sendResult(result);
            } else {
                sendResult("");
            }


        } catch (Exception ex) {
            //android.util.//Log.i("Tag", "1 - " + ex.getMessage().toString());
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
    }*/

    @Override
    public void onResponse(String response, int method) {
        if (!TextUtils.isEmpty(response)) {
            // if(askAQuestionDataPage == null || askAQuestionDataPage.equals("")){
            // askAQuestionDataPage = CGlobalVariables.ask_A_Question_Android;
            // }
            CUtils.saveStringData(ctx, askAQuestionDataPage + String.valueOf(LANGUAGE_CODE), response);

            sendResult(response);
        } else {
            sendResult("");
        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.i("", error.getMessage());
        sendResult("");
    }
}
