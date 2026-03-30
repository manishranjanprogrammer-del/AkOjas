package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayoutMediator;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HinduCalenderData;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.HinduCalenderFrag;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas on १२/१/१८.
 */

public class ActHinduCalender extends BaseInputActivity {
    private TabLayout tabLayout;
    public ViewPager2 mViewPager;
    public FragmentStateAdapter adapter;
    CustomProgressDialog pd;
    public String langCode;
    int LANGUAGE_CODE;
    RequestQueue queue;
    public static int year;
    public HinduCalenderData hinduCalenderData;
    public int currentYear;
    int savedYear;
    public BeanPlace beanPlace;
    public String cityId;

    public ActHinduCalender() {
        super(R.string.app_name);
    }
    private HomeNavigationDrawerFragment drawerFragment;
    private ImageView toggleImageView;
    Toolbar tool_barAppModule;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hindu_calender_layout);
        // beanPlace = CUtils.getUserDefaultCityForVrat(ActHinduCalender.this);
        toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
        toggleImageView.setVisibility(View.VISIBLE);
        beanPlace = CUtils.getBeanPalce(ActHinduCalender.this);
        if (beanPlace != null) {
            cityId = String.valueOf(beanPlace.getCityId());
        } else {
            cityId = "";
        }

        year = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear();
        hinduCalenderData = new HinduCalenderData();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        queue = VolleySingleton.getInstance(ActHinduCalender.this).getRequestQueue();
        setToolbarItem();

        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), tool_barAppModule, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        savedYear = CUtils.getIntData(ActHinduCalender.this, "savedyear", 0);
        if (!CUtils.isConnectedWithInternet(ActHinduCalender.this)) {
            MyCustomToast mct = new MyCustomToast(ActHinduCalender.this, getLayoutInflater(), ActHinduCalender.this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            checkCachedDataOfAmavasyaFast(true,Calendar.getInstance().get(Calendar.MONTH));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    List<Integer> getDrawerListItemIndex() {
        try {
            //return CUtils.getDrawerListItemIndex(OutputMasterActivity.this, app_home_menu_item_list_index, module_list_index);
            return Arrays.asList(module_list_index_for_panchang);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    public void sendToHinduCalendar() {
        if (drawerFragment!=null) {
            drawerFragment.closeDrawer();
        }
    }

    private List<Drawable> getDrawerListItemIcon() {
        try {
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.module_icons_for_panchang);
            return CUtils.convertTypedArrayToArrayList(ActHinduCalender.this, itemsIcon2, module_list_index_for_panchang);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    private List<String> getDrawerListItem() {
        try {
            String[] menuItems2 = getResources().getStringArray(R.array.input_page_titles_list_panchang);
            return Arrays.asList(menuItems2);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    private void setToolbarItem() {
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.hindu_calender));
        tvTitle.setTypeface(regularTypeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager =  findViewById(R.id.viewpager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void setupViewPager(int changePosition) {
        String[] titles = getResources().getStringArray(R.array.MonthName);
        adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return HinduCalenderFrag.newInstance(position, year);
            }

            @Override
            public int getItemCount() {
                return titles.length;
            }

        };
        mViewPager.setAdapter(adapter);

        TabLayoutMediator tabMediator = new TabLayoutMediator(tabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView customView = (TextView) LayoutInflater.from(tabLayout.getContext()).inflate(R.layout.item_tab_header, null);
                customView.setText(titles[position]);
                // Ensure the view ignores clicks so the Tab handles them
                customView.setClickable(false);
                customView.setFocusable(false);
                tab.setCustomView(customView);
            }
        });
        tabMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        //Log.d("tab_view_issue", "setupViewPager: " + changePosition);

        mViewPager.post(() -> {
            mViewPager.setCurrentItem(changePosition,false);
        });

    }

    public void setsTabLayout() {

//        tabLayout.setupWithViewPager(mViewPager);
//
//        // Iterate over all tabs and set the custom view
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(adapter.getTabView(i));
//        }
//
//        adapter.setAlpha(selectedFragPosition, tabLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkCachedDataOfAmavasyaFast(boolean isShowProgressbar,int position) {
      /*  if (cityId.equals("-1")) {
            cityId = "";
        }*/

        String url = CGlobalVariables.hinduCalenderApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + currentYear;
        Cache cache = VolleySingleton.getInstance(ActHinduCalender.this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String saveData = new String(entry.data, "UTF-8");
                pasreData(saveData, url,position);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (!CUtils.isConnectedWithInternet(ActHinduCalender.this)) {
                MyCustomToast mct = new MyCustomToast(ActHinduCalender.this, ActHinduCalender.this
                        .getLayoutInflater(), ActHinduCalender.this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                String cityId = "";
                if (beanPlace != null) {
                    cityId = String.valueOf(beanPlace.getCityId());
                    if (cityId.equals("-1")) {
                        cityId = "";
                    }
                }
                downloadHinduCalenderDetails(currentYear, cityId, isShowProgressbar,position);
            }
        }
    }

    public void downloadHinduCalenderDetails(final int year, final String cityId, boolean isShowProgressbar,int posotion) {
        final String url = CGlobalVariables.hinduCalenderApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + currentYear;

        if(pd == null)
            pd = new CustomProgressDialog(ActHinduCalender.this, regularTypeface);

        if (isShowProgressbar) {
            pd.show();
            pd.setCancelable(false);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            pasreData(response, url,posotion);
                            if (currentYear != savedYear) {
                                CUtils.saveStringData(ActHinduCalender.this, "HinduCalenderData", response);
                                CUtils.saveIntData(ActHinduCalender.this, "savedyear", currentYear);
                            }
                        }
                        pd.dismiss();
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActHinduCalender.this, ActHinduCalender.this
                        .getLayoutInflater(), ActHinduCalender.this, regularTypeface);
                mct.show(error.getMessage());

                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                pd.dismiss();
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActHinduCalender.this));
                headers.put("language", langCode);
                headers.put("date", String.valueOf(year));
                headers.put("lid", cityId);
                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // BeanPlace beanPlace = CUtils.getUserDefaultCityForVrat(ActHinduCalender.this);
        if (year == currentYear) {
            stringRequest.setShouldCache(true);
            //beanPlace.setDefaultCity(false);
            //CUtils.saveCityAsDefaultCityForVrat(ActHinduCalender.this, beanPlace);

        } else {
            stringRequest.setShouldCache(false);
        }
        queue.add(stringRequest);
    }

    private void pasreData(String resultStr, String url,int position) {
        HinduCalenderData hinduCalenderData = null;
        try {


            //Log.d("tab_view_issue", "pasreData: reponse : " + resultStr);

            JSONObject mainJsonObject = new JSONObject(resultStr);
            String status = mainJsonObject.getString("status");
            if (status.equals("1")) {
                hinduCalenderData = new HinduCalenderData();
                HinduCalenderData.MonthDataDetail monthDataDetail;
                ArrayList<HinduCalenderData.MonthDataDetail> monthDataDetailList = new ArrayList<>();
                JSONArray hinducalendarJsonArray = mainJsonObject.getJSONArray("hinducalendarapi");
                JSONObject monthJsonObject;
                for (int i = 0; i < hinducalendarJsonArray.length(); i++) {
                    monthDataDetail = hinduCalenderData.new MonthDataDetail();
                    ArrayList<HinduCalenderData.MonthDataDetail.FestDetail> festList = new ArrayList<>();
                    HinduCalenderData.MonthDataDetail.FestDetail festDetail;
                    monthJsonObject = hinducalendarJsonArray.getJSONObject(i);
                    monthDataDetail.setMonthname(monthJsonObject.getString("monthname"));
                    JSONArray monthdataArray = monthJsonObject.getJSONArray("monthdata");
                    JSONObject festJsonObject;
                    for (int j = 0; j < monthdataArray.length(); j++) {
                        festDetail = monthDataDetail.new FestDetail();
                        festJsonObject = monthdataArray.getJSONObject(j);
                        festDetail.setFestName(festJsonObject.getString("festival_name"));
                        festDetail.setFestDate(festJsonObject.getString("festival_date"));
                        festDetail.setFestUrl(festJsonObject.getString("festival_url"));
                        festDetail.setFestImgUrl(festJsonObject.getString("festival_image_url"));
                        festDetail.setFestival_page_view(festJsonObject.getString("festival_page_view"));
                        festList.add(festDetail);
                    }
                    monthDataDetail.setMonthdata(festList);
                    monthDataDetailList.add(monthDataDetail);
                }
                hinduCalenderData.setHinducalendar(monthDataDetailList);
                this.hinduCalenderData = hinduCalenderData;

                setupViewPager(position);

            }
        } catch (Exception e) {
            queue.getCache().remove(url);
            Log.e("error", e.getMessage() + "");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                }
            }
            break;
        }
    }
    private void setUserLoginDetails(String loginName, String loginPwd) {
        drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }
}
