package com.ojassoft.astrosage.varta.aichat;

import static com.ojassoft.astrosage.utils.CUtils.HoraEntryTime;
import static com.ojassoft.astrosage.utils.CUtils.HoraExitTime;
import static com.ojassoft.astrosage.utils.CUtils.HoraLordName;
import static com.ojassoft.astrosage.utils.CUtils.SetdataonList;
import static com.ojassoft.astrosage.utils.CUtils.getCurrentHoraNumber;
import static com.ojassoft.astrosage.utils.CUtils.isTimeBeforeSunRise;
import static com.ojassoft.astrosage.utils.CUtils.removeSecond;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.checkDate;
import static com.ojassoft.astrosage.varta.utils.CUtils.getCurrentDate;
import static com.ojassoft.astrosage.varta.utils.CUtils.getDateFromString;
import static com.ojassoft.astrosage.varta.utils.CUtils.removeZero;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanHoroscopeRemedies;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.CalculateChogadiya;
import com.ojassoft.astrosage.misc.CalculateDoGhatiMuhurat;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.HttpUtility;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocalAIAnswers {

    private final Context context;
    private static LocalAIAnswers instance;

    public AajKaPanchangModel model;
    public AajKaPanchangModel tomorrowModel;

    private String[] vastu;
    private String aboutHora;
    private List<HoraMetadata> data;
    private List<HoraMetadata> datatimestart;
    private List<HoraMetadata> datatimeexit;
    public String luckyNumber;

    public static LocalAIAnswers getInstance(Context context) {
        if (instance == null) {
            instance = new LocalAIAnswers(context);
        }
        return instance;
    }

    public LocalAIAnswers(Context context) {
        this.context = context;
    }

    public String searchAnswer(String question, String intent) {
        String answer = context.getResources().getString(R.string.answer_not_available);
        Log.d("appHangIssue", "intent: " + intent);

        /*if (intent.equals("daily_rashifal")) {
            answer = getAnswer(question, 1, 0);
        } else if (intent.equals("tomorrow_rashifal")) {
            answer = getAnswer(question, 1, 1);
        } else if (intent.equals("daily_panchang")) {
            answer = getAnswer(question, 2, 0);
        } else if (intent.equals("daily_panchang_tithi")) {
            answer = getAnswer(question, 2, 1);
        } else if (intent.equals("daily_panchang_nakshtra")) {
            answer = getAnswer(question, 2, 2);
        } else if (intent.equals("daily_panchang_karan")) {
            answer = getAnswer(question, 2, 3);
        } else if (intent.equals("daily_panchang_paksh")) {
            answer = getAnswer(question, 2, 4);
        } else if (intent.equals("daily_panchang_yog")) {
            answer = getAnswer(question, 2, 5);
        } else if (intent.equals("daily_panchang_vaar")) {
            answer = getAnswer(question, 2, 6);
        } else if (intent.equals("daily_panchang_disha_shoola")) {
            answer = getAnswer(question, 2, 7);
        } else if (intent.equals("daily_panchang_month")) {
            answer = getAnswer(question, 2, 8);
        } else if (intent.equals("tomorrow_panchang")) {
            answer = getAnswer(question, 2, 9);
        } else if (intent.equals("tomorrow_panchang_tithi")) {
            answer = getAnswer(question, 2, 10);
        } else if (intent.equals("tomorrow_panchang_nakshtra")) {
            answer = getAnswer(question, 2, 11);
        } else if (intent.equals("tomorrow_panchang_karan")) {
            answer = getAnswer(question, 2, 12);
        } else if (intent.equals("tomorrow_panchang_paksh")) {
            answer = getAnswer(question, 2, 13);
        } else if (intent.equals("tomorrow_panchang_yog")) {
            answer = getAnswer(question, 2, 14);
        } else if (intent.equals("tomorrow_panchang_vaar")) {
            answer = getAnswer(question, 2, 15);
        } else if (intent.equals("tomorrow_panchang_disha_shoola")) {
            answer = getAnswer(question, 2, 16);
        } else if (intent.equals("Current_Samvat")) {
            answer = getAnswer(question, 2, 17);
        } else *//*if (intent.equals("daily_panchang_sunrise")) {
            answer = getAnswer(question, 3, 1);
        } else if (intent.equals("daily_panchang_moonnrise")) {
            answer = getAnswer(question, 3, 2);
        } else if (intent.equals("daily_panchang_chandra_rashi")) {
            answer = getAnswer(question, 3, 3);
        } else if (intent.equals("daily_panchang_sunset")) {
            answer = getAnswer(question, 3, 4);
        } else if (intent.equals("daily_panchang_moonset")) {
            answer = getAnswer(question, 3, 5);
        } else if (intent.equals("daily_panchang_current_ritu")) {
            answer = getAnswer(question, 3, 6);
        } else if (intent.equals("tomorrow_panchang_sunrise")) {
            answer = getAnswer(question, 3, 7);
        } else if (intent.equals("tomorrow_panchang_moonnrise")) {
            answer = getAnswer(question, 3, 8);
        } else if (intent.equals("tomorrow_panchang_chandra_rashi")) {
            answer = getAnswer(question, 3, 9);
        } else if (intent.equals("tomorrow_panchang_sunset")) {
            answer = getAnswer(question, 3, 10);
        } else if (intent.equals("tomorrow_panchang_moonset")) {
            answer = getAnswer(question, 3, 11);
        } else if (intent.equals("daily_panchang_rahukaal")) {
            answer = getAnswer(question, 4, 0);
        } else if (intent.equals("tomorrow_panchang_rahukaal")) {
            answer = getAnswer(question, 4, 1);
        }*//* else if (intent.equals("daily_panchang_subh_muhurt")) {
            answer = getAnswer(question, 5, 0);
        } else if (intent.equals("tomorrow_panchang_subh_muhurt")) {
            answer = getAnswer(question, 5, 1);
        } else if (intent.equals("present_muhurt")) {
            answer = getAnswer(question, 5, 2);
        } else if (intent.equals("daily_panchang_chogdiya")) {
            answer = getAnswer(question, 6, 0);
        } else if (intent.equals("tomorrow_panchang_chogdiya")) {
            answer = getAnswer(question, 6, 1);
        } else if (intent.equals("daily_panchang_hora")) {
            answer = getAnswer(question, 7, 0);
        } else if (intent.equals("tomorrow_panchang_hora")) {
            answer = getAnswer(question, 7, 1);
        } else if (intent.equals("sade_sati_start")) {
            answer = getAnswer(question, 8, 1);
        } else if (intent.equals("sade_sati_end")) {
            answer = getAnswer(question, 8, 2);
        } else if (intent.equals("is_sade_sati_started")) {
            answer = getAnswer(question, 8, 3);
        } else if (intent.equals("maha_dasha_start")) {
            answer = getAnswer(question, 10, 1);
        }
*//*else if (isQuestionExist(results, maha_dasha_start) {
            answer = mainActivity.getAnswer(10, 2);
        } else if (isQuestionExist(results, CGlobalVariables.maha_dasha_start)) {
            answer = mainActivity.getAnswer(10, 3);
        }*//*
        else if (intent.equals("my_date_of_birth")) {
            answer = getAnswer(question, 12, 0);
        } else if (intent.equals("my_place_of_birth")) {
            answer = getAnswer(question, 12, 1);
        } else if (intent.equals("current_date")) {
            answer = getAnswer(question, 13, 0);
        } else if (intent.equals("current_date_tomorrow")) {
            answer = getAnswer(question, 13, 1);
        } else if (intent.equals("my_name")) {
            answer = getAnswer(question, 14, 0);
        }*/ /*else if (intent.equals("my_current_place")) {
            answer = getAnswer(question, 14, 1);
        }*/ /*else if (intent.equals("your_language")) {
            answer = getAnswer(question, 15, 0);
        }

        //hora questions
        else if (intent.equals("Politics_Govt_dealing_Govt Jobs_court_adventure")) {
            answer = getAnswer(question, 16, 0);
        } else if (intent.equals("Travelling_romance_ornaments_art")) {
            answer = getAnswer(question, 16, 1);
        } else if (intent.equals("Land_and_Agriculture_brother_engineering_sports")) {
            answer = getAnswer(question, 16, 2);
        } else if (intent.equals("Trade_learning_astrology_reading_and_writing")) {
            answer = getAnswer(question, 16, 3);
        } else if (intent.equals("Everything_work_jobs_business")) {
            answer = getAnswer(question, 16, 4);
        } else if (intent.equals("Love_marriage_entertainment_dance")) {
            answer = getAnswer(question, 16, 5);
        } else if (intent.equals("Labor_iron_oil_servants_renunciation")) {
            answer = getAnswer(question, 16, 6);
        }*/ /*else if (intent.equals("SURYA_HORA_SUBH")) {
            answer = getAnswer(question, 17, 0);
        } else if (intent.equals("MOON_HORA_SUBH")) {
            answer = getAnswer(question, 17, 1);
        } else if (intent.equals("MARS_HORA_SUBH")) {
            answer = getAnswer(question, 17, 2);
        } else if (intent.equals("MERCURY_HORA_SUBH")) {
            answer = getAnswer(question, 17, 3);
        } else if (intent.equals("JUPITER_HORA_SUBH")) {
            answer = getAnswer(question, 17, 4);
        } else if (intent.equals("VENUS_HORA_SUBH")) {
            answer = getAnswer(question, 17, 5);
        } else if (intent.equals("SATURN_HORA_SUBH")) {
            answer = getAnswer(question, 17, 6);
        } else if (intent.equals("SURYA_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 0);
        } else if (intent.equals("MOON_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 1);
        } else if (intent.equals("MARS_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 2);
        } else if (intent.equals("MERCURY_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 3);
        } else if (intent.equals("JUPITER_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 4);
        } else if (intent.equals("VENUS_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 5);
        } else if (intent.equals("SATURN_HORA_ASUBH")) {
            answer = getAnswer(question, 18, 6);
        }*/


        //tomorrow

        /*else if (intent.equals("Politics_Govt_dealing_Govt Jobs_court_adventure_tomorrow")) {
            answer = getAnswer(question, 19, 0);
        } else if (intent.equals("Travelling_romance_ornaments_art_tomorrow")) {
            answer = getAnswer(question, 19, 1);
        } else if (intent.equals("Land_and_Agriculture_brother_engineering_sports_tomorrow")) {
            answer = getAnswer(question, 19, 2);
        } else if (intent.equals("Trade_learning_astrology_reading_and_writing_tomorrow")) {
            answer = getAnswer(question, 19, 3);
        } else if (intent.equals("Everything_work_jobs_business_tomorrow")) {
            answer = getAnswer(question, 19, 4);
        } else if (intent.equals("Love_marriage_entertainment_dance_tomorrow")) {
            answer = getAnswer(question, 19, 5);
        } else if (intent.equals("Labor_iron_oil_servants_renunciation_tomorrow")) {
            answer = getAnswer(question, 19, 6);
        }*/


        //tomorrow subh asubh


        /*else if (intent.equals("TOMORROW_SURYA_HORA_SUBH")) {
            answer = getAnswer(question, 20, 0);
        } else if (intent.equals("TOMORROW_MOON_HORA_SUBH")) {
            answer = getAnswer(question, 20, 1);
        } else if (intent.equals("TOMORROW_MARS_HORA_SUBH")) {
            answer = getAnswer(question, 20, 2);
        } else if (intent.equals("TOMORROW_MERCURY_HORA_SUBH")) {
            answer = getAnswer(question, 20, 3);
        } else if (intent.equals("TOMORROW_JUPITER_HORA_SUBH")) {
            answer = getAnswer(question, 20, 4);
        } else if (intent.equals("TOMORROW_VENUS_HORA_SUBH")) {
            answer = getAnswer(question, 20, 5);
        } else if (intent.equals("TOMORROW_SATURN_HORA_SUBH")) {
            answer = getAnswer(question, 20, 6);
        }

        // tomorrow asubh
        else if (intent.equals("TOMORROW_SURYA_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 0);
        } else if (intent.equals("TOMORROW_MOON_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 1);
        } else if (intent.equals("TOMORROW_MARS_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 2);
        } else if (intent.equals("TOMORROW_MERCURY_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 3);
        } else if (intent.equals("TOMORROW_JUPITER_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 4);
        } else if (intent.equals("TOMORROW_VENUS_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 5);
        } else if (intent.equals("TOMORROW_SATURN_HORA_ASUBH")) {
            answer = getAnswer(question, 23, 6);
        }*/ /*else if (intent.equals("today_lucky_color")) {
            answer = getAnswer(question, 24, 0);
        }

        //Remedies
        else if (intent.equals("daily_horoscope_remedy")) {
            answer = getAnswer(question, 25, 0);
        } else if (intent.equals("current_time")) {
            answer = getAnswer(question, 26, 0);
        }*/

        return answer;
    }

    public String getAnswer(String question, int resultCode, int subResultCode) {
        String result = "";

        try {
            if (resultCode == 1) {
                int moonSign = CUtils.getIntData(context, com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_Moon_Degree, 0);
                Calendar calendar = Calendar.getInstance();
                if (subResultCode != 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    new TomorrowRashifal(moonSign).execute();
                    //result = "please wait";
                } else {
                    String data = getTodayRemedies(((AIChatWindowActivity) context).getChatLangCode(), context);
                    if (!data.equals("")) {
                        Gson gson = new Gson();
                        ((AIChatWindowActivity) context).beanHoroscopeRemedies = gson.fromJson(data, new TypeToken<ArrayList<BeanHoroscopeRemedies>>() {
                        }.getType());
                    }
                    BeanHoroscopeRemedies beanHoroscopeRemedies = ((AIChatWindowActivity) context).beanHoroscopeRemedies.get(moonSign);

//                    result = "\n" + beanHoroscopeRemedies.getRashi() + "\n\n" + Html.fromHtml(beanHoroscopeRemedies.getSentence()).toString();
                    result = "\n\n" + Html.fromHtml(beanHoroscopeRemedies.getSentence()).toString();
                }
            } else if (resultCode == 2) {
                Calendar calendar = Calendar.getInstance();
                if (subResultCode > 8) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    setCalculationsForTomorrow(calendar);
                } else {
                    setCalculations(calendar);
                }

                if (subResultCode == 0) {
                    result = getOtherLanguageString(R.string.panchang_str, context);
                    result = result.replace("#currendate", getCurrentDate(0, context));
                    result = result.replace("#vaar", model.getVaara());
                    result = result.replace("#samvat", model.getVikramSamvat());
                    result = result.replace("#paksh", model.getPakshaName());
                    result = result.replaceFirst("#tithi", model.getTithiValue());
                    result = result.replaceFirst("#tithiend", model.getTithiTime());
                    result = result.replaceFirst("#tithi", model.getTithiValue());
                    result = result.replaceFirst("#nakshtra", model.getNakshatraValue());
                    result = result.replaceFirst("#nakshtraend", model.getNakshatraTime());
                    result = result.replaceFirst("#nakshtra", model.getNakshatraValue());
                    String[] karan = model.getKaranaValue().split(",");
                    String[] karanTime = model.getKaranaTime().split(",");
                    result = result.replaceFirst("#karan", karan[0]);
                    result = result.replaceFirst("#karanend", karanTime[0]);
                    if (karan.length > 1) {
                        result = result.replaceFirst("#karan", karan[1]);
                        result = result.replaceFirst("#karanend", karanTime[1]);
                    } else {
                        result = result.replaceFirst(getOtherLanguageString(R.string.karan_till, context), "");
                    }
                    result = result.replaceFirst("#yog", model.getYogaValue());
                    result = result.replaceFirst("#yogend", model.getYogaTime());
                    result = result.replaceFirst("#yog", model.getYogaValue());
                    result = result.replace("\n", "");
                }
                if (subResultCode == 1) {
                    result = getOtherLanguageString(R.string.tithi_str, context).replace("#month", model.getMonthAmanta());
                    result = result.replace("#paksh", model.getPakshaName());
                    result = result.replace("#tithi", model.getTithiValue());
                    result = result.replace("#time", model.getTithiTime());
                    Log.d("appHangIssue", "model.getPakshaName(): "+model.getPakshaName());
                    Log.d("appHangIssue", "model.getTithiValue(): "+model.getTithiValue());
                    Log.d("appHangIssue", "model.getTithiTime(): "+model.getTithiTime());
                } else if (subResultCode == 2) {
                    result = getOtherLanguageString(R.string.nakshtra_str, context).replaceFirst("#", model.getNakshatraValue());
                    result = result.replaceFirst("#time", model.getNakshatraTime());
                } else if (subResultCode == 3) {
                    String[] karan = model.getKaranaValue().split(",");
                    String[] karanTime = model.getKaranaTime().split(",");

                    result = getOtherLanguageString(R.string.karan_Str, context);
                    result = result.replaceFirst("#karan", karan[0]).replace("\n", "");
                    result = result.replaceFirst("#date", karanTime[0]).replace("\n", "");
                    if (karan.length > 1) {
                        result = result.replaceFirst("#karan", karan[1]).replace("\n", "");
                        result = result.replaceFirst("#date", karanTime[1]).replace("\n", "");
                    } else {
                        result = result.replaceFirst(getOtherLanguageString(R.string.karan_after_that, context), "").replace("\n", "");
                    }
                } else if (subResultCode == 4) {
                    result = getOtherLanguageString(R.string.paksh_str, context).replace("#", model.getPakshaName());
                } else if (subResultCode == 5) {
                    result = getOtherLanguageString(R.string.yog_str, context).replaceFirst("#", model.getYogaValue());
                    result = result.replaceFirst("#time", model.getYogaTime());
                } else if (subResultCode == 6) {
                    result = getOtherLanguageString(R.string.var_str, context).replace("#", model.getVaara());
                } else if (subResultCode == 7) {
                    result = getOtherLanguageString(R.string.dishsool_str, context).replaceAll("#", model.getDishaShoola());
                } else if (subResultCode == 8) {
                    result = getOtherLanguageString(R.string.month_str, context).replaceAll("#", model.getMonthPurnimanta());
                } else if (subResultCode == 9) {
                    result = getOtherLanguageString(R.string.panchang_str, context);
                    result = result.replace("#currendate", getCurrentDate(1, context));
                    result = result.replace("#vaar", tomorrowModel.getVaara());
                    result = result.replace("#samvat", tomorrowModel.getVikramSamvat());
                    result = result.replace("#paksh", tomorrowModel.getPakshaName());
                    result = result.replaceFirst("#tithi", tomorrowModel.getTithiValue());
                    result = result.replaceFirst("#tithiend", tomorrowModel.getTithiTime());
                    result = result.replaceFirst("#tithi", tomorrowModel.getTithiValue());
                    result = result.replaceFirst("#nakshtra", tomorrowModel.getNakshatraValue());
                    result = result.replaceFirst("#nakshtraend", tomorrowModel.getNakshatraTime());
                    result = result.replaceFirst("#nakshtra", tomorrowModel.getNakshatraValue());
                    String[] karan = tomorrowModel.getKaranaValue().split(",");
                    String[] karanTime = tomorrowModel.getKaranaTime().split(",");
                    result = result.replaceFirst("#karan", karan[0]);
                    result = result.replaceFirst("#karanend", karanTime[0]);
                    if (karan.length > 1) {
                        result = result.replaceFirst("#karan", karan[1]);
                        result = result.replaceFirst("#karanend", karanTime[1]);
                    } else {
                        result = result.replaceFirst(getOtherLanguageString(R.string.karan_till, context), "");
                    }
                    result = result.replaceFirst("#yog", tomorrowModel.getYogaValue());
                    result = result.replaceFirst("#yogend", tomorrowModel.getYogaTime());
                    result = result.replaceFirst("#yog", tomorrowModel.getYogaValue());
                    result = result.replace("\n", "");
                }
                if (subResultCode == 10) {
                    result = getOtherLanguageString(R.string.tomorrow_tithi_str, context).replace("#month", tomorrowModel.getMonthAmanta());
                    result = result.replace("#paksh", tomorrowModel.getPakshaName());
                    result = result.replace("#tithi", tomorrowModel.getTithiValue());
                    result = result.replace("#time", tomorrowModel.getTithiTime());
                } else if (subResultCode == 11) {
                    result = getOtherLanguageString(R.string.tomorrow_nakshtra_str, context).replaceFirst("#", tomorrowModel.getNakshatraValue());
                    result = result.replaceFirst("#time", tomorrowModel.getNakshatraTime());
                } else if (subResultCode == 12) {
                    String[] karan = tomorrowModel.getKaranaValue().split(",");
                    String[] karanTime = tomorrowModel.getKaranaTime().split(",");

                    result = getOtherLanguageString(R.string.tomorrow_karan_Str, context);
                    result = result.replaceFirst("#karan", karan[0]).replace("\n", "");
                    result = result.replaceFirst("#date", karanTime[0]).replace("\n", "");
                    if (karan.length > 1) {
                        result = result.replaceFirst("#karan", karan[1]).replace("\n", "");
                        result = result.replaceFirst("#date", karanTime[1]).replace("\n", "");
                    } else {
                        result = result.replaceFirst(context.getResources().getString(R.string.karan_after_that), "").replace("\n", "");
                    }
                } else if (subResultCode == 13) {
                    result = getOtherLanguageString(R.string.tomorrow_paksh_str, context).replace("#", tomorrowModel.getPakshaName());
                } else if (subResultCode == 14) {
                    result = getOtherLanguageString(R.string.tomorrow_yog_str, context).replaceFirst("#", tomorrowModel.getYogaValue());
                    result = result.replaceFirst("#time", tomorrowModel.getYogaTime());
                } else if (subResultCode == 15) {
                    result = getOtherLanguageString(R.string.tomorrow_var_str, context).replace("#", tomorrowModel.getVaara());
                } else if (subResultCode == 16) {
                    result = getOtherLanguageString(R.string.tomorrow_dishsool_str, context).replaceAll("#", tomorrowModel.getDishaShoola());
                } else if (subResultCode == 17) {
                    result = getSamvat();
                }

            } else if (resultCode == 3) {
                Calendar calendar = Calendar.getInstance();
                if (subResultCode > 6) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    setCalculationsForTomorrow(calendar);
                } else {
                    setCalculations(calendar);
                }
                if (subResultCode == 1) {
                    String[] timeArray = model.getSunRise().split(":");
                    result = getOtherLanguageString(R.string.sun_rise_str, context).replace("#hour", removeZero(timeArray[0]));
                    result = result.replace("#minute", removeZero(timeArray[1]));
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 2) {
                    String[] timeArray = model.getMoonRise().split(":");
                    result = getOtherLanguageString(R.string.moon_rise_str, context).replace("#hour", removeZero(timeArray[0]));
                    result = result.replace("#minute", removeZero(timeArray[1]));
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 3) {
                    result = getOtherLanguageString(R.string.chandra_rashi_str, context).replace("#", model.getMoonSignValue());
                } else if (subResultCode == 4) {
                    String[] timeArray = model.getSunSet().split(":");
                    result = getOtherLanguageString(R.string.sun_set_str, context).replace("#hour", timeArray[0]);
                    result = result.replace("#minute", timeArray[1]);
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 5) {
                    String[] timeArray = model.getMoonSet().split(":");
                    result = getOtherLanguageString(R.string.moon_set_str, context).replace("#hour", removeZero(timeArray[0]));
                    result = result.replace("#minute", removeZero(timeArray[1]));
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 6) {
                    result = getOtherLanguageString(R.string.ritu_str, context).replace("#", model.getRitu());
                } else if (subResultCode == 7) {
                    String[] timeArray = tomorrowModel.getSunRise().split(":");
                    result = getOtherLanguageString(R.string.tomorrow_sun_rise_str, context).replace("#hour", timeArray[0]);
                    result = result.replace("#minute", timeArray[1]);
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 8) {
                    String[] timeArray = tomorrowModel.getMoonRise().split(":");
                    result = getOtherLanguageString(R.string.tomorrow_moon_rise_str, context).replace("#hour", removeZero(timeArray[0]));
                    result = result.replace("#minute", removeZero(timeArray[1]));
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 9) {
                    result = getOtherLanguageString(R.string.tomorrow_chandra_rashi_str, context).replace("#", tomorrowModel.getMoonSignValue());
                } else if (subResultCode == 10) {
                    String[] timeArray = tomorrowModel.getSunSet().split(":");
                    result = getOtherLanguageString(R.string.tomorrow_sun_set_str, context).replace("#hour", removeZero(timeArray[0]));
                    result = result.replace("#minute", removeZero(timeArray[1]));
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 11) {
                    String[] timeArray = tomorrowModel.getMoonSet().split(":");
                    result = getOtherLanguageString(R.string.tomorrow_moon_set_str, context).replace("#hour", removeZero(timeArray[0]));
                    result = result.replace("#minute", removeZero(timeArray[1]));
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                } else if (subResultCode == 12) {
                    result = getOtherLanguageString(R.string.ritu_str, context).replace("#", tomorrowModel.getRitu());
                }
            } else if (resultCode == 4) {
                //if (model == null) {
                String[] startTime;
                String[] endTime;
                Calendar calendar = Calendar.getInstance();
                if (subResultCode != 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    setCalculationsForTomorrow(calendar);
                    startTime = tomorrowModel.getRahuKaalVelaFrom().split(":");
                    endTime = tomorrowModel.getRahuKaalVelaTo().split(":");
                } else {
                    setCalculations(calendar);
                    startTime = model.getRahuKaalVelaFrom().split(":");
                    endTime = model.getRahuKaalVelaTo().split(":");
                }

                if (subResultCode == 0) {
                    result = getOtherLanguageString(R.string.rahukaal_str, context);
                } else {
                    result = getOtherLanguageString(R.string.tomorrow_rahukaal_str, context);
                }

                result = result.replace("#starthour", removeZero(startTime[0]));
                result = result.replace("#startminute", removeZero(startTime[1]));
                result = result.replace("#endhour", removeZero(endTime[0]));
                result = result.replace("#endminute", removeZero(endTime[1]));

            } else if (resultCode == 21) {
                result = vastu[subResultCode];
            } else if (resultCode == 5) {
                Calendar calendar = Calendar.getInstance();
                if (subResultCode == 1) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                result = initDoGhati(calendar, subResultCode);
            } else if (resultCode == 6) {
                Calendar calendar = Calendar.getInstance();
                if (subResultCode != 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                result = initChogadhiya(calendar, subResultCode);
            } else if (resultCode == 7) {
                Calendar calendar = Calendar.getInstance();
                if (subResultCode != 0) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                result = initHoraTable(calendar, subResultCode);
            } else if (resultCode == 8) {
                if (subResultCode == 1) {
                    result = getSadeSatiDetail(1);
                } else if (subResultCode == 2) {
                    result = getSadeSatiDetail(2);
                } else if (subResultCode == 3) {
                    result = getSadeSatiDetail(3);
                }

            }/* else if (resultCode == 9) {
                ((AIChatWindowActivity)context).getResponseFromServer(questions);
                result = "please wait";
            } */else if (resultCode == 10) {
                result = getMahaDashaDetail(question);
            } /*else if (resultCode == 11) {
                getResponseFromServer(questions);
                result = "please wait";
            } */else if (resultCode == 12) {
                if (subResultCode == 0) {
                    result = getOtherLanguageString(R.string.birthday_str, context);
                    result = result.replace("#date", getDateOfBirth());
                } else if (subResultCode == 1) {
                    result = getOtherLanguageString(R.string.birthplace_str, context);
                    result = result.replace("#place", getBirthPlace());
                }
            } else if (resultCode == 13) {
                if (subResultCode == 0) {
                    result = getOtherLanguageString(R.string.currentdate_str, context);
                    result = result.replace("#date", getCurrentDate(0, context));

                } else {
                    result = getOtherLanguageString(R.string.tomorrow_date_str, context);
                    result = result.replace("#date", getCurrentDate(1, context));

                }
            } else if (resultCode == 14) {
                if (subResultCode == 0) {
                    result = getOtherLanguageString(R.string.name_str, context);
                    result = result.replace("#name", getName());
                } else if (subResultCode == 1) {
                    result = getOtherLanguageString(R.string.current_place_str, context);
                    result = result.replace("#place", CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace));
                }
            } else if (resultCode == 15) {
                result = getOtherLanguageString(R.string.lang_str, context);
            } else if (resultCode == 16) {

                if (subResultCode == 0) {
                    aboutHora = "सूर्य";
                } else if (subResultCode == 1) {
                    aboutHora = "चन्द्रमा";
                } else if (subResultCode == 2) {
                    aboutHora = "मंगल";
                } else if (subResultCode == 3) {
                    aboutHora = "बुध";
                } else if (subResultCode == 4) {
                    aboutHora = "बृहस्पति";
                } else if (subResultCode == 5) {
                    aboutHora = "शुक्र";
                } else if (subResultCode == 6) {
                    aboutHora = "शनि";
                }

                UserProfileData userProfileData = CUtils.getProfileForChatFromPreference(context);

                String currentLalitude = userProfileData.getLatdeg();
                String currentLongitude = userProfileData.getLongdeg();
                String timeZone = userProfileData.getTimezone();

                Log.d("appHangIssue", "getAnswer: 1");

                Calendar cal = Calendar.getInstance();
                SetdataonList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), currentLalitude, currentLongitude, timeZone, "today");
                result = getExactHoraTime(aboutHora, "today");
            } else if (resultCode == 17) {

                if (subResultCode == 0) {
                    aboutHora = "सूर्य";
                } else if (subResultCode == 1) {
                    aboutHora = "चन्द्रमा";
                } else if (subResultCode == 2) {
                    aboutHora = "मंगल";
                } else if (subResultCode == 3) {
                    aboutHora = "बुध";
                } else if (subResultCode == 4) {
                    aboutHora = "बृहस्पति";
                } else if (subResultCode == 5) {
                    aboutHora = "शुक्र";
                } else if (subResultCode == 6) {
                    aboutHora = "शनि";
                }
                Calendar cal = Calendar.getInstance();
                SetdataonList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), CUtils.getStringData(context, CGlobalVariables.currentLalitude, "0"), CUtils.getStringData(context, CGlobalVariables.currentLongitude, "0"), CUtils.getStringData(context, CGlobalVariables.timeZone, "0"), "today");
                result = getExactHoraForSubhAsubhPlanet(aboutHora, "subh", "today");
            } else if (resultCode == 18) {

                if (subResultCode == 0) {
                    aboutHora = "सूर्य";
                } else if (subResultCode == 1) {
                    aboutHora = "चन्द्रमा";
                } else if (subResultCode == 2) {
                    aboutHora = "मंगल";
                } else if (subResultCode == 3) {
                    aboutHora = "बुध";
                } else if (subResultCode == 4) {
                    aboutHora = "बृहस्पति";
                } else if (subResultCode == 5) {
                    aboutHora = "शुक्र";
                } else if (subResultCode == 6) {
                    aboutHora = "शनि";
                }
                Calendar cal = Calendar.getInstance();
                SetdataonList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), CUtils.getStringData(context, CGlobalVariables.currentLalitude, "0"), CUtils.getStringData(context, CGlobalVariables.currentLongitude, "0"), CUtils.getStringData(context, CGlobalVariables.timeZone, "0"), "today");
                result = getExactHoraForSubhAsubhPlanet(aboutHora, "asubh", "today");
            }

            // tomorrow rajniti
            else if (resultCode == 19) {

                if (subResultCode == 0) {
                    aboutHora = "सूर्य";
                } else if (subResultCode == 1) {
                    aboutHora = "चन्द्रमा";
                } else if (subResultCode == 2) {
                    aboutHora = "मंगल";
                } else if (subResultCode == 3) {
                    aboutHora = "बुध";
                } else if (subResultCode == 4) {
                    aboutHora = "बृहस्पति";
                } else if (subResultCode == 5) {
                    aboutHora = "शुक्र";
                } else if (subResultCode == 6) {
                    aboutHora = "शनि";
                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);

                int day = cal.get(Calendar.DATE);

                SetdataonList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day, CUtils.getStringData(context, CGlobalVariables.currentLalitude, "0"), CUtils.getStringData(context, CGlobalVariables.currentLongitude, "0"), CUtils.getStringData(context, CGlobalVariables.timeZone, "0"), "tomorrow");
                result = getExactHoraTime(aboutHora, "tomorrow");
            }

            //tomorrow subh
            else if (resultCode == 20) {

                if (subResultCode == 0) {
                    aboutHora = "सूर्य";
                } else if (subResultCode == 1) {
                    aboutHora = "चन्द्रमा";
                } else if (subResultCode == 2) {
                    aboutHora = "मंगल";
                } else if (subResultCode == 3) {
                    aboutHora = "बुध";
                } else if (subResultCode == 4) {
                    aboutHora = "बृहस्पति";
                } else if (subResultCode == 5) {
                    aboutHora = "शुक्र";
                } else if (subResultCode == 6) {
                    aboutHora = "शनि";
                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);

                int day = cal.get(Calendar.DATE);
                SetdataonList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day, CUtils.getStringData(context, CGlobalVariables.currentLalitude, "0"), CUtils.getStringData(context, CGlobalVariables.currentLongitude, "0"), CUtils.getStringData(context, CGlobalVariables.timeZone, "0"), "tomorrow");
                result = getExactHoraForSubhAsubhPlanet(aboutHora, "subh", "tomorrow");
            }

//tomorrow asubh
            else if (resultCode == 23) {

                if (subResultCode == 0) {
                    aboutHora = "सूर्य";
                } else if (subResultCode == 1) {
                    aboutHora = "चन्द्रमा";
                } else if (subResultCode == 2) {
                    aboutHora = "मंगल";
                } else if (subResultCode == 3) {
                    aboutHora = "बुध";
                } else if (subResultCode == 4) {
                    aboutHora = "बृहस्पति";
                } else if (subResultCode == 5) {
                    aboutHora = "शुक्र";
                } else if (subResultCode == 6) {
                    aboutHora = "शनि";
                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);

                int day = cal.get(Calendar.DATE);
                SetdataonList(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day, CUtils.getStringData(context, CGlobalVariables.currentLalitude, "0"), CUtils.getStringData(context, CGlobalVariables.currentLongitude, "0"), CUtils.getStringData(context, CGlobalVariables.timeZone, "0"), "tomorrow");
                result = getExactHoraForSubhAsubhPlanet(aboutHora, "asubh", "tomorrow");
            } else if (resultCode == 24) {
                result = context.getResources().getString(R.string.lucky_for_today_str);
                result = result.replace("#", CUtils.getColor(LANGUAGE_CODE, Integer.parseInt(luckyNumber)));
            } else if (resultCode == 25) {
                try {
                    String data = getTodayRemedies(((AIChatWindowActivity) context).getChatLangCode(), context);
                    if (!data.equals("")) {
                        int rashi = CUtils.getIntData(context, com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_Moon_Degree, 0);
                        if (rashi != -1) {
                            Gson gson = new Gson();
                            ArrayList<BeanHoroscopeRemedies> beanHoroscopeRemediesArrayList = gson.fromJson(data, new TypeToken<ArrayList<BeanHoroscopeRemedies>>() {
                            }.getType());
                            BeanHoroscopeRemedies beanHoroscopeRemedies = beanHoroscopeRemediesArrayList.get(rashi);
                            result = context.getResources().getString(R.string.remedies_for_today) + ": ";
                            result = result + beanHoroscopeRemedies.getRemedy();
                        }
                    }
                } catch (Exception ex) {
                    Log.d("remediesIssue", "getAnswer 1 ex: "+ex);
                }
            } else if (resultCode == 26) {
                result = CUtils.getCurrentTime(context);
            }
        } catch (Exception e) {
            Log.d("remediesIssue", "getAnswer ex: "+e);
            result = result + e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    private class TomorrowRashifal extends AsyncTask<Integer, Void, String[]> {

        boolean isSuccess = true;
        String[] todaysRashifal = null;
        int moonSign;

        TomorrowRashifal(int moonSign) {
            this.moonSign = moonSign;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String[] doInBackground(Integer... params) {
            try {
                todaysRashifal = getTomorrowPredictionDetail(0, context, moonSign);
                Log.d("appHangIssue", "TomorrowRashifal todaysRashifal: "+ Arrays.toString(todaysRashifal));
            } catch (Exception e) {
                Log.d("appHangIssue", "TomorrowRashifal ex: "+e);
                isSuccess = false;
            }

            if (isSuccess) {
                todaysRashifal[1] = todaysRashifal[1].replace("AstroSage.com,", "");
                return todaysRashifal;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            if (s != null) {
                if (todaysRashifal != null) {
                    todaysRashifal[1] = todaysRashifal[1].replace("AstroSage.com,", "");
                }
                String result = "\n" + todaysRashifal[0] + "\n\n" + Html.fromHtml(todaysRashifal[1]).toString();
                ((AIChatWindowActivity)context).speakOut(result, "", "");
            }
        }
    }

    public String[] getTomorrowPredictionDetail(int LANGUAGE_CODE,
                                                      Context context, int SELECTED_MOON_SIGN) {
        String tomorrowsHoroscopeDetail[] = new String[2];
        tomorrowsHoroscopeDetail[1] = "YES";
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,
                Locale.ENGLISH);
        String tomorrowHoroscopeKey = dateFormat.format(c.getTime());

        SharedPreferences tomorrowsHoroscope = context.getSharedPreferences(
                CGlobalVariables.dailyHoroscopeTomorrowPrefName,
                Context.MODE_PRIVATE);
        /*if (CUtils.isSupportUnicodeHindi()
                && LANGUAGE_CODE == CGlobalVariables.HINDI) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefHindiName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefTamilName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefMarathiName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefBangaliName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefkannadName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefTelguName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefGujaratiName,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefMalayalamName,
                    Context.MODE_PRIVATE);
        } else {
            tomorrowsHoroscope = context.getSharedPreferences(
                    CGlobalVariables.dailyHoroscopeTomorrowPrefName,
                    Context.MODE_PRIVATE);
        }*/

        if (!tomorrowHoroscopeKey.equalsIgnoreCase(tomorrowsHoroscope
                .getString("TOMORROWSHOROSCOPEKEY", ""))) {
            List<com.libojassoft.android.customrssfeed.CMessage> listMessage = null;

            if (CUtils.isConnectedWithInternet(context)) {
                try {
                    listMessage = new CXmlPullFeedParser(
                            CGlobalVariables.dailyHoroscopeTomorrowRssFeedURL)
                            .parse();
                    /*if (CUtils.isSupportUnicodeHindi()
                            && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeHindi)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeTamil)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeMarathi)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeBengali)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodekannad)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeTelgu)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeGujarati)
                                .parse();
                    } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURLinUnicodeMalayalam)
                                .parse();
                    } else {
                        listMessage = new com.libojassoft.android.customrssfeed.CXmlPullFeedParser(
                                CGlobalVariables.dailyHoroscopeTomorrowRssFeedURL)
                                .parse();
                    }*/
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            } else {
                tomorrowsHoroscopeDetail[1] = "NO_INTERNET";
            }
            /*if (listMessage != null) {
                // String rashiTitle = "";
                String rashiPrediction = " ";
                SharedPreferences.Editor editor = tomorrowsHoroscope.edit();

                rashiPrediction = listMessage.get(CGlobalVariables.ARIES).getDescription().toString();
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
            }*/
            if (listMessage != null) {
                String rashiTitle = "";
                String rashiPrediction = " ";
                SharedPreferences.Editor editor = tomorrowsHoroscope.edit();

                rashiTitle = listMessage.get(CGlobalVariables.ARIES).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.ARIES).getDescription().toString();
                editor.putString("ARIES_TITLE", rashiTitle);
                editor.putString("ARIES", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.TAURUS).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.TAURUS).getDescription().toString();
                editor.putString("TAURUS_TITLE", rashiTitle);
                editor.putString("TAURUS", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.GEMINI).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.GEMINI).getDescription().toString();
                editor.putString("GEMINI_TITLE", rashiTitle);
                editor.putString("GEMINI", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.CANCER).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.CANCER).getDescription().toString();
                editor.putString("CANCER_TITLE", rashiTitle);
                editor.putString("CANCER", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.LEO).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.LEO).getDescription().toString();
                editor.putString("LEO_TITLE", rashiTitle);
                editor.putString("LEO", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.VIRGO).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.VIRGO).getDescription().toString();
                editor.putString("VIRGO_TITLE", rashiTitle);
                editor.putString("VIRGO", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.LIBRA).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.LIBRA).getDescription().toString();
                editor.putString("LIBRA_TITLE", rashiTitle);
                editor.putString("LIBRA", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.SCORPIO).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.SCORPIO).getDescription().toString();
                editor.putString("SCORPIO_TITLE", rashiTitle);
                editor.putString("SCORPIO", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.SAGITTARIUS).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.SAGITTARIUS).getDescription().toString();
                editor.putString("SAGITTARIUS_TITLE", rashiTitle);
                editor.putString("SAGITTARIUS", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.CAPRICORN).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.CAPRICORN).getDescription().toString();
                editor.putString("CAPRICORN_TITLE", rashiTitle);
                editor.putString("CAPRICORN", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.AQUARIUS).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.AQUARIUS).getDescription().toString();
                editor.putString("AQUARIUS_TITLE", rashiTitle);
                editor.putString("AQUARIUS", rashiPrediction);

                rashiTitle = listMessage.get(CGlobalVariables.PISCES).getTitle();
                rashiPrediction = listMessage.get(CGlobalVariables.PISCES).getDescription().toString();
                editor.putString("PISCES_TITLE", rashiTitle);
                editor.putString("PISCES", rashiPrediction);

                editor.putString("TOMORROWSHOROSCOPEKEY", tomorrowHoroscopeKey);

                editor.commit();
            }
        }
        switch (SELECTED_MOON_SIGN) {
           /* case CGlobalVariables.ARIES:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("ARIES_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("ARIES",
                        " ");
                break;
            case CGlobalVariables.TAURUS:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("TAURUS_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "TAURUS", " ");
                break;
            case CGlobalVariables.GEMINI:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("GEMINI_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "GEMINI", " ");
                break;
            case CGlobalVariables.CANCER:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("CANCER_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "CANCER", " ");
                break;
            case CGlobalVariables.LEO:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("LEO_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("LEO",
                        " ");
                break;
            case CGlobalVariables.VIRGO:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("VIRGO_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("VIRGO",
                        " ");
                break;
            case CGlobalVariables.LIBRA:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("LIBRA_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("LIBRA",
                        " ");
                break;
            case CGlobalVariables.SCORPIO:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("SCORPIO_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "SCORPIO", " ");
                break;
            case CGlobalVariables.SAGITTARIUS:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("SAGITTARIUS_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "SAGITTARIUS", " ");
                break;
            case CGlobalVariables.CAPRICORN:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("CAPRICORN_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "CAPRICORN", " ");
                break;
            case CGlobalVariables.AQUARIUS:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("AQUARIUS_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "AQUARIUS", " ");
                break;
            case CGlobalVariables.PISCES:
                // tomorrowsHoroscopeDetail[0] =
                // tomorrowsHoroscope.getString("PISCES_TITLE", " ");
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString(
                        "PISCES", " ");
                break;*/
            case CGlobalVariables.ARIES:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("ARIES_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("ARIES", " ");
                break;
            case CGlobalVariables.TAURUS:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("TAURUS_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("TAURUS", " ");
                break;
            case CGlobalVariables.GEMINI:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("GEMINI_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("GEMINI", " ");
                break;
            case CGlobalVariables.CANCER:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("CANCER_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("CANCER", " ");
                break;
            case CGlobalVariables.LEO:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("LEO_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("LEO", " ");
                break;
            case CGlobalVariables.VIRGO:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("VIRGO_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("VIRGO", " ");
                break;
            case CGlobalVariables.LIBRA:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("LIBRA_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("LIBRA", " ");
                break;
            case CGlobalVariables.SCORPIO:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("SCORPIO_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("SCORPIO", " ");
                break;
            case CGlobalVariables.SAGITTARIUS:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("SAGITTARIUS_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("SAGITTARIUS", " ");
                break;
            case CGlobalVariables.CAPRICORN:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("CAPRICORN_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("CAPRICORN", " ");
                break;
            case CGlobalVariables.AQUARIUS:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("AQUARIUS_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("AQUARIUS", " ");
                break;
            case CGlobalVariables.PISCES:
                tomorrowsHoroscopeDetail[0] = tomorrowsHoroscope.getString("PISCES_TITLE", " ");
                tomorrowsHoroscopeDetail[1] = tomorrowsHoroscope.getString("PISCES", " ");
                break;
        }

        if (tomorrowsHoroscopeDetail[1].equals("NO_INTERNET")) {
            tomorrowsHoroscopeDetail[0] = " ";
            // tomorrowsHoroscopeDetail[1] = " ";
        }
        return tomorrowsHoroscopeDetail;
    }

    public String getTodayRemedies(int LANGUAGE_CODE, Context context) {
        String todaysRemediesDetail = "";
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,
                Locale.ENGLISH);
        String todaysRemediesKeyValue = dateFormat.format(date.getTime());

        SharedPreferences todaysRemediesPref;
        if (CUtils.isSupportUnicodeHindi()
                && LANGUAGE_CODE == CGlobalVariables.HINDI) {
            todaysRemediesPref = context.getSharedPreferences(
                    CGlobalVariables.dailyRemediesPrefHindiName,
                    Context.MODE_PRIVATE);
        } else {
            todaysRemediesPref = context.getSharedPreferences(
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.dailyRemediesPrefName,
                    Context.MODE_PRIVATE);
        }
        String resultStr = "";
        if (!todaysRemediesKeyValue.equalsIgnoreCase(todaysRemediesPref
                .getString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, ""))) {
            if (CUtils.isConnectedWithInternet(context)) {
                try {
                    String urlParameters = "language=" + CUtils.getLanguageKey(LANGUAGE_CODE) +
                            "&key=" + CUtils.getApplicationSignatureHashCode(context);
                    resultStr = HttpUtility.sendPostRequestXForm(CGlobalVariables.dailyRemediesURL, urlParameters);
                    Log.d("MyTag", "resultStr->" + resultStr);
                } catch (Exception ex) {
                    Log.d("remediesIssue", "getTodayRemedies ex: "+ex);
                }
            } else {
                resultStr = "NO_INTERNET";
            }

            if (resultStr != null && !TextUtils.isEmpty(resultStr) && resultStr.contains("Rashi")) {
                SharedPreferences.Editor editor = todaysRemediesPref.edit();
                editor.putString("Remedies", resultStr);
                editor.putString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, todaysRemediesKeyValue);
                editor.commit();
            }
        }

        todaysRemediesDetail = todaysRemediesPref.getString("Remedies", "");

        if (resultStr.equals("NO_INTERNET")) {
            todaysRemediesDetail = "";
        }

        Log.d("remediesIssue", "getTodayRemedies todaysRemediesDetail: "+todaysRemediesDetail);
        return todaysRemediesDetail;
    }

    public void setCalculationsForTomorrow(Calendar calendar) {

        UserProfileData userProfileData = CUtils.getProfileForChatFromPreference(context);

        String currentLalitude = userProfileData.getLatdeg();
        String currentLongitude = userProfileData.getLongdeg();
        String timeZone = userProfileData.getTimezone();
        String timeZoneId = CGlobalVariables.defaultTimeZoneString;

        //Calendar calendar = Calendar.getInstance();
        Date _datePan = calendar.getTime();

        //Save calender value in shared pref. for further use
        //long millis = calendar.getTimeInMillis();
        //com.ojassoft.astrosage.utils.CUtils.saveLongData(context, CGlobalVariables.calenderCurrentTime, millis);

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, "en", currentLalitude, currentLongitude, timeZone, timeZoneId);
        tomorrowModel = calculation.getPanchang();
    }

    public void setCalculations(Calendar calendar) {

        UserProfileData userProfileData = CUtils.getProfileForChatFromPreference(context);

        String currentLalitude = userProfileData.getLatdeg();
        String currentLongitude = userProfileData.getLongdeg();
        String timeZone = userProfileData.getTimezone();
        String timeZoneId = CGlobalVariables.defaultTimeZoneString;

        Log.d("appHangIssue", "setCalculations currentLalitude: "+currentLalitude);
        Log.d("appHangIssue", "setCalculations currentLongitude: "+currentLongitude);
        Log.d("appHangIssue", "setCalculations timeZone: "+timeZone);
        Log.d("appHangIssue", "setCalculations timeZoneId: "+timeZoneId);

        //Calendar calendar = Calendar.getInstance();
        Date _datePan = calendar.getTime();

        //Save calender value in shared pref. for further use
        /*long millis = calendar.getTimeInMillis();
        com.ojassoft.astrosage.utils.CUtils.saveLongData(context, CGlobalVariables.calenderCurrentTime, millis);*/

        AajKaPanchangCalulation calculation = new AajKaPanchangCalulation(_datePan, "en", currentLalitude, currentLongitude, timeZone, timeZoneId);
        model = calculation.getPanchang();
    }

    public String getOtherLanguageString(int resId, Context context) {

        String result = context.getString(resId);
        Configuration conf = context.getResources().getConfiguration();
        Configuration conf1 = new Configuration(conf);

        /*if (langCode.equals("en")) {
            conf1.locale = new Locale("en");
        } else if (langCode.equals("hi")) {
            conf1.locale = new Locale("hi");
        }*/

        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            Resources resources = new Resources(context.getAssets(), metrics, conf1);
            result = resources.getString(resId);
        }
        return result;
    }

    private String getSamvat() {
        String samvatStr = "";
        try {
            Calendar calendar = Calendar.getInstance();
            setCalculations(calendar);
            samvatStr = context.getResources().getString(R.string.samvat_str);
            samvatStr = samvatStr.replaceFirst("#year", model.getShakaSamvatYear());
            samvatStr = samvatStr.replaceFirst("#year", model.getKaliSamvat());
            samvatStr = samvatStr.replaceFirst("#year", model.getVikramSamvat());

        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage());
        }
        return samvatStr;
    }

    /**
     * Initializing DoGhati
     */
    private String initDoGhati(Calendar calendar, int subResultCode) {
        String result = "";
        try {
            String currentLalitude = CUtils.getStringData(context, CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
            String currentLongitude = CUtils.getStringData(context, CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
            String locality = CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace);
            String timeZone = CUtils.getStringData(context, CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
            String timeZoneString = CUtils.getStringData(context, CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);
            //long currentTimeInMillis = CUtils.getLongData(context, CGlobalVariables.calenderCurrentTime, Calendar.getInstance().getTimeInMillis());


            //calendar.setTimeInMillis(currentTimeInMillis);

            int day_of_month = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            CalculateDoGhatiMuhurat calculateDoGhatiMuhurat = new CalculateDoGhatiMuhurat(context);
            List<HoraMetadata> data = calculateDoGhatiMuhurat.HoraLordName(day_of_month);
            List<HoraMetadata> datatime = calculateDoGhatiMuhurat.HoraEntryTime(calendar, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone, timeZoneString);
            List<HoraMetadata> datatimeexit = calculateDoGhatiMuhurat.HoraExitTime(calendar, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone, timeZoneString);
            int number = getCurrentHoraNumber(datatime, datatimeexit);

            String one = data.get(number).getPlanetmeaning() + "("
                    + data.get(number).getPlanetdata() + ")";
            String two = datatime.get(number).getEntertimedata() + "-"
                    + datatimeexit.get(number).getExittimedata();
            String[] startTime = datatime.get(number).getEntertimedata().split(":");
            String[] endTime = datatimeexit.get(number).getExittimedata().split(":");

           /* result = "आज का शुभ मुहूर्त" + data.get(number).getPlanetmeaning() + "("
                    + data.get(number).getPlanetdata() + ")" + " है जो" + startTime[0] + " बजकर " + startTime[1] + " मिनट से शुरू होकर " + endTime[0] + " बजकर " + endTime[1] + " मिनट पर ख़त्म होगा ";*/
            if (subResultCode == 0 || subResultCode == 2) {
                result = context.getResources().getString(R.string.muhurt_str).replace("#muhurt", data.get(number).getPlanetmeaning() + "("
                        + data.get(number).getPlanetdata() + ")");
            } else {
                result = context.getResources().getString(R.string.tomorrow_muhurt_str).replace("#muhurt", data.get(number).getPlanetmeaning() + "("
                        + data.get(number).getPlanetdata() + ")");
            }

            result = result.replace("#starthour", removeZero(startTime[0]));
            result = result.replace("#startminute", removeZero(startTime[1]));
            result = result.replace("#endhour", removeZero(endTime[0]));
            result = result.replace("#endminute", removeZero(endTime[1]));
        } catch (Exception ex) {
            ////MyLogger.errorLog("MainActivity 22 - " + ex.getMessage());
            Log.i("TAG", ex.getMessage());
        }
        return result;
    }

    /**
     * Initializing Chogadhiya
     */
    private String initChogadhiya(Calendar calendar, int subResultCode) {
        String result = "";
        try {
            //if (model == null) {
            setCalculations(calendar);
            //}

            String currentLalitude = CUtils.getStringData(context, CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
            String currentLongitude = CUtils.getStringData(context, CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
            String locality = CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace);
            String timeZone = CUtils.getStringData(context, CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
            String timeZoneString = CUtils.getStringData(context, CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);
            // long currentTimeInMillis = CUtils.getLongData(context, CGlobalVariables.calenderCurrentTime, Calendar.getInstance().getTimeInMillis());


            String[] sunRiseArr = model.getSunRise().split(":");
            boolean boolVal = isTimeBeforeSunRise(sunRiseArr);


            // calendar.setTimeInMillis(currentTimeInMillis);
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


            String one = data.get(number).getPlanetdata();
            String two = datatime.get(number).getEntertimedata() + "-"
                    + datatimeexit.get(number).getExittimedata();
            String three = data.get(number).getPlanetmeaning();
            String[] startTime = datatime.get(number).getEntertimedata().split(":");
            String[] endTime = datatimeexit.get(number).getExittimedata().split(":");
            //result = "आज " + data.get(number).getPlanetdata() + "  चोगडिया है जिसका मतलब " + data.get(number).getPlanetmeaning() + " है," + startTime[0] + " बजकर " + startTime[1] + " मिनट से शुरू होकर " + endTime[0] + " बजकर " + endTime[1] + " मिनट ख़त्म होगा  ";
            if (subResultCode == 0) {
                result = context.getResources().getString(R.string.chogdiya_str).replace("#chogdiya", data.get(number).getPlanetdata());
            } else {
                result = context.getResources().getString(R.string.tomorrow_chogdiya_str).replace("#chogdiya", data.get(number).getPlanetdata());
            }

            result = result.replace("#result", data.get(number).getPlanetmeaning());
            result = result.replace("#starthour", removeZero(startTime[0]));
            result = result.replace("#startminute", removeZero(startTime[1]));
            result = result.replace("#endhour", removeZero(endTime[0]));
            result = result.replace("#endminute", removeZero(endTime[1]));

        } catch (Exception ex) {
            ////MyLogger.errorLog("MainActivity 23 - " + ex.getMessage());
            Log.i("TAG", ex.getMessage());
        }
        return result;
    }

    /**
     * Initializing Hora Table
     */
    private String initHoraTable(Calendar calendar, int subResultCode) {
        String result = "";
        try {
            String currentLalitude = CUtils.getStringData(context, CGlobalVariables.currentLalitude, CGlobalVariables.defaultLatitude);
            String currentLongitude = CUtils.getStringData(context, CGlobalVariables.currentLongitude, CGlobalVariables.defaultLongitude);
            String locality = CUtils.getStringData(context, CGlobalVariables.locality, CGlobalVariables.defaultPlace);
            String timeZone = CUtils.getStringData(context, CGlobalVariables.timeZone, CGlobalVariables.defaultTimeZone);
            String timeZoneString = CUtils.getStringData(context, CGlobalVariables.timeZoneId, CGlobalVariables.defaultTimeZoneString);
            //long currentTimeInMillis = CUtils.getLongData(context, CGlobalVariables.calenderCurrentTime, Calendar.getInstance().getTimeInMillis());


            //calendar.setTimeInMillis(currentTimeInMillis);


            int day_of_month = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            List<HoraMetadata> data = HoraLordName(day_of_month);

            List<HoraMetadata> horaEntryTime = HoraEntryTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone);

            List<HoraMetadata> horaExitTime = HoraExitTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), currentLalitude, currentLongitude, timeZone);

            int number = getCurrentHoraNumber(horaEntryTime, horaExitTime);


            String one = data.get(number).getPlanetdata();
            String two = horaEntryTime.get(number).getEntertimedata() + "-"
                    + horaExitTime.get(number).getExittimedata();
            String three = data.get(number).getPlanetCurrentHorameaning();
            String[] startTime = horaEntryTime.get(number).getEntertimedata().split(":");
            String[] endTime = horaExitTime.get(number).getExittimedata().split(":");
            //result = "वर्तमान होरा " + data.get(number).getPlanetdata() + " है जो " + data.get(number).getPlanetCurrentHorameaning() + " है, " + startTime[0] + " बजकर " + startTime[1] + " मिनट से शुरू होकर " + endTime[0] + " बजकर " + endTime[1] + " मिनट पर ख़त्म होगा";
            if (subResultCode == 0) {
                result = context.getResources().getString(R.string.hora_str).replace("#hora", data.get(number).getPlanetdata());
            } else {
                result = context.getResources().getString(R.string.tomorrow_hora_str).replace("#hora", data.get(number).getPlanetdata());
            }


            result = result.replace("#result", data.get(number).getPlanetCurrentHorameaning());
            result = result.replace("#starthour", removeZero(startTime[0]));
            result = result.replace("#startminute", removeZero(startTime[1]));
            result = result.replace("#endhour", removeZero(endTime[0]));
            result = result.replace("#endminute", removeZero(endTime[1]));
        } catch (Exception ex) {
            ////MyLogger.errorLog("MainActivity 24 - " + ex.getMessage());
            Log.i("TAG", ex.getMessage());
        }
        return result;
    }

    private String getSadeSatiDetail(int resultCode) {
        String result = "";
        String resultStr = CUtils.getStringData(context, "defaultKundliData", "");
        Log.d("appHangIssue", "getSadeSatiDetail resultStr: "+resultStr);
        try {
            JSONObject mainJsonObject = new JSONObject(resultStr);
            JSONArray jsonArray = mainJsonObject.getJSONArray("ShaniSadeSati");
            if (resultCode == 1) {
                result = getSadeSatiResponse1(jsonArray);
            } else if (resultCode == 2) {
                result = getSadeSatiResponse2(jsonArray);
            } else if (resultCode == 3) {
                result = getSadeSatiResponse3(jsonArray);
            }

        } catch (Exception e) {
            Log.d("appHangIssue", "getSadeSatiDetail ex: "+e);
        }

        Log.d("appHangIssue", "getSadeSatiDetail result: "+result);
        return result;
    }

    private String getSadeSatiResponse1(JSONArray jsonArray) {
        String result = "";
        String[] startDates = new String[3];
        String[] endDates = new String[3];
        Calendar cal = Calendar.getInstance();
        //Date date = getDateFromString("July 12,  2061");
        Date date = cal.getTime();
        try {
            startDates = getStartDateArray(jsonArray);
            endDates = getEndDateArray(jsonArray);
            if (date.before(getDateFromString(startDates[0], "MMM dd, yyyy"))) {
                result = context.getResources().getString(R.string.sade_sati_start_str1).replace("#startdate", startDates[0]);
                //result = "तुम्हारी शनि साढ़े साती " + startDates[0] + " को शुरू होगी";
            } else if (checkDate(date, startDates[0], endDates[0], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str2).replace("#startdate", startDates[0]);
                result = result.replace("#enddate", endDates[0]);
                //result = "तुम्हारी शनि साढ़े साती" + startDates[0] + "को शुरू हो चुकी है जो" + endDates[0] + " को खत्म होगी";
            } else if (checkDate(date, endDates[0], startDates[1], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str1).replace("#startdate", startDates[1]);
                //result = "तुम्हारी शनि साढ़े साती " + startDates[1] + " को शुरू होगी";
            } else if (checkDate(date, startDates[1], endDates[1], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str2).replace("#startdate", startDates[1]);
                result = result.replace("#enddate", endDates[1]);
                //result = "तुम्हारी शनि साढ़े साती" + startDates[1] + "को शुरू हो चुकी है जो" + endDates[1] + " को खत्म होगी";
            } else if (checkDate(date, endDates[1], startDates[2], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str1).replace("#startdate", startDates[2]);
                //result = "तुम्हारी शनि साढ़े साती " + startDates[2] + " को शुरू होगी";
            } else if (checkDate(date, startDates[2], endDates[2], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str2).replace("#startdate", startDates[2]);
                result = result.replace("#enddate", endDates[2]);
                //result = "तुम्हारी शनि साढ़े साती" + startDates[2] + "को शुरू हो चुकी है जो" + endDates[2] + " को खत्म होगी";
            } else if (checkDate(date, endDates[2], startDates[3], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str1).replace("#startdate", startDates[3]);
                //result = "तुम्हारी शनि साढ़े साती " + startDates[3] + " को शुरू होगी";
            } else if (checkDate(date, startDates[3], endDates[3], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_start_str2).replace("#startdate", startDates[3]);
                result = result.replace("#enddate", endDates[3]);
                //result = "तुम्हारी शनि साढ़े साती" + startDates[3] + "को शुरू हो चुकी है जो" + endDates[3] + " को खत्म होगी";
            }


        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 13 - " + e.getMessage());
        }
        return result;
    }

    private String getSadeSatiResponse2(JSONArray jsonArray) {
        String result = "";
        String[] startDates = new String[3];
        String[] endDates = new String[3];
        Calendar cal = Calendar.getInstance();
        //Date date = getDateFromString("July 12,  2061");
        Date date = cal.getTime();
        try {
            startDates = getStartDateArray(jsonArray);
            endDates = getEndDateArray(jsonArray);
            if (date.before(getDateFromString(startDates[0], "MMM dd, yyyy"))) {
                result = context.getResources().getString(R.string.sade_sati_end_str1);
                //result = "तुम्हारी शनि साढ़ेसाती अभी शुरू ही नहीं हुई है";
            } else if (checkDate(date, startDates[0], endDates[0], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str2);
                result = result.replace("#enddate", endDates[0]);
                //result = "तुम्हारी शनि साढ़े साती" + endDates[0] + " को खत्म होगी";
            } else if (checkDate(date, endDates[0], startDates[1], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str1);
                //result = "तुम्हारी शनि साढ़ेसाती अभी शुरू ही नहीं हुई है";
            } else if (checkDate(date, startDates[1], endDates[1], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str2);
                result = result.replace("#enddate", endDates[1]);
                //result = "तुम्हारी शनि साढ़े साती" + endDates[1] + " को खत्म होगी";
            } else if (checkDate(date, endDates[1], startDates[2], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str1);
                //result = "तुम्हारी शनि साढ़ेसाती अभी शुरू ही नहीं हुई है";
            } else if (checkDate(date, startDates[2], endDates[2], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str2);
                result = result.replace("#enddate", endDates[2]);
                //result = "तुम्हारी शनि साढ़े साती" + endDates[2] + " को खत्म होगी";
            } else if (checkDate(date, endDates[2], startDates[3], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str1);
                //result = "तुम्हारी शनि साढ़ेसाती अभी शुरू ही नहीं हुई है";
            } else if (checkDate(date, startDates[3], endDates[3], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.sade_sati_end_str2);
                result = result.replace("#enddate", endDates[3]);
                //result = "तुम्हारी शनि साढ़े साती" + endDates[3] + " को खत्म होगी";
            }


        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 14 - " + e.getMessage());
        }
        return result;
    }

    private String getSadeSatiResponse3(JSONArray jsonArray) {
        String result = "";
        String[] startDates = new String[3];
        String[] endDates = new String[3];
        Calendar cal = Calendar.getInstance();
        //Date date = getDateFromString("July 12,  2061");
        Date date = cal.getTime();
        try {
            startDates = getStartDateArray(jsonArray);
            endDates = getEndDateArray(jsonArray);
            if (date.before(getDateFromString(startDates[0], "MMM dd, yyyy"))) {
                result = context.getResources().getString(R.string.about_sade_sati_str1);
                //result = "नहीं , तुम्हारी साढ़े साती अभी शुरू नहीं हुई है";
            } else if (checkDate(date, startDates[0], endDates[0], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str2);
                result = result.replace("#startdate", startDates[0]);
                //result = "हाँ ,तुम्हारी शनि साढ़े साती " + startDates[0] + " को शुरू हो चुकी है";
            } else if (checkDate(date, endDates[0], startDates[1], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str1);
                // result = "नहीं , तुम्हारी साढ़े साती अभी शुरू नहीं हुई है";
            } else if (checkDate(date, startDates[1], endDates[1], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str2);
                result = result.replace("#startdate", startDates[1]);
                // result = "हाँ ,तुम्हारी शनि साढ़े साती " + startDates[1] + " को शुरू हो चुकी है";
            } else if (checkDate(date, endDates[1], startDates[2], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str1);
                //result = "नहीं , तुम्हारी साढ़े साती अभी शुरू नहीं हुई है";
            } else if (checkDate(date, startDates[2], endDates[2], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str2);
                result = result.replace("#startdate", startDates[2]);
                // result = "हाँ ,तुम्हारी शनि साढ़े साती " + startDates[2] + " को शुरू हो चुकी है";
            } else if (checkDate(date, endDates[2], startDates[3], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str1);
                //result = "नहीं , तुम्हारी साढ़े साती अभी शुरू नहीं हुई है";
            } else if (checkDate(date, startDates[3], endDates[3], "MMM dd, yyyy")) {
                result = context.getResources().getString(R.string.about_sade_sati_str2);
                result = result.replace("#startdate", startDates[3]);
                //result = "हाँ ,तुम्हारी शनि साढ़े साती " + startDates[3] + " को शुरू हो चुकी है";
            }


        } catch (Exception e) {
            Log.d("appHangIssue", "getSadeSatiResponse3 ex: "+e);
        }
        return result;
    }

    private String getMahaDashaDetail(String question) {
        String result = "";
        String resultStr = CUtils.getStringData(context, "defaultKundliData", "");
        try {
            JSONObject mainJsonObject = new JSONObject(resultStr);
            JSONArray jsonArray = mainJsonObject.getJSONArray("Mahadsha");
            ArrayList<String> startDateList = getMahaDashaStartDate(jsonArray);
            ArrayList<String> endDateList = getMahaDashaEndDate(jsonArray);
            ArrayList<String> planetList = getMahaDashaPlanets(jsonArray);
            int currentDasha = getCurrentMahaDasha(startDateList, endDateList);
            String askedPlanet = getAskedPlanet(question);
            int planetIndex;
            if (askedPlanet.equals(getPlanetName(planetList.get(currentDasha)))
                    || askedPlanet.isEmpty()) {
                result = context.getResources().getString(R.string.mahadasha_str1);
                planetIndex = currentDasha;
                askedPlanet = getPlanetName(planetList.get(currentDasha));
            } else {
                result = context.getResources().getString(R.string.mahadasha_str2);
                planetIndex = getPlanetIndex(planetList, askedPlanet);
                if (planetIndex == -1) {
                    result = context.getResources().getString(R.string.mahadasha_str3);
                }
            }
            result = result.replace("#planet", askedPlanet);
            result = result.replace("#startdate", startDateList.get(planetIndex));
            result = result.replace("#enddate", endDateList.get(planetIndex));

        } catch (Exception e) {
            Log.i("", e.getMessage());
        }


        return result;
    }

    private static String getPlanetName(String planet) {
        String planetName = "";
        if (planet.equals("MON")) {
            planetName = "चंद्रमा";
        } else if (planet.equals("MAR")) {
            planetName = "मंगल";
        } else if (planet.equals("MER")) {
            planetName = "बुध";
        } else if (planet.equals("JUP")) {
            planetName = "बृहस्पति";
        } else if (planet.equals("VEN")) {
            planetName = "शुक्र";
        } else if (planet.equals("SAT")) {
            planetName = "शनि";
        } else if (planet.equals("SUN")) {
            planetName = "सूर्य";
        } else if (planet.equals("RAH")) {
            planetName = "राहु";
        } else if (planet.equals("KET")) {
            planetName = "केतु";
        }
        return planetName;
    }

    private int getPlanetIndex(ArrayList<String> planetList, String planet) {
        int index = -1;
        if (planet.equals("चंद्रमा")) {
            index = planetList.indexOf("MON");
        } else if (planet.equals("मंगल")) {
            index = planetList.indexOf("MAR");
        } else if (planet.equals("बुध")) {
            index = planetList.indexOf("MER");
        } else if (planet.equals("बृहस्पति")) {
            index = planetList.indexOf("JUP");
        } else if (planet.equals("शुक्र")) {
            index = planetList.indexOf("VEN");
        } else if (planet.equals("शनि")) {
            index = planetList.indexOf("SAT");
        } else if (planet.equals("सूर्य")) {
            index = planetList.indexOf("SUN");
        } else if (planet.equals("राहु")) {
            index = planetList.indexOf("RAH");
        } else if (planet.equals("केतु")) {
            index = planetList.indexOf("KET");
        }
        return index;
    }

    private String getDateOfBirth() {
        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(context);
        BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
        String[] monthName = context.getResources().getStringArray(R.array.MonthName);
        return beanDateTime.getDay() + ", " + monthName[beanDateTime.getMonth()] + " " + beanDateTime.getYear();
    }

    private String getBirthPlace() {
        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(context);
        BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();

        return beanPlace.getCityName();
    }

    private String getName() {
        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(context);

        return beanHoroPersonalInfo.getName();
    }

    private String getExactHoraTime(String planet, String todayOrTomorrow) {

        List<String> allPlanetName = new ArrayList<>();
        List<String> horaStartTime = new ArrayList<>();
        List<String> horaEndTime = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            String startTime = datatimestart.get(i).getEntertimedata();
            String[] arr = startTime.split(":");
            int hour = Integer.parseInt(arr[0]);

            if (hour >= 8 && hour <= 20) {
                allPlanetName.add(data.get(i).getPlanetdata());
                horaStartTime.add(datatimestart.get(i).getEntertimedata());
                horaEndTime.add(datatimeexit.get(i).getExittimedata());
            }
        }
        Log.d("appHangIssue", "allPlanetName: "+allPlanetName);
        Log.d("appHangIssue", "horaStartTime: "+horaStartTime);
        Log.d("appHangIssue", "horaEndTime: "+horaEndTime);
        return exactAnswer(planet, allPlanetName, horaStartTime, horaEndTime, todayOrTomorrow);
    }

    private String exactAnswer(String planet, List<String> allPlanetName, List<String> horaStartTime, List<String> horaEndTime, String todayOrTomorrow) {
        int index = 0;
        for (int i = 0; i < allPlanetName.size(); i++) {
            if (allPlanetName.get(i).equals(planet)) {
                index = i;
                break;
            }

        }
        String entryTime = horaStartTime.get(index);
        String exitTime = horaEndTime.get(index);
        String planetName = allPlanetName.get(index);

        if (planetName == null) {
            for (int i = allPlanetName.size(); i >= 0; i--) {
                if (allPlanetName.get(i).equals(planet)) {
                    index = i;
                    break;
                }
            }
            entryTime = horaStartTime.get(index);
            exitTime = horaEndTime.get(index);
            planetName = allPlanetName.get(index);
        }

        String[] arrStartTime = entryTime.split(":");
        String[] arrEndTime = exitTime.split(":");
        String hoursStart = arrStartTime[0];
        String minStart = arrStartTime[1];
        String hoursEnd = arrEndTime[0];
        String minEnd = arrEndTime[1];
        String ans = "";

        Log.d("appHangIssue", "exactAnswer: "+context);

        if (todayOrTomorrow.equalsIgnoreCase("today")) {
            ans = context.getResources().getString(R.string.auspious_hora_today);
            //ans = "आपके इस कार्य के लिए #planet की होरा सर्वश्रेष्ठ है जो #entrytimehour बजकर #entrytimeminute मिनट से शुरू होकर  #exittimehour बजकर #exittimeminute मिनट तक होगा";
        } else if (todayOrTomorrow.equalsIgnoreCase("tomorrow")) {
            // ans = "कल के आपके इस कार्य के लिए #planet की होरा सर्वश्रेष्ठ है जो #entrytimehour बजकर #entrytimeminute मिनट से शुरू होकर  #exittimehour बजकर #exittimeminute मिनट तक होगा";
            ans = context.getResources().getString(R.string.auspious_hora_tomorrow);
        }

        Log.d("appHangIssue", "ans 1: "+ans);

        ans = ans.replace("#planet", planetName)
                .replace("#entrytimehour", removeZero(hoursStart))
                .replace("#entrytimeminute", removeZero(minStart))
                .replace("#exittimehour", removeZero(hoursEnd))
                .replace("#exittimeminute", removeZero(minEnd));
        ans = replaceHourAndMinute(ans, Integer.parseInt(hoursStart), Integer.parseInt(minStart));
        ans = replaceHourAndMinute(ans, Integer.parseInt(hoursEnd), Integer.parseInt(minEnd));

        Log.d("appHangIssue", "ans 2: "+ans);

        return ans;
    }

    private String replaceHourAndMinute(String resultStr, int hour, int minute) {
        String ansStr = resultStr;
        if (hour > 1) {
            ansStr = ansStr.replaceFirst("#hour", "hours");
        } else {
            ansStr = ansStr.replaceFirst("#hour", "hour");
        }
        if (minute > 1) {
            ansStr = ansStr.replaceFirst("#minute", "minutes");
        } else {
            ansStr = ansStr.replaceFirst("#minute", "minute");
        }
        return ansStr;
    }

    private String getExactHoraForSubhAsubhPlanet(String planet, String subhAsubh, String todayortomorrow) {

        List<String> allPlanetName = new ArrayList<>();
        List<String> horaStartTime = new ArrayList<>();
        List<String> horaEndTime = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {
            String startTime = datatimestart.get(i).getEntertimedata();
            String[] arr = startTime.split(":");
            int hour = Integer.parseInt(arr[0]);

            if (hour >= 8 && hour <= 20) {
                allPlanetName.add(data.get(i).getPlanetdata());
                horaStartTime.add(datatimestart.get(i).getEntertimedata());
                horaEndTime.add(datatimeexit.get(i).getExittimedata());
            }

        }


        String ans = exactSubhTime(planet, allPlanetName, horaStartTime, horaEndTime, subhAsubh, todayortomorrow);
        return ans;
    }

    public String getExactHoraForSubhAsubhPlanet(String planet) {

        List<String> allPlanetName = new ArrayList<>();
        List<String> horaStartTime = new ArrayList<>();
        List<String> horaEndTime = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {
            String startTime = datatimestart.get(i).getEntertimedata();
            String[] arr = startTime.split(":");
            int hour = Integer.parseInt(arr[0]);

            if (hour >= 8 && hour <= 20) {
                allPlanetName.add(data.get(i).getPlanetdata());
                horaStartTime.add(datatimestart.get(i).getEntertimedata());
                horaEndTime.add(datatimeexit.get(i).getExittimedata());
            }


        }

        String ans = exactSubhTime(planet, allPlanetName, horaStartTime, horaEndTime);
        return ans;
    }

    private String exactSubhTime(String planet, List<String> allPlanetName, List<String> horaStartTime, List<String> horaEndTime, String subhAsubh, String todayortomorrow) {
        int index = 0;
        for (int i = 0; i < allPlanetName.size(); i++) {
            if (allPlanetName.get(i).equals(planet)) {
                index = i;
                break;
            }

        }
        String entryTime = horaStartTime.get(index);
        String exitTime = horaEndTime.get(index);
        String planetName = allPlanetName.get(index);

        if (planetName == null) {
            for (int i = allPlanetName.size(); i >= 0; i--) {
                if (allPlanetName.get(i).equals(planet)) {
                    index = i;
                    break;
                }
            }
            entryTime = horaStartTime.get(index);
            exitTime = horaEndTime.get(index);
            planetName = allPlanetName.get(index);
        }

        String[] arrStartTime = entryTime.split(":");
        String[] arrEndTime = exitTime.split(":");
        String hoursStart = arrStartTime[0];
        String minStart = arrStartTime[1];
        String hoursEnd = arrEndTime[0];
        String minEnd = arrEndTime[1];
        String ans = "";

        if (todayortomorrow.equalsIgnoreCase("today")) {

            if (subhAsubh.equalsIgnoreCase("subh")) {
                ans = context.getResources().getString(R.string.auspious_time_today);
                //ans = "आपके लिए आज सबसे शुभ समय #entrytimehour बजकर #entrytimeminute मिनट से शुरू होकर  #exittimehour बजकर #exittimeminute मिनट तक होगा";
            } else if (subhAsubh.equalsIgnoreCase("asubh")) {
                //ans = "आपके लिए आज सबसे अशुभ समय #entrytimehour बजकर #entrytimeminute मिनट से शुरू होकर  #exittimehour बजकर #exittimeminute मिनट तक होगा";
                ans = context.getResources().getString(R.string.unauspious_time_today);
                //ans = "आपको आज #entrytimehour बजकर #entrytimeminute मिनट से लेकर #exittimehour बजकर #exittimeminute मिनट तक के समय का विशेष ध्यान रखना चाहिए";


            }
        } else if (todayortomorrow.equalsIgnoreCase("tomorrow")) {
            if (subhAsubh.equalsIgnoreCase("subh")) {
                ans = context.getResources().getString(R.string.auspious_time_tomorrow);
                //ans = "कल के लिए आपका सबसे शुभ समय #entrytimehour बजकर #entrytimeminute मिनट से शुरू होकर  #exittimehour बजकर #exittimeminute मिनट तक होगा";
            } else if (subhAsubh.equalsIgnoreCase("asubh")) {
                //ans = "आपके लिए आज सबसे अशुभ समय #entrytimehour बजकर #entrytimeminute मिनट से शुरू होकर  #exittimehour बजकर #exittimeminute मिनट तक होगा";
                ans = context.getResources().getString(R.string.unauspious_time_tomorrow);
                //ans = "कल आपको #entrytimehour बजकर #entrytimeminute मिनट से लेकर #exittimehour बजकर #exittimeminute मिनट तक के समय का विशेष ध्यान रखना चाहिए";


            }

        }
        ans = ans.replace("#entrytimehour", removeZero(hoursStart))
                .replace("#entrytimeminute", removeZero(minStart))
                .replace("#exittimehour", removeZero(hoursEnd))
                .replace("#exittimeminute", removeZero(minEnd));
        ans = replaceHourAndMinute(ans, Integer.parseInt(hoursStart), Integer.parseInt(minStart));
        ans = replaceHourAndMinute(ans, Integer.parseInt(hoursEnd), Integer.parseInt(minEnd));
        return ans;
    }

    private String exactSubhTime(String planet, List<String> allPlanetName, List<String> horaStartTime, List<String> horaEndTime) {
        int index = 0;
        for (int i = 0; i < allPlanetName.size(); i++) {
            if (allPlanetName.get(i).equals(planet)) {
                index = i;
                break;
            }

        }
        String entryTime = horaStartTime.get(index);
        String exitTime = horaEndTime.get(index);
        String planetName = allPlanetName.get(index);

        if (planetName == null) {
            for (int i = allPlanetName.size(); i >= 0; i--) {
                if (allPlanetName.get(i).equals(planet)) {
                    index = i;
                    break;
                }
            }
            entryTime = horaStartTime.get(index);
            exitTime = horaEndTime.get(index);
            planetName = allPlanetName.get(index);
        }
        String ans = removeSecond(entryTime) + " " + context.getResources().getString(R.string.str_to) + " " + removeSecond(exitTime);
        return ans;
    }

    private static String[] getStartDateArray(JSONArray jsonArray) {
        String[] startDates = new String[4];
        try {
            int j = 0;
            JSONObject jsonObject;
            String previousPhase = "";
            String currentPhase = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                currentPhase = jsonObject.optString("Phase");
                if (currentPhase.equalsIgnoreCase("Rising")
                        && (previousPhase.equals("") || previousPhase.equals("Setting"))) {
                    String startDate = jsonObject.optString("Start Date");
                    startDates[j] = startDate;
                    j++;
                }
                previousPhase = currentPhase;
            }
        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 17 - " + e.getMessage());
            Log.i("", e.getMessage());
        }
        return startDates;
    }

    private static String[] getEndDateArray(JSONArray jsonArray) {
        String[] endDates = new String[4];
        try {
            int j = 0;
            JSONObject jsonObject;
            String nextPhase = "";
            String currentPhase = "";
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);
                currentPhase = nextPhase;
                nextPhase = jsonObject.optString("Phase");
                if (currentPhase.equalsIgnoreCase("Setting")
                        && (nextPhase.equals("") || nextPhase.equals("Rising"))) {
                    String endDate = jsonArray.getJSONObject(i - 1).optString("End Date");
                    endDates[j] = endDate;
                    j++;
                } else if (i == jsonArray.length() - 1) {
                    endDates[j] = jsonArray.getJSONObject(i).optString("End Date");
                }


            }
        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 18 - " + e.getMessage());
            Log.i("", e.getMessage());
        }
        return endDates;
    }

    private static ArrayList<String> getMahaDashaStartDate(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getJSONObject(i).getString("MahadashaStartDate"));
            }

        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 10 - " + e.getMessage());
        }

        return arrayList;
    }

    private static ArrayList<String> getMahaDashaEndDate(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getJSONObject(i).getString("MahadashaEndDate"));
            }

        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 10.1 - " + e.getMessage());
        }
        return arrayList;
    }

    private static ArrayList<String> getMahaDashaPlanets(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getJSONObject(i).getString("PlanetName"));
            }

        } catch (Exception e) {
            ////MyLogger.errorLog("MainActivity 11 - " + e.getMessage());
        }
        return arrayList;
    }

    private static int getCurrentMahaDasha(ArrayList<String> startDateList, ArrayList<String> endDateList) {
        int currentDasha = 0;
        for (int i = 0; i < startDateList.size(); i++) {
            if (checkDate(Calendar.getInstance().getTime(), startDateList.get(i), endDateList.get(i), "mm/dd/yyyy")) {
                currentDasha = i;
                break;
            }
        }
        return currentDasha;
    }

    static private String getAskedPlanet(String question) {
        String[] planets = {"सूर्य", "चंद्रमा", "मंगल", "बुध", "बृहस्पति", "शुक्र", "शनि", "राहु", "केतु"};
        String askedPlanet = "";
        for (String planet : planets) {
            if (question.contains(planet)) {
                askedPlanet = planet;
                break;
            }
        }
        return askedPlanet;
    }

    public void SetdataonList(int year, int month, int day, String latitude,
                                     String longitude, String timezone, String todayOrTomorrow) {
        try {
            data = new ArrayList<HoraMetadata>();
            datatimestart = new ArrayList<HoraMetadata>();
            datatimeexit = new ArrayList<HoraMetadata>();

            Calendar ca = Calendar.getInstance();
            int day_of_month;
            if (todayOrTomorrow.equalsIgnoreCase("today")) {
                day_of_month = ca.get(Calendar.DAY_OF_WEEK) - 1;
            } else if (todayOrTomorrow.equalsIgnoreCase("tomorrow")) {
                day_of_month = ca.get(Calendar.DAY_OF_WEEK);
            } else {
                day_of_month = ca.get(Calendar.DAY_OF_WEEK) - 1;
            }
            // //

            HoraLordName(day_of_month);
            HoraEndTime(year, month, day, latitude, longitude, timezone);
        } catch (Exception e) {
            Log.d("appHangIssue", "SetdataonList ex: "+e);
        }
    }

    public List<HoraMetadata> HoraLordName(int day_of_month) {
        try {
            int dayLordForDayHora[] = new int[24];

            String PlanetName[] = context.getResources().getStringArray(
                    R.array.hora_planets);
            String PlanetNameMeaning[] = context.getResources().getStringArray(
                    R.array.hora_planets_meaning);
            String PlanetNameMeaningforcurrentHora[] = context.getResources()
                    .getStringArray(R.array.pla_mean);

            for (int i = 0; i < 24; i++) {
                dayLordForDayHora[i] = (day_of_month + (i * 5)) % 7;
                HoraMetadata hora = new HoraMetadata();
                hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
                hora.setPlanetmeaning(PlanetNameMeaning[dayLordForDayHora[i]]);
                hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[dayLordForDayHora[i]]);
                data.add(hora);
            }
        } catch (Exception e) {
            Log.d("appHangIssue", "HoraLordName ex: "+e);
        }

        return data;
    }

    private double[] HoraEndTime(int year, int month, int day, String latitude, String longitude, String timezone) {
        int jd = (int) Masa.toJulian(year, month + 1, day);
        // System.out.println("LAT LNG TMZNE" + year + month + 1 + day);
        Place place = null;
        try {
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);
            double tzone = Double.parseDouble(timezone);

        if (lat == 0 || lng == 0) {
            lat = 28.36;
            lng = 77.12;
            tzone = +5.5;
        }

        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        place = new Place(lat, lng, tzone);

        for (int i = 0; i < 13; i++) {

            HoraMetadata hora = new HoraMetadata();
            if (i <= 11) {

                hora.setEntertimedata(com.ojassoft.astrosage.utils.CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 12)[i], 0));

                Muhurta.getDayDivisons(jd, place, Masa.getSunRise(jd, place), 8);
                Log.d("horaIssues", "HoraEndTime 1: "+new Gson().toJson(hora));
                datatimestart.add(hora);

            }
            if (i > 0) {
                hora.setExittimedata(com.ojassoft.astrosage.utils.CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 12)[i], 0));
                Log.d("horaIssues", "HoraEndTime 2: "+new Gson().toJson(hora));
                datatimeexit.add(hora);

            }

        }
        for (int j = 0; j < 13; j++) {

            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 11) {
                hora1.setEntertimedata(com.ojassoft.astrosage.utils.CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 12)[j], 0));
                Log.d("horaIssues", "HoraEndTime 3: "+new Gson().toJson(hora1));
                datatimestart.add(hora1);

            }
            if (j > 0) {
                hora1.setExittimedata(com.ojassoft.astrosage.utils.CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 12)[j], 0));
                Log.d("horaIssues", "HoraEndTime 4: "+new Gson().toJson(hora1));
                datatimeexit.add(hora1);

            }

        }
        } catch (Exception e) {
            Log.d("appHangIssue", "SetdataonList ex: "+e);
        }

        return Muhurta
                .getDayDivisons(jd, place, Masa.getSunRise(jd, place), 12);
    }

}
