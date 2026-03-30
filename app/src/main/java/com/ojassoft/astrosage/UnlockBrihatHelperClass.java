package com.ojassoft.astrosage;

import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CUtils.saveAppInstallTime;
import static com.ojassoft.astrosage.utils.CUtils.saveAppInstallTimeRecorded;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.service.Loginservice;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlockBrihatHelperClass {

    Context context;
    InstallTimeCallBack installTimeCallBack;

    public UnlockBrihatHelperClass(Context context){
        this.context = context;
    }


    public void handleFirstTimeInstall() {
        if (!CUtils.IsAppInstallTimeRecorded(context)) {
            checkServerForInstallTime(null);
        }
    }
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                checkServerForInstallTime(installTimeCallBack);
            }
            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };
    public void checkServerForInstallTime(InstallTimeCallBack installTimeCallBack) {
        this.installTimeCallBack = installTimeCallBack;

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.setAppInstallTime(getInstallTimeRequestParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() !=null) {
                        String myResponse = response.body().string();

                        Log.e("InputEmail", "onResponse: " +myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        //Log.e("TestProfile", "myResponse= "+jsonObject);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            String installTIme = jsonObject.getString("time");
                            saveAppInstallTime(context,installTIme);
                            saveAppInstallTimeRecorded(context);
                            if(installTimeCallBack!=null){
                                installTimeCallBack.onGetInstallTime(installTIme);
                            }
                            Log.e("InputEmail", "onResponse: time : "+installTIme );
                        }else if (status.equals("100")) {
                            LocalBroadcastManager.getInstance(context).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                            try {
                                if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(context)) {
                                    Intent intent = new Intent(context, Loginservice.class);
                                    context.startService(intent);
                                }
                            } catch (Exception e) {/**/}

                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private Map<String, String> getInstallTimeRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("countrycode", com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context));
        map.put("phoneno", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(context));
        map.put("userid", CUtils.getUserName(context));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(context));
        map.put("username", " ");
//        long millis = System.currentTimeMillis();
//        long twentyFiveHoursBack = millis - 150000000L;//TODO remove before committing
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        String formattedDate = sdf.format(new Date(System.currentTimeMillis()));
        map.put("installtime",formattedDate);
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION,BuildConfig.VERSION_NAME);
        map.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID,com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(context));
        return map;
    }
    public static interface InstallTimeCallBack{
        void onGetInstallTime(String installTime);
    }
}

