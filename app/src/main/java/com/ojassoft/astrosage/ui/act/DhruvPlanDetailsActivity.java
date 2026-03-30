package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_USER_ID;

/**
 * Created By Abhishek Raj
 */
public class DhruvPlanDetailsActivity extends BaseInputActivity implements View.OnClickListener, VolleyResponse {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private TextView titleBillPlanTV;
    private TextView monthlyTV;
    private TextView topupTV;
    private TextView dhruvPlanTV;
    private CustomProgressDialog pd;

    public DhruvPlanDetailsActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhruv_plan_details);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        titleBillPlanTV = findViewById(R.id.titleBillPlanTV);
        dhruvPlanTV = findViewById(R.id.dhruvPlanTV);
        topupTV = findViewById(R.id.topupTV);
        monthlyTV = findViewById(R.id.monthlyTV);

        tvTitle.setText(getString(R.string.plan_details));
        tvTitle.setTypeface(regularTypeface);
        titleBillPlanTV.setTypeface(mediumTypeface);
        dhruvPlanTV.setTypeface(regularTypeface);
        topupTV.setTypeface(regularTypeface);
        monthlyTV.setTypeface(regularTypeface);

        getPlanDetails();
    }

    private void initContext() {
        currentActivity = DhruvPlanDetailsActivity.this;
    }

    private void initListener() {

    }

    @Override
    public void onClick(View v) {

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

    @Override
    public void onResponse(String response, int method) {
        Log.e("PlanDetailsResponse", "method = " + method + response);
        hideProgressBar();
        try {
            JSONObject respObj = new JSONObject(response);
            parseJsonDataAndUpdateUi(respObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        if (error != null) {
            showSnackbar(titleBillPlanTV, error.toString());
        }
        setDataAfterError();
    }

    private void getPlanDetails() {
        if (com.ojassoft.astrosage.utils.CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.getPlanDetailsRequest(DhruvPlanDetailsActivity.this, getTopupDetailsRequestParams(), 1);
        } else {
            showSnackbar(titleBillPlanTV, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getTopupDetailsRequestParams() {

        String key = CUtils.getApplicationSignatureHashCode(currentActivity);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put(KEY_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(currentActivity)));
        return params;
    }

    private void showProgressBar() {
        pd = new CustomProgressDialog(this, regularTypeface);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJsonDataAndUpdateUi(JSONObject respObj) {
        try {
            String response = respObj.getString("responsecode");
            if (!TextUtils.isEmpty(response) && response.equalsIgnoreCase("1")) {
                int pdfDownloadRemCount = respObj.getInt("pdfDownloadRemCount");
                int topupPDFCount = respObj.getInt("topupPDFCount");
                String planExpDate = respObj.getString("planExpDate");

                monthlyTV.setText(Html.fromHtml(getResources().getString(R.string.msg_monthly_pdf_count).replace("#", pdfDownloadRemCount + "")));
                topupTV.setText(Html.fromHtml(topupTV.getText().toString().replace("#", topupPDFCount + "")));
                dhruvPlanTV.setText(Html.fromHtml(dhruvPlanTV.getText().toString().replace("#", planExpDate)));
            } else {
                showSnackbar(titleBillPlanTV, respObj.getString("message"));
                setDataAfterError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setDataAfterError();
        }
    }

    private void setDataAfterError(){
        monthlyTV.setText(Html.fromHtml(monthlyTV.getText().toString().replace("#", "0")));
        topupTV.setText(Html.fromHtml(topupTV.getText().toString().replace("#", "0")));
        dhruvPlanTV.setText(Html.fromHtml(dhruvPlanTV.getText().toString().replace("#", "")));

    }
}
