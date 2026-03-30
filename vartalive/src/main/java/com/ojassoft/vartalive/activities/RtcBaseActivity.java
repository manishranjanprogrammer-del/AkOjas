package com.ojassoft.vartalive.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;


import com.ojassoft.vartalive.rtc.EventHandler;
import com.ojassoft.vartalive.utils.Constants;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public abstract class RtcBaseActivity extends BaseActivity implements EventHandler {

    protected LiveAstrologerModel liveAstrologerModel;
    protected String channelName = "";
    protected String token = "";
    protected Activity currentActivity;

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

            //Log.d("MyTestingData ","token  called = "+token);

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
        configuration.mirrorMode = Constants.VIDEO_MIRROR_MODES[config().getMirrorEncodeIndex()];
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
        rtcEngine().setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        rtcEngine().enableVideo();
        configVideo();
        rtcEngine().joinChannel(token, channelName, "", 0);
    }

    protected SurfaceView prepareRtcVideo(int uid, boolean local) {
        // Render local/remote video on a SurfaceView

        SurfaceView surface = RtcEngine.CreateRendererView(getApplicationContext());
        if (local) {
            rtcEngine().setupLocalVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            0,
                            Constants.VIDEO_MIRROR_MODES[config().getMirrorLocalIndex()]
                    )
            );
        } else {
            rtcEngine().setupRemoteVideo(
                    new VideoCanvas(
                            surface,
                            VideoCanvas.RENDER_MODE_HIDDEN,
                            uid,
                            Constants.VIDEO_MIRROR_MODES[config().getMirrorRemoteIndex()]
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


}
