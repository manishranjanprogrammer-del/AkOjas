package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CUtils.openVartaTabActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.HomeArticlesAdapter;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAstroShopCategories;
import com.ojassoft.astrosage.ui.act.ActShowOjasSoftArticlesWithTabs;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.fragments.NewMagazineFrag;
import com.ojassoft.astrosage.ui.fragments.TopicSubscriptionDialog;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.Iterator;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends BaseActivity {
    String TAG = "ActShowOjasSoftArticles";
    private String title = "AstroSage.com : All Articles";
    WebView webView = null;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private Button textShowStatus;
    String englisBlogURL = "https://astrology.astrosage.com/?m=1";
    String hindiBlogURL = "https://jyotish.astrosage.com/?m=1";
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private boolean IS_ASTROSHOP = false;
    private boolean WEBVIEW_TOUCHED = false;
    Typeface typeface;
    int SCREEN_TYPE = -1;
    Menu actionMenu;
    boolean refresh = false;
    boolean activityRestarted = false;

    private TextView tvTitle;
    String articleIdLastPathSegment = null;
    private NetworkImageView imgBanner;
    private String offersUrl = "";
    ImageView ivBack;
    RelativeLayout mainlayout;

    private String IsShowBanner = "False";

    /**
     * Initializes the activity and sets up the WebView.
     * Called when the activity is first created.
     * 
     * @param savedInstanceState Bundle containing the activity's previously saved state
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        getData(getIntent());
    }

    /**
     * Handles new intents sent to this activity.
     * Used to update the WebView content when the activity is already running.
     * 
     * @param intent The new intent that was started for this activity
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData(intent);

    }

    /**
     * Extracts and processes data from the intent.
     * Sets up the WebView and initializes UI components.
     * 
     * @param intent The intent containing data to be processed
     */
    private void getData(Intent intent) {
        webView = (WebView) findViewById(R.id.webView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        mainlayout = findViewById(R.id.mainlayout);

        if(intent != null) {
            SCREEN_TYPE = intent.getIntExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, -1);
            offersUrl = intent.getStringExtra("URL");
            title = intent.getStringExtra("TITLE_TO_SHOW");
        }
        tvTitle.setText(title);
        initProgressBar();
        initWebView();
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Called when the activity is restarted.
     * Used to handle activity state changes.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        activityRestarted = true;
    }

    /**
     * Called when the activity becomes visible to the user.
     * Handles WebView reloading and network state checks.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (activityRestarted) {
            if (webView != null) {
                if (CUtils.isConnectedWithInternet(this))
                    webView.reload();
                else
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), WebViewActivity.this);
            }
        } else {
            loadUrl();
        }
    }

    /**
     * Initializes the progress bar for WebView loading.
     * Sets up the progress indicator and its layout parameters.
     */
    private void initProgressBar() {
        webView.setAlpha(0f);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        textShowStatus = new Button(this, null, android.R.attr.buttonStyle);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(this);
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }

    /**
     * Initializes the WebView with necessary settings and configurations.
     * Sets up JavaScript, zoom controls, and various WebView clients.
     */
    @SuppressLint("NewApi")
    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                // handle download, here we use brower to download, also you can try other approach.
                if (url != null) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                try {
                    showProgressBar(true);
                    //changeRefreshButton(true);
                    if (progress == 100) {
                        if (!IS_ASTROSHOP && WEBVIEW_TOUCHED) {
                            if (webView != null) {
                                setBlogTitle(webView.getTitle().toString());
                            }
                        }

                        showProgressBar(false);
                        //changeRefreshButton(false);
                    }
                } catch (Exception e) {
                    //Log.e("ActShowOjasSoftArticles", "Error" + e.getMessage());
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setAlpha(1f);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, final String description, String failingUrl) {
                view.loadUrl("about:blank");
                if (!CUtils.isConnectedWithInternet(WebViewActivity.this)) {
                    view.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), WebViewActivity.this);
                        }
                    }, 1000);
                } else {
                    view.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            CUtils.showSnackbar(mainlayout, description, WebViewActivity.this);
                        }
                    }, 1000);
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                try {
                    String _url = HomeArticlesAdapter.addParamsForDarkInURL(WebViewActivity.this,url);

                    if (_url.contains("https://play.google.com/store/apps/details")) {
                        _url = _url.replace(
                                "https://play.google.com/store/apps/details",
                                "market://details");
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(_url));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Google Play app is not installed, you may want to
                            // open the app store link
                            webView.loadUrl(_url);
                        }
                    }
                    else if (!_url.startsWith("http") && !_url.startsWith("https")) {
                        try {
                            if (_url.contains("mailto:customercare@AstroSage.com")) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("message/rfc822");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customercare@AstroSage.com"});
                                    intent.setPackage("com.google.android.gm");
                                    if (intent.resolveActivity(WebViewActivity.this.getPackageManager()) != null)
                                        startActivity(intent);
                                } catch (Exception ex) {
                                    //
                                }
                            } else {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        } catch (ActivityNotFoundException e) {
                            view.loadUrl(_url);
                        }
                    }
                    else {
                        //Updated on 05 feb 2016. using native functions from web view buttons clink
                        if (_url.contains("http://b.astrosage.com/") || _url.contains("https://b.astrosage.com/")) {
                            NewMagazineFrag.openInBuildFunction(WebViewActivity.this,new HomeInputScreen(), com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_BASIC);
                        } else if (url.contains("http://m.astrosage.com/horoscopematching/") ||
                                url.contains("https://m.astrosage.com/horoscopematching/")) {
                            NewMagazineFrag.openInBuildFunction(WebViewActivity.this,new HomeMatchMakingInputScreen(), com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_MATCHING);
                        }
                        else if (url.equals("http://buy.astrosage.com/") || url.equals("https://buy.astrosage.com/")) {
                            NewMagazineFrag.openInBuildFunction(WebViewActivity.this,new ActAstroShopCategories(), com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSHOP_SERVICES);
                        } else if (url.contains("http://buy.astrosage.com/") || url.contains(com.ojassoft.astrosage.utils.CGlobalVariables.astrosage_services_url) ||
                                url.contains("https://buy.astrosage.com/") || url.contains(com.ojassoft.astrosage.utils.CGlobalVariables.astrosage_services_urls)) {
                            com.ojassoft.astrosage.utils.CUtils.getUrlLink(_url, WebViewActivity.this, LANGUAGE_CODE, 0);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.cricketTopicUrl)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.cricketTopicUrl);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.cricketTopicUrls)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.cricketTopicUrls);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.shareMarketTopicUrl)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.shareMarketTopicUrl);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.shareMarketTopicUrls)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.shareMarketTopicUrls);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.bollywoodTopicUrl)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.bollywoodTopicUrl);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.bollywoodTopicUrls)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.bollywoodTopicUrls);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.newMagazineTopicUrl)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.newMagazineTopicUrl);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.newMagazineTopicUrls)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.newMagazineTopicUrls);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.politicsTopicUrl)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.politicsTopicUrl);
                        } else if (url.equals(com.ojassoft.astrosage.utils.CGlobalVariables.politicsTopicUrls)) {
                            openTopicSubscriptionDialog(com.ojassoft.astrosage.utils.CGlobalVariables.politicsTopicUrls);
                        } else if (_url.contains("mailto:customercare@AstroSage.com")) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customercare@AstroSage.com"});
                                intent.setPackage("com.google.android.gm");
                                if (intent.resolveActivity(WebViewActivity.this.getPackageManager()) != null)
                                    startActivity(intent);
                            } catch (Exception ex) {
                            }
                        } else if (url.contains(".pdf")) {
                            com.ojassoft.astrosage.utils.CUtils.openWebBrowser(WebViewActivity.this, Uri.parse(url));
                        } else if (url.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_FOLLOW)) {
                            Uri linkData = Uri.parse(url);
                            String articleIdLastPathSegment = linkData.getLastPathSegment();
                            openAstrologerDetail(WebViewActivity.this, articleIdLastPathSegment, false, true, linkData.toString());
                        } else if (url.contains(com.ojassoft.astrosage.utils.CGlobalVariables.talk_to_astrologers)) {
                            openVartaTabActivity(WebViewActivity.this, FILTER_TYPE_CALL);
                        } else if (url.contains(com.ojassoft.astrosage.utils.CGlobalVariables.chat_with_astrologers)) {
                            openVartaTabActivity(WebViewActivity.this, FILTER_TYPE_CHAT);
                        } else if (url.contains(com.ojassoft.astrosage.utils.CGlobalVariables.open_ai_astrologers)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(WebViewActivity.this);
                        } else {
                            webView.loadUrl(_url);
                        }
                    }
                } catch (Exception e) {
                    //Log.e("OjasSoftArticles", e.getMessage());
                }

                return true;
            }
        });

        webView.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WEBVIEW_TOUCHED = true;
                return false;
            }
        });
    }

    /**
     * Shows or hides the progress bar based on the loading state.
     * 
     * @param show true to show the progress bar, false to hide it
     */
    private void showProgressBar(boolean show) {
        try {
            if (webView != null) {
                if (progressBar != null) {
                    if (show) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        webView.setAlpha(1f);
                    }
                }
                webView.removeAllViews();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Loads the URL in the WebView.
     * Checks for internet connectivity before loading.
     */
    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(this)) {
            webView.loadUrl(getBlogUrl());
        } else {
            showProgressBar(false);
            //No Internet, Tap here to retry !
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), WebViewActivity.this);
        }
    }

    /**
     * Gets the appropriate blog URL based on the screen type and language.
     * 
     * @return The URL to be loaded in the WebView
     */
    private String getBlogUrl() {
        String blogUrl = englisBlogURL;

        if ((SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || offersUrl != null && !offersUrl.equals(""))) {
            blogUrl = offersUrl;
        }

        return blogUrl;
    }

    /**
     * Opens the topic subscription dialog for the given URL.
     * 
     * @param url The URL associated with the topic subscription
     */
    public void openTopicSubscriptionDialog(String url) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("TopicSubscriptionDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        TopicSubscriptionDialog topicSubscriptionDialog = TopicSubscriptionDialog.getInstance(url);
        topicSubscriptionDialog.show(fm, "TopicSubscriptionDialog");
        ft.commit();

    }

    /**
     * Updates the title of the blog/article in the UI.
     * 
     * @param title The title to be displayed
     */
    private void setBlogTitle(String title) {
       tvTitle.setText(title);
    }

    /**
     * Handles key events, particularly the back button.
     * Manages WebView navigation history.
     * 
     * @param keyCode The key code of the pressed key
     * @param event The key event
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                } else {
                    // changes by abhishek bcz GET_TASKS permission is depricated
                    if (isTaskRoot())
                        CUtils.restartApplication(WebViewActivity.this);
                    WebViewActivity.this.finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void redirectToOtherBrowser(String url) {
        if (url != null && !url.equals("")) {
            Uri uri = Uri.parse(url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(uri);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            webView.onPause();
        }
        super.onPause();
    }

}
