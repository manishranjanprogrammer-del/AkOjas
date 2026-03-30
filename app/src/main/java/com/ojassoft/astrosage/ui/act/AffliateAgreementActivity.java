package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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

public class AffliateAgreementActivity extends BaseInputActivity implements VolleyResponse {
    TextView headingTV;
    CheckBox checkBox;
    TextView agreementTV;
    Button button;
    private Toolbar toolbar;
    TabLayout tabLayout;
    TextView tvTitle;
    private RequestQueue queue;
    CustomProgressDialog pd;
    LinearLayout container;

    public AffliateAgreementActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.affliate_agreement_act_layout);
        container = findViewById(R.id.container);
        headingTV = findViewById(R.id.heading_text_view);
        checkBox = findViewById(R.id.checkbox);
        agreementTV = findViewById(R.id.agree_text);
        button = findViewById(R.id.sign_up_btn);
        toolbar = findViewById(R.id.tool_barAppModule);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.affliated_program));
        tvTitle.setVisibility(View.VISIBLE);
        ImageView ivToggleImage = findViewById(R.id.ivToggleImage);
        ivToggleImage.setVisibility(View.GONE);
        setTypefaceOfView();
        enableToolBar();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CUtils.isConnectedWithInternet(AffliateAgreementActivity.this)) {
                    doActionOnSignUpButton();
                } else {
                    CUtils.showSnakbar(container, getResources().getString(R.string.internet_is_not_working));
                }

            }
        });
    }

    private void setTypefaceOfView() {
        headingTV.setTypeface(mediumTypeface);
        headingTV.setTypeface(regularTypeface);
        agreementTV.setTypeface(regularTypeface);
        button.setTypeface(mediumTypeface);

    }

    private void enableToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    private void doActionOnSignUpButton() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_AFFILIATE_AGREEMENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        if (checkBox.isChecked()) {
            getDataFromServer();
        } else {
            CUtils.showSnakbar(container, getResources().getString(R.string.checkbox_validation));
        }

    }

    private void getDataFromServer() {
        showProgressBar();
        String url = CGlobalVariables.affiliateHome;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsForBrandingDetail(), 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    private HashMap<String, String> getParamsForBrandingDetail() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(AffliateAgreementActivity.this));
        params.put("userid", CUtils.getUserName(AffliateAgreementActivity.this));
        params.put("isapi", "1");
        params.put("chkAgreeDisagree", "1");

        return params;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(AffliateAgreementActivity.this, regularTypeface);
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
        Log.i("Resp", response);
        hideProgressBar();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseCode = jsonObject.getString("responsecode");
            if (responseCode.equals("1")) {
                CUtils.saveStringData(AffliateAgreementActivity.this, "affiliatePartnerId", jsonObject.getString("affiliatePartnerId"));
                Intent intent = new Intent(AffliateAgreementActivity.this, AffliateHomeActivity.class);
                startActivity(intent);
            } else {
                CUtils.showSnakbar(container, jsonObject.getString("msg"));
            }

        } catch (Exception e) {
            CUtils.showSnakbar(container, e.getMessage());
        }

    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        CUtils.showSnakbar(container, error.getMessage());
    }

}
