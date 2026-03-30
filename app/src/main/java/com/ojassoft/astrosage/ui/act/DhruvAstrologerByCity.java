package com.ojassoft.astrosage.ui.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.ojassoft.astrosage.R;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.customadapters.DhruvCountryListAdapter;
import com.ojassoft.astrosage.interfaces.DhruvCityClickListener;
import com.ojassoft.astrosage.ui.DhruvAstrologerList;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.FragPersonalDetailsOMF;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

public class DhruvAstrologerByCity extends BaseInputActivity implements VolleyResponse, DhruvCityClickListener {

    EditText inputSearch;
    RecyclerView recyclerView;
    String strFilter="";
    DhruvCountryListAdapter adapter;
    ArrayList<CountryBean> countryBeanList = new ArrayList<>();
    String url = CGlobalVariables.DHRUV_CITY_URL;
    CustomProgressDialog pd = null;
    Typeface typeFace;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    TextView tvTitle;

    public DhruvAstrologerByCity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dhruv_astrologer_search_layout);

        inputSearch = (EditText) findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        typeFace = CUtils.getRobotoFont(getApplicationContext(), 0, CGlobalVariables.regular);
        setToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(DhruvAstrologerByCity.this, LinearLayoutManager.VERTICAL, false));

        if (!CUtils.isConnectedWithInternet(DhruvAstrologerByCity.this)) {
            showCustomisedToastMessage(getResources().getString(R.string.no_internet));
        } else {
            showProgressBar();
            CUtils.vollyPostRequestForCity(DhruvAstrologerByCity.this, url,
                    getParamsNew(), 0);
        }

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter != null) {
                    adapter.filter(text);
                }
            }
        });
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

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Map<String, String> getParamsNew() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", CUtils.getApplicationSignatureHashCode(DhruvAstrologerByCity.this));
        Log.e("DATAA params ", params.toString());
        return params;
    }

    @Override
    public void onResponse(String response, int method) {

        //Log.e("DATAA response ", response);
        hideProgressBar();
        if(response != null && response.length()>0){
            try {
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray != null && jsonArray.length()>0){
                    countryBeanList.clear();
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CountryBean countryBean = new CountryBean();
                        countryBean.setCountryCode(jsonObject.getString("CityId"));
                        countryBean.setCountryName(jsonObject.getString("CityName"));
                        countryBeanList.add(countryBean);
                    }
                    adapter = new DhruvCountryListAdapter(DhruvAstrologerByCity.this, countryBeanList, DhruvAstrologerByCity.this);
                    recyclerView.setAdapter(adapter);

                }else{
                    showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
        }
    }

    private void showCustomisedToastMessage(String msg) {
        MyCustomToast mct = new MyCustomToast(DhruvAstrologerByCity.this, getLayoutInflater(), DhruvAstrologerByCity.this, typeFace);
        mct.show(msg);
    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void onItemClick(CountryBean countryBean) {
        if(countryBean != null) {
            Log.e("DATAA COUNTRY ", "" + countryBean.getCountryName());
            Intent intent = new Intent(DhruvAstrologerByCity.this, DhruvAstrologerList.class);
            intent.putExtra("COUNTRY_ID", countryBean.getCountryCode());
            intent.putExtra("COUNTRY_NAME", countryBean.getCountryName());
            startActivity(intent);
        }
    }

    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(DhruvAstrologerByCity.this);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}