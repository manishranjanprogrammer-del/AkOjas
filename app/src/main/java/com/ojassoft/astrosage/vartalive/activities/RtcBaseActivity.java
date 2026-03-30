package com.ojassoft.astrosage.vartalive.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;


import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.vartalive.rtc.EventHandler;
import com.ojassoft.astrosage.vartalive.utils.Constants;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import java.util.Locale;

import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.GetOnlineUsersOptions;
import io.agora.rtm.GetOnlineUsersResult;
import io.agora.rtm.MessageEvent;
import io.agora.rtm.PresenceEvent;
import io.agora.rtm.ResultCallback;

import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmConfig;
import io.agora.rtm.RtmConstants;
import io.agora.rtm.RtmEventListener;
import io.agora.rtm.RtmLogConfig;
import io.agora.rtm.SubscribeOptions;

public abstract class RtcBaseActivity extends BaseActivity implements EventHandler {

    protected LiveAstrologerModel liveAstrologerModel;
    protected String channelName = "";
    protected String token = "";
    protected Activity currentActivity;
    private RtmClient mRtmClient;

    protected RtmEventListener rtmEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentActivity = this;
        liveAstrologerModel = AstrosageKundliApplication.liveAstrologerModel;
        AstrosageKundliApplication.liveAstrologerModel = null;
        if (liveAstrologerModel != null) {
            channelName = liveAstrologerModel.getChannelName();
            token = liveAstrologerModel.getToken();
            //Log.d("MyTestingData ","channelName  called = "+channelName);

            //Log.d("LIveCount ","rtcToken  called = "+token);
            //Log.d("LIveCount ","channel = "+channelName);

            if (TextUtils.isEmpty(channelName) || TextUtils.isEmpty(token)) {
                return;
            }
            registerRtcEventHandler(this);
            joinChannel();
        }
    }

    private void configVideo() {
        VideoEncoderConfiguration configuration = new VideoEncoderConfiguration(
                Constants.VIDEO_DIMENSIONS[config().getVideoDimenIndex()],
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        );
        //configuration.mirrorMode = Constants.VIDEO_MIRROR_MODES[config().getMirrorEncodeIndex()];
        rtcEngine().setVideoEncoderConfiguration(configuration);
    }

    protected void joinChannel() {
        // Initialize token, extra info here before joining channel
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name and uid that
        // you use to generate this token.

        if (TextUtils.isEmpty(token)) {
            token = null; // default, no token
        }
        // Sets the channel profile of the Agora RtcEngine.
        // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
        //Log.e("LiveStreamAstro ", " RTC token " + token);
        //Log.e("LiveStreamAstro ", " RTC channelName " + channelName);
        rtcEngine().setChannelProfile(io.agora.rtc2.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        //LowLightEnhanceOptions lowLightEnhanceOptions = new LowLightEnhanceOptions(LowLightEnhanceOptions.LOW_LIGHT_ENHANCE_LEVEL_HIGH_QUALITY,LowLightEnhanceOptions.LOW_LIGHT_ENHANCE_LEVEL_FAST);
        //rtcEngine().setLowlightEnhanceOptions(true,lowLightEnhanceOptions);
        rtcEngine().enableVideo();
        configVideo();
        rtcEngine().joinChannel(token, channelName, "", 0);
    }

    protected void loginRtmSDK(String rtmToken,String userId){
        if(!TextUtils.isEmpty(rtmToken)) {
            new Thread(() -> {
                getRtmInstance(userId);
                rtmClient().login(rtmToken, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("LIveCount", " login () onSuccess ");
                        subscribeRtmChannel();
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        Log.e("LIveCount", " login () onFailure " + errorInfo);
                    }
                });
            }).start();
        }
    }
    private void subscribeRtmChannel(){
        SubscribeOptions subscribeOptions = new SubscribeOptions();
        subscribeOptions.setWithPresence(true);
        //Log.e("LIveCount", " Astrologer channelName  " + channelName);
        rtmClient().subscribe(channelName,subscribeOptions, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.e("LIveCount", " subscribe () onSuccess ");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e("LIveCount", " subscribe onFailure () " + errorInfo);
            }
        });
    }
    protected SurfaceView prepareRtcVideo(int uid, boolean local) {
        // Render local/remote video on a SurfaceView
        SurfaceView surface = new SurfaceView(getApplicationContext());
        if (local) {
            rtcEngine().setupLocalVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            0
                    )
            );
        } else {
            rtcEngine().setupRemoteVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            uid
                    )
            );
        }
        return surface;
    }

    protected void removeRtcVideo(int uid, boolean local) {
        if (local) {
            rtcEngine().setupLocalVideo(null);
        } else {
            rtcEngine().setupRemoteVideo(new VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AstrosageKundliApplication.liveActivityVisible = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.e("leaveChannelState", "onDestroy="+AstrosageKundliApplication.leaveChannelState);
        if(!AstrosageKundliApplication.leaveChannelState) {
            statsManager().clearAllData();
            removeRtcEventHandler(this);
            rtcEngine().leaveChannel();
        }
        AstrosageKundliApplication.liveActivityVisible = false;

    }

    abstract protected void updateUsersCount(String userCount);

    protected void getRtmInstance(String userId){
        rtmEventListener = new RtmEventListener() {
            @Override
            public void onMessageEvent(MessageEvent event) {
                RtmEventListener.super.onMessageEvent(event);
            }

            @Override
            public void onPresenceEvent(PresenceEvent event) {
                RtmEventListener.super.onPresenceEvent(event);
                GetOnlineUsersOptions options = new GetOnlineUsersOptions();
                mRtmClient.getPresence().getOnlineUsers(channelName, RtmConstants.RtmChannelType.MESSAGE, options, new ResultCallback<GetOnlineUsersResult>() {
                    @Override
                    public void onSuccess(GetOnlineUsersResult responseInfo) {
                        int usersCount = responseInfo.getTotalOccupancy();
                        if(usersCount > 1000){
                            updateUsersCount(String.format(Locale.getDefault(),"%.2fK",(float)usersCount/1000));
                        }else{
                            updateUsersCount(""+usersCount);
                        }

                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {}
                });
            }
        };
        RtmConfig rtmConfig =  new RtmConfig.Builder(getString(R.string.livestreaming_app_id), userId).build();

        try {
            mRtmClient = RtmClient.create(rtmConfig);
            mRtmClient.addEventListener(rtmEventListener);

        } catch (Exception e) {
        }
    }

    protected RtmClient rtmClient(){
        return mRtmClient;
    }

    protected void unSubscribeRTM(){
        try {
            if (!TextUtils.isEmpty(channelName)) {
                rtmClient().unsubscribe(channelName, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void responseInfo) {
                        Log.e("LIveCount", "unsubscribed onSuccess() ");
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                    }
                });
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
