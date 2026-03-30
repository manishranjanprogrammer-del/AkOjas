package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.ISearchBirthDetailsFragment;
import com.ojassoft.astrosage.ui.fragments.SearchBirthDetailsFragment;

public class MychartActivity extends BaseInputActivity implements
        ISearchBirthDetailsFragment {
    public MychartActivity() {
        super(R.string.app_name);
    }

    TabLayout tabs;
     Toolbar toolBar;
    private TextView tvToolBarTitle;
    ImageView imgBackButton;
    View personKundaliDetail;
    public SearchBirthDetailsFragment searchBirthDetailsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mychart_layout);

        // FrameLayout
        // frameLayout=(FrameLayout)findViewById(R.id.search_fragment_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        searchBirthDetailsFragment = new SearchBirthDetailsFragment();
        fragmentTransaction.add(R.id.search_fragment_container,
                searchBirthDetailsFragment, "HELLO");
        fragmentTransaction.commit();


        //personKundaliDetail = ;
       // ((SearchBirthDetailsFragment) searchBirthDetailsFragment).personKundaliDetail.setVisibility(View.VISIBLE);

        // showPersonalKundliData();
        toolBar = (Toolbar) findViewById(R.id.tool_barAppModule);
        toolBar.setNavigationIcon(R.drawable.ic_back_arrow);
        tvToolBarTitle = (TextView) toolBar.findViewById(R.id.tvTitle);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected void onResume() {
        super.onResume();
        showToolBarTitle(mediumTypeface, this.getResources()
                .getString(R.string.my_kundli));
        //searchBirthDetailsFragment.setVisibalityOfCreateKundliButton();
    }

    @Override
    public void selectedKundli(BeanHoroPersonalInfo beanHoroPersonalInfo,int position) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            Intent refresh = new Intent(this, MychartActivity.class);
            startActivity(refresh);
            this.finish();
        }catch (Exception ex){
            //Log.i("Exception",ex.getMessage().toString());
        }
    }

    @Override
    public void oneChartDeleted(long kundliId, boolean isOnlineChart) {
        // TODO Auto-generated method stub

    }

/* cmntd by Neeraj 04/04/16
    public void showPersonalKundliData() {


        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                .getCustomObject(MychartActivity.this);
        if (beanHoroPersonalInfo != null) {
            setTextonFields(beanHoroPersonalInfo.getName(),
                    beanHoroPersonalInfo.getDateTime().getDay() + "/"
                            + beanHoroPersonalInfo.getDateTime().getMonth() + 1
                            + "/"
                            + beanHoroPersonalInfo.getDateTime().getYear(),
                    beanHoroPersonalInfo.getDateTime().getHour() + ":"
                            + beanHoroPersonalInfo.getDateTime().getMin() + ":"
                            + beanHoroPersonalInfo.getDateTime().getSecond(),
                    beanHoroPersonalInfo.getPlace().getCityName());
        } else {
            setTextonFields("", "", "", "");
        }

    }

   private void setTextonFields(String name, String dob, String time,
                                 String palace) {
       TextView nameValueTextView = (TextView) findViewById(R.id.name_value_textview);

       TextView dobValueTextView = (TextView)
                findViewById(R.id.dob_value_textview);
       TextView timeValueTextView =
       (TextView) findViewById(R.id.time_value_textview);
       TextView
                palaceValueTextView = (TextView)
                findViewById(R.id.palace_value_textview);

       if (name.equals("")) {
           nameValueTextView.setText("None");
       } else {
           nameValueTextView.setText("Name : " + name + "\n" + "" + "DOB   : " + dob + "\n" + "" + "Time : " + time + "\n" + "" + "Place : "
                   + palace);
       }
   }*/

		/*
         * dobValueTextView.setText(dob); timeValueTextView.setText(time);
		 * palaceValueTextView.setText(palace);
		 */

    private void finishActivity() {
        this.finish();
    }

    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        tvToolBarTitle.setTypeface(typeface);
        if (titleToshow != null)
            tvToolBarTitle.setText(titleToshow);
        else
            // ADDED BY BIJENDRA ON 17-06-14
            tvToolBarTitle.setText("");

    }
}
