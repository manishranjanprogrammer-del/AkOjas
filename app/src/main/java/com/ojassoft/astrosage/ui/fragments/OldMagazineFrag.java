package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CUtils.openAstrologerDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

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

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActAstroShop;
import com.ojassoft.astrosage.ui.act.ActAstroShopCategories;
import com.ojassoft.astrosage.ui.act.ActShowOjasSoftArticlesWithTabs;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ojas on १९/३/१८.
 */

public class OldMagazineFrag extends Fragment {
    WebView webView;
    NetworkImageView imgBanner;
    Activity activity;
    ProgressBar progressBar;
    AdData topAdData;
    int SCREEN_TYPE = -1;
    String offersUrl = "";
    String title = "AstroSage.com : All Articles";
    boolean IS_ASTROSHOP = false;
    boolean refresh = false;
    String englisBlogURL = "https://astrology.astrosage.com";
    String hindiBlogURL = "https://jyotish.astrosage.com";
    private Button textShowStatus;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private ArrayList<AdData> adList;
    /*String englisBlogURL = "http://horoscope.astrosage.com";
    String hindiBlogURL = "http://horoscope.astrosage.com/hindi";*/
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
        imgBanner = (NetworkImageView) rootView.findViewById(R.id.imgBanner);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_magazine);
        View homeBottomNav = (View) rootView.findViewById(R.id.homeBottomNav);
        homeBottomNav.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        Log.e("SAN ", " OldMagazine ");
        getData();
        return rootView;
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
                if (refresh) {
                    webView.reload();
                } else {
                    webView.stopLoading();
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
            showToolBarTitle(((BaseInputActivity) activity).regularTypeface,
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
                            if (description != null) {
                                showWarning(description);
                            }
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

                            view.loadUrl(url);
                        }
                    }
                    //END ON 05-03-2015
                    else {
                        //Updated on 05 feb 2016. using native functions from web view buttons clink
                        if (url.contains("http://b.astrosage.com/") || url.contains("https://b.astrosage.com/")) {
                            openInBuildFunction(new HomeInputScreen(), CGlobalVariables.MODULE_BASIC);
                        } else if (url.contains("http://m.astrosage.com/horoscopematching/") ||
                                url.contains("https://m.astrosage.com/horoscopematching/")) {
                            openInBuildFunction(new HomeMatchMakingInputScreen(), CGlobalVariables.MODULE_MATCHING);
                        }
                        //Added by Amit Sharma to remove deeplinking on url.contains("http://www.astrosage.com/horoscope/ urls
                        /* else if (url.contains("http://www.astrosage.com/horoscope/") || url.contains("http://www.astrosage.com/rashifal/") ||
                                url.contains("https://www.astrosage.com/horoscope/") || url.contains("https://www.astrosage.com/rashifal/")) {
                            openInBuildFunction(new HoroscopeHomeActivity(), CGlobalVariables.MODULE_HOROSCOPE);
                        }*/
                        else if (url.equals("http://buy.astrosage.com/") || url.equals("https://buy.astrosage.com/")) {
                            openInBuildFunction(new ActAstroShopCategories(), CGlobalVariables.ASTROSHOP_SERVICES);
                        } else if (url.contains("http://buy.astrosage.com/") || url.contains(CGlobalVariables.astrosage_services_url) ||
                                url.contains("https://buy.astrosage.com/") || url.contains(CGlobalVariables.astrosage_services_urls)) {
                            CUtils.getUrlLink(url, activity, ((BaseInputActivity) activity).LANGUAGE_CODE, 0);
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
                            openAstrologerDetail(activity,articleIdLastPathSegment,false,true,linkData.toString());
                            //CUtils.openWebBrowserOnlyChrome(getActivity(), Uri.parse(url));
                                /*if (getUserLoginStatus(activity)) {
                                    Uri linkData = Uri.parse(url);
                                    String articleIdLastPathSegment = linkData.getLastPathSegment();
                                    if (articleIdLastPathSegment == null) articleIdLastPathSegment = "";
                                    getAstrologerDetails(articleIdLastPathSegment);
                                } else {
                                    showAstrologerFollowDialog(false);
                                }*/
                        } else if (url.contains(CGlobalVariables.VARTA_ASTROSAGE) || url.contains(CGlobalVariables.TALK_ASTROSAGE)){
                            CUtils.openWebBrowserOnlyChrome(getActivity(), Uri.parse(url));
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
                startActivity(intent);
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

    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(activity)) {
            webView.loadUrl(getBlogUrl());
            //webView.loadUrl(ActShowOjasSoftArticlesWithTabs.pathStr);

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
                // extra condition
            } else if (blogUrl.contains("horoscope.astrosage.com")
                    || blogUrl.contains("www.horoscope.astrosage.com")
                    || blogUrl.contains("horoscope.astrosage.com/hindi")
                    || blogUrl.contains("www.horoscope.astrosage.com/hindi")) {
                blogUrl = englisBlogURL;
                if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    if (LibCUtils.isSupportUnicodeHindi()) {
                        blogUrl = hindiBlogURL;
                    }
                }
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

    /*  @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {

          switch (keyCode) {
              case KeyEvent.KEYCODE_BACK:
                  if (webView != null && webView.canGoBack()) {
                      webView.goBack();
                      return true;
                  } else {

                      activity.finish();
                  }
                  break;
          }
          return super.onKeyDown(keyCode, event);
      }*/
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
                //imgBanner.setVisibility(View.GONE);
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
        if (webView == null) return;
        if (refresh) {
            webView.reload();
        } else {
            webView.stopLoading();
        }
    }

}