package com.libojassoft.android.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.ui.LibActAstroShop;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.libojassoft.android.utils.LibCGlobalVariables.URL;


/**
 * This class is a collection of misc. functions
 *
 * @author Bijendra
 */
public class LibCUtils {

    /**
     * This function is used to return dynamic notification id
     *
     * @return notification id
     * @author ADDED BY BIJENDRA ON 19-06-14
     */
    public static int getCustomNotificationId() {
        Calendar c = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();

        sb.append(c.get(Calendar.DAY_OF_MONTH));
        sb.append(c.get(Calendar.MINUTE));
        sb.append(c.get(Calendar.SECOND));

        return Integer.valueOf(sb.toString());

    }
    /*
     * @SuppressLint("SdCardPath") public static String
     * FILENAME="/sdcard/aslogfile.txt";
     */

    /**
     * This function is used to init list view
     *
     * @param context
     * @param listView
     * @param menuArray []
     * @param layout
     */
    public static void initListView(Context context, ListView listView,
                                    String[] menuArray, int layout) {

        listView.setAdapter(new ArrayAdapter<String>(context, layout, menuArray));
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Context context = view.getContext();
                // String msg = "item[" + position + "]=" +
                // parent.getItemAtPosition(position);
                // Toast.makeText(context, msg, 1000).show();
                // System.out.println(msg);

            }
        });
    }

    /**
     * This function check the device is connect with internet.
     *
     * @param context
     * @return boolean
     */
    public static boolean isConnectedWithInternet(Context context) {
        // return true;
        boolean _isNetAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null) {
            _isNetAvailable = wifiNetwork.isConnectedOrConnecting();
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null) {
            _isNetAvailable = mobileNetwork.isConnectedOrConnecting();
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            _isNetAvailable = activeNetwork.isConnectedOrConnecting();
        }
        // CGlobalVariables._isInternetOn = _isNetAvailable;

        return _isNetAvailable;

    }

    /**
     * This function is use to get notification id ,saved in preference
     *
     * @param context
     * @param key
     * @return String
     */
    public static String getAppNotificationIdPreference(Context context,
                                                        String key) {
        SharedPreferences settings = context.getSharedPreferences(
                LibCGlobalVariables.APP_NOTIFICATION_PREFERENCE, 0);

        String appId = settings.getString(key, "-1");

        return appId;

    }

    /**
     * This function is used to save notification id in preference.
     *
     * @param context
     * @param packageName
     * @param notificationId
     */
    public static void setAppNotificationIdPreference(Context context,
                                                      String packageName, String notificationId) {
        SharedPreferences settings = context.getSharedPreferences(
                LibCGlobalVariables.APP_NOTIFICATION_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(packageName, notificationId);
        editor.commit();

    }

    /**
     * This function return preference id based on called application(package
     * name).
     *
     * @param package_name
     * @return String
     */
    public static String getPreferenceKey(String package_name) {
        String strKey = package_name.replace(".", "_");
        strKey = LibCGlobalVariables.PREFIX_PREF_KEY + strKey;

        return strKey;
    }

    /**
     * This function is used to get device size used to show Banner on
     * notification.
     *
     * @param deviceHeight
     * @param deviceWidth
     * @return int
     */
    public static int getDeviceSize(int deviceHeight, int deviceWidth) {
        int deviceSize = 0;

        if (deviceWidth <= 240)
            deviceSize = LibCGlobalVariables.DEVICE_SMALL;

        if ((240 < deviceWidth) & (deviceWidth <= 320))
            deviceSize = LibCGlobalVariables.DEVICE_NORMAL;

        if ((320 < deviceWidth) & (deviceWidth <= 480))
            deviceSize = LibCGlobalVariables.DEVICE_LARGE;

        if (deviceWidth > 480)
            deviceSize = LibCGlobalVariables.DEVICE_XLARGE;

        return deviceSize;
    }

    /**
     * This function check that the given package is called first time or not
     *
     * @param context
     * @param package_name
     * @return boolean
     */
    public static boolean isCalledFirstTimeByPackage(Context context,
                                                     String package_name) {

        SharedPreferences settings = context.getSharedPreferences(
                LibCGlobalVariables.APP_NOTIFICATION_PREFERENCE, 0);

        boolean isFirstTime = settings.getBoolean(package_name, true);
        if (isFirstTime) {
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean(package_name, false);
            editor.commit();
        }
        return isFirstTime;
    }

    /*
     * public static void startNotificationService(Context context) {
     * context.startService(new
     * Intent(context,AstroSageMagazineNotificationService.class)); }
     */
    @SuppressWarnings("unused")
    public static void checkSettingDirecoryExists() {
        File mySettingFolder = new File(
                LibCGlobalVariables.SETTING_DIRECTORY_NAME);
        if (!mySettingFolder.exists()) {
            mySettingFolder.mkdir();
        }
    }

   /* public static String getMagazineNotificationId() throws IOException {
        String magzineId = "";
        FileInputStream fIn = null;
        BufferedReader myReader = null;
        try {

            File myFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME);
            fIn = new FileInputStream(myFile);
            myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }

            magzineId = aBuffer.toString();
            // Log.d("SERVICE_READ", aBuffer.toString());
        } catch (Exception e) {

        } finally {
            if (fIn != null && myReader != null) {
                myReader.close();
                fIn.close();
            }
        }
        return magzineId.trim();
    }*/

    /*public static String getMagazineNotificationIdHindi() throws IOException {
        String magzineId = "";
        FileInputStream fIn = null;
        BufferedReader myReader = null;
        try {

            File myFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME_HINDI);
            fIn = new FileInputStream(myFile);
            myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }

            magzineId = aBuffer.toString();
            // Log.d("SERVICE_READ", aBuffer.toString());
        } catch (Exception e) {

        } finally {
            if (fIn != null && myReader != null) {
                myReader.close();
                fIn.close();
            }
        }
        return magzineId.trim();
    }*/

   /* public static void saveMagazineNotificationJSONObject(String magzId) {
        try {
            checkSettingDirecoryExists();
            File magzFile = new File(
                    LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME);
            if (!magzFile.exists())
                magzFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(magzFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(magzId);
            myOutWriter.close();
            fOut.close();
            // Log.d("SERVICE_WRITE", "Bijendra");
        } catch (Exception e) {

        }

    }*/

    public static boolean isMagazineNotificationServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            // Log.d("Service", service.service.getClassName());
            if (LibCGlobalVariables.MAGZINE_NOTIFICATION_SERVICE_NAME
                    .equalsIgnoreCase(service.service.getClassName())) {
                // Log.d("Service found", service.service.getClassName());
                return true;
            }
        }
        return false;
    }

    /**
     * This function is used to store user choise on Disclaimer in appplication
     * preference
     *
     * @param context
     * @param resultCodeDesclaminer
     * @author Bijendra : 17-april-2013
     */
    public static void setDisclaimerAgreementInPreference(Context context,
                                                          int resultCodeDesclaminer) {
        SharedPreferences settings = context.getSharedPreferences(
                LibCGlobalVariables.DISCLAIMER_AGREE_PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(LibCGlobalVariables.DISCLAIMER_AGREE_RESULT_CODE,
                resultCodeDesclaminer);
        editor.commit();

    }

    /**
     * This function is used to return user choise on Disclaimer from
     * application preference
     *
     * @param context
     * @return int
     * @author Bijendra : 17-april-2013
     */
    public static int isUserAgreeForDisclaimer(Context context) {
        int result = LibCGlobalVariables.CONST_DISCLAIMER_DISAGREE;
        SharedPreferences settings = context.getSharedPreferences(
                LibCGlobalVariables.DISCLAIMER_AGREE_PREFS_NAME,
                Context.MODE_PRIVATE);
        result = settings.getInt(
                LibCGlobalVariables.DISCLAIMER_AGREE_RESULT_CODE,
                LibCGlobalVariables.CONST_DISCLAIMER_DISAGREE);

        return result;

    }

    /**
     * This function return formatted date in number(DDMMYYY)
     *
     * @return String
     */
    public static String getFormattedTodayDate() {
        String _datFormat = null;
        Date pubDate = new Date();
        _datFormat = String.valueOf(pubDate.getDate())
                + String.valueOf(pubDate.getMonth())
                + String.valueOf(pubDate.getYear());

        return _datFormat;
    }

    /**
     * This function is sued to return JSONObject that is save in external SD
     * card
     *
             * @return JSONObject
     */
    public static JSONObject getSavedJsonMagazineDetailObject(Context context) {
       /* boolean isDirFileExist = true;
        JSONObject savedObj = null;
        String savedString = null;
        try {
            // savedString = getMagazineNotificationId();MODIFIED BY BIJENDRA ON
            // 8-JULY-13
            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME).exists())
                isDirFileExist = false;

            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME)
                    .exists())
                isDirFileExist = false;

            if (isDirFileExist) {
                savedString = getMagazineNotificationId();// MODIFIED BY
                // BIJENDRA ON
                // 8-JULY-13
                savedObj = new JSONObject(savedString);
            }
        } catch (Exception e) {

        }*/
        JSONObject savedObj = null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
            String savedString = sharedPreferences.getString(LibCGlobalVariables.EN_MAGAZINE_NOTIFICATION_PREF_KEY, "");
            if (!TextUtils.isEmpty(savedString)) {
                savedObj = new JSONObject(savedString);
            }
        } catch (Exception e) {
            Log.i("", e.getMessage());
        }
        return savedObj;
    }

    public static JSONObject getSavedJsonMagazineDetailObjectHindi(Context context) {
       /* boolean isDirFileExist = true;
        JSONObject savedObj = null;
        JSONObject hindiBlogJson = null;// ADDED BY HUKUM
        String savedString = null;
        try {
            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME).exists())
                isDirFileExist = false;

            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME_HINDI)
                    .exists())
                isDirFileExist = false;

            if (isDirFileExist) {
                savedString = getMagazineNotificationIdHindi();
                savedObj = new JSONObject(savedString);
                hindiBlogJson = savedObj
                        .getJSONObject(LibCGlobalVariables.HINDI_BLOG_SETTING_JSON_OBJECT_NAME);// Added
                // By
                // Hukum
            }
        } catch (Exception e) {

        }*/
        JSONObject savedObj = null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
            String savedString = sharedPreferences.getString(LibCGlobalVariables.HI_MAGAZINE_NOTIFICATION_PREF_KEY, "");
            if (!TextUtils.isEmpty(savedString)) {
                savedObj = new JSONObject(savedString);
            }
        } catch (Exception e) {
            Log.i("", e.getMessage());
        }
        return savedObj;
    }

    public static String formatNotificationDescription(String notifText,
                                                       String removeText, int stringLengthToReturn) {
        String retVal = notifText;

        int len = retVal.indexOf(removeText);
        if (len > 0) {
            retVal = retVal.substring(len + "title=".length() + 5);
            retVal = retVal.substring(0, stringLengthToReturn);
            retVal += "...";

            // str=str.substring(str.indexOf("title=\"\""), 50);
            return Html.fromHtml(retVal).toString();
        } else
            return retVal;
    }

    /**
     * This function is used to return application version code
     *
     * @param context
     * @return String
     */

    public static String getApplicationVersionToShow(Context context) {
        // String version ="";
        try {
            // MODIFIED BY BY BIJENDRA ON 09-MAY-2013
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            /*
             * PackageInfo pInfo =
             * context.getPackageManager().getPackageInfo(context
             * .getPackageName(), 0); version = pInfo.versionName;
             */

        } catch (Exception e) {
            // Log.d("VERSION-Error", e.getMessage());
        }
        // return version;
        return "";
    }

    /**
     * Added by Hukum : Because app was not showing in google play for tablets.
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    public static boolean isSupportPhoneCalls(Context context) {
        try {
            // MODIFIED BY BY BIJENDRA ON 09-MAY-2013
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                return context.getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_TELEPHONY);
            }
            /*
             * PackageManager pm = context.getPackageManager(); return
             * pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
             */
        } catch (Exception activityException) {
            // //Log.e("helloandroid dialing example", "Call failed");
        }
        return false;
    }

    /**
     * This function is used to check that SD card is available to write
     *
     * @return boolean
     * @author Bijendra(09 - may - 2013)
     */
    public static boolean isExternalStorageAvailableToWrite() {
        String externalStroareState = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(externalStroareState))
            return isFreeMemoryAvailableToWrite();
        else
            return false;
    }

    /**
     * This function is used to check that SD card is available to Read
     *
     * @return boolean
     * @author Bijendra(09 - may - 2013)
     */
    /*public static boolean isExternalStorageAvailableToRead() {
        String externalStroareState = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(externalStroareState)
                || Environment.MEDIA_MOUNTED_READ_ONLY
                .equals(externalStroareState))
            return true;
        else
            return false;

    }*/

    /**
     * This function is used to check that there is free memory on SD card to
     * write
     *
     * @return boolean
     * @author Bijendra(09 - may - 2013)
     */
    public static boolean isFreeMemoryAvailableToWrite() {
        //We are using internal storage to save data that's why we have to use Environment.getExternalStorageDirectory().getPath()
        //StatFs staf = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        StatFs staf = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long free = ((long) staf.getAvailableBlocks() * (long) staf
                .getBlockSize()) / 1048576;

        if (free > 0)
            return true;
        else
            return false;

    }

    /**
     * This function is used to check internet connection
     *
     * @param context
     * @return boolean
     * @author Bijendra (10-may-13)
     */
    public static boolean isInternetOn(Context context) {
        boolean isNetOn = false;
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connect
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connect
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (info != null)
            isNetOn = info.isConnected();

        if (wifi != null && !isNetOn)
            isNetOn = wifi.isConnected();

        return isNetOn;
    }

    /**
     * This function checks weather the passed package is installed in device or
     * not.
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalledAlready(Context context,
                                                String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * This function is used to check that this device is phone or tablet
     *
     * @param context
     * @return boolean
     * @author Bijendra on 25-may-2013
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static boolean isTablet(Context context) {

        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void goToAstroSageShop(Context context, String pageIndex) {
        int iPageIndex = 0;
        try {
            iPageIndex = Integer.valueOf(pageIndex.trim());
        } catch (Exception e) {

        }
        Intent intentAstroShop = new Intent(context, LibActAstroShop.class);
        intentAstroShop.putExtra("ASTRO_SHOP_PAGE_INDEX", iPageIndex);
        intentAstroShop.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAstroShop);
    }

    /**
     * This function is used to return astro shop page url according to passed
     * page index
     *
     * @param pageIndex
     * @return page url
     * @author Bijendra(10 - june - 13)
     */
    public static String getAstroSageShopPage(int pageIndex, Context context) {
        return LibCGlobalVariables.LIB_ASTRO_SHOP_PAGES[pageIndex]
                + getGoogleAnalyticapplicationSrc(context);

    }

    public static String getGoogleAnalyticapplicationSrc(Context context) {
        if (context.getApplicationContext().getPackageName()
                .equalsIgnoreCase("com.ojassoft.astrosage"))
            return LibCGlobalVariables.GOOGLE_ANALYTIC_SRC_ASTROSAGEKUNDLI;

        if (context.getApplicationContext().getPackageName()
                .equalsIgnoreCase("com.ojassoft.MyKundliMatchMaking"))
            return LibCGlobalVariables.GOOGLE_ANALYTIC_SRC_HOROSCOPEMATCHING;

        if (context.getApplicationContext().getPackageName()
                .equalsIgnoreCase("com.ojassoft.Horoscope2012"))
            return LibCGlobalVariables.GOOGLE_ANALYTIC_SRC_HOROSCOPE2013;

        return "0";
    }

    public static String getGoogleAnalyticapplicationApplicationName(
            Context context) {
        if (context.getApplicationContext().getPackageName()
                .equalsIgnoreCase("com.ojassoft.astrosage"))
            return LibCGlobalVariables.GOOGLE_ANALYTIC_ASTROSAGEKUNDLI_NAME;

        if (context.getApplicationContext().getPackageName()
                .equalsIgnoreCase("com.ojassoft.MyKundliMatchMaking"))
            return LibCGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPEMATCHING_NAME;

        if (context.getApplicationContext().getPackageName()
                .equalsIgnoreCase("com.ojassoft.Horoscope2012"))
            return LibCGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE2012_NAME;

        return "0";
    }

    public static String getNotificationCompaignURL(Context context) {
        String add_to_url = LibCGlobalVariables.COMPAIGN_URL_NOTIFICATION;
        String tSrc = getGoogleAnalyticapplicationSrc(context);
        String tAppName = getGoogleAnalyticapplicationApplicationName(context);

        add_to_url = add_to_url.replace("app_src", tSrc);
        add_to_url = add_to_url.replace("app_name", tAppName);

        return add_to_url;
    }

    /**
     * This function is used to change font type for view in ViewGroup
     *
     * @param root
     * @return
     * @author Bijendra (Modified on 11-july-13)
     */
    public static void changeAllViewsFonts(ViewGroup root, Typeface fontType) {

        // Typeface tf = Typeface.createFromAsset(getAssets(),
        // "fonts/comicsans.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                try {
                    TextView tv = ((TextView) v);
                    // if(!checkTextView(tv.getId()))
                    tv.setTypeface(fontType);
                } catch (Exception e) {

                }

            } else if (v instanceof Button) {
                try {
                    ((Button) v).setTypeface(fontType);
                } catch (Exception e) {

                }

            } else if (v instanceof RadioButton) {
                try {

                    ((RadioButton) v).setTypeface(fontType);
                } catch (Exception e) {

                }

            } else if (v instanceof CheckBox) {
                try {
                    ((CheckBox) v).setTypeface(fontType);
                } catch (Exception e) {

                }

            } else if (v instanceof ViewGroup) {
                changeAllViewsFonts((ViewGroup) v, fontType);
            }
        }
    }

    /**
     * This function is used to change resource type for application, according
     * to the specific language
     *
     * @param context
     * @param englishOHindi
     * @author Bijendra(11 - july - 13)
     */
    public static void changeAppResourceTypeForLanguage(Context context,
                                                        int englishOHindi) {
        Locale locale = null;
        if (englishOHindi == LibCGlobalVariables.LIB_HINDI)
            locale = new Locale("hi");
        else
            locale = new Locale("en");

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());

    }

    /**
     * This function is sued to start other application
     *
     * @param context
     * @param packageName
     * @param activityNameWithPackage
     * @author Bijendra(11 - july - 13)
     */
    public static void startOtherApplication(Context context,
                                             String packageName, String activityNameWithPackage) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // intent.setComponent(new
        // ComponentName("com.package.address","com.package.address.MainActivity"));
        intent.setComponent(new ComponentName(packageName,
                activityNameWithPackage));
        context.startActivity(intent);
    }

    // START HOROSCOPE MATCHMAKING
    public static boolean isSupportUnicodeHindi() {
        boolean isAvailble = false;
        Locale[] local = Locale.getAvailableLocales();
        for (int i = 0; i < local.length; i++) {
            if (local[i].getLanguage().equalsIgnoreCase(
                    LibCGlobalVariables.HINDI_LOCALE_CODE)) {
                isAvailble = true;
            }
        }

        // ADDED BY BIJENDRA ON 23-JULY-13
        if (!isAvailble) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
                isAvailble = true;
        }
        return isAvailble;

    }

    /**
     * This function return the language code chossen by user
     *
     * @return int
     * @author Bijendra(25 - july - 13)
     */
    public static int getUserLanguageCode() {
        int languageCode = LibCGlobalVariables.LIB_ENGLISH;
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            if (obj != null)
                languageCode = obj
                        .getInt(LibCGlobalVariables.JSON_USER_LANGUAGE);

        } catch (Exception e) {

        }
        return languageCode;
    }

    /**
     * This function is used to return user login/pwd if they are saved
     *
     * @return String[]
     * @author Bijendra(25 - july - 13)
     */
    public static String[] getUserLoginPwd() {
        String[] userLP = new String[2];
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            if (obj != null) {
                userLP[0] = obj.getString(LibCGlobalVariables.JSON_USER_LOGIN);
                userLP[1] = obj.getString(LibCGlobalVariables.JSON_USER_PWD);
            }
        } catch (Exception e) {

        }
        return userLP;
    }

    /**
     * This function is used to return that user has checked the automatic sign.
     *
     * @return boolean
     * @author Bijendra(25 - july - 13)
     */
    public static boolean isUserAutomaticLogin() {
        boolean _isAutomaticLogin = false;
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            if (obj != null)
                _isAutomaticLogin = obj
                        .getBoolean(LibCGlobalVariables.JSON_USER_AUTO_LOGIN);

        } catch (Exception e) {

        }
        return _isAutomaticLogin;
    }

    /**
     * This function is used to get the user choice for notification.
     *
     * @return int
     * @author Bijendra(25 - july - 13)
     */
    public static int getUserNotificationChoice() {
        int choice = 0;
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            if (obj != null)
                choice = obj
                        .getInt(LibCGlobalVariables.JSON_USER_NOTIFICATION_CHOICE);

        } catch (Exception e) {

        }
        return choice;

    }

    /**
     * This function save the language code selected by user
     *
     * @param languageCode
     * @author Bijendra(25 - july - 13)
     */

    public static void saveUserLanguageCode(int languageCode) {
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            obj.put(LibCGlobalVariables.JSON_USER_LANGUAGE, languageCode);
            saveUserPreferenceJsonObjectFromSDCard(obj);
        } catch (Exception e) {

        }
    }

    /**
     * This function is used to save user login/pwd
     *
     * @param login
     * @param pwd
     * @param isAutomaticLogin
     * @author Bijendra(25 - july - 13)
     */
    public static void saveUserLoginPwd(String login, String pwd,
                                        boolean isAutomaticLogin) {
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            obj.put(LibCGlobalVariables.JSON_USER_LOGIN, login);
            obj.put(LibCGlobalVariables.JSON_USER_PWD, pwd);
            obj.put(LibCGlobalVariables.JSON_USER_AUTO_LOGIN, isAutomaticLogin);
            saveUserPreferenceJsonObjectFromSDCard(obj);
        } catch (Exception e) {

        }
    }

    /**
     * This function is used to save user notification choice
     *
     * @param notificationChoice
     * @author Bijendra(25 - july - 13)
     */
    public static void saveUserNotificationChoice(int notificationChoice) {
        JSONObject obj = null;
        try {
            obj = getUserPreferenceJsonObjectFromSDCard();
            obj.put(LibCGlobalVariables.JSON_USER_NOTIFICATION_CHOICE,
                    notificationChoice);
            saveUserPreferenceJsonObjectFromSDCard(obj);
        } catch (Exception e) {

        }
    }

    /**
     * This function is used to return the json object for saved user
     * preferences(language/login/pwd/isAutomatic sign/notification choice)
     *
     * @return JSONObject
     * @throws IOException
     * @author Bijendra(25 - july - 13)
     */
    private static JSONObject getUserPreferenceJsonObjectFromSDCard()
            throws IOException {
        boolean isDirFileExist = true;
        JSONObject savedObj = null;
        String savedString = null;
        FileInputStream fIn = null;
        BufferedReader myReader = null;
        try {
            savedObj = new JSONObject();
            // savedString = getMagazineNotificationId();MODIFIED BY BIJENDRA ON
            // 8-JULY-13
            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME).exists())
                isDirFileExist = false;

            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.USER_CONFIG_FILE_NAME).exists())
                isDirFileExist = false;

            if (isDirFileExist) {
                File myFile = new File(
                        LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                        LibCGlobalVariables.USER_CONFIG_FILE_NAME);
                fIn = new FileInputStream(myFile);
                myReader = new BufferedReader(new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                savedString = aBuffer.toString();
                savedObj = new JSONObject(savedString);
            }

        } catch (Exception e) {
            //Log.e("Exception", e.getMessage());
        } finally {
            if (fIn != null && myReader != null) {
                myReader.close();
                fIn.close();
            }
        }
        return savedObj;

    }

    /**
     * This function is used to save the json object for user
     * preferences(language/login/pwd/isAutomatic sign/notification choice)
     *
     * @param jobject
     * @throws IOException
     * @author Bijendra(25 - july - 13)
     */
    private static void saveUserPreferenceJsonObjectFromSDCard(
            JSONObject jobject) throws IOException {
        String strToSave = "";
        if (!LibCUtils.isExternalStorageAvailableToWrite())// ADDED BY BIJENDRA
            // ON 09-MAY-2013
            return;
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;

        try {
            strToSave = jobject.toString();
            checkSettingDirecoryExists();
            File magzFile = new File(
                    LibCGlobalVariables.SETTING_DIRECTORY_NAME,
                    LibCGlobalVariables.USER_CONFIG_FILE_NAME);

            if (!magzFile.exists())
                magzFile.createNewFile();

            fOut = new FileOutputStream(magzFile);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(strToSave);

        } catch (Exception e) {
            //Log.e("Exception", e.getMessage());

        } finally {
            myOutWriter.close();
            fOut.close();
        }
    }

    public static int getUserChoiceFromSDCard(Context context) {
        //Added on 7 - feb - 2014 [If there is no SD card notification will not show]
        /*if (!LibCUtils.isExternalStorageAvailableToWrite())
            return LibCGlobalVariables.TWICE_IN_A_DAY;//ADDED BY BIJENDRA ON 15-06-15
   */     /*return LibCGlobalVariables.NEVER;*///DISABLED BY BIJENDRA ON 15-06-15
        //End
        int userChoice = LibCGlobalVariables.AS_MANY_TIME_IT_COMES;
        JSONObject obj = null;
        obj = getSavedMagazineConfigJsonObject(context);
        if (obj != null) {
            try {
                userChoice = obj.getInt(LibCGlobalVariables.JSON_USER_CHOICE);
            } catch (JSONException e) {
                userChoice = LibCGlobalVariables.AS_MANY_TIME_IT_COMES;
            }
        }

        return userChoice;
    }

    public static JSONObject getSavedMagazineConfigJsonObject(Context context) {
       /* boolean isDirFileExist = true;
        JSONObject savedObj = null;
        String savedString = null;
        try {
            //savedString = getMagazineNotificationId();MODIFIED BY BIJENDRA ON 8-JULY-13
            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME).exists())
                isDirFileExist = false;

            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME).exists())
                isDirFileExist = false;

            if (isDirFileExist) {
                savedString = getMagazineNotificationConfig();
                savedObj = new JSONObject(savedString);
            }
        } catch (Exception e) {

        }*/
        JSONObject savedObj = null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
            String savedString = sharedPreferences.getString(LibCGlobalVariables.NOTIFICATION_CHOICE_PREF_KEY, "");
            if (!TextUtils.isEmpty(savedString)) {
                savedObj = new JSONObject(savedString);
            }
        } catch (Exception e) {
            Log.i("", e.getMessage());
        }
        return savedObj;
    }

    /*public static String getMagazineNotificationConfig() throws IOException {
        String magzineId = "";
        FileInputStream fIn = null;
        BufferedReader myReader = null;
        try {

            File myFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME);
            fIn = new FileInputStream(myFile);
            myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }


            magzineId = aBuffer.toString();
            //Log.d("SERVICE_READ", aBuffer.toString());
        } catch (Exception e) {

        } finally {
            if (fIn != null && myReader != null) {
                myReader.close();
                fIn.close();
            }
        }
        return magzineId.trim();
    }*/

    /*public static void googleAnalyticSend(Tracker tracker, String cat, String action, String label, long eventValue){
        try{
              *//*if(tracker==null) {
					tracker = EasyTracker.getTracker();
				}
				//tracker.sendView("Hukum_Page_Daily_monthLy-2");
				//tracker.sendEvent("Hukum_cat-2", "Hukum_action-2", "Hukum_label-2", null);
				tracker.sendEvent(cat, action, label, null);
				EasyTracker.getInstance().dispatch();*//*
			}catch(Exception e){
				e.printStackTrace();
			}
	  }*/
    private static HttpParams getHttpClientTimeoutParameter() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 1000 * 60;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000 * 60;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }

   /* public static int sendFeedbackToAstroSage(String apppName, String name, String emailId, String phone, String message) throws Exception {
        BufferedReader in = null;
        String strReturn = "";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        HttpClient httpclient = new DefaultHttpClient(getHttpClientTimeoutParameter());


        HttpPost httppost = new HttpPost(LibCGlobalVariables.FEED_BACK);
        nameValuePairs.add(new BasicNameValuePair("feedbackfrom", apppName));
        nameValuePairs.add(new BasicNameValuePair("feedbackfrom", apppName));

        nameValuePairs.add(new BasicNameValuePair("feedbackpersonname", name));
        nameValuePairs.add(new BasicNameValuePair(LibCGlobalVariables.KEY_EMAIL_ID, replaceEmailChar(emailId)));
        nameValuePairs.add(new BasicNameValuePair("phoneno", phone));
        nameValuePairs.add(new BasicNameValuePair("message", message));

        if (LibCGlobalVariables.userDetailAvailble) {
            nameValuePairs.add(new BasicNameValuePair(LibCGlobalVariables.KEY_USER_ID, replaceEmailChar(LibCGlobalVariables.useridsession)));
            nameValuePairs.add(new BasicNameValuePair("name", LibCGlobalVariables.name));
            nameValuePairs.add(new BasicNameValuePair("day", LibCGlobalVariables.day));
            nameValuePairs.add(new BasicNameValuePair("month", LibCGlobalVariables.month));
            nameValuePairs.add(new BasicNameValuePair("year", LibCGlobalVariables.year));
            nameValuePairs.add(new BasicNameValuePair("hour", LibCGlobalVariables.hour));
            nameValuePairs.add(new BasicNameValuePair("min", LibCGlobalVariables.min));
            nameValuePairs.add(new BasicNameValuePair("sec", LibCGlobalVariables.sec));
            nameValuePairs.add(new BasicNameValuePair("place", LibCGlobalVariables.place));
            nameValuePairs.add(new BasicNameValuePair("timezone", LibCGlobalVariables.timezone));
            nameValuePairs.add(new BasicNameValuePair("longdeg", LibCGlobalVariables.longdeg));
            nameValuePairs.add(new BasicNameValuePair("longmin", LibCGlobalVariables.longmin));
            nameValuePairs.add(new BasicNameValuePair("longew", LibCGlobalVariables.longew));
            nameValuePairs.add(new BasicNameValuePair("latdeg", LibCGlobalVariables.latdeg));
            nameValuePairs.add(new BasicNameValuePair("latmin", LibCGlobalVariables.latmin));
            nameValuePairs.add(new BasicNameValuePair("latns", LibCGlobalVariables.latns));
            nameValuePairs.add(new BasicNameValuePair("dst", LibCGlobalVariables.dst));
            nameValuePairs.add(new BasicNameValuePair("ayanamsa", LibCGlobalVariables.ayanamsa));
        }
        LibCGlobalVariables.userDetailAvailble = false;

        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);

        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String data = "";
        while ((data = in.readLine()) != null)
            sb.append(data);
        in.close();
        strReturn = sb.toString();
        int result = parseResponse(strReturn);

        return result;
    }*/

    /*
     * This function is used to send feed back with app version
     */
   /* public static int sendFeedbackToAstroSageWithVesrion(Context ctx, String apppName, String name, String emailId, String phone, String message,
                                                         String appVerName, String language, String activityName) throws Exception {
        BufferedReader in = null;
        String strReturn = "";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
        HttpClient httpclient = new DefaultHttpClient(getHttpClientTimeoutParameter());

        HttpPost httppost = new HttpPost(LibCGlobalVariables.FEED_BACK);
        nameValuePairs.add(new BasicNameValuePair("feedbackfrom", apppName));

        nameValuePairs.add(new BasicNameValuePair("appvesrion", appVerName));//ADDED BY BIJENDRA ON 01-MAY-15nameValuePairs.add(new BasicNameValuePair("appvesrion", appVerName));//ADDED BY BIJENDRA ON 01-MAY-15
        nameValuePairs.add(new BasicNameValuePair("key", getApplicationSignatureHashCode(ctx)));
        nameValuePairs.add(new BasicNameValuePair("feedbackpersonname", name));
        nameValuePairs.add(new BasicNameValuePair(LibCGlobalVariables.KEY_EMAIL_ID, replaceEmailChar(emailId)));
        nameValuePairs.add(new BasicNameValuePair("phoneno", phone));
        nameValuePairs.add(new BasicNameValuePair("message", message));
        nameValuePairs.add(new BasicNameValuePair("modelname", android.os.Build.MODEL));//ADDED BY TEJINDER ON 28-DEC-2015
        nameValuePairs.add(new BasicNameValuePair("brandname", android.os.Build.BRAND));//ADDED BY TEJINDER ON 28-DEC-2015
        nameValuePairs.add(new BasicNameValuePair("osversion", android.os.Build.VERSION.RELEASE));//ADDED BY TEJINDER ON 28-DEC-2015
        nameValuePairs.add(new BasicNameValuePair("sdkversion", "" + android.os.Build.VERSION.SDK_INT));//ADDED BY TEJINDER ON 28-DEC-2015
        nameValuePairs.add(new BasicNameValuePair("languagecode", language));//ADDED BY TEJINDER ON 28-DEC-2015
        nameValuePairs.add(new BasicNameValuePair("activityname", activityName));

        if (LibCGlobalVariables.userDetailAvailble) {
            nameValuePairs.add(new BasicNameValuePair(LibCGlobalVariables.KEY_USER_ID, replaceEmailChar(LibCGlobalVariables.useridsession)));
            nameValuePairs.add(new BasicNameValuePair("name", LibCGlobalVariables.name));
            nameValuePairs.add(new BasicNameValuePair("day", LibCGlobalVariables.day));
            nameValuePairs.add(new BasicNameValuePair("month", LibCGlobalVariables.month));
            nameValuePairs.add(new BasicNameValuePair("year", LibCGlobalVariables.year));
            nameValuePairs.add(new BasicNameValuePair("hour", LibCGlobalVariables.hour));
            nameValuePairs.add(new BasicNameValuePair("min", LibCGlobalVariables.min));
            nameValuePairs.add(new BasicNameValuePair("sec", LibCGlobalVariables.sec));
            nameValuePairs.add(new BasicNameValuePair("place", LibCGlobalVariables.place));
            nameValuePairs.add(new BasicNameValuePair("timezone", LibCGlobalVariables.timezone));
            nameValuePairs.add(new BasicNameValuePair("longdeg", LibCGlobalVariables.longdeg));
            nameValuePairs.add(new BasicNameValuePair("longmin", LibCGlobalVariables.longmin));
            nameValuePairs.add(new BasicNameValuePair("longew", LibCGlobalVariables.longew));
            nameValuePairs.add(new BasicNameValuePair("latdeg", LibCGlobalVariables.latdeg));
            nameValuePairs.add(new BasicNameValuePair("latmin", LibCGlobalVariables.latmin));
            nameValuePairs.add(new BasicNameValuePair("latns", LibCGlobalVariables.latns));
            nameValuePairs.add(new BasicNameValuePair("dst", LibCGlobalVariables.dst));
            nameValuePairs.add(new BasicNameValuePair("ayanamsa", LibCGlobalVariables.ayanamsa));
        }
        LibCGlobalVariables.userDetailAvailble = false;
        //  //Log.e("here@@@@@",""+nameValuePairs);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);

        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String data = "";
        while ((data = in.readLine()) != null)
            sb.append(data);
        in.close();
        strReturn = sb.toString();
        int result = parseResponse(strReturn);

        return result;
    }*/

    public static int parseResponse(String strData) throws Exception {
        int returnStr = -1;
        if (strData.contains("<msgcode>")) {
            returnStr = Integer.valueOf(strData.substring(strData.indexOf("<msgcode>") + "<msgcode>".length(), strData.indexOf("</msgcode>")).trim());
        }
        return returnStr;
    }


    public static String getApplicationSignatureHashCode(Context context) {

        String key = "";
        try {
            Signature[] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            for (Signature sig : sigs) {
                //Log.i("Signature hashcode : " + sig.hashCode());
                key = String.valueOf(sig.hashCode());
                //Log.e("Key is #####" + key);
                //Log.e(key);

            }
        } catch (Exception e) {

        }
        //return "-1489918760";
        return key;
    }

    public static void saveIntData(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntData(Context context, String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int value = preferences.getInt(key, defaultValue);
        return value;
    }

    public static String replaceEmailChar(String emailId) {

        try {
            int xCharacterAhead = 1;
            emailId = encodeWithAsciiValue(emailId, xCharacterAhead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailId;
    }

    /**
     * Method to encode String With Ascii Value
     *
     * @param stringToEncode
     * @param XAheadCharacter
     * @return
     */
    static String encodeWithAsciiValue(String stringToEncode, int XAheadCharacter) {
        // changed string
        String newString = "";
        // iterate for every characters
        for (int iterator = 0; iterator < stringToEncode.length(); ++iterator) {
            // ASCII value
            int val = stringToEncode.charAt(iterator);
            // store the duplicate
            newString += (char) (val + XAheadCharacter);
        }
        // return the new string
        return newString;
    }


    public static void vollyPostRequest(Context context, VolleyResponse volleyResponse, String url, Map<String, String> params, int method) {
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, volleyResponse, true, params, method).getMyStringRequest();
        queue.add(stringRequest);
    }

    public static void vollyGetRequest(Context context, VolleyResponse volleyResponse, String url, int method) {
        HashMap<String, String> params = new HashMap<String, String>();
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.GET, url, volleyResponse, true, params, method).getMyStringRequest();
        queue.add(stringRequest);
    }

    public static List<CMessage> parseXML(String response) {
        List<CMessage> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {

            // auto-detect the encoding from the stream
            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();
            CMessage currentMessage = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        messages = new ArrayList<CMessage>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(LibCGlobalVariables.ITEM)) {
                            currentMessage = new CMessage();
                        } else if (currentMessage != null) {
                            if (name.equalsIgnoreCase(LibCGlobalVariables.LINK)) {
                                currentMessage.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.DESCRIPTION)) {
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.PUB_DATE)) {
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.GUID)) {//ADDED BY DEEPAK ON 16-12-2014
                                currentMessage.setPostId(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(LibCGlobalVariables.ITEM) &&
                                currentMessage != null) {
                            messages.add(currentMessage);
                        } else if (name.equalsIgnoreCase(LibCGlobalVariables.CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
            Log.i("size==", messages.size() + "");
        } catch (Exception e) {
            messages = null;
        }
        return messages;
    }

    public static List<CMessage> parseYearlyXML(String response) {
        List<CMessage> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
            //parser.setInput(this.getInputStream(context), "UTF-8");
            parser.setInput(new StringReader(response));
            int eventType = parser.getEventType();
            CMessage currentMessage = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        messages = new ArrayList<CMessage>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(LibCGlobalVariables.ITEM)) {
                            currentMessage = new CMessage();
                        } else if (currentMessage != null) {
                            if (name.equalsIgnoreCase(LibCGlobalVariables.LINK)) {
                                currentMessage.setLink(parser.nextText());
                            }/* else if (name.equalsIgnoreCase(DESCRIPTION)) {
                               // currentMessage.setDescription(parser.nextText());

                            }*/ else if (name.equalsIgnoreCase(LibCGlobalVariables.PUB_DATE)) {
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.GUID)) {//ADDED BY DEEPAK ON 16-12-2014
                                currentMessage.setPostId(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.HEALTH)) {
                                currentMessage.setHealth(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.CAREER)) {
                                currentMessage.setCareer(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.Love_Marriage_PersonalRelations)) {
                                currentMessage.setLoveMarriagePersonalReltaion(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.Advice)) {
                                currentMessage.setAdvice(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.General)) {
                                currentMessage.setGeneral(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.Finance)) {
                                currentMessage.setFinance(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.Trade_Finance)) {
                                currentMessage.setTradeFinance(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.Family_Friends)) {
                                currentMessage.setFamilyFriends(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(LibCGlobalVariables.ITEM) &&
                                currentMessage != null) {
                            messages.add(currentMessage);
                        } else if (name.equalsIgnoreCase(LibCGlobalVariables.CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            throw new RuntimeException(e);
        }
        return messages;
    }


    public static List<YoutubeVideoBean> parseYoutubeRssFeedXML(String response) {
        List<YoutubeVideoBean> youtubeVideoBeanList = null;
        XmlPullParser parser = Xml.newPullParser();
        try {

            // auto-detect the encoding from the stream
            parser.setInput(new StringReader(response));

            int eventType = parser.getEventType();
            YoutubeVideoBean currentMessage = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        youtubeVideoBeanList = new ArrayList();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        //Log.d("XmlPullParser name", name);
                        if (name.equalsIgnoreCase(LibCGlobalVariables.ENTRY)) {
                            currentMessage = new YoutubeVideoBean();
                        } else if (currentMessage != null) {
                            if (name.equalsIgnoreCase(LibCGlobalVariables.DESCRIPTION)) {
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.TITLE)) {
                                currentMessage.setTitle(parser.nextText());
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.THUMBNAIL)) {
                                currentMessage.setThumbnail(parser.getAttributeValue(null, URL));
                            } else if (name.equalsIgnoreCase(LibCGlobalVariables.VIDEO_ID)) {
                                currentMessage.setVideoId(parser.nextText());
                            }else if (name.equalsIgnoreCase(LibCGlobalVariables.PUBLISHED_DATE)) {
                                currentMessage.setPublishedAt(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(LibCGlobalVariables.ENTRY) &&
                                currentMessage != null) {
                            youtubeVideoBeanList.add(currentMessage);
                        } else if (name.equalsIgnoreCase(LibCGlobalVariables.CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                //Log.d("XmlPullParser name", "#################################");
                eventType = parser.next();
            }
            Log.i("size==", youtubeVideoBeanList.size() + "");
        } catch (Exception e) {
            youtubeVideoBeanList = null;
        }
        return youtubeVideoBeanList;
    }

    private static final AtomicInteger notificationCounter = new AtomicInteger((int) (System.currentTimeMillis() & 0x0FFFFFFF));
    public static int getRandomNumber(){
        try {
            return notificationCounter.incrementAndGet();
        }catch (Exception e){
            return (int) System.currentTimeMillis();
        }
    }
}
