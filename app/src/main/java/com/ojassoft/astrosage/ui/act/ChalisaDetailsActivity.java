package com.ojassoft.astrosage.ui.act;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ojas on २२/७/१६.
 */
public class ChalisaDetailsActivity extends BaseInputActivity implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AppCompatCallback {
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private TextView tvTitle;
    private ChalisaDataModel modal;
    private LinearLayout baseLayout;
    private Button fullscreenButton;
    private CompoundButton checkbox;
    private View otherViews;
    private boolean fullscreen;
    //private AppCompatDelegate delegate;
    private TextView tvVidTitle;
    private Configuration config;
    private TextView tvDiscritption;

    private WebView webView;

    public ChalisaDetailsActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalisa_details);
        if (getIntent().getExtras() != null) {
            Bundle b = (Bundle) getIntent().getExtras().get("DATA");
            modal = (ChalisaDataModel) b.get("DATA");
        }
        init();
    }

    private void init() {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_navigation_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.color_title), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        baseLayout = findViewById(R.id.layout);

        fullscreenButton = findViewById(R.id.fullscreen_button);
        checkbox = findViewById(R.id.landscape_fullscreen_checkbox);
        otherViews = findViewById(R.id.other_views);
        checkbox.setOnCheckedChangeListener(this);
        // You can use your own button to switch to fullscreen too
        fullscreenButton.setOnClickListener(this);

        tvDiscritption = findViewById(R.id.tvDiscritption);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(modal.getTitle());
        tvVidTitle = findViewById(R.id.tvVidTitle);
        tvVidTitle.setText(modal.getTitle());

        webView = (WebView) findViewById(R.id.webViewYoutube);

        playIframeVideo(modal.getVideoURL());


    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
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
