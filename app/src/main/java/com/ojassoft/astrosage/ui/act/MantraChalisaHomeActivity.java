package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.ui.fragments.MantraAudioFrag;
import com.ojassoft.astrosage.ui.fragments.MantraVideoFrag;
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
public class MantraChalisaHomeActivity extends BaseInputActivity implements VolleyResponse {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<ChalisaDataModel> chalisaDataModelArrayList;
    private CustomProgressDialog pd;

    public MantraChalisaHomeActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_chalisa_home);
        initContext();
        initViews();
    }

    private void initViews() {
        chalisaDataModelArrayList = new ArrayList<>();
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager = findViewById(R.id.viewpager);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.title_mantra));

        tvTitle.setTypeface(regularTypeface);
        setupViewPager();
        getChalisaList();
    }

    private void initContext() {
        currentActivity = MantraChalisaHomeActivity.this;
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), currentActivity);
        adapter.addFragment(new MantraVideoFrag(), getString(R.string.t_video));
        adapter.addFragment(new MantraAudioFrag(), getString(R.string.t_audio));

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (tabLayout != null && adapter != null) {
                    adapter.setAlpha(position, tabLayout);
                }
                try {
                    String labell = CGlobalVariables.NUMEROLOGY_EVENT + "_" + CGlobalVariables.GOOGLE_ANALYTIC_NUMEROLOGY[position];
                    CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setsTabLayout();
    }

    private void setsTabLayout() {

        tabLayout.setupWithViewPager(mViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        adapter.setAlpha(0, tabLayout);
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
            CUtils.getChalisaListRequest(MantraChalisaHomeActivity.this, getChalisaRequestParams(), 1);
        } else {
            showSnackbar(tvTitle, getResources().getString(R.string.no_internet));
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
            showSnackbar(tvTitle, error.toString());
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
                updateData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        MantraVideoFrag videoFrag = (MantraVideoFrag) adapter.getItem(0);
        MantraAudioFrag audioFrag = (MantraAudioFrag) adapter.getItem(1);
        if (videoFrag != null) {
            videoFrag.updateMantraChalisa(chalisaDataModelArrayList);
        }
        if (audioFrag != null) {
            audioFrag.updateMantraChalisa(chalisaDataModelArrayList);
        }
    }
}
