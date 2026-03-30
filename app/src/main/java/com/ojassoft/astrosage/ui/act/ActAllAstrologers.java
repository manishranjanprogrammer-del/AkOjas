package com.ojassoft.astrosage.ui.act;

import com.google.android.material.tabs.TabLayout;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.AllAstrologerAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AllAstrologerInfo;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActAllAstrologers extends BaseInputActivity {

    private Toolbar tool_barAppModule;
    private TextView tvTitle,tvTopHeading;
    private RecyclerView mRecyclerView;
    private TabLayout tabLayout;
    private RequestQueue queue;
    private CustomProgressDialog pd = null;
    private List<AllAstrologerInfo> arrayListData;
    private AllAstrologerAdapter mAdapter;
    private String astrologerServerHitkey = "AstrologerServerHitkey";

    public ActAllAstrologers() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_all_astrologers);

        setLayRef();
    }

    /**
     * @author Amit Rautela
     * This method is used toset layout ref
     */
    private void setLayRef(){
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);

        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.astro_astrologer));
        tvTitle.setTypeface(mediumTypeface);

        tvTopHeading = (TextView) findViewById(R.id.tvTopHeading);
        tvTopHeading.setTypeface(robotMediumTypeface);
        tvTopHeading.setFocusable(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ActAllAstrologers.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        pd = new CustomProgressDialog(ActAllAstrologers.this, regularTypeface);

        arrayListData = new ArrayList<>();

        checkCachedData();
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

    /**
     * Check if data is already in cache. if not then download
     */
    private void checkCachedData() {

        String data = CUtils.getStringData(ActAllAstrologers.this,CGlobalVariables.All_Astrologer_List,"");

        if (data != null && !data.equals("")) {
            try {

                Date date = new Date();
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,
                        Locale.ENGLISH);
                String currentAstrologerServerHitkey = dateFormat.format(date.getTime());

                String savedAstrologerServerHitkey = CUtils.getStringData(ActAllAstrologers.this,astrologerServerHitkey,"");
                //String saveData = new String(entry.data, "UTF-8");
                parseJsonData(data,false);

                if(!savedAstrologerServerHitkey.equals(currentAstrologerServerHitkey)) {
                    downloadAstrologerDetails(false);
                    CUtils.saveStringData(ActAllAstrologers.this,astrologerServerHitkey,currentAstrologerServerHitkey);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, mediumTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                downloadAstrologerDetails(true);
            }
        }
    }

    /**
     * This method is used to download the user details
     */
    private void downloadAstrologerDetails(final boolean showDownloadData){

        if(showDownloadData)
        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.All_Astrologer_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {

                            CUtils.saveStringData(ActAllAstrologers.this,CGlobalVariables.All_Astrologer_List,response);

                           // CUtils.saveStringData(ActAllAstrologers.this,CGlobalVariables.);

                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                            if(showDownloadData) {
                                parseJsonData(response,false);
                            } else{
                                parseJsonData(response,true);
                            }


                        }

                        if(showDownloadData)
                        dismissProgressDialog();
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActAllAstrologers.this, ActAllAstrologers.this
                        .getLayoutInflater(), ActAllAstrologers.this, mediumTypeface);
                mct.show(error.getMessage());

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
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

                if(showDownloadData)
                dismissProgressDialog();
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
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAllAstrologers.this));
                return headers;
            }

        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    /**
     *Thi method is used to show the Progress Dialog
     */
    private void showProgressDialog(){
        if(pd != null && !pd.isShowing()) {
            pd.show();
            pd.setCancelable(false);
        }
    }

    /**
     *Thi method is used to dismiss the Progress Dialog
     */
    private void dismissProgressDialog(){
        if(pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * This method is used to parsing the Json data
     * @param data
     */
    private void parseJsonData(String data,boolean isNotifyAdapter){
        try {
            Gson gson = new Gson();
            if(arrayListData!=null) {
                arrayListData = gson.fromJson(data, new TypeToken<ArrayList<AllAstrologerInfo>>() {
                }.getType());
            }else{
                arrayListData = new ArrayList<>();
            }
            FragmentManager fm = getSupportFragmentManager();
            if (!isNotifyAdapter) {
                mAdapter = new AllAstrologerAdapter(ActAllAstrologers.this, fm, arrayListData);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }catch (JsonParseException ex){
            CUtils.saveStringData(ActAllAstrologers.this,CGlobalVariables.All_Astrologer_List,"");
        }catch (Exception ex){
            //
        }
    }
}
