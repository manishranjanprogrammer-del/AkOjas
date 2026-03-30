package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HinduCalenderData;
import com.ojassoft.astrosage.customadapters.HinduClanderAdapter;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.act.ActMontlyCalendar;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.customviews.basic.CalendarView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;

/**
 * Created by ojas on ५/२/१८.
 */

public class MonthViewFrag extends Fragment implements OnRefreshListener {
    TextView hinduMonthAamantName, hinduMonthPurnimantName, paksh, tithi, englishMonthName, yearText, placeText, hinduCalendar;
    RecyclerView recyclerView;
    CalendarView cv;
    Activity activity;
    int LANGUAGE_CODE;
    private String langCode = null;
    Calendar calendar;
    int position;
    ImageView moonImage;
    LinearLayout topContainerLayout;
    int[] moonImages = {
            R.drawable.moon_light_dark_14,
            R.drawable.moon_light_dark_13,
            R.drawable.moon_light_dark_12,
            R.drawable.moon_light_dark_11,
            R.drawable.moon_light_dark_10,
            R.drawable.moon_light_dark_9,
            R.drawable.moon_light_dark_8,
            R.drawable.moon_light_dark_7,
            R.drawable.moon_light_dark_6,
            R.drawable.moon_light_dark_5,
            R.drawable.moon_light_dark_4,
            R.drawable.moon_light_dark_3,
            R.drawable.moon_light_dark_2,
            R.drawable.moon_light_dark_1,
            R.drawable.ic_moon_light,
            R.drawable.moon_light_14,
            R.drawable.moon_light_13,
            R.drawable.moon_light_12,
            R.drawable.moon_light_11,
            R.drawable.moon_light_10,
            R.drawable.moon_light_9,
            R.drawable.moon_light_8,
            R.drawable.moon_light_7,
            R.drawable.moon_light_6,
            R.drawable.moon_light_5,
            R.drawable.moon_light_4,
            R.drawable.moon_light_3,
            R.drawable.moon_light_2,
            R.drawable.moon_light_1,
            R.drawable.ic_moon_dark
    };
    private SwipeRefreshLayout swipeView;
    public Date selecteddate = new Date();

    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;

    public static MonthViewFrag newInstance(int posotion) {
        MonthViewFrag monthViewFrag = new MonthViewFrag();

        Bundle args = new Bundle();
        args.putInt("position", posotion);
        //args.putInt("year", year);
        monthViewFrag.setArguments(args);
        return monthViewFrag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((ActMontlyCalendar) activity).LANGUAGE_CODE;
        langCode = ((ActMontlyCalendar) activity).langCode;
        calendar = ((ActMontlyCalendar) activity).calendar;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_monthview_layout, container, false);
        if (getArguments() != null) {
            position = getArguments().getInt("position", 0);
        }
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        //calendar.set(calendar.get(Calendar.YEAR), position, calendar.get(Calendar.DATE));
        calendar.set(calendar.get(Calendar.YEAR), position, 1); //changed by abhishek
        cv = ((CalendarView) rootView.findViewById(R.id.calendar_view));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.hindu_calender_recyclerview);
        hinduMonthAamantName = (TextView) rootView.findViewById(R.id.hindu_month_name_aamant);
        hinduMonthPurnimantName = (TextView) rootView.findViewById(R.id.hindu_month_name_purnimant);
        paksh = (TextView) rootView.findViewById(R.id.paksh_name);
        tithi = (TextView) rootView.findViewById(R.id.tithi_name);
        englishMonthName = (TextView) rootView.findViewById(R.id.english_month_name);
        yearText = (TextView) rootView.findViewById(R.id.year_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);
        hinduCalendar = (TextView) rootView.findViewById(R.id.hindu_calendar);
        moonImage = (ImageView) rootView.findViewById(R.id.moon);
        ImageView infoIV = (ImageView) rootView.findViewById(R.id.info_iv);

        topContainerLayout = (LinearLayout) rootView.findViewById(R.id.toppview_container);
        initSwipeRefreshLayout(rootView);
        addRecyclerView();
        topAdImage = (NetworkImageView) rootView.findViewById(R.id.topAdImage);
        llCustomAdv = (LinearLayout) rootView.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((ActMontlyCalendar) activity).regularTypeface, "AKMCA"));
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        //added by abhishek
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int selectedMonth = calendar.get(Calendar.MONTH);
        if (currentMonth == selectedMonth) {
            calendar.set(calendar.get(Calendar.YEAR), currentMonth, Calendar.getInstance().get(Calendar.DATE));
        }//end

        setTodayData(calendar, ((ActMontlyCalendar) activity).beanPlace);
        updateCalendarView(calendar.getTime());
        // assign event handler
        cv.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
            }
        });
        setTypefaceOfViews();
        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.openLanguageSelectDialog(getActivity().getSupportFragmentManager());
            }
        });
        return rootView;
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_53_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_53_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S53");
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
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(activity).getImageLoader());
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
                topAdData = CUtils.getSlotData(adList, "53");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initSwipeRefreshLayout(View view) {
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setNestedScrollingEnabled(true);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);//
    }

    public void updateCalendarView(Date date) {
        if (date == null) {
            date = new Date();
        }
        HashSet<Date> events = new HashSet<>();
        events.add(date);
        cv.updateCalendar(MonthViewFrag.this, events, ((ActMontlyCalendar) activity).beanPlace, date);
    }

    public void addRecyclerView() {
        DetailApiModel obj = new DetailApiModel(langCode, String.valueOf(((ActMontlyCalendar) activity).selectedYear), ((ActMontlyCalendar) activity).cityId);
        ArrayList<DetailApiModel> arrayListObj = new ArrayList<DetailApiModel>();
        arrayListObj.add(obj);
        if (swipeView != null) {
            swipeView.setRefreshing(false);
        }
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        HinduCalenderData hinduCalenderData = ((ActMontlyCalendar) activity).hinduCalenderData;
        if (hinduCalenderData != null && hinduCalenderData.getHinducalendar() != null && !hinduCalenderData.getHinducalendar().isEmpty()) {
            HinduCalenderData.MonthDataDetail monthDataDetail = hinduCalenderData.getHinducalendar().get(position);
            HinduClanderAdapter hinduClanderAdapter = new HinduClanderAdapter(activity, monthDataDetail, arrayListObj);
            recyclerView.setAdapter(hinduClanderAdapter);
        }
    }

    public void setTodayData(final Calendar date, final BeanPlace beanPlace) {
        selecteddate = date.getTime();
        String lat;
        String lng;
        String timeZone;
        String timeZoneString;
        String cityId = "";
        if (beanPlace != null) {
            cityId = String.valueOf(beanPlace.getCityId());
        }
        if (cityId == null) {
            cityId = "";
        }
        if (TextUtils.isEmpty(langCode)) {
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
        String[] monthArray = getResources().getStringArray(R.array.month_short_name_list);
        String[] weekArray = getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(date.getTime(), cityId, langCode, lat, lng, timeZone, timeZoneString);
        AajKaPanchangModel model = calculation.getPanchang();
        //hinduMonthName.setText(model.getMonthAmanta());
        //if (CUtils.getIntData(activity, CGlobalVariables.HINDU_MONTH_KEY, 0) == 0) {
      /*  if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            hinduMonthAamantName.setText(model.getMonthAmanta() + " " + "(" + getResources().getString(R.string.amanta) + ")"  );
            hinduMonthPurnimantName.setText(model.getMonthPurnimanta() + " " + "(" + getResources().getString(R.string.purnimant) + ")");
        } else {*/
        hinduMonthAamantName.setText(model.getMonthAmanta() + " " + "(" + getResources().getString(R.string.amanta) + ")");
        hinduMonthPurnimantName.setText(model.getMonthPurnimanta() + " " + "(" + getResources().getString(R.string.purnimant) + ")");
        // }

       /* } else {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                hinduMonthAamantName.setText(model.getMonthPurnimanta() + " " + "¼" + getResources().getString(R.string.purnimant) + "½");
            } else {
                hinduMonthAamantName.setText(model.getMonthPurnimanta() + " " + "(" + getResources().getString(R.string.purnimant) + ")");
            }
        }*/


        paksh.setText(model.getPakshaName());
        tithi.setText(model.getTithiValue());
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            //change from krutidev to unicode
            englishMonthName.setText(date.get(Calendar.DATE) + " " + monthArray[date.get(Calendar.MONTH)] + ", " + date.get(Calendar.YEAR));
            yearText.setText("(" + weekArray[date.get(Calendar.DAY_OF_WEEK) - 1] + ")");
        } else {
            englishMonthName.setText(getDayNumberSuffix(date.get(Calendar.DATE)) + " " + monthArray[date.get(Calendar.MONTH)] + ", " + date.get(Calendar.YEAR));
            yearText.setText("(" + weekArray[date.get(Calendar.DAY_OF_WEEK) - 1] + ")");

        }
        if (beanPlace != null) {
            placeText.setText(beanPlace.getCityName() + ", " + beanPlace.getCountryName());
        } else {
            placeText.setText("New Delhi" + ", " + "India");
        }

        moonImage.setImageDrawable(getResources().getDrawable(moonImages[(int) (model.getTithiInt()[0]) - 1]));

        topContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.set(date.get(Calendar.YEAR), position, date.get(Calendar.DATE));
                int moduleType = MODULE_ASTROSAGE_PANCHANG;
                Intent intent = new Intent(activity, InputPanchangActivity.class);
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
                intent.putExtra("date", date);
                intent.putExtra("place", beanPlace);
                activity.startActivityForResult(intent, MODULE_ASTROSAGE_PANCHANG);
            }
        });
        placeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActMontlyCalendar) activity).openSearchPlace(beanPlace);
            }
        });
        englishMonthName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActMontlyCalendar) activity).openCalendar();
            }
        });


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
                    return String.valueOf(day);
                }

            case 2:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "nd";
                } else {
                    return String.valueOf(day);
                }

            case 3:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "rd";
                } else {
                    return String.valueOf(day);
                }

            default:
                if (LANGUAGE_CODE != CGlobalVariables.HINDI) {
                    return day + "th";
                } else {
                    return String.valueOf(day);
                }

        }
    }

    private void setTypefaceOfViews() {
        hinduMonthAamantName.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        hinduMonthPurnimantName.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        paksh.setTypeface(((BaseInputActivity) activity).regularTypeface);
        tithi.setTypeface(((BaseInputActivity) activity).regularTypeface);
        englishMonthName.setTypeface(((BaseInputActivity) activity).regularTypeface);
        yearText.setTypeface(((BaseInputActivity) activity).regularTypeface);
        placeText.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        hinduCalendar.setTypeface(((BaseInputActivity) activity).mediumTypeface);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity != null) {

            CUtils.hideMyKeyboard(activity);
        }

    }

    public void updateFragment() {
//        if (swipeView != null) {
//            swipeView.setRefreshing(false);
//        }
        setTodayData(calendar, ((ActMontlyCalendar) activity).beanPlace);
        updateCalendarView(calendar.getTime());
        addRecyclerView();
    }

    @Override
    public void onRefresh() {
        if (swipeView != null) {
            swipeView.setRefreshing(true);
        }
        int year = ((ActMontlyCalendar) activity).selectedYear;
        // setTodayData(calendar, ((ActMontlyCalendar) activity).beanPlace);
        //updateCalendarView(calendar.getTime());
        //((ActHinduCalender) activity).checkCachedDataOfAmavasyaFast(false);
        //((ActMontlyCalendar) activity).beanPlace.setCityId(-1);
        ((ActMontlyCalendar) activity).downloadDataDetails(year, position, false);

    }
}
