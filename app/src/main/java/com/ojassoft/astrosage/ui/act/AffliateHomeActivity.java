package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class AffliateHomeActivity extends BaseInputActivity implements View.OnClickListener {
    private LinearLayout myProductServicesLL;
    private LinearLayout myEarningLL;
    private TextView myProductServicesTV;
    private TextView myEarningTV;
    private Typeface typeface;
    private Toolbar toolbar;
    TabLayout tabLayout;
    TextView tvTitle;

    public AffliateHomeActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affliate_home_layout);
        toolbar = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.affliated_program));
        tvTitle.setVisibility(View.VISIBLE);
        init();
        enableToolBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_product_sevices_ll: {
                Intent intent = new Intent(AffliateHomeActivity.this, MyServicesAndProductActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.my_earning_ll: {
                Intent intent = new Intent(AffliateHomeActivity.this, MyEarningActivity.class);
                startActivity(intent);
                break;
            }

        }
    }

    private void init() {

        LANGUAGE_CODE = ((AstrosageKundliApplication) AffliateHomeActivity.this.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(AffliateHomeActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);


        myProductServicesLL = findViewById(R.id.my_product_sevices_ll);
        myEarningLL = findViewById(R.id.my_earning_ll);


        myProductServicesTV = findViewById(R.id.my_product_sevices_tv);
        myEarningTV = findViewById(R.id.my_earning_tv);


        myProductServicesLL.setOnClickListener(this);
        myEarningLL.setOnClickListener(this);


        myProductServicesTV.setTypeface(typeface);
        myEarningTV.setTypeface(typeface);

    }

    private void enableToolBar() {
        setSupportActionBar(toolbar);
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
}

