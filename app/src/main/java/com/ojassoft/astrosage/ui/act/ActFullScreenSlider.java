package com.ojassoft.astrosage.ui.act;

import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.misc.SliderAdapterFullScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class ActFullScreenSlider extends BaseInputActivity {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabLayout dots;
    private SliderAdapterFullScreen adapter;
    private AstroShopItemDetails itemdetail;
    private Bundle data;
    private int position;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    public ActFullScreenSlider() {
        super(R.string.astrosage_name);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_full_screen_slider);

        findAllView();
        try {
            initView();
        } catch (Exception e) {
        }
    }


    private void findAllView() {
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        dots = (TabLayout) findViewById(R.id.dots);
        tvTitle.setText(getResources().getString(R.string.app_name));
        setSupportActionBar(tool_barAppModule);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActFullScreenSlider.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActFullScreenSlider.this.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        data = getIntent().getExtras();
        position = data.getInt("Index");
        itemdetail = (AstroShopItemDetails) data.getSerializable("Data");
        adapter = new SliderAdapterFullScreen(this, itemdetail.getLargeImageList());
        viewpager.setAdapter(adapter);
        dots.setupWithViewPager(viewpager, true);
        viewpager.setCurrentItem(position);
        if (itemdetail.getLargeImageList().size() <= 1) {
            dots.setVisibility(View.GONE);
        }
        setViewItem(itemdetail);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setViewItem(AstroShopItemDetails itemdetails) {
        String[] separated = null;
        if (itemdetails != null) {
            if (itemdetails.getPName() != null && itemdetails.getPName().contains("(")) {
                separated = itemdetails.getPName().split("\\(");
                String str = separated[0].trim();
                if (str.length() >= 20) {
                    str = itemdetails.getPName().substring(0, 20).concat("...");
                }
                tvTitle.setText(str);
            } else {
                String str = itemdetails.getPName();
                if (str.length() >= 20) {
                    str = itemdetails.getPName().substring(0, 20).concat("...");
                }
                tvTitle.setText(str);
            }
        }
    }
}
