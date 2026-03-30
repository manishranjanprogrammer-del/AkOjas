package com.ojassoft.astrosage.varta.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
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
import retrofit2.Response;

public class AIRandomChatAstroService extends IntentService {

    private Context context;
    private int LANGUAGE_CODE = 0;

    public AIRandomChatAstroService() {
        super("app_name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            context = AstrosageKundliApplication.getAppContext();
            LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_GET_RANDOM_AI_ASTROLOGER, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_API_CALLED, "");
            getAstroAiRandomChat();
            // sendGzipRequest(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_LIST_URL, getAtroParams());
        } catch (Exception e) {
            //
        }
    }

    private void getAstroAiRandomChat() {
        if (!CUtils.isConnectedWithInternet(this)) {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.aiChatInitiate(getAiChatParam());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //stopProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        Log.d("TestNewApi", "myResponse==>>>>   " + jsonObject);
                        String status = jsonObject.getString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS);
                        if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS_SUCESS)) {
//                            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
//                            astrologerDetailBean.setName(jsonObject.getString("n"));
//                            astrologerDetailBean.setImageFile(jsonObject.getString("if"));
//                            astrologerDetailBean.setAstroWalletId(jsonObject.getString("wi"));
//                            astrologerDetailBean.setUrlText(jsonObject.getString("urlt"));
//                            astrologerDetailBean.setCallSource("ActAppModule.class");
//                            astrologerDetailBean.setFreeForChat(true);
//                            astrologerDetailBean.setAiAstrologerId("12");
//                            astrologerDetailBean.setAstrologerId("13686");
//                            astrologerDetailBean.setAiAstrologerId(jsonObject.optString("aiai"));
//                            astrologerDetailBean.setAstrologerId(jsonObject.optString("ai"));
//                            if (CUtils.isChatNotInitiated() && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId())) {
//                                ChatUtils.getInstance(this).initAIChat(astrologerDetailBean);
//                            } else {
//                                Toast.makeText(activity, activity.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
//                            }


                            String imgUrl = CGlobalVariables.IMAGE_DOMAIN+jsonObject.optString("ifl");
                            //Log.d("TestNewApi", "imgUrl   " + imgUrl);
                           
                            Glide.with(AIRandomChatAstroService.this).load(imgUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .preload();

                            com.ojassoft.astrosage.varta.utils.CUtils.saveAiRandomChatAstroDeatils(jsonObject.toString());
                            Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_ASTRO_RANDOM_CHAT_DETAILS);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        }

                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents("ai_chat_initiate_exception", AstrosageKundliApplication.currentEventType, "");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //stopProgressBar();
                    // callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
                }
            });

        }
    }

    private Map<String, String> getAiChatParam() {
        HashMap<String, String> headers = new HashMap<String, String>();
        Context context = AstrosageKundliApplication.getAppContext();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(context));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));

        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(context));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, "");
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        return headers;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

