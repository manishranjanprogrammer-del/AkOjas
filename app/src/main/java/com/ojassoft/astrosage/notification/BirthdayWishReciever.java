package com.ojassoft.astrosage.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

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

public class BirthdayWishReciever extends BroadcastReceiver {
    int languageCode;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        // String action = intent.getAction();
        // get default kundli object
        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils
                .getCustomObject(context);
        languageCode = ((AstrosageKundliApplication) context.getApplicationContext())
                .getLanguageCode();
        this.context = context;
        // check for birtday Action
        //  if (action.equals(CGlobalVariables.BIRTHDAY_INTENT_ACTION)) {
        if (beanHoroPersonalInfo != null)

        {
            if (LibCUtils.getUserChoiceFromSDCard(context) != LibCGlobalVariables.NEVER) {
                Calendar c = Calendar.getInstance();
                showNotificationForBirthday(context, c, beanHoroPersonalInfo);
                showNotificationForMahaDasha(context, c, getName(beanHoroPersonalInfo.getName()));
                showNotificationForSadesati(context, c, getName(beanHoroPersonalInfo.getName()));
                showNotificationForAnterDasha(context, c, getName(beanHoroPersonalInfo.getName()));
            }
        }
       /* try {
            String dateInString = "18/01/16";
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat formatter1 = new SimpleDateFormat("d'st' MMMM yyyy");
            Date date = formatter.parse(dateInString);
            String dateStr = formatter1.format(date);

            LocationManager locMan = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            long time = locMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getTime();
            Calendar c = Calendar.getInstance();
            long time2 = c.getTimeInMillis();
            //Log.i("time1>>", time + "");
            //Log.i("time2>>", time2 + "");

        } catch (Exception e) {
            //Log.i("time2>>", e.getMessage() + "");
        }*/

       /* } else if (action.equals(CGlobalVariables.MY_MAHADASHA_NOTIFICATION)) {
            Calendar c = Calendar.getInstance();
            if (beanHoroPersonalInfo != null) {
                showNotificationForMahaDasha(context, c);
            }

        } else if (action.equals(CGlobalVariables.MY_SADESATI_NOTIFICATION)) {
            Calendar c = Calendar.getInstance();
            showNotificationForSadesati(context, c);
            *//*String jsonString = CUtils.getStringData(context,
                    "sadesatijsonstring", "");
            if (!jsonString.equals("")) {
                parseSadesatiData(context, jsonString, c);
            }*//*
        } else {
            if (beanHoroPersonalInfo != null) {
                CUtils.BirthdayNotifications(context,
                        CGlobalVariables.BIRTHDAY_INTENT_ACTION, 50);
                CUtils.startDashaAlarma(context,
                        CGlobalVariables.MY_MAHADASHA_NOTIFICATION, 25);
                CUtils.startSadesatiAlarma(context,
                        CGlobalVariables.MY_SADESATI_NOTIFICATION, 35);
            }

        }*/

    }

    private void showNotificationForBirthday(Context context, Calendar calendar, BeanHoroPersonalInfo beanHoroPersonalInfo) {
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        if (date == beanHoroPersonalInfo.getDateTime().getDay()
                && month == beanHoroPersonalInfo.getDateTime()
                .getMonth()) {
            showHappyBirthDayNotification(context, beanHoroPersonalInfo.getName());
        }
    }

    /*private void showNotificationForMahaDasha(Context context, Calendar calendar, String name) {
        ArrayList<MahaDashaDetail> arrayList = CUtils.getArraylistOfObject(context, "MAHADASHADETAIL");
        if (arrayList != null) {
            int index1 = 0;
            int index2 = 0;
            Date date1;
            Date date2;
            Date date3;
            for (int i = 1; i < arrayList.size(); i++) {
                date1 = CUtils.convertStringToDate(arrayList.get(i).getDateBeforeStartDate(), "dd/MM/yy");
                date2 = CUtils.convertStringToDate(arrayList.get(i).getMahadashaStartDate(), "dd/MM/yy");
                date3 = CUtils.convertStringToDate(CUtils.getFortyDaysAfterDate(arrayList.get(i).getMahadashaStartDate(), "dd/MM/yy"), "dd/MM/yy");
                if (CUtils.checkDate(calendar.getTime(), date1, date2) && !arrayList.get(i).isBeforeNotificationShown()) {
                    index1 = i;
                    break;
                } else if (CUtils.checkDate(calendar.getTime(), date2, date3) && !arrayList.get(i).isOnDateNotificationShown()) {
                    index2 = i;
                    break;
                }
            }
            if (index1 != 0) {
                String previousPlanetName = arrayList.get(index1 - 1).getPlanetName();
                String planetName = arrayList.get(index1).getPlanetName();
                String startDate = arrayList.get(index1).getMahadashaStartDate();
                String endDate = arrayList.get(index1).getMahadashaEndDate();
                boolean isBeforeNotificationShown = arrayList.get(index1).isBeforeNotificationShown();
                if (!isBeforeNotificationShown) {
                    showDashaNotification(context, "[" + name + "]" + "Your " + previousPlanetName + " mahadasa is changing", "Tap to check result of [" + planetName + "] mahadasa starting from [" + startDate + "]");
                    changeAndSaveMahadashaList(context, arrayList, index1, 1);
                }
            }
            if (index2 != 0) {
                String previousPlanetName = arrayList.get(index2 - 1).getPlanetName();
                String planetName = arrayList.get(index2).getPlanetName();
                String startDate = arrayList.get(index2).getMahadashaStartDate();
                String endDate = arrayList.get(index2).getMahadashaEndDate();
                boolean isOnDateNotificationShown = arrayList.get(index2).isOnDateNotificationShown();
                if (!isOnDateNotificationShown) {
                    showDashaNotification(context, name + "Your " + previousPlanetName + " mahadasa has changed", "Tap to check results of " + planetName + " mahadasa started today and will end on " + endDate);
                    changeAndSaveMahadashaList(context, arrayList, index2, 2);
                }
            }

        }
    }

    private void changeAndSaveMahadashaList(Context context, ArrayList arrayList, int position, int changeVal) {
        if (changeVal == 1) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == position) {
                    ((MahaDashaDetail) arrayList.get(i)).setBeforeNotificationShown(true);
                } else {
                    ((MahaDashaDetail) arrayList.get(i)).setBeforeNotificationShown(false);
                }
            }
        } else if (changeVal == 2) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == position) {
                    ((MahaDashaDetail) arrayList.get(i)).setOnDateNotificationShown(true);
                } else {
                    ((MahaDashaDetail) arrayList.get(i)).setOnDateNotificationShown(false);
                }
            }
        }
        CUtils.saveArraylistOfObject(context, "MAHADASHADETAIL", arrayList);
    }*/
    private void showNotificationForMahaDasha(Context context, Calendar calendar, String name) {
        try {

            int currentIndex = CUtils.getIntData(context, "currentMahadasha", 0);
            String jsonString = CUtils.getStringData(context, "defaultKundliData", "");
            String planetName = CUtils.getStringData(context, "PlanetName", "");
            String startDate = CUtils.getStringData(context, "MahadashaStartDate", "");
            String endDate = CUtils.getStringData(context, "MahadashaEndDate", "");
            String dateBeforeStartDate = CUtils.getStringData(context, "DateBeforeStartDate", "");
            if (!jsonString.equals("")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("Mahadsha");
                    String previousPlanet = jsonArray.getJSONObject(currentIndex - 1).getString("PlanetName");
                    if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(dateBeforeStartDate, "dd/MM/yy"))) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(currentIndex + 1);
                        if (languageCode == CGlobalVariables.ENGLISH) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text), context.getResources().getString(R.string.before_startdate_mahadasha_des_text1) + " " + getDashaName(planetName) + " " + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text2) + " " + convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy"));
                        } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text), convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.before_startdate_mahadasha_des_text1) + " " + getDashaName(planetName) + " " + context.getResources().getString(R.string.before_startdate_mahadasha_heading_text2));
                        }

                        String str = CUtils.getFortyDaysBeforeDate(jsonObject1.getString("MahadashaStartDate"), "dd/MM/yy");
                        CUtils.saveStringData(context, "DateBeforeStartDate", str);
                    } else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(startDate, "dd/MM/yy"))) {
                        if (languageCode == CGlobalVariables.ENGLISH) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_mahadasha_heading_text), context.getResources().getString(R.string.on_startdate_mahadasha_des_text1) + " " + getDashaName(planetName) + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text2) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy"));
                        } else if (languageCode == CGlobalVariables.HINDI) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_mahadasha_heading_text), context.getResources().getString(R.string.on_startdate_mahadasha_des_text1) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text2) + " " + getDashaName(planetName) + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                        } else if (languageCode == CGlobalVariables.BANGALI) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_mahadasha_heading_text), context.getResources().getString(R.string.on_startdate_mahadasha_des_text1) + " " + getDashaName(planetName) + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text2) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                        } else if (languageCode == CGlobalVariables.TAMIL) {
                            showDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_mahadasha_heading_text), context.getResources().getString(R.string.on_startdate_mahadasha_des_text1) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_mahadasha_des_text2) + " " + getDashaName(planetName) + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                        }


                        JSONObject jsonObject1 = jsonArray.getJSONObject(currentIndex + 1);
                        CUtils.saveStringData(context, "PlanetName", jsonObject1.getString("PlanetName"));
                        CUtils.saveStringData(context, "MahadashaStartDate", jsonObject1.getString("MahadashaStartDate"));
                        CUtils.saveStringData(context, "MahadashaEndDate", jsonObject1.getString("MahadashaEndDate"));
                        CUtils.saveIntData(context, "currentMahadasha", currentIndex + 1);
                    } /*else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(endDate, "dd/MM/yy"))) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(currentIndex - 1);
                        String prePlanetName = jsonObject1.getString("PlanetName");
                        showDashaNotification(context, "Congrats, your " + prePlanetName + " mahadasa has ended", "Your " + prePlanetName + "mahadasa is ended today.");
                        CUtils.saveStringData(context, "MahadashaEndDate", jsonArray.getJSONObject(currentIndex).getString("MahadashaEndDate"));
                    } */
                }
            }

        } catch (Exception e) {

        }

    }

    private void showNotificationForAnterDasha(Context context, Calendar calendar, String name) {
        try {

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
                        String planetName = jsonObject1.getString("Mahadasha");
                        String anterDashaPlanet = jsonObject2.getString("PlanetName");
                        String startDate = jsonObject2.getString("AnterDashaStartDate");
                        String endDate = jsonObject2.getString("AnterDashaEndDate");

                        JSONArray bjsonArray1 = jsonArray.getJSONArray(currentIndex2);
                        JSONArray bjsonArray2 = bjsonArray1.getJSONArray(1);
                        JSONObject bjsonObject1 = bjsonArray1.getJSONObject(0);
                        JSONObject bjsonObject2 = bjsonArray2.getJSONObject(currentAnterDashaIndex2);
                        String bplanetName = bjsonObject1.getString("Mahadasha");
                        String banterDashaPlanet = bjsonObject2.getString("PlanetName");
                        String bstartDate = bjsonObject2.getString("AnterDashaStartDate");
                        String beforeStartDate = CUtils.getFortyDaysBeforeDate(bstartDate, "dd/MM/yy");


                        if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(beforeStartDate, "dd/MM/yy"))) {
                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_anterdasha_heading_text), context.getResources().getString(R.string.before_startdate_anterdasha_des_text1) + " " + getDashaName(bplanetName) + "-" + getDashaName(banterDashaPlanet) + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text2) + " " + convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy"));
                            } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.before_startdate_anterdasha_heading_text), convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text1) + " " + getDashaName(bplanetName) + "-" + getDashaName(banterDashaPlanet) + " " + context.getResources().getString(R.string.before_startdate_anterdasha_des_text2));
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
                        } else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(startDate, "dd/MM/yy"))) {
                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showAnterDashaNotification(context, name + context.getString(R.string.on_startdate_anterdasha_heading_text), context.getString(R.string.on_startdate_anterdasha_des_text1) + " " + getDashaName(planetName) + "-" + getDashaName(anterDashaPlanet) + " " + context.getString(R.string.on_startdate_anterdasha_des_text2) + " " + convertDateStringInDifferentFormat(endDate, "dd/MM/yy", "MMMM dd, yyyy"));
                            } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.TAMIL) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_anterdasha_heading_text),
                                        context.getResources().getString(R.string.on_startdate_anterdasha_des_text1) + " " + convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_anterdasha_des_text2) + " " + getDashaName(bplanetName) + "-" + getDashaName(banterDashaPlanet) + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                            } else if (languageCode == CGlobalVariables.BANGALI) {
                                showAnterDashaNotification(context, name + context.getResources().getString(R.string.on_startdate_anterdasha_heading_text),
                                        context.getResources().getString(R.string.on_startdate_anterdasha_des_text1) + " " + getDashaName(bplanetName) + "-" + getDashaName(banterDashaPlanet) + " " + context.getResources().getString(R.string.on_startdate_anterdasha_des_text2) + " " + convertDateStringInDifferentFormat(startDate, "dd/MM/yy", "MMMM dd, yyyy") + " " + context.getResources().getString(R.string.on_startdate_start_des_text2));
                            } else if (languageCode == CGlobalVariables.TAMIL) {

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
            //Log.i("error", "" + e.getMessage());
        }

    }


    private void showNotificationForSadesati(Context context, Calendar calendar, String name) {
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
            String phase = CUtils.getStringData(context, "SadestiPhase", "");
            String dateBeforeStartDate = CUtils.getStringData(context, "SadestiDateBeforeStartDate", "");
            String dateBeforeEndDate = CUtils.getStringData(context, "SadestiDateBeforeEndDate", "");
            if (!jsonString.equals("")) {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("ShaniSadeSati");
                    if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(dateBeforeStartDate, "dd/MM/yy"))) {
                        JSONObject preJsonObject = null;
                        String prePhase = "";
                        if (currentIndex1 > 0) {
                            preJsonObject = jsonArray.getJSONObject(currentIndex1 - 1);
                        }
                        JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex1);
                        JSONObject nextJsonObject = jsonArray.getJSONObject(currentIndex1 + 1);
                        String currentSadesati = currentJsonObject.getString("Sade Sati");
                        String currentRashi = currentJsonObject.getString("Shani Rashi");
                        String currentPhase = currentJsonObject.getString("Phase");
                        String currentStartDate = currentJsonObject.getString("Start Date");
                        String currentEndDate = currentJsonObject.getString("End Date");
                        String nextPhase = nextJsonObject.getString("Phase");

                        if (preJsonObject != null) {
                            prePhase = preJsonObject.getString("Phase");
                        }
                        if (currentPhase.equals("Rising")) {
                            if (prePhase.equals("") || prePhase.equals("Setting")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_heading_text), context.getResources().getString(R.string.before_startdate_start_des_text) + " " + currentStartDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_heading_text), currentStartDate + " " + context.getResources().getString(R.string.before_startdate_start_des_text));
                                }

                            } else if (prePhase.equals("Rising")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_again_heading_text), context.getResources().getString(R.string.before_startdate_start_again_des_text1) + " " + currentStartDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_again_heading_text), currentStartDate + " " + context.getResources().getString(R.string.before_startdate_start_again_des_text1));
                                }

                            } else if (prePhase.equals("Peak")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.before_startdate_change_des_text2) + " " + currentStartDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), currentStartDate + " " + context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.before_startdate_change_des_text2));
                                }
                                //showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + currentPhase + context.getResources().getString(R.string.before_startdate_change_des_text2) + currentStartDate);
                            }
                        } else if (currentPhase.equals("Peak")) {
                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.before_startdate_change_des_text2) + " " + currentStartDate);
                            } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), currentStartDate + " " + context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.before_startdate_change_des_text2));
                            }
                            //showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + currentPhase + context.getResources().getString(R.string.before_startdate_change_des_text2) + currentStartDate);
                        } else if (currentPhase.equals("Setting")) {

                            if (prePhase.equals("") || prePhase.equals("Peak")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.before_startdate_change_des_text2) + " " + currentStartDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_change_heading_text), currentStartDate + " " + context.getResources().getString(R.string.before_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.before_startdate_change_des_text2));
                                }
                            } else if (prePhase.equals("Setting")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_temporarily_heading_text), context.getResources().getString(R.string.before_startdate_start_temporarily_des_text1) + " " + currentStartDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_startdate_start_temporarily_heading_text), currentStartDate + " " + context.getResources().getString(R.string.before_startdate_start_temporarily_des_text1));
                                }
                            }
                        }
                        String str = CUtils.getFortyDaysBeforeDate(nextJsonObject.getString("Start Date"), "MMMM dd, yyyy");
                        CUtils.saveStringData(context, "SadestiDateBeforeStartDate", str);
                        CUtils.saveIntData(context, "sadesatidashaarrayindex1", currentIndex1 + 1);

                    } else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(startDate, "MMMM dd, yyyy"))) {
                        JSONObject preJsonObject = null;
                        String prePhase = "";
                        if (currentIndex2 > 0) {
                            preJsonObject = jsonArray.getJSONObject(currentIndex2 - 1);
                        }
                        JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex2);
                        JSONObject nextJsonObject = jsonArray.getJSONObject(currentIndex2 + 1);
                        String currentSadesati = currentJsonObject.getString("Sade Sati");
                        String currentRashi = currentJsonObject.getString("Shani Rashi");
                        String currentPhase = currentJsonObject.getString("Phase");
                        String currentStartDate = currentJsonObject.getString("Start Date");
                        String currentEndDate = currentJsonObject.getString("End Date");
                        String nextPhase = nextJsonObject.getString("Phase");

                        if (preJsonObject != null) {
                            prePhase = preJsonObject.getString("Phase");
                        }
                        if (currentPhase.equals("Rising")) {
                            if (prePhase.equals("")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_heading_text), context.getResources().getString(R.string.on_startdate_start_des_text1) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_heading_text), context.getResources().getString(R.string.on_startdate_start_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }
                            } else if (prePhase.equals("Rising")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_again_heading_text), context.getResources().getString(R.string.on_startdate_start_again_des_text1) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_again_heading_text), context.getResources().getString(R.string.on_startdate_start_again_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }
                            } else if (prePhase.equals("Peak")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                } else if (languageCode == CGlobalVariables.BANGALI) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                } else if (languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }

                            }
                        } else if (currentPhase.equals("Peak")) {

                            if (languageCode == CGlobalVariables.ENGLISH) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);
                            } else if (languageCode == CGlobalVariables.HINDI) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                            } else if (languageCode == CGlobalVariables.BANGALI) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                            } else if (languageCode == CGlobalVariables.TAMIL) {
                                showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                            }
                            //  showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);

                            // if (prePhase.equals("") || prePhase.equals("Rising")) {

                            //}
                        } else if (currentPhase.equals("Setting")) {
                            if (prePhase.equals("") || prePhase.equals("Peak")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                } else if (languageCode == CGlobalVariables.BANGALI) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                } else if (languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text2) + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }
                                //showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_change_heading_text), context.getResources().getString(R.string.on_startdate_change_des_text1) + currentPhase + context.getResources().getString(R.string.on_startdate_change_des_text2) + currentEndDate);
                            } else if (prePhase.equals("Setting")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_setting_phase_heading_text), "Check result of " + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_start_setting_phase_des_text1) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_startdate_start_setting_phase_heading_text), "Check result of " + " " + currentPhase + " " + context.getResources().getString(R.string.on_startdate_start_setting_phase_des_text1) + " " + currentEndDate + " " + context.getResources().getString(R.string.on_startdate_change_des_text3));
                                }

                            }
                        }
                        String str = nextJsonObject.getString("Start Date");
                        CUtils.saveStringData(context, "SadesatiStartDate", str);
                        CUtils.saveIntData(context, "sadesatidashaarrayindex2", currentIndex2 + 1);
                    } else if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(dateBeforeEndDate, "dd/MM/yy"))) {
                        JSONObject preJsonObject = null;
                        String prePhase = "";
                        if (currentIndex3 > 0) {
                            preJsonObject = jsonArray.getJSONObject(currentIndex3 - 1);
                        }
                        JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex3);
                        JSONObject nextJsonObject = jsonArray.getJSONObject(currentIndex3 + 1);
                        String currentSadesati = currentJsonObject.getString("Sade Sati");
                        String currentRashi = currentJsonObject.getString("Shani Rashi");
                        String currentPhase = currentJsonObject.getString("Phase");
                        String currentStartDate = currentJsonObject.getString("Start Date");
                        String currentEndDate = currentJsonObject.getString("End Date");
                        String nextPhase = nextJsonObject.getString("Phase");

                        if (preJsonObject != null) {
                            prePhase = preJsonObject.getString("Phase");
                        }
                        if (currentPhase.equals("Rising")) {
                            if (nextPhase.equals("Rising")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate + " " + context.getResources().getString(R.string.before_enddate_des_text_temporarily2));
                                }
                            }
                        } else if (currentPhase.equals("Setting")) {
                            if (nextPhase.equals("Setting")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + " " + currentEndDate + " " + context.getResources().getString(R.string.before_enddate_des_text_temporarily2));
                                }
                                // showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_temporarily), context.getResources().getString(R.string.before_enddate_des_text_temporarily1) + currentEndDate);


                            } else if (nextPhase.equals("Rising")) {
                                if (languageCode == CGlobalVariables.ENGLISH) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_permanently), context.getResources().getString(R.string.before_enddate_des_text_permanently1) + " " + currentEndDate);
                                } else if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.TAMIL) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.before_enddate_heading_text_permanently), context.getResources().getString(R.string.before_enddate_des_text_permanently1) + " " + currentEndDate + " " + context.getResources().getString(R.string.before_enddate_des_text_permanently2));
                                }
                            }
                        }
                        String str = CUtils.getFortyDaysBeforeDate(nextJsonObject.getString("End Date"), "MMMM dd, yyyy");
                        CUtils.saveStringData(context, "SadestiDateBeforeEndDate", str);
                        CUtils.saveIntData(context, "sadesatidashaarrayindex3", currentIndex3 + 1);
                    } else {
                        if (CUtils.checkDate(calendar.getTime(), CUtils.convertStringToDate(endDate, "MMMM dd, yyyy"))) {
                            JSONObject preJsonObject = null;
                            String prePhase = "";
                            if (currentIndex4 > 0) {
                                preJsonObject = jsonArray.getJSONObject(currentIndex4 - 1);
                            }
                            JSONObject currentJsonObject = jsonArray.getJSONObject(currentIndex4);
                            JSONObject nextJsonObject = jsonArray.getJSONObject(currentIndex4 + 1);
                            String currentSadesati = currentJsonObject.getString("Sade Sati");
                            String currentRashi = currentJsonObject.getString("Shani Rashi");
                            String currentPhase = currentJsonObject.getString("Phase");
                            String currentStartDate = currentJsonObject.getString("Start Date");
                            String currentEndDate = currentJsonObject.getString("End Date");
                            String nextPhase = nextJsonObject.getString("Phase");

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
                                } else if (nextPhase.equals("Rising")) {
                                    showSadesatiNotification(context, name + context.getResources().getString(R.string.on_enddate_heading_text_permanently), context.getResources().getString(R.string.on_enddate_des_text_permanently));
                                }
                            }
                            String str = nextJsonObject.getString("End Date");
                            CUtils.saveStringData(context, "SadesatiEndDate", str);
                            CUtils.saveIntData(context, "sadesatidashaarrayindex4", currentIndex4 + 1);
                        }
                    }

                }
            }

        } catch (Exception e) {

        }
    }

  /*  private void checkPhaseAndShowNotification(Context context, String name, String sadesati, String shaniRashi, String startDate, String phase, String endDate) {
        if (phase.equals("Rising")) {
            showSadesatiNotification(context, name + ", Your " + sadesati + " " + shaniRashi + " is going to start", "Tap to check result of " + sadesati + " starting from " + startDate);
        } else if (phase.equals("Peak")) {
            showSadesatiNotification(context, name + ", Your " + sadesati + " " + shaniRashi + " phase is changing", "Tap to check result of " + sadesati + " " + phase + " phase starting from " + startDate);
        } else if (phase.equals("Setting")) {
            showSadesatiNotification(context, "Your " + sadesati + " " + shaniRashi + " is going to start", "Know how will be your " + sadesati + " starting from " + startDate);
        }
    }*/


    /**
     * this function is used to show birthday notification.
     *
     * @param context
     * @param name
     */
    @SuppressLint("NewApi")
    private void showHappyBirthDayNotification(Context context,
                                               String name) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, HomeInputScreen.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_VARSHAPHAL);

        int notificationId = LibCUtils.getRandomNumber();
        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getResources().getString(R.string.happy_birthday)
                        + " " + name)
                .setContentText(context.getResources().getString(R.string.happy_birthday_msg))
                .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setAutoCancel(true).setStyle((new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(
                        R.string.happy_birthday_msg))));
      /*  Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.happy_birthday)
                        + " " + name)
                .setStyle(new Notification.BigTextStyle().bigText(context.getResources().getString(
                        R.string.happy_birthday_msg)))
                .setContentText(context.getResources().getString(R.string.happy_birthday_msg))
                .setSmallIcon(R.drawable.magz_notification_icon)
                .setContentIntent(contentIntent).setAutoCancel(true).build();*/
        mNotificationManager.notify(1004, notification.build());
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

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, HomeInputScreen.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_DASA);

        int notificationId = LibCUtils.getRandomNumber();
        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading).setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent).setAutoCancel(true);
        mNotificationManager.notify(1005, notification.build());

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

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, HomeInputScreen.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_DASA);

        int notificationId = LibCUtils.getRandomNumber();
        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading).setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent).setAutoCancel(true);
        mNotificationManager.notify(1006, notification.build());

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

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, HomeInputScreen.class);

        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, 5);
        notificationIntent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                CGlobalVariables.MODULE_PREDICTION);

        int notificationId = LibCUtils.getRandomNumber();
        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), com.libojassoft.android.R.mipmap.icon);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading)
                .setContentText(content)
                .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true).setStyle((new NotificationCompat.BigTextStyle().bigText(content)));
       /* NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(heading)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentText(content)
                .setSmallIcon(R.drawable.magz_notification_icon)
                .setContentIntent(contentIntent).setAutoCancel(true).build();*/
        mNotificationManager.notify(1007, notification.build());
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

    public static String convertDateStringInDifferentFormat(String startDate, String formate1, String formate2) {
        Date date = CUtils.convertStringToDate(startDate, formate1);// "dd/MM/yy"
        Format formatter = new SimpleDateFormat(formate2);
        String dateStr = formatter.format(date);
        return dateStr;
    }

    private String getDashaName(String dasha) {
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
            String spilt[] = name.trim().split("\\s+");
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
}