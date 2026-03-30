package com.ojassoft.astrosage.ui.JobServices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class BirthdayWishService extends Worker {

    private Context context;

    public BirthdayWishService(@NonNull Context appContext, @NonNull WorkerParameters params) {
        super(appContext, params);
        context = appContext;
    }

    public static String convertDateStringInDifferentFormat(String startDate, String formate1, String formate2) {
        Date date = CUtils.convertStringToDate(startDate, formate1);// "dd/MM/yy"
        Format formatter = new SimpleDateFormat(formate2);
        String dateStr = formatter.format(date);
        return dateStr;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (context == null) {
            context = AstrosageKundliApplication.getAppContext();
        }
        populateBirthdayNotification();
        return Result.success();
    }

    @Override
    public void onStopped() {

    }

    private void populateBirthdayNotification() {
        //Get default kundli
        BeanHoroPersonalInfo beanHoroPersonalInfo = null;
        Object object = CUtils.getCustomObject(context);
        if (object instanceof BeanHoroPersonalInfo) { // by abhishek on 20/03/2019
            beanHoroPersonalInfo = (BeanHoroPersonalInfo) object;
        }

        int languageCode = ((AstrosageKundliApplication) this.getApplicationContext())
                .getLanguageCode();
        CUtils.getRobotoFont(getApplicationContext(), languageCode, CGlobalVariables.medium);

        if (beanHoroPersonalInfo != null) {
            Calendar c = Calendar.getInstance();
            //show birthday notification
            showNotificationForBirthday(context, c, beanHoroPersonalInfo);
            //show mahadasha notification
            showNotificationForMahaDasha(context, c, getName(beanHoroPersonalInfo.getName()), languageCode);
            //show sade sati notification
            showNotificationForSadesati(context, c, getName(beanHoroPersonalInfo.getName()), languageCode);
            //show antardasha notification
            showNotificationForAnterDasha(context, c, getName(beanHoroPersonalInfo.getName()), languageCode);
        }
    }

    private void showNotificationForBirthday(Context context, Calendar calendar, BeanHoroPersonalInfo beanHoroPersonalInfo) {
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        if (date == beanHoroPersonalInfo.getDateTime().getDay()
                && month == beanHoroPersonalInfo.getDateTime()
                .getMonth()) {
            showHappyBirthDayNotification(context, beanHoroPersonalInfo.getName(), beanHoroPersonalInfo);
        }
    }

    /**
     * this function is used to show birthday notification.
     *
     * @param context
     * @param name
     */
    @SuppressLint("NewApi")
    private void showHappyBirthDayNotification(Context context,
                                               String name, BeanHoroPersonalInfo beanHoroPersonalInfo) {
        showHappyBirthDayNotificationInternal(context, name, beanHoroPersonalInfo);
    }

    /**
     * Builds and posts the birthday notification using the same analytics and click-tracking flow as production.
     *
     * @param context context used to create pending intents and notification manager objects
     * @param name user name rendered in the notification title
     */
    @SuppressLint("NewApi")
    private static void showHappyBirthDayNotificationInternal(Context context, String name,
                                                              BeanHoroPersonalInfo beanHoroPersonalInfo) {
        //String CHANNEL_ID = "birthday_notification";
        Intent notificationIntent = new Intent(context, HomeInputScreen.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_VARSHAPHAL);
        attachNotificationKundliPayload(context, notificationIntent, beanHoroPersonalInfo);

        int notificationId = LibCUtils.getRandomNumber();
        PendingIntent contentIntent = createBirthdayWishClickPendingIntent(context, notificationIntent, notificationId,
                CGlobalVariables.BIRTHDAY_INTENT_ACTION);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.happy_birthday) + " " + name)
                .setContentText(context.getResources().getString(R.string.happy_birthday_msg))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setStyle((new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(
                        R.string.happy_birthday_msg))));


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


        mNotificationManager.notify(1004, notification.build());
        logBirthdayWishNotificationShown(CGlobalVariables.BIRTHDAY_INTENT_ACTION);
    }

    private void showNotificationForMahaDasha(Context context, Calendar calendar, String name, int languageCode) {
        try {

            int currentIndex = CUtils.getIntData(context, "currentMahadasha", 0);
            String jsonString = CUtils.getStringData(context, "defaultKundliData", "");
            String planetName = getPlanetNameInLocalLang(CUtils.getStringData(context, "PlanetName", ""));
            String startDate = CUtils.getStringData(context, "MahadashaStartDate", "");
            String endDate = CUtils.getStringData(context, "MahadashaEndDate", "");
            String dateBeforeStartDate = CUtils.getStringData(context, "DateBeforeStartDate", "");
            if (!jsonString.equals("")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Mahadsha");
                    String previousPlanet = jsonArray.getJSONObject(currentIndex - 1).getString("PlanetName");
                    //show 40 day before from start date notification
                    if (!dateBeforeStartDate.equals("") && CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(dateBeforeStartDate, "dd/MM/yy"))) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(currentIndex + 1);
                        if (languageCode == CGlobalVariables.ENGLISH) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text), context.getResources().getString(R.string.before_startdate_mahadasha_des_text1) + " " + planetName + " " + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text2) + " " + convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy"));
                        } else if (getConditionForLanguage(languageCode)) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text), convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.before_startdate_mahadasha_des_text1) + " " + planetName + " " + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text2));
                        }
                        String str = "";
                        if (jsonObject1.has("MahadashaStartDate")) {
                            str = CUtils.getFortyDaysBeforeDate(jsonObject1.getString("MahadashaStartDate"), "dd/MM/yy");
                        }

                        CUtils.saveStringData(context, "DateBeforeStartDate", str);
                    }//show on the day notification
                    else if (!startDate.equals("") && CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(startDate, "dd/MM/yy"))) {
                        if (languageCode == CGlobalVariables.ENGLISH) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_mahadasha_heading_text), context.getResources().getString(R.string.on_startdate_mahadasha_des_text1) + " " + planetName + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text2) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy"));
                        } else if (getConditionForLanguage(languageCode)) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_mahadasha_heading_text), context.getResources().getString(R.string.on_startdate_mahadasha_des_text1) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text2) + " " + planetName + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text3));
                        }

                        JSONObject jsonObject1 = jsonArray.getJSONObject(currentIndex + 1);
                        if (jsonObject1.has("PlanetName") && jsonObject1.has("MahadashaStartDate")
                                && jsonObject1.has("MahadashaEndDate")) {
                            CUtils.saveStringData(context, "PlanetName", jsonObject1.getString("PlanetName"));
                            CUtils.saveStringData(context, "MahadashaStartDate", jsonObject1.getString("MahadashaStartDate"));
                            CUtils.saveStringData(context, "MahadashaEndDate", jsonObject1.getString("MahadashaEndDate"));
                            CUtils.saveIntData(context, "currentMahadasha", currentIndex + 1);
                        } else {
                            CUtils.saveStringData(context, "PlanetName", "");
                            CUtils.saveStringData(context, "MahadashaStartDate", "");
                            CUtils.saveStringData(context, "MahadashaEndDate", "");
                            CUtils.saveIntData(context, "currentMahadasha", currentIndex + 1);
                        }
                    } /*else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(endDate, "dd/MM/yy"))) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(currentIndex - 1);
                        String prePlanetName = jsonObject1.getString("PlanetName");
                        showDashaNotification(context, "Congrats, your " + prePlanetName + " mahadasa has ended", "Your " + prePlanetName + "mahadasa is ended today.");
                        CUtils.saveStringData(context, "MahadashaEndDate", jsonArray.getJSONObject(currentIndex).getString("MahadashaEndDate"));
                    } */
                }
            }

        } catch (Exception e) {
            Log.i("Error>>", e.getMessage());
        }

    }

    private void showNotificationForAnterDasha(Context context, Calendar calendar, String name, int languageCode) {
        try {
            //get current index of antar dasha
            int currentIndex1 = CUtils.getIntData(context, "currentPlanet1", 0);
            int currentAnterDashaIndex1 = CUtils.getIntData(context, "currentAnterDashaIndex1", 0);
            int currentIndex2 = CUtils.getIntData(context, "currentPlanet2", 0);
            int currentAnterDashaIndex2 = CUtils.getIntData(context, "currentAnterDashaIndex2", 0);
            String jsonString = CUtils.getStringData(context, "defaultKundliData", "");


            if (!jsonString.equals("")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("AnterDasha");
                    if (jsonArray != null) {
                        JSONArray jsonArray1 = jsonArray.getJSONArray(currentIndex1);
                        JSONArray jsonArray2 = jsonArray1.getJSONArray(1);
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(currentAnterDashaIndex1);
                        String planetName = getPlanetNameInLocalLang(jsonObject1.getString("Mahadasha"));
                        String anterDashaPlanet = getPlanetNameInLocalLang(jsonObject2.getString("PlanetName"));
                        String startDate = jsonObject2.getString("AnterDashaStartDate");
                        String endDate = jsonObject2.getString("AnterDashaEndDate");

                        JSONArray bjsonArray1 = jsonArray.getJSONArray(currentIndex2);
                        JSONArray bjsonArray2 = bjsonArray1.getJSONArray(1);
                        JSONObject bjsonObject1 = bjsonArray1.getJSONObject(0);
                        JSONObject bjsonObject2 = bjsonArray2.getJSONObject(currentAnterDashaIndex2);
                        String bplanetName = getPlanetNameInLocalLang(bjsonObject1.getString("Mahadasha"));
                        String banterDashaPlanet = getPlanetNameInLocalLang(bjsonObject2.getString("PlanetName"));
                        String bstartDate = bjsonObject2.getString("AnterDashaStartDate");
                        String beforeStartDate = CUtils.getFortyDaysBeforeDate(bstartDate, "dd/MM/yy");

                        //show 40 days before from start date notification
                        if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(beforeStartDate, "dd/MM/yy"))) {
                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_anterdasha_heading_text), context.getResources().getString(R.string.before_startdate_anterdasha_des_text1) + " " + bplanetName + "-" + banterDashaPlanet + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text2) + " " + convertDateStringInDifferentFormat(bstartDate, "dd/MM/yy", "MMMM dd, yyyy"));
                            } else if (getConditionForLanguage(languageCode)) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_anterdasha_heading_text), convertDateStringInDifferentFormat(bstartDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text1) + " " + bplanetName + "-" + banterDashaPlanet + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text2));
                            }


                            if (currentAnterDashaIndex2 == bjsonArray2.length() - 1) {
                                CUtils.saveIntData(context,
                                        "currentPlanet2", currentIndex2 + 1);
                                CUtils.saveIntData(context,
                                        "currentAnterDashaIndex2", 0);
                            } else {
                                CUtils.saveIntData(context,
                                        "currentAnterDashaIndex2", currentAnterDashaIndex2 + 1);
                            }
                        }
                        //show notification on the start date
                        else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(startDate, "dd/MM/yy"))) {
                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showAnterDashaNotification(context, name + context.getString(R.string.on_startdate_anterdasha_heading_text), context.getString(R.string.on_startdate_anterdasha_des_text1) + " " + planetName + "-" + anterDashaPlanet + " " + context.getString(R.string.on_startdate_anterdasha_des_text2) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy"));
                            } else if (getConditionForLanguage(languageCode)) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_anterdasha_heading_text), context.getResources().getString(R.string.on_startdate_anterdasha_des_text1) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_anterdasha_des_text2) + " " + planetName + "-" + anterDashaPlanet + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text2));
                            }

                            if (currentAnterDashaIndex1 == jsonArray2.length() - 1) {
                                CUtils.saveIntData(context,
                                        "currentPlanet1", currentIndex1 + 1);
                                CUtils.saveIntData(context,
                                        "currentAnterDashaIndex1", 0);
                            } else {
                                CUtils.saveIntData(context,
                                        "currentAnterDashaIndex1", currentAnterDashaIndex1 + 1);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {

        }

    }

    private void showNotificationForSadesati(Context context, Calendar calendar, String name, int languageCode) {
        try {
            String jsonString = CUtils.getStringData(context, "defaultKundliData", "");
            int currentIndex1 = CUtils.getIntData(context, "sadesatidashaarrayindex1", 0);
            int currentIndex2 = CUtils.getIntData(context, "sadesatidashaarrayindex2", 0);
            int currentIndex3 = CUtils.getIntData(context, "sadesatidashaarrayindex3", 0);
            int currentIndex4 = CUtils.getIntData(context, "sadesatidashaarrayindex4", 0);
           /*  String sadesati = CUtils.getStringData(context, "Sadesati", "");
            String shaniRashi = CUtils.getStringData(context, "Shanirashi", "");*/
            String startDate = CUtils.getStringData(context, "SadesatiStartDate", "");
            String endDate = CUtils.getStringData(context, "SadesatiEndDate", "");
            String phase = getPhaseNameInLocalLang(CUtils.getStringData(context, "SadestiPhase", ""));
            String dateBeforeStartDate = CUtils.getStringData(context, "SadestiDateBeforeStartDate", "");
            String dateBeforeEndDate = CUtils.getStringData(context, "SadestiDateBeforeEndDate", "");
            if (!jsonString.equals("")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("ShaniSadeSati");
                    //For 40 days before from satrt date notification
                    if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(dateBeforeStartDate, "dd/MM/yy"))) {
                        JSONObject preJsonObject = null;
                        String prePhase = "";
                        if (currentIndex1 > 0) {
                            preJsonObject = jsonArray.getJSONObject(currentIndex1 - 1);
                        }
                        JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex1);
                        JSONObject nextJsonObject = null;
                        if (currentIndex1 < jsonArray.length() - 1) {
                            nextJsonObject = jsonArray.getJSONObject(currentIndex1 + 1);
                        }

                        String currentSadesati = currentJsonObject.getString("Sade Sati");
                        String currentRashi = currentJsonObject.getString("Shani Rashi");
                        String currentPhase = currentJsonObject.getString("Phase");
                        String currentStartDate = currentJsonObject.getString("Start Date");
                        String currentEndDate = currentJsonObject.getString("End Date");
                        if (nextJsonObject != null) {
                            String nextPhase = nextJsonObject.getString("Phase");
                        }


                        if (preJsonObject != null) {
                            prePhase = preJsonObject.getString("Phase");
                        }
                        if (currentPhase.equals("Rising")) {
                            if (prePhase.equals("") || prePhase.equals("Setting")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_heading_text), context.getResources().getString(R.string.before_startdate_start_des_text) + " " + getMonthNameInLocalLang(currentStartDate));
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_heading_text), getMonthNameInLocalLang(currentStartDate) + " " + context.getResources().getString(R.string.before_startdate_start_des_text));
                                }

                            } else if (prePhase.equals("Rising")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_again_heading_text), context.getResources().getString(R.string.before_startdate_start_again_des_text1) + " " + getMonthNameInLocalLang(currentStartDate));
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_again_heading_text), getMonthNameInLocalLang(currentStartDate) + " " + context.getResources().getString(R.string.before_startdate_start_again_des_text1));
                                }

                            } else if (prePhase.equals("Peak")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.before_startdate_change_des_text2) + " " + getMonthNameInLocalLang(currentStartDate));
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), getMonthNameInLocalLang(currentStartDate) + " " + context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.before_startdate_change_des_text2));
                                }
                                //showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + currentPhase + context.getResources().getString(R.string.before_startdate_change_des_text2) + currentStartDate);
                            }
                        } else if (currentPhase.equals("Peak")) {
                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.before_startdate_change_des_text2) + " " + getMonthNameInLocalLang(currentStartDate));
                            } else if (getConditionForLanguage(languageCode)) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), getMonthNameInLocalLang(currentStartDate) + " " + context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.before_startdate_change_des_text2));
                            }
                            //showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + currentPhase + context.getResources().getString(R.string.before_startdate_change_des_text2) + currentStartDate);
                        } else if (currentPhase.equals("Setting")) {

                            if (prePhase.equals("") || prePhase.equals("Peak")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.before_startdate_change_des_text2) + " " + getMonthNameInLocalLang(currentStartDate));
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), getMonthNameInLocalLang(currentStartDate) + " " + context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.before_startdate_change_des_text2));
                                }
                            } else if (prePhase.equals("Setting")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_temporarily_heading_text), context.getResources().getString(R.string.before_startdate_start_temporarily_des_text1) + " " + getMonthNameInLocalLang(currentStartDate));
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_temporarily_heading_text), getMonthNameInLocalLang(currentStartDate) + " " + context.getResources().getString(R.string.before_startdate_start_temporarily_des_text1));
                                }
                            }
                        }
                        String str = "";
                        if (nextJsonObject != null) {
                            str = CUtils.getFortyDaysBeforeDate(nextJsonObject.getString("Start Date"), "MMMM dd, yyyy");
                        }

                        CUtils.saveStringData(context, "SadestiDateBeforeStartDate", str);
                        CUtils.saveIntData(context, "sadesatidashaarrayindex1", currentIndex1 + 1);

                    }// notification  on start date
                    else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(startDate, "MMMM dd, yyyy"))) {
                        JSONObject preJsonObject = null;
                        String prePhase = "";
                        if (currentIndex2 > 0) {
                            preJsonObject = jsonArray.getJSONObject(currentIndex2 - 1);
                        }
                        JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex2);
                        JSONObject nextJsonObject = null;
                        if (currentIndex2 < jsonArray.length() - 1) {
                            nextJsonObject = jsonArray.getJSONObject(currentIndex2 + 1);
                        }

                        String currentSadesati = currentJsonObject.getString("Sade Sati");
                        String currentRashi = currentJsonObject.getString("Shani Rashi");
                        String currentPhase = currentJsonObject.getString("Phase");
                        String currentStartDate = currentJsonObject.getString("Start Date");
                        String currentEndDate = currentJsonObject.getString("End Date");
                        if (nextJsonObject != null) {
                            String nextPhase = nextJsonObject.getString("Phase");
                        }


                        if (preJsonObject != null) {
                            prePhase = preJsonObject.getString("Phase");
                        }
                        if (currentPhase.equals("Rising")) {
                            if (prePhase.equals("") || prePhase.equals("Setting")) {
                               /* if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_heading_text), context.getResources().getString(R.string.on_startdate_start_des_text1) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_heading_text), context.getResources().getString(R.string.on_startdate_start_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_heading_text), context.getResources().getString(R.string.on_startdate_start_des_text1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_heading_text), context.getResources().getString(R.string.on_startdate_start_des_text1) + " " + getMonthNameInLocalLang(currentEndDate));

                                }
                            } else if (prePhase.equals("Rising")) {
                               /* if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_again_heading_text), context.getResources().getString(R.string.on_startdate_start_again_des_text1) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_again_heading_text), context.getResources().getString(R.string.on_startdate_start_again_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_start_again_des_text2));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_again_heading_text), context.getResources().getString(R.string.on_startdate_start_again_des_text1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.on_startdate_start_again_des_text2));

                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_again_heading_text), context.getResources().getString(R.string.on_startdate_start_again_des_text1) + " " + getMonthNameInLocalLang(currentEndDate));
                                }

                            } else if (prePhase.equals("Peak")) {
                                /*if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getMonthNameInLocalLang(currentEndDate));

                                }

                            }
                        } else if (currentPhase.equals("Peak")) {

                            /*if (languageCode == CGlobalVariables.ENGLISH) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);
                            } else if (getConditionForLanguage(languageCode)) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                            }*/
                            if (valdationForSadesati(languageCode)) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                            } else {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getMonthNameInLocalLang(currentEndDate));
                            }
                            //  showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);

                            // if (prePhase.equals("") || prePhase.equals("Rising")) {

                            //}
                        } else if (currentPhase.equals("Setting")) {
                            if (prePhase.equals("") || prePhase.equals("Peak")) {
                               /* if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + getPhaseNameInLocalLang(currentPhase) + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + getMonthNameInLocalLang(currentEndDate));

                                }
                                //showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + currentPhase + context.getResources().getString(R.string.on_startdate_change_des_text2) + currentEndDate);
                            } else if (prePhase.equals("Setting")) {
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_setting_phase_heading_text), context.getResources().getString(R.string.on_startdate_start_setting_phase_des_text1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.on_startdate_start_setting_phase_des_text2));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_setting_phase_heading_text), context.getResources().getString(R.string.on_startdate_start_setting_phase_des_text1) + " " + getMonthNameInLocalLang(currentEndDate));
                                }

                            }
                        }
                        String str = "";
                        if (nextJsonObject != null) {
                            str = nextJsonObject.getString("Start Date");
                        }

                        CUtils.saveStringData(context, "SadesatiStartDate", str);
                        CUtils.saveIntData(context, "sadesatidashaarrayindex2", currentIndex2 + 1);
                    }// notification for 40 days before end date
                    else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(dateBeforeEndDate, "dd/MM/yy"))) {
                        JSONObject preJsonObject = null;
                        String prePhase = "";
                        if (currentIndex3 > 0) {
                            preJsonObject = jsonArray.getJSONObject(currentIndex3 - 1);
                        }
                        JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex3);
                        JSONObject nextJsonObject = null;
                        if (currentIndex3 < jsonArray.length() - 1) {
                            nextJsonObject = jsonArray.getJSONObject(currentIndex3 + 1);
                        }

                        String currentSadesati = currentJsonObject.getString("Sade Sati");
                        String currentRashi = currentJsonObject.getString("Shani Rashi");
                        String currentPhase = currentJsonObject.getString("Phase");
                        String currentStartDate = currentJsonObject.getString("Start Date");
                        String currentEndDate = currentJsonObject.getString("End Date");
                        String nextPhase = "";
                        if (nextJsonObject != null) {
                            nextPhase = nextJsonObject.getString("Phase");
                        }


                        if (preJsonObject != null) {
                            prePhase = preJsonObject.getString("Phase");
                        }
                        if (currentPhase.equals("Rising")) {
                            if (nextPhase.equals("Rising")) {
                                /*if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate + " " + context.getResources().getString(R.string.before_enddate_des_text_temporarily2));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.before_enddate_des_text_temporarily2));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + getMonthNameInLocalLang(currentEndDate));
                                }
                            }
                        } else if (currentPhase.equals("Setting")) {
                            if (nextPhase.equals("Setting")) {
                                /*if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate + " " + context.getResources().getString(R.string.before_enddate_des_text_temporarily2));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.before_enddate_des_text_temporarily2));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + getMonthNameInLocalLang(currentEndDate));

                                }
                                // showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + currentEndDate);


                            } else if (nextPhase.equals("Rising") || nextPhase.equals("")) {
                                /*if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_permanently), context.getResources().getString(R.string.before_enddate_des_text_permanently1) + " " + currentEndDate);
                                } else if (getConditionForLanguage(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_permanently), context.getResources().getString(R.string.before_enddate_des_text_permanently1) + " " + currentEndDate + " " + context.getResources().getString(R.string.before_enddate_des_text_permanently2));
                                }*/
                                if (valdationForSadesati(languageCode)) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_permanently), context.getResources().getString(R.string.before_enddate_des_text_permanently1) + " " + getMonthNameInLocalLang(currentEndDate) + " " + context.getResources().getString(R.string.before_enddate_des_text_permanently2));
                                } else {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_permanently), context.getResources().getString(R.string.before_enddate_des_text_permanently1) + " " + getMonthNameInLocalLang(currentEndDate));

                                }
                            }
                        }
                        String str = "";
                        if (nextJsonObject != null) {
                            str = CUtils.getFortyDaysBeforeDate(nextJsonObject.getString("End Date"), "MMMM dd, yyyy");
                        }

                        CUtils.saveStringData(context, "SadestiDateBeforeEndDate", str);
                        CUtils.saveIntData(context, "sadesatidashaarrayindex3", currentIndex3 + 1);
                    }//notification On end date
                    else {
                        if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(endDate, "MMMM dd, yyyy"))) {
                            JSONObject preJsonObject = null;
                            String prePhase = "";
                            if (currentIndex4 > 0) {
                                preJsonObject = jsonArray.getJSONObject(currentIndex4 - 1);
                            }
                            JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex4);
                            JSONObject nextJsonObject = null;
                            if (currentIndex4 < jsonArray.length() - 1) {
                                nextJsonObject = jsonArray.getJSONObject(currentIndex4 + 1);
                            }

                            String currentSadesati = currentJsonObject.getString("Sade Sati");
                            String currentRashi = currentJsonObject.getString("Shani Rashi");
                            String currentPhase = currentJsonObject.getString("Phase");
                            String currentStartDate = currentJsonObject.getString("Start Date");
                            String currentEndDate = currentJsonObject.getString("End Date");
                            String nextPhase = "";
                            if (nextJsonObject != null) {
                                nextPhase = nextJsonObject.getString("Phase");
                            }


                            if (preJsonObject != null) {
                                prePhase = preJsonObject.getString("Phase");
                            }
                            if (currentPhase.equals("Rising")) {
                                if (nextPhase.equals("Rising")) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_enddate_heading_text_temporarily), context.getResources().getString(R.string.on_enddate_des_text_temporarily));
                                }
                            } else if (currentPhase.equals("Setting")) {

                                if (nextPhase.equals("Setting")) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_enddate_heading_text_temporarily), context.getResources().getString(R.string.on_enddate_des_text_temporarily));
                                } else if (nextPhase.equals("Rising") || nextPhase.equals("")) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_enddate_heading_text_permanently), context.getResources().getString(R.string.on_enddate_des_text_permanently));
                                }
                            }
                            String str = "";
                            if (nextJsonObject != null) {
                                str = nextJsonObject.getString("End Date");
                            }

                            CUtils.saveStringData(context, "SadesatiEndDate", str);
                            CUtils.saveIntData(context, "sadesatidashaarrayindex4", currentIndex4 + 1);
                        }
                    }

                }
            }

        } catch (Exception e) {
            Log.i("", e.getMessage());
        }
    }

    /**
     * This function is used to show dasha notification.
     *
     * @param context
     * @param heading
     * @param content
     */
    @SuppressLint("NewApi")
    private void showDashaNotification(Context context, String heading, String content) {


        // String CHANNEL_ID = "dasha_notification";
        Intent notificationIntent = new Intent(context, HomeInputScreen.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_DASA);
        notificationIntent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, 2);
        attachNotificationKundliPayload(context, notificationIntent, null);
        int notificationId = (int) System.currentTimeMillis();
        PendingIntent contentIntent = createBirthdayWishClickPendingIntent(context, notificationIntent, notificationId,
                CGlobalVariables.MY_MAHADASHA_NOTIFICATION);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));


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

        mNotificationManager.notify(1005, notification.build());
        logBirthdayWishNotificationShown(CGlobalVariables.MY_MAHADASHA_NOTIFICATION);

    }

    /**
     * This function is used to show dasha notification.
     *
     * @param context
     * @param heading
     * @param content
     */
    @SuppressLint("NewApi")
    private void showAnterDashaNotification(Context context, String heading, String content) {

        //String CHANNEL_ID = "anterdasha_notification";
        Intent notificationIntent = new Intent(context, HomeInputScreen.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_DASA);
        attachNotificationKundliPayload(context, notificationIntent, null);

        int notificationId = (int) System.currentTimeMillis();
        PendingIntent contentIntent = createBirthdayWishClickPendingIntent(context, notificationIntent, notificationId,
                CGlobalVariables.MY_ANTERDASHA_NOTIFICATION);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));
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
        mNotificationManager.notify(1006, notification.build());
        logBirthdayWishNotificationShown(CGlobalVariables.MY_ANTERDASHA_NOTIFICATION);

    }

    /**
     * This function is used to show sadesati notification.
     *
     * @param context
     * @param heading
     * @param content
     */
    @SuppressLint("NewApi")
    private void showSadesatiNotification(Context context, String heading,
                                          String content) {


        //String CHANNEL_ID = "sadesati_notification";
        Intent notificationIntent = new Intent(context, HomeInputScreen.class);

        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, 5);
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_PREDICTION);
        attachNotificationKundliPayload(context, notificationIntent, null);

        int notificationId = (int) System.currentTimeMillis();
        PendingIntent contentIntent = createBirthdayWishClickPendingIntent(context, notificationIntent, notificationId,
                CGlobalVariables.MY_SADESATI_NOTIFICATION);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setStyle((new NotificationCompat.BigTextStyle().bigText(content)));
       /* NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentText(content)
                .setSmallIcon(R.drawable.magz_notification_icon)
                .setContentIntent(contentIntent).setAutoCancel(true).build();*/

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
        mNotificationManager.notify(1007, notification.build());
        logBirthdayWishNotificationShown(CGlobalVariables.MY_SADESATI_NOTIFICATION);
    }

    /**
     * Logs delivery analytics for locally generated birthday wish notifications.
     *
     * @param notificationLabel identifies which birthday reminder variant was shown
     */
    private static void logBirthdayWishNotificationShown(String notificationLabel) {
        CUtils.fcmAnalyticsEvents(notificationLabel, CGlobalVariables.FIREBASE_NOTIFICATION_DELIVERED_EVENT, "");
    }

    /**
     * Adds the latest available kundli payload to notification navigation so the destination screen can still resolve
     * a birth chart when the saved custom object is unavailable at tap time.
     *
     * @param context app context used to read the saved kundli object when no explicit payload is supplied
     * @param launchIntent destination intent that will be forwarded after analytics tracking
     * @param beanHoroPersonalInfo optional explicit kundli payload for the notification
     */
    private static void attachNotificationKundliPayload(Context context, Intent launchIntent,
                                                        BeanHoroPersonalInfo beanHoroPersonalInfo) {
        BeanHoroPersonalInfo notificationKundli = beanHoroPersonalInfo;
        if (notificationKundli == null) {
            Object savedObject = CUtils.getCustomObject(context);
            if (savedObject instanceof BeanHoroPersonalInfo) {
                notificationKundli = (BeanHoroPersonalInfo) savedObject;
            }
        }
        if (notificationKundli != null) {
            launchIntent.putExtra(CGlobalVariables.BIRTH_DETAILS_KEY, notificationKundli);
        }
    }

    /**
     * Builds a tracked notification click intent that logs analytics before opening the target screen.
     *
     * @param context app context used to create the pending intent
     * @param launchIntent destination intent to open after the click is tracked
     * @param requestCode unique request code for the notification tap action
     * @param notificationLabel analytics label describing which notification was clicked
     * @return activity pending intent that logs the click and then opens the app screen
     */
    private static PendingIntent createBirthdayWishClickPendingIntent(Context context, Intent launchIntent, int requestCode,
                                                                      String notificationLabel) {
        Intent clickTrackingIntent = new Intent(context, BirthdayWishNotificationClickActivity.class);
        clickTrackingIntent.setAction(notificationLabel + "_clicked_" + requestCode);
        clickTrackingIntent.putExtras(launchIntent);
        clickTrackingIntent.putExtra(BirthdayWishNotificationClickActivity.EXTRA_NOTIFICATION_ANALYTICS_LABEL,
                notificationLabel);
        clickTrackingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(context, requestCode, clickTrackingIntent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);
        }
        return PendingIntent.getActivity(context, requestCode, clickTrackingIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    private long getDateDifference(JSONObject currentJsonObject, JSONObject nextJsonObject) {
        long dayCount = 0;
        try {
            String startdate = nextJsonObject.getString("Start Date");
            String endDate = currentJsonObject.getString("End Date");
            Date date1 = CUtils.convertStringToDate(startdate, "MMMM dd, yyyy");
            Date date2 = CUtils.convertStringToDate(endDate, "MMMM dd, yyyy");
            long diff = Math.abs(date1.getTime() - date2.getTime());
            dayCount = diff / (24 * 60 * 60 * 1000);
            //Log.i("startdate>>", startdate);
            //Log.i("endDate>>", endDate);
            //Log.i("dayCount>>", "" + dayCount);
        } catch (Exception e) {

        }
        return dayCount;
    }

    private String getDashaName1(String dasha) {
        String dashaName = "";
        if (dasha.equals("SUN")) {
            dashaName = "Sun";
        } else if (dasha.equals("MON")) {
            dashaName = "Moon";
        } else if (dasha.equals("MAR")) {
            dashaName = "Mars";
        } else if (dasha.equals("MER")) {
            dashaName = "Mercury";
        } else if (dasha.equals("JUP")) {
            dashaName = "Jupiter";
        } else if (dasha.equals("VEN")) {
            dashaName = "Venus";
        } else if (dasha.equals("SAT")) {
            dashaName = "Saturn";
        } else if (dasha.equals("RAH")) {
            dashaName = "Rahu";
        } else if (dasha.equals("KET")) {
            dashaName = "Ketu";
        }
        return dashaName;
    }

    private String getName(String name) {
        String nameStr = "";
        if (name.trim().contains(" ")) {
            String[] spilt = name.trim().split("\\s+");
            nameStr = capWordString(spilt[0]);
            // for (int i = 0; i < spilt.length; i++) {

            //}
        } else {
            nameStr = capWordString(name);
        }
        return nameStr;
    }

    private String capWordString(String line) {
        if (line.length() > 0) {
            int asciiValue = (int) line.charAt(0);
            // showLogMessage("asciiValue: ", asciiValue+"");
            if (asciiValue > 96 && asciiValue < 123 || asciiValue > 64 && asciiValue < 91)
                return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
            else
                return line;
        } else {
            return "";
        }

    }

    private boolean getConditionForLanguage(int languageCode) {
        return languageCode == CGlobalVariables.HINDI
                || languageCode == CGlobalVariables.BANGALI
                || languageCode == CGlobalVariables.TAMIL
                || languageCode == CGlobalVariables.TELUGU
                || languageCode == CGlobalVariables.MARATHI
                || languageCode == CGlobalVariables.MALAYALAM
                || languageCode == CGlobalVariables.GUJARATI
                || languageCode == CGlobalVariables.KANNADA;
    }

    private boolean valdationForSadesati(int languageCode) {
        return languageCode == CGlobalVariables.HINDI
                || languageCode == CGlobalVariables.BANGALI
                || languageCode == CGlobalVariables.TAMIL;
    }

    private String getPlanetNameInLocalLang(String planet) {
        String planetName = "";
        if (planet.equals("SUN")) {
            planetName = context.getResources().getString(R.string.sun_planet);
        } else if (planet.equals("MON")) {
            planetName = context.getResources().getString(R.string.moon_planet);
        } else if (planet.equals("MAR")) {
            planetName = context.getResources().getString(R.string.Mars_planet);
        } else if (planet.equals("MER")) {
            planetName = context.getResources().getString(R.string.Mercury_planet);
        } else if (planet.equals("JUP")) {
            planetName = context.getResources().getString(R.string.Jupiter_planet);
        } else if (planet.equals("VEN")) {
            planetName = context.getResources().getString(R.string.Venus_planet);
        } else if (planet.equals("SAT")) {
            planetName = context.getResources().getString(R.string.Saturn_planet);
        } else if (planet.equals("RAH")) {
            planetName = context.getResources().getString(R.string.Rahu_planet);
        } else if (planet.equals("KET")) {
            planetName = context.getResources().getString(R.string.Ketu_planet);
        }
        return planetName;
    }

    private String getPhaseNameInLocalLang(String phase) {
        String phaseName = "";
        if (phase.equals("Rising")) {
            phaseName = context.getResources().getString(R.string.rising_phase);
        } else if (phase.equals("Peak")) {
            phaseName = context.getResources().getString(R.string.peak_phase);
        } else if (phase.equals("Setting")) {
            phaseName = context.getResources().getString(R.string.setting_phase);
        }
        return phaseName;
    }

    private String getMonthNameInLocalLang(String dateString) {
        String date = "";
        String[] monthName = context.getResources().getStringArray(R.array.WhatsAppMonthName);
        if (dateString.contains("January")) {
            date = dateString.replace("January", monthName[0]);
        } else if (dateString.contains("February")) {
            date = dateString.replace("February", monthName[1]);
        } else if (dateString.contains("March")) {
            date = dateString.replace("March", monthName[2]);
        } else if (dateString.contains("April")) {
            date = dateString.replace("April", monthName[3]);
        } else if (dateString.contains("May")) {
            date = dateString.replace("May", monthName[4]);
        } else if (dateString.contains("June")) {
            date = dateString.replace("June", monthName[5]);
        } else if (dateString.contains("July")) {
            date = dateString.replace("July", monthName[6]);
        } else if (dateString.contains("August")) {
            date = dateString.replace("August", monthName[7]);
        } else if (dateString.contains("September")) {
            date = dateString.replace("September", monthName[8]);
        } else if (dateString.contains("October")) {
            date = dateString.replace("October", monthName[9]);
        } else if (dateString.contains("November")) {
            date = dateString.replace("November", monthName[10]);
        } else if (dateString.contains("December")) {
            date = dateString.replace("December", monthName[11]);
        }
        return date;
    }


}
