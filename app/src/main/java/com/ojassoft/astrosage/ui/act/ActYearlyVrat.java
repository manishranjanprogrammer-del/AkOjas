package com.ojassoft.astrosage.ui.act;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AllPanchangData;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;

import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.AmavasyaDatesFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.EkadashiFastFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.MasikShivaratriFastFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.PradoshFastFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.PurnimaFastFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.SankashtiChaturthiFastFragment;
import com.ojassoft.astrosage.ui.fragments.vratfragment.SankrantiDatesFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas-02 on 15/1/18.
 */

public class ActYearlyVrat extends BaseInputActivity {
    Toolbar toolBar_InputKundli;
    TextView titleTextView;
    Typeface typeface;
    private ViewPager viewPager;
    public ViewPagerAdapter vratViewPagerAdapter;
    private String[] pageTitles;
    TabLayout tabs_input_kundli;
    public int selectedModule;
    public BeanPlace beanPlace;
    public static int year;
    private CustomProgressDialog pd = null;
    private RequestQueue queue;
    String langCode = null;
    int currentYear;
    int visibleFragmentPosition;
    //boolean isFirstTime;
    int previousPosition = -1;
    int currentPosition = 0;
    public static String cityId = "";

    private static boolean isChecked = false;

    private HomeNavigationDrawerFragment drawerFragment;
    private ImageView toggleImageView;

    String[] urlArray = {
            CGlobalVariables.purnimaVratUrl,
            CGlobalVariables.ekadashiVratUrl,
            CGlobalVariables.pradoshVratUrl,
            CGlobalVariables.masikShivratriVratUrl,
            CGlobalVariables.sankashtiVratUrl,
            CGlobalVariables.amavasyaVratUrl,
            CGlobalVariables.sankrantiVratUrl
    };
    String[] keyArray = {
            "key1",
            "key2",
            "key3",
            "key4",
            "key5",
            "key6",
            "key7"
    };
    Fragment fragment;

    public ActYearlyVrat() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_yearly_vrat);
        beanPlace = CUtils.getBeanPalce(ActYearlyVrat.this);
        if (beanPlace != null) {
            cityId = String.valueOf(beanPlace.getCityId());

        }

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        //isChecked = CUtils.getUsersCheckedDefaultCityForVrat(this);
        //year = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear();
        year = Calendar.getInstance().get(Calendar.YEAR);
        selectedModule = CGlobalVariables.MODULE_ASTROSAGE_YEARLY_VART;
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(toolBar_InputKundli);
        toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
        toggleImageView.setVisibility(View.VISIBLE);

        titleTextView = (TextView) toolBar_InputKundli.findViewById(R.id.tvTitle);

        tabs_input_kundli = (TabLayout) findViewById(R.id.tabs);

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showToolBarTitle(
                typeface, getResources().getString(R.string.vrat_title));

        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), toolBar_InputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        viewPager = (ViewPager) findViewById(R.id.vratPager);
        viewPager.setOffscreenPageLimit(6);
        pageTitles = getResources().getStringArray(
                R.array.vrat_page_titles_list);
        setAdapterForVrat();
        //isFirstTime = CUtils.getBooleanData(ActYearlyVrat.this, keyArray[0] + LANGUAGE_CODE, true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                downloadDataWhenChangeInDate(urlArray[0], year, cityId, true);
            }
        }, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //isFirstTime = CUtils.getBooleanData(ActYearlyVrat.this, keyArray[position] + LANGUAGE_CODE, true);
                visibleFragmentPosition = position;
                /*fragment = vratViewPagerAdapter.getFragment(visibleFragmentPosition);


                if (fragment instanceof PurnimaFastFragment) {
                    ((PurnimaFastFragment) fragment).downloadData(CGlobalVariables.purnimaVratUrl, year, beanPlace.getCityId());
                } else if (fragment instanceof EkadashiFastFragment) {
                    ((EkadashiFastFragment) fragment).downloadData(CGlobalVariables.ekadashiVratUrl, year, beanPlace.getCityId());
                } else if (fragment instanceof PradoshFastFragment) {
                    ((PradoshFastFragment) fragment).downloadData(CGlobalVariables.pradoshVratUrl, year, beanPlace.getCityId());
                } else if (fragment instanceof MasikShivaratriFastFragment) {
                    ((MasikShivaratriFastFragment) fragment).downloadData(CGlobalVariables.masikShivratriVratUrl, year, beanPlace.getCityId());
                } else if (fragment instanceof SankashtiChaturthiFastFragment) {
                    ((SankashtiChaturthiFastFragment) fragment).downloadData(CGlobalVariables.sankashtiVratUrl, year, beanPlace.getCityId());
                } else if (fragment instanceof SankrantiDatesFragment) {
                    ((SankrantiDatesFragment) fragment).downloadData(CGlobalVariables.sankrantiVratUrl, year, beanPlace.getCityId());
                } else if (fragment instanceof AmavasyaDatesFragment) {
                    ((AmavasyaDatesFragment) fragment).downloadData(CGlobalVariables.amavasyaVratUrl, year, beanPlace.getCityId());
                }*/
                currentPosition = position;
                /// if (previousPosition <= currentPosition) {
                downloadDataWhenChangeInDate(urlArray[position], year, cityId, true);
                //}
                previousPosition = currentPosition;
                //if (tabs_input_kundli != null && vratViewPagerAdapter != null) {
                vratViewPagerAdapter.setAlpha(position, tabs_input_kundli);
                //}

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void sendToYearlyVart() {
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
            return CUtils.convertTypedArrayToArrayList(ActYearlyVrat.this, itemsIcon2, module_list_index_for_panchang);
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


    public void downloadDataWhenChangeInDate(String url, int selectedYear, String cityId, boolean isShowProgressbar) {
        //selectedYear = Integer.valueOf(tvDatePicker.getText().toString());
        if (currentYear != selectedYear) {
            downloadDetails(url, selectedYear, cityId, isShowProgressbar);
        } else if (currentYear == selectedYear) {
            checkCachedData(url, selectedYear, cityId, isShowProgressbar);
        }
    }

    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        titleTextView.setTypeface(typeface);
        if (titleToshow != null)
            titleTextView.setText(titleToshow);
        else

            titleTextView.setText("");

    }


    private void setAdapterForVrat() {
        vratViewPagerAdapter = getAdapter();
        viewPager.setAdapter(vratViewPagerAdapter);

        tabs_input_kundli.setupWithViewPager(viewPager);
        addCustomViewInTAbs();
    }

    public void addCustomViewInTAbs() {

        if (tabs_input_kundli != null) {

            for (int i = 0; i < tabs_input_kundli.getTabCount(); i++) {
                TabLayout.Tab tab = tabs_input_kundli.getTabAt(i);
                tab.setCustomView(vratViewPagerAdapter.getTabView(i));
            }
            vratViewPagerAdapter.setAlpha(visibleFragmentPosition, tabs_input_kundli);
        }
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

    // as punit sir said number of adapter is fixed(hardcoded)
    private ViewPagerAdapter getAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActYearlyVrat.this);
        adapter.addFragment(PurnimaFastFragment.newInstance("AllPanchangData"), pageTitles[0]);
        adapter.addFragment(EkadashiFastFragment.newInstance("EkadashiFast"), pageTitles[1]);
        adapter.addFragment(PradoshFastFragment.newInstance("PradoshFast"), pageTitles[2]);
        adapter.addFragment(MasikShivaratriFastFragment.newInstance("MasikShivaratri"), pageTitles[3]);
        adapter.addFragment(SankashtiChaturthiFastFragment.newInstance("SankashtiChaturthi"), pageTitles[4]);
        adapter.addFragment(AmavasyaDatesFragment.newInstance("Amavasya Dates"), pageTitles[5]);
        adapter.addFragment(SankrantiDatesFragment.newInstance("Sankranti Dates"), pageTitles[6]);

        return adapter;
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            int yearDiff = 0, year2 = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear();

            if (checkYearInput(year)) {

                yearDiff = year - year2;
                onSelectedInputYear(yearDiff);
            }
            updateDataAfterDateCahnge(year);

           /* android.support.v4.app.Fragment fragment = vratViewPagerAdapter.getFragment(visibleFragmentPosition);

            if (fragment instanceof PurnimaFastFragment) {
                ((PurnimaFastFragment) fragment).setDateText(year);
            } else if (fragment instanceof EkadashiFastFragment) {
                ((EkadashiFastFragment) fragment).setDateText(year);
            } else if (fragment instanceof PradoshFastFragment) {
                ((PradoshFastFragment) fragment).setDateText(year);
            } else if (fragment instanceof MasikShivaratriFastFragment) {
                ((MasikShivaratriFastFragment) fragment).setDateText(year);
            } else if (fragment instanceof SankashtiChaturthiFastFragment) {
                ((SankashtiChaturthiFastFragment) fragment).setDateText(year);
            } else if (fragment instanceof SankrantiDatesFragment) {
                ((SankrantiDatesFragment) fragment).setDateText(year);
            } else if (fragment instanceof AmavasyaDatesFragment) {
                ((AmavasyaDatesFragment) fragment).setDateText(year);
            }*/
        }
    };

    public void initMonthPicker(int year, final TextView tvDatePicker) {
        // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
        if (Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25) {
            showCustomDatePickerDialogAboveHoneyComb(year);
            return;
        }
        /*@ Tejinder Singh
         * on 2-aug-2016
         * due to problem in below lollipop phone show month and day in calendar but we do not need it
         * so going to change it*/
        try {
            final DatePickerDialog dg = new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, mDateSetListener,
                    year, 10,
                    01);

            dg.setTitle("");
            /*This Code for set DatePicker Width full*/
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dg.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dg.show();
            dg.getWindow().setAttributes(lp);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                dg.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }

            //This code for API 6 date Picker Color not like holo
            CUtils.applyStyLing(dg, this);


            Button butOK = (Button) dg.findViewById(android.R.id.button1);
            Button butCancel = (Button) dg.findViewById(android.R.id.button2);
            butOK.setText(R.string.set);
            butCancel.setText(R.string.cancel);

            butOK.setTypeface(typeface);
            butCancel.setTypeface(typeface);

            CUtils.setCustomDatePickerEdittext(dg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = dg.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }

                int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                if (monthSpinnerId != 0) {
                    View monthSpinner = dg.findViewById(monthSpinnerId);
                    if (monthSpinner != null) {
                        monthSpinner.setVisibility(View.GONE);
                    }
                }

                int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
                if (yearSpinnerId != 0) {
                    View yearSpinner = dg.findViewById(yearSpinnerId);
                    if (yearSpinner != null) {
                        yearSpinner.setVisibility(View.VISIBLE);

                    }
                }
            } else { //Older SDK versions
                dg.setTitle("");
                Field f[] = dg.getClass().getDeclaredFields();
                for (Field field : f) {
                    if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                        field.setAccessible(true);
                        Object dayPicker = null;
                        try {
                            dayPicker = field.get(dg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }

                    if (field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner")) {
                        field.setAccessible(true);
                        Object monthPicker = null;
                        try {
                            monthPicker = field.get(dg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) monthPicker).setVisibility(View.GONE);
                    }

                    if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) {
                        field.setAccessible(true);
                        Object yearPicker = null;
                        try {
                            yearPicker = field.get(dg);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) yearPicker).setVisibility(View.VISIBLE);

                    }
                }
                {
                    if (drawerFragment != null) {
                        drawerFragment.closeDrawer();
                    }

                }
            }


            dg.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // DatePicker datePicker = dg.getDatePicker();

                    try {
                        DatePicker datePicker = null;
                        try {
                            Field mDatePickerField = dg.getClass().getDeclaredField("mDatePicker");
                            mDatePickerField.setAccessible(true);
                            datePicker = (DatePicker) mDatePickerField.get(dg);
                        } catch (Exception ex) {
                            //
                        }
                        // The following clear focus did the trick of saving the date while the date is put manually by the edit text.
                        datePicker.clearFocus();
                        mDateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                    } catch (Exception ex) {
                        //
                    }
                }
            });
        } catch (Exception e) {
            //android.util.//Log.e("Excption", "initMonthPicker: " + e.getMessage());
        }
    }

    public void showCustomDatePickerDialogAboveHoneyComb(int year) {
        try {
            final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {

                @Override
                public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    int yearDiff = 0, year2 = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .getDateTime().getYear();
                    //tvDatePicker.setText(String.valueOf(year));
                    if (checkYearInput(year)) {

                        yearDiff = year - year2;
                        onSelectedInputYear(yearDiff);
                    }
                    updateDataAfterDateCahnge(year);
                }

            };


            final MyDatePickerDialog dg = new MyDatePickerDialog(this, R.style.AppCompatAlertDialogStyle, myDateSetListener, 1, 1, year, false);
            dg.setCanceledOnTouchOutside(false);
            //mTimePicker.setTitle("hello");
            // dg.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
            dg.setTitle("");
            //if device is tablet than i do not need to set DatePicker and Time Picker to be match Parent
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (!tabletSize) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dg.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dg.show();
                dg.getWindow().setAttributes(lp);
            } else {
                dg.show();
            }

            try {
                //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    dg.getWindow().setBackgroundDrawableResource(android.R.color.white);
                }
                int divierId = dg.getContext().getResources()
                        .getIdentifier("android:id/titleDivider", null, null);
                View divider = dg.findViewById(divierId);
                divider.setVisibility(View.GONE);

            } catch (Exception e) {
                //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
            }

            Button butOK = (Button) dg.findViewById(android.R.id.button1);
            Button butCancel = (Button) dg.findViewById(android.R.id.button2);
            butOK.setText(R.string.set);
            butCancel.setText(R.string.cancel);

            try {
                com.ojassoft.astrosage.utils.NumberPicker date = (com.ojassoft.astrosage.utils.NumberPicker) dg.findViewById(R.id.date);
                date.setVisibility(View.GONE);
                com.ojassoft.astrosage.utils.NumberPicker month = (com.ojassoft.astrosage.utils.NumberPicker) dg.findViewById(R.id.month);
                month.setVisibility(View.GONE);
            } catch (Exception ex) {
                //Log.i(ex.getMessage().toString());
            }

            butOK.setTypeface(regularTypeface);
            butCancel.setTypeface(regularTypeface);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }
    }

    public String getYearText(int year) {
        int iYear = year;
        int monthNumber = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth();
        StringBuilder sb = new StringBuilder();
        String monthName = getResources().getStringArray(R.array.month_short_name_list)[monthNumber];
        sb.append(monthName + " " + String.valueOf(iYear) + " " + getResources().getString(R.string.desh_character) + " ");
        sb.append(monthName + " " + String.valueOf(iYear + 1));
        //return sb.toString();
        return String.valueOf(iYear);
    }


    public boolean checkYearInput(int year) {
        int userInputYear = -1;
        int birthYear = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getDateTime().getYear();

        boolean _isValid = true;
        // THIS FUNCTION IS UPDATED ON 3-SEP-13(BIJENDRA)
        try {
            userInputYear = year;
        } catch (Exception e) {
            _isValid = true;

        }
        if (!_isValid) {
            // Toast.makeText(this, "Please enter valid year", Toast.LENGTH_SHORT).show();
            // _ebtYear.setError("Please enter valid year");
            _isValid = false;
        }

        if (_isValid) {
            int diff = userInputYear - birthYear;
            if (diff < 0) {
                // Toast.makeText(OutputMasterActivity.this, "Year can not less than birth year", Toast.LENGTH_SHORT).show();
           /*     MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, regularTypeface);
                mct.show(getResources().getString(R.string.text_year_not_less_than_birth_year));*/
                // _ebtYear.setError("Year can not less than birth year");
                _isValid = false;
            }
            if (diff > 119) {
/*
                Toast.makeText(this, "Please enter valid year", Toast.LENGTH_SHORT).show();
*/
                // _ebtYear.setError("Please enter valid year");
                _isValid = false;
            }

        }

        return _isValid;
    }
 /*   public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        this.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }*/


    private void checkCachedData(String url, int selectedYear, String cityId, boolean isShowProgressbar) {

        final String urlData = url + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;

        Cache cache = VolleySingleton.getInstance(ActYearlyVrat.this).getRequestQueue().getCache();
        final Cache.Entry entry = cache.get(urlData);
        if (entry != null) {

            if (pd == null)
                pd = new CustomProgressDialog(ActYearlyVrat.this, typeface);
            if (isShowProgressbar) {
                pd.show();
                pd.setCancelable(false);
            }

            //  isCached = true;
            String saveData = null;
            try {
                saveData = new String(entry.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //     //Log.e("Volley Cached Data" + data.toString());
            parseGsonData(saveData, urlData);
            //Log.e("Hitting astro service now");

            dismissProgress();

        } else {
            // Cached response doesn't exists. Make network call here
            //Log.e("Volley Not Cached Data");
            if (!CUtils.isConnectedWithInternet(ActYearlyVrat.this)) {
                MyCustomToast mct = new MyCustomToast(ActYearlyVrat.this, ActYearlyVrat.this
                        .getLayoutInflater(), ActYearlyVrat.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                downloadDetails(url, selectedYear, cityId, isShowProgressbar);
            }
        }
    }

    public void downloadDetails(final String url, final int selectedYear, final String cityId, boolean isShowProgressbar) {
        final String urlCheck = url + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;

        if (pd == null)
            pd = new CustomProgressDialog(ActYearlyVrat.this, typeface);

        if (isShowProgressbar) {
            pd.show();
            pd.setCancelable(false);

        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCheck,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                //        //Log.e("Simple+" + response.toString());
                                if (response != null && !response.isEmpty()) {
                                    Gson gson = new Gson();
                                    JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                                    //Log.e("Element" + element.toString());
                                    parseGsonData(response, urlCheck);
                           /* if (isFirstTime) {
                                isFirstTime = false;
                                CUtils.saveBooleanData(ActYearlyVrat.this, keyArray[visibleFragmentPosition] + LANGUAGE_CODE, isFirstTime);
                            }*/
                                }
                                dismissProgress();
                            }

                        }

                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("Error Through" + error.getMessage());
                        MyCustomToast mct = new MyCustomToast(ActYearlyVrat.this, ActYearlyVrat.this
                                .getLayoutInflater(), ActYearlyVrat.this, typeface);
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
                        dismissProgress();
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
                        headers.put("key", CUtils.getApplicationSignatureHashCode(ActYearlyVrat.this));
                        headers.put("language", langCode);
                        headers.put("date", String.valueOf(year));
                        headers.put("isapi", "1");
                        String cityIdToSend = "";
                        if (beanPlace != null) {
                            cityIdToSend = cityId;
                        }

                        headers.put("lid", cityIdToSend);
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
        }, 1000);
    }

    private void parseGsonData(String saveData, String url) {
        try {
            AllPanchangData allPanchangData;
            Gson gson = new Gson();
            allPanchangData = gson.fromJson(saveData, AllPanchangData.class);
            Fragment fragment = vratViewPagerAdapter.getFragment(visibleFragmentPosition);

            if (fragment instanceof PurnimaFastFragment) {
                ((PurnimaFastFragment) fragment).upDataList(allPanchangData);
            } else if (fragment instanceof EkadashiFastFragment) {
                ((EkadashiFastFragment) fragment).upDataList(allPanchangData);
            } else if (fragment instanceof PradoshFastFragment) {
                ((PradoshFastFragment) fragment).upDataList(allPanchangData);
            } else if (fragment instanceof MasikShivaratriFastFragment) {
                ((MasikShivaratriFastFragment) fragment).upDataList(allPanchangData);
            } else if (fragment instanceof SankashtiChaturthiFastFragment) {
                ((SankashtiChaturthiFastFragment) fragment).upDataList(allPanchangData);
            } else if (fragment instanceof SankrantiDatesFragment) {
                ((SankrantiDatesFragment) fragment).upDataList(allPanchangData);
            } else if (fragment instanceof AmavasyaDatesFragment) {
                ((AmavasyaDatesFragment) fragment).upDataList(allPanchangData);
            }
            // dismissProgress();
        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
            queue.getCache().remove(url);
        }

    }

    private void dismissProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void updateDataAfterDateCahnge(int year) {

        Fragment fragment = vratViewPagerAdapter.getFragment(visibleFragmentPosition);

        if (fragment instanceof PurnimaFastFragment) {
            ((PurnimaFastFragment) fragment).setDateText(year);
        } else if (fragment instanceof EkadashiFastFragment) {
            ((EkadashiFastFragment) fragment).setDateText(year);
        } else if (fragment instanceof PradoshFastFragment) {
            ((PradoshFastFragment) fragment).setDateText(year);
        } else if (fragment instanceof MasikShivaratriFastFragment) {
            ((MasikShivaratriFastFragment) fragment).setDateText(year);
        } else if (fragment instanceof SankashtiChaturthiFastFragment) {
            ((SankashtiChaturthiFastFragment) fragment).setDateText(year);
        } else if (fragment instanceof SankrantiDatesFragment) {
            ((SankrantiDatesFragment) fragment).setDateText(year);
        } else if (fragment instanceof AmavasyaDatesFragment) {
            ((AmavasyaDatesFragment) fragment).setDateText(year);
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
/*  public void updateLayout() {
        if(vratViewPagerAdapter == null || viewPager == null){
            setAdapterForVrat();
        }
    }*/
}
