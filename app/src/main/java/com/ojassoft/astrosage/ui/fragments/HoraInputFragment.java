package com.ojassoft.astrosage.ui.fragments;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.customadapters.CustomAdapterforHora;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.ExpandableHeightListView;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;
import com.ojassoft.astrosage.utils.PanchangUtil;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.google.analytics.tracking.android.Log;

public class HoraInputFragment extends Fragment implements DateWatcher {

    String whatsAppData = "";
    //TextView placeTV;
    ImageView whatsAppIV;
    //public Typeface typeface;
    private CardView card;
    private List<HoraMetadata> data;
    private List<HoraMetadata> datatime;
    private List<HoraMetadata> datatimeexit;

    private List<HoraMetadata> dataCurrent;
    private List<HoraMetadata> datatimeCurrent;
    private List<HoraMetadata> datatimeexitCurrent;

    private List<HoraMetadata> dataCurrentN;
    private List<HoraMetadata> datatimeCurrentN;
    private List<HoraMetadata> datatimeexitCurrentN;
    private static final String DATE_FORMAT = "yyyy-mm-dd";
    private Button btnPreviousDate, btnNextDate;
    private String language;
    public int SELECTED_MODULE;
    int i;
    private java.text.DateFormat mDateFormat;
    private LinearLayout layPlaceHolder;
    private BeanPlace beanPlace;
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    public static final int SUB_ACTIVITY_USER_LOGIN = 1003;
    /*  private String latitude = "0";
      private String longitude = "0";
      private String timeZone = "+0";*/
    private BeanDateTimeForPanchang beanDateTime;
    //private BeanHoroPersonalInfo beanHoroPersonalInfo;
    private double lat = 28.36;
    private double lng = 77.12;
    private double tzone = +5.5;
    // AppHomeMenuFrag appHomeMenuFrag;

    private TextView tvCurrenthora, btnCurrentDate, tvplanetHoraName, tvhoratime,
            tvplanetsHoramean, tvDatePicker, tvplanet, tventrytme, tvexittme, tvTable, txtPlaceName, txtPlaceDetail;
    int spaceBetweenTexts = 2;
    int spaceBetweenTextsWithtag = 3;
    boolean flag = true;

    Activity activity;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    IPanchang panchang;
    LoadPanchangData loadPanchangData;
    private PanchangUtil objPanchangUtil;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

        this.activity = activity;
        panchang = (IPanchang) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (loadPanchangData != null && loadPanchangData.getStatus() == AsyncTask.Status.RUNNING)
            loadPanchangData.cancel(true);

        activity = null;
        panchang = null;
    }

    View view;
    LinearLayout llCustomAdv = null;
    AajKaPanchangModel model;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        data = new ArrayList<HoraMetadata>();
        datatime = new ArrayList<HoraMetadata>();
        datatimeexit = new ArrayList<HoraMetadata>();

        dataCurrent = new ArrayList<HoraMetadata>();
        datatimeCurrent = new ArrayList<HoraMetadata>();
        datatimeexitCurrent = new ArrayList<HoraMetadata>();

        dataCurrentN = new ArrayList<HoraMetadata>();
        datatimeCurrentN = new ArrayList<HoraMetadata>();
        datatimeexitCurrentN = new ArrayList<HoraMetadata>();
        //added by Ankit on 3-9-2019
        objPanchangUtil = new PanchangUtil();

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode();

        language = CUtils.getLanguage(CUtils.getLanguageCodeFromPreference(activity));
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_HORA;


        view = inflater.inflate(R.layout.activity_hora, container, false);
        whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((InputPanchangActivity) activity).regularTypeface, "SPCHO"));
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        //beanHoroPersonalInfo = new BeanHoroPersonalInfo();


        tvplanet = (TextView) view.findViewById(R.id.tvplanet);
        tvplanet.setText(getResources().getString(R.string.hora_planet_name));
        tventrytme = (TextView) view.findViewById(R.id.tventrytme);
        tvexittme = (TextView) view.findViewById(R.id.tvexittme);
        tvTable = (TextView) view.findViewById(R.id.tvTable);
        tvTable.setVisibility(View.VISIBLE);
        tvTable.setText(getResources().getString(R.string.table));
        tvCurrenthora = (TextView) view.findViewById(R.id.tvCurrenthora);
        tvplanetHoraName = (TextView) view.findViewById(R.id.tvplanetHoraName);
        tvhoratime = (TextView) view.findViewById(R.id.tvhoratime);
        tvplanetsHoramean = (TextView) view.findViewById(R.id.tvplanetsHoramean);
        txtPlaceName = (TextView) view.findViewById(R.id.textViewPlaceName);
        txtPlaceDetail = (TextView) view.findViewById(R.id.textViewPlaceDetails);

        card = (CardView) view.findViewById(R.id.cardViewDailyPanchang);
        tvDatePicker = (TextView) view.findViewById(R.id.tvDatePicker);
        layPlaceHolder = (LinearLayout) view.findViewById(R.id.layPlaceHolder);
        layPlaceHolder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // openSearchPlace(beanHoroPersonalInfo.getPlace());
            }
        });

        tvDatePicker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        btnPreviousDate = (Button) view.findViewById(R.id.btnPreviousDate);
        btnCurrentDate = (TextView) view.findViewById(R.id.btnCurrentDate);
        btnCurrentDate.setText(getResources().getString(R.string.current_day));
        btnNextDate = (Button) view.findViewById(R.id.btnNextDate);

        btnPreviousDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = beanDateTime.getCalender();
                calendar.add(Calendar.DATE, -1);
                beanDateTime.setCalender(calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
                // Date date = calendar.getTime();
                // setLayout(date,city_Id,language,this.beanPlace);
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
                updateBirthDate(beanDateTime, beanPlace);
            }
        });
        btnCurrentDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Date _datePan = new Date();
                Calendar calendar = Calendar.getInstance();
                beanDateTime.setCalender(calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
                // Date date = calendar.getTime();
                // setLayout(date,city_Id,language,this.beanPlace);
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

                updateBirthDate(beanDateTime, beanPlace);

            }
        });
        btnNextDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Calendar calendar = beanDateTime.getCalender();
                calendar.add(Calendar.DATE, 1);
                beanDateTime.setCalender(calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
                // Date date = calendar.getTime();
                // setLayout(date,city_Id,language,this.beanPlace);
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

                updateBirthDate(beanDateTime, beanPlace);

            }
        });

        beanDateTime = new BeanDateTimeForPanchang();
        //Date _datePan = new Date();
        Calendar calendar = Calendar.getInstance();
        if (((InputPanchangActivity) activity).calendar != null) {
            //calendar.setTime(((InputPanchangActivity) activity).calendar);
            calendar = ((InputPanchangActivity) activity).calendar;
        }

        beanDateTime.setCalender(calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));


        BeanDateTimeForPanchang beanDateTimeFromPref = CUtils
                .getDateTimeForPanchang(activity);
        if (beanDateTimeFromPref != null) {
            beanDateTime.setCalender(beanDateTimeFromPref.getMonth(),
                    beanDateTimeFromPref.getDay(),
                    beanDateTimeFromPref.getYear());
            beanDateTime.setDay(beanDateTimeFromPref.getDay());
            beanDateTime.setMonth(beanDateTimeFromPref.getMonth());
            beanDateTime.setYear(beanDateTimeFromPref.getYear());


        }
        beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
        updateBirthDate(beanDateTime, beanPlace);
        setTypeFaceOfView();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        flag = true;
        mDateFormat = DateFormat.getMediumDateFormat(activity);
        super.onCreate(savedInstanceState);
    }

    public static HoraInputFragment newInstance(String text) {

        // Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        HoraInputFragment f = new HoraInputFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_33_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_33_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S33");
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
                topAdData = CUtils.getSlotData(adList, "33");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHoraData(int year, int month, int day, BeanPlace beanPlace) {
        data.clear();
        datatime.clear();
        datatimeexit.clear();

        // //
        Calendar ca = beanDateTime.getCalender();
        int day_of_month = ca.get(Calendar.DAY_OF_WEEK) - 1;
        // //

        HoraLordName(day_of_month);
        HoraEndTime(year, month, day, beanPlace.getLatitude(), beanPlace.getLongitude(), beanPlace.getTimeZone());

        // System.out.println("ssss" + datatime.size() + datatimeexit.size());

        /*if (flag) {
            Calendar calendarCurrent = Calendar.getInstance();
            int hours = calendarCurrent.get(Calendar.HOUR);
            int minutes = calendarCurrent.get(Calendar.MINUTE);

            int day_Current = ca.get(Calendar.DAY_OF_WEEK) - 1;

            HoraLordNameCurrent(day_Current, true);
            HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), latitude, longitude, timezone, true);

            int numberx = getCurrentHoraNumber(datatimeCurrentN, datatimeexitCurrentN);
            if (datatimeCurrentN.size() > 0) {
                if (calendarCurrent.get(Calendar.AM_PM) == Calendar.AM && (hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) ||
                        hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) &&
                                minutes < Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(3, 5)))) {
                    int day_of_month_Current = ca.get(Calendar.DAY_OF_WEEK) - 2;

                    HoraLordNameCurrent(day_of_month_Current, false);
                    HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), latitude, longitude, timezone, false);

                    flag = false;
                } else {
//                Calendar calendarCurrent = Calendar.getInstance();
                    int day_of_month_Current = ca.get(Calendar.DAY_OF_WEEK) - 1;

                    HoraLordNameCurrent(day_of_month_Current, false);
                    HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), latitude, longitude, timezone, false);

                    flag = false;
                }
            }
        }*/

    }

    public void setDate() {
        // dateFragment = new DatePickerFragment();
        // dateFragment.show(getFragmentManager(), "datePicker");
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
        //showAndroidDatePicker(beanDateTime);
    }

    private void updateDateButtonText(BeanPlace beanPlace) {


        if (loadPanchangData != null && loadPanchangData.getStatus() == AsyncTask.Status.RUNNING) {
            loadPanchangData.cancel(true);
        }
        loadPanchangData = new LoadPanchangData(beanPlace);
        loadPanchangData.execute();
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
                MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private double[] HoraEndTime(int year, int month, int day, String latitude,
                                 String longitude, String timezone) {


        int jd = (int) Masa.toJulian(year, month + 1, day);
        // System.out.println("LAT LNG TMZNE" + year + month + 1 + day);
        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (NumberFormatException ex) {

        } catch (Exception e) {
            // TODO: handle exception
        }
        //added by Ankit on 3-9-2019
        if (beanPlace != null && objPanchangUtil.isDst(beanPlace.getTimeZoneString(), Calendar.getInstance().getTime())) {
            tzone = tzone + 1;
        }
        if (lat == 0 || lng == 0) {
            lat = 28.36;
            lng = 77.12;
            tzone = +5.5;
        }

        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        Place place = new Place(lat, lng, tzone);
        Double sunRise = model.getSunRiseDouble();
        Double sunSet = model.getSunSetDouble();
        try {
            for (i = 0; i < 13; i++) {

                HoraMetadata hora = new HoraMetadata();
                if (i <= 11) {

                    hora.setEntertimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(Muhurta.getDayDivisons(jd, place,
                                    sunRise, 12)[i], 0));

                    Muhurta.getDayDivisons(jd, place, sunRise, 8);

                    datatime.add(hora);

                }
                if (i > 0) {
                    hora.setExittimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(
                                    Muhurta.getDayDivisons(jd, place,
                                            sunRise, 12)[i], 0));
                    datatimeexit.add(hora);

                }

            }
            for (int j = 0; j < 13; j++) {

                HoraMetadata hora1 = new HoraMetadata();
                if (j <= 11) {
                    hora1.setEntertimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(
                                    Muhurta.getNightDivisons(jd, place,
                                            sunSet, 12)[j], 0));

                    datatime.add(hora1);

                }
                if (j > 0) {
                    hora1.setExittimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(
                                    Muhurta.getNightDivisons(jd, place,
                                            sunSet, 12)[j], 0));
                    datatimeexit.add(hora1);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Muhurta.getDayDivisons(jd, place, sunRise, 12);

    }


    private double[] HoraEndTimeCurrent(int year, int month, int day, BeanPlace beanPlace, boolean flag) {

        int jd = (int) Masa.toJulian(year, month + 1, day);
        // System.out.println("LAT LNG TMZNE" + year + month + 1 + day);
        try {
            lat = Double.parseDouble(beanPlace.getLatitude());
            lng = Double.parseDouble(beanPlace.getLongitude());
            tzone = Double.parseDouble(beanPlace.getTimeZone());

            //added by Ankit on 3-9-2019
            if (beanPlace != null && objPanchangUtil.isDst(beanPlace.getTimeZoneString(), Calendar.getInstance().getTime())) {
                tzone = tzone + 1;
            }

        } catch (NumberFormatException ex) {

        } catch (Exception e) {
            // TODO: handle exception
        }
        if (lat == 0 || lng == 0) {
            lat = 28.36;
            lng = 77.12;
            tzone = +5.5;
        }

        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        Place place = new Place(lat, lng, tzone);
        Double sunRise = Masa.getSunRise(jd, place);
        Double sunSet = Masa.getSunSet(jd, place);
       /* Double sunRise = model.getSunRiseDouble();
        Double sunSet = model.getSunSetDouble();*/
        try {
            for (i = 0; i < 13; i++) {

                HoraMetadata hora = new HoraMetadata();
                if (i <= 11) {

                    hora.setEntertimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(Muhurta.getDayDivisons(jd, place, sunRise, 12)[i], 0));

                    Muhurta.getDayDivisons(jd, place, sunRise, 8);
                    if (flag) {
                        datatimeCurrentN.add(hora);
                    } else {
                        datatimeCurrent.add(hora);
                    }

                }
                if (i > 0) {
                    hora.setExittimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(
                                    Muhurta.getDayDivisons(jd, place,
                                            sunRise, 12)[i], 0));
                    if (flag) {
                        datatimeexitCurrentN.add(hora);
                    } else {
                        datatimeexitCurrent.add(hora);
                    }

                }

            }
            for (int j = 0; j < 13; j++) {

                HoraMetadata hora1 = new HoraMetadata();
                if (j <= 11) {
                    hora1.setEntertimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(
                                    Muhurta.getNightDivisons(jd, place,
                                            sunSet, 12)[j], 0));

                    if (flag) {
                        datatimeCurrentN.add(hora1);
                    } else {
                        datatimeCurrent.add(hora1);
                    }

                }
                if (j > 0) {
                    hora1.setExittimedata(CUtils
                            .FormatDMSIn2DigitStringWithSignForhora(
                                    Muhurta.getNightDivisons(jd, place,
                                            sunSet, 12)[j], 0));
                    if (flag) {
                        datatimeexitCurrentN.add(hora1);
                    } else {
                        datatimeexitCurrent.add(hora1);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Muhurta.getDayDivisons(jd, place, sunRise, 12);

    }

    private int[] HoraLordName(int day_of_month) {
        int dayLordForDayHora[] = new int[24];
        try {
            String PlanetName[] = getResources().getStringArray(
                    R.array.hora_planets);
            String PlanetNameMeaning[] = getResources().getStringArray(
                    R.array.hora_planets_meaning);
            String PlanetNameMeaningforcurrentHora[] = getResources()
                    .getStringArray(R.array.pla_mean);

            /*
             * Calendar ca = Calendar.getInstance(); int day_of_month =
             * ca.get(Calendar.DAY_OF_WEEK) - 1;
             */
            // int day_of_month = day+1;

            for (int i = 0; i < 24; i++) {
                dayLordForDayHora[i] = (day_of_month + (i * 5)) % 7;
                HoraMetadata hora = new HoraMetadata();
                // System.out.println("PlanetName[dayLordForDayHora[i]]"+PlanetName[dayLordForDayHora[i]]);
                hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
                hora.setPlanetmeaning(PlanetNameMeaning[dayLordForDayHora[i]]);
                hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[dayLordForDayHora[i]]);
                data.add(hora);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayLordForDayHora;

    }

    private int[] HoraLordNameCurrent(int day_of_month, boolean flag) {
        int dayLordForDayHora[] = new int[24];
        try {
            String PlanetName[] = getResources().getStringArray(
                    R.array.hora_planets);
            String PlanetNameMeaning[] = getResources().getStringArray(
                    R.array.hora_planets_meaning);
            String PlanetNameMeaningforcurrentHora[] = getResources()
                    .getStringArray(R.array.pla_mean);

            /*
             * Calendar ca = Calendar.getInstance(); int day_of_month =
             * ca.get(Calendar.DAY_OF_WEEK) - 1;
             */
            // int day_of_month = day+1;

            for (int i = 0; i < 24; i++) {
                dayLordForDayHora[i] = (day_of_month + (i * 5)) % 7;
                HoraMetadata hora = new HoraMetadata();
                // System.out.println("PlanetName[dayLordForDayHora[i]]"+PlanetName[dayLordForDayHora[i]]);
                hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
                hora.setPlanetmeaning(PlanetNameMeaning[dayLordForDayHora[i]]);
                hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[dayLordForDayHora[i]]);
                if (flag) {
                    dataCurrentN.add(hora);
                } else {
                    dataCurrent.add(hora);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayLordForDayHora;

    }

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
           /* case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == activity.RESULT_OK) {
                    flag = true;
                    Bundle bundle = data.getExtras();
                    BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                    // set place detail which returned from place activity it should
                    // be set, because in case of activity recreation user should
                    // get updated place value.
                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
                    updateBirthPlace(place);
                }
                break;*/
        }
    }

    public void updateAfterPlaceSelect(BeanPlace place) {
        flag = true;
        //((InputPanchangActivity) activity).placeTV.setText(place.getCityName().trim());
        updateBirthPlace(place);
        setCurrentHora();
    }

    public void updateBirthPlace(BeanPlace beanPlace) {
        this.beanPlace = getBeanObj(beanPlace);
        ((InputPanchangActivity) activity).beanPlace = this.beanPlace;
        //this.beanHoroPersonalInfo.setPlace(beanPlace);
        txtPlaceName.setText(this.beanPlace.getCityName().trim());
        //placeTV.setText(beanPlace.getCityName().trim());
       /* txtPlaceDetail.setText(CUtils
                .getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));*/

        try {
            // CUtils.saveBeanPalce(activity, this.beanPlace);
           /* CUtils.savePlacePreference(activity, this.beanPlace,
                    this.beanHoroPersonalInfo);*/
            // CUtils.saveDateTimeForPanchang(activity,this.beanDateTime,true);
            if (beanDateTime == null) {
                Calendar calendar = Calendar.getInstance();
                beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
            }
            updateBirthDate(beanDateTime, this.beanPlace);
            panchang.updateLayout(3);// 3 means Hora

        } catch (Exception ex) {

            //Log.e(ex.getMessage().toString());
        }
    }

    public void updateBirthDate(BeanDateTimeForPanchang beanDateTime, BeanPlace beanPlace) {

        this.beanDateTime.setYear(beanDateTime.getYear());
        this.beanDateTime.setMonth(beanDateTime.getMonth());
        this.beanDateTime.setDay(beanDateTime.getDay());
        this.beanDateTime.setCalender(beanDateTime.getMonth(),
                beanDateTime.getDay(), beanDateTime.getYear());
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
            CUtils.setNewKundliSelectedMonth(monthInCount + 1);
        }

        if (activity instanceof InputPanchangActivity) {
            ((InputPanchangActivity) activity).beanDateTime = beanDateTime;
        }

        updateDateButtonText(beanPlace);
    }

    private String getFormatedTextToShowDate(
            BeanDateTimeForPanchang beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + " - "
                + months[beanDateTime.getMonth()] + " - "
                + beanDateTime.getYear();
        return strDateTime;
    }

    private void showCustomDatePickerDialog(
            final BeanDateTimeForPanchang beanDateTime) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(activity);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) activity
                .getLayoutInflater().inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        mDateTimePicker.setDateChangedListener(this);
        // Added by hukum to init date picker
        mDateTimePicker.initDateElements(beanDateTime.getYear(),
                beanDateTime.getMonth(), beanDateTime.getDay(),
                beanDateTime.getHour(), beanDateTime.getMin(),
                beanDateTime.getSecond());
        mDateTimePicker.initData();
        // end
        Button setDateBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.SetDateTime);
        setDateBtn.setTypeface(((InputPanchangActivity) activity).regularTypeface);
        // Update demo TextViews when the "OK" button is clicked
        setDateBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
                // System.out.println(beanDateTime.getDay());

            }
        });

        Button cancelBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(((InputPanchangActivity) activity).regularTypeface);
        // Cancel the dialog when the "Cancel" button is clicked
        cancelBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked

        Button resetBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.ResetDateTime);
        // resetBtn.setTypeface(typeface);
        resetBtn.setOnClickListener(new OnClickListener() {

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

    public void showCustomDatePickerDialogAboveHoneyComb(BeanDateTimeForPanchang beanDateTime1) {
        final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {
            @Override
            public void onDateSet(MyDatePicker view, int year, int month,
                                  int day) {
               /* BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
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
                beanDateTime.setDay(CUtils.getDayOfMonth(view, day, month, year));
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
            //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // mTimePicker.show().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND‌​);
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


    private void showAndroidDatePicker(BeanDateTimeForPanchang beanDateTime) {
        final CustomDatePicker dg = new CustomDatePicker(requireContext(), mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());

        dg.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month,
                              int day) {
           /* BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
            beanDateTime.setYear(year);
            beanDateTime.setMonth(month);
            beanDateTime.setDay(day);
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

    @Override
    public void onDateChanged(Calendar c) {
        // TODO Auto-generated method stub
       /* BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
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

    public void shareContentData(String packageName, BeanPlace beanPlace) {

        whatsAppData = "";

        String enter1 = "\n";
        String enter2 = "\n\n";
        String enter3 = "\n\n\n";

        String headingImage = "🚩";
        String titleImage = "☀";
        String contentImage = "🔅";
        String heading1 = "🚩श्री गणेशाय नम:🚩" + enter1;
        String heading2 = "📜 " + getResources().getString(R.string.hora_name)
                + " 📜" + enter2;

        String heading = heading1 + heading2;

        String date = tvDatePicker.getText().toString();

        if (beanPlace != null) {

            heading = heading + "☀ " + date + enter1;
            // heading = heading + "☀ " + beanPlace.getCityName() + ", "+
            // beanPlace.getCountryName();
            if (beanPlace.getCityName().equals("Manual_Lat_Long")
                    || beanPlace.getCityName().equals("Current Location")) {
                heading = heading + "☀ " + beanPlace.getCityName();
            } else {
                heading = heading + "☀ " + beanPlace.getCityName() + ", "
                        + beanPlace.getCountryName();
            }

        } else {

            heading = heading + "☀ " + date + enter1;
            heading = heading + "☀ New Delhi, India";

        }

        whatsAppData = heading + enter2;

        for (int i = 0; i < 24; i++) {
            if (data.size() > i && datatime.size() > i && datatimeexit.size() > i) {
                setDataToString(contentImage + data.get(i).getPlanetdata(),
                        datatime.get(i).getEntertimedata() + " - ", datatimeexit
                                .get(i).getExittimedata(), enter1, 0);
            }
        }

        CUtils.shareData(activity, whatsAppData, packageName, activity
                .getResources().getString(R.string.share_hora));

    }

    private void setDataToString(String title, String value, String time,
                                 String enter, int withOrWithoutTag) {
        try {
            // whatsAppData = whatsAppData +
            // getResources().getString(R.string.tithi1)+giveMeSpace(spaceBetweenTexts)+model.getTithiValue()+giveMeSpace(spaceBetweenTexts)+model.getTithiTime()+enter1;
            if (withOrWithoutTag == 1) {
                if (time.equals("")) {
                    whatsAppData = whatsAppData + title
                            + giveMeSpace(spaceBetweenTexts) + value + enter;
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
                        whatsAppData = whatsAppData + giveMeSpace(space)
                                + arrName[0]
                                + giveMeSpacewithTag(spaceBetweenTexts)
                                + arrValue[0] + enter;
                        // Toast.makeText(this, ""+space,
                        // Toast.LENGTH_SHORT).show();
                        whatsAppData = whatsAppData + giveMeSpace(space);
                        whatsAppData = whatsAppData + arrName[1]
                                + giveMeSpacewithTag(spaceBetweenTexts)
                                + arrValue[1] + enter;
                    } else {
                        whatsAppData = whatsAppData + title
                                + giveMeSpace(spaceBetweenTexts) + value
                                + giveMeSpacewithTag(spaceBetweenTexts) + time
                                + enter;
                    }
                }
            } else if (value.contains(",\n")) {
                String[] arrName = value.split(",\n");
                String[] arrValue = time.split(",\n");

                whatsAppData = whatsAppData + title + " :\n";
                int space = title.length() + spaceBetweenTexts + 3;
                whatsAppData = whatsAppData + giveMeSpace(space) + arrName[0]
                        + giveMeSpace(spaceBetweenTexts) + arrValue[0] + enter;
                // Toast.makeText(this, ""+space, Toast.LENGTH_SHORT).show();
                whatsAppData = whatsAppData + giveMeSpace(space);
                whatsAppData = whatsAppData + arrName[1]
                        + giveMeSpace(spaceBetweenTexts) + arrValue[1] + enter;
            } else {
                whatsAppData = whatsAppData + title
                        + giveMeSpace(spaceBetweenTexts) + value
                        + giveMeSpace(spaceBetweenTexts) + time + enter;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private int getCurrentHoraNumber(List<HoraMetadata> datatime2,
                                     List<HoraMetadata> datatimeexit2) {
        String[] resultEntryTime = null;
        String[] resultExitTime = null;
        int currentHoraNumber = 0;
        try {
            Calendar calendar = Calendar.getInstance();

            Calendar horaEntryTime = Calendar.getInstance();
            Calendar horaExitTime = Calendar.getInstance();
            /*
             * System.out .println("vvvv" + datatime2.size() +
             * datatimeexit2.size());
             */
            for (int i = 0; i <= datatime2.size(); i++) {
                resultEntryTime = datatime2.get(i).getEntertimedata()
                        .split(":");
                if (datatimeexit2.size() > i) {
                    resultExitTime = datatimeexit2.get(i).getExittimedata()
                            .split(":");
                }
                if (Integer.parseInt(resultExitTime[0]) >= 24) {
                    if (Integer.parseInt(resultExitTime[0]) == 24) {
                        resultExitTime[0] = "00";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 25) {
                        resultExitTime[0] = "01";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 26) {
                        resultExitTime[0] = "02";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 27) {
                        resultExitTime[0] = "03";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 28) {
                        resultExitTime[0] = "04";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 29) {
                        resultExitTime[0] = "05";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 30) {
                        resultExitTime[0] = "06";
                    }
                }
                if (Integer.parseInt(resultEntryTime[0]) >= 24) {
                    if (Integer.parseInt(resultEntryTime[0]) == 24) {
                        resultEntryTime[0] = "00";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 25) {
                        resultEntryTime[0] = "01";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 26) {
                        resultEntryTime[0] = "02";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 27) {
                        resultEntryTime[0] = "03";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 28) {
                        resultEntryTime[0] = "04";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 29) {
                        resultEntryTime[0] = "05";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 30) {
                        resultEntryTime[0] = "06";
                    }
                }

                long currentTimeMilliSeconds = calendar.getTimeInMillis();

                horaEntryTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(resultEntryTime[0]));
                horaEntryTime.set(Calendar.MINUTE,
                        Integer.parseInt(resultEntryTime[1]));
                horaEntryTime.set(Calendar.SECOND,
                        Integer.parseInt(resultEntryTime[2]));
                long horaEntryTimeMilliSeconds = horaEntryTime
                        .getTimeInMillis();

                horaExitTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(resultExitTime[0]));

                horaExitTime.set(Calendar.MINUTE,
                        Integer.parseInt(resultExitTime[1]));
                horaExitTime.set(Calendar.SECOND,
                        Integer.parseInt(resultExitTime[2]));
                long horaExitTimeMilliSeconds = horaExitTime.getTimeInMillis();
                if (horaEntryTimeMilliSeconds > horaExitTimeMilliSeconds) {
                    horaExitTimeMilliSeconds = horaExitTimeMilliSeconds + 24
                            * 60 * 60 * 1000;
                }
                if (currentTimeMilliSeconds < horaEntryTimeMilliSeconds) {
                    currentTimeMilliSeconds = currentTimeMilliSeconds + 24 * 60
                            * 60 * 1000;
                }

                if (currentTimeMilliSeconds >= horaEntryTimeMilliSeconds
                        && currentTimeMilliSeconds <= horaExitTimeMilliSeconds) {

                    currentHoraNumber = i;
                    break;
                }

            }
        } catch (Exception e) {
            //Log.e(e.getMessage().toString());
            System.out.println(e.getMessage().toString());

        }

        return currentHoraNumber;
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

                BeanHoroPersonalInfo bHPInfo = CUtils.getBeanHoroPersonalInfo(activity);

                flag = true;
                if (activity instanceof InputPanchangActivity) {
                    beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
                }
                updateBirthDate(beanDateTime, beanPlace);
                setCurrentHora();

            }
        } catch (Exception ex) {
            //
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void setTypeFaceOfView() {
        tvDatePicker.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        btnCurrentDate.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        txtPlaceName.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        //placeTV.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        txtPlaceDetail.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvCurrenthora.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        tvplanetHoraName.setTypeface(((InputPanchangActivity) activity).regularTypeface);
        tvhoratime.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvplanetsHoramean.setTypeface(((InputPanchangActivity) activity).regularTypeface);
        tvTable.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        tvplanet.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        tventrytme.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        tvexittme.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
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

    private void setData(BeanPlace beanPlace) {
        getHoraData(beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay(), beanPlace);
        String Horatag = "Horatag";
        ExpandableHeightListView listview = (ExpandableHeightListView) view.findViewById(R.id.rvListData);
        listview.setFocusable(false);
        if (beanPlace != null) {
            txtPlaceName.setText(beanPlace.getCityName().trim());
            //placeTV.setText(beanPlace.getCityName().trim());
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
        }
        listview.setAdapter(new CustomAdapterforHora(activity, data, datatime,
                datatimeexit, Horatag));
        listview.setExpanded(true);
        setListViewHeightBasedOnChildren(listview);
        //setCurrentHora();
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

    @Override
    public void onResume() {
        super.onResume();
        setCurrentHora();
    }

    private void setCurrentHora() {
        getCurrentHora(beanPlace);
        int number = getCurrentHoraNumber(datatimeCurrent, datatimeexitCurrent);
        if (dataCurrent.size() > number)
            tvplanetHoraName.setText(dataCurrent.get(number).getPlanetdata());

        if (datatimeCurrent.size() > number && datatimeexitCurrent.size() > number)
            tvhoratime.setText(CUtils.getTimeInFormate(datatimeCurrent.get(number).getEntertimedata()).replace("+", "") + " - "
                    + CUtils.getTimeInFormate(datatimeexitCurrent.get(number).getExittimedata()).replace("+", ""));
        if (dataCurrent.size() > number)
            tvplanetsHoramean.setText(dataCurrent.get(number).getPlanetCurrentHorameaning());

    }


    private void getCurrentHora(BeanPlace beanPlace) {
        if (flag) {
            dataCurrent.clear();
            datatimeCurrent.clear();
            datatimeexitCurrent.clear();
            dataCurrentN.clear();
            datatimeCurrentN.clear();
            datatimeexitCurrentN.clear();
            Calendar calendarCurrent = Calendar.getInstance();
            int hours = calendarCurrent.get(Calendar.HOUR);
            int minutes = calendarCurrent.get(Calendar.MINUTE);

            int day_Current = calendarCurrent.get(Calendar.DAY_OF_WEEK) - 1;

            HoraLordNameCurrent(day_Current, true);
            HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), beanPlace, true);

            //int numberx = getCurrentHoraNumber(datatimeCurrentN, datatimeexitCurrentN);
            if (datatimeCurrentN.size() > 0) {
                if (calendarCurrent.get(Calendar.AM_PM) == Calendar.AM &&
                        (hours < Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) ||
                                (hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) &&
                                        minutes < Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(3, 5))))) {
                    int day_of_month_Current = calendarCurrent.get(Calendar.DAY_OF_WEEK) - 2;
                    HoraLordNameCurrent(day_of_month_Current, false);
                    HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), beanPlace, false);

                    flag = false;
                } else {
//                Calendar calendarCurrent = Calendar.getInstance();
                    int day_of_month_Current = calendarCurrent.get(Calendar.DAY_OF_WEEK) - 1;
                    HoraLordNameCurrent(day_of_month_Current, false);
                    HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), beanPlace, false);
                    flag = false;
                }
            }
        }
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
