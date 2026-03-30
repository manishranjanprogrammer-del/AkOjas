package com.ojassoft.astrosage.varta.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;


@SuppressLint("SetJavaScriptEnabled")
public class VartaWebViewActivity extends BaseActivity {
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.varta_webview_activity);
        getData(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData(intent);

    }

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

    @Override
    protected void onRestart() {
        super.onRestart();
        activityRestarted = true;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (activityRestarted) {
            if (webView != null) {
                if (CUtils.isConnectedWithInternet(this))
                    webView.reload();
                else
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), VartaWebViewActivity.this);
            }
        } else {
            loadUrl();
        }
    }



    private void initProgressBar() {
//		progressBar = new ProgressBar(this, null,android.R.attr.progressBarStyleLarge);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
//	    progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        textShowStatus = new Button(this, null, android.R.attr.buttonStyle);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(this);
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }

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
            public void onReceivedError(WebView view, int errorCode, final String description, String failingUrl) {
                view.loadUrl("about:blank");
                if (!CUtils.isConnectedWithInternet(VartaWebViewActivity.this)) {
                    view.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), VartaWebViewActivity.this);
                        }
                    }, 1000);
                } else {
                    view.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            CUtils.showSnackbar(mainlayout, description, VartaWebViewActivity.this);
                        }
                    }, 1000);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

                // ADDED BY BIJENDRA ON 13-05-14
                try {
                    String _url = url;
                    if (url.contains("https://play.google.com/store/apps/details")) {
                        _url = url.replace(
                                "https://play.google.com/store/apps/details",
                                "market://details");
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(_url));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Google Play app is not installed, you may want to
                            // open the app store link
                            webView.loadUrl(url);
                        }
                    } else {
                            view.loadUrl(url);
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

    private void showProgressBar(boolean show) {
        try {
            if (webView != null) {
                if (progressBar != null) {
                    if (show) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
                webView.removeAllViews();
            }
        } catch (Exception e) {
        }
    }

    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(this)) {
            webView.loadUrl(getBlogUrl());
        } else {
            showProgressBar(false);
            //No Internet, Tap here to retry !
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), VartaWebViewActivity.this);
        }
    }

    private String getBlogUrl() {
        String blogUrl = englisBlogURL;

        if ((SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || offersUrl != null && !offersUrl.equals(""))) {
            blogUrl = offersUrl;
        }

        return blogUrl;
    }


    private void setBlogTitle(String title) {
       tvTitle.setText(title);
    }

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
                        CUtils.restartApplication(VartaWebViewActivity.this);
                    VartaWebViewActivity.this.finish();
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

}
