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
import androidx.core.app.NotificationCompat;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ojas-20 on 4/12/17.
 */

public class StaticDailyWeeklyMonthlyPredictions {
    private final int MY_DAILY_NOTIFICATION_ID = 2013;
    private final int MY_WEEKLY_NOTIFICATION_ID = 2014;
    private final int MY_WEEKLY_LOVE_NOTIFICATION_ID = 2015;
    private final int MY_MONTHLY_NOTIFICATION_ID = 2016;
    private final int MY_YEARLY_NOTIFICATION_ID = 2017;
    //boolean successful = false;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    public StaticDailyWeeklyMonthlyPredictions(Context context, String intent) {
        System.out.println("NOTIFIACATION");

        LANGUAGE_CODE = ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014

        CUtils.getRobotoFont(context.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);

        fatchNotificationData(context, intent);

    }

    /**
     * This method is used to filter and show the notification of Daily, Weekly, Weekly Love and Monthly
     *
     * @param context
     * @param intent
     */
    public void fatchNotificationData(Context context, String intent) {

        int rashiIndex = 0;
        String dailyKey = "MY_DAILY_NOTIFICATION_Key";
        String weeklyKey = "MY_WEEKLY_NOTIFICATION_Key";
        String weeklyLoveKey = "MY_WEEKLY_LOVE_NOTIFICATION_Key";
        String monthlyKey = "MY_MONTHLY_NOTIFICATION_Key";
        String yearlyKey = "MY_YEARLY_NOTIFICATION_Key";

        if (CUtils.getHoroscopeNotificationWant(context)) {
            rashiIndex = CUtils.getMoonSignIndex(context);
            if (CGlobalVariables.DAILY_PRIDICTION_INTENT_ACTION.equals(intent)) {
                Date date = new Date();
                DateFormat dateFormat = DateFormat.getDateInstance(
                        DateFormat.MEDIUM, Locale.ENGLISH);
                String todaysHoroscopeKey = dateFormat.format(date.getTime());

                SharedPreferences todaysHoroscope;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefHindiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefTamilName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefMarathiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefBangaliName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefkannadName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefTelguName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefGujaratiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefMalayalamName,
                            Context.MODE_PRIVATE);
                } else {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefName,
                            Context.MODE_PRIVATE);
                }

                String dailyValue = CUtils.getStringData(context, dailyKey, "");
                if ((!todaysHoroscopeKey.equalsIgnoreCase(todaysHoroscope
                        .getString(CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                ""))) && !todaysHoroscopeKey.equals(dailyValue)) {

                    /*String currentDate = "";
                    try {
                        currentDate = new SimpleDateFormat("dd MMM yyyy").format(date);
                    }catch (Exception ex){
                        //
                    }*/

                    String dailyHoroscopeData = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeDaily, "");
                    if (!dailyHoroscopeData.equals("")) {
                        String langKkey = CUtils.getLanguageKey(LANGUAGE_CODE);
                        String[] arr = CUtils.getLanguageBasedHoroscopeNotificationData(dailyHoroscopeData, langKkey, "daily");

                        if (arr != null && arr[0] != null && !arr[0].equals("")) {
                            //String str = context.getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiIndex];

                            String title = replaceString(context, arr[0], rashiIndex, CGlobalVariables.DAILY_TYPE);
                            String desc = replaceString(context, arr[1], rashiIndex, CGlobalVariables.DAILY_TYPE);

                            showNotificationOnStatusBar(
                                    context,
                                    getPendingIntent(
                                            context.getApplicationContext(),
                                            rashiIndex,
                                            CGlobalVariables.DAILY_TYPE,
                                            MY_DAILY_NOTIFICATION_ID),
                                    title, desc,
                                    com.libojassoft.android.R.drawable.ic_notification,
                                    MY_DAILY_NOTIFICATION_ID, CGlobalVariables.DAILY_TYPE);

                            CUtils.saveStringData(context, dailyKey, todaysHoroscopeKey);
                        }
                    }
                }

            } else if (CGlobalVariables.WEEKLY_PRIDICTION_INTENT_ACTION
                    .equals(intent)) {

                String weeklyHoroscopeKey = String.valueOf(CUtils.getWeekOfYear(Calendar.getInstance()));

                SharedPreferences weeklyHoroscopePref;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    weeklyHoroscopePref = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyHoroscopePrefHindiName,
                                    Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefTamilName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefMarathiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefBangaliName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefKannadName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefTelguName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefGujaratiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefMalayalamName,
                            Context.MODE_PRIVATE);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefAssammeseName,
                            Context.MODE_PRIVATE);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    weeklyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyHoroscopePrefOdiaName,
                            Context.MODE_PRIVATE);
                } else {
                    weeklyHoroscopePref = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyHoroscopePrefName,
                                    Context.MODE_PRIVATE);
                }

                String weeklyValue = CUtils.getStringData(context, weeklyKey, "");

                if ((!weeklyHoroscopeKey.equalsIgnoreCase(weeklyHoroscopePref
                        .getString(CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY,
                                ""))) && !weeklyHoroscopeKey.equals(weeklyValue)) {

                    String weeklyHoroscopeData = "";
                    String langKkey = CUtils.getLanguageKey(LANGUAGE_CODE);
                    if (langKkey.equalsIgnoreCase("en")
                            || langKkey.equalsIgnoreCase("hi")
                            || langKkey.equalsIgnoreCase("bn")
                            || langKkey.equalsIgnoreCase("ml")) {
                        weeklyHoroscopeData = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeWeekly, "");
                    }else{
                        weeklyHoroscopeData = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeWeeklyV2, "");
                    }

                    if (!weeklyHoroscopeData.equals("")) {

                        String[] arr = CUtils.getLanguageBasedHoroscopeNotificationData(weeklyHoroscopeData, langKkey, "weekly");

                        if (arr != null && arr[0] != null && !arr[0].equals("")) {
                            String title = replaceString(context, arr[0], rashiIndex, CGlobalVariables.WEEKLY_TYPE);
                            String desc = replaceString(context, arr[1], rashiIndex, CGlobalVariables.WEEKLY_TYPE);

                            showNotificationOnStatusBar(
                                    context,
                                    getPendingIntent(
                                            context.getApplicationContext(),
                                            rashiIndex,
                                            CGlobalVariables.WEEKLY_TYPE,
                                            MY_WEEKLY_NOTIFICATION_ID),
                                    title, desc,
                                    com.libojassoft.android.R.drawable.ic_notification,
                                    MY_WEEKLY_NOTIFICATION_ID, CGlobalVariables.WEEKLY_TYPE);

                            CUtils.saveStringData(context, weeklyKey, weeklyHoroscopeKey);
                        }
                    }

                }
            } else if (CGlobalVariables.WEEKLYLOVE_PRIDICTION_INTENT_ACTION
                    .equals(intent)) {

                String weeklyLoveHoroscopeKey = String.valueOf(CUtils.getWeekOfYear(Calendar.getInstance()));

                SharedPreferences weeklyLoveHoroscopePref;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    weeklyLoveHoroscopePref = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyLoveHoroscopePrefHindiName,
                                    Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefTamilName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefMarathiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefBangaliName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefKannadName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefTelguName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefGujaratiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefMalayalamName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefAssammeseName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    weeklyLoveHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.weeklyLoveHoroscopePrefOdiaName,
                            Context.MODE_PRIVATE);
                } else {
                    weeklyLoveHoroscopePref = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyLoveHoroscopePrefName,
                                    Context.MODE_PRIVATE);
                }

                String weeklyLoveValue = CUtils.getStringData(context, weeklyLoveKey, "");

                if ((!weeklyLoveHoroscopeKey
                        .equalsIgnoreCase(weeklyLoveHoroscopePref
                                .getString(
                                        CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY,
                                        ""))) && !weeklyLoveHoroscopeKey.equals(weeklyLoveValue)) {

                    String staticHoroscopeWeeklyLove = "";

                    String langKkey = CUtils.getLanguageKey(LANGUAGE_CODE);
                    if (langKkey.equalsIgnoreCase("en")
                            || langKkey.equalsIgnoreCase("hi")
                            || langKkey.equalsIgnoreCase("bn")
                            || langKkey.equalsIgnoreCase("ml")) {
                        staticHoroscopeWeeklyLove = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeWeeklyLove, "");
                    }else{
                        staticHoroscopeWeeklyLove = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeWeeklyLoveV2, "");
                    }

                    if (!staticHoroscopeWeeklyLove.equals("")) {
                        String[] arr = CUtils.getLanguageBasedHoroscopeNotificationData(staticHoroscopeWeeklyLove, langKkey, "weeklylove");

                        if (arr != null && arr[0] != null && !arr[0].equals("")) {
                            String title = replaceString(context, arr[0], rashiIndex, CGlobalVariables.WEEKLY_LOVE_TYPE);
                            String desc = replaceString(context, arr[1], rashiIndex, CGlobalVariables.WEEKLY_LOVE_TYPE);

                            showNotificationOnStatusBar(
                                    context,
                                    getPendingIntent(
                                            context.getApplicationContext(),
                                            rashiIndex,
                                            CGlobalVariables.WEEKLY_LOVE_TYPE,
                                            MY_WEEKLY_LOVE_NOTIFICATION_ID),
                                    title, desc,
                                    com.libojassoft.android.R.drawable.ic_notification,
                                    MY_WEEKLY_LOVE_NOTIFICATION_ID, CGlobalVariables.WEEKLY_LOVE_TYPE);

                            CUtils.saveStringData(context, weeklyLoveKey, weeklyLoveHoroscopeKey);
                        }
                    }
                }
            } else if (CGlobalVariables.MONTHLY_PRIDICTION_INTENT_ACTION
                    .equals(intent)) {
                Date date = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM",
                        Locale.ENGLISH);

                String MonthlyHoroscopeKey = dateFormat.format(date.getTime());

                SharedPreferences monthlyHoroscopePref;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefHindiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefTamilName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefMarathiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefBangaliName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefKannadName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefTelguName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefGujaratiName,
                            Context.MODE_PRIVATE);
                } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefMalayalamName,
                            Context.MODE_PRIVATE);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefAssammeseName,
                            Context.MODE_PRIVATE);
                }  else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefOdiaName,
                            Context.MODE_PRIVATE);
                } else {
                    monthlyHoroscopePref = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefName,
                            Context.MODE_PRIVATE);
                }

                String monthlyValue = CUtils.getStringData(context, monthlyKey, "");

                if ((!MonthlyHoroscopeKey.equalsIgnoreCase(monthlyHoroscopePref
                        .getString(
                                CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY,
                                ""))) && !MonthlyHoroscopeKey.equals(monthlyValue)) {

                    String staticHoroscopeMonth = "";

                    String langKkey = CUtils.getLanguageKey(LANGUAGE_CODE);
                    if (langKkey.equalsIgnoreCase("en")
                            || langKkey.equalsIgnoreCase("hi")
                            || langKkey.equalsIgnoreCase("bn")
                            || langKkey.equalsIgnoreCase("ml")) {
                        staticHoroscopeMonth = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeMonthly, "");
                    } else {
                        staticHoroscopeMonth = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeMonthlyV2, "");
                    }

                    if (!staticHoroscopeMonth.equals("")) {

                        String[] arr = CUtils.getLanguageBasedHoroscopeNotificationData(staticHoroscopeMonth, langKkey, "monthly");

                        if (arr != null && arr[0] != null && !arr[0].equals("")) {
                            String title = replaceString(context, arr[0], rashiIndex, CGlobalVariables.MONTHLY_TYPE);
                            String desc = replaceString(context, arr[1], rashiIndex, CGlobalVariables.MONTHLY_TYPE);

                            showNotificationOnStatusBar(
                                    context,
                                    getPendingIntent(
                                            context.getApplicationContext(),
                                            rashiIndex,
                                            CGlobalVariables.MONTHLY_TYPE,
                                            MY_MONTHLY_NOTIFICATION_ID),
                                    title, desc,
                                    com.libojassoft.android.R.drawable.ic_notification,
                                    MY_MONTHLY_NOTIFICATION_ID, CGlobalVariables.MONTHLY_TYPE);

                            CUtils.saveStringData(context, monthlyKey, MonthlyHoroscopeKey);
                        }
                    }
                }
            }

            // Check for yearly horoscope data

            try {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy",
                        Locale.ENGLISH);
                String YEARLYHoroscopeKey = dateFormat.format(date.getTime());

                String customDate = "01/01/";

                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy",
                        Locale.ENGLISH);
                String customDate1 = dateFormat1.format(date.getTime());

                if (customDate1.contains(customDate)) {

                    String yearlyValue = CUtils.getStringData(context, yearlyKey, "");

                    if (!YEARLYHoroscopeKey.equals(yearlyValue)) {

                        String staticHoroscopeYear = CUtils.getStringData(context, CGlobalVariables.staticHoroscopeYearly, "");
                        if (!staticHoroscopeYear.equals("")) {
                            String langKkey = CUtils.getLanguageKey(LANGUAGE_CODE);
                            String[] arr = CUtils.getLanguageBasedHoroscopeNotificationData(staticHoroscopeYear, langKkey, "yearly");

                            if (arr != null && arr[0] != null && !arr[0].equals("")) {
                                String title = replaceString(context, arr[0], rashiIndex, CGlobalVariables.YEARLY_TYPE);
                                String desc = replaceString(context, arr[1], rashiIndex, CGlobalVariables.YEARLY_TYPE);
                                updateYearlyPredictinDetail(context);
                                CUtils.saveNextYearPridictionPreferences(context, CGlobalVariables.YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN, false);
                                showNotificationOnStatusBar(
                                        context,
                                        getPendingIntent(
                                                context.getApplicationContext(),
                                                rashiIndex,
                                                CGlobalVariables.YEARLY_TYPE,
                                                MY_YEARLY_NOTIFICATION_ID),
                                        title, desc,
                                        com.libojassoft.android.R.drawable.ic_notification,
                                        MY_YEARLY_NOTIFICATION_ID, CGlobalVariables.YEARLY_TYPE);

                                CUtils.saveStringData(context, yearlyKey, YEARLYHoroscopeKey);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                //
            }
            // }
        }
    }

    @SuppressWarnings("deprecation")
    private void showNotificationOnStatusBar(Context context,
                                             PendingIntent pendingIntent, String title, String description,
                                             int iconId, int NOTIFICATION_ID, int notificationType) {
        description = description.replaceAll("AstroSage.com,", "").trim();
        //String CHANNEL_ID = "my_channel_01";// The id of the channel.

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
            arrayList.add(CGlobalVariables.DAILY_TYPE_COUNT);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);

        } else if (notificationType == CGlobalVariables.WEEKLY_TYPE) {

            arrayList.add(CGlobalVariables.WEEKLY_TYPE_COUNT);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);
        } else if (notificationType == CGlobalVariables.WEEKLY_LOVE_TYPE) {
            arrayList.add(CGlobalVariables.WEEKLY_LOVE_TYPE_COUNT);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);

        } else if (notificationType == CGlobalVariables.MONTHLY_TYPE) {
            arrayList.add(CGlobalVariables.MONTHLY_TYPE_COUNT);
            CUtils.saveArrayListStringForHoroscope(context, arrayList, CGlobalVariables.SAVEHOROSCOPECOUNT);

        }


    }

    private PendingIntent getPendingIntent(Context context, int rashiIndex,
                                           int predictionType, int uniqueID) {
        Intent notificationIntent = new Intent(context, DetailedHoroscope.class);
        notificationIntent.putExtra("rashiType", rashiIndex);
        notificationIntent.putExtra("prediction_type", predictionType);
        notificationIntent.putExtra("fromNotification", true);
        notificationIntent.putExtra("needToShowAD", true);
        notificationIntent.putExtra("staticnotification", CGlobalVariables.static_horscope_measurment);
        // notificationIntent.putExtra("fromNotification",true);

        PendingIntent contentIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(context, uniqueID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(context, uniqueID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        return contentIntent;
    }

    private String replaceString(Context context, String data, int rashiIndex, int isDaily) {
        try {
            String rashi = "";
            if (isDaily == CGlobalVariables.DAILY_TYPE) {
                rashi = context.getResources().getStringArray(R.array.rasiname_list_share_whatsapp)[rashiIndex];
            } else {
                rashi = context.getResources().getStringArray(R.array.horo_rasi_full_name_list)[rashiIndex];
            }

            if (data.contains("$")) {
                data = data.replace("$", rashi);
            }

            if (data.contains("#")) {
                Date date = new Date();
                String locale = "";
                String currentDate = "";
                if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    currentDate = new SimpleDateFormat("dd MMM yyyy").format(date);
                } else {
                    currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(date);
                }
                data = data.replace("#", currentDate);
            }

            if (data.contains("@")) {
                String month = "";
                if (isDaily == CGlobalVariables.DAILY_TYPE) {
                    month = context.getResources().getStringArray(R.array.WhatsAppMonthName)[new Date().getMonth()];
                } else {
                    month = context.getResources().getStringArray(R.array.MonthName_en_hi)[new Date().getMonth()];
                }
                data = data.replace("@", month);
            }

            if (data.contains("^")) {
                Date date = new Date();
                String currentDate = "";
                if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    currentDate = new SimpleDateFormat("yyyy").format(date);
                } else {
                    currentDate = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);
                }
                data = data.replace("^", currentDate);
            }
        } catch (Exception ex) {
            //
        }

        return data;
    }

    private void updateYearlyPredictinDetail(Context context) {

        SharedPreferences nextYearlyHoroscope;

        if (LANGUAGE_CODE == 0) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 1) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearHindi,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 2) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearTamil,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 4) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearKannad,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 5) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearTelugu,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 6) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearBangla,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 7) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearGujrati,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 8) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMalayalam,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == 9) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearMarathi,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.ASAMMESSE) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearAssammese,
                    Context.MODE_PRIVATE);
        } else if (LANGUAGE_CODE == CGlobalVariables.ODIA) {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYearOdia,
                    Context.MODE_PRIVATE);
        } else {
            nextYearlyHoroscope = context.getSharedPreferences(
                    CGlobalVariables.YearlyHoroscopePrefNameNextYear,
                    Context.MODE_PRIVATE);
        }

        CUtils.saveNextYearPredictionDataToCurrentYearIfAvailable(LANGUAGE_CODE, context, nextYearlyHoroscope);

    }

}
