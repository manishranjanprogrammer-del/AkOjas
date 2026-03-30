package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ChalisaPlayListAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By Abhishek Raj
 */
public class ChalisaListActivity extends BaseInputActivity implements View.OnClickListener, VolleyResponse {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private RecyclerView chalisaRecyclerView;
    private ChalisaPlayListAdapter chalisaPlayListAdapter;
    private ArrayList<ChalisaDataModel> chalisaDataModelArrayList;
    private CustomProgressDialog pd;

    public ChalisaListActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalisa_list);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        chalisaDataModelArrayList = new ArrayList<>();
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        chalisaRecyclerView = (RecyclerView) findViewById(R.id.chalisaRV);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chalisaRecyclerView.setLayoutManager(mLayoutManager);
        chalisaRecyclerView.setItemAnimator(new DefaultItemAnimator());

        tvTitle.setText(getString(R.string.text_god_chalisa));
        tvTitle.setTypeface(regularTypeface);
        getChalisaList();
    }

    private void initContext() {
        currentActivity = ChalisaListActivity.this;
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

    private void getChalisaList() {
        if (com.ojassoft.astrosage.utils.CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.getChalisaListRequest(ChalisaListActivity.this, getChalisaRequestParams(), 1);
        } else {
            showSnackbar(chalisaRecyclerView, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getChalisaRequestParams() {
        String key = CUtils.getApplicationSignatureHashCode(currentActivity);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("languagecode", String.valueOf(LANGUAGE_CODE));
        //Log.e("GetChalisaReq  params", params.toString());
        return params;
    }

    private void setChalisaAdapter() {
        chalisaPlayListAdapter = new ChalisaPlayListAdapter(currentActivity, chalisaDataModelArrayList);
        chalisaRecyclerView.setAdapter(chalisaPlayListAdapter);
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        Log.e("GetChalisaResp", "method = " + method + response);
        try {
            JSONArray respObj = new JSONArray(response);
            parseJsonDataAndUpdateUi(respObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.e("GetChalisaResp", "error = " + error.toString());
        hideProgressBar();
        if (error != null) {
            showSnackbar(chalisaRecyclerView, error.toString());
        }
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

    private void parseJsonDataAndUpdateUi(JSONArray respObj) {
        chalisaDataModelArrayList.clear();
        try {
            List<ChalisaDataModel> chalisaModels = new Gson().fromJson(respObj.toString(), new TypeToken<ArrayList<ChalisaDataModel>>() {
            }.getType());

            if (chalisaModels != null && !chalisaModels.isEmpty()) {
                chalisaDataModelArrayList.addAll(chalisaModels);
                setChalisaAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
