package com.ojassoft.astrosage.ui.act;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.view.ActionMode;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ojas on १३/११/१८.
 */

public class ActMantraVideoPlayer extends BaseInputActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AppCompatCallback {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface;
    ArrayList<String> videoIdList;
    int orientation = 2;
    int position;
    boolean isFirstVideo;
    private boolean fullscreen;
    private Configuration config;
    private WebView webView;

    public ActMantraVideoPlayer() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.youtube_player_lay);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER_MANTRA, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        position = bundle.getInt("position");
        videoIdList = new ArrayList<String>();
        ArrayList<ChalisaDataModel> playlistVedios = (ArrayList<ChalisaDataModel>) bundle.getSerializable("playlist");
        if (playlistVedios != null) {
            for (int i = 0; i < playlistVedios.size(); i++) {
                ChalisaDataModel youtubeData = playlistVedios.get(i);
                if (youtubeData != null) {
                    String videoId = youtubeData.getVideoURL();
                    videoIdList.add(videoId);
                }
            }
        }
        webView = (WebView) findViewById(R.id.webViewYoutube);

        playIframeVideo(videoIdList.get(position));

        //player.cueVideo(videoIdList.get(position));

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private void playIframeVideo(String videoId) {

        Log.e("SAN ", " VideoId " + videoId );

        String youTubeUrl = "https://www.youtube.com/embed/"+videoId+"?autoplay=1";
        //Log.e("SAN ", " youTubeUrl " + youTubeUrl );
        String frameVideo = "<html><body><center><iframe width=\"100%\" height=\"100%\" " +
                "src='" + youTubeUrl + "' frameborder=\"0\" allow=\"autoplay\" allowfullscreen>" +
                "</iframe></center></body></html>";

        String regexYoutUbe = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (youTubeUrl.matches(regexYoutUbe)) {

            //setting web client
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    //super.onPageFinished(view, url);
                    //Log.e("SAN ", " Video finished url " + url);
                }
            });
            //web settings for JavaScript Mode
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setMediaPlaybackRequiresUserGesture(false);

            webView.loadData(frameVideo, "text/html", "utf-8");

        } else {
            //Toast.makeText(this, "This is other video", Toast.LENGTH_SHORT).show();
        }



    }


}
