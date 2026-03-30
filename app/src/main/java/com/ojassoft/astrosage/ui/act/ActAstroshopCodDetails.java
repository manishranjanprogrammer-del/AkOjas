package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by ojas on १३/५/१६.
 */
public class ActAstroshopCodDetails extends BaseInputActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private Typeface typeface;
    private Button btn_ok;
    private TextView tvcashheading, tvcodsuccess, tvenquiry;
    private RequestQueue queue;
    private CustomProgressDialog pd = null;


    public ActAstroshopCodDetails() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                this, LANGUAGE_CODE, CGlobalVariables.regular);

        setContentView(R.layout.activity_astroshop_cod_details);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        pd = new CustomProgressDialog(this, typeface);
        init();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!CUtils.isConnectedWithInternet(this)) {
            MyCustomToast mct = new MyCustomToast(this, this
                    .getLayoutInflater(), this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            loadChequeDdData();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                goToAstroShop();
                break;
        }
    }

    private void init() {


        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        tvTitle.setTypeface(((BaseInputActivity) this).regularTypeface);
        tvcashheading = (TextView) findViewById(R.id.tvcashheading);
        tvcashheading.setTypeface(((BaseInputActivity) this).regularTypeface);

        tvcodsuccess = (TextView) findViewById(R.id.tvcodsuccess);
        //   tvcodsuccess.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvenquiry = (TextView) findViewById(R.id.tvenquiry);
        //  tvenquiry.setTypeface(((BaseInputActivity) this).mediumTypeface);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setTypeface(((BaseInputActivity) this).regularTypeface);
        btn_ok.setOnClickListener(this);


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

    @Override
    public void onBackPressed() {
        goToAstroShop();
        super.onBackPressed();
    }

    private void goToAstroShop() {
        Intent intent = new Intent(getApplicationContext(), ActAstroShop.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void loadChequeDdData() {
        pd.show();
        pd.setCancelable(false);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroshopCehequeDdUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pd.isShowing()) {
                            pd.dismiss();

                        }
                        //Log.e("Respo", response.toString());
                        if (response.toString() != null) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                String main = getResources().getString(R.string.astro_shop_cash_on_delivery_help);
                                main = main.replace("@", obj.getString("Ph_No"));
                                main = main.replace("%", obj.getString("mail"));

                                tvenquiry.setText(main);

                            } catch (Exception e) {

                            }

                        }

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
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
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstroshopCodDetails.this));
                headers.put("paymode", "2");
                return headers;
            }

        };


        ;


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }
}
