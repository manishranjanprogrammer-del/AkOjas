package com.ojassoft.astrosage.ui.cards;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.BeanNameValueCardListData;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.misc.CalculateChogadiya;
import com.ojassoft.astrosage.misc.CalculateDoGhatiMuhurat;
import com.ojassoft.astrosage.misc.CustomAdapterPanchang;
import com.ojassoft.astrosage.misc.ExpandableHeightListView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ojas on 10/2/17.
 */

public class Panchang extends ParentView {

    private Context context;
    // private TextView tvHeader,textView1;
    // private ExpandableHeightListView rvListData;
    private AajKaPanchangModel model;
    private String currentLalitude, currentLongitude, locality, timeZone, timeZoneString;
    private long currentTimeInMillis;
    private int language_code;
    private Typeface typeface;
    String panchangStr;
    String sunCalculation;
    String hinduMonth;
    String subhAndAshubh;
    String horaStr;
    String chogdiyaStr;
    String currentMuhurtStr;
    String rahukaalStr;
    String dailyAuspiciousMuhurat = "";
    int position;

    public Panchang(Context context, int cardPosition, AajKaPanchangModel model, String currentLalitude, String currentLongitude , String timeZone, String timeZoneString, long currentTimeInMillis, int language_code) {
        super(context);
        this.context = context;
        this.model = model;
        this.currentLalitude = currentLalitude;
        this.currentLongitude = currentLongitude;
        this.currentTimeInMillis = currentTimeInMillis;
        this.timeZone = timeZone;
        this.timeZoneString = timeZoneString;
        this.language_code = language_code;

        initData(cardPosition);
    }

    public Panchang(Context context, int cardPosition, AajKaPanchangModel model, String currentLalitude, String currentLongitude, String locality, String timeZone, String timeZoneString, long currentTimeInMillis, int language_code, Typeface typeface, int position) {
        super(context);
        this.context = context;
        this.model = model;
        this.currentLalitude = currentLalitude;
        this.currentLongitude = currentLongitude;
        this.locality = locality;
        this.currentTimeInMillis = currentTimeInMillis;
        this.timeZone = timeZone;
        this.timeZoneString = timeZoneString;
        this.language_code = language_code;
        this.typeface = typeface;
        this.position = position;

        initData(cardPosition);
    }

    private void initData(int module) {
        switch (module) {
            case CGlobalVariables.dailyPanchang:
                initPanchang();
                break;
            case CGlobalVariables.sunAndMoonCalculation:
                initSunMoonCalculation();
                break;
            case CGlobalVariables.hinduMonthAndYear:
                initHinduMonthAndYear();
                break;
            case CGlobalVariables.auspiciousInauspiciousTimings:
                initViewAuspiciousInauspiciousTimings();
                break;
            case CGlobalVariables.horaTable:
                initHoraTable();
                break;
            case CGlobalVariables.chogadhiya:
                initChogadhiya();
                break;
            case CGlobalVariables.doGhati:
                initDoGhati();
                break;
            case CGlobalVariables.rahuKaal:
                initRahuKaal();
                break;
            case CGlobalVariables.abhijit:
                initAbhijit();
                break;
            case CGlobalVariables.daily_auspicious_muhurat:
                dailyAuspiciousMuhurat();
                break;
            default:
                break;
        }
    }

    @Override
    public void onChangeFromSettings(int pos) {

    }

    private void dailyAuspiciousMuhurat() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeInMillis);

        int day_of_month = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        List<HoraMetadata> data = HoraLordShortName(day_of_month);

        List<HoraMetadata> horaEntryTime = HoraEntryTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone);

        List<HoraMetadata> horaExitTime = HoraExitTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone);

        String name[] = new String[7];
        String startTime[] = new String[7];
        String endTime[] = new String[7];
        HashMap<String, Integer> map = new HashMap<>();

        LinkedHashMap<String, String> speak = new LinkedHashMap<>();

        for (int i = 0; i < data.size(); i++) {

            String horaMeaning = data.get(i).getPlanetmeaning();
            if (map.containsKey(horaMeaning)) {
                String start = CUtils.removeSecond(startTime[map.get(horaMeaning)]);
                String end = CUtils.removeSecond(endTime[map.get(horaMeaning)]);

                start = start + "\n" + horaEntryTime.get(i).getEntertimedata();
                end = end + "\n" + horaExitTime.get(i).getExittimedata();

                startTime[map.get(horaMeaning)] = start;
                endTime[map.get(horaMeaning)] = end;

                String result = speak.get(horaMeaning);
                result = result + CUtils.removeSecond(horaEntryTime.get(i).getEntertimedata()) + " " + context.getResources().getString(R.string.str_to) + " " + CUtils.removeSecond(horaExitTime.get(i).getExittimedata()) + "\n";
                speak.put(horaMeaning, result);

            } else {
                map.put(horaMeaning, i);
                name[i] = horaMeaning;
                startTime[i] = horaEntryTime.get(i).getEntertimedata();
                endTime[i] = horaExitTime.get(i).getExittimedata();
                speak.put(horaMeaning, CUtils.removeSecond(startTime[i]) + " " + context.getResources().getString(R.string.str_to) + " " + CUtils.removeSecond(endTime[i]) + "\n");
            }
        }

        for (Map.Entry<String, String> entry : speak.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String[] timeArray = value.split("\n");
            if (timeArray.length == 4) {
                value = timeArray[1] + "\n" + timeArray[2];
            } else if (timeArray.length == 3) {
                value = timeArray[0] + "\n" + timeArray[1];
            }
            dailyAuspiciousMuhurat = dailyAuspiciousMuhurat + key + ": " + "\n" + value + "\n";
        }

        try {
            View view = LayoutInflater.from(context).inflate(R.layout.layout2, null);

            TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);
*/
            View view1 = view.findViewById(R.id.layTitles);
            view1.setVisibility(View.VISIBLE);

            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvValue = (TextView) view.findViewById(R.id.tvValue);
            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);

            tvName.setText(context.getResources().getString(R.string.task));
            tvValue.setText(context.getResources().getString(R.string.start_time));
            tvTime.setText(context.getResources().getString(R.string.end_times));

            /*imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.today_daily_auspicious_muhurat), dailyAuspiciousMuhurat);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.today_daily_auspicious_muhurat), dailyAuspiciousMuhurat);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*Toast.makeText(context, "position--"+position, Toast.LENGTH_SHORT).show();
                    //((MainActivity) context).speakAnswer(sunCalculation);*//*
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(dailyAuspiciousMuhurat);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvHeader.setTypeface(typeface);
            textView1.setTypeface(typeface);

            tvHeader.setText(getResources().getString(R.string.today_daily_auspicious_muhurat));
//            textView1.setText("- " + locality);

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);


            List<BeanNameValueCardListData> data1 = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                String[] startTimeArray = CUtils.removeSecond(startTime[i]).split("\n");
                String[] endTimeArray = CUtils.removeSecond(endTime[i]).split("\n");
                String startTimeStr = "";
                String endTimeStr = "";
                if (startTimeArray.length == 4) {
                    startTimeStr = startTimeArray[1] + "\n" + startTimeArray[2];
                    endTimeStr = endTimeArray[1] + "\n" + endTimeArray[2];
                } else if (startTimeArray.length == 3) {
                    startTimeStr = startTimeArray[0] + "\n" + startTimeArray[1];
                    endTimeStr = endTimeArray[0] + "\n" + endTimeArray[1];
                }
                listData.setName(name[i]);
                listData.setValue(startTimeStr);
                listData.setTime(endTimeStr);
                data1.add(listData);
            }

            rvListData.setAdapter(new CustomAdapterPanchang(context, data1, typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(10f);
            }
            addView(view);

        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing Panchang Data
     */
    private void initPanchang() {

        try {

            View view = LayoutInflater.from(context).inflate(R.layout.layout2, null);

            TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);

            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.panchang_for_today), panchangStr.replaceAll("\n", ""));

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.panchang_for_today), panchangStr.replaceAll("\n", ""));

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   *//* Toast.makeText(context, "position--"+position, Toast.LENGTH_SHORT).show();
                    // ((MainActivity) context).speakAnswer(panchangStr);*//*
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(panchangStr);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvHeader.setTypeface(typeface);
            textView1.setTypeface(typeface);

            tvHeader.setText(getResources().getString(R.string.panchang_for_today));
//            textView1.setText("- " + locality);

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);

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
            String[] karanTimes = model.getKaranaTime().split(",");
            String karanTime = "";
            String[] time = null;

            if (language_code == CGlobalVariables.HINDI) {
                karanTime = CUtils.removeSecond(karanTimes[0]) + " " + context.getResources().getString(R.string.till);
                if (karanTimes.length == 2) {
                    karanTime = karanTime + "," + CUtils.removeSecond(karanTimes[1]) + " " + context.getResources().getString(R.string.till);
                }

                time = new String[]{CUtils.removeSecond(model.getTithiTime()) + " " + context.getResources().getString(R.string.till),
                        CUtils.removeSecond(model.getNakshatraTime()) + " " + context.getResources().getString(R.string.till),
                        karanTime,
                        "",
                        CUtils.removeSecond(model.getYogaTime()) + " " + context.getResources().getString(R.string.till),
                        ""};
            } else {
                karanTime = context.getResources().getString(R.string.till) + " " + CUtils.removeSecond(karanTimes[0]);
                if (karanTimes.length == 2) {

                    String karanSecond = karanTimes[1].replace("\n", "");

                    karanTime = karanTime + "," + "\n" + context.getResources().getString(R.string.till) + " " + CUtils.removeSecond(karanSecond);
                }

                time = new String[]{context.getResources().getString(R.string.till) + " " + CUtils.removeSecond(model.getTithiTime()),
                        context.getResources().getString(R.string.till) + " " + CUtils.removeSecond(model.getNakshatraTime()),
                        karanTime,
                        "",
                        context.getResources().getString(R.string.till) + " " + CUtils.removeSecond(model.getYogaTime()),
                        ""};
            }

            if (language_code == CGlobalVariables.HINDI) {
                panchangStr = "आज " + name[0] + " " + value[0] + " " + time[0] + " तक, "
                        + name[1] + " " + value[1] + " " + time[1] + " तक, "
                        + name[2] + " " + value[2] + " " + time[2] + " तक, "
                        + name[3] + " " + value[3] + ", "
                        + name[4] + " " + value[4] + " " + time[4] + " तक, "
                        + name[5] + " " + value[5];
            } else {
                panchangStr = "Today " + name[0] + " is " + value[0] + " till " + time[0] + ", "
                        + name[1] + " is " + value[1] + " till " + time[1] + ", "
                        + name[2] + " is " + value[2] + " till " + karanTime + ", "
                        + name[3] + " is " + value[3] + ", "
                        + name[4] + " is " + value[4] + " till " + time[4] + ", "
                        + name[5] + " is " + value[5];
            }


            List<BeanNameValueCardListData> data = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);
                listData.setTime(time[i]);

                data.add(listData);
            }

            rvListData.setAdapter(new CustomAdapterPanchang(context, data, typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing Sun Moon Calculation
     */
    private void initSunMoonCalculation() {

        try {
            View view = LayoutInflater.from(context).inflate(R.layout.layout2, null);

            TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);

            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.sun_and_moon_calculation), sunCalculation);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.sun_and_moon_calculation), sunCalculation);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*Toast.makeText(context, "position--"+position, Toast.LENGTH_SHORT).show();
                    //((MainActivity) context).speakAnswer(sunCalculation);*//*
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(sunCalculation);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvHeader.setTypeface(typeface);
            textView1.setTypeface(typeface);

            tvHeader.setText(getResources().getString(R.string.sun_and_moon_calculation));
//            textView1.setText("- " + locality);

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);

            String name[] = new String[]{getResources().getString(R.string.sun_rises),
                    getResources().getString(R.string.moon_rises),
                    getResources().getString(R.string.moon_sign),
                    getResources().getString(R.string.sun_set),
                    getResources().getString(R.string.moon_set),
                    getResources().getString(R.string.ritu)};

            String value[] = new String[]{CUtils.removeSecond(model.getSunRise()),
                    CUtils.removeSecond(model.getMoonRise()),
                    model.getMoonSignValue(),
                    CUtils.removeSecond(model.getSunSet()),
                    CUtils.removeSecond(model.getMoonSet()),
                    model.getRitu()};

            String time[] = new String[]{"",
                    "",
                    CUtils.removeSecond(model.getMoonSignTime()),
                    "",
                    "",
                    ""};
            if (language_code == CGlobalVariables.HINDI) {
                sunCalculation = "आज " + name[0] + " " + value[0] + " पर, "
                        + name[1] + " " + value[1] + " पर, "
                        + name[3] + " " + value[3] + " पर, "
                        + name[4] + " " + value[4] + " पर,"
                        + name[2] + " " + value[2] + " है, "
                        + value[5] + " " + name[5] + "चल रही है";
            } else {
                sunCalculation = "Today " + name[0] + " at " + value[0] + ", "
                        + name[1] + " at " + value[1] + ", "
                        + name[3] + " at " + value[3] + ", "
                        + name[4] + " at " + value[4] + ","
                        + name[2] + " is " + value[2] + ", "
                        + value[5] + " " + name[5] + " is going on";
            }


            List<BeanNameValueCardListData> data = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);
                listData.setTime(time[i]);
                data.add(listData);
            }

            rvListData.setAdapter(new CustomAdapterPanchang(context, data, typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing hindu Month And Year
     */
    private void initHinduMonthAndYear() {

        try {
            View view = LayoutInflater.from(context).inflate(R.layout.layout2, null);

            TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            textView1.setVisibility(View.GONE);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);


            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);

            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.hindu_month_and_year), hinduMonth);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.hindu_month_and_year), hinduMonth);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*((MainActivity) context).speakAnswer(hinduMonth);*//*
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(hinduMonth);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvHeader.setTypeface(typeface);
            textView1.setTypeface(typeface);

            tvHeader.setText(getResources().getString(R.string.hindu_month_and_year));
            //textView1.setText("- "+locality);

            ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);

            String name[] = new String[]{getResources().getString(R.string.shaka_samvat),
                    getResources().getString(R.string.kali_samvat),
                    getResources().getString(R.string.day_duration),
                    getResources().getString(R.string.vikram_samvat),
                    getResources().getString(R.string.month_amanta),
                    getResources().getString(R.string.month_purnimanta)};

            String value[] = new String[]{model.getShakaSamvatYear(),
                    model.getKaliSamvat(),
                    CUtils.removeSecond(model.getDayDuration()),
                    model.getVikramSamvat(),
                    model.getMonthAmanta(),
                    model.getMonthPurnimanta()};

            String time[] = new String[]{model.getShakaSamvatName(),
                    "",
                    "",
                    "",
                    "",
                    ""};
            hinduMonth = name[0] + " - " + value[0] + " " + time[0] + ",\n"
                    + name[1] + " - " + value[1] + ",\n"
                    + name[2] + " - " + value[2] + ",\n"
                    + name[3] + " - " + value[3] + ",\n"
                    + name[4] + " - " + value[4] + ",\n"
                    + name[5] + " - " + value[5];
            List<BeanNameValueCardListData> data = new ArrayList<BeanNameValueCardListData>();

            for (int i = 0; i < name.length; i++) {

                BeanNameValueCardListData listData = new BeanNameValueCardListData();
                listData.setName(name[i]);
                listData.setValue(value[i]);
                listData.setTime(time[i]);
                data.add(listData);
            }

            rvListData.setAdapter(new CustomAdapterPanchang(context, data, typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing Auspicious Inauspicious Timings
     */
    private void initViewAuspiciousInauspiciousTimings() {

        try {
            View view = LayoutInflater.from(context).inflate(R.layout.layout4, null);

            TextView header = (TextView) view.findViewById(R.id.tvHeader);
            TextView tvSubHeader1 = (TextView) view.findViewById(R.id.tvSubHeader1);
            TextView tvSubHeader2 = (TextView) view.findViewById(R.id.tvSubHeader2);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);


            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.auspicious_and_inauspicious), subhAndAshubh);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.auspicious_and_inauspicious), subhAndAshubh);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //((MainActivity) context).speakAnswer(subhAndAshubh);
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(subhAndAshubh);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            header.setTypeface(typeface);
            tvSubHeader1.setTypeface(typeface);
            tvSubHeader2.setTypeface(typeface);
            textView1.setTypeface(typeface);

//            textView1.setText("- " + locality);

            header.setText(getResources().getString(R.string.auspicious_and_inauspicious));
            tvSubHeader1.setText(getResources().getString(R.string.auspicious_timings));
            tvSubHeader2.setText(getResources().getString(R.string.inauspicious_timings));

            String name1[] = new String[]{getResources().getString(R.string.abhijit)};

            String value1[] = new String[]{CUtils.removeSecond(model.getAbhijitFrom())};

            String time1[] = new String[]{CUtils.removeSecond(model.getAbhijitTo())};

            String name2[] = new String[]{getResources().getString(R.string.dushta_muhurtas),
                    getResources().getString(R.string.kantaka),
                    getResources().getString(R.string.yamaghanta),
                    getResources().getString(R.string.rahu_kaal),
                    getResources().getString(R.string.kulika),
                    getResources().getString(R.string.kalavela),
                    getResources().getString(R.string.yamaganda),
                    getResources().getString(R.string.gulika_kaal)};

            String Dmu = model.getDushtaMuhurtasFrom();
            String value2[] = new String[]{
                    CUtils.removeSecond(model.getDushtaMuhurtasFrom().replace("\n", ",\n")),
                    CUtils.removeSecond(model.getKantaka_MrityuFrom()),
                    CUtils.removeSecond(model.getYamaghantaFrom()),
                    CUtils.removeSecond(model.getRahuKaalVelaFrom()),
                    CUtils.removeSecond(model.getKulikaFrom()),
                    CUtils.removeSecond(model.getKalavela_ArdhayaamFrom()),
                    CUtils.removeSecond(model.getYamagandaVelaFrom()),
                    CUtils.removeSecond(model.getGulikaKaalVelaFrom())};

            String time2[] = new String[]{
                    CUtils.removeSecond(model.getDushtaMuhurtasTo()),
                    CUtils.removeSecond(model.getKantaka_MrityuTo()),
                    CUtils.removeSecond(model.getYamaghantaTo()),
                    CUtils.removeSecond(model.getRahuKaalVelaTo()),
                    CUtils.removeSecond(model.getKulikaTo()),
                    CUtils.removeSecond(model.getKalavela_ArdhayaamTo()),
                    CUtils.removeSecond(model.getYamagandaVelaTo()),
                    CUtils.removeSecond(model.getGulikaKaalVelaTo())};
            if (language_code == CGlobalVariables.HINDI) {

                subhAndAshubh = "शुभ समय \n" + name1[0] + " - " + value1[0] + " से " + time1[0] + "तक \n" +
                        "अशुभ समय \n" + name2[0] + " - " + value2[0] + " से " + time2[0] + "तक\n"
                        + name2[1] + " - " + value2[1] + " से " + time2[1] + "तक\n"
                        + name2[2] + " - " + value2[2] + " से " + time2[2] + "तक\n"
                        + name2[3] + " - " + value2[3] + " से " + time2[3] + "तक\n"
                        + name2[4] + " - " + value2[4] + " से " + time2[4] + "तक\n"
                        + name2[5] + " - " + value2[5] + " से " + time2[5] + "तक\n"
                        + name2[6] + " - " + value2[6] + " से " + time2[6] + "तक\n"
                        + name2[7] + " - " + value2[7] + " से " + time2[7] + "तक";
            } else {
                subhAndAshubh = "Auspicious time is \n" + name1[0] + " starts from " + value1[0] + " to " + time1[0] + " \n" +
                        "Inauspicious time \n" + name2[0] + " starts from " + value2[0] + " to " + time2[0] + "\n"
                        + name2[1] + " starts from " + value2[1] + " to " + time2[1] + "\n"
                        + name2[2] + " starts from " + value2[2] + " to " + time2[2] + "\n"
                        + name2[3] + " starts from " + value2[3] + " to " + time2[3] + "\n"
                        + name2[4] + " starts from " + value2[4] + " to " + time2[4] + "\n"
                        + name2[5] + " starts from " + value2[5] + " to " + time2[5] + "\n"
                        + name2[6] + " starts from " + value2[6] + " to " + time2[6] + "\n"
                        + name2[7] + " starts from " + value2[7] + " to " + time2[7];
            }


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

            ExpandableHeightListView rvListData1 = (ExpandableHeightListView) view.findViewById(R.id.rvListData1);
            rvListData1.setAdapter(new CustomAdapterPanchang(context, data1, typeface));
            rvListData1.setExpanded(true);
            rvListData1.setFocusable(false);

            ExpandableHeightListView rvListData2 = (ExpandableHeightListView) view.findViewById(R.id.rvListData2);
            rvListData2.setAdapter(new CustomAdapterPanchang(context, data2, typeface));
            rvListData2.setExpanded(true);
            rvListData2.setFocusable(false);

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing Hora Table
     */
    private void initHoraTable() {
        try {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTimeInMillis);

            View view = LayoutInflater.from(context).inflate(R.layout.layout5, null);
            //View layTitles = view.findViewById(R.id.layTitles);
            // layTitles.setVisibility(View.VISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tvText1 = (TextView) view.findViewById(R.id.tvText1);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView tvText2 = (TextView) view.findViewById(R.id.tvText2);
            TextView tvText3 = (TextView) view.findViewById(R.id.tvText3);
            TextView tvText4 = (TextView) view.findViewById(R.id.tvText4);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);


            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.current_hora), horaStr);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.current_hora), horaStr);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((MainActivity) context).speakAnswer(horaStr);
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(horaStr);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvText1.setTypeface(typeface);
            tvText2.setTypeface(typeface);
            tvText3.setTypeface(typeface);
            tvText4.setTypeface(typeface);
            textView1.setTypeface(typeface);

//            textView1.setText("- " + locality);

            int day_of_month = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            List<HoraMetadata> data = HoraLordName(day_of_month);

            List<HoraMetadata> horaEntryTime = HoraEntryTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone);

            List<HoraMetadata> horaExitTime = HoraExitTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone);

           /* ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);

            rvListData.setAdapter(new CustomAdapterforHora(context, data, horaEntryTime,horaExitTime, Horatag,typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);*/

            int number = getCurrentHoraNumber(horaEntryTime, horaExitTime);

            tvText1.setText(context.getResources().getString(R.string.current_hora));
            tvText2.setText(context.getResources().getString(R.string.hora_name_text) + context.getResources().getString(R.string.colon_horo) + " " + data.get(number).getPlanetdata());
            /*tvText3.setText(CUtils.removeSecond(horaEntryTime.get(number).getEntertimedata()) + "-"
                    + CUtils.removeSecond(horaExitTime.get(number).getExittimedata()));*/
            tvText4.setText(data.get(number).getPlanetCurrentHorameaning());
            if (language_code == CGlobalVariables.HINDI) {

                tvText3.setText(CUtils.removeSecond(horaEntryTime.get(number).getEntertimedata()) + " से "
                        + CUtils.removeSecond(horaExitTime.get(number).getExittimedata()) + " तक");

                horaStr = "वर्तमान होरा " + data.get(number).getPlanetdata() + " का है जो " + data.get(number).getPlanetCurrentHorameaning() + " है "
                        + CUtils.removeSecond(horaEntryTime.get(number).getEntertimedata()) + " से " + CUtils.removeSecond(horaExitTime.get(number).getExittimedata()) + " तक चलेगा";

            } else {

                tvText3.setText("from " + CUtils.removeSecond(horaEntryTime.get(number).getEntertimedata())
                        + " to " + CUtils.removeSecond(horaExitTime.get(number).getExittimedata()));

                horaStr = "Current hora is " + data.get(number).getPlanetdata() + " which is " + data.get(number).getPlanetCurrentHorameaning() + " starts from "
                        + CUtils.removeSecond(horaEntryTime.get(number).getEntertimedata()) + " to " + CUtils.removeSecond(horaExitTime.get(number).getExittimedata());

            }
            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing Chogadhiya
     */
    private void initChogadhiya() {

        try {
            // String Horatag = "chogadhiyatag";

            View view = LayoutInflater.from(context).inflate(R.layout.layout5, null);
            //View layTitles = view.findViewById(R.id.layTitles);
            // layTitles.setVisibility(View.VISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tvText1 = (TextView) view.findViewById(R.id.tvText1);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView tvText2 = (TextView) view.findViewById(R.id.tvText2);
            TextView tvText3 = (TextView) view.findViewById(R.id.tvText3);
            TextView tvText4 = (TextView) view.findViewById(R.id.tvText4);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);


            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.current_chogadiya), chogdiyaStr);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.current_chogadiya), chogdiyaStr);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((MainActivity) context).speakAnswer(chogdiyaStr);
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(chogdiyaStr);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvText1.setTypeface(typeface);
            tvText2.setTypeface(typeface);
            tvText3.setTypeface(typeface);
            tvText4.setTypeface(typeface);
            textView1.setTypeface(typeface);

//            textView1.setText("- " + locality);


            String[] sunRiseArr = model.getSunRise().split(":");
            boolean boolVal = CUtils.isTimeBeforeSunRise(sunRiseArr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTimeInMillis);
            int day_of_month = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            if (boolVal) {
                if (day_of_month == 0) {
                    day_of_month = 6;
                } else {
                    day_of_month--;
                }
                day--;
            }
            if (day == 0) {
                if (month == 0 || month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10) {
                    day = 31;
                    if (month == 0) {
                        year--;
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    day = 30;
                } else if (month == 2) {
                    if (year % 4 == 0) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                }
                if (month == 0) {
                    month = 11;
                } else {
                    month--;
                }
            }

            CalculateChogadiya calculateChogadiya = new CalculateChogadiya(context);
            List<HoraMetadata> data = calculateChogadiya.HoraLordName(year, month, day);
            List<HoraMetadata> datatime = calculateChogadiya.HoraEntryTime(year, month, day, currentLalitude, currentLongitude, timeZone);
            List<HoraMetadata> datatimeexit = calculateChogadiya.HoraExitTime(year, month, day, currentLalitude, currentLongitude, timeZone);


            int number = getCurrentHoraNumber(datatime, datatimeexit);

            tvText1.setText(context.getResources().getString(R.string.current_chogadiya));
            tvText2.setText(context.getResources().getString(R.string.chogadia_name_text) + context.getResources().getString(R.string.colon_horo) + " " + data.get(number).getPlanetdata());

            tvText4.setText(context.getResources().getString(R.string.chogadia_nature) + context.getResources().getString(R.string.colon_horo) + " " + data.get(number).getPlanetmeaning());
            if (language_code == CGlobalVariables.HINDI) {

                tvText3.setText(CUtils.removeSecond(datatime.get(number).getEntertimedata()) + " से "
                        + CUtils.removeSecond(datatimeexit.get(number).getExittimedata()) + " तक ");

                chogdiyaStr = "वर्तमान चौघड़िया " + data.get(number).getPlanetdata() + " है जो " + data.get(number).getPlanetmeaning() +
                        " है " + CUtils.removeSecond(datatime.get(number).getEntertimedata()) + " से " + CUtils.removeSecond(datatimeexit.get(number).getExittimedata()) + " तक चलेगा ";
            } else {

                tvText3.setText("from " + CUtils.removeSecond(datatime.get(number).getEntertimedata())
                        + " to " + CUtils.removeSecond(datatimeexit.get(number).getExittimedata()));

                chogdiyaStr = "Current chogadia is " + data.get(number).getPlanetdata() + " which is " + data.get(number).getPlanetmeaning() +
                        " starts from " + CUtils.removeSecond(datatime.get(number).getEntertimedata()) + " to " + CUtils.removeSecond(datatimeexit.get(number).getExittimedata());
            }

            /*ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);

            rvListData.setAdapter(new CustomAdapterforHora(context, data, datatime, datatimeexit, Horatag,typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);*/

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    private List<HoraMetadata> HoraLordShortName(int day_of_month) {

        List<HoraMetadata> data = new ArrayList<HoraMetadata>();
        int dayLordForDayHora[] = new int[24];

        String PlanetName[] = getResources().getStringArray(
                R.array.hora_planets);
        String PlanetNameMeaning[] = getResources().getStringArray(
                R.array.hora_planets_short_meaning);
        String PlanetNameMeaningforcurrentHora[] = getResources()
                .getStringArray(R.array.pla_mean);

        for (int i = 0; i < 24; i++) {
            dayLordForDayHora[i] = (day_of_month + (i * 5)) % 7;
            HoraMetadata hora = new HoraMetadata();
            hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
            hora.setPlanetmeaning(PlanetNameMeaning[dayLordForDayHora[i]]);
            hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[dayLordForDayHora[i]]);
            data.add(hora);
        }

        return data;
    }

    private List<HoraMetadata> HoraLordName(int day_of_month) {

        List<HoraMetadata> data = new ArrayList<HoraMetadata>();
        int dayLordForDayHora[] = new int[24];

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
            data.add(hora);
        }

        return data;
    }

    private List<HoraMetadata> HoraEntryTime(int year, int month, int day, String latitude, String longitude, String timezone) {

        List<HoraMetadata> horaEntryTime = new ArrayList<HoraMetadata>();

        int jd = (int) Masa.toJulian(year, month + 1, day);
        double lat = 0.0;
        double lng = 0.0;
        double tzone = 0.0;

        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (Exception ex) {
            Log.e("Exception ", ex.getMessage().toString());
        }

        Place place = new Place(lat, lng, tzone);

        for (int i = 0; i < 13; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i <= 11) {

                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 12)[i], 0));

                Muhurta.getDayDivisons(jd, place, Masa.getSunRise(jd, place), 8);

                horaEntryTime.add(hora);

            }
        }

        for (int j = 0; j < 13; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 11) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 12)[j], 0));

                horaEntryTime.add(hora1);

            }
        }

        return horaEntryTime;
    }

    private List<HoraMetadata> HoraExitTime(int year, int month, int day, String latitude, String longitude, String timezone) {

        List<HoraMetadata> horaExitTime = new ArrayList<HoraMetadata>();

        int jd = (int) Masa.toJulian(year, month + 1, day);
        double lat = 0.0;
        double lng = 0.0;
        double tzone = 0.0;

        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (Exception ex) {
            Log.e("Exception ", ex.getMessage().toString());
        }

        Place place = new Place(lat, lng, tzone);

        for (int i = 0; i < 13; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i > 0) {
                hora.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 12)[i], 0));
                horaExitTime.add(hora);

            }
        }

        for (int j = 0; j < 13; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j > 0) {
                hora1.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 12)[j], 0));
                horaExitTime.add(hora1);

            }
        }

        return horaExitTime;
    }

    /**
     * Initializing DoGhati
     */
    private void initDoGhati() {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTimeInMillis);

            View view = LayoutInflater.from(context).inflate(R.layout.layout5, null);
            //View layTitles = view.findViewById(R.id.layTitles);
            // layTitles.setVisibility(View.VISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tvText1 = (TextView) view.findViewById(R.id.tvText1);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView tvText2 = (TextView) view.findViewById(R.id.tvText2);
            TextView tvText3 = (TextView) view.findViewById(R.id.tvText3);
            TextView tvText4 = (TextView) view.findViewById(R.id.tvText4);

            tvText4.setVisibility(View.GONE);
            /*ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);


            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.current_doghati), currentMuhurtStr);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.current_doghati), currentMuhurtStr);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //((MainActivity) context).speakAnswer(currentMuhurtStr);
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(currentMuhurtStr);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvText1.setTypeface(typeface);
            tvText2.setTypeface(typeface);
            tvText3.setTypeface(typeface);
            tvText4.setTypeface(typeface);
            textView1.setTypeface(typeface);

//            textView1.setText("- " + locality);

            int day_of_month = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            // //

            CalculateDoGhatiMuhurat calculateDoGhatiMuhurat = new CalculateDoGhatiMuhurat(context);
            List<HoraMetadata> data = calculateDoGhatiMuhurat.HoraLordName(day_of_month);
            List<HoraMetadata> datatime = calculateDoGhatiMuhurat.HoraEntryTime(calendar, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone, timeZoneString);
            List<HoraMetadata> datatimeexit = calculateDoGhatiMuhurat.HoraExitTime(calendar, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone, timeZoneString);


            int number = getCurrentHoraNumber(datatime, datatimeexit);

            tvText1.setText(context.getResources().getString(R.string.current_doghati));
            tvText2.setText(data.get(number)
                    .getPlanetmeaning()
                    + "("
                    + data.get(number).getPlanetdata() + ")");
            tvText3.setText(CUtils.removeSecond(datatime.get(number).getEntertimedata()) + "-"
                    + CUtils.removeSecond(datatimeexit.get(number).getExittimedata()));
            if (language_code == CGlobalVariables.HINDI) {
                currentMuhurtStr = "वर्तमान मुहूर्त " + data.get(number).getPlanetmeaning()
                        + "(" + data.get(number).getPlanetdata() + ")" + " जो " + CUtils.removeSecond(datatime.get(number).getEntertimedata())
                        + " से " + CUtils.removeSecond(datatimeexit.get(number).getExittimedata()) + " तक है ";
            } else {
                currentMuhurtStr = "Current muhurat is" + data.get(number).getPlanetmeaning()
                        + "(" + data.get(number).getPlanetdata() + ")" + " which starts from " + CUtils.removeSecond(datatime.get(number).getEntertimedata())
                        + " to " + CUtils.removeSecond(datatimeexit.get(number).getExittimedata());
            }

           /* ExpandableHeightListView rvListData = (ExpandableHeightListView) view.findViewById(R.id.rvListData);

            rvListData.setAdapter(new CustomAdapterforHora(context, data, datatime, datatimeexit, Horatag,typeface));
            rvListData.setExpanded(true);
            rvListData.setFocusable(false);*/

            addView(view);

        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    /**
     * Initializing RahuKaal
     */
    private void initRahuKaal() {

        try {

            View view = LayoutInflater.from(context).inflate(R.layout.layout5, null);
            //View layTitles = view.findViewById(R.id.layTitles);
            // layTitles.setVisibility(View.VISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tvText1 = (TextView) view.findViewById(R.id.tvText1);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView tvText2 = (TextView) view.findViewById(R.id.tvText2);
            TextView tvText3 = (TextView) view.findViewById(R.id.tvText3);
            TextView tvText4 = (TextView) view.findViewById(R.id.tvText4);
            tvText3.setVisibility(View.GONE);
            tvText4.setVisibility(View.GONE);
           /* ImageView imagePlay = (ImageView) view.findViewById(R.id.imagePlay);
            ImageView imageCopy = (ImageView) view.findViewById(R.id.imageCopy);
            ImageView imageShare = (ImageView) view.findViewById(R.id.imageShare);

            LinearLayout imagePlayLayout = (LinearLayout) view.findViewById(R.id.imagePlayLayout);
            LinearLayout imageCopyLayout = (LinearLayout) view.findViewById(R.id.imageCopyLayout);
            LinearLayout imageShareLayout = (LinearLayout) view.findViewById(R.id.imageShareLayout);


            imageShareLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.currentrahukaal), rahukaalStr);

                }
            });
            imageCopyLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).textToShare(v, getResources().getString(R.string.currentrahukaal), rahukaalStr);

                }
            });
            imagePlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //((MainActivity) context).speakAnswer(rahukaalStr);
                    if (!CUtils.getBooleanData(context, CGlobalVariables.key_IsMute, false)) {
                        RecyclerCardViewAdapter.currentSpeakingPosition = position;
                        if (!((MainActivity) context).isSpeaking()) {
                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            } else {

                                ((MainActivity) context).speakAnswer(rahukaalStr);
                                imagePlay.setImageResource(R.mipmap.baseline_stop_black_24);
                                RecyclerCardViewAdapter.lastSpeakingPosition = RecyclerCardViewAdapter.currentSpeakingPosition;

                            }
                        } else {

                            if (RecyclerCardViewAdapter.currentSpeakingPosition == RecyclerCardViewAdapter.lastSpeakingPosition) {
                                imagePlay.setImageResource(R.mipmap.baseline_play_arrow_black_24);
                                ((MainActivity) context).stopSpeak();
                                SpeechRecognizerManager.getInstance().needToStopAudioSound = true;
                                SpeechRecognizerManager.getInstance().stopAudioListener();
                                RecyclerCardViewAdapter.lastSpeakingPosition = -1;
                            }

                        }
                    } else {
                        ((MainActivity) context).showSnakbar(getResources().getString(R.string.unmute_toast), "", Snackbar.LENGTH_LONG);
                    }
                }
            });*/
            tvText1.setTypeface(typeface);
            tvText2.setTypeface(typeface);
            tvText3.setTypeface(typeface);
            tvText4.setTypeface(typeface);
            textView1.setTypeface(typeface);

//            textView1.setText("- " + locality);

            tvText1.setText(context.getResources().getString(R.string.currentrahukaal));

            if (language_code == CGlobalVariables.HINDI) {
                tvText2.setText(CUtils.removeSecond(model.getRahuKaalVelaFrom()) + " से " + CUtils.removeSecond(model.getRahuKaalVelaTo()) + " तक");
                rahukaalStr = "आज का राहुकाल " + CUtils.removeSecond(model.getRahuKaalVelaFrom()) + " से " + CUtils.removeSecond(model.getRahuKaalVelaTo()) + " तक है";
            } else {
                tvText2.setText("from " + CUtils.removeSecond(model.getRahuKaalVelaFrom()) + " to " + CUtils.removeSecond(model.getRahuKaalVelaTo()));
                rahukaalStr = "Today's rahukaal starts from" + CUtils.removeSecond(model.getRahuKaalVelaFrom()) + " to " + CUtils.removeSecond(model.getRahuKaalVelaTo());
            }

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }

    private Date getNextDay(int i, Calendar calendar) {
        Date date = calendar.getTime();
        Calendar calendarNew = Calendar.getInstance();
        calendarNew.setTime(date);
        calendarNew.setTime(date);
        calendarNew.add(Calendar.DATE, i);
        date = calendarNew.getTime();
        return date;
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

            for (int i = 0; i <= datatime2.size(); i++) {
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
            Log.e("", e.getMessage().toString());
            System.out.println(e.getMessage().toString());

        }

        return currentHoraNumber;
    }

    private void initAbhijit() {

        try {

            View view = LayoutInflater.from(context).inflate(R.layout.layout5, null);
            //View layTitles = view.findViewById(R.id.layTitles);
            // layTitles.setVisibility(View.VISIBLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView tvText1 = (TextView) view.findViewById(R.id.tvText1);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView tvText2 = (TextView) view.findViewById(R.id.tvText2);
            TextView tvText3 = (TextView) view.findViewById(R.id.tvText3);
            TextView tvText4 = (TextView) view.findViewById(R.id.tvText4);
            tvText3.setVisibility(View.GONE);
            tvText4.setVisibility(View.GONE);

            tvText1.setTypeface(typeface);
            tvText2.setTypeface(typeface);
            tvText3.setTypeface(typeface);
            tvText4.setTypeface(typeface);
            textView1.setTypeface(typeface);

//            textView1.setText("- " + locality);

            tvText1.setText(context.getResources().getString(R.string.abhijit));
            tvText2.setText(model.getAbhijit());

            addView(view);
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage().toString());
        }
    }
}
