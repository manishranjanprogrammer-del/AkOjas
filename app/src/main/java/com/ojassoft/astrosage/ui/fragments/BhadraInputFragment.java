package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.CustomAdapterPanchaknBhadra;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.FestDataDetail;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.networkcall.VolleyServerRequest;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;
import com.ojassoft.astrosage.utils.VolleySingleton;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*@author Ankit
 */
public class BhadraInputFragment extends Fragment implements DateTimePicker.DateWatcher, OnVolleyResultListener {

    private View view;
    private java.text.DateFormat mDateFormat;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    LinearLayout layPlaceHolder;
    // public Typeface typeface;
    ImageView whatsAppIV;
    TextView textViewCurrent;
    TextView tvDatePicker;
    String language;
    public int SELECTED_MODULE;
    BeanPlace beanPlace;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    TextView txtPlaceName, txtPlaceDetail;
    String city_Id = "1261481";
    Button btnPreviousDate, btnNextDate;
    TextView btnCurrentDate;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String whatsAppData;
    int spaceBetweenTexts = 2;
    IPanchang panchang;
    LinearLayout llCustomAdv = null;
    BeanDateTimeForPanchang beanDateTime;
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    ScrollView scrollView;
    Activity activity;
    private ArrayList<Object> bhadraDataList;
    private RequestQueue queue;
    String[] monthArray;
    String[] weekArray;
    Typeface typeface;
    private TextView mtxtStartTime, mtxtEndTime;
    private TextView currentMonthTV;
    String[] fullMonthName;
    private CustomProgressDialog pd;
    private String previousCityId = "";
    private int previousMonth;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    public static BhadraInputFragment newInstance(String text) {
        BhadraInputFragment panchakInputFragment = new BhadraInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", text);
        panchakInputFragment.setArguments(bundle);
        return panchakInputFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDateFormat = DateFormat.getMediumDateFormat(activity);
        // if (view == null) {
        view = inflater.inflate(R.layout.lay_frag_panchak_bhadra, container, false);
        // }
        //add NEw
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;
        //Add advertisment in footer 10-Dec-2015
        //if (llCustomAdv == null) {
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((InputPanchangActivity) activity).regularTypeface, "SPNRA"));
        // }
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        beanHoroPersonalInfo = new BeanHoroPersonalInfo();

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        language = CUtils.getLanguageKey(CUtils.getLanguageCodeFromPreference(activity));
        monthArray = getActivity().getResources().getStringArray(R.array.month_short_name_list);
        weekArray = getActivity().getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);
        fullMonthName = getActivity().getResources().getStringArray(R.array.MonthName);

        setLayRef(view);
        Date _datePan = new Date();
        Calendar calendar = Calendar.getInstance();
        if (((InputPanchangActivity) activity).calendar != null) {
            calendar = ((InputPanchangActivity) activity).calendar;
        }

        if (activity instanceof InputPanchangActivity) {
            whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
        }

        BeanPlace bP = CUtils.getBeanPalce(activity);
        BeanHoroPersonalInfo bHPInfo = CUtils.getBeanHoroPersonalInfo(activity);
        if (bP != null || bHPInfo != null) {
            this.beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(activity);
            if (((InputPanchangActivity) activity).beanPlace == null) {
                this.beanPlace = CUtils.getBeanPalce(activity);
                txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));

            } else {
                this.beanPlace = ((InputPanchangActivity) activity).beanPlace;
                txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));

            }

            this.city_Id = String.valueOf(this.beanPlace.getCityId());
            txtPlaceName.setText(beanPlace.getCityName().trim());

            BeanDateTimeForPanchang beanDateTimeFromPref = CUtils.getDateTimeForPanchang(activity);
            if (beanDateTimeFromPref != null) {
                beanDateTime.setCalender(beanDateTimeFromPref.getMonth(), beanDateTimeFromPref.getDay(), beanDateTimeFromPref.getYear());
                beanDateTime.setDay(beanDateTimeFromPref.getDay());
                beanDateTime.setMonth(beanDateTimeFromPref.getMonth());
                beanDateTime.setYear(beanDateTimeFromPref.getYear());

                //CUtils.saveDateTimeForPanchang(activity,beanDateTime,false);
            }
        } else {
            if (((InputPanchangActivity) activity).beanPlace != null) {
                this.beanPlace = ((InputPanchangActivity) activity).beanPlace;
                this.city_Id = String.valueOf(this.beanPlace.getCityId());
                txtPlaceName.setText(beanPlace.getCityName().trim());
                txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));

            }
        }
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

        updateBirthDate(beanDateTime);

        return view;

        //end New
    }

    private void addRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(this.getContext().getDrawable(R.drawable.drawable_divider));
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private String getFormatedTextToShowDate(BeanDateTimeForPanchang beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + " - " + months[beanDateTime.getMonth()] + " - " + beanDateTime.getYear();
        return strDateTime;
    }
    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_38_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_38_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S38");
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
                topAdData = CUtils.getSlotData(adList, "38");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setLayRef(View view) {
        try {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            addRecyclerView();
            btnCurrentDate = (TextView) view.findViewById(R.id.btnCurrentDate);
            btnCurrentDate.setText(getActivity().getResources().getString(R.string.current_day));
            btnCurrentDate.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
            btnCurrentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentDayData();
                }
            });

            btnPreviousDate = view.findViewById(R.id.btnPrevious);
            btnPreviousDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPreviousDayData();
                }
            });

            btnNextDate = view.findViewById(R.id.btnNext);
            btnNextDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNextDayData();
                }
            });

            currentMonthTV = (TextView) view.findViewById(R.id.currentMonthTV);
            FontUtils.changeFont(activity, currentMonthTV, AppConstants.FONT_ROBOTO_MEDIUM);

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
            layPlaceHolder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openSearchPlace(beanHoroPersonalInfo.getPlace());
                }
            });
            txtPlaceName = (TextView) view.findViewById(R.id.textViewPlaceName);
            txtPlaceName.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
            txtPlaceDetail = (TextView) view.findViewById(R.id.textViewPlaceDetails);
            txtPlaceDetail.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);
            textViewCurrent = (TextView) view.findViewById(R.id.tv_panchak_bhadra);
            textViewCurrent.setText(getActivity().getResources().getString(R.string.bhadra_vishti_karana)
                    + " - "
                    + fullMonthName[beanDateTime.getMonth()]);
            textViewCurrent.setTypeface(((InputPanchangActivity) activity).mediumTypeface);

            mtxtStartTime = (TextView) view.findViewById(R.id.tv_start_time);
            mtxtStartTime.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);

            mtxtEndTime = (TextView) view.findViewById(R.id.tv_end_time);
            mtxtEndTime.setTypeface(((InputPanchangActivity) activity).robotMediumTypeface);

        } catch (Exception ex) {
            //Log.e("setLayRef: ", ex.getMessage());
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
        calendar.add(Calendar.MONTH, -1);
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        updateBirthDate(beanDateTime);

    }

    public void getCurrentDayData() {
        Calendar calendar = Calendar.getInstance();
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        updateBirthDate(beanDateTime);

    }

    public void getNextDayData() {
        Calendar calendar = beanDateTime.getCalender();
        calendar.add(Calendar.MONTH, 1);
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);
        updateBirthDate(beanDateTime);
    }


    public void updateBirthDate(BeanDateTimeForPanchang beanDateTime) {

        //Toast.makeText(this, beanDateTime.getYear(), Toast.LENGTH_SHORT).show();

        this.beanDateTime.setYear(beanDateTime.getYear());
        this.beanDateTime.setMonth(beanDateTime.getMonth());
        this.beanDateTime.setDay(beanDateTime.getDay());
        this.beanDateTime.setCalender(beanDateTime.getMonth(), beanDateTime.getDay(), beanDateTime.getYear());
        tvDatePicker.setText(getFormatedTextToShowDate(beanDateTime));

        currentMonthTV.setText(fullMonthName[(beanDateTime.getMonth())] + " " + beanDateTime.getYear());
        textViewCurrent.setText(getActivity().getResources().getString(R.string.bhadra_vishti_karana)
                + " - "
                + fullMonthName[beanDateTime.getMonth()]);

        /*set kundli date in newKundliSelectedDate*/
        if (tvDatePicker.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = tvDatePicker.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getActivity().getResources().getStringArray(R.array.month_short_name_list);
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

        updateDateButtonText();
    }

    private void updateDateButtonText() {
        bhadraDataList = null;
        Date date = beanDateTime.getCalender().getTime();
        setLayout(date, city_Id, language, this.beanPlace);
    }

    private void setLayout(Date _datePan, String cityId, String language, BeanPlace beanPlace) {
        initViewPanchangTimings(cityId);

    }

    private void initViewPanchangTimings(String cityId) {
        try {
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

                if (beanPlace != null) {
                    txtPlaceName.setText(beanPlace.getCityName().trim());
                    txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
                }


                timeZone = beanPlace.getTimeZone();
                timeZoneString = beanPlace.getTimeZoneString();
            }

            checkCachedData(cityId);

        } catch (Exception ex) {
            //Log.e("EXCEPTION IS::", ex.getMessage());
        }
    }

    /**
     * @param cityId
     */
    private void checkCachedData(String cityId) {
        String url = CGlobalVariables.badhraApiUrl;
        Cache cache = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null && cityId.equalsIgnoreCase(previousCityId) && previousMonth == (beanDateTime.getMonth() + 1)) {    // cache data
            previousCityId = cityId;
            previousMonth = beanDateTime.getMonth() + 1;
            try {
                String saveData = new String(entry.data, "UTF-8");
                if (bhadraDataList != null) {
                    mRecyclerView.setAdapter(new CustomAdapterPanchaknBhadra(activity, bhadraDataList, monthArray, weekArray));
                } else {
                    callApiforBhadra(cityId);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {    // Api hit
            // Cached response doesn't exists. Make network call here
            callApiforBhadra(cityId);
        }
    }

    /**
     * @param cityId
     */
    private void callApiforBhadra(String cityId) {
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                    .getLayoutInflater(), getActivity(), typeface);
            mct.show(getActivity().getResources().getString(R.string.no_internet));
        } else {
            getBhadraDetails(cityId);
        }
    }

    /**
     * get panchak detail
     */
    public void getBhadraDetails(final String cityId) {
        showProgressBar();
        final String url = CGlobalVariables.badhraApiUrl;
        new VolleyServerRequest(getActivity(), BhadraInputFragment.this, url, getParams(cityId));
    }

    /**
     * @param cityId
     * @return
     */
    public HashMap<String, String> getParams(String cityId) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
        headers.put("language", language);
        headers.put("date", "1-" + (beanDateTime.getMonth() + 1) + "-" + (beanDateTime.getYear()));
        if (beanPlace != null) {
            headers.put("lid", String.valueOf(beanPlace.getCityId()));
        } else {
            headers.put("lid", cityId);
        }
        headers.put("isapi", "1");
        return headers;
    }

    //calendar work
    public void showCustomDatePickerDialogAboveHoneyComb(BeanDateTimeForPanchang beanDateTime) {
        final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {
            @Override
            public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
                beanDateTime.setYear(year);
                beanDateTime.setMonth(monthOfYear);
                beanDateTime.setDay(dayOfMonth);
                beanDateTime.setHour(0);
                beanDateTime.setMin(0);
                beanDateTime.setSecond(0);
                updateBirthDate(beanDateTime);
            }
        };


        final MyDatePickerDialog mTimePicker = new MyDatePickerDialog(activity, R.style.AppCompatAlertDialogStyle, myDateSetListener, beanDateTime.getMonth(), (beanDateTime.getDay()), (beanDateTime.getYear()), false);
        mTimePicker.setCanceledOnTouchOutside(false);
        mTimePicker.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_today_black_icon));
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

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
            beanDateTime.setYear(year);
            beanDateTime.setMonth(monthOfYear);
            beanDateTime.setDay(dayOfMonth);
            beanDateTime.setHour(0);
            beanDateTime.setMin(0);
            beanDateTime.setSecond(0);
            updateBirthDate(beanDateTime);
        }
    };

    private void showAndroidDatePicker(BeanDateTimeForPanchang beanDateTime) {
        final DatePickerDialog dg = new DatePickerDialog(activity, R.style.AppCompatAlertDialogStyle, mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());

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
        dg.onDateChanged(dg.getDatePicker(), year, month, day);

        if (Build.VERSION.SDK_INT > 23) {
            dg.setTitle("");
        } else {
            dg.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_today_black_icon));
            dg.setTitle(mDateFormat.format(mCalendar.getTime()));
        }


        /*This Code for set DatePicker Width full*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dg.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dg.show();
        dg.getWindow().setAttributes(lp);
        datePicker.setScaleX(1.1f);

        //This code for API 6 date Picker Color not like holo
        CUtils.applyStyLing(dg, activity);

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

        Button butOK = (Button) dg.findViewById(android.R.id.button1);
        Button butCancel = (Button) dg.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);
        butOK.setTypeface(((InputPanchangActivity) activity).mediumTypeface);
        butCancel.setTypeface(((InputPanchangActivity) activity).mediumTypeface);

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
                        //Log.e("Exception IS: ", ex.getMessage());
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

    @Override
    public void onDateChanged(Calendar c) {
        // TODO Auto-generated method stub

        BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setHour(c.get(Calendar.HOUR_OF_DAY));
        beanDateTime.setMin(c.get(Calendar.MINUTE));
        beanDateTime.setSecond(c.get(Calendar.SECOND));

        updateBirthDate(beanDateTime);

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
        getActivity().startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                /*if (resultCode == activity.RESULT_OK) {
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
        updateBirthPlace(place);
    }

    public void updateBirthPlace(BeanPlace beanPlace) {
        this.beanPlace = beanPlace;
        if (this.beanHoroPersonalInfo == null) {
            this.beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        }
        this.beanHoroPersonalInfo.setPlace(beanPlace);
        txtPlaceName.setText(beanPlace.getCityName().trim());
        txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));
        try {
            CUtils.savePlacePreference(activity, this.beanPlace, this.beanHoroPersonalInfo);
            this.city_Id = String.valueOf(beanPlace.getCityId());

            Date _datePan = new Date();
            if (beanDateTime == null) {
                Calendar calendar = Calendar.getInstance();
                beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
            }
            updateBirthDate(beanDateTime);
            panchang.updateLayout(8);//8 means Bhadra
        } catch (Exception ex) {
            //com.google.analytics.tracking.android.//Log.e(ex.getMessage());
        }
    }


    public void shareContentData(String packageName) {

        whatsAppData = "";

        String enter1 = "\n";
        String enter2 = "\n\n";
        String enter3 = "\n\n\n";

        String headingImage = "🚩";
        String titleImage = "☀ ";
        String contentImage = "🔅 ";
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

        whatsAppData = whatsAppData.concat(getDataToShare(enter1, enter2, enter3));

        CUtils.shareData(activity, whatsAppData, packageName, activity.getResources().getString(R.string.bhadra));

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
        // TODO Auto-generated method stub
        try {
            if (activity != null && isVisibleToUser) {

                // If we are becoming invisible, then...
                if (whatsAppIV != null) {
                    whatsAppIV.setVisibility(View.VISIBLE);
                }

                BeanDateTimeForPanchang beanDateTimeFromPref = CUtils
                        .getDateTimeForPanchang(activity);
                if (beanDateTimeFromPref != null) {
                    beanDateTime.setCalender(beanDateTimeFromPref.getMonth(), beanDateTimeFromPref.getDay(), beanDateTimeFromPref.getYear());
                    beanDateTime.setDay(beanDateTimeFromPref.getDay());
                    beanDateTime.setMonth(beanDateTimeFromPref.getMonth());
                    beanDateTime.setYear(beanDateTimeFromPref.getYear());
                    tvDatePicker.setText(getFormatedTextToShowDate(beanDateTime));
                }

                BeanHoroPersonalInfo bHPInfo = CUtils.getBeanHoroPersonalInfo(activity);
                if (bHPInfo != null) {
                    this.beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(activity);
                    if (activity instanceof InputPanchangActivity) {
                        if (((InputPanchangActivity) activity).beanPlace == null) {
                            this.beanPlace = CUtils.getBeanPalce(activity);
                            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));

                        } else {
                            this.beanPlace = ((InputPanchangActivity) activity).beanPlace;
                            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));

                        }
                    } else if (activity instanceof DetailedHoroscope) {
                        if (((DetailedHoroscope) activity).beanPlace == null) {
                            this.beanPlace = CUtils.getBeanPalce(activity);
                            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));

                        } else {
                            this.beanPlace = ((DetailedHoroscope) activity).beanPlace;
                            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));

                        }
                    }

                    // this.beanPlace = CUtils.getBeanPalce(activity);

                    this.city_Id = String.valueOf(this.beanPlace.getCityId());
                    txtPlaceName.setText(beanPlace.getCityName().trim());

                } else {
                    if (activity instanceof InputPanchangActivity) {
                        if (((InputPanchangActivity) activity).beanPlace != null) {
                            this.beanPlace = ((InputPanchangActivity) activity).beanPlace;
                            this.city_Id = String.valueOf(this.beanPlace.getCityId());
                            txtPlaceName.setText(beanPlace.getCityName().trim());
                            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));
                        }
                    } else if (activity instanceof DetailedHoroscope) {
                        if (((DetailedHoroscope) activity).beanPlace != null) {
                            this.beanPlace = ((DetailedHoroscope) activity).beanPlace;
                            this.city_Id = String.valueOf(this.beanPlace.getCityId());
                            txtPlaceName.setText(beanPlace.getCityName().trim());
                            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));
                        }
                    }

                }

                updateBirthDate(beanDateTime);
            }
        } catch (Exception ex) {
            //
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    /**
     * get data to share
     *
     * @param singleSpace
     * @param doubleSpace
     * @param tripleSpace
     * @return
     */
    private String getDataToShare(String singleSpace, String doubleSpace, String tripleSpace) {
        String titleImage = "🔅 ";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(giveMeSpace(4));
        stringBuilder.append(getActivity().getResources().getString(R.string.bhadra_vishti_karana) + " - " + monthArray[beanDateTime.getMonth()] + doubleSpace);
        stringBuilder.append((getActivity().getResources().getString(R.string.start_time) + giveMeSpace(2) + "-" + giveMeSpace(2) + getActivity().getResources().getString(R.string.end_times) + doubleSpace));
        if (bhadraDataList != null && bhadraDataList.size() > 0) {
            for (int iterator = 0; iterator < bhadraDataList.size(); iterator++) {
                stringBuilder.append(titleImage);
                //set start date
                stringBuilder.append((CUtils.getDateInFormate(((FestDataDetail) bhadraDataList.get(iterator)).getFestStartDate(), weekArray, monthArray)));
                stringBuilder.append(giveMeSpace(1));
                //set start time
                stringBuilder.append(CUtils.getTimeInFormate(((FestDataDetail) bhadraDataList.get(iterator)).getStarthr(), ((FestDataDetail) bhadraDataList.get(iterator)).getStartmin()).toUpperCase());
                stringBuilder.append(giveMeSpace(2));

                stringBuilder.append(" - ");

                stringBuilder.append(giveMeSpace(2));
                //set end date
                stringBuilder.append((CUtils.getDateInFormate(((FestDataDetail) bhadraDataList.get(iterator)).getFestEndDate(), weekArray, monthArray)));
                stringBuilder.append(giveMeSpace(1));
                //set end time
                stringBuilder.append(CUtils.getTimeInFormate(((FestDataDetail) bhadraDataList.get(iterator)).getEndhr(), ((FestDataDetail) bhadraDataList.get(iterator)).getEndmin()).toUpperCase());

                stringBuilder.append(doubleSpace);
            }
        } else {
            stringBuilder.append(("-" + giveMeSpace(5) + "-"));
        }
        return stringBuilder.toString();
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

    @Override
    public void onVolleySuccess(String result, Cache cache) {
        hideProgressBar();
        parseApiResponse(result);
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }

    /**
     * Parse API response
     *
     * @param response
     */
    private void parseApiResponse(String response) {
        if (response != null && !response.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray("festivalapidata");
                Gson gson = new Gson();
                List<FestDataDetail> festDataDetails = Arrays.asList(gson.fromJson(jsonArray.toString(), FestDataDetail[].class));
                if (bhadraDataList == null) {
                    bhadraDataList = new ArrayList<>();
                } else {
                    bhadraDataList.clear();
                }
                bhadraDataList.addAll(festDataDetails);

                if (bhadraDataList != null) {
                    mRecyclerView.setAdapter(new CustomAdapterPanchaknBhadra(activity, bhadraDataList, monthArray, weekArray));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
