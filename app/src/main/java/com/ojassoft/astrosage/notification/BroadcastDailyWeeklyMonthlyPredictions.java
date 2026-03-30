package com.ojassoft.astrosage.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BroadcastDailyWeeklyMonthlyPredictions extends BroadcastReceiver {

    private final int MY_DAILY_NOTIFICATION_ID = 2013;
    private final int MY_WEEKLY_NOTIFICATION_ID = 2014;
    private final int MY_WEEKLY_LOVE_NOTIFICATION_ID = 2015;
    private final int MY_MONTHLY_NOTIFICATION_ID = 2016;
    boolean successful = false;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("NOTIFIACATION");

        LANGUAGE_CODE = ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014

        new PullAysncTask(context, intent).execute();

    }

    public void fatchNotificationData(Context context, Intent intent) {

        String[] titleAndDescription = new String[2];
        int rashiIndex = 0;

        if (CUtils.getHoroscopeNotificationWant(context)) {
            rashiIndex = CUtils.getMoonSignIndex(context);
            if (CGlobalVariables.DAILY_PRIDICTION_INTENT_ACTION.equals(intent
                    .getAction())) {
                Date date = new Date();
                DateFormat dateFormat = DateFormat.getDateInstance(
                        DateFormat.MEDIUM, Locale.ENGLISH);
                String todaysHoroscopeKey = dateFormat.format(date.getTime());

				/*SharedPreferences todaysHoroscope = context
                        .getSharedPreferences(
								CGlobalVariables.dailyHoroscopePrefName,
								Context.MODE_PRIVATE);*/

				/*
				 * update on 4 feb 2016 (putting the data of hindi HOROSCEOPE in weeklyLoveHoroscopePrefHindiName of English in weeklyLoveHoroscopePrefName Shared Preference)
				 */
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
                } else {
                    todaysHoroscope = context.getSharedPreferences(
                            CGlobalVariables.dailyHoroscopePrefName,
                            Context.MODE_PRIVATE);
                }

                if (!todaysHoroscopeKey.equalsIgnoreCase(todaysHoroscope
                        .getString(CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                ""))) {

                    if (CUtils.isConnectedWithInternet(context)) {
                        // DISABLED BY BIJENDRA ON 17-04-15
						/*
						 * successful = pullAndSaveNewPredictions(context,
						 * todaysHoroscope,
						 * CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
						 * todaysHoroscopeKey,
						 * CGlobalVariables.dailyHoroscopeRssFeedURL);
						 */
                        // ADDED BY BIJENDRA ON 17-04-15
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI
                                && CUtils.isSupportUnicodeHindi())
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    todaysHoroscope,
                                    CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                    todaysHoroscopeKey,
                                    CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeHindi);
                        else if (LANGUAGE_CODE == CGlobalVariables.TAMIL)
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    todaysHoroscope,
                                    CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                    todaysHoroscopeKey,
                                    CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeTamil);
                        else if (LANGUAGE_CODE == CGlobalVariables.MARATHI)
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    todaysHoroscope,
                                    CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                    todaysHoroscopeKey,
                                    CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeMarathi);
                        else if (LANGUAGE_CODE == CGlobalVariables.BANGALI)
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    todaysHoroscope,
                                    CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                    todaysHoroscopeKey,
                                    CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeBengali);
                        else
                            successful = pullAndSaveNewPredictions(context,
                                    todaysHoroscope,
                                    CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY,
                                    todaysHoroscopeKey,
                                    CGlobalVariables.dailyHoroscopeRssFeedURL);
                        // END
                    } else {
                        return;
                    }
                    if (successful) {
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
                                com.libojassoft.android.R.drawable.ic_notification,
                                MY_DAILY_NOTIFICATION_ID);
                    }
                }
                successful = false;
                titleAndDescription = null;

            } else if (CGlobalVariables.WEEKLY_PRIDICTION_INTENT_ACTION
                    .equals(intent.getAction())) {

                //update on 9-feb-2016 ,
				/*Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("w",
						Locale.ENGLISH);
				String weeklyHoroscopeKey = dateFormat.format(date.getTime());*/
                String weeklyHoroscopeKey = String.valueOf(CUtils.getWeekOfYear(Calendar.getInstance()));
				
				/*SharedPreferences weeklyHoroscope = context
						.getSharedPreferences(
								CGlobalVariables.weeklyHoroscopePrefName,
								Context.MODE_PRIVATE);*/
				/*
				 * update on 4 feb 2016 (putting the data of hindi HOROSCEOPE in weeklyHoroscopePrefHindiName and of English in weeklyHoroscopePrefName Shared Preference)
				 */
                SharedPreferences weeklyHoroscope;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    weeklyHoroscope = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyHoroscopePrefHindiName,
                                    Context.MODE_PRIVATE);
                } else {
                    weeklyHoroscope = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyHoroscopePrefName,
                                    Context.MODE_PRIVATE);
                }

                if (!weeklyHoroscopeKey.equalsIgnoreCase(weeklyHoroscope
                        .getString(CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY,
                                ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        // DISABLED BY BIJENDRA ON 17-04-15
						/*
						 * successful = pullAndSaveNewPredictions(context,
						 * weeklyHoroscope,
						 * CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY,
						 * weeklyHoroscopeKey,
						 * CGlobalVariables.weeklyHoroscopeRssFeedURL);
						 */
                        // ADDED BY BIJENDRA ON 17-04-15
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI
                                && CUtils.isSupportUnicodeHindi())
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    weeklyHoroscope,
                                    CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY,
                                    weeklyHoroscopeKey,
                                    CGlobalVariables.weeklyHoroscopeRssFeedURLinUnicodeHindi);
                        else
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    weeklyHoroscope,
                                    CGlobalVariables.WEEKLY_HOROSCEOPE_PREF_KEY,
                                    weeklyHoroscopeKey,
                                    CGlobalVariables.weeklyHoroscopeRssFeedURL);
                        // END

                    } else {
                        return;
                    }
                    if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, weeklyHoroscope);
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
                                com.libojassoft.android.R.drawable.ic_notification,
                                MY_WEEKLY_NOTIFICATION_ID);
                    }

                }
                successful = false;
                titleAndDescription = null;
            } else if (CGlobalVariables.WEEKLYLOVE_PRIDICTION_INTENT_ACTION
                    .equals(intent.getAction())) {

                //update on 9-feb-2016 ,
				/*Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("w",
						Locale.ENGLISH);
				String weeklyLoveHoroscopeKey = dateFormat.format(date
						.getTime());*/// it will return week no in year like on
                // (20-may-2013, MON) it returns 21th and on
                // 19-may it returns 20th week
                String weeklyLoveHoroscopeKey = String.valueOf(CUtils.getWeekOfYear(Calendar.getInstance()));
				/*SharedPreferences todaysHoroscope = context
						.getSharedPreferences(
								CGlobalVariables.weeklyLoveHoroscopePrefName,
								Context.MODE_PRIVATE);*/
				
				/*
				 * update on 4 feb 2016 (putting the data of hindi HOROSCEOPE in weeklyLoveHoroscopePrefHindiName of English in weeklyLoveHoroscopePrefName Shared Preference)
				 */
                SharedPreferences todaysHoroscope;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    todaysHoroscope = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyLoveHoroscopePrefHindiName,
                                    Context.MODE_PRIVATE);
                } else {
                    todaysHoroscope = context
                            .getSharedPreferences(
                                    CGlobalVariables.weeklyLoveHoroscopePrefName,
                                    Context.MODE_PRIVATE);
                }

                if (!weeklyLoveHoroscopeKey
                        .equalsIgnoreCase(todaysHoroscope
                                .getString(
                                        CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY,
                                        ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        // DISABLED BY BIJENDRA ON 17-04-15
						/*
						 * successful = pullAndSaveNewPredictions(context,
						 * todaysHoroscope,
						 * CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY,
						 * weeklyLoveHoroscopeKey,
						 * CGlobalVariables.weeklyLoveHoroscopeRssFeedURL);
						 */
                        // ADDED BY BIJENDRA ON 17-04-15
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI
                                && CUtils.isSupportUnicodeHindi())
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    todaysHoroscope,
                                    CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY,
                                    weeklyLoveHoroscopeKey,
                                    CGlobalVariables.weeklyLoveHoroscopeRssFeedURLinUnicodeHindi);
                        else
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    todaysHoroscope,
                                    CGlobalVariables.WEEKLY_LOVE_HOROSCEOPE_PREF_KEY,
                                    weeklyLoveHoroscopeKey,
                                    CGlobalVariables.weeklyLoveHoroscopeRssFeedURL);
                        // END
                    } else {
                        return;
                    }
                    if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, todaysHoroscope);
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
                                com.libojassoft.android.R.drawable.ic_notification,
                                MY_WEEKLY_LOVE_NOTIFICATION_ID);
                    }
                }
                successful = false;
                titleAndDescription = null;

            } else if (CGlobalVariables.MONTHLY_PRIDICTION_INTENT_ACTION
                    .equals(intent.getAction())) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM",
                        Locale.ENGLISH);
                String MonthlyHoroscopeKey = dateFormat.format(date.getTime());

				/*SharedPreferences monthlyHoroscope = context
						.getSharedPreferences(
								CGlobalVariables.monthlyHoroscopePrefName,
								Context.MODE_PRIVATE);*/
				/*
				 * update on 4 feb 2016 (putting the data of hindi HOROSCEOPE in weeklyLoveHoroscopePrefHindiName of English in weeklyLoveHoroscopePrefName Shared Preference)
				 */

                SharedPreferences monthlyHoroscope;

                if (CUtils.isSupportUnicodeHindi()
                        && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                    monthlyHoroscope = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefHindiName,
                            Context.MODE_PRIVATE);
                } else {
                    monthlyHoroscope = context.getSharedPreferences(
                            CGlobalVariables.monthlyHoroscopePrefName,
                            Context.MODE_PRIVATE);
                }

                if (!MonthlyHoroscopeKey.equalsIgnoreCase(monthlyHoroscope
                        .getString(
                                CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY,
                                ""))) {
                    if (CUtils.isConnectedWithInternet(context)) {
                        // DISABLED BY BIJENDRA ON 17-04-15
                        // successful = pullAndSaveNewPredictions(context,
                        // monthlyHoroscope,
                        // CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY,
                        // MonthlyHoroscopeKey,
                        // CGlobalVariables.monthlyHoroscopeRssFeedURL);
                        // ADDED BY BIJENDRA ON 17-04-15
                        if (CUtils.getLanguageCodeFromPreference(context) == CGlobalVariables.HINDI
                                && CUtils.isSupportUnicodeHindi())
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    monthlyHoroscope,
                                    CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY,
                                    MonthlyHoroscopeKey,
                                    CGlobalVariables.monthlyHoroscopeRssFeedURLinUnicodeHindi);
                        else
                            successful = pullAndSaveNewPredictions(
                                    context,
                                    monthlyHoroscope,
                                    CGlobalVariables.MONTHLY_HOROSCEOPE_PREF_KEY,
                                    MonthlyHoroscopeKey,
                                    CGlobalVariables.monthlyHoroscopeRssFeedURL);
                        // END
                    } else {
                        return;
                    }
                    if (successful) {
                        titleAndDescription = getTitleAndDescription(
                                rashiIndex, monthlyHoroscope);
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
                                com.libojassoft.android.R.drawable.ic_notification,
                                MY_MONTHLY_NOTIFICATION_ID);
                    }
                }
                successful = false;
                titleAndDescription = null;

            }
            // DISABLED BY BIJENDRA ON 17-04-15
			/*
			 * else
			 * if((CUtils.getLanguageCodeFromPreference(context)==CGlobalVariables
			 * .HINDI && CUtils.isSupportUnicodeHindi()) &&
			 * (CGlobalVariables.HINDI_PREDICTION_INTENT_ACTION
			 * .equals(intent.getAction()))||
			 * "android.net.conn.CONNECTIVITY_CHANGE"
			 * .equals(intent.getAction())) {
			 * 
			 * Date date = new Date(); DateFormat dateFormat =
			 * DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.ENGLISH);
			 * String HindiyHoroscopeKey = dateFormat.format(date.getTime());
			 * 
			 * 
			 * SharedPreferences monthlyHoroscope =
			 * context.getSharedPreferences(
			 * CGlobalVariables.dailyHoroscopePrefHindiName,
			 * Context.MODE_PRIVATE);
			 * if(!HindiyHoroscopeKey.equalsIgnoreCase(monthlyHoroscope
			 * .getString(CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY, ""))) {
			 * if(CUtils.isConnectedWithInternet(context)) { successful =
			 * pullAndSaveNewPredictions(context, monthlyHoroscope,
			 * CGlobalVariables.DAILY_HOROSCEOPE_PREF_KEY, HindiyHoroscopeKey,
			 * CGlobalVariables.dailyHoroscopeRssFeedURLinUnicodeHindi); }else {
			 * return; } if(successful) {
			 * 
			 * titleAndDescription = getTitleAndDescription(rashiIndex,
			 * monthlyHoroscope); titleAndDescription[0] =
			 * titleAndDescription[0].replaceAll("\\<.*?>","");
			 * titleAndDescription[1] =
			 * titleAndDescription[1].replaceAll("\\<.*?>","");
			 * showNotificationOnStatusBar( context,
			 * getPendingIntent(context.getApplicationContext(), rashiIndex,
			 * CGlobalVariables.HINDI_TYPE,MY_MONTHLY_NOTIFICATION_ID),
			 * titleAndDescription[1], titleAndDescription[0],
			 * R.drawable.magz_notification_icon, MY_MONTHLY_NOTIFICATION_ID); }
			 * } successful = false; titleAndDescription = null; }
			 */
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())
                && CUtils.getHoroscopeNotificationWant(context)) {
            CUtils.scheduleHoroscopeNotificationsEachHour(context);
        }

    }

    @SuppressWarnings("deprecation")
    private void showNotificationOnStatusBar(Context context,
                                             PendingIntent pendingIntent, String title, String description,
                                             int iconId, int NOTIFICATION_ID) {
        //String CHANNEL_ID = "broadcast_daily_weekly_monthly_prediction_notification";
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        // ADDED BY BIJENDRA ON 14-04-15
        //	Notification notification = new Notification();
        //	notification.icon = iconId;
        //	notification.when = System.currentTimeMillis();
        // END
        description = description.replaceAll("AstroSage.com,", "").trim();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(iconId)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true);
     /*   .setStyle(new Notification.BigTextStyle().bigText(description)).build();*/
		
	/*	Notification notification = new Notification(iconId,title,
		 System.currentTimeMillis());
		 
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		*/
		
	/*	RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.custom_notification);
		contentView.setImageViewResource(R.id.image,
				R.drawable.magz_notification_icon);
		
		//ADDED BY SHELENDRA 06-05-15 
		 description = description.replace("AstroSage.com,", "");
		contentView.setTextViewText(R.id.title, title);
		contentView.setTextViewText(R.id.text, description.trim());
		//END
		notification.contentView = contentView;
		notification.contentIntent = pendingIntent;*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    private PendingIntent getPendingIntent(Context context, int rashiIndex,
                                           int predictionType, int uniqueID) {
        Intent notificationIntent = new Intent(context, DetailedHoroscope.class);
        notificationIntent.putExtra("rashiType", rashiIndex);
        notificationIntent.putExtra("prediction_type", predictionType);
        notificationIntent.putExtra("needToShowAD", true);
        // PendingIntent contentIntent = PendingIntent.getActivity(context,
        // 0,notificationIntent,Intent.FLAG_ACTIVITY_NEW_TASK);//Intent.FLAG_ACTIVITY_NEW_TASK
        // in place of last 0

        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
        boolean success = true;
        List<CMessage> listMessage = null;
        /*try {
            listMessage = new CXmlPullFeedParser(horoscopeTypeURI,context,true).parse();

            if (listMessage != null) {
                CUtils.saveRasiPrediction(listMessage, horoscopePrefType,
                        horoscopeTypeKey, horoscopeTypeKeyValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }*/
        return success;
    }

    private String[] getTitleAndDescription(int SELECTED_MOON_SIGN,
                                            SharedPreferences horoscopePrefType) {
        String[] horoscopeDescAndTitle = new String[2];
        horoscopeDescAndTitle = CUtils.getRasiPrediction(horoscopePrefType,
                SELECTED_MOON_SIGN);
        return horoscopeDescAndTitle;
    }

    class PullAysncTask extends AsyncTask<String, Long, Void> {

        Context context;
        Intent intent;

        public PullAysncTask(Context context, Intent intent) {
            super();
            this.context = context;
            this.intent = intent;
        }

        @Override
        protected Void doInBackground(String... params) {

            fatchNotificationData(context, intent);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

    }

}
