package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HinduCalenderData;
import com.ojassoft.astrosage.customadapters.HinduClanderAdapter;
import com.ojassoft.astrosage.jinterface.IOpenInputYearCalendar;
import com.ojassoft.astrosage.jinterface.IYearInputBoxPopupFragmentDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.act.ActHinduCalender;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ojas on १२/१/१८.
 */

public class HinduCalenderFrag extends Fragment implements IYearInputBoxPopupFragmentDialog, IOpenInputYearCalendar, OnRefreshListener {
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    Activity activity;
    Typeface regularTypeface;
    Typeface RobotoRegularTypeface;
    //BeanHoroPersonalInfo beanHoroPersonalInfo;
    BeanPlace beanPlace;
    int SELECTED_MODULE;
    TextView txtPlaceName, txtPlaceDetail;
    TextView tvDatePicker;
    int position;
    RecyclerView recyclerView;
    int selectedListItem;
    NestedScrollView nestedScrollView;
    private SwipeRefreshLayout swipeView;
    private LinearLayout llCustomAdv;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            int yearDiff = 0, year2 = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear();
            // Toast.makeText(activity, "" + year, Toast.LENGTH_SHORT).show();
            tvDatePicker.setText(String.valueOf(year));

            /*set kundli date in newKundliSelectedDate*/
            if (tvDatePicker.toString().length() > 0) {
                CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                CUtils.setNewKundliSelectedMonth(position + 1);
            }
            setPlaceDataView();

            ((ActHinduCalender) activity).year = year;
            downloadDataWhenChangeInDate(((ActHinduCalender) activity).year, ((ActHinduCalender) activity).currentYear);
            if (checkYearInput(year)) {

                yearDiff = year - year2;
                onSelectedInputYear(yearDiff);
            }
        }
    };

    public static HinduCalenderFrag newInstance(int posotion, int year) {
        HinduCalenderFrag hinduCalenderFrag = new HinduCalenderFrag();

        Bundle args = new Bundle();
        args.putInt("position", posotion);
        args.putInt("year", year);
        hinduCalenderFrag.setArguments(args);
        return hinduCalenderFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        position = getArguments().getInt("position", 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.hindu_calender_month_layout, container, false);
        initTimePickerView(rootView);
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_HINDU_CALENDER;
       /* beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        if (CUtils.getBeanHoroPersonalInfo(activity) != null || CUtils.getBeanPalce(activity) != null) {
            beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(activity);
        } else {
            beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        }*/
        beanPlace = CUtils.getBeanPalce(activity);
        if (beanPlace == null) {
            beanPlace = CUtils.getDefaultPlace();
        }

        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedscrollview);
        LinearLayout layPlaceHolder = (LinearLayout) rootView.findViewById(R.id.layPlaceHolder);
        txtPlaceName = (TextView) rootView.findViewById(R.id.textViewPlaceName);
        txtPlaceDetail = (TextView) rootView.findViewById(R.id.textViewPlaceDetails);

        Button nextButton = (Button) rootView.findViewById(R.id.btnNextDate);
        Button previousButton = (Button) rootView.findViewById(R.id.btnPreviousDate);
        topAdImage = (NetworkImageView) rootView.findViewById(R.id.topAdImage);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.hindu_calender_recyclerview);
        if (((ActHinduCalender) activity).beanPlace != null) {
            setPlaceDataView();
        }
        llCustomAdv = (LinearLayout) rootView.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((ActHinduCalender) activity).regularTypeface, "AKHCA"));

        initSwipeRefreshLayout(rootView);
        layPlaceHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSearchPlace(beanPlace);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDatePicker.setText(getYearText(++((ActHinduCalender) activity).year));
                /*set kundli date in newKundliSelectedDate*/
                if (tvDatePicker.toString().length() > 0) {
                    CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                    CUtils.setNewKundliSelectedMonth(position + 1);
                }
                setPlaceDataView();
                downloadDataWhenChangeInDate(((ActHinduCalender) activity).year, ((ActHinduCalender) activity).currentYear);
                //((ActHinduCalender) activity).downloadHinduCalenderDetails(((ActHinduCalender) activity).year, "");
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDatePicker.setText(getYearText(--((ActHinduCalender) activity).year));
                /*set kundli date in newKundliSelectedDate*/
                if (tvDatePicker.toString().length() > 0) {
                    CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                    CUtils.setNewKundliSelectedMonth(position + 1);
                }
                setPlaceDataView();
                //((ActHinduCalender) activity).downloadHinduCalenderDetails(((ActHinduCalender) activity).year, "");
                downloadDataWhenChangeInDate(((ActHinduCalender) activity).year, ((ActHinduCalender) activity).currentYear);
            }
        });
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        initList(((ActHinduCalender) activity).hinduCalenderData);
        return rootView;
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_52_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_52_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S52");
                CustomAddModel modal = topAdData.getImageObj().get(0);
                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);
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
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), com.libojassoft.android.misc.VolleySingleton.getInstance(activity).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }

    }

    private void getData() {
        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "52");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSwipeRefreshLayout(View view) {
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);//
    }

    private void downloadDataWhenChangeInDate(int selectedYear, int currentYear) {
        selectedYear = Integer.valueOf(tvDatePicker.getText().toString());
        String cityId = "";
        if (((ActHinduCalender) activity).beanPlace != null) {
            cityId = String.valueOf(((ActHinduCalender) activity).beanPlace.getCityId());
        }
        if (currentYear != selectedYear) {
            ((ActHinduCalender) activity).downloadHinduCalenderDetails(((ActHinduCalender) activity).year, cityId, true,position);
        } else if (currentYear == selectedYear) {
            ((ActHinduCalender) activity).checkCachedDataOfAmavasyaFast(true,position);
        }
    }

    public void disableProgressbar() {
        if (swipeView != null) {
            swipeView.setRefreshing(false);
        }
    }

    public void initList(HinduCalenderData hinduCalenderData) {
        DetailApiModel obj = new DetailApiModel(((ActHinduCalender) activity).langCode, String.valueOf(((ActHinduCalender) activity).year), ((ActHinduCalender) activity).cityId);
        ArrayList<DetailApiModel> arrayListObj = new ArrayList<DetailApiModel>();
        arrayListObj.add(obj);
        if (swipeView != null) {
            swipeView.setRefreshing(false);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //int position = ((ActHinduCalender) activity).mViewPager.getCurrentItem();
        if(hinduCalenderData.getHinducalendar() != null) {
            HinduCalenderData.MonthDataDetail monthDataDetail = hinduCalenderData.getHinducalendar().get(position);
            HinduClanderAdapter hinduClanderAdapter = new HinduClanderAdapter(activity, monthDataDetail, arrayListObj);
            recyclerView.setAdapter(hinduClanderAdapter);
            selectedListItem = getSetectedListPosition(monthDataDetail.getMonthdata());
        }

    }

    private void initTimePickerView(View view) {
        tvDatePicker = (TextView) view.findViewById(R.id.tvDatePicker);

        //((ActHinduCalender) activity).year = year1;
        ;
        tvDatePicker.setText(String.valueOf(((ActHinduCalender) activity).year));
        /*set kundli date in newKundliSelectedDate*/
        if (tvDatePicker.toString().length() > 0) {
            CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
            CUtils.setNewKundliSelectedMonth(position + 1);
        }
        tvDatePicker.setTypeface(regularTypeface);
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAddEventClicked();
                //showAndroidDatePicker();
                //addEvent(activity);
                //addEventWithoutIntent();
                initMonthPicker(((ActHinduCalender) activity).year);
            }
        });
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

    public void initMonthPicker(int year) {

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
            final DatePickerDialog dg = new DatePickerDialog(activity, R.style.AppCompatAlertDialogStyle, mDateSetListener,
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
            CUtils.applyStyLing(dg, activity);


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

    public void showCustomDatePickerDialogAboveHoneyComb(int year) {
        try {
            final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {

                @Override
                public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    int yearDiff = 0, year2 = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .getDateTime().getYear();
                    tvDatePicker.setText(String.valueOf(year));
                    /*set kundli date in newKundliSelectedDate*/
                    if (tvDatePicker.toString().length() > 0) {
                        CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                        CUtils.setNewKundliSelectedMonth(position + 1);
                    }
                    ((ActHinduCalender) activity).year = year;
                    downloadDataWhenChangeInDate(((ActHinduCalender) activity).year, ((ActHinduCalender) activity).currentYear);

                    if (checkYearInput(year)) {

                        yearDiff = year - year2;
                        onSelectedInputYear(yearDiff);
                    }
                }

            };


            final MyDatePickerDialog dg = new MyDatePickerDialog(activity, R.style.AppCompatAlertDialogStyle, myDateSetListener, 1, 1, year, false);
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

    private boolean checkYearInput(int year) {
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
            // Toast.makeText(activity, "Please enter valid year", Toast.LENGTH_SHORT).show();
            // _ebtYear.setError("Please enter valid year");
            _isValid = false;
        }

        if (_isValid) {
            int diff = userInputYear - birthYear;
            if (diff < 0) {
                // Toast.makeText(OutputMasterActivity.this, "Year can not less than birth year", Toast.LENGTH_SHORT).show();
          /*      MyCustomToast mct = new MyCustomToast(activity, activity
                        .getLayoutInflater(), activity, regularTypeface);
                mct.show(getResources().getString(R.string.text_year_not_less_than_birth_year));*/
                // _ebtYear.setError("Year can not less than birth year");
                _isValid = false;
            }
            if (diff > 119) {
                //Toast.makeText(activity, "Please enter valid year", Toast.LENGTH_SHORT).show();
                // _ebtYear.setError("Please enter valid year");
                _isValid = false;
            }

        }
        /*
         * if(_ebtYear.getText().toString().trim().length()==0 ||
         * _ebtYear.getText().toString().trim().length()<4 || userInputYear <
         * _birthYear) { _ebtYear.setError("Please enter valid year");
         * _isValid=false; } if(_ebtYear.getText().toString().trim().length()==0
         * || _ebtYear.getText().toString().trim().length()<4 || userInputYear <
         * _birthYear) { _ebtYear.setError("Please enter valid year");
         * _isValid=false; }
         */
        return _isValid;
    }

    @Override
    public void onSelectedInputYear(int inputyear) {

        this.selectedInputYear(inputyear); // ADDED BY BIJENDRA ON 05-06-14
    }

    void selectedInputYear(int inputyear) {
    }

    @Override
    public void openInputYearCalendar() {

    }

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        this.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                    ((ActHinduCalender) activity).beanPlace = place;
                    //this.beanHoroPersonalInfo.setPlace(place);
                    setPlaceDataView();

                    String cityId = "";
                    if (place != null) {
                        cityId = String.valueOf(place.getCityId());
                        ((ActHinduCalender) activity).cityId = cityId;
                    }
                    beanPlace = place;
                    CUtils.saveBeanPalce(activity, place);
                    ((ActHinduCalender) activity).downloadHinduCalenderDetails(((ActHinduCalender) activity).year, cityId, true,position);

                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
                    ((ActHinduCalender) activity).adapter.notifyDataSetChanged();
                    ((ActHinduCalender) activity).setsTabLayout();
                    //CUtils.savePlacePreference(activity, ((ActHinduCalender) activity).beanPlace, this.beanHoroPersonalInfo);


                }
                break;
        }
    }

    public void setPlaceDataView() {
        if (((ActHinduCalender) activity).beanPlace != null) {

            if (((ActHinduCalender) activity).beanPlace != null && ((ActHinduCalender) activity).beanPlace.getCountryName() != null && ((ActHinduCalender) activity).beanPlace.getCountryName().trim().equalsIgnoreCase("Nepal")) {

                if (CUtils.getNewKundliSelectedDate() <= 1985) {
                    ((ActHinduCalender) activity).beanPlace.setTimeZoneName("GMT+5.5");
                    ((ActHinduCalender) activity).beanPlace.setTimeZone("5.5");
                    ((ActHinduCalender) activity).beanPlace.setTimeZoneValue(Float.parseFloat("5.5"));
                } else {
                    ((ActHinduCalender) activity).beanPlace.setTimeZoneName("GMT+5.75");
                    ((ActHinduCalender) activity).beanPlace.setTimeZone("5.75");
                    ((ActHinduCalender) activity).beanPlace.setTimeZoneValue(Float.parseFloat("5.75"));
                }
            }
            if (((ActHinduCalender) activity).beanPlace != null && ((ActHinduCalender) activity).beanPlace.getCountryName() != null && ((ActHinduCalender) activity).beanPlace.getCountryName().trim().equalsIgnoreCase("Suriname")) {
                if (CUtils.getNewKundliSelectedDate() <= 1984) {
                    if (CUtils.getNewKundliSelectedDate() == 1984 &&
                            CUtils.getNewKundliSelectedMonth() > 9) {
                        ((ActHinduCalender) activity).beanPlace.setTimeZoneName("GMT-3.0");
                        ((ActHinduCalender) activity).beanPlace.setTimeZone("-3.0");
                        ((ActHinduCalender) activity).beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                    } else {
                        ((ActHinduCalender) activity).beanPlace.setTimeZoneName("GMT-3.5");
                        ((ActHinduCalender) activity).beanPlace.setTimeZone("-3.5");
                        ((ActHinduCalender) activity).beanPlace.setTimeZoneValue(Float.parseFloat("-3.5"));
                    }
                } else {
                    ((ActHinduCalender) activity).beanPlace.setTimeZoneName("GMT-3.0");
                    ((ActHinduCalender) activity).beanPlace.setTimeZone("-3.0");
                    ((ActHinduCalender) activity).beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                }
            }


            txtPlaceName.setText(((ActHinduCalender) activity).beanPlace.getCityName());
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(((ActHinduCalender) activity).beanPlace));
            txtPlaceName.setTypeface(RobotoRegularTypeface);
            txtPlaceDetail.setTypeface(RobotoRegularTypeface);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity != null) {
          /*  if (((ActHinduCalender) activity).hinduCalenderData != null) {
                initList(((ActHinduCalender) activity).hinduCalenderData);
                tvDatePicker.setText(String.valueOf(((ActHinduCalender) activity).year));
            }*/
            CUtils.hideMyKeyboard(activity);
            /*if (((ActHinduCalender) activity).beanPlace != null) {
                //setPlaceDataView();
            }*/
        }


    }

    public void updateFragment() {
        if (activity != null && ((ActHinduCalender) activity).hinduCalenderData != null) {
            initList(((ActHinduCalender) activity).hinduCalenderData);
            tvDatePicker.setText(String.valueOf(((ActHinduCalender) activity).year));//currentYear
            /*set kundli date in newKundliSelectedDate*/
            if (tvDatePicker.toString().length() > 0) {
                CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                CUtils.setNewKundliSelectedMonth(position + 1);
            }
            setPlaceDataView();
        }
    }

    private int getSetectedListPosition(ArrayList<HinduCalenderData.MonthDataDetail.FestDetail> festDetailData) {
        int selectedPosition = 0;
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < festDetailData.size(); i++) {
            if (calendar.before(getDateObj(festDetailData.get(1).getFestDate()))) {
                selectedPosition++;
            }
        }
        return selectedPosition;
    }

    private Calendar getDateObj(String festivalDate) {


        Calendar calendar = Calendar.getInstance();
        try {
            Date obj = new SimpleDateFormat("dd/MM/yyyy").parse(festivalDate);
            calendar.setTime(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @Override
    public void onRefresh() {
        if (swipeView != null) {
            swipeView.setRefreshing(true);
        }
        String cityId = "";
        if (((ActHinduCalender) activity).beanPlace != null) {
            cityId = String.valueOf(((ActHinduCalender) activity).beanPlace.getCityId());
        }
        //((ActHinduCalender) activity).checkCachedDataOfAmavasyaFast(false);
        ((ActHinduCalender) activity).downloadHinduCalenderDetails(((ActHinduCalender) activity).year, cityId, false,position);

    }
}
