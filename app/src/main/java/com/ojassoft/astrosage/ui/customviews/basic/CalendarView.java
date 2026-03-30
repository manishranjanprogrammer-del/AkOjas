package com.ojassoft.astrosage.ui.customviews.basic;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.ui.act.ActMontlyCalendar;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.MonthViewFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.panchang.Masa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout {
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private ExpandableHeightGridView grid;
    Context context;
    BeanPlace beanPlace = null;
    Fragment frag;
    View previousSelectedView;
    int previousSelectedPosition;
    Date previousSelectedDate;


    // seasons' rainbow
    int[] rainbow = new int[]{
            R.color.calendar_color,
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };
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

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CalendarView(Context context) {
        super(context);
        this.context = context;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (ExpandableHeightGridView) findViewById(R.id.calendar_grid);
        grid.setExpanded(true);

    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                return true;
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Calendar cal = Calendar.getInstance();
                Date today = currentDate.getTime();
                cal.setTime((Date) parent.getItemAtPosition(position));
                ((MonthViewFrag) frag).setTodayData(cal, ((ActMontlyCalendar) context).beanPlace);
                if (previousSelectedView != null) {
                    //Date date = (Date) parent.getItemAtPosition(position);
                    int day = previousSelectedDate.getDate();
                    int month = previousSelectedDate.getMonth();
                    int year = previousSelectedDate.getYear();
                    if (previousSelectedPosition == 0 || previousSelectedPosition == 7 || previousSelectedPosition == 14 || previousSelectedPosition == 21 || previousSelectedPosition == 28 || previousSelectedPosition == 35) {
                        if (month != today.getMonth() || year != today.getYear()) {
                            previousSelectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                        } else {
                            previousSelectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.sunday_color));
                        }

                    } else {
                        if (month != today.getMonth() || year != today.getYear()) {
                            previousSelectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                        } else {
                            previousSelectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.no_change_white));
                        }
                    }
                }
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.ColorPrimary));
                previousSelectedView = view;
                previousSelectedPosition = position;
                previousSelectedDate = (Date) parent.getItemAtPosition(position);


            }
        });

    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(null, null, null, null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(Fragment fragment, HashSet<Date> events, BeanPlace beanPlace, Date date) {
        frag = fragment;
        ArrayList<Date> cells = new ArrayList<>();
        if (date != null) {
            currentDate.setTime(date);
        }
        this.beanPlace = beanPlace;
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events, beanPlace));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(R.color.calendar_color));
    }


    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private HashSet<Date> eventDays;
        // for view inflation
        private LayoutInflater inflater;
        AajKaPanchangCalulation calculation;
        AajKaPanchangModel model;
        public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
        String language;
        String lat;
        String lng;
        String timeZone;
        String timeZoneString;
        String cityId = "";
        int currentMonth;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays, BeanPlace beanPlace) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
            LANGUAGE_CODE = ((AstrosageKundliApplication) ((Activity) context).getApplication()).getLanguageCode();
            language = getLanguage(CUtils.getLanguageCodeFromPreference(((Activity) context)));
            if (language.equals("")) {
                language = "en";
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
                cityId = String.valueOf(beanPlace.getCityId());
            }
            if (cityId.equals("-1")) {
                cityId = "1261481";
            }
            currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();
            // today
            Date today = currentDate.getTime();

            Masa objMasa;
            calculation = new AajKaPanchangCalulation(date, cityId, language, lat, lng, timeZone, timeZoneString);
            model = calculation.getPanchang();
            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            // if this day has an event, specify event image
            TextView textView1 = (TextView) view.findViewById(R.id.day_text1);
            TextView textView2 = (TextView) view.findViewById(R.id.day_text2);
            //LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.day_background);
            LinearLayout frameLayout = (LinearLayout) view.findViewById(R.id.main_frame);
            ImageView moonImage = (ImageView) view.findViewById(R.id.moon_image);
            textView1.setBackgroundResource(0);
            textView2.setBackgroundResource(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        //textView1.setBackgroundResource(R.drawable.reminder);
                        //textView2.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            textView1.setTypeface(null, Typeface.NORMAL);
            textView1.setTextColor(Color.BLACK);
            textView2.setTypeface(null, Typeface.NORMAL);
            //textView2.setTextColor(Color.BLACK);
            Date sDate = new Date();
            if(frag != null){
                sDate =  ((MonthViewFrag) frag).selecteddate;
            }


            if (position == 0 || position == 7 || position == 14 || position == 21 || position == 28 || position == 35) {
                frameLayout.setBackgroundColor(getResources().getColor(R.color.sunday_color));
                textView1.setTextColor(getResources().getColor(R.color.red1));
            }
            if (month != today.getMonth() || year != today.getYear()) {
                // if this day is outside current month, grey it out
                textView1.setTextColor(getResources().getColor(R.color.greyed_out));
                //textView2.setTextColor(getResources().getColor(R.color.greyed_out));
                if (Build.VERSION.SDK_INT>=11) {
                    textView2.setAlpha(.4f);
                    textView1.setAlpha(.4f);
                    moonImage.setAlpha(.4f);
                }
                frameLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
            } else if (day == sDate.getDate()) { // changed by abhishek for highlight selected date.
                // if it is today, set it to blue/bold
                textView1.setTypeface(null, Typeface.BOLD);
                //textView1.setTextColor(getResources().getColor(R.color.today));
                textView2.setTypeface(null, Typeface.BOLD);
                //textView2.setTextColor(getResources().getColor(R.color.today));
                previousSelectedView = view;
                previousSelectedPosition = position;
                previousSelectedDate = date;
                frameLayout.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

            }
            if (position == 0 || position == 7 || position == 14 || position == 21 || position == 28 || position == 35) {
                textView1.setTextColor(getResources().getColor(R.color.red1));
            }
            // set text
            textView1.setText(String.valueOf(date.getDate()));
            double[] tithi = model.getTithiInt();
            String tithiStr = "";

            for (int i = 0; i < tithi.length; i++) {
                if (i % 2 == 0) {
                    moonImage.setVisibility(VISIBLE);
                    moonImage.setImageDrawable(getResources().getDrawable(moonImages[(int) tithi[i] - 1]));

                    if (tithi[i] > 15) {

                        tithiStr = tithiStr + ((int) tithi[i] - 15);
                        Log.i("tithi>>", "" + ((int) tithi[i] - 15));
                        //if(i<13){

                        //}

                        /*if (tithi[i] - 15 != 15) {
                            tithiStr = tithiStr + ((int) tithi[i] - 15);
                        } else {
                            moonImage.setVisibility(VISIBLE);
                            //textView1.setTextColor(getResources().getColor(R.color.white));

                            moonImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_moon_dark));
                            //linearLayout.setBackground(getResources().getDrawable(R.drawable.ic_moon_dark));
                        }*/
                    } else {
                        //if (tithi[i] != 15) {
                        tithiStr = tithiStr + (int) tithi[i];
                        //textView1.setTextColor(getResources().getColor(R.color.black));
                        //} else {
                        // moonImage.setVisibility(VISIBLE);
                        // moonImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_moon_light));
                        //linearLayout.setBackground(getResources().getDrawable(R.drawable.ic_moon_light));
                        //}

                    }
                    tithiStr = tithiStr + ",";
                }

            }
            if (tithiStr.endsWith(",")) {
                tithiStr = tithiStr.substring(0, tithiStr.length() - 1);
            }
            if (tithiStr.startsWith(",")) {
                tithiStr = tithiStr.substring(1);
            }
            textView2.setText(tithiStr);


            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayLongPress(Date date);
    }

    private String getLanguage(int languageCode) {

        String lan = "en";

        switch (languageCode) {
            case CGlobalVariables.HINDI:
                lan = "hi";
                break;
            case CGlobalVariables.TAMIL:
                lan = "ta";
                break;
            case CGlobalVariables.MARATHI:
                lan = "mr";
                break;
            case CGlobalVariables.BANGALI:
                lan = "bn";
                break;
            case CGlobalVariables.KANNADA:
                lan = "ka";
                break;
            case CGlobalVariables.TELUGU:
                lan = "te";
                break;
            case CGlobalVariables.GUJARATI:
                lan = "gu";
                break;
            case CGlobalVariables.MALAYALAM:
                lan = "ml";
                break;
            case CGlobalVariables.ODIA:
                lan = "or";
                break;
            case CGlobalVariables.ASAMMESSE:
                lan = "as";
                break;
            case CGlobalVariables.SPANISH:
                lan = "es";
                break;
            case CGlobalVariables.CHINESE:
                lan = "zh";
                break;
            case CGlobalVariables.JAPANESE:
                lan = "ja";
                break;
            case CGlobalVariables.PORTUGUESE:
                lan = "pt";
                break;
            case CGlobalVariables.ITALIAN:
                lan = "it";
                break;
            case CGlobalVariables.GERMAN:
                lan = "de";
                break;
            case CGlobalVariables.FRENCH:
                lan = "fr";
                break;
        }

        return lan;

    }
}
