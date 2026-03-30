package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanNameValueCardListData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomAdapterRahuKaal;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.ExpandableHeightListView;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*@author Tejinder Singh
 * This Fragment Show RahuKaal for Next 30 days*/
public class RahuKaalInputFragment extends Fragment implements DateTimePicker.DateWatcher {

    private View view;
    //TextView placeTV;
    ImageView whatsAppIV;
    CardView cardViewDailyPanchang;
    CardView cardViewSunAndMoonCalculation;
    CardView cardViewHinduMonthAndYear;
    CardView cardViewAuspiciousInauspiciousTimings;
    CardView cardViewChandrabalamAndTarabalam;
    List<BeanNameValueCardListData> data2;
    private java.text.DateFormat mDateFormat;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    LinearLayout layPlaceHolder;
    // public Typeface typeface;
    private DatePicker datePicker;
    TextView textViewDate, textViewTo, textViewFrom, textViewCurrentRahuKaal, textViewHeadingRahuKaal;
    TextView tvDatePicker;
    String startTime, endTime;
    AajKaPanchangCalulation calculation;
    AajKaPanchangModel model;
    String language;
    public int SELECTED_MODULE;
    BeanPlace beanPlace;
    //BeanHoroPersonalInfo beanHoroPersonalInfo;
    TextView txtPlaceName, txtPlaceDetail, tvNote, tvNotemain, tvNotevalue, tvHoraTime;
    String city_Id = "1261481";
    Button btnPreviousDate, btnNextDate;
    TextView btnCurrentDate;
    BeanDateTimeForPanchang beanDateTime;
    String whatsAppData;
    int spaceBetweenTexts = 2;
    IPanchang panchang;
    LinearLayout llCustomAdv = null;
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    ScrollView scrollView;
    Activity activity;
    LoadPanchangData loadPanchangData;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;

    /*@author Tejinder Singh
     * Factory Instance Method to get Object for RahuKaal Fragment*/
    public static RahuKaalInputFragment newInstance(String text) {
        RahuKaalInputFragment rahuKaalInputFragment = new RahuKaalInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", text);
        rahuKaalInputFragment.setArguments(bundle);
        return rahuKaalInputFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (loadPanchangData != null && loadPanchangData.getStatus() == AsyncTask.Status.RUNNING)
            loadPanchangData.cancel(true);

        activity = null;
        panchang = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDateFormat = DateFormat.getMediumDateFormat(activity);
        // if (view == null) {
        view = inflater.inflate(R.layout.lay_frag_rahukaal, container, false);
        // }
        //add NEw
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;
        //Add advertisment in footer 10-Dec-2015
        //if (llCustomAdv == null) {
        //placeTV = ((InputPanchangActivity) activity).placeTV;
        whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((InputPanchangActivity) activity).regularTypeface, "SPNRA"));
        // }


        //beanHoroPersonalInfo = new BeanHoroPersonalInfo();

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode();

        language = CUtils.getLanguage(CUtils.getLanguageCodeFromPreference(activity));


        setLayRef(view);
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        Date _datePan = new Date();
        Calendar calendar = Calendar.getInstance();
        if (((InputPanchangActivity) activity).calendar != null) {
            // calendar.setTime(((InputPanchangActivity) activity).calendar);
            calendar = ((InputPanchangActivity) activity).calendar;
        }

        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        //Date date = calendar.getTime();
        //setLayout(date,city_Id,language,this.beanPlace);


        BeanDateTimeForPanchang beanDateTimeFromPref = CUtils
                .getDateTimeForPanchang(activity);
        if (beanDateTimeFromPref != null) {
            beanDateTime.setCalender(beanDateTimeFromPref.getMonth(),
                    beanDateTimeFromPref.getDay(),
                    beanDateTimeFromPref.getYear());
            beanDateTime.setDay(beanDateTimeFromPref.getDay());
            beanDateTime.setMonth(beanDateTimeFromPref.getMonth());
            beanDateTime.setYear(beanDateTimeFromPref.getYear());
            System.out.println(beanDateTimeFromPref.getDay());
            System.out.println(beanDateTimeFromPref.getMonth());
            System.out.println(beanDateTimeFromPref.getYear());
            tvDatePicker.setText(getFormatedTextToShowDate(beanDateTime));
        }
        beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
        updateBirthDate(beanDateTime, beanPlace);

        return view;

        //end New
    }

    private String getFormatedTextToShowDate(BeanDateTimeForPanchang beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + " - " + months[beanDateTime.getMonth()] + " - " + beanDateTime.getYear();
        return strDateTime;
    }

    private void setLayRef(View view) {
        try {
            btnCurrentDate = (TextView) view.findViewById(R.id.btnCurrentDate);
            btnCurrentDate.setText(getResources().getString(R.string.current_day));
            btnCurrentDate.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
            btnCurrentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentDayData();
                }
            });

            btnPreviousDate = (Button) view.findViewById(R.id.btnPreviousDate);
            btnPreviousDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPreviousDayData();
                }
            });

            btnNextDate = (Button) view.findViewById(R.id.btnNextDate);
            btnNextDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNextDayData();
                }
            });


            // ll_card_container = (LinearLayout) view.findViewById(R.id.ll_card_container);
            cardViewDailyPanchang = (CardView) view.findViewById(R.id.cardViewDailyPanchang);
            cardViewSunAndMoonCalculation = (CardView) view.findViewById(R.id.cardViewSunAndMoonCalculation);
            cardViewHinduMonthAndYear = (CardView) view.findViewById(R.id.cardViewHinduMonthAndYear);
            cardViewAuspiciousInauspiciousTimings = (CardView) view.findViewById(R.id.cardViewDailyPanchang);
            cardViewChandrabalamAndTarabalam = (CardView) view.findViewById(R.id.cardViewChandrabalamAndTarabalam);
            tvDatePicker = (TextView) view.findViewById(R.id.tvDatePicker);
            tvDatePicker.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);

            beanDateTime = new BeanDateTimeForPanchang();
            tvDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDate();
                }
            });

            layPlaceHolder = (LinearLayout) view.findViewById(R.id.layPlaceHolder);
           /* layPlaceHolder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openSearchPlace(beanHoroPersonalInfo.getPlace());
                }
            });*/
           /* placeTV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openSearchPlace(beanHoroPersonalInfo.getPlace());
                }
            });*/
            txtPlaceName = (TextView) view.findViewById(R.id.textViewPlaceName);
            //placeTV.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
            txtPlaceName.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
            txtPlaceDetail = (TextView) view.findViewById(R.id.textViewPlaceDetails);
            txtPlaceDetail.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
          /*  tvNote = (TextView) view.findViewById(R.id.tvNote);
          //  tvNote.setText(getResources().getString(R.string.panchang_note));
            tvNotemain = (TextView) view.findViewById(R.id.tvNotemain);
            //tvNotemain.setText(getResources().getString(R.string.note));
            tvNotevalue = (TextView) view.findViewById(R.id.tvNotevalue);
         //   tvNotevalue.setText(getResources().getString(R.string.panchang_note_head));*/
            textViewDate = (TextView) view.findViewById(R.id.tvdate);
            textViewFrom = (TextView) view.findViewById(R.id.tventrytme);
            textViewTo = (TextView) view.findViewById(R.id.tvexittme);
            textViewCurrentRahuKaal = (TextView) view.findViewById(R.id.tvCurrenthora);
            textViewHeadingRahuKaal = (TextView) view.findViewById(R.id.tvheadingrahukaal);
            textViewHeadingRahuKaal.setText(getResources().getString(R.string.weeklyrahukaal));
            textViewCurrentRahuKaal.setText(getResources().getString(R.string.currentrahukaal));
            textViewDate.setText(getResources().getString(R.string.date));
            textViewTo.setText(getResources().getString(R.string.end_times));
            textViewFrom.setText(getResources().getString(R.string.start_time));
            textViewDate.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
            textViewFrom.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
            textViewTo.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
            textViewCurrentRahuKaal.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
            textViewHeadingRahuKaal.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        } catch (Exception ex) {
            //Log.e("setLayRef: ", ex.getMessage());
        }

    }
    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_36_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_36_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S36");
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
                topAdData = CUtils.getSlotData(adList, "36");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        scrollView = (ScrollView) view.findViewById(R.id.scrollviewrahu);
        scrollView.smoothScrollTo(0, 0);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    public void setDate() {
        if (CUtils.isUserWantsCustomCalender(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showCustomDatePickerDialogAboveHoneyComb(beanDateTime);
            } else {
                showCustomDatePickerDialog(beanDateTime);
            }
        } else {
            // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
            if (Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25) {
                showCustomDatePickerDialogAboveHoneyComb(beanDateTime);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showAndroidDatePicker(beanDateTime);
            } else {
                showCustomDatePickerDialog(beanDateTime);
            }
        }

    }

    public void getPreviousDayData() {
        Calendar calendar = beanDateTime.getCalender();
        calendar.add(Calendar.DATE, -1);
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        updateBirthDate(beanDateTime, beanPlace);

    }

    public void getCurrentDayData() {
        Calendar calendar = Calendar.getInstance();
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        updateBirthDate(beanDateTime, beanPlace);

    }

    public void getNextDayData() {
        Calendar calendar = beanDateTime.getCalender();
        calendar.add(Calendar.DATE, 1);
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        updateBirthDate(beanDateTime, beanPlace);
    }


    public void updateBirthDate(BeanDateTimeForPanchang beanDateTime, BeanPlace beanPlace) {

        //Toast.makeText(this, beanDateTime.getYear(), Toast.LENGTH_SHORT).show();
        if(beanPlace != null) {
            this.city_Id = String.valueOf(this.beanPlace.getCityId());
        }
        this.beanDateTime.setYear(beanDateTime.getYear());
        this.beanDateTime.setMonth(beanDateTime.getMonth());
        this.beanDateTime.setDay(beanDateTime.getDay());
        this.beanDateTime.setCalender(beanDateTime.getMonth(), beanDateTime.getDay(), beanDateTime.getYear());
        tvDatePicker.setText(getFormatedTextToShowDate(beanDateTime));

        /*set kundli date in newKundliSelectedDate*/
        if (tvDatePicker.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = tvDatePicker.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount);
        }

        if (activity instanceof InputPanchangActivity) {
            ((InputPanchangActivity) activity).beanDateTime = beanDateTime;
        }

        updateDateButtonText(beanPlace);
    }

    private void updateDateButtonText(BeanPlace beanPlace) {

        Date date = beanDateTime.getCalender().getTime();
        setLayout(date, beanPlace);
    }

    private void setLayout(Date _datePan, BeanPlace beanPlace) {
        /*initViewRahuKaalTimings();*/
        if (loadPanchangData != null && loadPanchangData.getStatus() == AsyncTask.Status.RUNNING) {
            loadPanchangData.cancel(true);
        }

        loadPanchangData = new LoadPanchangData(beanPlace);
        loadPanchangData.execute();
    }

    private void initViewRahuKaalTimings(BeanPlace beanPlace) {
        try {
            String dateRahuKaal[] = new String[8];
            String startTimeRahuKaal[] = new String[8];
            String endTimeRahuKaal[] = new String[8];
            String timeZoneString = "";
            String lat = "0";
            String lng = "0";
            String timeZone = "0";

            if (beanPlace != null) {
                lat = beanPlace.getLatitude();
                lng = beanPlace.getLongitude();

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
                        if (CUtils.getNewKundliSelectedDate() == 1984 &&
                                CUtils.getNewKundliSelectedMonth() > 9) {
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


                timeZone = beanPlace.getTimeZone();
                timeZoneString = beanPlace.getTimeZoneString();
            }
            Calendar calendar = beanDateTime.getCalender();
            Date date = null;
            for (int i = 0; i < 8; i++) {
                if (i == 0) {
                    date = getNextDay(0, calendar);
                } else {
                    date = getNextDay(i, calendar);
                }
                //Log.e("Date IS::", "" + date);
                calculation = new AajKaPanchangCalulation(date, city_Id, language, lat, lng, timeZone, timeZoneString);
                model = calculation.getPanchang();
                dateRahuKaal[i] = CUtils.getDateShowRahuKaal(date);
                startTimeRahuKaal[i] = model.getRahuKaalVelaFrom();
                endTimeRahuKaal[i] = model.getRahuKaalVelaTo();
            }
            startTime = startTimeRahuKaal[0];
            endTime = endTimeRahuKaal[0];
            data2 = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < dateRahuKaal.length; i++) {
                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setDate(dateRahuKaal[i]);

                listData.setStartTime(CUtils.getTimeInFormate(startTimeRahuKaal[i]).replace("+", getContext().getString(R.string.tomorrow_label) + "\n"));
                listData.setEndTime(CUtils.getTimeInFormate(endTimeRahuKaal[i]).replace("+", getContext().getString(R.string.tomorrow_label) + "\n"));
                data2.add(listData);
            }

            /*showCurrentHoraTime(date, lat, lng, timeZone, timeZoneString);*/


        } catch (Exception ex) {
            //Log.e("EXCEPTION IS::", ex.getMessage());
        }
    }

    private void showCurrentHoraTime(Date dateNew, String lat, String lng, String timeZone, String timeZoneString) {
        Calendar calendarNew = Calendar.getInstance();
        Date date = calendarNew.getTime();
        calculation = new AajKaPanchangCalulation(date, city_Id, language, lat, lng, timeZone, timeZoneString);
        model = calculation.getPanchang();
        startTime = CUtils.getTimeInFormate(model.getRahuKaalVelaFrom()).replace("+", getContext().getString(R.string.tomorrow_label) + "\n");
        endTime = CUtils.getTimeInFormate(model.getRahuKaalVelaTo()).replace("+", getContext().getString(R.string.tomorrow_label) + "\n");
        tvHoraTime = (TextView) view.findViewById(R.id.tvhoratime);
        tvHoraTime.setText(startTime + " - " + endTime);
        //Log.e("StartTime", startTime);
    }

    private Date getNextDay(int i, Calendar calendar) {
        Date date = beanDateTime.getCalender().getTime();
        Calendar calendarNew = Calendar.getInstance();
        calendarNew.setTime(date);
        calendarNew.setTime(date);
        calendarNew.add(Calendar.DATE, i);
        date = calendarNew.getTime();
        return date;
    }

    //calendar work
    public void showCustomDatePickerDialogAboveHoneyComb(BeanDateTimeForPanchang beanDateTime1) {
        final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {
            @Override
            public void onDateSet(MyDatePicker view, int year, int month,
                                  int day) {
                /*BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
                beanDateTime.setYear(year);
                beanDateTime.setMonth(monthOfYear);
                beanDateTime.setDay(dayOfMonth);
                beanDateTime.setHour(0);
                beanDateTime.setMin(0);
                beanDateTime.setSecond(0);
                updateBirthDate(beanDateTime, beanPlace);*/

                beanDateTime.setCalender(month, day, year);
                beanDateTime.setYear(year);
                beanDateTime.setMonth(month);
                beanDateTime.setDay(CUtils.getDayOfMonth(view,day,month,year));
                beanDateTime.setHour(0);
                beanDateTime.setMin(0);
                beanDateTime.setSecond(0);
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

                updateBirthDate(beanDateTime, beanPlace);
            }
        };


        final MyDatePickerDialog mTimePicker = new MyDatePickerDialog(activity, R.style.AppCompatAlertDialogStyle, myDateSetListener, beanDateTime.getMonth(), (beanDateTime.getDay()), (beanDateTime.getYear()), false);
        mTimePicker.setCanceledOnTouchOutside(false);
        mTimePicker.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
        //mTimePicker.setTitle("");
        mTimePicker.show();
        try {
            int divierId = mTimePicker.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = mTimePicker.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }

        Button butOK = (Button) mTimePicker.findViewById(android.R.id.button1);
        Button butCancel = (Button) mTimePicker.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);
        Typeface regularTypeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
            /*BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
            beanDateTime.setYear(year);
            beanDateTime.setMonth(monthOfYear);
            beanDateTime.setDay(dayOfMonth);
            beanDateTime.setHour(0);
            beanDateTime.setMin(0);
            beanDateTime.setSecond(0);
            updateBirthDate(beanDateTime, beanPlace);*/
            beanDateTime.setCalender(month, day, year);
            beanDateTime.setYear(year);
            beanDateTime.setMonth(month);
            beanDateTime.setDay(day);
            beanDateTime.setHour(0);
            beanDateTime.setMin(0);
            beanDateTime.setSecond(0);
            CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

            updateBirthDate(beanDateTime, beanPlace);
        }
    };

    private void showAndroidDatePicker(BeanDateTimeForPanchang beanDateTime) {
        final CustomDatePicker dg = new CustomDatePicker(requireContext(), mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());

        dg.show();
    }

    @Override
    public void onDateChanged(Calendar c) {
        // TODO Auto-generated method stub

        /*BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setHour(c.get(Calendar.HOUR_OF_DAY));
        beanDateTime.setMin(c.get(Calendar.MINUTE));
        beanDateTime.setSecond(c.get(Calendar.SECOND));

        updateBirthDate(beanDateTime, beanPlace);*/

        beanDateTime.setCalender(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setHour(c.get(Calendar.HOUR_OF_DAY));
        beanDateTime.setMin(c.get(Calendar.MINUTE));
        beanDateTime.setSecond(c.get(Calendar.SECOND));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

        updateBirthDate(beanDateTime, beanPlace);

    }

    private void showCustomDatePickerDialog(final BeanDateTimeForPanchang beanDateTime) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(activity);
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) activity.getLayoutInflater()
                .inflate(R.layout.date_time_dialog, null);
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        mDateTimePicker.setDateChangedListener(this);
        mDateTimePicker.initDateElements(beanDateTime.getYear(),
                beanDateTime.getMonth(), beanDateTime.getDay(),
                beanDateTime.getHour(), beanDateTime.getMin(),
                beanDateTime.getSecond());
        mDateTimePicker.initData();
        // end
        Button setDateBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.SetDateTime);
        setDateBtn.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        setDateBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();

            }
        });

        Button cancelBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
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
        resetBtn.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
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

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        activity.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
           /* case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
                    updateBirthPlace(place);
                }
                break;*/
        }
    }

    public void updateAfterPlaceSelect(BeanPlace place) {
        //((InputPanchangActivity) activity).placeTV.setText(place.getCityName().trim());
        updateBirthPlace(place);
    }


    public void updateBirthPlace(BeanPlace beanPlace) {
        this.beanPlace = getBeanObj(beanPlace);
        ((InputPanchangActivity) activity).beanPlace = this.beanPlace;
        //this.beanHoroPersonalInfo.setPlace(beanPlace);
        txtPlaceName.setText(this.beanPlace.getCityName().trim());
        //placeTV.setText(beanPlace.getCityName().trim());
        //txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));
        try {
           // CUtils.saveBeanPalce(activity, this.beanPlace);
            this.city_Id = String.valueOf(this.beanPlace.getCityId());


            Date _datePan = new Date();
            if(beanDateTime == null) {
                Calendar calendar = Calendar.getInstance();
                beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
            }
            updateBirthDate(beanDateTime, this.beanPlace);
            panchang.updateLayout(6);//6 means Rahukaal
        } catch (Exception ex) {
            //com.google.analytics.tracking.android.//Log.e(ex.getMessage());
        }
    }

    public void shareContentData(String packageName, BeanPlace beanPlace) {

        whatsAppData = "";

        String enter1 = "\n";
        String enter2 = "\n\n";
        String enter3 = "\n\n\n";

        String headingImage = "🚩";
        String titleImage = "☀ ";
        String contentImage = "🔅 ";
        String heading1 = "🚩श्री गणेशाय नम:🚩 " + enter1;
        String heading2 = "📜 " + getResources().getString(R.string.rahu_kaal) + " 📜" + enter2;

        String heading = heading1 + heading2;

      /* SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
       String dateForButton = dateFormat.format(calendar.getTime());*/

        String date = tvDatePicker.getText().toString();

        if (beanPlace != null) {
            heading = heading + "☀ " + date + enter1;
            if (beanPlace.getCityName().equals("Manual_Lat_Long") || beanPlace.getCityName().equals("Current Location")) {
                heading = heading + "☀ " + beanPlace.getCityName();
            } else {
                heading = heading + "☀ " + beanPlace.getCityName() + ", " + beanPlace.getCountryName();
            }

        } else {

            heading = heading + "☀ " + date + enter1;
            heading = heading + "☀ New Delhi, India";
        }

        whatsAppData = heading + enter2;


        if (data2 != null)
            for (int i = 0; i < data2.size(); i++) {
                setDataToString(contentImage + data2.get(i).getDate(), data2.get(i).getStartTime(), data2.get(i).getEndTime(), enter1, 0);

            }

        CUtils.shareData(activity, whatsAppData, packageName, getResources().getString(R.string.sharerahukaal));

    }

    private void setDataToString(String title, String value, String time, String enter, int withOrWithoutTag) {
        if (withOrWithoutTag == 1) {
            if (time.equals("")) {
                whatsAppData = whatsAppData + title + giveMeSpace(spaceBetweenTexts) + value + enter;
            } else {
                if (value.contains("\n")) {
                    String[] arrName, arrValue;
                    if (value.contains(",")) {
                        arrName = value.split(",\n");
                    } else {
                        arrName = value.split("\n");
                    }

                    if (time.contains(",")) {
                        arrValue = time.split(",\n");
                    } else {
                        arrValue = time.split("\n");
                    }

                    whatsAppData = whatsAppData + title + " : \n";
                    int space = title.length() + spaceBetweenTexts + 3;
                    whatsAppData = whatsAppData + giveMeSpace(space) + arrName[0] + giveMeSpacewithTag(spaceBetweenTexts) + arrValue[0] + enter;
                    //Toast.makeText(this, ""+space, Toast.LENGTH_SHORT).show();
                    whatsAppData = whatsAppData + giveMeSpace(space);
                    whatsAppData = whatsAppData + arrName[1] + giveMeSpacewithTag(spaceBetweenTexts) + arrValue[1] + enter;
                } else {
                    whatsAppData = whatsAppData + title + giveMeSpace(spaceBetweenTexts) + value + giveMeSpacewithTag(spaceBetweenTexts) + time + enter;
                }
            }
        } else if (value.contains(",\n")) {
            String[] arrName = value.split(",\n");
            String[] arrValue = time.split(",\n");

            whatsAppData = whatsAppData + title + " :\n";
            int space = title.length() + spaceBetweenTexts + 3;
            whatsAppData = whatsAppData + giveMeSpace(space) + arrName[0] + giveMeSpace(spaceBetweenTexts) + arrValue[0] + enter;
            whatsAppData = whatsAppData + giveMeSpace(space);
            whatsAppData = whatsAppData + arrName[1] + giveMeSpace(spaceBetweenTexts) + arrValue[1] + enter;
        } else {
            whatsAppData = whatsAppData + title + giveMeSpace(spaceBetweenTexts) + value + giveMeSpace(spaceBetweenTexts) + time + enter;
        }
    }

    private String giveMeSpace(int num) {

        String space = "";

        for (int i = 1; i <= num; i++) {
            space = space + " ";
        }
        return space;
    }

    private String giveMeSpacewithTag(int num) {

        String space = "";
        int mid = num % 2;

        for (int i = 1; i <= num; i++) {
            space = space + " ";
            if (i == mid + 1) {
                space = space + "-";
            }
        }

        return space;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        try {
            if (activity != null && isVisibleToUser) {
                // If we are becoming invisible, then...
                whatsAppIV.setVisibility(View.VISIBLE);
                BeanDateTimeForPanchang beanDateTimeFromPref = CUtils
                        .getDateTimeForPanchang(activity);
                if (beanDateTimeFromPref != null) {
                    beanDateTime.setCalender(beanDateTimeFromPref.getMonth(),
                            beanDateTimeFromPref.getDay(),
                            beanDateTimeFromPref.getYear());
                    beanDateTime.setDay(beanDateTimeFromPref.getDay());
                    beanDateTime.setMonth(beanDateTimeFromPref.getMonth());
                    beanDateTime.setYear(beanDateTimeFromPref.getYear());
                    tvDatePicker.setText(getFormatedTextToShowDate(beanDateTime));
                }
                if (activity instanceof InputPanchangActivity) {
                    beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
                }
                updateBirthDate(beanDateTime, beanPlace);
            }
        } catch (Exception ex) {
            //
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    class LoadPanchangData extends AsyncTask<Void, Void, Void> {
        BeanPlace beanPlace;

        public LoadPanchangData(BeanPlace beanPlace) {
            this.beanPlace = beanPlace;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Date date = beanDateTime.getCalender().getTime();
            String key = CUtils.getPanchangKey(date, beanPlace.getCityName(), LANGUAGE_CODE);
            if (activity != null) {
                model = CUtils.getPanchangObject(activity, key);
                if (model == null) {
                    getPanchangData(date, language, beanPlace);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (activity != null) {
                setData(beanPlace);
            }

        }
    }

    private void getPanchangData(Date _datePan, String language, BeanPlace beanPlace) {

        if (language.equals("")) {
            language = "en";
        }
        BeanPlace beanPlace1 = getBeanObj(beanPlace);

        String cityId = "";
        if (beanPlace1 != null) {
            cityId = String.valueOf(beanPlace1.getCityId());
        }

        if (cityId.equals("")) {
            cityId = "1261481";
        }
        String lat = beanPlace1.getLatitude();
        String lng = beanPlace1.getLongitude();
        String timeZone = beanPlace1.getTimeZone();
        String timeZoneString = beanPlace1.getTimeZoneString();

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, cityId, language, lat, lng, timeZone, timeZoneString);
        model = calculation.getPanchang();
        if (activity != null && CUtils.checkCurrentDate(_datePan)) {
            String key = CUtils.getPanchangKey(_datePan, beanPlace.getCityName(), LANGUAGE_CODE);
            CUtils.savePanchangObject(activity, key, model);
        }

    }

    private void setData(BeanPlace beanPlace) {
        initViewRahuKaalTimings(beanPlace);
        if (beanPlace != null) {
            txtPlaceName.setText(beanPlace.getCityName().trim());
            //placeTV.setText(beanPlace.getCityName().trim());
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
        }
        ExpandableHeightListView rvListData2 = (ExpandableHeightListView) view.findViewById(R.id.rvListData);
        rvListData2.setAdapter(new CustomAdapterRahuKaal(activity, data2));
        rvListData2.setExpanded(true);
        showCurrentHoraTime(beanDateTime.getCalender().getTime(), beanPlace.getLatitude(), beanPlace.getLongitude(), beanPlace.getTimeZone(), beanPlace.getTimeZoneString());
    }

    private BeanPlace getBeanObj(BeanPlace beanPlace) {

        if (beanPlace != null) {

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
                    if (CUtils.getNewKundliSelectedDate() == 1984 &&
                            CUtils.getNewKundliSelectedMonth() > 9) {
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
        }
        return beanPlace;
    }

}
