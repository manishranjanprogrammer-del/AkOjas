package com.ojassoft.astrosage.ui.act;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ojas on १३/११/१८.
 */

public class ActYouTubeVideoPlayer extends BaseInputActivity implements
        AppCompatCallback {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private boolean fullscreen;
    private Configuration config;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface;
    Toolbar tool_barAppModule;
    ArrayList<String> videoIdList;
    int orientation = 2;
    int position;
    boolean isFirstVideo;
    private WebView webView;
    private LinearLayout baseLayout, main_layout;

    public ActYouTubeVideoPlayer() {
        super(R.string.app_name);
    }
    YouTubePlayerView youtubePlayerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.youtube_player_lay);
        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER, null);

        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

       // position = bundle.getInt("position");
        position = bundle.getInt("position", 0);

// Fetch playlist
        ArrayList<YoutubeVideoBean> playlist =
                (ArrayList<YoutubeVideoBean>) bundle.getSerializable("playlist");

        videoIdList = new ArrayList<>();

        if (playlist != null) {
            for (YoutubeVideoBean data : playlist) {
                if (data != null && !TextUtils.isEmpty(data.getVideoId())) {
                    videoIdList.add(data.getVideoId());
                }
            }
        }

        if (videoIdList.isEmpty() || position >= videoIdList.size()) {
            finish();
            return;
        }

//        videoIdList = new ArrayList<String>();
//        ArrayList<YoutubeVideoBean> playlistVedios = (ArrayList<YoutubeVideoBean>) bundle.getSerializable("playlist");
//        if (playlistVedios != null) {
//            for (int i = 0; i < playlistVedios.size(); i++) {
//                YoutubeVideoBean youtubeData = playlistVedios.get(i);
//                if (youtubeData != null) {
//                    String videoId = youtubeData.getVideoId();
//                    videoIdList.add(videoId);
//                }
//            }
//        }

       // main_layout = (LinearLayout) findViewById(R.id.main_layout);
        //baseLayout = (LinearLayout) findViewById(R.id.layout);

        //webView = (WebView) findViewById(R.id.webViewYoutube);

        youtubePlayerView = findViewById(R.id.youtubePlayerView);

// Attach lifecycle
        getLifecycle().addObserver(youtubePlayerView);

        playVideo(videoIdList.get(position));
        //playIframeVideo(videoIdList.get(position));

    }

    private void playVideo(String videoId) {

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {

                // Load & play
                youTubePlayer.loadVideo(videoId, 0);

                Log.d("YT_PLAYER", "Playing: " + videoId);
            }
        });
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youtubePlayerView != null) {
            youtubePlayerView.release();
        }
    }

//    @Nullable
//    @Override
//    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
//        return null;
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//
//    private void playIframeVideo(String videoId) {
//
//        Log.e("SAN ", " VideoId " + videoId );
//
//        String youTubeUrl = "https://www.youtube.com/embed/"+videoId+"?autoplay=1";
//        //Log.e("SAN ", " youTubeUrl " + youTubeUrl );
//        String frameVideo = "<html><body><center><iframe width=\"100%\" height=\"100%\" " +
//                "src='" + youTubeUrl + "' frameborder=\"0\" allow=\"autoplay\" allowfullscreen>" +
//                "</iframe></center></body></html>";
//
//        String regexYoutUbe = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
//        if (youTubeUrl.matches(regexYoutUbe)) {
//
//            //setting web client
//            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    return false;
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    //super.onPageFinished(view, url);
//                    //Log.e("SAN ", " Video finished url " + url);
//                }
//            });
//            //web settings for JavaScript Mode
//            webView.setWebChromeClient(new CustomWebView( main_layout, baseLayout ));
//            WebSettings webSettings = webView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            webSettings.setDomStorageEnabled(true);
//            webSettings.setLoadWithOverviewMode(true);
//            webSettings.setUseWideViewPort(true);
//            webSettings.setMediaPlaybackRequiresUserGesture(false);
//
//            webView.loadData(frameVideo, "text/html", "utf-8");
//
//        } else {
//            //Toast.makeText(this, "This is other video", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }
//
//    // SAN Custom Web View Class to allow for full screen
//    private class CustomWebView extends WebChromeClient {
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//
//        ViewGroup parent;
//        ViewGroup content;
//        View customView;
//
//        public CustomWebView(ViewGroup parent, ViewGroup content){
//            this.parent = parent;
//            this.content = content;
//        }
//
//        @Override
//        public void onShowCustomView(View view, CustomViewCallback callback) {
//            super.onShowCustomView(view, callback);
//
//            customView = view;
//            view.setLayoutParams(layoutParams);
//            parent.addView(view);
//            content.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onHideCustomView() {
//            super.onHideCustomView();
//
//            content.setVisibility(View.VISIBLE);
//            parent.removeView(customView);
//            customView = null;
//        }
//
//    }
//
//

}
