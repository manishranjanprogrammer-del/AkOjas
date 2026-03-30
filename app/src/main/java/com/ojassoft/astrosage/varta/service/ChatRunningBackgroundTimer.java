package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.ServerValue;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatRunningBackgroundTimer implements VolleyResponse {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static  ChatRunningBackgroundTimer chatRunningBackgroundTimer;
    private static Context activity;
    CountDownTimer Count;
    RequestQueue queue;

    public static ChatRunningBackgroundTimer getInstance(Context activity) {
        ChatRunningBackgroundTimer.activity = activity;
        if (chatRunningBackgroundTimer == null) {
            chatRunningBackgroundTimer = new ChatRunningBackgroundTimer();
        }
        return chatRunningBackgroundTimer;
    }

    public void startTimer() {
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        long oneMinTime = 60000;
        long actualTime = 0;
        long userTimerTime = CGlobalVariables.chatTimerTime;
        if (userTimerTime <= oneMinTime) {
            actualTime = userTimerTime;
        } else {
            actualTime = oneMinTime;
        }
        initCountDownTimer(actualTime);
    }

    private void initCountDownTimer(long actualTime) {
        Count = new CountDownTimer(actualTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                try {
                    /*String text = String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                                    TimeUnit.MILLISECONDS.toDays(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));*/
                    //Log.e("TIMERSERVICE ", text);
                    ////Log.e("TIMERSERVICE ", "" + CGlobalVariables.chatTimerTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                try {
                    //Log.e("TIMERSERVICE ", " call onFinish ");
                    chatCompleted(CUtils.getSelectedChannelID(activity));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void chatCompleted(String channelID) {
       try {
           CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.USER_BACKGROUND;
           CGlobalVariables.CHAT_END_IN_BACKGROUND = true;
           CUtils.changeFirebaseKeyStatus(channelID, "NA", true, CGlobalVariables.CHAT_END_STATUS);
           CUtils.cancelNotification(activity);
           Log.e("TestChatAI", "isConnected="+CUtils.isConnectedWithInternet(activity));
           if (CUtils.isConnectedWithInternet(activity)) {
               callEndChatApi(channelID);
           }

           CUtils.saveAstrologerIDAndChannelID(activity, "", "");
           CGlobalVariables.chatTimerTime = 0;
       }catch (Exception e){
           //
       }
    }
    private void callEndChatApi(String channelID) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String myRes = response.body().string();
                        Log.e("TestChatAI", "onResponse="+myRes);
                    } catch (Exception e) {
                        Log.e("TestChatAI", "Exception="+e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TestChatAI", "onFailure="+t);
            }
        });
    }

    public Map<String, String> getChatCompleteParams(String channelID) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/timeChangeInSecond(CGlobalVariables.chatTimerTime));
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.REMARKS, CGlobalVariables.USER_BACKGROUND);
        if (CUtils.getSelectedAstrologerID(activity) != null && CUtils.getSelectedAstrologerID(activity).length() > 0) {
            headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(activity));
        }
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        //Log.e("TIMERSERVICE ", " Params => " + headers.toString() );
        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));

        return CUtils.setRequiredParams(headers);
    }


    private String timeChangeInSecond(long userRemainingTime) {
        //Log.e("TIMER remaining  ", "" + CGlobalVariables.chatTimerTime);
        String userChatDuration = "00";
        long totalTime = (long) 0.0, remainingUserTime = (long) 0.0, actualChatUserTime = (long) 0.0;
        String[] timeArray;
        try {
            if (CGlobalVariables.userChatTime.length() > 0) {
                timeArray = CGlobalVariables.userChatTime.split(":");
                if (timeArray != null && timeArray.length > 1) {
                    String[] secondArr = timeArray[1].split(" ");
                    long minTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(timeArray[0]));
                    long secTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(secondArr[0]));
                    totalTime = minTime + secTime;
                    //Log.e("TIMER totalTime  ", "" + totalTime);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            actualChatUserTime = totalTime - userRemainingTime;
            //Log.e("TIMER UserTime ", "" + actualChatUserTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        // long seconds = (milliseconds / 1000);
        try {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(actualChatUserTime);
            //Log.e("TIMER seconds ", "" + seconds);
            int actualSeconds = (int) seconds;
            //Log.e("TIMER actualSeconds ", "" + actualSeconds);
            userChatDuration = "" + actualSeconds;
            //Log.e("TIMER userChatDuration ", "" + userChatDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userChatDuration;
    }



    public void cancelTimer() {
        try {
            if (Count != null) {
                Count.cancel();
                Count = null;
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.e("TIMERSERVICE ", response);
        if (response != null && response.length() > 0) {

            CUtils.saveAstrologerIDAndChannelID(activity, "", "");
            CGlobalVariables.chatTimerTime = 0;
            //Toast.makeText(DashBoardActivity.this, "Response "+ response, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onError(VolleyError error) {

    }
}


