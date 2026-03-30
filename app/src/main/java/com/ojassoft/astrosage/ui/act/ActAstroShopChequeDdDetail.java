package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
//import com.google.analytics.tracking.android.EasyTracker;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas on १२/४/१६.
 */
public class ActAstroShopChequeDdDetail extends BaseInputActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    Intent mainIntent;
    String order_id = "";
    private CustomProgressDialog pd = null;
    private Button btn_ok;
    private TextView tvaccountname, tvacnam, tvaccountNO, tvaccNo, tvbank, tvbnk, tvbranch, tvbrn;
    private TextView tvstepone, tvdepositcheque, tvsteptwo, tvinformus, tvchequedd;
    private Typeface typeface;
    private RequestQueue queue;
    private String otype="0";

    public ActAstroShopChequeDdDetail() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                this, LANGUAGE_CODE, CGlobalVariables.regular);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.activity_astroshop_chequedd_detail);
        mainIntent = getIntent();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                //Log.e("Result set","Done 1");
                if(otype.equalsIgnoreCase("1"))
                {this.setResult(1);
                finish();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), ActAstroShop.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

           /*     this.setResult(1);
                finish();*/
                break;
        }
    }

    private void init() {

        order_id = mainIntent.getStringExtra("OrderId");
        otype=mainIntent.getStringExtra("Otype");
        //Log.e("id", order_id);
        //Log.e("Otype",otype);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        tvTitle.setTypeface(((BaseInputActivity) this).regularTypeface);
        tvchequedd = (TextView) findViewById(R.id.tvchequedd);

        tvaccountname = (TextView) findViewById(R.id.tvaccountname);
        tvaccountname.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvacnam = (TextView) findViewById(R.id.tvacnam);
        tvacnam.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvaccountNO = (TextView) findViewById(R.id.tvaccountNO);
        tvaccountNO.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvaccNo = (TextView) findViewById(R.id.tvaccNo);
        tvaccNo.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvbank = (TextView) findViewById(R.id.tvbank);
        tvbank.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvbnk = (TextView) findViewById(R.id.tvbnk);
        tvbnk.setTypeface(((BaseInputActivity) this).robotRegularTypeface);

        tvbranch = (TextView) findViewById(R.id.tvbranch);
        tvbranch.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvbrn = (TextView) findViewById(R.id.tvbrn);
        tvbrn.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setTypeface(((BaseInputActivity) this).regularTypeface);
        btn_ok.setOnClickListener(this);

        tvstepone = (TextView) findViewById(R.id.tvstepone);
        //     tvstepone.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvstepone.setText(getResources().getString(R.string.astroshop_step_one));
        tvdepositcheque = (TextView) findViewById(R.id.tvdepositcheque);
        tvdepositcheque.setTypeface(((BaseInputActivity) this).regularTypeface);
        tvsteptwo = (TextView) findViewById(R.id.tvsteptwo);
        tvsteptwo.setText(getResources().getString(R.string.astroshop_step_two));
        //     tvsteptwo.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvinformus = (TextView) findViewById(R.id.tvinformus);
        //     tvinformus.setTypeface(((BaseInputActivity) this).regularTypeface);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                        Log.e("Respo", response.toString());
                        if (response.toString() != null) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                tvacnam.setText(" " + obj.getString("AccountName"));
                                tvaccNo.setText(" " + obj.getString("AccountNumber"));
                                tvbnk.setText(" " + obj.getString("Bank"));
                                tvbrn.setText(" " + obj.getString("Branch"));
                                obj.getString("Step2");
                                obj.getString("mail");
                                String main = getResources().getString(R.string.astroshop_inform_us);
                                main = main.replace("%", obj.getString("Step2"));
                                main = main.replace("@", obj.getString("mail"));
                                //Log.e("rrr", main);
                                tvinformus.setText(main);

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
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstroShopChequeDdDetail.this));
                headers.put("paymode", "1");
                headers.put("OType",otype);
                //Log.e("Cheque",headers.toString());
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
