package com.ojassoft.astrosage.ui.act;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.ojassoft.astrosage.R;

public class HelpVideoPlayerActivity extends BaseInputActivity {

    VideoView videoView;
    ProgressBar progressBar;
    ImageView replayBtn, closeBtn;
    String videoUrl = "https://www.astrosage.com/images/ads/kundli-ai-help.mp4";

    public HelpVideoPlayerActivity() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_layout);
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progress_bar);
        replayBtn = findViewById(R.id.replay_iv);
        closeBtn = findViewById(R.id.close_iv);
        closeBtn.setVisibility(View.GONE);
        replayBtn.setVisibility(View.GONE);

        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(android.view.View.GONE);
            videoView.start();
        });

        videoView.setOnInfoListener((mp, what, extra) -> {
            if (what == android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                progressBar.setVisibility(android.view.View.VISIBLE);
            } else if (what == android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                progressBar.setVisibility(android.view.View.GONE);
            }
            return true;
        });

//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);

//        videoView.setMediaController(mediaController);
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);

        videoView.start();

        videoView.setOnCompletionListener(mp -> {
            replayBtn.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.VISIBLE);
        });

        replayBtn.setOnClickListener(v -> {
            replayBtn.setVisibility(View.GONE);
            closeBtn.setVisibility(View.GONE);
            videoView.start();
        });
        closeBtn.setOnClickListener(v -> {
            videoView.stopPlayback();
            finish();
        });
    }



    @Override
    public void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }


}
