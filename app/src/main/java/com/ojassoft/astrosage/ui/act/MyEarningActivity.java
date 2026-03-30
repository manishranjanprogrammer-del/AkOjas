package com.ojassoft.astrosage.ui.act;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.MyEarningBean;
import com.ojassoft.astrosage.customadapters.EarningAdapter;
import com.ojassoft.astrosage.customadapters.MyEarningViewPagerAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.VolleyResponse;
import com.ojassoft.astrosage.misc.VolleyServiceHandler;
import com.ojassoft.astrosage.ui.fragments.MyEarningFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyEarningActivity extends BaseInputActivity implements VolleyResponse {
    RecyclerView recyclerView;
    EarningAdapter earningAdapter;
    TextView tvTitle;
    public Toolbar toolBar_InputKundli;
    private RequestQueue queue;
    CustomProgressDialog pd;
    ArrayList<MyEarningBean> productEarningBeanArrayList;
    ArrayList<MyEarningBean> serviceEarningBeanArrayList;
    ViewPager viewPager;
    MyEarningViewPagerAdapter pagerAdapter;
    TabLayout tabLayout;
    LinearLayout container;

    public MyEarningActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        productEarningBeanArrayList = new ArrayList<MyEarningBean>();
        serviceEarningBeanArrayList = new ArrayList<>();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.earning_activity_layout);
        container = findViewById(R.id.container);
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.my_earning));
        tvTitle.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        enableToolBar();
        if (CUtils.isConnectedWithInternet(this)) {
            getDataFromServer();
        } else {
            CUtils.showSnakbar(container, getResources().getString(R.string.internet_is_not_working));
        }

    }

    private void setUpViewPager() {
        pagerAdapter = new MyEarningViewPagerAdapter(MyEarningActivity.this, getSupportFragmentManager(), getFraglist(), getTitles());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    ArrayList<String> getTitles() {
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.Services_Earning));
        titleList.add(getResources().getString(R.string.Products_Earning));
        return titleList;
    }

    private ArrayList<Fragment> getFraglist() {
        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(MyEarningFragment.newInstance(serviceEarningBeanArrayList, getResources().getString(R.string.services_orders), false));
        fragList.add(MyEarningFragment.newInstance(productEarningBeanArrayList, getResources().getString(R.string.product_orders), true));
        return fragList;

    }


    private void enableToolBar() {
        setSupportActionBar(toolBar_InputKundli);
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


    private void getDataFromServer() {
        showProgressBar();
        //String url = "https://dhruv.astrosage.com/dhruv/affiliate/affiliate-revenue-report.jsp";
        //String url = "http://14f1f0c4c250.ngrok.io/dhruv/affiliate/affiliate-revenue-report.jsp";
        String url = CGlobalVariables.revenueReportUrl;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsForBrandingDetail(), 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    private HashMap<String, String> getParamsForBrandingDetail() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(MyEarningActivity.this));
        params.put("userid", CUtils.getUserName(MyEarningActivity.this));
        params.put("isapi", "1");


        return params;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(MyEarningActivity.this, regularTypeface);
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
        Log.i("Response", response);
        //response = getResponse();
        hideProgressBar();
        parseResponse(response);

    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        CUtils.showSnakbar(container, error.getMessage());
    }

    private void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String resultCode = jsonObject.getString("responsecode");
            if (resultCode.equals("1")) {
                JSONArray jsonArray1 = jsonObject.getJSONArray("productRevenueList");
                JSONArray jsonArray2 = jsonObject.getJSONArray("serviceRevenueList");
                JSONObject innerJsonObject;
                MyEarningBean myEarningBean;
                for (int i = 0; i < jsonArray1.length(); i++) {
                    myEarningBean = new MyEarningBean();
                    innerJsonObject = jsonArray1.getJSONObject(i);
                    myEarningBean.setAffiliateEarnings(innerJsonObject.getString("affiliateEarnings"));
                    myEarningBean.setOrderOfMonthStr(innerJsonObject.getString("orderOfMonthStr"));
                    myEarningBean.setTotalOfOrders(innerJsonObject.getString("totalOfOrders"));
                    myEarningBean.setOrderOfMonth(innerJsonObject.getString("orderOfMonth"));
                    myEarningBean.setTotalOrders(innerJsonObject.getString("totalOrders"));
                    myEarningBean.setOrderOfYear(innerJsonObject.getString("orderOfYear"));
                    productEarningBeanArrayList.add(myEarningBean);
                }
                for (int i = 0; i < jsonArray2.length(); i++) {
                    myEarningBean = new MyEarningBean();
                    innerJsonObject = jsonArray2.getJSONObject(i);
                    myEarningBean.setAffiliateEarnings(innerJsonObject.getString("affiliateEarnings"));
                    myEarningBean.setOrderOfMonthStr(innerJsonObject.getString("orderOfMonthStr"));
                    myEarningBean.setTotalOfOrders(innerJsonObject.getString("totalOfOrders"));
                    myEarningBean.setOrderOfMonth(innerJsonObject.getString("orderOfMonth"));
                    myEarningBean.setTotalOrders(innerJsonObject.getString("totalOrders"));
                    myEarningBean.setOrderOfYear(innerJsonObject.getString("orderOfYear"));
                    serviceEarningBeanArrayList.add(myEarningBean);
                }
                //if (productEarningBeanArrayList.size() > 0) {
                //setRVAdapter();
                setUpViewPager();
                //}
            } else {
                CUtils.showSnakbar(container, jsonObject.getString("msg"));
            }

        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    private String getResponse() {
        return "{\n" +
                "    \"msg\": \"\",\n" +
                "    \"responsecode\": 1,\n" +
                "    \"productRevenueList\": [\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 600,\n" +
                "            \"monthYear\": \"March 2020\",\n" +
                "            \"totalOfOrders\": 6000,\n" +
                "            \"totalOrders\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 99.9,\n" +
                "            \"monthYear\": \"May 2019\",\n" +
                "            \"totalOfOrders\": 999,\n" +
                "            \"totalOrders\": 1\n" +
                "        }\n" +
                "    ],\n" +
                "    \"serviceRevenueList\": [\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 182,\n" +
                "            \"monthYear\": \"August 2020\",\n" +
                "            \"totalOfOrders\": 910,\n" +
                "            \"totalOrders\": 2\n" +
                "        },\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 91,\n" +
                "            \"monthYear\": \"July 2020\",\n" +
                "            \"totalOfOrders\": 455,\n" +
                "            \"totalOrders\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 130,\n" +
                "            \"monthYear\": \"February 2019\",\n" +
                "            \"totalOfOrders\": 650,\n" +
                "            \"totalOrders\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 79.8,\n" +
                "            \"monthYear\": \"July 2018\",\n" +
                "            \"totalOfOrders\": 399,\n" +
                "            \"totalOrders\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 130,\n" +
                "            \"monthYear\": \"June 2018\",\n" +
                "            \"totalOfOrders\": 650,\n" +
                "            \"totalOrders\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"affiliateEarnings\": 160,\n" +
                "            \"monthYear\": \"February 2018\",\n" +
                "            \"totalOfOrders\": 800,\n" +
                "            \"totalOrders\": 2\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
