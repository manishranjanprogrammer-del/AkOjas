package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActPlayVideo extends BaseInputActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, AppCompatCallback {

    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private CustomProgressDialog pd = null;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private TextView tvTitle, tvVidTitle, tvDiscritption;
    private SliderModal modal;

    private RelativeLayout main_layout;
    private LinearLayout baseLayout;

    private WebView webView;
    private YouTubePlayerView youTubePlayerView;

    private Button fullscreenButton;
    private CompoundButton checkbox;
    private View otherViews;
    private RequestQueue queue;

    public ActPlayVideo() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.lay_play_video);

        if (getIntent().getExtras() != null) {
            Bundle b = (Bundle) getIntent().getExtras().get("DATA");
            modal = (SliderModal) b.get("DATA");
        }

        init();
        getVideoContent();
    }

    private void init() {

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        main_layout = findViewById(R.id.main_layout);
        baseLayout = findViewById(R.id.layout);

        webView = findViewById(R.id.webViewYoutube);
        youTubePlayerView = findViewById(R.id.youtubePlayerView);

        // ATTACH PLAYER LIFECYCLE
        getLifecycle().addObserver(youTubePlayerView);

        fullscreenButton = findViewById(R.id.fullscreen_button);
        checkbox = findViewById(R.id.landscape_fullscreen_checkbox);
        otherViews = findViewById(R.id.other_views);

        tvDiscritption = findViewById(R.id.tvDiscritption);
        tvTitle = findViewById(R.id.tvTitle);
        tvVidTitle = findViewById(R.id.tvVidTitle);

        tvTitle.setText(modal.getTitle());
        tvVidTitle.setText(modal.getTitle());

        // FINAL: PLAY USING YOUTUBE PLAYER
        playYoutubeVideo(modal.getVideo_url());
    }

    // ------------------------------
    // 🔥 NEW OFFICIAL PLAYER METHOD
    // ------------------------------
    private void playYoutubeVideo(String videoId) {

        // Hide WebView Forever
        webView.setVisibility(View.GONE);

        // Show YouTube Player
        youTubePlayerView.setVisibility(View.VISIBLE);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }

    // ------------------------------
    // API Call for Description (UNCHANGED)
    // ------------------------------
    public void getVideoContent() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CGlobalVariables.GET_VIDEO_DESCRIPTION,
                response -> {
                    try {
                        response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                        parseData(response);
                    } catch (Exception e) { }
                },
                error -> { }) {

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActPlayVideo.this));
                headers.put("languageCode", String.valueOf(LANGUAGE_CODE));
                headers.put("videoid", modal.getVideoid());
                return headers;
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(60000, 1, 1f));

        queue.add(stringRequest);
    }

    private void parseData(String response) {
        try {
            JSONArray arr = new JSONArray(response);
            JSONObject obj = arr.getJSONObject(0);
            String desc = obj.getString("VideoDescriptioin");
            tvDiscritption.setText(Html.fromHtml(desc));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) { }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
