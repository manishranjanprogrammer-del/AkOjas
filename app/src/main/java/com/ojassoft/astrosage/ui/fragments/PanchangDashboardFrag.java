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
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanLagnaTable;
import com.ojassoft.astrosage.beans.BeanNameValueCardListData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.beans.NewFestivalBean;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.FontUtils;
import com.ojassoft.astrosage.misc.LagnaTableCalculation;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.FestDataDetail;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.BaseTtsActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customviews.basic.DigitalClock;
import com.ojassoft.astrosage.ui.fragments.horoscope.TtsCallbackListener;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;
import com.ojassoft.astrosage.utils.PanchangUtil;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.TTS_CHAR_LIMIT;
import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.mTextToSpeech;
import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.setmTtsCallbackListener;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_DUAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_FIXED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_MOVABLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MONTH_PURNIMANT;

public class PanchangDashboardFrag extends Fragment implements DateTimePicker.DateWatcher, TtsCallbackListener {
    // CONSTANTS
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    private static PanchangDashboardFrag panchangDashboardFrag;
    public int SELECTED_MODULE;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public String FONT_DIGITAL_MONO = "fonts/digital-7-mono.ttf";
    //TextView placeTV;
    ImageView whatsAppIV;
    /* CardView todayHoroscopeCV, shareCV;
     TextView todayHorascopeTV, shareTV;*/
    Button todayHorascopeBtn, shareBtn;
    DigitalClock digitalClockTV;
    ImageView clockIV;
    CardView tithiDetailCV;
    TextView tithiTitleTV, tithiTV, tithiDayTV, tithiSTTV, tithiETTV, tithiWeekDayTV, tithiDateTV, tithiMonthAndPakshTV, tithiMonthAndPakshTV1, tithiFestTV;
    ImageView tithiIV, tithiDetailSpeakerIV;
    LinearLayout tithiDetailSpeakerLL;
    ProgressBar tithiDetailPB;
    ImageView infoIV;


    TextView placeNameTV, placeDetailTV;
    TextView currentMuhuratHeadingTV, panchangHeadingTV, hinduMonthYearTV, auspiciousAndInauspiciousTV, sunAndMoonCAlculationTV;
    ImageView currentMuhuratHeadingIV, panchangHeadingIV, hinduMonthYearIV, auspiciousAndInauspiciousIV, sunAndMoonCAlculationIV;


    CardView horaCV;
    TextView horaHeadingTV, horaNameTV, horaTimeTV, horaGoodFor;
    ImageView horaSpeakerIV;
    LinearLayout horaSpeakerLL;
    ProgressBar horaPB;

    CardView chogdiyaCV;
    TextView chogdiyaHeadingTV, chogdiyaNameTV, chogdiyaTimeTV, chogdiyaGoodFor;
    ImageView chogdiyaSpeakerIV;
    LinearLayout chogdiyaSpeakerLL;
    ProgressBar chogdiyaPB;

    CardView rahukalCV;
    TextView rahukalHeadingTV, rahukalNameTV, rahukalTimeTV, rahukalGoodFor;
    ImageView rahukalSpeakerIV;
    LinearLayout rahukalSpeakerLL;
    ProgressBar rahukalPB;

    CardView doGhatiCV;
    TextView doGhatiHeadingTV, doGhatiNameTV, doGhatiTimeTV, doGhatiGoodFor;
    ImageView doGhatiSpeakerIV;
    LinearLayout doGhatiSpeakerLL;
    ProgressBar doGhatiPB;

    CardView tithiCV;
    TextView tithiHeadingTV, tithiNameTV, tithiTimeTV, tithiGoodFor;
    ImageView tithiSpeakerIV;
    LinearLayout tithiSpeakerLL;
    ProgressBar tithiPB;

    CardView pakshaCV;
    TextView pakshaHeadingTV, pakshaNameTV, pakshaTimeTV, pakshaGoodFor;
    ImageView pakshaSpeakerIV;
    LinearLayout pakshaSpeakerLL;
    ProgressBar pakshaPB;

    CardView karanCV;
    TextView karanHeadingTV, karanNameTV, karanTimeTV, karanGoodFor;
    ImageView karanSpeakerIV;
    LinearLayout karanSpeakerLL;
    ProgressBar karanPB;

    CardView nakshtraCV;
    TextView nakshtraHeadingTV, nakshtraNameTV, nakshtraTimeTV, nakshtraGoodFor;
    ImageView nakshtraSpeakerIV;
    LinearLayout nakshtraSpeakerLL;
    ProgressBar nakshtraPB;

    CardView yogaCV;
    TextView yogaHeadingTV, yogaNameTV, yogaTimeTV, yogaGoodFor;
    ImageView yogaSpeakerIV;
    LinearLayout yogaSpeakerLL;
    ProgressBar yogaPB;

    CardView dayCV;
    TextView dayHeadingTV, dayNameTV, dayTimeTV, dayGoodFor;
    ImageView daySpeakerIV;
    LinearLayout daySpeakerLL;
    ProgressBar dayPB;

    CardView shakaSamvatCV;
    TextView shakaSamvatHeadingTV, shakaSamvatNameTV, shakaSamvatTimeTV, shakaSamvatGoodFor;
    ImageView shakaSamvatSpeakerIV;
    LinearLayout shakaSamvatSpeakerLL;
    ProgressBar shakaSamvatPB;

    CardView dayDurationCV;
    TextView dayDurationHeadingTV, dayDurationNameTV, dayDurationTimeTV, dayDurationGoodFor;
    ImageView dayDurationSpeakerIV;
    LinearLayout dayDurationSpeakerLL;
    ProgressBar dayDurationPB;

    CardView kaliSamvatCV;
    TextView kaliSamvatHeadingTV, kaliSamvatNameTV, kaliSamvatTimeTV, kaliSamvatGoodFor;
    ImageView kaliSamvatSpeakerIV;
    LinearLayout kaliSamvatSpeakerLL;
    ProgressBar kaliSamvatPB;

    CardView vikramSamvatCV;
    TextView vikramSamvatHeadingTV, vikramSamvatNameTV, vikramSamvatTimeTV, vikramSamvatGoodFor;
    ImageView vikramSamvatSpeakerIV;
    LinearLayout vikramSamvatSpeakerLL;
    ProgressBar vikramSamvatPB;

    CardView monthAmanthaCV;
    TextView monthAmanthaHeadingTV, monthAmanthaNameTV, monthAmanthaTimeTV, monthAmanthaGoodFor;
    ImageView monthAmanthaSpeakerIV;
    LinearLayout monthAmanthaSpeakerLL;
    ProgressBar monthAmanthaPB;

    CardView monthPurnimantCV;
    TextView monthPurnimantHeadingTV, monthPurnimantNameTV, monthPurnimantTimeTV, monthPurnimantGoodFor;
    ImageView monthPurnimantSpeakerIV;
    LinearLayout monthPurnimantSpeakerLL;
    ProgressBar monthPurnimantPB;

    CardView abhijitCV;
    TextView abhijitHeadingTV, abhijitNameTV, abhijitTimeTV, abhijitGoodFor;
    ImageView abhijitSpeakerIV;
    LinearLayout abhijitSpeakerLL;
    ProgressBar abhijitPB;

    CardView dustMuhuratCV;
    TextView dustMuhuratHeadingTV, dustMuhuratNameTV, dustMuhuratTimeTV, dustMuhuratGoodFor;
    ImageView dustMuhuratSpeakerIV;
    LinearLayout dustMuhuratSpeakerLL;
    ProgressBar dustMuhuratPB;

    CardView kantakaCV;
    TextView kantakaHeadingTV, kantakaNameTV, kantakaTimeTV, kantakaGoodFor;
    ImageView kantakaSpeakerIV;
    LinearLayout kantakaSpeakerLL;
    ProgressBar kantakaPB;

    CardView yamaghantaCV;
    TextView yamaghantaHeadingTV, yamaghantaNameTV, yamaghantaTimeTV, yamaghantaGoodFor;
    ImageView yamaghantaSpeakerIV;
    LinearLayout yamaghantaSpeakerLL;
    ProgressBar yamaghantaPB;

    CardView kulikaCV;
    TextView kulikaHeadingTV, kulikaNameTV, kulikaTimeTV, kulikaGoodFor;
    ImageView kulikaSpeakerIV;
    LinearLayout kulikaSpeakerLL;
    ProgressBar kulikaPB;

    CardView kalavelaCV;
    TextView kalavelaHeadingTV, kalavelaNameTV, kalavelaTimeTV, kalavelaGoodFor;
    ImageView kalavelaSpeakerIV;
    LinearLayout kalavelaSpeakerLL;
    ProgressBar kalavelaPB;

    CardView yamagandaCV;
    TextView yamagandaHeadingTV, yamagandaNameTV, yamagandaTimeTV, yamagandaGoodFor;
    ImageView yamagandaSpeakerIV;
    LinearLayout yamagandaSpeakerLL;
    ProgressBar yamagandaPB;

    CardView gulikaKalCV;
    TextView gulikaKalHeadingTV, gulikaKalNameTV, gulikaKalTimeTV, gulikaKalGoodFor;
    ImageView gulikaKalSpeakerIV;
    LinearLayout gulikaKalSpeakerLL;
    ProgressBar gulikaKalPB;

    CardView panchakCV;
    TextView panchakHeadingTV, panchakNameTV, panchakTimeTV, panchakGoodFor;
    ImageView panchakSpeakerIV;
    LinearLayout panchakSpeakerLL;
    ProgressBar panchakPB;

    CardView bhadraCV;
    TextView bhadraHeadingTV, bhadraNameTV, bhadraTimeTV, bhadraGoodFor;
    ImageView bhadraSpeakerIV;
    LinearLayout bhadraSpeakerLL;
    ProgressBar bhadraPB;

    CardView dishaShoolCV;
    TextView dishaShoolHeadingTV, /*dishaShoolKeyTV,*/
            dishaShoolValTV;
    ImageView dishaShoolSpeakerIV;
    LinearLayout dishaShoolSpeakerLL;
    ProgressBar dishaShoolPB;

    CardView lagnaCV;
    TextView lagnaHeadingTV, lagnaNameTV, lagnaStartnEndTimeTV;
    ImageView lagnaSpeakerIV;
    LinearLayout lagnaSpeakerLL;
    ProgressBar lagnaPB;
    private LinearLayout currentLagnaNatureLL;
    private TextView lagnaNatureCurrentTV;
    private ImageView lagnaNatureCurrentIV;

    CardView tarabalCV;
    TextView tarabalHeadingTV, tarabalDescTV, nextTarabalHeadingTV, nextTarabalDescTV;
    ImageView tarabalSpeakerIV, nextTarabalSpeakerIV;
    LinearLayout tarabalSpeakerLL, nextTarabalSpeakerLL;
    ProgressBar tarabalPB, nextTarabalPB;

    CardView chandrabalCV;
    TextView chandrabalHeadingTV, chandrabalDescTV, nextChandrabalHeadingTV, nextChandrabalDescTV;
    ImageView chandrabalSpeakerIV, nextChandrabalSpeakerIV;
    LinearLayout chandrabalSpeakerLL, nextChandrabalSpeakerLL;
    ProgressBar chandrabalPB, nextChandrabalPB;

    //TextView noteHeadingTV, noteDescTV1, noteDescTV2;
    ImageView noteSpeakerIV;

    CardView sunRiseCV;
    TextView sunRiseHeadingTV, sunRiseNameTV, sunRiseTimeTV, sunRiseGoodFor;
    ImageView sunRiseSpeakerIV;
    LinearLayout sunRiseSpeakerLL;
    ProgressBar sunRisePB;

    CardView sunSetCV;
    TextView sunSetHeadingTV, sunSetNameTV, sunSetTimeTV, sunSetGoodFor;
    ImageView sunSetSpeakerIV;
    LinearLayout sunSetSpeakerLL;
    ProgressBar sunSetPB;

    CardView moonRiseCV;
    TextView moonRiseHeadingTV, moonRiseNameTV, moonRiseTimeTV, moonRiseGoodFor;
    ImageView moonRiseSpeakerIV;
    LinearLayout moonRiseSpeakerLL;
    ProgressBar moonRisePB;

    CardView moonSetCV;
    TextView moonSetHeadingTV, moonSetNameTV, moonSetTimeTV, moonSetGoodFor;
    ImageView moonSetSpeakerIV;
    LinearLayout moonSetSpeakerLL;
    ProgressBar moonSetPB;

    CardView rituCV;
    TextView rituHeadingTV, rituNameTV, rituTimeTV, rituGoodFor;
    ImageView rituSpeakerIV;
    LinearLayout rituSpeakerLL;
    ProgressBar rituPB;

    CardView moonSignCV;
    TextView moonSignHeadingTV, moonSignNameTV, moonSignTimeTV, moonSignGoodFor;
    ImageView moonSignSpeakerIV;
    LinearLayout moonSignSpeakerLL;
    ProgressBar moonSignPB;


    private double lat = 28.36;
    private double lng = 77.12;
    private double tzone = +5.5;
    int i;
    BeanDateTimeForPanchang beanDateTime;
    boolean flag = true;
    int startDay;
    int startNight;
    AajKaPanchangModel model;
    AajKaPanchangModel nextDayModel;
    AajKaPanchangModel preDayModel;
    String startTime, endTime;
    List<BeanNameValueCardListData> data2;
    String city_Id = "1261481";
    String language;
    Activity activity;
    View view;
    //BeanHoroPersonalInfo beanHoroPersonalInfo;
    TextView tvDatePicker;
    int currentYear;
    LinearLayout layPlaceHolder;
    TextView txtPlaceDetail;
    ImageView imgShareOnWhatsApp;
    Button btnPreviousDate, btnNextDate;
    TextView btnCurrentDate;
    String whatsAppData;
    int spaceBetweenTexts = 2;
    int spaceBetweenTextsWithtag = 3;
    //Date date;
    IPanchang panchang;
    ImageView clickedPlayedIV;
    RequestQueue queue;
    ArrayList<FestDataDetail> bhadraDataList;
    ArrayList<FestDataDetail> panchakDataList;
    int currentBhadra = -1;
    int currentPanchak = -1;
    private BeanLagnaTable beanLagna = null;
    private PanchangUtil objPanchangUtil;
    String langCode;
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

    /* private String latitude = "0";
     private String longitude = "0";
     private String timeZone = "+0";*/
    private BeanPlace beanPlace;
    private String timeZoneString = "Asia/Kolkata";
    private java.text.DateFormat mDateFormat;
    private int monthType;
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
    LoadPanchangData loadPanchangData;
    private String startTimeToShare;
    private String endTimeToShare;

    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;

    public static PanchangDashboardFrag newInstance(String text) {

        //Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        panchangDashboardFrag = new PanchangDashboardFrag();
        Bundle b = new Bundle();
        b.putString("msg", text);
        panchangDashboardFrag.setArguments(b);
        return panchangDashboardFrag;
    }

    public static PanchangDashboardFrag getInstance() {
        return panchangDashboardFrag;
    }

    private static String removeZero(String time) {
        String timeStr = time;
        if (timeStr.startsWith("0")) {
            timeStr = timeStr.replaceFirst("0", "");
        }
        return timeStr;
    }

    private static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langCode = CUtils.getLanguageKey(LANGUAGE_CODE);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
    }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            mDateFormat = DateFormat.getMediumDateFormat(activity);
            beanDateTime = new BeanDateTimeForPanchang();
            view = inflater.inflate(R.layout.panchang_dashbord_layout, container, false);

            SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;


            //beanHoroPersonalInfo = new BeanHoroPersonalInfo();

            LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode();

            language = CUtils.getLanguage(CUtils.getLanguageCodeFromPreference(activity));
            init(view);
            //setHeadingColor();
            setCardHeadings();
            setmTtsCallbackListener(PanchangDashboardFrag.this);
            Calendar calendar = Calendar.getInstance();
            if (activity instanceof InputPanchangActivity) {
                if (((InputPanchangActivity) activity).calendar != null) {
                    calendar = ((InputPanchangActivity) activity).calendar;
                    int month = calendar.get(Calendar.MONTH);
                }
            } else if (activity instanceof DetailedHoroscope) {
                if (((DetailedHoroscope) activity).calendar != null) {
                    calendar = ((DetailedHoroscope) activity).calendar;
                    int month = calendar.get(Calendar.MONTH);
                }
            }

            beanDateTime.setCalender(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
            beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            beanDateTime.setMonth(calendar.get(Calendar.MONTH));
            beanDateTime.setYear(calendar.get(Calendar.YEAR));

            BeanDateTimeForPanchang beanDateTimeFromPref = CUtils.getDateTimeForPanchang(activity);
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
        }catch(Exception e){
            //Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp="+e);
        }
        return view;
    }

    private void init(View rootView) {
        View view;
        monthType = CUtils.getMonthType(getActivity());
        //Toolbar view
        //placeTV = ((InputPanchangActivity) activity).placeTV;
        whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
        //Palce view
        view = rootView.findViewById(R.id.place_time_view);
        placeNameTV = (TextView) view.findViewById(R.id.textViewPlaceName);
        placeDetailTV = (TextView) view.findViewById(R.id.textViewPlaceDetails);
        btnCurrentDate = (TextView) view.findViewById(R.id.btnCurrentDate);
        txtPlaceDetail = (TextView) view.findViewById(R.id.textViewPlaceDetails);
        btnNextDate = (Button) view.findViewById(R.id.btnNextDate);
        btnPreviousDate = (Button) view.findViewById(R.id.btnPreviousDate);
        tvDatePicker = (TextView) view.findViewById(R.id.tvDatePicker);
        layPlaceHolder = (LinearLayout) view.findViewById(R.id.layPlaceHolder);
        //Tithi card view
        view = rootView.findViewById(R.id.panchang_detail_view);
        tithiDetailCV = (CardView) view.findViewById(R.id.cardview);
        tithiTitleTV = (TextView) view.findViewById(R.id.tithi);
        tithiTV = (TextView) view.findViewById(R.id.tithi_name);
        tithiDayTV = (TextView) view.findViewById(R.id.tithi_day);
        tithiSTTV = (TextView) view.findViewById(R.id.tithi_start_time);
        tithiETTV = (TextView) view.findViewById(R.id.tithi_end_time);
        tithiIV = (ImageView) view.findViewById(R.id.tithi_image);
        tithiDetailSpeakerIV = (ImageView) view.findViewById(R.id.tithi_speaker);
        tithiDetailSpeakerLL = (LinearLayout) view.findViewById(R.id.tithi_speaker_ll);
        tithiDetailPB = (ProgressBar) view.findViewById(R.id.progressBar);
        tithiWeekDayTV = (TextView) view.findViewById(R.id.tithi_day_name);
        tithiDateTV = (TextView) view.findViewById(R.id.tithi_date);
        tithiMonthAndPakshTV = (TextView) view.findViewById(R.id.tithi_paksha);
        tithiMonthAndPakshTV1 = (TextView) view.findViewById(R.id.tithi_paksha1);
        infoIV = (ImageView) view.findViewById(R.id.info_iv);
        tithiFestTV = (TextView) view.findViewById(R.id.tithi_festival);
        //clock view
        view = rootView.findViewById(R.id.clock_view);
        digitalClockTV = view.findViewById(R.id.timeTV);
        clockIV = (ImageView) view.findViewById(R.id.speaker_iv);
        //Heading views
        currentMuhuratHeadingTV = (TextView) rootView.findViewById(R.id.place_heading_view).findViewById(R.id.heading_tv);
        panchangHeadingTV = (TextView) rootView.findViewById(R.id.panchang_heading_view).findViewById(R.id.heading_tv);
        hinduMonthYearTV = (TextView) rootView.findViewById(R.id.hindu_month_year_view).findViewById(R.id.heading_tv);
        auspiciousAndInauspiciousTV = (TextView) rootView.findViewById(R.id.auspicious_inauspicious_view).findViewById(R.id.heading_tv);
        sunAndMoonCAlculationTV = (TextView) rootView.findViewById(R.id.sun_moon_calculation_heading_view).findViewById(R.id.heading_tv);
        currentMuhuratHeadingIV = (ImageView) rootView.findViewById(R.id.place_heading_view).findViewById(R.id.heading_iv);
        panchangHeadingIV = (ImageView) rootView.findViewById(R.id.panchang_heading_view).findViewById(R.id.heading_iv);
        hinduMonthYearIV = (ImageView) rootView.findViewById(R.id.hindu_month_year_view).findViewById(R.id.heading_iv);
        auspiciousAndInauspiciousIV = (ImageView) rootView.findViewById(R.id.auspicious_inauspicious_view).findViewById(R.id.heading_iv);
        sunAndMoonCAlculationIV = (ImageView) rootView.findViewById(R.id.sun_moon_calculation_heading_view).findViewById(R.id.heading_iv);

        //Hora view
        view = rootView.findViewById(R.id.hora_view);
        horaCV = (CardView) view.findViewById(R.id.cardview);
        horaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        horaNameTV = (TextView) view.findViewById(R.id.name_tv);
        horaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        horaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        horaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        horaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        horaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Choghadia view
        view = rootView.findViewById(R.id.chogdiya_view);
        chogdiyaCV = (CardView) view.findViewById(R.id.cardview);
        chogdiyaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        chogdiyaNameTV = (TextView) view.findViewById(R.id.name_tv);
        chogdiyaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        chogdiyaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        chogdiyaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        chogdiyaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        chogdiyaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Rahukal view
        view = rootView.findViewById(R.id.rahukaal_view);
        rahukalCV = (CardView) view.findViewById(R.id.cardview);
        rahukalHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        rahukalNameTV = (TextView) view.findViewById(R.id.name_tv);
        rahukalTimeTV = (TextView) view.findViewById(R.id.time_tv);
        rahukalGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        rahukalSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        rahukalSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        rahukalPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Do ghati view
        view = rootView.findViewById(R.id.do_ghati_view);
        doGhatiCV = (CardView) view.findViewById(R.id.cardview);
        doGhatiHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        doGhatiNameTV = (TextView) view.findViewById(R.id.name_tv);
        doGhatiTimeTV = (TextView) view.findViewById(R.id.time_tv);
        doGhatiGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        doGhatiSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        doGhatiSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        doGhatiPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Tithi view
        view = rootView.findViewById(R.id.tithi_view);
        tithiCV = (CardView) view.findViewById(R.id.cardview);
        tithiHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        tithiNameTV = (TextView) view.findViewById(R.id.name_tv);
        tithiTimeTV = (TextView) view.findViewById(R.id.time_tv);
        tithiGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        tithiSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        tithiSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        tithiPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Paksha view
        view = rootView.findViewById(R.id.paksh_view);
        pakshaCV = (CardView) view.findViewById(R.id.cardview);
        pakshaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        pakshaNameTV = (TextView) view.findViewById(R.id.name_tv);
        pakshaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        pakshaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        pakshaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        pakshaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        pakshaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Karan view
        view = rootView.findViewById(R.id.karan_view);
        karanCV = (CardView) view.findViewById(R.id.cardview);
        karanHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        karanNameTV = (TextView) view.findViewById(R.id.name_tv);
        karanTimeTV = (TextView) view.findViewById(R.id.time_tv);
        karanGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        karanSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        karanSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        karanPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Nakshtra view
        view = rootView.findViewById(R.id.nakshatra_view);
        nakshtraCV = (CardView) view.findViewById(R.id.cardview);
        nakshtraHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        nakshtraNameTV = (TextView) view.findViewById(R.id.name_tv);
        nakshtraTimeTV = (TextView) view.findViewById(R.id.time_tv);
        nakshtraGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        nakshtraSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        nakshtraSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        nakshtraPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Yoga view
        view = rootView.findViewById(R.id.yoga_view);
        yogaCV = (CardView) view.findViewById(R.id.cardview);
        yogaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        yogaNameTV = (TextView) view.findViewById(R.id.name_tv);
        yogaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        yogaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        yogaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        yogaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        yogaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Day view
        view = rootView.findViewById(R.id.day_view);
        dayCV = (CardView) view.findViewById(R.id.cardview);
        dayHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        dayNameTV = (TextView) view.findViewById(R.id.name_tv);
        dayTimeTV = (TextView) view.findViewById(R.id.time_tv);
        dayGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        daySpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        daySpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        dayPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Shaka Samvat view
        view = rootView.findViewById(R.id.shaka_samvat_view);
        shakaSamvatCV = (CardView) view.findViewById(R.id.cardview);
        shakaSamvatHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        shakaSamvatNameTV = (TextView) view.findViewById(R.id.name_tv);
        shakaSamvatTimeTV = (TextView) view.findViewById(R.id.time_tv);
        shakaSamvatGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        shakaSamvatSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        shakaSamvatSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        shakaSamvatPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Day duration view
        view = rootView.findViewById(R.id.day_duration_view);
        dayDurationCV = (CardView) view.findViewById(R.id.cardview);
        dayDurationHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        dayDurationNameTV = (TextView) view.findViewById(R.id.name_tv);
        dayDurationTimeTV = (TextView) view.findViewById(R.id.time_tv);
        dayDurationGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        dayDurationSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        dayDurationSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        dayDurationPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Kali samvat view
        view = rootView.findViewById(R.id.kali_samvat_view);
        kaliSamvatCV = (CardView) view.findViewById(R.id.cardview);
        kaliSamvatHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        kaliSamvatNameTV = (TextView) view.findViewById(R.id.name_tv);
        kaliSamvatTimeTV = (TextView) view.findViewById(R.id.time_tv);
        kaliSamvatGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        kaliSamvatSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        kaliSamvatSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        kaliSamvatPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Vikram samvat view
        view = rootView.findViewById(R.id.vikarm_samvat_view);
        vikramSamvatCV = (CardView) view.findViewById(R.id.cardview);
        vikramSamvatHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        vikramSamvatNameTV = (TextView) view.findViewById(R.id.name_tv);
        vikramSamvatTimeTV = (TextView) view.findViewById(R.id.time_tv);
        vikramSamvatGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        vikramSamvatSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        vikramSamvatSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        vikramSamvatPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Month amantha view
        view = rootView.findViewById(R.id.month_amanta_view);
        monthAmanthaCV = (CardView) view.findViewById(R.id.cardview);
        monthAmanthaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        monthAmanthaNameTV = (TextView) view.findViewById(R.id.name_tv);
        monthAmanthaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        monthAmanthaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        monthAmanthaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        monthAmanthaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        monthAmanthaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Purnimantha amantha view
        view = rootView.findViewById(R.id.month_purnimant_view);
        monthPurnimantCV = (CardView) view.findViewById(R.id.cardview);
        monthPurnimantHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        monthPurnimantNameTV = (TextView) view.findViewById(R.id.name_tv);
        monthPurnimantTimeTV = (TextView) view.findViewById(R.id.time_tv);
        monthPurnimantGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        monthPurnimantSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        monthPurnimantSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        monthPurnimantPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Abhjit view
        view = rootView.findViewById(R.id.abhijit_view);
        abhijitCV = (CardView) view.findViewById(R.id.cardview);
        abhijitHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        abhijitNameTV = (TextView) view.findViewById(R.id.name_tv);
        abhijitTimeTV = (TextView) view.findViewById(R.id.time_tv);
        abhijitGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        abhijitSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        abhijitSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        abhijitPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Dust Muhurat view
        view = rootView.findViewById(R.id.dusta_muhurat_view);
        dustMuhuratCV = (CardView) view.findViewById(R.id.cardview);
        dustMuhuratHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        dustMuhuratNameTV = (TextView) view.findViewById(R.id.name_tv);
        dustMuhuratTimeTV = (TextView) view.findViewById(R.id.time_tv);
        dustMuhuratGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        dustMuhuratSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        dustMuhuratSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        dustMuhuratPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Kantaka view
        view = rootView.findViewById(R.id.kantaka_view);
        kantakaCV = (CardView) view.findViewById(R.id.cardview);
        kantakaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        kantakaNameTV = (TextView) view.findViewById(R.id.name_tv);
        kantakaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        kantakaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        kantakaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        kantakaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        kantakaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //yamaghanta view
        view = rootView.findViewById(R.id.yamaghanta_view);
        yamaghantaCV = (CardView) view.findViewById(R.id.cardview);
        yamaghantaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        yamaghantaNameTV = (TextView) view.findViewById(R.id.name_tv);
        yamaghantaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        yamaghantaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        yamaghantaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        yamaghantaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        yamaghantaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Kulika view
        view = rootView.findViewById(R.id.kulika_view);
        kulikaCV = (CardView) view.findViewById(R.id.cardview);
        kulikaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        kulikaNameTV = (TextView) view.findViewById(R.id.name_tv);
        kulikaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        kulikaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        kulikaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        kulikaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        kulikaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Kalavela view
        view = rootView.findViewById(R.id.kalvela_view);
        kalavelaCV = (CardView) view.findViewById(R.id.cardview);
        kalavelaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        kalavelaNameTV = (TextView) view.findViewById(R.id.name_tv);
        kalavelaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        kalavelaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        kalavelaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        kalavelaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        kalavelaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Yamaganda view
        view = rootView.findViewById(R.id.yamaganda_view);
        yamagandaCV = (CardView) view.findViewById(R.id.cardview);
        yamagandaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        yamagandaNameTV = (TextView) view.findViewById(R.id.name_tv);
        yamagandaTimeTV = (TextView) view.findViewById(R.id.time_tv);
        yamagandaGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        yamagandaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        yamagandaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        yamagandaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //GulikaKal view
        view = rootView.findViewById(R.id.gulika_kaal_view);
        gulikaKalCV = (CardView) view.findViewById(R.id.cardview);
        gulikaKalHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        gulikaKalNameTV = (TextView) view.findViewById(R.id.name_tv);
        gulikaKalTimeTV = (TextView) view.findViewById(R.id.time_tv);
        gulikaKalGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        gulikaKalSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        gulikaKalSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        gulikaKalPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Panchak view
        view = rootView.findViewById(R.id.panchak_view);
        panchakCV = (CardView) view.findViewById(R.id.cardview);
        panchakHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        panchakNameTV = (TextView) view.findViewById(R.id.name_tv);
        panchakTimeTV = (TextView) view.findViewById(R.id.time_tv);
        panchakGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        panchakSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        panchakSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        panchakPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Bhadra view
        view = rootView.findViewById(R.id.bhadra_view);
        bhadraCV = (CardView) view.findViewById(R.id.cardview);
        bhadraHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        bhadraNameTV = (TextView) view.findViewById(R.id.name_tv);
        bhadraTimeTV = (TextView) view.findViewById(R.id.time_tv);
        bhadraGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        bhadraSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        bhadraSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        bhadraPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Disha Shool view
        view = rootView.findViewById(R.id.disha_shool_view);
        dishaShoolCV = (CardView) view.findViewById(R.id.cardview);
        dishaShoolHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
//        dishaShoolKeyTV = (TextView) view.findViewById(R.id.disha_shool_key_tv);
        dishaShoolValTV = (TextView) view.findViewById(R.id.disha_shool_val_tv);
        dishaShoolSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        dishaShoolSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        dishaShoolPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Lagna view
        view = rootView.findViewById(R.id.lagna_view);
        lagnaCV = (CardView) view.findViewById(R.id.cardview);
        lagnaHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        lagnaNameTV = (TextView) view.findViewById(R.id.lagna_kundli_tv);
        lagnaStartnEndTimeTV = (TextView) view.findViewById(R.id.lagna_start_time_tv);
        lagnaSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        lagnaSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        lagnaPB = (ProgressBar) view.findViewById(R.id.progressBar);
        currentLagnaNatureLL = (LinearLayout) view.findViewById(R.id.ll_current_lagna_nature);
        lagnaNatureCurrentIV = (ImageView) view.findViewById(R.id.iv_nature_current);
        lagnaNatureCurrentTV = (TextView) view.findViewById(R.id.tv_lagna_nature_current);
        //tara bal  view
        view = rootView.findViewById(R.id.tarabal_view);
        view.findViewById(R.id.tarabal_layout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.chandrabal_layout).setVisibility(View.VISIBLE);
        //view.findViewById(R.id.chandra_bal_speaker_iv).setVisibility(View.GONE);
        tarabalCV = (CardView) view.findViewById(R.id.cardview);
        tarabalHeadingTV = (TextView) view.findViewById(R.id.tara_bal_heading_tv);
        tarabalDescTV = (TextView) view.findViewById(R.id.tara_bal_desc_tv);
        nextTarabalHeadingTV = (TextView) view.findViewById(R.id.chandra_bal_heading_tv);
        nextTarabalDescTV = (TextView) view.findViewById(R.id.chandra_bal_desc_tv);
        tarabalSpeakerIV = (ImageView) view.findViewById(R.id.tara_bal_speaker_iv);
        tarabalSpeakerLL = (LinearLayout) view.findViewById(R.id.tara_bal_speaker_ll);
        nextTarabalSpeakerIV = (ImageView) view.findViewById(R.id.chandra_bal_speaker_iv);
        nextTarabalSpeakerLL = (LinearLayout) view.findViewById(R.id.chandra_bal_speaker_ll);
        tarabalPB = (ProgressBar) view.findViewById(R.id.progressBar);
        nextTarabalPB = (ProgressBar) view.findViewById(R.id.progressBar1);
        //Chandra bal view
        view = rootView.findViewById(R.id.chandrabal_view);
        view.findViewById(R.id.tarabal_layout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.chandrabal_layout).setVisibility(View.VISIBLE);
        //view.findViewById(R.id.chandra_bal_speaker_iv).setVisibility(View.GONE);
        chandrabalCV = (CardView) view.findViewById(R.id.cardview);
        nextChandrabalHeadingTV = (TextView) view.findViewById(R.id.chandra_bal_heading_tv);
        nextChandrabalDescTV = (TextView) view.findViewById(R.id.chandra_bal_desc_tv);
        chandrabalHeadingTV = (TextView) view.findViewById(R.id.tara_bal_heading_tv);
        chandrabalDescTV = (TextView) view.findViewById(R.id.tara_bal_desc_tv);
        chandrabalSpeakerIV = (ImageView) view.findViewById(R.id.tara_bal_speaker_iv);
        chandrabalSpeakerLL = (LinearLayout) view.findViewById(R.id.tara_bal_speaker_ll);
        nextChandrabalSpeakerIV = (ImageView) view.findViewById(R.id.chandra_bal_speaker_iv);
        nextChandrabalSpeakerLL = (LinearLayout) view.findViewById(R.id.chandra_bal_speaker_ll);
        chandrabalPB = (ProgressBar) view.findViewById(R.id.progressBar);
        nextChandrabalPB = (ProgressBar) view.findViewById(R.id.progressBar1);
        //Note view
//        view = rootView.findViewById(R.id.note_view);
//        view.findViewById(R.id.note_layout).setVisibility(View.VISIBLE);
//        noteHeadingTV = (TextView) view.findViewById(R.id.note_heading_tv);
//        noteDescTV1 = (TextView) view.findViewById(R.id.note_desc_tv1);
//        noteDescTV2 = (TextView) view.findViewById(R.id.note_desc_tv2);
//        noteSpeakerIV = (ImageView) view.findViewById(R.id.note_speaker_iv);
        //Sun Rise view
        view = rootView.findViewById(R.id.sun_rise_view);
        sunRiseCV = (CardView) view.findViewById(R.id.cardview);
        sunRiseHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        sunRiseNameTV = (TextView) view.findViewById(R.id.name_tv);
        sunRiseTimeTV = (TextView) view.findViewById(R.id.time_tv);
        sunRiseGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        sunRiseSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        sunRiseSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        sunRisePB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Sun set view
        view = rootView.findViewById(R.id.sun_set_view);
        sunSetCV = (CardView) view.findViewById(R.id.cardview);
        sunSetHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        sunSetNameTV = (TextView) view.findViewById(R.id.name_tv);
        sunSetTimeTV = (TextView) view.findViewById(R.id.time_tv);
        sunSetGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        sunSetSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        sunSetSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        sunSetPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Moon rise view
        view = rootView.findViewById(R.id.moon_rise_view);
        moonRiseCV = (CardView) view.findViewById(R.id.cardview);
        moonRiseHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        moonRiseNameTV = (TextView) view.findViewById(R.id.name_tv);
        moonRiseTimeTV = (TextView) view.findViewById(R.id.time_tv);
        moonRiseGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        moonRiseSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        moonRiseSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        moonRisePB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Moon set view
        view = rootView.findViewById(R.id.moon_set_view);
        moonSetCV = (CardView) view.findViewById(R.id.cardview);
        moonSetHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        moonSetNameTV = (TextView) view.findViewById(R.id.name_tv);
        moonSetTimeTV = (TextView) view.findViewById(R.id.time_tv);
        moonSetGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        moonSetSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        moonSetSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        moonSetPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Ritu view
        view = rootView.findViewById(R.id.ritu_view);
        rituCV = (CardView) view.findViewById(R.id.cardview);
        rituHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        rituNameTV = (TextView) view.findViewById(R.id.name_tv);
        rituTimeTV = (TextView) view.findViewById(R.id.time_tv);
        rituGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        rituSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        rituSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        rituPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Moon Sign view
        view = rootView.findViewById(R.id.moon_sign_view);
        moonSignCV = (CardView) view.findViewById(R.id.cardview);
        moonSignHeadingTV = (TextView) view.findViewById(R.id.heading_tv);
        moonSignNameTV = (TextView) view.findViewById(R.id.name_tv);
        moonSignTimeTV = (TextView) view.findViewById(R.id.time_tv);
        moonSignGoodFor = (TextView) view.findViewById(R.id.auspicious_tv);
        moonSignSpeakerIV = (ImageView) view.findViewById(R.id.speaker_iv);
        moonSignSpeakerLL = (LinearLayout) view.findViewById(R.id.speaker_container_ll);
        moonSignPB = (ProgressBar) view.findViewById(R.id.progressBar);
        //Today horoscope and Share view
        /*todayHoroscopeCV = (CardView) rootView.findViewById(R.id.today_horoscope_cv);
        shareCV = (CardView) rootView.findViewById(R.id.share_cv);
        todayHorascopeTV = (TextView) view.findViewById(R.id.today_horoscope_tv);
        shareTV = (TextView) view.findViewById(R.id.share_tv);*/
        todayHorascopeBtn = (Button) rootView.findViewById(R.id.today_horoscope_btn);
        shareBtn = (Button) rootView.findViewById(R.id.share_btn);

        topAdImage = (NetworkImageView) rootView.findViewById(R.id.topAdImage);
        llCustomAdv = (LinearLayout) rootView.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((InputPanchangActivity) activity).regularTypeface, "AKPDA"));
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_31_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_31_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S31");
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
                topAdData = CUtils.getSlotData(adList, "31");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWhatsappData() {
        //whatsAppIV.setVisibility(View.GONE);
    }

    private void setClockData(BeanPlace beanPlace) {
        if (beanPlace != null) {
            timeZoneString = beanPlace.getTimeZoneString();
        }
        FontUtils.changeFont(activity, digitalClockTV, FONT_DIGITAL_MONO);
        digitalClockTV.setTimeZone(TimeZone.getTimeZone(timeZoneString));
        clockIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(clockIV, getCurrentTime(), "ClockSpeakButton");
                    clickedPlayedIV = clockIV;
                } else {
                    if (clickedPlayedIV == clockIV) {
                        resetSpeakBtn(clockIV);
                        //clickedPlayedIV = null;
                    }
                }


            }
        });
    }

    private void setTithiAndDayData() {
        tithiDetailPB.setVisibility(View.GONE);
        tithiTV.setText(getTithiName());
        tithiDayTV.setText(getTithiInt());
        tithiSTTV.setText(getTithiST());
        tithiETTV.setText(getTithiET());
        tithiWeekDayTV.setText(model.getVaara());
        tithiDateTV.setText(getDate(beanDateTime.getCalender().getTime(), "MMM dd, yyyy"));

        if (monthType == MONTH_PURNIMANT) {
            tithiMonthAndPakshTV.setText(model.getMonthPurnimanta() + " - " + model.getPakshaName());
        } else {
            tithiMonthAndPakshTV.setText(model.getMonthAmanta() + " - " + model.getPakshaName());
        }
        tithiMonthAndPakshTV1.setVisibility(View.GONE);
        tithiFestTV.setVisibility(View.GONE);
        tithiIV.setImageResource(moonImages[(int) (model.getTithiInt()[0]) - 1]);
        setRobotoMediumTypeface(tithiTitleTV);
        setRobotoMediumTypeface(tithiTV);
        setRobotoMediumTypeface(tithiDayTV);
        setRobotoRegularTypeface(tithiSTTV);
        setRobotoRegularTypeface(tithiETTV);
        setRobotoMediumTypeface(tithiWeekDayTV);
        setRobotoMediumTypeface(tithiFestTV);
        setRobotoRegularTypeface(tithiDateTV);
        setRobotoRegularTypeface(tithiMonthAndPakshTV);
        setRobotoRegularTypeface(tithiMonthAndPakshTV1);


        final String resultStr = getPunchangString();
        tithiDetailSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(tithiDetailSpeakerIV, resultStr, "TithiAndDaySpeakButton");
                    clickedPlayedIV = tithiDetailSpeakerIV;
                } else {
                    if (clickedPlayedIV == tithiDetailSpeakerIV) {
                        resetSpeakBtn(tithiDetailSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });

        tithiDetailCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Tithi detail CardView");//1 means hora
            }
        });

        infoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    CUtils.openLanguageSelectDialog(getActivity().getSupportFragmentManager());
                }
            }
        });
    }

    private void setHeadingText() {
        currentMuhuratHeadingTV.setText(getResources().getString(R.string.current_doghati));
        panchangHeadingTV.setText(getResources().getString(R.string.todays_panchang));
        hinduMonthYearTV.setText(getResources().getString(R.string.hindu_month_and_year));
        auspiciousAndInauspiciousTV.setText(getResources().getString(R.string.auspicious_and_inauspicious));
        sunAndMoonCAlculationTV.setText(getResources().getString(R.string.sun_and_moon_calculation));

        currentMuhuratHeadingIV.setImageResource(R.drawable.current_muhurat);
        panchangHeadingIV.setImageResource(R.drawable.panchang);
        hinduMonthYearIV.setImageResource(R.drawable.hindu_month);
        auspiciousAndInauspiciousIV.setImageResource(R.drawable.auspicious);
        sunAndMoonCAlculationIV.setImageResource(R.drawable.sun_moon);

        currentMuhuratHeadingIV.setVisibility(View.VISIBLE);
        panchangHeadingIV.setVisibility(View.VISIBLE);
        hinduMonthYearIV.setVisibility(View.VISIBLE);
        auspiciousAndInauspiciousIV.setVisibility(View.VISIBLE);
        sunAndMoonCAlculationIV.setVisibility(View.VISIBLE);

        setRobotoMediumTypeface(currentMuhuratHeadingTV);
        setRobotoMediumTypeface(panchangHeadingTV);
        setRobotoMediumTypeface(hinduMonthYearTV);
        setRobotoMediumTypeface(auspiciousAndInauspiciousTV);
        setRobotoMediumTypeface(sunAndMoonCAlculationTV);
    }


    private void setHoraData(List<HoraMetadata> dataCurrent, List<HoraMetadata> datatimeCurrent, List<HoraMetadata> datatimeexitCurrent) {
        int number = getCurrentHoraNumber(datatimeCurrent, datatimeexitCurrent);
        String startTime = "", endTime = "", horaName = "", horaVal = "";
        if (dataCurrent.size() > number) {
            horaName = dataCurrent.get(number).getPlanetdata();
            horaNameTV.setText(horaName);
        }

        if (datatimeCurrent.size() > number && datatimeexitCurrent.size() > number) {
            startTime = CUtils.convertTimeToAmPm(datatimeCurrent.get(number).getEntertimedata()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
            endTime = CUtils.convertTimeToAmPm(datatimeexitCurrent.get(number).getExittimedata()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                horaTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime + " " + getResources().getString(R.string.till));
            } else {
                horaTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime);
            }
        }
        if (dataCurrent.size() > number) {
            horaVal = dataCurrent.get(number).getPlanetCurrentHorameaning();
            horaGoodFor.setText(horaVal);
        }


        //horaSpeakerIV.setAlpha(50f);
        setRobotoMediumTypeface(horaHeadingTV);
        setRobotoRegularTypeface(horaNameTV);
        setRobotoMediumTypeface(horaTimeTV);
        setRobotoRegularTypeface(horaGoodFor);

        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            startTime = startTime.replace("AM", "");
            startTime = startTime.replace("PM", "");
            endTime = endTime.replace("AM", "");
            endTime = endTime.replace("PM", "");
        }

        String[] startTimeArr = startTime.split(":");
        String[] endTimeArr = endTime.split(":");
        //result = "वर्तमान होरा " + data.get(number).getPlanetdata() + " है जो " + data.get(number).getPlanetCurrentHorameaning() + " है, " + startTime[0] + " बजकर " + startTime[1] + " मिनट से शुरू होकर " + endTime[0] + " बजकर " + endTime[1] + " मिनट पर ख़त्म होगा";
        String speakingStr = getResources().getString(R.string.hora_str).replace("#hora", horaName);


        speakingStr = speakingStr.replace("#result", horaVal);
        speakingStr = speakingStr.replace("#starthour", removeZero(startTimeArr[0]));
        speakingStr = speakingStr.replace("#startminute", removeZero(startTimeArr[1]));
        speakingStr = speakingStr.replace("#endhour", removeZero(endTimeArr[0]));
        speakingStr = speakingStr.replace("#endminute", removeZero(endTimeArr[1]));

        final String resultStr = speakingStr;
        final boolean isClicked = false;
        horaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(horaSpeakerIV, resultStr, "HoraSpeakButton");
                    clickedPlayedIV = horaSpeakerIV;
                } else {
                    if (clickedPlayedIV == horaSpeakerIV) {
                        resetSpeakBtn(horaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }


            }
        });
        horaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(3, "Hora CardView");//3 means hora
            }
        });
    }

    private void setChogdiyaData(List<HoraMetadata> datatimeCurrent, List<HoraMetadata> datatimeexitCurrent, List<HoraMetadata> dataCurrent) {
        int number = getCurrentChogdiaNumber(datatimeCurrent, datatimeexitCurrent);
        String startTime = "", endTime = "", chogdiaName = "", chogdiaVal = "";
        if (dataCurrent.size() > number) {
            chogdiaName = dataCurrent.get(number).getPlanetdata();
            chogdiyaNameTV.setText(chogdiaName);
        }
        if (datatimeCurrent.size() > number && datatimeexitCurrent.size() > number) {
            startTime = CUtils.convertTimeToAmPm(datatimeCurrent.get(number).getEntertimedata()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
            endTime = CUtils.convertTimeToAmPm(datatimeexitCurrent.get(number).getExittimedata()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                chogdiyaTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime + " " + getResources().getString(R.string.till));
            } else {
                chogdiyaTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime);
            }
        }
        if (dataCurrent.size() > number) {
            chogdiaVal = dataCurrent.get(number).getPlanetmeaning();
            chogdiyaGoodFor.setText(dataCurrent.get(number).getPlanetmeaning());
        }


        setRobotoMediumTypeface(chogdiyaHeadingTV);
        setRobotoRegularTypeface(chogdiyaNameTV);
        setRobotoMediumTypeface(chogdiyaTimeTV);
        setRobotoRegularTypeface(chogdiyaGoodFor);
       /* String speakingStr = getResources().getString(R.string.hora_speaking_str);
        speakingStr = speakingStr.replace("#strattime", startTime);
        speakingStr = speakingStr.replace("#endtime", endTime);
        speakingStr = speakingStr.replace("#name", chogdiaName);
        speakingStr = speakingStr.replace("#val", chogdiaVal);*/
        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            startTime = startTime.replace("AM", "");
            startTime = startTime.replace("PM", "");
            endTime = endTime.replace("AM", "");
            endTime = endTime.replace("PM", "");
        }
        String[] startTimeArr = startTime.split(":");
        String[] endTimeArr = endTime.split(":");
        //result = "वर्तमान होरा " + data.get(number).getPlanetdata() + " है जो " + data.get(number).getPlanetCurrentHorameaning() + " है, " + startTime[0] + " बजकर " + startTime[1] + " मिनट से शुरू होकर " + endTime[0] + " बजकर " + endTime[1] + " मिनट पर ख़त्म होगा";
        String speakingStr = getResources().getString(R.string.chogdiya_str).replace("#chogdiya", chogdiaName);


        speakingStr = speakingStr.replace("#result", chogdiaVal);
        speakingStr = speakingStr.replace("#starthour", removeZero(startTimeArr[0]));
        speakingStr = speakingStr.replace("#startminute", removeZero(startTimeArr[1]));
        speakingStr = speakingStr.replace("#endhour", removeZero(endTimeArr[0]));
        speakingStr = speakingStr.replace("#endminute", removeZero(endTimeArr[1]));


        final String resultStr = speakingStr;
        chogdiyaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(chogdiyaSpeakerIV, resultStr, "ChogdiaSpeakButton");
                    clickedPlayedIV = chogdiyaSpeakerIV;
                } else {
                    if (clickedPlayedIV == chogdiyaSpeakerIV) {
                        resetSpeakBtn(chogdiyaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        chogdiyaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(4, "Chogdia CardView");//4 means chogdia
            }
        });
    }

    private void setRahukalData() {
        String startTime = CUtils.convertTimeToAmPm(model.getRahuKaalVelaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
        String endTime = CUtils.convertTimeToAmPm(model.getRahuKaalVelaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");

        rahukalNameTV.setText(getResources().getString(R.string.currentrahukaal));
        rahukalNameTV.setVisibility(View.GONE);
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            rahukalTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime + " " + getResources().getString(R.string.till));
        } else {
            rahukalTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime);
        }


        rahukalGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(rahukalHeadingTV);
        setRobotoRegularTypeface(rahukalNameTV);
        setRobotoMediumTypeface(rahukalTimeTV);
        // String speakingStr = getResources().getString(R.string.hora_speaking_str);


        String[] startTimeArr;
        String[] endTimeArr;
        Calendar calendar = Calendar.getInstance();
        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            startTime = startTime.replace("AM", "");
            startTime = startTime.replace("PM", "");
            endTime = endTime.replace("AM", "");
            endTime = endTime.replace("PM", "");
        }
        startTimeArr = startTime.split(":");
        endTimeArr = endTime.split(":");


        String speakingStr = getResources().getString(R.string.rahukaal_str);


        speakingStr = speakingStr.replace("#starthour", removeZero(startTimeArr[0]));
        speakingStr = speakingStr.replace("#startminute", removeZero(startTimeArr[1]));
        speakingStr = speakingStr.replace("#endhour", removeZero(endTimeArr[0]));
        speakingStr = speakingStr.replace("#endminute", removeZero(endTimeArr[1]));

        final String resultStr = speakingStr;
        rahukalSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(rahukalSpeakerIV, resultStr, "RahuKalSpeakButton");
                    clickedPlayedIV = rahukalSpeakerIV;
                } else {
                    if (clickedPlayedIV == rahukalSpeakerIV) {
                        resetSpeakBtn(rahukalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        rahukalCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(6, "Rahukal CardView");//6 means rahukaal
            }
        });
    }

    private void setDoGhatiData(List<HoraMetadata> datatimeCurrent, List<HoraMetadata> datatimeexitCurrent, List<HoraMetadata> data) {

        int number = getCurrentHoraNumber(datatimeCurrent, datatimeexitCurrent);
        String startTime = CUtils.convertTimeToAmPm(datatimeCurrent.get(number).getEntertimedata()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
        String endTime = CUtils.convertTimeToAmPm(datatimeexitCurrent.get(number).getExittimedata()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");

        doGhatiNameTV.setText(data.get(number).getPlanetmeaning() + " (" + data.get(number).getPlanetdata() + ")");

        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            doGhatiTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime + " " + getResources().getString(R.string.till));
        } else {
            doGhatiTimeTV.setText(startTime + " " + getResources().getString(R.string.str_to) + " " + endTime);
        }


        doGhatiGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(doGhatiHeadingTV);
        setRobotoRegularTypeface(doGhatiNameTV);
        setRobotoMediumTypeface(doGhatiTimeTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);
        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            startTime = startTime.replace("AM", "");
            startTime = startTime.replace("PM", "");
            endTime = endTime.replace("AM", "");
            endTime = endTime.replace("PM", "");
        }

        String[] startTimeArr = startTime.split(":");
        String[] endTimeArr = endTime.split(":");


        String speakingStr = getResources().getString(R.string.muhurt_str).replace("#muhurt", data.get(number).getPlanetmeaning() + "("
                + data.get(number).getPlanetdata() + ")");


        speakingStr = speakingStr.replace("#starthour", removeZero(startTimeArr[0]));
        speakingStr = speakingStr.replace("#startminute", removeZero(startTimeArr[1]));
        speakingStr = speakingStr.replace("#endhour", removeZero(endTimeArr[0]));
        speakingStr = speakingStr.replace("#endminute", removeZero(endTimeArr[1]));
        final String resultStr = speakingStr;
        doGhatiSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(doGhatiSpeakerIV, resultStr, "DoGhatiSpeakButton");
                    clickedPlayedIV = doGhatiSpeakerIV;
                } else {
                    if (clickedPlayedIV == doGhatiSpeakerIV) {
                        resetSpeakBtn(doGhatiSpeakerIV);

                    }
                }
            }
        });
        doGhatiCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(5, "Do Ghati CardView");//5 means doghati
            }
        });
    }

    private void setTithiData() {
        //String tithiTime = CUtils.convertTimeToAmPm(model.getTithiTime()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
        String tithiTime = model.getTithiTime();
        String tithiVal = model.getTithiValue();
        String pakshaName = model.getPakshaName();
        String nextTithiVal = nextDayModel.getTithiValue();
        String[] tithiTimes = tithiTime.split(",");
        String[] tithiVals = tithiVal.split(",");
        String speakingStr;
        if (tithiVals.length > 1) {
            speakingStr = getResources().getString(R.string.tithi_str1);
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(tithiTimes[0].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replaceFirst("#tithi", tithiVals[0].replace("\n", ""));
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(tithiTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replaceFirst("#tithi", tithiVals[1].replace("\n", ""));

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                tithiNameTV.setText(tithiVals[0] + " - " + getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(tithiTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                tithiTimeTV.setText(tithiVals[1].replace("\n", "") + " - " + getResources().getString(R.string.till_cap) + " - " +
                        CUtils.convertTimeToAmPm(tithiTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

            } else {
                tithiNameTV.setText(tithiVals[0] + " " + CUtils.convertTimeToAmPm(tithiTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap) + ",");
                tithiTimeTV.setText(tithiVals[1].replace("\n", "") + " " +
                        CUtils.convertTimeToAmPm(tithiTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));

            }

        } else {
            speakingStr = getResources().getString(R.string.tithi_str).replace("#month", model.getMonthAmanta());
            speakingStr = speakingStr.replace("#paksh", pakshaName);
            speakingStr = speakingStr.replace("#tithi", tithiVal);
            speakingStr = speakingStr.replace("#date", CUtils.convertTimeToAmPm(tithiTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            tithiNameTV.setText(tithiVals[0]);
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                tithiTimeTV.setText(getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(tithiTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            } else {
                tithiTimeTV.setText(CUtils.convertTimeToAmPm(tithiTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            }
        }


        tithiGoodFor.setText(getResources().getString(R.string.button_next) + " - " + nextTithiVal);


        setRobotoMediumTypeface(tithiHeadingTV);
        setRobotoRegularTypeface(tithiNameTV);
        setRobotoRegularTypeface(tithiTimeTV);
        setRobotoRegularTypeface(tithiGoodFor);

        final String resultStr = speakingStr;
        tithiSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(tithiSpeakerIV, resultStr, "TithiSpeakButton");
                    clickedPlayedIV = tithiSpeakerIV;
                } else {
                    if (clickedPlayedIV == tithiSpeakerIV) {
                        resetSpeakBtn(tithiSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        tithiCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Tithi CardView");//5 means panchang
            }
        });
    }

    private void setPakshaData() {

        pakshaNameTV.setText(model.getPakshaName());

        pakshaGoodFor.setVisibility(View.GONE);
        pakshaTimeTV.setVisibility(View.GONE);

        setRobotoMediumTypeface(pakshaHeadingTV);
        setRobotoRegularTypeface(pakshaNameTV);
        setRobotoMediumTypeface(pakshaTimeTV);
        String speakingStr = getResources().getString(R.string.paksh_str);
        speakingStr = speakingStr.replace("#", model.getPakshaName());

        final String resultStr = speakingStr;
        pakshaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(pakshaSpeakerIV, resultStr, "PakshaSpeakButton");
                    clickedPlayedIV = pakshaSpeakerIV;
                } else {
                    if (clickedPlayedIV == pakshaSpeakerIV) {
                        resetSpeakBtn(pakshaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        pakshaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Paksha CardView");//5 means panchang
            }
        });
    }

    private void setKaranData() {
        String karanVal = model.getKaranaValue();
        String karanTime = model.getKaranaTime();
        String nextKaranVal = nextDayModel.getKaranaValue();

        karanGoodFor.setText(getResources().getString(R.string.button_next) + " - " + nextKaranVal);

        String speakingStr = getResources().getString(R.string.karan_Str);

        String[] karan = karanVal.split(",");
        String[] karanTimeArr = karanTime.split(",");


        speakingStr = speakingStr.replaceFirst("#karan", karan[0].replace("\n", ""));
        speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(karanTimeArr[0].replace("\n", "")));
        if (karan.length > 1) {
            speakingStr = speakingStr.replaceFirst("#karan", karan[1].replace("\n", ""));
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(karanTimeArr[1].replace("\n", "")));

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                karanNameTV.setText(karan[0] + " - " + getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(karanTimeArr[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                karanTimeTV.setText(karan[1].replace("\n", "") + " - " + getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(karanTimeArr[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            } else {
                karanNameTV.setText(karan[0] + " " + CUtils.convertTimeToAmPm(karanTimeArr[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap) + ",");
                karanTimeTV.setText(karan[1].replace("\n", "") + " " + CUtils.convertTimeToAmPm(karanTimeArr[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            }
        } else {
            speakingStr = speakingStr.replaceFirst(getResources().getString(R.string.karan_after_that), "").replace("\n", "");
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                karanNameTV.setText(karan[0] + " - " + getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(karanTimeArr[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            } else {
                karanNameTV.setText(karan[0] + " " + CUtils.convertTimeToAmPm(karanTimeArr[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            }
        }
        final String resultStr = speakingStr;


        setRobotoMediumTypeface(karanHeadingTV);
        setRobotoRegularTypeface(karanNameTV);
        setRobotoRegularTypeface(karanTimeTV);
        setRobotoRegularTypeface(karanGoodFor);

        karanSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(karanSpeakerIV, resultStr, "KaranSpeakButton");
                    clickedPlayedIV = karanSpeakerIV;
                } else {
                    if (clickedPlayedIV == karanSpeakerIV) {
                        resetSpeakBtn(karanSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        karanCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Karan CardView");//5 means panchang
            }
        });
    }

    private void setNakshtraData() {
        //String nakshtraTime = CUtils.convertTimeToAmPm(model.getNakshatraTime()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
        String nakshtraTime = model.getNakshatraTime();
        String nakshtraVal = model.getNakshatraValue();
        String nextNakshtraVal = nextDayModel.getNakshatraValue();
        String[] nakshtraVals = nakshtraVal.split(",");
        String[] nakshtraTimes = nakshtraTime.split(",");
        String speakingStr;
        if (nakshtraVals.length > 1) {
            speakingStr = getResources().getString(R.string.nakshtra_str1);
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(nakshtraTimes[0].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replaceFirst("#Nakshatra", nakshtraVals[0].replace("\n", ""));
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(nakshtraTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replaceFirst("#Nakshatra", nakshtraVals[1].replace("\n", ""));

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                nakshtraNameTV.setText(nakshtraVals[0] + " - " + getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(nakshtraTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                nakshtraTimeTV.setText(nakshtraVals[1].replace("\n", "") + " - " + getResources().getString(R.string.till_cap) + " - " +
                        CUtils.convertTimeToAmPm(nakshtraTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

            } else {
                nakshtraNameTV.setText(nakshtraVals[0] + " " + CUtils.convertTimeToAmPm(nakshtraTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap) + ",");
                nakshtraTimeTV.setText(nakshtraVals[1].replace("\n", "") + " " +
                        CUtils.convertTimeToAmPm(nakshtraTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));

            }

        } else {
            speakingStr = getResources().getString(R.string.nakshtra_str);
            speakingStr = speakingStr.replace("#date", CUtils.convertTimeToAmPm(nakshtraTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replace("#Nakshatra", nakshtraVal);
            nakshtraNameTV.setText(nakshtraVals[0]);
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                nakshtraTimeTV.setText(getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(nakshtraTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            } else {
                nakshtraTimeTV.setText(CUtils.convertTimeToAmPm(nakshtraTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            }
        }

        nakshtraGoodFor.setText(getResources().getString(R.string.button_next) + " - " + nextNakshtraVal);


        setRobotoMediumTypeface(nakshtraHeadingTV);
        setRobotoRegularTypeface(nakshtraNameTV);
        setRobotoRegularTypeface(nakshtraTimeTV);
        setRobotoRegularTypeface(nakshtraGoodFor);


        final String resultStr = speakingStr;
        nakshtraSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(nakshtraSpeakerIV, resultStr, "NakshtraSpeakButton");
                    clickedPlayedIV = nakshtraSpeakerIV;
                } else {
                    if (clickedPlayedIV == nakshtraSpeakerIV) {
                        resetSpeakBtn(nakshtraSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        nakshtraCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Nakshatra CardView");//5 means panchang
            }
        });
    }

    private void setYogaData() {
        //String yogaTime = CUtils.convertTimeToAmPm(model.getYogaTime()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
        String yogaTime = model.getYogaTime();
        String yogaVal = model.getYogaValue();
        String nextYogaVal = nextDayModel.getYogaValue();

        String[] yogaValues = yogaVal.split(",");
        String[] yogaTimes = yogaTime.split(",");
        String speakingStr;
        if (yogaValues.length > 1) {
            speakingStr = getResources().getString(R.string.yog_str1);
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(yogaTimes[0].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replaceFirst("#Yoga", yogaValues[0].replace("\n", ""));
            speakingStr = speakingStr.replaceFirst("#date", CUtils.convertTimeToAmPm(yogaTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replaceFirst("#Yoga", yogaValues[1].replace("\n", ""));

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                yogaNameTV.setText(yogaValues[0] + " - " + getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(yogaTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                yogaTimeTV.setText(yogaValues[1].replace("\n", "") + " - " + getResources().getString(R.string.till_cap) + " - " +
                        CUtils.convertTimeToAmPm(yogaTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            } else {
                yogaNameTV.setText(yogaValues[0] + " " + CUtils.convertTimeToAmPm(yogaTimes[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap) + ",");
                yogaTimeTV.setText(yogaValues[1].replace("\n", "") + " " +
                        CUtils.convertTimeToAmPm(yogaTimes[1].replace("\n", "")).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            }

        } else {
            speakingStr = getResources().getString(R.string.yog_str);
            speakingStr = speakingStr.replace("#date", CUtils.convertTimeToAmPm(yogaTime).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            speakingStr = speakingStr.replace("#Yoga", yogaVal);

            yogaNameTV.setText(yogaVal);
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                yogaTimeTV.setText(getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(yogaTime).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            } else {
                yogaTimeTV.setText(CUtils.convertTimeToAmPm(yogaTime).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + " " + getResources().getString(R.string.till_cap));
            }
        }

        yogaGoodFor.setText(getResources().getString(R.string.button_next) + " - " + nextYogaVal);


        setRobotoMediumTypeface(yogaHeadingTV);
        setRobotoRegularTypeface(yogaNameTV);
        setRobotoRegularTypeface(yogaTimeTV);
        setRobotoRegularTypeface(yogaGoodFor);


        final String resultStr = speakingStr;
        yogaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(yogaSpeakerIV, resultStr, "YogaSpeakButton");
                    clickedPlayedIV = yogaSpeakerIV;
                } else {
                    if (clickedPlayedIV == yogaSpeakerIV) {
                        resetSpeakBtn(yogaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        yogaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Yoga CardView");//5 means panchang
            }
        });
    }

    private void setDayData() {


        dayNameTV.setText(model.getVaara());


        dayTimeTV.setVisibility(View.GONE);
        dayGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(dayHeadingTV);
        setRobotoRegularTypeface(dayNameTV);
        setRobotoMediumTypeface(dayTimeTV);
        String speakingStr = getResources().getString(R.string.var_str);
        speakingStr = speakingStr.replace("#", model.getVaara());
        final String resultStr = speakingStr;
        daySpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(daySpeakerIV, resultStr, "DaySpeakButton");
                    clickedPlayedIV = daySpeakerIV;
                } else {
                    if (clickedPlayedIV == daySpeakerIV) {
                        resetSpeakBtn(daySpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        dayCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Day CardView");//5 means panchang
            }
        });
    }

    private void setShakaSamvatData() {

        shakaSamvatNameTV.setText(model.getShakaSamvatYear());

        shakaSamvatTimeTV.setVisibility(View.GONE);
        shakaSamvatGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(shakaSamvatHeadingTV);
        setRobotoRegularTypeface(shakaSamvatNameTV);
        setRobotoMediumTypeface(shakaSamvatTimeTV);
        String speakingStr = getResources().getString(R.string.current_samvat_str);
        speakingStr = speakingStr.replace("#samvat", getResources().getString(R.string.shaka_samvat));
        speakingStr = speakingStr.replace("#year", model.getShakaSamvatYear());
        final String resultStr = speakingStr;
        shakaSamvatSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(shakaSamvatSpeakerIV, resultStr, "ShakaSamvatSpeakButton");
                    clickedPlayedIV = shakaSamvatSpeakerIV;
                } else {
                    if (clickedPlayedIV == shakaSamvatSpeakerIV) {
                        resetSpeakBtn(shakaSamvatSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        shakaSamvatCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Shaka Samvat CardView");//5 means panchang
            }
        });
    }

    private void setDayDurationData() {

        dayDurationNameTV.setText(model.getDayDuration());

        dayDurationTimeTV.setVisibility(View.GONE);
        dayDurationGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(dayDurationHeadingTV);
        setRobotoRegularTypeface(dayDurationNameTV);
        setRobotoMediumTypeface(dayDurationTimeTV);
        String speakingStr = getResources().getString(R.string.day_duration_str);
        speakingStr = speakingStr.replace("#", model.getDayDuration());
        final String resultStr = speakingStr;
        dayDurationSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(dayDurationSpeakerIV, resultStr, "DayDurationSpeakButton");
                    clickedPlayedIV = dayDurationSpeakerIV;
                } else {
                    if (clickedPlayedIV == dayDurationSpeakerIV) {
                        resetSpeakBtn(dayDurationSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        dayDurationCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Day Duration CardView");//5 means panchang
            }
        });
    }

    private void setKaliSamvatData() {

        kaliSamvatNameTV.setText(model.getKaliSamvat());

        kaliSamvatTimeTV.setVisibility(View.GONE);
        kaliSamvatGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(kaliSamvatHeadingTV);
        setRobotoRegularTypeface(kaliSamvatNameTV);
        setRobotoMediumTypeface(kaliSamvatTimeTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);
        String speakingStr = getResources().getString(R.string.current_samvat_str);
        speakingStr = speakingStr.replace("#samvat", getResources().getString(R.string.kali_samvat));
        speakingStr = speakingStr.replace("#year", model.getKaliSamvat());

        final String resultStr = speakingStr;
        kaliSamvatSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(kaliSamvatSpeakerIV, resultStr, "KaliSamvatSpeakButton");
                    clickedPlayedIV = kaliSamvatSpeakerIV;
                } else {
                    if (clickedPlayedIV == kaliSamvatSpeakerIV) {
                        resetSpeakBtn(kaliSamvatSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        kaliSamvatCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Kali samvat CardView");//5 means panchang
            }
        });
    }

    private void setVikramSamvatData() {

        vikramSamvatNameTV.setText(model.getVikramSamvat());

        vikramSamvatTimeTV.setVisibility(View.GONE);
        vikramSamvatGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(vikramSamvatHeadingTV);
        setRobotoRegularTypeface(vikramSamvatNameTV);
        setRobotoMediumTypeface(vikramSamvatTimeTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);
        String speakingStr = getResources().getString(R.string.current_samvat_str);
        speakingStr = speakingStr.replace("#samvat", getResources().getString(R.string.vikram_samvat));
        speakingStr = speakingStr.replace("#year", model.getVikramSamvat());

        final String resultStr = speakingStr;
        vikramSamvatSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(vikramSamvatSpeakerIV, resultStr, "VikramSamvatSpeakButton");
                    clickedPlayedIV = vikramSamvatSpeakerIV;
                } else {
                    if (clickedPlayedIV == vikramSamvatSpeakerIV) {
                        resetSpeakBtn(vikramSamvatSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        vikramSamvatCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Vikram Samvat CardView");//5 means panchang
            }
        });
    }

    private void setMonthAmanthaData() {

        monthAmanthaNameTV.setText(model.getMonthAmanta());

        monthAmanthaTimeTV.setVisibility(View.GONE);
        monthAmanthaGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(monthAmanthaHeadingTV);
        setRobotoRegularTypeface(monthAmanthaNameTV);
        setRobotoMediumTypeface(monthAmanthaTimeTV);
        String speakingStr = getResources().getString(R.string.amant_hmonth_str);
        speakingStr = speakingStr.replace("#", model.getMonthAmanta());
        final String resultStr = speakingStr;
        monthAmanthaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(monthAmanthaSpeakerIV, resultStr, "MonthAmantaSpeakButton");
                    clickedPlayedIV = monthAmanthaSpeakerIV;
                } else {
                    if (clickedPlayedIV == monthAmanthaSpeakerIV) {
                        resetSpeakBtn(monthAmanthaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        monthAmanthaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Month Amanta CardView");//5 means panchang
            }
        });
    }

    private void setMonthPurnimantData() {

        monthPurnimantNameTV.setText(model.getMonthPurnimanta());

        monthPurnimantTimeTV.setVisibility(View.GONE);
        monthPurnimantGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(monthPurnimantHeadingTV);
        setRobotoRegularTypeface(monthPurnimantNameTV);
        setRobotoMediumTypeface(monthPurnimantTimeTV);
        String speakingStr = getResources().getString(R.string.purnimant_month_str);
        speakingStr = speakingStr.replace("#", model.getMonthPurnimanta());
        final String resultStr = speakingStr;
        monthPurnimantSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(monthPurnimantSpeakerIV, resultStr, "MonthPurnimantSpeakButton");
                    clickedPlayedIV = monthPurnimantSpeakerIV;
                } else {
                    if (clickedPlayedIV == monthPurnimantSpeakerIV) {
                        resetSpeakBtn(monthPurnimantSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        monthPurnimantCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Month Purnimanta CardView");//5 means panchang
            }
        });
    }

    private void setAbhijitData() {


        abhijitGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(abhijitHeadingTV);
        setRobotoRegularTypeface(abhijitNameTV);
        setRobotoRegularTypeface(abhijitTimeTV);
        String speakingStr;
        if (!model.getAbhijitFrom().equals(getResources().getString(R.string.none))) {
            abhijitNameTV.setText(CUtils.convertTimeToAmPm(model.getAbhijitFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            abhijitTimeTV.setText(CUtils.convertTimeToAmPm(model.getAbhijitTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = getResources().getString(R.string.abhijit_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getAbhijitFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getAbhijitTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            abhijitTimeTV.setVisibility(View.VISIBLE);
        } else {
            speakingStr = getResources().getString(R.string.abhijit_no_str);
            abhijitNameTV.setText(getResources().getString(R.string.none));
            abhijitTimeTV.setVisibility(View.GONE);
        }


        final String resultStr = speakingStr;
        abhijitSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(abhijitSpeakerIV, resultStr, "AbhjitSpeakButton");
                    clickedPlayedIV = abhijitSpeakerIV;
                } else {
                    if (clickedPlayedIV == abhijitSpeakerIV) {
                        resetSpeakBtn(abhijitSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        abhijitCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Abhijit CardView");//5 means panchang
            }
        });
    }

    private void setDustMuhuratData() {

        String dustMuhuratST = model.getDushtaMuhurtasFrom();
        String dustMuhuratET = model.getDushtaMuhurtasTo();
        String[] dmst = dustMuhuratST.split("\n");
        String[] dmet = dustMuhuratET.split("\n");
        String speakingStr;
        if (!model.getDushtaMuhurtasFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.dust_yes_str1);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(dmst[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(dmet[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            dustMuhuratNameTV.setText(CUtils.convertTimeToAmPm(dmst[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            dustMuhuratTimeTV.setText(CUtils.convertTimeToAmPm(dmet[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

            if (dmst.length > 1) {

                speakingStr = getResources().getString(R.string.dust_yes_str2);
                speakingStr = speakingStr.replace("#start1", CUtils.convertTimeToAmPm(dmst[1]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                speakingStr = speakingStr.replace("#end1", CUtils.convertTimeToAmPm(dmet[1]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

                speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(dmst[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(dmet[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

                dustMuhuratNameTV.setText(CUtils.convertTimeToAmPm(dmst[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + ", " + CUtils.convertTimeToAmPm(dmst[1]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                dustMuhuratTimeTV.setText(CUtils.convertTimeToAmPm(dmet[0]).replace("+", getResources().getString(R.string.tomorrow_label) + " ") + ", " + CUtils.convertTimeToAmPm(dmet[1]).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
                dustMuhuratTimeTV.setVisibility(View.VISIBLE);
            }
        } else {
            speakingStr = getResources().getString(R.string.dust_no_str);
            dustMuhuratNameTV.setText(getResources().getString(R.string.none));
            dustMuhuratTimeTV.setVisibility(View.GONE);

        }


        dustMuhuratGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(dustMuhuratHeadingTV);
        setRobotoRegularTypeface(dustMuhuratNameTV);
        setRobotoRegularTypeface(dustMuhuratTimeTV);

        final String resultStr = speakingStr;
        dustMuhuratSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(dustMuhuratSpeakerIV, resultStr, "DustMuhuratSpeakButton");
                    clickedPlayedIV = dustMuhuratSpeakerIV;
                } else {
                    if (clickedPlayedIV == dustMuhuratSpeakerIV) {
                        resetSpeakBtn(dustMuhuratSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        dustMuhuratCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Dust Muhurat CardView");//5 means panchang
            }
        });
    }

    private void setKantakaData() {

        kantakaNameTV.setText(CUtils.convertTimeToAmPm(model.getKantaka_MrityuFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        kantakaTimeTV.setText(CUtils.convertTimeToAmPm(model.getKantaka_MrityuTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

        kantakaGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(kantakaHeadingTV);
        setRobotoRegularTypeface(kantakaNameTV);
        setRobotoRegularTypeface(kantakaTimeTV);
        String speakingStr;
        if (!model.getKantaka_MrityuFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.kantka_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getKantaka_MrityuFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getKantaka_MrityuTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        } else {
            speakingStr = getResources().getString(R.string.kantka_no_str);
        }
        final String resultStr = speakingStr;
        kantakaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(kantakaSpeakerIV, resultStr, "KantkaSpeakButton");
                    clickedPlayedIV = kantakaSpeakerIV;
                } else {
                    if (clickedPlayedIV == kantakaSpeakerIV) {
                        resetSpeakBtn(kantakaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        kantakaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Kantka CardView");//5 means panchang
            }
        });

    }

    private void setYamghantaData() {

        yamaghantaNameTV.setText(CUtils.convertTimeToAmPm(model.getYamaghantaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        yamaghantaTimeTV.setText(CUtils.convertTimeToAmPm(model.getYamaghantaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

        yamaghantaGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(yamaghantaHeadingTV);
        setRobotoRegularTypeface(yamaghantaNameTV);
        setRobotoRegularTypeface(yamaghantaTimeTV);
        String speakingStr;
        if (!model.getYamaghantaFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.yamghanta_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getYamaghantaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getYamaghantaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        } else {
            speakingStr = getResources().getString(R.string.yamghanta_no_str);
        }
        final String resultStr = speakingStr;
        yamaghantaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(yamaghantaSpeakerIV, resultStr, "YamghantaSpeakButton");
                    clickedPlayedIV = yamaghantaSpeakerIV;
                } else {
                    if (clickedPlayedIV == yamaghantaSpeakerIV) {
                        resetSpeakBtn(yamaghantaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        yamaghantaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Yamghanta CardView");//5 means panchang
            }
        });
    }

    private void setKulikaData() {

        kulikaNameTV.setText(CUtils.convertTimeToAmPm(model.getKulikaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        kulikaTimeTV.setText(CUtils.convertTimeToAmPm(model.getKulikaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

        kulikaGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(kulikaHeadingTV);
        setRobotoRegularTypeface(kulikaNameTV);
        setRobotoRegularTypeface(kulikaTimeTV);
        String speakingStr;
        if (!model.getKulikaFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.kulika_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getKulikaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getKulikaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        } else {
            speakingStr = getResources().getString(R.string.kulika_no_str);
        }
        final String resultStr = speakingStr;
        kulikaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(kulikaSpeakerIV, resultStr, "KulikaSpeakButton");
                    clickedPlayedIV = kulikaSpeakerIV;
                } else {
                    if (clickedPlayedIV == kulikaSpeakerIV) {
                        resetSpeakBtn(kulikaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        kulikaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Kulika CardView");//5 means panchang
            }
        });
    }

    private void setKalavelaData() {

        kalavelaNameTV.setText(CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        kalavelaTimeTV.setText(CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

        kalavelaGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(kalavelaHeadingTV);
        setRobotoRegularTypeface(kalavelaNameTV);
        setRobotoRegularTypeface(kalavelaTimeTV);
        String speakingStr;
        if (!model.getKalavela_ArdhayaamFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.kalavela_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        } else {
            speakingStr = getResources().getString(R.string.kalavela_no_str);
        }
        final String resultStr = speakingStr;
        kalavelaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(kalavelaSpeakerIV, resultStr, "KalavelaSpeakButton");
                    clickedPlayedIV = kalavelaSpeakerIV;
                } else {
                    if (clickedPlayedIV == kalavelaSpeakerIV) {
                        resetSpeakBtn(kalavelaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        kalavelaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Kalvela CardView");//5 means panchang
            }
        });
    }

    private void setYamgandaData() {

        yamagandaNameTV.setText(CUtils.convertTimeToAmPm(model.getYamagandaVelaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        yamagandaTimeTV.setText(CUtils.convertTimeToAmPm(model.getYamagandaVelaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

        yamagandaGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(yamagandaHeadingTV);
        setRobotoRegularTypeface(yamagandaNameTV);
        setRobotoRegularTypeface(yamagandaTimeTV);
        String speakingStr;
        if (!model.getYamagandaVelaFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.yamaganda_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getYamagandaVelaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getYamagandaVelaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        } else {
            speakingStr = getResources().getString(R.string.yamaganda_no_str);
        }
        final String resultStr = speakingStr;
        yamagandaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(yamagandaSpeakerIV, resultStr, "YamgandaSpeakButton");
                    clickedPlayedIV = yamagandaSpeakerIV;
                } else {
                    if (clickedPlayedIV == yamagandaSpeakerIV) {
                        resetSpeakBtn(yamagandaSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        yamagandaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Yamaganda CardView");//5 means panchang
            }
        });
    }

    private void setGulikakalData() {

        gulikaKalNameTV.setText(CUtils.convertTimeToAmPm(model.getGulikaKaalVelaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        gulikaKalTimeTV.setText(CUtils.convertTimeToAmPm(model.getGulikaKaalVelaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));

        gulikaKalGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(gulikaKalHeadingTV);
        setRobotoRegularTypeface(gulikaKalNameTV);
        setRobotoRegularTypeface(gulikaKalTimeTV);
        String speakingStr;
        if (!model.getGulikaKaalVelaFrom().equals(getResources().getString(R.string.none))) {
            speakingStr = getResources().getString(R.string.gulikakaal_yes_str);
            speakingStr = speakingStr.replace("#start", CUtils.convertTimeToAmPm(model.getGulikaKaalVelaFrom()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
            speakingStr = speakingStr.replace("#end", CUtils.convertTimeToAmPm(model.getGulikaKaalVelaTo()).replace("+", getResources().getString(R.string.tomorrow_label) + " "));
        } else {
            speakingStr = getResources().getString(R.string.gulikakaal_no_str);
        }
        final String resultStr = speakingStr;
        gulikaKalSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(gulikaKalSpeakerIV, resultStr, "GulikakalSpeakButton");
                    clickedPlayedIV = gulikaKalSpeakerIV;
                } else {
                    if (clickedPlayedIV == gulikaKalSpeakerIV) {
                        resetSpeakBtn(gulikaKalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        gulikaKalCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Gulika kal CardView");//5 means panchang
            }
        });
    }

    /*private void setTarabalAndChandrabalData() {
        tarabalHeadingTV.setText(getResources().getString(R.string.tara_bala));
        chandrabalHeadingTV.setText(getResources().getString(R.string.chandra_bala));
        noteHeadingTV.setText(getResources().getString(R.string.note));
        chandrabalDescTV.setText(model.getChandraBala());
        tarabalDescTV.setText(model.getTaraBala());
        noteDescTV1.setText(getResources().getString(R.string.panchang_note));
        noteDescTV2.setText(getResources().getString(R.string.panchang_note_head));
        setRobotoMediumTypeface(tarabalHeadingTV);
        setRobotoMediumTypeface(chandrabalHeadingTV);
        setRobotoMediumTypeface(noteHeadingTV);
        setRobotoRegularTypeface(chandrabalDescTV);
        setRobotoRegularTypeface(tarabalDescTV);
        setRobotoRegularTypeface(noteDescTV1);
        setRobotoRegularTypeface(noteDescTV2);
        String taraBalSpeakingStr = getResources().getString(R.string.tara_bala_str);
        taraBalSpeakingStr = taraBalSpeakingStr.replace("#", model.getTaraBala());
        final String taraBalResultStr = taraBalSpeakingStr;
        String chandraBalSpeakingStr = getResources().getString(R.string.chandra_bala_str);
        chandraBalSpeakingStr = chandraBalSpeakingStr.replace("#", model.getChandraBala());
        final String chandraBalResultStr = chandraBalSpeakingStr;
        tarabalSpeakerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(tarabalSpeakerIV, taraBalResultStr, "TarabalSpeakButton");
                    clickedPlayedIV = tarabalSpeakerIV;
                } else {
                    if (clickedPlayedIV == tarabalSpeakerIV) {
                        resetSpeakBtn(tarabalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        chandrabalSpeakerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(chandrabalSpeakerIV, chandraBalResultStr, "ChandraBalSpeakButton");
                    clickedPlayedIV = chandrabalSpeakerIV;
                } else {
                    if (clickedPlayedIV == chandrabalSpeakerIV) {
                        resetSpeakBtn(chandrabalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        tarabalCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Tarabal CardView");//5 means panchang
            }
        });
    }*/

    private void setPanchakData() {
        panchakPB.setVisibility(View.GONE);

        FestDataDetail festDataDetail = null;
        panchakGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(panchakHeadingTV);
        setRobotoRegularTypeface(panchakNameTV);
        setRobotoRegularTypeface(panchakTimeTV);
        if (panchakDataList != null && currentPanchak != -1) {
            festDataDetail = panchakDataList.get(currentPanchak);
            currentPanchak = -1;
        }
        String speakingStr;


        if (festDataDetail != null) {

            panchakNameTV.setText(getResources().getString(R.string.start_time) + ": " + getBhadraDateTimeStr(festDataDetail.getFestStartDate(), festDataDetail.getStarthr(), festDataDetail.getStartmin()));
            panchakTimeTV.setText(getResources().getString(R.string.end_times) + ": " + getBhadraDateTimeStr(festDataDetail.getFestEndDate(), festDataDetail.getEndhr(), festDataDetail.getEndmin()));
            speakingStr = getResources().getString(R.string.panchak_yes_str);
            speakingStr = speakingStr.replace("#sd", getDate(getDateFromString(festDataDetail.getFestStartDate(), "dd/MM/yyyy"), "dd/MM/yyyy"));
            speakingStr = speakingStr.replace("#ed", getDate(getDateFromString(festDataDetail.getFestStartDate(), "dd/MM/yyyy"), "dd/MM/yyyy"));
            speakingStr = speakingStr.replace("#st", CUtils.convertTimeToAmPm(festDataDetail.getStarthr() + ":" + festDataDetail.getStartmin()));
            speakingStr = speakingStr.replace("#et", CUtils.convertTimeToAmPm(festDataDetail.getEndhr() + ":" + festDataDetail.getEndmin()));

        } else {
            panchakNameTV.setText(getResources().getString(R.string.start_time) + ": " + getResources().getString(R.string.none));
            panchakTimeTV.setText(getResources().getString(R.string.end_times) + ": " + getResources().getString(R.string.none));
            speakingStr = getResources().getString(R.string.panchak_no_str);
        }
        final String resultStr = speakingStr;
        panchakSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(panchakSpeakerIV, resultStr, "PanchakSpeakButton");
                    clickedPlayedIV = panchakSpeakerIV;
                } else {
                    if (clickedPlayedIV == panchakSpeakerIV) {
                        resetSpeakBtn(panchakSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        panchakCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(7, "Panchak CardView");//7 means panchak
            }
        });
    }

    private void setBhadraData() {
        bhadraPB.setVisibility(View.GONE);
        FestDataDetail festDataDetail = null;
        bhadraGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(bhadraHeadingTV);
        setRobotoRegularTypeface(bhadraNameTV);
        setRobotoRegularTypeface(bhadraTimeTV);
        if (bhadraDataList != null && currentBhadra != -1) {
            festDataDetail = bhadraDataList.get(currentBhadra);
            currentBhadra = -1;
        }
        String speakingStr;
        if (festDataDetail != null) {

            bhadraNameTV.setText(getResources().getString(R.string.start_time) + ": " + getBhadraDateTimeStr(festDataDetail.getFestStartDate(), festDataDetail.getStarthr(), festDataDetail.getStartmin()));
            bhadraTimeTV.setText(getResources().getString(R.string.end_times) + ": " + getBhadraDateTimeStr(festDataDetail.getFestEndDate(), festDataDetail.getEndhr(), festDataDetail.getEndmin()));
            speakingStr = getResources().getString(R.string.bhadra_yes_str);
            speakingStr = speakingStr.replace("#sd", getDate(getDateFromString(festDataDetail.getFestStartDate(), "dd/MM/yyyy"), "dd/MM/yyyy"));
            speakingStr = speakingStr.replace("#ed", getDate(getDateFromString(festDataDetail.getFestEndDate(), "dd/MM/yyyy"), "dd/MM/yyyy"));
            speakingStr = speakingStr.replace("#st", CUtils.convertTimeToAmPm(festDataDetail.getStarthr() + ":" + festDataDetail.getStartmin()));
            speakingStr = speakingStr.replace("#et", CUtils.convertTimeToAmPm(festDataDetail.getEndhr() + ":" + festDataDetail.getEndmin()));
        } else {

            bhadraNameTV.setText(getResources().getString(R.string.start_time) + ": " + getResources().getString(R.string.none));
            bhadraTimeTV.setText(getResources().getString(R.string.end_times) + ": " + getResources().getString(R.string.none));
            speakingStr = getResources().getString(R.string.bhadra_no_str);
        }
        final String resultStr = speakingStr;
        bhadraCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(8, "Bhadra kal CardView");//8 means bhadra
            }
        });
        bhadraSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(bhadraSpeakerIV, resultStr, "BhadraSpeakButton");
                    clickedPlayedIV = bhadraSpeakerIV;
                } else {
                    if (clickedPlayedIV == bhadraSpeakerIV) {
                        resetSpeakBtn(bhadraSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
    }

    private void setDishaShooData() {

//        dishaShoolKeyTV.setText(getResources().getString(R.string.disha_shoola));
        dishaShoolValTV.setText(model.getDishaShoola());


        setRobotoMediumTypeface(dishaShoolHeadingTV);
//        setRobotoMediumTypeface(dishaShoolKeyTV);
        setRobotoRegularTypeface(dishaShoolValTV);
        String speakingStr = getResources().getString(R.string.dishsool_str);
        speakingStr = speakingStr.replace("#", model.getDishaShoola());
        final String resultStr = speakingStr;
        dishaShoolSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(dishaShoolSpeakerIV, resultStr, "DishaShoolSpeakButton");
                    clickedPlayedIV = dishaShoolSpeakerIV;
                } else {
                    if (clickedPlayedIV == dishaShoolSpeakerIV) {
                        resetSpeakBtn(dishaShoolSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        dishaShoolCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Dish shool CardView");//5 means panchang
            }
        });
    }

    private void setLagnaData() {

        setRobotoMediumTypeface(lagnaHeadingTV);
        setRobotoMediumTypeface(lagnaStartnEndTimeTV);
        setRobotoRegularTypeface(lagnaNameTV);
        setRobotoRegularTypeface(lagnaNatureCurrentTV);

        lagnaSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] startTimeArr = startTimeToShare.split(":");
                    String[] endTimeArr = endTimeToShare.split(":");

                    String speakingStr = getResources().getString(R.string.lagna_str).replace("#lagna", lagnaNameTV.getText().toString() + "("
                            + lagnaNatureCurrentTV.getText().toString() + ")");

                    speakingStr = speakingStr.replace("#starthour", removeZero(startTimeArr[0]));
                    speakingStr = speakingStr.replace("#startminute", removeZero(startTimeArr[1]));
                    speakingStr = speakingStr.replace("#endhour", removeZero(endTimeArr[0]));
                    speakingStr = speakingStr.replace("#endminute", removeZero(endTimeArr[1]));

                    final String resultStr = speakingStr;
                    if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {

                        imagePlayPauseClick(lagnaSpeakerIV, resultStr, "LagnaSpeakButton");
                        clickedPlayedIV = lagnaSpeakerIV;
                    } else {
                        if (clickedPlayedIV == lagnaSpeakerIV) {
                            resetSpeakBtn(lagnaSpeakerIV);
                            //clickedPlayedIV = null;
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
        lagnaCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(10, "Lagna CardView");//5 means panchang
            }
        });
    }

    private void setTarabalData() {
        String taraBal = model.getTaraBala();
        String nextTaraBal = nextDayModel.getTaraBala();
        String naksTime = CUtils.convertTimeToAmPm(model.getNakshatraTime()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            tarabalHeadingTV.setText(getResources().getString(R.string.tara_bala_today) +
                    " (" + getResources().getString(R.string.till_cap) + " - " + naksTime + ")");
        } else {
            tarabalHeadingTV.setText(getResources().getString(R.string.tara_bala_today) +
                    " (" + naksTime + " " + getResources().getString(R.string.till_cap) + ")");
        }

        tarabalDescTV.setText(taraBal);
        nextTarabalHeadingTV.setText(getResources().getString(R.string.next_tara_bala));
        nextTarabalDescTV.setText(nextTaraBal);
        //nextTarabalDescTV.setBackgroundColor(getResources().getColor(R.color.color_tarabalam_background));
        setRobotoMediumTypeface(tarabalHeadingTV);
        setRobotoRegularTypeface(tarabalDescTV);
        setRobotoMediumTypeface(nextTarabalHeadingTV);
        setRobotoRegularTypeface(nextTarabalDescTV);
        String taraBalSpeakingStr = getResources().getString(R.string.tara_bala_str);
        taraBalSpeakingStr = taraBalSpeakingStr.replace("#", taraBal);
        final String taraBalResultStr = taraBalSpeakingStr;

        String nextTaraBalSpeakingStr = getResources().getString(R.string.next_tara_bala_str);
        nextTaraBalSpeakingStr = nextTaraBalSpeakingStr.replace("#", nextTaraBal);
        final String nextTaraBalResultStr = nextTaraBalSpeakingStr;
        tarabalSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(tarabalSpeakerIV, taraBalResultStr, "TarabalSpeakButton");
                    clickedPlayedIV = tarabalSpeakerIV;
                } else {
                    if (clickedPlayedIV == tarabalSpeakerIV) {
                        resetSpeakBtn(tarabalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });

        nextTarabalSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(nextTarabalSpeakerIV, nextTaraBalResultStr, "TarabalSpeakButton");
                    clickedPlayedIV = nextTarabalSpeakerIV;
                } else {
                    if (clickedPlayedIV == nextTarabalSpeakerIV) {
                        resetSpeakBtn(nextTarabalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });

        tarabalCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Tarabal CardView");//5 means panchang
            }
        });
    }

    private void setChandrabalData() {
        String chandraBal = model.getChandraBala();
        String moonSignTime = model.getMoonSignTime();
        String nextChandraBal = nextDayModel.getChandraBala();
        if (moonSignTime == null) {
            chandrabalHeadingTV.setText(getResources().getString(R.string.chandra_bala_today));
            nextChandrabalHeadingTV.setVisibility(View.GONE);
            nextChandrabalDescTV.setVisibility(View.GONE);
            nextChandrabalSpeakerLL.setVisibility(View.GONE);
        } else {
            nextChandrabalHeadingTV.setVisibility(View.VISIBLE);
            nextChandrabalDescTV.setVisibility(View.VISIBLE);
            nextChandrabalSpeakerLL.setVisibility(View.VISIBLE);
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                chandrabalHeadingTV.setText(getResources().getString(R.string.chandra_bala_today) + " (" +
                        getResources().getString(R.string.till_cap) + " - " + CUtils.convertTimeToAmPm(moonSignTime) + ")");
            } else {
                chandrabalHeadingTV.setText(getResources().getString(R.string.chandra_bala_today) + " (" +
                        CUtils.convertTimeToAmPm(moonSignTime) + " " + getResources().getString(R.string.till_cap) + ")");
            }
        }
        chandrabalDescTV.setText(chandraBal);
        nextChandrabalHeadingTV.setText(getResources().getString(R.string.next_chandrabal));
        nextChandrabalDescTV.setText(nextChandraBal);
        //chandrabalDescTV.setBackgroundColor(getResources().getColor(R.color.color_chandrabal_background));
        setRobotoMediumTypeface(chandrabalHeadingTV);
        setRobotoRegularTypeface(chandrabalDescTV);
        setRobotoMediumTypeface(nextChandrabalHeadingTV);
        setRobotoRegularTypeface(nextChandrabalDescTV);
        String chandraBalSpeakingStr = getResources().getString(R.string.chandra_bala_str);
        chandraBalSpeakingStr = chandraBalSpeakingStr.replace("#", chandraBal);
        final String chandraBalResultStr = chandraBalSpeakingStr;

        String nextChandraBalSpeakingStr = getResources().getString(R.string.next_chandra_bala_str);
        nextChandraBalSpeakingStr = nextChandraBalSpeakingStr.replace("#", nextChandraBal);
        final String nextChandraBalResultStr = nextChandraBalSpeakingStr;
        chandrabalSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(chandrabalSpeakerIV, chandraBalResultStr, "ChandraBalSpeakButton");
                    clickedPlayedIV = chandrabalSpeakerIV;
                } else {
                    if (clickedPlayedIV == chandrabalSpeakerIV) {
                        resetSpeakBtn(chandrabalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        nextChandrabalSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(nextChandrabalSpeakerIV, nextChandraBalResultStr, "ChandraBalSpeakButton");
                    clickedPlayedIV = nextChandrabalSpeakerIV;
                } else {
                    if (clickedPlayedIV == nextChandrabalSpeakerIV) {
                        resetSpeakBtn(nextChandrabalSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        chandrabalCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Chandra bal CardView");//5 means panchang
            }
        });
    }

    private void setSunRiseData(BeanPlace beanPlace) {
        String sunRiseTime = CUtils.convertTimeToAmPm(model.getSunRise()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");


        sunRiseNameTV.setText(sunRiseTime);
        //sunRiseNameTV.setBackgroundColor(getResources().getColor(R.color.color_sun_rise_background));
        sunRiseNameTV.setPadding(8, 8, 8, 8);
        sunRiseTimeTV.setVisibility(View.GONE);
        sunRiseGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(sunRiseHeadingTV);
        setRobotoRegularTypeface(sunRiseNameTV);
        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            sunRiseTime = sunRiseTime.replace("AM", "");
            sunRiseTime = sunRiseTime.replace("PM", "");
        }
        // String speakingStr = getResources().getString(R.string.hora_speaking_str);
        String[] timeArray = sunRiseTime.split(":");
        String speakingStr = getResources().getString(R.string.sun_rise_str).replace("#hour", removeZero(timeArray[0]));
        speakingStr = speakingStr.replace("#minute", removeZero(timeArray[1]));
        if (beanPlace != null) {
            speakingStr = speakingStr.replace("#place", beanPlace.getCityName());
        } else {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                speakingStr = speakingStr.replace("#place में", "");
            }
        }

        final String resultStr = speakingStr;
        sunRiseSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(sunRiseSpeakerIV, resultStr, "SunRiseSpeakButton");
                    clickedPlayedIV = sunRiseSpeakerIV;
                } else {
                    if (clickedPlayedIV == sunRiseSpeakerIV) {
                        resetSpeakBtn(sunRiseSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        sunRiseCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Sun Rise CardView");//5 means panchang
            }
        });
    }

    private void setSunSetData(BeanPlace beanPlace) {
        String sunSetTime = CUtils.convertTimeToAmPm(model.getSunSet()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");

        sunSetNameTV.setText(sunSetTime);
        //sunSetNameTV.setBackgroundColor(getResources().getColor(R.color.color_sunset_background));
        sunSetNameTV.setPadding(8, 8, 8, 8);


        sunSetTimeTV.setVisibility(View.GONE);
        sunSetGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(sunSetHeadingTV);
        setRobotoRegularTypeface(sunSetNameTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);
        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            sunSetTime = sunSetTime.replace("AM", "");
            sunSetTime = sunSetTime.replace("PM", "");
        }

        String[] timeArray = sunSetTime.split(":");
        String speakingStr = getResources().getString(R.string.sun_set_str).replace("#hour", removeZero(timeArray[0]));
        speakingStr = speakingStr.replace("#minute", removeZero(timeArray[1]));
        if (beanPlace != null) {
            speakingStr = speakingStr.replace("#place", beanPlace.getCityName());
        } else {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                speakingStr = speakingStr.replace("#place में", "");
            }

        }

        final String resultStr = speakingStr;
        sunSetSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(sunSetSpeakerIV, resultStr, "SunSetSpeakButton");
                    clickedPlayedIV = sunSetSpeakerIV;
                } else {
                    if (clickedPlayedIV == sunSetSpeakerIV) {
                        resetSpeakBtn(sunSetSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        sunSetCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Sun Set CardView");//5 means panchang
            }
        });
    }

    private void setMoonRiseData(BeanPlace beanPlace) {
        String moonRiseTime = CUtils.convertTimeToAmPm(model.getMoonRise()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");

        moonRiseNameTV.setText(moonRiseTime);
        //moonRiseNameTV.setBackgroundColor(getResources().getColor(R.color.color_moon_rise_background));
        moonRiseNameTV.setPadding(8, 8, 8, 8);


        moonRiseTimeTV.setVisibility(View.GONE);
        moonRiseGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(moonRiseHeadingTV);
        setRobotoRegularTypeface(moonRiseNameTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);

        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            moonRiseTime = moonRiseTime.replace("AM", "");
            moonRiseTime = moonRiseTime.replace("PM", "");
        }
        String[] timeArray = moonRiseTime.split(":");
        String speakingStr = getResources().getString(R.string.moon_rise_str).replace("#hour", removeZero(timeArray[0]));
        if (timeArray.length > 1) {
            speakingStr = speakingStr.replace("#minute", removeZero(timeArray[1]));
        }

        if (beanPlace != null) {
            speakingStr = speakingStr.replace("#place", beanPlace.getCityName());
        } else {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                speakingStr = speakingStr.replace("#place में", "");
            }
        }

        final String resultStr = speakingStr;
        moonRiseSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(moonRiseSpeakerIV, resultStr, "MoonRiseSpeakButton");
                    clickedPlayedIV = moonRiseSpeakerIV;
                } else {
                    if (clickedPlayedIV == moonRiseSpeakerIV) {
                        resetSpeakBtn(moonRiseSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        moonRiseCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Moon Rise CardView");//5 means panchang
            }
        });
    }

    private void setMoonSetData(BeanPlace beanPlace) {
        String moonSetTime = CUtils.convertTimeToAmPm(model.getMoonSet()).replace("+", getResources().getString(R.string.tomorrow_label) + " ");

        moonSetNameTV.setText(moonSetTime);
        // moonSetNameTV.setBackgroundColor(getResources().getColor(R.color.color_moon_set_background));
        moonSetNameTV.setPadding(8, 8, 8, 8);
        moonSetTimeTV.setVisibility(View.GONE);
        moonSetGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(moonSetHeadingTV);
        setRobotoRegularTypeface(moonSetNameTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);

        if (LANGUAGE_CODE != CGlobalVariables.ENGLISH) {
            moonSetTime = moonSetTime.replace("AM", "");
            moonSetTime = moonSetTime.replace("PM", "");
        }
        String[] timeArray = moonSetTime.split(":");
        String speakingStr = getResources().getString(R.string.moon_set_str).replace("#hour", removeZero(timeArray[0]));
        if (timeArray.length > 1) {
            speakingStr = speakingStr.replace("#minute", removeZero(timeArray[1]));
        } else {
            speakingStr = speakingStr.replace("#minute", "");
        }
        if (beanPlace != null) {
            speakingStr = speakingStr.replace("#place", beanPlace.getCityName());
        } else {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                speakingStr = speakingStr.replace("#place में", "");
            }
        }
        final String resultStr = speakingStr;
        moonSetSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(moonSetSpeakerIV, resultStr, "MoonSetSpeakButton");
                    clickedPlayedIV = moonSetSpeakerIV;
                } else {
                    if (clickedPlayedIV == moonSetSpeakerIV) {
                        resetSpeakBtn(moonSetSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        moonSetCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Moon Set CardView");//5 means panchang
            }
        });
    }

    private void setRituData() {

        rituNameTV.setText(model.getRitu());

        rituTimeTV.setVisibility(View.GONE);
        rituGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(rituHeadingTV);
        setRobotoRegularTypeface(rituNameTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);
        String speakingStr = getResources().getString(R.string.ritu_str).replace("#", model.getRitu());

        final String resultStr = speakingStr;
        rituSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(rituSpeakerIV, resultStr, "RituSpeakButton");
                    clickedPlayedIV = rituSpeakerIV;
                } else {
                    if (clickedPlayedIV == rituSpeakerIV) {
                        resetSpeakBtn(rituSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        rituCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Ritu CardView");//5 means panchang
            }
        });
    }

    private void setMoonSignData() {
        String moonSign = model.getMoonSignValue();
        String moonSignTime = model.getMoonSignTime();
        if (moonSignTime == null) {
            moonSignNameTV.setText(moonSign);

        } else {
            moonSignTime = CUtils.convertTimeToAmPm(moonSignTime).replace("+", " ");
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                moonSignNameTV.setText(moonSign + " - " + getResources().getString(R.string.till_cap) + " - " + moonSignTime);
            } else {
                moonSignNameTV.setText(moonSign + " " + moonSignTime + " " + getResources().getString(R.string.till_cap));

            }
        }


        moonSignTimeTV.setVisibility(View.GONE);
        moonSignGoodFor.setVisibility(View.GONE);

        setRobotoMediumTypeface(moonSignHeadingTV);
        setRobotoRegularTypeface(moonSignNameTV);
        //String speakingStr = getResources().getString(R.string.hora_speaking_str);
        String speakingStr = getResources().getString(R.string.chandra_rashi_str).replace("#", model.getMoonSignValue());

        final String resultStr = speakingStr;
        moonSignSpeakerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    imagePlayPauseClick(moonSignSpeakerIV, resultStr, "MoonSignSpeakButton");
                    clickedPlayedIV = moonSignSpeakerIV;
                } else {
                    if (clickedPlayedIV == moonSignSpeakerIV) {
                        resetSpeakBtn(moonSignSpeakerIV);
                        //clickedPlayedIV = null;
                    }
                }
            }
        });
        moonSignCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(1, "Moon Sign CardView");//5 means panchang
            }
        });
    }

    private void setShareAndTodayHoroscopeData() {
        setRobotoMediumTypeface(todayHorascopeBtn);
        setRobotoMediumTypeface(shareBtn);
        todayHorascopeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToFrag(2, "Horoscope CardView");//2 means panchang
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.shareToFriendMail(activity);
            }
        });
    }

    /*   private void getHoraData(int year, int month, int day, String latitude,
                                String longitude, String timezone) {
           flag = true;
           List<HoraMetadata> data = new ArrayList<HoraMetadata>();
           List<HoraMetadata> datatime = new ArrayList<HoraMetadata>();
           List<HoraMetadata> datatimeexit = new ArrayList<HoraMetadata>();

           List<HoraMetadata> dataCurrent = new ArrayList<HoraMetadata>();
           List<HoraMetadata> datatimeCurrent = new ArrayList<HoraMetadata>();
           List<HoraMetadata> datatimeexitCurrent = new ArrayList<HoraMetadata>();

           List<HoraMetadata> dataCurrentN = new ArrayList<HoraMetadata>();
           List<HoraMetadata> datatimeCurrentN = new ArrayList<HoraMetadata>();
           List<HoraMetadata> datatimeexitCurrentN = new ArrayList<HoraMetadata>();

           if (beanPlace != null) {
               latitude = beanPlace.getLatitude();
               longitude = beanPlace.getLongitude();

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


               timezone = beanPlace.getTimeZone();

           }
           String Horatag = "Horatag";

           // //
           Calendar ca = beanDateTime.getCalender();
           int day_of_month = ca.get(Calendar.DAY_OF_WEEK) - 1;
           // //

           data = HoraLordName(day_of_month);
           HoraEndTime(year, month, day, latitude, longitude, timezone, datatime, datatimeexit);


           // System.out.println("ssss" + datatime.size() + datatimeexit.size());

          *//* if (flag) {
            Calendar calendarCurrent = Calendar.getInstance();
            int hours = calendarCurrent.get(Calendar.HOUR);
            int minutes = calendarCurrent.get(Calendar.MINUTE);

            int day_Current = ca.get(Calendar.DAY_OF_WEEK) - 1;

            HoraLordNameCurrent(day_Current, true, dataCurrentN, dataCurrent);
            HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), latitude, longitude, timezone, true, datatimeCurrentN, datatimeCurrent
                    , datatimeexitCurrentN, datatimeexitCurrent);

            //int numberx = getCurrentHoraNumber(datatimeCurrentN, datatimeexitCurrentN);
            if (datatimeCurrentN.size() > 0) {
                if (calendarCurrent.get(Calendar.AM_PM) == Calendar.AM && (hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) ||
                        hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) &&
                                minutes < Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(3, 5)))) {
                    int day_of_month_Current = ca.get(Calendar.DAY_OF_WEEK) - 2;

                    HoraLordNameCurrent(day_of_month_Current, false, dataCurrentN, dataCurrent);
                    HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), latitude, longitude, timezone, false,
                            datatimeCurrentN, datatimeCurrent
                            , datatimeexitCurrentN, datatimeexitCurrent);


                    flag = false;
                } else {
                    int day_of_month_Current = ca.get(Calendar.DAY_OF_WEEK) - 1;

                    HoraLordNameCurrent(day_of_month_Current, false, dataCurrentN, dataCurrent);
                    HoraEndTimeCurrent(calendarCurrent.get(Calendar.YEAR), calendarCurrent.get(Calendar.MONTH), calendarCurrent.get(Calendar.DAY_OF_MONTH), latitude, longitude, timezone, false,
                            datatimeCurrentN, datatimeCurrent
                            , datatimeexitCurrentN, datatimeexitCurrent);

                    flag = false;
                }
            }
        }*//*
        final List<HoraMetadata> data1 = data;
        final List<HoraMetadata> datatime1 = datatime;
        final List<HoraMetadata> datatimeexit1 = datatimeexit;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    setHoraData(data1, datatime1, datatimeexit1);
                }
            });

        }


    }*/
    private void getHoraData(int year, int month, int day, BeanPlace beanPlace) {
        flag = true;
       /* List<HoraMetadata> data = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatime = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeexit = new ArrayList<HoraMetadata>();*/

        List<HoraMetadata> dataCurrent = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeCurrent = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeexitCurrent = new ArrayList<HoraMetadata>();

        List<HoraMetadata> dataCurrentN = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeCurrentN = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeexitCurrentN = new ArrayList<HoraMetadata>();
        //added by Ankit on 3-9-2019
        objPanchangUtil = new PanchangUtil();

        BeanPlace beanPlace1 = getBeanObj(beanPlace);
        String latitude = beanPlace1.getLatitude();
        String longitude = beanPlace1.getLongitude();
        String timeZone = beanPlace1.getTimeZone();

       /* if (beanPlace != null) {
            latitude = beanPlace.getLatitude();
            longitude = beanPlace.getLongitude();

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


            timezone = beanPlace.getTimeZone();

        }*/

        // String Horatag = "Horatag";

        // //
        Calendar ca = beanDateTime.getCalender();
        //int day_of_month = ca.get(Calendar.DAY_OF_WEEK) - 1;
        // //

        //HoraLordName(day_of_month);
        //HoraEndTime(year, month, day, latitude, longitude, timezone, datatime, datatimeexit);


        // System.out.println("ssss" + datatime.size() + datatimeexit.size());

        /*if (flag) {*/
        //Calendar calendarCurrent = Calendar.getInstance();
        int hours = ca.get(Calendar.HOUR);
        int minutes = ca.get(Calendar.MINUTE);

        int day_Current = ca.get(Calendar.DAY_OF_WEEK) - 1;

        HoraLordNameCurrent(day_Current, true, dataCurrentN, dataCurrent);
        HoraEndTimeCurrent(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), latitude, longitude, timeZone, true, datatimeCurrentN, datatimeCurrent
                , datatimeexitCurrentN, datatimeexitCurrent);
        //int numberx = getCurrentHoraNumber(datatimeCurrentN, datatimeexitCurrentN);
        if (datatimeCurrentN.size() > 0) {
            if (ca.get(Calendar.AM_PM) == Calendar.AM && (hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) ||
                    hours <= Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(0, 2)) &&
                            minutes < Integer.parseInt(datatimeCurrentN.get(0).getEntertimedata().substring(3, 5)))) {
                int day_of_month_Current = ca.get(Calendar.DAY_OF_WEEK) - 2;

                HoraLordNameCurrent(day_of_month_Current, false, dataCurrentN, dataCurrent);
                HoraEndTimeCurrent(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), latitude, longitude, timeZone, false, datatimeCurrentN, datatimeCurrent
                        , datatimeexitCurrentN, datatimeexitCurrent);

                flag = false;
            } else {
                int day_of_month_Current = ca.get(Calendar.DAY_OF_WEEK) - 1;

                HoraLordNameCurrent(day_of_month_Current, false, dataCurrentN, dataCurrent);
                HoraEndTimeCurrent(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), latitude, longitude, timeZone, false, datatimeCurrentN, datatimeCurrent
                        , datatimeexitCurrentN, datatimeexitCurrent);
                flag = false;
            }
            //}
        }

        /*if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {*/
        setHoraData(dataCurrent, datatimeCurrent, datatimeexitCurrent);
           /*     }
            });
*/
        //}
    }


    private List<HoraMetadata> HoraLordName(int day_of_month) {
        int dayLordForDayHora[] = new int[24];
        List<HoraMetadata> loardName = new ArrayList<HoraMetadata>();
        try {
            String PlanetName[] = getResources().getStringArray(
                    R.array.hora_planets);
            String PlanetNameMeaning[] = getResources().getStringArray(
                    R.array.hora_planets_meaning);
            String PlanetNameMeaningforcurrentHora[] = getResources()
                    .getStringArray(R.array.pla_mean);


            for (int i = 0; i < 24; i++) {
                dayLordForDayHora[i] = (day_of_month + (i * 5)) % 7;
                HoraMetadata hora = new HoraMetadata();
                hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
                hora.setPlanetmeaning(PlanetNameMeaning[dayLordForDayHora[i]]);
                hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[dayLordForDayHora[i]]);
                loardName.add(hora);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loardName;

    }

    private int[] HoraLordNameCurrent(int day_of_month, boolean flag, List<HoraMetadata> dataCurrentN, List<HoraMetadata> dataCurrent) {
        int dayLordForDayHora[] = new int[24];
        try {
            String PlanetName[] = getResources().getStringArray(
                    R.array.hora_planets);
            String PlanetNameMeaning[] = getResources().getStringArray(
                    R.array.hora_planets_meaning);
            String PlanetNameMeaningforcurrentHora[] = getResources()
                    .getStringArray(R.array.pla_mean);


            for (int i = 0; i < 24; i++) {
                dayLordForDayHora[i] = (day_of_month + (i * 5)) % 7;
                HoraMetadata hora = new HoraMetadata();
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

    private void HoraEndTime(int year, int month, int day, String latitude,
                             String longitude, String timezone, List<HoraMetadata> datatime, List<HoraMetadata> datatimeexit) {

        int jd = (int) Masa.toJulian(year, month + 1, day);
        double sunRise = model.getSunRiseDouble();
        double sunSet = model.getSunSetDouble();

        try {

            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (NumberFormatException ex) {

        } catch (Exception e) {
            Log.i("", e.getMessage());
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

        Place place = new Place(lat, lng, tzone);
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
                                            sunSet, 12)[i], 0));
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
       /* return Muhurta
                .getDayDivisons(jd, place, Double.parseDouble(model.getSunRise()), 12);
*/
    }

    private double[] HoraEndTimeCurrent(int year, int month, int day, String latitude,
                                        String longitude, String timezone, boolean flag
            , List<HoraMetadata> datatimeCurrentN, List<HoraMetadata> datatimeCurrent
            , List<HoraMetadata> datatimeexitCurrentN, List<HoraMetadata> datatimeexitCurrent) {

        int jd = (int) Masa.toJulian(year, month + 1, day);
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
        Double sunRise = Masa.getSunRise(jd, place);
        Double sunSet = Masa.getSunSet(jd, place);
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
        return Muhurta
                .getDayDivisons(jd, place, sunRise, 12);

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
            System.out.println(e.getMessage().toString());

        }

        return currentHoraNumber;
    }

    private void setRobotoMediumTypeface(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(((BaseInputActivity) activity).mediumTypeface);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(((BaseInputActivity) activity).mediumTypeface);
        }
    }

    private void setRobotoRegularTypeface(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(((BaseInputActivity) activity).regularTypeface);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(((BaseInputActivity) activity).regularTypeface);
        }
    }


    private void getChogdiaData(int year, int month, int day, BeanPlace beanPlace) {
        flag = true;
        List<HoraMetadata> data = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatime = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeexit = new ArrayList<HoraMetadata>();

        BeanPlace beanPlace1 = getBeanObj(beanPlace);
        String latitude = beanPlace1.getLatitude();
        String longitude = beanPlace1.getLongitude();
        String timezone = beanPlace1.getTimeZone();

        chogdiaLordName(year, month, day, data);
        chogdiaEndTime(year, month, day, latitude, longitude, timezone, datatime, datatimeexit);


        final List<HoraMetadata> datatimeCurrent1 = datatime;
        final List<HoraMetadata> datatimeexitCurrent1 = datatimeexit;
        final List<HoraMetadata> dataCurrent1 = data;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    setChogdiyaData(datatimeCurrent1, datatimeexitCurrent1, dataCurrent1);
                }
            });
        }


    }

    private int chogdiaLordName(int year, int month, int day, List<HoraMetadata> data) {

        Calendar calendar = new GregorianCalendar(year, month, day);
        int reslut = calendar.get(Calendar.DAY_OF_WEEK);
        int valueforday = getStartChoghadiaForDay(reslut);
        int valuefornight = getStartChoghadiaForNight(reslut);

        String[] chogadiyanameforday = getChogadiaDayName(valueforday);
        String[] chogadiyanamefornight = getChogadiaNightName(valuefornight);
        String[] chogadiyanamefordaymeaning = getChogadiaDayNameMeaning(valueforday);
        String[] chogadiyanamefornightmeaning = getChogadiaNightNameMeaning(valuefornight);

        for (int i = 0; i < 8; i++) {

            HoraMetadata hora = new HoraMetadata();
            hora.setPlanetdata(chogadiyanameforday[i]);

            hora.setPlanetmeaning(chogadiyanamefordaymeaning[i]);
            data.add(hora);
        }
        for (int i = 0; i < 8; i++) {

            HoraMetadata hora1 = new HoraMetadata();
            hora1.setPlanetdata(chogadiyanamefornight[i]);

            hora1.setPlanetmeaning(chogadiyanamefornightmeaning[i]);
            data.add(hora1);
        }

        return year;

    }

    private void chogdiaEndTime(int year, int month, int day, String latitude,
                                String longitude, String timezone,
                                List<HoraMetadata> datatime, List<HoraMetadata> datatimeexit) {

        int jd = (int) Masa.toJulian(year, month + 1, day);
        Double sunRise = model.getSunRiseDouble();
        Double sunSet = model.getSunSetDouble();
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

        Place place = new Place(lat, lng, tzone);

        for (i = 0; i < 9; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i <= 7) {
                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        sunRise, 8)[i], 0));
                datatime.add(hora);

            }
            if (i > 0) {
                hora.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        sunRise, 8)[i], 0));

                datatimeexit.add(hora);

            }

        }
        for (int j = 0; j < 9; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 7) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        sunSet, 8)[j], 0));
                datatime.add(hora1);

            }
            if (j > 0) {
                hora1.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        sunSet, 8)[j], 0));

                datatimeexit.add(hora1);

            }

        }

        //return Muhurta.getDayDivisons(jd, place, sunRise, 8);

    }

    private int chogdiaLordNameCurrent(int year, int month, int day, boolean flag,
                                       List<HoraMetadata> dataCurrentN, List<HoraMetadata> dataCurrent) {

        Calendar calendar = new GregorianCalendar(year, month, day);
        int reslut = calendar.get(Calendar.DAY_OF_WEEK);
        int valueforday = getStartChoghadiaForDay(reslut);
        int valuefornight = getStartChoghadiaForNight(reslut);

        String[] chogadiyanameforday = getChogadiaDayName(valueforday);
        String[] chogadiyanamefornight = getChogadiaNightName(valuefornight);
        String[] chogadiyanamefordaymeaning = getChogadiaDayNameMeaning(valueforday);
        String[] chogadiyanamefornightmeaning = getChogadiaNightNameMeaning(valuefornight);

        for (int i = 0; i < 8; i++) {

            HoraMetadata hora = new HoraMetadata();
            hora.setPlanetdata(chogadiyanameforday[i]);

            hora.setPlanetmeaning(chogadiyanamefordaymeaning[i]);
            if (flag) {
                dataCurrentN.add(hora);
            } else {
                dataCurrent.add(hora);
            }
        }
        for (int i = 0; i < 8; i++) {

            HoraMetadata hora1 = new HoraMetadata();
            hora1.setPlanetdata(chogadiyanamefornight[i]);

            hora1.setPlanetmeaning(chogadiyanamefornightmeaning[i]);
            if (flag) {
                dataCurrentN.add(hora1);
            } else {
                dataCurrent.add(hora1);
            }
        }

        return year;

    }

    private double[] chogdiaEndTimeCurrent(int year, int month, int day, String latitude,
                                           String longitude, String timezone, boolean flag,
                                           List<HoraMetadata> datatimeCurrentN, List<HoraMetadata> datatimeCurrent, List<HoraMetadata> datatimeexitCurrentN, List<HoraMetadata> datatimeexitCurrent) {

        int jd = (int) Masa.toJulian(year, month + 1, day);

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

        Place place = new Place(lat, lng, tzone);
        Double sunRise = Masa.getSunRise(jd, place);
        Double sunSet = Masa.getSunSet(jd, place);
        for (i = 0; i < 9; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i <= 7) {
                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        sunRise, 8)[i], 0));
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
                                        sunRise, 8)[i], 0));
                if (flag) {
                    datatimeexitCurrentN.add(hora);
                } else {
                    datatimeexitCurrent.add(hora);
                }

            }

        }
        for (int j = 0; j < 9; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 7) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        sunSet, 8)[j], 0));
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
                                        sunSet, 8)[j], 0));
                if (flag) {
                    datatimeexitCurrentN.add(hora1);
                } else {
                    datatimeexitCurrent.add(hora1);
                }

            }

        }

        return Muhurta.getDayDivisons(jd, place, sunRise, 8);

    }

    private int getStartChoghadiaForDay(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                startDay = 0;
                break;
            case Calendar.MONDAY:
                startDay = 3;
                break;
            case Calendar.TUESDAY:
                startDay = 6;
                break;
            case Calendar.WEDNESDAY:
                startDay = 2;
                break;
            case Calendar.THURSDAY:
                startDay = 5;
                break;
            case Calendar.FRIDAY:
                startDay = 1;
                break;
            case Calendar.SATURDAY:
                startDay = 4;
                break;
        }
        return startDay;
    }

    private int getStartChoghadiaForNight(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                startNight = 0;
                break;
            case Calendar.MONDAY:
                startNight = 2;
                break;
            case Calendar.TUESDAY:
                startNight = 4;
                break;
            case Calendar.WEDNESDAY:
                startNight = 6;
                break;
            case Calendar.THURSDAY:
                startNight = 1;
                break;
            case Calendar.FRIDAY:
                startNight = 3;
                break;
            case Calendar.SATURDAY:
                startNight = 5;
                break;
        }
        return startNight;
    }

    private String[] getChogadiaDayName(int startDay) {
        String dayChoghadiaName[] = new String[8];
        String chogadiyadayName[] = getResources().getStringArray(
                R.array.chogadiya_day_names_list);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            dayChoghadiaName[j] = chogadiyadayName[i];
        }
        for (int l = 0; l <= startDay; l++) {
            dayChoghadiaName[j] = chogadiyadayName[l];
            j++;
        }
        return dayChoghadiaName;
    }

    private String[] getChogadiaDayNameMeaning(int startDay) {
        String dayChoghadiaNameMeaning[] = new String[8];
        String chogadiyadayName[] = getResources().getStringArray(
                R.array.chogadiya_day_names_list_meaning);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            dayChoghadiaNameMeaning[j] = chogadiyadayName[i];
        }
        for (int l = 0; l <= startDay; l++) {
            dayChoghadiaNameMeaning[j] = chogadiyadayName[l];
            j++;
        }
        return dayChoghadiaNameMeaning;
    }

    private String[] getChogadiaNightName(int startDay) {
        String nightChoghadiaName[] = new String[8];
        String chogadiyaNightName[] = getResources().getStringArray(
                R.array.chogadiya_night_names_list);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            nightChoghadiaName[j] = chogadiyaNightName[i];
        }
        for (int l = 0; l <= startDay; l++) {
            nightChoghadiaName[j] = chogadiyaNightName[l];
            j++;
        }
        return nightChoghadiaName;
    }

    private String[] getChogadiaNightNameMeaning(int startDay) {
        String nightChoghadiaNameMeaning[] = new String[8];
        String chogadiyaNightNameMeaning[] = getResources().getStringArray(
                R.array.chogadiya_night_names_list_meaning);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            if (chogadiyaNightNameMeaning.length > i) { //by abhishek
                nightChoghadiaNameMeaning[j] = chogadiyaNightNameMeaning[i];
            }
        }
        for (int l = 0; l <= startDay; l++) {
            if (chogadiyaNightNameMeaning.length > l) {  //by abhishek
                nightChoghadiaNameMeaning[j] = chogadiyaNightNameMeaning[l];
            }
            j++;
        }
        return nightChoghadiaNameMeaning;
    }

    private int getCurrentChogdiaNumber(List<HoraMetadata> datatime2,
                                        List<HoraMetadata> datatimeexit2) {
        String[] resultEntryTime = null;
        String[] resultExitTime = null;

        int currentChogdiaNumber = 0;
        try {
            Calendar calendar = Calendar.getInstance();

            Calendar chogdiaEntryTime = Calendar.getInstance();
            Calendar chogdiaExitTime = Calendar.getInstance();

            for (int i = 0; i <= 15; i++) {
                resultEntryTime = datatime2.get(i).getEntertimedata()
                        .split(":");
                resultExitTime = datatimeexit2.get(i).getExittimedata()
                        .split(":");

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

                chogdiaEntryTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(resultEntryTime[0]));

                chogdiaEntryTime.set(Calendar.MINUTE,
                        Integer.parseInt(resultEntryTime[1]));
                chogdiaEntryTime.set(Calendar.SECOND,
                        Integer.parseInt(resultEntryTime[2]));
                long chogdiaEntryTimeMilliSeconds = chogdiaEntryTime
                        .getTimeInMillis();

                chogdiaExitTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(resultExitTime[0]));

                chogdiaExitTime.set(Calendar.MINUTE,
                        Integer.parseInt(resultExitTime[1]));
                chogdiaExitTime.set(Calendar.SECOND,
                        Integer.parseInt(resultExitTime[2]));
                long chogdiaExitTimeMilliSeconds = chogdiaExitTime.getTimeInMillis();
                if (chogdiaEntryTimeMilliSeconds > chogdiaExitTimeMilliSeconds) {
                    chogdiaExitTimeMilliSeconds = chogdiaExitTimeMilliSeconds + 24
                            * 60 * 60 * 1000;
                }
                if (currentTimeMilliSeconds < chogdiaEntryTimeMilliSeconds) {
                    currentTimeMilliSeconds = currentTimeMilliSeconds + 24 * 60
                            * 60 * 1000;
                }

                if (currentTimeMilliSeconds >= chogdiaEntryTimeMilliSeconds
                        && currentTimeMilliSeconds <= chogdiaExitTimeMilliSeconds) {

                    currentChogdiaNumber = i;
                    break;
                }

            }
        } catch (Exception e) {
        }
        return currentChogdiaNumber;
    }

    private void getDoGhatiData(int year, int month, int day, BeanPlace beanPlace) {
        flag = true;
        List<HoraMetadata> data = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatime = new ArrayList<HoraMetadata>();
        List<HoraMetadata> datatimeexit = new ArrayList<HoraMetadata>();


        BeanPlace beanPlace1 = getBeanObj(beanPlace);
        String latitude = beanPlace1.getLatitude();
        String longitude = beanPlace1.getLongitude();
        String timezone = beanPlace1.getTimeZone();
        timeZoneString = beanPlace1.getTimeZoneString();


        Calendar ca = beanDateTime.getCalender();
        int day_of_month = ca.get(Calendar.DAY_OF_WEEK) - 1;
        // //

        doGhatiLordName(day_of_month, data);


        doGhatiEndTime(year, month, day, latitude, longitude, timezone, timeZoneString, datatime, datatimeexit);


        final List<HoraMetadata> datatimeCurrent1 = datatime;
        final List<HoraMetadata> datatimeexitCurrent1 = datatimeexit;
        final List<HoraMetadata> data1 = data;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    setDoGhatiData(datatimeCurrent1, datatimeexitCurrent1, data1);
                }
            });
        }


    }

    private int[] doGhatiLordName(int day_of_month, List<HoraMetadata> data) {
        int i;
        int dayLordForDayHora[] = new int[30];

        String PlanetName[] = getResources().getStringArray(
                R.array.do_ghati_name_list);
        String PlanetNameMeaning[] = getResources().getStringArray(
                R.array.do_ghati_name_list_Muhurat);

        String PlanetNameMeaningforcurrentHora[] = getResources()
                .getStringArray(R.array.do_ghati_muhurut_meaning);
        String doghatiSecondMeaning[] = getResources().getStringArray(
                R.array.do_ghati_name_list_meaning_second);
        String doghatiSecondMeaningwikipedia[] = getResources().getStringArray(
                R.array.do_ghati_name_list_meaning);
        String doghatimuhurat[] = getResources().getStringArray(
                R.array.do_ghati_huhurat);


        for (i = 0; i < 30; i++) {

            HoraMetadata hora = new HoraMetadata();

            if (i == 0 || i <= 2) {
                hora.setPlanetdata(PlanetName[0]);
            } else if (i == 3 || i <= 5) {
                hora.setPlanetdata(PlanetName[1]);
            } else if (i == 6 || i <= 8) {
                hora.setPlanetdata(PlanetName[2]);
            } else if (i == 9 || i <= 11) {
                hora.setPlanetdata(PlanetName[3]);
            } else if (i == 12 || i <= 14) {
                hora.setPlanetdata(PlanetName[4]);
            } else if (i == 15 || i <= 17) {
                hora.setPlanetdata(PlanetName[5]);
            } else if (i == 18 || i <= 21) {
                hora.setPlanetdata(PlanetName[6]);
            } else if (i == 22 || i <= 22) {
                hora.setPlanetdata(PlanetName[7]);
            } else if (i == 23 || i <= 27) {
                hora.setPlanetdata(PlanetName[8]);
            } else if (i == 28 || i <= 29) {
                hora.setPlanetdata(PlanetName[9]);
            }
            // hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
            hora.setPlanetmeaning(PlanetNameMeaning[i]);
            // hora.setPlanetmeaning(PlanetName[0]);
            hora.setDoghatiSecondMeaning(doghatiSecondMeaning[i]);
            hora.setDoghatiSecondMeaningwikipedia(doghatiSecondMeaningwikipedia[i]);
            hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[i]);
            hora.setDoghatimuhurat(doghatimuhurat[i]);
            data.add(hora);

        }

        return dayLordForDayHora;

    }

    private double[] doGhatiEndTime(int year, int month, int day, String latitude,
                                    String longitude, String timezone, String timeZoneString,
                                    List<HoraMetadata> datatime, List<HoraMetadata> datatimeexit) {

        int jd = (int) Masa.toJulian(year, month + 1, day);
        // System.out.println("LAT LNG TMZNE" + year + month + 1 + day);
        double sunRise = model.getSunRiseDouble();
        double sunSet = model.getSunSetDouble();
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

        //Check timeZone for DST if applicable then set automatically correction
        PanchangUtil objPanchangUtil = new PanchangUtil();
        Date date = beanDateTime.getCalender().getTime();
        if (objPanchangUtil.isDst(timeZoneString, date)) {
            tzone = tzone + 1.0;
        }
        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        Place place = new Place(lat, lng, tzone);

        for (i = 0; i < 16; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i <= 14) {

                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        sunRise, 15)[i], 0));

                Muhurta.getDayDivisons(jd, place, sunRise, 8);

                datatime.add(hora);

            }
            if (i > 0) {
                hora.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        sunRise, 15)[i], 0));
                datatimeexit.add(hora);

            }

        }
        for (int j = 0; j < 16; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 14) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        sunSet, 15)[j], 0));

                datatime.add(hora1);

            }
            if (j > 0) {
                hora1.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        sunSet, 15)[j], 0));
                datatimeexit.add(hora1);

            }

        }

        return Muhurta
                .getDayDivisons(jd, place, sunRise, 15);

    }

    private double[] doGhatiEndTimeCurrent(int year, int month, int day, String latitude,
                                           String longitude, String timezone, String timeZoneString, boolean flag,
                                           List<HoraMetadata> datatimeCurrentN, List<HoraMetadata> datatimeCurrent, List<HoraMetadata> datatimeexitCurrentN, List<HoraMetadata> datatimeexitCurrent) {

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

        //Check timeZone for DST if applicable then set automatically correction
        PanchangUtil objPanchangUtil = new PanchangUtil();
        Date date = beanDateTime.getCalender().getTime();
        if (objPanchangUtil.isDst(timeZoneString, date)) {
            tzone = tzone + 1.0;
        }
        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        Place place = new Place(lat, lng, tzone);
        Double sunRise = Masa.getSunRise(jd, place);
        Double sunSet = Masa.getSunSet(jd, place);
        for (i = 0; i < 16; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i <= 14) {

                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        sunRise, 15)[i], 0));

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
                                        sunRise, 15)[i], 0));
                if (flag) {
                    datatimeexitCurrentN.add(hora);
                } else {
                    datatimeexitCurrent.add(hora);
                }

            }

        }
        for (int j = 0; j < 16; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 14) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        sunSet, 15)[j], 0));
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
                                        sunSet, 15)[j], 0));
                if (flag) {
                    datatimeexitCurrentN.add(hora1);
                } else {
                    datatimeexitCurrent.add(hora1);
                }


            }

        }

        return Muhurta
                .getDayDivisons(jd, place, sunRise, 15);

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
        String cityName = beanPlace1.getCityName();
        if (activity != null) {


            String key = CUtils.getPanchangKey(_datePan, cityName, LANGUAGE_CODE);
            model = CUtils.getPanchangObject(activity, key);
            if (model == null) {
                AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, cityId, language, lat, lng, timeZone, timeZoneString);
                model = calculation.getPanchang();
            }

            Date nextDate = getNextDate(_datePan);
            String nextKey = CUtils.getPanchangKey(nextDate, cityName, LANGUAGE_CODE);
            nextDayModel = CUtils.getPanchangObject(activity, nextKey);
            if (nextDayModel == null) {
                AajKaPanchangCalulation nextDayCalculation = new AajKaPanchangCalulation(nextDate, cityId, language, lat, lng, timeZone, timeZoneString);
                nextDayModel = nextDayCalculation.getPanchang();
            }

            Date preDate = getPreDate(_datePan);
            String preKey = CUtils.getPanchangKey(preDate, cityName, LANGUAGE_CODE);
            preDayModel = CUtils.getPanchangObject(activity, preKey);
            if (preDayModel == null) {
                AajKaPanchangCalulation preDayCalculation = new AajKaPanchangCalulation(preDate, cityId, language, lat, lng, timeZone, timeZoneString);
                preDayModel = preDayCalculation.getPanchang();
            }

            //ArrayList<String> a = CUtils.getAllPanchangKey(activity);


            if (activity != null && CUtils.checkCurrentDate(_datePan)) {
                CUtils.savePanchangObject(activity, key, model);
                CUtils.savePanchangObject(activity, nextKey, nextDayModel);
                CUtils.savePanchangObject(activity, preKey, preDayModel);
            }
        }
    }

    public void updateBirthPlace(BeanPlace beanPlace) {
        this.beanPlace = beanPlace;
        ((InputPanchangActivity) activity).beanPlace = beanPlace;
        //this.beanHoroPersonalInfo.setPlace(beanPlace);
        placeNameTV.setText(beanPlace.getCityName().trim());
        //placeTV.setText(beanPlace.getCityName().trim());
        //placeDetailTV.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));
        try {


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


            //updateBirthDate(beanDateTime);
            //panchang.updateLayout(0);//0 means panchang
        } catch (Exception ex) {
            //Toast.makeText(this,""+ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            //Log.e(ex.getMessage().toString());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            setmTtsCallbackListener(PanchangDashboardFrag.this);
            if (activity != null && isVisibleToUser) {
                // If we are becoming invisible, then...
                BeanDateTimeForPanchang beanDateTimeFromPref = CUtils
                        .getDateTimeForPanchang(activity);
                if (beanDateTimeFromPref != null) {
                    beanDateTime.setCalender(beanDateTimeFromPref.getMonth(),
                            beanDateTimeFromPref.getDay(),
                            beanDateTimeFromPref.getYear());
                    beanDateTime.setDay(beanDateTimeFromPref.getDay());
                    beanDateTime.setMonth(beanDateTimeFromPref.getMonth());
                    beanDateTime.setYear(beanDateTimeFromPref.getYear());

                    //tvDatePicker.setText(getFormatedTextToShowDate(beanDateTime));


                }

                BeanHoroPersonalInfo bHPInfo = CUtils.getBeanHoroPersonalInfo(activity);
                /*if (bHPInfo != null) {
                    this.beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(activity);
                    if (((InputPanchangActivity) activity).beanPlace == null) {
                        this.beanPlace = CUtils.getBeanPalce(activity);
                        placeDetailTV.setText(CUtils.getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));

                    } else {
                        this.beanPlace = ((InputPanchangActivity) activity).beanPlace;
                        placeDetailTV.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));

                    }
                    // this.beanPlace = CUtils.getBeanPalce(activity);

                    placeNameTV.setText(beanPlace.getCityName().trim());
                    //placeTV.setText(beanPlace.getCityName().trim());
                    //this.city_Id = String.valueOf(this.beanPlace.getCityId());
                } else {
                    if (((InputPanchangActivity) activity).beanPlace != null) {
                        this.beanPlace = ((InputPanchangActivity) activity).beanPlace;
                        //this.city_Id = String.valueOf(this.beanPlace.getCityId());
                        placeNameTV.setText(beanPlace.getCityName().trim());
                        //placeTV.setText(beanPlace.getCityName().trim());
                        placeDetailTV.setText(CUtils.getPlaceDetailInSingleString(this.beanPlace));
                    }
                }*/
                flag = true;
                beanPlace = getBeanObj(((InputPanchangActivity) activity).beanPlace);
                updateBirthDate(beanDateTime, beanPlace);

            }
        } catch (Exception ex) {
            //
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void setPlaceAndTime() {
        try {


            btnCurrentDate.setText(getResources().getString(R.string.current_day));
            btnCurrentDate.setTypeface(((BaseInputActivity) activity).mediumTypeface);
            btnCurrentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getCurrentDayData();
                }
            });


            btnPreviousDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getPreviousDayData();
                }
            });


            btnNextDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getNextDayData();
                }
            });


            // ll_card_container = (LinearLayout) view.findViewById(R.id.ll_card_container);

            tvDatePicker.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);

            //beanDateTime = new BeanDateTimeForPanchang();
            tvDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setDate();
                }
            });

            /*layPlaceHolder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openSearchPlace(beanHoroPersonalInfo.getPlace());
                }
            });*/
            /*placeTV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openSearchPlace(beanHoroPersonalInfo.getPlace());
                }
            });*/

            placeNameTV.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
            //placeTV.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
            placeDetailTV.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);


            //CUtils.showAdvertisement(activity,(LinearLayout) view.findViewById(R.id.advLayout));

        } catch (Exception ex) {
            Log.i("", ex.getMessage());
        }
    }

    public void getCurrentDayData() {

      /*calendar = Calendar.getInstance();
      Date _datePan =new Date();
      setLayout(_datePan,city_Id,language,this.beanPlace);*/

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

    public void getPreviousDayData() {
        //beanDateTime;
        //updateBirthDate
        Calendar calendar = beanDateTime.getCalender();
        calendar.add(Calendar.DATE, -1);
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

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* // TODO Auto-generated method stub
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
                    updateDateButtonText();
                }
                break;
        }*/
    }

    public void updateAfterPlaceSelect(BeanPlace place) {
        //((InputPanchangActivity) activity).placeTV.setText(place.getCityName().trim());
        updateBirthPlace(place);
        //load data on place change
        updateDateButtonText(place);
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

    private void showAndroidDatePicker(BeanDateTimeForPanchang beanDateTime) {
        final CustomDatePicker dg = new CustomDatePicker(requireContext(), mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());

        dg.show();
    }

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
        setDateBtn.setOnClickListener(new View.OnClickListener() {

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
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked

        Button resetBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.ResetDateTime);
        resetBtn.setTypeface(((BaseInputActivity) activity).mediumTypeface);
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

    public void updateBirthDate(BeanDateTimeForPanchang beanDateTime, BeanPlace beanPlace) {

        try {
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
        }catch (Exception e){
            Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp2="+e);
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

    public void updateDateButtonText(BeanPlace beanPlace) {
        try {
            Calendar calendar;
            if (beanDateTime != null) {
                calendar = beanDateTime.getCalender();
            } else {
                calendar = Calendar.getInstance();
            }
            try {
                downloadFestivalData(calendar.get(Calendar.YEAR), city_Id, true);
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp4="+e);
            }

            Date date = beanDateTime.getCalender().getTime();
            try {
                setLagnaData(beanPlace, date);
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp5="+e);
            }
            try {
                setLagnaData(beanPlace, date);
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp5="+e);
            }
            try {
                setWhatsappData();
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp6="+e);
            }
            try {
                setClockData(beanPlace);
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp7="+e);
            }

            try {
                setHeadingText();
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp8="+e);
            }
            try {
                loadData(beanPlace);
            }catch (Exception e){
                Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp9="+e);
            }
        }catch (Exception e){
            Log.e("ForPanchang", "InputPanchang  PanchangDashboard Exp3="+e);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        CUtils.hideMyKeyboard(activity);
        super.onResume();

    }


    @Override
    public void resetSpeakBtn(View view) {
        if (view != null) {
            ((ImageView) view).setImageResource(R.drawable.speaker);
        } else {
            return;
        }
        clickedPlayedIV = null;
        if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }
    }

    @Override
    public void disablePlayButton(View view) {
        if (view != null) {
            view.setClickable(false);
            view.setEnabled(false);
            view.setOnClickListener(null);

        }

    }

    @Override
    public void enablePlayButton(View view) {
        if (view != null) {
            view.setClickable(true);
            view.setEnabled(true);

        }

    }

    private void imagePlayPauseClick(View view, String speakingStr, String analyticsStr) {
        // GA
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                analyticsStr,
                null);
        // END

        if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
            ((BaseTtsActivity) activity).setPlayedView(view);
            //String stringToSpeak = "how are you";
            if (speakingStr != null) {
                ((ImageView) view).setImageResource(R.drawable.speaker_off);
                Log.i("TTS", "button clicked: " + speakingStr);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, System.currentTimeMillis() + "");

                if (speakingStr.length() > TTS_CHAR_LIMIT) {
                    speakingStr = speakingStr.substring(0, TTS_CHAR_LIMIT);
                }
                if (mTextToSpeech != null) {
                    int speechStatus = mTextToSpeech.speak(speakingStr, TextToSpeech.QUEUE_FLUSH, map);
                    if (speechStatus == TextToSpeech.ERROR) {
                        //Log.e("TTS", "Error in converting Text to Speech!");
                    }
                }
            }
        } else {
            resetSpeakBtn(view);
        }
    }

    private Date getNextDate(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);  // number of days to add
        return c.getTime();
    }

    private Date getPreDate(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);  // number of days to add
        return c.getTime();
    }

    private void switchToFrag(int pos, String analyticsStr) {
        CUtils.googleAnalyticSendWitPlayServie(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                analyticsStr,
                null);
        panchang.updateLayout(pos);//0 means panchang
    }

    private void checkBhadraCachedData(int selectedYear, int selectedMonth, String cityId, boolean isShowProgressbar) {
        String url = CGlobalVariables.badhraApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&month=" + (selectedMonth + 1) + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(activity).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        bhadraPB.setVisibility(View.GONE);
        if (entry != null) {
            try {
                //  isCached = true;
                String saveData = new String(entry.data, "UTF-8");
                parseBhadraData(saveData, url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {

            if (!CUtils.isConnectedWithInternet(activity)) {
                if (activity != null) {
                    Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                downloadBhadraData(selectedYear, selectedMonth, cityId, isShowProgressbar);
            }
        }
    }

    private void downloadBhadraData(final int selectedYear, final int selectedMonth, final String cityId, boolean isShowProgressbar) {

        final String url = CGlobalVariables.badhraApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&month=" + (selectedMonth + 1) + "&year=" + selectedYear;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null && !response.isEmpty()) {
                                // Gson gson = new Gson();
                                //JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                                if (isJSONValid(response)) {
                                    parseBhadraData(response, url);
                                }


                            }
                        } catch (Exception e) {

                        }

                        // pd.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /* if (activity != null) {
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                }*/


                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());

                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
                //pd.dismiss();

            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                //return super.getBodyContentType();
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(activity));//"-1489918760"
                headers.put("language", String.valueOf(LANGUAGE_CODE));
                headers.put("date", "1-" + (selectedMonth + 1) + "-" + selectedYear);
                headers.put("lid", city_Id);
                headers.put("isapi", "1");
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

    private void parseBhadraData(String saveData, String url) {
        try {
            JSONObject jsonObject = new JSONObject(saveData);
            JSONArray jsonArray = jsonObject.optJSONArray("festivalapidata");
            if (isJSONValid(jsonArray.toString())) {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonArray.toString()));
                reader.setLenient(true);
                FestDataDetail[] festDataDetailArr = gson.fromJson(reader, FestDataDetail[].class);
                List<FestDataDetail> festDataDetails = Arrays.asList(festDataDetailArr);
                if (bhadraDataList == null) {
                    bhadraDataList = new ArrayList<>();
                } else {
                    bhadraDataList.clear();
                }

                bhadraDataList.addAll(festDataDetails);
                FestDataDetail festDataDetail;
                for (int i = 0; i < bhadraDataList.size(); i++) {
                    festDataDetail = bhadraDataList.get(i);
                    if (checkPanchakDate(festDataDetail.getFestStartDate(), festDataDetail.getFestEndDate())) {
                        currentBhadra = i;
                        break;
                    }
                }
                setBhadraData();

            }


        } catch (Exception e) {
            e.printStackTrace();
            queue.getCache().remove(url);
        }
    }

    private void checkPanchakCachedData(int selectedYear, int selectedMonth, String cityId, boolean isShowProgressbar) {
        String url = CGlobalVariables.panchakApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&month=" + (selectedMonth + 1) + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(activity).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        panchakPB.setVisibility(View.GONE);
        if (entry != null) {
            try {
                String saveData = new String(entry.data, "UTF-8");
                parsePanchakData(saveData, url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (!CUtils.isConnectedWithInternet(activity)) {
                if (activity != null) {
                    Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

            } else {
                downloadPanchakData(selectedYear, selectedMonth, cityId, isShowProgressbar);
            }
        }
    }

    /**
     * @param beanPlace
     */
    private void setLagnaData(BeanPlace beanPlace, Date date) {
        parsingDataAfterResponse(setCalculations(beanPlace, date));
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
        String result =  LagnaTableCalculation.getStartEndMuhuratJson(calculation.getLagnaTable(), "");
        return result;
    }

    /**
     * @param response
     */
    private void parsingDataAfterResponse(String response) {
        try {
            Gson gson = new Gson();
            beanLagna = gson.fromJson(response, BeanLagnaTable.class);
            if (beanLagna != null) {
                Calendar calendar = Calendar.getInstance();
                long currentTime = calendar.getTimeInMillis();
                calendar.setTimeInMillis(currentTime);
                Date date = calendar.getTime();
                int mHour = date.getHours();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                long startTimee = 0;
                long endTimee = 0;
                // current lagna
                for (int iterator = 0; iterator < beanLagna.getFestivalapidata().size(); iterator++) {

                    String lagnaRashiName = beanLagna.getFestivalapidata().get(iterator).getLagnaName();
                    String lagnaStartTime = beanLagna.getFestivalapidata().get(iterator).getLagnaStart();
                    String lagnaEndTime = beanLagna.getFestivalapidata().get(iterator).getLagnaEnd();

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
                   /* // if hour is greater than 24 then subtract 24 from hour to get current hour.
                    if (Integer.parseInt(startTime[0]) < 24 &&
                            Integer.parseInt(endTime[0]) > 24 &&
                            mHour < 11) {
                        startTimee = getCurrentDate(year, month, day - 1, Integer.parseInt(startTime[0]) >= 24 ? Integer.parseInt(startTime[0]) - 24 : Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
                        endTimee = getCurrentDate(year, month, day, Integer.parseInt(endTime[0]) >= 24 ? Integer.parseInt(endTime[0]) - 24 : Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
                    }
                    // if start time is less then 24 hour and current time is over 24
                    else if (Integer.parseInt(startTime[0]) < 24 &&
                            Integer.parseInt(endTime[0]) > 24 &&
                            mHour < 11) {
                        startTimee = getCurrentDate(year, month, day, Integer.parseInt(startTime[0]) >= 24 ? Integer.parseInt(startTime[0]) - 24 : Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
                        endTimee = getCurrentDate(year, month, day + 1, Integer.parseInt(endTime[0]) >= 24 ? Integer.parseInt(endTime[0]) - 24 : Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
                    }
                    // if start time is less then 24 hour and current time is under 24
                    else {
                        startTimee = getCurrentDate(year, month, day, Integer.parseInt(startTime[0]) >= 24 ? Integer.parseInt(startTime[0]) - 24 : Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1]), Integer.parseInt(startTime[2]));
                        endTimee = getCurrentDate(year, month, day, Integer.parseInt(endTime[0]) >= 24 ? Integer.parseInt(endTime[0]) - 24 : Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]), Integer.parseInt(endTime[2]));
                    }*/


                    if (currentTime > cStartTime.getTimeInMillis() && currentTime < cEndTime.getTimeInMillis()) {
                        lagnaNameTV.setText(lagnaRashiName);
                        startTimeToShare = CUtils.getTimeInFormate(lagnaStartTime).replace("+", "");
                        endTimeToShare = CUtils.getTimeInFormate(lagnaEndTime).replace("+", "");
                        lagnaStartnEndTimeTV.setText(startTimeToShare + " " + getContext().getResources().getString(R.string.str_to) + " " + endTimeToShare);
                        setLagnaNatureAndColor(iterator, lagnaNatureCurrentTV);
                        lagnaNatureCurrentTV.setText(" (" + beanLagna.getFestivalapidata().get(iterator).getLagnaNature() + ")");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * \
     *
     * @param iterator
     * @param tvView
     */
    private void setLagnaNatureAndColor(int iterator, TextView tvView) {
        if (beanLagna.getFestivalapidata().get(iterator).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_MOVABLE)) {
            tvView.setTextColor(getContext().getResources().getColor(R.color.color_text_movable));
        } else if (beanLagna.getFestivalapidata().get(iterator).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_FIXED)) {
            tvView.setTextColor(getContext().getResources().getColor(R.color.color_text_fixed));
        } else if (beanLagna.getFestivalapidata().get(iterator).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_DUAL)) {
            tvView.setTextColor(getContext().getResources().getColor(R.color.color_text_dual));
        }
    }

    private void downloadPanchakData(final int selectedYear, final int selectedMonth, final String cityId, boolean isShowProgressbar) {

        final String url = CGlobalVariables.panchakApiUrl + LANGUAGE_CODE + "&cityid=" + cityId + "&month=" + (selectedMonth + 1) + "&year=" + selectedYear;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null && !response.isEmpty()) {
                               /* Gson gson = new Gson();
                                JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                                //Log.e("Element" + element.toString());
*/
                                if (isJSONValid(response)) {
                                    parsePanchakData(response, url);

                                }

                            }
                        } catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*if (activity != null) {
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                }*/

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                //return super.getBodyContentType();
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(activity));//"-1489918760"
                headers.put("language", String.valueOf(LANGUAGE_CODE));
                headers.put("date", "1-" + (selectedMonth + 1) + "-" + selectedYear);

                headers.put("lid", city_Id);
                headers.put("isapi", "1");
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

    private void parsePanchakData(String saveData, String url) {
        try {
            JSONObject jsonObject = new JSONObject(saveData);
            JSONArray jsonArray = jsonObject.optJSONArray("festivalapidata");
            if (isJSONValid(jsonArray.toString())) {
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonArray.toString()));
                reader.setLenient(true);
                FestDataDetail[] festDataDetailArr = gson.fromJson(reader, FestDataDetail[].class);
                List<FestDataDetail> festDataDetails = Arrays.asList(festDataDetailArr);
                if (panchakDataList == null) {
                    panchakDataList = new ArrayList<>();
                } else {
                    panchakDataList.clear();
                }
                panchakDataList.addAll(festDataDetails);
                FestDataDetail festDataDetail;
                for (int i = 0; i < panchakDataList.size(); i++) {
                    festDataDetail = panchakDataList.get(i);
                    if (checkPanchakDate(festDataDetail.getFestStartDate(), festDataDetail.getFestEndDate())) {
                        currentPanchak = i;
                        break;
                    }
                }
                setPanchakData();
            }


        } catch (Exception e) {
            e.printStackTrace();
            queue.getCache().remove(url);
        }
    }


    private boolean checkPanchakDate(String startDateStr, String endDateStr) {
        boolean boolVal = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Calendar calendar = Calendar.getInstance();
            if (beanDateTime != null) {
                calendar = beanDateTime.getCalender();
            }
            Date d1 = getZeroTimeDate(sdf.parse(startDateStr));
            Date d2 = getZeroTimeDate(sdf.parse(endDateStr));
            Date d3 = getZeroTimeDate(calendar.getTime());
            if (d1.compareTo(d3) == 0 || d2.compareTo(d3) == 0) {
                boolVal = true;
            } else if (d1.compareTo(d3) < 0 && d2.compareTo(d3) > 0) {
                boolVal = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return boolVal;
    }

    String getBhadraDateTimeStr(String dateStr, String hour, String minute) {
        String dateTimeStr = null;
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM dd");
            Date date = originalFormat.parse(dateStr);
            //Date date = originalFormat.parse("9/5/2019");

            dateTimeStr = targetFormat.format(date);  // 20120821
            String timeStr = CUtils.convertTimeToAmPm(hour + ":" + minute);
            dateTimeStr = dateTimeStr + ", " + timeStr;
        } catch (Exception e) {

        }
        return dateTimeStr;
    }

    private String getTithiName() {

        return model.getTithiValue().split(",")[0];
    }

    private String getTithiInt() {
        int tithiInt = (int) (model.getTithiInt()[0]) % 15;
        if (tithiInt == 0) {
            tithiInt = 15;
        }
        return String.valueOf(tithiInt).trim();
    }

    private String getTithiST() {
        String[] stArr = preDayModel.getTithiTime().split(",");
        String startTime = stArr[0];
        if (stArr.length > 1) {
            startTime = stArr[1].replace("\n", "");
        }
        startTime = CUtils.convertTimeToAmPm(startTime);
        if (startTime.contains("+")) {
            startTime = startTime.replace("+", " ");
        } else {
            startTime = getResources().getString(R.string.yesterday_label) + " " + startTime;
        }
        return getResources().getString(R.string.start) + ": " + startTime;
    }

    private String getTithiET() {
        String[] etArr = model.getTithiTime().split(",");
        String endTime = etArr[0];


        return getResources().getString(R.string.end) + ": " + CUtils.convertTimeToAmPm(endTime).replace("+", getResources().getString(R.string.tomorrow_label) + " ");
    }

    private String getDate(Date date, String format) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat(format);
        /*you can also use DateFormat reference instead of SimpleDateFormat
         * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
         */
        try {
            dateString = sdfr.format(date);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateString;
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
    public static long getCurrentDate(int year, int month, int day, int hour, int minute, int second) {
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

    private Date getDateFromString(String dateStr, String dateFormat) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            date = format.parse(dateStr);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param selectedYear
     * @param cityId
     * @param isShowProgressbar
     */
    public void downloadFestivalData(int selectedYear, String cityId, boolean isShowProgressbar) {
        checkCachedData(selectedYear, cityId, isShowProgressbar);
    }

    /**
     * @param selectedYear
     * @param cityId
     * @param isShowProgressbar
     */
    private void downloadDataDetails(final int selectedYear, final String cityId, boolean isShowProgressbar) {

        final String url = CGlobalVariables.DAILY_FEST_URL + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null && !response.isEmpty()) {
                              /*  Gson gson = new Gson();
                                JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
*/
                                if (isJSONValid(response)) {
                                    parseGsonData(response, url);

                                }


                            }
                        } catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());

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
                headers.put("key", CUtils.getApplicationSignatureHashCode(getActivity()));
                headers.put("isapi", "1");
                headers.put("language", String.valueOf(LANGUAGE_CODE));
                headers.put("date", getDate(beanDateTime.getCalender().getTime(), "dd-MM-yyyy"));
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
        stringRequest.setShouldCache(true);

        queue.add(stringRequest);
    }

    /**
     * @param selectedYear
     * @param cityId
     * @param isShowProgressbar
     */
    private void checkCachedData(int selectedYear, String cityId, boolean isShowProgressbar) {
        String url = CGlobalVariables.DAILY_FEST_URL + LANGUAGE_CODE + "&cityid=" + cityId + "&year=" + selectedYear;
        Cache cache = VolleySingleton.getInstance(activity).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String saveData = new String(entry.data, "UTF-8");
                parseGsonData(saveData, url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (!CUtils.isConnectedWithInternet(activity)) {
                if (activity != null) {
                    Toast.makeText(activity, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

            } else {
                downloadDataDetails(selectedYear, cityId, isShowProgressbar);
            }
        }
    }


    /**
     * @param saveData
     * @param url
     */
    private void parseGsonData(String saveData, String url) {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(saveData));
            reader.setLenient(true);
            NewFestivalBean newFestivalBean = gson.fromJson(reader, NewFestivalBean.class);

            if (newFestivalBean.getFestivals().size() > 0) {
                if (newFestivalBean.getFestivals().get(0).getFestivalName1() != null && newFestivalBean.getFestivals().get(0).getFestivalName1().trim() != "") {
                    tithiFestTV.setVisibility(View.VISIBLE);
                    tithiFestTV.setText(newFestivalBean.getFestivals().get(0).getFestivalName1().trim());
                } else {
                    tithiFestTV.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            queue.getCache().remove(url);
        }
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZoneString));
        //Date currentDate = digitalClockTV.getTime();
        //java.text.DateFormat df = new SimpleDateFormat("HH:mm");
        String currentTime = (String) DateFormat.format("hh:mm", calendar);
        //String currentTime = df.format(currentDate);
        String response = getResources().getString(R.string.current_time);
        String[] timeArray = currentTime.split(":");
        response = response.replace("#hour", removeZero(timeArray[0]));
        response = response.replace("#minute", removeZero(timeArray[1]));
        return response;
    }

    private String getCurrentDate(Calendar calendar, int day) {
        //Calendar calendar = Calendar.getInstance();
        String[] monthName = getResources().getStringArray(R.array.MonthName);
        if (day != 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        int date = calendar.get(Calendar.DATE);
        String month = monthName[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);
        String currentDate = date + " " + month + ", " + year;

        return currentDate;
    }

    private String getPunchangString() {
        String result;
        result = getResources().getString(R.string.panchang_str);
        result = result.replace("#currendate", getCurrentDate(beanDateTime.getCalender(), 0));
        result = result.replace("#vaar", model.getVaara());
        result = result.replace("#samvat", model.getVikramSamvat());
        result = result.replace("#paksh", model.getPakshaName());
        result = result.replaceFirst("#tithi", model.getTithiValue());
        result = result.replaceFirst("#month", model.getMonthPurnimanta());
        result = result.replaceFirst("#tithiend", CUtils.removeSecond(model.getTithiTime()));
        result = result.replaceFirst("#tithi", model.getTithiValue());
        result = result.replaceFirst("#nakshtra", model.getNakshatraValue());
        result = result.replaceFirst("#nakshtraend", CUtils.removeSecond(model.getNakshatraTime()));
        result = result.replaceFirst("#nakshtra", model.getNakshatraValue());
        String[] karan = model.getKaranaValue().split(",");
        String[] karanTime = model.getKaranaTime().split(",");
        result = result.replaceFirst("#karan", karan[0]);
        result = result.replaceFirst("#karanend", CUtils.removeSecond(karanTime[0]));
        if (karan.length > 1) {
            result = result.replaceFirst("#karan", karan[1]);
            result = result.replaceFirst("#karanend", CUtils.removeSecond(karanTime[1]));
        } else {
            result = result.replaceFirst(getResources().getString(R.string.karan_till), "");
        }
        result = result.replaceFirst("#yog", model.getYogaValue());
        result = result.replaceFirst("#yogend", CUtils.removeSecond(model.getYogaTime()));
        result = result.replaceFirst("#yog", model.getYogaValue());
        result = result.replace("\n", "");
        return result;
    }


    private void loadData(BeanPlace beanPlace) {
        if (loadPanchangData != null && loadPanchangData.getStatus() == AsyncTask.Status.RUNNING) {
            loadPanchangData.cancel(true);
        }

        loadPanchangData = new LoadPanchangData(beanPlace);
        loadPanchangData.execute();
    }

    class LoadPanchangData extends AsyncTask<Void, Void, Void> {
        BeanPlace beanPlace;

        public LoadPanchangData(BeanPlace beanPlace) {
            this.beanPlace = beanPlace;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setVisibalityOfView(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Date date = beanDateTime.getCalender().getTime();
            getPanchangData(date, language, beanPlace);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (activity != null) {
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                setData(beanPlace);
                  /*  }
                }, 60000);*/

            }
        }
    }

    private void setData(BeanPlace beanPlace) {
        getHoraData(beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay(), beanPlace);
        getChogdiaData(beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay(), beanPlace);


        getDoGhatiData(beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay(), beanPlace);
        setTithiAndDayData();
        setRahukalData();
        setTithiData();
        setPakshaData();
        setKaranData();
        setNakshtraData();
        setYogaData();
        setDayData();
        setShakaSamvatData();
        setDayDurationData();
        setKaliSamvatData();
        setVikramSamvatData();
        setMonthAmanthaData();
        setMonthPurnimantData();
        setAbhijitData();
        setDustMuhuratData();
        setKantakaData();
        setYamghantaData();
        setKulikaData();
        setKalavelaData();
        setYamgandaData();
        setGulikakalData();
        setDishaShooData();
        setLagnaData();
        setTarabalData();
        setChandrabalData();
        setSunRiseData(beanPlace);
        setSunSetData(beanPlace);
        setMoonRiseData(beanPlace);
        setMoonSetData(beanPlace);
        setRituData();
        setMoonSignData();
        setPlaceAndTime();
        setShareAndTodayHoroscopeData();
        setVisibalityOfView(View.GONE);
    }

    private void setVisibalityOfView(int visibality) {
        horaPB.setVisibility(visibality);
        chogdiyaPB.setVisibility(visibality);
        rahukalPB.setVisibility(visibality);
        doGhatiPB.setVisibility(visibality);

        tithiPB.setVisibility(visibality);
        yogaPB.setVisibility(visibality);
        karanPB.setVisibility(visibality);
        nakshtraPB.setVisibility(visibality);
        pakshaPB.setVisibility(visibality);
        dayPB.setVisibility(visibality);

        shakaSamvatPB.setVisibility(visibality);
        dayDurationPB.setVisibility(visibality);
        kaliSamvatPB.setVisibility(visibality);
        vikramSamvatPB.setVisibility(visibality);
        monthAmanthaPB.setVisibility(visibality);
        monthPurnimantPB.setVisibility(visibality);

        abhijitPB.setVisibility(visibality);
        dustMuhuratPB.setVisibility(visibality);
        kantakaPB.setVisibility(visibality);
        yamaghantaPB.setVisibility(visibality);
        kulikaPB.setVisibility(visibality);
        kalavelaPB.setVisibility(visibality);
        yamagandaPB.setVisibility(visibality);
        gulikaKalPB.setVisibility(visibality);
       /* panchakPB.setVisibility(visibality);
        bhadraPB.setVisibility(visibality);*/

        dishaShoolPB.setVisibility(visibality);
        //lagnaPB.setVisibility(visibality);
        chandrabalPB.setVisibility(visibality);
        tarabalPB.setVisibility(visibality);
        nextChandrabalPB.setVisibility(visibality);
        nextTarabalPB.setVisibility(visibality);

        sunRisePB.setVisibility(visibality);
        sunSetPB.setVisibility(visibality);
        moonRisePB.setVisibility(visibality);
        moonSetPB.setVisibility(visibality);
        rituPB.setVisibility(visibality);
        moonSignPB.setVisibility(visibality);
    }


    private BeanPlace getBeanObj(BeanPlace beanPlace) {

        if (beanPlace != null) {

            if (beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Nepal")) {

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

    public void shareContentData(String packageName, BeanPlace beanPlace) {
        whatsAppData = "";
  /*    String data;
  String data1;
  String data2;*/
        String enter1 = "\n";
        String enter2 = "\n\n";
        String enter3 = "\n\n\n";

        String headingImage = "🚩";
        String titleImage = "☀ ";
        String contentImage = "🔅 ";
        String heading1 = "🚩श्री गणेशाय नम:🚩 " + enter1;
        String heading2 = "📜 " + getResources().getString(R.string.daily_panchang) + " 📜" + enter2;

        String heading = heading1 + heading2;

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

        setDataToString(titleImage + getResources().getString(R.string.panchang_for_today), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.tithi), model.getTithiValue(), CUtils.convertTimeToAmPm(model.getTithiTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.nakshatra), model.getNakshatraValue(), CUtils.convertTimeToAmPm(model.getNakshatraTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.karana), model.getKaranaValue(), CUtils.convertTimeToAmPm(model.getKaranaTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.paksha), model.getPakshaName(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.yoga), model.getYogaValue(), CUtils.convertTimeToAmPm(model.getYogaTime()), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.day), model.getVaara(), "", enter2, 0);

        setDataToString(titleImage + getResources().getString(R.string.sun_and_moon_calculation), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.sun_rises), CUtils.convertTimeToAmPm(model.getSunRise()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.moon_rises), CUtils.convertTimeToAmPm(model.getMoonRise()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.moon_sign), CUtils.convertTimeToAmPm(model.getMoonSign()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.sun_set), CUtils.convertTimeToAmPm(model.getSunSet()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.moon_set), CUtils.convertTimeToAmPm(model.getMoonSet()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.ritu), model.getRitu(), "", enter2, 0);

        setDataToString(titleImage + getResources().getString(R.string.hindu_month_and_year), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.shaka_samvat), model.getShakaSamvatYear(), model.getShakaSamvatName(), enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.kali_samvat), model.getKaliSamvat(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.day_duration), CUtils.convertTimeToAmPm(model.getDayDuration()), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.vikram_samvat), model.getVikramSamvat(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.month_amanta), model.getMonthAmanta(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.month_purnimanta), model.getMonthPurnimanta(), "", enter2, 0);

        setDataToString(titleImage + getResources().getString(R.string.auspicious_and_inauspicious), "", "", enter1, 0);
        setDataToString(titleImage + getResources().getString(R.string.auspicious_timings), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.abhijit), CUtils.convertTimeToAmPm(model.getAbhijitFrom()), CUtils.convertTimeToAmPm(model.getAbhijitTo()), enter1, 1);

        setDataToString(titleImage + getResources().getString(R.string.inauspicious_timings), "", "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.dushta_muhurtas), CUtils.convertTimeToAmPm(model.getDushtaMuhurtasFrom()), CUtils.convertTimeToAmPm(model.getDushtaMuhurtasTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.kantaka), CUtils.convertTimeToAmPm(model.getKantaka_MrityuFrom()), CUtils.convertTimeToAmPm(model.getKantaka_MrityuTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.yamaghanta), CUtils.convertTimeToAmPm(model.getYamaghantaFrom()), CUtils.convertTimeToAmPm(model.getYamaghantaTo()), enter1, 1);
        setDataToString(titleImage + getResources().getString(R.string.current_lagna), lagnaNameTV.getText().toString() + giveMeSpace(2) + lagnaNatureCurrentTV.getText().toString(), startTimeToShare + " - " + endTimeToShare, enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.kulika), CUtils.convertTimeToAmPm(model.getKulikaFrom()), CUtils.convertTimeToAmPm(model.getKulikaTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.kalavela), CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamFrom()), CUtils.convertTimeToAmPm(model.getKalavela_ArdhayaamTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.yamaganda), CUtils.convertTimeToAmPm(model.getYamagandaVelaFrom()), CUtils.convertTimeToAmPm(model.getYamagandaVelaTo()), enter1, 1);
        setDataToString(contentImage + getResources().getString(R.string.gulika_kaal), CUtils.convertTimeToAmPm(model.getGulikaKaalVelaFrom()), CUtils.convertTimeToAmPm(model.getGulikaKaalVelaTo()), enter1, 1);

        setDataToString(contentImage + getResources().getString(R.string.disha_shoola), model.getDishaShoola(), "", enter1, 0);
        setDataToString(contentImage + getResources().getString(R.string.rahu_kaal), model.getRahuKaalVelaFrom(), model.getRahuKaalVelaTo(), enter1, 1);
        setDataToString(titleImage + getResources().getString(R.string.chandrabalam_and_tarabalam), "", "", enter1, 0);
        setDataToString(titleImage + getResources().getString(R.string.tara_bala), "\n" + contentImage + model.getTaraBala(), "", enter1, 0);
        setDataToString(titleImage + getResources().getString(R.string.chandra_bala), "\n" + contentImage + model.getChandraBala(), "", enter2, 0);

        CUtils.shareData(activity, whatsAppData, packageName, activity.getResources().getString(R.string.share_panchang));

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
            //Toast.makeText(this, ""+space, Toast.LENGTH_SHORT).show();
            whatsAppData = whatsAppData + giveMeSpace(space);
            String timeStr = arrValue[0];
            if (arrValue.length > 1) {
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

    private void setHeadingColor() {
        horaHeadingTV.setTextColor(getResources().getColor(R.color.color_hora));
        chogdiyaHeadingTV.setTextColor(getResources().getColor(R.color.color_choghadia));
        rahukalHeadingTV.setTextColor(getResources().getColor(R.color.color_red));
        doGhatiHeadingTV.setTextColor(getResources().getColor(R.color.color_do_ghati));
        tithiHeadingTV.setTextColor(getResources().getColor(R.color.color_tithi));
        tithiGoodFor.setTextColor(getResources().getColor(R.color.color_tithi));
        pakshaHeadingTV.setTextColor(getResources().getColor(R.color.color_paksh));
        karanHeadingTV.setTextColor(getResources().getColor(R.color.color_karan));
        karanGoodFor.setTextColor(getResources().getColor(R.color.color_karan));
        nakshtraHeadingTV.setTextColor(getResources().getColor(R.color.color_nakshtra));
        nakshtraGoodFor.setTextColor(getResources().getColor(R.color.color_nakshtra));
        yogaHeadingTV.setTextColor(getResources().getColor(R.color.color_red));
        yogaGoodFor.setTextColor(getResources().getColor(R.color.color_red));
        dayHeadingTV.setTextColor(getResources().getColor(R.color.color_day));
        shakaSamvatHeadingTV.setTextColor(getResources().getColor(R.color.color_shaka_samvat));
        dayDurationHeadingTV.setTextColor(getResources().getColor(R.color.color_day_duration));
        kaliSamvatHeadingTV.setTextColor(getResources().getColor(R.color.color_kali_samvat));
        vikramSamvatHeadingTV.setTextColor(getResources().getColor(R.color.color_vikram_samvat));
        monthAmanthaHeadingTV.setTextColor(getResources().getColor(R.color.color_month_amantha));
        monthPurnimantHeadingTV.setTextColor(getResources().getColor(R.color.color_month_purnimantha));
        abhijitHeadingTV.setTextColor(getResources().getColor(R.color.color_abhijit));
        dustMuhuratHeadingTV.setTextColor(getResources().getColor(R.color.color_dust_muhurat));
        kantakaHeadingTV.setTextColor(getResources().getColor(R.color.color_kantka));
        yamaghantaHeadingTV.setTextColor(getResources().getColor(R.color.color_yamaghanta));
        kulikaHeadingTV.setTextColor(getResources().getColor(R.color.color_kulika));
        kalavelaHeadingTV.setTextColor(getResources().getColor(R.color.color_kalavela));
        yamagandaHeadingTV.setTextColor(getResources().getColor(R.color.color_yamganda));
        gulikaKalHeadingTV.setTextColor(getResources().getColor(R.color.color_gulika_kaal));
        panchakHeadingTV.setTextColor(getResources().getColor(R.color.color_panchak));
        bhadraHeadingTV.setTextColor(getResources().getColor(R.color.color_bhadra));
        dishaShoolHeadingTV.setTextColor(getResources().getColor(R.color.color_disha_shool));
        lagnaHeadingTV.setTextColor(getResources().getColor(R.color.color_kantka));
        nextTarabalHeadingTV.setTextColor(getResources().getColor(R.color.color_tarabalam));
        chandrabalHeadingTV.setTextColor(getResources().getColor(R.color.color_chandrabal));
        sunRiseHeadingTV.setTextColor(getResources().getColor(R.color.color_sunrise));
        sunSetHeadingTV.setTextColor(getResources().getColor(R.color.color_sunset));
        moonRiseHeadingTV.setTextColor(getResources().getColor(R.color.color_moon_rise));
        moonSetHeadingTV.setTextColor(getResources().getColor(R.color.color_moon_set));
        rituHeadingTV.setTextColor(getResources().getColor(R.color.color_ritu));
        moonSignHeadingTV.setTextColor(getResources().getColor(R.color.color_moon_sign));
    }

    private void setCardHeadings() {
        horaHeadingTV.setText(getResources().getString(R.string.hora_name));
        chogdiyaHeadingTV.setText(getResources().getString(R.string.chogadia_name));
        rahukalHeadingTV.setText(getResources().getString(R.string.rahu_kaal));
        doGhatiHeadingTV.setText(getResources().getString(R.string.doghati_name));
        tithiHeadingTV.setText(getResources().getString(R.string.tithi));
        pakshaHeadingTV.setText(getResources().getString(R.string.paksha));
        karanHeadingTV.setText(getResources().getString(R.string.karana));
        nakshtraHeadingTV.setText(getResources().getString(R.string.nakshatra));
        yogaHeadingTV.setText(getResources().getString(R.string.yoga));
        dayHeadingTV.setText(getResources().getString(R.string.day));
        shakaSamvatHeadingTV.setText(getResources().getString(R.string.shaka_samvat));
        dayDurationHeadingTV.setText(getResources().getString(R.string.day_duration));
        kaliSamvatHeadingTV.setText(getResources().getString(R.string.kali_samvat));
        vikramSamvatHeadingTV.setText(getResources().getString(R.string.vikram_samvat));
        monthAmanthaHeadingTV.setText(getResources().getString(R.string.month_amanta));
        monthPurnimantHeadingTV.setText(getResources().getString(R.string.month_purnimanta));
        abhijitHeadingTV.setText(getResources().getString(R.string.abhijit));
        dustMuhuratHeadingTV.setText(getResources().getString(R.string.dushta_muhurtas));
        kantakaHeadingTV.setText(getResources().getString(R.string.kantaka));
        yamaghantaHeadingTV.setText(getResources().getString(R.string.yamaghanta));
        kulikaHeadingTV.setText(getResources().getString(R.string.kulika));
        kalavelaHeadingTV.setText(getResources().getString(R.string.kalavela));
        yamagandaHeadingTV.setText(getResources().getString(R.string.yamaganda));
        gulikaKalHeadingTV.setText(getResources().getString(R.string.gulika_kaal));
        panchakHeadingTV.setText(getResources().getString(R.string.panchak));
        bhadraHeadingTV.setText(getResources().getString(R.string.bhadra));
        dishaShoolHeadingTV.setText(getResources().getString(R.string.disha_shoola));
        lagnaHeadingTV.setText(getResources().getString(R.string.current_lagna));
        sunRiseHeadingTV.setText(getResources().getString(R.string.sun_rises));
        sunSetHeadingTV.setText(getResources().getString(R.string.sun_set));
        moonRiseHeadingTV.setText(getResources().getString(R.string.moon_rises));
        moonSetHeadingTV.setText(getResources().getString(R.string.moon_set));
        rituHeadingTV.setText(getResources().getString(R.string.ritu));
        moonSignHeadingTV.setText(getResources().getString(R.string.moon_sign));
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
