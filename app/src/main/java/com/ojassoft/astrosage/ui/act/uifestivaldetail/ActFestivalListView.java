package com.ojassoft.astrosage.ui.act.uifestivaldetail;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.PurnimaFastAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AllPanchangData;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.act.ActYearlyVrat;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas-02 on 22/3/18.
 */

public class ActFestivalListView extends BaseInputActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    String festivalUrl;
    ArrayList<DetailApiModel> allDetailData;
    TabLayout tabLayout;
    Typeface typeface;
    private CustomProgressDialog pd = null;
    private RequestQueue queue;
    private PurnimaFastAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeView;

    public ActFestivalListView() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_festival_list_view);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setToolbarItem();

        festivalUrl = getIntent().getStringExtra("festurl");
        allDetailData = (ArrayList<DetailApiModel>) getIntent().getSerializableExtra("detailapi");
        initSwipeRefreshLayout();
        recyclerView = (RecyclerView) findViewById(R.id.my_festival_list_recycler_view);
        addRecyclerView();
        if (!CUtils.isConnectedWithInternet(ActFestivalListView.this)) {
            MyCustomToast mct = new MyCustomToast(ActFestivalListView.this, ActFestivalListView.this
                    .getLayoutInflater(), ActFestivalListView.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            checkCachedData(festivalUrl+"?cityid="+allDetailData.get(0).getCityId()+"&year="+allDetailData.get(0).getYear()+"&lang="+allDetailData.get(0).getLangCode());

        }
    }

    private void initSwipeRefreshLayout() {
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view_festival_list);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);//
    }

    private void addRecyclerView() {


        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // TODO Auto-generated method stub
                //super.onScrollStateChanged(recyclerView, newState);
                try {
                    int firstPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if (firstPos > 0) {
                        swipeView.setEnabled(false);
                    } else {
                        swipeView.setEnabled(true);
                        if (recyclerView.getScrollState() == 1)
                            if (swipeView.isRefreshing())
                                recyclerView.stopScroll();
                    }

                } catch (Exception e) {
                    //Log.e(TAG, "Scroll Error : "+e.getLocalizedMessage());
                }
            }


        });
    }

    private void setToolbarItem() {
        Toolbar tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.fest_detail));
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void checkCachedData(String postUrl) {
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(postUrl);
        if (entry != null) {
            try {
                String saveData = new String(entry.data, "UTF-8");
                parseGsonData(saveData);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {

            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                getDetailFromServer(postUrl,true);
            }
        }
    }


    private void getDetailFromServer(String postUrl,boolean isShowProgressBar) {
        pd = new CustomProgressDialog(ActFestivalListView.this, typeface);
        if (isShowProgressBar)
        {
            pd.show();
        }

        pd.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //        //Log.e("Simple+" + response.toString());
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                            parseGsonData(response);

                        }
                        pd.dismiss();
                        if (swipeView != null) {
                            swipeView.setRefreshing(false);
                        }
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActFestivalListView.this, ActFestivalListView.this
                        .getLayoutInflater(), ActFestivalListView.this, typeface);
                mct.show(error.getMessage());


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
                pd.dismiss();
                if (swipeView != null) {
                    swipeView.setRefreshing(false);
                }
            }


        }


        ) {
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
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActFestivalListView.this));
                headers.put("isapi", "1");
                String cityIdToSend = null;
                if (allDetailData.get(0).getCityId() != null) {
                    cityIdToSend = allDetailData.get(0).getCityId();
                } else {
                    cityIdToSend = "";
                }
                headers.put("lid", cityIdToSend);
                headers.put("language", allDetailData.get(0).getLangCode());
                headers.put("date", allDetailData.get(0).getYear());
                return headers;
            }

        };


        ;
// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void parseGsonData(String saveData) {
        try {
            AllPanchangData allPanchangData;
            Gson gson = new Gson();
            allPanchangData = gson.fromJson(saveData, AllPanchangData.class);
            if (allPanchangData.getVratFestivalApiData() != null) {
                FragmentManager fm = getSupportFragmentManager();

                DetailApiModel obj = new DetailApiModel(CUtils.getLanguageCodeName(LANGUAGE_CODE), String.valueOf(ActYearlyVrat.year), ActYearlyVrat.cityId);
                ArrayList<DetailApiModel> arrayListObj = new ArrayList<DetailApiModel>();
                arrayListObj.add(obj);
                mAdapter = new PurnimaFastAdapter(ActFestivalListView.this, fm, allPanchangData.getVratFestivalApiData(), LANGUAGE_CODE,arrayListObj);
                recyclerView.setAdapter(mAdapter);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        if (swipeView != null) {
            swipeView.setRefreshing(true);
        }
        getDetailFromServer(festivalUrl,false);
    }
}
