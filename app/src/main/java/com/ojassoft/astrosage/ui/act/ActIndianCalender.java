package com.ojassoft.astrosage.ui.act;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
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
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.IndianCalenderAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.AllPanchangData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas-02 on 24/1/18.
 */

public class ActIndianCalender extends BaseInputActivity implements OnRefreshListener {
    //BeanHoroPersonalInfo beanHoroPersonalInfo;
    public BeanPlace beanPlace;
    public String cityId;
    Toolbar toolBar_InputKundli;
    TextView titleTextView;
    Typeface typeface;
    TabLayout tabs_input_kundli;
    TextView tvDatePicker;
    int selectedYear;
    int currentYear;
    String calTitle = null;
    TextView txtPlaceName, txtPlaceDetail;
    private RecyclerView icRecyclerView;
    private CustomProgressDialog pd = null;
    private String langCode = null;
    private RequestQueue queue;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private int year;
    private HomeNavigationDrawerFragment drawerFragment;
    private ImageView toggleImageView;
    private SwipeRefreshLayout swipeView;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;
    // private static boolean isChecked = false;
    // boolean isFirstTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            tvDatePicker.setText(String.valueOf(year));
            selectedYear = year;
            /*set kundli date in newKundliSelectedDate*/
            if (tvDatePicker.toString().length() > 0) {
                CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
            }
            showToolBarTitle(typeface, calTitle + " " + String.valueOf(selectedYear));
            checkCachedData(year, cityId, true);
        }
    };

    public ActIndianCalender() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_INDIAN_CALENDAR;
        setContentView(R.layout.act_indian_calender);
        // isChecked = CUtils.getUsersCheckedDefaultCityForVrat(this);


        ;
        //beanPlace = CUtils.getUserDefaultCityForVrat(ActIndianCalender.this);

        year = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear();
       /* beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        if (CUtils.getBeanHoroPersonalInfo(this) != null || CUtils.getBeanPalce(ActIndianCalender.this) != null) {
            beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(this);

        } else {
            beanHoroPersonalInfo = new BeanHoroPersonalInfo();
            cityId = "";
        }*/
        beanPlace = CUtils.getBeanPalce(ActIndianCalender.this);
        if (beanPlace == null) {
            beanPlace = CUtils.getDefaultPlace();
        }
        cityId = String.valueOf(beanPlace.getCityId());
        initSwipeRefreshLayout();

        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        selectedYear = currentYear;
        initTimePickerView();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(toolBar_InputKundli);
        toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
        toggleImageView.setVisibility(View.VISIBLE);
        titleTextView = (TextView) findViewById(R.id.tvTitle);
        txtPlaceName = (TextView) findViewById(R.id.textViewPlaceName);
        txtPlaceDetail = (TextView) findViewById(R.id.textViewPlaceDetails);
        tabs_input_kundli = (TabLayout) findViewById(R.id.tabs);
        tabs_input_kundli.setVisibility(View.GONE);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        //isFirstTime = CUtils.getBooleanData(ActIndianCalender.this, langCode, true);

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topAdImage = (NetworkImageView)findViewById(R.id.topAdImage);
        llCustomAdv = (LinearLayout)findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(ActIndianCalender.this, false, (ActIndianCalender.this).regularTypeface, "AKFES"));
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), toolBar_InputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        calTitle = getResources().getString(R.string.indian_calender_title);

        showToolBarTitle(typeface, calTitle + " " + String.valueOf(selectedYear));
        showToolBarTitle(typeface, calTitle + " " + String.valueOf(selectedYear));


        icRecyclerView = (RecyclerView) findViewById(R.id.my_indian_calender_recycler_view);
        addRecyclerView();
        checkCachedData(currentYear, cityId, true);
        Button nextButton = (Button) findViewById(R.id.btnNextDate);
        Button previousButton = (Button) findViewById(R.id.btnPreviousDate);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDatePicker.setText(getYearText(++selectedYear));

                /*set kundli date in newKundliSelectedDate*/
                if (tvDatePicker.toString().length() > 0) {
                    CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                }
                setPlaceDataView();

                showToolBarTitle(typeface, calTitle + " " + String.valueOf(selectedYear));

                downloadDataWhenChangeInDate(selectedYear, cityId, true);
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDatePicker.setText(getYearText(--selectedYear));

                /*set kundli date in newKundliSelectedDate*/
                if (tvDatePicker.toString().length() > 0) {
                    CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                }
                setPlaceDataView();

                showToolBarTitle(typeface, calTitle + " " + String.valueOf(selectedYear));

                downloadDataWhenChangeInDate(selectedYear, cityId, true);
            }
        });
        LinearLayout layPlaceHolder = (LinearLayout) findViewById(R.id.layPlaceHolder);


        layPlaceHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSearchPlace(beanPlace);
            }
        });
        if (beanPlace != null) {
            setPlaceDataView();
        }


        //setup navigation drawer


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(ActIndianCalender.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_61_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_61_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(ActIndianCalender.this, "S61");
                CustomAddModel modal = topAdData.getImageObj().get(0);
                CUtils.divertToScreen(ActIndianCalender.this, modal.getImgthumbnailurl(), LANGUAGE_CODE);
            }
        });
    }

    public void setTopAdd(AdData topData) {
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;
        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        } else {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.VISIBLE);
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), com.libojassoft.android.misc.VolleySingleton.getInstance(ActIndianCalender.this).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(ActIndianCalender.this) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }

    }

    private void getData() {
        try {
            String result = CUtils.getStringData(ActIndianCalender.this, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "61");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToFestival() {
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
            return CUtils.convertTypedArrayToArrayList(ActIndianCalender.this, itemsIcon2, module_list_index_for_panchang);
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

    private void initSwipeRefreshLayout() {
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);//
    }

    public void downloadDataWhenChangeInDate(int selectedYear, String cityId, boolean isShowProgressbar) {
        //selectedYear = Integer.valueOf(tvDatePicker.getText().toString());
        if (currentYear != selectedYear) {
            downloadDataDetails(selectedYear, String.valueOf(cityId), isShowProgressbar);
        } else if (currentYear == selectedYear) {
            checkCachedData(selectedYear, cityId, isShowProgressbar);
        }
    }

    private void openSearchPlace(BeanPlace place) {
        Intent intent = new Intent(this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        this.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle bundle = data.getExtras();

                    if(bundle != null) {
                        BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                        beanPlace = place;
                        CUtils.saveBeanPalce(ActIndianCalender.this, place);
                        //this.beanHoroPersonalInfo.setPlace(beanPlace);
                        setPlaceDataView();

                        String cityId = "";
                        if (place != null) {
                            cityId = String.valueOf(place.getCityId());
                            this.cityId = cityId;
                        }
                        downloadDataDetails(selectedYear, cityId, true);

                        CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                                .setPlace(place);
                        mAdapter.notifyDataSetChanged();


                        //CUtils.savePlacePreference(ActIndianCalender.this, this.beanPlace, this.beanHoroPersonalInfo);
                    }
                }
                break;
            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK && data != null) {
                    Bundle b = data.getExtras();
                    if(b != null) {
                        String loginName = b.getString("LOGIN_NAME");
                        String loginPwd = b.getString("LOGIN_PWD");
                        setUserLoginDetails(loginName, loginPwd);
                    }
                }
            }
            break;
        }
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }

    private void setPlaceDataView() {

        if (beanPlace != null && beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Nepal")) {

            if (CUtils.getNewKundliSelectedDate() <= 1985) {
                beanPlace.setTimeZoneName("GMT+5.5");
                beanPlace.setTimeZone("5.5");
                beanPlace.setTimeZoneValue(Float.parseFloat("5.5"));
            } else {
                beanPlace.setTimeZoneName("GMT+5.75");
                beanPlace.setTimeZone("5.75");
                beanPlace.setTimeZoneValue(Float.parseFloat("5.75"));
            }
        }
        if (beanPlace != null && beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Suriname")) {
            if (CUtils.getNewKundliSelectedDate() <= 1984) {
                if (CUtils.getNewKundliSelectedDate() == 1984) {
                    beanPlace.setTimeZoneName("GMT-3.0");
                    beanPlace.setTimeZone("-3.0");
                    beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                } else {
                    beanPlace.setTimeZoneName("GMT-3.5");
                    beanPlace.setTimeZone("-3.5");
                    beanPlace.setTimeZoneValue(Float.parseFloat("-3.5"));
                }
            } else {
                beanPlace.setTimeZoneName("GMT-3.0");
                beanPlace.setTimeZone("-3.0");
                beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
            }
        }

        txtPlaceName = (TextView) findViewById(R.id.textViewPlaceName);
        txtPlaceDetail = (TextView) findViewById(R.id.textViewPlaceDetails);
        if (beanPlace != null && beanPlace.getCityName() != null)
            txtPlaceName.setText(beanPlace.getCityName());
        if (beanPlace != null)
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));

        txtPlaceName.setTypeface(robotRegularTypeface);
        txtPlaceDetail.setTypeface(robotRegularTypeface);
    }

    private String getYearText(int year) {
        int iYear = year;
        int monthNumber = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth();
        StringBuilder sb = new StringBuilder();
        String monthName = getResources().getStringArray(R.array.month_short_name_list)[monthNumber];
        sb.append(monthName + " " + String.valueOf(iYear) + " " + getResources().getString(R.string.desh_character) + " ");
        sb.append(monthName + " " + String.valueOf(iYear + 1));
        //return sb.toString();
        return String.valueOf(year);
    }

    private void initTimePickerView() {
        tvDatePicker = (TextView) findViewById(R.id.tvDatePicker);
        tvDatePicker.setText(String.valueOf(currentYear));

        /*set kundli date in newKundliSelectedDate*/
        if (tvDatePicker.toString().length() > 0) {
            CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
        }

        tvDatePicker.setTypeface(regularTypeface);
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMonthPicker(selectedYear);
            }
        });
    }

    private void initMonthPicker(int year) {
        // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
        if (Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25) {
            showCustomDatePickerDialogAboveHoneyComb(year);
            return;
        }

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
            CUtils.applyStyLing(dg, ActIndianCalender.this);


            Button butOK = (Button) dg.findViewById(android.R.id.button1);
            Button butCancel = (Button) dg.findViewById(android.R.id.button2);
            butOK.setText(R.string.set);
            butCancel.setText(R.string.cancel);

            butOK.setTypeface(regularTypeface);
            butCancel.setTypeface(regularTypeface);

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

    private void showCustomDatePickerDialogAboveHoneyComb(int year) {
        try {
            final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {

                @Override
                public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    int yearDiff = 0, year2 = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .getDateTime().getYear();
                    tvDatePicker.setText(String.valueOf(year));
                    checkCachedData(year, cityId, true);
                    selectedYear = year;
                    showToolBarTitle(typeface, calTitle + " " + String.valueOf(selectedYear));
                  /*  ((ActHinduCalender) activity).year = year;
                    downloadDataWhenChangeInDate(((ActHinduCalender) activity).year, ((ActHinduCalender) activity).currentYear);

                    if (checkYearInput(year)) {

                        yearDiff = year - year2;
                        onSelectedInputYear(yearDiff);
                    }*/
                }

            };


            final MyDatePickerDialog dg = new MyDatePickerDialog(ActIndianCalender.this, R.style.AppCompatAlertDialogStyle, myDateSetListener, 1, 1, year, false);
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

    private void addRecyclerView() {
        icRecyclerView.setVisibility(View.VISIBLE);
        icRecyclerView.setHasFixedSize(true);
        icRecyclerView.setFocusable(false);
        icRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        icRecyclerView.setLayoutManager(mLayoutManager);
    }


    private void checkCachedData(int selectedYear, String cityId, boolean isShowProgressbar) {
        String url = CGlobalVariables.indianCalenderUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                //  isCached = true;
                String saveData = new String(entry.data, "UTF-8");
                parseGsonData(saveData, url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            //Log.e("Volley Not Cached Data");
            if (!CUtils.isConnectedWithInternet(this)) {
                MyCustomToast mct = new MyCustomToast(this, this
                        .getLayoutInflater(), this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                downloadDataDetails(selectedYear, cityId, isShowProgressbar);
            }
        }
    }

    private void downloadDataDetails(final int selectedYear, final String cityId, boolean isShowProgressbar) {

        if (pd == null)
            pd = new CustomProgressDialog(ActIndianCalender.this, typeface);

        if (isShowProgressbar) {
            pd.show();
            pd.setCancelable(false);
        }

        final String url = CGlobalVariables.indianCalenderUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                        /*    if (isFirstTime) {
                                isFirstTime = false;
                                CUtils.saveBooleanData(ActIndianCalender.this, langCode, isFirstTime);
                            }*/
                            parseGsonData(response, url);

                        }
                        pd.dismiss();
                        if (swipeView != null) {
                            swipeView.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActIndianCalender.this, ActIndianCalender.this
                        .getLayoutInflater(), ActIndianCalender.this, typeface);
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
                if (swipeView != null) {
                    swipeView.setRefreshing(true);
                }
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
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActIndianCalender.this));
                headers.put("language", langCode);
                headers.put("date", String.valueOf(selectedYear));
                String cityIdToSend = "";
                if (beanPlace != null) {
                    cityIdToSend = String.valueOf(cityId);
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

    private void parseGsonData(String saveData, String url) {
        try {


            AllPanchangData allPanchangData;
            Gson gson = new Gson();
            allPanchangData = gson.fromJson(saveData, AllPanchangData.class);

            if (allPanchangData.getIndianCalenderData().size() != 0) {
                FragmentManager fm = getSupportFragmentManager();
                String cityIdToSend = "";
                if (beanPlace != null) {
                    cityIdToSend = String.valueOf(cityId);
                    setPlaceDataView();
                }

                DetailApiModel obj = new DetailApiModel(langCode, String.valueOf(selectedYear), cityIdToSend);
                ArrayList<DetailApiModel> arrayListObj = new ArrayList<DetailApiModel>();
                arrayListObj.add(obj);
                mAdapter = new IndianCalenderAdapter(this, allPanchangData.getIndianCalenderData(), arrayListObj);
                icRecyclerView.setAdapter(mAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            queue.getCache().remove(url);
        }
    }

    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        if (titleToshow != null)
            titleTextView.setText(titleToshow);
        else
            titleTextView.setText("");
        titleTextView.setTypeface(typeface);
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

    @Override
    public void onRefresh() {
        if (swipeView != null) {
            swipeView.setRefreshing(true);
        }
        downloadDataDetails(selectedYear, cityId, false);
    }
}
