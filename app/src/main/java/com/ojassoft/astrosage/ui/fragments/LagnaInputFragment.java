package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanLagnaTable;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.Festivalapidata2;
import com.ojassoft.astrosage.beans.Festivalapidatum;
import com.ojassoft.astrosage.customadapters.CustomAdapterforLagna;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.LagnaTableCalculation;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_DUAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_FIXED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_MOVABLE;


public class LagnaInputFragment extends Fragment implements DateWatcher {

    String whatsAppData = "";
    ImageView whatsAppIV;
    private CardView card;
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
    private BeanDateTimeForPanchang beanDateTime;
    private TextView tvCurrentlagna, btnCurrentDate, tvRashiLagnaName, tvLagnaStarttime,
            tvLagnaEndtime, tvDatePicker, txtPlaceName, txtPlaceDetail;
    int spaceBetweenTexts = 2;
    int spaceBetweenTextsWithtag = 3;
    boolean flag = true;

    Activity activity;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    IPanchang panchang;
    BeanLagnaTable beanLagnaTable = null;
    private Typeface typeface;
    private TextView tvSunRiseAt;
    private TextView tvLagnaAtSunRise;
    private TextView tvLagnaRashiName;
    private TextView tvLagnaDegree;
    private LinearLayout llCurrentLagnaNature;
    private TextView tvLagnaNatureCurrent;
    private ImageView ivLagnaNatureCurrent;
    private LinearLayout llSunRiseLagnaNature;
    private TextView tvLagnaNatureSunRise;
    private ImageView ivLagnaNatureSunRise;

    private TextView tvNakshatra;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout llTwoCard;
    private NestedScrollView mNestedScrollView;
    private CustomProgressDialog pd;
    View view;
    LinearLayout llCustomAdv = null;
    AajKaPanchangModel model;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
        panchang = (IPanchang) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
        panchang = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();

        language = CUtils.getLanguage(CUtils.getLanguageCodeFromPreference(activity));

        SELECTED_MODULE = CGlobalVariables.MODULE_LAGNA;

        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        view = inflater.inflate(R.layout.activity_lagna, container, false);
        whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((InputPanchangActivity) activity).regularTypeface, "SPCHO"));
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        addRecyclerView();
        tvSunRiseAt = (TextView) view.findViewById(R.id.tv_sunrise_at);
        tvCurrentlagna = (TextView) view.findViewById(R.id.tv_current_lagna);
        tvRashiLagnaName = (TextView) view.findViewById(R.id.tv_rashi_lagna_name);
        llCurrentLagnaNature = (LinearLayout) view.findViewById(R.id.ll_current_lagna_nature);
        ivLagnaNatureCurrent = (ImageView) view.findViewById(R.id.iv_nature_current);
        tvLagnaNatureCurrent = (TextView) view.findViewById(R.id.tv_lagna_nature_current);
        tvLagnaStarttime = (TextView) view.findViewById(R.id.tv_lagna_start_time);
        tvLagnaEndtime = (TextView) view.findViewById(R.id.tv_lagna_end_time);
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        tvLagnaAtSunRise = (TextView) view.findViewById(R.id.lagna_at_sun_rise);
        llSunRiseLagnaNature = (LinearLayout) view.findViewById(R.id.ll_sunrise_lagna_nature);
        ivLagnaNatureSunRise = (ImageView) view.findViewById(R.id.iv_nature_sunrise);
        tvLagnaNatureSunRise = (TextView) view.findViewById(R.id.tv_lagna_nature_sunrise);
        tvLagnaRashiName = (TextView) view.findViewById(R.id.tv_rashi_name);
        tvLagnaDegree = (TextView) view.findViewById(R.id.tv_lagna_degree);
        tvNakshatra = (TextView) view.findViewById(R.id.tv_lagna_nakshatra);
        llTwoCard = (LinearLayout) view.findViewById(R.id.ll_two_card);

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
                if (!CUtils.isConnectedWithInternet(getActivity())) {
                    MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                            .getLayoutInflater(), getActivity(), typeface);
                    mct.show(getActivity().getResources().getString(R.string.no_internet));
                } else {
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
                if (!CUtils.isConnectedWithInternet(getActivity())) {
                    MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                            .getLayoutInflater(), getActivity(), typeface);
                    mct.show(getActivity().getResources().getString(R.string.no_internet));
                } else {
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
    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_40_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_40_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S40");
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
                topAdData = CUtils.getSlotData(adList, "40");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * set Calculations for lagna table data
     * */
    public String setCalculations(BeanPlace beanPlace, Date date) {
        String currentLalitude, currentLongitude, timeZone, timeZoneId;
        if (beanPlace != null) {
            currentLalitude = beanPlace.getLatitude();
            currentLongitude = beanPlace.getLongitude();
            timeZone = beanPlace.getTimeZone();
            timeZoneId = String.valueOf(beanPlace.getTimeZoneId());

        } else {
            currentLalitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
            currentLongitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
            timeZone = CUtils.getStringData(getActivity(), CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
            timeZoneId = CUtils.getStringData(getActivity(), CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);

        }
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            date = calendar.getTime();
        }

        //Save calender value in shared pref. for further use
        long millis = calendar.getTimeInMillis();
        CUtils.saveLongData(getActivity(), CGlobalVariables.calenderCurrentTime, millis);

        LagnaTableCalculation calculation = new LagnaTableCalculation(date, CUtils.getLanguageKey(LANGUAGE_CODE), currentLalitude, currentLongitude, timeZone, timeZoneId);
        return LagnaTableCalculation.getStartEndMuhuratJson(calculation.getLagnaTable(), "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        flag = true;
        mDateFormat = DateFormat.getMediumDateFormat(activity);
        super.onCreate(savedInstanceState);
    }

    public static LagnaInputFragment newInstance(String text) {

        // Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        LagnaInputFragment f = new LagnaInputFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    private void addRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
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

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }

    public void updateBirthPlace(BeanPlace beanPlace) {
        this.beanPlace = getBeanObj(beanPlace);
        ((InputPanchangActivity) activity).beanPlace = this.beanPlace;
        //this.beanHoroPersonalInfo.setPlace(beanPlace);
        txtPlaceName.setText(this.beanPlace.getCityName().trim());

        try {
            if (beanDateTime == null) {
                Calendar calendar = Calendar.getInstance();
                beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
            }
            updateBirthDate(beanDateTime, this.beanPlace);
            panchang.updateLayout(10);// 10 means lagna

        } catch (Exception ex) {

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

        Date date = beanDateTime.getCalender().getTime();
        getPanchangData(date, language, beanPlace);
        setData(beanPlace, date);
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
                if (!CUtils.isConnectedWithInternet(getActivity())) {
                    MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                            .getLayoutInflater(), getActivity(), typeface);
                    mct.show(getActivity().getResources().getString(R.string.no_internet));
                } else {
                    mDateTimePicker.clearFocus();
                    mDateTimeDialog.dismiss();
                    CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
                    // System.out.println(beanDateTime.getDay());
                }
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

    /**
     * @param packageName
     */
    public void shareContentData(String packageName) {

        whatsAppData = "";

        String enter1 = "\n";
        String enter2 = "\n\n";

        String heading1 = "🚩श्री गणेशाय नम:🚩 " + enter1;
        String heading = heading1;


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

        whatsAppData = whatsAppData.concat(getDataToShare(enter1, enter2));

        CUtils.shareData(activity, whatsAppData, packageName, activity.getResources().getString(R.string.bhadra));

    }

    /**
     * get data to share
     *
     * @param singleSpace
     * @param doubleSpace
     * @return
     */
    private String getDataToShare(String singleSpace, String doubleSpace) {
        String titleImage = "🔅 ";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(giveMeSpace(4));

        Festivalapidatum lagnaListData = null;

        if (beanLagnaTable != null) {
            stringBuilder.append(getActivity().getResources().getString(R.string.sunrise_at) + ": " + beanLagnaTable.getFestivalapidata2().getSunrise() + doubleSpace);
            stringBuilder.append((getActivity().getResources().getString(R.string.current_lagna) + giveMeSpace(2) + tvRashiLagnaName.getText().toString() +
                    giveMeSpace(2) + tvLagnaNatureCurrent.getText().toString() + singleSpace +
                    tvLagnaStarttime.getText().toString() + giveMeSpace(2) + tvLagnaEndtime.getText().toString()) + doubleSpace);

            stringBuilder.append((getActivity().getResources().getString(R.string.lagna_at_sunrise) + giveMeSpace(2) + tvLagnaRashiName.getText().toString() +
                    giveMeSpace(2) + tvLagnaNatureSunRise.getText().toString() + singleSpace +
                    tvLagnaDegree.getText().toString()) /*+ " " + tvNakshatra.getText().toString()*/ + doubleSpace);

            if (beanLagnaTable != null && beanLagnaTable.getFestivalapidata() != null && beanLagnaTable.getFestivalapidata().size() > 0) {
                for (int iterator = 0; iterator < beanLagnaTable.getFestivalapidata().size(); iterator++) {

                    lagnaListData = beanLagnaTable.getFestivalapidata().get(iterator);

                    stringBuilder.append(titleImage);
                    //set
                    stringBuilder.append(lagnaListData.getLagnaName() + giveMeSpace(2) + lagnaListData.getLagnaNature() + singleSpace);
                    stringBuilder.append(getContext().getResources().getString(R.string.start) + ": " + CUtils.getTimeInFormate(lagnaListData.getLagnaStart()).replace("+", getContext().getString(R.string.tomorrow_label) + " ")
                            + giveMeSpace(2)
                            + getContext().getResources().getString(R.string.end) + ": " + CUtils.getTimeInFormate(lagnaListData.getLagnaEnd()).replace("+", getContext().getString(R.string.tomorrow_label) + " ") + singleSpace);
                    stringBuilder.append(giveMeSpace(1) + singleSpace);
                }
            }
        }
        return stringBuilder.toString();
    }


    private String giveMeSpace(int num) {

        String space = "";

        for (int i = 1; i <= num; i++) {
            space = space + " ";
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

                flag = true;
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

    /**
     * set TypeFace Of View
     */
    private void setTypeFaceOfView() {
        tvDatePicker.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        btnCurrentDate.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        txtPlaceName.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        txtPlaceDetail.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvSunRiseAt.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvCurrentlagna.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvRashiLagnaName.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvLagnaNatureCurrent.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvLagnaStarttime.setTypeface(((InputPanchangActivity) activity).regularTypeface);
        tvLagnaEndtime.setTypeface(((InputPanchangActivity) activity).regularTypeface);
        tvLagnaAtSunRise.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvLagnaNatureSunRise.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvLagnaRashiName.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
        tvLagnaDegree.setTypeface(((InputPanchangActivity) activity).regularTypeface);
    }

    /**
     * @param beanPlace
     */
    private void setData(BeanPlace beanPlace, Date date) {
        showProgressBar();
        parsingDataAfterResponse(setCalculations(beanPlace, date));
        hideProgressBar();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (beanLagnaTable != null) {
            setLagnaAdapter();
        }
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static long getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * set Lagna Adapter
     */
    private void setLagnaAdapter() {
            if (beanLagnaTable != null) {
                Festivalapidata2 festivalapidata2 = beanLagnaTable.getFestivalapidata2();
                if (festivalapidata2 != null) {
                    tvSunRiseAt.setText(getContext().getResources().getString(R.string.sunrise_at) + ": " + CUtils.convertTimeToAmPm(model.getSunRise()));

                    Calendar calendar = Calendar.getInstance();
                    long currentTime = calendar.getTimeInMillis();
                    calendar.setTimeInMillis(currentTime);
                    Date date = calendar.getTime();
                    int mHour = calendar.get(Calendar.HOUR);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    long startTimee = 0;
                    long endTimee = 0;
                    // current lagna
                    for (int iterator = 0; iterator < beanLagnaTable.getFestivalapidata().size(); iterator++) {

                        String lagnaRashiName = beanLagnaTable.getFestivalapidata().get(iterator).getLagnaName();
                        String lagnaStartTime = beanLagnaTable.getFestivalapidata().get(iterator).getLagnaStart();
                        String lagnaEndTime = beanLagnaTable.getFestivalapidata().get(iterator).getLagnaEnd();

                        String startTime[] = lagnaStartTime.split(":");
                        String endTime[] = lagnaEndTime.split(":");

                        int startHour = Integer.parseInt(startTime[0]);
                        int startMinute = Integer.parseInt(startTime[1]);
                        int startSecond = Integer.parseInt(startTime[2]);
                        int endHour = Integer.parseInt(endTime[0]);
                        int endMinute = Integer.parseInt(endTime[1]);
                        int endSecond = Integer.parseInt(endTime[2]);

                        // Normalize start, end, and current times
                        Calendar cStartTime = Calendar.getInstance();
                        cStartTime.set(Calendar.HOUR_OF_DAY, startHour % 24);
                        cStartTime.set(Calendar.MINUTE, startMinute);
                        cStartTime.set(Calendar.SECOND,startSecond);

                        Calendar cEndTime = Calendar.getInstance();
                        cEndTime.set(Calendar.HOUR_OF_DAY, endHour % 24);
                        cEndTime.set(Calendar.MINUTE, endMinute);
                        cEndTime.set(Calendar.SECOND,endSecond);

                        // if hour is greater than 24 then subtract 24 from hour to get current hour.
                        if(startHour >= 24){
                            cStartTime.add(Calendar.DAY_OF_MONTH,-1);
                            cStartTime.set(Calendar.HOUR_OF_DAY, startHour - 24);
                        }
                        if(endHour >=24){
                            cEndTime.add(Calendar.DAY_OF_MONTH,1);
                            cEndTime.set(Calendar.HOUR_OF_DAY, endHour - 24);
                        }
                        /*if (Integer.parseInt(startTime[0]) < 24 &&
                                Integer.parseInt(endTime[0]) > 24 &&
                                mHour < 11) {
                            startTimee = getDate(year, month, day - 1, Integer.parseInt(startTime[0]) >= 24 ? Integer.parseInt(startTime[0]) - 24 : Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
                            endTimee = getDate(year, month, day, Integer.parseInt(endTime[0]) >= 24 ? Integer.parseInt(endTime[0]) - 24 : Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
                        }
                        // if start time is less then 24 hour and current time is over 24
                        else if (Integer.parseInt(startTime[0]) < 24 &&
                                Integer.parseInt(endTime[0]) > 24 &&
                                mHour < 11) {
                            startTimee = getDate(year, month, day, Integer.parseInt(startTime[0]) >= 24 ? Integer.parseInt(startTime[0]) - 24 : Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
                            endTimee = getDate(year, month, day + 1, Integer.parseInt(endTime[0]) >= 24 ? Integer.parseInt(endTime[0]) - 24 : Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
                        }
                        // if start time is less then 24 hour and current time is under 24
                        else {
                            startTimee = getDate(year, month, day, Integer.parseInt(startTime[0]) >= 24 ? Integer.parseInt(startTime[0]) - 24 : Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
                            endTimee = getDate(year, month, day, Integer.parseInt(endTime[0]) >= 24 ? Integer.parseInt(endTime[0]) - 24 : Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
                        }*/


                        if (currentTime > cStartTime.getTimeInMillis() && currentTime < cEndTime.getTimeInMillis()) {
                            tvRashiLagnaName.setText(lagnaRashiName);
                            tvLagnaStarttime.setText(getContext().getResources().getString(R.string.start) + ": " + CUtils.getTimeInFormate(lagnaStartTime).replace("+", ""));
                            tvLagnaEndtime.setText(getContext().getResources().getString(R.string.end) + ": " + CUtils.getTimeInFormate(lagnaEndTime).replace("+", ""));
                            setLagnaNatureAndColor(iterator, llCurrentLagnaNature, ivLagnaNatureCurrent, tvLagnaNatureCurrent);
                            tvLagnaNatureCurrent.setText(beanLagnaTable.getFestivalapidata().get(iterator).getLagnaNature());
                            break;
                        }
                    }

                    try{
                        //Lagna at sun rise
                        tvLagnaRashiName.setText(festivalapidata2.getLagnavalue());
                        setLagnaNatureAndColor(0, llSunRiseLagnaNature, ivLagnaNatureSunRise, tvLagnaNatureSunRise);
                        if(beanLagnaTable.getFestivalapidata() != null){
                            Festivalapidatum festivalapidatum = beanLagnaTable.getFestivalapidata().get(0);
                            tvLagnaNatureSunRise.setText(festivalapidatum.getLagnaNature());
                        }
                        tvLagnaDegree.setText(festivalapidata2.getLagnaDeg() + (char) 0x00B0 + festivalapidata2.getLagnaMin() + "\u2032" + festivalapidata2.getLagnaSec() + "\u2033");
                    }catch (Exception e){
                        Log.e("ForPanchang1","Exception="+e.toString());
                    }

                    if (beanPlace != null) {
                        txtPlaceName.setText(beanPlace.getCityName().trim());
                        txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
                    }

                    //if (model != null)
                        //tvNakshatra.setText(model.getNakshatraValue());
                    mRecyclerView.setAdapter(new CustomAdapterforLagna(activity, beanLagnaTable.getFestivalapidata()));
                    mRecyclerView.setFocusable(false);
                }
            }
    }

    private void setLagnaNatureAndColor(int iterator, LinearLayout llView, ImageView ivView, TextView tvView) {
        if (beanLagnaTable.getFestivalapidata().get(iterator).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_MOVABLE)) {
            llView.setBackgroundColor(getContext().getResources().getColor(R.color.bg_movable));
            ivView.setImageResource(R.drawable.movable);
            tvView.setTextColor(getContext().getResources().getColor(R.color.color_text_movable));
        } else if (beanLagnaTable.getFestivalapidata().get(iterator).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_FIXED)) {
            llView.setBackgroundColor(getContext().getResources().getColor(R.color.bg_fixed));
            ivView.setImageResource(R.drawable.fixed);
            tvView.setTextColor(getContext().getResources().getColor(R.color.color_text_fixed));
        } else if (beanLagnaTable.getFestivalapidata().get(iterator).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_DUAL)) {
            llView.setBackgroundColor(getContext().getResources().getColor(R.color.bg_dual));
            ivView.setImageResource(R.drawable.common);
            tvView.setTextColor(getContext().getResources().getColor(R.color.color_text_dual));
        }
    }


    /**
     * @param beanPlace
     * @return
     */
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

    /**
     * @param response
     */
    private void parsingDataAfterResponse(String response) {
        try {
            Gson gson = new Gson();
            beanLagnaTable = gson.fromJson(response, BeanLagnaTable.class);
            if (beanLagnaTable != null) {
                setLagnaAdapter();
            }
        } catch (Exception e) {
            Log.e("ForPanchang1", "parsingDataAfterResponse  Exception="+e);
        }
    }

    /**
     * @param _datePan
     * @param language
     * @param beanPlace
     */
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
        String cityName = beanPlace1.getCityName();

        if (activity != null) {
            String key = CUtils.getPanchangKey(_datePan, cityName, LANGUAGE_CODE);
            model = CUtils.getPanchangObject(activity, key);
            if (model == null) {
                AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, cityId, language, lat, lng, timeZone, timeZoneString);
                model = calculation.getPanchang();
            }

            if (activity != null && CUtils.checkCurrentDate(_datePan)) {
                CUtils.savePanchangObject(activity, key, model);
            }
        }
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(getActivity(), typeface);
            pd.setCanceledOnTouchOutside(false);
        }
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
