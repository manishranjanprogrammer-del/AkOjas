package com.ojassoft.astrosage.varta.service;

//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Build;
//import android.os.CountDownTimer;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.google.firebase.database.ServerValue;
//import com.ojassoft.astrosage.R;
//import com.ojassoft.astrosage.networkcall.ApiList;
//import com.ojassoft.astrosage.networkcall.RetrofitClient;
//import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
//import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
//import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
//import com.ojassoft.astrosage.varta.utils.CUtils;
//import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
//import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
//import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
//import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//
//public class CountDownTimerService extends Service implements VolleyResponse {
//    public static final String CHANNEL_ID = "ForegroundServiceChannel";
//    CountDownTimer Count;
//    RequestQueue queue;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        createNotificationChannel();
//        createNotification();
//        cancelOngoingNotification();
//        queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
//        long oneMinTime = 60000;
//        long actualTime = 0;
//        long userTimerTime = CGlobalVariables.chatTimerTime;
//        if (userTimerTime <= oneMinTime) {
//            actualTime = userTimerTime;
//        } else {
//            actualTime = oneMinTime;
//        }
//        initCountDownTimer(actualTime);
//        return START_STICKY;
//    }
//
//    private void initCountDownTimer(long actualTime) {
//        Count = new CountDownTimer(actualTime, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//                try {
//                    String text = String.format("%02d:%02d:%02d",
//                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
//                                    TimeUnit.MILLISECONDS.toDays(millisUntilFinished)),
//                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
//                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//                    //Log.e("TIMERSERVICE ", text);
//                    ////Log.e("TIMERSERVICE ", "" + CGlobalVariables.chatTimerTime);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFinish() {
//                try {
//                    //Log.e("TIMERSERVICE ", " call onFinish ");
//                    chatCompleted(CUtils.getSelectedChannelID(getApplicationContext()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
//
//    private void createNotification() {
//        try {
//            String msg = getString(R.string.ongoing_call);
//            Intent notificationIntent = new Intent(this, ChatWindowActivity.class);
//            notificationIntent.putExtra("ongoing_notification", true);
//            notificationIntent.setAction(Intent.ACTION_VIEW);
//            PendingIntent pendingIntent;
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//            } else {
//                pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//            }
//
//            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle(CUtils.getStringData(this, CGlobalVariables.TITLE_ONGOING_CHAT, ""))
//                    .setContentText(msg)
//                    .setSmallIcon(R.drawable.chat_logo)
//                    .setColor(getResources().getColor(R.color.colorPurple))
//                    .setLargeIcon(icon)
//                    .setDefaults(Notification.DEFAULT_SOUND)
//                    .setChannelId(CHANNEL_ID)
//                    .setAutoCancel(false)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                    .setContentIntent(pendingIntent)
//                    .build();
//            startForeground(1, notification);
//        } catch (Exception e) {
//           // Log.d("testService","createNotification "+e);
//
//        }
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID, "Foreground Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(serviceChannel);
//        }
//    }
//
//    public void cancelOngoingNotification() {
//        try {
//            //Log.d("testService","cancelOngoingNotification ");
//            NotificationManager nMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (nMgr != null) {
//                nMgr.cancel(CGlobalVariables.ONGOING_NOTIFICATION, 1001);
//               // Log.d("testService","cancelOngoingNotification 123 ");
//            }
//        } catch (Exception e) {
//           // Log.d("testService","cancelOngoingNotification "+e);
//
//            //e.printStackTrace();
//        }
//    }
//
//    private void chatCompleted(String channelID) {
//        //Log.e("TIMERSERVICE ", " chatCompleted channelID => " + channelID );
//        UpdateEndChatStatus(channelID);
//        CUtils.cancelNotification(getApplicationContext());
//        if (CUtils.isConnectedWithInternet(getApplicationContext())) {
////            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CHAT_URL,
////                    CountDownTimerService.this, false, getChatCompleteParams(channelID), 1).getMyStringRequest();
////            stringRequest.setShouldCache(true);
////            queue.add(stringRequest);
//            callEndChatApi(channelID );
//        }
//
//        CUtils.saveAstrologerIDAndChannelID(getApplicationContext(), "", "");
//        CGlobalVariables.chatTimerTime = 0;
//    }
//    private void callEndChatApi(String channelID) {
//        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
//        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID), channelID, getClass().getSimpleName());
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                if (response.body()!=null) {
//                    try {
//                        CUtils.saveAstrologerIDAndChannelID(getApplicationContext(), "", "");
//                        CGlobalVariables.chatTimerTime = 0;
//                    } catch (Exception e) {
//                        //
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//    private void UpdateEndChatStatus(String channelID){
//        try{
//            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
//                    child(CGlobalVariables.END_CHAT_TIME_FBD_KEY).setValue(ServerValue.TIMESTAMP);
//            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
//                    child(CGlobalVariables.END_CHAT_SATATUS_FBD_KEY).setValue(CGlobalVariables.USER_BACKGROUND);
//            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
//                    child(CGlobalVariables.END_CHAT_FBD_KEY).setValue(true);
//        } catch (Exception e) {
//            ////Log.e("UpdateEndChatStatus e//")
//        }
//    }
//
//    public Map<String, String> getChatCompleteParams(String channelID) {
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getApplicationContext()));
//        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
//        headers.put(CGlobalVariables.CHAT_DURATION, /*"15"*/timeChangeInSecond(CGlobalVariables.chatTimerTime));
//        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
//        headers.put(CGlobalVariables.REMARKS, CGlobalVariables.USER_BACKGROUND);
//        if (CUtils.getSelectedAstrologerID(getApplicationContext()) != null && CUtils.getSelectedAstrologerID(getApplicationContext()).length() > 0) {
//            headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(getApplicationContext()));
//        }
//        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
//        //Log.e("TIMERSERVICE ", " Params => " + headers.toString() );
//        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
//
//        return CUtils.setRequiredParams(headers);
//    }
//
//
//    private String timeChangeInSecond(long userRemainingTime) {
//        //talktime = "36:24 minutes";
//        //Log.e("TIMER totalTime  ", "" + CGlobalVariables.userChatTime);
//        //Log.e("TIMER remaining  ", "" + CGlobalVariables.chatTimerTime);
//        String userChatDuration = "00";
//        long totalTime = (long) 0.0, remainingUserTime = (long) 0.0, actualChatUserTime = (long) 0.0;
//        String[] timeArray;
//        try {
//            if (CGlobalVariables.userChatTime.length() > 0) {
//                timeArray = CGlobalVariables.userChatTime.split(":");
//                if (timeArray != null && timeArray.length > 1) {
//                    String[] secondArr = timeArray[1].split(" ");
//                    long minTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(timeArray[0]));
//                    long secTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(secondArr[0]));
//                    totalTime = minTime + secTime;
//                    //Log.e("TIMER totalTime  ", "" + totalTime);
//                }
//            }
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        try {
//           /* if(!userRemainingTime.equals("00:00:00")) {
//                String[] remainingTimeArr = userRemainingTime.split(":");
//                long hourTime = TimeUnit.HOURS.toMillis(Integer.parseInt(remainingTimeArr[0]));
//                long minTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(remainingTimeArr[1]));
//                long secTime = TimeUnit.SECONDS.toMillis(Integer.parseInt(remainingTimeArr[2]));
//                remainingUserTime = hourTime + minTime + secTime;
//                //Log.e("TIMER remainingUserTime ", "" + remainingUserTime);
//            }
//            */
//            actualChatUserTime = totalTime - userRemainingTime;
//            //Log.e("TIMER UserTime ", "" + actualChatUserTime);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//
//        // long seconds = (milliseconds / 1000);
//        try {
//            long seconds = TimeUnit.MILLISECONDS.toSeconds(actualChatUserTime);
//            //Log.e("TIMER seconds ", "" + seconds);
//            int actualSeconds = (int) seconds;
//            //Log.e("TIMER actualSeconds ", "" + actualSeconds);
//            userChatDuration = "" + actualSeconds;
//            //Log.e("TIMER userChatDuration ", "" + userChatDuration);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return userChatDuration;
//    }
//
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        try {
//            if (Count != null) {
//                Count.cancel();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onResponse(String response, int method) {
//        //Log.e("TIMERSERVICE ", response);
//        if (response != null && response.length() > 0) {
//
//            CUtils.saveAstrologerIDAndChannelID(getApplicationContext(), "", "");
//            CGlobalVariables.chatTimerTime = 0;
//            //Toast.makeText(DashBoardActivity.this, "Response "+ response, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    @Override
//    public void onError(VolleyError error) {
//
//    }
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        ////Log.e("TIMERSERVICE ", "onTaskRemoved");
//        //stop service  `
//        try {
//            CUtils.saveAstrologerIDAndChannelID(getApplicationContext(), "", "");
//            stopForeground(true);
//            stopSelf();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}


