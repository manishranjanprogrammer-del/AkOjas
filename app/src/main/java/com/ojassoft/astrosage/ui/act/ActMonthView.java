package com.ojassoft.astrosage.ui.act;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HinduCalenderData;
import com.ojassoft.astrosage.customadapters.HinduClanderAdapter;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customviews.basic.CalendarView;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.utils.VolleySingleton;

/**
 * Created by ojas on २९/१/१८.
 */

public class ActMonthView extends BaseInputActivity implements DateWatcher {
    private TabLayout tabLayout;
    public ViewPager mViewPager;
    BeanPlace beanPlace;
    RecyclerView recyclerView;
    private RequestQueue queue;
    int currentYear;
    int selectedYear;
    private String langCode = null;
    Typeface typeface;
    TextView hinduMonthName, paksh, tithi, englishMonthName, yearText, placeText, hinduCalendar;
    Calendar calendar;
    ImageView locationImageView, calendarImageView;
    CalendarView cv;
    String cityId;
    private java.text.DateFormat mDateFormat;


    public ActMonthView() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateFormat = android.text.format.DateFormat.getMediumDateFormat(ActMonthView.this);
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_MONTH_VIEW;
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        selectedYear = currentYear;
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        //beanPlace = CUtils.getUserDefaultCityForVrat(ActMonthView.this);
        beanPlace = CUtils.getBeanPalce(ActMonthView.this);
        if (beanPlace!=null)
        {
            cityId = String.valueOf(beanPlace.getCityId());

        }



        setContentView(R.layout.act_monthview_layout);
        setToolbarItem();
        cv = ((CalendarView) findViewById(R.id.calendar_view));
        recyclerView = (RecyclerView) findViewById(R.id.hindu_calender_recyclerview);
        hinduMonthName = (TextView) findViewById(R.id.hindu_month_name_aamant);
        paksh = (TextView) findViewById(R.id.paksh_name);
        tithi = (TextView) findViewById(R.id.tithi_name);
        englishMonthName = (TextView) findViewById(R.id.english_month_name);
        yearText = (TextView) findViewById(R.id.year_text);
        placeText = (TextView) findViewById(R.id.place_text);
        hinduCalendar = (TextView) findViewById(R.id.hindu_calendar);
        locationImageView = (ImageView) findViewById(R.id.location_image);
        calendarImageView = (ImageView) findViewById(R.id.calendar_image);

        locationImageView.setVisibility(View.VISIBLE);
        calendarImageView.setVisibility(View.VISIBLE);
        addRecyclerView();
        setTodayData(calendar, beanPlace);
        updateCalendarView(calendar.getTime());
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
            }
        });
        checkCachedData(selectedYear, calendar.get(Calendar.MONTH));
        setTypefaceOfViews();
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

    }

    private void updateCalendarView(Date date) {
        if (date == null) {
            date = new Date();
        }
        HashSet<Date> events = new HashSet<>();
        events.add(date);
        //cv.updateCalendar(events, beanPlace, date);
    }

    private void setToolbarItem() {
        Toolbar tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.hindu_calender));
        tvTitle.setTypeface(regularTypeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(ActMonthView.this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        this.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }


    private void addRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }


    private void checkCachedData(int selectedYear, int month) {

        if (cityId.equals("-1")) {
            cityId = "";
        }
        String url=CGlobalVariables.hinduCalenderApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        if (entry != null) {
            try {
                //  isCached = true;
                String saveData = new String(entry.data, "UTF-8");
                pasreData(saveData, month,url);
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
                downloadDataDetails(selectedYear, month);
            }
        }
    }

    private void downloadDataDetails(final int selectedYear, final int month) {
        final CustomProgressDialog pd = new CustomProgressDialog(this, typeface);
        pd.show();
        pd.setCancelable(false);
        final String url=CGlobalVariables.hinduCalenderApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                            pasreData(response, month,url);

                        }
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActMonthView.this, ActMonthView.this
                        .getLayoutInflater(), ActMonthView.this, typeface);
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

                headers.put("key", CUtils.getApplicationSignatureHashCode(ActMonthView.this));
                headers.put("language", langCode);
                headers.put("date", String.valueOf(selectedYear));
                headers.put("lid", String.valueOf(beanPlace.getCityId()));
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

    private void pasreData(String resultStr, int selectedMonth,String url) {
        HinduCalenderData hinduCalenderData = null;
        try {
            DetailApiModel obj = new DetailApiModel(langCode, String.valueOf(selectedYear),cityId);
            ArrayList<DetailApiModel> arrayListObj = new ArrayList<DetailApiModel>();
            arrayListObj.add(obj);

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
                HinduCalenderData.MonthDataDetail monthDataDetail1 = hinduCalenderData.getHinducalendar().get(selectedMonth);
                HinduClanderAdapter hinduClanderAdapter = new HinduClanderAdapter(ActMonthView.this, monthDataDetail1, arrayListObj);
                recyclerView.setAdapter(hinduClanderAdapter);
            } else {

            }
        } catch (Exception e) {
            Log.i("error", e.getMessage() + "");
            queue.getCache().remove(url);
        }

    }

    private void setTodayData(Calendar date, BeanPlace beanPlace) {
        String lat;
        String lng;
        String timeZone;
        String timeZoneString;
        String cityId = String.valueOf(beanPlace.getCityId());
        if (langCode.equals("")) {
            langCode = "en";
        }

        if (cityId.equals("-1")) {
            cityId = "1261481";
        }

        lat = "0";
        lng = "0";
        timeZone = "0";
        timeZoneString = "";

        if (beanPlace != null) {
            lat = beanPlace.getLatitude();
            lng = beanPlace.getLongitude();
            timeZone = beanPlace.getTimeZone();
            timeZoneString = beanPlace.getTimeZoneString();
        }
        String[] monthArray = getResources().getStringArray(R.array.MonthName);
        String[] weekArray = getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(date.getTime(), cityId, langCode, lat, lng, timeZone, timeZoneString);
        AajKaPanchangModel model = calculation.getPanchang();
        hinduMonthName.setText(model.getMonthAmanta());
        paksh.setText(model.getPakshaName());
        tithi.setText(model.getTithiValue());
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            englishMonthName.setText(getDayNumberSuffix(date.get(Calendar.DATE)) + " " + monthArray[date.get(Calendar.MONTH)] + "] " + date.get(Calendar.YEAR));
        } else {
            englishMonthName.setText(getDayNumberSuffix(date.get(Calendar.DATE)) + " " + monthArray[date.get(Calendar.MONTH)] + ", " + date.get(Calendar.YEAR));
        }

        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            yearText.setText("¼" + weekArray[date.get(Calendar.DAY_OF_WEEK) - 1] + "½");
        } else {
            yearText.setText("(" + weekArray[date.get(Calendar.DAY_OF_WEEK) - 1] + ")");
        }

        placeText.setText(beanPlace.getCityName() + ", " + beanPlace.getCountryName());
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "st";
                } else {
                    return day + "";
                }

            case 2:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "nd";
                } else {
                    return day + "";
                }

            case 3:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "rd";
                } else {
                    return day + "";
                }

            default:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "th";
                } else {
                    return day + "";
                }

        }
    }

    private void setTypefaceOfViews() {
        hinduMonthName.setTypeface(regularTypeface);
        paksh.setTypeface(regularTypeface);
        tithi.setTypeface(regularTypeface);
        englishMonthName.setTypeface(regularTypeface);
        yearText.setTypeface(regularTypeface);
        placeText.setTypeface(robotRegularTypeface);
        hinduCalendar.setTypeface(mediumTypeface);

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
                    cityId = String.valueOf(beanPlace.getCityId());

                    //downloadDataDetails(currentYear);

                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
              /*      checkCachedData(selectedYear);
                    setTodayData(calendar, beanPlace);
                    updateCalendarView();
*/
                    updateData();
                }
                break;
        }
    }

    public void openCalendar() {
        if (CUtils.isUserWantsCustomCalender(ActMonthView.this)) {
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
                updateData();
            }

        };


        final MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(ActMonthView.this, R.style.AppCompatAlertDialogStyle, myDateSetListener, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR), false);
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
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
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
        CUtils.applyStyLing(dg, ActMonthView.this);

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
    }

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
            updateData();
        }
    };

    @Override
    public void onDateChanged(Calendar c) {
/*
        BeanDateTime beanDateTime = new BeanDateTime();
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setHour(c.get(Calendar.HOUR_OF_DAY));
        beanDateTime.setMin(c.get(Calendar.MINUTE));
        beanDateTime.setSecond(c.get(Calendar.SECOND));*/
        calendar = c;
        selectedYear = c.get(Calendar.YEAR);
        updateData();
    }

    private void updateData() {
        checkCachedData(selectedYear, calendar.get(Calendar.MONTH));
        setTodayData(calendar, beanPlace);
        updateCalendarView(calendar.getTime());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
