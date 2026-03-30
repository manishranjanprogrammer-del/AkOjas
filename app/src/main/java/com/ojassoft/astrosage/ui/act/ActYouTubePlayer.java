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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.PlaylistVedio;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ojas on १३/११/१८.
 */

public class ActYouTubePlayer extends BaseInputActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AppCompatCallback {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private boolean fullscreen;
    private Configuration config;
    String vedioUrl;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface;
    Toolbar tool_barAppModule;
    ArrayList<String> videoIdList;
    int orientation = 2;
    int position;
    boolean fromSearch;
    boolean isFirstVideo;
    private boolean fromNotification;
    private String videoUrlNotification;

    public ActYouTubePlayer() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.youtube_player_lay);
        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER, null);

        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");


        position = bundle.getInt("position");
        fromSearch = bundle.getBoolean("fromSearch", false);
        //added by ankit
        fromNotification = bundle.getBoolean("fromNotification", false);


        if (!fromNotification) {
            videoIdList = new ArrayList<String>();
            ArrayList<PlaylistVedio> playlistVedios = (ArrayList<PlaylistVedio>) bundle.getSerializable("playlist");
            if (playlistVedios != null) {
                for (int i = 0; i < playlistVedios.size(); i++) {
                    if (fromSearch) {
                        PlaylistVedio playlistVedio = playlistVedios.get(i);
                        if (playlistVedio != null) {
                            videoIdList.add(playlistVedio.getSearchContentDetails().getVideoId());
                        }
                    } else {
                        PlaylistVedio youtubeData = playlistVedios.get(i);
                        if (youtubeData != null) {
                            PlaylistVedio.Snippet snippet = youtubeData.getSnippet();
                            if (snippet != null && snippet.getResourceId() != null) {
                                String videoId = snippet.getResourceId().getVideoId();
                                videoIdList.add(videoId);
                            }
                        }
                    }
                }
            }
        } else {
            //added by ankit
            videoUrlNotification = bundle.getString("playlist");
        }

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
    }

    private void hideSystemUI(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

}
