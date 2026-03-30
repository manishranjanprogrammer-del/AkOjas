package com.ojassoft.astrosage.ui.act;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.ojassoft.astrosage.beans.OrderDetailBean;
import com.ojassoft.astrosage.customadapters.OrderDetailAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.VolleyResponse;
import com.ojassoft.astrosage.misc.VolleyServiceHandler;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailActivity extends BaseInputActivity implements VolleyResponse {
    RecyclerView recyclerView;
    OrderDetailAdapter orderDetailAdapter;

    TextView tvTitle;
    public Toolbar toolBar_InputKundli;
    private TabLayout tabs_input_kundli;
    boolean isProduct;
    String month;
    String year;
    RequestQueue queue;
    CustomProgressDialog pd;
    LinearLayout container;

    public OrderDetailActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        Bundle bundle = getIntent().getExtras();
        isProduct = bundle.getBoolean("isProduct");
        month = bundle.getString("month");
        year = bundle.getString("year");
        setContentView(R.layout.order_detail_activity_layout);
        container = findViewById(R.id.container);
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        tabs_input_kundli = findViewById(R.id.tabs);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.order_details));
        tvTitle.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerview);
        //orderDetailAdapter = new OrderDetailAdapter(this, getOrderDetails());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(orderDetailAdapter);
        enableToolBar();
        if (CUtils.isConnectedWithInternet(this)) {
            getDataFromServer();
        } else {
            CUtils.showSnakbar(container, getResources().getString(R.string.internet_is_not_working));
        }

    }

    private ArrayList<OrderDetailBean> getOrderDetails() {

        return parseData(getresponse());
    }


    private ArrayList<OrderDetailBean> parseData(String resonse) {
        ArrayList<OrderDetailBean> orderDetailBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(resonse);
            String responseCode = jsonObject.getString("responsecode");
            if (responseCode.equals("1")) {
                JSONArray jsonArray;
                if (isProduct) {
                    jsonArray = jsonObject.getJSONArray("productOrderList");
                } else {
                    jsonArray = jsonObject.getJSONArray("serviceOrderList");
                }

                JSONObject innerJsonObject;
                OrderDetailBean orderDetailBean;
                for (int i = 0; i < jsonArray.length(); i++) {
                    innerJsonObject = jsonArray.getJSONObject(i);
                    orderDetailBean = new OrderDetailBean();
                    orderDetailBean.setOrderdate(innerJsonObject.getString("orderdate"));
                    orderDetailBean.setOrderName(innerJsonObject.getString("orderName"));
                    orderDetailBean.setOrderPrice(innerJsonObject.getString("orderPrice"));
                    orderDetailBean.setPartnerIdEarning(innerJsonObject.getString("partnerIdEarning"));
                    orderDetailBeanList.add(orderDetailBean);
                }
                orderDetailAdapter = new OrderDetailAdapter(this, orderDetailBeanList);
                recyclerView.setAdapter(orderDetailAdapter);
            } else {
                CUtils.showSnakbar(container, jsonObject.getString("msg"));
            }


        } catch (Exception e) {

        }
        return orderDetailBeanList;
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

    private String getresponse() {
        return "{\n" +
                "\t\"affiliatePartnerId\": \"D0001\",\n" +
                "\t\"msg\": \"\",\n" +
                "\t\"responsecode\": 1,\n" +
                "\t\"productOrderList\": [{\n" +
                "\t\t\t\"partnerIdEarning\": 600,\n" +
                "\t\t\t\"orderPrice\": 6000,\n" +
                "\t\t\t\"orderdate\": \"30/03/20\",\n" +
                "\t\t\t\"orderName\": \"Raj Yog\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"partnerIdEarning\": 600,\n" +
                "\t\t\t\"orderPrice\": 6000,\n" +
                "\t\t\t\"orderdate\": \"30/03/20\",\n" +
                "\t\t\t\"orderName\": \"Year Book\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"serviceOrderList\": [{\n" +
                "\t\t\t\"partnerIdEarning\": 91,\n" +
                "\t\t\t\"orderPrice\": 455,\n" +
                "\t\t\t\"orderdate\": \"11/08/20\",\n" +
                "\t\t\t\"orderName\": \"Ask a Question\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"partnerIdEarning\": 91,\n" +
                "\t\t\t\"orderPrice\": 455,\n" +
                "\t\t\t\"orderdate\": \"05/08/20\",\n" +
                "\t\t\t\"orderName\": \"Ask a Question\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"productOrderListTotal\": 600,\n" +
                "\t\"serviceOrderListTotal\": 182\n" +
                "}";
    }


    private void getDataFromServer() {
        showProgressBar();
        //String url = "https://dhruv.astrosage.com/dhruv/affiliate/affiliate-revenue-report.jsp";
        //String url = "http://14f1f0c4c250.ngrok.io/dhruv/affiliate/affiliate-order-detail.jsp";
        String url = CGlobalVariables.orderDetailUrl;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsForBrandingDetail(), 1).getMyStringRequest();
        queue.add(stringRequest);
    }

    private HashMap<String, String> getParamsForBrandingDetail() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(OrderDetailActivity.this));
        params.put("userid", CUtils.getUserName(OrderDetailActivity.this));
        params.put("isapi", "1");
        params.put("month", month);
        params.put("year", year);

        return params;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(OrderDetailActivity.this, regularTypeface);
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
        Log.i("Response", response);
        //response = getResponse();
        hideProgressBar();
        parseData(response);

    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        CUtils.showSnakbar(container, error.getMessage());
        //parseResponse(getResponse());
    }


}
