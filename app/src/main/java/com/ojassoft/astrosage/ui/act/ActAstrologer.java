package com.ojassoft.astrosage.ui.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.AstrologerAdapter;
import com.ojassoft.astrosage.misc.AstrologerDownloadService;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AstrologerInfo;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas on ३/६/१६.
 */
public class ActAstrologer extends BaseInputActivity implements View.OnClickListener {
    private Toolbar tool_barAppModule;
    public int SELECTED_MODULE;
    private RequestQueue queue;
    private TabLayout tabLayout;
    private CustomProgressDialog pd = null;
    private TextView tvTitle;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeface;

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AstrologerInfo data = new AstrologerInfo();
    private List<AstrologerInfo> arrayListdata = new ArrayList<AstrologerInfo>();
    private BroadcastReceiver receiver;
    private ArrayList<AstrologerInfo> referdata;
    private String redirectUrl;

    public ActAstrologer() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astrologer_main);
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.astro_astrologer));
        tvTitle.setTypeface(typeface);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ActAstrologer.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        try {
            redirectUrl = getIntent().getStringExtra(CGlobalVariables.RedirectUrlFromAstroShopAstrologerKey);
        } catch (Exception ex) {
            //android.util.//Log.i("TAG", ex.getMessage().toString());
        }


        if (!CUtils.isConnectedWithInternet(ActAstrologer.this)) {
            MyCustomToast mct = new MyCustomToast(ActAstrologer.this, ActAstrologer.this
                    .getLayoutInflater(), ActAstrologer.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            checkCachedData();
            //  loadAstroShopData();
        }


        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                referdata = (ArrayList<AstrologerInfo>) intent.getSerializableExtra(AstrologerDownloadService.BROAD_RESULT);

                //Log.e("Data trecived on"+referdata.size());
                if(referdata.size()>0)
                {
                    updateListData(referdata);

                }
            }
        };

       /* mAdapter = new AstrologerAdapter(ActAstrologer.this, arrayListdata);
        mRecyclerView.setAdapter(mAdapter);*/
    }




    private void checkCachedData() {
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(CGlobalVariables.astrologerLive);
        if (entry != null) {
            try {
                //  isCached = true;
                String saveData = new String(entry.data, "UTF-8");
                //     //Log.e("Volley Cached Data" + data.toString());
                   parseGsonData(saveData);

                Intent i = new Intent(ActAstrologer.this, AstrologerDownloadService.class);
                startService(i);
                //Log.e("Hitting astro service now");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            //Log.e("Volley Not Cached Data");
             if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                downloadAstrologerDetails();
            }

        }


    }

    private void init() {
       /* grid=(GridView) findViewById(R.id.gridView);
        moduleNameList = getResources().getStringArray(R.array.astroshopcategories);
        AstroshopcategoriesAdapter adapter = new AstroshopcategoriesAdapter(this, moduleIconList, moduleNameList, ((BaseInputActivity) this).mediumTypeface, ActAstroShopCategories.this);
        grid.setAdapter(adapter);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(AstrologerDownloadService.BROAD_ACTION)
        );
    }

    @Override
    protected void onStop() {
        //EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadAstrologerDetails() {
        pd = new CustomProgressDialog(ActAstrologer.this, typeface);
        pd.show();
        pd.setCancelable(false);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astrologerLive,
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
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActAstrologer.this, ActAstrologer.this
                        .getLayoutInflater(), ActAstrologer.this, typeface);
                mct.show(error.getMessage());

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
                pd.dismiss();
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
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstrologer.this));
                //   headers.put("Key","9865");
                return headers;
            }

        };


        ;
// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void parseGsonData(String saveData) {
        Gson gson = new Gson();
        //  JsonElement element = gson.fromJson(saveData.toString(), JsonElement.class);
        //Log.e("Element" + saveData.toString());
        arrayListdata = gson.fromJson(saveData, new TypeToken<ArrayList<AstrologerInfo>>() {
        }.getType());
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new AstrologerAdapter(ActAstrologer.this, fm, arrayListdata);
        mRecyclerView.setAdapter(mAdapter);

        if (redirectUrl != null && !redirectUrl.equals("")) {
            checkForRedirectToAstrologerItemDescription(arrayListdata);
        }
    }

    private void checkForRedirectToAstrologerItemDescription(List<AstrologerInfo> arrayListdata) {
        try {

            Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
            Cache.Entry entry = cache.get(CGlobalVariables.astrologerLive);
            if (entry != null) {
                try {

                    CUtils.goToAstrologerServiceDescription(ActAstrologer.this,arrayListdata,redirectUrl,true);

                } catch (Exception ex) {
                    //android.util.//Log.i("TAG", ex.getMessage().toString());
                }
            }
        } catch (Exception ex) {
            //android.util.//Log.i("TAG", ex.getMessage().toString());
        }
    }

    @Override
    public void onClick(View v) {

    }


private void updateListData(ArrayList<AstrologerInfo> data)
{
    if(mAdapter!=null)
    {
        ((AstrologerAdapter)mAdapter).updateListData(data);
    }
}


}
