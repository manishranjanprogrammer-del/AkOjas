package com.ojassoft.astrosage.ui.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.misc.VolleyResponse;
import com.ojassoft.astrosage.misc.VolleyServiceHandler;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.HomeArticlesAdapter;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAstroShop;
import com.ojassoft.astrosage.ui.act.ActAstroShopCategories;
import com.ojassoft.astrosage.ui.act.ActShowOjasSoftArticlesWithTabs;
import com.ojassoft.astrosage.ui.act.ActYearly;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CUtils.openVartaTabActivity;
import static com.ojassoft.astrosage.utils.CUtils.showSnackbar;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_DESCRIPTION_URL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_STATUS_N_PRICE_URL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROSAGE_MAGAZINE_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FOLLOW_ASTROLOGER_URL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FOLLOW;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_IAPI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.URL_TEXT;
import static com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName;
import static com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey;
import static com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserID;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus;
import static com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail;
import static com.ojassoft.astrosage.varta.utils.CUtils.subscribeFollowTopic;

import org.json.JSONObject;

/**
 * Created by ojas on १९/३/१८.
 */

public class NewMagazineFrag extends Fragment {
    WebView webView;
    NetworkImageView imgBanner;
    Activity activity;
    ProgressBar progressBar;
    private Button textShowStatus;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private ArrayList<AdData> adList;
    AdData topAdData;
    int SCREEN_TYPE = -1;
    String offersUrl = "";
    String title = "AstroSage.com : All Articles";
    boolean IS_ASTROSHOP = false;
    boolean refresh = false;
    /* String englisBlogURL = "http://astrology.astrosage.com";
     String hindiBlogURL = "http://jyotish.astrosage.com";*/
    String englisBlogURL = "https://horoscope.astrosage.com";
    String hindiBlogURL = "https://horoscope.astrosage.com/hindi";
    private String IsShowBanner = "False";
    private boolean WEBVIEW_TOUCHED = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_notification, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.setAlpha(0);
        imgBanner = (NetworkImageView) rootView.findViewById(R.id.imgBanner);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_magazine);
        View homeBottomNav = (View) rootView.findViewById(R.id.homeBottomNav);
        homeBottomNav.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        isDarkEnabled(getResources().getConfiguration());

        Log.e("SAN ", " NewMagazine ");
        getData();
        return rootView;
    }

    boolean isDarkEnabled =  false;

    public boolean isDarkEnabled(Configuration config){
        int nightModeFlags = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            isDarkEnabled = true;
        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            isDarkEnabled = false;
        }
        return isDarkEnabled;
    }
    private void getData() {


        //SCREEN_TYPE = intent.getIntExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, -1);
        SCREEN_TYPE = ((ActShowOjasSoftArticlesWithTabs) activity).SCREEN_TYPE;
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) {
            offersUrl = ((ActShowOjasSoftArticlesWithTabs) activity).offersUrl;
            if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US) {
                title = ((ActShowOjasSoftArticlesWithTabs) activity).title;
            }
        }
        initProgressBar();
        initWebView();


      /*  Bundle extras = intent.getExtras();
        if (extras != null) {*/
        try {
            title = ((ActShowOjasSoftArticlesWithTabs) activity).title;
            IS_ASTROSHOP = ((ActShowOjasSoftArticlesWithTabs) activity).IS_ASTROSHOP;
        } catch (Exception e) {
            //Log.e("BLOG_LINK_ID_ERROR", e.getMessage());
        }
        //}

        getAdData();

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CUtils.isConnectedWithInternet(activity)) {
                    if (SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB) {
                        CUtils.createSession(activity, "SHOMS");

                    } else {
                        CUtils.createSession(activity, "SMZ");

                    }

                }
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ARTICLE_SCREEN_ADD, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ARTICLE_SCREEN_ADD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                //Log.e("called", "Intent");
                CustomAddModel modal = topAdData.getImageObj().get(0);
                String url = modal.getImgthumbnailurl();

                if (url.contains(CGlobalVariables.buy_astrosage_url) || url.contains(CGlobalVariables.buy_astrosage_urls)
                        || url.contains(CGlobalVariables.astrocamp_product_url) || url.contains(CGlobalVariables.astrocamp_service_url)) {
                    CUtils.getUrlLink(url, activity, ((BaseInputActivity) activity).LANGUAGE_CODE, 0);
                } else if (url.contains(CGlobalVariables.talk_to_astrologers)) {
                    CUtils.googleAnalyticSendWitPlayServie(activity, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY, null);
                    openVartaTabActivity(activity, FILTER_TYPE_CALL);
                } else if (url.contains(CGlobalVariables.chat_with_astrologers)) {
                    CUtils.googleAnalyticSendWitPlayServie(activity, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_CHAT, null);
                    openVartaTabActivity(activity, FILTER_TYPE_CHAT);
                } else if (url.contains(CGlobalVariables.open_ai_astrologers)) {
                    CUtils.googleAnalyticSendWitPlayServie(activity, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_AI_CHAT, null);
                    com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(activity);
                } else if (url.contains(CGlobalVariables.astrosage_offers_url) ||
                        url.contains(CGlobalVariables.astrosage_offers_urls)) {
                    if (webView != null) {
                        webView.loadUrl(url);
                    } else {
                        //redirectToOtherBrowser(url);
                        CUtils.sendToShowOffers(activity, url);
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
                    activity.finish();
                } else {
                    CUtils.gotoHomeScreen(activity);
                }
                return true;
            case R.id.action_refresh_menu:
                if (webView != null) {
                    if (refresh) {
                        webView.reload();
                    } else {
                        webView.stopLoading();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        if (((ActShowOjasSoftArticlesWithTabs) activity).activityRestarted) {
            if (webView != null) {
                if (CUtils.isConnectedWithInternet(activity))
                    webView.reload();
                else
                    showWarning(getResources().getString(
                            R.string.no_internet_tap_to_retry));
            }
        } else {
            loadUrl();
        }

        if (SCREEN_TYPE == CGlobalVariables.MODULE_CALENDAR) {
            /*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
                    typeface, getResources().getString(R.string.calendar_title));*/
            showToolBarTitle(((BaseInputActivity) activity).regularTypeface,
                    getResources().getString(R.string.calendar_title));
        } else if (SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB) {
            /*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
                    typeface, null);*/
            showToolBarTitle(((BaseInputActivity) activity).regularTypeface,
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
            showToolBarTitle(((BaseInputActivity) activity).regularTypeface,
                    getResources().getString(R.string.porutham_title));
        } else if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE) {
            /*CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this,
                    typeface, getResources().getString(R.string.marriage_title));*/
            showToolBarTitle(((BaseInputActivity) activity).regularTypeface,
                    getResources().getString(R.string.marriage_title));
        } //END
        else if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) {
            String data = getResources().getString(R.string.astrosage_name);
            showToolBarTitle(((BaseInputActivity) activity).regularTypeface, data);
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
            showToolBarTitle(CUtils.getRobotoFont(getContext(),((BaseInputActivity) activity).LANGUAGE_CODE,CGlobalVariables.regular),
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
    public void onPause() {
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

//	    progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        textShowStatus = new Button(activity, null, android.R.attr.buttonStyle);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(activity);
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }

    @SuppressLint("NewApi")
    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                try {
                    showProgressBar(true);
                    changeRefreshButton(true);
                    if (progress == 100) {
                        if (!IS_ASTROSHOP && WEBVIEW_TOUCHED) {
                            if (webView != null) {
                                //setBlogTitle(webView.getTitle().toString());
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
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setAlpha(1f);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, final String description, String failingUrl) {
                if (view != null)
                    view.loadUrl("about:blank");
                if (activity == null) return;
                if (!CUtils.isConnectedWithInternet(activity)) {
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
                            if (description != null)
                                showWarning(description);
                        }
                    }, 1000);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

                // Do something with the URL
                // ADDED BY BIJENDRA ON 13-05-14
                try {
                    String _url = HomeArticlesAdapter.addParamsForDarkInURL(activity,url);

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
                    //ADDED BY DEEPAK ON 05-03-2015 to handle custom url schemes
                    else if (!_url.startsWith("http") && !_url.startsWith("https")) {
                        try {
                            if (_url.contains("mailto:customercare@AstroSage.com")) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("message/rfc822");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customercare@AstroSage.com"});
                                    intent.setPackage("com.google.android.gm");
                                    if (intent.resolveActivity(activity.getPackageManager()) != null)
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
                    //END ON 05-03-2015
                    else {
                        //Updated on 05 feb 2016. using native functions from web view buttons clink
                        if (_url.contains("http://b.astrosage.com/") || _url.contains("https://b.astrosage.com/")) {
                            openInBuildFunction(activity,new HomeInputScreen(), CGlobalVariables.MODULE_BASIC);
                        } else if (url.contains("http://m.astrosage.com/horoscopematching/") ||
                                url.contains("https://m.astrosage.com/horoscopematching/")) {
                            openInBuildFunction(activity,new HomeMatchMakingInputScreen(), CGlobalVariables.MODULE_MATCHING);
                        }
                        //Added by Amit Sharma to remove deeplinking on url.contains("http://www.astrosage.com/horoscope/ urls
                        /*else if (url.contains("http://www.astrosage.com/horoscope/") || url.contains("http://www.astrosage.com/rashifal/")||
                                url.contains("https://www.astrosage.com/horoscope/") || url.contains("https://www.astrosage.com/rashifal/")) {
                            openInBuildFunction(new HoroscopeHomeActivity(), CGlobalVariables.MODULE_HOROSCOPE);
                        }*/
                        else if (url.equals("http://buy.astrosage.com/") || url.equals("https://buy.astrosage.com/")) {
                            openInBuildFunction(activity,new ActAstroShopCategories(), CGlobalVariables.ASTROSHOP_SERVICES);
                        } else if (url.contains("http://buy.astrosage.com/") || url.contains(CGlobalVariables.astrosage_services_url) ||
                                url.contains("https://buy.astrosage.com/") || url.contains(CGlobalVariables.astrosage_services_urls)) {
                            CUtils.getUrlLink(_url, activity, ((BaseInputActivity) activity).LANGUAGE_CODE, 0);
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
                        }/*else if (url.contains(CGlobalVariables.astrosage_offers_url)) {
                            CUtils.sendToShowOffers(ActShowOjasSoftArticles.this,url);
                        }*/ else if (_url.contains("mailto:customercare@AstroSage.com")) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customercare@AstroSage.com"});
                                intent.setPackage("com.google.android.gm");
                                if (intent.resolveActivity(activity.getPackageManager()) != null)
                                    startActivity(intent);
                            } catch (Exception ex) {
                                //
                            }
                        } else if (url.contains(".pdf")) {
                            CUtils.openWebBrowser(getActivity(), Uri.parse(url));
                        } else if (url.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_FOLLOW)) {
                            Uri linkData = Uri.parse(url);
                            String articleIdLastPathSegment = linkData.getLastPathSegment();
                            openAstrologerDetail(activity, articleIdLastPathSegment, false, true, linkData.toString());
                            //CUtils.openWebBrowserOnlyChrome(getActivity(), Uri.parse(url));
                                /*if (getUserLoginStatus(activity)) {
                                    Uri linkData = Uri.parse(url);
                                    String articleIdLastPathSegment = linkData.getLastPathSegment();
                                    if (articleIdLastPathSegment == null) articleIdLastPathSegment = "";
                                    getAstrologerDetails(articleIdLastPathSegment);
                                } else {
                                    showAstrologerFollowDialog(false);
                                }*/
                        } else if (url.contains(CGlobalVariables.talk_to_astrologers)) {
                            openVartaTabActivity(activity, FILTER_TYPE_CALL);
                        } else if (url.contains(CGlobalVariables.chat_with_astrologers)) {
                            openVartaTabActivity(activity, FILTER_TYPE_CHAT);
                        } else if (url.contains(CGlobalVariables.open_ai_astrologers)) {
                            com.ojassoft.astrosage.varta.utils.CUtils.openAIChatList(activity);
                        } else {
                            webView.loadUrl(_url);
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
    public static void openInBuildFunction(Activity activity,Object obj, int moduleType) {

        try {
            Intent intent = null;
            if (obj instanceof HomeInputScreen) {
                intent = new Intent(activity, HomeInputScreen.class);
            } else if (obj instanceof HomeMatchMakingInputScreen) {
                intent = new Intent(activity, HomeMatchMakingInputScreen.class);
            } else if (obj instanceof HoroscopeHomeActivity) {
                intent = new Intent(activity, HoroscopeHomeActivity.class);
            } else if (obj instanceof ActAstroShopCategories) {
                intent = new Intent(activity, ActAstroShopCategories.class);
            } else if (obj instanceof ActAstroShop) {
                intent = new Intent(activity, ActAstroShop.class);
            }

            if (intent != null) {
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
                activity.startActivity(intent);
            }
        } catch (Exception ex) {
            //Log.i("OjasSoftArticles", ex.getMessage());
        }

    }

    private void changeRefreshButton(boolean show) {
        try {
            if (((ActShowOjasSoftArticlesWithTabs) activity).actionMenu != null) {
                if (show) {
                    //cross
                    refresh = false;
                    ((ActShowOjasSoftArticlesWithTabs) activity).actionMenu.findItem(R.id.action_refresh_menu).setIcon(R.drawable.ic_action_content_remove);
                } else {
                    //refresh
                    refresh = true;
                    ((ActShowOjasSoftArticlesWithTabs) activity).actionMenu.findItem(R.id.action_refresh_menu).setIcon(R.drawable.ic_action_navigation_refresh);
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
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isDarkEnabled(newConfig);
    }
    
    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(activity)) {
                webView.loadUrl(HomeArticlesAdapter.addParamsForDarkInURL(activity,getBlogUrl()));
           // webView.loadUrl(getBlogUrl());
            // webView.loadUrl(ActShowOjasSoftArticlesWithTabs.pathStr);
        } else {
            showProgressBar(false);
            //No Internet, Tap here to retry !
            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
        }
    }

    public String getBlogUrl() {
        //String blogUrl = englisBlogURL;
        String blogUrl = ActShowOjasSoftArticlesWithTabs.pathStr;

        if (blogUrl != null && !blogUrl.equals("")) {

            if (blogUrl.contains("astrology.astrosage.com")
                    || blogUrl.contains("www.astrology.astrosage.com")
                    || blogUrl.contains("jyotish.astrosage.com")
                    || blogUrl.contains("www.jyotish.astrosage.com")) {
                blogUrl = englisBlogURL;
                if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    if (LibCUtils.isSupportUnicodeHindi()) {
                        blogUrl = hindiBlogURL;
                    }
                }
            } else if (blogUrl.contains("horoscope.astrosage.com")
                    || blogUrl.contains("www.horoscope.astrosage.com")
                    || blogUrl.contains("horoscope.astrosage.com/hindi")
                    || blogUrl.contains("www.horoscope.astrosage.com/hindi")) {
                // extra condition
            }
        }

        if (blogUrl == null || blogUrl.equals("")) {
            blogUrl = englisBlogURL; // bydefault english url
            if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI) {
                if (LibCUtils.isSupportUnicodeHindi()) {
                    blogUrl = hindiBlogURL;
                }
            }
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTRO_SHOP) {
            blogUrl = CGlobalVariables.ASTRO_SHOP_URL;
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASK_OUR_ASTROLOGER) {
            blogUrl = CGlobalVariables.ASTRO_ASK_ASTROLOGER_URL;
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB) {
            if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.ENGLISH)
                blogUrl = CGlobalVariables.KNOW_YOUR_MOON_SIGN_BY_DOB_URL;
            else if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI)
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

            if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.ENGLISH)
                blogUrl = CGlobalVariables.PORUTHAM_URL_EN;
            else if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI)
                blogUrl = CGlobalVariables.PORUTHAM_URL_HI;
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE) {
            if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.ENGLISH)
                blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_EN;
            else if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI)
                blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_HI;
        }

        if ((SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) && offersUrl != null && !offersUrl.equals("")) {
            blogUrl = offersUrl;
        }

       /* if(blogUrlFromIntent != null){
            blogUrl = blogUrlFromIntent;
        }*/
        //END

        return blogUrl;
    }

    private void showWarning(String message) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBlogTitle(String title) {
        //CUtils.applyTypeFaceOnActionBarTitle(ActShowOjasSoftArticles.this, Typeface.DEFAULT,title);
        showToolBarTitle(Typeface.DEFAULT, title);
    }


    public boolean doActionOnBackPress() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            if (activity != null) {
                // changes by abhishek bcz GET_TASKS permission is depricated
                if (activity.isTaskRoot()) {
                    CUtils.restartApplication(activity);
                }
                activity.finish();
            }
            return false;
        }
    }


    private boolean isApplicatonOpened() {
        boolean haveActivitiesInTask = false;
        try {
            if (activity != null) {
                ActivityManager m = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(20);
                Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
                while (itr.hasNext()) {
                    ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) itr.next();
                    if (runningTaskInfo.baseActivity.getPackageName().equalsIgnoreCase("com.ojassoft.astrosage")) {
                        int numOfActivities = runningTaskInfo.numActivities;
                        if (numOfActivities > 1)
                            haveActivitiesInTask = true;
                    }

                }
            }
        } catch (Exception ex) {
            //
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
        if (((ActShowOjasSoftArticlesWithTabs) activity).tvTitle != null) {
            ((ActShowOjasSoftArticlesWithTabs) activity).tvTitle.setTypeface(typeface);
            if (titleToshow != null)
                ((ActShowOjasSoftArticlesWithTabs) activity).tvTitle.setText(titleToshow);
            else
                // ADDED BY BIJENDRA ON 17-06-14
                ((ActShowOjasSoftArticlesWithTabs) activity).tvTitle.setText("");
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


    private void getAdData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
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
        getAdData();
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;

        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (imgBanner != null) {

               // SAN Commented
                //  imgBanner.setVisibility(View.GONE);
            }
        } else {

            if (imgBanner != null) {
                // SAN Commented
                //imgBanner.setVisibility(View.VISIBLE);
                imgBanner.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(activity).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (imgBanner != null) {
                // SAN Commented
                //imgBanner.setVisibility(View.GONE);
            }
        }

    }

    //Open dialog for topic subscription
    public void openTopicSubscriptionDialog(String url) {
        FragmentManager fm = getChildFragmentManager();
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

    public void refreshWebView() {
        if (webView != null && refresh) {
            webView.reload();
        } else {
            webView.stopLoading();
        }
    }

    int ASTROLOGER_DETAIL_METHOD = 1;
    int ASTRO_STATUS_UPDATE = 2;
    int FOLLOW_ASTROLOGER_REQ = 3;
    RequestQueue queue;

    /*private void getAstrologerDetails(String urlText) {
        if (queue == null)
            queue = VolleySingleton.getInstance(activity).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, ASTROLOGER_DESCRIPTION_URL,
                NewMagazineFrag.this, false, getAstroDetailsParams(urlText),
                ASTROLOGER_DETAIL_METHOD).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    public Map<String, String> getAstroDetailsParams(String urlText) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(APP_KEY, getApplicationSignatureHashCode(activity));
        boolean isLogin = getUserLoginStatus(activity);
        try {
            if (isLogin) {
                headers.put(PHONE_NO, getUserID(activity));
                headers.put(COUNTRY_CODE, getCountryCode(activity));
            } else {
                headers.put(PHONE_NO, "");
                headers.put(COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(URL_TEXT, urlText);
        headers.put(PACKAGE_NAME, getAppPackageName(activity));
        headers.put(APP_VERSION, "" + BuildConfig.VERSION_NAME);
        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(LANG, getLanguageKey(LANGUAGE_CODE));

        return headers;
    }

    private void getAstrologerStatusPrice(String astroid) {
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, ASTROLOGER_STATUS_N_PRICE_URL,
                NewMagazineFrag.this, false, getAstroStatusParams(astroid), ASTRO_STATUS_UPDATE).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    public Map<String, String> getAstroStatusParams(String astroId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(APP_KEY, getApplicationSignatureHashCode(activity));
        boolean isLogin = getUserLoginStatus(activity);
        try {
            if (isLogin) {
                headers.put(PHONE_NO, getUserID(activity));
                headers.put(COUNTRY_CODE, getCountryCode(activity));
            } else {
                headers.put(PHONE_NO, "");
                headers.put(COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(ASTROLOGER_ID, astroId);
        headers.put(PACKAGE_NAME, getAppPackageName(activity));
        headers.put(APP_VERSION, "" + BuildConfig.VERSION_NAME);
        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(DEVICE_ID, getMyAndroidId(activity));
        headers.put(LANG, getLanguageKey(LANGUAGE_CODE));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_USER_ID, getUserIdForBlock(activity));
        // strPDialoag = headers.toString();
        return headers;
    }

    public void followAstrologerRequest() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(activity)) {
                String url = FOLLOW_ASTROLOGER_URL;
                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                        NewMagazineFrag.this, false, getfollowAstrologerParams(), FOLLOW_ASTROLOGER_REQ).getMyStringRequest();
                stringRequest.setShouldCache(true);
                if (queue == null)
                    queue = VolleySingleton.getInstance(activity).getRequestQueue();

                queue.add(stringRequest);
            } else {
                //CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), currentActivity);
                Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Log.d("mResponse",e.toString());
        }
    }

    public Map<String, String> getfollowAstrologerParams() {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(CGlobalVariables.KEY_API, getApplicationSignatureHashCode(currentActivity));
        headers.put(ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.KEY_USER_ID, getUserIdForBlock(activity));
        headers.put(KEY_FOLLOW, "1");
        headers.put(KEY_IAPI, "1");
        headers.put(DEVICE_ID, getMyAndroidId(currentActivity));
        headers.put(CGlobalVariables.PACKAGE_NAME, getAppPackageName(currentActivity));
        headers.put(APP_VERSION, "" + BuildConfig.VERSION_NAME);
        return headers;
    }

    String astroId = "", astroName = "", followStatus = "";

    @Override
    public void onResponse(String response, int method) {
        Log.d("mResponse",method+" == "+response);
        if (method == ASTROLOGER_DETAIL_METHOD){
            try {
                JSONObject astrologerObj = new JSONObject(response);
                astroId = astrologerObj.getString("ai");
                astroName = astrologerObj.getString("n");
                getAstrologerStatusPrice(astroId);
            } catch (Exception e){
                //
            }
        } else if (method == ASTRO_STATUS_UPDATE){
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("followbyuser")) {
                    followStatus = jsonObject.getString("followbyuser");
                    showAstrologerFollowDialog(true);
                }
            } catch (Exception e){
                //
            }
        } else if (method == FOLLOW_ASTROLOGER_REQ){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                if(status.equals("1")) {
                    if(followStatus!=null && followStatus.equals("0")) {
                        followStatus="1";
                        subscribeFollowTopic(activity, astroId);
                    }
                } else {
                    showSnackbar(activity.findViewById(android.R.id.content), message, activity);
                }
            } catch (Exception e){
                //
            }
        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    public void showAstrologerFollowDialog(boolean loginStatus){
        String title = getString(R.string.follow_astrologer);
        String message = getString(R.string.follow_to_get_regular_updates);
        message = message.replace("#",astroName);
        String yes = activity.getResources().getString(R.string.confirm_txt), no = activity.getResources().getString(R.string.no);

        if (!loginStatus){
            yes = activity.getResources().getString(R.string.login);
            no = getString(R.string.not_now);
            title = getString(R.string.login_to_follow);
            message = getString(R.string.login_to_follow_astrologer);
        } else if (followStatus != null && followStatus.equals("1")){
            title = getString(R.string.already_following);
            message = getString(R.string.you_are_alredy_following);
            message = message.replace("#",astroName);
            yes = activity.getResources().getString(R.string.ok_txt);
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(yes, (dialog, which) -> {
                    if (loginStatus && followStatus != null && !followStatus.equals("1")){
                        followAstrologerRequest();
                    }
                    else if(!loginStatus){
                        Intent intent1 = new Intent(activity, LoginSignUpActivity.class);
                        intent1.putExtra(IS_FROM_SCREEN, ASTROSAGE_MAGAZINE_SCREEN);
                        activity.startActivity(intent1);
                    }
        });
        if(followStatus != null && !followStatus.equals("1")) {
            alertDialog.setNegativeButton(no,null);
        }
        alertDialog.show();
    }*/

}