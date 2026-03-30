package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.PlaylistVedio;
import com.ojassoft.astrosage.beans.YoutubeData;
import com.ojassoft.astrosage.customadapters.YoutubePlayListAdapter;
import com.ojassoft.astrosage.customadapters.YoutubePlayListAdapter1;
import com.ojassoft.astrosage.jinterface.OnLoadMoreListener;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ojas-08 on 5/7/17.
 */
public class YoutubePlaylist extends BaseInputActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        AppCompatCallback, VolleyResponse {
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private boolean fullscreen;
    private Configuration config;
    String vedioUrl;
    YoutubePlayListAdapter youtubePlayListAdapter1;
    YoutubePlayListAdapter1 youtubePlayListAdapter2;
    RecyclerView recyclerView;
    ArrayList<YoutubeData> playlist1;
    ArrayList<PlaylistVedio> playlist2;
    String nextToken;
    String previousToken;
    String url;
    String playListUrl;
    String type;
    String playListId;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface;
    String orderBy;
    Toolbar tool_barAppModule;
    //private AppCompatDelegate delegate;
    static int selectedVideo;
    static int totalVideo;
    ArrayList<String> videoIdList;
    int orientation = 2;
    int totalPlaylistVideo;
    private WebView webView;
    private LinearLayout drawerLayout, childView;

    public YoutubePlaylist() {
        super(R.string.app_name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_youtube_playlist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        selectedVideo = 0;
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);

        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        //setSupportActionBar(tool_barAppModule);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        drawerLayout = (LinearLayout) findViewById(R.id.drawerLayout);
        childView = (LinearLayout) findViewById(R.id.childView);
        recyclerView = (RecyclerView) findViewById(R.id.playlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        videoIdList = new ArrayList<String>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getString("type");
        String title = bundle.getString("title");
        totalPlaylistVideo = bundle.getInt("totalvedio");
        //Log.i("total size>>", "" + totalPlaylistVideo);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        webView = (WebView) findViewById(R.id.webViewYoutube);

        if (type.equals("popular")) {
            playlist1 = (ArrayList<YoutubeData>) (bundle.getSerializable("playlist"));
            orderBy = bundle.getString("orderby");
            nextToken = bundle.getString("nexttoken");
            youtubePlayListAdapter1 = new YoutubePlayListAdapter(YoutubePlaylist.this, recyclerView, playlist1);
            recyclerView.setAdapter(youtubePlayListAdapter1);

            YoutubeData bean = playlist1.get(0);
            if (bean.getObjectInfo() != null) {
                vedioUrl = bean.getObjectInfo().getVedioId();
            }

            for (int i = 0; i < playlist1.size(); i++) {
                if (playlist1.get(i).getObjectInfo() != null) {
                    videoIdList.add(playlist1.get(i).getObjectInfo().getVedioId());
                }
            }
            totalVideo = videoIdList.size();
            //Log.i("Total size>>", totalVideo + "");
            youtubePlayListAdapter1.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    int totalListItem = youtubePlayListAdapter1.getItemCount();
                    //Log.i("Total>>", totalListItem + "----" + totalPlaylistVideo + "--" + totalVideo);
                    if (totalListItem < totalPlaylistVideo) {
                        youtubePlayListAdapter1.addNullVal();
                        getMoreData();
                    }
                }
            });
        } else if (type.equals("recent") || type.equals("playlist")) {
            playlist2 = (ArrayList<PlaylistVedio>) bundle.getSerializable("playlist1");
            for (int i = 0; i < playlist2.size(); i++) {
                PlaylistVedio playlistVedio = playlist2.get(i);
                if (playlistVedio != null) {
                    PlaylistVedio.Snippet snippet = playlistVedio.getSnippet();
                    if (snippet != null && snippet.getResourceId() != null) {
                        String videoId = snippet.getResourceId().getVideoId();
                        videoIdList.add(videoId);
                    }
                }
            }
            totalVideo = videoIdList.size();
            //Log.i("Total size>>", totalVideo + "");
            nextToken = bundle.getString("nexttoken");
            playListId = bundle.getString("playlistid");
            youtubePlayListAdapter2 = new YoutubePlayListAdapter1(YoutubePlaylist.this, recyclerView, playlist2);
            recyclerView.setAdapter(youtubePlayListAdapter2);

            PlaylistVedio bean = playlist2.get(0);
            if (bean != null) {
                PlaylistVedio.Snippet snippet = bean.getSnippet();
                if (snippet != null && snippet.getResourceId() != null) {
                    String videoId = snippet.getResourceId().getVideoId();
                    vedioUrl = videoId;
                }
            }

            youtubePlayListAdapter2.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    int totalListItem = youtubePlayListAdapter2.getItemCount();
                    //Log.i("Total>>", totalListItem + "----" + totalPlaylistVideo + "--" + totalVideo);
                    if (totalListItem < totalPlaylistVideo) {
                        youtubePlayListAdapter2.addNullVal();
                        getMoreData();
                    }
                }
            });
        }

        playIframeVideo(vedioUrl);

        //playIframeVideo(videoIdList.get(0));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (newConfig != null) {
                config = newConfig;

                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Log.e("SAN ", " ORIENTATION_LANDSCAPE ");
                   // webView.setWebChromeClient(new CustomWebView( drawerLayout, childView ));

                } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Log.e("SAN ", " ORIENTATION_PORTRAIT ");

                }


            }

        } catch (Exception e) {

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

    private void getMoreData() {
        if (nextToken != null && !nextToken.equals(previousToken)) {
            String urls = "";
            if (type.equals("popular")) {
                urls = url + "&pageToken=" + nextToken + "&order=" + orderBy;
            } else if (type.equals("recent") || type.equals("playlist")) {
                urls = playListUrl + "&pageToken=" + nextToken + "&playlistId=" + playListId;
            }

            ////Log.i("urls>>", urls);
            /*new GetData(urls).execute();*/
            getData(urls);
            previousToken = nextToken;

        }
    }

    public void initializeVedio(int position) {
        selectedVideo = position;

        playIframeVideo(videoIdList.get(selectedVideo));
    }

    private void getData(String url) {
        CUtils.makeGetRequest(YoutubePlaylist.this, url, 0);
    }


    @Override
    public void onResponse(String response, int method) {
        try {
            JSONObject mainJsonObject = new JSONObject(response);
            if (type.equals("popular")) {
                youtubePlayListAdapter1.removeNullVal();
                ArrayList<YoutubeData> playlistArrayList = CUtils.parseResult(mainJsonObject);
                youtubePlayListAdapter1.addData(playlistArrayList);
                for (int i = 0; i < playlistArrayList.size(); i++) {
                    if (playlistArrayList.get(i).getObjectInfo() != null) {
                        videoIdList.add(playlistArrayList.get(i).getObjectInfo().getVedioId());
                    }
                }
                youtubePlayListAdapter1.setLoaded();
            } else if (type.equals("recent") || type.equals("playlist")) {
                youtubePlayListAdapter2.removeNullVal();
                ArrayList<PlaylistVedio> playlistArrayList = CUtils.parsePlayListData(mainJsonObject);
                youtubePlayListAdapter2.addData(playlistArrayList);
                for (int i = 0; i < playlistArrayList.size(); i++) {

                    PlaylistVedio bean = playlistArrayList.get(i);
                    if (bean != null) {
                        PlaylistVedio.Snippet snippet = bean.getSnippet();
                        if (snippet != null && snippet.getResourceId() != null) {
                            String videoId = snippet.getResourceId().getVideoId();
                            videoIdList.add(videoId);
                        }
                    }
                }
                youtubePlayListAdapter2.setLoaded();
            }
            nextToken = mainJsonObject.getString("nextPageToken");

        } catch (Exception e) {
            //Log.i("Exception >>", e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError error) {

    }



    private void playIframeVideo(String videoId){
        //Log.e("SAN ", " VideoId " + videoId );

        String youTubeUrl = "https://www.youtube.com/embed/"+videoId+"?autoplay=1";

        String frameVideo = "<html><body><iframe width=\"100%\" height=\"550\" " +
                "src='" + youTubeUrl + "' frameborder=\"0\" allow=\"autoplay\" allowfullscreen>" +
                "</iframe></body></html>";

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
                    //Log.e("SAN ", " Video finished ");
                }
            });
            webView.setWebChromeClient(new CustomWebView( drawerLayout, childView ));
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


    // SAN Custom Web View Class to allow for full screen
    private class CustomWebView extends WebChromeClient {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        ViewGroup parent;
        ViewGroup content;
        View customView;

        public CustomWebView(ViewGroup parent, ViewGroup content){
            this.parent = parent;
            this.content = content;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);

            customView = view;
            view.setLayoutParams(layoutParams);
            parent.addView(view);
            content.setVisibility(View.GONE);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();

            content.setVisibility(View.VISIBLE);
            parent.removeView(customView);
            customView = null;
        }

    }



}
