package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CUtils.appendDarkModeCheckForURL;
import static com.ojassoft.astrosage.utils.CUtils.openVartaTabActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.fragments.TopicSubscriptionDialog;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class ActYearly extends BaseInputActivity {

    private WebView webView;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private Button textShowStatus;
    private ProgressBar progressBar;
    private Menu actionMenu;
    private boolean refresh = false;
    private String url = "";
    private String title = "";
    private boolean activityRestarted = false;
    private boolean isBottomNavigation = true;
    //private boolean WEBVIEW_TOUCHED = false;

    public ActYearly() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_yearly);
        if(getIntent().hasExtra(CGlobalVariables.COMPLETE_URL)){
            url = getIntent().getStringExtra("url");
            Log.d("testCompleteurl","testCompleteurl==>>>"+url);
        }else {
            title = getIntent().getStringExtra("title");
            url =appendDarkModeCheckForURL(this, new StringBuilder(getIntent().getStringExtra("url")));
        }
        if(getIntent().hasExtra("isBottomNavigation")){
            isBottomNavigation = getIntent().getBooleanExtra("isBottomNavigation", true);
        }
        setLayRef();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.actionMenu = menu;
        getMenuInflater().inflate(R.menu.article_screen_menu, menu);
        //getSupportMenuInflater().inflate(R.menu.article_screen_menu, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (activityRestarted) {
            if (webView != null) {
                if (CUtils.isConnectedWithInternet(this))
                    webView.reload();
                else
                    showWarning(getResources().getString(
                            R.string.no_internet_tap_to_retry));
            }
        } else {
            loadUrl();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh_menu:
                if (refresh) {
                    webView.reload();
                } else {
                    webView.stopLoading();
                }
                return true;
            case R.id.action_share_menu:
                String data = title + "\n" + url;
                com.ojassoft.astrosage.varta.utils.CUtils.shareWithFriends(ActYearly.this, data);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    LinearLayout navView;
    private void setLayRef() {
        View homeBottomNav = findViewById(R.id.homeBottomNav);
        navView = findViewById(R.id.nav_view);
        if (!isBottomNavigation){
            homeBottomNav.setVisibility(View.GONE);
        }
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        if(getIntent().hasExtra(CGlobalVariables.COMPLETE_URL)) {
            View appBar = findViewById(R.id.appbarAppModule);
            appBar.setVisibility(View.GONE);
        }

        Toolbar tool_barAppModule = findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setTypeface(mediumTypeface);
        tvTitle.setText(title);

        initProgressBar();
        initWebView();
    }
    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(ActYearly.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }
    private void setBottomNavigationText() {

        // find MenuItem you want to change
        TextView navLive = navView.findViewById(R.id.txtViewLive);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);

        boolean isAIChatDisplayed = CUtils.isAIChatDisplayed(this);
        if (isAIChatDisplayed) {
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.nav_call_icons);
        }

        // set new title to the MenuItem
        //CUtils.handleFabOnActivities(this, fabOutputMaster, navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActYearly.this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        // setting click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
        //navView.getMenu().setGroupCheckable(0,false,true);
    }
    private void initWebView() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                // handle download, here we use brower to download, also you can try other approach.
                if(url != null) {
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
                    changeRefreshButton(true);
                    if (progress == 100) {
                       /* if (WEBVIEW_TOUCHED) {
                            if (webView != null) {
                                setBlogTitle(webView.getTitle().toString());
                            }
                        }*/

                        showProgressBar(false);
                        changeRefreshButton(false);
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
                if (!CUtils.isConnectedWithInternet(ActYearly.this)) {
                    view.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
                        }
                    }, 1000);
                } else {
                    view.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showWarning(description);
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
                    }
                    //ADDED BY DEEPAK ON 05-03-2015 to handle custom url schemes
                    else if (!url.startsWith("http") && !url.startsWith("https")) {
                        try {
                            if (url.contains("mailto:customercare@AstroSage.com")) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("message/rfc822");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customercare@AstroSage.com"});
                                    intent.setPackage("com.google.android.gm");
                                    if (intent.resolveActivity(getPackageManager()) != null)
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

                            view.loadUrl(url);
                        }
                    }
                    //END ON 05-03-2015
                    else {
                        //Updated on 05 feb 2016. using native functions from web view buttons clink
                        if (url.contains("http://b.astrosage.com/") || url.contains("https://b.astrosage.com/")) {
                            openInBuildFunction(new HomeInputScreen(), CGlobalVariables.MODULE_BASIC);
                        } else if (url.contains("http://m.astrosage.com/horoscopematching/") || url.contains("https://m.astrosage.com/horoscopematching/")) {
                            openInBuildFunction(new HomeMatchMakingInputScreen(), CGlobalVariables.MODULE_MATCHING);
                        } else if (url.contains("http://www.astrosage.com/horoscope/") || url.contains("http://www.astrosage.com/rashifal/") ||
                                url.contains("https://www.astrosage.com/horoscope/") || url.contains("https://www.astrosage.com/rashifal/")) {
                            openInBuildFunction(new HoroscopeHomeActivity(), CGlobalVariables.MODULE_HOROSCOPE);
                        } else if (url.equals("http://buy.astrosage.com/") || url.equals("https://buy.astrosage.com/")) {
                            openInBuildFunction(new ActAstroShopCategories(), CGlobalVariables.ASTROSHOP_SERVICES);
                        } else if (url.contains("http://buy.astrosage.com/") || url.contains(CGlobalVariables.astrosage_services_url) ||
                                url.contains("https://buy.astrosage.com/") || url.contains(CGlobalVariables.astrosage_services_urls)) {
                            CUtils.getUrlLink(url, ActYearly.this, LANGUAGE_CODE, 0);
                        } else if (url.equals(CGlobalVariables.cricketTopicUrl)) {
                            openTopicSubscriptionDialog(CGlobalVariables.cricketTopicUrl);
                        } else if (url.equals(CGlobalVariables.cricketTopicUrls)) {
                            openTopicSubscriptionDialog(CGlobalVariables.cricketTopicUrls);
                        } else if (url.equals(CGlobalVariables.shareMarketTopicUrl)) {
                            openTopicSubscriptionDialog(CGlobalVariables.shareMarketTopicUrl);
                        } else if (url.equals(CGlobalVariables.shareMarketTopicUrls)) {
                            openTopicSubscriptionDialog(CGlobalVariables.shareMarketTopicUrls);
                        } else if (url.equals(CGlobalVariables.bollywoodTopicUrl)) {
                            openTopicSubscriptionDialog(CGlobalVariables.bollywoodTopicUrl);
                        } else if (url.equals(CGlobalVariables.bollywoodTopicUrls)) {
                            openTopicSubscriptionDialog(CGlobalVariables.bollywoodTopicUrls);
                        } else if (url.equals(CGlobalVariables.newMagazineTopicUrl)) {
                            openTopicSubscriptionDialog(CGlobalVariables.newMagazineTopicUrl);
                        } else if (url.equals(CGlobalVariables.newMagazineTopicUrls)) {
                            openTopicSubscriptionDialog(CGlobalVariables.newMagazineTopicUrls);
                        }else if (url.equals(CGlobalVariables.politicsTopicUrl)) {
                            openTopicSubscriptionDialog(CGlobalVariables.politicsTopicUrl);
                        } else if (url.equals(CGlobalVariables.politicsTopicUrls)) {
                            openTopicSubscriptionDialog(CGlobalVariables.politicsTopicUrls);
                        } else if (url.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_ASTROLOGER)) {
                            Uri linkData = Uri.parse(url);
                            String articleIdLastPathSegment = linkData.getLastPathSegment();
                            openAstrologerDetail(ActYearly.this, articleIdLastPathSegment, false, false,linkData.toString());
                        } else if (url.contains(CGlobalVariables.talk_to_astrologers)) {
                            openVartaTabActivity(ActYearly.this, FILTER_TYPE_CALL);
                        } else if (url.contains(CGlobalVariables.chat_with_astrologers)) {
                            openVartaTabActivity(ActYearly.this, FILTER_TYPE_CHAT);
                        } else if (url.contains(CGlobalVariables.open_ai_astrologers)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(ActYearly.this);
                        }

                        /*else if (url.contains(CGlobalVariables.astrosage_offers_url)) {
                            CUtils.sendToShowOffers(ActShowOjasSoftArticles.this,url);
                        }*/
                        else if (url.contains("mailto:customercare@AstroSage.com")) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customercare@AstroSage.com"});
                                intent.setPackage("com.google.android.gm");
                                if (intent.resolveActivity(getPackageManager()) != null)
                                    startActivity(intent);
                            } catch (Exception ex) {
                                //
                            }
                        } else if (url.contains("https://astrosage.shop/products/") && !url.contains("data-theme-mode")) {
                            String newUrl = CUtils.getUrlWithThemeParameter(ActYearly.this, url);
                            webView.loadUrl(newUrl);
                        } else {
                            webView.loadUrl(url);
                        }
                        //webView.loadUrl(url);
                    }
                } catch (Exception e) {
                    //Log.e("OjasSoftArticles", e.getMessage());
                }

                return true;
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //WEBVIEW_TOUCHED = true;
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activityRestarted = true;
    }

    private void changeRefreshButton(boolean show) {
        try {
            if (actionMenu != null) {
                if (show) {
                    //cross
                    refresh = false;
                    actionMenu.findItem(R.id.action_refresh_menu).setIcon(R.drawable.ic_action_content_remove);
                } else {
                    //refresh
                    refresh = true;
                    actionMenu.findItem(R.id.action_refresh_menu).setIcon(R.drawable.ic_action_navigation_refresh);
                }
            }
        } catch (Exception e) {
            //Log.e("ActShowOjasSoftArticles", e.getMessage());
        }
    }


    private void showWarning(String message) {
        textShowStatus.setText(message);
        Drawable icon = getResources().getDrawable(R.drawable.ic_av_replay);
        textShowStatus.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        textShowStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
        relativeView.removeAllViews();
        relativeView.addView(textShowStatus);
        webView.removeAllViews();
        webView.addView(relativeView, params);
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
            //Log.e("ActShowOjasSoftArticles", e.getMessage());
        }
    }

    private void openInBuildFunction(Object obj, int moduleType) {

        try {
            Intent intent = null;
            if (obj instanceof HomeInputScreen) {
                intent = new Intent(ActYearly.this, HomeInputScreen.class);
            } else if (obj instanceof HomeMatchMakingInputScreen) {
                intent = new Intent(ActYearly.this, HomeMatchMakingInputScreen.class);
            } else if (obj instanceof HoroscopeHomeActivity) {
                intent = new Intent(ActYearly.this, HoroscopeHomeActivity.class);
            } else if (obj instanceof ActAstroShopCategories) {
                intent = new Intent(ActYearly.this, ActAstroShopCategories.class);
            } else if (obj instanceof ActAstroShop) {
                intent = new Intent(ActYearly.this, ActAstroShop.class);
            }

            if (intent != null) {
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
                startActivity(intent);
            }
        } catch (Exception ex) {
            //Log.i("OjasSoftArticles", ex.getMessage());
        }

    }

    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(this)) {
            webView.loadUrl(url);
          //  Log.d("loadUrl",url);
        } else {
            showProgressBar(false);
            //No Internet, Tap here to retry !
            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
        }
    }

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
}
