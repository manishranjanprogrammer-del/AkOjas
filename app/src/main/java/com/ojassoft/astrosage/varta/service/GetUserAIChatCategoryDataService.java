package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_USER_CATEGORY;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GetUserAIChatCategoryDataService extends IntentService {

    private Context context;
    private String offerType = "";
    private String channelID = "";
    private String source = "";

    public GetUserAIChatCategoryDataService() {
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        context = AstrosageKundliApplication.getAppContext();
        if(intent != null){
            offerType = intent.getStringExtra(CGlobalVariables.OFFER_TYPE);
            channelID =  intent.getStringExtra(CGlobalVariables.CHANNEL_ID);
            source = intent.getStringExtra(CGlobalVariables.CALL_SOURCE);
            Log.e("TestMemoryGen2", "offerType="+offerType+" channelID="+channelID+" source="+source);
            if(offerType == null){
                offerType = "";
            }
        }
        fetchUserChatCategoryData();
    }

    private void fetchUserChatCategoryData() {

        if (CUtils.isConnectedWithInternet(this)) {

            ApiList api = RetrofitClient.getAIInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getUserAIChatCategory(getParamsNew());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            try {
                                String myResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(myResponse);
                                Log.e("TestMemoryGen", "jsonObject: "+jsonObject);
                                String status = jsonObject.optString("status");
                                if (status.equals("1")) {
                                    String topic = jsonObject.optString("topic");
                                    if(!TextUtils.isEmpty(topic)){
                                        CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_USER_CATEGORY, topic, source);
                                        // FIRST UNSUBSCRIBE OLD TOPIC THEN SUBSCRIBE NEW TOPIC
                                        String oldTopic = CUtils.getStringData(context, TOPIC_USER_CATEGORY, "");
                                        if(!TextUtils.isEmpty(oldTopic)){
                                            CUtils.unSubscribeTopics("", TOPIC_USER_CATEGORY + oldTopic, context);
                                        }
                                        CUtils.subscribeTopics("", TOPIC_USER_CATEGORY+topic, context);
                                        CUtils.saveStringData(context, TOPIC_USER_CATEGORY, topic);
                                    }
                                }
                            } catch (Exception e) {
                                //Log.e("TestMemoryGen", "Exception2: "+e);
                            }
                        }
                    } catch (Exception e) {
                        //Log.e("TestMemoryGen", "Exception1: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TestMemoryGen", "onFailure: "+t);
                }
            });
        }
    }

    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));
            headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(this));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this));
            headers.put(KEY_PASSWORD, CUtils.getUserLoginPassword(this));
            headers.put(CGlobalVariables.CHANNEL_ID, channelID);
            headers.put(CGlobalVariables.OFFER_TYPE, offerType);
            headers.put(CGlobalVariables.ANDROID_ID, CUtils.getMyAndroidId(this));
            headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
            headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
            headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, CUtils.isSecondFreeChat(context)?"1":"0");
        } catch (Exception e) {
            //Log.e("PrefetchHistoryData", "headers ex: "+e);
        }
        //Log.e("TestMemoryGen", "headers: "+headers);
        return headers;
    }

}
