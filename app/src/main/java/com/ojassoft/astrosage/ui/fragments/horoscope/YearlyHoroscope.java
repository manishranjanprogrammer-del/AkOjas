package com.ojassoft.astrosage.ui.fragments.horoscope;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.isTextToSpeechAvailable;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.TTS_CHAR_LIMIT;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.mTextToSpeech;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.setmTtsCallbackListener;
import static com.ojassoft.astrosage.utils.CGlobalVariables.TOTAL_RASHI_COUNT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.customcontrols.FlowTextView;
import com.ojassoft.astrosage.ui.customcontrols.Rotate3dAnimation;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class YearlyHoroscope extends Fragment implements TtsCallbackListener, View.OnClickListener, SendDataBackToComponent {
    private boolean currentyear = false;
    private boolean checkforcondition = false;
    private int rashiType;
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String URL = CGlobalVariables.gPlusUrl;
    private static final long FLIP_ANIMATION_DURATION = 200;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    // public Typeface typeface;
    //Typeface regularTypeface;
    //Typeface mediumTypeface;
    String serverError = "Server Error: ";
    String rashiIntroduction;
    String strRashiName;
    ImageView _imageRashiWithoutName;
    ScrollView wholeScreen;
    private RelativeLayout parentRL;
    FlowTextView rashiIntro;
    private RelativeLayout callNowBtn, chatNowBtn;
    private TextView titleChatCall, callNowBtnTxt, chatNowBtnTxt;
    //TextView  _tvPredictionHeading;
    TextView _tvShowMoonRashiSign;
    ToggleButton toggleButtonGeneral, toggleButtonFamily, toggleButtonHealth, toggleButtonLove, toggleButtonCareer, toggleButtonMoney, toggleButtonEducation, toggleButtonRemedies;
    TextView textViewFamilyContent, textViewHealthContent, textViewLoveContent, textViewCareerContent, textViewMoneyContent, textViewEducationContent, textViewRemediesContent;
    Button whatsappBtn;
    Drawable expand, collapsed;
    int YEARLY_HOROSCOPE = 0;

    final int RASHI_INTRO_KEY = 0;
    final int FAMILY = 1;
    final int HEALTH = 2;
    final int LOVE = 3;
    final int CAREER = 4;
    final int MONEY = 5;
    final int EDUCATION = 6;
    final int REMEDIES = 7;
    private LinearLayout layoutforgonevisibilityforTamil;
    LinearLayout llCustomAdv = null;
    Activity activity;
    View view = null;

    boolean isToastShouldShow = false;
    //private GetYearlyRashifalASync getYearlyRashifalASync;
    boolean chenextyeardata;
    SharedPreferences nextyearlyHoroscope;
    private int SIMPLE_CALL = 2;

    private ProgressBar pd;
    boolean checkfirst = false;
    private Button btnBottomForYear;

    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    AdData topAdData;
    // private long MILLIs_IN_WEEK = 6 * 24 * 60 * 60 * 1000;
    private ArrayList<AdData> adList;
    public Spinner spinnerScreenList;
    int screenType;
    private LinearLayout playStopImgll, shareImgll, copyImgll;
    private ImageView playStopImg;
    String _whatsAppTitle = "";
    boolean hitAgain = true;

    public static YearlyHoroscope newInstance(int rashiType) {
        YearlyHoroscope yearlyHoroscopeFragment = new YearlyHoroscope();
        Bundle args = new Bundle();
        args.putInt("rashiType", rashiType);
        yearlyHoroscopeFragment.setArguments(args);
        return yearlyHoroscopeFragment;
    }

    private void initValues() {
        CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);
        //mediumTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, "Medium");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (getYearlyRashifalASync != null && getYearlyRashifalASync.getStatus() == AsyncTask.Status.RUNNING) {
            getYearlyRashifalASync.cancel(true);
        }*/
        activity = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && playStopImg != null) {
            playStopImg.setImageResource(R.mipmap.ic_play);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(activity);
        currentyear = false;
        // Toast.makeText(activity, "onCreateView", Toast.LENGTH_SHORT).show();

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        initValues();

        expand = getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black);
        collapsed = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black);

        //rashiType = getArguments().getInt("rashiType", 0);
        rashiType = ((DetailedHoroscope) activity).rashiType;
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.lay_yearly_horoscope, container,false);
        if (view == null) {
            view = inflater
                    .inflate(R.layout.lay_yearly_horoscope, container, false);
        }/* else {
            ((ViewGroup) view.getParent()).removeView(view);
        }*/
        spinnerScreenList = (Spinner) view.findViewById(R.id.sign_spinner);
        parentRL = view.findViewById(R.id.parentRL);
        callNowBtn = view.findViewById(R.id.call_now_btn);
        chatNowBtn = view.findViewById(R.id.chat_now_btn);
        callNowBtnTxt = view.findViewById(R.id.call_now_btn_txt);
        chatNowBtnTxt = view.findViewById(R.id.chat_now_btn_txt);
        titleChatCall = view.findViewById(R.id.titleChatCall);
        pd = (ProgressBar) view.findViewById(R.id.progressBarYearly);
        //Add advertisment in footer 10-Dec-2015
        if (llCustomAdv == null) {
            llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
            //llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, (((DetailedHoroscope) activity).regularTypeface), "SHOYE"));
            CardView customAdvertisementCardview = view.findViewById(R.id.custom_advertisement_cardview);
            View adView = CUtils.getCustomAdvertismentView(activity, false, (((DetailedHoroscope) activity).regularTypeface), "SHOYE");
            llCustomAdv.addView(adView);
            if(CUtils.isShowCustomAds(activity)){
                customAdvertisementCardview.setVisibility(VISIBLE);
            } else {
                customAdvertisementCardview.setVisibility(GONE);
            }
        }
        btnBottomForYear = (Button) view.findViewById(R.id.btnBottom);

        btnBottomForYear.setTypeface((((DetailedHoroscope) activity).mediumTypeface));

        btnBottomForYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearlyPrediction();
            }
        });
        /*mPlusClient = new PlusClient.Builder(activity,
                YearlyHoroscope.this, YearlyHoroscope.this).clearScopes()
                .build();*/
        wholeScreen = (ScrollView) view.findViewById(R.id.scrollViewWholeScreen);
        //Here this string contains rashi name, which is selected by user on previous screen.
        layoutforgonevisibilityforTamil = (LinearLayout) view.findViewById(R.id.layout_for_gone_visivility_oftamil);
        strRashiName = activity.getResources().getStringArray(R.array.rashiName_list)[rashiType];
        //here we set icon of particular sign(rashi).
        _imageRashiWithoutName = (ImageView) view.findViewById(R.id.imageViewRasi);
        _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);
        rashiIntro = (FlowTextView) view.findViewById(R.id.textViewPrediction);
        //rashiIntro.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);
        LinearLayout whatsAppBtn = (LinearLayout) view.findViewById(R.id.whatsappll);

        // FOR WHATS APP
        _whatsAppTitle = getResources().getString(R.string.text_share_whatsapp_heading_yearly);
        _whatsAppTitle = _whatsAppTitle.replace("#", getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiType]);
        _whatsAppTitle = _whatsAppTitle.replace("$", getMonthNameForWhatsApp());

        whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof DetailedHoroscope) {
                    ((DetailedHoroscope) activity).shareMessageWithWhatsApp(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_YEARLY);
                }
            }
        });

        //ADDED BY SHELENDRA ON 13.07.2015 FOR TAMIL
        if (LANGUAGE_CODE == CGlobalVariables.TAMIL || LANGUAGE_CODE == CGlobalVariables.BANGALI || LANGUAGE_CODE == CGlobalVariables.MARATHI || LANGUAGE_CODE == CGlobalVariables.TELUGU || LANGUAGE_CODE == CGlobalVariables.KANNADA
                || LANGUAGE_CODE == CGlobalVariables.GUJARATI || LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
            layoutforgonevisibilityforTamil.setVisibility(View.VISIBLE);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.otherDetails);
            linearLayout.setVisibility(View.VISIBLE);
        }

        // rashiIntro.setText(rashiIntroduction);
        rashiIntro.setTextSize(getResources().getDimension(R.dimen.body_text_size));
        rashiIntro.setTypeface((((DetailedHoroscope) activity).regularTypeface));
        String _rashiHeading = getResources().getString(R.string.Horoscop_for_2012);
        //here we set moonrashi sign
        _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
        String heading = CUtils.returnFormattedString(getResources().getString(R.string.MoonSign), LANGUAGE_CODE);
        heading = heading.replace("#", strRashiName);
        //_tvShowMoonRashiSign.setText(heading);
        _tvShowMoonRashiSign.setTypeface(((DetailedHoroscope) activity).mediumTypeface, Typeface.BOLD);

        //init other content text
        toggleButtonGeneral = (ToggleButton) view.findViewById(R.id.toggleButtonGeneral);
        toggleButtonFamily = (ToggleButton) view.findViewById(R.id.toggleButtonFamily);
        toggleButtonHealth = (ToggleButton) view.findViewById(R.id.toggleButtonHealth);
        toggleButtonLove = (ToggleButton) view.findViewById(R.id.toggleButtonLove);
        toggleButtonCareer = (ToggleButton) view.findViewById(R.id.toggleButtonCareer);
        toggleButtonMoney = (ToggleButton) view.findViewById(R.id.toggleButtonMoney);
        // toggleButtonEducation = (ToggleButton) view.findViewById(R.id.toggleButtonEducation);
        toggleButtonRemedies = (ToggleButton) view.findViewById(R.id.toggleButtonRemedies);

        textViewFamilyContent = (TextView) view.findViewById(R.id.textViewFamilyContent);
        textViewHealthContent = (TextView) view.findViewById(R.id.textViewHealthContent);
        textViewLoveContent = (TextView) view.findViewById(R.id.textViewLoveContent);
        textViewCareerContent = (TextView) view.findViewById(R.id.textViewCareerContent);
        textViewMoneyContent = (TextView) view.findViewById(R.id.textViewMoneyContent);
        //   textViewEducationContent = (TextView) view.findViewById(R.id.textViewEducationContent);
        textViewRemediesContent = (TextView) view.findViewById(R.id.textViewRemediesContent);

        titleChatCall.setTypeface((((BaseInputActivity) activity).mediumTypeface), Typeface.BOLD);
        callNowBtnTxt.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        chatNowBtnTxt.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        handleCallChatClick();

        texttospeechFunctionality();

        registerTextViewsForContextMenu();

        setTypeFaceOverContent();
        setToggleButtonsListeners();

        //setAllPredictionContentFromFile(rashiType, LANGUAGE_CODE);


        //start mahtab
        setYealyData();
        if (!getCurrentYearPreferenceCheckAccordingLang()) {
         /* getYearlyRashifalASync = new GetYearlyRashifalASync(SIMPLE_CALL);
            getYearlyRashifalASync.execute();*/
            getYearlyRashifalASync();
        }


        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_23_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_23_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S23");

                CustomAddModel modal = topAdData.getImageObj().get(0);


                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });

        //end
        return view;
    }

    private boolean getCurrentYearPreferenceCheckAccordingLang() {
        boolean isDataExist = false;
        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE);
        } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_HINDI);
        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TAMIL);
        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_KANNAD);
        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TELEGU);
        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_BANGLA);
        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_GUJRATI);
        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MALAYALAM);
        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MARATHI);
        } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_ODIA);
        } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
            isDataExist = CUtils.getCurrentYearPreferenceCheck(activity,
                    CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_ASSAMMESE);
        }
        return isDataExist;
    }

    /**
     * text to speech Functionality
     */
    private void texttospeechFunctionality() {

        setmTtsCallbackListener(YearlyHoroscope.this);

        playStopImgll = (LinearLayout) view.findViewById(R.id.imagePlayll);
        shareImgll = (LinearLayout) view.findViewById(R.id.imagesharell);
        copyImgll = (LinearLayout) view.findViewById(R.id.imagecopyll);
        playStopImg = (ImageView) view.findViewById(R.id.imagePlay);
        playStopImg.setImageResource(R.mipmap.ic_play);
        playStopImgll.setOnClickListener(this);
        copyImgll.setOnClickListener(this);
        shareImgll.setOnClickListener(this);
        disablePlayButton(playStopImg);
        if (isTextToSpeechAvailable) {
            enablePlayButton(playStopImg);
        }
    }

    private String getStringToSpeak(String stringToSpeak) {
        if (rashiIntro.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonGeneral.getText().toString() + " \n " + rashiIntro.getText().toString();
        } else if (textViewFamilyContent.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonFamily.getText().toString() + " \n " + textViewFamilyContent.getText().toString();
        } else if (textViewHealthContent.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonHealth.getText().toString() + " \n " + textViewHealthContent.getText().toString();
        } else if (textViewLoveContent.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonLove.getText().toString() + " \n " + textViewLoveContent.getText().toString();
        } else if (textViewCareerContent.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonCareer.getText().toString() + " \n " + textViewCareerContent.getText().toString();
        } else if (textViewMoneyContent.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonMoney.getText().toString() + " \n " + textViewMoneyContent.getText().toString();
        } else if (textViewRemediesContent.getVisibility() == View.VISIBLE) {
            stringToSpeak = stringToSpeak + " \n " + toggleButtonRemedies.getText().toString() + " \n " + textViewRemediesContent.getText().toString();
        }
        return stringToSpeak;
    }

    private void setYealyData() {
        chenextyeardata = CUtils.getNextYearPridictionPreferences(
                activity, CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN);

        if (chenextyeardata) {

            int year = Integer.valueOf(CUtils.getCurrentYear());

            String after_until = "30/09/" + year;
            String before_until = "01/01/" + (year + 1);
            // comparision b/w dates
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date afterDate = null;
            Date beforeDate = null;
            try {
                afterDate = sdf.parse(after_until);
                beforeDate = sdf.parse(before_until);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (new Date().after(afterDate) && new Date().before(beforeDate)) {
                setYearToPass(Integer.parseInt(CUtils.getCurrentYear()) + 1);
                checkfirst = true;
                btnBottomForYear.setVisibility(View.VISIBLE);
                btnBottomForYear.setText(getResources().getString(
                        R.string.horoscopeText)
                        + " "
                        + String.valueOf(Integer.valueOf(CUtils
                        .getCurrentYear())));
                checkforcondition = true;
                if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearHindi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearTamil,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionTamil(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearKannad,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionKannad(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearTelugu,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionTelegu(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearBangla,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionBangla(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearGujrati,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionGujrati(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }

                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionMalayalam(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearMarathi,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionMarathi(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearAssammese,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionMarathi(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYearOdia,
                            Context.MODE_PRIVATE);
                    String[] yearlyContent = CUtils.getYearlyRasiPredictionMarathi(nextyearlyHoroscope, rashiType);
                    if (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                            TextUtils.isEmpty(yearlyContent[3].trim()) ||
                            TextUtils.isEmpty(yearlyContent[4].trim()) ||
                            TextUtils.isEmpty(yearlyContent[5].trim()) ||
                            TextUtils.isEmpty(yearlyContent[6].trim()) ||
                            TextUtils.isEmpty(yearlyContent[7].trim())) {
                        CUtils.saveNextYearPridictionPreferences(getActivity(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                        setYealyData();
                        return;
                    }
                    rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                    _tvShowMoonRashiSign.setText(yearlyContent[0]);
                    textViewFamilyContent.setText(yearlyContent[2]);
                    textViewHealthContent.setText(yearlyContent[3]);
                    textViewLoveContent.setText(yearlyContent[4]);
                    textViewCareerContent.setText(yearlyContent[5]);
                    textViewMoneyContent.setText(yearlyContent[6]);
                    textViewRemediesContent.setText(yearlyContent[7]);
                } else {
                    nextyearlyHoroscope = activity.getSharedPreferences(
                            CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                            Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                }

            } else {
                setYearToPass(Integer.parseInt(CUtils.getCurrentYear()));
                btnBottomForYear.setText(getResources().getString(
                        R.string.horoscopeText)
                        + " "
                        + String.valueOf(Integer.valueOf(CUtils
                        .getCurrentYear()) + 1));

                //CUtils.saveNextYearPredictionDataToCurrentYearIfAvailable(LANGUAGE_CODE,activity);

              /*  getYearlyRashifalASync = new GetYearlyRashifalASync(SIMPLE_CALL);
                getYearlyRashifalASync.execute();*/
                getYearlyRashifalASync();

            }
        } else {
            btnBottomForYear.setVisibility(View.GONE);

            //CUtils.saveNextYearPredictionDataToCurrentYearIfAvailable(LANGUAGE_CODE,activity);

          /*  getYearlyRashifalASync = new GetYearlyRashifalASync(SIMPLE_CALL);
            getYearlyRashifalASync.execute();*/
            getYearlyRashifalASync();

        }

    }

    private void registerTextViewsForContextMenu() {
        registerForContextMenu(rashiIntro);
        registerForContextMenu(textViewFamilyContent);
        registerForContextMenu(textViewHealthContent);
        registerForContextMenu(textViewLoveContent);
        registerForContextMenu(textViewCareerContent);
        registerForContextMenu(textViewMoneyContent);
        // registerForContextMenu(textViewEducationContent);
        registerForContextMenu(textViewRemediesContent);
    }

    // private String clipBoardText = "";

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
       /* super.onCreateContextMenu(menu, v, menuInfo);
        clipBoardText = ((FlowTextView) v).getText().toString();
        menu.setHeaderTitle(getString(R.string.select_item));
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menucopy, menu);*/
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (getUserVisibleHint() == false) {
            return false;
        }

        /*AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.copyMenu:
                CUtils.copyTextToClipBoard(CUtils.getShareData(activity, _tvShowMoonRashiSign.getText().toString(), rashiIntro.getText().toString()), activity);
                return true;
            case R.id.shareMenu:
                CUtils.sharePrediction(activity,"", CUtils.getShareData(activity,_tvShowMoonRashiSign.getText().toString(),rashiIntro.getText().toString()));
                return true;

            default:
                return super.onContextItemSelected(item);
        }*/
        return super.onContextItemSelected(item);
    }


    private void setToggleButtonsListeners() {
        toggleButtonGeneral.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rashiIntro.setVisibility(View.VISIBLE);
                   /* textViewFamilyContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewFamilyContent);
                            //							wholeScreen.scrollTo(0, textViewFamilyContent.getBottom());
                        }
                    });*/
                    toggleButtonGeneral.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    rashiIntro.setVisibility(View.GONE);
                    /*textViewFamilyContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewFamilyContent);
                        }
                    });*/
                    toggleButtonGeneral.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }
            }
        });

        toggleButtonFamily.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewFamilyContent.setVisibility(View.VISIBLE);
                   /* textViewFamilyContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewFamilyContent);
                            //							wholeScreen.scrollTo(0, textViewFamilyContent.getBottom());
                        }
                    });*/
                    toggleButtonFamily.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    textViewFamilyContent.setVisibility(View.GONE);
                    /*textViewFamilyContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewFamilyContent);
                        }
                    });*/
                    toggleButtonFamily.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }
            }
        });
        toggleButtonHealth.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewHealthContent.setVisibility(View.VISIBLE);
                   /* textViewHealthContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewHealthContent);
                        }
                    });*/

                    toggleButtonHealth.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    textViewHealthContent.setVisibility(View.GONE);
                    /*textViewHealthContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewHealthContent);
                        }
                    });*/
                    toggleButtonHealth.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }

            }
        });
        toggleButtonLove.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewLoveContent.setVisibility(View.VISIBLE);
                    /*textViewLoveContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewLoveContent);
                        }
                    });*/
                    toggleButtonLove.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    textViewLoveContent.setVisibility(View.GONE);
                  /*  textViewLoveContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewLoveContent);
                        }
                    });*/
                    toggleButtonLove.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }

            }
        });
        toggleButtonCareer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewCareerContent.setVisibility(View.VISIBLE);
                    /*textViewCareerContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewCareerContent);
                        }
                    });*/
                    toggleButtonCareer.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    textViewCareerContent.setVisibility(View.GONE);
                    /*textViewCareerContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewCareerContent);
                        }
                    });*/
                    toggleButtonCareer.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }
            }
        });
        toggleButtonMoney.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewMoneyContent.setVisibility(View.VISIBLE);
                    /*textViewMoneyContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewMoneyContent);
                        }
                    });*/
                    toggleButtonMoney.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    textViewMoneyContent.setVisibility(View.GONE);
                  /*  textViewMoneyContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewMoneyContent);
                        }
                    });*/
                    toggleButtonMoney.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }
            }
        });
  /*      toggleButtonEducation.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewEducationContent.setVisibility(View.VISIBLE);
                   *//* textViewEducationContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewEducationContent);
                        }
                    });*//*
                    toggleButtonEducation.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);
                } else {
                    textViewEducationContent.setVisibility(View.GONE);
                    *//*textViewEducationContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewEducationContent);
                        }
                    });*//*
                    toggleButtonEducation.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }
            }
        });*/
        toggleButtonRemedies.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewRemediesContent.setVisibility(View.VISIBLE);
                    /*textViewRemediesContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextShow(textViewRemediesContent);
                            wholeScreen.fullScroll(View.FOCUS_DOWN);
                        }
                    });*/
                    toggleButtonRemedies.setCompoundDrawablesWithIntrinsicBounds(null, null, collapsed, null);

                } else {
                    textViewRemediesContent.setVisibility(View.GONE);
                    /*textViewRemediesContent.post(new Runnable() {

                        @Override
                        public void run() {
                            applyFlipAnimaionOnTextHide(textViewRemediesContent);
                        }
                    });*/
                    toggleButtonRemedies.setCompoundDrawablesWithIntrinsicBounds(null, null, expand, null);
                }
            }
        });

    }

    private void applyFlipAnimaionOnTextShow(TextView view) {
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        Rotate3dAnimation rotation;
        rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 0.0f, false);
        rotation.setDuration(FLIP_ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(rotation);
    }

    private void applyFlipAnimaionOnTextHide(final TextView view) {
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        Rotate3dAnimation rotation;
        rotation = new Rotate3dAnimation(360, 270, centerX, centerY, 0.0f, false);
        rotation.setDuration(FLIP_ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(rotation);
        rotation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });
    }

    private void setAllPredictionContentFromFile(int rashiType,
                                                 int LANGUAGE_CODE) {

        //here we show prediction according to category selected by the user on previous screen.
        String predictionFamily = CUtils.getPredictinDetail(activity, rashiType, FAMILY, LANGUAGE_CODE);
        String predictionHealth = CUtils.getPredictinDetail(activity, rashiType, HEALTH, LANGUAGE_CODE);
        String predictionLove = CUtils.getPredictinDetail(activity, rashiType, LOVE, LANGUAGE_CODE);
        String predictionCareer = CUtils.getPredictinDetail(activity, rashiType, CAREER, LANGUAGE_CODE);
        String predictionMoney = CUtils.getPredictinDetail(activity, rashiType, MONEY, LANGUAGE_CODE);
        String predictionEducation = CUtils.getPredictinDetail(activity, rashiType, EDUCATION, LANGUAGE_CODE);
        String predictionRemedies = CUtils.getPredictinDetail(activity, rashiType, REMEDIES, LANGUAGE_CODE);

        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            predictionFamily = predictionFamily.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionFamily = predictionFamily.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);

            predictionHealth = predictionHealth.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionHealth = predictionHealth.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);

            predictionLove = predictionLove.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionLove = predictionLove.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);

            predictionCareer = predictionCareer.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionCareer = predictionCareer.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);

            predictionMoney = predictionMoney.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionMoney = predictionMoney.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);

            predictionEducation = predictionEducation.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionEducation = predictionEducation.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);

            predictionRemedies = predictionRemedies.replace(CGlobalVariables.CHAR_PIPE, CGlobalVariables.CHAR_AMPERSENT);
            predictionRemedies = predictionRemedies.replace(CGlobalVariables.CHAR_NINE, CGlobalVariables.CHAR_LESS_THEN);
        }

        textViewFamilyContent.setText(predictionFamily);
        textViewHealthContent.setText(predictionHealth);
        textViewLoveContent.setText(predictionLove);
        textViewCareerContent.setText(predictionCareer);
        textViewMoneyContent.setText(predictionMoney);
        // textViewEducationContent.setText(predictionEducation);
        textViewRemediesContent.setText(predictionRemedies);
    }

    private void setTypeFaceOverContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && LANGUAGE_CODE == 1) {
            toggleButtonGeneral.setText(getResources().getString(R.string.general));
            toggleButtonGeneral.setTextOn(getResources().getString(R.string.general));
            toggleButtonGeneral.setTextOff(getResources().getString(R.string.general));
            toggleButtonFamily.setText(getResources().getString(R.string.family_hindi));
            toggleButtonFamily.setTextOn(getResources().getString(R.string.family_hindi));
            toggleButtonFamily.setTextOff(getResources().getString(R.string.family_hindi));
            toggleButtonHealth.setText(getResources().getString(R.string.health_hindi));
            toggleButtonHealth.setTextOn(getResources().getString(R.string.health_hindi));
            toggleButtonHealth.setTextOff(getResources().getString(R.string.health_hindi));
            toggleButtonLove.setText(getResources().getString(R.string.love_hindi));
            toggleButtonLove.setTextOn(getResources().getString(R.string.love_hindi));
            toggleButtonLove.setTextOff(getResources().getString(R.string.love_hindi));
            toggleButtonCareer.setText(getResources().getString(R.string.career_hindi));
            toggleButtonCareer.setTextOn(getResources().getString(R.string.career_hindi));
            toggleButtonCareer.setTextOff(getResources().getString(R.string.career_hindi));
            toggleButtonMoney.setText(getResources().getString(R.string.money_hindi));
            toggleButtonMoney.setTextOn(getResources().getString(R.string.money_hindi));
            toggleButtonMoney.setTextOff(getResources().getString(R.string.money_hindi));
            // toggleButtonEducation.setText(getResources().getString(R.string.education_hindi));
            // toggleButtonEducation.setTextOn(getResources().getString(R.string.education_hindi));
            //  toggleButtonEducation.setTextOff(getResources().getString(R.string.education_hindi));
            toggleButtonRemedies.setText(getResources().getString(R.string.remedies_hindi));
            toggleButtonRemedies.setTextOn(getResources().getString(R.string.remedies_hindi));
            toggleButtonRemedies.setTextOff(getResources().getString(R.string.remedies_hindi));
        } else {
          /*  toggleButtonGeneral.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonFamily.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonHealth.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonLove.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonCareer.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonMoney.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonEducation.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
            toggleButtonRemedies.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);*/
        }


        textViewFamilyContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        textViewHealthContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        textViewLoveContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        textViewCareerContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        textViewMoneyContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        // textViewEducationContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        textViewRemediesContent.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
    }

    @Override
    public void onStart() {
        super.onStart();
        // mPlusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pd.isShown())
            pd.setVisibility(View.GONE);
        // mPlusClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
        // Refresh the state of the +1 button each time we receive focus.
        /*mPlusOneStandardButtonWithAnnotation.initialize(mPlusClient, URL,
                PLUS_ONE_REQUEST_CODE);*/

    }

    public void updateData() {
        if (activity != null) {
            rashiType = ((DetailedHoroscope) activity).rashiType;
            _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);
            setSpinner();
            setYealyData();
            scrollToTop();
        }

    }
    private void scrollToTop(){
        wholeScreen.post(new Runnable() {
            public void run() {
                wholeScreen.setFocusableInTouchMode(true);
                wholeScreen.fullScroll(View.FOCUS_UP);
                wholeScreen.smoothScrollTo(0, parentRL.getTop());
            }
        });
    }
    /*@Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

    @Override
    public void onConnected(Bundle arg0) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }*/

    public void showYearlyPrediction() {

        /*
         * int predictionType = Integer.parseInt(v.getTag().toString()); switch
         * (predictionType) { case CGlobalVariables.NEXTYEAR:
         */

        // TODO Auto-generated method stub
        int year = Integer.valueOf(CUtils.getCurrentYear());

        /*
         * String valid_until = "30/09/" + year; // comparision b/w dates
         * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); Date
         * strDate=null; try { strDate = sdf.parse(valid_until); } catch
         * (ParseException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); } if (new Date().after(strDate)) {
         * Toast.makeText(getApplicationContext(), "after start date",
         * 2000).show(); }else{ Toast.makeText(getApplicationContext(),
         * "before start date", 2000).show();
         *
         * }
         */

        if (checkforcondition) {


            if (currentyear) {
                setYearToPass(year + 1);
                btnBottomForYear.setText(getResources().getString(R.string.horoscopeText) + "  " + String.valueOf(year));
                if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearHindi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearTamil,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearKannad,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearTelugu,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearBangla,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[0]);


                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearGujrati,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMarathi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearAssammese,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearOdia,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                } else {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                }
                currentyear = false;
            } else {
                setYearToPass(year);
                checkfirst = false;

                btnBottomForYear.setText(getResources().getString(R.string.horoscopeText) + "  " + String.valueOf(year + 1));
                if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefName,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameHindi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {

                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameTamil,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA ) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameKannad,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameTelegu,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameBangla,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameGujrati,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameMalayalam,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameMarathi,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameAssam,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameOdia,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                } else {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefName,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                }


                currentyear = true;
            }

        } else {


            if (!currentyear) {
                setYearToPass(year + 1);
                btnBottomForYear.setText(getResources().getString(R.string.horoscopeText) + "  " + String.valueOf(year));
                if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearHindi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearTamil,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE ==CGlobalVariables.KANNADA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearKannad,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearTelugu,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearBangla,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearGujrati,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMarathi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);


                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearAssammese,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);


                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearOdia,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);


                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                } else {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                }
                currentyear = true;
            } else {
                setYearToPass(year);
                checkfirst = false;
                btnBottomForYear.setText(getResources().getString(R.string.horoscopeText) + "  " + String.valueOf(year + 1));

                if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefName,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameHindi,
                                    Context.MODE_PRIVATE);

                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {

                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameTamil,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTamil(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameKannad,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[0]);


                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionKannad(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameTelegu,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionTelegu(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameBangla,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionBangla(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE ==CGlobalVariables.GUJARATI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameGujrati,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionGujrati(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMalayalam(
                            nextyearlyHoroscope, rashiType)[7]);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameMarathi,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameAssam,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefNameOdia,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[0]);

                    textViewFamilyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPredictionMarathi(
                            nextyearlyHoroscope, rashiType)[7]);
                } else {
                    nextyearlyHoroscope = activity
                            .getSharedPreferences(
                                    CGlobalVariables.YearlyHoroscopePrefName,
                                    Context.MODE_PRIVATE);
                    rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[1]));
                    _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[0]);
                    textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[2]);
                    textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[3]);
                    textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[4]);
                    textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[5]);
                    textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[6]);
                    textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                            nextyearlyHoroscope, rashiType)[7]);
                }
                currentyear = false;
            }
        }

    }

    // mahtab start
    String[] rashifal;

    @Override
    public void resetSpeakBtn(View view) {
        playStopImg.setImageResource(R.mipmap.ic_play);
        if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }
    }

    @Override
    public void disablePlayButton(View view) {
        playStopImgll.setClickable(false);
        playStopImgll.setEnabled(false);
        playStopImgll.setOnClickListener(null);
        playStopImgll.setAlpha(0.5f);
    }

    @Override
    public void enablePlayButton(View view) {
        playStopImgll.setClickable(true);
        playStopImgll.setEnabled(true);
        playStopImgll.setOnClickListener(this);
        playStopImgll.setAlpha(1f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagePlayll:
                imagePlayPauseClick();
                break;
            case R.id.imagecopyll:
                imageCopyClick();
                break;
            case R.id.imagesharell:
                imageShareClick();
                break;
        }
    }

    private void imageShareClick() {
        // GA
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_YEARLY,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_YEARLY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        // END

        String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim();
        stringToSpeak = getStringToSpeak(stringToSpeak);
        if (stringToSpeak != null) {
            CUtils.shareHoroscope(getActivity(), stringToSpeak);
        }
    }

    private void imageCopyClick() {
        // GA
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_COPY_TEXT_YEARLY,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_COPY_TEXT_YEARLY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        // END

        String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim();
        stringToSpeak = getStringToSpeak(stringToSpeak);
        if (stringToSpeak != null) {
            CUtils.copyTextToClipBoard(stringToSpeak, getActivity());
        }
    }

    private void imagePlayPauseClick() {
        // GA
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_YEARLY,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_YEARLY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        // END

        if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {

            String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim();

            stringToSpeak = getStringToSpeak(stringToSpeak);

            if (stringToSpeak != null) {
                playStopImg.setImageResource(R.mipmap.ic_stop);
                Log.i("TTS", "button clicked: " + stringToSpeak);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, System.currentTimeMillis() + "");

                if (stringToSpeak.length() > TTS_CHAR_LIMIT) {
                    stringToSpeak = stringToSpeak.substring(0, TTS_CHAR_LIMIT);
                }
                if (mTextToSpeech != null) {
                    int speechStatus = mTextToSpeech.speak(stringToSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("TTS", "Error in converting Text to Speech!");
                    }
                }
            }
        } else {
            resetSpeakBtn(playStopImg);
        }
    }

    private void getYearlyRashifalASync() {
       /* getYearlyRashifalASync = new GetYearlyRashifalASync(SIMPLE_CALL);
        getYearlyRashifalASync.execute();*/

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String YEARLYHoroscopeKey = dateFormat.format(date.getTime());
        SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
        if (!YEARLYHoroscopeKey.equalsIgnoreCase(yearlyHoroscope.getString(
                CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, ""))) {
            if (CUtils.isConnectedWithInternet(activity)) {
                pd.setVisibility(View.VISIBLE);
                playStopImgll.setOnClickListener(null);
                copyImgll.setOnClickListener(null);
                shareImgll.setOnClickListener(null);
                CUtils.getYearlyPredictinDetail(LANGUAGE_CODE, activity, YearlyHoroscope.this, rashiType, YEARLY_HOROSCOPE);
            } else {
                _tvShowMoonRashiSign.setText(" ");
                rashiIntro.setText(" ");
                rashiIntro.setContentDescription(" ");
                Toast.makeText(activity,
                        "Please Check Internet Connection !",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            showYearlyRasifal(CUtils.getYearlyPredictionData(yearlyHoroscope,
                    rashiType, LANGUAGE_CODE));
        }


    }


   /* private class GetYearlyRashifalASync extends AsyncTask<String, Long, Void> {

        private boolean isSuccess = true;
        private String msg = "";
        private int callToFunction = 0;


        GetYearlyRashifalASync(int callToFunction) {
            this.callToFunction = callToFunction;

        }

        @Override
        protected void onPreExecute() {
            pd.setVisibility(View.VISIBLE);
            playStopImgll.setOnClickListener(null);
            copyImgll.setOnClickListener(null);
            shareImgll.setOnClickListener(null);
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                if (callToFunction == SIMPLE_CALL) {
                    rashifal = CUtils.getYearlyPredictinDetail(LANGUAGE_CODE, activity, YearlyHoroscope.this, rashiType, YEARLY_HOROSCOPE);
                } else {
                    // rashifal = CUtils.getYearlyPredictinDetailForRefress(
                    //      context, rashiIndex);

                }

                if (rashifal != null && !rashifal[1].equals("")) {
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
            } catch (final RuntimeException e) {
                // errorStr = e.getMessage();
                isSuccess = false;
                msg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                if (activity != null) {
                    try {
                        if (pd.isShown())
                            pd.setVisibility(View.GONE);
                        playStopImgll.setOnClickListener(YearlyHoroscope.this);
                        copyImgll.setOnClickListener(YearlyHoroscope.this);
                        shareImgll.setOnClickListener(YearlyHoroscope.this);
                    } catch (Exception e) {
                        // nothing
                    }

                    if (isSuccess) {

                        if (checkfirst) {
                            if (LANGUAGE_CODE == 0) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[0]);
                                textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[2]);
                                textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[3]);
                                textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[4]);
                                textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[5]);
                                textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[6]);
                                textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[7]);
                            } else if (LANGUAGE_CODE == 1) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearHindi,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[0]);
                                textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[2]);
                                textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[3]);
                                textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[4]);
                                textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[5]);
                                textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[6]);
                                textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[7]);
                            } else if (LANGUAGE_CODE == 2) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearTamil,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTamil(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTamil(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else if (LANGUAGE_CODE == 4) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearKannad,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionKannad(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionKannad(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else if (LANGUAGE_CODE == 5) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearTelugu,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionTelegu(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionTelegu(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else if (LANGUAGE_CODE == 6) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearBangla,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionBangla(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionBangla(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else if (LANGUAGE_CODE == 7) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearGujrati,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionGujrati(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionGujrati(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else if (LANGUAGE_CODE == 8) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMalayalam(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMalayalam(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else if (LANGUAGE_CODE == 9) {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYearMarathi,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPredictionMarathi(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPredictionMarathi(
                                        nextyearlyHoroscope, rashiType)[0]);
                            } else {
                                nextyearlyHoroscope = activity
                                        .getSharedPreferences(
                                                CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                                Context.MODE_PRIVATE);

                                rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[1]));
                                _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[0]);
                                textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[2]);
                                textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[3]);
                                textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[4]);
                                textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[5]);
                                textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[6]);
                                textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                                        nextyearlyHoroscope, rashiType)[7]);
                            }


                        } else {
                            if (LANGUAGE_CODE == 2) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TAMIL, true);
                            } else if (LANGUAGE_CODE == 4) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_KANNAD, true);

                            } else if (LANGUAGE_CODE == 5) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                _tvShowMoonRashiSign.setContentDescription(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TELEGU, true);

                            } else if (LANGUAGE_CODE == 6) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_BANGLA, true);

                            } else if (LANGUAGE_CODE == 7) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_GUJRATI, true);

                            } else if (LANGUAGE_CODE == 8) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MALAYALAM, true);

                            } else if (LANGUAGE_CODE == 9) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MARATHI, true);

                            } else if (LANGUAGE_CODE == 1) {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                                textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                                textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                                textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                                textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                                textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));

                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_HINDI, true);
                            } else {
                                _tvShowMoonRashiSign.setText(rashifal[0]);
                                rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                                rashiIntro.setText(Html.fromHtml(rashifal[1]));
                                rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                                textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                                textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                                textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                                textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                                textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                                textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                                CUtils.saveCurrentYearPreferenceCheck(activity,
                                        CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE, true);

                            }

                            if (rashifal[1].equals("NO_INTERNET")) {
                                _tvShowMoonRashiSign.setText(" ");
                                rashiIntro.setText(" ");
                                rashiIntro.setContentDescription(" ");
                                Toast.makeText(activity,
                                        "Please Check Internet Connection !",
                                        Toast.LENGTH_LONG).show();
                            }


                        }


                    } else {
                        _tvShowMoonRashiSign.setText(" ");
                        rashiIntro.setText(activity.getResources().getString(R.string.internal_error));
                        rashiIntro.setContentDescription(activity.getResources().getString(R.string.internal_error));
                       *//* Toast.makeText(activity, msg,
                                Toast.LENGTH_LONG).show();*//*
                    }
                }
            } catch (Exception ex) {
                //
            }
        }

    }*/


//end mahtab

    private void showToast() {
        Toast.makeText(activity, serverError, Toast.LENGTH_SHORT).show();
        serverError = "";
    }

    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "23");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTopAdd(AdData topData) {
        getData();
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

    private void setSpinner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)//addded by neeraj to display correct spinner in higher virsion 29/4/16
        {
//            spinnerScreenList.setPopupBackgroundResource(R.drawable.spinner_dropdown);
        }
        final String[] pageTitles = activity.getResources().getStringArray(
                R.array.rashiName_list);

        //((InputPanchangActivity) activity).spinnerScreenList.setVisibility(View.VISIBLE);
        ArrayAdapter<String> topDropDownListAadapter = new ArrayAdapter<String>(
                activity, R.layout.tool_bar_spinner_list_item,
                pageTitles) {

            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                final TextView textView = (TextView) view.findViewById(R.id.textview);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(18);
                textView.setText(pageTitles[position]);
                //textView.setTypeface(regularTypeface);


                return view;
            }


            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                view.setBackgroundColor(getResources().getColor(R.color.bg_card_view_color));
                TextView textView = (TextView) view.findViewById(R.id.textview);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(16);
                String title = pageTitles[position];

                textView.setText(title);
                //textView.setTypeface(regularTypeface);

                return view;
            }
        };
        spinnerScreenList.setAdapter(topDropDownListAadapter);
        int moonSign = 0;
        if (activity instanceof DetailedHoroscope) {
            screenType = ((DetailedHoroscope) activity).screenType;
            moonSign = rashiType;
        } else if (activity instanceof InputPanchangActivity) {
            moonSign = CUtils.getMoonSignIndex(activity);
        }
        spinnerScreenList.setSelected(false);  // must
        spinnerScreenList.setSelection(moonSign, false);
        spinnerScreenList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            int count = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                if (count >= 1 || screenType > 0) {
                    ((DetailedHoroscope) activity).screenType = 0;
                    rashiType = position;


                    if (activity instanceof DetailedHoroscope) {
                        ((DetailedHoroscope) activity).changeMoonSign(position);
                    }
                    updateData();
                }
                count++;
                //CUtils.saveIntData(activity, "moon_sign", position);
                //setCurrentView(position, false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
    }

    public String getWhatsAppTitle() {
        return _whatsAppTitle;
    }

    public String getDescription() {
        String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim();
        stringToSpeak = getStringToSpeak(stringToSpeak);
        return stringToSpeak;
    }

    private String getMonthNameForWhatsApp() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String formattedString = getResources().getStringArray(R.array.WhatsAppMonthName)[month] + " " + year;
        return formattedString;
    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        Log.i("", response);
        if (activity != null) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy",
                    Locale.ENGLISH);
            String YEARLYHoroscopeKey = dateFormat.format(date.getTime());
            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);

            if (!TextUtils.isEmpty(response)) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (Exception e) {

                }
                List<CMessage> listMessage = LibCUtils.parseYearlyXML(response);
                if (listMessage != null && listMessage.size() == TOTAL_RASHI_COUNT) {
                    if (LANGUAGE_CODE == CGlobalVariables.ENGLISH || LANGUAGE_CODE == CGlobalVariables.HINDI) {
                        CUtils.saveYearlyRasiPrediction(listMessage, yearlyHoroscope,
                                CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY,
                                YEARLYHoroscopeKey);
                    } else {
                        CUtils.saveYearlyRasiPredictionOtherLanguage(listMessage, yearlyHoroscope,
                                CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY,
                                YEARLYHoroscopeKey);
                    }
                    showYearlyRasifal(CUtils.getYearlyPredictionData(yearlyHoroscope,
                            rashiType, LANGUAGE_CODE));

                }
            }

        }

    }

    @Override
    public void doActionOnError(String response) {
        if (activity != null) {
            _tvShowMoonRashiSign.setText(" ");
            rashiIntro.setText(activity.getResources().getString(R.string.internal_error));
            rashiIntro.setContentDescription(activity.getResources().getString(R.string.internal_error));

        }
    }

    private void showYearlyRasifal(String[] rashifal) {
        try {
            if (activity != null) {
                try {
                    if (pd.isShown())
                        pd.setVisibility(View.GONE);
                    playStopImgll.setOnClickListener(YearlyHoroscope.this);
                    copyImgll.setOnClickListener(YearlyHoroscope.this);
                    shareImgll.setOnClickListener(YearlyHoroscope.this);
                } catch (Exception e) {
                    // nothing
                }

                if (checkfirst) {
                    if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                        Context.MODE_PRIVATE);

                        rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[1]));
                        _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[0]);
                        textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[2]);
                        textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[3]);
                        textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[4]);
                        textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[5]);
                        textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[6]);
                        textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearHindi,
                                        Context.MODE_PRIVATE);

                        rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[1]));
                        _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[0]);
                        textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[2]);
                        textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[3]);
                        textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[4]);
                        textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[5]);
                        textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[6]);
                        textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearTamil,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionTamil(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }
                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearKannad,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionKannad(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }

                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearTelugu,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionTelegu(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }
                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearBangla,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionBangla(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }
                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearGujrati,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionGujrati(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }
                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionMalayalam(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }

                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);
                    } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYearMarathi,
                                        Context.MODE_PRIVATE);
                        String[] yearlyContent = CUtils.getYearlyRasiPredictionMarathi(
                                nextyearlyHoroscope, rashiType);
                        if (hitAgain && (TextUtils.isEmpty(yearlyContent[2].trim()) ||
                                TextUtils.isEmpty(yearlyContent[3].trim()) ||
                                TextUtils.isEmpty(yearlyContent[4].trim()) ||
                                TextUtils.isEmpty(yearlyContent[5].trim()) ||
                                TextUtils.isEmpty(yearlyContent[6].trim()) ||
                                TextUtils.isEmpty(yearlyContent[7].trim()))) {
                            SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                            SharedPreferences.Editor editor = yearlyHoroscope.edit();
                            editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                            editor.commit();
                            getYearlyRashifalASync();
                            hitAgain = false;
                            return;
                        }

                        rashiIntro.setText(Html.fromHtml(yearlyContent[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(yearlyContent[1]));
                        _tvShowMoonRashiSign.setText(yearlyContent[0]);
                        textViewFamilyContent.setText(yearlyContent[2]);
                        textViewHealthContent.setText(yearlyContent[3]);
                        textViewLoveContent.setText(yearlyContent[4]);
                        textViewCareerContent.setText(yearlyContent[5]);
                        textViewMoneyContent.setText(yearlyContent[6]);
                        textViewRemediesContent.setText(yearlyContent[7]);

                    } else {
                        nextyearlyHoroscope = activity
                                .getSharedPreferences(
                                        CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                                        Context.MODE_PRIVATE);

                        rashiIntro.setText(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[1]));
                        _tvShowMoonRashiSign.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[0]);
                        textViewFamilyContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[2]);
                        textViewHealthContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[3]);
                        textViewLoveContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[4]);
                        textViewCareerContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[5]);
                        textViewMoneyContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[6]);
                        textViewRemediesContent.setText(CUtils.getYearlyRasiPrediction(
                                nextyearlyHoroscope, rashiType)[7]);
                    }


                } else {
                    if (hitAgain && (TextUtils.isEmpty(rashifal[2].trim()) ||
                            TextUtils.isEmpty(rashifal[3].trim()) ||
                            TextUtils.isEmpty(rashifal[4].trim()) ||
                            TextUtils.isEmpty(rashifal[5].trim()) ||
                            TextUtils.isEmpty(rashifal[6].trim()) ||
                            TextUtils.isEmpty(rashifal[7].trim()))) {
                        SharedPreferences yearlyHoroscope = CUtils.getYearlyPref(activity, LANGUAGE_CODE);
                        SharedPreferences.Editor editor = yearlyHoroscope.edit();
                        editor.putString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEY, "");
                        editor.commit();
                        getYearlyRashifalASync();
                        hitAgain = false;
                        return;
                    }
                    if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));

                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TAMIL, true);
                    } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));

                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_KANNAD, true);

                    } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        _tvShowMoonRashiSign.setContentDescription(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TELEGU, true);

                    } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));

                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_BANGLA, true);

                    } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_GUJRATI, true);

                    } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MALAYALAM, true);

                    } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MARATHI, true);

                    } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));

                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_HINDI, true);
                    } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        // 3. Set the UI elements
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        textViewFamilyContent.setText(rashifal[2]);
                        textViewHealthContent.setText(rashifal[3]);
                        textViewLoveContent.setText(rashifal[4]);
                        textViewCareerContent.setText(rashifal[5]);
                        textViewMoneyContent.setText(rashifal[6]);
                        textViewRemediesContent.setText(rashifal[7]);
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_ASSAMMESE, true);
                    } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                        // 1. Get the SharedPreference for Odia
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        // 3. Set the UI elements

                        // 3. Set the UI elements
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        textViewFamilyContent.setText(rashifal[2]);
                        textViewHealthContent.setText(rashifal[3]);
                        textViewLoveContent.setText(rashifal[4]);
                        textViewCareerContent.setText(rashifal[5]);
                        textViewMoneyContent.setText(rashifal[6]);
                        textViewRemediesContent.setText(rashifal[7]);
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE_ODIA, true);
                    }else {
                        _tvShowMoonRashiSign.setText(rashifal[0]);
                        rashifal[1] = rashifal[1].replace("AstroSage.com,", "");
                        rashiIntro.setText(Html.fromHtml(rashifal[1]));
                        rashiIntro.setContentDescription(Html.fromHtml(rashifal[1]));
                        textViewFamilyContent.setText(Html.fromHtml(rashifal[2]));
                        textViewHealthContent.setText(Html.fromHtml(rashifal[3]));
                        textViewLoveContent.setText(Html.fromHtml(rashifal[4]));
                        textViewCareerContent.setText(Html.fromHtml(rashifal[5]));
                        textViewMoneyContent.setText(Html.fromHtml(rashifal[6]));
                        textViewRemediesContent.setText(Html.fromHtml(rashifal[7]));
                        CUtils.saveCurrentYearPreferenceCheck(activity,
                                CGlobalVariables.YEARLY_HOROSCOPE_PREF_CHECK_VALUE, true);

                    }

                   /* if (rashifal[1].equals("NO_INTERNET")) {
                        _tvShowMoonRashiSign.setText(" ");
                        rashiIntro.setText(" ");
                        rashiIntro.setContentDescription(" ");
                        Toast.makeText(activity,
                                "Please Check Internet Connection !",
                                Toast.LENGTH_LONG).show();
                    }*/


                }


            } else {
                _tvShowMoonRashiSign.setText(" ");
                rashiIntro.setText(activity.getResources().getString(R.string.internal_error));
                rashiIntro.setContentDescription(activity.getResources().getString(R.string.internal_error));
                       /* Toast.makeText(activity, msg,
                                Toast.LENGTH_LONG).show();*/
            }

        } catch (Exception ex) {
            Log.i("", ex.getMessage());
        }
    }

    private void handleCallChatClick(){
        callNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_YEARLY_HOROSCOPE_CALL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.YEARLY_HOROSCOPE_CALL_PARTNER_ID);
                    com.ojassoft.astrosage.varta.utils.CUtils.openCallList( activity);
                } catch (Exception e) {
                    //
                }
            }
        });

        chatNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_YEARLY_HOROSCOPE_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.YEARLY_HOROSCOPE_CHAT_PARTNER_ID);
                    //com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CHAT, activity);
                    com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CALL, activity);//redirect to ai list
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    private void setYearToPass(int year) {
        if (activity instanceof DetailedHoroscope) {
            ((DetailedHoroscope) activity).yearToPass = year;
        }
    }

}
