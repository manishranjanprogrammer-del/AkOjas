package com.ojassoft.astrosage.ui.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.AstroShopServiceAdapter;
import com.ojassoft.astrosage.misc.AstroServiceDownloader;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

//import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on १/६/१६.
 */

public class ActAstroShopServices extends BaseInputActivity implements View.OnClickListener {
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private RequestQueue queue;
    CustomProgressDialog pd = null;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeface;
    private ImageView imgicmore, imgicviewlist, imgicviewmodule, imgshopingcart;
    private RecyclerView my_recycler_view;
    private RecyclerView.LayoutManager mLayoutManager;
    String id;
    private BroadcastReceiver receiver;
    private ArrayList<ServicelistModal> referdata;
    private String redirectUrl;
    public String sourceActivity ="";
    private Boolean shouldCache = false;

    public ActAstroShopServices() {
        super(R.string.app_name);
    }

    private AstroShopServiceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_services);
        if (getIntent().getExtras() != null)
            id = getIntent().getExtras().getString("AstroId");
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (id != null) {
            tvTitle.setText(getResources().getString(R.string.astro_services_by_astrologer));
        } else {
            tvTitle.setText(getResources().getString(R.string.astro_services));
        }

        tvTitle.setTypeface(typeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        imgicmore = (ImageView) findViewById(R.id.imgMoreItem);
        imgicviewlist = (ImageView) findViewById(R.id.imgicviewlist);
        imgicviewmodule = (ImageView) findViewById(R.id.imgicviewmodule);
        imgshopingcart = (ImageView) findViewById(R.id.imgshopingcart);
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            redirectUrl = getIntent().getStringExtra(CGlobalVariables.RedirectUrlFromAstroShopServicesKey);
            sourceActivity = getIntent().getStringExtra(CGlobalVariables.SOUCRE_ACTIVITY);
        } catch (Exception ex) {
            //android.util.//Log.i("TAG", ex.getMessage().toString());
        }

        init();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                referdata = (ArrayList<ServicelistModal>) intent.getSerializableExtra(AstroServiceDownloader.BROAD_RESULT);

                //Log.e("Data trecived on" + referdata.size());
                if (referdata.size() > 0) {
                    updateListData(referdata);

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(AstroServiceDownloader.BROAD_ACTION)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgicviewmodule:
                break;
            case R.id.imgicviewlist:
                break;
        }
    }

    private void init() {
        imgicmore.setVisibility(View.GONE);
        imgshopingcart.setVisibility(View.GONE);
        imgicviewmodule.setOnClickListener(this);
        imgicviewlist.setOnClickListener(this);
        imgshopingcart.setOnClickListener(this);
        imgicmore.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(this);
        my_recycler_view.setLayoutManager(mLayoutManager);
        if (id != null) {

            if (id.isEmpty()) {
                id = "";
            }

            loadAstroShopServiceData(id);

        } else {
            checkCachedData();
        }

        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTROSHOP_SERVICES_DEEP_LINKING,
                FirebaseAnalytics.Event.VIEW_ITEM_LIST, "");

    }


    private void loadAstroShopServiceData(final String astroId) {
        pd = new CustomProgressDialog(ActAstroShopServices.this, typeface);
        pd.show();
        pd.setCancelable(false);
        //Log.e("id" + astroId);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopServiceList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("apiOutput", response.toString());
                        hideProgressBar();
                        if (response != null && !response.isEmpty()) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                response = str;
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.has("Result")) {
                                    MyCustomToast mct = new MyCustomToast(ActAstroShopServices.this, ActAstroShopServices.this.getLayoutInflater(), ActAstroShopServices.this, typeface);
                                    mct.show(getResources().getString(R.string.server_error_msg));
                                    return;
                                }
                                if (shouldCache) {
                                    CUtils.saveStringData(ActAstroShopServices.this, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), response);
                                }

                                parseGsonData(response);

                            } catch (Exception e) {
                                CUtils.saveStringData(ActAstroShopServices.this, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), "");

                                MyCustomToast mct = new MyCustomToast(ActAstroShopServices.this, ActAstroShopServices.this.getLayoutInflater(), ActAstroShopServices.this, typeface);
                                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                                finish();
                            }

                        } else {
                            MyCustomToast mct = new MyCustomToast(ActAstroShopServices.this, ActAstroShopServices.this.getLayoutInflater(), ActAstroShopServices.this, typeface);
                            mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                            CUtils.saveStringData(ActAstroShopServices.this, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), "");

                            finish();

                        }

//                        data = gson.fromJson(response, new TypeToken<ArrayList<AstroShopMaindata>>() {
//                        }.getType());
                        //    //Log.e("Size returned" + data.get(0).getGemStones().size());

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error != null && error.getMessage() != null) {
                    Log.e("Error Through", error.getMessage());
                    MyCustomToast mct = new MyCustomToast(ActAstroShopServices.this, ActAstroShopServices.this
                            .getLayoutInflater(), ActAstroShopServices.this, typeface);
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
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (isDestroyed()) { // or call isFinishing() if min sdk version < 17
                        return;
                    }
                } else {
                    if (isFinishing()) { // or call isFinishing() if min sdk version < 17
                        return;
                    }
                }
                hideProgressBar();
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
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstroShopServices.this));
                headers.put("profile_Id", astroId);
                headers.put("langcode", "" + LANGUAGE_CODE);
                headers.put("app_version_code", "" + BuildConfig.VERSION_CODE);

                // parameter added for discount
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActAstroShopServices.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActAstroShopServices.this)));

                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
               // headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);


                //Log.e("services", headers.toString());
                return headers;
            }
        };
        ;
// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        if (astroId.isEmpty()) {
            shouldCache = true;
            //stringRequest.setShouldCache(true);

        } else {
            shouldCache = false;
            //   stringRequest.setShouldCache(false);
        }
        queue.add(stringRequest);
    }


    private void parseGsonData(String saveData) {
        try {
            List<ServicelistModal> data;
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(saveData.toString(), JsonElement.class);
            //Log.e("Element" + element.toString());
            data = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
            }.getType());
            //Log.e("List size" + data.size());
            adapter = new AstroShopServiceAdapter(this, data, id, LANGUAGE_CODE);
            my_recycler_view.setAdapter(adapter);
            //  setupViewPager();
            if (redirectUrl != null && !redirectUrl.equals("")) {
                checkForRedirectToAstroShopServicesItemDescription(data);
            }
        } catch (Exception ex) {

        }
    }

    private void checkForRedirectToAstroShopServicesItemDescription(List<ServicelistModal> data) {

        try {

           /* Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
            Cache.Entry entry = cache.get(CGlobalVariables.astroShopServiceList);
            if (entry != null) {*/
            // String cacheData =CUtils.getStringData(this,CGlobalVariables.Astroshop_Service_Data+String.valueOf(LANGUAGE_CODE),"");

            if (data != null && data.size() > 0) {
                try {

                    CUtils.goToServiceDescription(ActAstroShopServices.this, data, redirectUrl, sourceActivity,true);

                } catch (Exception ex) {
                    //android.util.//Log.i("TAG", ex.getMessage().toString());
                }
            }
        } catch (Exception ex) {
            //android.util.//Log.i("TAG", ex.getMessage().toString());
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void checkCachedData() {
        String data = CUtils.getStringData(this, CGlobalVariables.Astroshop_Service_Data + String.valueOf(LANGUAGE_CODE), "");

        if (data != null && !data.isEmpty()) {
            try {
                Intent i = new Intent(ActAstroShopServices.this, AstroServiceDownloader.class);
                startService(i);
                parseGsonData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                loadAstroShopServiceData("");
            }
        }


    }

    private void updateListData(ArrayList<ServicelistModal> data) {
        if (adapter != null) {
            ((AstroShopServiceAdapter) adapter).updateListData(data);
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
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
