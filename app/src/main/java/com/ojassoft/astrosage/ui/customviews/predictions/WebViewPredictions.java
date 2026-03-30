package com.ojassoft.astrosage.ui.customviews.predictions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.ojassoft.astrosage.misc.CGenerateAppViews.getAppViewFragment;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_PREDICTION;
import static com.ojassoft.astrosage.utils.CGlobalVariables.REPORT_ERROR_PREF;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_PREDICTION_JADI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_PREDICTION_MANGAL_DOSH;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_PREDICTION_RUDRAKSHA;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_PREDICTION_YANTRA;

public class WebViewPredictions extends WebView {

    /*	public static final int LIFE_PREDICTIONS = 0;
        public static final int MONTHLY_PREDICTIONS = 1;
        public static final int DAILY_PREDICTIONS = 2;
        public static final int MANGAL_DOSH = 3;
        public static final int SADE_SATI = 4;
        public static final int KAAL_SARP_DOSHA = 5;
        public static final int LALKITAB_DEBT = 6;
        public static final int LALKITAB_TEVA_TYPE = 7;
        public static final int LAL_KITAB_REMEDIES = 8;
        public static final int ASCENDANT_PREDICTION = 9;
        public static final int PLANET_CONSIDERATION = 10;
        public static final int GEMSTONE_REPORT = 11;
        public static final int TRANSIT_TODAY = 12;
        public static final int MAHADASHA_PHALA = 13;
        public static final int NAKSHATRA_REPORT = 14;
        public static final int PREDICTION_VARSHPHAL = 15; //ADDED BY HEVENDRA ON 13-01-2015
    */
    /* MODIFIED  BY BIJENDRA FOR PREDICTION CATEGORY ON 07-05-15*/
    //WE HAVE ADDED CATEGORY PREDICTION ,STATED INDEX FOR IT IS 0
    public static final int LIFE_PREDICTIONS = 1;
    public static final int MONTHLY_PREDICTIONS = 2;
    public static final int DAILY_PREDICTIONS = 3;
    public static final int MANGAL_DOSH = 4;
    public static final int SADE_SATI = 5;
    public static final int KAAL_SARP_DOSHA = 6;
    public static final int LALKITAB_DEBT = 7;
    public static final int LALKITAB_TEVA_TYPE = 8;
    public static final int LAL_KITAB_REMEDIES = 9;
    public static final int LALKITAB_DASHA_TYPE = 48;
    public static final int LALKITAB_PLANET_TYPE = 49;
    public static final int LALKITAB_HOUSE_TYPE = 50;

    public static final int ASCENDANT_PREDICTION = 10;
    public static final int PLANET_CONSIDERATION = 11;
    public static final int GEMSTONE_REPORT = 12;
    public static final int TRANSIT_TODAY = 13;
    public static final int MAHADASHA_PHALA = 14;
    public static final int NAKSHATRA_REPORT = 15;
    public static final int PREDICTION_VARSHPHAL = 16;
    //tejinder added here
    public static final int PREDICTION_BABYNAME = 17;
    public static final int PREDICTION_MOONWESTERN = 18;
    public static final int PREDICTION_MOON = 19;
    //temporary added tejinder
    public static final int CALCULATE_SHADBALA = 20;
    public static final int CALCULATE_PRASTHARASHTAKVARGA = 21;
    public static final int CALCULATE_BHAV_MADHYA = 22;
    public static final int CALCULATE_KP_CUSP = 23;
    public static final int CALCULATE_SHODASHVARGA = 24;
    public static final int CALCULATE_FRIENDSHIP = 25;
    public static final int CALCULATE_CHARANTARDSASA = 26;
    public static final int CALCULATE_YOGINIDASHA = 27;
    public static final int CALCULATE_JAMINISYSTEMKARAKAMSASWAMSA = 28;
    public static final int CALCULATE_AVAKAHADA_CHAKRA = 29;
    public static final int CALCULATE_PERSONAL_DETAIL = 30;
    public static final int CALCULATE_GATHAK_FAVOURABLE_POINTS = 31;
    public static final int CALCULATE_RUDRAKSHA = 32;
    public static final int CALCULATE_JADI = 33;
    public static final int CALCULATE_YANTRA = 34;
    public static final int CALCULATE_ISHT_DEVTA = 35;

    public static final int MISC_Panchadhikari = 36;
    public static final int MISC_Yoga_Dosha_summary = 37;
    public static final int MISC_Remedies_Recommendations = 38;
    public static final int MISC_Karak = 39;
    public static final int MISC_Avastha = 40;
    public static final int MISC_Navatara = 41;
    public static final int MISC_Upgraha_Table = 42;
    public static final int MISC_Upgraha_Chart = 43;
    public static final int MISC_Arudha_Chart = 44;
    public static final int MISC_Current_Ruling_Planets = 45;
    public static final int MISC_Shodashvarta_Table_Rashi = 46;
    public static final int MISC_Shodashvarga_Bhava = 47;


    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    String articleIdLastPathSegment = null;
    ArrayList<String> CategoryFullName;
    private ProgressBar progressBar;
    private LinearLayout.LayoutParams params;
    private LinearLayout relativeView;
    private Button textShowStatus;
    //END
    private Context act;
    private String url;
    private PageLoadFinish onPageLoad;

    public WebViewPredictions(Context act, String url) {
        super(act);
        this.act = act;
        this.url = url;
        initProgressBar();
        initWebView();
    }
    public WebViewPredictions(Context act, String url,PageLoadFinish onPageLoad) {
        super(act);
        this.act = act;
        this.url = url;
        this.onPageLoad = onPageLoad;
        initProgressBar();
        initWebView();
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(act, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        textShowStatus = new Button(act, null, android.R.attr.buttonStyle);
        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(act);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        relativeView = new LinearLayout(act);
        this.setLayoutParams(params);
        relativeView.setOrientation(LinearLayout.VERTICAL);
        relativeView.setLayoutParams(params);
        relativeView.setBackgroundColor(getResources().getColor(R.color.white));
        relativeView.setGravity(Gravity.CENTER);
    }

    private void initWebView() {
        this.getSettings().setJavaScriptEnabled(true);
        //this.getSettings().setBuiltInZoomControls(true);
        //this.getSettings().setSupportZoom(true);
        this.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                showProgressBar(true);
                if (progress == 100) {
                    showProgressBar(false);
                }
            }
        });

        this.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(act, "onPageFinished", Toast.LENGTH_LONG).show();
                showProgressBar(false);
                super.onPageFinished(view,url);
                if(onPageLoad != null) onPageLoad.onPageFinished(view,url);
                Log.e("urlLoadCheck ", " WVP onPageFinished() URL => " + url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgressBar(true);
                super.onPageStarted(view, url, favicon);

                Log.e("urlLoadCheck ", " WVP onPageStarted() URL => " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, final String description, String failingUrl) {
                view.loadUrl("about:blank");
                view.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        showWarning(description);
                        CUtils.saveStringData(act, REPORT_ERROR_PREF, description + "\n\n"+url);
                        //  Toast.makeText(AstrosageKundliApplication.getAppContext(), "Server Error: " + description , Toast.LENGTH_LONG).show();
                        //Toast.makeText(act, "description = "+description +"errorCode = "+errorCode, Toast.LENGTH_LONG).show();

                    }
                }, 1000);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(final WebView view, final WebResourceRequest request, final WebResourceResponse errorResponse) {
                final int statusCode;
                view.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //  Toast.makeText(AstrosageKundliApplication.getAppContext(), "Server Error: " + description , Toast.LENGTH_LONG).show();
                        //Toast.makeText(act, "description = "+errorResponse.getResponseHeaders().toString() +"errorCode = "+errorResponse.getStatusCode(), Toast.LENGTH_LONG).show();
                        if (errorResponse.getResponseHeaders() != null) {
                            CUtils.saveStringData(act, REPORT_ERROR_PREF, errorResponse.getResponseHeaders().toString()+ "\n\n"+url);
                        }

                    }
                }, 1000);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.clearCache(true);//ADDED BY BIJENDRA ON 01-05-15
                //Log.e("SAN ", " WVP shouldOverrideUrlLoading() URL => " + url);
                try {
                    // SAN Exp for link click if conditions
                    if (url.contains(CGlobalVariables.JADI_URL) ) {
                        getAppViewFragment().callMethodForRedirection((Activity) act, MODULE_PREDICTION, SUB_MODULE_PREDICTION_JADI);
                    } else if (url.contains(CGlobalVariables.YANTRA_URL) ) {
                        getAppViewFragment().callMethodForRedirection((Activity) act, MODULE_PREDICTION, SUB_MODULE_PREDICTION_YANTRA);
                    } else if (url.contains(CGlobalVariables.RUDRAKSHA_URL) ) {
                        getAppViewFragment().callMethodForRedirection((Activity) act, MODULE_PREDICTION, SUB_MODULE_PREDICTION_RUDRAKSHA);
                    } else if (url.contains(CGlobalVariables.URL_MANGAL_DOSH_PREDICTION) ) {
                        getAppViewFragment().callMethodForRedirection((Activity) act, MODULE_PREDICTION, SUB_MODULE_PREDICTION_MANGAL_DOSH);
                    } else if (url.contains(CGlobalVariables.URL_KALSARPA_YOGA_PREDICTION) ) {
                        getAppViewFragment().callMethodForRedirection((Activity) act, MODULE_PREDICTION, SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA);
                    } else if (url.contains(CGlobalVariables.talk_to_astrologers) || url.contains(CGlobalVariables.chat_with_astrologers) || url.contains(CGlobalVariables.open_ai_astrologers) || url.contains(CGlobalVariables.buy_astrosage_url) || url.contains(CGlobalVariables.buy_astrosage_urls)
                            || url.contains(CGlobalVariables.astrocamp_product_url) || url.contains(CGlobalVariables.astrocamp_service_url) || url.contains(CGlobalVariables.astrosage_services_url) || url.contains(CGlobalVariables.astrosage_services_urls)) {
                        CUtils.getUrlLink(url, (Activity) act, LANGUAGE_CODE, 0);
                    } else {
                        view.loadUrl(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

           /*     if (url.contains("https://buy.astrosage.com/gemstone/")){
                    //Using Deep Linking.
                    *//*Intent intent = new Intent(getContext(), ActAppModule.class);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    getContext().startActivity(intent);*//*

                    //Using Direct Intent

                    articleIdLastPathSegment = Uri.parse(url).getLastPathSegment();
                    Cache cache = VolleySingleton.getInstance(getContext()).getRequestQueue().getCache();
                    Cache.Entry entry = cache.get(CGlobalVariables.astroShopItemsLive);
                    if (entry != null) {
                        try {
                            String saveData = new String(entry.data, "UTF-8");
                            String[] array = parseAstroShopData(saveData, articleIdLastPathSegment);
                            if (array != null) {
                                //initializeGoogleIndexing(array);
                                int pos = Integer.parseInt(array[2]);
                                CUtils.callActAstroShop(pos,(OutputMasterActivity)act,"");
                            } else {
                                if (CategoryFullName != null) {
                                    boolean result = false;
                                    for (String titleName : CategoryFullName) {
                                        result = getItemsInDetail(titleName, saveData);
                                        if (result) {
                                            break;
                                        }
                                    }

                                    if (!result) {
                                        CUtils.callActAstroShop(0,(OutputMasterActivity)act,"");
                                    }

                                } else {
                                    CUtils.callActAstroShop(0,(OutputMasterActivity)act,"");
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            CUtils.callActAstroShop(0,(OutputMasterActivity)act,"");
                        }
                    }else{
                        CUtils.callActAstroShop(0,((OutputMasterActivity)act),url);
                    }
                } else if (url.contains("https://buy.astrosage.com/service/")){
                    //Using Deep Linking.
                    *//*Intent intent = new Intent(getContext(), ActAppModule.class);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    getContext().startActivity(intent);*//*

                    //Using Direct Intent
                    Intent intent = new Intent(getContext(), ActAstroShopCategories.class);
                    getContext().startActivity(intent);
                } else {
                    view.loadUrl(url);
                }*/
                return true;
            }

        });


        loadUrl();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int temp_ScrollY = getScrollY();
            scrollTo(getScrollX(), getScrollY() + 1);
            scrollTo(getScrollX(), temp_ScrollY);

        }
        return super.onTouchEvent(event);
    }

    private void loadUrl() {
        this.clearCache(true);//ADDED BY BIJENDRA ON 01-05-15
        if (CUtils.isConnectedWithInternet(act)) {
            this.loadUrl(url);
            //Log.e("SAN ", " WVP loadUrl() URL => " + url);
            //Toast.makeText(act, "url = https://www.google.com/", Toast.LENGTH_LONG).show();

        } else {
            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
        }
    }

    private void showWarning(String message) {
        textShowStatus.setText(message);
        Drawable icon = getContext().getResources().getDrawable(R.drawable.ic_av_replay);
        textShowStatus.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        textShowStatus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
        relativeView.removeAllViews();
        relativeView.addView(textShowStatus);
        this.removeAllViews();
        this.addView(relativeView, params);
    }

    private void showProgressBar(boolean show) {
        if (show) {
            relativeView.removeAllViews();
            relativeView.addView(progressBar);
            this.removeAllViews();
            this.addView(relativeView, params);

        } else {
            this.removeView(relativeView);
        }
    }

    private String[] parseAstroShopData(String data, String lastSegment) {
        String[] array = null;
        CategoryFullName = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(data);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            JSONArray jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");

            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {

                String catUrl = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryUrl").trim().toLowerCase();
                CategoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));

                if (catUrl.equalsIgnoreCase(lastSegment)) {
                    array = new String[3];
                    array[0] = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName");
                    array[1] = jsonArrayProductCategoryName.getJSONObject(i).getString("CategorySmallDescription");
                    array[2] = i + "";
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return array;
    }

    private boolean getItemsInDetail(String title, String mainData) {
        boolean resultFound = false;
        try {
            List<AstroShopItemDetails> alldta = new ArrayList<>();
            int pos = 0;
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject innerObject;
            JSONArray arrayOfItems;
            for (int i = 1; i < jsonArray.length(); i++) {
                pos = i - 1;
                innerObject = jsonArray.getJSONObject(i);
                Iterator<String> iter = innerObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (key.equals(title)) {
                        arrayOfItems = innerObject.getJSONArray(title);
                        if (arrayOfItems != null) {
                            alldta.addAll((ArrayList<AstroShopItemDetails>) new Gson().fromJson(arrayOfItems.toString(), new TypeToken<ArrayList<AstroShopItemDetails>>() {
                            }.getType()));
                        }
                    }
                }
            }

            if (alldta.size() > 0) {
                resultFound = checkForListInnerData(alldta, articleIdLastPathSegment, pos);
            } else {
                CUtils.callActAstroShop(0, (OutputMasterActivity) act, "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultFound;
    }

    private boolean checkForListInnerData(List<AstroShopItemDetails> alldta, String articleIdLastPathSegment, int postion) {

        boolean resultFound = false;

        for (int i = 0; i < alldta.size(); i++) {
            if (alldta.get(i).getP_url_text().trim().equalsIgnoreCase(articleIdLastPathSegment)) {
                AstroShopItemDetails astroShopItemDetails = alldta.get(i);
                String[] array = new String[]{astroShopItemDetails.getPName(), astroShopItemDetails.getPSmallDesc()};
                // initializeGoogleIndexing(array);
                resultFound = true;
                CUtils.goToFoundedItemScreen(postion, astroShopItemDetails, (OutputMasterActivity) act, alldta);
            }
        }

        return resultFound;
        /*if(!resultFound){
            CUtils.callActAstroShop(0,ActAppModule.this);
        }*/

    }

    @FunctionalInterface
    public interface PageLoadFinish{
        void onPageFinished(View view, String url);
    }
}
