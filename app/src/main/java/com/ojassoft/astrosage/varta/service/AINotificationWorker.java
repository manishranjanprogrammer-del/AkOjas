package com.ojassoft.astrosage.varta.service;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.libojassoft.android.utils.LibCGlobalVariables.UPDATE_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CUtils.prepareUserProfile;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.models.NotificationModel;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.custompushnotification.CustomAINotification;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AINotificationWorker extends Worker {

    private final String TAG = "AINotificationWorker";

    private final Context context;
    private final String methodName;
    private String title = "";

    public AINotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.methodName = workerParams.getInputData().getString("method_name");
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String today = methodName + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (!CUtils.getBooleanData(context,today,false)) {
                CUtils.saveBooleanData(context, today, true);
                callAINotificationAPI(methodName);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private void showNotification(String question) {
        CustomAINotification customAINotification = new CustomAINotification(context, question, title, true);
        customAINotification.loadNotification();
    }

    private void callAINotificationAPI(String method) {
        try {
            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).callAIServiceNotification(getServiceParams(method));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String mResponse = response.body().string();
                            //Log.d("method_name", "onResponse: "+mResponse);
                            JSONObject jsonObject = new JSONObject(mResponse);
                            String question;
                            if (jsonObject.has("status")) {
                                int status = jsonObject.getInt("status");
                                if (status == 1) {
                                    question = jsonObject.optString("message");
                                    title = jsonObject.optString("title");
                                    showNotification(question);
                                }
                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t);
                }
            });
        } catch (Exception e) {

        }
    }

    private Map<String, String> getServiceParams(String method) {

        UserProfileData userProfileData = prepareUserProfile(context);
        HashMap<String, String> postDataParams = new HashMap<String, String>();

        try {
            postDataParams.put("name", userProfileData.getName());
            postDataParams.put("sex", userProfileData.getGender());
            postDataParams.put("day", userProfileData.getDay());
            postDataParams.put("month", String.valueOf(Integer.parseInt(userProfileData.getMonth())));
            postDataParams.put("year", userProfileData.getYear());
            postDataParams.put("hrs", userProfileData.getHour());
            postDataParams.put("min", userProfileData.getMinute());
            postDataParams.put("sec", userProfileData.getSecond());
            postDataParams.put("dst", "0");
            postDataParams.put("place", userProfileData.getPlace());
            postDataParams.put("longdeg", userProfileData.getLongdeg());
            postDataParams.put("longmin", userProfileData.getLongmin());
            postDataParams.put("longew", userProfileData.getLongew());
            postDataParams.put("latdeg", userProfileData.getLatdeg());
            postDataParams.put("latmin", userProfileData.getLatmin());
            postDataParams.put("latns", userProfileData.getLatns());
            postDataParams.put("timezone", userProfileData.getTimezone());
            postDataParams.put("ayanamsa", "0");
            postDataParams.put("charting", "0");
            postDataParams.put("kphn", "0");
            postDataParams.put("languagecode", String.valueOf(com.ojassoft.astrosage.utils.CUtils.getLanguageCode(context)));
            postDataParams.put("androidid", CUtils.getMyAndroidId(context));
            postDataParams.put("methodname", method);
            postDataParams.put("key", CUtils.getApplicationSignatureHashCode(context));
            postDataParams.put("pkgname", BuildConfig.APPLICATION_ID);
            postDataParams.put("appversion", BuildConfig.VERSION_NAME);

        } catch (Exception e) {
            //
        }
        return postDataParams;
    }

}
