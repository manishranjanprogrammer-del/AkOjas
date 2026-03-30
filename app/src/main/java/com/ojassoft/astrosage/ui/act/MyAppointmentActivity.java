package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ApmtAdapter;
import com.ojassoft.astrosage.customadapters.MyApmtAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AppointmentModel;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By Abhishek Raj
 */
public class MyAppointmentActivity extends BaseInputActivity implements View.OnClickListener, VolleyResponse {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private CustomProgressDialog pd;
    private TextView apmtDateTV;
    private Button btnShowApmt;
    private Button btnNewApmt;
    private TextView todayDateTV;
    private TextView noApmtTV;
    private RecyclerView myApmtRecyclerView;
    private RecyclerView apmtRecyclerView;
    private MyApmtAdapter myApmtAdapter;
    private ApmtAdapter apmtAdapter;
    private RecyclerView.LayoutManager myApmtLayoutManager;
    private RecyclerView.LayoutManager apmtLayoutManager;
    private ArrayList<AppointmentModel> myAppointmentModelArrayList;

    private int startDateYear;
    private int startDateMonth;
    private int startDateDayOfMonth;
    private Calendar startDateCalendar;
    private String apmtDate = "";

    public MyAppointmentActivity() {
        super(R.string.app_name);
    }

    private static String getTodayDate() {
        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getScheduledDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, startDateYear);
            calendar.set(Calendar.MONTH, startDateMonth);
            calendar.set(Calendar.DAY_OF_MONTH, startDateDayOfMonth);

            Date c = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM, dd");
            String formattedDate = df.format(c);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        apmtDateTV = findViewById(R.id.apmtDateTV);
        btnShowApmt = findViewById(R.id.btnShowApmt);
        btnNewApmt = findViewById(R.id.btnNewApmt);
        todayDateTV = findViewById(R.id.todayDateTV);
        noApmtTV = findViewById(R.id.noApmtTV);
        apmtRecyclerView = findViewById(R.id.apmtRecyclerView);
        myApmtRecyclerView = findViewById(R.id.myApmtRecyclerView);

        tvTitle.setText(getString(R.string.my_apmt_title));
        tvTitle.setTypeface(regularTypeface);
        apmtDateTV.setTypeface(regularTypeface);
        btnShowApmt.setTypeface(mediumTypeface);

        todayDateTV.setTypeface(mediumTypeface);
        noApmtTV.setTypeface(regularTypeface);

        startDateCalendar = Calendar.getInstance();
        startDateYear = startDateCalendar.get(Calendar.YEAR);
        startDateMonth = startDateCalendar.get(Calendar.MONTH);
        startDateDayOfMonth = startDateCalendar.get(Calendar.DAY_OF_MONTH);

        todayDateTV.setText(getScheduledDate() + " | " + getResources().getString(R.string.scheduled));
        String todayDate = getTodayDate();
        noApmtTV.setText(getResources().getString(R.string.no_apmt_booked).replace("#", todayDate));
        myAppointmentModelArrayList = new ArrayList<>();
        setMyApmtAdapter();
        setApmtAdapter();
        getMyAppointmentDetails(todayDate);
    }

    private void initContext() {
        currentActivity = MyAppointmentActivity.this;
    }

    private void initListener() {
        apmtDateTV.setOnClickListener(this);
        btnShowApmt.setOnClickListener(this);
        btnNewApmt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowApmt: {
                apmtDate = apmtDateTV.getText().toString();
                if (!TextUtils.isEmpty(apmtDate)) {
                    getMyAppointmentDetails(apmtDate);
                } else {
                    showSnackbar(apmtDateTV, getResources().getString(R.string.please_select_apmt_date));
                }
                break;
            }
            case R.id.btnNewApmt: {
                Intent intent = new Intent(currentActivity, NewAppointmentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.apmtDateTV: {
                openStartDate();
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

    private void openStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(currentActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        startDateYear = year;
                        startDateMonth = month;
                        startDateDayOfMonth = day;
                        apmtDateTV.setText(CUtils.appendZeroOnSingleDigit(day) + "/" + CUtils.appendZeroOnSingleDigit((month + 1)) + "/" + year);
                    }
                }, startDateYear, startDateMonth, startDateDayOfMonth);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getMyAppointmentDetails(String date) {
        if (com.ojassoft.astrosage.utils.CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.getMyAppointmentRequest(MyAppointmentActivity.this, getAppointmentRequestParams(date), 1);
        } else {
            showSnackbar(btnShowApmt, getResources().getString(R.string.no_internet));
        }
    }

    private void setApmtAdapter() {
        String[] arrStartTime = getResources().getStringArray(R.array.arr_myapmt_time);
        apmtAdapter = new ApmtAdapter(currentActivity, arrStartTime, myAppointmentModelArrayList);
        apmtLayoutManager = new LinearLayoutManager(currentActivity);
        apmtRecyclerView.setLayoutManager(apmtLayoutManager);
        apmtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        apmtRecyclerView.setAdapter(apmtAdapter);
    }

    private void setMyApmtAdapter() {
        myApmtAdapter = new MyApmtAdapter(currentActivity, myAppointmentModelArrayList);
        myApmtLayoutManager = new LinearLayoutManager(currentActivity);
        myApmtRecyclerView.setLayoutManager(myApmtLayoutManager);
        myApmtRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myApmtRecyclerView.setAdapter(myApmtAdapter);
    }

    public Map<String, String> getAppointmentRequestParams(String date) {

        String key = CUtils.getApplicationSignatureHashCode(currentActivity);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("userid", CUtils.getUserName(currentActivity));
        params.put("isapi", "1");
        params.put("date", date);

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
        hideProgressBar();
        Log.e("JoinReqResponse", "method = " + method + response);
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
    }

    private void parseJsonDataAndUpdateUi(JSONObject respObj) {
        myAppointmentModelArrayList.clear();
        try {
            String response = respObj.getString("response");
            if (!TextUtils.isEmpty(response) && response.equalsIgnoreCase("1")) {
                JSONArray callDetailsArr = respObj.getJSONArray("appointmentList");
                List<AppointmentModel> appointmentModels = new Gson().fromJson(callDetailsArr.toString(), new TypeToken<ArrayList<AppointmentModel>>() {
                }.getType());
                if (appointmentModels != null && !appointmentModels.isEmpty()) {
                    myAppointmentModelArrayList.addAll(appointmentModels);
                    setMyApmtAdapter();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showHideView();
        apmtAdapter.notifyDataSetChanged();
    }

    private void showHideView() {
        if (myAppointmentModelArrayList != null && !myAppointmentModelArrayList.isEmpty()) {
            noApmtTV.setVisibility(View.GONE);
            myApmtRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noApmtTV.setVisibility(View.VISIBLE);
            myApmtRecyclerView.setVisibility(View.GONE);
        }

        todayDateTV.setText(getScheduledDate() + " | " + getResources().getString(R.string.scheduled));
        if (TextUtils.isEmpty(apmtDate)) {
            apmtDate = getTodayDate();
        }
        noApmtTV.setText(getResources().getString(R.string.no_apmt_booked).replace("#", apmtDate));
    }

}
