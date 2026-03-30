package com.ojassoft.astrosage.ui.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.PlaylistData;
import com.ojassoft.astrosage.beans.PlaylistVedio;
import com.ojassoft.astrosage.beans.YoutubeData;
import com.ojassoft.astrosage.customadapters.PlaylistAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customviews.basic.ScrollableGridView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CustomComparator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Created by ojas-08 on 3/7/17.
 */
public class ActAstrosageTV extends BaseInputActivity implements VolleyResponse {

    String url;
    String playListUrl;
    String recentNextPageToken;
    String popularNextPageToken;
    String nextToken1;
    String nextToken2;
    String nextToken3;
    String nextToken4;
    String nextToken5;
    String nextToken6;
    String nextToken7;
    String nextToken8;
    String nextToken9;
    String nextToken10;
    String nextToken11;
    String nextToken12;
    String[] playlistId = {"PLPURdN8kWpLMn-mAa4wa9bQ1MXnC26DAo", /*"PLPURdN8kWpLOf7VSCmZ7wqo87BAWhRwRC", */"PLPURdN8kWpLO57qGSMig-y3aC0DwhEH_L",
            /*"PLPURdN8kWpLMsbVozsbuGA8VksHztBZGg",*/ "PLPURdN8kWpLNF9EQMr5-Crf4ED14sab25", "PLPURdN8kWpLNMBnL1z7Ffnibm_TCHFClt",
            "PLPURdN8kWpLPWG2TkleSczbiHSaJZqjhU", "PLPURdN8kWpLOgRXoXJ1vb7QLhscYEpIKJ", "PLPURdN8kWpLOf7VSCmZ7wqo87BAWhRwRC",
            "PLPURdN8kWpLNPhdVBtJ3RR6E49uN-8qji", "PLPURdN8kWpLNJeACsPMjUTcphAzkP6J3T", "PLPURdN8kWpLOo095PZHj9jrt2lSFjadLT"};
    String[] analyticsLabel = {
            "AstroSageTV-अंक-ज्योतिष-grid",
            /*"AstroSageTV-Learn-Astrology-grid",*/
            "AstroSageTV-Yoga-and-Spirituality-grid",
            /*"AstroSageTV-Zodiac-Signs-grid",*/
            "AstroSageTV-Hindi-grid",
            "AstroSageTV-Horoscope-grid",
            "AstroSageTV-Astrology-Tutorials-grid",
            "AstroSageTV-ज्‍योतिष कोर्स-grid",
            "AstroSageTV-Astrology-Lessons-grid",
            "AstroSageTV-12Signs-grid",
            "AstroSageTV-Myths-and-Misconceptions-grid",
            "AstroSageTV-Numerology-grid",
    };

    NetworkImageView networkImageView;
    ImageView playerIcon;
    ArrayList<PlaylistVedio> recentArrayList;
    ArrayList<YoutubeData> popularArrayList;
    ArrayList<PlaylistVedio> playlist1;
    ArrayList<PlaylistVedio> playlist2;
    ArrayList<PlaylistVedio> playlist3;
    ArrayList<PlaylistVedio> playlist4;
    ArrayList<PlaylistVedio> playlist5;
    ArrayList<PlaylistVedio> playlist6;
    ArrayList<PlaylistVedio> playlist7;
    ArrayList<PlaylistVedio> playlist8;
    ArrayList<PlaylistVedio> playlist9;
    ArrayList<PlaylistVedio> playlist10;
    ArrayList<PlaylistVedio> playlist11;
    ArrayList<PlaylistVedio> playlist12;
    private CustomProgressDialog pd = null;
    int clickedButton = R.id.recent_uploads;
    int selectedPosotion;
    String type;
    String selectedPlayListId;
    Button recentUploads;
    Button popularUploads;
    TextView moreVedios;
    String orderBy;
    String uploadsId = "UUHBM-lmpDCFzu8hpo8HjW-A";
    Toolbar tool_barAppModule;
    String title;
    String[] categoryListText;
    int totalVedio;
    HashMap<String, Integer> hm;
    private String youTubeApiKey;

    public ActAstrosageTV() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.astrotv_layout);

        youTubeApiKey = YoutubeKeySingleton.getInstance().getApiKey();
        url = CGlobalVariables.youTubePopularUrl.replace("#key", youTubeApiKey);
        playListUrl = CGlobalVariables.playListUrl.replace("#key", youTubeApiKey);

        hm = new HashMap<String, Integer>();
        categoryListText = getResources().getStringArray(R.array.playlist_category);
        int[] categoryListIcon = {R.drawable.ic_numerology,/* R.drawable.ic_astrology,*/ R.drawable.ic_yoga,
                /*R.drawable.ic_zodiac_sign,*/ R.drawable.ic_hindi, R.drawable.ic_zodiac_sign,
                R.drawable.ic_2_miniute_video, R.drawable.ic_2_miniute_video, R.drawable.ic_astrology_lesson,
                R.drawable.ic_zodiac_sign, R.drawable.ic_myths, R.drawable.ic_numerology};
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        ((TabLayout) findViewById(R.id.tabs)).setVisibility(View.GONE);
        networkImageView = (NetworkImageView) findViewById(R.id.img1);
        playerIcon = (ImageView) findViewById(R.id.vedioplayer);
        ScrollableGridView gridView = (ScrollableGridView) findViewById(R.id.playlists);
        if (CUtils.isTablet(this)) {
            gridView.setNumColumns(3);
        } else {

        }
        if (gridView != null) {
            gridView.setFocusable(false);
        }

        ArrayList<PlaylistData> entries = new ArrayList<>();
        PlaylistData playlistData;
        for (int i = 0; i < categoryListText.length; i++) {
            playlistData = new PlaylistData();
            playlistData.setBitmap(getBitmap(this, categoryListIcon[i]));
            playlistData.setPlaylistName(categoryListText[i]);
            entries.add(playlistData);
        }
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, entries);
        gridView.setAdapter(playlistAdapter);
        GetDataFromServer(playListUrl + "&playlistId=" + uploadsId);
        //new GetData(playListUrl + "&playlistId=" + uploadsId).execute();
        moreVedios = (TextView) findViewById(R.id.more_vedios);
        recentUploads = (Button) findViewById(R.id.recent_uploads);
        popularUploads = (Button) findViewById(R.id.popular_uploads);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle != null) {
            tvTitle.setTypeface(regularTypeface);
            tvTitle.setText(getResources().getString(R.string.astrosage_tv));
        }

        recentUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBy = "date";
                clickedButton = R.id.recent_uploads;
                type = "recent";
                selectedPlayListId = uploadsId;
                title = getResources().getString(R.string.recent_title);
                if (recentArrayList != null && recentArrayList.size() > 0) {
                    startNewActivity(null, recentArrayList, recentNextPageToken, title);
                }

                eventTrack("AstroSageTV-Recent-Button");
            }
        });
        popularUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButton = R.id.popular_uploads;
                type = "popular";
                orderBy = "viewcount";
                title = getResources().getString(R.string.popular_title);
                if (popularArrayList != null && popularArrayList.size() > 0) {
                    startNewActivity(popularArrayList, null, popularNextPageToken, title);
                } else {
                    GetDataFromServer(url + "&order=viewcount");
                    //new GetData(url + "&order=viewcount").execute();
                }
                eventTrack("AstroSageTV-Popular-Button");

            }
        });
        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "recent";
                orderBy = "date";
                selectedPlayListId = uploadsId;
                title = getResources().getString(R.string.recent_title);
                if (recentArrayList != null && recentArrayList.size() > 0) {
                    startNewActivity(null, recentArrayList, recentNextPageToken, title);
                }
                eventTrack("AstroSageTV-Recent-MainImage");
            }
        });
        playerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "recent";
                orderBy = "date";
                selectedPlayListId = uploadsId;
                title = getResources().getString(R.string.recent_title);
                if (recentArrayList != null && recentArrayList.size() > 0) {
                    startNewActivity(null, recentArrayList, recentNextPageToken, title);
                }
                eventTrack("AstroSageTV-Recent-MainImage");
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedButton = R.id.playlists;
                type = "playlist";
                String playListUrl;
                switch (position) {
                    case 0:
                        selectedPosotion = 0;
                        selectedPlayListId = playlistId[0];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[0];
                        title = categoryListText[0];
                        if (playlist1 != null && playlist1.size() > 0) {
                            startNewActivity(null, playlist1, nextToken1, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[0]);
                        break;
                    case 1:
                        selectedPosotion = 1;
                        selectedPlayListId = playlistId[1];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[1];
                        title = categoryListText[1];
                        if (playlist2 != null && playlist2.size() > 0) {
                            startNewActivity(null, playlist2, nextToken2, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[1]);
                        break;
                    case 2:
                        selectedPosotion = 2;
                        selectedPlayListId = playlistId[2];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[2];
                        title = categoryListText[2];
                        if (playlist3 != null && playlist3.size() > 0) {
                            startNewActivity(null, playlist3, nextToken3, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[2]);
                        break;
                    case 3:
                        selectedPosotion = 3;
                        selectedPlayListId = playlistId[3];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[3];
                        title = categoryListText[3];
                        if (playlist4 != null && playlist4.size() > 0) {
                            startNewActivity(null, playlist4, nextToken4, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[3]);
                        break;
                    case 4:
                        selectedPosotion = 4;
                        selectedPlayListId = playlistId[4];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[4];
                        title = categoryListText[4];
                        if (playlist5 != null && playlist5.size() > 0) {
                            startNewActivity(null, playlist5, nextToken5, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[4]);
                        break;
                    case 5:
                        selectedPosotion = 5;
                        selectedPlayListId = playlistId[5];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[5];
                        title = categoryListText[5];
                        if (playlist6 != null && playlist6.size() > 0) {
                            startNewActivity(null, playlist6, nextToken6, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[5]);
                        break;
                    case 6:
                        selectedPosotion = 6;
                        selectedPlayListId = playlistId[6];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[6];
                        title = categoryListText[6];
                        if (playlist7 != null && playlist7.size() > 0) {
                            startNewActivity(null, playlist7, nextToken7, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[6]);
                        break;
                    case 7:
                        selectedPosotion = 7;
                        selectedPlayListId = playlistId[7];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[7];
                        title = categoryListText[7];
                        if (playlist8 != null && playlist8.size() > 0) {
                            startNewActivity(null, playlist8, nextToken8, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[7]);
                        break;
                    case 8:
                        selectedPosotion = 8;
                        selectedPlayListId = playlistId[8];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[8];
                        title = categoryListText[8];
                        if (playlist9 != null && playlist9.size() > 0) {
                            startNewActivity(null, playlist9, nextToken9, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[8]);
                        break;
                    case 9:
                        selectedPosotion = 9;
                        selectedPlayListId = playlistId[9];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[9];
                        title = categoryListText[9];
                        if (playlist10 != null && playlist10.size() > 0) {
                            startNewActivity(null, playlist10, nextToken10, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[9]);
                        break;
                    case 10:
                        selectedPosotion = 10;
                        selectedPlayListId = playlistId[10];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[10];
                        title = categoryListText[10];
                        if (playlist11 != null && playlist11.size() > 0) {
                            startNewActivity(null, playlist11, nextToken11, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[10]);
                        break;
                    case 11:
                        selectedPosotion = 11;
                        selectedPlayListId = playlistId[11];
                        playListUrl = ActAstrosageTV.this.playListUrl + "&playlistId=" + playlistId[11];
                        title = categoryListText[11];
                        if (playlist12 != null && playlist12.size() > 0) {
                            startNewActivity(null, playlist12, nextToken12, title);
                        } else {
                            GetDataFromServer(playListUrl);
                        }
                        eventTrack(analyticsLabel[11]);
                        break;
                }
            }
        });
        setCustomTypeface();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getBitmap(Context context, int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }


   /* private class GetData extends AsyncTask<Void, Void, String> {
        String url;

        GetData(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new CustomProgressDialog(ActAstrosageTV.this, regularTypeface);
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(Void... params) {
            //InputStream inputStream = null;
            String resultStr = "";
            //InputStream is = null;
            try {
                resultStr = CUtils.makeGetRequest(url);
                return resultStr;
            } catch (Exception ex) {
                //Log.i("Tag", "1 - " + ex.getMessage());
            }
            return resultStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                JSONObject mainJsonObject = new JSONObject(result);
                totalVedio = Integer.parseInt(mainJsonObject.getJSONObject("pageInfo").getString("totalResults"));

                //networkImageView.setImageUrl(url, VolleySingleton.getInstance(ActAstrosageTV.this).getImageLoader());
                if (clickedButton == R.id.recent_uploads) {
                    if (mainJsonObject.has("nextPageToken")) {
                        recentNextPageToken = mainJsonObject.getString("nextPageToken");
                    }
                    recentArrayList = CUtils.parsePlayListData(mainJsonObject);
                    Collections.sort(recentArrayList, new CustomComparator());
                    PlaylistVedio bean = recentArrayList.get(0);
                    PlaylistVedio.Snippet snippet = bean.getSnippet();
                    PlaylistVedio.Snippet.Thumbnails thumbnail = snippet.getThumbnails();
                    PlaylistVedio.Snippet.Thumbnails.Medium medium = thumbnail.getMedium();
                    String url = medium.getUrl();

                    networkImageView.setImageUrl(url, VolleySingleton.getInstance(ActAstrosageTV.this).getImageLoader());
                    playerIcon.setVisibility(View.VISIBLE);
                    hm.put(getResources().getString(R.string.recent_title), totalVedio);
                } else if (clickedButton == R.id.popular_uploads) {
                    popularNextPageToken = mainJsonObject.getString("nextPageToken");
                    popularArrayList = CUtils.parseResult(mainJsonObject);
                    title = getResources().getString(R.string.popular_title);
                    hm.put(title, totalVedio);
                    startNewActivity(popularArrayList, null, popularNextPageToken, title);
                } else if (clickedButton == R.id.playlists) {
                    if (selectedPosotion == 0) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken1 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist1 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[0];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist1, nextToken1, title);
                    } else if (selectedPosotion == 1) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken2 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist2 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[1];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist2, nextToken2, title);
                    } else if (selectedPosotion == 2) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken3 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist3 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[2];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist3, nextToken3, title);
                    } else if (selectedPosotion == 3) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken4 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist4 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[3];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist4, nextToken4, title);
                    } else if (selectedPosotion == 4) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken5 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist5 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[4];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist5, nextToken5, title);
                    } else if (selectedPosotion == 5) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken6 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist6 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[5];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist6, nextToken6, title);
                    } else if (selectedPosotion == 6) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken7 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist7 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[6];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist7, nextToken7, title);
                    } else if (selectedPosotion == 7) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken8 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist8 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[7];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist8, nextToken8, title);
                    } else if (selectedPosotion == 8) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken9 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist9 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[8];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist9, nextToken9, title);
                    } else if (selectedPosotion == 9) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken10 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist10 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[9];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist10, nextToken10, title);
                    } else if (selectedPosotion == 10) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken11 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist11 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[10];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist11, nextToken11, title);
                    } else if (selectedPosotion == 11) {
                        if (mainJsonObject.has("nextPageToken")) {
                            nextToken12 = mainJsonObject.getString("nextPageToken");
                        }
                        playlist12 = CUtils.parsePlayListData(mainJsonObject);
                        title = categoryListText[11];
                        hm.put(title, totalVedio);
                        startNewActivity(null, playlist12, nextToken12, title);
                    }
                }
            } catch (Exception e) {
                //Log.i("Error>>", e.getMessage());
            }


        }

    }*/

    private void startNewActivity(ArrayList<YoutubeData> youtubeDataArrayList, ArrayList<PlaylistVedio> playlistVedioList, String nextPageToken, String title) {
        Intent intent = new Intent(ActAstrosageTV.this, YoutubePlaylist.class);
        Bundle bundle = new Bundle();
        if (type.equals("recent") || type.equals("playlist")) {
            bundle.putSerializable("playlist1", playlistVedioList);
            bundle.putString("playlistid", selectedPlayListId);
        } else if (type.equals("popular")) {
            bundle.putSerializable("playlist", youtubeDataArrayList);
            bundle.putString("orderby", orderBy);
        }
        bundle.putString("type", type);
        bundle.putString("nexttoken", nextPageToken);
        bundle.putString("title", title);
        bundle.putInt("totalvedio", getTotalVideo(title));
        intent.putExtras(bundle);

        /*final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(ActAstrosageTV.this);

        if (result != YouTubeInitializationResult.SUCCESS) {
            //If there are any issues we can show an error dialog.
            result.getErrorDialog(ActAstrosageTV.this, 0).show();
        } else {
            startActivity(intent);

        }*/

        startActivity(intent);


    }

    private int getTotalVideo(String title) {
        return hm.get(title);
    }

    private void setCustomTypeface() {
        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            moreVedios.setTypeface(regularTypeface);
            recentUploads.setTypeface(mediumTypeface);
            popularUploads.setTypeface(mediumTypeface);
        }
    }

    private void GetDataFromServer(String url) {
        if (!CUtils.isConnectedWithInternet(ActAstrosageTV.this)) {
            MyCustomToast mct = new MyCustomToast(ActAstrosageTV.this, ActAstrosageTV.this
                    .getLayoutInflater(), ActAstrosageTV.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            //new GetData(url).execute();
            showProgressBar();
            CUtils.makeGetRequest(ActAstrosageTV.this, url, 0);
        }
    }

    private void eventTrack(String action) {
        CUtils.googleAnalyticSendWitPlayServie(ActAstrosageTV.this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                action,
                null);

        CUtils.fcmAnalyticsEvents(action,
                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            hideProgressBar();
            JSONObject mainJsonObject = new JSONObject(response);
            totalVedio = Integer.parseInt(mainJsonObject.getJSONObject("pageInfo").getString("totalResults"));

            //networkImageView.setImageUrl(url, VolleySingleton.getInstance(ActAstrosageTV.this).getImageLoader());
            if (clickedButton == R.id.recent_uploads) {
                if (mainJsonObject.has("nextPageToken")) {
                    recentNextPageToken = mainJsonObject.getString("nextPageToken");
                }
                recentArrayList = CUtils.parsePlayListData(mainJsonObject);
                try {
                    Collections.sort(recentArrayList, new CustomComparator());
                }catch (Exception e){
                    // do nothing
                }
                PlaylistVedio bean = recentArrayList.get(0);
                PlaylistVedio.Snippet snippet = bean.getSnippet();
                PlaylistVedio.Snippet.Thumbnails thumbnail = snippet.getThumbnails();
                PlaylistVedio.Snippet.Thumbnails.Medium medium = thumbnail.getMedium();
                String url = medium.getUrl();

                networkImageView.setImageUrl(url, VolleySingleton.getInstance(ActAstrosageTV.this).getImageLoader());
                playerIcon.setVisibility(View.VISIBLE);
                hm.put(getResources().getString(R.string.recent_title), totalVedio);
            } else if (clickedButton == R.id.popular_uploads) {
                popularNextPageToken = mainJsonObject.getString("nextPageToken");
                popularArrayList = CUtils.parseResult(mainJsonObject);
                title = getResources().getString(R.string.popular_title);
                hm.put(title, totalVedio);
                startNewActivity(popularArrayList, null, popularNextPageToken, title);
            } else if (clickedButton == R.id.playlists) {
                if (selectedPosotion == 0) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken1 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist1 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[0];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist1, nextToken1, title);
                } else if (selectedPosotion == 1) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken2 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist2 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[1];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist2, nextToken2, title);
                } else if (selectedPosotion == 2) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken3 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist3 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[2];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist3, nextToken3, title);
                } else if (selectedPosotion == 3) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken4 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist4 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[3];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist4, nextToken4, title);
                } else if (selectedPosotion == 4) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken5 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist5 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[4];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist5, nextToken5, title);
                } else if (selectedPosotion == 5) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken6 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist6 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[5];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist6, nextToken6, title);
                } else if (selectedPosotion == 6) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken7 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist7 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[6];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist7, nextToken7, title);
                } else if (selectedPosotion == 7) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken8 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist8 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[7];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist8, nextToken8, title);
                } else if (selectedPosotion == 8) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken9 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist9 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[8];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist9, nextToken9, title);
                } else if (selectedPosotion == 9) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken10 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist10 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[9];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist10, nextToken10, title);
                } else if (selectedPosotion == 10) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken11 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist11 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[10];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist11, nextToken11, title);
                } else if (selectedPosotion == 11) {
                    if (mainJsonObject.has("nextPageToken")) {
                        nextToken12 = mainJsonObject.getString("nextPageToken");
                    }
                    playlist12 = CUtils.parsePlayListData(mainJsonObject);
                    title = categoryListText[11];
                    hm.put(title, totalVedio);
                    startNewActivity(null, playlist12, nextToken12, title);
                }
            }
        } catch (Exception e) {
            hideProgressBar();
        }

    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        if (error != null && error.getMessage() != null) {
            Toast.makeText(ActAstrosageTV.this, error.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActAstrosageTV.this, regularTypeface);
        }

        if (!pd.isShowing()) {
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
