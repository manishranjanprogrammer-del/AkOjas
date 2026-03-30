package com.ojassoft.astrosage.varta.service;


import android.content.Context;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class AiVoiceCallObserverService extends Worker {

    private final Context context;

    public AiVoiceCallObserverService(@NotNull Context context, @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @Override
    public @NotNull Result doWork() {
        String callsId = getInputData().getString(CGlobalVariables.AGORA_CALLS_ID);
        String astrologerId = getInputData().getString(CGlobalVariables.ASTROLOGER_ID);

        HashMap<String, String> headers = new HashMap<>();
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.FAIL;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.FAILED);
        headers.put(CGlobalVariables.CHAT_DURATION, "0");
        headers.put(CGlobalVariables.CHANNEL_ID, callsId);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, CGlobalVariables.AUTO_ENDED);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(CGlobalVariables.LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_NAME, CUtils.getUserFullName(context));
        headers.put(CGlobalVariables.CALLS_ID, callsId);
        headers.put("activity", "AutoReleaseCallService");
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        try {
            Response<ResponseBody> response = RetrofitClient.getInstance().create(ApiList.class).endAIcall(headers).execute();
            if (response.isSuccessful()) {
                return Result.success();
            }else{
               return Result.failure();
            }
        }catch(Exception e){
            return Result.failure();
        }
    }
}
