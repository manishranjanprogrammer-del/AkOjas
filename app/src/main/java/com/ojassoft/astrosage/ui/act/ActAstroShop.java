package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.APP_THEME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PACKAGE_NAME;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.AstroShopMaindata;
import com.ojassoft.astrosage.customadapters.ProductSearchAdapter;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.misc.AstroShopDataDownloadService;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.FragGridView;
import com.ojassoft.astrosage.ui.fragments.FragListView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MaterialSearchView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActAstroShop extends BaseInputActivity implements OnClickListener {
    public static Activity activity;
    private static ArrayList<String> CategoryFullName;
    final Integer[] astrpshop_output_menu_item_list_index = {152};
    public int SELECTED_MODULE;
    public ViewPagerAdapter adapter;
    public ArrayList<AstroShopMaindata> data = new ArrayList();
    public ArrayList<CustomAddModel> sliderList;
    String mainData;
    FragListView fragListView;
    FragGridView fragGridView;
    ArrayList<String> categoryFullName;
    ArrayList<String> categoryShortName;
    CustomProgressDialog pd = null;
    Typeface typeface;
    String redirectUrl = "";
    private Toolbar tool_barAppModule;
    private ViewPager mViewPager;
    private ImageView imgicmore, imgicviewlist, imgicviewmodule, imgshopingcart;
    private RelativeLayout rlcart;
    private TextView txcartCount;
    private TabLayout tabLayout;
    private String[] titles = new String[7];
    private String[] tvtitle = new String[6];
    private TextView tvTitle;
    private int grid = R.id.imgicviewmodule;
    private int list = R.id.imgicviewlist;
    private boolean expanded = false;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private float offset;
    private Interpolator interp;
    private int duration;
    private MaterialSearchView searchView;
    private RequestQueue queue;
    public ArrayList<AstroShopItemDetails> allData = new ArrayList<>();
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private BroadcastReceiver receiver;
    private String referdata;
    private int currentTabPosition = 0;
    private List<CustomAddModel> customaddmodel;
    private String IsShowBanner = "False";
    String tabTitleValue="";
    int poss=0;
    public String customAdsStr = "";


    public ActAstroShop() {
        super(R.string.app_name);
    }

    private static String[] parseAstroShopDataForRedirectToAstroShopItemDescription(String data, String lastSegment) {
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        if (CUtils.isConnectedWithInternet(ActAstroShop.this)) {
            if (CUtils.getUserPurchasedPlanFromPreference(ActAstroShop.this) == CGlobalVariables.BASIC_PLAN_ID) {
                String url = CGlobalVariables.CUSTOM_ADDS + "?languagecode=" + LANGUAGE_CODE + "&versioncode=" + BuildConfig.VERSION_CODE+"&adscreenname=2";
                //get CustomAds Data
                checkCachedAdsData(url);
            }
        }
        fragListView = new FragListView();
        fragGridView = new FragGridView();

        setContentView(R.layout.lay_astroshop_main);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        tvTitle.setTypeface(regularTypeface);
        tvTitle.setWidth(dpToPx(120));


        imgicmore = (ImageView) findViewById(R.id.imgMoreItem);
        imgicviewlist = (ImageView) findViewById(R.id.imgicviewlist);
        imgicviewmodule = (ImageView) findViewById(R.id.imgicviewmodule);
        imgshopingcart = (ImageView) findViewById(R.id.imgshopingcart);
        rlcart = (RelativeLayout) findViewById(R.id.rlcart);
        txcartCount = (TextView) findViewById(R.id.txtcartCount);
        rlcart.setVisibility(View.VISIBLE);
        imgicmore.setVisibility(View.GONE);
        txcartCount.setVisibility(View.GONE);
        imgshopingcart.setVisibility(View.VISIBLE);
        imgicviewmodule.setVisibility(View.VISIBLE);
        imgicviewmodule.setOnClickListener(this);
        txcartCount.setOnClickListener(this);
        imgicviewlist.setOnClickListener(this);
        imgshopingcart.setOnClickListener(this);
        imgicmore.setOnClickListener(this);
        rlcart.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        currentTabPosition = getIntent()
                .getIntExtra(CGlobalVariables.MODULE_TYPE_KEY,
                        CGlobalVariables.MODULE_BASIC);
        if (CGlobalVariables.isGrid) {
            imgicviewmodule.setVisibility(View.GONE);
            imgicviewlist.setVisibility(View.VISIBLE);
        } else {
            imgicviewmodule.setVisibility(View.VISIBLE);
            imgicviewlist.setVisibility(View.GONE);

        }

        try {
            redirectUrl = getIntent().getStringExtra(CGlobalVariables.RedirectUrlFromAstroShopKey);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage().toString());
        }


        //  getActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            searchView = (MaterialSearchView) findViewById(R.id.search_view);
            searchView.setVoiceSearch(true);
            searchView.showVoice(true);
            //searchView.setCursorDrawable(R.drawable.custom_cursor);
            searchView.setEllipsize(true);
            searchView.setHintTextColor(Color.parseColor("#ffc107"));
        } catch (Exception e) {
            //Log.e("Exceptioninflate", "ActAstroShop" + e.getMessage());
        }

        if (!CUtils.isConnectedWithInternet(ActAstroShop.this)) {
            MyCustomToast mct = new MyCustomToast(ActAstroShop.this, ActAstroShop.this
                    .getLayoutInflater(), ActAstroShop.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            checkCachedData();
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                referdata = intent.getStringExtra(AstroShopDataDownloadService.COPA_MESSAGE);

                // //Log.e("tag","DTAGEYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" + referdata.size());
                if (referdata.equals("1")) {
                    String data = CUtils.getStringData(ActAstroShop.this, CGlobalVariables.Astroshop_Data + String.valueOf(LANGUAGE_CODE), "");

                    /*Cache cache = VolleySingleton.getInstance(ActAstroShop.this).getRequestQueue().getCache();
                    Cache.Entry entry = cache.get(CGlobalVariables.astroShopItemsLive);*/
                    if (data != null && !data.isEmpty()) {
                        try {
                            currentTabPosition = mViewPager.getCurrentItem();
                            // String saveData = new String(entry.data, "UTF-8");
                            mainData = data;
                            allData.clear();
                            parseAstroShopData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
                        .show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                ArrayList<AstroShopItemDetails> fetchedData = getitems(newText);
                ProductSearchAdapter sAdapter = new ProductSearchAdapter(ActAstroShop.this, fetchedData);
                searchView.setAdapter(sAdapter);
                return false;
            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                searchView.setTintVisibility(true);
                ProductSearchAdapter sAdapter = new ProductSearchAdapter(ActAstroShop.this, allData);
                searchView.setAdapter(sAdapter);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                searchView.setTintVisibility(false);

            }
        });
        searchView.closeSearch();

        setCartItems();
        setMoreOption();
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void setMoreOption() {
        setVisibilityOfMoreIconImage(imgicmore, getResources().getStringArray(
                R.array.astroshop_output_menu_item_list), getResources().obtainTypedArray(
                R.array.astroshop_output_menu_item_list_icon), astrpshop_output_menu_item_list_index);
    }

    @Override
    public void setVisibilityOfMoreIconImage(View view, final String[] subMenuItems, final TypedArray subMenuItemsIcon, final Integer[] menuIndex) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingForpopUpMenu(view, subMenuItems, subMenuItemsIcon, menuIndex);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_more:
                //   Toast.makeText(ActAstroShop.this, "Show Here", Toast.LENGTH_SHORT).show();
                openOrderHistory();
                break;
        }

        // return super.onOptionsItemSelected(item);
        return true;
    }


    private void openOrderHistory() {
        Intent actAstroShopHistoryIntent = new Intent(this, ActAstroShopHistory.class);
        startActivity(actAstroShopHistoryIntent);
    }

    private void setPageTitles(String title) {

        tvTitle.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartItems();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgicviewmodule:
                allData.clear();
                CGlobalVariables.isGrid = true;
                currentTabPosition = mViewPager.getCurrentItem();
                setupFragmentInViewPager();
                imgicviewmodule.setVisibility(View.GONE);
                imgicviewlist.setVisibility(View.VISIBLE);

                break;
            case R.id.imgicviewlist:
                allData.clear();
                CGlobalVariables.isGrid = false;
                currentTabPosition = mViewPager.getCurrentItem();
                setupFragmentInViewPager();
                imgicviewmodule.setVisibility(View.VISIBLE);
                imgicviewlist.setVisibility(View.GONE);
                break;
            case R.id.imgMoreItem:
                break;
            case R.id.rlcart:
            case R.id.imgshopingcart:
            case R.id.txtcartCount:
                if (CUtils.getCartProducts(this).size() > 0) {
                    Intent addtocart = new Intent(ActAstroShop.this, AstroShopShopingCartAct.class);
                    startActivity(addtocart);

                }
                break;

            default:
                break;
        }
    }

    private void loadAstroShopData() {
        pd = new CustomProgressDialog(ActAstroShop.this, regularTypeface);
        pd.show();
        pd.setCancelable(false);
        //Log.e("tag", "Key is" + CUtils.getApplicationSignatureHashCode(AstrosageKundliApplication.getAppContext()));
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopItemsLive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("Simple+", response.toString());
                        if (response != null && !response.isEmpty()) {
                            try {

                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                Log.e("wiConvert+", str);

                               /* byte ptext[] = response.getBytes();
                                String value = new String(ptext, "UTF-8");
                                response=value;*/
                                response = str;
                                //Log.e("Convert+", response.toString());

                                CUtils.saveStringData(ActAstroShop.this, CGlobalVariables.Astroshop_Data + String.valueOf(LANGUAGE_CODE), response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject obj = jsonArray.getJSONObject(0);
                                Gson gson = new Gson();
                                JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                                //Log.e("tag", "Element" + element.toString());
                                String result = "";
                                if (obj.has("result")) {
                                    result = obj.getString("result");

                                }
                                if (result.isEmpty() && !result.equalsIgnoreCase("0") && !result.equalsIgnoreCase("2")) {
                                    //  mainData= Charset.forName("UTF-8").encode(result).toString();
                                    mainData = response;
                                    parseAstroShopData();

                                } else {
                                    MyCustomToast mct = new MyCustomToast(ActAstroShop.this, ActAstroShop.this.getLayoutInflater(), ActAstroShop.this, regularTypeface);
                                    mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                                    CUtils.saveStringData(ActAstroShop.this, CGlobalVariables.Astroshop_Data + String.valueOf(LANGUAGE_CODE), "");


                                }
                            } catch (Exception e) {
                                CUtils.saveStringData(ActAstroShop.this, CGlobalVariables.Astroshop_Data + String.valueOf(LANGUAGE_CODE), "");
                                MyCustomToast mct = new MyCustomToast(ActAstroShop.this, ActAstroShop.this.getLayoutInflater(), ActAstroShop.this, regularTypeface);
                                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                                finish();
                            }

                        }
                        hideProgressBar();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("tag", "Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActAstroShop.this, ActAstroShop.this.getLayoutInflater(), ActAstroShop.this, regularTypeface);
                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                hideProgressBar();
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstroShop.this));
                headers.put("langcode", "" + LANGUAGE_CODE);

                headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActAstroShop.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActAstroShop.this)));
                headers.put(PACKAGE_NAME, BuildConfig.APPLICATION_ID);
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);

                // headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                //headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);

                //Log.e("Data", headers.toString());

                //   headers.put("Key","9865");
                return headers;
            }

        };

// Add the request to the RequestQueue.
        //Log.e("tag", "API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void checkCachedData() {
        String data = CUtils.getStringData(this, CGlobalVariables.Astroshop_Data + String.valueOf(LANGUAGE_CODE), "");
        if (data != null && !data.isEmpty()) {
            try {
                //  isCached = true;
                //String saveData = new String(entry.data, "UTF-8");
                //Log.e("tag", "Volley Cached Data" + data.toString());
                Intent i = new Intent(ActAstroShop.this, AstroShopDataDownloadService.class);
                startService(i);
                mainData = data;
                parseAstroShopData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            //Log.e("tag", "Volley Not Cached Data");
            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                loadAstroShopData();
                if (mainData != null && !mainData.equals(""))
                    parseAstroShopData();
            }

        }
    }

    JSONArray jsonArrayProductCategoryName = new JSONArray();

    private void parseAstroShopData() {
        categoryFullName = new ArrayList<>();
        categoryShortName = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");
            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {
                categoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));
                categoryShortName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryShortName"));
            }
            setupFragmentInViewPager();
            getItemsInDetail(categoryFullName.get(mViewPager.getCurrentItem()));
          //  Log.e("TestData", "parseAstroShopData: " + allData);

            updateCartList();

            if (redirectUrl != null && !redirectUrl.equals("")) {
                checkForRedirectToAstroShopItemDescription();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray getItemsInDetail(String title) {
        try {
            JSONArray jsonArray = new JSONArray(mainData);
        //    Log.e("TestData", "getItemsInDetail:MainData--- "+mainData );
            JSONObject innerObject;
            JSONArray arrayOfItems;
            for (int i = 1; i <= jsonArray.length(); i++) {
                innerObject = jsonArray.getJSONObject(i);
                Iterator<String> iter = innerObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (key.equals(title)) {
                        arrayOfItems = innerObject.getJSONArray(title);
                        if (arrayOfItems != null) {
                            allData.addAll(new Gson().fromJson(arrayOfItems.toString(), new TypeToken<ArrayList<AstroShopItemDetails>>() {
                            }.getType()));
                            //Log.e("All length", "" + allData.size());
                            return arrayOfItems;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
           // Log.e("TestData", "getItemsInDetail:error "+e.getMessage() );
        }
        return null;
    }

    private void setupFragmentInViewPager() {
        if (categoryFullName != null && !categoryFullName.isEmpty()) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActAstroShop.this);
            if (!CGlobalVariables.isGrid) {
                for (int i = 0; i < categoryFullName.size(); i++) {
                    adapter.addFragment(new FragListView().newInstance(jsonArrayProductCategoryName, i), categoryShortName.get(i));
                }
            } else {
                for (int i = 0; i < categoryFullName.size(); i++) {
                    adapter.addFragment(new FragGridView().newInstance(jsonArrayProductCategoryName, i), categoryShortName.get(i));
                }
            }
        }

        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setPageTitles(categoryShortName.get(position));
            }

            @Override
            public void onPageSelected(int position) {
                setCurrentView(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(mViewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        //mViewPager.setCurrentItem(currentTabPosition);
        setCurrentView(currentTabPosition, false);
    }

    private void setCurrentView(int index, boolean smoothScroll) {
        mViewPager.setCurrentItem(index, smoothScroll);
        if (tabLayout != null && adapter != null) {
            adapter.setAlpha(index, tabLayout);
        }

        if(index != poss)
        {
            poss = index;
            CUtils.fcmAnalyticsEvents(categoryShortName.get(index),
                    FirebaseAnalytics.Event.VIEW_ITEM_LIST, "");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(AstroShopDataDownloadService.COPA_RESULT)
        );
    }

    @Override
    protected void onStop() {
        // EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    private ArrayList<AstroShopItemDetails> getitems(String itemName) {
        ArrayList<AstroShopItemDetails> item = new ArrayList<>();
     //   Log.e("TestData", "getitems:allData "+allData );
        for (AstroShopItemDetails data : allData) {
            if (data.getPName().toLowerCase().contains(itemName.toLowerCase())) {
                item.add(data);
            }
        }
        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem item = menu.findItem(R.id.action_search);
            MenuItem shareItem = menu.findItem(R.id.action_share);
            shareItem.setVisible(false);
            searchView.setVisibility(View.VISIBLE);
            searchView.setMenuItem(item);


        } else {
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateCartList() {
        ArrayList<AstroShopItemDetails> cartList = CUtils.getCartProducts(this);
        ArrayList<AstroShopItemDetails> newcartList = new ArrayList<AstroShopItemDetails>();

        if (cartList.size() > 0 && allData.size() > 0) {
            for (AstroShopItemDetails item : allData) {
                for (AstroShopItemDetails cartItem : cartList) {

                    if (item.getPId().equalsIgnoreCase(cartItem.getPId())) {
                        newcartList.add(item);
                    }
                }
            }
            CUtils.setCartProduct(this, new Gson().toJson(newcartList));
        }

    }

    private void setCartItems() {
        ArrayList<AstroShopItemDetails> list = CUtils.getCartProducts(this);
        if (list.size() > 0) {
            txcartCount.setVisibility(View.VISIBLE);
            txcartCount.setText(String.valueOf(list.size()));
        } else {
            txcartCount.setVisibility(View.GONE);

        }
    }

    private void checkForRedirectToAstroShopItemDescription() {
        try {

            String articleIdLastPathSegment = Uri.parse(redirectUrl).getLastPathSegment();

            //  String cacheData =CUtils.getStringData(this,CGlobalVariables.Astroshop_Service_Data+String.valueOf(LANGUAGE_CODE),"");

            if (mainData != null && !mainData.isEmpty()) {
                try {
                    // String saveData = new String(entry.data, "UTF-8");
                    String saveData = mainData;
                    String[] array = parseAstroShopDataForRedirectToAstroShopItemDescription(saveData, articleIdLastPathSegment);
                    if (array != null) {
                        int pos = Integer.parseInt(array[2]);
                        // mViewPager.setCurrentItem(pos);
                        setCurrentView(pos, false);
                    } else {
                        if (CategoryFullName != null) {
                            boolean result = false;
                            for (String titleName : CategoryFullName) {
                                result = getItemsInDetailForRedirectToAstroShopItemDescription(titleName, saveData, articleIdLastPathSegment);
                                if (result) {
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    //Log.i("TAG", ex.getMessage().toString());
                }
            }
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage().toString());
        }
    }

    private boolean getItemsInDetailForRedirectToAstroShopItemDescription(String title, String mainData, String articleIdLastPathSegment) {
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
                shiftToMAinScreen(mainData);
                resultFound = CUtils.checkForListInnerData(alldta, articleIdLastPathSegment, pos, ActAstroShop.this);
                redirectUrl = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultFound;
    }

    private void shiftToMAinScreen(String data) {

        try {

            JSONArray jsonArray = new JSONArray(data);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            JSONArray jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");

            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {

                String catUrl = jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryUrl").trim().toLowerCase();

                if (redirectUrl.contains(catUrl)) {
                    // mViewPager.setCurrentItem(i);
                    setCurrentView(i, false);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String getVisibility() {

        return IsShowBanner;
    }

    /*Parse Recieved Gson Data*/
    public ArrayList<CustomAddModel> parseData(String response) {
        try {
            sliderList = new ArrayList<>();
            JSONArray jsonarr = new JSONArray(response);
            JSONObject obj = jsonarr.getJSONObject(0);
            IsShowBanner = obj.getString("IsShowBanner");
            String ImageData = obj.getString("ImageObj");
            //   ArrayList<CustomAddModel> sliderList = new ArrayList<CustomAddModel>();
            customaddmodel = new Gson().fromJson(ImageData, new TypeToken<ArrayList<CustomAddModel>>() {
            }.getType());
            //setSliderdata();
            for (CustomAddModel modal : customaddmodel) {
                //  if(modal.getIsfeatured().equalsIgnoreCase("1"))
                sliderList.add(modal);
            }
        } catch (Exception e) {
            //Log.i("Exception generate", e.toString());
        }
        return sliderList;
    }

    private void setSliderdata(ArrayList<CustomAddModel> sliderList) {
        try {
            int position = mViewPager.getCurrentItem();
            if ((ViewPagerAdapter) mViewPager.getAdapter() != null) {
                Fragment fragment = ((ViewPagerAdapter) mViewPager.getAdapter()).getFragment(position);
                if (CGlobalVariables.isGrid == false) {
                    ((FragListView) (fragment)).setCustomAdd(sliderList);
                } else if (CGlobalVariables.isGrid == true) {
                    ((FragGridView) (fragment)).setCustomAdd(sliderList);

                }
            }
        } catch (Exception ex) {
            //
        }
    }

   /* private List<NameValuePair> getNameValuePairs_CustomAdds(String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("adscreenname", "2"));
        nameValuePairs.add(new BasicNameValuePair("key", CUtils.getApplicationSignatureHashCode(this)));
        nameValuePairs.add(new BasicNameValuePair("languagecode", String.valueOf(LANGUAGE_CODE)));
        nameValuePairs.add(new BasicNameValuePair("versioncode", String.valueOf(BuildConfig.VERSION_CODE)));

        return nameValuePairs;

    }*/

    /*private class GetData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //    pd = new CustomProgressDialog(ActAstroShop.this, typeface);
            //  pd.show();
            //   pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = "";
            InputStream is = null;

            try {


                HttpClient hc = CUtils.getNewHttpClient();
                HttpPost postMethod = new HttpPost(CGlobalVariables.CUSTOM_ADDS);

                postMethod.setEntity(new UrlEncodedFormEntity(getNameValuePairs_CustomAdds(
                        CUtils.getApplicationSignatureHashCode(ActAstroShop.this)), HTTP.UTF_8));
                HttpResponse httpResponse = null;

                httpResponse = hc.execute(postMethod);

                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                System.out.println("Responec=====for Custom Add" + result);

                return result;


            } catch (Exception ex) {
                CUtils.saveStringData(ActAstroShop.this, "CUSTOMADDSFORPRODUCT", "");

                //android.util.//Log.i("Tag", "1 - " + ex.getMessage());
            } finally {
                if (httpConn != null) {
                    result = "";
                    httpConn.disconnect();

                }
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //   pd.dismiss();
           *//* if(result.trim().contains("[{\"Result\":\"2\"}]")){
                showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                ActAstroShop.this.finish();
            }
            else*//*
            if (result != null && !result.isEmpty()) {

                CUtils.saveStringData(ActAstroShop.this, "CUSTOMADDSFORPRODUCT", result);
                ArrayList<CustomAddModel> sliderList = parseData(result);
                if (sliderList != null && sliderList.size() > 0) {
                    setSliderdata(sliderList);
                }
            }
        }

    }*/


    /**
     * Check and load cache data
     */
    private void checkCachedAdsData(String url) {
        //boolean isShowProgressbar = true;
        Cache cache = VolleySingleton.getInstance(ActAstroShop.this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        // cache data
        try {
            if (entry != null) {
                String saveData = new String(entry.data, "UTF-8");
                if (!TextUtils.isEmpty(saveData)) {
                    /*parseData(saveData);*/
                    customAdsStr = saveData;
                    //isShowProgressbar = false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Cached response doesn't exists. Make network call here
       /* if (!CUtils.isConnectedWithInternet(ActAstroShop.this)) {
            MyCustomToast mct = new MyCustomToast(ActAstroShop.this, ActAstroShop.this
                    .getLayoutInflater(), ActAstroShop.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {*/
        getCustomAds(url);
        // }
    }


    /**
     * get custom Ads
     *
     * @param url
     */
    public void getCustomAds(String url) {

      /*  if (isShowProgressbar) {
            showProgressBar();
        }*/


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            try {
                                customAdsStr = response;
                                //CUtils.saveStringData(ActAstroShop.this, "CUSTOMADDSFORPRODUCT", result);
                                ArrayList<CustomAddModel> sliderList = parseData(response);
                                if (sliderList != null && sliderList.size() > 0) {
                                    setSliderdata(sliderList);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //hideProgressBar();
                        }
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // hideProgressBar();
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActAstroShop.this));
                headers.put("adscreenname", "2");
                headers.put("languagecode", String.valueOf(LANGUAGE_CODE));
                headers.put("versioncode", String.valueOf(BuildConfig.VERSION_CODE));
                headers.put(APP_THEME, CUtils.getAppThemeMode(ActAstroShop.this));
                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if(activity == null) return;
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
        }
    }
}
