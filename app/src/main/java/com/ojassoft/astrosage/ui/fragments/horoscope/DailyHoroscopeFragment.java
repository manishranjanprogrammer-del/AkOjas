package com.ojassoft.astrosage.ui.fragments.horoscope;

import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.isTextToSpeechAvailable;
import static com.ojassoft.astrosage.ui.act.BaseTtsActivity.mTextToSpeech;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.TTS_CHAR_LIMIT;
import static com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope.setmTtsCallbackListener;
import static com.ojassoft.astrosage.utils.CGlobalVariables.daily_auspicious_muhurat;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanHoroscopeRemedies;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.cards.Panchang;
import com.ojassoft.astrosage.ui.customcontrols.FlowTextView;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.HttpUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DailyHoroscopeFragment extends Fragment implements TtsCallbackListener, OnClickListener, SendDataBackToComponent {

    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static final String URL = CGlobalVariables.gPlusUrl;
    public final int DAILY_TYPE = 0;
    public final int TOMORROW_TYPE = 1;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public int rashiType;
    private RelativeLayout parentRL;
    private RelativeLayout callNowBtn, chatNowBtn;
    private TextView titleChatCall, callNowBtnTxt, chatNowBtnTxt;
    public Spinner spinnerScreenList;
    boolean isToastShouldShow;
    Button nextDayHoroscope;
    String serverError = "Server Error: ";
    int horoscopeType;
    String strRashiName = "";
    FlowTextView rashiIntro;
    ImageView _imageRashiWithoutName;
    String _whatsAppTitle = "";
    AdData topAdData;
    String[] rashifal;
    ArrayList<BeanHoroscopeRemedies> beanHoroscopeRemedies;
    LinearLayout llCustomAdv = null;
    Activity activity;
    View view = null;
    private TextView _tvShowMoonRashiSign;
    private ProgressBar progressBar;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    // private long MILLIs_IN_WEEK = 6 * 24 * 60 * 60 * 1000;
    private ArrayList<AdData> adList;
    private AajKaPanchangModel model;
    //private GetTodayRashifalASync getTodayRashifalASync;
    //private GetRemediesASync getRemediesASync;
    //private LinearLayout llRamedies;
    private CardView cvRemedy, cvTotalRating, auspiciousCV;
    private TextView tvRemediesTitle, tvRemediesContent, tvHealth, tvWealth, tvFamily, tvLoveMatters, tvOccupation, tvMarriedLife, tvTodayRating;
    private RatingBar ratingBarHealth, ratingBarWealth, ratingBarFamily, ratingBarMattersRating, ratingBarOccupation, ratingBarMarriedLife;
    private BeanPlace beanPlace;
    private LinearLayout llauspicious;
    private ScrollView scrollview;
    private LinearLayout playStopImgll, shareImgll, copyImgll;
    private ImageView playStopImg;
    int TODAY_PREDICTION = 1;
    int TOMORROW_PREDICTION = 2;
    int TODAY_REMEDIES = 3;
    int TOMORROW_REMEDIES = 4;

    public static DailyHoroscopeFragment newInstance(int rashiType) {

        DailyHoroscopeFragment dailyHoroscopeFragment = new DailyHoroscopeFragment();
        Bundle args = new Bundle();
        args.putInt("rashiType", rashiType);
        dailyHoroscopeFragment.setArguments(args);

        return dailyHoroscopeFragment;
    }

    public static void main(String[] args) {

    }

    private void initValues() {
        //regularTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        //mediumTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium);
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

       /* if (getRemediesASync.getStatus() == Status.RUNNING)
            getRemediesASync.cancel(true);*/

        activity = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        ((BaseInputActivity) getActivity()).LANGUAGE_CODE = LANGUAGE_CODE;
        initValues();
        //horoscopeType = getArguments().getInllwhatsappt("horoscopeType", 0);
        rashiType = getArguments().getInt("rashiType", 0);
        if (rashiType == -1) {
            rashiType = 0;
        }
        /* rashiType = ((DetailedHoroscope) activity).rashiType;
         */

        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.lay_daily_horoscope, container,false);
        if (view == null) {
            view = inflater.inflate(R.layout.lay_daily_horoscope, container, false);
        } /*else {
            ((ViewGroup) view.getParent()).removeView(view);
        }*/
        spinnerScreenList = (Spinner) view.findViewById(R.id.sign_spinner);
        parentRL = view.findViewById(R.id.parentRL);
        callNowBtn = view.findViewById(R.id.call_now_btn);
        chatNowBtn = view.findViewById(R.id.chat_now_btn);
        callNowBtnTxt = view.findViewById(R.id.call_now_btn_txt);
        chatNowBtnTxt = view.findViewById(R.id.chat_now_btn_txt);
        titleChatCall = view.findViewById(R.id.titleChatCall);
        cvRemedy = (CardView) view.findViewById(R.id.cvRemedy);
        cvTotalRating = (CardView) view.findViewById(R.id.cvTotalRating);
        auspiciousCV = view.findViewById(R.id.auspiciousCV);
        tvRemediesTitle = (TextView) view.findViewById(R.id.tvRemediesTitle);
        tvRemediesContent = (TextView) view.findViewById(R.id.tvRemediesContent);
        tvHealth = (TextView) view.findViewById(R.id.tvHealth);
        tvWealth = (TextView) view.findViewById(R.id.tvWealth);
        tvFamily = (TextView) view.findViewById(R.id.tvFamily);
        tvLoveMatters = (TextView) view.findViewById(R.id.tvLoveMatters);
        tvOccupation = (TextView) view.findViewById(R.id.tvOccupation);
        tvMarriedLife = (TextView) view.findViewById(R.id.tvMarriedLife);
        ratingBarHealth = (RatingBar) view.findViewById(R.id.ratingBarHealth);
        ratingBarWealth = (RatingBar) view.findViewById(R.id.ratingBarWealth);
        ratingBarFamily = (RatingBar) view.findViewById(R.id.ratingBarFamily);
        ratingBarMattersRating = (RatingBar) view.findViewById(R.id.ratingBarMattersRating);
        ratingBarOccupation = (RatingBar) view.findViewById(R.id.ratingBarOccupation);
        ratingBarMarriedLife = (RatingBar) view.findViewById(R.id.ratingBarMarriedLife);
        tvTodayRating = (TextView) view.findViewById(R.id.tvTodayRating);
        scrollview = (ScrollView) view.findViewById(R.id.scrollview);
        llauspicious = (LinearLayout) view.findViewById(R.id.llauspicious);
        nextDayHoroscope = ((Button) view.findViewById(R.id.butTomorrowPrediction));
        LinearLayout whatsAppBtn = (LinearLayout) view.findViewById(R.id.whatsappll);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        rashiIntro = (FlowTextView) view.findViewById(R.id.textViewPrediction);
        rashiIntro.setColor(getResources().getColor(R.color.text_color_black));
        _tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);

        //setSpinner();
        //Add advertisment in footer 10-Dec-2015
        if (llCustomAdv == null) {
            llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
            llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, (((BaseInputActivity) activity).regularTypeface), "SHODA"));
        }

        /*mPlusClient = new PlusClient.Builder(getActivity(), DailyHoroscopeFragment.this, DailyHoroscopeFragment.this)
                .clearScopes()
                .build();*/
        whatsAppBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof DetailedHoroscope) {
                    ((DetailedHoroscope) activity).shareMessageWithWhatsApp(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_DAILY);
                } else if (activity instanceof InputPanchangActivity) {
                    ((InputPanchangActivity) activity).shareMessageWithWhatsApp();
                }
            }
        });


        strRashiName = activity.getResources().getStringArray(R.array.rashiName_list)[rashiType];
        horoscopeType = DAILY_TYPE;
        if (horoscopeType == DAILY_TYPE) {
            // here we set moonrashi sign
            setHeadingText();
            // FOR WHATS APP
            _whatsAppTitle = activity.getResources().getString(R.string.text_share_whatsapp_heading_daily);
            _whatsAppTitle = _whatsAppTitle.replace("#", activity.getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiType]);
            _whatsAppTitle = _whatsAppTitle.replace("$", getTodayDateForWhatsApp());
        }
        titleChatCall.setTypeface((((BaseInputActivity) activity).mediumTypeface), Typeface.BOLD);
        callNowBtnTxt.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        chatNowBtnTxt.setTypeface((((BaseInputActivity) activity).mediumTypeface));
        handleCallChatClick();

        _tvShowMoonRashiSign.setTypeface((((BaseInputActivity) activity).mediumTypeface), Typeface.BOLD);
        //here we set icon of particular sign(rashi).
        _imageRashiWithoutName = (ImageView) view.findViewById(R.id.imageViewRasi);
        _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);
        //here we show introduction of rashi.
        rashiIntro.setTypeface((((BaseInputActivity) activity).robotRegularTypeface));
        rashiIntro.setTextSize(activity.getResources().getDimension(R.dimen.body_text_size));
        rashiIntro.setTextColor(getResources().getColor(R.color.text_color_black));
        rashiIntro.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);

        //text to speech implementation if language is english or hindi
        //  if (LANGUAGE_CODE == CGlobalVariables.ENGLISH || LANGUAGE_CODE == CGlobalVariables.HINDI) {
        texttospeechFunctionality();
        //   }

        registerForContextMenu(rashiIntro);
        /*if (getTodayRashifalASync != null) {
            getTodayRashifalASync.cancel(true);
        }
        getTodayRashifalASync = new GetTodayRashifalASync();
        //getTodayRashifalASync.execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getTodayRashifalASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getTodayRashifalASync.execute();
        }*/
        getTodayRashifal();

        //start remedies Async
       /* if (getRemediesASync != null) {
            getRemediesASync.cancel(true);
        }*/
        getRemedies();
        /*getRemediesASync = new GetRemediesASync();
        //getRemediesASync.execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getRemediesASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getRemediesASync.execute();
        }*/

        //ADD TOMORROW BUTTON EVENT HANDLER
        nextDayHoroscope.setTypeface((((BaseInputActivity) activity).regularTypeface));

        if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            nextDayHoroscope.setText(activity.getResources().getString(R.string.tomorrow_horoscope).toUpperCase());
        }
        nextDayHoroscope.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (horoscopeType == DAILY_TYPE)
                    horoscopeType = TOMORROW_TYPE;
                else if (horoscopeType == TOMORROW_TYPE)
                    horoscopeType = DAILY_TYPE;

               /* if (getTodayRashifalASync != null) {
                    getTodayRashifalASync.cancel(true);
                }
//                changeButtonText(v);
                getTodayRashifalASync = new GetTodayRashifalASync();
                //getTodayRashifalASync.execute();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getTodayRashifalASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    getTodayRashifalASync.execute();
                }*/
                getTodayRashifal();

               /* if (getRemediesASync != null) {
                    getRemediesASync.cancel(true);
                }*/
                getRemedies();
               /* getRemediesASync = new GetRemediesASync();
                // getRemediesASync.execute();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getRemediesASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    getRemediesASync.execute();
                }*/


                //Get Next Day Horoscope
                /*cvRemedy.setVisibility(View.GONE);
                cvTotalRating.setVisibility(View.GONE);*/
            }
        });

        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        topAdImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_19_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_19_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S19");

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

        //llRamedies = (LinearLayout)view.findViewById(R.id.llRamedies);

        return view;
    }

    /**
     * text to speech Functionality
     */
    private void texttospeechFunctionality() {

        // setmTtsCallbackListener(DailyHoroscopeFragment.this);

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

    private void setHeadingText() {
        String heading = CUtils.returnFormattedString(LANGUAGE_CODE, activity.getResources().getString(R.string.MoonSignDaily));
        heading = heading.replace("#", strRashiName);
        heading = heading.replace("$", getTodayDate());
        _tvShowMoonRashiSign.setText(heading);
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
                textView.setTextSize(16);
                textView.setText(pageTitles[position]);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setBackgroundColor(getResources().getColor(R.color.bg_card_view_color));
                //textView.setTypeface(regularTypeface);


                return view;
            }


            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_layout, null);
                try {
                    view.setBackgroundColor(activity.getResources().getColor(R.color.bg_card_view_color));
                    TextView textView = (TextView) view.findViewById(R.id.textview);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(16);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setBackgroundColor(getResources().getColor(R.color.bg_card_view_color));
                    String title = pageTitles[position];

                    textView.setText(title);
                    //textView.setTypeface(regularTypeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


                        getRashifalOfSelectedMoonSign(position);
                        rashiType = position;
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

    private void changeButtonText() {
        if (horoscopeType == DAILY_TYPE) {
            if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.ENGLISH) {
                nextDayHoroscope.setText(activity.getResources().getString(R.string.tomorrow_horoscope).toUpperCase());
            } else {
                nextDayHoroscope.setText(activity.getResources().getString(R.string.tomorrow_horoscope));
            }
        }

        if (horoscopeType == TOMORROW_TYPE) {
            if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.ENGLISH) {
                nextDayHoroscope.setText(activity.getResources().getString(R.string.today_horoscope).toUpperCase());
            } else {
                nextDayHoroscope.setText(activity.getResources().getString(R.string.today_horoscope));
            }
        }
        scrollToTop();
    }

    // private String clipBoardText = "";

    private void changeTitleForPrediction() {
        String heading = "";
        //_tvShowMoonRashiSign = (TextView) view.findViewById(R.id.textViewShowMoonRashiSign);
        if (horoscopeType == DAILY_TYPE) {
            heading = CUtils.returnFormattedString(LANGUAGE_CODE, activity.getResources().getString(R.string.MoonSignDaily));
            heading = heading.replace("$", getTodayDate());

            // FOR WHATS APP
            _whatsAppTitle = activity.getResources().getString(R.string.text_share_whatsapp_heading_daily);
            _whatsAppTitle = _whatsAppTitle.replace("#", activity.getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiType]);
            _whatsAppTitle = _whatsAppTitle.replace("$", getTodayDateForWhatsApp());
        }

        if (horoscopeType == TOMORROW_TYPE) {

            heading = CUtils.returnFormattedString(LANGUAGE_CODE, activity.getResources().getString(R.string.MoonSignTomorrow));
            heading = heading.replace("$", getTomorrowDate());

            //FOR WHATSAPP
            _whatsAppTitle = activity.getResources().getString(R.string.text_share_whatsapp_heading_daily_tomorrow);
            _whatsAppTitle = _whatsAppTitle.replace("#", activity.getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiType]);
            _whatsAppTitle = _whatsAppTitle.replace("$", getTomorrowDateForWhatsApp());

        }

        heading = heading.replace("#", strRashiName);
        _tvShowMoonRashiSign.setText(heading);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //clipBoardText = ((FlowTextView) v).getText().toString();
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
                //CUtils.sharePrediction(getActivity(), _tvShowMoonRashiSign.getText().toString(), clipBoardText);
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
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_DAILY,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_DAILY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

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
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_COPY_TEXT_DAILY,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_COPY_TEXT_DAILY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

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
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_DAILY,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_DAILY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

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

    private void setRemediesLay() {
        Typeface regularTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);
        Typeface mediumTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium);

        try {
            if (beanHoroscopeRemedies != null && beanHoroscopeRemedies.size() > 0) {


                tvRemediesTitle.setText(activity.getResources().getString(R.string.remedy) + activity.getResources().getString(R.string.colon_horo));
                tvHealth.setText(activity.getResources().getString(R.string.health) + activity.getResources().getString(R.string.colon_horo));
                tvWealth.setText(activity.getResources().getString(R.string.wealth) + activity.getResources().getString(R.string.colon_horo));
                tvFamily.setText(activity.getResources().getString(R.string.family) + activity.getResources().getString(R.string.colon_horo));
                tvLoveMatters.setText(activity.getResources().getString(R.string.love_matters) + activity.getResources().getString(R.string.colon_horo));
                tvOccupation.setText(activity.getResources().getString(R.string.occupation) + activity.getResources().getString(R.string.colon_horo));
                tvMarriedLife.setText(activity.getResources().getString(R.string.married_life) + activity.getResources().getString(R.string.colon_horo));

                if (horoscopeType == DAILY_TYPE) {
                    tvTodayRating.setText(activity.getResources().getString(R.string.today_rating) + activity.getResources().getString(R.string.colon_horo));
                } else {
                    tvTodayRating.setText(activity.getResources().getString(R.string.tomorrow_rating) + activity.getResources().getString(R.string.colon_horo));
                }

                tvRemediesTitle.setTypeface((mediumTypeface), Typeface.BOLD);
                tvHealth.setTypeface((mediumTypeface));
                tvWealth.setTypeface((mediumTypeface));
                tvFamily.setTypeface((mediumTypeface));
                tvLoveMatters.setTypeface((mediumTypeface));
                tvOccupation.setTypeface((mediumTypeface));
                tvMarriedLife.setTypeface((mediumTypeface));
                tvTodayRating.setTypeface((mediumTypeface), Typeface.BOLD);

                tvRemediesContent.setTypeface((regularTypeface));

                BeanHoroscopeRemedies data = beanHoroscopeRemedies.get(rashiType);
                tvRemediesContent.setText(data.getRemedy());

                float rating = Float.valueOf(data.getHealth());
                ratingBarHealth.setRating(rating);

                rating = Float.valueOf(data.getWealth());
                ratingBarWealth.setRating(rating);

                rating = Float.valueOf(data.getFamily());
                ratingBarFamily.setRating(rating);

                rating = Float.valueOf(data.getLoveMatters());
                ratingBarMattersRating.setRating(rating);

                rating = Float.valueOf(data.getOccupation());
                ratingBarOccupation.setRating(rating);

                rating = Float.valueOf(data.getMarriedLife());
                ratingBarMarriedLife.setRating(rating);
            }
        } catch (Exception ex) {
            //Log.i("", ex.getMessage());
        }
    }

    private String getTodayDate() {
        String result = "";
        Date date = new Date();
        String dateStr = CUtils.getStringData(activity, "DAILY_HOROSCOPE_DATE", "");
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US).parse(dateStr);
            }
        } catch (Exception e) {

        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH);
        String[] dateElements = dateFormat.format(date.getTime()).split("/");
        if (dateElements != null && !(dateElements.length < 3)) {
            int month = Integer.valueOf(dateElements[0]);
            String year = "20" + dateElements[2];
            if (1 <= month && month <= 12) {
                result = dateElements[1] + " " + activity.getResources().getStringArray(R.array.MonthName)[month - 1] + activity.getResources().getString(R.string.comma) + " " + year;
            } else {
                result = dateElements[1] + " " + month + activity.getResources().getString(R.string.comma) + " " + year;
            }
        }

        return result;
    }

    private String getTodayDateForWhatsApp() {
        Date date = new Date();
        String dateStr = CUtils.getStringData(activity, "DAILY_HOROSCOPE_DATE", "");
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US).parse(dateStr);
            }
        } catch (Exception e) {

        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH);
        String[] dateElements = dateFormat.format(date.getTime()).split("/");
        if (dateElements != null && dateElements.length >= 3) {
            int month = Integer.valueOf(dateElements[0]);
            String year = "20" + dateElements[2];
            month = (month - 1) % 12;
            String result = dateElements[1] + " " + activity.getResources().getStringArray(R.array.WhatsAppMonthName)[month] + ", " + year;
            return result;
        }
        return "";
    }

    private String getTomorrowDate() {

        Calendar cal = Calendar.getInstance();
        String dateStr = CUtils.getStringData(activity, "DAILY_HOROSCOPE_DATE", "");
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
                cal.setTime(sdf.parse(dateStr));
            }
        } catch (Exception e) {

        }
        cal.add(Calendar.DATE, 1);
        int month = cal.get(Calendar.MONTH);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String result = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + " " + activity.getResources().getStringArray(R.array.MonthName)[month] + activity.getResources().getString(R.string.comma) + " " + year;
        return result;
    }

    private String getTomorrowDateForWhatsApp() {

        Calendar cal = Calendar.getInstance();
        String dateStr = CUtils.getStringData(activity, "DAILY_HOROSCOPE_DATE", "");
        try {
            if (!TextUtils.isEmpty(dateStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
                cal.setTime(sdf.parse(dateStr));
            }
        } catch (Exception e) {

        }
        cal.add(Calendar.DATE, 1);
        int month = cal.get(Calendar.MONTH);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String result = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + " " + activity.getResources().getStringArray(R.array.WhatsAppMonthName)[month] + ", " + year;
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        //mPlusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
        // mPlusClient.disconnect();
    }

    @Override
    public void onDestroy() {
        /*if (getTodayRashifalASync.getStatus() == Status.RUNNING)
            getTodayRashifalASync.cancel(true);*/

        /*if (getRemediesASync.getStatus() == Status.RUNNING)
            getRemediesASync.cancel(true);*/
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (activity instanceof DetailedHoroscope) {
            CUtils.getRobotoFont(
                    activity, LANGUAGE_CODE, CGlobalVariables.regular);

            rashiType = ((DetailedHoroscope) activity).rashiType;
            if (rashiType == -1) {
                rashiType = 0;
            }

        }
        setSpinner();
        getRashifalOfSelectedMoonSign(rashiType);
        // Refresh the state of the +1 button each time we receive focus.
        //mPlusOneStandardButtonWithAnnotation.initialize(mPlusClient, URL, PLUS_ONE_REQUEST_CODE);
        //mPlusOneStandardButtonWithAnnotation.initialize(URL, PLUS_ONE_REQUEST_CODE);//CHANGED BY DEEPAK ON 21-11-2014
    }

    @Override
    public void onPause() {
        resetSpeakBtn(playStopImg);
        super.onPause();
    }

    public String getWhatsAppTitle() {
        return _whatsAppTitle;
    }

    /*@Override
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

    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "19");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        return builder.build();
    }*/

    private void getGoodTimeCards(String LuckyNumber) {
        Typeface regularTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);
        Typeface mediumTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium);

        CardView luckyView = (CardView) view.findViewById(R.id.card_view);
        TextView headerTextView = (TextView) view.findViewById(R.id.tvHeader);
        TextView goodTimeTextView = (TextView) view.findViewById(R.id.goodtimeval);
        TextView badTimeTextView = (TextView) view.findViewById(R.id.badtimeval);
        TextView luckeyNumberTextView = (TextView) view.findViewById(R.id.luckeynumberval);
        TextView luckeyColorTextView = (TextView) view.findViewById(R.id.luckeycolorval);
        TextView badDirectionTextView = (TextView) view.findViewById(R.id.baddirectionval);

        TextView lgoodTimeTextView = (TextView) view.findViewById(R.id.lgoodtimeval);
        TextView lbadTimeTextView = (TextView) view.findViewById(R.id.lbadtimeval);
        TextView lluckeyNumberTextView = (TextView) view.findViewById(R.id.lluckeynumberval);
        TextView lluckeyColorTextView = (TextView) view.findViewById(R.id.lluckeycolorval);
        TextView lbadDirectionTextView = (TextView) view.findViewById(R.id.lbaddirectionval);

        headerTextView.setTypeface((mediumTypeface), Typeface.BOLD);
        goodTimeTextView.setTypeface((regularTypeface));
        badTimeTextView.setTypeface((regularTypeface));
        luckeyNumberTextView.setTypeface((regularTypeface));
        luckeyColorTextView.setTypeface((regularTypeface));
        badDirectionTextView.setTypeface((regularTypeface));
        lgoodTimeTextView.setTypeface((mediumTypeface));
        lbadTimeTextView.setTypeface((mediumTypeface));
        lluckeyNumberTextView.setTypeface((mediumTypeface));
        lluckeyColorTextView.setTypeface((mediumTypeface));
        lbadDirectionTextView.setTypeface((mediumTypeface));


        badDirectionTextView.setText(getDishaShool());
        if (LuckyNumber != null && !LuckyNumber.isEmpty() && LuckyNumber.matches("-?\\d+")) {
            luckeyNumberTextView.setText(LuckyNumber);
            luckeyColorTextView.setText(CUtils.getColor(getActivity(), LANGUAGE_CODE, Integer.parseInt(LuckyNumber.trim())));
        } else {
            luckeyNumberTextView.setText("-");
            luckeyColorTextView.setText("-");
        }


//        headerTextView.setTypeface(typeface);
        String goodTime = CUtils.getGoodTime(getActivity(), true, LANGUAGE_CODE, rashiType + 1);
        String badTime = CUtils.getGoodTime(getActivity(), false, LANGUAGE_CODE, rashiType + 1);
        goodTimeTextView.setText(goodTime);
        badTimeTextView.setText(badTime);
        /*auspiciousAndInauspiciousResult = getActivity().getResources().getString(R.string.lucky_for_today_text);
        if (goodTime != null) {
            auspiciousAndInauspiciousResult = auspiciousAndInauspiciousResult.replaceFirst("#", goodTime);
        }
        if (badTime != null) {
            auspiciousAndInauspiciousResult = auspiciousAndInauspiciousResult.replaceFirst("#", badTime);
        }
        if ((getActivity()).luckeyNumber != null) {
            auspiciousAndInauspiciousResult = auspiciousAndInauspiciousResult.replaceFirst("#", (getActivity()).luckeyNumber);
        }
        if ((getActivity()).luckeyNumber != null) {
            auspiciousAndInauspiciousResult = auspiciousAndInauspiciousResult.replaceFirst("#", CUtils.getColor(language_code, Integer.parseInt((getActivity()).luckeyNumber)));
        }
        if ((getActivity()).getDishaShool() != null) {
            auspiciousAndInauspiciousResult = auspiciousAndInauspiciousResult.replaceFirst("#", (getActivity()).getDishaShool());
        }*/

    }

    public String getDishaShool() {
        Calendar calendar = Calendar.getInstance();
        if (model == null) {
            setCalculations(calendar);
        }

        return model.getDishaShoola();

    }

    public void setCalculations(Calendar calendar) {

        String currentLalitude = "";
        String currentLongitude = "";
        String timeZone = "";
        String timeZoneId = "";
        beanPlace = CUtils.getBeanPalce(getActivity());
        Log.e("ForPanchang", "InputPanchang  DailyHoroscope setCalculations() beanPlace="+beanPlace);
        if (beanPlace == null) {
            currentLalitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
            currentLongitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
            timeZone = CUtils.getStringData(getActivity(), CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
            timeZoneId = CUtils.getStringData(getActivity(), CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);
        } else {
            currentLalitude = beanPlace.getLatitude();
            currentLongitude = beanPlace.getLongitude();
            timeZone = beanPlace.getTimeZone();
            timeZoneId = beanPlace.getTimeZoneString();
        }


        //Calendar calendar = Calendar.getInstance();
        Date _datePan = calendar.getTime();

        //Save calender value in shared pref. for further use
        long millis = calendar.getTimeInMillis();
        CUtils.saveLongData(getActivity(), CGlobalVariables.calenderCurrentTime, millis);
        Log.e("ForPanchang", "InputPanchang  DailyHoroscope setCalculations() _datePan="+_datePan+" currentLalitude="+currentLalitude
        +" currentLongitude="+currentLongitude+" timeZone="+timeZone+" timeZoneId="+timeZoneId);
        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan,
                CUtils.getLanguageKey(LANGUAGE_CODE),
                currentLalitude,
                currentLongitude,
                timeZone,
                timeZoneId);
        Log.e("ForPanchang", "InputPanchang  DailyHoroscope setCalculations() calculation="+calculation);
        model = calculation.getPanchang();
        if(model != null){
            Log.e("ForPanchang", "InputPanchang  DailyHoroscope setCalculations() getPanchang="+model.getPanchang());
        }
    }

    private View getLocationBasedCards() {

        String currentLalitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
        String currentLongitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
        String timeZone = CUtils.getStringData(getActivity(), CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
        String timeZoneId = CUtils.getStringData(getActivity(), CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);
        String timeZoneString = CUtils.getStringData(getActivity(), CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);

        if (model == null) {
            setCalculations();
        }

        //Get current Time in millis
        long currentTimeInMillis = CUtils.getLongData(getActivity(), CGlobalVariables.calenderCurrentTime, Calendar.getInstance().getTimeInMillis());

        //Preparing Panchang Relate Cards
        Panchang view = new Panchang(getActivity(), daily_auspicious_muhurat, model, currentLalitude, currentLongitude, timeZone, timeZoneString, currentTimeInMillis, LANGUAGE_CODE);

        return view;
    }

    public void setCalculations() {

        String currentLalitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
        String currentLongitude = CUtils.getStringData(getActivity(), CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
        String timeZone = CUtils.getStringData(getActivity(), CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
        String timeZoneId = CUtils.getStringData(getActivity(), CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);


        Calendar calendar = Calendar.getInstance();
        Date _datePan = calendar.getTime();

        //Save calender value in shared pref. for further use
        long millis = calendar.getTimeInMillis();
        CUtils.saveLongData(getActivity(), CGlobalVariables.calenderCurrentTime, millis);

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, CUtils.getLanguageKey(LANGUAGE_CODE), currentLalitude, currentLongitude, timeZone, timeZoneId);
        model = calculation.getPanchang();
    }

    public void getRashifalOfSelectedMoonSign(int rashiType) {
        this.rashiType = rashiType;
        strRashiName = activity.getResources().getStringArray(R.array.rashiName_list)[rashiType];

        _imageRashiWithoutName.setImageResource(CGlobalVariables.rashiImageWithoutName[rashiType]);

        String heading = CUtils.returnFormattedString(LANGUAGE_CODE, activity.getResources().getString(R.string.MoonSignDaily));
        heading = heading.replace("#", strRashiName);
        heading = heading.replace("$", getTodayDate());
        _tvShowMoonRashiSign.setText(heading);

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
        getTodayRashifal();

        //start remedies Async
        /*if (getRemediesASync != null) {
            getRemediesASync.cancel(true);
        }*/
        getRemedies();
        /*getRemediesASync = new GetRemediesASync();
        //getRemediesASync.execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getRemediesASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getRemediesASync.execute();
        }*/
    }

    public void shareData(String packageName) {
        String heading = "", horoscopeText = "";
        try {

            heading = getWhatsAppTitle();
            horoscopeText = getDescription();

            CUtils.shareData(activity, getShareTextForWhatsApp(heading, horoscopeText), packageName, activity.getResources().getString(R.string.horoscopeText));

            //shareDataOnWhatsApp(getShareTextForWhatsApp(heading, horoscopeText));

            // ADDED BY BIJENDRA ON 14-04-15
            CUtils.googleAnalyticSendWitPlayServie(
                    activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_PREDICTION_ON_WHATSAPP,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHARE_PREDICTION_ON_WHATSAPP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            // END

        } catch (Exception e) {
            //Log.e("shareMessage", e.getMessage());

        }

    }

    private String getShareTextForWhatsApp(String heading,
                                           String horoscopeText) {

        StringBuilder stringBuilder = new StringBuilder();

        // if (((predictionType == CGlobalVariables.MONTHLY_TYPE) && (LANGUAGE_CODE == CGlobalVariables.HINDI))&& CUtils.isSupportUnicodeHindi()) {

        stringBuilder.append(heading + "\n");
        stringBuilder.append(horoscopeText + "\n");

        /*
         * stringBuilder.append("Shared By: "+CUtils.getMyApplicationName(this)+"\n"
         * );
         * stringBuilder.append("https://play.google.com/store/apps/details?id="
         * +getPackageName());
         */
        stringBuilder.append("Download " + CUtils.getMyApplicationName(activity)
                + " App: \n");
        stringBuilder.append("https://go.astrosage.com/akwa");
        return stringBuilder.toString();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            if (isVisibleToUser) {
                setmTtsCallbackListener(DailyHoroscopeFragment.this);
                //texttospeechFunctionality();
            }

        } catch (Exception e) {

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public interface UpdateMoonSign {
        void changeMoonSign(int moonSign);
    }

    private void getTodayRashifal() {
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

        if (activity instanceof InputPanchangActivity) {
            if (horoscopeType == DAILY_TYPE) {
                ((InputPanchangActivity) activity).updateBeanDateTime(true);
            } else if (horoscopeType == TOMORROW_TYPE) {
                ((InputPanchangActivity) activity).updateBeanDateTime(false);
            }
        }

        getTodayRashifalASync();
    }

    private void getTodayRashifalASync() {
        try {
            if (horoscopeType == DAILY_TYPE) {
                Date date = new Date();
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                SharedPreferences todaysHoroscopePref = CUtils.getTodaysHoroscopePref(activity, LANGUAGE_CODE);
                String currentTodaysHoroscopeKeyValue = dateFormat.format(date.getTime());
                String savedTodaysHoroscopeKeyValue = todaysHoroscopePref.getString(CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY, "");
                if (!currentTodaysHoroscopeKeyValue.equalsIgnoreCase(savedTodaysHoroscopeKeyValue)) {
                    if (CUtils.isConnectedWithInternet(activity)) {
                        progressBar.setVisibility(View.VISIBLE);
                        playStopImgll.setOnClickListener(null);
                        copyImgll.setOnClickListener(null);
                        shareImgll.setOnClickListener(null);
                        CUtils.getTodaysPredictinDetail(activity, DailyHoroscopeFragment.this, rashiType, LANGUAGE_CODE, TODAY_PREDICTION);

                    } else {
                        MyCustomToast mct2 = new MyCustomToast(activity, activity.getLayoutInflater(), activity, (((BaseInputActivity) activity).regularTypeface));
                        mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
                    }
                } else {
                    showRashifal(CUtils.getRasiPrediction(todaysHoroscopePref, rashiType));
                }
            } else if (horoscopeType == TOMORROW_TYPE) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                SharedPreferences tomorrowsHoroscope = CUtils.getTomorrowPref(activity, LANGUAGE_CODE);
                String currentTomorrowHoroscopeKey = dateFormat.format(c.getTime());
                String savedTomorrowHoroscopeKey = tomorrowsHoroscope.getString("TOMORROWSHOROSCOPEKEY", "");
                if (!currentTomorrowHoroscopeKey.equalsIgnoreCase(savedTomorrowHoroscopeKey)) {
                    if (CUtils.isConnectedWithInternet(activity)) {
                        progressBar.setVisibility(View.VISIBLE);
                        playStopImgll.setOnClickListener(null);
                        copyImgll.setOnClickListener(null);
                        shareImgll.setOnClickListener(null);
                        CUtils.getTomorrowPredictinDetail(LANGUAGE_CODE, getActivity(), DailyHoroscopeFragment.this, rashiType, TOMORROW_PREDICTION);
                    } else {
                        MyCustomToast mct2 = new MyCustomToast(activity, activity.getLayoutInflater(), activity, (((BaseInputActivity) activity).regularTypeface));
                        mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
                    }
                } else {
                    String[] tomorrowsHoroscopeDetail = {CUtils.getTomorrowHoroscopeData(tomorrowsHoroscope, rashiType), "YES"};
                    showRashifal(tomorrowsHoroscopeDetail);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is AsyncTask to fetch today's weekly and monthly horoscope RSS feed.
     *
     * @author Hukum
     * @since 15-May-2013
     */
    /*private class GetTodayRashifalASync extends AsyncTask<String, Long, Void> {
        //String errorStr = "";

        @Override
        protected Void doInBackground(String... params) {
            try {
                if (horoscopeType == DAILY_TYPE) {
                    rashifal = CUtils.getTodaysPredictinDetail(getActivity(), DailyHoroscopeFragment.this, LANGUAGE_CODE, rashiType, TODAY_PREDICTION);
                } else if (horoscopeType == TOMORROW_TYPE) {
                    rashifal = CUtils.getTomorrowPredictinDetail(LANGUAGE_CODE, getActivity(), rashiType);
                }
            } catch (final RuntimeException e) {
                //errorStr = e.getMessage();
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
                playStopImgll.setOnClickListener(DailyHoroscopeFragment.this);
                copyImgll.setOnClickListener(DailyHoroscopeFragment.this);
                shareImgll.setOnClickListener(DailyHoroscopeFragment.this);
            } catch (Exception e) {
                // nothing
            }
            CUtils.getRobotoFont(
                    getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

            changeButtonText();
            if (rashifal != null) {
                setHeadingText();
                rashifal[0] = rashifal[0].replace("AstroSage.com,", "");
                String luckyNumber = "";
                if (rashifal[0] != null) {
                    String[] splitString = rashifal[0].split("</B>");
                    luckyNumber = splitString[splitString.length - 1].replace("\n", "").trim();
                }
                //rashiIntro.setTypeface(Typeface.DEFAULT); //Set Typeface to Default as this prediction comes from server in unicode and some characters are not displayed correctly if Hindi Typeface is set.
                rashiIntro.setText(Html.fromHtml(rashifal[0]));
                //by abhishek
                if (horoscopeType == DAILY_TYPE) {
                    auspiciousCV.setVisibility(View.VISIBLE);
                    getGoodTimeCards(luckyNumber);
                } else {
                    auspiciousCV.setVisibility(View.GONE);
                }

                llauspicious.removeAllViews();
                View view = getLocationBasedCards();
                llauspicious.addView(view);
                rashiIntro.setContentDescription(Html.fromHtml(rashifal[0]));
                if (rashifal[1].equals("NO_INTERNET")) {
                    rashiIntro.setText(" ");
                    rashiIntro.setContentDescription("");
                    MyCustomToast mct2 = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), (((BaseInputActivity) activity).regularTypeface));
                    mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
                } else
                    changeTitleForPrediction();
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
    private void getRemedies() {
       /* if (getRemediesASync != null) {
            getRemediesASync.cancel(true);
        }
        getRemediesASync = new GetRemediesASync();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getRemediesASync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getRemediesASync.execute();
        }*/
        if (horoscopeType == DAILY_TYPE) {
            getRemediesASync();
        } else if (horoscopeType == TOMORROW_TYPE) {
            getTomorrowRemediesASync();
        }
    }

   /* private class GetRemediesASync extends AsyncTask<String, Long, Void> {
        String data = "";

        @Override
        protected Void doInBackground(String... strings) {
            try {
                beanHoroscopeRemedies = new ArrayList<>();
                if (horoscopeType == DAILY_TYPE) {
                    data = CUtils.getTodayRemedies(LANGUAGE_CODE, getActivity());

                } else if (horoscopeType == TOMORROW_TYPE) {
                    data = CUtils.getTomorrowRemedies(LANGUAGE_CODE, getActivity());
                }
            } catch (final RuntimeException e) {
                //errorStr = e.getMessage();
                //serverError = serverError + e.getMessage();
            } catch (Exception ex) {
                //
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {

                CUtils.getRobotoFont(
                        getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);


                if (!data.equals("")) {
                    Gson gson = new Gson();
                    beanHoroscopeRemedies = gson.fromJson(data, new TypeToken<ArrayList<BeanHoroscopeRemedies>>() {
                    }.getType());
                    // llRamedies.setVisibility(View.VISIBLE);
                    cvRemedy.setVisibility(View.VISIBLE);
                    cvTotalRating.setVisibility(View.VISIBLE);
                    setRemediesLay();
                } else {
                    //llRamedies.setVisibility(View.GONE);
                    cvRemedy.setVisibility(View.GONE);
                    cvTotalRating.setVisibility(View.GONE);
                }
            } catch (JsonSyntaxException ex) {
                //clear the chache
                if (horoscopeType == DAILY_TYPE) {
                    SharedPreferences todaysRemediesPref;
                    if (CUtils.isSupportUnicodeHindi()
                            && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefHindiName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefTamilName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefMarathiName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefBangaliName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefKannadName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefTelguName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefGujaratiName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefMalayalamName,
                                Context.MODE_PRIVATE);
                    } else {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.dailyRemediesPrefEnglishName,
                                Context.MODE_PRIVATE);
                    }

                    SharedPreferences.Editor editor = todaysRemediesPref.edit();
                    editor.putString("Remedies", "");
                    editor.putString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, "");
                    editor.commit();
                } else if (horoscopeType == TOMORROW_TYPE) {

                    SharedPreferences todaysRemediesPref;
                    if (CUtils.isSupportUnicodeHindi()
                            && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefHindiName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefTamilName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefMarathiName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefBangaliName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefKannadName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefTelguName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefGujaratiName,
                                Context.MODE_PRIVATE);
                    } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefMalayalamName,
                                Context.MODE_PRIVATE);
                    } else {
                        todaysRemediesPref = getActivity().getSharedPreferences(
                                CGlobalVariables.tomorrowRemediesPrefEnglishName,
                                Context.MODE_PRIVATE);
                    }

                    SharedPreferences.Editor editor = todaysRemediesPref.edit();
                    editor.putString("Remedies", "");
                    editor.putString(CGlobalVariables.TOMORROW_REMEDIES_PREF_KEY, "");
                    editor.commit();
                }
            } catch (Exception ex) {
                //
            }
            super.onPostExecute(aVoid);
        }
    }*/

    private void getRemediesASync() {
        beanHoroscopeRemedies = new ArrayList<>();
        String data = CUtils.getTodayRemedies(LANGUAGE_CODE, getActivity());
        if (TextUtils.isEmpty(data)) {
            if (CUtils.isConnectedWithInternet(activity)) {
                new HttpUtility(DailyHoroscopeFragment.this).sendPostRequest(CGlobalVariables.dailyRemediesURL, getParamsNew(), TODAY_REMEDIES);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                Gson gson = new Gson();
                beanHoroscopeRemedies = gson.fromJson(data, new TypeToken<ArrayList<BeanHoroscopeRemedies>>() {
                }.getType());
                // llRamedies.setVisibility(View.VISIBLE);
                if (activity != null) {
                    cvRemedy.setVisibility(View.VISIBLE);
                    cvTotalRating.setVisibility(View.VISIBLE);
                    setRemediesLay();
                }
            }catch (Exception e){
                //
            }

        }

    }

    private void getTomorrowRemediesASync() {
        beanHoroscopeRemedies = new ArrayList<>();
        String data = CUtils.getTomorrowRemedies(LANGUAGE_CODE, getActivity());
        if (TextUtils.isEmpty(data)) {
            if (CUtils.isConnectedWithInternet(activity)) {
                Log.e("TomorrowRating", CGlobalVariables.tomorrowRemediesURL);
                new HttpUtility(DailyHoroscopeFragment.this).sendPostRequest(CGlobalVariables.tomorrowRemediesURL, getParamsNew(), TOMORROW_REMEDIES);
            } else {
                Toast.makeText(activity, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            try{
            Gson gson = new Gson();
            beanHoroscopeRemedies = gson.fromJson(data, new TypeToken<ArrayList<BeanHoroscopeRemedies>>() {
            }.getType());
            // llRamedies.setVisibility(View.VISIBLE);
            if (activity != null) {
                cvRemedy.setVisibility(View.VISIBLE);
                cvTotalRating.setVisibility(View.VISIBLE);
                setRemediesLay();
            }
            }catch (Exception e){
                //
            }
        }

    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        //Log.e("TomorrowRating response", method +" =>" +response);
        if (activity != null) {
            try {
                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {

            }
            if (method == TODAY_PREDICTION) {
                Date date = new Date();
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                String todaysHoroscopeKeyValue = dateFormat.format(date.getTime());
                List<CMessage> listMessage = LibCUtils.parseXML(response);
                SharedPreferences todaysHoroscopePref = CUtils.getTodaysHoroscopePref(activity, LANGUAGE_CODE);

                if (listMessage != null && listMessage.size() > 0) {
                    try {
                        CUtils.saveRasiPrediction(listMessage, todaysHoroscopePref,
                                CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY, todaysHoroscopeKeyValue);
                        String serverDateStr = listMessage.get(0).getDate();
                        CUtils.saveStringData(activity, "DAILY_HOROSCOPE_DATE", serverDateStr);
                    } catch (Exception e) {

                    }
                    showRashifal(CUtils.getRasiPrediction(todaysHoroscopePref, rashiType));
                }

            } else if (method == TOMORROW_PREDICTION) {
                SharedPreferences tomorrowsHoroscope = CUtils.getTomorrowPref(activity, LANGUAGE_CODE);

                List<CMessage> listMessage = LibCUtils.parseXML(response);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,
                        Locale.ENGLISH);
                String tomorrowHoroscopeKey = dateFormat.format(c.getTime());
                if (listMessage != null) {
                    // String rashiTitle = "";
                    String rashiPrediction = " ";
                    SharedPreferences.Editor editor = tomorrowsHoroscope.edit();

                    rashiPrediction = listMessage.get(CGlobalVariables.ARIES)
                            .getDescription().toString();
                    editor.putString("ARIES", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.TAURUS)
                            .getDescription().toString();
                    editor.putString("TAURUS", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.GEMINI)
                            .getDescription().toString();
                    editor.putString("GEMINI", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.CANCER)
                            .getDescription().toString();
                    editor.putString("CANCER", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.LEO)
                            .getDescription().toString();
                    editor.putString("LEO", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.VIRGO)
                            .getDescription().toString();
                    editor.putString("VIRGO", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.LIBRA)
                            .getDescription().toString();
                    editor.putString("LIBRA", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.SCORPIO)
                            .getDescription().toString();
                    editor.putString("SCORPIO", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.SAGITTARIUS)
                            .getDescription().toString();
                    editor.putString("SAGITTARIUS", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.CAPRICORN)
                            .getDescription().toString();
                    editor.putString("CAPRICORN", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.AQUARIUS)
                            .getDescription().toString();
                    editor.putString("AQUARIUS", rashiPrediction);

                    rashiPrediction = listMessage.get(CGlobalVariables.PISCES)
                            .getDescription().toString();
                    editor.putString("PISCES", rashiPrediction);

                    editor.putString("TOMORROWSHOROSCOPEKEY", tomorrowHoroscopeKey);
                    editor.commit();
                    String tomorrowsHoroscopeDetail[] = new String[2];
                    tomorrowsHoroscopeDetail[1] = "YES";
                    tomorrowsHoroscopeDetail[0] = CUtils.getTomorrowHoroscopeData(tomorrowsHoroscope, rashiType);
                    showRashifal(tomorrowsHoroscopeDetail);
                }
            } else {
                try {

                    CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);
                    if (method == TODAY_REMEDIES) {
                        SharedPreferences todaysRemediesPref = CUtils.getTodaySharedPreferenceAccordingLang(activity, LANGUAGE_CODE);
                        Date date = new Date();
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                        String todaysRemediesKeyValue = dateFormat.format(date.getTime());
                        if (!TextUtils.isEmpty(response) && response.contains("Rashi")) {
                            SharedPreferences.Editor editor = todaysRemediesPref.edit();
                            editor.putString("Remedies", response);
                            editor.putString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, todaysRemediesKeyValue);
                            editor.commit();
                        }
                    } else if (method == TOMORROW_REMEDIES) {
                        Log.e("TomorrowRating res ", response);
                        SharedPreferences tomorrowRemediesPref = CUtils.getTomorrowSharedPreferenceAccordingLang(activity, LANGUAGE_CODE);
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, 1);
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                        String tomorrowRemediesKeyValue = dateFormat.format(c.getTime());
                        if (!TextUtils.isEmpty(response) && response.contains("Rashi")) {
                            SharedPreferences.Editor editor = tomorrowRemediesPref.edit();
                            editor.putString("Remedies", response);
                            editor.putString(CGlobalVariables.TOMORROW_REMEDIES_PREF_KEY, tomorrowRemediesKeyValue);
                            editor.commit();
                        }
                    }
                    //Log.e("TomorrowRating res ", response);
                    if (!TextUtils.isEmpty(response)) {
                        try {
                            Gson gson = new Gson();
                            beanHoroscopeRemedies = gson.fromJson(response, new TypeToken<ArrayList<BeanHoroscopeRemedies>>() {
                            }.getType());
                            // llRamedies.setVisibility(View.VISIBLE);
                            cvRemedy.setVisibility(View.VISIBLE);
                            cvTotalRating.setVisibility(View.VISIBLE);
                            setRemediesLay();
                        }catch (Exception e){
                            //
                        }
                    } else {
                        //llRamedies.setVisibility(View.GONE);
                        cvRemedy.setVisibility(View.GONE);
                        cvTotalRating.setVisibility(View.GONE);
                    }
                } catch (JsonSyntaxException ex) {
                    //clear the chache
                    if (horoscopeType == DAILY_TYPE) {
                        SharedPreferences todaysRemediesPref;
                        if (CUtils.isSupportUnicodeHindi()
                                && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefHindiName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefTamilName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefMarathiName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefBangaliName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefKannadName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefTelguName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefGujaratiName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefMalayalamName,
                                    Context.MODE_PRIVATE);
                        }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefOdiaName,
                                    Context.MODE_PRIVATE);
                        }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefAssammeseName,
                                    Context.MODE_PRIVATE);
                        } else {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.dailyRemediesPrefEnglishName,
                                    Context.MODE_PRIVATE);
                        }

                        SharedPreferences.Editor editor = todaysRemediesPref.edit();
                        editor.putString("Remedies", "");
                        editor.putString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, "");
                        editor.commit();
                    } else if (horoscopeType == TOMORROW_TYPE) {

                        SharedPreferences todaysRemediesPref;
                        if (CUtils.isSupportUnicodeHindi()
                                && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefHindiName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefTamilName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefMarathiName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefBangaliName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefKannadName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefTelguName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefGujaratiName,
                                    Context.MODE_PRIVATE);
                        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefMalayalamName,
                                    Context.MODE_PRIVATE);
                        }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefAssammeseName,
                                    Context.MODE_PRIVATE);
                        }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefOdiaName,
                                    Context.MODE_PRIVATE);
                        } else {
                            todaysRemediesPref = getActivity().getSharedPreferences(
                                    CGlobalVariables.tomorrowRemediesPrefEnglishName,
                                    Context.MODE_PRIVATE);
                        }

                        SharedPreferences.Editor editor = todaysRemediesPref.edit();
                        editor.putString("Remedies", "");
                        editor.putString(CGlobalVariables.TOMORROW_REMEDIES_PREF_KEY, "");
                        editor.commit();
                    }
                } catch (Exception ex) {
                    Log.i("", "");
                }
            }
        }


    }

    public void showRashifal(String[] rashiphal) {
        try {
            if (progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }
            playStopImgll.setOnClickListener(DailyHoroscopeFragment.this);
            copyImgll.setOnClickListener(DailyHoroscopeFragment.this);
            shareImgll.setOnClickListener(DailyHoroscopeFragment.this);
        } catch (Exception e) {
            // nothing
        }
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        rashifal = rashiphal;
        changeButtonText();
        if (rashifal != null && rashifal.length > 0) {
            setHeadingText();
            rashifal[0] = rashifal[0].replace("AstroSage.com,", "");
            String luckyNumber = "";
            if (rashifal[0] != null) {
                String[] splitString = rashifal[0].split("</B>");
                luckyNumber = splitString[splitString.length - 1].replace("\n", "").trim();
            }
            //rashiIntro.setTypeface(Typeface.DEFAULT); //Set Typeface to Default as this prediction comes from server in unicode and some characters are not displayed correctly if Hindi Typeface is set.
            rashiIntro.setText(Html.fromHtml(rashifal[0]));
            //by abhishek
            if (horoscopeType == DAILY_TYPE) {
                auspiciousCV.setVisibility(View.VISIBLE);
                getGoodTimeCards(luckyNumber);
            } else {
                auspiciousCV.setVisibility(View.GONE);
            }

            llauspicious.removeAllViews();
            View view = getLocationBasedCards();
            llauspicious.addView(view);
            rashiIntro.setContentDescription(Html.fromHtml(rashifal[0]));
            if (rashifal[1].equals("NO_INTERNET")) {
                rashiIntro.setText(" ");
                rashiIntro.setContentDescription("");
                MyCustomToast mct2 = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), (((BaseInputActivity) activity).regularTypeface));
                mct2.show(activity.getResources().getString(R.string.internet_is_not_working));
            } else
                changeTitleForPrediction();
        } else {
            if (isToastShouldShow) {
                showToast();
            }
            isToastShouldShow = true;
        }
    }

    private Map<String, String> getParamsNew() {
        Map<String, String> param = new HashMap<>();
        param.put("language", CUtils.getLanguageCodeName(LANGUAGE_CODE));
        param.put("key", CUtils.getApplicationSignatureHashCode(activity));
        Log.e("TomorrowRating params", param.toString());
        return param;
    }

    @Override
    public void doActionOnError(String response) {
        //Log.i("", response);
    }

    private void scrollToTop(){
        scrollview.post(new Runnable() {
            public void run() {
                scrollview.setFocusableInTouchMode(true);
                scrollview.fullScroll(View.FOCUS_UP);
                scrollview.smoothScrollTo(0, parentRL.getTop());
            }
        });
    }

    private void handleCallChatClick(){
        callNowBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_DAILY_HOROSCOPE_CALL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.DAILY_HOROSCOPE_CALL_PARTNER_ID);
                    com.ojassoft.astrosage.varta.utils.CUtils.openCallList( activity);
                } catch (Exception e) {
                    //
                }
            }
        });

        chatNowBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_DAILY_HOROSCOPE_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    CUtils.createSession(activity, CGlobalVariables.DAILY_HOROSCOPE_CHAT_PARTNER_ID);
                    //com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CHAT, activity);
                    com.ojassoft.astrosage.varta.utils.CUtils.switchToConsultTab(FILTER_TYPE_CALL, activity);//redirect to ai list
                } catch (Exception e) {
                    //
                }
            }
        });
    }

}
