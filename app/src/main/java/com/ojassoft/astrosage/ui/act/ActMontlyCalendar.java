package com.ojassoft.astrosage.ui.act;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HinduCalenderData;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DepthPageTransformer;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.ChooseHinduMonth;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.MonthViewFrag;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;


/**
 * Created by ojas on ५/२/१८.
 */

public class ActMontlyCalendar extends BaseInputActivity implements DateWatcher {
    public String langCode = null;
    public int selectedYear;
    public Calendar calendar;
    public BeanPlace beanPlace;
    public String cityId;
    //BeanHoroPersonalInfo beanHoroPersonalInfo;
    public HinduCalenderData hinduCalenderData = null;
    public int selectedfragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    int currentYear;
    ImageView locationImageView, calendarImageView, settingImageView;
    ViewPagerAdapter adapter;
    Toolbar tool_barAppModule;
    private RequestQueue queue;
    private java.text.DateFormat mDateFormat;
    private HomeNavigationDrawerFragment drawerFragment;
    private ImageView toggleImageView;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

           /* BeanDateTime beanDateTime = new BeanDateTime();
            beanDateTime.setYear(year);
            beanDateTime.setMonth(monthOfYear);
            beanDateTime.setDay(dayOfMonth);
            beanDateTime.setHour(0);
            beanDateTime.setMin(0);
            beanDateTime.setSecond(0);
            birthDetailInputFragment.updateBirthDate(beanDateTime);*/
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            calendar = c;
            selectedYear = year;
            updateData(calendar.get(Calendar.MONTH));
        }
    };

    public ActMontlyCalendar() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_MONTH_VIEW;
        mDateFormat = android.text.format.DateFormat.getMediumDateFormat(ActMontlyCalendar.this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        selectedYear = currentYear;
        /*if (CUtils.getBeanHoroPersonalInfo(this) != null || CUtils.getBeanPalce(ActMontlyCalendar.this) != null) {
            beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(this);
            beanPlace = CUtils.getBeanPalce(ActMontlyCalendar.this);
        } else {
            beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        }*/
        beanPlace = CUtils.getBeanPalce(ActMontlyCalendar.this);
        if (beanPlace == null) {
            beanPlace = CUtils.getDefaultPlace();
        }
        //beanPlace = CUtils.getUserDefaultCityForVrat(ActMontlyCalendar.this);
        if (beanPlace != null) {
            cityId = String.valueOf(beanPlace.getCityId());
        } else {
            cityId = "-1";
        }


        setContentView(R.layout.monthly_calendar_layout);
        setToolbarItem();
        toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
        toggleImageView.setVisibility(View.VISIBLE);
        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), tool_barAppModule, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());


        locationImageView = (ImageView) findViewById(R.id.location_image);
        calendarImageView = (ImageView) findViewById(R.id.calendar_image);
        settingImageView = (ImageView) findViewById(R.id.setting_image);
        //settingImageView.setVisibility(View.VISIBLE);
        locationImageView.setVisibility(View.VISIBLE);
        calendarImageView.setVisibility(View.VISIBLE);
        locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchPlace(beanPlace);
            }
        });
        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
        settingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLanguageSelectDialog();
            }
        });
        checkCachedData(selectedYear, calendar.get(Calendar.MONTH), true);

    }

    public void sendToMonthlyPanchang() {
        if (drawerFragment != null) {
            drawerFragment.closeDrawer();
        }
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

    private List<Drawable> getDrawerListItemIcon() {
        try {
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.module_icons_for_panchang);
            return CUtils.convertTypedArrayToArrayList(ActMontlyCalendar.this, itemsIcon2, module_list_index_for_panchang);
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

    private void openLanguageSelectDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("CHOOSE_HINDU_MONTH");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChooseHinduMonth clfd = new ChooseHinduMonth();
        clfd.show(fm, "CHOOSE_HINDU_MONTH");
        ft.commit();
    }

    private void setToolbarItem() {
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.month_view__title));
        tvTitle.setTypeface(regularTypeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void checkCachedData(int selectedYear, int month, boolean isShowProgressbar) {

        if (cityId.equals("-1")) {
            cityId = "";
        }
        String url = CGlobalVariables.hinduCalenderApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        if (entry != null) {
            try {
                //  isCached = true;
                String saveData = new String(entry.data, "UTF-8");
                pasreData(saveData, month, url, isShowProgressbar);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            //Log.e("Volley Not Cached Data");
            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                downloadDataDetails(selectedYear, month, isShowProgressbar);
            }
        }
    }

    public void downloadDataDetails(final int selectedYear, final int month, final boolean isShowProgressbar) {
        final CustomProgressDialog pd = new CustomProgressDialog(this, regularTypeface);
        if (isShowProgressbar) {
            pd.show();
            pd.setCancelable(false);
        }

        final String url = CGlobalVariables.hinduCalenderApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                            pasreData(response, month, url, isShowProgressbar);

                        }
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActMontlyCalendar.this, ActMontlyCalendar.this
                        .getLayoutInflater(), ActMontlyCalendar.this, regularTypeface);
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
                return super.getBodyContentType();
            }

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("key", CUtils.getApplicationSignatureHashCode(ActMontlyCalendar.this));
                headers.put("language", langCode);
                headers.put("date", String.valueOf(selectedYear));
                if (beanPlace != null) {
                    String cityId = String.valueOf(beanPlace.getCityId());
                    headers.put("lid", String.valueOf(beanPlace.getCityId()));
                } else {
                    headers.put("lid", "");
                }

                return headers;
            }
        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        if (selectedYear == currentYear) {
            stringRequest.setShouldCache(true);
        } else {
            stringRequest.setShouldCache(false);
        }
        queue.add(stringRequest);
    }

    private void pasreData(String resultStr, int selectedMonth, String url, boolean isNotifyData) {
        HinduCalenderData hinduCalenderData = null;
        try {

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
                // HinduCalenderData.MonthDataDetail monthDataDetail1 = hinduCalenderData.getHinducalendar().get(selectedMonth);
                //HinduClanderAdapter hinduClanderAdapter = new HinduClanderAdapter(ActMontlyCalendar.this, monthDataDetail1);
                //recyclerView.setAdapter(hinduClanderAdapter);
                /*if (adapter == null) {
                    setupViewPager();
                } else {
                    viewPager.setCurrentItem(selectedMonth);
                    adapter.notifyDataSetChanged();
                }*/
                if (adapter == null) {
                    setupViewPager();
                } else {
                    if (!isNotifyData) {
                        //changed by abhishek
                        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                        if(currentMonth == selectedMonth){
                            calendar.set(calendar.get(Calendar.YEAR), selectedMonth, Calendar.getInstance().get(Calendar.DATE));
                        }else {
                            calendar.set(calendar.get(Calendar.YEAR), selectedMonth, 1);
                        }//end

                        //((MonthViewFrag) adapter.getFragment(selectedMonth)).updateFragment();
                        ((MonthViewFrag) adapter.getFragment(selectedfragment)).addRecyclerView();
                        ((MonthViewFrag) adapter.getFragment(selectedfragment)).setTodayData(calendar, beanPlace);
                        ((MonthViewFrag) adapter.getFragment(selectedfragment)).updateCalendarView(calendar.getTime());
                        viewPager.setCurrentItem(selectedMonth);
                    } else {
                        viewPager.setCurrentItem(selectedMonth);
                        adapter.notifyDataSetChanged();
                    }

                    //adapter.notifyDataSetChanged();

                }

            } else {

            }
        } catch (Exception e) {
            queue.getCache().remove(url);
            Log.i("error", e.getMessage() + "");
        }

    }

    private void setupViewPager() {
        String[] titles = getResources().getStringArray(R.array.MonthName_en_hi);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActMontlyCalendar.this);
        for (int i = 0; i < titles.length; i++) {

            adapter.addFragment(MonthViewFrag.newInstance(i), titles[i]);
        }

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

               /* if (tabLayout != null && adapter != null) {
                    adapter.setAlpha(position, tabLayout);
                }*/
                //selectedFragPosition = position;
                selectedfragment = position;
                //((MonthViewFrag) adapter.getFragment(selectedfragment)).updateFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //setsTabLayout();
        int month = Calendar.getInstance().get(Calendar.MONTH);
        viewPager.setCurrentItem(month);
    }

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(ActMontlyCalendar.this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        this.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    public void openCalendar() {
        if (CUtils.isUserWantsCustomCalender(ActMontlyCalendar.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showCustomDatePickerDialogAboveHoneyComb();
            } else {
                showCustomDatePickerDialog();
            }
        } else {
            // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
            if (Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25) {
                showCustomDatePickerDialogAboveHoneyComb();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showAndroidDatePicker();
            } else {
                showCustomDatePickerDialog();
            }
        }
    }

    public void showCustomDatePickerDialogAboveHoneyComb() {
        final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {

            @Override
            public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                calendar = c;
                selectedYear = year;
                updateData(calendar.get(Calendar.MONTH));
            }

        };


        final MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(ActMontlyCalendar.this, R.style.AppCompatAlertDialogStyle, myDateSetListener, selectedfragment, calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR), false);
        myDatePickerDialog.setCanceledOnTouchOutside(false);
        //mTimePicker.setTitle("hello");
        myDatePickerDialog.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
        //if device is tablet than i do not need to set DatePicker and Time Picker to be match Parent
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(myDatePickerDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            myDatePickerDialog.show();
            myDatePickerDialog.getWindow().setAttributes(lp);
        } else {
            myDatePickerDialog.show();
        }

        try {
            //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                myDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
            int divierId = myDatePickerDialog.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = myDatePickerDialog.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }
        try {
            com.ojassoft.astrosage.utils.NumberPicker date = (com.ojassoft.astrosage.utils.NumberPicker) myDatePickerDialog.findViewById(R.id.date);
            date.setVisibility(View.GONE);

        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }
        Button butOK = (Button) myDatePickerDialog.findViewById(android.R.id.button1);
        Button butCancel = (Button) myDatePickerDialog.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);

        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);

    }

    private void showCustomDatePickerDialog() {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        mDateTimePicker.setDateChangedListener(this);
        // Added by hukum to init date picker
        mDateTimePicker.initDateElements(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        mDateTimePicker.initData();
        // end
        Button setDateBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.SetDateTime);
        setDateBtn.setTypeface(regularTypeface);
        // Update demo TextViews when the "OK" button is clicked
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // birthDetailInputFragment.isDstOrNot();
                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();
            }
        });

        Button cancelBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(regularTypeface);
        // Cancel the dialog when the "Cancel" button is clicked
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked

        Button resetBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.ResetDateTime);
        resetBtn.setTypeface(regularTypeface);
        resetBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
            }
        });

        // Setup TimePicker
        // No title on the dialog window
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the dialog content view
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        // Display the dialog
        mDateTimeDialog.show();
    }

    private void showAndroidDatePicker() {
        final DatePickerDialog dg = new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, mDateSetListener,
                calendar.get(Calendar.YEAR), selectedfragment,
                calendar.get(Calendar.DATE)) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if (day != 0) {
                    View dayPicker = findViewById(day);
                    if (dayPicker != null) {
                        //Set Day view visibility Off/Gone
                        dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };


        dg.setCanceledOnTouchOutside(false);
        /*Tejinder Singh on 09-09-2016
        set Icon for the Title of DatePicker and date do not worry about api 9 do not support getDatePikcer we have
        diffrent datePiker dialog for it*/

        DatePicker datePicker = dg.getDatePicker();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month, day);

        //Due to Nought issue these line added
        if (Build.VERSION.SDK_INT > 23) {
            dg.setTitle("");
        } else {
            dg.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
            dg.setTitle(mDateFormat.format(mCalendar.getTime()));
        }


        dg.onDateChanged(dg.getDatePicker(), year, month, day);
        /*This Code for set DatePicker Width full*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dg.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dg.show();
        dg.getWindow().setAttributes(lp);
        datePicker.setScaleX(1.1f);


        //This code for API 6 date Picker Color not like holo
        CUtils.applyStyLing(dg, ActMontlyCalendar.this);

        //hide divider line in title for holo
        try {
            //this condition because setBacgroundDrawable Resource does not work well lollipop and above
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                dg.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
            //dg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND‌​);
            int divierId = dg.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = dg.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }

        //dg.show();


        Button butOK = (Button) dg.findViewById(android.R.id.button1);
        Button butCancel = (Button) dg.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);

        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);

        CUtils.setCustomDatePickerEdittext(dg);

        dg.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // DatePicker datePicker = dg.getDatePicker();

                try {
                    DatePicker datePicker = null;
                    try {
                        //Field mDatePickerField = dg.getClass().getDeclaredField("mDatePicker");
                        //mDatePickerField.setAccessible(true);
                        //datePicker = (DatePicker) mDatePickerField.get(dg);
                        datePicker = dg.getDatePicker();
                    } catch (Exception ex) {
                        //
                    }
                    // The following clear focus did the trick of saving the date while the date is put manually by the edit text.
                    datePicker.clearFocus();
                    mDateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                } catch (Exception ex) {
                    Log.i("exception>>", ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                    this.beanPlace = place;
                    CUtils.saveBeanPalce(ActMontlyCalendar.this, place);
                    cityId = String.valueOf(beanPlace.getCityId());

                    //downloadDataDetails(currentYear);

                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
              /*      checkCachedData(selectedYear);addRecyclerView()
                    setTodayData(calendar, beanPlace);
                    updateCalendarView();
*/
                    updateData(selectedfragment);
                    //CUtils.savePlacePreference(ActMontlyCalendar.this, this.beanPlace, this.beanHoroPersonalInfo);

                }
                break;
            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                }
            }
            break;
            case MODULE_ASTROSAGE_PANCHANG: {
                beanPlace = CUtils.getBeanPalce(ActMontlyCalendar.this);
                if (beanPlace == null) {
                    beanPlace = CUtils.getDefaultPlace();
                    cityId = "-1";
                }
                if (beanPlace != null) {
                    cityId = String.valueOf(beanPlace.getCityId());
                }
                updateData(selectedfragment);
                break;
            }
        }
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }

    public void updateData(int month) {
       /* if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            setupViewPager();
        }*/
        //checkCachedData(selectedYear, calendar.get(Calendar.MONTH));
        checkCachedData(selectedYear, month, true);
    }

    @Override
    public void onDateChanged(Calendar c) {
        calendar = c;
        calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        selectedYear = c.get(Calendar.YEAR);
        updateData(calendar.get(Calendar.MONTH));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
