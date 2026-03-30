package com.ojassoft.astrosage.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.customrssfeed.CXmlPullFeedParser;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class DailyWeeklyMonthlyPredictions implements SendDataBackToComponent {

    private final int MY_DAILY_NOTIFICATION_ID = 2013;
    private final int MY_WEEKLY_NOTIFICATION_ID = 2014;
    private final int MY_WEEKLY_LOVE_NOTIFICATION_ID = 2015;
    private final int MY_MONTHLY_NOTIFICATION_ID = 2016;
    //private boolean successful = false;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Context context;
    SharedPreferences horoscopePrefType;
    String horoscopeTypeKey;
    String horoscopeTypeKeyValue;
    String horoscopeTypeURI;
    String intent;
    int rashiIndex = 0;

    public DailyWeeklyMonthlyPredictions(Context context, String intent) {
        this.intent = intent;
        LANGUAGE_CODE = ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        fatchNotificationData(context, intent);
    }

    public void fatchNotificationData(Context context, String intent) {
        //String[] titleAndDescription = new String[2];
        if (CUtils.getHoroscopeNotificationWant(context)) {
            rashiIndex = CUtils.getMoonSignIndex(context);
            if (CGlobalVariables.DAILY_PRIDICTION_INTENT_ACTION.equals(intent)) {
                Date date = new Date();
                DateFormat dateFormat = DateFormat.getDateInstance(
                        DateFormat.MEDIUM, Locale.ENGLISH);
                String todaysHoroscopeKey = dateFormat.format(date.getTime());
                /*
                 * update on 4 feb 2016 (putting the data of hindi HOROSCEOPE in weeklyLoveHoroscopePrefHindiName of English in weeklyLoveHoroscopePrefName Shared Preference)
                 */
                SharedPreferences todaysHoroscope = CUtils.getTodaysHoroscopePref(context, LANGUAGE_CODE);
                if (!todaysHoroscopeKey.equalsIgnoreCase(todaysHoroscope.getString(CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY, ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        String url = CGlobalVariables.dailyHoroscopeRssFeedURL;
                        // ADDED BY BIJENDRA ON 17-04-15
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI && CUtils.isSupportUnicodeHindi()) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeHindi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeTamil;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeMarathi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeBengali;
                        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodekannad;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeTelgu;
                        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeGujarati;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeMalayalam;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeAssammese;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                            url = CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeOdia;
                        }
                    //    Log.e("HOROSCOPE_URLS", "dailyHoroscopeRssFeedURL: "+url );
                        pullAndSaveNewPredictions(context, todaysHoroscope, CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY, todaysHoroscopeKey, url);
                        // END
                    } else {
                        return;
                    }
                   /* if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, todaysHoroscope);
                        titleAndDescription[0] = titleAndDescription[0]
                                .replaceAll("\\<.*?>", "");
                        titleAndDescription[1] = titleAndDescription[1]
                                .replaceAll("\\<.*?>", "");
                        showNotificationOnStatusBar(
                                context,
                                getPendingIntent(
                                        context.getApplicationContext(),
                                        rashiIndex,
                                        CGlobalVariables.DAILY_TYPE,
                                        MY_DAILY_NOTIFICATION_ID),
                                titleAndDescription[1], titleAndDescription[0],
                                R.drawable.ic_notification,
                                MY_DAILY_NOTIFICATION_ID, CGlobalVariables.DAILY_TYPE);
                    }*/
                }
                // successful = false;
                //titleAndDescription = null;

            } else if (CGlobalVariables.WEEKLY_PRIDICTION_INTENT_ACTION.equals(intent)) {

                String weeklyHoroscopeKey = String.valueOf(CUtils.getWeekOfYear(Calendar.getInstance()));
                SharedPreferences weeklyHoroscopePref = CUtils.getWeeklyPreference(context, LANGUAGE_CODE);
                if (!weeklyHoroscopeKey.equalsIgnoreCase(weeklyHoroscopePref.getString(CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY, ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        String url = CGlobalVariables.weeklyHoroscopeRssFeedURL;
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI && CUtils.isSupportUnicodeHindi()) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeHindi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeTamil;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeMarathi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeBengali;
                        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeKannad;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeTelgu;
                        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeGujarati;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeMalayalam;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeOdia;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                            url = CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeAssammese;
                        }
                  //      Log.e("HOROSCOPE_URLS", "weeklyHoroscopeRssFeedURL: "+url );
                        pullAndSaveNewPredictions(context, weeklyHoroscopePref, CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY, weeklyHoroscopeKey, url);
                        // END
                    } else {
                        return;
                    }
                    /*if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, weeklyHoroscopePref);
                        titleAndDescription[0] = titleAndDescription[0]
                                .replaceAll("\\<.*?>", "");
                        titleAndDescription[1] = titleAndDescription[1]
                                .replaceAll("\\<.*?>", "");
                        showNotificationOnStatusBar(
                                context,
                                getPendingIntent(
                                        context.getApplicationContext(),
                                        rashiIndex,
                                        CGlobalVariables.WEEKLY_TYPE,
                                        MY_WEEKLY_NOTIFICATION_ID),
                                titleAndDescription[1], titleAndDescription[0],
                                R.drawable.ic_notification,
                                MY_WEEKLY_NOTIFICATION_ID, CGlobalVariables.WEEKLY_TYPE);
                    }*/

                }
                //successful = false;
                //titleAndDescription = null;
            } else if (CGlobalVariables.WEEKLYLOVE_PRIDICTION_INTENT_ACTION.equals(intent)) {

                String weeklyLoveHoroscopeKey = String.valueOf(CUtils.getWeekOfYear(Calendar.getInstance()));
                /*
                 * update on 4 feb 2016 (putting the data of hindi HOROSCEOPE in weeklyLoveHoroscopePrefHindiName of English in weeklyLoveHoroscopePrefName Shared Preference)
                 */
                SharedPreferences weeklyLoveHoroscopePref = CUtils.getWeeklyLovePref(context, LANGUAGE_CODE);
                if (!weeklyLoveHoroscopeKey.equalsIgnoreCase(weeklyLoveHoroscopePref.getString(CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY, ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        // ADDED BY BIJENDRA ON 17-04-15
                        String url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURL;
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI && CUtils.isSupportUnicodeHindi()) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeHindi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeTamil;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeMarathi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeBengali;
                        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeKannad;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeTelgu;
                        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeGujarati;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeMalayalam;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeOdia;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                            url = CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeAssammese;
                        }
                       // Log.e("HOROSCOPE_URLS", "weeklyLoveHoroscopeRssFeedURL: "+url );
                        pullAndSaveNewPredictions(context, weeklyLoveHoroscopePref, CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY, weeklyLoveHoroscopeKey, url);
                        // END
                    } else {
                        return;
                    }
                    /*if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, weeklyLoveHoroscopePref);
                        titleAndDescription[0] = titleAndDescription[0]
                                .replaceAll("\\<.*?>", "");
                        titleAndDescription[1] = titleAndDescription[1]
                                .replaceAll("\\<.*?>", "");
                        // //Log.e("MY_WEEKLY_LOVE_NOTIFICATION_ID",
                        // String.valueOf(MY_WEEKLY_LOVE_NOTIFICATION_ID));
                        showNotificationOnStatusBar(
                                context,
                                getPendingIntent(
                                        context.getApplicationContext(),
                                        rashiIndex,
                                        CGlobalVariables.WEEKLY_LOVE_TYPE,
                                        MY_WEEKLY_LOVE_NOTIFICATION_ID),
                                titleAndDescription[1], titleAndDescription[0],
                                R.drawable.ic_notification,
                                MY_WEEKLY_LOVE_NOTIFICATION_ID, CGlobalVariables.WEEKLY_LOVE_TYPE);
                    }*/
                }
                //successful = false;
                // titleAndDescription = null;

            } else if (CGlobalVariables.MONTHLY_PRIDICTION_INTENT_ACTION.equals(intent)) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
                String MonthlyHoroscopeKey = dateFormat.format(date.getTime());
                SharedPreferences monthlyHoroscopePref = CUtils.getMonthlyPref(context, LANGUAGE_CODE);
                if (!MonthlyHoroscopeKey.equalsIgnoreCase(monthlyHoroscopePref.getString(
                        CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY, ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        String url = CGlobalVariables.monthlyHoroscopeRssFeedURL;
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI && CUtils.isSupportUnicodeHindi()) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeHindi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeTamil;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeMarathi;
                        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeBengali;
                        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeKannad;
                        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeTelgu;
                        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeGujarati;
                        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeMalayalam;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeASAMMESE;
                        } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                            url = CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeODIA;
                        }
                      //  Log.e("HOROSCOPE_URLS", "monthlyHoroscopeRssFeedURL: "+url );
                        pullAndSaveNewPredictions(context, monthlyHoroscopePref, CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY, MonthlyHoroscopeKey, url);
                        // END
                    } else {
                        return;
                    }
                    /*if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, monthlyHoroscopePref);
                        titleAndDescription[0] = titleAndDescription[0]
                                .replaceAll("\\<.*?>", "");
                        titleAndDescription[1] = titleAndDescription[1]
                                .replaceAll("\\<.*?>", "");
                        // //Log.e("MY_MONTHLY_NOTIFICATION_ID",
                        // String.valueOf(MY_MONTHLY_NOTIFICATION_ID));
                        showNotificationOnStatusBar(
                                context,
                                getPendingIntent(
                                        context.getApplicationContext(),
                                        rashiIndex,
                                        CGlobalVariables.MONTHLY_TYPE,
                                        MY_MONTHLY_NOTIFICATION_ID),
                                titleAndDescription[1], titleAndDescription[0],
                                R.drawable.ic_notification,
                                MY_MONTHLY_NOTIFICATION_ID, CGlobalVariables.MONTHLY_TYPE);
                    }*/
                }
                //successful = false;
                //titleAndDescription = null;

            }
        }
    }

    @SuppressWarnings("deprecation")
    private void showNotificationOnStatusBar(Context context,
                                             PendingIntent pendingIntent, String title, String description,
                                             int iconId, int NOTIFICATION_ID, int notificationType) {


        // ADDED BY BIJENDRA ON 14-04-15
        //	Notification notification = new Notification();
        //	notification.icon = iconId;
        //	notification.when = System.currentTimeMillis();
        // END
        description = description.replaceAll("AstroSage.com,", "").trim();
        //String CHANNEL_ID = "daily_weekly_monthly_prediction_notification";
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(iconId)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        mNotificationManager.notify(NOTIFICATION_ID, notification.build());

        ArrayList<String> arrayList = CUtils.getArrayListStringForHoroscope(context, CGlobalVariables.SAVEHOROSCOPECOUNT);
        if (notificationType == CGlobalVariables.DAILY_TYPE) {
            arrayList.add(CGlobalVariables.DAILY_TYPE_COUNT_DYNAMIC);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);

        } else if (notificationType == CGlobalVariables.WEEKLY_TYPE) {

            arrayList.add(CGlobalVariables.WEEKLY_TYPE_COUNT_DYNAMIC);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);
        } else if (notificationType == CGlobalVariables.WEEKLY_LOVE_TYPE) {
            arrayList.add(CGlobalVariables.WEEKLY_LOVE_TYPE_COUNT_DYNAMIC);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);

        } else if (notificationType == CGlobalVariables.MONTHLY_TYPE) {
            arrayList.add(CGlobalVariables.MONTHLY_TYPE_COUNT_DYNAMIC);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);

        }


    }

    private PendingIntent getPendingIntent(Context context, int rashiIndex,
                                           int predictionType, int uniqueID) {
        Intent notificationIntent = new Intent(context, DetailedHoroscope.class);
        notificationIntent.putExtra("rashiType", rashiIndex);
        notificationIntent.putExtra("prediction_type", predictionType);
        notificationIntent.putExtra("fromNotification", true);
        notificationIntent.putExtra("needToShowAD",true);
        notificationIntent.putExtra("dynamicnotification", CGlobalVariables.dynamic_horscope_measurment);
        // PendingIntent contentIntent = PendingIntent.getActivity(context,
        // 0,notificationIntent,Intent.FLAG_ACTIVITY_NEW_TASK);//Intent.FLAG_ACTIVITY_NEW_TASK
        // in place of last 0
        PendingIntent contentIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(context, uniqueID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(context, uniqueID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        // in
        // place
        // of
        // last
        // 0
        // //Log.e("predictionType-1", String.valueOf(predictionType));
        return contentIntent;
    }

    private boolean pullAndSaveNewPredictions(Context context,
                                              SharedPreferences horoscopePrefType, String horoscopeTypeKey,
                                              String horoscopeTypeKeyValue, String horoscopeTypeURI) {
        this.context = context;
        this.horoscopePrefType = horoscopePrefType;
        this.horoscopeTypeKey = horoscopeTypeKey;
        this.horoscopeTypeKeyValue = horoscopeTypeKeyValue;
        this.horoscopeTypeURI = horoscopeTypeURI;
        boolean success = true;
        /*List<CMessage> listMessage = null;
        try {
            listMessage = new CXmlPullFeedParser(horoscopeTypeURI, context, true).parse();

            if (listMessage != null) {
                CUtils.saveRasiPrediction(listMessage, horoscopePrefType,
                        horoscopeTypeKey, horoscopeTypeKeyValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }*/
        new CXmlPullFeedParser(horoscopeTypeURI, context, DailyWeeklyMonthlyPredictions.this, true).getDataFromServer(context, horoscopeTypeURI, CUtils.getParamsForTodayRashifal(context), 0);
        return success;
    }

    private String[] getTitleAndDescription(int SELECTED_MOON_SIGN,
                                            SharedPreferences horoscopePrefType) {
        String[] horoscopeDescAndTitle = new String[2];
        horoscopeDescAndTitle = CUtils.getRasiPrediction(horoscopePrefType,
                SELECTED_MOON_SIGN);
        return horoscopeDescAndTitle;
    }


    @Override
    public void doActionAfterGetResult(String response, int method) {
        boolean success = true;
        String[] titleAndDescription = new String[2];
        if (!TextUtils.isEmpty(response)) {
            try {
                response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {

            }
            try {
                List<CMessage> listMessage = LibCUtils.parseXML(response);
                if (listMessage != null) {
                    CUtils.saveRasiPrediction(listMessage, horoscopePrefType,
                            horoscopeTypeKey, horoscopeTypeKeyValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }

            if (success) {
                int notificationType = -1;
                int notificationId = -1;
                titleAndDescription = getTitleAndDescription(
                        rashiIndex, horoscopePrefType);
                titleAndDescription[0] = titleAndDescription[0]
                        .replaceAll("\\<.*?>", "");
                titleAndDescription[1] = titleAndDescription[1]
                        .replaceAll("\\<.*?>", "");
                if (CGlobalVariables.DAILY_PRIDICTION_INTENT_ACTION.equals(intent)) {
                    notificationType = CGlobalVariables.DAILY_TYPE;
                    notificationId = MY_DAILY_NOTIFICATION_ID;
                } else if (CGlobalVariables.WEEKLY_PRIDICTION_INTENT_ACTION.equals(intent)) {
                    notificationType = CGlobalVariables.WEEKLY_TYPE;
                    notificationId = MY_WEEKLY_NOTIFICATION_ID;
                } else if (CGlobalVariables.WEEKLYLOVE_PRIDICTION_INTENT_ACTION.equals(intent)) {
                    notificationType = CGlobalVariables.WEEKLY_LOVE_TYPE;
                    notificationId = MY_WEEKLY_LOVE_NOTIFICATION_ID;
                } else if (CGlobalVariables.MONTHLY_PRIDICTION_INTENT_ACTION.equals(intent)) {
                    notificationType = CGlobalVariables.MONTHLY_TYPE;
                    notificationId = MY_MONTHLY_NOTIFICATION_ID;
                }
                showNotificationOnStatusBar(context, getPendingIntent(context.getApplicationContext(),
                        rashiIndex, notificationType, notificationId),
                        titleAndDescription[1], titleAndDescription[0],
                        com.libojassoft.android.R.drawable.ic_notification, notificationId, notificationType);
            }
        }

    }

    @Override
    public void doActionOnError(String response) {

    }
}
