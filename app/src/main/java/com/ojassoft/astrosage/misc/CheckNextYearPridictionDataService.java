package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckNextYearPridictionDataService extends IntentService implements SendDataBackToComponent {
    public static final String ACTION_KEY = "com.ojassoft.rasipalan.misc.RESULT";
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    String[] rashifal;
    private boolean isSuccess = false;

    public CheckNextYearPridictionDataService() {
        super(CheckNextYearPridictionDataService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplicationContext()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String NEXTYEARLYHoroscopeKey = dateFormat.format(date.getTime());
        String year = String.valueOf(Integer.valueOf(NEXTYEARLYHoroscopeKey) + 1);
        SharedPreferences nextYearlyHoroscope = CUtils.getNextYearPref(this, LANGUAGE_CODE);
        if (!year.equalsIgnoreCase(nextYearlyHoroscope.getString(CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR, ""))) {
            CUtils.saveNextYearPredictionDataToCurrentYearIfAvailable(LANGUAGE_CODE, this, nextYearlyHoroscope);

            if (CUtils.isConnectedWithInternet(this)) {
                try {
                    CUtils.getYearlyPredictinDetailForNextYear(getApplicationContext(), this, LANGUAGE_CODE);
                } catch (Exception e) {
                    isSuccess = false;
                }
            } else {
                CUtils.saveNextYearPridictionPreferences(getApplicationContext(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
            }
        } else {
            String[] yearlyHoroscopeDetail = CUtils.getYearlyHoroscopeDetail(nextYearlyHoroscope, LANGUAGE_CODE);
            if (yearlyHoroscopeDetail != null && !TextUtils.isEmpty(yearlyHoroscopeDetail[1].trim())) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
            CUtils.saveNextYearPridictionPreferences(getApplicationContext(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, isSuccess);
        }

    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        if (!TextUtils.isEmpty(response)) {
            try {
                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {

            }
            try {
                SharedPreferences nextYearlyHoroscope = CUtils.getNextYearPref(CheckNextYearPridictionDataService.this, LANGUAGE_CODE);
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                String NEXTYEARLYHoroscopeKey = dateFormat.format(date.getTime());
                String year = String.valueOf(Integer.valueOf(NEXTYEARLYHoroscopeKey) + 1);

                if (response != null && response.contains("rss version")) {

                    List<CMessage> listMessage = LibCUtils.parseYearlyXML(response);
                    if (listMessage != null && listMessage.size() > 0) {
                        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH || LANGUAGE_CODE == CGlobalVariables.HINDI) {
                            CUtils.saveYearlyRasiPrediction(listMessage, nextYearlyHoroscope, CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR, year);
                        } else {
                            CUtils.saveYearlyRasiPredictionOtherLanguage(listMessage, nextYearlyHoroscope, CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR, year);
                        }
                    }
                    String[] yearlyHoroscopeDetail = CUtils.getYearlyHoroscopeDetail(nextYearlyHoroscope, LANGUAGE_CODE);
                    if (yearlyHoroscopeDetail != null && !TextUtils.isEmpty(yearlyHoroscopeDetail[1].trim())) {
                        isSuccess = true;
                    } else {
                        isSuccess = false;
                    }
                    CUtils.saveNextYearPridictionPreferences(getApplicationContext(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, isSuccess);
                }
            }catch (Exception e){
                e.printStackTrace();
                CUtils.saveNextYearPridictionPreferences(getApplicationContext(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
            }
        }
    }

    @Override
    public void doActionOnError(String response) {
        CUtils.saveNextYearPridictionPreferences(getApplicationContext(), CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
    }
}
