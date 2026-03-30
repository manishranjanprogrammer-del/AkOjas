/**
 * Requirements:
 * 1. Internet permission in AndroidManifest.xml.
 * 2. Dependencies: Retrofit for network calls, and a JSON parsing library.
 * 3. `CUtils` class must provide necessary user and app data.
 * 4. `ApiList` interface must define the `isAIPassSubscriber` endpoint.
 * 5. `RetrofitClient` must be configured to handle network requests.
 */
package com.ojassoft.astrosage.misc;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ServiceToGetIsAIPassSubscriber is an IntentService that runs in the background to check
 * if the current user is a subscriber to the AI Pass feature. It makes a network request
 * to the backend, parses the response, and saves the subscription status locally using CUtils.
 */

public class ServiceToGetIsAIPassSubscriber extends IntentService {
    boolean loopFlag = false;
    private static final String TAG = "AIPassSubscriber";

    /**
     * Constructor for the service.
     */
    public ServiceToGetIsAIPassSubscriber() {
        super("ServiceToGetIsAIPassSubscriber");
    }

    /**
     * This method is called when the service is started with an intent.
     * It triggers the process to fetch the AI Pass subscriber status.
     *
     * @param intent The intent that started the service.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            fetchAIPassSubscriberStatus();
        }
    }

    /**
     * Fetches the AI Pass subscriber status from the server.
     * It uses Retrofit to make an asynchronous API call.
     */
    private void fetchAIPassSubscriberStatus() {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.isAIPassSubscriber(getAIPassParams());

        // Enqueue the call to run asynchronously
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String myResponse = response.body().string();

                        JSONObject obj = new JSONObject(myResponse);
//                        Log.d("TestAiPass","isAIPassSubscriber ojb==>>"+obj);
                        // Check if the response contains the subscriber status
                        if (obj.has("isaipasssubscriber")) {
                            int isSubscriber = obj.getInt("isaipasssubscriber");
                            // Save the subscription status locally
                            if (isSubscriber == 1) {
                                sendBroadcastMessage();
                                CUtils.setIsKundliAiProPlan(ServiceToGetIsAIPassSubscriber.this, true);
                            } else {
                                CUtils.setIsKundliAiProPlan(ServiceToGetIsAIPassSubscriber.this, false);

                            }
                        }
                        if(obj.has("status")){
                            String status=obj.getString("status");
                            //Log.d("TestAiPass","isAIPassSubscriber status==>>"+status);
                          if(status.equals("100")){
                              startBackgroundLogin();
                          }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "AI Pass Subscriber e " + e);
                    }
                } else {
                    Log.e(TAG, "API call failed or returned null response.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    // Initiates a background login to re-authenticate the user.
    private void startBackgroundLogin() {
        String _pwd = CUtils.getUserLoginPassword(this);
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.backgroundLogin(getVerifyLoginParams(_pwd));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        try {
                            parseLoginResponse(response.body().string());
                            if(!loopFlag){
                                fetchAIPassSubscriberStatus();
                                loopFlag = true;
                            }

                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }
    // Parses the response from the background login API call.
    private void parseLoginResponse(String response) {
        //Log.e("LoadMore back login  ",response);
        if (response != null && response.length() > 0) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        boolean liveintrooffer = jsonObject.getBoolean("liveintrooffer");
                        String privateintrooffertype  = jsonObject.getString("privateintrooffertype");
                        boolean isSecondFreeChat  = jsonObject.optBoolean("sfc");
                         CUtils.setUserOffers(this, liveintrooffer, privateintrooffertype);
                        CUtils.setSecondFreeChat(this, isSecondFreeChat);
                        CUtils.setUserIdForBlock(this,jsonObject.getString("userid"));
                        String blockedby = jsonObject.getString("blockedby");
                        String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
                        CUtils.setblockByAstrologerList(blockByAstrologer);
                        CUtils.startFollowerSubscriptionService(this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }
    // Sends a local broadcast to notify the app about the AI Pass subscription status.
    private void sendBroadcastMessage() {
        try {
            Log.d("TestAiPass","sendBroadcastMessage");
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.AI_PASS_ASTRO_BROAD_ACTION);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Prepares the parameters for the API call.
     *
     * @return A map containing the required parameters like country code, phone number, etc.
     */
    private Map<String, String> getAIPassParams() {
        Map<String, String> map = new HashMap<>();
        map.put("countrycode", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
        map.put("phoneno", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_USER_ID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(this));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(map);
    }
    // Prepares parameters for the user login verification.
    public static Map<String, String> getVerifyLoginParams(String _pwd) {

        Context context = AstrosageKundliApplication.getAppContext();
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        String deviceId = CUtils.getMyAndroidId(context);
        String key = CUtils.getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        params.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        params.put(KEY_PASSWORD, _pwd);
        params.put(CGlobalVariables.KEY_API, key);
        params.put(DEVICE_ID, deviceId);
        params.put("packageName", BuildConfig.APPLICATION_ID);
        params.put(CGlobalVariables.PROPERTY_APP_VERSION, BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("TestFreeIssue", "params="+params);
        // Log.e("SAN CHAT ", " Loginservice params " + params.toString() );
        return params;
    }

}
