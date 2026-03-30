package com.ojassoft.astrosage.ui.fragments.horoscope;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.isTextToSpeechAvailable;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.TTS_CHAR_LIMIT;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.mTextToSpeech;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.setmTtsCallbackListener;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;

public class MonthlyHoroscopeFragment extends Fragment implements TtsCallbackListener, View.OnClickListener, SendDataBackToComponent {
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    // public Typeface typeface;
    //public Typeface regularTypeface;
    //public Typeface mediumTypeface;

    boolean isToastShouldShow = false;
    String serverError = "Server Error: ";
    int horoscopeType;
    int rashiType;
    String strRashiName = "";
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String URL = CGlobalVariables.gPlusUrl;
    private TextView _tvShowMoonRashiSign;
    private FlowTextView rashiIntro;
    ImageView _imageRashiWithoutName;
    private RelativeLayout callNowBtn, chatNowBtn;
    private TextView titleChatCall, callNowBtnTxt, chatNowBtnTxt;
    private ProgressBar progressBar;
    private long MILLIs_IN_WEEK = 6 * 24 * 60 * 60 * 1000;

    String[] rashifal;
    //private GetTodayRashifalASync getTodayRashifalASync;
    String _whatsAppTitle = "";
    LinearLayout llCustomAdv = null;
    Activity activity;
    View view = null;

    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    AdData topAdData;
    // private long MILLIs_IN_WEEK = 6 * 24 * 60 * 60 * 1000;
    private ArrayList<AdData> adList;
    public Spinner spinnerScreenList;
    ScrollView scrollView;
    private RelativeLayout parentRL;
    private LinearLayout playStopImgll, shareImgll, copyImgll;
    private ImageView playStopImg;
    int MONTHLY_HOROSCOPE = 0;

    public static MonthlyHoroscopeFragment newInstance(int rashiType) {

        MonthlyHoroscopeFragment monthlyHoroscopeFragment = new MonthlyHoroscopeFragment();
        Bundle args = new Bundle();
        args.putInt("rashiType", rashiType);
        monthlyHoroscopeFragment.setArguments(args);

        return monthlyHoroscopeFragment;
    }

  /*  private void initValues() {
        //regularTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        //mediumTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (getTodayRashifalASync.getStatus() == Status.RUNNING)
            getTodayRashifalASync.cancel(true);*/
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        ((BaseInputActivity) getActivity()).LANGUAGE_CODE = LANGUAGE_CODE;
        //initValues();
        //horoscopeType = getArguments().getInt("horoscopeType", 0);
        // rashiType = getArguments().getInt("rashiType", 0);
        rashiType = ((DetailedHoroscope) activity).rashiType;

        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.lay_daily_weekly_monthly_horoscope, container,false);
        if (view == null) {
            view = inflater
                    .inflate(R.layout.lay_daily_weekly_monthly_horoscope, container, false);
        }/* else {
            ((ViewGroup) view.getParent()).removeView(view);
        }*/
        callNowBtn = view.findViewById(R.id.call_now_btn);
        chatNowBtn = view.findViewById(R.id.chat_now_btn);
        callNowBtnTxt = view.findViewById(R.id.call_now_btn_txt);
        chatNowBtnTxt = view.findViewById(R.id.chat_now_btn_txt);
        titleChatCall = view.findViewById(R.id.titleChatCall);
        spinnerScreenList = (Spinner) view.findViewById(R.id.sign_spinner);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        parentRL = view.findViewById(R.id.parentRL);
        //Add advertisment in footer 10-Dec-2015
        if (llCustomAdv == null) {
            llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
            //llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, (((DetailedHoroscope) activity).regularTypeface), "SHOMO"));
            CardView customAdvertisementCardview = view.findViewById(R.id.custom_advertisement_cardview);
            View adView = CUtils.getCustomAdvertismentView(activity, false, (((DetailedHoroscope) activity).regularTypeface), "SHOMO");
            llCustomAdv.addView(adView);
            if(CUtils.isShowCustomAds(activity)){
                customAdvertisementCardview.setVisibility(VISIBLE);
            } else {
                customAdvertisementCardview.setVisibility(GONE);
            }
        }

        /*mPlusClient = new PlusClient.Builder(getActivity(), MonthlyHoroscopeFragment.this, MonthlyHoroscopeFragment.this)
                .clearScopes()
                .build();*/
        LinearLayout whatsAppBtn = (LinearLayout) view.findViewById(R.id.whatsappll);
        whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof DetailedHoroscope) {
                    ((DetailedHoroscope) activity).shareMessageWithWhatsApp(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_MONTHLY);
                }
            }
        });
        strRashiName = activity.getResources().getStringArray(R.array.rashiName_list)[rashiType];
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

        //here we set moonrashi sign
        _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
        String heading = CUtils.returnFormattedString(LANGUAGE_CODE, activity.getResources().getString(R.string.moonSignMonthly));
        heading = heading.replace("#", strRashiName);
        heading = heading.replace("$", getMonthName());
        _tvShowMoonRashiSign.setText(heading);

        _tvShowMoonRashiSign.setTypeface((((DetailedHoroscope) activity).mediumTypeface), Typeface.BOLD);
        //here we set icon of particular sign(rashi).
        _imageRashiWithoutName = (ImageView) view.findViewById(R.id.imageViewRasi);
        _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);
        //here we show introduction of rashi.
        rashiIntro = (FlowTextView) view.findViewById(R.id.textViewPrediction);
        rashiIntro.setTypeface((((DetailedHoroscope) activity).robotRegularTypeface));
        rashiIntro.setTextSize(activity.getResources().getDimension(R.dimen.body_text_size));
        rashiIntro.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);

        titleChatCall.setTypeface((((BaseInputActivity) activity).mediumTypeface), Typeface.BOLD);
        callNowBtnTxt.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        chatNowBtnTxt.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        handleCallChatClick();

        texttospeechFunctionality();

        registerForContextMenu(rashiIntro);
       /* if (getTodayRashifalASync != null) {
            getTodayRashifalASync.cancel(true);
        }
        getTodayRashifalASync = new GetTodayRashifalASync();
        getTodayRashifalASync.execute();*/
        //getMonthlyHoroscope();

        // FOR WHATS APP
        _whatsAppTitle = activity.getResources().getString(R.string.text_share_whatsapp_heading_monthly);
        _whatsAppTitle = _whatsAppTitle.replace("#", activity.getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiType]);
        _whatsAppTitle = _whatsAppTitle.replace("$", getMonthNameForWhatsApp());

        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_22_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_22_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S22");

                CustomAddModel modal = topAdData.getImageObj().get(0);

                /*if (modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)) {
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), activity,LANGUAGE_CODE, 0);
                } else {
                    if (modal.getImgthumbnailurl() != null && !modal.getImgthumbnailurl().equals("")) {
                        Uri uri = Uri.parse(modal.getImgthumbnailurl());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri);
                        startActivity(i);
                    }
                }*/

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });


        return view;
    }

    /**
     * text to speech Functionality
     */
    private void texttospeechFunctionality() {

        setmTtsCallbackListener(MonthlyHoroscopeFragment.this);

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


    //   private String clipBoardText = "";

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //    clipBoardText = ((FlowTextView) v).getText().toString();
        menu.setHeaderTitle(getString(R.string.select_item));
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menucopy, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (getUserVisibleHint() == false) {
            return false;
        }

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.copyMenu:
                CUtils.copyTextToClipBoard(CUtils.getShareData(getActivity(), _whatsAppTitle, rashiIntro.getText().toString()), getActivity());
                return true;
            case R.id.shareMenu:
                CUtils.sharePrediction(getActivity(), "", CUtils.getShareData(getActivity(), _whatsAppTitle, rashiIntro.getText().toString()));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

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
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_MONTHLY,
                null);
        // END

        String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim()
                + "\n" + rashiIntro.getText().toString();
        if (stringToSpeak != null) {
            CUtils.shareHoroscope(getActivity(), stringToSpeak);
        }
    }

    private void imageCopyClick() {
        // GA
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_COPY_TEXT_MONTHLY,
                null);
        // END

        String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim()
                + "\n" + rashiIntro.getText().toString();
        if (stringToSpeak != null) {
            CUtils.copyTextToClipBoard(stringToSpeak, getActivity());
        }
    }

    private void imagePlayPauseClick() {
        // GA
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_MONTHLY,
                null);
        // END

        if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
            String stringToSpeak = _tvShowMoonRashiSign.getText().toString().trim()
                    + "\n" + rashiIntro.getText().toString();
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

    private void getMonthlyHoroscope() {
        /*if (getTodayRashifalASync != null) {
            getTodayRashifalASync.cancel(true);
        }
        getTodayRashifalASync = new GetTodayRashifalASync();
        getTodayRashifalASync.execute();*/
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
        SharedPreferences monthlyHoroscopePref = CUtils.getMonthlyPref(activity, LANGUAGE_CODE);
        String currentMonthlyHoroscopeKey = dateFormat.format(date.getTime());
        String savedMonthlyHoroscopeKey = monthlyHoroscopePref.getString(CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY, "");
        if (!currentMonthlyHoroscopeKey.equalsIgnoreCase(savedMonthlyHoroscopeKey)) {
            if (CUtils.isConnectedWithInternet(activity)) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    playStopImgll.setOnClickListener(null);
                    copyImgll.setOnClickListener(null);
                    shareImgll.setOnClickListener(null);
                }
                CUtils.getMonthlyPredictinDetail(LANGUAGE_CODE, activity, MonthlyHoroscopeFragment.this, rashiType, MONTHLY_HOROSCOPE);
            } else {
                MyCustomToast mct2 = new MyCustomToast(activity, activity.getLayoutInflater(), activity, (((DetailedHoroscope) activity).regularTypeface));
                mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
            }
        } else {
            showMonthlyHoroscope(CUtils.getRasiPrediction(monthlyHoroscopePref, rashiType));
        }
    }


    /**
     * This is AsyncTask to fetch today's weekly and monthly horoscope RSS feed.
     *
     * @author Hukum
     * @since 15-May-2013
     */
    /*private class GetTodayRashifalASync extends AsyncTask<String, Long, Void> {
        String errorStr;

        @Override
        protected Void doInBackground(String... params) {
            try {
                //rashifal = CUtils.getMonthlyPredictinDetail(LANGUAGE_CODE, activity,MonthlyHoroscopeFragment.this, rashiType);
            } catch (final RuntimeException e) {
                serverError = serverError + e.getMessage();

            }
            return null;
        }

        *//* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     *//*
        @Override
        protected void onPostExecute(Void result) {
            try {
                if (progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
                playStopImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
                copyImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
                shareImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
            } catch (Exception e) {
                // nothing

            }

            CUtils.getRobotoFont(
                    getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

            if (rashifal != null) {
                rashifal[0] = rashifal[0].replace("AstroSage.com,", "");
                // rashiIntro.setTypeface(Typeface.DEFAULT); //Set Typeface to Default as this prediction comes from server in unicode and some characters are not displayed correctly if Hindi Typeface is set.
                rashiIntro.setText(Html.fromHtml(rashifal[0]));
                rashiIntro.setContentDescription(Html.fromHtml(rashifal[0]));
                if (rashifal[1].equals("NO_INTERNET")) {
                    rashiIntro.setText(" ");
                    rashiIntro.setContentDescription(" ");
                    MyCustomToast mct2 = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), (((DetailedHoroscope) activity).regularTypeface));
                    mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
                }

            } else {
                if (isToastShouldShow) {
                    showToast();
                }
                isToastShouldShow = true;
            }

            super.onPostExecute(result);
        }

        *//* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     *//*
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            playStopImgll.setOnClickListener(null);
            copyImgll.setOnClickListener(null);
            shareImgll.setOnClickListener(null);
            super.onPreExecute();
        }
    }*/


    /*private String getTodayDate() {
        Date date = new Date();
         DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,Locale.ENGLISH);
         String[] dateElements = dateFormat.format(date.getTime()).split("/");
         int month = Integer.valueOf(dateElements[0]);
         String year = "20"+dateElements[2];
         String result = dateElements[1]+" "+activity.getResources().getStringArray(R.array.MonthName)[month-1]+activity.getResources().getString(R.string.comma)+" "+year;
        return  result;
    }
    private String getWeeklyDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int satrtDate,startMonth,endDate,endMonth;
        satrtDate = calendar.get(Calendar.DAY_OF_MONTH);
        startMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(calendar.getTimeInMillis()+MILLIs_IN_WEEK);
        endDate = calendar.get(Calendar.DAY_OF_MONTH);
        endMonth = calendar.get(Calendar.MONTH);
        String formattedString = satrtDate+" "+activity.getResources().getStringArray(R.array.MonthName)[startMonth]+" "+activity.getResources().getString(R.string.str_to)+" "+
                                        endDate+" "+activity.getResources().getStringArray(R.array.MonthName)[endMonth];
        return formattedString;
    }*/
    private String getMonthName() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String formattedString = activity.getResources().getStringArray(R.array.MonthName)[month] + " " + year;
        return formattedString;
    }

    private String getMonthNameForWhatsApp() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String formattedString = activity.getResources().getStringArray(R.array.WhatsAppMonthName)[month] + " " + year;
        return formattedString;
    }

    @Override
    public void onStart() {
        super.onStart();
        // mPlusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        resetSpeakBtn(playStopImg);
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
        // mPlusClient.disconnect();
    }

    @Override
    public void onDestroy() {
       /* if (getTodayRashifalASync.getStatus() == Status.RUNNING)
            getTodayRashifalASync.cancel(true);*/

        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateData();
        // Refresh the state of the +1 button each time we receive focus.
        //mPlusOneStandardButtonWithAnnotation.initialize(mPlusClient, URL, PLUS_ONE_REQUEST_CODE);
        //  mPlusOneStandardButtonWithAnnotation.initialize(URL, PLUS_ONE_REQUEST_CODE);//CHANGED BY DEEPAK ON 21-11-2014
    }

    public void updateData() {

        if (activity != null) {
            CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);

            rashiType = ((DetailedHoroscope) activity).rashiType;
            if (rashiType == -1) {
                rashiType = 0;
            }
            setSpinner();
            getRashifalOfSelectedMoonSign(rashiType);
        }

    }
   /* @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionSuspended(int i) {

    }*/

    public String getWhatsAppTitle() {
        return _whatsAppTitle;
    }

    public String getDescription() {
        return rashiIntro.getText().toString();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            if (isToastShouldShow) {
                showToast();
            }
            isToastShouldShow = true;
        } else {
            isToastShouldShow = false;
        }
    }

    private void showToast() {
        Toast.makeText(getActivity(), serverError, Toast.LENGTH_SHORT).show();
        serverError = "";
    }


    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "22");
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
            spinnerScreenList.setPopupBackgroundResource(R.drawable.spinner_dropdown);
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
                textView.setTextSize(16);
                textView.setText(pageTitles[position]);
                //textView.setTypeface(regularTypeface);


                return view;
            }


            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                view.setBackgroundColor(activity.getResources().getColor(R.color.bg_card_view_color));
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
            moonSign = rashiType;
        } else if (activity instanceof InputPanchangActivity) {
            moonSign = CUtils.getMoonSignIndex(activity);
        }
        spinnerScreenList.setSelected(false);  // must
        spinnerScreenList.setSelection(moonSign, true);
        spinnerScreenList
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        rashiType = position;
                        getRashifalOfSelectedMoonSign(position);

                        if (activity instanceof DetailedHoroscope) {
                            ((DetailedHoroscope) activity).changeMoonSign(position);
                        }
                        //CUtils.saveIntData(activity, "moon_sign", position);
                        //setCurrentView(position, false);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }

                });
    }

    private void getRashifalOfSelectedMoonSign(int rashiType) {
        this.rashiType = rashiType;
        strRashiName = activity.getResources().getStringArray(R.array.rashiName_list)[rashiType];
        _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);

        setHeadingText();

       /* if (getTodayRashifalASync != null) {
            getTodayRashifalASync.cancel(true);
        }

        getTodayRashifalASync = new GetTodayRashifalASync();
        //getTodayRashifalASync.execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getTodayRashifalASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getTodayRashifalASync.execute();
        }*/
        getMonthlyHoroscope();
    }

    private void setHeadingText() {
        String heading = CUtils.returnFormattedString(LANGUAGE_CODE, activity.getResources().getString(R.string.moonSignMonthly));
        heading = heading.replace("#", strRashiName);
        heading = heading.replace("$", getMonthName());
        _tvShowMoonRashiSign.setText(heading);
       scrollToTop();
    }
    private void scrollToTop(){
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.setFocusableInTouchMode(true);
                scrollView.fullScroll(View.FOCUS_UP);
                scrollView.smoothScrollTo(0, parentRL.getTop());
            }
        });
    }
    @Override
    public void doActionAfterGetResult(String response, int method) {
        if (activity != null) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
            String MonthlyHoroscopeKey = dateFormat.format(date.getTime());
            SharedPreferences monthlyHoroscopePref = CUtils.getMonthlyPref(activity, LANGUAGE_CODE);
            try {
                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");

            } catch (Exception e) {

            }
            List<CMessage> listMessage = LibCUtils.parseXML(response);
            if (listMessage != null) {
                CUtils.saveRasiPrediction(listMessage, monthlyHoroscopePref,
                        CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY, MonthlyHoroscopeKey);
                showMonthlyHoroscope(CUtils.getRasiPrediction(monthlyHoroscopePref, rashiType));
            }
        }
    }

    @Override
    public void doActionOnError(String response) {
        try {
            if (progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }
            playStopImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
            copyImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
            shareImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
        } catch (Exception e) {

        }
    }

    public void showMonthlyHoroscope(String[] rashifal) {

        try {
            if (progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }
            playStopImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
            copyImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
            shareImgll.setOnClickListener(MonthlyHoroscopeFragment.this);
        } catch (Exception e) {

        }

        CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);

        if (rashifal != null) {
            rashifal[0] = rashifal[0].replace("AstroSage.com,", "");
            // rashiIntro.setTypeface(Typeface.DEFAULT); //Set Typeface to Default as this prediction comes from server in unicode and some characters are not displayed correctly if Hindi Typeface is set.
            rashiIntro.setText(Html.fromHtml(rashifal[0]));
            rashiIntro.setContentDescription(Html.fromHtml(rashifal[0]));
            /*if (rashifal[1].equals("NO_INTERNET")) {
                rashiIntro.setText(" ");
                rashiIntro.setContentDescription(" ");
                MyCustomToast mct2 = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), (((DetailedHoroscope) activity).regularTypeface));
                mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
            }*/

        } else {
            if (isToastShouldShow) {
                showToast();
            }
            isToastShouldShow = true;
        }
    }

    private void handleCallChatClick(){
        callNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_MONTHLY_HOROSCOPE_CALL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.MONTHLY_HOROSCOPE_CALL_PARTNER_ID);
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
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_MONTHLY_HOROSCOPE_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.MONTHLY_HOROSCOPE_CHAT_PARTNER_ID);
                    //com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CHAT, activity);
                    com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CALL, activity);//redirect to ai list
                } catch (Exception e) {
                    //
                }
            }
        });
    }
}
