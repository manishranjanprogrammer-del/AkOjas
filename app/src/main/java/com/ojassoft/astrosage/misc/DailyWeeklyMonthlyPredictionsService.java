package com.ojassoft.astrosage.misc;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.customrssfeed.CXmlPullFeedParser;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;


public class DailyWeeklyMonthlyPredictionsService extends Service {

    Context context;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        fatchAndSaveDailiyRasiPrediction();
        return START_STICKY;
    }

    private void fatchAndSaveDailiyRasiPrediction() {

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(
                DateFormat.MEDIUM, Locale.ENGLISH);
        String todaysHoroscopeKey = dateFormat.format(date.getTime());

        SharedPreferences todaysHoroscope = getSharedPreferences(
                CGlobalVariables.dailyHoroscopePrefName,
                Context.MODE_PRIVATE);
        if (!todaysHoroscopeKey.equalsIgnoreCase(todaysHoroscope
                .getString(CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                        ""))) {
            /*//Log.e("Bijendra", "In service");*/
            if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI
                    && CUtils.isSupportUnicodeHindi())
                pullAndSaveNewPredictions(
                        context,
                        todaysHoroscope,
                        CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                        todaysHoroscopeKey,
                        CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeHindi);
            else
                pullAndSaveNewPredictions(context,
                        todaysHoroscope,
                        CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                        todaysHoroscopeKey,
                        CGlobalVariables.dailyHoroscopeRssFeedURL);
        }
        stopSelf();
    }

    private boolean pullAndSaveNewPredictions(Context context,
                                              SharedPreferences horoscopePrefType, String horoscopeTypeKey,
                                              String horoscopeTypeKeyValue, String horoscopeTypeURI) {
        boolean success = true;
        ////Log.e("Bijendra", "fatch data");
        List<CMessage> listMessage = null;
        try {
            //listMessage = new CXmlPullFeedParser(horoscopeTypeURI,context,true).parse();

            if (listMessage != null) {
                CUtils.saveRasiPrediction(listMessage, horoscopePrefType,
                        horoscopeTypeKey, horoscopeTypeKeyValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
