package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.RippleBackground;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAcceptRejectActivity extends AppCompatActivity implements VolleyResponse {
    private Activity context;
    private String msg, channelId, chatJsonObject;
    private MediaPlayer mMediaPlayer;
    private String userChatStatus = "", astrologerName, astrologerProfileUrl, astrologerId,userChatTime;
    private boolean isTimerStart = false;
    private boolean isContinueBtnClicked = false;
    private CustomProgressDialog pd;
    private RequestQueue queue;
    private CountDownTimer countDownTimerForOneMein = null;
    private static final long longOneSecond = 1000;
    int intentNotificationId;
    AstrologerDetailBean astrologerDetailBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        setContentView(R.layout.activity_chat_accept_reject);
        context = this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.regular);
        init();
    }

    private void init() {
        Log.d("testNewChat", " Chat Accept RejectActivity called " );
        Bundle bundle = getIntent().getExtras();
        msg = bundle.getString("msg");
        intentNotificationId = getIntent().getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
        channelId = bundle.getString(CGlobalVariables.CHAT_USER_CHANNEL);
        chatJsonObject = bundle.getString("connect_chat_bean");
        astrologerName = bundle.getString("astrologer_name");
        astrologerProfileUrl = bundle.getString("astrologer_profile_url");
        astrologerId = bundle.getString("astrologer_id");
        userChatTime  = bundle.getString("userChatTime");
        // astrologerDetailBean = (AstrologerDetailBean)bundle.getSerializable("chatInitiateAstrologerDetailBean");

        final RippleBackground rippleBackground = findViewById(R.id.rippleBackground);
        RelativeLayout btnAcceptChat = findViewById(R.id.btnAcceptChat);
        TextView txt_top_head = findViewById(R.id.astrologerName);
        FontUtils.changeFont(context, txt_top_head, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);

        TextView txt_accept_chat = findViewById(R.id.txt_accept_chat);
        FontUtils.changeFont(context, txt_accept_chat, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);

        TextView rejectChat = findViewById(R.id.reject_chat);
        FontUtils.changeFont(context, rejectChat, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);

        TextView txtAstrologerName = findViewById(R.id.astrologerName);
        FontUtils.changeFont(context, txtAstrologerName, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);

        txtAstrologerName.setText(astrologerName);
        rippleBackground.startRippleAnimation();
        CircularNetworkImageView astroCircularNetworkImageView = findViewById(R.id.centerImage);
        String callingAstroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
        if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
            astroCircularNetworkImageView.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(astroCircularNetworkImageView);
        } else astroCircularNetworkImageView.setVisibility(View.GONE);

        mMediaPlayer = CUtils.playDefaultRingtone(context);

        try {
            /*Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }*/
        } catch (Exception e) {
            //
        }


        btnAcceptChat.setOnClickListener(view -> {
            try {


            CUtils.fcmAnalyticsEvents("continue_chat_astro_accept_timer_dialog", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            if (!CUtils.isConnectedWithInternet(context)) {
                CUtils.showSnackbar(btnAcceptChat, context.getResources().getString(R.string.no_internet), context);
                return;
            }
            if (countDownTimerForOneMein != null) {
                countDownTimerForOneMein.cancel();
            }
           // CUtils.changeFirebaseKeyStatus(channelId, "true", false, "");
            chatAccepted(channelId);
            userChatStatus = "";
            }catch (Exception e){
                Log.d("testNewChat"," Error ===="+e.toString());
            }
        });
        rejectChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.fcmAnalyticsEvents("cancel_chat_astro_accept_timer_dialog", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                if (countDownTimerForOneMein != null) {
                    countDownTimerForOneMein.cancel();
                }
                CUtils.changeFirebaseKeyStatus(channelId, "false", true, CGlobalVariables.USER_REJECTED);
                CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.USER_BUSY;
                userChatStatus = CGlobalVariables.USER_BUSY;
                chatCompleted(CUtils.getSelectedChannelID(context), CGlobalVariables.USER_BUSY, CGlobalVariables.USER_REJECTED);
            }
        });
        initializingCountDownTimerForOneMin();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CUtils.stopDefaultRingtone(mMediaPlayer);
    }

    public void initializingCountDownTimerForOneMin() {
        long longTotalVerificationTimeForMin = 61 * 1000;
        if (countDownTimerForOneMein == null) {
            countDownTimerForOneMein = new CountDownTimer(longTotalVerificationTimeForMin, longOneSecond) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    try {
                        CUtils.changeFirebaseKeyStatus(channelId, "false", true, CGlobalVariables.REMARKS_USER_NO_ANSWER);
                        userChatStatus = CGlobalVariables.USER_NO_ANSWER;
                        CUtils.fcmAnalyticsEvents("time_over_astro_accept_timer_dialog", CGlobalVariables.DISMISS_DIALOG_EVENT, "");
                        chatCompleted(channelId, CGlobalVariables.USER_NO_ANSWER, CGlobalVariables.REMARKS_USER_NO_ANSWER);
                    } catch (Exception e) {
                        //
                    }
                }
            }.start();
        }
    }

    public void chatCompleted(String channelID, String chatStatus, String remarks) {
        if (context == null) return;
        CUtils.cancelNotification(context);
        CGlobalVariables.chatTimerTime = 0;
        isTimerStart = false;
        if (CUtils.isConnectedWithInternet(context)) {
            if (pd == null)
                pd = new CustomProgressDialog(context);
            pd.show();
            pd.setCancelable(false);
            //userChatStatus = chatStatus;
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.END_CHAT_URL,
//                    this, false, getChatCompleteParams(channelID, chatStatus, remarks), 1).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID,chatStatus, remarks), channelID, getClass().getSimpleName());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        stopService(new Intent(context, AstroAcceptRejectService.class));
                        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        nMgr.cancel(intentNotificationId);
                    } catch (Exception e){
                    //
                    }
                    if (response.body()!=null){
                        try {
                            Log.d("testChatAccpetRejcet","response.body()==>>"+response.body().string());
                            CGlobalVariables.chatTimerTime = 0;
                            isTimerStart = false;
                            onBackPressed();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }


    public Map<String, String> getChatCompleteParams(String channelID, String chatStatus, String remarks) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, chatStatus);
        headers.put(CGlobalVariables.CHAT_DURATION, "00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
       // Log.d("check_end_chat_api","Called from Chat AcceptReject Activity");

        return CUtils.setRequiredParams(headers);
    }

    public void chatAccepted(String channelID) {
        CUtils.fcmAnalyticsEvents("user_chat_accept_api_call", CGlobalVariables.API_CALL_EVENT, "");
        //Log.d("testNewDialog", "channelID   =    " + channelID);
        if (!CUtils.isConnectedWithInternet(context)) {
            //CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), DashBoardActivity.this);
        } else {
            /*if (pd == null)
                pd = new CustomProgressDialog(context);
            pd.show();
            pd.setCancelable(false);*/
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CHAT_ACCEPTED_URL,
//                    this, false, getChatAcceptedParams(channelID), 2).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.userChatAcceptV2( getChatAcceptedParams(channelID));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body()!=null){
                        try {
                            try {
                                hideProgressBar();
                                stopService(new Intent(context, AstroAcceptRejectService.class));
                                NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                nMgr.cancel(intentNotificationId);


                            } catch (Exception e){

                            }
                           // String myResponse = response.body().string();
                            Log.d("testChatAccpetRejcet","response.body()112375240358-34==>>"+response.body().string());
                            Intent intent = new Intent(context, ChatWindowActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, channelId);
                            bundle.putString("connect_chat_bean", chatJsonObject);
                            bundle.putString("astrologer_name", astrologerName);
                            bundle.putString("astrologer_profile_url", astrologerProfileUrl);
                            bundle.putString("astrologer_id", astrologerId);
                            bundle.putString("userChatTime", userChatTime);
                            // bundle.putSerializable("chatInitiateAstrologerDetailBean", astrologerDetailBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            onBackPressed();
                        }catch (Exception e){
                            //
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    public Map<String, String> getChatAcceptedParams(String channelID) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.CHAT_DURATION, changeMinToSec());
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, CUtils.getSelectedAstrologerID(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.d("testNewDialog", "getChatAcceptedParams   =    " + headers.toString());

        return CUtils.setRequiredParams(headers);
    }

    public String changeMinToSec() {
        String seconds = "00";
        if (CGlobalVariables.userChatTime != null && CGlobalVariables.userChatTime.length() > 0) {
            String[] time = CGlobalVariables.userChatTime.split(" ");
            if (time != null && time.length > 0) {
                String[] minSec = time[0].split(":");
                if (minSec != null && minSec.length > 1) {
                    int convertSec = Integer.parseInt(minSec[0]) * 60;
                    int totalSec = convertSec + Integer.parseInt(minSec[1]);
                    seconds = "" + totalSec;
                }
            }
        }
        return seconds;
    }

    @Override
    public void onResponse(String response, int method) {
//        hideProgressBar();
//        if (response != null && response.length() > 0) {
//            try {
//                stopService(new Intent(this, AstroAcceptRejectService.class));
//                NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                nMgr.cancel(intentNotificationId);
//            } catch (Exception e){
//
//            }
//            if (method == 1) {
//                CGlobalVariables.chatTimerTime = 0;
//                isTimerStart = false;
//                onBackPressed();
//                //CUtils.saveAstrologerIDAndChannelID(getActivity(), "", "");
//            } else if (method == 2) {
//                //Log.d("testNewChat", " ChatAcceptRejectActivity call ChatWindowActivity  " );
//
//                Intent intent = new Intent(context, ChatWindowActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, channelId);
//                bundle.putString("connect_chat_bean", chatJsonObject);
//                bundle.putString("astrologer_name", astrologerName);
//                bundle.putString("astrologer_profile_url", astrologerProfileUrl);
//                bundle.putString("astrologer_id", astrologerId);
//                bundle.putString("userChatTime", userChatTime);
//                // bundle.putSerializable("chatInitiateAstrologerDetailBean", astrologerDetailBean);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                onBackPressed();
//
//            }
//        } else {
//            try {
//                CGlobalVariables.chatTimerTime = 0;
//                isTimerStart = false;
//                userChatStatus = "";
//                if (countDownTimerForOneMein != null) {
//                    countDownTimerForOneMein.cancel();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onError(VolleyError error) {
        //hideProgressBar();
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        try {
            if (countDownTimerForOneMein != null) {
                countDownTimerForOneMein.cancel();
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}