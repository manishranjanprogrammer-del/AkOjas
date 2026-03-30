package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

/**
 * Created by ojas on ८/६/१६.
 */
public class ActDhruvDashboard extends BaseInputActivity implements View.OnClickListener {

    private Activity activity;
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    private LinearLayout newAppoinmentLL;
    private LinearLayout myAppoinmentLL;
    private LinearLayout reqVartaLL;
    private LinearLayout custFooterLL;
    private TextView newAppoinmentTV;
    private TextView myAppoinmentTV;
    private TextView reqVartaTV;
    private TextView custFooterTV;

    public ActDhruvDashboard() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhruv_dashboard);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newAppoinmentLL: {
                Intent intent = new Intent(activity, NewAppointmentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.myAppoinmentLL: {
                Intent intent = new Intent(activity, MyAppointmentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.reqVartaLL: {
                Intent intent = new Intent(activity, VartaReqJoinActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.custFooterLL: {
                Intent intent = new Intent(activity, DhruvPlanDetailsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void init() {
        activity = ActDhruvDashboard.this;
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActDhruvDashboard.this.getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(ActDhruvDashboard.this, LANGUAGE_CODE, CGlobalVariables.regular);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        newAppoinmentLL = findViewById(R.id.newAppoinmentLL);
        myAppoinmentLL = findViewById(R.id.myAppoinmentLL);
        reqVartaLL = findViewById(R.id.reqVartaLL);
        custFooterLL = findViewById(R.id.custFooterLL);
        newAppoinmentTV = findViewById(R.id.newAppoinmentTV);
        myAppoinmentTV = findViewById(R.id.myAppoinmentTV);
        reqVartaTV = findViewById(R.id.reqVartaTV);
        custFooterTV = findViewById(R.id.custFooterTV);

        newAppoinmentLL.setOnClickListener(this);
        myAppoinmentLL.setOnClickListener(this);
        reqVartaLL.setOnClickListener(this);
        custFooterLL.setOnClickListener(this);

        tvTitle.setText(getResources().getString(R.string.dhruv_dashboard));
        tvTitle.setTypeface(typeface);
        newAppoinmentTV.setTypeface(typeface);
        myAppoinmentTV.setTypeface(typeface);
        reqVartaTV.setTypeface(typeface);
        custFooterTV.setTypeface(typeface);
    }

    List<String> getDrawerListItem() {

        try {
            String[] menuItems1 = getResources().getStringArray(R.array.ask_a_question_list);
            return CUtils.getDrawerListItem(ActDhruvDashboard.this, menuItems1, null, ask_a_question_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }

    }

    List<Drawable> getDrawerListItemIcon() {

        try {
            TypedArray itemsIcon1 = getResources().obtainTypedArray(R.array.ask_a_question_list_icon);

            return CUtils.getDrawerListItemIcon(ActDhruvDashboard.this, itemsIcon1, null, ask_a_question_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }

    }

    List<Integer> getDrawerListItemIndex() {
        try {
            return CUtils.getDrawerListItemIndex(ActDhruvDashboard.this, ask_a_question_list_index, null);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }
    }
}
