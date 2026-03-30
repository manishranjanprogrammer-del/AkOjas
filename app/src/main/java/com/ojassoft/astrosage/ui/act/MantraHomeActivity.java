package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created By Abhishek Raj
 */
public class MantraHomeActivity extends BaseInputActivity implements View.OnClickListener {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private TextView chalisaTV;
    private TextView mantraTV;
    private LinearLayout chalisaLL;
    private LinearLayout mantraLL;

    public MantraHomeActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_home);
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
        chalisaTV = findViewById(R.id.chalisaTV);
        mantraTV = findViewById(R.id.mantraTV);
        chalisaLL = findViewById(R.id.chalisaLL);
        mantraLL = findViewById(R.id.mantraLL);

        tvTitle.setText(getString(R.string.title_mantra));
        tvTitle.setTypeface(regularTypeface);
        chalisaTV.setTypeface(mediumTypeface);
        mantraTV.setTypeface(mediumTypeface);
    }

    private void initContext() {
        currentActivity = MantraHomeActivity.this;
    }

    private void initListener() {
        chalisaLL.setOnClickListener(this);
        mantraLL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chalisaLL: {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_OPEN_CHALISA_ACTIVITY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(this, ChalisaListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mantraLL: {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_OPEN_MANTRA_ACTIVITY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(this, ChalisaDetailsActivity.class);
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
