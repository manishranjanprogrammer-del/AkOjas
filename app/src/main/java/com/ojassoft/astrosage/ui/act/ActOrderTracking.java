package com.ojassoft.astrosage.ui.act;

import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.TrackingAdapter;
import com.ojassoft.astrosage.model.TrackModal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActOrderTracking extends BaseInputActivity {

    private RecyclerView recyclerView;
    private String trackingData="";
    private Toolbar tool_barAppModule;
    private TextView tvTitle;
    private TabLayout tabLayout;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeface;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<TrackModal> list;
    public RecyclerView.Adapter adapter;

    public ActOrderTracking() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_order_tracking);
        trackingData=getIntent().getStringExtra("DATA");

        init();
    }

    private void init()
    {

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        recyclerView=(RecyclerView) findViewById(R.id.my_recycler_view);
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.shipping_details));
        tvTitle.setTypeface(typeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        if(trackingData!=null&& !trackingData.isEmpty())
        {
        parseDataAndSetAdapter();

        }
    }

    private void parseDataAndSetAdapter() {
        try {
            JSONObject obj = new JSONObject(trackingData);
            String status=obj.getString("status");
            if(status.equalsIgnoreCase("Success"))
            {
                JSONObject shipObject=obj.getJSONObject("response");
                JSONArray array=shipObject.getJSONArray("scan");
                if(array.length()>=0)
                {

                    list = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<TrackModal>>() {
                    }.getType());
                    adapter=new TrackingAdapter(this,list);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(mLayoutManager);
                }

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
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


}
