package com.ojassoft.astrosage.varta.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;

import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.OtpVerifyActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_REFUND_FREE_CHAT;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class Loginservice extends IntentService implements VolleyResponse {
    private RequestQueue queue;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public Loginservice() {
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Log.e("LoginResponse1", "Loginservice started");
        //Log.e("SAN CHAT ", " Loginservice onHandleIntent() " );
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        //String url = CGlobalVariables.VERIFY_LOGIN_URL;
        String _pwd = CUtils.getUserLoginPassword(this);
        //Log.e("LoginFlow", "_pwd="+_pwd);
        if(TextUtils.isEmpty(_pwd)){
            //Log.e("LoginResponse1", "onUserLogout1");
            onUserLogout();
            return;
        }
        if (CGlobalVariables.chatTimerTime != 0) {
            return; //avoid api-hit during ongoing chat
        }
        callBackgroundLoginApi(_pwd);
        //Log.e("SAN CHAT ", " Loginservice onHandleIntent() url  " + url );
//        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                Loginservice.this, false, getVerifyLoginParams(_pwd), 0).getMyStringRequest();
//        stringRequest.setShouldCache(false);
//        queue.add(stringRequest);
        CUtils.fcmAnalyticsEvents("bg_login_called_api",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

    }

    private void callBackgroundLoginApi(String _pwd) {

        Context context = AstrosageKundliApplication.getAppContext();

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.backgroundLogin(getVerifyLoginParams(_pwd));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        try {
                            parseLoginResponse(response.body().string(), 0);
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

    @Override
    public void onResponse(String response, int method) {
        //Log.d("LoginFlow", " onResponse1: " + response);
        //Log.e("LoginResponse1", "resp="+response);
        //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        //Log.e("SAN CHAT ", " Loginservice onResponse() parseLoginResponse() " );


        //parseLoginResponse(response, method);
    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("LoginResponse1", "error="+error.toString());
        sendBroadcastMesage(CGlobalVariables.FAIL);
    }

    private void parseLoginResponse(String response, int method) {
        Log.d("TestLogin","parseLoginResponse Service = " +response );
        //Log.e("LoadMore back login  ",response);
        if (response != null && response.length() > 0) {
            if (method == 0) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        //login success
                        CUtils.fcmAnalyticsEvents("bg_varta_login_success",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                        boolean liveintrooffer = jsonObject.getBoolean("liveintrooffer");
                        String privateintrooffertype  = jsonObject.getString("privateintrooffertype");
                        boolean isSecondFreeChat  = jsonObject.optBoolean("sfc");
                        //Log.d("TestOfferType", "offerType1="+privateintrooffertype);
                        //Log.e("SAN CHAT ", " Loginservice parseLoginResponse() offertype privateintrooffertype  " + privateintrooffertype );
                        CUtils.setUserOffers(this, liveintrooffer, privateintrooffertype);
                        CUtils.setSecondFreeChat(this, isSecondFreeChat);
                        /*
                        if ( privateintrooffertype.equalsIgnoreCase(INTRO_OFFER_TYPE_FREE) ){
                            sendBroadcastRefundFreeChat();
                        }*/
//                        CUtils.setUserIdForBlock(jsonObject.getString("userid"));
                        CUtils.setUserIdForBlock(this,jsonObject.getString("userid"));
                        String blockedby = jsonObject.getString("blockedby");
                        String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
                        CUtils.setblockByAstrologerList(blockByAstrologer);

                        sendBroadcastMesage(CGlobalVariables.SUCCESS);
                        /*String subscribeFollowDate = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FOLOW_TOPIC_SUBSCRIBE_DATE, "");
                        String todayDate = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.ONE_DAY);
                        if(TextUtils.isEmpty(subscribeFollowDate) || !todayDate.equalsIgnoreCase(subscribeFollowDate)){
                            Log.d("unfolist", "parseLoginResponse: first time");
                            CUtils.startFollowerSubscriptionService(this);
                            CUtils.saveStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FOLOW_TOPIC_SUBSCRIBE_DATE, todayDate);
                        }*/
                        CUtils.startFollowerSubscriptionService(this);

                        String bonustitle = "", bonusdescription = "";
                        if (jsonObject.has("bonustitle")) {
                            bonustitle = jsonObject.getString("bonustitle");
                        }
                        if (jsonObject.has("bonusdescription")) {
                            bonusdescription = jsonObject.getString("bonusdescription");
                        }

                        if (!TextUtils.isEmpty(bonustitle) && !TextUtils.isEmpty(bonusdescription)) {
                            boolean isNotificationShown = CUtils.getBooleanData(Loginservice.this,CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN,false);
                            if (!isNotificationShown) {
                                CUtils.saveBooleanData(Loginservice.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, true);
                                CreateCustomLocalNotification notification = new CreateCustomLocalNotification(this);
                                notification.showLocalNotification(bonustitle, bonusdescription, true);
                            }
                        }

                    } else {
                        CUtils.fcmAnalyticsEvents("bg_varta_login_fail",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");
                        //Log.e("LoginResponse1", "onUserLogout2");
                        onUserLogout();
                        sendBroadcastMesage(CGlobalVariables.FAIL);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onUserLogout() {
        try {
            CUtils.clearAllSharedPreferences(this);
            CUtils.saveLoginDetailInPrefs(Loginservice.this, "", false, "", "0");
            CUtils.setUserLoginPassword(Loginservice.this, "");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Loginservice.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.LOGOUT_BTN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Loginservice.this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastMesage(String status) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN);
            intent.putExtra("status", status);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastRefundFreeChat() {
        try {
            //Log.e("SAN BGLogin ", "sendBroadcastRefundFreeChat()");
            Context context = this;
            Intent intent = new Intent(SEND_BROADCAST_REFUND_FREE_CHAT);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
