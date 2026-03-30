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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.VolleyResponse;
import com.ojassoft.astrosage.misc.VolleyServiceHandler;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;

public class MyServicesAndProductActivity extends BaseInputActivity implements VolleyResponse {
    TextView tvTitle;
    public Toolbar toolBar_InputKundli;
    private TabLayout tabs_input_kundli;
    private RequestQueue queue;
    CustomProgressDialog pd;
    TextView heading1TV;
    TextView heading2TV;
    TextView desc1TV;
    TextView desc2TV;
    TextView copyHereServiceTV;
    TextView copyHereServiceValTV;

    TextView copyHereProductTV;
    TextView copyHereProductValTV;

    TextView clickHereTV;
    Button clickHereServiceValBtn;
    Button clickHereProductValBtn;
    TextView coupanCodeTV;
    TextView coupanCodeValTV;
    TextView discountTV;
    TextView discountValTV;
    TextView expiryDateTV;
    TextView expiryDateValTV;
    TextView totalUserTV;
    TextView totalUserValTV;
    TextView productCatTV;
    TextView productCatValTV;
    String ptnrID;
    LinearLayout container;


    public MyServicesAndProductActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.my_service_and_product_layout);
        container = findViewById(R.id.container);
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        tabs_input_kundli = findViewById(R.id.tabs);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.buy_product));

        heading1TV = findViewById(R.id.heading_tv1);
        heading2TV = findViewById(R.id.heading_tv2);
        desc1TV = findViewById(R.id.desc_tv1);
        desc2TV = findViewById(R.id.desc_tv2);

        copyHereProductTV = findViewById(R.id.copy_here_product_tv);
        copyHereProductValTV = findViewById(R.id.copy_url_product_tv);
        copyHereServiceTV = findViewById(R.id.copy_here_service_tv);
        copyHereServiceValTV = findViewById(R.id.copy_url_service_tv);

        clickHereTV = findViewById(R.id.or_click_here_tv);

        coupanCodeTV = findViewById(R.id.coupan_code_tv);
        coupanCodeValTV = findViewById(R.id.coupan_code_val_tv);
        discountTV = findViewById(R.id.discount_tv);
        discountValTV = findViewById(R.id.discount_val_tv);
        expiryDateTV = findViewById(R.id.expiry_date_tv);
        expiryDateValTV = findViewById(R.id.expiry_date_val_tv);
        totalUserTV = findViewById(R.id.total_user_limit_tv);
        totalUserValTV = findViewById(R.id.total_user_limit_val_tv);
        productCatTV = findViewById(R.id.product_category_tv);
        productCatValTV = findViewById(R.id.product_category_val_tv);

        clickHereServiceValBtn = findViewById(R.id.buy_service_btn);
        clickHereProductValBtn = findViewById(R.id.buy_product_btn);
        enableToolBar();
        setTypeface();
        if (CUtils.isConnectedWithInternet(this)) {
            getDataFromServer();
        } else {
            CUtils.showSnakbar(container, getResources().getString(R.string.internet_is_not_working));
        }

        clickHereServiceValBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_AFFILIATE_BUY_SERVICES, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(MyServicesAndProductActivity.this, ptnrID);
                Intent i = new Intent(MyServicesAndProductActivity.this, ActAstroShopServices.class);
                startActivity(i);
            }
        });
        clickHereProductValBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_AFFILIATE_BUY_PRODUCT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(MyServicesAndProductActivity.this, ptnrID);
               /* Intent goToAstroShop = new Intent(MyServicesAndProductActivity.this,
                        ActAstroShop.class);
                goToAstroShop.putExtra(CGlobalVariables.MODULE_TYPE_KEY, 0);
                goToAstroShop.putExtra(CGlobalVariables.RedirectUrlFromAstroShopKey, "");
                startActivity(goToAstroShop);*/
                CUtils.getUrlLink("https://buy.astrosage.com/gemstone", MyServicesAndProductActivity.this, LANGUAGE_CODE, 0);
            }
        });
    }

    private void enableToolBar() {
        setSupportActionBar(toolBar_InputKundli);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabs_input_kundli.setVisibility(View.GONE);
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

    private void setTypeface() {
        heading1TV.setTypeface(robotMediumTypeface);
        heading2TV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        desc1TV.setTypeface(robotRegularTypeface);
        desc2TV.setTypeface(robotRegularTypeface);
        copyHereServiceTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        copyHereServiceValTV.setTypeface(robotRegularTypeface);
        clickHereTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        clickHereServiceValBtn.setTypeface(robotMediumTypeface);
        clickHereProductValBtn.setTypeface(robotMediumTypeface);

        copyHereProductTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        copyHereProductValTV.setTypeface(robotRegularTypeface);

        coupanCodeTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        coupanCodeValTV.setTypeface(robotRegularTypeface);
        discountTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        discountValTV.setTypeface(robotRegularTypeface);
        expiryDateTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        expiryDateValTV.setTypeface(robotRegularTypeface);
        totalUserTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        totalUserValTV.setTypeface(robotRegularTypeface);
        productCatTV.setTypeface(robotMediumTypeface, Typeface.BOLD);
        productCatValTV.setTypeface(robotRegularTypeface);
    }

    private void getDataFromServer() {
        showProgressBar();
        //String url = "http://14f1f0c4c250.ngrok.io/dhruv/affiliate/buy-product.jsp";
        String url = CGlobalVariables.buyProductUrl;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsForBrandingDetail(), 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    private HashMap<String, String> getParamsForBrandingDetail() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(MyServicesAndProductActivity.this));
        params.put("userid", CUtils.getUserName(MyServicesAndProductActivity.this));
        params.put("isapi", "1");


        return params;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(MyServicesAndProductActivity.this, regularTypeface);
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

    @Override
    public void onResponse(String response, int method) {
        Log.i("Result", response);
        hideProgressBar();
        //{"affiliatePartnerId":"D0001","msg":"","responsecode":1,"serviceUrl":"https://buy.astrosage.com/service?prtnr_id=D0001","productUrl":"https://buy.astrosage.com/products?prtnr_id=D0001"}
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseCode = jsonObject.getString("responsecode");
            if (responseCode.equals("1")) {
                ptnrID = jsonObject.getString("affiliatePartnerId");
                copyHereProductValTV.setText(": " + jsonObject.getString("productUrl"));
                copyHereServiceValTV.setText(": " + jsonObject.getString("serviceUrl"));
            } else {
                CUtils.showSnakbar(container, jsonObject.getString("msg"));
            }


        } catch (Exception e) {

        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        CUtils.showSnakbar(container, error.getMessage());
    }
}