package com.ojassoft.astrosage.ui.act;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.model.NameHoroscopeMatchingModel;
import com.ojassoft.astrosage.model.NameSwarCombModel;
import com.ojassoft.astrosage.ui.fragments.matching.NameHororscopeOtherFragment;
import com.ojassoft.astrosage.ui.fragments.matching.NameMatchingHomeFragment;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_BOY_SWAR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_GIRL_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_GIRL_SWAR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_MATCHING_DATA;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NAME;

public class NameMatchingOutputActivity extends NameMatchingBaseActivity {

    public String boyName;
    public String girlName;
    public String boySwar;
    public String girlswar;

    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;

    private NameHoroscopeMatchingModel nameHoroscopeMatchingModel;

    public NameMatchingOutputActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_matching_output);
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
        tvTitle.setText(getString(R.string.title_name_matching_result));
        tvTitle.setTypeface(regularTypeface);
        mViewPager = findViewById(R.id.viewpager);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boyName = bundle.getString(KEY_NAME);
            girlName = bundle.getString(KEY_GIRL_NAME);
            boySwar = bundle.getString(KEY_BOY_SWAR);
            girlswar = bundle.getString(KEY_GIRL_SWAR);
            nameHoroscopeMatchingModel = parseData(bundle.getString(KEY_MATCHING_DATA));
            setupViewPager();
        }

    }

    @Override
    protected void initContext() {
        currentActivity = NameMatchingOutputActivity.this;
        context = NameMatchingOutputActivity.this;
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

    private void initListener() {

    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), currentActivity);
        Bundle bundle;

        NameMatchingHomeFragment nameMatchingHomeFragment = new NameMatchingHomeFragment();
        bundle = new Bundle();
        bundle.putParcelable("nameHoroscopeMatchingModel", nameHoroscopeMatchingModel);
        nameMatchingHomeFragment.setArguments(bundle);

        NameHororscopeOtherFragment varnaHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getVarnaInterpretation());
        bundle.putString("Title", getString(R.string.mm_varna));
        varnaHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment vasyaHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getVasyaInterpretation());
        bundle.putString("Title", getString(R.string.mm_vasya));
        vasyaHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment taraHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getTarraInterpretation());
        bundle.putString("Title", getString(R.string.mm_tara));
        taraHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment yoniHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getYoniInterpretation());
        bundle.putString("Title", getString(R.string.mm_yoni));
        yoniHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment maitriHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getRasilordInterpretation());
        bundle.putString("Title", getString(R.string.mm_maitri));
        maitriHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment ganaHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getGanaInterpretation());
        bundle.putString("Title", getString(R.string.mm_gana));
        ganaHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment bhakootHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getBhakootInterpretation());
        bundle.putString("Title", getString(R.string.mm_bhakoot));
        bhakootHororscopeMatch.setArguments(bundle);

        NameHororscopeOtherFragment nadiHororscopeMatch = new NameHororscopeOtherFragment();
        bundle = new Bundle();
        bundle.putString("interpretation", nameHoroscopeMatchingModel.getNadiInterpretation());
        bundle.putString("Title", getString(R.string.mm_nadi));
        nadiHororscopeMatch.setArguments(bundle);

        adapter.addFragment(nameMatchingHomeFragment, getString(R.string.matching));
        adapter.addFragment(varnaHororscopeMatch, getString(R.string.mm_varna));
        adapter.addFragment(vasyaHororscopeMatch, getString(R.string.mm_vasya));
        adapter.addFragment(taraHororscopeMatch, getString(R.string.mm_tara));
        adapter.addFragment(yoniHororscopeMatch, getString(R.string.mm_yoni));
        adapter.addFragment(maitriHororscopeMatch, getString(R.string.mm_maitri));
        adapter.addFragment(ganaHororscopeMatch, getString(R.string.mm_gana));
        adapter.addFragment(bhakootHororscopeMatch, getString(R.string.mm_bhakoot));
        adapter.addFragment(nadiHororscopeMatch, getString(R.string.mm_nadi));

        //adapter.addFragment(nameMatchingHomeFragment, getString(R.string.lbl_radical_num));

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


    public void handleSwarCombo(NameSwarCombModel nameSwarCombModel) {
        if (nameSwarCombModel == null) return;
        this.nameSwarCombModel = nameSwarCombModel;
        if (nameSwarCombModel == null) return;
        boySwar = nameSwarCombModel.getBoyName();
        girlswar = nameSwarCombModel.getGirlName();
        getMatchingDetails(boySwar, girlswar);
    }

    @Override
    protected void setMatchingResult(String response) {
        nameHoroscopeMatchingModel = parseData(response);
        if (nameHoroscopeMatchingModel != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
