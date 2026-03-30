package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created By Abhishek Raj
 */
public class ApmtBookingConfirmationActivity extends BaseInputActivity implements View.OnClickListener {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private TextView lblMsgTV;
    private Button btnShowApmt;

    public ApmtBookingConfirmationActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apmt_booking_confirmation);
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
        lblMsgTV = findViewById(R.id.lblMsgTV);
        btnShowApmt = findViewById(R.id.btnShowApmt);

        tvTitle.setText(getString(R.string.apmt_details));
        tvTitle.setTypeface(regularTypeface);
        lblMsgTV.setTypeface(regularTypeface);
        btnShowApmt.setTypeface(mediumTypeface);
    }

    private void initContext() {
        currentActivity = ApmtBookingConfirmationActivity.this;
    }

    private void initListener() {
        btnShowApmt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowApmt: {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.OPEN_MY_APPOINTMENT_FROM_BOOKING_CONFIRMATION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(this, MyAppointmentActivity.class);
                startActivity(intent);
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
}
