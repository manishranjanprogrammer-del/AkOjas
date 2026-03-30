package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.tabs.TabLayout;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.DhruvAstrologerList;
import com.ojassoft.astrosage.ui.fragments.DhruvLeadSucessfulDialog;
import com.ojassoft.astrosage.ui.fragments.FreeConsultationDialog;
import com.ojassoft.astrosage.ui.fragments.FreeConsultationOTPDialog;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By Abhishek Raj
 */
public class AstrologerListingDetailsActivity extends BaseInputActivity implements View.OnClickListener,
        VolleyResponse {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private CustomProgressDialog pd;
    private TextView title2TV;
    private TextView astrologer_description_dummy;
    private TextView read_more_txt;
    private TextView astrologer_description;
    private Button btnBuyNow;
    private TextView title1TV;
    private TextView address1TV;
    private TextView address2TV;
    private TextView aboutTV;
    private LinearLayout astrologer_description_dummy_layout;
    private TextView lblExpTV;
    private TextView valExpTV;
    private TextView lblSystemKnownTV;
    private TextView valSystemKnownTV;
    private TextView lblLangKnownTV;
    private TextView valLangKnownTV;
    private TextView ratingTV;
    private TextView voteTV;
    private AppCompatRatingBar ratingStar;
    private LinearLayout rating_layout;
    private TextView astrologers1TV;
    private TextView astrologers2TV;
    private NetworkImageView networkProfileIV;
    private ImageView profileIV;
    private RequestQueue queue;
    private String profileId;

    public AstrologerListingDetailsActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astrologer_listing_details);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        title2TV = findViewById(R.id.title2TV);
        astrologer_description_dummy = findViewById(R.id.astrologer_description_dummy);
        read_more_txt = findViewById(R.id.read_more_txt);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        title1TV = findViewById(R.id.title1TV);
        address1TV = findViewById(R.id.address1TV);
        address2TV = findViewById(R.id.address2TV);
        astrologer_description_dummy_layout = findViewById(R.id.astrologer_description_dummy_layout);
        astrologer_description = findViewById(R.id.astrologer_description);
        aboutTV = findViewById(R.id.aboutTV);
        lblExpTV = findViewById(R.id.lblExpTV);
        valExpTV = findViewById(R.id.valExpTV);
        lblSystemKnownTV = findViewById(R.id.lblSystemKnownTV);
        valSystemKnownTV = findViewById(R.id.valSystemKnownTV);
        lblLangKnownTV = findViewById(R.id.lblLangKnownTV);
        valLangKnownTV = findViewById(R.id.valLangKnownTV);
        voteTV = findViewById(R.id.voteTV);
        ratingTV = findViewById(R.id.ratingTV);
        ratingStar = findViewById(R.id.rating_star);
        rating_layout = findViewById(R.id.rating_layout);
        astrologers1TV = findViewById(R.id.astrologers1TV);
        astrologers2TV = findViewById(R.id.astrologers2TV);
        networkProfileIV = findViewById(R.id.networkProfileIV);
        profileIV = findViewById(R.id.profileIV);

        tvTitle.setText(getString(R.string.directory_listing));
        tvTitle.setTypeface(regularTypeface);
        title1TV.setTypeface(mediumTypeface);
        title2TV.setTypeface(mediumTypeface);
        btnBuyNow.setTypeface(mediumTypeface);
        address1TV.setTypeface(regularTypeface);
        address2TV.setTypeface(regularTypeface);
        astrologers1TV.setTypeface(regularTypeface);
        astrologers2TV.setTypeface(regularTypeface);
        astrologer_description_dummy.setTypeface(regularTypeface);
        read_more_txt.setTypeface(mediumTypeface);
        lblExpTV.setTypeface(mediumTypeface);
        valExpTV.setTypeface(mediumTypeface);
        lblSystemKnownTV.setTypeface(mediumTypeface);
        valSystemKnownTV.setTypeface(mediumTypeface);
        lblLangKnownTV.setTypeface(mediumTypeface);
        valLangKnownTV.setTypeface(mediumTypeface);
        aboutTV.setTypeface(mediumTypeface);
        astrologer_description.setTypeface(regularTypeface);
        voteTV.setTypeface(regularTypeface);
        ratingTV.setTypeface(mediumTypeface);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        read_more_txt.setText(Html.fromHtml(getResources().getString(R.string.read_more)));
        lblExpTV.append(":");
        lblSystemKnownTV.append(":");
        lblLangKnownTV.append(":");
        if(getIntent() != null){
            profileId = getIntent().getStringExtra("profileId");
        }
        //profileId = "4875";
        if(!TextUtils.isEmpty(profileId)) {
            getProfileDetails();
        }
    }

    private void initContext() {
        currentActivity = AstrologerListingDetailsActivity.this;
    }

    private void initListener() {
        btnBuyNow.setOnClickListener(this);
        read_more_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuyNow: {
                if (!CUtils.isConnectedWithInternet(currentActivity)) {
                    showSnackbar(btnBuyNow, getResources().getString(R.string.no_internet));
                } else {
                    try {
                        FreeConsultationDialog dialog = new FreeConsultationDialog(profileId);
                        dialog.show(getSupportFragmentManager(), "Dialog");
                    } catch (Exception e) {

                    }
                }
                break;
            }
            case R.id.read_more_txt: {
                astrologer_description_dummy.setVisibility(View.GONE);
                astrologer_description.setVisibility(View.VISIBLE);
                read_more_txt.setVisibility(View.GONE);
                break;
            }
        }
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

    private void getProfileDetails() {
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.getAstrologerProfileDetailsRequest(AstrologerListingDetailsActivity.this, getProfileDetailsRequestParams(), 1);
        } else {
            showSnackbar(btnBuyNow, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getProfileDetailsRequestParams() {

        String key = CUtils.getApplicationSignatureHashCode(currentActivity);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("languagecode", LANGUAGE_CODE + "");
        params.put("profileId", profileId);
        //Log.e("JoinReqResponse  params", params.toString());
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

    @Override
    public void onResponse(String response, int method) {
        Log.e("JoinReqResponse", "method = " + method + response);
        hideProgressBar();
        try {
            JSONObject respObj = new JSONObject(response);
            parseJsonDataAndUpdateUi(respObj);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar(btnBuyNow, e.toString());
            finish();
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        if (error != null) {
            showSnackbar(btnBuyNow, error.toString());
            finish();
        }
    }

    private void parseJsonDataAndUpdateUi(JSONObject respObj) {
        try {
            JSONObject profileDetailsObj = respObj.getJSONObject("ProfileDetails");
            String name = profileDetailsObj.getString("Name");
            String title = profileDetailsObj.getString("Title");
            String description = profileDetailsObj.getString("Description");
            String address = profileDetailsObj.getString("FullAddress");
            String rating = profileDetailsObj.getString("AverageRating");
            String city = profileDetailsObj.getString("City");
            String imageURL = profileDetailsObj.getString("ImageURL");

            if(!TextUtils.isEmpty(imageURL)){
                networkProfileIV.setImageUrl(imageURL, VolleySingleton.getInstance(currentActivity).getImageLoader());
                profileIV.setVisibility(View.GONE);
                networkProfileIV.setVisibility(View.VISIBLE);
            }else {
                profileIV.setVisibility(View.VISIBLE);
                networkProfileIV.setVisibility(View.GONE);
            }

            title1TV.setText(title + " " + name);
            title2TV.setText(title + " " + name);
            aboutTV.append(" " + name);
            address1TV.setText(address);
            address2TV.setText(address);
            astrologer_description.setText(description);
            astrologer_description_dummy.setText(description);
            if (!TextUtils.isEmpty(description)) {
                read_more_txt.setVisibility(View.VISIBLE);
            }
            valExpTV.setText(profileDetailsObj.getString("Experience"));
            valSystemKnownTV.setText(profileDetailsObj.getString("PrimaryExperties"));
            valLangKnownTV.setText(profileDetailsObj.getString("Language"));
            if (TextUtils.isEmpty(rating) || rating.equalsIgnoreCase("0") || rating.equalsIgnoreCase("0.0")) {
                rating_layout.setVisibility(View.GONE);
            } else {
                rating_layout.setVisibility(View.VISIBLE);
                voteTV.setText(profileDetailsObj.getString("TotalRating"));
                voteTV.append(" " + getString(R.string.text_votes));
                ratingTV.setText(rating);
                ratingStar.setRating(Float.parseFloat(rating));
            }

            astrologers1TV.append(" "+city);
            astrologers2TV.append(" "+city);
        } catch (Exception e) {
            showSnackbar(btnBuyNow, e.toString());
            finish();
        }
    }

    public void showOtpDialog(String fromwhere){

        Log.e("DATAA ", fromwhere);

        if(fromwhere.equalsIgnoreCase(CGlobalVariables.SHIW_OTP_DIALOG)) {
            FreeConsultationOTPDialog dialog = new FreeConsultationOTPDialog(
                    CUtils.getStringData(AstrologerListingDetailsActivity.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_USERNAME, ""),
                    CUtils.getStringData(AstrologerListingDetailsActivity.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_EMAILID, ""),
                    CUtils.getStringData(AstrologerListingDetailsActivity.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_COUNTRY_CODE, ""),
                    CUtils.getStringData(AstrologerListingDetailsActivity.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_MOBILENO, ""),
                    CUtils.getStringData(AstrologerListingDetailsActivity.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_ASTROLOGERID, ""));
            dialog.show(getSupportFragmentManager(), "Dialog");
        }else if(fromwhere.equalsIgnoreCase(CGlobalVariables.SHIW_LEAD_SUCCESS_DIALOG)){

            DhruvLeadSucessfulDialog dialog = new DhruvLeadSucessfulDialog();
            dialog.show(getSupportFragmentManager(), "Dialog");
        }
    }
}
