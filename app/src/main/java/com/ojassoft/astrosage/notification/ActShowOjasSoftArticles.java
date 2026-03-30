package com.ojassoft.astrosage.notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActAstroShop;
import com.ojassoft.astrosage.ui.act.ActAstroShopCategories;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
  import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.fragments.TopicSubscriptionDialog;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class ActShowOjasSoftArticles extends BaseInputActivity {
    String TAG = "ActShowOjasSoftArticles";
    private String title = "AstroSage.com : All Articles";
    WebView webView = null;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private TextView textShowStatus;
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
    private Toolbar toolbar_magazine;
    private TextView tvToolBarTitle;
    String articleIdLastPathSegment = null;
    private NetworkImageView imgBanner;
    private String offersUrl = "";

    private String IsShowBanner = "False";
    AdData topAdData;
    // private long MILLIs_IN_WEEK = 6 * 24 * 60 * 60 * 1000;
    private ArrayList<AdData> adList;
    // private String blogUrlFromIntent;
    public ActShowOjasSoftArticles() {
        super(R.string.astrosage_name);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getData(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData(intent);

    }

    private void getData(Intent intent) {

        webView = (WebView) findViewById(R.id.webView);
        webView.clearCache(true);
        toolbar_magazine = (Toolbar) findViewById(R.id.toolbar_magazine);
        tvToolBarTitle = (TextView) toolbar_magazine.findViewById(R.id.tvToolBarTitle);
        setSupportActionBar(toolbar_magazine);
        imgBanner = (NetworkImageView) findViewById(R.id.imgBanner);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014

        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        SCREEN_TYPE = intent.getIntExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, -1);
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) {
            offersUrl = intent.getStringExtra("URL");
            if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US) {
                title = intent.getStringExtra("TITLE_TO_SHOW");
            }
        }
        initProgressBar();
        initWebView();
        setBottomNavigationText();

        // Set up action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        // Specify that the Home button should show an "Up" caret, indicating
        // that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            try {
                title = extras.getString("TITLE_TO_SHOW");
                IS_ASTROSHOP = extras.getBoolean("IS_ASTROSHOP");
            } catch (Exception e) {
                //Log.e("BLOG_LINK_ID_ERROR", e.getMessage());
            }
        }

        getData();

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CUtils.isConnectedWithInternet(ActShowOjasSoftArticles.this)) {
                    if (SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB) {
                        CUtils.createSession(ActShowOjasSoftArticles.this, "SHOMS");

                    } else {
                        CUtils.createSession(ActShowOjasSoftArticles.this, "SMZ");

                    }

                }
                CUtils.googleAnalyticSendWitPlayServie(ActShowOjasSoftArticles.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ARTICLE_SCREEN_ADD, null);
                CUtils.fcmAnalyticsEvents(
                        CGlobalVariables.GOOGLE_ANALYTIC_ARTICLE_SCREEN_ADD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


                //Log.e("called", "Intent");
                CustomAddModel modal = topAdData.getImageObj().get(0);
                String url = modal.getImgthumbnailurl();

                if (url.contains(CGlobalVariables.buy_astrosage_url) || url.contains(CGlobalVariables.buy_astrosage_urls)
                        || url.contains(CGlobalVariables.astrocamp_product_url) || url.contains(CGlobalVariables.astrocamp_service_url)) {
                    CUtils.getUrlLink(url, ActShowOjasSoftArticles.this, LANGUAGE_CODE, 0);
                } else if (url.contains(CGlobalVariables.astrosage_offers_url) ||
                        url.contains(CGlobalVariables.astrosage_offers_urls)) {
                    if (webView != null) {
                        url = getDataThemeModeParam(url);
                        webView.loadUrl(url);
                    } else {
                        //redirectToOtherBrowser(url);
                        CUtils.sendToShowOffers(ActShowOjasSoftArticles.this, url);
                    }
                } else {
                    redirectToOtherBrowser(url);
                }

                //   CUtils.divertToScreen(ActShowOjasSoftArticles.this,modal.getImgthumbnailurl(),LANGUAGE_CODE);


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB || SCREEN_TYPE == CGlobalVariables.MODULE_ASK_OUR_ASTROLOGER || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_ARTICLES) {
                    finish();
                } else {
                    CUtils.gotoHomeScreen(ActShowOjasSoftArticles.this);
                }
                return true;
            case R.id.action_refresh_menu:
                if (refresh) {
                    webView.reload();
                } else {
                    webView.stopLoading();
                }
                return true;
            case R.id.action_open_web:
                CUtils.openWebBrowser(this,Uri.parse(offersUrl));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.actionMenu = menu;
        getMenuInflater().inflate(R.menu.web_view_screen_menu, menu);
        //getSupportMenuInflater().inflate(R.menu.article_screen_menu, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
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
                    showWarning(getResources().getString(
                            R.string.no_internet_tap_to_retry));
            }
        } else {
            loadUrl();
        }

        typeface = CUtils.getRobotoFont(
                getApplicationContext(),
                CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
        if (SCREEN_TYPE == CGlobalVariables.MODULE_CALENDAR) {
            /*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
                    typeface, getResources().getString(R.string.calendar_title));*/
            showToolBarTitle(typeface,
                    getResources().getString(R.string.calendar_title));
        } else if (SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB) {
            /*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
                    typeface, null);*/
            showToolBarTitle(typeface,
                    getString(R.string.moon_sign_calculator));
        }/* else if (SCREEN_TYPE != CGlobalVariables.MODULE_ASTROSAGE_ARTICLES) {
			CUtils.applyTypeFaceOnActionBarTitle(
					ActShowOjasSoftArticles.this,
					typeface,
					getResources().getStringArray(
							R.array.module_names_for_title_and_home_screen)[SCREEN_TYPE]);
		} */ else if ((SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_ARTICLES)
                && (title != null)) {
            setBlogTitle(title);
        } else if (SCREEN_TYPE == CGlobalVariables.MODULE_PORUTHAM)//ADDED BY BIJENDRA ON 18-02-15
        {
			/*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
					typeface, getResources().getString(R.string.porutham_title));*/
            showToolBarTitle(typeface,
                    getResources().getString(R.string.porutham_title));
        } else if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE) {
			/*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
					typeface, getResources().getString(R.string.marriage_title));*/
            showToolBarTitle(typeface,
                    getResources().getString(R.string.marriage_title));
        } //END
        else if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) {
            String data = getResources().getString(R.string.astrosage_name);
            showToolBarTitle(typeface, data);
        } //END
        else if (title == null) {
            //DISABLED BY BIJENDRA ON 17-08-15
			/*CUtils.applyTypeFaceOnActionBarTitle(
					ActShowOjasSoftArticles.this,
					typeface,
					getResources().getStringArray(
							R.array.module_names_for_title_and_home_screen)[SCREEN_TYPE]);*/
            //ADDED BY BIJENDRA ON 17-08-15
            SCREEN_TYPE = CGlobalVariables.MODULE_ASTROSAGE_ARTICLES;
            showToolBarTitle(typeface,
                    getResources().getStringArray(
                            R.array.module_names_for_title_and_home_screen)[SCREEN_TYPE]);
        } else {
            setBlogTitle(title);
        }
        //DISABLED BY BIJENDRA ON 23-06-15
        //REASON:Violating google Adv policy
		/*CUtils.showAdvertisement(ActShowOjasSoftArticles.this,
				(LinearLayout) findViewById(R.id.advLayout));*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //DISABLED BY BIJENDRA ON 23-06-15
        //REASON:Violating google Adv policy
		/*try
		{		
			CUtils.removeAdvertisement(ActShowOjasSoftArticles.this,(LinearLayout)findViewById(R.id.advLayout));
		}
		catch(Exception e)
		{
			//Log.e(TAG, e.getMessage());
		}*/
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
        //for image load
        webView.getSettings().setDomStorageEnabled(true);
        //webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
                    changeRefreshButton(true);
                    if (progress == 100) {
                        if (!IS_ASTROSHOP && WEBVIEW_TOUCHED) {
                            if (webView != null) {
                                setBlogTitle(webView.getTitle().toString());
                            }
                        }

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
                if (!CUtils.isConnectedWithInternet(ActShowOjasSoftArticles.this)) {
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
                            CUtils.getUrlLink(url, ActShowOjasSoftArticles.this, LANGUAGE_CODE, 0);
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
                        } else if (url.equals(CGlobalVariables.politicsTopicUrl)) {
                            openTopicSubscriptionDialog(CGlobalVariables.politicsTopicUrl);
                        } else if (url.equals(CGlobalVariables.politicsTopicUrls)) {
                            openTopicSubscriptionDialog(CGlobalVariables.politicsTopicUrls);
                        } else if (url.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls)
                                || url.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_url)) {
                            CUtils.getUrlLink(url, ActShowOjasSoftArticles.this, LANGUAGE_CODE, 0);
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
                        } else if (url.contains(".pdf")) {
                            CUtils.openWebBrowser(ActShowOjasSoftArticles.this, Uri.parse(url));
                        } else {
                            //Log.e("urlCheck", "shouldOverrideUrlLoading: "+getDataThemeModeParam(url) );
                            webView.loadUrl(getDataThemeModeParam(url));
                        }
                        //webView.loadUrl(url);
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

    /*ArrayList<String> CategoryFullName;
    private String[]  parseAstroShopData(String data,String lastSegment){
        String[] array = null;
        CategoryFullName = new ArrayList<>();
        try{

            JSONArray jsonArray = new JSONArray(data);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            JSONArray jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");

            for (int i=0; i<jsonArrayProductCategoryName.length(); i++){

                String catUrl = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryUrl").trim().toLowerCase();
                CategoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));

                if(catUrl.equalsIgnoreCase(lastSegment)){
                    array = new String[3];
                    array[0] = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName");
                    array[1] = jsonArrayProductCategoryName.getJSONObject(i).getString("CategorySmallDescription");
                    array[2] = i+"";
                    break;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return  array;
    }

    private boolean getItemsInDetail(String title,String mainData) {
        boolean resultFound = false;
        try {
            List<AstroShopItemDetails> alldta=new ArrayList<>();
            int pos = 0;
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject innerObject;
            JSONArray arrayOfItems;
            for (int i=1;i<jsonArray.length();i++) {
                pos = i-1;
                innerObject = jsonArray.getJSONObject(i);
                Iterator<String> iter = innerObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if(key.equals(title)){
                        arrayOfItems = innerObject.getJSONArray(title);
                        if (arrayOfItems != null) {
                            alldta.addAll((ArrayList<AstroShopItemDetails>)new Gson().fromJson(arrayOfItems.toString(), new TypeToken<ArrayList<AstroShopItemDetails>>() {
                            }.getType()));
                        }
                    }
                }
            }

            if(alldta.size() > 0){
                resultFound = checkForListInnerData(alldta,articleIdLastPathSegment,pos);
            }else{
                CUtils.callActAstroShop(0,ActShowOjasSoftArticles.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultFound;
    }

    private boolean checkForListInnerData(List<AstroShopItemDetails> alldta, String articleIdLastPathSegment,int postion) {

        boolean resultFound = false;

        for(int i = 0;i<alldta.size();i++){
            if(alldta.get(i).getP_url_text().trim().equalsIgnoreCase(articleIdLastPathSegment)){
                AstroShopItemDetails astroShopItemDetails = alldta.get(i);
                String [] array = new String[]{astroShopItemDetails.getPName(),astroShopItemDetails.getPSmallDesc()};
                // initializeGoogleIndexing(array);
                resultFound = true;
                CUtils.goToFoundedItemScreen(postion,astroShopItemDetails,ActShowOjasSoftArticles.this);
            }
        }

        return resultFound;
        *//*if(!resultFound){
            CUtils.callActAstroShop(0,ActAppModule.this);
        }*//*

    }*/

    /*
     * @date : 05 feb 2016
     * @description : This function is used to call native methods from Webview button click
     * @params :obj,moduleType
     */
    private void openInBuildFunction(Object obj, int moduleType) {

        try {
            Intent intent = null;
            if (obj instanceof HomeInputScreen) {
                intent = new Intent(ActShowOjasSoftArticles.this, HomeInputScreen.class);
            } else if (obj instanceof HomeMatchMakingInputScreen) {
                intent = new Intent(ActShowOjasSoftArticles.this, HomeMatchMakingInputScreen.class);
            } else if (obj instanceof HoroscopeHomeActivity) {
                intent = new Intent(ActShowOjasSoftArticles.this, HoroscopeHomeActivity.class);
            } else if (obj instanceof ActAstroShopCategories) {
                intent = new Intent(ActShowOjasSoftArticles.this, ActAstroShopCategories.class);
            } else if (obj instanceof ActAstroShop) {
                intent = new Intent(ActShowOjasSoftArticles.this, ActAstroShop.class);
            }

            if (intent != null) {
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
                startActivity(intent);
            }
        } catch (Exception ex) {
            //Log.i("OjasSoftArticles", ex.getMessage());
        }

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

    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(this)) {
            webView.loadUrl(getBlogUrl());
        } else {
            showProgressBar(false);
            //No Internet, Tap here to retry !
            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
        }
    }

    private String getBlogUrl() {
        String blogUrl = englisBlogURL;
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            if (LibCUtils.isSupportUnicodeHindi()) {
                blogUrl = hindiBlogURL;
            }
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTRO_SHOP) {
            blogUrl = CGlobalVariables.ASTRO_SHOP_URL;
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASK_OUR_ASTROLOGER) {
            blogUrl = CGlobalVariables.ASTRO_ASK_ASTROLOGER_URL;
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB) {
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH)
                blogUrl = CGlobalVariables.KNOW_YOUR_MOON_SIGN_BY_DOB_URL;
            else if (LANGUAGE_CODE == CGlobalVariables.HINDI)
                blogUrl = CGlobalVariables.KNOW_YOUR_MOON_SIGN_BY_DOB_URL_HI;
            else
                blogUrl = CGlobalVariables.KNOW_YOUR_MOON_SIGN_BY_DOB_URL;
        }

        //		blogUrl = CGlobalVariables.KNOW_YOUR_MOON_SIGN_BY_DOB_URL;

        if (SCREEN_TYPE == CGlobalVariables.MODULE_CALENDAR) {
            blogUrl = CGlobalVariables.CALENDAR_URL;
        }
        //ADDED BY BIJENDRA ON 18-02-15
        if (SCREEN_TYPE == CGlobalVariables.MODULE_PORUTHAM) {

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH)
                blogUrl = CGlobalVariables.PORUTHAM_URL_EN;
            else if (LANGUAGE_CODE == CGlobalVariables.HINDI)
                blogUrl = CGlobalVariables.PORUTHAM_URL_HI;
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE) {
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH)
                blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_EN;
            else if (LANGUAGE_CODE == CGlobalVariables.HINDI)
                blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_HI;
        }

        if ((SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) && offersUrl != null && !offersUrl.equals("")) {
            blogUrl = offersUrl;
        }

       /* if(blogUrlFromIntent != null){
            blogUrl = blogUrlFromIntent;
        }*/
        //END
        blogUrl = getDataThemeModeParam(blogUrl);
       // Log.e("urlCheck", "getBlogUrl: "+blogUrl );

        return blogUrl;
    }

    private String getDataThemeModeParam(String url) {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        String appendSymbol = url.contains("?")?"&":"?"; // it checks if url already contains another params then use "&" else add as new param using "?"
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            url = url+appendSymbol+"data-theme-mode=dark";
        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            url = url+appendSymbol+"data-theme-mode=light";
        }
        return url;
    }

    private void showWarning(String message) {
        textShowStatus.setText(message);
        Drawable icon = getResources().getDrawable(R.drawable.ic_av_replay);
        icon.setTint(getColor(R.color.colorPrimary_day_night));
        textShowStatus.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        textShowStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
        textShowStatus.setTextColor(getColor(R.color.black));
        textShowStatus.setBackgroundColor(getColor(R.color.backgroundColorWhiteBlack));
        textShowStatus.setElevation(0);
        relativeView.removeAllViews();
        relativeView.addView(textShowStatus);
        relativeView.setBackgroundColor(getColor(R.color.backgroundColorWhiteBlack));
        webView.removeAllViews();
        webView.addView(relativeView, params);
    }

    private void setBlogTitle(String title) {
        //CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this, Typeface.DEFAULT,title);
        showToolBarTitle(Typeface.DEFAULT, title);
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
                        CUtils.restartApplication(ActShowOjasSoftArticles.this);
                    ActShowOjasSoftArticles.this.finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isApplicatonOpened() {
        boolean haveActivitiesInTask = false;
        ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(20);
        Iterator<RunningTaskInfo> itr = runningTaskInfoList.iterator();
        while (itr.hasNext()) {
            RunningTaskInfo runningTaskInfo = (RunningTaskInfo) itr.next();
            if (runningTaskInfo.baseActivity.getPackageName().equalsIgnoreCase("com.ojassoft.astrosage")) {
                int numOfActivities = runningTaskInfo.numActivities;
                if (numOfActivities > 1)
                    haveActivitiesInTask = true;
            }

        }
        return haveActivitiesInTask;
    }

    /**
     * This function is used to show tool bar title
     *
     * @param typeface
     * @param titleToshow 17-Aug-2015
     */
    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        if (tvToolBarTitle != null) {
            tvToolBarTitle.setTypeface(typeface);
            if (titleToshow != null)
                tvToolBarTitle.setText(titleToshow);
            else
                // ADDED BY BIJENDRA ON 17-06-14
                tvToolBarTitle.setText("");
        }

    }

    /**
     * @author amit Rautela
     * @desc This method is used to get the data from cointainer
     */
  /*  private void getDataFromGTMCointainer() {
        try {
            if (ContainerHolderSingleton.getContainerHolder() != null) {
                Container container = ContainerHolderSingleton.getContainerHolder().getContainer();
                boolean atrosageArticleCustomAdsVisibility = container.getBoolean(CGlobalVariables.key_AstrosageArticleCustomAdsVisibility);
                if (atrosageArticleCustomAdsVisibility) {
                    String astrosageArticleCustomAdsImageData = container.getString(CGlobalVariables.key_AstrosageArticleCustomAdsImageUrl);
                    String astrosageArticleCustomAdsImageClickListenerUrl = container.getString(CGlobalVariables.key_AstrosageArticleCustomAdsImageClickListenerUrl);

                    String key = CUtils.getLanguageKey(LANGUAGE_CODE);
                    String astrosageArticleCustomAdsImageUrl = CUtils.getLanguageBasedUrl(astrosageArticleCustomAdsImageData, key);
                    if (!astrosageArticleCustomAdsImageUrl.equals("")) {
                        imgBanner.setVisibility(View.VISIBLE);
                        imgBanner.setImageUrl(astrosageArticleCustomAdsImageUrl, VolleySingletonForDefaultHttp.getInstance(ActShowOjasSoftArticles.this).getImageLoader());
                        setImgBannerClickListener(astrosageArticleCustomAdsImageClickListenerUrl);
                    } else {
                        //due to some error
                        imgBanner.setOnClickListener(null);
                        imgBanner.setVisibility(View.GONE);
                    }
                } else {
                    imgBanner.setOnClickListener(null);
                    imgBanner.setVisibility(View.GONE);
                }
                if (CUtils.getUserPurchasedPlanFromPreference(ActShowOjasSoftArticles.this) != 1) {
                    imgBanner.setOnClickListener(null);
                    imgBanner.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
            //Log.i("ActAppModule", "getDataFromCointainer - " + ex.getMessage().toString());
        }
    }*/

    /**
     * @param url
     * @desc This method is used to set the listener of Image Ad Banner
     */


    private void redirectToOtherBrowser(String url) {
        if (url != null && !url.equals("")) {
            Uri uri = Uri.parse(url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(uri);
            startActivity(i);
        }
    }


    private void getData() {

        try {
            String result = CUtils.getStringData(this, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "24");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTopAdd(AdData topData) {
        getData();
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;

        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (imgBanner != null) {

                imgBanner.setVisibility(View.GONE);
            }
        } else {

            if (imgBanner != null) {
                imgBanner.setVisibility(View.VISIBLE);
                imgBanner.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(this).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(this) != 1) {
            if (imgBanner != null) {
                imgBanner.setVisibility(View.GONE);
            }
        }

    }

    //Open dialog for topic subscription
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


    private void setBottomNavigationText() {

        // find MenuItem you want to change
        TextView navLive = findViewById(R.id.txtViewLive);
        ImageView navCallImg = findViewById(R.id.imgViewCall);
        TextView navCallTxt = findViewById(R.id.txtViewCall);
        TextView navChatTxt = findViewById(R.id.txtViewChat);
        ImageView navHisImg = findViewById(R.id.imgViewHistory);
        TextView navHisTxt = findViewById(R.id.txtViewHistory);

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
//        CUtils.handleFabOnActivities(this, fabOutputMaster, navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActShowOjasSoftArticles.this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        // setting click listener
        findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
        //navView.getMenu().setGroupCheckable(0,false,true);
    }
}
