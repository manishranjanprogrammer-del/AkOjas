package com.ojassoft.astrosage.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.DhruvAstrologerListModel;
import com.ojassoft.astrosage.customadapters.DhruvAstrologerListAdapter;
import com.ojassoft.astrosage.interfaces.GetConsultaionInterface;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.DhruvAstrologerByCity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.DhruvLeadSucessfulDialog;
import com.ojassoft.astrosage.ui.fragments.FreeConsultationDialog;
import com.ojassoft.astrosage.ui.fragments.FreeConsultationOTPDialog;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DhruvAstrologerList extends BaseInputActivity implements VolleyResponse , GetConsultaionInterface {

    RecyclerView recyclerview;
    CustomProgressDialog pd;
    String url = CGlobalVariables.DHRUV_CITY_ASTROLOGER_LIST_URL;
    String cityID="", cityName="";
    ArrayList<DhruvAstrologerListModel> dhruvAstrologerListModelArrayList;
    DhruvAstrologerListAdapter dhruvAstrologerListAdapter;
    Typeface typeFace;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    TextView tvTitle;

    public DhruvAstrologerList() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhruv_astrologer_list);
        typeFace = CUtils.getRobotoFont(getApplicationContext(), 0, CGlobalVariables.regular);
        setToolbar();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(DhruvAstrologerList.this, LinearLayoutManager.VERTICAL, false));

        if(getIntent().getExtras() != null){
            cityID = getIntent().getStringExtra("COUNTRY_ID");
            cityName = getIntent().getStringExtra("COUNTRY_NAME");
        }

        if (!CUtils.isConnectedWithInternet(DhruvAstrologerList.this)) {
            showCustomisedToastMessage(getResources().getString(R.string.no_internet));
        } else {
            showProgressBar();
            CUtils.vollyPostRequestForCity(DhruvAstrologerList.this, url,
                    getParams(cityID), 0);
        }
    }

    public void setToolbar(){
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.directory_listing));
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Map<String, String> getParams(String cityid) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(DhruvAstrologerList.this));
        params.put("cityid",cityid);
        //Log.e("DATAA params ", params.toString());
        return params;
    }

    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(DhruvAstrologerList.this);
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
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponse(String response, int method) {

        hideProgressBar();
        Log.e("DATAA params ", response.toString());

        try {
            if (response != null && response.length() > 0) {

                if(!(response.length() == 1)) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<DhruvAstrologerListModel>>() {
                    }.getType();
                    dhruvAstrologerListModelArrayList = gson.fromJson(response, type);

                    if (dhruvAstrologerListModelArrayList != null && dhruvAstrologerListModelArrayList.size() > 0) {
                        dhruvAstrologerListAdapter = new DhruvAstrologerListAdapter(DhruvAstrologerList.this, dhruvAstrologerListModelArrayList, DhruvAstrologerList.this);
                        recyclerview.setAdapter(dhruvAstrologerListAdapter);

                    } else {
                        showCustomisedToastMessage(getResources().getString(R.string.no_data_available));
                        finish();
                    }
                }else{
                    showCustomisedToastMessage(getResources().getString(R.string.no_data_available));
                    finish();
                }
            } else {
                showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showCustomisedToastMessage(String msg) {
        MyCustomToast mct = new MyCustomToast(DhruvAstrologerList.this, getLayoutInflater(), DhruvAstrologerList.this, typeFace);
        mct.show(msg);
    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void getConsultationClick(String astrologerProfileID) {
        try {
            FreeConsultationDialog dialog = new FreeConsultationDialog(astrologerProfileID);
            dialog.show(getSupportFragmentManager(), "Dialog");
        } catch (Exception e) {

        }
    }

    public void showOtpDialog(String fromwhere){
        Log.e("DATAA ", "showOtpDialog "+fromwhere);
        if(fromwhere.equalsIgnoreCase(CGlobalVariables.SHIW_OTP_DIALOG)) {
            FreeConsultationOTPDialog dialog = new FreeConsultationOTPDialog(
                    CUtils.getStringData(DhruvAstrologerList.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_USERNAME, ""),
                    CUtils.getStringData(DhruvAstrologerList.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_EMAILID, ""),
                    CUtils.getStringData(DhruvAstrologerList.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_COUNTRY_CODE, ""),
                    CUtils.getStringData(DhruvAstrologerList.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_MOBILENO, ""),
                    CUtils.getStringData(DhruvAstrologerList.this, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_ASTROLOGERID, ""));
            dialog.show(getSupportFragmentManager(), "Dialog");
        }else if(fromwhere.equalsIgnoreCase(CGlobalVariables.SHIW_LEAD_SUCCESS_DIALOG)){

            DhruvLeadSucessfulDialog dialog = new DhruvLeadSucessfulDialog();
            dialog.show(getSupportFragmentManager(), "Dialog");
        }
    }


}