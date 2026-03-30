package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.ojassoft.astrosage.beans.BeanNameValueCardListData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomAdapter;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.ExpandableHeightListView;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.google.analytics.tracking.android.Log;

public class PanchangInputFragment extends Fragment implements DateWatcher {

    Activity activity;

    ImageView whatsAppIV;

    CardView cardViewDailyPanchang;
    CardView cardViewSunAndMoonCalculation;
    CardView cardViewHinduMonthAndYear;
    CardView cardViewAuspiciousInauspiciousTimings;
    CardView cardViewDishaShoola;
    CardView cardViewChandrabalamAndTarabalam;
    ProgressBar progressBar;
    private java.text.DateFormat mDateFormat;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    private DatePicker datePicker;


    TextView tvDatePicker;
    DialogFragment dateFragment;

    private static final String DATE_FORMAT = "MM-dd-yyyy";


    AajKaPanchangModel model;

    String language;

    // CONSTANTS
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    public int SELECTED_MODULE;

    BeanPlace beanPlace;

    //BeanHoroPersonalInfo beanHoroPersonalInfo;

    LinearLayout layPlaceHolder;
    TextView txtPlaceName, txtPlaceDetail;

    String city_Id = "1261481";

    ImageView imgShareOnWhatsApp;

    Button btnPreviousDate, btnNextDate;
    TextView btnCurrentDate;
    BeanDateTimeForPanchang beanDateTime;

    String whatsAppData;
    int spaceBetweenTexts = 2;
    int spaceBetweenTextsWithtag = 3;
    Date date;

    IPanchang panchang;
    LoadPanchangData loadPanchangData;

    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDateFormat = DateFormat.getMediumDateFormat(activity);
        try {
            view = inflater.inflate(R.layout.lay_frag_panchang, container, false);
            LinearLayout placeDetailLayout = (LinearLayout) view.findViewById(R.id.place_detail_layout);

            if (activity instanceof InputPanchangActivity) {
                //placeDetailLayout.setVisibility(View.GONE);
                whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
            } else {
                placeDetailLayout.setVisibility(View.VISIBLE);

            }

            SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;

            llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
            llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((BaseInputActivity) activity).regularTypeface, "SPNPN"));

            LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode();

            language = CUtils.getLanguage(CUtils.getLanguageCodeFromPreference(activity));

            if (activity instanceof InputPanchangActivity) {
                beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
            } else if (activity instanceof DetailedHoroscope) {
                beanPlace = getBeanObj(((DetailedHoroscope) activity).beanPlace);
            }

            setLayRef(view);
            topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
            initAdClickListner();
            getData();
            if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
                setTopAdd(topAdData);
            }
            //Date _datePan = new Date();
            Calendar calendar = Calendar.getInstance();
            if (activity instanceof InputPanchangActivity) {
                if (((InputPanchangActivity) activity).calendar != null) {
                    // calendar.setTime(((InputPanchangActivity) activity).date);
                    calendar = ((InputPanchangActivity) activity).calendar;
                    int month = calendar.get(Calendar.MONTH);
                }
            } else if (activity instanceof DetailedHoroscope) {
                if (((DetailedHoroscope) activity).calendar != null) {
                    // calendar.setTime(((InputPanchangActivity) activity).date);
                    calendar = ((DetailedHoroscope) activity).calendar;
                    int month = calendar.get(Calendar.MONTH);
                }
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


            updateBirthDate(beanDateTime, beanPlace);
        }catch(Exception e){
        }
        return view;
    }

    public static PanchangInputFragment newInstance(String text) {

        //Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        PanchangInputFragment f = new PanchangInputFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    private void setLayRef(View view) {
        try {
            btnCurrentDate = (TextView) view.findViewById(R.id.btnCurrentDate);
            btnCurrentDate.setText(getResources().getString(R.string.current_day));
            btnCurrentDate.setTypeface(((BaseInputActivity) activity).mediumTypeface);
            btnCurrentDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getCurrentDayData();
                }
            });

            btnPreviousDate = (Button) view.findViewById(R.id.btnPreviousDate);
            btnPreviousDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getPreviousDayData();
                }
            });

            btnNextDate = (Button) view.findViewById(R.id.btnNextDate);
            btnNextDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getNextDayData();
                }
            });


            // ll_card_container = (LinearLayout) view.findViewById(R.id.ll_card_container);
            cardViewDailyPanchang = (CardView) view.findViewById(R.id.cardViewDailyPanchang);
            cardViewSunAndMoonCalculation = (CardView) view.findViewById(R.id.cardViewSunAndMoonCalculation);
            cardViewHinduMonthAndYear = (CardView) view.findViewById(R.id.cardViewHinduMonthAndYear);
            cardViewAuspiciousInauspiciousTimings = (CardView) view.findViewById(R.id.cardViewAuspiciousInauspiciousTimings);
            cardViewDishaShoola = (CardView) view.findViewById(R.id.cardViewDishaShoola);
            cardViewChandrabalamAndTarabalam = (CardView) view.findViewById(R.id.cardViewChandrabalamAndTarabalam);
            tvDatePicker = (TextView) view.findViewById(R.id.tvDatePicker);
            tvDatePicker.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            beanDateTime = new BeanDateTimeForPanchang();
            tvDatePicker.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setDate();
                }
            });

            layPlaceHolder = (LinearLayout) view.findViewById(R.id.layPlaceHolder);
            layPlaceHolder.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    openSearchPlace(beanPlace);
                }
            });
            /*if (placeTV != null) {
             *//*placeTV.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        openSearchPlace(beanHoroPersonalInfo.getPlace());
                    }
                });*//*
                placeTV.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);

            }*/

            txtPlaceName = (TextView) view.findViewById(R.id.textViewPlaceName);
            txtPlaceName.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
            txtPlaceDetail = (TextView) view.findViewById(R.id.textViewPlaceDetails);
            txtPlaceDetail.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);

            //CUtils.showAdvertisement(activity,(LinearLayout) view.findViewById(R.id.advLayout));

        } catch (Exception ex) {
        }
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_32_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_32_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S32");
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
                topAdData = CUtils.getSlotData(adList, "32");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        CUtils.hideMyKeyboard(activity);
        /*CUtils.showAdvertisement(activity,
                (LinearLayout) view.findViewById(R.id.advLayout));*/
        super.onResume();

    }

   /*@Override
   public void onPause() {
      super.onPause();
      CUtils.removeAdvertisement(activity,
            (LinearLayout) view.findViewById(R.id.advLayout));
   }*/

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                    // set place detail which returned from place activity it should
                    // be set, because in case of activity recreation user should
                    // get updated place value.
                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
                    updateBirthPlace(place);
                }
                break;
        }
    }

    public void updateAfterPlaceSelect(BeanPlace place) {
        //((InputPanchangActivity) activity).placeTV.setText(place.getCityName().trim());
        updateBirthPlace(place);
    }
   /*public void subActivityPlaceSearch(Intent data){

      Bundle bundle = data.getExtras();
      BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
      // set place detail which returned from place activity it should
      // be set, because in case of activity recreation user should
      // get updated place value.
      CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
      .setPlace(place);
      updateBirthPlace(place);

   }*/

    public void updateBirthPlace(BeanPlace beanPlace) {
        this.beanPlace = beanPlace;
        if (activity instanceof InputPanchangActivity) {
            ((InputPanchangActivity) activity).beanPlace = beanPlace;
        } else {
            ((DetailedHoroscope) activity).beanPlace = beanPlace;
        }
        //this.beanHoroPersonalInfo.setPlace(beanPlace);
        txtPlaceName.setText(beanPlace.getCityName().trim());
        txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
        try {

            //CUtils.saveBeanPalce(activity, beanPlace);
            //    CUtils.saveDateTimeForPanchang(activity,this.beanDateTime,true);
            //Toast.makeText(this,""+beanPlace.getCityId(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,""+beanPlace.getLongitude(), Toast.LENGTH_SHORT).show();
            this.city_Id = String.valueOf(beanPlace.getCityId());
            // Date _datePan = calendar.getTime();
            // setLayout(_datePan,city_Id,language,beanPlace);

            // Date _datePan =new Date();
            if (beanDateTime == null) {
                Calendar calendar = Calendar.getInstance();
                beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
                beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                beanDateTime.setMonth(calendar.get(Calendar.MONTH));
                beanDateTime.setYear(calendar.get(Calendar.YEAR));
            }


            updateBirthDate(beanDateTime, beanPlace);
            panchang.updateLayout(1);//0 means panchang
        } catch (Exception ex) {
            //Toast.makeText(this,""+ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            //Log.e(ex.getMessage().toString());
        }
    }


    public void updateBirthDate(BeanDateTimeForPanchang beanDateTime, BeanPlace beanPlace) {

        //Toast.makeText(this, beanDateTime.getYear(), Toast.LENGTH_SHORT).show();

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
            CUtils.setNewKundliSelectedMonth(monthInCount + 1);
        }

        if (activity instanceof InputPanchangActivity) {
            ((InputPanchangActivity) activity).beanDateTime = beanDateTime;
        }

        updateDateButtonText(beanPlace);
    }

    private String getFormatedTextToShowDate(BeanDateTimeForPanchang beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + " - " + months[beanDateTime.getMonth()] + " - " + beanDateTime.getYear();
        return strDateTime;
    }

    private void updateDateButtonText(BeanPlace beanPlace) {

        if (loadPanchangData != null && loadPanchangData.getStatus() == AsyncTask.Status.RUNNING) {
            loadPanchangData.cancel(true);
        }
        loadPanchangData = new LoadPanchangData(beanPlace);
        loadPanchangData.execute();
        //setLayout(date, city_Id, language, this.beanPlace);
    }

    /* private void setLayout(Date _datePan, String cityId, String language, BeanPlace beanPlace) {

         if (language.equals("")) {
             language = "en";
         }
         if (beanPlace != null) {
             cityId = String.valueOf(beanPlace.getCityId());
         }

         if (cityId.equals("")) {
             cityId = "1261481";
         }

         String lat = "0";
         String lng = "0";
         String timeZone = "0";
         String timeZoneString = "";

       *//*  if (beanPlace != null) {
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
        }*//*

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, cityId, language, lat, lng, timeZone, timeZoneString);
        model = calculation.getPanchang();
        if (activity != null && CUtils.checkCurrentDate(_datePan)) {
            String key = CUtils.getPanchangKey(_datePan, beanPlace.getCityName(), LANGUAGE_CODE);
            CUtils.savePanchangObject(activity, key, model);
        }


    }*/
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


    private void initViewChandrabalamAndTarabalam(View view) {
        try {

            TextView header = (TextView) view.findViewById(R.id.tvHeader);
            header.setText(getResources().getString(R.string.chandrabalam_and_tarabalam));
            header.setTypeface(((BaseInputActivity) activity).mediumTypeface);

            TextView tvSubHeader1 = (TextView) view.findViewById(R.id.tvSubHeader1);
            tvSubHeader1.setText(getResources().getString(R.string.tara_bala));
            tvSubHeader1.setTypeface(((BaseInputActivity) activity).mediumTypeface);

            TextView tvSubContent1 = (TextView) view.findViewById(R.id.tvSubContent1);
            tvSubContent1.setText(model.getTaraBala());
            // tvSubContent1.setTypeface(typeface);

            TextView tvSubHeader2 = (TextView) view.findViewById(R.id.tvSubHeader2);
            tvSubHeader2.setText(getResources().getString(R.string.chandra_bala));
            tvSubHeader2.setTypeface(((BaseInputActivity) activity).mediumTypeface);

            TextView tvSubContent2 = (TextView) view.findViewById(R.id.tvSubContent2);
            tvSubContent2.setText(model.getChandraBala());
            // tvSubContent2.setTypeface(typeface);

        } catch (Exception ex) {
        }
    }

    private void initViewDishaShoola(View view) {
        try {
            TextView tvHeaderDishaShoola = (TextView) view.findViewById(R.id.tvHeaderDishaShoola);
            tvHeaderDishaShoola.setText(getResources().getString(R.string.disha_shoola));
            tvHeaderDishaShoola.setTypeface(((BaseInputActivity) activity).mediumTypeface);

            String name[] = new String[]{getResources().getString(R.string.disha_shoola)};

            String value[] = new String[]{model.getDishaShoola()};

            List<BeanNameValueCardListData> data3 = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);

                data3.add(listData);
            }

            ExpandableHeightListView rvListDataDishaShoola = (ExpandableHeightListView) view.findViewById(R.id.rvListDataDishaShoola);
            rvListDataDishaShoola.setAdapter(new CustomAdapter(activity, data3,false));
            rvListDataDishaShoola.setExpanded(true);
            rvListDataDishaShoola.setFocusable(false);
        } catch (Exception ex) {
            //Log.e(ex.getMessage().toString());
        }
    }

    private void initViewAuspiciousInauspiciousTimings(View view) {
        try {

            TextView header = (TextView) view.findViewById(R.id.tvHeader);
            TextView tvSubHeader1 = (TextView) view.findViewById(R.id.tvSubHeader1);
            TextView tvSubHeader2 = (TextView) view.findViewById(R.id.tvSubHeader2);
            TextView tvSubHeader3 = (TextView) view.findViewById(R.id.tvSubHeader3);
            tvSubHeader3.setVisibility(View.GONE);

            header.setText(getResources().getString(R.string.auspicious_and_inauspicious));
            tvSubHeader1.setText(getResources().getString(R.string.auspicious_timings));
            tvSubHeader2.setText(getResources().getString(R.string.inauspicious_timings));
            // tvSubHeader3.setText(getResources().getString(R.string.disha_shoola));

            header.setTypeface(((BaseInputActivity) activity).mediumTypeface);
            tvSubHeader1.setTypeface(((BaseInputActivity) activity).mediumTypeface);
            tvSubHeader2.setTypeface(((BaseInputActivity) activity).mediumTypeface);
            // tvSubHeader3.setTypeface(((InputPanchangActivity) activity).mediumTypeface);

            String name1[] = new String[]{getResources().getString(R.string.abhijit)};

            String value1[] = new String[]{model.getAbhijitFrom()};

            String time1[] = new String[]{model.getAbhijitTo()};

            String name2[] = new String[]{getResources().getString(R.string.dushta_muhurtas),
                    getResources().getString(R.string.kantaka),
                    getResources().getString(R.string.yamaghanta),
                    getResources().getString(R.string.rahu_kaal),
                    getResources().getString(R.string.kulika),
                    getResources().getString(R.string.kalavela),
                    getResources().getString(R.string.yamaganda),
                    getResources().getString(R.string.gulika_kaal)};

            String value2[] = new String[]{model.getDushtaMuhurtasFrom(),
                    model.getKantaka_MrityuFrom(),
                    model.getYamaghantaFrom(),
                    model.getRahuKaalVelaFrom(),
                    model.getKulikaFrom(),
                    model.getKalavela_ArdhayaamFrom(),
                    model.getYamagandaVelaFrom(),
                    model.getGulikaKaalVelaFrom()};

            String time2[] = new String[]{model.getDushtaMuhurtasTo(),
                    model.getKantaka_MrityuTo(),
                    model.getYamaghantaTo(),
                    model.getRahuKaalVelaTo(),
                    model.getKulikaTo(),
                    model.getKalavela_ArdhayaamTo(),
                    model.getYamagandaVelaTo(),
                    model.getGulikaKaalVelaTo()};


            // String name3[] = new String[]{getResources().getString(R.string.disha_shoola)};

            // String value3[] = new String[]{model.getDishaShoola()};


            List<BeanNameValueCardListData> data1 = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name1.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name1[i]);
                listData.setValue(value1[i]);
                listData.setTime(time1[i]);
                data1.add(listData);
            }


            List<BeanNameValueCardListData> data2 = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name2.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name2[i]);
                listData.setValue(value2[i]);
                listData.setTime(time2[i]);
                data2.add(listData);
            }

           /* List<BeanNameValueCardListData> data3 = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name3.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name3[i]);
                listData.setValue(value3[i]);

                data3.add(listData);
            }*/

            ExpandableHeightListView rvListData1 = (ExpandableHeightListView) view.findViewById(R.id.rvListData1);
            rvListData1.setAdapter(new CustomAdapter(activity, data1,false));
            rvListData1.setExpanded(true);
            rvListData1.setFocusable(false);
            //setListViewHeightBasedOnChildren(rvListData1);
            //Helper.getListViewSize(rvListData1);

            ExpandableHeightListView rvListData2 = (ExpandableHeightListView) view.findViewById(R.id.rvListData2);
            rvListData2.setAdapter(new CustomAdapter(activity, data2,false));
            rvListData2.setExpanded(true);
            rvListData2.setFocusable(false);
            //setListViewHeightBasedOnChildren(rvListData2);
            //Helper.getListViewSize(rvListData2);

            ExpandableHeightListView rvListData3 = (ExpandableHeightListView) view.findViewById(R.id.rvListData3);
            rvListData3.setVisibility(View.GONE);
            rvListData3.setFocusable(false);
            // rvListData3.setAdapter(new CustomAdapter(activity, data3));
            // rvListData3.setExpanded(true);
            //setListViewHeightBasedOnChildren(rvListData3);
            //Helper.getListViewSize(rvListData3);

        } catch (Exception ex) {
        }
    }

    private void initViewDailyPanchang(View view) {
        try {

            TextView header = (TextView) view.findViewById(R.id.tvHeader);
            header.setText(getResources().getString(R.string.panchang_for_today));
            header.setTypeface(((BaseInputActivity) activity).mediumTypeface);


            String name[] = new String[]{getResources().getString(R.string.tithi),
                    getResources().getString(R.string.nakshatra),
                    getResources().getString(R.string.karana),
                    getResources().getString(R.string.paksha),
                    getResources().getString(R.string.yoga),
                    getResources().getString(R.string.day)};

            String value[] = new String[]{model.getTithiValue(),
                    model.getNakshatraValue(),
                    model.getKaranaValue(),
                    model.getPakshaName(),
                    model.getYogaValue(),
                    model.getVaara()};

            String time[] = new String[]{model.getTithiTime(),
                    model.getNakshatraTime(),
                    model.getKaranaTime(),
                    "",
                    model.getYogaTime(),
                    ""};


            List<BeanNameValueCardListData> data = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);
                listData.setTime(time[i]);

                data.add(listData);
            }

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);
            rvListData.setAdapter(new CustomAdapter(activity, data,false));
            rvListData.setExpanded(true);
            rvListData.setFocusable(true);
            //setListViewHeightBasedOnChildren(rvListData);

        } catch (Exception ex) {
        }
    }

    private void initViewSunAndMoonCalculation(View view) {

        try {
            TextView header = (TextView) view.findViewById(R.id.tvHeader);
            header.setText(getResources().getString(R.string.sun_and_moon_calculation));
            header.setTypeface(((BaseInputActivity) activity).mediumTypeface);

            String name[] = new String[]{getResources().getString(R.string.sun_rises),
                    getResources().getString(R.string.moon_rises),
                    getResources().getString(R.string.moon_sign),
                    getResources().getString(R.string.sun_set),
                    getResources().getString(R.string.moon_set),
                    getResources().getString(R.string.ritu)};

            String value[] = new String[]{model.getSunRise(),
                    model.getMoonRise(),
                    model.getMoonSignValue(),
                    model.getSunSet(),
                    model.getMoonSet(),
                    model.getRitu()};

            String time[] = new String[]{"",
                    "",
                    model.getMoonSignTime(),
                    "",
                    "",
                    ""};

            List<BeanNameValueCardListData> data = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);
                listData.setTime(time[i]);
                data.add(listData);
            }

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);
            rvListData.setAdapter(new CustomAdapter(activity, data,false));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);
            //setListViewHeightBasedOnChildren(rvListData);
        } catch (Exception ex) {

        }
    }

    private void initViewHinduMonthAndYear(View view) {

        try {
            TextView header = (TextView) view.findViewById(R.id.tvHeader);
            header.setText(getResources().getString(R.string.hindu_month_and_year));
            header.setTypeface(((BaseInputActivity) activity).mediumTypeface);

            String name[] = new String[]{getResources().getString(R.string.shaka_samvat),
                    getResources().getString(R.string.kali_samvat),
                    getResources().getString(R.string.day_duration),
                    getResources().getString(R.string.vikram_samvat),
                    getResources().getString(R.string.month_amanta),
                    getResources().getString(R.string.month_purnimanta)};

            //Toast.makeText(this, model.getShakaSamvat(), Toast.LENGTH_SHORT).show();

            String value[] = new String[]{model.getShakaSamvatYear(),
                    model.getKaliSamvat(),
                    model.getDayDuration(),
                    model.getVikramSamvat(),
                    model.getMonthAmanta(),
                    model.getMonthPurnimanta()};

            String time[] = new String[]{model.getShakaSamvatName(),
                    "",
                    "",
                    "",
                    "",
                    ""};


            List<BeanNameValueCardListData> data = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);
                listData.setTime(time[i]);
                data.add(listData);
            }

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);
            rvListData.setAdapter(new CustomAdapter(activity, data,true));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);
            //setListViewHeightBasedOnChildren(rvListData);
        } catch (Exception ex) {

        }
    }

    public void setDate() {
        //dateFragment = new DatePickerFragment();
        //dateFragment.show(getFragmentManager(), "datePicker");
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

    private void showCustomDatePickerDialog(final BeanDateTimeForPanchang beanDateTime) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(activity);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) activity.getLayoutInflater()
                .inflate(R.layout.date_time_dialog, null);
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
        setDateBtn.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        // Update demo TextViews when the "OK" button is clicked
        setDateBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();

            }
        });

        Button cancelBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(((BaseInputActivity) activity).mediumTypeface);
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
        resetBtn.setTypeface(((BaseInputActivity) activity).mediumTypeface);
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
                /*BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
                beanDateTime.setYear(year);
                beanDateTime.setMonth(month);
                beanDateTime.setDay(day);
                beanDateTime.setHour(0);
                beanDateTime.setMin(0);
                beanDateTime.setSecond(0);
                updateBirthDate(beanDateTime, beanPlace);*/

                beanDateTime.setCalender(month, day, year);
                beanDateTime.setDay(CUtils.getDayOfMonth(view,day,month,year));
                beanDateTime.setMonth(month);
                beanDateTime.setYear(year);
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
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(mTimePicker.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mTimePicker.show();
            mTimePicker.getWindow().setAttributes(lp);
        } else {
            mTimePicker.show();
        }

        try {
            //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
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
          /*  BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
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

    private void showAndroidDatePicker(BeanDateTimeForPanchang beanDateTime) {
        final CustomDatePicker dg = new CustomDatePicker(requireContext(), mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());

        dg.show();
    }

    @Override
    public void onDateChanged(Calendar c) {
        // TODO Auto-generated method stub

      /*  BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
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


        Date _datePan = new Date();
        Calendar calendar = Calendar.getInstance();
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        //Date date = calendar.getTime();
        //setLayout(date,city_Id,language,this.beanPlace);
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

        updateBirthDate(beanDateTime, beanPlace);

    }

    public void getNextDayData() {

      /*calendar.add(Calendar.DATE, 1);
      Date date = calendar.getTime();
      setLayout(date,city_Id,language,this.beanPlace);  */

        Calendar calendar = beanDateTime.getCalender();
        calendar.add(Calendar.DATE, 1);
        beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
        beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        beanDateTime.setYear(calendar.get(Calendar.YEAR));
        //Date date = calendar.getTime();
        //setLayout(date,city_Id,language,this.beanPlace);
        CUtils.saveDateTimeForPanchang(activity, beanDateTime, true);

        updateBirthDate(beanDateTime, beanPlace);

    }

    public void shareContentData(String packageName, BeanPlace beanPlace) {
        Log.e("whatsAppData", "shareContentData() enter");
        whatsAppData = "";
        String enter1 = "\n";
        String enter2 = "\n\n";

        String titleImage = "☀ ";
        String contentImage = "🔅 ";
        String heading1 = "🚩श्री गणेशाय नम:🚩 " + enter1;
        String heading2 = "📜 " + getResources().getString(R.string.daily_panchang) + " 📜" + enter2;

        String heading = heading1 + heading2;

        String date = tvDatePicker.getText().toString();
        Log.e("whatsAppData", "shareContentData() 1");
        if (beanPlace != null) {
            Log.e("whatsAppData", "shareContentData() 2");
            heading = heading + "☀ " + date + enter1;
            if (beanPlace.getCityName().equals("Manual_Lat_Long") || beanPlace.getCityName().equals("Current Location")) {
                heading = heading + "☀ " + beanPlace.getCityName();
            } else {
                heading = heading + "☀ " + beanPlace.getCityName() + ", " + beanPlace.getCountryName();
            }
            Log.e("whatsAppData", "shareContentData() 3");
        } else {
            heading = heading + "☀ " + date + enter1;
            heading = heading + "☀ New Delhi, India";
        }

        whatsAppData = heading + enter2;
        Log.e("whatsAppData", "shareContentData() 4");
        setDataToString(titleImage + getResources().getString(R.string.panchang_for_today), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.tithi), model.getTithiValue(), CUtils.convertTimeToAmPm(model.getTithiTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.nakshatra), model.getNakshatraValue(), CUtils.convertTimeToAmPm(model.getNakshatraTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.karana), model.getKaranaValue(), CUtils.convertTimeToAmPm(model.getKaranaTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.paksha), model.getPakshaName(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.yoga), model.getYogaValue(), CUtils.convertTimeToAmPm(model.getYogaTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.day), model.getVaara(), "", enter2, 0);

        Log.e("whatsAppData", "shareContentData() 5");
        setDataToString(titleImage + getResources().getString(R.string.sun_and_moon_calculation), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.sun_rises), CUtils.convertTimeToAmPm(model.getSunRise()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.moon_rises), CUtils.convertTimeToAmPm(model.getMoonRise()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.moon_sign), model.getMoonSignValue(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.sun_set), CUtils.convertTimeToAmPm(model.getSunSet()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.moon_set), CUtils.convertTimeToAmPm(model.getMoonSet()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.ritu), model.getRitu(), "", enter2, 0);

        Log.e("whatsAppData", "shareContentData() 6");
        setDataToString(titleImage + getResources().getString(R.string.hindu_month_and_year), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.shaka_samvat), model.getShakaSamvatYear(), model.getShakaSamvatName(), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.kali_samvat), model.getKaliSamvat(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.day_duration), CUtils.convertTimeToAmPm(model.getDayDuration()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.vikram_samvat), model.getVikramSamvat(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.month_amanta), model.getMonthAmanta(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.month_purnimanta), model.getMonthPurnimanta(), "", enter2, 0);

        Log.e("whatsAppData", "shareContentData() 7");
        setDataToString(titleImage + getResources().getString(R.string.auspicious_and_inauspicious), "", "", enter1, 0);
        setDataToString(titleImage + getResources().getString(R.string.auspicious_timings), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.abhijit), model.getAbhijitFrom(), model.getAbhijitTo(), enter1, 1);

        setDataToString(titleImage + getResources().getString(R.string.inauspicious_timings), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.dushta_muhurtas), CUtils.convertTimeToAmPm(model.getDushtaMuhurtasFrom()), CUtils.convertTimeToAmPm(model.getDushtaMuhurtasTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.kantaka), CUtils.convertTimeToAmPm(model.getKantaka_MrityuFrom()), CUtils.convertTimeToAmPm(model.getKantaka_MrityuTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.yamaghanta), CUtils.convertTimeToAmPm(model.getYamaghantaFrom()), CUtils.convertTimeToAmPm(model.getYamaghantaTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.rahu_kaal), CUtils.convertTimeToAmPm(model.getRahuKaalVelaFrom()), CUtils.convertTimeToAmPm(model.getRahuKaalVelaTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.kulika), CUtils.convertTimeToAmPm(model.getKulikaFrom()), CUtils.convertTimeToAmPm(model.getKulikaTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.kalavela), CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamFrom()), CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.yamaganda), CUtils.convertTimeToAmPm(model.getYamagandaVelaFrom()), CUtils.convertTimeToAmPm(model.getYamagandaVelaTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.gulika_kaal), CUtils.convertTimeToAmPm(model.getGulikaKaalVelaFrom()), CUtils.convertTimeToAmPm(model.getGulikaKaalVelaTo()), enter1, 1);
        //whatsAppData = whatsAppData + getResources().getString(R.string.inauspicious_timings1)+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.dushta_muhurtas1)+giveMeSpace(spaceBetweenTexts)+model.getDushtaMuhurtasFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getDushtaMuhurtasTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.kantaka1)+giveMeSpace(spaceBetweenTexts)+model.getKantaka_MrityuFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getKantaka_MrityuTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.yamaghanta)+giveMeSpace(spaceBetweenTexts)+model.getYamaghantaFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getYamaghantaTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.rahu_kaal)+giveMeSpace(spaceBetweenTexts)+model.getRahuKaalVelaFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getRahuKaalVelaTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.kulika)+giveMeSpace(spaceBetweenTexts)+model.getKulikaFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getKulikaTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.kalavela)+giveMeSpace(spaceBetweenTexts)+model.getKalavela_ArdhayaamFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getKalavela_ArdhayaamTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.yamaganda)+giveMeSpace(spaceBetweenTexts)+model.getYamagandaVelaFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getYamagandaVelaTo()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.gulika_kaal)+giveMeSpace(spaceBetweenTexts)+model.getGulikaKaalVelaFrom()+giveMeSpacewithTag(spaceBetweenTextsWithtag)+model.getGulikaKaalVelaTo()+enter2;

        setDataToString(titleImage + getResources().getString(R.string.disha_shoola), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.disha_shoola), model.getDishaShoola(), "", enter2, 0);
        //whatsAppData = whatsAppData + getResources().getString(R.string.disha_shoola)+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.disha_shoola)+giveMeSpace(spaceBetweenTexts)+model.getDishaShoola()+enter2;

        setDataToString(titleImage + getResources().getString(R.string.chandrabalam_and_tarabalam), "", "", enter1, 0);
        setDataToString(titleImage + getResources().getString(R.string.tara_bala), "\n" + contentImage + model.getTaraBala(), "", enter1, 0);
        setDataToString(titleImage + getResources().getString(R.string.chandra_bala), "\n" + contentImage + model.getChandraBala(), "", enter2, 0);
        //whatsAppData = whatsAppData + getResources().getString(R.string.chandrabalam_and_tarabalam)+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.tara_bala)+giveMeSpace(spaceBetweenTexts)+model.getTaraBala()+enter1;
        //whatsAppData = whatsAppData + getResources().getString(R.string.chandra_bala)+giveMeSpace(spaceBetweenTexts)+model.getChandraBala()+enter2;
        Log.e("whatsAppData", "shareData() before = "+ whatsAppData);
        CUtils.shareData(activity, whatsAppData, packageName, getResources().getString(R.string.share_panchang));

    }

    private void setDataToString(String title, String value, String time, String enter, int withOrWithoutTag) {

        //whatsAppData = whatsAppData + getResources().getString(R.string.tithi1)+giveMeSpace(spaceBetweenTexts)+model.getTithiValue()+giveMeSpace(spaceBetweenTexts)+model.getTithiTime()+enter1;
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
                    String timeStr = arrValue[0];
                    if(arrValue.length > 1){
                        timeStr = arrValue[1];
                    }
                    whatsAppData = whatsAppData + arrName[1] + giveMeSpacewithTag(spaceBetweenTexts) + timeStr + enter;
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
            //Toast.makeText(this, ""+space, Toast.LENGTH_SHORT).show();
            whatsAppData = whatsAppData + giveMeSpace(space);
            String timeStr = arrValue[0];
            if(arrValue.length > 1){
                timeStr = arrValue[1];
            }
            whatsAppData = whatsAppData + arrName[1] + giveMeSpace(spaceBetweenTexts) + timeStr + enter;
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
                if (activity instanceof InputPanchangActivity) {
                    beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
                }

                txtPlaceName.setText(beanPlace.getCityName().trim());
                txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));

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
            model = CUtils.getPanchangObject(activity, key);

            if (model == null) {
                getPanchangData(date, language, beanPlace);
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
        if (beanPlace != null) {
            txtPlaceName.setText(beanPlace.getCityName().trim());
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
        }
        progressBar.setVisibility(View.GONE);
        initViewDailyPanchang(cardViewDailyPanchang);
        initViewSunAndMoonCalculation(cardViewSunAndMoonCalculation);
        initViewHinduMonthAndYear(cardViewHinduMonthAndYear);
        initViewAuspiciousInauspiciousTimings(cardViewAuspiciousInauspiciousTimings);
        initViewDishaShoola(cardViewDishaShoola);
        initViewChandrabalamAndTarabalam(cardViewChandrabalamAndTarabalam);
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

