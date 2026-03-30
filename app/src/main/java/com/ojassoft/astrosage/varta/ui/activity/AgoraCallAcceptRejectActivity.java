package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.OnSwipeTouchListener;
import com.ojassoft.astrosage.varta.utils.RippleBackground;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.activities.VideoCallActivity;
import com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgoraCallAcceptRejectActivity extends AppCompatActivity implements OnSwipeTouchListener.onSwipeListener, VolleyResponse {
    private Context context;
    private Activity activity;
    Animation animSlideUp;
    ImageView ivVideoCallEnd, ivUpArrowVideoCallEnd, ivUpArrowVideoCall, ivVideoCallStart;
    OnSwipeTouchListener onSwipeTouchListener;
    int intentNotificationId;
    private CustomProgressDialog pd;
    private RequestQueue queue;
    private MediaPlayer mMediaPlayer;
    private CountDownTimer countDownTimerForOneMein = null;
    String consultationType;
    String agoraCallSId;
    String agoraToken;
    String agoraTokenId;
    String astrologerName;
    String astrologerProfileUrl;
    String agoraCallDuration;
    String astrologerId;
    private static final long longOneSecond = 1000;
    private CircularNetworkImageView circularNetworkImageView;

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

        setContentView(R.layout.activity_agora_call_accept_reject);
        this.context = this;
        this.activity = this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.regular);

        try {
            inti();
        }catch (Exception e){}

    }

    private void inti() {
        Intent intent = getIntent();
        actionOnIntent(intent);

        circularNetworkImageView = findViewById(R.id.centerImage);

        if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
            String newAstrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
            circularNetworkImageView.setImageUrl(newAstrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());

        }

        TextView sub_title = findViewById(R.id.sub_title);
        FontUtils.changeFont(context, sub_title, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);

        TextView txtAstrologerName = findViewById(R.id.astrologerName);
        FontUtils.changeFont(context, txtAstrologerName, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);
        ivVideoCallEnd = findViewById(R.id.ivVideoCallEnd);

        ivUpArrowVideoCall = findViewById(R.id.ivUpArrowVideoCall);
        ivVideoCallStart = findViewById(R.id.ivVideoCallStart);
        TextView acceptBtn = findViewById(R.id.acceptBtn);
        FontUtils.changeFont(context, acceptBtn, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
        TextView rejectBtn = findViewById(R.id.rejectBtn);
        FontUtils.changeFont(context, rejectBtn, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);

        //onSwipeTouchListener = new OnSwipeTouchListener(this, ivVideoCallStart,this);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        ivVideoCallStart.startAnimation(animSlideUp);
        ivUpArrowVideoCall.startAnimation(animSlideUp);

        mMediaPlayer = CUtils.playDefaultRingtone(context);

        String titleText = astrologerName + " " + getResources().getString(R.string.isCalling);
        txtAstrologerName.setText(titleText);
        if (consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)) {
            ivVideoCallStart.setImageDrawable(getResources().getDrawable(R.drawable.video_call_full_icon));
            sub_title.setText(getResources().getString(R.string.varta_video_call));
        } else {
            ivVideoCallStart.setImageDrawable(getResources().getDrawable(R.drawable.voice_call_icon));
            sub_title.setText(getResources().getString(R.string.varta_audio_call));
        }
        ivVideoCallEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.stopDefaultRingtone(mMediaPlayer);
                if (countDownTimerForOneMein != null) {
                    countDownTimerForOneMein.cancel();
                }
                CUtils.changeFirebaseKeyStatus(agoraCallSId, "false", true, CGlobalVariables.USER_REJECTED);
                CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.USER_BUSY;
                // userChatStatus = CGlobalVariables.USER_BUSY;
                chatCompleted(agoraCallSId, CGlobalVariables.USER_BUSY, CGlobalVariables.USER_REJECTED);
            }
        });
        ivVideoCallStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               acceptAction();
               return false;
            }
        });
        initializingCountDownTimerForOneMin();
    }

    private void acceptAction() {
        CUtils.stopDefaultRingtone(mMediaPlayer);
        try {
            //CUtils.fcmAnalyticsEvents("continue_chat_astro_accept_timer_dialog", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            if (!CUtils.isConnectedWithInternet(context)) {
                CUtils.showSnackbar(ivVideoCallStart, context.getResources().getString(R.string.no_internet), context);
                return;
            }
            if (countDownTimerForOneMein != null) {
                countDownTimerForOneMein.cancel();
            }
            // CUtils.changeFirebaseKeyStatus(channelId, "true", false, "");
            agoraCallAccepted(agoraCallSId);
            //userChatStatus = "";
        } catch (Exception e) {
            //
        }
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
                        CUtils.changeFirebaseKeyStatus(agoraCallSId, "false", true, CGlobalVariables.REMARKS_USER_NO_ANSWER);
                        // userChatStatus = CGlobalVariables.USER_NO_ANSWER;
                        CUtils.fcmAnalyticsEvents("time_over_astro_accept_timer_dialog", CGlobalVariables.DISMISS_DIALOG_EVENT, "");
                        chatCompleted(agoraCallSId, CGlobalVariables.USER_NO_ANSWER, CGlobalVariables.REMARKS_USER_NO_ANSWER);
                    } catch (Exception e) {
                        //
                    }
                }
            }.start();
        }
    }

    private void actionOnIntent(Intent intent) {
        if (intent != null) {
            intentNotificationId = intent.getIntExtra(CGlobalVariables.NOTIFICATION_ID, -1);
            consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
            agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
            agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
            agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
            astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
            astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
            agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
            astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
        }
    }

    public void chatCompleted(String channelID, String chatStatus, String remarks) {
        if (context == null) return;
        CUtils.cancelNotification(context);
        CGlobalVariables.chatTimerTime = 0;
        //isTimerStart = false;
        if (CUtils.isConnectedWithInternet(context)) {
            if (pd == null)
                pd = new CustomProgressDialog(context);
            pd.show();
            pd.setCancelable(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.endInternetcall( getChatCompleteParams(channelID, chatStatus, remarks));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            stopService(new Intent(AgoraCallAcceptRejectActivity.this, AgoraCallInitiateService.class));
                            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            nMgr.cancel(intentNotificationId);
                        } catch (Exception e) {

                        }
                            CGlobalVariables.callTimerTime = 0;
                            removeListeners();
                            //CUtils.saveAstrologerIDAndChannelID(getActivity(), "", "");
                        }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        headers.put(CGlobalVariables.ASTROLOGER_ID, agoraCallSId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));


        return CUtils.setRequiredParams(headers);
    }

    public void agoraCallAccepted(String channelID) {
        //CUtils.fcmAnalyticsEvents("user_chat_accept_api_call", CGlobalVariables.API_CALL_EVENT, "");
        if (!CUtils.isConnectedWithInternet(context)) {
            //CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), DashBoardActivity.this);
        } else {
            /*if (pd == null)
                pd = new CustomProgressDialog(context);
            pd.show();
            pd.setCancelable(false);*/
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CHAT_ACCEPTED_URL,
//                    this, false, getAgoraCallAcceptedParams(channelID), 2).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.userChatAcceptV2( getAgoraCallAcceptedParams(channelID));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Intent resultIntent;
                        if (consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)) {
                            resultIntent = new Intent(AgoraCallAcceptRejectActivity.this, VideoCallActivity.class);
                        } else {
                            resultIntent = new Intent(AgoraCallAcceptRejectActivity.this, VoiceCallActivity.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(CGlobalVariables.IS_FROM_RETURN_TO_CALL, true);
                        bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
                        bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
                        bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
                        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
                        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
                        bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, agoraCallDuration);
                        bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
                        resultIntent.setPackage(BuildConfig.APPLICATION_ID);
                        resultIntent.putExtras(bundle);

                        startActivity(resultIntent);
                        removeListeners();
                    }catch (Exception e){
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public Map<String, String> getAgoraCallAcceptedParams(String channelID) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.CHAT_DURATION, changeMinToSec());
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    public String changeMinToSec() {
        String seconds = "00";
        if (agoraCallDuration != null && agoraCallDuration.length() > 0) {
            String[] time = agoraCallDuration.split(" ");
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
        hideProgressBar();
        if (response != null && response.length() > 0) {
            try {
                stopService(new Intent(this, AgoraCallInitiateService.class));
                NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(intentNotificationId);
            } catch (Exception e) {

            }
            if (method == 1) {
                CGlobalVariables.callTimerTime = 0;
                //isTimerStart = false;
               removeListeners();
                //CUtils.saveAstrologerIDAndChannelID(getActivity(), "", "");
            } else if (method == 2) {
                Intent resultIntent;
                if (consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)) {
                    resultIntent = new Intent(this, VideoCallActivity.class);
                } else {
                    resultIntent = new Intent(this, VoiceCallActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean(CGlobalVariables.IS_FROM_RETURN_TO_CALL, true);
                bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
                bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
                bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
                bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
                bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
                bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, agoraCallDuration);
                bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
                resultIntent.setPackage(BuildConfig.APPLICATION_ID);
                resultIntent.putExtras(bundle);

                startActivity(resultIntent);
                removeListeners();

            }
        } else {
            try {
                CGlobalVariables.chatTimerTime = 0;
                //isTimerStart = false;
                // userChatStatus = "";
                if (countDownTimerForOneMein != null) {
                    countDownTimerForOneMein.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {

        }
    }
    private void removeListeners(){
        try {
            if (countDownTimerForOneMein != null) {
                countDownTimerForOneMein.cancel();
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();

    }

    @Override
    public void swipeRight() {
        Toast.makeText(context, "swipeRight", Toast.LENGTH_SHORT).show();
        acceptAction();
    }

    @Override
    public void swipeTop() {
        Toast.makeText(context, "swipeRight", Toast.LENGTH_SHORT).show();

        acceptAction();
    }

    @Override
    public void swipeBottom() {
        Toast.makeText(context, "swipeRight", Toast.LENGTH_SHORT).show();

        acceptAction();
    }

    @Override
    public void swipeLeft() {
        Toast.makeText(context, "swipeRight", Toast.LENGTH_SHORT).show();

        acceptAction();
    }

    /*@Override
    public void downTouch() {

    }*/

    @Override
    protected void onDestroy() {
        CUtils.stopDefaultRingtone(mMediaPlayer);
        super.onDestroy();
    }
}