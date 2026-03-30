package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
//import com.google.analytics.tracking.android.EasyTracker;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.CustomPagerAdapter;
import com.ojassoft.astrosage.customadapters.VideoGridAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.LearnVideoAdapter;
import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.ExpandedGridView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ojas on २१/७/१६.
 */
public class ActLearnAstrology extends BaseInputActivity {

    private Toolbar tool_barAppModule;
    private RequestQueue queue;
    private CustomProgressDialog pd = null;
    //private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    //public Typeface mediumTypeface;
    //private Typeface typeface;
    private ViewPager mViewPager;
    private TextView tvTitle, tvVidTitle, tvResTitle;
    private CustomPagerAdapter mPagerAdapter;

    private RecyclerView gridView;
    //private ImageView img1,img2,img3,img4,img5,img6;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public ActLearnAstrology() {
        super(R.string.app_name);
    }

    private Bundle bundle;
    private VideoGridAdapter adapter;
    private NestedScrollView scroll;
    private Button btnLoad;
    //CirclePageIndicator indicator;
    //private LinearLayout llResume;
    private List<SliderModal> data;

    private List<SliderModal> sublist;
    ArrayList<SliderModal> videoList;
    private RecyclerView my_recycler_view;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private TabLayout tab_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_lear_astrology);
       /* Intent startService = new Intent(ActLearnAstrology.this, ServiceLearnAstrology.class);
        startService(startService);*/
        init();
    }


    private void init() {

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        // Get the navigation icon drawable
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
        if (navIcon != null) {
            // Tint the drawable with the desired color
            navIcon.setTint(ContextCompat.getColor(this, R.color.black));

            // Set the tinted drawable as the navigation icon
            tool_barAppModule.setNavigationIcon(navIcon);
        }
        setSupportActionBar(tool_barAppModule);
        ((TabLayout) findViewById(R.id.tabs)).setVisibility(View.GONE);
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        scroll =  findViewById(R.id.scroll);
        tvVidTitle = (TextView) findViewById(R.id.tvVidTitle);
        tvResTitle = (TextView) findViewById(R.id.tvResTitle);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        gridView =  findViewById(R.id.gridView);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);


        mViewPager.setOffscreenPageLimit(3);
        tvTitle.setText(getResources().getString(R.string.l_astro));
        tvTitle.setTypeface(mediumTypeface);
        tvVidTitle.setTypeface(regularTypeface);
        tvResTitle.setTypeface(regularTypeface);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showView(false);

        scroll.fullScroll(ScrollView.FOCUS_UP);
        gridView.setFocusable(false);
        gridView.setLayoutManager(new GridLayoutManager(this,2));
        String url = CGlobalVariables.LEARN_ASTROLOGY + "lang=" + LANGUAGE_CODE;
        checkCachedData(url);


        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreData();
            }
        });


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                    mViewPager.setCurrentItem(currentPage, true);
                } else {
                    mViewPager.setCurrentItem(currentPage++, true);

                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 8000, 5000);


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


    /*Parse Recieved Gson Data*/
    private void parseData(String response) {
        try {
            data = new Gson().fromJson(response, new TypeToken<ArrayList<SliderModal>>() {
            }.getType());
            showView(true);
            setSliderdata();
            setGridData();
            scroll.fullScroll(ScrollView.FOCUS_UP);
            gridView.setFocusable(false);
        } catch (Exception ex) {
            //
        }
    }


    private void setSliderdata() {
        ArrayList<SliderModal> sliderList = new ArrayList<>();
        for (SliderModal modal : data) {
            if (modal.getIsfeatured().equalsIgnoreCase("1"))
                sliderList.add(modal);
        }

        mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), (sliderList));
        mViewPager.setAdapter(mPagerAdapter);
        NUM_PAGES = sliderList.size();
        mViewPager.setCurrentItem(0);
       /* indicator.setViewPager(mViewPager);
        indicator.setRadius(10);
        indicator.setStrokeWidth(3);*/

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                try {
	                    /*for (int i = 0; i < dotsCount; i++) {
	                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
	                    }

	                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
	*/
                    mPagerAdapter.notifyDataSetChanged();
                    currentPage = position;
                } catch (Exception ex) {
                    //Log.e("Exception",ex.getMessage());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab_layout.setupWithViewPager(mViewPager);

        // setUiPageViewController();

    }

    private void setGridData() {
        sublist = new ArrayList<>();
        if (data.size() > 10) {
            for (int i = 0; i < 10; i++) {
                sublist.add(data.get(i));

            }

        } else {
            sublist = new ArrayList<>(data);
            btnLoad.setVisibility(View.GONE);
        }
        adapter = new VideoGridAdapter(this, sublist);
        gridView.setAdapter(adapter);
        scroll.fullScroll(ScrollView.FOCUS_UP);
        gridView.setFocusable(false);
    }

    /*Handle pagination from local cache*/
    private void loadMoreData() {
        int mainSize = data.size();
        int childSize = sublist.size();
        if (mainSize > childSize) {
            if ((mainSize - childSize) >= 6) {
                for (int i = childSize; i < childSize + 6; i++) {
                    sublist.add(data.get(i));
                }
                adapter.notifyItemRangeInserted(childSize,childSize + 6);
                if (data.size() == sublist.size()) {
                    btnLoad.setVisibility(View.GONE);
                }
            } else {
                for (int i = childSize; i < data.size(); i++) {
                    SliderModal mod1 = data.get(i);
                    sublist.add(data.get(i));
                }
                //   sublist.addAll(data.subList(childSize,data.size()));
                adapter.notifyItemRangeInserted(childSize,mainSize);
                btnLoad.setVisibility(View.GONE);
            }
        }
    }

    private void showView(Boolean showview) {
        if (showview) {
            btnLoad.setVisibility(View.VISIBLE);
            tvVidTitle.setVisibility(View.VISIBLE);
        } else {
            btnLoad.setVisibility(View.GONE);
            tvVidTitle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSaveVideos();

        /*CUtils.showAdvertisement(ActLearnAstrology.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*CUtils.removeAdvertisement(ActLearnAstrology.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    private void getSaveVideos() {
        String videoFromPrefs = CUtils.getSavedVideos(this, String.valueOf(LANGUAGE_CODE));
        videoList = (ArrayList<SliderModal>) new Gson().fromJson(videoFromPrefs,
                new TypeToken<ArrayList<SliderModal>>() {
                }.getType());
        if (videoList != null && videoList.size() > 0) {
            //llResume.setVisibility(View.VISIBLE);
            tvResTitle.setVisibility(View.VISIBLE);
            my_recycler_view.setVisibility(View.VISIBLE);
            Collections.reverse(videoList);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mAdapter = new LearnVideoAdapter(this, videoList);
            my_recycler_view.setAdapter(mAdapter);
            my_recycler_view.setLayoutManager(layoutManager);

        } else {
            //llResume.setVisibility(View.GONE);
            tvResTitle.setVisibility(View.GONE);
            my_recycler_view.setVisibility(View.GONE);
        }

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

    /**
     * Check and load cache data
     */
    private void checkCachedData(String url) {
        boolean isShowProgressbar = true;
        // Cached response doesn't exists. Make network call here
        if (!CUtils.isConnectedWithInternet(ActLearnAstrology.this)) {
            MyCustomToast mct = new MyCustomToast(ActLearnAstrology.this, ActLearnAstrology.this
                    .getLayoutInflater(), ActLearnAstrology.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            getLearnAstrologyData(url, isShowProgressbar);
        }
    }


    /**
     * get Learn astrology data
     */
    public void getLearnAstrologyData(String url, final boolean isShowProgressbar) {

        if (isShowProgressbar) {
            showProgressBar();
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            try {
                                if (response.trim().contains("[{\"Result\":\"2\"}]")) {
                                    showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                                    //ActLearnAstrology.this.finish();
                                } else if (!TextUtils.isEmpty(response)) {
                                    //CUtils.saveStringData(ActLearnAstrology.this, "LEARN" + String.valueOf(LANGUAGE_CODE), result);
                                    if (isShowProgressbar) {
                                        response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                        parseData(response);
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        hideProgressBar();
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
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
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActLearnAstrology.this));
                //headers.put("key", "5465477");
                headers.put("languageCode", String.valueOf(LANGUAGE_CODE));

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
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActLearnAstrology.this, regularTypeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
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

    private void showCustomMsg(String msg) {

        MyCustomToast mct = new MyCustomToast(this,
                this.getLayoutInflater(), this,
                ((ActLearnAstrology) this).regularTypeface);
        mct.show(msg);
    }
}
