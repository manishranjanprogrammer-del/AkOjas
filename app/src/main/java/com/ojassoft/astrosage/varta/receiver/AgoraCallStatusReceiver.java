package com.ojassoft.astrosage.varta.receiver;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTRO_NO_ANSWER;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AgoraCallStatusReceiver extends BroadcastReceiver {

    private String intentActionType, channelId;
    private int intentNotificationId;
    private Context context;
    private String userChatStatus = "", astrologerName, astrologerProfileUrl, astrologerId, userChatTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        try {
            actionOnIntent(intent);
            //initializingCountDownTimer(61 * 1000, channelId);
            Log.d("AgoraCallStatusReceiver", "Called ");
        } catch (Exception e) {
            Log.d("AgoraCallStatusReceiver", "receiver Exception 1-->" + e);
        }
    }


    private void actionOnIntent(Intent intent) {
        if (intent != null) {
            intentActionType = intent.getStringExtra(CGlobalVariables.ACTION_TYPE);
            intentNotificationId = intent.getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
            Log.d("AgoraCallStatusReceiver", "Called intentNotificationId=" + intentNotificationId);

            String consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
            channelId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
            String agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
            String agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
            String astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
            String astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
            String agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
            String astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);

            if (intentActionType.equals(CGlobalVariables.AGORA_CALL_REJECTED_BY_USER)) {
                agoraCallRejected();

            }
        }
    }

    private void agoraCallRejected() {
        try {
            if (CUtils.isConnectedWithInternet(context)) {
                if (context == null) return;

                CUtils.changeFirebaseKeyStatus(channelId, "false", true, CGlobalVariables.USER_REJECTED);
                String callStatus = CGlobalVariables.USER_BUSY;
                String remarks = CGlobalVariables.USER_REJECTED;
                try {
                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                    nMgr.cancel(intentNotificationId);
                    Log.d("AgoraCallStatusReceiver", " stopService--> nMgr" + nMgr);
                    Log.d("AgoraCallStatusReceiver", " stopService--> intentNotificationId" + intentNotificationId);
                    context.stopService(new Intent(context, AstroAcceptRejectService.class));

                } catch (Exception e) {
                    Log.d("AgoraCallStatusReceiver", " stopService-->" + e);
                }
                clearNotificationAndStopRingtone(intentNotificationId);
                callEndCallApi(channelId, remarks, callStatus);
            }
        } catch (Exception e) {
        }
    }

    // Method to clear the notification and stop the ringtone
    private void clearNotificationAndStopRingtone(int notificationId) {
        Log.d("AgoraCallStatusReceiver", " clearNotificationAndStopRingtone-->");

        // Cancel the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);

        // Stop the ringtone if it's playing
        // Stop the default ringtone
        if (CUtils.player != null) {
            CUtils.player.stop();
            CUtils.player.release();
            CUtils.player = null;
        }
    }

    private void callEndCallApi(String channelID, String remarks, String status) {

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endInternetcall(getChatCompleteParams(channelID, remarks, status));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {

                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("AgoraCallStatusReceiver", "Called callEndCallApi onFailure " + t.getMessage());

            }
        });
    }

    public Map<String, String> getChatCompleteParams(String channelID, String callStatus, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, callStatus);
        headers.put(CGlobalVariables.CHAT_DURATION, "00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }


}
