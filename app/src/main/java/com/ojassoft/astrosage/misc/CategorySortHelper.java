package com.ojassoft.astrosage.misc;

import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.fAfter40CategoryList;
import static com.ojassoft.astrosage.utils.CGlobalVariables.m19_25CategoryList;
import static com.ojassoft.astrosage.utils.CGlobalVariables.mAfter40CategoryList;

import android.app.Person;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.os.Build;
import android.util.Log;

import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.model.UserProfileData;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Author: Gaurav
// Created on: 15-04-2025
public class CategorySortHelper {

    /**
     * Sends all screen_id counts to the new API once every 24 hours if due.
     * Only sends categories with a count greater than 0. Uses SharedPreferences to store the last sync time.
     * API endpoint format: https://talk.astrosage.com/sdk/screen-count-data?screenid=[{"screen_id":"176","count":"3"},...]
     *
     * @param context The application context used to access SharedPreferences.
     */
    public static void sendAllScreenCountsIfDue(Context context) {
        final String PREF_LAST_SYNC = "all_screen_counts_last_sync";
        final long INTERVAL_24H = 24 * 60 * 60 * 1000L;
        long lastSync = CUtils.getLongData(context, PREF_LAST_SYNC, 0);
        long now = System.currentTimeMillis();
        if (now - lastSync < INTERVAL_24H) {
            Log.i("CategorySortHelper", "Not syncing screen counts: last sync was less than 24h ago");
            return;
        }
        try {
            // Build the screenid param as JSON array string
            org.json.JSONArray arr = new org.json.JSONArray();
            for (PersonalizedCategoryENUM cat : PersonalizedCategoryENUM.values()) {
                String key = getClickCountKeyForCategory(cat);
                int count = CUtils.getIntData(context, key, 0);
                if (count > 0) {
                    org.json.JSONObject obj = new org.json.JSONObject();
                    obj.put("screen_id", String.valueOf(cat.getScreenID()));
                    obj.put("count", String.valueOf(count));
                    arr.put(obj);
                }
            }
           // Log.e("CategorySortHelper", "screen count Details: " + arr.toString());
            if (arr.length() == 0) {
              //  Log.i("CategorySortHelper", "No screen counts > 0, nothing to sync.");
                return;
            }
            String screenidJson = arr.toString();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.updateClickCount(getCountUpdateParams(context, screenidJson));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                      //  Log.i("CategorySortHelper", "All screen counts synced successfully");
                        CUtils.saveLongData(context, PREF_LAST_SYNC, System.currentTimeMillis());
                    } else {
                        try {
                       //     Log.w("CategorySortHelper", "Failed to sync all screen counts. Response code: " + response.code());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("CategorySortHelper", "Error syncing all screen counts", t);
                }
            });
        } catch (Exception e) {
            Log.e("CategorySortHelper", "Exception in sendAllScreenCountsIfDue", e);
        }
    }

    private static Map<String, String> getCountUpdateParams(Context context, String screenidJson) {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("screenid", screenidJson);
            params.put("packagename", ASTROSAGE_AI_PACKAGE_NAME);
            params.put("userid", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(context));//TODO mobile number without country code
            params.put("deviceid", com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(context));
            params.put("osname", "android");
            params.put("key", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
            params.put("countrycode", CUtils.getStringData(context, CGlobalVariables.COUNTRY_CODE, "91"));
        } catch (Exception e) {
            Log.e("CategorySortHelper", "getCountUpdateParams:exception-- " + e.getMessage());
        }
        return params;
    }


    // Method to update click count when a user interacts with a category.

    /**
     * Updates the click count for a specified category both locally and on the server.
     *
     * @param context      The application context.
     * @param categoryEnum The category enum to update.
     */
    public static void updateClickCount(Context context, PersonalizedCategoryENUM categoryEnum) {
        String key = getClickCountKeyForCategory(categoryEnum);
        int clickCount = CUtils.getIntData(context, key, 0);
        CUtils.saveIntData(context, key, ++clickCount); // update preference with increased click count
        sendAllScreenCountsIfDue(context);
    }


    /**
     * Determines a personalized category list based on user profile data such as age, gender, and marital status.
     *
     * @param userProfileData The user's profile data.
     * @return An array of personalized category names.
     */
    public static String[] givePersonalizeCategoryList(UserProfileData userProfileData) {
        try {
            if (userProfileData == null) {
                return CGlobalVariables.below19UnmarriedCategoryList;
            } else if (userProfileData.getName().isEmpty() || userProfileData.getGender().isEmpty() || userProfileData.getYear().isEmpty()) {
                return CGlobalVariables.below19UnmarriedCategoryList;
            }
            LocalDate birthDate = null;
            LocalDate currentDate = null;
            int age;
            // by @gaurav to calculate exact age , fallback is handled in else condition with rough age calculation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                birthDate = LocalDate.of(Integer.parseInt(userProfileData.getYear()),
                        Integer.parseInt(userProfileData.getMonth()) + 1, // Add 1 to month for 1-based index
                        Integer.parseInt(userProfileData.getDay()));
                currentDate = LocalDate.now();
                age = Period.between(birthDate, currentDate).getYears();
            } else {
                age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(userProfileData.getYear());
            }

            String maritalStatus = userProfileData.getMaritalStatus();
            String gender = userProfileData.getGender();

            if (age < 19) {  //all gender below age 19
                return CGlobalVariables.below19UnmarriedCategoryList;
            } else if (age < 25) {
                if (gender.equalsIgnoreCase("F")) { // female 19-24 age group
                    if (maritalStatus.equalsIgnoreCase("married")) {
                        return CGlobalVariables.f19_25MarriedCategoryList;
                    } else {
                        return CGlobalVariables.f19_25UnmarriedCategoryList;
                    }
                } else { // male  19-24 age group
                    return CGlobalVariables.m19_25CategoryList;
                }

            } else if (age < 35) {
                if (gender.equalsIgnoreCase("F")) { // female 25-35 age group
                    if (maritalStatus.equalsIgnoreCase("married")) {
                        return CGlobalVariables.f25_35MarriedCategoryList;
                    } else {
                        return CGlobalVariables.f25_35UnmarriedList;
                    }
                } else {//male 25-35 age group
                    return CGlobalVariables.m25_35CategoryList;
                }
            } else {
                if (gender.equalsIgnoreCase("F")) { // female above 35 age group
                    return fAfter40CategoryList;
                } else {
                    return mAfter40CategoryList;
                }
            }
        } catch (Exception e) {
            return CGlobalVariables.m19_25CategoryList;
        }
    }

    /**
     * Sorts the specified list of category enums in-place by their click count in descending order.
     *
     * @param categoryEnums The list of category enums to sort.
     */
    public static void applySortByClickCount(ArrayList<PersonalizedCategoryENUM> categoryEnums) {

        HashMap<PersonalizedCategoryENUM, Integer> clickCountMap = new HashMap<>();
        if (categoryEnums != null && !categoryEnums.isEmpty()) {
            for (PersonalizedCategoryENUM personalizedCategoryENUM : categoryEnums) {
                String key = getClickCountKeyForCategory(personalizedCategoryENUM);
                int clickCount = CUtils.getIntData(AstrosageKundliApplication.getAppContext(), key, 0);
                clickCountMap.put(personalizedCategoryENUM, clickCount);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                categoryEnums.sort((a, b) -> clickCountMap.get(b) - clickCountMap.get(a));
            }
        }
    }

    /**
     * Retrieves the shared preference key used for storing the click count of a specified category.
     *
     * @param categoryEnum The category enum.
     * @return The key string used for shared preferences.
     */
    public static String getClickCountKeyForCategory(PersonalizedCategoryENUM categoryEnum) {
        return categoryEnum.name() + "_" + categoryEnum.getScreenID() + "_count";
    }


//    public static ArrayList<PersonalizedCategoryENUM> getRecommendedCategories(ArrayList<PersonalizedCategoryENUM> categoryEnums, UserProfileData userProfileData) {
//        try {
//            // Map to store category priority scores
//            Map<PersonalizedCategoryENUM, Integer> priorityMap = new HashMap<>();
//
//            for (PersonalizedCategoryENUM category : categoryEnums) {
//                int score = 0;
//                LocalDate birthDate = null;
//                LocalDate currentDate = null;
//                int age;
//
//                // by @gaurav to calculate exact age , fallback is handled in else condition with rough age calculation
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    birthDate = LocalDate.of(Integer.parseInt(userProfileData.getYear()),
//                            Integer.parseInt(userProfileData.getMonth()),
//                            Integer.parseInt(userProfileData.getDay()));
//                    currentDate = LocalDate.now();
//                    age = Period.between(birthDate, currentDate).getYears();
//                } else {
//                    age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(userProfileData.getYear());
//                }
//
//
//                // Age-based priority
//                if (age < 18) {
//                    if (category == EDUCATION) score += 5;
//                    if (category == CAREER) score += 3;
//                } else if (age < 35) {
//                    if (category == CAREER) score += 5;
//                    if (category == BUSINESS) score += 4;
//                    if (category == MARRIAGE) score += 3;
//                    if (category == STOCK_MARKET) score += 2;
//                } else if (age < 50) {
//                    if (category == HEALTH) score += 5;
//                    if (category == STOCK_MARKET) score += 4;
//                    if (category == BUSINESS) score += 3;
//                } else {
//                    if (category == HEALTH) score += 5;
//                    if (category == POLITICS) score += 4;
//                }
//
//                // Occupation-based priority
//                switch (userProfileData.getOccupation().toLowerCase()) {
//                    case "student":
//                        if (category == EDUCATION) score += 5;
//                        if (category == CAREER) score += 4;
//                        if (category == COUNSELLOR) score += 3;
//                        break;
//
//                    case "businessperson":
//                        if (category == BUSINESS) score += 5;
//                        if (category == STOCK_MARKET) score += 4;
//                        if (category == CAREER) score += 3;
//                        break;
//
//                    case "employee":
//                        if (category == CAREER) score += 5;
//                        if (category == BUSINESS) score += 3;
//                        if (category == STOCK_MARKET) score += 2;
//                        break;
//
//                    case "retired":
//                        if (category == HEALTH) score += 5;
//                        if (category == POLITICS) score += 4;
//                        if (category == COUNSELLOR) score += 3;
//                        break;
//
//                    case "housewife":
//                        if (category == MOTHERHOOD) score += 5;
//                        if (category == HEALTH) score += 4;
//                        if (category == WEIGHT_LOSS) score += 3;
//                        break;
//                }
//
//                // Gender-based priority
//                if (userProfileData.getGender().equalsIgnoreCase("M")) {
//                    if (category == IPL) score += 5;
//                    if (category == BUSINESS) score += 3;
//                } else if (userProfileData.getGender().equalsIgnoreCase("F")) {
//                    if (category == MOTHERHOOD) score += 5;
//                    if (category == WEIGHT_LOSS) score += 3;
//                }
//
//                // Marital Status-based priority
//                if (userProfileData.getMaritalStatus().equalsIgnoreCase("Married")) {
//                    if (category == MARRIAGE) score += 5;
//                    if (category == HEALTH) score += 3;
//                } else if (userProfileData.getMaritalStatus().equalsIgnoreCase("Single")) {
//                    if (category == LOVE) score += 5;
//                    if (category == CAREER) score += 3;
//                } else if (userProfileData.getMaritalStatus().equalsIgnoreCase("Divorced")) {
//                    if (category == COUNSELLOR) score += 5;
//                    if (category == HEALTH) score += 4;
//                    if (category == LEGAL) score += 3;
//                }
//
//                priorityMap.put(category, score );
//            }
//
//            // Sort categories based on priority score in descending order
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                categoryEnums.sort((a, b) -> priorityMap.get(b) - priorityMap.get(a));
//            }
//
//            applySortByClickCount(categoryEnums);
//
//            // Most recent clicks first
//        } catch (Exception e) {
//            Log.e("error", "getRecommendedCategories: Exception = "+e );
//        }
//
//        return categoryEnums;
//    }


}
