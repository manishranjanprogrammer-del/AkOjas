package com.ojassoft.astrosage.ui.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.DhruvCountryListAdapter;
import com.ojassoft.astrosage.ui.DhruvAstrologerList;
import com.ojassoft.astrosage.varta.model.CountryBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularCityActivity extends BaseInputActivity implements View.OnClickListener {

    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    TextView tvTitle, all_cities;
    ArrayList<CountryBean> countryBeanList;
    LinearLayout inner_layout1, inner_layout2, inner_layout3, inner_layout4, inner_layout5, inner_layout6;

    public PopularCityActivity() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_city);

        setToolbar();
        all_cities = findViewById(R.id.all_cities);
        all_cities.setOnClickListener(this);
        inner_layout1 = findViewById(R.id.inner_layout1);
        inner_layout2 = findViewById(R.id.inner_layout2);
        inner_layout3 = findViewById(R.id.inner_layout3);
        inner_layout4 = findViewById(R.id.inner_layout4);
        inner_layout5 = findViewById(R.id.inner_layout5);
        inner_layout6 = findViewById(R.id.inner_layout6);
        parsePopularCities();

        addLayout(inner_layout1,0,4);
        addLayout(inner_layout2,4,8);
        addLayout(inner_layout3,8,12);
        addLayout(inner_layout4,12,16);
        addLayout(inner_layout5,16,20);
        addLayout(inner_layout6,20,24);
    }

    public void setToolbar(){
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.directory_listing));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addLayout(LinearLayout parentLayout, int start, int end) {
        for(int i=start; i<end; i++) {
            View layout = LayoutInflater.from(this).inflate(R.layout.txt_layout, parentLayout, false);
            final TextView upper_txt = (TextView) layout.findViewById(R.id.upper_txt);
            final TextView lower_txt = (TextView) layout.findViewById(R.id.lower_txt);
            if(i == end-1) {
                upper_txt.setVisibility(View.GONE);
                lower_txt.setVisibility(View.VISIBLE);
                if(countryBeanList != null && countryBeanList.size()>0) {
                    lower_txt.setTag(i);
                    lower_txt.setText(countryBeanList.get(i).getCountryName());
                }
            }else{
                upper_txt.setVisibility(View.VISIBLE);
                lower_txt.setVisibility(View.GONE);
                if(countryBeanList != null && countryBeanList.size()>0) {
                    upper_txt.setTag(i);
                    upper_txt.setText(countryBeanList.get(i).getCountryName());
                }
            }

            upper_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(upper_txt.getTag().toString());
                    CountryBean countryBean = countryBeanList.get(pos);
                    if(countryBean != null) {
                        Log.e("DATAA COUNTRY ", "" + countryBean.getCountryName());
                        Intent intent = new Intent(PopularCityActivity.this, DhruvAstrologerList.class);
                        intent.putExtra("COUNTRY_ID", countryBean.getCountryCode());
                        intent.putExtra("COUNTRY_NAME", countryBean.getCountryName());
                        startActivity(intent);
                    }
                }
            });

            lower_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(lower_txt.getTag().toString());
                    CountryBean countryBean = countryBeanList.get(pos);
                    if(countryBean != null) {
                        Log.e("DATAA COUNTRY ", "" + countryBean.getCountryName());
                        Intent intent = new Intent(PopularCityActivity.this, DhruvAstrologerList.class);
                        intent.putExtra("COUNTRY_ID", countryBean.getCountryCode());
                        intent.putExtra("COUNTRY_NAME", countryBean.getCountryName());
                        startActivity(intent);
                    }
                }
            });
            parentLayout.addView(layout);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_cities:
                Intent intent = new Intent(PopularCityActivity.this, DhruvAstrologerByCity.class);
                startActivity(intent);
                break;
        }
    }

    public void parsePopularCities(){

        String response="[\n" +
                "  {\n" +
                "    \"CityId\": \"6\",\n" +
                "    \"CityName\": \"Agra\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"28\",\n" +
                "    \"CityName\": \"Ahmedabad\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"36\",\n" +
                "    \"CityName\": \"Ajmer\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"50\",\n" +
                "    \"CityName\": \"Allahabad\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"346\",\n" +
                "    \"CityName\": \"Coimbatore\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"613\",\n" +
                "    \"CityName\": \"Gurgaon\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"676\",\n" +
                "    \"CityName\": \"Hyderabad\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"683\",\n" +
                "    \"CityName\": \"Indore\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"707\",\n" +
                "    \"CityName\": \"Jaipur\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"769\",\n" +
                "    \"CityName\": \"Kolkata\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1123\",\n" +
                "    \"CityName\": \"Mysore\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1146\",\n" +
                "    \"CityName\": \"Nagpur\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1184\",\n" +
                "    \"CityName\": \"Nashik\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1216\",\n" +
                "    \"CityName\": \"Noida\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1351\",\n" +
                "    \"CityName\": \"Pune\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1633\",\n" +
                "    \"CityName\": \"Surat\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1854\",\n" +
                "    \"CityName\": \"Delhi\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1863\",\n" +
                "    \"CityName\": \"Chennai\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1864\",\n" +
                "    \"CityName\": \"Ghaziabad\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1868\",\n" +
                "    \"CityName\": \"Mumbai\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1870\",\n" +
                "    \"CityName\": \"Bangalore\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"1871\",\n" +
                "    \"CityName\": \"Faridabad\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"2302\",\n" +
                "    \"CityName\": \"Nanital\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"CityId\": \"2317\",\n" +
                "    \"CityName\": \"Chandigarh\"\n" +
                "  }\n" +
                "]";
        try {
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray != null && jsonArray.length()>0){
                countryBeanList = new ArrayList<CountryBean>();
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CountryBean countryBean = new CountryBean();
                    countryBean.setCountryCode(jsonObject.getString("CityId"));
                    countryBean.setCountryName(jsonObject.getString("CityName"));
                    countryBeanList.add(countryBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}